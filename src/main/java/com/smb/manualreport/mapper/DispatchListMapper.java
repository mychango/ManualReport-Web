package com.smb.manualreport.mapper;

import com.smb.manualreport.bean.OpDispatchOrder;
import com.smb.manualreport.bean.SrcDispatchOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface DispatchListMapper {

    @Insert("insert into smb_op.dispatch_list " +
        "(dispatch_id, material_id, total_cnt, finish_cnt, assign_worker, assign_machine, expect_finish_dt, actual_finish_dt, process_step, status, expect_start_dt, mfgorder_id)" +
        "values " +
        "(#{srcDispatchOrder.dispatchId}, #{srcDispatchOrder.materialId}, #{srcDispatchOrder.expectAmount}, null, #{srcDispatchOrder.expectWorker}, #{srcDispatchOrder.expectMachine}, #{srcDispatchOrder.expectOffline}, null, #{srcDispatchOrder.processStep}, 'Wait', #{srcDispatchOrder.expectOnline}, #{srcDispatchOrder.mfgorderId})")
    int insertDispatchList(@Param("srcDispatchOrder") SrcDispatchOrder srcDispatchOrder);

    @Select("<script>"
            + "select * from smb_op.dispatch_list where 1 = 1 "
            + "<if test='workerId != null'> and assign_worker = #{workerId} </if>"
            + "<if test='machineId != null'> and assign_machine = #{machineId} </if>"
            + "and process_step = #{processStep} "
            + "and status not in ('Finish', 'In Process') "
            + "</script>")
    List<OpDispatchOrder> findOpDispatchOrderByStepAndWorkerOrMachine(@Param("processStep") String processStep, @Param("workerId") String workerId, @Param("machineId") String machineId);

    @Select("select * from smb_op.dispatch_list where uuid = #{uuid}")
    OpDispatchOrder findOpDispatchOrderByUUID(@Param("uuid") String uuid);


    @Update("<script>"
        + "update smb_op.dispatch_list set status = #{opDispatchOrder.status}, "
        + "<if test='opDispatchOrder.actualFinishDt != null'> actual_finish_dt = #{opDispatchOrder.actualFinishDt}, </if>"
        + "finish_cnt = #{opDispatchOrder.finishCnt} "
        + "where uuid = #{uuid}"
        + "</script>")
    int updateDispatchStatusAndCountByUUID(@Param("uuid") String uuid, @Param("opDispatchOrder") OpDispatchOrder opDispatchOrder);

    @Update("update smb_op.dispatch_list set status = #{status} where uuid = #{uuid}")
    int updateDispatchStatusByUUID(@Param("uuid") String uuid, @Param("status") String status);

    @Update("update smb_op.dispatch_list set assign_worker = #{expectWorker}, assign_machine = #{expectMachine}, expect_start_dt = #{expectOnline}, expect_finish_dt = #{expectOffline} where uuid = #{uuid}")
    int updateDispatchExpectDetailByUUID(@Param("uuid") String uuid, @Param("expectWorker") String expectWorker, @Param("expectMachine") String expectMachine, @Param("expectOnline") String expectOnline, @Param("expectOffline") String expectOffline);

    @Select("select max(last_report_dt) as last_query_dt from smb_op.dispatch_list where process_step = #{processStep}")
    String findLastUpdateTimeByProcess(@Param("processStep") String processStep);

    @Select("select * from smb_op.dispatch_list where mfgorder_id = #{mfgorderId} and dispatch_id = #{dispatchId} and process_step = #{processStep} and material_id = #{materialId}")
    OpDispatchOrder findOnlyOpDispatchOrderByDispatchInfo(@Param("mfgorderId") String mfgorderId, @Param("dispatchId") String dispatchId, @Param("processStep") String processStep, @Param("materialId") String materialId);

}