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
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_CANCELED;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnlineCollectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnlineCollectionFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final int ufin_ONLINE_COLLECTION_DIALOG_STATE_NONE                    = 0;
    public static final int ufin_ONLINE_COLLECTION_DIALOG_STATE_INVALID_INFO            = 1;
    public static final int ufin_ONLINE_COLLECTION_DIALOG_STATE_FETCH_TOKEN             = 2;
    public static final int ufin_ONLINE_COLLECTION_DIALOG_STATE_INVALID_CLIENT          = 3;
    public static final int ufin_ONLINE_COLLECTION_DIALOG_STATE_TRANSACTION_INITIATE    = 4;
    public static final int ufin_ONLINE_COLLECTION_DIALOG_STATE_TRANSACTION_SHOW_QR     = 5;
    public static final int ufin_ONLINE_COLLECTION_DIALOG_STATE_TRANSACTION_WAIT        = 6;
    public static final int ufin_ONLINE_COLLECTION_DIALOG_STATE_TRANSACTION_CANCEL      = 7;
    public static final int ufin_ONLINE_COLLECTION_DIALOG_STATE_TRANSACTION_TIMEOUT     = 8;
    public static final int ufin_ONLINE_COLLECTION_DIALOG_STATE_TRANSACTION_FAILED      = 9;
    public static final int ufin_ONLINE_COLLECTION_DIALOG_STATE_TRANSACTION_SUCCESSFUL  = 10;

    public static final String MSG_ufin_ONLINE_COLLECTION_INVALID_INFO =
            "Online Collection Information Invalid:";

    View rootView;
    MainActivity mainActivity;
    private boolean dbMaster = false;
    public AlertDialog.Builder dlgAlert;
    public static int dlgState = ufin_ONLINE_COLLECTION_DIALOG_STATE_NONE;

    private static final int CAMERA_REQUEST = 1;
    public static final int MAX_PHOTO_FILESIZE             = (4 * 1024 * 1024);
//    public static final int MAX_PHOTO_FILESIZE             = (8 * 1024 * 1024);

    int selectedCollectionMemberIndex;
    String strFilepath = "";
    String strFilename = "unknown";

    ArrayList<String> OnlineCollectionProblems = new ArrayList<String>();
    String strName, strSavingAccNo;
    int iOnlinePaymentAmount;
    long lGroupID;
    long selectedCollectionMemberID;

    private ArrayList<Long> RelationIDMaster = new ArrayList<Long>();
    private ArrayList<String> RelationMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<String> RelationDescriptionMaster = new ArrayList<String>();
    private ArrayList<Long> PaymentByOtherRelationIDMaster = new ArrayList<Long>();
    private ArrayList<String> PaymentByOtherRelationMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<String> PaymentByOtherRelationDescriptionMaster = new ArrayList<String>();
    private long RelationIDMainMember;
    private int RelationIndexMainMember;
    private ArrayList<Long> OnlinePaymentByMemberIDMaster = new ArrayList<Long>();
    private ArrayList<String> OnlinePaymentByMaster = new ArrayList<String>();
    private ArrayList<String> OnlinePaymentByRelationMaster = new ArrayList<String>();
    private ArrayList<Long> OnlinePaymentByRelationIDMaster = new ArrayList<Long>();
//    private ArrayList<Long> OnlinePaymentAppsIDMaster = new ArrayList<Long>();
//    private ArrayList<String> OnlinePaymentAppsMaster = new ArrayList<String>();
    long lGroupMemberRelationID;
    String strGroupMemberRelationMarathiDescription, strGroupMemberRelationDescription;

    DatePickerDialog dlgDatePicker = null;
    final Calendar myCalendar = Calendar.getInstance();
    long OnlinePaymentByMemberID;
    String strOnlinePaymentByFName;
    String strOnlinePaymentByMName;
    String strOnlinePaymentByLName;
    long OnlinePaymentByRelationID;
    String strOnlineTransactionDate, strOnlineTransactionDateForDB, strOnlinePaymentTransactionID;
    int iOnlineTransactionAmount;
    String strOnlinePaymentUPIID;
//    String strOnlinePaymentApp;
//    long OnlinePaymentAppID;
//    String strScreenShotDate, strScreenShotDateForDB;
    int iSelectDate;
//Hardcoded for testing
    String strClientID = "IT1247";
    String strClientSecret = "qSmaPjSnDlmoPjscBU9eacch623sXNHd";
//    String strPaymentGatewayUsername = "1078346192";
//    String strPaymentGatewayPassword = "1321";

    String strInvoiceNo, strTransactionAmount;
    String access_token, qrString, referenceId, QRreferenceId;
    int itokenExpires_in, iQRExpires_in;
    boolean flagCancel, flagClose;

    public OnlineCollectionFragment() {
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
    public static OnlineCollectionFragment newInstance(String param1, String param2) {
        OnlineCollectionFragment fragment = new OnlineCollectionFragment();
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
//        return inflater.inflate(R.layout.fragment_collection, container, false);
        rootView = inflater.inflate(R.layout.fragment_online_collection, container, false);
        mainActivity = (MainActivity) getActivity();

        if(!dbMaster) {
            SQLite_getRelationMaster();
//            SQLite_getOnlinePaymentAppMaster();

            strOnlinePaymentByFName = "";
            strOnlinePaymentByMName = "";
            strOnlinePaymentByLName = "";
            OnlinePaymentByRelationID = 0L;
//            strOnlinePaymentApp = "";
//            OnlinePaymentAppID = 0L;
            strOnlinePaymentTransactionID = "";
            strOnlinePaymentUPIID = "";
            strOnlineTransactionDateForDB = "";
            strOnlineTransactionDate = "";
//            strScreenShotDateForDB = "";
//            strScreenShotDate = "";
            iSelectDate = -1;
            iOnlineTransactionAmount = 0;
            dbMaster = true;
        }

        ImageView imgview = (ImageView) rootView.findViewById(R.id.imageViewQR);
        imgview.setImageBitmap(null);

        Button bt = (Button) rootView.findViewById(R.id.buttonCancel);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonSave);
        bt.setOnClickListener(this);
//        ImageButton imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonPhotoDoc);
//        imgbt.setOnClickListener(this);
//        imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonPreviewDoc);
//        imgbt.setOnClickListener(this);
//        imgbt = (ImageButton) rootView.findViewById(R.id.imageButtonDeleteDoc);
//        imgbt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonGenerateQR);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonCancelQR);
        bt.setOnClickListener(this);

        dlgDatePicker = new DatePickerDialog(getContext(),
                datepick,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

//        EditText etTransactionDate = (EditText) rootView.findViewById(R.id.editTextTransactionDate);
//        etTransactionDate.setKeyListener(null);
//        etTransactionDate.setEnabled(true);
//        etTransactionDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//                keyboard.hideSoftInputFromWindow(etTransactionDate.getWindowToken(), 0);
//                iSelectDate = 1;
//                if (!dlgDatePicker.isShowing()) {
//                    dlgDatePicker.show();
//                }
//            }
//        });
//        etTransactionDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View arg0, boolean hasFocus) {
//                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//                keyboard.hideSoftInputFromWindow(etTransactionDate.getWindowToken(), 0);
//                iSelectDate = 1;
//                if (hasFocus && !(dlgDatePicker.isShowing())) {
//                    dlgDatePicker.show();
//                }
//            }
//        });
//        EditText etScreenShotDate = (EditText) rootView.findViewById(R.id.editTextScreenshotDate);
//        etScreenShotDate.setKeyListener(null);
//        etScreenShotDate.setEnabled(true);
//        etScreenShotDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//                keyboard.hideSoftInputFromWindow(etScreenShotDate.getWindowToken(), 0);
//                iSelectDate = 0;
//                if (!dlgDatePicker.isShowing()) {
//                    dlgDatePicker.show();
//                }
//            }
//        });
//        etScreenShotDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View arg0, boolean hasFocus) {
//                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//                keyboard.hideSoftInputFromWindow(etScreenShotDate.getWindowToken(), 0);
//                iSelectDate = 0;
//                if (hasFocus && !(dlgDatePicker.isShowing())) {
//                    dlgDatePicker.show();
//                }
//            }
//        });

//        String[] OnlinePaymentAppMaster = getResources().getStringArray(R.array.OnlinePaymentApp_array);
//        ArrayAdapter<String> adapterPaymentApp = new ArrayAdapter<String>(getActivity().getBaseContext(),
//                R.layout.simple_spinner_item, OnlinePaymentAppsMaster);
//        adapterPaymentApp.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
//        Spinner sp3 = (Spinner) rootView.findViewById(R.id.spinnerOnlinePaymentApp);
//        sp3.setAdapter(adapterPaymentApp);
//        sp3.setSelection(0);
//        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(position == 0)
//                    return;
////                strOnlinePaymentApp = OnlinePaymentAppsMaster.get(position);
//                OnlinePaymentAppID = OnlinePaymentAppsIDMaster.get(position);
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });

        RefreshScreen();

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            if(mainActivity == null)
                return;

            RefreshScreen();
        }
    }

    public void RefreshScreen() {
//bmp 12-Aug-26
//        if(mainActivity.indexCollectionMemberList != 0) {
        if(mainActivity.indexCollectionMemberList >= 0) {
//bmp 12-Aug-26
            selectedCollectionMemberIndex = mainActivity.indexCollectionMemberList;
            selectedCollectionMemberID = mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).MemberID;
            strName = mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).MemberName;
            String[] sprSplit = strName.split(" ");
            String strTemp = "";
            for(int i=0; i<sprSplit.length; i++) {
                sprSplit[i] = sprSplit[i].replace(" ", "");
                if(!sprSplit[i].isEmpty()) {
                    if(!strTemp.isEmpty()) {
                        strTemp = strTemp + " ";
                    }
                    strTemp = strTemp + sprSplit[i];
                }
            }
            strName = strTemp;

            strSavingAccNo = mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).SavingAccNo;
            lGroupID = mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).GroupID;

            TextView tv = (TextView) rootView.findViewById(R.id.textViewMemberName);
            tv.setText(strName);
            tv = (TextView) rootView.findViewById(R.id.textViewMemberSavingAccNo);
            tv.setText(strSavingAccNo);
//bmp 24-Jul-26
//            Calendar cal = Calendar.getInstance();
//            Date date = cal.getTime();
//            SimpleDateFormat sdf0 = new SimpleDateFormat("dd-MMM", Locale.ENGLISH);
//            String ddMMMTodate = sdf0.format(date);// all done
//            tv = (TextView) rootView.findViewById(R.id.textViewTodateTotalDue);
//            tv.setText(ddMMMTodate + " देय रू.: ");
//            tv = (TextView) rootView.findViewById(R.id.textViewTotalDueTodate);
//            tv.setText(String.valueOf(mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).TotalDueAmountsToPayToday));
////bmp 29-May-26
//            tv = (TextView) rootView.findViewById(R.id.textViewPrePaymentPreClosureAmount);
//            tv.setText(String.valueOf(mainActivity.iPrePaymentPreClosureAmount));
//            if(mainActivity.iPrePaymentPreClosureAmount == 0) {
//                tv.setText("-");
//            }
////bmp 29-May-26
            tv = (TextView) rootView.findViewById(R.id.textViewTotalDueTodate);
            if(mainActivity.iPrePaymentPreClosureAmount > 0) {
                tv.setText(String.valueOf(mainActivity.iPrePaymentPreClosureAmount));
            } else {
                tv.setText(String.valueOf(mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).TotalDueAmountsToPayToday));
            }
//bmp 24-Jul-26
            SQLite_getMemberData();

            strOnlinePaymentTransactionID = "";
            iOnlineTransactionAmount = 0;
            strOnlineTransactionDate = "";
            strOnlineTransactionDateForDB = "";
//            strScreenShotDateForDB = "";
//            strScreenShotDate = "";
            iSelectDate = -1;
            strOnlinePaymentUPIID = "";
//            strOnlinePaymentApp = "";
//            OnlinePaymentAppID = 0L;

            strOnlinePaymentByFName = "";
            strOnlinePaymentByMName = "";
            strOnlinePaymentByLName = "";
            String strOnlinePaymentByName = mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strOnlinePaymentByName;
            String[] strSplit = strOnlinePaymentByName.split(" ");
            switch (strSplit.length) {
                case 1:
                    strOnlinePaymentByFName = strSplit[0];
                    break;
                case 2:
                    strOnlinePaymentByFName = strSplit[0];
                    strOnlinePaymentByLName = strSplit[1];
                    break;
                case 3:
                    strOnlinePaymentByFName = strSplit[0];
                    strOnlinePaymentByMName = strSplit[1];
                    strOnlinePaymentByLName = strSplit[2];
                    break;
            }
            OnlinePaymentByMemberID =
                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).OnlinePaymentByMemberID;
            OnlinePaymentByRelationID =
                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).OnlinePaymentByRelationID;
//            strOnlinePaymentApp =
//                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strOnlinePaymentApp;
//            OnlinePaymentAppID =
//                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).OnlinePaymentAppID;
            strOnlinePaymentTransactionID =
                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strOnlineTransactionID;
            strOnlinePaymentUPIID =
                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strOnlinePaymentUPIID;
            strOnlineTransactionDateForDB =
                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strOnlineTransactionDate;
//bmp 27-Jul-26
            Calendar calSet = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            //yyyy-MM-dd Format for DB
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                strOnlineTransactionDate = "";
                Date date1 = sdf2.parse(strOnlineTransactionDateForDB);
                calSet.setTime(date1);
                strOnlineTransactionDate = sdf1.format(calSet.getTime());
            } catch (ParseException e) {
//                throw new RuntimeException(e);
            }

//            strScreenShotDateForDB =
//                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strScreenShotDate;
//            try {
//                strScreenShotDate = "";
//                Date date0 = sdf2.parse(strScreenShotDateForDB);
//                calSet.setTime(date0);
//                strScreenShotDate = sdf1.format(calSet.getTime());
//            } catch (ParseException e) {
////                throw new RuntimeException(e);
//            }
//bmp 27-Jul-26
            iOnlineTransactionAmount =
                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).iOnlineTransactionAmount;

            ArrayAdapter<String> adapterOnlinePaymentByMember = new ArrayAdapter<String>(getActivity().getBaseContext(),
                    R.layout.simple_spinner_item, OnlinePaymentByMaster);
            adapterOnlinePaymentByMember.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            Spinner sp1 = (Spinner) rootView.findViewById(R.id.spinnerOnlinePaymentBy);
            sp1.setAdapter(adapterOnlinePaymentByMember);
            sp1.setSelection(0);
            sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position == 0) {
                        EditText et = (EditText) rootView.findViewById(R.id.editTextOnlinePaymentByFname);
                        et.setEnabled(true);
                        et = (EditText) rootView.findViewById(R.id.editTextOnlinePaymentByMname);
                        et.setEnabled(true);
                        et = (EditText) rootView.findViewById(R.id.editTextOnlinePaymentByLname);
                        et.setEnabled(true);
                        Spinner sp2 = (Spinner) rootView.findViewById(R.id.spinnerOnlinePaymentByRelation);
                        sp2.setEnabled(true);
                    } else {
                        EditText et = (EditText) rootView.findViewById(R.id.editTextOnlinePaymentByFname);
                        et.setText("");
                        et.setEnabled(false);
                        et = (EditText) rootView.findViewById(R.id.editTextOnlinePaymentByMname);
                        et.setText("");
                        et.setEnabled(false);
                        et = (EditText) rootView.findViewById(R.id.editTextOnlinePaymentByLname);
                        et.setText("");
                        et.setEnabled(false);
                        Spinner sp2 = (Spinner) rootView.findViewById(R.id.spinnerOnlinePaymentByRelation);
                        sp2.setSelection(0);
                        sp2.setEnabled(false);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

//            ArrayAdapter<String> adapterPaymentByRelation = new ArrayAdapter<String>(getActivity().getBaseContext(),
//                    R.layout.simple_spinner_item, RelationMarathiDescriptionMaster);
            ArrayAdapter<String> adapterPaymentByRelation = new ArrayAdapter<String>(getActivity().getBaseContext(),
                    R.layout.simple_spinner_item, PaymentByOtherRelationMarathiDescriptionMaster);
            adapterPaymentByRelation.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            Spinner sp2 = (Spinner) rootView.findViewById(R.id.spinnerOnlinePaymentByRelation);
            sp2.setAdapter(adapterPaymentByRelation);
            sp2.setSelection(0);
            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position == 0)
                        return;
                    OnlinePaymentByRelationID = PaymentByOtherRelationIDMaster.get(position);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            boolean flagOther = true;
            for(int i=0; i<OnlinePaymentByMaster.size(); i++) {
                if(OnlinePaymentByMaster.get(i).startsWith(strOnlinePaymentByName)) {
                    flagOther = false;
                    sp1.setSelection(i);
                    break;
                }
            }
            if(flagOther) {
                EditText et1 = (EditText) rootView.findViewById(R.id.editTextOnlinePaymentByFname);
                et1.setText(strOnlinePaymentByFName);
                et1 = (EditText) rootView.findViewById(R.id.editTextOnlinePaymentByMname);
                et1.setText(strOnlinePaymentByMName);
                et1 = (EditText) rootView.findViewById(R.id.editTextOnlinePaymentByLname);
                et1.setText(strOnlinePaymentByLName);
                Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerOnlinePaymentByRelation);
                for(int i=0; i<PaymentByOtherRelationIDMaster.size(); i++) {
                    if(PaymentByOtherRelationIDMaster.get(i) == OnlinePaymentByRelationID) {
                        sp.setSelection(i);
                        break;
                    }
                }
//bmp 07-Jul-26
            } else {
                EditText et1 = (EditText) rootView.findViewById(R.id.editTextOnlinePaymentByFname);
                et1.setText("");
                et1 = (EditText) rootView.findViewById(R.id.editTextOnlinePaymentByMname);
                et1.setText("");
                et1 = (EditText) rootView.findViewById(R.id.editTextOnlinePaymentByLname);
                et1.setText("");
//bmp 07-Jul-26
            }


//            String[] OnlinePaymentAppMaster = getResources().getStringArray(R.array.OnlinePaymentApp_array);
//            ArrayAdapter<String> adapterPaymentApp = new ArrayAdapter<String>(getActivity().getBaseContext(),
//                    R.layout.simple_spinner_item, OnlinePaymentAppsMaster);
//            adapterPaymentApp.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
//            Spinner sp3 = (Spinner) rootView.findViewById(R.id.spinnerOnlinePaymentApp);
//            sp3.setAdapter(adapterPaymentApp);
//            sp3.setSelection(0);
//            sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    if(position == 0)
//                        return;
////                    strOnlinePaymentApp = OnlinePaymentAppsMaster[position];
//                    OnlinePaymentAppID = OnlinePaymentAppsIDMaster.get(position);
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//                }
//            });
//            for(int i=0; i<OnlinePaymentAppsIDMaster.size(); i++) {
//                if(OnlinePaymentAppsIDMaster.get(i) == OnlinePaymentAppID) {
//                    sp3.setSelection(i);
//                    break;
//                }
//            }
        }

        TextView tv = (TextView) rootView.findViewById(R.id.textViewTransactionID);
        tv.setText(strOnlinePaymentTransactionID);
        EditText et = (EditText) rootView.findViewById(R.id.editTextOnlineTransactionAmount);
        et.setText("");
        if(iOnlineTransactionAmount > 0) {
            et.setText(String.valueOf(iOnlineTransactionAmount));
        }
        tv = (TextView) rootView.findViewById(R.id.textViewTransactionDate);
        tv.setText(strOnlineTransactionDate);
////bmp 28-Jul-26
////        if(strScreenShotDate.isEmpty()) {
//        if(!mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strOnlinePaymentProof.isEmpty()
//                && strScreenShotDate.isEmpty() ) {
////bmp 28-Jul-26
//            Calendar calNow = Calendar.getInstance();
//            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
//            strScreenShotDate = sdf1.format(calNow.getTime());
//            //yyyy-MM-dd Format for DB
//            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//            strScreenShotDateForDB = sdf2.format(calNow.getTime());
//        }
//        et = (EditText) rootView.findViewById(R.id.editTextScreenshotDate);
//        et.setText(strScreenShotDate);

        if(strOnlinePaymentTransactionID.isEmpty()) {
            RadioGroup rgStatus = (RadioGroup) rootView.findViewById(R.id.radioGroupOnlinePaymentStatus);
            rgStatus.clearCheck();
        } else {
            RadioButton rb = (RadioButton) rootView.findViewById(R.id.radioButtonOnlinePaymentSuccessful);
            rb.setChecked(true);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonGenerateQR:
                EditText et = (EditText) rootView.findViewById(R.id.editTextOnlineTransactionAmount);
                int iAmount = Integer.parseInt(et.getText().toString());
                if(iAmount > 0) {
                    strTransactionAmount = String.valueOf(iAmount) + ".00";
                    AsyncCallWS_InitiatePayment taskInitiatePayment = new AsyncCallWS_InitiatePayment();
                    taskInitiatePayment.execute();
                }
                break;
            case R.id.buttonCancelQR:
                if(!qrString.isEmpty()) {
                    flagCancel = true;
//                    AsyncCallWS_CancelPayment taskCancelPayment = new AsyncCallWS_CancelPayment();
//                    taskCancelPayment.execute();
                }
                break;
            case R.id.buttonCancel:
                if(!qrString.isEmpty()) {
                    flagClose = true;
//                    AsyncCallWS_CancelPayment taskCancelPayment = new AsyncCallWS_CancelPayment();
//                    taskCancelPayment.execute();
                } else {
                    CustomViewPager viewPager1 = (CustomViewPager) mainActivity.findViewById(R.id.viewpagerCollection);
                    if (viewPager1 != null) {
                        viewPager1.requestFocus();
                        mainActivity.iCurrentScreen = 1;
                        viewPager1.setCurrentItem(mainActivity.iCurrentScreen);
                    }
                }
                break;
            case R.id.buttonSave:
                if(ValidateOnlineCollectionInfo()) {
                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).OnlinePaymentByMemberID
                            = OnlinePaymentByMemberID;
                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strOnlinePaymentByName
                            = strOnlinePaymentByFName + " " + strOnlinePaymentByMName + " " + strOnlinePaymentByLName;
                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).OnlinePaymentByRelationID
                            = OnlinePaymentByRelationID;
//                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strOnlinePaymentApp
//                            = strOnlinePaymentApp;
//                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).OnlinePaymentAppID
//                            = OnlinePaymentAppID;
                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strOnlineTransactionID
                            = strOnlinePaymentTransactionID;
                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strOnlinePaymentUPIID
                            = strOnlinePaymentUPIID;
                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strOnlineTransactionDate
                            = strOnlineTransactionDateForDB;
//                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strScreenShotDate
//                            = strScreenShotDateForDB;
                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).iOnlineTransactionAmount
                            = iOnlineTransactionAmount;
                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).TotalCollectionAmount
                            = iOnlineTransactionAmount;
//                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strOnlinePaymentProof;
                    mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strPaymentMode
                            = "Online";

                    CustomViewPager viewPager2 = (CustomViewPager) mainActivity.findViewById(R.id.viewpagerCollection);
                    if (viewPager2 != null) {
                        viewPager2.requestFocus();
                        mainActivity.iCurrentScreen = 1;
                        viewPager2.setCurrentItem(mainActivity.iCurrentScreen);
                    }
                } else {
                    String strMsg = MSG_ufin_ONLINE_COLLECTION_INVALID_INFO;
                    if(OnlineCollectionProblems.size() > 0) {
                        for(int i=0; i<OnlineCollectionProblems.size(); i++) {
                            strMsg = strMsg + "\n" + OnlineCollectionProblems.get(i);
                        }
                    }
                    CreateAlertDialog(true, true);
                    dlgState = ufin_ONLINE_COLLECTION_DIALOG_STATE_INVALID_INFO;
                    dlgAlert.setMessage(strMsg);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(28);
                    textView.setTextColor(Color.RED);
                }
                break;
//            case R.id.imageButtonPhotoDoc:
//                strFilepath = mainActivity.baseDir + "/data/";
//                Calendar cal = Calendar.getInstance();
//                Date date = cal.getTime();
//                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH);
//                String strTimeStamp = sdf.format(date);// all done
//                strFilename = String.valueOf(mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).MemberID) + "-" +
//                        strTimeStamp + "-" + String.valueOf(mainActivity.OrgID) + ".jpg";
//                File outFile = new File(strFilepath + strFilename);
//                if (!mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strOnlinePaymentProof.isEmpty()) {
//                    File oldFile = new File(strFilepath + mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strOnlinePaymentProof);
//                    if(oldFile.exists()) {
//                        oldFile.delete();
//                    }
//                }
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                Uri outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(),
//                        mainActivity.getApplicationContext().getPackageName() + ".provider",
//                        outFile);
////                        cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                cameraIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outuri);
//                startActivityIntent.launch(cameraIntent);
//
//                break;
//            case R.id.imageButtonPreviewDoc:
//                strFilepath = mainActivity.baseDir + "/data/";
//                strFilename = mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strOnlinePaymentProof;
//                if(!strFilename.isEmpty()) {
//                    File outFile1 = new File(strFilepath + strFilename);
//                    if (outFile1.exists()) {
//                        Intent intent = new Intent();
//                        intent.setAction(Intent.ACTION_VIEW);
//                        Uri outuri1 = FileProvider.getUriForFile(mainActivity.getApplicationContext(),
//                                mainActivity.getApplicationContext().getPackageName() + ".provider",
//                                outFile1);
//                        intent.setDataAndType(outuri1, "image/*");
//                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        startActivity(intent);
//                    }
//                }
//                break;
//            case R.id.imageButtonDeleteDoc:
//                strFilename = mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strOnlinePaymentProof;
//                if(!strFilename.isEmpty()) {
//                    strFilepath = mainActivity.baseDir + "/data/";
//                    File outFile2 = new File(strFilepath + strFilename);
//                    if(outFile2.exists()) {
//                        outFile2.delete();
//                        mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strOnlinePaymentProof = "";
//                    }
//                }
//                break;
        }
    }
    void SQLite_getMemberData() {
        OnlinePaymentByMemberIDMaster.clear();
        OnlinePaymentByMaster.clear();
        OnlinePaymentByRelationMaster.clear();
        OnlinePaymentByRelationIDMaster.clear();
        OnlinePaymentByMemberIDMaster.add(0L);
        OnlinePaymentByMaster.add("");
        OnlinePaymentByRelationMaster.add("");
        OnlinePaymentByRelationIDMaster.add(0L);

        OnlinePaymentByMemberIDMaster.add(selectedCollectionMemberID);
        String strMember = strName + ":" + RelationMarathiDescriptionMaster.get(RelationIndexMainMember);
        OnlinePaymentByMaster.add(strMember);
        OnlinePaymentByRelationMaster.add(RelationDescriptionMaster.get(RelationIndexMainMember));
        OnlinePaymentByRelationIDMaster.add(RelationIDMainMember);

        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.MemberData.COLUMN_NAME_MemberID,
                ufinDbContract.MemberData.COLUMN_NAME_Fname,
                ufinDbContract.MemberData.COLUMN_NAME_Mname,
                ufinDbContract.MemberData.COLUMN_NAME_Lname,
                ufinDbContract.MemberData.COLUMN_NAME_RelationID,
        };
        String Active = "1";

        String selection1 = ufinDbContract.MemberData.COLUMN_NAME_IsActive + "=" + "'" + Active + "'"
                + " AND " + ufinDbContract.MemberData.COLUMN_NAME_MainMemberID + "=" + "'" + String.valueOf(selectedCollectionMemberID) + "'";

        Cursor c1 = db.query(
                ufinDbContract.MemberData.TABLE_NAME,  // The table to query
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
                            long lMemberID = Long.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.MemberData.COLUMN_NAME_MemberID)));
                            OnlinePaymentByMemberIDMaster.add(lMemberID);

                            String strFname = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.MemberData.COLUMN_NAME_Fname));
                            if ((strFname == null) || strFname.isEmpty() || strFname.equalsIgnoreCase(" ")) {
                                strFname = "";
                            }
                            strFname = strFname.replace(" ", "");

                            String strMname = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.MemberData.COLUMN_NAME_Mname));
                            if ((strMname == null) || strMname.isEmpty() || strMname.equalsIgnoreCase(" ")) {
                                strMname = "";
                            }
                            strMname = strMname.replace(" ", "");

                            String strLname = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.MemberData.COLUMN_NAME_Lname));
                            if ((strLname == null) || strLname.isEmpty() || strLname.equalsIgnoreCase(" ")) {
                                strLname = "";
                            }
                            strLname = strLname.replace(" ", "");

                            String strFamilyMemberName = strFname + " " + strMname + " " + strLname;
                            long lRelationID = Long.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.MemberData.COLUMN_NAME_RelationID)));

                            String strFamilyMemberRelation = "?";
                            for(int j=0; j<RelationIDMaster.size(); j++) {
                                if(RelationIDMaster.get(j) == lRelationID) {
                                    strFamilyMemberRelation = RelationMarathiDescriptionMaster.get(j);
                                    String strFamilyMember = strFamilyMemberName + ":" + strFamilyMemberRelation;
                                    OnlinePaymentByMaster.add(strFamilyMember);
                                    OnlinePaymentByRelationMaster.add(RelationDescriptionMaster.get(j));
                                    OnlinePaymentByRelationIDMaster.add(lRelationID);
                                    break;
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
//            db.close();
        }

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection2 = {
                ufinDbContract.MemberData.COLUMN_NAME_MemberID,
                ufinDbContract.MemberData.COLUMN_NAME_Fname,
                ufinDbContract.MemberData.COLUMN_NAME_Mname,
                ufinDbContract.MemberData.COLUMN_NAME_Lname,
        };
        String Active2 = "1";

        String selection2 = ufinDbContract.MemberData.COLUMN_NAME_IsActive + "=" + "'" + Active + "'"
                + " AND " + ufinDbContract.MemberData.COLUMN_NAME_MainMemberID + "=" + "'" + String.valueOf(0) + "'"
                + " AND " + ufinDbContract.MemberData.COLUMN_NAME_GroupID + "=" + "'" + String.valueOf(lGroupID) + "'";

        Cursor c2 = db.query(
                ufinDbContract.MemberData.TABLE_NAME,  // The table to query
                projection2,                               // The columns to return
                selection2,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        try {
            if (c2 != null && c2.moveToFirst()) {
                if (c2.getCount() > 0) {
                    try {
                        int iCount = c2.getCount();
                        for (int i = 0; i < iCount; i++) {
                            long lMemberID = Long.valueOf(c2.getString(c2.getColumnIndexOrThrow(ufinDbContract.MemberData.COLUMN_NAME_MemberID)));
                            if(lMemberID != selectedCollectionMemberID) {
                                OnlinePaymentByMemberIDMaster.add(lMemberID);

                                String strFname = c2.getString(c2.getColumnIndexOrThrow(ufinDbContract.MemberData.COLUMN_NAME_Fname));
                                if ((strFname == null) || strFname.isEmpty() || strFname.equalsIgnoreCase(" ")) {
                                    strFname = "";
                                }
                                strFname = strFname.replace(" ", "");

                                String strMname = c2.getString(c2.getColumnIndexOrThrow(ufinDbContract.MemberData.COLUMN_NAME_Mname));
                                if ((strMname == null) || strMname.isEmpty() || strMname.equalsIgnoreCase(" ")) {
                                    strMname = "";
                                }
                                strMname = strMname.replace(" ", "");

                                String strLname = c2.getString(c2.getColumnIndexOrThrow(ufinDbContract.MemberData.COLUMN_NAME_Lname));
                                if ((strLname == null) || strLname.isEmpty() || strLname.equalsIgnoreCase(" ")) {
                                    strLname = "";
                                }
                                strLname = strLname.replace(" ", "");

                                String strGroupMemberName = strFname + " " + strMname + " " + strLname;
//bmp 06-Jul-26
//                                String strGroupMemberRelation = "गटसदस्य";
//                                String strGroupMember = strGroupMemberName + ":" + strGroupMemberRelation;
//                                OnlinePaymentByMaster.add(strGroupMember);
//                                OnlinePaymentByRelationMaster.add("Group Member");
//                                OnlinePaymentByRelationIDMaster.add(37L);
                                String strGroupMember = strGroupMemberName + ":" + strGroupMemberRelationMarathiDescription;
                                OnlinePaymentByMaster.add(strGroupMember);
                                OnlinePaymentByRelationMaster.add(strGroupMemberRelationDescription);
                                OnlinePaymentByRelationIDMaster.add(lGroupMemberRelationID);
//bmp 06-Jul-26
                            }
                            c2.moveToNext();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            if(c2 != null) { c2.close(); }
            db.close();
        }
    }
    boolean ValidateOnlineCollectionInfo() {
        boolean Status = false;
        OnlineCollectionProblems.clear();

        Spinner sp1 = (Spinner) rootView.findViewById(R.id.spinnerOnlinePaymentBy);
        int indexOnlinePaymentBy = sp1.getSelectedItemPosition();
        if(indexOnlinePaymentBy == 0) {
            EditText et = (EditText) rootView.findViewById(R.id.editTextOnlinePaymentByFname);
            strOnlinePaymentByFName = et.getText().toString();
            et = (EditText) rootView.findViewById(R.id.editTextOnlinePaymentByMname);
            strOnlinePaymentByMName = et.getText().toString();
            et = (EditText) rootView.findViewById(R.id.editTextOnlinePaymentByLname);
            strOnlinePaymentByLName = et.getText().toString();
            Spinner sp2 = (Spinner) rootView.findViewById(R.id.spinnerOnlinePaymentByRelation);
            int indexOnlinePaymentByRelation = sp2.getSelectedItemPosition();
            OnlinePaymentByRelationID = PaymentByOtherRelationIDMaster.get(indexOnlinePaymentByRelation);
            OnlinePaymentByMemberID = 0L;
        } else {
            OnlinePaymentByMemberID = OnlinePaymentByMemberIDMaster.get(indexOnlinePaymentBy);
            String strOnlinePaymentBy = OnlinePaymentByMaster.get(indexOnlinePaymentBy);
            String[] strSplit = strOnlinePaymentBy.split(":");
            String strOnlinePaymentByName = strSplit[0];
            OnlinePaymentByRelationID = OnlinePaymentByRelationIDMaster.get(indexOnlinePaymentBy);
            String[] strSplit2 = strOnlinePaymentByName.split(" ");
            strOnlinePaymentByFName = "";
            strOnlinePaymentByMName = "";
            strOnlinePaymentByLName = "";
            switch (strSplit2.length) {
                case 1:
                    strOnlinePaymentByFName = strSplit2[0];
                case 2:
                    strOnlinePaymentByFName = strSplit2[0];
                    strOnlinePaymentByLName = strSplit2[1];
                    break;
                case 3:
                    strOnlinePaymentByFName = strSplit2[0];
                    strOnlinePaymentByMName = strSplit2[1];
                    strOnlinePaymentByLName = strSplit2[2];
                    break;
                default:
                    strOnlinePaymentByFName = strSplit2[0];
                    for(int i=1; i<(strSplit2.length-1); i++) {
                        strOnlinePaymentByMName = strOnlinePaymentByMName + " " + strSplit2[i];
                    }
                    strOnlinePaymentByMName = strOnlinePaymentByMName.trim();
                    strOnlinePaymentByLName = strSplit2[strSplit2.length-1];
                    break;
            }
        }
        strOnlinePaymentByFName = strOnlinePaymentByFName.trim();
        strOnlinePaymentByMName = strOnlinePaymentByMName.trim();
        strOnlinePaymentByLName = strOnlinePaymentByLName.trim();
        if(strOnlinePaymentByFName.isEmpty()) {
            OnlineCollectionProblems.add("Invalid Online Payment By Name.");
        } else if(strOnlinePaymentByMName.isEmpty() && strOnlinePaymentByLName.isEmpty()) {
            OnlineCollectionProblems.add("Invalid Online Payment By Name.");
        }

//        strFilepath = mainActivity.baseDir + "/data/";
//        strFilename = mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strOnlinePaymentProof;
//        File outFile = new File(strFilepath + strFilename);
//        if(strFilename.isEmpty() || (!outFile.exists())) {
//            OnlineCollectionProblems.add("Online Payment Screenshot Photo Pending.");
//        }

//        if(strOnlinePaymentApp.isEmpty()) {
//        if(OnlinePaymentAppID == 0) {
//            OnlineCollectionProblems.add("Online Payment App Not Selected.");
//        }
        if(strOnlinePaymentTransactionID.length() != 12) {
            OnlineCollectionProblems.add("Invalid Online TransactionID.");
        } else {
            try {
                long lTemp = Long.parseLong(strOnlinePaymentTransactionID);
//bmp 30-Jun-26
                //Check for duplicate TransactionID in recent transactions
                String strDetails = "";
                for(int i=0; i<mainActivity.CollectionMemberList.size(); i++) {
                    if(strOnlinePaymentTransactionID.equalsIgnoreCase(mainActivity.CollectionMemberList.get(i).strOnlineTransactionID)) {
//bmp 09-Jul-26
                        //Allow duplicate TransactionID to allow correction to entered info,
                        //before Center collection is saved to database.
                        if(mainActivity.CollectionMemberList.get(i).MemberID == selectedCollectionMemberID)
                            continue;
//bmp 09-Jul-26
                        strDetails = "Duplicate TransactionID:" + strOnlinePaymentTransactionID + "\n"
                                + "C-" + String.valueOf(mainActivity.CollectionMemberList.get(i).CenterID) + ":"
                                + "SavingAccNo:" + mainActivity.CollectionMemberList.get(i).SavingAccNo + ":\n"
                                + mainActivity.CollectionMemberList.get(i).MemberName;
                        OnlineCollectionProblems.add(strDetails);
                        break;
                    }
                }
                if(strDetails.isEmpty()) {
                    //Check for duplicate TransactionID in all previous valid transactions in SQLite database
                    long lMemberID = SQLite_CheckDuplicateTransactionID(strOnlinePaymentTransactionID);
                    for(int i=0; i<mainActivity.CollectionMemberList.size(); i++) {
                        if(mainActivity.CollectionMemberList.get(i).MemberID == lMemberID) {
                            strDetails = "Duplicate TransactionID:" + strOnlinePaymentTransactionID + "\n"
                                    + "C-" + String.valueOf(mainActivity.CollectionMemberList.get(i).CenterID) + ":"
                                    + "SavingAccNo:" + mainActivity.CollectionMemberList.get(i).SavingAccNo + ":\n"
                                    + mainActivity.CollectionMemberList.get(i).MemberName;
                            OnlineCollectionProblems.add(strDetails);
                            break;
                        }
                    }
                }
//bmp 30-Jun-26
            } catch (NumberFormatException e) {
                OnlineCollectionProblems.add("Invalid Online TransactionID.");
            }
        }
        if(strOnlineTransactionDateForDB.isEmpty()) {
            OnlineCollectionProblems.add("Invalid Online Transaction Date.");
        } else {
            Calendar calNow = Calendar.getInstance();
            //yyyy-MM-dd Format for DB
            SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
            SimpleDateFormat sdfMonth = new SimpleDateFormat("MM", Locale.ENGLISH);
            SimpleDateFormat sdfDay = new SimpleDateFormat("dd", Locale.ENGLISH);
            String strYearNow = sdfYear.format(calNow.getTime());
            String strMonthNow = sdfMonth.format(calNow.getTime());
            String strDayNow = sdfDay.format(calNow.getTime());
            if(!strOnlineTransactionDateForDB.substring(0, 4).equalsIgnoreCase(strYearNow)) {
                OnlineCollectionProblems.add("Invalid Online Transaction Date.");
            } else if(!strOnlineTransactionDateForDB.substring(5, 7).equalsIgnoreCase(strMonthNow)) {
                OnlineCollectionProblems.add("Invalid Online Transaction Date.");
            } else {
                try {
                    int iTemp1 = Integer.parseInt(strOnlineTransactionDateForDB.substring(8, 10));
                    int iTempNow = Integer.parseInt(strDayNow);
                    if(iTemp1 > iTempNow) {
                        OnlineCollectionProblems.add("Invalid Online Transaction Date.");
                    }
                } catch (NumberFormatException e) {
                    OnlineCollectionProblems.add("Invalid Online Transaction Date.");
                }
            }
        }
//        if(strScreenShotDateForDB.isEmpty()) {
//            OnlineCollectionProblems.add("Invalid Online Transaction ScreenShot Date.");
//        } else {
//            Calendar calNow = Calendar.getInstance();
//            //yyyy-MM-dd Format for DB
//            SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
//            SimpleDateFormat sdfMonth = new SimpleDateFormat("MM", Locale.ENGLISH);
//            SimpleDateFormat sdfDay = new SimpleDateFormat("dd", Locale.ENGLISH);
//            String strYearNow = sdfYear.format(calNow.getTime());
//            String strMonthNow = sdfMonth.format(calNow.getTime());
//            String strDayNow = sdfDay.format(calNow.getTime());
//            if(!strScreenShotDateForDB.substring(0, 4).equalsIgnoreCase(strYearNow)) {
//                OnlineCollectionProblems.add("Invalid Online Transaction ScreenShot Date.");
//            } else if(!strScreenShotDateForDB.substring(5, 7).equalsIgnoreCase(strMonthNow)) {
//                OnlineCollectionProblems.add("Invalid Online Transaction ScreenShot Date.");
////bmp 28-Jul-26
////            } else if(!strScreenShotDateForDB.substring(8, 10).equalsIgnoreCase(strDayNow)) {
////                OnlineCollectionProblems.add("Invalid Online Transaction ScreenShot Date.");
////bmp 28-Jul-26
//            } else {
//                try {
//                    int iTemp1 = Integer.parseInt(strScreenShotDateForDB.substring(8, 10));
//                    int iTempNow = Integer.parseInt(strDayNow);
//                    if(iTemp1 > iTempNow) {
//                        OnlineCollectionProblems.add("Invalid Online Transaction ScreenShot Date.");
//                    }
//                } catch (NumberFormatException e) {
//                    OnlineCollectionProblems.add("Invalid Online Transaction ScreenShot Date.");
//                }
//            }
//        }
        EditText et = (EditText) rootView.findViewById(R.id.editTextOnlineTransactionAmount);
        String strOnlineTransactionAmount = et.getText().toString();
        try {
            iOnlineTransactionAmount = Integer.parseInt(strOnlineTransactionAmount);
        } catch (NumberFormatException e) {
        }
        if(iOnlineTransactionAmount == 0) {
            OnlineCollectionProblems.add("Invalid Online Payment Amount.");
        }
        RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupOnlinePaymentStatus);
        if(rg.getCheckedRadioButtonId() != R.id.radioButtonOnlinePaymentSuccessful) {
            OnlineCollectionProblems.add("Online Payment Not Successful.");
        }
//        rg = (RadioGroup) rootView.findViewById(R.id.radioGroupOnlinePaymentConfirm);
//        if(rg.getCheckedRadioButtonId() != R.id.radioButtonOnlinePaymentConfirmYes) {
//            OnlineCollectionProblems.add("Online Payment Screenshot Confirmation Pending.");
//        }

        if(OnlineCollectionProblems.isEmpty())
            Status = true;

        return Status;
    }
//    ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    // Add same code that you want to add in onActivityResult method
//                    if(result.getResultCode() == Activity.RESULT_OK) {
//                        File outFile = new File(strFilepath + strFilename);
//                        if(!outFile.exists())
//                            return;
//                        if(outFile.length() >= MAX_PHOTO_FILESIZE) {
//                            String currentPhotoPath = strFilepath + strFilename;
//                            mainActivity.AdjustImageFileSize(currentPhotoPath);
//                        }
//                        mainActivity.CollectionMemberList.get(selectedCollectionMemberIndex).strOnlinePaymentProof = strFilename;
////bmp 29-Jul-26
//                        Calendar calNow = Calendar.getInstance();
//                        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
//                        strScreenShotDate = sdf1.format(calNow.getTime());
//                        //yyyy-MM-dd Format for DB
//                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//                        strScreenShotDateForDB = sdf2.format(calNow.getTime());
////                        EditText et = (EditText) rootView.findViewById(R.id.editTextScreenshotDate);
////                        et.setText(strScreenShotDate);
////bmp 29-Jul-26
///////////////////////////////////////////////////////////////////////////////////////////////
////////bmp 23-Jul-26
//////                        mainActivity.strVisionText = "";
//////                        String currentPhotoPath = strFilepath + strFilename;
//////                        mainActivity.GetVisionText(currentPhotoPath);
////////bmp 23-Jul-26
//////bmp 23-Jul-26
////                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
////                        bmOptions.inJustDecodeBounds = true;
////                        String currentPhotoPath = strFilepath + strFilename;
////                        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
////                        // Decode the image file into a Bitmap sized to fill the View
////                        bmOptions.inJustDecodeBounds = false;
////                        bmOptions.inSampleSize = 1; //scaleFactor;
////                        bmOptions.inPurgeable = true;
////                        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
////                        InputImage image = InputImage.fromBitmap(bitmap, 0);
////                        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
////                        recognizer.process(image)
////                                .addOnSuccessListener(visionText -> {
////                                    // Task completed successfully
////                                    String extractedText = visionText.getText();
////                                    if (!extractedText.isEmpty()) {
//////                                        System.out.println(extractedText);
////                                        ParseVisionText(extractedText);
////                                        ShowdlgAlertonActivityResult(extractedText);
//////                                        if(     strOnlinePaymentTransactionID.isEmpty() ||
//////                                                strOnlineTransactionDate.isEmpty() ||
//////                                                (iOnlineTransactionAmount == 0) ) {
//////                                            ShowdlgAlertonActivityResult(extractedText);
//////                                        }
////                                    } else {
//////                                        System.out.println("No text found in the image.");
//////                                        ShowdlgAlertonActivityResult("No text found in the image.");
////                                    }
////                                })
////                                .addOnFailureListener(e -> {
////                                    // Task failed with an exception
//////                                    Toast.makeText(getContext(), "Failed to recognize text: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//////                                    ShowdlgAlertonActivityResult("Failed to recognize text: " + e.getMessage());
////                                });
//////bmp 23-Jul-26
///////////////////////////////////////////////////////////////////////////////////////////////
//                    } else if(result.getResultCode() == RESULT_CANCELED){
//                        //handle cancel
//                        ShowdlgAlertonActivityResult("CAMERA REQUEST CANCELED!");
//                    }
//                }
//            });
//    void ParseVisionText(String strMsg) {
//        strOnlinePaymentTransactionID = "";
//        iOnlineTransactionAmount = 0;
//        strOnlineTransactionDateForDB = "";
//        strOnlineTransactionDate = "";
//        Calendar calNow = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("MMM", Locale.ENGLISH);
//        String strMonth = sdf.format(calNow.getTime());
//        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
//        String strDate1 = sdf1.format(calNow.getTime());
//        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//        String strDate2 = sdf2.format(calNow.getTime());
//
//        // [0-9]{12} - match 12 numbers 0 to 9
//        String strMsg1 = strMsg.replace(" ", "");
//        Pattern pattern = Pattern.compile("[0-9]{12}");
//        Matcher matcher = pattern.matcher(strMsg1);
//        // Check if pattern matches
////bmp 28-Jul-26
//        while(matcher.find()) {
//            int iIndex = matcher.start();
//            if(strMsg1.charAt(iIndex-1) == '+') {
//                continue;
//            }
////            String strMatch = matcher.group();
////            if(strMatch.startsWith("91")) {
////                continue;
////            }
//            strOnlinePaymentTransactionID = matcher.group();
//            EditText et = (EditText) rootView.findViewById(R.id.editTextTransactionID);
//            et.setText(strOnlinePaymentTransactionID);
//            break;
//        }
////bmp 28-Jul-26
//        strMsg1 = strMsg1.toUpperCase();
//        Pattern pattern_date = Pattern.compile("[0-9]{2}[A-Z]{3}[0-9]{4}");
//        matcher = pattern_date.matcher(strMsg1);
//        while(matcher.find()) {
//            String strMatch = matcher.group();
//            if(!strMatch.substring(2, 5).equalsIgnoreCase(strDate1.substring(3, 6)))
//                continue;
//            if(!strMatch.substring(5).equalsIgnoreCase(strDate1.substring(7)))
//                continue;
//            strDate1 = strMatch.substring(0, 2) + strDate1.substring(2);
//            strOnlineTransactionDate = strDate1;
////            EditText et = (EditText) rootView.findViewById(R.id.editTextTransactionDate);
////            et.setText(strDate1);
//            strDate2 = strDate2.substring(0, 8) + strMatch.substring(0, 2);
//            //yyyy-MM-dd Format for DB
//            strOnlineTransactionDateForDB = strDate2;
//            break;
//        }
//
//        strMsg = strMsg.replace(" ", "");
//        strMsg = strMsg.replace("\n", " ");
//        String[] strSplit = strMsg.split(" ");
//        for(int i=0; i<strSplit.length; i++) {
//            String strTemp = strSplit[i].replace(",", "");
//            if((iOnlineTransactionAmount == 0) && (strTemp.length() < 10)) {
//                if(!strTemp.startsWith("+")) {
//                    if (strTemp.startsWith("\u20B9")) {
//                        strTemp = strTemp.substring(1);
//                    }
//                    strTemp = strTemp.toUpperCase();
//                    // [A-Z]{1} - match 1 literal which can be A to Z
//                    Pattern pattern2 = Pattern.compile("[A-Z]{1}");
//                    Matcher matcher2 = pattern2.matcher(strTemp.substring(0, 1));
//                    if (matcher2.matches()) {
//                        strTemp = strTemp.substring(1);
//                    }
//                    try {
//                        iOnlineTransactionAmount = Integer.parseInt(strTemp);
//                        EditText et = (EditText) rootView.findViewById(R.id.editTextOnlineTransactionAmount);
//                        et.setText(strTemp);
//                    } catch (NumberFormatException e) {
//                        System.out.println(e.getMessage());
//                    }
//                }
//            }
//            if(strOnlineTransactionDate.isEmpty() && strTemp.equalsIgnoreCase(strMonth)) {
//                if( (i > 0) && (strSplit[i-1].length() == 2) ) {
//                    try {
//                        int iDay = Integer.parseInt(strSplit[i-1]);
//                        strDate1 = strSplit[i-1] + strDate1.substring(2);
//                        strOnlineTransactionDate = strDate1;
////                        EditText et = (EditText) rootView.findViewById(R.id.editTextTransactionDate);
////                        et.setText(strDate1);
//                        strDate2 = strDate2.substring(0, 8) + strSplit[i-1];
//                        //yyyy-MM-dd Format for DB
//                        strOnlineTransactionDateForDB = strDate2;
//                    } catch (NumberFormatException e) {
//                        System.out.println(e.getMessage());
//                    }
//                }
//            }
//            if((iOnlineTransactionAmount != 0) && !strOnlineTransactionDateForDB.isEmpty()) {
//                break;
//            }
//        }
//    }
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
    final DatePickerDialog.OnDateSetListener datepick = new DatePickerDialog.OnDateSetListener() {
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

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            String strDate = sdf.format(myCalendar.getTime());

            if(iSelectDate == 0) {
//                EditText et = (EditText) rootView.findViewById(R.id.editTextScreenshotDate);
//                et.setText(strDate);
//                strScreenShotDate = strDate;
                //yyyy-MM-dd Format for DB
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//                strScreenShotDateForDB = sdf2.format(myCalendar.getTime());
            } else if(iSelectDate == 1) {
//                EditText et = (EditText) rootView.findViewById(R.id.editTextTransactionDate);
//                et.setText(strDate);
                strOnlineTransactionDate = strDate;
                //yyyy-MM-dd Format for DB
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                strOnlineTransactionDateForDB = sdf2.format(myCalendar.getTime());
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
                        case ufin_ONLINE_COLLECTION_DIALOG_STATE_NONE:
                            break;
                        case ufin_ONLINE_COLLECTION_DIALOG_STATE_INVALID_INFO:
                            break;
                    }
                    dlgState = ufin_ONLINE_COLLECTION_DIALOG_STATE_NONE;
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
                        case ufin_ONLINE_COLLECTION_DIALOG_STATE_NONE:
                            break;
                        case ufin_ONLINE_COLLECTION_DIALOG_STATE_INVALID_INFO:
                            break;
                    }
                    dlgState = ufin_ONLINE_COLLECTION_DIALOG_STATE_NONE;
                }
            });
        }
//        dlgAlert.setCancelable(true);
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
        RelationDescriptionMaster.clear();
        RelationIDMaster.add(0L);
        RelationMarathiDescriptionMaster.add("");
        RelationDescriptionMaster.add("");

        PaymentByOtherRelationIDMaster.clear();
        PaymentByOtherRelationMarathiDescriptionMaster.clear();
        PaymentByOtherRelationDescriptionMaster.clear();
        PaymentByOtherRelationIDMaster.add(0L);
        PaymentByOtherRelationMarathiDescriptionMaster.add("");
        PaymentByOtherRelationDescriptionMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
//                            RelationIDMaster.add(Long.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.RelationMaster.COLUMN_NAME_RelationID))));
                            long lRelationID = Long.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.RelationMaster.COLUMN_NAME_RelationID)));
                            RelationIDMaster.add(lRelationID);
                            String str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.RelationMaster.COLUMN_NAME_MarathiDescription));
                            if (str1 == null) {
                                str1 = "?";
                            }
                            RelationMarathiDescriptionMaster.add(str1);
                            String str2 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.RelationMaster.COLUMN_NAME_Description));
                            if(str2.equalsIgnoreCase("Main Member")) {
                                RelationIndexMainMember = RelationIDMaster.size() - 1;
                                RelationIDMainMember = RelationIDMaster.get(RelationIndexMainMember);
                            } else if(str2.equalsIgnoreCase("Friend or Colleague_No Family Relation")) {
                                PaymentByOtherRelationIDMaster.add(lRelationID);
                                PaymentByOtherRelationMarathiDescriptionMaster.add(str1);
                                PaymentByOtherRelationDescriptionMaster.add(str2);
                            } else if(str2.equalsIgnoreCase("Other Relative_Not Staying in the Same Household")) {
                                PaymentByOtherRelationIDMaster.add(lRelationID);
                                PaymentByOtherRelationMarathiDescriptionMaster.add(str1);
                                PaymentByOtherRelationDescriptionMaster.add(str2);
//bmp 06-Jul-26
                            } else if(str2.equalsIgnoreCase("Employer")) {
                                PaymentByOtherRelationIDMaster.add(lRelationID);
                                PaymentByOtherRelationMarathiDescriptionMaster.add(str1);
                                PaymentByOtherRelationDescriptionMaster.add(str2);
                            } else if(str2.equalsIgnoreCase("Group Member")) {
                                lGroupMemberRelationID = lRelationID;
                                strGroupMemberRelationMarathiDescription = str1;
                                strGroupMemberRelationDescription = str2;
//bmp 06-Jul-26
                            }
                            RelationDescriptionMaster.add(str2);
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
    public long SQLite_CheckDuplicateTransactionID(String strTransactionID) {
        long lMemberID = 0;

        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.MemberTransaction.COLUMN_NAME_MemberID,
        };
        String Active = "1";
        String selection1 = ufinDbContract.MemberTransaction.COLUMN_NAME_TransactionStatus + "=" + "'" + Active + "'"
                + " AND " + ufinDbContract.MemberTransaction.COLUMN_NAME_OnlineTransactionID + "=" + "'" + strTransactionID + "'";
        Cursor c1 = db.query(
                ufinDbContract.MemberTransaction.TABLE_NAME,  // The table to query
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
                        lMemberID = Long.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.MemberTransaction.COLUMN_NAME_MemberID)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            if(c1 != null) { c1.close(); }
            db.close();
        }

        return lMemberID;
    }
//    public void SQLite_getOnlinePaymentAppMaster() {
//        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
//        // Gets the data repository in write mode
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//
//        // Define a projection that specifies which columns from the database
//        // you will actually use after this query.
//        String[] projection1 = {
//                ufinDbContract.OnlinePaymentApps.COLUMN_NAME_OnlinePaymentAppID,
//                ufinDbContract.OnlinePaymentApps.COLUMN_NAME_Description,
//        };
//        String Active = "1";
//        String selection1 = ufinDbContract.OnlinePaymentApps.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";
//        Cursor c1 = db.query(
//                ufinDbContract.OnlinePaymentApps.TABLE_NAME,  // The table to query
//                projection1,                               // The columns to return
//                selection1,                                // The columns for the WHERE clause
//                null,
//                null,                                     // don't group the rows
//                null,                                     // don't filter by row groups
//                null                                 // The sort order
//        );
//
//        OnlinePaymentAppsIDMaster.clear();
//        OnlinePaymentAppsMaster.clear();
//        OnlinePaymentAppsIDMaster.add(0L);
//        OnlinePaymentAppsMaster.add("");
//
//        try {
//            if (c1 != null && c1.moveToFirst()) {
//                if (c1.getCount() > 0) {
//                    try {
//                        int iCount = c1.getCount();
//                        for (int i = 0; i < iCount; i++) {
//                            OnlinePaymentAppsIDMaster.add(Long.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.OnlinePaymentApps.COLUMN_NAME_OnlinePaymentAppID))));
//                            OnlinePaymentAppsMaster.add(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.OnlinePaymentApps.COLUMN_NAME_Description)));
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
//bmp 23-Sep-26
    private class AsyncCallWS_CancelPayment extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarOnlineCollection);
            if (pbSync != null) {
                pbSync.setVisibility(View.VISIBLE);
            }
            EditText et = (EditText) rootView.findViewById(R.id.editTextOnlineTransactionAmount);
            et.setEnabled(false);
            Button bt = (Button) rootView.findViewById(R.id.buttonGenerateQR);
            bt.setEnabled(false);
            bt = (Button) rootView.findViewById(R.id.buttonSave);
            bt.setEnabled(false);

            qrString = "";
            ImageView imgview = (ImageView) rootView.findViewById(R.id.imageViewQR);
            imgview.setImageBitmap(null);
            TextView tv = (TextView) rootView.findViewById(R.id.textViewQRTimeout);
            tv.setText("");
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
        @Override
        protected Void doInBackground(String... strings) {
            CancelTransaction();
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarOnlineCollection);
            if (pbSync != null) {
                pbSync.setVisibility(View.INVISIBLE);
            }
            EditText et = (EditText) rootView.findViewById(R.id.editTextOnlineTransactionAmount);
            et.setEnabled(true);
            Button bt = (Button) rootView.findViewById(R.id.buttonGenerateQR);
            bt.setEnabled(true);
            flagCancel = false;
            if(flagClose) {
                flagClose = false;
                CustomViewPager viewPager1 = (CustomViewPager) mainActivity.findViewById(R.id.viewpagerCollection);
                if (viewPager1 != null) {
                    viewPager1.requestFocus();
                    mainActivity.iCurrentScreen = 1;
                    viewPager1.setCurrentItem(mainActivity.iCurrentScreen);
                }
            }
        }
    }
    private class AsyncCallWS_InitiatePayment extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarOnlineCollection);
            if (pbSync != null) {
                pbSync.setVisibility(View.VISIBLE);
            }
            EditText et = (EditText) rootView.findViewById(R.id.editTextOnlineTransactionAmount);
            et.setEnabled(false);
            Button bt = (Button) rootView.findViewById(R.id.buttonGenerateQR);
            bt.setEnabled(false);
            bt = (Button) rootView.findViewById(R.id.buttonSave);
            bt.setEnabled(false);
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
        @Override
        protected Void doInBackground(String... strings) {
            access_token = "";
            dlgState = ufin_ONLINE_COLLECTION_DIALOG_STATE_FETCH_TOKEN;
            FetchToken();
            if(dlgState == ufin_ONLINE_COLLECTION_DIALOG_STATE_TRANSACTION_INITIATE) {
                strOnlinePaymentTransactionID = "";
                iOnlineTransactionAmount = 0;
                strOnlineTransactionDate = "";
                strOnlinePaymentUPIID = "";
    //                strOnlinePaymentApp = "";
    //                OnlinePaymentAppID = 0L;
                qrString = "";
                InitiateTransaction();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            if (dlgState == ufin_ONLINE_COLLECTION_DIALOG_STATE_TRANSACTION_SHOW_QR) {
    //                    qrString = "000201010212021645851910449489500415545080003565971061661000100356596840822SBIN002044844709819075111531090003517802526560010A0000005240138HSBIMOPAD.21624198-02MOM0000004199@SBI27770010A0000005240123526032407363520877295010232https://www.hitachi-payments.com28180010A0000005240100520493995303356540553.005802IN5922KALYANA KARNATAKA ROAD6008GULBARGA61065851056258031502MOM000000419905235260324073635208772950107082162419863043683";
    //                Bitmap qrBitmap = generateQrCode(qrString, 400, 400);
                Bitmap qrBitmap = generateQrCode(qrString, 300, 300);
                if (qrBitmap != null) {
                    ImageView imgview = (ImageView) rootView.findViewById(R.id.imageViewQR);
                    imgview.setImageBitmap(qrBitmap);
                }

                iQRExpires_in = 300;    //Hardcoded for testing
                Calendar calStart = Calendar.getInstance();
                long lStart = calStart.getTimeInMillis();
                int iCountDown = iQRExpires_in;
                TextView tv = (TextView) rootView.findViewById(R.id.textViewQRTimeout);
                String strMsg = "QR Timeout Sec: " + String.valueOf(iCountDown);
                tv.setText(strMsg);

                AsyncCallWS_CheckTransactionStatus taskCheckTransactionStatus = new AsyncCallWS_CheckTransactionStatus();
                taskCheckTransactionStatus.execute();
            } else {
                ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarOnlineCollection);
                if (pbSync != null) {
                    pbSync.setVisibility(View.INVISIBLE);
                }
                dlgState = ufin_ONLINE_COLLECTION_DIALOG_STATE_NONE;
                EditText et = (EditText) rootView.findViewById(R.id.editTextOnlineTransactionAmount);
                et.setEnabled(true);
                Button bt = (Button) rootView.findViewById(R.id.buttonGenerateQR);
                bt.setEnabled(true);
            }
        }
    }

    Handler QRTimeoutHandler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle messageData = msg.getData();
            int iCountDown = messageData.getInt("TimeoutSec");
            switch (msg.what) {
                case 1:
                    TextView tv = (TextView) rootView.findViewById(R.id.textViewQRTimeout);
                    String strMsg = "QR Timeout Sec: " + String.valueOf(iCountDown);
                    tv.setText(strMsg);
                    break;
            }
        }
    };

    private class AsyncCallWS_CheckTransactionStatus extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            TextView tv = (TextView) rootView.findViewById(R.id.textViewQRTimeout);
            String strMsg = "QR Timeout Sec: " + String.valueOf(iQRExpires_in);
            tv.setText(strMsg);
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
        @Override
        protected Void doInBackground(String... strings) {
            Calendar calStart = Calendar.getInstance();
            long lStart = calStart.getTimeInMillis();
            int iCountDown = iQRExpires_in;
            int iLastStatusCheck = iCountDown;

            dlgState = ufin_ONLINE_COLLECTION_DIALOG_STATE_TRANSACTION_WAIT;
            while (dlgState == ufin_ONLINE_COLLECTION_DIALOG_STATE_TRANSACTION_WAIT) {
                Calendar calNow = Calendar.getInstance();
                long lNow = calNow.getTimeInMillis();
                long lCountDown = lNow - lStart;
                lCountDown = (lCountDown / 1000);
                iCountDown = iQRExpires_in - (int) lCountDown;

                Message msg;
                Bundle messageData;
                msg = Message.obtain(QRTimeoutHandler);
                messageData = new Bundle();
                msg.what = 1;
                messageData.putInt("TimeoutSec", iCountDown);
                msg.setData(messageData);
                QRTimeoutHandler.sendMessage(msg);

                if(iCountDown <= 0) {
                    dlgState = ufin_ONLINE_COLLECTION_DIALOG_STATE_TRANSACTION_TIMEOUT;
                    break;
                }
                if(flagCancel || flagClose) {
                    dlgState = ufin_ONLINE_COLLECTION_DIALOG_STATE_TRANSACTION_CANCEL;
                    break;
                }
                if ((iLastStatusCheck - iCountDown) >= 20) {
                    CheckStatus();
                    iLastStatusCheck = iCountDown;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarOnlineCollection);
            if (pbSync != null) {
                pbSync.setVisibility(View.INVISIBLE);
            }
            qrString = "";
            ImageView imgview = (ImageView) rootView.findViewById(R.id.imageViewQR);
            imgview.setImageBitmap(null);
            TextView tv = (TextView) rootView.findViewById(R.id.textViewQRTimeout);
            tv.setText("");
            if (dlgState == ufin_ONLINE_COLLECTION_DIALOG_STATE_TRANSACTION_SUCCESSFUL) {
                System.out.println("Transaction Successful");
                tv = (TextView) rootView.findViewById(R.id.textViewQRTimeout);
                String strMsg = "Online Transaction Successful.";
                tv.setText(strMsg);
                tv = (TextView) rootView.findViewById(R.id.textViewTransactionID);
                tv.setText(strOnlinePaymentTransactionID);
                tv = (TextView) rootView.findViewById(R.id.textViewTransactionDate);
                tv.setText(strOnlineTransactionDate);
                if(strOnlinePaymentTransactionID.isEmpty()) {
                    RadioGroup rgStatus = (RadioGroup) rootView.findViewById(R.id.radioGroupOnlinePaymentStatus);
                    rgStatus.clearCheck();
                } else {
                    RadioButton rb = (RadioButton) rootView.findViewById(R.id.radioButtonOnlinePaymentSuccessful);
                    rb.setChecked(true);
                }
                tv = (TextView) rootView.findViewById(R.id.textViewUPIID);
                tv.setText(strOnlinePaymentUPIID);
                Button bt = (Button) rootView.findViewById(R.id.buttonSave);
                bt.setEnabled(true);
//                String[] strSplit = strUPIID.split("@");
//                if(strSplit.length > 1) {
//                    strOnlinePaymentApp = strSplit[1];
//                }
//                boolean flagOther = true;
//                for(int i=0; i<OnlinePaymentAppsMaster.size(); i++) {
//                    if(OnlinePaymentAppsMaster.get(i).equalsIgnoreCase(strOnlinePaymentApp)) {
//                        OnlinePaymentAppID = OnlinePaymentAppsIDMaster.get(i);
//                        flagOther = false;
//                        break;
//                    }
//                }
//                if(flagOther) {
//                    strOnlinePaymentApp = "Other";
//                    OnlinePaymentAppID = 5L;
//                    for(int i=0; i<OnlinePaymentAppsMaster.size(); i++) {
//                        if(OnlinePaymentAppsMaster.get(i).equalsIgnoreCase(strOnlinePaymentApp)) {
//                            OnlinePaymentAppID = OnlinePaymentAppsIDMaster.get(i);
//                            break;
//                        }
//                    }
//                }
//                tv = (TextView) rootView.findViewById(R.id.textViewPaymentApp);
//                tv.setText(strOnlinePaymentApp);
            } else if (dlgState == ufin_ONLINE_COLLECTION_DIALOG_STATE_TRANSACTION_TIMEOUT) {
                System.out.println("Transaction Timeout");
                tv = (TextView) rootView.findViewById(R.id.textViewQRTimeout);
                String strMsg = "Online Transaction Timeout.";
                tv.setText(strMsg);
                EditText et = (EditText) rootView.findViewById(R.id.editTextOnlineTransactionAmount);
                et.setEnabled(true);
                Button bt = (Button) rootView.findViewById(R.id.buttonGenerateQR);
                bt.setEnabled(true);
            } else if (dlgState == ufin_ONLINE_COLLECTION_DIALOG_STATE_TRANSACTION_CANCEL) {
                System.out.println("Transaction Cancel");
                tv = (TextView) rootView.findViewById(R.id.textViewQRTimeout);
                String strMsg = "Online Transaction Cancel.";
                tv.setText(strMsg);
                AsyncCallWS_CancelPayment taskCancelPayment = new AsyncCallWS_CancelPayment();
                taskCancelPayment.execute();
            }
        }
    }
    void FetchToken() {
        String strRequest = "client_id=" + strClientID + "&client_secret=" + strClientSecret + "&grant_type=client_credentials";

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, strRequest);
        Request request = new Request.Builder()
                .url("https://auth.mod91.io/realms/mif/protocol/openid-connect/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String strResponse = response.body().string();
            System.out.println(strResponse);
//            {
//                "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI3Wk9qYjIwdG5qOWwwN2Jzd3BJYlJ4OVhycFlOTzZkODNwQ2JtckpmM2RBIn0.eyJleHAiOjE3OTAzNTk5ODMsImlhdCI6MTc5MDMyMzk4MywianRpIjoidHJydGNjOmI2ZDhhMjhmLTJlYTAtZmJjMC01ZDk1LTFkMDcwMDQzZjFlMSIsImlzcyI6Imh0dHBzOi8vYXV0aC5tb2Q5MS5pby9yZWFsbXMvbWlmIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjQ0MmE2OGEwLWU4M2UtNGVmNS1iYTlmLWI0ZGZjNjc0ODdlOCIsInR5cCI6IkJlYXJlciIsImF6cCI6IklUMTI0NyIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWlmIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7IklUMTI0NyI6eyJyb2xlcyI6WyJ1bWFfcHJvdGVjdGlvbiJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJjbGllbnRIb3N0IjoiNTguODQuNjEuNjUiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzZXJ2aWNlLWFjY291bnQtaXQxMjQ3IiwiY2xpZW50QWRkcmVzcyI6IjU4Ljg0LjYxLjY1IiwiY2xpZW50X2lkIjoiSVQxMjQ3In0.Fi8dSPLkkS4UFbzKN_AM1_LlrCvt42LkzM7re7e0NLSPEPafD2a3S-wU8Yg8VKXKK4VXhhDnTpQvypiCCkFnI1nQaf0eVxb-JFu3uTmvMX3pwhk4AH8p_znSR43B6lH1bSgNZqzqoI4P7qjLzDoFQOItZfH74W1-bhtb61MtTckSjOLBqlmGlJYW552WjGF7C2OuBOq6dOWQkknJqZ5-7GwJVtZAxUrqqR7V48WFBH_q5FYLTEJjlmSpwoyxtdA1cakLAPozJg3vCil3-VAoUcEhSr49pnXU4CMF3mrVjJIOf90Zz8LS0kiwpygdBs2D_Cqb_Xce90wP12Xx3A1Bug",
//                    "expires_in": 36000,
//                    "refresh_expires_in": 0,
//                    "token_type": "Bearer",
//                    "not-before-policy": 0,
//                    "scope": "profile email"
//            }
            try {
                JSONObject jsonObject = new JSONObject(strResponse);
                access_token = jsonObject.getString("access_token");
                itokenExpires_in = jsonObject.getInt("expires_in");
                dlgState = ufin_ONLINE_COLLECTION_DIALOG_STATE_TRANSACTION_INITIATE;
            } catch (JSONException e) {
//                throw new RuntimeException(e);
                System.out.println(e.getMessage());
                dlgState = ufin_ONLINE_COLLECTION_DIALOG_STATE_INVALID_CLIENT;
            }
        } catch (IOException e) {
//            throw new RuntimeException(e);
            System.out.println(e.getMessage());
            dlgState = ufin_ONLINE_COLLECTION_DIALOG_STATE_INVALID_CLIENT;
        }
    }
    void InitiateTransaction() {
//Hardcoded for testing
        strTransactionAmount = "1.00";

        Calendar calNow = Calendar.getInstance();
        long lNow = calNow.getTimeInMillis();
        strInvoiceNo = String.valueOf(mainActivity.OrgID)
                        + String.valueOf(mainActivity.LSO_ID)
                        + String.valueOf(lNow);
        String strRequest = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", mainActivity.strPaymentGatewayUsername);
            jsonObject.put("password", mainActivity.strPaymentGatewayPassword);
            jsonObject.put("invoiceNo", strInvoiceNo);
            jsonObject.put("transactionAmount", strTransactionAmount);
            jsonObject.put("additionalAmount", "0.00");
            jsonObject.put("transactionType", 22);
            jsonObject.put("txnRef1", mainActivity.BranchName);
//            jsonObject.put("txnRef2", mainActivity.LoginID);
            jsonObject.put("txnRef2", mainActivity.LSO_NodeName);
            jsonObject.put("txnRef3", strSavingAccNo);
            jsonObject.put("txnRef4", strName);
            strRequest = jsonObject.toString();
        } catch (JSONException e) {
//            throw new RuntimeException(e);
            System.out.println(e.getMessage());
        }
        String strToken = "Bearer " + access_token;
        String strParam = "clientid=" + strClientID + "&journey=initiatetransaction";
        String strUrl = "https://hermes.mod91.io/mif/v1/process?" + strParam;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, strRequest);
        Request request = new Request.Builder()
                .url(strUrl)
                .method("POST", body)
                .addHeader("Authorization", strToken)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String strResponse = response.body().string();
            System.out.println(strResponse);
//        {
//            "statusCode": "100",
//                "message": "Request has been served",
//                "data": {
//            "resultCode": "200",
//                    "resultMessage": "Request processed successfully",
//                    "referenceId": "REF-5e9c573d09524ee18dd02c9945f8dd4a",
//                    "qrString": " upi://pay?pa=0790646A0217269.bqr@k....."
//        }
//        }

//            {
//                "data" : {
//                "errorCode" : "105",
//                        "errorMessage" : "device not found or not active.",
//                        "referenceId" : "1752041790347521534",
//                        "qrData" : "upi://pay?pa=mosambee.17891352014960562@pineaxis&pn=Ultimatemile%20India%20Private%20Limited&am=1.00&cu=INR&mc=5046&tr=SU01M3CGEX9DH5A7MWWQFZ91NGVG",
//                        "qrReferenceId" : "01M3CGEX9DH5A7MWWQFZ91NGVG"
//            },
//                "message" : "Request has been served",
//                    "statusCode" : "100"
//            }

            try {
                jsonObject = new JSONObject(strResponse);
                String strStatusCode = jsonObject.getString("statusCode");
                if(strStatusCode.equalsIgnoreCase("100")) {
                    JSONObject jsonData = (JSONObject) jsonObject.get("data");
                    referenceId = jsonData.getString("referenceId");
                    qrString = jsonData.getString("qrData");
                    QRreferenceId = jsonData.getString("qrReferenceId");
                    dlgState = ufin_ONLINE_COLLECTION_DIALOG_STATE_TRANSACTION_SHOW_QR;
                } else {
                    dlgState = ufin_ONLINE_COLLECTION_DIALOG_STATE_INVALID_CLIENT;
                }
            } catch (JSONException e) {
//                throw new RuntimeException(e);
                System.out.println(e.getMessage());
                dlgState = ufin_ONLINE_COLLECTION_DIALOG_STATE_INVALID_CLIENT;
            }
        } catch (IOException e) {
//            throw new RuntimeException(e);
            System.out.println(e.getMessage());
            dlgState = ufin_ONLINE_COLLECTION_DIALOG_STATE_INVALID_CLIENT;
        }
    }
    void CheckStatus() {
        String strRequest = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", mainActivity.strPaymentGatewayUsername);
//As per Hermes API Specification V2.2 PDF Initiate Transaction API Response Body Field data.referenceId
//            jsonObject.put("referenceId", QRreferenceId);
//As per discussion with Mosambee Technical Team
            jsonObject.put("referenceId", referenceId);
            strRequest = jsonObject.toString();
        } catch (JSONException e) {
//            throw new RuntimeException(e);
            System.out.println(e.getMessage());
        }
        String strToken = "Bearer " + access_token;
        String strParam = "clientid=" + strClientID + "&journey=checkstatus";
        String strUrl = "https://hermes.mod91.io/mif/v1/process?" + strParam;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, strRequest);
        Request request = new Request.Builder()
                .url(strUrl)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", strToken)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String strResponse = response.body().string();
            System.out.println(strResponse);
//        {
//            "statusCode": "100",
//                "message": "Request has been served",
//                "data": {
//            "transactions": [
//            {
//                "transactionId": "32445",
//                    "tipAmount": "0.00",
//                    "mmutId": "2774644",
//                    "pan": "7596DA86CE2BE637CFCFCE00B83B7B2C962DD5595111F6B7684E8792659BF895",
//                    "tgName": "GPHYP",
//                    "settlementStatus": "1",
//                    "typeId": "1",
//                    "referenceTransactionId": "32445",
//                    "securityToken": "Sakinaka",
//                    "clientId": "Renbo Hospital",
//                    "merchantId": "581007000087869",
//                    "tempTerminalId": "81030612",
//                    "DCCOptIn": "0",
//                    "pinStatus": "PIN VERIFIED OK \n SIGNATURE NOT REQUIRED",
//                    "divisionID": "2774644",
//                    "userId": "206174",
//                    "userName": "BS665485",
//                    "doingBusinessAs": "Renbo Hospital",
//                    "amount": "1.02",
//                    "addrId": "42663",
//                    "firstName": "BS665485",
//                    "merchantCode": "581007000087869",
//                    "businessName": "Renbo Hospital",
//                    "address1": "sakinaka",
//                    "transactionTime": "2026-01-05 17:05:53.0",
//                    "transactionLat": "19.10230333",
//                    "transactionLong": "72.886025",
//                    "stan": "000952",
//                    "retrievalReferenceNumber": "004455976940",
//                    "transactionAuthCode": "035058",
//                    "transactionCardNumber": "XXXXXXXXXXXX2255",
//                    "invoiceNumber": "000626",
//                    "acquirerName": "BHARATPE",
//                    "transactionStatus": "Transaction Approved",
//                    "responseCode": "00",
//                    "narration": "14996",
//                    "cardHolderName": "ONKAR PURANIK /",
//                    "transactionTypeName": "Sale",
//                    "aid": "A0000000041010",
//                    "ici": "01",
//                    "apn": "4D617374657263617264",
//                    "appLabel": "4D617374657263617264",
//                    "tvr": "0000048000",
//                    "tempTID": "81030612",
//                    "terminalId": "81030612",
//                    "receivingInstitutionCode": "GP",
//                    "billNumber": "0000000000121",
//                    "cardTypeName": "MASTERCARD",
//                    "description": "Transaction Approved",
//                    "tsi": "E800",
//                    "ac": "DF397B1172A57F81",
//                    "cid": "40",
//                    "modeId": "1",
//                    "appVersion": "2.1.73S228",
//                    "acqID": "75",
//                    "pinValidation": "1",
//                    "authAmount": " ",
//                    "drCrCode": "DC",
//                    "currencyCode": "INR",
//                    "tempMerchantCode": "581007000087869",
//                    "emiDiscount": "0",
//                    "emiTotalDiscount": "0",
//                    "emiCashback": "0",
//                    "emiTotalCashback": "0",
//                    "fullCardType": "Premium",
//                    "custMaskedCardNumber":
//                "7596DA86CE2BE637CFCFCE00B83B7B2C962DD5595111F6B7684E8792659BF895",
//                        "merchantTIDFlag": "1",
//                    "custTIDFlag": "1",
//                    "merchantMIDFlag": "1",
//                    "custMIDFlag": "1",
//                    "ref6": "Z8P9AK5N77Q0M4DP063",
//                    "drCrName": "Domestic Credit",
//                    "referenceTxnID": "32445",
//                    "approvalCode": "035058",
//                    "acquirerLogo": "BHARATPE",
//                    "transactionType": "1",
//                    "statusCode": "2",
//                    "expiryDate": "**/**",
//                    "cardType": "MASTERCARD",
//                    "currency": "INR",
//                    "tempMerchantId": "581007000087869",
//                    "applicationId": "A0000000041010",
//                    "issuerCTI": "01",
//                    "merchantTidVisibility": "1",
//                    "custTidVisibility": "1",
//                    "merchantMidVisibility": "1",
//                    "custMidVisibility": "1",
//                    "appPreName": "4D617374657263617264",
//                    "isPinVerified": "true",
//                    "convienceAmount": "0.00",
//                    "taxAmount": "0.00",
//                    "tipOption": "0",
//                    "creditDebitCardType": "DC",
//                    "creditDebitCardTypeName": "Domestic Credit",
//                    "transactionMode": "CHIP",
//                    "refTransactionType": "0",
//                    "result": "Success",
//                    "date": "05-01-2026",
//                    "time": "17:05:55",
//                    "longitude": "72.886025",
//                    "latitude": "19.10230333",
//                    "cardNumber": "XXXXXXXXXXXX2255",
//                    "batchNumber": "000066",
//                    "totalAmount": "1.02",
//                    "decimalPlaces": "2",
//                    "divisionUnit": "100",
//                    "acptModeId": "0",
//                    "acptModeName": "FULL_SWIPE",
//                    "entModeId": "1",
//                    "entModeName": "PHYSICAL CARD",
//                    "confStatus": "NOT APPLICABLE",
//                    "partnerRefNo": "0",
//                    "ssName": "UNSETTLED"
//            }
//],
//            "referenceId": "0000000000121"
//        }}
            try {
                jsonObject = new JSONObject(strResponse);
                JSONObject jsonData = jsonObject.getJSONObject("data");
                try {
                    JSONArray jarrTransactions = jsonData.getJSONArray("transactions");
                    String strRefID = jsonData.getString("referenceId");
                    if(jarrTransactions.length() > 0) {
                        JSONObject jsonData1 = jarrTransactions.getJSONObject(0);
                        System.out.println(jsonData1.toString());
                        String strStatusCode = jsonData1.getString("statusCode");
                        if(strRefID.equalsIgnoreCase(referenceId) && strStatusCode.equalsIgnoreCase("2")) {   //Approved
                            strOnlinePaymentTransactionID = jsonData1.getString("retrievalReferenceNumber");

                            String strDate = jsonData1.getString("date");
                            int iYear = Integer.parseInt(strDate.substring(6));
                            int iMonth = Integer.parseInt(strDate.substring(3, 5));
                            int iDay = Integer.parseInt(strDate.substring(0, 2));
                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.YEAR, iYear);
                            cal.set(Calendar.MONTH, (iMonth - 1));
                            cal.set(Calendar.DAY_OF_MONTH, iDay);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                            strOnlineTransactionDate = sdf.format(cal.getTime());
                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                            strOnlineTransactionDateForDB = sdf2.format(cal.getTime());
//                            strScreenShotDateForDB = strOnlineTransactionDateForDB;

                            strTransactionAmount = jsonData1.getString("totalAmount");
                            if(!strTransactionAmount.isEmpty()) {
                                float flTemp = Float.parseFloat(strTransactionAmount);
                                iOnlineTransactionAmount = (int) flTemp;
                            }

                            strOnlinePaymentUPIID = jsonData1.getString("cardNumber");

                            dlgState = ufin_ONLINE_COLLECTION_DIALOG_STATE_TRANSACTION_SUCCESSFUL;
                        }
                    }
                } catch (JSONException e) {
                    System.out.println(e.getMessage());
                    dlgState = ufin_ONLINE_COLLECTION_DIALOG_STATE_TRANSACTION_WAIT;
                }
            } catch (JSONException e) {
//                throw new RuntimeException(e);
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
//            throw new RuntimeException(e);
            System.out.println(e.getMessage());
        }
    }
    void CancelTransaction() {
        Calendar calNow = Calendar.getInstance();
        long lNow = calNow.getTimeInMillis();
        strInvoiceNo = String.valueOf(mainActivity.OrgID)
                + String.valueOf(mainActivity.LSO_ID)
                + String.valueOf(lNow);
        String strRequest = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", mainActivity.strPaymentGatewayUsername);
            jsonObject.put("referenceId", referenceId);
            strRequest = jsonObject.toString();
        } catch (JSONException e) {
//            throw new RuntimeException(e);
            System.out.println(e.getMessage());
        }
        String strToken = "Bearer " + access_token;
        String strParam = "clientid=" + strClientID + "&journey=canceltransaction";
        String strUrl = "https://hermes.mod91.io/mif/v1/process?" + strParam;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, strRequest);
        Request request = new Request.Builder()
                .url(strUrl)
                .method("POST", body)
                .addHeader("Authorization", strToken)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String strResponse = response.body().string();
            System.out.println(strResponse);
//{
//"statusCode": "100",
//"message": "Request has been served",
//"data": {
//"transactions": [
//{
//"statusCode": "1"
//}
//],
//"referenceId": "132155"
//}
//}
            try {
                jsonObject = new JSONObject(strResponse);
                String strStatusCode = jsonObject.getString("statusCode");
                if(strStatusCode.equalsIgnoreCase("100")) {
                    JSONObject jsonData = (JSONObject) jsonObject.get("data");
                    String stRefId = jsonData.getString("referenceId");
                    if(stRefId.equalsIgnoreCase(referenceId)) {
                        dlgState = ufin_ONLINE_COLLECTION_DIALOG_STATE_TRANSACTION_CANCEL;
                    }
                }
            } catch (JSONException e) {
//                throw new RuntimeException(e);
                System.out.println(e.getMessage());
                dlgState = ufin_ONLINE_COLLECTION_DIALOG_STATE_INVALID_CLIENT;
            }
        } catch (IOException e) {
//            throw new RuntimeException(e);
            System.out.println(e.getMessage());
            dlgState = ufin_ONLINE_COLLECTION_DIALOG_STATE_INVALID_CLIENT;
        }
    }
//bmp 23-Sep-26

    public Bitmap generateQrCode(String text, int width, int height) {
        try {
            // Encode the string into a BitMatrix
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    text,
                    BarcodeFormat.QR_CODE,
                    width,
                    height
            );
            int matrixWidth = bitMatrix.getWidth();
            int matrixHeight = bitMatrix.getHeight();
            int[] pixels = new int[matrixWidth * matrixHeight];
            // Map the BitMatrix elements to black and white pixels
            for (int y = 0; y < matrixHeight; y++) {
                int offset = y * matrixWidth;
                for (int x = 0; x < matrixWidth; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }
            // Create a Bitmap from the pixel array
            Bitmap bitmap = Bitmap.createBitmap(matrixWidth, matrixHeight, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, matrixWidth, 0, 0, matrixWidth, matrixHeight);
            return bitmap;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
