package com.example.ufin_lso;

import java.util.ArrayList;

//clsNewGroup, clsGroupMember <=> Data collected/presented by:
//fragment_group_check, fragment_group_identity, grt_checklist
public class clsGroupMember {

//LSO:PIF (0)Created   - Open   ----Post----> Validator:PIF Created    - Locked
//LSO:PIF (1)Edit      - Open   ----Post----> Validator:PIF Edit       - Locked
//LSO:PIF (2)Submitted - Locked ----Post----> Validator:PIF Submitted  - Open
//LSO:PIF (20)SubmitACK - Locked <--Response-- Validator:PIF Submitted  - Open
//LSO:PIF (3)Query     - Open   <---Get------ Validator:PIF Query      - Locked
//LSO:PIF (2)Submitted - Locked ----Post----> Validator:PIF Submitted  - Open
//LSO:PIF (20)SubmitACK - Locked <--Response-- Validator:PIF Submitted  - Open
//LSO:PIF (3)Query     - Open   <---Get------ Validator:PIF Query      - Locked
//LSO:PIF (2)Submitted - Locked ----Post----> Validator:PIF Submitted  - Open
//LSO:PIF (20)SubmitACK - Locked <--Response-- Validator:PIF Submitted  - Open
//LSO:PIF (4)Validated - Locked <---Get------ Validator:PIF Validated  - Locked

    public static final int MEMBER_STATUS_CREATED           = 0;
    public static final int MEMBER_STATUS_PIF_EDIT          = 1;
    public static final int MEMBER_STATUS_PIF_SUBMIT        = 2;
//bmp 01-Feb-23
//    public static final int MEMBER_STATUS_PIF_SUBMIT_ACK    = 20;
//bmp 01-Feb-23
    public static final int MEMBER_STATUS_PIF_QUERY         = 3;
    public static final int MEMBER_STATUS_PIF_OK            = 4;
    public static final int MEMBER_STATUS_GRT_OK            = 5;
//bmp 29-Sep-22
//    public static final int MEMBER_STATUS_LOAN_CANCELLED    = 6;
//    public static final int MEMBER_STATUS_LOAN_DISBURSED    = 7;
    public static final int MEMBER_STATUS_LOAN_DISBURSED    = 6;
    public static final int MEMBER_STATUS_LOAN_CANCELLED    = 7;
//bmp 29-Sep-22
    public static final int MEMBER_STATUS_LOAN_ON_GOING     = 8;
    public static final int MEMBER_STATUS_LOAN_ENDED        = 9;
    public static final int MEMBER_STATUS_NO_LOAN           = 10;
//bmp 14-Dec-23
    public static final int MEMBER_STATUS_READY_FOR_GRT     = 99;
//bmp 14-Dec-23

    public static final String strMEMBER_STATUS_CREATED           = "Created";
    public static final String strMEMBER_STATUS_PIF_EDIT          = "PIF Edit";
    public static final String strMEMBER_STATUS_PIF_SUBMIT        = "PIF Submited";
    public static final String strMEMBER_STATUS_PIF_QUERY         = "PIF Query";
    public static final String strMEMBER_STATUS_PIF_OK            = "PIF OK";
    public static final String strMEMBER_STATUS_GRT_OK            = "GRT OK";
    public static final String strMEMBER_STATUS_LOAN_CANCELLED    = "Loan Cancelled";
    public static final String strMEMBER_STATUS_LOAN_DISBURSED    = "Loan Disbursed";
    public static final String strMEMBER_STATUS_LOAN_ON_GOING     = "Loan On-going";
    public static final String strMEMBER_STATUS_LOAN_ENDED        = "Loan Ended";
    public static final String strMEMBER_STATUS_NO_LOAN           = "No Loan";

    long MemberID;
    long AddressID;

    long MemberDetailsForImpactID;
    long MemberDocuemntsRcvdID;
    long MemberDocuemntsRcvdID2;
    long MemberDocuemntsRcvdID3;

    String AadhaarNumber;
    String Fname;
    String Mname;
    String Lname;
    String Gender;
    String BirthDate;
    String Address;
    String Pincode;
    long MaritalStatusID;
    long OccupationID;
    int OwnHouse;

    String filenamePhoto;

    String GuarantorName;
    long GuarantorRelationID;
    int LoanAmount;
    int LoanDuration;

    int MemberStatus;
    int RemainingLoanMonths;
    int LoanApplicant;
    long LoanProductDetailsID;
    int selectedPositionLoanProduct;

    ArrayList<Integer> LoanProductDetailsIDMaster = new ArrayList<Integer>();
    ArrayList<Integer> LoanProductIDMaster = new ArrayList<Integer>();
    ArrayList<String> PrincipalAmtMaster = new ArrayList<String>();
    ArrayList<String> LoanPeriodInMonthsMaster = new ArrayList<String>();
    ArrayList<String> TotalMonthlyInstallmentAmtMaster = new ArrayList<String>();
    ArrayList<String> Principal_Period_Installment_Master = new ArrayList<String>();
    int iMonthlySurplus;
    boolean Reloan;
    boolean IsJointAccountHolderRequired;

    int PresentForMeeting;

    int iDisbursedLoan;
    int iEligibleLoan;
    String strLoanStartDate;
    String strLoanEndDate;
    long GroupID;
    String ContactNo;

    public void InitGroupMember() {
        MemberID = 0;
        AddressID = 0;
        MemberDetailsForImpactID = 0;
        MemberDocuemntsRcvdID = 0;
        MemberDocuemntsRcvdID2 = 0;
        MemberDocuemntsRcvdID3 = 0;
        AadhaarNumber = "";
        Fname = "";
        Mname = "";
        Lname = "";
        Gender = "F";
        BirthDate = "2000-01-01";
        Address = "Not Available";
        Pincode = "0";
        MaritalStatusID = 0;
        OccupationID = 0;
        OwnHouse = 0;
        GuarantorName = "";
        GuarantorRelationID = 0;
        filenamePhoto = "";
        LoanAmount = 0;
        LoanDuration = 0;
        MemberStatus = MEMBER_STATUS_CREATED;
        RemainingLoanMonths = 0;
        LoanApplicant = -1;
        LoanProductDetailsID = 0;
        selectedPositionLoanProduct = -1;

        PresentForMeeting = 0;
        GroupID = 0;
        ContactNo = "";
    }

    public boolean Compare_GroupMember(clsGroupMember Obj) {
        if(!AadhaarNumber.equalsIgnoreCase(Obj.AadhaarNumber))
            return false;
        if(!Fname.equalsIgnoreCase(Obj.Fname))
            return false;
        if(!Mname.equalsIgnoreCase(Obj.Mname))
            return false;
        if(!Lname.equalsIgnoreCase(Obj.Lname))
            return false;
        if(!Gender.equalsIgnoreCase(Obj.Gender))
            return false;
        if(!BirthDate.equalsIgnoreCase(Obj.BirthDate))
            return false;
        if(!Address.equalsIgnoreCase(Obj.Address))
            return false;
        if(!Pincode.equalsIgnoreCase(Obj.Pincode))
            return false;
        if(MemberID != Obj.MemberID)
            return false;
        if(AddressID != Obj.AddressID)
            return false;
        if(MemberDetailsForImpactID != Obj.MemberDetailsForImpactID)
            return false;
        if(MemberDocuemntsRcvdID != Obj.MemberDocuemntsRcvdID)
            return false;
        if(MemberDocuemntsRcvdID2 != Obj.MemberDocuemntsRcvdID2)
            return false;
        if(MemberDocuemntsRcvdID3 != Obj.MemberDocuemntsRcvdID3)
            return false;
        if(MaritalStatusID != Obj.MaritalStatusID)
            return false;
        if(OccupationID != Obj.OccupationID)
            return false;
        if(OwnHouse != Obj.OwnHouse)
            return false;
        if(!GuarantorName.equalsIgnoreCase(Obj.GuarantorName))
            return false;
        if(GuarantorRelationID != Obj.GuarantorRelationID)
            return false;
        if(!filenamePhoto.equalsIgnoreCase(Obj.filenamePhoto))
            return false;
        if(LoanAmount != Obj.LoanAmount)
            return false;
        if(LoanDuration != Obj.LoanDuration)
            return false;
        if(MemberStatus != Obj.MemberStatus)
            return false;
        if(RemainingLoanMonths != Obj.RemainingLoanMonths)
            return false;
        if(LoanApplicant != Obj.LoanApplicant)
            return false;
        if(LoanProductDetailsID != Obj.LoanProductDetailsID)
            return false;
        if(selectedPositionLoanProduct != Obj.selectedPositionLoanProduct)
            return false;
        if(PresentForMeeting != Obj.PresentForMeeting)
            return false;
        if(this.GroupID != Obj.GroupID)
            return false;

        if(!this.ContactNo.equalsIgnoreCase(Obj.ContactNo))
            return false;

        return true;
    }
    public void Copy_GroupMember(clsGroupMember Obj) {
        Obj.MemberID = MemberID;
        Obj.AddressID = AddressID;
        Obj.MemberDetailsForImpactID = MemberDetailsForImpactID;
        Obj.MemberDocuemntsRcvdID = MemberDocuemntsRcvdID;
        Obj.MemberDocuemntsRcvdID2 = MemberDocuemntsRcvdID2;
        Obj.MemberDocuemntsRcvdID3 = MemberDocuemntsRcvdID3;
        Obj.AadhaarNumber = AadhaarNumber;
        Obj.Fname = Fname;
        Obj.Mname = Mname;
        Obj.Lname = Lname;
        Obj.Gender = Gender;
        Obj.BirthDate = BirthDate;
        Obj.Address = Address;
        Obj.Pincode = Pincode;
        Obj.MaritalStatusID = MaritalStatusID;
        Obj.OccupationID = OccupationID;
        Obj.OwnHouse = OwnHouse;
        Obj.GuarantorName = GuarantorName;
        Obj.GuarantorRelationID = GuarantorRelationID;
        Obj.filenamePhoto = filenamePhoto;
        Obj.LoanAmount = LoanAmount;
        Obj.LoanDuration = LoanDuration;
        Obj.MemberStatus = MemberStatus;
        Obj.RemainingLoanMonths = RemainingLoanMonths;
        Obj.LoanApplicant = LoanApplicant;
        Obj.LoanProductDetailsID = LoanProductDetailsID;
        Obj.selectedPositionLoanProduct = selectedPositionLoanProduct;

        Obj.PresentForMeeting = PresentForMeeting;
        Obj.GroupID = GroupID;

        Obj.ContactNo = ContactNo;
    }
}
