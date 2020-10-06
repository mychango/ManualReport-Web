package com.smb.manualreport.bean;

public class ProductOrder {
    private String poId;
    private String customerName;
    private String customerCode;
    private int rawState;

    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public int getRawState() {
        return rawState;
    }

    public void setRawState(int rawState) {
        this.rawState = rawState;
    }
}
