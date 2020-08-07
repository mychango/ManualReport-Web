package com.smb.manualreport.service;

import com.smb.manualreport.bean.OpDispatchOrder;
import com.smb.manualreport.bean.SrcDispatchOrder;
import com.smb.manualreport.bean.WorkLog;
import com.smb.manualreport.mapper.DispatchListMapper;
import com.smb.manualreport.mapper.SourceDispatchMapper;
import com.smb.manualreport.mapper.WorkLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class DispatchInfoService {

    private static Logger logger = LoggerFactory.getLogger(DispatchInfoService.class);

    @Autowired
    private DispatchListMapper dispatchListMapper;

    @Autowired
    private WorkLogMapper workLogMapper;

    @Autowired
    private SourceDispatchMapper sourceDispatchMapper;

    public List<OpDispatchOrder> findOpDispatchOrderByStepAndWorkerOrMachine(String step, String worker, String machine){
        return dispatchListMapper.findOpDispatchOrderByStepAndWorkerOrMachine(step, worker, machine);
    }

    public OpDispatchOrder findOpDispatchOrderByUUID(String uuid){
        return dispatchListMapper.findOpDispatchOrderByUUID(uuid);
    }

    @Transactional(rollbackFor = Exception.class)
    public int SyncDispatchOrderFromSourceToOP(String step){
        //之後要改成讀檔案或者DB拿到最新Sync時間
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowDateString = sdf.format(new Date());

        List<SrcDispatchOrder> listSrcDispatchOrder = sourceDispatchMapper.findDispatchOrderByProcessAndTime(step, nowDateString);
        if(listSrcDispatchOrder == null || listSrcDispatchOrder.size() == 0) {
            logger.info("No dispatch order in smbsource.");
        } else {
            for(SrcDispatchOrder dp : listSrcDispatchOrder){
                logger.info("insert dispatch order into smp_op table dispatch_list: " + dp.getDispatchId());
                //將炬將的製程代碼Mapping回去公版
                if(dp.getProcessStep().equals("MA")){
                    dp.setProcessStep("PRGM");
                } else if(dp.getProcessStep().equals("MB")){
                    dp.setProcessStep("LASR");
                } else if(dp.getProcessStep().equals("MC")){
                    dp.setProcessStep("BEND");
                } else if(dp.getProcessStep().equals("MD")){
                    dp.setProcessStep("WELD");
                }
                dispatchListMapper.insertDispatchList(dp);
            }
        }
        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateDispatchStatusByUUID(String dispatchUUID, String status){
        return dispatchListMapper.updateDispatchStatusByUUID(dispatchUUID, status);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateDispatchOrderByReportStats(String dispatchUUID, String workerId, String machineId, String materialId, String processStep) {
        if(dispatchUUID == null || dispatchUUID.isEmpty()){
            logger.info(">>> Manual key-in, no dispatch order to update status");
            return 0;
        }
        if(machineId == null || machineId.isEmpty()) {
            machineId = null;
        }
        Boolean isException = false;
        OpDispatchOrder opDispatchOrder = dispatchListMapper.findOpDispatchOrderByUUID(dispatchUUID);
        List<WorkLog> listWorkLog = workLogMapper.findWorkLogByWorkerAndMachineAndMaterial(workerId, machineId, materialId);
        if (listWorkLog.size() == 0) {
            logger.error("!!!No work log, please check program!!!");
        } else {
            int state = listWorkLog.get(0).getState();
            if(state == 6){
                opDispatchOrder.setStatus("Exception");
                if(opDispatchOrder.getFinishCnt() != null){
                    opDispatchOrder.setFinishCnt(opDispatchOrder.getFinishCnt() + listWorkLog.get(0).getMaterialCnt());
                } else {
                    opDispatchOrder.setFinishCnt(listWorkLog.get(0).getMaterialCnt());
                }
            } else if(state == 4){
                opDispatchOrder.setStatus("Finish");
//                opDispatchOrder.setFinishCnt(listWorkLog.get(0).getMaterialCnt());
                opDispatchOrder.setFinishCnt(opDispatchOrder.getTotalCnt());
                opDispatchOrder.setActualFinishDt(listWorkLog.get(0).getCreateDt());
            } else {
                logger.error("!!!Last state is not finish state!!!");
            }
        }
        return dispatchListMapper.updateDispatchStatusAndCountByUUID(dispatchUUID, opDispatchOrder);
    }
}
