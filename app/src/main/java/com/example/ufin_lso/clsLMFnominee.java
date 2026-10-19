package com.example.ufin_lso;

public class clsLMFnominee {
    long LMFNomineeDetailsID;
    long MemberID;
    int NomineeIsFamilyMember;
    long NomineeID;
    String Guardian_FName;
    String Guardian_MName;
    String Guardian_LName;
    String GuardianAddress;
    String NomineeStartDate;
    String NomineeEndDate;
    int IsActive;

    void Init_LMFnominee() {
        LMFNomineeDetailsID = 0;
        MemberID = 0;
        NomineeIsFamilyMember = 0;
        NomineeID = 0;
        Guardian_FName = "NA";
        Guardian_MName = "NA";
        Guardian_LName = "NA";
        GuardianAddress = "NA";
        NomineeStartDate = "NA";
        NomineeEndDate = "NA";
        IsActive = 1;
    }
    public boolean Compare_LMFnominee(clsLMFnominee Obj) {
        if(this.LMFNomineeDetailsID != Obj.LMFNomineeDetailsID)
            return false;
        if(this.MemberID != Obj.MemberID)
            return false;
        if(this.NomineeIsFamilyMember != Obj.NomineeIsFamilyMember)
            return false;
        if(this.NomineeID != Obj.NomineeID)
            return false;
        if(this.Guardian_FName != Obj.Guardian_FName)
            return false;
        if(this.Guardian_MName != Obj.Guardian_MName)
            return false;
        if(this.Guardian_LName != Obj.Guardian_LName)
            return false;
        if(this.GuardianAddress != Obj.GuardianAddress)
            return false;
        if(this.NomineeStartDate != Obj.NomineeStartDate)
            return false;
        if(this.NomineeEndDate != Obj.NomineeEndDate)
            return false;
        if(this.IsActive != Obj.IsActive)
            return false;

        return true;
    }
    public void Copy_LMFnominee(clsLMFnominee Obj) {
        Obj.LMFNomineeDetailsID = LMFNomineeDetailsID;
        Obj.MemberID = MemberID;
        Obj.NomineeIsFamilyMember = NomineeIsFamilyMember;
        Obj.NomineeID = NomineeID;
        Obj.Guardian_FName = Guardian_FName;
        Obj.Guardian_MName = Guardian_MName;
        Obj.Guardian_LName = Guardian_LName;
        Obj.GuardianAddress = GuardianAddress;
        Obj.NomineeStartDate = NomineeStartDate;
        Obj.NomineeEndDate = NomineeEndDate;
        Obj.IsActive = IsActive;
    }
}
