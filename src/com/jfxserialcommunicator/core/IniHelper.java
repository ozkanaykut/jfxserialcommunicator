package com.jfxserialcommunicator.core;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;


public class IniHelper{

    private static final String path = System.getProperty("user.dir");

    //private static final Wini ini = new Wini();

    public static Wini loadIni(String filePath) throws IOException {
        /*Wini ini = new Wini();
        ini.clear();
        ini.load(new File(path + "/" + filePath));*/
        Wini ini = new Wini(new File(path + "/" + filePath));
        return ini;
    }

    // ini file save method
    public static void saveIni(Wini iniFile){
        try {
            iniFile.store();
        } catch (InvalidPropertiesFormatException e) {
            System.out.println("Invalid file format.");
        } catch (IOException e) {
            System.out.println("Problem reading file.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * langName = tr_TR or en_EN or en_US v.b. ....
     *
     * filename and path is auto merged
     * */
    public static Wini loadLangIni(String langName) throws IOException {
        Wini ini = new Wini();
        ini.clear();
        ini.load(new File(path + "/config/lang/lang_" + langName+".ini"));
        return ini;
    }

    public static String getPath(){
        return path;
    }

}
