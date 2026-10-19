package com.example.ufin_lso;

import java.io.File;
import java.util.ArrayList;

//clsNewGroup, clsGroupMember <=> Data collected/presented by:
//fragment_group_check, fragment_group_identity, grt_checklist
public class clsNewGroup {

    public static final int GROUP_STATUS_CREATED                        = 0;
    public static final int GROUP_STATUS_GROUP_FORM_EDIT                = 1;
    public static final int GROUP_STATUS_GROUP_FORM_SUBMIT              = 2;
//bmp 01-Feb-23
//    public static final int GROUP_STATUS_GROUP_FORM_SUBMIT_ACK          = 20;
//bmp 01-Feb-23
    public static final int GROUP_STATUS_GROUP_FORM_QUERY               = 3;
    public static final int GROUP_STATUS_GROUP_FORM_OK                  = 4;
    public static final int GROUP_STATUS_GRT_OK                         = 5;
//    public static final int GROUP_STATUS_LOAN_CANCELLED                 = 6;
//    public static final int GROUP_STATUS_LOAN_DISBURSED                 = 7;
//    public static final int GROUP_STATUS_LOAN_ON_GOING                  = 8;
//    public static final int GROUP_STATUS_LOAN_ENDED                     = 9;
    public static final int GROUP_STATUS_LOAN_ACTIVE                    = 6;
    public static final int GROUP_STATUS_NO_LOAN                        = 10;
//bmp 14-Dec-23
    public static final int GROUP_STATUS_READY_FOR_GRT                  = 99;
//bmp 14-Dec-23

//    clsGroupMember[] arrGroupMember = new clsGroupMember[5];
    ArrayList<clsGroupMember> arrGroupMember = new ArrayList<clsGroupMember>();

    int iSingles;
    int iOnRent;
    int iDomestic;
    String pathDocuments;
    long lCenterID;
    String CenterName;
    long lNoLoanGroupID;
    long lGroupID;
    long iCommunityID;
    long iClusterID;
    String CommunityName;
    String ClusterName;
    String BranchNo;
    String BranchName;

    String LoanDisbursementDate;
    String LoanDisbursementTime;
    String LoanRepayInstallmentDate;
    String LoanRepayInstallmentTime;
    int GroupStatus;
    String PIFDate;
    String PIFTime;
    String GroupTestDate;
    String GroupTestTime;

    String filenamePhoto;
    long GroupDocuemntsRcvdID;
    long CollectionMemberID;
    int MemberCount;

    ArrayList<Integer> LoanProductMasterID = new ArrayList<Integer>();

    public void InitNewGroup() {

        File folder = new File(pathDocuments);
        if(!folder.exists()) {
            folder.mkdir();
        }

        iSingles = 0;
        iOnRent = 0;
        MemberCount = 0;
//        for(int i=0; i<5; i++) {
//            clsGroupMember groupMember = new clsGroupMember();
//            groupMember.InitGroupMember();
//            arrGroupMember.get(i) = groupMember;
//        }
//bmp 22-Aug-26
        arrGroupMember.clear();
//bmp 22-Aug-26

        lCenterID = 0;
        CenterName = "";
        lNoLoanGroupID = 0;
        lGroupID = 0;
        iCommunityID = 0;
        iClusterID = 0;
        CommunityName = "";
        ClusterName = "";
        BranchNo = "";
        BranchName = "";
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
        GroupStatus = GROUP_STATUS_CREATED;
        GroupTestDate = "2010-01-01";
        GroupTestTime = "00:00";
        PIFDate = "2010-01-01";
        PIFTime = "00:00";

        filenamePhoto = "";
        GroupDocuemntsRcvdID = 0;
        CollectionMemberID = 0;
    }

    public boolean Compare_NewGroup(clsNewGroup Obj) {
        if(iSingles != Obj.iSingles)
            return false;
        if(iOnRent != Obj.iOnRent)
            return false;

        for(int i=0; i<MemberCount; i++) {
            if(!arrGroupMember.get(i).AadhaarNumber.equalsIgnoreCase(Obj.arrGroupMember.get(i).AadhaarNumber))
                return false;
            if(!arrGroupMember.get(i).Fname.equalsIgnoreCase(Obj.arrGroupMember.get(i).Fname))
                return false;
            if(!arrGroupMember.get(i).Mname.equalsIgnoreCase(Obj.arrGroupMember.get(i).Mname))
                return false;
            if(!arrGroupMember.get(i).Lname.equalsIgnoreCase(Obj.arrGroupMember.get(i).Lname))
                return false;
            if(!arrGroupMember.get(i).Gender.equalsIgnoreCase(Obj.arrGroupMember.get(i).Gender))
                return false;
            if(!arrGroupMember.get(i).BirthDate.equalsIgnoreCase(Obj.arrGroupMember.get(i).BirthDate))
                return false;
            if(!arrGroupMember.get(i).Address.equalsIgnoreCase(Obj.arrGroupMember.get(i).Address))
                return false;
            if(!arrGroupMember.get(i).Pincode.equalsIgnoreCase(Obj.arrGroupMember.get(i).Pincode))
                return false;
            if(arrGroupMember.get(i).MemberID != Obj.arrGroupMember.get(i).MemberID)
                return false;
            if(arrGroupMember.get(i).AddressID != Obj.arrGroupMember.get(i).AddressID)
                return false;
            if(arrGroupMember.get(i).MemberDetailsForImpactID != Obj.arrGroupMember.get(i).MemberDetailsForImpactID)
                return false;
            if(arrGroupMember.get(i).MemberDocuemntsRcvdID != Obj.arrGroupMember.get(i).MemberDocuemntsRcvdID)
                return false;
            if(arrGroupMember.get(i).MaritalStatusID != Obj.arrGroupMember.get(i).MaritalStatusID)
                return false;
            if(arrGroupMember.get(i).OccupationID != Obj.arrGroupMember.get(i).OccupationID)
                return false;
            if(arrGroupMember.get(i).OwnHouse != Obj.arrGroupMember.get(i).OwnHouse)
                return false;
            if(!arrGroupMember.get(i).GuarantorName.equalsIgnoreCase(Obj.arrGroupMember.get(i).GuarantorName))
                return false;
            if(arrGroupMember.get(i).GuarantorRelationID != Obj.arrGroupMember.get(i).GuarantorRelationID)
                return false;
            if(!arrGroupMember.get(i).filenamePhoto.equalsIgnoreCase(Obj.arrGroupMember.get(i).filenamePhoto))
                return false;
            if(arrGroupMember.get(i).LoanAmount != Obj.arrGroupMember.get(i).LoanAmount)
                return false;
            if(arrGroupMember.get(i).LoanDuration != Obj.arrGroupMember.get(i).LoanDuration)
                return false;
            if(arrGroupMember.get(i).MemberStatus != Obj.arrGroupMember.get(i).MemberStatus)
                return false;
            if(arrGroupMember.get(i).LoanApplicant != Obj.arrGroupMember.get(i).LoanApplicant)
                return false;
            if(arrGroupMember.get(i).RemainingLoanMonths != Obj.arrGroupMember.get(i).RemainingLoanMonths)
                return false;
        }

        if(lCenterID != Obj.lCenterID)
            return false;
        if(lNoLoanGroupID != Obj.lNoLoanGroupID)
            return false;
        if(lGroupID != Obj.lGroupID)
            return false;
        if(iCommunityID != Obj.iCommunityID)
            return false;
        if(iClusterID != Obj.iClusterID)
            return false;
        if(!CommunityName.equalsIgnoreCase(Obj.CommunityName))
            return false;
        if(!ClusterName.equalsIgnoreCase(Obj.ClusterName))
            return false;
        if(!CenterName.equalsIgnoreCase(Obj.CenterName))
            return false;
        if(!BranchNo.equalsIgnoreCase(Obj.BranchNo))
            return false;
        if(!BranchName.equalsIgnoreCase(Obj.BranchName))
            return false;

        if(!LoanDisbursementDate.equalsIgnoreCase(Obj.LoanDisbursementDate))
            return false;
        if(!LoanDisbursementTime.equalsIgnoreCase(Obj.LoanDisbursementTime))
            return false;
        if(!LoanRepayInstallmentDate.equalsIgnoreCase(Obj.LoanRepayInstallmentDate))
            return false;
        if(!LoanRepayInstallmentTime.equalsIgnoreCase(Obj.LoanRepayInstallmentTime))
            return false;
        if(!GroupTestDate.equalsIgnoreCase(Obj.GroupTestDate))
            return false;
        if(!GroupTestTime.equalsIgnoreCase(Obj.GroupTestTime))
            return false;
        if(!PIFDate.equalsIgnoreCase(Obj.PIFDate))
            return false;
        if(!PIFTime.equalsIgnoreCase(Obj.PIFTime))
            return false;
        if(!filenamePhoto.equalsIgnoreCase(Obj.filenamePhoto))
            return false;
        if(GroupStatus != Obj.GroupStatus)
            return false;
        if(GroupDocuemntsRcvdID != Obj.GroupDocuemntsRcvdID)
            return false;
        if(CollectionMemberID != Obj.CollectionMemberID)
            return false;
        if(MemberCount != Obj.MemberCount)
            return false;

        return true;
    }
    public void Copy_NewGroup(clsNewGroup Obj) {
        Obj.iSingles = iSingles;
        Obj.iOnRent = iOnRent;
        Obj.MemberCount = MemberCount;
        for(int i=0; i<MemberCount; i++) {
            Obj.arrGroupMember.get(i).MemberID = arrGroupMember.get(i).MemberID;
            Obj.arrGroupMember.get(i).AddressID = arrGroupMember.get(i).AddressID;
            Obj.arrGroupMember.get(i).MemberDetailsForImpactID = arrGroupMember.get(i).MemberDetailsForImpactID;
            Obj.arrGroupMember.get(i).MemberDocuemntsRcvdID = arrGroupMember.get(i).MemberDocuemntsRcvdID;
            Obj.arrGroupMember.get(i).AadhaarNumber = arrGroupMember.get(i).AadhaarNumber;
            Obj.arrGroupMember.get(i).Fname = arrGroupMember.get(i).Fname;
            Obj.arrGroupMember.get(i).Mname = arrGroupMember.get(i).Mname;
            Obj.arrGroupMember.get(i).Lname = arrGroupMember.get(i).Lname;
            Obj.arrGroupMember.get(i).Gender = arrGroupMember.get(i).Gender;
            Obj.arrGroupMember.get(i).BirthDate = arrGroupMember.get(i).BirthDate;
            Obj.arrGroupMember.get(i).Address = arrGroupMember.get(i).Address;
            Obj.arrGroupMember.get(i).Pincode = arrGroupMember.get(i).Pincode;
            Obj.arrGroupMember.get(i).MaritalStatusID = arrGroupMember.get(i).MaritalStatusID;
            Obj.arrGroupMember.get(i).OccupationID = arrGroupMember.get(i).OccupationID;
            Obj.arrGroupMember.get(i).OwnHouse = arrGroupMember.get(i).OwnHouse;
            Obj.arrGroupMember.get(i).GuarantorName = arrGroupMember.get(i).GuarantorName;
            Obj.arrGroupMember.get(i).GuarantorRelationID = arrGroupMember.get(i).GuarantorRelationID;
            Obj.arrGroupMember.get(i).filenamePhoto = arrGroupMember.get(i).filenamePhoto;
            Obj.arrGroupMember.get(i).LoanAmount = arrGroupMember.get(i).LoanAmount;
            Obj.arrGroupMember.get(i).LoanDuration = arrGroupMember.get(i).LoanDuration;
            Obj.arrGroupMember.get(i).MemberStatus = arrGroupMember.get(i).MemberStatus;
            Obj.arrGroupMember.get(i).LoanApplicant = arrGroupMember.get(i).LoanApplicant;
            Obj.arrGroupMember.get(i).RemainingLoanMonths = arrGroupMember.get(i).RemainingLoanMonths;
        }

        Obj.lCenterID = lCenterID;
        Obj.lNoLoanGroupID = lNoLoanGroupID;
        Obj.lGroupID = lGroupID;

        Obj.iCommunityID = iCommunityID;
        Obj.iClusterID = iClusterID;
        Obj.CommunityName = CommunityName;
        Obj.ClusterName = ClusterName;
        Obj.CenterName = CenterName;
        Obj.BranchNo = BranchNo;
        Obj.BranchName = BranchName;

        Obj.LoanDisbursementDate = LoanDisbursementDate;
        Obj.LoanDisbursementTime = LoanDisbursementTime;
        Obj.LoanRepayInstallmentDate = LoanRepayInstallmentDate;
        Obj.LoanRepayInstallmentTime = LoanRepayInstallmentTime;

        Obj.GroupStatus = GroupStatus;

        Obj.GroupTestDate = GroupTestDate;
        Obj.GroupTestTime = GroupTestTime;
        Obj.PIFDate = PIFDate;
        Obj.PIFTime = PIFTime;

        Obj.filenamePhoto = filenamePhoto;
        Obj.GroupDocuemntsRcvdID = GroupDocuemntsRcvdID;

        Obj.CollectionMemberID = CollectionMemberID;
    }
}
