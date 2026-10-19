package com.example.ufin_lso;

public class clsRevisitMember {
    public static final Integer REVISIT_REMARK_ID_LOAN_REQUIRED                         = 1;
    public static final Integer REVISIT_REMARK_ID_HOMELOAN_REQUIRED                     = 15;
    public static final Integer REVISIT_REMARK_ID_TURNLOAN_REQUIRED                     = 16;
    public static final Integer REVISIT_REMARK_ID_LOAN_ONGOING                          = 2;
    public static final Integer REVISIT_REMARK_ID_LOAN_NOT_REQUIRED                     = 4;
    public static final Integer REVISIT_REMARK_ID_LOAN_REQUIRED_GROUP_NOT_READY         = 5;
    public static final Integer REVISIT_REMARK_ID_ANOTHER_LOAN_CREDIT_REJECT            = 6;
    public static final Integer REVISIT_REMARK_ID_MEMBER_DEATH                          = 7;
    public static final Integer REVISIT_REMARK_ID_MOVEDTO_NEWPLACE_PERMANENT_BLACKLIST  = 9;
    public static final Integer REVISIT_REMARK_ID_NO_RATIONCARD                         = 10;

    public static final Integer REVISIT_REMARK_ID_INCOMPLETE_KYC_DOCUMENTS              = 426;
    public static final Integer REVISIT_REMARK_ID_AREA_CHANGE_MEMBER_RETURNED_BACK      = 388;

    public static final int REVISIT_MEMBER_STATUS_INIT                = 0;
    public static final int REVISIT_MEMBER_STATUS_SCHEDULED           = 1;
    public static final int REVISIT_MEMBER_STATUS_REVISITED           = 2;
    public static final int REVISIT_MEMBER_STATUS_REGROUPED           = 3;
    public static final int REVISIT_MEMBER_STATUS_READYGROUP          = 4;
    public static final int REVISIT_MEMBER_STATUS_GROUPFORM_SUBMITTED = 5;
    public static final int REVISIT_MEMBER_STATUS_PIF_SUBMITTED       = 6;
    public static final int REVISIT_MEMBER_STATUS_QUERY               = 7;

    public static final int REVISIT_MEMBER_INCENTIVE_GRADE_A_GROUP    = 15000;
    public static final int REVISIT_MEMBER_INCENTIVE_FULL_ATTENDANCE  = 10000;
    public static final int REVISIT_MEMBER_INCENTIVE_HALF_ATTENDANCE  = 5000;
    public static final int REVISIT_MEMBER_INCENTIVE_NO_ATTENDANCE    = 0;

    long ClusterID;
    String Cluster;
    long CommunityID;
    String Community;
    long CenterID;
    long GroupID;
    String GroupName;
    long MemberID;
    String MemberName;
    String SavingAccNo;
    String ContactNumber;
//bmp 07-Jul-25
    String strRevisitProof;
    String strRegroupProof;
//bmp 07-Jul-25
    String ListType;
    int LastLoanAmount;
    int PrevMaxLoanAmount;
    int HomeLoanAmount;
    int LoanCycle;
    long MaritalStatusID;
    int OwnHouse;
    int CreditCheck;
    String VisitDate;
    long LastVisitDetailsID;
    long OccupationID;
    String GuarantorName;
    long GuarantorRelationID;

    String ActualVisitDate;
    long VisitDetailsID;
    int RevisitMemberStatus;         //Init/Scheduled/Revisited/Regrouped/GroupFormSubmitted/PIFSubmitted
    int CBTotalLoanBalance;
    int CBNoOfActiveLoans;
    int CBScore;
    String CBRemark;
    String EnquiryDate;
    String LastLoanStatus;
    boolean Single;
    long LoanReasonID;
    String filenamePhoto;
    boolean Edited;
    int LastCycleGroupContinued;
    String MaturityDate;
    long LongMaturityDate;
    int BalanceAmount;
    int OverdueAmount;
    long LastCenterID;
    long LastGroupID;
//bmp 14-Nov-22
    String TargetComMeetingDate;
    String TargetLoanDisbursementDate;
//bmp 14-Nov-22
    int incentiveGenuineBTypeGroup;
    int incentiveRegularLoanRepay;
    int incentiveNotWithdrawingSavingsShares;
    int incentiveRegularRDpay;
    int incentiveRegularADHPpay;
    int incentiveAGMAttendance;
//bmp 11-Jan-24
    int incentiveCommunityMeetingNoAttendance;
    int incentiveCommunityMeetingPartAttendance;
    int incentiveCommunityMeetingFullAttendance;
    int NextLoanOfferAmount;
    int flagNewMember;
    int incentiveGenuineGradeGroup;
    int incentiveLowPrevMaxLoanAmount;
//bmp 11-Jan-24
//bmp 28-Aug-25
    int UpFrontJLGBalance;
    int CurrentJLGBalance;
//bmp 28-Aug-25
    public void InitRevisitMember() {
        ClusterID = 0;
        Cluster = "";
        CommunityID = 0;
        Community = "";
        CenterID = 0;
        GroupID = 0;
        GroupName = "";
        MemberID = 0;
        MemberName = "";
        SavingAccNo = "";
        ContactNumber = "";
//bmp 07-Jul-25
        strRevisitProof = "";
//bmp 07-Jul-25
        strRegroupProof = "";
        ListType = "";
        LastLoanAmount = 0;
        PrevMaxLoanAmount = 0;
        HomeLoanAmount = 0;
        LoanCycle = 0;
        MaritalStatusID = 0;
        OwnHouse = 0;
//bmp 18-Mar-26
//        CreditCheck = 0;
        CreditCheck = -1;
//bmp 18-Mar-26
        VisitDate = "";
        LastVisitDetailsID = 0;
        OccupationID = 0;
        GuarantorName = "";
        GuarantorRelationID = 0;

        ActualVisitDate = "";
        VisitDetailsID = 0;
        RevisitMemberStatus = REVISIT_MEMBER_STATUS_INIT;
        CBTotalLoanBalance = -1;
        CBNoOfActiveLoans = -1;
        CBScore = -1;
        CBRemark = "";
        EnquiryDate = "";
        LastLoanStatus = "";
        Single = false;
        LoanReasonID = 0;
        filenamePhoto = "";

        Edited = false;
        LastCycleGroupContinued = 0;
        MaturityDate = "2010-01-01";
        LongMaturityDate = 0;
        BalanceAmount = 0;
        OverdueAmount = 0;
        LastCenterID = 0;
        LastGroupID = 0;
//bmp 14-Nov-22
        TargetComMeetingDate = "";
        TargetLoanDisbursementDate = "";
//bmp 14-Nov-22
        incentiveGenuineBTypeGroup = 0;
        incentiveRegularLoanRepay = 0;
        incentiveNotWithdrawingSavingsShares = 0;
        incentiveRegularRDpay = 0;
        incentiveRegularADHPpay = 0;
        incentiveAGMAttendance = 0;
//bmp 11-Jan-24
        incentiveCommunityMeetingNoAttendance = 0;
        incentiveCommunityMeetingPartAttendance = 0;
        incentiveCommunityMeetingFullAttendance = 0;
        NextLoanOfferAmount = 0;
        flagNewMember = 0;
        incentiveGenuineGradeGroup = 0;
        incentiveLowPrevMaxLoanAmount = 0;
//bmp 11-Jan-24
//bmp 28-Aug-25
        UpFrontJLGBalance = 0;
        CurrentJLGBalance = 0;
//bmp 28-Aug-25
    }
    public void Copy_RevisitMember(clsRevisitMember Obj) {
        Obj.ClusterID = ClusterID;
        Obj.Cluster = Cluster;
        Obj.CommunityID = CommunityID;
        Obj.Community = Community;
        Obj.CenterID = CenterID;
        Obj.GroupID = GroupID;
        Obj.GroupName = GroupName;
        Obj.MemberID = MemberID;
        Obj.MemberName = MemberName;
        Obj.SavingAccNo = SavingAccNo;
        Obj.ContactNumber = ContactNumber;
//bmp 07-Jul-25
        Obj.strRevisitProof = strRevisitProof;
//bmp 07-Jul-25
        Obj.strRegroupProof = strRegroupProof;
        Obj.ListType = ListType;
        Obj.LastLoanAmount = LastLoanAmount;
        Obj.PrevMaxLoanAmount = PrevMaxLoanAmount;
        Obj.HomeLoanAmount = HomeLoanAmount;
        Obj.LoanCycle = LoanCycle;
        Obj.MaritalStatusID = MaritalStatusID;
        Obj.OwnHouse = OwnHouse;
        Obj.CreditCheck = CreditCheck;
        Obj.VisitDate = VisitDate;
        Obj.LastVisitDetailsID = LastVisitDetailsID;
        Obj.OccupationID = OccupationID;
        Obj.GuarantorName = GuarantorName;
        Obj.GuarantorRelationID = GuarantorRelationID;

        Obj.ActualVisitDate = ActualVisitDate;
        Obj.VisitDetailsID = VisitDetailsID;
        Obj.RevisitMemberStatus = RevisitMemberStatus;
        Obj.CBTotalLoanBalance = CBTotalLoanBalance;
        Obj.CBNoOfActiveLoans = CBNoOfActiveLoans;
        Obj.CBScore = CBScore;
        Obj.CBRemark = CBRemark;
        Obj.EnquiryDate = EnquiryDate;
        Obj.LastLoanStatus = LastLoanStatus;
        Obj.Single = Single;
        Obj.LoanReasonID = LoanReasonID;
        Obj.filenamePhoto = filenamePhoto;

        Obj.Edited = Edited;
        Obj.LastCycleGroupContinued = LastCycleGroupContinued;
        Obj.MaturityDate = MaturityDate;
        Obj.LongMaturityDate = LongMaturityDate;
        Obj.BalanceAmount = BalanceAmount;
        Obj.OverdueAmount = OverdueAmount;
        Obj.LastCenterID = LastCenterID;
        Obj.LastGroupID = LastGroupID;
//bmp 14-Nov-22
        Obj.TargetComMeetingDate = TargetComMeetingDate;
        Obj.TargetLoanDisbursementDate = TargetLoanDisbursementDate;
//bmp 14-Nov-22
        Obj.incentiveGenuineBTypeGroup = incentiveGenuineBTypeGroup;
        Obj.incentiveRegularLoanRepay = incentiveRegularLoanRepay;
        Obj.incentiveNotWithdrawingSavingsShares = incentiveNotWithdrawingSavingsShares;
        Obj.incentiveRegularRDpay = incentiveRegularRDpay;
        Obj.incentiveRegularADHPpay = incentiveRegularADHPpay;
        Obj.incentiveAGMAttendance = incentiveAGMAttendance;
//bmp 11-Jan-24
        Obj.incentiveCommunityMeetingNoAttendance = incentiveCommunityMeetingNoAttendance;
        Obj.incentiveCommunityMeetingPartAttendance = incentiveCommunityMeetingPartAttendance;
        Obj.incentiveCommunityMeetingFullAttendance = incentiveCommunityMeetingFullAttendance;
        Obj.NextLoanOfferAmount = NextLoanOfferAmount;
        Obj.flagNewMember = flagNewMember;
        Obj.incentiveGenuineGradeGroup = incentiveGenuineGradeGroup;
        Obj.incentiveLowPrevMaxLoanAmount = incentiveLowPrevMaxLoanAmount;
//bmp 11-Jan-24
//bmp 28-Aug-25
        Obj.UpFrontJLGBalance = UpFrontJLGBalance;
        Obj.CurrentJLGBalance = CurrentJLGBalance;
//bmp 28-Aug-25
    }
}
