package com.smb.manualreport.service;

import com.smb.manualreport.bean.*;
import com.smb.manualreport.config.ApiConfig;
import com.smb.manualreport.config.CustomConfig;
import com.smb.manualreport.mapper.DispatchListMapper;
import com.smb.manualreport.mapper.SourceDispatchMapper;
import com.smb.manualreport.mapper.WorkLogMapper;
import com.smb.manualreport.utililty.Constant;
import com.smb.manualreport.utililty.Util;
import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
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

    @Autowired
    private CustomConfig customConfig;

    public List<OpDispatchOrder> findOpDispatchOrderByStepAndWorkerOrMachine(String step, String worker, String nickName, String machine, String sessionId){
        logger.info(">>> [" + sessionId + "] " + "find task with: Worker = " + worker + " / machine = " + machine + " / process = " + step);
        return dispatchListMapper.findOpDispatchOrderByStepAndWorkerOrMachine(step, worker, nickName, machine);
    }

    public List<OpDispatchOrder> findOpDispatchOrderByStepAndWorkerOrMachineWithProcessing(String step, String worker, String machine, String sessionId){
        logger.info(">>> [" + sessionId + "] " + "find task includes processing status with: Worker = " + worker + " / machine = " + machine + " / process = " + step);
        return dispatchListMapper.findOpDispatchOrderByStepAndWorkerOrMachineWithProcessing(step, worker, machine);
    }

    public OpDispatchOrder findOpDispatchOrderByUUID(String uuid, String sessionId){
        logger.info(">>> [" + sessionId + "] " + "find dispatch task with uuid = " + uuid);
        return dispatchListMapper.findOpDispatchOrderByUUID(uuid);
    }

    @Transactional(rollbackFor = Exception.class)
    public int SyncDispatchOrderFromSourceToOP(String step, String sessionId){
        logger.info(">>> [" + sessionId + "] " + "Start to sync dispatch detail from smbsource");
        //之後要改成讀檔案或者DB拿到最新Sync時間
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String lastQueryDateStr = sdf.format(new Date());
        String lastSyncDateStr = dispatchListMapper.findLastUpdateTimeByProcess(step);
        String lastOrderSyncDateStr = null;
        if (lastSyncDateStr == null || lastSyncDateStr.isEmpty()){
            logger.info(">>> [" + sessionId + "] " + "No last sync date for reference");
            try {
                lastSyncDateStr = sdf.format(sdf.parse(customConfig.getSync()));
                lastOrderSyncDateStr = sdf.format(sdf.parse(customConfig.getOrdersync()));
                logger.info(">>> [" + sessionId + "] " + "Set order sync date from config = " + lastOrderSyncDateStr);
                logger.info(">>> [" + sessionId + "] " + "Set sync date from config = " + lastSyncDateStr + " and process = " + step);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            lastOrderSyncDateStr = lastSyncDateStr;
            logger.info(">>> [" + sessionId + "] " + "last order sync date = " + lastOrderSyncDateStr);
            logger.info(">>> [" + sessionId + "] " + "last sync date = " + lastSyncDateStr + " and process = " + step);
        }

        // 炬將專屬 Process Mapping
        List<SrcDispatchOrder> listSrcDispatchOrder = sourceDispatchMapper.findDispatchOrderByProcessAndTime(Constant.JUJIANG_PROCESS_MAP.get(step), lastSyncDateStr);
        if(listSrcDispatchOrder == null || listSrcDispatchOrder.size() == 0) {
            logger.info(">>> [" + sessionId + "] " + "No dispatch order need to update in smbsource.");
        } else {
            for(SrcDispatchOrder dp : listSrcDispatchOrder){
                logger.info(">>> [" + sessionId + "] " + "Check dispatch info exists or not: " + dp.getMfgorderId() + "/" + dp.getDispatchId() + "/" + dp.getProcessStep());
                OpDispatchOrder opDispatchOrder = dispatchListMapper.findOnlyOpDispatchOrderByDispatchInfo(dp.getMfgorderId(), dp.getDispatchId(), step, dp.getMaterialId());
                if(opDispatchOrder != null){
                    logger.info(">>> [" + sessionId + "] " + "Update dispatch order with uuid = " + opDispatchOrder.getUuid() + ", set the new information!!!");
                    //20200915 取出原本的指定工人，修正dispatch已被開工後修改assignWorker的Exception
                    String beforeWorker = dispatchListMapper.findAssignWorkerByUUID(opDispatchOrder.getUuid());
                    dispatchListMapper.updateDispatchExpectDetailByUUID(opDispatchOrder.getUuid(), dp.getExpectWorker(), dp.getExpectMachine(), dp.getExpectOnline(), dp.getExpectOffline());
                    //20200915 判斷是否有已開工的記錄並insert一筆異常完工，修正dispatch已被開工後修改assignWorker的Exception
                    if(beforeWorker != null && !beforeWorker.equals(dp.getExpectWorker())){
                        logger.warn(">>> [" + sessionId + "] " + "Discovcer the dispatch worker has been changed from " + beforeWorker + " to " + dp.getExpectWorker());
                        WorkLog wl = workLogMapper.findLastWorkLogByDpIdAndWorker(opDispatchOrder.getUuid(), beforeWorker);
                        if(wl != null && wl.getState() != 3 && wl.getState() != 4){
                            logger.warn(">>> [" + sessionId + "] " + "Discovcer the dispatch task has been fetched but also assigned to other worker, need fix");
                            wl.setState(3);
                            wl.setWorkDesc("人員切換，強制結束");
                            workLogMapper.insertWorkLog(wl);
                        }
                    }
                } else {
                    logger.info(">>> [" + sessionId + "] " + "Insert new dispatch order into smp_op table dispatch_list");
                    if(dp.getMaterialId() == null || dp.getExpectAmount() == null){
                        logger.error(">>> [" + sessionId + "] " + "Miss material/count information, cannot insert this dispatch task!!");
                    } else {
                        dp.setProcessStep(Constant.JUJIANG_PROCESS_MAP.get(dp.getProcessStep()));
                        dispatchListMapper.insertDispatchList(dp);
                    }
                }
            }
        }

//        List<ProductOrder> listProductOrder = sourceDispatchMapper.findProductOrderByTime("2020-01-01");
        List<ProductOrder> listProductOrder = sourceDispatchMapper.findProductOrderByTime(lastOrderSyncDateStr);
        if(listProductOrder == null || listProductOrder.size() == 0){
            logger.info(">>> [" + sessionId + "] " + "No product order need to update in smbsource.");
        } else {
            for(ProductOrder po : listProductOrder){
                if(po.getRawState().equals(Constant.PO_STATUS_ONGOING)) {
                    logger.info(">>> [" + sessionId + "] " + "PO:" + po.getPoId() + " state = " + po.getRawState() + ", do nothing");
                } else {
                    logger.info(">>> [" + sessionId + "] " + "PO:" + po.getPoId() + " state = " + po.getRawState() + ", ready to delete dispatch by mfgorderId");
                    List<String> listMfgorderId = sourceDispatchMapper.findMfgOrderIdByProductOrder(po.getPoId());
                    for(String mfgorderId : listMfgorderId) {
                        logger.info(">>> Delete dispatch order by mfgorder - " + mfgorderId);
                        dispatchListMapper.softDeleteDispatchOrderByMfgOrder(mfgorderId);
                    }
                }
            }
        }

        //從訂單狀態向下決定誰要sync
//        List<ProductOrder> listProductOrder = sourceDispatchMapper.findProductOrderByTime(lastSyncDateStr);
//        if(listProductOrder == null || listProductOrder.size() == 0){
//            logger.info(">>> No product order need to update in smbsource.");
//        } else {
//            for(ProductOrder po : listProductOrder){
//                if(po.getRawState().equals(Constant.PO_STATUS_ONGOING)) {
//                    logger.info(">>> PO:" + po.getPoId() + " state = " + Constant.PO_STATUS_ONGOING + ", ready to check dispatch");
//                    List<SrcDispatchOrder> listSrcDispatchOrder = sourceDispatchMapper.findDispatchOrderByProcessAndOrderAndTime(Constant.JUJIANG_PROCESS_MAP.get(step), po.getPoId(), lastSyncDateStr);
//                    if (listSrcDispatchOrder == null || listSrcDispatchOrder.size() == 0) {
//                        logger.info(">>> No dispatch order need to update for [" + po.getPoId() + "] in smbsource.");
//                    } else {
//                        for (SrcDispatchOrder dp : listSrcDispatchOrder) {
//                            logger.info(">>> check dispatch info exists or not: " + dp.getMfgorderId() + "/" + dp.getDispatchId() + "/" + step + "/" + dp.getMaterialId());
//                            OpDispatchOrder opDispatchOrder = dispatchListMapper.findOnlyOpDispatchOrderByDispatchInfo(dp.getMfgorderId(), dp.getDispatchId(), step, dp.getMaterialId());
//                            if (opDispatchOrder != null) {
//                                logger.info(">>> Update dispatch order with uuid = " + opDispatchOrder.getUuid() + ", set the new information!!!");
//                                dispatchListMapper.updateDispatchExpectDetailByUUID(opDispatchOrder.getUuid(), dp.getExpectWorker(), dp.getExpectMachine(), dp.getExpectOnline(), dp.getExpectOffline());
//                            } else {
//                                logger.info(">>> Insert new dispatch order into smp_op table dispatch_list");
//                                if (dp.getMaterialId() == null || dp.getExpectAmount() == null) {
//                                    logger.error(">>> Miss material/count information, cannot insert this dispatch task!!");
//                                } else {
//                                    dp.setProcessStep(Constant.JUJIANG_PROCESS_MAP.get(dp.getProcessStep()));
//                                    dispatchListMapper.insertDispatchList(dp);
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    logger.info(">>> PO:" + po.getPoId() + " ready to delete dispatch by mfgorderId");
//                    List<String> listMfgorderId = sourceDispatchMapper.findMfgOrderIdByProductOrder(po.getPoId());
//                    for(String mfgorderId : listMfgorderId) {
//                        logger.info(">>> Delete dispatch order by mfgorder - " + mfgorderId);
//                        dispatchListMapper.softDeleteDispatchOrderByMfgOrder(mfgorderId);
//                    }
//                }
//            }
//        }

        return 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateDispatchStatusByUUID(String dispatchUUID, String status, String sessionId){
        logger.info(">>> [" + sessionId + "] " + "Update dispatch task status = " + status + " with dispatch UUID = " + dispatchUUID);
        return dispatchListMapper.updateDispatchStatusByUUID(dispatchUUID, status);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateDispatchOrderByReportStats(String dispatchUUID, String workerId, String machineId, String materialId, String processStep, String sessionId) {
        if(dispatchUUID == null || dispatchUUID.isEmpty()){
            logger.info(">>> [" + sessionId + "] " + "Manual key-in, no dispatch order to update status");
            return 0;
        }
        logger.info(">>> [" + sessionId + "] " + "Start to update dispatch order");
        if(machineId == null || machineId.isEmpty()) {
            machineId = null;
        }
        Boolean isException = false;
        OpDispatchOrder opDispatchOrder = dispatchListMapper.findOpDispatchOrderByUUID(dispatchUUID);
        List<WorkLog> listWorkLog = workLogMapper.findWorkLogByWorkerAndMachineAndMaterial(workerId, machineId, materialId);
        if (listWorkLog.size() == 0) {
            logger.error(">>> [" + sessionId + "] " + "!!!No work log, please check program!!!");
        } else {
            int state = listWorkLog.get(0).getState();
            if(state == 3){
                opDispatchOrder.setStatus("Exception");
                if(opDispatchOrder.getFinishCnt() != null){
                    opDispatchOrder.setFinishCnt(opDispatchOrder.getFinishCnt() + listWorkLog.get(0).getMaterialCnt());
                } else {
                    opDispatchOrder.setFinishCnt(listWorkLog.get(0).getMaterialCnt());
                }
            } else if(state == 4){
                //第一次修正，因為可能會有Exception後再次領走task且正常完成報工
//                opDispatchOrder.setFinishCnt(listWorkLog.get(0).getMaterialCnt());
                //第二次修正，因為永進興每次都只卡1個，所以必須得用數量來判斷是否完成
//                opDispatchOrder.setStatus("Finish");
//                opDispatchOrder.setFinishCnt(opDispatchOrder.getTotalCnt());
//                opDispatchOrder.setActualFinishDt(listWorkLog.get(0).getCreateDt());
                if(opDispatchOrder.getFinishCnt() != null){
                    opDispatchOrder.setFinishCnt(opDispatchOrder.getFinishCnt() + listWorkLog.get(0).getMaterialCnt());
                } else {
                    opDispatchOrder.setFinishCnt(listWorkLog.get(0).getMaterialCnt());
                }
                if(opDispatchOrder.getFinishCnt() >= opDispatchOrder.getTotalCnt()){
                    opDispatchOrder.setActualFinishDt(listWorkLog.get(0).getCreateDt());
                    opDispatchOrder.setStatus("Finish");
                }
            } else {
                logger.error(">>> [" + sessionId + "] " + "!!!Last state is not finish state!!!");
            }
        }
        logger.info(">>> [" + sessionId + "] " + "Finish to update dispatch order for dispatch UUID = " + dispatchUUID + " with status = " + opDispatchOrder.getStatus());
        return dispatchListMapper.updateDispatchStatusAndCountByUUID(dispatchUUID, opDispatchOrder);
    }
}
