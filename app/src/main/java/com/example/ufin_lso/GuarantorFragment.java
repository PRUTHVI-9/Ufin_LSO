package com.example.ufin_lso;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class GuarantorFragment extends Fragment implements View.OnClickListener {
    View rootView;
    MainActivity mainActivity;

    private static final int SCAN_REQUEST           = -1;
    int ScanRequest = -1;
    String Aadhaaruid, AadhaarName, AadhaarGender, AadhaarYearOfBirth, AadhaarCareOf;
    String AadhaarHouse, AadhaarLandmark, AadhaarLocality, AadhaarVillageTownCity, AadhaarPostOffice;
    String AadhaarDistrict, AadhaarSubDistrict, AadhaarState, AadhaarPinCode, AadhaarDOB;

    int IDRequest = -1;

    private long RelationIDSpouse;
    private long RelationIDMainMember;
    private int RelationIndexMainMember;

    private boolean dbMaster = false;
    private ArrayList<Long> RelationIDMaster = new ArrayList<Long>();
    private ArrayList<String> RelationMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Long> BusinessIDMaster = new ArrayList<Long>();
    private ArrayList<String> BusinessMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Long> OtherGuarantorReasonIDMaster = new ArrayList<Long>();
    private ArrayList<String> OtherGuarantorReasonMaster = new ArrayList<String>();

    private boolean GuarantorIsFamilyMember = false;
    private boolean GuarantorIsFamilyMemberOtherThanSpouse = false;
    int GuarantorFamilyMemberIndex;

//    private ArrayList<Integer> GuarantorMemberIndexMaster = new ArrayList<Integer>();
//    private ArrayList<String> GuarantorMaster = new ArrayList<String>();
//    int selectedGuarantorIndex;
//    int selectedGuarantorPosition = -1;

    public GuarantorFragment() {
// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_guarantor, container, false);
        mainActivity = (MainActivity) getActivity();

        if(!dbMaster) {
            SQLite_getRelationMaster();
            SQLite_getBusinessMaster();
            SQLite_getReasonMaster();
            dbMaster = true;
        }

        GuarantorIsFamilyMember = false;
        for(int i=1; i<mainActivity.Member.size(); i++) {
            if(mainActivity.Member.get(i).IsActive == 1) {
                if(mainActivity.AdditionalDetails.LoanGuarantorID ==
                        mainActivity.Member.get(i).MemberID) {
                    GuarantorIsFamilyMember = true;
                    if(mainActivity.Member.get(i).RelationID != RelationIDSpouse)
                        GuarantorIsFamilyMemberOtherThanSpouse = true;
                    break;
                }
            }
        }

//        GuarantorMemberIndexMaster.clear();
//        GuarantorMemberIndexMaster.add(-1);
//        GuarantorMaster.clear();
//        GuarantorMaster.add("");
//        for(int i=1; i<mainActivity.Member.size(); i++) {
//            if(mainActivity.Member.get(i).IsActive == 1) {
//                if(mainActivity.Member.get(i).MemberID
//                        != mainActivity.AdditionalDetails.JointLoaneeID) {
//                    GuarantorMemberIndexMaster.add(i);
//                    GuarantorMaster.add(mainActivity.Member.get(i).Fname);
//                }
//            }
//        }
//        GuarantorMemberIndexMaster.add(-1);
//        GuarantorMaster.add("इतर जामीनदार");
//
//        ArrayAdapter<String> adapterGuarantor = new ArrayAdapter<String>(getActivity().getBaseContext(),
//                R.layout.simple_spinner_item, GuarantorMaster);
//        adapterGuarantor.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
//        Spinner sp1 = (Spinner) rootView.findViewById(R.id.spinnerGuarantor);
//        sp1.setAdapter(adapterGuarantor);
//        sp1.setSelection(0);
//        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (mainActivity.FragmentVisibleToUser !=
//                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_GUARANTOR)
//                    return;
//                if (mainActivity.iCurrentScreen !=
//                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_GUARANTOR)
//                    return;
//                if(selectedGuarantorPosition == position)
//                    return;
//
//                selectedGuarantorPosition = position;
//
//                if(position == 0) {
//                    //Guarantor Not Selected
//                    GuarantorIsFamilyMember = false;
//                    GuarantorFamilyMemberIndex = -1;
////                    if(!mainActivity.GuarantorRequired)
//                    if(false)
//                        mainActivity.AdditionalDetails.LoanGuarantorID = -1;
//                    else
//                        mainActivity.AdditionalDetails.LoanGuarantorID = 0;
//                } else if(position == (GuarantorMaster.size() - 1)) {
//                    //Other Guarantor
//                    GuarantorIsFamilyMember = false;
//                    GuarantorFamilyMemberIndex = -1;
//                    mainActivity.AdditionalDetails.LoanGuarantorID = 0;
//                } else  {
//                    //Family Member Guarantor
//                    GuarantorIsFamilyMember = true;
//                    GuarantorFamilyMemberIndex = GuarantorMemberIndexMaster.get(position);
//                    mainActivity.AdditionalDetails.LoanGuarantorID =
//                            mainActivity.Member.get(GuarantorFamilyMemberIndex).MemberID;
//                }
//
//                RefreshScreen();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });

        EditText et = (EditText) rootView.findViewById(R.id.editTextGuarantorFname);
        if(GuarantorIsFamilyMember){
            et.setEnabled(false);
        } else {
            et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                            (actionId == EditorInfo.IME_ACTION_NEXT)) {
                        mainActivity.AdditionalDetails.OtherGuarantorFName = v.getText().toString();
                    }
                    return false;
                }
            });
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                    } else {
                        if (mainActivity.iCurrentScreen !=
                                mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_GUARANTOR)
                            return;
                        mainActivity.AdditionalDetails.OtherGuarantorFName = ((EditText) view).getText().toString();
                    }
                }
            });
        }
////bmp 26-Dec-24
//        et.setEnabled(false);
////bmp 26-Dec-24
        et = (EditText) rootView.findViewById(R.id.editTextGuarantorMname);
        if(GuarantorIsFamilyMember){
            et.setEnabled(false);
        } else {
            et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                            (actionId == EditorInfo.IME_ACTION_NEXT)) {
                        mainActivity.AdditionalDetails.OtherGuarantorMName = v.getText().toString();
                    }
                    return false;
                }
            });
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                    } else {
                        if (mainActivity.iCurrentScreen !=
                                mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_GUARANTOR)
                            return;
                        mainActivity.AdditionalDetails.OtherGuarantorMName = ((EditText) view).getText().toString();
                    }
                }
            });
        }
////bmp 26-Dec-24
//        et.setEnabled(false);
////bmp 26-Dec-24
        et = (EditText) rootView.findViewById(R.id.editTextGuarantorLname);
        if(GuarantorIsFamilyMember){
            et.setEnabled(false);
        } else {
            et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                            (actionId == EditorInfo.IME_ACTION_NEXT)) {
                        mainActivity.AdditionalDetails.OtherGuarantorLName = v.getText().toString();
                    }
                    return false;
                }
            });
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                    } else {
                        if (mainActivity.iCurrentScreen !=
                                mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_GUARANTOR)
                            return;
                        mainActivity.AdditionalDetails.OtherGuarantorLName = ((EditText) view).getText().toString();
                    }
                }
            });
        }
////bmp 26-Dec-24
//        et.setEnabled(false);
////bmp 26-Dec-24
        et = (EditText) rootView.findViewById(R.id.editTextAddress);
        if(GuarantorIsFamilyMember){
            et.setEnabled(false);
        } else {
            et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                            (actionId == EditorInfo.IME_ACTION_NEXT)) {
                        mainActivity.AdditionalDetails.GuarantorAddress = v.getText().toString();
                    }
                    return false;
                }
            });
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                    } else {
                        if (mainActivity.iCurrentScreen !=
                                mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_GUARANTOR)
                            return;
                        mainActivity.AdditionalDetails.GuarantorAddress = ((EditText) view).getText().toString();
                    }
                }
            });
        }
////bmp 26-Dec-24
//        et.setEnabled(false);
////bmp 26-Dec-24
        et = (EditText) rootView.findViewById(R.id.editTextGuarantorPincode);
        if(GuarantorIsFamilyMember){
            et.setEnabled(false);
        } else {
            et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                            (actionId == EditorInfo.IME_ACTION_NEXT)) {
                        mainActivity.AdditionalDetails.GuarantorAddressPincode = v.getText().toString();
                    }
                    return false;
                }
            });
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                    } else {
                        if (mainActivity.iCurrentScreen !=
                                mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_GUARANTOR)
                            return;
                        mainActivity.AdditionalDetails.GuarantorAddressPincode = ((EditText) view).getText().toString();
                    }
                }
            });
        }
////bmp 26-Dec-24
//        et.setEnabled(false);
////bmp 26-Dec-24
        et = (EditText) rootView.findViewById(R.id.editTextGuarantorAge);
        if(GuarantorIsFamilyMember) {
            et.setEnabled(false);
        } else {
            et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                            (actionId == EditorInfo.IME_ACTION_NEXT)) {
                        try {
                            int iAge = Integer.parseInt(v.getText().toString());
                            Calendar CalendarDOB = Calendar.getInstance();
                            int iYear = CalendarDOB.get(Calendar.YEAR);
                            int iYearOfBirth = iYear - iAge;
                            CalendarDOB.set(Calendar.YEAR, iYearOfBirth);
                            CalendarDOB.set(Calendar.MONTH, Calendar.JANUARY);
                            CalendarDOB.set(Calendar.DATE, 1);
                            String strBirthDate = String.format("%04d", CalendarDOB.get(Calendar.YEAR)) + "-"
                                    + String.format("%02d", (CalendarDOB.get(Calendar.MONTH) + 1)) + "-"
                                    + String.format("%02d", CalendarDOB.get(Calendar.DATE));
                            mainActivity.AdditionalDetails.OtherGuarantorBirthDate = strBirthDate;
                        } catch (NumberFormatException e) {
                        }
                    }
                    return false;
                }
            });
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                    } else {
                        if (mainActivity.iCurrentScreen !=
                                mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_GUARANTOR)
                            return;
                        try {
                            int iAge = Integer.parseInt(((EditText) view).getText().toString());
                            Calendar CalendarDOB = Calendar.getInstance();
                            int iYear = CalendarDOB.get(Calendar.YEAR);
                            int iYearOfBirth = iYear - iAge;
                            CalendarDOB.set(Calendar.YEAR, iYearOfBirth);
                            CalendarDOB.set(Calendar.MONTH, Calendar.JANUARY);
                            CalendarDOB.set(Calendar.DATE, 1);
                            String strBirthDate = String.format("%04d", CalendarDOB.get(Calendar.YEAR)) + "-"
                                    + String.format("%02d", (CalendarDOB.get(Calendar.MONTH) + 1)) + "-"
                                    + String.format("%02d", CalendarDOB.get(Calendar.DATE));
                            mainActivity.AdditionalDetails.OtherGuarantorBirthDate = strBirthDate;
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            });
        }
////bmp 26-Dec-24
//        et.setEnabled(false);
////bmp 26-Dec-24
        et = (EditText) rootView.findViewById(R.id.editTextMobile);
        if(GuarantorIsFamilyMember){
            et.setEnabled(false);
        } else {
            et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                            (actionId == EditorInfo.IME_ACTION_NEXT)) {
                        String strTemp = v.getText().toString();
                        if(strTemp == null)
                            strTemp = "0";
                        else if(strTemp.isEmpty())
                            strTemp = "0";
                        else if(strTemp.length() < 10)
                            strTemp = "0";
                        mainActivity.AdditionalDetails.OtherGuarantorContactNumber = strTemp;
                    }
                    return false;
                }
            });
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                    } else {
                        if (mainActivity.iCurrentScreen !=
                                mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_GUARANTOR)
                            return;
                        String strTemp = ((EditText) view).getText().toString();
                        if(strTemp == null)
                            strTemp = "0";
                        else if(strTemp.isEmpty())
                            strTemp = "0";
                        else if(strTemp.length() < 10)
                            strTemp = "0";
                        mainActivity.AdditionalDetails.OtherGuarantorContactNumber = strTemp;
                    }
                }
            });
        }
        ArrayAdapter<String> adapterRelation = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, RelationMarathiDescriptionMaster);
        adapterRelation.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerRelation3);
        sp.setAdapter(adapterRelation);
        if(GuarantorIsFamilyMember){
            sp.setEnabled(false);
        } else {
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (mainActivity.FragmentVisibleToUser !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_GUARANTOR)
                        return;
                    if (mainActivity.iCurrentScreen !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_GUARANTOR)
                        return;
                    if(RelationIDMaster.get(position) != RelationIDMainMember) {
                        mainActivity.AdditionalDetails.OtherGuarantorRelationID = RelationIDMaster.get(position);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
        ArrayAdapter<String> adapterBusiness = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, BusinessMarathiDescriptionMaster);
        adapterBusiness.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupation);
        sp.setAdapter(adapterBusiness);
        if(GuarantorIsFamilyMember){
            sp.setEnabled(false);
        } else {
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (mainActivity.FragmentVisibleToUser !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_GUARANTOR)
                        return;
                    if (mainActivity.iCurrentScreen !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_GUARANTOR)
                        return;
                    mainActivity.AdditionalDetails.OtherGuarantorBusinessID = BusinessIDMaster.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
        et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncome);
        if(GuarantorIsFamilyMember){
            et.setEnabled(false);
        } else {
            et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                            (actionId == EditorInfo.IME_ACTION_NEXT)) {
                        mainActivity.AdditionalDetails.OtherGuarantorMonthlyIncome = Integer.valueOf(v.getText().toString());
                    }
                    return false;
                }
            });
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                    } else {
                        if (mainActivity.iCurrentScreen !=
                                mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_GUARANTOR)
                            return;
                        mainActivity.AdditionalDetails.OtherGuarantorMonthlyIncome = Integer.valueOf(((EditText) view).getText().toString());
                    }
                }
            });
        }

//        Button btn = (Button) rootView.findViewById(R.id.buttonScanGuarantor);
//        btn.setOnClickListener(this);
//        if(GuarantorIsFamilyMember){
//            btn.setEnabled(false);
//        } else {
//            btn.setEnabled(true);
//        }

        ArrayAdapter<String> adapterOtherGuarantorReason = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, OtherGuarantorReasonMaster);
        adapterOtherGuarantorReason.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerReasonForOtherGuarantor);
        sp.setAdapter(adapterOtherGuarantorReason);
        if(GuarantorIsFamilyMember && (!GuarantorIsFamilyMemberOtherThanSpouse)){
            sp.setEnabled(false);
        } else {
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (mainActivity.FragmentVisibleToUser !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_GUARANTOR)
                        return;
                    if (mainActivity.iCurrentScreen !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_GUARANTOR)
                        return;
                    mainActivity.AdditionalDetails.OtherGuarantorReasonID = OtherGuarantorReasonIDMaster.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        RefreshScreen();

        return rootView;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
//            case R.id.buttonScanGuarantor:
//                try {
//                    ScanRequest = -1;
//                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
//                    startActivityForResult(intent, SCAN_REQUEST);
//                } catch (Exception e) {
//                }
//                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            if(mainActivity == null)
                return;

            mainActivity.currentMemberIndex = 0;
            mainActivity.FragmentVisibleToUser = mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_GUARANTOR;
            RefreshScreen();
        }
    }

    public void RefreshScreen() {
        int indexGuarantor = -1;
        for(int i=0; i<mainActivity.Member.size(); i++) {
            if(mainActivity.Member.get(i).IsActive == 1) {
                if(mainActivity.AdditionalDetails.LoanGuarantorID == mainActivity.Member.get(i).MemberID) {
                    indexGuarantor = i;
                    break;
                }
            }
        }

//        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerGuarantor);
////        if(!mainActivity.GuarantorRequired) {
//        if(false) {
//            sp.setEnabled(false);
//            sp.setSelection(0);
//        } else {
//            if(mainActivity.AdditionalDetails.LoanGuarantorID == 0) {
//                GuarantorIsFamilyMember = false;
//                sp.setSelection(GuarantorMemberIndexMaster.size()-1);
//            } else {
//                for(int k=0; k<GuarantorMemberIndexMaster.size(); k++) {
//                    if(GuarantorMemberIndexMaster.get(k) < 0)
//                        continue;
//                    int iIndex = GuarantorMemberIndexMaster.get(k);
//                    if(mainActivity.AdditionalDetails.LoanGuarantorID ==
//                            mainActivity.Member.get(iIndex).MemberID) {
//                        GuarantorIsFamilyMember = true;
//                        GuarantorFamilyMemberIndex = iIndex;
//                        sp.setSelection(k);
//                        break;
//                    }
//                }
//            }
//            sp.setEnabled(true);
//        }

        String strFname = "";
        String strMname = "";
        String strLname = "";
        long lRelation = -1;
        String strAddress = "";
        String strPincode = "";
        String strBirthDate = "";
        long lOccupation = -1;
        String strContactNumber = "";
        int iMonthlyIncome = 0;
        if(indexGuarantor > 0) {
            strFname = mainActivity.Member.get(indexGuarantor).Fname;
            strMname = mainActivity.Member.get(indexGuarantor).Mname;
            strLname = mainActivity.Member.get(indexGuarantor).Lname;
            lRelation = mainActivity.Member.get(indexGuarantor).RelationID;
            strAddress = mainActivity.MemberAddress.Address;
            strPincode = mainActivity.MemberAddress.Pincode;

            strBirthDate = mainActivity.Member.get(indexGuarantor).BirthDate;

            lOccupation = mainActivity.PersonalImpactInfo.get(indexGuarantor).BusinessID;
            strContactNumber = mainActivity.Member.get(indexGuarantor).ContactNo;
            iMonthlyIncome = mainActivity.PersonalImpactInfo.get(indexGuarantor).MonthlyIncome;
        } else {
            strFname = mainActivity.AdditionalDetails.OtherGuarantorFName;
            strMname = mainActivity.AdditionalDetails.OtherGuarantorMName;
            strLname = mainActivity.AdditionalDetails.OtherGuarantorLName;
            lRelation = mainActivity.AdditionalDetails.OtherGuarantorRelationID;
            strAddress = mainActivity.AdditionalDetails.GuarantorAddress;
            strPincode = mainActivity.AdditionalDetails.GuarantorAddressPincode;

            strBirthDate = mainActivity.AdditionalDetails.OtherGuarantorBirthDate;

            lOccupation = mainActivity.AdditionalDetails.OtherGuarantorBusinessID;
            strContactNumber = mainActivity.AdditionalDetails.OtherGuarantorContactNumber;
            iMonthlyIncome = mainActivity.AdditionalDetails.OtherGuarantorMonthlyIncome;
//bmp 20-Feb-23
//            String strFilepath = mainActivity.baseDir + "/data/";
//            ImageButton imgbtn = (ImageButton) rootView.findViewById(R.id.imageButtonOtherGuarantorPhoto);
//            String strDocPhotoID = "";
//            for(int i=0; i<mainActivity.MemberDocRcvd.size(); i++) {
//                if( (mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == mainActivity.DocumentIDs.MEMBER_DOC_ID_OTHER_GUARANTOR_PHOTO) &&
//                        (mainActivity.MemberDocRcvd.get(i).IsActive == 1) &&
//                        (mainActivity.MemberDocRcvd.get(i).MemberID == mainActivity.Member.get(0).MemberID) ) {
//                    for(int k=0; k<mainActivity.DocumentPhotos.size(); k++) {
//                        if(     (mainActivity.DocumentPhotos.get(k).MemberDocuemntsRcvdID ==
//                                mainActivity.MemberDocRcvd.get(i).MemberDocuemntsRcvdID) &&
//                                (mainActivity.DocumentPhotos.get(k).IsActive == 1) ) {
//                            String strTemp = String.valueOf(mainActivity.DocumentPhotos.get(k).DocumentPhotoID);
//                            File file = new File(strFilepath + strTemp + ".png");
//                            if(file.exists()) {
//                                strDocPhotoID = strTemp;
//                                break;
//                            }
//                        }
//                    }
//                }
//                if(strDocPhotoID.length() > 0)
//                    break;
//            }
//            if(strDocPhotoID.length() > 0) {
//                String strFilename = strDocPhotoID + ".png";
//                if(!strFilename.contains(".png")) {
//                    strFilename = strFilename + ".png";
//                }
//                BitmapFactory.Options options = new BitmapFactory.Options();
////            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                options.inPreferredConfig = Bitmap.Config.RGB_565;
//                options.inSampleSize = 4;
//                try {
//                    Bitmap bitmap = BitmapFactory.decodeFile((strFilepath + strFilename), options);
////                imgbtn.setImageBitmap(bitmap);
//                    if(bitmap != null) {
//                        int iHt = bitmap.getHeight();
//                        int iWd = bitmap.getWidth();
//                        if (iHt < iWd) {
//                            Matrix matrix = new Matrix();
//                            matrix.postRotate(90);
//                            Bitmap rotate_bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//                            bitmap.recycle();
//                            imgbtn.setImageBitmap(rotate_bitmap);
//                        } else {
//                            imgbtn.setImageBitmap(bitmap);
//                        }
//                    } else {
//                        imgbtn.setImageBitmap(bitmap);
//                    }
//                } catch (OutOfMemoryError e) {
//                    System.out.println(e.getMessage());
//                }
//            }
//
//            imgbtn = (ImageButton) rootView.findViewById(R.id.imageButtonOtherGuarantorIDPhoto);
//            strDocPhotoID = "";
//            for(int i=0; i<mainActivity.MemberDocRcvd.size(); i++) {
//                if( (mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == mainActivity.DocumentIDs.MEMBER_DOC_ID_OTHER_GUARANTOR_PHOTOID) &&
//                        (mainActivity.MemberDocRcvd.get(i).IsActive == 1) &&
//                        (mainActivity.MemberDocRcvd.get(i).MemberID == mainActivity.Member.get(0).MemberID) ) {
//                    for(int k=0; k<mainActivity.DocumentPhotos.size(); k++) {
//                        if(     (mainActivity.DocumentPhotos.get(k).MemberDocuemntsRcvdID ==
//                                mainActivity.MemberDocRcvd.get(i).MemberDocuemntsRcvdID) &&
//                                (mainActivity.DocumentPhotos.get(k).IsActive == 1) ) {
//                            String strTemp = String.valueOf(mainActivity.DocumentPhotos.get(k).DocumentPhotoID);
//                            File file = new File(strFilepath + strTemp + ".png");
//                            if(file.exists()) {
//                                strDocPhotoID = strTemp;
//                                break;
//                            }
//                        }
//                    }
//                }
//                if(strDocPhotoID.length() > 0)
//                    break;
//            }
//            if(strDocPhotoID.length() > 0) {
//                String strFilename = strDocPhotoID + ".png";
//                if(!strFilename.contains(".png")) {
//                    strFilename = strFilename + ".png";
//                }
//                BitmapFactory.Options options = new BitmapFactory.Options();
////            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                options.inPreferredConfig = Bitmap.Config.RGB_565;
//                options.inSampleSize = 4;
//                try {
//                    Bitmap bitmap = BitmapFactory.decodeFile((strFilepath + strFilename), options);
////                imgbtn.setImageBitmap(bitmap);
//                    if(bitmap != null) {
//                        int iHt = bitmap.getHeight();
//                        int iWd = bitmap.getWidth();
//                        if (iHt < iWd) {
//                            Matrix matrix = new Matrix();
//                            matrix.postRotate(90);
//                            Bitmap rotate_bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//                            bitmap.recycle();
//                            imgbtn.setImageBitmap(rotate_bitmap);
//                        } else {
//                            imgbtn.setImageBitmap(bitmap);
//                        }
//                    } else {
//                        imgbtn.setImageBitmap(bitmap);
//                    }
//                } catch (OutOfMemoryError e) {
//                    System.out.println(e.getMessage());
//                }
//            }
//bmp 20-Feb-23
        }

        TextView tv = (TextView) rootView.findViewById(R.id.editTextGuarantorFname);
        tv.setText(strFname);
        tv = (TextView) rootView.findViewById(R.id.editTextGuarantorMname);
        tv.setText(strMname);
        tv = (TextView) rootView.findViewById(R.id.editTextGuarantorLname);
        tv.setText(strLname);
        tv = (TextView) rootView.findViewById(R.id.editTextAddress);
        tv.setText(strAddress);
        tv = (TextView) rootView.findViewById(R.id.editTextGuarantorPincode);
        tv.setText(strPincode);

        int iAge = 0;
        try {
            if(strBirthDate.length() > 10)
                strBirthDate = strBirthDate.substring(0, 10);
            String[] strSplit = strBirthDate.split("-");
            Calendar dob = Calendar.getInstance();
            dob.set(Calendar.YEAR, Integer.parseInt(strSplit[0]));
            dob.set(Calendar.MONTH, (Integer.parseInt(strSplit[1]) - 1));
            dob.set(Calendar.DATE, Integer.parseInt(strSplit[2]));
            Calendar today = Calendar.getInstance();
            iAge = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
                iAge--;
            }
        }catch (Exception e){
        }
        tv = (TextView) rootView.findViewById(R.id.editTextGuarantorAge);
        tv.setText(String.valueOf(iAge));

        tv = (TextView) rootView.findViewById(R.id.editTextMobile);
        tv.setText(strContactNumber);
        tv = (TextView) rootView.findViewById(R.id.editTextMonthlyIncome);
        tv.setText(String.valueOf(iMonthlyIncome));
        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerRelation3);
        sp.setSelection(0);
        for(int i=0; i<RelationIDMaster.size(); i++) {
            if(RelationIDMaster.get(i) == lRelation) {
                sp.setSelection(i);
                break;
            }
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupation);
        sp.setSelection(0);
        for(int i=0; i<BusinessIDMaster.size(); i++) {
            if(BusinessIDMaster.get(i) == lOccupation) {
                sp.setSelection(i);
                break;
            }
        }

        sp = (Spinner) rootView.findViewById(R.id.spinnerReasonForOtherGuarantor);
        for(int i=0; i<OtherGuarantorReasonIDMaster.size(); i++) {
            if(OtherGuarantorReasonIDMaster.get(i) == mainActivity.AdditionalDetails.OtherGuarantorReasonID) {
                sp.setSelection(i);
            }
        }

        tv = (TextView) rootView.findViewById(R.id.textViewRequestFocus);
        tv.requestFocus();
    }

    public void SQLite_getReasonMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonID,
                ufinDbContract.ReasonMaster.COLUMN_NAME_Reason,
                ufinDbContract.ReasonMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.ReasonMaster.COLUMN_NAME_SequenceID,
        };
        String Active = "1";
        String strReasonType = "Other Guarantor reason";
        String selection1 = ufinDbContract.ReasonMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'" +
                " AND " + ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonType + "=" + "'" + strReasonType + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.ReasonMaster.COLUMN_NAME_SequenceID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.ReasonMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        OtherGuarantorReasonIDMaster.clear();
        OtherGuarantorReasonMaster.clear();
        OtherGuarantorReasonIDMaster.add(0L);
        OtherGuarantorReasonMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            String Str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_MarathiDescription));
                            if ((Str1 == null) || (Str1.equalsIgnoreCase(" ")) || (Str1.equalsIgnoreCase(""))) {
                            } else {
                                OtherGuarantorReasonIDMaster.add(Long.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonID))));
                                OtherGuarantorReasonMaster.add(Str1);
                            }
                            c1.moveToNext();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            if(c1 != null) { c1.close(); }
            db.close();
        }
    }
    public void SQLite_getRelationMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.RelationMaster.COLUMN_NAME_RelationID,
                ufinDbContract.RelationMaster.COLUMN_NAME_Description,
                ufinDbContract.RelationMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.RelationMaster.COLUMN_NAME_SequenceID,
        };
        String Active = "1";

        String selection1 = ufinDbContract.RelationMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.RelationMaster.COLUMN_NAME_SequenceID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.RelationMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        RelationIDMaster.clear();
        RelationMarathiDescriptionMaster.clear();
        RelationIDMaster.add(0L);
        RelationMarathiDescriptionMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            RelationIDMaster.add(Long.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.RelationMaster.COLUMN_NAME_RelationID))));
                            String str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.RelationMaster.COLUMN_NAME_MarathiDescription));
                            if (str1 == null) {
                                str1 = "?";
                            }
                            RelationMarathiDescriptionMaster.add(str1);
                            String str2 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.RelationMaster.COLUMN_NAME_Description));
                            if(str2.equalsIgnoreCase("Main Member")) {
                                RelationIndexMainMember = RelationIDMaster.size() - 1;
                                RelationIDMainMember = RelationIDMaster.get(RelationIndexMainMember);
                            } else if(str2.equalsIgnoreCase("Spouse")) {
                                RelationIDSpouse = RelationIDMaster.get(RelationIDMaster.size()-1);
                            }
                            c1.moveToNext();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            if(c1 != null) { c1.close(); }
            db.close();
        }
    }
    public void SQLite_getBusinessMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.BusinessMaster.COLUMN_NAME_BusinessID,
                ufinDbContract.BusinessMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.BusinessMaster.COLUMN_NAME_SequenceID,
        };
        String Active = "1";

        String selection1 = ufinDbContract.BusinessMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.BusinessMaster.COLUMN_NAME_SequenceID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.BusinessMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        BusinessIDMaster.clear();
        BusinessMarathiDescriptionMaster.clear();
        BusinessIDMaster.add(0L);
        BusinessMarathiDescriptionMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            BusinessIDMaster.add(Long.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.BusinessMaster.COLUMN_NAME_BusinessID))));
                            String str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.BusinessMaster.COLUMN_NAME_MarathiDescription));
                            if (str1 == null) {
                                str1 = "?";
                            }
                            BusinessMarathiDescriptionMaster.add(str1);
                            c1.moveToNext();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            if(c1 != null) { c1.close(); }
            db.close();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCAN_REQUEST:
                if (resultCode == RESULT_OK) {
                    String contents = data.getStringExtra("SCAN_RESULT");
                    System.out.println(contents);
                    String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                    System.out.println(format);

                    ProcessScannedData(contents);

                    RefreshScreen();
                }
                break;
        }
        if(resultCode == RESULT_CANCELED){
            //handle cancel
            if (mainActivity.iCurrentScreen == mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_GUARANTOR) {
                ShowdlgAlertonActivityResult("SCAN REQUEST CANCELED!");
            }
        }
    }

    void ShowdlgAlertonActivityResult(String strMessage) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getContext());
        TextView tvTitle = new TextView(getContext());
        tvTitle.setText(getString(R.string.app_name));
        tvTitle.setTextSize(30);
        tvTitle.setTextColor(getResources().getColor(R.color.colorAccent));
        dlgAlert.setCustomTitle(tvTitle);
        dlgAlert.setIcon(R.drawable.ic_launcher_foreground);
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                dialog.dismiss();
            }
        });
        dlgAlert.setCancelable(true);

        dlgAlert.setMessage(strMessage);
        AlertDialog dialog = dlgAlert.create();
        dialog.show();
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(30);
    }

    void ProcessScannedData(String scanData) {
        XmlPullParserFactory xmlFactoryObject;
        try {
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();
            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myparser.setInput(new StringReader(scanData));
            int eventType = myparser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                String name= myparser.getName();
                //myparser.getAttributeValue()
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if(myparser.getName().equalsIgnoreCase("PrintLetterBarcodeData")) {
                            Aadhaaruid = myparser.getAttributeValue(null, "uid");
                            long lMemberID = mainActivity.SQLite_getMemberID("AADHAARCARD", Aadhaaruid);
                            if(lMemberID <= 0) {
                            } else {
                                //Existing member, check loan status
                            }

                            AadhaarName = myparser.getAttributeValue(null, "name");
                            mainActivity.AdditionalDetails.OtherGuarantorFName = "";
                            mainActivity.AdditionalDetails.OtherGuarantorMName = "";
                            mainActivity.AdditionalDetails.OtherGuarantorLName = "";
                            if(lMemberID <= 0) {
                                AadhaarName = AadhaarName.trim();
                                String[] strSplit = AadhaarName.split(" ");
                                switch (strSplit.length) {
                                    case 0:
                                        mainActivity.AdditionalDetails.OtherGuarantorFName = "";
                                        mainActivity.AdditionalDetails.OtherGuarantorMName = "";
                                        mainActivity.AdditionalDetails.OtherGuarantorLName = "";
                                        break;
                                    case 1:
                                        mainActivity.AdditionalDetails.OtherGuarantorFName = strSplit[0];
                                        mainActivity.AdditionalDetails.OtherGuarantorMName = "";
                                        mainActivity.AdditionalDetails.OtherGuarantorLName = "";
                                        break;
                                    case 2:
                                        mainActivity.AdditionalDetails.OtherGuarantorFName = strSplit[0];
                                        mainActivity.AdditionalDetails.OtherGuarantorMName = "";
                                        mainActivity.AdditionalDetails.OtherGuarantorLName = strSplit[1];
                                        break;
                                    case 3:
                                        mainActivity.AdditionalDetails.OtherGuarantorFName = strSplit[0];
                                        mainActivity.AdditionalDetails.OtherGuarantorMName = strSplit[1];
                                        mainActivity.AdditionalDetails.OtherGuarantorLName = strSplit[2];
                                        break;
                                    default:
                                        mainActivity.AdditionalDetails.OtherGuarantorFName = AadhaarName;
                                        mainActivity.AdditionalDetails.OtherGuarantorMName = AadhaarName;
                                        mainActivity.AdditionalDetails.OtherGuarantorLName = AadhaarName;
                                        break;
                                }
                            } else {
                                //Existing member, check loan status
                            }
                            AadhaarGender = myparser.getAttributeValue(null, "gender");
                            if(lMemberID <= 0) {
                            } else {
                                //Existing member, check loan status
                            }
                            int iAadhaarYearOfBirth = 0;
                            AadhaarYearOfBirth = myparser.getAttributeValue(null, "yob");
                            try {
                                iAadhaarYearOfBirth = Integer.parseInt(AadhaarYearOfBirth);
                            } catch (NumberFormatException e) {
                            }
                            AadhaarDOB = myparser.getAttributeValue(null, "dob");
                            if(AadhaarDOB == null) {
                                AadhaarDOB = "";
                                Calendar CalendarDOB = Calendar.getInstance();
                                int iYear = CalendarDOB.get(Calendar.YEAR);
                                int iYearOfBirth = iAadhaarYearOfBirth;
                                CalendarDOB.set(Calendar.YEAR, iYearOfBirth);
                                CalendarDOB.set(Calendar.MONTH, Calendar.JANUARY);
                                CalendarDOB.set(Calendar.DATE, 1);
                                String strBirthDate = String.format("%04d", CalendarDOB.get(Calendar.YEAR)) + "-"
                                        + String.format("%02d", (CalendarDOB.get(Calendar.MONTH) + 1)) + "-"
                                        + String.format("%02d", CalendarDOB.get(Calendar.DATE));
                                if(lMemberID <= 0) {
                                    mainActivity.AdditionalDetails.OtherGuarantorBirthDate = strBirthDate;
                                } else {
                                    //Existing member, check loan status
                                }
                            } else {
                                if(lMemberID <= 0) {
                                    mainActivity.AdditionalDetails.OtherGuarantorBirthDate = AadhaarDOB;
                                } else {
                                    //Existing member, check loan status
                                }
                            }

                            AadhaarCareOf = myparser.getAttributeValue(null, "co");

                            AadhaarHouse = myparser.getAttributeValue(null, "house");
                            if(AadhaarHouse == null)
                                AadhaarHouse = "";
                            AadhaarLandmark = myparser.getAttributeValue(null, "lm");
                            if(AadhaarLandmark == null)
                                AadhaarLandmark = "";
                            AadhaarLocality = myparser.getAttributeValue(null, "loc");
                            if(AadhaarLocality == null)
                                AadhaarLocality = "";
                            AadhaarVillageTownCity = myparser.getAttributeValue(null, "vtc");
                            if(AadhaarVillageTownCity == null)
                                AadhaarVillageTownCity = "";
                            AadhaarPostOffice = myparser.getAttributeValue(null, "po");
                            if(AadhaarPostOffice == null)
                                AadhaarPostOffice = "";
                            AadhaarDistrict = myparser.getAttributeValue(null, "dist");
                            if(AadhaarDistrict == null)
                                AadhaarDistrict = "";
                            AadhaarSubDistrict = myparser.getAttributeValue(null, "subdist");
                            if(AadhaarSubDistrict == null)
                                AadhaarSubDistrict = "";
                            AadhaarState = myparser.getAttributeValue(null, "state");
                            if(AadhaarState == null)
                                AadhaarState = "";
                            AadhaarPinCode = myparser.getAttributeValue(null, "pc");
                            if(AadhaarPinCode == null)
                                AadhaarPinCode = "0";
                            mainActivity.AdditionalDetails.GuarantorAddress = AadhaarHouse + " " + AadhaarLandmark + " " + AadhaarLocality + " "
                                    + AadhaarVillageTownCity /*+ " " + AadhaarPostOffice*/ + " " + AadhaarSubDistrict + " " + AadhaarState;
                            mainActivity.AdditionalDetails.GuarantorAddressPincode = AadhaarPinCode;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = myparser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
