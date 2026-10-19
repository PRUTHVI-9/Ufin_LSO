package com.example.ufin_lso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class clsMemberData {
    public static int DELETION_REASON_ID_DUPLICATE_MEMBER_ORGID_1          = 693;
    public static int DELETION_REASON_ID_DUPLICATE_MEMBER_ORGID_2          = 668;

    long MemberID;
    String MemberNumber;
    String Fname;
    String Mname;
    String Lname;
    String BirthDate;
    String Gender;
    String JoiningDate;
    String ContactNo;
    String SavingAccNo;
    long AddressID;
    long BranchID;
    long PrevMemberID;
//    byte[] SysliftMemberID;
//    String SysliftMemberID;
    String DeathDate;
    int IsActive;
    String Title;
    int CustomerTypeID;
    long GroupID;
    String Email;
    long ReligionID;
    long CasteID;
    long CasteCategoryID;               //Local variable
    long MaritalStatusID;
    long RelationID;
    long MainMemberID;
    int FamilyHead;
    int HMFApplicable;
    long EncodedBy;
    String EncodedDate;
    long DeletionReasonID;
    long MemberFamilyDetailsID;
    long BankID;
    String BankAccountNumber;
    String Bank_Fname;
    String Bank_Mname;
    String Bank_Lname;
    int IsMaidenName;
    String MaidenORMarriedName;
    String RationCard_Fname;
    String RationCard_Mname;
    String RationCard_Lname;
    long BankBranchID;
    int GroupGuarantor;
    String DeactivateDate;

    public void InitNewMember(String strToday) {
        MemberID = 0;
        MemberNumber = "1";
        Fname = "NA";
        Mname = "NA";
        Lname = "NA";
//bmp 22-Mar-24
//        BirthDate = "2000-01-01";
        BirthDate = strToday;
//bmp 22-Mar-24
        Gender = "F";
        Title = "Ms.";
        JoiningDate = "2000-01-01";
        ContactNo = "0";
        SavingAccNo = "NA";
        AddressID = 0;
        BranchID = 0;
        PrevMemberID = 0;
//        SysliftMemberID = "NA";
        DeathDate = "NA";
        IsActive = 1;
        CustomerTypeID = 1;
        GroupID = 0;
        Email = "NA";
        ReligionID = 1;
        CasteID = 1;
        CasteCategoryID = -1;
        MaritalStatusID = 1;
        RelationID = 0;
        MainMemberID = 0;
        FamilyHead = 0;
        HMFApplicable = 1;
        EncodedBy = 0;
        EncodedDate = "2000-01-01";
        DeletionReasonID = 0;
        MemberFamilyDetailsID = 0;
        BankID = 0;
        BankAccountNumber = "NA";
        Bank_Fname = "NA";
        Bank_Mname = "NA";
        Bank_Lname = "NA";
        IsMaidenName = -1;
        MaidenORMarriedName = "NA";
        RationCard_Fname = "NA";
        RationCard_Mname = "NA";
        RationCard_Lname = "NA";
        BankBranchID = 1;
        GroupGuarantor = 0;
        DeactivateDate = "NA";
    }
    public boolean Compare_Memberdata(clsMemberData Obj) {
        if(MemberID != Obj.MemberID)
            return false;
        if(!MemberNumber.equalsIgnoreCase(Obj.MemberNumber))
            return false;
        if(!Fname.equalsIgnoreCase(Obj.Fname))
            return false;
        if(!Mname.equalsIgnoreCase(Obj.Mname))
            return false;
        if(!Lname.equalsIgnoreCase(Obj.Lname))
            return false;
        if(!BirthDate.equalsIgnoreCase(Obj.BirthDate))
            return false;
        if(!Gender.equalsIgnoreCase(Obj.Gender))
            return false;
        if(!Title.equalsIgnoreCase(Obj.Title))
            return false;
        if(!JoiningDate.equalsIgnoreCase(Obj.JoiningDate))
            return false;
        if(!ContactNo.equalsIgnoreCase(Obj.ContactNo))
            return false;
        if(!SavingAccNo.equalsIgnoreCase(Obj.SavingAccNo))
            return false;
        if(AddressID != Obj.AddressID)
            return false;
        if(BranchID != Obj.BranchID)
            return false;
        if(PrevMemberID != Obj.PrevMemberID)
            return false;
//        if(!SysliftMemberID.equalsIgnoreCase(Obj.SysliftMemberID))
//            return false;
        if(!DeathDate.equalsIgnoreCase(Obj.DeathDate))
            return false;
        if(IsActive != Obj.IsActive)
            return false;
        if(CustomerTypeID != Obj.CustomerTypeID)
            return false;
        if(GroupID != Obj.GroupID)
            return false;
        if(!Email.equalsIgnoreCase(Obj.Email))
            return false;
        if(ReligionID != Obj.ReligionID)
            return false;
        if(CasteID != Obj.CasteID)
            return false;
        if(CasteCategoryID != Obj.CasteCategoryID)
            return false;
        if(MaritalStatusID != Obj.MaritalStatusID)
            return false;
        if(RelationID != Obj.RelationID)
            return false;
        if(MainMemberID != Obj.MainMemberID)
            return false;
        if(FamilyHead != Obj.FamilyHead)
            return false;
        if(HMFApplicable != Obj.HMFApplicable)
            return false;
        if(EncodedBy != Obj.EncodedBy)
            return false;
        if(!EncodedDate.equalsIgnoreCase(Obj.EncodedDate))
            return false;
        if(DeletionReasonID != Obj.DeletionReasonID)
            return false;
        if(MemberFamilyDetailsID != Obj.MemberFamilyDetailsID)
            return false;
        if(BankID != Obj.BankID)
            return false;
        if(!BankAccountNumber.equalsIgnoreCase(Obj.BankAccountNumber))
            return false;
        if(!Bank_Fname.equalsIgnoreCase(Obj.Bank_Fname))
            return false;
        if(!Bank_Mname.equalsIgnoreCase(Obj.Bank_Mname))
            return false;
        if(!Bank_Lname.equalsIgnoreCase(Obj.Bank_Lname))
            return false;
        if(IsMaidenName != Obj.IsMaidenName)
            return false;
        if(!MaidenORMarriedName.equalsIgnoreCase(Obj.MaidenORMarriedName))
            return false;
        if(!RationCard_Fname.equalsIgnoreCase(Obj.RationCard_Fname))
            return false;
        if(!RationCard_Mname.equalsIgnoreCase(Obj.RationCard_Mname))
            return false;
        if(!RationCard_Lname.equalsIgnoreCase(Obj.RationCard_Lname))
            return false;
        if(BankBranchID != Obj.BankBranchID)
            return false;
        if (GroupGuarantor != Obj.GroupGuarantor)
            return false;
        if(!DeactivateDate.equalsIgnoreCase(Obj.DeactivateDate))
            return false;

        return true;
    }
    public void Copy_Memberdata(clsMemberData Obj) {
        Obj.MemberID = MemberID;
        Obj.MemberNumber = MemberNumber;
        Obj.Fname = Fname;
        Obj.Mname = Mname;
        Obj.Lname = Lname;
        Obj.BirthDate = BirthDate;
        Obj.Gender = Gender;
        Obj.Title = Title;
        Obj.JoiningDate = JoiningDate;
        Obj.ContactNo = ContactNo;
        Obj.SavingAccNo = SavingAccNo;
        Obj.AddressID = AddressID;
        Obj.BranchID = BranchID;
        Obj.PrevMemberID = PrevMemberID;
//        Obj.SysliftMemberID = SysliftMemberID;
        Obj.DeathDate = DeathDate;
        Obj.IsActive = IsActive;
        Obj.CustomerTypeID = CustomerTypeID;
        Obj.GroupID = GroupID;
        Obj.Email = Email;
        Obj.ReligionID = ReligionID;
        Obj.CasteID = CasteID;
        Obj.CasteCategoryID = CasteCategoryID;
        Obj.MaritalStatusID = MaritalStatusID;
        Obj.RelationID = RelationID;
        Obj.MainMemberID = MainMemberID;
        Obj.FamilyHead = FamilyHead;
        Obj.HMFApplicable = HMFApplicable;
        Obj.EncodedBy = EncodedBy;
        Obj.EncodedDate = EncodedDate;
        Obj.DeletionReasonID = DeletionReasonID;
        Obj.MemberFamilyDetailsID = MemberFamilyDetailsID;
        Obj.BankID = BankID;
        Obj.BankAccountNumber = BankAccountNumber;
        Obj.Bank_Fname = Bank_Fname;
        Obj.Bank_Mname = Bank_Mname;
        Obj.Bank_Lname = Bank_Lname;
        Obj.IsMaidenName = IsMaidenName;
        Obj.MaidenORMarriedName = MaidenORMarriedName;
        Obj.RationCard_Fname = RationCard_Fname;
        Obj.RationCard_Mname = RationCard_Mname;
        Obj.RationCard_Lname = RationCard_Lname;
        Obj.BankBranchID = BankBranchID;
        Obj.GroupGuarantor = GroupGuarantor;
        Obj.DeactivateDate = DeactivateDate;
    }
}
