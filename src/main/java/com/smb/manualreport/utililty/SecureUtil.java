package com.smb.manualreport.utililty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class SecureUtil {

    private static Logger logger = LoggerFactory.getLogger(SecureUtil.class);

    /**
     * Method for get System CPU Serial Number
     */
    public static String getSystemCPU_SerialNumber(){
        try{
            String OSName =  System.getProperty("os.name");
//            logger.info(OSName);
            if(OSName.contains("Windows")){
                return getWindowsCPU_SerialNumber();
            }
            else{
                return getLinuxCPU_serialNumber();
            }
        }
        catch(Exception E){
            System.err.println("System CPU Exp : "+E.getMessage());
            return null;
        }
    }


    /**
     * Method for get System Motherboard Serial Number
     */
    public static String getSystemMotherBoard_SerialNumber(){
        try{
            String OSName =  System.getProperty("os.name");
            logger.info(OSName);
            if(OSName.contains("Windows")){
                return getWindowsMotherboard_SerialNumber();
            }
            else{
                return getLinuxMotherBoard_serialNumber();
            }
        }
        catch(Exception E){
            System.err.println("System MotherBoard Exp : "+E.getMessage());
            return null;
        }
    }

    /**
     * Method for get Windows Machine MotherBoard Serial Number
     * @return
     */
    private static String getWindowsMotherboard_SerialNumber() {
        String result = "";
        try {
            File file = File.createTempFile("realhowto",".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);

            String vbs =
                    "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                            + "Set colItems = objWMIService.ExecQuery _ \n"
                            + "   (\"Select * from Win32_BaseBoard\") \n"
                            + "For Each objItem in colItems \n"
                            + "    Wscript.Echo objItem.SerialNumber \n"
                            + "    exit for  ' do the first cpu only! \n"
                            + "Next \n";

            fw.write(vbs);
            fw.close();

            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
            file.delete();
        }
        catch(Exception E){
            System.err.println("Windows MotherBoard Exp : "+E.getMessage());
        }
        return result.trim();
    }

    /**
     * Method for get Windows Machine CPU Serial Number
     * @return
     */
    public static String getWindowsCPU_SerialNumber() {
        String result = "";
        try {
            File file = File.createTempFile("tmp", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);

            String vbs =
                    "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                            + "Set colItems = objWMIService.ExecQuery _ \n"
                            + "   (\"Select * from Win32_Processor\") \n"
                            + "For Each objItem in colItems \n"
                            + "    Wscript.Echo objItem.ProcessorId \n"
                            + "    exit for  ' do the first cpu only! \n"
                            + "Next \n";

            fw.write(vbs);
            fw.close();

//            String path = file.getPath().replace("%20", " ");
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
            file.delete();
        } catch (Exception e) {
            System.err.println("Windows CPU Exp : "+e.getMessage());
        }
        return result.trim();
    }

    /**
     * Method for get Linux Machine MotherBoard Serial Number
     * @return
     */
    private static String getLinuxMotherBoard_serialNumber() {
        return getLinuxSerialNumber("dmidecode |grep 'Serial Number'", "Serial Number",":");
    }

    /**
     * Method for get Linux Machine MotherBoard Serial Number
     * @return
     */
    private static String getLinuxCPU_serialNumber() {
        return getLinuxSerialNumber("dmidecode -t processor | grep 'ID'", "ID",":");
    }

    public static String executeLinuxCmd(String cmd)  {
        try {
            System.out.println("got cmd job : " + cmd);
            Runtime run = Runtime.getRuntime();
            Process process;
            process = run.exec(cmd);
            InputStream in = process.getInputStream();
            BufferedReader bs = new BufferedReader(new InputStreamReader(in));
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[8192];
            for (int n; (n = in.read(b)) != -1;) {
                out.append(new String(b, 0, n));
            }

            in.close();
            process.destroy();
            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getLinuxSerialNumber(String cmd ,String record,String symbol) {
        String execResult = executeLinuxCmd(cmd);
        String[] infos = execResult.split("\n");

        for(String info : infos) {
            info = info.trim();
            if(info.indexOf(record) != -1) {
                info.replace(" ", "");
                String[] sn = info.split(symbol);
                return sn[1];
            }
        }

        return null;
    }
}
