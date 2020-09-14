package com.smb.manualreport.controller;

import com.alibaba.fastjson.JSON;
import com.smb.manualreport.bean.ApiReturn;
import com.smb.manualreport.bean.ElementLog;
import com.smb.manualreport.bean.OpDispatchOrder;
import com.smb.manualreport.bean.WorkLog;
import com.smb.manualreport.config.ApiConfig;
import com.smb.manualreport.config.CustomConfig;
import com.smb.manualreport.config.ReportConfig;
import com.smb.manualreport.service.DispatchInfoService;
import com.smb.manualreport.service.WorkRecordService;
import com.smb.manualreport.utililty.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/working")
public class WorkTrackController {

    private static Logger logger = LoggerFactory.getLogger(WorkTrackController.class);

    @Autowired
    private WorkRecordService workRecordService;

    @Autowired
    private DispatchInfoService dispatchInfoService;

    @Autowired
    private ReportConfig reportConfig;

    @Autowired
    private CustomConfig customConfig;

    @RequestMapping(value = "configtest")
    @ResponseBody
    public ResponseEntity<String> configtest(){
        logger.info(reportConfig.getControl().toString());
        logger.info(reportConfig.getRecipient());
        return new ResponseEntity<String>("OK",HttpStatus.OK);
    }

//    測試用Function
//    @Autowired
//    private ApiConfig apiConfig;
//
//    @RequestMapping(value = "configtest")
//    @ResponseBody
//    public ResponseEntity<String> configtest(){
//        logger.info(apiConfig.getUrl());
//        logger.info(String.valueOf(apiConfig.getPort()));
//        String apiUrl = apiConfig.getUrl() + ":" + apiConfig.getPort() + "/";
//        logger.info(apiUrl);
//        ElementLog el = new ElementLog();
//        el.setWorker_code("mychango");
//        el.setMachine_code("GuitarCreator-1");
//        el.setElement_code("Furch-23");
//        el.setFinish_number(2);
//        el.setStart_datetime("2020-08-14 15:58:58");
//        el.setFinish_datetime("2020-08-14 16:40:20");
//        String simulateStep = "LASR";
//        switch(simulateStep){
//            case "LASR":
//                el.setStep_code("MB");
//                break;
//            case "BEND":
//                el.setStep_code("MC");
//                break;
//            case "WELD":
//                el.setStep_code("MD");
//                break;
//            default:
//                //do nothing
//        }
//        el.setFaile_number(1);
//        logger.info(JSON.toJSONString(el));
//        Util.reportToSmartboss(JSON.toJSONString(el), apiUrl);
//        return new ResponseEntity<String>("OK",HttpStatus.OK);
//    }

    @RequestMapping(value = "/getCurrentWorkState")
    @ResponseBody
    public ResponseEntity<String> getCurrentWorkState(HttpServletRequest request, Model model){
        logger.info(">>> Start to get current work status");

        String workerId = request.getSession().getAttribute("nickName").toString();
        WorkLog wl = workRecordService.findWorkLogByWorker(workerId);

        return new ResponseEntity<String>(JSON.toJSONString(wl), HttpStatus.OK);
    }

    @RequestMapping(value = "/insertWorkLog", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> insertWorkLog(HttpServletRequest request, Model model, String workerId, String machineId, String materialId, String processStep, Integer materialCnt, Integer state, String dispatchUUID){

        logger.info(">>> Start to insert work log with detail = " + workerId + " / " + machineId + " / " + materialId + " / " + processStep + " / " + materialCnt + " / " + state + " / " + dispatchUUID);

        ApiReturn ar = new ApiReturn();
        try {
            workRecordService.insertWorkLog(workerId, machineId, materialId, processStep, materialCnt, state, dispatchUUID);
            ar.setRetMessage("");
            ar.setRetStatus("Success");
        } catch (Exception e) {
            e.printStackTrace();
            ar.setRetMessage(e.getMessage());
            ar.setRetStatus("Exception");
        }
        return new ResponseEntity<String>(JSON.toJSONString(ar), HttpStatus.OK);
    }

    @RequestMapping(value = "/updateDispatchStatus")
    @ResponseBody
    public ResponseEntity<String> updateDispatchStatus(HttpServletRequest request, Model model, String dispatchUUID, String status){
        logger.info(">>> Update dispatch status = " + status);

        ApiReturn ar = new ApiReturn();
        try {
            dispatchInfoService.updateDispatchStatusByUUID(dispatchUUID, status);
            ar.setRetMessage("");
            ar.setRetStatus("Success");
        } catch (Exception e) {
            e.printStackTrace();
            ar.setRetMessage(e.getMessage());
            ar.setRetStatus("Exception");
        }
        return new ResponseEntity<String>(JSON.toJSONString(ar), HttpStatus.OK);
    }

    @RequestMapping(value = "/reportWorkStats", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> reportWorkStats(HttpServletRequest request, Model model, String dispatchUUID, String workerId, String machineId, String materialId, String processStep){

        logger.info(">>> Start to report work stats, including [insert report log] and [update dispatch order status]");

        ApiReturn ar = new ApiReturn();
        try {
            workRecordService.insertRecordLog(dispatchUUID, workerId, machineId, materialId, processStep, reportConfig.getControl(), reportConfig.getRecipient());
            dispatchInfoService.updateDispatchOrderByReportStats(dispatchUUID, workerId, machineId, materialId, processStep);
            ar.setRetMessage("");
            ar.setRetStatus("Success");
        } catch (Exception e) {
            e.printStackTrace();
            ar.setRetMessage(e.getMessage());
            ar.setRetStatus("Exception");
        }
        return new ResponseEntity<String>(JSON.toJSONString(ar), HttpStatus.OK);
    }

    @RequestMapping("/jobFinish")
    public String jobFinish(HttpServletRequest request, Model model){
        String workerId = request.getSession().getAttribute("nickName").toString();
        WorkLog wl = workRecordService.findWorkLogByWorker(workerId);
        model.addAttribute("materialId", wl.getMaterialId());
        model.addAttribute("materialCnt", wl.getMaterialCnt());
        if(wl.getDispatchUuid() ==null || wl.getDispatchUuid().isEmpty()){
            //do nothing
        } else {
            model.addAttribute("dispatchUuid", wl.getDispatchUuid());
        }
        String returnStr;
        switch(customConfig.getCode()){
            case "YJX":
                returnStr = "jobFinish3";
                break;
            case "VIC":
            default:
                returnStr = "jobFinish2";
        }
        return returnStr;
//        return "jobFinish2";
    }

}
