package com.example.ufin_lso;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectionFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final int ufin_COLLECTION_DIALOG_STATE_NONE                          = 0;
    public static final int ufin_COLLECTION_DIALOG_STATE_CLOSE                         = 1;
    public static final int ufin_COLLECTION_DIALOG_STATE_SUBMIT_DCS                    = 2;
    public static final int ufin_COLLECTION_DIALOG_STATE_INCORRECT_MOBILE_NO           = 3;
    public static final int ufin_COLLECTION_DIALOG_STATE_INCORRECT_EXTRA_COLLECTION    = 5;
    public static final int ufin_COLLECTION_DIALOG_STATE_RESUME_PRINTING               = 6;
    public static final int ufin_COLLECTION_DIALOG_STATE_NO_PRINTER                    = 7;
    public static final int ufin_COLLECTION_DIALOG_STATE_COMPLETED_PRINTING            = 8;
    public static final int ufin_COLLECTION_DIALOG_STATE_INCORRECT_NEXT_SCHEDULE       = 9;
    public static final int ufin_COLLECTION_DIALOG_STATE_MOBILE_NO_NOT_VERIFIED        = 10;
    public static final int ufin_COLLECTION_DIALOG_STATE_AMT_EXCEED_COLLECTION_LIMIT   = 11;
    public static final int ufin_COLLECTION_DIALOG_STATE_PRECLOSURE_REQUEST_SENT       = 12;
    public static final int ufin_COLLECTION_DIALOG_STATE_EXPECTED_AMT_COLLECTION_MISMATCH = 13;
    public static final int ufin_COLLECTION_DIALOG_STATE_CONFIRM_UNCOLLECTED           = 14;
    public static final int ufin_COLLECTION_DIALOG_STATE_PRECLOSURE_REASON_NOTSELECTED = 15;
    public static final int ufin_COLLECTION_DIALOG_STATE_MD_NOT_WITHIN_LIMIT           = 16;
    public static final int ufin_COLLECTION_DIALOG_STATE_PREPAYMENT_MONTHS_AMT_EMPTY   = 17;
    public static final int ufin_COLLECTION_DIALOG_STATE_PREPAYMENT_REQUEST_SENT       = 18;
    public static final int ufin_COLLECTION_DIALOG_STATE_WAITING_FOR_PACKET_UPLOAD     = 19;
    public static final int ufin_COLLECTION_DIALOG_STATE_COLLECTION_SUMMARY_GENERATED  = 20;

    public static final int ufin_COLLECTION_DIALOG_STATE_PRECLOSURE_CALCULATION_REMARK = 21;
    public static final int ufin_COLLECTION_DIALOG_STATE_INVALID_RECOVERY_MODE_SET     = 22;
    public static final int ufin_COLLECTION_DIALOG_STATE_PRECLOSURE_REJECT_REASON      = 23;
    public static final int ufin_COLLECTION_DIALOG_STATE_PENDING_REQUEST_WAIT          = 24;
    public static final int ufin_COLLECTION_DIALOG_STATE_PENDING_REQUEST_RESENT        = 25;
//bmp 21-Jul-26
    public static final int ufin_COLLECTION_DIALOG_STATE_CONFIRM_RESET_RECOVERY_MODE   = 26;
//bmp 21-Jul-26
//bmp 20-Aug-26
    public static final int ufin_COLLECTION_DIALOG_STATE_CONFIRM_PRINT_BUTTON          = 27;
//bmp 20-Aug-26

    public static final String MSG_ufin_PENDING_REQUEST_WAIT =
            "Request Pending, Please Check after sometime.";
    public static final String MSG_ufin_PENDING_REQUEST_RESENT =
            "Request Resent, Please Check after sometime.";
    public static final String MSG_ufin_COLLECTION_INVALID_RECOVERY_MODE_SET =
            "Invalid Recovery Mode Set.";
//bmp 21-Jul-26
    public static final String MSG_ufin_COLLECTION_CONFIRM_RESET_RECOVERY_MODE =
            "Press OK to Reset Recovery Mode or Cancel to Continue.";
//bmp 21-Jul-26
    public static final String MSG_ufin_COLLECTION_SUMMARY_GENERATED =
            "Collection Summary Generated.";
    public static final String MSG_ufin_WAITING_FOR_PACKET_UPLOAD =
            "Collection Data Syncup with Server Not Completed.";
    public static final String MSG_ufin_PREPAYMENT_REQUEST_SENT =
            "Loan PrePayment Request Sent, Please Check PrePayment Amount after sometime.";
    //जास्तीचे हप्ते घेण्याची रिक्वेस्ट परवानगीसाठी सर्व्हरला पाठवली आहे.
    public static final String MSG_ufin_PREPAYMENT_MONTHS_AMT_EMPTY =
            "Enter Loan PrePayment Months or Amount and Try Again.";
    public static final String MSG_ufin_MD_NOT_WITHIN_LIMIT =
            "Loan maturity date is Not within 3 months.";
    public static final String MSG_ufin_PRECLOSURE_REASON_NOTSELECTED =
            "Loan PrePayment/PreClosure Reason Not Selected.";
    public static final String MSG_ufin_EXPECTED_AMT_MISMATCH_LESS_COLLECTION =
            "पेमेंटची रक्कम देय रक्कमेपेक्षा कमी आहे.";
    public static final String MSG_ufin_EXPECTED_AMT_MISMATCH_EXTRA_COLLECTION =
            "पेमेंटची रक्कम देय रक्कमेपेक्षा जास्त आहे.";
    public static final String MSG_ufin_PREPAYMENT_COLLECTION_REMINDER =
            "जास्तीचे हप्ते:पुढील महिन्याची मुद्दल देय नसली तरी\nबाकी मुद्दलावर व्याज, बाकी बचत पुढील महिन्यात भरावी लागेल";
    public static final String MSG_ufin_PRECLOSURE_COLLECTION_REMINDER =
            "मुदतपूर्व खाते बंद:";
    public static final String MSG_ufin_PRECLOSURE_REQUEST_SENT =
            "Loan Pre-Closure Request Sent, Please Check Pre-Closure Amount after sometime.";
    public static final String MSG_ufin_PRECLOSURE_CALCULATION_REMARK =
            "Loan Pre-Closure calculation will NOT consider same day collection, Please reconfirm amounts with MIS.";
    public static final String MSG_ufin_AMT_EXCEED_COLLECTION_LIMIT =
            "3 कर्ज हप्त्यापेक्षा जास्त रक्कम भरण्याची परवानगी नाही.";
    public static final String MSG_ufin_COLLECTION_CONFIRM_CLOSE =
            "Press OK to Close Without Saving Changes";
    public static final String MSG_ufin_COLLECTION_CONFIRM_SUBMIT_DCS =
            "Press OK to Submit DCS (Daily Collection Summary)";
    public static final String MSG_ufin_COLLECTION_INCORRECT_MOBILE_NO =
            "Incorrect Mobile Number.";
    public static final String MSG_ufin_COLLECTION_MOBILE_NO_NOT_VERIFIED =
            "Mobile Number Not Verified.";
    public static final String MSG_ufin_COLLECTION_CONFIRM_UNCOLLECTED =
            "Collection Pending.";
    public static final String MSG_ufin_COLLECTION_NO_PRINTER =
            "Printer Not Connected.\nPress Cancel and Retry.";
    public static final String MSG_ufin_COLLECTION_CHECK_PRINTER =
            "Printer Not Connected.\nPress Cancel to Retry.\nPress OK for No Receipt.";
    public static final String MSG_ufin_COLLECTION_INCORRECT_EXTRA_COLLECTION =
            "Enter correct details for Extra Collection Amount";
    public static final String MSG_ufin_COLLECTION_RESUME_PRINTING =
            "Press OK to Resume Printing";
    public static final String MSG_ufin_COLLECTION_COMPLETED_PRINTING =
            "Completed Printing";
//bmp 20-Aug-26
    public static final String MSG_ufin_COLLECTION_CONFIRM_PRINT_BUTTON =
//bmp 31-Aug-26
//        "Press OK to Continue Print.\nPress Cancel to Abort.";
        "To Print: Check Printer is Connected,\nthen Press OK to Continue Print.\nPress Cancel to Abort.";
//bmp 31-Aug-26
//bmp 20-Aug-26
    public static final String MSG_ufin_COLLECTION_INCORRECT_NEXT_SCHEDULE =
            "Enter correct Date and Time for Next Collection Schedule";
    public static final String MSG_ufin_COLLECTION_INCORRECT_NEXT_SCHEDULE_STARTTIME_MINUTES =
            "Start Time for Next Collection Schedule should be in steps of 10 minutes";
    public static final String MSG_ufin_COLLECTION_INCORRECT_NEXT_SCHEDULE_STARTTIME =
            "Start Time for Next Collection Schedule should be between 11:00 AM to 06:00 PM";

    View rootView;
    MainActivity mainActivity;
    public AlertDialog.Builder dlgAlert;
    public static int dlgState = ufin_COLLECTION_DIALOG_STATE_NONE;
    public boolean ReadyToPrint = false;
    public boolean SavedNarration = false;
    public boolean ConfirmPrint = false;

    final ArrayList<String> AscCenterInfoList = new ArrayList<String>();
    final ArrayList<Integer> ColorAscCenterInfoList = new ArrayList<Integer>();
    final ArrayList<clsCollectionCenter> AscCenterList = new ArrayList<clsCollectionCenter>();
    final ArrayList<String> CenterMemberInfoList = new ArrayList<String>();
    final ArrayList<Integer> ColorCenterMemberInfoList = new ArrayList<Integer>();
    final ArrayList<clsCenterMember> CenterMemberList = new ArrayList<clsCenterMember>();

    clsCollectionCenter selectedCollectionCenter;
    clsCenterMember selectedCollectionMember;
    int currentCenterMemberListPosition = -1;

    long lCollectionID;
    String strCollectionDate;
    int iStartCount500Notes, iCenterCount500Notes, iTotalCount500Notes;
    int iStartCount200Notes, iCenterCount200Notes, iTotalCount200Notes;
    int iStartCount100Notes, iCenterCount100Notes, iTotalCount100Notes;
    int iStartCount50Notes, iCenterCount50Notes, iTotalCount50Notes;
    int iStartCount20Notes, iCenterCount20Notes, iTotalCount20Notes;
    int iStartCount10Notes, iCenterCount10Notes, iTotalCount10Notes;
    int iStartCount5Notes, iCenterCount5Notes, iTotalCount5Notes;
    int iStartCount2Notes, iCenterCount2Notes, iTotalCount2Notes;
    int iStartCount1Notes, iCenterCount1Notes, iTotalCount1Notes;
    int iStartValueCash, iCenterValueCash, iTotalValueCash;
    int iStartValueOnline, iCenterValueOnline, iTotalValueOnline;

    int iStartValue500Notes, iCenterValue500Notes;
    int iStartValue200Notes, iCenterValue200Notes;
    int iStartValue100Notes, iCenterValue100Notes;
    int iStartValue50Notes, iCenterValue50Notes;
    int iStartValue20Notes, iCenterValue20Notes;
    int iStartValue10Notes, iCenterValue10Notes;
    int iStartValue5Notes, iCenterValue5Notes;
    int iStartValue2Notes, iCenterValue2Notes;
    int iStartValue1Notes, iCenterValue1Notes;

    int iStartValueTotal, iCenterValueTotal, iTotalValue;
    int iCollectionTarget0;
    int iCollectionTarget1, iCollectionTarget2, iCollectionTarget3, iCollectionTarget4, iCollectionTarget5;
    int iCollectionTarget6, iCollectionTarget7;//, iCollectionTarget8, iCollectionTarget9, iCollectionTarget10;
    int /*iCollectionTarget11, iCollectionTarget12, iCollectionTarget13, iCollectionTarget14,*/ iCollectionTarget15;
    int iCollectionTarget16, iCollectionTarget17, iCollectionTarget18, iCollectionTarget19, iCollectionTarget20;
//    int iCollectionTarget21, iCollectionTarget22, iCollectionTarget23, iCollectionTarget24, iCollectionTarget25;
    int /*iCollectionTarget26, iCollectionTarget27,*/ iCollectionTarget28, iCollectionTarget29, iCollectionTarget30;

    int iTotalEMIReceiptsCount, iTotalEMIReceiptsValue;
    int iTotalADHPReceiptsCount, iTotalADHPReceiptsValue;
    int iTotalRDReceiptsCount, iTotalRDReceiptsValue;
    int iTotalAdditionalEMIReceiptsCount, iTotalAdditionalEMIReceiptsValue;
    int iTotalExtraReceiptsCount, iTotalExtraReceiptsValue;
    int iTotalPreClosureAdditionalEMIReceiptsCount, iTotalPreClosureAdditionalEMIReceiptsValue;
    int iTotalADHPExtraReceiptsCount, iTotalADHPExtraReceiptsValue;
    int iTotalNewRDExtraReceiptsCount, iTotalNewRDExtraReceiptsValue;
    int iTotalFDExtraReceiptsCount, iTotalFDExtraReceiptsValue;
    int iTotalJLGExtraReceiptsCount, iTotalJLGExtraReceiptsValue;
    int iTotalReprintChargesExtraReceiptsCount, iTotalReprintChargesExtraReceiptsValue;

    int iTotalCashEMIReceiptsCount, iTotalCashEMIReceiptsValue;
    int iTotalCashADHPReceiptsCount, iTotalCashADHPReceiptsValue;
    int iTotalCashRDReceiptsCount, iTotalCashRDReceiptsValue;
    int iTotalCashAdditionalEMIReceiptsCount, iTotalCashAdditionalEMIReceiptsValue;
    int iTotalCashExtraReceiptsCount, iTotalCashExtraReceiptsValue;
    int iTotalCashPreClosureAdditionalEMIReceiptsCount, iTotalCashPreClosureAdditionalEMIReceiptsValue;
    int iTotalCashADHPExtraReceiptsCount, iTotalCashADHPExtraReceiptsValue;
    int iTotalCashNewRDExtraReceiptsCount, iTotalCashNewRDExtraReceiptsValue;
    int iTotalCashFDExtraReceiptsCount, iTotalCashFDExtraReceiptsValue;
    int iTotalCashJLGExtraReceiptsCount, iTotalCashJLGExtraReceiptsValue;
    int iTotalCashReprintChargesExtraReceiptsCount, iTotalCashReprintChargesExtraReceiptsValue;

    int iTotalOnlineEMIReceiptsCount, iTotalOnlineEMIReceiptsValue;
    int iTotalOnlineADHPReceiptsCount, iTotalOnlineADHPReceiptsValue;
    int iTotalOnlineRDReceiptsCount, iTotalOnlineRDReceiptsValue;
    int iTotalOnlineAdditionalEMIReceiptsCount, iTotalOnlineAdditionalEMIReceiptsValue;
    int iTotalOnlineExtraReceiptsCount, iTotalOnlineExtraReceiptsValue;
    int iTotalOnlinePreClosureAdditionalEMIReceiptsCount, iTotalOnlinePreClosureAdditionalEMIReceiptsValue;
    int iTotalOnlineADHPExtraReceiptsCount, iTotalOnlineADHPExtraReceiptsValue;
    int iTotalOnlineNewRDExtraReceiptsCount, iTotalOnlineNewRDExtraReceiptsValue;
    int iTotalOnlineFDExtraReceiptsCount, iTotalOnlineFDExtraReceiptsValue;
    int iTotalOnlineJLGExtraReceiptsCount, iTotalOnlineJLGExtraReceiptsValue;
    int iTotalOnlineReprintChargesExtraReceiptsCount, iTotalOnlineReprintChargesExtraReceiptsValue;

    public class clsMemberReceipt {
        long MembeID;
        String pdfReceipt;
    }
    public ArrayList<clsMemberReceipt> memberReceipt = new ArrayList<clsMemberReceipt>();
    int memberReceiptIndex;
    String ddMMMTodate = "";
    String ddMMMReferencedate = "";

    DatePickerDialog dlgDatePicker = null;
    TimePickerDialog dlgTimePicker = null;
    final Calendar myCalendar = Calendar.getInstance();
    int editDateType, editTimeType;
    EditText etNextRecoveryDate, etNextRecoveryTime;
    String[] strPreClosureCollect;
    String[] strPrePaymentCollect;
    public ArrayList<String> PrePaymentPreClosureReasons = new ArrayList<String>();
    public ArrayList<Integer> PrePaymentReasonIDs = new ArrayList<Integer>();
    public ArrayList<Integer> PreClosureReasonIDs = new ArrayList<Integer>();
    public ArrayList<String> PrePaymentPreClosureRejectionReasons = new ArrayList<String>();
    public ArrayList<Integer> PrePaymentPreClosureRejectionReasonIDs = new ArrayList<Integer>();

    String[] strPaymentOptions;
    boolean flagMDWithinLimit;
    int iInstallmentsTillMD;
    String strSelectedPaymentOption;
    int iCollectionPacketsPendingUpload;
    int iCollectionDetailsPendingUpload;
    int iRDReceiptPendingUpload;
    int iCollectionSMSPendingUpload;
    int iNarrationPendingUpload;
	long LastTimeInMillis;
    private ArrayList<Long> OnlinePaymentAppsIDMaster = new ArrayList<Long>();
    private ArrayList<String> OnlinePaymentAppsMaster = new ArrayList<String>();

    public CollectionFragment() {
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
    public static CollectionFragment newInstance(String param1, String param2) {
        CollectionFragment fragment = new CollectionFragment();
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
        rootView = inflater.inflate(R.layout.fragment_collection, container, false);
        mainActivity = (MainActivity) getActivity();
        SavedNarration = false;
        ConfirmPrint = false;

        SQLite_getPrePaymentPreClosureRejectionReasonMaster();
        SQLite_getOnlinePaymentAppMaster();

        if(!SQLite_getCollectionSummary()) {
            mainActivity.flagNewSummary = true;
            iStartCount500Notes = 0;
            iStartCount200Notes = 0;
            iStartCount100Notes = 0;
            iStartCount50Notes = 0;
            iStartCount20Notes = 0;
            iStartCount10Notes = 0;
            iStartCount5Notes = 0;
            iStartCount2Notes = 0;
            iStartCount1Notes = 0;
            iStartValueOnline = 0;
            iStartValueCash = 0;
            iStartValueTotal = 0;
            iTotalEMIReceiptsCount = 0;
            iTotalEMIReceiptsValue = 0;
            iTotalADHPReceiptsCount = 0;
            iTotalADHPReceiptsValue = 0;
            iTotalRDReceiptsCount = 0;
            iTotalRDReceiptsValue = 0;
            iTotalAdditionalEMIReceiptsCount = 0;
            iTotalAdditionalEMIReceiptsValue = 0;
            iTotalExtraReceiptsCount = 0;
            iTotalExtraReceiptsValue = 0;
            iTotalPreClosureAdditionalEMIReceiptsCount = 0;
            iTotalPreClosureAdditionalEMIReceiptsValue = 0;
            iTotalADHPExtraReceiptsCount = 0;
            iTotalADHPExtraReceiptsValue = 0;
            iTotalNewRDExtraReceiptsCount = 0;
            iTotalNewRDExtraReceiptsValue = 0;
            iTotalFDExtraReceiptsCount = 0;
            iTotalFDExtraReceiptsValue = 0;
            iTotalJLGExtraReceiptsCount = 0;
            iTotalJLGExtraReceiptsValue = 0;
            iTotalReprintChargesExtraReceiptsCount = 0;
            iTotalReprintChargesExtraReceiptsValue = 0;

            iTotalCashEMIReceiptsCount = 0;
            iTotalCashEMIReceiptsValue = 0;
            iTotalCashADHPReceiptsCount = 0;
            iTotalCashADHPReceiptsValue = 0;
            iTotalCashRDReceiptsCount = 0;
            iTotalCashRDReceiptsValue = 0;
            iTotalCashAdditionalEMIReceiptsCount = 0;
            iTotalCashAdditionalEMIReceiptsValue = 0;
            iTotalCashExtraReceiptsCount = 0;
            iTotalCashExtraReceiptsValue = 0;
            iTotalCashPreClosureAdditionalEMIReceiptsCount = 0;
            iTotalCashPreClosureAdditionalEMIReceiptsValue = 0;
            iTotalCashADHPExtraReceiptsCount = 0;
            iTotalCashADHPExtraReceiptsValue = 0;
            iTotalCashNewRDExtraReceiptsCount = 0;
            iTotalCashNewRDExtraReceiptsValue = 0;
            iTotalCashFDExtraReceiptsCount = 0;
            iTotalCashFDExtraReceiptsValue = 0;
            iTotalCashJLGExtraReceiptsCount = 0;
            iTotalCashJLGExtraReceiptsValue = 0;
            iTotalCashReprintChargesExtraReceiptsCount = 0;
            iTotalCashReprintChargesExtraReceiptsValue = 0;

            iTotalOnlineEMIReceiptsCount = 0;
            iTotalOnlineEMIReceiptsValue = 0;
            iTotalOnlineADHPReceiptsCount = 0;
            iTotalOnlineADHPReceiptsValue = 0;
            iTotalOnlineRDReceiptsCount = 0;
            iTotalOnlineRDReceiptsValue = 0;
            iTotalOnlineAdditionalEMIReceiptsCount = 0;
            iTotalOnlineAdditionalEMIReceiptsValue = 0;
            iTotalOnlineExtraReceiptsCount = 0;
            iTotalOnlineExtraReceiptsValue = 0;
            iTotalOnlinePreClosureAdditionalEMIReceiptsCount = 0;
            iTotalOnlinePreClosureAdditionalEMIReceiptsValue = 0;
            iTotalOnlineADHPExtraReceiptsCount = 0;
            iTotalOnlineADHPExtraReceiptsValue = 0;
            iTotalOnlineNewRDExtraReceiptsCount = 0;
            iTotalOnlineNewRDExtraReceiptsValue = 0;
            iTotalOnlineFDExtraReceiptsCount = 0;
            iTotalOnlineFDExtraReceiptsValue = 0;
            iTotalOnlineJLGExtraReceiptsCount = 0;
            iTotalOnlineJLGExtraReceiptsValue = 0;
            iTotalOnlineReprintChargesExtraReceiptsCount = 0;
            iTotalOnlineReprintChargesExtraReceiptsValue = 0;
        }

        Button bt = (Button) rootView.findViewById(R.id.buttonCloseCollection);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonPrintCollection);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonSaveCollection);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonSummaryCollection);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonSchedule);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonPrePayClosureRequest);
        bt.setOnClickListener(this);
        TextView tv = (TextView) rootView.findViewById(R.id.textViewLoanPrincipal);
        tv.setText("");
        tv = (TextView) rootView.findViewById(R.id.textViewLoanInterest);
        tv.setText("");
        tv = (TextView) rootView.findViewById(R.id.textViewLoanLateCharge);
        tv.setText("");
        tv = (TextView) rootView.findViewById(R.id.textViewLoanJLG);
        tv.setText("");
        tv = (TextView) rootView.findViewById(R.id.textViewLoanPolicyRenewalAmt);
        tv.setText("");
        tv = (TextView) rootView.findViewById(R.id.textViewLoanADHP);
        tv.setText("");
        tv = (TextView) rootView.findViewById(R.id.textViewLoanRD);
        tv.setText("");
        tv = (TextView) rootView.findViewById(R.id.textViewLoanTotal);
        tv.setText("");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strText = (String) ((TextView) view).getText();
                if(strText.equalsIgnoreCase("Rejected")) {
                    CreateAlertDialog(true, false);
                    dlgState = ufin_COLLECTION_DIALOG_STATE_PRECLOSURE_REJECT_REASON;
                    String strMsg = "";
                    for (int i = 0; i < PrePaymentPreClosureRejectionReasonIDs.size(); i++) {
                        if (PrePaymentPreClosureRejectionReasonIDs.get(i) == selectedCollectionMember.collectionMember.PreClosureRejectReasonID) {
                            strMsg = PrePaymentPreClosureRejectionReasons.get(i);
                            break;
                        }
                    }
                    dlgAlert.setMessage(strMsg);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    textView.setTextColor(Color.RED);
                } else if(strText.equalsIgnoreCase("Pending")) {
                    boolean flagPacketPending = false;
                    for (int i = 0; i < mainActivity.SyncPrePayment_PreClosureRequest.size(); i++) {
                        if (mainActivity.SyncPrePayment_PreClosureRequest.get(i).recordID
                                == selectedCollectionMember.collectionMember.MemberID) {
                            flagPacketPending = true;
                            break;
                        }
                    }
                    for (int i = 0; i < mainActivity.SyncPrePayment_PreClosureApprovalStatus.size(); i++) {
                        if (mainActivity.SyncPrePayment_PreClosureApprovalStatus.get(i).recordID
                                == selectedCollectionMember.collectionMember.MemberID) {
                            flagPacketPending = true;
                            break;
                        }
                    }
                    if (flagPacketPending) {
                        CreateAlertDialog(true, true);
                        dlgState = ufin_COLLECTION_DIALOG_STATE_PENDING_REQUEST_WAIT;
                        dlgAlert.setMessage(MSG_ufin_PENDING_REQUEST_WAIT);
                        AlertDialog dialog = dlgAlert.create();
                        dialog.show();
                        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                        textView.setTextSize(30);
                        textView.setTextColor(Color.RED);
                    } else {
                        mainActivity.GetPrePayment_PreClosureAmount(
                                selectedCollectionMember.collectionMember.MemberID,
                                selectedCollectionMember.collectionMember.PreClosureReasonID,
                                selectedCollectionMember.collectionMember.PrePaymentORPreClosure,
                                selectedCollectionMember.collectionMember.PrePaymentReqMonths,
                                selectedCollectionMember.collectionMember.PrePaymentReqAmount);
                        CreateAlertDialog(true, true);
                        dlgState = ufin_COLLECTION_DIALOG_STATE_PENDING_REQUEST_RESENT;
                        dlgAlert.setMessage(MSG_ufin_PENDING_REQUEST_RESENT);
                        AlertDialog dialog = dlgAlert.create();
                        dialog.show();
                        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                        textView.setTextSize(30);
                        textView.setTextColor(Color.RED);
                    }
//bmp 21-Jul-26
                } else {
                    CreateAlertDialog(true, true);
                    dlgState = ufin_COLLECTION_DIALOG_STATE_CONFIRM_RESET_RECOVERY_MODE;
                    dlgAlert.setMessage(MSG_ufin_COLLECTION_CONFIRM_RESET_RECOVERY_MODE);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    textView.setTextColor(Color.RED);
//bmp 21-Jul-26
                }
            }
        });
        tv = (TextView) rootView.findViewById(R.id.editTextPrePaymentReqMonths);
        tv.setText("");
        tv = (TextView) rootView.findViewById(R.id.editTextPrePaymentReqAmount);
        tv.setText("");
        tv = (TextView) rootView.findViewById(R.id.textViewPrePaymentApproval);
        tv.setText("");

        final ArrayList<String> alCollectionDate = new ArrayList<>();
        alCollectionDate.clear();
        alCollectionDate.add("");
        final ArrayList<String> alCollectionDateDDMMYY = new ArrayList<>();
        alCollectionDateDDMMYY.clear();
        alCollectionDateDDMMYY.add("");
        final ArrayList<Integer> alCollectionDay = new ArrayList<>();
        alCollectionDay.clear();
        alCollectionDay.add(0);
        final ArrayList<String> alAscCollectionDate = new ArrayList<>();
        alAscCollectionDate.clear();
        alAscCollectionDate.add("");
        final ArrayList<String> alAscCollectionDateDDMMYY = new ArrayList<>();
        alAscCollectionDateDDMMYY.clear();
        alAscCollectionDateDDMMYY.add("");
        for(int i=0; i<mainActivity.CollectionCenterList.size(); i++) {
            String strTemp = mainActivity.CollectionCenterList.get(i).RecoveryDate;
            boolean newFlag = true;
            for(int j=0; j<alCollectionDate.size(); j++) {
                if(strTemp.equalsIgnoreCase(alCollectionDate.get(j))) {
                    newFlag = false;
                    break;
                }
            }
            if(newFlag) {
                alCollectionDay.add(Integer.valueOf(strTemp.substring(0, 2)));
                alCollectionDate.add(strTemp);
                String strTemp2 =   strTemp.substring(0, 6) + "-" + strTemp.substring(9, 11);
                alCollectionDateDDMMYY.add(strTemp2);
            }
        }
        for(int i=1; i<32; i++) {
            for(int j=1; j<alCollectionDay.size(); j++) {
                if(alCollectionDay.get(j) == i) {
                    alAscCollectionDate.add(alCollectionDate.get(j));
                    alAscCollectionDateDDMMYY.add(alCollectionDateDDMMYY.get(j));
                }
            }
        }

        ArrayAdapter<String> adapterCollectionDetails = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, alAscCollectionDateDDMMYY);
        adapterCollectionDetails.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner spSelectDate = (Spinner) rootView.findViewById(R.id.spinnerCollectionDate);
        spSelectDate.setAdapter(adapterCollectionDetails);
        spSelectDate.setSelection(0);
        spSelectDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                    return;
                Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerCluster);
                sp.setSelection(0);
                sp = (Spinner) rootView.findViewById(R.id.spinnerCommunity);
                sp.setSelection(0);
                String strSelectDate = alAscCollectionDate.get(position);

                RefreshCollectionCenterList(strSelectDate, 0, 0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spSelectDate.setFocusable(true);
        spSelectDate.setFocusableInTouchMode(true);

        RefreshCollectionCenterList("", 0, 0);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf0 = new SimpleDateFormat("dd-MMM", Locale.ENGLISH);
        ddMMMTodate = sdf0.format(date);// all done
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        String strDate = sdf.format(date);// all done
        for(int i=0; i<alAscCollectionDateDDMMYY.size(); i++) {
            if(strDate.equalsIgnoreCase(alAscCollectionDateDDMMYY.get(i))) {
                spSelectDate.setSelection(i);
                break;
            }
        }

        final ArrayList<Long> alCollectionClusterID = new ArrayList<>();
        alCollectionClusterID.clear();
        alCollectionClusterID.add(0L);
        final ArrayList<String> alCollectionCluster = new ArrayList<>();
        alCollectionCluster.clear();
        alCollectionCluster.add("");
        for(int i=0; i<mainActivity.CollectionCenterList.size(); i++) {
            long lTemp = mainActivity.CollectionCenterList.get(i).ClusterID;
            boolean newFlag = true;
            for(int j=0; j<alCollectionClusterID.size(); j++) {
                if(lTemp == alCollectionClusterID.get(j)) {
                    newFlag = false;
                    break;
                }
            }
            if(newFlag) {
                alCollectionClusterID.add(lTemp);
                alCollectionCluster.add(mainActivity.CollectionCenterList.get(i).ClusterName);
            }
        }
        ArrayAdapter<String> adapterCollectionDetailsCluster = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, alCollectionCluster);
        adapterCollectionDetailsCluster.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner spSelectCluster = (Spinner) rootView.findViewById(R.id.spinnerCluster);
        spSelectCluster.setAdapter(adapterCollectionDetailsCluster);
        spSelectCluster.setSelection(0);
        spSelectCluster.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                    return;
                Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerCollectionDate);
                sp.setSelection(0);
                sp = (Spinner) rootView.findViewById(R.id.spinnerCommunity);
                sp.setSelection(0);
                long lTemp = alCollectionClusterID.get(position);
                RefreshCollectionCenterList("", lTemp, 0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spSelectCluster.setFocusable(true);
        spSelectCluster.setFocusableInTouchMode(true);

        final ArrayList<Long> alCollectionCommunityID = new ArrayList<>();
        alCollectionCommunityID.clear();
        alCollectionCommunityID.add(0L);
        final ArrayList<String> alCollectionCommunity = new ArrayList<>();
        alCollectionCommunity.clear();
        alCollectionCommunity.add("");
        for(int i=0; i<mainActivity.CollectionCenterList.size(); i++) {
            long lTemp = mainActivity.CollectionCenterList.get(i).CommunityID;
            boolean newFlag = true;
            for(int j=0; j<alCollectionCommunityID.size(); j++) {
                if(lTemp == alCollectionCommunityID.get(j)) {
                    newFlag = false;
                    break;
                }
            }
            if(newFlag) {
                alCollectionCommunityID.add(lTemp);
                alCollectionCommunity.add(mainActivity.CollectionCenterList.get(i).CommunityName);
            }
        }
        ArrayAdapter<String> adapterCollectionDetailsCommunity = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item, alCollectionCommunity);
        adapterCollectionDetailsCommunity.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner spSelectCommunity = (Spinner) rootView.findViewById(R.id.spinnerCommunity);
        spSelectCommunity.setAdapter(adapterCollectionDetailsCommunity);
        spSelectCommunity.setSelection(0);
        spSelectCommunity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                    return;
                Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerCollectionDate);
                sp.setSelection(0);
                sp = (Spinner) rootView.findViewById(R.id.spinnerCluster);
                sp.setSelection(0);
                long lTemp = alCollectionCommunityID.get(position);
                RefreshCollectionCenterList("", 0, lTemp);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spSelectCommunity.setFocusable(true);
        spSelectCommunity.setFocusableInTouchMode(true);

        EditText et = (EditText) rootView.findViewById(R.id.editTextCenterCount500);
        et.setText(String.valueOf(iCenterCount500Notes));
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        iCenterCount500Notes = Integer.parseInt(v.getText().toString());
                    } catch (NumberFormatException e) {
                        iCenterCount500Notes = 0;
                    }
                    InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    RefreshCollection();
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    try {
                        iCenterCount500Notes = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        iCenterCount500Notes = 0;
                    }
                    RefreshCollection();
                }
            }
        });

        et = (EditText) rootView.findViewById(R.id.editTextCenterCount200);
        et.setText(String.valueOf(iCenterCount200Notes));
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        iCenterCount200Notes = Integer.parseInt(v.getText().toString());
                    } catch (NumberFormatException e) {
                        iCenterCount200Notes = 0;
                    }
                    InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    RefreshCollection();
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    try {
                        iCenterCount200Notes = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        iCenterCount200Notes = 0;
                    }
                    RefreshCollection();
                }
            }
        });

        et = (EditText) rootView.findViewById(R.id.editTextCenterCount100);
        et.setText(String.valueOf(iCenterCount100Notes));
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        iCenterCount100Notes = Integer.parseInt(v.getText().toString());
                    } catch (NumberFormatException e) {
                        iCenterCount100Notes = 0;
                    }
                    InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    RefreshCollection();
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    try {
                        iCenterCount100Notes = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        iCenterCount100Notes = 0;
                    }
                    RefreshCollection();
                }
            }
        });

        et = (EditText) rootView.findViewById(R.id.editTextCenterCount50);
        et.setText(String.valueOf(iCenterCount50Notes));
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        iCenterCount50Notes = Integer.parseInt(v.getText().toString());
                    } catch (NumberFormatException e) {
                        iCenterCount50Notes = 0;
                    }
                    InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    RefreshCollection();
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    try {
                        iCenterCount50Notes = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        iCenterCount50Notes = 0;
                    }
                    RefreshCollection();
                }
            }
        });

        et = (EditText) rootView.findViewById(R.id.editTextCenterCount20);
        et.setText(String.valueOf(iCenterCount20Notes));
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        iCenterCount20Notes = Integer.parseInt(v.getText().toString());
                    } catch (NumberFormatException e) {
                        iCenterCount20Notes = 0;
                    }
                    InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    RefreshCollection();
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    try {
                        iCenterCount20Notes = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        iCenterCount20Notes = 0;
                    }
                    RefreshCollection();
                }
            }
        });

        et = (EditText) rootView.findViewById(R.id.editTextCenterCount10);
        et.setText(String.valueOf(iCenterCount10Notes));
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        iCenterCount10Notes = Integer.parseInt(v.getText().toString());
                    } catch (NumberFormatException e) {
                        iCenterCount10Notes = 0;
                    }
                    InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    RefreshCollection();
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    try {
                        iCenterCount10Notes = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        iCenterCount10Notes = 0;
                    }
                    RefreshCollection();
                }
            }
        });

        et = (EditText) rootView.findViewById(R.id.editTextCenterCount5);
        et.setText(String.valueOf(iCenterCount5Notes));
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        iCenterCount5Notes = Integer.parseInt(v.getText().toString());
                    } catch (NumberFormatException e) {
                        iCenterCount5Notes = 0;
                    }
                    InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    RefreshCollection();
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    try {
                        iCenterCount5Notes = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        iCenterCount5Notes = 0;
                    }
                    RefreshCollection();
                }
            }
        });

        et = (EditText) rootView.findViewById(R.id.editTextCenterCount2);
        et.setText(String.valueOf(iCenterCount2Notes));
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        iCenterCount2Notes = Integer.parseInt(v.getText().toString());
                    } catch (NumberFormatException e) {
                        iCenterCount2Notes = 0;
                    }
                    InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    RefreshCollection();
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    try {
                        iCenterCount2Notes = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        iCenterCount2Notes = 0;
                    }
                    RefreshCollection();
                }
            }
        });

        et = (EditText) rootView.findViewById(R.id.editTextCenterCount1);
        et.setText(String.valueOf(iCenterCount1Notes));
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        iCenterCount1Notes = Integer.parseInt(v.getText().toString());
                    } catch (NumberFormatException e) {
                        iCenterCount1Notes = 0;
                    }
                    InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    RefreshCollection();
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    try {
                        iCenterCount1Notes = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        iCenterCount1Notes = 0;
                    }
                    RefreshCollection();
                }
            }
        });

        strSelectedPaymentOption = "";
        strPaymentOptions = getResources().getStringArray(R.array.paymentoptions_array);
        ArrayAdapter<String> adapterPaymentOptions = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item_small, strPaymentOptions);
        adapterPaymentOptions.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerPaymentOptions);
        sp.setAdapter(adapterPaymentOptions);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSelectedPaymentOption = "";
                switch (position) {
                    case 0:
                        strSelectedPaymentOption = "Monthly";
                        EditText et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqMonths);
                        et.setEnabled(false);
                        et.setText("0");
                        et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqAmount);
                        et.setEnabled(false);
                        et.setText("0");
                        TextView tv = (TextView) rootView.findViewById(R.id.textViewPrePaymentApproval);
                        tv.setText("");
                        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerLoanPreClosureReason);
                        sp.setEnabled(false);
                        sp.setSelection(0);
                        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectAdditionalRepay);
                        cb.setEnabled(false);
                        cb.setChecked(false);
                        et = (EditText) rootView.findViewById(R.id.editTextAdditionalRepay);
                        et.setEnabled(false);
                        et.setText("");
                        if(selectedCollectionMember == null)
                            break;
                        selectedCollectionMember.collectionMember.PrePaymentORPreClosure = 0;
                        selectedCollectionMember.collectionMember.PrePaymentReqMonths = 0;
                        selectedCollectionMember.collectionMember.PrePaymentReqAmount = 0;
                        selectedCollectionMember.collectionMember.PreClosureReasonID = 0;
//bmp 30-Jun-26
                        RefreshMemberDueDetails();
//bmp 30-Jun-26
                        break;
                    case 1:
                        strSelectedPaymentOption = "PrePayment";
                        if(selectedCollectionMember == null)
                            break;
                        selectedCollectionMember.collectionMember.PrePaymentORPreClosure = 1;
                        Calendar cal = Calendar.getInstance();
                        Date date = cal.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        String strDate = sdf.format(date);
                        boolean flagEnable = true;
                        if(     strDate.equalsIgnoreCase(selectedCollectionMember.collectionMember.PreClosureRequestDate) &&
                                (selectedCollectionMember.collectionMember.PreClosureRequestStatus >= 0) &&
                                (       (selectedCollectionMember.collectionMember.PrePaymentReqMonths > 0) ||
                                        (selectedCollectionMember.collectionMember.PrePaymentReqAmount > 0) ) ) {
                            flagEnable = false;
                        }
                        et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqMonths);
                        et.setText(String.valueOf(selectedCollectionMember.collectionMember.PrePaymentReqMonths));
                        et.setEnabled(flagEnable);
                        et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqAmount);
                        et.setText(String.valueOf(selectedCollectionMember.collectionMember.PrePaymentReqAmount));
                        et.setEnabled(flagEnable);
                        String strApproval = "";
                        if(selectedCollectionMember.collectionMember.PrePaymentApprovedMonths > 0) {
                            strApproval = "Approved महिने: " + String.valueOf(selectedCollectionMember.collectionMember.PrePaymentApprovedMonths);
                        } else if(selectedCollectionMember.collectionMember.PrePaymentApprovedAmount > 0) {
                            strApproval = "Approved रू." + String.valueOf(selectedCollectionMember.collectionMember.PrePaymentApprovedAmount) + "/-";
                        }
                        tv = (TextView) rootView.findViewById(R.id.textViewPrePaymentApproval);
                        tv.setText(strApproval);
                        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectAdditionalRepay);
                        cb.setChecked(false);
                        cb.setEnabled(true);
                        et = (EditText) rootView.findViewById(R.id.editTextAdditionalRepay);
                        et.setText("");
                        et.setEnabled(true);
//bmp 30-Jun-26
                        RefreshMemberDueDetails();
//bmp 30-Jun-26
                        break;
                    case 2:
                        strSelectedPaymentOption = "PreClosure";
                        if(selectedCollectionMember == null)
                            break;
                        selectedCollectionMember.collectionMember.PrePaymentORPreClosure = 2;
                        selectedCollectionMember.collectionMember.PrePaymentReqMonths = 0;
                        et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqMonths);
                        et.setEnabled(false);
                        et.setText("0");
                        selectedCollectionMember.collectionMember.PrePaymentReqAmount = 0;
                        et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqAmount);
                        et.setEnabled(false);
                        et.setText("0");
                        tv = (TextView) rootView.findViewById(R.id.textViewPrePaymentApproval);
                        tv.setText("");
                        Calendar cal2 = Calendar.getInstance();
                        Date date2 = cal2.getTime();
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        String strDate2 = sdf2.format(date2);
                        boolean flagEnable2 = true;
                        if(     strDate2.equalsIgnoreCase(selectedCollectionMember.collectionMember.PreClosureRequestDate) &&
                                (selectedCollectionMember.collectionMember.PreClosureRequestStatus >= 0) &&
                                (selectedCollectionMember.collectionMember.PreClosureReasonID != 0)) {
                            flagEnable2 = false;
                        }
                        if(flagEnable2) {
                            if (flagMDWithinLimit) {
                                strPrePaymentCollect = getResources().getStringArray(R.array.PrePaymentCollect_Within3Months_array);
                                strPreClosureCollect = getResources().getStringArray(R.array.PreClosureCollect_Within3Months_array);
                                if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                                    SQLite_getPrePaymentUpto3MonthsReasonMaster();
                                else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                                    SQLite_getPreClosureUpto3MonthsReasonMaster();
                            } else {
                                strPrePaymentCollect = getResources().getStringArray(R.array.PrePaymentCollect_Morethan3Months_array);
                                strPreClosureCollect = getResources().getStringArray(R.array.PreClosureCollect_Morethan3Months_array);
                                if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                                    SQLite_getPrePaymentMorethan3MonthsReasonMaster();
                                else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                                    SQLite_getPreClosureMorethan3MonthsReasonMaster();
                            }
                            ArrayAdapter<String> adapterPreClosureReasons = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                                    R.layout.simple_spinner_item, PrePaymentPreClosureReasons);
                            adapterPreClosureReasons.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                            Spinner spPreClosureReason = (Spinner) rootView.findViewById(R.id.spinnerLoanPreClosureReason);
                            spPreClosureReason.setAdapter(adapterPreClosureReasons);
                            spPreClosureReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                                        selectedCollectionMember.collectionMember.PreClosureReasonID = PrePaymentReasonIDs.get(position);
                                    else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                                        selectedCollectionMember.collectionMember.PreClosureReasonID = PreClosureReasonIDs.get(position);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                            spPreClosureReason.setFocusable(true);
                            spPreClosureReason.setFocusableInTouchMode(true);
                            spPreClosureReason.setEnabled(true);
                        }
                        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectAdditionalRepay);
                        cb.setChecked(false);
                        cb.setEnabled(true);
                        et = (EditText) rootView.findViewById(R.id.editTextAdditionalRepay);
                        et.setText("");
                        et.setEnabled(true);
//bmp 30-Jun-26
                        RefreshMemberDueDetails();
//bmp 30-Jun-26
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        sp.setSelection(0);

        et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqMonths);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        selectedCollectionMember.collectionMember.PrePaymentReqMonths = Integer.parseInt(v.getText().toString());
                        if(selectedCollectionMember.collectionMember.PrePaymentReqMonths >= iInstallmentsTillMD)
                            selectedCollectionMember.collectionMember.PrePaymentReqMonths = iInstallmentsTillMD - 1;

                        int iAutoApprovalLimit = selectedCollectionMember.collectionMember.TotalDueAmountsToPayRefday
                                + (2 * selectedCollectionMember.collectionMember.StandardEMI)
                                - selectedCollectionMember.collectionMember.PrePaymentPrincipalCollected;
                        int iTemp =  selectedCollectionMember.collectionMember.PrePaymentReqMonths *
                                selectedCollectionMember.collectionMember.StandardEMI;
                        if (flagMDWithinLimit) {
                            if(selectedCollectionMember.collectionMember.PrePaymentReqMonths > 2) {
                                selectedCollectionMember.collectionMember.PrePaymentReqMonths = 2;
                            }
                        }
                        if(     (selectedCollectionMember.collectionMember.PrePaymentReqMonths <= 2) &&
                                (iTemp <= iAutoApprovalLimit)) {
                            strPrePaymentCollect = getResources().getStringArray(R.array.PrePaymentCollect_Within3Months_array);
                            strPreClosureCollect = getResources().getStringArray(R.array.PreClosureCollect_Within3Months_array);
                            if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                                SQLite_getPrePaymentUpto3MonthsReasonMaster();
                            else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                                SQLite_getPreClosureUpto3MonthsReasonMaster();
                        } else {
                            strPrePaymentCollect = getResources().getStringArray(R.array.PrePaymentCollect_Morethan3Months_array);
                            strPreClosureCollect = getResources().getStringArray(R.array.PreClosureCollect_Morethan3Months_array);
                            if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                                SQLite_getPrePaymentMorethan3MonthsReasonMaster();
                            else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                                SQLite_getPreClosureMorethan3MonthsReasonMaster();
                        }
                        ArrayAdapter<String> adapterPreClosureReasons = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                                R.layout.simple_spinner_item, PrePaymentPreClosureReasons);
                        adapterPreClosureReasons.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                        Spinner spPreClosureReason = (Spinner) rootView.findViewById(R.id.spinnerLoanPreClosureReason);
                        spPreClosureReason.setAdapter(adapterPreClosureReasons);
                        spPreClosureReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                                    selectedCollectionMember.collectionMember.PreClosureReasonID = PrePaymentReasonIDs.get(position);
                                else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                                    selectedCollectionMember.collectionMember.PreClosureReasonID = PreClosureReasonIDs.get(position);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                        spPreClosureReason.setFocusable(true);
                        spPreClosureReason.setFocusableInTouchMode(true);
                        spPreClosureReason.setEnabled(true);

                        EditText et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqMonths);
                        et.setText(String.valueOf(selectedCollectionMember.collectionMember.PrePaymentReqMonths));
                        selectedCollectionMember.collectionMember.PrePaymentReqAmount = 0;
                        et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqAmount);
                        et.setText("0");
                    } catch (NumberFormatException e) {
                        selectedCollectionMember.collectionMember.PrePaymentReqMonths = 0;
                    }
                    InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    try {
                        selectedCollectionMember.collectionMember.PrePaymentReqMonths = Integer.parseInt(((EditText) view).getText().toString());
                        if(selectedCollectionMember.collectionMember.PrePaymentReqMonths >= iInstallmentsTillMD)
                            selectedCollectionMember.collectionMember.PrePaymentReqMonths = iInstallmentsTillMD - 1;

                        int iAutoApprovalLimit = selectedCollectionMember.collectionMember.TotalDueAmountsToPayRefday
                                + (2 * selectedCollectionMember.collectionMember.StandardEMI)
                                - selectedCollectionMember.collectionMember.PrePaymentPrincipalCollected;
                        int iTemp =  selectedCollectionMember.collectionMember.PrePaymentReqMonths *
                                selectedCollectionMember.collectionMember.StandardEMI;
                        if (flagMDWithinLimit) {
                            if(selectedCollectionMember.collectionMember.PrePaymentReqMonths > 2) {
                                selectedCollectionMember.collectionMember.PrePaymentReqMonths = 2;
                            }
                        }
                        if(     (selectedCollectionMember.collectionMember.PrePaymentReqMonths <= 2) &&
                                (iTemp <= iAutoApprovalLimit)) {
                            strPrePaymentCollect = getResources().getStringArray(R.array.PrePaymentCollect_Within3Months_array);
                            strPreClosureCollect = getResources().getStringArray(R.array.PreClosureCollect_Within3Months_array);
                            if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                                SQLite_getPrePaymentUpto3MonthsReasonMaster();
                            else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                                SQLite_getPreClosureUpto3MonthsReasonMaster();
                        } else {
                            strPrePaymentCollect = getResources().getStringArray(R.array.PrePaymentCollect_Morethan3Months_array);
                            strPreClosureCollect = getResources().getStringArray(R.array.PreClosureCollect_Morethan3Months_array);
                            if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                                SQLite_getPrePaymentMorethan3MonthsReasonMaster();
                            else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                                SQLite_getPreClosureMorethan3MonthsReasonMaster();
                        }
                        ArrayAdapter<String> adapterPreClosureReasons = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                                R.layout.simple_spinner_item, PrePaymentPreClosureReasons);
                        adapterPreClosureReasons.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                        Spinner spPreClosureReason = (Spinner) rootView.findViewById(R.id.spinnerLoanPreClosureReason);
                        spPreClosureReason.setAdapter(adapterPreClosureReasons);
                        spPreClosureReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                                    selectedCollectionMember.collectionMember.PreClosureReasonID = PrePaymentReasonIDs.get(position);
                                else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                                    selectedCollectionMember.collectionMember.PreClosureReasonID = PreClosureReasonIDs.get(position);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                        spPreClosureReason.setFocusable(true);
                        spPreClosureReason.setFocusableInTouchMode(true);
                        spPreClosureReason.setEnabled(true);

                        EditText et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqMonths);
                        et.setText(String.valueOf(selectedCollectionMember.collectionMember.PrePaymentReqMonths));
                        selectedCollectionMember.collectionMember.PrePaymentReqAmount = 0;
                        et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqAmount);
                        et.setText("0");
                    } catch (NumberFormatException e) {
                        selectedCollectionMember.collectionMember.PrePaymentReqMonths = 0;
                    }
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqAmount);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        selectedCollectionMember.collectionMember.PrePaymentReqAmount = Integer.parseInt(v.getText().toString());
                        if(selectedCollectionMember.collectionMember.PrePaymentReqAmount
                                > selectedCollectionMember.collectionMember.PendingPrincipal) {
                            selectedCollectionMember.collectionMember.PrePaymentReqAmount =
                                    selectedCollectionMember.collectionMember.PendingPrincipal;
                        }
                        if(selectedCollectionMember.collectionMember.PrePaymentReqAmount
                                < selectedCollectionMember.collectionMember.TotalDueAmountsToPayRefday) {
                            selectedCollectionMember.collectionMember.PrePaymentReqAmount = 0;
                        }
                        int iAutoApprovalLimit = selectedCollectionMember.collectionMember.TotalDueAmountsToPayRefday
                                + (2 * selectedCollectionMember.collectionMember.StandardEMI)
                                - selectedCollectionMember.collectionMember.PrePaymentPrincipalCollected;
                        if(selectedCollectionMember.collectionMember.PrePaymentReqAmount <= iAutoApprovalLimit) {
                            strPrePaymentCollect = getResources().getStringArray(R.array.PrePaymentCollect_Within3Months_array);
                            strPreClosureCollect = getResources().getStringArray(R.array.PreClosureCollect_Within3Months_array);
                            if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                                SQLite_getPrePaymentUpto3MonthsReasonMaster();
                            else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                                SQLite_getPreClosureUpto3MonthsReasonMaster();
                        } else {
                            strPrePaymentCollect = getResources().getStringArray(R.array.PrePaymentCollect_Morethan3Months_array);
                            strPreClosureCollect = getResources().getStringArray(R.array.PreClosureCollect_Morethan3Months_array);
                            if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                                SQLite_getPrePaymentMorethan3MonthsReasonMaster();
                            else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                                SQLite_getPreClosureMorethan3MonthsReasonMaster();
                        }
                        ArrayAdapter<String> adapterPreClosureReasons = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                                R.layout.simple_spinner_item, PrePaymentPreClosureReasons);
                        adapterPreClosureReasons.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                        Spinner spPreClosureReason = (Spinner) rootView.findViewById(R.id.spinnerLoanPreClosureReason);
                        spPreClosureReason.setAdapter(adapterPreClosureReasons);
                        spPreClosureReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                                    selectedCollectionMember.collectionMember.PreClosureReasonID = PrePaymentReasonIDs.get(position);
                                else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                                    selectedCollectionMember.collectionMember.PreClosureReasonID = PreClosureReasonIDs.get(position);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                        spPreClosureReason.setFocusable(true);
                        spPreClosureReason.setFocusableInTouchMode(true);
                        spPreClosureReason.setEnabled(true);

                        EditText et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqAmount);
                        et.setText(String.valueOf(selectedCollectionMember.collectionMember.PrePaymentReqAmount));
                        selectedCollectionMember.collectionMember.PrePaymentReqMonths = 0;
                        et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqMonths);
                        et.setText("0");
                    } catch (NumberFormatException e) {
                        selectedCollectionMember.collectionMember.PrePaymentReqAmount = 0;
                    }
                    InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    try {
                        selectedCollectionMember.collectionMember.PrePaymentReqAmount = Integer.parseInt(((EditText) view).getText().toString());
                        if(selectedCollectionMember.collectionMember.PrePaymentReqAmount
                                > selectedCollectionMember.collectionMember.PendingPrincipal) {
                            selectedCollectionMember.collectionMember.PrePaymentReqAmount =
                                    selectedCollectionMember.collectionMember.PendingPrincipal;
                        }
                        if(selectedCollectionMember.collectionMember.PrePaymentReqAmount
                                < selectedCollectionMember.collectionMember.TotalDueAmountsToPayRefday) {
                            selectedCollectionMember.collectionMember.PrePaymentReqAmount = 0;
                        }
                        int iAutoApprovalLimit = selectedCollectionMember.collectionMember.TotalDueAmountsToPayRefday
                                + (2 * selectedCollectionMember.collectionMember.StandardEMI)
                                - selectedCollectionMember.collectionMember.PrePaymentPrincipalCollected;
                        if(selectedCollectionMember.collectionMember.PrePaymentReqAmount <= iAutoApprovalLimit) {
                            strPrePaymentCollect = getResources().getStringArray(R.array.PrePaymentCollect_Within3Months_array);
                            strPreClosureCollect = getResources().getStringArray(R.array.PreClosureCollect_Within3Months_array);
                            if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                                SQLite_getPrePaymentUpto3MonthsReasonMaster();
                            else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                                SQLite_getPreClosureUpto3MonthsReasonMaster();
                        } else {
                            strPrePaymentCollect = getResources().getStringArray(R.array.PrePaymentCollect_Morethan3Months_array);
                            strPreClosureCollect = getResources().getStringArray(R.array.PreClosureCollect_Morethan3Months_array);
                            if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                                SQLite_getPrePaymentMorethan3MonthsReasonMaster();
                            else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                                SQLite_getPreClosureMorethan3MonthsReasonMaster();
                        }
                        ArrayAdapter<String> adapterPreClosureReasons = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                                R.layout.simple_spinner_item, PrePaymentPreClosureReasons);
                        adapterPreClosureReasons.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                        Spinner spPreClosureReason = (Spinner) rootView.findViewById(R.id.spinnerLoanPreClosureReason);
                        spPreClosureReason.setAdapter(adapterPreClosureReasons);
                        spPreClosureReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                                    selectedCollectionMember.collectionMember.PreClosureReasonID = PrePaymentReasonIDs.get(position);
                                else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                                    selectedCollectionMember.collectionMember.PreClosureReasonID = PreClosureReasonIDs.get(position);
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                        spPreClosureReason.setFocusable(true);
                        spPreClosureReason.setFocusableInTouchMode(true);
                        spPreClosureReason.setEnabled(true);

                        EditText et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqAmount);
                        et.setText(String.valueOf(selectedCollectionMember.collectionMember.PrePaymentReqAmount));
                        selectedCollectionMember.collectionMember.PrePaymentReqMonths = 0;
                        et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqMonths);
                        et.setText("0");
                    } catch (NumberFormatException e) {
                        selectedCollectionMember.collectionMember.PrePaymentReqAmount = 0;
                    }
                }
            }
        });

        dlgDatePicker = new DatePickerDialog(getContext(),
                datepick,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        dlgTimePicker = new TimePickerDialog(getContext(),
                timepick,
                myCalendar.get(Calendar.HOUR_OF_DAY),
                myCalendar.get(Calendar.MINUTE),
                false);

        etNextRecoveryDate = (EditText) rootView.findViewById(R.id.editTextNextRecoveryDate);
        etNextRecoveryDate.setKeyListener(null);
        etNextRecoveryDate.setEnabled(true);
        etNextRecoveryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(etNextRecoveryDate.getWindowToken(), 0);

                if (!dlgDatePicker.isShowing()) {
                    editDateType = 4;
                    dlgDatePicker.show();
                }
            }
        });
        etNextRecoveryDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasFocus) {
                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(etNextRecoveryDate.getWindowToken(), 0);

                if (hasFocus && !(dlgDatePicker.isShowing())) {
                    editDateType = 4;
                    dlgDatePicker.show();
                }
            }
        });
        etNextRecoveryTime = (EditText) rootView.findViewById(R.id.editTextNextRecoveryTime);
        etNextRecoveryTime.setKeyListener(null);
        etNextRecoveryTime.setEnabled(true);
        etNextRecoveryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(etNextRecoveryTime.getWindowToken(), 0);

                if (!dlgTimePicker.isShowing()) {
                    editTimeType = 4;
                    dlgTimePicker.show();
                }
            }
        });
        etNextRecoveryTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasFocus) {
                InputMethodManager keyboard = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(etNextRecoveryTime.getWindowToken(), 0);

                if (hasFocus && !(dlgTimePicker.isShowing())) {
                    editTimeType = 4;
                    dlgTimePicker.show();
                }
            }
        });

        RefreshStartCollection();
        RefreshCollection();
        EnableNarationEdit(false);

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            if(mainActivity == null)
                return;
            if(selectedCollectionMember == null)
                return;

            selectedCollectionMember.collectionMember.strPaymentMode
                    = mainActivity.CollectionMemberList.get(mainActivity.indexCollectionMemberList).strPaymentMode;
            if(selectedCollectionMember.collectionMember.strPaymentMode.equalsIgnoreCase("Online")) {
                selectedCollectionMember.collectionMember.TotalCollectionAmount
                        = mainActivity.CollectionMemberList.get(mainActivity.indexCollectionMemberList).TotalCollectionAmount;
                CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxPaymentOnline);
                cb.setChecked(true);
//                cb.setEnabled(false);
                cb = (CheckBox) rootView.findViewById(R.id.checkBoxPaymentCash);
                cb.setChecked(false);
                cb.setEnabled(false);
                EditText et = (EditText) rootView.findViewById(R.id.editTextTotalCollection);
                et.setText(String.valueOf(selectedCollectionMember.collectionMember.TotalCollectionAmount));
                et.setEnabled(false);
                et.onEditorAction(EditorInfo.IME_ACTION_DONE);
            } else {
                CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxPaymentOnline);
                cb.setChecked(false);
                cb = (CheckBox) rootView.findViewById(R.id.checkBoxPaymentCash);
                cb.setChecked(true);
            }
//bmp 14-May-26
        }
    }

    void RefreshMemberDueDetails() {
        TextView tv = (TextView) rootView.findViewById(R.id.textViewReferencedateTotalDue);
        tv.setText(ddMMMReferencedate + " देय रू.");
        tv = (TextView) rootView.findViewById(R.id.textViewTodateTotalDue);
        tv.setText(ddMMMTodate + " देय रू.");

//        tv = (TextView) rootView.findViewById(R.id.textViewLoanEMIReferencedate);
//        tv.setText("कर्ज हप्ता " + ddMMMReferencedate);
//        tv = (TextView) rootView.findViewById(R.id.textViewLoanEMITodate);
//        tv.setText("कर्ज हप्ता " + ddMMMTodate);

        String strPreClosurePrincipal = "";
        String strPreClosureInterest = "";
        String strPreClosureLateCharge = "";
        String strPreClosureJLG = "";
        String strPreClosurePolicyRenewalAmt = "";
        String strPreClosureADHP = "";
        String strPreClosureRD = "";
        String strPreClosureAmount = "";
        String selectedPreCollect = "";

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String strDate = sdf.format(date);

        if(selectedCollectionMember.collectionMember.EMIReceiptNumber.isEmpty()) {
            //NoLoan
            Spinner spPaymentOptions = (Spinner) rootView.findViewById(R.id.spinnerPaymentOptions);
            spPaymentOptions.setSelection(0);
            spPaymentOptions.setEnabled(false);
            strSelectedPaymentOption = "Monthly";
            EditText et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqMonths);
            et.setEnabled(false);
            et.setText("");
            et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqAmount);
            et.setEnabled(false);
            et.setText("");
            tv = (TextView) rootView.findViewById(R.id.textViewPrePaymentApproval);
            tv.setText("");
            Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerLoanPreClosureReason);
            sp.setEnabled(false);
            sp.setSelection(0);
            CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectAdditionalRepay);
            cb.setEnabled(false);
            cb.setChecked(false);
            et = (EditText) rootView.findViewById(R.id.editTextAdditionalRepay);
            et.setEnabled(false);
            et.setText("");
        } else if (strDate.equalsIgnoreCase(selectedCollectionMember.collectionMember.PreClosureRequestDate)) {
            //Today's Auto/Manual Request
            if (selectedCollectionMember.collectionMember.PreClosureReasonID == 0) {
                //AutoRequest: Reason NOT submitted
                Spinner spPaymentOptions = (Spinner) rootView.findViewById(R.id.spinnerPaymentOptions);
                spPaymentOptions.setEnabled(true);

                EditText et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqMonths);
                et.setText(String.valueOf(selectedCollectionMember.collectionMember.PrePaymentReqMonths));
                et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqAmount);
                et.setText(String.valueOf(selectedCollectionMember.collectionMember.PrePaymentReqAmount));
                tv = (TextView) rootView.findViewById(R.id.textViewPrePaymentApproval);
                tv.setText("");

                strPrePaymentCollect = getResources().getStringArray(R.array.PrePaymentCollect_Within3Months_array);
                strPreClosureCollect = getResources().getStringArray(R.array.PreClosureCollect_Within3Months_array);
                if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                    SQLite_getPrePaymentUpto3MonthsReasonMaster();
                else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                    SQLite_getPreClosureUpto3MonthsReasonMaster();

                ArrayAdapter<String> adapterPreClosureReasons = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                        R.layout.simple_spinner_item, PrePaymentPreClosureReasons);
                adapterPreClosureReasons.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                Spinner spPreClosureReason = (Spinner) rootView.findViewById(R.id.spinnerLoanPreClosureReason);
                spPreClosureReason.setAdapter(adapterPreClosureReasons);
                spPreClosureReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                            selectedCollectionMember.collectionMember.PreClosureReasonID = PrePaymentReasonIDs.get(position);
                        else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                            selectedCollectionMember.collectionMember.PreClosureReasonID = PreClosureReasonIDs.get(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                spPreClosureReason.setFocusable(true);
                spPreClosureReason.setFocusableInTouchMode(true);
                spPreClosureReason.setEnabled(true);
            } else {
                //Request with Reason already submitted
                if ((selectedCollectionMember.collectionMember.PrePaymentReqMonths == 0) &&
                        (selectedCollectionMember.collectionMember.PrePaymentReqAmount == 0)) {
                    //PreClosure Request with Reason already submitted
                    Spinner spPaymentOptions = (Spinner) rootView.findViewById(R.id.spinnerPaymentOptions);
                    spPaymentOptions.setEnabled(false);
                    spPaymentOptions.setSelection(2);
                    strSelectedPaymentOption = "PreClosure";
                    EditText et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqMonths);
                    et.setText("");
                    et.setEnabled(false);
                    et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqAmount);
                    et.setText("");
                    et.setEnabled(false);
                    tv = (TextView) rootView.findViewById(R.id.textViewPrePaymentApproval);
                    tv.setText("");

                    if (flagMDWithinLimit) {
                        strPrePaymentCollect = getResources().getStringArray(R.array.PrePaymentCollect_Within3Months_array);
                        strPreClosureCollect = getResources().getStringArray(R.array.PreClosureCollect_Within3Months_array);
                        if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                            SQLite_getPrePaymentUpto3MonthsReasonMaster();
                        else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                            SQLite_getPreClosureUpto3MonthsReasonMaster();
                    } else {
                        strPrePaymentCollect = getResources().getStringArray(R.array.PrePaymentCollect_Morethan3Months_array);
                        strPreClosureCollect = getResources().getStringArray(R.array.PreClosureCollect_Morethan3Months_array);
                        if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                            SQLite_getPrePaymentMorethan3MonthsReasonMaster();
                        else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                            SQLite_getPreClosureMorethan3MonthsReasonMaster();
                    }
                    ArrayAdapter<String> adapterPreClosureReasons = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                            R.layout.simple_spinner_item, PrePaymentPreClosureReasons);
                    adapterPreClosureReasons.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                    Spinner spPreClosureReason = (Spinner) rootView.findViewById(R.id.spinnerLoanPreClosureReason);
                    spPreClosureReason.setAdapter(adapterPreClosureReasons);
                    spPreClosureReason.setEnabled(false);
                    for (int i = 0; i < PreClosureReasonIDs.size(); i++) {
                        if (PreClosureReasonIDs.get(i) == selectedCollectionMember.collectionMember.PreClosureReasonID) {
                            spPreClosureReason.setSelection(i);
                            selectedPreCollect = strPreClosureCollect[i];
                            break;
                        }
                    }
                } else {
                    //PrePayment Request with Reason already submitted
                    Spinner spPaymentOptions = (Spinner) rootView.findViewById(R.id.spinnerPaymentOptions);
                    spPaymentOptions.setEnabled(false);
                    spPaymentOptions.setSelection(1);
                    strSelectedPaymentOption = "PrePayment";
                    EditText et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqMonths);
                    et.setText(String.valueOf(selectedCollectionMember.collectionMember.PrePaymentReqMonths));
                    et.setEnabled(false);
                    et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqAmount);
                    et.setText(String.valueOf(selectedCollectionMember.collectionMember.PrePaymentReqAmount));
                    et.setEnabled(false);
                    String strApproval = "";
                    if (selectedCollectionMember.collectionMember.PrePaymentApprovedMonths > 0) {
                        strApproval = "Approved महिने: " + String.valueOf(selectedCollectionMember.collectionMember.PrePaymentApprovedMonths);
                    } else if (selectedCollectionMember.collectionMember.PrePaymentApprovedAmount > 0) {
                        strApproval = "Approved रू." + String.valueOf(selectedCollectionMember.collectionMember.PrePaymentApprovedAmount) + "/-";
                    }
                    tv = (TextView) rootView.findViewById(R.id.textViewPrePaymentApproval);
                    tv.setText(strApproval);
                    int iAutoApprovalLimit = selectedCollectionMember.collectionMember.TotalDueAmountsToPayRefday
                            + (2 * selectedCollectionMember.collectionMember.StandardEMI)
                            - selectedCollectionMember.collectionMember.PrePaymentPrincipalCollected;
                    if (    flagMDWithinLimit ||
                            ((selectedCollectionMember.collectionMember.PrePaymentReqAmount > 0) &&
                             (selectedCollectionMember.collectionMember.PrePaymentReqAmount <= iAutoApprovalLimit)) ||
                            ((selectedCollectionMember.collectionMember.PrePaymentReqMonths > 0) &&
                             (selectedCollectionMember.collectionMember.PrePaymentReqMonths <= 2))) {
                        strPrePaymentCollect = getResources().getStringArray(R.array.PrePaymentCollect_Within3Months_array);
                        strPreClosureCollect = getResources().getStringArray(R.array.PreClosureCollect_Within3Months_array);
                        if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                            SQLite_getPrePaymentUpto3MonthsReasonMaster();
                        else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                            SQLite_getPreClosureUpto3MonthsReasonMaster();
                    } else {
                        strPrePaymentCollect = getResources().getStringArray(R.array.PrePaymentCollect_Morethan3Months_array);
                        strPreClosureCollect = getResources().getStringArray(R.array.PreClosureCollect_Morethan3Months_array);
                        if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                            SQLite_getPrePaymentMorethan3MonthsReasonMaster();
                        else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                            SQLite_getPreClosureMorethan3MonthsReasonMaster();
                    }
                    ArrayAdapter<String> adapterPreClosureReasons = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                            R.layout.simple_spinner_item, PrePaymentPreClosureReasons);
                    adapterPreClosureReasons.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                    Spinner spPreClosureReason = (Spinner) rootView.findViewById(R.id.spinnerLoanPreClosureReason);
                    spPreClosureReason.setAdapter(adapterPreClosureReasons);
                    spPreClosureReason.setEnabled(false);
                    for (int i = 0; i < PrePaymentReasonIDs.size(); i++) {
                        if (PrePaymentReasonIDs.get(i) == selectedCollectionMember.collectionMember.PreClosureReasonID) {
                            spPreClosureReason.setSelection(i);
                            selectedPreCollect = strPrePaymentCollect[i];
                            break;
                        }
                    }

                    if(iAutoApprovalLimit <= 0) {
                        selectedCollectionMember.collectionMember.PreClosureRequestStatus = -1;
                        selectedCollectionMember.collectionMember.PrePaymentORPreClosure = 0;
                        selectedCollectionMember.collectionMember.PrePaymentReqMonths = 0;
                        selectedCollectionMember.collectionMember.PrePaymentApprovedMonths = 0;
                        selectedCollectionMember.collectionMember.PrePaymentReqAmount = 0;
                        selectedCollectionMember.collectionMember.PrePaymentApprovedAmount = 0;
                        selectedCollectionMember.collectionMember.PreClosureReasonID = 0;
                        selectedCollectionMember.collectionMember.PreClosureRequestDate = "";
                        selectedCollectionMember.collectionMember.PreClosureRejectReasonID = 0;
                    }
                }

                if (selectedCollectionMember.collectionMember.PreClosureRequestStatus == 0) {
                    strPreClosurePrincipal = "-";
                    strPreClosureInterest = "-";
                    strPreClosureLateCharge = "-";
                    strPreClosureJLG = "-";
                    strPreClosurePolicyRenewalAmt = "-";
                    strPreClosureADHP = "-";
                    strPreClosureRD = "-";
                    strPreClosureAmount = "Pending";
                } else if (selectedCollectionMember.collectionMember.PreClosureRequestStatus == 1) {
                    int iPreClosureJLG = 0;
                    int iPreClosurePolicyRenewalAmt = 0;
                    if(     (selectedCollectionMember.collectionMember.PrePaymentReqMonths > 0) ||
                            (selectedCollectionMember.collectionMember.PrePaymentReqAmount > 0) ) {
                        //PrePayment
                        for (int i = 0; i < PrePaymentReasonIDs.size(); i++) {
                            if (PrePaymentReasonIDs.get(i) == selectedCollectionMember.collectionMember.PreClosureReasonID) {
                                selectedPreCollect = strPrePaymentCollect[i];
                                break;
                            }
                        }
                        CalculatePrePaymentDetails(selectedPreCollect);
                        iPreClosureJLG = selectedCollectionMember.collectionMember.PreClosureJLG;
                        iPreClosurePolicyRenewalAmt = selectedCollectionMember.collectionMember.PreClosurePolicyRenewalAmt;
                    } else {
                        //PreClosure
                        for (int i = 0; i < PreClosureReasonIDs.size(); i++) {
                            if (PreClosureReasonIDs.get(i) == selectedCollectionMember.collectionMember.PreClosureReasonID) {
                                selectedPreCollect = strPreClosureCollect[i];
                                break;
                            }
                        }
                        if (selectedPreCollect.contains("JLG")) {
                            iPreClosureJLG = selectedCollectionMember.collectionMember.PreClosureJLG;
                            iPreClosurePolicyRenewalAmt = selectedCollectionMember.collectionMember.PreClosurePolicyRenewalAmt;
                        } else {
                            iPreClosureJLG = selectedCollectionMember.collectionMember.Expected_SD;
                            iPreClosurePolicyRenewalAmt = selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt;
                        }

                        int iPreClosureADHP
                                = selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt;
                        if (selectedPreCollect.contains("ADHP")) {
                            if (selectedCollectionMember.collectionMember.PendingPrincipal > 0) {
                                iPreClosureADHP = iPreClosureADHP +
                                        (iInstallmentsTillMD * selectedCollectionMember.collectionMember.Monthly_ADHP);
                            }
                        }
                        selectedCollectionMember.collectionMember.PreClosureADHP = iPreClosureADHP;
//bmp 16-Jul-26
                        //PreClosure part payment -> already collected PreClosureADHP as ExtraADHPCollectionAmount
                        selectedCollectionMember.collectionMember.PreClosureADHP =
                                selectedCollectionMember.collectionMember.PreClosureADHP
                              - selectedCollectionMember.collectionMember.ExtraADHPAmountCollected;
                        if(selectedCollectionMember.collectionMember.PreClosureADHP < 0) {
                            selectedCollectionMember.collectionMember.PreClosureADHP = 0;
                        }
//bmp 16-Jul-26
                    }
                    strPreClosurePrincipal = String.valueOf(selectedCollectionMember.collectionMember.PreClosurePrincipal);
                    strPreClosureInterest = String.valueOf(selectedCollectionMember.collectionMember.PreClosureInterest);
                    strPreClosureLateCharge = String.valueOf(selectedCollectionMember.collectionMember.PreClosureLatePayCharge);
                    strPreClosureJLG = String.valueOf(iPreClosureJLG);
                    strPreClosurePolicyRenewalAmt = String.valueOf(iPreClosurePolicyRenewalAmt);
                    strPreClosureADHP = String.valueOf(selectedCollectionMember.collectionMember.PreClosureADHP);

                    int iPreClosureRD = 0;
                    if (selectedPreCollect.contains("RD")) {
                        for(int k=0; k<selectedCollectionMember.memberRDReceipt.size(); k++) {
                            switch (k) {
                                case 0:
                                    iPreClosureRD = iPreClosureRD + selectedCollectionMember.collectionMember.RD1ExpectedAmount;
                                    break;
                                case 1:
                                    iPreClosureRD = iPreClosureRD + selectedCollectionMember.collectionMember.RD2ExpectedAmount;
                                    break;
                                case 2:
                                    iPreClosureRD = iPreClosureRD + selectedCollectionMember.collectionMember.RD3ExpectedAmount;
                                    break;
                                case 3:
                                    iPreClosureRD = iPreClosureRD + selectedCollectionMember.collectionMember.RD4ExpectedAmount;
                                    break;
                                case 4:
                                    iPreClosureRD = iPreClosureRD + selectedCollectionMember.collectionMember.RD5ExpectedAmount;
                                    break;
                            }
                        }
                    }
                    selectedCollectionMember.collectionMember.PreClosureRD = iPreClosureRD;
                    strPreClosureRD = String.valueOf(selectedCollectionMember.collectionMember.PreClosureRD);
                    int iPreClosureAmount = selectedCollectionMember.collectionMember.PreClosurePrincipal
                            + selectedCollectionMember.collectionMember.PreClosureInterest
                            + selectedCollectionMember.collectionMember.PreClosureLatePayCharge
                            + iPreClosureJLG
                            + iPreClosurePolicyRenewalAmt
                            + selectedCollectionMember.collectionMember.PreClosureADHP
                            + selectedCollectionMember.collectionMember.PreClosureRD;
                    selectedCollectionMember.collectionMember.PreClosureAmount = iPreClosureAmount;
                    strPreClosureAmount = String.valueOf(iPreClosureAmount);
                } else if (selectedCollectionMember.collectionMember.PreClosureRequestStatus == 2) {
                    strPreClosurePrincipal = "-";
                    strPreClosureInterest = "-";
                    strPreClosureLateCharge = "-";
                    strPreClosureJLG = "-";
                    strPreClosurePolicyRenewalAmt = "-";
                    strPreClosureADHP = "-";
                    strPreClosureRD = "-";
                    strPreClosureAmount = "Rejected";
                }
            }
        } else {
            //Request Not yet submitted today
            Spinner spPaymentOptions = (Spinner) rootView.findViewById(R.id.spinnerPaymentOptions);
            spPaymentOptions.setEnabled(true);
            if (strSelectedPaymentOption.equalsIgnoreCase("Monthly")) {
                spPaymentOptions.setSelection(0);
            } else {
                EditText et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqMonths);
                et.setText(String.valueOf(selectedCollectionMember.collectionMember.PrePaymentReqMonths));
                et = (EditText) rootView.findViewById(R.id.editTextPrePaymentReqAmount);
                et.setText(String.valueOf(selectedCollectionMember.collectionMember.PrePaymentReqAmount));
                tv = (TextView) rootView.findViewById(R.id.textViewPrePaymentApproval);
                tv.setText("");

                int iAutoApprovalLimit = selectedCollectionMember.collectionMember.TotalDueAmountsToPayRefday
                        + (2 * selectedCollectionMember.collectionMember.StandardEMI)
                        - selectedCollectionMember.collectionMember.PrePaymentPrincipalCollected;
                if (    flagMDWithinLimit ||
                        ((selectedCollectionMember.collectionMember.PrePaymentReqAmount > 0) && (selectedCollectionMember.collectionMember.PrePaymentReqAmount <= iAutoApprovalLimit)) ||
                        ((selectedCollectionMember.collectionMember.PrePaymentReqMonths > 0) &&
                                (selectedCollectionMember.collectionMember.PrePaymentReqMonths <= 2)) ) {
                    strPrePaymentCollect = getResources().getStringArray(R.array.PrePaymentCollect_Within3Months_array);
                    strPreClosureCollect = getResources().getStringArray(R.array.PreClosureCollect_Within3Months_array);
                    if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                        SQLite_getPrePaymentUpto3MonthsReasonMaster();
                    else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                        SQLite_getPreClosureUpto3MonthsReasonMaster();
                } else {
                    strPrePaymentCollect = getResources().getStringArray(R.array.PrePaymentCollect_Morethan3Months_array);
                    strPreClosureCollect = getResources().getStringArray(R.array.PreClosureCollect_Morethan3Months_array);
                    if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                        SQLite_getPrePaymentMorethan3MonthsReasonMaster();
                    else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                        SQLite_getPreClosureMorethan3MonthsReasonMaster();
                }
                ArrayAdapter<String> adapterPreClosureReasons = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                        R.layout.simple_spinner_item, PrePaymentPreClosureReasons);
                adapterPreClosureReasons.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                Spinner spPreClosureReason = (Spinner) rootView.findViewById(R.id.spinnerLoanPreClosureReason);
                spPreClosureReason.setAdapter(adapterPreClosureReasons);
                spPreClosureReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                            selectedCollectionMember.collectionMember.PreClosureReasonID = PrePaymentReasonIDs.get(position);
                        else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                            selectedCollectionMember.collectionMember.PreClosureReasonID = PreClosureReasonIDs.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                spPreClosureReason.setFocusable(true);
                spPreClosureReason.setFocusableInTouchMode(true);
                spPreClosureReason.setEnabled(true);
                for (int i = 0; i < PrePaymentReasonIDs.size(); i++) {
                    if (PrePaymentReasonIDs.get(i) == selectedCollectionMember.collectionMember.PreClosureReasonID) {
                        spPreClosureReason.setSelection(i);
                        break;
                    }
                }
                for (int i = 0; i < PreClosureReasonIDs.size(); i++) {
                    if (PreClosureReasonIDs.get(i) == selectedCollectionMember.collectionMember.PreClosureReasonID) {
                        spPreClosureReason.setSelection(i);
                        break;
                    }
                }
            }
        }
//bmp 24-Jun-26
//        if (strSelectedPaymentOption.equalsIgnoreCase("Monthly")) {
//bmp 11-Jul-26
        int iDeltaEMI = 0;
//bmp 11-Jul-26

//bmp 21-Jul-26
////bmp 14-Jul-26
////        if (selectedCollectionMember.collectionMember.PrePaymentORPreClosure == 0) {
//        if (    (selectedCollectionMember.collectionMember.PrePaymentORPreClosure == 0) ||
//                (selectedCollectionMember.collectionMember.PreClosureRequestStatus == 0) ||
//                (selectedCollectionMember.collectionMember.PreClosureReasonID == 0) ||
//                (strSelectedPaymentOption.equalsIgnoreCase("Monthly")) ) {
////bmp 14-Jul-26
        if (    (selectedCollectionMember.collectionMember.PrePaymentORPreClosure == 0) ||
                (selectedCollectionMember.collectionMember.PreClosureRequestStatus == -1) ||
                (selectedCollectionMember.collectionMember.PreClosureReasonID == 0) ||
                (strSelectedPaymentOption.equalsIgnoreCase("Monthly")) ) {
//bmp 21-Jul-26
            int iDuePrincipal = selectedCollectionMember.collectionMember.Expected_Principal + selectedCollectionMember.collectionMember.PastDue_Principal;
            int iDueInterest = selectedCollectionMember.collectionMember.Expected_Interest + selectedCollectionMember.collectionMember.PastDue_Interest;
//bmp 10-Jul-26
//            int iInterestDelta = selectedCollectionMember.collectionMember.TotalDueAmountsToPayToday - selectedCollectionMember.collectionMember.TotalDueAmountsToPayRefday;
            int iInterestDelta = InterestDeltaTodate(selectedCollectionMember.indexCollectionMemberList);
//bmp 10-Jul-26
//bmp 16-Jul-26
            int iTemp = selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtRefday -
                    selectedCollectionMember.collectionMember.Expected_SD -
                    selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt -
                    selectedCollectionMember.collectionMember.Expected_LatePaymentCharges -
                    selectedCollectionMember.collectionMember.Expected_Penalty -
                    selectedCollectionMember.collectionMember.PastDue_SD -
                    selectedCollectionMember.collectionMember.PastDue_PolicyRenewalAmt -
                    selectedCollectionMember.collectionMember.PastDue_LatePaymentCharges -
                    selectedCollectionMember.collectionMember.PastDue_Penalty;
            if(iTemp <= 0) {
                iInterestDelta = 0;
            }
//bmp 16-Jul-26
            iDueInterest = iDueInterest + iInterestDelta;
            if(iInterestDelta < 0) {
                iDuePrincipal = iDuePrincipal - iInterestDelta;
            }
            int iDueLateCharges = selectedCollectionMember.collectionMember.Expected_LatePaymentCharges + selectedCollectionMember.collectionMember.PastDue_LatePaymentCharges;
            int iDueJLG = selectedCollectionMember.collectionMember.Expected_SD + selectedCollectionMember.collectionMember.PastDue_SD;
            int iDuePolicyRenewalAmt = selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt + selectedCollectionMember.collectionMember.PastDue_PolicyRenewalAmt;
            int iDueADHP = selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt;
//bmp 08-Jul-26
//            int iDueRD = selectedCollectionMember.collectionMember.RD1ExpectedAmount + selectedCollectionMember.collectionMember.RD1PastDue
//                    + selectedCollectionMember.collectionMember.RD2ExpectedAmount + selectedCollectionMember.collectionMember.RD2PastDue
//                    + selectedCollectionMember.collectionMember.RD3ExpectedAmount + selectedCollectionMember.collectionMember.RD3PastDue
//                    + selectedCollectionMember.collectionMember.RD4ExpectedAmount + selectedCollectionMember.collectionMember.RD4PastDue
//                    + selectedCollectionMember.collectionMember.RD5ExpectedAmount + selectedCollectionMember.collectionMember.RD5PastDue;
            int iDueRD = selectedCollectionMember.collectionMember.RD1ExpectedAmount
                    + selectedCollectionMember.collectionMember.RD2ExpectedAmount
                    + selectedCollectionMember.collectionMember.RD3ExpectedAmount
                    + selectedCollectionMember.collectionMember.RD4ExpectedAmount
                    + selectedCollectionMember.collectionMember.RD5ExpectedAmount;
//bmp 08-Jul-26
            int iDueTotal = iDuePrincipal + iDueInterest + iDueLateCharges + iDueJLG + iDuePolicyRenewalAmt + iDueADHP + iDueRD;
//bmp 11-Jul-26
            if((iDueInterest == 0) && (iDueTotal < selectedCollectionMember.collectionMember.TotalDueAmountsToPayToday)) {
                iDeltaEMI = selectedCollectionMember.collectionMember.TotalDueAmountsToPayToday - iDueTotal;
                iDuePrincipal = iDuePrincipal + iDeltaEMI;
                iDueTotal = selectedCollectionMember.collectionMember.TotalDueAmountsToPayToday;
//bmp 13-Jul-26
            } else if(iInterestDelta > 0) {
                iDeltaEMI = iInterestDelta;
//bmp 13-Jul-26
            }
//bmp 11-Jul-26
            tv = (TextView) rootView.findViewById(R.id.textViewLoanPrincipal);
            tv.setText(String.valueOf(iDuePrincipal));
            tv = (TextView) rootView.findViewById(R.id.textViewLoanInterest);
            tv.setText(String.valueOf(iDueInterest));
            tv = (TextView) rootView.findViewById(R.id.textViewLoanLateCharge);
            tv.setText(String.valueOf(iDueLateCharges));
            tv = (TextView) rootView.findViewById(R.id.textViewLoanJLG);
            tv.setText(String.valueOf(iDueJLG));
            tv = (TextView) rootView.findViewById(R.id.textViewLoanPolicyRenewalAmt);
            tv.setText(String.valueOf(iDuePolicyRenewalAmt));
            tv = (TextView) rootView.findViewById(R.id.textViewLoanADHP);
            tv.setText(String.valueOf(iDueADHP));
            tv = (TextView) rootView.findViewById(R.id.textViewLoanRD);
            tv.setText(String.valueOf(iDueRD));
            tv = (TextView) rootView.findViewById(R.id.textViewLoanTotal);
            tv.setText(String.valueOf(iDueTotal));
        } else {
//bmp 24-Jun-26
            tv = (TextView) rootView.findViewById(R.id.textViewLoanPrincipal);
            tv.setText(strPreClosurePrincipal);
            tv = (TextView) rootView.findViewById(R.id.textViewLoanInterest);
            tv.setText(strPreClosureInterest);
            tv = (TextView) rootView.findViewById(R.id.textViewLoanLateCharge);
            tv.setText(strPreClosureLateCharge);
            tv = (TextView) rootView.findViewById(R.id.textViewLoanJLG);
            tv.setText(strPreClosureJLG);
            tv = (TextView) rootView.findViewById(R.id.textViewLoanPolicyRenewalAmt);
            tv.setText(strPreClosurePolicyRenewalAmt);
            tv = (TextView) rootView.findViewById(R.id.textViewLoanADHP);
            tv.setText(strPreClosureADHP);
            tv = (TextView) rootView.findViewById(R.id.textViewLoanRD);
            tv.setText(strPreClosureRD);
            tv = (TextView) rootView.findViewById(R.id.textViewLoanTotal);
            tv.setText(strPreClosureAmount);
//bmp 24-Jun-26
        }
//bmp 24-Jun-26

        tv = (TextView) rootView.findViewById(R.id.textViewTotalDueReferencedate);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.TotalDueAmountsToPayRefday));
        tv = (TextView) rootView.findViewById(R.id.textViewTotalDueTodate);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.TotalDueAmountsToPayToday));

//        tv = (TextView) rootView.findViewById(R.id.textViewReferencedateLoanEMIPast);
//        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.LoanPastDueAmountTillRefday));
//        tv = (TextView) rootView.findViewById(R.id.textViewReferencedateLoanEMICurrent);
//        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.LoanCurrentDueAmountRefday));
//        tv = (TextView) rootView.findViewById(R.id.textViewReferencedateLoanEMI);
//        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtRefday));
//bmp 10-Jul-26
//        tv = (TextView) rootView.findViewById(R.id.textViewTodateLoanEMIPast);
////        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.LoanPastDueAmountTillToday));
//        int iPplusIPast = selectedCollectionMember.collectionMember.LoanPastDueAmountTillToday
//                - selectedCollectionMember.collectionMember.PastDue_SD
//                - selectedCollectionMember.collectionMember.PastDue_PolicyRenewalAmt;
////bmp 09-Jul-26
//        if(iPplusIPast <= 0)
//            iPplusIPast = 0;
////bmp 09-Jul-26
//        tv.setText(String.valueOf(iPplusIPast));
//
//        tv = (TextView) rootView.findViewById(R.id.textViewTodateLoanEMICurrent);
////        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.LoanCurrentDueAmountToday));
//        int iPplusICurrent = selectedCollectionMember.collectionMember.LoanCurrentDueAmountToday
//                - selectedCollectionMember.collectionMember.Expected_SD
//                - selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt;
////bmp 09-Jul-26
//        if(iPplusICurrent <= 0)
//            iPplusICurrent = 0;
////bmp 09-Jul-26
//        tv.setText(String.valueOf(iPplusICurrent));
//        tv = (TextView) rootView.findViewById(R.id.textViewTodateLoanEMI);
////        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtToday));
//        int iPplusIPastplusCurrent = iPplusIPast + iPplusICurrent;
//        tv.setText(String.valueOf(iPplusIPastplusCurrent));

        int iIplusPplusLateFeePast = selectedCollectionMember.collectionMember.PastDue_Interest
                + selectedCollectionMember.collectionMember.PastDue_Principal
                + selectedCollectionMember.collectionMember.PastDue_LatePaymentCharges;
        tv = (TextView) rootView.findViewById(R.id.textViewTodateLoanEMIPast);
        tv.setText(String.valueOf(iIplusPplusLateFeePast));
        int iIplusPplusLateFeeCurrent = selectedCollectionMember.collectionMember.Expected_Interest
                + selectedCollectionMember.collectionMember.Expected_Principal
                + selectedCollectionMember.collectionMember.Expected_LatePaymentCharges;
//bmp 11-Jul-26
        iIplusPplusLateFeeCurrent = iIplusPplusLateFeeCurrent + iDeltaEMI;
//bmp 11-Jul-26
        tv = (TextView) rootView.findViewById(R.id.textViewTodateLoanEMICurrent);
        tv.setText(String.valueOf(iIplusPplusLateFeeCurrent));
        int iIplusPplusLateFeePastplusCurrent = iIplusPplusLateFeePast + iIplusPplusLateFeeCurrent;
        tv = (TextView) rootView.findViewById(R.id.textViewTodateLoanEMI);
        tv.setText(String.valueOf(iIplusPplusLateFeePastplusCurrent));
//bmp 10-Jul-26

        tv = (TextView) rootView.findViewById(R.id.textViewJLGPast);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.PastDue_SD));
        tv = (TextView) rootView.findViewById(R.id.textViewJLGCurrent);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.Expected_SD));
        tv = (TextView) rootView.findViewById(R.id.textViewTodateJLG);
        int iSDPastDuePlusCurrentDue = selectedCollectionMember.collectionMember.PastDue_SD
                + selectedCollectionMember.collectionMember.Expected_SD;
        tv.setText(String.valueOf(iSDPastDuePlusCurrentDue));

        tv = (TextView) rootView.findViewById(R.id.textViewNidhiPast);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.PastDue_PolicyRenewalAmt));
        tv = (TextView) rootView.findViewById(R.id.textViewNidhiCurrent);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt));
        tv = (TextView) rootView.findViewById(R.id.textViewTodateNidhi);
        int iNidhiPastDuePlusCurrentDue = selectedCollectionMember.collectionMember.PastDue_PolicyRenewalAmt
                + selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt;
        tv.setText(String.valueOf(iNidhiPastDuePlusCurrentDue));

        tv = (TextView) rootView.findViewById(R.id.textViewADHPPast);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.ADHPPastDueAmountTillToday));
        tv = (TextView) rootView.findViewById(R.id.textViewADHPCurrent);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.ADHPContributionAmount));
        tv = (TextView) rootView.findViewById(R.id.textViewADHP);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt));
//bmp 12-Aug-26
        if(selectedCollectionMember.collectionMember.Internal_Transfer_Reason != null) {
            if(!selectedCollectionMember.collectionMember.Internal_Transfer_Reason.isEmpty()) {
                tv = (TextView) rootView.findViewById(R.id.textViewPrePaymentApproval);
                tv.setText(selectedCollectionMember.collectionMember.Internal_Transfer_Reason);
                tv.setBackgroundColor(Color.argb(255, 250, 180, 180));
            }
        }
//bmp 12-Aug-26
//bmp 05-Aug-26
        if(selectedCollectionMember.collectionMember.Internal_Transfer_Reason.equalsIgnoreCase("FLMFType1")) {
            //FLMF member
//bmp 12-Aug-26
////bmp 06-Aug-26
//            tv = (TextView) rootView.findViewById(R.id.textViewPrePaymentApproval);
//            tv.setText(selectedCollectionMember.collectionMember.Internal_Transfer_Reason);
//            tv.setBackgroundColor(Color.argb(255, 250, 180, 180));
////bmp 06-Aug-26
//bmp 12-Aug-26
            CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectJLG);
            cb.setVisibility(View.VISIBLE);
            cb.setEnabled(iSDPastDuePlusCurrentDue > 0);
            cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectNidhi);
            cb.setVisibility(View.VISIBLE);
            cb.setEnabled(iNidhiPastDuePlusCurrentDue > 0);
            cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectADHP);
            cb.setVisibility(View.VISIBLE);
            cb.setEnabled(selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt > 0);
        } else {
//bmp 06-Aug-26
            tv = (TextView) rootView.findViewById(R.id.textViewPrePaymentApproval);
            tv.setText("");
            int color = Color.WHITE;
            TextView tv1 = (TextView) rootView.findViewById(R.id.textViewReferencedateTotalDue);
            Drawable background = tv1.getBackground();
            if (background instanceof ColorDrawable) {
                color = ((ColorDrawable) background).getColor();
                // 'color' is the int representation of the background color
            }
            tv.setBackgroundColor(color);
//bmp 06-Aug-26
            CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectJLG);
            cb.setVisibility(View.INVISIBLE);
            cb.setChecked(iSDPastDuePlusCurrentDue > 0);
            cb.setEnabled(false);
            cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectNidhi);
            cb.setVisibility(View.INVISIBLE);
            cb.setChecked(iNidhiPastDuePlusCurrentDue > 0);
            cb.setEnabled(false);
            cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectADHP);
            cb.setVisibility(View.INVISIBLE);
            cb.setChecked(selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt > 0);
            cb.setEnabled(false);
        }
//bmp 05-Aug-26
        tv = (TextView) rootView.findViewById(R.id.textViewRDCount);
        tv.setText("RD Count: " + String.valueOf(selectedCollectionMember.memberRDReceipt.size()));

        for(int k=0; k<selectedCollectionMember.memberRDReceipt.size(); k++) {
            if(k >= 5) {
                System.out.println("Total RD Receipts > 5");
                break;
            }
            switch(k) {
                case 0:
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD1);
                    cb.setText(selectedCollectionMember.memberRDReceipt.get(k).FirstAcHolder);
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount1Past);
                    tv.setText(String.valueOf(selectedCollectionMember.collectionMember.RD1PastDue));
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount1Current);
                    tv.setText(String.valueOf(selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount));
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount1);
                    tv.setText(String.valueOf(selectedCollectionMember.collectionMember.RD1ExpectedAmount));
                    break;
                case 1:
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD2);
                    cb.setText(selectedCollectionMember.memberRDReceipt.get(k).FirstAcHolder);
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount2Past);
                    tv.setText(String.valueOf(selectedCollectionMember.collectionMember.RD2PastDue));
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount2Current);
                    tv.setText(String.valueOf(selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount));
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount2);
                    tv.setText(String.valueOf(selectedCollectionMember.collectionMember.RD2ExpectedAmount));
                    break;
                case 2:
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD3);
                    cb.setText(selectedCollectionMember.memberRDReceipt.get(k).FirstAcHolder);
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount3Past);
                    tv.setText(String.valueOf(selectedCollectionMember.collectionMember.RD3PastDue));
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount3Current);
                    tv.setText(String.valueOf(selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount));
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount3);
                    tv.setText(String.valueOf(selectedCollectionMember.collectionMember.RD3ExpectedAmount));
                    break;
                case 3:
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD4);
                    cb.setText(selectedCollectionMember.memberRDReceipt.get(k).FirstAcHolder);
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount4Past);
                    tv.setText(String.valueOf(selectedCollectionMember.collectionMember.RD4PastDue));
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount4Current);
                    tv.setText(String.valueOf(selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount));
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount4);
                    tv.setText(String.valueOf(selectedCollectionMember.collectionMember.RD4ExpectedAmount));
                    break;
                case 4:
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD5);
                    cb.setText(selectedCollectionMember.memberRDReceipt.get(k).FirstAcHolder);
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount5Past);
                    tv.setText(String.valueOf(selectedCollectionMember.collectionMember.RD5PastDue));
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount5Current);
                    tv.setText(String.valueOf(selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount));
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount5);
                    tv.setText(String.valueOf(selectedCollectionMember.collectionMember.RD5ExpectedAmount));
                    break;
            }
        }

        for(int k=selectedCollectionMember.memberRDReceipt.size(); k<5; k++) {
            switch(k) {
                case 0:
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD1);
                    cb.setText("");
                    cb.setChecked(false);
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount1Past);
                    tv.setText("");
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount1Current);
                    tv.setText("");
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount1);
                    tv.setText("");
                    break;
                case 1:
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD2);
                    cb.setText("");
                    cb.setChecked(false);
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount2Past);
                    tv.setText("");
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount2Current);
                    tv.setText("");
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount2);
                    tv.setText("");
                    break;
                case 2:
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD3);
                    cb.setText("");
                    cb.setChecked(false);
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount3Past);
                    tv.setText("");
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount3Current);
                    tv.setText("");
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount3);
                    tv.setText("");
                    break;
                case 3:
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD4);
                    cb.setText("");
                    cb.setChecked(false);
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount4Past);
                    tv.setText("");
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount4Current);
                    tv.setText("");
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount4);
                    tv.setText("");
                    break;
                case 4:
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD5);
                    cb.setText("");
                    cb.setChecked(false);
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount5Past);
                    tv.setText("");
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount5Current);
                    tv.setText("");
                    tv = (TextView) rootView.findViewById(R.id.textViewRDAmount5);
                    tv.setText("");
                    break;
            }
        }

        EditText et = (EditText) rootView.findViewById(R.id.editTextPhoneNo);
        et.setText(selectedCollectionMember.collectionMember.PhoneNo);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String strTemp = v.getText().toString();
                    if(strTemp == null)
                        strTemp = "0";
                    else if(strTemp.isEmpty())
                        strTemp = "0";
                    else if(strTemp.length() < 10)
                        strTemp = "0";
                    if(!strTemp.equalsIgnoreCase("0")) {
                        selectedCollectionMember.collectionMember.PhoneNo = strTemp;
                    } else {
                        CreateAlertDialog(true, false);
                        dlgState = ufin_COLLECTION_DIALOG_STATE_INCORRECT_MOBILE_NO;
                        dlgAlert.setMessage(MSG_ufin_COLLECTION_INCORRECT_MOBILE_NO);
                        AlertDialog dialog = dlgAlert.create();
                        dialog.show();
                        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                        textView.setTextSize(30);
                        textView.setTextColor(Color.RED);
                    }
                    InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    String strTemp = ((EditText) view).getText().toString();
                    if(strTemp == null)
                        strTemp = "0";
                    else if(strTemp.isEmpty())
                        strTemp = "0";
                    else if(strTemp.length() < 10)
                        strTemp = "0";
                    if(!strTemp.equalsIgnoreCase("0")) {
                        selectedCollectionMember.collectionMember.PhoneNo = strTemp;
                    } else {
                        CreateAlertDialog(true, false);
                        dlgState = ufin_COLLECTION_DIALOG_STATE_INCORRECT_MOBILE_NO;
                        dlgAlert.setMessage(MSG_ufin_COLLECTION_INCORRECT_MOBILE_NO);
                        AlertDialog dialog = dlgAlert.create();
                        dialog.show();
                        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                        textView.setTextSize(30);
                        textView.setTextColor(Color.RED);
                    }
                }
            }
        });
        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxPhoneNo);
        cb.setChecked(selectedCollectionMember.collectionMember.VerifiedPhoneNo);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCollectionMember.collectionMember.VerifiedPhoneNo = ((CheckBox) v).isChecked();
                mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).VerifiedPhoneNo
                        = ((CheckBox) v).isChecked();
            }
        });
//bmp 09-May-26
        CheckBox cbCash = (CheckBox) rootView.findViewById(R.id.checkBoxPaymentCash);
        cbCash.setChecked(false);
        cbCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cbOnline = (CheckBox) rootView.findViewById(R.id.checkBoxPaymentOnline);
                cbOnline.setChecked(false);
                if(selectedCollectionMember.collectionMember.TotalCollectionAmount > 0) {
                    if(selectedCollectionMember.collectionMember.strPaymentMode.equalsIgnoreCase("Online")) {
                        iCenterValueOnline = iCenterValueOnline - selectedCollectionMember.collectionMember.TotalCollectionAmount;
                        if(iCenterValueOnline < 0)
                            iCenterValueOnline = 0;
                    }
                    selectedCollectionMember.collectionMember.strPaymentMode = "Cash";
                    EditText et = (EditText) rootView.findViewById(R.id.editTextTotalCollection);
                    et.setText(String.valueOf(selectedCollectionMember.collectionMember.TotalCollectionAmount));
                    et.onEditorAction(EditorInfo.IME_ACTION_DONE);
                }
            }
        });
        CheckBox cbOnline = (CheckBox) rootView.findViewById(R.id.checkBoxPaymentOnline);
        cbOnline.setChecked(false);
        cbOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cbCash = (CheckBox) rootView.findViewById(R.id.checkBoxPaymentCash);
                cbCash.setChecked(false);
//bmp 14-May-26
//Update strPaymentMode only after Online Collection Validation and Save
//                selectedCollectionMember.collectionMember.strPaymentMode = "Online";
//bmp 14-May-26
                mainActivity.indexCollectionMemberList = selectedCollectionMember.indexCollectionMemberList;
//bmp 14-Jul-26
//                mainActivity.iPrePaymentPreClosureAmount = selectedCollectionMember.collectionMember.PreClosureAmount;
                mainActivity.iPrePaymentPreClosureAmount = 0;
                if(     (selectedCollectionMember.collectionMember.PrePaymentORPreClosure > 0) &&
                        (selectedCollectionMember.collectionMember.PreClosureRequestStatus == 1) &&
                        (selectedCollectionMember.collectionMember.PreClosureReasonID > 0) ) {
                    mainActivity.iPrePaymentPreClosureAmount = selectedCollectionMember.collectionMember.PreClosureAmount;
                }
//bmp 14-Jul-26
                CustomViewPager viewPager = (CustomViewPager) mainActivity.findViewById(R.id.viewpagerCollection);
                if (viewPager != null) {
                    viewPager.requestFocus();
                    mainActivity.iPrevViewId = R.id.viewpagerCollection;
                    mainActivity.iCurrentScreen = 2;
                    viewPager.setCurrentItem(mainActivity.iCurrentScreen);
                }
            }
        });
        if(selectedCollectionMember.collectionMember.strPaymentMode.equalsIgnoreCase("Cash")) {
            cbCash.setChecked(true);
            cbCash.setEnabled(true);
            cbOnline.setChecked(false);
            cbOnline.setEnabled(true);
            et = (EditText) rootView.findViewById(R.id.editTextTotalCollection);
            et.setEnabled(true);
        } else if(selectedCollectionMember.collectionMember.strPaymentMode.equalsIgnoreCase("Online")) {
            cbOnline.setChecked(true);
            cbOnline.setEnabled(true);
            cbCash.setChecked(false);
            cbCash.setEnabled(false);
            et = (EditText) rootView.findViewById(R.id.editTextTotalCollection);
            et.setEnabled(false);
        }
//bmp 09-May-26
        et = (EditText) rootView.findViewById(R.id.editTextTotalCollection);
        if(selectedCollectionMember.collectionMember.TotalCollectionAmount > 0)
            et.setText(String.valueOf(selectedCollectionMember.collectionMember.TotalCollectionAmount));
        else
            et.setText("");
//bmp 29-Apr-26
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        if(     (selectedCollectionMember.collectionMember.PrePaymentORPreClosure != 0) &&
                                (selectedCollectionMember.collectionMember.PreClosureRequestStatus != 1) ) {
                            selectedCollectionMember.collectionMember.TotalCollectionAmount = 0;
                            EditText et = (EditText) rootView.findViewById(R.id.editTextTotalCollection);
                            et.setText(String.valueOf(selectedCollectionMember.collectionMember.TotalCollectionAmount));
                            InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            CreateAlertDialog(false, true);
                            dlgState = ufin_COLLECTION_DIALOG_STATE_INVALID_RECOVERY_MODE_SET;
                            dlgAlert.setMessage(MSG_ufin_COLLECTION_INVALID_RECOVERY_MODE_SET);
                            AlertDialog dialog = dlgAlert.create();
                            dialog.show();
                            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                            textView.setTextSize(30);
                            textView.setTextColor(Color.RED);
                            return false;
                        }
                        selectedCollectionMember.collectionMember.TotalCollectionAmount = Integer.parseInt(v.getText().toString());
                        //Do not allow recovery amount more than 3 EMIs + ADHP + RD
                        int iMaxCollectionAmount = 0;
                        iMaxCollectionAmount = 3 * selectedCollectionMember.collectionMember.TotalDueAmountsToPayRefday;
                        Calendar cal = Calendar.getInstance();
                        Date date = cal.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        String strDate = sdf.format(date);
                        int iPaymentType = 0;
                        if(strDate.equalsIgnoreCase(selectedCollectionMember.collectionMember.PreClosureRequestDate)) {
                            if(selectedCollectionMember.collectionMember.PreClosureRequestStatus == 1) {
                                iMaxCollectionAmount = selectedCollectionMember.collectionMember.PreClosureAmount;
                                if(     (selectedCollectionMember.collectionMember.PrePaymentReqMonths > 0) ||
                                        (selectedCollectionMember.collectionMember.PrePaymentReqAmount > 0)) {
                                    iPaymentType = 1;
                                } else {
                                    iPaymentType = 2;
                                }
                            }
                        }
                        iMaxCollectionAmount = iMaxCollectionAmount + selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt;
                        iMaxCollectionAmount = iMaxCollectionAmount + selectedCollectionMember.collectionMember.RD1ExpectedAmount;
                        iMaxCollectionAmount = iMaxCollectionAmount + selectedCollectionMember.collectionMember.RD2ExpectedAmount;
                        iMaxCollectionAmount = iMaxCollectionAmount + selectedCollectionMember.collectionMember.RD3ExpectedAmount;
                        iMaxCollectionAmount = iMaxCollectionAmount + selectedCollectionMember.collectionMember.RD4ExpectedAmount;
                        iMaxCollectionAmount = iMaxCollectionAmount + selectedCollectionMember.collectionMember.RD5ExpectedAmount;
                        iMaxCollectionAmount = iMaxCollectionAmount + 10000;
                        if(selectedCollectionMember.collectionMember.TotalCollectionAmount > iMaxCollectionAmount) {
                            selectedCollectionMember.collectionMember.TotalCollectionAmount = 0;
                            CreateAlertDialog(true, true);
                            dlgState = ufin_COLLECTION_DIALOG_STATE_AMT_EXCEED_COLLECTION_LIMIT;
                            dlgAlert.setMessage(MSG_ufin_AMT_EXCEED_COLLECTION_LIMIT);
                            AlertDialog dialog = dlgAlert.create();
                            dialog.show();
                            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                            textView.setTextSize(30);
                            textView.setTextColor(Color.RED);
                        } else {
                            int iExpectedCollectionAmount = selectedCollectionMember.collectionMember.TotalDueAmountsToPayToday;
                            if(selectedCollectionMember.collectionMember.TotalCollectionAmount != iExpectedCollectionAmount) {
                                CreateAlertDialog(true, true);
                                dlgState = ufin_COLLECTION_DIALOG_STATE_EXPECTED_AMT_COLLECTION_MISMATCH;
                                if (selectedCollectionMember.collectionMember.TotalCollectionAmount > iExpectedCollectionAmount) {
                                    dlgAlert.setMessage(MSG_ufin_EXPECTED_AMT_MISMATCH_EXTRA_COLLECTION);
                                    if (selectedCollectionMember.collectionMember.TotalCollectionAmount
                                            >= selectedCollectionMember.collectionMember.PreClosureAmount) {
                                        if (iPaymentType == 1) {
                                            //PrePayment
                                            dlgAlert.setMessage(MSG_ufin_PREPAYMENT_COLLECTION_REMINDER);
                                        } else if (iPaymentType == 2) {
                                            //PreClosure
                                            if (selectedCollectionMember.collectionMember.StandardEMI > 0) {
                                                int iTemp1 = selectedCollectionMember.collectionMember.StandardEMI * 3;
                                                int iTemp2 = selectedCollectionMember.collectionMember.PendingPrincipal / iTemp1;
                                                int iTemp3 = selectedCollectionMember.collectionMember.PendingPrincipal % iTemp1;
                                                if (iTemp3 > 0) {
                                                    iTemp2 = iTemp2 + 1;
                                                }
                                                int iWaitTillNextLoan = iTemp2;
                                                if (iWaitTillNextLoan > 1) {
                                                    String strMsg = MSG_ufin_PRECLOSURE_COLLECTION_REMINDER;
                                                    strMsg = strMsg + "\nPending Principal:" + String.valueOf(selectedCollectionMember.collectionMember.PendingPrincipal);
                                                    strMsg = strMsg + "\nStandard EMI:" + String.valueOf(selectedCollectionMember.collectionMember.StandardEMI);
                                                    strMsg = strMsg + "\nसभासदाला पुढील कर्ज " + String.valueOf(iWaitTillNextLoan) + " महिन्यानंतर देता येईल.";
                                                    dlgAlert.setMessage(strMsg);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    dlgAlert.setMessage(MSG_ufin_EXPECTED_AMT_MISMATCH_LESS_COLLECTION);
////bmp 16-Apr-26
//                                    //Regular EMI ?????????????????????????
//                                    selectedCollectionMember.collectionMember.PrePaymentORPreClosure = 0;
//                                    selectedCollectionMember.collectionMember.PreClosureReasonID = 0;
////bmp 16-Apr-26
                                }
                                AlertDialog dialog = dlgAlert.create();
                                dialog.show();
                                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                                textView.setTextSize(30);
                                textView.setTextColor(Color.RED);
////bmp 16-Apr-26
//                            } else {
//                                //Regular EMI ?????????????????????????
//                                selectedCollectionMember.collectionMember.PrePaymentORPreClosure = 0;
//                                selectedCollectionMember.collectionMember.PreClosureReasonID = 0;
////bmp 16-Apr-26
                            }
                        }
                    } catch (NumberFormatException e) {
                        selectedCollectionMember.collectionMember.TotalCollectionAmount = 0;
                    }
                    InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    if(selectedCollectionMember.collectionMember.TotalCollectionAmount == 0)
                        AllowMemberCollectionSelection(true);
                    else
                        AllowMemberCollectionSelection(false);

                    RefreshMemberCollectionDetails(true);
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    try {
                        if(     (selectedCollectionMember.collectionMember.PrePaymentORPreClosure != 0) &&
                                (selectedCollectionMember.collectionMember.PreClosureRequestStatus != 1) ) {
                            selectedCollectionMember.collectionMember.TotalCollectionAmount = 0;
                            EditText et = (EditText) rootView.findViewById(R.id.editTextTotalCollection);
                            et.setText(String.valueOf(selectedCollectionMember.collectionMember.TotalCollectionAmount));
                            CreateAlertDialog(false, true);
                            dlgState = ufin_COLLECTION_DIALOG_STATE_INVALID_RECOVERY_MODE_SET;
                            dlgAlert.setMessage(MSG_ufin_COLLECTION_INVALID_RECOVERY_MODE_SET);
                            AlertDialog dialog = dlgAlert.create();
                            dialog.show();
                            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                            textView.setTextSize(30);
                            textView.setTextColor(Color.RED);
                            return;
                        }
                        selectedCollectionMember.collectionMember.TotalCollectionAmount = Integer.parseInt(((EditText) view).getText().toString());
                        //Do not allow recovery amount more than 3 EMIs + ADHP + RD
                        int iMaxCollectionAmount = 0;
                        iMaxCollectionAmount = 3 * selectedCollectionMember.collectionMember.TotalDueAmountsToPayRefday;
                        Calendar cal = Calendar.getInstance();
                        Date date = cal.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        String strDate = sdf.format(date);
                        int iPaymentType = 0;
                        if(strDate.equalsIgnoreCase(selectedCollectionMember.collectionMember.PreClosureRequestDate)) {
                            if(selectedCollectionMember.collectionMember.PreClosureRequestStatus == 1) {
                                iMaxCollectionAmount = selectedCollectionMember.collectionMember.PreClosureAmount;
                                if(     (selectedCollectionMember.collectionMember.PrePaymentReqMonths > 0) ||
                                        (selectedCollectionMember.collectionMember.PrePaymentReqAmount > 0)) {
                                    iPaymentType = 1;
                                } else {
                                    iPaymentType = 2;
                                }
                            }
                        }
                        iMaxCollectionAmount = iMaxCollectionAmount + selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt;
                        iMaxCollectionAmount = iMaxCollectionAmount + selectedCollectionMember.collectionMember.RD1ExpectedAmount;
                        iMaxCollectionAmount = iMaxCollectionAmount + selectedCollectionMember.collectionMember.RD2ExpectedAmount;
                        iMaxCollectionAmount = iMaxCollectionAmount + selectedCollectionMember.collectionMember.RD3ExpectedAmount;
                        iMaxCollectionAmount = iMaxCollectionAmount + selectedCollectionMember.collectionMember.RD4ExpectedAmount;
                        iMaxCollectionAmount = iMaxCollectionAmount + selectedCollectionMember.collectionMember.RD5ExpectedAmount;
                        iMaxCollectionAmount = iMaxCollectionAmount + 10000;
                        if(selectedCollectionMember.collectionMember.TotalCollectionAmount > iMaxCollectionAmount) {
                            selectedCollectionMember.collectionMember.TotalCollectionAmount = 0;
                            CreateAlertDialog(true, true);
                            dlgState = ufin_COLLECTION_DIALOG_STATE_AMT_EXCEED_COLLECTION_LIMIT;
                            dlgAlert.setMessage(MSG_ufin_AMT_EXCEED_COLLECTION_LIMIT);
                            AlertDialog dialog = dlgAlert.create();
                            dialog.show();
                            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                            textView.setTextSize(30);
                            textView.setTextColor(Color.RED);
                        } else {
                            int iExpectedCollectionAmount = selectedCollectionMember.collectionMember.TotalDueAmountsToPayToday;
                            if(selectedCollectionMember.collectionMember.TotalCollectionAmount != iExpectedCollectionAmount) {
                                CreateAlertDialog(true, true);
                                dlgState = ufin_COLLECTION_DIALOG_STATE_EXPECTED_AMT_COLLECTION_MISMATCH;
                                if(selectedCollectionMember.collectionMember.TotalCollectionAmount > iExpectedCollectionAmount) {
                                    dlgAlert.setMessage(MSG_ufin_EXPECTED_AMT_MISMATCH_EXTRA_COLLECTION);
                                    if(selectedCollectionMember.collectionMember.TotalCollectionAmount
                                            >= selectedCollectionMember.collectionMember.PreClosureAmount) {
                                        if(iPaymentType == 1) {
                                            //PrePayment
                                            dlgAlert.setMessage(MSG_ufin_PREPAYMENT_COLLECTION_REMINDER);
                                        } else if(iPaymentType == 2) {
                                            //PreClosure
                                            if(selectedCollectionMember.collectionMember.StandardEMI > 0) {
                                                int iTemp1 = selectedCollectionMember.collectionMember.StandardEMI * 3;
                                                int iTemp2 = selectedCollectionMember.collectionMember.PendingPrincipal / iTemp1;
                                                int iTemp3 = selectedCollectionMember.collectionMember.PendingPrincipal % iTemp1;
                                                if (iTemp3 > 0) {
                                                    iTemp2 = iTemp2 + 1;
                                                }
                                                int iWaitTillNextLoan = iTemp2;
                                                if (iWaitTillNextLoan > 1) {
                                                    String strMsg = MSG_ufin_PRECLOSURE_COLLECTION_REMINDER;
                                                    strMsg = strMsg + "\nPending Principal:" + String.valueOf(selectedCollectionMember.collectionMember.PendingPrincipal);
                                                    strMsg = strMsg + "\nStandard EMI:" + String.valueOf(selectedCollectionMember.collectionMember.StandardEMI);
                                                    strMsg = strMsg + "\nसभासदाला पुढील कर्ज " + String.valueOf(iWaitTillNextLoan) + " महिन्यानंतर देता येईल.";
                                                    dlgAlert.setMessage(strMsg);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    dlgAlert.setMessage(MSG_ufin_EXPECTED_AMT_MISMATCH_LESS_COLLECTION);
////bmp 16-Apr-26
//                                    //Regular EMI ?????????????????????????
//                                    selectedCollectionMember.collectionMember.PrePaymentORPreClosure = 0;
//                                    selectedCollectionMember.collectionMember.PreClosureReasonID = 0;
////bmp 16-Apr-26
                                }
                                AlertDialog dialog = dlgAlert.create();
                                dialog.show();
                                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                                textView.setTextSize(30);
                                textView.setTextColor(Color.RED);
////bmp 16-Apr-26
//                            } else {
//                                //Regular EMI ?????????????????????????
//                                selectedCollectionMember.collectionMember.PrePaymentORPreClosure = 0;
//                                selectedCollectionMember.collectionMember.PreClosureReasonID = 0;
////bmp 16-Apr-26
                            }
                        }
                    } catch (NumberFormatException e) {
                        selectedCollectionMember.collectionMember.TotalCollectionAmount = 0;
                    }
                    if(selectedCollectionMember.collectionMember.TotalCollectionAmount == 0)
                        AllowMemberCollectionSelection(true);
                    else
                        AllowMemberCollectionSelection(false);

                    RefreshMemberCollectionDetails(true);
                }
            }
        });

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount == 0)
            AllowMemberCollectionSelection(true);
        else
            AllowMemberCollectionSelection(false);

        et = (EditText) rootView.findViewById(R.id.editTextExtraADHP);
        if(selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount > 0)
            et.setText(String.valueOf(selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount));
        else
            et.setText("");
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = Integer.parseInt(v.getText().toString());
                    } catch (NumberFormatException e) {
                        selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
                    }
                    InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    RefreshMemberCollectionDetails(true);
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    try {
                        selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
                    }
                    RefreshMemberCollectionDetails(true);
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextNewRD);
        if(selectedCollectionMember.collectionMember.NewRDCollectionAmount > 0)
            et.setText(String.valueOf(selectedCollectionMember.collectionMember.NewRDCollectionAmount));
        else
            et.setText("");
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        selectedCollectionMember.collectionMember.NewRDCollectionAmount = Integer.parseInt(v.getText().toString());
                    } catch (NumberFormatException e) {
                        selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
                    }
                    InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    RefreshMemberCollectionDetails(true);
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    try {
                        selectedCollectionMember.collectionMember.NewRDCollectionAmount = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
                    }
                    RefreshMemberCollectionDetails(true);
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextFDCollection);
        if(selectedCollectionMember.collectionMember.FDCollectionAmount > 0)
            et.setText(String.valueOf(selectedCollectionMember.collectionMember.FDCollectionAmount));
        else
            et.setText("");
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        selectedCollectionMember.collectionMember.FDCollectionAmount = Integer.parseInt(v.getText().toString());
                    } catch (NumberFormatException e) {
                        selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
                    }
                    InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    RefreshMemberCollectionDetails(true);
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    try {
                        selectedCollectionMember.collectionMember.FDCollectionAmount = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
                    }
                    RefreshMemberCollectionDetails(true);
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextExtraJLG);
        if(selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount > 0)
            et.setText(String.valueOf(selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount));
        else
            et.setText("");
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = Integer.parseInt(v.getText().toString());
                    } catch (NumberFormatException e) {
                        selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
                    }
                    InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    RefreshMemberCollectionDetails(true);
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    try {
                        selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
                    }
                    RefreshMemberCollectionDetails(true);
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextReprintCharges);
        if(selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount > 0)
            et.setText(String.valueOf(selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount));
        else
            et.setText("");
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = Integer.parseInt(v.getText().toString());
                    } catch (NumberFormatException e) {
                        selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
                    }
                    InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    RefreshMemberCollectionDetails(true);
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    try {
                        selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
                    }
                    RefreshMemberCollectionDetails(true);
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD1);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCollectionMember.collectionMember.RD1Select = ((CheckBox) v).isChecked();
                if(selectedCollectionMember.collectionMember.RD1Select) {
                    EditText et = (EditText) rootView.findViewById(R.id.editTextRDCollection1);
                    if(selectedCollectionMember.collectionMember.RD1CollectionAmount > 0)
                        et.setText(String.valueOf(selectedCollectionMember.collectionMember.RD1CollectionAmount));
                    else
                        et.setText("");
//bmp 07-Aug-26
                    if(selectedCollectionMember.collectionMember.Internal_Transfer_Reason.equalsIgnoreCase("FLMFType1")) {
                        SetFLMFCollectionTargets();
                    } else {
                        SetCollectionTargets();
                    }
//bmp 07-Aug-26
                } else {
                    selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
                    EditText et = (EditText) rootView.findViewById(R.id.editTextRDCollection1);
                    et.setText("");
                    RefreshMemberCollectionDetails(true);
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD2);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCollectionMember.collectionMember.RD2Select = ((CheckBox) v).isChecked();
                if(selectedCollectionMember.collectionMember.RD2Select) {
                    EditText et = (EditText) rootView.findViewById(R.id.editTextRDCollection2);
                    if(selectedCollectionMember.collectionMember.RD2CollectionAmount > 0)
                        et.setText(String.valueOf(selectedCollectionMember.collectionMember.RD2CollectionAmount));
                    else
                        et.setText("");
//bmp 07-Aug-26
                    if(selectedCollectionMember.collectionMember.Internal_Transfer_Reason.equalsIgnoreCase("FLMFType1")) {
                        SetFLMFCollectionTargets();
                    } else {
                        SetCollectionTargets();
                    }
//bmp 07-Aug-26
                } else {
                    selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
                    EditText et = (EditText) rootView.findViewById(R.id.editTextRDCollection2);
                    et.setText("");
                    RefreshMemberCollectionDetails(true);
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD3);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCollectionMember.collectionMember.RD3Select = ((CheckBox) v).isChecked();
                if(selectedCollectionMember.collectionMember.RD3Select) {
                    EditText et = (EditText) rootView.findViewById(R.id.editTextRDCollection3);
                    if(selectedCollectionMember.collectionMember.RD3CollectionAmount > 0)
                        et.setText(String.valueOf(selectedCollectionMember.collectionMember.RD3CollectionAmount));
                    else
                        et.setText("");
//bmp 07-Aug-26
                    if(selectedCollectionMember.collectionMember.Internal_Transfer_Reason.equalsIgnoreCase("FLMFType1")) {
                        SetFLMFCollectionTargets();
                    } else {
                        SetCollectionTargets();
                    }
//bmp 07-Aug-26
                } else {
                    selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
                    EditText et = (EditText) rootView.findViewById(R.id.editTextRDCollection3);
                    et.setText("");
                    RefreshMemberCollectionDetails(true);
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD4);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCollectionMember.collectionMember.RD4Select = ((CheckBox) v).isChecked();
                if(selectedCollectionMember.collectionMember.RD4Select) {
                    EditText et = (EditText) rootView.findViewById(R.id.editTextRDCollection4);
                    if(selectedCollectionMember.collectionMember.RD4CollectionAmount > 0)
                        et.setText(String.valueOf(selectedCollectionMember.collectionMember.RD4CollectionAmount));
                    else
                        et.setText("");
//bmp 07-Aug-26
                    if(selectedCollectionMember.collectionMember.Internal_Transfer_Reason.equalsIgnoreCase("FLMFType1")) {
                        SetFLMFCollectionTargets();
                    } else {
                        SetCollectionTargets();
                    }
//bmp 07-Aug-26
                } else {
                    selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
                    EditText et = (EditText) rootView.findViewById(R.id.editTextRDCollection4);
                    et.setText("");
                    RefreshMemberCollectionDetails(true);
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD5);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCollectionMember.collectionMember.RD5Select = ((CheckBox) v).isChecked();
                if(selectedCollectionMember.collectionMember.RD5Select) {
                    EditText et = (EditText) rootView.findViewById(R.id.editTextRDCollection5);
                    if(selectedCollectionMember.collectionMember.RD5CollectionAmount > 0)
                        et.setText(String.valueOf(selectedCollectionMember.collectionMember.RD5CollectionAmount));
                    else
                        et.setText("");
//bmp 07-Aug-26
                    if(selectedCollectionMember.collectionMember.Internal_Transfer_Reason.equalsIgnoreCase("FLMFType1")) {
                        SetFLMFCollectionTargets();
                    } else {
                        SetCollectionTargets();
                    }
//bmp 07-Aug-26
                } else {
                    selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
                    EditText et = (EditText) rootView.findViewById(R.id.editTextRDCollection5);
                    et.setText("");
                    RefreshMemberCollectionDetails(true);
                }
            }
        });
//bmp 05-Aug-26
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectJLG);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCollectionMember.collectionMember.JLGSelect = ((CheckBox) v).isChecked();
                if(selectedCollectionMember.collectionMember.JLGSelect) {
                    EditText et = (EditText) rootView.findViewById(R.id.editTextJLGCollection);
                    if(selectedCollectionMember.collectionMember.JLGCollectionAmount > 0)
                        et.setText(String.valueOf(selectedCollectionMember.collectionMember.JLGCollectionAmount));
                    else
                        et.setText("");
//bmp 07-Aug-26
                    SetFLMFCollectionTargets();
//bmp 07-Aug-26
                } else {
                    selectedCollectionMember.collectionMember.JLGCollectionAmount = 0;
                    EditText et = (EditText) rootView.findViewById(R.id.editTextJLGCollection);
                    et.setText("");
                    RefreshMemberCollectionDetails(true);
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectNidhi);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCollectionMember.collectionMember.NidhiSelect = ((CheckBox) v).isChecked();
                if(selectedCollectionMember.collectionMember.NidhiSelect) {
                    EditText et = (EditText) rootView.findViewById(R.id.editTextNidhiCollection);
                    if(selectedCollectionMember.collectionMember.NidhiCollectionAmount > 0)
                        et.setText(String.valueOf(selectedCollectionMember.collectionMember.NidhiCollectionAmount));
                    else
                        et.setText("");
//bmp 07-Aug-26
                    SetFLMFCollectionTargets();
//bmp 07-Aug-26
                } else {
                    selectedCollectionMember.collectionMember.NidhiCollectionAmount = 0;
                    EditText et = (EditText) rootView.findViewById(R.id.editTextNidhiCollection);
                    et.setText("");
                    RefreshMemberCollectionDetails(true);
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectADHP);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCollectionMember.collectionMember.ADHPSelect = ((CheckBox) v).isChecked();
                if(selectedCollectionMember.collectionMember.ADHPSelect) {
                    EditText et = (EditText) rootView.findViewById(R.id.editTextADHPCollection);
                    if(selectedCollectionMember.collectionMember.TotalADHPCollectionAmount > 0)
                        et.setText(String.valueOf(selectedCollectionMember.collectionMember.TotalADHPCollectionAmount));
                    else
                        et.setText("");
//bmp 07-Aug-26
                    SetFLMFCollectionTargets();
//bmp 07-Aug-26
                } else {
                    selectedCollectionMember.collectionMember.TotalADHPCollectionAmount = 0;
                    EditText et = (EditText) rootView.findViewById(R.id.editTextADHPCollection);
                    et.setText("");
                    RefreshMemberCollectionDetails(true);
                }
            }
        });
//bmp 05-Aug-26
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectAdditionalRepay);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(strSelectedPaymentOption.equalsIgnoreCase("Monthly")) {
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectAdditionalRepay);
                    cb.setEnabled(false);
                    cb.setChecked(false);
                    EditText et = (EditText) rootView.findViewById(R.id.editTextAdditionalRepay);
                    et.setEnabled(false);
                    et.setText("");
                    return;
                }
                selectedCollectionMember.collectionMember.AdditionalRepaySelect = ((CheckBox) v).isChecked();
                if(selectedCollectionMember.collectionMember.AdditionalRepaySelect) {
                    EditText et = (EditText) rootView.findViewById(R.id.editTextAdditionalRepay);
                    if(selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount > 0)
                        et.setText(String.valueOf(selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount));
                    else
                        et.setText("");
                    et.setEnabled(true);
                    et.requestFocus();
                } else {
                    selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
                    EditText et = (EditText) rootView.findViewById(R.id.editTextAdditionalRepay);
                    et.setText("");
                    et.setEnabled(false);
                    RefreshMemberCollectionDetails(true);
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextAdditionalRepay);
        if(selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount > 0)
            et.setText(String.valueOf(selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount));
        else
            et.setText("");
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(strSelectedPaymentOption.equalsIgnoreCase("Monthly")) {
                        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectAdditionalRepay);
                        cb.setEnabled(false);
                        cb.setChecked(false);
                        EditText et = (EditText) rootView.findViewById(R.id.editTextAdditionalRepay);
                        et.setEnabled(false);
                        et.setText("");
                        return false;
                    }
                    try {
                        selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = Integer.parseInt(v.getText().toString());
                    } catch (NumberFormatException e) {
                        selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
                    }
                    RefreshMemberCollectionDetails(false);

                    InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    if(strSelectedPaymentOption.equalsIgnoreCase("Monthly")) {
                        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectAdditionalRepay);
                        cb.setEnabled(false);
                        cb.setChecked(false);
                        EditText et = (EditText) rootView.findViewById(R.id.editTextAdditionalRepay);
                        et.setEnabled(false);
                        et.setText("");
                        return;
                    }
                    try {
                        selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
                    }
                    RefreshMemberCollectionDetails(false);
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectExtraADHP);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCollectionMember.collectionMember.ExtraADHPSelect = ((CheckBox) v).isChecked();
                if(selectedCollectionMember.collectionMember.ExtraADHPSelect) {
                    EditText et = (EditText) rootView.findViewById(R.id.editTextExtraADHP);
                    if(selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount > 0)
                        et.setText(String.valueOf(selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount));
                    else
                        et.setText("");
                    et.setEnabled(true);
                } else {
                    selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
                    EditText et = (EditText) rootView.findViewById(R.id.editTextExtraADHP);
                    et.setText("");
                    et.setEnabled(false);
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectNewRD);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCollectionMember.collectionMember.NewRDSelect = ((CheckBox) v).isChecked();
                if(selectedCollectionMember.collectionMember.NewRDSelect) {
                    EditText et = (EditText) rootView.findViewById(R.id.editTextNewRD);
                    if(selectedCollectionMember.collectionMember.NewRDCollectionAmount > 0)
                        et.setText(String.valueOf(selectedCollectionMember.collectionMember.NewRDCollectionAmount));
                    else
                        et.setText("");
                    et.setEnabled(true);
                } else {
                    selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
                    EditText et = (EditText) rootView.findViewById(R.id.editTextNewRD);
                    et.setText("");
                    et.setEnabled(false);
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectFDCollection);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCollectionMember.collectionMember.FDSelect = ((CheckBox) v).isChecked();
                if(selectedCollectionMember.collectionMember.FDSelect) {
                    EditText et = (EditText) rootView.findViewById(R.id.editTextFDCollection);
                    if(selectedCollectionMember.collectionMember.FDCollectionAmount > 0)
                        et.setText(String.valueOf(selectedCollectionMember.collectionMember.FDCollectionAmount));
                    else
                        et.setText("");
                    et.setEnabled(true);
                } else {
                    selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
                    EditText et = (EditText) rootView.findViewById(R.id.editTextFDCollection);
                    et.setText("");
                    et.setEnabled(false);
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectExtraJLG);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCollectionMember.collectionMember.ExtraJLGSelect = ((CheckBox) v).isChecked();
                if(selectedCollectionMember.collectionMember.ExtraJLGSelect) {
                    EditText et = (EditText) rootView.findViewById(R.id.editTextExtraJLG);
                    if(selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount > 0)
                        et.setText(String.valueOf(selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount));
                    else
                        et.setText("");
                    et.setEnabled(true);
                } else {
                    selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
                    EditText et = (EditText) rootView.findViewById(R.id.editTextExtraJLG);
                    et.setText("");
                    et.setEnabled(false);
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectReprintCharges);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCollectionMember.collectionMember.ReprintChargesSelect = ((CheckBox) v).isChecked();
                if(selectedCollectionMember.collectionMember.ReprintChargesSelect) {
                    EditText et = (EditText) rootView.findViewById(R.id.editTextReprintCharges);
                    if(selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount > 0)
                        et.setText(String.valueOf(selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount));
                    else
                        et.setText("");
                    et.setEnabled(true);
                } else {
                    selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
                    EditText et = (EditText) rootView.findViewById(R.id.editTextReprintCharges);
                    et.setText("");
                    et.setEnabled(false);
                }
            }
        });
//bmp 05-Aug-26
        if(selectedCollectionMember.collectionMember.Internal_Transfer_Reason.equalsIgnoreCase("FLMFType1")) {
            SetFLMFCollectionTargets();
        } else {
            SetCollectionTargets();
        }
//bmp 05-Aug-26
    }
    void SetCollectionTargets() {
        iCollectionTarget1 = selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtToday;
        iCollectionTarget2 = iCollectionTarget1 + selectedCollectionMember.collectionMember.ADHPContributionAmount;
        if (selectedCollectionMember.collectionMember.RD1Select)
            iCollectionTarget3 = iCollectionTarget2 + selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
        else
            iCollectionTarget3 = iCollectionTarget2;
        if (selectedCollectionMember.collectionMember.RD2Select)
            iCollectionTarget4 = iCollectionTarget3 + selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
        else
            iCollectionTarget4 = iCollectionTarget3;
        if (selectedCollectionMember.collectionMember.RD3Select)
            iCollectionTarget5 = iCollectionTarget4 + selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
        else
            iCollectionTarget5 = iCollectionTarget4;
        if (selectedCollectionMember.collectionMember.RD4Select)
            iCollectionTarget6 = iCollectionTarget5 + selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
        else
            iCollectionTarget6 = iCollectionTarget5;
        if (selectedCollectionMember.collectionMember.RD5Select)
            iCollectionTarget7 = iCollectionTarget6 + selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount;
        else
            iCollectionTarget7 = iCollectionTarget6;

        iCollectionTarget15 = iCollectionTarget7 + selectedCollectionMember.collectionMember.ADHPPastDueAmountTillToday;

        if (selectedCollectionMember.collectionMember.RD1Select)
            iCollectionTarget16 = iCollectionTarget15 + selectedCollectionMember.collectionMember.RD1PastDue;
        else
            iCollectionTarget16 = iCollectionTarget15;
        if (selectedCollectionMember.collectionMember.RD2Select)
            iCollectionTarget17 = iCollectionTarget16 + selectedCollectionMember.collectionMember.RD2PastDue;
        else
            iCollectionTarget17 = iCollectionTarget16;
        if (selectedCollectionMember.collectionMember.RD3Select)
            iCollectionTarget18 = iCollectionTarget17 + selectedCollectionMember.collectionMember.RD3PastDue;
        else
            iCollectionTarget18 = iCollectionTarget17;
        if (selectedCollectionMember.collectionMember.RD4Select)
            iCollectionTarget19 = iCollectionTarget18 + selectedCollectionMember.collectionMember.RD4PastDue;
        else
            iCollectionTarget19 = iCollectionTarget18;
        if (selectedCollectionMember.collectionMember.RD5Select)
            iCollectionTarget20 = iCollectionTarget19 + selectedCollectionMember.collectionMember.RD5PastDue;
        else
            iCollectionTarget20 = iCollectionTarget19;
        if (selectedCollectionMember.collectionMember.AdditionalRepaySelect)
            iCollectionTarget28 = iCollectionTarget20 + selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount;
        else
            iCollectionTarget28 = iCollectionTarget20;
    }
    void SetFLMFCollectionTargets() {
        if(selectedCollectionMember.collectionMember.JLGSelect) {
            iCollectionTarget0 = selectedCollectionMember.collectionMember.PastDue_SD
                    + selectedCollectionMember.collectionMember.Expected_SD;
        } else {
            iCollectionTarget0 = 0;
        }
        if(selectedCollectionMember.collectionMember.NidhiSelect) {
            iCollectionTarget1 = iCollectionTarget0
                    + selectedCollectionMember.collectionMember.PastDue_PolicyRenewalAmt
                    + selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt;
        } else {
            iCollectionTarget1 = iCollectionTarget0;
        }
        if(selectedCollectionMember.collectionMember.ADHPSelect) {
            iCollectionTarget2 = iCollectionTarget1 + selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt;
        } else {
            iCollectionTarget2 = iCollectionTarget1;
        }
        if(selectedCollectionMember.collectionMember.RD1Select)
            iCollectionTarget3 = iCollectionTarget2 + selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
        else
            iCollectionTarget3 = iCollectionTarget2;
        if(selectedCollectionMember.collectionMember.RD2Select)
            iCollectionTarget4 = iCollectionTarget3 + selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
        else
            iCollectionTarget4 = iCollectionTarget3;
        if(selectedCollectionMember.collectionMember.RD3Select)
            iCollectionTarget5 = iCollectionTarget4 + selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
        else
            iCollectionTarget5 = iCollectionTarget4;
        if(selectedCollectionMember.collectionMember.RD4Select)
            iCollectionTarget6 = iCollectionTarget5 + selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
        else
            iCollectionTarget6 = iCollectionTarget5;
        if(selectedCollectionMember.collectionMember.RD5Select)
            iCollectionTarget7 = iCollectionTarget6 + selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount;
        else
            iCollectionTarget7 = iCollectionTarget6;

//            iCollectionTarget15 = iCollectionTarget7 + selectedCollectionMember.collectionMember.ADHPPastDueAmountTillToday;
        iCollectionTarget15 = iCollectionTarget7;

        if(selectedCollectionMember.collectionMember.RD1Select)
            iCollectionTarget16 = iCollectionTarget15 + selectedCollectionMember.collectionMember.RD1PastDue;
        else
            iCollectionTarget16 = iCollectionTarget15;
        if(selectedCollectionMember.collectionMember.RD2Select)
            iCollectionTarget17 = iCollectionTarget16 + selectedCollectionMember.collectionMember.RD2PastDue;
        else
            iCollectionTarget17 = iCollectionTarget16;
        if(selectedCollectionMember.collectionMember.RD3Select)
            iCollectionTarget18 = iCollectionTarget17 + selectedCollectionMember.collectionMember.RD3PastDue;
        else
            iCollectionTarget18 = iCollectionTarget17;
        if(selectedCollectionMember.collectionMember.RD4Select)
            iCollectionTarget19 = iCollectionTarget18 + selectedCollectionMember.collectionMember.RD4PastDue;
        else
            iCollectionTarget19 = iCollectionTarget18;
        if(selectedCollectionMember.collectionMember.RD5Select)
            iCollectionTarget20 = iCollectionTarget19 + selectedCollectionMember.collectionMember.RD5PastDue;
        else
            iCollectionTarget20 = iCollectionTarget19;
        if(selectedCollectionMember.collectionMember.AdditionalRepaySelect)
            iCollectionTarget28 = iCollectionTarget20 + selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount;
        else
            iCollectionTarget28 = iCollectionTarget20;
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
                    selectedCollectionCenter.NextRecoveryDate = sdf2.format(myCalendar.getTime());
                    selectedCollectionCenter.NextRecoveryDateTime = selectedCollectionCenter.NextRecoveryDate + " " + selectedCollectionCenter.NextRecoveryTime;
                    if(!selectedCollectionCenter.NextRecoveryTime.isEmpty()) {
                        if (!ValidateNextRecoveryDateTime()) {
                            selectedCollectionCenter.NextRecoveryDate = "";
                            selectedCollectionCenter.NextRecoveryDateTime = selectedCollectionCenter.NextRecoveryDate + " " + selectedCollectionCenter.NextRecoveryTime;
                            strDate = "";
                        }
                    }
                    RefreshCollection();
                    break;
            }
        }
    };
    final TimePickerDialog.OnTimeSetListener timepick = new TimePickerDialog.OnTimeSetListener() {
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
                    selectedCollectionCenter.NextRecoveryTime = sdf2.format(myCalendar.getTime());
                    selectedCollectionCenter.NextRecoveryDateTime = selectedCollectionCenter.NextRecoveryDate + " " + selectedCollectionCenter.NextRecoveryTime;
                    if(!selectedCollectionCenter.NextRecoveryDate.isEmpty()) {
                        if (!ValidateNextRecoveryDateTime()) {
                            selectedCollectionCenter.NextRecoveryTime = "";
                            selectedCollectionCenter.NextRecoveryDateTime = selectedCollectionCenter.NextRecoveryDate + " " + selectedCollectionCenter.NextRecoveryTime;
                            strTime = "";
                        }
                    }
                    RefreshCollection();
                    break;
            }
        }
    };
    boolean ValidateCenterMemberCollection() {
        boolean bValid = true;

        for(int i=1; i<CenterMemberList.size(); i++) {
            if(     (CenterMemberList.get(i).collectionMember.MemberStatus != clsCollectionMember.COLLECTION_MEMBER_STATUS_FULL) &&
                    (CenterMemberList.get(i).collectionMember.TotalCollectionAmount == 0) &&
                    (mainActivity.CollectionMemberList.get(CenterMemberList.get(i).indexCollectionMemberList).ConfirmUnCollected == 0) ) {
                mainActivity.CollectionMemberList.get(CenterMemberList.get(i).indexCollectionMemberList).ConfirmUnCollected = 1;
                bValid = false;
                CreateAlertDialog(true, false);
                dlgState = ufin_COLLECTION_DIALOG_STATE_CONFIRM_UNCOLLECTED;
                String strMsg = CenterMemberList.get(i).collectionMember.MemberName + ":" + MSG_ufin_COLLECTION_CONFIRM_UNCOLLECTED;
                dlgAlert.setMessage(strMsg);
                AlertDialog dialog = dlgAlert.create();
                dialog.show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(30);
                textView.setTextColor(Color.RED);

                return bValid;
            }
        }
        return bValid;
    }
    boolean ValidateCenterMemberPhoneNo() {
        boolean bValid = true;

        for(int i=1; i<CenterMemberList.size(); i++) {
            if(     (CenterMemberList.get(i).collectionMember.TotalCollectionAmount > 0) &&
                    (!mainActivity.CollectionMemberList.get(CenterMemberList.get(i).indexCollectionMemberList).VerifiedPhoneNo) ) {
                bValid = false;
                CreateAlertDialog(true, false);
                dlgState = ufin_COLLECTION_DIALOG_STATE_MOBILE_NO_NOT_VERIFIED;
                String strMsg = CenterMemberList.get(i).collectionMember.MemberName + ":" + MSG_ufin_COLLECTION_MOBILE_NO_NOT_VERIFIED;
                dlgAlert.setMessage(strMsg);
                AlertDialog dialog = dlgAlert.create();
                dialog.show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(30);
                textView.setTextColor(Color.RED);

                return bValid;
            }
        }
        return bValid;
    }
    boolean ValidateNextRecoveryDateTime() {
        boolean bValid = true;
        EditText et = (EditText) rootView.findViewById(R.id.editTextNextRecoveryDate);
        if(!et.isEnabled())
            return true;

        if(selectedCollectionCenter.NextRecoveryDate.isEmpty() || selectedCollectionCenter.NextRecoveryTime.isEmpty() ) {
            bValid = false;
            CreateAlertDialog(true, false);
            dlgState = ufin_COLLECTION_DIALOG_STATE_INCORRECT_NEXT_SCHEDULE;
            dlgAlert.setMessage(MSG_ufin_COLLECTION_INCORRECT_NEXT_SCHEDULE);
            AlertDialog dialog = dlgAlert.create();
            dialog.show();
            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
            textView.setTextSize(30);
            textView.setTextColor(Color.RED);

            return bValid;
        }
        SimpleDateFormat sdftimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
        try {
            Date dateNextRecoverytimestamp = sdftimestamp.parse(selectedCollectionCenter.NextRecoveryDateTime);
            Calendar doeNextRecovery = Calendar.getInstance();
            doeNextRecovery.setTime(dateNextRecoverytimestamp);
            Calendar calNow = Calendar.getInstance();
            int iNowYear = calNow.get(Calendar.YEAR);
            int iNextMonthYear = iNowYear;
            int iNowMonth = calNow.get(Calendar.MONTH);
            int iNextMonth = iNowMonth + 1;
            if(iNextMonth == 12) {
                iNextMonth = 0;
                iNextMonthYear = iNextMonthYear + 1;
            }
            int iNextRecoveryYear = doeNextRecovery.get(Calendar.YEAR);
            if(iNextRecoveryYear != iNextMonthYear) {
                bValid = false;
                CreateAlertDialog(true, false);
                dlgState = ufin_COLLECTION_DIALOG_STATE_INCORRECT_NEXT_SCHEDULE;
                dlgAlert.setMessage(MSG_ufin_COLLECTION_INCORRECT_NEXT_SCHEDULE);
                AlertDialog dialog = dlgAlert.create();
                dialog.show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(30);
                textView.setTextColor(Color.RED);

                return bValid;
            }
            int iNextRecoveryMonth = doeNextRecovery.get(Calendar.MONTH);
            if(iNextRecoveryMonth != iNextMonth) {
                bValid = false;
                CreateAlertDialog(true, false);
                dlgState = ufin_COLLECTION_DIALOG_STATE_INCORRECT_NEXT_SCHEDULE;
                dlgAlert.setMessage(MSG_ufin_COLLECTION_INCORRECT_NEXT_SCHEDULE);
                AlertDialog dialog = dlgAlert.create();
                dialog.show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(30);
                textView.setTextColor(Color.RED);

                return bValid;
            }
            int iStartMinutes = doeNextRecovery.get(Calendar.MINUTE);
            if((iStartMinutes % 10) > 0) {
                bValid = false;
                CreateAlertDialog(true, false);
                dlgState = ufin_COLLECTION_DIALOG_STATE_INCORRECT_NEXT_SCHEDULE;
                dlgAlert.setMessage(MSG_ufin_COLLECTION_INCORRECT_NEXT_SCHEDULE_STARTTIME_MINUTES);
                AlertDialog dialog = dlgAlert.create();
                dialog.show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(30);
                textView.setTextColor(Color.RED);

                return bValid;
            }
            int iStartHours = doeNextRecovery.get(Calendar.HOUR_OF_DAY);
            if( (iStartHours < 11) || (iStartHours > 18) || ((iStartHours == 18) && (iStartMinutes > 0)) ) {
                bValid = false;
                CreateAlertDialog(true, false);
                dlgState = ufin_COLLECTION_DIALOG_STATE_INCORRECT_NEXT_SCHEDULE;
                dlgAlert.setMessage(MSG_ufin_COLLECTION_INCORRECT_NEXT_SCHEDULE_STARTTIME);
                AlertDialog dialog = dlgAlert.create();
                dialog.show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(30);
                textView.setTextColor(Color.RED);

                return bValid;
            }

            long lNextRecoveryStartTime = doeNextRecovery.getTimeInMillis();
            int iPerMemberRecoveryMinutes = 2;
            int iCenterCollectionDurationMinutes = selectedCollectionCenter.ScheduledCollectionMemberCount * iPerMemberRecoveryMinutes;
            //per center: Minimum 10 minutes and multiple of 10 minutes
            if((iCenterCollectionDurationMinutes % 10) > 0)
                iCenterCollectionDurationMinutes = iCenterCollectionDurationMinutes + 10;
            iCenterCollectionDurationMinutes = iCenterCollectionDurationMinutes / 10;
            iCenterCollectionDurationMinutes = iCenterCollectionDurationMinutes * 10;
            long lCenterCollectionDurationMSec = (long) iCenterCollectionDurationMinutes * 60L * 1000L;
            long lNextRecoveryEndTime = lNextRecoveryStartTime + lCenterCollectionDurationMSec;

            String strType = "Recovery";
            String strDetails = "C-" + String.valueOf(selectedCollectionCenter.CenterID);
            if(mainActivity.searchScheduleTime(strType, strDetails, lNextRecoveryStartTime, lNextRecoveryEndTime)) {
                bValid = false;
                CreateAlertDialog(true, false);
                dlgState = ufin_COLLECTION_DIALOG_STATE_INCORRECT_NEXT_SCHEDULE;
                dlgAlert.setMessage(MSG_ufin_COLLECTION_INCORRECT_NEXT_SCHEDULE);
                AlertDialog dialog = dlgAlert.create();
                dialog.show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(30);
                textView.setTextColor(Color.RED);

                return bValid;
            } else {
                selectedCollectionCenter.lNextRecoveryStartTime = lNextRecoveryStartTime;
                selectedCollectionCenter.lNextRecoveryEndTime = lNextRecoveryEndTime;
            }
        } catch (ParseException e) {
            bValid = false;
            throw new RuntimeException(e);
        }

        return bValid;
    }
    void RefreshStartCollection() {
        iStartValue500Notes = 500 * iStartCount500Notes;
        iStartValue200Notes = 200 * iStartCount200Notes;
        iStartValue100Notes = 100 * iStartCount100Notes;
        iStartValue50Notes = 50 * iStartCount50Notes;
        iStartValue20Notes = 20 * iStartCount20Notes;
        iStartValue10Notes = 10 * iStartCount10Notes;
        iStartValue5Notes = 5 * iStartCount5Notes;
        iStartValue2Notes = 2 * iStartCount2Notes;
        iStartValue1Notes = 1 * iStartCount1Notes;

        iStartValueCash = iStartValue500Notes
                + iStartValue200Notes
                + iStartValue100Notes
                + iStartValue50Notes
                + iStartValue20Notes
                + iStartValue10Notes
                + iStartValue5Notes
                + iStartValue2Notes
                + iStartValue1Notes;

        iStartValueTotal = iStartValueCash + iStartValueOnline;

        TextView tv = (TextView) rootView.findViewById(R.id.textViewStartCount500);
        tv.setText(String.valueOf(iStartCount500Notes));
        tv = (TextView) rootView.findViewById(R.id.textViewStartCount200);
        tv.setText(String.valueOf(iStartCount200Notes));
        tv = (TextView) rootView.findViewById(R.id.textViewStartCount100);
        tv.setText(String.valueOf(iStartCount100Notes));
        tv = (TextView) rootView.findViewById(R.id.textViewStartCount50);
        tv.setText(String.valueOf(iStartCount50Notes));
        tv = (TextView) rootView.findViewById(R.id.textViewStartCount20);
        tv.setText(String.valueOf(iStartCount20Notes));
        tv = (TextView) rootView.findViewById(R.id.textViewStartCount10);
        tv.setText(String.valueOf(iStartCount10Notes));
        tv = (TextView) rootView.findViewById(R.id.textViewStartCount5);
        tv.setText(String.valueOf(iStartCount5Notes));
        tv = (TextView) rootView.findViewById(R.id.textViewStartCount2);
        tv.setText(String.valueOf(iStartCount2Notes));
        tv = (TextView) rootView.findViewById(R.id.textViewStartCount1);
        tv.setText(String.valueOf(iStartCount1Notes));
    }
    void RefreshCollection() {
        iCenterValue500Notes = 500 * iCenterCount500Notes;
        iCenterValue200Notes = 200 * iCenterCount200Notes;
        iCenterValue100Notes = 100 * iCenterCount100Notes;
        iCenterValue50Notes = 50 * iCenterCount50Notes;
        iCenterValue20Notes = 20 * iCenterCount20Notes;
        iCenterValue10Notes = 10 * iCenterCount10Notes;
        iCenterValue5Notes = 5 * iCenterCount5Notes;
        iCenterValue2Notes = 2 * iCenterCount2Notes;
        iCenterValue1Notes = 1 * iCenterCount1Notes;

        iCenterValueCash = iCenterValue500Notes
                + iCenterValue200Notes
                + iCenterValue100Notes
                + iCenterValue50Notes
                + iCenterValue20Notes
                + iCenterValue10Notes
                + iCenterValue5Notes
                + iCenterValue2Notes
                + iCenterValue1Notes;

        iTotalCount500Notes = iStartCount500Notes + iCenterCount500Notes;
        iTotalCount200Notes = iStartCount200Notes + iCenterCount200Notes;
        iTotalCount100Notes = iStartCount100Notes + iCenterCount100Notes;
        iTotalCount50Notes = iStartCount50Notes + iCenterCount50Notes;
        iTotalCount20Notes = iStartCount20Notes + iCenterCount20Notes;
        iTotalCount10Notes = iStartCount10Notes + iCenterCount10Notes;
        iTotalCount5Notes = iStartCount5Notes + iCenterCount5Notes;
        iTotalCount2Notes = iStartCount2Notes + iCenterCount2Notes;
        iTotalCount1Notes = iStartCount1Notes + iCenterCount1Notes;

        TextView tv = (TextView) rootView.findViewById(R.id.textViewTotalCount500);
        tv.setText(String.valueOf(iTotalCount500Notes));
        tv = (TextView) rootView.findViewById(R.id.textViewTotalCount200);
        tv.setText(String.valueOf(iTotalCount200Notes));
        tv = (TextView) rootView.findViewById(R.id.textViewTotalCount100);
        tv.setText(String.valueOf(iTotalCount100Notes));
        tv = (TextView) rootView.findViewById(R.id.textViewTotalCount50);
        tv.setText(String.valueOf(iTotalCount50Notes));
        tv = (TextView) rootView.findViewById(R.id.textViewTotalCount20);
        tv.setText(String.valueOf(iTotalCount20Notes));
        tv = (TextView) rootView.findViewById(R.id.textViewTotalCount10);
        tv.setText(String.valueOf(iTotalCount10Notes));
        tv = (TextView) rootView.findViewById(R.id.textViewTotalCount5);
        tv.setText(String.valueOf(iTotalCount5Notes));
        tv = (TextView) rootView.findViewById(R.id.textViewTotalCount2);
        tv.setText(String.valueOf(iTotalCount2Notes));
        tv = (TextView) rootView.findViewById(R.id.textViewTotalCount1);
        tv.setText(String.valueOf(iTotalCount1Notes));
//bmp 29-May-26
        tv = (TextView) rootView.findViewById(R.id.textViewCenterNarrationTotal);
        tv.setText(String.valueOf(iCenterValueCash));
//bmp 29-May-26
        int iCenterValueOnline = 0;
        for(int i=1; i<CenterMemberList.size(); i++) {
            if(CenterMemberList.get(i).collectionMember.strPaymentMode.equalsIgnoreCase("Online")) {
                iCenterValueOnline = iCenterValueOnline + CenterMemberList.get(i).collectionMember.TotalCollectionAmount;
            }
        }

        iCenterValueTotal = iCenterValueCash + iCenterValueOnline;
        iTotalValueCash = iStartValueCash + iCenterValueCash;
        iTotalValueOnline = iStartValueOnline + iCenterValueOnline;
        iTotalValue = iStartValueTotal + iCenterValueTotal;

        tv = (TextView) rootView.findViewById(R.id.textViewDailyCashTotal);
        tv.setText(String.valueOf(iTotalValueCash));
        tv = (TextView) rootView.findViewById(R.id.textViewDailyOnlineTotal);
        tv.setText(String.valueOf(iTotalValueOnline));
        tv = (TextView) rootView.findViewById(R.id.textViewDailyTotal);
        tv.setText(String.valueOf(iTotalValue));

        if(selectedCollectionCenter != null) {
            selectedCollectionCenter.CenterNarationValueTotal = iCenterValueCash;
//            selectedCollectionCenter.CenterCashCollectionTotal = iCenterValueCash;
            selectedCollectionCenter.CenterOnlineCollectionTotal = iCenterValueOnline;
            selectedCollectionCenter.CenterCollectionTotal = iCenterValueTotal;

            if(!selectedCollectionCenter.NextRecoveryDate.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                //yyyy-MM-dd Format for DB
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                try {
                    Date nextDate = sdf2.parse(selectedCollectionCenter.NextRecoveryDate);
                    Calendar calnextDate = Calendar.getInstance();
                    calnextDate.setTime(nextDate);
                    String strDate = "";
                    switch(calnextDate.get(Calendar.DAY_OF_WEEK)) {
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
                    strDate = strDate + " " + sdf.format(calnextDate.getTime());// all done
                    etNextRecoveryDate.setText(strDate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            if(!selectedCollectionCenter.NextRecoveryTime.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
                //24Hour Format for DB
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                try {
                    Date nextTime = sdf2.parse(selectedCollectionCenter.NextRecoveryTime);
                    Calendar calnextTime = Calendar.getInstance();
                    calnextTime.setTime(nextTime);
                    int hourOfDay = calnextTime.get(Calendar.HOUR_OF_DAY);
                    String strTime = "सकाळी ";
                    if (calnextTime.get(Calendar.AM_PM) == Calendar.PM) {
                        if(hourOfDay < 17)
                            strTime = "दुपारी ";
                        else if(hourOfDay < 19)
                            strTime = "संध्याकाळी ";
                        else
                            strTime = "रात्री ";
                    }
                    strTime = strTime + sdf.format(calnextTime.getTime());// all done
                    etNextRecoveryTime.setText(strTime);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            Button bt = (Button) rootView.findViewById(R.id.buttonSaveCollection);
//bmp 14-Jul-26
//            if (    (!selectedCollectionCenter.NextRecoveryDate.isEmpty()) &&
//                    (!selectedCollectionCenter.NextRecoveryTime.isEmpty()) &&
//                    (!selectedCollectionCenter.NextRecoveryDateTime.isEmpty()) &&
//                    (selectedCollectionCenter.CenterCashCollectionTotal == selectedCollectionCenter.CenterNarationValueTotal) ) {
            if (    (!selectedCollectionCenter.NextRecoveryDate.isEmpty()) &&
                    (!selectedCollectionCenter.NextRecoveryTime.isEmpty()) &&
                    (!selectedCollectionCenter.NextRecoveryDateTime.isEmpty()) &&
                    (selectedCollectionCenter.CenterCashCollectionTotal == selectedCollectionCenter.CenterNarationValueTotal) &&
                    (selectedCollectionCenter.CenterCollectionTotal > 0) ) {
//bmp 14-Jul-26
                bt.setEnabled(true);
            } else {
                bt.setEnabled(false);
            }
            bt = (Button) rootView.findViewById(R.id.buttonSummaryCollection);
            bt.setEnabled(false);
        } else {
            etNextRecoveryDate.setText("");
            etNextRecoveryTime.setText("");
            etNextRecoveryDate.setEnabled(false);
            etNextRecoveryTime.setEnabled(false);

            Button bt = (Button) rootView.findViewById(R.id.buttonSummaryCollection);
            bt.setEnabled(false);
            if(iTotalValue > 0) {
                bt.setEnabled(true);
            }
        }

        tv = (TextView) rootView.findViewById(R.id.textViewRequestFocus);
        tv.requestFocus();
    }
    void EnableNarationEdit(boolean Flag) {
        //Disable Naration Editing
        EditText et = (EditText) rootView.findViewById(R.id.editTextCenterCount500);
        et.setEnabled(Flag);
        et = (EditText) rootView.findViewById(R.id.editTextCenterCount200);
        et.setEnabled(Flag);
        et = (EditText) rootView.findViewById(R.id.editTextCenterCount100);
        et.setEnabled(Flag);
        et = (EditText) rootView.findViewById(R.id.editTextCenterCount50);
        et.setEnabled(Flag);
        et = (EditText) rootView.findViewById(R.id.editTextCenterCount20);
        et.setEnabled(Flag);
        et = (EditText) rootView.findViewById(R.id.editTextCenterCount10);
        et.setEnabled(Flag);
        et = (EditText) rootView.findViewById(R.id.editTextCenterCount5);
        et.setEnabled(Flag);
        et = (EditText) rootView.findViewById(R.id.editTextCenterCount2);
        et.setEnabled(Flag);
        et = (EditText) rootView.findViewById(R.id.editTextCenterCount1);
        et.setEnabled(Flag);
    }

    void RefreshCollectionCenterList(String strSelectDate, long lSelectClusterID, long lSelectCommunityID) {
        ArrayList<String> CenterInfoList = new ArrayList<String>();
        ArrayList<clsCollectionCenter> CenterList = new ArrayList<clsCollectionCenter>();
        ArrayList<Integer> CollectionCenterTime = new ArrayList<Integer>();
        CollectionCenterTime.clear();
        CollectionCenterTime.add(-1);
        CenterInfoList.clear();
        CenterList.clear();
        CenterInfoList.add("");
        CenterList.add(null);
        if(!strSelectDate.isEmpty()) {
            for (int j = 0; j < mainActivity.CollectionCenterList.size(); j++) {
                if (!mainActivity.CollectionCenterList.get(j).RecoveryDate.equalsIgnoreCase(strSelectDate))
                    continue;
                if (    (mainActivity.CollectionCenterList.get(j).CollectionStatus == clsCollectionCenter.COLLECTION_CENTER_STATUS_SCHEDULED) ||
                        (mainActivity.CollectionCenterList.get(j).CollectionStatus == clsCollectionCenter.COLLECTION_CENTER_STATUS_PARTIAL) ||
                        (mainActivity.CollectionCenterList.get(j).CollectionStatus == clsCollectionCenter.COLLECTION_CENTER_STATUS_FULL) ) {
                    CenterList.add(mainActivity.CollectionCenterList.get(j));
                    String strTemp =    "C-" + String.valueOf(mainActivity.CollectionCenterList.get(j).CenterID) +
                            ":" + mainActivity.CollectionCenterList.get(j).RecoveryTime +
                            ":" + mainActivity.CollectionCenterList.get(j).CommunityName +
                            ":" + mainActivity.CollectionCenterList.get(j).CollectionMemberName;
                    CenterInfoList.add(strTemp);

                    String strTemp1 = mainActivity.CollectionCenterList.get(j).RecoveryDateTime.substring(12, 14) + mainActivity.CollectionCenterList.get(j).RecoveryDateTime.substring(15, 17);
                    CollectionCenterTime.add(Integer.valueOf(strTemp1));
                }
            }
        } else if(lSelectClusterID > 0) {
            for (int j = 0; j < mainActivity.CollectionCenterList.size(); j++) {
                if (mainActivity.CollectionCenterList.get(j).ClusterID != lSelectClusterID)
                    continue;
                if (    (mainActivity.CollectionCenterList.get(j).CollectionStatus == clsCollectionCenter.COLLECTION_CENTER_STATUS_SCHEDULED) ||
                        (mainActivity.CollectionCenterList.get(j).CollectionStatus == clsCollectionCenter.COLLECTION_CENTER_STATUS_PARTIAL) ||
                        (mainActivity.CollectionCenterList.get(j).CollectionStatus == clsCollectionCenter.COLLECTION_CENTER_STATUS_FULL) ) {
                    CenterList.add(mainActivity.CollectionCenterList.get(j));
                    String strTemp =    "C-" + String.valueOf(mainActivity.CollectionCenterList.get(j).CenterID) +
                            ":" + mainActivity.CollectionCenterList.get(j).RecoveryTime +
                            ":" + mainActivity.CollectionCenterList.get(j).CommunityName +
                            ":" + mainActivity.CollectionCenterList.get(j).CollectionMemberName;
                    CenterInfoList.add(strTemp);

                    String strTemp1 = mainActivity.CollectionCenterList.get(j).RecoveryDateTime.substring(12, 14) + mainActivity.CollectionCenterList.get(j).RecoveryDateTime.substring(15, 17);
                    CollectionCenterTime.add(Integer.valueOf(strTemp1));
                }
            }
        } else if(lSelectCommunityID > 0) {
            for (int j = 0; j < mainActivity.CollectionCenterList.size(); j++) {
                if (mainActivity.CollectionCenterList.get(j).CommunityID != lSelectCommunityID)
                    continue;
                if (    (mainActivity.CollectionCenterList.get(j).CollectionStatus == clsCollectionCenter.COLLECTION_CENTER_STATUS_SCHEDULED) ||
                        (mainActivity.CollectionCenterList.get(j).CollectionStatus == clsCollectionCenter.COLLECTION_CENTER_STATUS_PARTIAL) ||
                        (mainActivity.CollectionCenterList.get(j).CollectionStatus == clsCollectionCenter.COLLECTION_CENTER_STATUS_FULL) ) {
                    CenterList.add(mainActivity.CollectionCenterList.get(j));
                    String strTemp =    "C-" + String.valueOf(mainActivity.CollectionCenterList.get(j).CenterID) +
                            ":" + mainActivity.CollectionCenterList.get(j).RecoveryTime +
                            ":" + mainActivity.CollectionCenterList.get(j).CommunityName +
                            ":" + mainActivity.CollectionCenterList.get(j).CollectionMemberName;
                    CenterInfoList.add(strTemp);

                    String strTemp1 = mainActivity.CollectionCenterList.get(j).RecoveryDateTime.substring(12, 14) + mainActivity.CollectionCenterList.get(j).RecoveryDateTime.substring(15, 17);
                    CollectionCenterTime.add(Integer.valueOf(strTemp1));
                }
            }
        } else {
            for (int j = 0; j < mainActivity.CollectionCenterList.size(); j++) {
                if (    (mainActivity.CollectionCenterList.get(j).CollectionStatus == clsCollectionCenter.COLLECTION_CENTER_STATUS_SCHEDULED) ||
                        (mainActivity.CollectionCenterList.get(j).CollectionStatus == clsCollectionCenter.COLLECTION_CENTER_STATUS_PARTIAL) ||
                        (mainActivity.CollectionCenterList.get(j).CollectionStatus == clsCollectionCenter.COLLECTION_CENTER_STATUS_FULL) ) {
                    CenterList.add(mainActivity.CollectionCenterList.get(j));
                    System.out.println(String.valueOf(mainActivity.CollectionCenterList.get(j).CenterID));
                    String strTemp =    "C-" + String.valueOf(mainActivity.CollectionCenterList.get(j).CenterID) +
                            ":" + mainActivity.CollectionCenterList.get(j).RecoveryTime +
                            ":" + mainActivity.CollectionCenterList.get(j).CommunityName +
                            ":" + mainActivity.CollectionCenterList.get(j).CollectionMemberName;
                    CenterInfoList.add(strTemp);

                    String strTemp1 = mainActivity.CollectionCenterList.get(j).RecoveryDateTime.substring(12, 14) + mainActivity.CollectionCenterList.get(j).RecoveryDateTime.substring(15, 17);
                    CollectionCenterTime.add(Integer.valueOf(strTemp1));
                    System.out.println("j = " + String.valueOf(j) + " strTemp1 = " + strTemp1);
                }
            }
        }

        AscCenterInfoList.clear();
        ColorAscCenterInfoList.clear();
        AscCenterList.clear();
        AscCenterInfoList.add("");
        ColorAscCenterInfoList.add(Color.WHITE);
        AscCenterList.add(null);
        for(int i=0; i<2400; i++) {
            for(int j=1; j<CollectionCenterTime.size(); j++) {
                if(CollectionCenterTime.get(j) == i) {
                    AscCenterList.add(CenterList.get(j));
                    AscCenterInfoList.add(CenterInfoList.get(j));
                    if(CenterList.get(j).CollectionStatus == clsCollectionCenter.COLLECTION_CENTER_STATUS_FULL)
                        ColorAscCenterInfoList.add(Color.GREEN);
                    else if(CenterList.get(j).CollectionStatus == clsCollectionCenter.COLLECTION_CENTER_STATUS_PARTIAL)
                        ColorAscCenterInfoList.add(Color.YELLOW);
                    else
                        ColorAscCenterInfoList.add(Color.argb(255, 250, 180, 180));
                }
            }
        }

        CustomSpinnerAdapter adapterCollectionCenterInfo = new CustomSpinnerAdapter(
                mainActivity.getBaseContext(), AscCenterInfoList, ColorAscCenterInfoList);
        adapterCollectionCenterInfo.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_small);
        Spinner spCollectionCenterInfo = (Spinner) rootView.findViewById(R.id.spinnerCollectionCenterInfo);
        spCollectionCenterInfo.setAdapter(adapterCollectionCenterInfo);
        spCollectionCenterInfo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner sp0 = (Spinner) rootView.findViewById(R.id.spinnerCollectionMember);
                sp0.setSelection(0);
                TextView tv0 = (TextView) rootView.findViewById(R.id.textViewDueCollectionTotal);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewCenterCollectionTotal);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewCenterCashTotal);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewCenterOnlineTotal);
                tv0.setText("");

                tv0 = (TextView) rootView.findViewById(R.id.textViewReferencedateTotalDue);
                tv0.setText("dd-MMM देय रू.");
                tv0 = (TextView) rootView.findViewById(R.id.textViewTodateTotalDue);
                tv0.setText("dd-MMM देय रू.");

//                tv0 = (TextView) rootView.findViewById(R.id.textViewLoanEMIReferencedate);
//                tv0.setText("कर्ज हप्ता dd-MMM");
//                tv0 = (TextView) rootView.findViewById(R.id.textViewLoanEMITodate);
//                tv0.setText("कर्ज हप्ता dd-MMM");

                tv0 = (TextView) rootView.findViewById(R.id.textViewTotalDueReferencedate);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewTotalDueTodate);
                tv0.setText("");

//                tv0 = (TextView) rootView.findViewById(R.id.textViewReferencedateLoanEMIPast);
//                tv0.setText("");
//                tv0 = (TextView) rootView.findViewById(R.id.textViewReferencedateLoanEMICurrent);
//                tv0.setText("");
//                tv0 = (TextView) rootView.findViewById(R.id.textViewReferencedateLoanEMI);
//                tv0.setText("");

                tv0 = (TextView) rootView.findViewById(R.id.textViewTodateLoanEMIPast);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewTodateLoanEMICurrent);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewTodateLoanEMI);
                tv0.setText("");

                tv0 = (TextView) rootView.findViewById(R.id.textViewJLGPast);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewJLGCurrent);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewTodateJLG);
                tv0.setText("");

                tv0 = (TextView) rootView.findViewById(R.id.textViewNidhiPast);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewNidhiCurrent);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewTodateNidhi);
                tv0.setText("");

                tv0 = (TextView) rootView.findViewById(R.id.textViewADHPPast);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewADHPCurrent);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewADHP);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewRDAmount1Past);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewRDAmount1Current);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewRDAmount1);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewRDAmount2Past);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewRDAmount2Current);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewRDAmount2);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewRDAmount3Past);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewRDAmount3Current);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewRDAmount3);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewRDAmount4Past);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewRDAmount4Current);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewRDAmount4);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewRDAmount5Past);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewRDAmount5Current);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewRDAmount5);
                tv0.setText("");
                CheckBox cb0 = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD1);
                cb0.setText("");
                cb0 = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD2);
                cb0.setText("");
                cb0 = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD3);
                cb0.setText("");
                cb0 = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD4);
                cb0.setText("");
                cb0 = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD5);
                cb0.setText("");

                cb0 = (CheckBox) rootView.findViewById(R.id.checkBoxPhoneNo);
                cb0.setChecked(false);
                EditText et0 = (EditText) rootView.findViewById(R.id.editTextPhoneNo);
                et0.setText("");
                CheckBox cbCash = (CheckBox) rootView.findViewById(R.id.checkBoxPaymentCash);
                cbCash.setChecked(false);
                CheckBox cbOnline = (CheckBox) rootView.findViewById(R.id.checkBoxPaymentOnline);
                cbOnline.setChecked(false);
                et0 = (EditText) rootView.findViewById(R.id.editTextTotalCollection);
                et0.setText("");
                et0 = (EditText) rootView.findViewById(R.id.editTextLoanEMICollection);
                et0.setText("");
                et0 = (EditText) rootView.findViewById(R.id.editTextJLGCollection);
                et0.setText("");
                et0 = (EditText) rootView.findViewById(R.id.editTextNidhiCollection);
                et0.setText("");
                et0 = (EditText) rootView.findViewById(R.id.editTextADHPCollection);
                et0.setText("");

                et0 = (EditText) rootView.findViewById(R.id.editTextRDCollection1);
                et0.setText("");
                et0 = (EditText) rootView.findViewById(R.id.editTextRDCollection2);
                et0.setText("");
                et0 = (EditText) rootView.findViewById(R.id.editTextRDCollection3);
                et0.setText("");
                et0 = (EditText) rootView.findViewById(R.id.editTextRDCollection4);
                et0.setText("");
                et0 = (EditText) rootView.findViewById(R.id.editTextRDCollection5);
                et0.setText("");

                et0 = (EditText) rootView.findViewById(R.id.editTextAdditionalRepay);
                et0.setText("");

                tv0 = (TextView) rootView.findViewById(R.id.textViewExtraCollection);
                tv0.setText("");
                et0 = (EditText) rootView.findViewById(R.id.editTextExtraADHP);
                et0.setText("");
                et0 = (EditText) rootView.findViewById(R.id.editTextNewRD);
                et0.setText("");
                et0 = (EditText) rootView.findViewById(R.id.editTextFDCollection);
                et0.setText("");
                et0 = (EditText) rootView.findViewById(R.id.editTextExtraJLG);
                et0.setText("");
                et0 = (EditText) rootView.findViewById(R.id.editTextReprintCharges);
                et0.setText("");

                tv0 = (TextView) rootView.findViewById(R.id.textViewStdRecoveryDay);
                tv0.setText("");
                tv0 = (TextView) rootView.findViewById(R.id.textViewStdRecoveryTime);
                tv0.setText("");

                et0 = (EditText) rootView.findViewById(R.id.editTextNextRecoveryDate);
                et0.setText("");
                et0 = (EditText) rootView.findViewById(R.id.editTextNextRecoveryTime);
                et0.setText("");

                if(position == 0)
                    return;

                Spinner spCollectionDate = (Spinner) rootView.findViewById(R.id.spinnerCollectionDate);
                spCollectionDate.setEnabled(false);
                Spinner spCollectionCenterInfo = (Spinner) rootView.findViewById(R.id.spinnerCollectionCenterInfo);
                spCollectionCenterInfo.setEnabled(false);
                selectedCollectionCenter = new clsCollectionCenter();
                selectedCollectionCenter.InitCollectionCenter();
                EnableNarationEdit(false);
                AscCenterList.get(position).Copy_CollectionCenter(selectedCollectionCenter);
                ArrayList<String> UnSortedCenterMemberInfoList = new ArrayList<String>();
                ArrayList<clsCenterMember> UnSortedCenterMemberList = new ArrayList<clsCenterMember>();
                UnSortedCenterMemberInfoList.clear();
                UnSortedCenterMemberInfoList.add("");
                UnSortedCenterMemberList.clear();
                UnSortedCenterMemberList.add(null);

                for(int i=0; i<mainActivity.CollectionMemberList.size(); i++) {
                    if(mainActivity.CollectionMemberList.get(i).CenterID != selectedCollectionCenter.CenterID)
                        continue;
                    if(     (mainActivity.CollectionMemberList.get(i).MemberStatus == clsCollectionMember.COLLECTION_MEMBER_STATUS_SCHEDULED) ||
                            (mainActivity.CollectionMemberList.get(i).MemberStatus == clsCollectionMember.COLLECTION_MEMBER_STATUS_PARTIAL) ||
                            (mainActivity.CollectionMemberList.get(i).MemberStatus == clsCollectionMember.COLLECTION_MEMBER_STATUS_FULL) ) {
                        clsCenterMember centerMember = new clsCenterMember();
                        centerMember.InitCenterMember();
                        mainActivity.CollectionMemberList.get(i).Copy_CollectionMember(centerMember.collectionMember);
                        centerMember.indexCollectionMemberList = i;
                        UnSortedCenterMemberList.add(centerMember);
                        String strSavingAccNo_MemberName
                                = mainActivity.CollectionMemberList.get(i).SavingAccNo + ":"
                                + mainActivity.CollectionMemberList.get(i).MemberName;
                        UnSortedCenterMemberInfoList.add(strSavingAccNo_MemberName);
                    }
                }
                for(int i=0; i<mainActivity.RDReceiptList.size(); i++) {
                    if(mainActivity.RDReceiptList.get(i).CenterID != selectedCollectionCenter.CenterID)
                        continue;
                    if(     (mainActivity.RDReceiptList.get(i).CollectionStatus == clsRDReceipt.COLLECTION_RDRECEIPT_STATUS_SCHEDULED) ||
                            (mainActivity.RDReceiptList.get(i).CollectionStatus == clsRDReceipt.COLLECTION_RDRECEIPT_STATUS_PARTIAL) ||
                            (mainActivity.RDReceiptList.get(i).CollectionStatus == clsRDReceipt.COLLECTION_RDRECEIPT_STATUS_FULL) ) {
                        for(int j=1; j<UnSortedCenterMemberInfoList.size(); j++) {
                            if(mainActivity.RDReceiptList.get(i).OperatingMemberID == UnSortedCenterMemberList.get(j).collectionMember.MemberID) {
                                UnSortedCenterMemberList.get(j).memberRDReceipt.add(mainActivity.RDReceiptList.get(i));
                                UnSortedCenterMemberList.get(j).collectionMember.TotalRDCount = UnSortedCenterMemberList.get(j).collectionMember.TotalRDCount + 1;
                                switch (UnSortedCenterMemberList.get(j).collectionMember.TotalRDCount) {
                                    case 1:
                                        UnSortedCenterMemberList.get(j).collectionMember.RD1DetailsID = mainActivity.RDReceiptList.get(i).RDDetailsID;
                                        UnSortedCenterMemberList.get(j).collectionMember.RD1PastDue = mainActivity.RDReceiptList.get(i).PastDue;
                                        UnSortedCenterMemberList.get(j).collectionMember.RD1CurrentInstallmentAmount = mainActivity.RDReceiptList.get(i).ExpectedInstallmentAmt;
                                        UnSortedCenterMemberList.get(j).collectionMember.RD1ExpectedAmount
                                                = mainActivity.RDReceiptList.get(i).PastDue + mainActivity.RDReceiptList.get(i).ExpectedInstallmentAmt;
                                        UnSortedCenterMemberList.get(j).index1RDReceipts = i;
                                        UnSortedCenterMemberList.get(j).collectionMember.RD1MonthlyInstallmentAmt = mainActivity.RDReceiptList.get(i).MonthlyInstallmentAmt;
                                        break;
                                    case 2:
                                        UnSortedCenterMemberList.get(j).collectionMember.RD2DetailsID = mainActivity.RDReceiptList.get(i).RDDetailsID;
                                        UnSortedCenterMemberList.get(j).collectionMember.RD2PastDue = mainActivity.RDReceiptList.get(i).PastDue;
                                        UnSortedCenterMemberList.get(j).collectionMember.RD2CurrentInstallmentAmount = mainActivity.RDReceiptList.get(i).ExpectedInstallmentAmt;
                                        UnSortedCenterMemberList.get(j).collectionMember.RD2ExpectedAmount
                                                = mainActivity.RDReceiptList.get(i).PastDue + mainActivity.RDReceiptList.get(i).ExpectedInstallmentAmt;
                                        UnSortedCenterMemberList.get(j).index2RDReceipts = i;
                                        UnSortedCenterMemberList.get(j).collectionMember.RD2MonthlyInstallmentAmt = mainActivity.RDReceiptList.get(i).MonthlyInstallmentAmt;
                                        break;
                                    case 3:
                                        UnSortedCenterMemberList.get(j).collectionMember.RD3DetailsID = mainActivity.RDReceiptList.get(i).RDDetailsID;
                                        UnSortedCenterMemberList.get(j).collectionMember.RD3PastDue = mainActivity.RDReceiptList.get(i).PastDue;
                                        UnSortedCenterMemberList.get(j).collectionMember.RD3CurrentInstallmentAmount = mainActivity.RDReceiptList.get(i).ExpectedInstallmentAmt;
                                        UnSortedCenterMemberList.get(j).collectionMember.RD3ExpectedAmount
                                                = mainActivity.RDReceiptList.get(i).PastDue + mainActivity.RDReceiptList.get(i).ExpectedInstallmentAmt;
                                        UnSortedCenterMemberList.get(j).index3RDReceipts = i;
                                        UnSortedCenterMemberList.get(j).collectionMember.RD3MonthlyInstallmentAmt = mainActivity.RDReceiptList.get(i).MonthlyInstallmentAmt;
                                        break;
                                    case 4:
                                        UnSortedCenterMemberList.get(j).collectionMember.RD4DetailsID = mainActivity.RDReceiptList.get(i).RDDetailsID;
                                        UnSortedCenterMemberList.get(j).collectionMember.RD4PastDue = mainActivity.RDReceiptList.get(i).PastDue;
                                        UnSortedCenterMemberList.get(j).collectionMember.RD4CurrentInstallmentAmount = mainActivity.RDReceiptList.get(i).ExpectedInstallmentAmt;
                                        UnSortedCenterMemberList.get(j).collectionMember.RD4ExpectedAmount
                                                = mainActivity.RDReceiptList.get(i).PastDue + mainActivity.RDReceiptList.get(i).ExpectedInstallmentAmt;
                                        UnSortedCenterMemberList.get(j).index4RDReceipts = i;
                                        UnSortedCenterMemberList.get(j).collectionMember.RD4MonthlyInstallmentAmt = mainActivity.RDReceiptList.get(i).MonthlyInstallmentAmt;
                                        break;
                                    case 5:
                                        UnSortedCenterMemberList.get(j).collectionMember.RD5DetailsID = mainActivity.RDReceiptList.get(i).RDDetailsID;
                                        UnSortedCenterMemberList.get(j).collectionMember.RD5PastDue = mainActivity.RDReceiptList.get(i).PastDue;
                                        UnSortedCenterMemberList.get(j).collectionMember.RD5CurrentInstallmentAmount = mainActivity.RDReceiptList.get(i).ExpectedInstallmentAmt;
                                        UnSortedCenterMemberList.get(j).collectionMember.RD5ExpectedAmount
                                                = mainActivity.RDReceiptList.get(i).PastDue + mainActivity.RDReceiptList.get(i).ExpectedInstallmentAmt;
                                        UnSortedCenterMemberList.get(j).index5RDReceipts = i;
                                        UnSortedCenterMemberList.get(j).collectionMember.RD5MonthlyInstallmentAmt = mainActivity.RDReceiptList.get(i).MonthlyInstallmentAmt;
                                        break;
                                }
                                break;
                            }
                        }
                    }
                }

                CenterMemberInfoList.clear();
                ColorCenterMemberInfoList.clear();
                CenterMemberList.clear();
                CenterMemberInfoList.add("");
                ColorCenterMemberInfoList.add(Color.WHITE);
                CenterMemberList.add(null);
                currentCenterMemberListPosition = -1;

                for(int i=1; i<UnSortedCenterMemberList.size(); i++) {
                    if(!UnSortedCenterMemberList.get(i).collectionMember.EMIReceiptNumber.isEmpty()) {
                        clsCenterMember centerMember = new clsCenterMember();
                        centerMember.InitCenterMember();
                        UnSortedCenterMemberList.get(i).Copy_CenterMember(centerMember);
                        CenterMemberList.add(centerMember);
                        String strLoanMember = UnSortedCenterMemberInfoList.get(i);
//bmp 06=Aug-26
//                        if(UnSortedCenterMemberList.get(i).collectionMember.Internal_Transfer_Reason != null) {
//                            if(!UnSortedCenterMemberList.get(i).collectionMember.Internal_Transfer_Reason.isEmpty()) {
//                                strLoanMember = strLoanMember + ":" + UnSortedCenterMemberList.get(i).collectionMember.Internal_Transfer_Reason;
//                            }
//                        }
//bmp 06=Aug-26
                        CenterMemberInfoList.add(strLoanMember);
                        if(UnSortedCenterMemberList.get(i).collectionMember.MemberStatus == clsCollectionMember.COLLECTION_MEMBER_STATUS_FULL)
                            ColorCenterMemberInfoList.add(Color.GREEN);
                        else if(UnSortedCenterMemberList.get(i).collectionMember.MemberStatus == clsCollectionMember.COLLECTION_MEMBER_STATUS_PARTIAL)
                            ColorCenterMemberInfoList.add(Color.YELLOW);
                        else
                            ColorCenterMemberInfoList.add(Color.argb(255, 250, 180, 180));
                    }
                }
                for(int i=1; i<UnSortedCenterMemberList.size(); i++) {
                    if(UnSortedCenterMemberList.get(i).collectionMember.EMIReceiptNumber.isEmpty()) {
                        clsCenterMember centerMember = new clsCenterMember();
                        centerMember.InitCenterMember();
                        UnSortedCenterMemberList.get(i).Copy_CenterMember(centerMember);
                        CenterMemberList.add(centerMember);
                        String strNoLoanMember = UnSortedCenterMemberInfoList.get(i) + ": NoLoan";
                        CenterMemberInfoList.add(strNoLoanMember);
                        if(UnSortedCenterMemberList.get(i).collectionMember.MemberStatus == clsCollectionMember.COLLECTION_MEMBER_STATUS_FULL)
                            ColorCenterMemberInfoList.add(Color.GREEN);
                        else if(UnSortedCenterMemberList.get(i).collectionMember.MemberStatus == clsCollectionMember.COLLECTION_MEMBER_STATUS_PARTIAL)
                            ColorCenterMemberInfoList.add(Color.YELLOW);
                        else
                            ColorCenterMemberInfoList.add(Color.argb(255, 250, 180, 180));
                    }
                }

                int iDueCollectionTotal = 0;
                for(int i=1; i<CenterMemberList.size(); i++) {
                    iDueCollectionTotal = iDueCollectionTotal + CenterMemberList.get(i).collectionMember.TotalDueAmountsToPayRefday;
                    int iTemp = InterestDeltaTodate(CenterMemberList.get(i).indexCollectionMemberList);
                    iDueCollectionTotal = iDueCollectionTotal + iTemp;
                }
                selectedCollectionCenter.CenterDueCollectionTotal = iDueCollectionTotal;
                TextView tv = (TextView) rootView.findViewById(R.id.textViewDueCollectionTotal);
                tv.setText(String.valueOf(selectedCollectionCenter.CenterDueCollectionTotal));

                SQLite_getCenterCollection();

                tv0 = (TextView) rootView.findViewById(R.id.textViewStdRecoveryDay);
                tv0.setText(selectedCollectionCenter.StdRecoveryDay);
                int iHour = Integer.parseInt(selectedCollectionCenter.StdRecoveryTime.substring(0, 2));
                String strTime = "सकाळी ";
                if(selectedCollectionCenter.StdRecoveryTime.contains("PM")){
                    if((iHour == 12) || (iHour < 5))
                        strTime = "दुपारी ";
                    else if(iHour < 7)
                        strTime = "संध्याकाळी ";
                    else
                        strTime = "रात्री ";
                }
                strTime = strTime + selectedCollectionCenter.StdRecoveryTime.substring(0, 5);
                tv0 = (TextView) rootView.findViewById(R.id.textViewStdRecoveryTime);
                tv0.setText(strTime);

                if(selectedCollectionCenter.NextRecoveryDate.isEmpty()) {
                    et0 = (EditText) rootView.findViewById(R.id.editTextNextRecoveryDate);
                    et0.setText("");
                    et0.setEnabled(true);
                    et0 = (EditText) rootView.findViewById(R.id.editTextNextRecoveryTime);
                    et0.setText("");
                    et0.setEnabled(true);
                } else {
                    et0 = (EditText) rootView.findViewById(R.id.editTextNextRecoveryDate);
                    et0.setText(selectedCollectionCenter.NextRecoveryDate);
                    et0.setEnabled(false);
                    et0 = (EditText) rootView.findViewById(R.id.editTextNextRecoveryTime);
                    et0.setText(selectedCollectionCenter.NextRecoveryTime);
                    et0.setEnabled(false);
                }

                RefreshCollectionMemberList();
                RefreshStartCollection();
                RefreshCollection();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spCollectionCenterInfo.setSelection(0);
        spCollectionCenterInfo.setFocusable(true);
        spCollectionCenterInfo.setFocusableInTouchMode(true);
    }
    void RefreshCollectionMemberList() {
        System.out.println("CenterMemberInfoList: " + String.valueOf(CenterMemberInfoList.size()));

        CustomSpinnerAdapter adapterCollectionMemberInfo = new CustomSpinnerAdapter(
                mainActivity.getBaseContext(), CenterMemberInfoList, ColorCenterMemberInfoList);
        adapterCollectionMemberInfo.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner spCollectionMemberInfo = (Spinner) rootView.findViewById(R.id.spinnerCollectionMember);
        spCollectionMemberInfo.setAdapter(adapterCollectionMemberInfo);
        spCollectionMemberInfo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if((position == 0) && (currentCenterMemberListPosition <= 0))
                    return;
                if(position == currentCenterMemberListPosition)
                    return;
                if(selectedCollectionMember != null) {
                    int iTemp = selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount +
                            selectedCollectionMember.collectionMember.NewRDCollectionAmount +
                            selectedCollectionMember.collectionMember.FDCollectionAmount +
                            selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount +
                            selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount;
                    if(selectedCollectionMember.collectionMember.ExtraCollectionAmount != iTemp) {
                        CreateAlertDialog(true, false);
                        dlgState = ufin_COLLECTION_DIALOG_STATE_INCORRECT_EXTRA_COLLECTION;
                        dlgAlert.setMessage(MSG_ufin_COLLECTION_INCORRECT_EXTRA_COLLECTION);
                        AlertDialog dialog = dlgAlert.create();
                        dialog.show();
                        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                        textView.setTextSize(30);
                        textView.setTextColor(Color.RED);

                        Spinner spCollectionMemberInfo = (Spinner) rootView.findViewById(R.id.spinnerCollectionMember);
                        spCollectionMemberInfo.setSelection(currentCenterMemberListPosition);

                        return;
                    }

                    if(position == 0) {
                        Spinner spCollectionMemberInfo = (Spinner) rootView.findViewById(R.id.spinnerCollectionMember);
                        spCollectionMemberInfo.setSelection(currentCenterMemberListPosition);
                        return;
                    }

                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PhoneNo
                            = selectedCollectionMember.collectionMember.PhoneNo;
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).VerifiedPhoneNo
                            = selectedCollectionMember.collectionMember.VerifiedPhoneNo;

                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).TotalCollectionAmount
                            = selectedCollectionMember.collectionMember.TotalCollectionAmount;
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).TotalEMICollectionAmount
                            = selectedCollectionMember.collectionMember.TotalEMICollectionAmount;
//bmp 12-Aug-26
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).JLGCollectionAmount
                            = selectedCollectionMember.collectionMember.JLGCollectionAmount;
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).NidhiCollectionAmount
                            = selectedCollectionMember.collectionMember.NidhiCollectionAmount;
//bmp 12-Aug-26
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).TotalADHPCollectionAmount
                            = selectedCollectionMember.collectionMember.TotalADHPCollectionAmount;
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).AdditionalRepayCollectionAmount
                            = selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount;

                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).ExtraCollectionAmount
                            = selectedCollectionMember.collectionMember.ExtraCollectionAmount;
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).ExtraADHPCollectionAmount
                            = selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount;
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).NewRDCollectionAmount
                            = selectedCollectionMember.collectionMember.NewRDCollectionAmount;
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).FDCollectionAmount
                            = selectedCollectionMember.collectionMember.FDCollectionAmount;
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).ExtraJLGCollectionAmount
                            = selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount;
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).ReprintChargesCollectionAmount
                            = selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount;

                    if(selectedCollectionMember.collectionMember.RD1CollectionAmount > 0) {
                        CenterMemberList.get(currentCenterMemberListPosition).collectionMember.RD1Select = true;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).RD1CollectionAmount
                                = selectedCollectionMember.collectionMember.RD1CollectionAmount;
                        mainActivity.RDReceiptList.get(selectedCollectionMember.index1RDReceipts).ActualAmountReceived
                                = selectedCollectionMember.collectionMember.RD1CollectionAmount;
                    } else {
                        CenterMemberList.get(currentCenterMemberListPosition).collectionMember.RD1Select = false;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).RD1CollectionAmount = 0;
                        if(selectedCollectionMember.index1RDReceipts >= 0)
                            mainActivity.RDReceiptList.get(selectedCollectionMember.index1RDReceipts).ActualAmountReceived = 0;
                    }
                    if(selectedCollectionMember.collectionMember.RD2CollectionAmount > 0) {
                        CenterMemberList.get(currentCenterMemberListPosition).collectionMember.RD2Select = true;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).RD2CollectionAmount
                                = selectedCollectionMember.collectionMember.RD2CollectionAmount;
                        mainActivity.RDReceiptList.get(selectedCollectionMember.index2RDReceipts).ActualAmountReceived
                                = selectedCollectionMember.collectionMember.RD2CollectionAmount;
                    } else {
                        CenterMemberList.get(currentCenterMemberListPosition).collectionMember.RD2Select = false;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).RD2CollectionAmount = 0;
                        if(selectedCollectionMember.index2RDReceipts >= 0)
                            mainActivity.RDReceiptList.get(selectedCollectionMember.index2RDReceipts).ActualAmountReceived = 0;
                    }
                    if(selectedCollectionMember.collectionMember.RD3CollectionAmount > 0) {
                        CenterMemberList.get(currentCenterMemberListPosition).collectionMember.RD3Select = true;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).RD3CollectionAmount
                                = selectedCollectionMember.collectionMember.RD3CollectionAmount;
                        mainActivity.RDReceiptList.get(selectedCollectionMember.index3RDReceipts).ActualAmountReceived
                                = selectedCollectionMember.collectionMember.RD3CollectionAmount;
                    } else {
                        CenterMemberList.get(currentCenterMemberListPosition).collectionMember.RD3Select = false;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).RD3CollectionAmount = 0;
                        if(selectedCollectionMember.index3RDReceipts >= 0)
                            mainActivity.RDReceiptList.get(selectedCollectionMember.index3RDReceipts).ActualAmountReceived = 0;
                    }
                    if(selectedCollectionMember.collectionMember.RD4CollectionAmount > 0) {
                        CenterMemberList.get(currentCenterMemberListPosition).collectionMember.RD4Select = true;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).RD4CollectionAmount
                                = selectedCollectionMember.collectionMember.RD4CollectionAmount;
                        mainActivity.RDReceiptList.get(selectedCollectionMember.index4RDReceipts).ActualAmountReceived
                                = selectedCollectionMember.collectionMember.RD4CollectionAmount;
                    } else {
                        CenterMemberList.get(currentCenterMemberListPosition).collectionMember.RD4Select = false;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).RD4CollectionAmount = 0;
                        if(selectedCollectionMember.index4RDReceipts >= 0)
                            mainActivity.RDReceiptList.get(selectedCollectionMember.index4RDReceipts).ActualAmountReceived = 0;
                    }
                    if(selectedCollectionMember.collectionMember.RD5CollectionAmount > 0) {
                        CenterMemberList.get(currentCenterMemberListPosition).collectionMember.RD5Select = true;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).RD5CollectionAmount
                                = selectedCollectionMember.collectionMember.RD5CollectionAmount;
                        mainActivity.RDReceiptList.get(selectedCollectionMember.index5RDReceipts).ActualAmountReceived
                                = selectedCollectionMember.collectionMember.RD5CollectionAmount;
                    } else {
                        CenterMemberList.get(currentCenterMemberListPosition).collectionMember.RD5Select = false;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).RD5CollectionAmount = 0;
                        if(selectedCollectionMember.index5RDReceipts >= 0)
                            mainActivity.RDReceiptList.get(selectedCollectionMember.index5RDReceipts).ActualAmountReceived = 0;
                    }
//bmp 05-Aug-26
                    if(selectedCollectionMember.collectionMember.JLGCollectionAmount > 0) {
                        CenterMemberList.get(currentCenterMemberListPosition).collectionMember.JLGSelect = true;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).JLGCollectionAmount
                                = selectedCollectionMember.collectionMember.JLGCollectionAmount;
                    } else {
                        CenterMemberList.get(currentCenterMemberListPosition).collectionMember.JLGSelect = false;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).JLGCollectionAmount = 0;
                    }
                    if(selectedCollectionMember.collectionMember.NidhiCollectionAmount > 0) {
                        CenterMemberList.get(currentCenterMemberListPosition).collectionMember.NidhiSelect = true;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).NidhiCollectionAmount
                                = selectedCollectionMember.collectionMember.NidhiCollectionAmount;
                    } else {
                        CenterMemberList.get(currentCenterMemberListPosition).collectionMember.NidhiSelect = false;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).NidhiCollectionAmount = 0;
                    }
                    if(selectedCollectionMember.collectionMember.TotalADHPCollectionAmount > 0) {
                        CenterMemberList.get(currentCenterMemberListPosition).collectionMember.ADHPSelect = true;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).TotalADHPCollectionAmount
                                = selectedCollectionMember.collectionMember.TotalADHPCollectionAmount;
                    } else {
                        CenterMemberList.get(currentCenterMemberListPosition).collectionMember.ADHPSelect = false;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).TotalADHPCollectionAmount = 0;
                    }
//bmp 05-Aug-26
                    CenterMemberList.get(currentCenterMemberListPosition).collectionMember.PrePaymentReqMonths
                            = selectedCollectionMember.collectionMember.PrePaymentReqMonths;
                    CenterMemberList.get(currentCenterMemberListPosition).collectionMember.PrePaymentReqAmount
                            = selectedCollectionMember.collectionMember.PrePaymentReqAmount;
                    CenterMemberList.get(currentCenterMemberListPosition).collectionMember.PrePaymentApprovedMonths
                            = selectedCollectionMember.collectionMember.PrePaymentApprovedMonths;
                    CenterMemberList.get(currentCenterMemberListPosition).collectionMember.PrePaymentApprovedAmount
                            = selectedCollectionMember.collectionMember.PrePaymentApprovedAmount;
                    CenterMemberList.get(currentCenterMemberListPosition).collectionMember.PreClosureReasonID
                            = selectedCollectionMember.collectionMember.PreClosureReasonID;
                    CenterMemberList.get(currentCenterMemberListPosition).collectionMember.PreClosureRequestDate
                            = selectedCollectionMember.collectionMember.PreClosureRequestDate;
                    CenterMemberList.get(currentCenterMemberListPosition).collectionMember.PreClosureRequestStatus
                            = selectedCollectionMember.collectionMember.PreClosureRequestStatus;

                    CenterMemberList.get(currentCenterMemberListPosition).collectionMember.PreClosurePrincipal
                            = selectedCollectionMember.collectionMember.PreClosurePrincipal;
                    CenterMemberList.get(currentCenterMemberListPosition).collectionMember.PreClosureInterest
                            = selectedCollectionMember.collectionMember.PreClosureInterest;
                    CenterMemberList.get(currentCenterMemberListPosition).collectionMember.PreClosureLatePayCharge
                            = selectedCollectionMember.collectionMember.PreClosureLatePayCharge;

                    if(     (selectedCollectionMember.collectionMember.PrePaymentReqMonths > 0) ||
                            (selectedCollectionMember.collectionMember.PrePaymentReqAmount > 0)) {
                        CenterMemberList.get(currentCenterMemberListPosition).collectionMember.PreClosureJLG
                                = selectedCollectionMember.collectionMember.PreClosureJLG;
                        CenterMemberList.get(currentCenterMemberListPosition).collectionMember.PreClosurePolicyRenewalAmt
                                = selectedCollectionMember.collectionMember.PreClosurePolicyRenewalAmt;
                    }
                    CenterMemberList.get(currentCenterMemberListPosition).collectionMember.PreClosureADHP
                            = selectedCollectionMember.collectionMember.PreClosureADHP;
                    CenterMemberList.get(currentCenterMemberListPosition).collectionMember.PreClosureRD
                            = selectedCollectionMember.collectionMember.PreClosureRD;
                    CenterMemberList.get(currentCenterMemberListPosition).collectionMember.PreClosureAmount
                            = selectedCollectionMember.collectionMember.PreClosureAmount;
                    CenterMemberList.get(currentCenterMemberListPosition).collectionMember.PrePaymentORPreClosure
                            = selectedCollectionMember.collectionMember.PrePaymentORPreClosure;
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PrePaymentReqMonths
                            = selectedCollectionMember.collectionMember.PrePaymentReqMonths;
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PrePaymentReqAmount
                            = selectedCollectionMember.collectionMember.PrePaymentReqAmount;
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PrePaymentApprovedMonths
                            = selectedCollectionMember.collectionMember.PrePaymentApprovedMonths;
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PrePaymentApprovedAmount
                            = selectedCollectionMember.collectionMember.PrePaymentApprovedAmount;
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PreClosureReasonID
                            = selectedCollectionMember.collectionMember.PreClosureReasonID;
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PreClosureRequestDate
                            = selectedCollectionMember.collectionMember.PreClosureRequestDate;
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PreClosureRequestStatus
                            = selectedCollectionMember.collectionMember.PreClosureRequestStatus;

                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PreClosurePrincipal
                            = selectedCollectionMember.collectionMember.PreClosurePrincipal;
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PreClosureInterest
                            = selectedCollectionMember.collectionMember.PreClosureInterest;
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PreClosureLatePayCharge
                            = selectedCollectionMember.collectionMember.PreClosureLatePayCharge;

                    if(     (selectedCollectionMember.collectionMember.PrePaymentReqMonths > 0) ||
                            (selectedCollectionMember.collectionMember.PrePaymentReqAmount > 0)) {
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PreClosureJLG
                                = selectedCollectionMember.collectionMember.PreClosureJLG;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PreClosurePolicyRenewalAmt
                                = selectedCollectionMember.collectionMember.PreClosurePolicyRenewalAmt;
                    }
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PreClosureADHP
                            = selectedCollectionMember.collectionMember.PreClosureADHP;
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PreClosureRD
                            = selectedCollectionMember.collectionMember.PreClosureRD;
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PreClosureAmount
                            = selectedCollectionMember.collectionMember.PreClosureAmount;
                    mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PrePaymentORPreClosure
                            = selectedCollectionMember.collectionMember.PrePaymentORPreClosure;
                }
                selectedCollectionMember = new clsCenterMember();
                selectedCollectionMember.InitCenterMember();
                CenterMemberList.get(position).Copy_CenterMember(selectedCollectionMember);
                currentCenterMemberListPosition = position;

                ddMMMReferencedate = selectedCollectionMember.collectionMember.ReferenceRecoveryDate.substring(0, 6);
                int iInterestDelta = InterestDeltaTodate(selectedCollectionMember.indexCollectionMemberList);
                int iTemp = selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtRefday -
                            selectedCollectionMember.collectionMember.Expected_SD -
                            selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt -
                            selectedCollectionMember.collectionMember.Expected_LatePaymentCharges -
                            selectedCollectionMember.collectionMember.Expected_Penalty -
                            selectedCollectionMember.collectionMember.PastDue_SD -
                            selectedCollectionMember.collectionMember.PastDue_PolicyRenewalAmt -
                            selectedCollectionMember.collectionMember.PastDue_LatePaymentCharges -
                            selectedCollectionMember.collectionMember.PastDue_Penalty;
                if(iTemp <= 0) {
                    iInterestDelta = 0;
                }
                if(iInterestDelta <= 0) {
                    selectedCollectionMember.collectionMember.TotalDueAmountsToPayToday =
                            selectedCollectionMember.collectionMember.TotalDueAmountsToPayRefday;

                    selectedCollectionMember.collectionMember.LoanCurrentDueAmountToday =
                            selectedCollectionMember.collectionMember.LoanCurrentDueAmountRefday;
                    selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtToday =
                            selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtRefday;
                    selectedCollectionMember.collectionMember.LoanPastDueAmountTillToday =
                            selectedCollectionMember.collectionMember.LoanPastDueAmountTillRefday;
                } else {
                    selectedCollectionMember.collectionMember.TotalDueAmountsToPayToday =
                            selectedCollectionMember.collectionMember.TotalDueAmountsToPayRefday + iInterestDelta;

                    selectedCollectionMember.collectionMember.LoanCurrentDueAmountToday =
                            selectedCollectionMember.collectionMember.LoanCurrentDueAmountRefday + iInterestDelta;
                    selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtToday =
                            selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtRefday + iInterestDelta;
                    selectedCollectionMember.collectionMember.LoanPastDueAmountTillToday =
                            selectedCollectionMember.collectionMember.LoanPastDueAmountTillRefday;
                }
                mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).TotalDueAmountsToPayToday =
                        selectedCollectionMember.collectionMember.TotalDueAmountsToPayToday;
                mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).LoanCurrentDueAmountToday =
                        selectedCollectionMember.collectionMember.LoanCurrentDueAmountToday;
                mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).LoanPastDuePlusCurrentDueAmtToday =
                        selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtToday;
                mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).LoanPastDueAmountTillToday =
                        selectedCollectionMember.collectionMember.LoanPastDueAmountTillToday;
                //Verify if Member MD is within 3 months
                Calendar cal = Calendar.getInstance();
                int iThisYear = cal.get(Calendar.YEAR);
                int iThisMonth = cal.get(Calendar.MONTH) + 1;
                int iMDLimitYear = iThisYear;
                int iMDLimitMonth = iThisMonth + 2;
                if(iMDLimitMonth > 12) {
                    iMDLimitYear = iMDLimitYear + 1;
                    iMDLimitMonth = iMDLimitMonth - 12;
                }
                flagMDWithinLimit = false;
                String strMaturityDate = selectedCollectionMember.collectionMember.MaturityDate;
                strMaturityDate = strMaturityDate.substring(0, 10);
                String[] strSplit = strMaturityDate.split("-");
                int iMaturityYear = Integer.parseInt(strSplit[0]);
                if (mainActivity.OrgID == 1) {
                    iMaturityYear = Integer.parseInt(strSplit[2]);
                }
                int iMaturityMonth = Integer.parseInt(strSplit[1]);
                if(iMaturityYear < iMDLimitYear)
                    flagMDWithinLimit = true;
                else if((iMaturityYear == iMDLimitYear) && (iMaturityMonth <= iMDLimitMonth) )
                    flagMDWithinLimit = true;

                iInstallmentsTillMD = 0;
                if(iMaturityYear >= iThisYear) {
                    iInstallmentsTillMD = 12 * (iMaturityYear - iThisYear);
                    iInstallmentsTillMD = iInstallmentsTillMD + iMaturityMonth - iThisMonth;
                    if(iInstallmentsTillMD < 0)
                        iInstallmentsTillMD = 0;
                }
                if(flagMDWithinLimit) {
                    strPrePaymentCollect = getResources().getStringArray(R.array.PrePaymentCollect_Within3Months_array);
                    strPreClosureCollect = getResources().getStringArray(R.array.PreClosureCollect_Within3Months_array);
                    if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                        SQLite_getPrePaymentUpto3MonthsReasonMaster();
                    else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                        SQLite_getPreClosureUpto3MonthsReasonMaster();
                } else {
                    strPrePaymentCollect = getResources().getStringArray(R.array.PrePaymentCollect_Morethan3Months_array);
                    strPreClosureCollect = getResources().getStringArray(R.array.PreClosureCollect_Morethan3Months_array);
                    if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                        SQLite_getPrePaymentMorethan3MonthsReasonMaster();
                    else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                        SQLite_getPreClosureMorethan3MonthsReasonMaster();
                }

                ArrayAdapter<String> adapterPreClosureReasons = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                        R.layout.simple_spinner_item, PrePaymentPreClosureReasons);
                adapterPreClosureReasons.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                Spinner spPreClosureReason = (Spinner) rootView.findViewById(R.id.spinnerLoanPreClosureReason);
                spPreClosureReason.setAdapter(adapterPreClosureReasons);
                spPreClosureReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(strSelectedPaymentOption.equalsIgnoreCase("PrePayment"))
                            selectedCollectionMember.collectionMember.PreClosureReasonID = PrePaymentReasonIDs.get(position);
                        else if(strSelectedPaymentOption.equalsIgnoreCase("PreClosure"))
                            selectedCollectionMember.collectionMember.PreClosureReasonID = PreClosureReasonIDs.get(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                spPreClosureReason.setFocusable(true);
                spPreClosureReason.setFocusableInTouchMode(true);
                spPreClosureReason.setEnabled(false);
                Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerPaymentOptions);
                sp.setSelection(0);
                RefreshMemberDueDetails();
                RefreshMemberCollectionDetails(true);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spCollectionMemberInfo.setSelection(0);
        spCollectionMemberInfo.setFocusable(true);
        spCollectionMemberInfo.setFocusableInTouchMode(true);
    }
    void AllocateFLMFClaimMemberPayment() {
        if(selectedCollectionMember.collectionMember.TotalCollectionAmount == 0) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount = 0;
            selectedCollectionMember.collectionMember.JLGCollectionAmount = 0;
            selectedCollectionMember.collectionMember.NidhiCollectionAmount = 0;
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
            selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
            selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
            selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
            selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
            selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount
                <= iCollectionTarget0) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount = 0;
            if(selectedCollectionMember.collectionMember.JLGSelect) {
                selectedCollectionMember.collectionMember.JLGCollectionAmount
                        = selectedCollectionMember.collectionMember.TotalCollectionAmount;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
                selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
            } else {
                selectedCollectionMember.collectionMember.JLGCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount
                        = selectedCollectionMember.collectionMember.TotalCollectionAmount;
            }
            selectedCollectionMember.collectionMember.NidhiCollectionAmount = 0;
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount
                <= iCollectionTarget1) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount = 0;
            if(selectedCollectionMember.collectionMember.JLGSelect) {
                selectedCollectionMember.collectionMember.JLGCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_SD
                        + selectedCollectionMember.collectionMember.Expected_SD;;
            } else {
                selectedCollectionMember.collectionMember.JLGCollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.JLGCollectionAmount;
            if(selectedCollectionMember.collectionMember.NidhiSelect) {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount = iTemp;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
                selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
            } else {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp;
            }
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget2) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount = 0;
            if(selectedCollectionMember.collectionMember.JLGSelect) {
                selectedCollectionMember.collectionMember.JLGCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_SD
                        + selectedCollectionMember.collectionMember.Expected_SD;;
            } else {
                selectedCollectionMember.collectionMember.JLGCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.NidhiSelect) {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_PolicyRenewalAmt
                        + selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt;
            } else {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.JLGCollectionAmount
                    - selectedCollectionMember.collectionMember.NidhiCollectionAmount;
            if(selectedCollectionMember.collectionMember.ADHPSelect) {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount = iTemp;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
                selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
            } else {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp;
            }
            selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget3) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount = 0;
            if(selectedCollectionMember.collectionMember.JLGSelect) {
                selectedCollectionMember.collectionMember.JLGCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_SD
                        + selectedCollectionMember.collectionMember.Expected_SD;;
            } else {
                selectedCollectionMember.collectionMember.JLGCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.NidhiSelect) {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_PolicyRenewalAmt
                        + selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt;
            } else {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.ADHPSelect) {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                        = selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt;
            } else {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                        - selectedCollectionMember.collectionMember.JLGCollectionAmount
                        - selectedCollectionMember.collectionMember.NidhiCollectionAmount
                        - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount;
            if(selectedCollectionMember.collectionMember.RD1Select && (iTemp == selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount) ) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
                selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp;
            }
            selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget4) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount = 0;
            if(selectedCollectionMember.collectionMember.JLGSelect) {
                selectedCollectionMember.collectionMember.JLGCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_SD
                        + selectedCollectionMember.collectionMember.Expected_SD;;
            } else {
                selectedCollectionMember.collectionMember.JLGCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.NidhiSelect) {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_PolicyRenewalAmt
                        + selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt;
            } else {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.ADHPSelect) {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                        = selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt;
            } else {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.JLGCollectionAmount
                    - selectedCollectionMember.collectionMember.NidhiCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    - selectedCollectionMember.collectionMember.RD1CollectionAmount;
            if(selectedCollectionMember.collectionMember.RD2Select && (iTemp == selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount) ) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
                selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp;
            }
            selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget5) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount = 0;
            if(selectedCollectionMember.collectionMember.JLGSelect) {
                selectedCollectionMember.collectionMember.JLGCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_SD
                        + selectedCollectionMember.collectionMember.Expected_SD;;
            } else {
                selectedCollectionMember.collectionMember.JLGCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.NidhiSelect) {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_PolicyRenewalAmt
                        + selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt;
            } else {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.ADHPSelect) {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                        = selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt;
            } else {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.JLGCollectionAmount
                    - selectedCollectionMember.collectionMember.NidhiCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    - selectedCollectionMember.collectionMember.RD1CollectionAmount
                    - selectedCollectionMember.collectionMember.RD2CollectionAmount;
            if(selectedCollectionMember.collectionMember.RD3Select && (iTemp == selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount) ) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
                selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp;
            }
            selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget6) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount = 0;
            if(selectedCollectionMember.collectionMember.JLGSelect) {
                selectedCollectionMember.collectionMember.JLGCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_SD
                        + selectedCollectionMember.collectionMember.Expected_SD;;
            } else {
                selectedCollectionMember.collectionMember.JLGCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.NidhiSelect) {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_PolicyRenewalAmt
                        + selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt;
            } else {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.ADHPSelect) {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                        = selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt;
            } else {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.JLGCollectionAmount
                    - selectedCollectionMember.collectionMember.NidhiCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    - selectedCollectionMember.collectionMember.RD1CollectionAmount
                    - selectedCollectionMember.collectionMember.RD2CollectionAmount
                    - selectedCollectionMember.collectionMember.RD3CollectionAmount;
            if(selectedCollectionMember.collectionMember.RD4Select && (iTemp == selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount) ) {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
                selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp;
            }
            selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget7) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount = 0;
            if(selectedCollectionMember.collectionMember.JLGSelect) {
                selectedCollectionMember.collectionMember.JLGCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_SD
                        + selectedCollectionMember.collectionMember.Expected_SD;;
            } else {
                selectedCollectionMember.collectionMember.JLGCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.NidhiSelect) {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_PolicyRenewalAmt
                        + selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt;
            } else {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.ADHPSelect) {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                        = selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt;
            } else {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD4Select) {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.JLGCollectionAmount
                    - selectedCollectionMember.collectionMember.NidhiCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    - selectedCollectionMember.collectionMember.RD1CollectionAmount
                    - selectedCollectionMember.collectionMember.RD2CollectionAmount
                    - selectedCollectionMember.collectionMember.RD3CollectionAmount
                    - selectedCollectionMember.collectionMember.RD4CollectionAmount;
            if(selectedCollectionMember.collectionMember.RD5Select && (iTemp == selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount) ) {
                selectedCollectionMember.collectionMember.RD5CollectionAmount
                        = selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
                selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
            } else {
                selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp;
            }
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

//        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget15) {
//            selectedCollectionMember.collectionMember.TotalEMICollectionAmount
//                    = selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtToday;
//            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
//                    = selectedCollectionMember.collectionMember.ADHPContributionAmount;
//            if(selectedCollectionMember.collectionMember.RD1Select) {
//                selectedCollectionMember.collectionMember.RD1CollectionAmount
//                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
//            } else {
//                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
//            }
//            if(selectedCollectionMember.collectionMember.RD2Select) {
//                selectedCollectionMember.collectionMember.RD2CollectionAmount
//                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
//            } else {
//                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
//            }
//            if(selectedCollectionMember.collectionMember.RD3Select) {
//                selectedCollectionMember.collectionMember.RD3CollectionAmount
//                        = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
//            } else {
//                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
//            }
//            if(selectedCollectionMember.collectionMember.RD4Select) {
//                selectedCollectionMember.collectionMember.RD4CollectionAmount
//                        = selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
//            } else {
//                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
//            }
//            if(selectedCollectionMember.collectionMember.RD5Select) {
//                selectedCollectionMember.collectionMember.RD5CollectionAmount
//                        = selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount;
//            } else {
//                selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
//            }
//            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
//                    - selectedCollectionMember.collectionMember.TotalEMICollectionAmount
//                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
//                    - selectedCollectionMember.collectionMember.RD1CollectionAmount
//                    - selectedCollectionMember.collectionMember.RD2CollectionAmount
//                    - selectedCollectionMember.collectionMember.RD3CollectionAmount
//                    - selectedCollectionMember.collectionMember.RD4CollectionAmount
//                    - selectedCollectionMember.collectionMember.RD5CollectionAmount;
//            if(iTemp <= selectedCollectionMember.collectionMember.ADHPPastDueAmountTillToday) {
//                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
//                        = selectedCollectionMember.collectionMember.TotalADHPCollectionAmount + iTemp;
//                selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
//                selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
//                selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
//                selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
//                selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
//                selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
//            } else {
//                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
//                        = selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
//                        + selectedCollectionMember.collectionMember.ADHPPastDueAmountTillToday;
//                selectedCollectionMember.collectionMember.ExtraCollectionAmount
//                        = iTemp - selectedCollectionMember.collectionMember.ADHPPastDueAmountTillToday;
//            }
//            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
//            return;
//        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget16) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount = 0;
            if(selectedCollectionMember.collectionMember.JLGSelect) {
                selectedCollectionMember.collectionMember.JLGCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_SD
                        + selectedCollectionMember.collectionMember.Expected_SD;;
            } else {
                selectedCollectionMember.collectionMember.JLGCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.NidhiSelect) {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_PolicyRenewalAmt
                        + selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt;
            } else {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.ADHPSelect) {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                        = selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt;
            } else {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD4Select) {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD5Select) {
                selectedCollectionMember.collectionMember.RD5CollectionAmount
                        = selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.JLGCollectionAmount
                    - selectedCollectionMember.collectionMember.NidhiCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    - selectedCollectionMember.collectionMember.RD1CollectionAmount
                    - selectedCollectionMember.collectionMember.RD2CollectionAmount
                    - selectedCollectionMember.collectionMember.RD3CollectionAmount
                    - selectedCollectionMember.collectionMember.RD4CollectionAmount
                    - selectedCollectionMember.collectionMember.RD5CollectionAmount;
            if(selectedCollectionMember.collectionMember.RD1Select && (iTemp <= selectedCollectionMember.collectionMember.RD1PastDue) ) {
                int iTemp2 = 0;
                if(selectedCollectionMember.collectionMember.RD1MonthlyInstallmentAmt > 0) {
                    iTemp2 = iTemp / selectedCollectionMember.collectionMember.RD1MonthlyInstallmentAmt;
                    iTemp2 = iTemp2 * selectedCollectionMember.collectionMember.RD1MonthlyInstallmentAmt;
                }
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CollectionAmount + iTemp2;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp - iTemp2;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CollectionAmount
                        + selectedCollectionMember.collectionMember.RD1PastDue;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount
                        = iTemp - selectedCollectionMember.collectionMember.RD1PastDue;
            }
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget17) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount = 0;
            if(selectedCollectionMember.collectionMember.JLGSelect) {
                selectedCollectionMember.collectionMember.JLGCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_SD
                        + selectedCollectionMember.collectionMember.Expected_SD;;
            } else {
                selectedCollectionMember.collectionMember.JLGCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.NidhiSelect) {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_PolicyRenewalAmt
                        + selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt;
            } else {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.ADHPSelect) {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                        = selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt;
            } else {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD4Select) {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD5Select) {
                selectedCollectionMember.collectionMember.RD5CollectionAmount
                        = selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CollectionAmount
                        + selectedCollectionMember.collectionMember.RD1PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.JLGCollectionAmount
                    - selectedCollectionMember.collectionMember.NidhiCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    - selectedCollectionMember.collectionMember.RD1CollectionAmount
                    - selectedCollectionMember.collectionMember.RD2CollectionAmount
                    - selectedCollectionMember.collectionMember.RD3CollectionAmount
                    - selectedCollectionMember.collectionMember.RD4CollectionAmount
                    - selectedCollectionMember.collectionMember.RD5CollectionAmount;
            if(selectedCollectionMember.collectionMember.RD2Select && (iTemp <= selectedCollectionMember.collectionMember.RD2PastDue) ) {
                int iTemp2 = 0;
                if(selectedCollectionMember.collectionMember.RD2MonthlyInstallmentAmt > 0) {
                    iTemp2 = iTemp / selectedCollectionMember.collectionMember.RD2MonthlyInstallmentAmt;
                    iTemp2 = iTemp2 * selectedCollectionMember.collectionMember.RD2MonthlyInstallmentAmt;
                }
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CollectionAmount + iTemp2;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp - iTemp2;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CollectionAmount
                        + selectedCollectionMember.collectionMember.RD2PastDue;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount
                        = iTemp - selectedCollectionMember.collectionMember.RD2PastDue;
            }
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget18) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount = 0;
            if(selectedCollectionMember.collectionMember.JLGSelect) {
                selectedCollectionMember.collectionMember.JLGCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_SD
                        + selectedCollectionMember.collectionMember.Expected_SD;;
            } else {
                selectedCollectionMember.collectionMember.JLGCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.NidhiSelect) {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_PolicyRenewalAmt
                        + selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt;
            } else {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.ADHPSelect) {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                        = selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt;
            } else {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD4Select) {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD5Select) {
                selectedCollectionMember.collectionMember.RD5CollectionAmount
                        = selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CollectionAmount
                        + selectedCollectionMember.collectionMember.RD1PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CollectionAmount
                        + selectedCollectionMember.collectionMember.RD2PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.JLGCollectionAmount
                    - selectedCollectionMember.collectionMember.NidhiCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    - selectedCollectionMember.collectionMember.RD1CollectionAmount
                    - selectedCollectionMember.collectionMember.RD2CollectionAmount
                    - selectedCollectionMember.collectionMember.RD3CollectionAmount
                    - selectedCollectionMember.collectionMember.RD4CollectionAmount
                    - selectedCollectionMember.collectionMember.RD5CollectionAmount;
            if(selectedCollectionMember.collectionMember.RD3Select && (iTemp <= selectedCollectionMember.collectionMember.RD3PastDue) ) {
                int iTemp2 = 0;
                if(selectedCollectionMember.collectionMember.RD3MonthlyInstallmentAmt > 0) {
                    iTemp2 = iTemp / selectedCollectionMember.collectionMember.RD3MonthlyInstallmentAmt;
                    iTemp2 = iTemp2 * selectedCollectionMember.collectionMember.RD3MonthlyInstallmentAmt;
                }
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CollectionAmount + iTemp2;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp - iTemp2;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CollectionAmount
                        + selectedCollectionMember.collectionMember.RD3PastDue;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount
                        = iTemp - selectedCollectionMember.collectionMember.RD3PastDue;
            }
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget19) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount = 0;
            if(selectedCollectionMember.collectionMember.JLGSelect) {
                selectedCollectionMember.collectionMember.JLGCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_SD
                        + selectedCollectionMember.collectionMember.Expected_SD;;
            } else {
                selectedCollectionMember.collectionMember.JLGCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.NidhiSelect) {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_PolicyRenewalAmt
                        + selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt;
            } else {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.ADHPSelect) {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                        = selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt;
            } else {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD4Select) {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD5Select) {
                selectedCollectionMember.collectionMember.RD5CollectionAmount
                        = selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CollectionAmount
                        + selectedCollectionMember.collectionMember.RD1PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CollectionAmount
                        + selectedCollectionMember.collectionMember.RD2PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CollectionAmount
                        + selectedCollectionMember.collectionMember.RD3PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.JLGCollectionAmount
                    - selectedCollectionMember.collectionMember.NidhiCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    - selectedCollectionMember.collectionMember.RD1CollectionAmount
                    - selectedCollectionMember.collectionMember.RD2CollectionAmount
                    - selectedCollectionMember.collectionMember.RD3CollectionAmount
                    - selectedCollectionMember.collectionMember.RD4CollectionAmount
                    - selectedCollectionMember.collectionMember.RD5CollectionAmount;
            if(selectedCollectionMember.collectionMember.RD4Select && (iTemp <= selectedCollectionMember.collectionMember.RD4PastDue) ) {
                int iTemp2 = 0;
                if(selectedCollectionMember.collectionMember.RD4MonthlyInstallmentAmt > 0) {
                    iTemp2 = iTemp / selectedCollectionMember.collectionMember.RD4MonthlyInstallmentAmt;
                    iTemp2 = iTemp2 * selectedCollectionMember.collectionMember.RD4MonthlyInstallmentAmt;
                }
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CollectionAmount + iTemp2;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp - iTemp2;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CollectionAmount
                        + selectedCollectionMember.collectionMember.RD4PastDue;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount
                        = iTemp - selectedCollectionMember.collectionMember.RD4PastDue;
            }
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget20) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount = 0;
            if(selectedCollectionMember.collectionMember.JLGSelect) {
                selectedCollectionMember.collectionMember.JLGCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_SD
                        + selectedCollectionMember.collectionMember.Expected_SD;;
            } else {
                selectedCollectionMember.collectionMember.JLGCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.NidhiSelect) {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_PolicyRenewalAmt
                        + selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt;
            } else {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.ADHPSelect) {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                        = selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt;
            } else {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD4Select) {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD5Select) {
                selectedCollectionMember.collectionMember.RD5CollectionAmount
                        = selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CollectionAmount
                        + selectedCollectionMember.collectionMember.RD1PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CollectionAmount
                        + selectedCollectionMember.collectionMember.RD2PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CollectionAmount
                        + selectedCollectionMember.collectionMember.RD3PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD4Select) {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CollectionAmount
                        + selectedCollectionMember.collectionMember.RD4PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.JLGCollectionAmount
                    - selectedCollectionMember.collectionMember.NidhiCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    - selectedCollectionMember.collectionMember.RD1CollectionAmount
                    - selectedCollectionMember.collectionMember.RD2CollectionAmount
                    - selectedCollectionMember.collectionMember.RD3CollectionAmount
                    - selectedCollectionMember.collectionMember.RD4CollectionAmount
                    - selectedCollectionMember.collectionMember.RD5CollectionAmount;
            if(selectedCollectionMember.collectionMember.RD5Select && (iTemp <= selectedCollectionMember.collectionMember.RD5PastDue) ) {
                int iTemp2 = 0;
                if(selectedCollectionMember.collectionMember.RD5MonthlyInstallmentAmt > 0) {
                    iTemp2 = iTemp / selectedCollectionMember.collectionMember.RD5MonthlyInstallmentAmt;
                    iTemp2 = iTemp2 * selectedCollectionMember.collectionMember.RD5MonthlyInstallmentAmt;
                }
                selectedCollectionMember.collectionMember.RD5CollectionAmount
                        = selectedCollectionMember.collectionMember.RD5CollectionAmount + iTemp2;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp - iTemp2;
            } else {
                selectedCollectionMember.collectionMember.RD5CollectionAmount
                        = selectedCollectionMember.collectionMember.RD5CollectionAmount
                        + selectedCollectionMember.collectionMember.RD5PastDue;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount
                        = iTemp - selectedCollectionMember.collectionMember.RD5PastDue;
            }
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget28) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount = 0;
            if(selectedCollectionMember.collectionMember.JLGSelect) {
                selectedCollectionMember.collectionMember.JLGCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_SD
                        + selectedCollectionMember.collectionMember.Expected_SD;;
            } else {
                selectedCollectionMember.collectionMember.JLGCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.NidhiSelect) {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount
                        = selectedCollectionMember.collectionMember.PastDue_PolicyRenewalAmt
                        + selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt;
            } else {
                selectedCollectionMember.collectionMember.NidhiCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.ADHPSelect) {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                        = selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt;
            } else {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD4Select) {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD5Select) {
                selectedCollectionMember.collectionMember.RD5CollectionAmount
                        = selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CollectionAmount
                        + selectedCollectionMember.collectionMember.RD1PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CollectionAmount
                        + selectedCollectionMember.collectionMember.RD2PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CollectionAmount
                        + selectedCollectionMember.collectionMember.RD3PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD4Select) {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CollectionAmount
                        + selectedCollectionMember.collectionMember.RD4PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD5Select) {
                selectedCollectionMember.collectionMember.RD5CollectionAmount
                        = selectedCollectionMember.collectionMember.RD5CollectionAmount
                        + selectedCollectionMember.collectionMember.RD5PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.JLGCollectionAmount
                    - selectedCollectionMember.collectionMember.NidhiCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    - selectedCollectionMember.collectionMember.RD1CollectionAmount
                    - selectedCollectionMember.collectionMember.RD2CollectionAmount
                    - selectedCollectionMember.collectionMember.RD3CollectionAmount
                    - selectedCollectionMember.collectionMember.RD4CollectionAmount
                    - selectedCollectionMember.collectionMember.RD5CollectionAmount;
            if(selectedCollectionMember.collectionMember.AdditionalRepaySelect && (iTemp <= selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount) ) {
                selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = iTemp;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
            } else {
                selectedCollectionMember.collectionMember.ExtraCollectionAmount
                        = iTemp - selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount;
            }
            return;
        }

        selectedCollectionMember.collectionMember.TotalEMICollectionAmount = 0;
        if(selectedCollectionMember.collectionMember.JLGSelect) {
            selectedCollectionMember.collectionMember.JLGCollectionAmount
                    = selectedCollectionMember.collectionMember.PastDue_SD
                    + selectedCollectionMember.collectionMember.Expected_SD;;
        } else {
            selectedCollectionMember.collectionMember.JLGCollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.NidhiSelect) {
            selectedCollectionMember.collectionMember.NidhiCollectionAmount
                    = selectedCollectionMember.collectionMember.PastDue_PolicyRenewalAmt
                    + selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt;
        } else {
            selectedCollectionMember.collectionMember.NidhiCollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.ADHPSelect) {
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    = selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt;
        } else {
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.RD1Select) {
            selectedCollectionMember.collectionMember.RD1CollectionAmount
                    = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
        } else {
            selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.RD2Select) {
            selectedCollectionMember.collectionMember.RD2CollectionAmount
                    = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
        } else {
            selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.RD3Select) {
            selectedCollectionMember.collectionMember.RD3CollectionAmount
                    = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
        } else {
            selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.RD4Select) {
            selectedCollectionMember.collectionMember.RD4CollectionAmount
                    = selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
        } else {
            selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.RD5Select) {
            selectedCollectionMember.collectionMember.RD5CollectionAmount
                    = selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount;
        } else {
            selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.RD1Select) {
            selectedCollectionMember.collectionMember.RD1CollectionAmount
                    = selectedCollectionMember.collectionMember.RD1CollectionAmount
                    + selectedCollectionMember.collectionMember.RD1PastDue;
        } else {
            selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.RD2Select) {
            selectedCollectionMember.collectionMember.RD2CollectionAmount
                    = selectedCollectionMember.collectionMember.RD2CollectionAmount
                    + selectedCollectionMember.collectionMember.RD2PastDue;
        } else {
            selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.RD3Select) {
            selectedCollectionMember.collectionMember.RD3CollectionAmount
                    = selectedCollectionMember.collectionMember.RD3CollectionAmount
                    + selectedCollectionMember.collectionMember.RD3PastDue;
        } else {
            selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.RD4Select) {
            selectedCollectionMember.collectionMember.RD4CollectionAmount
                    = selectedCollectionMember.collectionMember.RD4CollectionAmount
                    + selectedCollectionMember.collectionMember.RD4PastDue;
        } else {
            selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.RD5Select) {
            selectedCollectionMember.collectionMember.RD5CollectionAmount
                    = selectedCollectionMember.collectionMember.RD5CollectionAmount
                    + selectedCollectionMember.collectionMember.RD5PastDue;
        } else {
            selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.AdditionalRepaySelect) {
        } else {
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
        }
        int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                - selectedCollectionMember.collectionMember.JLGCollectionAmount
                - selectedCollectionMember.collectionMember.NidhiCollectionAmount
                - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                - selectedCollectionMember.collectionMember.RD1CollectionAmount
                - selectedCollectionMember.collectionMember.RD2CollectionAmount
                - selectedCollectionMember.collectionMember.RD3CollectionAmount
                - selectedCollectionMember.collectionMember.RD4CollectionAmount
                - selectedCollectionMember.collectionMember.RD5CollectionAmount
                - selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount;

        selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp;

        return;
    }
    void AllocateMemberPayment() {
        if(selectedCollectionMember.collectionMember.TotalCollectionAmount == 0) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount = 0;
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
            selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
            selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
            selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
            selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
            selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount
                <= iCollectionTarget1) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    = selectedCollectionMember.collectionMember.TotalCollectionAmount;

            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
            selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
            selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
            selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
            selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
            selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget2) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    = selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtToday;
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalEMICollectionAmount;
            selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
            selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
            selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
            selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
            selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
            selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget3) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    = selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtToday;
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    = selectedCollectionMember.collectionMember.ADHPContributionAmount;
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                        - selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                        - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount;
            if(selectedCollectionMember.collectionMember.RD1Select && (iTemp == selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount) ) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
                selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp;
            }
            selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget4) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    = selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtToday;
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    = selectedCollectionMember.collectionMember.ADHPContributionAmount;
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    - selectedCollectionMember.collectionMember.RD1CollectionAmount;
            if(selectedCollectionMember.collectionMember.RD2Select && (iTemp == selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount) ) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
                selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp;
            }
            selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget5) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    = selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtToday;
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    = selectedCollectionMember.collectionMember.ADHPContributionAmount;
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    - selectedCollectionMember.collectionMember.RD1CollectionAmount
                    - selectedCollectionMember.collectionMember.RD2CollectionAmount;
            if(selectedCollectionMember.collectionMember.RD3Select && (iTemp == selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount) ) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
                selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp;
            }
            selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget6) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    = selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtToday;
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    = selectedCollectionMember.collectionMember.ADHPContributionAmount;
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    - selectedCollectionMember.collectionMember.RD1CollectionAmount
                    - selectedCollectionMember.collectionMember.RD2CollectionAmount
                    - selectedCollectionMember.collectionMember.RD3CollectionAmount;
            if(selectedCollectionMember.collectionMember.RD4Select && (iTemp == selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount) ) {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
                selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp;
            }
            selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget7) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    = selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtToday;
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    = selectedCollectionMember.collectionMember.ADHPContributionAmount;
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD4Select) {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    - selectedCollectionMember.collectionMember.RD1CollectionAmount
                    - selectedCollectionMember.collectionMember.RD2CollectionAmount
                    - selectedCollectionMember.collectionMember.RD3CollectionAmount
                    - selectedCollectionMember.collectionMember.RD4CollectionAmount;
            if(selectedCollectionMember.collectionMember.RD5Select && (iTemp == selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount) ) {
                selectedCollectionMember.collectionMember.RD5CollectionAmount
                        = selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
                selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
            } else {
                selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp;
            }
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget15) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    = selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtToday;
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    = selectedCollectionMember.collectionMember.ADHPContributionAmount;
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD4Select) {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD5Select) {
                selectedCollectionMember.collectionMember.RD5CollectionAmount
                        = selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    - selectedCollectionMember.collectionMember.RD1CollectionAmount
                    - selectedCollectionMember.collectionMember.RD2CollectionAmount
                    - selectedCollectionMember.collectionMember.RD3CollectionAmount
                    - selectedCollectionMember.collectionMember.RD4CollectionAmount
                    - selectedCollectionMember.collectionMember.RD5CollectionAmount;
            if(iTemp <= selectedCollectionMember.collectionMember.ADHPPastDueAmountTillToday) {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                        = selectedCollectionMember.collectionMember.TotalADHPCollectionAmount + iTemp;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
                selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
                selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
            } else {
                selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                        = selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                            + selectedCollectionMember.collectionMember.ADHPPastDueAmountTillToday;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount
                        = iTemp - selectedCollectionMember.collectionMember.ADHPPastDueAmountTillToday;
            }
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget16) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    = selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtToday;
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    = selectedCollectionMember.collectionMember.ADHPContributionAmount;
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD4Select) {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD5Select) {
                selectedCollectionMember.collectionMember.RD5CollectionAmount
                        = selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            }
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    = selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    + selectedCollectionMember.collectionMember.ADHPPastDueAmountTillToday;
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    - selectedCollectionMember.collectionMember.RD1CollectionAmount
                    - selectedCollectionMember.collectionMember.RD2CollectionAmount
                    - selectedCollectionMember.collectionMember.RD3CollectionAmount
                    - selectedCollectionMember.collectionMember.RD4CollectionAmount
                    - selectedCollectionMember.collectionMember.RD5CollectionAmount;
            if(selectedCollectionMember.collectionMember.RD1Select && (iTemp <= selectedCollectionMember.collectionMember.RD1PastDue) ) {
                int iTemp2 = 0;
                if(selectedCollectionMember.collectionMember.RD1MonthlyInstallmentAmt > 0) {
                    iTemp2 = iTemp / selectedCollectionMember.collectionMember.RD1MonthlyInstallmentAmt;
                    iTemp2 = iTemp2 * selectedCollectionMember.collectionMember.RD1MonthlyInstallmentAmt;
                }
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CollectionAmount + iTemp2;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp - iTemp2;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CollectionAmount
                        + selectedCollectionMember.collectionMember.RD1PastDue;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount
                        = iTemp - selectedCollectionMember.collectionMember.RD1PastDue;
            }
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget17) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    = selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtToday;
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    = selectedCollectionMember.collectionMember.ADHPContributionAmount;
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD4Select) {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD5Select) {
                selectedCollectionMember.collectionMember.RD5CollectionAmount
                        = selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            }
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    = selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    + selectedCollectionMember.collectionMember.ADHPPastDueAmountTillToday;
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CollectionAmount
                        + selectedCollectionMember.collectionMember.RD1PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    - selectedCollectionMember.collectionMember.RD1CollectionAmount
                    - selectedCollectionMember.collectionMember.RD2CollectionAmount
                    - selectedCollectionMember.collectionMember.RD3CollectionAmount
                    - selectedCollectionMember.collectionMember.RD4CollectionAmount
                    - selectedCollectionMember.collectionMember.RD5CollectionAmount;
            if(selectedCollectionMember.collectionMember.RD2Select && (iTemp <= selectedCollectionMember.collectionMember.RD2PastDue) ) {
                int iTemp2 = 0;
                if(selectedCollectionMember.collectionMember.RD2MonthlyInstallmentAmt > 0) {
                    iTemp2 = iTemp / selectedCollectionMember.collectionMember.RD2MonthlyInstallmentAmt;
                    iTemp2 = iTemp2 * selectedCollectionMember.collectionMember.RD2MonthlyInstallmentAmt;
                }
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CollectionAmount + iTemp2;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp - iTemp2;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CollectionAmount
                        + selectedCollectionMember.collectionMember.RD2PastDue;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount
                        = iTemp - selectedCollectionMember.collectionMember.RD2PastDue;
            }
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget18) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    = selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtToday;
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    = selectedCollectionMember.collectionMember.ADHPContributionAmount;
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD4Select) {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD5Select) {
                selectedCollectionMember.collectionMember.RD5CollectionAmount
                        = selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            }
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    = selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    + selectedCollectionMember.collectionMember.ADHPPastDueAmountTillToday;
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CollectionAmount
                        + selectedCollectionMember.collectionMember.RD1PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CollectionAmount
                        + selectedCollectionMember.collectionMember.RD2PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    - selectedCollectionMember.collectionMember.RD1CollectionAmount
                    - selectedCollectionMember.collectionMember.RD2CollectionAmount
                    - selectedCollectionMember.collectionMember.RD3CollectionAmount
                    - selectedCollectionMember.collectionMember.RD4CollectionAmount
                    - selectedCollectionMember.collectionMember.RD5CollectionAmount;
            if(selectedCollectionMember.collectionMember.RD3Select && (iTemp <= selectedCollectionMember.collectionMember.RD3PastDue) ) {
                int iTemp2 = 0;
                if(selectedCollectionMember.collectionMember.RD3MonthlyInstallmentAmt > 0) {
                    iTemp2 = iTemp / selectedCollectionMember.collectionMember.RD3MonthlyInstallmentAmt;
                    iTemp2 = iTemp2 * selectedCollectionMember.collectionMember.RD3MonthlyInstallmentAmt;
                }
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CollectionAmount + iTemp2;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp - iTemp2;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CollectionAmount
                        + selectedCollectionMember.collectionMember.RD3PastDue;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount
                        = iTemp - selectedCollectionMember.collectionMember.RD3PastDue;
            }
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget19) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    = selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtToday;
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    = selectedCollectionMember.collectionMember.ADHPContributionAmount;
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD4Select) {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD5Select) {
                selectedCollectionMember.collectionMember.RD5CollectionAmount
                        = selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            }
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    = selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    + selectedCollectionMember.collectionMember.ADHPPastDueAmountTillToday;
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CollectionAmount
                        + selectedCollectionMember.collectionMember.RD1PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CollectionAmount
                        + selectedCollectionMember.collectionMember.RD2PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CollectionAmount
                        + selectedCollectionMember.collectionMember.RD3PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    - selectedCollectionMember.collectionMember.RD1CollectionAmount
                    - selectedCollectionMember.collectionMember.RD2CollectionAmount
                    - selectedCollectionMember.collectionMember.RD3CollectionAmount
                    - selectedCollectionMember.collectionMember.RD4CollectionAmount
                    - selectedCollectionMember.collectionMember.RD5CollectionAmount;
            if(selectedCollectionMember.collectionMember.RD4Select && (iTemp <= selectedCollectionMember.collectionMember.RD4PastDue) ) {
                int iTemp2 = 0;
                if(selectedCollectionMember.collectionMember.RD4MonthlyInstallmentAmt > 0) {
                    iTemp2 = iTemp / selectedCollectionMember.collectionMember.RD4MonthlyInstallmentAmt;
                    iTemp2 = iTemp2 * selectedCollectionMember.collectionMember.RD4MonthlyInstallmentAmt;
                }
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CollectionAmount + iTemp2;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp - iTemp2;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CollectionAmount
                        + selectedCollectionMember.collectionMember.RD4PastDue;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount
                        = iTemp - selectedCollectionMember.collectionMember.RD4PastDue;
            }
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget20) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    = selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtToday;
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    = selectedCollectionMember.collectionMember.ADHPContributionAmount;
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD4Select) {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD5Select) {
                selectedCollectionMember.collectionMember.RD5CollectionAmount
                        = selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            }
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    = selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    + selectedCollectionMember.collectionMember.ADHPPastDueAmountTillToday;
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CollectionAmount
                        + selectedCollectionMember.collectionMember.RD1PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CollectionAmount
                        + selectedCollectionMember.collectionMember.RD2PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CollectionAmount
                        + selectedCollectionMember.collectionMember.RD3PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD4Select) {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CollectionAmount
                        + selectedCollectionMember.collectionMember.RD4PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    - selectedCollectionMember.collectionMember.RD1CollectionAmount
                    - selectedCollectionMember.collectionMember.RD2CollectionAmount
                    - selectedCollectionMember.collectionMember.RD3CollectionAmount
                    - selectedCollectionMember.collectionMember.RD4CollectionAmount
                    - selectedCollectionMember.collectionMember.RD5CollectionAmount;
            if(selectedCollectionMember.collectionMember.RD5Select && (iTemp <= selectedCollectionMember.collectionMember.RD5PastDue) ) {
                int iTemp2 = 0;
                if(selectedCollectionMember.collectionMember.RD5MonthlyInstallmentAmt > 0) {
                    iTemp2 = iTemp / selectedCollectionMember.collectionMember.RD5MonthlyInstallmentAmt;
                    iTemp2 = iTemp2 * selectedCollectionMember.collectionMember.RD5MonthlyInstallmentAmt;
                }
                selectedCollectionMember.collectionMember.RD5CollectionAmount
                        = selectedCollectionMember.collectionMember.RD5CollectionAmount + iTemp2;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp - iTemp2;
            } else {
                selectedCollectionMember.collectionMember.RD5CollectionAmount
                        = selectedCollectionMember.collectionMember.RD5CollectionAmount
                        + selectedCollectionMember.collectionMember.RD5PastDue;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount
                        = iTemp - selectedCollectionMember.collectionMember.RD5PastDue;
            }
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            return;
        }

        if(selectedCollectionMember.collectionMember.TotalCollectionAmount <= iCollectionTarget28) {
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    = selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtToday;
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    = selectedCollectionMember.collectionMember.ADHPContributionAmount;
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD4Select) {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD5Select) {
                selectedCollectionMember.collectionMember.RD5CollectionAmount
                        = selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount;
            } else {
                selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            }
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    = selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    + selectedCollectionMember.collectionMember.ADHPPastDueAmountTillToday;
            if(selectedCollectionMember.collectionMember.RD1Select) {
                selectedCollectionMember.collectionMember.RD1CollectionAmount
                        = selectedCollectionMember.collectionMember.RD1CollectionAmount
                        + selectedCollectionMember.collectionMember.RD1PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD2Select) {
                selectedCollectionMember.collectionMember.RD2CollectionAmount
                        = selectedCollectionMember.collectionMember.RD2CollectionAmount
                        + selectedCollectionMember.collectionMember.RD2PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD3Select) {
                selectedCollectionMember.collectionMember.RD3CollectionAmount
                        = selectedCollectionMember.collectionMember.RD3CollectionAmount
                        + selectedCollectionMember.collectionMember.RD3PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD4Select) {
                selectedCollectionMember.collectionMember.RD4CollectionAmount
                        = selectedCollectionMember.collectionMember.RD4CollectionAmount
                        + selectedCollectionMember.collectionMember.RD4PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            }
            if(selectedCollectionMember.collectionMember.RD5Select) {
                selectedCollectionMember.collectionMember.RD5CollectionAmount
                        = selectedCollectionMember.collectionMember.RD5CollectionAmount
                        + selectedCollectionMember.collectionMember.RD5PastDue;
            } else {
                selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            }
            int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                    - selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                    - selectedCollectionMember.collectionMember.RD1CollectionAmount
                    - selectedCollectionMember.collectionMember.RD2CollectionAmount
                    - selectedCollectionMember.collectionMember.RD3CollectionAmount
                    - selectedCollectionMember.collectionMember.RD4CollectionAmount
                    - selectedCollectionMember.collectionMember.RD5CollectionAmount;
            if(selectedCollectionMember.collectionMember.AdditionalRepaySelect && (iTemp <= selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount) ) {
                selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = iTemp;
                selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
            } else {
                selectedCollectionMember.collectionMember.ExtraCollectionAmount
                        = iTemp - selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount;
            }
            return;
        }

        selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                = selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtToday;
        selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                = selectedCollectionMember.collectionMember.ADHPContributionAmount;
        if(selectedCollectionMember.collectionMember.RD1Select) {
            selectedCollectionMember.collectionMember.RD1CollectionAmount
                    = selectedCollectionMember.collectionMember.RD1CurrentInstallmentAmount;
        } else {
            selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.RD2Select) {
            selectedCollectionMember.collectionMember.RD2CollectionAmount
                    = selectedCollectionMember.collectionMember.RD2CurrentInstallmentAmount;
        } else {
            selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.RD3Select) {
            selectedCollectionMember.collectionMember.RD3CollectionAmount
                    = selectedCollectionMember.collectionMember.RD3CurrentInstallmentAmount;
        } else {
            selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.RD4Select) {
            selectedCollectionMember.collectionMember.RD4CollectionAmount
                    = selectedCollectionMember.collectionMember.RD4CurrentInstallmentAmount;
        } else {
            selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.RD5Select) {
            selectedCollectionMember.collectionMember.RD5CollectionAmount
                    = selectedCollectionMember.collectionMember.RD5CurrentInstallmentAmount;
        } else {
            selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
        }
        selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                = selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                + selectedCollectionMember.collectionMember.ADHPPastDueAmountTillToday;
        if(selectedCollectionMember.collectionMember.RD1Select) {
            selectedCollectionMember.collectionMember.RD1CollectionAmount
                    = selectedCollectionMember.collectionMember.RD1CollectionAmount
                    + selectedCollectionMember.collectionMember.RD1PastDue;
        } else {
            selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.RD2Select) {
            selectedCollectionMember.collectionMember.RD2CollectionAmount
                    = selectedCollectionMember.collectionMember.RD2CollectionAmount
                    + selectedCollectionMember.collectionMember.RD2PastDue;
        } else {
            selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.RD3Select) {
            selectedCollectionMember.collectionMember.RD3CollectionAmount
                    = selectedCollectionMember.collectionMember.RD3CollectionAmount
                    + selectedCollectionMember.collectionMember.RD3PastDue;
        } else {
            selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.RD4Select) {
            selectedCollectionMember.collectionMember.RD4CollectionAmount
                    = selectedCollectionMember.collectionMember.RD4CollectionAmount
                    + selectedCollectionMember.collectionMember.RD4PastDue;
        } else {
            selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.RD5Select) {
            selectedCollectionMember.collectionMember.RD5CollectionAmount
                    = selectedCollectionMember.collectionMember.RD5CollectionAmount
                    + selectedCollectionMember.collectionMember.RD5PastDue;
        } else {
            selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
        }
        if(selectedCollectionMember.collectionMember.AdditionalRepaySelect) {
        } else {
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
        }
        int iTemp = selectedCollectionMember.collectionMember.TotalCollectionAmount
                - selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                - selectedCollectionMember.collectionMember.TotalADHPCollectionAmount
                - selectedCollectionMember.collectionMember.RD1CollectionAmount
                - selectedCollectionMember.collectionMember.RD2CollectionAmount
                - selectedCollectionMember.collectionMember.RD3CollectionAmount
                - selectedCollectionMember.collectionMember.RD4CollectionAmount
                - selectedCollectionMember.collectionMember.RD5CollectionAmount
                - selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount;

        selectedCollectionMember.collectionMember.ExtraCollectionAmount = iTemp;

        return;
    }
    void RefreshMemberCollectionDetails(boolean flagCalculateAdditionalRepayCollectionAmount) {
        if (!strSelectedPaymentOption.equalsIgnoreCase("Monthly")) {
            if (flagCalculateAdditionalRepayCollectionAmount) {
                int iTemp1 = selectedCollectionMember.collectionMember.PreClosureADHP
                        - selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt;
                if (iTemp1 < 0)
                    iTemp1 = 0;

                int iTempRD = 0;
                if(!selectedCollectionMember.collectionMember.RD1Select) {
                    iTempRD = iTempRD + selectedCollectionMember.collectionMember.RD1ExpectedAmount;
                }
                if(!selectedCollectionMember.collectionMember.RD2Select) {
                    iTempRD = iTempRD + selectedCollectionMember.collectionMember.RD2ExpectedAmount;
                }
                if(!selectedCollectionMember.collectionMember.RD3Select) {
                    iTempRD = iTempRD + selectedCollectionMember.collectionMember.RD3ExpectedAmount;
                }
                if(!selectedCollectionMember.collectionMember.RD4Select) {
                    iTempRD = iTempRD + selectedCollectionMember.collectionMember.RD4ExpectedAmount;
                }
                if(!selectedCollectionMember.collectionMember.RD5Select) {
                    iTempRD = iTempRD + selectedCollectionMember.collectionMember.RD5ExpectedAmount;
                }
                int iTemp2 = selectedCollectionMember.collectionMember.TotalCollectionAmount
                        - selectedCollectionMember.collectionMember.TotalDueAmountsToPayToday
                        - iTemp1
                        + iTempRD;
                if (iTemp2 > 0) {
                    selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = iTemp2;
                    selectedCollectionMember.collectionMember.AdditionalRepaySelect = true;
                }

                if (iTemp1 > 0) {
                    String selectedPreCollect = "";
                    if (strSelectedPaymentOption.equalsIgnoreCase("PrePayment")) {
                        for (int i = 0; i < PrePaymentReasonIDs.size(); i++) {
                            if (PrePaymentReasonIDs.get(i) == selectedCollectionMember.collectionMember.PreClosureReasonID) {
                                selectedPreCollect = strPrePaymentCollect[i];
                                break;
                            }
                        }
                    } else if (strSelectedPaymentOption.equalsIgnoreCase("PreClosure")) {
                        for (int i = 0; i < PreClosureReasonIDs.size(); i++) {
                            if (PreClosureReasonIDs.get(i) == selectedCollectionMember.collectionMember.PreClosureReasonID) {
                                selectedPreCollect = strPreClosureCollect[i];
                                break;
                            }
                        }
                    }

                    selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = iTemp1;
                    selectedCollectionMember.collectionMember.ExtraADHPSelect = true;
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectExtraADHP);
                    EditText et = (EditText) rootView.findViewById(R.id.editTextExtraADHP);
                    if(selectedPreCollect.contains("OADHP")) {
                        cb.setEnabled(true);
                        et.setEnabled(true);
                    } else if(selectedPreCollect.contains("ADHP")) {
                        cb.setEnabled(false);
                        et.setEnabled(false);
                    }
                }
            }
        }
//bmp 05-Aug-26
//        AllocateMemberPayment();
        if(selectedCollectionMember.collectionMember.Internal_Transfer_Reason.equalsIgnoreCase("FLMFType1")) {
            AllocateFLMFClaimMemberPayment();
//bmp 12-Aug-26
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount
                    = selectedCollectionMember.collectionMember.JLGCollectionAmount
                    + selectedCollectionMember.collectionMember.NidhiCollectionAmount;
//bmp 12-Aug-26
        } else {
            AllocateMemberPayment();
        }
//bmp 05-Aug-26

        if(selectedCollectionMember.collectionMember.ExtraCollectionAmount < 0) {
            selectedCollectionMember.collectionMember.TotalCollectionAmount = 0;
            CheckBox cbCash = (CheckBox) rootView.findViewById(R.id.checkBoxPaymentCash);
            cbCash.setChecked(false);
            CheckBox cbOnline = (CheckBox) rootView.findViewById(R.id.checkBoxPaymentOnline);
            cbOnline.setChecked(false);
            EditText et = (EditText) rootView.findViewById(R.id.editTextTotalCollection);
            et.setText("");
            selectedCollectionMember.collectionMember.TotalEMICollectionAmount = 0;
            selectedCollectionMember.collectionMember.TotalADHPCollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD1CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD2CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD3CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD4CollectionAmount = 0;
            selectedCollectionMember.collectionMember.RD5CollectionAmount = 0;
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
            selectedCollectionMember.collectionMember.ExtraCollectionAmount = 0;
            selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount = 0;
            selectedCollectionMember.collectionMember.NewRDCollectionAmount = 0;
            selectedCollectionMember.collectionMember.FDCollectionAmount = 0;
            selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount = 0;
            selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount = 0;
        } else if(selectedCollectionMember.collectionMember.ExtraCollectionAmount > 0) {
        }

//bmp 06-Aug-26
        if(selectedCollectionMember.collectionMember.Internal_Transfer_Reason.equalsIgnoreCase("FLMFType1")) {
            TextView tv = (TextView) rootView.findViewById(R.id.editTextLoanEMICollection);
//bmp 12-Aug-26
//            tv.setText(String.valueOf(selectedCollectionMember.collectionMember.TotalEMICollectionAmount));
            tv.setText("0");
//bmp 12-Aug-26
            tv = (TextView) rootView.findViewById(R.id.editTextJLGCollection);
            tv.setText(String.valueOf(selectedCollectionMember.collectionMember.JLGCollectionAmount));
            tv = (TextView) rootView.findViewById(R.id.editTextNidhiCollection);
            tv.setText(String.valueOf(selectedCollectionMember.collectionMember.NidhiCollectionAmount));
        } else {
            TextView tv = (TextView) rootView.findViewById(R.id.editTextLoanEMICollection);
    //bmp 26-Jun-26
            tv.setText(String.valueOf(selectedCollectionMember.collectionMember.TotalEMICollectionAmount));
            int iPplusIPast = selectedCollectionMember.collectionMember.LoanPastDueAmountTillToday
                    - selectedCollectionMember.collectionMember.PastDue_SD
                    - selectedCollectionMember.collectionMember.PastDue_PolicyRenewalAmt;
            int iPplusICurrent = selectedCollectionMember.collectionMember.LoanCurrentDueAmountToday
                    - selectedCollectionMember.collectionMember.Expected_SD
                    - selectedCollectionMember.collectionMember.Expected_PolicyRenewalAmt;
            int iPplusIPastplusCurrent = iPplusIPast + iPplusICurrent;
            if(selectedCollectionMember.collectionMember.TotalEMICollectionAmount <= iPplusIPastplusCurrent) {
                tv.setText(String.valueOf(selectedCollectionMember.collectionMember.TotalEMICollectionAmount));
                tv = (TextView) rootView.findViewById(R.id.editTextJLGCollection);
                tv.setText("0");
                tv = (TextView) rootView.findViewById(R.id.editTextNidhiCollection);
                tv.setText("0");
            } else {
                tv.setText(String.valueOf(iPplusIPastplusCurrent));
                int iTotalJLGplusNidhiCollectionAmount =
                        selectedCollectionMember.collectionMember.TotalEMICollectionAmount - iPplusIPastplusCurrent;
                int iJLGPastplusCurrent = selectedCollectionMember.collectionMember.PastDue_SD
                        + selectedCollectionMember.collectionMember.Expected_SD;
                if(iTotalJLGplusNidhiCollectionAmount <= iJLGPastplusCurrent) {
                    tv = (TextView) rootView.findViewById(R.id.editTextJLGCollection);
                    tv.setText(String.valueOf(iTotalJLGplusNidhiCollectionAmount));
                    tv = (TextView) rootView.findViewById(R.id.editTextNidhiCollection);
                    tv.setText("0");
                } else {
                    tv = (TextView) rootView.findViewById(R.id.editTextJLGCollection);
                    tv.setText(String.valueOf(iJLGPastplusCurrent));
                    int iTotalNidhiCollectionAmount = iTotalJLGplusNidhiCollectionAmount - iJLGPastplusCurrent;
                    tv = (TextView) rootView.findViewById(R.id.editTextNidhiCollection);
                    tv.setText(String.valueOf(iTotalNidhiCollectionAmount));
                }
            }
    //bmp 26-Jun-26
        }
//bmp 06-Aug-26

        TextView tv = (TextView) rootView.findViewById(R.id.editTextADHPCollection);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.TotalADHPCollectionAmount));

        tv = (TextView) rootView.findViewById(R.id.editTextRDCollection1);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.RD1CollectionAmount));
        tv = (TextView) rootView.findViewById(R.id.editTextRDCollection2);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.RD2CollectionAmount));
        tv = (TextView) rootView.findViewById(R.id.editTextRDCollection3);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.RD3CollectionAmount));
        tv = (TextView) rootView.findViewById(R.id.editTextRDCollection4);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.RD4CollectionAmount));
        tv = (TextView) rootView.findViewById(R.id.editTextRDCollection5);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.RD5CollectionAmount));
        tv = (TextView) rootView.findViewById(R.id.textViewExtraCollection);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.ExtraCollectionAmount));
        tv.setTextColor(getResources().getColor(R.color.black));
        if(selectedCollectionMember.collectionMember.ExtraCollectionAmount > 0) {
            tv.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        tv = (TextView) rootView.findViewById(R.id.editTextExtraADHP);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount));
        tv.setTextColor(getResources().getColor(R.color.black));
        if(selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount > 0) {
            tv.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            tv.setText("");
        }
        tv = (TextView) rootView.findViewById(R.id.editTextNewRD);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.NewRDCollectionAmount));
        tv.setTextColor(getResources().getColor(R.color.black));
        if(selectedCollectionMember.collectionMember.NewRDCollectionAmount > 0) {
            tv.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            tv.setText("");
        }
        tv = (TextView) rootView.findViewById(R.id.editTextFDCollection);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.FDCollectionAmount));
        tv.setTextColor(getResources().getColor(R.color.black));
        if(selectedCollectionMember.collectionMember.FDCollectionAmount > 0) {
            tv.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            tv.setText("");
        }
        tv = (TextView) rootView.findViewById(R.id.editTextExtraJLG);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount));
        tv.setTextColor(getResources().getColor(R.color.black));
        if(selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount > 0) {
            tv.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            tv.setText("");
        }
        tv = (TextView) rootView.findViewById(R.id.editTextReprintCharges);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount));
        tv.setTextColor(getResources().getColor(R.color.black));
        if(selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount > 0) {
            tv.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            tv.setText("");
        }

        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD1);
        if (selectedCollectionMember.collectionMember.RD1CollectionAmount > 0) {
            cb.setChecked(true);
            selectedCollectionMember.collectionMember.RD1Select = true;
        } else {
            cb.setChecked(false);
            selectedCollectionMember.collectionMember.RD1Select = false;
        }
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD2);
        if(selectedCollectionMember.collectionMember.RD2CollectionAmount > 0) {
            cb.setChecked(true);
            selectedCollectionMember.collectionMember.RD2Select = true;
        } else {
            cb.setChecked(false);
            selectedCollectionMember.collectionMember.RD2Select = false;
        }
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD3);
        if(selectedCollectionMember.collectionMember.RD3CollectionAmount > 0) {
            cb.setChecked(true);
            selectedCollectionMember.collectionMember.RD3Select = true;
        } else {
            cb.setChecked(false);
            selectedCollectionMember.collectionMember.RD3Select = false;
        }
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD4);
        if(selectedCollectionMember.collectionMember.RD4CollectionAmount > 0) {
            cb.setChecked(true);
            selectedCollectionMember.collectionMember.RD4Select = true;
        } else {
            cb.setChecked(false);
            selectedCollectionMember.collectionMember.RD4Select = false;
        }
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD5);
        if(selectedCollectionMember.collectionMember.RD5CollectionAmount > 0) {
            cb.setChecked(true);
            selectedCollectionMember.collectionMember.RD5Select = true;
        } else {
            cb.setChecked(false);
            selectedCollectionMember.collectionMember.RD5Select = false;
        }
//bmp 05-Aug-26
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectJLG);
        if(selectedCollectionMember.collectionMember.JLGCollectionAmount > 0) {
            cb.setChecked(true);
            selectedCollectionMember.collectionMember.JLGSelect = true;
        } else {
            cb.setChecked(false);
            selectedCollectionMember.collectionMember.JLGSelect = false;
        }
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectNidhi);
        if(selectedCollectionMember.collectionMember.NidhiCollectionAmount > 0) {
            cb.setChecked(true);
            selectedCollectionMember.collectionMember.NidhiSelect = true;
        } else {
            cb.setChecked(false);
            selectedCollectionMember.collectionMember.NidhiSelect = false;
        }
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectADHP);
        if(selectedCollectionMember.collectionMember.TotalADHPCollectionAmount > 0) {
            cb.setChecked(true);
            selectedCollectionMember.collectionMember.ADHPSelect = true;
        } else {
            cb.setChecked(false);
            selectedCollectionMember.collectionMember.ADHPSelect = false;
        }
//bmp 05-Aug-26

        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectAdditionalRepay);
        if(     (selectedCollectionMember.collectionMember.InterestTypeID == 1) &&
                (selectedCollectionMember.collectionMember.Monthly_Interest == 0) &&
                (selectedCollectionMember.collectionMember.Monthly_Principal == 0) &&
                (selectedCollectionMember.collectionMember.Monthly_SD == 0) &&
                (selectedCollectionMember.collectionMember.Monthly_PolicyRenewalAmt == 0) ) {
            ShowToastMessage("Member EMI information not found.");
            EditText et = (EditText) rootView.findViewById(R.id.editTextAdditionalRepay);
            et.setText("0");
            et.setEnabled(false);
            cb.setChecked(false);
            selectedCollectionMember.collectionMember.AdditionalRepaySelect = false;
            selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount = 0;
        } else if(selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount > 0) {
            cb.setChecked(true);
            selectedCollectionMember.collectionMember.AdditionalRepaySelect = true;
        } else {
            EditText et = (EditText) rootView.findViewById(R.id.editTextAdditionalRepay);
            if(!et.hasFocus()) {
                cb.setChecked(false);
                selectedCollectionMember.collectionMember.AdditionalRepaySelect = false;
            }
        }
        tv = (TextView) rootView.findViewById(R.id.editTextAdditionalRepay);
        tv.setText(String.valueOf(selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount));
        tv.setTextColor(getResources().getColor(R.color.black));
        if(selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount > 0) {
            tv.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            tv.setText("");
        }
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectExtraADHP);
        if(selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount > 0) {
            cb.setChecked(true);
            selectedCollectionMember.collectionMember.ExtraADHPSelect = true;
        } else {
            cb.setChecked(false);
            selectedCollectionMember.collectionMember.ExtraADHPSelect = false;
        }
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectNewRD);
        if(selectedCollectionMember.collectionMember.NewRDCollectionAmount > 0) {
            cb.setChecked(true);
            selectedCollectionMember.collectionMember.NewRDSelect = true;
        } else {
            cb.setChecked(false);
            selectedCollectionMember.collectionMember.NewRDSelect = false;
        }
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectFDCollection);
        if(selectedCollectionMember.collectionMember.FDCollectionAmount > 0) {
            cb.setChecked(true);
            selectedCollectionMember.collectionMember.FDSelect = true;
        } else {
            cb.setChecked(false);
            selectedCollectionMember.collectionMember.FDSelect = false;
        }
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectExtraJLG);
        if(selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount > 0) {
            cb.setChecked(true);
            selectedCollectionMember.collectionMember.ExtraJLGSelect = true;
        } else {
            cb.setChecked(false);
            selectedCollectionMember.collectionMember.ExtraJLGSelect = false;
        }
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectReprintCharges);
        if(selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount > 0) {
            cb.setChecked(true);
            selectedCollectionMember.collectionMember.ReprintChargesSelect = true;
        } else {
            cb.setChecked(false);
            selectedCollectionMember.collectionMember.ReprintChargesSelect = false;
        }

        int iCenterCollectionTotal = 0;
        int iCenterOnlineCollectionTotal = 0;
        int iCenterCashCollectionTotal = 0;
        for(int i=1; i<CenterMemberList.size(); i++) {
            if(CenterMemberList.get(i).collectionMember.SavingAccNo.equalsIgnoreCase(selectedCollectionMember.collectionMember.SavingAccNo)) {
                CenterMemberList.get(i).collectionMember.TotalCollectionAmount = selectedCollectionMember.collectionMember.TotalCollectionAmount;
                CenterMemberList.get(i).collectionMember.strPaymentMode = selectedCollectionMember.collectionMember.strPaymentMode;

                CenterMemberList.get(i).collectionMember.PhoneNo = selectedCollectionMember.collectionMember.PhoneNo;
                CenterMemberList.get(i).collectionMember.AdditionalRepayCollectionAmount = selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount;
                if(selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount > 0)
                    CenterMemberList.get(i).collectionMember.AdditionalRepaySelect = true;

                CenterMemberList.get(i).collectionMember.ExtraADHPCollectionAmount
                        = selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount;
                if(selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount > 0)
                    CenterMemberList.get(i).collectionMember.ExtraADHPSelect = true;
                CenterMemberList.get(i).collectionMember.NewRDCollectionAmount
                        = selectedCollectionMember.collectionMember.NewRDCollectionAmount;
                if(selectedCollectionMember.collectionMember.NewRDCollectionAmount > 0)
                    CenterMemberList.get(i).collectionMember.NewRDSelect = true;
                CenterMemberList.get(i).collectionMember.FDCollectionAmount
                        = selectedCollectionMember.collectionMember.FDCollectionAmount;
                if(selectedCollectionMember.collectionMember.FDCollectionAmount > 0)
                    CenterMemberList.get(i).collectionMember.FDSelect = true;
                CenterMemberList.get(i).collectionMember.ExtraJLGCollectionAmount
                        = selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount;
                if(selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount > 0)
                    CenterMemberList.get(i).collectionMember.ExtraJLGSelect = true;
                CenterMemberList.get(i).collectionMember.ReprintChargesCollectionAmount
                        = selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount;
                if(selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount > 0)
                    CenterMemberList.get(i).collectionMember.ReprintChargesSelect = true;
            }
            iCenterCollectionTotal = iCenterCollectionTotal + CenterMemberList.get(i).collectionMember.TotalCollectionAmount;
            if(CenterMemberList.get(i).collectionMember.strPaymentMode.equalsIgnoreCase("Online")) {
                iCenterOnlineCollectionTotal = iCenterOnlineCollectionTotal + CenterMemberList.get(i).collectionMember.TotalCollectionAmount;
            } else {
                iCenterCashCollectionTotal = iCenterCashCollectionTotal + CenterMemberList.get(i).collectionMember.TotalCollectionAmount;
            }
        }
        selectedCollectionCenter.CenterCollectionTotal = iCenterCollectionTotal;
        selectedCollectionCenter.CenterOnlineCollectionTotal = iCenterOnlineCollectionTotal;
        selectedCollectionCenter.CenterCashCollectionTotal = iCenterCashCollectionTotal;

        tv = (TextView) rootView.findViewById(R.id.textViewCenterCashTotal);
        tv.setText(String.valueOf(selectedCollectionCenter.CenterCashCollectionTotal));
        tv = (TextView) rootView.findViewById(R.id.textViewCenterOnlineTotal);
        tv.setText(String.valueOf(selectedCollectionCenter.CenterOnlineCollectionTotal));
        tv = (TextView) rootView.findViewById(R.id.textViewCenterCollectionTotal);
        tv.setText(String.valueOf(selectedCollectionCenter.CenterCollectionTotal));

//        if(selectedCollectionCenter.CenterCollectionTotal > 0) {
        if(selectedCollectionCenter.CenterCashCollectionTotal > 0) {
            EnableNarationEdit(true);
        }

        Button bt = (Button) rootView.findViewById(R.id.buttonSaveCollection);
//bmp 14-Jul-26
//        if(     (!selectedCollectionCenter.NextRecoveryDate.isEmpty()) &&
//                (!selectedCollectionCenter.NextRecoveryTime.isEmpty()) &&
//                (!selectedCollectionCenter.NextRecoveryDateTime.isEmpty()) &&
//                (selectedCollectionCenter.CenterCashCollectionTotal == selectedCollectionCenter.CenterNarationValueTotal) ) {
        if(     (!selectedCollectionCenter.NextRecoveryDate.isEmpty()) &&
                (!selectedCollectionCenter.NextRecoveryTime.isEmpty()) &&
                (!selectedCollectionCenter.NextRecoveryDateTime.isEmpty()) &&
                (selectedCollectionCenter.CenterCashCollectionTotal == selectedCollectionCenter.CenterNarationValueTotal) &&
                (selectedCollectionCenter.CenterCollectionTotal > 0) ) {
//bmp 14-Jul-26
            bt.setEnabled(true);
        } else {
            bt.setEnabled(false);
        }
        bt = (Button) rootView.findViewById(R.id.buttonSummaryCollection);
        bt.setEnabled(false);
        tv = (TextView) rootView.findViewById(R.id.textViewRequestFocus);
        tv.requestFocus();
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
                        case ufin_COLLECTION_DIALOG_STATE_NONE:
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_INCORRECT_EXTRA_COLLECTION:
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_NO_PRINTER:
                            Button bt = (Button) rootView.findViewById(R.id.buttonPrintCollection);
                            if(bt != null)
                                bt.setEnabled(true);
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_WAITING_FOR_PACKET_UPLOAD:
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_AMT_EXCEED_COLLECTION_LIMIT:
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_PRECLOSURE_REQUEST_SENT:
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_EXPECTED_AMT_COLLECTION_MISMATCH:
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_CONFIRM_UNCOLLECTED:
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_PRECLOSURE_REASON_NOTSELECTED:
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_MD_NOT_WITHIN_LIMIT:
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_PREPAYMENT_MONTHS_AMT_EMPTY:
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_PREPAYMENT_REQUEST_SENT:
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_COLLECTION_SUMMARY_GENERATED:
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_INVALID_RECOVERY_MODE_SET:
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_PRECLOSURE_REJECT_REASON:
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_PENDING_REQUEST_WAIT:
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_PENDING_REQUEST_RESENT:
                            break;
//bmp 21-Jul-26
                        case ufin_COLLECTION_DIALOG_STATE_CONFIRM_RESET_RECOVERY_MODE:
                            break;
//bmp 21-Jul-26
                        case ufin_COLLECTION_DIALOG_STATE_CONFIRM_PRINT_BUTTON:
                            break;
                    }
                    dlgState = ufin_COLLECTION_DIALOG_STATE_NONE;
                }
            });
            dlgAlert.setCancelable(true);
        }
        if(enablePositiveButton) {
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Button bt;
                    // User clicked OK button
                    dialog.dismiss();

                    switch (dlgState) {
                        case ufin_COLLECTION_DIALOG_STATE_NONE:
                        case ufin_COLLECTION_DIALOG_STATE_SUBMIT_DCS:
                        case ufin_COLLECTION_DIALOG_STATE_INCORRECT_EXTRA_COLLECTION:
                        case ufin_COLLECTION_DIALOG_STATE_MOBILE_NO_NOT_VERIFIED:
                        case ufin_COLLECTION_DIALOG_STATE_NO_PRINTER:
                        case ufin_COLLECTION_DIALOG_STATE_AMT_EXCEED_COLLECTION_LIMIT:
                        case ufin_COLLECTION_DIALOG_STATE_EXPECTED_AMT_COLLECTION_MISMATCH:
                        case ufin_COLLECTION_DIALOG_STATE_CONFIRM_UNCOLLECTED:
                        case ufin_COLLECTION_DIALOG_STATE_PRECLOSURE_REASON_NOTSELECTED:
                        case ufin_COLLECTION_DIALOG_STATE_MD_NOT_WITHIN_LIMIT:
                        case ufin_COLLECTION_DIALOG_STATE_PREPAYMENT_MONTHS_AMT_EMPTY:
                        case ufin_COLLECTION_DIALOG_STATE_PREPAYMENT_REQUEST_SENT:
                        case ufin_COLLECTION_DIALOG_STATE_COLLECTION_SUMMARY_GENERATED:
                        case ufin_COLLECTION_DIALOG_STATE_INVALID_RECOVERY_MODE_SET:
                        case ufin_COLLECTION_DIALOG_STATE_PENDING_REQUEST_WAIT:
                        case ufin_COLLECTION_DIALOG_STATE_PENDING_REQUEST_RESENT:
                            dlgState = ufin_COLLECTION_DIALOG_STATE_NONE;
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_CLOSE:
                            if(selectedCollectionCenter != null) {
                                mainActivity.selectedCollectionCenterID = selectedCollectionCenter.CenterID;
                            } else {
                                mainActivity.selectedCollectionCenterID = 0L;
                            }
                            dlgState = ufin_COLLECTION_DIALOG_STATE_NONE;
                            mainActivity.InitDesktop();
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_INCORRECT_MOBILE_NO:
                            EditText et = (EditText) rootView.findViewById(R.id.editTextPhoneNo);
                            et.setText(selectedCollectionMember.collectionMember.PhoneNo);
                            CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxPhoneNo);
                            cb.setChecked(false);
                            dlgState = ufin_COLLECTION_DIALOG_STATE_NONE;
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_RESUME_PRINTING:
                            bt = (Button) rootView.findViewById(R.id.buttonPrintCollection);
                            if(bt != null)
                                bt.setEnabled(true);
                            dlgState = ufin_COLLECTION_DIALOG_STATE_NONE;
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_COMPLETED_PRINTING:
                            if(selectedCollectionCenter != null) {
                                mainActivity.selectedCollectionCenterID = selectedCollectionCenter.CenterID;
                            } else {
                                mainActivity.selectedCollectionCenterID = 0L;
                            }
                            dlgState = ufin_COLLECTION_DIALOG_STATE_NONE;
                            mainActivity.InitDesktop();
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_PRECLOSURE_REQUEST_SENT:
                            RefreshMemberDueDetails();
                            dlgState = ufin_COLLECTION_DIALOG_STATE_NONE;
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_WAITING_FOR_PACKET_UPLOAD:
                            String strPDFDCSFileName = generateDCS();
                            if(ReadyToPrint) {
                                mainActivity.printPDF(strPDFDCSFileName);
                                mainActivity.printPDF(strPDFDCSFileName);
                            }
                            String strActivity = "DCS Print: " + strPDFDCSFileName;
                            mainActivity.UpdateTabLocation(strActivity);
                            mainActivity.flagNewSummary = true;
                            dlgState = ufin_COLLECTION_DIALOG_STATE_NONE;
                            mainActivity.InitDesktop();
                            break;
                        case ufin_COLLECTION_DIALOG_STATE_PRECLOSURE_REJECT_REASON:
                            selectedCollectionMember.collectionMember.PrePaymentORPreClosure = 0;
                            selectedCollectionMember.collectionMember.PrePaymentReqMonths = 0;
                            selectedCollectionMember.collectionMember.PrePaymentApprovedMonths = 0;
                            selectedCollectionMember.collectionMember.PrePaymentReqAmount = 0;
                            selectedCollectionMember.collectionMember.PrePaymentApprovedAmount = 0;
                            selectedCollectionMember.collectionMember.PreClosureReasonID = 0;
                            selectedCollectionMember.collectionMember.PreClosureRequestStatus = -1;
                            selectedCollectionMember.collectionMember.PreClosureRequestDate = "";
                            selectedCollectionMember.collectionMember.PreClosureRejectReasonID = 0;
//bmp 26-Jun-26
                            strSelectedPaymentOption = "Monthly";
//bmp 26-Jun-26
                            RefreshMemberDueDetails();
                            dlgState = ufin_COLLECTION_DIALOG_STATE_NONE;
                            break;
//bmp 21-Jul-26
                        case ufin_COLLECTION_DIALOG_STATE_CONFIRM_RESET_RECOVERY_MODE:
                            selectedCollectionMember.collectionMember.PrePaymentORPreClosure = 0;
                            selectedCollectionMember.collectionMember.PrePaymentReqMonths = 0;
                            selectedCollectionMember.collectionMember.PrePaymentApprovedMonths = 0;
                            selectedCollectionMember.collectionMember.PrePaymentReqAmount = 0;
                            selectedCollectionMember.collectionMember.PrePaymentApprovedAmount = 0;
                            selectedCollectionMember.collectionMember.PreClosureReasonID = 0;
                            selectedCollectionMember.collectionMember.PreClosureRequestStatus = -1;
                            selectedCollectionMember.collectionMember.PreClosureRequestDate = "";
                            selectedCollectionMember.collectionMember.PreClosureRejectReasonID = 0;
                            strSelectedPaymentOption = "Monthly";
                            RefreshMemberDueDetails();
                            dlgState = ufin_COLLECTION_DIALOG_STATE_NONE;
                            break;
//bmp 21-Jul-26
                        case ufin_COLLECTION_DIALOG_STATE_CONFIRM_PRINT_BUTTON:
                            ConfirmPrint = true;
                            bt = (Button) rootView.findViewById(R.id.buttonPrintCollection);
                            bt.performClick();
                            break;
                    }
//                    dlgState = ufin_COLLECTION_DIALOG_STATE_NONE;
                }
            });
        }
//        dlgAlert.setCancelable(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPrePayClosureRequest:
                if(strSelectedPaymentOption.isEmpty() || strSelectedPaymentOption.equalsIgnoreCase("Monthly")) {
                    selectedCollectionMember.collectionMember.PrePaymentORPreClosure = 0;
                    break;
                }
                if(selectedCollectionMember.collectionMember.PreClosureReasonID == 0) {
                    CreateAlertDialog(true, true);
                    dlgState = ufin_COLLECTION_DIALOG_STATE_PRECLOSURE_REASON_NOTSELECTED;
                    dlgAlert.setMessage(MSG_ufin_PRECLOSURE_REASON_NOTSELECTED);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    textView.setTextColor(Color.RED);
                    break;
                } else if (strSelectedPaymentOption.equalsIgnoreCase("PreClosure")) {
                    selectedCollectionMember.collectionMember.PrePaymentORPreClosure = 2;
                    Calendar cal = Calendar.getInstance();
                    Date date = cal.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    String strDate = sdf.format(date);
                    if (strDate.equalsIgnoreCase(selectedCollectionMember.collectionMember.PreClosureRequestDate)) {
                        RefreshMemberDueDetails();
                        break;
                    }
                    mainActivity.GetPrePayment_PreClosureAmount(
                            selectedCollectionMember.collectionMember.MemberID,
                            selectedCollectionMember.collectionMember.PreClosureReasonID,
                            selectedCollectionMember.collectionMember.PrePaymentORPreClosure,
                            0,
                            0);
                    selectedCollectionMember.collectionMember.PreClosureRequestStatus = 0;
                    selectedCollectionMember.collectionMember.PreClosureRequestDate = strDate;

                    CreateAlertDialog(true, true);
                    dlgState = ufin_COLLECTION_DIALOG_STATE_PRECLOSURE_REQUEST_SENT;
                    String strMessage1 = MSG_ufin_PRECLOSURE_REQUEST_SENT;
                    String strMessage2 = "";
                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                    String strToday = sdf2.format(date);
                    if(     !selectedCollectionMember.collectionMember.LastEMIReceiptNumber.isEmpty() &&
                            selectedCollectionMember.collectionMember.ReferenceRecoveryDate.equalsIgnoreCase(strToday) ) {
                        strMessage2 = strMessage2 + "\n" + MSG_ufin_PRECLOSURE_CALCULATION_REMARK;
                    }
                    String fullMessage = strMessage1 + strMessage2;
                    SpannableString spannableMessage = new SpannableString(fullMessage);
                    spannableMessage.setSpan(new ForegroundColorSpan(Color.BLACK),
                            0,
                            strMessage1.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableMessage.setSpan(new ForegroundColorSpan(Color.RED),
                            strMessage1.length(),
                            fullMessage.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    dlgAlert.setMessage(spannableMessage);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                } else if (strSelectedPaymentOption.equalsIgnoreCase("PrePayment")) {
                    selectedCollectionMember.collectionMember.PrePaymentORPreClosure = 1;
                    if(     (selectedCollectionMember.collectionMember.PrePaymentReqMonths > 0) ||
                            (selectedCollectionMember.collectionMember.PrePaymentReqAmount > 0) ) {
                        Calendar cal = Calendar.getInstance();
                        Date date = cal.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        String strDate = sdf.format(date);
                        if (selectedCollectionMember.collectionMember.PrePaymentReqMonths > 0) {
                            if(selectedCollectionMember.collectionMember.PrePaymentReqMonths <= 2) {
                                //PrePayment upto 3 months: Approval not Required
                                selectedCollectionMember.collectionMember.PreClosureRequestDate = strDate;
                                selectedCollectionMember.collectionMember.PrePaymentApprovedMonths = selectedCollectionMember.collectionMember.PrePaymentReqMonths;
                                selectedCollectionMember.collectionMember.PrePaymentApprovedAmount = 0;
                                selectedCollectionMember.collectionMember.PreClosureRequestStatus = 1;

                                String selectedPreCollect = "";
                                for (int i = 0; i < PrePaymentReasonIDs.size(); i++) {
                                    if (PrePaymentReasonIDs.get(i) == selectedCollectionMember.collectionMember.PreClosureReasonID) {
                                        selectedPreCollect = strPrePaymentCollect[i];
                                        break;
                                    }
                                }

                                CalculatePrePaymentDetails(selectedPreCollect);
                                RefreshMemberDueDetails();
                                break;
                            }
                        } else {
                            int iAutoApprovalLimit = selectedCollectionMember.collectionMember.TotalDueAmountsToPayRefday
                                    + (2 * selectedCollectionMember.collectionMember.StandardEMI)
                                    - selectedCollectionMember.collectionMember.PrePaymentPrincipalCollected;
                            if(selectedCollectionMember.collectionMember.PrePaymentReqAmount <= iAutoApprovalLimit) {
                                //PrePayment Amount upto 3 month EMI: Approval not Required
                                selectedCollectionMember.collectionMember.PreClosureRequestDate = strDate;
                                selectedCollectionMember.collectionMember.PrePaymentApprovedMonths = 0;
                                selectedCollectionMember.collectionMember.PrePaymentApprovedAmount = selectedCollectionMember.collectionMember.PrePaymentReqAmount;
                                selectedCollectionMember.collectionMember.PreClosureRequestStatus = 1;

                                String selectedPreCollect = "";
                                for (int i = 0; i < PrePaymentReasonIDs.size(); i++) {
                                    if (PrePaymentReasonIDs.get(i) == selectedCollectionMember.collectionMember.PreClosureReasonID) {
                                        selectedPreCollect = strPrePaymentCollect[i];
                                        break;
                                    }
                                }

                                CalculatePrePaymentDetails(selectedPreCollect);
                                RefreshMemberDueDetails();
                                break;
                            }
                        }

                        if (strDate.equalsIgnoreCase(selectedCollectionMember.collectionMember.PreClosureRequestDate)) {
                            RefreshMemberDueDetails();
                            break;
                        }
                        mainActivity.GetPrePayment_PreClosureAmount(
                                selectedCollectionMember.collectionMember.MemberID,
                                selectedCollectionMember.collectionMember.PreClosureReasonID,
                                selectedCollectionMember.collectionMember.PrePaymentORPreClosure,
                                selectedCollectionMember.collectionMember.PrePaymentReqMonths,
                                selectedCollectionMember.collectionMember.PrePaymentReqAmount);
                        selectedCollectionMember.collectionMember.PreClosureRequestStatus = 0;
                        selectedCollectionMember.collectionMember.PreClosureRequestDate = strDate;

                        CreateAlertDialog(true, true);
                        dlgState = ufin_COLLECTION_DIALOG_STATE_PREPAYMENT_REQUEST_SENT;
                        dlgAlert.setMessage(MSG_ufin_PREPAYMENT_REQUEST_SENT);
                        AlertDialog dialog = dlgAlert.create();
                        dialog.show();
                        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                        textView.setTextSize(30);
                        textView.setTextColor(Color.RED);
                    } else {
                        CreateAlertDialog(true, true);
                        dlgState = ufin_COLLECTION_DIALOG_STATE_PREPAYMENT_MONTHS_AMT_EMPTY;
                        dlgAlert.setMessage(MSG_ufin_PREPAYMENT_MONTHS_AMT_EMPTY);
                        AlertDialog dialog = dlgAlert.create();
                        dialog.show();
                        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                        textView.setTextSize(30);
                        textView.setTextColor(Color.RED);
                    }
                }
                break;
            case R.id.buttonSchedule:
                CustomViewPager viewPager = (CustomViewPager) mainActivity.findViewById(R.id.viewpagerCollection);
                if(viewPager != null)
                    viewPager.requestFocus();
                mainActivity.iPrevViewId = R.id.viewpagerCollection;
                mainActivity.iCurrentScreen = 0;
                viewPager.setCurrentItem(mainActivity.iCurrentScreen);
                break;
            case R.id.buttonCloseCollection:
                CreateAlertDialog(true, true);
                dlgState = ufin_COLLECTION_DIALOG_STATE_CLOSE;
                dlgAlert.setMessage(MSG_ufin_COLLECTION_CONFIRM_CLOSE);
                AlertDialog dialog = dlgAlert.create();
                dialog.show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(30);
                textView.setTextColor(Color.RED);
                break;
            case R.id.buttonPrintCollection:
                if(!ConfirmPrint) {
                    CreateAlertDialog(true, true);
                    dlgState = ufin_COLLECTION_DIALOG_STATE_CONFIRM_PRINT_BUTTON;
                    dlgAlert.setMessage(MSG_ufin_COLLECTION_CONFIRM_PRINT_BUTTON);
                    dialog = dlgAlert.create();
                    dialog.show();
                    textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    textView.setTextColor(Color.RED);
                    break;
                }
                ConfirmPrint = false;
                if(SavedNarration) {
                    if(memberReceiptIndex == 0) {
                        mainActivity.disconnectPrinter();
                        ReadyToPrint = mainActivity.checkPrinter();
                        if(!ReadyToPrint) {
                            CreateAlertDialog(false, true);
                            dlgState = ufin_COLLECTION_DIALOG_STATE_NO_PRINTER;
                            dlgAlert.setMessage(MSG_ufin_COLLECTION_NO_PRINTER);
                            dialog = dlgAlert.create();
                            dialog.show();
                            textView = (TextView) dialog.findViewById(android.R.id.message);
                            textView.setTextSize(30);
                            textView.setTextColor(Color.RED);
                            break;
                        }
                    }
//bmp 17-Sep-26
//                    Button bt = rootView.findViewById(R.id.buttonPrintCollection);
//                    bt.setEnabled(false);
//                    long receiptMemberID = memberReceipt.get(memberReceiptIndex).MembeID;
//                    for(int i=memberReceiptIndex; i<memberReceipt.size(); i++) {
//                        if(memberReceipt.get(i).MembeID != receiptMemberID)
//                            break;
//                        mainActivity.printPDF(memberReceipt.get(i).pdfReceipt);
//                        memberReceiptIndex = memberReceiptIndex + 1;
//                    }
//                    if(memberReceiptIndex < memberReceipt.size()) {
//                        CreateAlertDialog(true, false);
//                        dlgState = ufin_COLLECTION_DIALOG_STATE_RESUME_PRINTING;
//                        dlgAlert.setMessage(MSG_ufin_COLLECTION_RESUME_PRINTING);
//                        dialog = dlgAlert.create();
//                        dialog.show();
//                        textView = (TextView) dialog.findViewById(android.R.id.message);
//                        textView.setTextSize(30);
//                        textView.setTextColor(Color.RED);
//                        textView.setPadding(0,500,0,0);
//                        textView.setGravity(Gravity.CENTER);
//                        textView.setLayoutParams(new LinearLayout.LayoutParams(
//                                LinearLayout.LayoutParams.MATCH_PARENT,
//                                LinearLayout.LayoutParams.MATCH_PARENT));
//                        WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
//                        lp2.copyFrom(dialog.getWindow().getAttributes());
//                        lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
//                        lp2.height = WindowManager.LayoutParams.MATCH_PARENT;
//                        lp2.gravity = Gravity.CENTER_VERTICAL;
//                        dialog.getWindow().setAttributes(lp2);
//                    } else {
//                        if(selectedCollectionCenter != null) {
//                            mainActivity.selectedCollectionCenterID = selectedCollectionCenter.CenterID;
//                        } else {
//                            mainActivity.selectedCollectionCenterID = 0L;
//                        }
//                        bt = rootView.findViewById(R.id.buttonPrintCollection);
//                        bt.setEnabled(true);
//                        mainActivity.InitDesktop();
//                    }
                    AsyncCallWS_PrintPDF1 taskPrintPDF = new AsyncCallWS_PrintPDF1();
                    taskPrintPDF.execute();
//bmp 17-Sep-26
                } else {
                    if(selectedCollectionMember != null) {
                        if(     (selectedCollectionMember.collectionMember.MemberStatus == clsCollectionMember.COLLECTION_MEMBER_STATUS_PARTIAL) ||
                                (selectedCollectionMember.collectionMember.MemberStatus == clsCollectionMember.COLLECTION_MEMBER_STATUS_FULL) ) {
                            String strMemberFolderpath = mainActivity.baseDir + "/data/receipt/" + String.valueOf(selectedCollectionMember.collectionMember.MemberID);
                            File memberFolder = new File(strMemberFolderpath);
                            if(memberFolder.exists()) {
                                File[] memberFiles = memberFolder.listFiles();
                                mainActivity.disconnectPrinter();
                                ReadyToPrint = mainActivity.checkPrinter();
                                if(!ReadyToPrint) {
                                    CreateAlertDialog(false, true);
                                    dlgState = ufin_COLLECTION_DIALOG_STATE_NO_PRINTER;
                                    dlgAlert.setMessage(MSG_ufin_COLLECTION_NO_PRINTER);
                                    dialog = dlgAlert.create();
                                    dialog.show();
                                    textView = (TextView) dialog.findViewById(android.R.id.message);
                                    textView.setTextSize(30);
                                    textView.setTextColor(Color.RED);
                                    break;
                                }
//bmp 17-Sep-26
//                                for(int i=0; i<memberFiles.length; i++) {
//                                    mainActivity.printPDF(strMemberFolderpath + "/" + memberFiles[i].getName());
//                                }
//                                Button bt = rootView.findViewById(R.id.buttonPrintCollection);
//                                bt.setEnabled(true);
                                AsyncCallWS_PrintPDF2 taskPrintPDF = new AsyncCallWS_PrintPDF2();
                                taskPrintPDF.execute();
//bmp 17-Sep-26
                            }
                        }
                    }
                }
                break;
            case R.id.buttonSaveCollection:
                if(!ValidateCenterMemberPhoneNo())
                    break;
                if(!ValidateNextRecoveryDateTime())
                    break;
                if(!ValidateCenterMemberCollection())
                    break;
                if(!SavedNarration) {
                    if (selectedCollectionMember != null) {
                        int iTemp = selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount +
                                selectedCollectionMember.collectionMember.NewRDCollectionAmount +
                                selectedCollectionMember.collectionMember.FDCollectionAmount +
                                selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount +
                                selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount;
                        if (selectedCollectionMember.collectionMember.ExtraCollectionAmount != iTemp) {
///////////This needs to be checked first before validating PhoneNo,NextRecoveryDateTime,UncollectedMembers.... ????    ///////////
                            CreateAlertDialog(true, false);
                            dlgState = ufin_COLLECTION_DIALOG_STATE_INCORRECT_EXTRA_COLLECTION;
                            dlgAlert.setMessage(MSG_ufin_COLLECTION_INCORRECT_EXTRA_COLLECTION);
                            dialog = dlgAlert.create();
                            dialog.show();
                            textView = (TextView) dialog.findViewById(android.R.id.message);
                            textView.setTextSize(30);
                            textView.setTextColor(Color.RED);
                            break;
                        }
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PrePaymentORPreClosure
                                = selectedCollectionMember.collectionMember.PrePaymentORPreClosure;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PreClosureReasonID
                                = selectedCollectionMember.collectionMember.PreClosureReasonID;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PhoneNo
                                = selectedCollectionMember.collectionMember.PhoneNo;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).TotalCollectionAmount
                                = selectedCollectionMember.collectionMember.TotalCollectionAmount;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).TotalEMICollectionAmount
                                = selectedCollectionMember.collectionMember.TotalEMICollectionAmount;
//bmp 12-Aug-26
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).JLGCollectionAmount
                                = selectedCollectionMember.collectionMember.JLGCollectionAmount;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).NidhiCollectionAmount
                                = selectedCollectionMember.collectionMember.NidhiCollectionAmount;
//bmp 12-Aug-26
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).TotalADHPCollectionAmount
                                = selectedCollectionMember.collectionMember.TotalADHPCollectionAmount;

                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).AdditionalRepayCollectionAmount
                                = selectedCollectionMember.collectionMember.AdditionalRepayCollectionAmount;

                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PreClosureRequestDate
                                = selectedCollectionMember.collectionMember.PreClosureRequestDate;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PreClosureRequestStatus
                                = selectedCollectionMember.collectionMember.PreClosureRequestStatus;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).PreClosurePrincipal
                                = selectedCollectionMember.collectionMember.PreClosurePrincipal;

                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).ExtraCollectionAmount
                                = selectedCollectionMember.collectionMember.ExtraCollectionAmount;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).ExtraADHPCollectionAmount
                                = selectedCollectionMember.collectionMember.ExtraADHPCollectionAmount;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).NewRDCollectionAmount
                                = selectedCollectionMember.collectionMember.NewRDCollectionAmount;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).FDCollectionAmount
                                = selectedCollectionMember.collectionMember.FDCollectionAmount;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).ExtraJLGCollectionAmount
                                = selectedCollectionMember.collectionMember.ExtraJLGCollectionAmount;
                        mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).ReprintChargesCollectionAmount
                                = selectedCollectionMember.collectionMember.ReprintChargesCollectionAmount;

                        if (selectedCollectionMember.collectionMember.RD1CollectionAmount > 0) {
                            mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).RD1CollectionAmount
                                    = selectedCollectionMember.collectionMember.RD1CollectionAmount;
                            mainActivity.RDReceiptList.get(selectedCollectionMember.index1RDReceipts).ActualAmountReceived
                                    = selectedCollectionMember.collectionMember.RD1CollectionAmount;
                        } else {
                            mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).RD1CollectionAmount = 0;
                            if(selectedCollectionMember.index1RDReceipts >= 0)
                                mainActivity.RDReceiptList.get(selectedCollectionMember.index1RDReceipts).ActualAmountReceived = 0;
                        }
                        if (selectedCollectionMember.collectionMember.RD2CollectionAmount > 0) {
                            mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).RD2CollectionAmount
                                    = selectedCollectionMember.collectionMember.RD2CollectionAmount;
                            mainActivity.RDReceiptList.get(selectedCollectionMember.index2RDReceipts).ActualAmountReceived
                                    = selectedCollectionMember.collectionMember.RD2CollectionAmount;
                        } else {
                            mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).RD2CollectionAmount = 0;
                            if(selectedCollectionMember.index2RDReceipts >= 0)
                                mainActivity.RDReceiptList.get(selectedCollectionMember.index2RDReceipts).ActualAmountReceived = 0;
                        }
                        if (selectedCollectionMember.collectionMember.RD3CollectionAmount > 0) {
                            mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).RD3CollectionAmount
                                    = selectedCollectionMember.collectionMember.RD3CollectionAmount;
                            mainActivity.RDReceiptList.get(selectedCollectionMember.index3RDReceipts).ActualAmountReceived
                                    = selectedCollectionMember.collectionMember.RD3CollectionAmount;
                        } else {
                            mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).RD3CollectionAmount = 0;
                            if(selectedCollectionMember.index3RDReceipts >= 0)
                                mainActivity.RDReceiptList.get(selectedCollectionMember.index3RDReceipts).ActualAmountReceived = 0;
                        }
                        if (selectedCollectionMember.collectionMember.RD4CollectionAmount > 0) {
                            mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).RD4CollectionAmount
                                    = selectedCollectionMember.collectionMember.RD4CollectionAmount;
                            mainActivity.RDReceiptList.get(selectedCollectionMember.index4RDReceipts).ActualAmountReceived
                                    = selectedCollectionMember.collectionMember.RD4CollectionAmount;
                        } else {
                            mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).RD4CollectionAmount = 0;
                            if(selectedCollectionMember.index4RDReceipts >= 0)
                                mainActivity.RDReceiptList.get(selectedCollectionMember.index4RDReceipts).ActualAmountReceived = 0;
                        }
                        if (selectedCollectionMember.collectionMember.RD5CollectionAmount > 0) {
                            mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).RD5CollectionAmount
                                    = selectedCollectionMember.collectionMember.RD5CollectionAmount;
                            mainActivity.RDReceiptList.get(selectedCollectionMember.index5RDReceipts).ActualAmountReceived
                                    = selectedCollectionMember.collectionMember.RD5CollectionAmount;
                        } else {
                            mainActivity.CollectionMemberList.get(selectedCollectionMember.indexCollectionMemberList).RD5CollectionAmount = 0;
                            if(selectedCollectionMember.index5RDReceipts >= 0)
                                mainActivity.RDReceiptList.get(selectedCollectionMember.index5RDReceipts).ActualAmountReceived = 0;
                        }
                    }

                    memberReceipt.clear();

                    for (int i = 1; i < CenterMemberList.size(); i++) {
                        int memberIndex = CenterMemberList.get(i).indexCollectionMemberList;
                        if (mainActivity.CollectionMemberList.get(memberIndex).TotalCollectionAmount == 0)
                            continue;

                        if (mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount > 0) {
//bmp 09-Jul-26
//                            if (mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount
//                                    < mainActivity.CollectionMemberList.get(memberIndex).LoanCurrentDueAmountToday) {
//                                //LoanCurrentDueAmount
//                                mainActivity.CollectionMemberList.get(memberIndex).LoanCurrentDueAmountRefday
//                                        = mainActivity.CollectionMemberList.get(memberIndex).LoanCurrentDueAmountToday
//                                        - mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount;
//                            } else {
//                                //LoanPastDueAmountTillToday
//                                mainActivity.CollectionMemberList.get(memberIndex).LoanPastDueAmountTillRefday
//                                        = mainActivity.CollectionMemberList.get(memberIndex).LoanPastDueAmountTillToday
//                                        - (mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount
//                                        - mainActivity.CollectionMemberList.get(memberIndex).LoanCurrentDueAmountToday);
//                                if(mainActivity.CollectionMemberList.get(memberIndex).LoanPastDueAmountTillRefday < 0) {
//                                    mainActivity.CollectionMemberList.get(memberIndex).LoanPastDueAmountTillRefday = 0;
//                                }
//                                //LoanCurrentDueAmount
//                                mainActivity.CollectionMemberList.get(memberIndex).LoanCurrentDueAmountRefday = 0;
//                            }
//                            //LoanPastDuePlusCurrentDueAmt
//                            mainActivity.CollectionMemberList.get(memberIndex).LoanPastDuePlusCurrentDueAmtRefday
//                                    = mainActivity.CollectionMemberList.get(memberIndex).LoanPastDueAmountTillRefday
//                                    + mainActivity.CollectionMemberList.get(memberIndex).LoanCurrentDueAmountRefday;
///////////////////////////////////////////////////////////////////////////////////
                            if (mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount
                                    < mainActivity.CollectionMemberList.get(memberIndex).LoanPastDueAmountTillToday) {
                                //LoanPastDueAmountTillToday
                                mainActivity.CollectionMemberList.get(memberIndex).LoanPastDueAmountTillRefday
                                        = mainActivity.CollectionMemberList.get(memberIndex).LoanPastDueAmountTillToday
                                        - mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount;
                            } else {
                                //LoanCurrentDueAmount
                                mainActivity.CollectionMemberList.get(memberIndex).LoanCurrentDueAmountRefday
                                        = mainActivity.CollectionMemberList.get(memberIndex).LoanCurrentDueAmountToday
                                        - (mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount
                                        - mainActivity.CollectionMemberList.get(memberIndex).LoanPastDueAmountTillToday);
                                if(mainActivity.CollectionMemberList.get(memberIndex).LoanCurrentDueAmountRefday < 0) {
                                    mainActivity.CollectionMemberList.get(memberIndex).LoanCurrentDueAmountRefday = 0;
                                }
                                //LoanPastDueAmountTillToday
                                mainActivity.CollectionMemberList.get(memberIndex).LoanPastDueAmountTillRefday = 0;
                            }
                            //LoanPastDuePlusCurrentDueAmt
                            mainActivity.CollectionMemberList.get(memberIndex).LoanPastDuePlusCurrentDueAmtRefday
                                    = mainActivity.CollectionMemberList.get(memberIndex).LoanPastDueAmountTillRefday
                                    + mainActivity.CollectionMemberList.get(memberIndex).LoanCurrentDueAmountRefday;
///////////////////////////////////////////////////////////////////////////////////
//                            if (mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount
//                                    < mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest) {
//                                mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest
//                                        = mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest
//                                        - mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount;
//                            } else {
//                                int iTemp = mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount
//                                            - mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest;
//                                if(iTemp < mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest
//                                        = mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest
//                                        - (mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount
//                                        - mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest);
//                                if(mainActivity.CollectionMemberList.get(memberIndex).LoanCurrentDueAmountRefday < 0) {
//                                    mainActivity.CollectionMemberList.get(memberIndex).LoanCurrentDueAmountRefday = 0;
//                                }
//                                mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest = 0;
//                            }
//                            //LoanPastDuePlusCurrentDueAmt
//                            mainActivity.CollectionMemberList.get(memberIndex).LoanPastDuePlusCurrentDueAmtRefday
//                                    = mainActivity.CollectionMemberList.get(memberIndex).LoanPastDueAmountTillRefday
//                                    + mainActivity.CollectionMemberList.get(memberIndex).LoanCurrentDueAmountRefday;
//
//                            //LoanPastDueAmountTillToday
//                            //LoanCurrentDueAmount
//
///////////////////////////////////////////////////////////////////////////////////
//bmp 09-Jul-26
                        }

                        if (mainActivity.CollectionMemberList.get(memberIndex).TotalADHPCollectionAmount > 0) {
                            if (mainActivity.CollectionMemberList.get(memberIndex).TotalADHPCollectionAmount
                                    < mainActivity.CollectionMemberList.get(memberIndex).ADHPContributionAmount) {
                                //ADHPContributionAmount
                                mainActivity.CollectionMemberList.get(memberIndex).ADHPContributionAmount
                                        = mainActivity.CollectionMemberList.get(memberIndex).ADHPContributionAmount
                                        - mainActivity.CollectionMemberList.get(memberIndex).TotalADHPCollectionAmount;
                                //ADHPTotalExpectedAndPastDueAmt
                                mainActivity.CollectionMemberList.get(memberIndex).ADHPTotalExpectedAndPastDueAmt
                                        = mainActivity.CollectionMemberList.get(memberIndex).ADHPTotalExpectedAndPastDueAmt
                                        - mainActivity.CollectionMemberList.get(memberIndex).TotalADHPCollectionAmount;
                            } else {
                                //ADHPTotalExpectedAndPastDueAmt
                                mainActivity.CollectionMemberList.get(memberIndex).ADHPTotalExpectedAndPastDueAmt
                                        = mainActivity.CollectionMemberList.get(memberIndex).ADHPTotalExpectedAndPastDueAmt
                                        - mainActivity.CollectionMemberList.get(memberIndex).TotalADHPCollectionAmount;
                                //ADHPContributionAmount
                                mainActivity.CollectionMemberList.get(memberIndex).ADHPContributionAmount = 0;
                            }
                        }

                        //RD1 to RD5
                        //ActualAmountReceived
                        if (mainActivity.CollectionMemberList.get(memberIndex).RD1CollectionAmount > 0) {
                            //PastDue
                            CenterMemberList.get(i).collectionMember.RD1PastDue
                                    = CenterMemberList.get(i).collectionMember.RD1PastDue
                                    - (mainActivity.CollectionMemberList.get(memberIndex).RD1CollectionAmount
                                    - CenterMemberList.get(i).collectionMember.RD1CurrentInstallmentAmount);
                            mainActivity.RDReceiptList.get(CenterMemberList.get(i).index1RDReceipts).PastDue
                                    = CenterMemberList.get(i).collectionMember.RD1PastDue;

                            //ExpectedInstallmentAmt
                            CenterMemberList.get(i).collectionMember.RD1CurrentInstallmentAmount = 0;
                            mainActivity.RDReceiptList.get(CenterMemberList.get(i).index1RDReceipts).ExpectedInstallmentAmt = 0;
                            //ExtraPaid => Not used
                        }
                        if (mainActivity.CollectionMemberList.get(memberIndex).RD2CollectionAmount > 0) {
                            //PastDue
                            CenterMemberList.get(i).collectionMember.RD2PastDue
                                    = CenterMemberList.get(i).collectionMember.RD2PastDue
                                    - (mainActivity.CollectionMemberList.get(memberIndex).RD2CollectionAmount
                                    - CenterMemberList.get(i).collectionMember.RD2CurrentInstallmentAmount);
                            mainActivity.RDReceiptList.get(CenterMemberList.get(i).index2RDReceipts).PastDue
                                    = CenterMemberList.get(i).collectionMember.RD2PastDue;

                            //ExpectedInstallmentAmt
                            CenterMemberList.get(i).collectionMember.RD2CurrentInstallmentAmount = 0;
                            mainActivity.RDReceiptList.get(CenterMemberList.get(i).index2RDReceipts).ExpectedInstallmentAmt = 0;
                            //ExtraPaid => Not used
                        }
                        if (mainActivity.CollectionMemberList.get(memberIndex).RD3CollectionAmount > 0) {
                            //PastDue
                            CenterMemberList.get(i).collectionMember.RD3PastDue
                                    = CenterMemberList.get(i).collectionMember.RD3PastDue
                                    - (mainActivity.CollectionMemberList.get(memberIndex).RD3CollectionAmount
                                    - CenterMemberList.get(i).collectionMember.RD3CurrentInstallmentAmount);
                            mainActivity.RDReceiptList.get(CenterMemberList.get(i).index3RDReceipts).PastDue
                                    = CenterMemberList.get(i).collectionMember.RD3PastDue;

                            //ExpectedInstallmentAmt
                            CenterMemberList.get(i).collectionMember.RD3CurrentInstallmentAmount = 0;
                            mainActivity.RDReceiptList.get(CenterMemberList.get(i).index3RDReceipts).ExpectedInstallmentAmt = 0;
                            //ExtraPaid => Not used
                        }
                        if (mainActivity.CollectionMemberList.get(memberIndex).RD4CollectionAmount > 0) {
                            //PastDue
                            CenterMemberList.get(i).collectionMember.RD4PastDue
                                    = CenterMemberList.get(i).collectionMember.RD4PastDue
                                    - (mainActivity.CollectionMemberList.get(memberIndex).RD4CollectionAmount
                                    - CenterMemberList.get(i).collectionMember.RD4CurrentInstallmentAmount);
                            mainActivity.RDReceiptList.get(CenterMemberList.get(i).index4RDReceipts).PastDue
                                    = CenterMemberList.get(i).collectionMember.RD4PastDue;

                            //ExpectedInstallmentAmt
                            CenterMemberList.get(i).collectionMember.RD4CurrentInstallmentAmount = 0;
                            mainActivity.RDReceiptList.get(CenterMemberList.get(i).index4RDReceipts).ExpectedInstallmentAmt = 0;
                            //ExtraPaid => Not used
                        }
                        if (mainActivity.CollectionMemberList.get(memberIndex).RD5CollectionAmount > 0) {
                            //PastDue
                            CenterMemberList.get(i).collectionMember.RD5PastDue
                                    = CenterMemberList.get(i).collectionMember.RD5PastDue
                                    - (mainActivity.CollectionMemberList.get(memberIndex).RD5CollectionAmount
                                    - CenterMemberList.get(i).collectionMember.RD5CurrentInstallmentAmount);
                            mainActivity.RDReceiptList.get(CenterMemberList.get(i).index5RDReceipts).PastDue
                                    = CenterMemberList.get(i).collectionMember.RD5PastDue;

                            //ExpectedInstallmentAmt
                            CenterMemberList.get(i).collectionMember.RD5CurrentInstallmentAmount = 0;
                            mainActivity.RDReceiptList.get(CenterMemberList.get(i).index5RDReceipts).ExpectedInstallmentAmt = 0;
                            //ExtraPaid => Not used
                        }
                        //TotalRDCurrentDueAndExpectedAmt - Not Used
                        //RDMonthlyTotalContributionAmount - Not Used
                        //TotalMonthlyInstallmentAmount - Not Used

                        if (mainActivity.CollectionMemberList.get(memberIndex).TotalCollectionAmount >= mainActivity.CollectionMemberList.get(memberIndex).TotalDueAmountsToPayToday)
                            mainActivity.CollectionMemberList.get(memberIndex).MemberStatus = clsCollectionMember.COLLECTION_MEMBER_STATUS_FULL;
                        else
                            mainActivity.CollectionMemberList.get(memberIndex).MemberStatus = clsCollectionMember.COLLECTION_MEMBER_STATUS_PARTIAL;
//bmp 12-May-26
                        if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
                            SQLite_saveOnlineTransactionDoc(memberIndex);
                        }
//bmp 12-May-26
                        //TotalDueAmountsToPay
                        mainActivity.CollectionMemberList.get(memberIndex).TotalDueAmountsToPayRefday
                                = mainActivity.CollectionMemberList.get(memberIndex).LoanPastDuePlusCurrentDueAmtRefday
                                + mainActivity.CollectionMemberList.get(memberIndex).ADHPTotalExpectedAndPastDueAmt
                                + CenterMemberList.get(i).collectionMember.RD1PastDue
                                + CenterMemberList.get(i).collectionMember.RD1CurrentInstallmentAmount
                                + CenterMemberList.get(i).collectionMember.RD2PastDue
                                + CenterMemberList.get(i).collectionMember.RD2CurrentInstallmentAmount
                                + CenterMemberList.get(i).collectionMember.RD3PastDue
                                + CenterMemberList.get(i).collectionMember.RD3CurrentInstallmentAmount
                                + CenterMemberList.get(i).collectionMember.RD4PastDue
                                + CenterMemberList.get(i).collectionMember.RD4CurrentInstallmentAmount
                                + CenterMemberList.get(i).collectionMember.RD5PastDue
                                + CenterMemberList.get(i).collectionMember.RD5CurrentInstallmentAmount;

                        if (mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount > 0) {
                            String StrPDFReceiptFileName = generateReceipt(memberIndex, 0, "Loan");
                            SQLite_updateCollectionMember(memberIndex, "Loan", 0, true);
                            iTotalEMIReceiptsCount = iTotalEMIReceiptsCount + 1;
                            iTotalEMIReceiptsValue = iTotalEMIReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount;
                            if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
                                iTotalOnlineEMIReceiptsCount = iTotalOnlineEMIReceiptsCount + 1;
                                iTotalOnlineEMIReceiptsValue = iTotalOnlineEMIReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount;
                            } else {
                                iTotalCashEMIReceiptsCount = iTotalCashEMIReceiptsCount + 1;
                                iTotalCashEMIReceiptsValue = iTotalCashEMIReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount;
                            }
                            clsMemberReceipt memberPDF = new clsMemberReceipt();
                            memberPDF.MembeID = mainActivity.CollectionMemberList.get(memberIndex).MemberID;
                            memberPDF.pdfReceipt = StrPDFReceiptFileName;
                            memberReceipt.add(memberPDF);
                        }

                        if (mainActivity.CollectionMemberList.get(memberIndex).AdditionalRepayCollectionAmount > 0) {
                            String StrPDFReceiptFileName = generateReceipt(memberIndex, 0, "AdditionalRepay");
                            SQLite_updateCollectionMember(memberIndex, "AdditionalRepay", 0, true);
//bmp 20-Jul-26
//                            if(mainActivity.CollectionMemberList.get(memberIndex).PrePaymentORPreClosure == 1) {
                            if(     (mainActivity.CollectionMemberList.get(memberIndex).PrePaymentORPreClosure == 0) ||
                                    (mainActivity.CollectionMemberList.get(memberIndex).PrePaymentORPreClosure == 1) ) {
//bmp 20-Jul-26
                                iTotalAdditionalEMIReceiptsCount = iTotalAdditionalEMIReceiptsCount + 1;
                                iTotalAdditionalEMIReceiptsValue = iTotalAdditionalEMIReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).AdditionalRepayCollectionAmount;
                                if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
                                    iTotalOnlineAdditionalEMIReceiptsCount = iTotalOnlineAdditionalEMIReceiptsCount + 1;
                                    iTotalOnlineAdditionalEMIReceiptsValue = iTotalOnlineAdditionalEMIReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).AdditionalRepayCollectionAmount;
                                } else {
                                    iTotalCashAdditionalEMIReceiptsCount = iTotalCashAdditionalEMIReceiptsCount + 1;
                                    iTotalCashAdditionalEMIReceiptsValue = iTotalCashAdditionalEMIReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).AdditionalRepayCollectionAmount;
                                }
                            } else if(mainActivity.CollectionMemberList.get(memberIndex).PrePaymentORPreClosure == 2) {
                                iTotalPreClosureAdditionalEMIReceiptsCount = iTotalPreClosureAdditionalEMIReceiptsCount + 1;
                                iTotalPreClosureAdditionalEMIReceiptsValue = iTotalPreClosureAdditionalEMIReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).AdditionalRepayCollectionAmount;
                                if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
                                    iTotalOnlinePreClosureAdditionalEMIReceiptsCount = iTotalOnlinePreClosureAdditionalEMIReceiptsCount + 1;
                                    iTotalOnlinePreClosureAdditionalEMIReceiptsValue = iTotalOnlinePreClosureAdditionalEMIReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).AdditionalRepayCollectionAmount;
                                } else {
                                    iTotalCashPreClosureAdditionalEMIReceiptsCount = iTotalCashPreClosureAdditionalEMIReceiptsCount + 1;
                                    iTotalCashPreClosureAdditionalEMIReceiptsValue = iTotalCashPreClosureAdditionalEMIReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).AdditionalRepayCollectionAmount;
                                }
                            }
                            clsMemberReceipt memberPDF = new clsMemberReceipt();
                            memberPDF.MembeID = mainActivity.CollectionMemberList.get(memberIndex).MemberID;
                            memberPDF.pdfReceipt = StrPDFReceiptFileName;
                            memberReceipt.add(memberPDF);
                        }
                        if (mainActivity.CollectionMemberList.get(memberIndex).TotalADHPCollectionAmount > 0) {
                            String StrPDFReceiptFileName = generateReceipt(memberIndex, 0, "ADHP");
                            SQLite_updateCollectionMember(memberIndex, "ADHP", 0, true);
                            iTotalADHPReceiptsCount = iTotalADHPReceiptsCount + 1;
                            iTotalADHPReceiptsValue = iTotalADHPReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).TotalADHPCollectionAmount;
                            if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
                                iTotalOnlineADHPReceiptsCount = iTotalOnlineADHPReceiptsCount + 1;
                                iTotalOnlineADHPReceiptsValue = iTotalOnlineADHPReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).TotalADHPCollectionAmount;
                            } else {
                                iTotalCashADHPReceiptsCount = iTotalCashADHPReceiptsCount + 1;
                                iTotalCashADHPReceiptsValue = iTotalCashADHPReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).TotalADHPCollectionAmount;
                            }
                            clsMemberReceipt memberPDF = new clsMemberReceipt();
                            memberPDF.MembeID = mainActivity.CollectionMemberList.get(memberIndex).MemberID;
                            memberPDF.pdfReceipt = StrPDFReceiptFileName;
                            memberReceipt.add(memberPDF);
                        }
                        if (mainActivity.CollectionMemberList.get(memberIndex).ExtraCollectionAmount > 0) {
                            String StrPDFReceiptFileName = generateReceipt(memberIndex, 0, "Extra");
                            if (mainActivity.CollectionMemberList.get(memberIndex).ExtraADHPCollectionAmount > 0) {
                                iTotalADHPExtraReceiptsCount = iTotalADHPExtraReceiptsCount + 1;
                                iTotalADHPExtraReceiptsValue = iTotalADHPExtraReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).ExtraADHPCollectionAmount;
                                if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
                                    iTotalOnlineADHPExtraReceiptsCount = iTotalOnlineADHPExtraReceiptsCount + 1;
                                    iTotalOnlineADHPExtraReceiptsValue = iTotalOnlineADHPExtraReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).ExtraADHPCollectionAmount;
                                } else {
                                    iTotalCashADHPExtraReceiptsCount = iTotalCashADHPExtraReceiptsCount + 1;
                                    iTotalCashADHPExtraReceiptsValue = iTotalCashADHPExtraReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).ExtraADHPCollectionAmount;
                                }
                                SQLite_updateCollectionMember(memberIndex, "Extra", 2, true);
                            }
                            if (mainActivity.CollectionMemberList.get(memberIndex).NewRDCollectionAmount > 0) {
                                iTotalNewRDExtraReceiptsCount = iTotalNewRDExtraReceiptsCount + 1;
                                iTotalNewRDExtraReceiptsValue = iTotalNewRDExtraReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).NewRDCollectionAmount;
                                if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
                                    iTotalOnlineNewRDExtraReceiptsCount = iTotalOnlineNewRDExtraReceiptsCount + 1;
                                    iTotalOnlineNewRDExtraReceiptsValue = iTotalOnlineNewRDExtraReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).NewRDCollectionAmount;
                                } else {
                                    iTotalCashNewRDExtraReceiptsCount = iTotalCashNewRDExtraReceiptsCount + 1;
                                    iTotalCashNewRDExtraReceiptsValue = iTotalCashNewRDExtraReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).NewRDCollectionAmount;
                                }
                                SQLite_updateCollectionMember(memberIndex, "Extra", 3, true);
                            }
                            if (mainActivity.CollectionMemberList.get(memberIndex).FDCollectionAmount > 0) {
                                iTotalFDExtraReceiptsCount = iTotalFDExtraReceiptsCount + 1;
                                iTotalFDExtraReceiptsValue = iTotalFDExtraReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).FDCollectionAmount;
                                if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
                                    iTotalOnlineFDExtraReceiptsCount = iTotalOnlineFDExtraReceiptsCount + 1;
                                    iTotalOnlineFDExtraReceiptsValue = iTotalOnlineFDExtraReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).FDCollectionAmount;
                                } else {
                                    iTotalCashFDExtraReceiptsCount = iTotalCashFDExtraReceiptsCount + 1;
                                    iTotalCashFDExtraReceiptsValue = iTotalCashFDExtraReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).FDCollectionAmount;
                                }
                                SQLite_updateCollectionMember(memberIndex, "Extra", 5, true);
                            }
                            if (mainActivity.CollectionMemberList.get(memberIndex).ExtraJLGCollectionAmount > 0) {
                                iTotalJLGExtraReceiptsCount = iTotalJLGExtraReceiptsCount + 1;
                                iTotalJLGExtraReceiptsValue = iTotalJLGExtraReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).ExtraJLGCollectionAmount;
                                if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
                                    iTotalOnlineJLGExtraReceiptsCount = iTotalOnlineJLGExtraReceiptsCount + 1;
                                    iTotalOnlineJLGExtraReceiptsValue = iTotalOnlineJLGExtraReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).ExtraJLGCollectionAmount;
                                } else {
                                    iTotalCashJLGExtraReceiptsCount = iTotalCashJLGExtraReceiptsCount + 1;
                                    iTotalCashJLGExtraReceiptsValue = iTotalCashJLGExtraReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).ExtraJLGCollectionAmount;
                                }
                                SQLite_updateCollectionMember(memberIndex, "Extra", 9, true);
                            }
                            if (mainActivity.CollectionMemberList.get(memberIndex).ReprintChargesCollectionAmount > 0) {
                                iTotalReprintChargesExtraReceiptsCount = iTotalReprintChargesExtraReceiptsCount + 1;
                                iTotalReprintChargesExtraReceiptsValue = iTotalReprintChargesExtraReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).ReprintChargesCollectionAmount;
                                if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
                                    iTotalOnlineReprintChargesExtraReceiptsCount = iTotalOnlineReprintChargesExtraReceiptsCount + 1;
                                    iTotalOnlineReprintChargesExtraReceiptsValue = iTotalOnlineReprintChargesExtraReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).ReprintChargesCollectionAmount;
                                } else {
                                    iTotalCashReprintChargesExtraReceiptsCount = iTotalCashReprintChargesExtraReceiptsCount + 1;
                                    iTotalCashReprintChargesExtraReceiptsValue = iTotalCashReprintChargesExtraReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).ReprintChargesCollectionAmount;
                                }
                                SQLite_updateCollectionMember(memberIndex, "Extra", 10, true);
                            }
                            iTotalExtraReceiptsCount = iTotalExtraReceiptsCount + 1;
                            iTotalExtraReceiptsValue = iTotalExtraReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).ExtraCollectionAmount;
                            if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
                                iTotalOnlineExtraReceiptsCount = iTotalOnlineExtraReceiptsCount + 1;
                                iTotalOnlineExtraReceiptsValue = iTotalOnlineExtraReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).ExtraCollectionAmount;
                            } else {
                                iTotalCashExtraReceiptsCount = iTotalCashExtraReceiptsCount + 1;
                                iTotalCashExtraReceiptsValue = iTotalCashExtraReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).ExtraCollectionAmount;
                            }
                            clsMemberReceipt memberPDF = new clsMemberReceipt();
                            memberPDF.MembeID = mainActivity.CollectionMemberList.get(memberIndex).MemberID;
                            memberPDF.pdfReceipt = StrPDFReceiptFileName;
                            memberReceipt.add(memberPDF);
                        }
                        if (mainActivity.CollectionMemberList.get(memberIndex).RD1CollectionAmount > 0) {
                            int rdIndex = CenterMemberList.get(i).index1RDReceipts;
                            if (rdIndex >= 0) {
                                String StrPDFReceiptFileName = generateReceipt(memberIndex, rdIndex, "RD");
                                SQLite_updateRDReceipt(memberIndex, rdIndex, true);
                                iTotalRDReceiptsCount = iTotalRDReceiptsCount + 1;
                                iTotalRDReceiptsValue = iTotalRDReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).RD1CollectionAmount;
                                if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
                                    iTotalOnlineRDReceiptsCount = iTotalOnlineRDReceiptsCount + 1;
                                    iTotalOnlineRDReceiptsValue = iTotalOnlineRDReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).RD1CollectionAmount;
                                } else {
                                    iTotalCashRDReceiptsCount = iTotalCashRDReceiptsCount + 1;
                                    iTotalCashRDReceiptsValue = iTotalCashRDReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).RD1CollectionAmount;
                                }
                                clsMemberReceipt memberPDF = new clsMemberReceipt();
                                memberPDF.MembeID = mainActivity.CollectionMemberList.get(memberIndex).MemberID;
                                memberPDF.pdfReceipt = StrPDFReceiptFileName;
                                memberReceipt.add(memberPDF);
                            }
                        }
                        if (mainActivity.CollectionMemberList.get(memberIndex).RD2CollectionAmount > 0) {
                            int rdIndex = CenterMemberList.get(i).index2RDReceipts;
                            if (rdIndex >= 0) {
                                String StrPDFReceiptFileName = generateReceipt(memberIndex, rdIndex, "RD");
                                SQLite_updateRDReceipt(memberIndex, rdIndex, true);
                                iTotalRDReceiptsCount = iTotalRDReceiptsCount + 1;
                                iTotalRDReceiptsValue = iTotalRDReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).RD2CollectionAmount;
                                if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
                                    iTotalOnlineRDReceiptsCount = iTotalOnlineRDReceiptsCount + 1;
                                    iTotalOnlineRDReceiptsValue = iTotalOnlineRDReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).RD2CollectionAmount;
                                } else {
                                    iTotalCashRDReceiptsCount = iTotalCashRDReceiptsCount + 1;
                                    iTotalCashRDReceiptsValue = iTotalCashRDReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).RD2CollectionAmount;
                                }
                                clsMemberReceipt memberPDF = new clsMemberReceipt();
                                memberPDF.MembeID = mainActivity.CollectionMemberList.get(memberIndex).MemberID;
                                memberPDF.pdfReceipt = StrPDFReceiptFileName;
                                memberReceipt.add(memberPDF);
                            }
                        }
                        if (mainActivity.CollectionMemberList.get(memberIndex).RD3CollectionAmount > 0) {
                            int rdIndex = CenterMemberList.get(i).index3RDReceipts;
                            if (rdIndex >= 0) {
                                String StrPDFReceiptFileName = generateReceipt(memberIndex, rdIndex, "RD");
                                SQLite_updateRDReceipt(memberIndex, rdIndex, true);
                                iTotalRDReceiptsCount = iTotalRDReceiptsCount + 1;
                                iTotalRDReceiptsValue = iTotalRDReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).RD3CollectionAmount;
                                if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
                                    iTotalOnlineRDReceiptsCount = iTotalOnlineRDReceiptsCount + 1;
                                    iTotalOnlineRDReceiptsValue = iTotalOnlineRDReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).RD3CollectionAmount;
                                } else {
                                    iTotalCashRDReceiptsCount = iTotalCashRDReceiptsCount + 1;
                                    iTotalCashRDReceiptsValue = iTotalCashRDReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).RD3CollectionAmount;
                                }
                                clsMemberReceipt memberPDF = new clsMemberReceipt();
                                memberPDF.MembeID = mainActivity.CollectionMemberList.get(memberIndex).MemberID;
                                memberPDF.pdfReceipt = StrPDFReceiptFileName;
                                memberReceipt.add(memberPDF);
                            }
                        }
                        if (mainActivity.CollectionMemberList.get(memberIndex).RD4CollectionAmount > 0) {
                            int rdIndex = CenterMemberList.get(i).index4RDReceipts;
                            if (rdIndex >= 0) {
                                String StrPDFReceiptFileName = generateReceipt(memberIndex, rdIndex, "RD");
                                SQLite_updateRDReceipt(memberIndex, rdIndex, true);
                                iTotalRDReceiptsCount = iTotalRDReceiptsCount + 1;
                                iTotalRDReceiptsValue = iTotalRDReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).RD4CollectionAmount;
                                if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
                                    iTotalOnlineRDReceiptsCount = iTotalOnlineRDReceiptsCount + 1;
                                    iTotalOnlineRDReceiptsValue = iTotalOnlineRDReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).RD4CollectionAmount;
                                } else {
                                    iTotalCashRDReceiptsCount = iTotalCashRDReceiptsCount + 1;
                                    iTotalCashRDReceiptsValue = iTotalCashRDReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).RD4CollectionAmount;
                                }
                                clsMemberReceipt memberPDF = new clsMemberReceipt();
                                memberPDF.MembeID = mainActivity.CollectionMemberList.get(memberIndex).MemberID;
                                memberPDF.pdfReceipt = StrPDFReceiptFileName;
                                memberReceipt.add(memberPDF);
                            }
                        }
                        if (mainActivity.CollectionMemberList.get(memberIndex).RD5CollectionAmount > 0) {
                            int rdIndex = CenterMemberList.get(i).index5RDReceipts;
                            if (rdIndex >= 0) {
                                String StrPDFReceiptFileName = generateReceipt(memberIndex, rdIndex, "RD");
                                SQLite_updateRDReceipt(memberIndex, rdIndex, true);
                                iTotalRDReceiptsCount = iTotalRDReceiptsCount + 1;
                                iTotalRDReceiptsValue = iTotalRDReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).RD5CollectionAmount;
                                if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
                                    iTotalOnlineRDReceiptsCount = iTotalOnlineRDReceiptsCount + 1;
                                    iTotalOnlineRDReceiptsValue = iTotalOnlineRDReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).RD5CollectionAmount;
                                } else {
                                    iTotalCashRDReceiptsCount = iTotalCashRDReceiptsCount + 1;
                                    iTotalCashRDReceiptsValue = iTotalCashRDReceiptsValue + mainActivity.CollectionMemberList.get(memberIndex).RD5CollectionAmount;
                                }
                                clsMemberReceipt memberPDF = new clsMemberReceipt();
                                memberPDF.MembeID = mainActivity.CollectionMemberList.get(memberIndex).MemberID;
                                memberPDF.pdfReceipt = StrPDFReceiptFileName;
                                memberReceipt.add(memberPDF);
                            }
                        }
                        if (mainActivity.CollectionMemberList.get(memberIndex).TotalCollectionAmount > 0) {
                            SendCollectionSMS(memberIndex);
                        }
                        SQLite_updateCollectionMember(memberIndex);
                    }

                    //Update Cash Naration to SQLite
                    if (selectedCollectionCenter != null) {
                        SQLite_updateCenterCollection();
                        String strDetails = ("C-" + String.valueOf(selectedCollectionCenter.CenterID)) + ":" +
                                            selectedCollectionCenter.ClusterName + ":" +
                                            selectedCollectionCenter.CommunityName;
                        EditText et = (EditText) rootView.findViewById(R.id.editTextNextRecoveryDate);
                        if(     et.isEnabled() &&
                                (selectedCollectionCenter.lNextRecoveryStartTime != 0) &&
                                (selectedCollectionCenter.lNextRecoveryEndTime != 0)
                        ) {
                            mainActivity.insertSchedule(
                                    "Recovery",
                                    strDetails,
                                    selectedCollectionCenter.lNextRecoveryStartTime,
                                    selectedCollectionCenter.lNextRecoveryEndTime);
                        }

                        iTotalValueOnline = iStartValueOnline + selectedCollectionCenter.CenterOnlineCollectionTotal;
                        iTotalValueCash = iStartValueCash + selectedCollectionCenter.CenterCashCollectionTotal;
                        iTotalValue = iStartValueTotal + selectedCollectionCenter.CenterCollectionTotal;
//                        RefreshCollection();

                        SQLite_updateCollectionSummary(true);
                        String strActivity = "Recovery: " + strDetails;
                        mainActivity.UpdateTabLocation(strActivity);
                    }

                    SavedNarration = true;
                    if(memberReceipt.size() > 0) {
                        EnablePrintCloseOnly();
                    } else {
                        if(selectedCollectionCenter != null) {
                            mainActivity.selectedCollectionCenterID = selectedCollectionCenter.CenterID;
                        } else {
                            mainActivity.selectedCollectionCenterID = 0L;
                        }
                        mainActivity.InitDesktop();
                    }
                }
                break;
            case R.id.buttonSummaryCollection:
                if(mainActivity.flagNewSummary) {
                    SQLite_updateCollectionSummary(true);

                    //CollectionNarration packet added to queue SyncUpdateCollectionNarration
                    CreateAlertDialog(true, false);
                    dlgState = ufin_COLLECTION_DIALOG_STATE_COLLECTION_SUMMARY_GENERATED;
                    dlgAlert.setMessage(MSG_ufin_COLLECTION_SUMMARY_GENERATED);
                    dialog = dlgAlert.create();
                    dialog.show();
                    textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    textView.setTextColor(Color.RED);

                    mainActivity.flagNewSummary = false;
                } else {
                    //Check if all collection packets are uploaded to server
                    iCollectionPacketsPendingUpload = 0;
                    iCollectionDetailsPendingUpload = 0;
                    iRDReceiptPendingUpload = 0;
                    iCollectionSMSPendingUpload = 0;
                    iNarrationPendingUpload = 0;

                    boolean flagPendingCollectionSyncup = false;
                    String strPendingUpload = "";
                    if(!mainActivity.SyncUpdateCollectionDetails.isEmpty()) {
                        iCollectionDetailsPendingUpload
                                = iCollectionDetailsPendingUpload
                                + mainActivity.SyncUpdateCollectionDetails.size();
                        iCollectionPacketsPendingUpload
                                = iCollectionPacketsPendingUpload
                                + mainActivity.SyncUpdateCollectionDetails.size();
                        strPendingUpload = strPendingUpload + "\nCollectionDetails: " + String.valueOf(mainActivity.SyncUpdateCollectionDetails.size());
                        flagPendingCollectionSyncup = true;
                    }
                    if(!mainActivity.SyncUpdateRDReceipt.isEmpty()) {
                        iRDReceiptPendingUpload
                                = iRDReceiptPendingUpload
                                + mainActivity.SyncUpdateRDReceipt.size();
                        iCollectionPacketsPendingUpload
                                = iCollectionPacketsPendingUpload
                                + mainActivity.SyncUpdateRDReceipt.size();
                        strPendingUpload = strPendingUpload + "\nRDReceipt: " + String.valueOf(mainActivity.SyncUpdateRDReceipt.size());
                        flagPendingCollectionSyncup = true;
                    }
                    if(!mainActivity.SyncCollectionSMS.isEmpty()) {
                        iCollectionSMSPendingUpload
                                = iCollectionSMSPendingUpload
                                + mainActivity.SyncCollectionSMS.size();
                        iCollectionPacketsPendingUpload
                                = iCollectionPacketsPendingUpload
                                + mainActivity.SyncCollectionSMS.size();
                        strPendingUpload = strPendingUpload + "\nCollectionSMS: " + String.valueOf(mainActivity.SyncCollectionSMS.size());
                        flagPendingCollectionSyncup = true;
                    }
                    if(!mainActivity.SyncUpdateCollectionNarration.isEmpty()) {
                        iNarrationPendingUpload
                                = iNarrationPendingUpload
                                + mainActivity.SyncUpdateCollectionNarration.size();
                        iCollectionPacketsPendingUpload
                                = iCollectionPacketsPendingUpload
                                + mainActivity.SyncUpdateCollectionNarration.size();
                        strPendingUpload = strPendingUpload + "\nCollectionNarration: " + String.valueOf(mainActivity.SyncUpdateCollectionNarration.size());
                        flagPendingCollectionSyncup = true;
                    }
                    if(!flagPendingCollectionSyncup) {
                        //Check if all collection packets from P.txt file are uploaded and saved in L.txt file
                        mainActivity.CheckForMissingCollectionPackets();
                        if(!mainActivity.SyncUpdateCollectionDetails.isEmpty()) {
                            iCollectionDetailsPendingUpload
                                    = iCollectionDetailsPendingUpload
                                    + mainActivity.SyncUpdateCollectionDetails.size();
                            iCollectionPacketsPendingUpload
                                    = iCollectionPacketsPendingUpload
                                    + mainActivity.SyncUpdateCollectionDetails.size();
                            strPendingUpload = strPendingUpload + "\nCollectionDetails: " + String.valueOf(mainActivity.SyncUpdateCollectionDetails.size());
                            flagPendingCollectionSyncup = true;
                        }
                        if(!mainActivity.SyncUpdateRDReceipt.isEmpty()) {
                            iRDReceiptPendingUpload
                                    = iRDReceiptPendingUpload
                                    + mainActivity.SyncUpdateRDReceipt.size();
                            iCollectionPacketsPendingUpload
                                    = iCollectionPacketsPendingUpload
                                    + mainActivity.SyncUpdateRDReceipt.size();
                            strPendingUpload = strPendingUpload + "\nRDReceipt: " + String.valueOf(mainActivity.SyncUpdateRDReceipt.size());
                            flagPendingCollectionSyncup = true;
                        }
                        if(!mainActivity.SyncCollectionSMS.isEmpty()) {
                            iCollectionSMSPendingUpload
                                    = iCollectionSMSPendingUpload
                                    + mainActivity.SyncCollectionSMS.size();
                            iCollectionPacketsPendingUpload
                                    = iCollectionPacketsPendingUpload
                                    + mainActivity.SyncCollectionSMS.size();
                            strPendingUpload = strPendingUpload + "\nCollectionSMS: " + String.valueOf(mainActivity.SyncCollectionSMS.size());
                            flagPendingCollectionSyncup = true;
                        }
                        if(!mainActivity.SyncUpdateCollectionNarration.isEmpty()) {
                            iNarrationPendingUpload
                                    = iNarrationPendingUpload
                                    + mainActivity.SyncUpdateCollectionNarration.size();
                            iCollectionPacketsPendingUpload
                                    = iCollectionPacketsPendingUpload
                                    + mainActivity.SyncUpdateCollectionNarration.size();
                            strPendingUpload = strPendingUpload + "\nCollectionNarration: " + String.valueOf(mainActivity.SyncUpdateCollectionNarration.size());
                            flagPendingCollectionSyncup = true;
                        }
                    }
                    mainActivity.disconnectPrinter();
                    ReadyToPrint = mainActivity.checkPrinter();
                    if(!ReadyToPrint) {
                        strPendingUpload = strPendingUpload + "\nPrinter Not Connected.";
                    }
                    if (flagPendingCollectionSyncup) {
                        strPendingUpload = MSG_ufin_WAITING_FOR_PACKET_UPLOAD + strPendingUpload;
                    } else if (strPendingUpload.isEmpty()) {
                        strPendingUpload = "Press OK to Generate DCS";
                    }
                    CreateAlertDialog(true, true);
                    dlgState = ufin_COLLECTION_DIALOG_STATE_WAITING_FOR_PACKET_UPLOAD;
                    dlgAlert.setMessage(strPendingUpload);
                    dialog = dlgAlert.create();
                    dialog.show();
                    textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    textView.setTextColor(Color.RED);
                    break;
                }
                break;
        }
    }
//bmp 17-Sep-26
    private class AsyncCallWS_PrintPDF1 extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarCollection);
            if (pbSync != null) {
                pbSync.setVisibility(View.VISIBLE);
            }
            Button bt = rootView.findViewById(R.id.buttonPrintCollection);
            bt.setEnabled(false);
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
        @Override
        protected Void doInBackground(String... strings) {
            long receiptMemberID = memberReceipt.get(memberReceiptIndex).MembeID;
            for(int i=memberReceiptIndex; i<memberReceipt.size(); i++) {
                if(memberReceipt.get(i).MembeID != receiptMemberID)
                    break;
                mainActivity.printPDF(memberReceipt.get(i).pdfReceipt);
                memberReceiptIndex = memberReceiptIndex + 1;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarCollection);
            if (pbSync != null) {
                pbSync.setVisibility(View.INVISIBLE);
            }
            if(memberReceiptIndex < memberReceipt.size()) {
                CreateAlertDialog(true, false);
                dlgState = ufin_COLLECTION_DIALOG_STATE_RESUME_PRINTING;
                dlgAlert.setMessage(MSG_ufin_COLLECTION_RESUME_PRINTING);
                AlertDialog dialog = dlgAlert.create();
                dialog.show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(30);
                textView.setTextColor(Color.RED);
//                        textView.setPadding(0,500,0,0);
//                        textView.setGravity(Gravity.CENTER);
//                        textView.setLayoutParams(new LinearLayout.LayoutParams(
//                                LinearLayout.LayoutParams.MATCH_PARENT,
//                                LinearLayout.LayoutParams.MATCH_PARENT));
//                        WindowManager.LayoutParams lp2 = new WindowManager.LayoutParams();
//                        lp2.copyFrom(dialog.getWindow().getAttributes());
//                        lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
//                        lp2.height = WindowManager.LayoutParams.MATCH_PARENT;
//                        lp2.gravity = Gravity.CENTER_VERTICAL;
//                        dialog.getWindow().setAttributes(lp2);
            } else {
                if(selectedCollectionCenter != null) {
                    mainActivity.selectedCollectionCenterID = selectedCollectionCenter.CenterID;
                } else {
                    mainActivity.selectedCollectionCenterID = 0L;
                }
                Button bt = rootView.findViewById(R.id.buttonPrintCollection);
                bt.setEnabled(true);
                mainActivity.InitDesktop();
            }
        }
    }
    private class AsyncCallWS_PrintPDF2 extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarCollection);
            if (pbSync != null) {
                pbSync.setVisibility(View.VISIBLE);
            }
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
        @Override
        protected Void doInBackground(String... strings) {
            String strMemberFolderpath = mainActivity.baseDir + "/data/receipt/" + String.valueOf(selectedCollectionMember.collectionMember.MemberID);
            File memberFolder = new File(strMemberFolderpath);
            if(memberFolder.exists()) {
                File[] memberFiles = memberFolder.listFiles();
                for (int i = 0; i < memberFiles.length; i++) {
                    mainActivity.printPDF(strMemberFolderpath + "/" + memberFiles[i].getName());
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarCollection);
            if (pbSync != null) {
                pbSync.setVisibility(View.INVISIBLE);
            }
            Button bt = rootView.findViewById(R.id.buttonPrintCollection);
            bt.setEnabled(true);
        }
    }
//bmp 17-Sep-26

//bmp 12-May-26
    public void SQLite_saveOnlineTransactionDoc(int iIndex) {
//bmp 30-Sep-26
//        clsMemberDocRcvd newDocRcvd = new clsMemberDocRcvd();
//        newDocRcvd.Init_MemberDocRcvd();
//        newDocRcvd.MemberDocumentsID = clsDocumentIDs.MEMBER_DOC_ID_ONLINE_COLLECTION;
//        newDocRcvd.MemberID = mainActivity.CollectionMemberList.get(iIndex).MemberID;
//        newDocRcvd.DocumentNo = mainActivity.CollectionMemberList.get(iIndex).strOnlineTransactionID;
//        newDocRcvd.IsActive = 1;
////bmp 04-Jun-26
////        newDocRcvd.MemberDocuemntsRcvdID = mainActivity.SQLite_saveMemberDocRcvd(newDocRcvd, 0, true);
//        Calendar cal = Calendar.getInstance();
//        while (LastTimeInMillis > cal.getTimeInMillis()) {
//            cal = Calendar.getInstance();
//        }
//        LastTimeInMillis = cal.getTimeInMillis();
//        LastTimeInMillis += 1000;
//        newDocRcvd.MemberDocuemntsRcvdID = mainActivity.SQLite_saveMemberDocRcvd(newDocRcvd, LastTimeInMillis, true);
////bmp 04-Jun-26
//        mainActivity.CollectionMemberList.get(iIndex).MemberDocumentRcvdID = newDocRcvd.MemberDocuemntsRcvdID;

//        clsDocumentPhoto newDocPhoto = new clsDocumentPhoto();
//        newDocPhoto.Init_DocumentPhoto();
//        newDocPhoto.MemberDocuemntsRcvdID = newDocRcvd.MemberDocuemntsRcvdID;
//        newDocPhoto.GroupID = 0;
//        newDocPhoto.MemberDocumentsID = clsDocumentIDs.MEMBER_DOC_ID_ONLINE_COLLECTION;
////bmp 04-Jun-26
////        newDocPhoto.DocumentPhotoID = mainActivity.SQLite_saveDocumentPhoto(newDocPhoto, 0, true);
//        while (LastTimeInMillis > cal.getTimeInMillis()) {
//            cal = Calendar.getInstance();
//        }
//        LastTimeInMillis = cal.getTimeInMillis();
//        LastTimeInMillis += 1000;
//        newDocPhoto.DocumentPhotoID = mainActivity.SQLite_saveDocumentPhoto(newDocPhoto, LastTimeInMillis, true);
//bmp 04-Jun-26
//        String strFilepath = mainActivity.baseDir + "/data/";
//        String strFilename = mainActivity.CollectionMemberList.get(iIndex).strOnlinePaymentProof;
//        File onlinepaymentFile = new File(strFilepath + strFilename);
//        if (onlinepaymentFile.exists()) {
//            try {
//                InputStream in = null;
//                OutputStream outmemberdata = null;
//                OutputStream outmemberupload = null;
//                byte[] buffer = new byte[1024];
//                File memberdataFile = new File(mainActivity.baseDir + "/data/" + String.valueOf(newDocPhoto.DocumentPhotoID) + ".jpg");
//                File memberuploadFile = new File(mainActivity.baseDir + "/upload/" + String.valueOf(newDocPhoto.DocumentPhotoID) + ".jpg");
//                in = new FileInputStream(onlinepaymentFile);
//                outmemberdata = new FileOutputStream(memberdataFile);
//                outmemberupload = new FileOutputStream(memberuploadFile);
//                int read;
//                while ((read = in.read(buffer)) != -1) {
//                    outmemberdata.write(buffer, 0, read);
//                    outmemberupload.write(buffer, 0, read);
//                }
//                in.close();
//                in = null;
//                outmemberdata.flush();
//                outmemberdata.close();
//                outmemberdata = null;
//                outmemberupload.flush();
//                outmemberupload.close();
//                outmemberupload = null;
//
//                String strDocUploadFilename = String.valueOf(newDocPhoto.DocumentPhotoID) + ".jpg";
//                mainActivity.SyncDocUpload.add(strDocUploadFilename);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            onlinepaymentFile.delete();
//        }
//bmp 30-Sep-26
        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        try {
            ContentValues values_online = new ContentValues();
            values_online.put(ufinDbContract.OnlineCollection.COLUMN_NAME_MemberID, mainActivity.CollectionMemberList.get(iIndex).MemberID);
            values_online.put(ufinDbContract.OnlineCollection.COLUMN_NAME_OnlinePaymentByMemberID, mainActivity.CollectionMemberList.get(iIndex).OnlinePaymentByMemberID);
            values_online.put(ufinDbContract.OnlineCollection.COLUMN_NAME_OnlinePaymentByName, mainActivity.CollectionMemberList.get(iIndex).strOnlinePaymentByName);
            values_online.put(ufinDbContract.OnlineCollection.COLUMN_NAME_OnlinePaymentByRelationID, mainActivity.CollectionMemberList.get(iIndex).OnlinePaymentByRelationID);
//bmp 30-Sep-26
//            values_online.put(ufinDbContract.OnlineCollection.COLUMN_NAME_OnlinePaymentProof, mainActivity.CollectionMemberList.get(iIndex).strOnlinePaymentProof);
            values_online.put(ufinDbContract.OnlineCollection.COLUMN_NAME_OnlinePaymentProof, "");
//            values_online.put(ufinDbContract.OnlineCollection.COLUMN_NAME_MemberDocumentRcvdID, mainActivity.CollectionMemberList.get(iIndex).MemberDocumentRcvdID);
            values_online.put(ufinDbContract.OnlineCollection.COLUMN_NAME_MemberDocumentRcvdID, 0L);
//bmp 30-Sep-26
            values_online.put(ufinDbContract.OnlineCollection.COLUMN_NAME_OnlinePaymentUPIID, mainActivity.CollectionMemberList.get(iIndex).strOnlinePaymentUPIID);
            values_online.put(ufinDbContract.OnlineCollection.COLUMN_NAME_OnlinePaymentTransactionID, mainActivity.CollectionMemberList.get(iIndex).strOnlineTransactionID);
            values_online.put(ufinDbContract.OnlineCollection.COLUMN_NAME_OnlinePaymentTransactionDate, mainActivity.CollectionMemberList.get(iIndex).strOnlineTransactionDate);
            values_online.put(ufinDbContract.OnlineCollection.COLUMN_NAME_OnlineTransactionAmount, mainActivity.CollectionMemberList.get(iIndex).iOnlineTransactionAmount);

            String whereClause = ufinDbContract.OnlineCollection.COLUMN_NAME_MemberID + "=" + "'" + String.valueOf(mainActivity.CollectionMemberList.get(iIndex).MemberID) + "'"
                    + " AND " + ufinDbContract.OnlineCollection.COLUMN_NAME_OnlinePaymentTransactionID + "=" + "'" + mainActivity.CollectionMemberList.get(iIndex).strOnlineTransactionID + "'";
            long lTemp = db.update(ufinDbContract.OnlineCollection.TABLE_NAME, values_online, whereClause, null);
            if (lTemp > 0) {
                System.out.println("OnlineCollection Updated.");
            } else {
                lTemp = db.insert(ufinDbContract.OnlineCollection.TABLE_NAME, null, values_online);
                System.out.println("OnlineCollection Added.");
            }
        } finally {
            db.close();
        }
    }
//bmp 12-May-26
    public void AllowMemberCollectionSelection(boolean flag) {
        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD1);
        cb.setEnabled(flag);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD2);
        cb.setEnabled(flag);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD3);
        cb.setEnabled(flag);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD4);
        cb.setEnabled(flag);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD5);
        cb.setEnabled(flag);
//bmp 05-Aug-26
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectJLG);
        cb.setEnabled(flag);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectNidhi);
        cb.setEnabled(flag);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectADHP);
        cb.setEnabled(flag);
//bmp 05-Aug-26
    }
    public void EnablePrintCloseOnly() {
        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerCollectionDate);
        sp.setEnabled(false);
        sp = (Spinner) rootView.findViewById(R.id.spinnerCluster);
        sp.setEnabled(false);
        sp = (Spinner) rootView.findViewById(R.id.spinnerCommunity);
        sp.setEnabled(false);
        sp = (Spinner) rootView.findViewById(R.id.spinnerCollectionCenterInfo);
        sp.setEnabled(false);
        sp = (Spinner) rootView.findViewById(R.id.spinnerCollectionMember);
        sp.setEnabled(false);
        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxPhoneNo);
        cb.setEnabled(false);
        EditText et = (EditText) rootView.findViewById(R.id.editTextPhoneNo);
        et.setEnabled(false);
        CheckBox cbCash = (CheckBox) rootView.findViewById(R.id.checkBoxPaymentCash);
        cbCash.setEnabled(false);
        CheckBox cbOnline = (CheckBox) rootView.findViewById(R.id.checkBoxPaymentOnline);
        cbOnline.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextTotalCollection);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextLoanEMICollection);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextJLGCollection);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextNidhiCollection);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextADHPCollection);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextRDCollection1);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextRDCollection2);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextRDCollection3);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextRDCollection4);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextRDCollection5);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextAdditionalRepay);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextExtraADHP);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextNewRD);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextFDCollection);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextExtraJLG);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextReprintCharges);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextCenterCount500);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextCenterCount200);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextCenterCount100);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextCenterCount50);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextCenterCount20);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextCenterCount10);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextCenterCount5);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextCenterCount2);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextCenterCount1);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextNextRecoveryDate);
        et.setEnabled(false);
        et = (EditText) rootView.findViewById(R.id.editTextNextRecoveryTime);
        et.setEnabled(false);
//bmp 05-Aug-26
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectJLG);
        cb.setEnabled(false);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectNidhi);
        cb.setEnabled(false);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectADHP);
        cb.setEnabled(false);
//bmp 05-Aug-26
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD1);
        cb.setEnabled(false);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD2);
        cb.setEnabled(false);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD3);
        cb.setEnabled(false);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD4);
        cb.setEnabled(false);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectRD5);
        cb.setEnabled(false);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectAdditionalRepay);
        cb.setEnabled(false);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectExtraADHP);
        cb.setEnabled(false);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectNewRD);
        cb.setEnabled(false);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectFDCollection);
        cb.setEnabled(false);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectExtraJLG);
        cb.setEnabled(false);
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSelectReprintCharges);
        cb.setEnabled(false);
        Button bt = (Button) rootView.findViewById(R.id.buttonSaveCollection);
        bt.setEnabled(false);
        bt = (Button) rootView.findViewById(R.id.buttonSummaryCollection);
        bt.setEnabled(false);
        bt = (Button) rootView.findViewById(R.id.buttonPrintCollection);
        bt.setEnabled(true);
        bt = (Button) rootView.findViewById(R.id.buttonCloseCollection);
        bt.setEnabled(true);
        memberReceiptIndex = 0;
    }
    public boolean SQLite_getCollectionSummary() {
        boolean Status = false;

        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String strDate = sdf.format(date);// all done

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.CollectionSummary.COLUMN_NAME_CollectionID,
                ufinDbContract.CollectionSummary.COLUMN_NAME_CollectionDate,
                ufinDbContract.CollectionSummary.COLUMN_NAME_Total500NotesCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_Total200NotesCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_Total100NotesCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_Total50NotesCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_Total20NotesCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_Total10NotesCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_Total5NotesCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_Total2NotesCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_Total1NotesCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalValueOnline,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalValueCash,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCollectionValue,

                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalEMIReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalEMIReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalADHPReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalADHPReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalRDReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalRDReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalAdditionalRepayReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalAdditionalRepayReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalExtraReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalExtraReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalPreClosureAdditionalEMIReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalPreClosureAdditionalEMIReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalADHPExtraReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalADHPExtraReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalNewRDExtraReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalNewRDExtraReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalFDExtraReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalFDExtraReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalJLGExtraReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalJLGExtraReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalReprintChargesExtraReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalReprintChargesExtraReceiptsValue,

                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashEMIReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashEMIReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashADHPReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashADHPReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashRDReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashRDReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashAdditionalRepayReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashAdditionalRepayReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashExtraReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashExtraReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashPreClosureAdditionalEMIReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashPreClosureAdditionalEMIReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashADHPExtraReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashADHPExtraReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashNewRDExtraReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashNewRDExtraReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashFDExtraReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashFDExtraReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashJLGExtraReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashJLGExtraReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashReprintChargesExtraReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashReprintChargesExtraReceiptsValue,

                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineEMIReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineEMIReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineADHPReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineADHPReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineRDReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineRDReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineAdditionalRepayReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineAdditionalRepayReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineExtraReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineExtraReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlinePreClosureAdditionalEMIReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlinePreClosureAdditionalEMIReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineADHPExtraReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineADHPExtraReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineNewRDExtraReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineNewRDExtraReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineFDExtraReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineFDExtraReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineJLGExtraReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineJLGExtraReceiptsValue,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineReprintChargesExtraReceiptsCount,
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineReprintChargesExtraReceiptsValue,
        };
        String selection1 = ufinDbContract.CollectionSummary.COLUMN_NAME_CollectionDate + "=" + "'" + strDate + "'";

        Cursor c1 = db.query(
                ufinDbContract.CollectionSummary.TABLE_NAME,  // The table to query
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
                        lCollectionID = Long.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_CollectionID)));
                        strCollectionDate = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_CollectionDate));

                        iStartCount500Notes = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_Total500NotesCount)));
                        iStartCount200Notes = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_Total200NotesCount)));
                        iStartCount100Notes = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_Total100NotesCount)));
                        iStartCount50Notes = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_Total50NotesCount)));
                        iStartCount20Notes = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_Total20NotesCount)));
                        iStartCount10Notes = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_Total10NotesCount)));
                        iStartCount5Notes = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_Total5NotesCount)));
                        iStartCount2Notes = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_Total2NotesCount)));
                        iStartCount1Notes = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_Total1NotesCount)));
                        iStartValueOnline = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalValueOnline)));
                        iStartValueCash = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalValueCash)));
                        iStartValueTotal = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCollectionValue)));

                        iTotalEMIReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalEMIReceiptsCount)));
                        iTotalEMIReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalEMIReceiptsValue)));
                        iTotalADHPReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalADHPReceiptsCount)));
                        iTotalADHPReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalADHPReceiptsValue)));
                        iTotalRDReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalRDReceiptsCount)));
                        iTotalRDReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalRDReceiptsValue)));
                        iTotalAdditionalEMIReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalAdditionalRepayReceiptsCount)));
                        iTotalAdditionalEMIReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalAdditionalRepayReceiptsValue)));
                        iTotalExtraReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalExtraReceiptsCount)));
                        iTotalExtraReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalExtraReceiptsValue)));
                        iTotalPreClosureAdditionalEMIReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalPreClosureAdditionalEMIReceiptsCount)));
                        iTotalPreClosureAdditionalEMIReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalPreClosureAdditionalEMIReceiptsValue)));
                        iTotalADHPExtraReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalADHPExtraReceiptsCount)));
                        iTotalADHPExtraReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalADHPExtraReceiptsValue)));
                        iTotalNewRDExtraReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalNewRDExtraReceiptsCount)));
                        iTotalNewRDExtraReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalNewRDExtraReceiptsValue)));
                        iTotalFDExtraReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalFDExtraReceiptsCount)));
                        iTotalFDExtraReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalFDExtraReceiptsValue)));
                        iTotalJLGExtraReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalJLGExtraReceiptsCount)));
                        iTotalJLGExtraReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalJLGExtraReceiptsValue)));
                        iTotalReprintChargesExtraReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalReprintChargesExtraReceiptsCount)));
                        iTotalReprintChargesExtraReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalReprintChargesExtraReceiptsValue)));

                        iTotalCashEMIReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashEMIReceiptsCount)));
                        iTotalCashEMIReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashEMIReceiptsValue)));
                        iTotalCashADHPReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashADHPReceiptsCount)));
                        iTotalCashADHPReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashADHPReceiptsValue)));
                        iTotalCashRDReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashRDReceiptsCount)));
                        iTotalCashRDReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashRDReceiptsValue)));
                        iTotalCashAdditionalEMIReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashAdditionalRepayReceiptsCount)));
                        iTotalCashAdditionalEMIReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashAdditionalRepayReceiptsValue)));
                        iTotalCashExtraReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashExtraReceiptsCount)));
                        iTotalCashExtraReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashExtraReceiptsValue)));
                        iTotalCashPreClosureAdditionalEMIReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashPreClosureAdditionalEMIReceiptsCount)));
                        iTotalCashPreClosureAdditionalEMIReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashPreClosureAdditionalEMIReceiptsValue)));
                        iTotalCashADHPExtraReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashADHPExtraReceiptsCount)));
                        iTotalCashADHPExtraReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashADHPExtraReceiptsValue)));
                        iTotalCashNewRDExtraReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashNewRDExtraReceiptsCount)));
                        iTotalCashNewRDExtraReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashNewRDExtraReceiptsValue)));
                        iTotalCashFDExtraReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashFDExtraReceiptsCount)));
                        iTotalCashFDExtraReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashFDExtraReceiptsValue)));
                        iTotalCashJLGExtraReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashJLGExtraReceiptsCount)));
                        iTotalCashJLGExtraReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashJLGExtraReceiptsValue)));
                        iTotalCashReprintChargesExtraReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashReprintChargesExtraReceiptsCount)));
                        iTotalCashReprintChargesExtraReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashReprintChargesExtraReceiptsValue)));

                        iTotalOnlineEMIReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineEMIReceiptsCount)));
                        iTotalOnlineEMIReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineEMIReceiptsValue)));
                        iTotalOnlineADHPReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineADHPReceiptsCount)));
                        iTotalOnlineADHPReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineADHPReceiptsValue)));
                        iTotalOnlineRDReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineRDReceiptsCount)));
                        iTotalOnlineRDReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineRDReceiptsValue)));
                        iTotalOnlineAdditionalEMIReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineAdditionalRepayReceiptsCount)));
                        iTotalOnlineAdditionalEMIReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineAdditionalRepayReceiptsValue)));
                        iTotalOnlineExtraReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineExtraReceiptsCount)));
                        iTotalOnlineExtraReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineExtraReceiptsValue)));
                        iTotalOnlinePreClosureAdditionalEMIReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlinePreClosureAdditionalEMIReceiptsCount)));
                        iTotalOnlinePreClosureAdditionalEMIReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlinePreClosureAdditionalEMIReceiptsValue)));
                        iTotalOnlineADHPExtraReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineADHPExtraReceiptsCount)));
                        iTotalOnlineADHPExtraReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineADHPExtraReceiptsValue)));
                        iTotalOnlineNewRDExtraReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineNewRDExtraReceiptsCount)));
                        iTotalOnlineNewRDExtraReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineNewRDExtraReceiptsValue)));
                        iTotalOnlineFDExtraReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineFDExtraReceiptsCount)));
                        iTotalOnlineFDExtraReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineFDExtraReceiptsValue)));
                        iTotalOnlineJLGExtraReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineJLGExtraReceiptsCount)));
                        iTotalOnlineJLGExtraReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineJLGExtraReceiptsValue)));
                        iTotalOnlineReprintChargesExtraReceiptsCount = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineReprintChargesExtraReceiptsCount)));
                        iTotalOnlineReprintChargesExtraReceiptsValue = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineReprintChargesExtraReceiptsValue)));

                        Status = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            if(c1 != null) { c1.close(); }
            db.close();
        }

        return Status;
    }
    public boolean SQLite_updateCollectionMember(int memberIndex) {
        boolean Status = false;

        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        String strTimeStamp = sdf.format(date);// all done

        try {
            ContentValues values1 = new ContentValues();
            values1.put(ufinDbContract.MemberRecovery.COLUMN_NAME_MemberStatus, mainActivity.CollectionMemberList.get(memberIndex).MemberStatus);
            values1.put(ufinDbContract.MemberRecovery.COLUMN_NAME_PhoneNo, mainActivity.CollectionMemberList.get(memberIndex).PhoneNo);
            long lTemp1 = 0;
            String whereClause = ufinDbContract.MemberRecovery.COLUMN_NAME_SavingAccNo + "=" + "'" + mainActivity.CollectionMemberList.get(memberIndex).SavingAccNo + "'";
            lTemp1 = db.update(ufinDbContract.MemberRecovery.TABLE_NAME, values1, whereClause, null);
            if (lTemp1 > 0) {
                System.out.println("MemberRecovery Updated.");
            }
//bmp 18-Jun-26 ????????????????????????????????
//Avoid update ufinDbContract.MemberTransaction.TABLE_NAME only insert new transactions on save
//            ContentValues values2 = new ContentValues();
//            values2.put(ufinDbContract.MemberTransaction.COLUMN_NAME_TotalDueAmountsToPay, mainActivity.CollectionMemberList.get(memberIndex).TotalDueAmountsToPayRefday);
//            long lTemp2 = 0;
////bmp 10-Jun-26 ????????????????????????????????
////            String whereClause2 = ufinDbContract.MemberTransaction.COLUMN_NAME_MemberID + "=" + "'" + String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).MemberID) + "'";
//            String whereClause2 = ufinDbContract.MemberTransaction.COLUMN_NAME_MemberID + "=" + "'" + String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).MemberID) + "'"
//                    + " AND " + ufinDbContract.MemberTransaction.COLUMN_NAME_TransactionTimestamp + "=" + "'" + String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).TransactionTimestamp) + "'";
////bmp 10-Jun-26 ????????????????????????????????
//            lTemp2 = db.update(ufinDbContract.MemberTransaction.TABLE_NAME, values2, whereClause2, null);
//            if (lTemp2 > 0) {
//                System.out.println("MemberTransaction Updated.");
//            }
//bmp 18-Jun-26 ????????????????????????????????
        } finally {
            db.close();
        }

        return Status;
    }

    public String generateDCS() {
//bmp 20-Jul-26
//        int pageHeight = 1450;
        int pageHeight = 1500;
//bmp 20-Jul-26
        // create a new document
        PdfDocument document = new PdfDocument();
        // create a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(576, pageHeight, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        // draw something on the page

        String strReceiptFolderpath = mainActivity.baseDir + "/data/receipt";
        File receiptFolder = new File(strReceiptFolderpath);
        if(!receiptFolder.exists()) {
            receiptFolder.mkdir();
        }

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String strDate = sdf.format(date);// all done

        String StrPDFDCSFileName = strReceiptFolderpath + "/DCS_" + strDate + ".pdf";
        drawPageDCS(page);

        // finish the page
        document.finishPage(page);
        // add more pages
        // write the document content

        File PdfFile = new File(StrPDFDCSFileName);
        try {
            document.writeTo(new FileOutputStream(PdfFile));
        } catch (FileNotFoundException e) {
        } catch (IOException e1) {
        } finally {
            // close the document
            document.close();
        }

        System.out.println("PDF DCS: " + StrPDFDCSFileName);
        return StrPDFDCSFileName;
    }
    public String generateReceipt(int memberIndex, int rdIndex, String strModule) {
//bmp 23-Jul-26
//        int pageHeight = 1600;
        int pageHeight = 1700;
//bmp 23-Jul-26

        if(strModule.equalsIgnoreCase("ADHP"))
//bmp 23-Jul-26
//            pageHeight = 1200;
            pageHeight = 1300;
//bmp 23-Jul-26
        else if(strModule.equalsIgnoreCase("RD"))
            pageHeight = 1100;
        else if(strModule.equalsIgnoreCase("Extra"))
            pageHeight = 1200;
//bmp 08-Jul-26
        if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Cash")) {
            pageHeight = 1300;
            if(strModule.equalsIgnoreCase("ADHP"))
//bmp 20-Jul-26
//                pageHeight = 1000;
                pageHeight = 1100;
//bmp 20-Jul-26
            else if(strModule.equalsIgnoreCase("RD"))
                pageHeight = 800;
            else if(strModule.equalsIgnoreCase("Extra"))
                pageHeight = 900;
        }
//bmp 08-Jul-26
        // create a new document
        PdfDocument document = new PdfDocument();
        // create a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(576, pageHeight, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        // draw something on the page

        String strReceiptFolderpath = mainActivity.baseDir + "/data/receipt";
        File receiptFolder = new File(strReceiptFolderpath);
        if(!receiptFolder.exists()) {
            receiptFolder.mkdir();
        }
        String strMemberFolderpath = strReceiptFolderpath + "/" + String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).MemberID);
        File memberFolder = new File(strMemberFolderpath);
        if(!memberFolder.exists()) {
            memberFolder.mkdir();
        }
        String strReceiptPath = strMemberFolderpath + "/";

        String StrPDFReceiptFileName = strReceiptPath + String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).MemberID) + ".pdf";
        if(strModule.equalsIgnoreCase("Loan")) {
            drawPageEMIReceipt(page, memberIndex);
            StrPDFReceiptFileName = strReceiptPath +
                                    String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).MemberID) +
                                    "_" + mainActivity.CollectionMemberList.get(memberIndex).LastEMIReceiptNumber +
                                    "_" + strModule + ".pdf";
        } else if(strModule.equalsIgnoreCase("AdditionalRepay")) {
            drawPageAdditionalEMIReceipt(page, memberIndex);
            StrPDFReceiptFileName = strReceiptPath +
                                    String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).MemberID) +
                                    "_" + mainActivity.CollectionMemberList.get(memberIndex).LastEMIReceiptNumber +
                                    "_" + strModule + ".pdf";
        } else if(strModule.equalsIgnoreCase("ADHP")) {
            drawPageADHPReceipt(page, memberIndex);
            StrPDFReceiptFileName = strReceiptPath +
                                    String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).ADHPDetailsID) +
                                    "_" + mainActivity.CollectionMemberList.get(memberIndex).LastADHPReceiptNumber +
                                    "_" + strModule + ".pdf";
        } else if(strModule.equalsIgnoreCase("RD")) {
            drawPageRDReceipt(page, memberIndex, rdIndex);
            StrPDFReceiptFileName = strReceiptPath +
                                    String.valueOf(mainActivity.RDReceiptList.get(rdIndex).RDDetailsID) +
                                    "_" + mainActivity.RDReceiptList.get(rdIndex).LastReceiptNumber +
                                    "_" + strModule + ".pdf";
        } else if(strModule.equalsIgnoreCase("Extra")) {
            drawPageExtraReceipt(page, memberIndex);
            StrPDFReceiptFileName = strReceiptPath +
                                    String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).MemberID) +
                                    "_" + mainActivity.CollectionMemberList.get(memberIndex).LastExtraReceiptNumber +
                                    "_" + strModule + ".pdf";
        }

        // finish the page
        document.finishPage(page);
        // add more pages
        // write the document content

        File PdfFile = new File(StrPDFReceiptFileName);
        try {
            document.writeTo(new FileOutputStream(PdfFile));
        } catch (FileNotFoundException e) {
        } catch (IOException e1) {
        } finally {
            // close the document
            document.close();
        }

        System.out.println("PDF Receipt: " + StrPDFReceiptFileName);
        return StrPDFReceiptFileName;
    }
    public void drawPageDCS(PdfDocument.Page page) {
        Paint text_paint = new Paint();
        text_paint.setColor(Color.BLACK);
        Typeface tf = Typeface.create("Arial", Typeface.NORMAL);
        text_paint.setTypeface(tf);
        text_paint.setTextSize(40);

        Paint number_paint = new Paint();
        number_paint.setColor(Color.BLACK);
        number_paint.setTypeface(tf);
        number_paint.setTextAlign(Paint.Align.RIGHT);
        number_paint.setTextSize(20);

        Paint border_paint = new Paint();
        border_paint.setColor(Color.BLACK);
        border_paint.setStrokeWidth((float) 3.0);

        Canvas canvas = page.getCanvas();

        canvas.drawColor(Color.WHITE);

        canvas.drawLine(0, 0, 575, 0, border_paint);
//        text_paint.setTextSize(26);
        text_paint.setTextSize(24);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_OrgName)), 20, 40, text_paint);
//        canvas.drawText(String.format("%s", getString(R.string.Receipt_OrgRegNo)), 80, 80, text_paint);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_OrgRegNo)), 120, 80, text_paint);
        canvas.drawLine(5, 95, 570, 95, border_paint);
        text_paint.setTextSize(24);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
//bmp 08-Jul-26
//        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH);
//bmp 08-Jul-26
        String strDate = sdf.format(date);
        String strTime = sdf2.format(date);
        long lToday = cal.getTimeInMillis();
        canvas.drawText(String.format("%s", "डेली कलेक्शन समरी (DCS)"), 130, 120, text_paint);

        canvas.drawText(String.format("%s", "ब्रँच"), 0, 160, text_paint);
        String strBranchName = mainActivity.LSO_Branch;
        String[] strSplit = strBranchName.split(" ");
        if(strSplit.length >= 2)
            strBranchName = strSplit[1];
        canvas.drawText(String.format("%s%s", ":", strBranchName), 50, 160, text_paint);
        canvas.drawText(String.format("%s%s", "दिनांक:", strDate), 350, 160, text_paint);

        canvas.drawText(String.format("%s", "LSO"), 0, 190, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.LoginID), 50, 190, text_paint);
        canvas.drawText(String.format("%s%s", "वेळ:", strTime), 350, 190, text_paint);

        //Horizontal lines
        canvas.drawLine(0, 200, 575, 200, border_paint);
//        canvas.drawLine(0, 240, 575, 240, border_paint);
        canvas.drawLine(0, 270, 575, 270, border_paint);
        canvas.drawLine(0, 310, 575, 310, border_paint);
        canvas.drawLine(0, 350, 575, 350, border_paint);
        canvas.drawLine(0, 390, 575, 390, border_paint);
        canvas.drawLine(0, 430, 575, 430, border_paint);
        canvas.drawLine(0, 470, 575, 470, border_paint);
        canvas.drawLine(0, 510, 575, 510, border_paint);
        canvas.drawLine(0, 550, 575, 550, border_paint);
        canvas.drawLine(0, 590, 575, 590, border_paint);
        canvas.drawLine(0, 630, 575, 630, border_paint);
        canvas.drawLine(0, 670, 575, 670, border_paint);
        canvas.drawLine(0, 710, 575, 710, border_paint);
        //Vertical lines
//bmp 14-May-26
//        canvas.drawLine(0, 200, 0, 680, border_paint);
//        canvas.drawLine(275, 200, 275, 680, border_paint);
//        canvas.drawLine(425, 200, 425, 680, border_paint);
//        canvas.drawLine(575, 200, 575, 680, border_paint);
        canvas.drawLine(0, 200, 0, 710, border_paint);
        canvas.drawLine(125, 200, 125, 710, border_paint);
        canvas.drawLine(185, 200, 185, 710, border_paint);
        canvas.drawLine(275, 200, 275, 710, border_paint);
        canvas.drawLine(335, 200, 335, 710, border_paint);
        canvas.drawLine(425, 200, 425, 710, border_paint);
        canvas.drawLine(485, 200, 485, 710, border_paint);
        canvas.drawLine(575, 200, 575, 710, border_paint);
//bmp 14-May-26

//bmp 14-May-26
//        canvas.drawText(String.format("%s", "कलेक्शन"), 100, 225, text_paint);
//        canvas.drawText(String.format("%s", "एकूण पावत्या"), 290, 225, text_paint);
//        canvas.drawText(String.format("%s", "एकूण रक्कम"), 440, 225, text_paint);
        text_paint.setTextSize(18);
        canvas.drawText(String.format("%s", "कलेक्शन"), 30, 225, text_paint);
        canvas.drawText(String.format("%s", "कॅश"), 130, 225, text_paint);
        canvas.drawText(String.format("%s", "पावत्या"), 130, 250, text_paint);
        canvas.drawText(String.format("%s", "कॅश"), 200, 225, text_paint);
        canvas.drawText(String.format("%s", "रक्कम"), 200, 250, text_paint);
        canvas.drawText(String.format("%s", "Online"), 280, 225, text_paint);
        canvas.drawText(String.format("%s", "पावत्या"), 280, 250, text_paint);
        canvas.drawText(String.format("%s", "Online"), 350, 225, text_paint);
        canvas.drawText(String.format("%s", "रक्कम"), 350, 250, text_paint);
        canvas.drawText(String.format("%s", "एकूण"), 430, 225, text_paint);
        canvas.drawText(String.format("%s", "पावत्या"), 430, 250, text_paint);
        canvas.drawText(String.format("%s", "एकूण"), 500, 225, text_paint);
        canvas.drawText(String.format("%s", "रक्कम"), 500, 250, text_paint);
//bmp 14-May-26

//        canvas.drawText(String.format("%s", "कर्ज हप्ता"), 20, 265, text_paint);
//        canvas.drawText(String.format("%s", "आधारपूर्णा"), 20, 305, text_paint);
//        canvas.drawText(String.format("%s", "आर. डी."), 20, 345, text_paint);
//        canvas.drawText(String.format("%s", "जास्तीचा हप्ता"), 20, 385, text_paint);
//        canvas.drawText(String.format("%s", "मुदतपूर्व खाते बंद"), 20, 425, text_paint);
//        canvas.drawText(String.format("%s", "जास्तीचे आधारपूर्णा"), 20, 465, text_paint);
//        canvas.drawText(String.format("%s", "नवीन आर.डी."), 20, 505, text_paint);
//        canvas.drawText(String.format("%s", "फिक्स डिपॉझिट"), 20, 545, text_paint);
//        canvas.drawText(String.format("%s", "जास्तीचे जे.एल.जी."), 20, 585, text_paint);
//        canvas.drawText(String.format("%s", "रिप्रिंट शुल्क"), 20, 625, text_paint);
//        canvas.drawText(String.format("%s", "एकूण"), 20, 665, text_paint);
        canvas.drawText(String.format("%s", "कर्ज हप्ता"), 0, 295, text_paint);
        canvas.drawText(String.format("%s", "आधारपूर्णा"), 0, 335, text_paint);
        canvas.drawText(String.format("%s", "आर. डी."), 0, 375, text_paint);
        canvas.drawText(String.format("%s", "जास्तीचा हप्ता"), 0, 415, text_paint);
        canvas.drawText(String.format("%s", "मुदतपूर्व खाते बंद"), 0, 455, text_paint);
        canvas.drawText(String.format("%s", "जास्तीचे आधारपूर्णा"), 0, 495, text_paint);
        canvas.drawText(String.format("%s", "नवीन आर.डी."), 0, 535, text_paint);
        canvas.drawText(String.format("%s", "फिक्स डिपॉझिट"), 0, 575, text_paint);
        canvas.drawText(String.format("%s", "जास्तीचे जे.एल.जी."), 0, 615, text_paint);
        canvas.drawText(String.format("%s", "रिप्रिंट शुल्क"), 0, 655, text_paint);
        canvas.drawText(String.format("%s", "एकूण"), 0, 695, text_paint);

        DecimalFormat df = new DecimalFormat("##,##,###");
        canvas.drawText(String.format("%s", df.format(iTotalCashEMIReceiptsCount)), 180, 295, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalCashEMIReceiptsValue)), 270, 295, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalCashADHPReceiptsCount)), 180, 335, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalCashADHPReceiptsValue)), 270, 335, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalCashRDReceiptsCount)), 180, 375, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalCashRDReceiptsValue)), 270, 375, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalCashAdditionalEMIReceiptsCount)), 180, 415, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalCashAdditionalEMIReceiptsValue)), 270, 415, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalCashPreClosureAdditionalEMIReceiptsCount)), 180, 455, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalCashPreClosureAdditionalEMIReceiptsValue)), 270, 455, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalCashADHPExtraReceiptsCount)), 180, 495, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalCashADHPExtraReceiptsValue)), 270, 495, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalCashNewRDExtraReceiptsCount)), 180, 535, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalCashNewRDExtraReceiptsValue)), 270, 535, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalCashFDExtraReceiptsCount)), 180, 575, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalCashFDExtraReceiptsValue)), 270, 575, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalCashJLGExtraReceiptsCount)), 180, 615, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalCashJLGExtraReceiptsValue)), 270, 615, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalCashReprintChargesExtraReceiptsCount)), 180, 655, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalCashReprintChargesExtraReceiptsValue)), 270, 655, number_paint);

        canvas.drawText(String.format("%s", df.format(iTotalOnlineEMIReceiptsCount)), 330, 295, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalOnlineEMIReceiptsValue)), 420, 295, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalOnlineADHPReceiptsCount)), 330, 335, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalOnlineADHPReceiptsValue)), 420, 335, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalOnlineRDReceiptsCount)), 330, 375, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalOnlineRDReceiptsValue)), 420, 375, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalOnlineAdditionalEMIReceiptsCount)), 330, 415, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalOnlineAdditionalEMIReceiptsValue)), 420, 415, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalOnlinePreClosureAdditionalEMIReceiptsCount)), 330, 455, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalOnlinePreClosureAdditionalEMIReceiptsValue)), 420, 455, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalOnlineADHPExtraReceiptsCount)), 330, 495, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalOnlineADHPExtraReceiptsValue)), 420, 495, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalOnlineNewRDExtraReceiptsCount)), 330, 535, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalOnlineNewRDExtraReceiptsValue)), 420, 535, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalOnlineFDExtraReceiptsCount)), 330, 575, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalOnlineFDExtraReceiptsValue)), 420, 575, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalOnlineJLGExtraReceiptsCount)), 330, 615, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalOnlineJLGExtraReceiptsValue)), 420, 615, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalOnlineReprintChargesExtraReceiptsCount)), 330, 655, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalOnlineReprintChargesExtraReceiptsValue)), 420, 655, number_paint);

        canvas.drawText(String.format("%s", df.format(iTotalEMIReceiptsCount)), 480, 295, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalEMIReceiptsValue)), 570, 295, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalADHPReceiptsCount)), 480, 335, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalADHPReceiptsValue)), 570, 335, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalRDReceiptsCount)), 480, 375, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalRDReceiptsValue)), 570, 375, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalAdditionalEMIReceiptsCount)), 480, 415, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalAdditionalEMIReceiptsValue)), 570, 415, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalPreClosureAdditionalEMIReceiptsCount)), 480, 455, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalPreClosureAdditionalEMIReceiptsValue)), 570, 455, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalADHPExtraReceiptsCount)), 480, 495, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalADHPExtraReceiptsValue)), 570, 495, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalNewRDExtraReceiptsCount)), 480, 535, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalNewRDExtraReceiptsValue)), 570, 535, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalFDExtraReceiptsCount)), 480, 575, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalFDExtraReceiptsValue)), 570, 575, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalJLGExtraReceiptsCount)), 480, 615, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalJLGExtraReceiptsValue)), 570, 615, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalReprintChargesExtraReceiptsCount)), 480, 655, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalReprintChargesExtraReceiptsValue)), 570, 655, number_paint);

        int iTotalReceiptsCount = iTotalEMIReceiptsCount
                                + iTotalADHPReceiptsCount
                                + iTotalRDReceiptsCount
                                + iTotalAdditionalEMIReceiptsCount
                                + iTotalPreClosureAdditionalEMIReceiptsCount
                                + iTotalADHPExtraReceiptsCount
                                + iTotalNewRDExtraReceiptsCount
                                + iTotalFDExtraReceiptsCount
                                + iTotalJLGExtraReceiptsCount
                                + iTotalReprintChargesExtraReceiptsCount;

        int iTotalReceiptsValue = iTotalEMIReceiptsValue
                                + iTotalADHPReceiptsValue
                                + iTotalRDReceiptsValue
                                + iTotalAdditionalEMIReceiptsValue
                                + iTotalPreClosureAdditionalEMIReceiptsValue
                                + iTotalADHPExtraReceiptsValue
                                + iTotalNewRDExtraReceiptsValue
                                + iTotalFDExtraReceiptsValue
                                + iTotalJLGExtraReceiptsValue
                                + iTotalReprintChargesExtraReceiptsValue;

        int iTotalCashReceiptsCount = iTotalCashEMIReceiptsCount
                + iTotalCashADHPReceiptsCount
                + iTotalCashRDReceiptsCount
                + iTotalCashAdditionalEMIReceiptsCount
                + iTotalCashPreClosureAdditionalEMIReceiptsCount
                + iTotalCashADHPExtraReceiptsCount
                + iTotalCashNewRDExtraReceiptsCount
                + iTotalCashFDExtraReceiptsCount
                + iTotalCashJLGExtraReceiptsCount
                + iTotalCashReprintChargesExtraReceiptsCount;

        int iTotalCashReceiptsValue = iTotalCashEMIReceiptsValue
                + iTotalCashADHPReceiptsValue
                + iTotalCashRDReceiptsValue
                + iTotalCashAdditionalEMIReceiptsValue
                + iTotalCashPreClosureAdditionalEMIReceiptsValue
                + iTotalCashADHPExtraReceiptsValue
                + iTotalCashNewRDExtraReceiptsValue
                + iTotalCashFDExtraReceiptsValue
                + iTotalCashJLGExtraReceiptsValue
                + iTotalCashReprintChargesExtraReceiptsValue;

        int iTotalOnlineReceiptsCount = iTotalOnlineEMIReceiptsCount
                + iTotalOnlineADHPReceiptsCount
                + iTotalOnlineRDReceiptsCount
                + iTotalOnlineAdditionalEMIReceiptsCount
                + iTotalOnlinePreClosureAdditionalEMIReceiptsCount
                + iTotalOnlineADHPExtraReceiptsCount
                + iTotalOnlineNewRDExtraReceiptsCount
                + iTotalOnlineFDExtraReceiptsCount
                + iTotalOnlineJLGExtraReceiptsCount
                + iTotalOnlineReprintChargesExtraReceiptsCount;

        int iTotalOnlineReceiptsValue = iTotalOnlineEMIReceiptsValue
                + iTotalOnlineADHPReceiptsValue
                + iTotalOnlineRDReceiptsValue
                + iTotalOnlineAdditionalEMIReceiptsValue
                + iTotalOnlinePreClosureAdditionalEMIReceiptsValue
                + iTotalOnlineADHPExtraReceiptsValue
                + iTotalOnlineNewRDExtraReceiptsValue
                + iTotalOnlineFDExtraReceiptsValue
                + iTotalOnlineJLGExtraReceiptsValue
                + iTotalOnlineReprintChargesExtraReceiptsValue;

        canvas.drawText(String.format("%s", df.format(iTotalCashReceiptsCount)), 180, 695, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalCashReceiptsValue)), 270, 695, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalOnlineReceiptsCount)), 330, 695, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalOnlineReceiptsValue)), 420, 695, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalReceiptsCount)), 480, 695, number_paint);
        canvas.drawText(String.format("%s", df.format(iTotalReceiptsValue)), 570, 695, number_paint);

        //Horizontal lines
        canvas.drawLine(0, 720, 575, 720, border_paint);
        canvas.drawLine(0, 760, 575, 760, border_paint);
        canvas.drawLine(0, 800, 575, 800, border_paint);
        canvas.drawLine(0, 840, 575, 840, border_paint);
        canvas.drawLine(0, 880, 575, 880, border_paint);

        canvas.drawLine(0, 920, 575, 920, border_paint);
        canvas.drawLine(0, 960, 575, 960, border_paint);
        canvas.drawLine(0, 1000, 575, 1000, border_paint);
        canvas.drawLine(0, 1040, 575, 1040, border_paint);
        //Vertical lines
//        canvas.drawLine(0, 720, 0, 880, border_paint);
        canvas.drawLine(0, 720, 0, 1040, border_paint);
        canvas.drawLine(60, 720, 60, 800, border_paint);
        canvas.drawLine(120, 720, 120, 800, border_paint);
        canvas.drawLine(180, 720, 180, 800, border_paint);
        canvas.drawLine(240, 720, 240, 800, border_paint);
        canvas.drawLine(300, 720, 300, 800, border_paint);
        canvas.drawLine(355, 720, 355, 800, border_paint);
        canvas.drawLine(410, 720, 410, 800, border_paint);
        canvas.drawLine(465, 720, 465, 800, border_paint);
        canvas.drawLine(520, 720, 520, 800, border_paint);
//        canvas.drawLine(575, 720, 575, 880, border_paint);
        canvas.drawLine(575, 720, 575, 1040, border_paint);

        text_paint.setTextSize(20);
        canvas.drawText(String.format("%s", "नोटा"), 0, 745, text_paint);
        canvas.drawText(String.format("%s", "500"), 73, 745, text_paint);
        canvas.drawText(String.format("%s", "200"), 133, 745, text_paint);
        canvas.drawText(String.format("%s", "100"), 193, 745, text_paint);
        canvas.drawText(String.format("%s", "50"), 260, 745, text_paint);
        canvas.drawText(String.format("%s", "20"), 320, 745, text_paint);
        canvas.drawText(String.format("%s", "10"), 372, 745, text_paint);
        canvas.drawText(String.format("%s", "5"), 432, 745, text_paint);
        canvas.drawText(String.format("%s", "2"), 487, 745, text_paint);
        canvas.drawText(String.format("%s", "1"), 542, 745, text_paint);
        canvas.drawText(String.format("%s", "जमा"), 0, 785, text_paint);
        canvas.drawText(String.format("%6s", String.valueOf(iTotalCount500Notes)), 65, 785, text_paint);
        canvas.drawText(String.format("%6s", String.valueOf(iTotalCount200Notes)), 125, 785, text_paint);
        canvas.drawText(String.format("%6s", String.valueOf(iTotalCount100Notes)), 185, 785, text_paint);
        canvas.drawText(String.format("%6s", String.valueOf(iTotalCount50Notes)), 245, 785, text_paint);
        canvas.drawText(String.format("%6s", String.valueOf(iTotalCount20Notes)), 305, 785, text_paint);
        canvas.drawText(String.format("%6s", String.valueOf(iTotalCount10Notes)), 362, 785, text_paint);
        canvas.drawText(String.format("%6s", String.valueOf(iTotalCount5Notes)), 412, 785, text_paint);
        canvas.drawText(String.format("%6s", String.valueOf(iTotalCount2Notes)), 467, 785, text_paint);
        canvas.drawText(String.format("%6s", String.valueOf(iTotalCount1Notes)), 522, 785, text_paint);

//        canvas.drawText(String.format("%s%9s", "एकूण रु:", df.format(iTotalValue)), 0, 825, text_paint);
        canvas.drawText(String.format("%s%9s", "कॅश रु:", df.format(iTotalValueCash)), 0, 825, text_paint);

        text_paint.setTextSize(20);
//        String strInWords = "";
//        strInWords = AmountInWords(iTotalValue);
//        canvas.drawText(String.format("%s%s", "रु.", strInWords), 0, 865, text_paint);
        String strInWordsCash = "";
        strInWordsCash = AmountInWords(iTotalValueCash);
        canvas.drawText(String.format("%s%s", "कॅश रु.", strInWordsCash), 0, 865, text_paint);

        canvas.drawText(String.format("%s%9s", "Online रु:", df.format(iTotalValueOnline)), 0, 905, text_paint);
        String strInWordsOnline = "";
        strInWordsOnline = AmountInWords(iTotalValueOnline);
        canvas.drawText(String.format("%s%s", "Online रु.", strInWordsOnline), 0, 945, text_paint);

        canvas.drawText(String.format("%s%9s", "एकूण रु:", df.format(iTotalValue)), 0, 985, text_paint);
        String strInWords = "";
        strInWords = AmountInWords(iTotalValue);
        canvas.drawText(String.format("%s%s", "एकूण रु.", strInWords), 0, 1025, text_paint);

        canvas.drawText(String.format("%s%s", "Collection Details Packets Pending Upload to Server: ",
                String.valueOf(iCollectionDetailsPendingUpload)), 0, 1065, text_paint);
        canvas.drawText(String.format("%s%s", "RDReceipt Packets Pending Upload to Server: ",
                String.valueOf(iRDReceiptPendingUpload)), 0, 1105, text_paint);
        canvas.drawText(String.format("%s%s", "SMS Packets Pending Upload to Server: ",
                String.valueOf(iCollectionSMSPendingUpload)), 0, 1145, text_paint);
        canvas.drawText(String.format("%s%s", "Narration Packets Pending Upload to Server: ",
                String.valueOf(iNarrationPendingUpload)), 0, 1185, text_paint);

//        canvas.drawText(String.format("%s%s", "Online Collection Packets Pending Upload to Server: ",
//                String.valueOf(iOnlineCollectionPacketsPendingUpload)), 0, 1225, text_paint);
//        canvas.drawText(String.format("%s%s", "Online Collection photos Pending Upload to Server: ",
//                String.valueOf(iOnlineCollectionPhotosPendingUpload)), 0, 1265, text_paint);

        //एल. एस. ओ.
        canvas.drawText(String.format("%s", "बी.एम. कडे रक्कम दिली"), 0, 1315, text_paint);
        canvas.drawText(String.format("%s", "एल.एस.ओ. ची सही"), 20, 1370, text_paint);
        canvas.drawText(String.format("%s", mainActivity.LoginID), 40, 1395, text_paint);
        //BM
        canvas.drawText(String.format("%s", "एल.एस.ओ. कडून रक्कम मिळाली"), 270, 1315, text_paint);
        canvas.drawText(String.format("%s", "बी.एम. ची सही"), 350, 1370, text_paint);
        canvas.drawText(String.format("%s", strBranchName), 350, 1395, text_paint);

        canvas.drawLine(0, 1408, 575, 1408, border_paint);
    }
    public void CalculatePrePaymentDetails(String selectedPreCollect) {
        int iToBeCollectedInterest = 0;
        int iToBeCollectedPrincipal = 0;
        int iToBeCollectedLateCharges = 0;
        int iToBeCollectedJLG = 0;
        int iToBeCollectedPolicyRenewalAmt = 0;
        int iToBeCollectedADHP = 0;

        //Use temporary copy tcollectionMember of selectedCollectionMember.collectionMember
        //to calculate P+I+LateCharge+JLG breakup required for Past+Current+PrePaymentReqMonths
        clsCollectionMember tcollectionMember = new clsCollectionMember();
        tcollectionMember.InitCollectionMember();
        selectedCollectionMember.collectionMember.Copy_CollectionMember(tcollectionMember);
        tcollectionMember.TotalEMICollectionAmount
                = selectedCollectionMember.collectionMember.LoanPastDuePlusCurrentDueAmtToday;

        //Calculation For Past and Current Dues as on Today
        if(selectedCollectionMember.collectionMember.InterestTypeID == 1)
            CalculateFlatInterestPastCurrentDuesToBeCollected(tcollectionMember);
        else
            CalculateReducingInterestPastCurrentDuesToBeCollected(tcollectionMember);

        iToBeCollectedADHP
                = iToBeCollectedADHP + selectedCollectionMember.collectionMember.ADHPTotalExpectedAndPastDueAmt;

        if(selectedCollectionMember.collectionMember.PrePaymentApprovedMonths > 0) {
            //Calculation For Additional Months after Past and Current Dues are calculated
            int iCalculatedInterest = 0;
            if(selectedCollectionMember.collectionMember.InterestTypeID == 1) {    //flat rate
                int iPendingPrincipal = selectedCollectionMember.collectionMember.PendingPrincipal;
                int iExpected_Principal;
                int iExpected_Interest = selectedCollectionMember.collectionMember.Monthly_Interest;
                for(int i=0; i<selectedCollectionMember.collectionMember.PrePaymentApprovedMonths; i++) {
                    iExpected_Principal = selectedCollectionMember.collectionMember.Monthly_Principal;
                    if(iExpected_Principal > iPendingPrincipal)
                        iExpected_Principal = iPendingPrincipal;
                    if(iExpected_Principal == 0)
                        iExpected_Interest = 0;

                    iToBeCollectedInterest = iToBeCollectedInterest + iExpected_Interest;
                    iToBeCollectedPrincipal = iToBeCollectedPrincipal + iExpected_Principal;
                    iPendingPrincipal = iPendingPrincipal - iExpected_Principal;
                    if(iPendingPrincipal < 0)
                        iPendingPrincipal = 0;

                    iToBeCollectedJLG = iToBeCollectedJLG + selectedCollectionMember.collectionMember.Monthly_SD;
                    iToBeCollectedPolicyRenewalAmt = iToBeCollectedPolicyRenewalAmt + selectedCollectionMember.collectionMember.Monthly_PolicyRenewalAmt;
                    if(selectedPreCollect.contains("ADHP"))
                        iToBeCollectedADHP = iToBeCollectedADHP + selectedCollectionMember.collectionMember.Monthly_ADHP;
                }
            } else {
                int iTemp = selectedCollectionMember.collectionMember.PrePaymentApprovedMonths
                        * (selectedCollectionMember.collectionMember.StandardEMI);
                if(selectedPreCollect.contains("ADHP")) {
                    iTemp = iTemp + (selectedCollectionMember.collectionMember.PrePaymentApprovedMonths
                                    * selectedCollectionMember.collectionMember.Monthly_ADHP);
                }
                if(iTemp <= iCalculatedInterest) {
                    iToBeCollectedInterest = iTemp;
                    iTemp = 0;
                } else {
                    iToBeCollectedInterest = iCalculatedInterest;
                    iTemp = iTemp - iToBeCollectedInterest;
                    if(iTemp <= (selectedCollectionMember.collectionMember.PrePaymentApprovedMonths
                            * selectedCollectionMember.collectionMember.StandardEMI)) {
                        iToBeCollectedPrincipal = iTemp;
                        iTemp = 0;
                    } else {
                        iToBeCollectedPrincipal
                                = (selectedCollectionMember.collectionMember.PrePaymentApprovedMonths
                                * selectedCollectionMember.collectionMember.StandardEMI);
                        iTemp = iTemp - iToBeCollectedPrincipal;

                        if(selectedPreCollect.contains("ADHP")) {
                            if(iTemp < (selectedCollectionMember.collectionMember.PrePaymentApprovedMonths
                                    * selectedCollectionMember.collectionMember.Monthly_ADHP)) {
                                iToBeCollectedPrincipal = iToBeCollectedPrincipal + iTemp;
                                iTemp = 0;
                            } else {
                                iToBeCollectedADHP = iToBeCollectedADHP + iTemp;
                                iTemp = 0;
                            }
                        } else {
                            iToBeCollectedPrincipal = iToBeCollectedPrincipal + iTemp;
                            iTemp = 0;
                        }
                    }
                }
            }
        } else {
            //Calculation For Additional Amount after Past and Current Dues are calculated
            int iCalculatedInterest = 0;
            if(selectedCollectionMember.collectionMember.InterestTypeID == 1) {    //flat rate
                int iTemp = selectedCollectionMember.collectionMember.PrePaymentApprovedAmount
                        - selectedCollectionMember.collectionMember.TotalDueAmountsToPayToday;
                int iPendingPrincipal = selectedCollectionMember.collectionMember.PendingPrincipal;
                int iExpected_Principal;
                int iExpected_Interest = selectedCollectionMember.collectionMember.Monthly_Interest;
                while(iTemp > 0) {
                    iExpected_Principal = selectedCollectionMember.collectionMember.Monthly_Principal;
                    if(iExpected_Principal > iPendingPrincipal)
                        iExpected_Principal = iPendingPrincipal;
                    if(iExpected_Principal == 0)
                        iExpected_Interest = 0;
                    if(iTemp <= iExpected_Interest) {
                        iToBeCollectedInterest = iToBeCollectedInterest + iTemp;
                        iTemp = 0;
                    } else {
                        iToBeCollectedInterest = iToBeCollectedInterest + iExpected_Interest;
                        iTemp = iTemp - iExpected_Interest;
                        if(iTemp <= iExpected_Principal) {
                            iToBeCollectedPrincipal = iToBeCollectedPrincipal + iTemp;
                            iTemp = 0;
                        } else {
                            iToBeCollectedPrincipal = iToBeCollectedPrincipal + iExpected_Principal;
                            iPendingPrincipal = iPendingPrincipal - iExpected_Principal;
                            if(iPendingPrincipal < 0)
                                iPendingPrincipal = 0;
                            iTemp = iTemp - iExpected_Principal;
                            if(iTemp <= selectedCollectionMember.collectionMember.Monthly_SD) {
                                iToBeCollectedJLG = iToBeCollectedJLG + iTemp;
                                iTemp = 0;
                            } else {
                                iToBeCollectedJLG = iToBeCollectedJLG + selectedCollectionMember.collectionMember.Monthly_SD;
                                iTemp = iTemp - selectedCollectionMember.collectionMember.Monthly_SD;
                                if(iTemp <= selectedCollectionMember.collectionMember.Monthly_PolicyRenewalAmt) {
                                    iToBeCollectedPolicyRenewalAmt = iToBeCollectedPolicyRenewalAmt + iTemp;
                                    iTemp = 0;
                                } else {
                                    iToBeCollectedPolicyRenewalAmt = iToBeCollectedPolicyRenewalAmt + selectedCollectionMember.collectionMember.Monthly_PolicyRenewalAmt;
                                    iTemp = iTemp - selectedCollectionMember.collectionMember.Monthly_PolicyRenewalAmt;
                                    if(selectedPreCollect.contains("ADHP")) {
                                        if(iTemp < selectedCollectionMember.collectionMember.Monthly_ADHP) {
                                            iToBeCollectedJLG = iToBeCollectedJLG + iTemp;
                                            // ?????????? ****** Add extra paid amount to JLG and not Policy Renewal ?????????? ******
//                                            iToBeCollectedPolicyRenewalAmt = iToBeCollectedPolicyRenewalAmt + iTemp;
                                            iTemp = 0;
                                        } else {
                                            iToBeCollectedADHP = iToBeCollectedADHP + selectedCollectionMember.collectionMember.Monthly_ADHP;
                                            iTemp = iTemp - selectedCollectionMember.collectionMember.Monthly_ADHP;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                int iTemp = selectedCollectionMember.collectionMember.PrePaymentApprovedAmount
                            - selectedCollectionMember.collectionMember.TotalDueAmountsToPayToday;
                while(iTemp > 0) {
                    if(iTemp <= iCalculatedInterest) {
                        iToBeCollectedInterest = iTemp;
                        iTemp = 0;
                    } else {
                        iToBeCollectedInterest = iToBeCollectedInterest + iCalculatedInterest;
                        iTemp = iTemp - iCalculatedInterest;
                        if(iTemp <= selectedCollectionMember.collectionMember.StandardEMI) {
                            iToBeCollectedPrincipal = iToBeCollectedPrincipal + iTemp;
                            iTemp = 0;
                        } else {
                            iToBeCollectedPrincipal = iToBeCollectedPrincipal + selectedCollectionMember.collectionMember.StandardEMI;
                            iTemp = iTemp - selectedCollectionMember.collectionMember.StandardEMI;
                            if(selectedPreCollect.contains("ADHP")) {
                                if(iTemp < selectedCollectionMember.collectionMember.Monthly_ADHP) {
                                    iToBeCollectedPrincipal = iToBeCollectedPrincipal + iTemp;
                                    iTemp = 0;
                                } else {
                                    iToBeCollectedADHP =
                                            iToBeCollectedADHP + selectedCollectionMember.collectionMember.Monthly_ADHP;
                                    iTemp = iTemp - selectedCollectionMember.collectionMember.Monthly_ADHP;
                                }
                            }
                        }
                    }
                }
            }
        }

        selectedCollectionMember.collectionMember.PreClosurePrincipal = tcollectionMember.CollectedPrincipal + iToBeCollectedPrincipal;
        selectedCollectionMember.collectionMember.PreClosureInterest = tcollectionMember.CollectedInterest + iToBeCollectedInterest;
        selectedCollectionMember.collectionMember.PreClosureLatePayCharge = tcollectionMember.CollectedCurrentLateCharges + iToBeCollectedLateCharges;
        selectedCollectionMember.collectionMember.PreClosureJLG = tcollectionMember.CollectedJLG + iToBeCollectedJLG;
        selectedCollectionMember.collectionMember.PreClosurePolicyRenewalAmt = tcollectionMember.CollectedPolicyRenewalAmt + iToBeCollectedPolicyRenewalAmt;
        selectedCollectionMember.collectionMember.PreClosureADHP = iToBeCollectedADHP;
    }
    public void CalculateFlatInterestPastCurrentDuesToBeCollected(clsCollectionMember tcollectionMember) {
        int iDueInterest = tcollectionMember.Expected_Interest + tcollectionMember.PastDue_Interest;
        int iDuePrincipal = tcollectionMember.Expected_Principal + tcollectionMember.PastDue_Principal;
        int iDueLateCharges = tcollectionMember.Expected_LatePaymentCharges + tcollectionMember.PastDue_LatePaymentCharges;
        int iDueJLG = tcollectionMember.Expected_SD + tcollectionMember.PastDue_SD;
        int iDuePolicyRenewalAmt = tcollectionMember.Expected_PolicyRenewalAmt + tcollectionMember.PastDue_PolicyRenewalAmt;

        int iToBeCollectedPastCurrentInterest = 0;
        int iToBeCollectedPastCurrentPrincipal = 0;
        int iToBeCollectedPastCurrentLateCharges = 0;
        int iToBeCollectedPastCurrentJLG = 0;
        int iToBeCollectedPastCurrentPolicyRenewalAmt = 0;
        int iToBeCollectedPastInterest = 0;
        int iToBeCollectedPastPrincipal = 0;
        int iToBeCollectedPastLateCharges = 0;
        int iToBeCollectedPastJLG = 0;
        int iToBeCollectedPastPolicyRenewalAmt = 0;
        int iToBeCollectedCurrentInterest = 0;
        int iToBeCollectedCurrentPrincipal = 0;
        int iToBeCollectedCurrentLateCharges = 0;
        int iToBeCollectedCurrentJLG = 0;
        int iToBeCollectedCurrentPolicyRenewalAmt = 0;
        int iPastDueInterest = tcollectionMember.PastDue_Interest;
        int iPastDuePrincipal = tcollectionMember.PastDue_Principal;
        int iPastDueLateCharges = tcollectionMember.PastDue_LatePaymentCharges;
        int iPastDueSD = tcollectionMember.PastDue_SD;
        int iPastDuePolicyRenewalAmt = tcollectionMember.PastDue_PolicyRenewalAmt;

        int iCalculatedInterest = iDueInterest;
        if(tcollectionMember.TotalEMICollectionAmount <= iDueInterest) {
            iToBeCollectedPastCurrentInterest = tcollectionMember.TotalEMICollectionAmount;
            if(iToBeCollectedPastCurrentInterest <= tcollectionMember.PastDue_Interest) {
                tcollectionMember.PastDue_Interest
                        = tcollectionMember.PastDue_Interest - iToBeCollectedPastCurrentInterest;
            } else {
                int iTempInterest = iToBeCollectedPastCurrentInterest - tcollectionMember.PastDue_Interest;
                tcollectionMember.Expected_Interest
                        =   tcollectionMember.Expected_Interest - iTempInterest;
                tcollectionMember.PastDue_Interest = 0;
            }
        } else {
            iToBeCollectedPastCurrentInterest = iDueInterest;
            tcollectionMember.PastDue_Interest = 0;
            tcollectionMember.Expected_Interest = 0;
            int iTemp = tcollectionMember.TotalEMICollectionAmount - iToBeCollectedPastCurrentInterest;
            if(iTemp <= iDuePrincipal) {
                iToBeCollectedPastCurrentPrincipal = iTemp;
                if(iToBeCollectedPastCurrentPrincipal <= tcollectionMember.PastDue_Principal) {
                    tcollectionMember.PastDue_Principal
                            = tcollectionMember.PastDue_Principal - iToBeCollectedPastCurrentPrincipal;
                } else {
                    int iTempPrincipal = iToBeCollectedPastCurrentPrincipal - tcollectionMember.PastDue_Principal;
                    tcollectionMember.Expected_Principal
                            =   tcollectionMember.Expected_Principal - iTempPrincipal;
                    tcollectionMember.PastDue_Principal = 0;
                }
            } else {
                iToBeCollectedPastCurrentPrincipal = iDuePrincipal;
                tcollectionMember.PastDue_Principal = 0;
                tcollectionMember.Expected_Principal = 0;
                iTemp = iTemp - iToBeCollectedPastCurrentPrincipal;
                if(iTemp <= iDueLateCharges) {
                    iToBeCollectedPastCurrentLateCharges = iTemp;
                    if(iToBeCollectedPastCurrentLateCharges <= tcollectionMember.PastDue_LatePaymentCharges) {
                        tcollectionMember.PastDue_LatePaymentCharges
                                = tcollectionMember.PastDue_LatePaymentCharges - iToBeCollectedPastCurrentLateCharges;
                    } else {
                        int iTempLateCharges = iToBeCollectedPastCurrentLateCharges - tcollectionMember.PastDue_LatePaymentCharges;
                        tcollectionMember.Expected_LatePaymentCharges
                                =   tcollectionMember.Expected_LatePaymentCharges - iTempLateCharges;
                        tcollectionMember.PastDue_LatePaymentCharges = 0;
                    }
                } else {
                    iToBeCollectedPastCurrentLateCharges = iDueLateCharges;
                    tcollectionMember.PastDue_LatePaymentCharges = 0;
                    tcollectionMember.Expected_LatePaymentCharges = 0;
                    iTemp = iTemp - iToBeCollectedPastCurrentJLG;
                    if(iTemp <= iDueJLG) {
                        iToBeCollectedPastCurrentJLG = iTemp;
                        if(iToBeCollectedPastCurrentJLG <= tcollectionMember.PastDue_SD) {
                            tcollectionMember.PastDue_SD
                                    = tcollectionMember.PastDue_SD - iToBeCollectedPastCurrentJLG;
                        } else {
                            int iTempJLG = iToBeCollectedPastCurrentJLG - tcollectionMember.PastDue_SD;
                            tcollectionMember.Expected_SD
                                    =   tcollectionMember.Expected_SD - iTempJLG;
                            tcollectionMember.PastDue_SD = 0;
                        }
                    } else {
                        iToBeCollectedPastCurrentJLG = iDueJLG;
                        tcollectionMember.PastDue_SD = 0;
                        tcollectionMember.Expected_SD = 0;

                        iToBeCollectedPastCurrentPolicyRenewalAmt = iTemp - iToBeCollectedPastCurrentJLG;
                        if(iToBeCollectedPastCurrentPolicyRenewalAmt <= tcollectionMember.PastDue_PolicyRenewalAmt) {
                            tcollectionMember.PastDue_PolicyRenewalAmt
                                    = tcollectionMember.PastDue_PolicyRenewalAmt - iToBeCollectedPastCurrentPolicyRenewalAmt;
                        } else {
                            int iTempPolicyRenewalAmt = iToBeCollectedPastCurrentPolicyRenewalAmt - tcollectionMember.PastDue_PolicyRenewalAmt;
                            tcollectionMember.Expected_PolicyRenewalAmt
                                    =   tcollectionMember.Expected_PolicyRenewalAmt - iTempPolicyRenewalAmt;
                            if(tcollectionMember.Expected_PolicyRenewalAmt < 0)
                                tcollectionMember.Expected_PolicyRenewalAmt = 0;
                            tcollectionMember.PastDue_PolicyRenewalAmt = 0;
                        }
                    }
                }
            }
        }

        if(iToBeCollectedPastCurrentInterest <= iPastDueInterest) {
            iToBeCollectedPastInterest = iToBeCollectedPastCurrentInterest;
            iToBeCollectedCurrentInterest = 0;
        } else {
            iToBeCollectedPastInterest = iPastDueInterest;
            iToBeCollectedCurrentInterest = iToBeCollectedPastCurrentInterest - iToBeCollectedPastInterest;
        }
        if(iToBeCollectedPastCurrentPrincipal <= iPastDuePrincipal) {
            iToBeCollectedPastPrincipal = iToBeCollectedPastCurrentPrincipal;
            iToBeCollectedCurrentPrincipal = 0;
        } else {
            iToBeCollectedPastPrincipal = iPastDuePrincipal;
            iToBeCollectedCurrentPrincipal = iToBeCollectedPastCurrentPrincipal - iToBeCollectedPastPrincipal;
        }
        if(iToBeCollectedPastCurrentLateCharges <= iPastDueLateCharges) {
            iToBeCollectedPastLateCharges = iToBeCollectedPastCurrentLateCharges;
            iToBeCollectedCurrentLateCharges = 0;
        } else {
            iToBeCollectedPastLateCharges = iPastDueLateCharges;
            iToBeCollectedCurrentLateCharges = iToBeCollectedPastCurrentLateCharges - iToBeCollectedPastLateCharges;
        }
        if(iToBeCollectedPastCurrentJLG <= iPastDueSD) {
            iToBeCollectedPastJLG = iToBeCollectedPastCurrentJLG;
            iToBeCollectedCurrentJLG = 0;
        } else {
            iToBeCollectedPastJLG = iPastDueSD;
            iToBeCollectedCurrentJLG = iToBeCollectedPastCurrentJLG - iToBeCollectedPastJLG;
        }
        if(iToBeCollectedPastCurrentPolicyRenewalAmt <= iPastDuePolicyRenewalAmt) {
            iToBeCollectedPastPolicyRenewalAmt = iToBeCollectedPastCurrentPolicyRenewalAmt;
            iToBeCollectedCurrentPolicyRenewalAmt = 0;
        } else {
            iToBeCollectedPastPolicyRenewalAmt = iPastDuePolicyRenewalAmt;
            iToBeCollectedCurrentPolicyRenewalAmt = iToBeCollectedPastCurrentPolicyRenewalAmt - iToBeCollectedPastPolicyRenewalAmt;
        }
        tcollectionMember.CollectedPastPrincipal = iToBeCollectedPastPrincipal;
        tcollectionMember.CollectedPastInterest = iToBeCollectedPastInterest;
        tcollectionMember.CollectedPastLateCharges = iToBeCollectedPastLateCharges;
        tcollectionMember.CollectedPastJLG = iToBeCollectedPastJLG;
        tcollectionMember.CollectedPastPolicyRenewalAmt = iToBeCollectedPastPolicyRenewalAmt;
        tcollectionMember.CollectedCurrentPrincipal = iToBeCollectedCurrentPrincipal;
        tcollectionMember.CollectedCurrentInterest = iToBeCollectedCurrentInterest;
        tcollectionMember.CollectedCurrentLateCharges = iToBeCollectedCurrentLateCharges;
        tcollectionMember.CollectedCurrentJLG = iToBeCollectedCurrentJLG;
        tcollectionMember.CollectedCurrentPolicyRenewalAmt = iToBeCollectedCurrentPolicyRenewalAmt;

        tcollectionMember.CollectedPrincipal = iToBeCollectedPastCurrentPrincipal;
        tcollectionMember.CollectedInterest = iToBeCollectedPastCurrentInterest;
        tcollectionMember.CollectedLateCharges = iToBeCollectedPastCurrentLateCharges;
        tcollectionMember.CollectedJLG = iToBeCollectedPastCurrentJLG;
        tcollectionMember.CollectedPolicyRenewalAmt = iToBeCollectedPastCurrentPolicyRenewalAmt;

        tcollectionMember.PendingPrincipal
                = tcollectionMember.PendingPrincipal
                - iToBeCollectedPastCurrentPrincipal;
        if(tcollectionMember.PendingPrincipal < 0) {
            tcollectionMember.PendingPrincipal = 0;
        }
        if(iToBeCollectedPastCurrentInterest >= iCalculatedInterest) {
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            String strDate = sdf.format(date);// all done
            tcollectionMember.ReferenceRecoveryDate = strDate;
        }
        tcollectionMember.TotalOutstandingAmount
                = tcollectionMember.TotalOutstandingAmount
                - iToBeCollectedPastCurrentPrincipal
                - iToBeCollectedPastCurrentInterest
                - iToBeCollectedPastCurrentLateCharges
                - iToBeCollectedPastCurrentJLG
                - iToBeCollectedPastCurrentPolicyRenewalAmt;
        if(tcollectionMember.TotalOutstandingAmount < 0) {
            tcollectionMember.TotalOutstandingAmount = 0;
        }
    }
    public void CalculateReducingInterestPastCurrentDuesToBeCollected(clsCollectionMember tcollectionMember) {
        int iDuePrincipal = tcollectionMember.Expected_Principal + tcollectionMember.PastDue_Principal;
        int iDueInterest = tcollectionMember.Expected_Interest + tcollectionMember.PastDue_Interest;
        int iDueLateCharges = tcollectionMember.Expected_LatePaymentCharges + tcollectionMember.PastDue_LatePaymentCharges;
        int iDueJLG = tcollectionMember.Expected_SD + tcollectionMember.PastDue_SD;
        int iDuePolicyRenewalAmt = tcollectionMember.Expected_PolicyRenewalAmt + tcollectionMember.PastDue_PolicyRenewalAmt;

        int iToBeCollectedPastCurrentInterest = 0;
        int iToBeCollectedPastCurrentPrincipal = 0;
        int iToBeCollectedPastCurrentLateCharges = 0;
        int iToBeCollectedPastCurrentJLG = 0;
        int iToBeCollectedPastCurrentPolicyRenewalAmt = 0;
        int iToBeCollectedPastInterest = 0;
        int iToBeCollectedPastPrincipal = 0;
        int iToBeCollectedPastLateCharges = 0;
        int iToBeCollectedPastJLG = 0;
        int iToBeCollectedPastPolicyRenewalAmt = 0;
        int iToBeCollectedCurrentInterest = 0;
        int iToBeCollectedCurrentPrincipal = 0;
        int iToBeCollectedCurrentLateCharges = 0;
        int iToBeCollectedCurrentJLG = 0;
        int iToBeCollectedCurrentPolicyRenewalAmt = 0;
        int iPastDueInterest = tcollectionMember.PastDue_Interest;
        int iPastDuePrincipal = tcollectionMember.PastDue_Principal;
        int iPastDueLateCharges = tcollectionMember.PastDue_LatePaymentCharges;
        int iPastDueSD = tcollectionMember.PastDue_SD;
        int iPastDuePolicyRenewalAmt = tcollectionMember.PastDue_PolicyRenewalAmt;
        int iCalculatedInterest = iDueInterest;
        int iCalculatedPrincipal = iDuePrincipal;

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String strDate = sdf.format(date);// all done
        long DeltaDays = 0;
        int iInterestDelta = 0;
        int iToday = Integer.parseInt(strDate.substring(0, 2));
        int iRecovery = Integer.parseInt(tcollectionMember.ReferenceRecoveryDate.substring(0, 2));
        DeltaDays = iToday - iRecovery;
        //(No. of days * Pending Principal * ROI)/36500
        //YearlyInterest = (YearlyInterestPercentage * 100) //Stored as Integer instead of float in SQLite database
        long lTemp = DeltaDays * tcollectionMember.PendingPrincipal * tcollectionMember.YearlyInterest;
        long lTemp2 = lTemp / 3650000;
        long lTemp3 = lTemp2 * 3650000;
        if(lTemp3 != lTemp2) {            //Avoid Roundup for exact Integer value
            if(lTemp < 0)
                lTemp = lTemp - 1825000;
            else
                lTemp = lTemp + 1825000;    //For Roundup after division by 3650000
        }
        lTemp = lTemp / 3650000;
        iInterestDelta = (int) lTemp;
        int iTemp1 = iDuePrincipal + iDueInterest;
        if(iTemp1 <= 0) {
            iInterestDelta = 0;
        }
        iCalculatedInterest = iCalculatedInterest + iInterestDelta;

        if(DeltaDays < 0) { //Before Reference date
            //Less Interest => More Principal => Same JLG
            iCalculatedPrincipal = iDuePrincipal - iInterestDelta;
            if(iCalculatedPrincipal > tcollectionMember.PendingPrincipal)
                iCalculatedPrincipal = tcollectionMember.PendingPrincipal;

            if(tcollectionMember.TotalEMICollectionAmount <= iCalculatedInterest) {
                iToBeCollectedPastCurrentInterest = tcollectionMember.TotalEMICollectionAmount;
                if(iToBeCollectedPastCurrentInterest <= tcollectionMember.PastDue_Interest) {
                    tcollectionMember.PastDue_Interest
                            = tcollectionMember.PastDue_Interest - iToBeCollectedPastCurrentInterest;
                } else {
                    int iTempInterest = iToBeCollectedPastCurrentInterest - tcollectionMember.PastDue_Interest;
                    tcollectionMember.Expected_Interest
                            =   tcollectionMember.Expected_Interest - iTempInterest;
                    tcollectionMember.PastDue_Interest = 0;
                }
            } else {
                iToBeCollectedPastCurrentInterest = iCalculatedInterest;
                tcollectionMember.PastDue_Interest = 0;
                tcollectionMember.Expected_Interest = 0;
                int iTemp = tcollectionMember.TotalEMICollectionAmount - iToBeCollectedPastCurrentInterest;
                if(iTemp <= iCalculatedPrincipal) {
                    iToBeCollectedPastCurrentPrincipal = iTemp;
                    if(iToBeCollectedPastCurrentPrincipal <= tcollectionMember.PastDue_Principal) {
                        tcollectionMember.PastDue_Principal
                                = tcollectionMember.PastDue_Principal - iToBeCollectedPastCurrentPrincipal;
                    } else {
                        int iTempPrincipal = iToBeCollectedPastCurrentPrincipal - tcollectionMember.PastDue_Principal;
                        tcollectionMember.Expected_Principal
                                =   tcollectionMember.Expected_Principal - iTempPrincipal;
                        tcollectionMember.PastDue_Principal = 0;
                    }
                } else {
                    iToBeCollectedPastCurrentPrincipal = iCalculatedPrincipal;
                    tcollectionMember.PastDue_Principal = 0;
                    tcollectionMember.Expected_Principal = 0;
                    iTemp = iTemp - iToBeCollectedPastCurrentPrincipal;
                    if(iTemp <= iDueLateCharges) {
                        iToBeCollectedPastCurrentLateCharges = iTemp;
                        if(iToBeCollectedPastCurrentLateCharges <= tcollectionMember.PastDue_LatePaymentCharges) {
                            tcollectionMember.PastDue_LatePaymentCharges
                                    = tcollectionMember.PastDue_LatePaymentCharges - iToBeCollectedPastCurrentLateCharges;
                        } else {
                            int iTempLateCharges = iToBeCollectedPastCurrentLateCharges - tcollectionMember.PastDue_LatePaymentCharges;
                            tcollectionMember.Expected_LatePaymentCharges
                                    =   tcollectionMember.Expected_LatePaymentCharges - iTempLateCharges;
                            tcollectionMember.PastDue_LatePaymentCharges = 0;
                        }
                    } else {
                        iToBeCollectedPastCurrentLateCharges = iDueLateCharges;
                        tcollectionMember.PastDue_LatePaymentCharges = 0;
                        tcollectionMember.Expected_LatePaymentCharges = 0;
                        iTemp = iTemp - iToBeCollectedPastCurrentLateCharges;
                        if(iTemp <= iDueJLG) {
                            iToBeCollectedPastCurrentJLG = iTemp;
                            if(iToBeCollectedPastCurrentJLG <= tcollectionMember.PastDue_SD) {
                                tcollectionMember.PastDue_SD
                                        = tcollectionMember.PastDue_SD - iToBeCollectedPastCurrentJLG;
                            } else {
                                int iTempJLG = iToBeCollectedPastCurrentJLG - tcollectionMember.PastDue_SD;
                                tcollectionMember.Expected_SD
                                        =   tcollectionMember.Expected_SD - iTempJLG;
                                tcollectionMember.PastDue_SD = 0;
                            }
                        } else {
                            iToBeCollectedPastCurrentJLG = iDueJLG;
                            tcollectionMember.PastDue_SD = 0;
                            tcollectionMember.Expected_SD = 0;

                            iToBeCollectedPastCurrentPolicyRenewalAmt = iTemp - iToBeCollectedPastCurrentJLG;
                            if(iToBeCollectedPastCurrentPolicyRenewalAmt <= tcollectionMember.PastDue_PolicyRenewalAmt) {
                                tcollectionMember.PastDue_PolicyRenewalAmt
                                        = tcollectionMember.PastDue_PolicyRenewalAmt - iToBeCollectedPastCurrentPolicyRenewalAmt;
                            } else {
                                int iTempPolicyRenewalAmt = iToBeCollectedPastCurrentPolicyRenewalAmt - tcollectionMember.PastDue_PolicyRenewalAmt;
                                tcollectionMember.Expected_PolicyRenewalAmt
                                        =   tcollectionMember.Expected_PolicyRenewalAmt - iTempPolicyRenewalAmt;
                                if(tcollectionMember.Expected_PolicyRenewalAmt < 0)
                                    tcollectionMember.Expected_PolicyRenewalAmt = 0;
                                tcollectionMember.PastDue_PolicyRenewalAmt = 0;
                            }
                        }
                    }
                }
            }
        } else if(DeltaDays == 0) { //On Scheduled date OR On Reference date
            if(tcollectionMember.TotalEMICollectionAmount <= iDueInterest) {
                iToBeCollectedPastCurrentInterest = tcollectionMember.TotalEMICollectionAmount;
                if(iToBeCollectedPastCurrentInterest <= tcollectionMember.PastDue_Interest) {
                    tcollectionMember.PastDue_Interest
                            = tcollectionMember.PastDue_Interest - iToBeCollectedPastCurrentInterest;
                } else {
                    int iTempInterest = iToBeCollectedPastCurrentInterest - tcollectionMember.PastDue_Interest;
                    tcollectionMember.Expected_Interest
                            =   tcollectionMember.Expected_Interest - iTempInterest;
                    tcollectionMember.PastDue_Interest = 0;
                }
            } else {
                iToBeCollectedPastCurrentInterest = iDueInterest;
                tcollectionMember.PastDue_Interest = 0;
                tcollectionMember.Expected_Interest = 0;
                int iTemp = tcollectionMember.TotalEMICollectionAmount - iToBeCollectedPastCurrentInterest;
                if(iTemp <= iDuePrincipal) {
                    iToBeCollectedPastCurrentPrincipal = iTemp;
                    if(iToBeCollectedPastCurrentPrincipal <= tcollectionMember.PastDue_Principal) {
                        tcollectionMember.PastDue_Principal
                                = tcollectionMember.PastDue_Principal - iToBeCollectedPastCurrentPrincipal;
                    } else {
                        int iTempPrincipal = iToBeCollectedPastCurrentPrincipal - tcollectionMember.PastDue_Principal;
                        tcollectionMember.Expected_Principal
                                =   tcollectionMember.Expected_Principal - iTempPrincipal;
                        tcollectionMember.PastDue_Principal = 0;
                    }
                } else {
                    iToBeCollectedPastCurrentPrincipal = iDuePrincipal;
                    tcollectionMember.PastDue_Principal = 0;
                    tcollectionMember.Expected_Principal = 0;
                    iTemp = iTemp - iToBeCollectedPastCurrentPrincipal;
                    if(iTemp <= iDueLateCharges) {
                        iToBeCollectedPastCurrentLateCharges = iTemp;
                        if(iToBeCollectedPastCurrentLateCharges <= tcollectionMember.PastDue_LatePaymentCharges) {
                            tcollectionMember.PastDue_LatePaymentCharges
                                    = tcollectionMember.PastDue_LatePaymentCharges - iToBeCollectedPastCurrentLateCharges;
                        } else {
                            int iTempLateCharges = iToBeCollectedPastCurrentLateCharges - tcollectionMember.PastDue_LatePaymentCharges;
                            tcollectionMember.Expected_LatePaymentCharges
                                    =   tcollectionMember.Expected_LatePaymentCharges - iTempLateCharges;
                            tcollectionMember.PastDue_LatePaymentCharges = 0;
                        }
                    } else {
                        iToBeCollectedPastCurrentLateCharges = iDueLateCharges;
                        tcollectionMember.PastDue_LatePaymentCharges = 0;
                        tcollectionMember.Expected_LatePaymentCharges = 0;
                        iTemp = iTemp - iToBeCollectedPastCurrentLateCharges;
                        if(iTemp <= iDueJLG) {
                            iToBeCollectedPastCurrentJLG = iTemp;
                            if(iToBeCollectedPastCurrentJLG <= tcollectionMember.PastDue_SD) {
                                tcollectionMember.PastDue_SD
                                        = tcollectionMember.PastDue_SD - iToBeCollectedPastCurrentJLG;
                            } else {
                                int iTempJLG = iToBeCollectedPastCurrentJLG - tcollectionMember.PastDue_SD;
                                tcollectionMember.Expected_SD
                                        =   tcollectionMember.Expected_SD - iTempJLG;
                                tcollectionMember.PastDue_SD = 0;
                            }
                        } else {
                            iToBeCollectedPastCurrentJLG = iDueJLG;
                            tcollectionMember.PastDue_SD = 0;
                            tcollectionMember.Expected_SD = 0;

                            iToBeCollectedPastCurrentPolicyRenewalAmt = iTemp - iToBeCollectedPastCurrentJLG;
                            if(iToBeCollectedPastCurrentPolicyRenewalAmt <= tcollectionMember.PastDue_PolicyRenewalAmt) {
                                tcollectionMember.PastDue_PolicyRenewalAmt
                                        = tcollectionMember.PastDue_PolicyRenewalAmt - iToBeCollectedPastCurrentPolicyRenewalAmt;
                            } else {
                                int iTempPolicyRenewalAmt = iToBeCollectedPastCurrentPolicyRenewalAmt - tcollectionMember.PastDue_PolicyRenewalAmt;
                                tcollectionMember.Expected_PolicyRenewalAmt
                                        =   tcollectionMember.Expected_PolicyRenewalAmt - iTempPolicyRenewalAmt;
                                if(tcollectionMember.Expected_PolicyRenewalAmt < 0)
                                    tcollectionMember.Expected_PolicyRenewalAmt = 0;
                                tcollectionMember.PastDue_PolicyRenewalAmt = 0;
                            }
                        }
                    }
                }
            }
        } else {  //After Reference date
            //More Interest => Same Principal => Less JLG
            if(tcollectionMember.TotalEMICollectionAmount <= iCalculatedInterest) {
                iToBeCollectedPastCurrentInterest = tcollectionMember.TotalEMICollectionAmount;
                if(iToBeCollectedPastCurrentInterest <= tcollectionMember.PastDue_Interest) {
                    tcollectionMember.PastDue_Interest
                            = tcollectionMember.PastDue_Interest - iToBeCollectedPastCurrentInterest;
                } else {
                    int iTempInterest = iToBeCollectedPastCurrentInterest - tcollectionMember.PastDue_Interest;
                    tcollectionMember.Expected_Interest
                            =   tcollectionMember.Expected_Interest - iTempInterest;
                    tcollectionMember.PastDue_Interest = 0;
                }
            } else {
                iToBeCollectedPastCurrentInterest = iCalculatedInterest;
                tcollectionMember.PastDue_Interest = 0;
                tcollectionMember.Expected_Interest = 0;
                int iTemp = tcollectionMember.TotalEMICollectionAmount - iToBeCollectedPastCurrentInterest;
                if(iTemp <= iDuePrincipal) {
                    iToBeCollectedPastCurrentPrincipal = iTemp;
                    if(iToBeCollectedPastCurrentPrincipal <= tcollectionMember.PastDue_Principal) {
                        tcollectionMember.PastDue_Principal
                                = tcollectionMember.PastDue_Principal - iToBeCollectedPastCurrentPrincipal;
                    } else {
                        int iTempPrincipal = iToBeCollectedPastCurrentPrincipal - tcollectionMember.PastDue_Principal;
                        tcollectionMember.Expected_Principal
                                =   tcollectionMember.Expected_Principal - iTempPrincipal;
                        tcollectionMember.PastDue_Principal = 0;
                    }
                } else {
                    iToBeCollectedPastCurrentPrincipal = iDuePrincipal;
                    tcollectionMember.PastDue_Principal = 0;
                    tcollectionMember.Expected_Principal = 0;
                    iTemp = iTemp - iToBeCollectedPastCurrentPrincipal;
                    if(iTemp <= iDueLateCharges) {
                        iToBeCollectedPastCurrentLateCharges = iTemp;
                        if(iToBeCollectedPastCurrentLateCharges <= tcollectionMember.PastDue_LatePaymentCharges) {
                            tcollectionMember.PastDue_LatePaymentCharges
                                    = tcollectionMember.PastDue_LatePaymentCharges - iToBeCollectedPastCurrentLateCharges;
                        } else {
                            int iTempLateCharges = iToBeCollectedPastCurrentLateCharges - tcollectionMember.PastDue_LatePaymentCharges;
                            tcollectionMember.Expected_LatePaymentCharges
                                    =   tcollectionMember.Expected_LatePaymentCharges - iTempLateCharges;
                            tcollectionMember.PastDue_LatePaymentCharges = 0;
                        }
                    } else {
                        iToBeCollectedPastCurrentLateCharges = iDueLateCharges;
                        tcollectionMember.PastDue_LatePaymentCharges = 0;
                        tcollectionMember.Expected_LatePaymentCharges = 0;
                        iTemp = iTemp - iToBeCollectedPastCurrentLateCharges;
                        if(iTemp <= iDueJLG) {
                            iToBeCollectedPastCurrentJLG = iTemp;
                            if(iToBeCollectedPastCurrentJLG <= tcollectionMember.PastDue_SD) {
                                tcollectionMember.PastDue_SD
                                        = tcollectionMember.PastDue_SD - iToBeCollectedPastCurrentJLG;
                            } else {
                                int iTempJLG = iToBeCollectedPastCurrentJLG - tcollectionMember.PastDue_SD;
                                tcollectionMember.Expected_SD
                                        =   tcollectionMember.Expected_SD - iTempJLG;
                                tcollectionMember.PastDue_SD = 0;
                            }
                        } else {
                            iToBeCollectedPastCurrentJLG = iDueJLG;
                            tcollectionMember.PastDue_SD = 0;
                            tcollectionMember.Expected_SD = 0;

                            iToBeCollectedPastCurrentPolicyRenewalAmt = iTemp - iToBeCollectedPastCurrentJLG;
                            if(iToBeCollectedPastCurrentPolicyRenewalAmt <= tcollectionMember.PastDue_PolicyRenewalAmt) {
                                tcollectionMember.PastDue_PolicyRenewalAmt
                                        = tcollectionMember.PastDue_PolicyRenewalAmt - iToBeCollectedPastCurrentPolicyRenewalAmt;
                            } else {
                                int iTempPolicyRenewalAmt = iToBeCollectedPastCurrentPolicyRenewalAmt - tcollectionMember.PastDue_PolicyRenewalAmt;
                                tcollectionMember.Expected_PolicyRenewalAmt
                                        =   tcollectionMember.Expected_PolicyRenewalAmt - iTempPolicyRenewalAmt;
                                if(tcollectionMember.Expected_PolicyRenewalAmt < 0)
                                    tcollectionMember.Expected_PolicyRenewalAmt = 0;
                                tcollectionMember.PastDue_PolicyRenewalAmt = 0;
                            }
                        }
                    }
                }
            }
        }

        if(iToBeCollectedPastCurrentInterest <= iPastDueInterest) {
            iToBeCollectedPastInterest = iToBeCollectedPastCurrentInterest;
            iToBeCollectedCurrentInterest = 0;
        } else {
            iToBeCollectedPastInterest = iPastDueInterest;
            iToBeCollectedCurrentInterest = iToBeCollectedPastCurrentInterest - iToBeCollectedPastInterest;
        }
        if(iToBeCollectedPastCurrentPrincipal <= iPastDuePrincipal) {
            iToBeCollectedPastPrincipal = iToBeCollectedPastCurrentPrincipal;
            iToBeCollectedCurrentPrincipal = 0;
        } else {
            iToBeCollectedPastPrincipal = iPastDuePrincipal;
            iToBeCollectedCurrentPrincipal = iToBeCollectedPastCurrentPrincipal - iToBeCollectedPastPrincipal;
        }
        if(iToBeCollectedPastCurrentLateCharges <= iPastDueLateCharges) {
            iToBeCollectedPastLateCharges = iToBeCollectedPastCurrentLateCharges;
            iToBeCollectedCurrentLateCharges = 0;
        } else {
            iToBeCollectedPastLateCharges = iPastDueLateCharges;
            iToBeCollectedCurrentLateCharges = iToBeCollectedPastCurrentLateCharges - iToBeCollectedPastLateCharges;
        }
        if(iToBeCollectedPastCurrentJLG <= iPastDueSD) {
            iToBeCollectedPastJLG = iToBeCollectedPastCurrentJLG;
            iToBeCollectedCurrentJLG = 0;
        } else {
            iToBeCollectedPastJLG = iPastDueSD;
            iToBeCollectedCurrentJLG = iToBeCollectedPastCurrentJLG - iToBeCollectedPastJLG;
        }
        if(iToBeCollectedPastCurrentPolicyRenewalAmt <= iPastDuePolicyRenewalAmt) {
            iToBeCollectedPastPolicyRenewalAmt = iToBeCollectedPastCurrentPolicyRenewalAmt;
            iToBeCollectedCurrentPolicyRenewalAmt = 0;
        } else {
            iToBeCollectedPastPolicyRenewalAmt = iPastDuePolicyRenewalAmt;
            iToBeCollectedCurrentPolicyRenewalAmt = iToBeCollectedPastCurrentPolicyRenewalAmt - iToBeCollectedPastPolicyRenewalAmt;
        }
        tcollectionMember.CollectedPastPrincipal = iToBeCollectedPastPrincipal;
        tcollectionMember.CollectedPastInterest = iToBeCollectedPastInterest;
        tcollectionMember.CollectedPastLateCharges = iToBeCollectedPastLateCharges;
        tcollectionMember.CollectedPastJLG = iToBeCollectedPastJLG;
        tcollectionMember.CollectedPastPolicyRenewalAmt = iToBeCollectedPastPolicyRenewalAmt;
        tcollectionMember.CollectedCurrentPrincipal = iToBeCollectedCurrentPrincipal;
        tcollectionMember.CollectedCurrentInterest = iToBeCollectedCurrentInterest;
        tcollectionMember.CollectedCurrentLateCharges = iToBeCollectedCurrentLateCharges;
        tcollectionMember.CollectedCurrentJLG = iToBeCollectedCurrentJLG;
        tcollectionMember.CollectedCurrentPolicyRenewalAmt = iToBeCollectedCurrentPolicyRenewalAmt;

        tcollectionMember.CollectedPrincipal = iToBeCollectedPastCurrentPrincipal;
        tcollectionMember.CollectedInterest = iToBeCollectedPastCurrentInterest;
        tcollectionMember.CollectedLateCharges = iToBeCollectedPastCurrentLateCharges;
        tcollectionMember.CollectedJLG = iToBeCollectedPastCurrentJLG;
        tcollectionMember.CollectedPolicyRenewalAmt = iToBeCollectedPastCurrentPolicyRenewalAmt;

        tcollectionMember.PendingPrincipal
                = tcollectionMember.PendingPrincipal
                - iToBeCollectedPastCurrentPrincipal;
        if(tcollectionMember.PendingPrincipal < 0) {
            tcollectionMember.PendingPrincipal = 0;
        }
        if(iToBeCollectedPastCurrentInterest >= iCalculatedInterest) {
            tcollectionMember.ReferenceRecoveryDate = strDate;
        }
        tcollectionMember.TotalOutstandingAmount
                = tcollectionMember.TotalOutstandingAmount
                - iToBeCollectedPastCurrentPrincipal
                - iToBeCollectedPastCurrentInterest
                - iToBeCollectedPastCurrentLateCharges
                - iToBeCollectedPastCurrentJLG
                - iToBeCollectedPastCurrentPolicyRenewalAmt;
        if(tcollectionMember.TotalOutstandingAmount < 0) {
            tcollectionMember.TotalOutstandingAmount = 0;
        }
    }

    public void CalculateFlatInterest(int memberIndex) {
        int iDueInterest = mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest + mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest;
        int iDuePrincipal = mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal + mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal;
        int iDueLateCharges = mainActivity.CollectionMemberList.get(memberIndex).Expected_LatePaymentCharges + mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges;
        int iDueJLG = mainActivity.CollectionMemberList.get(memberIndex).Expected_SD + mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD;
        int iDuePolicyRenewalAmt = mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt + mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt;

        int iCollectedInterest = 0;
        int iCollectedPrincipal = 0;
        int iCollectedLateCharges = 0;
        int iCollectedJLG = 0;
        int iCollectedPolicyRenewalAmt = 0;
        int iCollectedPastInterest = 0;
        int iCollectedPastPrincipal = 0;
        int iCollectedPastLateCharges = 0;
        int iCollectedPastJLG = 0;
        int iCollectedPastPolicyRenewalAmt = 0;
        int iCollectedCurrentInterest = 0;
        int iCollectedCurrentPrincipal = 0;
        int iCollectedCurrentLateCharges = 0;
        int iCollectedCurrentJLG = 0;
        int iCollectedCurrentPolicyRenewalAmt = 0;
        int iPastDueInterest = mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest;
        int iPastDuePrincipal = mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal;
        int iPastDueLateCharges = mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges;
        int iPastDueSD = mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD;
        int iPastDuePolicyRenewalAmt = mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt;

        int iCalculatedInterest = iDueInterest;
        if(mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount <= iDueInterest) {
            iCollectedInterest = mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount;
            if(iCollectedInterest <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest) {
                mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest
                        = mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest - iCollectedInterest;
            } else {
                int iTempInterest = iCollectedInterest - mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest;
                mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest
                        =   mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest - iTempInterest;
                mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest = 0;
            }
        } else {
            iCollectedInterest = iDueInterest;
            mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest = 0;
            mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest = 0;
            int iTemp = mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount - iCollectedInterest;
            if(iTemp <= iDuePrincipal) {
                iCollectedPrincipal = iTemp;
                if(iCollectedPrincipal <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal) {
                    mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal
                            = mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal - iCollectedPrincipal;
                } else {
                    int iTempPrincipal = iCollectedPrincipal - mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal;
                    mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal
                            =   mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal - iTempPrincipal;
                    mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal = 0;
                }
            } else {
                iCollectedPrincipal = iDuePrincipal;
                mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal = 0;
                mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal = 0;
                iTemp = iTemp - iCollectedPrincipal;
                if(iTemp <= iDueLateCharges) {
                    iCollectedLateCharges = iTemp;
                    if(iCollectedLateCharges <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges) {
                        mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges
                                = mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges - iCollectedLateCharges;
                    } else {
                        int iTempLateCharges = iCollectedLateCharges - mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges;
                        mainActivity.CollectionMemberList.get(memberIndex).Expected_LatePaymentCharges
                                =   mainActivity.CollectionMemberList.get(memberIndex).Expected_LatePaymentCharges - iTempLateCharges;
                        mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges = 0;
                    }
                } else {
                    iCollectedLateCharges = iDueLateCharges;
                    mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges = 0;
                    mainActivity.CollectionMemberList.get(memberIndex).Expected_LatePaymentCharges = 0;
                    iTemp = iTemp - iCollectedLateCharges;
                    if(iTemp <= iDueJLG) {
                        iCollectedJLG = iTemp;
                        if(iCollectedJLG <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD) {
                            mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD
                                    = mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD - iCollectedJLG;
                        } else {
                            int iTempJLG = iCollectedJLG - mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD;
                            mainActivity.CollectionMemberList.get(memberIndex).Expected_SD
                                    =   mainActivity.CollectionMemberList.get(memberIndex).Expected_SD - iTempJLG;
                            mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD = 0;
                        }
                    } else {
                        iCollectedJLG = iDueJLG;
                        mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD = 0;
                        mainActivity.CollectionMemberList.get(memberIndex).Expected_SD = 0;
                        iCollectedPolicyRenewalAmt = iTemp - iCollectedJLG;
                        if(iCollectedPolicyRenewalAmt <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt) {
                            mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt
                                    = mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt - iCollectedPolicyRenewalAmt;
                        } else {
                            int iTempPolicyRenewalAmt = iCollectedPolicyRenewalAmt - mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt;
                            mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt
                                    =   mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt - iTempPolicyRenewalAmt;
                            if(mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt < 0)
                                mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt = 0;
                            mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt = 0;
                        }
                    }
                }
            }
        }

        if(iCollectedInterest <= iPastDueInterest) {
            iCollectedPastInterest = iCollectedInterest;
            iCollectedCurrentInterest = 0;
        } else {
            iCollectedPastInterest = iPastDueInterest;
            iCollectedCurrentInterest = iCollectedInterest - iCollectedPastInterest;
        }
        if(iCollectedPrincipal <= iPastDuePrincipal) {
            iCollectedPastPrincipal = iCollectedPrincipal;
            iCollectedCurrentPrincipal = 0;
        } else {
            iCollectedPastPrincipal = iPastDuePrincipal;
            iCollectedCurrentPrincipal = iCollectedPrincipal - iCollectedPastPrincipal;
        }
        if(iCollectedLateCharges <= iPastDueLateCharges) {
            iCollectedPastLateCharges = iCollectedLateCharges;
            iCollectedCurrentLateCharges = 0;
        } else {
            iCollectedPastLateCharges = iPastDueLateCharges;
            iCollectedCurrentLateCharges = iCollectedLateCharges - iCollectedPastLateCharges;
        }
        if(iCollectedJLG <= iPastDueSD) {
            iCollectedPastJLG = iCollectedJLG;
            iCollectedCurrentJLG = 0;
        } else {
            iCollectedPastJLG = iPastDueSD;
            iCollectedCurrentJLG = iCollectedJLG - iCollectedPastJLG;
        }
        if(iCollectedPolicyRenewalAmt <= iPastDuePolicyRenewalAmt) {
            iCollectedPastPolicyRenewalAmt = iCollectedPolicyRenewalAmt;
            iCollectedCurrentPolicyRenewalAmt = 0;
        } else {
            iCollectedPastPolicyRenewalAmt = iPastDuePolicyRenewalAmt;
            iCollectedCurrentPolicyRenewalAmt = iCollectedPolicyRenewalAmt - iCollectedPastPolicyRenewalAmt;
        }
        mainActivity.CollectionMemberList.get(memberIndex).CollectedPastPrincipal = iCollectedPastPrincipal;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedPastInterest = iCollectedPastInterest;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedPastLateCharges = iCollectedPastLateCharges;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedPastJLG = iCollectedPastJLG;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedPastPolicyRenewalAmt = iCollectedPastPolicyRenewalAmt;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentPrincipal = iCollectedCurrentPrincipal;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentInterest = iCollectedCurrentInterest;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentLateCharges = iCollectedCurrentLateCharges;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentJLG = iCollectedCurrentJLG;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentPolicyRenewalAmt = iCollectedCurrentPolicyRenewalAmt;

        mainActivity.CollectionMemberList.get(memberIndex).CollectedPrincipal = iCollectedPrincipal;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedInterest = iCollectedInterest;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedLateCharges = iCollectedLateCharges;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedJLG = iCollectedJLG;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedPolicyRenewalAmt = iCollectedPolicyRenewalAmt;

        mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal
                = mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal
                - iCollectedPrincipal;
        if(mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal < 0) {
            mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal = 0;
        }
        if(iCollectedInterest >= iCalculatedInterest) {
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            String strDate = sdf.format(date);// all done
            mainActivity.CollectionMemberList.get(memberIndex).ReferenceRecoveryDate = strDate;
        }
        mainActivity.CollectionMemberList.get(memberIndex).TotalOutstandingAmount
                = mainActivity.CollectionMemberList.get(memberIndex).TotalOutstandingAmount
                - iCollectedPrincipal
                - iCollectedInterest
                - iCollectedLateCharges
                - iCollectedJLG
                - iCollectedPolicyRenewalAmt;
        if(mainActivity.CollectionMemberList.get(memberIndex).TotalOutstandingAmount < 0) {
            mainActivity.CollectionMemberList.get(memberIndex).TotalOutstandingAmount = 0;
        }
    }
    public int InterestDeltaTodate(int memberIndex) {
        if(mainActivity.CollectionMemberList.get(memberIndex).InterestTypeID != 2)
            return 0;

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String strDate = sdf.format(date);// all done

        long DeltaDays = 0;
        int iInterestDelta = 0;

        int iToday = Integer.parseInt(strDate.substring(0, 2));
        int iRecovery = Integer.parseInt(mainActivity.CollectionMemberList.get(memberIndex).ReferenceRecoveryDate.substring(0, 2));
        DeltaDays = iToday - iRecovery;
        //(No. of days * Pending Principal * ROI)/36500
        //YearlyInterest = (YearlyInterestPercentage * 100) //Stored as Integer instead of float in SQLite database
        long lTemp = DeltaDays * mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal * mainActivity.CollectionMemberList.get(memberIndex).YearlyInterest;
        //Roundup Interest
        long lTemp2 = lTemp / 3650000;
        long lTemp3 = lTemp2 * 3650000;
        if(lTemp3 != lTemp2) {            //Avoid Roundup for exact Integer value
            if(lTemp < 0)
                lTemp = lTemp - 1825000;
            else
                lTemp = lTemp + 1825000;    //For Roundup after division by 3650000
        }
        lTemp = lTemp / 3650000;
        iInterestDelta = (int) lTemp;

        return iInterestDelta;
    }
    public void CalculateReducingInterest(int memberIndex) {
        int iDuePrincipal = mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal + mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal;
        int iDueInterest = mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest + mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest;
        int iDueLateCharges = mainActivity.CollectionMemberList.get(memberIndex).Expected_LatePaymentCharges + mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges;
        int iDueJLG = mainActivity.CollectionMemberList.get(memberIndex).Expected_SD + mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD;
        int iDuePolicyRenewalAmt = mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt + mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt;

        int iCollectedInterest = 0;
        int iCollectedPrincipal = 0;
        int iCollectedLateCharges = 0;
        int iCollectedJLG = 0;
        int iCollectedPolicyRenewalAmt = 0;
        int iCollectedPastInterest = 0;
        int iCollectedPastPrincipal = 0;
        int iCollectedPastLateCharges = 0;
        int iCollectedPastJLG = 0;
        int iCollectedPastPolicyRenewalAmt = 0;
        int iCollectedCurrentInterest = 0;
        int iCollectedCurrentPrincipal = 0;
        int iCollectedCurrentLateCharges = 0;
        int iCollectedCurrentJLG = 0;
        int iCollectedCurrentPolicyRenewalAmt = 0;
        int iPastDueInterest = mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest;
        int iPastDuePrincipal = mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal;
        int iPastDueLateCharges = mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges;
        int iPastDueSD = mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD;
        int iPastDuePolicyRenewalAmt = mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt;

        int iCalculatedInterest = iDueInterest;
        int iCalculatedPrincipal = iDuePrincipal;

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String strDate = sdf.format(date);// all done
        long DeltaDays = 0;
        int iInterestDelta = 0;
        int iToday = Integer.parseInt(strDate.substring(0, 2));
        int iRecovery = Integer.parseInt(mainActivity.CollectionMemberList.get(memberIndex).ReferenceRecoveryDate.substring(0, 2));
        DeltaDays = iToday - iRecovery;
//bmp 18-May-26
        if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
            try {
                int iTransactionDay = Integer.parseInt(mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionDate.substring(8));
                DeltaDays = iTransactionDay - iRecovery;
            } catch (Exception e) {
                System.out.println("Parse Error TransactionDay: " + mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionDate);
            }
        }
//bmp 18-May-26
        //(No. of days * Pending Principal * ROI)/36500
        //YearlyInterest = (YearlyInterestPercentage * 100) //Stored as Integer instead of float in SQLite database
        long lTemp = DeltaDays * mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal * mainActivity.CollectionMemberList.get(memberIndex).YearlyInterest;
        long lTemp2 = lTemp / 3650000;
        long lTemp3 = lTemp2 * 3650000;
        if(lTemp3 != lTemp2) {            //Avoid Roundup for exact Integer value
            if(lTemp < 0)
                lTemp = lTemp - 1825000;
            else
                lTemp = lTemp + 1825000;    //For Roundup after division by 3650000
        }
        lTemp = lTemp / 3650000;
        iInterestDelta = (int) lTemp;
        int iTemp1 = iDuePrincipal + iDueInterest;
        if(iTemp1 <= 0) {
            iInterestDelta = 0;
        }
        iCalculatedInterest = iCalculatedInterest + iInterestDelta;

        if(DeltaDays < 0) { //Before Reference date
            //Less Interest => More Principal => Same JLG
            iCalculatedPrincipal = iDuePrincipal - iInterestDelta;
            if(iCalculatedPrincipal > mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal)
                iCalculatedPrincipal = mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal;

            if(mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount <= iCalculatedInterest) {
                iCollectedInterest = mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount;
                if(iCollectedInterest <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest) {
                    mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest
                            = mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest - iCollectedInterest;
                } else {
                    int iTempInterest = iCollectedInterest - mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest;
                    mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest
                            =   mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest - iTempInterest;
                    mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest = 0;
                }
            } else {
                iCollectedInterest = iCalculatedInterest;
                mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest = 0;
                mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest = 0;
                int iTemp = mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount - iCollectedInterest;
                if(iTemp <= iCalculatedPrincipal) {
                    iCollectedPrincipal = iTemp;
                    if(iCollectedPrincipal <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal) {
                        mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal
                                = mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal - iCollectedPrincipal;
                    } else {
                        int iTempPrincipal = iCollectedPrincipal - mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal;
//bmp 10-Jul-26
//                        mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal
//                                =   mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal - iTempPrincipal;
                        mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal
                                =   mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal
                                    - iTempPrincipal
                                    - iInterestDelta;
//bmp 10-Jul-26
                        mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal = 0;
                    }
                } else {
                    iCollectedPrincipal = iCalculatedPrincipal;
                    mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal = 0;
                    mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal = 0;
                    iTemp = iTemp - iCollectedPrincipal;
                    if(iTemp <= iDueLateCharges) {
                        iCollectedLateCharges = iTemp;
                        if(iCollectedLateCharges <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges) {
                            mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges
                                    = mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges - iCollectedLateCharges;
                        } else {
                            int iTempLateCharges = iCollectedLateCharges - mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges;
                            mainActivity.CollectionMemberList.get(memberIndex).Expected_LatePaymentCharges
                                    =   mainActivity.CollectionMemberList.get(memberIndex).Expected_LatePaymentCharges - iTempLateCharges;
                            mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges = 0;
                        }
                    } else {
                        iCollectedLateCharges = iDueLateCharges;
                        mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges = 0;
                        mainActivity.CollectionMemberList.get(memberIndex).Expected_LatePaymentCharges = 0;
                        iTemp = iTemp - iCollectedLateCharges;
                        if(iTemp <= iDueJLG) {
                            iCollectedJLG = iTemp;
                            if(iCollectedJLG <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD) {
                                mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD
                                        = mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD - iCollectedJLG;
                            } else {
                                int iTempJLG = iCollectedJLG - mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD;
                                mainActivity.CollectionMemberList.get(memberIndex).Expected_SD
                                        =   mainActivity.CollectionMemberList.get(memberIndex).Expected_SD - iTempJLG;
                                mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD = 0;
                            }
                        } else {
                            iCollectedJLG = iDueJLG;
                            mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD = 0;
                            mainActivity.CollectionMemberList.get(memberIndex).Expected_SD = 0;
//bmp 02-Jun-26             ?????????????????????????????????????????????????????????????????
//                            iCollectedPolicyRenewalAmt = iTemp - iCollectedJLG;
//                            if(iCollectedPolicyRenewalAmt <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt) {
//                                mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt
//                                        = mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt - iCollectedPolicyRenewalAmt;
//                            } else {
//                                int iTempPolicyRenewalAmt = iCollectedPolicyRenewalAmt - mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt;
//                                mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt
//                                        =   mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt - iTempPolicyRenewalAmt;
//                                if(mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt < 0)
//                                    mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt = 0;
//                                mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt = 0;
//                            }
                            iTemp = iTemp - iCollectedJLG;
                            if(iTemp <= iDuePolicyRenewalAmt) {
                                iCollectedPolicyRenewalAmt = iTemp;
                                if (iCollectedPolicyRenewalAmt <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt) {
                                    mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt
                                            = mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt - iCollectedPolicyRenewalAmt;
                                } else {
                                    int iTempPolicyRenewalAmt = iCollectedPolicyRenewalAmt - mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt;
                                    mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt
                                            = mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt - iTempPolicyRenewalAmt;
                                    mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt = 0;
                                }
                            } else {
                                iCollectedPolicyRenewalAmt = iDuePolicyRenewalAmt;
                                mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt = 0;
                                mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt = 0;
                                iTemp = iTemp - iCollectedPolicyRenewalAmt;
                                if(iTemp > 0) {
                                    int iTempPendingPrincipal
                                            = mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal - iCollectedPrincipal;
                                    if(iTemp <= iTempPendingPrincipal) {
                                        iCollectedPrincipal = iCollectedPrincipal + iTemp;
//                                        mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal
//                                                = mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal - iTemp;
                                    } else {
                                        iCollectedPrincipal = iCollectedPrincipal + iTempPendingPrincipal;
                                        iTemp = iTemp - iTempPendingPrincipal;
                                        iCollectedJLG = iCollectedJLG + iTemp;
//                                        mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal = 0;
                                    }
                                }
                            }
//bmp 02-Jun-26             ?????????????????????????????????????????????????????????????????
                        }
                    }
                }
            }
        } else if(DeltaDays == 0) { //On Scheduled date OR On Reference date
//11-Jul-26
            int iDeltaEMI = mainActivity.CollectionMemberList.get(memberIndex).LoanPastDuePlusCurrentDueAmtToday
                            - mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal
                            - mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal
                            - mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest
                            - mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest
                            - mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges
                            - mainActivity.CollectionMemberList.get(memberIndex).Expected_LatePaymentCharges
                            - mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD
                            - mainActivity.CollectionMemberList.get(memberIndex).Expected_SD
                            - mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt
                            - mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt;
            if(iDeltaEMI > 0) {
                iDuePrincipal = iDuePrincipal + iDeltaEMI;
            }
//11-Jul-26
            if(mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount <= iDueInterest) {
                iCollectedInterest = mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount;
                if(iCollectedInterest <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest) {
                    mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest
                            = mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest - iCollectedInterest;
                } else {
                    int iTempInterest = iCollectedInterest - mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest;
                    mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest
                            =   mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest - iTempInterest;
                    mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest = 0;
                }
            } else {
                iCollectedInterest = iDueInterest;
                mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest = 0;
                mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest = 0;
                int iTemp = mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount - iCollectedInterest;
                if(iTemp <= iDuePrincipal) {
                    iCollectedPrincipal = iTemp;
                    if(iCollectedPrincipal <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal) {
                        mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal
                                = mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal - iCollectedPrincipal;
                    } else {
                        int iTempPrincipal = iCollectedPrincipal - mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal;
                        mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal
                                =   mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal - iTempPrincipal;
                        mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal = 0;
                    }
                } else {
                    iCollectedPrincipal = iDuePrincipal;
                    mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal = 0;
                    mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal = 0;
                    iTemp = iTemp - iCollectedPrincipal;
                    if(iTemp <= iDueLateCharges) {
                        iCollectedLateCharges = iTemp;
                        if(iCollectedLateCharges <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges) {
                            mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges
                                    = mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges - iCollectedLateCharges;
                        } else {
                            int iTempLateCharges = iCollectedLateCharges - mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges;
                            mainActivity.CollectionMemberList.get(memberIndex).Expected_LatePaymentCharges
                                    =   mainActivity.CollectionMemberList.get(memberIndex).Expected_LatePaymentCharges - iTempLateCharges;
                            mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges = 0;
                        }
                    } else {
                        iCollectedLateCharges = iDueLateCharges;
                        mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges = 0;
                        mainActivity.CollectionMemberList.get(memberIndex).Expected_LatePaymentCharges = 0;
                        iTemp = iTemp - iCollectedLateCharges;
                        if(iTemp <= iDueJLG) {
                            iCollectedJLG = iTemp;
                            if(iCollectedJLG <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD) {
                                mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD
                                        = mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD - iCollectedJLG;
                            } else {
                                int iTempJLG = iCollectedJLG - mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD;
                                mainActivity.CollectionMemberList.get(memberIndex).Expected_SD
                                        =   mainActivity.CollectionMemberList.get(memberIndex).Expected_SD - iTempJLG;
                                mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD = 0;
                            }
                        } else {
                            iCollectedJLG = iDueJLG;
                            mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD = 0;
                            mainActivity.CollectionMemberList.get(memberIndex).Expected_SD = 0;
//bmp 02-Jun-26             ?????????????????????????????????????????????????????????????????
//                            iCollectedPolicyRenewalAmt = iTemp - iCollectedJLG;
//                            if(iCollectedPolicyRenewalAmt <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt) {
//                                mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt
//                                        = mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt - iCollectedPolicyRenewalAmt;
//                            } else {
//                                int iTempPolicyRenewalAmt = iCollectedPolicyRenewalAmt - mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt;
//                                mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt
//                                        =   mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt - iTempPolicyRenewalAmt;
//                                if(mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt < 0)
//                                    mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt = 0;
//                                mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt = 0;
//                            }
                            iTemp = iTemp - iCollectedJLG;
                            if(iTemp <= iDuePolicyRenewalAmt) {
                                iCollectedPolicyRenewalAmt = iTemp;
                                if (iCollectedPolicyRenewalAmt <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt) {
                                    mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt
                                            = mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt - iCollectedPolicyRenewalAmt;
                                } else {
                                    int iTempPolicyRenewalAmt = iCollectedPolicyRenewalAmt - mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt;
                                    mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt
                                            = mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt - iTempPolicyRenewalAmt;
                                    mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt = 0;
                                }
                            } else {
                                iCollectedPolicyRenewalAmt = iDuePolicyRenewalAmt;
                                mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt = 0;
                                mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt = 0;
                                iTemp = iTemp - iCollectedPolicyRenewalAmt;
                                if(iTemp > 0) {
                                    int iTempPendingPrincipal
                                            = mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal - iCollectedPrincipal;
                                    if(iTemp <= iTempPendingPrincipal) {
                                        iCollectedPrincipal = iCollectedPrincipal + iTemp;
//                                        mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal
//                                                = mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal - iTemp;
                                    } else {
                                        iCollectedPrincipal = iCollectedPrincipal + iTempPendingPrincipal;
                                        iTemp = iTemp - iTempPendingPrincipal;
                                        iCollectedJLG = iCollectedJLG + iTemp;
//                                        mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal = 0;
                                    }
                                }
                            }
//bmp 02-Jun-26             ?????????????????????????????????????????????????????????????????
                        }
                    }
                }
            }
        } else {  //After Reference date
            //More Interest => Same Principal => Less JLG
            if(mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount <= iCalculatedInterest) {
                iCollectedInterest = mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount;
                if(iCollectedInterest <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest) {
                    mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest
                            = mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest - iCollectedInterest;
                } else {
                    int iTempInterest = iCollectedInterest - mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest;
                    mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest
                            =   mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest - iTempInterest;
                    mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest = 0;
                }
            } else {
                iCollectedInterest = iCalculatedInterest;
                mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest = 0;
                mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest = 0;
                int iTemp = mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount - iCollectedInterest;
                if(iTemp <= iDuePrincipal) {
                    iCollectedPrincipal = iTemp;
                    if(iCollectedPrincipal <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal) {
                        mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal
                                = mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal - iCollectedPrincipal;
                    } else {
                        int iTempPrincipal = iCollectedPrincipal - mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal;
                        mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal
                                =   mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal - iTempPrincipal;
                        mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal = 0;
                    }
                } else {
                    iCollectedPrincipal = iDuePrincipal;
                    mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal = 0;
                    mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal = 0;
                    iTemp = iTemp - iCollectedPrincipal;
                    if(iTemp <= iDueLateCharges) {
                        iCollectedLateCharges = iTemp;
                        if(iCollectedLateCharges <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges) {
                            mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges
                                    = mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges - iCollectedLateCharges;
                        } else {
                            int iTempLateCharges = iCollectedLateCharges - mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges;
                            mainActivity.CollectionMemberList.get(memberIndex).Expected_LatePaymentCharges
                                    =   mainActivity.CollectionMemberList.get(memberIndex).Expected_LatePaymentCharges - iTempLateCharges;
                            mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges = 0;
                        }
                    } else {
                        iCollectedLateCharges = iDueLateCharges;
                        mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges = 0;
                        mainActivity.CollectionMemberList.get(memberIndex).Expected_LatePaymentCharges = 0;
                        iTemp = iTemp - iCollectedLateCharges;
                        if(iTemp <= iDueJLG) {
                            iCollectedJLG = iTemp;
                            if(iCollectedJLG <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD) {
                                mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD
                                        = mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD - iCollectedJLG;
                            } else {
                                int iTempJLG = iCollectedJLG - mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD;
                                mainActivity.CollectionMemberList.get(memberIndex).Expected_SD
                                        =   mainActivity.CollectionMemberList.get(memberIndex).Expected_SD - iTempJLG;
                                mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD = 0;
                            }
                        } else {
                            iCollectedJLG = iDueJLG;
                            mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD = 0;
                            mainActivity.CollectionMemberList.get(memberIndex).Expected_SD = 0;
//bmp 02-Jun-26             ?????????????????????????????????????????????????????????????????
//                            iCollectedPolicyRenewalAmt = iTemp - iCollectedJLG;
//                            if(iCollectedPolicyRenewalAmt <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt) {
//                                mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt
//                                        = mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt - iCollectedPolicyRenewalAmt;
//                            } else {
//                                int iTempPolicyRenewalAmt = iCollectedPolicyRenewalAmt - mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt;
//                                mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt
//                                        =   mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt - iTempPolicyRenewalAmt;
//                                if(mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt < 0)
//                                    mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt = 0;
//                                mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt = 0;
//                            }
                            iTemp = iTemp - iCollectedJLG;
                            if(iTemp <= iDuePolicyRenewalAmt) {
                                iCollectedPolicyRenewalAmt = iTemp;
                                if (iCollectedPolicyRenewalAmt <= mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt) {
                                    mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt
                                            = mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt - iCollectedPolicyRenewalAmt;
                                } else {
                                    int iTempPolicyRenewalAmt = iCollectedPolicyRenewalAmt - mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt;
                                    mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt
                                            = mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt - iTempPolicyRenewalAmt;
                                    mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt = 0;
                                }
                            } else {
                                iCollectedPolicyRenewalAmt = iDuePolicyRenewalAmt;
                                mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt = 0;
                                mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt = 0;
                                iTemp = iTemp - iCollectedPolicyRenewalAmt;
                                if(iTemp > 0) {
                                    int iTempPendingPrincipal
                                            = mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal - iCollectedPrincipal;
                                    if(iTemp <= iTempPendingPrincipal) {
                                        iCollectedPrincipal = iCollectedPrincipal + iTemp;
//                                        mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal
//                                                = mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal - iTemp;
                                    } else {
                                        iCollectedPrincipal = iCollectedPrincipal + iTempPendingPrincipal;
                                        iTemp = iTemp - iTempPendingPrincipal;
                                        iCollectedJLG = iCollectedJLG + iTemp;
//                                        mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal = 0;
                                    }
                                }
                            }
//bmp 02-Jun-26             ?????????????????????????????????????????????????????????????????
                        }
                    }
                }
            }
        }

        if(iCollectedInterest <= iPastDueInterest) {
            iCollectedPastInterest = iCollectedInterest;
            iCollectedCurrentInterest = 0;
        } else {
            iCollectedPastInterest = iPastDueInterest;
            iCollectedCurrentInterest = iCollectedInterest - iCollectedPastInterest;
        }
        if(iCollectedPrincipal <= iPastDuePrincipal) {
            iCollectedPastPrincipal = iCollectedPrincipal;
            iCollectedCurrentPrincipal = 0;
        } else {
            iCollectedPastPrincipal = iPastDuePrincipal;
            iCollectedCurrentPrincipal = iCollectedPrincipal - iCollectedPastPrincipal;
        }
        if(iCollectedLateCharges <= iPastDueLateCharges) {
            iCollectedPastLateCharges = iCollectedLateCharges;
            iCollectedCurrentLateCharges = 0;
        } else {
            iCollectedPastLateCharges = iPastDueLateCharges;
            iCollectedCurrentLateCharges = iCollectedLateCharges - iCollectedPastLateCharges;
        }
        if(iCollectedJLG <= iPastDueSD) {
            iCollectedPastJLG = iCollectedJLG;
            iCollectedCurrentJLG = 0;
        } else {
            iCollectedPastJLG = iPastDueSD;
            iCollectedCurrentJLG = iCollectedJLG - iCollectedPastJLG;
        }
        if(iCollectedPolicyRenewalAmt <= iPastDuePolicyRenewalAmt) {
            iCollectedPastPolicyRenewalAmt = iCollectedPolicyRenewalAmt;
            iCollectedCurrentPolicyRenewalAmt = 0;
        } else {
            iCollectedPastPolicyRenewalAmt = iPastDuePolicyRenewalAmt;
            iCollectedCurrentPolicyRenewalAmt = iCollectedPolicyRenewalAmt - iCollectedPastPolicyRenewalAmt;
        }
        mainActivity.CollectionMemberList.get(memberIndex).CollectedPastPrincipal = iCollectedPastPrincipal;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedPastInterest = iCollectedPastInterest;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedPastLateCharges = iCollectedPastLateCharges;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedPastJLG = iCollectedPastJLG;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedPastPolicyRenewalAmt = iCollectedPastPolicyRenewalAmt;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentPrincipal = iCollectedCurrentPrincipal;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentInterest = iCollectedCurrentInterest;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentLateCharges = iCollectedCurrentLateCharges;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentJLG = iCollectedCurrentJLG;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentPolicyRenewalAmt = iCollectedCurrentPolicyRenewalAmt;

        mainActivity.CollectionMemberList.get(memberIndex).CollectedPrincipal = iCollectedPrincipal;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedInterest = iCollectedInterest;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedLateCharges = iCollectedLateCharges;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedJLG = iCollectedJLG;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedPolicyRenewalAmt = iCollectedPolicyRenewalAmt;

        mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal
                = mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal
                - iCollectedPrincipal;
        if(mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal < 0) {
            mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal = 0;
        }
        if(iCollectedInterest >= iCalculatedInterest) {
            mainActivity.CollectionMemberList.get(memberIndex).ReferenceRecoveryDate = strDate;
        }
        mainActivity.CollectionMemberList.get(memberIndex).TotalOutstandingAmount
                = mainActivity.CollectionMemberList.get(memberIndex).TotalOutstandingAmount
                - iCollectedPrincipal
                - iCollectedInterest
                - iCollectedLateCharges
                - iCollectedJLG
                - iCollectedPolicyRenewalAmt;
        if(mainActivity.CollectionMemberList.get(memberIndex).TotalOutstandingAmount < 0) {
            mainActivity.CollectionMemberList.get(memberIndex).TotalOutstandingAmount = 0;
        }
    }
    public void drawPageEMIReceipt(PdfDocument.Page page, int memberIndex) {
        Paint text_paint = new Paint();
        text_paint.setColor(Color.BLACK);
        Typeface tf = Typeface.create("Arial", Typeface.NORMAL);
        text_paint.setTypeface(tf);
        text_paint.setTextSize(40);

        Paint number_paint = new Paint();
        number_paint.setColor(Color.BLACK);
        number_paint.setTypeface(tf);
        number_paint.setTextAlign(Paint.Align.RIGHT);
        number_paint.setTextSize(24);

        Paint border_paint = new Paint();
        border_paint.setColor(Color.BLACK);
        border_paint.setStrokeWidth((float) 3.0);

        Canvas canvas = page.getCanvas();

        canvas.drawColor(Color.WHITE);

        canvas.drawLine(0, 0, 575, 0, border_paint);
//        text_paint.setTextSize(26);
        text_paint.setTextSize(24);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_OrgName)), 20, 40, text_paint);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_OrgRegNo)), 120, 80, text_paint);
        canvas.drawLine(5, 95, 570, 95, border_paint);
        text_paint.setTextSize(24);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String strDate = sdf.format(date);// all done
//bmp 08-Jul-26
//        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH);
//bmp 08-Jul-26
        String strTime1 = sdf1.format(date);
        long lToday = cal.getTimeInMillis();

        int row_offset = 120;
        canvas.drawText(String.format("%s%s", "दिनांक:", strDate), 0, row_offset, text_paint);
        canvas.drawLine(210, 95, 210, 130, border_paint);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_EMI)), 220, row_offset, text_paint);
        canvas.drawLine(210, 130, 360, 130, border_paint);
        canvas.drawLine(360, 95, 360, 130, border_paint);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_MemberCopy)), 450, row_offset, text_paint);
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s%s", "वेळ:", strTime1), 0, row_offset, text_paint);
//bmp 03-Jul-26
//        canvas.drawText(String.format("%s%s", "पेमेंट:", mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode), 210, row_offset, text_paint);
        if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
            canvas.drawText(String.format("%s%s", "पेमेंट:", "ऑनलाईन"), 200, row_offset, text_paint);
        } else {
            canvas.drawText(String.format("%s%s", "पेमेंट:", "कॅश"), 200, row_offset, text_paint);
        }
//bmp 03-Jul-26
        canvas.drawText(String.format("%s", "ब्रँच"), 340, row_offset, text_paint);
        String strBranchName = mainActivity.CollectionMemberList.get(memberIndex).BranchName;
        String[] strSplit = strBranchName.split(" ");
        if(strSplit.length >= 2)
            strBranchName = strSplit[1];
        canvas.drawText(String.format("%s%s", ":", strBranchName), 370, row_offset, text_paint);
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s", "पावती क्र"), 0, row_offset, text_paint);
        if(mainActivity.CollectionMemberList.get(memberIndex).LastEMIReceiptNumber.isEmpty()) {
            mainActivity.CollectionMemberList.get(memberIndex).LastEMIReceiptNumber
                    = mainActivity.CollectionMemberList.get(memberIndex).EMIReceiptNumber;
        } else {
            SimpleDateFormat sdf2 = new SimpleDateFormat("ddHHmmssSSS", Locale.ENGLISH);
            mainActivity.CollectionMemberList.get(memberIndex).LastEMIReceiptNumber
                    = sdf2.format(date) + "_" + String.valueOf(mainActivity.LSO_ID);
        }
        canvas.drawText(String.format("%s%s", ":", mainActivity.CollectionMemberList.get(memberIndex).LastEMIReceiptNumber), 80, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "सेंटर ID"), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).CenterID)), 80, row_offset, text_paint);
        canvas.drawText(String.format("%s", "ग्रुप ID"), 300, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).GroupID)), 370, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "सभासद"), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.CollectionMemberList.get(memberIndex).MemberName), 80, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "मोबाइल"), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.CollectionMemberList.get(memberIndex).PhoneNo), 80, row_offset, text_paint);
        canvas.drawText(String.format("%s", "खाते क्र."), 300, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.CollectionMemberList.get(memberIndex).SavingAccNo), 370, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "कर्ज क्र."), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).LoanNo)), 80, row_offset, text_paint);
        canvas.drawText(String.format("%s%s%s%s", "हप्ता:", mainActivity.CollectionMemberList.get(memberIndex).LoanInstallmentNo, " तारीख:", mainActivity.CollectionMemberList.get(memberIndex).RecoveryDate), 270, row_offset, text_paint);
//bmp 15-May-26
        if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s%s", "ट्रांझॅक्शन आय.डी.:", mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionID), 0, row_offset, text_paint);
            SimpleDateFormat sdf_1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat sdf_2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            try {
                Date dateTransaction = sdf_1.parse(mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionDate);
                String strTransactionDate = sdf_2.format(dateTransaction);
                canvas.drawText(String.format("%s%s", "तारीख:", strTransactionDate), 350, row_offset, text_paint);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
//bmp 01-Oct-26
////bmp 03-Jul-26
//            String strOnlinePaymentApp = "";
//            for(int k=0; k<OnlinePaymentAppsIDMaster.size(); k++) {
//                if(OnlinePaymentAppsIDMaster.get(k) == mainActivity.CollectionMemberList.get(memberIndex).OnlinePaymentAppID) {
//                    strOnlinePaymentApp = OnlinePaymentAppsMaster.get(k);
//                    break;
//                }
//            }
//            row_offset = row_offset + 30;
//            canvas.drawText(String.format("%s%s", "पेमेंट अँप:", strOnlinePaymentApp), 0, row_offset, text_paint);
////bmp 03-Jul-26
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s%s", "यु.पी.आय. आय.डी.:", mainActivity.CollectionMemberList.get(memberIndex).strOnlinePaymentUPIID), 0, row_offset, text_paint);
//bmp 01-Oct-26
        }
        row_offset = row_offset + 40;
//bmp 15-May-26
        canvas.drawText(String.format("%s", "हप्ता भरण्या आधीची मुद्दल बाकी रक्कम"), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", "रु: ", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal)), 400, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "एकूण बाकी (मुद्दल+व्याज+विलंबशुल्क+बचत)"), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", "रु: ", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).TotalOutstandingAmount)), 400, row_offset, text_paint);
        int igrid_offset = row_offset + 20;
        //Horizontal lines
        canvas.drawLine(0, igrid_offset, 575, igrid_offset, border_paint);
        igrid_offset = igrid_offset + 40;
        canvas.drawLine(0, igrid_offset, 575, igrid_offset, border_paint);
        igrid_offset = igrid_offset + 40;
        canvas.drawLine(0, igrid_offset, 575, igrid_offset, border_paint);
        igrid_offset = igrid_offset + 40;
        canvas.drawLine(0, igrid_offset, 575, igrid_offset, border_paint);
        igrid_offset = igrid_offset + 40;
        canvas.drawLine(0, igrid_offset, 575, igrid_offset, border_paint);
        igrid_offset = igrid_offset + 40;
        canvas.drawLine(0, igrid_offset, 575, igrid_offset, border_paint);
        igrid_offset = igrid_offset + 40;
        canvas.drawLine(0, igrid_offset, 575, igrid_offset, border_paint);
        igrid_offset = igrid_offset + 40;
        canvas.drawLine(0, igrid_offset, 575, igrid_offset, border_paint);
        igrid_offset = igrid_offset + 40;
        canvas.drawLine(0, igrid_offset, 575, igrid_offset, border_paint);
        //Vertical lines
        igrid_offset = row_offset + 20;
        canvas.drawLine(0, igrid_offset, 0, (igrid_offset+320), border_paint);
        canvas.drawLine(135, igrid_offset, 135, (igrid_offset+280), border_paint);
        canvas.drawLine(245, igrid_offset, 245, (igrid_offset+280), border_paint);
        canvas.drawLine(355, igrid_offset, 355, (igrid_offset+280), border_paint);
        canvas.drawLine(465, igrid_offset, 465, (igrid_offset+280), border_paint);
        canvas.drawLine(575, igrid_offset, 575, (igrid_offset+320), border_paint);

        row_offset = row_offset + 45;
        canvas.drawText(String.format("%s", "हप्ता"), 50, row_offset, text_paint);
        canvas.drawText(String.format("%s", "मुद्दल"), 10, (row_offset+40), text_paint);
        canvas.drawText(String.format("%s", "व्याज"), 10, (row_offset+80), text_paint);
        text_paint.setTextSize(22);
        canvas.drawText(String.format("%s", "विलंब शुल्क"), 10, (row_offset+120), text_paint);
        text_paint.setTextSize(24);
        canvas.drawText(String.format("%s", "बचत"), 10, (row_offset+160), text_paint);
        text_paint.setTextSize(22);
        canvas.drawText(String.format("%s", "निधी नूतनीकरण"), 0, (row_offset+200), text_paint);
        text_paint.setTextSize(24);
        canvas.drawText(String.format("%s", "एकूण"), 10, (row_offset+240), text_paint);

        canvas.drawText(String.format("%s", "चालू हप्ता"), 145, row_offset, text_paint);
        canvas.drawText(String.format("%s", "थकीत हप्ता"), 250, row_offset, text_paint);
        canvas.drawText(String.format("%s", "एकूण येणे"), 365, row_offset, text_paint);
        canvas.drawText(String.format("%s", "जमा रक्कम"), 470, row_offset, text_paint);

        DecimalFormat df = new DecimalFormat("##,##,###");

        //Current Due: Principal, Interest, LateCharges, JLG, PolicyRenewal, Total
        //Expected_Principal, Expected_Interest, Expected_LatePaymentCharges, Expected_SD, Expected_PolicyRenewalPolicyRenewal, TotalMonthlyInstallmentAmount
        canvas.drawText(String.format("%9s", df.format(mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal)), 240, (row_offset+40), number_paint);
        canvas.drawText(String.format("%9s", df.format(mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest)), 240, (row_offset+80), number_paint);
        canvas.drawText(String.format("%9s", df.format(mainActivity.CollectionMemberList.get(memberIndex).Expected_LatePaymentCharges)), 240, (row_offset+120), number_paint);
        canvas.drawText(String.format("%9s", df.format(mainActivity.CollectionMemberList.get(memberIndex).Expected_SD)), 240, (row_offset+160), number_paint);
        canvas.drawText(String.format("%9s", df.format(mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt)), 240, (row_offset+200), number_paint);
        canvas.drawText(String.format("%9s", df.format(mainActivity.CollectionMemberList.get(memberIndex).CopyLoanCurrentDueAmountRefday)), 240, (row_offset+240), number_paint);

        //Past Due: Principal, Interest, LateCharges, JLG, PolicyRenewal, Total
        //PastDue_Principal, PastDue_Interest, PastDue_LatePaymentCharges, PastDue_SD, PastDue_PolicyRenewal, PastDue_Total
        canvas.drawText(String.format("%9s", df.format(mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal)), 350, (row_offset+40), number_paint);
        canvas.drawText(String.format("%9s", df.format(mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest)), 350, (row_offset+80), number_paint);
        canvas.drawText(String.format("%9s", df.format(mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges)), 350, (row_offset+120), number_paint);
        canvas.drawText(String.format("%9s", df.format(mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD)), 350, (row_offset+160), number_paint);
        canvas.drawText(String.format("%9s", df.format(mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt)), 350, (row_offset+200), number_paint);
        canvas.drawText(String.format("%9s", df.format(mainActivity.CollectionMemberList.get(memberIndex).CopyLoanPastDueAmountTillRefday)), 350, (row_offset+240), number_paint);

        //Total Due: Principal, Interest, LateCharges, JLG, PolicyRenewal, Total
        int iDuePrincipal = mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal + mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal;
        int iDueInterest = mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest + mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest;
        int iDueLateCharges = mainActivity.CollectionMemberList.get(memberIndex).Expected_LatePaymentCharges + mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges;
        int iDueJLG = mainActivity.CollectionMemberList.get(memberIndex).Expected_SD + mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD;
        int iDuePolicyRenewalAmt = mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt + mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt;

        canvas.drawText(String.format("%9s", df.format(iDuePrincipal)), 460, (row_offset+40), number_paint);
        canvas.drawText(String.format("%9s", df.format(iDueInterest)), 460, (row_offset+80), number_paint);
        canvas.drawText(String.format("%9s", df.format(iDueLateCharges)), 460, (row_offset+120), number_paint);
        canvas.drawText(String.format("%9s", df.format(iDueJLG)), 460, (row_offset+160), number_paint);
        canvas.drawText(String.format("%9s", df.format(iDuePolicyRenewalAmt)), 460, (row_offset+200), number_paint);
        canvas.drawText(String.format("%9s", df.format(mainActivity.CollectionMemberList.get(memberIndex).CopyLoanPastDuePlusCurrentDueAmtRefday)), 460, (row_offset+240), number_paint);

        //Total Collected: Principal, Interest, LateCharges, JLG, Total
        if(mainActivity.CollectionMemberList.get(memberIndex).InterestTypeID == 1)
            CalculateFlatInterest(memberIndex);
        else
            CalculateReducingInterest(memberIndex);

        canvas.drawText(String.format("%9s", df.format(mainActivity.CollectionMemberList.get(memberIndex).CollectedPrincipal)), 570, (row_offset+40), number_paint);
        canvas.drawText(String.format("%9s", df.format(mainActivity.CollectionMemberList.get(memberIndex).CollectedInterest)), 570, (row_offset+80), number_paint);
        canvas.drawText(String.format("%9s", df.format(mainActivity.CollectionMemberList.get(memberIndex).CollectedLateCharges)), 570, (row_offset+120), number_paint);
        canvas.drawText(String.format("%9s", df.format(mainActivity.CollectionMemberList.get(memberIndex).CollectedJLG)), 570, (row_offset+160), number_paint);
        canvas.drawText(String.format("%9s", df.format(mainActivity.CollectionMemberList.get(memberIndex).CollectedPolicyRenewalAmt)), 570, (row_offset+200), number_paint);
        canvas.drawText(String.format("%9s", df.format(mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount)), 570, (row_offset+240), number_paint);

        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String strDate3 = sdf3.format(date);
        if(     (strDate3.equalsIgnoreCase(mainActivity.CollectionMemberList.get(memberIndex).PreClosureRequestDate)) &&
                (mainActivity.CollectionMemberList.get(memberIndex).PreClosureRequestStatus == 1) ) {
            if(mainActivity.CollectionMemberList.get(memberIndex).PreClosurePrincipal > 0) {
                mainActivity.CollectionMemberList.get(memberIndex).PreClosurePrincipal =
                        mainActivity.CollectionMemberList.get(memberIndex).PreClosurePrincipal
                                - mainActivity.CollectionMemberList.get(memberIndex).CollectedPrincipal;
                if(mainActivity.CollectionMemberList.get(memberIndex).PreClosurePrincipal < 0)
                    mainActivity.CollectionMemberList.get(memberIndex).PreClosurePrincipal = 0;

                mainActivity.CollectionMemberList.get(memberIndex).PrePaymentPrincipalCollected =
                        mainActivity.CollectionMemberList.get(memberIndex).PrePaymentPrincipalCollected
                                + mainActivity.CollectionMemberList.get(memberIndex).CollectedPrincipal;
            }
            if(mainActivity.CollectionMemberList.get(memberIndex).PreClosureInterest > 0) {
                mainActivity.CollectionMemberList.get(memberIndex).PreClosureInterest =
                        mainActivity.CollectionMemberList.get(memberIndex).PreClosureInterest
                                - mainActivity.CollectionMemberList.get(memberIndex).CollectedInterest;
                if(mainActivity.CollectionMemberList.get(memberIndex).PreClosureInterest < 0)
                    mainActivity.CollectionMemberList.get(memberIndex).PreClosureInterest = 0;
            }
            if(mainActivity.CollectionMemberList.get(memberIndex).PreClosureLatePayCharge > 0) {
                mainActivity.CollectionMemberList.get(memberIndex).PreClosureLatePayCharge =
                        mainActivity.CollectionMemberList.get(memberIndex).PreClosureLatePayCharge
                                - mainActivity.CollectionMemberList.get(memberIndex).CollectedLateCharges;
                if(mainActivity.CollectionMemberList.get(memberIndex).PreClosureLatePayCharge < 0)
                    mainActivity.CollectionMemberList.get(memberIndex).PreClosureLatePayCharge = 0;
            }
            if(mainActivity.CollectionMemberList.get(memberIndex).PreClosureJLG > 0) {
                mainActivity.CollectionMemberList.get(memberIndex).PreClosureJLG =
                        mainActivity.CollectionMemberList.get(memberIndex).PreClosureJLG
                                - mainActivity.CollectionMemberList.get(memberIndex).CollectedJLG;
                if(mainActivity.CollectionMemberList.get(memberIndex).PreClosureJLG < 0)
                    mainActivity.CollectionMemberList.get(memberIndex).PreClosureJLG = 0;
            }
            if(mainActivity.CollectionMemberList.get(memberIndex).PreClosurePolicyRenewalAmt > 0) {
                mainActivity.CollectionMemberList.get(memberIndex).PreClosurePolicyRenewalAmt =
                        mainActivity.CollectionMemberList.get(memberIndex).PreClosurePolicyRenewalAmt
                                - mainActivity.CollectionMemberList.get(memberIndex).CollectedPolicyRenewalAmt;
                if(mainActivity.CollectionMemberList.get(memberIndex).PreClosurePolicyRenewalAmt < 0)
                    mainActivity.CollectionMemberList.get(memberIndex).PreClosurePolicyRenewalAmt = 0;
            }
        }

        String strInWords = "";
        strInWords = AmountInWords(mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount);
        text_paint.setTextSize(20);
        canvas.drawText(String.format("%s%s", "रु:", strInWords), 0, (row_offset+280), text_paint);

        row_offset = row_offset + 320;
        //Note
        text_paint.setTextSize(19);

        canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote_Line1)), 0, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote_Line2)), 0, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote_Line3)), 0, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote2_Line1)), 0, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote2_Line2)), 0, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote3_Line1)), 0, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote3_Line2)), 0, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote3_Line3)), 0, row_offset, text_paint);
        row_offset = row_offset + 30;
//bmp 20-May-26
        if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
//bmp 03-Jul-26
//            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote5_Line1)), 0, row_offset, text_paint);
//            row_offset = row_offset + 30;
//            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote5_Line2)), 0, row_offset, text_paint);
//            row_offset = row_offset + 30;
////            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote5_Line3)), 0, row_offset, text_paint);
////            row_offset = row_offset + 30;
//            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote6_Line1)), 0, row_offset, text_paint);
//            row_offset = row_offset + 30;
////            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote6_Line2)), 0, row_offset, text_paint);
////            row_offset = row_offset + 30;
////            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote6_Line3)), 0, row_offset, text_paint);
////            row_offset = row_offset + 30;
////            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote6_Line4)), 0, row_offset, text_paint);
////            row_offset = row_offset + 30;
//            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote7_Line1)), 0, row_offset, text_paint);
//            row_offset = row_offset + 30;

            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote4_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote5_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote5_Line2)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote6_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote6_Line2)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote7_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote7_Line2)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote7_Line3)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote8_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote8_Line2)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote9_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote9_Line2)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
//bmp 03-Jul-26
        } else {
//bmp 03-Jul-26
//            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote4_Line1)), 0, row_offset, text_paint);
//            row_offset = row_offset + 30;
//            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote4_Line2)), 0, row_offset, text_paint);
//            row_offset = row_offset + 30;
//            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote4_Line3)), 0, row_offset, text_paint);
//            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptCashNote4_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptCashNote4_Line2)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptCashNote4_Line3)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
//bmp 03-Jul-26
        }
//bmp 20-May-26
        text_paint.setTextSize(24);

        String strNextRecoveryDate = selectedCollectionCenter.NextRecoveryDate;
        SimpleDateFormat sdfOne = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat sdfTwo = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        try {
            Date date1 = sdfOne.parse(selectedCollectionCenter.NextRecoveryDate);
            strNextRecoveryDate = sdfTwo.format(date1);
        } catch (ParseException e) {
//            throw new RuntimeException(e);
        }
        row_offset = row_offset + 10;
        canvas.drawText(String.format("%s%s", "पुढील महिन्याची कलेक्शन तारीख: ", strNextRecoveryDate), 0, row_offset, text_paint);
        row_offset = row_offset + 30;

        String strNextRecoveryTime = selectedCollectionCenter.NextRecoveryTime;
        int hourOfDay = Integer.valueOf(strNextRecoveryTime.substring(0, 2));
        String strTime = "सकाळी ";
        if(hourOfDay < 17)
            strTime = "दुपारी ";
        else if(hourOfDay < 19)
            strTime = "संध्याकाळी ";
        else
            strTime = "रात्री ";
        SimpleDateFormat sdfThree = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat sdfFour = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        try {
            Date date1 = sdfThree.parse(strNextRecoveryTime);
            strNextRecoveryTime = strTime + sdfFour.format(date1);
        } catch (ParseException e) {
//            throw new RuntimeException(e);
        }
        canvas.drawText(String.format("%s%s", "पुढील महिन्याची कलेक्शन वेळ: ", strNextRecoveryTime), 0, row_offset, text_paint);

        //एल. एस. ओ.
        row_offset = row_offset + 80;
        canvas.drawText(String.format("%s", "एल.एस.ओ. ची सही"), 20, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", mainActivity.LoginID), 20, row_offset, text_paint);
        row_offset = row_offset + 15;
        canvas.drawLine(0, row_offset, 575, row_offset, border_paint);
    }
    public void drawPageAdditionalEMIReceipt(PdfDocument.Page page, int memberIndex) {
        Paint text_paint = new Paint();
        text_paint.setColor(Color.BLACK);
        Typeface tf = Typeface.create("Arial", Typeface.NORMAL);
        text_paint.setTypeface(tf);
        text_paint.setTextSize(40);

        Paint number_paint = new Paint();
        number_paint.setColor(Color.BLACK);
        number_paint.setTypeface(tf);
        number_paint.setTextAlign(Paint.Align.RIGHT);
        number_paint.setTextSize(24);

        Paint border_paint = new Paint();
        border_paint.setColor(Color.BLACK);
        border_paint.setStrokeWidth((float) 3.0);

        Canvas canvas = page.getCanvas();

        canvas.drawColor(Color.WHITE);

        canvas.drawLine(0, 0, 575, 0, border_paint);
//        text_paint.setTextSize(26);
        text_paint.setTextSize(24);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_OrgName)), 20, 40, text_paint);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_OrgRegNo)), 120, 80, text_paint);
        canvas.drawLine(5, 95, 570, 95, border_paint);
        text_paint.setTextSize(24);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String strDate = sdf.format(date);// all done
//bmp 08-Jul-26
//        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH);
//bmp 08-Jul-26
        String strTime1 = sdf1.format(date);
        long lToday = cal.getTimeInMillis();

        int row_offset = 120;
        text_paint.setTextSize(22);
        canvas.drawText(String.format("%s%s", "दिनांक:", strDate), 0, 120, text_paint);
        canvas.drawLine(205, 95, 205, 130, border_paint);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_ExtraEMI)), 210, 120, text_paint);
        canvas.drawLine(205, 130, 420, 130, border_paint);
        canvas.drawLine(420, 95, 420, 130, border_paint);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_MemberCopy)), 450, 120, text_paint);
        text_paint.setTextSize(24);
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s%s", "वेळ:", strTime1), 0, 160, text_paint);
//bmp 03-Jul-26
//        canvas.drawText(String.format("%s%s", "पेमेंट:", mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode), 205, 160, text_paint);
        if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
            canvas.drawText(String.format("%s%s", "पेमेंट:", "ऑनलाईन"), 200, 160, text_paint);
        } else {
            canvas.drawText(String.format("%s%s", "पेमेंट:", "कॅश"), 200, 160, text_paint);
        }
//bmp 03-Jul-26
        canvas.drawText(String.format("%s", "ब्रँच"), 340, 160, text_paint);
        String strBranchName = mainActivity.CollectionMemberList.get(memberIndex).BranchName;
        String[] strSplit = strBranchName.split(" ");
        if(strSplit.length >= 2)
            strBranchName = strSplit[1];
        canvas.drawText(String.format("%s%s", ":", strBranchName), 370, 160, text_paint);
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s", "पावती क्र"), 0, 200, text_paint);
        SimpleDateFormat sdf2 = new SimpleDateFormat("ddHHmmssSSS", Locale.ENGLISH);
        mainActivity.CollectionMemberList.get(memberIndex).LastEMIReceiptNumber
                = sdf2.format(date) + "_" + String.valueOf(mainActivity.LSO_ID);
        canvas.drawText(String.format("%s%s", ":", mainActivity.CollectionMemberList.get(memberIndex).LastEMIReceiptNumber), 80, 200, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "सेंटर ID"), 0, 230, text_paint);
        canvas.drawText(String.format("%s%s", ":", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).CenterID)), 80, 230, text_paint);
        canvas.drawText(String.format("%s", "ग्रुप ID"), 300, 230, text_paint);
        canvas.drawText(String.format("%s%s", ":", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).GroupID)), 370, 230, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "सभासद"), 0, 260, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.CollectionMemberList.get(memberIndex).MemberName), 80, 260, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "मोबाइल"), 0, 290, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.CollectionMemberList.get(memberIndex).PhoneNo), 80, 290, text_paint);
        canvas.drawText(String.format("%s", "खाते क्र."), 300, 290, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.CollectionMemberList.get(memberIndex).SavingAccNo), 370, 290, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "कर्ज क्र."), 0, 320, text_paint);
        canvas.drawText(String.format("%s%s", ":", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).LoanNo)), 80, 320, text_paint);
        canvas.drawText(String.format("%s%s%s%s", "हप्ता:", mainActivity.CollectionMemberList.get(memberIndex).LoanInstallmentNo, " तारीख:", mainActivity.CollectionMemberList.get(memberIndex).RecoveryDate), 240, 320, text_paint);
//bmp 15-May-26
        if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s%s", "ट्रांझॅक्शन आय.डी.:", mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionID), 0, row_offset, text_paint);
            SimpleDateFormat sdf_1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat sdf_2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            try {
                Date dateTransaction = sdf_1.parse(mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionDate);
                String strTransactionDate = sdf_2.format(dateTransaction);
                canvas.drawText(String.format("%s%s", "तारीख:", strTransactionDate), 350, row_offset, text_paint);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
//bmp 01-Oct-26
////bmp 03-Jul-26
//            String strOnlinePaymentApp = "";
//            for(int k=0; k<OnlinePaymentAppsIDMaster.size(); k++) {
//                if(OnlinePaymentAppsIDMaster.get(k) == mainActivity.CollectionMemberList.get(memberIndex).OnlinePaymentAppID) {
//                    strOnlinePaymentApp = OnlinePaymentAppsMaster.get(k);
//                    break;
//                }
//            }
//            row_offset = row_offset + 30;
//            canvas.drawText(String.format("%s%s", "पेमेंट अँप:", strOnlinePaymentApp), 0, row_offset, text_paint);
////bmp 03-Jul-26
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s%s", "यु.पी.आय. आय.डी.:", mainActivity.CollectionMemberList.get(memberIndex).strOnlinePaymentUPIID), 0, row_offset, text_paint);
//bmp 01-Oct-26
        }
        row_offset = row_offset + 40;
//bmp 15-May-26

        canvas.drawText(String.format("%s", "हप्ता भरण्या आधीची मुद्दल बाकी रक्कम"), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", "रु: ", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal)), 400, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "एकूण बाकी (मुद्दल+व्याज+विलंबशुल्क+बचत)"), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", "रु: ", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).TotalOutstandingAmount)), 400, row_offset, text_paint);
        //Horizontal lines
        int grid_offset = row_offset + 20;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        //Vertical lines
        grid_offset = row_offset + 20;
        canvas.drawLine(0, grid_offset, 0, (grid_offset+320), border_paint);
        canvas.drawLine(400, grid_offset, 400, (grid_offset+280), border_paint);
        canvas.drawLine(575, grid_offset, 575, (grid_offset+320), border_paint);

        row_offset = row_offset + 45;
        canvas.drawText(String.format("%s", "हप्ता"), 50, row_offset, text_paint);
        canvas.drawText(String.format("%s", "मुद्दल"), 10, (row_offset+40), text_paint);
        canvas.drawText(String.format("%s", "व्याज"), 10, (row_offset+80), text_paint);
        text_paint.setTextSize(22);
        canvas.drawText(String.format("%s", "विलंब शुल्क"), 10, (row_offset+120), text_paint);
        text_paint.setTextSize(24);
        canvas.drawText(String.format("%s", "बचत"), 10, (row_offset+160), text_paint);
        text_paint.setTextSize(22);
        canvas.drawText(String.format("%s", "निधी नूतनीकरण"), 10, (row_offset+200), text_paint);
        text_paint.setTextSize(24);
        canvas.drawText(String.format("%s", "एकूण"), 10, (row_offset+240), text_paint);

        canvas.drawText(String.format("%s", "जमा रक्कम (रुपये)"), 405, row_offset, text_paint);

        DecimalFormat df = new DecimalFormat("##,##,###");

        //Total Collected: Principal, Interest, LateCharges, JLG, PolicyRenewalAmt, Total
        int iCollectedInterest = 0;
        int iCollectedPrincipal = 0;
        int iCollectedLateCharges = 0;
        int iCollectedJLG = 0;
        int iCollectedPolicyRenewalAmt = 0;
        int iCalculatedInterest = 0;
        iCalculatedInterest = mainActivity.CollectionMemberList.get(memberIndex).PreClosureInterest;
        if(mainActivity.CollectionMemberList.get(memberIndex).InterestTypeID == 1) {    //flat rate
            int iTemp = mainActivity.CollectionMemberList.get(memberIndex).AdditionalRepayCollectionAmount;
            int iPendingPrincipal = mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal;
            int iExpected_Principal;
            int iExpected_Interest = mainActivity.CollectionMemberList.get(memberIndex).Monthly_Interest;
            while(iTemp > 0) {
                iExpected_Principal = mainActivity.CollectionMemberList.get(memberIndex).Monthly_Principal;
                if(iExpected_Principal > iPendingPrincipal)
                    iExpected_Principal = iPendingPrincipal;
                if(iExpected_Principal == 0)
                    iExpected_Interest = 0;
                if(iTemp <= iExpected_Interest) {
                    iCollectedInterest = iCollectedInterest + iTemp;
                    iTemp = 0;
                } else {
                    iCollectedInterest = iCollectedInterest + iExpected_Interest;
                    iTemp = iTemp - iExpected_Interest;
                    if(iTemp <= iExpected_Principal) {
                        iCollectedPrincipal = iCollectedPrincipal + iTemp;
                        iTemp = 0;
                    } else {
                        iCollectedPrincipal = iCollectedPrincipal + iExpected_Principal;
                        iPendingPrincipal = iPendingPrincipal - iExpected_Principal;
                        if(iPendingPrincipal < 0)
                            iPendingPrincipal = 0;
                        iTemp = iTemp - iExpected_Principal;
                        if(iTemp <= mainActivity.CollectionMemberList.get(memberIndex).Monthly_SD) {
                            iCollectedJLG = iCollectedJLG + iTemp;
                            iTemp = 0;
                        } else {
                            iCollectedJLG = iCollectedJLG + mainActivity.CollectionMemberList.get(memberIndex).Monthly_SD;
                            iTemp = iTemp - mainActivity.CollectionMemberList.get(memberIndex).Monthly_SD;
                            if(iTemp <= mainActivity.CollectionMemberList.get(memberIndex).Monthly_PolicyRenewalAmt) {
                                iCollectedPolicyRenewalAmt = iCollectedPolicyRenewalAmt + iTemp;
                                iTemp = 0;
                            } else {
                                iCollectedPolicyRenewalAmt = iCollectedPolicyRenewalAmt + mainActivity.CollectionMemberList.get(memberIndex).Monthly_PolicyRenewalAmt;
                                iTemp = iTemp - mainActivity.CollectionMemberList.get(memberIndex).Monthly_PolicyRenewalAmt;
                            }
                        }
                    }
                }
            }
        } else {
            int iTemp = mainActivity.CollectionMemberList.get(memberIndex).AdditionalRepayCollectionAmount;
            if(iTemp <= iCalculatedInterest) {
                iCollectedInterest = iTemp;
                iTemp = 0;
            } else {
                iCollectedInterest = iCalculatedInterest;
                iTemp = iTemp - iCollectedInterest;
                if(iTemp <= mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal) {
                    iCollectedPrincipal = iTemp;
                    iTemp = 0;
                } else {
                    iCollectedPrincipal = mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal;
                    iTemp = iTemp - iCollectedPrincipal;
                    iCollectedJLG = iCollectedJLG + iTemp;
                    //Atfer Pending Principal, additional paid amount goes to JLG. Policy Renewal ????
                    iTemp = 0;
                }
            }
        }

        mainActivity.CollectionMemberList.get(memberIndex).CollectedPastPrincipal = 0;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedPastInterest = 0;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedPastLateCharges = 0;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedPastJLG = 0;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedPastPolicyRenewalAmt = 0;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentPrincipal = iCollectedPrincipal;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentInterest = iCollectedInterest;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentLateCharges = iCollectedLateCharges;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentJLG = iCollectedJLG;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentPolicyRenewalAmt = iCollectedPolicyRenewalAmt;

        mainActivity.CollectionMemberList.get(memberIndex).CollectedPrincipal = iCollectedPrincipal;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedInterest = iCollectedInterest;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedLateCharges = iCollectedLateCharges;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedJLG = iCollectedJLG;
        mainActivity.CollectionMemberList.get(memberIndex).CollectedPolicyRenewalAmt = iCollectedPolicyRenewalAmt;

        mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal
                = mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal
                - iCollectedPrincipal;
        if(mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal < 0) {
            mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal = 0;
        }
        if(iCollectedInterest >= iCalculatedInterest) {
            mainActivity.CollectionMemberList.get(memberIndex).ReferenceRecoveryDate = strDate;
        }
        mainActivity.CollectionMemberList.get(memberIndex).TotalOutstandingAmount
                = mainActivity.CollectionMemberList.get(memberIndex).TotalOutstandingAmount
                - iCollectedPrincipal
                - iCollectedInterest
                - iCollectedLateCharges
                - iCollectedJLG
                - iCollectedPolicyRenewalAmt;
        if(mainActivity.CollectionMemberList.get(memberIndex).TotalOutstandingAmount < 0) {
            mainActivity.CollectionMemberList.get(memberIndex).TotalOutstandingAmount = 0;
        }
        canvas.drawText(String.format("%9s", df.format(iCollectedPrincipal)), 570, (row_offset+40), number_paint);
        canvas.drawText(String.format("%9s", df.format(iCollectedInterest)), 570, (row_offset+80), number_paint);
        canvas.drawText(String.format("%9s", df.format(iCollectedLateCharges)), 570, (row_offset+120), number_paint);
        canvas.drawText(String.format("%9s", df.format(iCollectedJLG)), 570, (row_offset+160), number_paint);
        canvas.drawText(String.format("%9s", df.format(iCollectedPolicyRenewalAmt)), 570, (row_offset+200), number_paint);
        canvas.drawText(String.format("%9s", df.format(mainActivity.CollectionMemberList.get(memberIndex).AdditionalRepayCollectionAmount)), 570, (row_offset+240), number_paint);
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String strDate3 = sdf3.format(date);
        if(     (strDate3.equalsIgnoreCase(mainActivity.CollectionMemberList.get(memberIndex).PreClosureRequestDate)) &&
                (mainActivity.CollectionMemberList.get(memberIndex).PreClosureRequestStatus == 1) ) {
            if(mainActivity.CollectionMemberList.get(memberIndex).PreClosurePrincipal > 0) {
                mainActivity.CollectionMemberList.get(memberIndex).PreClosurePrincipal =
                        mainActivity.CollectionMemberList.get(memberIndex).PreClosurePrincipal - iCollectedPrincipal;
                if(mainActivity.CollectionMemberList.get(memberIndex).PreClosurePrincipal < 0)
                    mainActivity.CollectionMemberList.get(memberIndex).PreClosurePrincipal = 0;

                mainActivity.CollectionMemberList.get(memberIndex).PrePaymentPrincipalCollected =
                        mainActivity.CollectionMemberList.get(memberIndex).PrePaymentPrincipalCollected
                                + iCollectedPrincipal;
            }
            if(mainActivity.CollectionMemberList.get(memberIndex).PreClosureInterest > 0) {
                mainActivity.CollectionMemberList.get(memberIndex).PreClosureInterest =
                        mainActivity.CollectionMemberList.get(memberIndex).PreClosureInterest - iCollectedInterest;
                if(mainActivity.CollectionMemberList.get(memberIndex).PreClosureInterest < 0)
                    mainActivity.CollectionMemberList.get(memberIndex).PreClosureInterest = 0;
            }
            if(mainActivity.CollectionMemberList.get(memberIndex).PreClosureLatePayCharge > 0) {
                mainActivity.CollectionMemberList.get(memberIndex).PreClosureLatePayCharge =
                        mainActivity.CollectionMemberList.get(memberIndex).PreClosureLatePayCharge - iCollectedLateCharges;
                if(mainActivity.CollectionMemberList.get(memberIndex).PreClosureLatePayCharge < 0)
                    mainActivity.CollectionMemberList.get(memberIndex).PreClosureLatePayCharge = 0;
            }
            if(mainActivity.CollectionMemberList.get(memberIndex).PreClosureJLG > 0) {
                mainActivity.CollectionMemberList.get(memberIndex).PreClosureJLG =
                        mainActivity.CollectionMemberList.get(memberIndex).PreClosureJLG - iCollectedJLG;
                if(mainActivity.CollectionMemberList.get(memberIndex).PreClosureJLG < 0)
                    mainActivity.CollectionMemberList.get(memberIndex).PreClosureJLG = 0;
            }
            if(mainActivity.CollectionMemberList.get(memberIndex).PreClosurePolicyRenewalAmt > 0) {
                mainActivity.CollectionMemberList.get(memberIndex).PreClosurePolicyRenewalAmt =
                        mainActivity.CollectionMemberList.get(memberIndex).PreClosurePolicyRenewalAmt - iCollectedPolicyRenewalAmt;
                if(mainActivity.CollectionMemberList.get(memberIndex).PreClosurePolicyRenewalAmt < 0)
                    mainActivity.CollectionMemberList.get(memberIndex).PreClosurePolicyRenewalAmt = 0;
            }
        }

        String strInWords = "";
        strInWords = AmountInWords(mainActivity.CollectionMemberList.get(memberIndex).AdditionalRepayCollectionAmount);
        text_paint.setTextSize(20);
        canvas.drawText(String.format("%s%s", "रु:", strInWords), 0, (row_offset+280), text_paint);

        row_offset = row_offset + 320;
        //Note
        text_paint.setTextSize(19);

        canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote_Line1)), 0, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote_Line2)), 0, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote_Line3)), 0, row_offset, text_paint);
        row_offset = row_offset + 30;

        canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote2_Line1)), 0, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote2_Line2)), 0, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote3_Line1)), 0, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote3_Line2)), 0, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote3_Line3)), 0, row_offset, text_paint);
        row_offset = row_offset + 30;
//bmp 20-May-26
        if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
//bmp 03-Jul-26
//            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote5_Line1)), 0, row_offset, text_paint);
//            row_offset = row_offset + 30;
//            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote5_Line2)), 0, row_offset, text_paint);
//            row_offset = row_offset + 30;
////            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote5_Line3)), 0, row_offset, text_paint);
////            row_offset = row_offset + 30;
//            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote6_Line1)), 0, row_offset, text_paint);
//            row_offset = row_offset + 30;
////            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote6_Line2)), 0, row_offset, text_paint);
////            row_offset = row_offset + 30;
////            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote6_Line3)), 0, row_offset, text_paint);
////            row_offset = row_offset + 30;
////            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote6_Line4)), 0, row_offset, text_paint);
////            row_offset = row_offset + 30;
//            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote7_Line1)), 0, row_offset, text_paint);
//            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote4_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote5_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote5_Line2)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote6_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote6_Line2)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote7_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote7_Line2)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote7_Line3)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote8_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote8_Line2)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote9_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptOnlineNote9_Line2)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
//bmp 03-Jul-26
        } else {
//            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote4_Line1)), 0, row_offset, text_paint);
//            row_offset = row_offset + 30;
//            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote4_Line2)), 0, row_offset, text_paint);
//            row_offset = row_offset + 30;
//            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptNote4_Line3)), 0, row_offset, text_paint);
//            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptCashNote4_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptCashNote4_Line2)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.EMIReceiptCashNote4_Line3)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
//bmp 03-Jul-26
        }
//bmp 20-May-26
        text_paint.setTextSize(24);

        String strNextRecoveryDate = selectedCollectionCenter.NextRecoveryDate;
        SimpleDateFormat sdfOne = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat sdfTwo = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        try {
            Date date1 = sdfOne.parse(selectedCollectionCenter.NextRecoveryDate);
            strNextRecoveryDate = sdfTwo.format(date1);
        } catch (ParseException e) {
//            throw new RuntimeException(e);
        }
        row_offset = row_offset + 10;
        canvas.drawText(String.format("%s%s", "पुढील महिन्याची कलेक्शन तारीख: ", strNextRecoveryDate), 0, row_offset, text_paint);
        row_offset = row_offset + 30;

        String strNextRecoveryTime = selectedCollectionCenter.NextRecoveryTime;
        int hourOfDay = Integer.valueOf(strNextRecoveryTime.substring(0, 2));
        String strTime = "सकाळी ";
        if(hourOfDay < 17)
            strTime = "दुपारी ";
        else if(hourOfDay < 19)
            strTime = "संध्याकाळी ";
        else
            strTime = "रात्री ";
        SimpleDateFormat sdfThree = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat sdfFour = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        try {
            Date date1 = sdfThree.parse(strNextRecoveryTime);
            strNextRecoveryTime = strTime + sdfFour.format(date1);
        } catch (ParseException e) {
//            throw new RuntimeException(e);
        }
        canvas.drawText(String.format("%s%s", "पुढील महिन्याची कलेक्शन वेळ: ", strNextRecoveryTime), 0, row_offset, text_paint);

        //एल. एस. ओ.
        row_offset = row_offset + 80;
        canvas.drawText(String.format("%s", "एल.एस.ओ. ची सही"), 20, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", mainActivity.LoginID), 20, row_offset, text_paint);
        row_offset = row_offset + 15;
        canvas.drawLine(0, row_offset, 575, row_offset, border_paint);
    }
    public void drawPageADHPReceipt(PdfDocument.Page page, int memberIndex) {
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
//        text_paint.setTextSize(26);
        text_paint.setTextSize(24);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_OrgName)), 20, 40, text_paint);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_OrgRegNo)), 120, 80, text_paint);
        canvas.drawLine(5, 95, 570, 95, border_paint);
        text_paint.setTextSize(24);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String strDate = sdf.format(date);// all done
//bmp 08-Jul-26
//        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH);
//bmp 08-Jul-26
        String strTime1 = sdf1.format(date);

        int row_offset = 120;
        text_paint.setTextSize(22);
//        canvas.drawText(String.format("%s%s", "दिनांक:", strDate), 0, row_offset, text_paint);
//        canvas.drawLine(205, 95, 205, 130, border_paint);
//        canvas.drawText(String.format("%s", getString(R.string.Receipt_ADHP)), 210, row_offset, text_paint);
//        canvas.drawLine(205, 130, 370, 130, border_paint);
//        canvas.drawLine(370, 95, 370, 130, border_paint);
//        canvas.drawText(String.format("%s", getString(R.string.Receipt_MemberCopy)), 450, row_offset, text_paint);
        canvas.drawLine(120, 95, 120, 130, border_paint);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_ADHP)), 125, row_offset, text_paint);
        canvas.drawLine(120, 130, 460, 130, border_paint);
        canvas.drawLine(460, 95, 460, 130, border_paint);
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s%s", "दिनांक:", strDate), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_MemberCopy)), 450, row_offset, text_paint);
        text_paint.setTextSize(24);
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s%s", "वेळ:", strTime1), 0, row_offset, text_paint);
//bmp 03-Jul-26
//        canvas.drawText(String.format("%s%s", "पेमेंट:", mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode), 205, row_offset, text_paint);
        if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
            canvas.drawText(String.format("%s%s", "पेमेंट:", "ऑनलाईन"), 200, row_offset, text_paint);
        } else {
            canvas.drawText(String.format("%s%s", "पेमेंट:", "कॅश"), 200, row_offset, text_paint);
        }
//bmp 03-Jul-26
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s", "पावती क्र."), 0, row_offset, text_paint);
        if(mainActivity.CollectionMemberList.get(memberIndex).LastADHPReceiptNumber.isEmpty()) {
            mainActivity.CollectionMemberList.get(memberIndex).LastADHPReceiptNumber
                    = mainActivity.CollectionMemberList.get(memberIndex).ADHPReceiptNumber;
        } else {
            SimpleDateFormat sdf2 = new SimpleDateFormat("ddHHmmssSSS", Locale.ENGLISH);
            mainActivity.CollectionMemberList.get(memberIndex).LastADHPReceiptNumber
                    = sdf2.format(date) + "_" + String.valueOf(mainActivity.LSO_ID);
        }
        canvas.drawText(String.format("%s%s", ":", mainActivity.CollectionMemberList.get(memberIndex).LastADHPReceiptNumber), 120, row_offset, text_paint);

        canvas.drawText(String.format("%s", "ब्रँच"), 350, row_offset, text_paint);
        String strBranchName = mainActivity.CollectionMemberList.get(memberIndex).BranchName;
        String[] strSplit = strBranchName.split(" ");
        if(strSplit.length >= 2)
            strBranchName = strSplit[1];
        canvas.drawText(String.format("%s%s", ":", strBranchName), 400, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "सेंटर ID"), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).CenterID)), 120, row_offset, text_paint);
        canvas.drawText(String.format("%s", "ग्रुप ID"), 300, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).GroupID)), 400, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "खातेदार"), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.CollectionMemberList.get(memberIndex).MemberName), 120, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "मोबाइल क्र."), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.CollectionMemberList.get(memberIndex).PhoneNo), 120, row_offset, text_paint);
        canvas.drawText(String.format("%s", "खाते क्र."), 300, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.CollectionMemberList.get(memberIndex).SavingAccNo), 400, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "मेंबरशिप क्र."), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.CollectionMemberList.get(memberIndex).ADHPMembershipNumber), 120, row_offset, text_paint);
        canvas.drawText(String.format("%s", "वर्गणी क्र."), 300, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).ContributionNo)), 400, row_offset, text_paint);
//bmp 15-May-26
        if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s%s", "ट्रांझॅक्शन आय.डी.:", mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionID), 0, row_offset, text_paint);
            SimpleDateFormat sdf_1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat sdf_2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            try {
                Date dateTransaction = sdf_1.parse(mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionDate);
                String strTransactionDate = sdf_2.format(dateTransaction);
                canvas.drawText(String.format("%s%s", "तारीख:", strTransactionDate), 340, row_offset, text_paint);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
//bmp 01-Oct-26
////bmp 03-Jul-26
//            String strOnlinePaymentApp = "";
//            for(int k=0; k<OnlinePaymentAppsIDMaster.size(); k++) {
//                if(OnlinePaymentAppsIDMaster.get(k) == mainActivity.CollectionMemberList.get(memberIndex).OnlinePaymentAppID) {
//                    strOnlinePaymentApp = OnlinePaymentAppsMaster.get(k);
//                    break;
//                }
//            }
//            row_offset = row_offset + 30;
//            canvas.drawText(String.format("%s%s", "पेमेंट अँप:", strOnlinePaymentApp), 0, row_offset, text_paint);
////bmp 03-Jul-26
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s%s", "यु.पी.आय. आय.डी.:", mainActivity.CollectionMemberList.get(memberIndex).strOnlinePaymentUPIID), 0, row_offset, text_paint);
//bmp 01-Oct-26
        }
        row_offset = row_offset + 40;
//bmp 15-May-26

        canvas.drawText(String.format("%s", "थकीत वर्गणी"), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", "रु: ", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).CopyADHPPastDueAmountTillToday)), 200, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "चालू वर्गणी"), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", "रु:", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).CopyADHPContributionAmount)), 200, row_offset, text_paint);
        int grid_offset = row_offset + 20;
        //Horizontal lines
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;

        //Vertical lines
        grid_offset = row_offset + 20;
        canvas.drawLine(0, grid_offset, 0, (grid_offset+200), border_paint);
        canvas.drawLine(480, grid_offset, 480, (grid_offset+160), border_paint);
        canvas.drawLine(575, grid_offset, 575, (grid_offset+200), border_paint);

        row_offset = row_offset + 45;
        canvas.drawText(String.format("%s", "कलेक्शन"), 200, row_offset, text_paint);
        canvas.drawText(String.format("%s", "रुपये"), 490, row_offset, text_paint);
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s", "आधारपूर्णा कलेक्शन"), 5, row_offset, text_paint);
        int iTemp = (mainActivity.CollectionMemberList.get(memberIndex).TotalADHPCollectionAmount * 95) / 100;
        canvas.drawText(String.format("%s", String.valueOf(iTemp)), 490, row_offset, text_paint);
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s", "कार्यालयीन शुल्क"), 5, row_offset, text_paint);
        iTemp = mainActivity.CollectionMemberList.get(memberIndex).TotalADHPCollectionAmount - iTemp;
        canvas.drawText(String.format("%s", String.valueOf(iTemp)), 490, row_offset, text_paint);
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s", "एकूण वर्गणी"), 5, row_offset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).TotalADHPCollectionAmount)), 490, row_offset, text_paint);
        row_offset = row_offset + 40;
        String strInWords = "";
        strInWords = AmountInWords(mainActivity.CollectionMemberList.get(memberIndex).TotalADHPCollectionAmount);
        text_paint.setTextSize(20);
        canvas.drawText(String.format("%s%s", "रु:", strInWords), 5, row_offset, text_paint);
        text_paint.setTextSize(24);

        row_offset = row_offset + 40;
        //Note
        text_paint.setTextSize(19);
        canvas.drawText(String.format("%s", getString(R.string.ADHPReceiptNote1_Line1)), 0, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", getString(R.string.ADHPReceiptNote2_Line1)), 0, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", getString(R.string.ADHPReceiptNote3_Line1)), 0, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", getString(R.string.ADHPReceiptNote3_Line2)), 0, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", getString(R.string.ADHPReceiptNote4_Line1)), 0, row_offset, text_paint);
        row_offset = row_offset + 30;
        if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
            canvas.drawText(String.format("%s", getString(R.string.ADHPReceiptNote5_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.ADHPReceiptNote5_Line2)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
//            canvas.drawText(String.format("%s", getString(R.string.ADHPReceiptNote5_Line3)), 0, row_offset, text_paint);
//            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.ADHPReceiptNote6_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.ADHPReceiptNote7_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.ADHPReceiptNote7_Line2)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
        }
        text_paint.setTextSize(24);

        String strNextRecoveryDate = selectedCollectionCenter.NextRecoveryDate;
        SimpleDateFormat sdfOne = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat sdfTwo = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        try {
            Date date1 = sdfOne.parse(selectedCollectionCenter.NextRecoveryDate);
            strNextRecoveryDate = sdfTwo.format(date1);
        } catch (ParseException e) {
//            throw new RuntimeException(e);
        }
        row_offset = row_offset + 5;
        canvas.drawText(String.format("%s%s", "पुढील महिन्याची कलेक्शन तारीख: ", strNextRecoveryDate), 0, row_offset, text_paint);
        row_offset = row_offset + 30;

        String strNextRecoveryTime = selectedCollectionCenter.NextRecoveryTime;
        int hourOfDay = Integer.valueOf(strNextRecoveryTime.substring(0, 2));
        String strTime = "सकाळी ";
        if(hourOfDay < 17)
            strTime = "दुपारी ";
        else if(hourOfDay < 19)
            strTime = "संध्याकाळी ";
        else
            strTime = "रात्री ";
        SimpleDateFormat sdfThree = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat sdfFour = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        try {
            Date date1 = sdfThree.parse(strNextRecoveryTime);
            strNextRecoveryTime = strTime + sdfFour.format(date1);
        } catch (ParseException e) {
//            throw new RuntimeException(e);
        }
        canvas.drawText(String.format("%s%s", "पुढील महिन्याची कलेक्शन वेळ: ", strNextRecoveryTime), 0, row_offset, text_paint);
        row_offset = row_offset + 80;

        //एल. एस. ओ.
        canvas.drawText(String.format("%s", "एल.एस.ओ. ची सही"), 20, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", mainActivity.LoginID), 20, row_offset, text_paint);
        row_offset = row_offset + 13;
        canvas.drawLine(0, row_offset, 575, row_offset, border_paint);
    }
    public void drawPageRDReceipt(PdfDocument.Page page, int memberIndex, int rdIndex) {
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
//        text_paint.setTextSize(26);
        text_paint.setTextSize(24);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_OrgName)), 20, 40, text_paint);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_OrgRegNo)), 120, 80, text_paint);
        canvas.drawLine(5, 95, 570, 95, border_paint);
        text_paint.setTextSize(24);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String strDate = sdf.format(date);// all done
//bmp 08-Jul-26
//        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH);
//bmp 08-Jul-26
        String strTime1 = sdf1.format(date);
        int row_offset = 120;
        canvas.drawText(String.format("%s%s", "दिनांक:", strDate), 0, row_offset, text_paint);
        canvas.drawLine(210, 95, 210, 130, border_paint);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_RD)), 240, row_offset, text_paint);
        canvas.drawLine(210, 130, 365, 130, border_paint);
        canvas.drawLine(365, 95, 365, 130, border_paint);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_MemberCopy)), 450, row_offset, text_paint);
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s%s", "वेळ:", strTime1), 0, row_offset, text_paint);
//bmp 03-Jul-26
//        canvas.drawText(String.format("%s%s", "पेमेंट:", mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode), 205, row_offset, text_paint);
        if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
            canvas.drawText(String.format("%s%s", "पेमेंट:", "ऑनलाईन"), 200, row_offset, text_paint);
        } else {
            canvas.drawText(String.format("%s%s", "पेमेंट:", "कॅश"), 200, row_offset, text_paint);
        }
//bmp 03-Jul-26
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s", "पावती क्र."), 0, row_offset, text_paint);
        if(mainActivity.RDReceiptList.get(rdIndex).LastReceiptNumber.isEmpty()) {
            mainActivity.RDReceiptList.get(rdIndex).LastReceiptNumber
                    = mainActivity.RDReceiptList.get(rdIndex).ReceiptNumber;
        } else {
            SimpleDateFormat sdf2 = new SimpleDateFormat("ddHHmmssSSS", Locale.ENGLISH);
            mainActivity.RDReceiptList.get(rdIndex).LastReceiptNumber
                    = sdf2.format(date) + "_" + String.valueOf(mainActivity.LSO_ID);
        }
        canvas.drawText(String.format("%s%s", ":", mainActivity.RDReceiptList.get(rdIndex).LastReceiptNumber), 120, row_offset, text_paint);

        canvas.drawText(String.format("%s", "ब्रँच"), 350, row_offset, text_paint);
        String strBranchName = mainActivity.CollectionMemberList.get(memberIndex).BranchName;
        String[] strSplit = strBranchName.split(" ");
        if(strSplit.length >= 2)
            strBranchName = strSplit[1];
        canvas.drawText(String.format("%s%s", ":", strBranchName), 400, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "सेंटर ID"), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).CenterID)), 120, row_offset, text_paint);
        canvas.drawText(String.format("%s", "ग्रुप ID"), 300, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).GroupID)), 400, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "खातेदार"), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.RDReceiptList.get(rdIndex).OperatedBY), 120, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "RD खातेदार"), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.RDReceiptList.get(rdIndex).FirstAcHolder), 120, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "मोबाइल क्र."), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.CollectionMemberList.get(memberIndex).PhoneNo), 120, row_offset, text_paint);
        canvas.drawText(String.format("%s", "खाते क्र."), 300, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.CollectionMemberList.get(memberIndex).SavingAccNo), 400, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "RD क्र."), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", String.valueOf(mainActivity.RDReceiptList.get(rdIndex).RDDetailsID)), 120, row_offset, text_paint);
        canvas.drawText(String.format("%s", "कलेक्शन क्र."), 300, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.RDReceiptList.get(rdIndex).InstallmentNo), 400, row_offset, text_paint);
//bmp 15-May-26
        if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s%s", "ट्रांझॅक्शन आय.डी.:", mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionID), 0, row_offset, text_paint);
            SimpleDateFormat sdf_1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat sdf_2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            try {
                Date dateTransaction = sdf_1.parse(mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionDate);
                String strTransactionDate = sdf_2.format(dateTransaction);
                canvas.drawText(String.format("%s%s", "तारीख:", strTransactionDate), 340, row_offset, text_paint);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
//bmp 01-Oct-26
////bmp 03-Jul-26
//            String strOnlinePaymentApp = "";
//            for(int k=0; k<OnlinePaymentAppsIDMaster.size(); k++) {
//                if(OnlinePaymentAppsIDMaster.get(k) == mainActivity.CollectionMemberList.get(memberIndex).OnlinePaymentAppID) {
//                    strOnlinePaymentApp = OnlinePaymentAppsMaster.get(k);
//                    break;
//                }
//            }
//            row_offset = row_offset + 30;
//            canvas.drawText(String.format("%s%s", "पेमेंट अँप:", strOnlinePaymentApp), 0, row_offset, text_paint);
////bmp 03-Jul-26
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s%s", "यु.पी.आय. आय.डी.:", mainActivity.CollectionMemberList.get(memberIndex).strOnlinePaymentUPIID), 0, row_offset, text_paint);
//bmp 01-Oct-26
        }
//bmp 15-May-26
        int grid_offset = row_offset + 10;

        //Horizontal lines
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;

        //Vertical lines
        grid_offset = row_offset + 10;
        canvas.drawLine(0, grid_offset, 0, (grid_offset+200), border_paint);
        canvas.drawLine(480, grid_offset, 480, (grid_offset+160), border_paint);
        canvas.drawLine(575, grid_offset, 575, (grid_offset+200), border_paint);

        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s", "कलेक्शन"), 100, row_offset, text_paint);
        canvas.drawText(String.format("%s", "रुपये"), 490, row_offset, text_paint);
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s", "RD थकीत कलेक्शन"), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(mainActivity.RDReceiptList.get(rdIndex).CopyPastDue)), 490, row_offset, text_paint);
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s", "RD चालू कलेक्शन"), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(mainActivity.RDReceiptList.get(rdIndex).CopyExpectedInstallmentAmt)), 490, row_offset, text_paint);
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s", "RD एकूण कलेक्शन"), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(mainActivity.RDReceiptList.get(rdIndex).ActualAmountReceived)), 490, row_offset, text_paint);
        String strInWords = "";
        strInWords = AmountInWords(mainActivity.RDReceiptList.get(rdIndex).ActualAmountReceived);
        text_paint.setTextSize(20);
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s%s", "रु:", strInWords), 0, row_offset, text_paint);
        row_offset = row_offset + 30;
        if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
            text_paint.setTextSize(19);
            canvas.drawText(String.format("%s", getString(R.string.RDReceiptNote1_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.RDReceiptNote1_Line2)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
//            canvas.drawText(String.format("%s", getString(R.string.RDReceiptNote1_Line3)), 0, row_offset, text_paint);
//            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.RDReceiptNote2_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.RDReceiptNote3_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.RDReceiptNote3_Line2)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            text_paint.setTextSize(24);
        }
        String strNextRecoveryDate = selectedCollectionCenter.NextRecoveryDate;
        SimpleDateFormat sdfOne = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat sdfTwo = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        try {
            Date date1 = sdfOne.parse(selectedCollectionCenter.NextRecoveryDate);
            strNextRecoveryDate = sdfTwo.format(date1);
        } catch (ParseException e) {
//            throw new RuntimeException(e);
        }
        row_offset = row_offset + 35;
        canvas.drawText(String.format("%s%s", "पुढील महिन्याची कलेक्शन तारीख: ", strNextRecoveryDate), 0, row_offset, text_paint);

        String strNextRecoveryTime = selectedCollectionCenter.NextRecoveryTime;
        int hourOfDay = Integer.valueOf(strNextRecoveryTime.substring(0, 2));
        String strTime = "सकाळी ";
        if(hourOfDay < 17)
            strTime = "दुपारी ";
        else if(hourOfDay < 19)
            strTime = "संध्याकाळी ";
        else
            strTime = "रात्री ";
        SimpleDateFormat sdfThree = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat sdfFour = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        try {
            Date date1 = sdfThree.parse(strNextRecoveryTime);
            strNextRecoveryTime = strTime + sdfFour.format(date1);
        } catch (ParseException e) {
//            throw new RuntimeException(e);
        }
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s%s", "पुढील महिन्याची कलेक्शन वेळ: ", strNextRecoveryTime), 0, row_offset, text_paint);

        //एल. एस. ओ.
        row_offset = row_offset + 100;
        canvas.drawText(String.format("%s", "एल.एस.ओ. ची सही"), 20, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", mainActivity.LoginID), 20, row_offset, text_paint);
        row_offset = row_offset + 13;
        canvas.drawLine(0, row_offset, 575, row_offset, border_paint);
    }
    public void drawPageExtraReceipt(PdfDocument.Page page, int memberIndex) {
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
//        text_paint.setTextSize(26);
        text_paint.setTextSize(24);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_OrgName)), 20, 40, text_paint);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_OrgRegNo)), 120, 80, text_paint);
        canvas.drawLine(5, 95, 570, 95, border_paint);
        text_paint.setTextSize(24);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String strDate = sdf.format(date);// all done
//bmp 08-Jul-26
//        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH);
//bmp 08-Jul-26
        String strTime1 = sdf1.format(date);

        int row_offset = 120;
        text_paint.setTextSize(22);
        canvas.drawText(String.format("%s%s", "दिनांक:", strDate), 0, row_offset, text_paint);
        canvas.drawLine(205, 95, 205, 130, border_paint);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_Extra)), 205, row_offset, text_paint);
        canvas.drawLine(205, 130, 420, 130, border_paint);
        canvas.drawLine(420, 95, 420, 130, border_paint);
        canvas.drawText(String.format("%s", getString(R.string.Receipt_MemberCopy)), 450, row_offset, text_paint);
        text_paint.setTextSize(24);

        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s%s", "वेळ:", strTime1), 0, 160, text_paint);
//bmp 03-Jul-26
//        canvas.drawText(String.format("%s%s", "पेमेंट:", mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode), 205, row_offset, text_paint);
        if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
            canvas.drawText(String.format("%s%s", "पेमेंट:", "ऑनलाईन"), 200, row_offset, text_paint);
        } else {
            canvas.drawText(String.format("%s%s", "पेमेंट:", "कॅश"), 200, row_offset, text_paint);
        }
//bmp 03-Jul-26
        canvas.drawText(String.format("%s", "ब्रँच"), 340, row_offset, text_paint);
        String strBranchName = mainActivity.CollectionMemberList.get(memberIndex).BranchName;
        String[] strSplit = strBranchName.split(" ");
        if(strSplit.length >= 2)
            strBranchName = strSplit[1];
        canvas.drawText(String.format("%s%s", ":", strBranchName), 400, row_offset, text_paint);
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s", "पावती क्र."), 0, row_offset, text_paint);
        SimpleDateFormat sdf2 = new SimpleDateFormat("ddHHmmssSSS", Locale.ENGLISH);
        mainActivity.CollectionMemberList.get(memberIndex).LastExtraReceiptNumber
                = sdf2.format(date) + "_" + String.valueOf(mainActivity.LSO_ID);
        canvas.drawText(String.format("%s%s", ":", mainActivity.CollectionMemberList.get(memberIndex).LastExtraReceiptNumber), 100, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "सेंटर ID"), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).CenterID)), 100, row_offset, text_paint);
        canvas.drawText(String.format("%s", "ग्रुप ID"), 320, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).GroupID)), 400, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "सभासद"), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.CollectionMemberList.get(memberIndex).MemberName), 100, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", "मोबाइल क्र."), 0, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.CollectionMemberList.get(memberIndex).PhoneNo), 100, row_offset, text_paint);
        canvas.drawText(String.format("%s", "खाते क्र."), 320, row_offset, text_paint);
        canvas.drawText(String.format("%s%s", ":", mainActivity.CollectionMemberList.get(memberIndex).SavingAccNo), 400, row_offset, text_paint);
//bmp 15-May-26
        if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s%s", "ट्रांझॅक्शन आय.डी.:", mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionID), 0, row_offset, text_paint);
            SimpleDateFormat sdf_1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            SimpleDateFormat sdf_2 = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            try {
                Date dateTransaction = sdf_1.parse(mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionDate);
                String strTransactionDate = sdf_2.format(dateTransaction);
                canvas.drawText(String.format("%s%s", "तारीख:", strTransactionDate), 350, row_offset, text_paint);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
//bmp 01-Oct-26
////bmp 03-Jul-26
//            String strOnlinePaymentApp = "";
//            for(int k=0; k<OnlinePaymentAppsIDMaster.size(); k++) {
//                if(OnlinePaymentAppsIDMaster.get(k) == mainActivity.CollectionMemberList.get(memberIndex).OnlinePaymentAppID) {
//                    strOnlinePaymentApp = OnlinePaymentAppsMaster.get(k);
//                    break;
//                }
//            }
//            row_offset = row_offset + 30;
//            canvas.drawText(String.format("%s%s", "पेमेंट अँप:", strOnlinePaymentApp), 0, row_offset, text_paint);
////bmp 03-Jul-26
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s%s", "यु.पी.आय. आय.डी.:", mainActivity.CollectionMemberList.get(memberIndex).strOnlinePaymentUPIID), 0, row_offset, text_paint);
//bmp 01-Oct-26
        }
        row_offset = row_offset + 40;
//bmp 15-May-26
        int grid_offset = row_offset + 10;

        //Horizontal lines
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        canvas.drawLine(0, grid_offset, 575, grid_offset, border_paint);
        grid_offset = grid_offset + 40;
        //Vertical lines
        grid_offset = row_offset + 10;
        canvas.drawLine(0, grid_offset, 0, (grid_offset+320), border_paint);
        canvas.drawLine(400, grid_offset, 400, (grid_offset+280), border_paint);
        canvas.drawLine(575, grid_offset, 575, (grid_offset+320), border_paint);

        row_offset = row_offset + 35;
        canvas.drawText(String.format("%s", "कलेक्शन"), 100, row_offset, text_paint);
        canvas.drawText(String.format("%s", "एकूण रक्कम"), 440, row_offset, text_paint);
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s", "जास्तीचे आधारपूर्णा"), 10, row_offset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).ExtraADHPCollectionAmount)), 490, row_offset, text_paint);
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s", "नवीन आर.डी."), 10, row_offset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).NewRDCollectionAmount)), 490, row_offset, text_paint);
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s", "फिक्स डिपॉझिट"), 10, row_offset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).FDCollectionAmount)), 490, row_offset, text_paint);
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s", "जास्तीचे जे.एल.जी."), 10, row_offset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).ExtraJLGCollectionAmount)), 490, row_offset, text_paint);
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s", "रिप्रिंट शुल्क"), 10, row_offset, text_paint);
        canvas.drawText(String.format("%s", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).ReprintChargesCollectionAmount)), 490, row_offset, text_paint);
        row_offset = row_offset + 40;
//        canvas.drawText(String.format("%s", "एकूण अतिरिक्त कलेक्शन"), 0, row_offset, text_paint);
        if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
            canvas.drawText(String.format("%s", "एकूण अतिरिक्त ऑनलाईन कलेक्शन"), 0, row_offset, text_paint);
        } else {
            canvas.drawText(String.format("%s", "एकूण अतिरिक्त कॅश कलेक्शन"), 0, row_offset, text_paint);
        }
        canvas.drawText(String.format("%s", String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).ExtraCollectionAmount)), 490, row_offset, text_paint);
        String strInWords = "";
        strInWords = AmountInWords(mainActivity.CollectionMemberList.get(memberIndex).ExtraCollectionAmount);
        text_paint.setTextSize(20);
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s%s", "रु:", strInWords), 0, row_offset, text_paint);
        row_offset = row_offset + 40;
        if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
            text_paint.setTextSize(19);
//bmp 03-Jul-26
//            canvas.drawText(String.format("%s", getString(R.string.ExtraReceiptNote1_Line1)), 0, row_offset, text_paint);
//            row_offset = row_offset + 30;
//            canvas.drawText(String.format("%s", getString(R.string.ExtraReceiptNote1_Line2)), 0, row_offset, text_paint);
//            row_offset = row_offset + 30;
////            canvas.drawText(String.format("%s", getString(R.string.ExtraReceiptNote1_Line3)), 0, row_offset, text_paint);
////            row_offset = row_offset + 30;
//            canvas.drawText(String.format("%s", getString(R.string.ExtraReceiptNote2_Line1)), 0, row_offset, text_paint);
//            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.ExtraReceiptOnlineNote1_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.ExtraReceiptOnlineNote1_Line2)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.ExtraReceiptOnlineNote2_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.ExtraReceiptOnlineNote2_Line2)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.ExtraReceiptOnlineNote3_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.ExtraReceiptOnlineNote3_Line2)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
            canvas.drawText(String.format("%s", getString(R.string.ExtraReceiptOnlineNote4_Line1)), 0, row_offset, text_paint);
            row_offset = row_offset + 30;
//bmp 03-Jul-26
            text_paint.setTextSize(24);
        }
        String strNextRecoveryDate = selectedCollectionCenter.NextRecoveryDate;
        SimpleDateFormat sdfOne = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat sdfTwo = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        try {
            Date date1 = sdfOne.parse(selectedCollectionCenter.NextRecoveryDate);
            strNextRecoveryDate = sdfTwo.format(date1);
        } catch (ParseException e) {
//            throw new RuntimeException(e);
        }
        row_offset = row_offset + 10;
        canvas.drawText(String.format("%s%s", "पुढील महिन्याची कलेक्शन तारीख: ", strNextRecoveryDate), 0, row_offset, text_paint);

        String strNextRecoveryTime = selectedCollectionCenter.NextRecoveryTime;
        int hourOfDay = Integer.valueOf(strNextRecoveryTime.substring(0, 2));
        String strTime = "सकाळी ";
        if(hourOfDay < 17)
            strTime = "दुपारी ";
        else if(hourOfDay < 19)
            strTime = "संध्याकाळी ";
        else
            strTime = "रात्री ";
        SimpleDateFormat sdfThree = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat sdfFour = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        try {
            Date date1 = sdfThree.parse(strNextRecoveryTime);
            strNextRecoveryTime = strTime + sdfFour.format(date1);
        } catch (ParseException e) {
//            throw new RuntimeException(e);
        }
        row_offset = row_offset + 40;
        canvas.drawText(String.format("%s%s", "पुढील महिन्याची कलेक्शन वेळ: ", strNextRecoveryTime), 0, row_offset, text_paint);

        //एल. एस. ओ.
        row_offset = row_offset + 80;
        canvas.drawText(String.format("%s", "एल.एस.ओ. ची सही"), 20, row_offset, text_paint);
        row_offset = row_offset + 30;
        canvas.drawText(String.format("%s", mainActivity.LoginID), 20, row_offset, text_paint);
        row_offset = row_offset + 13;
        canvas.drawLine(0, row_offset, 575, row_offset, border_paint);
    }
    public void SQLite_getOnlinePaymentAppMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.OnlinePaymentApps.COLUMN_NAME_OnlinePaymentAppID,
                ufinDbContract.OnlinePaymentApps.COLUMN_NAME_Description,
        };
        String Active = "1";
        String selection1 = ufinDbContract.OnlinePaymentApps.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";
        Cursor c1 = db.query(
                ufinDbContract.OnlinePaymentApps.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        OnlinePaymentAppsIDMaster.clear();
        OnlinePaymentAppsMaster.clear();
        OnlinePaymentAppsIDMaster.add(0L);
        OnlinePaymentAppsMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            OnlinePaymentAppsIDMaster.add(Long.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.OnlinePaymentApps.COLUMN_NAME_OnlinePaymentAppID))));
                            OnlinePaymentAppsMaster.add(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.OnlinePaymentApps.COLUMN_NAME_Description)));
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

    public String AmountInWords(int iAmount) {
        String strInWords = "";

        String[] strUnits = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};
        String[] strTeens = {"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
        String[] strTens = {"", "Ten", "Twenty", "Thirty", "Fourty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
        String strLac = "";
        String strThousand = "";
        String strHundred = "";
        String strSubHundred = "";

        int iTemp = iAmount;
        int iTemp1 = iTemp / 100000;
        if(iTemp1 > 0) {
            if(iTemp1 < 10) {
                strLac = strUnits[iTemp1] + " Lac";
            } else if(iTemp1 < 20) {
                strLac = strTeens[iTemp1 - 10] + " Lac";
            } else {
                int iTemp2 = iTemp1 / 10;
                int iTemp3 = iTemp1 % 10;
                strLac = strTens[iTemp2] + " " + strUnits[iTemp3] + " Lac";
            }
        }
        iTemp = iTemp % 100000;
        iTemp1 = iTemp / 1000;
        if(iTemp1 > 0) {
            if(iTemp1 < 10) {
                strThousand = strUnits[iTemp1] + " Thousand";
            } else if(iTemp1 < 20) {
                strThousand = strTeens[iTemp1 - 10] + " Thousand";
            } else {
                int iTemp2 = iTemp1 / 10;
                int iTemp3 = iTemp1 % 10;
                strThousand = strTens[iTemp2] + " " + strUnits[iTemp3] + " Thousand";
            }
        }
        iTemp = iTemp % 1000;
        iTemp1 = iTemp / 100;
        if(iTemp1 > 0) {
            strHundred = strUnits[iTemp1] + " Hundred";
        }
        iTemp1 = iTemp % 100;
        if(iTemp1 > 0) {
            if(iTemp1 < 10) {
                strSubHundred = strUnits[iTemp1];
            } else if(iTemp1 < 20) {
                strSubHundred = strTeens[iTemp1 - 10];
            } else {
                int iTemp2 = iTemp1 / 10;
                int iTemp3 = iTemp1 % 10;
                strSubHundred = strTens[iTemp2] + " " + strUnits[iTemp3];
            }
        }

        if(!strLac.isEmpty()) {
            strInWords = strLac;
        }
        if(!strThousand.isEmpty()) {
            if(!strInWords.isEmpty())
                strInWords = strInWords + " " + strThousand;
            else
                strInWords = strThousand;
        }
        if(!strHundred.isEmpty()) {
            if(!strInWords.isEmpty())
                strInWords = strInWords + " " + strHundred;
            else
                strInWords = strHundred;
        }
        if(!strSubHundred.isEmpty()) {
            if(!strInWords.isEmpty())
                strInWords = strInWords + " " + strSubHundred;
            else
                strInWords = strSubHundred;
        }
        if(!strInWords.isEmpty())
            strInWords = strInWords + " " + "Only";

        return strInWords;
    }

    public boolean SendCollectionSMS(int memberIndex) {
        boolean Status = false;

        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        String strTimeStamp = sdf.format(date);// all done

        try {
            JSONObject postDataParams = new JSONObject();
            try {
                postDataParams.put(ufinDbContract.MemberRecovery.COLUMN_NAME_MemberID, mainActivity.CollectionMemberList.get(memberIndex).MemberID);
                postDataParams.put("ContactNo", mainActivity.CollectionMemberList.get(memberIndex).PhoneNo);
                postDataParams.put("SavingAccNo", mainActivity.CollectionMemberList.get(memberIndex).SavingAccNo);
                postDataParams.put("CollectionDate", strTimeStamp);
                postDataParams.put("CollectionAmount", mainActivity.CollectionMemberList.get(memberIndex).TotalCollectionAmount);
//bmp 06-Jul-26
                postDataParams.put("PaymentMode", 1);           //1-cash,6-online
                if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
                    postDataParams.put("PaymentMode", 6);           //1-cash,6-online
                }
//bmp 06-Jul-26
                postDataParams.put("SMSResponse", "NA");
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
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_DbTableName, "CollectionSMS");
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_updatedRecordID, Long.valueOf(mainActivity.CollectionMemberList.get(memberIndex).SavingAccNo));
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsNewRecord, "True");
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsReady, "True");
            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_PostDataParams, strDataParamsQuery);

            lTemp1 = db.insert(ufinDbContract.RecordsToSync.TABLE_NAME, null, values2);
            if (lTemp1 > 0) {
                MainActivity.clsRecordsToSync newRec = new MainActivity.clsRecordsToSync();
                newRec.SyncID = strTimeStamp;
                newRec.recordID = Long.valueOf(mainActivity.CollectionMemberList.get(memberIndex).SavingAccNo);
                newRec.recordInserted = true;
                newRec.recordReady = true;
                newRec.PostParams = strDataParamsQuery;
                mainActivity.SyncCollectionSMS.add(newRec);
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
                Status = true;
            }
        } finally {
            db.close();
        }

        return Status;
    }
    public boolean SQLite_updateCollectionMember(int memberIndex, String strModule, int iSubModuleID, boolean syncRecord) {
        boolean Status = false;
//bmp 16-Apr-26
        int iPreClosureReasonID = 0;
        int iPrePaymentORPreClosure = 0;
//bmp 16-Apr-26
        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        long lNowTime = cal.getTimeInMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        String strTimeStamp = sdf.format(date);// all done

        try {
            ContentValues values1 = new ContentValues();
            ContentValues values11 = new ContentValues();

            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_MemberID, mainActivity.CollectionMemberList.get(memberIndex).MemberID);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_TransactionTimestamp, lNowTime);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_TransactionDateTime, strTimeStamp);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_TransactionType, mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode);
//            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_OnlinePaymentAppID, mainActivity.CollectionMemberList.get(memberIndex).OnlinePaymentAppID);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_OnlinePaymentAppID, 0L);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_OnlineTransactionID, mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionID);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_OnlinePaymentUPIID, mainActivity.CollectionMemberList.get(memberIndex).strOnlinePaymentUPIID);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_OnlineTransactionDate, mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionDate);
//            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_ScreenShotDate, mainActivity.CollectionMemberList.get(memberIndex).strScreenShotDate);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_ScreenShotDate, "");
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_TransactionStatus, 1);
//bmp 10-Jun-26
//            if(strModule.equalsIgnoreCase("Loan")) {
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_LoanCurrentDueAmount, mainActivity.CollectionMemberList.get(memberIndex).LoanCurrentDueAmountRefday);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_LoanPastDueAmountTillToday, mainActivity.CollectionMemberList.get(memberIndex).LoanPastDueAmountTillRefday);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_LoanPastDuePlusCurrentDueAmt, mainActivity.CollectionMemberList.get(memberIndex).LoanPastDuePlusCurrentDueAmtRefday);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_LastEMIReceiptNumber, mainActivity.CollectionMemberList.get(memberIndex).LastEMIReceiptNumber);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PendingPrincipal, mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_ReferenceRecoveryDate, mainActivity.CollectionMemberList.get(memberIndex).ReferenceRecoveryDate);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_TotalOutstandingAmount, mainActivity.CollectionMemberList.get(memberIndex).TotalOutstandingAmount);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PastDue_Interest, mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_Expected_Interest, mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PastDue_Principal, mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal);
////bmp 29-May-26
//                //For Multiple Cash/Online Part-Payments within month ????????????????????
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_Expected_Principal, mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal);
////bmp 29-May-26
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PastDue_LatePaymentCharges, mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_Expected_LatePaymentCharges, mainActivity.CollectionMemberList.get(memberIndex).Expected_LatePaymentCharges);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PastDue_SD, mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PastDue_PolicyRenewalAmt, mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_Expected_SD, mainActivity.CollectionMemberList.get(memberIndex).Expected_SD);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_Expected_PolicyRenewalAmt, mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosurePrincipal, mainActivity.CollectionMemberList.get(memberIndex).PreClosurePrincipal);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PrePaymentPrincipalCollected, mainActivity.CollectionMemberList.get(memberIndex).PrePaymentPrincipalCollected);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureInterest, mainActivity.CollectionMemberList.get(memberIndex).PreClosureInterest);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureLatePayCharges, mainActivity.CollectionMemberList.get(memberIndex).PreClosureLatePayCharge);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureJLG, mainActivity.CollectionMemberList.get(memberIndex).PreClosureJLG);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosurePolicyRenewalAmt, mainActivity.CollectionMemberList.get(memberIndex).PreClosurePolicyRenewalAmt);
//            } else if(strModule.equalsIgnoreCase("AdditionalRepay")) {
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_LastEMIReceiptNumber, mainActivity.CollectionMemberList.get(memberIndex).LastEMIReceiptNumber);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PendingPrincipal, mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_ReferenceRecoveryDate, mainActivity.CollectionMemberList.get(memberIndex).ReferenceRecoveryDate);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_TotalOutstandingAmount, mainActivity.CollectionMemberList.get(memberIndex).TotalOutstandingAmount);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosurePrincipal, mainActivity.CollectionMemberList.get(memberIndex).PreClosurePrincipal);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PrePaymentPrincipalCollected, mainActivity.CollectionMemberList.get(memberIndex).PrePaymentPrincipalCollected);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureInterest, mainActivity.CollectionMemberList.get(memberIndex).PreClosureInterest);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureLatePayCharges, mainActivity.CollectionMemberList.get(memberIndex).PreClosureLatePayCharge);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureJLG, mainActivity.CollectionMemberList.get(memberIndex).PreClosureJLG);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosurePolicyRenewalAmt, mainActivity.CollectionMemberList.get(memberIndex).PreClosurePolicyRenewalAmt);
//            } else if(strModule.equalsIgnoreCase("ADHP")) {
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_ADHPContributionAmount, mainActivity.CollectionMemberList.get(memberIndex).ADHPContributionAmount);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_ADHPTotalExpectedAndPastDueAmt, mainActivity.CollectionMemberList.get(memberIndex).ADHPTotalExpectedAndPastDueAmt);
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_LastADHPReceiptNumber, mainActivity.CollectionMemberList.get(memberIndex).LastADHPReceiptNumber);
//            } else if(strModule.equalsIgnoreCase("Extra")) {
//                values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_LastExtraReceiptNumber, mainActivity.CollectionMemberList.get(memberIndex).LastExtraReceiptNumber);
//            }
//            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PrePaymentORPreClosure, mainActivity.CollectionMemberList.get(memberIndex).PrePaymentORPreClosure);
//
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_LoanCurrentDueAmount, mainActivity.CollectionMemberList.get(memberIndex).LoanCurrentDueAmountRefday);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_LoanPastDueAmountTillToday, mainActivity.CollectionMemberList.get(memberIndex).LoanPastDueAmountTillRefday);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_LoanPastDuePlusCurrentDueAmt, mainActivity.CollectionMemberList.get(memberIndex).LoanPastDuePlusCurrentDueAmtRefday);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_LastEMIReceiptNumber, mainActivity.CollectionMemberList.get(memberIndex).LastEMIReceiptNumber);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PendingPrincipal, mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_ReferenceRecoveryDate, mainActivity.CollectionMemberList.get(memberIndex).ReferenceRecoveryDate);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_TotalOutstandingAmount, mainActivity.CollectionMemberList.get(memberIndex).TotalOutstandingAmount);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PastDue_Interest, mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_Expected_Interest, mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PastDue_Principal, mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_Expected_Principal, mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PastDue_LatePaymentCharges, mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_Expected_LatePaymentCharges, mainActivity.CollectionMemberList.get(memberIndex).Expected_LatePaymentCharges);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PastDue_SD, mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PastDue_PolicyRenewalAmt, mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_Expected_SD, mainActivity.CollectionMemberList.get(memberIndex).Expected_SD);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_Expected_PolicyRenewalAmt, mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosurePrincipal, mainActivity.CollectionMemberList.get(memberIndex).PreClosurePrincipal);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PrePaymentPrincipalCollected, mainActivity.CollectionMemberList.get(memberIndex).PrePaymentPrincipalCollected);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureInterest, mainActivity.CollectionMemberList.get(memberIndex).PreClosureInterest);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureLatePayCharges, mainActivity.CollectionMemberList.get(memberIndex).PreClosureLatePayCharge);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureJLG, mainActivity.CollectionMemberList.get(memberIndex).PreClosureJLG);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosurePolicyRenewalAmt, mainActivity.CollectionMemberList.get(memberIndex).PreClosurePolicyRenewalAmt);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_ADHPContributionAmount, mainActivity.CollectionMemberList.get(memberIndex).ADHPContributionAmount);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_ADHPTotalExpectedAndPastDueAmt, mainActivity.CollectionMemberList.get(memberIndex).ADHPTotalExpectedAndPastDueAmt);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_LastADHPReceiptNumber, mainActivity.CollectionMemberList.get(memberIndex).LastADHPReceiptNumber);
//bmp 16-Jul-26
            mainActivity.CollectionMemberList.get(memberIndex).ExtraADHPAmountCollected =
                    mainActivity.CollectionMemberList.get(memberIndex).ExtraADHPAmountCollected +
                    mainActivity.CollectionMemberList.get(memberIndex).ExtraADHPCollectionAmount;
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_ExtraADHPAmountCollected, mainActivity.CollectionMemberList.get(memberIndex).ExtraADHPAmountCollected);
//bmp 16-Jul-26
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_LastExtraReceiptNumber, mainActivity.CollectionMemberList.get(memberIndex).LastExtraReceiptNumber);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PrePaymentORPreClosure, mainActivity.CollectionMemberList.get(memberIndex).PrePaymentORPreClosure);
//bmp 10-Jun-26
//Added following values to avoid null entries while inserting to database table.
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_TotalDueAmountsToPay, mainActivity.CollectionMemberList.get(memberIndex).TotalDueAmountsToPayRefday);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PastDue_Penalty, mainActivity.CollectionMemberList.get(memberIndex).PastDue_Penalty);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_Expected_Penalty, mainActivity.CollectionMemberList.get(memberIndex).Expected_Penalty);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureReasonID, mainActivity.CollectionMemberList.get(memberIndex).PreClosureReasonID);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureRequestDate, mainActivity.CollectionMemberList.get(memberIndex).PreClosureRequestDate);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureRequestStatus, mainActivity.CollectionMemberList.get(memberIndex).PreClosureRequestStatus);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureAmount, mainActivity.CollectionMemberList.get(memberIndex).PreClosureAmount);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PrePaymentReqMonths, mainActivity.CollectionMemberList.get(memberIndex).PrePaymentReqMonths);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PrePaymentReqAmount, mainActivity.CollectionMemberList.get(memberIndex).PrePaymentReqAmount);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PrePaymentApprovedMonths, mainActivity.CollectionMemberList.get(memberIndex).PrePaymentApprovedMonths);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PrePaymentApprovedAmount, mainActivity.CollectionMemberList.get(memberIndex).PrePaymentApprovedAmount);
            values1.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureRejectReasonID, mainActivity.CollectionMemberList.get(memberIndex).PreClosureRejectReasonID);
//bmp 10-Jun-26
            values11.put(ufinDbContract.MemberRecovery.COLUMN_NAME_PhoneNo, mainActivity.CollectionMemberList.get(memberIndex).PhoneNo);

            JSONObject postDataParams = new JSONObject();
            if(syncRecord) {
                try {
                    postDataParams.put(ufinDbContract.MemberRecovery.COLUMN_NAME_MemberName, mainActivity.CollectionMemberList.get(memberIndex).MemberName);
                    postDataParams.put(ufinDbContract.MemberRecovery.COLUMN_NAME_MemberID, mainActivity.CollectionMemberList.get(memberIndex).MemberID);
                    if(strModule.equalsIgnoreCase("Loan")) {
                        postDataParams.put("ModuleID", 1);  //LoanEMI
                        postDataParams.put("SubModuleID", 0);
                        postDataParams.put("ModuleReferenceID", mainActivity.CollectionMemberList.get(memberIndex).LoanNo);
                        postDataParams.put("CollectionAmount", mainActivity.CollectionMemberList.get(memberIndex).TotalEMICollectionAmount);
                        postDataParams.put("Act_Principal", mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentPrincipal);
                        postDataParams.put("Act_Interest", mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentInterest);
                        postDataParams.put("Act_LatePaymentCharges", mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentLateCharges);
                        postDataParams.put("Act_SD", mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentJLG);
                        postDataParams.put("Act_PolicyRenewalAmount", mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentPolicyRenewalAmt);
                        postDataParams.put("Act_Pend_Principal", mainActivity.CollectionMemberList.get(memberIndex).CollectedPastPrincipal);
                        postDataParams.put("Act_Pend_Interest", mainActivity.CollectionMemberList.get(memberIndex).CollectedPastInterest);
                        postDataParams.put("Act_Pend_LatePaymentCharges", mainActivity.CollectionMemberList.get(memberIndex).CollectedPastLateCharges);
                        postDataParams.put("Act_Pend_SD", mainActivity.CollectionMemberList.get(memberIndex).CollectedPastJLG);
                        postDataParams.put("Act_Pend_PolicyRenewalAmount", mainActivity.CollectionMemberList.get(memberIndex).CollectedPastPolicyRenewalAmt);
                        postDataParams.put("ReceiptNumber", mainActivity.CollectionMemberList.get(memberIndex).LastEMIReceiptNumber);
                        if(mainActivity.CollectionMemberList.get(memberIndex).LastEMIReceiptNumber.equalsIgnoreCase(mainActivity.CollectionMemberList.get(memberIndex).EMIReceiptNumber))
                            postDataParams.put("ReceiptType", "Regular");
                        else
                            postDataParams.put("ReceiptType", "Extra");
                    } else if(strModule.equalsIgnoreCase("AdditionalRepay")) {
                        postDataParams.put("ModuleID", 1);  //AdditionalRepay
                        postDataParams.put("SubModuleID", 0);
                        postDataParams.put("ModuleReferenceID", mainActivity.CollectionMemberList.get(memberIndex).LoanNo);
                        postDataParams.put("CollectionAmount", mainActivity.CollectionMemberList.get(memberIndex).AdditionalRepayCollectionAmount);
                        postDataParams.put("Act_Principal", mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentPrincipal);
                        postDataParams.put("Act_Interest", mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentInterest);
                        postDataParams.put("Act_LatePaymentCharges", mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentLateCharges);
                        postDataParams.put("Act_SD", mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentJLG);
                        postDataParams.put("Act_PolicyRenewalAmount", mainActivity.CollectionMemberList.get(memberIndex).CollectedCurrentPolicyRenewalAmt);
                        postDataParams.put("Act_Pend_Principal", mainActivity.CollectionMemberList.get(memberIndex).CollectedPastPrincipal);
                        postDataParams.put("Act_Pend_Interest", mainActivity.CollectionMemberList.get(memberIndex).CollectedPastInterest);
                        postDataParams.put("Act_Pend_LatePaymentCharges", mainActivity.CollectionMemberList.get(memberIndex).CollectedPastLateCharges);
                        postDataParams.put("Act_Pend_SD", mainActivity.CollectionMemberList.get(memberIndex).CollectedPastJLG);
                        postDataParams.put("Act_Pend_PolicyRenewalAmount", mainActivity.CollectionMemberList.get(memberIndex).CollectedPastPolicyRenewalAmt);
                        postDataParams.put("ReceiptNumber", mainActivity.CollectionMemberList.get(memberIndex).LastEMIReceiptNumber);
                        postDataParams.put("ReceiptType", "Extra");
//bmp 17-Apr-26
                        iPreClosureReasonID = mainActivity.CollectionMemberList.get(memberIndex).PreClosureReasonID;
                        iPrePaymentORPreClosure = mainActivity.CollectionMemberList.get(memberIndex).PrePaymentORPreClosure;
//bmp 17-Apr-26
                    } else if(strModule.equalsIgnoreCase("ADHP")) {
                        postDataParams.put("ModuleID", 11); //ADHP
                        postDataParams.put("SubModuleID", 0);
                        postDataParams.put("ModuleReferenceID", mainActivity.CollectionMemberList.get(memberIndex).ADHPDetailsID);
                        postDataParams.put("CollectionAmount", mainActivity.CollectionMemberList.get(memberIndex).TotalADHPCollectionAmount);
                        postDataParams.put("Act_Principal", 0);
                        postDataParams.put("Act_Interest", 0);
                        postDataParams.put("Act_LatePaymentCharges", 0);
                        postDataParams.put("Act_SD", 0);
                        postDataParams.put("Act_PolicyRenewalAmount", 0);
                        postDataParams.put("Act_Pend_Principal", 0);
                        postDataParams.put("Act_Pend_Interest", 0);
                        postDataParams.put("Act_Pend_LatePaymentCharges", 0);
                        postDataParams.put("Act_Pend_SD", 0);
                        postDataParams.put("Act_Pend_PolicyRenewalAmount", 0);
                        postDataParams.put("ReceiptNumber", mainActivity.CollectionMemberList.get(memberIndex).LastADHPReceiptNumber);
                        if(mainActivity.CollectionMemberList.get(memberIndex).LastADHPReceiptNumber.equalsIgnoreCase(mainActivity.CollectionMemberList.get(memberIndex).ADHPReceiptNumber))
                            postDataParams.put("ReceiptType", "Regular");
                        else
                            postDataParams.put("ReceiptType", "Extra");
                    } else if(strModule.equalsIgnoreCase("Extra")) {
                        postDataParams.put("SubModuleID", iSubModuleID);
                        switch (iSubModuleID) {
                            case 2:
                                postDataParams.put("ModuleID", 11); //ADHP
                                postDataParams.put("ModuleReferenceID", mainActivity.CollectionMemberList.get(memberIndex).ADHPDetailsID);
                                postDataParams.put("CollectionAmount", mainActivity.CollectionMemberList.get(memberIndex).ExtraADHPCollectionAmount);
                                break;
                            case 3:
                                postDataParams.put("ModuleID", 0); //Extra
                                postDataParams.put("ModuleReferenceID", 0);
                                postDataParams.put("CollectionAmount", mainActivity.CollectionMemberList.get(memberIndex).NewRDCollectionAmount);
                                break;
                            case 4:
                                break;
                            case 5:
                                postDataParams.put("ModuleID", 0); //Extra
                                postDataParams.put("ModuleReferenceID", 0);
                                postDataParams.put("CollectionAmount", mainActivity.CollectionMemberList.get(memberIndex).FDCollectionAmount);
                                break;
                            case 9:
                                postDataParams.put("ModuleID", 9);
                                postDataParams.put("ModuleReferenceID", 0);
                                postDataParams.put("CollectionAmount", mainActivity.CollectionMemberList.get(memberIndex).ExtraJLGCollectionAmount);
                                break;
                            case 10:
                                postDataParams.put("ModuleID", 0); //Extra
                                postDataParams.put("ModuleReferenceID", 0);
                                postDataParams.put("CollectionAmount", mainActivity.CollectionMemberList.get(memberIndex).ReprintChargesCollectionAmount);
                                break;
                        }
                        postDataParams.put("Act_Principal", 0);
                        postDataParams.put("Act_Interest", 0);
                        postDataParams.put("Act_LatePaymentCharges", 0);
                        postDataParams.put("Act_SD", 0);
                        postDataParams.put("Act_PolicyRenewalAmount", 0);
                        postDataParams.put("Act_Pend_Principal", 0);
                        postDataParams.put("Act_Pend_Interest", 0);
                        postDataParams.put("Act_Pend_LatePaymentCharges", 0);
                        postDataParams.put("Act_Pend_SD", 0);
                        postDataParams.put("Act_Pend_PolicyRenewalAmount", 0);
                        postDataParams.put("ReceiptNumber", mainActivity.CollectionMemberList.get(memberIndex).LastExtraReceiptNumber);
                        postDataParams.put("ReceiptType", "Extra");
                    }
                    postDataParams.put("PaymentMode", 1);           //1-cash,6-online
//bmp 12-May-26
//                    postDataParams.put("OnlinePaymentByName", "");
//                    postDataParams.put("OnlinePaymentByRelation", "");
//                    postDataParams.put("MemberDocumentRcvdID", 0);
//                    postDataParams.put("OnlinePaymentUPIID", "");
//                    postDataParams.put("OnlinePaymentTransactionID", "");
//                    postDataParams.put("OnlinePaymentTransactionDate", "");
//                    postDataParams.put("OnlineTransactionAmount", 0);
                    if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
                        postDataParams.put("PaymentMode", 6);           //1-cash,6-online
                        postDataParams.put("OnlinePaymentByMemberID", mainActivity.CollectionMemberList.get(memberIndex).OnlinePaymentByMemberID);
                        postDataParams.put("OnlinePaymentByName", mainActivity.CollectionMemberList.get(memberIndex).strOnlinePaymentByName);
                        postDataParams.put("OnlinePaymentByRelation", mainActivity.CollectionMemberList.get(memberIndex).OnlinePaymentByRelationID);
//bmp 01-Oct-26
//                        postDataParams.put("MemberDocumentRcvdID", mainActivity.CollectionMemberList.get(memberIndex).MemberDocumentRcvdID);
////bmp 08-Jul-26
//                        postDataParams.put("OnlinePaymentUPIID", "NA");
////bmp 08-Jul-26
                        postDataParams.put("OnlinePaymentUPIID", mainActivity.CollectionMemberList.get(memberIndex).strOnlinePaymentUPIID);
//                        postDataParams.put("OnlinePaymentAppID", mainActivity.CollectionMemberList.get(memberIndex).OnlinePaymentAppID);
//bmp 01-Oct-26
                        postDataParams.put("OnlinePaymentTransactionID", mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionID);
                        postDataParams.put("OnlinePaymentTransactionDate", mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionDate);
//bmp 01-Oct-26
//                        postDataParams.put("ScreenShotDate", mainActivity.CollectionMemberList.get(memberIndex).strScreenShotDate);
//bmp 01-Oct-26
                        postDataParams.put("OnlineTransactionAmount", mainActivity.CollectionMemberList.get(memberIndex).iOnlineTransactionAmount);
                    }
//bmp 12-May-26
                    postDataParams.put("CollectionDate", strTimeStamp);
                    String strTemp = mainActivity.CollectionMemberList.get(memberIndex).PhoneNo;
                    if(strTemp.equalsIgnoreCase(mainActivity.CollectionMemberList.get(memberIndex).CopyPhoneNo)) {
                        postDataParams.put("ContactNo", "NA");
                    } else {
                        postDataParams.put("ContactNo", strTemp);
                    }
                    postDataParams.put("NextCollectionDate", selectedCollectionCenter.NextRecoveryDateTime);
//bmp 16-Apr-26
//                    postDataParams.put("PreClosureReasonID", mainActivity.CollectionMemberList.get(memberIndex).PreClosureReasonID);
//                    postDataParams.put("PrePaymentORPreClosure", mainActivity.CollectionMemberList.get(memberIndex).PrePaymentORPreClosure);
                    postDataParams.put("PreClosureReasonID", iPreClosureReasonID);
                    postDataParams.put("PrePaymentORPreClosure", iPrePaymentORPreClosure);
//bmp 16-Apr-26
                    postDataParams.put("LSO_ID", mainActivity.LSO_ID);
                    postDataParams.put("Org_ID", mainActivity.OrgID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            long lTemp1 = 0;
            String StrNewRecord = "";
            String StrReady = "";
            String StrTableName = "";
//            String whereClause = ufinDbContract.MemberTransaction.COLUMN_NAME_MemberID + "=" + "'" + String.valueOf(mainActivity.CollectionMemberList.get(memberIndex).MemberID) + "'";
//            lTemp1 = db.update(ufinDbContract.MemberTransaction.TABLE_NAME, values1, whereClause, null);
            lTemp1 = db.insert(ufinDbContract.MemberTransaction.TABLE_NAME, null, values1);
            if (lTemp1 > 0) {
                System.out.println("MemberTransaction Updated.");
                StrTableName = ufinDbContract.MemberTransaction.TABLE_NAME;
                StrNewRecord = "False";
                StrReady = "True";
            }

            long lTemp11 = 0;
            String whereClause11 = ufinDbContract.MemberRecovery.COLUMN_NAME_SavingAccNo + "=" + "'" + mainActivity.CollectionMemberList.get(memberIndex).SavingAccNo + "'";
            lTemp11 = db.update(ufinDbContract.MemberRecovery.TABLE_NAME, values11, whereClause11, null);
            if (lTemp11 > 0) {
                System.out.println("MemberRecovery Updated.");
//                StrTableName = ufinDbContract.MemberRecovery.TABLE_NAME;
//                StrNewRecord = "False";
//                StrReady = "True";
            }

            ContentValues values2 = new ContentValues();

            if(syncRecord) {
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

                if (!StrNewRecord.isEmpty()) {
                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_SyncID, strTimeStamp);
                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_DbTableName, StrTableName);
                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_updatedRecordID, Long.valueOf(mainActivity.CollectionMemberList.get(memberIndex).SavingAccNo));
                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsNewRecord, StrNewRecord);
                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsReady, StrReady);
                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_PostDataParams, strDataParamsQuery);

                    lTemp1 = db.insert(ufinDbContract.RecordsToSync.TABLE_NAME, null, values2);
                    if (lTemp1 > 0) {
                        MainActivity.clsRecordsToSync newRec = new MainActivity.clsRecordsToSync();
                        newRec.SyncID = strTimeStamp;
                        newRec.recordID = Long.valueOf(mainActivity.CollectionMemberList.get(memberIndex).SavingAccNo);
                        newRec.recordInserted = StrNewRecord.equalsIgnoreCase("True");
                        newRec.recordReady = StrReady.equalsIgnoreCase("True");
                        newRec.PostParams = strDataParamsQuery;
                        mainActivity.SyncUpdateCollectionDetails.add(newRec);
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
                        Status = true;
                    }
                }
            }
            long lMemberID = mainActivity.CollectionMemberList.get(memberIndex).MemberID;
            String strContactNumber = mainActivity.CollectionMemberList.get(memberIndex).PhoneNo;
            values1 = new ContentValues();
            values1.put(ufinDbContract.MemberData.COLUMN_NAME_ContactNo, strContactNumber);
            String whereClause1 = ufinDbContract.MemberData.COLUMN_NAME_MemberID + "=" + "'" + String.valueOf(lMemberID) + "'";
            lTemp1 = db.update(ufinDbContract.MemberData.TABLE_NAME, values1, whereClause1, null);
            if (lTemp1 > 0) {
                System.out.println("MemberData Updated with ContactNo.");
            }
            values1 = new ContentValues();
            values1.put(ufinDbContract.PunarbhetSchedule.COLUMN_NAME_ContactNumber, strContactNumber);
            whereClause1 = ufinDbContract.PunarbhetSchedule.COLUMN_NAME_MemberID + "=" + "'" + String.valueOf(lMemberID) + "'";
            lTemp1 = db.update(ufinDbContract.PunarbhetSchedule.TABLE_NAME, values1, whereClause1, null);
            if (lTemp1 > 0) {
                System.out.println("PunarbhetSchedule Updated with ContactNo.");
            }
        } finally {
            db.close();
        }

        return Status;
    }
    public boolean SQLite_updateRDReceipt(int memberIndex, int rdIndex, boolean syncRecord) {
        boolean Status = false;

        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        long lNowTime = cal.getTimeInMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        String strTimeStamp = sdf.format(date);// all done

        try {
            ContentValues values1 = new ContentValues();
            long lRDDetailsID = 0;
            lRDDetailsID = mainActivity.RDReceiptList.get(rdIndex).RDDetailsID;
            values1.put(ufinDbContract.RDTransaction.COLUMN_NAME_RDDetailsID, mainActivity.RDReceiptList.get(rdIndex).RDDetailsID);
            values1.put(ufinDbContract.RDTransaction.COLUMN_NAME_TransactionTimestamp, lNowTime);
            values1.put(ufinDbContract.RDTransaction.COLUMN_NAME_TransactionDateTime, strTimeStamp);
            values1.put(ufinDbContract.RDTransaction.COLUMN_NAME_TransactionType, mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode);
//            values1.put(ufinDbContract.RDTransaction.COLUMN_NAME_OnlinePaymentAppID, mainActivity.CollectionMemberList.get(memberIndex).OnlinePaymentAppID);
            values1.put(ufinDbContract.RDTransaction.COLUMN_NAME_OnlinePaymentAppID, 0L);
            values1.put(ufinDbContract.RDTransaction.COLUMN_NAME_OnlineTransactionID, mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionID);
            values1.put(ufinDbContract.RDTransaction.COLUMN_NAME_OnlinePaymentUPIID, mainActivity.CollectionMemberList.get(memberIndex).strOnlinePaymentUPIID);
            values1.put(ufinDbContract.RDTransaction.COLUMN_NAME_OnlineTransactionDate, mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionDate);
//            values1.put(ufinDbContract.RDTransaction.COLUMN_NAME_ScreenShotDate, mainActivity.CollectionMemberList.get(memberIndex).strScreenShotDate);
            values1.put(ufinDbContract.RDTransaction.COLUMN_NAME_ScreenShotDate, "");
            values1.put(ufinDbContract.RDTransaction.COLUMN_NAME_TransactionStatus, 1);
//            values1.put(ufinDbContract.RDReceipts.COLUMN_NAME_PastDue, mainActivity.RDReceiptList.get(rdIndex).PastDue);
//            values1.put(ufinDbContract.RDReceipts.COLUMN_NAME_ExpectedInstallmentAmt, mainActivity.RDReceiptList.get(rdIndex).ExpectedInstallmentAmt);
//            values1.put(ufinDbContract.RDReceipts.COLUMN_NAME_LastReceiptNumber, mainActivity.RDReceiptList.get(rdIndex).LastReceiptNumber);
            values1.put(ufinDbContract.RDTransaction.COLUMN_NAME_PastDue, mainActivity.RDReceiptList.get(rdIndex).PastDue);
            values1.put(ufinDbContract.RDTransaction.COLUMN_NAME_ExpectedInstallmentAmt, mainActivity.RDReceiptList.get(rdIndex).ExpectedInstallmentAmt);
            values1.put(ufinDbContract.RDTransaction.COLUMN_NAME_LastReceiptNumber, mainActivity.RDReceiptList.get(rdIndex).LastReceiptNumber);
//bmp 12-Aug-26
            values1.put(ufinDbContract.RDTransaction.COLUMN_NAME_OperatingMemberID, mainActivity.RDReceiptList.get(rdIndex).OperatingMemberID);
//bmp 12-Aug-26

            JSONObject postDataParams = new JSONObject();
            if(syncRecord) {
                try {
                    postDataParams.put("MemberName", mainActivity.RDReceiptList.get(rdIndex).FirstAcHolder);
                    postDataParams.put("MemberID", mainActivity.RDReceiptList.get(rdIndex).FirstACMemberID);
                    postDataParams.put("ModuleID", 3);  //RD
                    postDataParams.put("SubModuleID", 0);
                    postDataParams.put("ModuleReferenceID", lRDDetailsID);
                    postDataParams.put("CollectionAmount", mainActivity.RDReceiptList.get(rdIndex).ActualAmountReceived);
                    postDataParams.put("Act_Principal", 0);
                    postDataParams.put("Act_Interest", 0);
                    postDataParams.put("Act_LatePaymentCharges", 0);
                    postDataParams.put("Act_SD", 0);
                    postDataParams.put("Act_PolicyRenewalAmount", 0);
                    postDataParams.put("Act_Pend_Principal", 0);
                    postDataParams.put("Act_Pend_Interest", 0);
                    postDataParams.put("Act_Pend_LatePaymentCharges", 0);
                    postDataParams.put("Act_Pend_SD", 0);
                    postDataParams.put("Act_Pend_PolicyRenewalAmount", 0);
                    postDataParams.put("ReceiptNumber", mainActivity.RDReceiptList.get(rdIndex).LastReceiptNumber);
                    if(mainActivity.RDReceiptList.get(rdIndex).LastReceiptNumber.equalsIgnoreCase(mainActivity.RDReceiptList.get(rdIndex).ReceiptNumber))
                        postDataParams.put("ReceiptType", "Regular");
                    else
                        postDataParams.put("ReceiptType", "Extra");
                    postDataParams.put("PaymentMode", 1);           //1 - cash , 2 - cheque, 3 - bank transfer
//bmp 12-May-26
//                    postDataParams.put("OnlinePaymentByName", "");
//                    postDataParams.put("OnlinePaymentByRelation", "");
//                    postDataParams.put("MemberDocumentRcvdID", 0);
//                    postDataParams.put("OnlinePaymentUPIID", "");
//                    postDataParams.put("OnlinePaymentTransactionID", "");
//                    postDataParams.put("OnlinePaymentTransactionDate", "");
//                    postDataParams.put("OnlineTransactionAmount", 0);
                    if(mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode.equalsIgnoreCase("Online")) {
                        postDataParams.put("PaymentMode", 6);           //1-cash,6-online
                        postDataParams.put("OnlinePaymentByMemberID", mainActivity.CollectionMemberList.get(memberIndex).OnlinePaymentByMemberID);
                        postDataParams.put("OnlinePaymentByName", mainActivity.CollectionMemberList.get(memberIndex).strOnlinePaymentByName);
                        postDataParams.put("OnlinePaymentByRelation", mainActivity.CollectionMemberList.get(memberIndex).OnlinePaymentByRelationID);
//                        postDataParams.put("MemberDocumentRcvdID", mainActivity.CollectionMemberList.get(memberIndex).MemberDocumentRcvdID);
//bmp 08-Jul-26
//                        postDataParams.put("OnlinePaymentUPIID", "NA");
//bmp 08-Jul-26
//                        postDataParams.put("OnlinePaymentAppID", mainActivity.CollectionMemberList.get(memberIndex).OnlinePaymentAppID);
                        postDataParams.put("OnlinePaymentUPIID", mainActivity.CollectionMemberList.get(memberIndex).strOnlinePaymentUPIID);
                        postDataParams.put("OnlinePaymentTransactionID", mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionID);
                        postDataParams.put("OnlinePaymentTransactionDate", mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionDate);
//                        postDataParams.put("ScreenShotDate", mainActivity.CollectionMemberList.get(memberIndex).strScreenShotDate);
                        postDataParams.put("OnlineTransactionAmount", mainActivity.CollectionMemberList.get(memberIndex).iOnlineTransactionAmount);
                    }
//bmp 12-May-26
                    postDataParams.put("CollectionDate", strTimeStamp);
                    String strTemp = mainActivity.CollectionMemberList.get(memberIndex).PhoneNo;
                    if(strTemp.equalsIgnoreCase(mainActivity.CollectionMemberList.get(memberIndex).CopyPhoneNo)) {
                        postDataParams.put("ContactNo", "NA");
                    } else {
                        postDataParams.put("ContactNo", strTemp);
                    }
                    postDataParams.put("NextCollectionDate", selectedCollectionCenter.NextRecoveryDateTime);
//bmp 17-Apr-26
//                    postDataParams.put("PreClosureReasonID", mainActivity.CollectionMemberList.get(memberIndex).PreClosureReasonID);
//                    postDataParams.put("PrePaymentORPreClosure", mainActivity.CollectionMemberList.get(memberIndex).PrePaymentORPreClosure);
                    postDataParams.put("PreClosureReasonID", 0);
                    postDataParams.put("PrePaymentORPreClosure", 0);
//bmp 17-Apr-26
                    postDataParams.put("LSO_ID", mainActivity.LSO_ID);
                    postDataParams.put("Org_ID", mainActivity.OrgID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            long lTemp1 = 0;
            ContentValues values2 = new ContentValues();
            String StrNewRecord = "";
            String StrReady = "";
//            String whereClause = ufinDbContract.RDReceipts.COLUMN_NAME_RDDetailsID + "=" + "'" + String.valueOf(lRDDetailsID) + "'";
//            lTemp1 = db.update(ufinDbContract.RDReceipts.TABLE_NAME, values1, whereClause, null);
            lTemp1 = db.insert(ufinDbContract.RDTransaction.TABLE_NAME, null, values1);
            if (lTemp1 > 0) {
                System.out.println("RDTransaction Updated.");
                StrNewRecord = "False";
                StrReady = "True";
            }
            //PhoneNo may be updated for Member having only RD Module
            ContentValues values3 = new ContentValues();
            values3.put(ufinDbContract.MemberRecovery.COLUMN_NAME_PhoneNo, mainActivity.CollectionMemberList.get(memberIndex).PhoneNo);
            String whereClause3 = ufinDbContract.MemberRecovery.COLUMN_NAME_SavingAccNo + "=" + "'" + mainActivity.CollectionMemberList.get(memberIndex).SavingAccNo + "'";
            lTemp1 = db.update(ufinDbContract.MemberRecovery.TABLE_NAME, values3, whereClause3, null);
            if (lTemp1 > 0) {
                System.out.println("MemberRecovery Updated.");
            }
//bmp 12-Aug-26
            //Also insert entry to MemberTransaction table
            ContentValues values4 = new ContentValues();
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_MemberID, mainActivity.CollectionMemberList.get(memberIndex).MemberID);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_TransactionTimestamp, lNowTime);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_TransactionDateTime, strTimeStamp);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_TransactionType, mainActivity.CollectionMemberList.get(memberIndex).strPaymentMode);
//            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_OnlinePaymentAppID, mainActivity.CollectionMemberList.get(memberIndex).OnlinePaymentAppID);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_OnlinePaymentAppID, 0L);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_OnlineTransactionID, mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionID);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_OnlinePaymentUPIID, mainActivity.CollectionMemberList.get(memberIndex).strOnlinePaymentUPIID);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_OnlineTransactionDate, mainActivity.CollectionMemberList.get(memberIndex).strOnlineTransactionDate);
//            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_ScreenShotDate, mainActivity.CollectionMemberList.get(memberIndex).strScreenShotDate);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_ScreenShotDate, "");
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_TransactionStatus, 1);

            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_LoanCurrentDueAmount, mainActivity.CollectionMemberList.get(memberIndex).LoanCurrentDueAmountRefday);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_LoanPastDueAmountTillToday, mainActivity.CollectionMemberList.get(memberIndex).LoanPastDueAmountTillRefday);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_LoanPastDuePlusCurrentDueAmt, mainActivity.CollectionMemberList.get(memberIndex).LoanPastDuePlusCurrentDueAmtRefday);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_LastEMIReceiptNumber, mainActivity.CollectionMemberList.get(memberIndex).LastEMIReceiptNumber);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PendingPrincipal, mainActivity.CollectionMemberList.get(memberIndex).PendingPrincipal);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_ReferenceRecoveryDate, mainActivity.CollectionMemberList.get(memberIndex).ReferenceRecoveryDate);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_TotalOutstandingAmount, mainActivity.CollectionMemberList.get(memberIndex).TotalOutstandingAmount);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PastDue_Interest, mainActivity.CollectionMemberList.get(memberIndex).PastDue_Interest);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_Expected_Interest, mainActivity.CollectionMemberList.get(memberIndex).Expected_Interest);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PastDue_Principal, mainActivity.CollectionMemberList.get(memberIndex).PastDue_Principal);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_Expected_Principal, mainActivity.CollectionMemberList.get(memberIndex).Expected_Principal);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PastDue_LatePaymentCharges, mainActivity.CollectionMemberList.get(memberIndex).PastDue_LatePaymentCharges);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_Expected_LatePaymentCharges, mainActivity.CollectionMemberList.get(memberIndex).Expected_LatePaymentCharges);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PastDue_SD, mainActivity.CollectionMemberList.get(memberIndex).PastDue_SD);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PastDue_PolicyRenewalAmt, mainActivity.CollectionMemberList.get(memberIndex).PastDue_PolicyRenewalAmt);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_Expected_SD, mainActivity.CollectionMemberList.get(memberIndex).Expected_SD);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_Expected_PolicyRenewalAmt, mainActivity.CollectionMemberList.get(memberIndex).Expected_PolicyRenewalAmt);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosurePrincipal, mainActivity.CollectionMemberList.get(memberIndex).PreClosurePrincipal);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PrePaymentPrincipalCollected, mainActivity.CollectionMemberList.get(memberIndex).PrePaymentPrincipalCollected);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureInterest, mainActivity.CollectionMemberList.get(memberIndex).PreClosureInterest);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureLatePayCharges, mainActivity.CollectionMemberList.get(memberIndex).PreClosureLatePayCharge);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureJLG, mainActivity.CollectionMemberList.get(memberIndex).PreClosureJLG);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosurePolicyRenewalAmt, mainActivity.CollectionMemberList.get(memberIndex).PreClosurePolicyRenewalAmt);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_ADHPContributionAmount, mainActivity.CollectionMemberList.get(memberIndex).ADHPContributionAmount);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_ADHPTotalExpectedAndPastDueAmt, mainActivity.CollectionMemberList.get(memberIndex).ADHPTotalExpectedAndPastDueAmt);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_LastADHPReceiptNumber, mainActivity.CollectionMemberList.get(memberIndex).LastADHPReceiptNumber);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_ExtraADHPAmountCollected, mainActivity.CollectionMemberList.get(memberIndex).ExtraADHPAmountCollected);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_LastExtraReceiptNumber, mainActivity.CollectionMemberList.get(memberIndex).LastExtraReceiptNumber);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PrePaymentORPreClosure, mainActivity.CollectionMemberList.get(memberIndex).PrePaymentORPreClosure);

            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_TotalDueAmountsToPay, mainActivity.CollectionMemberList.get(memberIndex).TotalDueAmountsToPayRefday);

            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PastDue_Penalty, mainActivity.CollectionMemberList.get(memberIndex).PastDue_Penalty);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_Expected_Penalty, mainActivity.CollectionMemberList.get(memberIndex).Expected_Penalty);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureReasonID, mainActivity.CollectionMemberList.get(memberIndex).PreClosureReasonID);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureRequestDate, mainActivity.CollectionMemberList.get(memberIndex).PreClosureRequestDate);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureRequestStatus, mainActivity.CollectionMemberList.get(memberIndex).PreClosureRequestStatus);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureAmount, mainActivity.CollectionMemberList.get(memberIndex).PreClosureAmount);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PrePaymentReqMonths, mainActivity.CollectionMemberList.get(memberIndex).PrePaymentReqMonths);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PrePaymentReqAmount, mainActivity.CollectionMemberList.get(memberIndex).PrePaymentReqAmount);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PrePaymentApprovedMonths, mainActivity.CollectionMemberList.get(memberIndex).PrePaymentApprovedMonths);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PrePaymentApprovedAmount, mainActivity.CollectionMemberList.get(memberIndex).PrePaymentApprovedAmount);
            values4.put(ufinDbContract.MemberTransaction.COLUMN_NAME_PreClosureRejectReasonID, mainActivity.CollectionMemberList.get(memberIndex).PreClosureRejectReasonID);

            lTemp1 = db.insert(ufinDbContract.MemberTransaction.TABLE_NAME, null, values4);
            if (lTemp1 > 0) {
                System.out.println("MemberTransaction Inserted.");
            }
//bmp 12-Aug-26
            if(syncRecord) {
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

                if (!StrNewRecord.isEmpty()) {
                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_SyncID, strTimeStamp);
                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_DbTableName, ufinDbContract.RDTransaction.TABLE_NAME);
                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_updatedRecordID, lRDDetailsID);
                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsNewRecord, StrNewRecord);
                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsReady, StrReady);
                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_PostDataParams, strDataParamsQuery);

                    lTemp1 = db.insert(ufinDbContract.RecordsToSync.TABLE_NAME, null, values2);
                    if (lTemp1 > 0) {
                        MainActivity.clsRecordsToSync newRec = new MainActivity.clsRecordsToSync();
                        newRec.SyncID = strTimeStamp;
                        newRec.recordID = lRDDetailsID;
                        newRec.recordInserted = StrNewRecord.equalsIgnoreCase("True");
                        newRec.recordReady = StrReady.equalsIgnoreCase("True");
                        newRec.PostParams = strDataParamsQuery;
                        mainActivity.SyncUpdateRDReceipt.add(newRec);
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
                        Status = true;
                    }
                }
            }
        } finally {
            db.close();
        }

        return Status;
    }
//Everytime LSO visits Center for Collection, Cash and Narration need to tally for that visit.
//Cash and Narration from previous visit should not be carried forward, as it can't be tallied.
    public boolean SQLite_getCenterCollection() {
        boolean Status = false;

        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String strDate = sdf.format(date);// all done

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.CenterCollection.COLUMN_NAME_CenterID,
                ufinDbContract.CenterCollection.COLUMN_NAME_NextRecoveryDate,
                ufinDbContract.CenterCollection.COLUMN_NAME_NextRecoveryTime,
                ufinDbContract.CenterCollection.COLUMN_NAME_NextRecoveryDateTime,
        };
        String selection1 = ufinDbContract.CenterCollection.COLUMN_NAME_CenterID + "=" + "'" + selectedCollectionCenter.CenterID + "'";

        Cursor c1 = db.query(
                ufinDbContract.CenterCollection.TABLE_NAME,  // The table to query
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
                        selectedCollectionCenter.NextRecoveryDate = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CenterCollection.COLUMN_NAME_NextRecoveryDate));
                        selectedCollectionCenter.NextRecoveryTime = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CenterCollection.COLUMN_NAME_NextRecoveryTime));
                        selectedCollectionCenter.NextRecoveryDateTime = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CenterCollection.COLUMN_NAME_NextRecoveryDateTime));

                        Status = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            if(c1 != null) { c1.close(); }
            db.close();
        }

        return Status;
    }
    public boolean SQLite_updateCenterCollection() {
        boolean Status = false;

        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        String strTimeStamp = sdf.format(date);// all done

        try {
            ContentValues values1 = new ContentValues();

            values1.put(ufinDbContract.CenterCollection.COLUMN_NAME_CenterID, selectedCollectionCenter.CenterID);
            values1.put(ufinDbContract.CenterCollection.COLUMN_NAME_CollectionTimeStamp, strTimeStamp);
            values1.put(ufinDbContract.CenterCollection.COLUMN_NAME_TotalOnlineCollection, selectedCollectionCenter.CenterOnlineCollectionTotal);
            values1.put(ufinDbContract.CenterCollection.COLUMN_NAME_TotalCollection, selectedCollectionCenter.CenterCollectionTotal);
            values1.put(ufinDbContract.CenterCollection.COLUMN_NAME_TotalCollectionValue, selectedCollectionCenter.CenterNarationValueTotal);
            values1.put(ufinDbContract.CenterCollection.COLUMN_NAME_Notes500Count, iCenterCount500Notes);
            values1.put(ufinDbContract.CenterCollection.COLUMN_NAME_Notes200Count, iCenterCount200Notes);
            values1.put(ufinDbContract.CenterCollection.COLUMN_NAME_Notes100Count, iCenterCount100Notes);
            values1.put(ufinDbContract.CenterCollection.COLUMN_NAME_Notes50Count, iCenterCount50Notes);
            values1.put(ufinDbContract.CenterCollection.COLUMN_NAME_Notes20Count, iCenterCount20Notes);
            values1.put(ufinDbContract.CenterCollection.COLUMN_NAME_Notes10Count, iCenterCount10Notes);
            values1.put(ufinDbContract.CenterCollection.COLUMN_NAME_Notes5Count, iCenterCount5Notes);
            values1.put(ufinDbContract.CenterCollection.COLUMN_NAME_Notes2Count, iCenterCount2Notes);
            values1.put(ufinDbContract.CenterCollection.COLUMN_NAME_Notes1Count, iCenterCount1Notes);

            values1.put(ufinDbContract.CenterCollection.COLUMN_NAME_NextRecoveryDate, selectedCollectionCenter.NextRecoveryDate);
            values1.put(ufinDbContract.CenterCollection.COLUMN_NAME_NextRecoveryTime, selectedCollectionCenter.NextRecoveryTime);
            values1.put(ufinDbContract.CenterCollection.COLUMN_NAME_NextRecoveryDateTime, selectedCollectionCenter.NextRecoveryDateTime);

            long lTemp1 = 0;
            ContentValues values2 = new ContentValues();
            String StrNewRecord = "";
            String StrReady = "";
            String whereClause = ufinDbContract.CenterCollection.COLUMN_NAME_CenterID + "=" + "'" + String.valueOf(selectedCollectionCenter.CenterID) + "'";
            lTemp1 = db.update(ufinDbContract.CenterCollection.TABLE_NAME, values1, whereClause, null);
            if (lTemp1 > 0) {
                System.out.println("CenterCollection Updated.");
                StrNewRecord = "False";
                StrReady = "True";
            } else {
                lTemp1 = db.insert(ufinDbContract.CenterCollection.TABLE_NAME, null, values1);
                if (lTemp1 > 0) {
                    System.out.println("CenterCollection Inserted.");
                    StrNewRecord = "False";
                    StrReady = "True";
                }
            }
        } finally {
            db.close();
        }

        return Status;
    }
    public boolean SQLite_updateCollectionSummary(boolean syncRecord) {
        boolean Status = false;

        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        String strTimeStamp = sdf.format(date);// all done
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String strDate = sdf2.format(date);// all done

        try {
            ContentValues values1 = new ContentValues();
            boolean newDay = false;
            if(lCollectionID == 0) {
                newDay = true;
            } else {
                String strCollectionID = String.valueOf(lCollectionID);
                String strCollectionDay = strCollectionID.substring(0, 2);
                String strToday = strDate.substring(8);
                //In case user never Logout at the end of the day,
                //and Login again at the start of a new day,
                //comparing today's date with collection date will be useful.
                if(!strCollectionDay.equalsIgnoreCase(strToday))
                    newDay = true;
            }
            if(newDay) {
                //Ensure NEW CollectionSummary entry Every NEW Day
                SimpleDateFormat sdf3 = new SimpleDateFormat("ddHHmmssSSS", Locale.ENGLISH);
                String Str1 = sdf3.format(date) + String.format("%08d", mainActivity.LSO_ID);
                Str1 = Str1.replaceFirst("^0+(?!$)", "");
                try {
                    lCollectionID = Long.parseLong(Str1);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_CollectionID, lCollectionID);

            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_CollectionDate, strDate);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalValueOnline, iTotalValueOnline);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalValueCash, iTotalValueCash);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCollectionValue, iTotalValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_Total500NotesCount, iTotalCount500Notes);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_Total200NotesCount, iTotalCount200Notes);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_Total100NotesCount, iTotalCount100Notes);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_Total50NotesCount, iTotalCount50Notes);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_Total20NotesCount, iTotalCount20Notes);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_Total10NotesCount, iTotalCount10Notes);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_Total5NotesCount, iTotalCount5Notes);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_Total2NotesCount, iTotalCount2Notes);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_Total1NotesCount, iTotalCount1Notes);

            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalEMIReceiptsCount, iTotalEMIReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalEMIReceiptsValue, iTotalEMIReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalADHPReceiptsCount, iTotalADHPReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalADHPReceiptsValue, iTotalADHPReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalRDReceiptsCount, iTotalRDReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalRDReceiptsValue, iTotalRDReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalAdditionalRepayReceiptsCount, iTotalAdditionalEMIReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalAdditionalRepayReceiptsValue, iTotalAdditionalEMIReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalExtraReceiptsCount, iTotalExtraReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalExtraReceiptsValue, iTotalExtraReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalPreClosureAdditionalEMIReceiptsCount, iTotalPreClosureAdditionalEMIReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalPreClosureAdditionalEMIReceiptsValue, iTotalPreClosureAdditionalEMIReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalADHPExtraReceiptsCount, iTotalADHPExtraReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalADHPExtraReceiptsValue, iTotalADHPExtraReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalNewRDExtraReceiptsCount, iTotalNewRDExtraReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalNewRDExtraReceiptsValue, iTotalNewRDExtraReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalFDExtraReceiptsCount, iTotalFDExtraReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalFDExtraReceiptsValue, iTotalFDExtraReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalJLGExtraReceiptsCount, iTotalJLGExtraReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalJLGExtraReceiptsValue, iTotalJLGExtraReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalReprintChargesExtraReceiptsCount, iTotalReprintChargesExtraReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalReprintChargesExtraReceiptsValue, iTotalReprintChargesExtraReceiptsValue);

            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashEMIReceiptsCount, iTotalCashEMIReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashEMIReceiptsValue, iTotalCashEMIReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashADHPReceiptsCount, iTotalCashADHPReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashADHPReceiptsValue, iTotalCashADHPReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashRDReceiptsCount, iTotalCashRDReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashRDReceiptsValue, iTotalCashRDReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashAdditionalRepayReceiptsCount, iTotalCashAdditionalEMIReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashAdditionalRepayReceiptsValue, iTotalCashAdditionalEMIReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashExtraReceiptsCount, iTotalCashExtraReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashExtraReceiptsValue, iTotalCashExtraReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashPreClosureAdditionalEMIReceiptsCount, iTotalCashPreClosureAdditionalEMIReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashPreClosureAdditionalEMIReceiptsValue, iTotalCashPreClosureAdditionalEMIReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashADHPExtraReceiptsCount, iTotalCashADHPExtraReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashADHPExtraReceiptsValue, iTotalCashADHPExtraReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashNewRDExtraReceiptsCount, iTotalCashNewRDExtraReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashNewRDExtraReceiptsValue, iTotalCashNewRDExtraReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashFDExtraReceiptsCount, iTotalCashFDExtraReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashFDExtraReceiptsValue, iTotalCashFDExtraReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashJLGExtraReceiptsCount, iTotalCashJLGExtraReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashJLGExtraReceiptsValue, iTotalCashJLGExtraReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashReprintChargesExtraReceiptsCount, iTotalCashReprintChargesExtraReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashReprintChargesExtraReceiptsValue, iTotalCashReprintChargesExtraReceiptsValue);

            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineEMIReceiptsCount, iTotalOnlineEMIReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineEMIReceiptsValue, iTotalOnlineEMIReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineADHPReceiptsCount, iTotalOnlineADHPReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineADHPReceiptsValue, iTotalOnlineADHPReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineRDReceiptsCount, iTotalOnlineRDReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineRDReceiptsValue, iTotalOnlineRDReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineAdditionalRepayReceiptsCount, iTotalOnlineAdditionalEMIReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineAdditionalRepayReceiptsValue, iTotalOnlineAdditionalEMIReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineExtraReceiptsCount, iTotalOnlineExtraReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineExtraReceiptsValue, iTotalOnlineExtraReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlinePreClosureAdditionalEMIReceiptsCount, iTotalOnlinePreClosureAdditionalEMIReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlinePreClosureAdditionalEMIReceiptsValue, iTotalOnlinePreClosureAdditionalEMIReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineADHPExtraReceiptsCount, iTotalOnlineADHPExtraReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineADHPExtraReceiptsValue, iTotalOnlineADHPExtraReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineNewRDExtraReceiptsCount, iTotalOnlineNewRDExtraReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineNewRDExtraReceiptsValue, iTotalOnlineNewRDExtraReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineFDExtraReceiptsCount, iTotalOnlineFDExtraReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineFDExtraReceiptsValue, iTotalOnlineFDExtraReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineJLGExtraReceiptsCount, iTotalOnlineJLGExtraReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineJLGExtraReceiptsValue, iTotalOnlineJLGExtraReceiptsValue);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineReprintChargesExtraReceiptsCount, iTotalOnlineReprintChargesExtraReceiptsCount);
            values1.put(ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineReprintChargesExtraReceiptsValue, iTotalOnlineReprintChargesExtraReceiptsValue);

            JSONObject postDataParams = new JSONObject();
            if(syncRecord) {
                try {
                    postDataParams.put(ufinDbContract.CollectionSummary.COLUMN_NAME_CollectionDate, strTimeStamp);

                    postDataParams.put("Nar_2000", 0);
                    postDataParams.put("Nar_500", iTotalCount500Notes);
                    postDataParams.put("Nar_200", iTotalCount200Notes);
                    postDataParams.put("Nar_100", iTotalCount100Notes);
                    postDataParams.put("Nar_50", iTotalCount50Notes);
                    postDataParams.put("Nar_20", iTotalCount20Notes);
                    postDataParams.put("Nar_10", iTotalCount10Notes);
                    postDataParams.put("Nar_5", iTotalCount5Notes);
                    postDataParams.put("Nar_2", iTotalCount2Notes);
                    postDataParams.put("Nar_1", iTotalCount1Notes);
//bmp 12-May-26
                    postDataParams.put("OnlineCollection", iTotalValueOnline);
//bmp 12-May-26
                    postDataParams.put("LSO_ID", mainActivity.LSO_ID);
                    postDataParams.put("Org_ID", mainActivity.OrgID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            long lTemp1 = 0;
            ContentValues values2 = new ContentValues();
            String StrNewRecord = "";
            String StrReady = "";
            String whereClause = ufinDbContract.CollectionSummary.COLUMN_NAME_CollectionDate + "=" + "'" + strDate + "'";
            lTemp1 = db.update(ufinDbContract.CollectionSummary.TABLE_NAME, values1, whereClause, null);
            if (lTemp1 > 0) {
                System.out.println("CollectionSummary Updated.");
                StrNewRecord = "False";
                StrReady = "True";
            } else {
                lTemp1 = db.insert(ufinDbContract.CollectionSummary.TABLE_NAME, null, values1);
                if (lTemp1 > 0) {
                    System.out.println("CollectionSummary Inserted.");
                    StrNewRecord = "False";
                    StrReady = "True";
                }
            }

            if(syncRecord) {
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

                if (!StrNewRecord.isEmpty()) {
                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_SyncID, strTimeStamp);
                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_DbTableName, ufinDbContract.CollectionSummary.TABLE_NAME);
                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_updatedRecordID, lCollectionID);
                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsNewRecord, StrNewRecord);
                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsReady, StrReady);
                    values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_PostDataParams, strDataParamsQuery);

                    lTemp1 = db.insert(ufinDbContract.RecordsToSync.TABLE_NAME, null, values2);
                    if (lTemp1 > 0) {
                        MainActivity.clsRecordsToSync newRec = new MainActivity.clsRecordsToSync();
                        newRec.SyncID = strTimeStamp;
                        newRec.recordID = lCollectionID;
                        newRec.recordInserted = StrNewRecord.equalsIgnoreCase("True");
                        newRec.recordReady = StrReady.equalsIgnoreCase("True");
                        newRec.PostParams = strDataParamsQuery;
                        mainActivity.SyncUpdateCollectionNarration.add(newRec);
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
                        Status = true;
                    }
                }
            }
        } finally {
            db.close();
        }

        return Status;
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

    public void SQLite_getPrePaymentPreClosureRejectionReasonMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonID,
                ufinDbContract.ReasonMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.ReasonMaster.COLUMN_NAME_Reason,
        };

        String selection1 = ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonType + "=" + "'" + "Prepayment/preclosure Rejection Reason" + "'"
                + " and " + ufinDbContract.ReasonMaster.COLUMN_NAME_IsActive + "=" + "'" + String.valueOf(1) + "'";
        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.ReasonMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        PrePaymentPreClosureRejectionReasonIDs.clear();
        PrePaymentPreClosureRejectionReasonIDs.add(0);
        PrePaymentPreClosureRejectionReasons.clear();
        PrePaymentPreClosureRejectionReasons.add("?");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            String str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_MarathiDescription));
                            if ( (str1 == null) || str1.isEmpty() ) {
                                str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_Reason));
                                if (str1 == null) {
                                    str1 = "?";
                                }
                            }
                            str1 = str1.trim();
                            PrePaymentPreClosureRejectionReasons.add(str1);
                            PrePaymentPreClosureRejectionReasonIDs.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonID))));
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
    public void SQLite_getPrePaymentUpto3MonthsReasonMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonID,
                ufinDbContract.ReasonMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.ReasonMaster.COLUMN_NAME_Reason,
        };

        String selection1 = ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonType + "=" + "'" + "Prepayment Upto 3 Months" + "'"
                + " and " + ufinDbContract.ReasonMaster.COLUMN_NAME_IsActive + "=" + "'" + String.valueOf(1) + "'";
        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.ReasonMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        PrePaymentReasonIDs.clear();
        PrePaymentReasonIDs.add(0);
        PrePaymentPreClosureReasons.clear();
        PrePaymentPreClosureReasons.add("?");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            String str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_MarathiDescription));
                            if ( (str1 == null) || str1.isEmpty() ) {
                                str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_Reason));
                                if (str1 == null) {
                                    str1 = "?";
                                }
                            }
                            str1 = str1.trim();
                            PrePaymentPreClosureReasons.add(str1);
                            PrePaymentReasonIDs.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonID))));
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
    public void SQLite_getPreClosureUpto3MonthsReasonMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonID,
                ufinDbContract.ReasonMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.ReasonMaster.COLUMN_NAME_Reason,
        };

        String selection1 = ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonType + "=" + "'" + "Preclosure Upto 3 Months" + "'"
                + " and " + ufinDbContract.ReasonMaster.COLUMN_NAME_IsActive + "=" + "'" + String.valueOf(1) + "'";
        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.ReasonMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        PreClosureReasonIDs.clear();
        PreClosureReasonIDs.add(0);
        PrePaymentPreClosureReasons.clear();
        PrePaymentPreClosureReasons.add("?");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            String str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_MarathiDescription));
                            if ( (str1 == null) || str1.isEmpty() ) {
                                str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_Reason));
                                if (str1 == null) {
                                    str1 = "?";
                                }
                            }
                            str1 = str1.trim();
                            PrePaymentPreClosureReasons.add(str1);
                            PreClosureReasonIDs.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonID))));
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
    public void SQLite_getPrePaymentMorethan3MonthsReasonMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonID,
                ufinDbContract.ReasonMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.ReasonMaster.COLUMN_NAME_Reason,
        };

        String selection1 = ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonType + "=" + "'" + "Prepayment More than 3 Months" + "'"
                + " and " + ufinDbContract.ReasonMaster.COLUMN_NAME_IsActive + "=" + "'" + String.valueOf(1) + "'";
        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.ReasonMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        PrePaymentReasonIDs.clear();
        PrePaymentReasonIDs.add(0);
        PrePaymentPreClosureReasons.clear();
        PrePaymentPreClosureReasons.add("?");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            String str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_MarathiDescription));
                            if ( (str1 == null) || str1.isEmpty() ) {
                                str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_Reason));
                                if (str1 == null) {
                                    str1 = "?";
                                }
                            }
                            str1 = str1.trim();
                            PrePaymentPreClosureReasons.add(str1);
                            PrePaymentReasonIDs.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonID))));
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
    public void SQLite_getPreClosureMorethan3MonthsReasonMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonID,
                ufinDbContract.ReasonMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.ReasonMaster.COLUMN_NAME_Reason,
        };

        String selection1 = ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonType + "=" + "'" + "Preclosure More than 3 Months" + "'"
                + " and " + ufinDbContract.ReasonMaster.COLUMN_NAME_IsActive + "=" + "'" + String.valueOf(1) + "'";
        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.ReasonMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        PreClosureReasonIDs.clear();
        PreClosureReasonIDs.add(0);
        PrePaymentPreClosureReasons.clear();
        PrePaymentPreClosureReasons.add("?");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            String str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_MarathiDescription));
                            if ( (str1 == null) || str1.isEmpty() ) {
                                str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_Reason));
                                if (str1 == null) {
                                    str1 = "?";
                                }
                            }
                            str1 = str1.trim();
                            PrePaymentPreClosureReasons.add(str1);
                            PreClosureReasonIDs.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonID))));
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
