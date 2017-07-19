package com.dto;

/**
 * Created by JoeHuang on 2017/7/18.
 */
public class Province {
    private String provinceCode;
    private String provinceName;


    public Province(String provinceCode, String provinceName) {
        this.provinceCode = provinceCode;
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
