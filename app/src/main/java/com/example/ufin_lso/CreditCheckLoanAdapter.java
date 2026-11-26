package com.example.ufin_lso.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ufin_lso.Model.CreditCheckLoan;
import com.example.ufin_lso.R;

import java.util.ArrayList;

import android.widget.HorizontalScrollView;
import java.util.HashSet;
import java.util.Set;

/*

public class CreditCheckLoanAdapter extends RecyclerView.Adapter<CreditCheckLoanAdapter.LoanViewHolder> {

    private final Context context;
    private final ArrayList<CreditCheckLoan> loanList;

    public CreditCheckLoanAdapter(
            Context context,
            ArrayList<CreditCheckLoan> loanList) {

        this.context = context;
        this.loanList = loanList;
    }

    @NonNull
    @Override
    public LoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(context).inflate(R.layout.item_credit_check_details, parent, false);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_credit_check_details, parent, false
                );


        return new LoanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanViewHolder holder, int position) {

        CreditCheckLoan loan = loanList.get(position);

        holder.tvMfiName.setText(  loan.getMfiName());

        holder.tvLoanStatus.setText( loan.getLoanStatus());

        holder.tvLoanType.setText( loan.getLoanType());

        holder.tvOwnGuarantor.setText( loan.getOwnGuarantor());

        holder.tvLoanStartDate.setText(loan.getLoanStartDate());

        holder.tvDisbursementAmount.setText(loan.getDisbursementAmount());

        holder.tvInstallmentAmount.setText(  loan.getInstallmentAmount());

        holder.tvCurrentBalance.setText( loan.getCurrentBalance());

        holder.tvDelinquentAmount.setText( loan.getDelinquentAmount());

        holder.tvWriteOffAmount.setText( loan.getWriteOffAmount());
    }

    @Override
    public int getItemCount() {
        return loanList.size();
    }

    public static class LoanViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvMfiName;
        TextView tvLoanStatus;
        TextView tvLoanType;
        TextView tvOwnGuarantor;
        TextView tvLoanStartDate;
        TextView tvDisbursementAmount;
        TextView tvInstallmentAmount;
        TextView tvCurrentBalance;
        TextView tvDelinquentAmount;
        TextView tvWriteOffAmount;

        public LoanViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMfiName = itemView.findViewById(R.id.tvMfiName);

            tvLoanStatus = itemView.findViewById(R.id.tvLoanStatus);

            tvLoanType = itemView.findViewById(R.id.tvLoanType);

            tvOwnGuarantor = itemView.findViewById(R.id.tvOwnGuarantor);

            tvLoanStartDate = itemView.findViewById(R.id.tvLoanStartDate);

            tvDisbursementAmount = itemView.findViewById(R.id.tvDisbursementAmount);

            tvInstallmentAmount = itemView.findViewById(R.id.tvInstallmentAmount);

            tvCurrentBalance = itemView.findViewById(R.id.tvCurrentBalance);

            tvDelinquentAmount = itemView.findViewById(R.id.tvDelinquentAmount);

            tvWriteOffAmount = itemView.findViewById(R.id.tvWriteOffAmount);
        }
    }
}*/





public class CreditCheckLoanAdapter extends RecyclerView.Adapter<CreditCheckLoanAdapter.LoanViewHolder> {

    private final Context context;
    private final ArrayList<CreditCheckLoan> loanList;
    private final HorizontalScrollView headerScrollView;

    private final Set<LoanViewHolder> attachedHolders = new HashSet<>();

    private boolean isSyncingHorizontal = false;

    public CreditCheckLoanAdapter(
            Context context,
            ArrayList<CreditCheckLoan> loanList,
            HorizontalScrollView headerScrollView) {

        this.context = context;
        this.loanList = loanList;
        this.headerScrollView = headerScrollView;

        // Header scroll -> sync all visible rows
        if (headerScrollView != null) {
            headerScrollView.setOnScrollChangeListener(
                    (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

                        if (!isSyncingHorizontal) {
                            syncHorizontalScroll(scrollX);
                        }
                    });
        }
    }

    @NonNull
    @Override
    public LoanViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context).inflate(
                R.layout.item_credit_check_details,
                parent,
                false
        );

        LoanViewHolder holder = new LoanViewHolder(view);

        // Row scroll -> sync header and other visible rows
        holder.hsvLoanRow.setOnScrollChangeListener(
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

                    if (!isSyncingHorizontal) {
                        syncHorizontalScroll(scrollX);
                    }
                });

        return holder;
    }

    @Override
    public void onBindViewHolder(
            @NonNull LoanViewHolder holder,
            int position) {

        CreditCheckLoan loan = loanList.get(position);

        holder.tvMfiName.setText(safe(loan.getMfiName()));

        holder.tvLoanType.setText(safe(loan.getLoanType()));

        holder.tvLoanStartDate.setText(
                safe(loan.getLoanStartDate()));

        // NEW: Own Guarantor
        holder.tvOwnGuarantor.setText(
                safe(loan.getOwnGuarantor()));

        holder.tvDisbursementAmount.setText(
                safe(loan.getDisbursementAmount()));

        holder.tvInstallmentAmount.setText(
                safe(loan.getInstallmentAmount()));

        holder.tvCurrentBalance.setText(
                safe(loan.getCurrentBalance()));

        holder.tvDelinquentAmount.setText(
                safe(loan.getDelinquentAmount()));

        holder.tvWriteOffAmount.setText(
                safe(loan.getWriteOffAmount()));

        // Keep newly bound row at the same horizontal position as header
        if (headerScrollView != null) {

            isSyncingHorizontal = true;

            holder.hsvLoanRow.scrollTo(
                    headerScrollView.getScrollX(), 0);

            isSyncingHorizontal = false;
        }
    }

    private String safe(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private void syncHorizontalScroll(int scrollX) {

        if (isSyncingHorizontal) {
            return;
        }

        isSyncingHorizontal = true;

        try {

            // Sync header only when position is different
            if (headerScrollView != null &&
                    headerScrollView.getScrollX() != scrollX) {

                headerScrollView.scrollTo(scrollX, 0);
            }

            // Sync visible rows only when position is different
            for (LoanViewHolder holder : attachedHolders) {

                if (holder.hsvLoanRow.getScrollX() != scrollX) {

                    holder.hsvLoanRow.scrollTo(scrollX, 0);
                }
            }

        } finally {

            isSyncingHorizontal = false;
        }
    }

    @Override
    public void onViewAttachedToWindow(
            @NonNull LoanViewHolder holder) {

        super.onViewAttachedToWindow(holder);
        attachedHolders.add(holder);
    }

    @Override
    public void onViewDetachedFromWindow(
            @NonNull LoanViewHolder holder) {

        attachedHolders.remove(holder);
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(
            @NonNull LoanViewHolder holder) {

        attachedHolders.remove(holder);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return loanList == null ? 0 : loanList.size();
    }

    static class LoanViewHolder extends RecyclerView.ViewHolder {

        TextView tvMfiName;
        TextView tvLoanType;

        TextView tvLoanStartDate;
        TextView tvOwnGuarantor;

        TextView tvDisbursementAmount;
        TextView tvInstallmentAmount;
        TextView tvCurrentBalance;
        TextView tvDelinquentAmount;
        TextView tvWriteOffAmount;

        HorizontalScrollView hsvLoanRow;

        LoanViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMfiName =
                    itemView.findViewById(R.id.tvMfiName);

            tvLoanType =
                    itemView.findViewById(R.id.tvLoanType);

            tvLoanStartDate =
                    itemView.findViewById(R.id.tvLoanStartDate);

            // NEW: Own Guarantor TextView
            tvOwnGuarantor =
                    itemView.findViewById(R.id.tvOwnGuarantor);

            tvDisbursementAmount =
                    itemView.findViewById(R.id.tvDisbursementAmount);

            tvInstallmentAmount =
                    itemView.findViewById(R.id.tvInstallmentAmount);

            tvCurrentBalance =
                    itemView.findViewById(R.id.tvCurrentBalance);

            tvDelinquentAmount =
                    itemView.findViewById(R.id.tvDelinquentAmount);

            tvWriteOffAmount =
                    itemView.findViewById(R.id.tvWriteOffAmount);

            hsvLoanRow =
                    itemView.findViewById(R.id.hsvLoanRow);
        }
    }
}
