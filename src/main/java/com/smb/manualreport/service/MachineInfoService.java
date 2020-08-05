package com.smb.manualreport.service;

import com.smb.manualreport.bean.MachineInfo;
import com.smb.manualreport.bean.UserInfo;
import com.smb.manualreport.mapper.MachineInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineInfoService {
    private static Logger logger = LoggerFactory.getLogger(MachineInfoService.class);

    @Autowired
    private MachineInfoMapper machineInfoMapper;

    public List<MachineInfo> findMachineByProcessStep(String processStep) {
        return machineInfoMapper.findMachineByProcessStep(processStep);
    }

}
