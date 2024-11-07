package com.erikat.crudjavageneral.Util;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import java.io.IOException;
import java.util.Properties;

//Utiles de MongoDB
public class MongoDBUtils {
    static MongoClient cliente;
    static MongoDatabase database;
    static {
        try {
            Properties dbConf = new Properties(); //Crea un objeto de tipo Properties
            dbConf.load(R.getDBConfig("MongoDB.properties")); //Lee el archivo de propiedades y procede a coger todos los datos que ve necesarios
            String host = dbConf.getProperty("host");
            String user = dbConf.getProperty("user");
            String pass = dbConf.getProperty("password");
            String port = dbConf.getProperty("port");
            String source = dbConf.getProperty("source");

            cliente = new MongoClient(new MongoClientURI("mongodb://"+user+":"+pass+"@"+host+":"+port+"/?authSource="+source)); //Se conecta a la base de datos en función de la url
            database = cliente.getDatabase("CochesMultas");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void closeDataBase(){
        cliente.close();
    }
    public static MongoDatabase getDatabase(){
        return database;
    }
}
