package com.example.ufin_lso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_CANCELED;

public class DocsFragment extends Fragment implements View.OnClickListener {
    View rootView;
    MainActivity mainActivity;

    private static final int CAMERA_REQUEST = 1;
    public static final int MAX_PHOTO_FILESIZE             = (4 * 1024 * 1024);
//    public static final int MAX_PHOTO_FILESIZE             = (8 * 1024 * 1024);

    int DocumentRequest = -1;
    private boolean dbMaster = false;
    private ArrayList<Integer> CheckDuplicateMaster = new ArrayList<Integer>();
    private ArrayList<Integer> DocumentIDMaster = new ArrayList<Integer>();
    private ArrayList<String> DocumentMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Integer> StoredDocumentIDMaster = new ArrayList<Integer>();
    private ArrayList<String> StoredDocumentMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Integer> FamilyMemberIndexMaster = new ArrayList<Integer>();
    private ArrayList<String> FamilyMemberNameMaster = new ArrayList<String>();
    private ArrayList<Integer> RelationIDMaster = new ArrayList<Integer>();
    private ArrayList<String> RelationMarathiDescriptionMaster = new ArrayList<String>();
    private int RelationIDMainMember;
    private int RelationIndexMainMember;
    int selectedDocumentID, selectedFamilyMemberIndex, CheckDuplicate;
    private static int currentphotoDoc = 1;
    public ArrayList<String> PhotoDoc = new ArrayList<String>();

    public DocsFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_docs, container, false);
        mainActivity = (MainActivity) getActivity();

        if(!dbMaster) {
            SQLite_getDocumentMaster();
            SQLite_getRelationMaster();

            FamilyMemberIndexMaster.clear();
            FamilyMemberNameMaster.clear();
            FamilyMemberIndexMaster.add(-1);
            FamilyMemberNameMaster.add("");
            for(int i=0; i<mainActivity.Member.size(); i++) {
                if(mainActivity.Member.get(i).IsActive == 1) {
                    FamilyMemberIndexMaster.add(i);
                    String strRelation = "";
                    for(int k=0; k<RelationIDMaster.size(); k++) {
                        if(mainActivity.Member.get(i).RelationID == RelationIDMaster.get(k)) {
                            strRelation = RelationMarathiDescriptionMaster.get(k);
                            break;
                        }
                    }
                    String strFname = mainActivity.Member.get(i).Fname;
                    FamilyMemberNameMaster.add(strFname + ":" + strRelation);
                }
            }

            PhotoDoc.clear();
            dbMaster = true;
        }

        selectedDocumentID = -1;
        CheckDuplicate = 0;
        selectedFamilyMemberIndex = -1;

        ImageButton bt = (ImageButton) rootView.findViewById(R.id.imageButtonPhotoDoc);
        bt.setOnClickListener(this);
        bt = (ImageButton) rootView.findViewById(R.id.imageButtonPreviewDoc);
        bt.setOnClickListener(this);
        bt = (ImageButton) rootView.findViewById(R.id.imageButtonDeleteDoc);
        bt.setOnClickListener(this);
        bt = (ImageButton) rootView.findViewById(R.id.imageButtonDocumentsPack);
        bt.setOnClickListener(this);

        TextView tv = (TextView) rootView.findViewById(R.id.textViewQtyPhotoDoc);
        tv.setOnClickListener(this);

        EditText et = (EditText) rootView.findViewById(R.id.editTextDocNo);
        if( (selectedFamilyMemberIndex < 0) || (selectedDocumentID < 0) )
            et.setEnabled(false);

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    String strDocumentNo = v.getText().toString().trim();
                    if ((strDocumentNo != null) && (strDocumentNo.length() > 0)) {
                        if(!ValidateDocNumber(strDocumentNo)) {
                            ShowdlgAlert("अवैध/चुकीचा कागदपत्र क्रमांक: " + strDocumentNo);
                        } else {
                            UpdateDocument(selectedDocumentID, strDocumentNo);
                            if(CheckDuplicateDocNumber(strDocumentNo)) {
                                ShowdlgAlert("डुप्लिकेट कागदपत्र क्रमांक: " + strDocumentNo);
                            }
                        }
                    }
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    if(mainActivity.FragmentVisibleToUser !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS)
                        return;
                    if (mainActivity.iCurrentScreen !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS)
                        return;

                    String strDocumentNo = ((TextView)view).getText().toString().trim();
                    if ((strDocumentNo != null) && (strDocumentNo.length() > 0)) {
                        if(!ValidateDocNumber(strDocumentNo)) {
                            ShowdlgAlert("अवैध/चुकीचा कागदपत्र क्रमांक: " + strDocumentNo);
                        } else {
                            UpdateDocument(selectedDocumentID, strDocumentNo);
                            if(CheckDuplicateDocNumber(strDocumentNo)) {
                                ShowdlgAlert("डुप्लिकेट कागदपत्र क्रमांक: " + strDocumentNo);
                            }
                        }
                    }
                }
            }
        });

        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxIsAgeProofDoc);
        if( (selectedFamilyMemberIndex < 0) || (selectedDocumentID < 0) )
            cb.setEnabled(false);

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iChecked = 0;
                if(((CheckBox) view).isChecked())
                    iChecked = 1;
                UpdateDocument(selectedDocumentID, iChecked);
            }
        });

        ArrayAdapter<String> adapterFamilyMember = new ArrayAdapter<String>(getActivity().getBaseContext(),
            R.layout.simple_spinner_item, FamilyMemberNameMaster);
        adapterFamilyMember.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner sp1 = (Spinner) rootView.findViewById(R.id.spinnerFamilyMemberName);
        sp1.setAdapter(adapterFamilyMember);
        sp1.setSelection(0);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS)
                    return;
                if(selectedFamilyMemberIndex == FamilyMemberIndexMaster.get(position))
                    return;

                selectedFamilyMemberIndex = FamilyMemberIndexMaster.get(position);

                selectedDocumentID = -1;
                CheckDuplicate = 0;
                Spinner sp2 = (Spinner) rootView.findViewById(R.id.spinnerDocumentType);
                sp2.setSelection(0);
                EditText et = (EditText) rootView.findViewById(R.id.editTextDocNo);
                et.setText("");
                CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxIsAgeProofDoc);
                cb.setChecked(false);

                RefreshScreen();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<String> adapterDocument = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, DocumentMarathiDescriptionMaster);
        adapterDocument.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner sp2 = (Spinner) rootView.findViewById(R.id.spinnerDocumentType);
        sp2.setAdapter(adapterDocument);
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS)
                    return;
                if(selectedDocumentID == DocumentIDMaster.get(position))
                    return;

                selectedDocumentID = DocumentIDMaster.get(position);
                CheckDuplicate = CheckDuplicateMaster.get(position);

                EditText et = (EditText) rootView.findViewById(R.id.editTextDocNo);
                et.setText("");
                CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxIsAgeProofDoc);
                cb.setChecked(false);

                RefreshScreen();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        et = (EditText) rootView.findViewById(R.id.editTextRationcardFname);
        if( (selectedFamilyMemberIndex != 0) || (selectedDocumentID != mainActivity.DocumentIDs.MEMBER_DOC_ID_RATIONCARD) )
            et.setEnabled(false);

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    String strName = v.getText().toString();
                    mainActivity.Member.get(0).RationCard_Fname = strName;
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    if(mainActivity.FragmentVisibleToUser !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS)
                        return;
                    if (mainActivity.iCurrentScreen !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS)
                        return;

                    String strName = ((TextView)view).getText().toString();
                    mainActivity.Member.get(0).RationCard_Fname = strName;
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextRationcardMname);
        if( (selectedFamilyMemberIndex != 0) || (selectedDocumentID != mainActivity.DocumentIDs.MEMBER_DOC_ID_RATIONCARD) )
            et.setEnabled(false);

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    String strName = v.getText().toString();
                    mainActivity.Member.get(0).RationCard_Mname = strName;
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    if(mainActivity.FragmentVisibleToUser !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS)
                        return;
                    if (mainActivity.iCurrentScreen !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS)
                        return;

                    String strName = ((TextView)view).getText().toString();
                    mainActivity.Member.get(0).RationCard_Mname = strName;
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextRationcardLname);
        if( (selectedFamilyMemberIndex != 0) || (selectedDocumentID != mainActivity.DocumentIDs.MEMBER_DOC_ID_RATIONCARD) )
            et.setEnabled(false);

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    String strName = v.getText().toString();
                    mainActivity.Member.get(0).RationCard_Lname = strName;
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    if(mainActivity.FragmentVisibleToUser !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS)
                        return;
                    if (mainActivity.iCurrentScreen !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS)
                        return;

                    String strName = ((TextView)view).getText().toString();
                    mainActivity.Member.get(0).RationCard_Lname = strName;
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextRationcardAddress);
        if( (selectedFamilyMemberIndex != 0) || (selectedDocumentID != mainActivity.DocumentIDs.MEMBER_DOC_ID_RATIONCARD) )
            et.setEnabled(false);

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    String strAddress = v.getText().toString();
                    mainActivity.MemberFamilyDetails.AddressOnRationcard = strAddress;
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    if(mainActivity.FragmentVisibleToUser !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS)
                        return;
                    if (mainActivity.iCurrentScreen !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS)
                        return;

                    String strAddress = ((TextView)view).getText().toString();
                    mainActivity.MemberFamilyDetails.AddressOnRationcard = strAddress;
                }
            }
        });

        return rootView;
    }

    public void UpdateDocument(int DocID, int isAgeProofDoc) {
        int i;
        if(isAgeProofDoc == 1) {
            for (i = 0; i < mainActivity.MemberDocRcvd.size(); i++) {
                if(mainActivity.MemberDocRcvd.get(i).MemberID == mainActivity.Member.get(selectedFamilyMemberIndex).MemberID) {
                    if(mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == DocID)
                        mainActivity.MemberDocRcvd.get(i).IsAgeProofDoc = 1;
                    else    //Only one document can be selected for Age proof
                        mainActivity.MemberDocRcvd.get(i).IsAgeProofDoc = 0;
                }
            }
        } else {
            for (i = 0; i < mainActivity.MemberDocRcvd.size(); i++) {
                if ((mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == DocID) &&
                        (mainActivity.MemberDocRcvd.get(i).MemberID == mainActivity.Member.get(selectedFamilyMemberIndex).MemberID)) {
                    mainActivity.MemberDocRcvd.get(i).IsAgeProofDoc = 0;
                    break;
                }
            }
        }
    }

    public void UpdateDocument(int DocID, String strDocNo) {
        int i;
        int iCount = 0;
        for (i = 0; i < mainActivity.MemberDocRcvd.size(); i++) {
            if ((mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == DocID) &&
                    (mainActivity.MemberDocRcvd.get(i).MemberID == mainActivity.Member.get(selectedFamilyMemberIndex).MemberID)) {
                mainActivity.MemberDocRcvd.get(i).DocumentNo = strDocNo;
                iCount++;
            }
        }
        if (iCount == 0) {
            AddMemberDocRcvd(DocID, strDocNo);
        }
        if(DocID == mainActivity.DocumentIDs.MEMBER_DOC_ID_BANK_PASSBOOK) {
                mainActivity.Member.get(selectedFamilyMemberIndex).BankAccountNumber = strDocNo;
        } else if(DocID == mainActivity.DocumentIDs.MEMBER_DOC_ID_RATIONCARD) {
                mainActivity.MemberFamilyDetails.RationCardNo = strDocNo;
        }
    }

    public long AddDocumentPhotos(long DocRcvdID, long groupID) {
        clsDocumentPhoto newDocPhoto = new clsDocumentPhoto();
        newDocPhoto.Init_DocumentPhoto();
        newDocPhoto.MemberDocuemntsRcvdID = DocRcvdID;
        newDocPhoto.GroupID = groupID;

        mainActivity.DocumentPhotos.add(newDocPhoto);
        int newIndex = mainActivity.DocumentPhotos.size() - 1;

        Calendar cal = Calendar.getInstance();
        while(mainActivity.LastTimeInMillis > cal.getTimeInMillis()) {
            cal = Calendar.getInstance();
        }
        mainActivity.LastTimeInMillis = cal.getTimeInMillis();

        mainActivity.LastTimeInMillis += 1000;
        mainActivity.SQLite_saveDocumentPhoto(newIndex, mainActivity.LastTimeInMillis, false);
        newDocPhoto.DocumentPhotoID = mainActivity.DocumentPhotos.get(newIndex).DocumentPhotoID;

        clsDocumentPhoto tnewDocPhoto = new clsDocumentPhoto();
        newDocPhoto.Copy_DocumentPhoto(tnewDocPhoto);
        mainActivity.tDocumentPhotos.add(tnewDocPhoto);

        RefreshScreen();

        return newDocPhoto.DocumentPhotoID;
    }

    public long AddMemberDocRcvd(int DocID, String strDocNo, int iAgeProofDoc) {
        clsMemberDocRcvd newDocRcvd = new clsMemberDocRcvd();
        newDocRcvd.Init_MemberDocRcvd();
        newDocRcvd.MemberDocumentsID = DocID;
        newDocRcvd.MemberID = mainActivity.Member.get(selectedFamilyMemberIndex).MemberID;
        newDocRcvd.DocumentNo = strDocNo;
        newDocRcvd.IsActive = 1;
        newDocRcvd.IsAgeProofDoc = iAgeProofDoc;

        mainActivity.MemberDocRcvd.add(newDocRcvd);
        int newIndex = mainActivity.MemberDocRcvd.size() - 1;

        Calendar cal = Calendar.getInstance();
        while(mainActivity.LastTimeInMillis > cal.getTimeInMillis()) {
            cal = Calendar.getInstance();
        }
        mainActivity.LastTimeInMillis = cal.getTimeInMillis();

        mainActivity.LastTimeInMillis += 1000;
        mainActivity.SQLite_saveMemberDocRcvd(newIndex, mainActivity.LastTimeInMillis, false);
        newDocRcvd.MemberDocuemntsRcvdID = mainActivity.MemberDocRcvd.get(newIndex).MemberDocuemntsRcvdID;

        clsMemberDocRcvd tnewDocRcvd = new clsMemberDocRcvd();
        newDocRcvd.Copy_MemberDocRcvd(tnewDocRcvd);
        mainActivity.tMemberDocRcvd.add(tnewDocRcvd);

        RefreshScreen();

        return newDocRcvd.MemberDocuemntsRcvdID;
    }

    public long AddMemberDocRcvd(int DocID, String strDocNo) {
        clsMemberDocRcvd newDocRcvd = new clsMemberDocRcvd();
        newDocRcvd.Init_MemberDocRcvd();
        newDocRcvd.MemberDocumentsID = DocID;
        newDocRcvd.MemberID = mainActivity.Member.get(selectedFamilyMemberIndex).MemberID;
        newDocRcvd.DocumentNo = strDocNo;
        newDocRcvd.IsActive = 1;
        newDocRcvd.IsAgeProofDoc = 0;

        mainActivity.MemberDocRcvd.add(newDocRcvd);
        int newIndex = mainActivity.MemberDocRcvd.size() - 1;

        Calendar cal = Calendar.getInstance();
        while(mainActivity.LastTimeInMillis > cal.getTimeInMillis()) {
            cal = Calendar.getInstance();
        }
        mainActivity.LastTimeInMillis = cal.getTimeInMillis();

        mainActivity.LastTimeInMillis += 1000;
        mainActivity.SQLite_saveMemberDocRcvd(newIndex, mainActivity.LastTimeInMillis, false);
        newDocRcvd.MemberDocuemntsRcvdID = mainActivity.MemberDocRcvd.get(newIndex).MemberDocuemntsRcvdID;

        clsMemberDocRcvd tnewDocRcvd = new clsMemberDocRcvd();
        newDocRcvd.Copy_MemberDocRcvd(tnewDocRcvd);
        mainActivity.tMemberDocRcvd.add(tnewDocRcvd);

        RefreshScreen();

        return newDocRcvd.MemberDocuemntsRcvdID;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            if(mainActivity == null)
                return;

            mainActivity.currentMemberIndex = 0;
            mainActivity.FragmentVisibleToUser = mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS;

            selectedDocumentID = -1;
            CheckDuplicate = 0;
            selectedFamilyMemberIndex = -1;

            RefreshScreen();
        }
    }

    public void RefreshScreen() {
        CheckBox cb;
        TextView tv;
        ImageButton imgbt1;
        ImageButton imgbt2;
        ImageButton imgbt3;
        EditText et;

        if(selectedFamilyMemberIndex < 0) {
            Spinner sp1 = (Spinner) rootView.findViewById(R.id.spinnerFamilyMemberName);
            sp1.setSelection(0);
            Spinner sp2 = (Spinner) rootView.findViewById(R.id.spinnerDocumentType);
            sp2.setSelection(0);
        }

        try {
            StoredDocumentIDMaster.clear();
            StoredDocumentMarathiDescriptionMaster.clear();

            for(int j=0; j<DocumentIDMaster.size(); j++) {
                int iCount = 0;
                for(int i=0; i<mainActivity.MemberDocRcvd.size(); i++) {
                    if( (mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == DocumentIDMaster.get(j)) &&
                            (mainActivity.MemberDocRcvd.get(i).IsActive == 1) ) {
                        iCount++;
                    }
                }
                if(iCount > 0) {
                    StoredDocumentIDMaster.add(DocumentIDMaster.get(j));
                    String strTemp = DocumentMarathiDescriptionMaster.get(j) + ": " + String.valueOf(iCount);
                    StoredDocumentMarathiDescriptionMaster.add(strTemp);
                }
            }

            TableLayout tblDocsSaved = (TableLayout) rootView.findViewById(R.id.tblLayoutDocumentsSaved);
            tblDocsSaved.removeAllViews();

            TextView tvref = (TextView) rootView.findViewById(R.id.textViewSavedDocs);
            for(int i=0; i<StoredDocumentMarathiDescriptionMaster.size(); i++) {
                tv = new TextView(mainActivity.getBaseContext());
                tv.setTextSize(30);
                tv.setBackgroundColor(Color.WHITE);
                tv.setTextColor(tvref.getTextColors());
                tv.setTypeface(Typeface.DEFAULT_BOLD);
                tv.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1f));
                tv.setGravity(Gravity.CENTER_VERTICAL);

                tv.setText(StoredDocumentMarathiDescriptionMaster.get(i));

                TableRow tblRow = new TableRow(mainActivity.getBaseContext());
                tblRow.setPadding(5, 5, 5, 5);
                tblRow.addView(tv);
                tblRow.setId(i);

                tblDocsSaved.addView(tblRow, i);
            }

            et = (EditText) rootView.findViewById(R.id.editTextDocNo);
            et.setText("");
            if( (mainActivity.LSO_ID > 0) && (selectedFamilyMemberIndex >= 0) && (selectedDocumentID > 0) )
                et.setEnabled(true);
            else
                et.setEnabled(false);

            CheckBox cbAgeProof = (CheckBox) rootView.findViewById(R.id.checkBoxIsAgeProofDoc);
            cbAgeProof.setChecked(false);
            if( (mainActivity.LSO_ID > 0) && (selectedFamilyMemberIndex >= 0) && (selectedDocumentID > 0) )
                cbAgeProof.setEnabled(true);
            else
                cbAgeProof.setEnabled(false);

            if(     (selectedDocumentID == mainActivity.DocumentIDs.MEMBER_DOC_ID_HOUSE_AGREEMENT) ||
                    (selectedDocumentID == mainActivity.DocumentIDs.MEMBER_DOC_ID_MEMBER_PHOTO) ||
                    (selectedDocumentID == mainActivity.DocumentIDs.MEMBER_DOC_ID_FAMILY_PHOTO) ||
                    (selectedDocumentID == mainActivity.DocumentIDs.MEMBER_DOC_ID_OTHER_GUARANTOR_PHOTO) ||
//bmp 19-Sep-22
                    (selectedDocumentID == mainActivity.DocumentIDs.MEMBER_DOC_ID_ITER_NATEWAIK_CHITTHI) ||
//bmp 19-Sep-22
                    (selectedDocumentID == mainActivity.DocumentIDs.MEMBER_DOC_ID_JOINT_LOANEE_PHOTO) ) {
                    et.setText("0");
                    et.setEnabled(false);
            }

            imgbt1 = (ImageButton) rootView.findViewById(R.id.imageButtonPhotoDoc);
            imgbt1.setEnabled(false);
            imgbt2 = (ImageButton) rootView.findViewById(R.id.imageButtonPreviewDoc);
            imgbt2.setEnabled(false);
            imgbt3 = (ImageButton) rootView.findViewById(R.id.imageButtonDeleteDoc);
            imgbt3.setEnabled(false);
            cb = (CheckBox) rootView.findViewById(R.id.checkBoxPhotoDoc);
            cb.setChecked(false);
            tv = (TextView) rootView.findViewById(R.id.textViewQtyPhotoDoc);
            tv.setText("0/0");
            if(mainActivity.LSO_ID > 0)
                imgbt1.setEnabled(true);
            PhotoDoc.clear();
            if( (selectedFamilyMemberIndex >= 0) && (selectedDocumentID > 0) ) {
                File folder = new File(mainActivity.baseDir + "/data/");
                File[] imgfiles = folder.listFiles();
                for (int i = 0; i < mainActivity.MemberDocRcvd.size(); i++) {
                    if ((selectedDocumentID == mainActivity.MemberDocRcvd.get(i).MemberDocumentsID) &&
                            (mainActivity.Member.get(selectedFamilyMemberIndex).MemberID == mainActivity.MemberDocRcvd.get(i).MemberID)) {
                        for(int k=0; k<mainActivity.DocumentPhotos.size(); k++) {
                            if (mainActivity.MemberDocRcvd.get(i).MemberDocuemntsRcvdID ==
                                    mainActivity.DocumentPhotos.get(k).MemberDocuemntsRcvdID) {
                                for (int j = 0; j < imgfiles.length; j++) {
                                    if (imgfiles[j].getName().contains(String.valueOf(mainActivity.DocumentPhotos.get(k).DocumentPhotoID))) {
                                        PhotoDoc.add(String.valueOf(mainActivity.DocumentPhotos.get(k).DocumentPhotoID) + ".jpg");
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if( (selectedFamilyMemberIndex >= 0) && (selectedDocumentID > 0) ) {
                for(int i=0; i<mainActivity.MemberDocRcvd.size(); i++) {
                    if( (mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == selectedDocumentID) &&
                        (mainActivity.MemberDocRcvd.get(i).MemberID == mainActivity.Member.get(selectedFamilyMemberIndex).MemberID) ) {
                        et.setText(mainActivity.MemberDocRcvd.get(i).DocumentNo);
                        if(mainActivity.MemberDocRcvd.get(i).IsAgeProofDoc == 1)
                            cbAgeProof.setChecked(true);
                        else
                            cbAgeProof.setChecked(false);
                        if(mainActivity.LSO_ID > 0)
                            imgbt3.setEnabled(true);
                        if(PhotoDoc.size() > 0) {
                            cb.setChecked(true);
                            currentphotoDoc = 1;
                            tv.setText("1/" + String.valueOf(PhotoDoc.size()));
                            imgbt2.setEnabled(true);
                        }
                    }
                }
                String strTemp = et.getText().toString();
                if( (strTemp == null) || strTemp.trim().isEmpty() ) {
                    if(selectedDocumentID == mainActivity.DocumentIDs.MEMBER_DOC_ID_BANK_PASSBOOK) {
                        String strDocumentNo = mainActivity.Member.get(selectedFamilyMemberIndex).BankAccountNumber;
                        if ((strDocumentNo != null) && (strDocumentNo.trim().length() > 0)) {
                            et.setText(strDocumentNo);
                            UpdateDocument(selectedDocumentID, strDocumentNo);
                        }
                    } else if(selectedDocumentID == mainActivity.DocumentIDs.MEMBER_DOC_ID_RATIONCARD) {
                        String strDocumentNo = mainActivity.MemberFamilyDetails.RationCardNo;
                        if ((strDocumentNo != null) && (strDocumentNo.trim().length() > 0)) {
                            et.setText(strDocumentNo);
                            UpdateDocument(selectedDocumentID, strDocumentNo);
                        }
                    }
                }
            }

            et = (EditText) rootView.findViewById(R.id.editTextRationcardFname);
            et.setText("");
            if( (mainActivity.LSO_ID > 0) && (selectedFamilyMemberIndex == 0) && (selectedDocumentID == mainActivity.DocumentIDs.MEMBER_DOC_ID_RATIONCARD) ) {
                et.setText(mainActivity.Member.get(0).RationCard_Fname);
                et.setEnabled(true);
            } else {
                et.setEnabled(false);
            }
            et = (EditText) rootView.findViewById(R.id.editTextRationcardMname);
            et.setText("");
            if( (mainActivity.LSO_ID > 0) && (selectedFamilyMemberIndex == 0) && (selectedDocumentID == mainActivity.DocumentIDs.MEMBER_DOC_ID_RATIONCARD) ) {
                et.setText(mainActivity.Member.get(0).RationCard_Mname);
                et.setEnabled(true);
            } else {
                et.setEnabled(false);
            }
            et = (EditText) rootView.findViewById(R.id.editTextRationcardLname);
            et.setText("");
            if( (mainActivity.LSO_ID > 0) && (selectedFamilyMemberIndex == 0) && (selectedDocumentID == mainActivity.DocumentIDs.MEMBER_DOC_ID_RATIONCARD) ) {
                et.setText(mainActivity.Member.get(0).RationCard_Lname);
                et.setEnabled(true);
            } else {
                et.setEnabled(false);
            }
            et = (EditText) rootView.findViewById(R.id.editTextRationcardAddress);
            et.setText("");
            if( (mainActivity.LSO_ID > 0) && (selectedFamilyMemberIndex == 0) && (selectedDocumentID == mainActivity.DocumentIDs.MEMBER_DOC_ID_RATIONCARD) ) {
                et.setText(mainActivity.MemberFamilyDetails.AddressOnRationcard);
                et.setEnabled(true);
            } else {
                et.setEnabled(false);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        String strFilepath = mainActivity.baseDir + "/data/";
        String strFilename = "unknown";
        switch(v.getId()) {
            case R.id.textViewQtyPhotoDoc:
                if(PhotoDoc.size() == 0)
                    currentphotoDoc = 0;
                else if(currentphotoDoc < PhotoDoc.size())
                    currentphotoDoc++;
                else
                    currentphotoDoc = 1;
                String strCount = String.valueOf(currentphotoDoc);
                strCount = strCount + "/" + String.valueOf(PhotoDoc.size());
                TextView tv = (TextView) rootView.findViewById(R.id.textViewQtyPhotoDoc);
                tv.setText(strCount);
                break;
            case R.id.imageButtonPhotoDoc:
                tv = (TextView) rootView.findViewById(R.id.editTextDocNo);
                if(tv.isEnabled()) {
                    String strDocumentNo = tv.getText().toString().trim();
                    if (strDocumentNo.isEmpty()) {
                        ShowdlgAlert("कागदपत्राचा क्रमांक भरणे बाकी.");
                    } else if (!ValidateDocNumber(strDocumentNo)) {
                        ShowdlgAlert("अवैध/चुकीचा कागदपत्र क्रमांक: " + strDocumentNo);
                    } else if ((selectedFamilyMemberIndex >= 0) && (selectedDocumentID > 0)) {
                        UpdateDocument(selectedDocumentID, strDocumentNo);
                        if (CheckDuplicateDocNumber(strDocumentNo)) {
//                            ShowdlgAlert("डुप्लिकेट कागदपत्र क्रमांक: " + strDocumentNo);
                        }

                        /*File outFile = new File(strFilepath + strFilename + ".jpg");
                        Uri outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", outFile);
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outuri);
//                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        startActivityIntent.launch(cameraIntent);*/

                        //RK
                        Intent cameraIntent = new Intent();

                        cameraIntent.setClassName(
                                "com.example.cameraapp",
                                "com.example.cameraapp.DocumentScannerActivity"
                        );

                        startActivityIntent.launch(cameraIntent);
                    }
                } else if ((selectedFamilyMemberIndex >= 0) && (selectedDocumentID > 0)) {
                    //RK
                    Intent cameraIntent = new Intent();

                    cameraIntent.setClassName(
                            "com.example.cameraapp",
                            "com.example.cameraapp.DocumentScannerActivity"
                    );

                    startActivityIntent.launch(cameraIntent);
                    /*File outFile = new File(strFilepath + strFilename + ".jpg");
                    Uri outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", outFile);
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outuri);
//                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    startActivityIntent.launch(cameraIntent);*/
                }
                break;
            case R.id.imageButtonPreviewDoc:
                if(PhotoDoc.size() > 0) {
                    strFilename = PhotoDoc.get(currentphotoDoc - 1);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    File outFile = new File(strFilepath + strFilename);
                    Uri outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", outFile);
                    intent.setDataAndType(outuri, "image/*");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                }
                break;
            case R.id.imageButtonDeleteDoc:
                if(PhotoDoc.size() == 0)
                    break;
                strFilename = PhotoDoc.get(currentphotoDoc - 1);
                File file = new File(strFilepath + strFilename);
                if(file.exists()) {
                    file.delete();
                    Uri outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", file);
                    getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, outuri));
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

                RefreshScreen();
                break;
            case R.id.imageButtonDocumentsPack:
                ShowdlgAlert2("Press OK to remove document entries without photo.");
                break;
        }
    }
    /*ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Add same code that you want to add in onActivityResult method
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        String strFilepath = mainActivity.baseDir + "/data/";
                        String strFilename = "unknown";
                        File outFile = new File(strFilepath + strFilename + ".jpg");
                        if(!outFile.exists())
                            return;
                        if(outFile.length() >= MAX_PHOTO_FILESIZE) {
//bmp 28-Apr-26
//                                ShowdlgAlertonActivityResult("Incorrect Image file size, Adjust Camera Setting and Try Again!");
//                                return;
                            String currentPhotoPath = strFilepath + strFilename + ".jpg";
                            mainActivity.AdjustImageFileSize(currentPhotoPath);
//bmp 28-Apr-26
                        }
                        strFilepath = mainActivity.baseDir + "/data/";
                        strFilename = "";

                        EditText et = (EditText) rootView.findViewById(R.id.editTextDocNo);
                        TextView tv = (TextView) rootView.findViewById(R.id.textViewQtyPhotoDoc);
                        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxPhotoDoc);
                        CheckBox cb2 = (CheckBox) rootView.findViewById(R.id.checkBoxIsAgeProofDoc);

                        String strDocPhotoID = "";
                        String strDocNo = "";
                        int iIsAgeProofDoc = 0;
                        int indexMemberDocRcvd = -1;
                        for(int i=0; i<mainActivity.MemberDocRcvd.size(); i++) {
                            if( (mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == selectedDocumentID) &&
                                    (mainActivity.MemberDocRcvd.get(i).IsActive == 1) &&
                                    (mainActivity.MemberDocRcvd.get(i).MemberID == mainActivity.Member.get(selectedFamilyMemberIndex).MemberID) ) {
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
                            if(strDocNo.isEmpty()) {
                                strDocNo = et.getText().toString();
                            }
                            long lTemp = 0;
                            if(indexMemberDocRcvd < 0) {
                                lTemp = AddMemberDocRcvd(selectedDocumentID, strDocNo, iIsAgeProofDoc);
                                lTemp = AddDocumentPhotos(lTemp, 0);
                            } else {
                                lTemp = AddDocumentPhotos(
                                        mainActivity.MemberDocRcvd.get(indexMemberDocRcvd).MemberDocuemntsRcvdID,
                                        0);
                            }
                            strDocPhotoID = String.valueOf(lTemp);
                            strFilename = strDocPhotoID + ".jpg";
                        }
                        PhotoDoc.add(strFilename);
                        currentphotoDoc = PhotoDoc.size();
                        String strCount = String.valueOf(currentphotoDoc);
                        strCount = strCount + "/" + String.valueOf(PhotoDoc.size());
                        tv.setText(strCount);
                        cb.setChecked(true);
                        if(iIsAgeProofDoc == 1)
                            cb2.setChecked(true);
                        else
                            cb2.setChecked(false);
                        ImageButton imgbt2 = (ImageButton) rootView.findViewById(R.id.imageButtonPreviewDoc);
                        imgbt2.setEnabled(true);

                        //Copy image file to 'upload' folder only on 'PIF Submit'
                        File imageFile = new File(strFilepath + strFilename);
                        outFile.renameTo(imageFile);
                    } else if(result.getResultCode() == RESULT_CANCELED){
                        //handle cancel
                        if (mainActivity.iCurrentScreen == mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS) {
                            ShowdlgAlertonActivityResult("CAMERA REQUEST CANCELED!");
                        }
                    }
                }
            });*/

    ActivityResultLauncher<Intent> startActivityIntent =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                //  RECEIVE IMAGE FROM CAMERA APP
                                Intent data = result.getData();
                                if (data == null || data.getData() == null) {
                                    ShowdlgAlertonActivityResult("No image received from Camera App!");
                                    return;
                                }

                                Uri imageUri = data.getData();
                                // NEW CODE - COPY IMAGE TO FINANCE APP
                                String strFilepath = mainActivity.baseDir + "/data/";
                                String strFilename = "unknown";
                                File directory = new File(strFilepath);
                                if (!directory.exists()) {
                                    directory.mkdirs();
                                }
                                File outFile = new File(strFilepath + strFilename + ".jpg");
                                try {
                                    InputStream inputStream =
                                            requireContext()
                                                    .getContentResolver()
                                                    .openInputStream(imageUri);
                                    if (inputStream == null) {
                                        ShowdlgAlertonActivityResult("Unable to read image!");
                                        return;
                                    }

                                    FileOutputStream outputStream = new FileOutputStream(outFile);
                                    byte[] buffer = new byte[8192];
                                    int length;
                                    while ((length = inputStream.read(buffer)) != -1) {
                                        outputStream.write(buffer, 0, length);
                                    }
                                    outputStream.close();
                                    inputStream.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ShowdlgAlertonActivityResult("Unable to save captured image!");
                                    return;
                                }
                                // YOUR EXISTING CODE STARTS HERE
                                if (!outFile.exists())
                                    return;

                                if (outFile.length() >= MAX_PHOTO_FILESIZE) {
                                    String currentPhotoPath = strFilepath + strFilename + ".jpg";
                                    mainActivity.AdjustImageFileSize(currentPhotoPath);
                                }
                                strFilepath = mainActivity.baseDir + "/data/";
                                strFilename = "";

                                EditText et = (EditText) rootView.findViewById(R.id.editTextDocNo);
                                TextView tv = (TextView) rootView.findViewById(R.id.textViewQtyPhotoDoc);
                                CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxPhotoDoc);
                                CheckBox cb2 = (CheckBox) rootView.findViewById(R.id.checkBoxIsAgeProofDoc);

                                String strDocPhotoID = "";
                                String strDocNo = "";
                                int iIsAgeProofDoc = 0;
                                int indexMemberDocRcvd = -1;
                                for (int i = 0; i < mainActivity.MemberDocRcvd.size(); i++) {
                                    if (    (mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == selectedDocumentID) &&
                                            (mainActivity.MemberDocRcvd.get(i).IsActive == 1) &&
                                            (mainActivity.MemberDocRcvd.get(i).MemberID == mainActivity.Member.get(selectedFamilyMemberIndex).MemberID)) {
                                        strDocNo = mainActivity.MemberDocRcvd.get(i).DocumentNo;
                                        iIsAgeProofDoc = mainActivity.MemberDocRcvd.get(i).IsAgeProofDoc;
                                        indexMemberDocRcvd = i;
                                        for (int k = 0; k < mainActivity.DocumentPhotos.size(); k++) {
                                            if (mainActivity.MemberDocRcvd.get(i).MemberDocuemntsRcvdID ==
                                                    mainActivity.DocumentPhotos.get(k).MemberDocuemntsRcvdID) {
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

                                if (strDocPhotoID.length() > 0) {
                                    strFilename = strDocPhotoID + ".jpg";
                                } else {
                                    if (strDocNo.isEmpty()) {
                                        strDocNo = et.getText().toString();
                                    }
                                    long lTemp = 0;
                                    if (indexMemberDocRcvd < 0) {
                                        lTemp = AddMemberDocRcvd(selectedDocumentID, strDocNo, iIsAgeProofDoc);
                                        lTemp = AddDocumentPhotos(lTemp, 0);
                                    } else {
                                        lTemp = AddDocumentPhotos(mainActivity.MemberDocRcvd.get(indexMemberDocRcvd).MemberDocuemntsRcvdID, 0);
                                    }
                                    strDocPhotoID = String.valueOf(lTemp);
                                    strFilename = strDocPhotoID + ".jpg";
                                }

                                PhotoDoc.add(strFilename);
                                currentphotoDoc = PhotoDoc.size();
                                String strCount = String.valueOf(currentphotoDoc);
                                strCount = strCount + "/" + String.valueOf(PhotoDoc.size());
                                tv.setText(strCount);
                                cb.setChecked(true);
                                if (iIsAgeProofDoc == 1)
                                    cb2.setChecked(true);
                                else
                                    cb2.setChecked(false);

                                ImageButton imgbt2 = (ImageButton) rootView.findViewById(R.id.imageButtonPreviewDoc);
                                imgbt2.setEnabled(true);

                                // MOVE unknown.jpg TO ACTUAL PHOTO ID
                                File imageFile = new File(strFilepath + strFilename);
                                outFile.renameTo(imageFile);
                            } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                                if (mainActivity.iCurrentScreen == mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS) {
                                    ShowdlgAlertonActivityResult("CAMERA REQUEST CANCELED!");
                                }
                            }
                        }
                    }
            );

    void DeactivateDocumentsWithoutPhoto() {
        for(int i=0; i<mainActivity.MemberDocRcvd.size(); i++) {
            if(mainActivity.MemberDocRcvd.get(i).IsActive == 1) {
                int iDocumentPhotoAvailable = 0;
                for (int k = 0; k < mainActivity.DocumentPhotos.size(); k++) {
                    if ((mainActivity.MemberDocRcvd.get(i).MemberDocuemntsRcvdID ==
                            mainActivity.DocumentPhotos.get(k).MemberDocuemntsRcvdID) &&
                            (mainActivity.DocumentPhotos.get(k).IsActive == 1)) {
                        iDocumentPhotoAvailable = 1;
                        break;
                    }
                }
                mainActivity.MemberDocRcvd.get(i).IsActive = iDocumentPhotoAvailable;
            }
            if(     (mainActivity.MemberDocRcvd.get(i).IsActive == 0) &&
                    (mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == mainActivity.DocumentIDs.MEMBER_DOC_ID_RATIONCARD) ) {
                mainActivity.Member.get(0).RationCard_Fname = "NA";
                mainActivity.Member.get(0).RationCard_Mname = "NA";
                mainActivity.Member.get(0).RationCard_Lname = "NA";
                mainActivity.MemberFamilyDetails.AddressOnRationcard = "NA";
            }
        }
    }
    boolean ValidateDocNumber(String strDocNo) {
        boolean valid = true;
        if(     (selectedDocumentID == mainActivity.DocumentIDs.MEMBER_DOC_ID_AADHAARCARD) ||
                (selectedDocumentID == mainActivity.DocumentIDs.MEMBER_DOC_ID_JOINT_LOANEE_AADHAARCARD) ||
                (selectedDocumentID == mainActivity.DocumentIDs.MEMBER_DOC_ID_OTHER_GUARANTOR_AADHAARCARD) ) {
            // [0-9]{12} - match 12 numbers 0 to 9
            Pattern pattern = Pattern.compile("[0-9]{12}");
            Matcher matcher = pattern.matcher(strDocNo);
            // Check if pattern matches
            valid = matcher.matches();
        } else if(      (selectedDocumentID == mainActivity.DocumentIDs.MEMBER_DOC_ID_PANCARD) ||
                        (selectedDocumentID == mainActivity.DocumentIDs.MEMBER_DOC_ID_JOINT_LOANEE_PANCARD) ||
                        (selectedDocumentID == mainActivity.DocumentIDs.MEMBER_DOC_ID_OTHER_GUARANTOR_PANCARD) ) {
            // [A-Z]{5} - match five literals which can be A to Z
            // [0-9]{4} - followed by 4 numbers 0 to 9
            // [A-Z]{1} - followed by one literal which can A to Z
            Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
            Matcher matcher = pattern.matcher(strDocNo);
            // Check if pattern matches
            valid = matcher.matches();
        } else if(selectedDocumentID == mainActivity.DocumentIDs.MEMBER_DOC_ID_ELECTIONCARD) {
//            Pattern pattern = Pattern.compile("/^([a-zA-Z]){3}([0-9]){7}?$/g");
            Pattern pattern1 = Pattern.compile("[A-Z]{3}[0-9]{7}");
            Matcher matcher1 = pattern1.matcher(strDocNo);
            // Check if pattern matches
            valid = matcher1.matches();
            if(!valid) {
                Pattern pattern2 = Pattern.compile("[A-Z]{2}/[0-9]{4}/[0-9]{4}/[0-9]{6}");
                Matcher matcher2 = pattern2.matcher(strDocNo);
                // Check if pattern matches
                valid = matcher2.matches();
            }
        } else if(selectedDocumentID == mainActivity.DocumentIDs.MEMBER_DOC_ID_BANK_PASSBOOK) {
            for(int i=0; i<strDocNo.length(); i++) {
                try {
                    Integer.parseInt(String.valueOf(strDocNo.charAt(i)));
                } catch (NumberFormatException e) {
                    valid = false;
                    break;
                }
            }
        }

        return valid;
    }
    boolean CheckDuplicateDocNumber(String strDocNo) {
        if(CheckDuplicate == 0)
            return false;

        boolean duplicate = false;

        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
//                ufinDbContract.MemberDocRcvd.COLUMN_NAME_MemberDocuemntsRcvdID,
                ufinDbContract.MemberDocRcvd.COLUMN_NAME_MemberID,
        };
        String selection1 = ufinDbContract.MemberDocRcvd.COLUMN_NAME_IsActive + "=" + "'" + String.valueOf(1) + "'"
                + " and " + ufinDbContract.MemberDocRcvd.COLUMN_NAME_MemberDocumentsID + "=" + "'" + String.valueOf(selectedDocumentID) + "'"
                + " and " + ufinDbContract.MemberDocRcvd.COLUMN_NAME_DocumentNo + "=" + "'" + String.valueOf(strDocNo) + "'";

        Cursor c1 = db.query(
                ufinDbContract.MemberDocRcvd.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            Long lTemp = Long.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.MemberDocRcvd.COLUMN_NAME_MemberID)));
                            if(lTemp != mainActivity.Member.get(selectedFamilyMemberIndex).MemberID) {
                                duplicate = true;
                                break;
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

        return duplicate;
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

    void ShowdlgAlert(String strMessage) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getContext());
        TextView tvTitle = new TextView(getContext());
        tvTitle.setText(getString(R.string.app_name));
        tvTitle.setTextSize(30);
        tvTitle.setTextColor(getResources().getColor(R.color.colorAccent));
        dlgAlert.setCustomTitle(tvTitle);
        dlgAlert.setIcon(R.drawable.ic_launcher_foreground);
        dlgAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Cancel button
                dialog.dismiss();
                RefreshScreen();
                mainActivity.FragmentVisibleToUser = mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS;
            }
        });
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                dialog.dismiss();
                RefreshScreen();
                mainActivity.FragmentVisibleToUser = mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS;
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
                mainActivity.FragmentVisibleToUser = mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS;
            }
        });
        dlgAlert2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                dialog.dismiss();
                mainActivity.FragmentVisibleToUser = mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS;

                DeactivateDocumentsWithoutPhoto();
                RefreshScreen();
            }
        });
        dlgAlert2.setCancelable(true);

        dlgAlert2.setMessage(strMessage);
        AlertDialog dialog2 = dlgAlert2.create();
        dialog2.show();
        TextView textView = (TextView) dialog2.findViewById(android.R.id.message);
        textView.setTextSize(30);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch(requestCode) {
//            case CAMERA_REQUEST:
//                if(resultCode == Activity.RESULT_OK) {
//                    String strFilepath = mainActivity.baseDir + "/data/";
//                    String strFilename = "unknown";
//                    File outFile = new File(strFilepath + strFilename + ".jpg");
//                    if(!outFile.exists())
//                        break;
//                    if(outFile.length() >= MAX_PHOTO_FILESIZE) {
//                        ShowdlgAlertonActivityResult("Incorrect Image file size, Adjust Camera Setting and Try Again!");
//                        break;
//                    }
//                    strFilepath = mainActivity.baseDir + "/data/";
//                    strFilename = "";
//
//                    EditText et = (EditText) rootView.findViewById(R.id.editTextDocNo);
//                    TextView tv = (TextView) rootView.findViewById(R.id.textViewQtyPhotoDoc);
//                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxPhotoDoc);
//                    CheckBox cb2 = (CheckBox) rootView.findViewById(R.id.checkBoxIsAgeProofDoc);
//
//                    String strDocPhotoID = "";
//                    String strDocNo = "";
//                    int iIsAgeProofDoc = 0;
//                    int indexMemberDocRcvd = -1;
//                    for(int i=0; i<mainActivity.MemberDocRcvd.size(); i++) {
//                        if( (mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == selectedDocumentID) &&
//                                (mainActivity.MemberDocRcvd.get(i).IsActive == 1) &&
//                                (mainActivity.MemberDocRcvd.get(i).MemberID == mainActivity.Member.get(selectedFamilyMemberIndex).MemberID) ) {
//                            strDocNo = mainActivity.MemberDocRcvd.get(i).DocumentNo;
//                            iIsAgeProofDoc = mainActivity.MemberDocRcvd.get(i).IsAgeProofDoc;
//                            indexMemberDocRcvd = i;
//                            for(int k=0; k<mainActivity.DocumentPhotos.size(); k++) {
//                                if(mainActivity.MemberDocRcvd.get(i).MemberDocuemntsRcvdID ==
//                                        mainActivity.DocumentPhotos.get(k).MemberDocuemntsRcvdID) {
//                                    String strTemp = String.valueOf(mainActivity.DocumentPhotos.get(k).DocumentPhotoID);
//                                    File file = new File(strFilepath + strTemp + ".jpg");
//                                    if(!file.exists()) {
//                                        strDocPhotoID = strTemp;
//                                        mainActivity.DocumentPhotos.get(k).IsActive = 1;
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    if(strDocPhotoID.length() > 0) {
//                        strFilename = strDocPhotoID + ".jpg";
//                    } else {
//                        if(strDocNo.isEmpty()) {
//                            strDocNo = et.getText().toString();
//                        }
//                        long lTemp = 0;
//                        if(indexMemberDocRcvd < 0) {
//                            lTemp = AddMemberDocRcvd(selectedDocumentID, strDocNo, iIsAgeProofDoc);
//                            lTemp = AddDocumentPhotos(lTemp, 0);
//                        } else {
//                            lTemp = AddDocumentPhotos(
//                                    mainActivity.MemberDocRcvd.get(indexMemberDocRcvd).MemberDocuemntsRcvdID,
//                                    0);
//                        }
//                        strDocPhotoID = String.valueOf(lTemp);
//                        strFilename = strDocPhotoID + ".jpg";
//                    }
//                    PhotoDoc.add(strFilename);
//                    currentphotoDoc = PhotoDoc.size();
//                    String strCount = String.valueOf(currentphotoDoc);
//                    strCount = strCount + "/" + String.valueOf(PhotoDoc.size());
//                    tv.setText(strCount);
//                    cb.setChecked(true);
//                    if(iIsAgeProofDoc == 1)
//                        cb2.setChecked(true);
//                    else
//                        cb2.setChecked(false);
//                    ImageButton imgbt2 = (ImageButton) rootView.findViewById(R.id.imageButtonPreviewDoc);
//                    imgbt2.setEnabled(true);
//
//                    //Copy image file to 'upload' folder only on 'PIF Submit'
//                    File imageFile = new File(strFilepath + strFilename);
//                    outFile.renameTo(imageFile);
//                }
//                break;
//        }
//        if(resultCode == RESULT_CANCELED){
//            //handle cancel
//            if (mainActivity.iCurrentScreen == mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS) {
//                ShowdlgAlertonActivityResult("CAMERA REQUEST CANCELED!");
//            }
//        }
//    }

    public void SQLite_getDocumentMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
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
}
