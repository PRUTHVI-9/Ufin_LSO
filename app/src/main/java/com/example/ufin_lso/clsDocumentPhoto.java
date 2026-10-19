package com.example.ufin_lso;

public class clsDocumentPhoto {
    long DocumentPhotoID;
    long MemberDocuemntsRcvdID;
    long GroupID;
    int IsActive;
//bmp 07-Apr-26
    long MemberDocumentsID;
//bmp 07-Apr-26
    void Init_DocumentPhoto() {
        DocumentPhotoID = 0;
        MemberDocuemntsRcvdID = 0;
        GroupID = 0;
        IsActive = 1;
        MemberDocumentsID = 0;
    }
    public boolean Compare_DocumentPhoto(clsDocumentPhoto Obj) {
        if (DocumentPhotoID != Obj.DocumentPhotoID)
            return false;
        if (MemberDocuemntsRcvdID != Obj.MemberDocuemntsRcvdID)
            return false;
        if (GroupID != Obj.GroupID)
            return false;
        if (MemberDocumentsID != Obj.MemberDocumentsID)
            return false;
        if (IsActive != Obj.IsActive)
            return false;

        return true;
    }
    public void Copy_DocumentPhoto(clsDocumentPhoto Obj) {
        Obj.DocumentPhotoID = DocumentPhotoID;
        Obj.MemberDocuemntsRcvdID = MemberDocuemntsRcvdID;
        Obj.GroupID = GroupID;
        Obj.MemberDocumentsID = MemberDocumentsID;
        Obj.IsActive = IsActive;
    }
}
