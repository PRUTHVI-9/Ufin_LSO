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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewgroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewgroupFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View rootView;
    MainActivity mainActivity;

    public static final int ufin_NEWGROUP_DIALOG_STATE_NONE                  = 0;
    public static final int ufin_NEWGROUP_DIALOG_STATE_CLOSE                 = 1;
    public static final int ufin_DIALOG_STATE_VALIDATE_NEWGROUP_SCREEN       = 22;
    public static final int ufin_DIALOG_STATE_CONFIRM_NEWGROUP_SCREEN        = 23;
    public static final int ufin_NEWGROUP_DIALOG_STATE_RESUME_PRINTING       = 6;
    public static final int ufin_NEWGROUP_DIALOG_STATE_NO_PRINTER            = 7;
    public static final int ufin_NEWGROUP_DIALOG_STATE_CBCHECK_DOC_UPLOAD    = 8;
    public static final int ufin_NEWGROUP_DIALOG_STATE_ACTIVE_LOAN_GROUP     = 9;
    public static final int ufin_NEWGROUP_DIALOG_STATE_CONFIRM_PRINT_BUTTON  = 10;
//bmp 21-Aug-26
    public static final String MSG_ufin_NEWGROUP_CONFIRM_PRINT_BUTTON =
//bmp 31-Aug-26
//        "Press OK to Continue Print.\nPress Cancel to Abort.";
        "To Print: Check Printer is Connected,\nthen Press OK to Continue Print.\nPress Cancel to Abort.";
//bmp 31-Aug-26
//bmp 21-Aug-26

    public static final String MSG_ufin_CBCHECK_DOC_UPLOAD =
            "CB Check Document sent to Server.";
    public static final String MSG_ufin_NEWGROUP_NO_PRINTER =
            "Printer Not Connected.\nPress Cancel and Retry.";
    public static final String MSG_ufin_NEWGROUP_RESUME_PRINTING =
            "After printing is complete, Press OK to Resume Printing";

//    public static final String MSG_ufin_REVISIT_CONFIRM_CLOSE =
//            "Press OK to Close Without Saving Changes";
    public static final String MSG_ufin_NEWGROUP_CONFIRM_SAVE =
            "Press OK to Confirm Save New Group.";

    public AlertDialog.Builder dlgAlert;
    public static int dlgState = ufin_NEWGROUP_DIALOG_STATE_NONE;;
    ArrayList<String> NewgroupProblemsCheck = new ArrayList<String>();
    int selectedNoloangroupMemberPosition;
    long selectedNoloangroupMemberID;
    long selectedNoLoanMemberID;
    int selectedNewgroupMemberPosition;
    long selectedNewgroupMemberID;
    ArrayList<Long> LSO_ClusterCommunityID = new ArrayList<Long>();
    ArrayList<String> LSO_ClusterCommunityName = new ArrayList<String>();
    private ArrayList<Integer> ReasonIDMaster = new ArrayList<Integer>();
    private ArrayList<String> ReasonMarathiDescriptionMaster = new ArrayList<String>();
    clsRevisitGroup selectedRevisitGroup, originalRevisitGroup;
    long selectedNoLoanGroupID;
    long LastTimeInMillis;

    public static final int MAX_PHOTO_FILESIZE             = (4 * 1024 * 1024);
//    public static final int MAX_PHOTO_FILESIZE             = (8 * 1024 * 1024);
    int IDRequest = -1;
    String strFilepath = "";
    String strFilename = "unknown";
    boolean flagDisableNextPrint;
    boolean ConfirmPrint;
    int iPrintButton;
    int iIDindex;

    public NewgroupFragment() {
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
    public static NewgroupFragment newInstance(String param1, String param2) {
        NewgroupFragment fragment = new NewgroupFragment();
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
//        return inflater.inflate(R.layout.fragment_newgroup, container, false);
//        rootView = inflater.inflate(R.layout.fragment_newgroup, container, false);
        rootView = inflater.inflate(R.layout.fragment_newgroup2, container, false);
        mainActivity = (MainActivity) getActivity();
        mainActivity.SQLite_getPunarbhetVisitDetailsMaster();

        Calendar cal = Calendar.getInstance();
        LastTimeInMillis = cal.getTimeInMillis();

        ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarNewgroup);
        if (pbSync != null) {
            pbSync.setIndeterminate(true);
            pbSync.setVisibility(View.VISIBLE);
        }

        ConfirmPrint = false;
        iPrintButton = -1;
        iIDindex = -1;

        Button bt = (Button) rootView.findViewById(R.id.buttonCloseNewgroup);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonSaveNewgroup);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonRemoveNewgroup);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonAddNewgroup);
        bt.setOnClickListener(this);

        bt = (Button) rootView.findViewById(R.id.buttonSaveNewgroup);
        bt.setEnabled(false);

        mainActivity.selectedClusterID = -1;
        mainActivity.selectedCommunityID = -1;
        mainActivity.selectedGroupID = -1;
        selectedNewgroupMemberPosition = -1;
        selectedNewgroupMemberID = -1;
        selectedNoloangroupMemberPosition = -1;
        selectedNoloangroupMemberID = -1;
        selectedRevisitGroup = new clsRevisitGroup();
        selectedRevisitGroup.InitRevisitGroup();

        ArrayAdapter<String> adapterLSO_Cluster = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, mainActivity.LSO_ClusterName);
        adapterLSO_Cluster.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner spSelectCluster = (Spinner) rootView.findViewById(R.id.spinnerCluster);
        spSelectCluster.setAdapter(adapterLSO_Cluster);
        spSelectCluster.setSelection(0);
        spSelectCluster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                    return;

                for(int i=0; i<selectedRevisitGroup.MemberCount; i++) {
                    mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i)).RevisitMemberStatus
                            = clsRevisitMember.REVISIT_MEMBER_STATUS_REVISITED;
                }

                Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerCommunity);
                sp.setSelection(0);

                mainActivity.selectedClusterID = -1;
                mainActivity.selectedCommunityID = -1;
                mainActivity.selectedGroupID = -1;
                selectedNewgroupMemberPosition = -1;
                selectedNewgroupMemberID = -1;
                selectedNoloangroupMemberPosition = -1;
                selectedNoloangroupMemberID = -1;
                selectedRevisitGroup = new clsRevisitGroup();
                selectedRevisitGroup.InitRevisitGroup();

                mainActivity.selectedClusterID = mainActivity.LSO_ClusterID.get(position);
                RefreshCluster_Community();
                RefreshNewGroupMembers();
                RefreshReadyMemberListClusterCommunity();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spSelectCluster.setFocusable(true);
        spSelectCluster.setFocusableInTouchMode(true);

        RefreshCluster_Community();

        bt = (Button) rootView.findViewById(R.id.buttonAddNewgroup);
        bt.setEnabled(false);
        bt = (Button) rootView.findViewById(R.id.buttonRemoveNewgroup);
        bt.setEnabled(false);
        bt = (Button) rootView.findViewById(R.id.buttonSaveNewgroup);
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
                        case ufin_NEWGROUP_DIALOG_STATE_NONE:
                            dlgState = ufin_NEWGROUP_DIALOG_STATE_NONE;
                            break;
                    }
//                    dlgState = ufin_NEWGROUP_DIALOG_STATE_NONE;
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
                        case ufin_NEWGROUP_DIALOG_STATE_NONE:
                        case ufin_DIALOG_STATE_VALIDATE_NEWGROUP_SCREEN:
                        case ufin_NEWGROUP_DIALOG_STATE_NO_PRINTER:
                            dlgState = ufin_NEWGROUP_DIALOG_STATE_NONE;
                            break;
                        case ufin_NEWGROUP_DIALOG_STATE_CLOSE:
                            dlgState = ufin_NEWGROUP_DIALOG_STATE_NONE;
                            mainActivity.InitDesktop();
                            break;
                        case ufin_DIALOG_STATE_CONFIRM_NEWGROUP_SCREEN:
                            dlgState = ufin_NEWGROUP_DIALOG_STATE_NONE;
                            AsyncCallWS_SaveNewgroup taskSaveNewgroup = new AsyncCallWS_SaveNewgroup();
                            taskSaveNewgroup.execute();
                            break;
                        case ufin_NEWGROUP_DIALOG_STATE_RESUME_PRINTING:
                            dlgState = ufin_NEWGROUP_DIALOG_STATE_NONE;
                            flagDisableNextPrint = false;
                            break;
                        case ufin_NEWGROUP_DIALOG_STATE_CONFIRM_PRINT_BUTTON:
                            ConfirmPrint = true;
                            Button bt = (Button) rootView.findViewById(iPrintButton);
                            if(bt != null)
                                bt.performClick();
                            break;
                    }
//                    dlgState = ufin_NEWGROUP_DIALOG_STATE_NONE;
                }
            });
        }
//        dlgAlert.setCancelable(true);
    }
    private class AsyncCallWS_SaveNewgroup extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.progressBarNewgroup);
            if (pb != null) {
                pb.setIndeterminate(true);
                pb.setVisibility(View.VISIBLE);
            }

            Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerCluster);
            sp.setEnabled(false);
            sp = (Spinner) rootView.findViewById(R.id.spinnerCommunity);
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

            Button bt = (Button) rootView.findViewById(R.id.buttonCloseNewgroup);
            bt.setEnabled(false);
            bt = (Button) rootView.findViewById(R.id.buttonSaveNewgroup);
            bt.setEnabled(false);
            bt = (Button) rootView.findViewById(R.id.buttonAddNewgroup);
            bt.setEnabled(false);
            bt = (Button) rootView.findViewById(R.id.buttonRemoveNewgroup);
            bt.setEnabled(false);
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
        @Override
        protected Void doInBackground(String... strings) {
            SaveNewgroup();

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.progressBarNewgroup);
            if(pb != null)
                pb.setVisibility(View.INVISIBLE);

            Button bt = (Button) rootView.findViewById(R.id.buttonCloseNewgroup);
            bt.setEnabled(true);
        }
    }
    public void SaveNewgroup() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        String strTimeStamp = sdf.format(date);// all done
        Date date1 = cal.getTime();

        long TimeInMillis = cal.getTimeInMillis();
        TimeInMillis = TimeInMillis + 1000;
        cal.setTimeInMillis(TimeInMillis);
        Date date2 = cal.getTime();

        TimeInMillis = TimeInMillis + 1000;
        cal.setTimeInMillis(TimeInMillis);
        Date date3 = cal.getTime();

        SimpleDateFormat sdf2 = new SimpleDateFormat("MMddHHmmss", Locale.ENGLISH);

        String Str1 = sdf2.format(date1) + String.format("%08d", mainActivity.LSO_ID);
        Str1 = Str1.replaceFirst("^0+(?!$)", "");

        String Str2 = sdf2.format(date2) + String.format("%08d", mainActivity.LSO_ID);
        Str2 = Str2.replaceFirst("^0+(?!$)", "");

        String Str3 = sdf2.format(date3) + String.format("%08d", mainActivity.LSO_ID);
        Str3 = Str3.replaceFirst("^0+(?!$)", "");
        try {
            selectedRevisitGroup.CenterID = Long.parseLong(Str1);
            selectedNoLoanGroupID = Long.parseLong(Str2);
            selectedRevisitGroup.GroupID = Long.parseLong(Str3);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        selectedRevisitGroup.CommunityID = mainActivity.selectedCommunityID;
        selectedRevisitGroup.GroupName = "G-" + String.valueOf(selectedRevisitGroup.GroupID);

        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try {
            //Create new Center
            ContentValues values1 = new ContentValues();
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_ORGStructureID, selectedRevisitGroup.CenterID);
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_NodeName, ("C-" + String.valueOf(selectedRevisitGroup.CenterID)));
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_NodeLevel, 5);
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_ParentID, selectedRevisitGroup.CommunityID);
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_ParentLevel, 4);
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_ChildsAllowed, 3);
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_MarathiName, "NA");
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_BranchNumber, "NA");
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_IsActive, 1);
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_Remark, "New Center Created by LSO");

            JSONObject postDataParams = new JSONObject();
            try {
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_ORGStructureID, selectedRevisitGroup.CenterID);
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_NodeName, ("C-" + String.valueOf(selectedRevisitGroup.CenterID)));
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_NodeLevel, 5);
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_ParentID, selectedRevisitGroup.CommunityID);
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_ParentLevel, 4);
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_ChildsAllowed, 3);
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_MarathiName, "NA");
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_Remark, "New Center Created by LSO");
                postDataParams.put("LSO_ID", mainActivity.LSO_ID);
                postDataParams.put("Org_ID", mainActivity.OrgID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ContentValues values2 = new ContentValues();
            String StrNewRecord = "";
            String StrReady = "";
            long lTemp1 = db.insert(ufinDbContract.OrgStructure.TABLE_NAME, null, values1);
            if (lTemp1 > 0) {
                System.out.println("OrgStructure Inserted.");
                StrNewRecord = "True";
                StrReady = "True";
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
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_SyncID, strTimeStamp);
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_DbTableName, ufinDbContract.OrgStructure.TABLE_NAME);
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_updatedRecordID, selectedRevisitGroup.CenterID);
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsNewRecord, StrNewRecord);
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsReady, StrReady);
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_PostDataParams, strDataParamsQuery);

            lTemp1 = db.insert(ufinDbContract.RecordsToSync.TABLE_NAME, null, values2);
            if (lTemp1 > 0) {
                MainActivity.clsRecordsToSync newRec = new MainActivity.clsRecordsToSync();
                newRec.SyncID = strTimeStamp;
                newRec.recordID = selectedRevisitGroup.CenterID;
                newRec.recordInserted = StrNewRecord.equalsIgnoreCase("True");
                newRec.recordReady = StrReady.equalsIgnoreCase("True");
                newRec.PostParams = strDataParamsQuery;
                mainActivity.SyncOrgStructure.add(newRec);
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

            //Create new NoLoan Group
            values1 = new ContentValues();
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_ORGStructureID, selectedNoLoanGroupID);
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_NodeName, ("C-" + String.valueOf(selectedRevisitGroup.CenterID) + " No Loan Group"));
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_NodeLevel, 6);
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_ParentID, selectedRevisitGroup.CenterID);
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_ParentLevel, 5);
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_ChildsAllowed, 0);
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_MarathiName, "NA");
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_BranchNumber, "NA");
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_IsActive, 1);
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_Remark, "New No Loan Group Created by LSO");

            postDataParams = new JSONObject();
            try {
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_ORGStructureID, selectedNoLoanGroupID);
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_NodeName, ("C-" + String.valueOf(selectedRevisitGroup.CenterID) + " No Loan Group"));
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_NodeLevel, 6);
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_ParentID, selectedRevisitGroup.CenterID);
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_ParentLevel, 5);
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_ChildsAllowed, 0);
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_MarathiName, "NA");
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_Remark, "New No Loan Group Created by LSO");
                postDataParams.put("LSO_ID", mainActivity.LSO_ID);
                postDataParams.put("Org_ID", mainActivity.OrgID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            values2 = new ContentValues();
            StrNewRecord = "";
            StrReady = "";
            lTemp1 = db.insert(ufinDbContract.OrgStructure.TABLE_NAME, null, values1);
            if (lTemp1 > 0) {
                System.out.println("OrgStructure Inserted.");
                StrNewRecord = "True";
                StrReady = "True";
            }
            strDataParamsQuery = "";
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
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_SyncID, strTimeStamp);
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_DbTableName, ufinDbContract.OrgStructure.TABLE_NAME);
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_updatedRecordID, selectedNoLoanGroupID);
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsNewRecord, StrNewRecord);
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsReady, StrReady);
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_PostDataParams, strDataParamsQuery);

            lTemp1 = db.insert(ufinDbContract.RecordsToSync.TABLE_NAME, null, values2);
            if (lTemp1 > 0) {
                MainActivity.clsRecordsToSync newRec = new MainActivity.clsRecordsToSync();
                newRec.SyncID = strTimeStamp;
                newRec.recordID = selectedNoLoanGroupID;
                newRec.recordInserted = StrNewRecord.equalsIgnoreCase("True");
                newRec.recordReady = StrReady.equalsIgnoreCase("True");
                newRec.PostParams = strDataParamsQuery;
                mainActivity.SyncOrgStructure.add(newRec);
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

            //Create new Group
            values1 = new ContentValues();
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_ORGStructureID, selectedRevisitGroup.GroupID);
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_NodeName, ("G-" + String.valueOf(selectedRevisitGroup.GroupID)));
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_NodeLevel, 6);
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_ParentID, selectedRevisitGroup.CenterID);
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_ParentLevel, 5);
//bmp 08-Aug-26
//            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_ChildsAllowed, 5);
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_ChildsAllowed, 5);
//bmp 08-Aug-26
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_MarathiName, "NA");
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_BranchNumber, "NA");
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_IsActive, 1);
            values1.put(ufinDbContract.OrgStructure.COLUMN_NAME_Remark, "New Group Created by LSO");

            postDataParams = new JSONObject();
            try {
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_ORGStructureID, selectedRevisitGroup.GroupID);
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_NodeName, ("G-" + String.valueOf(selectedRevisitGroup.GroupID)));
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_NodeLevel, 6);
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_ParentID, selectedRevisitGroup.CenterID);
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_ParentLevel, 5);
//bmp 08-Aug-26
//                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_ChildsAllowed, 5);
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_ChildsAllowed, 5);
//bmp 08-Aug-26
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_MarathiName, "NA");
                postDataParams.put(ufinDbContract.OrgStructure.COLUMN_NAME_Remark, "New Group Created by LSO");
                postDataParams.put("LSO_ID", mainActivity.LSO_ID);
                postDataParams.put("Org_ID", mainActivity.OrgID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            values2 = new ContentValues();
            StrNewRecord = "";
            StrReady = "";
            lTemp1 = db.insert(ufinDbContract.OrgStructure.TABLE_NAME, null, values1);
            if (lTemp1 > 0) {
                System.out.println("OrgStructure Inserted.");
                StrNewRecord = "True";
                StrReady = "True";
            }
            strDataParamsQuery = "";
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
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_SyncID, strTimeStamp);
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_DbTableName, ufinDbContract.OrgStructure.TABLE_NAME);
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_updatedRecordID, selectedRevisitGroup.GroupID);
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsNewRecord, StrNewRecord);
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsReady, StrReady);
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_PostDataParams, strDataParamsQuery);

            lTemp1 = db.insert(ufinDbContract.RecordsToSync.TABLE_NAME, null, values2);
            if (lTemp1 > 0) {
                MainActivity.clsRecordsToSync newRec = new MainActivity.clsRecordsToSync();
                newRec.SyncID = strTimeStamp;
                newRec.recordID = selectedRevisitGroup.GroupID;
                newRec.recordInserted = StrNewRecord.equalsIgnoreCase("True");
                newRec.recordReady = StrReady.equalsIgnoreCase("True");
                newRec.PostParams = strDataParamsQuery;
                mainActivity.SyncOrgStructure.add(newRec);
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
        } finally {
            db.close();
        }

        for (int i = 0; i < selectedRevisitGroup.MemberCount; i++) {
            //Member Added:
            mainActivity.SQLite_getMembers(selectedRevisitGroup.GroupMemberID.get(i), "MemberID");
            mainActivity.SQLite_getMembers(mainActivity.Member.get(0).MemberID, "MainMemberID");
            mainActivity.Member.get(0).IsActive = 1;
            for (int k = 0; k < mainActivity.Member.size(); k++) {
                mainActivity.Member.get(k).GroupID = selectedRevisitGroup.GroupID;
                mainActivity.Member.get(k).BranchID = mainActivity.BranchID;
                LastTimeInMillis += 1000;
                if (mainActivity.SQLite_saveMember(mainActivity.Member.get(k), LastTimeInMillis, true)) {
                    System.out.println("Moved MemberID: " + String.valueOf(mainActivity.Member.get(k).MemberID) + " To GroupID: " + String.valueOf(selectedRevisitGroup.GroupID));
                }
            }
        }
        selectedRevisitGroup.RevisitGroupStatus = clsRevisitGroup.REVISIT_GROUP_STATUS_READYGROUP;
        for (int i = 0; i < selectedRevisitGroup.MemberCount; i++) {
            clsRevisitMember member = mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i));
            member.CenterID = selectedRevisitGroup.CenterID;
            member.GroupID = selectedRevisitGroup.GroupID;
            member.GroupName = selectedRevisitGroup.GroupName;
            member.RevisitMemberStatus = clsRevisitMember.REVISIT_MEMBER_STATUS_READYGROUP;
            member.Edited = true;
            member.LastCycleGroupContinued = selectedRevisitGroup.LastCycleGroupContinued;
            mainActivity.SQLite_updateRevisitMember(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i), false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAddNewgroup:
                if(selectedNoloangroupMemberID < 0)
                    break;
                if(mainActivity.RevisitMemberList.get(selectedNoloangroupMemberPosition).CreditCheck != 1)
                    break;
//                int iSingles = 0;
//                int iOnRent = 0;
                int iLoanCycleDelta = 0;
                String strGroupType = "A";
                clsRevisitMember addMember = mainActivity.RevisitMemberList.get(selectedNoloangroupMemberPosition);
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
                    if (member.LastGroupID != addMember.LastGroupID) {
                        strGroupType = "B";
                    }
                }
//                if(addMember.Single)
//                    iSingles++;
//                if(addMember.OwnHouse == 0)
//                    iOnRent++;
                if (strGroupType.equalsIgnoreCase("A")) {
                    ShowToastMessage("Member with same LastGroupID added, New Group Formation Rules Not Applied.");

                    selectedRevisitGroup.GroupMemberID.add(mainActivity.RevisitMemberList.get(selectedNoloangroupMemberPosition).MemberID);
                    selectedRevisitGroup.GroupMemberRevisitMemberListIndex.add(selectedNoloangroupMemberPosition);
                    selectedRevisitGroup.MemberCount++;
                    addMember.RevisitMemberStatus = clsRevisitMember.REVISIT_MEMBER_STATUS_REGROUPED;
                    mainActivity.RevisitMemberList.set(selectedNoloangroupMemberPosition, addMember);
                    RefreshNewGroupMembers();
                    RefreshReadyMemberListClusterCommunity();
//                } else if((iSingles + iOnRent) > 2) {
//                    ShowToastMessage("भाड्याच्या घरात राहणाऱ्या अथवा एकट्या सभासदांची संख्या 2 पेक्षा जास्त आहे.");
                } else {    //Type-B Group Criteria Satisfied
                        selectedRevisitGroup.GroupMemberID.add(mainActivity.RevisitMemberList.get(selectedNoloangroupMemberPosition).MemberID);
                        selectedRevisitGroup.GroupMemberRevisitMemberListIndex.add(selectedNoloangroupMemberPosition);
                        selectedRevisitGroup.MemberCount++;
                        addMember.RevisitMemberStatus = clsRevisitMember.REVISIT_MEMBER_STATUS_REGROUPED;
                        mainActivity.RevisitMemberList.set(selectedNoloangroupMemberPosition, addMember);
                        RefreshNewGroupMembers();
                        RefreshReadyMemberListClusterCommunity();
                }
                break;
            case R.id.buttonRemoveNewgroup:
                if(selectedNewgroupMemberPosition < 0)
                    break;

                for (int i = 0; i < selectedRevisitGroup.MemberCount; i++) {
                    if (selectedRevisitGroup.GroupMemberID.get(i) ==
                            mainActivity.RevisitMemberList.get(selectedNewgroupMemberPosition).MemberID) {
                        clsRevisitMember removeMember = mainActivity.RevisitMemberList.get(selectedNewgroupMemberPosition);
                        removeMember.RevisitMemberStatus = clsRevisitMember.REVISIT_MEMBER_STATUS_REVISITED;
                        removeMember.Edited = true;
                        mainActivity.RevisitMemberList.set(selectedNewgroupMemberPosition, removeMember);
                        selectedRevisitGroup.MemberCount--;
                        for(int j=i; j < selectedRevisitGroup.MemberCount; j++) {
                            selectedRevisitGroup.GroupMemberID.set(j, selectedRevisitGroup.GroupMemberID.get(j+1));
                            selectedRevisitGroup.GroupMemberRevisitMemberListIndex.set(j, selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(j+1));
                        }
                        RefreshNewGroupMembers();
                        RefreshReadyMemberListClusterCommunity();
                        break;
                    }
                }
                break;
            case R.id.buttonCloseNewgroup:
                mainActivity.InitDesktop();
                break;
            case R.id.buttonSaveNewgroup:
                ValidateScreenNewgroup();
                if(NewgroupProblemsCheck.size() == 0) {
                    CreateAlertDialog(true, true);
                    dlgState = ufin_DIALOG_STATE_CONFIRM_NEWGROUP_SCREEN;
                    dlgAlert.setMessage(MSG_ufin_NEWGROUP_CONFIRM_SAVE);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                } else {
                    String strMsg = "";
                    for(int i=0; i<NewgroupProblemsCheck.size(); i++) {
                        strMsg = strMsg + NewgroupProblemsCheck.get(i) + "\n";
                    }
                    CreateAlertDialog(true, true);
                    dlgState = ufin_DIALOG_STATE_VALIDATE_NEWGROUP_SCREEN;
                    dlgAlert.setMessage(strMsg);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                }
                break;
        }
    }
    public void ValidateScreenNewgroup() {
        NewgroupProblemsCheck.clear();

        if(selectedRevisitGroup.MemberCount < 3) {
            NewgroupProblemsCheck.add("गटातील सभासद संख्या 3 पेक्षा कमी आहे.");
            return;
        }
        if(selectedRevisitGroup.MemberCount > 5) {
            NewgroupProblemsCheck.add("गटातील सभासद संख्या 5 पेक्षा जास्त आहे.");
            return;
        }

        int minLoanCycle = 100;
        int maxLoanCycle = 0;
//        int iSingles = 0;
//        int iOnRent = 0;
//        int iBusinessLoan = 0;
//        int iEducationLoan = 0;
//        int iHouseLoan = 0;
        for(int i=0; i<selectedRevisitGroup.MemberCount; i++) {
            clsRevisitMember member = mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i));
//            if(member.Single)
//                iSingles++;
//            if(member.OwnHouse == 0)
//                iOnRent++;

            if (member.LoanCycle < minLoanCycle)
                minLoanCycle = member.LoanCycle;
            if (member.LoanCycle > maxLoanCycle)
                maxLoanCycle = member.LoanCycle;

            if( (   (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) ||
                    (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) ||
                    (member.VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED) ) ) {
                 if (member.CreditCheck == -1) {
                    NewgroupProblemsCheck.add("सभासदाचा क्रेडिट ब्युरो चेक बाकी आहे: " + member.MemberName);
                } else if (member.CreditCheck == 0) {
                    NewgroupProblemsCheck.add("सभासदाचा क्रेडिट ब्युरो चेक रिजेक्ट आहे: " + member.MemberName);
                }
            }
            if(     (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) &&
                    (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) &&
                    (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED) &&
                    (member.VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING) ) {
                NewgroupProblemsCheck.add("सभासदाचा पुनर्भेट रिमार्क \"कर्ज पाहिजे\" अथवा \"चालू कर्ज\" नाही: " + member.MemberName);
            }
            if(member.PrevMaxLoanAmount == 0) {
                NewgroupProblemsCheck.add("सभासदाला अन्नपूर्णाकडून मिळणारी नवीन कर्ज रक्कम 0 आहे: " + member.MemberName);
            }
        }

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
//                boolean newFlag = true;
//                for(int j=0; j<alDifferentLastGroupID.size(); j++) {
//                    if(lTemp1 == alDifferentLastGroupID.get(j)) {
//                        newFlag = false;
//                        break;
//                    }
//                }
//                if(newFlag) {
//                    alDifferentLastGroupID.add(lTemp1);
//                }
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
//                    NewgroupProblemsCheck.add("आधीच्या एकाच गटातील सभासद संख्या २ पेक्षा कमी आहे.");
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
//                        NewgroupProblemsCheck.add("आधीच्या एकाच गटातील सभासद संख्या २ पेक्षा कमी आहे.");
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
//                        NewgroupProblemsCheck.add("आधीच्या एकाच गटातील सभासद संख्या २ पेक्षा कमी आहे.");
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
//                        NewgroupProblemsCheck.add("आधीच्या एकाच गटातील सभासद संख्या २ पेक्षा कमी आहे.");
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
//                NewgroupProblemsCheck.add("आधीच्या एकाच गटातील सभासद संख्या २ पेक्षा कमी आहे.");
//                break;
//        }
        if(selectedRevisitGroup.LastCycleGroupContinued == 0) {
//            if((iSingles + iOnRent) > 2)
//                NewgroupProblemsCheck.add("भाड्याच्या घरात राहणाऱ्या अथवा एकट्या सभासदांची संख्या 2 पेक्षा जास्त आहे.");
        }
    }
    void RefreshCluster_Community() {
        LSO_ClusterCommunityID.clear();
        LSO_ClusterCommunityID.add(0L);
        LSO_ClusterCommunityName.clear();
        LSO_ClusterCommunityName.add("");

        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection3 = {
                ufinDbContract.OrgStructureMaster.COLUMN_NAME_ORGStructureID,
                ufinDbContract.OrgStructureMaster.COLUMN_NAME_NodeName,
        };
        String selection3 = ufinDbContract.OrgStructureMaster.COLUMN_NAME_ParentID + "=" + "'" + String.valueOf(mainActivity.selectedClusterID) + "'"
                + " and " + ufinDbContract.OrgStructureMaster.COLUMN_NAME_NodeLevel + "=" + "'" + String.valueOf(4) + "'";
        // How you want the results sorted in the resulting Cursor
        String sortOrder3 = ufinDbContract.OrgStructureMaster.COLUMN_NAME_ORGStructureID + " DESC";

        Cursor c3 = db.query(
                ufinDbContract.OrgStructureMaster.TABLE_NAME,  // The table to query
                projection3,                               // The columns to return
                selection3,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder3                                 // The sort order
        );

        try {
            if (c3 != null && c3.moveToFirst()) {
                if (c3.getCount() > 0) {
                    int iCount = c3.getCount();
                    for (int i = 0; i < iCount; i++) {
                        try {
                            LSO_ClusterCommunityID.add(Long.valueOf(c3.getString(c3.getColumnIndexOrThrow(ufinDbContract.OrgStructureMaster.COLUMN_NAME_ORGStructureID))));
                            LSO_ClusterCommunityName.add(c3.getString(c3.getColumnIndexOrThrow(ufinDbContract.OrgStructureMaster.COLUMN_NAME_NodeName)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        c3.moveToNext();
                    }
                }
            }
        } finally {
            if (c3 != null) {
                c3.close();
            }
            db.close();
        }

        ArrayAdapter<String> adapterLSOCluster_Community = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, LSO_ClusterCommunityName);
        adapterLSOCluster_Community.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner spSelectCommunity = (Spinner) rootView.findViewById(R.id.spinnerCommunity);
        spSelectCommunity.setAdapter(adapterLSOCluster_Community);
        spSelectCommunity.setSelection(0);
        spSelectCommunity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    return;

                for(int i=0; i<selectedRevisitGroup.MemberCount; i++) {
                    mainActivity.RevisitMemberList.get(selectedRevisitGroup.GroupMemberRevisitMemberListIndex.get(i)).RevisitMemberStatus
                            = clsRevisitMember.REVISIT_MEMBER_STATUS_REVISITED;
                }

                mainActivity.selectedCommunityID = -1;
                mainActivity.selectedGroupID = -1;
                selectedNewgroupMemberPosition = -1;
                selectedNewgroupMemberID = -1;
                selectedNoloangroupMemberPosition = -1;
                selectedNoloangroupMemberID = -1;

                selectedRevisitGroup = new clsRevisitGroup();
                selectedRevisitGroup.InitRevisitGroup();

                mainActivity.selectedCommunityID = LSO_ClusterCommunityID.get(position);
                RefreshNewGroupMembers();
                RefreshReadyMemberListClusterCommunity();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spSelectCommunity.setFocusable(true);
        spSelectCommunity.setFocusableInTouchMode(true);
    }
    void RefreshReadyMemberListClusterCommunity() {
        //selectedClusterID, selectedCommunityID
        ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarNewgroup);
        if (pbSync != null) {
            pbSync.setIndeterminate(true);
            pbSync.setVisibility(View.VISIBLE);
        }
        TableLayout tblReadyForLoanMembers = (TableLayout) rootView.findViewById(R.id.tblLayoutReadyForLoanMembers);
        tblReadyForLoanMembers.removeAllViews();
        TextView tvref = (TextView) rootView.findViewById(R.id.textViewNoLoanGroupReadyForLoanMembers);
        int OtherGroupReadyForLoanMemberCount = 0;
        int Prefer2Index = 0;
        for(int i=0; i<mainActivity.RevisitMemberList.size(); i++) {
            iIDindex = (2000 * i);
            if(mainActivity.RevisitMemberList.get(i).ClusterID != mainActivity.selectedClusterID)
                continue;
            if(mainActivity.RevisitMemberList.get(i).CommunityID != mainActivity.selectedCommunityID)
                continue;
            if(     (mainActivity.RevisitMemberList.get(i).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) &&
                    (mainActivity.RevisitMemberList.get(i).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) &&
                    (mainActivity.RevisitMemberList.get(i).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED) )
                continue;
            if(mainActivity.RevisitMemberList.get(i).RevisitMemberStatus >= clsRevisitMember.REVISIT_MEMBER_STATUS_REGROUPED)
                continue;
            //This is to avoid showing members from other groups 'awaiting regrouping'
            boolean bFlag = false;
            for(int k=0; k<mainActivity.RevisitGroupList.size(); k++) {
                if(     (mainActivity.RevisitGroupList.get(k).ReadyForLoanGroupMemberCount >= 3) &&
                        (mainActivity.RevisitGroupList.get(k).RevisitGroupStatus == clsRevisitGroup.REVISIT_GROUP_STATUS_SCHEDULED) ) {
                    if(mainActivity.RevisitMemberList.get(i).GroupID == mainActivity.RevisitGroupList.get(k).GroupID)
                        bFlag = true;
                }
            }
            if(bFlag)
                continue;

            LinearLayout llv = new LinearLayout(mainActivity.getBaseContext());
            llv.setOrientation(LinearLayout.VERTICAL);
            llv.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            LinearLayout llh1 = new LinearLayout(mainActivity.getBaseContext());
            llh1.setOrientation(LinearLayout.HORIZONTAL);
            llh1.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            LinearLayout llh2 = new LinearLayout(mainActivity.getBaseContext());
            llh2.setOrientation(LinearLayout.HORIZONTAL);
            llh2.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            LinearLayout llh3 = new LinearLayout(mainActivity.getBaseContext());
            llh3.setOrientation(LinearLayout.HORIZONTAL);
            llh3.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            LinearLayout llh4 = new LinearLayout(mainActivity.getBaseContext());
            llh4.setOrientation(LinearLayout.HORIZONTAL);
            llh4.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

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

            ImageButton imgbtPhoto = new ImageButton(mainActivity.getBaseContext());
            imgbtPhoto.setImageResource(R.drawable.ic_menu_camera);
            imgbtPhoto.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    1f));
            imgbtPhoto.setId(1+iIDindex);
            imgbtPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IDRequest = view.getId();
                    IDRequest = (IDRequest - 1)/2000;
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
                       /* Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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
            imgbtPreview.setId(2+iIDindex);
            imgbtPreview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IDRequest = view.getId();
                    IDRequest = (IDRequest - 2)/2000;
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
            cbSingle.setId(3+iIDindex);
            cbSingle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int iTemp = v.getId();
                    iTemp = (iTemp - 3)/2000;
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
            cbOnRent.setId(4+iIDindex);
            cbOnRent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int iTemp = v.getId();
                    iTemp = (iTemp - 4)/2000;
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
            tvCreditCheck.setId(5+iIDindex);
            tvCreditCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int iTemp = view.getId();
                    iTemp = (iTemp - 5)/2000;
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
                                dlgState = ufin_NEWGROUP_DIALOG_STATE_CBCHECK_DOC_UPLOAD;
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
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvCenterGroup.setGravity(Gravity.CENTER_VERTICAL);
            tvCenterGroup.setPadding(10, 0, 0, 0);

            TextView tvGroupType = new TextView(mainActivity.getBaseContext());
            tvGroupType.setTextSize(24);
            tvGroupType.setTextColor(tvref.getTextColors());
            tvGroupType.setTypeface(Typeface.DEFAULT_BOLD);
            tvGroupType.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
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
                    TableRow.LayoutParams.WRAP_CONTENT));
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
            tvLastLoanAmount.setText("कर्ज:जुने रु." + String.valueOf(member.LastLoanAmount) + "/-");

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

            tvLoanCycle.setText("सायकल " + String.valueOf(member.LoanCycle) + " ");
            tvHomeLoanAmount.setText("गृहकर्ज रु." + String.valueOf(member.HomeLoanAmount) + "/-");


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
                        dlgState = ufin_NEWGROUP_DIALOG_STATE_CONFIRM_PRINT_BUTTON;
                        dlgAlert.setMessage(MSG_ufin_NEWGROUP_CONFIRM_PRINT_BUTTON);
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
                        dlgState = ufin_NEWGROUP_DIALOG_STATE_NO_PRINTER;
                        dlgAlert.setMessage(MSG_ufin_NEWGROUP_NO_PRINTER);
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

            tvCenterGroup.setText("G-" + String.valueOf(member.GroupID));
            if(String.valueOf(member.CenterID).length() >= 17) {
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
            tblRow.setId(8+iIDindex);
            tblRow.setEnabled(true);
            tblRow.setClickable(true);
            tblRow.setOnClickListener(noloangroupmemberNewgroupOnClickListener);

            if (mainActivity.RevisitMemberList.get(i).LastGroupID == mainActivity.selectedGroupID) {
                tblReadyForLoanMembers.addView(tblRow, 0);
                Prefer2Index++;
            } else if (mainActivity.RevisitMemberList.get(i).CenterID == mainActivity.selectedCenterID) {
                tblReadyForLoanMembers.addView(tblRow, Prefer2Index);
            } else {
                tblReadyForLoanMembers.addView(tblRow);
            }
        }

        selectedNoloangroupMemberPosition = -1;
        selectedNoloangroupMemberID = -1;
        Button bt1 = (Button) rootView.findViewById(R.id.buttonAddNewgroup);
        bt1.setEnabled(false);
        Button bt2 = (Button) rootView.findViewById(R.id.buttonRemoveNewgroup);
        bt2.setEnabled(false);

        String strTemp = "कर्ज उत्सुक सभासद" + " (" + String.valueOf(OtherGroupReadyForLoanMemberCount) + ")";
        TextView tv = (TextView) rootView.findViewById(R.id.textViewNoLoanGroupReadyForLoanMembers);
        tv.setText(strTemp);

        if (pbSync != null) {
            pbSync.setIndeterminate(true);
            pbSync.setVisibility(View.INVISIBLE);
        }
    }
    public void DisableNextPrintTillUserConfirm() {
        flagDisableNextPrint = true;
        CreateAlertDialog(true, false);
        dlgState = ufin_NEWGROUP_DIALOG_STATE_RESUME_PRINTING;
        dlgAlert.setMessage(MSG_ufin_NEWGROUP_RESUME_PRINTING);
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
                                String currentPhotoPath = strFilepath + strFilename;
                                mainActivity.AdjustImageFileSize(currentPhotoPath);
                                mainActivity.RevisitMemberList.get(IDRequest).strRegroupProof = strFilename;
                            } else {
                                mainActivity.RevisitMemberList.get(IDRequest).strRegroupProof = strFilename;
                            }
                        }
                    } else if(result.getResultCode() == Activity.RESULT_CANCELED){
                        //handle cancel
                        ShowdlgAlertonActivityResult("CAMERA REQUEST CANCELED!");
                    }
                }
            });*/

    //RK
    ActivityResultLauncher<Intent> startActivityIntent =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {

                        @Override
                        public void onActivityResult(ActivityResult result) {

                            if (result.getResultCode() == Activity.RESULT_OK) {

                                // Get image URI returned by Camera App

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

                                    if (Build.VERSION.SDK_INT >=
                                            Build.VERSION_CODES.TIRAMISU) {

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


                                File outFile = new File(
                                        strFilepath + strFilename
                                );

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
                                            "Unable to save captured image!\n" + e.getMessage());
                                    return;
                                }


                                if (!outFile.exists() || outFile.length() == 0) {

                                    ShowdlgAlertonActivityResult(
                                            "Image was not saved!"
                                    );
                                    return;
                                }


                                if (outFile.length() >= MAX_PHOTO_FILESIZE) {

                                    String currentPhotoPath =
                                            strFilepath + strFilename;

                                    mainActivity.AdjustImageFileSize(
                                            currentPhotoPath
                                    );
                                }

                                mainActivity.RevisitMemberList
                                        .get(IDRequest)
                                        .strRegroupProof = strFilename;

                            } else if (
                                    result.getResultCode() ==
                                            Activity.RESULT_CANCELED) {

                                ShowdlgAlertonActivityResult(
                                        "CAMERA REQUEST CANCELED!"
                                );
                            }
                        }
                    }
            );
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
    private View.OnClickListener noloangroupmemberNewgroupOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            if(selectedNoloangroupMemberPosition >= 0) {
//bmp 31-Aug-26
//                View vPrev = (View) rootView.findViewById(selectedNoloangroupMemberPosition);
                int iPrev = (selectedNoloangroupMemberPosition * 2000) + 8;
                View vPrev = (View) rootView.findViewById(iPrev);
//bmp 31-Aug-26
                vPrev.setBackgroundColor(Color.WHITE);
            }
            int iTemp = v.getId();
            iTemp = (iTemp - 8)/2000;
            if(selectedNoloangroupMemberPosition == iTemp) {
                selectedNoloangroupMemberPosition = -1;
                selectedNoloangroupMemberID = -1;
            } else {
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
                    if (member.CreditCheck == -1) {
                        ShowToastMessage("सभासदाचा क्रेडिट ब्युरो चेक बाकी आहे.");
                    } else if (member.CreditCheck == 0) {
                        ShowToastMessage("सभासदाचा क्रेडिट ब्युरो चेक रिजेक्ट आहे.");
                    } else {
                        v.setBackgroundColor(Color.CYAN);
                        selectedNoloangroupMemberPosition = iTemp;
                        selectedNoloangroupMemberID = member.MemberID;
//bmp 17-Aug-26
                        boolean flagActiveGroup = false;
                        String strMsg = "गटातील सभासदाचा चालू कर्ज कालावधी बाकी आहे:";
                        for(int i=0; i<mainActivity.RevisitMemberList.size(); i++) {
                            if(     (mainActivity.RevisitMemberList.get(i).GroupID == member.GroupID) &&
                                    (mainActivity.RevisitMemberList.get(i).MemberID != member.MemberID) ) {
//                                if(mainActivity.RevisitMemberList.get(i).LastLoanStatus.startsWith("Active")) {
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
                            dlgState = ufin_NEWGROUP_DIALOG_STATE_ACTIVE_LOAN_GROUP;
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
            }

            Button bt1 = (Button) rootView.findViewById(R.id.buttonAddNewgroup);
            bt1.setEnabled(false);
            Button bt2 = (Button) rootView.findViewById(R.id.buttonRemoveNewgroup);
            bt2.setEnabled(false);

            if(  (selectedRevisitGroup.MemberCount < 5) &&
                    (selectedNewgroupMemberID <= 0) &&
                    (selectedNoloangroupMemberID > 0) ) {
                bt1.setEnabled(true);
            }
        }
    };
    public void RefreshNewGroupMembers() {
        ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarNewgroup);
        if (pbSync != null) {
            pbSync.setIndeterminate(true);
            pbSync.setVisibility(View.VISIBLE);
        }
        TableLayout tblGroupMembers = (TableLayout) rootView.findViewById(R.id.tblLayoutGroupMembers);
        tblGroupMembers.removeAllViews();
        TextView tvref = (TextView) rootView.findViewById(R.id.textViewNoLoanGroupReadyForLoanMembers);
        for(int i=0; i<mainActivity.RevisitMemberList.size(); i++) {
            iIDindex = (2000 * i);
            if(mainActivity.RevisitMemberList.get(i).CommunityID != mainActivity.selectedCommunityID)
                continue;
            if(     (mainActivity.RevisitMemberList.get(i).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) &&
                    (mainActivity.RevisitMemberList.get(i).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) &&
                    (mainActivity.RevisitMemberList.get(i).VisitDetailsID != clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED) )
                continue;
            if(mainActivity.RevisitMemberList.get(i).RevisitMemberStatus != clsRevisitMember.REVISIT_MEMBER_STATUS_REGROUPED)
                continue;
            LinearLayout llv = new LinearLayout(mainActivity.getBaseContext());
            llv.setOrientation(LinearLayout.VERTICAL);
            llv.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            LinearLayout llh1 = new LinearLayout(mainActivity.getBaseContext());
            llh1.setOrientation(LinearLayout.HORIZONTAL);
            llh1.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            LinearLayout llh2 = new LinearLayout(mainActivity.getBaseContext());
            llh2.setOrientation(LinearLayout.HORIZONTAL);
            llh2.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            LinearLayout llh3 = new LinearLayout(mainActivity.getBaseContext());
            llh3.setOrientation(LinearLayout.HORIZONTAL);
            llh3.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            LinearLayout llh4 = new LinearLayout(mainActivity.getBaseContext());
            llh4.setOrientation(LinearLayout.HORIZONTAL);
            llh4.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

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
            cbSingle.setId(9+iIDindex);
            cbSingle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int iTemp = v.getId();
                    iTemp = (iTemp - 9)/2000;
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
            cbOnRent.setId(10+iIDindex);
            cbOnRent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int iTemp = v.getId();
                    iTemp = (iTemp - 10)/2000;
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
            tvCreditCheck.setId(11+iIDindex);
            tvCreditCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int iTemp = view.getId();
                    iTemp = (iTemp - 11)/2000;
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
                                dlgState = ufin_NEWGROUP_DIALOG_STATE_CBCHECK_DOC_UPLOAD;
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
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvCenterGroup.setGravity(Gravity.CENTER_VERTICAL);
            tvCenterGroup.setPadding(10, 0, 0, 0);

            TextView tvGroupType = new TextView(mainActivity.getBaseContext());
            tvGroupType.setTextSize(24);
            tvGroupType.setTextColor(tvref.getTextColors());
            tvGroupType.setTypeface(Typeface.DEFAULT_BOLD);
            tvGroupType.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
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
                    TableRow.LayoutParams.WRAP_CONTENT));
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
            tvLastLoanAmount.setText("कर्ज:जुने रु." + String.valueOf(member.LastLoanAmount) + "/-");

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

            tvLoanCycle.setText("सायकल " + String.valueOf(member.LoanCycle) + " ");
            tvHomeLoanAmount.setText("गृहकर्ज रु." + String.valueOf(member.HomeLoanAmount) + "/-");

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
            btPreviewCBCheck.setId(12+iIDindex);
            btPreviewCBCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int iTemp = view.getId();
                    iTemp = (iTemp - 12)/2000;
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
            btPrintCBCheck.setId(13+iIDindex);
            btPrintCBCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int iTemp = view.getId();
                    iTemp = (iTemp - 13)/2000;
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
                        dlgState = ufin_NEWGROUP_DIALOG_STATE_CONFIRM_PRINT_BUTTON;
                        dlgAlert.setMessage(MSG_ufin_NEWGROUP_CONFIRM_PRINT_BUTTON);
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
                        dlgState = ufin_NEWGROUP_DIALOG_STATE_NO_PRINTER;
                        dlgAlert.setMessage(MSG_ufin_NEWGROUP_NO_PRINTER);
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

            tvCenterGroup.setText("G-" + String.valueOf(member.GroupID));
            if(String.valueOf(member.CenterID).length() >= 17) {
                tvCenterGroup.setText("G-" + String.valueOf(member.GroupID).substring(0, (String.valueOf(member.GroupID).length() - 8)));
            }
            if(member.GroupName.contains("No Loan")) {
                tvGroupType.setVisibility(View.VISIBLE);
            } else {
                tvGroupType.setVisibility(View.INVISIBLE);
            }
            tvLastCenterLastGroup.setText("LC-" + String.valueOf(member.LastCenterID) + ":LG-" + String.valueOf(member.LastGroupID));

            llh1.addView(tvListType);
            llh1.addView(tvLastLoanStatus);
            llh1.addView(tvMemberName);
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
            tblRow.setId(14+iIDindex);
            tblRow.setEnabled(true);
            tblRow.setClickable(true);
            tblRow.setOnClickListener(groupmemberNewgroupOnClickListener);

            tblGroupMembers.addView(tblRow);
        }

        int iCount = tblGroupMembers.getChildCount();
        EditText et = (EditText) rootView.findViewById(R.id.editTextGroupMemberCount);
        et.setText(String.valueOf(iCount));

        selectedNewgroupMemberPosition = -1;
        selectedNewgroupMemberID = -1;
        Button bt1 = (Button) rootView.findViewById(R.id.buttonAddNewgroup);
        bt1.setEnabled(false);
        Button bt2 = (Button) rootView.findViewById(R.id.buttonRemoveNewgroup);
        bt2.setEnabled(false);

        if (pbSync != null) {
            pbSync.setIndeterminate(true);
            pbSync.setVisibility(View.INVISIBLE);
        }
    }
    private View.OnClickListener groupmemberNewgroupOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            if(selectedNewgroupMemberPosition >= 0) {
//bmp 31-Aug-26
//                View vPrev = (View) rootView.findViewById(selectedNewgroupMemberPosition);
                int iPrev = (selectedNewgroupMemberPosition * 2000) + 14;
                View vPrev = (View) rootView.findViewById(iPrev);
//bmp 31-Aug-26
                vPrev.setBackgroundColor(Color.WHITE);
            }
            int iTemp = v.getId();
            iTemp = (iTemp - 14)/2000;
            if(selectedNewgroupMemberPosition == iTemp) {
                selectedNewgroupMemberPosition = -1;
                selectedNewgroupMemberID = -1;
            } else {
                clsRevisitMember member = mainActivity.RevisitMemberList.get(iTemp);
                v.setBackgroundColor(Color.CYAN);
                selectedNewgroupMemberPosition = iTemp;
                selectedNewgroupMemberID = member.MemberID;
            }

            Button bt1 = (Button) rootView.findViewById(R.id.buttonAddNewgroup);
            bt1.setEnabled(false);
            Button bt2 = (Button) rootView.findViewById(R.id.buttonRemoveNewgroup);
            bt2.setEnabled(false);
            if(selectedNewgroupMemberID > 0) {
                bt2.setEnabled(true);
            }
        }
    };
    public void SQLite_getReasonMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
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
        String strReasonType = "Loan Application";
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

        ReasonIDMaster.clear();
        ReasonMarathiDescriptionMaster.clear();
        ReasonIDMaster.add(0);
        ReasonMarathiDescriptionMaster.add("?");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            String Str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_MarathiDescription));
                            if ((Str1 == null) || (Str1.equalsIgnoreCase(" ")) || (Str1.equalsIgnoreCase(""))) {
                            } else {
                                ReasonIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonID))));
                                ReasonMarathiDescriptionMaster.add(Str1);
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
