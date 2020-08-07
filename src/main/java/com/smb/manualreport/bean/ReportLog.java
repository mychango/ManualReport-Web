package com.smb.manualreport.bean;

import java.io.Serializable;

public class ReportLog implements Serializable {
    private String workerId;
    private String machineId;
    private String mfgorderId;
    private String dispatchId;
    private String materialId;
    private String processStep;
    private Integer totalCnt;
    private Integer finishCnt;
    private Integer leftCnt;
    private String startDt;
    private String finishDt;

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMfgorderId() {
        return mfgorderId;
    }

    public void setMfgorderId(String mfgorderId) {
        this.mfgorderId = mfgorderId;
    }

    public String getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getProcessStep() {
        return processStep;
    }

    public void setProcessStep(String processStep) {
        this.processStep = processStep;
    }

    public Integer getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(Integer totalCnt) {
        this.totalCnt = totalCnt;
    }

    public Integer getFinishCnt() {
        return finishCnt;
    }

    public void setFinishCnt(Integer finishCnt) {
        this.finishCnt = finishCnt;
    }

    public Integer getLeftCnt() {
        return leftCnt;
    }

    public void setLeftCnt(Integer leftCnt) {
        this.leftCnt = leftCnt;
    }

    public String getStartDt() {
        return startDt;
    }

    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    public String getFinishDt() {
        return finishDt;
    }

    public void setFinishDt(String finishDt) {
        this.finishDt = finishDt;
    }
}
