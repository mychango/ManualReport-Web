package com.smb.manualreport.mapper;

import com.smb.manualreport.bean.OpDispatchOrder;
import com.smb.manualreport.bean.SrcDispatchOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DispatchListMapper {

    @Insert("insert into smb_op.dispatch_list " +
            "(dispatch_id, material_id, total_cnt, finish_cnt, assign_worker, assign_machine, expect_finish_dt, last_report_dt, actual_finish_dt, process_step, status, expect_start_dt, mfgorder_id)" +
            "values " +
            "(#{srcDispatchOrder.dispatchId}, #{srcDispatchOrder.materialId}, #{srcDispatchOrder.expectAmount}, null, #{srcDispatchOrder.expectWorker}, #{srcDispatchOrder.expectMachine}, #{srcDispatchOrder.expectOffline}, null, null, #{srcDispatchOrder.processStep}, 'Wait', #{srcDispatchOrder.expectOnline}, #{srcDispatchOrder.mfgorderId})")
    int insertDispatchList(@Param("srcDispatchOrder") SrcDispatchOrder srcDispatchOrder);

    @Select("<script>"
            + "select * from smb_op.dispatch_list where 1 = 1 "
            + "<if test='workerId != null'> and assign_worker = #{workerId} </if>"
            + "<if test='machineId != null'> and assign_machine = #{machineId} </if>"
            + "and process_step = #{processStep} "
            + "and status not in ('Finish', 'In Process') "
            + "</script>")
    List<OpDispatchOrder> findOpDispatchOrderByStepAndWorkerOrMachine(@Param("processStep") String processStep, @Param("workerId") String workerId, @Param("machineId") String machineId);
}