package com.smb.manualreport.bean;

public class ElementLog {
    private String machine_code;
    private String worker_code;
    private String element_code;
    private String step_code;
    private String start_datetime;
    private String finish_datetime;
    private Integer finish_number;
    private Integer faile_number;
    private String dispatch_code;
    private String manufacture_code;
    private String nest_program_id;

    public String getMachine_code() {
        return machine_code;
    }

    public void setMachine_code(String machine_code) {
        this.machine_code = machine_code;
    }

    public String getWorker_code() {
        return worker_code;
    }

    public void setWorker_code(String worker_code) {
        this.worker_code = worker_code;
    }

    public String getElement_code() {
        return element_code;
    }

    public void setElement_code(String element_code) {
        this.element_code = element_code;
    }

    public String getStep_code() {
        return step_code;
    }

    public void setStep_code(String step_code) {
        this.step_code = step_code;
    }

    public String getStart_datetime() {
        return start_datetime;
    }

    public void setStart_datetime(String start_datetime) {
        this.start_datetime = start_datetime;
    }

    public String getFinish_datetime() {
        return finish_datetime;
    }

    public void setFinish_datetime(String finish_datetime) {
        this.finish_datetime = finish_datetime;
    }

    public Integer getFinish_number() {
        return finish_number;
    }

    public void setFinish_number(Integer finish_number) {
        this.finish_number = finish_number;
    }

    public Integer getFaile_number() {
        return faile_number;
    }

    public void setFaile_number(Integer faile_number) {
        this.faile_number = faile_number;
    }

    public String getDispatch_code() {
        return dispatch_code;
    }

    public void setDispatch_code(String dispatch_code) {
        this.dispatch_code = dispatch_code;
    }

    public String getManufacture_code() {
        return manufacture_code;
    }

    public void setManufacture_code(String manufacture_code) {
        this.manufacture_code = manufacture_code;
    }

    public String getNest_program_id() {
        return nest_program_id;
    }

    public void setNest_program_id(String nest_program_id) {
        this.nest_program_id = nest_program_id;
    }
}
