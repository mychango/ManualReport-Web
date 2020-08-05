package com.smb.manualreport.service;

import com.smb.manualreport.bean.OpDispatchOrder;
import com.smb.manualreport.bean.SrcDispatchOrder;
import com.smb.manualreport.mapper.DispatchListMapper;
import com.smb.manualreport.mapper.SourceDispatchMapper;
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
    private SourceDispatchMapper sourceDispatchMapper;

//    @Transactional(rollbackFor = Exception.class)
    public List<OpDispatchOrder> findOpDispatchOrderByStepAndWorkerOrMachine(String step, String worker, String machine){
//    public Page<OpDispatchOrder> findOpDispatchOrderByStepAndWorkerOrMachine(String step, String worker, String machine, int page){
//        int pageSize = 10; //暫時寫死一頁10筆資料
//        int currentPage = page; //寫死從第一頁開始瀏覽
//        int startItem = currentPage * pageSize;
//        List<OpDispatchOrder> listOpDispatchOrder = dispatchListMapper.findOpDispatchOrderByStepAndWorkerOrMachine(step, worker, machine);
//        List<OpDispatchOrder> list;
//
//        logger.info(worker + " and " + machine);
//
//        if(listOpDispatchOrder.size() < startItem){
//            list = Collections.emptyList();
//        } else {
//            int toIndex = Math.min(startItem + pageSize, listOpDispatchOrder.size());
//            list = listOpDispatchOrder.subList(startItem, toIndex);
//        }
//
//        Page<OpDispatchOrder> dispatchOrderPage = new PageImpl<OpDispatchOrder>(list, PageRequest.of(currentPage, pageSize), listOpDispatchOrder.size());
//
//        return dispatchOrderPage;
        return dispatchListMapper.findOpDispatchOrderByStepAndWorkerOrMachine(step, worker, machine);
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
}
