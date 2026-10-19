package com.example.ufin_lso;

public class clsMemberAddress {
    long AddressID;
    String Address;
    String Pincode;
//bmp 13-Aug-25
    String DigiPin;
//bmp 13-Aug-25
    int IsActive;
    String DistrictID;

    public void InitNewMemberAddress() {
        AddressID = 0;
        Address = "Not Available";
        Pincode = "0";
//bmp 13-Aug-25
        DigiPin = "NA";
//bmp 13-Aug-25
        IsActive = 1;
        DistrictID = "06-16";
    }

    public boolean Compare_MemberAddress(clsMemberAddress Obj) {
        if(AddressID != Obj.AddressID)
            return false;
        if(!Address.equalsIgnoreCase(Obj.Address))
            return false;
        if(!Pincode.equalsIgnoreCase(Obj.Pincode))
            return false;
//bmp 13-Aug-25
        if(!DigiPin.equalsIgnoreCase(Obj.DigiPin))
            return false;
//bmp 13-Aug-25
        if(IsActive != Obj.IsActive)
            return false;
        if(!DistrictID.equalsIgnoreCase(Obj.DistrictID))
            return false;

        return true;
    }
    public void Copy_MemberAddress(clsMemberAddress Obj) {
        Obj.AddressID = AddressID;
        Obj.Address = Address;
        Obj.Pincode = Pincode;
//bmp 13-Aug-25
        Obj.DigiPin = DigiPin;
//bmp 13-Aug-25
        Obj.IsActive = IsActive;
        Obj.DistrictID = DistrictID;
    }
}
