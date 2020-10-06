package com.smb.manualreport.mapper;

import com.smb.manualreport.bean.OpDispatchOrder;
import com.smb.manualreport.bean.SrcDispatchOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface DispatchListMapper {

    @Insert("insert into smb_op.dispatch_list " +
        "(dispatch_id, material_id, total_cnt, finish_cnt, assign_worker, assign_machine, expect_finish_dt, actual_finish_dt, process_step, status, expect_start_dt, mfgorder_id)" +
        "values " +
        "(#{srcDispatchOrder.dispatchId}, #{srcDispatchOrder.materialId}, #{srcDispatchOrder.expectAmount}, null, #{srcDispatchOrder.expectWorker}, #{srcDispatchOrder.expectMachine}, #{srcDispatchOrder.expectOffline}, null, #{srcDispatchOrder.processStep}, #{status}, #{srcDispatchOrder.expectOnline}, #{srcDispatchOrder.mfgorderId})")
    int insertDispatchList(@Param("srcDispatchOrder") SrcDispatchOrder srcDispatchOrder, @Param("status") String status);

    @Select("<script>"
            + "select * from smb_op.dispatch_list where 1 = 1 "
            + "<if test='workerId != null'> and (assign_worker = #{workerId} or assign_worker = #{nickName}) </if>"
            + "<if test='machineId != null'> and assign_machine = #{machineId} </if>"
            + "and process_step = #{processStep} "
            + "and status not in ('Finish', 'In Process', 'Delete') "
            + "</script>")
    List<OpDispatchOrder> findOpDispatchOrderByStepAndWorkerOrMachine(@Param("processStep") String processStep, @Param("workerId") String workerId, @Param("nickName") String userName, @Param("machineId") String machineId);

    @Select("<script>"
            + "select * from smb_op.dispatch_list where 1 = 1 "
            + "<if test='workerId != null'> and assign_worker = #{workerId} </if>"
            + "<if test='machineId != null'> and assign_machine = #{machineId} </if>"
            + "and process_step = #{processStep} "
            + "and status not in ('Finish', 'Delete') "
            + "</script>")
    List<OpDispatchOrder> findOpDispatchOrderByStepAndWorkerOrMachineWithProcessing(@Param("processStep") String processStep, @Param("workerId") String workerId, @Param("machineId") String machineId);

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

//    @Update("update smb_op.dispatch_list set assign_worker = #{expectWorker}, assign_machine = #{expectMachine}, expect_start_dt = #{expectOnline}, expect_finish_dt = #{expectOffline}, sync_dt = current_timestamp() where uuid = #{uuid}")
    //20200915 新增update判斷條件，修正dispatch已被開工後修改assignWorker的Exception
    @Update("update smb_op.dispatch_list set status = if(assign_worker <> #{expectWorker} and status <> 'Finish', 'Wait', status), assign_worker = #{expectWorker}, assign_machine = #{expectMachine}, expect_start_dt = #{expectOnline}, expect_finish_dt = #{expectOffline}, sync_dt = current_timestamp() where uuid = #{uuid}")
    int updateDispatchExpectDetailByUUID(@Param("uuid") String uuid, @Param("expectWorker") String expectWorker, @Param("expectMachine") String expectMachine, @Param("expectOnline") String expectOnline, @Param("expectOffline") String expectOffline);

    @Select("select max(sync_dt) as last_query_dt from smb_op.dispatch_list where process_step = #{processStep}")
    String findLastUpdateTimeByProcess(@Param("processStep") String processStep);

    @Select("select * from smb_op.dispatch_list where mfgorder_id = #{mfgorderId} and dispatch_id = #{dispatchId} and process_step = #{processStep} and material_id = #{materialId}")
    OpDispatchOrder findOnlyOpDispatchOrderByDispatchInfo(@Param("mfgorderId") String mfgorderId, @Param("dispatchId") String dispatchId, @Param("processStep") String processStep, @Param("materialId") String materialId);

    @Update("update smb_op.dispatch_list set status = 'Delete', sync_dt = current_timestamp() where mfgorder_id = #{mfgorderId}")
    int softDeleteDispatchOrderByMfgOrder(@Param("mfgorderId") String mfgorderId);

    @Select("select assign_worker from smb_op.dispatch_list where uuid = #{uuid}")
    String findAssignWorkerByUUID(@Param("uuid") String uuid);

    @Delete("delete from smb_op.dispatch_list where mfgorder_id = #{mfgorderId} and process_step = #{processStep}")
    int deleteDispatchOrderByMfgOrderAndProcess(@Param("mfgorderId") String mfgorderId, @Param("processStep") String processStep);

}