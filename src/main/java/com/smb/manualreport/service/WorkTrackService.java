package com.smb.manualreport.service;

import com.smb.manualreport.mapper.WorkLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkTrackService {

    private static Logger logger = LoggerFactory.getLogger(WorkTrackService.class);

    @Autowired
    private WorkLogMapper workLogMapper;

    @Transactional(rollbackFor = Exception.class)
    public int insertWorkRecord(){
        return 0;
    }
}
