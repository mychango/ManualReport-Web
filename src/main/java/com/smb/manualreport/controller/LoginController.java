package com.smb.manualreport.controller;

import com.smb.manualreport.bean.MachineInfo;
import com.smb.manualreport.bean.OpDispatchOrder;
import com.smb.manualreport.bean.WorkLog;
import com.smb.manualreport.config.CustomConfig;
import com.smb.manualreport.service.DispatchInfoService;
import com.smb.manualreport.service.MachineInfoService;
import com.smb.manualreport.service.UserInfoService;
import com.smb.manualreport.service.WorkRecordService;
import com.smb.manualreport.utililty.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private WorkRecordService workRecordService;

    @Autowired
    private MachineInfoService machineInfoService;

    @Autowired
    private DispatchInfoService dispatchInfoService;

    @Autowired
    private CustomConfig customConfig;

    @RequestMapping("/")
    public String onluUrl(HttpServletRequest request, Model model) {
        return "redirect:/index";
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        //分客戶頁面
        String returnStr;
        switch(customConfig.getCode()){
            case "YJX":
                returnStr = "login3";
                break;
            case "PMCI":
            default:
                returnStr = "login2";
        }
        return returnStr;
    }

    @RequestMapping("/index")
    public String indexMapping(HttpServletRequest request, Model model){
        logger.info(">>> [" + request.getSession().getId() + "] " + request.getSession().getAttribute("userName").toString() + " - Login successful, start to redirect");
        Optional<Object> userArea = Optional.ofNullable(request.getSession().getAttribute("processCode"));
        String processStep = null;
        if (userArea.isPresent()){
            processStep = userArea.get().toString();
        }

        String workerId = request.getSession().getAttribute("userName").toString();
        logger.info(">>> [" + request.getSession().getId() + "] " + "Get work log to check un-closed work");
        WorkLog wl = workRecordService.findWorkLogByWorker(workerId);

//        logger.info(processStep);
        if(wl == null || wl.getState() == 4 || wl.getState() == 3) {
            logger.info(">>> [" + request.getSession().getId() + "] " + "No work to continue, redirect to select page");
            if (processStep != null && processStep.equals("WELD")) {
                return "redirect:/task/elementSelect";
            } else {
                return "redirect:/task/machineSelect";
            }
        } else {
            logger.info(">>> [" + request.getSession().getId() + "] " + "Resume previous un-closed work");
            String target = "/task/workStatus";
            if (wl.getDispatchUuid() != null) {
                target += "?dispatchUUID=" + wl.getDispatchUuid();
            }
            if (wl.getMachineId() != null){
                request.getSession().setAttribute("machineId", wl.getMachineId());
            }
            request.getSession().setAttribute("processCode", wl.getProcessStep());
            request.getSession().setAttribute("processName", Constant.PROCESS_MAP_LANGUAGE_MAP.get(wl.getProcessStep()));
            return "redirect:" + target;
        }
    }

    //thymeleaf測試用model傳遞參數
    @RequestMapping("/test")
    public String webTest(HttpServletRequest request, Model model) {
        model.addAttribute("name","mychango");
        return "testThymeleaf";
    }
}