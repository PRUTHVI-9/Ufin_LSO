package com.example.ufin_lso;

public class clsRDReceipt {
    public static final int COLLECTION_RDRECEIPT_STATUS_INIT                = 0;
    public static final int COLLECTION_RDRECEIPT_STATUS_SCHEDULED           = 1;
    public static final int COLLECTION_RDRECEIPT_STATUS_PARTIAL             = 2;
    public static final int COLLECTION_RDRECEIPT_STATUS_FULL                = 3;

    long CenterID;
    long OperatingMemberID;
    String OperatedBY;
    long FirstACMemberID;
    String FirstAcHolder;
    long RDReceiptID;
    long TransactionTimestamp;
    String TransactionDateTime;
    String TransactionType;
    long OnlinePaymentAppID;
    String OnlineTransactionID;
    String OnlineTransactionDate;
    String ScreenShotDate;
    int TransactionStatus;
    long RDDetailsID;
    String ReceiptNumber;
    String LastReceiptNumber;
    int PastDue;
    int ExpectedInstallmentAmt;
//bmp 21-Dec-23
    int MonthlyInstallmentAmt;
//bmp 21-Dec-23
    int CopyPastDue;
    int CopyExpectedInstallmentAmt;
    int ActualAmountReceived;
    int CollectionStatus;
    int InstallmentNo;

    public void InitRDReceipt() {
        CenterID = 0;
        OperatingMemberID = 0;
        OperatedBY = "";
        FirstACMemberID = 0;
        FirstAcHolder = "";
        RDReceiptID = 0;
        TransactionTimestamp = 0;
        TransactionDateTime = "";
        TransactionType = "";
        OnlinePaymentAppID = 0L;
        OnlineTransactionID = "";
        OnlineTransactionDate = "";
        ScreenShotDate = "";
        TransactionStatus = 0;
        RDDetailsID = 0;
        ReceiptNumber = "";
        LastReceiptNumber = "";
        PastDue = 0;
        ExpectedInstallmentAmt = 0;
//bmp 21-Dec-23
        MonthlyInstallmentAmt = 0;
//bmp 21-Dec-23
        CopyPastDue = 0;
        CopyExpectedInstallmentAmt = 0;
        ActualAmountReceived = 0;
        CollectionStatus = 0;
        InstallmentNo = 0;
    }
    public void Copy_RDReceipt(clsRDReceipt Obj) {
        Obj.CenterID = CenterID;
        Obj.OperatingMemberID = OperatingMemberID;
        Obj.OperatedBY = OperatedBY;
        Obj.FirstACMemberID = FirstACMemberID;
        Obj.FirstAcHolder = FirstAcHolder;
        Obj.RDReceiptID = RDReceiptID;
        Obj.TransactionTimestamp = TransactionTimestamp;
        Obj.TransactionDateTime = TransactionDateTime;
        Obj.TransactionType = TransactionType;
        Obj.OnlinePaymentAppID = OnlinePaymentAppID;
        Obj.OnlineTransactionID = OnlineTransactionID;
        Obj.OnlineTransactionDate = OnlineTransactionDate;
        Obj.ScreenShotDate = ScreenShotDate;
        Obj.TransactionStatus = TransactionStatus;
        Obj.RDDetailsID = RDDetailsID;
        Obj.ReceiptNumber = ReceiptNumber;
        Obj.LastReceiptNumber = LastReceiptNumber;
        Obj.PastDue = PastDue;
        Obj.ExpectedInstallmentAmt = ExpectedInstallmentAmt;
//bmp 21-Dec-23
        Obj.MonthlyInstallmentAmt = MonthlyInstallmentAmt;
//bmp 21-Dec-23
        Obj.CopyPastDue = CopyPastDue;
        Obj.CopyExpectedInstallmentAmt = CopyExpectedInstallmentAmt;
        Obj.ActualAmountReceived = ActualAmountReceived;
        Obj.CollectionStatus = CollectionStatus;
        Obj.InstallmentNo = InstallmentNo;
    }
}
