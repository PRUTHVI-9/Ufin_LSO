package com.example.ufin_lso;

public class clsNewMemberIntro {
    clsMemberData newMemberData;
    clsMemberAddress newMemberAddress;
    clsRevisitMember newRevisitMember;
    clsPersonalImpactInfo newMemberPersonalImpactInfo;
    clsPersonalImpactInfo newGuarantorMemberPersonalImpactInfo;
    clsMemberFamilyDetails newMemberFamilyDetails;
    clsMemberDocRcvd newMemberDocRcvd;
    clsMemberDocRcvd newMemberKYCDoc2Rcvd;
    clsFamilyImpactInfo newMemberFamilyImpactInfo;
    clsAdditionalDetails newMemberAdditionalDetails;

    clsMemberData newGuarantorMemberData;
    clsMemberData newCoBorrowerMemberData;
    clsMemberAddress newOtherGuarantorAddress;
    clsMemberAddress newOtherCoBorrowerAddress;
    clsMemberDocRcvd newGuarantorMemberDocRcvd;
    clsMemberDocRcvd newGuarantorMemberKYCDoc2Rcvd;
    clsMemberDocRcvd newCoBorrowerMemberDocRcvd;
    clsMemberDocRcvd newCoBorrowerMemberKYCDoc2Rcvd;

    int AadhaarAge;

    public void InitNewMemberIntro(String strToday) {
        newMemberAddress = new clsMemberAddress();
        newMemberAddress.InitNewMemberAddress();
        newOtherGuarantorAddress = new clsMemberAddress();
        newOtherGuarantorAddress.InitNewMemberAddress();
        newOtherCoBorrowerAddress = new clsMemberAddress();
        newOtherCoBorrowerAddress.InitNewMemberAddress();

        newMemberData = new clsMemberData();
        newMemberData.InitNewMember(strToday);
        newCoBorrowerMemberData = new clsMemberData();
        newCoBorrowerMemberData.InitNewMember(strToday);
        newGuarantorMemberData = new clsMemberData();
        newGuarantorMemberData.InitNewMember(strToday);

        newRevisitMember = new clsRevisitMember();
        newRevisitMember.InitRevisitMember();
        newMemberPersonalImpactInfo = new clsPersonalImpactInfo();
        newMemberPersonalImpactInfo.Init_PersonalImpactInfo();
        newGuarantorMemberPersonalImpactInfo = new clsPersonalImpactInfo();
        newGuarantorMemberPersonalImpactInfo.Init_PersonalImpactInfo();
        newMemberFamilyDetails = new clsMemberFamilyDetails();
        newMemberFamilyDetails.Init_MemberFamilyDetails();
        newMemberDocRcvd = new clsMemberDocRcvd();
        newMemberDocRcvd.Init_MemberDocRcvd();
        newMemberKYCDoc2Rcvd = new clsMemberDocRcvd();
        newMemberKYCDoc2Rcvd.Init_MemberDocRcvd();
        newMemberFamilyImpactInfo = new clsFamilyImpactInfo();
        newMemberFamilyImpactInfo.Init_FamilyImpactInfo();
        newMemberAdditionalDetails = new clsAdditionalDetails();
        newMemberAdditionalDetails.Init_AdditionalDetails();

        newGuarantorMemberDocRcvd = new clsMemberDocRcvd();
        newGuarantorMemberDocRcvd.Init_MemberDocRcvd();
        newGuarantorMemberKYCDoc2Rcvd = new clsMemberDocRcvd();
        newGuarantorMemberKYCDoc2Rcvd.Init_MemberDocRcvd();
        newCoBorrowerMemberDocRcvd = new clsMemberDocRcvd();
        newCoBorrowerMemberDocRcvd.Init_MemberDocRcvd();
        newCoBorrowerMemberKYCDoc2Rcvd = new clsMemberDocRcvd();
        newCoBorrowerMemberKYCDoc2Rcvd.Init_MemberDocRcvd();

        newRevisitMember.PrevMaxLoanAmount = 55000;
        newRevisitMember.HomeLoanAmount = 0;
        AadhaarAge = 0;
    }
    public boolean Compare_NewMemberIntro(clsNewMemberIntro Obj) {
        return true;
    }
    public void Copy_NewMemberIntro(clsNewMemberIntro Obj) {
    }
}
