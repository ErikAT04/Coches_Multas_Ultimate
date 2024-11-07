package com.erikat.crudjavageneral.Util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.InputStream;
import java.net.URL;

public class R {
    public static URL getHibernateConfig(String path){
        return Thread.currentThread().getContextClassLoader().getResource("config/"+path);
    }
    public static URL getUIResource(String path){
        return Thread.currentThread().getContextClassLoader().getResource("ui/"+path);
    }
    public static InputStream getDBConfig(String path){
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("config/"+path);
    }
    public static String encrypt(String string){
        return DigestUtils.sha256Hex(string);
    }
}
