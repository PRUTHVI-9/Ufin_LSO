package com.example.ufin_lso;

public class clsFamilyImpactInfo {
    long MFamilyImpactID;
    long MemberFamilyDetailsID;
    int PerCapitaIncome;
    int HouseRentExpense;
    int FoodExpense;
    int EducationExpense;
    int OtherExpenses;
    long EncodedBy;
    String ImpactEncodedDate;
    long UpdatedBy;
    String ImpactUpdatedDate;
    int IsActive;

    int FamilyTotalIncome;  //Local Variable

    public void Init_FamilyImpactInfo() {
        MFamilyImpactID = 0;
        MemberFamilyDetailsID = 0;
        PerCapitaIncome = 0;
        HouseRentExpense = 0;
        FoodExpense = 0;
        EducationExpense = 0;
        OtherExpenses = 0;
        EncodedBy = 0;
        ImpactEncodedDate = "2000-01-01";
        UpdatedBy = 0;
        ImpactUpdatedDate = "2000-01-01";
        IsActive = 1;
        FamilyTotalIncome = 0;
    }
    public boolean Compare_MemberFamilyImpactInfo(clsFamilyImpactInfo Obj) {
        if(MFamilyImpactID != Obj.MFamilyImpactID)
            return false;
        if(MemberFamilyDetailsID != Obj.MemberFamilyDetailsID)
            return false;
        if(PerCapitaIncome != Obj.PerCapitaIncome)
            return false;
        if(HouseRentExpense != Obj.HouseRentExpense)
            return false;
        if(FoodExpense != Obj.FoodExpense)
            return false;
        if(EducationExpense != Obj.EducationExpense)
            return false;
        if(OtherExpenses != Obj.OtherExpenses)
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
    public void Copy_MemberFamilyImpactInfo(clsFamilyImpactInfo Obj) {
        Obj.MFamilyImpactID = MFamilyImpactID;
        Obj.MemberFamilyDetailsID = MemberFamilyDetailsID;
        Obj.PerCapitaIncome = PerCapitaIncome;
        Obj.HouseRentExpense = HouseRentExpense;
        Obj.FoodExpense = FoodExpense;
        Obj.EducationExpense = EducationExpense;
        Obj.OtherExpenses = OtherExpenses;
        Obj.EncodedBy = EncodedBy;
        Obj.ImpactEncodedDate = ImpactEncodedDate;
        Obj.UpdatedBy = UpdatedBy;
        Obj.ImpactUpdatedDate = ImpactUpdatedDate;
        Obj.IsActive = IsActive;
    }
}
