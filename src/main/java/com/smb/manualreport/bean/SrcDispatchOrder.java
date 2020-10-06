package com.smb.manualreport.bean;

import java.io.Serializable;

public class SrcDispatchOrder implements Serializable {
    private String dispatchId;
    private String mfgorderId;
    private String materialId;
    private Integer expectAmount;
    private String expectOnline;
    private String expectOffline;
    private String expectWorker;
    private String expectMachine;
    private String processStep;

    public SrcDispatchOrder(String dispatchId, String mfgorderId, String materialId, Integer expectAmount, String expectOnline, String expectOffline, String expectWorker, String expectMachine, String processStep) {
        this.dispatchId = dispatchId;
        this.mfgorderId = mfgorderId;
        this.materialId = materialId;
        this.expectAmount = expectAmount;
        this.expectOnline = expectOnline;
        this.expectOffline = expectOffline;
        this.expectWorker = expectWorker;
        this.expectMachine = expectMachine;
        this.processStep = processStep;
    }

    public String getMfgorderId() {
        return mfgorderId;
    }

    public void setMfgorderId(String mfgorderId) {
        this.mfgorderId = mfgorderId;
    }

    public Integer getExpectAmount() {
        return expectAmount;
    }

    public void setExpectAmount(Integer expectAmount) {
        this.expectAmount = expectAmount;
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

    public String getExpectOnline() {
        return expectOnline;
    }

    public void setExpectOnline(String expectOnline) {
        this.expectOnline = expectOnline;
    }

    public String getExpectOffline() {
        return expectOffline;
    }

    public void setExpectOffline(String expectOffline) {
        this.expectOffline = expectOffline;
    }

    public String getExpectWorker() {
        return expectWorker;
    }

    public void setExpectWorker(String expectWorker) {
        this.expectWorker = expectWorker;
    }

    public String getExpectMachine() {
        return expectMachine;
    }

    public void setExpectMachine(String expectMachine) {
        this.expectMachine = expectMachine;
    }

    public String getProcessStep() {
        return processStep;
    }

    public void setProcessStep(String processStep) {
        this.processStep = processStep;
    }
}
