package com.example.bvideojuegosrest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamAppResult {
    private int appid;
    private SteamAppData data;

    public int getAppid() { return appid; }
    public void setAppid(int appid) { this.appid = appid; }
    public SteamAppData getData() { return data; }
    public void setData(SteamAppData data) { this.data = data; }
}
