package com.example.ufin_lso;

import android.provider.BaseColumns;

public class ufinDbContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public ufinDbContract() {
    }

    private static final String BLOB_TYPE = " BLOB";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String AUTOINCREMENT = " AUTOINCREMENT";
    private static final String UNIQUE = " UNIQUE";
    private static final String COMMA_SEP = ",";

//All Master Tables (M_XXX):Downloaded/Copied from Filesystem and used by App, No Insert/Update by App.
//All Transaction Tables (TD_XXX):Inserted/Edited by app and Uploaded to Server.

    //Inner class that defines the table contents
    public static abstract class OrgStructureMaster implements BaseColumns {
        public static final String TABLE_NAME = "M_ORG_STRUCTURE";

        public static final String COLUMN_NAME_ORGStructureID = "ORGStructureID";
        public static final String COLUMN_NAME_NodeName = "NodeName";
        public static final String COLUMN_NAME_NodeLevel = "NodeLevel";
        public static final String COLUMN_NAME_ParentID = "ParentID";
        public static final String COLUMN_NAME_ParentLevel = "ParentLevel";
        public static final String COLUMN_NAME_BranchNumber = "BranchNumber";
        public static final String COLUMN_NAME_IsActive = "IsActive";

        public static final String SQL_CREATE_ORG_STRUCTURE_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ORGStructureID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_NodeName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_NodeLevel + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ParentID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ParentLevel + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_BranchNumber + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE +
                        ")";
    }
    public static abstract class LoanRulesMaster implements BaseColumns {
        public static final String TABLE_NAME = "M_LOAN_RULES";

        public static final String COLUMN_NAME_LoanRuleID = "LoanRuleID";
        public static final String COLUMN_NAME_LoanRuleRef = "LoanRuleRef";
        public static final String COLUMN_NAME_LoanParameter = "LoanParameter";
        public static final String COLUMN_NAME_LowerLimit = "LowerLimit";
        public static final String COLUMN_NAME_UpperLimit = "UpperLimit";
        public static final String COLUMN_NAME_IsActive = "IsActive";

        public static final String SQL_CREATE_LOAN_RULES_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_LoanRuleID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanRuleRef + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanParameter + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LowerLimit + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_UpperLimit + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE +
                        ")";
    }
    public static abstract class UserForTabMaster implements BaseColumns {
        public static final String TABLE_NAME = "M_USER_ForTab";

        public static final String COLUMN_NAME_ORGStructureID = "ORGStructureID";
        public static final String COLUMN_NAME_UserID = "UserID";
        public static final String COLUMN_NAME_UserName = "UserName";
        public static final String COLUMN_NAME_Password = "Password";
        public static final String COLUMN_NAME_NodeLevel = "NodeLevel";
        public static final String COLUMN_NAME_ServerIP = "ServerIP";
//bmp 19-Jun-23
        public static final String COLUMN_NAME_ServerIP2 = "ServerIP2";
//bmp 19-Jun-23
        public static final String COLUMN_NAME_ServerPort = "ServerPort";
        public static final String COLUMN_NAME_DefaultTimestamp = "DefaultTimestamp";
        public static final String COLUMN_NAME_OrgID = "OrgID";
        public static final String COLUMN_NAME_BranchID = "BranchID";
        public static final String COLUMN_NAME_BranchName = "BranchName";
        public static final String COLUMN_NAME_StartOfDaySyncupTimestamp = "StartOfDaySyncupTimestamp";
        public static final String COLUMN_NAME_EndOfDaySyncupTimestamp = "EndOfDaySyncupTimestamp";
        public static final String COLUMN_NAME_PaymentGatewayUsername = "PaymentGatewayUsername";
        public static final String COLUMN_NAME_PaymentGatewayPassword = "PaymentGatewayPassword";

        public static final String SQL_CREATE_USER_FORTAB_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ORGStructureID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_UserID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_UserName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Password + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_NodeLevel + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ServerIP + TEXT_TYPE + COMMA_SEP +
//bmp 19-Jun-23
                        COLUMN_NAME_ServerIP2 + TEXT_TYPE + COMMA_SEP +
//bmp 19-Jun-23
                        COLUMN_NAME_ServerPort + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_DefaultTimestamp + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_StartOfDaySyncupTimestamp + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_EndOfDaySyncupTimestamp + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OrgID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_BranchID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PaymentGatewayUsername + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PaymentGatewayPassword + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_BranchName + TEXT_TYPE +
                        ")";
    }
    public static abstract class StateMaster implements BaseColumns {
        public static final String TABLE_NAME = "M_STATE";

        public static final String COLUMN_NAME_StateID = "StateID";
        public static final String COLUMN_NAME_StateCode = "StateCode";
        public static final String COLUMN_NAME_Description = "Description";
        public static final String COLUMN_NAME_IsActive = "IsActive";
        public static final String COLUMN_NAME_MarathiDescription = "MarathiDescription";
        public static final String COLUMN_NAME_SequenceID = "SequenceID";

        public static final String SQL_CREATE_STATE_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_StateID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_StateCode + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Description + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MarathiDescription + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SequenceID + INTEGER_TYPE +
                        ")";
    }
    public static abstract class ReligionMaster implements BaseColumns {
        public static final String TABLE_NAME = "M_RELIGION";

        public static final String COLUMN_NAME_ReligionID = "ReligionID";
        public static final String COLUMN_NAME_Description = "Description";
        public static final String COLUMN_NAME_IsActive = "IsActive";
        public static final String COLUMN_NAME_MarathiDescription = "MarathiDescription";
        public static final String COLUMN_NAME_SequenceID = "SequenceID";

        public static final String SQL_CREATE_RELIGION_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ReligionID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Description + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MarathiDescription + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SequenceID + INTEGER_TYPE +
                        ")";
    }
    public static abstract class RelationMaster implements BaseColumns {
        public static final String TABLE_NAME = "M_RELATION";

        public static final String COLUMN_NAME_RelationID = "RelationID";
        public static final String COLUMN_NAME_Description = "Description";
        public static final String COLUMN_NAME_IsActive = "IsActive";
        public static final String COLUMN_NAME_MarathiDescription = "MarathiDescription";
        public static final String COLUMN_NAME_SequenceID = "SequenceID";

        public static final String SQL_CREATE_RELATION_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_RelationID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Description + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MarathiDescription + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SequenceID + INTEGER_TYPE +
                        ")";
    }
    public static abstract class OnlinePaymentApps implements BaseColumns {
        public static final String TABLE_NAME = "M_Online_Payment_Option_Master";

        public static final String COLUMN_NAME_OnlinePaymentAppID = "OnlinePaymentAppID";
        public static final String COLUMN_NAME_Description = "Description";
        public static final String COLUMN_NAME_IsActive = "IsActive";

        public static final String SQL_CREATE_ONLINE_PAYMENT_APPS_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_OnlinePaymentAppID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Description + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE +
                        ")";
    }
    public static abstract class QualificationMaster implements BaseColumns {
        public static final String TABLE_NAME = "M_QUALIFICATION";

        public static final String COLUMN_NAME_QualificationID = "QualificationID";
        public static final String COLUMN_NAME_Description = "Description";
        public static final String COLUMN_NAME_IsActive = "IsActive";
        public static final String COLUMN_NAME_MarathiDescription = "MarathiDescription";
        public static final String COLUMN_NAME_SequenceID = "SequenceID";

        public static final String SQL_CREATE_QUALIFICATION_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_QualificationID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Description + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MarathiDescription + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SequenceID + INTEGER_TYPE +
                        ")";
    }
    public static abstract class DocumentMaster implements BaseColumns {
        public static final String TABLE_NAME = "M_MEMBER_DOCUMENTS";

        public static final String COLUMN_NAME_DocumentID = "MemberDocumentsID";
        public static final String COLUMN_NAME_Description = "Description";
        public static final String COLUMN_NAME_IsActive = "IsActive";
        public static final String COLUMN_NAME_MarathiDescription = "MarathiDescription";
        public static final String COLUMN_NAME_SequenceID = "SequenceID";
        public static final String COLUMN_NAME_CheckDuplicate = "CheckDuplicate";

        public static final String SQL_CREATE_DOCUMENT_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_DocumentID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Description + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MarathiDescription + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SequenceID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_CheckDuplicate + INTEGER_TYPE +
                        ")";
    }
    public static abstract class MaritalStatusMaster implements BaseColumns {
        public static final String TABLE_NAME = "M_MARITAL_STATUS";

        public static final String COLUMN_NAME_MaritalStatusID = "MaritalStatusID";
        public static final String COLUMN_NAME_Description = "Description";
        public static final String COLUMN_NAME_IsActive = "IsActive";
        public static final String COLUMN_NAME_MarathiDescription = "MarathiDescription";
        public static final String COLUMN_NAME_SequenceID = "SequenceID";

        public static final String SQL_CREATE_MARITALSTATUS_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_MaritalStatusID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Description + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MarathiDescription + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SequenceID + INTEGER_TYPE +
                        ")";
    }
    public static abstract class HealthProblemMaster implements BaseColumns {
        public static final String TABLE_NAME = "M_HEALTH_PROBLEMS";

        public static final String COLUMN_NAME_HealthProblemID = "HealthProblemID";
        public static final String COLUMN_NAME_Description = "Description";
        public static final String COLUMN_NAME_IsActive = "IsActive";
        public static final String COLUMN_NAME_MarathiDescription = "MarathiDescription";
        public static final String COLUMN_NAME_SequenceID = "SequenceID";

        public static final String SQL_CREATE_HEALTHPROBLEM_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_HealthProblemID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Description + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MarathiDescription + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SequenceID + INTEGER_TYPE +
                        ")";
    }
    public static abstract class HabbitAddictionMaster implements BaseColumns {
        public static final String TABLE_NAME = "M_HABBIT_ADDICTION";

        public static final String COLUMN_NAME_HabbitAddictionID = "HabbitID";
        public static final String COLUMN_NAME_Description = "Description";
        public static final String COLUMN_NAME_IsActive = "IsActive";
        public static final String COLUMN_NAME_MarathiDescription = "MarathiDescription";
        public static final String COLUMN_NAME_SequenceID = "SequenceID";

        public static final String SQL_CREATE_HABBITADDICTION_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_HabbitAddictionID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Description + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MarathiDescription + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SequenceID + INTEGER_TYPE +
                        ")";
    }
    public static abstract class BusinessMaster implements BaseColumns {
        public static final String TABLE_NAME = "M_PROFESSION";

        public static final String COLUMN_NAME_BusinessID = "BusinessID";
        public static final String COLUMN_NAME_Description = "Description";
        public static final String COLUMN_NAME_IsActive = "IsActive";
        public static final String COLUMN_NAME_MarathiDescription = "MarathiDescription";
        public static final String COLUMN_NAME_SequenceID = "SequenceID";
        public static final String COLUMN_NAME_ReferenceMemberDocumentID_1 = "ReferenceMemberDocumentID_1";

        public static final String SQL_CREATE_BUSINESS_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_BusinessID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Description + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MarathiDescription + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SequenceID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ReferenceMemberDocumentID_1 + INTEGER_TYPE +
                        ")";
    }
    public static abstract class BankMaster implements BaseColumns {
        public static final String TABLE_NAME = "M_BANK";

        public static final String COLUMN_NAME_BankID = "BankID";
        public static final String COLUMN_NAME_Description = "Description";
        public static final String COLUMN_NAME_IsActive = "IsActive";
        public static final String COLUMN_NAME_SequenceID = "SequenceID";

        public static final String SQL_CREATE_BANK_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_BankID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Description + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_SequenceID + INTEGER_TYPE +
                        ")";
    }
    public static abstract class BankBranchMaster implements BaseColumns {
        public static final String TABLE_NAME = "M_BankBranch";

        public static final String COLUMN_NAME_BankBranchID = "BankBranchID";
        public static final String COLUMN_NAME_BankID = "BankID";
        public static final String COLUMN_NAME_BankBranchName = "BankBranchName";
        public static final String COLUMN_NAME_IFSCCode = "IFSCCode";
        public static final String COLUMN_NAME_IsActive = "IsActive";
        public static final String COLUMN_NAME_SequenceID = "SequenceID";

        public static final String SQL_CREATE_BANK_BRANCH_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_BankBranchID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_BankID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_BankBranchName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IFSCCode + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_SequenceID + INTEGER_TYPE +
                        ")";
    }
    public static abstract class CasteMaster implements BaseColumns {
        public static final String TABLE_NAME = "M_CASTE";

        public static final String COLUMN_NAME_CasteID = "CasteID";
        public static final String COLUMN_NAME_Description = "Description";
        public static final String COLUMN_NAME_CasteReligionID = "ReligionID";
        public static final String COLUMN_NAME_IsActive = "IsActive";
        public static final String COLUMN_NAME_CategoryID = "CategoryID";
        public static final String COLUMN_NAME_MarathiDescription = "MarathiDescription";
        public static final String COLUMN_NAME_SequenceID = "SequenceID";

        public static final String SQL_CREATE_CASTE_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_CasteID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Description + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_CasteReligionID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_CategoryID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MarathiDescription + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SequenceID + INTEGER_TYPE +
                        ")";
    }
    public static abstract class ReasonMaster implements BaseColumns {
        public static final String TABLE_NAME = "M_REASONS";

        public static final String COLUMN_NAME_ReasonID = "ReasonID";
        public static final String COLUMN_NAME_Reason = "Reason";
        public static final String COLUMN_NAME_ReasonType = "ReasonType";
        public static final String COLUMN_NAME_IsActive = "IsActive";
        public static final String COLUMN_NAME_MarathiDescription = "MarathiDescription";
        public static final String COLUMN_NAME_SequenceID = "SequenceID";

        public static final String SQL_CREATE_REASON_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ReasonID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Reason + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ReasonType + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MarathiDescription + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SequenceID + INTEGER_TYPE +
                        ")";
    }
    public static abstract class LoanUtilizationMaster implements BaseColumns {
        public static final String TABLE_NAME = "M_LOAN_UTILIZATION";

        public static final String COLUMN_NAME_LoanUtilizationID = "LoanUtilizationID";
        public static final String COLUMN_NAME_Description = "Description";
        public static final String COLUMN_NAME_LoanReasonID = "LoanReasonID";
        public static final String COLUMN_NAME_MarathiDescription = "MarathiDescription";
        public static final String COLUMN_NAME_SequenceID = "SequenceID";
//        public static final String COLUMN_NAME_ForSoftwareUse = "ForSoftwareUse";
        public static final String COLUMN_NAME_IsActive = "IsActive";

        public static final String SQL_CREATE_LOAN_UTILIZATION_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_LoanUtilizationID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Description + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanReasonID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MarathiDescription + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SequenceID + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_ForSoftwareUse + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE +
                        ")";
    }
    public static abstract class OtherBorrowingSourcesMaster implements BaseColumns {
        public static final String TABLE_NAME = "M_OTHER_BORROWING_SOURCES";

        public static final String COLUMN_NAME_BorrowingSourcesID = "BorrowingSourcesID";
        public static final String COLUMN_NAME_Description = "Description";
        public static final String COLUMN_NAME_MarathiDescription = "MarathiDescription";
        public static final String COLUMN_NAME_SequenceID = "SequenceID";
//        public static final String COLUMN_NAME_ForSoftwareUse = "ForSoftwareUse";
        public static final String COLUMN_NAME_IsActive = "IsActive";

        public static final String SQL_CREATE_OTHER_BORROWING_SOURCES_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_BorrowingSourcesID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Description + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_MarathiDescription + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SequenceID + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_ForSoftwareUse + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE +
                        ")";
    }
//    public static abstract class LoanStatusMaster implements BaseColumns {
//        public static final String TABLE_NAME = "M_LOAN_STATUS";
//
//        public static final String COLUMN_NAME_LoanStatusID = "LoanStatusID";
//        public static final String COLUMN_NAME_Description = "Description";
//        public static final String COLUMN_NAME_IsActive = "IsActive";
//
//        public static final String SQL_CREATE_LOAN_STATUS_MASTER =
//                "CREATE TABLE " + TABLE_NAME + " (" +
//                        COLUMN_NAME_LoanStatusID + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_Description + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_IsActive + INTEGER_TYPE +
//                        ")";
//    }

    public static abstract class LoanProductsMaster implements BaseColumns {
        public static final String TABLE_NAME = "M_LOAN_PRODUCT";

        public static final String COLUMN_NAME_LoanProductID = "LoanProductID";
//bmp 31-Jul-23
        public static final String COLUMN_NAME_ProductName = "ProductName";
//bmp 31-Jul-23
        public static final String COLUMN_NAME_ProductStartDate = "ProductStartDate";
        public static final String COLUMN_NAME_ProductEndDate = "ProductEndDate";
        public static final String COLUMN_NAME_JointAccountHolderRequired = "JointAccountHolderRequired";
        public static final String COLUMN_NAME_IsActive = "IsActive";

        public static final String SQL_CREATE_LOAN_PRODUCTS_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_LoanProductID + INTEGER_TYPE + COMMA_SEP +
//bmp 31-Jul-23
                        COLUMN_NAME_ProductName + TEXT_TYPE + COMMA_SEP +
//bmp 31-Jul-23
                        COLUMN_NAME_ProductStartDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ProductEndDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_JointAccountHolderRequired + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE +
                        ")";
    }
    public static abstract class LoanProductDetailsMaster implements BaseColumns {
        public static final String TABLE_NAME = "M_LOAN_PRODUCT_DETAILS";

        public static final String COLUMN_NAME_LoanProductDetailsID = "LoanProductDetailsID";
        public static final String COLUMN_NAME_LoanProductID = "LoanProductID";
        public static final String COLUMN_NAME_PrincipalAmt = "PrincipalAmt";
        public static final String COLUMN_NAME_LoanPeriodInMonths = "LoanPeriodInMonths";
        public static final String COLUMN_NAME_TotalMonthlyInstallmentAmt = "TotalMonthlyInstallmentAmt";
        public static final String COLUMN_NAME_IsActive = "IsActive";

        public static final String SQL_CREATE_LOAN_PRODUCT_DETAILS_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_LoanProductDetailsID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanProductID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PrincipalAmt + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanPeriodInMonths + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalMonthlyInstallmentAmt + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE +
                        ")";
    }

//All Transaction Tables (TD_XXX):Inserted/Edited by app and Uploaded to Server.
    public static abstract class OrgStructure implements BaseColumns {
        public static final String TABLE_NAME = "TD_ORG_STRUCTURE";

        public static final String COLUMN_NAME_ORGStructureID = "ORGStructureID";
        public static final String COLUMN_NAME_NodeName = "NodeName";
        public static final String COLUMN_NAME_NodeLevel = "NodeLevel";
        public static final String COLUMN_NAME_ParentID = "ParentID";
        public static final String COLUMN_NAME_ParentLevel = "ParentLevel";
        public static final String COLUMN_NAME_ChildsAllowed = "ChildsAllowed";
        public static final String COLUMN_NAME_MarathiName = "MarathiName";
        public static final String COLUMN_NAME_BranchNumber = "BranchNumber";
        public static final String COLUMN_NAME_IsActive = "IsActive";
        public static final String COLUMN_NAME_Remark = "Remark";

        public static final String SQL_CREATE_TD_ORG_STRUCTURE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ORGStructureID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_NodeName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_NodeLevel + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ParentID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ParentLevel + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ChildsAllowed + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MarathiName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_BranchNumber + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Remark + TEXT_TYPE +
                        ")";
    }
    public static abstract class MemberData implements BaseColumns {
        public static final String TABLE_NAME = "M_MEMBER";

        public static final String COLUMN_NAME_MemberID = "MemberID";
        public static final String COLUMN_NAME_MemberNumber = "MemberNumber";
        public static final String COLUMN_NAME_Fname = "FName";
        public static final String COLUMN_NAME_Mname = "MName";
        public static final String COLUMN_NAME_Lname = "LName";
        public static final String COLUMN_NAME_BirthDate = "BirthDate";
        public static final String COLUMN_NAME_Gender = "Gender";
        public static final String COLUMN_NAME_JoiningDate = "JoiningDate";
        public static final String COLUMN_NAME_ContactNo = "ContactNo";
        public static final String COLUMN_NAME_SavingAccNo = "SavingAccNo";
        public static final String COLUMN_NAME_AddressID = "AddressID";
        public static final String COLUMN_NAME_BranchID = "BranchID";
        public static final String COLUMN_NAME_PrevMemberID = "PrevMemberID";
        public static final String COLUMN_NAME_DeathDate = "DeathDate";
        public static final String COLUMN_NAME_IsActive = "IsActive";
        public static final String COLUMN_NAME_Title = "Title";
        public static final String COLUMN_NAME_CustomerTypeID = "CustomerTypeID";
        public static final String COLUMN_NAME_GroupID = "GroupID";
        public static final String COLUMN_NAME_Email = "Email";
        public static final String COLUMN_NAME_ReligionID = "ReligionID";
        public static final String COLUMN_NAME_CasteID = "CasteID";
        public static final String COLUMN_NAME_MaritalStatusID = "MaritalStatusID";
        public static final String COLUMN_NAME_RelationID = "RelationID";
        public static final String COLUMN_NAME_MainMemberID = "MainMemberID";
        public static final String COLUMN_NAME_FamilyHead = "FamilyHead";
        public static final String COLUMN_NAME_HMFApplicable = "HMFApplicabel";
        public static final String COLUMN_NAME_EncodedBy = "EncodedBy";
        public static final String COLUMN_NAME_EncodedDate = "EncodedDate";
        public static final String COLUMN_NAME_DeletionReasonID = "DeletionReasonID";
        public static final String COLUMN_NAME_MemberFamilyDetailsID = "MemberFamilyDetailsID";
        public static final String COLUMN_NAME_BankID = "BankID";
        public static final String COLUMN_NAME_BankAccountNumber = "BankAccountNumber";
        public static final String COLUMN_NAME_Bank_Fname = "Bank_FName";
        public static final String COLUMN_NAME_Bank_Mname = "Bank_MName";
        public static final String COLUMN_NAME_Bank_Lname = "Bank_LName";
        public static final String COLUMN_NAME_IsMaidenName = "IsMaidenName";
        public static final String COLUMN_NAME_MaidenORMarriedName = "MaidenORMarriedName";
        public static final String COLUMN_NAME_RationCard_Fname = "RatioCard_FName";
        public static final String COLUMN_NAME_RationCard_Mname = "RatioCard_MName";
        public static final String COLUMN_NAME_RationCard_Lname = "RatioCard_LName";
        public static final String COLUMN_NAME_BankBranchID = "BankBranchID";
        public static final String COLUMN_NAME_DeactivateDate = "DeactivateDate";
        public static final String COLUMN_NAME_GroupGuarantor = "GroupGuarantor";

        public static final String SQL_CREATE_MEMBER_DATA =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_MemberID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberNumber + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Fname + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Mname + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Lname + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_BirthDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Gender + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_JoiningDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ContactNo + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SavingAccNo + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_AddressID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_BranchID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PrevMemberID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_DeathDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Title + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_CustomerTypeID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_GroupID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Email + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ReligionID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_CasteID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MaritalStatusID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_RelationID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MainMemberID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_FamilyHead + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_HMFApplicable + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_EncodedBy + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_EncodedDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_DeletionReasonID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberFamilyDetailsID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_BankID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_BankAccountNumber + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Bank_Fname + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Bank_Mname + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Bank_Lname + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsMaidenName + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MaidenORMarriedName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_RationCard_Fname + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_RationCard_Mname + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_RationCard_Lname + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_BankBranchID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_GroupGuarantor + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_DeactivateDate + TEXT_TYPE +
                        ")";
    }
    public static abstract class MemberFamilyDetails implements BaseColumns {
        public static final String TABLE_NAME = "M_MEMBER_FAMILY_DETAILS";

        public static final String COLUMN_NAME_MemberFamilyDetailsID = "MemberFamilyDetailsID";
        public static final String COLUMN_NAME_MemberID = "MemberID";
        public static final String COLUMN_NAME_RefereceMemberID = "RefereceMemberID";
        public static final String COLUMN_NAME_LoanAllowed = "LoanAllowed";
        public static final String COLUMN_NAME_LoanNotAllowedReason = "LoanNotAllowedReason";
        public static final String COLUMN_NAME_Remarks = "Remarks";
        public static final String COLUMN_NAME_MonthlyBhishi = "MonthlyBhishi";
        public static final String COLUMN_NAME_MedicalExpenses = "MedicalExpenses";
        public static final String COLUMN_NAME_MonthlyInstallments = "MonthlyInstallments";
        public static final String COLUMN_NAME_RationCardNo = "RationCardNo";
        public static final String COLUMN_NAME_RationCardType = "RationCardType";
        public static final String COLUMN_NAME_WhoseContactNo = "WhoseContactNo";
        public static final String COLUMN_NAME_WhoRunsBusiness = "WhoRunsBusiness";
        public static final String COLUMN_NAME_AddressOnRationcard = "AddressOnRationcard";
        public static final String COLUMN_NAME_LoanNotAllowedReasonId = "LoanNotAllowedReasonId";

        public static final String SQL_CREATE_MEMBER_FAMILY_DETAILS =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_MemberFamilyDetailsID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_RefereceMemberID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanAllowed + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanNotAllowedReason + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Remarks + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_MonthlyBhishi + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MedicalExpenses + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MonthlyInstallments + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_RationCardNo + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_RationCardType + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_WhoseContactNo + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_WhoRunsBusiness + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_AddressOnRationcard + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanNotAllowedReasonId + INTEGER_TYPE +
                        ")";
    }
    public static abstract class PersonalImpactInfo implements BaseColumns {
        public static final String TABLE_NAME = "TD_Member_Personal_Impact_Info";

        public static final String COLUMN_NAME_MemberDetailsForImpactID = "MemberDetailsForImpactID";
        public static final String COLUMN_NAME_MemberID = "MemberID";
        public static final String COLUMN_NAME_MonthlyIncome = "MonthlyIncome";
        public static final String COLUMN_NAME_YearlyIncome = "YearlyIncome";
        public static final String COLUMN_NAME_BusinessID = "BusinessID";
        public static final String COLUMN_NAME_QualificationID = "QualificationID";
        public static final String COLUMN_NAME_HabitID = "HabitID";
        public static final String COLUMN_NAME_HealthProblemID = "HealthProblemID";
        public static final String COLUMN_NAME_OwnHouse = "OwnHouse";
        public static final String COLUMN_NAME_EncodedBy = "EncodedBy";
        public static final String COLUMN_NAME_ImpactEncodedDate = "ImpactEncodedDate";
        public static final String COLUMN_NAME_UpdatedBy = "UpdatedBy";
        public static final String COLUMN_NAME_ImpactUpdatedDate = "ImpactUpdatedDate";
        public static final String COLUMN_NAME_IsActive = "IsActive";
        public static final String COLUMN_NAME_OtherMonthlyIncome = "OtherMonthlyIncome";

        public static final String SQL_CREATE_PERSONAL_IMPACT_INFO =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_MemberDetailsForImpactID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MonthlyIncome + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_YearlyIncome + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_BusinessID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_QualificationID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_HabitID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_HealthProblemID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OwnHouse + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_EncodedBy + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ImpactEncodedDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_UpdatedBy + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ImpactUpdatedDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherMonthlyIncome + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE +
                        ")";
    }
    public static abstract class FamilyImpactInfo implements BaseColumns {
        public static final String TABLE_NAME = "TD_Family_Impact_Info";

        public static final String COLUMN_NAME_MFamilyImpactID = "MFamilyImpactID";
        public static final String COLUMN_NAME_MemberFamilyDetailsID = "MemberFamilyDetailsID";
        public static final String COLUMN_NAME_PerCapitaIncome = "PerCapitaIncome";
        public static final String COLUMN_NAME_HouseRentExpense = "HouseRentExpense";
        public static final String COLUMN_NAME_FoodExpense = "FoodExpense";
        public static final String COLUMN_NAME_EducationExpense = "EducationExpense";
        public static final String COLUMN_NAME_OtherExpenses = "OtherExpenses";
        public static final String COLUMN_NAME_EncodedBy = "EncodedBy";
        public static final String COLUMN_NAME_ImpactEncodedDate = "ImpactEncodedDate";
        public static final String COLUMN_NAME_UpdatedBy = "UpdatedBy";
        public static final String COLUMN_NAME_ImpactUpdatedDate = "ImpactUpdatedDate";
        public static final String COLUMN_NAME_IsActive = "IsActive";

        public static final String SQL_CREATE_FAMILY_IMPACT_INFO =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_MFamilyImpactID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberFamilyDetailsID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PerCapitaIncome + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_HouseRentExpense + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_FoodExpense + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_EducationExpense + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherExpenses + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_EncodedBy + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ImpactEncodedDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_UpdatedBy + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ImpactUpdatedDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE +
                        ")";
    }
    public static abstract class MemberAddress implements BaseColumns {
        public static final String TABLE_NAME = "M_ADDRESS";

        public static final String COLUMN_NAME_AddressID = "AddressId";
        public static final String COLUMN_NAME_Address = "Address";
        public static final String COLUMN_NAME_Pincode = "PinCode";
        public static final String COLUMN_NAME_DigiPin = "DigiPin";
        public static final String COLUMN_NAME_IsActive = "IsActive";
        public static final String COLUMN_NAME_DistrictID = "DistrictId";

        public static final String SQL_CREATE_MEMBER_ADDRESS =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_AddressID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Address + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Pincode + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_DigiPin + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_DistrictID + TEXT_TYPE +
                        ")";
    }
    public static abstract class MemberDocRcvd implements BaseColumns {
        public static final String TABLE_NAME = "TD_Member_Documents";

        public static final String COLUMN_NAME_MemberDocuemntsRcvdID = "MemberDocuemntsRcvdID";
        public static final String COLUMN_NAME_MemberID = "MemberID";
        public static final String COLUMN_NAME_MemberDocumentsID = "MemberDocumentsID";
        public static final String COLUMN_NAME_DocumentNo = "DocumentNo";
        public static final String COLUMN_NAME_IsActive = "IsActive";
        public static final String COLUMN_NAME_IsAgeProofDoc = "IsAgeProofDoc";

        public static final String SQL_CREATE_MEMBER_DOCUMENTS =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_MemberDocuemntsRcvdID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberDocumentsID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_DocumentNo + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsAgeProofDoc + INTEGER_TYPE +
                        ")";
    }
    public static abstract class DocumentPhoto implements BaseColumns {
        public static final String TABLE_NAME = "TD_Document_Photo";

        public static final String COLUMN_NAME_DocumentPhotoID = "DocumentPhotoID";
        public static final String COLUMN_NAME_MemberDocuemntsRcvdID = "MemberDocuemntsRcvdID";
        public static final String COLUMN_NAME_GroupID = "GroupID";
        public static final String COLUMN_NAME_IsActive = "IsActive";
        public static final String COLUMN_NAME_MemberDocumentsID = "MemberDocumentsID";

        public static final String SQL_CREATE_DOCUMENT_PHOTO =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_DocumentPhotoID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberDocuemntsRcvdID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_GroupID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberDocumentsID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE +
                        ")";
    }
    public static abstract class NomineeDetails implements BaseColumns {
        public static final String TABLE_NAME = "TD_LMF_Nominee_Details";

        public static final String COLUMN_NAME_LMFNomineeDetailsID = "LMFNomineeDetailsID";
        public static final String COLUMN_NAME_MemberID = "MemberID";
        public static final String COLUMN_NAME_NomineeIsFamilyMember = "NomineeIsFamilyMember";
        public static final String COLUMN_NAME_NomineeID = "NomineeID";
        public static final String COLUMN_NAME_Guardian_FName = "Guardian_FName";
        public static final String COLUMN_NAME_Guardian_MName = "Guardian_MName";
        public static final String COLUMN_NAME_Guardian_LName = "Guardian_LName";
        public static final String COLUMN_NAME_GuardianAddress = "GuardianAddress";
        public static final String COLUMN_NAME_NomineeStartDate = "NomineeStartDate";
        public static final String COLUMN_NAME_NomineeEndDate = "NomineeEndDate";
        public static final String COLUMN_NAME_IsActive = "IsActive";

        public static final String SQL_CREATE_NOMINEE_DETAILS =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_LMFNomineeDetailsID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_NomineeIsFamilyMember + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_NomineeID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Guardian_FName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Guardian_MName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Guardian_LName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_GuardianAddress + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_NomineeStartDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_NomineeEndDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE +
                        ")";
    }
    public static abstract class OtherNominee implements BaseColumns {
        public static final String TABLE_NAME = "M_OTHER_NOMINEE";

        public static final String COLUMN_NAME_NomineeID = "NomineeID";
        public static final String COLUMN_NAME_MemberID = "MemberID";
        public static final String COLUMN_NAME_FName = "FName";
        public static final String COLUMN_NAME_MName = "MName";
        public static final String COLUMN_NAME_LName = "LName";
        public static final String COLUMN_NAME_AddressID = "AddressID";
        public static final String COLUMN_NAME_RelationID = "RelationID";
        public static final String COLUMN_NAME_ContactNo = "ContactNo";
        public static final String COLUMN_NAME_DateOfBirth = "DateOfBirth";
        public static final String COLUMN_NAME_IsActive = "IsActive";
        public static final String COLUMN_NAME_BankBranchID = "BankBranchID";
        public static final String COLUMN_NAME_BankAccountNumber = "BankAccountNumber";
        public static final String COLUMN_NAME_Bank_FName = "Bank_FName";
        public static final String COLUMN_NAME_Bank_MName = "Bank_MName";
        public static final String COLUMN_NAME_Bank_LName = "Bank_LName";

        public static final String SQL_CREATE_OTHER_NOMINEE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_NomineeID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_FName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_MName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_AddressID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_RelationID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ContactNo + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_DateOfBirth + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_BankBranchID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_BankAccountNumber + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Bank_FName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Bank_MName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Bank_LName + TEXT_TYPE +
                        ")";
    }
    public static abstract class MemberImpactAnswer implements BaseColumns {
        public static final String TABLE_NAME = "TD_Member_Impact_Answers";

        public static final String COLUMN_NAME_MemberImpactAnswerID = "MemberImpactAnswerID";
        public static final String COLUMN_NAME_MemberFamilyDetailsID = "MemberFamilyDetailsID";
        public static final String COLUMN_NAME_IPAnswerID = "IPAnswerID";
        public static final String COLUMN_NAME_EncodedBy = "EncodedBy";
        public static final String COLUMN_NAME_ImpactEncodedDate = "ImpactEncodedDate";
        public static final String COLUMN_NAME_UpdatedBy = "UpdatedBy";
        public static final String COLUMN_NAME_ImpactUpdatedDate = "ImpactUpdatedDate";
        public static final String COLUMN_NAME_IsActive = "IsActive";

        public static final String SQL_CREATE_MEMBER_IMPACT_ANSWERS =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_MemberImpactAnswerID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberFamilyDetailsID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_IPAnswerID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_EncodedBy + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ImpactEncodedDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_UpdatedBy + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ImpactUpdatedDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsActive + INTEGER_TYPE +
                        ")";
    }
    public static abstract class NewGroupMemberInfo implements BaseColumns {
        public static final String TABLE_NAME = "TD_NewGroupMember_Info";

        public static final String COLUMN_NAME_MemberID = "MemberID";
        public static final String COLUMN_NAME_GuarantorName = "GuarantorName";
        public static final String COLUMN_NAME_GuarantorRelationID = "GuarantorRelationID";
        public static final String COLUMN_NAME_LoanAmount = "LoanAmount";
        public static final String COLUMN_NAME_LoanDuration = "LoanDuration";
        public static final String COLUMN_NAME_MemberStatus = "InfoStatus";
        public static final String COLUMN_NAME_LoanApplicant = "LoanApplicant";
        public static final String COLUMN_NAME_ContactNo = "ContactNo";
        public static final String COLUMN_NAME_GroupID = "GroupID";
        public static final String COLUMN_NAME_iEligibleLoan = "iEligibleLoan";
        public static final String COLUMN_NAME_selectedPositionLoanProduct = "selectedPositionLoanProduct";

        public static final String SQL_CREATE_NEWGROUPMEMBER_INFO =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_MemberID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_GuarantorName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_GuarantorRelationID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanAmount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanDuration + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberStatus + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanApplicant + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ContactNo + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_GroupID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_iEligibleLoan + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_selectedPositionLoanProduct + INTEGER_TYPE +
                        ")";
    }
    public static abstract class NewGroupInfo implements BaseColumns {
        public static final String TABLE_NAME = "TD_NewGroup_Info";

        public static final String COLUMN_NAME_GroupID = "GroupID";
        public static final String COLUMN_NAME_LoanDisbursementDate = "LoanDisbursementDate";
        public static final String COLUMN_NAME_LoanDisbursementTime = "LoanDisbursementTime";
        public static final String COLUMN_NAME_LoanRepayInstallmentDate = "LoanRepayInstallmentDate";
        public static final String COLUMN_NAME_LoanRepayInstallmentTime = "LoanRepayInstallmentTime";
        public static final String COLUMN_NAME_GroupStatus = "GRTStatus";
        public static final String COLUMN_NAME_GroupTestDate = "GroupTestDate";
        public static final String COLUMN_NAME_GroupTestTime = "GroupTestTime";
        public static final String COLUMN_NAME_PIFdate = "PIFdate";
        public static final String COLUMN_NAME_PIFtime = "PIFtime";
        public static final String COLUMN_NAME_CollectionCenterID = "CollectionCenterID";
        public static final String COLUMN_NAME_CollectionMemberID = "CollectionMemberID";
        public static final String COLUMN_NAME_GradeOfGroup = "GradeOfGroup";
        public static final String COLUMN_NAME_MemberCount = "MemberCount";

        public static final String SQL_CREATE_NEWGROUP_INFO =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_GroupID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanDisbursementDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanDisbursementTime + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanRepayInstallmentDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanRepayInstallmentTime + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_GroupStatus + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_GroupTestDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_GroupTestTime + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PIFdate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PIFtime + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_CollectionCenterID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_CollectionMemberID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_GradeOfGroup + TEXT_TYPE +
                        ")";
    }
    public static abstract class MemberAdditionalDetails implements BaseColumns {
        public static final String TABLE_NAME = "TD_MemberAdditionalDetails";

        public static final String COLUMN_NAME_MemberID                     = "MemberID";
        public static final String COLUMN_NAME_NomineeGuardianBusinessID    = "NomineeGuardianBusinessID";
        public static final String COLUMN_NAME_NomineeGuardianContactNumber = "NomineeGuardianContactNumber";
        public static final String COLUMN_NAME_NomineeGuardianRelationID    = "NomineeGuardianRelationID";
        public static final String COLUMN_NAME_NomineeBusinessID            = "NomineeBusinessID";

        public static final String COLUMN_NAME_LoanProductDetailsID         = "LoanProductDetailsID";
//bmp 23-Oct-23
        public static final String COLUMN_NAME_LoanProductDetailsID1        = "LoanProductDetailsID1";
        public static final String COLUMN_NAME_LoanProductDetailsID2        = "LoanProductDetailsID2";
        public static final String COLUMN_NAME_LoanProductDetailsID3        = "LoanProductDetailsID3";
        public static final String COLUMN_NAME_LoanProductDetailsID4        = "LoanProductDetailsID4";
//bmp 23-Oct-23
        public static final String COLUMN_NAME_LoanReasonID                 = "LoanReasonID";

        public static final String COLUMN_NAME_GuarantorType                = "GuarantorType";
        public static final String COLUMN_NAME_LoanGuarantorID              = "LoanGuarantorID";

        public static final String COLUMN_NAME_OtherGuarantorBusinessID          = "OtherGuarantorBusinessID";
        public static final String COLUMN_NAME_OtherGuarantorContactNumber       = "OtherGuarantorContactNumber";
        public static final String COLUMN_NAME_OtherGuarantorFName               = "OtherGuarantorFName";
        public static final String COLUMN_NAME_OtherGuarantorMName               = "OtherGuarantorMName";
        public static final String COLUMN_NAME_OtherGuarantorLName               = "OtherGuarantorLName";
        public static final String COLUMN_NAME_OtherGuarantorMonthlyIncome       = "OtherGuarantorMonthlyIncome";
        public static final String COLUMN_NAME_OtherGuarantorRelationID          = "OtherGuarantorRelationID";
        public static final String COLUMN_NAME_OtherGuarantorAddressID      = "OtherGuarantorAddressID";
        public static final String COLUMN_NAME_OtherGuarantorBirthDate      = "OtherGuarantorBirthDate";
        public static final String COLUMN_NAME_OtherGuarantorReasonID       = "OtherGuarantorReasonID";

        public static final String COLUMN_NAME_JointLoaneeID                    = "JointLoaneeID";
        public static final String COLUMN_NAME_OtherJointLoaneeAddressID        = "OtherJointLoaneeAddressID";
        public static final String COLUMN_NAME_OtherJointLoaneeBirthDate        = "OtherJointLoaneeBirthDate";
        public static final String COLUMN_NAME_OtherJointLoaneeContactNumber    = "OtherJointLoaneeContactNumber";
        public static final String COLUMN_NAME_OtherJointLoaneeFName            = "OtherJointLoaneeFName";
        public static final String COLUMN_NAME_OtherJointLoaneeMName            = "OtherJointLoaneeMName";
        public static final String COLUMN_NAME_OtherJointLoaneeLName            = "OtherJointLoaneeLName";
        public static final String COLUMN_NAME_OtherJointLoaneeMonthlyIncome    = "OtherJointLoaneeMonthlyIncome";
        public static final String COLUMN_NAME_OtherJointLoaneeProfessionID     = "OtherJointLoaneeProfessionID";
        public static final String COLUMN_NAME_OtherJointLoaneeReasonID         = "OtherJointLoaneeReasonID";
        public static final String COLUMN_NAME_OtherJointLoaneeRelationID       = "OtherJointLoaneeRelationID";

        public static final String COLUMN_NAME_DeficitFundResourceID            = "DeficitFundResourceID";
        public static final String COLUMN_NAME_LoanUtilizationID1               = "LoanUtilizationID1";
        public static final String COLUMN_NAME_LoanUtilizationID2               = "LoanUtilizationID2";
        public static final String COLUMN_NAME_LoanUtilizationID3               = "LoanUtilizationID3";
        public static final String COLUMN_NAME_LoanUtilizationID4               = "LoanUtilizationID4";
        public static final String COLUMN_NAME_OtherLoan                        = "OtherLoan";
        public static final String COLUMN_NAME_OtherLoanAmount1                 = "OtherLoanAmount1";
        public static final String COLUMN_NAME_OtherLoanAmount2                 = "OtherLoanAmount2";
        public static final String COLUMN_NAME_OtherLoanBankID1                 = "OtherLoanBankID1";
        public static final String COLUMN_NAME_OtherLoanBankID2                 = "OtherLoanBankID2";
        public static final String COLUMN_NAME_OtherLoanInstallment1            = "OtherLoanInstallment1";
        public static final String COLUMN_NAME_OtherLoanInstallment2            = "OtherLoanInstallment2";
        public static final String COLUMN_NAME_OtherLoanRepayMonths1            = "OtherLoanRepayMonths1";
        public static final String COLUMN_NAME_OtherLoanRepayMonths2            = "OtherLoanRepayMonths2";
        public static final String COLUMN_NAME_Monthly_Surplus                  = "Monthly_Surplus";

        public static final String SQL_CREATE_MEMBER_ADDITIONAL_DETAILS =
                "CREATE TABLE " + TABLE_NAME + " (" +

                        COLUMN_NAME_MemberID + INTEGER_TYPE + COMMA_SEP +

                        COLUMN_NAME_NomineeGuardianBusinessID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_NomineeGuardianContactNumber + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_NomineeGuardianRelationID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_NomineeBusinessID + INTEGER_TYPE + COMMA_SEP +

                        COLUMN_NAME_LoanProductDetailsID + INTEGER_TYPE + COMMA_SEP +
//bmp 23-Oct-23
                        COLUMN_NAME_LoanProductDetailsID1 + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanProductDetailsID2 + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanProductDetailsID3 + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanProductDetailsID4 + INTEGER_TYPE + COMMA_SEP +
//bmp 23-Oct-23
                        COLUMN_NAME_LoanReasonID + INTEGER_TYPE + COMMA_SEP +

                        COLUMN_NAME_GuarantorType + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanGuarantorID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherGuarantorBusinessID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherGuarantorContactNumber + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherGuarantorFName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherGuarantorMName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherGuarantorLName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherGuarantorMonthlyIncome + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherGuarantorRelationID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherGuarantorAddressID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherGuarantorBirthDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherGuarantorReasonID + INTEGER_TYPE + COMMA_SEP +

                        COLUMN_NAME_JointLoaneeID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherJointLoaneeAddressID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherJointLoaneeBirthDate + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherJointLoaneeContactNumber + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherJointLoaneeFName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherJointLoaneeMName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherJointLoaneeLName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherJointLoaneeMonthlyIncome + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherJointLoaneeProfessionID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherJointLoaneeReasonID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherJointLoaneeRelationID + INTEGER_TYPE + COMMA_SEP +

                        COLUMN_NAME_DeficitFundResourceID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanUtilizationID1 + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanUtilizationID2 + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanUtilizationID3 + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanUtilizationID4 + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherLoan + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherLoanAmount1 + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherLoanAmount2 + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherLoanBankID1 + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherLoanBankID2 + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherLoanInstallment1 + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherLoanInstallment2 + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherLoanRepayMonths1 + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OtherLoanRepayMonths2 + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Monthly_Surplus + INTEGER_TYPE +
                        ")";
    }
//    public static abstract class LoanGuarantorForTab implements BaseColumns {
//        public static final String TABLE_NAME = "TD_LoanGuarantor_ForTab";
//
//        public static final String COLUMN_NAME_LoanID = "LoanID";
//        public static final String COLUMN_NAME_LoanGuarantorID = "LoanGuarantorID";
//        public static final String COLUMN_NAME_FName = "FName";
//        public static final String COLUMN_NAME_MName = "MName";
//        public static final String COLUMN_NAME_LName = "LName";
//        public static final String COLUMN_NAME_BirthDate = "BirthDate";
//        public static final String COLUMN_NAME_RelationID = "RelationID";
//        public static final String COLUMN_NAME_BusinessID = "BusinessID";
//        public static final String COLUMN_NAME_AddressID = "AddressID";
//        public static final String COLUMN_NAME_ContactNo = "ContactNo";
//        public static final String COLUMN_NAME_MonthlyIncome = "MonthlyIncome";
//
//        public static final String SQL_CREATE_LOANGUARANTOR_FORTAB =
//                "CREATE TABLE " + TABLE_NAME + " (" +
//                        COLUMN_NAME_LoanID + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_LoanGuarantorID + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_FName + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_MName + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_LName + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_BirthDate + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_RelationID + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_BusinessID + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_AddressID + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_ContactNo + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_MonthlyIncome + INTEGER_TYPE +
//                        ")";
//    }
    public static abstract class TablesToSync implements BaseColumns {
        public static final String TABLE_NAME = "TABLES_TO_SYNC";

        public static final String COLUMN_NAME_TableSyncID = "TableSyncID";
        public static final String COLUMN_NAME_DbTableName = "DbTableName";
        public static final String COLUMN_NAME_LastSyncTimestamp = "LastSyncTimestamp";
        public static final String COLUMN_NAME_LastSyncRecordIndex = "LastSyncRecordIndex";

        public static final String SQL_CREATE_TABLES_TO_SYNC =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_TableSyncID + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_DbTableName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LastSyncTimestamp + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LastSyncRecordIndex + INTEGER_TYPE +
                        ")";
    }
    public static abstract class RecordsToSync implements BaseColumns {
        public static final String TABLE_NAME = "RECORDS_TO_SYNC";

        public static final String COLUMN_NAME_SyncID = "SyncID";
        public static final String COLUMN_NAME_DbTableName = "DbTableName";
        public static final String COLUMN_NAME_updatedRecordID = "updatedRecordID";
        public static final String COLUMN_NAME_IsNewRecord = "IsNewRecord";
        public static final String COLUMN_NAME_IsReady = "IsReady";
        public static final String COLUMN_NAME_PostDataParams = "PostDataParams";

        public static final String SQL_CREATE_RECORDS_TO_SYNC =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_SyncID + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_DbTableName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_updatedRecordID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsNewRecord + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsReady + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PostDataParams + TEXT_TYPE +
                        ")";
    }
    public static abstract class MessagesInbox implements BaseColumns {
        public static final String TABLE_NAME = "MESSAGES_INBOX";

        public static final String COLUMN_NAME_MessageID = "MessageID";
        public static final String COLUMN_NAME_Message = "Message";
        public static final String COLUMN_NAME_MessageFilter = "MessageFilter";

        public static final String SQL_CREATE_MESSAGES_INBOX =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_MessageID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Message + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_MessageFilter + TEXT_TYPE +
                        ")";
    }
    public static abstract class SearchMemberData implements BaseColumns {
        public static final String TABLE_NAME = "TD_SearchMemberData";

        public static final String COLUMN_NAME_AadhaarcardNumber = "AadhaarcardNumber";
        public static final String COLUMN_NAME_RationcardNumber = "RationcardNumber";
        public static final String COLUMN_NAME_SearchStatus = "SearchStatus";

        public static final String SQL_CREATE_SEARCH_MEMBERDATA =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_AadhaarcardNumber + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_RationcardNumber + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SearchStatus + TEXT_TYPE +
                        ")";
    }

    public static abstract class PunarbhetSchedule implements BaseColumns {
        public static final String TABLE_NAME = "TD_PromotionLists";

        public static final String COLUMN_NAME_ClusterID = "ClusterID";
        public static final String COLUMN_NAME_Cluster = "Cluster";
        public static final String COLUMN_NAME_CommunityID = "CommunityID";
        public static final String COLUMN_NAME_Community = "Community";
        public static final String COLUMN_NAME_CenterID = "CenterID";
        public static final String COLUMN_NAME_GroupID = "GroupID";
        public static final String COLUMN_NAME_GroupName = "GroupName";
        public static final String COLUMN_NAME_MemberID = "MemberID";
        public static final String COLUMN_NAME_MemberName = "MemberName";
        public static final String COLUMN_NAME_SavingAccNo = "SavingAccNo";
        public static final String COLUMN_NAME_ContactNumber = "ContactNumber";
        public static final String COLUMN_NAME_ListType = "ListType";
        public static final String COLUMN_NAME_LastLoanAmount = "LastLoanAmount";
        public static final String COLUMN_NAME_NewLoanAmount = "NewLoanAmount";
//        public static final String COLUMN_NAME_PrevMaxLoanAmount = "PrevMaxLoanAmount";
        public static final String COLUMN_NAME_HomeLoanAmount = "HomeLoanAmount";
        public static final String COLUMN_NAME_LoanCycle = "LoanCycle";
        public static final String COLUMN_NAME_MaritalStatusID = "MaritalStatusID";
        public static final String COLUMN_NAME_OwnHouse = "OwnHouse";
        public static final String COLUMN_NAME_CreditBureauCheck = "CreditBureauCheck";
        public static final String COLUMN_NAME_VisitDate = "VisitDate";
        public static final String COLUMN_NAME_LastVisitDetailsID = "LastVisitDetailsID";
        public static final String COLUMN_NAME_BusinessID = "BusinessID";
        public static final String COLUMN_NAME_LoanGuarantorName = "LoanGuarantorName";
        public static final String COLUMN_NAME_LoanGuarantorRelationID = "LoanGuarantorRelationID";
        public static final String COLUMN_NAME_LastCenterID = "LastCenterID";
        public static final String COLUMN_NAME_LastGroupID = "LastGroupID";
        public static final String COLUMN_NAME_MaturityDate = "MaturityDate";
        public static final String COLUMN_NAME_LongMaturityDate = "LongMaturityDate";
        public static final String COLUMN_NAME_BalanceAmount = "BalanceAmount";
        public static final String COLUMN_NAME_OverdueAmount = "OverdueAmount";
        public static final String COLUMN_NAME_ComMeetingDate = "ComMeetingDate";
        public static final String COLUMN_NAME_LoanDisbursementDate = "LoanDisbursementDate";

        public static final String COLUMN_NAME_incentiveGenuineBTypeGroup = "incentiveGenuineBTypeGroup";
        public static final String COLUMN_NAME_incentiveRegularLoanRepay = "incentiveRegularLoanRepay";
        public static final String COLUMN_NAME_incentiveNotWithdrawingSavingsShares = "incentiveNotWithdrawingSavingsShares";
        public static final String COLUMN_NAME_incentiveRegularRDpay = "incentiveRegularRDpay";
        public static final String COLUMN_NAME_incentiveRegularADHPpay = "incentiveRegularADHPpay";
        public static final String COLUMN_NAME_incentiveAGMAttendance = "incentiveAGMAttendance";

        public static final String COLUMN_NAME_ActualVisitDate = "ActualVisitDate";
        public static final String COLUMN_NAME_VisitDetailsID = "VisitDetailsID";
        public static final String COLUMN_NAME_RevisitStatus = "RevisitStatus";
        public static final String COLUMN_NAME_LastCycleGroupContinued = "LastCycleGroupContinued";

        public static final String COLUMN_NAME_incentiveCommunityMeetingNoAttendance = "incentiveCommunityMeetingNoAttendance";
        public static final String COLUMN_NAME_incentiveCommunityMeetingPartAttendance = "incentiveCommunityMeetingPartAttendance";
        public static final String COLUMN_NAME_incentiveCommunityMeetingFullAttendance = "incentiveCommunityMeetingFullAttendance";
        public static final String COLUMN_NAME_NextLoanOfferAmount = "NextLoanOfferAmount";
        public static final String COLUMN_NAME_incentiveGenuineGradeGroup = "incentiveGenuineGradeGroup";
        public static final String COLUMN_NAME_incentiveLowPrevMaxLoanAmount = "incentiveLowPrevMaxLoanAmount";
        public static final String COLUMN_NAME_flagNewMember = "flagNewMember";
        public static final String COLUMN_NAME_UpFrontJLGBalance = "UpFrontJLGBalance";
        public static final String COLUMN_NAME_CurrentJLGBalance = "CurrentJLGBalance";
//bmp 25-Mar-26
        public static final String COLUMN_NAME_CBTotalLoanBalance = "CBTotalLoanBalance";
        public static final String COLUMN_NAME_CBNoOfActiveLoans = "CBNoOfActiveLoans";
        public static final String COLUMN_NAME_CBScore = "CBScore";
        public static final String COLUMN_NAME_CBRemark = "CBRemark";
        public static final String COLUMN_NAME_EnquiryDate = "EnquiryDate";
        public static final String COLUMN_NAME_LastLoanStatus = "LastLoanStatus";
//bmp 25-Mar-26
        public static final String SQL_CREATE_PUNARBHET_SCHEDULE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ClusterID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Cluster + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_CommunityID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Community + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_CenterID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_GroupID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_GroupName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SavingAccNo + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ContactNumber + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ListType + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LastLoanAmount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_NewLoanAmount + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PrevMaxLoanAmount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_HomeLoanAmount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanCycle + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MaritalStatusID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OwnHouse + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_CreditBureauCheck + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_VisitDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LastVisitDetailsID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_BusinessID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanGuarantorName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanGuarantorRelationID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LastCenterID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LastGroupID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MaturityDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LongMaturityDate + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_BalanceAmount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OverdueAmount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ComMeetingDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanDisbursementDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_incentiveGenuineBTypeGroup + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_incentiveRegularLoanRepay + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_incentiveNotWithdrawingSavingsShares + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_incentiveRegularRDpay + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_incentiveRegularADHPpay + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_incentiveAGMAttendance + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ActualVisitDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_VisitDetailsID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_RevisitStatus + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LastCycleGroupContinued + INTEGER_TYPE + COMMA_SEP +

                        COLUMN_NAME_incentiveCommunityMeetingNoAttendance + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_incentiveCommunityMeetingPartAttendance + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_incentiveCommunityMeetingFullAttendance + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_NextLoanOfferAmount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_incentiveGenuineGradeGroup + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_incentiveLowPrevMaxLoanAmount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_UpFrontJLGBalance + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_CurrentJLGBalance + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_CBTotalLoanBalance + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_CBNoOfActiveLoans + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_CBScore + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_CBRemark + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_EnquiryDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LastLoanStatus + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_flagNewMember + INTEGER_TYPE +
                        ")";
    }
    public static abstract class PunarbhetVisitDetailsMatser implements BaseColumns {
        public static final String TABLE_NAME = "M_Punarbhet_VisitDetails";

        public static final String COLUMN_NAME_VisitDetailsID = "VisitDetailsID";
        public static final String COLUMN_NAME_VisitDetails = "VisitDetails";
        public static final String COLUMN_NAME_VisitDetailsInMarathi = "VisitDetailsInMarathi";
        public static final String COLUMN_NAME_SequenceID = "SequenceID";
        public static final String COLUMN_NAME_ForDropdown = "ForDropdown";

        public static final String SQL_CREATE_PUNARBHET_VISITDETAILS_MASTER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_VisitDetailsID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_VisitDetails + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_VisitDetailsInMarathi + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SequenceID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ForDropdown + TEXT_TYPE +
                        ")";
    }
    public static abstract class CreditValidityCheck implements BaseColumns {
        public static final String TABLE_NAME = "TD_CREDIT_VALIDITY_CHECK";

        public static final String COLUMN_NAME_Role = "Role";
        public static final String COLUMN_NAME_AdhaarNo = "AdhaarNo";
        public static final String COLUMN_NAME_MainBorrowerAdhaarNo = "MainBorrowerAdhaarNo";
        public static final String COLUMN_NAME_RationCardNo = "RationCardNo";
        public static final String COLUMN_NAME_PAN_No = "PAN_No";
        public static final String COLUMN_NAME_VoterID = "VoterID";
        public static final String COLUMN_NAME_FName = "FName";
        public static final String COLUMN_NAME_MName = "MName";
        public static final String COLUMN_NAME_LName = "LName";
        public static final String COLUMN_NAME_Gender = "Gender";
        public static final String COLUMN_NAME_DateOfBirth = "DateOfBirth";
        public static final String COLUMN_NAME_Address = "Address";
        public static final String COLUMN_NAME_City = "City";
        public static final String COLUMN_NAME_State = "State";
        public static final String COLUMN_NAME_Pincode = "Pincode";
        public static final String COLUMN_NAME_ContactNo = "ContactNo";
        public static final String COLUMN_NAME_ProfessionID = "ProfessionID";
        public static final String COLUMN_NAME_OwnHouse = "OwnHouse";
        public static final String COLUMN_NAME_MaritalStatusID = "MaritalStatusID";
        public static final String COLUMN_NAME_ClusterID = "ClusterID";
        public static final String COLUMN_NAME_CommunityID = "CommunityID";
        public static final String COLUMN_NAME_Cluster = "Cluster";
        public static final String COLUMN_NAME_Community = "Community";

        public static final String COLUMN_NAME_MainBorrowerRelationID = "MainBorrowerRelationID";
        public static final String COLUMN_NAME_IsFamilyMember = "IsFamilyMember";
        public static final String COLUMN_NAME_MonthlyIncome = "MonthlyIncome";

        public static final String COLUMN_NAME_CreditCheckProgress = "CreditCheckProgress";
        public static final String COLUMN_NAME_CreditStatus = "CreditStatus";
        public static final String COLUMN_NAME_MemberType = "MemberType";
        public static final String COLUMN_NAME_ListType = "ListType";
        public static final String COLUMN_NAME_Remarks = "Remarks";

        public static final String COLUMN_NAME_CBEnquiryDate = "CBEnquiryDate";
        public static final String COLUMN_NAME_CBScore = "CBScore";
        public static final String COLUMN_NAME_CBStatus = "CBStatus";
        public static final String COLUMN_NAME_CBRemark = "CBRemark";

        public static final String SQL_CREATE_CREDIT_VALIDITY_CHECK =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_Role + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_AdhaarNo + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_RationCardNo + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PAN_No + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_VoterID + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_FName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_MName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Gender + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_DateOfBirth + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Address + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_City + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_State + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Pincode + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ContactNo + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_CreditCheckProgress + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_CreditStatus + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberType + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ListType + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Remarks + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_CBEnquiryDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_CBScore + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_CBStatus + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_CBRemark + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_MainBorrowerRelationID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_IsFamilyMember + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MainBorrowerAdhaarNo + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ProfessionID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OwnHouse + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MaritalStatusID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MonthlyIncome + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ClusterID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_CommunityID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Cluster + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Community + TEXT_TYPE +
                        ")";
    }

    public static abstract class MemberRecovery implements BaseColumns {
        public static final String TABLE_NAME = "TD_MEMBER_RECOVERY";
//monthly/partial update columns
///////////////////////////////////////////////////////////////////////////////
        public static final String COLUMN_NAME_BranchName = "BranchName";
        public static final String COLUMN_NAME_ClusterID = "ClusterID";
        public static final String COLUMN_NAME_ClusterName = "ClusterName";
        public static final String COLUMN_NAME_CommunityID = "CommunityID";
        public static final String COLUMN_NAME_CommunityName = "CommunityName";
        public static final String COLUMN_NAME_CenterID = "CenterID";
        public static final String COLUMN_NAME_GroupID = "GroupID";
        public static final String COLUMN_NAME_RecoveryDate = "RecoveryDate";
        public static final String COLUMN_NAME_RecoveryTime = "RecoveryTime";
        public static final String COLUMN_NAME_RecoveryDateTime = "RecoveryDateTime";
        public static final String COLUMN_NAME_MemberID = "MemberID";
        public static final String COLUMN_NAME_MemberName = "MemberName";
        public static final String COLUMN_NAME_SavingAccNo = "SavingAccNo";
        public static final String COLUMN_NAME_PhoneNo = "PhoneNo";
        public static final String COLUMN_NAME_LoanNo = "LoanNo";
//        public static final String COLUMN_NAME_LoanPastDueAmountTillToday = "LoanPastDueAmountTillToday";
//        public static final String COLUMN_NAME_LoanCurrentDueAmount = "LoanCurrentDueAmount";
//        public static final String COLUMN_NAME_LoanPastDuePlusCurrentDueAmt = "LoanPastDuePlusCurrentDueAmt";
        public static final String COLUMN_NAME_ADHPDetailsID = "ADHPDetailsID";
//        public static final String COLUMN_NAME_ADHPContributionAmount = "ADHPContributionAmount";
//        public static final String COLUMN_NAME_ADHPTotalExpectedAndPastDueAmt = "ADHPTotalExpectedAndPastDueAmt";
//        public static final String COLUMN_NAME_TotalDueAmountsToPay = "TotalDueAmountsToPay";
//        public static final String COLUMN_NAME_TotalOutstandingAmount = "TotalOutstandingAmount";
        public static final String COLUMN_NAME_Col_MemberName = "Col_MemberName";
        public static final String COLUMN_NAME_EMIReceiptNumber = "EMIReceiptNumber";
//        public static final String COLUMN_NAME_PastDue_Interest = "PastDue_Interest";
//        public static final String COLUMN_NAME_PastDue_LatePaymentCharges = "PastDue_LatePaymentCharges";
//        public static final String COLUMN_NAME_PastDue_Penalty = "PastDue_Penalty";
//        public static final String COLUMN_NAME_PastDue_Principal = "PastDue_Principal";
//        public static final String COLUMN_NAME_PastDue_SD = "PastDue_SD";
//        public static final String COLUMN_NAME_PastDue_PolicyRenewalAmt = "PastDue_PolicyRenewalAmt";
//        public static final String COLUMN_NAME_Expected_Interest = "Expected_Interest";
//        public static final String COLUMN_NAME_Expected_LatePaymentCharges = "Expected_LatePaymentCharges";
//        public static final String COLUMN_NAME_Expected_Penalty = "Expected_Penalty";
//        public static final String COLUMN_NAME_Expected_Principal = "Expected_Principal";
//        public static final String COLUMN_NAME_Expected_SD = "Expected_SD";
//        public static final String COLUMN_NAME_Expected_PolicyRenewalAmt = "Expected_PolicyRenewalAmt";
        public static final String COLUMN_NAME_ADHPReceiptNumber = "ADHPReceiptNumber";
        public static final String COLUMN_NAME_ADHPMembershipNumber = "ADHPMembershipNumber";
        public static final String COLUMN_NAME_ContributionNo = "ContributionNo";
//        public static final String COLUMN_NAME_PendingPrincipal = "PendingPrincipal";
        public static final String COLUMN_NAME_YearlyInterest = "YearlyInterest";
        public static final String COLUMN_NAME_InterestTypeID = "InterestTypeID";
//        public static final String COLUMN_NAME_WaveOffInterestAmount = "WaveOffInterestAmount";
//        public static final String COLUMN_NAME_ActualAmtToBePaidToCloseRescheduleLoan = "ActualAmtToBePaidToCloseRescheduleLoan";
        public static final String COLUMN_NAME_LoanInstallmentNo = "LoanInstallmentNo";
        public static final String COLUMN_NAME_Monthly_Principal = "Monthly_Principal";
        public static final String COLUMN_NAME_Monthly_Interest = "Monthly_Interest";
        public static final String COLUMN_NAME_Monthly_SD = "Monthly_SD";
        public static final String COLUMN_NAME_StdRecoveryDay = "StdRecoveryDay";
        public static final String COLUMN_NAME_StdRecoveryTime = "StdRecoveryTime";
        public static final String COLUMN_NAME_MaturityDate = "MaturityDate";
        public static final String COLUMN_NAME_Internal_Transfer_Reason = "Internal_Transfer_Reason";
        public static final String COLUMN_NAME_StandardEMI = "StandardEMI";
///////////////////////////////////////////////////////////////////////////////
        public static final String COLUMN_NAME_MemberStatus = "MemberStatus";
        public static final String COLUMN_NAME_Monthly_ADHP = "Monthly_ADHP";
        public static final String COLUMN_NAME_Monthly_PolicyRenewalAmt = "Monthly_PolicyRenewalAmt";
//Additional columns required for calculations => moved to MemberTransaction table
//        public static final String COLUMN_NAME_PreClosureReasonID = "PreClosureReasonID";
//        public static final String COLUMN_NAME_LastEMIReceiptNumber = "LastEMIReceiptNumber";
//        public static final String COLUMN_NAME_LastExtraReceiptNumber = "LastExtraReceiptNumber";
//        public static final String COLUMN_NAME_LastADHPReceiptNumber = "LastADHPReceiptNumber";
//        public static final String COLUMN_NAME_ReferenceRecoveryDate = "ReferenceRecoveryDate";
//        public static final String COLUMN_NAME_PreClosureRequestDate = "PreClosureRequestDate";
//        public static final String COLUMN_NAME_PreClosureRequestStatus = "PreClosureRequestStatus";
//        public static final String COLUMN_NAME_PreClosurePrincipal = "PreClosurePrincipal";
//        public static final String COLUMN_NAME_PrePaymentPrincipalCollected = "PrePaymentPrincipalCollected";
//        public static final String COLUMN_NAME_PreClosureInterest = "PreClosureInterest";
//        public static final String COLUMN_NAME_PreClosureJLG = "PreClosureJLG";
//        public static final String COLUMN_NAME_PreClosurePolicyRenewalAmt = "PreClosurePolicyRenewalAmt";
//        public static final String COLUMN_NAME_PreClosureLatePayCharges = "PreClosureLatePayCharges";
//        public static final String COLUMN_NAME_PreClosureAmount = "PreClosureAmount";
//        public static final String COLUMN_NAME_PrePaymentReqMonths = "PrePaymentReqMonths";
//        public static final String COLUMN_NAME_PrePaymentReqAmount = "PrePaymentReqAmount";
//        public static final String COLUMN_NAME_PrePaymentApprovedMonths = "PrePaymentApprovedMonths";
//        public static final String COLUMN_NAME_PrePaymentApprovedAmount = "PrePaymentApprovedAmount";
//        public static final String COLUMN_NAME_PrePaymentORPreClosure = "PrePaymentORPreClosure";
//        public static final String COLUMN_NAME_PreClosureRejectReasonID = "PreClosureRejectReasonID";

        public static final String SQL_CREATE_MEMBER_RECOVERY =
                "CREATE TABLE " + TABLE_NAME + " (" +
//monthly/partial update columns
///////////////////////////////////////////////////////////////////////////////
//Following column values remain constant for month
                        COLUMN_NAME_BranchName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ClusterID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ClusterName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_CommunityID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_CommunityName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_CenterID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_GroupID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_RecoveryDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_RecoveryTime + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_RecoveryDateTime + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_SavingAccNo + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PhoneNo + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanNo + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ADHPDetailsID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Col_MemberName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_EMIReceiptNumber + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ADHPReceiptNumber + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ADHPMembershipNumber + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ContributionNo + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_YearlyInterest + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_InterestTypeID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanInstallmentNo + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Monthly_Principal + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Monthly_Interest + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Monthly_SD + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_StdRecoveryDay + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_StdRecoveryTime + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_MaturityDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Internal_Transfer_Reason + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_StandardEMI + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_WaveOffInterestAmount + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_ActualAmtToBePaidToCloseRescheduleLoan + INTEGER_TYPE + COMMA_SEP +

//Following column values remain constant for month - Copy of ADHPContributionAmount and Expected_PolicyRenewalAmt
                        COLUMN_NAME_Monthly_ADHP + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Monthly_PolicyRenewalAmt + INTEGER_TYPE + COMMA_SEP +

//Following column values vary with transactions. Monthly/Partial update values stored to MemberTransaction table.
//Values updated per transaction are appended to MemberTransaction table.
//                        COLUMN_NAME_LoanPastDueAmountTillToday + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_LoanCurrentDueAmount + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_LoanPastDuePlusCurrentDueAmt + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_ADHPContributionAmount + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_ADHPTotalExpectedAndPastDueAmt + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_TotalDueAmountsToPay + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_TotalOutstandingAmount + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PastDue_Interest + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PastDue_LatePaymentCharges + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PastDue_Penalty + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PastDue_Principal + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PastDue_SD + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PastDue_PolicyRenewalAmt + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_Expected_Interest + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_Expected_LatePaymentCharges + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_Expected_Penalty + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_Expected_Principal + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_Expected_SD + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_Expected_PolicyRenewalAmt + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PendingPrincipal + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberStatus + INTEGER_TYPE +
//bmp 08-Jun-26
//Additional columns required for calculations => moved to MemberTransaction table
//                        COLUMN_NAME_PreClosureReasonID + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_LastEMIReceiptNumber + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_LastExtraReceiptNumber + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_LastADHPReceiptNumber + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_ReferenceRecoveryDate + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PreClosureRequestDate + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PreClosureRequestStatus + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PreClosurePrincipal + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PrePaymentPrincipalCollected + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PreClosureInterest + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PreClosureJLG + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PreClosurePolicyRenewalAmt + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PreClosureLatePayCharges + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PreClosureAmount + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PrePaymentReqMonths + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PrePaymentReqAmount + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PrePaymentApprovedMonths + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PrePaymentApprovedAmount + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PrePaymentORPreClosure + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PreClosureRejectReasonID + INTEGER_TYPE +
//bmp 08-Jun-26
                        ")";
    }
//bmp 08-Jun-26
    public static abstract class MemberTransaction implements BaseColumns {
        public static final String TABLE_NAME = "TD_MEMBER_TRANSACTION";

        public static final String COLUMN_NAME_MemberID = "MemberID";
        public static final String COLUMN_NAME_TransactionTimestamp = "TransactionTimestamp";
        public static final String COLUMN_NAME_TransactionDateTime = "TransactionDateTime";
        public static final String COLUMN_NAME_TransactionType = "TransactionType";
        public static final String COLUMN_NAME_TransactionStatus = "TransactionStatus";
        public static final String COLUMN_NAME_OnlinePaymentAppID = "OnlinePaymentAppID";
        public static final String COLUMN_NAME_OnlineTransactionID = "OnlineTransactionID";
        public static final String COLUMN_NAME_OnlinePaymentUPIID = "OnlinePaymentUPIID";
        public static final String COLUMN_NAME_OnlineTransactionDate = "OnlineTransactionDate";
        public static final String COLUMN_NAME_ScreenShotDate = "ScreenShotDate";
//Following column values vary with transactions. Starting values copied from monthly/partial update
//then onwards new record with updated values appended every transaction
        public static final String COLUMN_NAME_LoanPastDueAmountTillToday = "LoanPastDueAmountTillToday";
        public static final String COLUMN_NAME_LoanCurrentDueAmount = "LoanCurrentDueAmount";
        public static final String COLUMN_NAME_LoanPastDuePlusCurrentDueAmt = "LoanPastDuePlusCurrentDueAmt";
        public static final String COLUMN_NAME_ADHPContributionAmount = "ADHPContributionAmount";
        public static final String COLUMN_NAME_ADHPTotalExpectedAndPastDueAmt = "ADHPTotalExpectedAndPastDueAmt";
//bmp 16-Jul-26
        public static final String COLUMN_NAME_ExtraADHPAmountCollected = "ExtraADHPAmountCollected";
//bmp 16-Jul-26
        public static final String COLUMN_NAME_TotalDueAmountsToPay = "TotalDueAmountsToPay";
        public static final String COLUMN_NAME_TotalOutstandingAmount = "TotalOutstandingAmount";
        public static final String COLUMN_NAME_PastDue_Interest = "PastDue_Interest";
        public static final String COLUMN_NAME_PastDue_LatePaymentCharges = "PastDue_LatePaymentCharges";
        public static final String COLUMN_NAME_PastDue_Penalty = "PastDue_Penalty";
        public static final String COLUMN_NAME_PastDue_Principal = "PastDue_Principal";
        public static final String COLUMN_NAME_PastDue_SD = "PastDue_SD";
        public static final String COLUMN_NAME_PastDue_PolicyRenewalAmt = "PastDue_PolicyRenewalAmt";
        public static final String COLUMN_NAME_Expected_Interest = "Expected_Interest";
        public static final String COLUMN_NAME_Expected_LatePaymentCharges = "Expected_LatePaymentCharges";
        public static final String COLUMN_NAME_Expected_Penalty = "Expected_Penalty";
        public static final String COLUMN_NAME_Expected_Principal = "Expected_Principal";
        public static final String COLUMN_NAME_Expected_SD = "Expected_SD";
        public static final String COLUMN_NAME_Expected_PolicyRenewalAmt = "Expected_PolicyRenewalAmt";
        public static final String COLUMN_NAME_PendingPrincipal = "PendingPrincipal";
//Additional columns required for calculations => moved from MemberRecovery table
//new record with updated values appended every transaction
        public static final String COLUMN_NAME_PreClosureReasonID = "PreClosureReasonID";
        public static final String COLUMN_NAME_LastEMIReceiptNumber = "LastEMIReceiptNumber";
        public static final String COLUMN_NAME_LastExtraReceiptNumber = "LastExtraReceiptNumber";
        public static final String COLUMN_NAME_LastADHPReceiptNumber = "LastADHPReceiptNumber";
        public static final String COLUMN_NAME_ReferenceRecoveryDate = "ReferenceRecoveryDate";
        public static final String COLUMN_NAME_PreClosureRequestDate = "PreClosureRequestDate";
        public static final String COLUMN_NAME_PreClosureRequestStatus = "PreClosureRequestStatus";
        public static final String COLUMN_NAME_PreClosurePrincipal = "PreClosurePrincipal";
        public static final String COLUMN_NAME_PrePaymentPrincipalCollected = "PrePaymentPrincipalCollected";
        public static final String COLUMN_NAME_PreClosureInterest = "PreClosureInterest";
        public static final String COLUMN_NAME_PreClosureJLG = "PreClosureJLG";
        public static final String COLUMN_NAME_PreClosurePolicyRenewalAmt = "PreClosurePolicyRenewalAmt";
        public static final String COLUMN_NAME_PreClosureLatePayCharges = "PreClosureLatePayCharges";
        public static final String COLUMN_NAME_PreClosureAmount = "PreClosureAmount";
        public static final String COLUMN_NAME_PrePaymentReqMonths = "PrePaymentReqMonths";
        public static final String COLUMN_NAME_PrePaymentReqAmount = "PrePaymentReqAmount";
        public static final String COLUMN_NAME_PrePaymentApprovedMonths = "PrePaymentApprovedMonths";
        public static final String COLUMN_NAME_PrePaymentApprovedAmount = "PrePaymentApprovedAmount";
        public static final String COLUMN_NAME_PrePaymentORPreClosure = "PrePaymentORPreClosure";
        public static final String COLUMN_NAME_PreClosureRejectReasonID = "PreClosureRejectReasonID";

        public static final String SQL_CREATE_MEMBER_TRANSACTION =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_MemberID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TransactionTimestamp + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TransactionDateTime + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_TransactionType + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_TransactionStatus + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OnlinePaymentAppID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OnlineTransactionID + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OnlinePaymentUPIID + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OnlineTransactionDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ScreenShotDate + TEXT_TYPE + COMMA_SEP +
//Following column values vary with transactions. Starting values copied from monthly/partial update
//then onwards new record with updated values appended every transaction
                        COLUMN_NAME_LoanPastDueAmountTillToday + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanCurrentDueAmount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanPastDuePlusCurrentDueAmt + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ADHPContributionAmount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ADHPTotalExpectedAndPastDueAmt + INTEGER_TYPE + COMMA_SEP +
//bmp 16-Jul-26
                        COLUMN_NAME_ExtraADHPAmountCollected + INTEGER_TYPE + COMMA_SEP +
//bmp 16-Jul-26
                        COLUMN_NAME_TotalDueAmountsToPay + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOutstandingAmount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PastDue_Interest + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PastDue_LatePaymentCharges + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PastDue_Penalty + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PastDue_Principal + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PastDue_SD + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PastDue_PolicyRenewalAmt + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Expected_Interest + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Expected_LatePaymentCharges + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Expected_Penalty + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Expected_Principal + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Expected_SD + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Expected_PolicyRenewalAmt + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PendingPrincipal + INTEGER_TYPE + COMMA_SEP +
//Additional columns required for calculations => moved from MemberRecovery table
//new record with updated values appended every transaction
                        COLUMN_NAME_PreClosureReasonID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LastEMIReceiptNumber + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LastExtraReceiptNumber + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LastADHPReceiptNumber + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ReferenceRecoveryDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PreClosureRequestDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PreClosureRequestStatus + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PreClosurePrincipal + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PrePaymentPrincipalCollected + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PreClosureInterest + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PreClosureJLG + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PreClosurePolicyRenewalAmt + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PreClosureLatePayCharges + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PreClosureAmount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PrePaymentReqMonths + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PrePaymentReqAmount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PrePaymentApprovedMonths + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PrePaymentApprovedAmount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PrePaymentORPreClosure + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_PreClosureRejectReasonID + INTEGER_TYPE +
                ")";
    }

//bmp 08-Jun-26
//CenterID|OperatingMemberID|OperatedBY|FirstAcHolder|RDReceiptID|ReceiptNumber|RDDetailsID|InstallmentNo|PastDue|
//ExpectedInstallmentAmt|FirstAccHolderID
    public static abstract class RDReceipts implements BaseColumns {
        public static final String TABLE_NAME = "TD_RD_Receipts";

    //Following column values remain constant for month
        public static final String COLUMN_NAME_CenterID = "CenterID";
        public static final String COLUMN_NAME_OperatingMemberID = "OperatingMemberID";
        public static final String COLUMN_NAME_OperatedBY = "OperatedBY";
        public static final String COLUMN_NAME_FirstACMemberID = "FirstACMemberID";
        public static final String COLUMN_NAME_FirstAcHolder = "FirstAcHolder";
        public static final String COLUMN_NAME_RDReceiptID = "RDReceiptID";
        public static final String COLUMN_NAME_ReceiptNumber = "ReceiptNumber";
        public static final String COLUMN_NAME_RDDetailsID = "RDDetailsID";
        public static final String COLUMN_NAME_InstallmentNo = "InstallmentNo";
//Following column values remain constant for month - Copy of ExpectedInstallmentAmt
        public static final String COLUMN_NAME_MonthlyInstallmentAmt = "MonthlyInstallmentAmt";
//Following column values vary with transactions. Monthly/Partial update values stored here.
//Values updated per transaction are appended to RDTransaction table.
//        public static final String COLUMN_NAME_PastDue = "PastDue";
//        public static final String COLUMN_NAME_ExpectedInstallmentAmt = "ExpectedInstallmentAmt";
//        public static final String COLUMN_NAME_LastReceiptNumber = "LastReceiptNumber";
        public static final String COLUMN_NAME_CollectionStatus = "CollectionStatus";

        public static final String SQL_CREATE_RD_RECEIPTS =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_CenterID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OperatingMemberID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_FirstACMemberID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_FirstAcHolder + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_RDReceiptID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_RDDetailsID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ReceiptNumber + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_LastReceiptNumber + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PastDue + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_ExpectedInstallmentAmt + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MonthlyInstallmentAmt + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_CollectionStatus + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OperatedBY + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_InstallmentNo + INTEGER_TYPE +
                        ")";
    }
    public static abstract class RDTransaction implements BaseColumns {
        public static final String TABLE_NAME = "TD_RD_TRANSACTION";

        public static final String COLUMN_NAME_RDDetailsID = "RDDetailsID";
        public static final String COLUMN_NAME_OperatingMemberID = "OperatingMemberID";
        public static final String COLUMN_NAME_TransactionTimestamp = "TransactionTimestamp";
        public static final String COLUMN_NAME_TransactionDateTime = "TransactionDateTime";
        public static final String COLUMN_NAME_TransactionType = "TransactionType";
        public static final String COLUMN_NAME_TransactionStatus = "TransactionStatus";
        public static final String COLUMN_NAME_OnlinePaymentAppID = "OnlinePaymentAppID";
        public static final String COLUMN_NAME_OnlineTransactionID = "OnlineTransactionID";
        public static final String COLUMN_NAME_OnlinePaymentUPIID = "OnlinePaymentUPIID";
        public static final String COLUMN_NAME_OnlineTransactionDate = "OnlineTransactionDate";
        public static final String COLUMN_NAME_ScreenShotDate = "ScreenShotDate";
//Following column values vary with transactions. Starting values copied from monthly/partial update
//then onwards new record with updated values appended every transaction
        public static final String COLUMN_NAME_PastDue = "PastDue";
        public static final String COLUMN_NAME_ExpectedInstallmentAmt = "ExpectedInstallmentAmt";
        public static final String COLUMN_NAME_LastReceiptNumber = "LastReceiptNumber";

        public static final String SQL_CREATE_RD_TRANSACTION =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_RDDetailsID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OperatingMemberID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TransactionTimestamp + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TransactionDateTime + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_TransactionType + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_TransactionStatus + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OnlinePaymentAppID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OnlineTransactionID + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OnlinePaymentUPIID + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OnlineTransactionDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ScreenShotDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_PastDue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ExpectedInstallmentAmt + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_LastReceiptNumber + TEXT_TYPE +
                        ")";
    }

    public static abstract class CenterCollection implements BaseColumns {
        public static final String TABLE_NAME = "TD_CenterCollection";

        public static final String COLUMN_NAME_CenterID = "CenterID";
        public static final String COLUMN_NAME_CollectionTimeStamp = "CollectionTimeStamp";
        public static final String COLUMN_NAME_Notes2000Count = "Notes2000Count";
        public static final String COLUMN_NAME_Notes500Count = "Notes500Count";
        public static final String COLUMN_NAME_Notes200Count = "Notes200Count";
        public static final String COLUMN_NAME_Notes100Count = "Notes100Count";
        public static final String COLUMN_NAME_Notes50Count = "Notes50Count";
        public static final String COLUMN_NAME_Notes20Count = "Notes20Count";
        public static final String COLUMN_NAME_Notes10Count = "Notes10Count";
        public static final String COLUMN_NAME_Notes5Count = "Notes5Count";
        public static final String COLUMN_NAME_Notes2Count = "Notes2Count";
        public static final String COLUMN_NAME_Notes1Count = "Notes1Count";
        public static final String COLUMN_NAME_TotalCollectionValue = "TotalCollectionValue";
        public static final String COLUMN_NAME_TotalOnlineCollection = "TotalOnlineCollection";
        public static final String COLUMN_NAME_TotalCollection = "TotalCollection";
        public static final String COLUMN_NAME_NextRecoveryDate = "NextRecoveryDate";
        public static final String COLUMN_NAME_NextRecoveryTime = "NextRecoveryTime";
        public static final String COLUMN_NAME_NextRecoveryDateTime = "NextRecoveryDateTime";

        public static final String SQL_CREATE_CENTER_COLLECTION =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_CenterID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_CollectionTimeStamp + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_NextRecoveryDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_NextRecoveryTime + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_NextRecoveryDateTime + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Notes2000Count + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Notes500Count + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Notes200Count + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Notes100Count + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Notes50Count + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Notes20Count + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Notes10Count + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Notes5Count + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Notes2Count + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Notes1Count + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlineCollection + INTEGER_TYPE +
                        COLUMN_NAME_TotalCollection + INTEGER_TYPE +
                        COLUMN_NAME_TotalCollectionValue + INTEGER_TYPE +
                        ")";
    }

    public static abstract class CollectionSummary implements BaseColumns {
        public static final String TABLE_NAME = "TD_CollectionSummary";

        public static final String COLUMN_NAME_CollectionID = "CollectionID";
        public static final String COLUMN_NAME_CollectionDate = "CollectionDate";
        public static final String COLUMN_NAME_Total2000NotesCount = "Total2000NotesCount";
        public static final String COLUMN_NAME_Total500NotesCount = "Total500NotesCount";
        public static final String COLUMN_NAME_Total200NotesCount = "Total200NotesCount";
        public static final String COLUMN_NAME_Total100NotesCount = "Total100NotesCount";
        public static final String COLUMN_NAME_Total50NotesCount = "Total50NotesCount";
        public static final String COLUMN_NAME_Total20NotesCount = "Total20NotesCount";
        public static final String COLUMN_NAME_Total10NotesCount = "Total10NotesCount";
        public static final String COLUMN_NAME_Total5NotesCount = "Total5NotesCount";
        public static final String COLUMN_NAME_Total2NotesCount = "Total2NotesCount";
        public static final String COLUMN_NAME_Total1NotesCount = "Total1NotesCount";
        public static final String COLUMN_NAME_TotalValueOnline = "TotalValueOnline";
        public static final String COLUMN_NAME_TotalValueCash = "TotalValueCash";
        public static final String COLUMN_NAME_TotalCollectionValue = "TotalCollectionValue";

        public static final String COLUMN_NAME_TotalEMIReceiptsCount = "TotalEMIReceiptsCount";
        public static final String COLUMN_NAME_TotalEMIReceiptsValue = "TotalEMIReceiptsValue";
        public static final String COLUMN_NAME_TotalADHPReceiptsCount = "TotalADHPReceiptsCount";
        public static final String COLUMN_NAME_TotalADHPReceiptsValue = "TotalADHPReceiptsValue";
        public static final String COLUMN_NAME_TotalRDReceiptsCount = "TotalRDReceiptsCount";
        public static final String COLUMN_NAME_TotalRDReceiptsValue = "TotalRDReceiptsValue";
        public static final String COLUMN_NAME_TotalAdditionalRepayReceiptsCount = "TotalAdditionalRepayReceiptsCount";
        public static final String COLUMN_NAME_TotalAdditionalRepayReceiptsValue = "TotalAdditionalRepayReceiptsValue";
        public static final String COLUMN_NAME_TotalExtraReceiptsCount = "TotalExtraReceiptsCount";
        public static final String COLUMN_NAME_TotalExtraReceiptsValue = "TotalExtraReceiptsValue";
        public static final String COLUMN_NAME_TotalPreClosureAdditionalEMIReceiptsCount = "TotalPreClosureAdditionalEMIReceiptsCount";
        public static final String COLUMN_NAME_TotalPreClosureAdditionalEMIReceiptsValue = "TotalPreClosureAdditionalEMIReceiptsValue";
        public static final String COLUMN_NAME_TotalADHPExtraReceiptsCount = "TotalADHPExtraReceiptsCount";
        public static final String COLUMN_NAME_TotalADHPExtraReceiptsValue = "TotalADHPExtraReceiptsValue";
        public static final String COLUMN_NAME_TotalNewRDExtraReceiptsCount = "TotalNewRDExtraReceiptsCount";
        public static final String COLUMN_NAME_TotalNewRDExtraReceiptsValue = "TotalNewRDExtraReceiptsValue";
        public static final String COLUMN_NAME_TotalFDExtraReceiptsCount = "TotalFDExtraReceiptsCount";
        public static final String COLUMN_NAME_TotalFDExtraReceiptsValue = "TotalFDExtraReceiptsValue";
        public static final String COLUMN_NAME_TotalJLGExtraReceiptsCount = "TotalJLGExtraReceiptsCount";
        public static final String COLUMN_NAME_TotalJLGExtraReceiptsValue = "TotalJLGExtraReceiptsValue";
        public static final String COLUMN_NAME_TotalReprintChargesExtraReceiptsCount = "TotalReprintChargesExtraReceiptsCount";
        public static final String COLUMN_NAME_TotalReprintChargesExtraReceiptsValue = "TotalReprintChargesExtraReceiptsValue";

        public static final String COLUMN_NAME_TotalCashEMIReceiptsCount = "TotalCashEMIReceiptsCount";
        public static final String COLUMN_NAME_TotalCashEMIReceiptsValue = "TotalCashEMIReceiptsValue";
        public static final String COLUMN_NAME_TotalCashADHPReceiptsCount = "TotalCashADHPReceiptsCount";
        public static final String COLUMN_NAME_TotalCashADHPReceiptsValue = "TotalCashADHPReceiptsValue";
        public static final String COLUMN_NAME_TotalCashRDReceiptsCount = "TotalCashRDReceiptsCount";
        public static final String COLUMN_NAME_TotalCashRDReceiptsValue = "TotalCashRDReceiptsValue";
        public static final String COLUMN_NAME_TotalCashAdditionalRepayReceiptsCount = "TotalCashAdditionalRepayReceiptsCount";
        public static final String COLUMN_NAME_TotalCashAdditionalRepayReceiptsValue = "TotalCashAdditionalRepayReceiptsValue";
        public static final String COLUMN_NAME_TotalCashExtraReceiptsCount = "TotalCashExtraReceiptsCount";
        public static final String COLUMN_NAME_TotalCashExtraReceiptsValue = "TotalCashExtraReceiptsValue";
        public static final String COLUMN_NAME_TotalCashPreClosureAdditionalEMIReceiptsCount = "TotalCashPreClosureAdditionalEMIReceiptsCount";
        public static final String COLUMN_NAME_TotalCashPreClosureAdditionalEMIReceiptsValue = "TotalCashPreClosureAdditionalEMIReceiptsValue";
        public static final String COLUMN_NAME_TotalCashADHPExtraReceiptsCount = "TotalCashADHPExtraReceiptsCount";
        public static final String COLUMN_NAME_TotalCashADHPExtraReceiptsValue = "TotalCashADHPExtraReceiptsValue";
        public static final String COLUMN_NAME_TotalCashNewRDExtraReceiptsCount = "TotalCashNewRDExtraReceiptsCount";
        public static final String COLUMN_NAME_TotalCashNewRDExtraReceiptsValue = "TotalCashNewRDExtraReceiptsValue";
        public static final String COLUMN_NAME_TotalCashFDExtraReceiptsCount = "TotalCashFDExtraReceiptsCount";
        public static final String COLUMN_NAME_TotalCashFDExtraReceiptsValue = "TotalCashFDExtraReceiptsValue";
        public static final String COLUMN_NAME_TotalCashJLGExtraReceiptsCount = "TotalCashJLGExtraReceiptsCount";
        public static final String COLUMN_NAME_TotalCashJLGExtraReceiptsValue = "TotalCashJLGExtraReceiptsValue";
        public static final String COLUMN_NAME_TotalCashReprintChargesExtraReceiptsCount = "TotalCashReprintChargesExtraReceiptsCount";
        public static final String COLUMN_NAME_TotalCashReprintChargesExtraReceiptsValue = "TotalCashReprintChargesExtraReceiptsValue";

        public static final String COLUMN_NAME_TotalOnlineEMIReceiptsCount = "TotalOnlineEMIReceiptsCount";
        public static final String COLUMN_NAME_TotalOnlineEMIReceiptsValue = "TotalOnlineEMIReceiptsValue";
        public static final String COLUMN_NAME_TotalOnlineADHPReceiptsCount = "TotalOnlineADHPReceiptsCount";
        public static final String COLUMN_NAME_TotalOnlineADHPReceiptsValue = "TotalOnlineADHPReceiptsValue";
        public static final String COLUMN_NAME_TotalOnlineRDReceiptsCount = "TotalOnlineRDReceiptsCount";
        public static final String COLUMN_NAME_TotalOnlineRDReceiptsValue = "TotalOnlineRDReceiptsValue";
        public static final String COLUMN_NAME_TotalOnlineAdditionalRepayReceiptsCount = "TotalOnlineAdditionalRepayReceiptsCount";
        public static final String COLUMN_NAME_TotalOnlineAdditionalRepayReceiptsValue = "TotalOnlineAdditionalRepayReceiptsValue";
        public static final String COLUMN_NAME_TotalOnlineExtraReceiptsCount = "TotalOnlineExtraReceiptsCount";
        public static final String COLUMN_NAME_TotalOnlineExtraReceiptsValue = "TotalOnlineExtraReceiptsValue";
        public static final String COLUMN_NAME_TotalOnlinePreClosureAdditionalEMIReceiptsCount = "TotalOnlinePreClosureAdditionalEMIReceiptsCount";
        public static final String COLUMN_NAME_TotalOnlinePreClosureAdditionalEMIReceiptsValue = "TotalOnlinePreClosureAdditionalEMIReceiptsValue";
        public static final String COLUMN_NAME_TotalOnlineADHPExtraReceiptsCount = "TotalOnlineADHPExtraReceiptsCount";
        public static final String COLUMN_NAME_TotalOnlineADHPExtraReceiptsValue = "TotalOnlineADHPExtraReceiptsValue";
        public static final String COLUMN_NAME_TotalOnlineNewRDExtraReceiptsCount = "TotalOnlineNewRDExtraReceiptsCount";
        public static final String COLUMN_NAME_TotalOnlineNewRDExtraReceiptsValue = "TotalOnlineNewRDExtraReceiptsValue";
        public static final String COLUMN_NAME_TotalOnlineFDExtraReceiptsCount = "TotalOnlineFDExtraReceiptsCount";
        public static final String COLUMN_NAME_TotalOnlineFDExtraReceiptsValue = "TotalOnlineFDExtraReceiptsValue";
        public static final String COLUMN_NAME_TotalOnlineJLGExtraReceiptsCount = "TotalOnlineJLGExtraReceiptsCount";
        public static final String COLUMN_NAME_TotalOnlineJLGExtraReceiptsValue = "TotalOnlineJLGExtraReceiptsValue";
        public static final String COLUMN_NAME_TotalOnlineReprintChargesExtraReceiptsCount = "TotalOnlineReprintChargesExtraReceiptsCount";
        public static final String COLUMN_NAME_TotalOnlineReprintChargesExtraReceiptsValue = "TotalOnlineReprintChargesExtraReceiptsValue";

        public static final String SQL_CREATE_COLLECTION_SUMMARY =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_CollectionID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_CollectionDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_Total2000NotesCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Total500NotesCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Total200NotesCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Total100NotesCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Total50NotesCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Total20NotesCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Total10NotesCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Total5NotesCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Total2NotesCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_Total1NotesCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalValueOnline + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalValueCash + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCollectionValue + INTEGER_TYPE + COMMA_SEP +

                        COLUMN_NAME_TotalEMIReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalEMIReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalADHPReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalADHPReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalRDReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalRDReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalAdditionalRepayReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalAdditionalRepayReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalExtraReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalExtraReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalPreClosureAdditionalEMIReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalPreClosureAdditionalEMIReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalADHPExtraReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalADHPExtraReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalNewRDExtraReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalNewRDExtraReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalFDExtraReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalFDExtraReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalJLGExtraReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalJLGExtraReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalReprintChargesExtraReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalReprintChargesExtraReceiptsValue + INTEGER_TYPE + COMMA_SEP +

                        COLUMN_NAME_TotalCashEMIReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCashEMIReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCashADHPReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCashADHPReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCashRDReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCashRDReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCashAdditionalRepayReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCashAdditionalRepayReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCashExtraReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCashExtraReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCashPreClosureAdditionalEMIReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCashPreClosureAdditionalEMIReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCashADHPExtraReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCashADHPExtraReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCashNewRDExtraReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCashNewRDExtraReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCashFDExtraReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCashFDExtraReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCashJLGExtraReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCashJLGExtraReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCashReprintChargesExtraReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalCashReprintChargesExtraReceiptsValue + INTEGER_TYPE + COMMA_SEP +

                        COLUMN_NAME_TotalOnlineEMIReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlineEMIReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlineADHPReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlineADHPReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlineRDReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlineRDReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlineAdditionalRepayReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlineAdditionalRepayReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlineExtraReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlineExtraReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlinePreClosureAdditionalEMIReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlinePreClosureAdditionalEMIReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlineADHPExtraReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlineADHPExtraReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlineNewRDExtraReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlineNewRDExtraReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlineFDExtraReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlineFDExtraReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlineJLGExtraReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlineJLGExtraReceiptsValue + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlineReprintChargesExtraReceiptsCount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_TotalOnlineReprintChargesExtraReceiptsValue + INTEGER_TYPE +
                        ")";
    }
//bmp 12-May-26
    public static abstract class OnlineCollection implements BaseColumns {
        public static final String TABLE_NAME = "TD_OnlineCollection";

        public static final String COLUMN_NAME_OnlineCollectionID = "OnlineCollectionID";
        public static final String COLUMN_NAME_MemberID = "MemberID";
        public static final String COLUMN_NAME_OnlinePaymentByMemberID = "OnlinePaymentByMemberID";
        public static final String COLUMN_NAME_OnlinePaymentByName = "OnlinePaymentByName";
        public static final String COLUMN_NAME_OnlinePaymentByRelationID = "OnlinePaymentByRelationID";
        public static final String COLUMN_NAME_OnlinePaymentProof = "OnlinePaymentProof";
        public static final String COLUMN_NAME_MemberDocumentRcvdID = "MemberDocumentRcvdID";
        public static final String COLUMN_NAME_OnlinePaymentUPIID = "OnlinePaymentUPIID";
        public static final String COLUMN_NAME_OnlinePaymentTransactionID = "OnlinePaymentTransactionID";
        public static final String COLUMN_NAME_OnlinePaymentTransactionDate = "OnlinePaymentTransactionDate";
        public static final String COLUMN_NAME_OnlineTransactionAmount = "OnlineTransactionAmount";

        public static final String SQL_CREATE_ONLINE_COLLECTION =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_OnlineCollectionID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OnlinePaymentByMemberID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OnlinePaymentByName + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OnlinePaymentByRelationID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OnlinePaymentProof + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_MemberDocumentRcvdID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_OnlinePaymentUPIID + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OnlinePaymentTransactionID + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OnlinePaymentTransactionDate + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_OnlineTransactionAmount + INTEGER_TYPE +
                        ")";
    }
//bmp 12-May-26

//    public static abstract class WriteOffLoanRecovery implements BaseColumns {
//        public static final String TABLE_NAME = "TD_Loan_WO_RepaymentReceipt";
//
//        public static final String COLUMN_NAME_BranchName = "BranchName";
//        public static final String COLUMN_NAME_ClusterID = "ClusterID";
//        public static final String COLUMN_NAME_ClusterName = "ClusterName";
//        public static final String COLUMN_NAME_CommunityID = "CommunityID";
//        public static final String COLUMN_NAME_CommunityName = "CommunityName";
//        public static final String COLUMN_NAME_CenterID = "CenterID";
//        public static final String COLUMN_NAME_GroupID = "GroupID";
//        public static final String COLUMN_NAME_MemberID = "MemberID";
//        public static final String COLUMN_NAME_MemberName = "MemberName";
//        public static final String COLUMN_NAME_SavingAccNo = "SavingAccNo";
//        public static final String COLUMN_NAME_PhoneNo = "PhoneNo";
//        public static final String COLUMN_NAME_WO_LoanRepaymentReceiptID = "WO_LoanRepaymentReceiptID";
//        public static final String COLUMN_NAME_WO_ReceiptNumber = "WO_ReceiptNumber";
//        public static final String COLUMN_NAME_LoanID = "LoanID";
//        public static final String COLUMN_NAME_Expected_Principal = "Expected_Principal";
//        public static final String COLUMN_NAME_Expected_Interest = "Expected_Interest";
//        public static final String COLUMN_NAME_Expected_SD = "Expected_SD";
//        public static final String COLUMN_NAME_Expected_Penalty = "Expected_Penalty";
////bmp 21-Mar-24
//        public static final String COLUMN_NAME_InstallmentNo = "InstallmentNo";
//        public static final String COLUMN_NAME_RecoveryDate = "RecoveryDate";
//        public static final String COLUMN_NAME_RecoveryTime = "RecoveryTime";
//        public static final String COLUMN_NAME_RecoveryDateTime = "RecoveryDateTime";
////bmp 21-Mar-24
//        public static final String COLUMN_NAME_WO_LastReceiptNumber = "WO_LastReceiptNumber";
//        public static final String COLUMN_NAME_CollectionStatus = "CollectionStatus";
//        public static final String COLUMN_NAME_ReferenceRecoveryDate = "ReferenceRecoveryDate";
////
////        public static final String COLUMN_NAME_InterestTypeID = "InterestTypeID";
////        public static final String COLUMN_NAME_TotalCollectionAmount = "TotalCollectionAmount";
////        public static final String COLUMN_NAME_TotalEMICollectionAmount = "TotalEMICollectionAmount";
////        public static final String COLUMN_NAME_TotalDueAmountsToPay = "TotalDueAmountsToPay";
////        public static final String COLUMN_NAME_PendingPrincipal = "PendingPrincipal";
////        public static final String COLUMN_NAME_TotalOutstandingAmount = "TotalOutstandingAmount";
////        public static final String COLUMN_NAME_LoanPastDueAmountTillToday = "LoanPastDueAmountTillToday";
//
//        public static final String SQL_CREATE_WRITEOFF_LOAN_RECOVERY =
//                "CREATE TABLE " + TABLE_NAME + " (" +
//                        COLUMN_NAME_BranchName + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_ClusterID + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_ClusterName + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_CommunityID + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_CommunityName + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_CenterID + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_GroupID + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_MemberID + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_MemberName + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_SavingAccNo + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_PhoneNo + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_WO_LoanRepaymentReceiptID + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_WO_ReceiptNumber + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_LoanID + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_Expected_Principal + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_Expected_Interest + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_Expected_SD + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_Expected_Penalty + INTEGER_TYPE + COMMA_SEP +
////bmp 21-Mar-24
//                        COLUMN_NAME_InstallmentNo + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_RecoveryDate + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_RecoveryTime + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_RecoveryDateTime + TEXT_TYPE + COMMA_SEP +
////bmp 21-Mar-24
//                        COLUMN_NAME_WO_LastReceiptNumber + TEXT_TYPE + COMMA_SEP +
//                        COLUMN_NAME_CollectionStatus + INTEGER_TYPE + COMMA_SEP +
//                        COLUMN_NAME_ReferenceRecoveryDate + TEXT_TYPE +
////                        COLUMN_NAME_InterestTypeID + INTEGER_TYPE + COMMA_SEP +
////                        COLUMN_NAME_TotalCollectionAmount + INTEGER_TYPE + COMMA_SEP +
////                        COLUMN_NAME_TotalEMICollectionAmount + INTEGER_TYPE + COMMA_SEP +
////                        COLUMN_NAME_TotalDueAmountsToPay + INTEGER_TYPE + COMMA_SEP +
////                        COLUMN_NAME_PendingPrincipal + INTEGER_TYPE + COMMA_SEP +
////                        COLUMN_NAME_TotalOutstandingAmount + INTEGER_TYPE + COMMA_SEP +
////                        COLUMN_NAME_LoanPastDueAmountTillToday + INTEGER_TYPE +
//                        ")";
//    }

    public static abstract class ActivitySchedule implements BaseColumns {
        public static final String TABLE_NAME = "TD_ACTIVITY_SCHEDULE";

        public static final String COLUMN_NAME_StartTime = "StartTime";
        public static final String COLUMN_NAME_EndTime = "EndTime";
        public static final String COLUMN_NAME_ActivityType = "ActivityType";
        public static final String COLUMN_NAME_ActivityDetails = "ActivityDetails";

        public static final String SQL_CREATE_ACTIVITY_SCHEDULE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_StartTime + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_EndTime + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ActivityType + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_ActivityDetails + TEXT_TYPE +
                        ")";
    }

    public static abstract class OtherMFIData implements BaseColumns {
        public static final String TABLE_NAME = "OTHER_MFI_DATA";

        public static final String COLUMN_NAME_MemberID = "MemberID";
        public static final String COLUMN_NAME_MFI_Name = "MFI_Name";
        public static final String COLUMN_NAME_LoanAmount = "LoanAmount";
        public static final String COLUMN_NAME_ThakitAmount = "ThakitAmount";
        public static final String COLUMN_NAME_RightOffAmount = "RightOffAmount";
        public static final String COLUMN_NAME_MonthlyInstallment = "MonthlyInstallment";
        public static final String COLUMN_NAME_CreditStatus = "CreditStatus";

        public static final String SQL_CREATE_OTHER_MFI_DATA =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_MemberID + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MFI_Name + TEXT_TYPE + COMMA_SEP +
                        COLUMN_NAME_LoanAmount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_ThakitAmount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_RightOffAmount + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_MonthlyInstallment + INTEGER_TYPE + COMMA_SEP +
                        COLUMN_NAME_CreditStatus + TEXT_TYPE +
                        ")";
    }
}
