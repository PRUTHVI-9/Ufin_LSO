
package com.example.ufin_lso;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ufin_lso.Adapter.CreditCheckLoanAdapter;
import com.example.ufin_lso.Model.CreditCheckLoan;


import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreditCheckDetailFragment extends Fragment {

    private static final String TAG = "CB_DETAIL";
    private static final String ARG_MEMBER_ID = "memberID";
    private static final String ARG_ORG_ID = "orgID";

    private long memberID = -1;
    private long orgID = -1;

    private RecyclerView rvCreditCheckLoans;
    private Button backbtn;

    private TextView tvCBStatus;
    private TextView tvCBScore;
    private TextView tvActiveLoans;
    private TextView tvTotalLoanBalance;
    private TextView tvLastLoanStatus;
    private TextView tvCheckedDate;
    private TextView tvMemberName;
    private TextView tvSavingAccountNo;
    private TextView tvCurrentbalance;
    private TextView tvSummaryEmiTotal;
    private CreditCheckLoanAdapter adapter;
    HorizontalScrollView hsvLoanHeader;

    private final ArrayList<CreditCheckLoan> loanList = new ArrayList<>();

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private boolean isBackPressed = false;

    public CreditCheckDetailFragment() {}

    public static CreditCheckDetailFragment newInstance(long memberID, long orgID) {
        CreditCheckDetailFragment fragment = new CreditCheckDetailFragment();

        Bundle args = new Bundle();
        args.putLong(ARG_MEMBER_ID, memberID);
        args.putLong(ARG_ORG_ID, orgID);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_credit_check_detail, container, false);
        Bundle args = getArguments();

        if (args != null) {
            memberID = args.getLong(ARG_MEMBER_ID, -1);
            orgID = args.getLong(ARG_ORG_ID, -1);
        }

        rvCreditCheckLoans = view.findViewById(R.id.rvCreditCheckLoans);
        backbtn = view.findViewById(R.id.backbtn);

//        tvCBStatus = view.findViewById(R.id.tvCBStatus);
        tvCBScore = view.findViewById(R.id.tvSummaryCBScore);
        tvMemberName = view.findViewById(R.id.tvSummaryMemberName);
        tvSavingAccountNo = view.findViewById(R.id.tvSummarySavingAccount);
        tvActiveLoans = view.findViewById(R.id.tvSummaryactiveloan);
        tvCurrentbalance = view.findViewById(R.id.tvSummarycurrentbal);
        tvSummaryEmiTotal = view.findViewById(R.id.tvSummaryEmiTotal);
        hsvLoanHeader = view.findViewById(R.id.hsvLoanHeader);

        isBackPressed = false;

       /* rvCreditCheckLoans.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new CreditCheckLoanAdapter(requireContext(), loanList);
        rvCreditCheckLoans.setAdapter(adapter);*/


        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());

        rvCreditCheckLoans.setLayoutManager(layoutManager);

        adapter = new CreditCheckLoanAdapter(requireContext(), loanList, hsvLoanHeader);

        rvCreditCheckLoans.setAdapter(adapter);

        setupBackButton();

        requireActivity()
                .getOnBackPressedDispatcher()
                .addCallback(
                        getViewLifecycleOwner(),
                        new OnBackPressedCallback(true) {
                            @Override
                            public void handleOnBackPressed() {
                                navigateBack();
                            }
                        }
                );
        backbtn.setOnClickListener(v -> {

            if (isAdded()) {
                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack();
            }
        });
        if (memberID <= 0 || orgID <= 0) {
            Toast.makeText(requireContext(), "Invalid Member ID or Org ID", Toast.LENGTH_LONG).show();
            return view;
        }
        getCreditCheckDetails();

        return view;


    }

    private void setupBackButton() {

        if (backbtn == null) {
            return;
        }

        backbtn.setOnClickListener(v -> navigateBack());
    }

    private void navigateBack() {

        if (isBackPressed || !isAdded()) {
            return;
        }
        androidx.fragment.app.FragmentManager fm = getParentFragmentManager();
        if (fm.isStateSaved()) {
            return;
        }
        isBackPressed = true;
        if (backbtn != null) {
            backbtn.setEnabled(false);
        }

        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            isBackPressed = false;

            if (backbtn != null) {
                backbtn.setEnabled(true);
            }

//            Log.w(TAG, "No fragment in back stack to return to");
        }
    }

    private void getCreditCheckDetails() {

        final Context appContext = requireContext().getApplicationContext();

        final long requestedMemberID = memberID;
        final long requestedOrgID = orgID;

        new Thread(() -> {
            ArrayList<CreditCheckLoan> resultList = new ArrayList<>();
            String cbStatus = "-";
            String cbScore = "-";
            String activeLoans = "-";
            String totalBalance = "-";
            String lastLoanStatus = "-";
            String checkedDate = "-";
            String memberName = "-";
            String SavingAccNo = "-";
            String currentbal = "-";
            String emitotal = "-";

            boolean recordFound = false;
            String errorMessage = null;

            ufinDbHelper dbHelper = null;
            SQLiteDatabase db = null;

            try {

                dbHelper = new ufinDbHelper(appContext);
                db = dbHelper.getReadableDatabase();

                String[] memberDetails = getMemberDetails(db, requestedMemberID);

                memberName = memberDetails[0];
                SavingAccNo = memberDetails[1];

                String selection = ufinDbContract.MemberCBStatusAuto.COLUMN_MemberID
                        + " = ? AND "
                        + ufinDbContract.MemberCBStatusAuto.COLUMN_Org_ID
                        + " = ?";

                String[] selectionArgs = {
                        String.valueOf(requestedMemberID),
                        String.valueOf(requestedOrgID)
                };

                try (Cursor cursor = db.query(
                        ufinDbContract.MemberCBStatusAuto.TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                )) {

                    Log.i(TAG, "MemberID: " + requestedMemberID);
                    Log.i(TAG, "OrgID: " + requestedOrgID);
                    Log.i(TAG, "Rows found: " + cursor.getCount());

                    if (cursor.moveToFirst()) {

                        recordFound = true;

                        // Summary values
                        cbStatus = getColumnValue(
                                cursor,
                                ufinDbContract.MemberCBStatusAuto.COLUMN_CBStatus
                        );

                        cbScore = getColumnValue(
                                cursor,
                                ufinDbContract.MemberCBStatusAuto.COLUMN_CBScore
                        );

                        activeLoans = getColumnValue(
                                cursor,
                                ufinDbContract.MemberCBStatusAuto.COLUMN_NoOfActiveLoans
                        );

                        totalBalance = getColumnValue(
                                cursor,
                                ufinDbContract.MemberCBStatusAuto.COLUMN_TotalLoanBalance
                        );

                        lastLoanStatus = getColumnValue(
                                cursor,
                                ufinDbContract.MemberCBStatusAuto.COLUMN_LastLoanStatus
                        );

                        checkedDate = getColumnValue(
                                cursor,
                                ufinDbContract.MemberCBStatusAuto.COLUMN_Checked_Date
                        );
                        currentbal = getColumnValue(cursor,ufinDbContract.MemberCBStatusAuto.COLUMN_CurrentBalance);
                        emitotal = getColumnValue(cursor,ufinDbContract.MemberCBStatusAuto.COLUMN_InstallamentAmount);

                        do {

                            CreditCheckLoan loan = new CreditCheckLoan();

//                            loan.setMfiName(getColumnValue(cursor, ufinDbContract.MemberCBStatusAuto.COLUMN_MFIname));
                            loan.setMfiName("Ujjivan small  Finance");

                            loan.setLoanStatus(getColumnValue(
                                    cursor,
                                    ufinDbContract.MemberCBStatusAuto.COLUMN_LoanStatus
                            ));

                            loan.setLoanType(getColumnValue(
                                    cursor,
                                    ufinDbContract.MemberCBStatusAuto.COLUMN_LoanType
                            ));

                            loan.setOwnGuarantor(getColumnValue(
                                    cursor,
                                    ufinDbContract.MemberCBStatusAuto.COLUMN_OwnGuarantor
                            ));

                            loan.setLoanStartDate(getColumnValue(
                                    cursor,
                                    ufinDbContract.MemberCBStatusAuto.COLUMN_LoanStartSate
                            ));

                            loan.setDisbursementAmount(getColumnValue(
                                    cursor,
                                    ufinDbContract.MemberCBStatusAuto.COLUMN_DisbursementAmout
                            ));

                            loan.setInstallmentAmount(getColumnValue(
                                    cursor,
                                    ufinDbContract.MemberCBStatusAuto.COLUMN_InstallamentAmount
                            ));

                            loan.setCurrentBalance(getColumnValue(
                                    cursor,
                                    ufinDbContract.MemberCBStatusAuto.COLUMN_CurrentBalance
                            ));

                            loan.setDelinquentAmount(getColumnValue(
                                    cursor,
                                    ufinDbContract.MemberCBStatusAuto.COLUMN_DelinquentAmount
                            ));

                            loan.setWriteOffAmount(getColumnValue(
                                    cursor,
                                    ufinDbContract.MemberCBStatusAuto.COLUMN_WriteOffAmount
                            ));

                            resultList.add(loan);

                        } while (cursor.moveToNext());
                    }
                }

            } catch (Exception e) {

                Log.e(TAG, "Failed to read CB details", e);
                errorMessage = e.getMessage();

            } finally {

                if (db != null && db.isOpen()) {
                    db.close();
                }

                if (dbHelper != null) {
                    dbHelper.close();
                }
            }

            // Make final copies for UI thread
            final ArrayList<CreditCheckLoan> finalResultList = new ArrayList<>(resultList);

            final String finalCBStatus = cbStatus;
            final String finalCBScore = cbScore;
            final String finalActiveLoans = activeLoans;
            final String finalTotalBalance = totalBalance;
            final String finalLastLoanStatus = lastLoanStatus;
            final String finalCheckedDate = checkedDate;
            final boolean finalRecordFound = recordFound;
            final String finalErrorMessage = errorMessage;
            final String finalMemberName = memberName;
            final String finalSavingAccNo = SavingAccNo;
            final String finalcurrentbal = currentbal;
            final String finalemiTotal = emitotal;

            mainHandler.post(() -> {

                if (!isAdded() || getView() == null) {
                    return;
                }

                if (finalErrorMessage != null) {
                    Toast.makeText(requireContext(), "Unable to load CB details", Toast.LENGTH_LONG).show();
                    return;
                }

                loanList.clear();
                loanList.addAll(finalResultList);

                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }

                if (!finalRecordFound) {

                    showEmptySummary();

                    Toast.makeText(requireContext(), "No CB details found for this member", Toast.LENGTH_LONG).show();
                    return;
                }

//                setTextIfAvailable(tvCBStatus, finalCBStatus);
                setTextIfAvailable(tvCBScore, finalCBScore);
                setTextIfAvailable(tvActiveLoans, finalActiveLoans);
                setTextIfAvailable(tvTotalLoanBalance, finalTotalBalance);
                setTextIfAvailable(tvLastLoanStatus, finalLastLoanStatus);
                setTextIfAvailable(tvCheckedDate, finalCheckedDate);
                setTextIfAvailable(tvMemberName, finalMemberName);
                setTextIfAvailable(tvSavingAccountNo, finalSavingAccNo);
                setTextIfAvailable(tvCurrentbalance ,finalcurrentbal);
                setTextIfAvailable(tvSummaryEmiTotal,finalemiTotal);

                if (finalResultList.isEmpty()) {
                    Toast.makeText(requireContext(), "CB summary loaded, but no loan details found", Toast.LENGTH_LONG).show();}

                Log.i(TAG, "CB details loaded. Loan rows: " + finalResultList.size());
            });

        }).start();
    }


    private void showEmptySummary() {

        setTextIfAvailable(tvCBStatus, "-");
        setTextIfAvailable(tvCBScore, "-");
        setTextIfAvailable(tvActiveLoans, "-");
        setTextIfAvailable(tvTotalLoanBalance, "-");
        setTextIfAvailable(tvLastLoanStatus, "-");
        setTextIfAvailable(tvCheckedDate, "-");
    }


    private String getColumnValue(
            Cursor cursor,
            String columnName) {

        int index = cursor.getColumnIndex(columnName);

        if (index == -1 || cursor.isNull(index)) {
            return "-";
        }

        String value = cursor.getString(index);

        if (value == null || value.trim().isEmpty()) {
            return "-";
        }

        return value;
    }



    private void setTextIfAvailable(
            TextView textView,
            String value) {

        if (textView == null) {
            return;
        }

        if (value == null || value.trim().isEmpty()) {
            textView.setText("-");
        } else {
            textView.setText(value);
        }
    }

    private String[] getMemberDetails(
            SQLiteDatabase db,
            long memberID) {

        String memberName = "-";
        String savingAccNo = "-";

        String selection = ufinDbContract.PunarbhetSchedule.COLUMN_NAME_MemberID + " = ?";

        String[] selectionArgs = {
                String.valueOf(memberID)
        };

        try (Cursor cursor = db.query(
                ufinDbContract.PunarbhetSchedule.TABLE_NAME,
                new String[]{
                        ufinDbContract.PunarbhetSchedule.COLUMN_NAME_MemberName,
                        ufinDbContract.PunarbhetSchedule.COLUMN_NAME_SavingAccNo
                },
                selection,
                selectionArgs,
                null,
                null,
                null
        )) {

            if (cursor.moveToFirst()) {

                memberName = getColumnValue(
                        cursor,
                        ufinDbContract.PunarbhetSchedule.COLUMN_NAME_MemberName
                );

                savingAccNo = getColumnValue(
                        cursor,
                        ufinDbContract.PunarbhetSchedule.COLUMN_NAME_SavingAccNo
                );

                Log.e(TAG, "getMemberDetails: "+ memberName+savingAccNo );
            }
        }

        return new String[]{memberName, savingAccNo};
    }

    @Override
    public void onDestroyView() {

        mainHandler.removeCallbacksAndMessages(null);

        if (rvCreditCheckLoans != null) {
            rvCreditCheckLoans.setAdapter(null);
        }

        rvCreditCheckLoans = null;
        backbtn = null;

        tvCBStatus = null;
        tvCBScore = null;
        tvActiveLoans = null;
        tvTotalLoanBalance = null;
        tvLastLoanStatus = null;
        tvCheckedDate = null;

        adapter = null;

        super.onDestroyView();
    }
}
