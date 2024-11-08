package com.erikat.crudjavageneral.DAO;

import com.erikat.crudjavageneral.Model.Coche;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


//DAO de coches con Mongo
public class CocheDAOMongo implements CocheDAOInterface{
    MongoDatabase db; //Base de datos de MongoDB
    Gson gson; //Convertidor de JSON a Gson
    MongoCollection<Document> collection;
    public CocheDAOMongo(){
        db = MongoDBUtils.getDatabase();
        gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
        //gson = new Gsoon(), //Si no usas fechas
        collection = db.getCollection("Coche");
    }

    /*--------------------------------------------------------------------------------------*/

    @Override
    public boolean insertarCoche(Coche c) {
        boolean completadoCorrectamente;
        try {
            long tamanoActual = collection.countDocuments(); //Guarda el tamaño
            c.setId((int) (tamanoActual+1)); //Hace un id nuevo
            Document coche = Document.parse(gson.toJson(c)); //Parsea con GSON el coche
            collection.insertOne(coche); //Inserta el documento
            completadoCorrectamente = (tamanoActual!=collection.countDocuments()); //Devuelve true si se ha introducido algo
        }catch (Exception e){
            System.out.println("Error de BD Mongo: " + e.getMessage());
            completadoCorrectamente = false;
        }
        return completadoCorrectamente;
    }

    /*--------------------------------------------------------------------------------------*/

    @Override
    public boolean borrarCoche(Coche c) {
        boolean completadoCorrectamente;
        try {
            long tamanoActual = collection.countDocuments();
            collection.deleteOne(Filters.eq("matricula", c.getMatricula())); //Borra el primero que coincida con "matricula"
            MultaDAOMongo multasDAO = new MultaDAOMongo();
            List<Multa> listaMultas = multasDAO.listarMultas(c.getMatricula()); //Borra todas las multas que tenga ese coche
            for (Multa m : listaMultas){
                multasDAO.borrarMulta(m);
            }
            completadoCorrectamente = (tamanoActual!=collection.countDocuments()); //Devuelve si se han editado los coches
        }catch (Exception e){
            System.out.println("Error de BD Mongo");
            completadoCorrectamente = false;
        }
        return completadoCorrectamente;
    }

    /*--------------------------------------------------------------------------------------*/

    @Override
    public boolean actualizarCoche(Coche c) {
        boolean completadoCorrectamente;
        try{
            UpdateResult resultado = collection.updateOne(new Document("matricula", c.getMatricula()), new Document("$set", new Document("marca", c.getMarca()).append("modelo", c.getModelo()).append("tipo", c.getTipo()).append("codSecreto", c.getCodSecreto())));
            //Para las relaciones 1:N, como las pilla muy mal el programa, lo mejor es hacerlo a mano
            completadoCorrectamente = resultado.wasAcknowledged(); //Devuelve si se ha hecho algo con el update
        } catch (Exception e){
            System.out.println("Error de BD Mongo");
            completadoCorrectamente = false;
        }
        return completadoCorrectamente;
    }

    /*--------------------------------------------------------------------------------------*/

    @Override
    public Coche buscarCoche(String matricula) {
        Coche c = null;
        try {
            Document document = collection.find(Filters.eq("matricula", matricula)).first();
            //Coge el primer resultado que pille con esa matricula
            if (document!=null){
                c = gson.fromJson(document.toJson(), Coche.class);
                //Pasa de JSON a Coche
            }
        }catch (Exception e){
            System.out.println("Error de BD Mongo");
        }
        return c;
    }

    /*--------------------------------------------------------------------------------------*/

    @Override
    public List<Coche> listarCoches() {
        List<Coche> coches = new ArrayList<>();
        try(MongoCursor<Document> results = collection.find().iterator()){ //Coge todos los coches
            while (results.hasNext()){ //Hace en cada uno lo que hacia en BuscarCoche()
                Coche coche = gson.fromJson(results.next().toJson(), Coche.class);
                coches.add(coche);
            }
        }catch (Exception e){
            System.out.println("Error de BD Mongo");
        }
        return coches;
    }
}
