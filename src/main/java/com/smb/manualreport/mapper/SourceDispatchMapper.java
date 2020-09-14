package com.smb.manualreport.mapper;

import com.smb.manualreport.bean.ProductOrder;
import com.smb.manualreport.bean.SrcDispatchOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SourceDispatchMapper {

    @Select("select p.mo_id as mfgorder_id, p.dp_id as dispatch_id, q.part_id as material_id, q.expect_amount, p.expect_worker, p.expect_machine, p.expect_online, p.expect_offline, p.mp_id as process_step from smbsource.dispatch_order p left join smbsource.manufacture_part q on q.dp_id = p.dp_id and q.mo_id = p.mo_id and q.mp_id = p.mp_id where p.expect_online is not null and p.expect_offline is not null and p.mp_id = #{processStep} and p.last_update_date > #{lastQueryTime}")
    List<SrcDispatchOrder> findDispatchOrderByProcessAndTime(@Param("processStep") String processStep, @Param("lastQueryTime") String lastQueryTime);

    @Select("select p.mo_id as mfgorder_id, p.dp_id as dispatch_id, q.part_id as material_id, q.expect_amount, p.expect_worker, p.expect_machine, p.expect_online, p.expect_offline, p.mp_id as process_step from smbsource.dispatch_order p left join smbsource.manufacture_part q on q.dp_id = p.dp_id and q.mo_id = p.mo_id and q.mp_id = p.mp_id where p.mp_id = #{processStep} and p.expect_worker = #{workerId}")
    List<SrcDispatchOrder> findDispatchOrderByProcessAndWorker(@Param("processStep") String processStep, @Param("workerId") String workerId);

    @Select("select p.mo_id as mfgorder_id, p.dp_id as dispatch_id, q.part_id as material_id, q.expect_amount, p.expect_worker, p.expect_machine, p.expect_online, p.expect_offline, p.mp_id as process_step from smbsource.dispatch_order p left join smbsource.manufacture_part q on q.dp_id = p.dp_id and q.mo_id = p.mo_id and q.mp_id = p.mp_id where p.mp_id = #{processStep} and p.expect_machine = #{machineId}")
    List<SrcDispatchOrder> findDispatchOrderByProcessAndMachine(@Param("processStep") String processStep, @Param("machineId") String machineId);

    @Select("select po_id, customer_name, customer_code, raw_state from smbsource.product_order where sync_date > #{lastQueryTime}")
    List<ProductOrder> findProductOrderByTime(@Param("lastQueryTime") String lastQueryTime);

    @Select("select p.mo_id as mfgorder_id, p.dp_id as dispatch_id, q.part_id as material_id, q.expect_amount, p.expect_worker, p.expect_machine, p.expect_online, p.expect_offline, p.mp_id as process_step from smbsource.manufacture_part q left join smbsource.dispatch_order p on q.dp_id = p.dp_id and q.mo_id = p.mo_id and q.mp_id = p.mp_id where p.expect_online is not null and p.expect_offline is not null and p.mp_id = #{processStep} and q.po_id = #{orderId} and p.last_update_date > #{lastQueryTime}")
    List<SrcDispatchOrder> findDispatchOrderByProcessAndOrderAndTime(@Param("processStep") String processStep, @Param("orderId") String orderId, @Param("lastQueryTime") String lastQueryTime);

    @Select("select mo_id from smbsource.manufacture_part where po_id = #{orderId} group by po_id, mo_id")
    List<String> findMfgOrderIdByProductOrder(@Param("orderId") String orderId);

}
