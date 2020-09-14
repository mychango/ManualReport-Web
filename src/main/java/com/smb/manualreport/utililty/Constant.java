package com.smb.manualreport.utililty;

import java.util.HashMap;

public class Constant {
    public static final String[] JUJIANG_PROCESS_STEP = {"MA", "MB", "MC", "MD"};
    public static final HashMap<String, String> JUJIANG_PROCESS_MAP;
    static{
        JUJIANG_PROCESS_MAP = new HashMap<String, String>();
        JUJIANG_PROCESS_MAP.put("PRGM", "MA");
        JUJIANG_PROCESS_MAP.put("LASR", "MB");
        JUJIANG_PROCESS_MAP.put("BEND", "MC");
        JUJIANG_PROCESS_MAP.put("WELD", "MD");
        JUJIANG_PROCESS_MAP.put("MA", "PRGM");
        JUJIANG_PROCESS_MAP.put("MB", "LASR");
        JUJIANG_PROCESS_MAP.put("MC", "BEND");
        JUJIANG_PROCESS_MAP.put("MD", "WELD");
    }
    public static final HashMap<String, String> PROCESS_MAP_LANGUAGE_MAP;
    static{
        PROCESS_MAP_LANGUAGE_MAP = new HashMap<String, String>();
        PROCESS_MAP_LANGUAGE_MAP.put("PRGM", "排版");
        PROCESS_MAP_LANGUAGE_MAP.put("LASR", "雷射");
        PROCESS_MAP_LANGUAGE_MAP.put("BEND", "折床");
        PROCESS_MAP_LANGUAGE_MAP.put("WELD", "焊接");
        PROCESS_MAP_LANGUAGE_MAP.put("SHER", "剪床");
        PROCESS_MAP_LANGUAGE_MAP.put("VCUT", "刨溝");
        PROCESS_MAP_LANGUAGE_MAP.put("PNCH", "沖孔");
        PROCESS_MAP_LANGUAGE_MAP.put("排版", "PRGM");
        PROCESS_MAP_LANGUAGE_MAP.put("雷射", "LASR");
        PROCESS_MAP_LANGUAGE_MAP.put("折床", "BEND");
        PROCESS_MAP_LANGUAGE_MAP.put("焊接", "WELD");
        PROCESS_MAP_LANGUAGE_MAP.put("剪床", "SHER");
        PROCESS_MAP_LANGUAGE_MAP.put("刨溝", "VCUT");
        PROCESS_MAP_LANGUAGE_MAP.put("沖孔", "PNCH");
    }
    public static final HashMap<String, String> DISPATCH_STATUS_MAP;
    static{
        DISPATCH_STATUS_MAP = new HashMap<String, String>();
        DISPATCH_STATUS_MAP.put("Wait", "未開工");
        DISPATCH_STATUS_MAP.put("Finish", "已完工");
        DISPATCH_STATUS_MAP.put("In Process", "處理中");
        DISPATCH_STATUS_MAP.put("Exception", "異常/缺料");
    }

    public static final String[] COMMON_PROCESS_STEP = {"PRGM", "LASR", "BEND", "VCUT", "PNCH", "WELD", "SHER", "TAPP", "DRAW"};
    public static final String LANGUAGE_CHINESE = "zh_TW";
    public static final String LANGUAGE_ENGLISH = "en_US";
    public static final String LANGUAGE_VIETNAMESE = "vi_VN";
    public static final String PO_STATUS_ONGOING = "未結案";
}
