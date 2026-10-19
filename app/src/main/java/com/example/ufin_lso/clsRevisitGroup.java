package com.example.ufin_lso;

import java.util.ArrayList;

public class clsRevisitGroup {
    public static final int REVISIT_GROUP_STATUS_INIT                = 0;
    public static final int REVISIT_GROUP_STATUS_SCHEDULED           = 1;
    public static final int REVISIT_GROUP_STATUS_REVISITED           = 2;
    public static final int REVISIT_GROUP_STATUS_REGROUPED           = 3;
    public static final int REVISIT_GROUP_STATUS_READYGROUP          = 4;
    public static final int REVISIT_GROUP_STATUS_GROUPFORM_SUBMITTED = 5;

    long ClusterID;
    String Cluster;
    long CommunityID;
    String Community;
    long CenterID;
    long GroupID;
    String GroupName;
    int ReadyForLoanGroupMemberCount;
    int OngoingLoanGroupMemberCount;
    int LMFGroupMemberCount;
//bmp 20-Jul-23
    int ZeroNewLoanAmtGroupMemberCount;
//bmp 20-Jul-23
    int MemberCount;
    String ActualVisitDate;
//    int GroupMemberRevisitMemberListIndex[] = new int[5];
    ArrayList<Integer> GroupMemberRevisitMemberListIndex = new ArrayList<Integer>();
//    long GroupMemberID[] = new long[5];
    ArrayList<Long> GroupMemberID = new ArrayList<Long>();
//    boolean MemberCreditCheck[] = new boolean[5];
    ArrayList<Boolean> MemberCreditCheck = new ArrayList<Boolean>();
    int RevisitGroupStatus;
    String PIFDate;
    String PIFTime;
    long lPIFStartTime;
    long lPIFEndTime;
    long lGRTStartTime;
    long lGRTEndTime;
    long lOrientationStartTime;
    long lOrientationEndTime;
    String GroupTestDate;
    String GroupTestTime;
    String LoanDisbursementDate;
    String LoanDisbursementTime;
    int LastCycleGroupContinued;
    long CollectionMemberID;
    long OutgoingMemberID1;
    long OutgoingMemberID2;
    String GradeOfGroup;
    long IncomingMemberGroupID;
    int flagGRTQ2a;

    public void InitRevisitGroup() {
        ClusterID = 0;
        Cluster = "";
        CommunityID = 0;
        Community = "";
        CenterID = 0;
        GroupID = 0;
        GroupName = "";
        MemberCount = 0;
        ReadyForLoanGroupMemberCount = 0;
        OngoingLoanGroupMemberCount = 0;
        LMFGroupMemberCount = 0;
        ZeroNewLoanAmtGroupMemberCount = 0;
        ActualVisitDate = "";
        GroupMemberRevisitMemberListIndex.clear();
        GroupMemberID.clear();
        MemberCreditCheck.clear();
        RevisitGroupStatus = REVISIT_GROUP_STATUS_INIT;
        PIFDate = "";
        PIFTime = "";
        lPIFStartTime = 0;
        lPIFEndTime = 0;
        lGRTStartTime = 0;
        lGRTEndTime = 0;
        lOrientationStartTime = 0;
        lOrientationEndTime = 0;
        GroupTestDate = "";
        GroupTestTime = "";
        LoanDisbursementDate = "";
        LoanDisbursementTime = "";
        LastCycleGroupContinued = 0;
        CollectionMemberID = 0;
        OutgoingMemberID1 = 0;
        OutgoingMemberID2 = 0;
        GradeOfGroup = "A";
        IncomingMemberGroupID = 0;
        flagGRTQ2a = -1;
    }
    public void Copy_RevisitGroup(clsRevisitGroup Obj) {
        Obj.ClusterID = ClusterID;
        Obj.Cluster = Cluster;
        Obj.CommunityID = CommunityID;
        Obj.Community = Community;
        Obj.CenterID = CenterID;
        Obj.GroupID = GroupID;
        Obj.GroupName = GroupName;
        Obj.MemberCount = MemberCount;
        Obj.ReadyForLoanGroupMemberCount = ReadyForLoanGroupMemberCount;
        Obj.OngoingLoanGroupMemberCount = OngoingLoanGroupMemberCount;
        Obj.LMFGroupMemberCount = LMFGroupMemberCount;
        Obj.ZeroNewLoanAmtGroupMemberCount = ZeroNewLoanAmtGroupMemberCount;
        Obj.ActualVisitDate = ActualVisitDate;
        Obj.GroupMemberRevisitMemberListIndex.clear();
        Obj.GroupMemberID.clear();
        Obj.MemberCreditCheck.clear();
        for(int i=0; i<MemberCount; i++) {
            Obj.GroupMemberRevisitMemberListIndex.add(GroupMemberRevisitMemberListIndex.get(i));
            Obj.GroupMemberID.add(GroupMemberID.get(i));
            Obj.MemberCreditCheck.add(MemberCreditCheck.get(i));
        }
        Obj.RevisitGroupStatus = RevisitGroupStatus;
        Obj.PIFDate = PIFDate;
        Obj.PIFTime = PIFTime;
        Obj.lPIFStartTime = lPIFStartTime;
        Obj.lPIFEndTime = lPIFEndTime;
        Obj.lGRTStartTime = lGRTStartTime;
        Obj.lGRTEndTime = lGRTEndTime;
        Obj.lOrientationStartTime = lOrientationStartTime;
        Obj.lOrientationEndTime = lOrientationEndTime;
        Obj.GroupTestDate = GroupTestDate;
        Obj.GroupTestTime = GroupTestTime;
        Obj.LoanDisbursementDate = LoanDisbursementDate;
        Obj.LoanDisbursementTime = LoanDisbursementTime;
        Obj.LastCycleGroupContinued = LastCycleGroupContinued;
        Obj.CollectionMemberID = CollectionMemberID;
        Obj.OutgoingMemberID1 = OutgoingMemberID1;
        Obj.OutgoingMemberID2 = OutgoingMemberID2;
        Obj.GradeOfGroup = GradeOfGroup;
        Obj.IncomingMemberGroupID = IncomingMemberGroupID;

        Obj.flagGRTQ2a = flagGRTQ2a;
    }
}
