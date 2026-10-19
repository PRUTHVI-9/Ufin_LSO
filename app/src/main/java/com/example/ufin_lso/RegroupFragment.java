package com.example.ufin_lso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
 * Use the {@link RegroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegroupFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View rootView;
    MainActivity mainActivity;

    public static final int ufin_REGROUP_DIALOG_STATE_NONE                  = 0;
    public static final int ufin_DIALOG_STATE_VALIDATE_REGROUP_SCREEN       = 20;
    public static final int ufin_DIALOG_STATE_CONFIRM_REGROUP_SCREEN        = 21;
    public static final int ufin_REGROUP_DIALOG_STATE_RESUME_PRINTING       = 6;
    public static final int ufin_REGROUP_DIALOG_STATE_NO_PRINTER            = 7;
    public static final int ufin_REGROUP_DIALOG_STATE_CBCHECK_DOC_UPLOAD    = 8;
    public static final int ufin_REGROUP_DIALOG_STATE_ACTIVE_LOAN_GROUP     = 9;
    public static final int ufin_REGROUP_DIALOG_STATE_CONFIRM_PRINT_BUTTON  = 10;
//bmp 21-Aug-26
    public static final String MSG_ufin_REGROUP_CONFIRM_PRINT_BUTTON =
//bmp 31-Aug-26
//        "Press OK to Continue Print.\nPress Cancel to Abort.";
        "To Print: Check Printer is Connected,\nthen Press OK to Continue Print.\nPress Cancel to Abort.";
//bmp 31-Aug-26
//bmp 21-Aug-26

    public static final String MSG_ufin_CBCHECK_DOC_UPLOAD =
            "CB Check Document sent to Server.";
    public static final String MSG_ufin_REGROUP_NO_PRINTER =
            "Printer Not Connected.\nPress Cancel and Retry.";
    public static final String MSG_ufin_REGROUP_RESUME_PRINTING =
            "After printing is complete, Press OK to Resume Printing";

    public static final String MSG_ufin_REGROUP_CONFIRM_SAVE =
        "Press OK to Confirm Save Group.";

    public AlertDialog.Builder dlgAlert;
    public static int dlgState;// = ufin_REVISIT_DIALOG_STATE_NONE;;

    int selectedRegroupMemberPosition;
    long selectedRegroupMemberID;
    int selectedOthergroupMemberPosition;
    long selectedOthergroupMemberID;
    ArrayList<String> RegroupProblemsCheck = new ArrayList<String>();
    clsRevisitGroup selectedRevisitGroup, originalRevisitGroup;
    long LastTimeInMillis;
//    private ArrayList<Integer> ReasonIDMaster = new ArrayList<Integer>();
//    private ArrayList<String> ReasonMarathiDescriptionMaster = new ArrayList<String>();
    long selectedNoLoanGroupID;

    public static final int MAX_PHOTO_FILESIZE             = (4 * 1024 * 1024);
//    public static final int MAX_PHOTO_FILESIZE             = (8 * 1024 * 1024);
    int IDRequest = -1;
    String strFilepath = "";
    String strFilename = "unknown";
    boolean flagDisableNextPrint;
    boolean ConfirmPrint;
    int iPrintButton;
    int iIDindex;

    public RegroupFragment() {
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
    public static RegroupFragment newInstance(String param1, String param2) {
        RegroupFragment fragment = new RegroupFragment();
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
//        return inflater.inflate(R.layout.fragment_regroup, container, false);
//        rootView = inflater.inflate(R.layout.fragment_regroup, container, false);
        rootView = inflater.inflate(R.layout.fragment_regroup2, container, false);
        mainActivity = (MainActivity) getActivity();
//bmp 11-May-23
        mainActivity.SQLite_getPunarbhetVisitDetailsMaster();
//bmp 11-May-23
        ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarRegroup);
        if (pbSync != null) {
            pbSync.setIndeterminate(true);
            pbSync.setVisibility(View.VISIBLE);
        }

        ConfirmPrint = false;
        iPrintButton = -1;
        iIDindex = -1;

        Button bt = (Button) rootView.findViewById(R.id.buttonCloseRegroup);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonSaveRegroup);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonRemoveRegroup);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonAddRegroup);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonReplaceRegroup);
        bt.setOnClickListener(this);

        bt = (Button) rootView.findViewById(R.id.buttonSaveRegroup);
        bt.setEnabled(false);

        mainActivity.selectedGroupID = -1;
        mainActivity.selectedClusterID = -1;
        mainActivity.selectedCommunityID = -1;
        selectedRegroupMemberPosition = -1;
        selectedRegroupMemberID = -1;
        selectedOthergroupMemberPosition = -1;
        selectedOthergroupMemberID = -1;

        final ArrayList<String> ReGroupInfoList = new ArrayList<String>();
        ReGroupInfoList.clear();
        final ArrayList<clsRevisitGroup> ReGroupList = new ArrayList<clsRevisitGroup>();
        ReGroupList.clear();
        for(int j=0; j<mainActivity.RevisitGroupList.size(); j++) {
            if(     (   (mainActivity.RevisitGroupList.get(j).ReadyForLoanGroupMemberCount > 0) ||
                        (mainActivity.RevisitGroupList.get(j).ZeroNewLoanAmtGroupMemberCount > 0) ) &&
                    (mainActivity.RevisitGroupList.get(j).GroupID > 0) &&
                    (       (mainActivity.RevisitGroupList.get(j).ReadyForLoanGroupMemberCount +
                            mainActivity.RevisitGroupList.get(j).OngoingLoanGroupMemberCount +
                            mainActivity.RevisitGroupList.get(j).LMFGroupMemberCount) >= 3) &&
                    (mainActivity.RevisitGroupList.get(j).RevisitGroupStatus == clsRevisitGroup.REVISIT_GROUP_STATUS_SCHEDULED) ) {
                ReGroupList.add(mainActivity.RevisitGroupList.get(j));
                String strTemp =    "C-" + String.valueOf(mainActivity.RevisitGroupList.get(j).CenterID) + ":" +
                        "G-" + String.valueOf(mainActivity.RevisitGroupList.get(j).GroupID) + ":(" +
                        String.valueOf(mainActivity.RevisitGroupList.get(j).ReadyForLoanGroupMemberCount +
                                        mainActivity.RevisitGroupList.get(j).OngoingLoanGroupMemberCount) + "/" +
                        String.valueOf(mainActivity.RevisitGroupList.get(j).MemberCount) + ")";
                if(String.valueOf(mainActivity.RevisitGroupList.get(j).CenterID).length() >= 17) {
                    strTemp =    "C-" + String.valueOf(mainActivity.RevisitGroupList.get(j).CenterID).substring(0, (String.valueOf(mainActivity.RevisitGroupList.get(j).CenterID).length() - 8)) + ":" +
                            "G-" + String.valueOf(mainActivity.RevisitGroupList.get(j).GroupID).substring(0, (String.valueOf(mainActivity.RevisitGroupList.get(j).GroupID).length() - 8)) + ":(" +
                            String.valueOf(mainActivity.RevisitGroupList.get(j).ReadyForLoanGroupMemberCount +
                                            mainActivity.RevisitGroupList.get(j).OngoingLoanGroupMemberCount) + "/" +
                            String.valueOf(mainActivity.RevisitGroupList.get(j).MemberCount) + ")";
                }
                ReGroupInfoList.add(strTemp);
            }
        }

        ArrayAdapter<String> adapterRevisitGroupInfo = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, ReGroupInfoList);
        adapterRevisitGroupInfo.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner spRevisitGroupInfo = (Spinner) rootView.findViewById(R.id.spinnerRevisitGroupInfo);
        spRevisitGroupInfo.setAdapter(adapterRevisitGroupInfo);
        spRevisitGroupInfo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                originalRevisitGroup = new clsRevisitGroup();
                originalRevisitGroup.InitRevisitGroup();
                ReGroupList.get(position).Copy_RevisitGroup(originalRevisitGroup);
                selectedRevisitGroup = new clsRevisitGroup();
                selectedRevisitGroup.InitRevisitGroup();
                ReGroupList.get(position).Copy_RevisitGroup(selectedRevisitGroup);
                mainActivity.selectedGroupID = selectedRevisitGroup.GroupID;
                mainActivity.selectedCenterID = selectedRevisitGroup.CenterID;
                mainActivity.selectedClusterID = selectedRevisitGroup.ClusterID;
                mainActivity.selectedCommunityID = selectedRevisitGroup.CommunityID;
                TextView tv = (TextView) rootView.findViewById(R.id.textViewCluster);
                tv.setText(selectedRevisitGroup.Cluster);
                tv = (TextView) rootView.findViewById(R.id.textViewCommunity);
                tv.setText(selectedRevisitGroup.Community);
                RefreshGroupMembers();
                RefreshReadyForLoanMembers();

                mainActivity.newGroup.pathDocuments = mainActivity.baseDir + "/data";
                mainActivity.newGroup.InitNewGroup();
                mainActivity.newGroup.BranchNo = mainActivity.LSO_BranchNo;
                mainActivity.newGroup.BranchName = mainActivity.LSO_Branch;
                mainActivity.selectedGroupStatusPosition = mainActivity.GROUP_TYPE_LOAN_INPROCESS;
                mainActivity.SQLite_getGroupMembersInfo();

                mainActivity.newRegroup.pathDocuments = mainActivity.baseDir + "/data";
                mainActivity.newRegroup.InitNewGroup();
                mainActivity.newGroup.Copy_NewGroup(mainActivity.newRegroup);

                SQLite_getNoLoanGroupForCenter(selectedRevisitGroup.CenterID);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spRevisitGroupInfo.setSelection(0);
        spRevisitGroupInfo.setFocusable(true);
        spRevisitGroupInfo.setFocusableInTouchMode(true);

        bt = (Button) rootView.findViewById(R.id.buttonRemoveRegroup);
        bt.setEnabled(false);
        bt = (Button) rootView.findViewById(R.id.buttonAddRegroup);
        bt.setEnabled(false);
        bt = (Button) rootView.findViewById(R.id.buttonReplaceRegroup);
        bt.setEnabled(false);

        bt = (Button) rootView.findViewById(R.id.buttonSaveRegroup);
        bt.setEnabled(true);

        if (pbSync != null) {
            pbSync.setIndeterminate(true);
            pbSync.setVisibility(View.INVISIBLE);
        }

        return rootView;
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
                        case ufin_REGROUP_DIALOG_STATE_NONE:
                        case ufin_REGROUP_DIALOG_STATE_NO_PRINTER:
                        case ufin_REGROUP_DIALOG_STATE_CBCHECK_DOC_UPLOAD:
                        case ufin_REGROUP_DIALOG_STATE_CONFIRM_PRINT_BUTTON:
                            dlgState = ufin_REGROUP_DIALOG_STATE_NONE;
                            break;
                        case ufin_REGROUP_DIALOG_STATE_ACTIVE_LOAN_GROUP:
                            View vPrev = (View) rootView.findViewById(selectedOthergroupMemberPosition);
                            vPrev.setBackgroundColor(Color.WHITE);
                            selectedOthergroupMemberPosition = -1;
                            selectedOthergroupMemberID = -1;
                            dlgState = ufin_REGROUP_DIALOG_STATE_NONE;
                            break;
                    }
//                    dlgState = ufin_REGROUP_DIALOG_STATE_NONE;
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
                        case ufin_DIALOG_STATE_VALIDATE_REGROUP_SCREEN:
                        case ufin_REGROUP_DIALOG_STATE_NO_PRINTER:
                        case ufin_REGROUP_DIALOG_STATE_CBCHECK_DOC_UPLOAD:
                        case ufin_REGROUP_DIALOG_STATE_ACTIVE_LOAN_GROUP:
                            dlgState = ufin_REGROUP_DIALOG_STATE_NONE;
                            break;
                        case ufin_DIALOG_STATE_CONFIRM_REGROUP_SCREEN:
                            dlgState = ufin_REGROUP_DIALOG_STATE_NONE;
                            AsyncCallWS_SaveRegroup taskSaveRegroup = new AsyncCallWS_SaveRegroup();
                            taskSaveRegroup.execute();
                            break;
                        case ufin_REGROUP_DIALOG_STATE_RESUME_PRINTING:
                            flagDisableNextPrint = false;
                            dlgState = ufin_REGROUP_DIALOG_STATE_NONE;
                            break;
                        case ufin_REGROUP_DIALOG_STATE_CONFIRM_PRINT_BUTTON:
                            ConfirmPrint = true;
                            Button bt = (Button) rootView.findViewById(iPrintButton);
                            if(bt != null)
                                bt.performClick();
                            break;
                    }
//                    dlgState = ufin_REGROUP_DIALOG_STATE_NONE;
                }
            });
        }
//        dlgAlert.setCancelable(true);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCloseRegroup:
                mainActivity.InitDesktop();
                break;
            case R.id.buttonSaveRegroup:
                ValidateScreenRegroup();
                if(RegroupProblemsCheck.size() == 0) {
                    CreateAlertDialog(true, true);
                    dlgState = ufin_DIALOG_STATE_CONFIRM_REGROUP_SCREEN;
                    dlgAlert.setMessage(MSG_ufin_REGROUP_CONFIRM_SAVE);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                } else {
                    String strMsg = "";
                    for(int i=0; i<RegroupProblemsCheck.size(); i++) {
                        strMsg = strMsg + RegroupProblemsCheck.get(i) + "\n";
                    }
                    CreateAlertDialog(true, true);
                    dlgState = ufin_DIALOG_STATE_VALIDATE_REGROUP_SCREEN;
                    dlgAlert.setMessage(strMsg);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                }
                break;
            case R.id.buttonAddRegroup:
                if(selectedOthergroupMemberID < 0)
                    break;
                if(mainActivity.RevisitMemberList.get(selectedOthergroupMemberPosition).CreditCheck != 1)
                    break;
//                int iSingles = 0;
//                int iOnRent = 0;
                int iLoanCycleDelta = 0;
                clsRevisitMember addMember = mainActivity.RevisitMemberList.get(selectedOthergroupMemberPosition);
                for(int i=0; i<selectedRevisitGroup.MemberCount; i++) {
                    int memberIndex = selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i);
                    clsRevisitMember member = mainActivity.RevisitMemberList.get(memberIndex);
//                    if(member.Single)
//                        iSingles++;
//                    if(member.OwnHouse == 0)
//                        iOnRent++;
                    if(member.LoanCycle >= addMember.LoanCycle) {
                        if((member.LoanCycle - addMember.LoanCycle) > iLoanCycleDelta) {
                            iLoanCycleDelta = member.LoanCycle - addMember.LoanCycle;
                        }
                    } else {
                        if((addMember.LoanCycle - member.LoanCycle) > iLoanCycleDelta) {
                            iLoanCycleDelta = addMember.LoanCycle - member.LoanCycle;
                        }
                    }
                }
//                if(addMember.Single)
//                    iSingles++;
//                if(addMember.OwnHouse == 0)
//                    iOnRent++;
                if (originalRevisitGroup.GroupID == addMember.LastGroupID) {
                    ShowToastMessage("Member with same LastGroupID added, New Group Formation Rules Not Applied.");

                    selectedRevisitGroup.GroupMemberID.add(mainActivity.RevisitMemberList.get(selectedOthergroupMemberPosition).MemberID);
                    selectedRevisitGroup.GroupMemberRevisitMemberListIndex.add(selectedOthergroupMemberPosition);
                    selectedRevisitGroup.MemberCount++;
                    addMember.CenterID = selectedRevisitGroup.CenterID;
                    addMember.GroupID = selectedRevisitGroup.GroupID;
                    addMember.GroupName = selectedRevisitGroup.GroupName;
                    addMember.Edited = true;
                    mainActivity.RevisitMemberList.set(selectedOthergroupMemberPosition, addMember);
                    RefreshGroupMembers();
                    RefreshReadyForLoanMembers();
//                } else if((iSingles + iOnRent) > 2) {
//                    ShowToastMessage("भाड्याच्या घरात राहणाऱ्या अथवा एकट्या सभासदांची संख्या 2 पेक्षा जास्त आहे.");
                } else {
                    if(addMember.GroupID > 0)
                        selectedRevisitGroup.IncomingMemberGroupID = addMember.GroupID;
                        selectedRevisitGroup.GroupMemberID.add(mainActivity.RevisitMemberList.get(selectedOthergroupMemberPosition).MemberID);
                        selectedRevisitGroup.GroupMemberRevisitMemberListIndex.add(selectedOthergroupMemberPosition);
                        selectedRevisitGroup.MemberCount++;
                        addMember.CenterID = selectedRevisitGroup.CenterID;
                        addMember.GroupID = selectedRevisitGroup.GroupID;
                        addMember.GroupName = selectedRevisitGroup.GroupName;
                        addMember.Edited = true;
                        mainActivity.RevisitMemberList.set(selectedOthergroupMemberPosition, addMember);

                        RefreshGroupMembers();
                        RefreshReadyForLoanMembers();
                }
                break;
            case R.id.buttonRemoveRegroup:
                if(selectedRegroupMemberPosition < 0)
                    break;
                if(selectedOthergroupMemberID > 0)
                    break;
                for (int i = 0; i < selectedRevisitGroup.MemberCount; i++) {
                    if (selectedRevisitGroup.GroupMemberID.get(i) ==
                            mainActivity.RevisitMemberList.get(selectedRegroupMemberPosition).MemberID) {
                        clsRevisitMember removeMember = mainActivity.RevisitMemberList.get(selectedRegroupMemberPosition);
                        removeMember.GroupID = selectedNoLoanGroupID;
                        removeMember.RevisitMemberStatus = clsRevisitMember.REVISIT_MEMBER_STATUS_REVISITED;
                        removeMember.Edited = true;
                        mainActivity.RevisitMemberList.set(selectedRegroupMemberPosition, removeMember);
                        selectedRevisitGroup.GroupMemberID.remove(i);
                        selectedRevisitGroup.GroupMemberRevisitMemberListIndex.remove(i);
//                        selectedRevisitGroup.MemberCreditCheck.remove(i);
                        selectedRevisitGroup.MemberCount--;
                        RefreshGroupMembers();
                        RefreshReadyForLoanMembers();
                        break;
                    }
                }
                break;
            case R.id.buttonReplaceRegroup:
                if(selectedRegroupMemberPosition < 0)
                    break;
                if(selectedOthergroupMemberID < 0)
                    break;
                if (mainActivity.RevisitMemberList.get(selectedOthergroupMemberPosition).CreditCheck != 1)
                    break;
//                iSingles = 0;
//                iOnRent = 0;
                iLoanCycleDelta = 0;
                clsRevisitMember replaceMember = mainActivity.RevisitMemberList.get(selectedOthergroupMemberPosition);
                for(int i=0; i<selectedRevisitGroup.MemberCount; i++) {
                    if(selectedRevisitGroup.GroupMemberID.get(i) !=
                            mainActivity.RevisitMemberList.get(selectedRegroupMemberPosition).MemberID) {
                        int memberIndex = selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i);
                        clsRevisitMember member = mainActivity.RevisitMemberList.get(memberIndex);
//                        if(member.Single)
//                            iSingles++;
//                        if(member.OwnHouse == 0)
//                            iOnRent++;
                        if(member.LoanCycle >= replaceMember.LoanCycle) {
                            if((member.LoanCycle - replaceMember.LoanCycle) > iLoanCycleDelta) {
                                iLoanCycleDelta = member.LoanCycle - replaceMember.LoanCycle;
                            }
                        } else {
                            if((replaceMember.LoanCycle - member.LoanCycle) > iLoanCycleDelta) {
                                iLoanCycleDelta = replaceMember.LoanCycle - member.LoanCycle;
                            }
                        }
                    }
                }
//                if(replaceMember.Single)
//                    iSingles++;
//                if(replaceMember.OwnHouse == 0)
//                    iOnRent++;

                if (originalRevisitGroup.GroupID == replaceMember.LastGroupID) {
                    ShowToastMessage("Member with same LastGroupID added, New Group Formation Rules Not Applied.");
                    //Type-B Group Criteria Not Checked as Member with same LastGroupID added
                    for (int i = 0; i < selectedRevisitGroup.MemberCount; i++) {
                        if (selectedRevisitGroup.GroupMemberID.get(i) ==
                                mainActivity.RevisitMemberList.get(selectedRegroupMemberPosition).MemberID) {
                            clsRevisitMember removeMember = mainActivity.RevisitMemberList.get(selectedRegroupMemberPosition);
                            replaceMember = mainActivity.RevisitMemberList.get(selectedOthergroupMemberPosition);
                            selectedRevisitGroup.GroupMemberID.set(i, mainActivity.RevisitMemberList.get(selectedOthergroupMemberPosition).MemberID);
                            selectedRevisitGroup.GroupMemberRevisitMemberListIndex.set(i, selectedOthergroupMemberPosition);
                            replaceMember.CenterID = selectedRevisitGroup.CenterID;
                            replaceMember.GroupID = selectedRevisitGroup.GroupID;
                            replaceMember.GroupName = selectedRevisitGroup.GroupName;
                            replaceMember.Edited = true;
                            removeMember.GroupID = selectedNoLoanGroupID;
                            removeMember.RevisitMemberStatus = clsRevisitMember.REVISIT_MEMBER_STATUS_REVISITED;
                            removeMember.Edited = true;
                            mainActivity.RevisitMemberList.set(selectedOthergroupMemberPosition, replaceMember);
                            mainActivity.RevisitMemberList.set(selectedRegroupMemberPosition, removeMember);
                            RefreshGroupMembers();
                            RefreshReadyForLoanMembers();
                            break;
                        }
                    }
//                } else if((iSingles + iOnRent) > 2) {
//                    ShowToastMessage("भाड्याच्या घरात राहणाऱ्या अथवा एकट्या सभासदांची संख्या 2 पेक्षा जास्त आहे.");
                } else {
                    if(replaceMember.GroupID > 0)
                        selectedRevisitGroup.IncomingMemberGroupID = replaceMember.GroupID;
                    for (int i = 0; i < selectedRevisitGroup.MemberCount; i++) {
                        if (selectedRevisitGroup.GroupMemberID.get(i) ==
                                mainActivity.RevisitMemberList.get(selectedRegroupMemberPosition).MemberID) {
                            clsRevisitMember removeMember = mainActivity.RevisitMemberList.get(selectedRegroupMemberPosition);
                            replaceMember = mainActivity.RevisitMemberList.get(selectedOthergroupMemberPosition);
//bmp 28-Aug-26
//                            selectedRevisitGroup.GroupMemberID.add(mainActivity.RevisitMemberList.get(selectedOthergroupMemberPosition).MemberID);
                            selectedRevisitGroup.GroupMemberID.set(i, mainActivity.RevisitMemberList.get(selectedOthergroupMemberPosition).MemberID);
//                            selectedRevisitGroup.GroupMemberRevisitMemberListIndex.add(selectedOthergroupMemberPosition);
                            selectedRevisitGroup.GroupMemberRevisitMemberListIndex.set(i, selectedOthergroupMemberPosition);
//bmp 28-Aug-26
                            replaceMember.CenterID = selectedRevisitGroup.CenterID;
                            replaceMember.GroupID = selectedRevisitGroup.GroupID;
                            replaceMember.GroupName = selectedRevisitGroup.GroupName;
                            replaceMember.Edited = true;
                            removeMember.GroupID = selectedNoLoanGroupID;
                            removeMember.RevisitMemberStatus = clsRevisitMember.REVISIT_MEMBER_STATUS_REVISITED;
                            removeMember.Edited = true;
                            mainActivity.RevisitMemberList.set(selectedOthergroupMemberPosition, replaceMember);
                            mainActivity.RevisitMemberList.set(selectedRegroupMemberPosition, removeMember);
                            RefreshGroupMembers();
                            RefreshReadyForLoanMembers();
                            break;
                        }
                    }
                }
                break;
        }
    }
    public void RefreshGroupMembers() {
        ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarRegroup);
        if (pbSync != null) {
            pbSync.setIndeterminate(true);
            pbSync.setVisibility(View.VISIBLE);
        }
        TableLayout tblGroupMembers = (TableLayout) rootView.findViewById(R.id.tblLayoutGroupMembers);
        tblGroupMembers.removeAllViews();
        TextView tvref = (TextView) rootView.findViewById(R.id.textViewCenterGroup);
        for(int i=0; i<mainActivity.RevisitMemberList.size(); i++) {
            iIDindex = (2000 * i);
            if(mainActivity.RevisitMemberList.get(i).GroupID != mainActivity.selectedGroupID)
                continue;
            LinearLayout llv = new LinearLayout(mainActivity.getBaseContext());
            llv.setOrientation(LinearLayout.VERTICAL);
            llv.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0F));
            LinearLayout llh1 = new LinearLayout(mainActivity.getBaseContext());
            llh1.setOrientation(LinearLayout.HORIZONTAL);
            llh1.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0F));
            LinearLayout llh2 = new LinearLayout(mainActivity.getBaseContext());
            llh2.setOrientation(LinearLayout.HORIZONTAL);
            llh2.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0F));
            LinearLayout llh3 = new LinearLayout(mainActivity.getBaseContext());
            llh3.setOrientation(LinearLayout.HORIZONTAL);
            llh3.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0F));
            LinearLayout llh4 = new LinearLayout(mainActivity.getBaseContext());
            llh4.setOrientation(LinearLayout.HORIZONTAL);
            llh4.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0F));

            TextView tvListType = new TextView(mainActivity.getBaseContext());
            tvListType.setTextSize(24);
            tvListType.setTextColor(tvref.getTextColors());
            tvListType.setTypeface(Typeface.DEFAULT_BOLD);
            tvListType.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvListType.setGravity(Gravity.CENTER_VERTICAL);
            tvListType.setPadding(0, 0, 10, 0);

            TextView tvLastLoanStatus = new TextView(mainActivity.getBaseContext());
            tvLastLoanStatus.setTextSize(24);
            tvLastLoanStatus.setTextColor(tvref.getTextColors());
            tvLastLoanStatus.setTypeface(Typeface.DEFAULT_BOLD);
            tvLastLoanStatus.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvLastLoanStatus.setGravity(Gravity.CENTER_VERTICAL);
            tvLastLoanStatus.setPadding(0, 0, 10, 0);

            TextView tvMemberName = new TextView(mainActivity.getBaseContext());
            tvMemberName.setTextSize(26);
            tvMemberName.setTextColor(tvref.getTextColors());
            tvMemberName.setTypeface(Typeface.DEFAULT_BOLD);
            tvMemberName.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1f));
            tvMemberName.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

            CheckBox cbSingle = new CheckBox(mainActivity.getBaseContext());
            cbSingle.setText("एकाकी महिला");
            cbSingle.setTextSize(26);
            cbSingle.setTextColor(tvref.getTextColors());
            cbSingle.setTypeface(Typeface.DEFAULT_BOLD);
            cbSingle.setButtonTintList(ColorStateList.valueOf(Color.RED));
            cbSingle.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            cbSingle.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            cbSingle.setGravity(Gravity.CENTER_VERTICAL);
            cbSingle.setId(1+iIDindex);
            cbSingle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int iTemp = v.getId();
                    iTemp = (iTemp - 1)/2000;
                    System.out.println("cbSingle: " + String.valueOf(iTemp));
                    mainActivity.RevisitMemberList.get(iTemp).Single = ((CheckBox) v).isChecked();
                }
            });

            CheckBox cbOnRent = new CheckBox(mainActivity.getBaseContext());
            cbOnRent.setText("भाड्याचे घर");
            cbOnRent.setTextSize(26);
            cbOnRent.setTextColor(tvref.getTextColors());
            cbOnRent.setTypeface(Typeface.DEFAULT_BOLD);
            cbOnRent.setButtonTintList(ColorStateList.valueOf(Color.RED));
            cbOnRent.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            cbOnRent.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            cbOnRent.setGravity(Gravity.CENTER_VERTICAL);
            cbOnRent.setId(2+iIDindex);
            cbOnRent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int iTemp = v.getId();
                    iTemp = (iTemp - 2)/2000;
                    System.out.println("cbOnRent: " + String.valueOf(iTemp));
                    if(((CheckBox) v).isChecked())
                        mainActivity.RevisitMemberList.get(iTemp).OwnHouse = 0;
                    else
                        mainActivity.RevisitMemberList.get(iTemp).OwnHouse = 1;
                }
            });
            cbOnRent.setPadding(10, 0, 0, 0);

            TextView tvContactNumber = new TextView(mainActivity.getBaseContext());
            tvContactNumber.setTextSize(26);
            tvContactNumber.setTextColor(tvref.getTextColors());
            tvContactNumber.setTypeface(Typeface.DEFAULT_BOLD);
            tvContactNumber.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvContactNumber.setGravity(Gravity.CENTER_VERTICAL);
            tvContactNumber.setPadding(10, 0, 10, 0);

            TextView tvSavingAccNo = new TextView(mainActivity.getBaseContext());
            tvSavingAccNo.setTextSize(26);
            tvSavingAccNo.setTextColor(tvref.getTextColors());
            tvSavingAccNo.setTypeface(Typeface.DEFAULT_BOLD);
            tvSavingAccNo.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvSavingAccNo.setGravity(Gravity.CENTER_VERTICAL);

            TextView tvLastLoanAmount = new TextView(mainActivity.getBaseContext());
            tvLastLoanAmount.setTextSize(26);
            tvLastLoanAmount.setTextColor(tvref.getTextColors());
            tvLastLoanAmount.setTypeface(Typeface.DEFAULT_BOLD);
            tvLastLoanAmount.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvLastLoanAmount.setGravity(Gravity.CENTER_VERTICAL);

            TextView tvNewLoanAmount = new TextView(mainActivity.getBaseContext());
            tvNewLoanAmount.setTextSize(26);
            tvNewLoanAmount.setTextColor(tvref.getTextColors());
            tvNewLoanAmount.setTypeface(Typeface.DEFAULT_BOLD);
            tvNewLoanAmount.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvNewLoanAmount.setGravity(Gravity.CENTER_VERTICAL);

            TextView tvLoanCycle = new TextView(mainActivity.getBaseContext());
            tvLoanCycle.setTextSize(26);
            tvLoanCycle.setTextColor(tvref.getTextColors());
            tvLoanCycle.setTypeface(Typeface.DEFAULT_BOLD);
            tvLoanCycle.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvLoanCycle.setGravity(Gravity.CENTER_VERTICAL);

            TextView tvHomeLoanAmount = new TextView(mainActivity.getBaseContext());
            tvHomeLoanAmount.setTextSize(26);
            tvHomeLoanAmount.setTextColor(tvref.getTextColors());
            tvHomeLoanAmount.setTypeface(Typeface.DEFAULT_BOLD);
            tvHomeLoanAmount.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvHomeLoanAmount.setGravity(Gravity.CENTER_VERTICAL);

            TextView tvCreditCheck = new TextView(mainActivity.getBaseContext());
            tvCreditCheck.setTextSize(24);
            tvCreditCheck.setTextColor(tvref.getTextColors());
            tvCreditCheck.setTypeface(Typeface.DEFAULT_BOLD);
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, // Width
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f);
            layoutParams1.gravity = Gravity.CENTER_VERTICAL;
            tvCreditCheck.setLayoutParams(layoutParams1);
            tvCreditCheck.setGravity(Gravity.CENTER_VERTICAL);
            tvCreditCheck.setId(3+iIDindex);
            tvCreditCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int iTemp = view.getId();
                    iTemp = (iTemp - 3)/2000;
                    clsRevisitMember thismember = mainActivity.RevisitMemberList.get(iTemp);
                    if(thismember.CreditCheck == 0) {
                        if(!thismember.strRegroupProof.isEmpty()) {
                            File proofFile = new File(strFilepath + thismember.strRegroupProof);
                            if (proofFile.exists()) {
                                //Add CB Check Loan Repaid proof Document Entry
                                clsMemberDocRcvd newDocRcvd = new clsMemberDocRcvd();
                                newDocRcvd.Init_MemberDocRcvd();
                                newDocRcvd.MemberDocumentsID = clsDocumentIDs.MEMBER_DOC_ID_CREDIT_BUREAU_ENQUIRY_NIL;
                                newDocRcvd.MemberID = thismember.MemberID;
                                newDocRcvd.DocumentNo = "0";
                                newDocRcvd.IsActive = 1;
                                Calendar cal = Calendar.getInstance();
                                while (LastTimeInMillis > cal.getTimeInMillis()) {
                                    cal = Calendar.getInstance();
                                }
                                LastTimeInMillis = cal.getTimeInMillis();
                                LastTimeInMillis += 1000;
                                newDocRcvd.MemberDocuemntsRcvdID = mainActivity.SQLite_saveMemberDocRcvd(newDocRcvd, LastTimeInMillis, true);

                                //Add CB Check Loan Repaid proof Document Photo Entry
                                clsDocumentPhoto newDocPhoto = new clsDocumentPhoto();
                                newDocPhoto.Init_DocumentPhoto();
                                newDocPhoto.MemberDocuemntsRcvdID = newDocRcvd.MemberDocuemntsRcvdID;
                                newDocPhoto.GroupID = 0;
                                newDocPhoto.MemberDocumentsID = clsDocumentIDs.MEMBER_DOC_ID_CREDIT_BUREAU_ENQUIRY_NIL;

                                cal = Calendar.getInstance();
                                while (LastTimeInMillis > cal.getTimeInMillis()) {
                                    cal = Calendar.getInstance();
                                }
                                LastTimeInMillis = cal.getTimeInMillis();
                                LastTimeInMillis += 1000;
                                newDocPhoto.DocumentPhotoID = mainActivity.SQLite_saveDocumentPhoto(newDocPhoto, LastTimeInMillis, true);
                                String strMemberFilename = String.valueOf(newDocPhoto.DocumentPhotoID) + ".jpg";
                                try {
                                    InputStream in = null;
                                    OutputStream outmemberdata = null;
                                    OutputStream outmemberupload = null;
                                    byte[] buffer = new byte[1024];
                                    File memberdataFile = new File(mainActivity.baseDir + "/data/" + strMemberFilename);
                                    File memberuploadFile = new File(
                                            mainActivity.baseDir + "/upload/" +
                                            String.valueOf(thismember.MemberID) + "_" +
                                            strMemberFilename
                                    );
                                    in = new FileInputStream(proofFile);
                                    outmemberdata = new FileOutputStream(memberdataFile);
                                    outmemberupload = new FileOutputStream(memberuploadFile);
                                    int read;
                                    while ((read = in.read(buffer)) != -1) {
                                        outmemberdata.write(buffer, 0, read);
                                        outmemberupload.write(buffer, 0, read);
                                    }
                                    in.close();
                                    in = null;
                                    outmemberdata.flush();
                                    outmemberdata.close();
                                    outmemberdata = null;
                                    outmemberupload.flush();
                                    outmemberupload.close();
                                    outmemberupload = null;

                                    mainActivity.SyncMemberCBDocUpload.add(strMemberFilename);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                proofFile.delete();

                                CreateAlertDialog(true, false);
                                dlgState = ufin_REGROUP_DIALOG_STATE_CBCHECK_DOC_UPLOAD;
                                dlgAlert.setMessage(MSG_ufin_CBCHECK_DOC_UPLOAD);
                                AlertDialog dialog = dlgAlert.create();
                                dialog.show();
                                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                                textView.setTextSize(30);
                                textView.setTextColor(Color.RED);
                            }
                        }
                    }
                }
            });

            ArrayAdapter<String> adapterVisitDetails = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                    R.layout.simple_spinner_item, mainActivity.VisitDetailsInMarathiMaster);
            if(mainActivity.RevisitMemberList.get(i).HomeLoanAmount > 0) {
                adapterVisitDetails = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                        R.layout.simple_spinner_item, mainActivity.HomeLoanOfferVisitDetailsInMarathiMaster);
            }
            TextView tvVisitDetails = new TextView(mainActivity.getBaseContext());
            tvVisitDetails.setTextSize(24);
            tvVisitDetails.setTextColor(tvref.getTextColors());
            tvVisitDetails.setTypeface(Typeface.DEFAULT_BOLD);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, // Width
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    0.6f);
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            tvVisitDetails.setLayoutParams(layoutParams);
            tvVisitDetails.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

            clsRevisitMember member = mainActivity.RevisitMemberList.get(i);
            tvListType.setText(member.ListType);
            if(member.ListType.toLowerCase().contains("yellow"))
                tvListType.setBackgroundColor(Color.YELLOW);
            else if(member.ListType.toLowerCase().contains("pink"))
                tvListType.setBackgroundColor(Color.argb(255, 255, 192, 203));    //Pink
            else if(member.ListType.toLowerCase().contains("pista"))
                tvListType.setBackgroundColor(Color.argb(255, 147, 197, 114));
            else if(member.ListType.toLowerCase().contains("blue"))
                tvListType.setBackgroundColor(Color.BLUE);
            else if(member.ListType.toLowerCase().contains("white"))
                tvListType.setBackgroundColor(Color.WHITE);
            else if(member.ListType.toLowerCase().contains("cream"))
                tvListType.setBackgroundColor(Color.argb(255, 255, 253, 208));    //Pink
            else {
                tvListType.setBackgroundColor(Color.GRAY);
            }

            String strLastLoanStatus = "लोन चेक:बाकी";
            tvLastLoanStatus.setBackgroundColor(Color.argb(255, 255, 191, 0));
            if(member.EnquiryDate != null) {
                Calendar cal = Calendar.getInstance();
                Date date1 = cal.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String strToday = sdf.format(date1);// all done
                if(member.EnquiryDate.startsWith(strToday)) {
                    if(member.LastLoanStatus.startsWith("Active")) {
                        strLastLoanStatus = "लोन:Active";
                        tvLastLoanStatus.setBackgroundColor(Color.CYAN);
                    } else if(member.LastLoanStatus.startsWith("Repaid")) {
                        strLastLoanStatus = "लोन:Repaid";
                        tvLastLoanStatus.setBackgroundColor(Color.GREEN);
                    } else if(member.LastLoanStatus.startsWith("Write-off")) {
                        strLastLoanStatus = "लोन:Write-off";
                        tvLastLoanStatus.setBackgroundColor(Color.RED);
                    }
                } else {
                    mainActivity.GetMemberLoanStatus(member.MemberID);
                }
            }
            tvLastLoanStatus.setText(strLastLoanStatus);

            tvMemberName.setText(member.MemberName);

            ImageButton imgbtPhoto = new ImageButton(mainActivity.getBaseContext());
            imgbtPhoto.setImageResource(R.drawable.ic_menu_camera);
            imgbtPhoto.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    1f));
            imgbtPhoto.setId(4+iIDindex);
            imgbtPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IDRequest = view.getId();
                    IDRequest = (IDRequest - 4)/2000;
                    strFilepath = mainActivity.baseDir + "/data/";
                    boolean flagRegroupProof = false;
                    clsRevisitMember thismember = mainActivity.RevisitMemberList.get(IDRequest);
                    if (    (thismember.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) ||
                            (thismember.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) ||
                            (thismember.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED))
                    {
                        if(thismember.CreditCheck == 0) {
                            //Other loan repaid proof
                            flagRegroupProof = true;
                        }
                    } else if(thismember.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING) {
                        //Group Guarantor Undertaking
                        flagRegroupProof = true;
                    }
                    if(flagRegroupProof) {
                        strFilename = String.valueOf(thismember.MemberID) + "-" +
                                "RegroupProof" + "-" + String.valueOf(mainActivity.OrgID) + ".jpg";
                        File outFile = new File(strFilepath + strFilename);
                        if(!thismember.strRegroupProof.isEmpty()) {
                            File oldFile = new File(strFilepath + thismember.strRegroupProof);
                            if(oldFile.exists()) {
                                oldFile.delete();
                            }
                        }
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", outFile);
                        cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outuri);
                        startActivityIntent.launch(cameraIntent);
                    }
                }
            });
            ImageButton imgbtPreview = new ImageButton(mainActivity.getBaseContext());
            imgbtPreview.setImageResource(R.drawable.ic_menu_gallery);
            imgbtPreview.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    1f));
            imgbtPreview.setId(5+iIDindex);
            imgbtPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IDRequest = view.getId();
                    IDRequest = (IDRequest - 5)/2000;
                    strFilepath = mainActivity.baseDir + "/data/";
                    strFilename = String.valueOf(mainActivity.RevisitMemberList.get(IDRequest).MemberID) + "-" +
                            "RegroupProof" + "-" + String.valueOf(mainActivity.OrgID) + ".jpg";
                    File outFile = new File(strFilepath + strFilename);
                    if(outFile.exists()) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        Uri outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", outFile);
                        intent.setDataAndType(outuri, "image/*");
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(intent);
                    }
                }
            });
            LinearLayout llhProof = new LinearLayout(mainActivity.getBaseContext());
            llhProof.setOrientation(LinearLayout.HORIZONTAL);
            llhProof.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    1.8f));
            llhProof.addView(imgbtPhoto);
            llhProof.addView(imgbtPreview);

            Button btPreviewCBCheck = new Button(mainActivity.getBaseContext());
            btPreviewCBCheck.setText("PREVIEW");
            btPreviewCBCheck.setTextSize(16);
            btPreviewCBCheck.setTypeface(Typeface.DEFAULT_BOLD);
            btPreviewCBCheck.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            btPreviewCBCheck.setGravity(Gravity.CENTER_VERTICAL);
            btPreviewCBCheck.setId(6+iIDindex);
            btPreviewCBCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int iTemp = view.getId();
                    iTemp = (iTemp - 6)/2000;
                    if(mainActivity.RevisitMemberList.get(iTemp).CreditCheck == -1) {
                        return;
                    }
                    String StrPDFCBCheckFileName = generateCBCheckPDF(iTemp);
                    if(StrPDFCBCheckFileName.isEmpty()) {
                        return;
                    }
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    File pdfFile = new File(StrPDFCBCheckFileName);
                    Uri pdfuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", pdfFile);
                    intent.setDataAndType(pdfuri, "application/pdf");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                }
            });
            Button btPrintCBCheck = new Button(mainActivity.getBaseContext());
            btPrintCBCheck.setText("PRINT");
            btPrintCBCheck.setTextSize(16);
            btPrintCBCheck.setTypeface(Typeface.DEFAULT_BOLD);
            btPrintCBCheck.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            btPrintCBCheck.setId(7+iIDindex);
            btPrintCBCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int iTemp = view.getId();
                    iTemp = (iTemp - 7)/2000;
                    String StrPDFCBCheckFileName
                            =   mainActivity.baseDir + "/data/cbcheck/" +
                            String.valueOf(mainActivity.RevisitMemberList.get(iTemp).MemberID) + "/" +
                            String.valueOf(mainActivity.RevisitMemberList.get(iTemp).MemberID) + ".pdf";
                    if(StrPDFCBCheckFileName.isEmpty()) {
                        return;
                    }
                    if(flagDisableNextPrint)
                        return;
                    if(!ConfirmPrint) {
                        iPrintButton = view.getId();
                        CreateAlertDialog(true, true);
                        dlgState = ufin_REGROUP_DIALOG_STATE_CONFIRM_PRINT_BUTTON;
                        dlgAlert.setMessage(MSG_ufin_REGROUP_CONFIRM_PRINT_BUTTON);
                        AlertDialog dialog = dlgAlert.create();
                        dialog.show();
                        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                        textView.setTextSize(30);
                        textView.setTextColor(Color.RED);
                        return;
                    }
                    iPrintButton = -1;
                    ConfirmPrint = false;
                    mainActivity.disconnectPrinter();
                    boolean ReadyToPrint = mainActivity.checkPrinter();
                    if(!ReadyToPrint) {
                        CreateAlertDialog(false, true);
                        dlgState = ufin_REGROUP_DIALOG_STATE_NO_PRINTER;
                        dlgAlert.setMessage(MSG_ufin_REGROUP_NO_PRINTER);
                        AlertDialog dialog = dlgAlert.create();
                        dialog.show();
                        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                        textView.setTextSize(30);
                        textView.setTextColor(Color.RED);
                        return;
                    }
                    mainActivity.printPDF(StrPDFCBCheckFileName);
                    DisableNextPrintTillUserConfirm();
                }
            });
            LinearLayout llhCBCeck = new LinearLayout(mainActivity.getBaseContext());
            llhCBCeck.setOrientation(LinearLayout.HORIZONTAL);
            llhCBCeck.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0F));
            llhCBCeck.addView(btPreviewCBCheck);
            llhCBCeck.addView(btPrintCBCheck);
            if(member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_NOT_REQUIRED) {
                btPreviewCBCheck.setEnabled(false);
                btPrintCBCheck.setEnabled(false);
            }

            if( (member.MaritalStatusID == 1) || (member.MaritalStatusID == 7) )
                member.Single = false;
            else
                member.Single = true;
            cbSingle.setChecked(member.Single);
            if(member.OwnHouse == 0)
                cbOnRent.setChecked(true);
            else
                cbOnRent.setChecked(false);
            tvContactNumber.setText("M:" + member.ContactNumber);
            tvSavingAccNo.setText("AccNo:" + member.SavingAccNo);
            tvLastLoanAmount.setText("कर्ज:जुने रु. " + String.valueOf(member.LastLoanAmount) + "/-");

            int iNextLoanOfferAmount = member.PrevMaxLoanAmount;
            if( (member.LastLoanAmount == 0) || (member.PrevMaxLoanAmount == 0) ) {
                //New Member or Blacklisted Member
            } else {
                if (member.ListType.equalsIgnoreCase("Yellow-I") ||
                        member.ListType.equalsIgnoreCase("Pista-I") ||
                        member.ListType.equalsIgnoreCase("Pink-I")) {
                    iNextLoanOfferAmount = iNextLoanOfferAmount + clsRevisitMember.REVISIT_MEMBER_INCENTIVE_GRADE_A_GROUP;
                } else {
                    iNextLoanOfferAmount = iNextLoanOfferAmount + member.incentiveGenuineBTypeGroup;
                }
                iNextLoanOfferAmount = iNextLoanOfferAmount + member.incentiveRegularLoanRepay;
                iNextLoanOfferAmount = iNextLoanOfferAmount + member.incentiveNotWithdrawingSavingsShares;
                iNextLoanOfferAmount = iNextLoanOfferAmount + member.incentiveRegularRDpay;
                iNextLoanOfferAmount = iNextLoanOfferAmount + member.incentiveRegularADHPpay;
                iNextLoanOfferAmount = iNextLoanOfferAmount + member.incentiveAGMAttendance;
//bmp 11-Jan-24 To match Printout at Step-2 GroupForm Submit
                if(iNextLoanOfferAmount < 55000) {
                    iNextLoanOfferAmount = 55000;
                } else {
                    iNextLoanOfferAmount = iNextLoanOfferAmount + clsRevisitMember.REVISIT_MEMBER_INCENTIVE_FULL_ATTENDANCE;
                }
            }
//bmp 11-Jan-24
            tvNewLoanAmount.setText("नवीन रु. " + String.valueOf(iNextLoanOfferAmount) + "/-");

            tvLoanCycle.setText("सायकल:" + String.valueOf(member.LoanCycle) + " ");
            tvHomeLoanAmount.setText("गृहकर्ज रु. " + String.valueOf(member.HomeLoanAmount) + "/-");

            if(member.CreditCheck == -1) {
                if(     (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) &&
                        (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING) &&
                        (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) &&
                        (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED) ) {
                    tvCreditCheck.setBackgroundColor(Color.argb(255, 255, 191, 0));
                    tvCreditCheck.setText("क्रेडिट चेक:-");
                } else {
                    tvCreditCheck.setBackgroundColor(Color.argb(255, 255, 191, 0));
                    tvCreditCheck.setText("क्रेडिट चेक: बाकी");
                }
            } else if(member.CreditCheck == 0) {
                tvCreditCheck.setBackgroundColor(Color.RED);
                tvCreditCheck.setText("क्रेडिट चेक:रिजेक्ट");
            } else if(member.CreditCheck == 1) {
                tvCreditCheck.setBackgroundColor(Color.GREEN);
                tvCreditCheck.setText("क्रेडिट चेक: ओके");
            }

            if(     (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_NOT_REQUIRED) ||
                    (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED_GROUP_NOT_READY) ||
                    (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_ANOTHER_LOAN_CREDIT_REJECT) ||
                    (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_MOVEDTO_NEWPLACE_PERMANENT_BLACKLIST) ||
                    (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_NO_RATIONCARD) ) {
                if(selectedRevisitGroup.OutgoingMemberID1 == 0)
                    selectedRevisitGroup.OutgoingMemberID1 = member.MemberID;
                else if(    (selectedRevisitGroup.OutgoingMemberID1 != member.MemberID) &&
                        (selectedRevisitGroup.OutgoingMemberID2 == 0) )
                    selectedRevisitGroup.OutgoingMemberID2 = member.MemberID;
            }
            if(member.HomeLoanAmount > 0) {
                for(int k=0; k<mainActivity.HomeLoanOfferVisitDetailsIDMaster.size(); k++) {
                    if(member.VisitDetailsID == mainActivity.HomeLoanOfferVisitDetailsIDMaster.get(k)) {
                        tvVisitDetails.setText(adapterVisitDetails.getItem(k));
                        break;
                    }
                }
            } else {
                for(int k=0; k<mainActivity.VisitDetailsIDMaster.size(); k++) {
                    if(member.VisitDetailsID == mainActivity.VisitDetailsIDMaster.get(k)) {
                        tvVisitDetails.setText(adapterVisitDetails.getItem(k));
                        break;
                    }
                }
            }

            llh1.addView(tvListType);
            llh1.addView(tvLastLoanStatus);
            llh1.addView(tvMemberName);
            llh1.addView(llhProof);

            llh2.addView(cbSingle);
            llh2.addView(cbOnRent);
            llh2.addView(tvContactNumber);
            llh2.addView(tvSavingAccNo);

            llh3.addView(tvLastLoanAmount);
            llh3.addView(tvNewLoanAmount);
            llh3.addView(tvLoanCycle);
            llh3.addView(tvHomeLoanAmount);

            llh4.addView(tvCreditCheck);
            llh4.addView(llhCBCeck);
            llh4.addView(tvVisitDetails);

            if(member.PrevMaxLoanAmount > 0) {
                if ((member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_NOT_REQUIRED) ||
                        (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED_GROUP_NOT_READY) ||
                        (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_ANOTHER_LOAN_CREDIT_REJECT) ||
                        (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_MOVEDTO_NEWPLACE_PERMANENT_BLACKLIST) ||
                        (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_NO_RATIONCARD)) {
                        if (selectedRevisitGroup.OutgoingMemberID1 == 0) {
                            selectedRevisitGroup.OutgoingMemberID1 = member.MemberID;
                        } else if ((selectedRevisitGroup.OutgoingMemberID1 != member.MemberID) &&
                                (selectedRevisitGroup.OutgoingMemberID2 == 0)) {
                            selectedRevisitGroup.OutgoingMemberID2 = member.MemberID;
                        }
                } else {
                    if (selectedRevisitGroup.OutgoingMemberID1 == member.MemberID) {
                        selectedRevisitGroup.OutgoingMemberID1 = 0;
                    } else if (selectedRevisitGroup.OutgoingMemberID2 == member.MemberID) {
                        selectedRevisitGroup.OutgoingMemberID2 = 0;
                    }
                }
            }

            llv.addView(llh1);
            llv.addView(llh2);
            llv.addView(llh3);
            llv.addView(llh4);

            TableRow tblRow = new TableRow(mainActivity.getBaseContext());
            tblRow.setBackground(mainActivity.getDrawable(R.drawable.customborder));
            tblRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tblRow.addView(llv);
            tblRow.setId(8+iIDindex);
            tblRow.setEnabled(true);
            tblRow.setClickable(true);
            tblRow.setOnClickListener(groupmemberRegroupOnClickListener);

            tblGroupMembers.addView(tblRow);
        }

        int iCount = tblGroupMembers.getChildCount();
        EditText et = (EditText) rootView.findViewById(R.id.editTextGroupMemberCount);
        et.setText(String.valueOf(iCount));

        selectedRegroupMemberPosition = -1;
        selectedRegroupMemberID = -1;
        Button bt0 = (Button) rootView.findViewById(R.id.buttonRemoveRegroup);
        bt0.setEnabled(false);
        Button bt1 = (Button) rootView.findViewById(R.id.buttonAddRegroup);
        bt1.setEnabled(false);
        Button bt2 = (Button) rootView.findViewById(R.id.buttonReplaceRegroup);
        bt2.setEnabled(false);

        if (pbSync != null) {
            pbSync.setIndeterminate(true);
            pbSync.setVisibility(View.INVISIBLE);
        }
    }
    public void RefreshReadyForLoanMembers() {
        ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarRegroup);
        if (pbSync != null) {
            pbSync.setIndeterminate(true);
            pbSync.setVisibility(View.VISIBLE);
        }
        TableLayout tblReadyForLoanMembers = (TableLayout) rootView.findViewById(R.id.tblLayoutReadyForLoanMembers);
        tblReadyForLoanMembers.removeAllViews();
        TextView tvref = (TextView) rootView.findViewById(R.id.textViewCenterGroup);
        int OtherGroupReadyForLoanMemberCount = 0;
        int Prefer2Index = 0;
        for(int i=0; i<mainActivity.RevisitMemberList.size(); i++) {
            iIDindex = (2000 * i);
            if(mainActivity.RevisitMemberList.get(i).GroupID == mainActivity.selectedGroupID)
                continue;
            if(mainActivity.RevisitMemberList.get(i).ClusterID != mainActivity.selectedClusterID)
                continue;
            if(mainActivity.RevisitMemberList.get(i).CommunityID != mainActivity.selectedCommunityID)
                continue;
            if(     (mainActivity.RevisitMemberList.get(i).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) &&
                    (mainActivity.RevisitMemberList.get(i).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) &&
                    (mainActivity.RevisitMemberList.get(i).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED) )
                continue;
            if(mainActivity.RevisitMemberList.get(i).RevisitMemberStatus >= clsRevisitMember.REVISIT_MEMBER_STATUS_READYGROUP)
                continue;
            //This is to avoid showing members from other groups 'awaiting regrouping'
            if (mainActivity.RevisitMemberList.get(i).GroupID != 0) {
                boolean bFlag = false;
                for (int k = 0; k < mainActivity.RevisitGroupList.size(); k++) {
                    if ((mainActivity.RevisitGroupList.get(k).ReadyForLoanGroupMemberCount >= 3) &&
                            (mainActivity.RevisitGroupList.get(k).RevisitGroupStatus == clsRevisitGroup.REVISIT_GROUP_STATUS_SCHEDULED)) {
                    if(mainActivity.RevisitMemberList.get(i).GroupID == mainActivity.RevisitGroupList.get(k).GroupID)
                        bFlag = true;
                    }
                }
                if (bFlag)
                    continue;
            }

            LinearLayout llv = new LinearLayout(mainActivity.getBaseContext());
            llv.setOrientation(LinearLayout.VERTICAL);
            llv.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0F));
            LinearLayout llh1 = new LinearLayout(mainActivity.getBaseContext());
            llh1.setOrientation(LinearLayout.HORIZONTAL);
            llh1.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0F));
            LinearLayout llh2 = new LinearLayout(mainActivity.getBaseContext());
            llh2.setOrientation(LinearLayout.HORIZONTAL);
            llh2.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0F));
            LinearLayout llh3 = new LinearLayout(mainActivity.getBaseContext());
            llh3.setOrientation(LinearLayout.HORIZONTAL);
            llh3.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0F));
            LinearLayout llh4 = new LinearLayout(mainActivity.getBaseContext());
            llh4.setOrientation(LinearLayout.HORIZONTAL);
            llh4.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0F));

            TextView tvListType = new TextView(mainActivity.getBaseContext());
            tvListType.setTextSize(24);
            tvListType.setTextColor(tvref.getTextColors());
            tvListType.setTypeface(Typeface.DEFAULT_BOLD);
            tvListType.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvListType.setGravity(Gravity.CENTER_VERTICAL);
            tvListType.setPadding(0, 0, 10, 0);

            TextView tvLastLoanStatus = new TextView(mainActivity.getBaseContext());
            tvLastLoanStatus.setTextSize(24);
            tvLastLoanStatus.setTextColor(tvref.getTextColors());
            tvLastLoanStatus.setTypeface(Typeface.DEFAULT_BOLD);
            tvLastLoanStatus.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvLastLoanStatus.setGravity(Gravity.CENTER_VERTICAL);
            tvLastLoanStatus.setPadding(0, 0, 10, 0);

            TextView tvMemberName = new TextView(mainActivity.getBaseContext());
            tvMemberName.setTextSize(26);
            tvMemberName.setTextColor(tvref.getTextColors());
            tvMemberName.setTypeface(Typeface.DEFAULT_BOLD);
            tvMemberName.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1f));
            tvMemberName.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
//bmp 13-Apr-26
            ImageButton imgbtPhoto = new ImageButton(mainActivity.getBaseContext());
            imgbtPhoto.setImageResource(R.drawable.ic_menu_camera);
            imgbtPhoto.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    1f));
            imgbtPhoto.setId(9+iIDindex);
            imgbtPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IDRequest = view.getId();
                    IDRequest = (IDRequest - 9)/2000;
                    strFilepath = mainActivity.baseDir + "/data/";
                    boolean flagRegroupProof = false;
                    clsRevisitMember thismember = mainActivity.RevisitMemberList.get(IDRequest);
                    if(thismember.CreditCheck == 0) {
                        //Other loan repaid proof
                        strFilename = String.valueOf(thismember.MemberID) + "-" +
                                "RegroupProof" + "-" + String.valueOf(mainActivity.OrgID) + ".jpg";
                        File outFile = new File(strFilepath + strFilename);
                        if(!thismember.strRegroupProof.isEmpty()) {
                            File oldFile = new File(strFilepath + thismember.strRegroupProof);
                            if(oldFile.exists()) {
                                oldFile.delete();
                            }
                        }
                        /*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", outFile);
                        cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outuri);
                        startActivityIntent.launch(cameraIntent);*/

                        //RK
                        Intent cameraIntent = new Intent();
                        cameraIntent.setClassName(
                                "com.example.cameraapp",
                                "com.example.cameraapp.DocumentScannerActivity"
                        );
                        startActivityIntent.launch(cameraIntent);
                    }
                }
            });
            ImageButton imgbtPreview = new ImageButton(mainActivity.getBaseContext());
            imgbtPreview.setImageResource(R.drawable.ic_menu_gallery);
            imgbtPreview.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    1f));
            imgbtPreview.setId(10+iIDindex);
            imgbtPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IDRequest = view.getId();
                    IDRequest = (IDRequest - 10)/2000;
                    strFilepath = mainActivity.baseDir + "/data/";
                    strFilename = String.valueOf(mainActivity.RevisitMemberList.get(IDRequest).MemberID) + "-" +
                            "RegroupProof" + "-" + String.valueOf(mainActivity.OrgID) + ".jpg";
                    File outFile = new File(strFilepath + strFilename);
                    if(outFile.exists()) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        Uri outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", outFile);
                        intent.setDataAndType(outuri, "image/*");
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(intent);
                    }
                }
            });
            LinearLayout llhProof = new LinearLayout(mainActivity.getBaseContext());
            llhProof.setOrientation(LinearLayout.HORIZONTAL);
            llhProof.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    1.8f));
            llhProof.addView(imgbtPhoto);
            llhProof.addView(imgbtPreview);
//bmp 13-Apr-26
            CheckBox cbSingle = new CheckBox(mainActivity.getBaseContext());
            cbSingle.setText("एकाकी महिला");
            cbSingle.setTextSize(26);
            cbSingle.setTextColor(tvref.getTextColors());
            cbSingle.setTypeface(Typeface.DEFAULT_BOLD);
            cbSingle.setButtonTintList(ColorStateList.valueOf(Color.RED));
            cbSingle.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            cbSingle.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            cbSingle.setGravity(Gravity.CENTER_VERTICAL);
            cbSingle.setId(11+iIDindex);
            cbSingle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int iTemp = v.getId();
                    iTemp = (iTemp - 11)/2000;
                    System.out.println("cbSingle: " + String.valueOf(iTemp));
                    mainActivity.RevisitMemberList.get(iTemp).Single = ((CheckBox) v).isChecked();
                }
            });

            CheckBox cbOnRent = new CheckBox(mainActivity.getBaseContext());
            cbOnRent.setText("भाड्याचे घर");
            cbOnRent.setTextSize(26);
            cbOnRent.setTextColor(tvref.getTextColors());
            cbOnRent.setTypeface(Typeface.DEFAULT_BOLD);
            cbOnRent.setButtonTintList(ColorStateList.valueOf(Color.RED));
            cbOnRent.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            cbOnRent.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            cbOnRent.setGravity(Gravity.CENTER_VERTICAL);
            cbOnRent.setId(12+iIDindex);
            cbOnRent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int iTemp = v.getId();
                    iTemp = (iTemp - 12)/2000;
                    System.out.println("cbOnRent: " + String.valueOf(iTemp));
                    if(((CheckBox) v).isChecked())
                        mainActivity.RevisitMemberList.get(iTemp).OwnHouse = 0;
                    else
                        mainActivity.RevisitMemberList.get(iTemp).OwnHouse = 1;
                }
            });
            cbOnRent.setPadding(10, 0, 0, 0);

            TextView tvContactNumber = new TextView(mainActivity.getBaseContext());
            tvContactNumber.setTextSize(26);
            tvContactNumber.setTextColor(tvref.getTextColors());
            tvContactNumber.setTypeface(Typeface.DEFAULT_BOLD);
            tvContactNumber.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvContactNumber.setGravity(Gravity.CENTER_VERTICAL);
            tvContactNumber.setPadding(10, 0, 10, 0);

            TextView tvSavingAccNo = new TextView(mainActivity.getBaseContext());
            tvSavingAccNo.setTextSize(26);
            tvSavingAccNo.setTextColor(tvref.getTextColors());
            tvSavingAccNo.setTypeface(Typeface.DEFAULT_BOLD);
            tvSavingAccNo.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvSavingAccNo.setGravity(Gravity.CENTER_VERTICAL);

            TextView tvLastLoanAmount = new TextView(mainActivity.getBaseContext());
            tvLastLoanAmount.setTextSize(26);
            tvLastLoanAmount.setTextColor(tvref.getTextColors());
            tvLastLoanAmount.setTypeface(Typeface.DEFAULT_BOLD);
            tvLastLoanAmount.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvLastLoanAmount.setGravity(Gravity.CENTER_VERTICAL);

            TextView tvNewLoanAmount = new TextView(mainActivity.getBaseContext());
            tvNewLoanAmount.setTextSize(26);
            tvNewLoanAmount.setTextColor(tvref.getTextColors());
            tvNewLoanAmount.setTypeface(Typeface.DEFAULT_BOLD);
            tvNewLoanAmount.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvNewLoanAmount.setGravity(Gravity.CENTER_VERTICAL);

            TextView tvLoanCycle = new TextView(mainActivity.getBaseContext());
            tvLoanCycle.setTextSize(26);
            tvLoanCycle.setTextColor(tvref.getTextColors());
            tvLoanCycle.setTypeface(Typeface.DEFAULT_BOLD);
            tvLoanCycle.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvLoanCycle.setGravity(Gravity.CENTER_VERTICAL);

            TextView tvHomeLoanAmount = new TextView(mainActivity.getBaseContext());
            tvHomeLoanAmount.setTextSize(26);
            tvHomeLoanAmount.setTextColor(tvref.getTextColors());
            tvHomeLoanAmount.setTypeface(Typeface.DEFAULT_BOLD);
            tvHomeLoanAmount.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvHomeLoanAmount.setGravity(Gravity.CENTER_VERTICAL);

            TextView tvCreditCheck = new TextView(mainActivity.getBaseContext());
            tvCreditCheck.setTextSize(24);
            tvCreditCheck.setTextColor(tvref.getTextColors());
            tvCreditCheck.setTypeface(Typeface.DEFAULT_BOLD);
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, // Width
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f);
            layoutParams1.gravity = Gravity.CENTER_VERTICAL;
            tvCreditCheck.setLayoutParams(layoutParams1);
            tvCreditCheck.setGravity(Gravity.CENTER_VERTICAL);
            tvCreditCheck.setId(13+iIDindex);
            tvCreditCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int iTemp = view.getId();
                    iTemp = (iTemp - 13)/2000;
                    clsRevisitMember thismember = mainActivity.RevisitMemberList.get(iTemp);
                    if(thismember.CreditCheck == 0) {
                        if(!thismember.strRegroupProof.isEmpty()) {
                            File proofFile = new File(strFilepath + thismember.strRegroupProof);
                            if (proofFile.exists()) {
                                //Add CB Check Loan Repaid proof Document Entry
                                clsMemberDocRcvd newDocRcvd = new clsMemberDocRcvd();
                                newDocRcvd.Init_MemberDocRcvd();
                                newDocRcvd.MemberDocumentsID = clsDocumentIDs.MEMBER_DOC_ID_CREDIT_BUREAU_ENQUIRY_NIL;
                                newDocRcvd.MemberID = thismember.MemberID;
                                newDocRcvd.DocumentNo = "0";
                                newDocRcvd.IsActive = 1;
                                Calendar cal = Calendar.getInstance();
                                while (LastTimeInMillis > cal.getTimeInMillis()) {
                                    cal = Calendar.getInstance();
                                }
                                LastTimeInMillis = cal.getTimeInMillis();
                                LastTimeInMillis += 1000;
                                newDocRcvd.MemberDocuemntsRcvdID = mainActivity.SQLite_saveMemberDocRcvd(newDocRcvd, LastTimeInMillis, true);

                                //Add CB Check Loan Repaid proof Document Photo Entry
                                clsDocumentPhoto newDocPhoto = new clsDocumentPhoto();
                                newDocPhoto.Init_DocumentPhoto();
                                newDocPhoto.MemberDocuemntsRcvdID = newDocRcvd.MemberDocuemntsRcvdID;
                                newDocPhoto.GroupID = 0;
                                newDocPhoto.MemberDocumentsID = clsDocumentIDs.MEMBER_DOC_ID_CREDIT_BUREAU_ENQUIRY_NIL;

                                cal = Calendar.getInstance();
                                while (LastTimeInMillis > cal.getTimeInMillis()) {
                                    cal = Calendar.getInstance();
                                }
                                LastTimeInMillis = cal.getTimeInMillis();
                                LastTimeInMillis += 1000;
                                newDocPhoto.DocumentPhotoID = mainActivity.SQLite_saveDocumentPhoto(newDocPhoto, LastTimeInMillis, true);
                                String strMemberFilename = String.valueOf(newDocPhoto.DocumentPhotoID) + ".jpg";
                                try {
                                    InputStream in = null;
                                    OutputStream outmemberdata = null;
                                    OutputStream outmemberupload = null;
                                    byte[] buffer = new byte[1024];
                                    File memberdataFile = new File(mainActivity.baseDir + "/data/" + strMemberFilename);
//                                    File memberuploadFile = new File(mainActivity.baseDir + "/upload/" + strMemberFilename);
                                    File memberuploadFile = new File(
                                            mainActivity.baseDir + "/upload/" +
                                                    String.valueOf(thismember.MemberID) + "_" +
                                                    strMemberFilename
                                    );
                                    in = new FileInputStream(proofFile);
                                    outmemberdata = new FileOutputStream(memberdataFile);
                                    outmemberupload = new FileOutputStream(memberuploadFile);
                                    int read;
                                    while ((read = in.read(buffer)) != -1) {
                                        outmemberdata.write(buffer, 0, read);
                                        outmemberupload.write(buffer, 0, read);
                                    }
                                    in.close();
                                    in = null;
                                    outmemberdata.flush();
                                    outmemberdata.close();
                                    outmemberdata = null;
                                    outmemberupload.flush();
                                    outmemberupload.close();
                                    outmemberupload = null;

                                    mainActivity.SyncMemberCBDocUpload.add(strMemberFilename);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                proofFile.delete();

                                CreateAlertDialog(true, false);
                                dlgState = ufin_REGROUP_DIALOG_STATE_CBCHECK_DOC_UPLOAD;
                                dlgAlert.setMessage(MSG_ufin_CBCHECK_DOC_UPLOAD);
                                AlertDialog dialog = dlgAlert.create();
                                dialog.show();
                                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                                textView.setTextSize(30);
                                textView.setTextColor(Color.RED);
                            }
                        }
                    }
                }
            });

            TextView tvCenterGroup = new TextView(mainActivity.getBaseContext());
            tvCenterGroup.setTextSize(24);
            tvCenterGroup.setTextColor(tvref.getTextColors());
            tvCenterGroup.setTypeface(Typeface.DEFAULT_BOLD);
            tvCenterGroup.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            tvCenterGroup.setGravity(Gravity.CENTER_VERTICAL);
            tvCenterGroup.setPadding(10, 0, 0, 0);

            TextView tvGroupType = new TextView(mainActivity.getBaseContext());
            tvGroupType.setTextSize(24);
            tvGroupType.setTextColor(tvref.getTextColors());
            tvGroupType.setTypeface(Typeface.DEFAULT_BOLD);
            tvGroupType.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            tvGroupType.setGravity(Gravity.CENTER_VERTICAL);
            tvGroupType.setPadding(10, 0, 10, 0);
            tvGroupType.setText("NoLoan");
            tvGroupType.setBackgroundColor(Color.CYAN);
            tvGroupType.setVisibility(View.INVISIBLE);

            TextView tvLastCenterLastGroup = new TextView(mainActivity.getBaseContext());
            tvLastCenterLastGroup.setTextSize(26);
            tvLastCenterLastGroup.setTextColor(tvref.getTextColors());
            tvLastCenterLastGroup.setTypeface(Typeface.DEFAULT_BOLD);
            tvLastCenterLastGroup.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            tvLastCenterLastGroup.setGravity(Gravity.CENTER_VERTICAL);
            tvLastCenterLastGroup.setPadding(10, 0, 0, 0);

            clsRevisitMember member = mainActivity.RevisitMemberList.get(i);
            tvListType.setText(member.ListType);
            if(member.ListType.toLowerCase().contains("yellow"))
                tvListType.setBackgroundColor(Color.YELLOW);
            else if(member.ListType.toLowerCase().contains("pink"))
                tvListType.setBackgroundColor(Color.argb(255, 255, 192, 203));    //Pink
            else if(member.ListType.toLowerCase().contains("pista"))
                tvListType.setBackgroundColor(Color.argb(255, 147, 197, 114));
            else if(member.ListType.toLowerCase().contains("blue"))
                tvListType.setBackgroundColor(Color.BLUE);
            else if(member.ListType.toLowerCase().contains("white"))
                tvListType.setBackgroundColor(Color.WHITE);
            else if(member.ListType.toLowerCase().contains("cream"))
                tvListType.setBackgroundColor(Color.argb(255, 255, 253, 208));    //Pink
            else {
                tvListType.setBackgroundColor(Color.GRAY);
            }

            String strLastLoanStatus = "लोन चेक:बाकी";
            tvLastLoanStatus.setBackgroundColor(Color.argb(255, 255, 191, 0));
            if(member.EnquiryDate != null) {
                Calendar cal = Calendar.getInstance();
                Date date1 = cal.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String strToday = sdf.format(date1);// all done
                if(member.EnquiryDate.startsWith(strToday)) {
                    if(member.LastLoanStatus.startsWith("Active")) {
                        strLastLoanStatus = "लोन:Active";
                        tvLastLoanStatus.setBackgroundColor(Color.CYAN);
                    } else if(member.LastLoanStatus.startsWith("Repaid")) {
                        strLastLoanStatus = "लोन:Repaid";
                        tvLastLoanStatus.setBackgroundColor(Color.GREEN);
                    } else if(member.LastLoanStatus.startsWith("Write-off")) {
                        strLastLoanStatus = "लोन:Write-off";
                        tvLastLoanStatus.setBackgroundColor(Color.RED);
                    }
                } else {
                    mainActivity.GetMemberLoanStatus(member.MemberID);
                }
            }
            tvLastLoanStatus.setText(strLastLoanStatus);

            tvMemberName.setText(member.MemberName);
            if( (member.MaritalStatusID == 1) || (member.MaritalStatusID == 7) )
                member.Single = false;
            else
                member.Single = true;
            cbSingle.setChecked(member.Single);
            if(member.OwnHouse == 0)
                cbOnRent.setChecked(true);
            else
                cbOnRent.setChecked(false);
            tvContactNumber.setText("M:" + member.ContactNumber);
            tvSavingAccNo.setText("AccNo:" + member.SavingAccNo);
            tvLastLoanAmount.setText("कर्ज:जुने रु. " + String.valueOf(member.LastLoanAmount) + "/-");

            int iNextLoanOfferAmount = member.PrevMaxLoanAmount;
            if( (member.LastLoanAmount == 0) || (member.PrevMaxLoanAmount == 0) ) {
                //New Member or Blacklisted Member
            } else {
                if (member.ListType.equalsIgnoreCase("Yellow-I") ||
                        member.ListType.equalsIgnoreCase("Pista-I") ||
                        member.ListType.equalsIgnoreCase("Pink-I")) {
                    iNextLoanOfferAmount = iNextLoanOfferAmount + clsRevisitMember.REVISIT_MEMBER_INCENTIVE_GRADE_A_GROUP;
                } else {
                    iNextLoanOfferAmount = iNextLoanOfferAmount + member.incentiveGenuineBTypeGroup;
                }
                iNextLoanOfferAmount = iNextLoanOfferAmount + member.incentiveRegularLoanRepay;
                iNextLoanOfferAmount = iNextLoanOfferAmount + member.incentiveNotWithdrawingSavingsShares;
                iNextLoanOfferAmount = iNextLoanOfferAmount + member.incentiveRegularRDpay;
                iNextLoanOfferAmount = iNextLoanOfferAmount + member.incentiveRegularADHPpay;
                iNextLoanOfferAmount = iNextLoanOfferAmount + member.incentiveAGMAttendance;
//bmp 11-Jan-24 To match Printout at Step-2 GroupForm Submit
                if(iNextLoanOfferAmount < 55000) {
                    iNextLoanOfferAmount = 55000;
                } else {
                    iNextLoanOfferAmount = iNextLoanOfferAmount + clsRevisitMember.REVISIT_MEMBER_INCENTIVE_FULL_ATTENDANCE;
                }
            }
//bmp 11-Jan-24
            tvNewLoanAmount.setText("नवीन रु. " + String.valueOf(iNextLoanOfferAmount) + "/-");

            tvLoanCycle.setText("सायकल:" + String.valueOf(member.LoanCycle) + " ");
            tvHomeLoanAmount.setText("गृहकर्ज रु. " + String.valueOf(member.HomeLoanAmount) + "/-");

            if(member.CreditCheck == -1) {
                tvCreditCheck.setBackgroundColor(Color.argb(255, 255, 191, 0));
                tvCreditCheck.setText("क्रेडिट चेक: बाकी");
            } else if(member.CreditCheck == 0) {
                tvCreditCheck.setBackgroundColor(Color.RED);
                tvCreditCheck.setText("क्रेडिट चेक:रिजेक्ट");
            } else if(member.CreditCheck == 1) {
                tvCreditCheck.setBackgroundColor(Color.GREEN);
                tvCreditCheck.setText("क्रेडिट चेक: ओके");
            }

            Button btPreviewCBCheck = new Button(mainActivity.getBaseContext());
            btPreviewCBCheck.setText("PREVIEW");
            btPreviewCBCheck.setTextSize(16);
            btPreviewCBCheck.setTypeface(Typeface.DEFAULT_BOLD);
            btPreviewCBCheck.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            btPreviewCBCheck.setId(14+iIDindex);
            btPreviewCBCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int iTemp = view.getId();
                    iTemp = (iTemp - 14)/2000;
                    if(mainActivity.RevisitMemberList.get(iTemp).CreditCheck == -1) {
                        return;
                    }
                    String StrPDFCBCheckFileName = generateCBCheckPDF(iTemp);
                    if(StrPDFCBCheckFileName.isEmpty()) {
                        return;
                    }
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    File pdfFile = new File(StrPDFCBCheckFileName);
                    Uri pdfuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", pdfFile);
                    intent.setDataAndType(pdfuri, "application/pdf");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                }
            });
            Button btPrintCBCheck = new Button(mainActivity.getBaseContext());
            btPrintCBCheck.setText("PRINT");
            btPrintCBCheck.setTextSize(16);
            btPrintCBCheck.setTypeface(Typeface.DEFAULT_BOLD);
            btPrintCBCheck.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT));
            btPrintCBCheck.setId(15+iIDindex);
            btPrintCBCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int iTemp = view.getId();
                    iTemp = (iTemp - 15)/2000;
                    String StrPDFCBCheckFileName
                            =   mainActivity.baseDir + "/data/cbcheck/" +
                            String.valueOf(mainActivity.RevisitMemberList.get(iTemp).MemberID) + "/" +
                            String.valueOf(mainActivity.RevisitMemberList.get(iTemp).MemberID) + ".pdf";
                    if(StrPDFCBCheckFileName.isEmpty()) {
                        return;
                    }
                    if(flagDisableNextPrint)
                        return;
                    if(!ConfirmPrint) {
                        iPrintButton = view.getId();
                        CreateAlertDialog(true, true);
                        dlgState = ufin_REGROUP_DIALOG_STATE_CONFIRM_PRINT_BUTTON;
                        dlgAlert.setMessage(MSG_ufin_REGROUP_CONFIRM_PRINT_BUTTON);
                        AlertDialog dialog = dlgAlert.create();
                        dialog.show();
                        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                        textView.setTextSize(30);
                        textView.setTextColor(Color.RED);
                        return;
                    }
                    iPrintButton = -1;
                    ConfirmPrint = false;
                    mainActivity.disconnectPrinter();
                    boolean ReadyToPrint = mainActivity.checkPrinter();
                    if(!ReadyToPrint) {
                        CreateAlertDialog(false, true);
                        dlgState = ufin_REGROUP_DIALOG_STATE_NO_PRINTER;
                        dlgAlert.setMessage(MSG_ufin_REGROUP_NO_PRINTER);
                        AlertDialog dialog = dlgAlert.create();
                        dialog.show();
                        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                        textView.setTextSize(30);
                        textView.setTextColor(Color.RED);
                        return;
                    }
                    mainActivity.printPDF(StrPDFCBCheckFileName);
                    DisableNextPrintTillUserConfirm();
                }
            });
            LinearLayout llhCBCeck = new LinearLayout(mainActivity.getBaseContext());
            llhCBCeck.setOrientation(LinearLayout.HORIZONTAL);
            llhCBCeck.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0F));
            llhCBCeck.addView(btPreviewCBCheck);
            llhCBCeck.addView(btPrintCBCheck);
            if(member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_NOT_REQUIRED) {
                btPreviewCBCheck.setEnabled(false);
//                btPreviewCBCheck.setVisibility(View.INVISIBLE);
                btPrintCBCheck.setEnabled(false);
//                btPrintCBCheck.setVisibility(View.INVISIBLE);
            }

            tvCenterGroup.setText("G-" + String.valueOf(member.GroupID));
            if(String.valueOf(member.GroupID).length() >= 17) {
                tvCenterGroup.setText("G-" + String.valueOf(member.GroupID).substring(0, (String.valueOf(member.GroupID).length() - 8)));
            }
            if(member.GroupName.contains("No Loan")) {
                tvGroupType.setVisibility(View.VISIBLE);
            } else {
                tvGroupType.setVisibility(View.INVISIBLE);
            }

            tvLastCenterLastGroup.setText("LC-" + String.valueOf(member.LastCenterID) + ":LG-" + String.valueOf(member.LastGroupID));

            OtherGroupReadyForLoanMemberCount = OtherGroupReadyForLoanMemberCount + 1;

            llh1.addView(tvListType);
            llh1.addView(tvLastLoanStatus);
            llh1.addView(tvMemberName);
            llh1.addView(llhProof);
            llh2.addView(cbSingle);
            llh2.addView(cbOnRent);
            llh2.addView(tvContactNumber);
            llh2.addView(tvSavingAccNo);
            llh3.addView(tvLastLoanAmount);
            llh3.addView(tvNewLoanAmount);
            llh3.addView(tvLoanCycle);
            llh3.addView(tvHomeLoanAmount);
            llh4.addView(tvCreditCheck);
            llh4.addView(llhCBCeck);
            llh4.addView(tvCenterGroup);
            llh4.addView(tvLastCenterLastGroup);
            llh4.addView(tvGroupType);

            llv.addView(llh1);
            llv.addView(llh2);
            llv.addView(llh3);
            llv.addView(llh4);


            TableRow tblRow = new TableRow(mainActivity.getBaseContext());
            tblRow.setBackground(mainActivity.getDrawable(R.drawable.customborder));
            tblRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tblRow.addView(llv);
            tblRow.setId(16+iIDindex);
            tblRow.setEnabled(true);
            tblRow.setClickable(true);
            tblRow.setOnClickListener(othergroupmemberRegroupOnClickListener);

            if (mainActivity.RevisitMemberList.get(i).LastGroupID == mainActivity.selectedGroupID) {
                tblReadyForLoanMembers.addView(tblRow, 0);
                Prefer2Index++;
            } else if (mainActivity.RevisitMemberList.get(i).CenterID == mainActivity.selectedCenterID) {
                tblReadyForLoanMembers.addView(tblRow, Prefer2Index);
            } else {
                tblReadyForLoanMembers.addView(tblRow);
            }
        }

        selectedOthergroupMemberPosition = -1;
        selectedOthergroupMemberID = -1;
        Button bt1 = (Button) rootView.findViewById(R.id.buttonAddRegroup);
        bt1.setEnabled(false);
        Button bt2 = (Button) rootView.findViewById(R.id.buttonReplaceRegroup);
        bt2.setEnabled(false);

        String strTemp = "इतर गटातील कर्ज उत्सुक सभासद" + " (" + String.valueOf(OtherGroupReadyForLoanMemberCount) + ")";
        TextView tv = (TextView) rootView.findViewById(R.id.textViewOtherGroupReadyForLoanMembers);
        tv.setText(strTemp);

        if (pbSync != null) {
            pbSync.setIndeterminate(true);
            pbSync.setVisibility(View.INVISIBLE);
        }
    }
//bmp 10-Mar-26
    /*ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Add same code that you want to add in onActivityResult method
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        File outFile = new File(strFilepath + strFilename);
                        if(outFile.exists()) {
                            if(outFile.length() >= MAX_PHOTO_FILESIZE) {
//bmp 28-Apr-26
//                                ShowdlgAlertonActivityResult("Incorrect Image file size, Adjust Camera Setting and Try Again!");
                                String currentPhotoPath = strFilepath + strFilename;
                                mainActivity.AdjustImageFileSize(currentPhotoPath);
                                mainActivity.RevisitMemberList.get(IDRequest).strRegroupProof = strFilename;
//bmp 28-Apr-26
                            } else {
                                mainActivity.RevisitMemberList.get(IDRequest).strRegroupProof = strFilename;
//                                if(mainActivity.RevisitMemberList.get(IDRequest).CreditCheck == 0) {
//                                    mainActivity.RevisitMemberList.get(IDRequest).CreditCheck = -1;
//                                }
                            }
                        }
                    } else if(result.getResultCode() == Activity.RESULT_CANCELED){
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
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            ShowdlgAlertonActivityResult("No result received from Camera App!");
                            return;
                        }
                        // =================================================
                        // Get image URI returned by Camera App
                        // =================================================

                        Uri imageUri = null;
                        if (
                                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                        ) {
                            imageUri = data.getParcelableExtra(Intent.EXTRA_STREAM, Uri.class);
                        } else {
                            imageUri = data.getParcelableExtra(Intent.EXTRA_STREAM);}
                        if (imageUri == null) {imageUri = data.getData();}
                        if (imageUri == null) {
                            ShowdlgAlertonActivityResult("No image received from Camera App!");
                            return;
                        }
                        File outFile = new File(strFilepath + strFilename);
                        try {
                            InputStream inputStream = mainActivity.getContentResolver().openInputStream(imageUri);
                            if (inputStream == null) {ShowdlgAlertonActivityResult("Unable to read captured image!");
                                return;
                            }
                            FileOutputStream outputStream = new FileOutputStream(outFile);
                            byte[] buffer = new byte[8192];
                            int length;
                            while ((length = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, length);}
                            outputStream.flush();
                            outputStream.close();
                            inputStream.close();
                            if (outFile.exists()) {
                                if (outFile.length() >= MAX_PHOTO_FILESIZE) {
                                    String currentPhotoPath = strFilepath + strFilename;
                                    mainActivity.AdjustImageFileSize(currentPhotoPath);
                                    mainActivity.RevisitMemberList.get(IDRequest).strRegroupProof = strFilename;
                                } else {
                                    mainActivity.RevisitMemberList.get(IDRequest).strRegroupProof = strFilename;
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ShowdlgAlertonActivityResult("Unable to save captured image!\n" + e.getMessage());
                            return;
                        }
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED
                    ) {
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
//bmp 10-Mar-26
    private View.OnClickListener groupmemberRegroupOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            if(selectedRegroupMemberPosition >= 0) {
//bmp 31-Aug-26
//                View vPrev = (View) rootView.findViewById(selectedRegroupMemberPosition);
                int iPrev = (selectedRegroupMemberPosition * 2000) + 8;
                View vPrev = (View) rootView.findViewById(iPrev);
//bmp 31-Aug-26
                vPrev.setBackgroundColor(Color.WHITE);
            }
            int iTemp = v.getId();
            iTemp = (iTemp - 8)/2000;
            if(selectedRegroupMemberPosition == iTemp) {
                selectedRegroupMemberPosition = -1;
                selectedRegroupMemberID = -1;
            } else {
//bmp 27-Mar-26
                clsRevisitMember member = mainActivity.RevisitMemberList.get(iTemp);
                boolean flagInvalidLastLoanStatus = false;
                if(member.EnquiryDate == null) {
                    flagInvalidLastLoanStatus = true;
                    ShowToastMessage("सभासदाचा लोन स्टेटस चेक बाकी आहे: " + member.MemberName);
                } else {
                    Calendar cal = Calendar.getInstance();
                    Date date1 = cal.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    String strToday = sdf.format(date1);// all done
                    if(member.EnquiryDate.startsWith(strToday)) {
                        if(member.LastLoanStatus.startsWith("Active")) {
//                            flagInvalidLastLoanStatus = true;
                            ShowToastMessage("सभासदाचा लोन स्टेटस Active आहे: " + member.MemberName);
                        }
                    } else {
                        flagInvalidLastLoanStatus = true;
                        ShowToastMessage("सभासदाचा लोन स्टेटस चेक बाकी आहे: " + member.MemberName);
                    }
                }
                if(!flagInvalidLastLoanStatus) {
//bmp 27-Mar-26
                    boolean flagOngoingLoanMemberInGroup = false;
                    for (int i = 0; i < selectedRevisitGroup.MemberCount; i++) {
                        if (selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i) < 0)
                            continue;
                        if (mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i)).VisitDetailsID
                                == clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING) {
                            flagOngoingLoanMemberInGroup = true;
                            ShowToastMessage("गटातील सभासदाचे कर्ज चालू आहे.");
                            break;
                        }
                    }
                    boolean flagZeroNewLoanAmtMemberInGroup = false;
                    for (int i = 0; i < selectedRevisitGroup.MemberCount; i++) {
                        if (selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i) < 0)
                            continue;
                        if (mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i)).PrevMaxLoanAmount == 0) {
                            flagZeroNewLoanAmtMemberInGroup = true;
                            ShowToastMessage("गटातील सभासदाची नवीन कर्ज रक्कम 0 आहे.");
                            break;
                        }
                    }
                    if (!flagOngoingLoanMemberInGroup) {
//                    clsRevisitMember member = mainActivity.RevisitMemberList.get(iTemp);
                        if (member.PrevMaxLoanAmount > 0) {
                            if ((member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_NOT_REQUIRED) ||
                                    (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED_GROUP_NOT_READY) ||
                                    (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_ANOTHER_LOAN_CREDIT_REJECT) ||
                                    (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_MOVEDTO_NEWPLACE_PERMANENT_BLACKLIST) ||
                                    (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_NO_RATIONCARD)) {
                                boolean Flag = false;
                                if (member.MemberID == selectedRevisitGroup.OutgoingMemberID1) {
                                    Flag = true;
                                    v.setBackgroundColor(Color.CYAN);
                                    selectedRegroupMemberPosition = iTemp;
                                    selectedRegroupMemberID = member.MemberID;
                                } else if (member.MemberID == selectedRevisitGroup.OutgoingMemberID2) {
                                    Flag = true;
                                    v.setBackgroundColor(Color.CYAN);
                                    selectedRegroupMemberPosition = iTemp;
                                    selectedRegroupMemberID = member.MemberID;
                                }
                                if (!Flag) {
                                    ShowToastMessage("सभासदाच्या हमीपत्राचा फोटो बाकी आहे.");
                                }
                            } else if ((member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) &&
                                    (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) &&
                                    (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED) &&
                                    (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING)) {
                                v.setBackgroundColor(Color.CYAN);
                                selectedRegroupMemberPosition = iTemp;
                                selectedRegroupMemberID = member.MemberID;
//bmp 07-Apr-26
//                            } else if((member.CreditCheck == 0) && (!member.LastLoanStatus.startsWith("Active"))) {
                            } else if(member.CreditCheck == 0) {
                                //Member wants Next Loan, Last Loan Not Active BUT CB Reject
                                boolean Flag = false;
                                if (member.MemberID == selectedRevisitGroup.OutgoingMemberID1) {
                                    Flag = true;
                                    v.setBackgroundColor(Color.CYAN);
                                    selectedRegroupMemberPosition = iTemp;
                                    selectedRegroupMemberID = member.MemberID;
                                } else if (member.MemberID == selectedRevisitGroup.OutgoingMemberID2) {
                                    Flag = true;
                                    v.setBackgroundColor(Color.CYAN);
                                    selectedRegroupMemberPosition = iTemp;
                                    selectedRegroupMemberID = member.MemberID;
                                }
                                if (!Flag) {
                                    ShowToastMessage("सभासदाच्या हमीपत्राचा फोटो बाकी आहे.");
                                }
//bmp 07-Apr-26
                            }
                        } else if (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING) {
                            v.setBackgroundColor(Color.CYAN);
                            selectedRegroupMemberPosition = iTemp;
                            selectedRegroupMemberID = member.MemberID;
                        }
                    } else {
//                    clsRevisitMember member = mainActivity.RevisitMemberList.get(iTemp);
//                    if (member.PrevMaxLoanAmount == 0) {
                        if ((member.PrevMaxLoanAmount == 0) || (!member.strRegroupProof.isEmpty())) {
                            v.setBackgroundColor(Color.CYAN);
                            selectedRegroupMemberPosition = iTemp;
                            selectedRegroupMemberID = member.MemberID;
                        } else {
                            ShowToastMessage("सभासदाच्या हमीपत्राचा फोटो बाकी आहे.");
                        }
                    }
//bmp 27-Mar-26
                }
//bmp 27-Mar-26
            }

            Button bt0 = (Button) rootView.findViewById(R.id.buttonRemoveRegroup);
            bt0.setEnabled(false);
            Button bt1 = (Button) rootView.findViewById(R.id.buttonAddRegroup);
            bt1.setEnabled(false);
            Button bt2 = (Button) rootView.findViewById(R.id.buttonReplaceRegroup);
            bt2.setEnabled(false);

            if( (selectedRegroupMemberID > 0) && (selectedOthergroupMemberID > 0) ) {
                bt2.setEnabled(true);
            } else if(  (selectedRevisitGroup.MemberCount < 5) &&
                    (selectedRegroupMemberID <= 0) &&
                    (selectedOthergroupMemberID > 0) ) {
                bt1.setEnabled(true);
            } else if(  (selectedRevisitGroup.MemberCount > 3) &&
                    (selectedRegroupMemberID > 0) &&
                    (selectedOthergroupMemberID <= 0) ) {
                bt0.setEnabled(true);
            }
        }
    };
    private View.OnClickListener othergroupmemberRegroupOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            if(selectedOthergroupMemberPosition >= 0) {
//bmp 31-Aug-26
//                View vPrev = (View) rootView.findViewById(selectedOthergroupMemberPosition);
                int iPrev = (selectedOthergroupMemberPosition * 2000) + 16;
                View vPrev = (View) rootView.findViewById(iPrev);
//bmp 31-Aug-26
                vPrev.setBackgroundColor(Color.WHITE);
            }
            int iTemp = v.getId();
            iTemp = (iTemp - 16)/2000;
            if(selectedOthergroupMemberPosition == iTemp) {
                selectedOthergroupMemberPosition = -1;
                selectedOthergroupMemberID = -1;
            } else {
//bmp 27-Mar-26
                clsRevisitMember member = mainActivity.RevisitMemberList.get(iTemp);
                boolean flagInvalidLastLoanStatus = false;
                if(member.EnquiryDate == null) {
                    flagInvalidLastLoanStatus = true;
                    ShowToastMessage("सभासदाचा लोन स्टेटस चेक बाकी आहे: " + member.MemberName);
                } else {
                    Calendar cal = Calendar.getInstance();
                    Date date1 = cal.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    String strToday = sdf.format(date1);// all done
                    if(member.EnquiryDate.startsWith(strToday)) {
                        if(member.LastLoanStatus.startsWith("Active")) {
//                            flagInvalidLastLoanStatus = true;
                            ShowToastMessage("सभासदाचा लोन स्टेटस Active आहे: " + member.MemberName);
                        } else if(member.LastLoanStatus.startsWith("Write-off")) {
                            flagInvalidLastLoanStatus = true;
                            ShowToastMessage("सभासदाचा लोन स्टेटस Write-off आहे: " + member.MemberName);
                        }

                    } else {
                        flagInvalidLastLoanStatus = true;
                        ShowToastMessage("सभासदाचा लोन स्टेटस चेक बाकी आहे: " + member.MemberName);
                    }
                }
                if(!flagInvalidLastLoanStatus) {
//bmp 18-May-26
//bmp 27-Mar-26
//                    boolean flagOngoingLoanMemberInGroup = false;
//                    for (int i = 0; i < 5; i++) {
//                        if (selectedRevisitGroup.GroupMemberRevisitMemberListIndex[i] < 0)
//                            continue;
//
//                        if (mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex[i]).VisitDetailsID
//                                == clsRevisitMember.REVISIT_REMARK_ID_LOAN_NOT_REQUIRED) {
//                            if (!mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex[i]).strRegroupProof.isEmpty()) {
//                                if (selectedRegroupMemberID == mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex[i]).MemberID) {
//                                    break;
//                                }
//                            }
//                        }
//
//                        if (mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex[i]).VisitDetailsID
//                                == clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING) {
//                            flagOngoingLoanMemberInGroup = true;
//                            ShowToastMessage("गटातील सभासदाचे कर्ज चालू आहे.");
//                            break;
//                        }
//                    }
                    boolean flagOngoingLoanMemberInGroup = false;
                    boolean flagNoLoanWithGroupGuarantor = false;
                    for (int i = 0; i < selectedRevisitGroup.MemberCount; i++) {
                        if (selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i) < 0)
                            continue;

                        if (mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i)).VisitDetailsID
                                == clsRevisitMember.REVISIT_REMARK_ID_LOAN_NOT_REQUIRED) {
                            if (!mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i)).strRegroupProof.isEmpty()) {
                                if (selectedRegroupMemberID == mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i)).MemberID) {
                                    flagNoLoanWithGroupGuarantor = true;
                                    break;
                                }
                            }
                        }

                        if (mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i)).VisitDetailsID
                                == clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING) {
                            flagOngoingLoanMemberInGroup = true;
//                            ShowToastMessage("गटातील सभासदाचे कर्ज चालू आहे.");
//                            break;
                        }
                    }
                    if(flagOngoingLoanMemberInGroup) {
                        ShowToastMessage("गटातील सभासदाचे कर्ज चालू आहे.");
                    }
//bmp 18-May-26
                    boolean flagZeroNewLoanAmtMemberInGroup = false;
                    for (int i = 0; i < selectedRevisitGroup.MemberCount; i++) {
                        if (selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i) < 0)
                            continue;
                        if (mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i)).PrevMaxLoanAmount == 0) {
                            flagZeroNewLoanAmtMemberInGroup = true;
                            ShowToastMessage("गटातील सभासदाची नवीन कर्ज रक्कम 0 आहे.");
                            break;
                        }
                    }
//bmp 28-Aug-26
//                    if (!flagOngoingLoanMemberInGroup || flagZeroNewLoanAmtMemberInGroup || flagNoLoanWithGroupGuarantor) {
                    if (    !flagOngoingLoanMemberInGroup ||
                            flagZeroNewLoanAmtMemberInGroup ||
                            flagNoLoanWithGroupGuarantor ||
                            (selectedRevisitGroup.MemberCount < 5) ) {
//bmp 28-Aug-26
                        if (member.CreditCheck == -1) {
                            ShowToastMessage("सभासदाचा क्रेडिट ब्युरो चेक बाकी आहे.");
                        } else if (member.CreditCheck == 0) {
                            ShowToastMessage("सभासदाचा क्रेडिट ब्युरो चेक रिजेक्ट आहे.");
                        } else {
                            v.setBackgroundColor(Color.CYAN);
                            selectedOthergroupMemberPosition = iTemp;
                            selectedOthergroupMemberID = member.MemberID;
//bmp 17-Aug-26
                            boolean flagActiveGroup = false;
                            String strMsg = "गटातील सभासदाचा चालू कर्ज कालावधी बाकी आहे:";
                            for(int i=0; i<mainActivity.RevisitMemberList.size(); i++) {
                                if(     (mainActivity.RevisitMemberList.get(i).GroupID == member.GroupID) &&
                                        (mainActivity.RevisitMemberList.get(i).MemberID != member.MemberID) ) {
//                                    if(mainActivity.RevisitMemberList.get(i).LastLoanStatus.startsWith("Active")) {
                                    Calendar cal = Calendar.getInstance();
                                    long lToday = cal.getTimeInMillis();
                                    String strMaturityDate = mainActivity.RevisitMemberList.get(i).MaturityDate;
                                    strMaturityDate = strMaturityDate.substring(0, 10);
                                    String[] strSplit = strMaturityDate.split("-");
                                    Calendar doe = Calendar.getInstance();
                                    doe.set(Calendar.YEAR, Integer.parseInt(strSplit[0]));
                                    doe.set(Calendar.MONTH, (Integer.parseInt(strSplit[1]) - 1));
                                    doe.set(Calendar.DATE, Integer.parseInt(strSplit[2]));
                                    long ldoMaturity = doe.getTimeInMillis();
                                    if(ldoMaturity > lToday) {
                                        strMsg = strMsg + "\nSavingAccNo:" + mainActivity.RevisitMemberList.get(i).SavingAccNo + "\n" + mainActivity.RevisitMemberList.get(i).MemberName;
                                        flagActiveGroup = true;
                                    }
                                }
                            }
                            if(flagActiveGroup) {
                                CreateAlertDialog(true, true);
                                dlgState = ufin_REGROUP_DIALOG_STATE_ACTIVE_LOAN_GROUP;
                                dlgAlert.setMessage(strMsg);
                                AlertDialog dialog = dlgAlert.create();
                                dialog.show();
                                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                                textView.setTextSize(30);
                                textView.setTextColor(Color.RED);
                            }

//bmp 17-Aug-26
                        }
                    }
//bmp 27-Mar-26
                }
//bmp 27-Mar-26
            }

            Button bt0 = (Button) rootView.findViewById(R.id.buttonRemoveRegroup);
            bt0.setEnabled(false);
            Button bt1 = (Button) rootView.findViewById(R.id.buttonAddRegroup);
            bt1.setEnabled(false);
            Button bt2 = (Button) rootView.findViewById(R.id.buttonReplaceRegroup);
            bt2.setEnabled(false);

            if( (selectedRegroupMemberID > 0) && (selectedOthergroupMemberID > 0) ) {
                bt2.setEnabled(true);
            } else if(  (selectedRevisitGroup.MemberCount < 5) &&
                    (selectedRegroupMemberID <= 0) &&
                    (selectedOthergroupMemberID > 0) ) {
                bt1.setEnabled(true);
            }
        }
    };
    public void ValidateScreenRegroup() {
        RegroupProblemsCheck.clear();

        if(selectedRevisitGroup.MemberCount < 3) {
            RegroupProblemsCheck.add("गटातील सभासद संख्या 3 पेक्षा कमी आहे.");
        }
        if(selectedRevisitGroup.MemberCount > 5) {
            RegroupProblemsCheck.add("गटातील सभासद संख्या 5 पेक्षा जास्त आहे.");
        }

//        int iSingles = 0;
//        int iOnRent = 0;
//        int iBusinessLoan = 0;
//        int iEducationLoan = 0;
//        int iHouseLoan = 0;
//bmp 08-Aug-26
        int iReplacedMemberCount = 0;
//bmp 08-Aug-26
        for(int i=0; i<selectedRevisitGroup.MemberCount; i++) {
            clsRevisitMember member = mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i));
            if(member.EnquiryDate == null) {
                RegroupProblemsCheck.add("सभासदाचा लोन स्टेटस चेक बाकी आहे: " + member.MemberName);
            } else {
                Calendar cal = Calendar.getInstance();
                Date date1 = cal.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String strToday = sdf.format(date1);// all done
                if(member.EnquiryDate.startsWith(strToday)) {
                    if(member.LastLoanStatus.startsWith("Active")) {
                    } else if(member.LastLoanStatus.startsWith("Write-off")) {
                        RegroupProblemsCheck.add("सभासदाचा लोन स्टेटस Write-off आहे: " + member.MemberName);
                    }
                } else {
                    RegroupProblemsCheck.add("सभासदाचा लोन स्टेटस चेक बाकी आहे: " + member.MemberName);
                }
            }
//            if(member.Single)
//                iSingles++;
//            if(member.OwnHouse == 0)
//                iOnRent++;

            if(     (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) ||
                    (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) ||
                    (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED) ) {
                if (member.CreditCheck == -1) {
                    RegroupProblemsCheck.add("सभासदाचा क्रेडिट ब्युरो चेक बाकी आहे: " + member.MemberName);
                } else if (member.CreditCheck == 0) {
                    RegroupProblemsCheck.add("सभासदाचा क्रेडिट ब्युरो चेक रिजेक्ट आहे: " + member.MemberName);
                }
            }
            if(member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING) {
                if (member.CreditCheck == -1) {
                    RegroupProblemsCheck.add("सभासदाचा क्रेडिट ब्युरो चेक बाकी आहे: " + member.MemberName);
                }
            }
//            if(     (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) &&
//                    (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) &&
//                    (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED) &&
//                    (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING) ) {
            if(     (selectedRevisitGroup.GroupID == member.GroupID) &&
                    (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) &&
                    (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) &&
                    (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED) &&
                    (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING) ) {
                RegroupProblemsCheck.add("सभासदाचा पुनर्भेट रिमार्क \"कर्ज पाहिजे\" अथवा \"चालू कर्ज\" नाही: " + member.MemberName);
            }
            if(     (   (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) ||
                        (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) ||
                        (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED) ) &&
                    (member.PrevMaxLoanAmount == 0) ) {
                RegroupProblemsCheck.add("सभासदाला अन्नपूर्णाकडून मिळणारी नवीन कर्ज रक्कम 0 आहे: " + member.MemberName);
            }
//bmp 08-Aug-26
            if(member.GroupID != member.LastGroupID)
                iReplacedMemberCount++;
//bmp 08-Aug-26
        }

//        int iReplacedMemberCount = 0;
//        selectedRevisitGroup.LastCycleGroupContinued = 1;
//        for(int i=0; i<5; i++) {
//            if (originalRevisitGroup.GroupMemberID[i] != selectedRevisitGroup.GroupMemberID[i]) {
//                clsRevisitMember member = mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex[i]);
//                if (originalRevisitGroup.GroupID == member.LastGroupID) {
//                    Toast.makeText(mainActivity.getBaseContext(),"Member with same LastGroupID added, New Group Formation Rules Not Applied.", Toast.LENGTH_SHORT).show();
//                } else {
//                    selectedRevisitGroup.LastCycleGroupContinued = 0;
//                }
//                iReplacedMemberCount++;
//            }
//        }
//        if(selectedRevisitGroup.LastCycleGroupContinued == 0) {
//            if((iSingles + iOnRent) > 2)
//                RegroupProblemsCheck.add("भाड्याच्या घरात राहणाऱ्या अथवा एकट्या सभासदांची संख्या 2 पेक्षा जास्त आहे.");
//        }
//        if(iReplacedMemberCount > 2)
//            RegroupProblemsCheck.add("गटातील नवीन सभासदांची संख्या 2 पेक्षा जास्त आहे.");

//bmp 19-Aug-26
//        int iTemp = selectedRevisitGroup.MemberCount - iReplacedMemberCount;
//        if(iReplacedMemberCount > iTemp)
//            RegroupProblemsCheck.add("गटातील नवीन सभासदांची संख्या जुन्या पेक्षा जास्त आहे.");
        if(String.valueOf(selectedRevisitGroup.GroupID).length() < 17) {
            int iTemp = selectedRevisitGroup.MemberCount - iReplacedMemberCount;
            if (iReplacedMemberCount > iTemp)
                RegroupProblemsCheck.add("गटातील नवीन सभासदांची संख्या जुन्या पेक्षा जास्त आहे.");
        }
//bmp 19-Aug-26

//        ArrayList<Long> alDifferentLastGroupID = new ArrayList<Long>();
//        alDifferentLastGroupID.clear();
//        int iZeroLastGroupIDCount = 0;
//        long lGroupID = mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex[0]).LastGroupID;
//        alDifferentLastGroupID.add(lGroupID);
//        if(lGroupID == 0)
//            iZeroLastGroupIDCount++;
//
//        for(int i=1; i<selectedRevisitGroup.TotalGroupMemberCount; i++) {
//            long lTemp1 = mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex[i]).LastGroupID;
//            if(lTemp1 == 0)
//                iZeroLastGroupIDCount++;
//
//            boolean newFlag = true;
//            for(int j=0; j<alDifferentLastGroupID.size(); j++) {
//                if(lTemp1 == alDifferentLastGroupID.get(j)) {
//                    newFlag = false;
//                    break;
//                }
//            }
//            if(newFlag) {
//                alDifferentLastGroupID.add(lTemp1);
//            }
//        }
//        switch(alDifferentLastGroupID.size()) {
//            case 1:
//                //00000 => Not Allowed
//                //xxxxx => Allowed
//                if(iZeroLastGroupIDCount == 0) {
//                    selectedRevisitGroup.GradeOfGroup = "A";
//                    selectedRevisitGroup.LastCycleGroupContinued = 1;
//                } else {
//                    selectedRevisitGroup.GradeOfGroup = "E";
//                    selectedRevisitGroup.LastCycleGroupContinued = 0;
//                    if(mainActivity.AllowGradeEGroup)
//                        break;
//                    RegroupProblemsCheck.add("आधीच्या एकाच गटातील सभासद संख्या २ पेक्षा कमी आहे.");
//                }
//                break;
//            case 2:
//                selectedRevisitGroup.LastCycleGroupContinued = 0;
//                selectedRevisitGroup.GradeOfGroup = "B";
//                //0000x => Not Allowed
//                //000xx => Allowed "D"
//                //00xxx => Allowed "C"
//                //0xxxx => Allowed "B"
//                //xxxyy => Allowed "B"
//                //xxyyy => Allowed "B"
//                switch (iZeroLastGroupIDCount) {
//                    case 0:
//                    case 1:
//                        selectedRevisitGroup.GradeOfGroup = "B";
//                        break;
//                    case 2:
//                        selectedRevisitGroup.GradeOfGroup = "C";
//                        break;
//                    case 3:
//                        selectedRevisitGroup.GradeOfGroup = "D";
//                        break;
//                    case 4:
//                    case 5:
//                        selectedRevisitGroup.GradeOfGroup = "E";
//                        if(mainActivity.AllowGradeEGroup)
//                            break;
//
//                        RegroupProblemsCheck.add("आधीच्या एकाच गटातील सभासद संख्या २ पेक्षा कमी आहे.");
//                        break;
//                }
//                break;
//            case 3:
//                selectedRevisitGroup.LastCycleGroupContinued = 0;
//                selectedRevisitGroup.GradeOfGroup = "C";
//                //000xy => Not Allowed
//                //00xxy => Allowed "D"
//                //0xxxy => Allowed "C"
//                //0xxyy => Allowed "C"
//                //zzzxy => Allowed "C"
//                //zzxxy => Allowed "C"
//                //zxxxy => Allowed "C"
//                //zxxyy => Allowed "C"
//                switch (iZeroLastGroupIDCount) {
//                    case 0:
//                    case 1:
//                        selectedRevisitGroup.GradeOfGroup = "C";
//                        break;
//                    case 2:
//                        selectedRevisitGroup.GradeOfGroup = "D";
//                        break;
//                    case 3:
//                    case 4:
//                    case 5:
//                        selectedRevisitGroup.GradeOfGroup = "E";
//                        if(mainActivity.AllowGradeEGroup)
//                            break;
//
//                        RegroupProblemsCheck.add("आधीच्या एकाच गटातील सभासद संख्या २ पेक्षा कमी आहे.");
//                        break;
//                }
//                break;
//            case 4:
//                selectedRevisitGroup.LastCycleGroupContinued = 0;
//                selectedRevisitGroup.GradeOfGroup = "D";
//                //00xyz => Not Allowed
//                //0xxyz => Allowed "D"
//                //wwxyz => Allowed "D"
//                //wxxyz => Allowed "D"
//                switch (iZeroLastGroupIDCount) {
//                    case 0:
//                    case 1:
//                        selectedRevisitGroup.GradeOfGroup = "D";
//                        break;
//                    case 2:
//                    case 3:
//                    case 4:
//                    case 5:
//                        selectedRevisitGroup.GradeOfGroup = "E";
//                        if(mainActivity.AllowGradeEGroup)
//                            break;
//
//                        RegroupProblemsCheck.add("आधीच्या एकाच गटातील सभासद संख्या २ पेक्षा कमी आहे.");
//                        break;
//                }
//                break;
//            case 5:
//                selectedRevisitGroup.LastCycleGroupContinued = 0;
//                //0wxyz => Not Allowed
//                //vwxyz => Not Allowed
//                selectedRevisitGroup.GradeOfGroup = "E";
//                if(mainActivity.AllowGradeEGroup)
//                    break;
//
//                RegroupProblemsCheck.add("आधीच्या एकाच गटातील सभासद संख्या २ पेक्षा कमी आहे.");
//                break;
//        }
    }
    private class AsyncCallWS_SaveRegroup extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.progressBarRegroup);
            if (pb != null) {
                pb.setIndeterminate(true);
                pb.setVisibility(View.VISIBLE);
            }

            Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerRevisitGroupInfo);
            sp.setEnabled(false);
            TableLayout tblGroupMembers = (TableLayout) rootView.findViewById(R.id.tblLayoutGroupMembers);
            tblGroupMembers.setEnabled(false);
            for(int k=0; k<tblGroupMembers.getChildCount(); k++) {
                tblGroupMembers.getChildAt(k).setEnabled(false);
            }
            tblGroupMembers = (TableLayout) rootView.findViewById(R.id.tblLayoutReadyForLoanMembers);
            tblGroupMembers.setEnabled(false);
            for(int k=0; k<tblGroupMembers.getChildCount(); k++) {
                tblGroupMembers.getChildAt(k).setEnabled(false);
            }

            Button bt = (Button) rootView.findViewById(R.id.buttonCloseRegroup);
            bt.setEnabled(false);
            bt = (Button) rootView.findViewById(R.id.buttonSaveRegroup);
            bt.setEnabled(false);
            bt = (Button) rootView.findViewById(R.id.buttonRemoveRegroup);
            bt.setEnabled(false);
            bt = (Button) rootView.findViewById(R.id.buttonAddRegroup);
            bt.setEnabled(false);
            bt = (Button) rootView.findViewById(R.id.buttonReplaceRegroup);
            bt.setEnabled(false);
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
        @Override
        protected Void doInBackground(String... strings) {
            SaveRegroup();

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.progressBarRegroup);
            if(pb != null)
                pb.setVisibility(View.INVISIBLE);

            Button bt = (Button) rootView.findViewById(R.id.buttonCloseRegroup);
            bt.setEnabled(true);
        }
    }
//bmp 11-Aug-26
//    public void SaveRegroup() {
//        Calendar cal0 = Calendar.getInstance();
//        LastTimeInMillis = cal0.getTimeInMillis();
////bmp 12-Mar-26
//        boolean flagOngoingLoanMemberInGroup = false;
//        for(int i=0; i<selectedRevisitGroup.MemberCount; i++) {
//            if(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i) < 0)
//                continue;
//            if(mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i)).VisitDetailsID
//                    == clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING) {
//                flagOngoingLoanMemberInGroup = true;
//                break;
//            }
//        }
////bmp 12-Mar-26
//
//        for (int i = 0; i < selectedRevisitGroup.MemberCount; i++) {
////bmp 16-Jun-23
//// Do this for all 5 group members to ensure newly 'Added'/'Replaced' and continued members are 'Active'
//            mainActivity.SQLite_getMembers(selectedRevisitGroup.GroupMemberID.get(i), "MemberID");
//            mainActivity.SQLite_getMembers(mainActivity.Member.get(0).MemberID, "MainMemberID");
//            mainActivity.Member.get(0).IsActive = 1;
////bmp 16-Jun-23
//            if (originalRevisitGroup.GroupMemberID.get(i) != selectedRevisitGroup.GroupMemberID.get(i)) {
//                if (originalRevisitGroup.GroupMemberID.get(i) == 0) {
//                    //Member Added:
//                    for (int k = 0; k < mainActivity.Member.size(); k++) {
//                        mainActivity.Member.get(k).GroupID = originalRevisitGroup.GroupID;
//                        mainActivity.Member.get(k).BranchID = mainActivity.BranchID;
//                        LastTimeInMillis += 1000;
//                        if (mainActivity.SQLite_saveMember(mainActivity.Member.get(k), LastTimeInMillis, true)) {
//                            System.out.println("Moved MemberID: " + String.valueOf(mainActivity.Member.get(k).MemberID) + " To GroupID: " + String.valueOf(originalRevisitGroup.GroupID));
//                        }
//                    }
//                } else {
//                    //Members Exchanged:
//                    for (int k = 0; k < mainActivity.Member.size(); k++) {
//                        mainActivity.Member.get(k).GroupID = originalRevisitGroup.GroupID;
//                        mainActivity.Member.get(k).BranchID = mainActivity.BranchID;
//                        LastTimeInMillis += 1000;
//                        if (mainActivity.SQLite_saveMember(mainActivity.Member.get(k), LastTimeInMillis, true)) {
//                            System.out.println("Moved MemberID: " + String.valueOf(mainActivity.Member.get(k).MemberID) + " To GroupID: " + String.valueOf(originalRevisitGroup.GroupID));
//                        }
//                    }
//
//                    mainActivity.SQLite_getMembers(originalRevisitGroup.GroupMemberID.get(i), "MemberID");
//                    mainActivity.SQLite_getMembers(mainActivity.Member.get(0).MemberID, "MainMemberID");
////bmp 11-Feb-26
//                    if(flagOngoingLoanMemberInGroup) {
//                        //Add Outgoing Member Undertaking Document Entry
//                        clsMemberDocRcvd newDocRcvd = new clsMemberDocRcvd();
//                        newDocRcvd.Init_MemberDocRcvd();
//                        newDocRcvd.MemberDocumentsID = clsDocumentIDs.MEMBER_DOC_ID_NEXTLOAN_NOT_REQ_UNDERTAKING;
//                        newDocRcvd.MemberID = mainActivity.Member.get(0).MemberID;
//                        newDocRcvd.DocumentNo = "0";
//                        newDocRcvd.IsActive = 1;
//                        Calendar cal = Calendar.getInstance();
//                        while (LastTimeInMillis > cal.getTimeInMillis()) {
//                            cal = Calendar.getInstance();
//                        }
//                        LastTimeInMillis = cal.getTimeInMillis();
//                        LastTimeInMillis += 1000;
//                        newDocRcvd.MemberDocuemntsRcvdID = mainActivity.SQLite_saveMemberDocRcvd(newDocRcvd, LastTimeInMillis, true);
//
//                        //Add Outgoing Member Undertaking Photo Entry
//                        clsDocumentPhoto newDocPhoto = new clsDocumentPhoto();
//                        newDocPhoto.Init_DocumentPhoto();
//                        newDocPhoto.MemberDocuemntsRcvdID = newDocRcvd.MemberDocuemntsRcvdID;
//                        newDocPhoto.GroupID = selectedNoLoanGroupID;
////bmp 07-Apr-26
//                        newDocPhoto.MemberDocumentsID = clsDocumentIDs.MEMBER_DOC_ID_NEXTLOAN_NOT_REQ_UNDERTAKING;
////bmp 07-Apr-26
//                        cal = Calendar.getInstance();
//                        while (LastTimeInMillis > cal.getTimeInMillis()) {
//                            cal = Calendar.getInstance();
//                        }
//                        LastTimeInMillis = cal.getTimeInMillis();
//                        LastTimeInMillis += 1000;
//                        newDocPhoto.DocumentPhotoID = mainActivity.SQLite_saveDocumentPhoto(newDocPhoto, LastTimeInMillis, true);
//                        String strMemberFilename = String.valueOf(newDocPhoto.DocumentPhotoID) + ".jpg";
//
//                        //Add Outgoing Member Undertaking Photo Entry to Group
//                        long lTemp = 0;
//                        lTemp = mainActivity.AddDocumentPhotos(0, selectedRevisitGroup.GroupID, true);
////                        lTemp = mainActivity.AddGroupDocumentPhotos(newDocPhoto, true);
//                        String strGroupFilename = String.valueOf(lTemp) + ".jpg";
//
//                        String strFilepath = mainActivity.baseDir + "/data/";
//                        String strUndertaking = "";
//                        //                    if (selectedRevisitGroup.OutgoingMemberID1 == mainActivity.Member.get(0).MemberID) {
//                        //                        strUndertaking = selectedRevisitGroup.filenameOutgoingMemberUndertaking1;
//                        //                    } else if (selectedRevisitGroup.OutgoingMemberID2 == mainActivity.Member.get(0).MemberID) {
//                        //                        strUndertaking = selectedRevisitGroup.filenameOutgoingMemberUndertaking2;
//                        //                    }
//                        strUndertaking = mainActivity.RevisitMemberList.get(originalRevisitGroup.GroupMemberRevisitMemberListIndex.get(i)).strRegroupProof;
//                        //                    File undertakingFile = new File(strFilepath + strUndertaking + ".jpg");
//                        File undertakingFile = new File(strFilepath + strUndertaking);
//                        if (undertakingFile.exists()) {
//                            try {
//                                InputStream in = null;
//                                OutputStream outmemberdata = null;
//                                OutputStream outmemberupload = null;
//                                OutputStream outgroupdata = null;
//                                OutputStream outgroupupload = null;
//                                byte[] buffer = new byte[1024];
//                                File memberdataFile = new File(mainActivity.baseDir + "/data/" + strMemberFilename);
//                                File memberuploadFile = new File(mainActivity.baseDir + "/upload/" + strMemberFilename);
//                                File groupdataFile = new File(mainActivity.baseDir + "/data/" + strGroupFilename);
//                                File groupuploadFile = new File(mainActivity.baseDir + "/upload/" + strGroupFilename);
//                                in = new FileInputStream(undertakingFile);
//                                outmemberdata = new FileOutputStream(memberdataFile);
//                                outmemberupload = new FileOutputStream(memberuploadFile);
//                                outgroupdata = new FileOutputStream(groupdataFile);
//                                outgroupupload = new FileOutputStream(groupuploadFile);
//                                int read;
//                                while ((read = in.read(buffer)) != -1) {
//                                    outmemberdata.write(buffer, 0, read);
//                                    outmemberupload.write(buffer, 0, read);
//                                    outgroupdata.write(buffer, 0, read);
//                                    outgroupupload.write(buffer, 0, read);
//                                }
//                                in.close();
//                                in = null;
//                                outmemberdata.flush();
//                                outmemberdata.close();
//                                outmemberdata = null;
//                                outmemberupload.flush();
//                                outmemberupload.close();
//                                outmemberupload = null;
//                                outgroupdata.flush();
//                                outgroupdata.close();
//                                outgroupdata = null;
//                                outgroupupload.flush();
//                                outgroupupload.close();
//                                outgroupupload = null;
//
//                                mainActivity.SyncDocUpload.add(strMemberFilename);
//                                mainActivity.SyncDocUpload.add(strGroupFilename);
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            undertakingFile.delete();
//                        }
////bmp 11-Feb-26
////bmp 12-Mar-26
//                        mainActivity.Member.get(0).GroupGuarantor = 1;
//                    }
////bmp 12-Mar-26
//                    for (int k = 0; k < mainActivity.Member.size(); k++) {
//                        mainActivity.Member.get(k).GroupID = selectedNoLoanGroupID;
//                        mainActivity.Member.get(k).BranchID = mainActivity.BranchID;
//                        LastTimeInMillis += 1000;
//                        if (mainActivity.SQLite_saveMember(mainActivity.Member.get(k), LastTimeInMillis, true)) {
//                            System.out.println("Moved MemberID: " + String.valueOf(mainActivity.Member.get(k).MemberID) + " To GroupID: " + String.valueOf(selectedNoLoanGroupID));
//                        }
//                    }
//                    clsNewGroupMemberInfo newGroupMemberInfo = new clsNewGroupMemberInfo();
//                    newGroupMemberInfo.Init_NewGroupMemberInfo();
//                    newGroupMemberInfo.MemberID = mainActivity.Member.get(0).MemberID;
//                    newGroupMemberInfo.LoanApplicant = 0;
//                    newGroupMemberInfo.GroupID = selectedNoLoanGroupID;
////bmp 17-Jan-25
//                    newGroupMemberInfo.ContactNo = mainActivity.Member.get(0).ContactNo;
////bmp 17-Jan-25
//                    LastTimeInMillis += 1000;
//                    mainActivity.SQLite_saveGroupMemberInfo(newGroupMemberInfo, LastTimeInMillis, true);
//                    mainActivity.SQLite_updateRevisitMember(originalRevisitGroup.GroupMemberRevisitMemberListIndex.get(i), true);
//                }
////bmp 22-Jun-23
////In case: Reactivating member with same GroupID but not in Promotionlist
//            } else {
//                LastTimeInMillis += 1000;
//                mainActivity.SQLite_saveMember(mainActivity.Member.get(0), LastTimeInMillis, true);
////bmp 22-Jun-23
//            }
//        }
//
//        originalRevisitGroup.RevisitGroupStatus = clsRevisitGroup.REVISIT_GROUP_STATUS_READYGROUP;
//        for (int i = 0; i < selectedRevisitGroup.MemberCount; i++) {
//            clsRevisitMember member = mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i));
//            member.CenterID = selectedRevisitGroup.CenterID;
//            member.RevisitMemberStatus = clsRevisitMember.REVISIT_MEMBER_STATUS_READYGROUP;
//            member.Edited = true;
//            member.LastCycleGroupContinued = selectedRevisitGroup.LastCycleGroupContinued;
//            mainActivity.SQLite_updateRevisitMember(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i), false);
////bmp 02-Sep-24
////            if(member.CreditCheck == 1) {
////                ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
////                // Gets the data repository in write mode
////                SQLiteDatabase db = mDbHelper.getWritableDatabase();
////                try {
////                    Calendar cal = Calendar.getInstance();
////                    Date date = cal.getTime();
////                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
////                    String strTimeStamp = sdf.format(date);// all done
////
////                    ContentValues values2 = new ContentValues();
////                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_SyncID, strTimeStamp);
////                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_DbTableName, ufinDbContract.OtherMFIData.TABLE_NAME);
////                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_updatedRecordID, member.MemberID);
////                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsNewRecord, "True");
////                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsReady, "True");
////                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_PostDataParams, "");
////
////                    long lTemp1 = db.insert(ufinDbContract.RecordsToSync.TABLE_NAME, null, values2);
////                    if (lTemp1 > 0) {
////                        MainActivity.clsRecordsToSync newRec = new MainActivity.clsRecordsToSync();
////                        newRec.SyncID = strTimeStamp;
////                        newRec.recordID = member.MemberID;
////                        newRec.recordInserted = true;
////                        newRec.recordReady = true;
////                        newRec.PostParams = "";
////                        mainActivity.SyncMemberOtherMFIData.add(newRec);
////                    }
////                } finally {
////                    db.close();
////                }
////            }
////bmp 02-Sep-24
//        }
//        for (int i = 0; i < mainActivity.arrAllGroupMembers.size(); i++) {
//            Long lMemberID = mainActivity.arrAllGroupMembers.get(i);
//            boolean Flag = false;
//            for (int j = 0; j < selectedRevisitGroup.MemberCount; j++) {
//                if (lMemberID == mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(j)).MemberID) {
//                    Flag = true;
//                    break;
//                }
//            }
//            if(!Flag) {
//                mainActivity.SQLite_getMembers(lMemberID, "MemberID");
//                mainActivity.SQLite_getMembers(mainActivity.Member.get(0).MemberID, "MainMemberID");
//
//                for (int k = 0; k < mainActivity.Member.size(); k++) {
//                    mainActivity.Member.get(k).GroupID = selectedNoLoanGroupID;
//                    mainActivity.Member.get(k).BranchID = mainActivity.BranchID;
//                    LastTimeInMillis += 1000;
//                    if (mainActivity.SQLite_saveMember(mainActivity.Member.get(k), LastTimeInMillis, true)) {
//                        System.out.println("Moved MemberID: " + String.valueOf(mainActivity.Member.get(k).MemberID) + " To GroupID: " + String.valueOf(selectedNoLoanGroupID));
//                    }
//                }
//                clsNewGroupMemberInfo newGroupMemberInfo = new clsNewGroupMemberInfo();
//                newGroupMemberInfo.Init_NewGroupMemberInfo();
//                newGroupMemberInfo.MemberID = mainActivity.Member.get(0).MemberID;
//                newGroupMemberInfo.LoanApplicant = 0;
//                newGroupMemberInfo.GroupID = selectedNoLoanGroupID;
////bmp 17-Jan-25
//                newGroupMemberInfo.ContactNo = mainActivity.Member.get(0).ContactNo;
////bmp 17-Jan-25
//                LastTimeInMillis += 1000;
//                mainActivity.SQLite_saveGroupMemberInfo(newGroupMemberInfo, LastTimeInMillis, true);
//            }
//        }
//    }
    public void SaveRegroup() {
        Calendar cal0 = Calendar.getInstance();
        LastTimeInMillis = cal0.getTimeInMillis();

        boolean flagOngoingLoanMemberInGroup = false;
        for(int i=0; i<selectedRevisitGroup.MemberCount; i++) {
            if(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i) < 0)
                continue;
            if(mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i)).VisitDetailsID
                    == clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING) {
                flagOngoingLoanMemberInGroup = true;
                break;
            }
        }

        for (int i = 0; i < selectedRevisitGroup.MemberCount; i++) {
// Do this for all group members to ensure newly 'Added'/'Replaced' and continued members are 'Active'
            mainActivity.SQLite_getMembers(selectedRevisitGroup.GroupMemberID.get(i), "MemberID");
            mainActivity.SQLite_getMembers(mainActivity.Member.get(0).MemberID, "MainMemberID");
//bmp 31-Aug-26
//            mainActivity.Member.get(0).IsActive = 1;
            if(mainActivity.Member.get(0).IsActive == 0) {
                mainActivity.Member.get(0).IsActive = 1;
                //If member is not newly Added or Replaced in the group,
                //save IsActive status to SQLite database.
                mainActivity.SQLite_saveMember(mainActivity.Member.get(0), LastTimeInMillis, true);
            }
//bmp 31-Aug-26

            int originalRevisitGroupIndex = -1;
            for (int k = 0; k < originalRevisitGroup.MemberCount; k++) {
                if (selectedRevisitGroup.GroupMemberID.get(i)
                        == originalRevisitGroup.GroupMemberID.get(k)) {
                    originalRevisitGroupIndex = k;
                    break;
                }
            }
            if (originalRevisitGroupIndex < 0) {
                //Member Added:
                for (int k = 0; k < mainActivity.Member.size(); k++) {
                    mainActivity.Member.get(k).GroupID = originalRevisitGroup.GroupID;
                    mainActivity.Member.get(k).BranchID = mainActivity.BranchID;
                    LastTimeInMillis += 1000;
                    if (mainActivity.SQLite_saveMember(mainActivity.Member.get(k), LastTimeInMillis, true)) {
                        System.out.println("Moved MemberID: " + String.valueOf(mainActivity.Member.get(k).MemberID) + " To GroupID: " + String.valueOf(originalRevisitGroup.GroupID));
                    }
                }
            }
            clsRevisitMember member = mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i));
            member.CenterID = selectedRevisitGroup.CenterID;
            member.RevisitMemberStatus = clsRevisitMember.REVISIT_MEMBER_STATUS_READYGROUP;
            member.Edited = true;
            member.LastCycleGroupContinued = selectedRevisitGroup.LastCycleGroupContinued;
            mainActivity.SQLite_updateRevisitMember(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i), false);
        }
        for (int i = 0; i < originalRevisitGroup.MemberCount; i++) {
            boolean memberRemovedOrReplaced = true;
            for (int k = 0; k < selectedRevisitGroup.MemberCount; k++) {
                if (originalRevisitGroup.GroupMemberID.get(i)
                        == selectedRevisitGroup.GroupMemberID.get(k)) {
                    memberRemovedOrReplaced = false;
                    break;
                }
            }
            if(memberRemovedOrReplaced) {
                mainActivity.SQLite_getMembers(originalRevisitGroup.GroupMemberID.get(i), "MemberID");
                mainActivity.SQLite_getMembers(mainActivity.Member.get(0).MemberID, "MainMemberID");

                if(flagOngoingLoanMemberInGroup) {
                    //Add Outgoing Member Undertaking Document Entry
                    clsMemberDocRcvd newDocRcvd = new clsMemberDocRcvd();
                    newDocRcvd.Init_MemberDocRcvd();
                    newDocRcvd.MemberDocumentsID = clsDocumentIDs.MEMBER_DOC_ID_NEXTLOAN_NOT_REQ_UNDERTAKING;
                    newDocRcvd.MemberID = mainActivity.Member.get(0).MemberID;
                    newDocRcvd.DocumentNo = "0";
                    newDocRcvd.IsActive = 1;
                    Calendar cal = Calendar.getInstance();
                    while (LastTimeInMillis > cal.getTimeInMillis()) {
                        cal = Calendar.getInstance();
                    }
                    LastTimeInMillis = cal.getTimeInMillis();
                    LastTimeInMillis += 1000;
                    newDocRcvd.MemberDocuemntsRcvdID = mainActivity.SQLite_saveMemberDocRcvd(newDocRcvd, LastTimeInMillis, true);

                    //Add Outgoing Member Undertaking Photo Entry
                    clsDocumentPhoto newDocPhoto = new clsDocumentPhoto();
                    newDocPhoto.Init_DocumentPhoto();
                    newDocPhoto.MemberDocuemntsRcvdID = newDocRcvd.MemberDocuemntsRcvdID;
                    newDocPhoto.GroupID = selectedNoLoanGroupID;
                    newDocPhoto.MemberDocumentsID = clsDocumentIDs.MEMBER_DOC_ID_NEXTLOAN_NOT_REQ_UNDERTAKING;
                    cal = Calendar.getInstance();
                    while (LastTimeInMillis > cal.getTimeInMillis()) {
                        cal = Calendar.getInstance();
                    }
                    LastTimeInMillis = cal.getTimeInMillis();
                    LastTimeInMillis += 1000;
                    newDocPhoto.DocumentPhotoID = mainActivity.SQLite_saveDocumentPhoto(newDocPhoto, LastTimeInMillis, true);
                    String strMemberFilename = String.valueOf(newDocPhoto.DocumentPhotoID) + ".jpg";

                    //Add Outgoing Member Undertaking Photo Entry to Group
                    long lTemp = 0;
                    lTemp = mainActivity.AddDocumentPhotos(0, selectedRevisitGroup.GroupID, true);
                    String strGroupFilename = String.valueOf(lTemp) + ".jpg";

                    String strFilepath = mainActivity.baseDir + "/data/";
                    String strUndertaking = "";
                    strUndertaking = mainActivity.RevisitMemberList.get(originalRevisitGroup.GroupMemberRevisitMemberListIndex.get(i)).strRegroupProof;
                    File undertakingFile = new File(strFilepath + strUndertaking);
                    if (undertakingFile.exists()) {
                        try {
                            InputStream in = null;
                            OutputStream outmemberdata = null;
                            OutputStream outmemberupload = null;
                            OutputStream outgroupdata = null;
                            OutputStream outgroupupload = null;
                            byte[] buffer = new byte[1024];
                            File memberdataFile = new File(mainActivity.baseDir + "/data/" + strMemberFilename);
                            File memberuploadFile = new File(mainActivity.baseDir + "/upload/" + strMemberFilename);
                            File groupdataFile = new File(mainActivity.baseDir + "/data/" + strGroupFilename);
                            File groupuploadFile = new File(mainActivity.baseDir + "/upload/" + strGroupFilename);
                            in = new FileInputStream(undertakingFile);
                            outmemberdata = new FileOutputStream(memberdataFile);
                            outmemberupload = new FileOutputStream(memberuploadFile);
                            outgroupdata = new FileOutputStream(groupdataFile);
                            outgroupupload = new FileOutputStream(groupuploadFile);
                            int read;
                            while ((read = in.read(buffer)) != -1) {
                                outmemberdata.write(buffer, 0, read);
                                outmemberupload.write(buffer, 0, read);
                                outgroupdata.write(buffer, 0, read);
                                outgroupupload.write(buffer, 0, read);
                            }
                            in.close();
                            in = null;
                            outmemberdata.flush();
                            outmemberdata.close();
                            outmemberdata = null;
                            outmemberupload.flush();
                            outmemberupload.close();
                            outmemberupload = null;
                            outgroupdata.flush();
                            outgroupdata.close();
                            outgroupdata = null;
                            outgroupupload.flush();
                            outgroupupload.close();
                            outgroupupload = null;

                            mainActivity.SyncDocUpload.add(strMemberFilename);
                            mainActivity.SyncDocUpload.add(strGroupFilename);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        undertakingFile.delete();
                    }
                    mainActivity.Member.get(0).GroupGuarantor = 1;
                }
                for (int k = 0; k < mainActivity.Member.size(); k++) {
                    mainActivity.Member.get(k).GroupID = selectedNoLoanGroupID;
                    mainActivity.Member.get(k).BranchID = mainActivity.BranchID;
                    LastTimeInMillis += 1000;
                    if (mainActivity.SQLite_saveMember(mainActivity.Member.get(k), LastTimeInMillis, true)) {
                        System.out.println("Moved MemberID: " + String.valueOf(mainActivity.Member.get(k).MemberID) + " To GroupID: " + String.valueOf(selectedNoLoanGroupID));
                    }
                }
                clsNewGroupMemberInfo newGroupMemberInfo = new clsNewGroupMemberInfo();
                newGroupMemberInfo.Init_NewGroupMemberInfo();
                newGroupMemberInfo.MemberID = mainActivity.Member.get(0).MemberID;
                newGroupMemberInfo.LoanApplicant = 0;
                newGroupMemberInfo.GroupID = selectedNoLoanGroupID;
                newGroupMemberInfo.ContactNo = mainActivity.Member.get(0).ContactNo;
                LastTimeInMillis += 1000;
                mainActivity.SQLite_saveGroupMemberInfo(newGroupMemberInfo, LastTimeInMillis, true);
                mainActivity.SQLite_updateRevisitMember(originalRevisitGroup.GroupMemberRevisitMemberListIndex.get(i), true);
            }
        }

        //In case there are more than 5 members in group,
        //Move all those members to selectedNoLoanGroupID
        for (int i = 0; i < mainActivity.arrAllGroupMembers.size(); i++) {
            Long lMemberID = mainActivity.arrAllGroupMembers.get(i);
            boolean Flag = false;
            for (int j = 0; j < selectedRevisitGroup.MemberCount; j++) {
                if (lMemberID == mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(j)).MemberID) {
                    Flag = true;
                    break;
                }
            }
            if(!Flag) {
                mainActivity.SQLite_getMembers(lMemberID, "MemberID");
                mainActivity.SQLite_getMembers(mainActivity.Member.get(0).MemberID, "MainMemberID");

                for (int k = 0; k < mainActivity.Member.size(); k++) {
                    mainActivity.Member.get(k).GroupID = selectedNoLoanGroupID;
                    mainActivity.Member.get(k).BranchID = mainActivity.BranchID;
                    LastTimeInMillis += 1000;
                    if (mainActivity.SQLite_saveMember(mainActivity.Member.get(k), LastTimeInMillis, true)) {
                        System.out.println("Moved MemberID: " + String.valueOf(mainActivity.Member.get(k).MemberID) + " To GroupID: " + String.valueOf(selectedNoLoanGroupID));
                    }
                }
                clsNewGroupMemberInfo newGroupMemberInfo = new clsNewGroupMemberInfo();
                newGroupMemberInfo.Init_NewGroupMemberInfo();
                newGroupMemberInfo.MemberID = mainActivity.Member.get(0).MemberID;
                newGroupMemberInfo.LoanApplicant = 0;
                newGroupMemberInfo.GroupID = selectedNoLoanGroupID;
                newGroupMemberInfo.ContactNo = mainActivity.Member.get(0).ContactNo;
                LastTimeInMillis += 1000;
                mainActivity.SQLite_saveGroupMemberInfo(newGroupMemberInfo, LastTimeInMillis, true);
            }
        }
    }
//bmp 11-Aug-26
    public void SQLite_getNoLoanGroupForCenter(long lCenterID) {
        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.OrgStructure.COLUMN_NAME_ORGStructureID,
                ufinDbContract.OrgStructure.COLUMN_NAME_NodeName,
        };

        String selection1 = ufinDbContract.OrgStructure.COLUMN_NAME_IsActive + "=" + "'" + String.valueOf(1) + "'"
                + " and " + ufinDbContract.OrgStructure.COLUMN_NAME_ParentID + "=" + "'" + String.valueOf(lCenterID) + "'"
                + " and " + ufinDbContract.OrgStructure.COLUMN_NAME_NodeLevel + "=" + "'" + String.valueOf(6) + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.OrgStructure.COLUMN_NAME_ORGStructureID + " DESC";

        Cursor c1 = db.query(
                ufinDbContract.OrgStructure.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        selectedNoLoanGroupID = 0;

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    int iCount = c1.getCount();
                    for (int i = 0; i < iCount; i++) {
                        try {
                            long lTemp = Long.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.OrgStructure.COLUMN_NAME_ORGStructureID)));
                            String strTemp = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.OrgStructure.COLUMN_NAME_NodeName));
                            if (strTemp.contains("No Loan")) {
                                selectedNoLoanGroupID = lTemp;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        c1.moveToNext();
                    }
                }
            }
        } finally {
            if(c1 != null) { c1.close(); }
            db.close();
        }
    }
    public String generateCBCheckPDF(int memberIndex) {
        String strCBCheckFolderpath = mainActivity.baseDir + "/data/cbcheck";
        File cbcheckFolder = new File(strCBCheckFolderpath);
        if(!cbcheckFolder.exists()) {
            cbcheckFolder.mkdir();
        }
        String strMemberFolderpath = strCBCheckFolderpath + "/" + String.valueOf(mainActivity.RevisitMemberList.get(memberIndex).MemberID);
        File memberFolder = new File(strMemberFolderpath);
        if(!memberFolder.exists()) {
            memberFolder.mkdir();
        }
        String strCBCheckPath = strMemberFolderpath + "/";
        String StrPDFCBCheckFileName = strCBCheckPath + String.valueOf(mainActivity.RevisitMemberList.get(memberIndex).MemberID) + ".pdf";
        // create a new document
        PdfDocument document = new PdfDocument();
        int pageHt = 950;
//        if(mainActivity.RevisitMemberList.get(memberIndex).CreditCheck == 0) {
//            pageHt = 900;
//        }
        // create a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(576, pageHt, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        // draw something on the page
        drawPageCreditReport(page, memberIndex);
        // finish the page
        document.finishPage(page);
        // add more pages.....
        // write the document content
        File PdfFile = new File(StrPDFCBCheckFileName);
        try {
            document.writeTo(new FileOutputStream(PdfFile));
        } catch (FileNotFoundException e) {
        } catch (IOException e1) {
        } finally {
            // close the document
            document.close();
        }
        System.out.println("CBCheck: " + StrPDFCBCheckFileName);

        return StrPDFCBCheckFileName;
    }
    public void drawPageCreditReport(PdfDocument.Page page, int memberIndex) {
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
        text_paint.setTextSize(24);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_OrgName)), 20, 40, text_paint);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_OrgRegNo)), 120, 80, text_paint);
        canvas.drawLine(5, 95, 570, 95, border_paint);
        text_paint.setTextSize(24);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String strDate = sdf.format(date);// all done

        int iRowOffset = 120;
        canvas.drawText(String.format("%s", strDate), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s", "क्रेडिट ब्युरो रिपोर्ट"), 210, iRowOffset, text_paint);
        iRowOffset += 40;
        canvas.drawText(String.format("%s", "ब्रँच"), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.BranchName), 120, iRowOffset, text_paint);
        iRowOffset += 30;
//        canvas.drawText(String.format("%s", "BM"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s%s", ":", mainActivity.LSO_BM), 120, iRowOffset, text_paint);
//        iRowOffset += 30;
        canvas.drawText(String.format("%s", "LSO"), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.LoginID), 120, iRowOffset, text_paint);
        iRowOffset += 30;
        canvas.drawText(String.format("%s", "सेंटर ID"), 0, iRowOffset, text_paint);
        String strCenterID = String.valueOf(mainActivity.RevisitMemberList.get(memberIndex).CenterID);
        if(strCenterID.length() > 10)
            strCenterID = strCenterID.substring(0, 10);
        canvas.drawText(String.format("%s%s", ":", strCenterID), 120, iRowOffset, text_paint);
        canvas.drawText(String.format("%s", "ग्रुप ID"), 300, iRowOffset, text_paint);
        String strGroupID = String.valueOf(mainActivity.RevisitMemberList.get(memberIndex).GroupID);
        if(strGroupID.length() > 10)
            strGroupID = strGroupID.substring(0, 10);
        canvas.drawText(String.format("%s%s", ":", strGroupID), 400, iRowOffset, text_paint);
        iRowOffset += 30;
        canvas.drawText(String.format("%s", "खाते क्र."), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.RevisitMemberList.get(memberIndex).SavingAccNo), 120, iRowOffset, text_paint);
        iRowOffset += 30;
        canvas.drawText(String.format("%s", "सभासद"), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s%s", ":",
                mainActivity.RevisitMemberList.get(memberIndex).MemberName), 120, iRowOffset, text_paint);
        iRowOffset += 40;
/// /////////////////////////////// For Testing 850
//        mainActivity.RevisitMemberList.get(memberIndex).CreditCheck = 0;
//        mainActivity.RevisitMemberList.get(memberIndex).VisitDetailsID
//                = clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED;
///// /////////////////////////////// For Testing 950
//        mainActivity.RevisitMemberList.get(memberIndex).CreditCheck = 1;
//        mainActivity.RevisitMemberList.get(memberIndex).VisitDetailsID
//                = clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED;
///// /////////////////////////////// For Testing 950
//        mainActivity.RevisitMemberList.get(memberIndex).CreditCheck = 0;
//        mainActivity.RevisitMemberList.get(memberIndex).VisitDetailsID
//                = clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING;
///// /////////////////////////////// For Testing 900
//        mainActivity.RevisitMemberList.get(memberIndex).CreditCheck = 1;
//        mainActivity.RevisitMemberList.get(memberIndex).VisitDetailsID
//                = clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING;
/// ///////////////////////////////
//        mainActivity.RevisitMemberList.get(memberIndex).CreditCheck = 0;
//        mainActivity.RevisitMemberList.get(memberIndex).CBRemark = "सभासदाचे अन्नपुर्णा सोडुन 4 पेक्षा जास्त सस्थेचे कर्ज चालु आहे.";
//        mainActivity.RevisitMemberList.get(memberIndex).CBRemark = "सभासदाची सर्व कर्जाची बाकी रक्कम 2 लाख पेक्षा जास्त आहेे.";
//        mainActivity.RevisitMemberList.get(memberIndex).CBRemark = "सभासदाचा क्रेडिट ब्युरो स्कोर कमी असल्याने सभासद रिजेक्ट आहे.";
//        mainActivity.RevisitMemberList.get(memberIndex).CBRemark = "सभासदाने कर्ज निल केल्याचे पुरावे अपुर्ण किंवा चुकिचे दिले आहेत.";
/// ///////////////////////////////
        text_paint.setTextSize(26);
        if(mainActivity.RevisitMemberList.get(memberIndex).CreditCheck == 0) {
            canvas.drawText(String.format("%s", "क्रेडिट ब्युरो रिमार्क: पुढील कर्ज देऊ शकत नाही."), 0, iRowOffset, text_paint);
            iRowOffset += 10;
            canvas.drawLine(0, iRowOffset, 450, iRowOffset, border_paint);
        } else if(mainActivity.RevisitMemberList.get(memberIndex).CreditCheck == 1) {
            canvas.drawText(String.format("%s", "क्रेडिट ब्युरो रिमार्क: पुढील कर्ज देऊ शकतो."), 0, iRowOffset, text_paint);
            iRowOffset += 10;
            canvas.drawLine(0, iRowOffset, 400, iRowOffset, border_paint);
        }
        text_paint.setTextSize(24);
        iRowOffset += 40;
        canvas.drawText(String.format("%s", "इतर संस्थांची चालू कर्ज" ), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s%s", ": ", String.valueOf(mainActivity.RevisitMemberList.get(memberIndex).CBNoOfActiveLoans)), 300, iRowOffset, text_paint);
        iRowOffset += 30;
        canvas.drawText(String.format("%s", "इतर संस्थांची कर्ज बाकी रक्कम" ), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s%s", ": ", String.valueOf(mainActivity.RevisitMemberList.get(memberIndex).CBTotalLoanBalance)), 300, iRowOffset, text_paint);
        iRowOffset += 30;
        if(mainActivity.RevisitMemberList.get(memberIndex).CreditCheck == 0) {
            canvas.drawText(String.format("%s", "क्रेडिट ब्युरो रिजेक्टचे कारण: "), 0, iRowOffset, text_paint);
            iRowOffset += 30;
            canvas.drawText(String.format("%s", mainActivity.RevisitMemberList.get(memberIndex).CBRemark), 0, iRowOffset, text_paint);
            iRowOffset += 30;
        }
        iRowOffset += 10;
        canvas.drawText(String.format("%s", "सभासदाचे निवेदन:" ), 0, iRowOffset, text_paint);
        iRowOffset += 30;
        if(mainActivity.RevisitMemberList.get(memberIndex).CreditCheck == 0) {
            if(mainActivity.RevisitMemberList.get(memberIndex).VisitDetailsID
                    == clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING) {
                canvas.drawText(String.format("%s", "1) अन्नपूर्णा मधून कर्ज घेताना मी जास्तीची कर्ज घेतलेली नव्हती /"), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "थकीत नव्हती. परंतु आजच्या रिपोर्टनुसार मी रिजेक्ट आहे."), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "2) त्यामुळे वरील रिपोर्टनुसार कर्ज संपेपर्यंत अन्नपूर्णा मधून पुढील"), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "कर्ज मिळू शकत नाही."), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "3) वरील क्रेडिट ब्युरो रिपोर्टनुसार दिसत असलेली जास्तीची कर्ज"), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "फेडून थकीत रक्कम भरून काढेन."), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "4) इथून पुढे मी कोणत्याही इतर संस्थांचे कर्ज घेणार नाही."), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "5) माझ्या उत्पन्नातील ५०% रक्कम मला घरखर्चाला वापरता येईल"), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "एवढेच कर्ज मी घेतलेले आहे. हे मी सत्य प्रतिज्ञेवर कथन करीत आहे."), 0, iRowOffset, text_paint);
                iRowOffset += 30;
            } else {
                canvas.drawText(String.format("%s", "1) माझ्या वरील क्रेडिट ब्युरो रिपोर्ट प्रमाणे अन्नपूर्णा मधून पुढील कर्ज"), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "मला मिळू शकत नाही."), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "2) माझे इतर संस्थांचे थकीत कर्ज असतील तर मी ते भरून निल"), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "केल्याची पावती आणून देइन."), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "3) माझ्या उत्पन्नातील ५०% रक्कम मला घरखर्चाला वापरता येईल"), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "एवढेच कर्ज मी घेइन. हे मी सत्य प्रतिज्ञेवर कथन करीत आहे."), 0, iRowOffset, text_paint);
                iRowOffset += 30;
            }
        } else if(mainActivity.RevisitMemberList.get(memberIndex).CreditCheck == 1) {
            if(mainActivity.RevisitMemberList.get(memberIndex).VisitDetailsID
                    == clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING) {
                canvas.drawText(String.format("%s", "1) मी वरील संस्थांचे कर्ज घेतलेले आहे. त्यामध्ये थकीत रक्कम नाही."), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "थकीत रक्कम असेल तर मी भरून काढेन."), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "2) माझ्या नावावरील एकूण कर्ज रक्कम आज रोजी क्रेडिट ब्युरोला"), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "जेवढ्या संस्थांची कर्ज रक्कम दिसत आहे."), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "3) ती कर्ज फिटेपर्यंत मी आणखी कोणत्याही संस्थांचे कर्ज घेणार नाही."), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "4) माझ्या उत्पन्नातील ५०% रक्कम मला घरखर्चाला वापरता येईल"), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "एवढेच कर्ज मी घेतलेले आहे. हे मी सत्य प्रतिज्ञेवर कथन करीत आहे."), 0, iRowOffset, text_paint);
                iRowOffset += 30;
            } else {
                canvas.drawText(String.format("%s", "1) मी वरील संस्थांचे कर्ज घेतलेले आहे. त्यामध्ये थकीत रक्कम नाही."), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "थकीत रक्कम असेल तर मी भरून काढेन."), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "2) अन्नपूर्णा मधून आता मला कर्ज मिळत आहे."), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "3) माझ्या नावावरील एकूण कर्ज रक्कम आज रोजी क्रेडिट ब्युरोला"), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "जेवढ्या संस्थांची कर्ज रक्कम दिसत आहे + अन्नपूर्णाकडून मी आता"), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "घेणारी कर्ज रक्कम एवढीच असणार आहे."), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "4)सर्व कर्ज फिटेपर्यंत मी आणखी कोणत्याही संस्थांचे कर्ज घेणार नाही."), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "5) माझ्या उत्पन्नातील ५०% रक्कम मला घरखर्चाला वापरता येईल"), 0, iRowOffset, text_paint);
                iRowOffset += 30;
                canvas.drawText(String.format("%s", "एवढेच कर्ज मी घेतलेले आहे. हे मी सत्य प्रतिज्ञेवर कथन करीत आहे."), 0, iRowOffset, text_paint);
                iRowOffset += 30;
            }
        }
        //Note
        iRowOffset += 10;
        canvas.drawText(String.format("%s", "वरील माहिती आज मला एल.एस.ओ. ताईंनी दिली. मला मान्य आहे." ), 0, iRowOffset, text_paint);
        iRowOffset += 30;
        canvas.drawText(String.format("%s", "या विषयी माझी काहीही तक्रार नाही." ), 0, iRowOffset, text_paint);
        iRowOffset += 30;

        iRowOffset += 60;
        canvas.drawText(String.format("%s", "(सभासदाची सही)"), 0, iRowOffset, text_paint);
        canvas.drawText(String.format("%s", "(एल.एस.ओ.ची सही)"), 350, iRowOffset, text_paint);
        iRowOffset += 23;
        canvas.drawLine(0, iRowOffset, 575, iRowOffset, border_paint);
    }
    public void drawPageOngoingLoanCBCheck(PdfDocument.Page page, int memberIndex) {
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
//18-Aug-26
//        canvas.drawText(String.format("%s%s", "ग्रेड:", String.valueOf(mainActivity.selectedRevisitGroup.GradeOfGroup)), 500, 220, text_paint);
//18-Aug-26
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
        for(int i=0; i<5; i++) {
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
        if(hourOfDay < 17)
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
//    public void drawPageCBCheck(PdfDocument.Page page, int memberIndex) {
//        Paint text_paint = new Paint();
//        text_paint.setColor(Color.BLACK);
//        Typeface tf = Typeface.create("Arial", Typeface.NORMAL);
//        text_paint.setTypeface(tf);
//        text_paint.setTextSize(40);
//
//        Paint border_paint = new Paint();
//        border_paint.setColor(Color.BLACK);
//        border_paint.setStrokeWidth((float) 3.0);
//
//        Canvas canvas = page.getCanvas();
//
//        canvas.drawColor(Color.WHITE);
//
//        canvas.drawLine(0, 0, 575, 0, border_paint);
////bmp 12-Nov-25
////        text_paint.setTextSize(28);
//        text_paint.setTextSize(26);
////bmp 12-Nov-25
//        canvas.drawText(String.format("%s", getString(R.string.Receipt_OrgName)), 0, 40, text_paint);
//        canvas.drawText(String.format("%s", getString(R.string.Receipt_OrgRegNo)), 80, 80, text_paint);
//        canvas.drawLine(5, 95, 570, 95, border_paint);
//        text_paint.setTextSize(24);
//
//        Calendar cal = Calendar.getInstance();
//        Date date = cal.getTime();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
//        String strDate = sdf.format(date);// all done
//
//        canvas.drawText(String.format("%s", strDate), 0, 120, text_paint);
//        canvas.drawText(String.format("%s", "पुढील कर्जाबाबतचे वेळापत्रक"), 200, 120, text_paint);
//
//        canvas.drawText(String.format("%s", "ब्रँच"), 0, 190, text_paint);
//        canvas.drawText(String.format("%s%s", ":", mainActivity.BranchName), 80, 190, text_paint);
//
//        canvas.drawText(String.format("%s", "सेंटर ID"), 0, 220, text_paint);
//        String strTemp = String.valueOf(mainActivity.selectedRevisitGroupMember[memberIndex].CenterID);
//        if(strTemp.length() > 10)
//            strTemp = strTemp.substring(0, 10);
//        canvas.drawText(String.format("%s%s", ":", strTemp), 80, 220, text_paint);
//        canvas.drawText(String.format("%s", "ग्रुप ID"), 250, 220, text_paint);
//        strTemp = String.valueOf(mainActivity.selectedRevisitGroupMember[memberIndex].GroupID);
//        if(strTemp.length() > 10)
//            strTemp = strTemp.substring(0, 10);
//        canvas.drawText(String.format("%s%s", ":", strTemp), 320, 220, text_paint);
//        canvas.drawText(String.format("%s%s", "ग्रेड:", String.valueOf(mainActivity.selectedRevisitGroup.GradeOfGroup)), 500, 220, text_paint);
//
//        canvas.drawText(String.format("%s", "सभासद"), 0, 260, text_paint);
//        canvas.drawText(String.format("%s%s", ":", mainActivity.selectedRevisitGroupMember[memberIndex].MemberName), 80, 260, text_paint);
//
//        canvas.drawText(String.format("%s", "मोबाइल"), 0, 290, text_paint);
//        canvas.drawText(String.format("%s%s", ":", mainActivity.selectedRevisitGroupMember[memberIndex].ContactNumber), 80, 290, text_paint);
//        canvas.drawText(String.format("%s", "खाते क्र."), 250, 290, text_paint);
//        canvas.drawText(String.format("%s%s", ":", mainActivity.selectedRevisitGroupMember[memberIndex].SavingAccNo), 320, 290, text_paint);
//
//        int iRowOffset = 330;
//        if(mainActivity.selectedRevisitGroupMember[memberIndex].HomeLoanAmount > 0) {
//            canvas.drawText(String.format("%s%s%s", "सभासदाला मिळू शकणारी गृहकर्ज रक्कम: ", String.valueOf(mainActivity.selectedRevisitGroupMember[memberIndex].HomeLoanAmount), "/-"), 0, iRowOffset, text_paint);
//            iRowOffset += 40;
//        }
////bmp 07-Jun-25
////        canvas.drawText(String.format("%s", "सभासदाला मिळू शकणारी इतर कर्ज रक्कम"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", "सभासदाला मिळू शकणारी कर्ज रक्कम"), 0, iRowOffset, text_paint);
////bmp 07-Jun-25
//        iRowOffset += 10;
//
//        int iRowHt = 40;
//        int iTblOffset = iRowOffset;
//        iRowOffset = iTblOffset - iRowHt;
//        int iColWd[] = {375, 90, 110};
//        int iColOffset = 0;
//
//        //Horizontal lines
//        for (int i = 0; i < 13; i++) {
//            iRowOffset += iRowHt;
//            canvas.drawLine(0, iRowOffset, 575, iRowOffset, border_paint);
//        }
//        //Vertical lines
//        canvas.drawLine(iColOffset, iTblOffset, iColOffset, iRowOffset, border_paint);
//        for (int i = 0; i < 3; i++) {
//            iColOffset += iColWd[i];
//            canvas.drawLine(iColOffset, iTblOffset, iColOffset, iRowOffset, border_paint);
//        }
//        iColOffset = iColWd[0] + 5;
//        iRowOffset = iTblOffset + 25;
//
//////////////////////////////////////////////////////////////////
////bmp 12-Jan-24
////        if(     (mainActivity.selectedRevisitGroupMember[memberIndex].MaritalStatusID == 1) ||
////                (mainActivity.selectedRevisitGroupMember[memberIndex].MaritalStatusID == 7) )
////            mainActivity.selectedRevisitGroupMember[memberIndex].Single = false;
////        else
////            mainActivity.selectedRevisitGroupMember[memberIndex].Single = true;
////
////        int incentiveGenuineGradeGroup = 0;
////        if (mainActivity.selectedRevisitGroup.GradeOfGroup.equalsIgnoreCase("A")) {
////            incentiveGenuineGradeGroup = clsRevisitMember.REVISIT_MEMBER_INCENTIVE_GRADE_A_GROUP;
////            if(mainActivity.selectedRevisitGroupMember[memberIndex].PrevMaxLoanAmount > 150000) {
////                if(     (mainActivity.selectedRevisitGroupMember[memberIndex].OwnHouse == 0) ||
////                        (mainActivity.selectedRevisitGroupMember[memberIndex].Single) ) {
////                    incentiveGenuineGradeGroup = 5000;
////                }
////            }
////        } else if (mainActivity.selectedRevisitGroupMember[memberIndex].incentiveGenuineBTypeGroup > 0) {
////            if (mainActivity.selectedRevisitGroup.GradeOfGroup.equalsIgnoreCase("B")) {
////                incentiveGenuineGradeGroup = mainActivity.selectedRevisitGroupMember[memberIndex].incentiveGenuineBTypeGroup;
////            }
////        }
////        int incentiveLowPrevMaxLoanAmount = 0;
////        int incentiveCommunityMeetingNoAttendance = 0;
////        int incentiveCommunityMeetingPartAttendance = 5000;
////        int incentiveCommunityMeetingFullAttendance = 10000;
////
////        boolean flagNewMember = false;
////        if(     (mainActivity.selectedRevisitGroupMember[memberIndex].LastLoanAmount == 0) &&
////                (mainActivity.selectedRevisitGroupMember[memberIndex].PrevMaxLoanAmount == 55000) ) {
////            flagNewMember = true;
////        }
////
////        if(flagNewMember) {
////            incentiveCommunityMeetingPartAttendance = 0;
////            incentiveCommunityMeetingFullAttendance = 0;
////        } else {
////            if(mainActivity.selectedRevisitGroupMember[memberIndex].PrevMaxLoanAmount > 150000) {
////                if ((mainActivity.selectedRevisitGroupMember[memberIndex].OwnHouse == 0) ||
////                        (mainActivity.selectedRevisitGroupMember[memberIndex].Single)) {
////                    incentiveCommunityMeetingPartAttendance = 0;
////                    incentiveCommunityMeetingFullAttendance = 5000;
////                }
////            } else {
////                int iTemp = mainActivity.selectedRevisitGroupMember[memberIndex].PrevMaxLoanAmount;
////                iTemp = iTemp + incentiveGenuineGradeGroup;
////                iTemp = iTemp + mainActivity.selectedRevisitGroupMember[memberIndex].incentiveRegularLoanRepay;
////                iTemp = iTemp + mainActivity.selectedRevisitGroupMember[memberIndex].incentiveNotWithdrawingSavingsShares;
////                iTemp = iTemp + mainActivity.selectedRevisitGroupMember[memberIndex].incentiveRegularRDpay;
////                iTemp = iTemp + mainActivity.selectedRevisitGroupMember[memberIndex].incentiveRegularADHPpay;
////                iTemp = iTemp + mainActivity.selectedRevisitGroupMember[memberIndex].incentiveAGMAttendance;
////                if(iTemp < 55000) {
////                    incentiveCommunityMeetingPartAttendance = 0;
////                    incentiveCommunityMeetingFullAttendance = 0;
////                    incentiveLowPrevMaxLoanAmount = 55000 - iTemp;
////                }
////            }
////        }
////bmp 12-Jan-24
//////////////////////////////////////////////////////////////////
//        canvas.drawText(String.format("%s", "वाढीव कर्ज रक्कमेचे निकष"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", String.valueOf("वाढ रु.")), iColOffset, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", String.valueOf("एकूण रु.")), (iColOffset + iColWd[1]), iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//
////        canvas.drawText(String.format("%s", "फ़िटलेल्या/पहिल्या कर्जाची रक्कम"), 0, iRowOffset, text_paint);
//        if(mainActivity.selectedRevisitGroupMember[memberIndex].flagNewMember == 1) {
//            canvas.drawText(String.format("%s", "नवीन सभासदासाठी पहिल्या कर्जाची रक्कम"), 0, iRowOffset, text_paint);
//        } else {
//            canvas.drawText(String.format("%s", "आधीच्या 5 कर्जापैकी सर्वात मोठी रक्कम"), 0, iRowOffset, text_paint);
//        }
//        canvas.drawText(String.format("%s", String.valueOf(" --- ")), iColOffset, iRowOffset, text_paint);
//        int iLoanAmount = mainActivity.selectedRevisitGroupMember[memberIndex].PrevMaxLoanAmount;
//        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember[memberIndex].PrevMaxLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//        canvas.drawText(String.format("%s", "A/* ग्रेड गट बनवण्याबाबत"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember[memberIndex].incentiveGenuineGradeGroup)), iColOffset, iRowOffset, text_paint);
//        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember[memberIndex].incentiveGenuineGradeGroup;
//        canvas.drawText(String.format("%s", String.valueOf(iLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//        canvas.drawText(String.format("%s", "आधीच्या कर्ज परतफेडीनुसार"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember[memberIndex].incentiveRegularLoanRepay)), iColOffset, iRowOffset, text_paint);
//        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember[memberIndex].incentiveRegularLoanRepay;
//        canvas.drawText(String.format("%s", String.valueOf(iLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//        canvas.drawText(String.format("%s", "बचत व शेअर्स काढून न घेतल्यास"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember[memberIndex].incentiveNotWithdrawingSavingsShares)), iColOffset, iRowOffset, text_paint);
//        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember[memberIndex].incentiveNotWithdrawingSavingsShares;
//        canvas.drawText(String.format("%s", String.valueOf(iLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//        canvas.drawText(String.format("%s", "आर. डी. नियमित भरण्याबाबत"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember[memberIndex].incentiveRegularRDpay)), iColOffset, iRowOffset, text_paint);
//        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember[memberIndex].incentiveRegularRDpay;
//        canvas.drawText(String.format("%s", String.valueOf(iLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//        canvas.drawText(String.format("%s", "आधारपूर्णा नियमित भरण्याबाबत"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember[memberIndex].incentiveRegularADHPpay)), iColOffset, iRowOffset, text_paint);
//        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember[memberIndex].incentiveRegularADHPpay;
//        canvas.drawText(String.format("%s", String.valueOf(iLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//        canvas.drawText(String.format("%s", "वार्षिक मिटिंगला हजर राहिल्यास"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember[memberIndex].incentiveAGMAttendance)), iColOffset, iRowOffset, text_paint);
//        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember[memberIndex].incentiveAGMAttendance;
//        canvas.drawText(String.format("%s", String.valueOf(iLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//        canvas.drawText(String.format("%s", "कम्युनिटी मिटिंगला गैरहजर राहिल्यास"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember[memberIndex].incentiveCommunityMeetingNoAttendance)), iColOffset, iRowOffset, text_paint);
//        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember[memberIndex].incentiveCommunityMeetingNoAttendance;
//        canvas.drawText(String.format("%s", String.valueOf(iLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//        canvas.drawText(String.format("%s", "कम्युनिटी मिटिंगला अर्धा वेळ हजर असल्यास"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember[memberIndex].incentiveCommunityMeetingPartAttendance)), iColOffset, iRowOffset, text_paint);
//        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember[memberIndex].incentiveCommunityMeetingPartAttendance;
//        canvas.drawText(String.format("%s", String.valueOf(iLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//        canvas.drawText(String.format("%s", "कम्युनिटी मिटिंगला पूर्ण वेळ हजर राहिल्यास"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember[memberIndex].incentiveCommunityMeetingFullAttendance)), iColOffset, iRowOffset, text_paint);
//        iLoanAmount = iLoanAmount + (mainActivity.selectedRevisitGroupMember[memberIndex].incentiveCommunityMeetingFullAttendance - mainActivity.selectedRevisitGroupMember[memberIndex].incentiveCommunityMeetingPartAttendance);
//        canvas.drawText(String.format("%s", String.valueOf(iLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//        canvas.drawText(String.format("%s", "मिळू शकणारी अंदाजे कर्ज रक्कम"), 0, iRowOffset, text_paint);
////        canvas.drawText(String.format("%s", String.valueOf(" --- ")), iColOffset, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", String.valueOf(mainActivity.selectedRevisitGroupMember[memberIndex].incentiveLowPrevMaxLoanAmount)), iColOffset, iRowOffset, text_paint);
//        iLoanAmount = iLoanAmount + mainActivity.selectedRevisitGroupMember[memberIndex].incentiveLowPrevMaxLoanAmount;
//        canvas.drawText(String.format("%s", String.valueOf(iLoanAmount)), (iColOffset + iColWd[1]), iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//
//        iRowOffset += 10;
//        canvas.drawText(String.format("%s", "वैयक्तिक माहिती (पीआयएफ) साठी आवश्यक ओरिजिनल कागदपत्रे"), 0, iRowOffset, text_paint);
//        iRowOffset += 10;
//
//        iTblOffset = iRowOffset;
//        iRowOffset = iTblOffset - iRowHt;
//        int iColWd2[] = { 575, 0 };
//        //Horizontal lines
////        for(int i=0; i<(alRequiredDocuments.size()+3); i++) {
////            iRowOffset += iRowHt;
////            canvas.drawLine(0, iRowOffset, 575, iRowOffset, border_paint);
////        }
//        iColOffset = 0;
//        //Vertical lines
//        canvas.drawLine(iColOffset, iTblOffset, iColOffset, iRowOffset, border_paint);
//        for(int i=0; i<1; i++) {
//            iColOffset += iColWd2[i];
//            canvas.drawLine(iColOffset, iTblOffset, iColOffset, iRowOffset, border_paint);
//        }
//
//        iRowOffset = iTblOffset + 25;
////        for(int i=0; i<alRequiredDocuments.size(); i++) {
////            canvas.drawText(String.format("%s", alRequiredDocuments.get(i)), 0, iRowOffset, text_paint);
////            iRowOffset += iRowHt;
////        }
//
//        iRowOffset += iRowHt;
//        iRowOffset += iRowHt;
//
//        iRowOffset += 10;
//        canvas.drawText(String.format("%s", "सभासदाने वैयक्तिक माहिती (पीआयएफ) भरण्याच्या तारखेला"), 0, iRowOffset, text_paint);
//        iRowOffset += iRowHt - 10;
//        canvas.drawText(String.format("%s", "ठरलेल्या वेळी ओरिजिनल कागदपत्रांसहित उपस्थित रहावे."), 0, iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//
//        iRowOffset += 10;
//        canvas.drawText(String.format("%s", "कर्जबाबतचे वेळापत्रक"), 0, iRowOffset, text_paint);
//        iRowOffset += 10;
//
//        iTblOffset = iRowOffset;
//        iRowOffset = iTblOffset - iRowHt;
//        int iColWd3[] = { 410, 165 };
//        //Horizontal lines
////bmp 20-Aug-25
////        for(int i=0; i<9; i++) {
//        for(int i=0; i<7; i++) {
////bmp 20-Aug-25
//            iRowOffset += iRowHt;
//            canvas.drawLine(0, iRowOffset, 575, iRowOffset, border_paint);
//        }
//        iColOffset = 0;
//        //Vertical lines
//        canvas.drawLine(iColOffset, iTblOffset, iColOffset, iRowOffset, border_paint);
//        for(int i=0; i<2; i++) {
//            iColOffset += iColWd3[i];
//            canvas.drawLine(iColOffset, iTblOffset, iColOffset, iRowOffset, border_paint);
//        }
//
//        iColOffset = iColWd3[0] + 5;
//        iRowOffset = iTblOffset + 25;
//        canvas.drawText(String.format("%s", "वैयक्तिक माहिती(पीआयएफ) भरण्याची तारीख"), 0, iRowOffset, text_paint);
////        canvas.drawText(String.format("%s", mainActivity.selectedRevisitGroup.PIFDate), iColOffset, iRowOffset, text_paint);
//        String strPIFDate = mainActivity.selectedRevisitGroup.PIFDate;
//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
//        try {
//            Date date1 = sdf1.parse(mainActivity.selectedRevisitGroup.PIFDate);
//            strPIFDate = sdf2.format(date1);
//        } catch (ParseException e) {
////            throw new RuntimeException(e);
//        }
//        canvas.drawText(String.format("%s", strPIFDate), iColOffset, iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//        canvas.drawText(String.format("%s", "वैयक्तिक माहिती(पीआयएफ) भरण्याची वेळ"), 0, iRowOffset, text_paint);
////        canvas.drawText(String.format("%s", mainActivity.selectedRevisitGroup.PIFTime), iColOffset, iRowOffset, text_paint);
//        String strPIFTime = mainActivity.selectedRevisitGroup.PIFTime;
//        int hourOfDay = Integer.valueOf(strPIFTime.substring(0, 2));
//        String strTime = "सकाळी ";
//        if(hourOfDay < 17)
//            strTime = "दुपारी ";
//        else if(hourOfDay < 19)
//            strTime = "संध्याकाळी ";
//        else
//            strTime = "रात्री ";
//        SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
//        SimpleDateFormat sdf4 = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
//        try {
//            Date date1 = sdf3.parse(strPIFTime);
//            strPIFTime = strTime + sdf4.format(date1);
//        } catch (ParseException e) {
////            throw new RuntimeException(e);
//        }
//        canvas.drawText(String.format("%s", strPIFTime), iColOffset, iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//        canvas.drawText(String.format("%s", "कम्युनिटी मिटिंगची तारीख"), 0, iRowOffset, text_paint);
////        canvas.drawText(String.format("%s", mainActivity.selectedRevisitGroup.PIFDate), iColOffset, iRowOffset, text_paint);
//        String strComMeetingDate = mainActivity.selectedRevisitGroupMember[memberIndex].TargetComMeetingDate;
//        if(strComMeetingDate.length() > 11)
//            strComMeetingDate = strComMeetingDate.substring(0, 11);
//        try {
//            Date date1 = sdf1.parse(strComMeetingDate);
//            strComMeetingDate = sdf2.format(date1);
//        } catch (ParseException e) {
////            throw new RuntimeException(e);
//            try {
//                Date date1 = sdf2.parse(strComMeetingDate);
//                strComMeetingDate = sdf2.format(date1);
//            } catch (ParseException ex) {
////                throw new RuntimeException(ex);
//                strComMeetingDate = "---";
//            }
//        }
//        canvas.drawText(String.format("%s", strComMeetingDate), iColOffset, iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//        canvas.drawText(String.format("%s", "कम्युनिटी मिटिंगची वेळ"), 0, iRowOffset, text_paint);
////        canvas.drawText(String.format("%s", mainActivity.selectedRevisitGroup.PIFTime), iColOffset, iRowOffset, text_paint);
//        String strComMeetingTime = mainActivity.selectedRevisitGroupMember[memberIndex].TargetComMeetingDate;
//        if(strComMeetingTime.length() >= 13) {
//            strComMeetingTime = "0" + strComMeetingTime.substring(12, 13) + ":00";
//            hourOfDay = 12 + Integer.valueOf(strComMeetingTime.substring(0, 2));
//            strTime = "सकाळी ";
//            if(hourOfDay < 17)
//                strTime = "दुपारी ";
//            else if(hourOfDay < 19)
//                strTime = "संध्याकाळी ";
//            else
//                strTime = "रात्री ";
//            try {
//                Date date1 = sdf3.parse(strComMeetingTime);
//                strComMeetingTime = strTime + sdf4.format(date1);
//            } catch (ParseException e) {
////            throw new RuntimeException(e);
//                strComMeetingTime = "---";
//            }
//        } else {
//            strComMeetingTime = "---";
//        }
//        canvas.drawText(String.format("%s", strComMeetingTime), iColOffset, iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//        canvas.drawText(String.format("%s", "गटाच्या परीक्षेची (जीआरटी) तारीख"), 0, iRowOffset, text_paint);
////        canvas.drawText(String.format("%s", mainActivity.selectedRevisitGroup.PIFDate), iColOffset, iRowOffset, text_paint);
//        String strGRTDate = mainActivity.selectedRevisitGroup.GroupTestDate;
//        try {
//            Date date1 = sdf1.parse(mainActivity.selectedRevisitGroup.GroupTestDate);
//            strGRTDate = sdf2.format(date1);
//        } catch (ParseException e) {
////            throw new RuntimeException(e);
//        }
//        canvas.drawText(String.format("%s", strGRTDate), iColOffset, iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//        canvas.drawText(String.format("%s", "गटाच्या परीक्षेची (जीआरटी) वेळ"), 0, iRowOffset, text_paint);
////        canvas.drawText(String.format("%s", mainActivity.selectedRevisitGroup.PIFTime), iColOffset, iRowOffset, text_paint);
//        String strGRTTime = mainActivity.selectedRevisitGroup.GroupTestTime;
//        hourOfDay = Integer.valueOf(strGRTTime.substring(0, 2));
//        strTime = "सकाळी ";
//        if(hourOfDay < 17)
//            strTime = "दुपारी ";
//        else if(hourOfDay < 19)
//            strTime = "संध्याकाळी ";
//        else
//            strTime = "रात्री ";
//        try {
//            Date date1 = sdf3.parse(strGRTTime);
//            strGRTTime = strTime + sdf4.format(date1);
//        } catch (ParseException e) {
////            throw new RuntimeException(e);
//        }
//        canvas.drawText(String.format("%s", strGRTTime), iColOffset, iRowOffset, text_paint);
//        iRowOffset += iRowHt;
////bmp 20-Aug-25
////        canvas.drawText(String.format("%s", "लोन ओरिएंटेशनची तारीख"), 0, iRowOffset, text_paint);
////        String strLoanDisbursementDate = mainActivity.selectedRevisitGroup.LoanDisbursementDate;
////        try {
////            Date date1 = sdf1.parse(mainActivity.selectedRevisitGroup.LoanDisbursementDate);
////            strLoanDisbursementDate = sdf2.format(date1);
////        } catch (ParseException e) {
//////            throw new RuntimeException(e);
////        }
////        canvas.drawText(String.format("%s", strLoanDisbursementDate), iColOffset, iRowOffset, text_paint);
////        iRowOffset += iRowHt;
////        canvas.drawText(String.format("%s", "लोन ओरिएंटेशनची वेळ"), 0, iRowOffset, text_paint);
////        String strLoanDisbursementTime = mainActivity.selectedRevisitGroup.LoanDisbursementTime;
////        hourOfDay = Integer.valueOf(strLoanDisbursementTime.substring(0, 2));
////        strTime = "सकाळी ";
////        if(hourOfDay < 17)
////            strTime = "दुपारी ";
////        else if(hourOfDay < 19)
////            strTime = "संध्याकाळी ";
////        else
////            strTime = "रात्री ";
////        try {
////            Date date1 = sdf3.parse(strLoanDisbursementTime);
////            strLoanDisbursementTime = strTime + sdf4.format(date1);
////        } catch (ParseException e) {
//////            throw new RuntimeException(e);
////        }
////        canvas.drawText(String.format("%s", strLoanDisbursementTime), iColOffset, iRowOffset, text_paint);
////        iRowOffset += iRowHt;
////bmp 20-Aug-25
////////////////////////////
//        //PDC Table
//        int iCollateralSecurityRequired = 0;
//        int iJointAccountPDCRequired = 0;
//        int iGuarantorPDCRequired = 0;
//        if(iLoanAmount <= 200000) {
//            iCollateralSecurityRequired = 0;
//            iJointAccountPDCRequired = 0;
//            iGuarantorPDCRequired = 0;
//        } else if(iLoanAmount <= 350000) {
//            iCollateralSecurityRequired = 2;
//            iJointAccountPDCRequired = 0;
//            iGuarantorPDCRequired = 0;
//        } else {
//            iCollateralSecurityRequired = 13;
//            iJointAccountPDCRequired = 2;
//            iGuarantorPDCRequired = 2;
//        }
//
//        iRowOffset += 10;
//        canvas.drawText(String.format("%s", "लोन ओरिएंटेशनच्या दिवशी जमा करण्याचे PDCs"), 0, iRowOffset, text_paint);
//        iRowOffset += 10;
//
//        iTblOffset = iRowOffset;
//        iRowOffset = iTblOffset - iRowHt;
//        //Horizontal lines
//        for(int i=0; i<4; i++) {
//            iRowOffset += iRowHt;
//            canvas.drawLine(0, iRowOffset, 575, iRowOffset, border_paint);
//        }
//        iColOffset = 0;
//        //Vertical lines
//        int iColWd4[] = { 410, 165 };
//        canvas.drawLine(iColOffset, iTblOffset, iColOffset, iRowOffset, border_paint);
//        for(int i=0; i<2; i++) {
//            iColOffset += iColWd4[i];
//            canvas.drawLine(iColOffset, iTblOffset, iColOffset, iRowOffset, border_paint);
//        }
//
//        iColOffset = iColWd4[0] + 5;
//        iRowOffset = iTblOffset + 25;
//        canvas.drawText(String.format("%s", "सभासदाने जमा करण्याचे PDCs"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", String.valueOf(iCollateralSecurityRequired)), iColOffset, iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//        canvas.drawText(String.format("%s", "सहकर्जदाराने जमा करण्याचे PDCs"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", String.valueOf(iJointAccountPDCRequired)), iColOffset, iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//        canvas.drawText(String.format("%s", "जामीनदाराने जमा करण्याचे PDCs"), 0, iRowOffset, text_paint);
//        canvas.drawText(String.format("%s", String.valueOf(iGuarantorPDCRequired)), iColOffset, iRowOffset, text_paint);
//        iRowOffset += iRowHt;
////////////////////////////
//        iRowOffset += 10;
//        canvas.drawText(String.format("%s", "लोन ओरिएंटेशन झाल्यावर कर्ज रक्कम सभासदाच्या बँक खात्यात"), 0, iRowOffset, text_paint);
//        iRowOffset += iRowHt - 10;
//        canvas.drawText(String.format("%s", "अंदाजे 3 ते 5 दिवसांनी जमा होणार."), 0, iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//
//        canvas.drawText(String.format("%s", "वैयक्तिक माहिती(पीआयएफ) भरताना सभासदाने कर्ज परतफेडीचा"), 0, iRowOffset, text_paint);
//        iRowOffset += iRowHt - 10;
//        canvas.drawText(String.format("%s", "कालावधी नक्की करायचा आहे. त्यानंतर गटचाचणी, ओरिएंटेशनच्या"), 0, iRowOffset, text_paint);
//        iRowOffset += iRowHt - 10;
//        canvas.drawText(String.format("%s", "वेळी सभासदाला कर्ज कालावधी बदलता येणार नाही."), 0, iRowOffset, text_paint);
//        iRowOffset += iRowHt;
//
//        //एल. एस. ओ.
//        iRowOffset += iRowHt;
//        canvas.drawText(String.format("%s", "एल.एस.ओ. ची सही"), 0, iRowOffset, text_paint);
////bmp 07-Jun-25
//        canvas.drawText(String.format("%s", "सभासदाची सही"), 350, iRowOffset, text_paint);
////bmp 07-Jun-25
//        iRowOffset += iRowHt - 10;
//        canvas.drawText(String.format("%s", mainActivity.LoginID), 0, iRowOffset, text_paint);
//        iRowOffset += iRowHt - 10;
//        canvas.drawLine(0, iRowOffset, 575, iRowOffset, border_paint);
//        System.out.println("iRowOffset= " + String.valueOf(iRowOffset));
//    }

    public void DisableNextPrintTillUserConfirm() {
        flagDisableNextPrint = true;
        CreateAlertDialog(true, false);
        dlgState = ufin_REGROUP_DIALOG_STATE_RESUME_PRINTING;
        dlgAlert.setMessage(MSG_ufin_REGROUP_RESUME_PRINTING);
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
    void ShowToastMessage(String strMsg) {
        LayoutInflater inflator = getLayoutInflater();
        View layout = inflator.inflate(R.layout.custom_toast, rootView.findViewById(R.id.custom_toast_container));
        TextView messageTextView = layout.findViewById(R.id.custom_toast_text);
        messageTextView.setText(strMsg);
        messageTextView.setTextSize(26);
        Toast toast = new Toast(mainActivity.getBaseContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
//    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    // Add same code that you want to add in onActivityResult method
//                    String strFilepath = mainActivity.baseDir + "/data/";
//                    String strFilename = "";
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        switch (IDRequest) {
//                            case ID_REQUEST_PHOTO_OutgoingMemberUndertaking1:
//                                strFilename = "undertaking1_photo";
//                                File outFile = new File(strFilepath + strFilename + ".jpg");
//                                if(outFile.exists()) {
//                                    if(outFile.length() >= MAX_PHOTO_FILESIZE) {
//                                        ShowdlgAlertonActivityResult("Incorrect Image file size, Adjust Camera Setting and Try Again!");
//                                        break;
//                                    }
//                                    //Save filename as Group Document
//                                    selectedRevisitGroup.filenameOutgoingMemberUndertaking1 = strFilename;
//                                }
//                                break;
//                            case ID_REQUEST_PHOTO_OutgoingMemberUndertaking2:
//                                strFilename = "undertaking2_photo";
//                                outFile = new File(strFilepath + strFilename + ".jpg");
//                                if(outFile.exists()) {
//                                    if(outFile.length() >= MAX_PHOTO_FILESIZE) {
//                                        ShowdlgAlertonActivityResult("Incorrect Image file size, Adjust Camera Setting and Try Again!");
//                                        break;
//                                    }
//                                    //Save filename as Group Document
//                                    selectedRevisitGroup.filenameOutgoingMemberUndertaking2 = strFilename;
//                                }
//                                break;
//                        }
//                    } else if(result.getResultCode() == RESULT_CANCELED){
//                        //handle cancel
//                        ShowdlgAlertonActivityResult("CAMERA REQUEST CANCELED!");
//                    }
//                }
//            });
//
//    void ShowdlgAlertonActivityResult(String strMessage) {
//        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(mainActivity.getApplicationContext());
//        TextView tvTitle = new TextView(mainActivity.getApplicationContext());
//        tvTitle.setText(getString(R.string.app_name));
//        tvTitle.setTextSize(30);
//        tvTitle.setTextColor(getResources().getColor(R.color.colorAccent));
//        dlgAlert.setCustomTitle(tvTitle);
//        dlgAlert.setIcon(R.drawable.ic_launcher_foreground);
//        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // User clicked OK button
//                dialog.dismiss();
//            }
//        });
//        dlgAlert.setCancelable(true);
//
//        dlgAlert.setMessage(strMessage);
//        AlertDialog dialog = dlgAlert.create();
//        dialog.show();
//        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
//        textView.setTextSize(30);
//    }
}
