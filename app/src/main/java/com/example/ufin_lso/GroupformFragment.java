package com.example.ufin_lso;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupformFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupformFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View rootView;
    MainActivity mainActivity;

    private static final int CAMERA_REQUEST = 0;
    private static final int ID_REQUEST_PHOTO_Member1       = 1;
    private static final int ID_REQUEST_PHOTO_Member2       = 2;
    private static final int ID_REQUEST_PHOTO_Member3       = 3;
    private static final int ID_REQUEST_PHOTO_Member4       = 4;
    private static final int ID_REQUEST_PHOTO_Member5       = 5;
    public static final int MAX_PHOTO_FILESIZE             = (4 * 1024 * 1024);
//    public static final int MAX_PHOTO_FILESIZE             = (8 * 1024 * 1024);
    int IDRequest = -1;

    public static final int ufin_GROUPFORM_DIALOG_STATE_NONE            = 0;
    public static final int ufin_GROUPFORM_DIALOG_STATE_CLOSE           = 1;
    public static final int ufin_GROUPFORM_DIALOG_STATE_RESUME_PRINTING = 6;
    public static final int ufin_GROUPFORM_DIALOG_STATE_NO_PRINTER      = 7;
    public static final int ufin_GROUPFORM_DIALOG_STATE_VALIDATE        = 24;
    public static final int ufin_GROUPFORM_DIALOG_STATE_SUBMIT          = 25;
    public static final int ufin_GROUPFORM_DIALOG_STATE_CONFIRM_PRINT_BUTTON1 = 27;
    public static final int ufin_GROUPFORM_DIALOG_STATE_CONFIRM_PRINT_BUTTON2 = 28;
    public static final int ufin_GROUPFORM_DIALOG_STATE_CONFIRM_PRINT_BUTTON3 = 29;
    public static final int ufin_GROUPFORM_DIALOG_STATE_CONFIRM_PRINT_BUTTON4 = 30;
    public static final int ufin_GROUPFORM_DIALOG_STATE_CONFIRM_PRINT_BUTTON5 = 31;
//bmp 20-Aug-26
    public static final String MSG_ufin_GROUPFORM_CONFIRM_PRINT_BUTTON =
//bmp 31-Aug-26
//        "Press OK to Continue Print.\nPress Cancel to Abort.";
        "To Print: Check Printer is Connected,\nthen Press OK to Continue Print.\nPress Cancel to Abort.";
//bmp 31-Aug-26
//bmp 20-Aug-26

    public static final String MSG_ufin_GROUPFORM_CONFIRM_CLOSE =
            "Press OK to Close Without Saving Changes";
//    public static final String MSG_ufin_GROUPFORM_CONFIRM_SUBMIT =
//            "Press OK to Submit the Group.";
    public static final String MSG_ufin_GROUPFORM_CONFIRM_SUBMIT =
        "If All Printouts are taken, Press OK to Submit the Group.";
    public static final String MSG_ufin_GROUPFORM_NO_PRINTER =
            "Printer Not Connected.\nPress Cancel and Retry.";
    public static final String MSG_ufin_GROUPFORM_RESUME_PRINTING =
            "After printing is complete, Press OK to Resume Printing";

    public AlertDialog.Builder dlgAlert;
    public static int dlgState = ufin_GROUPFORM_DIALOG_STATE_NONE;

    private ArrayList<Integer> BusinessIDMaster = new ArrayList<Integer>();
    private ArrayList<String> BusinessMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Integer> MaritalStatusIDMaster = new ArrayList<Integer>();
    private ArrayList<String> MaritalStatusMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Integer> RelationIDMaster = new ArrayList<Integer>();
    private ArrayList<String> RelationMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Integer> ReferenceMemberDocumentID_1 = new ArrayList<Integer>();
    private long RelationIDMainMember = 0;
    private long RelationIDSpouse = 0;
    DatePickerDialog dlgDatePicker = null;
    TimePickerDialog dlgTimePicker = null;
    final Calendar myCalendar = Calendar.getInstance();
    int editDateType, editTimeType;
    EditText etGrtDate, etGrtTime, etLoanDisbursementDate;
    EditText etPifDate, etPifTime;
    EditText etGrtLoanDisbursementDate, etGrtLoanDisbursementTime, etGrtMonthlyRepayDate, etGrtMonthlyRepayTime;
    ArrayList<String> NewGroupFormProblemsCheck = new ArrayList<String>();
    long LastTimeInMillis;
    boolean flagRequirementPrint1, flagRequirementPrint2, flagRequirementPrint3, flagRequirementPrint4, flagRequirementPrint5;
    boolean flagDisableNextPrint;
    public boolean ConfirmPrint = false;

    ArrayList<String> alRequiredDocuments = new ArrayList<String>();

    ArrayList<Long> alGrtTimestampMaster = new ArrayList<Long>();
    ArrayList<String> alGrtDateTimeMaster = new ArrayList<String>();
//bmp 20-Aug-25
//    ArrayList<Long> alLoanDisbursementTimestampMaster = new ArrayList<Long>();
//    ArrayList<String> alLoanDisbursementDateTimeMaster = new ArrayList<String>();
//bmp 20-Aug-25

    public GroupformFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CollectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupformFragment newInstance(String param1, String param2) {
        GroupformFragment fragment = new GroupformFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_groupform, container, false);
//        rootView = inflater.inflate(R.layout.fragment_groupform, container, false);
        rootView = inflater.inflate(R.layout.fragment_groupform2, container, false);
        mainActivity = (MainActivity) getActivity();

        mainActivity.SQLite_getActivitySchedule();

        Button bt = (Button) rootView.findViewById(R.id.buttonCloseGroupform);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonSaveGroupform);
        bt.setOnClickListener(this);

        bt = (Button) rootView.findViewById(R.id.buttonSchedule);
        bt.setOnClickListener(this);
        flagDisableNextPrint = false;
        ConfirmPrint = false;
        bt = (Button) rootView.findViewById(R.id.buttonRequirementPreview1);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonRequirementPreview2);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonRequirementPreview3);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonRequirementPreview4);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonRequirementPreview5);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonRequirementPrint1);
        bt.setOnClickListener(this);
        bt.setEnabled(false);
        flagRequirementPrint1 = false;
        bt = (Button) rootView.findViewById(R.id.buttonRequirementPrint2);
        bt.setOnClickListener(this);
        bt.setEnabled(false);
        flagRequirementPrint2 = false;
        bt = (Button) rootView.findViewById(R.id.buttonRequirementPrint3);
        bt.setOnClickListener(this);
        bt.setEnabled(false);
        flagRequirementPrint3 = false;
        bt = (Button) rootView.findViewById(R.id.buttonRequirementPrint4);
        bt.setOnClickListener(this);
        bt.setEnabled(false);
        flagRequirementPrint4 = false;
        bt = (Button) rootView.findViewById(R.id.buttonRequirementPrint5);
        bt.setOnClickListener(this);
        bt.setEnabled(false);
        flagRequirementPrint5 = false;
        ImageButton imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonGroupIDPhoto1);
        imgbt.setOnClickListener(this);
        imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonGroupIDPhoto2);
        imgbt.setOnClickListener(this);
        imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonGroupIDPhoto3);
        imgbt.setOnClickListener(this);
        imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonGroupIDPhoto4);
        imgbt.setOnClickListener(this);
        imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonGroupIDPhoto5);
        imgbt.setOnClickListener(this);

        RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroup2a);
        if(mainActivity.selectedRevisitGroup != null) {
            switch (mainActivity.selectedRevisitGroup.flagGRTQ2a) {
                case -1:
                    rg.clearCheck();
                    break;
                case 0:
                    rg.check(R.id.radioButton2aNo);
                    break;
                case 1:
                    rg.check(R.id.radioButton2aYes);
                    break;
            }
        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton rb = (RadioButton) rootView.findViewById(checkedId);
                if(rb != null ) {
                    mainActivity.selectedRevisitGroup.flagGRTQ2a = -1;
                    if (rb.getId() == R.id.radioButton2aYes) {
                        mainActivity.selectedRevisitGroup.flagGRTQ2a = 1;
                    } else if (rb.getId() == R.id.radioButton2aNo) {
                        mainActivity.selectedRevisitGroup.flagGRTQ2a = 0;
                    }
                }
            }
        });

        SQLite_getBusinessMaster();
        SQLite_getMaritalStatusMaster();
        SQLite_getRelationMaster();

        mainActivity.selectedGroupID = -1;
        mainActivity.selectedClusterID = -1;
        mainActivity.selectedCommunityID = -1;

        final ArrayList<String> GroupInfoList = new ArrayList<String>();
        GroupInfoList.clear();
        final ArrayList<clsRevisitGroup> GroupList = new ArrayList<clsRevisitGroup>();
        GroupList.clear();
        for(int j=0; j<mainActivity.RevisitGroupList.size(); j++) {
            if(mainActivity.RevisitGroupList.get(j).RevisitGroupStatus == clsRevisitGroup.REVISIT_GROUP_STATUS_READYGROUP) {
//bmp 26-Aug-26
                if(mainActivity.RevisitGroupList.get(j).MemberCount < 3)
                    continue;
//bmp 26-Aug-26
                GroupList.add(mainActivity.RevisitGroupList.get(j));
                String strTemp =    "C-" + String.valueOf(mainActivity.RevisitGroupList.get(j).CenterID) + ":" +
                        "G-" + String.valueOf(mainActivity.RevisitGroupList.get(j).GroupID);
                if(String.valueOf(mainActivity.RevisitGroupList.get(j).CenterID).length() >= 17) {
                    strTemp =    "C-" + String.valueOf(mainActivity.RevisitGroupList.get(j).CenterID).substring(0, (String.valueOf(mainActivity.RevisitGroupList.get(j).CenterID).length() - 8)) + ":" +
                            "G-" + String.valueOf(mainActivity.RevisitGroupList.get(j).GroupID).substring(0, (String.valueOf(mainActivity.RevisitGroupList.get(j).GroupID).length() - 8));
                }
                GroupInfoList.add(strTemp);
            }
        }

        ArrayAdapter<String> adapterRevisitGroupInfo = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, GroupInfoList);
        adapterRevisitGroupInfo.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner spRevisitGroupInfo = (Spinner) rootView.findViewById(R.id.spinnerRevisitGroupInfo);
        spRevisitGroupInfo.setAdapter(adapterRevisitGroupInfo);
        spRevisitGroupInfo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainActivity.selectedRevisitGroup = new clsRevisitGroup();
                mainActivity.selectedRevisitGroup.InitRevisitGroup();
                GroupList.get(position).Copy_RevisitGroup(mainActivity.selectedRevisitGroup);
                mainActivity.selectedRevisitGroupMember.clear();
                for(int i=0; i<mainActivity.selectedRevisitGroup.MemberCount; i++) {
                    clsRevisitMember member = mainActivity.RevisitMemberList.get(mainActivity.selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i));
//                    mainActivity.selectedRevisitGroupMember[i] = new clsRevisitMember();
//                    mainActivity.selectedRevisitGroupMember[i].InitRevisitMember();
//                    member.Copy_RevisitMember(mainActivity.selectedRevisitGroupMember[i]);
                    mainActivity.selectedRevisitGroupMember.add(member);
                }
                ArrayList<Long> alDifferentLastGroupID = new ArrayList<Long>();
                alDifferentLastGroupID.clear();
                int iZeroLastGroupIDCount = 0;
                long lGroupID = mainActivity.selectedRevisitGroupMember.get(0).LastGroupID;
                alDifferentLastGroupID.add(lGroupID);
                if(lGroupID == 0)
                    iZeroLastGroupIDCount++;

                for(int i=1; i<mainActivity.selectedRevisitGroup.MemberCount; i++) {
                    long lTemp1 = mainActivity.selectedRevisitGroupMember.get(i).LastGroupID;
                    if(lTemp1 == 0)
                        iZeroLastGroupIDCount++;

                    boolean newFlag = true;
                    for(int j=0; j<alDifferentLastGroupID.size(); j++) {
                        if(lTemp1 == alDifferentLastGroupID.get(j)) {
                            newFlag = false;
                            break;
                        }
                    }
                    if(newFlag) {
                        alDifferentLastGroupID.add(lTemp1);
                    }
                }
                switch(alDifferentLastGroupID.size()) {
                    case 1:
                        //00000 => Not Allowed
                        //xxxxx => Allowed
                        if(iZeroLastGroupIDCount == 0) {
                            mainActivity.selectedRevisitGroup.LastCycleGroupContinued = 1;
                            mainActivity.selectedRevisitGroup.GradeOfGroup = "A";
                        } else {
                            mainActivity.selectedRevisitGroup.GradeOfGroup = "E";
                            mainActivity.selectedRevisitGroup.LastCycleGroupContinued = 0;
                        }
                        break;
                    case 2:
                        mainActivity.selectedRevisitGroup.LastCycleGroupContinued = 0;
                        mainActivity.selectedRevisitGroup.GradeOfGroup = "B";
                        //0000x => Not Allowed
                        //000xx => Allowed "D"
                        //00xxx => Allowed "C"
                        //0xxxx => Allowed "B"
                        //xxxyy => Allowed "B"
                        //xxyyy => Allowed "B"
                        switch (iZeroLastGroupIDCount) {
                            case 0:
                            case 1:
                                mainActivity.selectedRevisitGroup.GradeOfGroup = "B";
                                break;
                            case 2:
                                mainActivity.selectedRevisitGroup.GradeOfGroup = "C";
                                break;
                            case 3:
                                mainActivity.selectedRevisitGroup.GradeOfGroup = "D";
                                break;
                            case 4:
                            case 5:
                                mainActivity.selectedRevisitGroup.GradeOfGroup = "E";
                                break;
                        }
                        break;
                    case 3:
                        mainActivity.selectedRevisitGroup.LastCycleGroupContinued = 0;
                        mainActivity.selectedRevisitGroup.GradeOfGroup = "C";
                        //000xy => Not Allowed
                        //00xxy => Allowed "D"
                        //0xxxy => Allowed "C"
                        //0xxyy => Allowed "C"
                        //zzzxy => Allowed "C"
                        //zzxxy => Allowed "C"
                        //zxxxy => Allowed "C"
                        //zxxyy => Allowed "C"
                        switch (iZeroLastGroupIDCount) {
                            case 0:
                            case 1:
                                mainActivity.selectedRevisitGroup.GradeOfGroup = "C";
                                break;
                            case 2:
                                mainActivity.selectedRevisitGroup.GradeOfGroup = "D";
                                break;
                            case 3:
                            case 4:
                            case 5:
                                mainActivity.selectedRevisitGroup.GradeOfGroup = "E";
                                break;
                        }
                        break;
                    case 4:
                        mainActivity.selectedRevisitGroup.LastCycleGroupContinued = 0;
                        mainActivity.selectedRevisitGroup.GradeOfGroup = "D";
                        //00xyz => Not Allowed
                        //0xxyz => Allowed "D"
                        //wwxyz => Allowed "D"
                        //wxxyz => Allowed "D"
                        switch (iZeroLastGroupIDCount) {
                            case 0:
                            case 1:
                                mainActivity.selectedRevisitGroup.GradeOfGroup = "D";
                                break;
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                                mainActivity.selectedRevisitGroup.GradeOfGroup = "E";
                                break;
                        }
                        break;
                    case 5:
                        mainActivity.selectedRevisitGroup.LastCycleGroupContinued = 0;
                        //0wxyz => Not Allowed
                        //vwxyz => Not Allowed
                        mainActivity.selectedRevisitGroup.GradeOfGroup = "E";
                        break;
                }

                mainActivity.selectedGroupID = mainActivity.selectedRevisitGroup.GroupID;
                mainActivity.selectedClusterID = mainActivity.selectedRevisitGroup.ClusterID;
                mainActivity.selectedCommunityID = mainActivity.selectedRevisitGroup.CommunityID;

                mainActivity.newGroup.pathDocuments = mainActivity.baseDir + "/data";
                mainActivity.newGroup.InitNewGroup();
                mainActivity.newGroup.BranchNo = mainActivity.LSO_BranchNo;
                mainActivity.newGroup.BranchName = mainActivity.LSO_Branch;
                mainActivity.selectedGroupStatusPosition = mainActivity.GROUP_TYPE_LOAN_INPROCESS;
                mainActivity.SQLite_getGroupMembersInfo();
                for(int i=0; i<mainActivity.selectedRevisitGroup.MemberCount; i++) {
                    mainActivity.selectedRevisitGroupMember.get(i).filenamePhoto = "";
//                    for(int j=0; j<mainActivity.selectedRevisitGroup.MemberCount; j++) {
                    for(int j=0; j<mainActivity.newGroup.arrGroupMember.size(); j++) {
                        if(mainActivity.selectedRevisitGroupMember.get(i).MemberID == mainActivity.newGroup.arrGroupMember.get(j).MemberID) {
                            mainActivity.selectedRevisitGroupMember.get(i).filenamePhoto = mainActivity.newGroup.arrGroupMember.get(j).filenamePhoto;
                            break;
                        }
                    }
                }
                mainActivity.selectedRevisitGroup.CollectionMemberID = mainActivity.newGroup.CollectionMemberID;
                mainActivity.selectedRevisitGroup.PIFDate = mainActivity.newGroup.PIFDate;
                mainActivity.selectedRevisitGroup.PIFTime = mainActivity.newGroup.PIFTime;
                mainActivity.selectedRevisitGroup.GroupTestDate = mainActivity.newGroup.GroupTestDate;
                mainActivity.selectedRevisitGroup.GroupTestTime = mainActivity.newGroup.GroupTestTime;
                mainActivity.selectedRevisitGroup.LoanDisbursementDate = mainActivity.newGroup.LoanDisbursementDate;
//bmp 25-Aug-25
                mainActivity.selectedRevisitGroup.LoanDisbursementTime = mainActivity.newGroup.LoanDisbursementTime;
//bmp 25-Aug-25
                mainActivity.newRegroup.pathDocuments = mainActivity.baseDir + "/data";
                mainActivity.newRegroup.InitNewGroup();
                mainActivity.newGroup.Copy_NewGroup(mainActivity.newRegroup);

                RefreshGroupForm();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spRevisitGroupInfo.setSelection(0);
        spRevisitGroupInfo.setFocusable(true);
        spRevisitGroupInfo.setFocusableInTouchMode(true);

        ArrayAdapter<String> adapterMaritalStatus1 = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, MaritalStatusMarathiDescriptionMaster);
        adapterMaritalStatus1.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus1);
        sp.setAdapter(adapterMaritalStatus1);
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainActivity.selectedRevisitGroupMember.get(0).MaritalStatusID = MaritalStatusIDMaster.get(position);
                CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxOtherRelativeNote1);
                cb.setChecked(false);
                mainActivity.selectedRevisitGroupMember.get(0).Single = false;
                if ((mainActivity.selectedRevisitGroupMember.get(0).MaritalStatusID != 1) &&
                        (mainActivity.selectedRevisitGroupMember.get(0).MaritalStatusID != 7)) {
                    cb.setChecked(true);
                    mainActivity.selectedRevisitGroupMember.get(0).Single = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterMaritalStatus2 = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, MaritalStatusMarathiDescriptionMaster);
        adapterMaritalStatus2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
        sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus2);
        sp.setAdapter(adapterMaritalStatus2);
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainActivity.selectedRevisitGroupMember.get(1).MaritalStatusID = MaritalStatusIDMaster.get(position);
                CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxOtherRelativeNote2);
                cb.setChecked(false);
                mainActivity.selectedRevisitGroupMember.get(1).Single = false;
                if ((mainActivity.selectedRevisitGroupMember.get(1).MaritalStatusID != 1) &&
                        (mainActivity.selectedRevisitGroupMember.get(1).MaritalStatusID != 7)) {
                    cb.setChecked(true);
                    mainActivity.selectedRevisitGroupMember.get(1).Single = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterMaritalStatus3 = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, MaritalStatusMarathiDescriptionMaster);
        adapterMaritalStatus3.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
        sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus3);
        sp.setAdapter(adapterMaritalStatus3);
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainActivity.selectedRevisitGroupMember.get(2).MaritalStatusID = MaritalStatusIDMaster.get(position);
                CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxOtherRelativeNote3);
                cb.setChecked(false);
                mainActivity.selectedRevisitGroupMember.get(2).Single = false;
                if ((mainActivity.selectedRevisitGroupMember.get(2).MaritalStatusID != 1) &&
                        (mainActivity.selectedRevisitGroupMember.get(2).MaritalStatusID != 7)) {
                    cb.setChecked(true);
                    mainActivity.selectedRevisitGroupMember.get(2).Single = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterMaritalStatus4 = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, MaritalStatusMarathiDescriptionMaster);
        adapterMaritalStatus4.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
        sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus4);
        sp.setAdapter(adapterMaritalStatus4);
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mainActivity.selectedRevisitGroupMember.get(3).MaritalStatusID = MaritalStatusIDMaster.get(position);
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxOtherRelativeNote4);
                    cb.setChecked(false);
                    mainActivity.selectedRevisitGroupMember.get(3).Single = false;
                    if ((mainActivity.selectedRevisitGroupMember.get(3).MaritalStatusID != 1) &&
                            (mainActivity.selectedRevisitGroupMember.get(3).MaritalStatusID != 7)) {
                        cb.setChecked(true);
                        mainActivity.selectedRevisitGroupMember.get(3).Single = true;
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterMaritalStatus5 = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, MaritalStatusMarathiDescriptionMaster);
        adapterMaritalStatus5.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
        sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus5);
        sp.setAdapter(adapterMaritalStatus5);
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mainActivity.selectedRevisitGroupMember.get(4).MaritalStatusID = MaritalStatusIDMaster.get(position);
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxOtherRelativeNote5);
                    cb.setChecked(false);
                    mainActivity.selectedRevisitGroupMember.get(4).Single = false;
                    if ((mainActivity.selectedRevisitGroupMember.get(4).MaritalStatusID != 1) &&
                            (mainActivity.selectedRevisitGroupMember.get(4).MaritalStatusID != 7)) {
                        cb.setChecked(true);
                        mainActivity.selectedRevisitGroupMember.get(4).Single = true;
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterBusiness1 = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, BusinessMarathiDescriptionMaster);
        adapterBusiness1.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
        sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation1);
        sp.setAdapter(adapterBusiness1);
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainActivity.selectedRevisitGroupMember.get(0).OccupationID = BusinessIDMaster.get(position);
                CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxServiceNote1);
                cb.setChecked(false);
                if(ReferenceMemberDocumentID_1.get(position) == 32) {
                    cb.setChecked(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterBusiness2 = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, BusinessMarathiDescriptionMaster);
        adapterBusiness2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
        sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation2);
        sp.setAdapter(adapterBusiness2);
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainActivity.selectedRevisitGroupMember.get(1).OccupationID = BusinessIDMaster.get(position);
                CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxServiceNote2);
                cb.setChecked(false);
                if(ReferenceMemberDocumentID_1.get(position) == 32) {
                    cb.setChecked(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterBusiness3 = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, BusinessMarathiDescriptionMaster);
        adapterBusiness3.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
        sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation3);
        sp.setAdapter(adapterBusiness3);
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainActivity.selectedRevisitGroupMember.get(2).OccupationID = BusinessIDMaster.get(position);
                CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxServiceNote3);
                cb.setChecked(false);
                if(ReferenceMemberDocumentID_1.get(position) == 32) {
                    cb.setChecked(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterBusiness4 = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, BusinessMarathiDescriptionMaster);
        adapterBusiness4.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
        sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation4);
        sp.setAdapter(adapterBusiness4);
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mainActivity.selectedRevisitGroupMember.get(3).OccupationID = BusinessIDMaster.get(position);
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxServiceNote4);
                    cb.setChecked(false);
                    if (ReferenceMemberDocumentID_1.get(position) == 32) {
                        cb.setChecked(true);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
            ArrayAdapter<String> adapterBusiness5 = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                    R.layout.simple_spinner_item, BusinessMarathiDescriptionMaster);
            adapterBusiness5.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
            sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation5);
            sp.setAdapter(adapterBusiness5);
            sp.setFocusable(true);
            sp.setFocusableInTouchMode(true);
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        mainActivity.selectedRevisitGroupMember.get(4).OccupationID = BusinessIDMaster.get(position);
                        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxServiceNote5);
                        cb.setChecked(false);
                        if (ReferenceMemberDocumentID_1.get(position) == 32) {
                            cb.setChecked(true);
                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        EditText et = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName1);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    String strTemp1 = v.getText().toString();
                    mainActivity.selectedRevisitGroupMember.get(0).GuarantorName = strTemp1;
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    String strTemp1 = ((EditText) view).getText().toString();
                    mainActivity.selectedRevisitGroupMember.get(0).GuarantorName = strTemp1;
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName2);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    String strTemp1 = v.getText().toString();
                    mainActivity.selectedRevisitGroupMember.get(1).GuarantorName = strTemp1;
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    String strTemp1 = ((EditText) view).getText().toString();
                    mainActivity.selectedRevisitGroupMember.get(1).GuarantorName = strTemp1;
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName3);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    String strTemp1 = v.getText().toString();
                    mainActivity.selectedRevisitGroupMember.get(2).GuarantorName = strTemp1;
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    String strTemp1 = ((EditText) view).getText().toString();
                    mainActivity.selectedRevisitGroupMember.get(2).GuarantorName = strTemp1;
                }
            }
        });
        try {
            et = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName4);
            et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                            (actionId == EditorInfo.IME_ACTION_NEXT)) {
                        String strTemp1 = v.getText().toString();
                        mainActivity.selectedRevisitGroupMember.get(3).GuarantorName = strTemp1;
                    }
                    return false;
                }
            });
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                    } else {
                        String strTemp1 = ((EditText) view).getText().toString();
                        mainActivity.selectedRevisitGroupMember.get(3).GuarantorName = strTemp1;
                    }
                }
            });
        } catch ( Exception e) {

        }
        try {
            et = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName5);
            et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                            (actionId == EditorInfo.IME_ACTION_NEXT)) {
                        String strTemp1 = v.getText().toString();
                        mainActivity.selectedRevisitGroupMember.get(4).GuarantorName = strTemp1;
                    }
                    return false;
                }
            });
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                    } else {
                        String strTemp1 = ((EditText) view).getText().toString();
                        mainActivity.selectedRevisitGroupMember.get(4).GuarantorName = strTemp1;
                    }
                }
            });
        } catch (Exception e) {

        }
        ArrayAdapter<String> adapterGuarantorRelation1 = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, RelationMarathiDescriptionMaster);
        adapterGuarantorRelation1.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
        sp = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation1);
        sp.setAdapter(adapterGuarantorRelation1);
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //                    if(posRelation1 == position)
                //                        return;
                mainActivity.selectedRevisitGroupMember.get(0).GuarantorRelationID = RelationIDMaster.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterGuarantorRelation2 = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, RelationMarathiDescriptionMaster);
        adapterGuarantorRelation2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
        sp = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation2);
        sp.setAdapter(adapterGuarantorRelation2);
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //                    if(posRelation1 == position)
                //                        return;
                mainActivity.selectedRevisitGroupMember.get(1).GuarantorRelationID = RelationIDMaster.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterGuarantorRelation3 = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, RelationMarathiDescriptionMaster);
        adapterGuarantorRelation3.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
        sp = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation3);
        sp.setAdapter(adapterGuarantorRelation3);
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //                    if(posRelation1 == position)
                //                        return;
                mainActivity.selectedRevisitGroupMember.get(2).GuarantorRelationID = RelationIDMaster.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterGuarantorRelation4 = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, RelationMarathiDescriptionMaster);
        adapterGuarantorRelation4.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
        sp = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation4);
        sp.setAdapter(adapterGuarantorRelation4);
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    //                    if(posRelation1 == position)
                    //                        return;
                    mainActivity.selectedRevisitGroupMember.get(3).GuarantorRelationID = RelationIDMaster.get(position);
                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
            ArrayAdapter<String> adapterGuarantorRelation5 = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                    R.layout.simple_spinner_item, RelationMarathiDescriptionMaster);
            adapterGuarantorRelation5.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
            sp = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation5);
            sp.setAdapter(adapterGuarantorRelation5);
            sp.setFocusable(true);
            sp.setFocusableInTouchMode(true);
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        //                    if(posRelation1 == position)
                        //                        return;
                        mainActivity.selectedRevisitGroupMember.get(4).GuarantorRelationID = RelationIDMaster.get(position);
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        dlgDatePicker = new DatePickerDialog(getContext(),
                date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        dlgTimePicker = new TimePickerDialog(getContext(),
                time,
                myCalendar.get(Calendar.HOUR_OF_DAY),
                myCalendar.get(Calendar.MINUTE),
                false);

        etPifDate = (EditText) rootView.findViewById(R.id.editTextPIFdate);
        etPifDate.setKeyListener(null);
        etPifDate.setEnabled(true);
        etPifDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(etPifDate.getWindowToken(), 0);

                if (!dlgDatePicker.isShowing()) {
                    editDateType = 4;
                    dlgDatePicker.show();
                }
            }
        });
        etPifDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasFocus) {
                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(etPifDate.getWindowToken(), 0);

                if (hasFocus && !(dlgDatePicker.isShowing())) {
                    editDateType = 4;
                    dlgDatePicker.show();
                }
            }
        });
        etPifTime = (EditText) rootView.findViewById(R.id.editTextPIFtime);
        etPifTime.setKeyListener(null);
        etPifTime.setEnabled(true);
        etPifTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(etPifTime.getWindowToken(), 0);

                if (!dlgTimePicker.isShowing()) {
                    editTimeType = 4;
                    dlgTimePicker.show();
                }
            }
        });
        etPifTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasFocus) {
                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(etPifTime.getWindowToken(), 0);

                if (hasFocus && !(dlgTimePicker.isShowing())) {
                    editTimeType = 4;
                    dlgTimePicker.show();
                }
            }
        });

//        etGrtDate = (EditText) rootView.findViewById(R.id.editTextGRTdate);
//        etGrtDate.setKeyListener(null);
//        etGrtDate.setEnabled(true);
//        etGrtDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//                keyboard.hideSoftInputFromWindow(etGrtDate.getWindowToken(), 0);
//
//                if(!dlgDatePicker.isShowing()) {
//                    editDateType = 5;
//                    dlgDatePicker.show();
//                }
//            }
//        });
//        etGrtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View arg0, boolean hasFocus) {
//                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//                keyboard.hideSoftInputFromWindow(etGrtDate.getWindowToken(), 0);
//
//                if(hasFocus && !(dlgDatePicker.isShowing())) {
//                    editDateType = 5;
//                    dlgDatePicker.show();
//                }
//            }
//        });
//        etGrtTime = (EditText) rootView.findViewById(R.id.editTextGRTtime);
//        etGrtTime.setKeyListener(null);
//        etGrtTime.setEnabled(true);
//        etGrtTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//                keyboard.hideSoftInputFromWindow(etGrtTime.getWindowToken(), 0);
//
//                if (!dlgTimePicker.isShowing()) {
//                    editTimeType = 5;
//                    dlgTimePicker.show();
//                }
//            }
//        });
//        etGrtTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View arg0, boolean hasFocus) {
//                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//                keyboard.hideSoftInputFromWindow(etGrtTime.getWindowToken(), 0);
//
//                if (hasFocus && !(dlgTimePicker.isShowing())) {
//                    editTimeType = 5;
//                    dlgTimePicker.show();
//                }
//            }
//        });

//        etLoanDisbursementDate = (EditText) rootView.findViewById(R.id.editTextLoanDisbursementDate);
//        etLoanDisbursementDate.setKeyListener(null);
//        etLoanDisbursementDate.setEnabled(true);
//        etLoanDisbursementDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//                keyboard.hideSoftInputFromWindow(etLoanDisbursementDate.getWindowToken(), 0);
//
//                if(!dlgDatePicker.isShowing()) {
//                    editDateType = 6;
//                    dlgDatePicker.show();
//                }
//            }
//        });
//        etLoanDisbursementDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View arg0, boolean hasFocus) {
//                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//                keyboard.hideSoftInputFromWindow(etLoanDisbursementDate.getWindowToken(), 0);
//
//                if(hasFocus && !(dlgDatePicker.isShowing())) {
//                    editDateType = 6;
//                    dlgDatePicker.show();
//                }
//            }
//        });

        return rootView;
    }

    public String convertTimeInMillisToString(long lTimeInMillis) {
        String strDateTime = "";

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(lTimeInMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String strDate = "";
        switch(cal.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                strDate = "रविवार";
                break;
            case 2:
                strDate = "सोमवार";
                break;
            case 3:
                strDate = "मंगळवार";
                break;
            case 4:
                strDate = "बुधवार";
                break;
            case 5:
                strDate = "गुरुवार";
                break;
            case 6:
                strDate = "शुक्रवार";
                break;
            case 7:
                strDate = "शनिवार";
                break;
        }
        strDate = strDate + " " + sdf.format(cal.getTime());// all done

        SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        int hourOfDay = Integer.valueOf(sdf3.format(cal.getTime()).substring(0, 2));
        String strTime = "सकाळी ";
        if (cal.get(Calendar.AM_PM) == Calendar.PM) {
            if(hourOfDay < 17)
                strTime = "दुपारी ";
            else if(hourOfDay < 19)
                strTime = "संध्याकाळी ";
            else
                strTime = "रात्री ";
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        strTime = strTime + sdf1.format(cal.getTime());// all done
        strDateTime = strDate + " " + strTime;

        return strDateTime;
    }
    public void CreateAlertDialog(boolean enablePositiveButton, boolean enableNegativeButton) {
        dlgAlert = new AlertDialog.Builder(getContext());
        TextView tvTitle = new TextView(getContext());
        tvTitle.setText(getString(R.string.app_name));
        tvTitle.setTextSize(30);
        tvTitle.setTextColor(getResources().getColor(R.color.colorAccent));
        dlgAlert.setCustomTitle(tvTitle);
        dlgAlert.setIcon(R.drawable.ic_launcher_foreground);
        if(enableNegativeButton) {
            dlgAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked Cancel button
                    dialog.dismiss();

                    switch (dlgState) {
                        case ufin_GROUPFORM_DIALOG_STATE_NONE:
                        case ufin_GROUPFORM_DIALOG_STATE_NO_PRINTER:
                        case ufin_GROUPFORM_DIALOG_STATE_CONFIRM_PRINT_BUTTON1:
                        case ufin_GROUPFORM_DIALOG_STATE_CONFIRM_PRINT_BUTTON2:
                        case ufin_GROUPFORM_DIALOG_STATE_CONFIRM_PRINT_BUTTON3:
                        case ufin_GROUPFORM_DIALOG_STATE_CONFIRM_PRINT_BUTTON4:
                        case ufin_GROUPFORM_DIALOG_STATE_CONFIRM_PRINT_BUTTON5:
                            dlgState = ufin_GROUPFORM_DIALOG_STATE_NONE;
                            break;
                    }
                }
            });
            dlgAlert.setCancelable(true);
        }
        if(enablePositiveButton) {
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    dialog.dismiss();

                    switch (dlgState) {
                        case ufin_GROUPFORM_DIALOG_STATE_NONE:
                        case ufin_GROUPFORM_DIALOG_STATE_NO_PRINTER:
                        case ufin_GROUPFORM_DIALOG_STATE_VALIDATE:
                            dlgState = ufin_GROUPFORM_DIALOG_STATE_NONE;
                            break;
                        case ufin_GROUPFORM_DIALOG_STATE_CLOSE:
                            dlgState = ufin_GROUPFORM_DIALOG_STATE_NONE;
                            mainActivity.InitDesktop();
                            break;
                        case ufin_GROUPFORM_DIALOG_STATE_RESUME_PRINTING:
                            flagDisableNextPrint = false;
                            dlgState = ufin_GROUPFORM_DIALOG_STATE_NONE;
                            break;
                        case ufin_GROUPFORM_DIALOG_STATE_SUBMIT:
                            AsyncCallWS_SubmitGroup taskSubmitGroup = new AsyncCallWS_SubmitGroup();
                            taskSubmitGroup.execute();
                            dlgState = ufin_GROUPFORM_DIALOG_STATE_NONE;
                            break;
                        case ufin_GROUPFORM_DIALOG_STATE_CONFIRM_PRINT_BUTTON1:
                            ConfirmPrint = true;
                            Button bt = (Button) rootView.findViewById(R.id.buttonRequirementPrint1);
                            bt.performClick();
                            break;
                        case ufin_GROUPFORM_DIALOG_STATE_CONFIRM_PRINT_BUTTON2:
                            ConfirmPrint = true;
                            bt = (Button) rootView.findViewById(R.id.buttonRequirementPrint2);
                            bt.performClick();
                            break;
                        case ufin_GROUPFORM_DIALOG_STATE_CONFIRM_PRINT_BUTTON3:
                            ConfirmPrint = true;
                            bt = (Button) rootView.findViewById(R.id.buttonRequirementPrint3);
                            bt.performClick();
                            break;
                        case ufin_GROUPFORM_DIALOG_STATE_CONFIRM_PRINT_BUTTON4:
                            ConfirmPrint = true;
                            bt = (Button) rootView.findViewById(R.id.buttonRequirementPrint4);
                            bt.performClick();
                            break;
                        case ufin_GROUPFORM_DIALOG_STATE_CONFIRM_PRINT_BUTTON5:
                            ConfirmPrint = true;
                            bt = (Button) rootView.findViewById(R.id.buttonRequirementPrint5);
                            bt.performClick();
                            break;
                    }
//                    dlgState = ufin_GROUPFORM_DIALOG_STATE_NONE;
                }
            });
        }
//        dlgAlert.setCancelable(true);
    }
    private class AsyncCallWS_SubmitGroup extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.progressBarGroupform);
            if (pb != null) {
                pb.setIndeterminate(true);
                pb.setVisibility(View.VISIBLE);
            }

            Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerRevisitGroupInfo);
            sp.setEnabled(false);
            sp = (Spinner) rootView.findViewById(R.id.spinnerGroupName);
            sp.setEnabled(false);
            sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation1);
            sp.setEnabled(false);
            sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation2);
            sp.setEnabled(false);
            sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation3);
            sp.setEnabled(false);
            sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation4);
            sp.setEnabled(false);
            sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation5);
            sp.setEnabled(false);
            sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus1);
            sp.setEnabled(false);
            sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus2);
            sp.setEnabled(false);
            sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus3);
            sp.setEnabled(false);
            sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus4);
            sp.setEnabled(false);
            sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus5);
            sp.setEnabled(false);
            sp = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation1);
            sp.setEnabled(false);
            sp = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation2);
            sp.setEnabled(false);
            sp = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation3);
            sp.setEnabled(false);
            sp = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation4);
            sp.setEnabled(false);
            sp = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation5);
            sp.setEnabled(false);
            EditText et = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName1);
            et.setEnabled(false);
            et = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName2);
            et.setEnabled(false);
            et = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName3);
            et.setEnabled(false);
            et = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName4);
            et.setEnabled(false);
            et = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName5);
            et.setEnabled(false);
            et = (EditText) rootView.findViewById(R.id.editTextPIFdate);
            et.setEnabled(false);
            et = (EditText) rootView.findViewById(R.id.editTextPIFtime);
            et.setEnabled(false);
//            et = (EditText) rootView.findViewById(R.id.editTextGRTdate);
//            et.setEnabled(false);
//            et = (EditText) rootView.findViewById(R.id.editTextGRTtime);
//            et.setEnabled(false);
//            et = (EditText) rootView.findViewById(R.id.editTextLoanDisbursementDate);
//            et.setEnabled(false);
            sp = (Spinner) rootView.findViewById(R.id.spinnerGRTdatetime);
            sp.setEnabled(false);
//bmp 20-Aug-25
//            sp = (Spinner) rootView.findViewById(R.id.spinnerLoanDisbursementDatetime);
//            sp.setEnabled(false);
//bmp 20-Aug-25
            ImageButton imgbtn = (ImageButton) rootView.findViewById(R.id.imageButtonGroupIDPhoto1);
            imgbtn.setEnabled(false);
            imgbtn = (ImageButton) rootView.findViewById(R.id.imageButtonGroupIDPhoto2);
            imgbtn.setEnabled(false);
            imgbtn = (ImageButton) rootView.findViewById(R.id.imageButtonGroupIDPhoto3);
            imgbtn.setEnabled(false);
            imgbtn = (ImageButton) rootView.findViewById(R.id.imageButtonGroupIDPhoto4);
            imgbtn.setEnabled(false);
            imgbtn = (ImageButton) rootView.findViewById(R.id.imageButtonGroupIDPhoto5);
            imgbtn.setEnabled(false);

            Button bt = (Button) rootView.findViewById(R.id.buttonCloseGroupform);
            bt.setEnabled(false);
            bt = (Button) rootView.findViewById(R.id.buttonSaveGroupform);
            bt.setEnabled(false);
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
        @Override
        protected Void doInBackground(String... strings) {
            SubmitGroup();

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.progressBarGroupform);
            if(pb != null)
                pb.setVisibility(View.INVISIBLE);

            Button bt = (Button) rootView.findViewById(R.id.buttonCloseGroupform);
            bt.setEnabled(true);
        }
    }
    public void SubmitGroup() {
        Calendar cal = Calendar.getInstance();
        LastTimeInMillis = cal.getTimeInMillis();
        //Submit Group Form
        mainActivity.selectedRevisitGroup.RevisitGroupStatus = clsRevisitGroup.REVISIT_GROUP_STATUS_GROUPFORM_SUBMITTED;
        for (int i = 0; i < mainActivity.selectedRevisitGroup.MemberCount; i++) {
            generatePIFRequirementsPDF(i);

            clsRevisitMember revisitMember =  mainActivity.selectedRevisitGroupMember.get(i);
            revisitMember.RevisitMemberStatus = clsRevisitMember.REVISIT_MEMBER_STATUS_GROUPFORM_SUBMITTED;
            revisitMember.Edited = true;
            revisitMember.Copy_RevisitMember(mainActivity.RevisitMemberList.get(mainActivity.selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i)));
            mainActivity.SQLite_updateRevisitMember(mainActivity.selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i), false);

            clsNewGroupMemberInfo newGroupMemberInfo = new clsNewGroupMemberInfo();
            newGroupMemberInfo.Init_NewGroupMemberInfo();
            newGroupMemberInfo.MemberID = revisitMember.MemberID;
//            if(revisitMember.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) {
            if(     (revisitMember.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) ||
                    (revisitMember.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) ||
                    (revisitMember.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED) ) {
                newGroupMemberInfo.GuarantorName = revisitMember.GuarantorName;
                newGroupMemberInfo.GuarantorRelationID = revisitMember.GuarantorRelationID;
                newGroupMemberInfo.LoanAmount = revisitMember.NextLoanOfferAmount;
                newGroupMemberInfo.LoanDuration = 1;
                newGroupMemberInfo.LoanApplicant = 1;
                newGroupMemberInfo.MemberStatus = clsGroupMember.MEMBER_STATUS_PIF_EDIT;
            } else {
                newGroupMemberInfo.LoanApplicant = 0;
            }
            newGroupMemberInfo.ContactNo = revisitMember.ContactNumber;
            newGroupMemberInfo.GroupID = revisitMember.GroupID;
            LastTimeInMillis += 1000;
            mainActivity.SQLite_saveGroupMemberInfo(newGroupMemberInfo, LastTimeInMillis, true);

            clsMemberData newMember = new clsMemberData();
            newMember.InitNewMember(mainActivity.strToday);
            mainActivity.SQLite_getMembers(revisitMember.MemberID, "MemberID");
            if( mainActivity.Member.size() == 0) {
                newMember.MemberID = revisitMember.MemberID;
            } else {
                newMember =  mainActivity.Member.get(0);
            }
            newMember.MaritalStatusID = revisitMember.MaritalStatusID;
            LastTimeInMillis += 1000;
            mainActivity.SQLite_saveMember(newMember, LastTimeInMillis, true);

            clsPersonalImpactInfo newInfo = new clsPersonalImpactInfo();
            newInfo.Init_PersonalImpactInfo();
            mainActivity.SQLite_getPersonalImpactInfo(revisitMember.MemberID);
            if( mainActivity.PersonalImpactInfo.size() == 0) {
                newInfo.MemberID = revisitMember.MemberID;
            } else {
                newInfo =  mainActivity.PersonalImpactInfo.get(0);
            }
            newInfo.BusinessID = revisitMember.OccupationID;
            newInfo.OwnHouse = revisitMember.OwnHouse;
            newInfo.UpdatedBy = mainActivity.UserID;
            LastTimeInMillis += 1000;
            mainActivity.SQLite_savePersonalImpactInfo(newInfo, LastTimeInMillis, true);
            mainActivity.SQLite_getMemberFamilyDetails(newMember.MemberID);

            mainActivity.SQLite_getMemberImpactAnswer(mainActivity.MemberFamilyDetails.MemberFamilyDetailsID);
            boolean boolDone = false;
            for(int j=0; j<mainActivity.MemberImpactAnswer.size(); j++) {
                clsMemberImpactAnswer oldAnswer = mainActivity.MemberImpactAnswer.get(j);
                if((int) oldAnswer.IPAnswerID == mainActivity.ImpactAnswer.HOUSE_RENTED) {
                    if (revisitMember.OwnHouse == 1) {
                        oldAnswer.IPAnswerID = mainActivity.ImpactAnswer.HOUSE_OWNED;
                        LastTimeInMillis += 1000;
                        mainActivity.SQLite_saveMemberImpactAnswer(oldAnswer, LastTimeInMillis, true);
                    }
                    boolDone = true;
                } else if((int) oldAnswer.IPAnswerID == mainActivity.ImpactAnswer.HOUSE_OWNED) {
                    if (revisitMember.OwnHouse == 0) {
                        oldAnswer.IPAnswerID = mainActivity.ImpactAnswer.HOUSE_RENTED;
                        LastTimeInMillis += 1000;
                        mainActivity.SQLite_saveMemberImpactAnswer(oldAnswer, LastTimeInMillis, true);
                    }
                    boolDone = true;
                }
            }
            if(!boolDone) {
                clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                newAnswer.Init_MemberImpactAnswer();
                newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                if (revisitMember.OwnHouse == 1)
                    newAnswer.IPAnswerID = mainActivity.ImpactAnswer.HOUSE_OWNED;
                else
                    newAnswer.IPAnswerID = mainActivity.ImpactAnswer.HOUSE_RENTED;
                LastTimeInMillis += 1000;
                mainActivity.SQLite_saveMemberImpactAnswer(newAnswer, LastTimeInMillis, true);
            }
            clsMemberDocRcvd newDocRcvd;
            newDocRcvd = new clsMemberDocRcvd();
            newDocRcvd.Init_MemberDocRcvd();
            newDocRcvd.MemberDocumentsID = mainActivity.DocumentIDs.MEMBER_DOC_ID_MEMBER_PHOTO;
            newDocRcvd.MemberID = revisitMember.MemberID;
            newDocRcvd.DocumentNo = "0";
            newDocRcvd.IsActive = 1;

            mainActivity.SQLite_getMemberDocRcvd(revisitMember.MemberID);
            boolean Flag1 = false;
            for(int j=0; j<mainActivity.MemberDocRcvd.size(); j++) {
                if((int)mainActivity.MemberDocRcvd.get(j).MemberDocumentsID == mainActivity.DocumentIDs.MEMBER_DOC_ID_MEMBER_PHOTO) {
                    mainActivity.MemberDocRcvd.set(j, newDocRcvd);
                    LastTimeInMillis += 1000;
                    mainActivity.SQLite_saveMemberDocRcvd(j, LastTimeInMillis, true);
                    newDocRcvd.MemberDocuemntsRcvdID = mainActivity.MemberDocRcvd.get(j).MemberDocuemntsRcvdID;
                    Flag1 = true;
                }
            }
            if(!Flag1) {
                mainActivity.MemberDocRcvd.add(newDocRcvd);
                int newIndex = mainActivity.MemberDocRcvd.size() - 1;
                LastTimeInMillis += 1000;
                mainActivity.SQLite_saveMemberDocRcvd(newIndex, LastTimeInMillis, true);
                newDocRcvd.MemberDocuemntsRcvdID = mainActivity.MemberDocRcvd.get(newIndex).MemberDocuemntsRcvdID;
            }
            String strFilepath = mainActivity.newGroup.pathDocuments + "/";
            String strJpgFilename = "";
            String strDocNo = "";
            int indexMemberDocRcvd = -1;
            int indexDocumentPhoto = -1;
            String strDocPhotoID = "";
            mainActivity.DocumentPhotos.clear();
            mainActivity.tDocumentPhotos.clear();
            mainActivity.SQLite_getDocumentPhotos(newDocRcvd.MemberDocuemntsRcvdID);
            for(int k=0; k<mainActivity.DocumentPhotos.size(); k++) {
                if(newDocRcvd.MemberDocuemntsRcvdID ==
                        mainActivity.DocumentPhotos.get(k).MemberDocuemntsRcvdID) {
                    String strTemp = String.valueOf(mainActivity.DocumentPhotos.get(k).DocumentPhotoID);
                    File file = new File(strFilepath + strTemp + ".jpg");
                    strDocPhotoID = strTemp;
                    indexDocumentPhoto = k;
                    break;
                }
            }
            if(strDocPhotoID.length() > 0) {
                strJpgFilename = strDocPhotoID;
                LastTimeInMillis += 1000;
                mainActivity.SQLite_saveDocumentPhoto(indexDocumentPhoto, LastTimeInMillis, true);
            } else {
                if(strDocNo.isEmpty()) {
                    strDocNo = "0";
                }
                long lTemp = 0;
                if(indexMemberDocRcvd < 0) {
                    lTemp = AddMemberDocRcvd(mainActivity.DocumentIDs.MEMBER_DOC_ID_MEMBER_PHOTO, strDocNo, true);
                    lTemp = mainActivity.AddDocumentPhotos(lTemp, 0, true);
                } else {
                    lTemp = mainActivity.AddDocumentPhotos(
                            mainActivity.MemberDocRcvd.get(indexMemberDocRcvd).MemberDocuemntsRcvdID,
                            0, true);
                }
                strDocPhotoID = String.valueOf(lTemp);
                strJpgFilename = strDocPhotoID;
            }

            File photofile = new File(strFilepath + strJpgFilename + ".jpg");
            String strFilename = "member" + String.valueOf(i+1) + "_photo";
            File jpgfile = new File(strFilepath + strFilename + ".jpg");
            if(jpgfile.exists()) {
                if(photofile.exists()) {
                    photofile.delete();
                }
                jpgfile.renameTo(photofile);
                revisitMember.filenamePhoto = strJpgFilename;
                mainActivity.selectedRevisitGroupMember.get(i).filenamePhoto = strJpgFilename;
            } else {
                if(!mainActivity.selectedRevisitGroupMember.get(i).filenamePhoto.isEmpty()) {
                    File oldfile = new File(strFilepath + mainActivity.selectedRevisitGroupMember.get(i).filenamePhoto + ".jpg");
                    if(photofile.exists()) {
                        photofile.delete();
                    }
                    oldfile.renameTo(photofile);
                    revisitMember.filenamePhoto = strJpgFilename;
                    mainActivity.selectedRevisitGroupMember.get(i).filenamePhoto = strJpgFilename;
                }
            }

            InputStream in = null;
            OutputStream out = null;
            byte[] buffer = new byte[1024];
            try {
                File outFile = new File(mainActivity.baseDir + "/upload/" + strJpgFilename + ".jpg");
                if (outFile.exists()) {
                    outFile.delete();
                }

                in = new FileInputStream(photofile);
                out = new FileOutputStream(outFile);
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
                mainActivity.SyncDocUpload.add(strJpgFilename + ".jpg");
            } catch (IOException e) {
            }
        }

        clsNewGroupInfo newGroupInfo = new clsNewGroupInfo();
        newGroupInfo.Init_NewGroupInfo();
        newGroupInfo.GroupID = mainActivity.selectedRevisitGroup.GroupID;
//bmp 09-Sep-25
//        newGroupInfo.LoanDisbursementDate = mainActivity.selectedRevisitGroup.LoanDisbursementDate;
//        newGroupInfo.LoanDisbursementTime = mainActivity.selectedRevisitGroup.LoanDisbursementTime;
        newGroupInfo.LoanDisbursementDate = "2010-01-01";
        newGroupInfo.LoanDisbursementTime = "00:00";
        newGroupInfo.LoanRepayInstallmentDate = "2010-01-01";
        newGroupInfo.LoanRepayInstallmentTime = "00:00";
//        newGroupInfo.LoanDisbursementDate = "";
//        newGroupInfo.LoanDisbursementTime = "";
//        newGroupInfo.LoanRepayInstallmentDate = "";
//        newGroupInfo.LoanRepayInstallmentTime = "";
//bmp 09-Sep-25
        newGroupInfo.MemberCount = mainActivity.selectedRevisitGroup.MemberCount;
        newGroupInfo.GroupStatus = clsNewGroup.GROUP_STATUS_GROUP_FORM_SUBMIT;
        newGroupInfo.PIFDate = mainActivity.selectedRevisitGroup.PIFDate;
        newGroupInfo.PIFTime = mainActivity.selectedRevisitGroup.PIFTime;
        newGroupInfo.GroupTestDate = mainActivity.selectedRevisitGroup.GroupTestDate;
        newGroupInfo.GroupTestTime = mainActivity.selectedRevisitGroup.GroupTestTime;
        newGroupInfo.CollectionCenterID = mainActivity.selectedRevisitGroup.CenterID;
        newGroupInfo.CollectionMemberID = mainActivity.selectedRevisitGroup.CollectionMemberID;
        newGroupInfo.GradeOfGroup = mainActivity.selectedRevisitGroup.GradeOfGroup;
        if(mainActivity.selectedRevisitGroup.LastCycleGroupContinued == 1)
            newGroupInfo.GradeOfGroup = "A";
        LastTimeInMillis += 1000;
        mainActivity.SQLite_saveNewGroupInfo(newGroupInfo, mainActivity.LSO_ID, LastTimeInMillis, true);
//bmp 02-Apr-24
        String strCollectionMemberName = "";
        for(int i=0; i<mainActivity.selectedRevisitGroup.MemberCount; i++) {
            if(mainActivity.selectedRevisitGroup.CollectionMemberID == mainActivity.selectedRevisitGroupMember.get(i).MemberID) {
                strCollectionMemberName = mainActivity.selectedRevisitGroupMember.get(i).MemberName;
                break;
            }
        }
        mainActivity.insertSchedule(
                "PIF",
                ("G-" + String.valueOf(mainActivity.selectedRevisitGroup.GroupID) + ":" + strCollectionMemberName),
                mainActivity.selectedRevisitGroup.lPIFStartTime,
                mainActivity.selectedRevisitGroup.lPIFEndTime);
        mainActivity.insertSchedule(
                "GRT",
                ("G-" + String.valueOf(mainActivity.selectedRevisitGroup.GroupID)),
                mainActivity.selectedRevisitGroup.lGRTStartTime,
                mainActivity.selectedRevisitGroup.lGRTEndTime);
//bmp 20-Aug-25
//        mainActivity.insertSchedule(
//                "Orientation",
//                ("G-" + String.valueOf(mainActivity.selectedRevisitGroup.GroupID)),
//                mainActivity.selectedRevisitGroup.lOrientationStartTime,
//                mainActivity.selectedRevisitGroup.lOrientationEndTime);
//bmp 20-Aug-25

        String strActivity = "GF Submit: " + ("G-" + String.valueOf(mainActivity.selectedRevisitGroup.GroupID));
        mainActivity.UpdateTabLocation(strActivity);
    }

    public String generatePIFRequirementsPDF(int memberIndex) {
        String strPifFolderpath = mainActivity.baseDir + "/data/pif";
        File pifFolder = new File(strPifFolderpath);
        if(!pifFolder.exists()) {
            pifFolder.mkdir();
        }
        String strMemberFolderpath = strPifFolderpath + "/" + String.valueOf(mainActivity.selectedRevisitGroupMember.get(memberIndex).MemberID);
        File memberFolder = new File(strMemberFolderpath);
        if(!memberFolder.exists()) {
            memberFolder.mkdir();
        }
        String strRequirementPath = strMemberFolderpath + "/";
        String StrPDFRequirementFileName = strRequirementPath + String.valueOf(mainActivity.selectedRevisitGroupMember.get(memberIndex).MemberID) + ".pdf";
        // create a new document
        PdfDocument document = new PdfDocument();
        if(mainActivity.selectedRevisitGroupMember.get(memberIndex).VisitDetailsID
                == clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING) {
            // create a page description
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(576, 1000, 1).create();
            // start a page
            PdfDocument.Page page = document.startPage(pageInfo);
            // draw something on the page
            drawPageOngoingLoanRequirements(page, memberIndex);
            // finish the page
            document.finishPage(page);
        } else {
            ListRequiredDocuments(memberIndex);

            int pageHt = 1800 + (40 * (alRequiredDocuments.size()+3));
            // create a page description
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(576, pageHt, 1).create();
            // start a page
            PdfDocument.Page page = document.startPage(pageInfo);
            // draw something on the page
            drawPagePIFRequirements(page, memberIndex);
            // finish the page
            document.finishPage(page);
        }
        // add more pages
        // write the document content
        File PdfFile = new File(StrPDFRequirementFileName);
        try {
            document.writeTo(new FileOutputStream(PdfFile));
        } catch (FileNotFoundException e) {
        } catch (IOException e1) {
        } finally {
            // close the document
            document.close();
        }
        System.out.println("PIF Requirement: " + StrPDFRequirementFileName);

        return StrPDFRequirementFileName;
    }
    public void ListRequiredDocuments(int memberIndex) {
        alRequiredDocuments.clear();

////////////////////////////////////////////////////////////////
        if(     (mainActivity.selectedRevisitGroupMember.get(memberIndex).MaritalStatusID == 1) ||
                (mainActivity.selectedRevisitGroupMember.get(memberIndex).MaritalStatusID == 7) )
            mainActivity.selectedRevisitGroupMember.get(memberIndex).Single = false;
        else
            mainActivity.selectedRevisitGroupMember.get(memberIndex).Single = true;

        int incentiveGenuineGradeGroup = 0;
        if (mainActivity.selectedRevisitGroup.GradeOfGroup.equalsIgnoreCase("A")) {
            incentiveGenuineGradeGroup = clsRevisitMember.REVISIT_MEMBER_INCENTIVE_GRADE_A_GROUP;
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).PrevMaxLoanAmount > 150000) {
                if(     (mainActivity.selectedRevisitGroupMember.get(memberIndex).OwnHouse == 0) ||
                        (mainActivity.selectedRevisitGroupMember.get(memberIndex).Single) ) {
                    incentiveGenuineGradeGroup = 5000;
                }
            }
        } else if (mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveGenuineBTypeGroup > 0) {
            if (mainActivity.selectedRevisitGroup.GradeOfGroup.equalsIgnoreCase("B")) {
                incentiveGenuineGradeGroup = mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveGenuineBTypeGroup;
            }
        }
        int incentiveLowPrevMaxLoanAmount = 0;
        int incentiveCommunityMeetingNoAttendance = 0;
        int incentiveCommunityMeetingPartAttendance = 5000;
        int incentiveCommunityMeetingFullAttendance = 10000;

        int flagNewMember = 0;
        if(     (mainActivity.selectedRevisitGroupMember.get(memberIndex).LastLoanAmount == 0) &&
                (mainActivity.selectedRevisitGroupMember.get(memberIndex).PrevMaxLoanAmount == 55000) ) {
            flagNewMember = 1;
        }

        if(flagNewMember == 1) {
            incentiveCommunityMeetingPartAttendance = 0;
            incentiveCommunityMeetingFullAttendance = 0;
        } else {
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).PrevMaxLoanAmount > 150000) {
                if ((mainActivity.selectedRevisitGroupMember.get(memberIndex).OwnHouse == 0) ||
                        (mainActivity.selectedRevisitGroupMember.get(memberIndex).Single)) {
                    incentiveCommunityMeetingPartAttendance = 0;
                    incentiveCommunityMeetingFullAttendance = 5000;
                }
            } else {
                int iTemp = mainActivity.selectedRevisitGroupMember.get(memberIndex).PrevMaxLoanAmount;
                iTemp = iTemp + incentiveGenuineGradeGroup;
                iTemp = iTemp + mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveRegularLoanRepay;
                iTemp = iTemp + mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveNotWithdrawingSavingsShares;
                iTemp = iTemp + mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveRegularRDpay;
                iTemp = iTemp + mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveRegularADHPpay;
                iTemp = iTemp + mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveAGMAttendance;
                if(iTemp < 55000) {
                    incentiveCommunityMeetingPartAttendance = 0;
                    incentiveCommunityMeetingFullAttendance = 0;
                    incentiveLowPrevMaxLoanAmount = 55000 - iTemp;
                }
            }
        }
////////////////////////////////////////////////////////////////
        int iLoanAmount = mainActivity.selectedRevisitGroupMember.get(memberIndex).PrevMaxLoanAmount;
        iLoanAmount = iLoanAmount + incentiveGenuineGradeGroup;
        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveRegularLoanRepay;
        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveNotWithdrawingSavingsShares;
        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveRegularRDpay;
        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveRegularADHPpay;
        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveAGMAttendance;
        iLoanAmount = iLoanAmount + incentiveCommunityMeetingNoAttendance;
        iLoanAmount = iLoanAmount + incentiveCommunityMeetingPartAttendance;
        iLoanAmount = iLoanAmount + (incentiveCommunityMeetingFullAttendance - incentiveCommunityMeetingPartAttendance);
        iLoanAmount = iLoanAmount + incentiveLowPrevMaxLoanAmount;
//bmp 12-Jan-24
        mainActivity.selectedRevisitGroupMember.get(memberIndex).flagNewMember = flagNewMember;
        mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveGenuineGradeGroup = incentiveGenuineGradeGroup;
        mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveCommunityMeetingFullAttendance
                = incentiveCommunityMeetingFullAttendance;
        mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveCommunityMeetingPartAttendance
                = incentiveCommunityMeetingPartAttendance;
        mainActivity.selectedRevisitGroupMember.get(memberIndex).NextLoanOfferAmount = iLoanAmount;
        mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveLowPrevMaxLoanAmount = incentiveLowPrevMaxLoanAmount;
//bmp 12-Jan-24

        if(iLoanAmount <= 55000) {
            //Mainmember Aadhaarcard or Electioncard
            alRequiredDocuments.add("सभासदाचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड/इलेक्शनकार्ड");
            //Mainmember Rationcard or Pancard or Aadhaarcard or Electioncard
//            alRequiredDocuments.add("सभासदाच्या राहत्या घराच्या पत्त्याचा पुरावा:रेशनकार्ड/पॅनकार्ड/आधारकार्ड/इलेक्शनकार्ड");
//            alRequiredDocuments.add("राहत्या घराच्या पत्त्याचा पुरावा:रेशनकार्ड/पॅनकार्ड/आधारकार्ड/इलेक्शनकार्ड");
            alRequiredDocuments.add("राहते घर पत्ता पुरावा:रेशनकार्ड/पॅनकार्ड/आधारकार्ड/इलेक्शनकार्ड");
            //Mainmember BankPassbook
            alRequiredDocuments.add("सभासदाचे बँक पासबुक");
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).OwnHouse == 0) {
                alRequiredDocuments.add("घर करारकॉपी / भाड्याची पावती");
            } else {
                alRequiredDocuments.add("सभासदाचे स्वतःचे घर कागदपत्र");
            }
            //Family Photo
            alRequiredDocuments.add("सभासदाच्या संपूर्ण कुटुंबाचा फोटो");
            if( mainActivity.getReferenceMemberDocumentID_1(mainActivity.selectedRevisitGroupMember.get(memberIndex).OccupationID)
                    == mainActivity.DocumentIDs.MEMBER_DOC_ID_KAMACHI_CHITTHI)
            {
                alRequiredDocuments.add("सभासदाच्या कामाची चिट्ठी");
            }

            //Guarantor Aadhaarcard or Electioncard
            alRequiredDocuments.add("जामीनदाराचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड/इलेक्शनकार्ड");
            if(RelationIDSpouse <= 0) {
                SQLite_getRelationMaster();
            }
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).GuarantorRelationID != RelationIDSpouse) {
                alRequiredDocuments.add("इतर जामीनदाराचा फोटो");
                alRequiredDocuments.add("इतर जामीनदार नातेवाईक चिठ्ठी");
            }
        } else if(iLoanAmount <= 65000) {
            //Mainmember Aadhaarcard or Electioncard
            alRequiredDocuments.add("सभासदाचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड/इलेक्शनकार्ड");
            //Mainmember Rationcard or Pancard
            alRequiredDocuments.add("सभासदाच्या राहत्या घराच्या पत्त्याचा पुरावा:रेशनकार्ड/पॅनकार्ड");
            //Mainmember BankPassbook
            alRequiredDocuments.add("सभासदाचे बँक पासबुक");
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).OwnHouse == 0) {
                alRequiredDocuments.add("घर करार कॉपी");
            } else {
                //Own House => Light Bill/Property tax document/Nal Patti/Pani Patti/Photo Pass/Survey Receipt/Survey Receipt/House Rent Receipt
                alRequiredDocuments.add("सभासदाचे स्वतःचे घर कागदपत्र");
            }
            //Family Photo
            alRequiredDocuments.add("सभासदाच्या संपूर्ण कुटुंबाचा फोटो");
            if( mainActivity.getReferenceMemberDocumentID_1(mainActivity.selectedRevisitGroupMember.get(memberIndex).OccupationID)
                    == mainActivity.DocumentIDs.MEMBER_DOC_ID_KAMACHI_CHITTHI)
            {
                alRequiredDocuments.add("सभासदाच्या कामाची चिट्ठी");
            }

            //Guarantor Aadhaarcard or Electioncard
            alRequiredDocuments.add("जामीनदाराचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड/इलेक्शनकार्ड");
            if(RelationIDSpouse <= 0) {
                SQLite_getRelationMaster();
            }
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).GuarantorRelationID != RelationIDSpouse) {
                alRequiredDocuments.add("इतर जामीनदाराचा फोटो");
                alRequiredDocuments.add("इतर जामीनदार नातेवाईक चिठ्ठी");
            }
        } else if(iLoanAmount <= 100000) {
            //Mainmember Aadhaarcard
            alRequiredDocuments.add("सभासदाचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            //Mainmember Rationcard or Pancard
            alRequiredDocuments.add("सभासदाच्या राहत्या घराच्या पत्त्याचा पुरावा:रेशनकार्ड/पॅनकार्ड");
            //Mainmember BankPassbook
            alRequiredDocuments.add("सभासदाचे बँक पासबुक");
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).OwnHouse == 0) {
                alRequiredDocuments.add("घर करार कॉपी");
            } else {
                //Own House => Light Bill/Property tax document/Nal Patti/Pani Patti/Photo Pass/Survey Receipt/Survey Receipt/House Rent Receipt
                alRequiredDocuments.add("सभासदाचे स्वतःचे घर कागदपत्र");
            }
            //Family Photo
            alRequiredDocuments.add("सभासदाच्या संपूर्ण कुटुंबाचा फोटो");
            //Guarantor Aadhaarcard
            alRequiredDocuments.add("जामीनदाराचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            if(RelationIDSpouse <= 0) {
                SQLite_getRelationMaster();
            }
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).GuarantorRelationID != RelationIDSpouse) {
                alRequiredDocuments.add("इतर जामीनदाराचा फोटो");
                alRequiredDocuments.add("इतर जामीनदार नातेवाईक चिठ्ठी");
            }
        } else if(iLoanAmount <= 200000) {
            //Mainmember Aadhaarcard
            alRequiredDocuments.add("सभासदाचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            //Mainmember Rationcard
            alRequiredDocuments.add("सभासदाच्या राहत्या घराच्या पत्त्याचा पुरावा:रेशनकार्ड");
            //Mainmember Pancard
            alRequiredDocuments.add("सभासदाचे पॅनकार्ड");
            //Mainmember BankPassbook
            alRequiredDocuments.add("सभासदाचे बँक पासबुक");
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).OwnHouse == 0) {
                alRequiredDocuments.add("घर करार कॉपी");
            } else {
                //Own House => Light Bill/Property tax document/Nal Patti/Pani Patti/Photo Pass/Survey Receipt/Survey Receipt/House Rent Receipt
                alRequiredDocuments.add("सभासदाचे स्वतःचे घर कागदपत्र");
            }
            //Family Photo
            alRequiredDocuments.add("सभासदाच्या संपूर्ण कुटुंबाचा फोटो");
            if( mainActivity.getReferenceMemberDocumentID_1(mainActivity.selectedRevisitGroupMember.get(memberIndex).OccupationID)
                    == mainActivity.DocumentIDs.MEMBER_DOC_ID_KAMACHI_CHITTHI)
            {
                alRequiredDocuments.add("सभासदाच्या कामाची चिट्ठी");
            }

            //Guarantor Aadhaarcard
            alRequiredDocuments.add("जामीनदाराचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            //Guarantor Pancard
            alRequiredDocuments.add("जामीनदाराचे पॅनकार्ड");
            if(RelationIDSpouse <= 0) {
                SQLite_getRelationMaster();
            }
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).GuarantorRelationID != RelationIDSpouse) {
                alRequiredDocuments.add("इतर जामीनदाराचा फोटो");
                alRequiredDocuments.add("इतर जामीनदार नातेवाईक चिठ्ठी");
            }
        } else if(iLoanAmount <= 300000) {
            //Mainmember Aadhaarcard
            alRequiredDocuments.add("सभासदाचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            //Mainmember Rationcard
            alRequiredDocuments.add("सभासदाच्या राहत्या घराच्या पत्त्याचा पुरावा:रेशनकार्ड");
            //Mainmember Pancard
            alRequiredDocuments.add("सभासदाचे पॅनकार्ड");
            //Mainmember BankPassbook
            alRequiredDocuments.add("सभासदाचे बँक पासबुक");
            //Mainmember BankStatement
            alRequiredDocuments.add("सभासदाचे बँक स्टेटमेंट");
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).OwnHouse == 0) {
                alRequiredDocuments.add("घर करार कॉपी");
            } else {
                //Own House => Light Bill
                alRequiredDocuments.add("सभासदाचे स्वतःचे घर कागदपत्र - Light Bill");
                //Home Loan => Property tax document/Nal Patti/Pani Patti/Photo Pass/Survey Receipt/Survey Receipt/House Rent Receipt
                alRequiredDocuments.add("गृहकर्जासाठी-सभासदाच्या राहत्या घराच्या पुराव्याचे कागदपत्र");
            }
            //Family Photo
            alRequiredDocuments.add("सभासदाच्या संपूर्ण कुटुंबाचा फोटो");
            if( mainActivity.getReferenceMemberDocumentID_1(mainActivity.selectedRevisitGroupMember.get(memberIndex).OccupationID)
                    == mainActivity.DocumentIDs.MEMBER_DOC_ID_KAMACHI_CHITTHI)
            {
                alRequiredDocuments.add("सभासदाच्या कामाची चिट्ठी");
            }

            //JointLoanee Aadhaarcard
            alRequiredDocuments.add("सहकर्जदाराचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            //JointLoanee - Photo, Aadhaarcard, BankPassbook, Bank Statement
            alRequiredDocuments.add("सहकर्जदाराचा फोटो");
            alRequiredDocuments.add("सहकर्जदाराचे बँक पासबुक");
            alRequiredDocuments.add("सहकर्जदाराचे बँक स्टेटमेंट");

            //Guarantor Aadhaarcard
            alRequiredDocuments.add("जामीनदाराचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            //Guarantor Pancard
            alRequiredDocuments.add("जामीनदाराचे पॅनकार्ड");
            //Guarantor BankStatement
            alRequiredDocuments.add("जामीनदाराचे बँक स्टेटमेंट");
            if(RelationIDSpouse <= 0) {
                SQLite_getRelationMaster();
            }
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).GuarantorRelationID != RelationIDSpouse) {
                alRequiredDocuments.add("इतर जामीनदाराचा फोटो");
                alRequiredDocuments.add("इतर जामीनदार नातेवाईक चिठ्ठी");
            }
        } else if(iLoanAmount <= 500000) {
            //Mainmember Aadhaarcard
            alRequiredDocuments.add("सभासदाचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            //Mainmember Rationcard
            alRequiredDocuments.add("सभासदाच्या राहत्या घराच्या पत्त्याचा पुरावा:रेशनकार्ड");
            //Mainmember Pancard
            alRequiredDocuments.add("सभासदाचे पॅनकार्ड");
            //Mainmember BankPassbook
            alRequiredDocuments.add("सभासदाचे बँक पासबुक");
            //Mainmember BankStatement
            alRequiredDocuments.add("सभासदाचे बँक स्टेटमेंट");
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).OwnHouse == 0) {
                alRequiredDocuments.add("घर करार कॉपी");
            } else {
                //Own House => Light Bill
                alRequiredDocuments.add("सभासदाचे स्वतःचे घर कागदपत्र - Light Bill");
                //Home Loan => Property tax document/Nal Patti/Pani Patti/Photo Pass/Survey Receipt/Survey Receipt/House Rent Receipt
                alRequiredDocuments.add("गृहकर्जासाठी-सभासदाच्या राहत्या घराच्या पुराव्याचे कागदपत्र");
            }
            //Business Loan => Shopact
            if(mainActivity.getReferenceMemberDocumentID_1(mainActivity.selectedRevisitGroupMember.get(memberIndex).OccupationID)
                    == mainActivity.DocumentIDs.MEMBER_DOC_ID_SHOPACT_LICENSE) {
                alRequiredDocuments.add("व्यवसाय कर्जासाठी - शॉपऍक्ट लायसेन्स");
            }
            //Family Photo
            alRequiredDocuments.add("सभासदाच्या संपूर्ण कुटुंबाचा फोटो");
            if( mainActivity.getReferenceMemberDocumentID_1(mainActivity.selectedRevisitGroupMember.get(memberIndex).OccupationID)
                    == mainActivity.DocumentIDs.MEMBER_DOC_ID_KAMACHI_CHITTHI)
            {
                alRequiredDocuments.add("सभासदाच्या कामाची चिट्ठी");
            }

            //JointLoanee Aadhaarcard
            alRequiredDocuments.add("सहकर्जदाराचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            //JointLoanee - Photo, Aadhaarcard, BankPassbook, Bank Statement
            alRequiredDocuments.add("सहकर्जदाराचा फोटो");
            alRequiredDocuments.add("सहकर्जदाराचे बँक पासबुक");
            alRequiredDocuments.add("सहकर्जदाराचे बँक स्टेटमेंट");

            //Guarantor Aadhaarcard
            alRequiredDocuments.add("जामीनदाराचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            //Guarantor Pancard
            alRequiredDocuments.add("जामीनदाराचे पॅनकार्ड");
            //Guarantor BankStatement
            alRequiredDocuments.add("जामीनदाराचे बँक स्टेटमेंट");
            if(RelationIDSpouse <= 0) {
                SQLite_getRelationMaster();
            }
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).GuarantorRelationID != RelationIDSpouse) {
                alRequiredDocuments.add("इतर जामीनदाराचा फोटो");
                alRequiredDocuments.add("इतर जामीनदार नातेवाईक चिठ्ठी");
            }
        } else if(iLoanAmount <= 700000) {
            //Mainmember Aadhaarcard
            alRequiredDocuments.add("सभासदाचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            //Mainmember Rationcard
            alRequiredDocuments.add("सभासदाच्या राहत्या घराच्या पत्त्याचा पुरावा:रेशनकार्ड");
            //Mainmember Pancard
            alRequiredDocuments.add("सभासदाचे पॅनकार्ड");
            //Mainmember BankPassbook
            alRequiredDocuments.add("सभासदाचे बँक पासबुक");
            //Mainmember BankStatement
            alRequiredDocuments.add("सभासदाचे बँक स्टेटमेंट");
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).OwnHouse == 0) {
                alRequiredDocuments.add("घर करार कॉपी");
            } else {
                //Own House => Light Bill
                alRequiredDocuments.add("सभासदाचे स्वतःचे घर कागदपत्र - Light Bill");
                //Home Loan => House Documents/Builder Receipt/Allotment Letter/Property tax document/Nal Patti/Pani Patti/Photo Pass/Survey Receipt/Survey Receipt/House Rent Receipt
                alRequiredDocuments.add("गृहकर्जासाठी-सभासदाच्या राहत्या घराच्या पुराव्याचे कागदपत्र");
            }
            //Business Loan => Shopact
            if(mainActivity.getReferenceMemberDocumentID_1(mainActivity.selectedRevisitGroupMember.get(memberIndex).OccupationID)
                    == mainActivity.DocumentIDs.MEMBER_DOC_ID_SHOPACT_LICENSE) {
                alRequiredDocuments.add("व्यवसाय कर्जासाठी - शॉपऍक्ट लायसेन्स");
            }
            //Family Photo
            alRequiredDocuments.add("सभासदाच्या संपूर्ण कुटुंबाचा फोटो");
            if( mainActivity.getReferenceMemberDocumentID_1(mainActivity.selectedRevisitGroupMember.get(memberIndex).OccupationID)
                    == mainActivity.DocumentIDs.MEMBER_DOC_ID_KAMACHI_CHITTHI)
            {
                alRequiredDocuments.add("सभासदाच्या कामाची चिट्ठी");
            }

            //JointLoanee Aadhaarcard
            alRequiredDocuments.add("सहकर्जदाराचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            //JointLoanee - Photo, Aadhaarcard, BankPassbook, Bank Statement
            alRequiredDocuments.add("सहकर्जदाराचा फोटो");
            alRequiredDocuments.add("सहकर्जदाराचे बँक पासबुक");
            alRequiredDocuments.add("सहकर्जदाराचे बँक स्टेटमेंट");

            //Guarantor Aadhaarcard
            alRequiredDocuments.add("जामीनदाराचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            //Guarantor Pancard
            alRequiredDocuments.add("जामीनदाराचे पॅनकार्ड");
            //Guarantor BankStatement
            alRequiredDocuments.add("जामीनदाराचे बँक स्टेटमेंट");
            if(RelationIDSpouse <= 0) {
                SQLite_getRelationMaster();
            }
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).GuarantorRelationID != RelationIDSpouse) {
                alRequiredDocuments.add("इतर जामीनदाराचा फोटो");
                alRequiredDocuments.add("इतर जामीनदार नातेवाईक चिठ्ठी");
            }
        } else if(iLoanAmount <= 1000000) {
            //Mainmember Aadhaarcard
            alRequiredDocuments.add("सभासदाचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            //Mainmember Rationcard
            alRequiredDocuments.add("सभासदाच्या राहत्या घराच्या पत्त्याचा पुरावा:रेशनकार्ड");
            //Mainmember Pancard
            alRequiredDocuments.add("सभासदाचे पॅनकार्ड");
            //Mainmember BankPassbook
            alRequiredDocuments.add("सभासदाचे बँक पासबुक");
            //Mainmember BankStatement
            alRequiredDocuments.add("सभासदाचे बँक स्टेटमेंट");
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).OwnHouse == 0) {
                alRequiredDocuments.add("घर करार कॉपी");
            } else {
                //Own House => Light Bill
                alRequiredDocuments.add("सभासदाचे स्वतःचे घर कागदपत्र - Light Bill");
                //Home Loan => House Documents/Builder Receipt/Allotment Letter/Property tax document/Nal Patti/Pani Patti/Photo Pass/Survey Receipt/Survey Receipt/House Rent Receipt
                alRequiredDocuments.add("गृहकर्जासाठी-सभासदाच्या राहत्या घराच्या पुराव्याचे कागदपत्र");
            }
            //Business Loan => Shopact
            if(mainActivity.getReferenceMemberDocumentID_1(mainActivity.selectedRevisitGroupMember.get(memberIndex).OccupationID)
                    == mainActivity.DocumentIDs.MEMBER_DOC_ID_SHOPACT_LICENSE) {
                alRequiredDocuments.add("व्यवसाय कर्जासाठी - शॉपऍक्ट लायसेन्स");
            }
            //Family Photo
            alRequiredDocuments.add("सभासदाच्या संपूर्ण कुटुंबाचा फोटो");
            if( mainActivity.getReferenceMemberDocumentID_1(mainActivity.selectedRevisitGroupMember.get(memberIndex).OccupationID)
                    == mainActivity.DocumentIDs.MEMBER_DOC_ID_KAMACHI_CHITTHI)
            {
                alRequiredDocuments.add("सभासदाच्या कामाची चिट्ठी");
            }

            //JointLoanee Aadhaarcard
            alRequiredDocuments.add("सहकर्जदाराचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            //JointLoanee - Photo, Aadhaarcard, BankPassbook, Bank Statement
            alRequiredDocuments.add("सहकर्जदाराचा फोटो");
            alRequiredDocuments.add("सहकर्जदाराचे बँक पासबुक");
            alRequiredDocuments.add("सहकर्जदाराचे बँक स्टेटमेंट");

            //Guarantor Aadhaarcard
            alRequiredDocuments.add("जामीनदाराचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            //Guarantor Pancard
            alRequiredDocuments.add("जामीनदाराचे पॅनकार्ड");
            //Guarantor BankStatement
            alRequiredDocuments.add("जामीनदाराचे बँक स्टेटमेंट");
            if(RelationIDSpouse <= 0) {
                SQLite_getRelationMaster();
            }
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).GuarantorRelationID != RelationIDSpouse) {
                alRequiredDocuments.add("इतर जामीनदाराचा फोटो");
                alRequiredDocuments.add("इतर जामीनदार नातेवाईक चिठ्ठी");
            }
        } else if(iLoanAmount <= 1200000) {
            //Mainmember Aadhaarcard
            alRequiredDocuments.add("सभासदाचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            //Mainmember Rationcard
            alRequiredDocuments.add("सभासदाच्या राहत्या घराच्या पत्त्याचा पुरावा:रेशनकार्ड");
            //Mainmember Pancard
            alRequiredDocuments.add("सभासदाचे पॅनकार्ड");
            //Mainmember BankPassbook
            alRequiredDocuments.add("सभासदाचे बँक पासबुक");
            //Mainmember BankStatement
            alRequiredDocuments.add("सभासदाचे बँक स्टेटमेंट");
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).OwnHouse == 0) {
                alRequiredDocuments.add("घर करार कॉपी");
            } else {
                //Own House => Light Bill
                alRequiredDocuments.add("सभासदाचे स्वतःचे घर कागदपत्र - Light Bill");
                //Home Loan => House Documents/Builder Receipt/Allotment Letter/Property tax document/Nal Patti/Pani Patti/Photo Pass/Survey Receipt/Survey Receipt/House Rent Receipt
                alRequiredDocuments.add("गृहकर्जासाठी-सभासदाच्या राहत्या घराच्या पुराव्याचे कागदपत्र");
            }
            //Business Loan => Shopact
            if(mainActivity.getReferenceMemberDocumentID_1(mainActivity.selectedRevisitGroupMember.get(memberIndex).OccupationID)
                    == mainActivity.DocumentIDs.MEMBER_DOC_ID_SHOPACT_LICENSE) {
                alRequiredDocuments.add("व्यवसाय कर्जासाठी - शॉपऍक्ट लायसेन्स");
            }
            //Family Photo
            alRequiredDocuments.add("सभासदाच्या संपूर्ण कुटुंबाचा फोटो");
            if( mainActivity.getReferenceMemberDocumentID_1(mainActivity.selectedRevisitGroupMember.get(memberIndex).OccupationID)
                    == mainActivity.DocumentIDs.MEMBER_DOC_ID_KAMACHI_CHITTHI)
            {
                alRequiredDocuments.add("सभासदाच्या कामाची चिट्ठी");
            }

            //JointLoanee Aadhaarcard
            alRequiredDocuments.add("सहकर्जदाराचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            //JointLoanee - Photo, Aadhaarcard, BankPassbook, Bank Statement
            alRequiredDocuments.add("सहकर्जदाराचा फोटो");
            alRequiredDocuments.add("सहकर्जदाराचे बँक पासबुक");
            alRequiredDocuments.add("सहकर्जदाराचे बँक स्टेटमेंट");

            //Guarantor Aadhaarcard
            alRequiredDocuments.add("जामीनदाराचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            //Guarantor Pancard
            alRequiredDocuments.add("जामीनदाराचे पॅनकार्ड");
            //Guarantor BankStatement
            alRequiredDocuments.add("जामीनदाराचे बँक स्टेटमेंट");
            if(RelationIDSpouse <= 0) {
                SQLite_getRelationMaster();
            }
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).GuarantorRelationID != RelationIDSpouse) {
                alRequiredDocuments.add("इतर जामीनदाराचा फोटो");
                alRequiredDocuments.add("इतर जामीनदार नातेवाईक चिठ्ठी");
            }
        } else {
            //Mainmember Aadhaarcard
            alRequiredDocuments.add("सभासदाचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            //Mainmember Rationcard
            alRequiredDocuments.add("सभासदाच्या राहत्या घराच्या पत्त्याचा पुरावा:रेशनकार्ड");
            //Mainmember Pancard
            alRequiredDocuments.add("सभासदाचे पॅनकार्ड");
            //Mainmember BankPassbook
            alRequiredDocuments.add("सभासदाचे बँक पासबुक");
            //Mainmember BankStatement
            alRequiredDocuments.add("सभासदाचे बँक स्टेटमेंट");
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).OwnHouse == 0) {
                alRequiredDocuments.add("घर करार कॉपी");
            } else {
                //Own House => Light Bill
                alRequiredDocuments.add("सभासदाचे स्वतःचे घर कागदपत्र - Light Bill");
                //Home Loan => House Documents/Builder Receipt/Allotment Letter/Property tax document/Nal Patti/Pani Patti/Photo Pass/Survey Receipt/Survey Receipt/House Rent Receipt
                alRequiredDocuments.add("गृह कर्जासाठी-सभासदाच्या राहत्या घराच्या पुराव्याचे कागदपत्र");
            }
            //Business Loan => Shopact
            if(mainActivity.getReferenceMemberDocumentID_1(mainActivity.selectedRevisitGroupMember.get(memberIndex).OccupationID)
                    == mainActivity.DocumentIDs.MEMBER_DOC_ID_SHOPACT_LICENSE) {
                alRequiredDocuments.add("व्यवसाय कर्जासाठी - शॉपऍक्ट लायसेन्स");
            }
            //Family Photo
            alRequiredDocuments.add("सभासदाच्या संपूर्ण कुटुंबाचा फोटो");
            if( mainActivity.getReferenceMemberDocumentID_1(mainActivity.selectedRevisitGroupMember.get(memberIndex).OccupationID)
                    == mainActivity.DocumentIDs.MEMBER_DOC_ID_KAMACHI_CHITTHI)
            {
                alRequiredDocuments.add("सभासदाच्या कामाची चिट्ठी");
            }

            //JointLoanee Aadhaarcard
            alRequiredDocuments.add("सहकर्जदाराचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            //JointLoanee - Photo, Aadhaarcard, BankPassbook, Bank Statement
            alRequiredDocuments.add("सहकर्जदाराचा फोटो");
            alRequiredDocuments.add("सहकर्जदाराचे बँक पासबुक");
            alRequiredDocuments.add("सहकर्जदाराचे बँक स्टेटमेंट");

            //Guarantor Aadhaarcard
            alRequiredDocuments.add("जामीनदाराचे ओळखपत्र/वयाचा पुरावा:आधारकार्ड");
            //Guarantor Pancard
            alRequiredDocuments.add("जामीनदाराचे पॅनकार्ड");
            //Guarantor BankStatement
            alRequiredDocuments.add("जामीनदाराचे बँक स्टेटमेंट");
            if(RelationIDSpouse <= 0) {
                SQLite_getRelationMaster();
            }
            if(mainActivity.selectedRevisitGroupMember.get(memberIndex).GuarantorRelationID != RelationIDSpouse) {
                alRequiredDocuments.add("इतर जामीनदाराचा फोटो");
                alRequiredDocuments.add("इतर जामीनदार नातेवाईक चिठ्ठी");
            }
        }
    }
    public void drawPagePIFRequirements(PdfDocument.Page page, int memberIndex) {
        Paint text_paint = new Paint();
        text_paint.setColor(Color.BLACK);
        Typeface tf = Typeface.create("Arial", Typeface.NORMAL);
        text_paint.setTypeface(tf);
        text_paint.setTextSize(40);

        Paint border_paint = new Paint();
        border_paint.setColor(Color.BLACK);
        border_paint.setStrokeWidth((float) 3.0);

        Canvas canvas = page.getCanvas();

        canvas.drawColor(Color.WHITE);

        canvas.drawLine(0, 0, 575, 0, border_paint);
//bmp 12-Nov-25
//        text_paint.setTextSize(28);
        text_paint.setTextSize(24);
//bmp 12-Nov-25
        canvas.drawText(String.format("%s", getString(R.string.Receipt_OrgName)), 20, 40, text_paint);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_OrgRegNo)), 120, 80, text_paint);
        canvas.drawLine(5, 95, 570, 95, border_paint);
        text_paint.setTextSize(24);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String strDate = sdf.format(date);// all done

        canvas.drawText(String.format("%s", strDate), 0, 120, text_paint);
        canvas.drawText(String.format("%s", "पुढील कर्जाबाबतचे वेळापत्रक"), 200, 120, text_paint);

        canvas.drawText(String.format("%s", "ब्रँच"), 0, 190, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.BranchName), 80, 190, text_paint);

        canvas.drawText(String.format("%s", "सेंटर ID"), 0, 220, text_paint);
        String strTemp = String.valueOf(mainActivity.selectedRevisitGroupMember.get(memberIndex).CenterID);
        if(strTemp.length() > 10)
            strTemp = strTemp.substring(0, 10);
        canvas.drawText(String.format("%s%s", ":", strTemp), 80, 220, text_paint);
        canvas.drawText(String.format("%s", "ग्रुप ID"), 250, 220, text_paint);
        strTemp = String.valueOf(mainActivity.selectedRevisitGroupMember.get(memberIndex).GroupID);
        if(strTemp.length() > 10)
            strTemp = strTemp.substring(0, 10);
        canvas.drawText(String.format("%s%s", ":", strTemp), 320, 220, text_paint);
//bmp 17-Aug-26
//        canvas.drawText(String.format("%s%s", "ग्रेड:", String.valueOf(mainActivity.selectedRevisitGroup.GradeOfGroup)), 500, 220, text_paint);
//bmp 17-Aug-26
        canvas.drawText(String.format("%s", "सभासद"), 0, 260, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.selectedRevisitGroupMember.get(memberIndex).MemberName), 80, 260, text_paint);
        canvas.drawText(String.format("%s", "मोबाइल"), 0, 290, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.selectedRevisitGroupMember.get(memberIndex).ContactNumber), 80, 290, text_paint);
        canvas.drawText(String.format("%s", "खाते क्र."), 250, 290, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.selectedRevisitGroupMember.get(memberIndex).SavingAccNo), 320, 290, text_paint);

        int iRowOffset = 330;
        if(mainActivity.selectedRevisitGroupMember.get(memberIndex).HomeLoanAmount > 0) {
            canvas.drawText(String.format("%s%s%s", "सभासदाला मिळू शकणारी गृहकर्ज रक्कम: ", String.valueOf(mainActivity.selectedRevisitGroupMember.get(memberIndex).HomeLoanAmount), "/-"), 0, iRowOffset, text_paint);
            iRowOffset += 40;
        }
//bmp 07-Jun-25
//        canvas.drawText(String.format("%s", "सभासदाला मिळू शकणारी इतर कर्ज रक्कम"), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s", "सभासदाला मिळू शकणारी कर्ज रक्कम"), 0, iRowOffset, text_paint);
//bmp 07-Jun-25
        iRowOffset += 10;

        int iRowHt = 40;
        int iTblOffset = iRowOffset;
        iRowOffset = iTblOffset - iRowHt;
        int iColWd[] = {375, 90, 110};
        int iColOffset = 0;

        //Horizontal lines
        for (int i = 0; i < 13; i++) {
            iRowOffset += iRowHt;
            canvas.drawLine(0, iRowOffset, 575, iRowOffset, border_paint);
        }
        //Vertical lines
        canvas.drawLine(iColOffset, iTblOffset, iColOffset, iRowOffset, border_paint);
        for (int i = 0; i < 3; i++) {
            iColOffset += iColWd[i];
            canvas.drawLine(iColOffset, iTblOffset, iColOffset, iRowOffset, border_paint);
        }
        iColOffset = iColWd[0] + 5;
        iRowOffset = iTblOffset + 25;

////////////////////////////////////////////////////////////////
//bmp 12-Jan-24
//        if(     (mainActivity.selectedRevisitGroupMember[memberIndex].MaritalStatusID == 1) ||
//                (mainActivity.selectedRevisitGroupMember[memberIndex].MaritalStatusID == 7) )
//            mainActivity.selectedRevisitGroupMember[memberIndex].Single = false;
//        else
//            mainActivity.selectedRevisitGroupMember[memberIndex].Single = true;
//
//        int incentiveGenuineGradeGroup = 0;
//        if (mainActivity.selectedRevisitGroup.GradeOfGroup.equalsIgnoreCase("A")) {
//            incentiveGenuineGradeGroup = clsRevisitMember.REVISIT_MEMBER_INCENTIVE_GRADE_A_GROUP;
//            if(mainActivity.selectedRevisitGroupMember[memberIndex].PrevMaxLoanAmount > 150000) {
//                if(     (mainActivity.selectedRevisitGroupMember[memberIndex].OwnHouse == 0) ||
//                        (mainActivity.selectedRevisitGroupMember[memberIndex].Single) ) {
//                    incentiveGenuineGradeGroup = 5000;
//                }
//            }
//        } else if (mainActivity.selectedRevisitGroupMember[memberIndex].incentiveGenuineBTypeGroup > 0) {
//            if (mainActivity.selectedRevisitGroup.GradeOfGroup.equalsIgnoreCase("B")) {
//                incentiveGenuineGradeGroup = mainActivity.selectedRevisitGroupMember[memberIndex].incentiveGenuineBTypeGroup;
//            }
//        }
//        int incentiveLowPrevMaxLoanAmount = 0;
//        int incentiveCommunityMeetingNoAttendance = 0;
//        int incentiveCommunityMeetingPartAttendance = 5000;
//        int incentiveCommunityMeetingFullAttendance = 10000;
//
//        boolean flagNewMember = false;
//        if(     (mainActivity.selectedRevisitGroupMember[memberIndex].LastLoanAmount == 0) &&
//                (mainActivity.selectedRevisitGroupMember[memberIndex].PrevMaxLoanAmount == 55000) ) {
//            flagNewMember = true;
//        }
//
//        if(flagNewMember) {
//            incentiveCommunityMeetingPartAttendance = 0;
//            incentiveCommunityMeetingFullAttendance = 0;
//        } else {
//            if(mainActivity.selectedRevisitGroupMember[memberIndex].PrevMaxLoanAmount > 150000) {
//                if ((mainActivity.selectedRevisitGroupMember[memberIndex].OwnHouse == 0) ||
//                        (mainActivity.selectedRevisitGroupMember[memberIndex].Single)) {
//                    incentiveCommunityMeetingPartAttendance = 0;
//                    incentiveCommunityMeetingFullAttendance = 5000;
//                }
//            } else {
//                int iTemp = mainActivity.selectedRevisitGroupMember[memberIndex].PrevMaxLoanAmount;
//                iTemp = iTemp + incentiveGenuineGradeGroup;
//                iTemp = iTemp + mainActivity.selectedRevisitGroupMember[memberIndex].incentiveRegularLoanRepay;
//                iTemp = iTemp + mainActivity.selectedRevisitGroupMember[memberIndex].incentiveNotWithdrawingSavingsShares;
//                iTemp = iTemp + mainActivity.selectedRevisitGroupMember[memberIndex].incentiveRegularRDpay;
//                iTemp = iTemp + mainActivity.selectedRevisitGroupMember[memberIndex].incentiveRegularADHPpay;
//                iTemp = iTemp + mainActivity.selectedRevisitGroupMember[memberIndex].incentiveAGMAttendance;
//                if(iTemp < 55000) {
//                    incentiveCommunityMeetingPartAttendance = 0;
//                    incentiveCommunityMeetingFullAttendance = 0;
//                    incentiveLowPrevMaxLoanAmount = 55000 - iTemp;
//                }
//            }
//        }
//bmp 12-Jan-24
////////////////////////////////////////////////////////////////
        canvas.drawText(String.format("%s", "वाढीव कर्ज रक्कमेचे निकष"), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf("वाढ रु.")), iColOffset, iRowOffset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf("एकूण रु.")), (iColOffset + iColWd[1]), iRowOffset, text_paint);
        iRowOffset += iRowHt;

//        canvas.drawText(String.format("%s", "फ़िटलेल्या/पहिल्या कर्जाची रक्कम"), 0, iRowOffset, text_paint);
        if(mainActivity.selectedRevisitGroupMember.get(memberIndex).flagNewMember == 1) {
            canvas.drawText(String.format("%s", "नवीन सभासदासाठी पहिल्या कर्जाची रक्कम"), 0, iRowOffset, text_paint);
        } else {
            canvas.drawText(String.format("%s", "आधीच्या 5 कर्जापैकी सर्वात मोठी रक्कम"), 0, iRowOffset, text_paint);
        }
        canvas.drawText(String.format("%s", String.valueOf(" --- ")), iColOffset, iRowOffset, text_paint);
        int iLoanAmount = mainActivity.selectedRevisitGroupMember.get(memberIndex).PrevMaxLoanAmount;
        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember.get(memberIndex).PrevMaxLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
        iRowOffset += iRowHt;
        canvas.drawText(String.format("%s", "A/* ग्रेड गट बनवण्याबाबत"), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveGenuineGradeGroup)), iColOffset, iRowOffset, text_paint);
        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveGenuineGradeGroup;
        canvas.drawText(String.format("%s", String.valueOf(iLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
        iRowOffset += iRowHt;
        canvas.drawText(String.format("%s", "आधीच्या कर्ज परतफेडीनुसार"), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveRegularLoanRepay)), iColOffset, iRowOffset, text_paint);
        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveRegularLoanRepay;
        canvas.drawText(String.format("%s", String.valueOf(iLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
        iRowOffset += iRowHt;
        canvas.drawText(String.format("%s", "बचत व शेअर्स काढून न घेतल्यास"), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveNotWithdrawingSavingsShares)), iColOffset, iRowOffset, text_paint);
        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveNotWithdrawingSavingsShares;
        canvas.drawText(String.format("%s", String.valueOf(iLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
        iRowOffset += iRowHt;
        canvas.drawText(String.format("%s", "आर. डी. नियमित भरण्याबाबत"), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveRegularRDpay)), iColOffset, iRowOffset, text_paint);
        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveRegularRDpay;
        canvas.drawText(String.format("%s", String.valueOf(iLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
        iRowOffset += iRowHt;
        canvas.drawText(String.format("%s", "आधारपूर्णा नियमित भरण्याबाबत"), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveRegularADHPpay)), iColOffset, iRowOffset, text_paint);
        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveRegularADHPpay;
        canvas.drawText(String.format("%s", String.valueOf(iLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
        iRowOffset += iRowHt;
        canvas.drawText(String.format("%s", "वार्षिक मिटिंगला हजर राहिल्यास"), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveAGMAttendance)), iColOffset, iRowOffset, text_paint);
        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveAGMAttendance;
        canvas.drawText(String.format("%s", String.valueOf(iLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
        iRowOffset += iRowHt;
        canvas.drawText(String.format("%s", "कम्युनिटी मिटिंगला गैरहजर राहिल्यास"), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveCommunityMeetingNoAttendance)), iColOffset, iRowOffset, text_paint);
        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveCommunityMeetingNoAttendance;
        canvas.drawText(String.format("%s", String.valueOf(iLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
        iRowOffset += iRowHt;
        canvas.drawText(String.format("%s", "कम्युनिटी मिटिंगला अर्धा वेळ हजर असल्यास"), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveCommunityMeetingPartAttendance)), iColOffset, iRowOffset, text_paint);
        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveCommunityMeetingPartAttendance;
        canvas.drawText(String.format("%s", String.valueOf(iLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
        iRowOffset += iRowHt;
        canvas.drawText(String.format("%s", "कम्युनिटी मिटिंगला पूर्ण वेळ हजर राहिल्यास"), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveCommunityMeetingFullAttendance)), iColOffset, iRowOffset, text_paint);
        iLoanAmount = iLoanAmount + (mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveCommunityMeetingFullAttendance - mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveCommunityMeetingPartAttendance);
        canvas.drawText(String.format("%s", String.valueOf(iLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
        iRowOffset += iRowHt;
        canvas.drawText(String.format("%s", "मिळू शकणारी अंदाजे कर्ज रक्कम"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", String.valueOf(" --- ")), iColOffset, iRowOffset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveLowPrevMaxLoanAmount)), iColOffset, iRowOffset, text_paint);
        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember.get(memberIndex).incentiveLowPrevMaxLoanAmount;
        canvas.drawText(String.format("%s", String.valueOf(iLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
        iRowOffset += iRowHt;

        iRowOffset += 10;
        canvas.drawText(String.format("%s", "वैयक्तिक माहिती (पीआयएफ) साठी आवश्यक ओरिजिनल कागदपत्रे"), 0, iRowOffset, text_paint);
        iRowOffset += 10;

        iTblOffset = iRowOffset;
        iRowOffset = iTblOffset - iRowHt;
        int iColWd2[] = { 575, 0 };
        //Horizontal lines
        for(int i=0; i<(alRequiredDocuments.size()+3); i++) {
            iRowOffset += iRowHt;
            canvas.drawLine(0, iRowOffset, 575, iRowOffset, border_paint);
        }
        iColOffset = 0;
        //Vertical lines
        canvas.drawLine(iColOffset, iTblOffset, iColOffset, iRowOffset, border_paint);
        for(int i=0; i<1; i++) {
            iColOffset += iColWd2[i];
            canvas.drawLine(iColOffset, iTblOffset, iColOffset, iRowOffset, border_paint);
        }

        iRowOffset = iTblOffset + 25;
        for(int i=0; i<alRequiredDocuments.size(); i++) {
            canvas.drawText(String.format("%s", alRequiredDocuments.get(i)), 0, iRowOffset, text_paint);
            iRowOffset += iRowHt;
        }

        iRowOffset += iRowHt;
        iRowOffset += iRowHt;

        iRowOffset += 10;
        canvas.drawText(String.format("%s", "सभासदाने वैयक्तिक माहिती (पीआयएफ) भरण्याच्या तारखेला"), 0, iRowOffset, text_paint);
        iRowOffset += iRowHt - 10;
        canvas.drawText(String.format("%s", "ठरलेल्या वेळी ओरिजिनल कागदपत्रांसहित उपस्थित रहावे."), 0, iRowOffset, text_paint);
        iRowOffset += iRowHt;

        iRowOffset += 10;
        canvas.drawText(String.format("%s", "कर्जबाबतचे वेळापत्रक"), 0, iRowOffset, text_paint);
        iRowOffset += 10;

        iTblOffset = iRowOffset;
        iRowOffset = iTblOffset - iRowHt;
        int iColWd3[] = { 410, 165 };
        //Horizontal lines
//bmp 20-Aug-25
//        for(int i=0; i<9; i++) {
        for(int i=0; i<7; i++) {
//bmp 20-Aug-25
            iRowOffset += iRowHt;
            canvas.drawLine(0, iRowOffset, 575, iRowOffset, border_paint);
        }
        iColOffset = 0;
        //Vertical lines
        canvas.drawLine(iColOffset, iTblOffset, iColOffset, iRowOffset, border_paint);
        for(int i=0; i<2; i++) {
            iColOffset += iColWd3[i];
            canvas.drawLine(iColOffset, iTblOffset, iColOffset, iRowOffset, border_paint);
        }

        iColOffset = iColWd3[0] + 5;
        iRowOffset = iTblOffset + 25;
        canvas.drawText(String.format("%s", "वैयक्तिक माहिती(पीआयएफ) भरण्याची तारीख"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", mainActivity.selectedRevisitGroup.PIFDate), iColOffset, iRowOffset, text_paint);
        String strPIFDate = mainActivity.selectedRevisitGroup.PIFDate;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        try {
            Date date1 = sdf1.parse(mainActivity.selectedRevisitGroup.PIFDate);
            strPIFDate = sdf2.format(date1);
        } catch (ParseException e) {
//            throw new RuntimeException(e);
        }
        canvas.drawText(String.format("%s", strPIFDate), iColOffset, iRowOffset, text_paint);
        iRowOffset += iRowHt;
        canvas.drawText(String.format("%s", "वैयक्तिक माहिती(पीआयएफ) भरण्याची वेळ"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", mainActivity.selectedRevisitGroup.PIFTime), iColOffset, iRowOffset, text_paint);
        String strPIFTime = mainActivity.selectedRevisitGroup.PIFTime;
        int hourOfDay = Integer.valueOf(strPIFTime.substring(0, 2));
        String strTime = "सकाळी ";
//bmp 29-Aug-26
//        if(hourOfDay < 17)
        if(hourOfDay < 12)
            strTime = "सकाळी ";
        else if(hourOfDay < 17)
//bmp 29-Aug-26
            strTime = "दुपारी ";
        else if(hourOfDay < 19)
            strTime = "संध्याकाळी ";
        else
            strTime = "रात्री ";
        SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat sdf4 = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        try {
            Date date1 = sdf3.parse(strPIFTime);
            strPIFTime = strTime + sdf4.format(date1);
        } catch (ParseException e) {
//            throw new RuntimeException(e);
        }
        canvas.drawText(String.format("%s", strPIFTime), iColOffset, iRowOffset, text_paint);
        iRowOffset += iRowHt;
        canvas.drawText(String.format("%s", "कम्युनिटी मिटिंगची तारीख"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", mainActivity.selectedRevisitGroup.PIFDate), iColOffset, iRowOffset, text_paint);
        String strComMeetingDate = mainActivity.selectedRevisitGroupMember.get(memberIndex).TargetComMeetingDate;
        if(strComMeetingDate.length() > 11)
            strComMeetingDate = strComMeetingDate.substring(0, 11);
        try {
            Date date1 = sdf1.parse(strComMeetingDate);
            strComMeetingDate = sdf2.format(date1);
        } catch (ParseException e) {
//            throw new RuntimeException(e);
            try {
                Date date1 = sdf2.parse(strComMeetingDate);
                strComMeetingDate = sdf2.format(date1);
            } catch (ParseException ex) {
//                throw new RuntimeException(ex);
                strComMeetingDate = "---";
            }
        }
        canvas.drawText(String.format("%s", strComMeetingDate), iColOffset, iRowOffset, text_paint);
        iRowOffset += iRowHt;
        canvas.drawText(String.format("%s", "कम्युनिटी मिटिंगची वेळ"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", mainActivity.selectedRevisitGroup.PIFTime), iColOffset, iRowOffset, text_paint);
        String strComMeetingTime = mainActivity.selectedRevisitGroupMember.get(memberIndex).TargetComMeetingDate;
        if(strComMeetingTime.length() >= 13) {
            strComMeetingTime = "0" + strComMeetingTime.substring(12, 13) + ":00";
            hourOfDay = 12 + Integer.valueOf(strComMeetingTime.substring(0, 2));
            strTime = "सकाळी ";
            if(hourOfDay < 17)
                strTime = "दुपारी ";
            else if(hourOfDay < 19)
                strTime = "संध्याकाळी ";
            else
                strTime = "रात्री ";
            try {
                Date date1 = sdf3.parse(strComMeetingTime);
                strComMeetingTime = strTime + sdf4.format(date1);
            } catch (ParseException e) {
//            throw new RuntimeException(e);
                strComMeetingTime = "---";
            }
        } else {
            strComMeetingTime = "---";
        }
        canvas.drawText(String.format("%s", strComMeetingTime), iColOffset, iRowOffset, text_paint);
        iRowOffset += iRowHt;
        canvas.drawText(String.format("%s", "गटाच्या परीक्षेची (जीआरटी) तारीख"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", mainActivity.selectedRevisitGroup.PIFDate), iColOffset, iRowOffset, text_paint);
        String strGRTDate = mainActivity.selectedRevisitGroup.GroupTestDate;
        try {
            Date date1 = sdf1.parse(mainActivity.selectedRevisitGroup.GroupTestDate);
            strGRTDate = sdf2.format(date1);
        } catch (ParseException e) {
//            throw new RuntimeException(e);
        }
        canvas.drawText(String.format("%s", strGRTDate), iColOffset, iRowOffset, text_paint);
        iRowOffset += iRowHt;
        canvas.drawText(String.format("%s", "गटाच्या परीक्षेची (जीआरटी) वेळ"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", mainActivity.selectedRevisitGroup.PIFTime), iColOffset, iRowOffset, text_paint);
        String strGRTTime = mainActivity.selectedRevisitGroup.GroupTestTime;
        hourOfDay = Integer.valueOf(strGRTTime.substring(0, 2));
        strTime = "सकाळी ";
//bmp 29-Aug-26
//        if(hourOfDay < 17)
        if(hourOfDay < 12)
            strTime = "सकाळी ";
        else if(hourOfDay < 17)
//bmp 29-Aug-26
            strTime = "दुपारी ";
        else if(hourOfDay < 19)
            strTime = "संध्याकाळी ";
        else
            strTime = "रात्री ";
        try {
            Date date1 = sdf3.parse(strGRTTime);
            strGRTTime = strTime + sdf4.format(date1);
        } catch (ParseException e) {
//            throw new RuntimeException(e);
        }
        canvas.drawText(String.format("%s", strGRTTime), iColOffset, iRowOffset, text_paint);
        iRowOffset += iRowHt;
//bmp 20-Aug-25
//        canvas.drawText(String.format("%s", "लोन ओरिएंटेशनची तारीख"), 0, iRowOffset, text_paint);
//        String strLoanDisbursementDate = mainActivity.selectedRevisitGroup.LoanDisbursementDate;
//        try {
//            Date date1 = sdf1.parse(mainActivity.selectedRevisitGroup.LoanDisbursementDate);
//            strLoanDisbursementDate = sdf2.format(date1);
//        } catch (ParseException e) {
////            throw new RuntimeException(e);
//        }
//        canvas.drawText(String.format("%s", strLoanDisbursementDate), iColOffset, iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//        canvas.drawText(String.format("%s", "लोन ओरिएंटेशनची वेळ"), 0, iRowOffset, text_paint);
//        String strLoanDisbursementTime = mainActivity.selectedRevisitGroup.LoanDisbursementTime;
//        hourOfDay = Integer.valueOf(strLoanDisbursementTime.substring(0, 2));
//        strTime = "सकाळी ";
//        if(hourOfDay < 17)
//            strTime = "दुपारी ";
//        else if(hourOfDay < 19)
//            strTime = "संध्याकाळी ";
//        else
//            strTime = "रात्री ";
//        try {
//            Date date1 = sdf3.parse(strLoanDisbursementTime);
//            strLoanDisbursementTime = strTime + sdf4.format(date1);
//        } catch (ParseException e) {
////            throw new RuntimeException(e);
//        }
//        canvas.drawText(String.format("%s", strLoanDisbursementTime), iColOffset, iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//bmp 20-Aug-25
//////////////////////////
        //PDC Table
        int iCollateralSecurityRequired = 0;
        int iJointAccountPDCRequired = 0;
        int iGuarantorPDCRequired = 0;
        if(iLoanAmount <= 200000) {
            iCollateralSecurityRequired = 0;
            iJointAccountPDCRequired = 0;
            iGuarantorPDCRequired = 0;
        } else if(iLoanAmount <= 350000) {
            iCollateralSecurityRequired = 2;
            iJointAccountPDCRequired = 0;
            iGuarantorPDCRequired = 0;
        } else {
            iCollateralSecurityRequired = 13;
            iJointAccountPDCRequired = 2;
            iGuarantorPDCRequired = 2;
        }

        iRowOffset += 10;
        canvas.drawText(String.format("%s", "लोन ओरिएंटेशनच्या दिवशी जमा करण्याचे PDCs"), 0, iRowOffset, text_paint);
        iRowOffset += 10;

        iTblOffset = iRowOffset;
        iRowOffset = iTblOffset - iRowHt;
        //Horizontal lines
        for(int i=0; i<4; i++) {
            iRowOffset += iRowHt;
            canvas.drawLine(0, iRowOffset, 575, iRowOffset, border_paint);
        }
        iColOffset = 0;
        //Vertical lines
        int iColWd4[] = { 410, 165 };
        canvas.drawLine(iColOffset, iTblOffset, iColOffset, iRowOffset, border_paint);
        for(int i=0; i<2; i++) {
            iColOffset += iColWd4[i];
            canvas.drawLine(iColOffset, iTblOffset, iColOffset, iRowOffset, border_paint);
        }

        iColOffset = iColWd4[0] + 5;
        iRowOffset = iTblOffset + 25;
        canvas.drawText(String.format("%s", "सभासदाने जमा करण्याचे PDCs"), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(iCollateralSecurityRequired)), iColOffset, iRowOffset, text_paint);
        iRowOffset += iRowHt;
        canvas.drawText(String.format("%s", "सहकर्जदाराने जमा करण्याचे PDCs"), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(iJointAccountPDCRequired)), iColOffset, iRowOffset, text_paint);
        iRowOffset += iRowHt;
        canvas.drawText(String.format("%s", "जामीनदाराने जमा करण्याचे PDCs"), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(iGuarantorPDCRequired)), iColOffset, iRowOffset, text_paint);
        iRowOffset += iRowHt;
//////////////////////////
        iRowOffset += 10;
        canvas.drawText(String.format("%s", "लोन ओरिएंटेशन झाल्यावर कर्ज रक्कम सभासदाच्या बँक खात्यात"), 0, iRowOffset, text_paint);
        iRowOffset += iRowHt - 10;
        canvas.drawText(String.format("%s", "अंदाजे 3 ते 5 दिवसांनी जमा होणार."), 0, iRowOffset, text_paint);
        iRowOffset += iRowHt;

        canvas.drawText(String.format("%s", "वैयक्तिक माहिती(पीआयएफ) भरताना सभासदाने कर्ज परतफेडीचा"), 0, iRowOffset, text_paint);
        iRowOffset += iRowHt - 10;
        canvas.drawText(String.format("%s", "कालावधी नक्की करायचा आहे. त्यानंतर गटचाचणी, ओरिएंटेशनच्या"), 0, iRowOffset, text_paint);
        iRowOffset += iRowHt - 10;
        canvas.drawText(String.format("%s", "वेळी सभासदाला कर्ज कालावधी बदलता येणार नाही."), 0, iRowOffset, text_paint);
        iRowOffset += iRowHt;

        //एल. एस. ओ.
        iRowOffset += iRowHt;
        canvas.drawText(String.format("%s", "एल.एस.ओ. ची सही"), 0, iRowOffset, text_paint);
//bmp 07-Jun-25
        canvas.drawText(String.format("%s", "सभासदाची सही"), 350, iRowOffset, text_paint);
//bmp 07-Jun-25
        iRowOffset += iRowHt - 10;
        canvas.drawText(String.format("%s", mainActivity.LoginID), 0, iRowOffset, text_paint);
        iRowOffset += iRowHt - 10;
        canvas.drawLine(0, iRowOffset, 575, iRowOffset, border_paint);
        System.out.println("iRowOffset= " + String.valueOf(iRowOffset));
    }
    public void drawPageOngoingLoanRequirements(PdfDocument.Page page, int memberIndex) {
        Paint text_paint = new Paint();
        text_paint.setColor(Color.BLACK);
        Typeface tf = Typeface.create("Arial", Typeface.NORMAL);
        text_paint.setTypeface(tf);
        text_paint.setTextSize(40);

        Paint border_paint = new Paint();
        border_paint.setColor(Color.BLACK);
        border_paint.setStrokeWidth((float) 3.0);

        Canvas canvas = page.getCanvas();

        canvas.drawColor(Color.WHITE);

        canvas.drawLine(0, 0, 575, 0, border_paint);
//bmp 12-Nov-25
//        text_paint.setTextSize(28);
        text_paint.setTextSize(24);
//bmp 12-Nov-25
        canvas.drawText(String.format("%s", getString(R.string.Receipt_OrgName)), 20, 40, text_paint);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_OrgRegNo)), 120, 80, text_paint);
        canvas.drawLine(5, 95, 570, 95, border_paint);
        text_paint.setTextSize(24);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String strDate = sdf.format(date);// all done

        canvas.drawText(String.format("%s", strDate), 0, 120, text_paint);
//        canvas.drawText(String.format("%s", "पुढील कर्जाबाबतचे वेळापत्रक"), 200, 120, text_paint);
        canvas.drawText(String.format("%s", "जीआरटी चे वेळापत्रक"), 200, 120, text_paint);

        canvas.drawText(String.format("%s", "ब्रँच"), 0, 190, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.BranchName), 80, 190, text_paint);

        canvas.drawText(String.format("%s", "सेंटर ID"), 0, 220, text_paint);
        String strTemp = String.valueOf(mainActivity.selectedRevisitGroupMember.get(memberIndex).CenterID);
        if(strTemp.length() > 10)
            strTemp = strTemp.substring(0, 10);
        canvas.drawText(String.format("%s%s", ":", strTemp), 80, 220, text_paint);
        canvas.drawText(String.format("%s", "ग्रुप ID"), 250, 220, text_paint);
        strTemp = String.valueOf(mainActivity.selectedRevisitGroupMember.get(memberIndex).GroupID);
        if(strTemp.length() > 10)
            strTemp = strTemp.substring(0, 10);
        canvas.drawText(String.format("%s%s", ":", strTemp), 320, 220, text_paint);
//bmp 18-Aug-26
//        canvas.drawText(String.format("%s%s", "ग्रेड:", String.valueOf(mainActivity.selectedRevisitGroup.GradeOfGroup)), 500, 220, text_paint);
//bmp 18-Aug-26
        canvas.drawText(String.format("%s", "सभासद"), 0, 260, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.selectedRevisitGroupMember.get(memberIndex).MemberName), 80, 260, text_paint);

        canvas.drawText(String.format("%s", "मोबाइल"), 0, 290, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.selectedRevisitGroupMember.get(memberIndex).ContactNumber), 80, 290, text_paint);
        canvas.drawText(String.format("%s", "खाते क्र."), 250, 290, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.selectedRevisitGroupMember.get(memberIndex).SavingAccNo), 320, 290, text_paint);

        int iRowOffset = 330;
//
        iRowOffset += 10;
        canvas.drawText(String.format("%s", "गटातील कर्जासाठी अर्ज करणारे सभासद:"), 0, iRowOffset, text_paint);
        iRowOffset += 10;

        int j = 1;
        for(int i=0; i<mainActivity.selectedRevisitGroup.MemberCount; i++) {
//            if(mainActivity.selectedRevisitGroupMember.get(i).VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) {
            if(     (mainActivity.selectedRevisitGroupMember.get(i).VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) ||
                    (mainActivity.selectedRevisitGroupMember.get(i).VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) ||
                    (mainActivity.selectedRevisitGroupMember.get(i).VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED) ) {
                iRowOffset += 30;
                canvas.drawText(String.format("%s%s%s", String.valueOf(j++), "] ", mainActivity.selectedRevisitGroupMember.get(i).MemberName), 0, iRowOffset, text_paint);
            }
        }
        iRowOffset += 40;
        canvas.drawText(String.format("%s", "तुमच्या गटातील वरील सभासदांनी कर्जासाठी अर्ज केला आहे. त्यांच्या"), 0, iRowOffset, text_paint);
        iRowOffset += 30;
        canvas.drawText(String.format("%s", "कर्जाला तुम्ही जामीनदार आहात. म्हणून तुम्ही खालील तारखांना वेळेत"), 0, iRowOffset, text_paint);
        iRowOffset += 30;
        canvas.drawText(String.format("%s", "हजर रहावे."), 0, iRowOffset, text_paint);
        iRowOffset += 10;

        int iRowHt = 40;
        int iTblOffset = iRowOffset;
        iRowOffset = iTblOffset - iRowHt;
        int iColWd3[] = { 410, 165 };
        //Horizontal lines
//bmp 20-Aug-25
//        for(int i=0; i<5; i++) {
        for(int i=0; i<3; i++) {
//bmp 20-Aug-25
            iRowOffset += iRowHt;
            canvas.drawLine(0, iRowOffset, 575, iRowOffset, border_paint);
        }
        int iColOffset = 0;
        //Vertical lines
        canvas.drawLine(iColOffset, iTblOffset, iColOffset, iRowOffset, border_paint);
        for(int i=0; i<2; i++) {
            iColOffset += iColWd3[i];
            canvas.drawLine(iColOffset, iTblOffset, iColOffset, iRowOffset, border_paint);
        }

        iColOffset = iColWd3[0] + 5;
        iRowOffset = iTblOffset + 25;
        canvas.drawText(String.format("%s", "गटाच्या परीक्षेची (जीआरटी) तारीख"), 0, iRowOffset, text_paint);
        String strGRTDate = mainActivity.selectedRevisitGroup.GroupTestDate;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        try {
            Date date1 = sdf1.parse(mainActivity.selectedRevisitGroup.GroupTestDate);
            strGRTDate = sdf2.format(date1);
        } catch (ParseException e) {
//            throw new RuntimeException(e);
        }
        canvas.drawText(String.format("%s", strGRTDate), iColOffset, iRowOffset, text_paint);
        iRowOffset += iRowHt;
        canvas.drawText(String.format("%s", "गटाच्या परीक्षेची (जीआरटी) वेळ"), 0, iRowOffset, text_paint);
        String strGRTTime = mainActivity.selectedRevisitGroup.GroupTestTime;
        int hourOfDay = Integer.valueOf(strGRTTime.substring(0, 2));
        String strTime = "सकाळी ";
//bmp 29-Aug-26
//        if(hourOfDay < 17)
        if(hourOfDay < 12)
            strTime = "सकाळी ";
        else if(hourOfDay < 17)
//bmp 29-Aug-26
            strTime = "दुपारी ";
        else if(hourOfDay < 19)
            strTime = "संध्याकाळी ";
        else
            strTime = "रात्री ";
        SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat sdf4 = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        try {
            Date date1 = sdf3.parse(strGRTTime);
            strGRTTime = strTime + sdf4.format(date1);
        } catch (ParseException e) {
//            throw new RuntimeException(e);
        }
        canvas.drawText(String.format("%s", strGRTTime), iColOffset, iRowOffset, text_paint);
        iRowOffset += iRowHt;
//bmp 20-Aug-25
//        canvas.drawText(String.format("%s", "लोन ओरिएंटेशनची तारीख"), 0, iRowOffset, text_paint);
//        String strLoanDisbursementDate = mainActivity.selectedRevisitGroup.LoanDisbursementDate;
//        try {
//            Date date1 = sdf1.parse(mainActivity.selectedRevisitGroup.LoanDisbursementDate);
//            strLoanDisbursementDate = sdf2.format(date1);
//        } catch (ParseException e) {
////            throw new RuntimeException(e);
//        }
//        canvas.drawText(String.format("%s", strLoanDisbursementDate), iColOffset, iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//        canvas.drawText(String.format("%s", "लोन ओरिएंटेशनची वेळ"), 0, iRowOffset, text_paint);
//        String strLoanDisbursementTime = mainActivity.selectedRevisitGroup.LoanDisbursementTime;
//        hourOfDay = Integer.valueOf(strLoanDisbursementTime.substring(0, 2));
//        strTime = "सकाळी ";
//        if(hourOfDay < 17)
//            strTime = "दुपारी ";
//        else if(hourOfDay < 19)
//            strTime = "संध्याकाळी ";
//        else
//            strTime = "रात्री ";
//        try {
//            Date date1 = sdf3.parse(strLoanDisbursementTime);
//            strLoanDisbursementTime = strTime + sdf4.format(date1);
//        } catch (ParseException e) {
////            throw new RuntimeException(e);
//        }
//        canvas.drawText(String.format("%s", strLoanDisbursementTime), iColOffset, iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//bmp 20-Aug-25

        iRowOffset += 10;
        canvas.drawText(String.format("%s", "सभासदाने गटाच्या परीक्षेच्या (जीआरटी) तारखेला ठरलेल्या वेळी"), 0, iRowOffset, text_paint);
        iRowOffset += iRowHt - 10;
//        canvas.drawText(String.format("%s", "ओरिजिनल ओळखपत्र - आधारकार्ड सहित उपस्थित रहावे."), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s", "उपस्थित रहावे."), 0, iRowOffset, text_paint);
        iRowOffset += iRowHt;

        //एल. एस. ओ.
        iRowOffset += 50;
        canvas.drawText(String.format("%s", "एल.एस.ओ. ची सही"), 0, iRowOffset, text_paint);
//bmp 07-Jun-25
        canvas.drawText(String.format("%s", "सभासदाची सही"), 350, iRowOffset, text_paint);
//bmp 07-Jun-25
        iRowOffset += iRowHt;
        canvas.drawText(String.format("%s", mainActivity.LoginID), 0, iRowOffset, text_paint);
        iRowOffset += iRowHt;
        canvas.drawLine(0, iRowOffset, 575, iRowOffset, border_paint);
    }

    @Override
    public void onClick(View v) {
        String StrPDFReportFileName = "";

        switch (v.getId()) {
            case R.id.buttonRequirementPreview1:
                ValidateScreenNewGroupForm();
                if(NewGroupFormProblemsCheck.size() > 0) {
                    String strMsg = "";
                    for(int i=0; i<NewGroupFormProblemsCheck.size(); i++) {
                        strMsg = strMsg + NewGroupFormProblemsCheck.get(i) + "\n";
                    }
                    CreateAlertDialog(true, true);
                    dlgState = ufin_GROUPFORM_DIALOG_STATE_VALIDATE;
                    dlgAlert.setMessage(strMsg);
                    //                        dlgAlert.create().show();
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    break;
                }
                StrPDFReportFileName = generatePIFRequirementsPDF(0);
                if(StrPDFReportFileName.isEmpty()) {
                    break;
                }
                Button bt = (Button) rootView.findViewById(R.id.buttonRequirementPrint1);
                bt.setEnabled(true);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                File pdfFile = new File(StrPDFReportFileName);
                Uri pdfuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", pdfFile);
                intent.setDataAndType(pdfuri, "application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
                break;
            case R.id.buttonRequirementPreview2:
                ValidateScreenNewGroupForm();
                if(NewGroupFormProblemsCheck.size() > 0) {
                    String strMsg = "";
                    for(int i=0; i<NewGroupFormProblemsCheck.size(); i++) {
                        strMsg = strMsg + NewGroupFormProblemsCheck.get(i) + "\n";
                    }
                    CreateAlertDialog(true, true);
                    dlgState = ufin_GROUPFORM_DIALOG_STATE_VALIDATE;
                    dlgAlert.setMessage(strMsg);
                    //                        dlgAlert.create().show();
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    break;
                }
                StrPDFReportFileName = generatePIFRequirementsPDF(1);
                if(StrPDFReportFileName.isEmpty()) {
                    break;
                }
                bt = (Button) rootView.findViewById(R.id.buttonRequirementPrint2);
                bt.setEnabled(true);
                intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                pdfFile = new File(StrPDFReportFileName);
                pdfuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", pdfFile);
                intent.setDataAndType(pdfuri, "application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
                break;
            case R.id.buttonRequirementPreview3:
                ValidateScreenNewGroupForm();
                if(NewGroupFormProblemsCheck.size() > 0) {
                    String strMsg = "";
                    for(int i=0; i<NewGroupFormProblemsCheck.size(); i++) {
                        strMsg = strMsg + NewGroupFormProblemsCheck.get(i) + "\n";
                    }
                    CreateAlertDialog(true, true);
                    dlgState = ufin_GROUPFORM_DIALOG_STATE_VALIDATE;
                    dlgAlert.setMessage(strMsg);
                    //                        dlgAlert.create().show();
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    break;
                }
                StrPDFReportFileName = generatePIFRequirementsPDF(2);
                if(StrPDFReportFileName.isEmpty()) {
                    break;
                }
                bt = (Button) rootView.findViewById(R.id.buttonRequirementPrint3);
                bt.setEnabled(true);
                intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                pdfFile = new File(StrPDFReportFileName);
                pdfuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", pdfFile);
                intent.setDataAndType(pdfuri, "application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
                break;
            case R.id.buttonRequirementPreview4:
                ValidateScreenNewGroupForm();
                if(NewGroupFormProblemsCheck.size() > 0) {
                    String strMsg = "";
                    for(int i=0; i<NewGroupFormProblemsCheck.size(); i++) {
                        strMsg = strMsg + NewGroupFormProblemsCheck.get(i) + "\n";
                    }
                    CreateAlertDialog(true, true);
                    dlgState = ufin_GROUPFORM_DIALOG_STATE_VALIDATE;
                    dlgAlert.setMessage(strMsg);
                    //                        dlgAlert.create().show();
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    break;
                }
                StrPDFReportFileName = generatePIFRequirementsPDF(3);
                if(StrPDFReportFileName.isEmpty()) {
                    break;
                }
                bt = (Button) rootView.findViewById(R.id.buttonRequirementPrint4);
                bt.setEnabled(true);
                intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                pdfFile = new File(StrPDFReportFileName);
                pdfuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", pdfFile);
                intent.setDataAndType(pdfuri, "application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
                break;
            case R.id.buttonRequirementPreview5:
                ValidateScreenNewGroupForm();
                if(NewGroupFormProblemsCheck.size() > 0) {
                    String strMsg = "";
                    for(int i=0; i<NewGroupFormProblemsCheck.size(); i++) {
                        strMsg = strMsg + NewGroupFormProblemsCheck.get(i) + "\n";
                    }
                    CreateAlertDialog(true, true);
                    dlgState = ufin_GROUPFORM_DIALOG_STATE_VALIDATE;
                    dlgAlert.setMessage(strMsg);
                    //                        dlgAlert.create().show();
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    break;
                }
                StrPDFReportFileName = generatePIFRequirementsPDF(4);
                if(StrPDFReportFileName.isEmpty()) {
                    break;
                }
                bt = (Button) rootView.findViewById(R.id.buttonRequirementPrint5);
                bt.setEnabled(true);
                intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                pdfFile = new File(StrPDFReportFileName);
                pdfuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", pdfFile);
                intent.setDataAndType(pdfuri, "application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
                break;
            case R.id.buttonRequirementPrint1:
                if(flagDisableNextPrint)
                    break;
                if(!ConfirmPrint) {
                    CreateAlertDialog(true, true);
                    dlgState = ufin_GROUPFORM_DIALOG_STATE_CONFIRM_PRINT_BUTTON1;
                    dlgAlert.setMessage(MSG_ufin_GROUPFORM_CONFIRM_PRINT_BUTTON);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    textView.setTextColor(Color.RED);
                    break;
                }
                ConfirmPrint = false;
                mainActivity.disconnectPrinter();
                boolean ReadyToPrint = mainActivity.checkPrinter();
                if(!ReadyToPrint) {
                    CreateAlertDialog(false, true);
                    dlgState = ufin_GROUPFORM_DIALOG_STATE_NO_PRINTER;
                    dlgAlert.setMessage(MSG_ufin_GROUPFORM_NO_PRINTER);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    textView.setTextColor(Color.RED);
                    break;
                }
                String StrPDFRequirementFileName
                        =   mainActivity.baseDir + "/data/pif/" +
                            String.valueOf(mainActivity.selectedRevisitGroupMember.get(0).MemberID) + "/" +
                            String.valueOf(mainActivity.selectedRevisitGroupMember.get(0).MemberID) + ".pdf";
                mainActivity.printPDF(StrPDFRequirementFileName);
                flagRequirementPrint1 = true;
                DisableNextPrintTillUserConfirm();
                break;
            case R.id.buttonRequirementPrint2:
                if(flagDisableNextPrint)
                    break;
                if(!ConfirmPrint) {
                    CreateAlertDialog(true, true);
                    dlgState = ufin_GROUPFORM_DIALOG_STATE_CONFIRM_PRINT_BUTTON2;
                    dlgAlert.setMessage(MSG_ufin_GROUPFORM_CONFIRM_PRINT_BUTTON);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    textView.setTextColor(Color.RED);
                    break;
                }
                ConfirmPrint = false;
                mainActivity.disconnectPrinter();
                ReadyToPrint = mainActivity.checkPrinter();
                if(!ReadyToPrint) {
                    CreateAlertDialog(false, true);
                    dlgState = ufin_GROUPFORM_DIALOG_STATE_NO_PRINTER;
                    dlgAlert.setMessage(MSG_ufin_GROUPFORM_NO_PRINTER);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    textView.setTextColor(Color.RED);
                    break;
                }
                StrPDFRequirementFileName
                        =   mainActivity.baseDir + "/data/pif/" +
                        String.valueOf(mainActivity.selectedRevisitGroupMember.get(1).MemberID) + "/" +
                        String.valueOf(mainActivity.selectedRevisitGroupMember.get(1).MemberID) + ".pdf";
                mainActivity.printPDF(StrPDFRequirementFileName);
                flagRequirementPrint2 = true;
                DisableNextPrintTillUserConfirm();
                break;
            case R.id.buttonRequirementPrint3:
                if(flagDisableNextPrint)
                    break;
                if(!ConfirmPrint) {
                    CreateAlertDialog(true, true);
                    dlgState = ufin_GROUPFORM_DIALOG_STATE_CONFIRM_PRINT_BUTTON3;
                    dlgAlert.setMessage(MSG_ufin_GROUPFORM_CONFIRM_PRINT_BUTTON);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    textView.setTextColor(Color.RED);
                    break;
                }
                ConfirmPrint = false;
                mainActivity.disconnectPrinter();
                ReadyToPrint = mainActivity.checkPrinter();
                if(!ReadyToPrint) {
                    CreateAlertDialog(false, true);
                    dlgState = ufin_GROUPFORM_DIALOG_STATE_NO_PRINTER;
                    dlgAlert.setMessage(MSG_ufin_GROUPFORM_NO_PRINTER);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    textView.setTextColor(Color.RED);
                    break;
                }
                StrPDFRequirementFileName
                        =   mainActivity.baseDir + "/data/pif/" +
                        String.valueOf(mainActivity.selectedRevisitGroupMember.get(2).MemberID) + "/" +
                        String.valueOf(mainActivity.selectedRevisitGroupMember.get(2).MemberID) + ".pdf";
                mainActivity.printPDF(StrPDFRequirementFileName);
                flagRequirementPrint3 = true;
                DisableNextPrintTillUserConfirm();
                break;
            case R.id.buttonRequirementPrint4:
                if(flagDisableNextPrint)
                    break;
                if(!ConfirmPrint) {
                    CreateAlertDialog(true, true);
                    dlgState = ufin_GROUPFORM_DIALOG_STATE_CONFIRM_PRINT_BUTTON4;
                    dlgAlert.setMessage(MSG_ufin_GROUPFORM_CONFIRM_PRINT_BUTTON);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    textView.setTextColor(Color.RED);
                    break;
                }
                ConfirmPrint = false;
                mainActivity.disconnectPrinter();
                ReadyToPrint = mainActivity.checkPrinter();
                if(!ReadyToPrint) {
                    CreateAlertDialog(false, true);
                    dlgState = ufin_GROUPFORM_DIALOG_STATE_NO_PRINTER;
                    dlgAlert.setMessage(MSG_ufin_GROUPFORM_NO_PRINTER);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    textView.setTextColor(Color.RED);
                    break;
                }
                StrPDFRequirementFileName
                        =   mainActivity.baseDir + "/data/pif/" +
                        String.valueOf(mainActivity.selectedRevisitGroupMember.get(3).MemberID) + "/" +
                        String.valueOf(mainActivity.selectedRevisitGroupMember.get(3).MemberID) + ".pdf";
                mainActivity.printPDF(StrPDFRequirementFileName);
                flagRequirementPrint4 = true;
                DisableNextPrintTillUserConfirm();
                break;
            case R.id.buttonRequirementPrint5:
                if(flagDisableNextPrint)
                    break;
                if(!ConfirmPrint) {
                    CreateAlertDialog(true, true);
                    dlgState = ufin_GROUPFORM_DIALOG_STATE_CONFIRM_PRINT_BUTTON5;
                    dlgAlert.setMessage(MSG_ufin_GROUPFORM_CONFIRM_PRINT_BUTTON);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    textView.setTextColor(Color.RED);
                    break;
                }
                ConfirmPrint = false;
                mainActivity.disconnectPrinter();
                ReadyToPrint = mainActivity.checkPrinter();
                if(!ReadyToPrint) {
                    CreateAlertDialog(false, true);
                    dlgState = ufin_GROUPFORM_DIALOG_STATE_NO_PRINTER;
                    dlgAlert.setMessage(MSG_ufin_GROUPFORM_NO_PRINTER);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    textView.setTextColor(Color.RED);
                    break;
                }
                StrPDFRequirementFileName
                        =   mainActivity.baseDir + "/data/pif/" +
                        String.valueOf(mainActivity.selectedRevisitGroupMember.get(4).MemberID) + "/" +
                        String.valueOf(mainActivity.selectedRevisitGroupMember.get(4).MemberID) + ".pdf";
                mainActivity.printPDF(StrPDFRequirementFileName);
                flagRequirementPrint5 = true;
                DisableNextPrintTillUserConfirm();
                break;
            case R.id.buttonSchedule:
                CustomViewPager viewPager = (CustomViewPager) mainActivity.findViewById(R.id.viewpagerGF);
                if(viewPager != null)
                    viewPager.requestFocus();
                mainActivity.iPrevViewId = R.id.viewpagerGF;
                mainActivity.iCurrentScreen = 1;
                viewPager.setCurrentItem(mainActivity.iCurrentScreen);
                break;
            case R.id.buttonCloseGroupform:
                mainActivity.InitDesktop();
                break;
            case R.id.buttonSaveGroupform:
                ValidateScreenNewGroupForm();
                if(NewGroupFormProblemsCheck.size() == 0) {
                    CreateAlertDialog(true, true);
                    dlgState = ufin_GROUPFORM_DIALOG_STATE_SUBMIT;
                    dlgAlert.setMessage(MSG_ufin_GROUPFORM_CONFIRM_SUBMIT);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                } else {
                    String strMsg = "";
                    for(int i=0; i<NewGroupFormProblemsCheck.size(); i++) {
                        strMsg = strMsg + NewGroupFormProblemsCheck.get(i) + "\n";
                    }
                    CreateAlertDialog(true, true);
                    dlgState = ufin_GROUPFORM_DIALOG_STATE_VALIDATE;
                    dlgAlert.setMessage(strMsg);
                    //                        dlgAlert.create().show();
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                }
                break;
            case R.id.imageButtonGroupIDPhoto1:
                IDRequest = ID_REQUEST_PHOTO_Member1;
                String strFilepath = mainActivity.baseDir + "/data/";
                String strFilename = "member1_photo";
                File outFile = new File(strFilepath + strFilename + ".jpg");
//bmp 19-Aug-26
                if(outFile.exists()) {
                    outFile.delete();
                    outFile = new File(strFilepath + strFilename + ".jpg");
                }
//bmp 19-Aug-26
               /* Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                Uri outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", outFile);
                cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outuri);*/
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);

                //RK
                Intent cameraIntent = new Intent();
                cameraIntent.setClassName(
                        "com.example.cameraapp",
                        "com.example.cameraapp.DocumentScannerActivity"
                );
                startActivityIntent.launch(cameraIntent);
                break;
            case R.id.imageButtonGroupIDPhoto2:
                IDRequest = ID_REQUEST_PHOTO_Member2;
                strFilepath = mainActivity.baseDir + "/data/";
                strFilename = "member2_photo";
                outFile = new File(strFilepath + strFilename + ".jpg");
//bmp 19-Aug-26
                if(outFile.exists()) {
                    outFile.delete();
                    outFile = new File(strFilepath + strFilename + ".jpg");
                }
//bmp 19-Aug-26
                /*cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", outFile);
                cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outuri);
                 startActivityIntent.launch(cameraIntent);
                */
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);

                //RK
                 cameraIntent = new Intent();
                cameraIntent.setClassName(
                        "com.example.cameraapp",
                        "com.example.cameraapp.DocumentScannerActivity"
                );
                startActivityIntent.launch(cameraIntent);
                break;
            case R.id.imageButtonGroupIDPhoto3:
                IDRequest = ID_REQUEST_PHOTO_Member3;
                strFilepath = mainActivity.baseDir + "/data/";
                strFilename = "member3_photo";
                outFile = new File(strFilepath + strFilename + ".jpg");
//bmp 19-Aug-26
                if(outFile.exists()) {
                    outFile.delete();
                    outFile = new File(strFilepath + strFilename + ".jpg");
                }
//bmp 19-Aug-26
                /*cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", outFile);
                cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outuri);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                startActivityIntent.launch(cameraIntent);*/


                //RK
                cameraIntent = new Intent();
                cameraIntent.setClassName(
                        "com.example.cameraapp",
                        "com.example.cameraapp.DocumentScannerActivity"
                );

                startActivityIntent.launch(cameraIntent);

                break;
            case R.id.imageButtonGroupIDPhoto4:
                IDRequest = ID_REQUEST_PHOTO_Member4;
                strFilepath = mainActivity.baseDir + "/data/";
                strFilename = "member4_photo";
                outFile = new File(strFilepath + strFilename + ".jpg");
//bmp 19-Aug-26
                if(outFile.exists()) {
                    outFile.delete();
                    outFile = new File(strFilepath + strFilename + ".jpg");
                }
//bmp 19-Aug-26
                /*cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", outFile);
                cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outuri);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                startActivityIntent.launch(cameraIntent);*/

                //RK
                cameraIntent = new Intent();
                cameraIntent.setClassName(
                        "com.example.cameraapp",
                        "com.example.cameraapp.DocumentScannerActivity"
                );

                startActivityIntent.launch(cameraIntent);

                break;
            case R.id.imageButtonGroupIDPhoto5:
                IDRequest = ID_REQUEST_PHOTO_Member5;
                strFilepath = mainActivity.baseDir + "/data/";
                strFilename = "member5_photo";
                outFile = new File(strFilepath + strFilename + ".jpg");
//bmp 19-Aug-26
                if(outFile.exists()) {
                    outFile.delete();
                    outFile = new File(strFilepath + strFilename + ".jpg");
                }
//bmp 19-Aug-26
                /*cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", outFile);
                cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outuri);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                startActivityIntent.launch(cameraIntent);*/

                //RK
                cameraIntent = new Intent();
                cameraIntent.setClassName(
                        "com.example.cameraapp",
                        "com.example.cameraapp.DocumentScannerActivity"
                );

                startActivityIntent.launch(cameraIntent);

                break;
        }
    }
    public void DisableNextPrintTillUserConfirm() {
        flagDisableNextPrint = true;
        CreateAlertDialog(true, false);
        dlgState = ufin_GROUPFORM_DIALOG_STATE_RESUME_PRINTING;
        dlgAlert.setMessage(MSG_ufin_GROUPFORM_RESUME_PRINTING);
        AlertDialog dialog = dlgAlert.create();
        dialog.show();
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(30);
        textView.setTextColor(Color.RED);

        textView.setPadding(0,500,0,0);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
        lp2.copyFrom(dialog.getWindow().getAttributes());
        lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp2.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp2.gravity = Gravity.CENTER_VERTICAL;
        dialog.getWindow().setAttributes(lp2);
    }
    public void SQLite_getBusinessMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.BusinessMaster.COLUMN_NAME_BusinessID,
                ufinDbContract.BusinessMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.BusinessMaster.COLUMN_NAME_SequenceID,
                ufinDbContract.BusinessMaster.COLUMN_NAME_ReferenceMemberDocumentID_1,
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
        ReferenceMemberDocumentID_1.clear();
        ReferenceMemberDocumentID_1.add(0);

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
                            ReferenceMemberDocumentID_1.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.BusinessMaster.COLUMN_NAME_ReferenceMemberDocumentID_1))));
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
    public void SQLite_getMaritalStatusMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.MaritalStatusMaster.COLUMN_NAME_MaritalStatusID,
                ufinDbContract.MaritalStatusMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.MaritalStatusMaster.COLUMN_NAME_SequenceID,
        };
        String Active = "1";

        String selection1 = ufinDbContract.MaritalStatusMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.MaritalStatusMaster.COLUMN_NAME_SequenceID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.MaritalStatusMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        MaritalStatusIDMaster.clear();
        MaritalStatusMarathiDescriptionMaster.clear();
        MaritalStatusIDMaster.add(0);
        MaritalStatusMarathiDescriptionMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            MaritalStatusIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.MaritalStatusMaster.COLUMN_NAME_MaritalStatusID))));
                            String str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.MaritalStatusMaster.COLUMN_NAME_MarathiDescription));
                            if (str1 == null) {
                                str1 = "?";
                            }
                            MaritalStatusMarathiDescriptionMaster.add(str1);
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
        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
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
                                RelationIDMainMember = RelationIDMaster.get(RelationIDMaster.size() - 1);
                            }
                            if(str2.equalsIgnoreCase("Spouse")) {
                                RelationIDSpouse = RelationIDMaster.get(RelationIDMaster.size() - 1);
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
    public void RefreshGroupForm() {
//        TextView tvGrade = (TextView) rootView.findViewById(R.id.textViewGroupGrade);
//        tvGrade.setText("ग्रेड:" + mainActivity.selectedRevisitGroup.GradeOfGroup);
        TextView tvCount = (TextView) rootView.findViewById(R.id.editTextGroupMemberCount);
        tvCount.setText(String.valueOf(mainActivity.selectedRevisitGroup.MemberCount));

        LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.linearLayoutGroupMember4);
        ll.setEnabled(true);
        ll.setVisibility(View.VISIBLE);
        ll = (LinearLayout) rootView.findViewById(R.id.linearLayoutGroupMember5);
        ll.setEnabled(true);
        ll.setVisibility(View.VISIBLE);
        if(mainActivity.selectedRevisitGroup.MemberCount == 3) {
            ll = (LinearLayout) rootView.findViewById(R.id.linearLayoutGroupMember4);
            ll.setEnabled(false);
            ll.setVisibility(View.INVISIBLE);
            ll = (LinearLayout) rootView.findViewById(R.id.linearLayoutGroupMember5);
            ll.setEnabled(false);
            ll.setVisibility(View.INVISIBLE);
        } else if(mainActivity.selectedRevisitGroup.MemberCount == 4) {
            ll = (LinearLayout) rootView.findViewById(R.id.linearLayoutGroupMember5);
            ll.setEnabled(false);
            ll.setVisibility(View.INVISIBLE);
        }

        mainActivity.GroupMemberNameMaster.clear();
        mainActivity.GroupMemberNameMaster.add("?");
        for(int i=0; i<mainActivity.selectedRevisitGroup.MemberCount; i++) {
            mainActivity.GroupMemberNameMaster.add(mainActivity.selectedRevisitGroupMember.get(i).MemberName);
        }
        ArrayAdapter<String> adapterGroupName = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, mainActivity.GroupMemberNameMaster);
        adapterGroupName.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerGroupName);
        sp.setAdapter(adapterGroupName);
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);
        sp.setSelection(0);
        for(int i=0; i<mainActivity.selectedRevisitGroup.MemberCount; i++) {
            if(mainActivity.selectedRevisitGroup.CollectionMemberID == mainActivity.selectedRevisitGroupMember.get(i).MemberID) {
                sp.setSelection(i+1);
                break;
            }
        }
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                    mainActivity.selectedRevisitGroup.CollectionMemberID = 0;
                else
                    mainActivity.selectedRevisitGroup.CollectionMemberID = mainActivity.selectedRevisitGroupMember.get(position - 1).MemberID;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroup2a);
        if(mainActivity.selectedRevisitGroup != null) {
            switch (mainActivity.selectedRevisitGroup.flagGRTQ2a) {
                case -1:
                    rg.clearCheck();
                    break;
                case 0:
                    rg.check(R.id.radioButton2aNo);
                    break;
                case 1:
                    rg.check(R.id.radioButton2aYes);
                    break;
            }
        }
        TextView tv = (TextView) rootView.findViewById(R.id.textViewGroupIDName1);
        tv.setText(mainActivity.selectedRevisitGroupMember.get(0).MemberName);
        tv = (TextView) rootView.findViewById(R.id.textViewGroupIDName2);
        tv.setText(mainActivity.selectedRevisitGroupMember.get(1).MemberName);
        tv = (TextView) rootView.findViewById(R.id.textViewGroupIDName3);
        tv.setText(mainActivity.selectedRevisitGroupMember.get(2).MemberName);
        try {
            tv = (TextView) rootView.findViewById(R.id.textViewGroupIDName4);
            tv.setText(mainActivity.selectedRevisitGroupMember.get(3).MemberName);
            tv = (TextView) rootView.findViewById(R.id.textViewGroupIDName5);
            tv.setText(mainActivity.selectedRevisitGroupMember.get(4).MemberName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation1);
        sp.setSelection(0);
        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxServiceNote1);
        cb.setChecked(false);
        for (int i = 0; i < BusinessIDMaster.size(); i++) {
            if(mainActivity.selectedRevisitGroupMember.get(0).OccupationID == BusinessIDMaster.get(i)) {
                sp.setSelection(i);
                if(ReferenceMemberDocumentID_1.get(i) == 32) {
                    cb.setChecked(true);
                }
                break;
            }
        }

        sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus1);
        sp.setSelection(0);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxOtherRelativeNote1);
        cb.setChecked(false);
        for (int i = 0; i < MaritalStatusIDMaster.size(); i++) {
            if(mainActivity.selectedRevisitGroupMember.get(0).MaritalStatusID == MaritalStatusIDMaster.get(i)) {
                sp.setSelection(i);
                if ((mainActivity.selectedRevisitGroupMember.get(0).MaritalStatusID != 1) &&
                        (mainActivity.selectedRevisitGroupMember.get(0).MaritalStatusID != 7)) {
                    cb.setChecked(true);
                }
                break;
            }
        }

        EditText et = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName1);
        et.setText(mainActivity.selectedRevisitGroupMember.get(0).GuarantorName);
//        et.setEnabled(mainActivity.selectedRevisitGroupMember.get(0).GuarantorName.isEmpty());
        Spinner spRel = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation1);
        spRel.setSelection(0);
        for (int i = 0; i < RelationIDMaster.size(); i++) {
            if (mainActivity.selectedRevisitGroupMember.get(0).GuarantorRelationID == RelationIDMaster.get(i)) {
                spRel.setSelection(i);
//                spRel.setEnabled(false);
                break;
            }
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation2);
        sp.setSelection(0);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxServiceNote2);
        cb.setChecked(false);
        for (int i = 0; i < BusinessIDMaster.size(); i++) {
            if(mainActivity.selectedRevisitGroupMember.get(1).OccupationID == BusinessIDMaster.get(i)) {
                sp.setSelection(i);
                if(ReferenceMemberDocumentID_1.get(i) == 32) {
                    cb.setChecked(true);
                }
                break;
            }
        }

        sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus2);
        sp.setSelection(0);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxOtherRelativeNote2);
        cb.setChecked(false);
        for (int i = 0; i < MaritalStatusIDMaster.size(); i++) {
            if(mainActivity.selectedRevisitGroupMember.get(1).MaritalStatusID == MaritalStatusIDMaster.get(i)) {
                sp.setSelection(i);
                if ((mainActivity.selectedRevisitGroupMember.get(1).MaritalStatusID != 1) &&
                        (mainActivity.selectedRevisitGroupMember.get(1).MaritalStatusID != 7)) {
                    cb.setChecked(true);
                }
                break;
            }
        }

        et = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName2);
        et.setText(mainActivity.selectedRevisitGroupMember.get(1).GuarantorName);
//        et.setEnabled(mainActivity.selectedRevisitGroupMember.get(1).GuarantorName.isEmpty());
        spRel = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation2);
        spRel.setSelection(0);
        for (int i = 0; i < RelationIDMaster.size(); i++) {
            if (mainActivity.selectedRevisitGroupMember.get(1).GuarantorRelationID == RelationIDMaster.get(i)) {
                spRel.setSelection(i);
//                spRel.setEnabled(false);
                break;
            }
        }

        sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation3);
        sp.setSelection(0);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxServiceNote3);
        cb.setChecked(false);
        for (int i = 0; i < BusinessIDMaster.size(); i++) {
            if(mainActivity.selectedRevisitGroupMember.get(2).OccupationID == BusinessIDMaster.get(i)) {
                sp.setSelection(i);
                if(ReferenceMemberDocumentID_1.get(i) == 32) {
                    cb.setChecked(true);
                }
                break;
            }
        }

        sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus3);
        sp.setSelection(0);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxOtherRelativeNote3);
        cb.setChecked(false);
        for (int i = 0; i < MaritalStatusIDMaster.size(); i++) {
            if(mainActivity.selectedRevisitGroupMember.get(2).MaritalStatusID == MaritalStatusIDMaster.get(i)) {
                sp.setSelection(i);
                if ((mainActivity.selectedRevisitGroupMember.get(2).MaritalStatusID != 1) &&
                        (mainActivity.selectedRevisitGroupMember.get(2).MaritalStatusID != 7)) {
                    cb.setChecked(true);
                }
                break;
            }
        }

        et = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName3);
        et.setText(mainActivity.selectedRevisitGroupMember.get(2).GuarantorName);
//        et.setEnabled(mainActivity.selectedRevisitGroupMember.get(2).GuarantorName.isEmpty());
        spRel = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation3);
        spRel.setSelection(0);
        for (int i = 0; i < RelationIDMaster.size(); i++) {
            if (mainActivity.selectedRevisitGroupMember.get(2).GuarantorRelationID == RelationIDMaster.get(i)) {
                spRel.setSelection(i);
//                spRel.setEnabled(false);
                break;
            }
        }

        sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation4);
        sp.setSelection(0);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxServiceNote4);
        cb.setChecked(false);
        for (int i = 0; i < BusinessIDMaster.size(); i++) {
            try {
                if (mainActivity.selectedRevisitGroupMember.get(3).OccupationID == BusinessIDMaster.get(i)) {
                    sp.setSelection(i);
                    if (ReferenceMemberDocumentID_1.get(i) == 32) {
                        cb.setChecked(true);
                    }
                    break;
                }
            } catch (Exception e) {

            }
        }

        sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus4);
        sp.setSelection(0);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxOtherRelativeNote4);
        cb.setChecked(false);
        for (int i = 0; i < MaritalStatusIDMaster.size(); i++) {
            try {
                if (mainActivity.selectedRevisitGroupMember.get(3).MaritalStatusID == MaritalStatusIDMaster.get(i)) {
                    sp.setSelection(i);
                    if ((mainActivity.selectedRevisitGroupMember.get(3).MaritalStatusID != 1) &&
                            (mainActivity.selectedRevisitGroupMember.get(3).MaritalStatusID != 7)) {
                        cb.setChecked(true);
                    }
                    break;
                }
            } catch (Exception e) {

            }
        }

        try {
        et = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName4);
        et.setText(mainActivity.selectedRevisitGroupMember.get(3).GuarantorName);
//        et.setEnabled(mainActivity.selectedRevisitGroupMember.get(3).GuarantorName.isEmpty());
        spRel = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation4);
        spRel.setSelection(0);
        for (int i = 0; i < RelationIDMaster.size(); i++) {
            try {
                if (mainActivity.selectedRevisitGroupMember.get(3).GuarantorRelationID == RelationIDMaster.get(i)) {
                    spRel.setSelection(i);
//                spRel.setEnabled(false);
                    break;
                }
            } catch (Exception e) {

            }
        }
        } catch (Exception e) {

        }

        sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation5);
        sp.setSelection(0);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxServiceNote5);
        cb.setChecked(false);
        for (int i = 0; i < BusinessIDMaster.size(); i++) {
            try {
                if (mainActivity.selectedRevisitGroupMember.get(4).OccupationID == BusinessIDMaster.get(i)) {
                    sp.setSelection(i);
                    if (ReferenceMemberDocumentID_1.get(i) == 32) {
                        cb.setChecked(true);
                    }
                    break;
                }
            } catch (Exception e) {

            }
        }

        sp = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus5);
        sp.setSelection(0);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxOtherRelativeNote5);
        cb.setChecked(false);
        for (int i = 0; i < MaritalStatusIDMaster.size(); i++) {
            try {
                if (mainActivity.selectedRevisitGroupMember.get(4).MaritalStatusID == MaritalStatusIDMaster.get(i)) {
                    sp.setSelection(i);
                    if ((mainActivity.selectedRevisitGroupMember.get(4).MaritalStatusID != 1) &&
                            (mainActivity.selectedRevisitGroupMember.get(4).MaritalStatusID != 7)) {
                        cb.setChecked(true);
                    }
                    break;
                }
            } catch (Exception e) {

            }
        }
        try {
            et = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName5);
            et.setText(mainActivity.selectedRevisitGroupMember.get(4).GuarantorName);
//        et.setEnabled(mainActivity.selectedRevisitGroupMember.get(4).GuarantorName.isEmpty());
            spRel = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation5);
            spRel.setSelection(0);
            for (int i = 0; i < RelationIDMaster.size(); i++) {
                if (mainActivity.selectedRevisitGroupMember.get(4).GuarantorRelationID == RelationIDMaster.get(i)) {
                    spRel.setSelection(i);
//                spRel.setEnabled(false);
                    break;
                }
            }
        } catch (Exception e) {

        }
//bmp 19-Jul-22
//        if(mainActivity.selectedRevisitGroupMember.get(0).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) {
        if(     (mainActivity.selectedRevisitGroupMember.get(0).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) &&
                (mainActivity.selectedRevisitGroupMember.get(0).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) &&
                (mainActivity.selectedRevisitGroupMember.get(0).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED) ) {
            Spinner sp0 = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation1);
            sp0.setEnabled(false);
            CheckBox cb0 = (CheckBox) rootView.findViewById(R.id.checkBoxServiceNote1);
            cb0.setChecked(false);
            sp0 = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus1);
            sp0.setEnabled(false);
            cb0 = (CheckBox) rootView.findViewById(R.id.checkBoxOtherRelativeNote1);
            cb0.setChecked(false);
            EditText et0 = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName1);
            et0.setEnabled(false);
            sp0 = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation1);
            sp0.setEnabled(false);
        } else {
            Spinner sp0 = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation1);
            sp0.setEnabled(true);
            sp0 = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus1);
            sp0.setEnabled(true);
            EditText et0 = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName1);
            et0.setEnabled(true);
//            et0.setEnabled(mainActivity.selectedRevisitGroupMember[0].GuarantorName.isEmpty());
            sp0 = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation1);
            sp0.setEnabled(true);
//            if(mainActivity.selectedRevisitGroupMember[0].GuarantorRelationID == 0)
//                sp0.setEnabled(true);
//            else
//                sp0.setEnabled(false);
        }
//        if(mainActivity.selectedRevisitGroupMember.get(1).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) {
        if(     (mainActivity.selectedRevisitGroupMember.get(1).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) &&
                (mainActivity.selectedRevisitGroupMember.get(1).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) &&
                (mainActivity.selectedRevisitGroupMember.get(1).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED) ) {
            Spinner sp0 = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation2);
            sp0.setEnabled(false);
            CheckBox cb0 = (CheckBox) rootView.findViewById(R.id.checkBoxServiceNote2);
            cb0.setChecked(false);
            sp0 = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus2);
            sp0.setEnabled(false);
            cb0 = (CheckBox) rootView.findViewById(R.id.checkBoxOtherRelativeNote2);
            cb0.setChecked(false);
            EditText et0 = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName2);
            et0.setEnabled(false);
            sp0 = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation2);
            sp0.setEnabled(false);
        } else {
            Spinner sp0 = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation2);
            sp0.setEnabled(true);
            sp0 = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus2);
            sp0.setEnabled(true);
            EditText et0 = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName2);
            et0.setEnabled(true);
//            et0.setEnabled(mainActivity.selectedRevisitGroupMember[1].GuarantorName.isEmpty());
            sp0 = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation2);
            sp0.setEnabled(true);
//            if(mainActivity.selectedRevisitGroupMember[1].GuarantorRelationID == 0)
//                sp0.setEnabled(true);
//            else
//                sp0.setEnabled(false);
        }
//        if(mainActivity.selectedRevisitGroupMember.get(2).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) {
        if(     (mainActivity.selectedRevisitGroupMember.get(2).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) &&
                (mainActivity.selectedRevisitGroupMember.get(2).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) &&
                (mainActivity.selectedRevisitGroupMember.get(2).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED) ) {
            Spinner sp0 = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation3);
            sp0.setEnabled(false);
            CheckBox cb0 = (CheckBox) rootView.findViewById(R.id.checkBoxServiceNote3);
            cb0.setChecked(false);
            sp0 = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus3);
            sp0.setEnabled(false);
            cb0 = (CheckBox) rootView.findViewById(R.id.checkBoxOtherRelativeNote3);
            cb0.setChecked(false);
            EditText et0 = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName3);
            et0.setEnabled(false);
            sp0 = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation3);
            sp0.setEnabled(false);
        } else {
            Spinner sp0 = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation3);
            sp0.setEnabled(true);
            sp0 = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus3);
            sp0.setEnabled(true);
            EditText et0 = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName3);
            et0.setEnabled(true);
//            et0.setEnabled(mainActivity.selectedRevisitGroupMember[2].GuarantorName.isEmpty());
            sp0 = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation3);
            sp0.setEnabled(true);
//            if(mainActivity.selectedRevisitGroupMember[2].GuarantorRelationID == 0)
//                sp0.setEnabled(true);
//            else
//                sp0.setEnabled(false);
        }
        try {
            if ((mainActivity.selectedRevisitGroupMember.get(3).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) &&
                    (mainActivity.selectedRevisitGroupMember.get(3).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) &&
                    (mainActivity.selectedRevisitGroupMember.get(3).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED)) {
                Spinner sp0 = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation4);
                sp0.setEnabled(false);
                CheckBox cb0 = (CheckBox) rootView.findViewById(R.id.checkBoxServiceNote4);
                cb0.setChecked(false);
                sp0 = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus4);
                sp0.setEnabled(false);
                cb0 = (CheckBox) rootView.findViewById(R.id.checkBoxOtherRelativeNote4);
                cb0.setChecked(false);
                EditText et0 = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName4);
                et0.setEnabled(false);
                sp0 = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation4);
                sp0.setEnabled(false);
            } else {
                Spinner sp0 = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation4);
                sp0.setEnabled(true);
                sp0 = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus4);
                sp0.setEnabled(true);
                EditText et0 = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName4);
                et0.setEnabled(true);
//            et0.setEnabled(mainActivity.selectedRevisitGroupMember[3].GuarantorName.isEmpty());
                sp0 = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation4);
                sp0.setEnabled(true);
//            if(mainActivity.selectedRevisitGroupMember[3].GuarantorRelationID == 0)
//                sp0.setEnabled(true);
//            else
//                sp0.setEnabled(false);
            }
        } catch (Exception e) {

        }
        try {
            if ((mainActivity.selectedRevisitGroupMember.get(4).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) &&
                    (mainActivity.selectedRevisitGroupMember.get(4).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) &&
                    (mainActivity.selectedRevisitGroupMember.get(4).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED)) {
                Spinner sp0 = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation5);
                sp0.setEnabled(false);
                CheckBox cb0 = (CheckBox) rootView.findViewById(R.id.checkBoxServiceNote5);
                cb0.setChecked(false);
                sp0 = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus5);
                sp0.setEnabled(false);
                cb0 = (CheckBox) rootView.findViewById(R.id.checkBoxOtherRelativeNote5);
                cb0.setChecked(false);
                EditText et0 = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName5);
                et0.setEnabled(false);
                sp0 = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation5);
                sp0.setEnabled(false);
            } else {
                Spinner sp0 = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckOccupation5);
                sp0.setEnabled(true);
                sp0 = (Spinner) rootView.findViewById(R.id.spinnerGroupCheckMaritalStatus5);
                sp0.setEnabled(true);
                EditText et0 = (EditText) rootView.findViewById(R.id.editTextSinglesGuarantorName5);
                et0.setEnabled(true);
//            et0.setEnabled(mainActivity.selectedRevisitGroupMember[4].GuarantorName.isEmpty());
                sp0 = (Spinner) rootView.findViewById(R.id.spinnerSinglesGuarantorRelation5);
                sp0.setEnabled(true);
//            if(mainActivity.selectedRevisitGroupMember[4].GuarantorRelationID == 0)
//                sp0.setEnabled(true);
//            else
//                sp0.setEnabled(false);
            }
        } catch (Exception e) {

        }
//bmp 19-Jul-22
        String strFilepath = mainActivity.baseDir + "/data/";
        String strFilename = "";

        ImageButton imgbtn = (ImageButton) rootView.findViewById(R.id.imageButtonGroupIDPhoto1);
        imgbtn.setImageBitmap(null);
//        if(selectedRevisitGroupMember.get(0).filenamePhoto != null) {
        if(     (mainActivity.selectedRevisitGroupMember.get(0).filenamePhoto != null) &&
                (!mainActivity.selectedRevisitGroupMember.get(0).filenamePhoto.isEmpty()) ) {
            strFilename = mainActivity.selectedRevisitGroupMember.get(0).filenamePhoto;
            if(!strFilename.contains(".jpg")) {
                strFilename = strFilename + ".jpg";
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inSampleSize = 4;
            try {
                Bitmap bitmap = BitmapFactory.decodeFile((strFilepath + strFilename), options);
//                imgbtn.setImageBitmap(bitmap);
                if(bitmap != null) {
                    int iHt = bitmap.getHeight();
                    int iWd = bitmap.getWidth();
                    if (iHt < iWd) {
                        Matrix matrix = new Matrix();
//                        matrix.postRotate(90);
                        matrix.postRotate(270);
                        matrix.postScale(-1, -1, (bitmap.getWidth()/2f), (bitmap.getHeight()/2f));
                        Bitmap rotate_bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        bitmap.recycle();
                        imgbtn.setImageBitmap(rotate_bitmap);
                    } else {
                        imgbtn.setImageBitmap(bitmap);
                    }
                } else {
                    imgbtn.setImageBitmap(bitmap);
                }
            } catch (OutOfMemoryError e) {
                System.out.println(e.getMessage());
            }
        }
        imgbtn = (ImageButton) rootView.findViewById(R.id.imageButtonGroupIDPhoto2);
        imgbtn.setImageBitmap(null);
//        if(selectedRevisitGroupMember.get(1).filenamePhoto != null) {
        if(     (mainActivity.selectedRevisitGroupMember.get(1).filenamePhoto != null) &&
                (!mainActivity.selectedRevisitGroupMember.get(1).filenamePhoto.isEmpty()) ) {
            strFilename = mainActivity.selectedRevisitGroupMember.get(1).filenamePhoto;
            if(!strFilename.contains(".jpg")) {
                strFilename = strFilename + ".jpg";
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inSampleSize = 4;
            try {
                Bitmap bitmap = BitmapFactory.decodeFile((strFilepath + strFilename), options);
//                imgbtn.setImageBitmap(bitmap);
                if(bitmap != null) {
                    int iHt = bitmap.getHeight();
                    int iWd = bitmap.getWidth();
                    if (iHt < iWd) {
                        Matrix matrix = new Matrix();
//                        matrix.postRotate(90);
                        matrix.postRotate(270);
                        matrix.postScale(-1, -1, (bitmap.getWidth()/2f), (bitmap.getHeight()/2f));
                        Bitmap rotate_bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        bitmap.recycle();
                        imgbtn.setImageBitmap(rotate_bitmap);
                    } else {
                        imgbtn.setImageBitmap(bitmap);
                    }
                } else {
                    imgbtn.setImageBitmap(bitmap);
                }
            } catch (OutOfMemoryError e) {
                System.out.println(e.getMessage());
            }
        }
        imgbtn = (ImageButton) rootView.findViewById(R.id.imageButtonGroupIDPhoto3);
        imgbtn.setImageBitmap(null);
//        if(selectedRevisitGroupMember.get(2).filenamePhoto != null) {
        if(     (mainActivity.selectedRevisitGroupMember.get(2).filenamePhoto != null) &&
                (!mainActivity.selectedRevisitGroupMember.get(2).filenamePhoto.isEmpty()) ) {
            strFilename = mainActivity.selectedRevisitGroupMember.get(2).filenamePhoto;
            if(!strFilename.contains(".jpg")) {
                strFilename = strFilename + ".jpg";
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inSampleSize = 4;
            try {
                Bitmap bitmap = BitmapFactory.decodeFile((strFilepath + strFilename), options);
//                imgbtn.setImageBitmap(bitmap);
                if(bitmap != null) {
                    int iHt = bitmap.getHeight();
                    int iWd = bitmap.getWidth();
                    if (iHt < iWd) {
                        Matrix matrix = new Matrix();
//                        matrix.postRotate(90);
                        matrix.postRotate(270);
                        matrix.postScale(-1, -1, (bitmap.getWidth()/2f), (bitmap.getHeight()/2f));
                        Bitmap rotate_bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        bitmap.recycle();
                        imgbtn.setImageBitmap(rotate_bitmap);
                    } else {
                        imgbtn.setImageBitmap(bitmap);
                    }
                } else {
                    imgbtn.setImageBitmap(bitmap);
                }
            } catch (OutOfMemoryError e) {
                System.out.println(e.getMessage());
            }
        }
        imgbtn = (ImageButton) rootView.findViewById(R.id.imageButtonGroupIDPhoto4);
        imgbtn.setImageBitmap(null);
        try {
            if ((mainActivity.selectedRevisitGroupMember.get(3).filenamePhoto != null) &&
                    (!mainActivity.selectedRevisitGroupMember.get(3).filenamePhoto.isEmpty())) {
                strFilename = mainActivity.selectedRevisitGroupMember.get(3).filenamePhoto;
                if (!strFilename.contains(".jpg")) {
                    strFilename = strFilename + ".jpg";
                }
                BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inSampleSize = 4;
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile((strFilepath + strFilename), options);
//                imgbtn.setImageBitmap(bitmap);
                    if (bitmap != null) {
                        int iHt = bitmap.getHeight();
                        int iWd = bitmap.getWidth();
                        if (iHt < iWd) {
                            Matrix matrix = new Matrix();
//                        matrix.postRotate(90);
                            matrix.postRotate(270);
                            matrix.postScale(-1, -1, (bitmap.getWidth() / 2f), (bitmap.getHeight() / 2f));
                            Bitmap rotate_bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                            bitmap.recycle();
                            imgbtn.setImageBitmap(rotate_bitmap);
                        } else {
                            imgbtn.setImageBitmap(bitmap);
                        }
                    } else {
                        imgbtn.setImageBitmap(bitmap);
                    }
                } catch (OutOfMemoryError e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {

        }
        try {
            imgbtn = (ImageButton) rootView.findViewById(R.id.imageButtonGroupIDPhoto5);
            imgbtn.setImageBitmap(null);
//        if(selectedRevisitGroupMember.get(4).filenamePhoto != null) {
            if ((mainActivity.selectedRevisitGroupMember.get(4).filenamePhoto != null) &&
                    (!mainActivity.selectedRevisitGroupMember.get(4).filenamePhoto.isEmpty())) {
                strFilename = mainActivity.selectedRevisitGroupMember.get(4).filenamePhoto;
                if (!strFilename.contains(".jpg")) {
                    strFilename = strFilename + ".jpg";
                }
                BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inSampleSize = 4;
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile((strFilepath + strFilename), options);
//                imgbtn.setImageBitmap(bitmap);
                    if (bitmap != null) {
                        int iHt = bitmap.getHeight();
                        int iWd = bitmap.getWidth();
                        if (iHt < iWd) {
                            Matrix matrix = new Matrix();
//                        matrix.postRotate(90);
                            matrix.postRotate(270);
                            matrix.postScale(-1, -1, (bitmap.getWidth() / 2f), (bitmap.getHeight() / 2f));
                            Bitmap rotate_bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                            bitmap.recycle();
                            imgbtn.setImageBitmap(rotate_bitmap);
                        } else {
                            imgbtn.setImageBitmap(bitmap);
                        }
                    } else {
                        imgbtn.setImageBitmap(bitmap);
                    }
                } catch (OutOfMemoryError e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {

        }

        Calendar calSet = Calendar.getInstance();

        et = (EditText) rootView.findViewById(R.id.editTextPIFdate);
        if(mainActivity.selectedRevisitGroup.PIFDate.length() < 10) {
            et.setText("");
        } else {
            //yyyy-MM-dd Format for DB
            calSet.set(Calendar.YEAR, Integer.valueOf(mainActivity.selectedRevisitGroup.PIFDate.substring(0, 4)));
            calSet.set(Calendar.MONTH, (Integer.valueOf(mainActivity.selectedRevisitGroup.PIFDate.substring(5, 7)) - 1));
            calSet.set(Calendar.DAY_OF_MONTH, Integer.valueOf(mainActivity.selectedRevisitGroup.PIFDate.substring(8)));
            String strDate = "";
            switch(calSet.get(Calendar.DAY_OF_WEEK)) {
                case 1:
                    strDate = "रविवार";
                    break;
                case 2:
                    strDate = "सोमवार";
                    break;
                case 3:
                    strDate = "मंगळवार";
                    break;
                case 4:
                    strDate = "बुधवार";
                    break;
                case 5:
                    strDate = "गुरुवार";
                    break;
                case 6:
                    strDate = "शुक्रवार";
                    break;
                case 7:
                    strDate = "शनिवार";
                    break;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            strDate = strDate + " " + sdf.format(calSet.getTime());// all done
            et.setText(strDate);
        }

        et = (EditText) rootView.findViewById(R.id.editTextPIFtime);
        if(mainActivity.selectedRevisitGroup.PIFTime.length() < 5) {
            et.setText("00:00");
        } else {
            int hourOfDay = Integer.valueOf(mainActivity.selectedRevisitGroup.PIFTime.substring(0, 2));
            int minute = Integer.valueOf(mainActivity.selectedRevisitGroup.PIFTime.substring(3, 5));
            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);

            String strTime = "सकाळी ";
            if (calSet.get(Calendar.AM_PM) == Calendar.PM) {
                if(hourOfDay < 17)
                    strTime = "दुपारी ";
                else if(hourOfDay < 19)
                    strTime = "संध्याकाळी ";
                else
                    strTime = "रात्री ";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
            strTime = strTime + sdf.format(calSet.getTime());// all done
            et.setText(strTime);
        }

//        et = (EditText) rootView.findViewById(R.id.editTextGRTdate);
//        if(mainActivity.selectedRevisitGroup.GroupTestDate.length() < 10) {
//            et.setText("");
//        } else {
//            //yyyy-MM-dd Format for DB
//            calSet.set(Calendar.YEAR, Integer.valueOf(mainActivity.selectedRevisitGroup.GroupTestDate.substring(0, 4)));
//            calSet.set(Calendar.MONTH, (Integer.valueOf(mainActivity.selectedRevisitGroup.GroupTestDate.substring(5, 7)) - 1));
//            calSet.set(Calendar.DAY_OF_MONTH, Integer.valueOf(mainActivity.selectedRevisitGroup.GroupTestDate.substring(8)));
//            String strDate = "";
//            switch(calSet.get(Calendar.DAY_OF_WEEK)) {
//                case 1:
//                    strDate = "रविवार";
//                    break;
//                case 2:
//                    strDate = "सोमवार";
//                    break;
//                case 3:
//                    strDate = "मंगळवार";
//                    break;
//                case 4:
//                    strDate = "बुधवार";
//                    break;
//                case 5:
//                    strDate = "गुरुवार";
//                    break;
//                case 6:
//                    strDate = "शुक्रवार";
//                    break;
//                case 7:
//                    strDate = "शनिवार";
//                    break;
//            }
//            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
//            strDate = strDate + " " + sdf.format(calSet.getTime());// all done
//            et.setText(strDate);
//        }
//
//        et = (EditText) rootView.findViewById(R.id.editTextGRTtime);
//        if(mainActivity.selectedRevisitGroup.GroupTestTime.length() < 5) {
//            et.setText("00:00");
//        } else {
//            int hourOfDay = Integer.valueOf(mainActivity.selectedRevisitGroup.GroupTestTime.substring(0, 2));
//            int minute = Integer.valueOf(mainActivity.selectedRevisitGroup.GroupTestTime.substring(3, 5));
//            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
//            calSet.set(Calendar.MINUTE, minute);
//
//            String strTime = "सकाळी ";
//            if (calSet.get(Calendar.AM_PM) == Calendar.PM) {
//                if(hourOfDay < 17)
//                    strTime = "दुपारी ";
//                else if(hourOfDay < 19)
//                    strTime = "संध्याकाळी ";
//                else
//                    strTime = "रात्री ";
//            }
//            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
//            strTime = strTime + sdf.format(calSet.getTime());// all done
//            et.setText(strTime);
//        }
//        et = (EditText) rootView.findViewById(R.id.editTextLoanDisbursementDate);
//        if(mainActivity.selectedRevisitGroup.LoanDisbursementDate.length() < 10) {
//            et.setText("");
//        } else {
//            //yyyy-MM-dd Format for DB
//            calSet.set(Calendar.YEAR, Integer.valueOf(mainActivity.selectedRevisitGroup.LoanDisbursementDate.substring(0, 4)));
//            calSet.set(Calendar.MONTH, (Integer.valueOf(mainActivity.selectedRevisitGroup.LoanDisbursementDate.substring(5, 7)) - 1));
//            calSet.set(Calendar.DAY_OF_MONTH, Integer.valueOf(mainActivity.selectedRevisitGroup.LoanDisbursementDate.substring(8)));
//            String strDate = "";
//            switch(calSet.get(Calendar.DAY_OF_WEEK)) {
//                case 1:
//                    strDate = "रविवार";
//                    break;
//                case 2:
//                    strDate = "सोमवार";
//                    break;
//                case 3:
//                    strDate = "मंगळवार";
//                    break;
//                case 4:
//                    strDate = "बुधवार";
//                    break;
//                case 5:
//                    strDate = "गुरुवार";
//                    break;
//                case 6:
//                    strDate = "शुक्रवार";
//                    break;
//                case 7:
//                    strDate = "शनिवार";
//                    break;
//            }
//            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
//            strDate = strDate + " " + sdf.format(calSet.getTime());// all done
//            et.setText(strDate);
//        }

        alGrtTimestampMaster.clear();
        alGrtTimestampMaster.add(0L);
        alGrtDateTimeMaster.clear();
        alGrtDateTimeMaster.add("");
//bmp 20-Aug-25
//        alLoanDisbursementTimestampMaster.clear();
//        alLoanDisbursementTimestampMaster.add(0L);
//        alLoanDisbursementDateTimeMaster.clear();
//        alLoanDisbursementDateTimeMaster.add("");
//bmp 20-Aug-25
        String strTemp = "G-" + String.valueOf(mainActivity.selectedRevisitGroup.GroupID);
        for (int i=0; i<mainActivity.ActivitySchedule.size(); i++) {
            if (mainActivity.ActivitySchedule.get(i).ActivityType.equalsIgnoreCase("GRT")) {
                if(     mainActivity.ActivitySchedule.get(i).ActivityDetails.equalsIgnoreCase("Empty Time Slot") ||
                        mainActivity.ActivitySchedule.get(i).ActivityDetails.equalsIgnoreCase(strTemp)  ) {
                    alGrtTimestampMaster.add(mainActivity.ActivitySchedule.get(i).StartTime);
                    alGrtDateTimeMaster.add(convertTimeInMillisToString(mainActivity.ActivitySchedule.get(i).StartTime));
                }
//bmp 20-Aug-25
//            } else if (mainActivity.ActivitySchedule.get(i).ActivityType.equalsIgnoreCase("Orientation")) {
//                if(     mainActivity.ActivitySchedule.get(i).ActivityDetails.equalsIgnoreCase("Empty Time Slot") ||
//                        mainActivity.ActivitySchedule.get(i).ActivityDetails.equalsIgnoreCase(strTemp)  ) {
//                    alLoanDisbursementTimestampMaster.add(mainActivity.ActivitySchedule.get(i).StartTime);
//                    alLoanDisbursementDateTimeMaster.add(convertTimeInMillisToString(mainActivity.ActivitySchedule.get(i).StartTime));
//                }
//bmp 20-Aug-25
            }
        }
        ArrayAdapter<String> adapterGrtDateTime = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, alGrtDateTimeMaster);
        adapterGrtDateTime.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
        sp = (Spinner) rootView.findViewById(R.id.spinnerGRTdatetime);
        sp.setAdapter(adapterGrtDateTime);
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //                    if(posRelation1 == position)
                //                        return;
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(alGrtTimestampMaster.get(position));
                //yyyy-MM-dd Format for DB
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                mainActivity.selectedRevisitGroup.GroupTestDate = sdf2.format(cal.getTime());
                mainActivity.selectedRevisitGroup.GroupTestTime = sdf3.format(cal.getTime());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
//bmp 20-Aug-25
//        ArrayAdapter<String> adapterLoanDisbursementDateTime = new ArrayAdapter<String>(mainActivity.getBaseContext(),
//                R.layout.simple_spinner_item, alLoanDisbursementDateTimeMaster);
//        adapterLoanDisbursementDateTime.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
//        sp = (Spinner) rootView.findViewById(R.id.spinnerLoanDisbursementDatetime);
//        sp.setAdapter(adapterLoanDisbursementDateTime);
//        sp.setFocusable(true);
//        sp.setFocusableInTouchMode(true);
//        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //                    if(posRelation1 == position)
//                //                        return;
//                Calendar cal = Calendar.getInstance();
//                cal.setTimeInMillis(alLoanDisbursementTimestampMaster.get(position));
//                //yyyy-MM-dd Format for DB
//                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//                SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
//                mainActivity.selectedRevisitGroup.LoanDisbursementDate = sdf2.format(cal.getTime());
//                mainActivity.selectedRevisitGroup.LoanDisbursementTime = sdf3.format(cal.getTime());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//bmp 20-Aug-25
        tv = (TextView) rootView.findViewById(R.id.textViewCenterGroup);
        tv.requestFocus();
    }
    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            Calendar calSet = Calendar.getInstance();
            calSet.set(Calendar.YEAR, year);
            calSet.set(Calendar.MONTH, monthOfYear);
            calSet.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            long lSet = calSet.getTimeInMillis();
            Calendar calNow = Calendar.getInstance();
            long lNow = calNow.getTimeInMillis();
            if(lSet < lNow) {
                System.out.println("Invalid Date!");
                return;
            }

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            String strDate = "";
            switch(myCalendar.get(Calendar.DAY_OF_WEEK)) {
                case 1:
                    strDate = "रविवार";
                    break;
                case 2:
                    strDate = "सोमवार";
                    break;
                case 3:
                    strDate = "मंगळवार";
                    break;
                case 4:
                    strDate = "बुधवार";
                    break;
                case 5:
                    strDate = "गुरुवार";
                    break;
                case 6:
                    strDate = "शुक्रवार";
                    break;
                case 7:
                    strDate = "शनिवार";
                    break;
            }
            strDate = strDate + " " + sdf.format(myCalendar.getTime());// all done
            //yyyy-MM-dd Format for DB
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            switch (editDateType) {
                case 4:
                    etPifDate.setText(strDate);
                    mainActivity.selectedRevisitGroup.PIFDate = sdf2.format(myCalendar.getTime());
                    break;
                case 5:
                    etGrtDate.setText(strDate);
                    mainActivity.selectedRevisitGroup.GroupTestDate = sdf2.format(myCalendar.getTime());
                    break;
//bmp 20-Aug-25
//                case 6:
//                    etLoanDisbursementDate.setText(strDate);
//                    mainActivity.selectedRevisitGroup.LoanDisbursementDate = sdf2.format(myCalendar.getTime());
//                    break;
//bmp 20-Aug-25
            }
        }
    };
    final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);

            String strTime = "सकाळी ";
            if (myCalendar.get(Calendar.AM_PM) == Calendar.PM) {
                if(hourOfDay < 17)
                    strTime = "दुपारी ";
                else if(hourOfDay < 19)
                    strTime = "संध्याकाळी ";
                else
                    strTime = "रात्री ";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
            strTime = strTime + sdf.format(myCalendar.getTime());// all done
            //24Hour Format for DB
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

            switch (editTimeType) {
                case 4:
                    etPifTime.setText(strTime);
                    mainActivity.selectedRevisitGroup.PIFTime = sdf2.format(myCalendar.getTime());
                    break;
                case 5:
                    etGrtTime.setText(strTime);
                    mainActivity.selectedRevisitGroup.GroupTestTime = sdf2.format(myCalendar.getTime());
                    break;
                case 6:
                    mainActivity.selectedRevisitGroup.LoanDisbursementTime = sdf2.format(myCalendar.getTime());
                    break;
            }
        }
    };
    public void ValidateScreenNewGroupForm() {
        NewGroupFormProblemsCheck.clear();

//        int iSingles = 0;
//        int iOnRent = 0;
        boolean boolFlag = false;
        int iLoanApplicantCount = 0;
        for(int i=0; i<mainActivity.selectedRevisitGroup.MemberCount; i++) {
            clsRevisitMember member = mainActivity.selectedRevisitGroupMember.get(i);

//            if(member.Single)
//                iSingles++;
//            if(member.OwnHouse == 0)
//                iOnRent++;

//            if(member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) {
            if(     (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) &&
                    (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) &&
                    (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED) ) {
                //Skip This Validation Check if Group Member Not Applying for Loan
            } else {
                iLoanApplicantCount++;

                if (member.MaritalStatusID == 0) {
                    NewGroupFormProblemsCheck.add("सभासदाची वैवाहिक स्थिती भरणे बाकी - सभासद: " + member.MemberName);
                }
                if (member.OccupationID == 0) {
                    NewGroupFormProblemsCheck.add("सभासदाचा व्यवसाय भरणे बाकी - सभासद: " + member.MemberName);
                }
                if(!ValidateMainMemberProfession(member.OccupationID)) {
                    NewGroupFormProblemsCheck.add("सभासदाचा व्यवसाय अयोग्य आहे - सभासद: " + member.MemberName);
                }
            }

//            if(member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) {
            if(     (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) &&
                    (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) &&
                    (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED) ) {
                //Skip This Validation Check if Group Member Not Applying for Loan
            } else {
                if(member.GuarantorName.isEmpty()) {
                    NewGroupFormProblemsCheck.add("जामीनदाराचे पूर्ण नाव भरणे बाकी - सभासद: " + member.MemberName);
                } else {
                    String strFname = "";
                    String strMname = "";
                    String strLname = "";
                    String strName = member.GuarantorName.trim();
                    if (!strName.isEmpty()) {
                        String[] strSplit = strName.split(" ");
                        if (strSplit.length > 0) {
                            strFname = strSplit[0];
                        }
                        if (strSplit.length > 2) {
                            strMname = strSplit[1];
                            strLname = strSplit[2];
                        } else if (strSplit.length > 1) {
                            strMname = "";
                            strLname = strSplit[1];
                        }
                    }
                    if ((strFname.isEmpty()) ||
                            (strMname.isEmpty() && strLname.isEmpty())) {
                        NewGroupFormProblemsCheck.add("जामीनदाराचे पूर्ण नाव भरणे बाकी - सभासद: " + member.MemberName);
                    }
                }
                if(member.GuarantorRelationID == 0) {
                    NewGroupFormProblemsCheck.add("जामीनदाराचे नाते भरणे बाकी - सभासद: " + member.MemberName);
                }
                if(member.Single) {
                    if(     (member.GuarantorRelationID == RelationIDMainMember) ||
                            (member.GuarantorRelationID == RelationIDSpouse) ) {
                        NewGroupFormProblemsCheck.add("जामीनदाराचे नाते अयोग्य आहे - सभासद: " + member.MemberName);
                    }
                }
            }

            if (member.filenamePhoto.isEmpty()) {
                NewGroupFormProblemsCheck.add("सभासदाचा फोटो बाकी आहे - सभासद: " + member.MemberName);
            } else {
                String strFilepath = mainActivity.baseDir + "/data/";
                String strFilename = member.filenamePhoto;
                File jpgfile = new File(strFilepath + strFilename + ".jpg");
                if(!jpgfile.exists()) {
                    NewGroupFormProblemsCheck.add("सभासदाचा फोटो बाकी आहे - सभासद: " + member.MemberName);
                }
            }

            if(     (mainActivity.selectedRevisitGroup.CollectionMemberID == member.MemberID) &&
                    (mainActivity.selectedRevisitGroup.CollectionMemberID > 0) ) {
                boolFlag = true;
            }
        }

        if(!boolFlag) {
            NewGroupFormProblemsCheck.add("'हप्ता संकलन कोणाच्या घरी करणार' भरणे बाकी.");
        }

        if(mainActivity.selectedRevisitGroup.LastCycleGroupContinued == 0) {
//            if((iSingles + iOnRent) > 2)
//                NewGroupFormProblemsCheck.add("भाड्याच्या घरात राहणाऱ्या अथवा एकट्या सभासदांची संख्या 2 पेक्षा जास्त आहे.");
        }

        if(mainActivity.selectedRevisitGroup.flagGRTQ2a < 0) {
            NewGroupFormProblemsCheck.add("'गटातील सभासद एकमेकांचे नातेवाईक आहेत का?' भरणे बाकी.");
        }

        Calendar today = Calendar.getInstance();
        long ltoday = today.getTimeInMillis();
        long lOneHour = (1000L * 60L * 60L);
        long lTenMinutes = (1000L * 60L * 10L);
        long lOneMonth = (1000L * 60L * 60L * 24L * 30L);
        long lTwoMonths = (1000L * 60L * 60L * 24L * 60L);
        long lOneDay = (1000L * 60L * 60L * 24L * 1L);
        long lFiftyDays = (1000L * 60L * 60L * 24L * 50L);
        long ldoPIF = 0L;
        long ldoGRT = 0L;
        long ldoLoanDisbursement = 0L;

        if( mainActivity.selectedRevisitGroup.PIFDate.isEmpty() ) {
            NewGroupFormProblemsCheck.add("पी. आय. एफ. भरण्याची तारीख भरणे बाकी.");
        } else {
            String strDate = mainActivity.selectedRevisitGroup.PIFDate.substring(0, 10);
            String[] strSplit = strDate.split("-");
            Calendar doe = Calendar.getInstance();
            doe.set(Calendar.YEAR, Integer.parseInt(strSplit[0]));
            doe.set(Calendar.MONTH, (Integer.parseInt(strSplit[1]) - 1));
            doe.set(Calendar.DATE, Integer.parseInt(strSplit[2]));
            ldoPIF = doe.getTimeInMillis();
            long lDiff = ldoPIF - ltoday;
            if( (lDiff < 0L) || (lDiff > lTwoMonths) ) { //upto 2 months from today
                NewGroupFormProblemsCheck.add("पी. आय. एफ. भरण्याची तारीख पुढील 60 दिवसाच्या आतील असावी.");
            }
        }
        if( mainActivity.selectedRevisitGroup.PIFTime.isEmpty() ) {
            NewGroupFormProblemsCheck.add("पी. आय. एफ. भरण्याची वेळ भरणे बाकी.");
        }
//bmp 21-Feb-24
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        String strPIFtimestamp = mainActivity.selectedRevisitGroup.PIFDate + " " + mainActivity.selectedRevisitGroup.PIFTime;
        try {
            Date datePIFtimestamp = sdf2.parse(strPIFtimestamp);
            Calendar doePIF = Calendar.getInstance();
            doePIF.setTime(datePIFtimestamp);
            long lPIFStartTime = doePIF.getTimeInMillis();
//            long lPIFEndTime = lPIFStartTime + lOneHour;
            long lPIFEndTime = lPIFStartTime + (iLoanApplicantCount * lTenMinutes);

            //int searchScheduleActivity(String strType, String strDetails)
            String strType = "PIF";
            String strDetails = "G-" + String.valueOf(mainActivity.selectedRevisitGroup.GroupID);
            if(mainActivity.searchScheduleTime(strType, strDetails, lPIFStartTime, lPIFEndTime)) {
                NewGroupFormProblemsCheck.add("पी. आय. एफ. भरण्याची तारीख/वेळ अयोग्य आहे.");
            } else {
                mainActivity.selectedRevisitGroup.lPIFStartTime = lPIFStartTime;
                mainActivity.selectedRevisitGroup.lPIFEndTime = lPIFEndTime;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
//bmp 21-Feb-24
        if( mainActivity.selectedRevisitGroup.GroupTestDate.isEmpty() ) {
            NewGroupFormProblemsCheck.add("गट चाचणीची तारीख भरणे बाकी.");
        } else {
            String strDate = mainActivity.selectedRevisitGroup.GroupTestDate.substring(0, 10);
            String[] strSplit = strDate.split("-");
            Calendar doe = Calendar.getInstance();
            doe.set(Calendar.YEAR, Integer.parseInt(strSplit[0]));
            doe.set(Calendar.MONTH, (Integer.parseInt(strSplit[1]) - 1));
            doe.set(Calendar.DATE, Integer.parseInt(strSplit[2]));
            ldoGRT = doe.getTimeInMillis();
            if(ldoGRT < ldoPIF) {
                NewGroupFormProblemsCheck.add("गट चाचणीची तारीख पी. आय. एफ. भरण्याच्या तारीखेनंतर असावी.");
            }
            long lDiff = ldoGRT - ltoday;
            if( lDiff > lTwoMonths) { // 2 months
                NewGroupFormProblemsCheck.add("गट चाचणीची तारीख पुढील 60 दिवसाच्या आतील असावी.");
            }
        }
        if( mainActivity.selectedRevisitGroup.GroupTestTime.isEmpty() ) {
            NewGroupFormProblemsCheck.add("गट चाचणीची वेळ भरणे बाकी.");
        }
//bmp 21-Feb-24
        String strGRTtimestamp = mainActivity.selectedRevisitGroup.GroupTestDate + " " + mainActivity.selectedRevisitGroup.GroupTestTime;
        try {
            Date dateGRTtimestamp = sdf2.parse(strGRTtimestamp);
            Calendar doeGRT = Calendar.getInstance();
            doeGRT.setTime(dateGRTtimestamp);
            long lGRTStartTime = doeGRT.getTimeInMillis();
            long lGRTEndTime = lGRTStartTime + lOneHour;

            //int searchScheduleActivity(String strType, String strDetails)
            String strType = "GRT";
            String strDetails = "G-" + String.valueOf(mainActivity.selectedRevisitGroup.GroupID);
            if(mainActivity.searchScheduleTime(strType, strDetails, lGRTStartTime, lGRTEndTime)) {
                NewGroupFormProblemsCheck.add("गट चाचणीची तारीख/वेळ अयोग्य आहे.");
            } else {
                mainActivity.selectedRevisitGroup.lGRTStartTime = lGRTStartTime;
                mainActivity.selectedRevisitGroup.lGRTEndTime = lGRTEndTime;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
//bmp 21-Feb-24
//bmp 20-Aug-25
//        if( mainActivity.selectedRevisitGroup.LoanDisbursementDate.isEmpty() ) {
//            NewGroupFormProblemsCheck.add("कर्ज वाटपाची तारीख भरणे बाकी.");
//        } else {
//            String strDate = mainActivity.selectedRevisitGroup.LoanDisbursementDate.substring(0, 10);
//            String[] strSplit = strDate.split("-");
//            Calendar doe = Calendar.getInstance();
//            doe.set(Calendar.YEAR, Integer.parseInt(strSplit[0]));
//            doe.set(Calendar.MONTH, (Integer.parseInt(strSplit[1]) - 1));
//            doe.set(Calendar.DATE, Integer.parseInt(strSplit[2]));
//            ldoLoanDisbursement = doe.getTimeInMillis();
//            long lDiff = ldoLoanDisbursement - ldoGRT;
//            if(lDiff < lOneDay) {
//                NewGroupFormProblemsCheck.add("कर्ज वाटपाची तारीख गट चाचणीच्या तारीखेनंतर असावी.");
//            }
//            if( lDiff > lFiftyDays) { // 50 days
//                NewGroupFormProblemsCheck.add("कर्ज वाटपाची तारीख गट चाचणीच्या तारीखेनंतर 50 दिवसाच्या आतील असावी.");
//            }
//        }
//        String strOrientationtimestamp = mainActivity.selectedRevisitGroup.LoanDisbursementDate + " " +
//                                            mainActivity.selectedRevisitGroup.LoanDisbursementTime;
//        try {
//            Date dateOrientationtimestamp = sdf2.parse(strOrientationtimestamp);
//            Calendar doeOrientation = Calendar.getInstance();
//            doeOrientation.setTime(dateOrientationtimestamp);
//            long lOrientationStartTime = doeOrientation.getTimeInMillis();
//            long lOrientationEndTime = lOrientationStartTime + lOneHour;
//
//            //int searchScheduleActivity(String strType, String strDetails)
//            String strType = "Orientation";
//            String strDetails = "G-" + String.valueOf(mainActivity.selectedRevisitGroup.GroupID);
//            if(mainActivity.searchScheduleTime(strType, strDetails, lOrientationStartTime, lOrientationEndTime)) {
//                NewGroupFormProblemsCheck.add("कर्ज वाटपाची तारीख/वेळ अयोग्य आहे.");
//            } else {
//                mainActivity.selectedRevisitGroup.lOrientationStartTime = lOrientationStartTime;
//                mainActivity.selectedRevisitGroup.lOrientationEndTime = lOrientationEndTime;
//            }
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
//bmp 20-Aug-25
    }
    public boolean ValidateMainMemberProfession(long ProfessionID) {
        boolean flagValid = true;

        if(mainActivity.OrgID == 1) {
            if(     (ProfessionID == 39) ||
                    (ProfessionID == 40) ||
                    (ProfessionID == 55) ||
                    (ProfessionID == 56) ||
                    (ProfessionID == 57) ||
                    (ProfessionID == 59) ||
                    (ProfessionID == 62) ||
                    (ProfessionID == 63) ) {
                flagValid = false;
            }
        } else if(mainActivity.OrgID == 2) {
            if(     (ProfessionID == 39) ||
                    (ProfessionID == 40) ||
                    (ProfessionID == 55) ||
                    (ProfessionID == 56) ||
                    (ProfessionID == 57) ||
                    (ProfessionID == 59) ||
                    (ProfessionID == 62) ||
                    (ProfessionID == 63) ) {
                flagValid = false;
            }
        }

        return flagValid;
    }

    /*ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Add same code that you want to add in onActivityResult method
                    String strFilepath = mainActivity.baseDir + "/data/";
                    String strFilename = "";
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        switch (IDRequest) {
                            case ID_REQUEST_PHOTO_Member1:
                                strFilename = "member1_photo";
                                File outFile = new File(strFilepath + strFilename + ".jpg");
                                if(outFile.exists()) {
                                    if(outFile.length() >= MAX_PHOTO_FILESIZE) {
//bmp 28-Apr-26
//                                        ShowdlgAlertonActivityResult("Incorrect Image file size, Adjust Camera Setting and Try Again!");
//                                        break;
                                        String currentPhotoPath = strFilepath + strFilename + ".jpg";
                                        mainActivity.AdjustImageFileSize(currentPhotoPath);
//bmp 28-Apr-26
                                    }
                                    mainActivity.selectedRevisitGroupMember.get(0).filenamePhoto = strFilename;
                                    RefreshGroupForm();
                                }
                                break;
                            case ID_REQUEST_PHOTO_Member2:
                                strFilename = "member2_photo";
                                outFile = new File(strFilepath + strFilename + ".jpg");
                                if(outFile.exists()) {
                                    if(outFile.length() >= MAX_PHOTO_FILESIZE) {
//bmp 28-Apr-26
//                                        ShowdlgAlertonActivityResult("Incorrect Image file size, Adjust Camera Setting and Try Again!");
//                                        break;
                                        String currentPhotoPath = strFilepath + strFilename + ".jpg";
                                        mainActivity.AdjustImageFileSize(currentPhotoPath);
//bmp 28-Apr-26
                                    }
                                    mainActivity.selectedRevisitGroupMember.get(1).filenamePhoto = strFilename;
                                    RefreshGroupForm();
                                }
                                break;
                            case ID_REQUEST_PHOTO_Member3:
                                strFilename = "member3_photo";
                                outFile = new File(strFilepath + strFilename + ".jpg");
                                if(outFile.exists()) {
                                    if(outFile.length() >= MAX_PHOTO_FILESIZE) {
//bmp 28-Apr-26
//                                        ShowdlgAlertonActivityResult("Incorrect Image file size, Adjust Camera Setting and Try Again!");
//                                        break;
                                        String currentPhotoPath = strFilepath + strFilename + ".jpg";
                                        mainActivity.AdjustImageFileSize(currentPhotoPath);
//bmp 28-Apr-26
                                    }
                                    mainActivity.selectedRevisitGroupMember.get(2).filenamePhoto = strFilename;
                                    RefreshGroupForm();
                                }
                                break;
                            case ID_REQUEST_PHOTO_Member4:
                                strFilename = "member4_photo";
                                outFile = new File(strFilepath + strFilename + ".jpg");
                                if(outFile.exists()) {
                                    if(outFile.length() >= MAX_PHOTO_FILESIZE) {
//bmp 28-Apr-26
//                                        ShowdlgAlertonActivityResult("Incorrect Image file size, Adjust Camera Setting and Try Again!");
//                                        break;
                                        String currentPhotoPath = strFilepath + strFilename + ".jpg";
                                        mainActivity.AdjustImageFileSize(currentPhotoPath);
//bmp 28-Apr-26
                                    }
                                    mainActivity.selectedRevisitGroupMember.get(3).filenamePhoto = strFilename;
                                    RefreshGroupForm();
                                }
                                break;
                            case ID_REQUEST_PHOTO_Member5:
                                strFilename = "member5_photo";
                                outFile = new File(strFilepath + strFilename + ".jpg");
                                if(outFile.exists()) {
                                    if(outFile.length() >= MAX_PHOTO_FILESIZE) {
//bmp 28-Apr-26
//                                        ShowdlgAlertonActivityResult("Incorrect Image file size, Adjust Camera Setting and Try Again!");
//                                        break;
                                        String currentPhotoPath = strFilepath + strFilename + ".jpg";
                                        mainActivity.AdjustImageFileSize(currentPhotoPath);
//bmp 28-Apr-26
                                    }
                                    mainActivity.selectedRevisitGroupMember.get(4).filenamePhoto = strFilename;
                                    RefreshGroupForm();
                                }
                                break;
                        }
                    } else if(result.getResultCode() == RESULT_CANCELED){
                        //handle cancel
                        ShowdlgAlertonActivityResult("CAMERA REQUEST CANCELED!");
                    }
                }
            });*/
//RK
    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {

                    String strFilepath =
                            mainActivity.baseDir + "/data/";

                    String strFilename = "";

                    if (result.getResultCode() == Activity.RESULT_OK) {

                        // 1. GET IMAGE URI FROM CAMERA APP

                        Intent data = result.getData();

                        if (data == null) {
                            ShowdlgAlertonActivityResult(
                                    "No result received from Camera App!"
                            );
                            return;
                        }

                        Uri imageUri = data.getData();

                        // Fallback for EXTRA_STREAM
                        if (imageUri == null) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                                imageUri = data.getParcelableExtra(
                                        Intent.EXTRA_STREAM,
                                        Uri.class
                                );

                            } else {

                                imageUri = data.getParcelableExtra(
                                        Intent.EXTRA_STREAM
                                );
                            }
                        }

                        if (imageUri == null) {
                            ShowdlgAlertonActivityResult(
                                    "No image received from Camera App!"
                            );
                            return;
                        }

                        switch (IDRequest) {

                            case ID_REQUEST_PHOTO_Member1:
                                strFilename = "member1_photo";
                                break;

                            case ID_REQUEST_PHOTO_Member2:
                                strFilename = "member2_photo";
                                break;

                            case ID_REQUEST_PHOTO_Member3:
                                strFilename = "member3_photo";
                                break;

                            case ID_REQUEST_PHOTO_Member4:
                                strFilename = "member4_photo";
                                break;

                            case ID_REQUEST_PHOTO_Member5:
                                strFilename = "member5_photo";
                                break;

                            default:
                                ShowdlgAlertonActivityResult(
                                        "Invalid photo request!"
                                );
                                return;
                        }


                        // 3. FINANCE APP DESTINATION FILE

                        File outFile = new File(
                                strFilepath +
                                        strFilename +
                                        ".jpg"
                        );


                        // 4. COPY IMAGE FROM CAMERA APP TO FINANCE APP

                        try {

                            InputStream inputStream =
                                    mainActivity
                                            .getContentResolver()
                                            .openInputStream(imageUri);

                            if (inputStream == null) {

                                ShowdlgAlertonActivityResult(
                                        "Unable to read image received from Camera App!"
                                );
                                return;
                            }

                            FileOutputStream outputStream =
                                    new FileOutputStream(outFile);

                            byte[] buffer = new byte[8192];

                            int length;

                            while ((length =
                                    inputStream.read(buffer)) != -1) {

                                outputStream.write(
                                        buffer,
                                        0,
                                        length
                                );
                            }

                            outputStream.flush();

                            outputStream.close();
                            inputStream.close();


                        } catch (Exception e) {

                            e.printStackTrace();

                            ShowdlgAlertonActivityResult(
                                    "Unable to save captured image!\n" +
                                            e.getMessage()
                            );
                            return;
                        }



                        // 5. VERIFY FILE

                        if (!outFile.exists() ||
                                outFile.length() == 0) {

                            ShowdlgAlertonActivityResult(
                                    "Image was not saved!"
                            );
                            return;
                        }

                        // 6. YOUR EXISTING LOGIC

                        switch (IDRequest) {

                            case ID_REQUEST_PHOTO_Member1:

                                strFilename = "member1_photo";

                                outFile = new File(
                                        strFilepath +
                                                strFilename +
                                                ".jpg"
                                );

                                if (outFile.exists()) {

                                    if (outFile.length() >=
                                            MAX_PHOTO_FILESIZE) {

                                        String currentPhotoPath =
                                                strFilepath +
                                                        strFilename +
                                                        ".jpg";

                                        mainActivity.AdjustImageFileSize(
                                                currentPhotoPath
                                        );
                                    }

                                    mainActivity
                                            .selectedRevisitGroupMember
                                            .get(0)
                                            .filenamePhoto =
                                            strFilename;

                                    RefreshGroupForm();
                                }

                                break;


                            case ID_REQUEST_PHOTO_Member2:

                                strFilename = "member2_photo";

                                outFile = new File(strFilepath + strFilename + ".jpg");

                                if (outFile.exists()) {

                                    if (outFile.length() >=
                                            MAX_PHOTO_FILESIZE) {

                                        String currentPhotoPath = strFilepath + strFilename + ".jpg";

                                        mainActivity.AdjustImageFileSize(
                                                currentPhotoPath
                                        );
                                    }

                                    mainActivity
                                            .selectedRevisitGroupMember
                                            .get(1)
                                            .filenamePhoto = strFilename;

                                    RefreshGroupForm();
                                }

                                break;


                            case ID_REQUEST_PHOTO_Member3:

                                strFilename = "member3_photo";

                                outFile = new File(strFilepath + strFilename + ".jpg");

                                if (outFile.exists()) {

                                    if (outFile.length() >=
                                            MAX_PHOTO_FILESIZE) {

                                        String currentPhotoPath = strFilepath + strFilename + ".jpg";

                                        mainActivity.AdjustImageFileSize(
                                                currentPhotoPath
                                        );
                                    }

                                    mainActivity
                                            .selectedRevisitGroupMember
                                            .get(2)
                                            .filenamePhoto = strFilename;

                                    RefreshGroupForm();
                                }

                                break;


                            case ID_REQUEST_PHOTO_Member4:

                                strFilename = "member4_photo";

                                outFile = new File(strFilepath + strFilename + ".jpg");

                                if (outFile.exists()) {

                                    if (outFile.length() >=
                                            MAX_PHOTO_FILESIZE) {

                                        String currentPhotoPath = strFilepath + strFilename + ".jpg";
                                        mainActivity.AdjustImageFileSize(
                                                currentPhotoPath
                                        );
                                    }

                                    mainActivity
                                            .selectedRevisitGroupMember
                                            .get(3)
                                            .filenamePhoto = strFilename;
                                    RefreshGroupForm();
                                }

                                break;


                            case ID_REQUEST_PHOTO_Member5:

                                strFilename = "member5_photo";

                                outFile = new File(strFilepath + strFilename + ".jpg");

                                if (outFile.exists()) {

                                    if (outFile.length() >=
                                            MAX_PHOTO_FILESIZE) {

                                        String currentPhotoPath = strFilepath + strFilename + ".jpg";

                                        mainActivity.AdjustImageFileSize(currentPhotoPath);
                                    }

                                    mainActivity
                                            .selectedRevisitGroupMember.get(4)
                                            .filenamePhoto = strFilename;

                                    RefreshGroupForm();
                                }

                                break;
                        }

                    } else if (
                            result.getResultCode() == Activity.RESULT_CANCELED) {

                        ShowdlgAlertonActivityResult("CAMERA REQUEST CANCELED!");
                    }
                }
            }
    );
    void ShowdlgAlertonActivityResult(String strMessage) {
//        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(mainActivity.getApplicationContext());
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getContext());
//        TextView tvTitle = new TextView(mainActivity.getApplicationContext());
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
    public long AddMemberDocRcvd(int DocID, String strDocNo, boolean syncRecord) {
        clsMemberDocRcvd newDocRcvd = new clsMemberDocRcvd();
        newDocRcvd.Init_MemberDocRcvd();
        newDocRcvd.MemberDocumentsID = DocID;
        newDocRcvd.MemberID = mainActivity.Member.get(0).MemberID;
        newDocRcvd.DocumentNo = strDocNo;
        newDocRcvd.IsActive = 1;

        mainActivity.MemberDocRcvd.add(newDocRcvd);
        int newIndex = mainActivity.MemberDocRcvd.size() - 1;

        Calendar cal = Calendar.getInstance();
        while(LastTimeInMillis > cal.getTimeInMillis()) {
            cal = Calendar.getInstance();
        }
        LastTimeInMillis = cal.getTimeInMillis();

        LastTimeInMillis += 1000;
        mainActivity.SQLite_saveMemberDocRcvd(newIndex, LastTimeInMillis, syncRecord);
        newDocRcvd.MemberDocuemntsRcvdID = mainActivity.MemberDocRcvd.get(newIndex).MemberDocuemntsRcvdID;

        clsMemberDocRcvd tnewDocRcvd = new clsMemberDocRcvd();
        newDocRcvd.Copy_MemberDocRcvd(tnewDocRcvd);
        mainActivity.tMemberDocRcvd.add(tnewDocRcvd);

        return newDocRcvd.MemberDocuemntsRcvdID;
    }
}
