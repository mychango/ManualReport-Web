package com.smb.manualreport.bean;

import java.io.Serializable;
import java.util.Date;

public class WorkLog implements Serializable {
    private String workerId;
    private String materialId;
    private String machineId;
    private String processStep;
    private Integer materialCnt;
    private Integer state;
    private String createDt;
    private String dispatchUuid;
    private String workDesc;

//    public WorkLog(String workerId, String machineId, String materialId, String processStep, int materialCnt, int state, String createDt, String dispatchUuid){
//        this.workerId = workerId;
//        this.machineId = machineId;
//        this.materialId = materialId;
//        this.processStep = processStep;
//        this.materialCnt = materialCnt;
//        this.state = state;
//        this.createDt = createDt;
//        this.dispatchUuid = dispatchUuid;
//    }

    public String getWorkDesc() {
        return workDesc;
    }

    public void setWorkDesc(String workDesc) {
        this.workDesc = workDesc;
    }

    public String getDispatchUuid() {
        return dispatchUuid;
    }

    public void setDispatchUuid(String dispatchUuid) {
        this.dispatchUuid = dispatchUuid;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

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
