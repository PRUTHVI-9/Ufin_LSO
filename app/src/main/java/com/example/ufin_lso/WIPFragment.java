package com.example.ufin_lso;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WIPFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WIPFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View rootView;
    MainActivity mainActivity;

    public static final int ufin_REVISIT_DIALOG_STATE_NONE                          = 0;
    public static final int ufin_REVISIT_DIALOG_STATE_CLOSE                         = 1;

    public static final String MSG_ufin_REVISIT_CONFIRM_CLOSE =
            "Press OK to Close Without Saving Changes";

    public AlertDialog.Builder dlgAlert;
    public static int dlgState = ufin_REVISIT_DIALOG_STATE_NONE;;

    int selectedWIPGroupPosition;
    long selectedWIPGroupID;
    String selectedReasonToRemove;

    public WIPFragment() {
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
    public static WIPFragment newInstance(String param1, String param2) {
        WIPFragment fragment = new WIPFragment();
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
//        return inflater.inflate(R.layout.fragment_wip, container, false);
        rootView = inflater.inflate(R.layout.fragment_wip, container, false);
        mainActivity = (MainActivity) getActivity();

        ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarWIP);
        if (pbSync != null) {
            pbSync.setIndeterminate(true);
            pbSync.setVisibility(View.VISIBLE);
        }

        Button bt = (Button) rootView.findViewById(R.id.buttonCloseWIP);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonRemoveWIP);
        bt.setOnClickListener(this);

        InitWIP();

        if (pbSync != null) {
            pbSync.setIndeterminate(true);
            pbSync.setVisibility(View.INVISIBLE);
        }

        return rootView;
    }

    public void InitWIP() {
        mainActivity.strCurrentScreen = getString(R.string.app_name) + " LSO: " + mainActivity.LSO_NodeName;
        mainActivity.setAppActionbar();

        Button bt = (Button) rootView.findViewById(R.id.buttonRemoveWIP);
        bt.setEnabled(false);

        TableLayout tlStatus = (TableLayout) rootView.findViewById(R.id.tableLayoutWIP);
        tlStatus.removeAllViews();

        selectedWIPGroupPosition = -1;
        selectedWIPGroupID = -1;
        selectedReasonToRemove = "";

        String[] strReasonToRemove = getResources().getStringArray(R.array.WIP_ReasonToRemove_array);
        ArrayAdapter<String> adapterReasonToRemove = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, strReasonToRemove);
        adapterReasonToRemove.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner spReasonToRemove = (Spinner) rootView.findViewById(R.id.spinnerReasonToRemove);
        spReasonToRemove.setAdapter(adapterReasonToRemove);
        spReasonToRemove.setSelection(0);
        spReasonToRemove.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] strReasonToRemove_Shivaji01 = getResources().getStringArray(R.array.WIP_ReasonToRemove_array_Shivaji01);
                selectedReasonToRemove = strReasonToRemove_Shivaji01[position];
                if(selectedReasonToRemove.isEmpty()) {
                    Button bt = (Button) rootView.findViewById(R.id.buttonRemoveWIP);
                    bt.setEnabled(false);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spReasonToRemove.setFocusable(true);
        spReasonToRemove.setFocusableInTouchMode(true);

        TextView tvHdrGroupID = new TextView(mainActivity.getApplicationContext());
        tvHdrGroupID.setTextColor(Color.BLACK);
        tvHdrGroupID.setTextSize(24);
        tvHdrGroupID.setText("GroupID");
        tvHdrGroupID.setTypeface(Typeface.DEFAULT_BOLD);
        tvHdrGroupID.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tvHdrGroupID.setGravity(Gravity.CENTER_HORIZONTAL);
        tvHdrGroupID.setPadding(10, 0, 10, 0);

        TextView tvHdrMember = new TextView(mainActivity.getApplicationContext());
        tvHdrMember.setTextColor(Color.BLACK);
        tvHdrMember.setTextSize(24);
        tvHdrMember.setText("Member");
        tvHdrMember.setTypeface(Typeface.DEFAULT_BOLD);
        tvHdrMember.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.5F));
        tvHdrMember.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView tvHdrLoan = new TextView(mainActivity.getApplicationContext());
        tvHdrLoan.setTextColor(Color.BLACK);
        tvHdrLoan.setTextSize(24);
        tvHdrLoan.setText("Loan");
        tvHdrLoan.setTypeface(Typeface.DEFAULT_BOLD);
        tvHdrLoan.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tvHdrLoan.setGravity(Gravity.CENTER_HORIZONTAL);
        tvHdrLoan.setPadding(10, 0, 10, 0);

        TextView tvHdrStatus = new TextView(mainActivity.getApplicationContext());
        tvHdrStatus.setTextColor(Color.BLACK);
        tvHdrStatus.setTextSize(24);
        tvHdrStatus.setText("Status");
        tvHdrStatus.setTypeface(Typeface.DEFAULT_BOLD);
        tvHdrStatus.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.0F));
        tvHdrStatus.setGravity(Gravity.CENTER_HORIZONTAL);

        TableRow tblRowHdrStatus = new TableRow(mainActivity.getApplicationContext());
        tblRowHdrStatus.addView(tvHdrGroupID);
        tblRowHdrStatus.addView(tvHdrMember);
        tblRowHdrStatus.addView(tvHdrLoan);
        tblRowHdrStatus.addView(tvHdrStatus);
        tblRowHdrStatus.setBackground(mainActivity.getDrawable(R.drawable.customborder));
        tlStatus.addView(tblRowHdrStatus);

        mainActivity.UpdateWIP();

        for(int j=0; j<mainActivity.arrlistWIPGroupID.size(); j++) {
            LinearLayout llv = new LinearLayout(mainActivity.getBaseContext());
            llv.setOrientation(LinearLayout.VERTICAL);
            llv.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            for(int i=0; i<mainActivity.RevisitMemberList.size(); i++) {
                if(mainActivity.RevisitMemberList.get(i).GroupID != mainActivity.arrlistWIPGroupID.get(j))
                    continue;
                TextView tvGroupID = new TextView(mainActivity.getApplicationContext());
                tvGroupID.setTextColor(Color.BLACK);
                tvGroupID.setTextSize(24);
                tvGroupID.setTypeface(Typeface.DEFAULT_BOLD);
                tvGroupID.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.5F));
                tvGroupID.setGravity(Gravity.CENTER_HORIZONTAL);
                tvGroupID.setPadding(10, 0, 10, 0);

                TextView tvMember = new TextView(mainActivity.getApplicationContext());
                tvMember.setTextColor(Color.BLACK);
                tvMember.setTextSize(24);
                tvMember.setTypeface(Typeface.DEFAULT_BOLD);
                tvMember.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0F));

                TextView tvLoan = new TextView(mainActivity.getApplicationContext());
                tvLoan.setTextColor(Color.BLACK);
                tvLoan.setTextSize(24);
                tvLoan.setTypeface(Typeface.DEFAULT_BOLD);
                tvLoan.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.5F));
                tvLoan.setGravity(Gravity.CENTER_HORIZONTAL);
                tvLoan.setPadding(10, 0, 10, 0);

                TextView tvStatus = new TextView(mainActivity.getApplicationContext());
                tvStatus.setTextColor(Color.BLACK);
                tvStatus.setTextSize(24);
                tvStatus.setTypeface(Typeface.DEFAULT_BOLD);
                tvStatus.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.2F));
                tvStatus.setGravity(Gravity.CENTER_HORIZONTAL);
                String strGroupID = String.valueOf(mainActivity.RevisitMemberList.get(i).GroupID);
                if(strGroupID.length() >= 17) {
                    strGroupID = strGroupID.substring(0, (strGroupID.length() - 8));
                    tvGroupID.setTextSize(16);
                }
                tvGroupID.setText(strGroupID);
                tvMember.setText(String.valueOf(mainActivity.RevisitMemberList.get(i).MemberName));
//                if(mainActivity.RevisitMemberList.get(i).VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) {
                if(     (mainActivity.RevisitMemberList.get(i).VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) ||
                        (mainActivity.RevisitMemberList.get(i).VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) ||
                        (mainActivity.RevisitMemberList.get(i).VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED) ) {
                    tvLoan.setText("Yes");
                } else if(mainActivity.RevisitMemberList.get(i).VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING) {
                    tvLoan.setText("No");
                }
                switch (mainActivity.RevisitMemberList.get(i).RevisitMemberStatus) {
                    case clsRevisitMember.REVISIT_MEMBER_STATUS_INIT:
                        tvStatus.setText("Initialized");
                        break;
                    case clsRevisitMember.REVISIT_MEMBER_STATUS_SCHEDULED:
                        tvStatus.setText("Scheduled");
                        break;
                    case clsRevisitMember.REVISIT_MEMBER_STATUS_REVISITED:
                        tvStatus.setText("Punarbhet");
                        break;
                    case clsRevisitMember.REVISIT_MEMBER_STATUS_REGROUPED:
                        tvStatus.setText("Regroup");
                        break;
                    case clsRevisitMember.REVISIT_MEMBER_STATUS_READYGROUP:
                        tvStatus.setText("Punarbandhani-1");
                        break;
                    case clsRevisitMember.REVISIT_MEMBER_STATUS_GROUPFORM_SUBMITTED:
                        tvStatus.setText("GF Submitted");
                        break;
                    case clsRevisitMember.REVISIT_MEMBER_STATUS_PIF_SUBMITTED:
                        tvStatus.setText("PIF Submitted");
                        break;
                    case clsRevisitMember.REVISIT_MEMBER_STATUS_QUERY:
                        tvStatus.setText("PIF/Doc Query");
                        break;
                    default:
                        tvStatus.setText(String.valueOf(mainActivity.RevisitMemberList.get(i).RevisitMemberStatus));
                        break;
                }

                TableRow tblRowStatus = new TableRow(mainActivity.getApplicationContext());
                tblRowStatus.addView(tvGroupID);
                tblRowStatus.addView(tvMember);
                tblRowStatus.addView(tvLoan);
                tblRowStatus.addView(tvStatus);

                llv.addView(tblRowStatus);
            }

            llv.setBackground(mainActivity.getDrawable(R.drawable.customborder));
            llv.setId(j);
            llv.setEnabled(true);
            llv.setClickable(true);
            llv.setOnClickListener(groupWIPOnClickListener);
            tlStatus.addView(llv);
        }
    }
    private View.OnClickListener groupWIPOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(selectedWIPGroupPosition >= 0) {
                View vPrev = (View) rootView.findViewById(selectedWIPGroupPosition);
                vPrev.setBackgroundColor(Color.WHITE);
            }
            int iTemp = v.getId();
            if(selectedWIPGroupPosition == iTemp) {
                selectedWIPGroupPosition = -1;
                selectedWIPGroupID = -1;
                selectedReasonToRemove = "";
                Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerReasonToRemove);
                sp.setSelection(0);
            } else {
                boolean flagReadyOrOKGroup = false;
                String strMsg = "";
                for (int i = 0; i < mainActivity.SyncGroupStatusUpdate.size(); i++) {
                    if(mainActivity.arrlistWIPGroupID.get(iTemp) == mainActivity.SyncGroupStatusUpdate.get(i).GroupID) {
                        if(mainActivity.SyncGroupStatusUpdate.get(i).Status == clsNewGroup.GROUP_STATUS_READY_FOR_GRT) {
                            flagReadyOrOKGroup = true;
                            strMsg = "GroupID: " + String.valueOf(mainActivity.SyncGroupStatusUpdate.get(i).GroupID) + "\n";
                            strMsg = strMsg + "Group is Ready For GRT. Can not send Remove Request.";
                        } else if(mainActivity.SyncGroupStatusUpdate.get(i).Status == clsNewGroup.GROUP_STATUS_GRT_OK) {
                            flagReadyOrOKGroup = true;
                            strMsg = "GroupID: " + String.valueOf(mainActivity.SyncGroupStatusUpdate.get(i).GroupID) + "\n";
                            strMsg = strMsg + "Group is GRT OK. Can not send Remove Request.";
                        }
                        break;
                    }
                }
                if(flagReadyOrOKGroup) {
                    ShowToastMessage(strMsg);
                } else {
                    v.setBackgroundColor(Color.CYAN);
                    selectedWIPGroupPosition = iTemp;
                    selectedWIPGroupID = mainActivity.arrlistWIPGroupID.get(iTemp);
                }
            }

            Button bt = (Button) rootView.findViewById(R.id.buttonRemoveWIP);
            bt.setEnabled(false);
            if( (selectedWIPGroupID > 0) && (!selectedReasonToRemove.isEmpty()) ) {
                bt.setEnabled(true);
            }
        }
    };

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
                        case ufin_REVISIT_DIALOG_STATE_NONE:
                            break;
                    }
                    dlgState = ufin_REVISIT_DIALOG_STATE_NONE;
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
                        case ufin_REVISIT_DIALOG_STATE_NONE:
                            break;
                        case ufin_REVISIT_DIALOG_STATE_CLOSE:
                            mainActivity.InitDesktop();
                            break;
                    }
                    dlgState = ufin_REVISIT_DIALOG_STATE_NONE;
                }
            });
        }
//        dlgAlert.setCancelable(true);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCloseWIP:
                mainActivity.InitDesktop();
                break;
            case R.id.buttonRemoveWIP:
                long LastTimeInMillis;
                Calendar cal = Calendar.getInstance();
                LastTimeInMillis = cal.getTimeInMillis();
                if( (selectedWIPGroupID > 0) && (!selectedReasonToRemove.isEmpty()) ) {
                    ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarWIP);
                    if (pbSync != null) {
                        pbSync.setIndeterminate(true);
                        pbSync.setVisibility(View.VISIBLE);
                    }
                    boolean flagRemoveLocally = true;
                    for(int i=0; i<mainActivity.RevisitMemberList.size(); i++) {
                        if (mainActivity.RevisitMemberList.get(i).GroupID == selectedWIPGroupID) {
                            if(mainActivity.RevisitMemberList.get(i).RevisitMemberStatus >= clsRevisitMember.REVISIT_MEMBER_STATUS_GROUPFORM_SUBMITTED) {
                                flagRemoveLocally = false;
                                break;
                            }
                        }
                    }
                    if(flagRemoveLocally) {
                        int iMemberCount = 0;
                        for (int i = 0; i < mainActivity.RevisitMemberList.size(); i++) {
                            if (mainActivity.RevisitMemberList.get(i).GroupID == selectedWIPGroupID) {
                                mainActivity.RevisitMemberList.get(i).RevisitMemberStatus = clsRevisitMember.REVISIT_MEMBER_STATUS_REVISITED;
                                mainActivity.SQLite_updateRevisitMember(i, false);
                                iMemberCount++;
                            }
                        }

                        mainActivity.newGroup.pathDocuments = mainActivity.baseDir + "/data";
                        mainActivity.newGroup.InitNewGroup();
                        mainActivity.newGroup.MemberCount = iMemberCount;
                        mainActivity.newGroup.BranchNo = mainActivity.LSO_BranchNo;
                        mainActivity.newGroup.BranchName = mainActivity.LSO_Branch;
                        mainActivity.selectedGroupStatusPosition = mainActivity.GROUP_TYPE_LOAN_INPROCESS;
                        mainActivity.selectedGroupID = selectedWIPGroupID;
                        mainActivity.SQLite_getGroupMembersInfo();
                        for (int k = 0; k < mainActivity.newGroup.MemberCount; k++) {
                            clsNewGroupMemberInfo newGroupMemberInfo = new clsNewGroupMemberInfo();
                            newGroupMemberInfo.Init_NewGroupMemberInfo();
                            newGroupMemberInfo.MemberID = mainActivity.newGroup.arrGroupMember.get(k).MemberID;
                            newGroupMemberInfo.GuarantorName = mainActivity.newGroup.arrGroupMember.get(k).GuarantorName;
                            newGroupMemberInfo.GuarantorRelationID = mainActivity.newGroup.arrGroupMember.get(k).GuarantorRelationID;
                            newGroupMemberInfo.LoanAmount = mainActivity.newGroup.arrGroupMember.get(k).LoanAmount;
                            newGroupMemberInfo.LoanDuration = mainActivity.newGroup.arrGroupMember.get(k).LoanDuration;
                            newGroupMemberInfo.LoanApplicant = mainActivity.newGroup.arrGroupMember.get(k).LoanApplicant;
                            newGroupMemberInfo.ContactNo = mainActivity.newGroup.arrGroupMember.get(k).ContactNo;
                            newGroupMemberInfo.GroupID = mainActivity.newGroup.arrGroupMember.get(k).GroupID;

                            newGroupMemberInfo.MemberStatus = clsGroupMember.MEMBER_STATUS_CREATED;
                            LastTimeInMillis += 1000;
                            mainActivity.SQLite_saveGroupMemberInfoWIP(newGroupMemberInfo, LastTimeInMillis, true);
                        }
                        clsNewGroupInfo newGroupInfo = new clsNewGroupInfo();
                        newGroupInfo.Init_NewGroupInfo();
                        newGroupInfo.GroupID = mainActivity.newGroup.lGroupID;
                        newGroupInfo.LoanDisbursementDate = mainActivity.newGroup.LoanDisbursementDate;
                        newGroupInfo.LoanDisbursementTime = mainActivity.newGroup.LoanDisbursementTime;
                        newGroupInfo.LoanRepayInstallmentDate = mainActivity.newGroup.LoanRepayInstallmentDate;
                        newGroupInfo.LoanRepayInstallmentTime = mainActivity.newGroup.LoanRepayInstallmentTime;
                        newGroupInfo.PIFDate = mainActivity.newGroup.PIFDate;
                        newGroupInfo.PIFTime = mainActivity.newGroup.PIFTime;
                        newGroupInfo.GroupTestDate = mainActivity.newGroup.GroupTestDate;
                        newGroupInfo.GroupTestTime = mainActivity.newGroup.GroupTestTime;
                        newGroupInfo.CollectionCenterID = mainActivity.newGroup.lCenterID;
                        newGroupInfo.CollectionMemberID = mainActivity.newGroup.CollectionMemberID;
                        newGroupInfo.MemberCount = mainActivity.newGroup.MemberCount;

                        newGroupInfo.GroupStatus = clsNewGroup.GROUP_STATUS_CREATED;
                        LastTimeInMillis += 1000;
                        mainActivity.SQLite_saveNewGroupInfo(newGroupInfo, mainActivity.LSO_ID, LastTimeInMillis, true);
                    } else {
//bmp 16-Mar-26
                        String strMsg = "";
                        for (int i = 0; i < mainActivity.SyncGroupStatusUpdate.size(); i++) {
                            if(selectedWIPGroupID == mainActivity.SyncGroupStatusUpdate.get(i).GroupID) {
                                if(mainActivity.SyncGroupStatusUpdate.get(i).Status == clsNewGroup.GROUP_STATUS_READY_FOR_GRT) {
                                    strMsg = "GroupID: " + String.valueOf(selectedWIPGroupID) + "\n";
                                    strMsg = strMsg + "Group is Ready For GRT. Can not send Remove Request.";
                                } else if(mainActivity.SyncGroupStatusUpdate.get(i).Status == clsNewGroup.GROUP_STATUS_GRT_OK) {
                                    strMsg = "GroupID: " + String.valueOf(selectedWIPGroupID) + "\n";
                                    strMsg = strMsg + "Group is GRT OK. Can not send Remove Request.";
                                }
                                break;
                            }
                        }
                        if(strMsg.isEmpty()) {
//bmp 16-Mar-26
                            JSONObject postDataParams = new JSONObject();
                            try {
                                postDataParams.put(ufinDbContract.NewGroupInfo.COLUMN_NAME_GroupID, selectedWIPGroupID);

                                postDataParams.put("ReasonToRemove", selectedReasonToRemove);
                                postDataParams.put("LSO_ID", mainActivity.LSO_ID);
                                postDataParams.put("Org_ID", mainActivity.OrgID);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String strDataParamsQuery = "";
                            try {
                                StringBuilder sb = new StringBuilder();
                                Iterator<String> keys = postDataParams.keys();
                                sb.append("?"); //start of query args
                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    sb.append(key);
                                    sb.append("=");
                                    sb.append(postDataParams.get(key));
                                    sb.append("&"); //To allow for another argument.
                                }
                                strDataParamsQuery = sb.toString();
                                strDataParamsQuery = strDataParamsQuery.substring(0, (strDataParamsQuery.length() - 1));
                                strDataParamsQuery = strDataParamsQuery.replaceAll(" ", "%20");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
                            // Gets the data repository in write mode
                            SQLiteDatabase db = mDbHelper.getWritableDatabase();

//                        Calendar cal = Calendar.getInstance();
                            Date date = cal.getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
                            String strTimeStamp = sdf.format(date);// all done

                            ContentValues values2 = new ContentValues();
                            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_SyncID, strTimeStamp);
                            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_DbTableName, "ReasonToRemoveWIP");
                            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_updatedRecordID, selectedWIPGroupID);
                            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsNewRecord, "True");
                            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsReady, "True");
                            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_PostDataParams, strDataParamsQuery);

                            long lTemp1 = 0;
                            lTemp1 = db.insert(ufinDbContract.RecordsToSync.TABLE_NAME, null, values2);
                            if (lTemp1 > 0) {
                                MainActivity.clsRecordsToSync newRec = new MainActivity.clsRecordsToSync();
                                newRec.SyncID = strTimeStamp;
                                newRec.recordID = selectedWIPGroupID;
                                newRec.recordInserted = true;
                                newRec.recordReady = true;
                                newRec.PostParams = strDataParamsQuery;
                                mainActivity.SyncReasonToRemoveWIP.add(newRec);
                                File packetFile = new File(mainActivity.baseDir + "/config/" + mainActivity.strPacketFile);
                                try {
                                    BufferedWriter bwr = new BufferedWriter(new FileWriter(packetFile, true));
                                    bwr.newLine();
                                    bwr.newLine();
                                    bwr.write(strDataParamsQuery);
                                    bwr.flush();
                                    bwr.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("RecordsToSync Updated.");
                            }
                            ShowToastMessage("Remove Request Sent for Approval.");
//bmp 16-Mar-26
                        } else {
                            ShowToastMessage(strMsg);
                        }
//bmp 16-Mar-26
                    }

                    if (pbSync != null) {
                        pbSync.setIndeterminate(true);
                        pbSync.setVisibility(View.INVISIBLE);
                    }

                    InitWIP();
                }
                break;
        }
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
}
