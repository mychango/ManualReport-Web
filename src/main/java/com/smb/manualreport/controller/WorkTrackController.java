package com.smb.manualreport.controller;

import com.alibaba.fastjson.JSON;
import com.smb.manualreport.bean.ApiReturn;
import com.smb.manualreport.bean.OpDispatchOrder;
import com.smb.manualreport.bean.WorkLog;
import com.smb.manualreport.service.DispatchInfoService;
import com.smb.manualreport.service.WorkRecordService;
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
            workRecordService.insertRecordLog(dispatchUUID, workerId, machineId, materialId, processStep);
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
        model.addAttribute("materialId", wl.getMachineId());
        model.addAttribute("materialCnt", wl.getMaterialCnt());
        return "jobFinish";
    }

}
