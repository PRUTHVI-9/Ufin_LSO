package com.example.ufin_lso;

public class clsLoanRules {
//    long LoanRuleID;
//    String CriteriaType;    //Member/Group
//    String Criteria;
//    int Value;

//Punarbhet
//Punarbandhani-1
    //TotalGroupMemberCount = 5
    //Credit Check = Yes
    //(iSingles + iOnRent) <= 2
    //iReplacedMemberCount <= 2
    //(ReplacedMemberLoanCycle <-> GroupMemberLoanCycle) <= +/-3
//Punarbandhani-2
    //(PIFdate-Today) >= 0
    //(PIFdate-Today) <= 60
    //(GRTdate - PIFdate) >= 0
    //(GRTdate-Today) <= 60
    //(Disbursementdate - GRTdate) >= 0
    //(Disbursementdate - GRTdate) < 30
//PIF
    //NewloanMainMemberAge >= 22
    //NewloanMainMemberAge <= 50
    //ReloanMainMemberAge >= 23
    //ReloanMainMemberAge <= 62
    //Guarantor/OtherGuarantorAge >= 18
    //Guarantor/OtherGuarantorAge <= 65
    //JointLoanee != Guarantor
    //MainMember Age Proof = Aadhaarcard
    //Guarantor/Nominee Age Proof = Aadhaarcard/Pancard
    //MainMember Pancard required Loan Amount >= 100000
    //MainMember Shopact License required Loan Amount >= 205000
    //MainMember Own House Document required Loan Amount >= 130000
    //MainMember Rationcard required Loan Amount >= 40000

}
