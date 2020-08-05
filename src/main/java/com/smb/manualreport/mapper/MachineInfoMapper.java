package com.smb.manualreport.mapper;


import com.smb.manualreport.bean.MachineInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MachineInfoMapper {

    @Select("<script>"
            + "select * from smb_op.machine_info where 1 = 1 "
            + "<if test='processStep != null'> and machine_type = #{processStep} </if>"
            + "</script>")
    List<MachineInfo> findMachineByProcessStep(@Param("processStep") String processStep);
}
