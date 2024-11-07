package com.erikat.crudjavageneral.DAO;

import com.erikat.crudjavageneral.Model.Multa;
import com.erikat.crudjavageneral.Util.LocalDateAdapter;
import com.erikat.crudjavageneral.Util.MongoDBUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.mapping.Collection;

import javax.print.Doc;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MultaDAOMongo implements MultaDAOInterface{
    Gson gson;
    MongoCollection<Document> collection;
    MongoDatabase db;
    public MultaDAOMongo(){
        db = MongoDBUtils.getDatabase();
        gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        //gson = new Gson() si no utilizas LocalDate
        collection = db.getCollection("Multa");
    }
    @Override
    public boolean insertarMulta(Multa m) {
        boolean completadoCorrectamente;
        try {
            long tamanoActual = collection.countDocuments();
            m.setId((int) (tamanoActual+1));
            Document multa = new Document("id", m.getId()).append("precio", m.getPrecio()).append("fecha", m.getFecha().toString()).append("matricula", m.getCoche().getMatricula());
            collection.insertOne(multa);
            completadoCorrectamente = (tamanoActual!=collection.countDocuments());
        }catch (Exception e){
            System.out.println("Error de BD Mongo");
            completadoCorrectamente = false;
        }
        return completadoCorrectamente;
    }

    @Override
    public boolean borrarMulta(Multa m) {
        boolean completadoCorrectamente;
        try {
            long tamanoActual = collection.countDocuments();
            collection.deleteOne(Filters.eq("id", m.getId()));
            completadoCorrectamente = (tamanoActual!=collection.countDocuments());
        }catch (Exception e){
            System.out.println("Error de BD Mongo");
            completadoCorrectamente = false;
        }
        return completadoCorrectamente;
    }

    @Override
    public boolean actualizarMulta(Multa m) {
        boolean completadoCorrectamente = true;
        try{
            UpdateResult resultado = collection.updateOne(new Document("id", m.getId()), new Document("$set", new Document("precio", m.getPrecio()).append("fecha", m.getFecha().toString()).append("matricula", m.getCoche().getMatricula())));
            completadoCorrectamente = resultado.wasAcknowledged();
        }catch (Exception e){
            System.out.println("Error de BD Mongo");
            completadoCorrectamente = false;
        }
        return completadoCorrectamente;
    }

    @Override
    public Multa buscarMulta(int id) {
        Multa multa = null;
        try{
            Document multaDoc = collection.find(Filters.eq("id", id)).first();
            if (multaDoc != null) {
                multa = new Multa(
                        id,
                        Double.parseDouble(multaDoc.get("precio").toString()),
                        LocalDate.parse(multaDoc.getString("fecha")),
                        new CocheDAOMongo().buscarCoche(multaDoc.getString("matricula")));
            }
        }catch (Exception e){
            System.out.println("Error de BD Mongo: " + e.getMessage());
        }
        return multa;
    }

    @Override
    public List<Multa> listarMultas(String matricula) {
        List<Multa> listaMultas = new ArrayList<>();
        try(MongoCursor<Document> cursor = collection.find().iterator()){
            while (cursor.hasNext()){
                Document multaDoc = cursor.next();
                if (multaDoc.getString("matricula").equals(matricula)) {
                    Multa multa = new Multa(
                            multaDoc.getInteger("id"),
                            Double.parseDouble(multaDoc.get("precio").toString()),
                            LocalDate.parse(multaDoc.getString("fecha")),
                            new CocheDAOMongo().buscarCoche(multaDoc.getString("matricula")));
                    listaMultas.add(multa);
                }
            }
        //}catch (Exception e){
            //System.out.println("Error de BD Mongo" + e.getMessage());
            //
        }
        return listaMultas;
    }
}
