package com.example.ufin_lso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
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
    import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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
    import androidx.core.content.ContextCompat;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RevisitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RevisitFragment extends Fragment implements View.OnClickListener {

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
    public static final int ufin_REVISIT_DIALOG_STATE_REVISIT_PENDING               = 1;
    public static final int ufin_REVISIT_DIALOG_STATE_MEMBER_DEATH_CONFIRM          = 2;
    public static final int ufin_REVISIT_DIALOG_STATE_CLOSE                         = 3;
    public static final int ufin_REVISIT_DIALOG_STATE_ADDITIONAL_JLG                = 4;


    public static final String MSG_ufin_REVISIT_PENDING =
            "पुनर्भेट रिमार्क भरणे बाकी:";
//bmp 25-Aug-25
//    public static final String MSG_ufin_REVISIT_CONFIRM_MEMBER_DEATH =
//            "पुनर्भेट रिमार्क: सभासदाचा नुकताच मृत्यु झाला आहे. Press OK to Confirm.";
    public static final String MSG_ufin_REVISIT_CONFIRM_MEMBER_DEATH =
            "सभासदाचा नुकताच मृत्यु झाला आहे?\nPress OK to Confirm.";
//bmp 25-Aug-25
    public static final String MSG_ufin_REVISIT_CONFIRM_CLOSE =
            "Press OK to Close Without Saving Changes";

    public AlertDialog.Builder dlgAlert;
    public static int dlgState = ufin_REVISIT_DIALOG_STATE_NONE;;

    final ArrayList<String> alRevisitDate = new ArrayList<>();
    final ArrayList<String> alRevisitDateDDMMYY = new ArrayList<>();
    final ArrayList<String> alRevisitListType = new ArrayList<>();
    String strSelectDate, strSelectListType;
    long lSelectCommunityID;
    final ArrayList<Long> alMDWithinLimitGroupID = new ArrayList<>();
    final ArrayList<Integer> alMDWithinLimitGroup_MemerIndex = new ArrayList<>();
    public static final int MAX_PHOTO_FILESIZE             = (4 * 1024 * 1024);
//    public static final int MAX_PHOTO_FILESIZE             = (8 * 1024 * 1024);
    int IDRequest = -1;
    String strFilepath = "";
    String strFilename = "unknown";
    int selectedRevisitMemberIndex = -1;
    int selectedVisitDetailsID = -1;

    public RevisitFragment() {
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
    public static RevisitFragment newInstance(String param1, String param2) {
        RevisitFragment fragment = new RevisitFragment();
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
//        return inflater.inflate(R.layout.fragment_revisit, container, false);
        rootView = inflater.inflate(R.layout.fragment_revisit, container, false);
        mainActivity = (MainActivity) getActivity();

        mainActivity.SQLite_getPunarbhetVisitDetailsMaster();
        mainActivity.SQLite_getLastPunarbhetVisitDetailsMaster();
//bmp 24-Apr-24
        mainActivity.SQLite_getNextLoanNotAllowedMaster();
//bmp 24-Apr-24
        ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarRevisit);
        if (pbSync != null) {
            pbSync.setIndeterminate(true);
            pbSync.setVisibility(View.VISIBLE);
        }

        Button bt = (Button) rootView.findViewById(R.id.buttonCloseRevisit);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonSaveRevisit);
        bt.setOnClickListener(this);

        bt = (Button) rootView.findViewById(R.id.buttonSaveRevisit);
        bt.setEnabled(false);

        strSelectDate = "";
        strSelectListType = "";
        lSelectCommunityID = 0L;

//        final ArrayList<String> alRevisitDate = new ArrayList<>();
//        final ArrayList<String> alRevisitDateDDMMYY = new ArrayList<>();
        alRevisitDate.clear();
        alRevisitDate.add("");
        alRevisitDateDDMMYY.clear();
        alRevisitDateDDMMYY.add("");
/////////////////////////////////////////////////////////
        final ArrayList<Date> alDateRevisitDate = new ArrayList<>();
        alDateRevisitDate.clear();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
/////////////////////////////////////////////////////////
        for(int i=0; i<mainActivity.RevisitMemberList.size(); i++) {
//bmp 08-Aug-25
            if(mainActivity.RevisitMemberList.get(i).VisitDate.length() < 10)
                continue;
//bmp 08-Aug-25
            String strTemp = mainActivity.RevisitMemberList.get(i).VisitDate.substring(0, 10);
            boolean newFlag = true;
//            for(int j=0; j<alRevisitDate.size(); j++) {
//                if(strTemp.equalsIgnoreCase(alRevisitDate.get(j))) {
//                    newFlag = false;
//                    break;
//                }
//            }
            for(int j=0; j<alDateRevisitDate.size(); j++) {
                String strTemp1 = sdf.format(alDateRevisitDate.get(j));
                if(strTemp.equalsIgnoreCase(strTemp1)) {
                    newFlag = false;
                    break;
                }
            }
            if(newFlag) {
//                alRevisitDate.add(strTemp);
//                String strTemp2 =   strTemp.substring(8, 10) + "-" +
//                        strTemp.substring(5, 7) + "-" +
//                        strTemp.substring(2, 4);
//                alRevisitDateDDMMYY.add(strTemp2);
/////////////////////////////////////////////////////////
                try {
                    Date revisitDate = sdf.parse(strTemp);
                    alDateRevisitDate.add(revisitDate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
/////////////////////////////////////////////////////////
            }
        }
/////////////////////////////////////////////////////////
        alDateRevisitDate.sort(Comparator.naturalOrder());
        for(int i=0; i<alDateRevisitDate.size(); i++) {
            String strTemp = sdf.format(alDateRevisitDate.get(i));
            alRevisitDate.add(strTemp);
            String strTemp2 =   strTemp.substring(8, 10) + "-" +
                    strTemp.substring(5, 7) + "-" +
                    strTemp.substring(2, 4);
            alRevisitDateDDMMYY.add(strTemp2);
        }
/////////////////////////////////////////////////////////

        ArrayAdapter<String> adapterVisitDetailsDate = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item_22sp, alRevisitDateDDMMYY);
        adapterVisitDetailsDate.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_22sp);
        Spinner spSelectDate = (Spinner) rootView.findViewById(R.id.spinnerRevisitDate);
        spSelectDate.setAdapter(adapterVisitDetailsDate);
        spSelectDate.setSelection(0);
        spSelectDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSelectDate = "";
//                if(position == 0)
//                    return;
//                Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerRevisitListType);
//                sp.setSelection(0);
//                sp = (Spinner) rootView.findViewById(R.id.spinnerCluster);
//                sp.setSelection(0);
//                sp = (Spinner) rootView.findViewById(R.id.spinnerCommunity);
//                sp.setSelection(0);
                strSelectDate = alRevisitDate.get(position);
//                RefreshVisitListDate();
//bmp 25-Jun-25
//                RefreshVisitList();
                if(     strSelectDate.isEmpty() &&
                        strSelectListType.isEmpty() &&
                        (lSelectCommunityID == 0) &&
                        (mainActivity.selectedCollectionCenterID == 0)
                ) {
                    ClearVisitList();
                } else {
                    RefreshVisitList();
                }
//bmp 25-Jun-25
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spSelectDate.setFocusable(true);
        spSelectDate.setFocusableInTouchMode(true);

        alRevisitListType.clear();
        alRevisitListType.add("");
        alRevisitListType.add("Yellow-I");
        alRevisitListType.add("Yellow-II");
        alRevisitListType.add("Pink-I");
        alRevisitListType.add("Pink-II");
        alRevisitListType.add("Pink-III");
        alRevisitListType.add("Pista-I");
        alRevisitListType.add("Pista-II");
        alRevisitListType.add("Cream");
//bmp 26-Mar-26
//        alRevisitListType.add("White");
        alRevisitListType.add("White-I");
        alRevisitListType.add("White-II");
//bmp 26-Mar-26

        ArrayAdapter<String> adapterVisitDetailsListType = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                R.layout.simple_spinner_item_22sp, alRevisitListType);
        adapterVisitDetailsListType.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_22sp);
        Spinner spSelectListType = (Spinner) rootView.findViewById(R.id.spinnerRevisitListType);
        spSelectListType.setAdapter(adapterVisitDetailsListType);
        spSelectListType.setSelection(0);
        spSelectListType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSelectListType = "";
                strSelectListType = alRevisitListType.get(position);
                if(     strSelectDate.isEmpty() &&
                        strSelectListType.isEmpty() &&
                        (lSelectCommunityID == 0) &&
                        (mainActivity.selectedCollectionCenterID == 0)
                ) {
                    ClearVisitList();
                } else {
                    RefreshVisitList();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spSelectListType.setFocusable(true);
        spSelectListType.setFocusableInTouchMode(true);

        final ArrayList<Long> alRevisitCommunityID = new ArrayList<>();
        alRevisitCommunityID.clear();
        alRevisitCommunityID.add(0L);
        final ArrayList<String> alRevisitCommunity = new ArrayList<>();
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
                R.layout.simple_spinner_item_22sp, alRevisitCommunity);
        adapterVisitDetailsCommunity.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_22sp);
        Spinner spSelectCommunity = (Spinner) rootView.findViewById(R.id.spinnerCommunity);
        spSelectCommunity.setAdapter(adapterVisitDetailsCommunity);
        spSelectCommunity.setSelection(0);
        spSelectCommunity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lSelectCommunityID = 0;
                lSelectCommunityID = alRevisitCommunityID.get(position);
                if(     strSelectDate.isEmpty() &&
                        strSelectListType.isEmpty() &&
                        (lSelectCommunityID == 0) &&
                        (mainActivity.selectedCollectionCenterID == 0)
                ) {
                    ClearVisitList();
                } else {
                    RefreshVisitList();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spSelectCommunity.setFocusable(true);
        spSelectCommunity.setFocusableInTouchMode(true);

        bt = (Button) rootView.findViewById(R.id.buttonSaveRevisit);
        bt.setEnabled(true);

        if (pbSync != null) {
            pbSync.setIndeterminate(true);
            pbSync.setVisibility(View.INVISIBLE);
        }
        if(mainActivity.selectedCollectionCenterID == 0) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Calendar cal = Calendar.getInstance();
            Date currentDate = cal.getTime();
            String strDate = sdf1.format(currentDate);
            for(int i=0; i<alRevisitDate.size(); i++) {
                if(strDate.equalsIgnoreCase(alRevisitDate.get(i))) {
                    spSelectDate = (Spinner) rootView.findViewById(R.id.spinnerRevisitDate);
                    spSelectDate.setSelection(i);
                    break;
                }
            }
            alMDWithinLimitGroupID.clear();
            cal = Calendar.getInstance();
            long lToday = cal.getTimeInMillis();
            long lThreeMonths = (1000L * 60L * 60L * 24L * 90L);
            long lMDWithin = lToday + lThreeMonths;
            for(int i=0; i<mainActivity.RevisitMemberList.size(); i++) {
                if(!mainActivity.RevisitMemberList.get(i).VisitDate.equalsIgnoreCase(strDate))
                    continue;
                if(     (!mainActivity.RevisitMemberList.get(i).ListType.equalsIgnoreCase("Yellow-I")) &&
                        (!mainActivity.RevisitMemberList.get(i).ListType.equalsIgnoreCase("Yellow-II")) )
                    continue;
                if(mainActivity.RevisitMemberList.get(i).LongMaturityDate > lMDWithin)
                    continue;
                boolean flagNew = true;
                for(int j=0; j<alMDWithinLimitGroupID.size(); j++) {
                    if(alMDWithinLimitGroupID.get(j) == mainActivity.RevisitMemberList.get(i).GroupID) {
                        flagNew = false;
                        break;
                    }
                }
                if(flagNew)
                    alMDWithinLimitGroupID.add(mainActivity.RevisitMemberList.get(i).GroupID);
            }
            alMDWithinLimitGroup_MemerIndex.clear();
            for(int j=0; j<alMDWithinLimitGroupID.size(); j++) {
                for(int i=0; i<mainActivity.RevisitMemberList.size(); i++) {
                    if(mainActivity.RevisitMemberList.get(i).GroupID != alMDWithinLimitGroupID.get(j))
                        continue;
                    String strListType = mainActivity.RevisitMemberList.get(i).ListType;
                    if(     strListType.equalsIgnoreCase("Blue") ||
                            strListType.equalsIgnoreCase("NewMember") ||
                            strListType.equalsIgnoreCase("DeActive") ||
                            strListType.equalsIgnoreCase("Other")   )
                        continue;
                    alMDWithinLimitGroup_MemerIndex.add(i);
                }
            }
        } else {
            alMDWithinLimitGroupID.clear();
            Calendar cal = Calendar.getInstance();
            long lToday = cal.getTimeInMillis();
            long lThreeMonths = (1000L * 60L * 60L * 24L * 90L);
            long lMDWithin = lToday + lThreeMonths;
            for(int i=0; i<mainActivity.RevisitMemberList.size(); i++) {
                if(mainActivity.RevisitMemberList.get(i).CenterID != mainActivity.selectedCollectionCenterID)
                    continue;
                if(     (!mainActivity.RevisitMemberList.get(i).ListType.equalsIgnoreCase("Yellow-I")) &&
                        (!mainActivity.RevisitMemberList.get(i).ListType.equalsIgnoreCase("Yellow-II")) )
                    continue;
                if(mainActivity.RevisitMemberList.get(i).LongMaturityDate > lMDWithin)
                    continue;
                boolean flagNew = true;
                for(int j=0; j<alMDWithinLimitGroupID.size(); j++) {
                    if(alMDWithinLimitGroupID.get(j) == mainActivity.RevisitMemberList.get(i).GroupID) {
                        flagNew = false;
                        break;
                    }
                }
                if(flagNew)
                    alMDWithinLimitGroupID.add(mainActivity.RevisitMemberList.get(i).GroupID);
            }
            alMDWithinLimitGroup_MemerIndex.clear();
            for(int j=0; j<alMDWithinLimitGroupID.size(); j++) {
                for(int i=0; i<mainActivity.RevisitMemberList.size(); i++) {
                    if(mainActivity.RevisitMemberList.get(i).GroupID != alMDWithinLimitGroupID.get(j))
                        continue;
                    String strListType = mainActivity.RevisitMemberList.get(i).ListType;
                    if(     strListType.equalsIgnoreCase("Blue") ||
                            strListType.equalsIgnoreCase("NewMember") ||
                            strListType.equalsIgnoreCase("DeActive") ||
                            strListType.equalsIgnoreCase("Other")   )
                        continue;
                    alMDWithinLimitGroup_MemerIndex.add(i);
                }
            }
        }
        return rootView;
    }
    void ClearVisitList() {
        TableLayout tblRevisitMembers = (TableLayout) rootView.findViewById(R.id.tblLayoutRevisitMembers);
        tblRevisitMembers.removeAllViews();

        String strCenterCount = "मेंबर:0";
        TextView tv = (TextView) rootView.findViewById(R.id.textViewMemberCount);
        tv.setText(strCenterCount);
    }
    void RefreshVisitList() {
        ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarRevisit);
        if (pbSync != null) {
            pbSync.setIndeterminate(true);
            pbSync.setVisibility(View.VISIBLE);
        }
        TableLayout tblRevisitMembers = (TableLayout) rootView.findViewById(R.id.tblLayoutRevisitMembers);
        tblRevisitMembers.removeAllViews();
        TextView tvref = (TextView) rootView.findViewById(R.id.textViewRevisitDate);
        int iCount = 0;
        String strSeqListType = "";
        for(int seq=1; seq<alRevisitListType.size(); seq++) {
            strSeqListType = alRevisitListType.get(seq);
            for(int i=0; i<mainActivity.RevisitMemberList.size(); i++) {
                if( (mainActivity.selectedCollectionCenterID != 0) && (mainActivity.selectedCollectionCenterID != mainActivity.RevisitMemberList.get(i).CenterID) )
                    continue;
                String strListType = mainActivity.RevisitMemberList.get(i).ListType;
                if (    (mainActivity.selectedCollectionCenterID != 0) &&
                        (strListType.equalsIgnoreCase("Cream") ||
//bmp 26-Mar-26
//                         strListType.equalsIgnoreCase("White") ||
                        strListType.equalsIgnoreCase("White-I") ||
                        strListType.equalsIgnoreCase("White-II") ||
//bmp 26-Mar-26
                        strListType.equalsIgnoreCase("Blue") ||
                        strListType.equalsIgnoreCase("NewMember") ||
                        strListType.equalsIgnoreCase("DeActive") ||
                        strListType.equalsIgnoreCase("Other")
                        )
                )
                    continue;
                if(mainActivity.RevisitMemberList.get(i).VisitDate.length() < 10)
                    continue;
                String strVisitDate = mainActivity.RevisitMemberList.get(i).VisitDate.substring(0, 10);
                if ((!strSelectDate.isEmpty()) && (!strVisitDate.equalsIgnoreCase(strSelectDate)))
                    continue;
                if (    (!strSelectDate.isEmpty()) &&
                        (strListType.equalsIgnoreCase("Blue") ||
                         strListType.equalsIgnoreCase("NewMember") ||
                         strListType.equalsIgnoreCase("DeActive") ||
                         strListType.equalsIgnoreCase("Other")
                        )
                )
                    continue;
                if ((!strSelectListType.isEmpty()) && (!strListType.equalsIgnoreCase(strSelectListType)))
                    continue;
                if ((!strSeqListType.isEmpty()) && (!strListType.equalsIgnoreCase(strSeqListType)))
                    continue;
                if( (lSelectCommunityID != 0) && (lSelectCommunityID != mainActivity.RevisitMemberList.get(i).CommunityID) )
                    continue;
                if ((mainActivity.RevisitMemberList.get(i).RevisitMemberStatus != clsRevisitMember.REVISIT_MEMBER_STATUS_SCHEDULED) &&
                        (mainActivity.RevisitMemberList.get(i).RevisitMemberStatus != clsRevisitMember.REVISIT_MEMBER_STATUS_REVISITED))
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
                LinearLayout llh5 = new LinearLayout(mainActivity.getBaseContext());
                llh5.setOrientation(LinearLayout.HORIZONTAL);
                llh5.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                LinearLayout llh6 = new LinearLayout(mainActivity.getBaseContext());
                llh6.setOrientation(LinearLayout.HORIZONTAL);
                llh6.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                TextView tvCenterGroup = new TextView(mainActivity.getBaseContext());
                tvCenterGroup.setTextSize(24);
                tvCenterGroup.setTextColor(tvref.getTextColors());
                tvCenterGroup.setTypeface(Typeface.DEFAULT_BOLD);
                tvCenterGroup.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tvCenterGroup.setGravity(Gravity.CENTER_VERTICAL);

                TextView tvGroupType = new TextView(mainActivity.getBaseContext());
                tvGroupType.setTextSize(24);
                tvGroupType.setTextColor(tvref.getTextColors());
                tvGroupType.setTypeface(Typeface.DEFAULT_BOLD);
                tvGroupType.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tvGroupType.setGravity(Gravity.CENTER_VERTICAL);
                tvGroupType.setText("NoLoan");
                tvGroupType.setBackgroundColor(Color.CYAN);
                tvGroupType.setVisibility(View.INVISIBLE);

                TextView tvListType = new TextView(mainActivity.getBaseContext());
                tvListType.setTextSize(24);
                tvListType.setTextColor(tvref.getTextColors());
                tvListType.setTypeface(Typeface.DEFAULT_BOLD);
                tvListType.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tvListType.setGravity(Gravity.CENTER_VERTICAL);
                tvListType.setPadding(5, 0, 0, 0);

                TextView tvContactNumber = new TextView(mainActivity.getBaseContext());
                tvContactNumber.setTextSize(24);
                tvContactNumber.setTextColor(tvref.getTextColors());
                tvContactNumber.setTypeface(Typeface.DEFAULT_BOLD);
                tvContactNumber.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tvContactNumber.setGravity(Gravity.CENTER_VERTICAL);
                tvContactNumber.setText("M:");
                EditText etContactNumber = new EditText(mainActivity.getBaseContext());
                etContactNumber.setTextSize(24);
                etContactNumber.setTextColor(tvref.getTextColors());
                etContactNumber.setTypeface(Typeface.DEFAULT_BOLD);
                etContactNumber.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                etContactNumber.setGravity(Gravity.CENTER_VERTICAL);
                etContactNumber.setSingleLine();
                etContactNumber.setMaxLines(1);
                etContactNumber.setImeOptions(EditorInfo.IME_ACTION_DONE);
                etContactNumber.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                etContactNumber.setPadding(0, 0, 5, 0);
                etContactNumber.setId(i+1000);
                etContactNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            String strTemp = v.getText().toString();
                            if(strTemp == null)
                                strTemp = "0";
                            else if(strTemp.isEmpty())
                                strTemp = "0";
                            else if(strTemp.length() != 10)
                                strTemp = "0";
                            if(!strTemp.equalsIgnoreCase("0")) {
                                int iIndex = v.getId();
                                iIndex = iIndex - 1000;
                                mainActivity.RevisitMemberList.get(iIndex).ContactNumber = strTemp;
                                ((EditText) v).setBackgroundColor(Color.CYAN);
                            } else {
                                int iIndex = v.getId();
                                iIndex = iIndex - 1000;
                                v.setText(mainActivity.RevisitMemberList.get(iIndex).ContactNumber);
                            }
                            InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        }
                        return false;
                    }
                });
                etContactNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if (hasFocus) {
                        } else {
                            String strTemp = ((EditText) view).getText().toString();
                            if(strTemp == null)
                                strTemp = "0";
                            else if(strTemp.isEmpty())
                                strTemp = "0";
                            else if(strTemp.length() != 10)
                                strTemp = "0";
                            if(!strTemp.equalsIgnoreCase("0")) {
                                int iIndex = view.getId();
                                iIndex = iIndex - 1000;
                                mainActivity.RevisitMemberList.get(iIndex).ContactNumber = strTemp;
                                ((EditText) view).setBackgroundColor(Color.CYAN);
                            } else {
                                int iIndex = view.getId();
                                iIndex = iIndex - 1000;
                                ((EditText) view).setText(mainActivity.RevisitMemberList.get(iIndex).ContactNumber);
                            }
                            InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                });

                TextView tvSavingAccNo = new TextView(mainActivity.getBaseContext());
                tvSavingAccNo.setTextSize(24);
                tvSavingAccNo.setTextColor(tvref.getTextColors());
                tvSavingAccNo.setTypeface(Typeface.DEFAULT_BOLD);
                tvSavingAccNo.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tvSavingAccNo.setGravity(Gravity.CENTER_VERTICAL);

                TextView tvMemberName = new TextView(mainActivity.getBaseContext());
                tvMemberName.setTextSize(26);
                tvMemberName.setTextColor(tvref.getTextColors());
                tvMemberName.setTypeface(Typeface.DEFAULT_BOLD);
                tvMemberName.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1f));
                tvMemberName.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                ImageButton imgbtPhotoRevisitProof = new ImageButton(mainActivity.getBaseContext());
                imgbtPhotoRevisitProof.setImageResource(R.drawable.ic_menu_camera);
                imgbtPhotoRevisitProof.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1f));
                imgbtPhotoRevisitProof.setId(i);
                imgbtPhotoRevisitProof.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IDRequest = view.getId();
                        strFilepath = mainActivity.baseDir + "/data/";
                        Calendar cal = Calendar.getInstance();
                        Date date = cal.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH);
                        String strTimeStamp = sdf.format(date);// all done
                        strFilename = String.valueOf(mainActivity.RevisitMemberList.get(IDRequest).MemberID) + "-" +
                                strTimeStamp + "-" + String.valueOf(mainActivity.OrgID) + ".jpg";
                        File outFile = new File(strFilepath + strFilename);
                        if (!mainActivity.RevisitMemberList.get(IDRequest).strRevisitProof.isEmpty()) {
                            File oldFile = new File(strFilepath + mainActivity.RevisitMemberList.get(IDRequest).strRevisitProof);
                            if(oldFile.exists()) {
                                oldFile.delete();
                            }
                        }
                            /*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", outFile);
//                        cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        cameraIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
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
                });
                ImageButton imgbtPreviewRevisitProof = new ImageButton(mainActivity.getBaseContext());
                imgbtPreviewRevisitProof.setImageResource(R.drawable.ic_menu_gallery);
                imgbtPreviewRevisitProof.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1f));
                imgbtPreviewRevisitProof.setId(i);
                imgbtPreviewRevisitProof.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IDRequest = view.getId();
                        strFilepath = mainActivity.baseDir + "/data/";
                        Calendar cal = Calendar.getInstance();
                        Date date = cal.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH);
                        String strTimeStamp = sdf.format(date);// all done
                        strFilename = String.valueOf(mainActivity.RevisitMemberList.get(IDRequest).MemberID) + "-" +
                                strTimeStamp + "-" + String.valueOf(mainActivity.OrgID) + ".jpg";
                        File outFile = new File(strFilepath + strFilename);
                        if(outFile.exists()) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            Uri outuri = FileProvider.getUriForFile(mainActivity.getApplicationContext(), mainActivity.getApplicationContext().getPackageName() + ".provider", outFile);
                            intent.setDataAndType(outuri, "image/*");
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//bmp 06-Mar-26
//                            intent.setPackage("com.google.android.apps.photos");
//bmp 06-Mar-26
                            startActivity(intent);
                        }
                    }
                });
                LinearLayout llhProof = new LinearLayout(mainActivity.getBaseContext());
                llhProof.setOrientation(LinearLayout.HORIZONTAL);
                llhProof.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        3f));
                llhProof.addView(imgbtPhotoRevisitProof);
                llhProof.addView(imgbtPreviewRevisitProof);
//bmp 07-Jul-25
                TextView tvLastLoanAmount = new TextView(mainActivity.getBaseContext());
                tvLastLoanAmount.setTextSize(26);
                tvLastLoanAmount.setTextColor(tvref.getTextColors());
                tvLastLoanAmount.setTypeface(Typeface.DEFAULT_BOLD);
                tvLastLoanAmount.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tvLastLoanAmount.setGravity(Gravity.CENTER_VERTICAL);
                tvLastLoanAmount.setPadding(0, 0, 10, 0);

                TextView tvNewLoanAmount = new TextView(mainActivity.getBaseContext());
                tvNewLoanAmount.setTextSize(26);
                tvNewLoanAmount.setTextColor(tvref.getTextColors());
                tvNewLoanAmount.setTypeface(Typeface.DEFAULT_BOLD);
                tvNewLoanAmount.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tvNewLoanAmount.setGravity(Gravity.CENTER_VERTICAL);
                tvNewLoanAmount.setPadding(0, 0, 10, 0);

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
                tvHomeLoanAmount.setPadding(0, 0, 10, 0);

                TextView tvLastLoanDetails = new TextView(mainActivity.getBaseContext());
                tvLastLoanDetails.setTextSize(24);
                tvLastLoanDetails.setTextColor(tvref.getTextColors());
                tvLastLoanDetails.setTypeface(Typeface.DEFAULT_BOLD);
                tvLastLoanDetails.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tvLastLoanDetails.setGravity(Gravity.CENTER_VERTICAL);

                TextView tvTargetComMeetingDate = new TextView(mainActivity.getBaseContext());
                tvTargetComMeetingDate.setTextSize(24);
                tvTargetComMeetingDate.setTextColor(tvref.getTextColors());
                tvTargetComMeetingDate.setTypeface(Typeface.DEFAULT_BOLD);
                tvTargetComMeetingDate.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tvTargetComMeetingDate.setGravity(Gravity.CENTER_VERTICAL);
                tvTargetComMeetingDate.setVisibility(View.VISIBLE);

                TextView tvLastVisitDetails = new TextView(mainActivity.getBaseContext());
                tvLastVisitDetails.setTextSize(20);
                tvLastVisitDetails.setMarqueeRepeatLimit(-1);
                tvLastVisitDetails.setSingleLine();
                tvLastVisitDetails.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                tvLastVisitDetails.setSelected(true);

                tvLastVisitDetails.setFocusableInTouchMode(true);
                tvLastVisitDetails.setFreezesText(true);
                tvLastVisitDetails.setFocusable(true);
                tvLastVisitDetails.setHorizontallyScrolling(true);
                tvLastVisitDetails.setTextColor(tvref.getTextColors());
                tvLastVisitDetails.setTypeface(Typeface.DEFAULT_BOLD);
                tvLastVisitDetails.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1f));
                tvLastVisitDetails.setGravity(Gravity.CENTER_VERTICAL);

                TextView tvVisitDetails = new TextView(mainActivity.getBaseContext());
//bmp 07-Jun-25
//                tvVisitDetails.setTextSize(26);
                tvVisitDetails.setTextSize(24);
//bmp 07-Jun-25
                tvVisitDetails.setTextColor(tvref.getTextColors());
                tvVisitDetails.setTypeface(Typeface.DEFAULT_BOLD);
                tvVisitDetails.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tvVisitDetails.setGravity(Gravity.CENTER_VERTICAL);

                TextView tvDummy = new TextView(mainActivity.getBaseContext());
                tvDummy.setTextSize(26);
                tvDummy.setTextColor(tvref.getTextColors());
                tvDummy.setTypeface(Typeface.DEFAULT_BOLD);
                tvDummy.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tvDummy.setGravity(Gravity.CENTER_VERTICAL);
                tvDummy.setText(" ");

                ArrayAdapter<String> adapterVisitDetails = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                        R.layout.simple_spinner_item, mainActivity.VisitDetailsInMarathiMaster);
//bmp 03-Sep-24
                if(mainActivity.RevisitMemberList.get(i).HomeLoanAmount > 0) {
                    adapterVisitDetails = new ArrayAdapter<String>(mainActivity.getBaseContext(),
                            R.layout.simple_spinner_item, mainActivity.HomeLoanOfferVisitDetailsInMarathiMaster);
                }
//bmp 03-Sep-24
                adapterVisitDetails.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                Spinner spVisitDetails = new Spinner(mainActivity.getBaseContext());
                spVisitDetails.setAdapter(adapterVisitDetails);
                spVisitDetails.setSelection(0);
                if (mainActivity.RevisitMemberList.get(i).RevisitMemberStatus == clsRevisitMember.REVISIT_MEMBER_STATUS_REVISITED) {
                    if(mainActivity.RevisitMemberList.get(i).HomeLoanAmount > 0) {
                        for (int k = 0; k < mainActivity.HomeLoanOfferVisitDetailsIDMaster.size(); k++) {
                            if (mainActivity.RevisitMemberList.get(i).VisitDetailsID == mainActivity.HomeLoanOfferVisitDetailsIDMaster.get(k)) {
                                spVisitDetails.setSelection(k);
                                break;
                            }
                        }
                    } else {
                        for (int k = 0; k < mainActivity.VisitDetailsIDMaster.size(); k++) {
                            if (mainActivity.RevisitMemberList.get(i).VisitDetailsID == mainActivity.VisitDetailsIDMaster.get(k)) {
                                spVisitDetails.setSelection(k);
                                break;
                            }
                        }
                    }
                }
                spVisitDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0)
                            return;
                        int iIndex = parent.getId();
                        iIndex = iIndex - 4000;
                        if(mainActivity.RevisitMemberList.get(iIndex).HomeLoanAmount > 0) {
                            if (mainActivity.RevisitMemberList.get(iIndex).VisitDetailsID == mainActivity.HomeLoanOfferVisitDetailsIDMaster.get(position))
                                return;
                        } else {
                            if (mainActivity.RevisitMemberList.get(iIndex).VisitDetailsID == mainActivity.VisitDetailsIDMaster.get(position))
                                return;
                        }
                        long lThreeMonths = (1000L * 60L * 60L * 24L * 90L);
                        long lSixMonths = (1000L * 60L * 60L * 24L * 180L);
                        Calendar calToday = Calendar.getInstance();
                        long ltoday = calToday.getTimeInMillis();
                        String strMaturityDate = mainActivity.RevisitMemberList.get(iIndex).MaturityDate;
//bmp 25-Apr-25
                        strMaturityDate = strMaturityDate.substring(0, 10);
//bmp 25-Apr-25
                        String[] strSplit = strMaturityDate.split("-");
                        Calendar doe = Calendar.getInstance();
                        doe.set(Calendar.YEAR, Integer.parseInt(strSplit[0]));
                        doe.set(Calendar.MONTH, (Integer.parseInt(strSplit[1]) - 1));
                        doe.set(Calendar.DATE, Integer.parseInt(strSplit[2]));
                        long ldoMaturity = doe.getTimeInMillis();
                        long lDiff = ldoMaturity - ltoday;
                        if (lDiff <= lThreeMonths) { //upto 3 months from today
                            if(mainActivity.RevisitMemberList.get(iIndex).HomeLoanAmount > 0) {
                                if (mainActivity.HomeLoanOfferVisitDetailsIDMaster.get(position) == clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING) {
//                                    Toast toast = Toast.makeText(mainActivity.getBaseContext(), mainActivity.RevisitMemberList.get(iIndex).MemberName + ": सभासदाचा चालू कर्ज कालावधी 3 महिन्यांच्या आतील आहे.", Toast.LENGTH_SHORT);
//                                    ViewGroup group = (ViewGroup) toast.getView();
//                                    TextView messageTextView = (TextView) group.getChildAt(0);
//                                    messageTextView.setTextSize(28);
//                                    toast.show();
                                    String strMsg = mainActivity.RevisitMemberList.get(iIndex).MemberName + ":\nसभासदाचा चालू कर्ज कालावधी 3 महिन्यांच्या आतील आहे.";
                                    ShowToastMessage(strMsg);
                                }
                            } else {
                                if (mainActivity.VisitDetailsIDMaster.get(position) == clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING) {
//                                    Toast toast = Toast.makeText(mainActivity.getBaseContext(), mainActivity.RevisitMemberList.get(iIndex).MemberName + ": सभासदाचा चालू कर्ज कालावधी 3 महिन्यांच्या आतील आहे.", Toast.LENGTH_SHORT);
//                                    ViewGroup group = (ViewGroup) toast.getView();
//                                    TextView messageTextView = (TextView) group.getChildAt(0);
//                                    messageTextView.setTextSize(28);
//                                    toast.show();
                                    String strMsg = mainActivity.RevisitMemberList.get(iIndex).MemberName + ":\nसभासदाचा चालू कर्ज कालावधी 3 महिन्यांच्या आतील आहे.";
                                    ShowToastMessage(strMsg);
                                }
                            }
                        } else if (lDiff > lSixMonths) { //beyond 6 months from today
                            if(mainActivity.RevisitMemberList.get(iIndex).HomeLoanAmount > 0) {
                                if (mainActivity.HomeLoanOfferVisitDetailsIDMaster.get(position) != clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING) {
//                                    Toast toast = Toast.makeText(mainActivity.getBaseContext(), mainActivity.RevisitMemberList.get(iIndex).MemberName + ": सभासदाचा चालू कर्ज कालावधी 6 महिन्या पेक्षा अधिक आहे.", Toast.LENGTH_SHORT);
//                                    ViewGroup group = (ViewGroup) toast.getView();
//                                    TextView messageTextView = (TextView) group.getChildAt(0);
//                                    messageTextView.setTextSize(28);
//                                    toast.show();
                                    String strMsg = mainActivity.RevisitMemberList.get(iIndex).MemberName + ":\nसभासदाचा चालू कर्ज कालावधी 6 महिन्या पेक्षा अधिक आहे.";
                                    ShowToastMessage(strMsg);
                                }
                            } else {
                                if (mainActivity.VisitDetailsIDMaster.get(position) != clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING) {
//                                    Toast toast = Toast.makeText(mainActivity.getBaseContext(), mainActivity.RevisitMemberList.get(iIndex).MemberName + ": सभासदाचा चालू कर्ज कालावधी 6 महिन्या पेक्षा अधिक आहे.", Toast.LENGTH_SHORT);
//                                    ViewGroup group = (ViewGroup) toast.getView();
//                                    TextView messageTextView = (TextView) group.getChildAt(0);
//                                    messageTextView.setTextSize(28);
//                                    toast.show();
                                    String strMsg = mainActivity.RevisitMemberList.get(iIndex).MemberName + ":\nसभासदाचा चालू कर्ज कालावधी 6 महिन्या पेक्षा अधिक आहे.";
                                    ShowToastMessage(strMsg);
                                }
                            }
                        }
                        //Add Dialogbox to confirm selected Revisit Remark
                        selectedRevisitMemberIndex = -1;
                        if(mainActivity.RevisitMemberList.get(iIndex).HomeLoanAmount > 0) {
                            if(mainActivity.HomeLoanOfferVisitDetailsIDMaster.get(position)
                                    == clsRevisitMember.REVISIT_REMARK_ID_MEMBER_DEATH) {
                                selectedRevisitMemberIndex = iIndex;
                            }
                        } else {
                            if(mainActivity.VisitDetailsIDMaster.get(position)
                                    == clsRevisitMember.REVISIT_REMARK_ID_MEMBER_DEATH) {
                                selectedRevisitMemberIndex = iIndex;
                            }
                        }
                        if(selectedRevisitMemberIndex >= 0) {
                            CreateAlertDialog(true, true);
                            dlgState = ufin_REVISIT_DIALOG_STATE_MEMBER_DEATH_CONFIRM;
                            dlgAlert.setMessage(MSG_ufin_REVISIT_CONFIRM_MEMBER_DEATH);
                            AlertDialog dialog = dlgAlert.create();
                            dialog.show();
                            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                            textView.setTextSize(30);
                            textView.setTextColor(Color.RED);
                        } else if(  mainActivity.RevisitMemberList.get(iIndex).ListType.equalsIgnoreCase("Cream") ||
//bmp 26-Mar-26
//                                mainActivity.RevisitMemberList.get(iIndex).ListType.equalsIgnoreCase("White") ) {
                                    mainActivity.RevisitMemberList.get(iIndex).ListType.equalsIgnoreCase("White-I") ||
                                    mainActivity.RevisitMemberList.get(iIndex).ListType.equalsIgnoreCase("White-II") ) {
//bmp 26-Mar-26
                            boolean flagLoanRequired = false;
                            int iAdditionalAmountToBeCollected = mainActivity.RevisitMemberList.get(iIndex).UpFrontJLGBalance - mainActivity.RevisitMemberList.get(iIndex).CurrentJLGBalance;
                            /// //////////////////// commented for testing
                            if(iAdditionalAmountToBeCollected > 0) {
                            /// //////////////////// commented for testing
                                selectedRevisitMemberIndex = iIndex;
                                if(mainActivity.RevisitMemberList.get(iIndex).HomeLoanAmount > 0) {
                                    selectedVisitDetailsID = mainActivity.HomeLoanOfferVisitDetailsIDMaster.get(position);
                                    if(     (selectedVisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) ||
                                            (selectedVisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED) )
                                        flagLoanRequired = true;
                                } else {
                                    selectedVisitDetailsID = mainActivity.VisitDetailsIDMaster.get(position);
                                    if(selectedVisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED)
                                        flagLoanRequired = true;
                                }
                                if(flagLoanRequired) {
                                    CreateAlertDialog(true, true);
                                    String strMsg = mainActivity.RevisitMemberList.get(iIndex).MemberName + ":\n" +
                                            "अतिरिक्त रक्कम रु. " + String.valueOf(iAdditionalAmountToBeCollected) + "/- सभासदाला मिळणाऱ्या पुढील कर्ज रक्कमेतून वळती केली जाईल.";
                                    dlgState = ufin_REVISIT_DIALOG_STATE_ADDITIONAL_JLG;
                                    dlgAlert.setMessage(strMsg);
                                    AlertDialog dialog = dlgAlert.create();
                                    dialog.show();
                                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                                    textView.setTextSize(30);
                                    textView.setTextColor(Color.RED);
                                }
                                /// //////////////////// commented for testing
                            }
                            /// //////////////////// commented for testing
                            if( (iAdditionalAmountToBeCollected == 0) || (!flagLoanRequired) ) {
                                clsRevisitMember member = mainActivity.RevisitMemberList.get(iIndex);
                                if(mainActivity.RevisitMemberList.get(iIndex).HomeLoanAmount > 0) {
                                    member.VisitDetailsID = mainActivity.HomeLoanOfferVisitDetailsIDMaster.get(position);
                                } else {
                                    member.VisitDetailsID = mainActivity.VisitDetailsIDMaster.get(position);
                                }
                                Calendar calNow = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                                String strDate = sdf.format(calNow.getTime());// all done
                                member.ActualVisitDate = strDate;
                                member.RevisitMemberStatus = clsRevisitMember.REVISIT_MEMBER_STATUS_REVISITED;
                                member.Edited = true;
                                mainActivity.RevisitMemberList.set(iIndex, member);
                            }
////////////////////////////////
                        } else {
////////////////////////////////
                            clsRevisitMember member = mainActivity.RevisitMemberList.get(iIndex);
                            if(mainActivity.RevisitMemberList.get(iIndex).HomeLoanAmount > 0) {
                                member.VisitDetailsID = mainActivity.HomeLoanOfferVisitDetailsIDMaster.get(position);
                            } else {
                                member.VisitDetailsID = mainActivity.VisitDetailsIDMaster.get(position);
                            }
                            Calendar calNow = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                            String strDate = sdf.format(calNow.getTime());// all done
                            member.ActualVisitDate = strDate;
                            member.RevisitMemberStatus = clsRevisitMember.REVISIT_MEMBER_STATUS_REVISITED;
                            member.Edited = true;
                            mainActivity.RevisitMemberList.set(iIndex, member);
////////////////////////////////
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                spVisitDetails.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1f));
                spVisitDetails.setGravity(Gravity.CENTER_VERTICAL);
                spVisitDetails.setFocusable(true);
                spVisitDetails.setFocusableInTouchMode(true);
                spVisitDetails.setId(i+4000);

                clsRevisitMember member = mainActivity.RevisitMemberList.get(i);
                tvCenterGroup.setText("C-" + String.valueOf(member.CenterID) + ":G-" + String.valueOf(member.GroupID));
                if (String.valueOf(member.GroupID).length() >= 17) {
                    tvCenterGroup.setText("C-" + String.valueOf(member.CenterID).substring(0, (String.valueOf(member.GroupID).length() - 8)) + ":G-" + String.valueOf(member.GroupID).substring(0, (String.valueOf(member.GroupID).length() - 8)));
                    tvCenterGroup.setTextSize(24);
                }
                if (member.GroupName.contains("No Loan")) {
                    tvGroupType.setVisibility(View.VISIBLE);
                } else {
                    tvGroupType.setVisibility(View.INVISIBLE);
                }
                tvListType.setText(member.ListType);
                if (member.ListType.toLowerCase().contains("yellow")) {
                    tvListType.setBackgroundColor(Color.YELLOW);
                } else if(member.ListType.toLowerCase().contains("pink")) {
                    tvListType.setBackgroundColor(Color.argb(255, 255, 192, 203));    //Pink
                } else if(member.ListType.toLowerCase().contains("pista")) {
                    tvListType.setBackgroundColor(Color.argb(255, 147, 197, 114));
                } else if(member.ListType.toLowerCase().contains("white")) {
                    tvListType.setBackgroundColor(Color.WHITE);
                } else if(member.ListType.toLowerCase().contains("cream")) {
                    tvListType.setBackgroundColor(Color.argb(255, 255, 253, 208));    //Cream
                } else {
                    tvListType.setBackgroundColor(Color.GRAY);
                }
                etContactNumber.setText(member.ContactNumber);
                tvSavingAccNo.setText("AccNo:" + member.SavingAccNo);
                tvMemberName.setText(member.MemberName);
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

                String strMD =  member.MaturityDate.substring(8, 10) + "/" +
                        member.MaturityDate.substring(5, 7) + "/" +
                        member.MaturityDate.substring(2, 4);
                tvLastLoanDetails.setText("लोन MD: " + strMD + " बाकी रु. " + String.valueOf(member.BalanceAmount) + " थकीत रु. " + String.valueOf(member.OverdueAmount));
                tvTargetComMeetingDate.setText(" मीटिंग:" + member.TargetComMeetingDate);
//bmp 24-Apr-24
//            int iTemp = mainActivity.LastVisitDetailsIDMaster.indexOf((int) member.LastVisitDetailsID);
//            if (iTemp < 0)
//                iTemp = 0;
//            tvLastVisitDetails.setText("रिमार्क: " + mainActivity.LastVisitDetailsInMarathiMaster.get(iTemp));
                if(iNextLoanOfferAmount > 0) {
                    int iTemp = mainActivity.LastVisitDetailsIDMaster.indexOf((int) member.LastVisitDetailsID);
                    if (iTemp < 0)
                        iTemp = 0;
                    tvLastVisitDetails.setText("रिमार्क: " + mainActivity.LastVisitDetailsInMarathiMaster.get(iTemp));
                } else {
                    int iTemp = mainActivity.NextLoanNotAllowedIDMaster.indexOf((int) member.LastVisitDetailsID);
                    if (iTemp < 0)
                        iTemp = 0;
                    tvLastVisitDetails.setText("रिमार्क: " + mainActivity.NextLoanNotAllowedInMarathiMaster.get(iTemp));
                }
//bmp 24-Apr-24
//bmp 19-Jun-25
//                tvVisitDetails.setText("नवीन: ");
//                tvVisitDetails.setText("नवीन रिमार्क: ");
                tvVisitDetails.setText("पुनर्भेट रिमार्क: ");
//bmp 19-Jun-25

                    //RK
                    TableRow trCreditCheck = new TableRow(mainActivity.getBaseContext());

                    trCreditCheck.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    ));
                    trCreditCheck.setGravity(Gravity.CENTER_VERTICAL);


    // CREDIT CHECK
                    TextView tvCreditCheck = new TextView(mainActivity.getBaseContext());

                    tvCreditCheck.setTextSize(24);
                    tvCreditCheck.setText("क्रेडिट चेक :बाकी");
                    tvCreditCheck.setBackgroundColor(Color.argb(255, 255, 191, 0));
                    tvCreditCheck.setTextColor(tvref.getTextColors());
                    tvCreditCheck.setTypeface(Typeface.DEFAULT_BOLD);
                    tvCreditCheck.setGravity(Gravity.CENTER_VERTICAL);

                    TableRow.LayoutParams creditCheckParams = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT);
                    creditCheckParams.gravity = Gravity.CENTER_VERTICAL;
                    tvCreditCheck.setLayoutParams(creditCheckParams);

                   /* if(member.CreditCheck == -1) {
                        tvCreditCheck.setBackgroundColor(Color.argb(255, 255, 191, 0));
                        tvCreditCheck.setText("क्रेडिट चेक: बाकी");
                    } else if(member.CreditCheck == 0) {
                        tvCreditCheck.setBackgroundColor(Color.RED);
                        tvCreditCheck.setText("क्रेडिट चेक:रिजेक्ट");
                    } else if(member.CreditCheck == 1) {
                        tvCreditCheck.setBackgroundColor(Color.GREEN);
                        tvCreditCheck.setText("क्रेडिट चेक: ओके");
                    }*/
    // Keep your existing ID pattern
                    //tvCreditCheck.setId(3 + iIDindex);

    // CB DETAIL BUTTON
                    Button btCBDetail = new Button(mainActivity.getBaseContext());

                    btCBDetail.setText("CB DETAIL");
                    btCBDetail.setTextSize(16);
                    btCBDetail.setTypeface(Typeface.DEFAULT_BOLD);
                    btCBDetail.setGravity(Gravity.CENTER);

                    TableRow.LayoutParams cbDetailParams = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    );
                    cbDetailParams.gravity = Gravity.CENTER_VERTICAL;

                    btCBDetail.setLayoutParams(cbDetailParams);

    // FAMILY MEMBERS BUTTON
                    Button btFamilyMembers = new Button(mainActivity.getBaseContext());

                    btFamilyMembers.setText("Loan Eligibility");
                    btFamilyMembers.setTextSize(16);
                    btFamilyMembers.setTypeface(Typeface.DEFAULT_BOLD);
                    btFamilyMembers.setGravity(Gravity.CENTER);
                    TableRow.LayoutParams familyMembersParams = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                    familyMembersParams.gravity = Gravity.CENTER_VERTICAL;
                    btFamilyMembers.setLayoutParams(familyMembersParams);

    // CLICK
                    // btFamilyMembers.setOnClickListener
                    final long revisitMemberId = mainActivity.RevisitMemberList.get(i).MemberID;

                    btFamilyMembers.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            FamilyIncomeFragment familyIncomeFragment = new FamilyIncomeFragment();

                            Bundle args = new Bundle();
                            args.putLong(FamilyIncomeFragment.ARG_MEMBER_ID, revisitMemberId);
                            familyIncomeFragment.setArguments(args);

                            mainActivity.getSupportFragmentManager()
                                    .beginTransaction()
                                    .hide(RevisitFragment.this)                          // Revisit नष्ट होत नाही, फक्त लपते
                                    .add(R.id.fragment_container, familyIncomeFragment)  // replace ऐवजी add
                                    .addToBackStack(null)
                                    .commit();
                        }
                    });
                    Button btCBCheck = new Button(mainActivity.getBaseContext());
                    btCBCheck.setText("CB Check");
                    btCBCheck.setTextSize(16);
                    btCBCheck.setTypeface(Typeface.DEFAULT_BOLD);
                    btCBCheck.setGravity(Gravity.CENTER);

                    TableRow.LayoutParams cbCheckParams = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT);
                    cbCheckParams.gravity = Gravity.CENTER_VERTICAL;
                    btCBCheck.setLayoutParams(cbCheckParams);
    // New ID
                    //   btCBDetail.setId(5 + iIDindex);


    // PREVIEW BUTTON
                    Button btPreviewCBCheck = new Button(mainActivity.getBaseContext());

                    btPreviewCBCheck.setText("PREVIEW");
                    btPreviewCBCheck.setTextSize(16);
                    btPreviewCBCheck.setTypeface(Typeface.DEFAULT_BOLD);
                    btPreviewCBCheck.setGravity(Gravity.CENTER);

                    TableRow.LayoutParams previewParams = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    );
                    previewParams.gravity = Gravity.CENTER_VERTICAL;
                    btPreviewCBCheck.setLayoutParams(previewParams);
                    // btPreviewCBCheck.setId(6 + iIDindex);


    // PRINT BUTTON
                    Button btPrintCBCheck = new Button(mainActivity.getBaseContext());
                    btPrintCBCheck.setText("PRINT");
                    btPrintCBCheck.setTextSize(16);
                    btPrintCBCheck.setTypeface(Typeface.DEFAULT_BOLD);
                    btPrintCBCheck.setGravity(Gravity.CENTER);

                    TableRow.LayoutParams printParams = new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    );
                    printParams.gravity = Gravity.CENTER_VERTICAL;
                    btPrintCBCheck.setLayoutParams(printParams);

                    //  btPrintCBCheck.setId(7 + iIDindex);

                    trCreditCheck.addView(tvCreditCheck);
                    trCreditCheck.addView(btCBCheck);
                    trCreditCheck.addView(btCBDetail);
                    trCreditCheck.addView(btPreviewCBCheck);
                    trCreditCheck.addView(btPrintCBCheck);
                    trCreditCheck.addView(btFamilyMembers);

                    //----


                llh1.addView(tvCenterGroup);
                llh1.addView(tvGroupType);
                llh1.addView(tvContactNumber);
//bmp 17-Jun-25
                llh1.addView(etContactNumber);
//bmp 17-Jun-25
                llh1.addView(tvSavingAccNo);
                llh1.addView(tvListType);
//bmp 07-Jul-25
//                llh2.addView(tvMemberName);
                llh2.addView(llhProof);
                llh2.addView(tvMemberName);
//bmp 07-Jul-25
                llh3.addView(tvLastLoanAmount);
                llh3.addView(tvNewLoanAmount);
                llh3.addView(tvLoanCycle);
                llh3.addView(tvHomeLoanAmount);
                llh4.addView(tvLastLoanDetails);
                llh4.addView(tvTargetComMeetingDate);
                llh5.addView(tvLastVisitDetails);
                if(member.PrevMaxLoanAmount > 0) {
                    llh6.addView(tvVisitDetails);
                    llh6.addView(spVisitDetails);
                    llh6.addView(tvDummy);
                }

                llv.addView(llh1);
                llv.addView(llh2);
                llv.addView(llh3);
                llv.addView(llh4);
                llv.addView(llh5);
                llv.addView(llh6);
                    //RK
                    llv.addView(trCreditCheck);
//bmp 29-Sep-25
                //Additional Amount to be collected from Member to be Eligible for Next Loan
//bmp 26-Mar-26
//                if(member.ListType.equalsIgnoreCase("Cream") || member.ListType.equalsIgnoreCase("White")) {
                if(     member.ListType.equalsIgnoreCase("Cream") ||
                        member.ListType.equalsIgnoreCase("White-I") ||
                        member.ListType.equalsIgnoreCase("White-II") ) {
//bmp 26-Mar-26
                    int iAdditionalAmountToBeCollected = member.UpFrontJLGBalance - member.CurrentJLGBalance;
                    if (iAdditionalAmountToBeCollected < 0)
                        iAdditionalAmountToBeCollected = 0;

                    LinearLayout llh7 = new LinearLayout(mainActivity.getBaseContext());
                    llh7.setOrientation(LinearLayout.HORIZONTAL);
                    llh7.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    TextView tvRequiredJLGAmount = new TextView(mainActivity.getBaseContext());
                    tvRequiredJLGAmount.setTextSize(26);
                    tvRequiredJLGAmount.setTextColor(Color.RED);
                    tvRequiredJLGAmount.setTypeface(Typeface.DEFAULT_BOLD);
                    tvRequiredJLGAmount.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tvRequiredJLGAmount.setGravity(Gravity.CENTER_VERTICAL);
                    tvRequiredJLGAmount.setPadding(0, 0, 10, 0);
                    tvRequiredJLGAmount.setText("पुढील कर्जासाठी आवश्यक जेएलजी रु. " + String.valueOf(member.UpFrontJLGBalance) + "/-");
                    llh7.addView(tvRequiredJLGAmount);

                    LinearLayout llh8 = new LinearLayout(mainActivity.getBaseContext());
                    llh8.setOrientation(LinearLayout.HORIZONTAL);
                    llh8.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    TextView tviMemberJLGAmount = new TextView(mainActivity.getBaseContext());
                    tviMemberJLGAmount.setTextSize(26);
                    tviMemberJLGAmount.setTextColor(Color.RED);
                    tviMemberJLGAmount.setTypeface(Typeface.DEFAULT_BOLD);
                    tviMemberJLGAmount.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tviMemberJLGAmount.setGravity(Gravity.CENTER_VERTICAL);
                    tviMemberJLGAmount.setPadding(0, 0, 10, 0);
                    tviMemberJLGAmount.setText("सभासदाची अन्नपूर्णाकडे जमा असलेली जेएलजी रु. " + String.valueOf(member.CurrentJLGBalance) + "/-");
                    llh8.addView(tviMemberJLGAmount);

                    LinearLayout llh9 = new LinearLayout(mainActivity.getBaseContext());
                    llh9.setOrientation(LinearLayout.HORIZONTAL);
                    llh9.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    TextView tvAdditionalAmountToBeCollected = new TextView(mainActivity.getBaseContext());
                    tvAdditionalAmountToBeCollected.setTextSize(26);
                    tvAdditionalAmountToBeCollected.setTextColor(Color.RED);
                    tvAdditionalAmountToBeCollected.setTypeface(Typeface.DEFAULT_BOLD);
                    tvAdditionalAmountToBeCollected.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tvAdditionalAmountToBeCollected.setGravity(Gravity.CENTER_VERTICAL);
                    tvAdditionalAmountToBeCollected.setPadding(0, 0, 10, 0);
                    tvAdditionalAmountToBeCollected.setText("पुढील कर्जाआधी भरायला लागणारी अतिरिक्त जेएलजी रु. " + String.valueOf(iAdditionalAmountToBeCollected) + "/-");
                    llh9.addView(tvAdditionalAmountToBeCollected);

                    llv.addView(llh7);
                    llv.addView(llh8);
                    llv.addView(llh9);
                }

                TableRow tblRow = new TableRow(mainActivity.getBaseContext());
                tblRow.setBackground(mainActivity.getDrawable(R.drawable.customborder));
                tblRow.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tblRow.addView(llv);
                if(mainActivity.RevisitMemberList.get(i).RevisitMemberStatus == clsRevisitMember.REVISIT_MEMBER_STATUS_REVISITED) {
                    tblRow.setBackgroundColor(Color.LTGRAY);
//bmp 29-Jul-25
//                    etContactNumber.setEnabled(false);
//                    imgbtPhotoRevisitProof.setEnabled(false);
//                    spVisitDetails.setEnabled(false);
//bmp 29-Jul-25
                }
                tblRevisitMembers.addView(tblRow);
                iCount++;
            }
        }

        TextView tv = (TextView) rootView.findViewById(R.id.textViewMemberCount);
//bmp 07-Jun-25
//        tv.setText(String.valueOf(iCount));
//bmp 18-Jun-25
//        String strCenterCount = "";
        String strCenterCount = "मेंबर";
//bmp 18-Jun-25
        if(mainActivity.selectedCollectionCenterID > 0) {
            strCenterCount = "C-" + String.valueOf(mainActivity.selectedCollectionCenterID);
        }
        strCenterCount = strCenterCount + ":" + String.valueOf(iCount);
        tv.setText(strCenterCount);
//bmp 07-Jun-25
        pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarRevisit);
        if (pbSync != null) {
            pbSync.setIndeterminate(true);
            pbSync.setVisibility(View.INVISIBLE);
        }
    }
////bmp 07-Jul-25
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
                                mainActivity.RevisitMemberList.get(IDRequest).strRevisitProof = strFilename;
//bmp 28-Apr-26
                            } else {
//bmp 29-Jul-25
//                                File oldFile = new File(strFilepath + mainActivity.RevisitMemberList.get(IDRequest).strRevisitProof);
//                                if(oldFile.exists()) {
//                                    oldFile.delete();
//                                }
//bmp 29-Jul-25
                                mainActivity.RevisitMemberList.get(IDRequest).strRevisitProof = strFilename;
                            }
                        }
                    } else if(result.getResultCode() == Activity.RESULT_CANCELED){
                        //handle cancel
                        ShowdlgAlertonActivityResult("CAMERA REQUEST CANCELED!");
                    }
                }
                });*/

    //Rk
        ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {

                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == Activity.RESULT_OK) {

                            // Get image URI returned by Camera App
                            Intent data = result.getData();

                            if (data == null) {
                                ShowdlgAlertonActivityResult(
                                        "Image data not received!"
                                );
                                return;
                            }

                            Uri imageUri = data.getData();

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
                                        "Image URI not received!"
                                );
                                return;
                            }

                            // -------------------------------------------------
                            // Copy image returned from Camera App
                            // to existing Finance App path
                            // -------------------------------------------------

                            File outFile = new File(
                                    strFilepath + strFilename
                            );

                            try {

                                InputStream inputStream =
                                        mainActivity
                                                .getContentResolver()
                                                .openInputStream(imageUri);

                                if (inputStream == null) {
                                    throw new Exception(
                                            "Unable to open image URI"
                                    );
                                }

                                FileOutputStream outputStream =
                                        new FileOutputStream(outFile);

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
                                        "Unable to save scanned image!"
                                );

                                return;
                            }

                            // -------------------------------------------------
                            // Your OLD logic - kept same
                            // -------------------------------------------------

                            if (outFile.exists()) {

                                if (outFile.length() >= MAX_PHOTO_FILESIZE) {

                                    String currentPhotoPath =
                                            strFilepath + strFilename;

                                    mainActivity.AdjustImageFileSize(
                                            currentPhotoPath
                                    );

                                    mainActivity.RevisitMemberList
                                            .get(IDRequest)
                                            .strRevisitProof = strFilename;

                                } else {

                                    mainActivity.RevisitMemberList
                                            .get(IDRequest)
                                            .strRevisitProof = strFilename;
                                }
                            }

                        } else if (result.getResultCode() == Activity.RESULT_CANCELED) {

                            // Camera App / Scanner cancelled
                            ShowdlgAlertonActivityResult(
                                    "CAMERA REQUEST CANCELED!"
                            );
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
//bmp 07-Jul-25
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
                        case ufin_REVISIT_DIALOG_STATE_REVISIT_PENDING:
                            break;
                        case ufin_REVISIT_DIALOG_STATE_MEMBER_DEATH_CONFIRM:
//bmp 25-Aug-25
                            if(selectedRevisitMemberIndex >= 0) {
                                int iID = selectedRevisitMemberIndex + 4000;
                                View view = rootView.findViewById(iID);
                                ((Spinner) view).setSelection(0);
                            }
//bmp 25-Aug-25
                            break;
                        case ufin_REVISIT_DIALOG_STATE_ADDITIONAL_JLG:
                            if(selectedRevisitMemberIndex >= 0) {
                                int iID = selectedRevisitMemberIndex + 4000;
                                View view = rootView.findViewById(iID);
                                ((Spinner) view).setSelection(0);
                            }
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
                        case ufin_REVISIT_DIALOG_STATE_REVISIT_PENDING:
                            mainActivity.selectedCollectionCenterID = 0L;
                            mainActivity.InitDesktop();
                            break;
                        case ufin_REVISIT_DIALOG_STATE_MEMBER_DEATH_CONFIRM:
////////////////////////////////
                            if(selectedRevisitMemberIndex >= 0) {
                                clsRevisitMember member = mainActivity.RevisitMemberList.get(selectedRevisitMemberIndex);
                                member.VisitDetailsID = clsRevisitMember.REVISIT_REMARK_ID_MEMBER_DEATH;
                                Calendar calNow = Calendar.getInstance();
//bmp 17-Sep-25
//                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//bmp 17-Sep-25
                                String strDate = sdf.format(calNow.getTime());// all done
                                member.ActualVisitDate = strDate;
                                member.RevisitMemberStatus = clsRevisitMember.REVISIT_MEMBER_STATUS_REVISITED;
                                member.Edited = true;
                                mainActivity.RevisitMemberList.set(selectedRevisitMemberIndex, member);
                                selectedRevisitMemberIndex = -1;
                            }
////////////////////////////////
                            break;
                        case ufin_REVISIT_DIALOG_STATE_CLOSE:
                            mainActivity.InitDesktop();
                            break;
                        case ufin_REVISIT_DIALOG_STATE_ADDITIONAL_JLG:
                            clsRevisitMember member = mainActivity.RevisitMemberList.get(selectedRevisitMemberIndex);
                            member.VisitDetailsID = selectedVisitDetailsID;
                            Calendar calNow = Calendar.getInstance();
//bmp 17-Sep-25
//                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//bmp 17-Sep-25
                            String strDate = sdf.format(calNow.getTime());// all done
                            member.ActualVisitDate = strDate;
                            member.RevisitMemberStatus = clsRevisitMember.REVISIT_MEMBER_STATUS_REVISITED;
                            member.Edited = true;
                            mainActivity.RevisitMemberList.set(selectedRevisitMemberIndex, member);
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
            case R.id.buttonCloseRevisit:
//bmp 19-Jun-25
                ProgressBar pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarRevisit);
                if (pbSync != null) {
                    pbSync.setIndeterminate(true);
                    pbSync.setVisibility(View.VISIBLE);
                }
                if(SQLite_saveRevisitDetails()) {
//                    Toast toast = Toast.makeText(mainActivity.getBaseContext(), "पुनर्भेट रिमार्क Saved.", Toast.LENGTH_SHORT);
//                    ViewGroup group = (ViewGroup) toast.getView();
//                    TextView messageTextView = (TextView) group.getChildAt(0);
//                    messageTextView.setTextSize(28);
//                    toast.show();
                    String strMsg = "पुनर्भेट रिमार्क Saved.";
                    ShowToastMessage(strMsg);
                }
                String strList = "";
                for(int j=0; j<alMDWithinLimitGroup_MemerIndex.size(); j++) {
                    int iIndex = alMDWithinLimitGroup_MemerIndex.get(j);
//                    if(     (mainActivity.RevisitMemberList.get(iIndex).RevisitMemberStatus != clsRevisitMember.REVISIT_MEMBER_STATUS_REVISITED) ||
//                            (!mainActivity.RevisitMemberList.get(iIndex).Edited) ) {
                    if(mainActivity.RevisitMemberList.get(iIndex).RevisitMemberStatus
                            != clsRevisitMember.REVISIT_MEMBER_STATUS_REVISITED) {
                        strList = strList + "\n" + mainActivity.RevisitMemberList.get(iIndex).MemberName;
                    }
                }
                pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarRevisit);
                if (pbSync != null) {
                    pbSync.setIndeterminate(true);
                    pbSync.setVisibility(View.INVISIBLE);
                }
                if(!strList.isEmpty()) {
//bmp 09-Jul-25
//                    CreateAlertDialog(true, true);
//                    String strMsg = MSG_ufin_REVISIT_PENDING + strList;
//                    strMsg = strMsg + "\n\n" + "Press Cancel to Enter Remarks.\nPress OK to Close.";
                    String strMsg = MSG_ufin_REVISIT_PENDING + strList;
                    if(mainActivity.selectedCollectionCenterID == 0) {
                        CreateAlertDialog(true, true);
                        strMsg = strMsg + "\n\n" + "Press Cancel to Enter Remarks.\nPress OK to Close.";
                    } else {
                        CreateAlertDialog(false, true);
                        strMsg = strMsg + "\n\n" + "Press Cancel to Enter Remarks.";
                    }
//bmp 09-Jul-25
                    dlgState = ufin_REVISIT_DIALOG_STATE_REVISIT_PENDING;
                    dlgAlert.setMessage(strMsg);
                    AlertDialog dialog = dlgAlert.create();
                    dialog.show();
                    TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                    textView.setTextSize(30);
                    textView.setTextColor(Color.RED);
                    break;
                }
//bmp 19-Jun-25
                mainActivity.selectedCollectionCenterID = 0L;
                mainActivity.InitDesktop();
                break;
            case R.id.buttonSaveRevisit:
                pbSync = (ProgressBar) rootView.findViewById(R.id.progressBarRevisit);
                if (pbSync != null) {
                    pbSync.setIndeterminate(true);
                    pbSync.setVisibility(View.VISIBLE);
                }
                if(SQLite_saveRevisitDetails()) {
//                    Toast toast = Toast.makeText(mainActivity.getBaseContext(), "पुनर्भेट रिमार्क Saved.", Toast.LENGTH_SHORT);
//                    ViewGroup group = (ViewGroup) toast.getView();
//                    TextView messageTextView = (TextView) group.getChildAt(0);
//                    messageTextView.setTextSize(28);
//                    toast.show();
                    String strMsg = "पुनर्भेट रिमार्क Saved.";
                    ShowToastMessage(strMsg);
                }
                if (pbSync != null) {
                    pbSync.setIndeterminate(true);
                    pbSync.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }
    public boolean SQLite_saveRevisitDetails() {
        boolean Status = false;

        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        String strTimeStamp = sdf.format(date);// all done

        try {
            for(int i=0; i<mainActivity.RevisitMemberList.size(); i++) {
                if(     (mainActivity.RevisitMemberList.get(i).RevisitMemberStatus == clsRevisitMember.REVISIT_MEMBER_STATUS_REVISITED) &&
                        (mainActivity.RevisitMemberList.get(i).Edited) ) {
                    ContentValues values1 = new ContentValues();
                    values1.put(ufinDbContract.PunarbhetSchedule.COLUMN_NAME_VisitDetailsID, mainActivity.RevisitMemberList.get(i).VisitDetailsID);
                    values1.put(ufinDbContract.PunarbhetSchedule.COLUMN_NAME_ActualVisitDate, mainActivity.RevisitMemberList.get(i).ActualVisitDate);
                    values1.put(ufinDbContract.PunarbhetSchedule.COLUMN_NAME_RevisitStatus, mainActivity.RevisitMemberList.get(i).RevisitMemberStatus);
                    values1.put(ufinDbContract.PunarbhetSchedule.COLUMN_NAME_ContactNumber, mainActivity.RevisitMemberList.get(i).ContactNumber);
//bmp 20-Mar-26
                    mainActivity.RevisitMemberList.get(i).CreditCheck = -1;
                    values1.put(ufinDbContract.PunarbhetSchedule.COLUMN_NAME_CreditBureauCheck, mainActivity.RevisitMemberList.get(i).CreditCheck);
//bmp 20-Mar-26
                    JSONObject postDataParams = new JSONObject();
                    try {
                        postDataParams.put(ufinDbContract.PunarbhetSchedule.COLUMN_NAME_MemberID, mainActivity.RevisitMemberList.get(i).MemberID);
                        postDataParams.put("orgVisitDate", mainActivity.RevisitMemberList.get(i).VisitDate);
                        if(mainActivity.RevisitMemberList.get(i).ActualVisitDate.isEmpty()) {
                            mainActivity.RevisitMemberList.get(i).ActualVisitDate = strTimeStamp.substring(0, 19);
                        }
                        postDataParams.put("ActVisitDate", mainActivity.RevisitMemberList.get(i).ActualVisitDate);
                        postDataParams.put("VisitDetID", mainActivity.RevisitMemberList.get(i).VisitDetailsID);
                        postDataParams.put("ContactNumber", mainActivity.RevisitMemberList.get(i).ContactNumber);
                        postDataParams.put("LSO_ID", mainActivity.LSO_ID);
                        postDataParams.put("Org_ID", mainActivity.OrgID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    long lTemp1 = 0;
                    ContentValues values2 = new ContentValues();
                    String StrNewRecord = "";
                    String StrReady = "";
                    String whereClause = ufinDbContract.PunarbhetSchedule.COLUMN_NAME_MemberID + "=" + "'" + String.valueOf(mainActivity.RevisitMemberList.get(i).MemberID) + "'";
                    lTemp1 = db.update(ufinDbContract.PunarbhetSchedule.TABLE_NAME, values1, whereClause, null);
                    if (lTemp1 > 0) {
                        System.out.println("PunarbhetSchedule Updated.");
                        StrNewRecord = "False";
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

                    if (!StrNewRecord.isEmpty()) {
                        values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_SyncID, strTimeStamp);
                        values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_DbTableName, ufinDbContract.PunarbhetSchedule.TABLE_NAME);
                        values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_updatedRecordID, mainActivity.RevisitMemberList.get(i).MemberID);
                        values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsNewRecord, StrNewRecord);
                        values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsReady, StrReady);
                        values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_PostDataParams, strDataParamsQuery);

                        lTemp1 = db.insert(ufinDbContract.RecordsToSync.TABLE_NAME, null, values2);
                        if (lTemp1 > 0) {
                            MainActivity.clsRecordsToSync newRec = new MainActivity.clsRecordsToSync();
                            newRec.SyncID = strTimeStamp;
                            newRec.recordID = mainActivity.RevisitMemberList.get(i).MemberID;
                            newRec.recordInserted = StrNewRecord.equalsIgnoreCase("True");
                            newRec.recordReady = StrReady.equalsIgnoreCase("True");
                            newRec.PostParams = strDataParamsQuery;
                            mainActivity.SyncUpdateRevisitDetails.add(newRec);

                            if(!mainActivity.RevisitMemberList.get(i).strRevisitProof.isEmpty()) {
                                //Copy Punarbhet photo from '/data' folder to '/punarbhetupload' folder
                                File folder = new File(mainActivity.baseDir + "/punarbhetupload");
                                if (!folder.exists())
                                    folder.mkdir();

                                String strFilename = mainActivity.RevisitMemberList.get(i).strRevisitProof;
                                File uploadFile = new File(mainActivity.baseDir + "/punarbhetupload/" + strFilename);
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
                                    File dataFile = new File(mainActivity.baseDir + "/data/" + strFilename);
                                    FileInputStream inData = new FileInputStream(dataFile);
                                    byte[] buffer = new byte[4096];
                                    int bytesRead = -1;
                                    while ((bytesRead = inData.read(buffer)) != -1) {
                                        outData.write(buffer, 0, bytesRead);
                                    }
                                    inData.close();
                                    outData.flush();
                                    outData.close();
                                    mainActivity.SyncPunarbhetDocUpload.add(strFilename);
                                } catch (FileNotFoundException e) {
                                    throw new RuntimeException(e);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }

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

                    long lMemberID = mainActivity.RevisitMemberList.get(i).MemberID;
                    String strContactNumber = mainActivity.RevisitMemberList.get(i).ContactNumber;
                    values1 = new ContentValues();
                    values1.put(ufinDbContract.MemberData.COLUMN_NAME_ContactNo, strContactNumber);
                    String whereClause1 = ufinDbContract.MemberData.COLUMN_NAME_MemberID + "=" + "'" + String.valueOf(lMemberID) + "'";
                    lTemp1 = db.update(ufinDbContract.MemberData.TABLE_NAME, values1, whereClause1, null);
                    if (lTemp1 > 0) {
                        System.out.println("MemberData Updated with ContactNo.");
                    }
                    values1 = new ContentValues();
                    values1.put(ufinDbContract.MemberRecovery.COLUMN_NAME_PhoneNo, strContactNumber);
                    whereClause1 = ufinDbContract.MemberRecovery.COLUMN_NAME_MemberID + "=" + "'" + String.valueOf(lMemberID) + "'";
                    lTemp1 = db.update(ufinDbContract.MemberRecovery.TABLE_NAME, values1, whereClause1, null);
                    if (lTemp1 > 0) {
                        System.out.println("MemberRecovery Updated with ContactNo.");
                    }

                    if(     (mainActivity.RevisitMemberList.get(i).VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED) ||
                            (mainActivity.RevisitMemberList.get(i).VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_ONGOING) ||
                            (mainActivity.RevisitMemberList.get(i).VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_HOMELOAN_REQUIRED) ||
                            (mainActivity.RevisitMemberList.get(i).VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_TURNLOAN_REQUIRED) ) {
                        mainActivity.GetMemberCBCheck(mainActivity.RevisitMemberList.get(i).MemberID);
//                    } else if(      (mainActivity.RevisitMemberList.get(i).VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_NOT_REQUIRED) ||
//                                    (mainActivity.RevisitMemberList.get(i).VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_LOAN_REQUIRED_GROUP_NOT_READY) ||
//                                    (mainActivity.RevisitMemberList.get(i).VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_ANOTHER_LOAN_CREDIT_REJECT) ||
//                                    (mainActivity.RevisitMemberList.get(i).VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_MOVEDTO_NEWPLACE_PERMANENT_BLACKLIST) ||
//                                    (mainActivity.RevisitMemberList.get(i).VisitDetailsID == clsRevisitMember.REVISIT_REMARK_ID_NO_RATIONCARD) ) {
                    } else {
                        mainActivity.GetMemberLoanStatus(mainActivity.RevisitMemberList.get(i).MemberID);
                    }

                    mainActivity.RevisitMemberList.get(i).Edited = false;
                }
            }
        } finally {
            db.close();
        }

        return Status;
    }
//    void GetMemberLoanStatus(long lMemberID) {
//        JSONObject postDataParams = new JSONObject();
//        try {
//            postDataParams.put("PrimaryKey", lMemberID);
//            postDataParams.put("TableName", "TD_PromotionLists");
//            postDataParams.put("ParameterName", "Loan Status");
//            postDataParams.put("LSO_ID", mainActivity.LSO_ID);
//            postDataParams.put("Org_ID", mainActivity.OrgID);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        String strDataParams = "";
//        try {
//            StringBuilder sb = new StringBuilder();
//            Iterator<String> keys = postDataParams.keys();
//            sb.append("?"); //start of query args
//            while (keys.hasNext()) {
//                String key = keys.next();
//                sb.append(key);
//                sb.append("=");
//                sb.append(postDataParams.get(key));
//                sb.append("&"); //To allow for another argument.
//            }
//            strDataParams = sb.toString();
//            strDataParams = strDataParams.substring(0, (strDataParams.length() - 1));
//            strDataParams = strDataParams.replaceAll(" ", "%20");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        Calendar cal = Calendar.getInstance();
//        long lTimeStamp = cal.getTimeInMillis();
//        Date date = cal.getTime();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
//        String strTimeStamp = sdf.format(date);// all done
//        // Gets the data repository in write mode
//        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
//        SQLiteDatabase db2 = mDbHelper.getWritableDatabase();
//        try {
//            ContentValues values2 = new ContentValues();
//            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_SyncID, strTimeStamp);
//            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_DbTableName, "MemberSingleValue");
//            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_updatedRecordID, lMemberID);
//            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsNewRecord, "True");
//            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsReady, "True");
//            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_PostDataParams, strDataParams);
//
//            long lTemp2 = db2.insert(ufinDbContract.RecordsToSync.TABLE_NAME, null, values2);
//            if (lTemp2 > 0) {
//                MainActivity.clsRecordsToSync newRec = new MainActivity.clsRecordsToSync();
//                newRec.SyncID = strTimeStamp;
//                newRec.recordID = lMemberID;
//                newRec.recordInserted = true;
//                newRec.recordReady = true;
//                newRec.PostParams = strDataParams;
//                mainActivity.SyncMemberSingleValue.add(newRec);       //To App-Purna Server
//
//                File packetFile = new File(mainActivity.baseDir + "/config/" + mainActivity.strPacketFile);
//                try {
//                    BufferedWriter bwr = new BufferedWriter(new FileWriter(packetFile, true));
//                    bwr.newLine();
//                    bwr.newLine();
//                    bwr.write(strDataParams);
//                    bwr.flush();
//                    bwr.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        } finally {
//            db2.close();
//        }
//
//    }
//    void GetMemberCBCheck(long lMemberID) {
//        JSONObject postDataParams = new JSONObject();
//        try {
//            postDataParams.put("MemberID", lMemberID);
//            postDataParams.put("LSO_ID", mainActivity.LSO_ID);
//            postDataParams.put("Org_ID", mainActivity.OrgID);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        String strDataParams = "";
//        try {
//            StringBuilder sb = new StringBuilder();
//            Iterator<String> keys = postDataParams.keys();
//            sb.append("?"); //start of query args
//            while (keys.hasNext()) {
//                String key = keys.next();
//                sb.append(key);
//                sb.append("=");
//                sb.append(postDataParams.get(key));
//                sb.append("&"); //To allow for another argument.
//            }
//            strDataParams = sb.toString();
//            strDataParams = strDataParams.substring(0, (strDataParams.length() - 1));
//            strDataParams = strDataParams.replaceAll(" ", "%20");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        Calendar cal = Calendar.getInstance();
//        long lTimeStamp = cal.getTimeInMillis();
//        Date date = cal.getTime();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
//        String strTimeStamp = sdf.format(date);// all done
//        // Gets the data repository in write mode
//        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
//        SQLiteDatabase db2 = mDbHelper.getWritableDatabase();
//        try {
//            ContentValues values2 = new ContentValues();
//            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_SyncID, strTimeStamp);
//            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_DbTableName, "CBStatus");
//            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_updatedRecordID, lMemberID);
//            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsNewRecord, "True");
//            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_IsReady, "True");
//            values2.put(ufinDbContract.RecordsToSync.COLUMN_NAME_PostDataParams, strDataParams);
//
//            long lTemp2 = db2.insert(ufinDbContract.RecordsToSync.TABLE_NAME, null, values2);
//            if (lTemp2 > 0) {
//                MainActivity.clsRecordsToSync newRec = new MainActivity.clsRecordsToSync();
//                newRec.SyncID = strTimeStamp;
//                newRec.recordID = lMemberID;
//                newRec.recordInserted = true;
//                newRec.recordReady = true;
//                newRec.PostParams = strDataParams;
//                mainActivity.SyncCBStatus.add(newRec);       //To App-Purna Server
//
//                File packetFile = new File(mainActivity.baseDir + "/config/" + mainActivity.strPacketFile);
//                try {
//                    BufferedWriter bwr = new BufferedWriter(new FileWriter(packetFile, true));
//                    bwr.newLine();
//                    bwr.newLine();
//                    bwr.write(strDataParams);
//                    bwr.flush();
//                    bwr.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        } finally {
//            db2.close();
//        }
//
//    }

//    void RevisitMemberCreditCheckInfo(int indexRevisitMember) {
//        clsRevisitMember revisitMember = mainActivity.RevisitMemberList.get(indexRevisitMember);
//
//        mainActivity.SQLite_getMembers(revisitMember.MemberID, "MemberID");
//        if(mainActivity.Member.size() == 0) {
//            System.out.println("Revisit Member Not Found in Tab Database.");
//            return;
//        }
//
//        mainActivity.SQLite_getMembers(revisitMember.MemberID, "MainMemberID");
////bmp 14-May-25
//        mainActivity.SQLite_getPersonalImpactInfo(revisitMember.MemberID);
////bmp 14-May-25
//        mainActivity.SQLite_getMemberDocRcvd(revisitMember.MemberID);
//        clsMemberAddress memberAddress = new clsMemberAddress();
//        memberAddress.InitNewMemberAddress();
//        memberAddress.AddressID = mainActivity.Member.get(0).AddressID;
//        mainActivity.SQLite_getAddress(memberAddress);
//
//        clsCreditCheckInfo checkInfoMainBorrower = new clsCreditCheckInfo();
//        checkInfoMainBorrower.InitCreditCheckInfo();
//        checkInfoMainBorrower.Role = "M";
//        for(int i=0; i<mainActivity.MemberDocRcvd.size(); i++) {
//            if(mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == clsDocumentIDs.MEMBER_DOC_ID_AADHAARCARD) {
//                String strDocumentNo = mainActivity.MemberDocRcvd.get(i).DocumentNo;
//                strDocumentNo = strDocumentNo.replace(" ", "");
//                checkInfoMainBorrower.AdhaarNo = strDocumentNo;
//                checkInfoMainBorrower.MainBorrowerAdhaarNo = checkInfoMainBorrower.AdhaarNo;
//            } else if(mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == clsDocumentIDs.MEMBER_DOC_ID_RATIONCARD) {
//                checkInfoMainBorrower.RationCardNo = mainActivity.MemberDocRcvd.get(i).DocumentNo;
//            } else if(mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == clsDocumentIDs.MEMBER_DOC_ID_PANCARD) {
//                checkInfoMainBorrower.PAN_No = mainActivity.MemberDocRcvd.get(i).DocumentNo;
//            } else if(mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == clsDocumentIDs.MEMBER_DOC_ID_ELECTIONCARD) {
//                checkInfoMainBorrower.VoterID = mainActivity.MemberDocRcvd.get(i).DocumentNo;
//            }
//        }
////        checkInfoMainBorrower.AadhaarName = revisitMember.MemberName;
//        checkInfoMainBorrower.FName = mainActivity.Member.get(0).Fname;
//        checkInfoMainBorrower.MName = mainActivity.Member.get(0).Mname;
//        checkInfoMainBorrower.LName = mainActivity.Member.get(0).Lname;
//        checkInfoMainBorrower.Gender = mainActivity.Member.get(0).Gender;
//        checkInfoMainBorrower.DateOfBirth = mainActivity.Member.get(0).BirthDate;
//        checkInfoMainBorrower.Address = memberAddress.Address;
////        checkInfoMainBorrower.City;
////        checkInfoMainBorrower.State;
//        checkInfoMainBorrower.Pincode = memberAddress.Pincode;
//        checkInfoMainBorrower.ContactNo = revisitMember.ContactNumber;
//        if(mainActivity.RelationIDMainMember == 0) {
//            mainActivity.SQLite_getRelationMaster();
//        }
//        checkInfoMainBorrower.MainBorrowerRelationID = mainActivity.RelationIDMainMember;
////        checkInfoMainBorrower.iFamilyMember;
//        checkInfoMainBorrower.ProfessionID = revisitMember.OccupationID;
//        checkInfoMainBorrower.OwnHouse = revisitMember.OwnHouse;
//        checkInfoMainBorrower.MaritalStatusID = revisitMember.MaritalStatusID;
//        checkInfoMainBorrower.ClusterID = revisitMember.ClusterID;
//        checkInfoMainBorrower.Cluster = revisitMember.Cluster;
//        checkInfoMainBorrower.CommunityID = revisitMember.CommunityID;
//        checkInfoMainBorrower.Community = revisitMember.Community;
//
//        checkInfoMainBorrower.MonthlyIncome = mainActivity.PersonalImpactInfo.get(0).MonthlyIncome;
//        SQLite_SaveRevisitMemberCreditCheckInfo(checkInfoMainBorrower);
//
//        if(revisitMember.NextLoanOfferAmount >= 350000) {
//            clsCreditCheckInfo checkInfoCoBorrower = new clsCreditCheckInfo();
//            checkInfoCoBorrower.InitCreditCheckInfo();
//            checkInfoCoBorrower.Role = "C";
//            checkInfoCoBorrower.MainBorrowerAdhaarNo = checkInfoMainBorrower.AdhaarNo;
//            SQLite_SaveRevisitMemberCreditCheckInfo(checkInfoCoBorrower);
//        }
//
//        clsCreditCheckInfo checkInfoGuarantor = new clsCreditCheckInfo();
//        checkInfoGuarantor.InitCreditCheckInfo();
//        checkInfoGuarantor.Role = "G";
//        checkInfoGuarantor.MainBorrowerAdhaarNo = checkInfoMainBorrower.AdhaarNo;
//        int indexGuarantorMember = -1;
//        for(int i=1; i<mainActivity.Member.size(); i++) {
//            String strName = mainActivity.Member.get(i).Fname;
//            strName = strName + " " + mainActivity.Member.get(i).Mname;
//            strName = strName + " " + mainActivity.Member.get(i).Lname;
//            if(strName.equalsIgnoreCase(revisitMember.GuarantorName)) {
//                indexGuarantorMember = i;
//            }
//        }
//        if(indexGuarantorMember > 0) {
//            checkInfoGuarantor.iFamilyMember = 1;
//            checkInfoGuarantor.FName = mainActivity.Member.get(indexGuarantorMember).Fname;
//            checkInfoGuarantor.MName = mainActivity.Member.get(indexGuarantorMember).Mname;
//            checkInfoGuarantor.LName = mainActivity.Member.get(indexGuarantorMember).Lname;
//            checkInfoGuarantor.Gender = mainActivity.Member.get(indexGuarantorMember).Gender;
//            checkInfoGuarantor.DateOfBirth = mainActivity.Member.get(indexGuarantorMember).BirthDate;
//            checkInfoGuarantor.Address = memberAddress.Address;
//            checkInfoGuarantor.Pincode = memberAddress.Pincode;
//            checkInfoGuarantor.ContactNo = mainActivity.Member.get(indexGuarantorMember).ContactNo;
//            checkInfoGuarantor.MainBorrowerRelationID = mainActivity.Member.get(indexGuarantorMember).RelationID;
////bmp 14-May-25
//            mainActivity.SQLite_getPersonalImpactInfo(mainActivity.Member.get(indexGuarantorMember).MemberID);
//            for(int i=0; i<mainActivity.PersonalImpactInfo.size(); i++) {
//                if(mainActivity.PersonalImpactInfo.get(i).MemberID != mainActivity.Member.get(indexGuarantorMember).MemberID)
//                    continue;
//                checkInfoGuarantor.MonthlyIncome = mainActivity.PersonalImpactInfo.get(i).MonthlyIncome;
//                break;
//            }
////bmp 14-May-25
//
//            mainActivity.SQLite_getMemberDocRcvd(mainActivity.Member.get(indexGuarantorMember).MemberID);
//            for(int i=0; i<mainActivity.MemberDocRcvd.size(); i++) {
////bmp 13-May-25
//                if(mainActivity.MemberDocRcvd.get(i).MemberID != mainActivity.Member.get(indexGuarantorMember).MemberID)
//                    continue;
////bmp 13-May-25
//                if(mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == clsDocumentIDs.MEMBER_DOC_ID_AADHAARCARD) {
//                    String strDocumentNo = mainActivity.MemberDocRcvd.get(i).DocumentNo;
//                    strDocumentNo = strDocumentNo.replace(" ", "");
//                    checkInfoGuarantor.AdhaarNo = strDocumentNo;
//                } else if(mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == clsDocumentIDs.MEMBER_DOC_ID_RATIONCARD) {
//                    checkInfoGuarantor.RationCardNo = mainActivity.MemberDocRcvd.get(i).DocumentNo;
//                } else if(mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == clsDocumentIDs.MEMBER_DOC_ID_PANCARD) {
//                    checkInfoGuarantor.PAN_No = mainActivity.MemberDocRcvd.get(i).DocumentNo;
//                } else if(mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == clsDocumentIDs.MEMBER_DOC_ID_ELECTIONCARD) {
//                    checkInfoGuarantor.VoterID = mainActivity.MemberDocRcvd.get(i).DocumentNo;
//                }
//            }
//        } else {
//            checkInfoGuarantor.iFamilyMember = 0;
////            checkInfoGuarantor.AadhaarName = revisitMember.GuarantorName;
//            String[] strSplit = revisitMember.GuarantorName.split(" ");
//            switch (strSplit.length) {
//                case 0:
//                case 1:
//                    checkInfoGuarantor.FName = revisitMember.GuarantorName;
//                    break;
//                case 2:
//                    checkInfoGuarantor.FName = strSplit[0];
//                    checkInfoGuarantor.LName = strSplit[1];
//                    break;
//                case 3:
//                    checkInfoGuarantor.FName = strSplit[0];
//                    checkInfoGuarantor.MName = strSplit[1];
//                    checkInfoGuarantor.LName = strSplit[2];
//                    break;
//            }
//            checkInfoGuarantor.MainBorrowerRelationID = revisitMember.GuarantorRelationID;
//            for(int i=0; i<mainActivity.MemberDocRcvd.size(); i++) {
//                if(mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == clsDocumentIDs.MEMBER_DOC_ID_OTHER_GUARANTOR_AADHAARCARD) {
//                    String strDocumentNo = mainActivity.MemberDocRcvd.get(i).DocumentNo;
//                    strDocumentNo = strDocumentNo.replace(" ", "");
//                    checkInfoGuarantor.AdhaarNo = strDocumentNo;
//                } else if(mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == clsDocumentIDs.MEMBER_DOC_ID_OTHER_GUARANTOR_PANCARD) {
//                    checkInfoGuarantor.PAN_No = mainActivity.MemberDocRcvd.get(i).DocumentNo;
//                } else if(mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == clsDocumentIDs.MEMBER_DOC_ID_OTHER_GUARANTOR_PHOTOID) {
//                    checkInfoGuarantor.VoterID = mainActivity.MemberDocRcvd.get(i).DocumentNo;
//                }
//            }
//        }
//        SQLite_SaveRevisitMemberCreditCheckInfo(checkInfoGuarantor);
//    }
//    void SQLite_SaveRevisitMemberCreditCheckInfo(clsCreditCheckInfo checkInfo) {
//        ufinDbHelper mDbHelper = new ufinDbHelper(mainActivity.getBaseContext());
//        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//        try {
//            ContentValues values = new ContentValues();
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Role, checkInfo.Role);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_AdhaarNo, checkInfo.AdhaarNo);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_RationCardNo, checkInfo.RationCardNo);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_PAN_No, checkInfo.PAN_No);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_VoterID, checkInfo.VoterID);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_FName, checkInfo.FName);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_MName, checkInfo.MName);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_LName, checkInfo.LName);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Gender, checkInfo.Gender);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_DateOfBirth, checkInfo.DateOfBirth);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Address, checkInfo.Address);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_City, checkInfo.City);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_State, checkInfo.State);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Pincode, checkInfo.Pincode);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_MonthlyIncome, checkInfo.MonthlyIncome);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_ContactNo, checkInfo.ContactNo);
//
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_CreditCheckProgress, checkInfo.CreditCheckProgress);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_CreditStatus, checkInfo.CreditHistory);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_MemberType, checkInfo.MemberType);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_ListType, checkInfo.ListType);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Remarks, checkInfo.CHRemarks);
//
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBEnquiryDate, checkInfo.CBEnquiryDate);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBScore, checkInfo.CBScore);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBStatus, checkInfo.CBStatus);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBRemark, checkInfo.CBRemark);
//
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_MainBorrowerRelationID, checkInfo.MainBorrowerRelationID);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_IsFamilyMember, checkInfo.iFamilyMember);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_MainBorrowerAdhaarNo, checkInfo.MainBorrowerAdhaarNo);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_ProfessionID, checkInfo.ProfessionID);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_OwnHouse, checkInfo.OwnHouse);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_MaritalStatusID, checkInfo.MaritalStatusID);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_ClusterID, checkInfo.ClusterID);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_CommunityID, checkInfo.CommunityID);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Cluster, checkInfo.Cluster);
//            values.put(ufinDbContract.CreditValidityCheck.COLUMN_NAME_Community, checkInfo.Community);
////bmp 13-May-25
////            String whereClause = ufinDbContract.CreditValidityCheck.COLUMN_NAME_AdhaarNo + "=" + "'" + checkInfo.AdhaarNo + "'";
//            String whereClause = ufinDbContract.CreditValidityCheck.COLUMN_NAME_AdhaarNo + "=" + "'" + checkInfo.AdhaarNo + "'"
//                    + " and " + ufinDbContract.CreditValidityCheck.COLUMN_NAME_MainBorrowerAdhaarNo + "=" + "'" + checkInfo.MainBorrowerAdhaarNo + "'";
////bmp 13-May-25
//            long lTemp = 0;
//            lTemp = db.update(ufinDbContract.CreditValidityCheck.TABLE_NAME, values, whereClause, null);
//            if (lTemp <= 0) {
//                lTemp = db.insert(ufinDbContract.CreditValidityCheck.TABLE_NAME, null, values);
//                System.out.println(String.valueOf(lTemp));
//            }
//        } finally {
//            db.close();
//        }
//    }
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
