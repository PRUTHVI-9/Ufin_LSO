package com.example.ufin_lso;

public class clsAdditionalDetails {
    public static int OTHER_GUARANTOR_REASON_ID_SPOUSE_OUTSTATION_ORGID_1          = 694;
    public static int OTHER_GUARANTOR_REASON_ID_SPOUSE_OUTSTATION_ORGID_2          = 670;

    long MemberID;

    long NomineeGuardianBusinessID;
    String NomineeGuardianContactNumber;
    long NomineeGuardianRelationID;
    long NomineeBusinessID;

    //Selected by LSO when PIF Submitted
    long LoanProductDetailsID;
    int PromotionNextLoanAmount;
    int LoanProductDetailsAmount1;
    int LoanProductDetailsAmount2;
    int LoanProductDetailsAmount3;
    int LoanProductDetailsAmount4;
    long LoanProductDetailsID1;
    long LoanProductDetailsID2;
    long LoanProductDetailsID3;
    long LoanProductDetailsID4;     //HomeLoanAmount as per PromotionList.csv
    long LoanReasonID;
    String strLoanReason;
    int GuarantorType;
    long LoanGuarantorID;

    long OtherGuarantorBusinessID;
    String OtherGuarantorContactNumber;
    String OtherGuarantorFName;
    String OtherGuarantorMName;
    String OtherGuarantorLName;
    int OtherGuarantorMonthlyIncome;
    long OtherGuarantorRelationID;

    long OtherGuarantorAddressID;
    String OtherGuarantorBirthDate;
    long OtherGuarantorReasonID;

    long JointLoaneeID;
    long OtherJointLoaneeAddressID;
    String OtherJointLoaneeBirthDate;
    String OtherJointLoaneeContactNumber;
    String OtherJointLoaneeFname;
    String OtherJointLoaneeMname;
    String OtherJointLoaneeLname;

    int OtherJointLoaneeMonthlyIncome;
    long OtherJointLoaneeProfessionID;
    long OtherJointLoaneeReasonID;
    long OtherJointLoaneeRelationID;

    long DeficitFundResourceID;
    long LoanUtilizationID1;
    long LoanUtilizationID2;
    long LoanUtilizationID3;
    long LoanUtilizationID4;
    int OtherLoan;
    int OtherLoanAmount1;
    int OtherLoanAmount2;
    long OtherLoanBankID1;
    long OtherLoanBankID2;
    int OtherLoanInstallment1;
    int OtherLoanInstallment2;
    int OtherLoanRepayMonths1;
    int OtherLoanRepayMonths2;

    //Used Locally only
    long LoanID;
    int LoanAmount;
    int LoanDuration;
    String GuarantorAddress;
    String GuarantorAddressPincode;
    String OtherJointLoaneeAddress;
    String OtherJointLoaneePincode;

    int Monthly_Surplus;
    int NewLoanAmount;
    int HomeLoanAmount;
    String ListType;
    int incentiveCommunityMeetingPartAttendance;
    int incentiveCommunityMeetingFullAttendance;
    int NextLoanOfferAmount;
    long LastVisitDetailsID;
//bmp 13-Aug-26
    int LoanCycle;
//bmp 13-Aug-26

    void Init_AdditionalDetails() {
        MemberID = 0;

        NomineeGuardianBusinessID = 0;
        NomineeGuardianContactNumber = "0";
        NomineeGuardianRelationID = 0;
        NomineeBusinessID = 0;

        LoanProductDetailsID = 0;
        LoanProductDetailsID1 = 0;
        LoanProductDetailsID2 = 0;
        LoanProductDetailsID3 = 0;
        LoanProductDetailsID4 = 0;
        PromotionNextLoanAmount = 0;
        LoanProductDetailsAmount1 = 0;
        LoanProductDetailsAmount2 = 0;
        LoanProductDetailsAmount3 = 0;
        LoanProductDetailsAmount4 = 0;
        LoanReasonID = 0;
        strLoanReason = "";
        GuarantorType = 1;
        LoanGuarantorID = 0;

        OtherGuarantorBusinessID = 0;
        OtherGuarantorContactNumber = "0";
        OtherGuarantorFName = "NA";
        OtherGuarantorMName = "NA";
        OtherGuarantorLName = "NA";
        OtherGuarantorMonthlyIncome = 0;
        OtherGuarantorRelationID = 0;

        OtherGuarantorAddressID = 0;
        OtherGuarantorBirthDate = "2000-01-01";
        OtherGuarantorReasonID = 0;

        JointLoaneeID = -1;
        OtherJointLoaneeAddressID = 0;
        OtherJointLoaneeBirthDate = "2000-01-01";
        OtherJointLoaneeContactNumber = "0";

        OtherJointLoaneeFname = "NA";
        OtherJointLoaneeMname = "NA";
        OtherJointLoaneeLname = "NA";

        OtherJointLoaneeMonthlyIncome = 0;
        OtherJointLoaneeProfessionID = 0;
        OtherJointLoaneeReasonID = 0;
        OtherJointLoaneeRelationID = 0;

        DeficitFundResourceID = 0;
        LoanUtilizationID1 = 0;
        LoanUtilizationID2 = 0;
        LoanUtilizationID3 = 0;
        LoanUtilizationID4 = 0;
        OtherLoan = 0;
        OtherLoanAmount1 = 0;
        OtherLoanAmount2 = 0;
        OtherLoanBankID1 = 0;
        OtherLoanBankID2 = 0;
        OtherLoanInstallment1 = 0;
        OtherLoanInstallment2 = 0;
        OtherLoanRepayMonths1 = 0;
        OtherLoanRepayMonths2 = 0;

        //Used Locally only
        LoanID = 0;
        LoanAmount = 0;
        LoanDuration = 0;
        GuarantorAddress = "NA";
        GuarantorAddressPincode = "0";
        OtherJointLoaneeAddress = "NA";
        OtherJointLoaneePincode = "0";

        Monthly_Surplus = 0;
        NewLoanAmount = 0;
        HomeLoanAmount = 0;
        ListType = "";
        incentiveCommunityMeetingPartAttendance = 0;
        incentiveCommunityMeetingFullAttendance = 0;
        NextLoanOfferAmount = 0;
        LastVisitDetailsID = 0;
//bmp 13-Aug-26
        LoanCycle = 0;
//bmp 13-Aug-26
    }
    public boolean Compare_AdditionalDetails(clsAdditionalDetails Obj) {
        if(this.MemberID != Obj.MemberID)
            return false;

        if(this.NomineeGuardianBusinessID != Obj.NomineeGuardianBusinessID)
            return false;
        if(!this.NomineeGuardianContactNumber.equalsIgnoreCase(Obj.NomineeGuardianContactNumber))
            return false;
        if(this.NomineeGuardianRelationID != Obj.NomineeGuardianRelationID)
            return false;
        if(this.NomineeBusinessID != Obj.NomineeBusinessID)
            return false;

        if(this.LoanProductDetailsID != Obj.LoanProductDetailsID)
            return false;
        if(this.LoanReasonID != Obj.LoanReasonID)
            return false;

        if(this.GuarantorType != Obj.GuarantorType)
            return false;
        if(this.LoanGuarantorID != Obj.LoanGuarantorID)
            return false;

        if(this.OtherGuarantorBusinessID != Obj.OtherGuarantorBusinessID)
            return false;
        if(!this.OtherGuarantorContactNumber.equalsIgnoreCase(Obj.OtherGuarantorContactNumber))
            return false;
        if(!this.OtherGuarantorFName.equalsIgnoreCase(Obj.OtherGuarantorFName))
            return false;
        if(!this.OtherGuarantorMName.equalsIgnoreCase(Obj.OtherGuarantorMName))
            return false;
        if(!this.OtherGuarantorLName.equalsIgnoreCase(Obj.OtherGuarantorLName))
            return false;
        if(this.OtherGuarantorMonthlyIncome != Obj.OtherGuarantorMonthlyIncome)
            return false;
        if(this.OtherGuarantorRelationID != Obj.OtherGuarantorRelationID)
            return false;

        if(this.OtherGuarantorAddressID != Obj.OtherGuarantorAddressID)
            return false;
        if(!this.OtherGuarantorBirthDate.equalsIgnoreCase(Obj.OtherGuarantorBirthDate))
            return false;
        if(this.OtherGuarantorReasonID != Obj.OtherGuarantorReasonID)
            return false;

        if(this.JointLoaneeID != Obj.JointLoaneeID)
            return false;
        if(this.OtherJointLoaneeAddressID != Obj.OtherJointLoaneeAddressID)
            return false;
        if(!this.OtherJointLoaneeBirthDate.equalsIgnoreCase(Obj.OtherJointLoaneeBirthDate))
            return false;
        if(!this.OtherJointLoaneeContactNumber.equalsIgnoreCase(Obj.OtherJointLoaneeContactNumber))
            return false;

        if(!this.OtherJointLoaneeFname.equalsIgnoreCase(Obj.OtherJointLoaneeFname))
            return false;
        if(!this.OtherJointLoaneeMname.equalsIgnoreCase(Obj.OtherJointLoaneeMname))
            return false;
        if(!this.OtherJointLoaneeLname.equalsIgnoreCase(Obj.OtherJointLoaneeLname))
            return false;

        if(this.OtherJointLoaneeMonthlyIncome != Obj.OtherJointLoaneeMonthlyIncome)
            return false;
        if(this.OtherJointLoaneeProfessionID != Obj.OtherJointLoaneeProfessionID)
            return false;
        if(this.OtherJointLoaneeReasonID != Obj.OtherJointLoaneeReasonID)
            return false;
        if(this.OtherJointLoaneeRelationID != Obj.OtherJointLoaneeRelationID)
            return false;

        if(this.DeficitFundResourceID != Obj.DeficitFundResourceID)
            return false;
        if(this.LoanUtilizationID1 != Obj.LoanUtilizationID1)
            return false;
        if(this.LoanUtilizationID2 != Obj.LoanUtilizationID2)
            return false;
        if(this.LoanUtilizationID3 != Obj.LoanUtilizationID3)
            return false;
        if(this.LoanUtilizationID4 != Obj.LoanUtilizationID4)
            return false;
        if(this.OtherLoan != Obj.OtherLoan)
            return false;
        if(this.OtherLoanAmount1 != Obj.OtherLoanAmount1)
            return false;
        if(this.OtherLoanAmount2 != Obj.OtherLoanAmount2)
            return false;
        if(this.OtherLoanBankID1 != Obj.OtherLoanBankID1)
            return false;
        if(this.OtherLoanBankID2 != Obj.OtherLoanBankID2)
            return false;
        if(this.OtherLoanInstallment1 != Obj.OtherLoanInstallment1)
            return false;
        if(this.OtherLoanInstallment2 != Obj.OtherLoanInstallment2)
            return false;
        if(this.OtherLoanRepayMonths1 != Obj.OtherLoanRepayMonths1)
            return false;
        if(this.OtherLoanRepayMonths2 != Obj.OtherLoanRepayMonths2)
            return false;

        //Used Locally only
        if(this.LoanID != Obj.LoanID)
            return false;
        if(this.LoanAmount != Obj.LoanAmount)
            return false;
        if(this.LoanDuration != Obj.LoanDuration)
            return false;
        if(!this.GuarantorAddress.equalsIgnoreCase(Obj.GuarantorAddress))
            return false;
        if(!this.GuarantorAddressPincode.equalsIgnoreCase(Obj.GuarantorAddressPincode))
            return false;
        if(!this.OtherJointLoaneeAddress.equalsIgnoreCase(Obj.OtherJointLoaneeAddress))
            return false;
        if(!this.OtherJointLoaneePincode.equalsIgnoreCase(Obj.OtherJointLoaneePincode))
            return false;
        if(!this.strLoanReason.equalsIgnoreCase(Obj.strLoanReason))
            return false;

        if(this.Monthly_Surplus != Obj.Monthly_Surplus)
            return false;
        if(this.NewLoanAmount != Obj.NewLoanAmount)
            return false;
        if(this.HomeLoanAmount != Obj.HomeLoanAmount)
            return false;

        if(this.incentiveCommunityMeetingPartAttendance != Obj.incentiveCommunityMeetingPartAttendance)
            return false;
        if(this.incentiveCommunityMeetingFullAttendance != Obj.incentiveCommunityMeetingFullAttendance)
            return false;
        if(this.NextLoanOfferAmount != Obj.NextLoanOfferAmount)
            return false;
        if(this.LastVisitDetailsID != Obj.LastVisitDetailsID)
            return false;
        if(this.LoanCycle != Obj.LoanCycle)
            return false;

        return true;
    }
    public void Copy_AdditionalDetails(clsAdditionalDetails Obj) {
        Obj.MemberID = MemberID;

        Obj.NomineeGuardianBusinessID = NomineeGuardianBusinessID;
        Obj.NomineeGuardianContactNumber = NomineeGuardianContactNumber;
        Obj.NomineeGuardianRelationID = NomineeGuardianRelationID;
        Obj.NomineeBusinessID = NomineeBusinessID;

        Obj.LoanProductDetailsID = LoanProductDetailsID;
        Obj.LoanProductDetailsID1 = LoanProductDetailsID1;
        Obj.LoanProductDetailsID2 = LoanProductDetailsID2;
        Obj.LoanProductDetailsID3 = LoanProductDetailsID3;
        Obj.LoanProductDetailsID4 = LoanProductDetailsID4;
        Obj.LoanProductDetailsAmount1 = LoanProductDetailsAmount1;
        Obj.LoanProductDetailsAmount2 = LoanProductDetailsAmount2;
        Obj.LoanProductDetailsAmount3 = LoanProductDetailsAmount3;
        Obj.LoanProductDetailsAmount4 = LoanProductDetailsAmount4;
        Obj.LoanReasonID = LoanReasonID;
        Obj.strLoanReason = strLoanReason;

        Obj.GuarantorType = GuarantorType;
        Obj.LoanGuarantorID = LoanGuarantorID;

        Obj.OtherGuarantorBusinessID = OtherGuarantorBusinessID;
        Obj.OtherGuarantorContactNumber = OtherGuarantorContactNumber;
        Obj.OtherGuarantorFName = OtherGuarantorFName;
        Obj.OtherGuarantorMName = OtherGuarantorMName;
        Obj.OtherGuarantorLName = OtherGuarantorLName;
        Obj.OtherGuarantorMonthlyIncome = OtherGuarantorMonthlyIncome;
        Obj.OtherGuarantorRelationID = OtherGuarantorRelationID;

        Obj.OtherGuarantorAddressID = OtherGuarantorAddressID;
        Obj.OtherGuarantorBirthDate = OtherGuarantorBirthDate;
        Obj.OtherGuarantorReasonID = OtherGuarantorReasonID;

        Obj.JointLoaneeID = JointLoaneeID;
        Obj.OtherJointLoaneeAddressID = OtherJointLoaneeAddressID;
        Obj.OtherJointLoaneeBirthDate = OtherJointLoaneeBirthDate;
        Obj.OtherJointLoaneeContactNumber = OtherJointLoaneeContactNumber;

        Obj.OtherJointLoaneeFname = OtherJointLoaneeFname;
        Obj.OtherJointLoaneeMname = OtherJointLoaneeMname;
        Obj.OtherJointLoaneeLname = OtherJointLoaneeLname;

        Obj.OtherJointLoaneeMonthlyIncome = OtherJointLoaneeMonthlyIncome;
        Obj.OtherJointLoaneeProfessionID = OtherJointLoaneeProfessionID;
        Obj.OtherJointLoaneeReasonID = OtherJointLoaneeReasonID;
        Obj.OtherJointLoaneeRelationID = OtherJointLoaneeRelationID;

        Obj.DeficitFundResourceID = DeficitFundResourceID;
        Obj.LoanUtilizationID1 = LoanUtilizationID1;
        Obj.LoanUtilizationID2 = LoanUtilizationID2;
        Obj.LoanUtilizationID3 = LoanUtilizationID3;
        Obj.LoanUtilizationID4 = LoanUtilizationID4;
        Obj.OtherLoan = OtherLoan;
        Obj.OtherLoanAmount1 = OtherLoanAmount1;
        Obj.OtherLoanAmount2 = OtherLoanAmount2;
        Obj.OtherLoanBankID1 = OtherLoanBankID1;
        Obj.OtherLoanBankID2 = OtherLoanBankID2;
        Obj.OtherLoanInstallment1 = OtherLoanInstallment1;
        Obj.OtherLoanInstallment2 = OtherLoanInstallment2;
        Obj.OtherLoanRepayMonths1 = OtherLoanRepayMonths1;
        Obj.OtherLoanRepayMonths2 = OtherLoanRepayMonths2;

        //Used Locally only
        Obj.LoanID = LoanID;
        Obj.LoanAmount = LoanAmount;
        Obj.LoanDuration = LoanDuration;
        Obj.GuarantorAddress = GuarantorAddress;
        Obj.GuarantorAddressPincode = GuarantorAddressPincode;
        Obj.OtherJointLoaneeAddress = OtherJointLoaneeAddress;
        Obj.OtherJointLoaneePincode = OtherJointLoaneePincode;

        Obj.Monthly_Surplus = Monthly_Surplus;
        Obj.NewLoanAmount = NewLoanAmount;
        Obj.HomeLoanAmount = HomeLoanAmount;
        Obj.ListType = ListType;
        Obj.incentiveCommunityMeetingFullAttendance = incentiveCommunityMeetingFullAttendance;
        Obj.incentiveCommunityMeetingPartAttendance = incentiveCommunityMeetingPartAttendance;
        Obj.NextLoanOfferAmount = NextLoanOfferAmount;
        Obj.LastVisitDetailsID = LastVisitDetailsID;

        Obj.LoanCycle = LoanCycle;
    }
}
