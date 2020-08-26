package com.smb.manualreport.mapper;

import com.smb.manualreport.bean.WorkLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface WorkLogMapper {

    @Insert("insert into smb_op.work_log " +
            "(worker_id, material_id, material_cnt, create_dt, state, machine_id, process_step, dispatch_uuid)" +
            "values " +
            "(#{workLog.workerId}, #{workLog.materialId}, #{workLog.materialCnt}, current_timestamp(), #{workLog.state}, #{workLog.machineId}, #{workLog.processStep}, #{workLog.dispatchUuid})")
    int insertWorkLog(@Param("workLog") WorkLog workLog);

    @Select("<script>"
            + "select worker_id, material_id, machine_id, process_step, material_cnt, state, DATE_FORMAT(create_dt, \"%Y-%m-%d %T\") as create_dt, dispatch_uuid from smb_op.work_log where 1 = 1 "
            + "<if test='machineId != null'> and machine_id = #{machineId} </if>"
            + "and worker_id = #{workerId} "
            + "and material_id = #{materialId} "
            + "order by create_dt desc"
            + "</script>")
    List<WorkLog> findWorkLogByWorkerAndMachineAndMaterial(@Param("workerId") String workerId, @Param("machineId") String machineId, @Param("materialId") String materialId);

    @Select("select worker_id, material_id, machine_id, process_step, material_cnt, state, DATE_FORMAT(create_dt, \"%Y-%m-%d %T\") as create_dt, dispatch_uuid from smb_op.work_log where worker_id = #{workerId} order by create_dt desc")
    List<WorkLog> findWorkLogByWorker(@Param("workerId") String workerId);
}
