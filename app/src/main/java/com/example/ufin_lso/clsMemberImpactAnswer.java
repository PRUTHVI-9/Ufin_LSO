package com.example.ufin_lso;

public class clsMemberImpactAnswer {
    long MemberImpactAnswerID;
    long MemberFamilyDetailsID;
    long IPAnswerID;
    long EncodedBy;
    String ImpactEncodedDate;
    long UpdatedBy;
    String ImpactUpdatedDate;
    int IsActive;

    public void Init_MemberImpactAnswer() {
        MemberImpactAnswerID = 0;
        MemberFamilyDetailsID = 0;
        IPAnswerID = 0;
        EncodedBy = 0;
        ImpactEncodedDate = "";
        UpdatedBy = 0;
        ImpactUpdatedDate = "";
        IsActive = 1;
    }
    public boolean Compare_MemberImpactAnswer(clsMemberImpactAnswer Obj) {
        if(MemberImpactAnswerID != Obj.MemberImpactAnswerID)
            return false;
        if(MemberFamilyDetailsID != Obj.MemberFamilyDetailsID)
            return false;
        if(IPAnswerID != Obj.IPAnswerID)
            return false;
        if(EncodedBy != Obj.EncodedBy)
            return false;
        if(!ImpactEncodedDate.equalsIgnoreCase(Obj.ImpactEncodedDate))
            return false;
        if(UpdatedBy != Obj.UpdatedBy)
            return false;
        if(!ImpactUpdatedDate.equalsIgnoreCase(Obj.ImpactUpdatedDate))
            return false;
        if(IsActive != Obj.IsActive)
            return false;

        return true;
    }
    public void Copy_MemberImpactAnswer(clsMemberImpactAnswer Obj) {
        Obj.MemberImpactAnswerID = MemberImpactAnswerID;
        Obj.MemberFamilyDetailsID = MemberFamilyDetailsID;
        Obj.IPAnswerID = IPAnswerID;
        Obj.EncodedBy = EncodedBy;
        Obj.ImpactEncodedDate = ImpactEncodedDate;
        Obj.UpdatedBy = UpdatedBy;
        Obj.ImpactUpdatedDate = ImpactUpdatedDate;
        Obj.IsActive = IsActive;
    }
}
