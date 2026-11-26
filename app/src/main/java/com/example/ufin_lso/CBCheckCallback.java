package com.example.ufin_lso;

public interface CBCheckCallback {
    void onSuccess(String cbStatus);
    void onFailure(String message);
}
