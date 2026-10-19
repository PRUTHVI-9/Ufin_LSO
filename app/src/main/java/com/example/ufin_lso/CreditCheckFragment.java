package com.example.ufin_lso;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static android.app.Activity.RESULT_CANCELED;

public class CreditCheckFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View rootView;
    MainActivity mainActivity;

    public static final int MAX_PHOTO_FILESIZE                  = (4 * 1024 * 1024);
//    public static final int MAX_PHOTO_FILESIZE                  = (8 * 1024 * 1024);
    private static final int ID_REQUEST_PHOTO_DocMainBorrower   = 0;
    private static final int ID_REQUEST_PHOTO_DocCoBorrower     = 1;
    private static final int ID_REQUEST_PHOTO_DocGuarantor      = 2;
    int IDRequest = -1;
    String strFilepath = "";
    String strFilename = "unknown";

    public static final int ufin_CREDITCHECK_DIALOG_STATE_NONE           = 0;
    public static final int ufin_CREDITCHECK_DIALOG_STATE_VALIDATE        = 1;
    public static final int ufin_CREDITCHECK_DIALOG_STATE_CHREMARKS       = 2;
    public static final int ufin_CREDITCHECK_DIALOG_STATE_CBREMARKS       = 3;
    public static final int ufin_CREDITCHECK_DIALOG_STATE_CBRECHECK       = 4;
    public static final int ufin_CREDITCHECK_DIALOG_STATE_CREDITCHECK_OK  = 5;

    public AlertDialog.Builder dlgAlert;
    public static int dlgState = ufin_CREDITCHECK_DIALOG_STATE_NONE;

    private boolean dbMaster = false;
    private ArrayList<Integer> RelationIDMaster = new ArrayList<Integer>();
    private ArrayList<String> RelationMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Integer> BusinessIDMaster = new ArrayList<Integer>();
    private ArrayList<String> BusinessMarathiDescriptionMaster = new ArrayList<String>();
    private int RelationIDMainMember;
    private int RelationIndexMainMember;
    private long RelationIDSpouse = 0;
    private ArrayList<Integer> MaritalStatusIDMaster = new ArrayList<Integer>();
    private ArrayList<String> MaritalStatusMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<String> StateCodeMaster = new ArrayList<String>();
    private ArrayList<String> StateDescriptionMaster = new ArrayList<String>();

    clsCreditCheckInfo CreditCheckInfoMainBorrower; // = new clsCreditCheckInfo();
    clsCreditCheckInfo CreditCheckInfoCoBorrower;   // = new clsCreditCheckInfo();
    clsCreditCheckInfo CreditCheckInfoGuarantor;    // = new clsCreditCheckInfo();
    ArrayList<clsCreditCheckInfo> CreditCheckInfoList = new ArrayList<clsCreditCheckInfo>();
    private ArrayList<String> CreditCheckList = new ArrayList<String>();
    ArrayList<String> CreditCheckInfoProblems = new ArrayList<String>();
    ArrayList<Long> alRevisitClusterID = new ArrayList<>();
    ArrayList<String> alRevisitCluster = new ArrayList<>();
    ArrayList<Long> alRevisitCommunityID = new ArrayList<>();
    ArrayList<String> alRevisitCommunity = new ArrayList<>();

    DatePickerDialog dlgDatePicker = null;
    final Calendar myCalendar = Calendar.getInstance();
    int selectedType;
    EditText etBirthdateMainBorrower;
    EditText etBirthdateCoBorrower;
    EditText etBirthdateGuarantor;
    boolean CoBorrowerSelected;

    public CreditCheckFragment() {
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
    public static CreditCheckFragment newInstance(String param1, String param2) {
        CreditCheckFragment fragment = new CreditCheckFragment();
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
//        return inflater.inflate(R.layout.fragment_creditcheck, container, false);
        rootView = inflater.inflate(R.layout.fragment_creditcheck, container, false);
        mainActivity = (MainActivity) getActivity();
        strFilepath = mainActivity.baseDir + "/data/";

        if(!dbMaster) {
            SQLite_getRelationMaster();
            SQLite_getBusinessMaster();
            SQLite_getMaritalStatusMaster();
            SQLite_getStateMaster();
            dbMaster = true;
        }
//        ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarCreditCheck);
//        if (pbSync != null) {
//            pbSync.setIndeterminate(true);
//            pbSync.setVisibility(View.VISIBLE);
//        }

        if(CreditCheckInfoMainBorrower == null) {
            CreditCheckInfoMainBorrower = new clsCreditCheckInfo();
            CreditCheckInfoMainBorrower.InitCreditCheckInfo();
        }
        if(CreditCheckInfoCoBorrower == null) {
            CreditCheckInfoCoBorrower = new clsCreditCheckInfo();
            CreditCheckInfoCoBorrower.InitCreditCheckInfo();
        }
        if(CreditCheckInfoGuarantor == null) {
            CreditCheckInfoGuarantor = new clsCreditCheckInfo();
            CreditCheckInfoGuarantor.InitCreditCheckInfo();
        }
        Button bt = (Button) rootView.findViewById(R.id.buttonCloseCreditCheck);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonClearCreditCheck);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonCreditCheck);
        bt.setOnClickListener(this);
//        bt = (Button) rootView.findViewById(R.id.buttonCreditReview);
//        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonCreditCheckOK);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonScanMainBorrower);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonScanCoBorrower);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonScanGuarantor);
        bt.setOnClickListener(this);
        ImageButton imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonPhotoDocMainBorrower);
        imgbt.setOnClickListener(this);
        imgbt.setEnabled(false);
        imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonPhotoDocCoBorrower);
        imgbt.setOnClickListener(this);
        imgbt.setEnabled(false);
        imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonPhotoDocGuarantor);
        imgbt.setOnClickListener(this);
        imgbt.setEnabled(false);
        bt = (Button) rootView.findViewById(R.id.buttonCBReCheckMainBorrower);
        bt.setOnClickListener(this);
        bt.setEnabled(false);
        bt = (Button) rootView.findViewById(R.id.buttonCBReCheckCoBorrower);
        bt.setOnClickListener(this);
        bt.setEnabled(false);
        bt = (Button) rootView.findViewById(R.id.buttonCBReCheckGuarantor);
        bt.setOnClickListener(this);
        bt.setEnabled(false);
        bt = (Button) rootView.findViewById(R.id.buttonCHMainBorrower);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonCBMainBorrower);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonCHCoBorrower);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonCBCoBorrower);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonCHGuarantor);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonCBGuarantor);
        bt.setOnClickListener(this);

//        bt = (Button) rootView.findViewById(R.id.buttonCreditReview);
//        bt.setEnabled(false);
        bt = (Button) rootView.findViewById(R.id.buttonCreditCheckOK);
        bt.setEnabled(false);

        ArrayAdapter<String> adapterStateMainBorrower = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, StateDescriptionMaster);
        adapterStateMainBorrower.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerStateMainBorrower);
        sp.setAdapter(adapterStateMainBorrower);
        ArrayAdapter<String> adapterStateCoBorrower = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, StateDescriptionMaster);
        adapterStateCoBorrower.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerStateCoBorrower);
        sp.setAdapter(adapterStateCoBorrower);
        ArrayAdapter<String> adapterStateGuarantor = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, StateDescriptionMaster);
        adapterStateGuarantor.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerStateGuarantor);
        sp.setAdapter(adapterStateGuarantor);

        String[] otherID = getResources().getStringArray(R.array.KYC_DOC2_array);
        ArrayAdapter<String> adapterOtherIDMainBorrower = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, otherID);
        adapterOtherIDMainBorrower.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDMainBorrower);
        sp.setAdapter(adapterOtherIDMainBorrower);
        ArrayAdapter<String> adapterOtherIDCoBorrower = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, otherID);
        adapterOtherIDCoBorrower.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDCoBorrower);
        sp.setAdapter(adapterOtherIDCoBorrower);
        ArrayAdapter<String> adapterOtherIDGuarantor = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, otherID);
        adapterOtherIDGuarantor.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDGuarantor);
        sp.setAdapter(adapterOtherIDGuarantor);

        SQLite_getCreditCheckList();
        ArrayAdapter<String> adapterMainBorrower = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, CreditCheckList);
        adapterMainBorrower.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerMainBorrower);
        sp.setAdapter(adapterMainBorrower);
        sp.setSelection(0);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    CreditCheckInfoMainBorrower = new clsCreditCheckInfo();
                    CreditCheckInfoMainBorrower.InitCreditCheckInfo();
                    CreditCheckInfoCoBorrower = new clsCreditCheckInfo();
                    CreditCheckInfoCoBorrower.InitCreditCheckInfo();
                    CreditCheckInfoGuarantor = new clsCreditCheckInfo();
                    CreditCheckInfoGuarantor.InitCreditCheckInfo();
//                    Button bt = (Button) rootView.findViewById(R.id.buttonCreditReview);
//                    bt.setEnabled(false);
                    Button bt = (Button) rootView.findViewById(R.id.buttonCreditCheckOK);
                    bt.setEnabled(false);
                    ClearCreditCheckInfo();
                    RefreshCreditCheckInfo();
                    return;
                }
                String strTemp = CreditCheckList.get(position);
                String[] strSplit = strTemp.split(":");
                String strAdhaarNo = strSplit[0];
                CreditCheckInfoMainBorrower = new clsCreditCheckInfo();
                CreditCheckInfoMainBorrower.InitCreditCheckInfo();
                CreditCheckInfoCoBorrower = new clsCreditCheckInfo();
                CreditCheckInfoCoBorrower.InitCreditCheckInfo();
                CreditCheckInfoGuarantor = new clsCreditCheckInfo();
                CreditCheckInfoGuarantor.InitCreditCheckInfo();
//                Button bt = (Button) rootView.findViewById(R.id.buttonCreditReview);
//                bt.setEnabled(false);
                Button bt = (Button) rootView.findViewById(R.id.buttonCreditCheckOK);
                bt.setEnabled(false);
                CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxCoBorrower);
                cb.setChecked(false);
                CoBorrowerSelected = false;
                for(int i=0; i<CreditCheckInfoList.size(); i++) {
                    if(strAdhaarNo.equalsIgnoreCase(CreditCheckInfoList.get(i).MainBorrowerAdhaarNo)) {
                        if(CreditCheckInfoList.get(i).Role.equalsIgnoreCase("M")) {
                            CreditCheckInfoMainBorrower = CreditCheckInfoList.get(i);
                        } else if(CreditCheckInfoList.get(i).Role.equalsIgnoreCase("C")) {
                            CreditCheckInfoCoBorrower = CreditCheckInfoList.get(i);
                            cb = (CheckBox) rootView.findViewById(R.id.checkBoxCoBorrower);
                            cb.setChecked(true);
                            CoBorrowerSelected = true;
                        } else if(CreditCheckInfoList.get(i).Role.equalsIgnoreCase("G")) {
                            CreditCheckInfoGuarantor = CreditCheckInfoList.get(i);
                        }
                    }
                }
                RefreshCreditCheckInfo();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);

        ArrayAdapter<String> adapterMaritalStatus = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, MaritalStatusMarathiDescriptionMaster);
        adapterMaritalStatus.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerMaritalStatus);
        sp.setAdapter(adapterMaritalStatus);
        sp.setSelection(0);
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);

        ArrayAdapter<String> adapterBusiness = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, BusinessMarathiDescriptionMaster);
        adapterBusiness.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationMainBorrower);
        sp.setAdapter(adapterBusiness);
        sp.setSelection(0);
//        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0)
//                    return;
//                CreditCheckInfoMainBorrower.ProfessionID = BusinessIDMaster.get(position);
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);

        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationCoBorrower);
        sp.setAdapter(adapterBusiness);
        sp.setSelection(0);
//        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0)
//                    return;
//                CreditCheckInfoCoBorrower.ProfessionID = BusinessIDMaster.get(position);
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);

        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationGuarantor);
        sp.setAdapter(adapterBusiness);
        sp.setSelection(0);
//        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0)
//                    return;
//                CreditCheckInfoGuarantor.ProfessionID = BusinessIDMaster.get(position);
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);

        ArrayAdapter<String> adapterRelation = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, RelationMarathiDescriptionMaster);
        adapterRelation.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
//        sp = (Spinner) rootView.findViewById(R.id.spinnerRelationMainBorrower);
//        sp.setAdapter(adapterRelation);
//        sp.setSelection(0);
//        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0)
//                    return;
//                CreditCheckInfoMainBorrower.MainBorrowerRelationID = RelationIDMaster.get(position);
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//        sp.setFocusable(true);
//        sp.setFocusableInTouchMode(true);
//
        sp = (Spinner) rootView.findViewById(R.id.spinnerRelationCoBorrower);
        sp.setAdapter(adapterRelation);
        sp.setSelection(0);
//        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0)
//                    return;
//                CreditCheckInfoCoBorrower.MainBorrowerRelationID = RelationIDMaster.get(position);
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);

        sp = (Spinner) rootView.findViewById(R.id.spinnerRelationGuarantor);
        sp.setAdapter(adapterRelation);
        sp.setSelection(0);
//        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0)
//                    return;
//                CreditCheckInfoGuarantor.MainBorrowerRelationID = RelationIDMaster.get(position);
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
        sp.setFocusable(true);
        sp.setFocusableInTouchMode(true);

        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxFamilyMemberCoBorrower);
        cb.setChecked(false);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxFamilyMemberGuarantor);
        cb.setChecked(false);

//        final ArrayList<Long> alRevisitClusterID = new ArrayList<>();
        alRevisitClusterID.clear();
        alRevisitClusterID.add(0L);
//        final ArrayList<String> alRevisitCluster = new ArrayList<>();
        alRevisitCluster.clear();
        alRevisitCluster.add("");
        for(int i=0; i<mainActivity.RevisitMemberList.size(); i++) {
            long lTemp = mainActivity.RevisitMemberList.get(i).ClusterID;
            boolean newFlag = true;
            for(int j=0; j<alRevisitClusterID.size(); j++) {
                if(lTemp == alRevisitClusterID.get(j)) {
                    newFlag = false;
                    break;
                }
            }
            if(newFlag) {
                alRevisitClusterID.add(lTemp);
                alRevisitCluster.add(mainActivity.RevisitMemberList.get(i).Cluster);
            }
        }
        ArrayAdapter<String> adapterVisitDetailsCluster = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, alRevisitCluster);
        adapterVisitDetailsCluster.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner spSelectCluster = (Spinner) rootView.findViewById(R.id.spinnerCluster);
        spSelectCluster.setAdapter(adapterVisitDetailsCluster);
        spSelectCluster.setSelection(0);
        spSelectCluster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                    return;
                if(CreditCheckInfoMainBorrower == null) {
                    return;
                }
                CreditCheckInfoMainBorrower.ClusterID = alRevisitClusterID.get(position);
                CreditCheckInfoMainBorrower.Cluster = alRevisitCluster.get(position);
                //Reselect Community again if previously selected
//                CreditCheckInfoMainBorrower.CommunityID = 0;
//                CreditCheckInfoMainBorrower.Community = "";
                //Show only Communities within selected Cluster
//                final ArrayList<Long> alRevisitCommunityID = new ArrayList<>();
                alRevisitCommunityID.clear();
                alRevisitCommunityID.add(0L);
//                final ArrayList<String> alRevisitCommunity = new ArrayList<>();
                alRevisitCommunity.clear();
                alRevisitCommunity.add("");
                for(int i=0; i<mainActivity.RevisitMemberList.size(); i++) {
                    long lTemp = mainActivity.RevisitMemberList.get(i).CommunityID;
                    boolean newFlag = true;
                    for(int j=0; j<alRevisitCommunityID.size(); j++) {
                        if(lTemp == alRevisitCommunityID.get(j)) {
                            newFlag = false;
                            break;
                        }
                    }
                    if(newFlag) {
                        if(mainActivity.RevisitMemberList.get(i).ClusterID
                                == CreditCheckInfoMainBorrower.ClusterID) {
                            alRevisitCommunityID.add(lTemp);
                            alRevisitCommunity.add(mainActivity.RevisitMemberList.get(i).Community);
                        }
                    }
                }
                ArrayAdapter<String> adapterVisitDetailsCommunity = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                        R.layout.simple_spinner_item, alRevisitCommunity);
                adapterVisitDetailsCommunity.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                Spinner spSelectCommunity = (Spinner) rootView.findViewById(R.id.spinnerCommunity);
                spSelectCommunity.setAdapter(adapterVisitDetailsCommunity);
                spSelectCommunity.setSelection(0);
                spSelectCommunity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position == 0)
                            return;
                        if(CreditCheckInfoMainBorrower == null) {
                            return;
                        }
                        CreditCheckInfoMainBorrower.CommunityID = alRevisitCommunityID.get(position);
                        CreditCheckInfoMainBorrower.Community = alRevisitCommunity.get(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                spSelectCommunity.setFocusable(true);
                spSelectCommunity.setFocusableInTouchMode(true);
//bmp 02-Jan-25
                for(int i=0; i<alRevisitCommunityID.size(); i++) {
                    if(alRevisitCommunityID.get(i) == CreditCheckInfoMainBorrower.CommunityID) {
                        spSelectCommunity.setSelection(i);
                        break;
                    }
                }
//bmp 02-Jan-25
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spSelectCluster.setFocusable(true);
        spSelectCluster.setFocusableInTouchMode(true);

//        final ArrayList<Long> alRevisitCommunityID = new ArrayList<>();
        alRevisitCommunityID.clear();
        alRevisitCommunityID.add(0L);
//        final ArrayList<String> alRevisitCommunity = new ArrayList<>();
        alRevisitCommunity.clear();
        alRevisitCommunity.add("");
        for(int i=0; i<mainActivity.RevisitMemberList.size(); i++) {
            long lTemp = mainActivity.RevisitMemberList.get(i).CommunityID;
            boolean newFlag = true;
            for(int j=0; j<alRevisitCommunityID.size(); j++) {
                if(lTemp == alRevisitCommunityID.get(j)) {
                    newFlag = false;
                    break;
                }
            }
            if(newFlag) {
                alRevisitCommunityID.add(lTemp);
                alRevisitCommunity.add(mainActivity.RevisitMemberList.get(i).Community);
            }
        }
        ArrayAdapter<String> adapterVisitDetailsCommunity = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, alRevisitCommunity);
        adapterVisitDetailsCommunity.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner spSelectCommunity = (Spinner) rootView.findViewById(R.id.spinnerCommunity);
        spSelectCommunity.setAdapter(adapterVisitDetailsCommunity);
        spSelectCommunity.setSelection(0);
        spSelectCommunity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                    return;
                if(CreditCheckInfoMainBorrower == null) {
                    return;
                }
                CreditCheckInfoMainBorrower.CommunityID = alRevisitCommunityID.get(position);
                CreditCheckInfoMainBorrower.Community = alRevisitCommunity.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spSelectCommunity.setFocusable(true);
        spSelectCommunity.setFocusableInTouchMode(true);

        RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupHouse);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if(rb == null) {
                } else if (rb.getId() == R.id.radioButtonHouseYes) {
                    if(CreditCheckInfoMainBorrower != null)
                        CreditCheckInfoMainBorrower.OwnHouse = 1;
                } else if (rb.getId() == R.id.radioButtonHouseNo) {
                    if(CreditCheckInfoMainBorrower != null)
                        CreditCheckInfoMainBorrower.OwnHouse = 0;
                }
            }
        });

//        EditText et = (EditText) rootView.findViewById(R.id.editTextMobileMainBorrower);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    String strTemp = v.getText().toString();
//                    if(strTemp == null)
//                        strTemp = "0";
//                    else if(strTemp.isEmpty())
//                        strTemp = "0";
//                    else if(strTemp.length() < 10)
//                        strTemp = "0";
//                    CreditCheckInfoMainBorrower.ContactNo = strTemp;
//                }
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    String strTemp = ((EditText) view).getText().toString();
//                    if(strTemp == null)
//                        strTemp = "0";
//                    else if(strTemp.isEmpty())
//                        strTemp = "0";
//                    else if(strTemp.length() < 10)
//                        strTemp = "0";
//                    CreditCheckInfoMainBorrower.ContactNo = strTemp;
//                }
//            }
//        });
//        et = (EditText) rootView.findViewById(R.id.editTextMobileCoBorrower);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    String strTemp = v.getText().toString();
//                    if(strTemp == null)
//                        strTemp = "0";
//                    else if(strTemp.isEmpty())
//                        strTemp = "0";
//                    else if(strTemp.length() < 10)
//                        strTemp = "0";
//                    CreditCheckInfoCoBorrower.ContactNo = strTemp;
//                }
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    String strTemp = ((EditText) view).getText().toString();
//                    if(strTemp == null)
//                        strTemp = "0";
//                    else if(strTemp.isEmpty())
//                        strTemp = "0";
//                    else if(strTemp.length() < 10)
//                        strTemp = "0";
//                    CreditCheckInfoCoBorrower.ContactNo = strTemp;
//                }
//            }
//        });
//        et = (EditText) rootView.findViewById(R.id.editTextMobileGuarantor);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    String strTemp = v.getText().toString();
//                    if(strTemp == null)
//                        strTemp = "0";
//                    else if(strTemp.isEmpty())
//                        strTemp = "0";
//                    else if(strTemp.length() < 10)
//                        strTemp = "0";
//                    CreditCheckInfoGuarantor.ContactNo = strTemp;
//                }
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    String strTemp = ((EditText) view).getText().toString();
//                    if(strTemp == null)
//                        strTemp = "0";
//                    else if(strTemp.isEmpty())
//                        strTemp = "0";
//                    else if(strTemp.length() < 10)
//                        strTemp = "0";
//                    CreditCheckInfoGuarantor.ContactNo = strTemp;
//                }
//            }
//        });
//        et = (EditText) rootView.findViewById(R.id.editTextAadhaarNumberMainBorrower);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    String strTemp = v.getText().toString();
//                    if(strTemp == null)
//                        strTemp = "0";
//                    else if(strTemp.isEmpty())
//                        strTemp = "0";
//                    else if(strTemp.length() < 12)
//                        strTemp = "0";
//                    CreditCheckInfoMainBorrower.AdhaarNo = strTemp;
//                }
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    String strTemp = ((EditText) view).getText().toString();
//                    if(strTemp == null)
//                        strTemp = "0";
//                    else if(strTemp.isEmpty())
//                        strTemp = "0";
//                    else if(strTemp.length() < 12)
//                        strTemp = "0";
//                    CreditCheckInfoMainBorrower.AdhaarNo = strTemp;
//                }
//            }
//        });
//        et = (EditText) rootView.findViewById(R.id.editTextCityMainBorrower);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    CreditCheckInfoMainBorrower.City = v.getText().toString();
//                }
//
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    CreditCheckInfoMainBorrower.City = ((EditText) view).getText().toString();
//                }
//            }
//        });
//        et = (EditText) rootView.findViewById(R.id.editTextCityCoBorrower);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    CreditCheckInfoCoBorrower.City = v.getText().toString();
//                }
//
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    CreditCheckInfoCoBorrower.City = ((EditText) view).getText().toString();
//                }
//            }
//        });
//        et = (EditText) rootView.findViewById(R.id.editTextCityGuarantor);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    CreditCheckInfoGuarantor.City = v.getText().toString();
//                }
//
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    CreditCheckInfoGuarantor.City = ((EditText) view).getText().toString();
//                }
//            }
//        });
//        et = (EditText) rootView.findViewById(R.id.editTextStateMainBorrower);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    CreditCheckInfoMainBorrower.State = v.getText().toString();
//                }
//
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    CreditCheckInfoMainBorrower.State = ((EditText) view).getText().toString();
//                }
//            }
//        });
//        et = (EditText) rootView.findViewById(R.id.editTextStateCoBorrower);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    CreditCheckInfoCoBorrower.State = v.getText().toString();
//                }
//
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    CreditCheckInfoCoBorrower.State = ((EditText) view).getText().toString();
//                }
//            }
//        });
//        et = (EditText) rootView.findViewById(R.id.editTextStateGuarantor);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    CreditCheckInfoGuarantor.State = v.getText().toString();
//                }
//
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    CreditCheckInfoGuarantor.State = ((EditText) view).getText().toString();
//                }
//            }
//        });
//        et = (EditText) rootView.findViewById(R.id.editTextFnameMainBorrower);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    CreditCheckInfoMainBorrower.FName = v.getText().toString();
//                }
//
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    CreditCheckInfoMainBorrower.FName = ((EditText) view).getText().toString();
//                }
//            }
//        });
//        et = (EditText) rootView.findViewById(R.id.editTextMnameMainBorrower);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    CreditCheckInfoMainBorrower.MName = v.getText().toString();
//                }
//
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    CreditCheckInfoMainBorrower.MName = ((EditText) view).getText().toString();
//                }
//            }
//        });
//        et = (EditText) rootView.findViewById(R.id.editTextLnameMainBorrower);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    CreditCheckInfoMainBorrower.LName = v.getText().toString();
//                }
//
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    CreditCheckInfoMainBorrower.LName = ((EditText) view).getText().toString();
//                }
//            }
//        });
//        et = (EditText) rootView.findViewById(R.id.editTextGenderMainBorrower);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    CreditCheckInfoMainBorrower.Gender = v.getText().toString();
//                }
//
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    CreditCheckInfoMainBorrower.Gender = ((EditText) view).getText().toString();
//                }
//            }
//        });
//        et = (EditText) rootView.findViewById(R.id.editTextAddressMainBorrower);
//        et.setHorizontallyScrolling(false);
//        et.setLines(2);
//        et.setMaxLines(2);
//        et.setMinLines(2);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    CreditCheckInfoMainBorrower.Address = v.getText().toString();
//                }
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    CreditCheckInfoMainBorrower.Address = ((EditText) view).getText().toString();
//                }
//            }
//        });
//        et = (EditText) rootView.findViewById(R.id.editTextPincodeMainBorrower);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    String strTemp = v.getText().toString();
//                    try {
//                        int iTemp = Integer.parseInt(strTemp);
//                        if ((strTemp.length() == 6) && (strTemp.charAt(0) == '4')) {
//                            CreditCheckInfoMainBorrower.Pincode = strTemp;
//                        } else {
//                            //Invalid Pincode
//                            v.setText("");
//                        }
//                    } catch (NumberFormatException e) {
//                        //Invalid Pincode
//                        v.setText("");
//                    }
//                }
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    String strTemp = ((EditText) view).getText().toString();
//                    try {
//                        int iTemp = Integer.parseInt(strTemp);
//                        if ((strTemp.length() == 6) && (strTemp.charAt(0) == '4')) {
//                            CreditCheckInfoMainBorrower.Pincode = strTemp;
//                        } else {
//                            //Invalid Pincode
//                            ((EditText) view).getText().clear();
//                        }
//                    } catch (NumberFormatException e) {
//                        //Invalid Pincode
//                        ((EditText) view).getText().clear();
//                    }
//                }
//            }
//        });
//        et = (EditText) rootView.findViewById(R.id.editTextAgeMainBorrower);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    try {
//                        int iAge = Integer.parseInt(v.getText().toString());
//                        CreditCheckInfoMainBorrower.Age = iAge;
//                        Calendar CalendarDOB = Calendar.getInstance();
//                        int iYear = CalendarDOB.get(Calendar.YEAR);
//                        int iYearOfBirth = iYear - iAge;
//                        CalendarDOB.set(Calendar.YEAR, iYearOfBirth);
//                        CalendarDOB.set(Calendar.MONTH, Calendar.JANUARY);
//                        CalendarDOB.set(Calendar.DATE, 1);
//                        String strBirthDate = String.format("%04d", CalendarDOB.get(Calendar.YEAR)) + "-"
//                                + String.format("%02d", (CalendarDOB.get(Calendar.MONTH) + 1)) + "-"
//                                + String.format("%02d", CalendarDOB.get(Calendar.DATE));
//                        CreditCheckInfoMainBorrower.DateOfBirth = strBirthDate;
//                        EditText et = (EditText) rootView.findViewById(R.id.editTextBirthdateMainBorrower);
//                        String strDate = CreditCheckInfoMainBorrower.DateOfBirth;
//                        strBirthDate = strDate.substring(8, 10) + "/"
//                                + strDate.substring(5, 7) + "/"
//                                + strDate.substring(0, 4);
//                        et.setText(strBirthDate);
//                    } catch (NumberFormatException e) {
//                    }
//                }
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    try {
//                        int iAge = Integer.parseInt(((EditText) view).getText().toString());
//                        CreditCheckInfoMainBorrower.Age = iAge;
//                        Calendar CalendarDOB = Calendar.getInstance();
//                        int iYear = CalendarDOB.get(Calendar.YEAR);
//                        int iYearOfBirth = iYear - iAge;
//                        CalendarDOB.set(Calendar.YEAR, iYearOfBirth);
//                        CalendarDOB.set(Calendar.MONTH, Calendar.JANUARY);
//                        CalendarDOB.set(Calendar.DATE, 1);
//                        String strBirthDate = String.format("%04d", CalendarDOB.get(Calendar.YEAR)) + "-"
//                                + String.format("%02d", (CalendarDOB.get(Calendar.MONTH) + 1)) + "-"
//                                + String.format("%02d", CalendarDOB.get(Calendar.DATE));
//                        CreditCheckInfoMainBorrower.DateOfBirth = strBirthDate;
//                        EditText et = (EditText) rootView.findViewById(R.id.editTextBirthdateMainBorrower);
//                        String strDate = CreditCheckInfoMainBorrower.DateOfBirth;
//                        strBirthDate = strDate.substring(8, 10) + "/"
//                                + strDate.substring(5, 7) + "/"
//                                + strDate.substring(0, 4);
//                        et.setText(strBirthDate);
//                    } catch (NumberFormatException e) {
//                    }
//                }
//            }
//        });
        dlgDatePicker = new DatePickerDialog(getContext(),
                date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        etBirthdateMainBorrower = (EditText) rootView.findViewById(R.id.editTextBirthdateMainBorrower);
        etBirthdateMainBorrower.setKeyListener(null);
//        etBirthdateMainBorrower.setEnabled(true);
        etBirthdateMainBorrower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(etBirthdateMainBorrower.getWindowToken(), 0);

                if (!dlgDatePicker.isShowing()) {
                    selectedType = 1;
                    dlgDatePicker.show();
                }
            }
        });
        etBirthdateMainBorrower.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasFocus) {
                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(etBirthdateMainBorrower.getWindowToken(), 0);

                if (hasFocus && !(dlgDatePicker.isShowing())) {
                    selectedType = 1;
                    dlgDatePicker.show();
                }
            }
        });
        etBirthdateCoBorrower = (EditText) rootView.findViewById(R.id.editTextBirthdateCoBorrower);
        etBirthdateCoBorrower.setKeyListener(null);
//        etBirthdateCoBorrower.setEnabled(true);
        etBirthdateCoBorrower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(etBirthdateCoBorrower.getWindowToken(), 0);

                if (!dlgDatePicker.isShowing()) {
                    selectedType = 2;
                    dlgDatePicker.show();
                }
            }
        });
        etBirthdateCoBorrower.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasFocus) {
                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(etBirthdateCoBorrower.getWindowToken(), 0);

                if (hasFocus && !(dlgDatePicker.isShowing())) {
                    selectedType = 2;
                    dlgDatePicker.show();
                }
            }
        });
        etBirthdateGuarantor = (EditText) rootView.findViewById(R.id.editTextBirthdateGuarantor);
        etBirthdateGuarantor.setKeyListener(null);
//        etBirthdateGuarantor.setEnabled(true);
        etBirthdateGuarantor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(etBirthdateGuarantor.getWindowToken(), 0);

                if (!dlgDatePicker.isShowing()) {
                    selectedType = 3;
                    dlgDatePicker.show();
                }
            }
        });
        etBirthdateGuarantor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasFocus) {
                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(etBirthdateGuarantor.getWindowToken(), 0);

                if (hasFocus && !(dlgDatePicker.isShowing())) {
                    selectedType = 3;
                    dlgDatePicker.show();
                }
            }
        });

//        if (pbSync != null) {
//            pbSync.setIndeterminate(true);
//            pbSync.setVisibility(View.INVISIBLE);
//        }

        return rootView;
    }
    void ClearCreditCheckInfo() {
        TextView tv = (TextView) rootView.findViewById(R.id.textViewCreditHistoryMainBorrower);
        tv.setText("");
        tv = (TextView) rootView.findViewById(R.id.textViewCBStatusMainBorrower);
        tv.setText("");
        ImageButton imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonPhotoDocMainBorrower);
        imgbt.setEnabled(false);
        Button bt = (Button) rootView.findViewById(R.id.buttonCBReCheckMainBorrower);
        bt.setEnabled(false);
        EditText et = (EditText) rootView.findViewById(R.id.editTextAadhaarNumberMainBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextFnameMainBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextMnameMainBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextLnameMainBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextGenderMainBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextBirthdateMainBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextAddressMainBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextPincodeMainBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncomeMainBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextCityMainBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextMobileMainBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextOtherKYCIDMainBorrower);
        et.setText("");
        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDMainBorrower);
        sp.setSelection(0);
        sp = (Spinner) rootView.findViewById(R.id.spinnerStateMainBorrower);
        sp.setSelection(0);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationMainBorrower);
        sp.setSelection(0);
        sp = (Spinner) rootView.findViewById(R.id.spinnerMaritalStatus);
        sp.setSelection(0);
        sp = (Spinner) rootView.findViewById(R.id.spinnerCluster);
        sp.setSelection(0);
        sp = (Spinner) rootView.findViewById(R.id.spinnerCommunity);
        sp.setSelection(0);
        RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupHouse);
        rg.clearCheck();

        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxCoBorrower);
        cb.setChecked(false);
        CoBorrowerSelected = false;
        et = (EditText) rootView.findViewById(R.id.editTextAadhaarNumberCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextFnameCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextMnameCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextLnameCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextGenderCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextBirthdateCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextAddressCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextCityCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextPincodeCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncomeCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextMobileCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextOtherKYCIDCoBorrower);
        et.setEnabled(CoBorrowerSelected);

        sp = (Spinner) rootView.findViewById(R.id.spinnerStateCoBorrower);
        sp.setEnabled(CoBorrowerSelected);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDCoBorrower);
        sp.setEnabled(CoBorrowerSelected);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationCoBorrower);
        sp.setEnabled(CoBorrowerSelected);
        sp = (Spinner) rootView.findViewById(R.id.spinnerRelationCoBorrower);
        sp.setEnabled(CoBorrowerSelected);

        bt = (Button) rootView.findViewById(R.id.buttonScanCoBorrower);
        bt.setEnabled(CoBorrowerSelected);

        cb = (CheckBox) rootView.findViewById(R.id.checkBoxFamilyMemberCoBorrower);
        cb.setEnabled(CoBorrowerSelected);

        tv = (TextView) rootView.findViewById(R.id.textViewCreditHistoryCoBorrower);
        tv.setText("");
        tv = (TextView) rootView.findViewById(R.id.textViewCBStatusCoBorrower);
        tv.setText("");
        imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonPhotoDocCoBorrower);
        imgbt.setEnabled(false);
        bt = (Button) rootView.findViewById(R.id.buttonCBReCheckCoBorrower);
        bt.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextAadhaarNumberCoBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextFnameCoBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextMnameCoBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextLnameCoBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextGenderCoBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextBirthdateCoBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextAddressCoBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextPincodeCoBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncomeCoBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextCityCoBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextMobileCoBorrower);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextOtherKYCIDCoBorrower);
        et.setText("");
        sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDCoBorrower);
        sp.setSelection(0);
        sp = (Spinner) rootView.findViewById(R.id.spinnerStateCoBorrower);
        sp.setSelection(0);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationCoBorrower);
        sp.setSelection(0);
        sp = (Spinner) rootView.findViewById(R.id.spinnerRelationCoBorrower);
        sp.setSelection(0);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxFamilyMemberCoBorrower);
        cb.setChecked(false);

        tv = (TextView) rootView.findViewById(R.id.textViewCreditHistoryGuarantor);
        tv.setText("");
        tv = (TextView) rootView.findViewById(R.id.textViewCBStatusGuarantor);
        tv.setText("");
        imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonPhotoDocGuarantor);
        imgbt.setEnabled(false);
        bt = (Button) rootView.findViewById(R.id.buttonCBReCheckGuarantor);
        bt.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextAadhaarNumberGuarantor);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextFnameGuarantor);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextMnameGuarantor);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextLnameGuarantor);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextGenderGuarantor);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextBirthdateGuarantor);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextAddressGuarantor);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextPincodeGuarantor);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncomeGuarantor);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextCityGuarantor);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextMobileGuarantor);
        et.setText("");
        et = (EditText) rootView.findViewById(R.id.editTextOtherKYCIDGuarantor);
        et.setText("");
        sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDGuarantor);
        sp.setSelection(0);
        sp = (Spinner) rootView.findViewById(R.id.spinnerStateGuarantor);
        sp.setSelection(0);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationGuarantor);
        sp.setSelection(0);
        sp = (Spinner) rootView.findViewById(R.id.spinnerRelationGuarantor);
        sp.setSelection(0);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxFamilyMemberGuarantor);
        cb.setChecked(false);
    }
    void RefreshCreditCheckInfo() {
        TextView tv = (TextView) rootView.findViewById(R.id.textViewCreditHistoryMainBorrower);
//        tv.setText(CreditCheckInfoMainBorrower.CHRemarks);
        String strCH = CreditCheckInfoMainBorrower.CHRemarks + ":" + CreditCheckInfoMainBorrower.ListType;
        tv.setText(strCH);

        tv = (TextView) rootView.findViewById(R.id.textViewCBStatusMainBorrower);
        tv.setText(CreditCheckInfoMainBorrower.CBStatus);
        if(CreditCheckInfoMainBorrower.CBStatus.equalsIgnoreCase("Reject")) {
            ImageButton imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonPhotoDocMainBorrower);
            imgbt.setEnabled(true);
        } else {
            ImageButton imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonPhotoDocMainBorrower);
            imgbt.setEnabled(false);
            Button bt = (Button) rootView.findViewById(R.id.buttonCBReCheckMainBorrower);
            bt.setEnabled(false);
        }
        EditText et = (EditText) rootView.findViewById(R.id.editTextAadhaarNumberMainBorrower);
        et.setText(CreditCheckInfoMainBorrower.AdhaarNo);
        et = (EditText) rootView.findViewById(R.id.editTextCityMainBorrower);
        et.setText(CreditCheckInfoMainBorrower.City);
        et = (EditText) rootView.findViewById(R.id.editTextCityCoBorrower);
        et.setText(CreditCheckInfoCoBorrower.City);
        et = (EditText) rootView.findViewById(R.id.editTextCityGuarantor);
        et.setText(CreditCheckInfoGuarantor.City);
        et = (EditText) rootView.findViewById(R.id.editTextFnameMainBorrower);
        et.setText(CreditCheckInfoMainBorrower.FName);
        et = (EditText) rootView.findViewById(R.id.editTextMnameMainBorrower);
        et.setText(CreditCheckInfoMainBorrower.MName);
        et = (EditText) rootView.findViewById(R.id.editTextLnameMainBorrower);
        et.setText(CreditCheckInfoMainBorrower.LName);
        et = (EditText) rootView.findViewById(R.id.editTextGenderMainBorrower);
        et.setText(CreditCheckInfoMainBorrower.Gender);
        et = (EditText) rootView.findViewById(R.id.editTextBirthdateMainBorrower);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        //yyyy-MM-dd Format for DB
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String strDOB = "";
        try {
            if(!CreditCheckInfoMainBorrower.DateOfBirth.isEmpty()) {
                Date dob = sdf2.parse(CreditCheckInfoMainBorrower.DateOfBirth);
                strDOB = sdf.format(dob);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        et.setText(strDOB);

        et = (EditText) rootView.findViewById(R.id.editTextAddressMainBorrower);
        et.setText(CreditCheckInfoMainBorrower.Address);
        et = (EditText) rootView.findViewById(R.id.editTextPincodeMainBorrower);
        et.setText(CreditCheckInfoMainBorrower.Pincode);
        et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncomeMainBorrower);
        et.setText(String.valueOf(CreditCheckInfoMainBorrower.MonthlyIncome));
        et = (EditText) rootView.findViewById(R.id.editTextMobileMainBorrower);
        et.setText(CreditCheckInfoMainBorrower.ContactNo);
        et = (EditText) rootView.findViewById(R.id.editTextOtherKYCIDMainBorrower);
        if(!CreditCheckInfoMainBorrower.RationCardNo.isEmpty() && (!CreditCheckInfoMainBorrower.RationCardNo.equalsIgnoreCase("NA"))) {
            et.setText(CreditCheckInfoMainBorrower.RationCardNo);
            Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDMainBorrower);
            sp.setSelection(1);
        } else if(!CreditCheckInfoMainBorrower.PAN_No.isEmpty() && (!CreditCheckInfoMainBorrower.PAN_No.equalsIgnoreCase("NA"))) {
            et.setText(CreditCheckInfoMainBorrower.PAN_No);
            Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDMainBorrower);
            sp.setSelection(2);
        } else if(!CreditCheckInfoMainBorrower.VoterID.isEmpty() && (!CreditCheckInfoMainBorrower.VoterID.equalsIgnoreCase("NA"))) {
            et.setText(CreditCheckInfoMainBorrower.VoterID);
            Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDMainBorrower);
            sp.setSelection(3);
        }
        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationMainBorrower);
        for(int i=0; i<BusinessIDMaster.size(); i++) {
            if(BusinessIDMaster.get(i) == CreditCheckInfoMainBorrower.ProfessionID) {
                sp.setSelection(i);
                break;
            }
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerStateMainBorrower);
        for(int i=0; i<StateCodeMaster.size(); i++) {
            if(StateCodeMaster.get(i).equalsIgnoreCase(CreditCheckInfoMainBorrower.State)) {
                sp.setSelection(i);
                break;
            }
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerStateCoBorrower);
        for(int i=0; i<StateCodeMaster.size(); i++) {
            if(StateCodeMaster.get(i).equalsIgnoreCase(CreditCheckInfoCoBorrower.State)) {
                sp.setSelection(i);
                break;
            }
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerStateGuarantor);
        for(int i=0; i<StateCodeMaster.size(); i++) {
            if(StateCodeMaster.get(i).equalsIgnoreCase(CreditCheckInfoGuarantor.State)) {
                sp.setSelection(i);
                break;
            }
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerMaritalStatus);
        for(int i=0; i<MaritalStatusIDMaster.size(); i++) {
            if(MaritalStatusIDMaster.get(i) == CreditCheckInfoMainBorrower.MaritalStatusID) {
                sp.setSelection(i);
                break;
            }
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerCluster);
        for(int i=0; i<alRevisitClusterID.size(); i++) {
            if(alRevisitClusterID.get(i) == CreditCheckInfoMainBorrower.ClusterID) {
                sp.setSelection(i);

                alRevisitCommunityID.clear();
                alRevisitCommunityID.add(0L);
                alRevisitCommunity.clear();
                alRevisitCommunity.add("");
                for(int k=0; k<mainActivity.RevisitMemberList.size(); k++) {
                    long lTemp = mainActivity.RevisitMemberList.get(k).CommunityID;
                    boolean newFlag = true;
                    for(int j=0; j<alRevisitCommunityID.size(); j++) {
                        if(lTemp == alRevisitCommunityID.get(j)) {
                            newFlag = false;
                            break;
                        }
                    }
                    if(newFlag) {
                        if(mainActivity.RevisitMemberList.get(k).ClusterID
                                == CreditCheckInfoMainBorrower.ClusterID) {
                            alRevisitCommunityID.add(lTemp);
                            alRevisitCommunity.add(mainActivity.RevisitMemberList.get(k).Community);
                        }
                    }
                }
                ArrayAdapter<String> adapterVisitDetailsCommunity = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                        R.layout.simple_spinner_item, alRevisitCommunity);
                adapterVisitDetailsCommunity.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                Spinner spSelectCommunity = (Spinner) rootView.findViewById(R.id.spinnerCommunity);
                spSelectCommunity.setAdapter(adapterVisitDetailsCommunity);
                spSelectCommunity.setSelection(0);
                break;
            }
        }

        sp = (Spinner) rootView.findViewById(R.id.spinnerCommunity);
        for(int i=0; i<alRevisitCommunityID.size(); i++) {
            if(alRevisitCommunityID.get(i) == CreditCheckInfoMainBorrower.CommunityID) {
                sp.setSelection(i);
                break;
            }
        }
        if(CreditCheckInfoMainBorrower.OwnHouse == 1) {
            RadioButton rb = (RadioButton) rootView.findViewById(R.id.radioButtonHouseYes);
            rb.setChecked(true);
            rb = (RadioButton) rootView.findViewById(R.id.radioButtonHouseNo);
            rb.setChecked(false);
        } else if(CreditCheckInfoMainBorrower.OwnHouse == 0) {
            RadioButton rb = (RadioButton) rootView.findViewById(R.id.radioButtonHouseYes);
            rb.setChecked(false);
            rb = (RadioButton) rootView.findViewById(R.id.radioButtonHouseNo);
            rb.setChecked(true);
        } else {
            RadioButton rb = (RadioButton) rootView.findViewById(R.id.radioButtonHouseYes);
            rb.setChecked(false);
            rb = (RadioButton) rootView.findViewById(R.id.radioButtonHouseNo);
            rb.setChecked(false);
        }

        et = (EditText) rootView.findViewById(R.id.editTextAadhaarNumberCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextFnameCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextMnameCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextLnameCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextGenderCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextBirthdateCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextAddressCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextCityCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextPincodeCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncomeCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextMobileCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextOtherKYCIDCoBorrower);
        et.setEnabled(CoBorrowerSelected);

        sp = (Spinner) rootView.findViewById(R.id.spinnerStateCoBorrower);
        sp.setEnabled(CoBorrowerSelected);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDCoBorrower);
        sp.setEnabled(CoBorrowerSelected);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationCoBorrower);
        sp.setEnabled(CoBorrowerSelected);
        sp = (Spinner) rootView.findViewById(R.id.spinnerRelationCoBorrower);
        sp.setEnabled(CoBorrowerSelected);

        Button bt = (Button) rootView.findViewById(R.id.buttonScanCoBorrower);
        bt.setEnabled(CoBorrowerSelected);

        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxFamilyMemberCoBorrower);
        cb.setEnabled(CoBorrowerSelected);

        if(CoBorrowerSelected) {
            tv = (TextView) rootView.findViewById(R.id.textViewCreditHistoryCoBorrower);
            tv.setText(CreditCheckInfoCoBorrower.CHRemarks);
            tv = (TextView) rootView.findViewById(R.id.textViewCBStatusCoBorrower);
            tv.setText(CreditCheckInfoCoBorrower.CBStatus);
            if(CreditCheckInfoCoBorrower.CBStatus.equalsIgnoreCase("Reject")) {
                ImageButton imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonPhotoDocCoBorrower);
                imgbt.setEnabled(true);
            } else {
                ImageButton imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonPhotoDocCoBorrower);
                imgbt.setEnabled(false);
                bt = (Button) rootView.findViewById(R.id.buttonCBReCheckCoBorrower);
                bt.setEnabled(false);
            }
            et = (EditText) rootView.findViewById(R.id.editTextAadhaarNumberCoBorrower);
            et.setText(CreditCheckInfoCoBorrower.AdhaarNo);
            et = (EditText) rootView.findViewById(R.id.editTextFnameCoBorrower);
            et.setText(CreditCheckInfoCoBorrower.FName);
            et = (EditText) rootView.findViewById(R.id.editTextMnameCoBorrower);
            et.setText(CreditCheckInfoCoBorrower.MName);
            et = (EditText) rootView.findViewById(R.id.editTextLnameCoBorrower);
            et.setText(CreditCheckInfoCoBorrower.LName);
            et = (EditText) rootView.findViewById(R.id.editTextGenderCoBorrower);
            et.setText(CreditCheckInfoCoBorrower.Gender);
            et = (EditText) rootView.findViewById(R.id.editTextBirthdateCoBorrower);
            strDOB = "";
            try {
                if(!CreditCheckInfoCoBorrower.DateOfBirth.isEmpty()) {
                    Date dob = sdf2.parse(CreditCheckInfoCoBorrower.DateOfBirth);
                    strDOB = sdf.format(dob);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            et.setText(strDOB);
            et = (EditText) rootView.findViewById(R.id.editTextAddressCoBorrower);
            et.setText(CreditCheckInfoCoBorrower.Address);
            et = (EditText) rootView.findViewById(R.id.editTextPincodeCoBorrower);
            et.setText(CreditCheckInfoCoBorrower.Pincode);
            et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncomeCoBorrower);
            et.setText(String.valueOf(CreditCheckInfoCoBorrower.MonthlyIncome));
            et = (EditText) rootView.findViewById(R.id.editTextMobileCoBorrower);
            et.setText(CreditCheckInfoCoBorrower.ContactNo);
            et = (EditText) rootView.findViewById(R.id.editTextOtherKYCIDCoBorrower);
            if(!CreditCheckInfoCoBorrower.RationCardNo.isEmpty() && (!CreditCheckInfoCoBorrower.RationCardNo.equalsIgnoreCase("NA"))) {
                et.setText(CreditCheckInfoCoBorrower.RationCardNo);
                sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDCoBorrower);
                sp.setSelection(1);
            } else if(!CreditCheckInfoCoBorrower.PAN_No.isEmpty() && (!CreditCheckInfoCoBorrower.PAN_No.equalsIgnoreCase("NA"))) {
                et.setText(CreditCheckInfoCoBorrower.PAN_No);
                sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDCoBorrower);
                sp.setSelection(2);
            } else if(!CreditCheckInfoCoBorrower.VoterID.isEmpty() && (!CreditCheckInfoCoBorrower.VoterID.equalsIgnoreCase("NA"))) {
                et.setText(CreditCheckInfoCoBorrower.VoterID);
                sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDCoBorrower);
                sp.setSelection(3);
            }
            sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationCoBorrower);
            for(int i=0; i<BusinessIDMaster.size(); i++) {
                if(BusinessIDMaster.get(i) == CreditCheckInfoCoBorrower.ProfessionID) {
                    sp.setSelection(i);
                    break;
                }
            }
            sp = (Spinner) rootView.findViewById(R.id.spinnerRelationCoBorrower);
            for(int i=0; i<RelationIDMaster.size(); i++) {
                if(RelationIDMaster.get(i) == CreditCheckInfoCoBorrower.MainBorrowerRelationID) {
                    sp.setSelection(i);
                    break;
                }
            }
            cb = (CheckBox) rootView.findViewById(R.id.checkBoxFamilyMemberCoBorrower);
            if(CreditCheckInfoCoBorrower.iFamilyMember == 1)
                cb.setChecked(true);
        }
        tv = (TextView) rootView.findViewById(R.id.textViewCreditHistoryGuarantor);
        tv.setText(CreditCheckInfoGuarantor.CHRemarks);
        tv = (TextView) rootView.findViewById(R.id.textViewCBStatusGuarantor);
        tv.setText(CreditCheckInfoGuarantor.CBStatus);
        if(CreditCheckInfoGuarantor.CBStatus.equalsIgnoreCase("Reject")) {
            ImageButton imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonPhotoDocGuarantor);
            imgbt.setEnabled(true);
        } else {
            ImageButton imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonPhotoDocGuarantor);
            imgbt.setEnabled(false);
            bt = (Button) rootView.findViewById(R.id.buttonCBReCheckGuarantor);
            bt.setEnabled(false);
        }
        et = (EditText) rootView.findViewById(R.id.editTextAadhaarNumberGuarantor);
        et.setText(CreditCheckInfoGuarantor.AdhaarNo);
        et = (EditText) rootView.findViewById(R.id.editTextFnameGuarantor);
        et.setText(CreditCheckInfoGuarantor.FName);
        et = (EditText) rootView.findViewById(R.id.editTextMnameGuarantor);
        et.setText(CreditCheckInfoGuarantor.MName);
        et = (EditText) rootView.findViewById(R.id.editTextLnameGuarantor);
        et.setText(CreditCheckInfoGuarantor.LName);
        et = (EditText) rootView.findViewById(R.id.editTextGenderGuarantor);
        et.setText(CreditCheckInfoGuarantor.Gender);
        et = (EditText) rootView.findViewById(R.id.editTextBirthdateGuarantor);
        strDOB = "";
        try {
            if(!CreditCheckInfoGuarantor.DateOfBirth.isEmpty()) {
                Date dob = sdf2.parse(CreditCheckInfoGuarantor.DateOfBirth);
                strDOB = sdf.format(dob);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        et.setText(strDOB);
        et = (EditText) rootView.findViewById(R.id.editTextAddressGuarantor);
        et.setText(CreditCheckInfoGuarantor.Address);
        et = (EditText) rootView.findViewById(R.id.editTextPincodeGuarantor);
        et.setText(CreditCheckInfoGuarantor.Pincode);
        et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncomeGuarantor);
        et.setText(String.valueOf(CreditCheckInfoGuarantor.MonthlyIncome));
        et = (EditText) rootView.findViewById(R.id.editTextMobileGuarantor);
        et.setText(CreditCheckInfoGuarantor.ContactNo);
        et = (EditText) rootView.findViewById(R.id.editTextOtherKYCIDGuarantor);
        if(!CreditCheckInfoGuarantor.RationCardNo.isEmpty() && (!CreditCheckInfoGuarantor.RationCardNo.equalsIgnoreCase("NA"))) {
            et.setText(CreditCheckInfoGuarantor.RationCardNo);
            sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDGuarantor);
            sp.setSelection(1);
        } else if(!CreditCheckInfoGuarantor.PAN_No.isEmpty() && (!CreditCheckInfoGuarantor.PAN_No.equalsIgnoreCase("NA"))) {
            et.setText(CreditCheckInfoGuarantor.PAN_No);
            sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDGuarantor);
            sp.setSelection(2);
        } else if(!CreditCheckInfoGuarantor.VoterID.isEmpty() && (!CreditCheckInfoGuarantor.VoterID.equalsIgnoreCase("NA"))) {
            et.setText(CreditCheckInfoGuarantor.VoterID);
            sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDGuarantor);
            sp.setSelection(3);
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationGuarantor);
        for(int i=0; i<BusinessIDMaster.size(); i++) {
            if(BusinessIDMaster.get(i) == CreditCheckInfoGuarantor.ProfessionID) {
                sp.setSelection(i);
                break;
            }
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerRelationGuarantor);
        for(int i=0; i<RelationIDMaster.size(); i++) {
            if(RelationIDMaster.get(i) == CreditCheckInfoGuarantor.MainBorrowerRelationID) {
                sp.setSelection(i);
                break;
            }
        }
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxFamilyMemberGuarantor);
        if(CreditCheckInfoGuarantor.iFamilyMember == 1)
            cb.setChecked(true);

        bt = (Button) rootView.findViewById(R.id.buttonCreditCheck);
        if(CreditCheckInfoMainBorrower.CreditCheckProgress == null) {
            bt.setEnabled(true);
        } else if(CreditCheckInfoMainBorrower.CreditCheckProgress.isEmpty()) {
            bt.setEnabled(true);
        } else {
//bmp 14-May-25
//            bt.setEnabled(false);
//            DisableCreditCheckInfo();
//bmp 14-May-25
            boolean CreditCheckOK = false;
            if(     CreditCheckInfoMainBorrower.CHRemarks.equalsIgnoreCase("OK") &&
                    CreditCheckInfoMainBorrower.CBStatus.equalsIgnoreCase("OK") &&
                    CreditCheckInfoGuarantor.CHRemarks.equalsIgnoreCase("OK")
                    && CreditCheckInfoGuarantor.CBStatus.equalsIgnoreCase("OK")
            ) {
                if(CoBorrowerSelected) {
                    if(     CreditCheckInfoCoBorrower.CHRemarks.equalsIgnoreCase("OK")
                            && CreditCheckInfoCoBorrower.CBStatus.equalsIgnoreCase("OK")
                    ) {
                        CreditCheckOK = true;
                    }
                } else {
                    CreditCheckOK = true;
                }
            }
            if(CreditCheckOK) {
                bt = (Button) rootView.findViewById(R.id.buttonCreditCheckOK);
                bt.setEnabled(true);
//bmp 14-May-25
                bt = (Button) rootView.findViewById(R.id.buttonCreditCheck);
                bt.setEnabled(false);
                DisableCreditCheckInfo();
                return;
//bmp 14-May-25
            }
//bmp 14-May-25
            if(CoBorrowerSelected) {
                if(         CreditCheckInfoCoBorrower.CHRemarks.equalsIgnoreCase("Reject")
                        ||  CreditCheckInfoCoBorrower.CBStatus.equalsIgnoreCase("Reject")
                ) {
                    bt = (Button) rootView.findViewById(R.id.buttonCreditCheck);
                    bt.setEnabled(true);
                    return;
                }
            }
            if(     CreditCheckInfoGuarantor.CHRemarks.equalsIgnoreCase("Reject")
                    || CreditCheckInfoGuarantor.CBStatus.equalsIgnoreCase("Reject")
            ) {
                bt = (Button) rootView.findViewById(R.id.buttonCreditCheck);
                bt.setEnabled(true);
                return;
            }
            DisableCreditCheckInfo();
//bmp 14-May-25
        }
    }
    void DisableCreditCheckInfo() {
        EditText et = (EditText) rootView.findViewById(R.id.editTextAadhaarNumberMainBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextFnameMainBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextMnameMainBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextLnameMainBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextGenderMainBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextBirthdateMainBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextAddressMainBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextCityMainBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextPincodeMainBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncomeMainBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextMobileMainBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextOtherKYCIDMainBorrower);
        et.setEnabled(false);

        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerStateMainBorrower);
        sp.setEnabled(false);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDMainBorrower);
        sp.setEnabled(false);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationMainBorrower);
        sp.setEnabled(false);
        sp = (Spinner) rootView.findViewById(R.id.spinnerMaritalStatus);
        sp.setEnabled(false);
        sp = (Spinner) rootView.findViewById(R.id.spinnerCluster);
        sp.setEnabled(false);
        sp = (Spinner) rootView.findViewById(R.id.spinnerCommunity);
        sp.setEnabled(false);

        RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupHouse);
        rg.setEnabled(false);

        Button bt = (Button) rootView.findViewById(R.id.buttonScanMainBorrower);
        bt.setEnabled(false);

        et = (EditText) rootView.findViewById(R.id.editTextAadhaarNumberCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextFnameCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextMnameCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextLnameCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextGenderCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextBirthdateCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextAddressCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextCityCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextPincodeCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncomeCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextMobileCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextOtherKYCIDCoBorrower);
        et.setEnabled(CoBorrowerSelected);

        sp = (Spinner) rootView.findViewById(R.id.spinnerStateCoBorrower);
        sp.setEnabled(false);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDCoBorrower);
        sp.setEnabled(false);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationCoBorrower);
        sp.setEnabled(false);
        sp = (Spinner) rootView.findViewById(R.id.spinnerRelationCoBorrower);
        sp.setEnabled(false);

        bt = (Button) rootView.findViewById(R.id.buttonScanCoBorrower);
        bt.setEnabled(false);

        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxFamilyMemberCoBorrower);
        cb.setEnabled(false);

        et = (EditText) rootView.findViewById(R.id.editTextAadhaarNumberGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextFnameGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextMnameGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextLnameGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextGenderGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextBirthdateGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextAddressGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextCityGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextPincodeGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncomeGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextMobileGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextOtherKYCIDGuarantor);
        et.setEnabled(false);

        sp = (Spinner) rootView.findViewById(R.id.spinnerStateGuarantor);
        sp.setEnabled(false);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDGuarantor);
        sp.setEnabled(false);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationGuarantor);
        sp.setEnabled(false);
        sp = (Spinner) rootView.findViewById(R.id.spinnerRelationGuarantor);
        sp.setEnabled(false);

        bt = (Button) rootView.findViewById(R.id.buttonScanGuarantor);
        bt.setEnabled(false);

        cb = (CheckBox) rootView.findViewById(R.id.checkBoxFamilyMemberGuarantor);
        cb.setEnabled(false);
    }
    void DisableCreditCheckInfoCoBorrower() {
        EditText et = (EditText) rootView.findViewById(R.id.editTextAadhaarNumberCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextFnameCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextMnameCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextLnameCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextGenderCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextBirthdateCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextAddressCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextCityCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextPincodeCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncomeCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextMobileCoBorrower);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextOtherKYCIDCoBorrower);
        et.setEnabled(false);

        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerStateCoBorrower);
        sp.setEnabled(false);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDCoBorrower);
        sp.setEnabled(false);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationCoBorrower);
        sp.setEnabled(false);
        sp = (Spinner) rootView.findViewById(R.id.spinnerRelationCoBorrower);
        sp.setEnabled(false);

        Button bt = (Button) rootView.findViewById(R.id.buttonScanCoBorrower);
        bt.setEnabled(false);

        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxFamilyMemberCoBorrower);
        cb.setEnabled(false);
    }
    void EnableCreditCheckInfoCoBorrower() {
        EditText et = (EditText) rootView.findViewById(R.id.editTextAadhaarNumberCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextFnameCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextMnameCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextLnameCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextGenderCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextBirthdateCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextAddressCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextCityCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextPincodeCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncomeCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextMobileCoBorrower);
        et.setEnabled(CoBorrowerSelected);
        et = (EditText) rootView.findViewById(R.id.editTextOtherKYCIDCoBorrower);
        et.setEnabled(CoBorrowerSelected);

        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerStateCoBorrower);
        sp.setEnabled(CoBorrowerSelected);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDCoBorrower);
        sp.setEnabled(CoBorrowerSelected);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationCoBorrower);
        sp.setEnabled(CoBorrowerSelected);
        sp = (Spinner) rootView.findViewById(R.id.spinnerRelationCoBorrower);
        sp.setEnabled(CoBorrowerSelected);

        Button bt = (Button) rootView.findViewById(R.id.buttonScanCoBorrower);
        bt.setEnabled(CoBorrowerSelected);

        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxFamilyMemberCoBorrower);
        cb.setEnabled(CoBorrowerSelected);
    }
    void DisableCreditCheckInfoGuarantor() {
        EditText et = (EditText) rootView.findViewById(R.id.editTextAadhaarNumberGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextFnameGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextMnameGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextLnameGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextGenderGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextBirthdateGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextAddressGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextCityGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextPincodeGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncomeGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextMobileGuarantor);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextOtherKYCIDGuarantor);
        et.setEnabled(false);

        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerStateGuarantor);
        sp.setEnabled(false);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDGuarantor);
        sp.setEnabled(false);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationGuarantor);
        sp.setEnabled(false);
        sp = (Spinner) rootView.findViewById(R.id.spinnerRelationGuarantor);
        sp.setEnabled(false);

        Button bt = (Button) rootView.findViewById(R.id.buttonScanGuarantor);
        bt.setEnabled(false);

        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxFamilyMemberGuarantor);
        cb.setEnabled(false);
    }
    void EnableCreditCheckInfoGuarantor() {
        EditText et = (EditText) rootView.findViewById(R.id.editTextAadhaarNumberGuarantor);
        et.setEnabled(true);
        et = (EditText) rootView.findViewById(R.id.editTextFnameGuarantor);
        et.setEnabled(true);
        et = (EditText) rootView.findViewById(R.id.editTextMnameGuarantor);
        et.setEnabled(true);
        et = (EditText) rootView.findViewById(R.id.editTextLnameGuarantor);
        et.setEnabled(true);
        et = (EditText) rootView.findViewById(R.id.editTextGenderGuarantor);
        et.setEnabled(true);
        et = (EditText) rootView.findViewById(R.id.editTextBirthdateGuarantor);
        et.setEnabled(true);
        et = (EditText) rootView.findViewById(R.id.editTextAddressGuarantor);
        et.setEnabled(true);
        et = (EditText) rootView.findViewById(R.id.editTextCityGuarantor);
        et.setEnabled(true);
        et = (EditText) rootView.findViewById(R.id.editTextPincodeGuarantor);
        et.setEnabled(true);
        et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncomeGuarantor);
        et.setEnabled(true);
        et = (EditText) rootView.findViewById(R.id.editTextMobileGuarantor);
        et.setEnabled(true);
        et = (EditText) rootView.findViewById(R.id.editTextOtherKYCIDGuarantor);
        et.setEnabled(true);

        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerStateGuarantor);
        sp.setEnabled(true);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDGuarantor);
        sp.setEnabled(true);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationGuarantor);
        sp.setEnabled(true);
        sp = (Spinner) rootView.findViewById(R.id.spinnerRelationGuarantor);
        sp.setEnabled(true);

        Button bt = (Button) rootView.findViewById(R.id.buttonScanGuarantor);
        bt.setEnabled(true);

        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxFamilyMemberGuarantor);
        cb.setEnabled(true);
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
            if(lSet > lNow) {
                System.out.println("Invalid Date!");
                return;
            }

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            String strDate = sdf.format(myCalendar.getTime());
            //yyyy-MM-dd Format for DB
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

            int iYear = calNow.get(Calendar.YEAR);
            int iAge = iYear - year;
            if (calNow.get(Calendar.DAY_OF_YEAR) < calSet.get(Calendar.DAY_OF_YEAR)) {
                iAge--;
            }

            switch (selectedType) {
                case 1:
                    etBirthdateMainBorrower.setText(strDate);
                    CreditCheckInfoMainBorrower.DateOfBirth = sdf2.format(myCalendar.getTime());
//                    CreditCheckInfoMainBorrower.Age = iAge;
//                    EditText et = (EditText) rootView.findViewById(R.id.editTextAgeMainBorrower);
//                    et.setText(String.valueOf(CreditCheckInfoMainBorrower.Age));
                    break;
                case 2:
                    etBirthdateCoBorrower.setText(strDate);
                    CreditCheckInfoCoBorrower.DateOfBirth = sdf2.format(myCalendar.getTime());
//                    CreditCheckInfoCoBorrower.Age = iAge;
//                    et = (EditText) rootView.findViewById(R.id.editTextAgeCoBorrower);
//                    et.setText(String.valueOf(CreditCheckInfoCoBorrower.Age));
                    break;
                case 3:
                    etBirthdateGuarantor.setText(strDate);
                    CreditCheckInfoGuarantor.DateOfBirth = sdf2.format(myCalendar.getTime());
//                    CreditCheckInfoGuarantor.Age = iAge;
//                    et = (EditText) rootView.findViewById(R.id.editTextAgeGuarantor);
//                    et.setText(String.valueOf(CreditCheckInfoGuarantor.Age));
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonClearCreditCheck:
                Spinner spMainBorrower = (Spinner) rootView.findViewById(R.id.spinnerMainBorrower);
                int iSelection = spMainBorrower.getSelectedItemPosition();
                if(iSelection > 0) {
                    String strSelectedMainBorrowerAdhaarNo = CreditCheckInfoList.get(iSelection).MainBorrowerAdhaarNo;
                    ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
                    // Gets the data repository in write mode
                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    try {
                        String whereClause = ufinDbContract.CreditValidityCheck.COLUMN_NAME_MainBorrowerAdhaarNo + "=" + "'" + strSelectedMainBorrowerAdhaarNo + "'";
                        long lTemp1 = db.delete(ufinDbContract.CreditValidityCheck.TABLE_NAME, whereClause, null);
                    } finally {
                        db.close();
                    }
                    SQLite_getCreditCheckList();
                    ArrayAdapter<String> adapterMainBorrower = new ArrayAdapter<String>(getActivity().getBaseContext(),
                            R.layout.simple_spinner_item, CreditCheckList);
                    adapterMainBorrower.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                    Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerMainBorrower);
                    sp.setAdapter(adapterMainBorrower);
                    sp.setSelection(0);
                    sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                CreditCheckInfoMainBorrower = new clsCreditCheckInfo();
                                CreditCheckInfoMainBorrower.InitCreditCheckInfo();
                                CreditCheckInfoCoBorrower = new clsCreditCheckInfo();
                                CreditCheckInfoCoBorrower.InitCreditCheckInfo();
                                CreditCheckInfoGuarantor = new clsCreditCheckInfo();
                                CreditCheckInfoGuarantor.InitCreditCheckInfo();
//                                Button bt = (Button) rootView.findViewById(R.id.buttonCreditReview);
//                                bt.setEnabled(false);
                                Button bt = (Button) rootView.findViewById(R.id.buttonCreditCheckOK);
                                bt.setEnabled(false);
                                ClearCreditCheckInfo();
                                RefreshCreditCheckInfo();
                                return;
                            }
                            String strTemp = CreditCheckList.get(position);
                            String[] strSplit = strTemp.split(":");
                            String strAdhaarNo = strSplit[0];
                            CreditCheckInfoMainBorrower = new clsCreditCheckInfo();
                            CreditCheckInfoMainBorrower.InitCreditCheckInfo();
                            CreditCheckInfoCoBorrower = new clsCreditCheckInfo();
                            CreditCheckInfoCoBorrower.InitCreditCheckInfo();
                            CreditCheckInfoGuarantor = new clsCreditCheckInfo();
                            CreditCheckInfoGuarantor.InitCreditCheckInfo();
//                            Button bt = (Button) rootView.findViewById(R.id.buttonCreditReview);
//                            bt.setEnabled(false);
                            Button bt = (Button) rootView.findViewById(R.id.buttonCreditCheckOK);
                            bt.setEnabled(false);
                            CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxCoBorrower);
                            cb.setChecked(false);
                            CoBorrowerSelected = false;
                            for(int i=0; i<CreditCheckInfoList.size(); i++) {
                                if(strAdhaarNo.equalsIgnoreCase(CreditCheckInfoList.get(i).MainBorrowerAdhaarNo)) {
                                    if(CreditCheckInfoList.get(i).Role.equalsIgnoreCase("M")) {
                                        CreditCheckInfoMainBorrower = CreditCheckInfoList.get(i);
                                    } else if(CreditCheckInfoList.get(i).Role.equalsIgnoreCase("C")) {
                                        CreditCheckInfoCoBorrower = CreditCheckInfoList.get(i);
                                        cb = (CheckBox) rootView.findViewById(R.id.checkBoxCoBorrower);
                                        cb.setChecked(true);
                                        CoBorrowerSelected = true;
                                    } else if(CreditCheckInfoList.get(i).Role.equalsIgnoreCase("G")) {
                                        CreditCheckInfoGuarantor = CreditCheckInfoList.get(i);
                                    }
                                }
                            }
                            RefreshCreditCheckInfo();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    sp.setFocusable(true);
                    sp.setFocusableInTouchMode(true);
                }
                break;
            case R.id.buttonCHMainBorrower:
                String strCHDetails = CreditCheckInfoMainBorrower.CreditHistory;
                strCHDetails = strCHDetails.replace(";", "\n");
                CreateAlertDialog(true, true);
                dlgState = ufin_CREDITCHECK_DIALOG_STATE_CHREMARKS;
                dlgAlert.setMessage(strCHDetails);
                AlertDialog dialog = dlgAlert.create();
                dialog.show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(30);
                break;
            case R.id.buttonCHCoBorrower:
                strCHDetails = CreditCheckInfoCoBorrower.CreditHistory;
                strCHDetails = strCHDetails.replace(";", "\n");
                CreateAlertDialog(true, true);
                dlgState = ufin_CREDITCHECK_DIALOG_STATE_CHREMARKS;
                dlgAlert.setMessage(strCHDetails);
                dialog = dlgAlert.create();
                dialog.show();
                textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(30);
                break;
            case R.id.buttonCHGuarantor:
                strCHDetails = CreditCheckInfoGuarantor.CreditHistory;
                strCHDetails = strCHDetails.replace(";", "\n");
                CreateAlertDialog(true, true);
                dlgState = ufin_CREDITCHECK_DIALOG_STATE_CHREMARKS;
                dlgAlert.setMessage(strCHDetails);
                dialog = dlgAlert.create();
                dialog.show();
                textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(30);
                break;
            case R.id.buttonCBMainBorrower:
                String strCBDetails = CreditCheckInfoMainBorrower.CBRemark;
                strCBDetails = strCBDetails.replace(";", "\n");
                CreateAlertDialog(true, true);
                dlgState = ufin_CREDITCHECK_DIALOG_STATE_CBREMARKS;
                dlgAlert.setMessage(strCBDetails);
                dialog = dlgAlert.create();
                dialog.show();
                textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(30);
                break;
            case R.id.buttonCBCoBorrower:
                strCBDetails = CreditCheckInfoCoBorrower.CBRemark;
                strCBDetails = strCBDetails.replace(";", "\n");
                CreateAlertDialog(true, true);
                dlgState = ufin_CREDITCHECK_DIALOG_STATE_CBREMARKS;
                dlgAlert.setMessage(strCBDetails);
                dialog = dlgAlert.create();
                dialog.show();
                textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(30);
                break;
            case R.id.buttonCBGuarantor:
                strCBDetails = CreditCheckInfoGuarantor.CBRemark;
                strCBDetails = strCBDetails.replace(";", "\n");
                CreateAlertDialog(true, true);
                dlgState = ufin_CREDITCHECK_DIALOG_STATE_CBREMARKS;
                dlgAlert.setMessage(strCBDetails);
                dialog = dlgAlert.create();
                dialog.show();
                textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(30);
                break;
            case R.id.buttonScanMainBorrower:
                selectedType = 1;
                Intent QRScanintent = new Intent("com.google.zxing.client.android.SCAN");
                QRScanintent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
                startActivityIntent.launch(QRScanintent);
                break;
//            case R.id.checkBoxCoBorrower:
//                CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxCoBorrower);
//                CoBorrowerSelected = cb.isChecked();
//
//                EditText et = (EditText) rootView.findViewById(R.id.editTextAadhaarNumberCoBorrower);
//                et.setEnabled(CoBorrowerSelected);
//                et = (EditText) rootView.findViewById(R.id.editTextFnameCoBorrower);
//                et.setEnabled(CoBorrowerSelected);
//                et = (EditText) rootView.findViewById(R.id.editTextMnameCoBorrower);
//                et.setEnabled(CoBorrowerSelected);
//                et = (EditText) rootView.findViewById(R.id.editTextLnameCoBorrower);
//                et.setEnabled(CoBorrowerSelected);
//                et = (EditText) rootView.findViewById(R.id.editTextGenderCoBorrower);
//                et.setEnabled(CoBorrowerSelected);
//                et = (EditText) rootView.findViewById(R.id.editTextBirthdateCoBorrower);
//                et.setEnabled(CoBorrowerSelected);
//                et = (EditText) rootView.findViewById(R.id.editTextAddressCoBorrower);
//                et.setEnabled(CoBorrowerSelected);
//                et = (EditText) rootView.findViewById(R.id.editTextCityCoBorrower);
//                et.setEnabled(CoBorrowerSelected);
//                et = (EditText) rootView.findViewById(R.id.editTextPincodeCoBorrower);
//                et.setEnabled(CoBorrowerSelected);
//                et = (EditText) rootView.findViewById(R.id.editTextMobileCoBorrower);
//                et.setEnabled(CoBorrowerSelected);
//                et = (EditText) rootView.findViewById(R.id.editTextOtherKYCIDCoBorrower);
//                et.setEnabled(CoBorrowerSelected);
//
//                Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerStateCoBorrower);
//                sp.setEnabled(CoBorrowerSelected);
//                sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDCoBorrower);
//                sp.setEnabled(CoBorrowerSelected);
//                sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationCoBorrower);
//                sp.setEnabled(CoBorrowerSelected);
//                sp = (Spinner) rootView.findViewById(R.id.spinnerRelationCoBorrower);
//                sp.setEnabled(CoBorrowerSelected);
//
//                Button bt = (Button) rootView.findViewById(R.id.buttonScanCoBorrower);
//                bt.setEnabled(CoBorrowerSelected);
//
//                cb = (CheckBox) rootView.findViewById(R.id.checkBoxFamilyMemberCoBorrower);
//                cb.setEnabled(CoBorrowerSelected);
//                break;
            case R.id.buttonScanCoBorrower:
                selectedType = 2;
                QRScanintent = new Intent("com.google.zxing.client.android.SCAN");
                QRScanintent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
                startActivityIntent.launch(QRScanintent);
                break;
            case R.id.buttonScanGuarantor:
                selectedType = 3;
                QRScanintent = new Intent("com.google.zxing.client.android.SCAN");
                QRScanintent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
                startActivityIntent.launch(QRScanintent);
                break;
            case R.id.buttonCloseCreditCheck:
                mainActivity.InitDesktop();
                break;
            case R.id.buttonCreditCheck:
                ReadCreditCheckInfo();
                if(ValidateInfo()) {
                    Calendar cal = Calendar.getInstance();
                    long lTimeStamp = cal.getTimeInMillis();
                    Date date = cal.getTime();
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
                    String strTimeStamp = sdf.format(date);// all done

                    CreditCheckInfoMainBorrower.iFamilyMember = 1;
                    CreditCheckInfoMainBorrower.MainBorrowerAdhaarNo = CreditCheckInfoMainBorrower.AdhaarNo;
                    CreditCheckInfoMainBorrower.CreditCheckProgress = "Submitted";
                    CreditCheckInfoMainBorrower.CreditHistory = "";
                    CreditCheckInfoMainBorrower.CBStatus = "";
                    CreditCheckInfoMainBorrower.CBEnquiryDate = strTimeStamp;
                    SQLite_SaveCreditCheckInfo(CreditCheckInfoMainBorrower);

                    if(CoBorrowerSelected) {
                        CreditCheckInfoCoBorrower.MainBorrowerAdhaarNo = CreditCheckInfoMainBorrower.AdhaarNo;
                        CreditCheckInfoCoBorrower.CreditCheckProgress = "Submitted";
                        CreditCheckInfoCoBorrower.CreditHistory = "";
                        CreditCheckInfoCoBorrower.CBStatus = "";
                        CreditCheckInfoCoBorrower.CBEnquiryDate = strTimeStamp;
                        SQLite_SaveCreditCheckInfo(CreditCheckInfoCoBorrower);
                    }

                    CreditCheckInfoGuarantor.MainBorrowerAdhaarNo = CreditCheckInfoMainBorrower.AdhaarNo;
                    CreditCheckInfoGuarantor.CreditCheckProgress = "Submitted";
                    CreditCheckInfoGuarantor.CreditHistory = "";
                    CreditCheckInfoGuarantor.CBStatus = "";
                    CreditCheckInfoGuarantor.CBEnquiryDate = strTimeStamp;
                    SQLite_SaveCreditCheckInfo(CreditCheckInfoGuarantor);

                    mainActivity.InitDesktop();
                } else if(CreditCheckInfoProblems.size() > 0) {
                        String strMsg = "";
                        for(int i=0; i<CreditCheckInfoProblems.size(); i++) {
                            strMsg = strMsg + CreditCheckInfoProblems.get(i) + "\n";
                        }
                        CreateAlertDialog(true, true);
                        dlgState = ufin_CREDITCHECK_DIALOG_STATE_VALIDATE;
                        dlgAlert.setMessage(strMsg);
                        dialog = dlgAlert.create();
                        dialog.show();
                        textView = (TextView) dialog.findViewById(android.R.id.message);
                        textView.setTextSize(30);
                }
                break;
//            case R.id.buttonCreditReview:
//                //Review Necessary Compliance
//                break;
            case R.id.buttonCreditCheckOK:
                //Get Main Borrower Promotion list record from server
                long lAadhaarcardNumber = Long.parseLong(CreditCheckInfoMainBorrower.AdhaarNo);
                String strSearchRationcard = CreditCheckInfoMainBorrower.RationCardNo;
                GetMainBorrowerPromotionListData(lAadhaarcardNumber, strSearchRationcard);

//                mainActivity.InitDesktop();
                String strMsg =  "AadhaarNumber:" + CreditCheckInfoMainBorrower.AdhaarNo;
                strMsg = strMsg + "\nListType: " + CreditCheckInfoMainBorrower.ListType;
                strMsg = strMsg + "\nSyncup with server to get Promotion record";
                CreateAlertDialog(true, true);
                dlgState = ufin_CREDITCHECK_DIALOG_STATE_CREDITCHECK_OK;
                dlgAlert.setMessage(strMsg);
                dialog = dlgAlert.create();
                dialog.show();
                textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(30);
                break;
            case R.id.imageButtonPhotoDocMainBorrower:
                int iCount = 1;
                String strEnquiryDate = CreditCheckInfoMainBorrower.CBEnquiryDate.replace("-", "");
                strEnquiryDate = strEnquiryDate.replace(":", "");
                strEnquiryDate = strEnquiryDate.replace(" ", "");
                strFilename =   String.valueOf(mainActivity.OrgID) + "-" +
                        strEnquiryDate + "-" +
                        CreditCheckInfoMainBorrower.AdhaarNo + "_" + String.valueOf(iCount);
                File outFile = new File(strFilepath + strFilename + ".jpg");
                while(outFile.exists()) {
                    iCount++;
                    strFilename = strFilename.substring(0, strFilename.lastIndexOf("_"));
                    strFilename = strFilename + "_" + String.valueOf(iCount);
                    outFile = new File(strFilepath + strFilename + ".jpg");
                }
                Uri outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", outFile);
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outuri);
                IDRequest = ID_REQUEST_PHOTO_DocMainBorrower;
                startActivityCameraIntent.launch(cameraIntent);
                break;
            case R.id.imageButtonPhotoDocCoBorrower:
                iCount = 1;
                strEnquiryDate = CreditCheckInfoCoBorrower.CBEnquiryDate.replace("-", "");
                strEnquiryDate = strEnquiryDate.replace(":", "");
                strEnquiryDate = strEnquiryDate.replace(" ", "");
                strFilename =   String.valueOf(mainActivity.OrgID) + "-" +
                        strEnquiryDate + "-" +
                        CreditCheckInfoCoBorrower.AdhaarNo + "_" + String.valueOf(iCount);
                outFile = new File(strFilepath + strFilename + ".jpg");
                while(outFile.exists()) {
                    iCount++;
                    strFilename = strFilename.substring(0, strFilename.lastIndexOf("_"));
                    strFilename = strFilename + "_" + String.valueOf(iCount);
                    outFile = new File(strFilepath + strFilename + ".jpg");
                }
                outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", outFile);
                cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outuri);
                IDRequest = ID_REQUEST_PHOTO_DocCoBorrower;
                startActivityCameraIntent.launch(cameraIntent);
                break;
            case R.id.imageButtonPhotoDocGuarantor:
                iCount = 1;
                strEnquiryDate = CreditCheckInfoGuarantor.CBEnquiryDate.replace("-", "");
                strEnquiryDate = strEnquiryDate.replace(":", "");
                strEnquiryDate = strEnquiryDate.replace(" ", "");
                strFilename =   String.valueOf(mainActivity.OrgID) + "-" +
                        strEnquiryDate + "-" +
                        CreditCheckInfoGuarantor.AdhaarNo + "_" + String.valueOf(iCount);
                outFile = new File(strFilepath + strFilename + ".jpg");
                while(outFile.exists()) {
                    iCount++;
                    strFilename = strFilename.substring(0, strFilename.lastIndexOf("_"));
                    strFilename = strFilename + "_" + String.valueOf(iCount);
                    outFile = new File(strFilepath + strFilename + ".jpg");
                }
                outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", outFile);
                cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outuri);
                IDRequest = ID_REQUEST_PHOTO_DocGuarantor;
                startActivityCameraIntent.launch(cameraIntent);
                break;
            case R.id.buttonCBReCheckMainBorrower:
                UploadCBReCheckDocs(CreditCheckInfoMainBorrower.AdhaarNo);
                Button bt = (Button) rootView.findViewById(R.id.buttonCBReCheckMainBorrower);
                bt.setEnabled(false);
                break;
            case R.id.buttonCBReCheckCoBorrower:
                UploadCBReCheckDocs(CreditCheckInfoCoBorrower.AdhaarNo);
                bt = (Button) rootView.findViewById(R.id.buttonCBReCheckCoBorrower);
                bt.setEnabled(false);
                break;
            case R.id.buttonCBReCheckGuarantor:
                UploadCBReCheckDocs(CreditCheckInfoGuarantor.AdhaarNo);
                bt = (Button) rootView.findViewById(R.id.buttonCBReCheckGuarantor);
                bt.setEnabled(false);
                break;
        }
    }

    void UploadCBReCheckDocs(String strAadhaarNo) {
        File folder = new File(mainActivity.baseDir + "/cbupload");
        if (!folder.exists())
            folder.mkdir();
        File dataFolder = new File(mainActivity.baseDir + "/data/");
        File[] files = dataFolder.listFiles();
        int iCount = 0;
        for (int i = 0; i < files.length; i++) {
            if(files[i].getName().contains(strAadhaarNo)) {
                iCount++;
            }
        }
        if(iCount == 0)
            return;
        int iUploadCount = 0;
        for (int i = 0; i < files.length; i++) {
            String strName = files[i].getName();
            if (strName.contains(strAadhaarNo)) {
                String strNewname = strName.substring(0, strName.lastIndexOf(".")) + "of" + String.valueOf(iCount) + ".jpg";
                File uploadFile = new File(mainActivity.baseDir + "/cbupload/" + strNewname);
                if (!uploadFile.exists()) {
                    try {
                        uploadFile.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                FileOutputStream outData = null;
                try {
                    outData = new FileOutputStream(uploadFile, false);
                    File dataFile = new File(mainActivity.baseDir + "/data/" + strName);
                    FileInputStream inData = new FileInputStream(dataFile);
                    byte[] buffer = new byte[4096];
                    int bytesRead = -1;
                    while ((bytesRead = inData.read(buffer)) != -1) {
                        outData.write(buffer, 0, bytesRead);
                    }
                    inData.close();
                    outData.flush();
                    outData.close();
                    mainActivity.SyncCBDocUpload.add(strNewname);
                    iUploadCount++;
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                File dataFile = new File(mainActivity.baseDir + "/data/" + strNewname);
                files[i].renameTo(dataFile);
            }
        }
        if(iUploadCount > 0) {
            CreateAlertDialog(true, false);
            dlgState = ufin_CREDITCHECK_DIALOG_STATE_CBRECHECK;
            String strMsg = strAadhaarNo + ": " + String.valueOf(iUploadCount) + " Document Photos Submitted for Credit Bureau Recheck.";
            dlgAlert.setMessage(strMsg);
            AlertDialog dialog = dlgAlert.create();
            dialog.show();
            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
            textView.setTextSize(30);
        }
    }
    ActivityResultLauncher<Intent> startActivityCameraIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Add same code that you want to add in onActivityResult method
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        File dataFile = new File(strFilepath + strFilename + ".jpg");
                        if(!dataFile.exists())
                            return;
                        if(dataFile.length() >= MAX_PHOTO_FILESIZE) {
                            dataFile.delete();
                            ShowdlgAlertonActivityResult("Incorrect Image file size, Adjust Camera Setting and Try Again!");
                            return;
                        }
                        switch (IDRequest) {
                            case ID_REQUEST_PHOTO_DocMainBorrower:
                                Button bt = (Button) rootView.findViewById(R.id.buttonCBReCheckMainBorrower);
                                bt.setEnabled(true);
                                break;
                            case ID_REQUEST_PHOTO_DocCoBorrower:
                                DisableCreditCheckInfoCoBorrower();
                                bt = (Button) rootView.findViewById(R.id.buttonCBReCheckCoBorrower);
                                bt.setEnabled(true);
                                break;
                            case ID_REQUEST_PHOTO_DocGuarantor:
                                DisableCreditCheckInfoGuarantor();
                                bt = (Button) rootView.findViewById(R.id.buttonCBReCheckGuarantor);
                                bt.setEnabled(true);
                                break;
                        }
                        IDRequest = -1;
                        Button bt = (Button) rootView.findViewById(R.id.buttonCreditCheck);
                        bt.setEnabled(false);
                    }
                }
            });

    public void GetMainBorrowerPromotionListData(long lAadhaarcardNumber, String strRationcard) {
        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try {
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
            String strTimeStamp = sdf.format(date);// all done

            ContentValues values1 = new ContentValues();

            values1.put(ufinDbContract.SearchMemberData.COLUMN_NAME_AadhaarcardNumber, lAadhaarcardNumber);
            values1.put(ufinDbContract.SearchMemberData.COLUMN_NAME_RationcardNumber, strRationcard);
            values1.put(ufinDbContract.SearchMemberData.COLUMN_NAME_SearchStatus, "Search");

            String whereClause = ufinDbContract.SearchMemberData.COLUMN_NAME_AadhaarcardNumber + "=" + "'" + String.valueOf(lAadhaarcardNumber) + "'";
            long lTemp1 = db.update(ufinDbContract.SearchMemberData.TABLE_NAME, values1, whereClause, null);
            if (lTemp1 <= 0) {
                lTemp1 = db.insert(ufinDbContract.SearchMemberData.TABLE_NAME, null, values1);
            }
            if (lTemp1 > 0) {
                JSONObject postDataParams = new JSONObject();
                try {
                    postDataParams.put("AdhaarNo", lAadhaarcardNumber);
                    if(strRationcard.isEmpty())
                        strRationcard = "NA";
                    postDataParams.put("RationCardNo", strRationcard);
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

                ContentValues values2 = new ContentValues();
                values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_SyncID, strTimeStamp);
                values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_DbTableName, ufinDbContract.SearchMemberData.TABLE_NAME);
                values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_updatedRecordID, lAadhaarcardNumber);
                values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsNewRecord, "True");
                values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsReady, "True");
                values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_PostDataParams, strDataParamsQuery);
                lTemp1 = db.insert(ufinDbContract.RecordsToSync.TABLE_NAME, null, values2);
                if (lTemp1 > 0) {
                    MainActivity.clsRecordsToSync newRec = new MainActivity.clsRecordsToSync();
                    newRec.SyncID = strTimeStamp;
                    newRec.recordID = lAadhaarcardNumber;
                    newRec.recordInserted = true;
                    newRec.recordReady = true;
                    newRec.PostParams = strDataParamsQuery;
                    mainActivity.SyncPromotionListData.add(newRec);
                }
            }
        } finally {
            db.close();
        }
    }

    void SQLite_SaveCreditCheckInfo(clsCreditCheckInfo checkInfo) {
        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
//                            Role:M   => M - Main Member, C - CoBorrower, G-Guarantor
//                            MainMemberAdhaarNo:MainBorrower AdhaarNo
//                            AdhaarNo:NA => NA / Actual number
//                            RationCardNo:NA => NA / Actual number
//                            PAN_No:JMGPG1089H => NA / Actual number
//                            VoterID:17301055559993 => NA / Actual number
//                            FName:NIRMALA
//                            MName:G
//                            LName:NA
//                            Gender:FeMale => Female / Male
//                            DateOfBirth:01-01-1967
//                            Address:8 1 MUNISHWARA STREET HOUSR MAIN ROAD BANGALORE 560030
//                            City:BANGALORE
//                            State:KA
//                            Pincode:560030
//                            ContactNo:6568627692
//                            LSO_ID:13
//                            Org_ID:1

            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Role, checkInfo.Role);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_AdhaarNo, checkInfo.AdhaarNo);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_RationCardNo, checkInfo.RationCardNo);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_PAN_No, checkInfo.PAN_No);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_VoterID, checkInfo.VoterID);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_FName, checkInfo.FName.trim());
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_MName, checkInfo.MName.trim());
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_LName, checkInfo.LName.trim());
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Gender, checkInfo.Gender);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_DateOfBirth, checkInfo.DateOfBirth);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Address, checkInfo.Address);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_City, checkInfo.City);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_State, checkInfo.State);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Pincode, checkInfo.Pincode);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_ContactNo, checkInfo.ContactNo);

            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_CreditCheckProgress, checkInfo.CreditCheckProgress);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_CreditStatus, checkInfo.CreditHistory);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_MemberType, checkInfo.MemberType);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_ListType, checkInfo.ListType);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Remarks, checkInfo.CHRemarks);

            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBEnquiryDate, checkInfo.CBEnquiryDate);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBScore, checkInfo.CBScore);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBStatus, checkInfo.CBStatus);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBRemark, checkInfo.CBRemark);

//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Age, checkInfo.Age);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_MainBorrowerRelationID, checkInfo.MainBorrowerRelationID);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_IsFamilyMember, checkInfo.iFamilyMember);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_MainBorrowerAdhaarNo, checkInfo.MainBorrowerAdhaarNo);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_ProfessionID, checkInfo.ProfessionID);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_OwnHouse, checkInfo.OwnHouse);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_MaritalStatusID, checkInfo.MaritalStatusID);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_MonthlyIncome, checkInfo.MonthlyIncome);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_ClusterID, checkInfo.ClusterID);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_CommunityID, checkInfo.CommunityID);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Cluster, checkInfo.Cluster);
            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Community, checkInfo.Community);

            String whereClause = ufinDbContract.CreditValidityCheck.COLUMN_NAME_AdhaarNo + "=" + "'" + checkInfo.AdhaarNo + "'";
            long lTemp = 0;
            lTemp = db.update(ufinDbContract.CreditValidityCheck.TABLE_NAME, values, whereClause, null);
            if (lTemp <= 0) {
                lTemp = db.insert(ufinDbContract.CreditValidityCheck.TABLE_NAME, null, values);
            }

            Calendar cal = Calendar.getInstance();
            long lTimeStamp = cal.getTimeInMillis();
            Date date = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
            String strTimeStamp = sdf.format(date);// all done

            JSONObject postDataParams = new JSONObject();
            try {
                postDataParams.put("EnquiryDate", checkInfo.CBEnquiryDate);
                postDataParams.put("Role", checkInfo.Role);
                postDataParams.put("MainMemberAdhaarNo", checkInfo.MainBorrowerAdhaarNo);
                postDataParams.put("AdhaarNo", checkInfo.AdhaarNo);
                postDataParams.put("RationCardNo", checkInfo.RationCardNo);
                postDataParams.put("PAN_No", checkInfo.PAN_No);
                postDataParams.put("VoterID", checkInfo.VoterID);
                postDataParams.put("FName", checkInfo.FName.trim());
                postDataParams.put("MName", checkInfo.MName.trim());
                postDataParams.put("LName", checkInfo.LName.trim());
                postDataParams.put("Gender", checkInfo.Gender);
                postDataParams.put("DateOfBirth", checkInfo.DateOfBirth);
                postDataParams.put("Address", checkInfo.Address);
                postDataParams.put("City", checkInfo.City);
                postDataParams.put("State", checkInfo.State);
                postDataParams.put("Pincode", checkInfo.Pincode);
                postDataParams.put("ContactNo", checkInfo.ContactNo);
                postDataParams.put("MonthlyIncome", checkInfo.MonthlyIncome);
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

            long lTemp1 = 0;
            ContentValues values2 = new ContentValues();

            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_SyncID, strTimeStamp);
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_DbTableName, ufinDbContract.CreditValidityCheck.TABLE_NAME);
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_updatedRecordID, Long.parseLong(checkInfo.AdhaarNo));
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsNewRecord, "True");
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsReady, "True");
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_PostDataParams, strDataParamsQuery);

            lTemp1 = db.insert(ufinDbContract.RecordsToSync.TABLE_NAME, null, values2);
            if (lTemp1 > 0) {
                MainActivity.clsRecordsToSync newRec = new MainActivity.clsRecordsToSync();
                newRec.SyncID = strTimeStamp;
                newRec.recordID = Long.parseLong(checkInfo.AdhaarNo);
                newRec.recordInserted = true;
                newRec.recordReady = true;
                newRec.PostParams = strDataParamsQuery;
                mainActivity.SyncCheckCreditValidity.add(newRec);
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
    }

    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Add same code that you want to add in onActivityResult method
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String contents = data.getStringExtra("SCAN_RESULT");
                        System.out.println(contents);
//<?xml version="1.0" encoding="UTF-8"?>
//<PrintLetterBarcodeData uid="514448883180" name="Sarika Tanaji Jadhav" gender="F" yob="1982" co="W/O: Tanaji Jadhav" house="DUMBA WADI" street="URAN - PANVEL ROAD JASAI" vtc="Jasai" po="Jasai" dist="Raigarh" subdist="Uran" state="Maharashtra" pc="410206" dob="01/06/1982"/>
                        contents = contents.replace("</?", "<?");
                        ProcessScannedData(contents);
                        RefreshCreditCheckInfo();
                    } else if(result.getResultCode() == RESULT_CANCELED){
                        //handle cancel
                        ShowdlgAlertonActivityResult("SCAN REQUEST CANCELED!");
                        RefreshCreditCheckInfo();
                    }
                }
            });

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
                        case ufin_CREDITCHECK_DIALOG_STATE_NONE:
                            break;
                        case ufin_CREDITCHECK_DIALOG_STATE_VALIDATE:
                            break;
                        case ufin_CREDITCHECK_DIALOG_STATE_CHREMARKS:
                            break;
                        case ufin_CREDITCHECK_DIALOG_STATE_CBREMARKS:
                            break;
                        case ufin_CREDITCHECK_DIALOG_STATE_CBRECHECK:
                        case ufin_CREDITCHECK_DIALOG_STATE_CREDITCHECK_OK:
                            mainActivity.InitDesktop();
                            break;
                    }
                    dlgState = ufin_CREDITCHECK_DIALOG_STATE_NONE;
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
                        case ufin_CREDITCHECK_DIALOG_STATE_NONE:
                            break;
                        case ufin_CREDITCHECK_DIALOG_STATE_VALIDATE:
                            break;
                        case ufin_CREDITCHECK_DIALOG_STATE_CHREMARKS:
                            break;
                        case ufin_CREDITCHECK_DIALOG_STATE_CBREMARKS:
                            break;
                        case ufin_CREDITCHECK_DIALOG_STATE_CBRECHECK:
                        case ufin_CREDITCHECK_DIALOG_STATE_CREDITCHECK_OK:
                            mainActivity.InitDesktop();
                            break;
                    }
                    dlgState = ufin_CREDITCHECK_DIALOG_STATE_NONE;
                }
            });
        }
//        dlgAlert.setCancelable(true);
    }

//    void ReadCreditCheckInfo(boolean CoBorrowerSelected) {
    void ReadCreditCheckInfo() {
        if(CreditCheckInfoMainBorrower == null) {
            CreditCheckInfoMainBorrower = new clsCreditCheckInfo();
            CreditCheckInfoMainBorrower.InitCreditCheckInfo();
        }
        if(CoBorrowerSelected) {
            if (CreditCheckInfoCoBorrower == null) {
                CreditCheckInfoCoBorrower = new clsCreditCheckInfo();
                CreditCheckInfoCoBorrower.InitCreditCheckInfo();
            }
        }
        if(CreditCheckInfoGuarantor == null) {
            CreditCheckInfoGuarantor = new clsCreditCheckInfo();
            CreditCheckInfoGuarantor.InitCreditCheckInfo();
        }
        CreditCheckInfoMainBorrower.Role = "M";
        EditText et = (EditText) rootView.findViewById(R.id.editTextAadhaarNumberMainBorrower);
        CreditCheckInfoMainBorrower.AdhaarNo = et.getText().toString();
        CreditCheckInfoMainBorrower.MainBorrowerAdhaarNo = CreditCheckInfoMainBorrower.AdhaarNo;

        CreditCheckInfoMainBorrower.RationCardNo = "NA";
        CreditCheckInfoMainBorrower.PAN_No = "NA";
        CreditCheckInfoMainBorrower.VoterID = "NA";
        et = (EditText) rootView.findViewById(R.id.editTextOtherKYCIDMainBorrower);
        String strTemp = et.getText().toString();
        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDMainBorrower);
        String strTemp2 = sp.getSelectedItem().toString();
        if(strTemp2.isEmpty()) {
        } else if(strTemp2.equalsIgnoreCase("Ration Card")) {
            CreditCheckInfoMainBorrower.RationCardNo = strTemp;
        } else if(strTemp2.equalsIgnoreCase("PAN Card")) {
            CreditCheckInfoMainBorrower.PAN_No = strTemp;
        } else if(strTemp2.equalsIgnoreCase("Voter ID")) {
            CreditCheckInfoMainBorrower.VoterID = strTemp;
        }

        et = (EditText) rootView.findViewById(R.id.editTextFnameMainBorrower);
        CreditCheckInfoMainBorrower.FName = et.getText().toString();
        CreditCheckInfoMainBorrower.FName = CreditCheckInfoMainBorrower.FName.trim();
        et = (EditText) rootView.findViewById(R.id.editTextMnameMainBorrower);
        CreditCheckInfoMainBorrower.MName = et.getText().toString();
        CreditCheckInfoMainBorrower.MName = CreditCheckInfoMainBorrower.MName.trim();
        et = (EditText) rootView.findViewById(R.id.editTextLnameMainBorrower);
        CreditCheckInfoMainBorrower.LName = et.getText().toString();
        CreditCheckInfoMainBorrower.LName = CreditCheckInfoMainBorrower.LName.trim();

        et = (EditText) rootView.findViewById(R.id.editTextGenderMainBorrower);
        CreditCheckInfoMainBorrower.Gender = et.getText().toString();
//        et = (EditText) rootView.findViewById(R.id.editTextBirthdateMainBorrower);
//        CreditCheckInfoMainBorrower.DateOfBirth = et.getText().toString();
        et = (EditText) rootView.findViewById(R.id.editTextAddressMainBorrower);
        CreditCheckInfoMainBorrower.Address = et.getText().toString();
        et = (EditText) rootView.findViewById(R.id.editTextCityMainBorrower);
        CreditCheckInfoMainBorrower.City = et.getText().toString();
//        et = (EditText) rootView.findViewById(R.id.editTextStateMainBorrower);
//        CreditCheckInfoMainBorrower.State = et.getText().toString();
        et = (EditText) rootView.findViewById(R.id.editTextPincodeMainBorrower);
        CreditCheckInfoMainBorrower.Pincode = et.getText().toString();
        et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncomeMainBorrower);
        try {
            CreditCheckInfoMainBorrower.MonthlyIncome = Integer.parseInt(et.getText().toString());
        } catch (Exception e) {
            CreditCheckInfoMainBorrower.MonthlyIncome = 0;
        }
        et = (EditText) rootView.findViewById(R.id.editTextMobileMainBorrower);
        CreditCheckInfoMainBorrower.ContactNo = et.getText().toString();

        CreditCheckInfoMainBorrower.CreditHistory = "";
        CreditCheckInfoMainBorrower.MemberType = "";
        CreditCheckInfoMainBorrower.ListType = "";
        CreditCheckInfoMainBorrower.CHRemarks = "";
        CreditCheckInfoMainBorrower.CBEnquiryDate = "";
        CreditCheckInfoMainBorrower.CBScore = "";
        CreditCheckInfoMainBorrower.CBStatus = "";
        CreditCheckInfoMainBorrower.CBRemark = "";

        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationMainBorrower);
        int position = sp.getSelectedItemPosition();
        CreditCheckInfoMainBorrower.ProfessionID = BusinessIDMaster.get(position);
        sp = (Spinner) rootView.findViewById(R.id.spinnerStateMainBorrower);
        position = sp.getSelectedItemPosition();
        CreditCheckInfoMainBorrower.State = StateCodeMaster.get(position);
//        CreditCheckInfoMainBorrower.OwnHouse;
        sp = (Spinner) rootView.findViewById(R.id.spinnerMaritalStatus);
        position = sp.getSelectedItemPosition();
        CreditCheckInfoMainBorrower.MaritalStatusID = MaritalStatusIDMaster.get(position);
        CreditCheckInfoMainBorrower.MainBorrowerRelationID = RelationIDMainMember;
//        CreditCheckInfoMainBorrower.ClusterID;
//        CreditCheckInfoMainBorrower.CommunityID;

        if(CoBorrowerSelected) {
            CreditCheckInfoCoBorrower.Role = "C";
            et = (EditText) rootView.findViewById(R.id.editTextAadhaarNumberCoBorrower);
            CreditCheckInfoCoBorrower.AdhaarNo = et.getText().toString();
            CreditCheckInfoCoBorrower.MainBorrowerAdhaarNo = CreditCheckInfoMainBorrower.AdhaarNo;

            CreditCheckInfoCoBorrower.RationCardNo = "NA";
            CreditCheckInfoCoBorrower.PAN_No = "NA";
            CreditCheckInfoCoBorrower.VoterID = "NA";
            et = (EditText) rootView.findViewById(R.id.editTextOtherKYCIDCoBorrower);
            strTemp = et.getText().toString();
            sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDCoBorrower);
            strTemp2 = sp.getSelectedItem().toString();
            if(strTemp2.isEmpty()) {
            } else if(strTemp2.equalsIgnoreCase("Ration Card")) {
                CreditCheckInfoCoBorrower.RationCardNo = strTemp;
            } else if(strTemp2.equalsIgnoreCase("PAN Card")) {
                CreditCheckInfoCoBorrower.PAN_No = strTemp;
            } else if(strTemp2.equalsIgnoreCase("Voter ID")) {
                CreditCheckInfoCoBorrower.VoterID = strTemp;
            }

            et = (EditText) rootView.findViewById(R.id.editTextFnameCoBorrower);
            CreditCheckInfoCoBorrower.FName = et.getText().toString();
            CreditCheckInfoCoBorrower.FName = CreditCheckInfoCoBorrower.FName.trim();
            et = (EditText) rootView.findViewById(R.id.editTextMnameCoBorrower);
            CreditCheckInfoCoBorrower.MName = et.getText().toString();
            CreditCheckInfoCoBorrower.MName = CreditCheckInfoCoBorrower.MName.trim();
            et = (EditText) rootView.findViewById(R.id.editTextLnameCoBorrower);
            CreditCheckInfoCoBorrower.LName = et.getText().toString();
            CreditCheckInfoCoBorrower.LName = CreditCheckInfoCoBorrower.LName.trim();

            et = (EditText) rootView.findViewById(R.id.editTextGenderCoBorrower);
            CreditCheckInfoCoBorrower.Gender = et.getText().toString();
//            et = (EditText) rootView.findViewById(R.id.editTextBirthdateCoBorrower);
//            CreditCheckInfoCoBorrower.DateOfBirth = et.getText().toString();
            et = (EditText) rootView.findViewById(R.id.editTextAddressCoBorrower);
            CreditCheckInfoCoBorrower.Address = et.getText().toString();
            et = (EditText) rootView.findViewById(R.id.editTextCityCoBorrower);
            CreditCheckInfoCoBorrower.City = et.getText().toString();
//            et = (EditText) rootView.findViewById(R.id.editTextStateCoBorrower);
//            CreditCheckInfoCoBorrower.State = et.getText().toString();
            et = (EditText) rootView.findViewById(R.id.editTextPincodeCoBorrower);
            CreditCheckInfoCoBorrower.Pincode = et.getText().toString();
            et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncomeCoBorrower);
            try {
                CreditCheckInfoCoBorrower.MonthlyIncome = Integer.parseInt(et.getText().toString());
            } catch (Exception e) {
                CreditCheckInfoCoBorrower.MonthlyIncome = 0;
            }
            et = (EditText) rootView.findViewById(R.id.editTextMobileCoBorrower);
            CreditCheckInfoCoBorrower.ContactNo = et.getText().toString();

            CreditCheckInfoCoBorrower.CreditHistory = "";
            CreditCheckInfoCoBorrower.MemberType = "";
            CreditCheckInfoCoBorrower.ListType = "";
            CreditCheckInfoCoBorrower.CHRemarks = "";
            CreditCheckInfoCoBorrower.CBEnquiryDate = "";
            CreditCheckInfoCoBorrower.CBScore = "";
            CreditCheckInfoCoBorrower.CBStatus = "";
            CreditCheckInfoCoBorrower.CBRemark = "";

            sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationCoBorrower);
            position = sp.getSelectedItemPosition();
            CreditCheckInfoCoBorrower.ProfessionID = BusinessIDMaster.get(position);
            sp = (Spinner) rootView.findViewById(R.id.spinnerStateCoBorrower);
            position = sp.getSelectedItemPosition();
            CreditCheckInfoCoBorrower.State = StateCodeMaster.get(position);
            sp = (Spinner) rootView.findViewById(R.id.spinnerRelationCoBorrower);
            position = sp.getSelectedItemPosition();
            CreditCheckInfoCoBorrower.MainBorrowerRelationID = RelationIDMaster.get(position);
            CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxFamilyMemberCoBorrower);
            if(cb.isChecked()) {
                CreditCheckInfoCoBorrower.iFamilyMember = 1;
            } else {
                CreditCheckInfoCoBorrower.iFamilyMember = 0;
            }
        } else {

        }

        CreditCheckInfoGuarantor.Role = "G";
        et = (EditText) rootView.findViewById(R.id.editTextAadhaarNumberGuarantor);
        CreditCheckInfoGuarantor.AdhaarNo = et.getText().toString();
        CreditCheckInfoGuarantor.MainBorrowerAdhaarNo = CreditCheckInfoMainBorrower.AdhaarNo;

        CreditCheckInfoGuarantor.RationCardNo = "NA";
        CreditCheckInfoGuarantor.PAN_No = "NA";
        CreditCheckInfoGuarantor.VoterID = "NA";
        et = (EditText) rootView.findViewById(R.id.editTextOtherKYCIDGuarantor);
        strTemp = et.getText().toString();
        sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDGuarantor);
        strTemp2 = sp.getSelectedItem().toString();
        if(strTemp2.isEmpty()) {
        } else if(strTemp2.equalsIgnoreCase("Ration Card")) {
            CreditCheckInfoGuarantor.RationCardNo = strTemp;
        } else if(strTemp2.equalsIgnoreCase("PAN Card")) {
            CreditCheckInfoGuarantor.PAN_No = strTemp;
        } else if(strTemp2.equalsIgnoreCase("Voter ID")) {
            CreditCheckInfoGuarantor.VoterID = strTemp;
        }

        et = (EditText) rootView.findViewById(R.id.editTextFnameGuarantor);
        CreditCheckInfoGuarantor.FName = et.getText().toString();
        CreditCheckInfoGuarantor.FName = CreditCheckInfoGuarantor.FName.trim();
        et = (EditText) rootView.findViewById(R.id.editTextMnameGuarantor);
        CreditCheckInfoGuarantor.MName = et.getText().toString();
        CreditCheckInfoGuarantor.MName = CreditCheckInfoGuarantor.MName.trim();
        et = (EditText) rootView.findViewById(R.id.editTextLnameGuarantor);
        CreditCheckInfoGuarantor.LName = et.getText().toString();
        CreditCheckInfoGuarantor.LName = CreditCheckInfoGuarantor.LName.trim();

        et = (EditText) rootView.findViewById(R.id.editTextGenderGuarantor);
        CreditCheckInfoGuarantor.Gender = et.getText().toString();
//        et = (EditText) rootView.findViewById(R.id.editTextBirthdateGuarantor);
//        CreditCheckInfoGuarantor.DateOfBirth = et.getText().toString();
        et = (EditText) rootView.findViewById(R.id.editTextAddressGuarantor);
        CreditCheckInfoGuarantor.Address = et.getText().toString();
        et = (EditText) rootView.findViewById(R.id.editTextCityGuarantor);
        CreditCheckInfoGuarantor.City = et.getText().toString();
        et = (EditText) rootView.findViewById(R.id.editTextPincodeGuarantor);
        CreditCheckInfoGuarantor.Pincode = et.getText().toString();
        et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncomeGuarantor);
        try {
            CreditCheckInfoGuarantor.MonthlyIncome = Integer.parseInt(et.getText().toString());
        } catch (Exception e) {
            CreditCheckInfoGuarantor.MonthlyIncome = 0;
        }
        et = (EditText) rootView.findViewById(R.id.editTextMobileGuarantor);
        CreditCheckInfoGuarantor.ContactNo = et.getText().toString();

        CreditCheckInfoGuarantor.CreditHistory = "";
        CreditCheckInfoGuarantor.MemberType = "";
        CreditCheckInfoGuarantor.ListType = "";
        CreditCheckInfoGuarantor.CHRemarks = "";
        CreditCheckInfoGuarantor.CBEnquiryDate = "";
        CreditCheckInfoGuarantor.CBScore = "";
        CreditCheckInfoGuarantor.CBStatus = "";
        CreditCheckInfoGuarantor.CBRemark = "";

        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupationGuarantor);
        position = sp.getSelectedItemPosition();
        CreditCheckInfoGuarantor.ProfessionID = BusinessIDMaster.get(position);
        sp = (Spinner) rootView.findViewById(R.id.spinnerStateGuarantor);
        position = sp.getSelectedItemPosition();
        CreditCheckInfoGuarantor.State = StateCodeMaster.get(position);
        sp = (Spinner) rootView.findViewById(R.id.spinnerRelationGuarantor);
        position = sp.getSelectedItemPosition();
        CreditCheckInfoGuarantor.MainBorrowerRelationID = RelationIDMaster.get(position);
        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxFamilyMemberGuarantor);
        if(cb.isChecked()) {
            CreditCheckInfoGuarantor.iFamilyMember = 1;
        } else {
            CreditCheckInfoGuarantor.iFamilyMember = 0;
        }
    }

//    boolean ValidateInfo(boolean CoBorrowerSelected) {
    boolean ValidateInfo() {
        boolean Status = true;

        CreditCheckInfoProblems.clear();

//        // [0-9]{12} - match 12 numbers 0 to 9
        Pattern patternAdhaarNo = Pattern.compile("[0-9]{12}");
        // [A-Z]{5} - match five literals which can be A to Z
        // [0-9]{4} - followed by 4 numbers 0 to 9
        // [A-Z]{1} - followed by one literal which can A to Z
        Pattern patternPAN = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
//bmp 13-Feb-25
        Pattern pattern1VoterID = Pattern.compile("[A-Z]{3}[0-9]{7}");
        Pattern pattern2VoterID = Pattern.compile("[A-Z]{2}/[0-9]{4}/[0-9]{4}/[0-9]{6}");
//        Pattern pattern1VoterID = Pattern.compile("[0-9]{10}");
//        Pattern pattern2VoterID = Pattern.compile("[0-9]{14}");
//bmp 13-Feb-25

        Matcher matcher = patternAdhaarNo.matcher(CreditCheckInfoMainBorrower.AdhaarNo);
        // Check if pattern matches
        if(!matcher.matches()) {
            CreditCheckInfoProblems.add("MainBorrower: " + "AdhaarNo format Invalid.");
        }
        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDMainBorrower);
        String strTemp2 = sp.getSelectedItem().toString();
        if(strTemp2.isEmpty()) {
            CreditCheckInfoProblems.add("MainBorrower: " + "KYC Doc2 not selected.");
        } else if(strTemp2.equalsIgnoreCase("Ration Card")) {
            if(CreditCheckInfoMainBorrower.RationCardNo.isEmpty()) {
                CreditCheckInfoProblems.add("MainBorrower: " + "RationCardNo format Invalid.");
            }
        } else if(strTemp2.equalsIgnoreCase("PAN Card")) {
            matcher = patternPAN.matcher(CreditCheckInfoMainBorrower.PAN_No);
            // Check if pattern matches
            if(!matcher.matches()) {
                CreditCheckInfoProblems.add("MainBorrower: " + "PAN_No format Invalid.");
            }
        } else if(strTemp2.equalsIgnoreCase("Voter ID")) {
            matcher = pattern1VoterID.matcher(CreditCheckInfoMainBorrower.VoterID);
            // Check if pattern matches
            if(!matcher.matches()) {
                matcher = pattern2VoterID.matcher(CreditCheckInfoMainBorrower.VoterID);
                // Check if pattern matches
                if(!matcher.matches()) {
                    CreditCheckInfoProblems.add("MainBorrower: " + "VoterID format Invalid.");
                }
            }
        }
        if( (CreditCheckInfoMainBorrower.FName.isEmpty()) ||
                (CreditCheckInfoMainBorrower.MName.isEmpty() && CreditCheckInfoMainBorrower.LName.isEmpty()) ) {
            CreditCheckInfoProblems.add("MainBorrower: पूर्ण नाव भरणे बाकी");
        } else if(  (CreditCheckInfoMainBorrower.FName.equalsIgnoreCase("NA")) &&
                (CreditCheckInfoMainBorrower.MName.equalsIgnoreCase("NA")) &&
                (CreditCheckInfoMainBorrower.LName.equalsIgnoreCase("NA")) ) {
            CreditCheckInfoProblems.add("MainBorrower: पूर्ण नाव भरणे बाकी");
        }
        if(CreditCheckInfoMainBorrower.Gender.isEmpty()) {
            CreditCheckInfoProblems.add("MainBorrower: Gender भरणे बाकी");
        }
        if(CreditCheckInfoMainBorrower.DateOfBirth.isEmpty()) {
            CreditCheckInfoProblems.add("MainBorrower: DateOfBirth भरणे बाकी");
        }
        if(CreditCheckInfoMainBorrower.Address.isEmpty()) {
            CreditCheckInfoProblems.add("MainBorrower: Address भरणे बाकी");
        }
        if(CreditCheckInfoMainBorrower.Pincode.isEmpty()) {
            CreditCheckInfoProblems.add("MainBorrower: Pincode भरणे बाकी");
        }
        if(CreditCheckInfoMainBorrower.City.isEmpty()) {
            CreditCheckInfoProblems.add("MainBorrower: City भरणे बाकी");
        }
        if(CreditCheckInfoMainBorrower.State.isEmpty()) {
            CreditCheckInfoProblems.add("MainBorrower: State भरणे बाकी");
        }
        if(CreditCheckInfoMainBorrower.ContactNo.isEmpty()) {
            CreditCheckInfoProblems.add("MainBorrower: ContactNo भरणे बाकी");
        } else if(CreditCheckInfoMainBorrower.ContactNo.length() < 10) {
            CreditCheckInfoProblems.add("MainBorrower: ContactNo format Invalid.");
        }
        if (CreditCheckInfoMainBorrower.ProfessionID == 0) {
            CreditCheckInfoProblems.add("MainBorrower: व्यवसाय भरणे बाकी.");
        }
        if(!ValidateProfession(CreditCheckInfoMainBorrower.ProfessionID)) {
            CreditCheckInfoProblems.add("MainBorrower: व्यवसाय अयोग्य आहे.");
        }
//bmp 15-May-25
        if (CreditCheckInfoMainBorrower.MonthlyIncome == 0) {
            CreditCheckInfoProblems.add("MainBorrower: मासिक उत्पन्न भरणे बाकी.");
        }
//bmp 15-May-25
        RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupHouse);
        if(rg.getCheckedRadioButtonId() == -1) {
            CreditCheckInfoProblems.add("MainBorrower: घराची स्थिती भरणे बाकी.");
        }
        if (CreditCheckInfoMainBorrower.MaritalStatusID == 0) {
            CreditCheckInfoProblems.add("MainBorrower: वैवाहिक स्थिती भरणे बाकी.");
        }
        if (CreditCheckInfoMainBorrower.ClusterID == 0) {
            CreditCheckInfoProblems.add("MainBorrower: विभाग भरणे बाकी.");
        }
        if (CreditCheckInfoMainBorrower.CommunityID == 0) {
            CreditCheckInfoProblems.add("MainBorrower: वस्ती भरणे बाकी.");
        }
//////////////////////////////
        if(CoBorrowerSelected) {
            matcher = patternAdhaarNo.matcher(CreditCheckInfoCoBorrower.AdhaarNo);
            // Check if pattern matches
            if (!matcher.matches()) {
                CreditCheckInfoProblems.add("CoBorrower: " + "AdhaarNo format Invalid.");
            }
            sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDCoBorrower);
            strTemp2 = sp.getSelectedItem().toString();
            if(strTemp2.isEmpty()) {
                CreditCheckInfoProblems.add("CoBorrower: " + "KYC Doc2 not selected.");
            } else if(strTemp2.equalsIgnoreCase("Ration Card")) {
                if(CreditCheckInfoCoBorrower.RationCardNo.isEmpty()) {
                    CreditCheckInfoProblems.add("CoBorrower: " + "RationCardNo format Invalid.");
                }
            } else if(strTemp2.equalsIgnoreCase("PAN Card")) {
                matcher = patternPAN.matcher(CreditCheckInfoCoBorrower.PAN_No);
                // Check if pattern matches
                if (!matcher.matches()) {
                    CreditCheckInfoProblems.add("CoBorrower: " + "PAN_No format Invalid.");
                }
            } else if(strTemp2.equalsIgnoreCase("Voter ID")) {
                matcher = pattern1VoterID.matcher(CreditCheckInfoCoBorrower.VoterID);
                // Check if pattern matches
                if (!matcher.matches()) {
                    matcher = pattern2VoterID.matcher(CreditCheckInfoCoBorrower.VoterID);
                    // Check if pattern matches
                    if (!matcher.matches()) {
                        CreditCheckInfoProblems.add("CoBorrower: " + "VoterID format Invalid.");
                    }
                }
            }
            if ((CreditCheckInfoCoBorrower.FName.isEmpty()) ||
                    (CreditCheckInfoCoBorrower.MName.isEmpty() && CreditCheckInfoCoBorrower.LName.isEmpty())) {
                CreditCheckInfoProblems.add("CoBorrower: पूर्ण नाव भरणे बाकी");
            } else if ((CreditCheckInfoCoBorrower.FName.equalsIgnoreCase("NA")) &&
                    (CreditCheckInfoCoBorrower.MName.equalsIgnoreCase("NA")) &&
                    (CreditCheckInfoCoBorrower.LName.equalsIgnoreCase("NA"))) {
                CreditCheckInfoProblems.add("CoBorrower: पूर्ण नाव भरणे बाकी");
            }
            if(CreditCheckInfoCoBorrower.Gender.isEmpty()) {
                CreditCheckInfoProblems.add("CoBorrower: Gender भरणे बाकी");
            }
            if(CreditCheckInfoCoBorrower.DateOfBirth.isEmpty()) {
                CreditCheckInfoProblems.add("CoBorrower: DateOfBirth भरणे बाकी");
            }
            if(CreditCheckInfoCoBorrower.Address.isEmpty()) {
                CreditCheckInfoProblems.add("CoBorrower: Address भरणे बाकी");
            }
            if(CreditCheckInfoCoBorrower.Pincode.isEmpty()) {
                CreditCheckInfoProblems.add("CoBorrower: Pincode भरणे बाकी");
            }
            if(CreditCheckInfoCoBorrower.City.isEmpty()) {
                CreditCheckInfoProblems.add("CoBorrower: City भरणे बाकी");
            }
            if(CreditCheckInfoCoBorrower.State.isEmpty()) {
                CreditCheckInfoProblems.add("CoBorrower: State भरणे बाकी");
            }
            if(CreditCheckInfoCoBorrower.ContactNo.isEmpty()) {
                CreditCheckInfoProblems.add("CoBorrower: ContactNo भरणे बाकी");
            } else if(CreditCheckInfoCoBorrower.ContactNo.length() < 10) {
                CreditCheckInfoProblems.add("CoBorrower: ContactNo format Invalid.");
            }
            if (CreditCheckInfoCoBorrower.MainBorrowerRelationID == 0) {
                CreditCheckInfoProblems.add("CoBorrower: कर्जदारासोबतचे नाते भरणे बाकी.");
            }
//bmp 20-Jan-25
            if(CreditCheckInfoCoBorrower.MainBorrowerRelationID == RelationIDSpouse) {
                if(     (CreditCheckInfoMainBorrower.MaritalStatusID != 1) &&
                        (CreditCheckInfoMainBorrower.MaritalStatusID != 7) ) {
                    CreditCheckInfoProblems.add("CoBorrower: कर्जदारासोबतचे नाते अयोग्य आहे.");
                }
            }
//bmp 20-Jan-25
            if (CreditCheckInfoCoBorrower.ProfessionID == 0) {
                CreditCheckInfoProblems.add("CoBorrower: व्यवसाय भरणे बाकी.");
            }
            if(!ValidateProfession(CreditCheckInfoCoBorrower.ProfessionID)) {
                CreditCheckInfoProblems.add("CoBorrower: व्यवसाय अयोग्य आहे.");
            }
//bmp 15-May-25
            if (CreditCheckInfoCoBorrower.MonthlyIncome == 0) {
                CreditCheckInfoProblems.add("CoBorrower: मासिक उत्पन्न भरणे बाकी.");
            }
//bmp 15-May-25
        }
//////////////////////////////
        matcher = patternAdhaarNo.matcher(CreditCheckInfoGuarantor.AdhaarNo);
        // Check if pattern matches
        if(!matcher.matches()) {
            CreditCheckInfoProblems.add("Guarantor: " + "AdhaarNo format Invalid.");
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerOtherIDGuarantor);
        strTemp2 = sp.getSelectedItem().toString();
        if(strTemp2.isEmpty()) {
            CreditCheckInfoProblems.add("Guarantor: " + "KYC Doc2 not selected.");
        } else if(strTemp2.equalsIgnoreCase("Ration Card")) {
            if(CreditCheckInfoGuarantor.RationCardNo.isEmpty()) {
                CreditCheckInfoProblems.add("Guarantor: " + "RationCardNo format Invalid.");
            }
        } else if(strTemp2.equalsIgnoreCase("PAN Card")) {
            matcher = patternPAN.matcher(CreditCheckInfoGuarantor.PAN_No);
            // Check if pattern matches
            if(!matcher.matches()) {
                CreditCheckInfoProblems.add("Guarantor: " + "PAN_No format Invalid.");
            }
        } else if(strTemp2.equalsIgnoreCase("Voter ID")) {
            matcher = pattern1VoterID.matcher(CreditCheckInfoGuarantor.VoterID);
            // Check if pattern matches
            if(!matcher.matches()) {
                matcher = pattern2VoterID.matcher(CreditCheckInfoGuarantor.VoterID);
                // Check if pattern matches
                if(!matcher.matches()) {
                    CreditCheckInfoProblems.add("Guarantor: " + "VoterID format Invalid.");
                }
            }
        }
        if( (CreditCheckInfoGuarantor.FName.isEmpty()) ||
                (CreditCheckInfoGuarantor.MName.isEmpty() && CreditCheckInfoGuarantor.LName.isEmpty()) ) {
            CreditCheckInfoProblems.add("Guarantor: पूर्ण नाव भरणे बाकी");
        } else if(  (CreditCheckInfoGuarantor.FName.equalsIgnoreCase("NA")) &&
                (CreditCheckInfoGuarantor.MName.equalsIgnoreCase("NA")) &&
                (CreditCheckInfoGuarantor.LName.equalsIgnoreCase("NA")) ) {
            CreditCheckInfoProblems.add("Guarantor: पूर्ण नाव भरणे बाकी");
        }
        if(CreditCheckInfoGuarantor.Gender.isEmpty()) {
            CreditCheckInfoProblems.add("Guarantor: Gender भरणे बाकी");
        }
        if(CreditCheckInfoGuarantor.DateOfBirth.isEmpty()) {
            CreditCheckInfoProblems.add("Guarantor: DateOfBirth भरणे बाकी");
        }
        if(CreditCheckInfoGuarantor.Address.isEmpty()) {
            CreditCheckInfoProblems.add("Guarantor: Address भरणे बाकी");
        }
        if(CreditCheckInfoGuarantor.Pincode.isEmpty()) {
            CreditCheckInfoProblems.add("Guarantor: Pincode भरणे बाकी");
        }
        if(CreditCheckInfoGuarantor.City.isEmpty()) {
            CreditCheckInfoProblems.add("Guarantor: City भरणे बाकी");
        }
        if(CreditCheckInfoGuarantor.State.isEmpty()) {
            CreditCheckInfoProblems.add("Guarantor: State भरणे बाकी");
        }
        if(CreditCheckInfoGuarantor.ContactNo.isEmpty()) {
            CreditCheckInfoProblems.add("Guarantor: ContactNo भरणे बाकी");
        } else if(CreditCheckInfoGuarantor.ContactNo.length() < 10) {
            CreditCheckInfoProblems.add("Guarantor: ContactNo format Invalid.");
        }
        if (CreditCheckInfoGuarantor.MainBorrowerRelationID == 0) {
            CreditCheckInfoProblems.add("Guarantor: कर्जदारासोबतचे नाते भरणे बाकी.");
        }
//bmp 20-Jan-25
        if(CreditCheckInfoGuarantor.MainBorrowerRelationID == RelationIDSpouse) {
            if(     (CreditCheckInfoMainBorrower.MaritalStatusID != 1) &&
                    (CreditCheckInfoMainBorrower.MaritalStatusID != 7) ) {
                CreditCheckInfoProblems.add("Guarantor: कर्जदारासोबतचे नाते अयोग्य आहे.");
            }
        }
//bmp 20-Jan-25
        if (CreditCheckInfoGuarantor.ProfessionID == 0) {
            CreditCheckInfoProblems.add("Guarantor: व्यवसाय भरणे बाकी.");
        }
        if(!ValidateProfession(CreditCheckInfoGuarantor.ProfessionID)) {
            CreditCheckInfoProblems.add("Guarantor: व्यवसाय अयोग्य आहे.");
        }
//bmp 15-May-25
        if (CreditCheckInfoGuarantor.MonthlyIncome == 0) {
            CreditCheckInfoProblems.add("Guarantor: मासिक उत्पन्न भरणे बाकी.");
        }
//bmp 15-May-25

        if(CreditCheckInfoProblems.size() > 0)
            Status = false;

        return Status;
    }
    public boolean ValidateProfession(long ProfessionID) {
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
                            String strUID = myparser.getAttributeValue(null, "uid");
                            clsCreditCheckInfo newCreditCheckInfo = new clsCreditCheckInfo();
                            newCreditCheckInfo.InitCreditCheckInfo();
                            if(selectedType == 1) {
                                newCreditCheckInfo = CreditCheckInfoMainBorrower;
                            } else if(selectedType == 2) {
                                newCreditCheckInfo = CreditCheckInfoCoBorrower;
                            } else if (selectedType == 3) {
                                newCreditCheckInfo = CreditCheckInfoGuarantor;
                            }
                            newCreditCheckInfo.AdhaarNo = strUID;
                            String AadhaarName = myparser.getAttributeValue(null, "name");
                            AadhaarName = AadhaarName.trim();
                            String[] strSplit = AadhaarName.split(" ");
                            switch (strSplit.length) {
                                case 0:
                                    newCreditCheckInfo.FName = "";
                                    newCreditCheckInfo.MName = "";
                                    newCreditCheckInfo.LName = "";
                                    break;
                                case 1:
                                    newCreditCheckInfo.FName = strSplit[0].trim();
                                    newCreditCheckInfo.MName = "";
                                    newCreditCheckInfo.LName = "";
                                    break;
                                case 2:
                                    newCreditCheckInfo.FName = strSplit[0].trim();
                                    newCreditCheckInfo.MName = "";
                                    newCreditCheckInfo.LName = strSplit[1].trim();
                                    break;
                                case 3:
                                    newCreditCheckInfo.FName = strSplit[0].trim();
                                    newCreditCheckInfo.MName = strSplit[1].trim();
                                    newCreditCheckInfo.LName = strSplit[2].trim();
                                    break;
                                default:
                                    newCreditCheckInfo.FName = AadhaarName;
                                    newCreditCheckInfo.MName = AadhaarName;
                                    newCreditCheckInfo.LName = AadhaarName;
                                    break;
                            }

                            newCreditCheckInfo.Gender = myparser.getAttributeValue(null, "gender");
                            if(newCreditCheckInfo.Gender.toUpperCase().contains("F"))
                                newCreditCheckInfo.Gender = "F";
                            else
                                newCreditCheckInfo.Gender = "M";

                            int iAadhaarYearOfBirth = 0;
                            String AadhaarYearOfBirth = myparser.getAttributeValue(null, "yob");
                            try {
                                iAadhaarYearOfBirth = Integer.parseInt(AadhaarYearOfBirth);
                            } catch (NumberFormatException e) {
                            }
                            newCreditCheckInfo.DateOfBirth = myparser.getAttributeValue(null, "dob");
                            if(newCreditCheckInfo.DateOfBirth == null) {
                                newCreditCheckInfo.DateOfBirth = "";
                                Calendar CalendarDOB = Calendar.getInstance();
                                int iYear = CalendarDOB.get(Calendar.YEAR);
                                int iYearOfBirth = iAadhaarYearOfBirth;
//                                newCreditCheckInfo.Age = iYear - iYearOfBirth;
                                CalendarDOB.set(Calendar.YEAR, iYearOfBirth);
                                CalendarDOB.set(Calendar.MONTH, Calendar.JANUARY);
                                CalendarDOB.set(Calendar.DATE, 1);
                                String strBirthDate = String.format("%04d", CalendarDOB.get(Calendar.YEAR)) + "-"
                                        + String.format("%02d", (CalendarDOB.get(Calendar.MONTH) + 1)) + "-"
                                        + String.format("%02d", CalendarDOB.get(Calendar.DATE));
                                newCreditCheckInfo.DateOfBirth = strBirthDate;
                            } else {
                                try {
                                    Integer.parseInt(newCreditCheckInfo.DateOfBirth.substring(0, 4));
                                } catch (NumberFormatException e) {
                                    String strBirthDate = newCreditCheckInfo.DateOfBirth.substring(6, 10) + "-"
                                            + newCreditCheckInfo.DateOfBirth.substring(3, 5) + "-"
                                            + newCreditCheckInfo.DateOfBirth.substring(0, 2);
                                    newCreditCheckInfo.DateOfBirth = strBirthDate;
                                }
                                Calendar calDOB = Calendar.getInstance();
                                calDOB.set(Calendar.YEAR, Integer.parseInt(newCreditCheckInfo.DateOfBirth.substring(0, 4)));
                                calDOB.set(Calendar.MONTH, (Integer.parseInt(newCreditCheckInfo.DateOfBirth.substring(5, 7)) - 1));
                                calDOB.set(Calendar.DATE, Integer.parseInt(newCreditCheckInfo.DateOfBirth.substring(8, 10)));
                                Calendar calNow = Calendar.getInstance();
                                int iAge = calNow.get(Calendar.YEAR) - calDOB.get(Calendar.YEAR);
                                if (calNow.get(Calendar.DAY_OF_YEAR) < calDOB.get(Calendar.DAY_OF_YEAR)){
                                    iAge--;
                                }
//                                newCreditCheckInfo.Age = iAge;
                            }

                            String AadhaarCareOf = myparser.getAttributeValue(null, "co");

                            String AadhaarHouse = myparser.getAttributeValue(null, "house");
                            if(AadhaarHouse == null)
                                AadhaarHouse = "";
                            else if(AadhaarHouse.equalsIgnoreCase("null"))
                                AadhaarHouse = "";
                            else
                                AadhaarHouse = AadhaarHouse.trim();

                            String AadhaarStreet = myparser.getAttributeValue(null, "street");
                            if(AadhaarStreet == null)
                                AadhaarStreet = "";
                            else if(AadhaarStreet.equalsIgnoreCase("null"))
                                AadhaarStreet = "";
                            else
                                AadhaarStreet = AadhaarStreet.trim();

                            String AadhaarLandmark = myparser.getAttributeValue(null, "lm");
                            if(AadhaarLandmark == null)
                                AadhaarLandmark = "";
                            else if(AadhaarLandmark.equalsIgnoreCase("null"))
                                AadhaarLandmark = "";
                            else
                                AadhaarLandmark = AadhaarLandmark.trim();

                            String AadhaarLocality = myparser.getAttributeValue(null, "loc");
                            if(AadhaarLocality == null)
                                AadhaarLocality = "";
                            else if(AadhaarLocality.equalsIgnoreCase("null"))
                                AadhaarLocality = "";
                            else
                                AadhaarLocality = AadhaarLocality.trim();

                            String AadhaarVillageTownCity = myparser.getAttributeValue(null, "vtc");
                            if(AadhaarVillageTownCity == null)
                                AadhaarVillageTownCity = "";
                            else if(AadhaarVillageTownCity.equalsIgnoreCase("null"))
                                AadhaarVillageTownCity = "";
                            else
                                AadhaarVillageTownCity = AadhaarVillageTownCity.trim();
                            newCreditCheckInfo.City = AadhaarVillageTownCity;

                            String AadhaarPostOffice = myparser.getAttributeValue(null, "po");
                            if(AadhaarPostOffice == null)
                                AadhaarPostOffice = "";
                            else if(AadhaarPostOffice.equalsIgnoreCase("null"))
                                AadhaarPostOffice = "";
                            else
                                AadhaarPostOffice = AadhaarPostOffice.trim();

                            String AadhaarDistrict = myparser.getAttributeValue(null, "dist");
                            if(AadhaarDistrict == null)
                                AadhaarDistrict = "";
                            else if(AadhaarDistrict.equalsIgnoreCase("null"))
                                AadhaarDistrict = "";
                            else
                                AadhaarDistrict = AadhaarDistrict.trim();

                            String AadhaarSubDistrict = myparser.getAttributeValue(null, "subdist");
                            if(AadhaarSubDistrict == null)
                                AadhaarSubDistrict = "";
                            else if(AadhaarSubDistrict.equalsIgnoreCase("null"))
                                AadhaarSubDistrict = "";
                            else
                                AadhaarSubDistrict = AadhaarSubDistrict.trim();

                            String AadhaarState = myparser.getAttributeValue(null, "state");
                            if(AadhaarState == null)
                                AadhaarState = "";
                            else if(AadhaarState.equalsIgnoreCase("null"))
                                AadhaarState = "";
                            else
                                AadhaarState = AadhaarState.trim();
//                            newCreditCheckInfo.State = AadhaarState;
                            for(int i=0; i<StateDescriptionMaster.size(); i++) {
                                if(StateDescriptionMaster.get(i).equalsIgnoreCase(AadhaarState)) {
                                    newCreditCheckInfo.State = StateCodeMaster.get(i);
                                    break;
                                }
                            }

                            String AadhaarAddress = myparser.getAttributeValue(null, "address");
                            if(AadhaarAddress == null)
                                AadhaarAddress = "";
                            else if(AadhaarAddress.equalsIgnoreCase("null"))
                                AadhaarAddress = "";
                            else
                                AadhaarAddress = AadhaarAddress.trim();

                            newCreditCheckInfo.Pincode = myparser.getAttributeValue(null, "pc");
                            if(newCreditCheckInfo.Pincode == null)
                                newCreditCheckInfo.Pincode = "0";
                            newCreditCheckInfo.Address = AadhaarHouse + " " + AadhaarStreet + " " + AadhaarLandmark + " " + AadhaarLocality + " "
                                    + AadhaarVillageTownCity /*+ " " + AadhaarPostOffice*/ + " " + AadhaarSubDistrict + " " + AadhaarState;
                            if(newCreditCheckInfo.Address.trim().isEmpty()) {
                                newCreditCheckInfo.Address = AadhaarAddress;
                            }
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

    public void SQLite_getCreditCheckList() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        try {
            db.execSQL(ufinDbContract.CreditValidityCheck.SQL_CREATE_CREDIT_VALIDITY_CHECK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        String alterTableQuery = "ALTER TABLE " +
                ufinDbContract.CreditValidityCheck.TABLE_NAME +
                " ADD COLUMN ";
        String[] newColumns = {
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Role + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_AdhaarNo + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_RationCardNo + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_PAN_No + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_VoterID + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_FName + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_MName + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_LName + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Gender + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_DateOfBirth + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Address + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_City + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_State + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Pincode + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_ContactNo + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_CreditCheckProgress + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_CreditStatus + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_MemberType + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_ListType + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Remarks + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBEnquiryDate + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBScore + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBStatus + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBRemark + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_ProfessionID + " INTEGER",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_MainBorrowerRelationID + " INTEGER",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_IsFamilyMember + " INTEGER",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_MainBorrowerAdhaarNo + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_OwnHouse + " INTEGER",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_MaritalStatusID + " INTEGER",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_ClusterID + " INTEGER",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_CommunityID + " INTEGER",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Cluster + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Community + " TEXT",
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_MonthlyIncome + " INTEGER",
        };
        db.beginTransaction();
        for (String column : newColumns){
            try {
                db.execSQL(alterTableQuery + column);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Role,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_AdhaarNo,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_RationCardNo,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_PAN_No,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_VoterID,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_FName,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_MName,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_LName,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Gender,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_DateOfBirth,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Address,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_City,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_State,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Pincode,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_MonthlyIncome,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_ContactNo,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_CreditCheckProgress,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_CreditStatus,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_MemberType,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_ListType,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Remarks,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBEnquiryDate,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBScore,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBStatus,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBRemark,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_ProfessionID,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_MainBorrowerRelationID,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_IsFamilyMember,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_MainBorrowerAdhaarNo,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_OwnHouse,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_MaritalStatusID,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_ClusterID,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_CommunityID,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Cluster,
                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Community,
        };
        Cursor c1 = db.query(
                ufinDbContract.CreditValidityCheck.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        CreditCheckInfoList.clear();
        CreditCheckList.clear();
        CreditCheckList.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            clsCreditCheckInfo newInfo = new clsCreditCheckInfo();
                            newInfo.InitCreditCheckInfo();
                            newInfo.Role = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Role));
                            newInfo.AdhaarNo = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_AdhaarNo));
                            String strDocumentNo = newInfo.AdhaarNo;
                            strDocumentNo = strDocumentNo.replace(" ", "");
                            newInfo.AdhaarNo = strDocumentNo;
                            newInfo.MainBorrowerAdhaarNo = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_MainBorrowerAdhaarNo));
                            strDocumentNo = newInfo.MainBorrowerAdhaarNo;
                            strDocumentNo = strDocumentNo.replace(" ", "");
                            newInfo.MainBorrowerAdhaarNo = strDocumentNo;
                            newInfo.RationCardNo = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_RationCardNo));
                            newInfo.PAN_No = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_PAN_No));
                            newInfo.VoterID = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_VoterID));
                            newInfo.FName = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_FName));
                            newInfo.MName = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_MName));
                            newInfo.LName = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_LName));
                            newInfo.Gender = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Gender));
                            newInfo.DateOfBirth = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_DateOfBirth));
                            newInfo.Address = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Address));
                            newInfo.City = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_City));
                            newInfo.State = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_State));
                            newInfo.Pincode = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Pincode));
                            newInfo.MonthlyIncome = c1.getInt(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_MonthlyIncome));
                            newInfo.ContactNo = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_ContactNo));
                            newInfo.CreditCheckProgress = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_CreditCheckProgress));
                            newInfo.CreditHistory = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_CreditStatus));
                            newInfo.MemberType = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_MemberType));
                            newInfo.ListType = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_ListType));
                            newInfo.CHRemarks = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Remarks));
                            newInfo.CBEnquiryDate = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBEnquiryDate));
                            newInfo.CBScore = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBScore));
                            newInfo.CBStatus = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBStatus));
                            newInfo.CBRemark = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBRemark));
                            newInfo.MainBorrowerRelationID = c1.getLong(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_MainBorrowerRelationID));
                            newInfo.iFamilyMember = c1.getInt(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_IsFamilyMember));
                            newInfo.ProfessionID = c1.getLong(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_ProfessionID));
                            newInfo.OwnHouse = c1.getInt(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_OwnHouse));
                            newInfo.MaritalStatusID = c1.getLong(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_MaritalStatusID));
                            newInfo.ClusterID = c1.getLong(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_ClusterID));
                            newInfo.CommunityID = c1.getLong(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_CommunityID));
                            newInfo.Cluster = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Cluster));
                            newInfo.Community = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Community));

                            if(newInfo.AdhaarNo.equalsIgnoreCase(newInfo.MainBorrowerAdhaarNo)) {
                                if(newInfo.MainBorrowerAdhaarNo.isEmpty() || newInfo.FName.isEmpty()) {
//                                    Toast toast = Toast.makeText(mainActivity.getBaseContext(), "MainBorrowerAdhaarNo and/or FName is empty", Toast.LENGTH_SHORT);
//                                    ViewGroup group = (ViewGroup) toast.getView();
//                                    TextView messageTextView = (TextView) group.getChildAt(0);
//                                    messageTextView.setTextSize(28);
//                                    toast.show();
                                    ShowToastMessage("MainBorrowerAdhaarNo and/or FName is empty");
                                } else {
                                    String strTemp = newInfo.MainBorrowerAdhaarNo + ":" + newInfo.FName + ":" + newInfo.CreditCheckProgress;
                                    CreditCheckList.add(strTemp);
                                }
                            }
                            if(newInfo.CreditHistory.equalsIgnoreCase("null")) {
                                newInfo.CreditHistory = "";
                            }
                            if((!newInfo.MemberType.isEmpty()) && (!newInfo.MemberType.equalsIgnoreCase("NA"))) {
                                newInfo.CreditHistory = newInfo.CreditHistory + ";Member Type: " + newInfo.MemberType;
                            }
                            if((!newInfo.ListType.isEmpty()) && (!newInfo.ListType.equalsIgnoreCase("NA"))) {
                                newInfo.CreditHistory = newInfo.CreditHistory + ";List Type: " + newInfo.ListType;
                            }
                            CreditCheckInfoList.add(newInfo);
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
//bmp 20-Jan-25
                            if(str2.equalsIgnoreCase("Spouse")) {
                                RelationIDSpouse = RelationIDMaster.get(RelationIDMaster.size() - 1);
                            }
//bmp 20-Jan-25
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
    public void SQLite_getMaritalStatusMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
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
    public void SQLite_getStateMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.StateMaster.COLUMN_NAME_StateCode,
                ufinDbContract.StateMaster.COLUMN_NAME_Description,
                ufinDbContract.StateMaster.COLUMN_NAME_SequenceID,
        };
        String Active = "1";

        String selection1 = ufinDbContract.StateMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.StateMaster.COLUMN_NAME_SequenceID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.StateMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        StateCodeMaster.clear();
        StateDescriptionMaster.clear();
        StateCodeMaster.add("");
        StateDescriptionMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            StateCodeMaster.add(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.StateMaster.COLUMN_NAME_StateCode)));
                            StateDescriptionMaster.add(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.StateMaster.COLUMN_NAME_Description)));
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
