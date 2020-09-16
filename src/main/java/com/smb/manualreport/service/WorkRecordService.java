package com.smb.manualreport.service;

import com.alibaba.fastjson.JSON;
import com.smb.manualreport.bean.ElementLog;
import com.smb.manualreport.bean.OpDispatchOrder;
import com.smb.manualreport.bean.ReportLog;
import com.smb.manualreport.bean.WorkLog;
import com.smb.manualreport.config.ApiConfig;
import com.smb.manualreport.mapper.DispatchListMapper;
import com.smb.manualreport.mapper.ReportLogMapper;
import com.smb.manualreport.mapper.WorkLogMapper;
import com.smb.manualreport.utililty.Constant;
import com.smb.manualreport.utililty.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkRecordService {

    private static Logger logger = LoggerFactory.getLogger(WorkRecordService.class);

    @Autowired
    private WorkLogMapper workLogMapper;

    @Autowired
    private ReportLogMapper reportLogMapper;

    @Autowired
    private DispatchListMapper dispatchListMapper;

    @Autowired
    private ApiConfig apiConfig;

    public WorkLog findWorkLogByWorker(String workerId){
        List<WorkLog> listWorkLog = workLogMapper.findWorkLogByWorker(workerId);
        if(listWorkLog == null || listWorkLog.size() == 0){
            return null;
        }
        return listWorkLog.get(0);
    }

    @Transactional(rollbackFor = Exception.class)
    public int insertWorkLog(String workerId, String machineId, String materialId, String processStep, int materialCnt, int state, String dispatchUUID, String sessionId){
        logger.info(">>> [" + sessionId + "] " + "Start to insert work log with detail = " + workerId + " / " + machineId + " / " + materialId + " / " + processStep + " / " + materialCnt + " / " + state + " / " + dispatchUUID);
        if (machineId.isEmpty()){
            machineId = null;
        }
        if (dispatchUUID.isEmpty()){
            dispatchUUID = null;
        }
        WorkLog wl = new WorkLog();
        wl.setWorkerId(workerId);
        wl.setMachineId(machineId);
        wl.setMaterialId(materialId);
        wl.setProcessStep(processStep);
        wl.setMaterialCnt(materialCnt);
        wl.setState(state);
        wl.setDispatchUuid(dispatchUUID);
        wl.setWorkDesc(null);
        return workLogMapper.insertWorkLog(wl);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public int insertRecordLog(String dispatchUUID, String workerId, String machineId, String materialId, String processStep, int reportControl, String reportNotify, String sessionId) {
        logger.info(">>> [" + sessionId + "] " + "Start to insert record log with detail = " + workerId + " / " + machineId + " / " + dispatchUUID + " / " + materialId);
        if(machineId == null || machineId.isEmpty()) {
            machineId = null;
        }
        ReportLog rl = new ReportLog();
        if(dispatchUUID == null || dispatchUUID.isEmpty()){
            //do nothing
            rl.setDispatchId(null);
            rl.setMfgorderId(null);
        } else {
            //Get mfgorderId and dispatchId
            OpDispatchOrder opDispatchOrder = dispatchListMapper.findOpDispatchOrderByUUID(dispatchUUID);
            rl.setDispatchId(opDispatchOrder.getDispatchId());
            rl.setMfgorderId(opDispatchOrder.getMfgorderId());
        }
        rl.setWorkerId(workerId);
        rl.setMachineId(machineId);
        rl.setProcessStep(processStep);
        rl.setMaterialId(materialId);

        List<WorkLog> listWorkLog = workLogMapper.findWorkLogByWorkerAndMachineAndMaterial(workerId, machineId, materialId);
        if (listWorkLog.size() == 0) {
            logger.error(">>> [" + sessionId + "] " + "!!! No work log, please check program!!!");
        } else {
            for(WorkLog wl : listWorkLog){
                if (wl.getState() == 4 || wl.getState() == 3){
                    logger.info(">>> [" + sessionId + "] " + "Set complete info with state = " + wl.getState());
                    rl.setFinishDt(wl.getCreateDt());
                    rl.setFinishCnt(wl.getMaterialCnt());
                } else if(wl.getState() == 1){
                    logger.info(">>> [" + sessionId + "] " + "Set start info with state = 1");
                    rl.setStartDt(wl.getCreateDt());
                    rl.setTotalCnt(wl.getMaterialCnt());
                    //抓到這一次工作的源頭，跳出資料
                    break;
                }
            }
        }
        rl.setLeftCnt(rl.getTotalCnt() - rl.getFinishCnt());

        // 打 API 報工
        String apiUrl = apiConfig.getUrl() + ":" + apiConfig.getPort() + "/";
        ElementLog el = new ElementLog();
        el.setWorker_code(rl.getWorkerId());
        el.setMachine_code(rl.getMachineId());
        el.setElement_code(rl.getMaterialId());
        el.setFinish_number(rl.getFinishCnt());
        el.setStart_datetime(rl.getStartDt());
        el.setFinish_datetime(rl.getFinishDt());

        el.setStep_code(Constant.JUJIANG_PROCESS_MAP.get(rl.getProcessStep()));
        el.setFaile_number(rl.getLeftCnt());
        // 有開才打到SmartBOSS去
        if(reportControl == 1) {
            logger.info(">>> [" + sessionId + "] " + "Report ON: Start call smartboss api - newElement");
            try {
                Util.reportToSmartboss(JSON.toJSONString(el), apiUrl);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(">>> [" + sessionId + "] " + "!!!!! Please fix the miss report record !!!!!");
                Util.mailReportFailAlarm(reportNotify, el);
            }
        } else {
            logger.info(">>> [" + sessionId + "] " + "Report OFF: Bypass call smartboss api - newElement");
        }

        return reportLogMapper.insertReportLog(rl);
    }
}
