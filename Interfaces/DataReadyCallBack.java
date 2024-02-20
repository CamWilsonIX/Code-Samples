package com.irrelevxnce.jblgroundscare.Interfaces;

import java.util.HashMap;

public interface DataReadyCallBack {
    void onDataReady(HashMap<String, Long> clientLatestEntry);
}
