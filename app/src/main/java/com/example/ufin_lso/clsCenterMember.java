package com.example.ufin_lso;

import java.util.ArrayList;

public class clsCenterMember {
    public static final int CENTER_MEMBER_STATUS_INIT                = 0;
    public static final int CENTER_MEMBER_STATUS_SCHEDULED           = 1;
    public static final int CENTER_MEMBER_STATUS_PARTIAL             = 2;
    public static final int CENTER_MEMBER_STATUS_FULL                = 3;

    clsCollectionMember collectionMember = new clsCollectionMember();
    ArrayList<clsRDReceipt> memberRDReceipt = new ArrayList<clsRDReceipt>();
    int indexCollectionMemberList;
    int index1RDReceipts;
    int index2RDReceipts;
    int index3RDReceipts;
    int index4RDReceipts;
    int index5RDReceipts;
    int index6RDReceipts;
    int index7RDReceipts;
    int index8RDReceipts;
    int index9RDReceipts;
    int index10RDReceipts;
    int index11RDReceipts;
    int index12RDReceipts;

    public void InitCenterMember() {
        collectionMember.InitCollectionMember();
        memberRDReceipt.clear();
        indexCollectionMemberList = -1;
        index1RDReceipts = -1;
        index2RDReceipts = -1;
        index3RDReceipts = -1;
        index4RDReceipts = -1;
        index5RDReceipts = -1;
        index6RDReceipts = -1;
        index7RDReceipts = -1;
        index8RDReceipts = -1;
        index9RDReceipts = -1;
        index10RDReceipts = -1;
        index11RDReceipts = -1;
        index12RDReceipts = -1;
    }
    public void Copy_CenterMember(clsCenterMember Obj) {
        collectionMember.Copy_CollectionMember(Obj.collectionMember);
        Obj.memberRDReceipt.clear();
        for(int i=0; i<memberRDReceipt.size(); i++) {
            clsRDReceipt rdReceipt = new clsRDReceipt();
            rdReceipt.InitRDReceipt();
            memberRDReceipt.get(i).Copy_RDReceipt(rdReceipt);
            Obj.memberRDReceipt.add(rdReceipt);
        }
        Obj.indexCollectionMemberList = indexCollectionMemberList;
        Obj.index1RDReceipts = index1RDReceipts;
        Obj.index2RDReceipts = index2RDReceipts;
        Obj.index3RDReceipts = index3RDReceipts;
        Obj.index4RDReceipts = index4RDReceipts;
        Obj.index5RDReceipts = index5RDReceipts;
        Obj.index6RDReceipts = index6RDReceipts;
        Obj.index7RDReceipts = index7RDReceipts;
        Obj.index8RDReceipts = index8RDReceipts;
        Obj.index9RDReceipts = index9RDReceipts;
        Obj.index10RDReceipts = index10RDReceipts;
        Obj.index11RDReceipts = index11RDReceipts;
        Obj.index12RDReceipts = index12RDReceipts;
    }
}
