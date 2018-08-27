package com.uestc.yihau.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ResultCodeTip {
    private static Properties properties = new Properties();
    static{
        InputStream inputStream = ResultCodeTip.class.getResourceAsStream("/code.properties");
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
        try {
            properties.load(bf);
            System.out.println("1234");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static Properties getProperties(){
//        InputStream inputStream = ResultCodeTip.class.getResourceAsStream("/code.properties");
//        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
//        Properties properties = new Properties();
//        try {
//            properties.load(bf);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return properties;
//    }

    public static String getTipContent(int code){
        Object object = properties.get(code + "");
        if (object == null){
            return "错误码 ：" + code;
        }
        return (String) object;
    }
}
