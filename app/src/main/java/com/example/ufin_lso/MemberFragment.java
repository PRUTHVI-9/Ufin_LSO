package com.example.ufin_lso;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MemberFragment extends Fragment {
    int RelationIDUnknown;
    int RelationIDMainMember;

    View rootView;
    MainActivity mainActivity;
    private boolean dbMaster = false;

    long lMemberID = 0;
    String strSavingAccNo = "";
    String strRationcard = "";
    String strAadhaarcard = "";
    private static final int SCAN_REQUEST           = 0;

    int ScanRequest = -1;
    String Aadhaaruid, AadhaarName, AadhaarGender, AadhaarYearOfBirth, AadhaarCareOf;
    String AadhaarHouse, AadhaarLandmark, AadhaarLocality, AadhaarVillageTownCity, AadhaarPostOffice;
    String AadhaarDistrict, AadhaarSubDistrict, AadhaarState, AadhaarPinCode, AadhaarDOB;

    int selectedFamilyMemberPosition = -1;
    private View.OnClickListener familyMemberOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(selectedFamilyMemberPosition > 0) {
                View vPrev = (View) rootView.findViewById(selectedFamilyMemberPosition);
                vPrev.setBackgroundColor(Color.WHITE);

                int etFnameviewID = selectedFamilyMemberPosition * 100;
                int etMnameviewID = etFnameviewID + 1;
                int etLnameviewID = etFnameviewID + 2;
                int cbStatusviewID = etFnameviewID + 3;
                EditText et = (EditText) rootView.findViewById(etFnameviewID);
                et.setEnabled(false);
                et = (EditText) rootView.findViewById(etMnameviewID);
                et.setEnabled(false);
                et = (EditText) rootView.findViewById(etLnameviewID);
                et.setEnabled(false);
            }

            int iTemp = v.getId();
            if(selectedFamilyMemberPosition == iTemp) {
                selectedFamilyMemberPosition = -1;
                mainActivity.currentMemberIndex = -1;
                Button bt1 = (Button) rootView.findViewById(R.id.buttonDeactivateMember);
                bt1.setEnabled(false);
                Button bt2 = (Button) rootView.findViewById(R.id.buttonReactivateMember);
                bt2.setEnabled(false);
            } else {
                v.setBackgroundColor(Color.CYAN);
                selectedFamilyMemberPosition = iTemp;
                mainActivity.currentMemberIndex = iTemp;
                int etFnameviewID = iTemp * 100;
                int etMnameviewID = etFnameviewID + 1;
                int etLnameviewID = etFnameviewID + 2;
                int cbStatusviewID = etFnameviewID + 3;
                if(mainActivity.Member.get(iTemp).IsActive == 1) {
////bmp 14-May-25
//                    if(mainActivity.AdditionalDetails.LoanGuarantorID != mainActivity.Member.get(iTemp).MemberID) {
////bmp 14-May-25
//bmp 16-Jun-25
//                    EditText et = (EditText) rootView.findViewById(etFnameviewID);
//                    et.setEnabled(true);
//                    et = (EditText) rootView.findViewById(etMnameviewID);
//                    et.setEnabled(true);
//                    et = (EditText) rootView.findViewById(etLnameviewID);
//                    et.setEnabled(true);
                    boolean flagEnable = true;
                    String strMemberID = String.valueOf(mainActivity.Member.get(iTemp).MemberID);
//bmp 20-Jun-25
//                    if(strMemberID.length() < 18) {
                    if(strMemberID.length() < 17) {
//bmp 20-Jun-25
                        flagEnable = false;
                    }
                    EditText et = (EditText) rootView.findViewById(etFnameviewID);
                    et.setEnabled(flagEnable);
                    et = (EditText) rootView.findViewById(etMnameviewID);
                    et.setEnabled(flagEnable);
                    et = (EditText) rootView.findViewById(etLnameviewID);
                    et.setEnabled(flagEnable);
//bmp 16-Jun-25
                    Button bt1 = (Button) rootView.findViewById(R.id.buttonDeactivateMember);
                    bt1.setEnabled(true);
                    Button bt2 = (Button) rootView.findViewById(R.id.buttonReactivateMember);
                    bt2.setEnabled(false);
////bmp 14-May-25
//                    }
////bmp 14-May-25
                } else {
                    EditText et = (EditText) rootView.findViewById(etFnameviewID);
                    et.setEnabled(false);
                    et = (EditText) rootView.findViewById(etMnameviewID);
                    et.setEnabled(false);
                    et = (EditText) rootView.findViewById(etLnameviewID);
                    et.setEnabled(false);
                    Button bt1 = (Button) rootView.findViewById(R.id.buttonDeactivateMember);
                    bt1.setEnabled(false);
                    Button bt2 = (Button) rootView.findViewById(R.id.buttonReactivateMember);
                    bt2.setEnabled(true);
                }
            }
        }
    };

    public MemberFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_member, container, false);
        mainActivity = (MainActivity) getActivity();

//        if(!dbMaster) {
//            SQLite_getRelationMaster();
//            dbMaster = true;
//        }

        ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.progressBarMember);
        pb.setVisibility(View.INVISIBLE);

        Button btPlus = (Button) rootView.findViewById(R.id.buttonFamilyMemberPlus);
        btPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//bmp 02-May-24
//                if(mainActivity.Member.size() < 12) {
                if(mainActivity.Member.size() < 99) {
//bmp 02-May-24
                    view.setEnabled(false);
                    ShowdlgAlert2("Press OK to Add New Family Member.");
                }
            }
        });

        TextView tv = (TextView) rootView.findViewById(R.id.textViewFamilyMemberCount);
        tv.setText(String.valueOf(mainActivity.Member.size()));

        mainActivity.currentMemberIndex = 0;

        EditText et = (EditText) rootView.findViewById(R.id.editTextMainMemberFname);
        if(mainActivity.Member.size() > 0) {
            et.setText(mainActivity.Member.get(0).Fname);
        } else {
            et.setText("");
        }
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextMainMemberMname);
        if(mainActivity.Member.size() > 0) {
            et.setText(mainActivity.Member.get(0).Mname);
        } else {
            et.setText("");
        }
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextMainMemberLname);
        if(mainActivity.Member.size() > 0) {
            et.setText(mainActivity.Member.get(0).Lname);
        } else {
            et.setText("");
        }
        et.setEnabled(false);

        Button btn = (Button) rootView.findViewById(R.id.buttonDeactivateMember);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mainActivity.currentMemberIndex == 0) {
                    ShowdlgAlert1("Can not Deactivate Main Member.");
                } else if(mainActivity.currentMemberIndex > 0) {
                    String strTemp = String.valueOf(mainActivity.Member.get(mainActivity.currentMemberIndex).MemberID);
//bmp 20-Jun-25
//                    if( (strTemp.equalsIgnoreCase("0")) || (strTemp.length() == 18) ) {
                    if( (strTemp.equalsIgnoreCase("0")) || (strTemp.length() >= 17) ) {
//bmp 20-Jun-25
                        //LSO added this family member entry by mistake, allow to delete it ????
                    }
                    if(mainActivity.Member.get(mainActivity.currentMemberIndex).IsActive == 1) {
                        mainActivity.Member.get(mainActivity.currentMemberIndex).IsActive = 0;
                        mainActivity.PersonalImpactInfo.get(mainActivity.currentMemberIndex).IsActive = 0;

                        int iFnameviewID = (mainActivity.currentMemberIndex * 100);
                        EditText et = (EditText) rootView.findViewById(iFnameviewID);
                        et.setEnabled(false);
                        int iMnameviewID = (mainActivity.currentMemberIndex * 100) + 1;
                        et = (EditText) rootView.findViewById(iMnameviewID);
                        et.setEnabled(false);
                        int iLnameviewID = (mainActivity.currentMemberIndex * 100) + 2;
                        et = (EditText) rootView.findViewById(iLnameviewID);
                        et.setEnabled(false);
                        int iStatusviewID = (mainActivity.currentMemberIndex * 100) + 3;
                        CheckBox cb = (CheckBox) rootView.findViewById(iStatusviewID);
                        cb.setChecked(false);
                        Button btn = (Button) rootView.findViewById(R.id.buttonDeactivateMember);
                        btn.setEnabled(false);
                        btn = (Button) rootView.findViewById(R.id.buttonReactivateMember);
                        btn.setEnabled(true);

                        Calendar cal = Calendar.getInstance();
                        Date date = cal.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        String strDeactivateDate = sdf.format(date);// all done
                        mainActivity.Member.get(mainActivity.currentMemberIndex).DeactivateDate = strDeactivateDate;
                        mainActivity.Member.get(mainActivity.currentMemberIndex).DeletionReasonID = 22; //20=Death,22=Other
                    }
                }
            }
        });

        btn = (Button) rootView.findViewById(R.id.buttonReactivateMember);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mainActivity.Member.get(mainActivity.currentMemberIndex).IsActive == 0) {
                    mainActivity.Member.get(mainActivity.currentMemberIndex).IsActive = 1;
                    mainActivity.PersonalImpactInfo.get(mainActivity.currentMemberIndex).IsActive = 1;
                    mainActivity.Member.get(mainActivity.currentMemberIndex).DeactivateDate = "NA";
                    mainActivity.Member.get(mainActivity.currentMemberIndex).DeathDate = "NA";
                    mainActivity.Member.get(mainActivity.currentMemberIndex).DeletionReasonID = 0;
                    int iStatusviewID = (mainActivity.currentMemberIndex * 100) + 3;
                    CheckBox cb = (CheckBox) rootView.findViewById(iStatusviewID);
                    cb.setChecked(true);
                    Button btn = (Button) rootView.findViewById(R.id.buttonDeactivateMember);
                    btn.setEnabled(true);
                    btn = (Button) rootView.findViewById(R.id.buttonReactivateMember);
                    btn.setEnabled(false);
                }
            }
        });

        btn = (Button) rootView.findViewById(R.id.buttonDeactivateMember);
        btn.setEnabled(false);
        btn = (Button) rootView.findViewById(R.id.buttonReactivateMember);
        btn.setEnabled(false);

        return rootView;
    }

    void ShowdlgAlert1(String strMessage) {
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

    void ShowdlgAlert2(String strMessage) {
        AlertDialog.Builder dlgAlert2 = new AlertDialog.Builder(getContext());
        TextView tvTitle = new TextView(getContext());
        tvTitle.setText(getString(R.string.app_name));
        tvTitle.setTextSize(30);
        tvTitle.setTextColor(getResources().getColor(R.color.colorAccent));
        dlgAlert2.setCustomTitle(tvTitle);
        dlgAlert2.setIcon(R.drawable.ic_launcher_foreground);
        dlgAlert2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Cancel button
                dialog.dismiss();

                Button btPlus = (Button) rootView.findViewById(R.id.buttonFamilyMemberPlus);
                btPlus.setEnabled(true);
            }
        });
        dlgAlert2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                dialog.dismiss();

                TextView tv = (TextView) rootView.findViewById(R.id.textViewFamilyMemberCount);
                tv.setText(String.valueOf(mainActivity.Member.size() + 1));
                MemberInit();

                AsyncCallWS_CreateNewMemberInfo taskCreateNewMemberInfo = new AsyncCallWS_CreateNewMemberInfo();
                taskCreateNewMemberInfo.execute();
            }
        });
        dlgAlert2.setCancelable(true);

        dlgAlert2.setMessage(strMessage);
        AlertDialog dialog2 = dlgAlert2.create();
        dialog2.show();
        TextView textView = (TextView) dialog2.findViewById(android.R.id.message);
        textView.setTextSize(30);
    }

    void MemberInit() {
        Calendar cal = Calendar.getInstance();
        while(mainActivity.LastTimeInMillis > cal.getTimeInMillis()) {
            cal = Calendar.getInstance();
        }
        mainActivity.LastTimeInMillis = cal.getTimeInMillis();

        int iSize = mainActivity.Member.size();
        TextView tv = (TextView) rootView.findViewById(R.id.textViewFamilyMemberCount);
        int iCount = Integer.valueOf(tv.getText().toString());
        if(iCount < iSize) {
//            for (int i = (iSize - 1); i > (iCount - 1); i--) {
//                mainActivity.Member.remove(i);
//                mainActivity.tMember.remove(i);
//                mainActivity.PersonalImpactInfo.remove(i);
//                mainActivity.tPersonalImpactInfo.remove(i);
//            }
        } else {
            for (int i = iSize; i < iCount; i++) {
                clsMemberData newMember = new clsMemberData();
                newMember.InitNewMember(mainActivity.strToday);
                newMember.BranchID = mainActivity.Member.get(0).BranchID;
                newMember.GroupID = mainActivity.Member.get(0).GroupID;
                newMember.MainMemberID = mainActivity.Member.get(0).MemberID;
                newMember.RelationID = RelationIDUnknown;
                newMember.EncodedBy = mainActivity.UserID;
                mainActivity.Member.add(newMember);

                clsMemberData tnewMember = new clsMemberData();
                newMember.Copy_Memberdata(tnewMember);
                mainActivity.tMember.add(tnewMember);

                clsPersonalImpactInfo newInfo = new clsPersonalImpactInfo();
                newInfo.Init_PersonalImpactInfo();
                newInfo.EncodedBy = mainActivity.UserID;
                mainActivity.PersonalImpactInfo.add(newInfo);

                clsPersonalImpactInfo tnewInfo = new clsPersonalImpactInfo();
                newInfo.Copy_PersonalImpactInfo(tnewInfo);
                mainActivity.tPersonalImpactInfo.add(tnewInfo);
            }
        }

        mainActivity.currentMemberIndex = 0;
    }

    void RefreshFamilyMembers() {
        if(mainActivity.Member.size() > 0) {
            TextView tv = (TextView) rootView.findViewById(R.id.textViewFamilyMemberCount);
            tv.setText(String.valueOf(mainActivity.Member.size()));

            EditText et = (EditText) rootView.findViewById(R.id.editTextMainMemberFname);
            et.setText(mainActivity.Member.get(0).Fname);
            et.setTextSize(30);
            et.setTextColor(Color.BLACK);
            et.setTypeface(Typeface.DEFAULT_BOLD);
            et.setEnabled(false);
            et = (EditText) rootView.findViewById(R.id.editTextMainMemberMname);
            et.setText(mainActivity.Member.get(0).Mname);
            et.setTextSize(30);
            et.setTextColor(Color.BLACK);
            et.setTypeface(Typeface.DEFAULT_BOLD);
            et.setEnabled(false);
            et = (EditText) rootView.findViewById(R.id.editTextMainMemberLname);
            et.setText(mainActivity.Member.get(0).Lname);
            et.setTextSize(30);
            et.setTextColor(Color.BLACK);
            et.setTypeface(Typeface.DEFAULT_BOLD);
            et.setEnabled(false);
            CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxStatusMainMember);
            cb.setText("");
            cb.setButtonTintList(ColorStateList.valueOf(Color.RED));
            cb.setChecked(true);
            cb.setEnabled(false);
        } else {
            TextView tv = (TextView) rootView.findViewById(R.id.textViewFamilyMemberCount);
            tv.setText(String.valueOf(mainActivity.Member.size()));

            EditText et = (EditText) rootView.findViewById(R.id.editTextMainMemberFname);
            et.setText("");
            et.setTextSize(30);
            et.setTextColor(Color.BLACK);
            et.setTypeface(Typeface.DEFAULT_BOLD);
            et.setEnabled(false);
            et = (EditText) rootView.findViewById(R.id.editTextMainMemberMname);
            et.setText("");
            et.setTextSize(30);
            et.setTextColor(Color.BLACK);
            et.setTypeface(Typeface.DEFAULT_BOLD);
            et.setEnabled(false);
            et = (EditText) rootView.findViewById(R.id.editTextMainMemberLname);
            et.setText("");
            et.setTextSize(30);
            et.setTextColor(Color.BLACK);
            et.setTypeface(Typeface.DEFAULT_BOLD);
            et.setEnabled(false);
            CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxStatusMainMember);
            cb.setText("");
            cb.setButtonTintList(ColorStateList.valueOf(Color.RED));
            cb.setChecked(false);
            cb.setEnabled(false);
            System.out.println("Saving Account does not exist!");
        }

        LinearLayout llv = (LinearLayout) rootView.findViewById(R.id.linearLayoutFamilyMembers);
        llv.removeAllViews();

        for(int i=1; i<mainActivity.Member.size(); i++) {
            TextView etFname = new EditText(mainActivity.getBaseContext());
            etFname.setSingleLine(true);
            etFname.setTextSize(30);
            etFname.setTextColor(Color.BLACK);
            etFname.setTypeface(Typeface.DEFAULT_BOLD);
            etFname.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    (float)1.0));
            etFname.setText(mainActivity.Member.get(i).Fname);
            int etFnameviewID = i * 100;
            etFname.setId(etFnameviewID);
            etFname.setEnabled(false);
            etFname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if( (actionId == EditorInfo.IME_ACTION_DONE) ||
                            (actionId == EditorInfo.IME_ACTION_NEXT) ){
                        String strName = v.getText().toString();
                        if(selectedFamilyMemberPosition > 0) {
                            if((strName != null) && (strName.length() > 0)) {
                                mainActivity.Member.get(selectedFamilyMemberPosition).Fname = strName;
                            }
                        }
                    }
                    return false;
                }
            });
            etFname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                    } else {
                        if (mainActivity.iCurrentScreen !=
                                mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_MEMBER)
                            return;
                        String strName = ((TextView) view).getText().toString();
                        if(selectedFamilyMemberPosition > 0) {
                            if((strName != null) && (strName.length() > 0)) {
                                mainActivity.Member.get(selectedFamilyMemberPosition).Fname = strName;
                            }
                        }
                    }
                }
            });

            EditText etMname = new EditText(mainActivity.getBaseContext());
            etMname.setSingleLine(true);
            etMname.setTextSize(30);
            etMname.setTextColor(Color.BLACK);
            etMname.setTypeface(Typeface.DEFAULT_BOLD);
            etMname.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    (float)1.0));
            etMname.setText(mainActivity.Member.get(i).Mname);
            int etMnameviewID = etFnameviewID + 1;
            etMname.setId(etMnameviewID);
            etMname.setEnabled(false);
            etMname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if( (actionId == EditorInfo.IME_ACTION_DONE) ||
                            (actionId == EditorInfo.IME_ACTION_NEXT) ){
                        String strName = v.getText().toString();
                        if(selectedFamilyMemberPosition > 0) {
                            if((strName != null) && (strName.length() > 0)) {
                                mainActivity.Member.get(selectedFamilyMemberPosition).Mname = strName;
                            }
                        }
                    }
                    return false;
                }
            });
            etMname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                    } else {
                        if (mainActivity.iCurrentScreen !=
                                mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_MEMBER)
                            return;
                        String strName = ((EditText) view).getText().toString();
                        if(selectedFamilyMemberPosition > 0) {
                            if((strName != null) && (strName.length() > 0)) {
                                mainActivity.Member.get(selectedFamilyMemberPosition).Mname = strName;
                            }
                        }
                    }
                }
            });

            EditText etLname = new EditText(mainActivity.getBaseContext());
            etLname.setSingleLine(true);
            etLname.setTextSize(30);
            etLname.setTextColor(Color.BLACK);
            etLname.setTypeface(Typeface.DEFAULT_BOLD);
            etLname.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    (float)1.0));
            etLname.setText(mainActivity.Member.get(i).Lname);
            int etLnameviewID = etFnameviewID + 2;
            etLname.setId(etLnameviewID);
            etLname.setEnabled(false);
            etLname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if( (actionId == EditorInfo.IME_ACTION_DONE) ||
                            (actionId == EditorInfo.IME_ACTION_NEXT) ){
                        String strName = v.getText().toString();
                        if(selectedFamilyMemberPosition > 0) {
                            if((strName != null) && (strName.length() > 0)) {
                                mainActivity.Member.get(selectedFamilyMemberPosition).Lname = strName;
                            }
                        }
                    }
                    return false;
                }
            });
            etLname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                    } else {
                        if (mainActivity.iCurrentScreen !=
                                mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_MEMBER)
                            return;
                        String strName = ((EditText) view).getText().toString();
                        if(selectedFamilyMemberPosition > 0) {
                            if((strName != null) && (strName.length() > 0)) {
                                mainActivity.Member.get(selectedFamilyMemberPosition).Lname = strName;
                            }
                        }
                    }
                }
            });

            CheckBox cbStatus = new CheckBox(mainActivity.getBaseContext());
            cbStatus.setText("");
            cbStatus.setButtonTintList(ColorStateList.valueOf(Color.RED));
            cbStatus.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            int cbStatusviewID = etFnameviewID + 3;
            cbStatus.setId(cbStatusviewID);
            cbStatus.setEnabled(false);
            if(mainActivity.Member.get(i).IsActive == 1)
                cbStatus.setChecked(true);
            else
                cbStatus.setChecked(false);

            LinearLayout llh1 = new LinearLayout(mainActivity.getBaseContext());
            llh1.setLayoutParams(new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (float) 20.0));
            LinearLayout llh2 = new LinearLayout(mainActivity.getBaseContext());
            llh2.setLayoutParams(new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (float) 1.0));

            TableRow tblRow = new TableRow(mainActivity.getBaseContext());
            tblRow.setBackground(getResources().getDrawable(R.drawable.customborder));
//            tblRow.addView(etFname);
//            tblRow.addView(etMname);
//            tblRow.addView(etLname);
//            tblRow.addView(cbStatus);
            llh2.addView(etFname);
            llh2.addView(etMname);
            llh2.addView(etLname);
            llh2.addView(cbStatus);
            tblRow.addView(llh1);
            tblRow.addView(llh2);
            tblRow.setId(i);
            tblRow.setEnabled(true);
            tblRow.setClickable(true);
////bmp 12-May-25
//            if(mainActivity.AdditionalDetails.LoanGuarantorID == mainActivity.Member.get(i).MemberID) {
//                tblRow.setEnabled(false);
//                tblRow.setClickable(false);
//            }
////bmp 12-May-25
            tblRow.setOnClickListener(familyMemberOnClickListener);

            llv.addView(tblRow);
        }

        TextView tv = (TextView) rootView.findViewById(R.id.textViewRequestFocus);
        tv.requestFocus();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            if(mainActivity == null)
                return;

            mainActivity.FragmentVisibleToUser = mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_MEMBER;

            ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.progressBarMember);
            pb.setVisibility(View.INVISIBLE);

            mainActivity.currentMemberIndex = 0;
            RefreshFamilyMembers();

            selectedFamilyMemberPosition = -1;
            mainActivity.currentMemberIndex = -1;
            Button bt1 = (Button) rootView.findViewById(R.id.buttonDeactivateMember);
            bt1.setEnabled(false);
            Button bt2 = (Button) rootView.findViewById(R.id.buttonReactivateMember);
            bt2.setEnabled(false);
        }
    }

    private class AsyncCallWS_CreateNewMemberInfo extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.progressBarMember);
            if(pb != null) {
                pb.setIndeterminate(true);
                pb.setVisibility(View.VISIBLE);
            }
            mainActivity.progressBarWait = true;

//            String strNA = "NA";
//            EditText et = (EditText) rootView.findViewById(R.id.editTextNewMemberFname);
//            if(strNA.equalsIgnoreCase(mainActivity.Member.get(0).Fname))
//                et.setText("");
//            else
//                et.setText(mainActivity.Member.get(0).Fname);
//            et.setEnabled(false);
//            et = (EditText) rootView.findViewById(R.id.editTextNewMemberMname);
//            if(strNA.equalsIgnoreCase(mainActivity.Member.get(0).Mname))
//                et.setText("");
//            else
//                et.setText(mainActivity.Member.get(0).Mname);
//            et.setEnabled(false);
//            et = (EditText) rootView.findViewById(R.id.editTextNewMemberLname);
//            if(strNA.equalsIgnoreCase(mainActivity.Member.get(0).Lname))
//                et.setText("");
//            else
//                et.setText(mainActivity.Member.get(0).Lname);
//            et.setEnabled(false);

            TextView tv = (TextView) rootView.findViewById(R.id.textViewFamilyMemberCount);
            tv.setText(String.valueOf(mainActivity.Member.size()));
//            tv = (TextView) rootView.findViewById(R.id.textViewFamilyMemberSN);
//            tv.setText("1");
//            Button bt = (Button) rootView.findViewById(R.id.buttonScanNewMember);
//            bt.setEnabled(false);
//            ImageButton imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonNewPrev);
//            imgbt.setEnabled(false);
//            imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonNewNext);
//            imgbt.setEnabled(false);

//            tv = (TextView) rootView.findViewById(R.id.editTextSavingAccNo);
//            tv.setEnabled(false);
//            tv = (TextView) rootView.findViewById(R.id.editTextRationcardNo);
//            tv.setEnabled(false);
//            tv = (TextView) rootView.findViewById(R.id.editTextAadhaarNo);
//            tv.setEnabled(false);
        }
        @Override
        protected Void doInBackground(String... params) {
            Calendar cal = Calendar.getInstance();
            while(mainActivity.LastTimeInMillis > cal.getTimeInMillis()) {
                cal = Calendar.getInstance();
            }
            mainActivity.LastTimeInMillis = cal.getTimeInMillis();

//            boolean newMainMember = false;
//            if (mainActivity.Member.get(0).MemberID == 0) {
//                newMainMember = true;
//                mainActivity.LastTimeInMillis += 1000;
//                if(mainActivity.SQLite_saveMemberAddress(mainActivity.LastTimeInMillis, true))
//                    mainActivity.MemberAddress.Copy_MemberAddress(mainActivity.tMemberAddress);
//                mainActivity.Member.get(0).AddressID = mainActivity.MemberAddress.AddressID;
//
//                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//                String strJoiningDate = sdf1.format(cal.getTime());// all done
//                mainActivity.Member.get(0).JoiningDate = strJoiningDate;
//
//                mainActivity.LastTimeInMillis += 1000;
//                if(mainActivity.SQLite_saveMember(mainActivity.Member.get(0), mainActivity.LastTimeInMillis, true)) {
//                    mainActivity.Member.get(0).Copy_Memberdata(mainActivity.tMember.get(0));
//                }
//
//                mainActivity.PersonalImpactInfo.get(0).MemberID = mainActivity.Member.get(0).MemberID;
//                mainActivity.PersonalImpactInfo.get(0).EncodedBy = mainActivity.UserID;
//                mainActivity.LastTimeInMillis += 1000;
//                if(mainActivity.SQLite_savePersonalImpactInfo(mainActivity.PersonalImpactInfo.get(0), mainActivity.LastTimeInMillis, true)) {
//                    mainActivity.PersonalImpactInfo.get(0).Copy_PersonalImpactInfo(mainActivity.tPersonalImpactInfo.get(0));
//                }
//
//                mainActivity.MemberFamilyDetails.MemberID = mainActivity.Member.get(0).MemberID;
//                mainActivity.MemberFamilyDetails.RationCardNo = strRationcard;
//                mainActivity.LastTimeInMillis += 1000;
//                if(mainActivity.SQLite_saveMemberFamilyDetails(mainActivity.LastTimeInMillis, true)) {
//                    mainActivity.MemberFamilyDetails.Copy_MemberFamilyDetails(mainActivity.tMemberFamilyDetails);
//                }
//                //Cross reference updated
//                mainActivity.Member.get(0).MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
//                mainActivity.SQLite_saveMember(mainActivity.Member.get(0), 0, true);
//
//                mainActivity.FamilyImpactInfo.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
//                mainActivity.FamilyImpactInfo.EncodedBy = mainActivity.UserID;
//                mainActivity.LastTimeInMillis += 1000;
//                if(mainActivity.SQLite_saveFamilyImpactInfo(mainActivity.LastTimeInMillis, true)) {
//                    mainActivity.FamilyImpactInfo.Copy_MemberFamilyImpactInfo(mainActivity.tFamilyImpactInfo);
//                }
//
//                mainActivity.LMFnominee.MemberID = mainActivity.Member.get(0).MemberID;
//                //Do NOT Save before selecting Nominee
//                mainActivity.LMFnominee.Copy_LMFnominee(mainActivity.tLMFnominee);
//
//                mainActivity.AdditionalDetails.MemberID = mainActivity.Member.get(0).MemberID;
//                mainActivity.LastTimeInMillis += 1000;
//                if(mainActivity.SQLite_saveAdditionalDetails(mainActivity.LastTimeInMillis, true)) {
//                    mainActivity.AdditionalDetails.Copy_AdditionalDetails(mainActivity.tAdditionalDetails);
//                }
//            }

            for(int i=1; i<mainActivity.Member.size(); i++) {
                if (mainActivity.Member.get(i).MemberID == 0) {
                    mainActivity.Member.get(i).AddressID = mainActivity.Member.get(0).AddressID;
                    mainActivity.Member.get(i).MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                    mainActivity.Member.get(i).MainMemberID = mainActivity.Member.get(0).MemberID;
                    mainActivity.Member.get(i).RelationID = RelationIDUnknown;

                    mainActivity.Member.get(i).BranchID = mainActivity.Member.get(0).BranchID;
                    mainActivity.Member.get(i).GroupID = mainActivity.Member.get(0).GroupID;

                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    String strJoiningDate = sdf1.format(cal.getTime());// all done
                    mainActivity.Member.get(i).JoiningDate = strJoiningDate;

                    mainActivity.LastTimeInMillis += 1000;
                    if(mainActivity.SQLite_saveMember(mainActivity.Member.get(i), mainActivity.LastTimeInMillis, true)) {
                        mainActivity.Member.get(i).Copy_Memberdata(mainActivity.tMember.get(i));
                    }
                    mainActivity.PersonalImpactInfo.get(i).MemberID = mainActivity.Member.get(i).MemberID;
                    mainActivity.PersonalImpactInfo.get(i).EncodedBy = mainActivity.UserID;
                    mainActivity.LastTimeInMillis += 1000;
                    if(mainActivity.SQLite_savePersonalImpactInfo(mainActivity.PersonalImpactInfo.get(i), mainActivity.LastTimeInMillis, true)) {
                        mainActivity.PersonalImpactInfo.get(i).Copy_PersonalImpactInfo(mainActivity.tPersonalImpactInfo.get(i));
                    }
                }
            }

//            if(newMainMember) {
//                for (int i = 0; i < mainActivity.MemberDocRcvd.size(); i++) {
//                    mainActivity.MemberDocRcvd.get(i).MemberID = mainActivity.Member.get(0).MemberID;
//                    mainActivity.LastTimeInMillis += 1000;
//                    if (mainActivity.SQLite_saveMemberDocRcvd(i, mainActivity.LastTimeInMillis, true)) {
//                        mainActivity.MemberDocRcvd.get(i).Copy_MemberDocRcvd(mainActivity.tMemberDocRcvd.get(i));
//                    }
//                }
//            }
            //For newMainMember MemberImpactAnswer is empty
            //MemberImpactAnswer is only for MainMember

            mainActivity.currentMemberIndex = 0;

            return null;
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
        @Override
        protected void onPostExecute(Void result) {
            ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.progressBarMember);
            if(pb != null) {
                pb.setIndeterminate(true);
                pb.setVisibility(View.INVISIBLE);
            }
            mainActivity.progressBarWait = false;

            RefreshFamilyMembers();

//            ImageButton imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonNewPrev);
//            imgbt.setEnabled(true);
//            imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonNewNext);
//            imgbt.setEnabled(true);

            Button btPlus = (Button) rootView.findViewById(R.id.buttonFamilyMemberPlus);
            btPlus.setEnabled(true);
        }
    }

//    private class AsyncCallWS_GetMemberInfo extends AsyncTask<String, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.progressBarMember);
//            if(pb != null) {
//                pb.setIndeterminate(true);
//                pb.setVisibility(View.VISIBLE);
//            }
//            mainActivity.progressBarWait = true;
//        }
//
//        @Override
//        protected Void doInBackground(String... params) {
//            mainActivity.SQLite_getMembers(lMemberID, "MemberID");
//            if(mainActivity.Member.size() > 0) {
//                mainActivity.SQLite_getMemberOrgDetails(mainActivity.Member.get(0).GroupID);
//                mainActivity.SQLite_getMembers(mainActivity.Member.get(0).MemberID, "MainMemberID");
//                for(int i=0; i<mainActivity.Member.size(); i++) {
//                    mainActivity.SQLite_getPersonalImpactInfo(mainActivity.Member.get(i).MemberID);
//                    mainActivity.SQLite_getMemberDocRcvd(mainActivity.Member.get(i).MemberID);
//                }
//                mainActivity.DocumentPhotos.clear();
//                mainActivity.tDocumentPhotos.clear();
//                for(int k=0; k<mainActivity.MemberDocRcvd.size(); k++) {
//                    mainActivity.SQLite_getDocumentPhotos(mainActivity.MemberDocRcvd.get(k).MemberDocuemntsRcvdID);
//                }
//                mainActivity.SQLite_getMemberFamilyDetails(mainActivity.Member.get(0).MemberID);
//                mainActivity.SQLite_getFamilyImpactInfo(mainActivity.MemberFamilyDetails.MemberFamilyDetailsID);
//                mainActivity.SQLite_getMemberImpactAnswer(mainActivity.MemberFamilyDetails.MemberFamilyDetailsID);
//                mainActivity.SQLite_getMemberAddress(mainActivity.Member.get(0).AddressID);
//
//                mainActivity.LMFnominee.Init_LMFnominee();
//                mainActivity.SQLite_getLMFnomineeDetails(mainActivity.Member.get(0).MemberID);
//
//                mainActivity.Othernominee.Init_OtherNominee();
//                mainActivity.SQLite_getOtherNomineeDetails(mainActivity.Member.get(0).MemberID);
//
//                mainActivity.currentMemberIndex = 0;
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.progressBarMember);
//            if(pb != null) {
//                pb.setIndeterminate(true);
//                pb.setVisibility(View.INVISIBLE);
//            }
//            mainActivity.progressBarWait = false;
//
//            if(mainActivity.Member.size() > 0) {
//                TextView tv = (TextView) rootView.findViewById(R.id.textViewFamilyMemberCount);
//                tv.setText(String.valueOf(mainActivity.Member.size()));
//                EditText et = (EditText) rootView.findViewById(R.id.editTextMainMemberFname);
//                et.setText(mainActivity.Member.get(0).Fname);
//                et.setEnabled(false);
//                et = (EditText) rootView.findViewById(R.id.editTextMainMemberMname);
//                et.setText(mainActivity.Member.get(0).Mname);
//                et.setEnabled(false);
//                et = (EditText) rootView.findViewById(R.id.editTextMainMemberLname);
//                et.setText(mainActivity.Member.get(0).Lname);
//                et.setEnabled(false);
//                CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxStatusMainMember);
//                cb.setChecked(true);
//                cb.setEnabled(false);
//            } else {
//                System.out.println("Saving Account does not exist!");
//            }
//
//            for(int i=1; i<mainActivity.Member.size(); i++) {
//                EditText etFname = new EditText(mainActivity.getBaseContext());
//                etFname.setTextSize(24);
//                etFname.setLayoutParams(new TableRow.LayoutParams(
//                        TableRow.LayoutParams.MATCH_PARENT,
//                        TableRow.LayoutParams.WRAP_CONTENT,
//                        (float)1.0));
//                etFname.setText(mainActivity.Member.get(i).Fname);
//                int etFnameviewID = i * 100;
//                etFname.setId(etFnameviewID);
//                etFname.setEnabled(false);
//                etFname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                    @Override
//                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                        if( (actionId == EditorInfo.IME_ACTION_DONE) ||
//                                (actionId == EditorInfo.IME_ACTION_NEXT) ){
//                            String strName = v.getText().toString();
//                            if(selectedFamilyMemberPosition > 0) {
//                                if((strName != null) && (strName.length() > 0)) {
//                                    mainActivity.Member.get(selectedFamilyMemberPosition).Fname = strName;
//                                }
//                            }
//                        }
//                        return false;
//                    }
//                });
//                etFname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                    @Override
//                    public void onFocusChange(View view, boolean hasFocus) {
//                        if (hasFocus) {
//                        } else {
//                            if (mainActivity.iCurrentScreen !=
//                                    mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_MEMBER)
//                                return;
//                            String strName = ((EditText) view).getText().toString();
//                            if(selectedFamilyMemberPosition > 0) {
//                                if((strName != null) && (strName.length() > 0)) {
//                                    mainActivity.Member.get(selectedFamilyMemberPosition).Fname = strName;
//                                }
//                            }
//                        }
//                    }
//                });
//
//                EditText etMname = new EditText(mainActivity.getBaseContext());
//                etMname.setTextSize(24);
//                etMname.setLayoutParams(new TableRow.LayoutParams(
//                        TableRow.LayoutParams.MATCH_PARENT,
//                        TableRow.LayoutParams.WRAP_CONTENT,
//                        (float)1.0));
//                etMname.setText(mainActivity.Member.get(i).Mname);
//                int etMnameviewID = etFnameviewID + 1;
//                etMname.setId(etMnameviewID);
//                etMname.setEnabled(false);
//                etMname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                    @Override
//                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                        if( (actionId == EditorInfo.IME_ACTION_DONE) ||
//                                (actionId == EditorInfo.IME_ACTION_NEXT) ){
//                            String strName = v.getText().toString();
//                            if(selectedFamilyMemberPosition > 0) {
//                                if((strName != null) && (strName.length() > 0)) {
//                                    mainActivity.Member.get(selectedFamilyMemberPosition).Mname = strName;
//                                }
//                            }
//                        }
//                        return false;
//                    }
//                });
//                etMname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                    @Override
//                    public void onFocusChange(View view, boolean hasFocus) {
//                        if (hasFocus) {
//                        } else {
//                            if (mainActivity.iCurrentScreen !=
//                                    mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_MEMBER)
//                                return;
//                            String strName = ((EditText) view).getText().toString();
//                            if(selectedFamilyMemberPosition > 0) {
//                                if((strName != null) && (strName.length() > 0)) {
//                                    mainActivity.Member.get(selectedFamilyMemberPosition).Mname = strName;
//                                }
//                            }
//                        }
//                    }
//                });
//
//                EditText etLname = new EditText(mainActivity.getBaseContext());
//                etLname.setTextSize(24);
//                etLname.setLayoutParams(new TableRow.LayoutParams(
//                        TableRow.LayoutParams.MATCH_PARENT,
//                        TableRow.LayoutParams.WRAP_CONTENT,
//                        (float)1.0));
//                etLname.setText(mainActivity.Member.get(i).Lname);
//                int etLnameviewID = etFnameviewID + 2;
//                etLname.setId(etLnameviewID);
//                etLname.setEnabled(false);
//                etLname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                    @Override
//                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                        if( (actionId == EditorInfo.IME_ACTION_DONE) ||
//                                (actionId == EditorInfo.IME_ACTION_NEXT) ){
//                            String strName = v.getText().toString();
//                            if(selectedFamilyMemberPosition > 0) {
//                                if((strName != null) && (strName.length() > 0)) {
//                                    mainActivity.Member.get(selectedFamilyMemberPosition).Lname = strName;
//                                }
//                            }
//                        }
//                        return false;
//                    }
//                });
//                etLname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                    @Override
//                    public void onFocusChange(View view, boolean hasFocus) {
//                        if (hasFocus) {
//                        } else {
//                            if (mainActivity.iCurrentScreen !=
//                                    mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_MEMBER)
//                                return;
//                            String strName = ((EditText) view).getText().toString();
//                            if(selectedFamilyMemberPosition > 0) {
//                                if((strName != null) && (strName.length() > 0)) {
//                                    mainActivity.Member.get(selectedFamilyMemberPosition).Lname = strName;
//                                }
//                            }
//                        }
//                    }
//                });
//
//                CheckBox cbStatus = new CheckBox(mainActivity.getBaseContext());
//                cbStatus.setLayoutParams(new TableRow.LayoutParams(
//                        TableRow.LayoutParams.WRAP_CONTENT,
//                        TableRow.LayoutParams.WRAP_CONTENT));
//                int cbStatusviewID = etFnameviewID + 3;
//                cbStatus.setId(cbStatusviewID);
//                cbStatus.setEnabled(false);
//                if(mainActivity.Member.get(i).IsActive == 1)
//                    cbStatus.setChecked(true);
//                else
//                    cbStatus.setChecked(false);
//
//                TableRow tblRow = new TableRow(mainActivity.getBaseContext());
//                tblRow.addView(etFname);
//                tblRow.addView(etMname);
//                tblRow.addView(etLname);
//                tblRow.addView(cbStatus);
//                tblRow.setId(i);
//                tblRow.setEnabled(true);
//                tblRow.setClickable(true);
//                tblRow.setOnClickListener(familyMemberOnClickListener);
//            }
//
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//        }
//    }

//    public void SQLite_getRelationMaster() {
//        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
//        // Gets the data repository in write mode
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//
//        // Define a projection that specifies which columns from the database
//        // you will actually use after this query.
//        String[] projection1 = {
//                ufinDbContract.RelationMaster.COLUMN_NAME_RelationID,
//                ufinDbContract.RelationMaster.COLUMN_NAME_Description,
//        };
//        String Active = "1";
//
//        String selection1 = ufinDbContract.RelationMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";
//
//        // How you want the results sorted in the resulting Cursor
//        String sortOrder1 = ufinDbContract.RelationMaster.COLUMN_NAME_SequenceID + " ASC";
//
//        Cursor c1 = db.query(
//                ufinDbContract.RelationMaster.TABLE_NAME,  // The table to query
//                projection1,                               // The columns to return
//                selection1,                                // The columns for the WHERE clause
//                null,
//                null,                                     // don't group the rows
//                null,                                     // don't filter by row groups
//                sortOrder1                                 // The sort order
//        );
//
//        try {
//            if (c1 != null && c1.moveToFirst()) {
//                if (c1.getCount() > 0) {
//                    try {
//                        int iCount = c1.getCount();
//                        for (int i = 0; i < iCount; i++) {
//                            int iTemp = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.RelationMaster.COLUMN_NAME_RelationID)));
//                            String str2 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.RelationMaster.COLUMN_NAME_Description));
//                            if(str2.equalsIgnoreCase("Main Member")) {
//                                RelationIDMainMember = iTemp;
//                            } else if(str2.equalsIgnoreCase("Unknown")) {
//                                RelationIDUnknown = iTemp;
//                            }
//                            c1.moveToNext();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        } finally {
//            if(c1 != null) { c1.close(); }
//            db.close();
//        }
//    }
}

