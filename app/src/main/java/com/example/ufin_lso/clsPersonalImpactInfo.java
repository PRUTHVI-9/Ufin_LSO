package com.example.ufin_lso;

public class clsPersonalImpactInfo {
    long MemberDetailsForImpactID;
    long MemberID;
    int MonthlyIncome;
    int YearlyIncome;
    long BusinessID;
    long QualificationID;
    long HabitID;
    long HealthProblemID;
    int OwnHouse;
    long EncodedBy;
    String ImpactEncodedDate;
    long UpdatedBy;
    String ImpactUpdatedDate;
    int IsActive;
    int OtherMonthlyIncome;

    public void Init_PersonalImpactInfo() {
        MemberDetailsForImpactID = 0;
        MemberID = 0;
        MonthlyIncome = 0;
        YearlyIncome = 0;
        BusinessID = 0;
        QualificationID = 0;
        HabitID = 9;
        HealthProblemID = 17;
        OwnHouse = 0;
        EncodedBy = 0;
        ImpactEncodedDate = "2000-01-01";
        UpdatedBy = 0;
        ImpactUpdatedDate = "2000-01-01";
        IsActive = 1;
        OtherMonthlyIncome = 0;
    }
    public boolean Compare_PersonalImpactInfo(clsPersonalImpactInfo Obj) {
        if(MemberDetailsForImpactID != Obj.MemberDetailsForImpactID)
            return false;
        if(MemberID != Obj.MemberID)
            return false;
        if(MonthlyIncome != Obj.MonthlyIncome)
            return false;
        if(YearlyIncome != Obj.YearlyIncome)
            return false;
        if(BusinessID != Obj.BusinessID)
            return false;
        if(QualificationID != Obj.QualificationID)
            return false;
        if(HabitID != Obj.HabitID)
            return false;
        if(HealthProblemID != Obj.HealthProblemID)
            return false;
        if(OwnHouse != Obj.OwnHouse)
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
        if(OtherMonthlyIncome != Obj.OtherMonthlyIncome)
            return false;

        return true;
    }
    public void Copy_PersonalImpactInfo(clsPersonalImpactInfo Obj) {
        Obj.MemberDetailsForImpactID = MemberDetailsForImpactID;
        Obj.MemberID = MemberID;
        Obj.MonthlyIncome = MonthlyIncome;
        Obj.YearlyIncome = YearlyIncome;
        Obj.BusinessID = BusinessID;
        Obj.QualificationID = QualificationID;
        Obj.HabitID = HabitID;
        Obj.HealthProblemID = HealthProblemID;
        Obj.OwnHouse = OwnHouse;
        Obj.EncodedBy = EncodedBy;
        Obj.ImpactEncodedDate = ImpactEncodedDate;
        Obj.UpdatedBy = UpdatedBy;
        Obj.ImpactUpdatedDate = ImpactUpdatedDate;
        Obj.IsActive = IsActive;
        Obj.OtherMonthlyIncome = OtherMonthlyIncome;
    }
}
