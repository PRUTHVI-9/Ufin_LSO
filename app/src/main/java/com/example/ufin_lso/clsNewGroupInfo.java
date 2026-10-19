package com.example.ufin_lso;

//clsNewGroupInfo <=> TD_NewGroup_Info SQLite DB Table
public class clsNewGroupInfo {
    long GroupID;
    int GroupStatus;
    String LoanDisbursementDate;
    String LoanDisbursementTime;
    String LoanRepayInstallmentDate;
    String LoanRepayInstallmentTime;
    String PIFDate;
    String PIFTime;
    String GroupTestDate;
    String GroupTestTime;
    long CollectionCenterID;
    long CollectionMemberID;
    String GradeOfGroup;
//bmp 10-Aug-26
    int MemberCount;
//bmp 10-Aug-26
    void Init_NewGroupInfo() {
        GroupID = 0;
        GroupStatus = 0;
//bmp 09-Sep-25
        LoanDisbursementDate = "2010-01-01";
        LoanDisbursementTime = "00:00";
        LoanRepayInstallmentDate = "2010-01-01";
        LoanRepayInstallmentTime = "00:00";
//        LoanDisbursementDate = "";
//        LoanDisbursementTime = "";
//        LoanRepayInstallmentDate = "";
//        LoanRepayInstallmentTime = "";
//bmp 09-Sep-25
        GroupTestDate = "2010-01-01";
        GroupTestTime = "00:00";
        PIFDate = "2010-01-01";
        PIFTime = "00:00";
        CollectionCenterID = 0;
        CollectionMemberID = 0;
        GradeOfGroup = "C";
//bmp 10-Aug-26
        MemberCount = 0;
//bmp 10-Aug-26
    }
    public boolean Compare_NewGroupInfo(clsNewGroupInfo Obj) {
        if(this.GroupID != Obj.GroupID)
            return false;
        if(this.GroupStatus != Obj.GroupStatus)
            return false;
        if(!this.LoanDisbursementDate.equalsIgnoreCase(Obj.LoanDisbursementDate))
            return false;
        if(!this.LoanDisbursementTime.equalsIgnoreCase(Obj.LoanDisbursementTime))
            return false;
        if(!this.LoanRepayInstallmentDate.equalsIgnoreCase(Obj.LoanRepayInstallmentDate))
            return false;
        if(!this.LoanRepayInstallmentTime.equalsIgnoreCase(Obj.LoanRepayInstallmentTime))
            return false;
        if(!this.GroupTestDate.equalsIgnoreCase(Obj.GroupTestDate))
            return false;
        if(!this.GroupTestTime.equalsIgnoreCase(Obj.GroupTestTime))
            return false;
        if(!this.PIFDate.equalsIgnoreCase(Obj.PIFDate))
            return false;
        if(!this.PIFTime.equalsIgnoreCase(Obj.PIFTime))
            return false;
        if(this.CollectionCenterID != Obj.CollectionCenterID)
            return false;
        if(this.CollectionMemberID != Obj.CollectionMemberID)
            return false;
        if(!this.GradeOfGroup.equalsIgnoreCase(Obj.GradeOfGroup))
            return false;
//bmp 10-Aug-26
        if(this.MemberCount != Obj.MemberCount)
            return false;
//bmp 10-Aug-26

        return true;
    }
    public void Copy_NewGroupInfo(clsNewGroupInfo Obj) {
        Obj.GroupID = GroupID;
        Obj.GroupStatus = GroupStatus;
        Obj.LoanDisbursementDate = LoanDisbursementDate;
        Obj.LoanDisbursementTime = LoanDisbursementTime;
        Obj.LoanRepayInstallmentDate = LoanRepayInstallmentDate;
        Obj.LoanRepayInstallmentTime = LoanRepayInstallmentTime;
        Obj.GroupTestDate = GroupTestDate;
        Obj.GroupTestTime = GroupTestTime;
        Obj.PIFDate = PIFDate;
        Obj.PIFTime = PIFTime;
        Obj.CollectionCenterID = CollectionCenterID;
        Obj.CollectionMemberID = CollectionMemberID;
        Obj.GradeOfGroup = GradeOfGroup;
//bmp 10-Aug-26
        Obj.MemberCount = MemberCount;
//bmp 10-Aug-26
    }
}
