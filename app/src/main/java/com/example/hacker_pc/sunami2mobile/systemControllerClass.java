package com.example.hacker_pc.sunami2mobile;

/**
 * Created by Hacker-pc on 5/18/2017.
 */

public class systemControllerClass {
    /*Imei = ts.imei,
    Sim_Number = ts.sim_no,
    Provider = ts.provider,
    Version = ts.version,
    Reg_Date = ts.date_manufactured,
    Recorded_By = ts.recorded_by*/
    private String Imei;
    private String Sim_Number;
    private String Provider;
    private String Version;
    private String Reg_Date;
    private String Recorded_By;

    public String getSim_Number() {
        return Sim_Number;
    }

    public void setSim_Number(String sim_Number) {
        Sim_Number = sim_Number;
    }

    public String getProvider() {
        return Provider;
    }

    public void setProvider(String provider) {
        Provider = provider;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getReg_Date() {
        return Reg_Date;
    }

    public void setReg_Date(String reg_Date) {
        Reg_Date = reg_Date;
    }

    public String getRecorded_By() {
        return Recorded_By;
    }

    public void setRecorded_By(String recorded_By) {
        Recorded_By = recorded_By;
    }

    public String getImei() {
        return Imei;
    }

    public void setImei(String imei) {
        Imei = imei;
    }
}
