package com.example.ufin_lso.Model;

public class CreditCheckLoan {

    private String mfiName;
    private String loanStatus;
    private String loanType;
    private String ownGuarantor;
    private String loanStartDate;
    private String disbursementAmount;
    private String installmentAmount;
    private String currentBalance;
    private String delinquentAmount;
    private String writeOffAmount;

    public CreditCheckLoan() {
    }

    public String getMfiName() {
        return mfiName;
    }

    public void setMfiName(String mfiName) {
        this.mfiName = mfiName;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getOwnGuarantor() {
        return ownGuarantor;
    }

    public void setOwnGuarantor(String ownGuarantor) {
        this.ownGuarantor = ownGuarantor;
    }

    public String getLoanStartDate() {
        return loanStartDate;
    }

    public void setLoanStartDate(String loanStartDate) {
        this.loanStartDate = loanStartDate;
    }

    public String getDisbursementAmount() {
        return disbursementAmount;
    }

    public void setDisbursementAmount(String disbursementAmount) {
        this.disbursementAmount = disbursementAmount;
    }

    public String getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(String installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getDelinquentAmount() {
        return delinquentAmount;
    }

    public void setDelinquentAmount(String delinquentAmount) {
        this.delinquentAmount = delinquentAmount;
    }

    public String getWriteOffAmount() {
        return writeOffAmount;
    }

    public void setWriteOffAmount(String writeOffAmount) {
        this.writeOffAmount = writeOffAmount;
    }
}