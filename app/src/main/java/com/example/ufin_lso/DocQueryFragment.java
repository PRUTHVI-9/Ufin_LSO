package com.example.ufin_lso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
 * Use the {@link DocQueryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DocQueryFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View rootView;
    MainActivity mainActivity;

    public static final int ufin_DOCQUERY_DIALOG_STATE_NONE                 = 0;
    public static final int ufin_DOCQUERY_DIALOG_STATE_CLOSE                = 1;
    public static final int ufin_DIALOG_STATE_SUBMIT_QUERY_DOCS             = 26;

    public static final String MSG_ufin_DOCQUERY_CONFIRM_SAVE =
            "Press OK to Submit Member Document Photos.";

    private static final int ID_REQUEST_PHOTO_DocQuery = 9;
    public static final int MAX_PHOTO_FILESIZE             = (4 * 1024 * 1024);
//    public static final int MAX_PHOTO_FILESIZE             = (8 * 1024 * 1024);
    int IDRequest = -1;

    public AlertDialog.Builder dlgAlert;
    public static int dlgState = ufin_DOCQUERY_DIALOG_STATE_NONE;;
    private static int currentphotoDoc = 1;
    private ArrayList<Integer> CheckDuplicateMaster = new ArrayList<Integer>();
    private ArrayList<Integer> DocumentIDMaster = new ArrayList<Integer>();
    private ArrayList<String> DocumentMarathiDescriptionMaster = new ArrayList<String>();
    long LastTimeInMillis;

    public DocQueryFragment() {
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
    public static DocQueryFragment newInstance(String param1, String param2) {
        DocQueryFragment fragment = new DocQueryFragment();
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
//        return inflater.inflate(R.layout.fragment_docquery, container, false);
        rootView = inflater.inflate(R.layout.fragment_docquery, container, false);
        mainActivity = (MainActivity) getActivity();

        Calendar cal = Calendar.getInstance();
        LastTimeInMillis = cal.getTimeInMillis();

        ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarDocQuery);
        if (pbSync != null) {
            pbSync.setIndeterminate(true);
            pbSync.setVisibility(View.VISIBLE);
        }

        Button bt = (Button) rootView.findViewById(R.id.buttonCloseDocQuery);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonSaveDocQuery);
        bt.setOnClickListener(this);
        ImageButton imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonDeleteDoc);
        imgbt.setOnClickListener(this);
        imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonPreviewDoc);
        imgbt.setOnClickListener(this);
        imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonPhotoDoc);
        imgbt.setOnClickListener(this);
        TextView tv = (TextView) rootView.findViewById(R.id.textViewQtyPhotoDoc);
        tv.setOnClickListener(this);

        SQLite_getDocumentMaster();

        mainActivity.selectedMemberID = -1;
//bmp 28-May-26
        mainActivity.PhotoDoc.clear();
//bmp 28-May-26
        final ArrayList<String> QueryMemberInfoList = new ArrayList<String>();
        QueryMemberInfoList.clear();
//        final ArrayList<clsRevisitMember> QueryMemberList = new ArrayList<clsRevisitMember>();
//        QueryMemberList.clear();
        final ArrayList<Long> QueryMemberList = new ArrayList<Long>();
        QueryMemberList.clear();
        QueryMemberInfoList.add("Select Query Member");
        QueryMemberList.add(null);
//bmp 26-May-26
//bmp 01-Jul-26
//        for(int i=0; i<mainActivity.CollectionMemberList.size(); i++) {
//            if(mainActivity.CollectionMemberList.get(i).MemberStatus > clsCollectionMember.COLLECTION_MEMBER_STATUS_QUERY_OFFSET) {
//                clsCollectionMember member = mainActivity.CollectionMemberList.get(i);
//                QueryMemberList.add(member.MemberID);
//                String strTemp =    "C-" + String.valueOf(member.CenterID) + ":" +
//                        "G-" + String.valueOf(member.GroupID) + ":" +
//                        member.MemberName;
//                if(String.valueOf(member.CenterID).length() >= 17) {
//                    strTemp =    "C-" + String.valueOf(member.CenterID).substring(0, (String.valueOf(member.CenterID).length() - 8)) + ":" +
//                            "G-" + String.valueOf(member.GroupID).substring(0, (String.valueOf(member.GroupID).length() - 8)) + ":" +
//                            member.MemberName;
//                }
//                QueryMemberInfoList.add(strTemp);
//            }
//        }
        for(int j=0; j<mainActivity.DocumentQuery.size(); j++) {
            if(mainActivity.DocumentQuery.get(j).DocumentTypeID == clsDocumentIDs.MEMBER_DOC_ID_ONLINE_COLLECTION) {
                for(int i=0; i<mainActivity.CollectionMemberList.size(); i++) {
                    if(     (mainActivity.CollectionMemberList.get(i).MemberID == mainActivity.DocumentQuery.get(j).MainMemberID) ||
                            (mainActivity.CollectionMemberList.get(i).MemberID == mainActivity.DocumentQuery.get(j).FamilyMemberID) ) {
                        clsCollectionMember member = mainActivity.CollectionMemberList.get(i);
                        QueryMemberList.add(member.MemberID);
                        String strTemp =    "C-" + String.valueOf(member.CenterID) + ":" +
                                "G-" + String.valueOf(member.GroupID) + ":" +
                                member.MemberName;
                        if(String.valueOf(member.CenterID).length() >= 17) {
                            strTemp =    "C-" + String.valueOf(member.CenterID).substring(0, (String.valueOf(member.CenterID).length() - 8)) + ":" +
                                    "G-" + String.valueOf(member.GroupID).substring(0, (String.valueOf(member.GroupID).length() - 8)) + ":" +
                                    member.MemberName;
                        }
                        QueryMemberInfoList.add(strTemp);

                        break;
                    }
                }
            }
        }
//bmp 01-Jul-26
        for(int i=0; i<mainActivity.RevisitMemberList.size(); i++) {
            if(mainActivity.RevisitMemberList.get(i).RevisitMemberStatus == clsRevisitMember.REVISIT_MEMBER_STATUS_QUERY) {
                clsRevisitMember member = new clsRevisitMember();
                member.InitRevisitMember();
                mainActivity.RevisitMemberList.get(i).Copy_RevisitMember(member);
//                QueryMemberList.add(member);
                QueryMemberList.add(member.MemberID);
                String strTemp =    "C-" + String.valueOf(member.CenterID) + ":" +
                        "G-" + String.valueOf(member.GroupID) + ":" +
                        member.MemberName;
                if(String.valueOf(member.CenterID).length() >= 17) {
                    strTemp =    "C-" + String.valueOf(member.CenterID).substring(0, (String.valueOf(member.CenterID).length() - 8)) + ":" +
                            "G-" + String.valueOf(member.GroupID).substring(0, (String.valueOf(member.GroupID).length() - 8)) + ":" +
                            member.MemberName;
                }
                QueryMemberInfoList.add(strTemp);
            }
        }

        ArrayAdapter<String> adapterQueryMemberInfo = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, QueryMemberInfoList);
        adapterQueryMemberInfo.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner spQueryMember = (Spinner) rootView.findViewById(R.id.spinnerQueryMember);
        spQueryMember.setAdapter(adapterQueryMemberInfo);
        spQueryMember.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                    return;
                parent.setEnabled(false);
                clsRevisitMember selectedQuerymember = new clsRevisitMember();
                selectedQuerymember.InitRevisitMember();
//                QueryMemberList.get(position).Copy_RevisitMember(selectedQuerymember);
//                mainActivity.selectedMemberID = selectedQuerymember.MemberID;
                mainActivity.selectedMemberID = QueryMemberList.get(position);
//bmp 28-May-26
                mainActivity.PhotoDoc.clear();
//bmp 28-May-26
                final ArrayList<MainActivity.clsDocQuery> PendingDocQuery = new ArrayList<MainActivity.clsDocQuery>();
                final ArrayList<String> PendingDocQueryMsg = new ArrayList<String>();
                PendingDocQuery.clear();
                PendingDocQueryMsg.clear();
                for(int j=0; j<mainActivity.DocumentQuery.size(); j++) {
                    if(     (mainActivity.selectedMemberID == mainActivity.DocumentQuery.get(j).MainMemberID) ||
                            (mainActivity.selectedMemberID == mainActivity.DocumentQuery.get(j).FamilyMemberID) ) {
                        PendingDocQuery.add(mainActivity.DocumentQuery.get(j));
                        PendingDocQueryMsg.add(mainActivity.DocumentQuery.get(j).strQueryMsg);
                    }
                }

                mainActivity.SQLite_getMembers(mainActivity.selectedMemberID, "MemberID");
                if(mainActivity.Member.size() > 0) {
                    mainActivity.SQLite_getMembers(mainActivity.Member.get(0).MemberID, "MainMemberID");
                    for (int i = 0; i < mainActivity.Member.size(); i++) {
                        mainActivity.SQLite_getMemberDocRcvd(mainActivity.Member.get(i).MemberID);
                    }
                    mainActivity.DocumentPhotos.clear();
                    mainActivity.tDocumentPhotos.clear();
                    for (int k = 0; k < mainActivity.MemberDocRcvd.size(); k++) {
                        mainActivity.SQLite_getDocumentPhotos(mainActivity.MemberDocRcvd.get(k).MemberDocuemntsRcvdID);
                    }
                }
//bmp 02-Jul-26
//                ArrayAdapter<String> adapterDocQuery = new ArrayAdapter<String>(mainActivity.getBaseContext(),
//                        R.layout.simple_spinner_item, PendingDocQueryMsg);
//                adapterDocQuery.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                ArrayAdapter<String> adapterDocQuery = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                        R.layout.spinner_item_custom_multiline, PendingDocQueryMsg);
                adapterDocQuery.setDropDownViewResource(R.layout.custom_multiline_spinner_dropdown_item);
//bmp 02-Jul-26
                Spinner spDocQuery = (Spinner) rootView.findViewById(R.id.spinnerDocQuery);
                spDocQuery.setAdapter(adapterDocQuery);
                spDocQuery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mainActivity.selectedDocQuery = PendingDocQuery.get(position);
                        TextView tv = (TextView) rootView.findViewById(R.id.textViewFamilyMemberName);
                        tv.setText(String.valueOf(mainActivity.selectedDocQuery.FamilyMemberName));
                        tv = (TextView) rootView.findViewById(R.id.textViewDocumentType);
                        String strDocType = "";
                        for(int i=0; i<DocumentIDMaster.size(); i++) {
                            if(mainActivity.selectedDocQuery.DocumentTypeID == DocumentIDMaster.get(i)) {
                                strDocType = DocumentMarathiDescriptionMaster.get(i);
                                break;
                            }
                        }
                        tv.setText(strDocType);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                spDocQuery.setSelection(0);
                spDocQuery.setFocusable(true);
                spDocQuery.setFocusableInTouchMode(true);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spQueryMember.setSelection(0);
        spQueryMember.setFocusable(true);
        spQueryMember.setFocusableInTouchMode(true);

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
                        case ufin_DOCQUERY_DIALOG_STATE_NONE:
                            break;
                    }
                    dlgState = ufin_DOCQUERY_DIALOG_STATE_NONE;
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
                        case ufin_DOCQUERY_DIALOG_STATE_NONE:
                            break;
                        case ufin_DOCQUERY_DIALOG_STATE_CLOSE:
                            mainActivity.InitDesktop();
                            break;
                        case ufin_DIALOG_STATE_SUBMIT_QUERY_DOCS:
                            AsyncCallWS_SubmitQueryDocs taskSubmitQueryDocs = new AsyncCallWS_SubmitQueryDocs();
                            taskSubmitQueryDocs.execute();
                            break;
                    }
                    dlgState = ufin_DOCQUERY_DIALOG_STATE_NONE;
                }
            });
        }
//        dlgAlert.setCancelable(true);
    }
    @Override
    public void onClick(View v) {
        String strFilepath = mainActivity.baseDir + "/data/";
        String strFilename = "unknown";
        switch (v.getId()) {
            case R.id.textViewQtyPhotoDoc:
                if(mainActivity.PhotoDoc.size() == 0)
                    currentphotoDoc = 0;
                else if(currentphotoDoc < mainActivity.PhotoDoc.size())
                    currentphotoDoc++;
                else
                    currentphotoDoc = 1;
                String strCount = String.valueOf(currentphotoDoc);
                strCount = strCount + "/" + String.valueOf(mainActivity.PhotoDoc.size());
                TextView tv = (TextView) rootView.findViewById(R.id.textViewQtyPhotoDoc);
                tv.setText(strCount);
                break;
            case R.id.imageButtonPhotoDoc:
//bmp 21-Jul-26
                if(mainActivity.selectedMemberID <= 0)
                    break;
                TextView tv1 = (TextView) rootView.findViewById(R.id.textViewFamilyMemberName);
                if(tv1.getText().toString().isEmpty())
                    break;
                tv1 = (TextView) rootView.findViewById(R.id.textViewDocumentType);
                if(tv1.getText().toString().isEmpty())
                    break;
//bmp 21-Jul-26
                IDRequest = ID_REQUEST_PHOTO_DocQuery;
                File outFile = new File(strFilepath + strFilename + ".jpg");
                /*Uri outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", outFile);
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outuri);
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                startActivityIntent.launch(cameraIntent);*/

                //RK
                Intent cameraIntent = new Intent();
                cameraIntent.setClassName(
                        "com.example.cameraapp",
                        "com.example.cameraapp.DocumentScannerActivity"
                );
                startActivityIntent.launch(cameraIntent);
                break;
            case R.id.imageButtonPreviewDoc:
                if(mainActivity.PhotoDoc.size() > 0) {
                    strFilename = mainActivity.PhotoDoc.get(currentphotoDoc - 1);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    outFile = new File(strFilepath + strFilename);
                   Uri outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", outFile);
                    intent.setDataAndType(outuri, "image/*");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                }
                break;
            case R.id.imageButtonDeleteDoc:
                if(mainActivity.PhotoDoc.size() == 0)
                    break;
                strFilename = mainActivity.PhotoDoc.get(currentphotoDoc - 1);
                File file = new File(strFilepath + strFilename);
                if(file.exists()) {
                    file.delete();
                   Uri outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", file);
                    mainActivity.getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, outuri));
                }
                String[] strSplit = strFilename.split("\\.");
                String strTemp1 = strSplit[0];
                for(int i=0; i<mainActivity.DocumentPhotos.size(); i++) {
                    String strTemp2 = String.valueOf(mainActivity.DocumentPhotos.get(i).DocumentPhotoID);
                    if(strTemp1.equalsIgnoreCase(strTemp2)) {
                        mainActivity.DocumentPhotos.get(i).IsActive = 0;
                        break;
                    }
                }

//                RefreshScreen();
                tv = (TextView) rootView.findViewById(R.id.textViewQtyPhotoDoc);
                tv.setText("0/0");
                ImageButton imgbt2 = (ImageButton) rootView.findViewById(R.id.imageButtonPreviewDoc);
                imgbt2.setEnabled(false);
                if(mainActivity.PhotoDoc.size() > 0) {
                    currentphotoDoc = 1;
                    tv.setText("1/" + String.valueOf(mainActivity.PhotoDoc.size()));
                    imgbt2.setEnabled(true);
                }
                break;
            case R.id.buttonCloseDocQuery:
                mainActivity.InitDesktop();
                break;
            case R.id.buttonSaveDocQuery:
                if(mainActivity.DocumentPhotos.size() > mainActivity.tDocumentPhotos.size()) {
                    CreateAlertDialog(true, true);
                    dlgState = ufin_DIALOG_STATE_SUBMIT_QUERY_DOCS;
                    dlgAlert.setMessage(MSG_ufin_DOCQUERY_CONFIRM_SAVE);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    textView.setTextColor(Color.RED);
                } else {
//                    Toast toast = Toast.makeText(mainActivity.getBaseContext(), "No Document Photos to Submit.", Toast.LENGTH_SHORT);
//                    ViewGroup group = (ViewGroup) toast.getView();
//                    TextView messageTextView = (TextView) group.getChildAt(0);
//                    messageTextView.setTextSize(28);
//                    toast.show();
                    ShowToastMessage("No Document Photos to Submit.");
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
                            case ID_REQUEST_PHOTO_DocQuery:
                                strFilename = "unknown";
                                File outFile = new File(strFilepath + strFilename + ".jpg");
                                if(!outFile.exists())
                                    break;
                                if(outFile.length() >= MAX_PHOTO_FILESIZE) {
//bmp 28-Apr-26
//                                ShowdlgAlertonActivityResult("Incorrect Image file size, Adjust Camera Setting and Try Again!");
//                                break;
                                    String currentPhotoPath = strFilepath + strFilename + ".jpg";
                                    mainActivity.AdjustImageFileSize(currentPhotoPath);
//bmp 28-Apr-26
                                }
                                strFilepath = mainActivity.baseDir + "/data/";
                                strFilename = "";

                                TextView tv = (TextView) rootView.findViewById(R.id.textViewQtyPhotoDoc);
                                CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxPhotoDoc);

                                String strDocPhotoID = "";
                                String strDocNo = "";
                                int iIsAgeProofDoc = 0;
                                int indexMemberDocRcvd = -1;
                                for(int i=0; i<mainActivity.MemberDocRcvd.size(); i++) {
                                    if(     (mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == mainActivity.selectedDocQuery.DocumentTypeID) &&
                                            (mainActivity.MemberDocRcvd.get(i).IsActive == 1) &&
                                            (   (mainActivity.MemberDocRcvd.get(i).MemberID == mainActivity.selectedDocQuery.MainMemberID) ||
                                                    (mainActivity.MemberDocRcvd.get(i).MemberID == mainActivity.selectedDocQuery.FamilyMemberID)
                                            )
                                    ) {
                                        strDocNo = mainActivity.MemberDocRcvd.get(i).DocumentNo;
                                        iIsAgeProofDoc = mainActivity.MemberDocRcvd.get(i).IsAgeProofDoc;
                                        indexMemberDocRcvd = i;
                                        for(int k=0; k<mainActivity.DocumentPhotos.size(); k++) {
                                            if(mainActivity.MemberDocRcvd.get(i).MemberDocuemntsRcvdID ==
                                                    mainActivity.DocumentPhotos.get(k).MemberDocuemntsRcvdID) {
                                                String strTemp = String.valueOf(mainActivity.DocumentPhotos.get(k).DocumentPhotoID);
                                                File file = new File(strFilepath + strTemp + ".jpg");
                                                if(!file.exists()) {
                                                    strDocPhotoID = strTemp;
                                                    mainActivity.DocumentPhotos.get(k).IsActive = 1;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                if(strDocPhotoID.length() > 0) {
                                    strFilename = strDocPhotoID + ".jpg";
                                } else {
                                    long lTemp = 0;
                                    if(indexMemberDocRcvd < 0) {
                                    } else {
//bmp 29-May-26
//                                        lTemp = AddDocumentQueryPhotos(
//                                                mainActivity.MemberDocRcvd.get(indexMemberDocRcvd).MemberDocuemntsRcvdID,
//                                                0, true);
                                        lTemp = AddDocumentQueryPhotos(
                                                mainActivity.MemberDocRcvd.get(indexMemberDocRcvd),
                                                0, true);
//bmp 29-May-26
                                    }
                                    strDocPhotoID = String.valueOf(lTemp);
                                    strFilename = strDocPhotoID + ".jpg";
                                }
                                mainActivity.PhotoDoc.add(strFilename);
                                currentphotoDoc = mainActivity.PhotoDoc.size();
                                String strCount = String.valueOf(currentphotoDoc);
                                strCount = strCount + "/" + String.valueOf(mainActivity.PhotoDoc.size());
                                tv.setText(strCount);
                                cb.setChecked(true);
                                ImageButton imgbt2 = (ImageButton) rootView.findViewById(R.id.imageButtonPreviewDoc);
                                imgbt2.setEnabled(true);

                                //Copy image file to 'upload' folder only on 'PIF Submit'
                                File imageFile = new File(strFilepath + strFilename);
                                outFile.renameTo(imageFile);
                                break;
                        }
                    }
                }
            });*/

    //RK
    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            String strFilepath = mainActivity.baseDir + "/data/";
                            String strFilename = "unknown";
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                // . RECEIVE IMAGE URI FROM CAMERA APP
                                Intent data = result.getData();
                                if (data == null) {
                                    ShowdlgAlertonActivityResult(
                                            "No result received from Camera App!"
                                    );
                                    return;
                                }

                                Uri imageUri = data.getData();
                                // Fallback: EXTRA_STREAM
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

                                //  DESTINATION FILE IN FINANCE APP
                                File outFile = new File(
                                        strFilepath + strFilename + ".jpg"
                                );
                                // COPY CAMERA APP IMAGE TO FINANCE APP
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

                                    FileOutputStream outputStream = new FileOutputStream(outFile);
                                    byte[] buffer = new byte[8192];
                                    int length;
                                    while ((length = inputStream.read(buffer)) != -1) {
                                        outputStream.write(buffer, 0, length);
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

                                //  VERIFY FILE
                                if (!outFile.exists() || outFile.length() == 0) {
                                    ShowdlgAlertonActivityResult(
                                            "Image was not saved!"
                                    );
                                    return;
                                }

                                // OLD CODE STARTS HERE
                                switch (IDRequest) {
                                    case ID_REQUEST_PHOTO_DocQuery:
                                        strFilename = "unknown";
                                        outFile = new File(
                                                strFilepath +
                                                        strFilename +
                                                        ".jpg"
                                        );
                                        if (!outFile.exists())
                                            break;
                                        //  IMAGE FILE SIZE
                                        if (outFile.length() >= MAX_PHOTO_FILESIZE) {
                                            String currentPhotoPath = strFilepath + strFilename + ".jpg";
                                            mainActivity.AdjustImageFileSize(currentPhotoPath);
                                        }

                                        //  RESET PATH / FILENAME
                                        strFilepath = mainActivity.baseDir + "/data/";
                                        strFilename = "";
                                        // UPDATE PHOTO COUNT
                                        TextView tv = (TextView) rootView.findViewById(R.id.textViewQtyPhotoDoc);
                                        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxPhotoDoc);
                                        // FIND DOCUMENT PHOTO
                                        String strDocPhotoID = "";
                                        String strDocNo = "";
                                        int iIsAgeProofDoc = 0;
                                        int indexMemberDocRcvd = -1;
                                        for (int i = 0; i < mainActivity.MemberDocRcvd.size(); i++) {
                                            if (    (mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == mainActivity.selectedDocQuery.DocumentTypeID)
                                                    && (mainActivity.MemberDocRcvd.get(i).IsActive == 1)
                                                    && (    (mainActivity.MemberDocRcvd.get(i).MemberID == mainActivity.selectedDocQuery.MainMemberID) ||
                                                            (mainActivity.MemberDocRcvd.get(i).MemberID == mainActivity.selectedDocQuery.FamilyMemberID))
                                            ) {
                                                strDocNo = mainActivity.MemberDocRcvd.get(i).DocumentNo;
                                                iIsAgeProofDoc = mainActivity.MemberDocRcvd.get(i).IsAgeProofDoc;
                                                indexMemberDocRcvd = i;
                                                // FIND AVAILABLE DOCUMENT PHOTO ID
                                                for (int k = 0; k < mainActivity.DocumentPhotos.size(); k++) {
                                                    if (mainActivity.MemberDocRcvd.get(i).MemberDocuemntsRcvdID == mainActivity.DocumentPhotos.get(k).MemberDocuemntsRcvdID) {
                                                        String strTemp = String.valueOf(mainActivity.DocumentPhotos.get(k).DocumentPhotoID);
                                                        File file = new File(strFilepath + strTemp + ".jpg");
                                                        if (!file.exists()) {
                                                            strDocPhotoID = strTemp;
                                                            mainActivity.DocumentPhotos.get(k).IsActive = 1;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        //  CREATE / GET DOCUMENT PHOTO ID
                                        if (strDocPhotoID.length() > 0) {
                                            strFilename = strDocPhotoID + ".jpg";
                                        } else {
                                            long lTemp = 0;
                                            if (indexMemberDocRcvd < 0) {
                                            } else {
                                                lTemp = AddDocumentQueryPhotos(mainActivity.MemberDocRcvd.get(indexMemberDocRcvd), 0, true);
                                            }
                                            strDocPhotoID = String.valueOf(lTemp);
                                            strFilename = strDocPhotoID + ".jpg";
                                        }
                                        //  ADD PHOTO TO LIST
                                        mainActivity.PhotoDoc.add(strFilename);
                                        currentphotoDoc = mainActivity.PhotoDoc.size();
                                        String strCount = String.valueOf(currentphotoDoc);
                                        strCount = strCount + "/" + String.valueOf(mainActivity.PhotoDoc.size());

                                        tv.setText(strCount);
                                        cb.setChecked(true);
                                        // . ENABLE PREVIEW BUTTO
                                        ImageButton imgbt2 = (ImageButton) rootView.findViewById(R.id.imageButtonPreviewDoc);
                                        imgbt2.setEnabled(true);

                                        // RENAME UNKNOWN.JPG
                                        //     TO ACTUAL DOCUMENT PHOTO NAME
                                        File imageFile = new File(strFilepath + strFilename);
                                        outFile.renameTo(imageFile);
                                        break;
                                }
                            } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
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
    public void SQLite_getDocumentMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.DocumentMaster.COLUMN_NAME_DocumentID,
                ufinDbContract.DocumentMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.DocumentMaster.COLUMN_NAME_Description,
                ufinDbContract.DocumentMaster.COLUMN_NAME_SequenceID,
                ufinDbContract.DocumentMaster.COLUMN_NAME_CheckDuplicate,
        };
        String Active = "1";

        String selection1 = ufinDbContract.DocumentMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.DocumentMaster.COLUMN_NAME_SequenceID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.DocumentMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        DocumentIDMaster.clear();
        DocumentMarathiDescriptionMaster.clear();
        CheckDuplicateMaster.clear();
        DocumentIDMaster.add(0);
        DocumentMarathiDescriptionMaster.add("");
        CheckDuplicateMaster.add(0);

        DocumentIDMaster.add(clsDocumentIDs.MEMBER_DOC_ID_ONLINE_COLLECTION);
        DocumentMarathiDescriptionMaster.add("Online Payment Transaction screenshot");
        CheckDuplicateMaster.add(0);

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            DocumentIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.DocumentMaster.COLUMN_NAME_DocumentID))));
                            String str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.DocumentMaster.COLUMN_NAME_MarathiDescription));
                            if ((str1 == null) || str1.isEmpty() || str1.equalsIgnoreCase(" ")) {
                                str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.DocumentMaster.COLUMN_NAME_Description));
                            }
                            DocumentMarathiDescriptionMaster.add(str1);
                            try {
                                CheckDuplicateMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.DocumentMaster.COLUMN_NAME_CheckDuplicate))));
                            } catch (Exception e) {
                                CheckDuplicateMaster.add(0);
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
    private class AsyncCallWS_SubmitQueryDocs extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.progressBarDocQuery);
            if (pb != null) {
                pb.setIndeterminate(true);
                pb.setVisibility(View.VISIBLE);
            }

            Button bt = (Button) rootView.findViewById(R.id.buttonCloseDocQuery);
            bt.setEnabled(false);
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
        @Override
        protected Void doInBackground(String... strings) {
            SubmitMemberQueryDocs();

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            ProgressBar pb = (ProgressBar) rootView.findViewById(R.id.progressBarDocQuery);
            if(pb != null)
                pb.setVisibility(View.INVISIBLE);

            mainActivity.InitDesktop();
        }
    }
    public void SubmitMemberQueryDocs() {
        String strFilepath = mainActivity.baseDir + "/data/";
        String strFilename = "";
        for(int i=mainActivity.tDocumentPhotos.size(); i<mainActivity.DocumentPhotos.size(); i++) {
            LastTimeInMillis += 1000;
            if(mainActivity.SQLite_saveDocumentPhoto(i, LastTimeInMillis, true)) {
                if (mainActivity.DocumentPhotos.get(i).IsActive == 1) {
                    strFilename = String.valueOf(mainActivity.DocumentPhotos.get(i).DocumentPhotoID) + ".jpg";
                    try {
                        File imageFile = new File(strFilepath + strFilename);
                        InputStream in = null;
                        OutputStream out = null;
                        byte[] buffer = new byte[1024];
                        File uploadFile = new File(mainActivity.baseDir + "/upload/" + strFilename);
                        if (!uploadFile.exists()) {
                            in = new FileInputStream(imageFile);
                            out = new FileOutputStream(uploadFile);
                            int read;
                            while ((read = in.read(buffer)) != -1) {
                                out.write(buffer, 0, read);
                            }
                            in.close();
                            in = null;
                            out.flush();
                            out.close();
                            out = null;

                            mainActivity.SyncDocUpload.add(strFilename);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if(mainActivity.selectedDocQuery.DocumentTypeID == clsDocumentIDs.MEMBER_DOC_ID_ONLINE_COLLECTION) {
            for(int i=0; i<mainActivity.CollectionMemberList.size(); i++) {
                if (    (mainActivity.CollectionMemberList.get(i).MemberID == mainActivity.selectedDocQuery.FamilyMemberID) ||
                        (mainActivity.CollectionMemberList.get(i).MemberID == mainActivity.selectedDocQuery.MainMemberID) ) {
//bmp 01-Jul-26
//                    mainActivity.CollectionMemberList.get(i).MemberStatus
//                            = mainActivity.CollectionMemberList.get(i).MemberStatus
//                            - clsCollectionMember.COLLECTION_MEMBER_STATUS_QUERY_OFFSET;
//bmp 01-Jul-26

                    ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
                    SQLiteDatabase dbWr = mDbHelper.getWritableDatabase();
                    try {
//bmp 01-Jul-26
//                        ContentValues values1 = new ContentValues();
//                        values1.put(ufinDbContract.MemberRecovery.COLUMN_NAME_MemberStatus, mainActivity.CollectionMemberList.get(i).MemberStatus);
//                        String whereClause = ufinDbContract.MemberRecovery.COLUMN_NAME_MemberID + "=" + "'" + String.valueOf(mainActivity.CollectionMemberList.get(i).MemberID) + "'";
//                        long lTemp1 = dbWr.update(ufinDbContract.MemberRecovery.TABLE_NAME, values1, whereClause, null);
//                        if (lTemp1 > 0) {
//                        }
//bmp 01-Jul-26

                        ContentValues values2 = new ContentValues();
                        String strFilter = "FQ";
                        String whereClause2 = ufinDbContract.MessagesInbox.COLUMN_NAME_MessageFilter + "=" + "'" + strFilter + "'"
                                + " AND " + ufinDbContract.MessagesInbox.COLUMN_NAME_MessageID + "=" + "'" + String.valueOf(mainActivity.selectedDocQuery.lMsgID) + "'";
                        long lTemp2 = dbWr.delete(ufinDbContract.MessagesInbox.TABLE_NAME, whereClause2, null);
                        if (lTemp2 > 0) {
                        }
                    } finally {
                        dbWr.close();
                    }
//bmp 04-Jun-26
                    //Delete selectedDocQuery
                    for(int j=0; j<mainActivity.DocumentQuery.size(); j++) {
                        if(mainActivity.DocumentQuery.get(j).lMsgID == mainActivity.selectedDocQuery.lMsgID) {
                            mainActivity.DocumentQuery.remove(j);
                            break;
                        }
                    }
//bmp 04-Jun-26
                    break;
                }
            }
            return;
        }
        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        SQLiteDatabase db1 = mDbHelper.getReadableDatabase();

        String[] projection10 = {
                ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_GuarantorName,
                ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_GuarantorRelationID,
                ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_LoanAmount,
                ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_LoanDuration,
                ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_MemberStatus,
                ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_LoanApplicant,
                ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_ContactNo,
                ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_GroupID,
                ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_iEligibleLoan,
                ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_selectedPositionLoanProduct,
        };
        String selection10 = ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_MemberID + "=" + "'" + String.valueOf(mainActivity.Member.get(0).MemberID) + "'";

        Cursor c10 = db1.query(
                ufinDbContract.NewGroupMemberInfo.TABLE_NAME,  // The table to query
                projection10,                               // The columns to return
                selection10,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );

        clsNewGroupMemberInfo newGroupMemberInfo = new clsNewGroupMemberInfo();
        newGroupMemberInfo.Init_NewGroupMemberInfo();

        try {
            if (c10 != null && c10.moveToFirst()) {
                if (c10.getCount() > 0) {
                    try {
                        newGroupMemberInfo.MemberID = mainActivity.Member.get(0).MemberID;
                        newGroupMemberInfo.GuarantorName = c10.getString(c10.getColumnIndexOrThrow(ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_GuarantorName));
                        newGroupMemberInfo.GuarantorRelationID = Integer.valueOf(c10.getString(c10.getColumnIndexOrThrow(ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_GuarantorRelationID)));
                        newGroupMemberInfo.LoanAmount = Integer.valueOf(c10.getString(c10.getColumnIndexOrThrow(ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_LoanAmount)));
                        newGroupMemberInfo.LoanDuration = Integer.valueOf(c10.getString(c10.getColumnIndexOrThrow(ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_LoanDuration)));
                        newGroupMemberInfo.MemberStatus = Integer.valueOf(c10.getString(c10.getColumnIndexOrThrow(ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_MemberStatus)));
                        newGroupMemberInfo.LoanApplicant = Integer.valueOf(c10.getString(c10.getColumnIndexOrThrow(ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_LoanApplicant)));
                        newGroupMemberInfo.ContactNo = c10.getString(c10.getColumnIndexOrThrow(ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_ContactNo));
                        newGroupMemberInfo.GroupID = Long.valueOf(c10.getString(c10.getColumnIndexOrThrow(ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_GroupID)));
                        newGroupMemberInfo.selectedPositionLoanProduct = Integer.valueOf(c10.getString(c10.getColumnIndexOrThrow(ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_selectedPositionLoanProduct)));
                        newGroupMemberInfo.iEligibleLoan = Integer.valueOf(c10.getString(c10.getColumnIndexOrThrow(ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_iEligibleLoan)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            if (c10 != null) {
                c10.close();
            }
        }

        newGroupMemberInfo.MemberStatus = clsGroupMember.MEMBER_STATUS_PIF_SUBMIT;
        LastTimeInMillis += 1000;
        mainActivity.SQLite_saveGroupMemberInfo(newGroupMemberInfo, LastTimeInMillis, true);

        for(int k=0; k<mainActivity.RevisitMemberList.size(); k++) {
            if(mainActivity.RevisitMemberList.get(k).MemberID == mainActivity.Member.get(0).MemberID) {
                mainActivity.RevisitMemberList.get(k).RevisitMemberStatus = clsRevisitMember.REVISIT_MEMBER_STATUS_PIF_SUBMITTED;
                mainActivity.RevisitMemberList.get(k).Edited = true;
                mainActivity.SQLite_updateRevisitMember(k, false);
                break;
            }
        }
    }
//    public long AddDocumentQueryPhotos(long DocRcvdID, long groupID, boolean syncRecord) {
//        clsDocumentPhoto newDocPhoto = new clsDocumentPhoto();
//        newDocPhoto.Init_DocumentPhoto();
//        newDocPhoto.MemberDocuemntsRcvdID = DocRcvdID;
//        newDocPhoto.GroupID = groupID;
//
//        mainActivity.DocumentPhotos.add(newDocPhoto);
//        int newIndex = mainActivity.DocumentPhotos.size() - 1;
//
//        Calendar cal = Calendar.getInstance();
//        while(LastTimeInMillis > cal.getTimeInMillis()) {
//            cal = Calendar.getInstance();
//        }
//        LastTimeInMillis = cal.getTimeInMillis();
//
//        LastTimeInMillis += 1000;
//        mainActivity.SQLite_saveDocumentPhoto(newIndex, LastTimeInMillis, syncRecord);
//        newDocPhoto.DocumentPhotoID = mainActivity.DocumentPhotos.get(newIndex).DocumentPhotoID;
//
//        return newDocPhoto.DocumentPhotoID;
//    }
    public long AddDocumentQueryPhotos(clsMemberDocRcvd docRcvd, long groupID, boolean syncRecord) {
        clsDocumentPhoto newDocPhoto = new clsDocumentPhoto();
        newDocPhoto.Init_DocumentPhoto();
        newDocPhoto.MemberDocumentsID = docRcvd.MemberDocumentsID;
        newDocPhoto.MemberDocuemntsRcvdID = docRcvd.MemberDocuemntsRcvdID;
        newDocPhoto.GroupID = groupID;

        mainActivity.DocumentPhotos.add(newDocPhoto);
        int newIndex = mainActivity.DocumentPhotos.size() - 1;

        Calendar cal = Calendar.getInstance();
        while(LastTimeInMillis > cal.getTimeInMillis()) {
            cal = Calendar.getInstance();
        }
        LastTimeInMillis = cal.getTimeInMillis();

        LastTimeInMillis += 1000;
        mainActivity.SQLite_saveDocumentPhoto(newIndex, LastTimeInMillis, syncRecord);
        newDocPhoto.DocumentPhotoID = mainActivity.DocumentPhotos.get(newIndex).DocumentPhotoID;

        return newDocPhoto.DocumentPhotoID;
    }
}
