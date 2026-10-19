package com.example.ufin_lso;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ufinDbHelper extends SQLiteOpenHelper {
//adb -d shell "run-as com.example.ufin_lso cat /data/data/com.example.ufin_lso/databases/ufin.db" > ufin.db

//  Steps to read database on pc
//    $ adb -s <DEVICE_ID> shell
//    $ run-as package.name
//    $ chmod 666 databases/dbname.db
//    $ exit
//    $ cp /data/data/package.name/databases/dbname.db /sdcard/dbname.db
//    $ exit
//    $ adb pull /sdcard/dbname.db ./dbname.db

// If you change the database schema, you must increment the database version.
//    public static final int DATABASE_VERSION = 11;
//    public static final int DATABASE_VERSION = 12;
//    public static final int DATABASE_VERSION = 13;
//    public static final int DATABASE_VERSION = 14;
//    public static final int DATABASE_VERSION = 15;
//    public static final int DATABASE_VERSION = 16;
//    public static final int DATABASE_VERSION = 17;
//    public static final int DATABASE_VERSION = 18;
//    public static final int DATABASE_VERSION = 19;
//    public static final int DATABASE_VERSION = 20;
//    public static final int DATABASE_VERSION = 21;
//    public static final int DATABASE_VERSION = 22;
//    public static final int DATABASE_VERSION = 23;
//    public static final int DATABASE_VERSION = 24;
//    public static final int DATABASE_VERSION = 25;
//    public static final int DATABASE_VERSION = 26;
//    public static final int DATABASE_VERSION = 27;
//    public static final int DATABASE_VERSION = 28;
//    public static final int DATABASE_VERSION = 29;
//    public static final int DATABASE_VERSION = 30;
//    public static final int DATABASE_VERSION = 31;
//bmp 07-May-26
////bmp 24-Mar-26
//    public static final int DATABASE_VERSION = 32;
////bmp 24-Mar-26
//    public static final int DATABASE_VERSION = 33;
////bmp 11-Aug-26
//    public static final int DATABASE_VERSION = 34;
////bmp 11-Aug-26
//bmp 29-Sep-26
    public static final int DATABASE_VERSION = 35;
//bmp 29-Sep-26

    public static final String DATABASE_NAME = "ufin.db";

    public ufinDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch(oldVersion) {
            // we want all updates, so no break statement here...
//            case 1:
//                dbMigrateVersion1To2(db);
//            case 2:
//                dbMigrateVersion2To3(db);
//            case 3:
//                dbMigrateVersion3To4(db);
//            case 4:
//                dbMigrateVersion4To5(db);
//            case 5:
//                dbMigrateVersion5To6(db);
//            case 6:
//                dbMigrateVersion6To7(db);
//            case 7:
//                dbMigrateVersion7To8(db);
//            case 8:
//                dbMigrateVersion8To9(db);
//            case 9:
//                dbMigrateVersion9To10(db);
//            case 10:
//                dbMigrateVersion10To11(db);
//            case 11:
//                dbMigrateVersion11To12(db);
//            case 12:
//                dbMigrateVersion12To13(db);
//            case 13:
//                dbMigrateVersion13To14(db);
//            case 14:
//                dbMigrateVersion14To15(db);
//            case 15:
//                dbMigrateVersion15To16(db);
//            case 16:
//                dbMigrateVersion16To17(db);
//            case 17:
//                dbMigrateVersion17To18(db);
//            case 18:
//                dbMigrateVersion18To19(db);
//            case 19:
//                dbMigrateVersion19To20(db);
//            case 20:
//                dbMigrateVersion20To21(db);
//            case 21:
//                dbMigrateVersion21To22(db);
//            case 22:
//                dbMigrateVersion22To23(db);
//            case 23:
//                dbMigrateVersion23To24(db);
////bmp 28-Nov-25
//            case 24:
//                dbMigrateVersion24To25(db);
////bmp 28-Nov-25
//            case 25:
//                dbMigrateVersion25To26(db);
//            case 26:
//                dbMigrateVersion26To27(db);
//            case 27:
//                dbMigrateVersion27To28(db);
//            case 28:
//                dbMigrateVersion28To29(db);
//            case 29:
//                dbMigrateVersion29To30(db);
//            case 30:
//                dbMigrateVersion30To31(db);
//            case 31:
//                dbMigrateVersion31To32(db);
            case 32:
                dbMigrateVersion32To33(db);
            case 33:
                dbMigrateVersion33To34(db);
            case 34:
                dbMigrateVersion34To35(db);
        }
    }
//    void dbMigrateVersion1To2(SQLiteDatabase db) {
//    }
//    void dbMigrateVersion2To3(SQLiteDatabase db) {
//    }
//    void dbMigrateVersion3To4(SQLiteDatabase db) {
//    }
//    void dbMigrateVersion4To5(SQLiteDatabase db) {
//    }
//    void dbMigrateVersion5To6(SQLiteDatabase db) {
//    }
//    void dbMigrateVersion6To7(SQLiteDatabase db) {
//    }
//    void dbMigrateVersion7To8(SQLiteDatabase db) {
//    }
//    void dbMigrateVersion8To9(SQLiteDatabase db) {
//    }
//    void dbMigrateVersion9To10(SQLiteDatabase db) {
//        String alterTableQuery = "ALTER TABLE " +
//                ufinDbContract.MemberRecovery.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns = {
//                ufinDbContract.MemberRecovery.COLUMN_NAME_ReferenceRecoveryDate + " TEXT",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_Monthly_Interest + " INTEGER",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_Monthly_Principal + " INTEGER",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_Monthly_SD + " INTEGER",
//        };
//
//        db.beginTransaction();
//        for (String column : newColumns){
//            try {
//                db.execSQL(alterTableQuery + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//
//        String alterTableQuery2 = "ALTER TABLE " +
//                ufinDbContract.RDReceipts.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns2 = {
//                ufinDbContract.RDReceipts.COLUMN_NAME_FirstACMemberID + " INTEGER",
//        };
//
//        db.beginTransaction();
//        for (String column : newColumns2){
//            try {
//                db.execSQL(alterTableQuery2 + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//
////        try {
////            db.execSQL(ufinDbContract.WriteOffLoanRecovery.SQL_CREATE_WRITEOFF_LOAN_RECOVERY);
////        } catch (Exception e) {
////            System.out.println(e.getMessage());
////        }
//    }
//    void dbMigrateVersion10To11(SQLiteDatabase db) {
//    }
////bmp 19-Jun-23
//    void dbMigrateVersion11To12(SQLiteDatabase db) {
//        String alterTableQuery = "ALTER TABLE " +
//                ufinDbContract.UserForTabMaster.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns = {
//                ufinDbContract.UserForTabMaster.COLUMN_NAME_ServerIP2 + " TEXT",
//        };
//
//        db.beginTransaction();
//        for (String column : newColumns){
//            try {
//                db.execSQL(alterTableQuery + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//    }
////bmp 19-Jun-23
////bmp 06-Jul-23
//void dbMigrateVersion12To13(SQLiteDatabase db) {
//    String alterTableQuery = "ALTER TABLE " +
//            ufinDbContract.PunarbhetVisitDetailsMatser.TABLE_NAME +
//            " ADD COLUMN ";
//    String[] newColumns = {
//            ufinDbContract.PunarbhetVisitDetailsMatser.COLUMN_NAME_ForDropdown + " TEXT",
//    };
//
//    db.beginTransaction();
//    for (String column : newColumns){
//        try {
//            db.execSQL(alterTableQuery + column);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//    db.setTransactionSuccessful();
//    db.endTransaction();
//}
////bmp 06-Jul-23
////bmp 24-Jul-23
//void dbMigrateVersion13To14(SQLiteDatabase db) {
//    String alterTableQuery = "ALTER TABLE " +
//            ufinDbContract.PunarbhetSchedule.TABLE_NAME +
//            " ADD COLUMN ";
//    String[] newColumns = {
//            ufinDbContract.PunarbhetSchedule.COLUMN_NAME_HomeLoanAmount + " INTEGER",
//    };
//
//    db.beginTransaction();
//    for (String column : newColumns){
//        try {
//            db.execSQL(alterTableQuery + column);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//    db.setTransactionSuccessful();
//    db.endTransaction();
////bmp 31-Jul-23
//    String alterTableQuery2 = "ALTER TABLE " +
//            ufinDbContract.LoanProductsMaster.TABLE_NAME +
//            " ADD COLUMN ";
//    String[] newColumns2 = {
//            ufinDbContract.LoanProductsMaster.COLUMN_NAME_ProductName + " TEXT",
//    };
//
//    db.beginTransaction();
//    for (String column : newColumns2){
//        try {
//            db.execSQL(alterTableQuery2 + column);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//    db.setTransactionSuccessful();
//    db.endTransaction();
////bmp 31-Jul-23
//}
////bmp 24-Jul-23
////bmp 23-Oct-23
//void dbMigrateVersion14To15(SQLiteDatabase db) {
//    String alterTableQuery = "ALTER TABLE " +
//            ufinDbContract.MemberAdditionalDetails.TABLE_NAME +
//            " ADD COLUMN ";
//    String[] newColumns = {
//            ufinDbContract.MemberAdditionalDetails.COLUMN_NAME_LoanProductDetailsID1 + " INTEGER",
//            ufinDbContract.MemberAdditionalDetails.COLUMN_NAME_LoanProductDetailsID2 + " INTEGER",
//            ufinDbContract.MemberAdditionalDetails.COLUMN_NAME_LoanProductDetailsID3 + " INTEGER",
//            ufinDbContract.MemberAdditionalDetails.COLUMN_NAME_LoanProductDetailsID4 + " INTEGER",
//    };
//
//    db.beginTransaction();
//    for (String column : newColumns) {
//        try {
//            db.execSQL(alterTableQuery + column);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//    db.setTransactionSuccessful();
//    db.endTransaction();
//}
// void dbMigrateVersion15To16(SQLiteDatabase db) {
//    String alterTableQuery = "ALTER TABLE " +
//            ufinDbContract.PunarbhetSchedule.TABLE_NAME +
//            " ADD COLUMN ";
//    String[] newColumns = {
//            ufinDbContract.PunarbhetSchedule.COLUMN_NAME_incentiveGenuineBTypeGroup + " INTEGER",
//            ufinDbContract.PunarbhetSchedule.COLUMN_NAME_incentiveRegularLoanRepay + " INTEGER",
//            ufinDbContract.PunarbhetSchedule.COLUMN_NAME_incentiveNotWithdrawingSavingsShares + " INTEGER",
//            ufinDbContract.PunarbhetSchedule.COLUMN_NAME_incentiveRegularRDpay + " INTEGER",
//            ufinDbContract.PunarbhetSchedule.COLUMN_NAME_incentiveRegularADHPpay + " INTEGER",
//            ufinDbContract.PunarbhetSchedule.COLUMN_NAME_incentiveAGMAttendance + " INTEGER",
//    };
//
//    db.beginTransaction();
//    for (String column : newColumns){
//        try {
//            db.execSQL(alterTableQuery + column);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//    db.setTransactionSuccessful();
//    db.endTransaction();
//}
////bmp 23-Oct-23
//void dbMigrateVersion16To17(SQLiteDatabase db) {
//    String alterTableQuery = "ALTER TABLE " +
//            ufinDbContract.RDReceipts.TABLE_NAME +
//            " ADD COLUMN ";
//    String[] newColumns = {
//            ufinDbContract.RDReceipts.COLUMN_NAME_MonthlyInstallmentAmt + " INTEGER",
//    };
//    db.beginTransaction();
//    for (String column : newColumns){
//        try {
//            db.execSQL(alterTableQuery + column);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//    db.setTransactionSuccessful();
//    db.endTransaction();
//}
//    void dbMigrateVersion17To18(SQLiteDatabase db) {
//        String alterTableQuery = "ALTER TABLE " +
//                ufinDbContract.PunarbhetSchedule.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns = {
//                ufinDbContract.PunarbhetSchedule.COLUMN_NAME_incentiveCommunityMeetingNoAttendance + " INTEGER",
//                ufinDbContract.PunarbhetSchedule.COLUMN_NAME_incentiveCommunityMeetingPartAttendance + " INTEGER",
//                ufinDbContract.PunarbhetSchedule.COLUMN_NAME_incentiveCommunityMeetingFullAttendance + " INTEGER",
//                ufinDbContract.PunarbhetSchedule.COLUMN_NAME_NextLoanOfferAmount + " INTEGER",
//                ufinDbContract.PunarbhetSchedule.COLUMN_NAME_incentiveGenuineGradeGroup + " INTEGER",
//                ufinDbContract.PunarbhetSchedule.COLUMN_NAME_incentiveLowPrevMaxLoanAmount + " INTEGER",
//                ufinDbContract.PunarbhetSchedule.COLUMN_NAME_flagNewMember + " INTEGER",
//        };
//        db.beginTransaction();
//        for (String column : newColumns){
//            try {
//                db.execSQL(alterTableQuery + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//    }
//    void dbMigrateVersion18To19(SQLiteDatabase db) {
//        String alterTableQuery = "ALTER TABLE " +
//                ufinDbContract.CenterCollection.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns = {
//                ufinDbContract.CenterCollection.COLUMN_NAME_NextRecoveryDate + " TEXT",
//                ufinDbContract.CenterCollection.COLUMN_NAME_NextRecoveryTime + " TEXT",
//                ufinDbContract.CenterCollection.COLUMN_NAME_NextRecoveryDateTime + " TEXT",
//        };
//        db.beginTransaction();
//        for (String column : newColumns){
//            try {
//                db.execSQL(alterTableQuery + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//
////        String alterTableQuery2 = "ALTER TABLE " +
////                ufinDbContract.WriteOffLoanRecovery.TABLE_NAME +
////                " ADD COLUMN ";
////        String[] newColumns2 = {
////                ufinDbContract.WriteOffLoanRecovery.COLUMN_NAME_ClusterID + " INTEGER",
////                ufinDbContract.WriteOffLoanRecovery.COLUMN_NAME_ClusterName + " TEXT",
////                ufinDbContract.WriteOffLoanRecovery.COLUMN_NAME_CommunityID + " INTEGER",
////                ufinDbContract.WriteOffLoanRecovery.COLUMN_NAME_CommunityName + " TEXT",
////                ufinDbContract.WriteOffLoanRecovery.COLUMN_NAME_InstallmentNo + " INTEGER",
////                ufinDbContract.WriteOffLoanRecovery.COLUMN_NAME_RecoveryDate + " TEXT",
////                ufinDbContract.WriteOffLoanRecovery.COLUMN_NAME_RecoveryTime + " TEXT",
////                ufinDbContract.WriteOffLoanRecovery.COLUMN_NAME_RecoveryDateTime + " TEXT",
////                ufinDbContract.WriteOffLoanRecovery.COLUMN_NAME_ReferenceRecoveryDate + " TEXT",
////        };
////
////        db.beginTransaction();
////        for (String column : newColumns2){
////            try {
////                db.execSQL(alterTableQuery2 + column);
////            } catch (Exception e) {
////                System.out.println(e.getMessage());
////            }
////        }
////        db.setTransactionSuccessful();
////        db.endTransaction();
//
//        try {
//            db.execSQL(ufinDbContract.ActivitySchedule.SQL_CREATE_ACTIVITY_SCHEDULE);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//    void dbMigrateVersion19To20(SQLiteDatabase db) {
//        try {
//            db.execSQL(ufinDbContract.OtherMFIData.SQL_CREATE_OTHER_MFI_DATA);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        try {
//            db.execSQL(ufinDbContract.SearchMemberData.SQL_CREATE_SEARCH_MEMBERDATA);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//    void dbMigrateVersion20To21(SQLiteDatabase db) {
//        String alterTableQuery = "ALTER TABLE " +
//                ufinDbContract.CreditValidityCheck.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns = {
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Role + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_AdhaarNo + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_RationCardNo + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_PAN_No + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_VoterID + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_FName + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_MName + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_LName + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Gender + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_DateOfBirth + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Address + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_City + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_State + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Pincode + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_ContactNo + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_CreditCheckProgress + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_CreditStatus + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_MemberType + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_ListType + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Remarks + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBEnquiryDate + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBScore + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBStatus + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_CBRemark + " TEXT",
////                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Age + " INTEGER",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_ProfessionID + " INTEGER",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_MainBorrowerRelationID + " INTEGER",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_IsFamilyMember + " INTEGER",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_MainBorrowerAdhaarNo + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_OwnHouse + " INTEGER",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_MaritalStatusID + " INTEGER",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_MonthlyIncome + " INTEGER",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_ClusterID + " INTEGER",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_CommunityID + " INTEGER",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Cluster + " TEXT",
//                ufinDbContract.CreditValidityCheck.COLUMN_NAME_Community + " TEXT",
//        };
//        db.beginTransaction();
//        for (String column : newColumns){
//            try {
//                db.execSQL(alterTableQuery + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//
//        String alterTableQuery2 = "ALTER TABLE " +
//                ufinDbContract.NewGroupMemberInfo.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns2 = {
//                ufinDbContract.NewGroupMemberInfo.COLUMN_NAME_ContactNo + " TEXT",
//        };
//        db.beginTransaction();
//        for (String column : newColumns2){
//            try {
//                db.execSQL(alterTableQuery2 + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//    }
//    void dbMigrateVersion21To22(SQLiteDatabase db) {
//        String alterTableQuery = "ALTER TABLE " +
//                ufinDbContract.PunarbhetSchedule.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns = {
//                ufinDbContract.PunarbhetSchedule.COLUMN_NAME_LongMaturityDate + " INTEGER",
//        };
//        db.beginTransaction();
//        for (String column : newColumns){
//            try {
//                db.execSQL(alterTableQuery + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//    }
//    void dbMigrateVersion22To23(SQLiteDatabase db) {
//        String alterTableQuery = "ALTER TABLE " +
//                ufinDbContract.MemberAddress.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns = {
//                ufinDbContract.MemberAddress.COLUMN_NAME_DigiPin + " TEXT",
//        };
//        db.beginTransaction();
//        for (String column : newColumns){
//            try {
//                db.execSQL(alterTableQuery + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//
//        String alterTableQuery2 = "ALTER TABLE " +
//                ufinDbContract.PunarbhetSchedule.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns2 = {
//                ufinDbContract.PunarbhetSchedule.COLUMN_NAME_UpFrontJLGBalance + " INTEGER",
//                ufinDbContract.PunarbhetSchedule.COLUMN_NAME_CurrentJLGBalance + " INTEGER",
//        };
//        db.beginTransaction();
//        for (String column : newColumns2){
//            try {
//                db.execSQL(alterTableQuery2 + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//    }
//    void dbMigrateVersion23To24(SQLiteDatabase db) {
//        String alterTableQuery = "ALTER TABLE " +
//                ufinDbContract.MemberRecovery.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns = {
//                ufinDbContract.MemberRecovery.COLUMN_NAME_PreClosureRequestDate + " TEXT",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_PreClosureRequestStatus + " INTEGER",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_PreClosurePrincipal + " INTEGER",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_PreClosureInterest + " INTEGER",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_PreClosureJLG + " INTEGER",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_PreClosureLatePayCharges + " INTEGER",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_PreClosureAmount + " INTEGER",
//        };
//        db.beginTransaction();
//        for (String column : newColumns){
//            try {
//                db.execSQL(alterTableQuery + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//    }
//    void dbMigrateVersion24To25(SQLiteDatabase db) {
//        String alterTableQuery = "ALTER TABLE " +
//                ufinDbContract.MemberRecovery.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns = {
////                ufinDbContract.MemberRecovery.COLUMN_NAME_PreClosureReason + " TEXT",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_PreClosureReasonID + " INTEGER",
//        };
//        db.beginTransaction();
//        for (String column : newColumns){
//            try {
//                db.execSQL(alterTableQuery + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//    }
//    void dbMigrateVersion25To26(SQLiteDatabase db) {
//        String alterTableQuery = "ALTER TABLE " +
//                ufinDbContract.MemberRecovery.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns = {
//                ufinDbContract.MemberRecovery.COLUMN_NAME_StdRecoveryDay + " TEXT",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_StdRecoveryTime + " TEXT",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_PrePaymentReqMonths + " INTEGER",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_PrePaymentReqAmount + " INTEGER",
////                ufinDbContract.MemberRecovery.COLUMN_NAME_PrePaymentApprovedMonths + " INTEGER",
////                ufinDbContract.MemberRecovery.COLUMN_NAME_PrePaymentApprovedAmount + " INTEGER",
//        };
//        db.beginTransaction();
//        for (String column : newColumns){
//            try {
//                db.execSQL(alterTableQuery + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//
//        String alterTableQuery2 = "ALTER TABLE " +
//                ufinDbContract.UserForTabMaster.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns2 = {
//                ufinDbContract.UserForTabMaster.COLUMN_NAME_StartOfDaySyncupTimestamp + " TEXT",
//                ufinDbContract.UserForTabMaster.COLUMN_NAME_EndOfDaySyncupTimestamp + " TEXT",
//        };
//        db.beginTransaction();
//        for (String column : newColumns2){
//            try {
//                db.execSQL(alterTableQuery2 + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//    }
//    void dbMigrateVersion26To27(SQLiteDatabase db) {
//        String alterTableQuery = "ALTER TABLE " +
//                ufinDbContract.MemberRecovery.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns = {
//                ufinDbContract.MemberRecovery.COLUMN_NAME_PrePaymentPrincipalCollected + " INTEGER",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_PrePaymentReqMonths + " INTEGER",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_PrePaymentReqAmount + " INTEGER",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_PrePaymentApprovedMonths + " INTEGER",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_PrePaymentApprovedAmount + " INTEGER",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_MaturityDate + " TEXT",
//        };
//        db.beginTransaction();
//        for (String column : newColumns){
//            try {
//                db.execSQL(alterTableQuery + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//    }
//    void dbMigrateVersion27To28(SQLiteDatabase db) {
//        String alterTableQuery = "ALTER TABLE " +
//                ufinDbContract.MemberRecovery.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns = {
//                ufinDbContract.MemberRecovery.COLUMN_NAME_Internal_Transfer_Reason + " TEXT",
//        };
//        db.beginTransaction();
//        for (String column : newColumns){
//            try {
//                db.execSQL(alterTableQuery + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//    }
//    void dbMigrateVersion28To29(SQLiteDatabase db) {
//        String alterTableQuery = "ALTER TABLE " +
//                ufinDbContract.MemberRecovery.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns = {
//                ufinDbContract.MemberRecovery.COLUMN_NAME_Monthly_ADHP + " INTEGER",
//        };
//        db.beginTransaction();
//        for (String column : newColumns){
//            try {
//                db.execSQL(alterTableQuery + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//
//        String setColumnQuery = "UPDATE " +
//                ufinDbContract.MemberRecovery.TABLE_NAME +
//                " SET " +
//                ufinDbContract.MemberRecovery.COLUMN_NAME_Monthly_ADHP +
//                " = " +
//                ufinDbContract.MemberRecovery.COLUMN_NAME_ADHPContributionAmount;
//        db.beginTransaction();
//        try {
//            db.execSQL(setColumnQuery);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//    }
//    void dbMigrateVersion29To30(SQLiteDatabase db) {
//        String alterTableQuery = "ALTER TABLE " +
//                ufinDbContract.MemberRecovery.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns = {
//                ufinDbContract.MemberRecovery.COLUMN_NAME_PrePaymentORPreClosure + " INTEGER",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_StandardEMI + " INTEGER",
////                ufinDbContract.MemberRecovery.COLUMN_NAME_PreClosureRejectReason + " TEXT",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_PreClosureRejectReasonID + " INTEGER",
//        };
//        db.beginTransaction();
//        for (String column : newColumns){
//            try {
//                db.execSQL(alterTableQuery + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//    }
//    void dbMigrateVersion30To31(SQLiteDatabase db) {
//        String alterTableQuery = "ALTER TABLE " +
//                ufinDbContract.MemberData.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns = {
//                ufinDbContract.MemberData.COLUMN_NAME_GroupGuarantor + " INTEGER",
//        };
//        db.beginTransaction();
//        for (String column : newColumns){
//            try {
//                db.execSQL(alterTableQuery + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//
//        String alterTableQuery2 = "ALTER TABLE " +
//                ufinDbContract.CollectionSummary.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns2 = {
//                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalPreClosureAdditionalEMIReceiptsCount + " INTEGER",
//                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalPreClosureAdditionalEMIReceiptsValue + " INTEGER",
//                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalADHPExtraReceiptsCount + " INTEGER",
//                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalADHPExtraReceiptsValue + " INTEGER",
//                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalNewRDExtraReceiptsCount + " INTEGER",
//                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalNewRDExtraReceiptsValue + " INTEGER",
//                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalFDExtraReceiptsCount + " INTEGER",
//                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalFDExtraReceiptsValue + " INTEGER",
//                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalJLGExtraReceiptsCount + " INTEGER",
//                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalJLGExtraReceiptsValue + " INTEGER",
//                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalReprintChargesExtraReceiptsCount + " INTEGER",
//                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalReprintChargesExtraReceiptsValue + " INTEGER",
//        };
//        db.beginTransaction();
//        for (String column : newColumns2){
//            try {
//                db.execSQL(alterTableQuery2 + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//    }
//    void dbMigrateVersion31To32(SQLiteDatabase db) {
//        String alterTableQuery = "ALTER TABLE " +
//                ufinDbContract.PunarbhetSchedule.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns = {
//                ufinDbContract.PunarbhetSchedule.COLUMN_NAME_CBTotalLoanBalance + " INTEGER",
//                ufinDbContract.PunarbhetSchedule.COLUMN_NAME_CBNoOfActiveLoans + " INTEGER",
//                ufinDbContract.PunarbhetSchedule.COLUMN_NAME_CBScore + " INTEGER",
//                ufinDbContract.PunarbhetSchedule.COLUMN_NAME_CBRemark + " TEXT",
//                ufinDbContract.PunarbhetSchedule.COLUMN_NAME_EnquiryDate + " TEXT",
//                ufinDbContract.PunarbhetSchedule.COLUMN_NAME_LastLoanStatus + " TEXT",
//        };
//        db.beginTransaction();
//        for (String column : newColumns){
//            try {
//                db.execSQL(alterTableQuery + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//
//        String alterTableQuery2 = "ALTER TABLE " +
//                ufinDbContract.MemberRecovery.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns2 = {
//                ufinDbContract.MemberRecovery.COLUMN_NAME_PastDue_PolicyRenewalAmt + " INTEGER",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_Expected_PolicyRenewalAmt + " INTEGER",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_Monthly_PolicyRenewalAmt + " INTEGER",
//                ufinDbContract.MemberRecovery.COLUMN_NAME_PreClosurePolicyRenewalAmt + " INTEGER",
//        };
//        db.beginTransaction();
//        for (String column : newColumns2){
//            try {
//                db.execSQL(alterTableQuery2 + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//
//        String alterTableQuery3 = "ALTER TABLE " +
//                ufinDbContract.DocumentPhoto.TABLE_NAME +
//                " ADD COLUMN ";
//        String[] newColumns3 = {
//                ufinDbContract.DocumentPhoto.COLUMN_NAME_MemberDocumentsID + " INTEGER",
//        };
//        db.beginTransaction();
//        for (String column : newColumns3){
//            try {
//                db.execSQL(alterTableQuery3 + column);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//    }
    void dbMigrateVersion32To33(SQLiteDatabase db) {
//bmp 07-Jul-26
        try {
            db.execSQL(ufinDbContract.OnlinePaymentApps.SQL_CREATE_ONLINE_PAYMENT_APPS_MASTER);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.MemberTransaction.SQL_CREATE_MEMBER_TRANSACTION);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            db.execSQL(ufinDbContract.RDTransaction.SQL_CREATE_RD_TRANSACTION);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
//bmp 07-Jul-26
        String alterTableQuery = "ALTER TABLE " +
                ufinDbContract.CollectionSummary.TABLE_NAME +
                " ADD COLUMN ";
        String[] newColumns = {
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalValueOnline + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalValueCash + " INTEGER",
//bmp 07-Jul-26
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashEMIReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashEMIReceiptsValue + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashADHPReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashADHPReceiptsValue + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashRDReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashRDReceiptsValue + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashAdditionalRepayReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashAdditionalRepayReceiptsValue + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashExtraReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashExtraReceiptsValue + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashPreClosureAdditionalEMIReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashPreClosureAdditionalEMIReceiptsValue + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashADHPExtraReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashADHPExtraReceiptsValue + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashNewRDExtraReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashNewRDExtraReceiptsValue + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashFDExtraReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashFDExtraReceiptsValue + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashJLGExtraReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashJLGExtraReceiptsValue + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashReprintChargesExtraReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalCashReprintChargesExtraReceiptsValue + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineEMIReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineEMIReceiptsValue + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineADHPReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineADHPReceiptsValue + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineRDReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineRDReceiptsValue + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineAdditionalRepayReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineAdditionalRepayReceiptsValue + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineExtraReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineExtraReceiptsValue + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlinePreClosureAdditionalEMIReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlinePreClosureAdditionalEMIReceiptsValue + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineADHPExtraReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineADHPExtraReceiptsValue + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineNewRDExtraReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineNewRDExtraReceiptsValue + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineFDExtraReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineFDExtraReceiptsValue + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineJLGExtraReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineJLGExtraReceiptsValue + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineReprintChargesExtraReceiptsCount + " INTEGER",
                ufinDbContract.CollectionSummary.COLUMN_NAME_TotalOnlineReprintChargesExtraReceiptsValue + " INTEGER",
//bmp 07-Jul-26
        };
        db.beginTransaction();
        for (String column : newColumns){
            try {
                db.execSQL(alterTableQuery + column);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();

        String alterTableQuery2 = "ALTER TABLE " +
                ufinDbContract.CenterCollection.TABLE_NAME +
                " ADD COLUMN ";
        String[] newColumns2 = {
                ufinDbContract.CenterCollection.COLUMN_NAME_TotalOnlineCollection + " INTEGER",
                ufinDbContract.CenterCollection.COLUMN_NAME_TotalCollection + " INTEGER",
//bmp 14-May-26
                ufinDbContract.CenterCollection.COLUMN_NAME_TotalCollectionValue + " INTEGER",
//bmp 14-May-26
        };
        db.beginTransaction();
        for (String column : newColumns2){
            try {
                db.execSQL(alterTableQuery2 + column);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();

        try {
            db.execSQL(ufinDbContract.OnlineCollection.SQL_CREATE_ONLINE_COLLECTION);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
//bmp 07-Jul-26
        String alterTableQuery3 = "ALTER TABLE " +
                ufinDbContract.MemberRecovery.TABLE_NAME +
                " ADD COLUMN ";
        String[] newColumns3 = {
                ufinDbContract.MemberRecovery.COLUMN_NAME_MemberStatus + " INTEGER",
        };
        db.beginTransaction();
        for (String column : newColumns3){
            try {
                db.execSQL(alterTableQuery3 + column);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
//bmp 07-Jul-26
    }
    void dbMigrateVersion33To34(SQLiteDatabase db) {
        String alterTableQuery1 = "ALTER TABLE " +
                ufinDbContract.NewGroupInfo.TABLE_NAME +
                " ADD COLUMN ";
        String[] newColumns1 = {
                ufinDbContract.NewGroupInfo.COLUMN_NAME_MemberCount + " INTEGER",
        };
        db.beginTransaction();
        for (String column : newColumns1){
            try {
                db.execSQL(alterTableQuery1 + column);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }
    void dbMigrateVersion34To35(SQLiteDatabase db) {
        String alterTableQuery1 = "ALTER TABLE " +
                ufinDbContract.UserForTabMaster.TABLE_NAME +
                " ADD COLUMN ";
        String[] newColumns1 = {
                ufinDbContract.UserForTabMaster.COLUMN_NAME_PaymentGatewayUsername + " TEXT",
                ufinDbContract.UserForTabMaster.COLUMN_NAME_PaymentGatewayPassword + " TEXT",
        };
        db.beginTransaction();
        for (String column : newColumns1){
            try {
                db.execSQL(alterTableQuery1 + column);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();

        String alterTableQuery2 = "ALTER TABLE " +
                ufinDbContract.MemberTransaction.TABLE_NAME +
                " ADD COLUMN ";
        String[] newColumns2 = {
                ufinDbContract.MemberTransaction.COLUMN_NAME_OnlinePaymentUPIID + " TEXT",
        };
        db.beginTransaction();
        for (String column : newColumns2){
            try {
                db.execSQL(alterTableQuery2 + column);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();

        String alterTableQuery3 = "ALTER TABLE " +
                ufinDbContract.RDTransaction.TABLE_NAME +
                " ADD COLUMN ";
        String[] newColumns3 = {
                ufinDbContract.RDTransaction.COLUMN_NAME_OnlinePaymentUPIID + " TEXT",
        };
        db.beginTransaction();
        for (String column : newColumns3){
            try {
                db.execSQL(alterTableQuery3 + column);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();

        String alterTableQuery4 = "ALTER TABLE " +
                ufinDbContract.OnlineCollection.TABLE_NAME +
                " ADD COLUMN ";
        String[] newColumns4 = {
                ufinDbContract.OnlineCollection.COLUMN_NAME_OnlinePaymentUPIID + " TEXT",
        };
        db.beginTransaction();
        for (String column : newColumns4){
            try {
                db.execSQL(alterTableQuery4 + column);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(ufinDbContract.OrgStructureMaster.SQL_CREATE_ORG_STRUCTURE_MASTER);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            db.execSQL(ufinDbContract.UserForTabMaster.SQL_CREATE_USER_FORTAB_MASTER);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.LoanRulesMaster.SQL_CREATE_LOAN_RULES_MASTER);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.StateMaster.SQL_CREATE_STATE_MASTER);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.ReligionMaster.SQL_CREATE_RELIGION_MASTER);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.RelationMaster.SQL_CREATE_RELATION_MASTER);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.QualificationMaster.SQL_CREATE_QUALIFICATION_MASTER);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.DocumentMaster.SQL_CREATE_DOCUMENT_MASTER);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.MaritalStatusMaster.SQL_CREATE_MARITALSTATUS_MASTER);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.HealthProblemMaster.SQL_CREATE_HEALTHPROBLEM_MASTER);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.HabbitAddictionMaster.SQL_CREATE_HABBITADDICTION_MASTER);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.BusinessMaster.SQL_CREATE_BUSINESS_MASTER);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.BankMaster.SQL_CREATE_BANK_MASTER);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.BankBranchMaster.SQL_CREATE_BANK_BRANCH_MASTER);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.CasteMaster.SQL_CREATE_CASTE_MASTER);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.ReasonMaster.SQL_CREATE_REASON_MASTER);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.LoanProductsMaster.SQL_CREATE_LOAN_PRODUCTS_MASTER);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.LoanProductDetailsMaster.SQL_CREATE_LOAN_PRODUCT_DETAILS_MASTER);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.OtherBorrowingSourcesMaster.SQL_CREATE_OTHER_BORROWING_SOURCES_MASTER);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.LoanUtilizationMaster.SQL_CREATE_LOAN_UTILIZATION_MASTER);
        } catch (Exception e) {
        }
//        try {
//            db.execSQL(ufinDbContract.LoanStatusMaster.SQL_CREATE_LOAN_STATUS_MASTER);
//        } catch (Exception e) {
//        }

        try {
            db.execSQL(ufinDbContract.MemberData.SQL_CREATE_MEMBER_DATA);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.MemberFamilyDetails.SQL_CREATE_MEMBER_FAMILY_DETAILS);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.PersonalImpactInfo.SQL_CREATE_PERSONAL_IMPACT_INFO);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.FamilyImpactInfo.SQL_CREATE_FAMILY_IMPACT_INFO);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.MemberAddress.SQL_CREATE_MEMBER_ADDRESS);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.MemberDocRcvd.SQL_CREATE_MEMBER_DOCUMENTS);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.DocumentPhoto.SQL_CREATE_DOCUMENT_PHOTO);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.NomineeDetails.SQL_CREATE_NOMINEE_DETAILS);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.OtherNominee.SQL_CREATE_OTHER_NOMINEE);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.MemberImpactAnswer.SQL_CREATE_MEMBER_IMPACT_ANSWERS);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.NewGroupInfo.SQL_CREATE_NEWGROUP_INFO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            db.execSQL(ufinDbContract.NewGroupMemberInfo.SQL_CREATE_NEWGROUPMEMBER_INFO);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            db.execSQL(ufinDbContract.MemberAdditionalDetails.SQL_CREATE_MEMBER_ADDITIONAL_DETAILS);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            db.execSQL(ufinDbContract.OrgStructure.SQL_CREATE_TD_ORG_STRUCTURE);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            db.execSQL(ufinDbContract.TablesToSync.SQL_CREATE_TABLES_TO_SYNC);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            db.execSQL(ufinDbContract.RecordsToSync.SQL_CREATE_RECORDS_TO_SYNC);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            db.execSQL(ufinDbContract.MessagesInbox.SQL_CREATE_MESSAGES_INBOX);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            db.execSQL(ufinDbContract.PunarbhetSchedule.SQL_CREATE_PUNARBHET_SCHEDULE);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            db.execSQL(ufinDbContract.PunarbhetVisitDetailsMatser.SQL_CREATE_PUNARBHET_VISITDETAILS_MASTER);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            db.execSQL(ufinDbContract.MemberRecovery.SQL_CREATE_MEMBER_RECOVERY);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
//bmp 08-Jun-26
        try {
            db.execSQL(ufinDbContract.MemberTransaction.SQL_CREATE_MEMBER_TRANSACTION);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
//bmp 08-Jun-26
        try {
            db.execSQL(ufinDbContract.RDReceipts.SQL_CREATE_RD_RECEIPTS);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            db.execSQL(ufinDbContract.RDTransaction.SQL_CREATE_RD_TRANSACTION);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            db.execSQL(ufinDbContract.OnlinePaymentApps.SQL_CREATE_ONLINE_PAYMENT_APPS_MASTER);
        } catch (Exception e) {
        }
        try {
            db.execSQL(ufinDbContract.CenterCollection.SQL_CREATE_CENTER_COLLECTION);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            db.execSQL(ufinDbContract.CollectionSummary.SQL_CREATE_COLLECTION_SUMMARY);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            db.execSQL(ufinDbContract.OnlineCollection.SQL_CREATE_ONLINE_COLLECTION);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
//        try {
//            db.execSQL(ufinDbContract.WriteOffLoanRecovery.SQL_CREATE_WRITEOFF_LOAN_RECOVERY);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
        try {
            db.execSQL(ufinDbContract.ActivitySchedule.SQL_CREATE_ACTIVITY_SCHEDULE);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            db.execSQL(ufinDbContract.OtherMFIData.SQL_CREATE_OTHER_MFI_DATA);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            db.execSQL(ufinDbContract.SearchMemberData.SQL_CREATE_SEARCH_MEMBERDATA);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            db.execSQL(ufinDbContract.CreditValidityCheck.SQL_CREATE_CREDIT_VALIDITY_CHECK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        onUpgrade(db, 1, DATABASE_VERSION);
    }
}
