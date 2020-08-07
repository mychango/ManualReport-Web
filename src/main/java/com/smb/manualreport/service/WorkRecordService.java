package com.smb.manualreport.service;

import com.smb.manualreport.bean.OpDispatchOrder;
import com.smb.manualreport.bean.ReportLog;
import com.smb.manualreport.bean.WorkLog;
import com.smb.manualreport.mapper.DispatchListMapper;
import com.smb.manualreport.mapper.ReportLogMapper;
import com.smb.manualreport.mapper.WorkLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public WorkLog findWorkLogByWorker(String workerId){
        logger.info(">>> Worker = " + workerId);
        List<WorkLog> listWorkLog = workLogMapper.findWorkLogByWorker(workerId);
        return listWorkLog.get(0);
    }

    @Transactional(rollbackFor = Exception.class)
    public int insertWorkLog(String workerId, String machineId, String materialId, String processStep, int materialCnt, int state, String dispatchUUID){
        if (machineId.isEmpty()){
            machineId = null;
        }
        if (dispatchUUID.isEmpty()){
            dispatchUUID = null;
        }
        WorkLog wl = new WorkLog(workerId, machineId, materialId, processStep, materialCnt, state, dispatchUUID);
        return workLogMapper.insertWorkLog(wl);
    }

    @Transactional(rollbackFor = Exception.class)
    public int insertRecordLog(String dispatchUUID, String workerId, String machineId, String materialId, String processStep){
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

        logger.info(workerId + " / " + machineId + " / " + materialId);

        List<WorkLog> listWorkLog = workLogMapper.findWorkLogByWorkerAndMachineAndMaterial(workerId, machineId, materialId);
        if (listWorkLog.size() == 0) {
            logger.error("!!! No work log, please check program!!!");
        } else {
            for(WorkLog wl : listWorkLog){
                if (wl.getState() == 4 || wl.getState() == 6){
                    logger.info(">>> Set complete info with state = " + wl.getState());
                    rl.setFinishDt(wl.getCreateDt());
                    rl.setFinishCnt(wl.getMaterialCnt());
                } else if(wl.getState() == 1){
                    logger.info(">>> Set start info with state = 1");
                    rl.setStartDt(wl.getCreateDt());
                    rl.setTotalCnt(wl.getMaterialCnt());
                    //抓到這一次工作的源頭，跳出資料
                    break;
                }
            }
        }
        rl.setLeftCnt(rl.getTotalCnt() - rl.getFinishCnt());
        return reportLogMapper.insertReportLog(rl);
    }
}
