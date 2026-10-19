package com.example.ufin_lso;

public class clsMemberDocRcvd {
    long MemberDocuemntsRcvdID;
    long MemberID;
    long MemberDocumentsID;
    String DocumentNo;
    int IsActive;
    int IsAgeProofDoc;

    void Init_MemberDocRcvd() {
        MemberDocuemntsRcvdID = 0;
        MemberID = 0;
        MemberDocumentsID = 0;
        DocumentNo = "";
        IsActive = 1;
        IsAgeProofDoc = 0;
    }
    public boolean Compare_MemberDocRcvd(clsMemberDocRcvd Obj) {
        if(MemberDocuemntsRcvdID != Obj.MemberDocuemntsRcvdID)
            return false;
        if(MemberID != Obj.MemberID)
            return false;
        if(MemberDocumentsID != Obj.MemberDocumentsID)
            return false;
        if(!DocumentNo.equalsIgnoreCase(Obj.DocumentNo))
            return false;
        if(IsActive != Obj.IsActive)
            return false;
        if(IsAgeProofDoc != Obj.IsAgeProofDoc)
            return false;

        return true;
    }
    public void Copy_MemberDocRcvd(clsMemberDocRcvd Obj) {
        Obj.MemberDocuemntsRcvdID = MemberDocuemntsRcvdID;
        Obj.MemberID = MemberID;
        Obj.MemberDocumentsID = MemberDocumentsID;
        Obj.DocumentNo = DocumentNo;
        Obj.IsActive = IsActive;
        Obj.IsAgeProofDoc = IsAgeProofDoc;
    }
}
