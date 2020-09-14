package com.smb.manualreport.controller;

import com.alibaba.fastjson.JSON;
import com.smb.manualreport.bean.MachineInfo;
import com.smb.manualreport.bean.OpDispatchOrder;
import com.smb.manualreport.config.CustomConfig;
import com.smb.manualreport.service.DispatchInfoService;
import com.smb.manualreport.service.MachineInfoService;
import com.smb.manualreport.utililty.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/task")
public class TaskConfirmController {

    private static Logger logger = LoggerFactory.getLogger(TaskConfirmController.class);

    @Autowired
    private MachineInfoService machineInfoService;

    @Autowired
    private DispatchInfoService dispatchInfoService;

    @Autowired
    private CustomConfig customConfig;

    @RequestMapping(value = "/findDispatchDetail", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> findDispatchDetail(HttpServletRequest request, Model model, String dispatchUUID){
        logger.info(">>> Using selected dispatch order to retrieve detail information : " + dispatchUUID);
        OpDispatchOrder selectedDispatchOrder = dispatchInfoService.findOpDispatchOrderByUUID(dispatchUUID);
        return new ResponseEntity<String>(JSON.toJSONString(selectedDispatchOrder), HttpStatus.OK);
    }

    @RequestMapping("/elementSelect")
    public String elementSelect(HttpServletRequest request, Model model, @RequestParam(name="selectMachine", required=false) String selectMachine) {
        Optional<Object> userArea = Optional.ofNullable(request.getSession().getAttribute("processCode"));
        String processStep = null;
        if (userArea.isPresent()){
            processStep = userArea.get().toString();
        }
        logger.info(">>> Sync dispatch information from smbsource!!");
        dispatchInfoService.SyncDispatchOrderFromSourceToOP(processStep);
        String selectWorker = request.getSession().getAttribute("nickName").toString();
        String userName = request.getSession().getAttribute("userName").toString();

        if (!processStep.equals("WELD")) {
            selectWorker = null;
            if (selectMachine == null || selectMachine.isEmpty()) {
                selectMachine = null;
            } else {
                request.getSession().setAttribute("machineId", selectMachine);
            }
        }
        //看須不須要指定機台來搜尋派工
        if(customConfig.getMachine() == 1) {
            logger.info(">>> Must select dispatch list with specific machine");
            if (request.getSession().getAttribute("machineId") != null) {
                selectMachine = request.getSession().getAttribute("machineId").toString();
            }
        } else {
            selectMachine = null;
        }
        logger.info(">>> List unfinished dispatch order and start to select!");
        List<OpDispatchOrder> listOpDispatchOrder;
        switch(customConfig.getCode()){
            case "YJX":
                listOpDispatchOrder = dispatchInfoService.findOpDispatchOrderByStepAndWorkerOrMachineWithProcessing(processStep, selectWorker, selectMachine);
                break;
            case "PMCI":
            default:
                listOpDispatchOrder = dispatchInfoService.findOpDispatchOrderByStepAndWorkerOrMachine(processStep, selectWorker, userName, selectMachine);
        }
        if(request.getSession().getAttribute("localeLang") == null || request.getSession().getAttribute("localeLang").toString().equals(Constant.LANGUAGE_CHINESE)) {
            for (OpDispatchOrder od : listOpDispatchOrder) {
                od.setStatus(Constant.DISPATCH_STATUS_MAP.get(od.getStatus()));
            }
        }
//        List<OpDispatchOrder> listOpDispatchOrder = dispatchInfoService.findOpDispatchOrderByStepAndWorkerOrMachine(processStep, selectWorker, selectMachine);
        model.addAttribute("listOpDispatchOrder", listOpDispatchOrder);
//        return "dispatchOrderSelect2";
        String returnStr;
        switch(customConfig.getCode()){
            case "YJX":
                returnStr = "dispatchOrderSelect3";
                break;
            case "PMCI":
            default:
                returnStr = "dispatchOrderSelect2";
        }
        return returnStr;
    }

    @RequestMapping("/elementSelectOther")
    public String elementSelectOther(HttpServletRequest request, Model model) {
        logger.info(">>> List unfinished dispatch order and start to select other tasks!");
        Optional<Object> userArea = Optional.ofNullable(request.getSession().getAttribute("processCode"));
        String processStep = null;
        if (userArea.isPresent()){
            processStep = userArea.get().toString();
        }
        logger.info(">>> Sync dispatch information from smbsource!!");
        dispatchInfoService.SyncDispatchOrderFromSourceToOP(processStep);
        String selectWorker = request.getSession().getAttribute("nickName").toString();
        String userName = request.getSession().getAttribute("userName").toString();
        if (!processStep.equals("WELD")) {
            selectWorker = null;
        }
        Optional<Object> machine = Optional.ofNullable(request.getSession().getAttribute("machineId"));
        String selectMachine = null;
        //看須不須要指定機台來搜尋派工
        if(customConfig.getMachine() == 1) {
            if (machine.isPresent()) {
                selectMachine = machine.get().toString();
            }
        }
        List<OpDispatchOrder> listOpDispatchOrder;
        switch(customConfig.getCode()){
            case "YJX":
                listOpDispatchOrder = dispatchInfoService.findOpDispatchOrderByStepAndWorkerOrMachineWithProcessing(processStep, selectWorker, selectMachine);
                break;
            case "PMCI":
            default:
                listOpDispatchOrder = dispatchInfoService.findOpDispatchOrderByStepAndWorkerOrMachine(processStep, selectWorker, userName, selectMachine);
        }
//        List<OpDispatchOrder> listOpDispatchOrder = dispatchInfoService.findOpDispatchOrderByStepAndWorkerOrMachine(processStep, selectWorker, selectMachine);
        //把 Status 轉中文
        if(request.getSession().getAttribute("localeLang") == null || request.getSession().getAttribute("localeLang").toString().equals(Constant.LANGUAGE_CHINESE)) {
            for (OpDispatchOrder od : listOpDispatchOrder) {
                od.setStatus(Constant.DISPATCH_STATUS_MAP.get(od.getStatus()));
            }
        }
        model.addAttribute("listOpDispatchOrder", listOpDispatchOrder);
//        return "dispatchOrderSelect2";
        String returnStr;
        switch(customConfig.getCode()){
            case "YJX":
                returnStr = "dispatchOrderSelect3";
                break;
            case "PMCI":
            default:
                returnStr = "dispatchOrderSelect2";
        }
        return returnStr;
    }

    @RequestMapping("/machineSelect")
    public String machineSelect(HttpServletRequest request, Model model, String processStep){
        logger.info(">>> Please select machine before you start to confirm task!");
        List<MachineInfo> listMachineInfo = machineInfoService.findMachineByProcessStep(processStep);
        model.addAttribute("listMachineInfo", listMachineInfo);
        return "machineSelect2";
    }

    @RequestMapping("/workStatus")
    public String confirmTask(HttpServletRequest request, Model model){
        String returnStr;
        switch(customConfig.getCode()){
            case "YJX":
                returnStr = "workStatus3";
                break;
            case "PMCI":
            default:
                returnStr = "workStatus2";
        }
        return returnStr;
//        return "workStatus2";
    }
}
