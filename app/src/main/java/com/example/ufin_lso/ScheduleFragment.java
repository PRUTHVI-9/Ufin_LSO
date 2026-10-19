package com.example.ufin_lso;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View rootView;
    MainActivity mainActivity;
    int selectedScheduleDay;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
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
//        return inflater.inflate(R.layout.fragment_schedule, container, false);
        rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
        mainActivity = (MainActivity) getActivity();

        Button bt = (Button) rootView.findViewById(R.id.buttonCloseSchedule);
        bt.setOnClickListener(this);
        bt = (Button) rootView.findViewById(R.id.buttonSaveSchedule);
        bt.setOnClickListener(this);

        mainActivity.SQLite_getActivitySchedule();

        SimpleDateFormat sdf1 = new SimpleDateFormat("EEE", Locale.ENGLISH);
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM", Locale.ENGLISH);
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyMMdd", Locale.ENGLISH);

        selectedScheduleDay = -1;
        LinearLayout llhCalendar = rootView.findViewById(R.id.linearLayoutCalendar);
        llhCalendar.removeAllViews();
        for(int i=0; i<7; i++) {
            LinearLayout llhWeekday = new LinearLayout(mainActivity.getBaseContext());
            llhWeekday.setOrientation(LinearLayout.VERTICAL);
            llhWeekday.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    1));

            Calendar calDay = Calendar.getInstance();
            try {
                //this is to ensure 00:00:00
                calDay.setTime(sdf3.parse(sdf3.format(calDay.getTime())));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            calDay.add(Calendar.DATE, i);

            TextView tvDayOfWeek = new TextView(mainActivity.getBaseContext());
            tvDayOfWeek.setTextSize(16);
            tvDayOfWeek.setTypeface(Typeface.DEFAULT_BOLD);
            tvDayOfWeek.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvDayOfWeek.setGravity(Gravity.CENTER);
            String strDay = sdf1.format(calDay.getTime());
            tvDayOfWeek.setText(strDay);
            int iColor = Color.BLACK;
            if(strDay.equalsIgnoreCase("Sun")) {
                iColor = Color.RED;
            }
            tvDayOfWeek.setTextColor(iColor);
            llhWeekday.addView(tvDayOfWeek);

            for(int j=0; j<6; j++) {
                LinearLayout llvDay = new LinearLayout(mainActivity.getBaseContext());
                llvDay.setBackground(mainActivity.getDrawable(R.drawable.customborder));
                llvDay.setOrientation(LinearLayout.VERTICAL);
                llvDay.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1));

                TextView tv1 = new TextView(mainActivity.getBaseContext());
//                tv1.setBackground(mainActivity.getDrawable(R.drawable.customborder));
                tv1.setTextSize(16);
                tv1.setTypeface(Typeface.DEFAULT_BOLD);
                tv1.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                tv1.setGravity(Gravity.CENTER_HORIZONTAL);
                tv1.setText(sdf2.format(calDay.getTime()));
                tv1.setTextColor(iColor);

                TextView tv2 = new TextView(mainActivity.getBaseContext());
                tv2.setTextSize(16);
                tv2.setTypeface(Typeface.DEFAULT_BOLD);
                tv2.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));

                long lStart = calDay.getTimeInMillis();
                Calendar calNextDay = Calendar.getInstance();
                calNextDay.setTimeInMillis(lStart);
                calNextDay.add(Calendar.DATE, 1);
                long lEnd = calNextDay.getTimeInMillis();
                int iRecoveryCount = 0;
                int iPunarbhetCount = 0;
//bmp 27-Mar-24
//                int iERPCount = 0;
                int iMeetingCount = 0;
                int iTrainingCount = 0;
                int iHolidayCount = 0;
                int iLeaveCount = 0;
                int iOtherCount = 0;
//bmp 27-Mar-24
                int iPIFCount = 0;
                int iGRTCount = 0;
                int iOrientationCount = 0;
                for (int k=0; k<mainActivity.ActivitySchedule.size(); k++) {
//bmp 26-Apr-24
// For activity continued across multiple days,
// we want to show it's entry on each day covered by that activity.
// For this, we need to check if activity starting on previous day and NOT ending on same day.
//                    if(mainActivity.ActivitySchedule.get(k).StartTime < lStart)
//                        continue;
                    if(     (mainActivity.ActivitySchedule.get(k).StartTime < lStart) &&
                            (mainActivity.ActivitySchedule.get(k).EndTime < lStart) )
                        continue;
//bmp 26-Apr-24
                    if(mainActivity.ActivitySchedule.get(k).StartTime >= lEnd)
                        break;
                    if(mainActivity.ActivitySchedule.get(k).ActivityType.equalsIgnoreCase("Recovery"))
                        iRecoveryCount++;
                    else if(mainActivity.ActivitySchedule.get(k).ActivityType.equalsIgnoreCase("Punarbhet"))
                        iPunarbhetCount++;
//bmp 27-Mar-24
//                    else if(mainActivity.ActivitySchedule.get(k).ActivityType.equalsIgnoreCase("ERP"))
//                        iERPCount++;
                    else if(mainActivity.ActivitySchedule.get(k).ActivityType.equalsIgnoreCase("Meeting"))
                        iMeetingCount++;
                    else if(mainActivity.ActivitySchedule.get(k).ActivityType.equalsIgnoreCase("Training"))
                        iTrainingCount++;
                    else if(mainActivity.ActivitySchedule.get(k).ActivityType.equalsIgnoreCase("Holiday"))
                        iHolidayCount++;
                    else if(mainActivity.ActivitySchedule.get(k).ActivityType.equalsIgnoreCase("Leave"))
                        iLeaveCount++;
                    else if(mainActivity.ActivitySchedule.get(k).ActivityType.equalsIgnoreCase("Other"))
                        iOtherCount++;
//bmp 27-Mar-24
                    else if(mainActivity.ActivitySchedule.get(k).ActivityType.equalsIgnoreCase("PIF")) {
                        long lTemp = mainActivity.ActivitySchedule.get(k).EndTime - mainActivity.ActivitySchedule.get(k).StartTime;
                        lTemp = lTemp / (1000 * 60 * 10);
                        if (lTemp > 5)
                            lTemp = 5;
                        iPIFCount = iPIFCount + (int) lTemp;
                    } else if(mainActivity.ActivitySchedule.get(k).ActivityType.equalsIgnoreCase("GRT"))
                        iGRTCount++;
                    else if(mainActivity.ActivitySchedule.get(k).ActivityType.equalsIgnoreCase("Orientation"))
                        iOrientationCount++;
                }
                String strSummary = "";
                String strNewLine = "";
                if(iRecoveryCount > 0) {
                    strSummary = strSummary + strNewLine + "Recovery: " + String.valueOf(iRecoveryCount);
                    strNewLine = "\n";
                }
                if(iPIFCount > 0) {
                    strSummary = strSummary + strNewLine + "PIF: " + String.valueOf(iPIFCount);
                    strNewLine = "\n";
                }
                if(iPunarbhetCount > 0) {
                    strSummary = strSummary + strNewLine + "Punarbhet: " + String.valueOf(iPunarbhetCount);
                    strNewLine = "\n";
                }
//bmp 27-Mar-24
//                if(iERPCount > 0) {
//                    strSummary = strSummary + strNewLine + "ERP: " + String.valueOf(iERPCount);
//                    strNewLine = "\n";
//                }
                if(iMeetingCount > 0) {
                    strSummary = strSummary + strNewLine + "Meeting: " + String.valueOf(iMeetingCount);
                    strNewLine = "\n";
                }
                if(iTrainingCount > 0) {
                    strSummary = strSummary + strNewLine + "Training: " + String.valueOf(iTrainingCount);
                    strNewLine = "\n";
                }
                if(iHolidayCount > 0) {
                    strSummary = strSummary + strNewLine + "Holiday: " + String.valueOf(iHolidayCount);
                    strNewLine = "\n";
                }
                if(iLeaveCount > 0) {
                    strSummary = strSummary + strNewLine + "Leave: " + String.valueOf(iLeaveCount);
                    strNewLine = "\n";
                }
                if(iOtherCount > 0) {
                    strSummary = strSummary + strNewLine + "Other: " + String.valueOf(iOtherCount);
                    strNewLine = "\n";
                }
//bmp 27-Mar-24
                if(iGRTCount > 0) {
                    strSummary = strSummary + strNewLine + "GRT: " + String.valueOf(iGRTCount);
                    strNewLine = "\n";
                }
                if(iOrientationCount > 0) {
                    strSummary = strSummary + strNewLine + "Orientation: " + String.valueOf(iOrientationCount);
                    strNewLine = "\n";
                }
                tv2.setText(strSummary);

                llvDay.addView(tv1);
                llvDay.addView(tv2);
                llvDay.setId(Integer.parseInt(sdf3.format(calDay.getTime())));
                llvDay.setEnabled(true);
                llvDay.setClickable(true);
                llvDay.setOnClickListener(ScheduleDayOnClickListener);

                llhWeekday.addView(llvDay);
                calDay.add(Calendar.DATE, 7);
            }

            llhCalendar.addView(llhWeekday);
        }

        return rootView;
    }
    private View.OnClickListener ScheduleDayOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if(selectedScheduleDay >= 0) {
                View vPrev = (View) rootView.findViewById(selectedScheduleDay);
                vPrev.setBackgroundColor(Color.WHITE);
                vPrev.setBackground(mainActivity.getDrawable(R.drawable.customborder));
            }
            int iTemp = v.getId();
            if(selectedScheduleDay == iTemp) {
                selectedScheduleDay = -1;
                TextView tv = rootView.findViewById(R.id.textViewSchedule);
                tv.setText("Schedule: ");
                LinearLayout llvSchedule = rootView.findViewById(R.id.linearLayoutSchedule);
                llvSchedule.removeAllViews();
            } else {
                selectedScheduleDay = iTemp;
                v.setBackgroundColor(Color.CYAN);
                TextView tv = rootView.findViewById(R.id.textViewSchedule);
                SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM", Locale.ENGLISH);
                SimpleDateFormat sdf3 = new SimpleDateFormat("yyMMdd", Locale.ENGLISH);
                Calendar calDay = Calendar.getInstance();
                try {
                    calDay.setTime(sdf3.parse(String.valueOf(selectedScheduleDay)));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                tv.setText("Schedule: " + sdf2.format(calDay.getTime()));

                long lStart = calDay.getTimeInMillis();
                Calendar calNextDay = Calendar.getInstance();
                try {
                    calNextDay.setTime(sdf3.parse(String.valueOf(selectedScheduleDay)));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                calNextDay.add(Calendar.DATE, 1);
                long lEnd = calNextDay.getTimeInMillis();

                showDaySchedule(lStart, lEnd);
            }
        }
    };

    public void showDaySchedule(long lStart, long lEnd) {
        LinearLayout llvSchedule = rootView.findViewById(R.id.linearLayoutSchedule);
        llvSchedule.removeAllViews();

        TableRow tblRowHdr = new TableRow(mainActivity.getBaseContext());
        tblRowHdr.setBackground(mainActivity.getDrawable(R.drawable.customborder));
        tblRowHdr.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        TextView tvHdr1 = new TextView(mainActivity.getBaseContext());
        tvHdr1.setBackground(mainActivity.getDrawable(R.drawable.customborder));
        tvHdr1.setTextSize(20);
        tvHdr1.setTypeface(Typeface.DEFAULT_BOLD);
        tvHdr1.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT,
                (float) 2.3));
        tvHdr1.setGravity(Gravity.CENTER_HORIZONTAL);
        tvHdr1.setText("Activity");

        TextView tvHdr2 = new TextView(mainActivity.getBaseContext());
        tvHdr2.setBackground(mainActivity.getDrawable(R.drawable.customborder));
        tvHdr2.setTextSize(20);
        tvHdr2.setTypeface(Typeface.DEFAULT_BOLD);
        tvHdr2.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                (float) 2.4));
        tvHdr2.setGravity(Gravity.CENTER_HORIZONTAL);
        tvHdr2.setText("Start");

        TextView tvHdr3 = new TextView(mainActivity.getBaseContext());
        tvHdr3.setBackground(mainActivity.getDrawable(R.drawable.customborder));
        tvHdr3.setTextSize(20);
        tvHdr3.setTypeface(Typeface.DEFAULT_BOLD);
        tvHdr3.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                (float) 2.4));
        tvHdr3.setGravity(Gravity.CENTER_HORIZONTAL);
        tvHdr3.setText("End");

        TextView tvHdr4 = new TextView(mainActivity.getBaseContext());
        tvHdr4.setBackground(mainActivity.getDrawable(R.drawable.customborder));
        tvHdr4.setTextSize(20);
        tvHdr4.setTypeface(Typeface.DEFAULT_BOLD);
        tvHdr4.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1));
        tvHdr4.setGravity(Gravity.CENTER_HORIZONTAL);
        tvHdr4.setText("Details");

        tblRowHdr.addView(tvHdr1);
        tblRowHdr.addView(tvHdr2);
        tblRowHdr.addView(tvHdr3);
        tblRowHdr.addView(tvHdr4);

        llvSchedule.addView(tblRowHdr);

        Calendar calTime = Calendar.getInstance();
        SimpleDateFormat sdf4 = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        int iCountYellow = 0;
        int iCountPink = 0;
        int iCountPista = 0;
        int iCountBlue = 0;
        int iCountWhite = 0;
        int iCountTotal = 0;
        boolean ActivityContinuedFromPreviousDay = false;
        boolean ActivityContinuedToNextDay = false;
        for (int i=0; i<mainActivity.ActivitySchedule.size(); i++) {
//bmp 26-Apr-24
// For activity continued across multiple days,
// we want to show it's entry on each day covered by that activity.
// For this, we need to check if activity starting on previous day and NOT ending on same day.
//                    if(mainActivity.ActivitySchedule.get(i).StartTime < lStart)
//                        continue;
            if(     (mainActivity.ActivitySchedule.get(i).StartTime < lStart) &&
                    (mainActivity.ActivitySchedule.get(i).EndTime < lStart) )
                continue;

            ActivityContinuedFromPreviousDay = false;
            if(     (mainActivity.ActivitySchedule.get(i).StartTime < lStart) &&
                    (mainActivity.ActivitySchedule.get(i).EndTime > lStart) )
                ActivityContinuedFromPreviousDay = true;
//bmp 26-Apr-24

            ActivityContinuedToNextDay = false;
            if(mainActivity.ActivitySchedule.get(i).EndTime > lEnd)
                ActivityContinuedToNextDay = true;

            if(mainActivity.ActivitySchedule.get(i).StartTime >= lEnd)
                break;

            calTime.setTimeInMillis(mainActivity.ActivitySchedule.get(i).StartTime);
            String strStartTime = sdf4.format(calTime.getTime());
            if(strStartTime.startsWith("00:00")) {
                strStartTime = "-";
            }
            if(ActivityContinuedFromPreviousDay) {
                strStartTime = "10:30 AM";
            }

            calTime.setTimeInMillis(mainActivity.ActivitySchedule.get(i).EndTime);
            String strEndTime = sdf4.format(calTime.getTime());
            if(strEndTime.startsWith("00:00")) {
                strEndTime = "-";
            }
            if(ActivityContinuedToNextDay) {
                strEndTime = "06:30 PM";
            }

            if(mainActivity.ActivitySchedule.get(i).ActivityType.equalsIgnoreCase("Punarbhet")) {
                iCountTotal++;
                if(mainActivity.ActivitySchedule.get(i).ActivityDetails.contains("Yellow"))
                    iCountYellow++;
                else if(mainActivity.ActivitySchedule.get(i).ActivityDetails.contains("Pink"))
                    iCountPink++;
                else if(mainActivity.ActivitySchedule.get(i).ActivityDetails.contains("Pista"))
                    iCountPista++;
                else if(mainActivity.ActivitySchedule.get(i).ActivityDetails.contains("Blue"))
                    iCountBlue++;
                else if(mainActivity.ActivitySchedule.get(i).ActivityDetails.contains("White"))
                    iCountWhite++;
                continue;
            }

            TableRow tblRow = new TableRow(mainActivity.getBaseContext());
//            tblRow.setBackground(mainActivity.getDrawable(R.drawable.customborder));
            tblRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            TextView tv1 = new TextView(mainActivity.getBaseContext());
            tv1.setBackground(mainActivity.getDrawable(R.drawable.customborder));
            tv1.setTextSize(19);
            tv1.setTypeface(Typeface.DEFAULT_BOLD);
            tv1.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    (float) 2.3));
            tv1.setGravity(Gravity.CENTER_HORIZONTAL);
            tv1.setText(mainActivity.ActivitySchedule.get(i).ActivityType);

            TextView tv2 = new TextView(mainActivity.getBaseContext());
            tv2.setBackground(mainActivity.getDrawable(R.drawable.customborder));
            tv2.setTextSize(18);
            tv2.setTypeface(Typeface.DEFAULT_BOLD);
            tv2.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    (float) 2.4));
            tv2.setGravity(Gravity.CENTER_HORIZONTAL);
            tv2.setText(strStartTime);

            TextView tv3 = new TextView(mainActivity.getBaseContext());
            tv3.setBackground(mainActivity.getDrawable(R.drawable.customborder));
            tv3.setTextSize(18);
            tv3.setTypeface(Typeface.DEFAULT_BOLD);
            tv3.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    (float) 2.4));
            tv3.setGravity(Gravity.CENTER_HORIZONTAL);
            tv3.setText(strEndTime);

            TextView tv4 = new TextView(mainActivity.getBaseContext());
            tv4.setBackground(mainActivity.getDrawable(R.drawable.customborder));
            tv4.setTextSize(19);
            tv4.setTypeface(Typeface.DEFAULT_BOLD);
            tv4.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    1));
            tv4.setGravity(Gravity.LEFT);
            tv4.setText(mainActivity.ActivitySchedule.get(i).ActivityDetails);

            tblRow.addView(tv1);
            tblRow.addView(tv2);
            tblRow.addView(tv3);
            tblRow.addView(tv4);

            llvSchedule.addView(tblRow);
        }

        if(iCountTotal > 0) {
            String strPunarbhetDetails = "";
            if(iCountYellow > 0)
                strPunarbhetDetails = strPunarbhetDetails + " " + "Yellow:" + String.valueOf(iCountYellow);
            if(iCountPink > 0)
                strPunarbhetDetails = strPunarbhetDetails + " " + "Pink:" + String.valueOf(iCountPink);
            if(iCountPista > 0)
                strPunarbhetDetails = strPunarbhetDetails + " " + "Pista:" + String.valueOf(iCountPista);
            if(iCountBlue > 0)
                strPunarbhetDetails = strPunarbhetDetails + " " + "Blue:" + String.valueOf(iCountBlue);
            if(iCountWhite > 0)
                strPunarbhetDetails = strPunarbhetDetails + " " + "White:" + String.valueOf(iCountWhite);

            TableRow tblRow = new TableRow(mainActivity.getBaseContext());
//            tblRow.setBackground(mainActivity.getDrawable(R.drawable.customborder));
            tblRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            TextView tv1 = new TextView(mainActivity.getBaseContext());
            tv1.setBackground(mainActivity.getDrawable(R.drawable.customborder));
            tv1.setTextSize(19);
            tv1.setTypeface(Typeface.DEFAULT_BOLD);
            tv1.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    (float) 2.3));
            tv1.setGravity(Gravity.CENTER_HORIZONTAL);
            tv1.setText("Punarbhet");

            TextView tv2 = new TextView(mainActivity.getBaseContext());
            tv2.setBackground(mainActivity.getDrawable(R.drawable.customborder));
            tv2.setTextSize(18);
            tv2.setTypeface(Typeface.DEFAULT_BOLD);
            tv2.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    (float) 2.4));
            tv2.setGravity(Gravity.CENTER_HORIZONTAL);
            tv2.setText("-");

            TextView tv3 = new TextView(mainActivity.getBaseContext());
            tv3.setBackground(mainActivity.getDrawable(R.drawable.customborder));
            tv3.setTextSize(18);
            tv3.setTypeface(Typeface.DEFAULT_BOLD);
            tv3.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    (float) 2.4));
            tv3.setGravity(Gravity.CENTER_HORIZONTAL);
            tv3.setText("-");

            TextView tv4 = new TextView(mainActivity.getBaseContext());
            tv4.setBackground(mainActivity.getDrawable(R.drawable.customborder));
            tv4.setTextSize(19);
            tv4.setTypeface(Typeface.DEFAULT_BOLD);
            tv4.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT,
                    1));
            tv4.setGravity(Gravity.LEFT);
            tv4.setText(strPunarbhetDetails);

            tblRow.addView(tv1);
            tblRow.addView(tv2);
            tblRow.addView(tv3);
            tblRow.addView(tv4);

            llvSchedule.addView(tblRow);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCloseSchedule:
//                mainActivity.InitDesktop();
//                if(mainActivity.iCurrentScreen == 1) {
                    if(mainActivity.iPrevViewId == R.id.viewpagerGF) {
                        CustomViewPager viewPager = (CustomViewPager) mainActivity.findViewById(R.id.viewpagerGF);
                        if (viewPager != null)
                            viewPager.requestFocus();
                        mainActivity.iCurrentScreen = 0;
                        viewPager.setCurrentItem(mainActivity.iCurrentScreen);
                    } else if(mainActivity.iPrevViewId == R.id.viewpagerCollection) {
                        CustomViewPager viewPager = (CustomViewPager) mainActivity.findViewById(R.id.viewpagerCollection);
                        if (viewPager != null)
                            viewPager.requestFocus();
                        mainActivity.iCurrentScreen = 1;
                        viewPager.setCurrentItem(mainActivity.iCurrentScreen);
//                    }
                } else {
                    mainActivity.InitDesktop();
                }
                break;
            case R.id.buttonSaveSchedule:
                break;
        }
    }
}