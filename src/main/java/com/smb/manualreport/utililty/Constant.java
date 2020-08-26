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
    public static final String[] COMMON_PROCESS_STEP = {"PRGM", "LASR", "BEND", "VCUT", "PNCH", "WELD", "SHER", "TAPP", "DRAW"};
    public static final String LANGUAGE_CHINESE = "zh_tw";
    public static final String LANGUAGE_ENGLISH = "en_us";
}
