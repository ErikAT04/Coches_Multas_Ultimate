package com.erikat.crudjavageneral.DAO;

import com.erikat.crudjavageneral.Model.Coche;
import com.erikat.crudjavageneral.Util.HibernateUtils;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;


//DAO de coches con Hibernate
public class CocheDAOHibernate implements CocheDAOInterface{
    Session session; //La sesión que se va a usar
    public CocheDAOHibernate(){
        session = HibernateUtils.getSession(); //Al iniciarse el objeto, se le da la sesión
    }

    /*--------------------------------------------------------------------------------------*/

    @Override
    public boolean insertarCoche(Coche c) {
        boolean completadoCorrectamente = true;
        try{
            session.beginTransaction();
            session.save(c); //Inserta
            session.getTransaction().commit();
        }catch (Exception e){
            completadoCorrectamente = false; //Si no inserta, false
            System.out.println("Error de BD Hibernate");
            session.getTransaction().rollback(); //Rollback
        }
        session.clear();
        return completadoCorrectamente;
    }

    /*--------------------------------------------------------------------------------------*/

    @Override
    public boolean borrarCoche(Coche c) {
        boolean completadoCorrectamente = true;
        try{
            session.beginTransaction();
            session.remove(c); //Quita
            session.getTransaction().commit();
        }catch (Exception e){
            completadoCorrectamente = false; //Si no quita, false
            System.out.println("Error de BD Hibernate");
            session.getTransaction().rollback(); //Rollback y cierra
        }
        session.clear();
        return completadoCorrectamente;
    }

    /*--------------------------------------------------------------------------------------*/

    @Override
    public boolean actualizarCoche(Coche c) {
        boolean completadoCorrectamente = true;
        try{
            session.beginTransaction();
            session.update(c); //Actualiza
            session.getTransaction().commit();
        }catch (Exception e){
            completadoCorrectamente = false; //Si no actualiza, false
            System.out.println("Error de BD Hibernate");
            session.getTransaction().rollback(); //Cierra transacción
        }
        session.clear();
        return completadoCorrectamente;
    }

    /*--------------------------------------------------------------------------------------*/

    @Override
    public Coche buscarCoche(String matricula) {
        Coche c = null;
        try{
            c = session.createQuery(" from Coche where matricula = '"+matricula+"'", Coche.class).uniqueResult();
            //Hace una query y coge el único resultado de tipo Coche
        }catch (Exception e){
            session.clear(); //Limpia la sesión
            System.out.println("Error de BD Hibernate");
        }
        return c;
    }

    /*--------------------------------------------------------------------------------------*/

    @Override
    public List<Coche> listarCoches() {
        List<Coche> coches = new ArrayList<>();
        try{
            coches = session.createQuery(" from Coche", Coche.class).getResultList(); //Coge la query entera
        }catch (Exception e){
            session.clear(); //Limpia la sesión
            System.out.println("Error de BD Hibernate");
        }
        return coches;
    }
    /*--------------------------------------------------------------------------------------*/

}
