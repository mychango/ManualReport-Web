package com.smb.manualreport.bean;

import java.io.Serializable;

public class WorkLog implements Serializable {
    private String workerId;
    private String materialId;
    private String machineId;
    private String processStep;
    private Integer materialCnt;
    private Integer state;

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getProcessStep() {
        return processStep;
    }

    public void setProcessStep(String processStep) {
        this.processStep = processStep;
    }

    public Integer getMaterialCnt() {
        return materialCnt;
    }

    public void setMaterialCnt(Integer materialCnt) {
        this.materialCnt = materialCnt;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
