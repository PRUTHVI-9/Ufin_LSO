package com.example.ufin_lso;

public class clsMemberFamilyDetails {
    long MemberFamilyDetailsID;
    long MemberID;
    long RefereceMemberID;
    int LoanAllowed;
    String LoanNotAllowedReason;
    String Remarks;
    int MonthlyBhishi;
    int MedicalExpenses;
    int MonthlyInstallments;
    String RationCardNo;
    String RationCardType;
    int WhoseContactNo;
    int WhoRunsBusiness;
    String AddressOnRationcard;
//bmp 20-Sep-22
    long LoanNotAllowedReasonId;
//bmp 20-Sep-22
    public void Init_MemberFamilyDetails() {
        MemberFamilyDetailsID = 0;
        MemberID = 0;
        RefereceMemberID = 0;
        LoanAllowed = 1;
        LoanNotAllowedReason = "NA";
        Remarks = "NA";
        MonthlyBhishi = 0;
        MedicalExpenses = 0;
        MonthlyInstallments = 0;
        RationCardNo = "NA";
        RationCardType = "White";
        WhoseContactNo = 1;
        WhoRunsBusiness = 0;
        AddressOnRationcard = "NA";
//bmp 20-Sep-22
        LoanNotAllowedReasonId = 0;
//bmp 20-Sep-22
    }

    public boolean Compare_MemberFamilyDetails(clsMemberFamilyDetails Obj) {
        if(MemberFamilyDetailsID != Obj.MemberFamilyDetailsID)
            return false;
        if(MemberID != Obj.MemberID)
            return false;
        if(RefereceMemberID != Obj.RefereceMemberID)
            return false;
        if(LoanAllowed != Obj.LoanAllowed)
            return false;
        if(!LoanNotAllowedReason.equalsIgnoreCase(Obj.LoanNotAllowedReason))
            return false;
        if(!Remarks.equalsIgnoreCase(Obj.Remarks))
            return false;
        if(MonthlyBhishi != Obj.MonthlyBhishi)
            return false;
        if(MedicalExpenses != Obj.MedicalExpenses)
            return false;
        if(MonthlyInstallments != Obj.MonthlyInstallments)
            return false;
        if(!RationCardNo.equalsIgnoreCase(Obj.RationCardNo))
            return false;
        if(!RationCardType.equalsIgnoreCase(Obj.RationCardType))
            return false;
        if(WhoseContactNo != Obj.WhoseContactNo)
            return false;
        if(WhoRunsBusiness != Obj.WhoRunsBusiness)
            return false;
        if(!AddressOnRationcard.equalsIgnoreCase(Obj.AddressOnRationcard))
            return false;
//bmp 20-Sep-22
        if(LoanNotAllowedReasonId != Obj.LoanNotAllowedReasonId)
            return false;
//bmp 20-Sep-22

        return true;
    }
    public void Copy_MemberFamilyDetails(clsMemberFamilyDetails Obj) {
        Obj.MemberFamilyDetailsID = MemberFamilyDetailsID;
        Obj.MemberID = MemberID;
        Obj.RefereceMemberID = RefereceMemberID;
        Obj.LoanAllowed = LoanAllowed;
        Obj.LoanNotAllowedReason = LoanNotAllowedReason;
        Obj.Remarks = Remarks;
        Obj.MonthlyBhishi = MonthlyBhishi;
        Obj.MedicalExpenses = MedicalExpenses;
        Obj.MonthlyInstallments = MonthlyInstallments;
        Obj.RationCardNo = RationCardNo;
        Obj.RationCardType = RationCardType;
        Obj.WhoseContactNo = WhoseContactNo;
        Obj.WhoRunsBusiness = WhoRunsBusiness;
        Obj.AddressOnRationcard = AddressOnRationcard;
//bmp 20-Sep-22
        Obj.LoanNotAllowedReasonId = LoanNotAllowedReasonId;
//bmp 20-Sep-22
    }
}
