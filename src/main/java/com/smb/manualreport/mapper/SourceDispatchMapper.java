package com.smb.manualreport.mapper;

import com.smb.manualreport.bean.SrcDispatchOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SourceDispatchMapper {

    @Select("select p.mo_id as mfgorder_id, p.dp_id as dispatch_id, q.part_id as material_id, q.expect_amount, p.expect_worker, p.expect_machine, p.expect_online, p.expect_offline, p.mp_id as process_step from smbsource.dispatch_order p left join smbsource.manufacture_part q on q.dp_id = p.dp_id and q.mo_id = p.mo_id and q.mp_id = p.mp_id where p.mp_id = #{processStep} and p.expect_offline > #{lastQueryTime}")
    List<SrcDispatchOrder> findDispatchOrderByProcessAndTime(@Param("processStep") String processStep, @Param("lastQueryTime") String lastQueryTime);

    @Select("select p.mo_id as mfgorder_id, p.dp_id as dispatch_id, q.part_id as material_id, q.expect_amount, p.expect_worker, p.expect_machine, p.expect_online, p.expect_offline, p.mp_id as process_step from smbsource.dispatch_order p left join smbsource.manufacture_part q on q.dp_id = p.dp_id and q.mo_id = p.mo_id and q.mp_id = p.mp_id where p.mp_id = #{processStep} and p.expect_worker = #{workerId}")
    List<SrcDispatchOrder> findDispatchOrderByProcessAndWorker(@Param("processStep") String processStep, @Param("workerId") String workerId);

    @Select("select p.mo_id as mfgorder_id, p.dp_id as dispatch_id, q.part_id as material_id, q.expect_amount, p.expect_worker, p.expect_machine, p.expect_online, p.expect_offline, p.mp_id as process_step from smbsource.dispatch_order p left join smbsource.manufacture_part q on q.dp_id = p.dp_id and q.mo_id = p.mo_id and q.mp_id = p.mp_id where p.mp_id = #{processStep} and p.expect_machine = #{machineId}")
    List<SrcDispatchOrder> findDispatchOrderByProcessAndMachine(@Param("processStep") String processStep, @Param("machineId") String machineId);

}
