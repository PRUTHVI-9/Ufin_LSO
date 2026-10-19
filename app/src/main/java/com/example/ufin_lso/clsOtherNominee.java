package com.example.ufin_lso;

public class clsOtherNominee {
    long NomineeID;
    long MemberID;
    String FName;
    String MName;
    String LName;
    long AddressID;
    long RelationID;
    String ContactNo;
    String DateOfBirth;
    int IsActive;
    long BankBranchID;
    String BankAccountNumber;
    String Bank_FName;
    String Bank_MName;
    String Bank_LName;

    void Init_OtherNominee() {
        NomineeID = 0;
        MemberID = 0;
        FName = "NA";
        MName = "NA";
        LName = "NA";
        AddressID = 0;
        RelationID = 0;
        ContactNo = "NA";
        DateOfBirth = "2000-01-01";
        IsActive = 1;
        BankBranchID = 0;
        BankAccountNumber = "NA";
        Bank_FName = "NA";
        Bank_MName = "NA";
        Bank_LName = "NA";
    }
    public boolean Compare_OtherNominee(clsOtherNominee Obj) {
        if(this.NomineeID != Obj.NomineeID)
            return false;
        if(this.MemberID != Obj.MemberID)
            return false;
        if(this.AddressID != Obj.AddressID)
            return false;
        if(this.RelationID != Obj.RelationID)
            return false;
        if(this.IsActive != Obj.IsActive)
            return false;
        if(this.BankBranchID != Obj.BankBranchID)
            return false;

        if(!this.FName.equalsIgnoreCase(Obj.FName))
            return false;
        if(!this.MName.equalsIgnoreCase(Obj.MName))
            return false;
        if(!this.LName.equalsIgnoreCase(Obj.LName))
            return false;
        if(!this.ContactNo.equalsIgnoreCase(Obj.ContactNo))
            return false;
        if(!this.DateOfBirth.equalsIgnoreCase(Obj.DateOfBirth))
            return false;
        if(!this.BankAccountNumber.equalsIgnoreCase(Obj.BankAccountNumber))
            return false;
        if(!this.Bank_FName.equalsIgnoreCase(Obj.Bank_FName))
            return false;
        if(!this.Bank_MName.equalsIgnoreCase(Obj.Bank_MName))
            return false;
        if(!this.Bank_LName.equalsIgnoreCase(Obj.Bank_LName))
            return false;

        return true;
    }
    public void Copy_OtherNominee(clsOtherNominee Obj) {
        Obj.NomineeID = NomineeID;
        Obj.MemberID = MemberID;
        Obj.FName = FName;
        Obj.MName = MName;
        Obj.LName = LName;
        Obj.AddressID = AddressID;
        Obj.RelationID = RelationID;
        Obj.ContactNo = ContactNo;
        Obj.DateOfBirth = DateOfBirth;
        Obj.IsActive = IsActive;
        Obj.BankBranchID = BankBranchID;
        Obj.BankAccountNumber = BankAccountNumber;
        Obj.Bank_FName = Bank_FName;
        Obj.Bank_MName = Bank_MName;
        Obj.Bank_LName = Bank_LName;
    }
}
