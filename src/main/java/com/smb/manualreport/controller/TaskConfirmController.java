package com.smb.manualreport.controller;

import com.smb.manualreport.bean.MachineInfo;
import com.smb.manualreport.bean.OpDispatchOrder;
import com.smb.manualreport.service.DispatchInfoService;
import com.smb.manualreport.service.MachineInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

//    @RequestMapping("/listDispatchOrder")
//    public String listDispatchOrder(HttpServletRequest request, Model model, @RequestParam("selectMachine") String selectMachine, @RequestParam("selectWorker") String selectWorker, @RequestParam("page") Optional<Integer> page){
//        logger.info("Access listDispatchOrder API!!");
//        Optional<Object> userArea = Optional.ofNullable(request.getSession().getAttribute("area"));
//        String processStep = null;
//        if (userArea.isPresent()){
//            processStep = userArea.get().toString();
//        }
//
//        int currentPage = page.orElse(1);
//        if (!processStep.equals("WELD")) {
//            selectWorker = null;
//        }
//        if (selectMachine.isEmpty()){
//            selectMachine = null;
//        }
//        /*
//            Page<OpDispatchOrder> dispatchOrderPage = dispatchInfoService.findOpDispatchOrderByStepAndWorkerOrMachine(processStep, selectWorker, selectMachine, currentPage-1);
//            model.addAttribute("worker", selectWorker);
//            model.addAttribute("machine", selectMachine);
//            model.addAttribute("dispatchOrderPage", dispatchOrderPage);
//            int totalPages = dispatchOrderPage.getTotalPages();
//            if(totalPages > 0){
//                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
//                model.addAttribute("pageNumbers", pageNumbers);
//            }
//            return "dispatchOrderSelect";
//        */
//        List<OpDispatchOrder> listOpDispatchOrder = dispatchInfoService.findOpDispatchOrderByStepAndWorkerOrMachine(processStep, null, selectMachine);
//        logger.info("Get wait/error dispatch order for worker/machine:" + listOpDispatchOrder.size());
//        model.addAttribute("listOpDispatchOrder", listOpDispatchOrder);
//        return "dispatchOrderSelect2";
//    }

    @RequestMapping("/dpOrderSelect")
    public String dpOrderSelect(HttpServletRequest request, Model model, @RequestParam(name="selectMachine", required=false) String selectMachine) {
        logger.info("List unfinished dispatch order and start to select!");
        Optional<Object> userArea = Optional.ofNullable(request.getSession().getAttribute("area"));
        String processStep = null;
        if (userArea.isPresent()){
            processStep = userArea.get().toString();
        }
        String selectWorker = request.getSession().getAttribute("nickName").toString();
        if (!processStep.equals("WELD")) {
            selectWorker = null;
        }
        if (selectMachine ==null || selectMachine.isEmpty()){
            selectMachine = null;
        }
        List<OpDispatchOrder> listOpDispatchOrder = dispatchInfoService.findOpDispatchOrderByStepAndWorkerOrMachine(processStep, selectWorker, selectMachine);
        model.addAttribute("listOpDispatchOrder", listOpDispatchOrder);
        return "dispatchOrderSelect2";
    }

    @RequestMapping("/machineSelect")
    public String machineSelect(HttpServletRequest request, Model model, String processStep){
        logger.info("Please select machine before you start to confirm task!");
        List<MachineInfo> listMachineInfo = machineInfoService.findMachineByProcessStep(processStep);
        model.addAttribute("listMachineInfo", listMachineInfo);
        return "machineSelect";
    }

    @RequestMapping("/workStatus")
    public String confirmTask(HttpServletRequest request, Model model){
        model.addAttribute("materialId", request.getParameter("materialId"));
        model.addAttribute("totalCnt", request.getParameter("totalCnt"));
        return "workStatus";
    }
}
