package com.smb.manualreport.controller;

import com.smb.manualreport.bean.MachineInfo;
import com.smb.manualreport.bean.OpDispatchOrder;
import com.smb.manualreport.service.DispatchInfoService;
import com.smb.manualreport.service.MachineInfoService;
import com.smb.manualreport.service.UserInfoService;
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

//    @Autowired
//    private UserInfoService userInfoService;

    @Autowired
    private MachineInfoService machineInfoService;

    @Autowired
    private DispatchInfoService dispatchInfoService;

    @RequestMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        return "login";
    }

    @RequestMapping("/index")
    public String indexMapping(HttpServletRequest request, Model model){
        logger.info(">>> Login successful, start to redirect");
        Optional<Object> userArea = Optional.ofNullable(request.getSession().getAttribute("area"));
        String processStep = null;
        if (userArea.isPresent()){
            processStep = userArea.get().toString();
        }

        if(processStep != null && processStep.equals("WELD")){
            return "redirect:/task/dpOrderSelect";
        } else {
            return "redirect:/task/machineSelect";
        }
    }

    //thymeleaf測試用model傳遞參數
    @RequestMapping("/test")
    public String webTest(HttpServletRequest request, Model model) {
        model.addAttribute("name","mychango");
        return "testThymeleaf";
    }
}