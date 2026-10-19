package com.example.ufin_lso;

//clsNewGroupMemberInfo <=> TD_NewGroupMember_Info SQLite DB Table
public class clsNewGroupMemberInfo {
    long MemberID;
    String GuarantorName;
    long GuarantorRelationID;
    int LoanAmount;
    int LoanDuration;
    int MemberStatus;
    int LoanApplicant;
    String ContactNo;
    long GroupID;
    int iEligibleLoan;
    int selectedPositionLoanProduct;

    void Init_NewGroupMemberInfo() {
        MemberID = 0;
        GuarantorRelationID = 24;
        GuarantorName = "Member Spouse";
        LoanAmount = 0;
        LoanDuration = 0;
        MemberStatus = 0;
        LoanApplicant = -1;
        ContactNo = "";
        GroupID = 0;
        iEligibleLoan = 0;
        selectedPositionLoanProduct = 0;
    }
    public boolean Compare_NewGroupMemberInfo(clsNewGroupMemberInfo Obj) {
        if(this.MemberID != Obj.MemberID)
            return false;
        if(this.GuarantorRelationID != Obj.GuarantorRelationID)
            return false;
        if(this.LoanAmount != Obj.LoanAmount)
            return false;
        if(this.LoanDuration != Obj.LoanDuration)
            return false;
        if(!this.GuarantorName.equalsIgnoreCase(Obj.GuarantorName))
            return false;
        if(!this.ContactNo.equalsIgnoreCase(Obj.ContactNo))
            return false;
        if(this.MemberStatus != Obj.MemberStatus)
            return false;
        if(this.LoanApplicant != Obj.LoanApplicant)
            return false;
        if(this.GroupID != Obj.GroupID)
            return false;
        if(this.iEligibleLoan != Obj.iEligibleLoan)
            return false;
        if(this.selectedPositionLoanProduct != Obj.selectedPositionLoanProduct)
            return false;

        return true;
    }
    public void Copy_NewGroupMemberInfo(clsNewGroupMemberInfo Obj) {
        Obj.MemberID = MemberID;
        Obj.GuarantorRelationID = GuarantorRelationID;
        Obj.LoanAmount = LoanAmount;
        Obj.LoanDuration = LoanDuration;
        Obj.GuarantorName = GuarantorName;
        Obj.MemberStatus = MemberStatus;
        Obj.LoanApplicant = LoanApplicant;
        Obj.ContactNo = ContactNo;
        Obj.GroupID = GroupID;
        Obj.iEligibleLoan = iEligibleLoan;
        Obj.selectedPositionLoanProduct = selectedPositionLoanProduct;
    }
}
