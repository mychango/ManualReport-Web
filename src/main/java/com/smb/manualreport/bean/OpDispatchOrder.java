package com.smb.manualreport.bean;

import com.smb.manualreport.utililty.Constant;

import java.io.Serializable;

public class OpDispatchOrder implements Serializable {
    private String uuid;
    private String mfgorderId;
    private String dispatchId;
    private String materialId;
    private Integer totalCnt;
    private String status;
    private String processStep;
    private String expectFinishDt;
    private String expectStartDt;
    private String assignWorker;
    private String assignMachine;
    private Integer finishCnt;
    private String actualFinishDt;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public Integer getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(Integer totalCnt) {
        this.totalCnt = totalCnt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
//        為了報表上的呈現
//        this.status = Constant.DISPATCH_STATUS_MAP.get(status);
        this.status = status;
    }

    public String getProcessStep() {
        return processStep;
    }

    public void setProcessStep(String processStep) {
        this.processStep = processStep;
    }

    public String getExpectFinishDt() {
        return expectFinishDt;
    }

    public void setExpectFinishDt(String expectFinishDt) {
        this.expectFinishDt = expectFinishDt;
    }

    public String getExpectStartDt() {
        return expectStartDt;
    }

    public void setExpectStartDt(String expectStartDt) {
        this.expectStartDt = expectStartDt;
    }

    public String getAssignWorker() {
        return assignWorker;
    }

    public void setAssignWorker(String assignWorker) {
        this.assignWorker = assignWorker;
    }

    public String getAssignMachine() {
        return assignMachine;
    }

    public void setAssignMachine(String assignMachine) {
        this.assignMachine = assignMachine;
    }

    public Integer getFinishCnt() {
        return finishCnt;
    }

    public void setFinishCnt(Integer finishCnt) {
        this.finishCnt = finishCnt;
    }

    public String getActualFinishDt() {
        return actualFinishDt;
    }

    public void setActualFinishDt(String actualFinishDt) {
        this.actualFinishDt = actualFinishDt;
    }
}
