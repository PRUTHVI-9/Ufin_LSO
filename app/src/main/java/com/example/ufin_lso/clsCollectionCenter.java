package com.example.ufin_lso;

import java.util.ArrayList;

public class clsCollectionCenter {
    public static final int COLLECTION_CENTER_STATUS_INIT                = 0;
    public static final int COLLECTION_CENTER_STATUS_SCHEDULED           = 1;
    public static final int COLLECTION_CENTER_STATUS_PARTIAL             = 2;
    public static final int COLLECTION_CENTER_STATUS_FULL                = 3;

    String BranchName;
    long ClusterID;
    String ClusterName;
    long CommunityID;
    String CommunityName;
    long CenterID;
//    String CenterName;
    int CollectionMemberCount;
    int ScheduledCollectionMemberCount;
    int PartialCollectionMemberCount;
    int CollectionStatus;
    String RecoveryDateTime;
    String RecoveryDate;
    String RecoveryTime;
    String NextRecoveryDateTime;
    long lNextRecoveryStartTime;
    long lNextRecoveryEndTime;
    String NextRecoveryDate;
    String NextRecoveryTime;
    String StdRecoveryDay;
    String StdRecoveryTime;
    String CollectionMemberName;
    int CenterDueCollectionTotal;
    int CenterCollectionTotal;
    int CenterNarationValueTotal;
    int CenterOnlineCollectionTotal;
    int CenterCashCollectionTotal;

    final ArrayList<String> CenterMemberInfoList = new ArrayList<String>();
    final ArrayList<clsCenterMember> CenterMemberList = new ArrayList<clsCenterMember>();

    public void InitCollectionCenter() {
        BranchName = "";
        ClusterID = 0;
        ClusterName = "";
        CommunityID = 0;
        CommunityName = "";
        CenterID = 0;
//        CenterName = "";
        CollectionMemberCount = 0;
        ScheduledCollectionMemberCount = 0;
        PartialCollectionMemberCount = 0;
        CollectionStatus = 0;
        RecoveryDate = "";
        RecoveryTime = "";
        RecoveryDateTime = "";
        NextRecoveryDate = "";
        NextRecoveryTime = "";
        NextRecoveryDateTime = "";
        lNextRecoveryStartTime = 0;
        lNextRecoveryEndTime = 0;
        StdRecoveryDay = "";
        StdRecoveryTime = "";
        CollectionMemberName = "";
        CenterDueCollectionTotal = 0;
        CenterCollectionTotal = 0;
        CenterNarationValueTotal = 0;
        CenterOnlineCollectionTotal = 0;
        CenterCashCollectionTotal = 0;

        CenterMemberInfoList.clear();
        CenterMemberList.clear();
    }
    public void Copy_CollectionCenter(clsCollectionCenter Obj) {
        Obj.BranchName = BranchName;
        Obj.ClusterID = ClusterID;
        Obj.ClusterName = ClusterName;
        Obj.CommunityID = CommunityID;
        Obj.CommunityName = CommunityName;
        Obj.CenterID = CenterID;
//        Obj.CenterName = CenterName;
        Obj.CollectionMemberCount = CollectionMemberCount;
        Obj.ScheduledCollectionMemberCount = ScheduledCollectionMemberCount;
        Obj.PartialCollectionMemberCount = PartialCollectionMemberCount;
        Obj.CollectionStatus = CollectionStatus;
        Obj.RecoveryDate = RecoveryDate;
        Obj.RecoveryTime = RecoveryTime;
        Obj.RecoveryDateTime = RecoveryDateTime;
        Obj.NextRecoveryDate = NextRecoveryDate;
        Obj.NextRecoveryTime = NextRecoveryTime;
        Obj.NextRecoveryDateTime = NextRecoveryDateTime;
        Obj.lNextRecoveryStartTime = lNextRecoveryStartTime;
        Obj.lNextRecoveryEndTime = lNextRecoveryEndTime;
        Obj.StdRecoveryDay = StdRecoveryDay;
        Obj.StdRecoveryTime = StdRecoveryTime;
        Obj.CollectionMemberName = CollectionMemberName;
        Obj.CenterDueCollectionTotal = CenterDueCollectionTotal;
        Obj.CenterCollectionTotal = CenterCollectionTotal;
        Obj.CenterNarationValueTotal = CenterNarationValueTotal;
        Obj.CenterOnlineCollectionTotal = CenterOnlineCollectionTotal;
        Obj.CenterCashCollectionTotal = CenterCashCollectionTotal;
    }
}
