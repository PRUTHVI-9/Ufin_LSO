package com.example.ufin_lso;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class RepayFragment extends Fragment implements View.OnClickListener {
    View rootView;
    MainActivity mainActivity;

    private boolean dbMaster = false;
    private ArrayList<Integer> LoanProductMasterID = new ArrayList<Integer>();
    private ArrayList<Integer> LoanProductDetailsIDMaster = new ArrayList<Integer>();
    private ArrayList<Integer> LoanProductIDMaster = new ArrayList<Integer>();
    private ArrayList<Boolean> JointAccountHolderRequiredMaster = new ArrayList<Boolean>();
    private ArrayList<String> PrincipalAmtMaster = new ArrayList<String>();
    private ArrayList<String> LoanPeriodInMonthsMaster = new ArrayList<String>();
    private ArrayList<String> TotalMonthlyInstallmentAmtMaster = new ArrayList<String>();
    private ArrayList<String> Principal_Period_Installment_Master = new ArrayList<String>();
    private ArrayList<Integer> RelationIDMaster = new ArrayList<Integer>();
    private ArrayList<String> RelationMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Integer> BusinessIDMaster = new ArrayList<Integer>();
    private ArrayList<String> BusinessMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Long> OtherJointLoaneeReasonIDMaster = new ArrayList<Long>();
    private ArrayList<String> OtherJointLoaneeReasonMaster = new ArrayList<String>();

    private static final int SCAN_REQUEST           = -1;
    int ScanRequest = -1;
    String Aadhaaruid, AadhaarName, AadhaarGender, AadhaarYearOfBirth, AadhaarCareOf;
    String AadhaarHouse, AadhaarLandmark, AadhaarLocality, AadhaarVillageTownCity, AadhaarPostOffice;
    String AadhaarDistrict, AadhaarSubDistrict, AadhaarState, AadhaarPinCode, AadhaarDOB;

    int IDRequest = -1;

    private int RelationIDSpouse;
    private int RelationIDMainMember;
    private int RelationIndexMainMember;

    int iGroupMemberIndex;
    int selectedLoanProductID;
    int selectedPrincipalAmt;
    int selectedLoanPeriodInMonths;
    int selectedTotalMonthlyInstallmentAmt;
    int selectedPosition = -1;
    boolean Reloan;
    boolean JointAccountHolderIsFamilyMember;
    boolean JointAccountHolderIsFamilyMemberOtherThanSpouse;
    int JointAccountHolderFamilyMemberIndex;

    private ArrayList<Integer> JointLoaneeMemberIndexMaster = new ArrayList<Integer>();
    private ArrayList<String> JointLoaneeMaster = new ArrayList<String>();
    int selectedJointLoaneeIndex;
    int selectedJointLoaneePosition = -1;

    private ArrayList<Integer> GuarantorMemberIndexMaster = new ArrayList<Integer>();
    private ArrayList<String> GuarantorMaster = new ArrayList<String>();
    int selectedGuarantorIndex;
    int selectedGuarantorPosition = -1;

    private boolean GuarantorIsFamilyMember = false;
    private boolean GuarantorIsFamilyMemberOtherThanSpouse = false;
    int GuarantorFamilyMemberIndex;

    public RepayFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_repay, container, false);
        mainActivity = (MainActivity) getActivity();

        if(!dbMaster) {
            SQLite_getRelationMaster();
            SQLite_getBusinessMaster();
            SQLite_getReasonMaster();
            dbMaster = true;
        }

        for(int i=0; i<mainActivity.newGroup.arrGroupMember.size(); i++) {
            if(mainActivity.Member.get(0).MemberID == mainActivity.newGroup.arrGroupMember.get(i).MemberID) {
                iGroupMemberIndex = i;
                break;
            }
        }

        JointLoaneeMemberIndexMaster.clear();
        JointLoaneeMemberIndexMaster.add(-1);
        JointLoaneeMaster.clear();
        JointLoaneeMaster.add("");
        for(int i=1; i<mainActivity.Member.size(); i++) {
            if(mainActivity.Member.get(i).IsActive == 1) {
                JointLoaneeMemberIndexMaster.add(i);
//                JointLoaneeMaster.add(mainActivity.Member.get(i).Fname);
                String strJointLoaneeInfo = mainActivity.Member.get(i).Fname;
                String strRelation = "";
                for(int k=0; k<RelationIDMaster.size(); k++) {
                    if(mainActivity.Member.get(i).RelationID == RelationIDMaster.get(k)) {
                        strRelation = RelationMarathiDescriptionMaster.get(k);
                        break;
                    }
                }
                strJointLoaneeInfo = strJointLoaneeInfo + ":" + strRelation;
                JointLoaneeMaster.add(strJointLoaneeInfo);
            }
        }
        JointLoaneeMemberIndexMaster.add(-1);
        JointLoaneeMaster.add("इतर सहकर्जदार");

        ArrayAdapter<String> adapterJointLoanee = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item_small, JointLoaneeMaster);
        adapterJointLoanee.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
        Spinner sp1 = (Spinner) rootView.findViewById(R.id.spinnerJointLoanee);
        sp1.setAdapter(adapterJointLoanee);
        sp1.setSelection(0);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
                    return;
                if(selectedJointLoaneePosition == position)
                    return;

                selectedJointLoaneePosition = position;

                if(position == 0) {
                    //Joint Loanee Not Selected
                    JointAccountHolderIsFamilyMember = false;
                    JointAccountHolderFamilyMemberIndex = -1;
                    if(!mainActivity.JointAccountHolderRequired)
                        mainActivity.AdditionalDetails.JointLoaneeID = -1;
                    else
                        mainActivity.AdditionalDetails.JointLoaneeID = 0;
                } else if(position == (JointLoaneeMaster.size() - 1)) {
                    //Other Joint Loanee
                    JointAccountHolderIsFamilyMember = false;
                    JointAccountHolderFamilyMemberIndex = -1;
                    mainActivity.AdditionalDetails.JointLoaneeID = 0;
                } else  {
                    //Family Member Joint Loanee
                    JointAccountHolderIsFamilyMember = true;
                    JointAccountHolderFamilyMemberIndex = JointLoaneeMemberIndexMaster.get(position);
                    mainActivity.AdditionalDetails.JointLoaneeID =
                            mainActivity.Member.get(JointAccountHolderFamilyMemberIndex).MemberID;
                }

                RefreshScreen();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        GuarantorMemberIndexMaster.clear();
        GuarantorMemberIndexMaster.add(-1);
        GuarantorMaster.clear();
        GuarantorMaster.add("");
        for(int i=1; i<mainActivity.Member.size(); i++) {
            if(mainActivity.Member.get(i).IsActive == 1) {
//                if(mainActivity.Member.get(i).MemberID
//                        != mainActivity.AdditionalDetails.JointLoaneeID) {
                    GuarantorMemberIndexMaster.add(i);
//                    GuarantorMaster.add(mainActivity.Member.get(i).Fname);
                String strGuarantorInfo = mainActivity.Member.get(i).Fname;
                String strRelation = "";
                for(int k=0; k<RelationIDMaster.size(); k++) {
                    if(mainActivity.Member.get(i).RelationID == RelationIDMaster.get(k)) {
                        strRelation = RelationMarathiDescriptionMaster.get(k);
                        break;
                    }
                }
                strGuarantorInfo = strGuarantorInfo + ":" + strRelation;
                GuarantorMaster.add(strGuarantorInfo);
//                }
            }
        }
        GuarantorMemberIndexMaster.add(-1);
        GuarantorMaster.add("इतर जामीनदार");

        ArrayAdapter<String> adapterGuarantor = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item_small, GuarantorMaster);
        adapterGuarantor.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
        sp1 = (Spinner) rootView.findViewById(R.id.spinnerGuarantor);
        sp1.setAdapter(adapterGuarantor);
        sp1.setSelection(0);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
                    return;
                if(selectedGuarantorPosition == position)
                    return;

                selectedGuarantorPosition = position;

                if(position == 0) {
                    //Guarantor Not Selected
                    GuarantorIsFamilyMember = false;
                    GuarantorFamilyMemberIndex = -1;
//                    if(!mainActivity.GuarantorRequired)
                    if(false)
                        mainActivity.AdditionalDetails.LoanGuarantorID = -1;
                    else
                        mainActivity.AdditionalDetails.LoanGuarantorID = 0;
                } else if(position == (GuarantorMaster.size() - 1)) {
                    //Other Guarantor
                    GuarantorIsFamilyMember = false;
                    GuarantorFamilyMemberIndex = -1;
                    mainActivity.AdditionalDetails.LoanGuarantorID = 0;
                } else  {
                    //Family Member Guarantor
                    GuarantorIsFamilyMember = true;
                    GuarantorFamilyMemberIndex = GuarantorMemberIndexMaster.get(position);
                    mainActivity.AdditionalDetails.LoanGuarantorID =
                            mainActivity.Member.get(GuarantorFamilyMemberIndex).MemberID;
                }

                RefreshScreen();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        EditText et = (EditText) rootView.findViewById(R.id.editTextJointLoaneeFname);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    mainActivity.AdditionalDetails.OtherJointLoaneeFname = v.getText().toString();
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
                        return;
                    mainActivity.AdditionalDetails.OtherJointLoaneeFname = ((EditText) view).getText().toString();
                }
            }
        });

        et = (EditText) rootView.findViewById(R.id.editTextJointLoaneeMname);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    mainActivity.AdditionalDetails.OtherJointLoaneeMname = v.getText().toString();
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
                        return;
                    mainActivity.AdditionalDetails.OtherJointLoaneeMname = ((EditText) view).getText().toString();
                }
            }
        });

        et = (EditText) rootView.findViewById(R.id.editTextJointLoaneeLname);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    mainActivity.AdditionalDetails.OtherJointLoaneeLname = v.getText().toString();
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
                        return;
                    mainActivity.AdditionalDetails.OtherJointLoaneeLname = ((EditText) view).getText().toString();
                }
            }
        });

        et = (EditText) rootView.findViewById(R.id.editTextAddressJointLoanee);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    String strOtherJointLoaneeAddress = v.getText().toString();
                    mainActivity.AdditionalDetails.OtherJointLoaneeAddress = strOtherJointLoaneeAddress;
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
                        return;
                    String strOtherJointLoaneeAddress = ((EditText) view).getText().toString();
                    mainActivity.AdditionalDetails.OtherJointLoaneeAddress = strOtherJointLoaneeAddress;
                }
            }
        });

        et = (EditText) rootView.findViewById(R.id.editTextPincodeJointLoanee);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    String strOtherJointLoaneePincode = v.getText().toString();
                    mainActivity.AdditionalDetails.OtherJointLoaneePincode = strOtherJointLoaneePincode;
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
                        return;
                    String strOtherJointLoaneePincode = ((EditText) view).getText().toString();
                    mainActivity.AdditionalDetails.OtherJointLoaneePincode = strOtherJointLoaneePincode;
                }
            }
        });

        et = (EditText) rootView.findViewById(R.id.editTextMobileJointLoanee);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    mainActivity.AdditionalDetails.OtherJointLoaneeContactNumber = v.getText().toString();
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
                        return;
                    mainActivity.AdditionalDetails.OtherJointLoaneeContactNumber = ((EditText) view).getText().toString();
                }
            }
        });

        et = (EditText) rootView.findViewById(R.id.editTextAgeJointLoanee);
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
                        mainActivity.AdditionalDetails.OtherJointLoaneeBirthDate = strBirthDate;
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
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
                        mainActivity.AdditionalDetails.OtherJointLoaneeBirthDate = strBirthDate;
                    } catch (NumberFormatException e) {
                    }
                }
            }
        });

        et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncomeJointLoanee);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    mainActivity.AdditionalDetails.OtherJointLoaneeMonthlyIncome = Integer.valueOf(v.getText().toString());
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
                        return;
                    mainActivity.AdditionalDetails.OtherJointLoaneeMonthlyIncome = Integer.parseInt(((EditText) view).getText().toString());
                }
            }
        });

        ArrayAdapter<String> adapterRelation = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, RelationMarathiDescriptionMaster);
        adapterRelation.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerRelationJointLoanee);
        sp.setAdapter(adapterRelation);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
                    return;
                if(RelationIDMaster.get(position) != RelationIDMainMember) {
                    mainActivity.AdditionalDetails.OtherJointLoaneeRelationID = RelationIDMaster.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<String> adapterBusiness = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, BusinessMarathiDescriptionMaster);
        adapterBusiness.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationJointLoanee);
        sp.setAdapter(adapterBusiness);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
                    return;
                mainActivity.AdditionalDetails.OtherJointLoaneeProfessionID = BusinessIDMaster.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<String> adapterOtherJointLoaneeReason = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, OtherJointLoaneeReasonMaster);
        adapterOtherJointLoaneeReason.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerReasonForOtherJointLoanee);
        sp.setAdapter(adapterOtherJointLoaneeReason);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
                    return;
                mainActivity.AdditionalDetails.OtherJointLoaneeReasonID = OtherJointLoaneeReasonIDMaster.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
//            case R.id.buttonScanOtherJointLoanee:
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
            mainActivity.FragmentVisibleToUser = mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY;
            RefreshScreen();
        }
    }

    public void RefreshScreen() {
        TextView tv = (TextView) rootView.findViewById(R.id.editTextMonthlyIncome);
        tv.setText(String.valueOf(mainActivity.FamilyImpactInfo.FamilyTotalIncome));
        tv = (TextView) rootView.findViewById(R.id.editTextMonthlyExpense);
        tv.setText(String.valueOf(mainActivity.ExpenseTotal));

//bmp 29-Mar-21
//In case User navigates to Repay screen from last screen using "<":
//Since Income screen is not crossed mainActivity.FamilyImpactInfo.FamilyTotalIncome is not calculated;
//Since Expense screen is not crossed mainActivity.ExpenseTotal is not calculated;
        int iFamilySize = 0;
        for(int i=0; i<mainActivity.Member.size(); i++) {
            if(mainActivity.Member.get(i).IsActive == 1) {
                iFamilySize++;
            }
        }
        mainActivity.FamilyImpactInfo.FamilyTotalIncome = mainActivity.FamilyImpactInfo.PerCapitaIncome * iFamilySize;
        mainActivity.ExpenseTotal =
                mainActivity.FamilyImpactInfo.HouseRentExpense +
                        mainActivity.FamilyImpactInfo.FoodExpense +
                        mainActivity.FamilyImpactInfo.EducationExpense +
                        mainActivity.MemberFamilyDetails.MedicalExpenses +
                        mainActivity.FamilyImpactInfo.OtherExpenses +
                        mainActivity.MemberFamilyDetails.MonthlyBhishi +
                        mainActivity.MemberFamilyDetails.MonthlyInstallments;
//        for(int i=0; i<mainActivity.OtherMFIData.size(); i++) {
//            mainActivity.ExpenseTotal = mainActivity.ExpenseTotal + mainActivity.OtherMFIData.get(i).MonthlyInstallment;
//        }
//bmp 29-Mar-21
        mainActivity.AdditionalDetails.Monthly_Surplus = mainActivity.FamilyImpactInfo.FamilyTotalIncome - mainActivity.ExpenseTotal;
        tv = (TextView) rootView.findViewById(R.id.editTextMonthlySurplus);
        tv.setText(String.valueOf(mainActivity.AdditionalDetails.Monthly_Surplus));

//        Reloan = mainActivity.SQLite_getMemberLoanStatus(mainActivity.selectedMemberID);
        Reloan = true;

        SQLite_getLoanProductMaster();
        SQLite_getLoanProductDetailsMaster();
        JointAccountHolderRequiredMaster.clear();
        JointAccountHolderRequiredMaster.add(false);
        for(int i=1; i<LoanProductIDMaster.size(); i++) {
            boolean boolFlag = IsJointAccountHolderRequired(LoanProductIDMaster.get(i));
            JointAccountHolderRequiredMaster.add(boolFlag);
        }

        String strRuppees = "रु. ";
        String strMonths = "  " + "महिने";
        String strInstallment = "  " + "हप्ता रु." + " ";
        for(int i=1; i<LoanProductDetailsIDMaster.size(); i++) {
            String strTemp =    strRuppees + PrincipalAmtMaster.get(i) + "/- "
                    + LoanPeriodInMonthsMaster.get(i) + strMonths
                    + strInstallment + TotalMonthlyInstallmentAmtMaster.get(i) + "/-";

            boolean boolFlag = JointAccountHolderRequiredMaster.get(i);
            if(boolFlag)
                strTemp = strTemp + " *";

            Principal_Period_Installment_Master.add(strTemp);
        }

        for(int i=1; i<LoanProductDetailsIDMaster.size(); i++) {
            if(mainActivity.AdditionalDetails.LoanProductDetailsID == LoanProductDetailsIDMaster.get(i)) {
                selectedPosition = i;

                selectedPrincipalAmt = Integer.valueOf(PrincipalAmtMaster.get(selectedPosition));
                mainActivity.newGroup.arrGroupMember.get(iGroupMemberIndex).LoanAmount = selectedPrincipalAmt;
                mainActivity.AdditionalDetails.LoanAmount = selectedPrincipalAmt;
                tv = (TextView) rootView.findViewById(R.id.textViewLoanAmount1);
                tv.setText(PrincipalAmtMaster.get(selectedPosition));

                selectedLoanPeriodInMonths = Integer.valueOf(LoanPeriodInMonthsMaster.get(selectedPosition));
                mainActivity.newGroup.arrGroupMember.get(iGroupMemberIndex).LoanDuration = selectedLoanPeriodInMonths;
                mainActivity.AdditionalDetails.LoanDuration = selectedLoanPeriodInMonths;
                tv = (TextView) rootView.findViewById(R.id.textViewLoanPeriod1);
                tv.setText(LoanPeriodInMonthsMaster.get(selectedPosition));

                selectedTotalMonthlyInstallmentAmt = Integer.valueOf(TotalMonthlyInstallmentAmtMaster.get(selectedPosition));
                tv = (TextView) rootView.findViewById(R.id.textViewLoanInstallment1);
                tv.setText(TotalMonthlyInstallmentAmtMaster.get(selectedPosition));

                selectedLoanProductID = LoanProductIDMaster.get(selectedPosition);
                mainActivity.JointAccountHolderRequired = IsJointAccountHolderRequired(selectedLoanProductID);
                if(!mainActivity.JointAccountHolderRequired)
                    mainActivity.AdditionalDetails.JointLoaneeID = -1;
                break;
            }
        }
        for(int i=1; i<LoanProductDetailsIDMaster.size(); i++) {
            if(mainActivity.AdditionalDetails.LoanProductDetailsID2 == LoanProductDetailsIDMaster.get(i)) {
                tv = (TextView) rootView.findViewById(R.id.textViewLoanAmount2);
                tv.setText(PrincipalAmtMaster.get(i));
                tv = (TextView) rootView.findViewById(R.id.textViewLoanPeriod2);
                tv.setText(LoanPeriodInMonthsMaster.get(i));
                tv = (TextView) rootView.findViewById(R.id.textViewLoanInstallment2);
                tv.setText(TotalMonthlyInstallmentAmtMaster.get(i));
                break;
            }
        }
        for(int i=1; i<LoanProductDetailsIDMaster.size(); i++) {
            if(mainActivity.AdditionalDetails.LoanProductDetailsID3 == LoanProductDetailsIDMaster.get(i)) {
                tv = (TextView) rootView.findViewById(R.id.textViewLoanAmount3);
                tv.setText(PrincipalAmtMaster.get(i));
                tv = (TextView) rootView.findViewById(R.id.textViewLoanPeriod3);
                tv.setText(LoanPeriodInMonthsMaster.get(i));
                tv = (TextView) rootView.findViewById(R.id.textViewLoanInstallment3);
                tv.setText(TotalMonthlyInstallmentAmtMaster.get(i));
                break;
            }
        }

        ArrayAdapter<String> adapterLoanAmount = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item_small, Principal_Period_Installment_Master);
        adapterLoanAmount.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
        Spinner sp1 = (Spinner) rootView.findViewById(R.id.spinnerPrincipal_Period_Installment);
        sp1.setAdapter(adapterLoanAmount);
        if(     ((selectedPosition > 0) && (selectedPosition >= Principal_Period_Installment_Master.size())) ||
                ((selectedPosition > 0) && (selectedLoanProductID != LoanProductIDMaster.get(selectedPosition))) ||
                ((selectedPosition > 0) && (mainActivity.AdditionalDetails.LoanProductDetailsID != LoanProductDetailsIDMaster.get(selectedPosition))) ) {
            selectedPosition = -1;
            selectedPrincipalAmt = 0;
            mainActivity.newGroup.arrGroupMember.get(iGroupMemberIndex).LoanAmount = selectedPrincipalAmt;
            mainActivity.AdditionalDetails.LoanAmount = selectedPrincipalAmt;
            tv = (TextView) rootView.findViewById(R.id.textViewLoanAmount1);
            tv.setText("0");
            tv = (TextView) rootView.findViewById(R.id.textViewLoanAmount2);
            tv.setText("0");
            tv = (TextView) rootView.findViewById(R.id.textViewLoanAmount3);
            tv.setText("0");

            selectedLoanPeriodInMonths = 0;
            mainActivity.newGroup.arrGroupMember.get(iGroupMemberIndex).LoanDuration = selectedLoanPeriodInMonths;
            mainActivity.AdditionalDetails.LoanDuration = selectedLoanPeriodInMonths;
            tv = (TextView) rootView.findViewById(R.id.textViewLoanPeriod1);
            tv.setText("0");
            tv = (TextView) rootView.findViewById(R.id.textViewLoanPeriod2);
            tv.setText("0");
            tv = (TextView) rootView.findViewById(R.id.textViewLoanPeriod3);
            tv.setText("0");

            selectedTotalMonthlyInstallmentAmt = 0;
            tv = (TextView) rootView.findViewById(R.id.textViewLoanInstallment1);
            tv.setText("0");
            tv = (TextView) rootView.findViewById(R.id.textViewLoanInstallment2);
            tv.setText("0");
            tv = (TextView) rootView.findViewById(R.id.textViewLoanInstallment3);
            tv.setText("0");

            selectedLoanProductID = 0;
            mainActivity.JointAccountHolderRequired = IsJointAccountHolderRequired(selectedLoanProductID);
            if(!mainActivity.JointAccountHolderRequired)
                mainActivity.AdditionalDetails.JointLoaneeID = -1;

            mainActivity.AdditionalDetails.LoanProductDetailsID = 0;
            mainActivity.AdditionalDetails.LoanProductDetailsID1 = 0;
            mainActivity.AdditionalDetails.LoanProductDetailsID2 = 0;
            mainActivity.AdditionalDetails.LoanProductDetailsID3 = 0;
            mainActivity.AdditionalDetails.LoanProductDetailsID4 = 0;
        }
        sp1.setSelection(selectedPosition);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
                    return;
                if( (position > 0) && (position != selectedPosition)) {
                    selectedPosition = position;
                    selectedPrincipalAmt = Integer.valueOf(PrincipalAmtMaster.get(position));
                    mainActivity.newGroup.arrGroupMember.get(iGroupMemberIndex).LoanAmount = selectedPrincipalAmt;
                    mainActivity.AdditionalDetails.LoanAmount = selectedPrincipalAmt;
                    TextView tv = (TextView) rootView.findViewById(R.id.textViewLoanAmount1);
                    tv.setText(PrincipalAmtMaster.get(position));

                    selectedLoanPeriodInMonths = Integer.valueOf(LoanPeriodInMonthsMaster.get(position));
                    mainActivity.newGroup.arrGroupMember.get(iGroupMemberIndex).LoanDuration = selectedLoanPeriodInMonths;
                    mainActivity.AdditionalDetails.LoanDuration = selectedLoanPeriodInMonths;
                    tv = (TextView) rootView.findViewById(R.id.textViewLoanPeriod1);
                    tv.setText(LoanPeriodInMonthsMaster.get(position));

                    selectedTotalMonthlyInstallmentAmt = Integer.valueOf(TotalMonthlyInstallmentAmtMaster.get(position));
                    tv = (TextView) rootView.findViewById(R.id.textViewLoanInstallment1);
                    tv.setText(TotalMonthlyInstallmentAmtMaster.get(position));

                    selectedLoanProductID = LoanProductIDMaster.get(position);
                    mainActivity.JointAccountHolderRequired = IsJointAccountHolderRequired(selectedLoanProductID);
                    if(!mainActivity.JointAccountHolderRequired)
                        mainActivity.AdditionalDetails.JointLoaneeID = -1;

                    mainActivity.AdditionalDetails.LoanProductDetailsID = LoanProductDetailsIDMaster.get(position);
//bmp 31-Oct-23
                    //With Community Meeting Full Attendance Incentive
                    mainActivity.AdditionalDetails.LoanProductDetailsID1 = mainActivity.AdditionalDetails.LoanProductDetailsID;

                    mainActivity.AdditionalDetails.LoanProductDetailsID2 = 0;
                    tv = (TextView) rootView.findViewById(R.id.textViewLoanAmount2);
                    tv.setText(String.valueOf(selectedPrincipalAmt));
                    tv = (TextView) rootView.findViewById(R.id.textViewLoanPeriod2);
                    tv.setText(String.valueOf(selectedLoanPeriodInMonths));
                    tv = (TextView) rootView.findViewById(R.id.textViewLoanInstallment2);
                    tv.setText(String.valueOf(selectedTotalMonthlyInstallmentAmt));

                    mainActivity.AdditionalDetails.LoanProductDetailsID3 = 0;
                    tv = (TextView) rootView.findViewById(R.id.textViewLoanAmount3);
                    tv.setText(String.valueOf(selectedPrincipalAmt));
                    tv = (TextView) rootView.findViewById(R.id.textViewLoanPeriod3);
                    tv.setText(String.valueOf(selectedLoanPeriodInMonths));
                    tv = (TextView) rootView.findViewById(R.id.textViewLoanInstallment3);
                    tv.setText(String.valueOf(selectedTotalMonthlyInstallmentAmt));

                    //With Community Meeting Half Attendance Incentive
//                    int iLoanAmountHalfAttendance = selectedPrincipalAmt - 5000;
                    int iLoanAmountHalfAttendance = selectedPrincipalAmt - mainActivity.AdditionalDetails.incentiveCommunityMeetingPartAttendance;
                    for(int k=1; k<PrincipalAmtMaster.size(); k++) {
                        int iPrincipalAmt = Integer.valueOf(PrincipalAmtMaster.get(k));
                        if(iPrincipalAmt == iLoanAmountHalfAttendance) {
                            if(selectedLoanPeriodInMonths == Integer.valueOf(LoanPeriodInMonthsMaster.get(k))) {
                                mainActivity.AdditionalDetails.LoanProductDetailsID2 = LoanProductDetailsIDMaster.get(k);
                                tv = (TextView) rootView.findViewById(R.id.textViewLoanAmount2);
                                tv.setText(PrincipalAmtMaster.get(k));
                                tv = (TextView) rootView.findViewById(R.id.textViewLoanPeriod2);
                                tv.setText(LoanPeriodInMonthsMaster.get(k));
                                tv = (TextView) rootView.findViewById(R.id.textViewLoanInstallment2);
                                tv.setText(TotalMonthlyInstallmentAmtMaster.get(k));

                                mainActivity.AdditionalDetails.LoanProductDetailsID3 = LoanProductDetailsIDMaster.get(k);
                                tv = (TextView) rootView.findViewById(R.id.textViewLoanAmount3);
                                tv.setText(PrincipalAmtMaster.get(k));
                                tv = (TextView) rootView.findViewById(R.id.textViewLoanPeriod3);
                                tv.setText(LoanPeriodInMonthsMaster.get(k));
                                tv = (TextView) rootView.findViewById(R.id.textViewLoanInstallment3);
                                tv.setText(TotalMonthlyInstallmentAmtMaster.get(k));
                                break;
                            }
                        }
                    }

                    //With Community Meeting No Attendance Incentive
//                    int iLoanAmountNoAttendance = selectedPrincipalAmt - 10000;
                    int iLoanAmountNoAttendance = selectedPrincipalAmt - mainActivity.AdditionalDetails.incentiveCommunityMeetingFullAttendance;
                    for(int k=1; k<PrincipalAmtMaster.size(); k++) {
                        int iPrincipalAmt = Integer.valueOf(PrincipalAmtMaster.get(k));
                        if(iPrincipalAmt == iLoanAmountNoAttendance) {
                            if(selectedLoanPeriodInMonths == Integer.valueOf(LoanPeriodInMonthsMaster.get(k))) {
                                mainActivity.AdditionalDetails.LoanProductDetailsID3 = LoanProductDetailsIDMaster.get(k);
                                tv = (TextView) rootView.findViewById(R.id.textViewLoanAmount3);
                                tv.setText(PrincipalAmtMaster.get(k));
                                tv = (TextView) rootView.findViewById(R.id.textViewLoanPeriod3);
                                tv.setText(LoanPeriodInMonthsMaster.get(k));
                                tv = (TextView) rootView.findViewById(R.id.textViewLoanInstallment3);
                                tv.setText(TotalMonthlyInstallmentAmtMaster.get(k));
                                break;
                            }
                        }
                    }
//bmp 31-Oct-23
                    RefreshScreen();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerJointLoanee);
        if(!mainActivity.JointAccountHolderRequired) {
            sp.setEnabled(false);
            sp.setSelection(0);
        } else {
            if(mainActivity.AdditionalDetails.JointLoaneeID == 0) {
                JointAccountHolderIsFamilyMember = false;
                sp.setSelection(JointLoaneeMemberIndexMaster.size()-1);
            } else {
                for(int k=0; k<JointLoaneeMemberIndexMaster.size(); k++) {
                    if(JointLoaneeMemberIndexMaster.get(k) < 0)
                        continue;
                    int iIndex = JointLoaneeMemberIndexMaster.get(k);
                    if(mainActivity.AdditionalDetails.JointLoaneeID ==
                            mainActivity.Member.get(iIndex).MemberID) {
                        JointAccountHolderIsFamilyMember = true;
                        JointAccountHolderFamilyMemberIndex = iIndex;
                        sp.setSelection(k);
                        break;
                    }
                }
            }
            sp.setEnabled(true);
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerGuarantor);
        if(false) {
            sp.setEnabled(false);
            sp.setSelection(0);
        } else {
            if(mainActivity.AdditionalDetails.LoanGuarantorID == 0) {
                GuarantorIsFamilyMember = false;
                sp.setSelection(GuarantorMemberIndexMaster.size()-1);
            } else {
                for(int k=0; k<GuarantorMemberIndexMaster.size(); k++) {
                    if(GuarantorMemberIndexMaster.get(k) < 0)
                        continue;
                    int iIndex = GuarantorMemberIndexMaster.get(k);
                    if(mainActivity.AdditionalDetails.LoanGuarantorID ==
                            mainActivity.Member.get(iIndex).MemberID) {
                        GuarantorIsFamilyMember = true;
                        GuarantorFamilyMemberIndex = iIndex;
                        sp.setSelection(k);
                        break;
                    }
                }
            }
            sp.setEnabled(true);
        }

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
        if(mainActivity.JointAccountHolderRequired) {
            if(JointAccountHolderIsFamilyMember) {
                strFname = mainActivity.Member.get(JointAccountHolderFamilyMemberIndex).Fname;
                strMname = mainActivity.Member.get(JointAccountHolderFamilyMemberIndex).Mname;
                strLname = mainActivity.Member.get(JointAccountHolderFamilyMemberIndex).Lname;
                lRelation = mainActivity.Member.get(JointAccountHolderFamilyMemberIndex).RelationID;
                if(lRelation == RelationIDSpouse)
                    JointAccountHolderIsFamilyMemberOtherThanSpouse = false;
                else
                    JointAccountHolderIsFamilyMemberOtherThanSpouse = true;

                strAddress = mainActivity.MemberAddress.Address;
                strPincode = mainActivity.MemberAddress.Pincode;
                strBirthDate = mainActivity.Member.get(JointAccountHolderFamilyMemberIndex).BirthDate;
                lOccupation = mainActivity.PersonalImpactInfo.get(JointAccountHolderFamilyMemberIndex).BusinessID;
                strContactNumber = mainActivity.Member.get(JointAccountHolderFamilyMemberIndex).ContactNo;
                iMonthlyIncome = mainActivity.PersonalImpactInfo.get(JointAccountHolderFamilyMemberIndex).MonthlyIncome;
            } else {
                strFname = mainActivity.AdditionalDetails.OtherJointLoaneeFname;
                strMname = mainActivity.AdditionalDetails.OtherJointLoaneeMname;
                strLname = mainActivity.AdditionalDetails.OtherJointLoaneeLname;
                lRelation = mainActivity.AdditionalDetails.OtherJointLoaneeRelationID;
                strAddress = mainActivity.AdditionalDetails.OtherJointLoaneeAddress;
                strPincode = mainActivity.AdditionalDetails.OtherJointLoaneePincode;
                strBirthDate = mainActivity.AdditionalDetails.OtherJointLoaneeBirthDate;
                lOccupation = mainActivity.AdditionalDetails.OtherJointLoaneeProfessionID;
                strContactNumber = mainActivity.AdditionalDetails.OtherJointLoaneeContactNumber;
                iMonthlyIncome = mainActivity.AdditionalDetails.OtherJointLoaneeMonthlyIncome;
            }
        }

        boolean enableChange = true;
        if(     (mainActivity.LSO_ID == 0) ||
                (!mainActivity.JointAccountHolderRequired) ||
                (JointAccountHolderIsFamilyMember) ) {
            enableChange = false;
        }
//        Button btn = (Button) rootView.findViewById(R.id.buttonScanOtherJointLoanee);
//        btn.setEnabled(enableChange);
        EditText et = (EditText) rootView.findViewById(R.id.editTextJointLoaneeFname);
        et.setText(strFname);
        et.setEnabled(enableChange);
        et = (EditText) rootView.findViewById(R.id.editTextJointLoaneeMname);
        et.setText(strMname);
        et.setEnabled(enableChange);
        et = (EditText) rootView.findViewById(R.id.editTextJointLoaneeLname);
        et.setText(strLname);
        et.setEnabled(enableChange);
        et = (EditText) rootView.findViewById(R.id.editTextAddressJointLoanee);
        et.setText(strAddress);
        et.setEnabled(enableChange);
        et = (EditText) rootView.findViewById(R.id.editTextPincodeJointLoanee);
        et.setText(strPincode);
        et.setEnabled(enableChange);
        et = (EditText) rootView.findViewById(R.id.editTextMobileJointLoanee);
        et.setText(strContactNumber);
        et.setEnabled(enableChange);
        et = (EditText) rootView.findViewById(R.id.editTextAgeJointLoanee);
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
        et.setText(String.valueOf(iAge));
        et.setEnabled(enableChange);
        et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncomeJointLoanee);
        et.setText(String.valueOf(iMonthlyIncome));
        et.setEnabled(enableChange);

        sp = (Spinner) rootView.findViewById(R.id.spinnerRelationJointLoanee);
        sp.setSelection(0);
        for(int i=0; i<RelationIDMaster.size(); i++) {
            if(RelationIDMaster.get(i) == lRelation) {
                sp.setSelection(i);
                break;
            }
        }
        sp.setEnabled(enableChange);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationJointLoanee);
        sp.setSelection(0);
        for(int i=0; i<BusinessIDMaster.size(); i++) {
            if(BusinessIDMaster.get(i) == lOccupation) {
                sp.setSelection(i);
                break;
            }
        }
        sp.setEnabled(enableChange);

        sp = (Spinner) rootView.findViewById(R.id.spinnerReasonForOtherJointLoanee);
        sp.setSelection(0);
        for(int i=0; i<OtherJointLoaneeReasonIDMaster.size(); i++) {
            if(OtherJointLoaneeReasonIDMaster.get(i) == mainActivity.AdditionalDetails.OtherJointLoaneeReasonID) {
                sp.setSelection(i);
            }
        }
        if(     (!mainActivity.JointAccountHolderRequired) ||
                (JointAccountHolderIsFamilyMember && (!JointAccountHolderIsFamilyMemberOtherThanSpouse)) ) {
            enableChange = false;
//bmp 12-Jun-23
//In case loan amount > 2,00,000/- it requires Co-Applicant
//If Spouse is Guarantor, Co-Applicant can be other family member
//Drop-Down need to be enabled to select Reason for Co-Applicant other than Spouse.
        } else {
            enableChange = true;
//bmp 12-Jun-23
        }
        sp.setEnabled(enableChange);

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
//        sp1 = (Spinner) rootView.findViewById(R.id.spinnerGuarantor);
//        sp1.setAdapter(adapterGuarantor);
//        sp1.setSelection(0);
//        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (mainActivity.FragmentVisibleToUser !=
//                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
//                    return;
//                if (mainActivity.iCurrentScreen !=
//                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY)
//                    return;
//                if(selectedGuarantorPosition == position)
//                    return;
//
//                selectedGuarantorPosition = position;
//
//                if(position == 0) {
//                    //Guarantor Not Selected
////                    GuarantorIsFamilyMember = false;
//                    GuarantorFamilyMemberIndex = -1;
////                    if(!mainActivity.GuarantorRequired)
//                    if(false)
//                        mainActivity.AdditionalDetails.LoanGuarantorID = -1;
//                    else
//                        mainActivity.AdditionalDetails.LoanGuarantorID = 0;
//                } else if(position == (GuarantorMaster.size() - 1)) {
//                    //Other Guarantor
////                    GuarantorIsFamilyMember = false;
//                    GuarantorFamilyMemberIndex = -1;
//                    mainActivity.AdditionalDetails.LoanGuarantorID = 0;
//                } else  {
//                    //Family Member Guarantor
////                    GuarantorIsFamilyMember = true;
//                    GuarantorFamilyMemberIndex = GuarantorMemberIndexMaster.get(position);
//                    mainActivity.AdditionalDetails.LoanGuarantorID =
//                            mainActivity.Member.get(GuarantorFamilyMemberIndex).MemberID;
//                }
//
////                RefreshScreen();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });

        tv = (TextView) rootView.findViewById(R.id.textViewRequestFocus);
        tv.requestFocus();
    }

    public boolean IsJointAccountHolderRequired(long lLoanProductID) {
        boolean bStatus = false;

        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.LoanProductsMaster.COLUMN_NAME_JointAccountHolderRequired,
        };
        String Active = "1";

        String selection1 = ufinDbContract.LoanProductsMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'"
        + " and " + ufinDbContract.LoanProductsMaster.COLUMN_NAME_LoanProductID + "=" + "'" + String.valueOf(lLoanProductID) + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.LoanProductsMaster.COLUMN_NAME_LoanProductID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.LoanProductsMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iTemp = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanProductsMaster.COLUMN_NAME_JointAccountHolderRequired)));
                        if (iTemp == 1)
                            bStatus = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            if(c1 != null) { c1.close(); }
            db.close();
        }

        return bStatus;
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
            if (mainActivity.iCurrentScreen == mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY) {
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
                            mainActivity.AdditionalDetails.OtherJointLoaneeFname = "";
                            mainActivity.AdditionalDetails.OtherJointLoaneeMname = "";
                            mainActivity.AdditionalDetails.OtherJointLoaneeLname = "";
                            if(lMemberID <= 0) {
                                AadhaarName = AadhaarName.trim();
                                String[] strSplit = AadhaarName.split(" ");
                                switch (strSplit.length) {
                                    case 0:
                                        mainActivity.AdditionalDetails.OtherJointLoaneeFname = "";
                                        mainActivity.AdditionalDetails.OtherJointLoaneeMname = "";
                                        mainActivity.AdditionalDetails.OtherJointLoaneeLname = "";
                                        break;
                                    case 1:
                                        mainActivity.AdditionalDetails.OtherJointLoaneeFname = strSplit[0];
                                        mainActivity.AdditionalDetails.OtherJointLoaneeMname = "";
                                        mainActivity.AdditionalDetails.OtherJointLoaneeLname = "";
                                        break;
                                    case 2:
                                        mainActivity.AdditionalDetails.OtherJointLoaneeFname = strSplit[0];
                                        mainActivity.AdditionalDetails.OtherJointLoaneeMname = "";
                                        mainActivity.AdditionalDetails.OtherJointLoaneeLname = strSplit[1];
                                        break;
                                    case 3:
                                        mainActivity.AdditionalDetails.OtherJointLoaneeFname = strSplit[0];
                                        mainActivity.AdditionalDetails.OtherJointLoaneeMname = strSplit[1];
                                        mainActivity.AdditionalDetails.OtherJointLoaneeLname = strSplit[2];
                                        break;
                                    default:
                                        mainActivity.AdditionalDetails.OtherJointLoaneeFname = AadhaarName;
                                        mainActivity.AdditionalDetails.OtherJointLoaneeMname = AadhaarName;
                                        mainActivity.AdditionalDetails.OtherJointLoaneeLname = AadhaarName;
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
                                    mainActivity.AdditionalDetails.OtherJointLoaneeBirthDate = strBirthDate;
                                } else {
                                    //Existing member, check loan status
                                }
                            } else {
                                if(lMemberID <= 0) {
                                    mainActivity.AdditionalDetails.OtherJointLoaneeBirthDate = AadhaarDOB;
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
                            mainActivity.AdditionalDetails.OtherJointLoaneeAddress = AadhaarHouse + " " + AadhaarLandmark + " " + AadhaarLocality + " "
                                    + AadhaarVillageTownCity /*+ " " + AadhaarPostOffice*/ + " " + AadhaarSubDistrict + " " + AadhaarState;
                            mainActivity.AdditionalDetails.OtherJointLoaneePincode = AadhaarPinCode;
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
    public void SQLite_getLoanProductMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.LoanProductsMaster.COLUMN_NAME_LoanProductID,
//bmp 10-Jun-24
//bmp 31-May-24
//                ufinDbContract.LoanProductsMaster.COLUMN_NAME_ProductName,
//bmp 31-May-24
                ufinDbContract.LoanProductsMaster.COLUMN_NAME_ProductName,
//bmp 10-Jun-24
                ufinDbContract.LoanProductsMaster.COLUMN_NAME_ProductStartDate,
                ufinDbContract.LoanProductsMaster.COLUMN_NAME_ProductEndDate,
        };
        String Active = "1";

        String selection1 = ufinDbContract.LoanProductsMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.LoanProductsMaster.COLUMN_NAME_LoanProductID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.LoanProductsMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        LoanProductMasterID.clear();
        LoanProductMasterID.add(0);

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
//bmp 22-Aug-25
//                        String strDate = mainActivity.newGroup.LoanDisbursementDate.substring(0, 10);
                        String strDate = mainActivity.newGroup.GroupTestDate.substring(0, 10);
//bmp 22-Aug-25
                        String[] strSplit = strDate.split("-");
                        Calendar doe = Calendar.getInstance();
                        doe.set(Calendar.YEAR, Integer.parseInt(strSplit[0]));
                        doe.set(Calendar.MONTH, (Integer.parseInt(strSplit[1]) - 1));
                        doe.set(Calendar.DATE, Integer.parseInt(strSplit[2]));
//bmp 22-Aug-25
//                        long ldoLoanDisbursement = doe.getTimeInMillis();
                        long lFiftyDays = (1000L * 60L * 60L * 24L * 50L);
                        long ldoLoanDisbursement = doe.getTimeInMillis() + lFiftyDays;
//bmp 22-Aug-25

                        for (int i = 0; i < iCount; i++) {
                            try {
//bmp 11-Jun-24
//bmp 31-May-24
                                String strProductName = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanProductsMaster.COLUMN_NAME_ProductName));
                                if(strProductName == null) {
                                    c1.moveToNext();
                                    continue;
                                }
                                if(strProductName.isEmpty()) {
                                    c1.moveToNext();
                                    continue;
                                }
//                                if(!strProductName.toLowerCase().contains(mainActivity.AdditionalDetails.strLoanReason.toLowerCase())) {
//                                    c1.moveToNext();
//                                    continue;
//                                }
//bmp 31-May-24
                                strProductName = strProductName.trim();
                                if(mainActivity.AdditionalDetails.strLoanReason.equalsIgnoreCase("Home")) {
                                    if(!strProductName.startsWith("Home")) {
                                        c1.moveToNext();
                                        continue;
                                    }
                                } else {
                                    if(strProductName.contains("Home")) {
                                        c1.moveToNext();
                                        continue;
                                    }
                                }
//bmp 11-Jun-24
                                String strStartDate = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanProductsMaster.COLUMN_NAME_ProductStartDate));
                                if(strStartDate == null) {
                                    c1.moveToNext();
                                    continue;
                                }
                                if(strStartDate.isEmpty()) {
                                    c1.moveToNext();
                                    continue;
                                }
                                strStartDate = strStartDate.substring(0, 10);
                                String[] strSplit2 = strStartDate.split("-");
                                doe.set(Calendar.YEAR, Integer.parseInt(strSplit2[0]));
                                doe.set(Calendar.MONTH, (Integer.parseInt(strSplit2[1]) - 1));
                                doe.set(Calendar.DATE, Integer.parseInt(strSplit2[2]));
                                long ldoStartDate = doe.getTimeInMillis();
                                if(ldoLoanDisbursement < ldoStartDate) {
                                    c1.moveToNext();
                                    continue;
                                }
                                String strEndDate = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanProductsMaster.COLUMN_NAME_ProductEndDate));
                                if(strEndDate != null) {
                                    if (!strEndDate.isEmpty()) {
                                        strEndDate = strEndDate.substring(0, 10);
                                        String[] strSplit3 = strEndDate.split("-");
                                        doe.set(Calendar.YEAR, Integer.parseInt(strSplit3[0]));
                                        doe.set(Calendar.MONTH, (Integer.parseInt(strSplit3[1]) - 1));
                                        doe.set(Calendar.DATE, Integer.parseInt(strSplit3[2]));
                                        long ldoEndDate = doe.getTimeInMillis();
                                        if (ldoLoanDisbursement > ldoEndDate) {
                                            c1.moveToNext();
                                            continue;
                                        }
                                    }
                                }
                                LoanProductMasterID.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanProductsMaster.COLUMN_NAME_LoanProductID))));
                            } catch (Exception e) {
                                e.printStackTrace();
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
    public void SQLite_getLoanProductDetailsMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_LoanProductDetailsID,
                ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_LoanProductID,
                ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_PrincipalAmt,
                ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_LoanPeriodInMonths,
                ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_TotalMonthlyInstallmentAmt,
        };
        String Active = "1";

        String selection1 = ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_PrincipalAmt + " DESC";

        Cursor c1 = db.query(
                ufinDbContract.LoanProductDetailsMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        LoanProductDetailsIDMaster.clear();
        LoanProductDetailsIDMaster.add(0);
        LoanProductIDMaster.clear();
        LoanProductIDMaster.add(0);
        PrincipalAmtMaster.clear();
        PrincipalAmtMaster.add("");
        LoanPeriodInMonthsMaster.clear();
        LoanPeriodInMonthsMaster.add("");
        Principal_Period_Installment_Master.clear();
        Principal_Period_Installment_Master.add("");
        TotalMonthlyInstallmentAmtMaster.clear();
        TotalMonthlyInstallmentAmtMaster.add("");

//bmp 31-Aug-26
        Calendar agelimit = Calendar.getInstance();
        String strBirthDate = mainActivity.Member.get(0).BirthDate;
        if (strBirthDate.length() > 10) {
            strBirthDate = strBirthDate.substring(0, 10);
        }
        String[] strSplit = strBirthDate.split("-");
        agelimit.set(Calendar.YEAR, Integer.parseInt(strSplit[0]));
        agelimit.set(Calendar.MONTH, (Integer.parseInt(strSplit[1]) - 1));
        agelimit.set(Calendar.DATE, Integer.parseInt(strSplit[2]));
        agelimit.add(Calendar.YEAR, 65);
//bmp 31-Aug-26

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
//bmp 31-Aug-26
                            int iLoanPeriodInMonths = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_LoanPeriodInMonths)));
                            Calendar loanEndday = Calendar.getInstance();
                            loanEndday.add(Calendar.MONTH, iLoanPeriodInMonths);
                            long lTemp1 = loanEndday.getTimeInMillis();
                            long lTemp2 = agelimit.getTimeInMillis();
                            if(lTemp1 > lTemp2) {
                                c1.moveToNext();
                                continue;
                            }
//bmp 31-Aug-26
                            String strTemp = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_TotalMonthlyInstallmentAmt));
                            int iTemp = Integer.valueOf(strTemp);
                            if (iTemp <= mainActivity.AdditionalDetails.Monthly_Surplus) {
                                int iTemp2 = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_LoanProductID)));
                                for(int j=1; j<LoanProductMasterID.size(); j++) {
                                    if(iTemp2 == LoanProductMasterID.get(j)) {
                                        String strPrincipalAmt = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_PrincipalAmt));
                                        int iPrincipalAmt = Integer.parseInt(strPrincipalAmt);
                                        if(mainActivity.AdditionalDetails.strLoanReason.equalsIgnoreCase("Home")) {
                                            if(iPrincipalAmt <= mainActivity.AdditionalDetails.HomeLoanAmount) {
                                                LoanProductDetailsIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_LoanProductDetailsID))));
                                                LoanProductIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_LoanProductID))));
                                                PrincipalAmtMaster.add(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_PrincipalAmt)));
                                                LoanPeriodInMonthsMaster.add(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_LoanPeriodInMonths)));
                                                TotalMonthlyInstallmentAmtMaster.add(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_TotalMonthlyInstallmentAmt)));
                                            }
                                        } else {
                                            if (iPrincipalAmt <= mainActivity.AdditionalDetails.NextLoanOfferAmount) {
                                                LoanProductDetailsIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_LoanProductDetailsID))));
                                                LoanProductIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_LoanProductID))));
                                                PrincipalAmtMaster.add(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_PrincipalAmt)));
                                                LoanPeriodInMonthsMaster.add(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_LoanPeriodInMonths)));
                                                TotalMonthlyInstallmentAmtMaster.add(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_TotalMonthlyInstallmentAmt)));
                                            }
                                        }
                                    }
                                }
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
        String strReasonType = "Other CoApplicant reason";
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

        OtherJointLoaneeReasonIDMaster.clear();
        OtherJointLoaneeReasonMaster.clear();
        OtherJointLoaneeReasonIDMaster.add(0L);
        OtherJointLoaneeReasonMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            String Str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_MarathiDescription));
                            if ((Str1 == null) || (Str1.equalsIgnoreCase(" ")) || (Str1.equalsIgnoreCase(""))) {
                            } else {
                                OtherJointLoaneeReasonIDMaster.add(Long.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonID))));
                                OtherJointLoaneeReasonMaster.add(Str1);
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
        RelationIDMaster.add(0);
        RelationMarathiDescriptionMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            RelationIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.RelationMaster.COLUMN_NAME_RelationID))));
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
        BusinessIDMaster.add(0);
        BusinessMarathiDescriptionMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            BusinessIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.BusinessMaster.COLUMN_NAME_BusinessID))));
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
}
