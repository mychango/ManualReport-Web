package com.smb.manualreport.mapper;

import com.smb.manualreport.bean.ReportLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface ReportLogMapper {

    @Insert("insert into smb_op.report_log " +
            "(worker_id, machine_id, material_id, totoal_cnt, finish_cnt, left_cnt, process_step, start_dt, finish_dt, dispatch_id, mfgorder_id, create_dt)" +
            "values " +
            "(#{reportLog.workerId}, #{reportLog.machineId}, #{reportLog.materialId}, #{reportLog.totalCnt}, #{reportLog.finishCnt}, #{reportLog.leftCnt}, #{reportLog.processStep}, timestamp(#{reportLog.startDt}), timestamp(#{reportLog.finishDt}), #{reportLog.dispatchId}, #{reportLog.mfgorderId}, current_timestamp())")
    int insertReportLog(@Param("reportLog") ReportLog reportLog);
}
