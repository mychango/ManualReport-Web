package com.smb.manualreport.mapper;

import com.smb.manualreport.bean.WorkLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface WorkLogMapper {

    @Insert("insert into work_log " +
            "(worker_id, material_id, material_cnt, create_dt, state, machine_id, process_step)" +
            "values " +
            "(#{workLog.workerId}, #{workLog.materialId}, #{workLog.materialCnt}, current_timestamp(), #{workLog.state}, #{workLog.machineId}, #{workLog.processStep})")
    int insertWorkLog(@Param("workLog") WorkLog workLog);
}
