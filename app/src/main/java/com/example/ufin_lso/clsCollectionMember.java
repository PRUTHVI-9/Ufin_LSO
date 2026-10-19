package com.example.ufin_lso;

public class clsCollectionMember {
    public static final int COLLECTION_MEMBER_STATUS_INIT                = 0;
    public static final int COLLECTION_MEMBER_STATUS_SCHEDULED           = 1;
    public static final int COLLECTION_MEMBER_STATUS_PARTIAL             = 2;
    public static final int COLLECTION_MEMBER_STATUS_FULL                = 3;
//bmp 01-Jul-26
////bmp 16-Jun-26
//    public static final int COLLECTION_MEMBER_STATUS_VALIDATION_PENDING  = 10;
//    public static final int COLLECTION_MEMBER_STATUS_QUERY_OFFSET        = 20;
////    public static final int COLLECTION_MEMBER_STATUS_QUERY_SUBMITTED_OFFSET = 30;
////bmp 16-Jun-26
//bmp 01-Jul-26

    String BranchName;
    long ClusterID;
    String ClusterName;
    long CommunityID;
    String CommunityName;
    long CenterID;
//    String CenterName;
    long GroupID;
//    String GroupName;
    long MemberID;
    long TransactionTimestamp;
    int TransactionStatus;
    String TransactionDateTime;
    String TransactionType;
    String MemberName;
    String SavingAccNo;
    String PhoneNo;
//    String PreClosureReason;
    int PreClosureReasonID;
    String CopyPhoneNo;
    boolean VerifiedPhoneNo;
    int ConfirmUnCollected;
    int MemberStatus;
    long LoanNo;
    int LoanInstallmentNo;
    String RecoveryDate;
    String RecoveryTime;
    String RecoveryDateTime;
    String StdRecoveryDay;
    String StdRecoveryTime;
    String CollectionMemberName;
    String MaturityDate;
    String Internal_Transfer_Reason;
    int TotalDueAmountsToPayRefday;
    int TotalDueAmountsToPayToday;

    int LoanPastDueAmountTillRefday;
//bmp 12-May-23
    int CopyLoanPastDueAmountTillRefday;
//bmp 12-May-23
    int LoanPastDueAmountTillToday;
//    int CopyLoanPastDueAmountTillToday;

    int LoanCurrentDueAmountRefday;
//bmp 12-May-23
    int CopyLoanCurrentDueAmountRefday;
//bmp 12-May-23
    int LoanCurrentDueAmountToday;
//    int CopyLoanCurrentDueAmountToday;

    int LoanPastDuePlusCurrentDueAmtRefday;
//bmp 12-May-23
    int CopyLoanPastDuePlusCurrentDueAmtRefday;
//bmp 12-May-23
    int LoanPastDuePlusCurrentDueAmtToday;
//    int CopyLoanPastDuePlusCurrentDueAmtToday;

    int ADHPPastDueAmountTillToday;
    int ADHPContributionAmount;
    int Monthly_ADHP;
    int ADHPTotalExpectedAndPastDueAmt;
    int CopyADHPPastDueAmountTillToday;
    int CopyADHPContributionAmount;
    int CopyADHPTotalExpectedAndPastDueAmt;

//    int TotalRDCurrentDueAndExpectedAmt;
    int TotalRDCount;

    long RD1DetailsID;
    int RD1PastDue;
    int RD1CurrentInstallmentAmount;
    int RD1ExpectedAmount;
//bmp 21-Dec-23
    int RD1MonthlyInstallmentAmt;
//bmp 21-Dec-23
    int RD1CollectionAmount;
    boolean RD1Select;

    long RD2DetailsID;
    int RD2PastDue;
    int RD2CurrentInstallmentAmount;
    int RD2ExpectedAmount;
    //bmp 21-Dec-23
    int RD2MonthlyInstallmentAmt;
    //bmp 21-Dec-23
    int RD2CollectionAmount;
    boolean RD2Select;

    long RD3DetailsID;
    int RD3PastDue;
    int RD3CurrentInstallmentAmount;
    int RD3ExpectedAmount;
    //bmp 21-Dec-23
    int RD3MonthlyInstallmentAmt;
    //bmp 21-Dec-23
    int RD3CollectionAmount;
    boolean RD3Select;

    long RD4DetailsID;
    int RD4PastDue;
    int RD4CurrentInstallmentAmount;
    int RD4ExpectedAmount;
    //bmp 21-Dec-23
    int RD4MonthlyInstallmentAmt;
    //bmp 21-Dec-23
    int RD4CollectionAmount;
    boolean RD4Select;

    long RD5DetailsID;
    int RD5PastDue;
    int RD5CurrentInstallmentAmount;
    int RD5ExpectedAmount;
    //bmp 21-Dec-23
    int RD5MonthlyInstallmentAmt;
    //bmp 21-Dec-23
    int RD5CollectionAmount;
    boolean RD5Select;
//bmp 05-Aug-26
    int JLGCollectionAmount;
    boolean JLGSelect;
    int NidhiCollectionAmount;
    boolean NidhiSelect;
    int ADHPCollectionAmount;
    boolean ADHPSelect;
//    boolean FLMFClaim;
//bmp 05-Aug-26

//    String LastRecoveryDate;
//    int OnetimeClosureWaveoffAmount;
//    int OnetimeClosureDueAmount;
//    int CopyOnetimeClosureWaveoffAmount;
//    int CopyOnetimeClosureDueAmount;
    int AdditionalRepayCollectionAmount;
    boolean AdditionalRepaySelect;
    int ExtraADHPCollectionAmount;
    int ExtraADHPAmountCollected;
    boolean ExtraADHPSelect;
//    int ExtraOnetimeClosureCollectionAmount;
//    boolean ExtraOnetimeClosureSelect;
    int NewRDCollectionAmount;
    boolean NewRDSelect;
    int ExtraJLGCollectionAmount;
    boolean ExtraJLGSelect;
//    int PolicyRenewalHMFCollectionAmount;
//    boolean PolicyRenewalHMFSelect;
//    int PolicyRenewalFLMFCollectionAmount;
//    boolean PolicyRenewalFLMFSelect;
//    int PolicyRenewalAssetLossCollectionAmount;
//    boolean PolicyRenewalAssetLossSelect;
    int ReprintChargesCollectionAmount;
    boolean ReprintChargesSelect;
    int FDCollectionAmount;
    boolean FDSelect;

    int TotalCollectionAmount;
//bmp 29-Apr-26
//    int CashCollectionAmount;
//    int OnlineCollectionAmount;
//bmp 29-Apr-26
    int TotalEMICollectionAmount;
    int CollectedPrincipal;
    int CollectedInterest;
    int CollectedLateCharges;
    int CollectedJLG;
    int CollectedPolicyRenewalAmt;
//bmp 04-May-23
    int CollectedPastPrincipal;
    int CollectedPastInterest;
    int CollectedPastLateCharges;
    int CollectedPastJLG;
    int CollectedPastPolicyRenewalAmt;
    int CollectedCurrentPrincipal;
    int CollectedCurrentInterest;
    int CollectedCurrentLateCharges;
    int CollectedCurrentJLG;
    int CollectedCurrentPolicyRenewalAmt;
//bmp 04-May-23
    int TotalADHPCollectionAmount;
    long ADHPDetailsID;
    int TotalRDCollectionAmount;
    int ExtraCollectionAmount;
    String strExtraCollectionReason;
//bmp 02-Feb-23
    String EMIReceiptNumber;
    String LastEMIReceiptNumber;
    String LastExtraReceiptNumber;

    int PastDue_Interest;
    int PastDue_LatePaymentCharges;
    int PastDue_Penalty;
    int PastDue_Principal;
    int PastDue_SD;
    int PastDue_PolicyRenewalAmt;
    int Expected_Interest;
    int Expected_LatePaymentCharges;
    int Expected_Penalty;
    int Expected_Principal;
    int Expected_SD;
    int Expected_PolicyRenewalAmt;
//    int TotalMonthlyInstallmentAmount;
    int YearlyInterest;
    int InterestTypeID;

    String ADHPReceiptNumber;
    String LastADHPReceiptNumber;
    String ADHPMembershipNumber;
    int ContributionNo;
//bmp 02-Feb-23
    int TotalOutstandingAmount;
    int PendingPrincipal;
//bmp 27-Mar-23
    String ReferenceRecoveryDate;
//bmp 27-Mar-23
    int Monthly_Interest;
    int Monthly_Principal;
    int Monthly_SD;
    int Monthly_PolicyRenewalAmt;
    String PreClosureRequestDate;
    int PreClosureRequestStatus;
    int PreClosureMonths;
    int PreClosurePrincipal;
    int PrePaymentPrincipalCollected;
    int PreClosureInterest;
    int PreClosureLatePayCharge;
    int PreClosureJLG;
    int PreClosurePolicyRenewalAmt;
    int PreClosureADHP;
    int PreClosureRD;
    int PreClosureAmount;
    int PrePaymentReqMonths;
    int PrePaymentReqAmount;
    int PrePaymentApprovedMonths;
    int PrePaymentApprovedAmount;
    int PrePaymentORPreClosure;
    int StandardEMI;
//    String PreClosureRejectReason;
    int PreClosureRejectReasonID;

    String strPaymentMode;
    long OnlinePaymentByMemberID;
    String strOnlinePaymentByName;
    long OnlinePaymentByRelationID;
    String strOnlinePaymentProof;
//    long MemberDocumentRcvdID;
//    long OnlinePaymentAppID;
//    String strOnlinePaymentApp;
    String strOnlineTransactionID;
    String strOnlineTransactionDate;
//    String strScreenShotDate;
    int iOnlineTransactionAmount;
    String strOnlinePaymentUPIID;

    public void InitCollectionMember() {
        BranchName = "";
        ClusterID = 0;
        ClusterName = "";
        CommunityID = 0;
        CommunityName = "";
        CenterID = 0;
//        CenterName = "";
        GroupID = 0;
//        GroupName = "";
        MemberID = 0;
        TransactionTimestamp = 0;
        TransactionStatus = 0;
        TransactionDateTime = "";
        TransactionType = "";
        MemberName = "";
        SavingAccNo = "";
        PhoneNo = "";
//        PreClosureReason = "";
        PreClosureReasonID = 0;
        CopyPhoneNo = "";
        VerifiedPhoneNo = false;
        ConfirmUnCollected = 0;
        MemberStatus = 0;
        LoanNo = 0;
        LoanInstallmentNo = 0;
        RecoveryDate = "";
        MaturityDate = "";
        Internal_Transfer_Reason = "";
        RecoveryTime = "";
        RecoveryDateTime = "";
//        LastRecoveryDate = "";
        StdRecoveryDay = "";
        StdRecoveryTime = "";
        CollectionMemberName = "";

        TotalDueAmountsToPayRefday = 0;
        TotalDueAmountsToPayToday = 0;

        LoanPastDueAmountTillRefday = 0;
//bmp 12-May-23
        CopyLoanPastDueAmountTillRefday = 0;
//bmp 12-May-23
        LoanPastDueAmountTillToday = 0;
//        CopyLoanPastDueAmountTillToday = 0;

        LoanCurrentDueAmountRefday = 0;
//bmp 12-May-23
        CopyLoanCurrentDueAmountRefday = 0;
//bmp 12-May-23
        LoanCurrentDueAmountToday = 0;
//        CopyLoanCurrentDueAmountToday = 0;

        LoanPastDuePlusCurrentDueAmtRefday = 0;
//bmp 12-May-23
        CopyLoanPastDuePlusCurrentDueAmtRefday = 0;
//bmp 12-May-23
        LoanPastDuePlusCurrentDueAmtToday = 0;
//        CopyLoanPastDuePlusCurrentDueAmtToday = 0;

        ADHPPastDueAmountTillToday = 0;
        ADHPContributionAmount = 0;
        Monthly_ADHP = 0;
        ADHPTotalExpectedAndPastDueAmt = 0;
        CopyADHPPastDueAmountTillToday = 0;
        CopyADHPContributionAmount = 0;
        CopyADHPTotalExpectedAndPastDueAmt = 0;
        ADHPDetailsID = 0;
//        TotalRDCurrentDueAndExpectedAmt = 0;

        TotalCollectionAmount = 0;
//bmp 29-Apr-26
//        CashCollectionAmount = 0;
//        OnlineCollectionAmount = 0;
//bmp 29-Apr-26
        TotalEMICollectionAmount = 0;
        CollectedPrincipal = 0;
        CollectedInterest = 0;
        CollectedLateCharges = 0;
        CollectedJLG = 0;
        CollectedPolicyRenewalAmt = 0;
//bmp 04-May-23
        CollectedPastPrincipal = 0;
        CollectedPastInterest = 0;
        CollectedPastLateCharges = 0;
        CollectedPastJLG = 0;
        CollectedPastPolicyRenewalAmt = 0;
        CollectedCurrentPrincipal = 0;
        CollectedCurrentInterest = 0;
        CollectedCurrentLateCharges = 0;
        CollectedCurrentJLG = 0;
        CollectedCurrentPolicyRenewalAmt = 0;
//bmp 04-May-23

        TotalADHPCollectionAmount = 0;
        TotalRDCollectionAmount = 0;

        RD1DetailsID = 0;
        RD1PastDue = 0;
        RD1CurrentInstallmentAmount = 0;
        RD1ExpectedAmount = 0;
//bmp 21-Dec-23
        RD1MonthlyInstallmentAmt = 0;
//bmp 21-Dec-23
        RD1CollectionAmount = 0;
        RD1Select = false;

        RD2DetailsID = 0;
        RD2PastDue = 0;
        RD2CurrentInstallmentAmount = 0;
        RD2ExpectedAmount = 0;
//bmp 21-Dec-23
        RD2MonthlyInstallmentAmt = 0;
//bmp 21-Dec-23
        RD2CollectionAmount = 0;
        RD2Select = false;

        RD3DetailsID = 0;
        RD3PastDue = 0;
        RD3CurrentInstallmentAmount = 0;
        RD3ExpectedAmount = 0;
//bmp 21-Dec-23
        RD3MonthlyInstallmentAmt = 0;
//bmp 21-Dec-23
        RD3CollectionAmount = 0;
        RD3Select = false;

        RD4DetailsID = 0;
        RD4PastDue = 0;
        RD4CurrentInstallmentAmount = 0;
        RD4ExpectedAmount = 0;
//bmp 21-Dec-23
        RD4MonthlyInstallmentAmt = 0;
//bmp 21-Dec-23
        RD4CollectionAmount = 0;
        RD4Select = false;

        RD5DetailsID = 0;
        RD5PastDue = 0;
        RD5CurrentInstallmentAmount = 0;
        RD5ExpectedAmount = 0;
//bmp 21-Dec-23
        RD5MonthlyInstallmentAmt = 0;
//bmp 21-Dec-23
        RD5CollectionAmount = 0;
        RD5Select = false;
//bmp 05-Aug-26
        JLGCollectionAmount = 0;
        JLGSelect = false;
        NidhiCollectionAmount = 0;
        NidhiSelect = false;
//        ADHPCollectionAmount = 0;
        ADHPSelect = false;
//        FLMFClaim = false;
//bmp 05-Aug-26

        AdditionalRepayCollectionAmount = 0;
        AdditionalRepaySelect = false;

//        OnetimeClosureDueAmount = 0;
//        OnetimeClosureWaveoffAmount = 0;
//        CopyOnetimeClosureDueAmount = 0;
//        CopyOnetimeClosureWaveoffAmount = 0;
        ExtraADHPCollectionAmount = 0;
        ExtraADHPAmountCollected = 0;
        ExtraADHPSelect = false;
//        ExtraOnetimeClosureCollectionAmount = 0;
//        ExtraOnetimeClosureSelect = false;
        NewRDCollectionAmount = 0;
        NewRDSelect = false;
        ExtraJLGCollectionAmount = 0;
        ExtraJLGSelect = false;
//        PolicyRenewalHMFCollectionAmount = 0;
//        PolicyRenewalHMFSelect = false;
//        PolicyRenewalFLMFCollectionAmount = 0;
//        PolicyRenewalFLMFSelect = false;
//        PolicyRenewalAssetLossCollectionAmount = 0;
//        PolicyRenewalAssetLossSelect = false;
        ReprintChargesCollectionAmount = 0;
        ReprintChargesSelect = false;
        FDCollectionAmount = 0;
        FDSelect = false;

        ExtraCollectionAmount = 0;
        strExtraCollectionReason = "NA";
        TotalRDCount = 0;
//bmp 02-Feb-23
        EMIReceiptNumber = "";
        LastEMIReceiptNumber = "";
        LastExtraReceiptNumber = "";
        PastDue_Interest = 0;
        PastDue_LatePaymentCharges = 0;
        PastDue_Penalty = 0;
        PastDue_Principal = 0;
        PastDue_SD = 0;
        PastDue_PolicyRenewalAmt = 0;
        Expected_Interest = 0;
        Expected_LatePaymentCharges = 0;
        Expected_Penalty = 0;
        Expected_Principal = 0;
        Expected_SD = 0;
        Expected_PolicyRenewalAmt = 0;
//        TotalMonthlyInstallmentAmount = 0;
        YearlyInterest = 0;
        InterestTypeID = 1;

        ADHPReceiptNumber = "";
        LastADHPReceiptNumber = "";
        ADHPMembershipNumber = "";
        ContributionNo = 0;
//bmp 02-Feb-23
        TotalOutstandingAmount = 0;
        PendingPrincipal = 0;
        ReferenceRecoveryDate = "";

        Monthly_Interest = 0;
        Monthly_Principal = 0;
        Monthly_SD = 0;
        Monthly_PolicyRenewalAmt = 0;

        PreClosureRequestDate = "";
        PreClosureRequestStatus = -1;
        PreClosureMonths = 0;
        PreClosurePrincipal = 0;
        PrePaymentPrincipalCollected = 0;
        PreClosureInterest = 0;
        PreClosureLatePayCharge = 0;
        PreClosureJLG = 0;
        PreClosurePolicyRenewalAmt = 0;
        PreClosureADHP = 0;
        PreClosureRD = 0;
        PreClosureAmount = 0;
        PrePaymentReqMonths = 0;
        PrePaymentReqAmount = 0;
        PrePaymentApprovedMonths = 0;
        PrePaymentApprovedAmount = 0;
        PrePaymentORPreClosure = 0;
        StandardEMI = 0;
//        PreClosureRejectReason = "";
        PreClosureRejectReasonID = 0;

        strPaymentMode = "Cash";
        OnlinePaymentByMemberID = 0L;
        strOnlinePaymentByName = "";
        OnlinePaymentByRelationID = 0L;
        strOnlinePaymentProof = "";
//        MemberDocumentRcvdID = 0;
//        strOnlinePaymentApp = "";
//        OnlinePaymentAppID = 0L;
        strOnlineTransactionID = "";
        strOnlinePaymentUPIID = "";
        strOnlineTransactionDate = "";
//        strScreenShotDate = "";
        iOnlineTransactionAmount = 0;
    }
    public void Copy_CollectionMember(clsCollectionMember Obj) {
        Obj.BranchName = BranchName;
        Obj.ClusterID = ClusterID;
        Obj.ClusterName = ClusterName;
        Obj.CommunityID = CommunityID;
        Obj.CommunityName = CommunityName;
        Obj.CenterID = CenterID;
//        Obj.CenterName = CenterName;
        Obj.GroupID = GroupID;
//        Obj.GroupName = GroupName;
        Obj.RecoveryDate = RecoveryDate;
        Obj.MaturityDate = MaturityDate;
        Obj.Internal_Transfer_Reason = Internal_Transfer_Reason;
        Obj.RecoveryTime = RecoveryTime;
        Obj.RecoveryDateTime = RecoveryDateTime;
//        Obj.LastRecoveryDate = LastRecoveryDate;
        Obj.StdRecoveryDay = StdRecoveryDay;
        Obj.StdRecoveryTime = StdRecoveryTime;
        Obj.MemberID = MemberID;
        Obj.TransactionTimestamp = TransactionTimestamp;
        Obj.TransactionStatus = TransactionStatus;
        Obj.TransactionDateTime = TransactionDateTime;
        Obj.TransactionType = TransactionType;
        Obj.MemberName = MemberName;
        Obj.SavingAccNo = SavingAccNo;
        Obj.PhoneNo = PhoneNo;
//        Obj.PreClosureReason = PreClosureReason;
        Obj.PreClosureReasonID = PreClosureReasonID;
        Obj.CopyPhoneNo = CopyPhoneNo;
        Obj.VerifiedPhoneNo = VerifiedPhoneNo;
        Obj.ConfirmUnCollected = ConfirmUnCollected;
        Obj.MemberStatus = MemberStatus;
        Obj.LoanNo = LoanNo;
        Obj.LoanInstallmentNo = LoanInstallmentNo;
        Obj.CollectionMemberName = CollectionMemberName;

        Obj.TotalDueAmountsToPayRefday = TotalDueAmountsToPayRefday;
        Obj.TotalDueAmountsToPayToday = TotalDueAmountsToPayToday;

        Obj.LoanPastDueAmountTillRefday = LoanPastDueAmountTillRefday;
//bmp 12-May-23
        Obj.CopyLoanPastDueAmountTillRefday = CopyLoanPastDueAmountTillRefday;
//bmp 12-May-23
        Obj.LoanPastDueAmountTillToday = LoanPastDueAmountTillToday;
//        Obj.CopyLoanPastDueAmountTillToday = CopyLoanPastDueAmountTillToday;

        Obj.LoanCurrentDueAmountRefday = LoanCurrentDueAmountRefday;
//bmp 12-May-23
        Obj.CopyLoanCurrentDueAmountRefday = CopyLoanCurrentDueAmountRefday;
//bmp 12-May-23
        Obj.LoanCurrentDueAmountToday = LoanCurrentDueAmountToday;
//        Obj.CopyLoanCurrentDueAmountToday = CopyLoanCurrentDueAmountToday;

        Obj.LoanPastDuePlusCurrentDueAmtRefday = LoanPastDuePlusCurrentDueAmtRefday;
//bmp 12-May-23
        Obj.CopyLoanPastDuePlusCurrentDueAmtRefday = CopyLoanPastDuePlusCurrentDueAmtRefday;
//bmp 12-May-23
        Obj.LoanPastDuePlusCurrentDueAmtToday = LoanPastDuePlusCurrentDueAmtToday;
//        Obj.CopyLoanPastDuePlusCurrentDueAmtToday = CopyLoanPastDuePlusCurrentDueAmtToday;

        Obj.ADHPPastDueAmountTillToday = ADHPPastDueAmountTillToday;
        Obj.ADHPContributionAmount = ADHPContributionAmount;
        Obj.Monthly_ADHP = Monthly_ADHP;
        Obj.ADHPTotalExpectedAndPastDueAmt = ADHPTotalExpectedAndPastDueAmt;
        Obj.CopyADHPPastDueAmountTillToday = CopyADHPPastDueAmountTillToday;
        Obj.CopyADHPContributionAmount = CopyADHPContributionAmount;
        Obj.CopyADHPTotalExpectedAndPastDueAmt = CopyADHPTotalExpectedAndPastDueAmt;

//        Obj.TotalRDCurrentDueAndExpectedAmt = TotalRDCurrentDueAndExpectedAmt;

        Obj.TotalCollectionAmount = TotalCollectionAmount;
//bmp 29-Apr-26
//        Obj.CashCollectionAmount = CashCollectionAmount;
//        Obj.OnlineCollectionAmount = OnlineCollectionAmount;
//bmp 29-Apr-26
        Obj.TotalEMICollectionAmount = TotalEMICollectionAmount;
        Obj.CollectedPrincipal = CollectedPrincipal;
        Obj.CollectedInterest = CollectedInterest;
        Obj.CollectedLateCharges = CollectedLateCharges;
        Obj.CollectedJLG = CollectedJLG;
        Obj.CollectedPolicyRenewalAmt = CollectedPolicyRenewalAmt;
//bmp 04-May-23
        Obj.CollectedPastPrincipal = CollectedPastPrincipal;
        Obj.CollectedPastInterest = CollectedPastInterest;
        Obj.CollectedPastLateCharges = CollectedPastLateCharges;
        Obj.CollectedPastJLG = CollectedPastJLG;
        Obj.CollectedPastPolicyRenewalAmt = CollectedPastPolicyRenewalAmt;
        Obj.CollectedCurrentPrincipal = CollectedCurrentPrincipal;
        Obj.CollectedCurrentInterest = CollectedCurrentInterest;
        Obj.CollectedCurrentLateCharges = CollectedCurrentLateCharges;
        Obj.CollectedCurrentJLG = CollectedCurrentJLG;
        Obj.CollectedCurrentPolicyRenewalAmt = CollectedCurrentPolicyRenewalAmt;
//bmp 04-May-23

        Obj.TotalADHPCollectionAmount = TotalADHPCollectionAmount;
        Obj.ADHPDetailsID = ADHPDetailsID;
        Obj.TotalRDCollectionAmount = TotalRDCollectionAmount;

        Obj.RD1DetailsID = RD1DetailsID;
        Obj.RD1PastDue = RD1PastDue;
        Obj.RD1CurrentInstallmentAmount = RD1CurrentInstallmentAmount;
        Obj.RD1ExpectedAmount = RD1ExpectedAmount;
//bmp 21-Dec-23
        Obj.RD1MonthlyInstallmentAmt = RD1MonthlyInstallmentAmt;
//bmp 21-Dec-23
        Obj.RD1CollectionAmount = RD1CollectionAmount;
        Obj.RD1Select = RD1Select;

        Obj.RD2DetailsID = RD2DetailsID;
        Obj.RD2PastDue = RD2PastDue;
        Obj.RD2CurrentInstallmentAmount = RD2CurrentInstallmentAmount;
        Obj.RD2ExpectedAmount = RD2ExpectedAmount;
//bmp 21-Dec-23
        Obj.RD2MonthlyInstallmentAmt = RD2MonthlyInstallmentAmt;
//bmp 21-Dec-23
        Obj.RD2CollectionAmount = RD2CollectionAmount;
        Obj.RD2Select = RD2Select;

        Obj.RD3DetailsID = RD3DetailsID;
        Obj.RD3PastDue = RD3PastDue;
        Obj.RD3CurrentInstallmentAmount = RD3CurrentInstallmentAmount;
        Obj.RD3ExpectedAmount = RD3ExpectedAmount;
//bmp 21-Dec-23
        Obj.RD3MonthlyInstallmentAmt = RD3MonthlyInstallmentAmt;
//bmp 21-Dec-23
        Obj.RD3CollectionAmount = RD3CollectionAmount;
        Obj.RD3Select = RD3Select;

        Obj.RD4DetailsID = RD4DetailsID;
        Obj.RD4PastDue = RD4PastDue;
        Obj.RD4CurrentInstallmentAmount = RD4CurrentInstallmentAmount;
        Obj.RD4ExpectedAmount = RD4ExpectedAmount;
//bmp 21-Dec-23
        Obj.RD4MonthlyInstallmentAmt = RD4MonthlyInstallmentAmt;
//bmp 21-Dec-23
        Obj.RD4CollectionAmount = RD4CollectionAmount;
        Obj.RD4Select = RD4Select;

        Obj.RD5DetailsID = RD5DetailsID;
        Obj.RD5PastDue = RD5PastDue;
        Obj.RD5CurrentInstallmentAmount = RD5CurrentInstallmentAmount;
        Obj.RD5ExpectedAmount = RD5ExpectedAmount;
//bmp 21-Dec-23
        Obj.RD5MonthlyInstallmentAmt = RD5MonthlyInstallmentAmt;
//bmp 21-Dec-23
        Obj.RD5CollectionAmount = RD5CollectionAmount;
        Obj.RD5Select = RD5Select;
//bmp 05-Aug-26
        Obj.JLGCollectionAmount = JLGCollectionAmount;
        Obj.JLGSelect = JLGSelect;
        Obj.NidhiCollectionAmount = NidhiCollectionAmount;
        Obj.NidhiSelect = NidhiSelect;
//        Obj.ADHPCollectionAmount = ADHPCollectionAmount;
        Obj.ADHPSelect = ADHPSelect;
//        Obj.FLMFClaim = FLMFClaim;
//bmp 05-Aug-26

        Obj.AdditionalRepayCollectionAmount = AdditionalRepayCollectionAmount;
        Obj.AdditionalRepaySelect = AdditionalRepaySelect;

//        Obj.OnetimeClosureDueAmount = OnetimeClosureDueAmount;
//        Obj.OnetimeClosureWaveoffAmount = OnetimeClosureWaveoffAmount;
//        Obj.CopyOnetimeClosureDueAmount = CopyOnetimeClosureDueAmount;
//        Obj.CopyOnetimeClosureWaveoffAmount = CopyOnetimeClosureWaveoffAmount;
        Obj.ExtraADHPCollectionAmount = ExtraADHPCollectionAmount;
        Obj.ExtraADHPAmountCollected = ExtraADHPAmountCollected;
        Obj.ExtraADHPSelect = ExtraADHPSelect;
//        Obj.ExtraOnetimeClosureCollectionAmount = ExtraOnetimeClosureCollectionAmount;
//        Obj.ExtraOnetimeClosureSelect = ExtraOnetimeClosureSelect;
        Obj.NewRDCollectionAmount = NewRDCollectionAmount;
        Obj.NewRDSelect = NewRDSelect;
        Obj.ExtraJLGCollectionAmount = ExtraJLGCollectionAmount;
        Obj.ExtraJLGSelect = ExtraJLGSelect;
//        Obj.PolicyRenewalHMFCollectionAmount = PolicyRenewalHMFCollectionAmount;
//        Obj.PolicyRenewalHMFSelect = PolicyRenewalHMFSelect;
//        Obj.PolicyRenewalFLMFCollectionAmount = PolicyRenewalFLMFCollectionAmount;
//        Obj.PolicyRenewalFLMFSelect = PolicyRenewalFLMFSelect;
//        Obj.PolicyRenewalAssetLossCollectionAmount = PolicyRenewalAssetLossCollectionAmount;
//        Obj.PolicyRenewalAssetLossSelect = PolicyRenewalAssetLossSelect;
        Obj.ReprintChargesCollectionAmount = ReprintChargesCollectionAmount;
        Obj.ReprintChargesSelect = ReprintChargesSelect;
        Obj.FDCollectionAmount = FDCollectionAmount;
        Obj.FDSelect = FDSelect;

        Obj.ExtraCollectionAmount = ExtraCollectionAmount;
        Obj.strExtraCollectionReason = strExtraCollectionReason;

        Obj.TotalRDCount = TotalRDCount;
//bmp 02-Feb-23
        Obj.EMIReceiptNumber = EMIReceiptNumber;
        Obj.LastEMIReceiptNumber = LastEMIReceiptNumber;
        Obj.LastExtraReceiptNumber = LastExtraReceiptNumber;
        Obj.PastDue_Interest = PastDue_Interest;
        Obj.PastDue_LatePaymentCharges = PastDue_LatePaymentCharges;
        Obj.PastDue_Penalty = PastDue_Penalty;
        Obj.PastDue_Principal = PastDue_Principal;
        Obj.PastDue_SD = PastDue_SD;
        Obj.PastDue_PolicyRenewalAmt = PastDue_PolicyRenewalAmt;
        Obj.Expected_Interest = Expected_Interest;
        Obj.Expected_LatePaymentCharges = Expected_LatePaymentCharges;
        Obj.Expected_Penalty = Expected_Penalty;
        Obj.Expected_Principal = Expected_Principal;
        Obj.Expected_SD = Expected_SD;
        Obj.Expected_PolicyRenewalAmt = Expected_PolicyRenewalAmt;
//        Obj.TotalMonthlyInstallmentAmount = TotalMonthlyInstallmentAmount;
        Obj.YearlyInterest = YearlyInterest;
        Obj.InterestTypeID = InterestTypeID;

        Obj.ADHPReceiptNumber = ADHPReceiptNumber;
        Obj.LastADHPReceiptNumber = LastADHPReceiptNumber;
        Obj.ADHPMembershipNumber = ADHPMembershipNumber;
        Obj.ContributionNo = ContributionNo;
//bmp 02-Feb-23
        Obj.TotalOutstandingAmount = TotalOutstandingAmount;
        Obj.PendingPrincipal = PendingPrincipal;
        Obj.ReferenceRecoveryDate = ReferenceRecoveryDate;

        Obj.Monthly_Interest = Monthly_Interest;
        Obj.Monthly_Principal = Monthly_Principal;
        Obj.Monthly_SD = Monthly_SD;
        Obj.Monthly_PolicyRenewalAmt = Monthly_PolicyRenewalAmt;

        Obj.PreClosureRequestDate = PreClosureRequestDate;
        Obj.PreClosureRequestStatus = PreClosureRequestStatus;
        Obj.PreClosureMonths = PreClosureMonths;
        Obj.PreClosurePrincipal = PreClosurePrincipal;
        Obj.PrePaymentPrincipalCollected = PrePaymentPrincipalCollected;
        Obj.PreClosureInterest = PreClosureInterest;
        Obj.PreClosureLatePayCharge = PreClosureLatePayCharge;
        Obj.PreClosureJLG = PreClosureJLG;
        Obj.PreClosurePolicyRenewalAmt = PreClosurePolicyRenewalAmt;
        Obj.PreClosureADHP = PreClosureADHP;
        Obj.PreClosureRD = PreClosureRD;
        Obj.PreClosureAmount = PreClosureAmount;
        Obj.PrePaymentReqMonths = PrePaymentReqMonths;
        Obj.PrePaymentReqAmount = PrePaymentReqAmount;
        Obj.PrePaymentApprovedMonths = PrePaymentApprovedMonths;
        Obj.PrePaymentApprovedAmount = PrePaymentApprovedAmount;
        Obj.PrePaymentORPreClosure = PrePaymentORPreClosure;
        Obj.StandardEMI = StandardEMI;
//        Obj.PreClosureRejectReason = PreClosureRejectReason;
        Obj.PreClosureRejectReasonID = PreClosureRejectReasonID;

        Obj.strPaymentMode = strPaymentMode;
        Obj.OnlinePaymentByMemberID = OnlinePaymentByMemberID;
        Obj.strOnlinePaymentByName = strOnlinePaymentByName;
        Obj.OnlinePaymentByRelationID = OnlinePaymentByRelationID;
        Obj.strOnlinePaymentProof = strOnlinePaymentProof;
//        Obj.MemberDocumentRcvdID = MemberDocumentRcvdID;
//        Obj.strOnlinePaymentApp = strOnlinePaymentApp;
//        Obj.OnlinePaymentAppID = OnlinePaymentAppID;
        Obj.strOnlineTransactionID = strOnlineTransactionID;
        Obj.strOnlinePaymentUPIID = strOnlinePaymentUPIID;
        Obj.strOnlineTransactionDate = strOnlineTransactionDate;
//        Obj.strScreenShotDate = strScreenShotDate;
        Obj.iOnlineTransactionAmount = iOnlineTransactionAmount;
    }
}
