package com.smb.manualreport.bean;

import java.io.Serializable;

public class MachineInfo implements Serializable {
    private String machineId;
    private String machineType;
    private String machineDesc;
    private String machineRemark;

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public String getMachineDesc() {
        return machineDesc;
    }

    public void setMachineDesc(String machineDesc) {
        this.machineDesc = machineDesc;
    }

    public String getMachineRemark() {
        return machineRemark;
    }

    public void setMachineRemark(String machineRemark) {
        this.machineRemark = machineRemark;
    }
}
