package com.example.ufin_lso.networkmonitor;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import okhttp3.ResponseBody;

public interface NetworkApiService {

    @POST("network-monitor")
    Call<ResponseBody> sendNetworkData(
            @Body NetworkInfoData networkInfoData
    );
}