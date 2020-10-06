package com.smb.manualreport.controller;

import com.smb.manualreport.utililty.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Controller
@RequestMapping("/util")
public class UtilController {

    private static Logger logger = LoggerFactory.getLogger(UtilController.class);

    @RequestMapping(value = "/changeSessionLanguage")
    public String changeSessionLanauage(HttpServletRequest request, HttpServletResponse response, String lang){

        logger.info(">>> Change language to " + lang);
        if(lang.equals(Constant.LANGUAGE_CHINESE)){
            request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, new Locale("zh", "TW"));
        } else if(lang.equals(Constant.LANGUAGE_ENGLISH)){
            request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, new Locale("en", "US"));
        } else if(lang.equals(Constant.LANGUAGE_VIETNAMESE)){
            request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, new Locale("vi", "VN"));
        }

        request.getSession().setAttribute("localeLang", lang);
        return "redirect:/index";
    }

}
