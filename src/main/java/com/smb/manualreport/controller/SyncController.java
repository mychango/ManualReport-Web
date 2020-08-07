package com.smb.manualreport.controller;

import com.smb.manualreport.service.DispatchInfoService;
import com.smb.manualreport.utililty.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SyncController {

    private static Logger logger = LoggerFactory.getLogger(SyncController.class);

    @Autowired
    private DispatchInfoService dispatchInfoService;

//    ***** 上班時間每兩小時執行一次 *****
//    @Scheduled(cron = "0 0 8,10,12,14,16 * * *")
    @RequestMapping("/tool/syncOrderTest")
    @ResponseBody
    public ResponseEntity<String> syncDispatchOrder(){
        logger.info(">>> Start to sync dispatch order from smbsource.");
        for(String s : Constant.JUJIANG_PROCESS_STEP){
            dispatchInfoService.SyncDispatchOrderFromSourceToOP(s);
        }
        return ResponseEntity.ok("Sync Done");
    }
}
