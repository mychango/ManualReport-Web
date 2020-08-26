package com.smb.manualreport.controller;

import com.alibaba.fastjson.JSON;
import com.smb.manualreport.bean.MachineInfo;
import com.smb.manualreport.bean.OpDispatchOrder;
import com.smb.manualreport.service.DispatchInfoService;
import com.smb.manualreport.service.MachineInfoService;
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

    @RequestMapping(value = "/findDispatchDetail", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> findDispatchDetail(HttpServletRequest request, Model model, String dispatchUUID){
        logger.info(">>> Using selected dispatch order to retrieve detail information : " + dispatchUUID);
        OpDispatchOrder selectedDispatchOrder = dispatchInfoService.findOpDispatchOrderByUUID(dispatchUUID);
        return new ResponseEntity<String>(JSON.toJSONString(selectedDispatchOrder), HttpStatus.OK);
    }

    @RequestMapping("/dpOrderSelect")
    public String dpOrderSelect(HttpServletRequest request, Model model, @RequestParam(name="selectMachine", required=false) String selectMachine) {

        Optional<Object> userArea = Optional.ofNullable(request.getSession().getAttribute("area"));
        String processStep = null;
        if (userArea.isPresent()){
            processStep = userArea.get().toString();
        }
        logger.info(">>> Sync dispatch information from smbsource!!");
        dispatchInfoService.SyncDispatchOrderFromSourceToOP(processStep);
        String selectWorker = request.getSession().getAttribute("nickName").toString();

        if (!processStep.equals("WELD")) {
            selectWorker = null;
            if (selectMachine == null || selectMachine.isEmpty()) {
                selectMachine = null;
            } else {
                request.getSession().setAttribute("machineId", selectMachine);
            }
        }
        if (request.getSession().getAttribute("machineId") != null){
            selectMachine = request.getSession().getAttribute("machineId").toString();
        }
        logger.info(">>> List unfinished dispatch order and start to select!");
        List<OpDispatchOrder> listOpDispatchOrder = dispatchInfoService.findOpDispatchOrderByStepAndWorkerOrMachine(processStep, selectWorker, selectMachine);
        model.addAttribute("listOpDispatchOrder", listOpDispatchOrder);
        return "dispatchOrderSelect2";
    }

    @RequestMapping("/dpOrderSelectOther")
    public String dpOrderSelect(HttpServletRequest request, Model model) {
        logger.info(">>> List unfinished dispatch order and start to select other tasks!");
        Optional<Object> userArea = Optional.ofNullable(request.getSession().getAttribute("area"));
        String processStep = null;
        if (userArea.isPresent()){
            processStep = userArea.get().toString();
        }
        String selectWorker = request.getSession().getAttribute("nickName").toString();
        if (!processStep.equals("WELD")) {
            selectWorker = null;
        }
        Optional<Object> machine = Optional.ofNullable(request.getSession().getAttribute("machineId"));
        String selectMachine = null;
        if(machine.isPresent()){
            selectMachine = machine.get().toString();
        }
        List<OpDispatchOrder> listOpDispatchOrder = dispatchInfoService.findOpDispatchOrderByStepAndWorkerOrMachine(processStep, selectWorker, selectMachine);
        model.addAttribute("listOpDispatchOrder", listOpDispatchOrder);
        return "dispatchOrderSelect2";
    }

    @RequestMapping("/machineSelect")
    public String machineSelect(HttpServletRequest request, Model model, String processStep){
        logger.info(">>> Please select machine before you start to confirm task!");
        List<MachineInfo> listMachineInfo = machineInfoService.findMachineByProcessStep(processStep);
        model.addAttribute("listMachineInfo", listMachineInfo);
        return "machineSelect";
    }

    @RequestMapping("/workStatus")
    public String confirmTask(HttpServletRequest request, Model model){
        return "workStatus";
    }
}
