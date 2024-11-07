package com.erikat.crudjavageneral.DAO;

import com.erikat.crudjavageneral.Model.Coche;
import com.erikat.crudjavageneral.Util.HibernateUtils;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class CocheDAOHibernate implements CocheDAOInterface{
    Session session;
    public CocheDAOHibernate(){
        session = HibernateUtils.getSession();
    }
    @Override
    public boolean insertarCoche(Coche c) {
        boolean completadoCorrectamente = true;
        try{
            session.beginTransaction();
            session.save(c);
            session.getTransaction().commit();
        }catch (Exception e){
            completadoCorrectamente = false;
            System.out.println("Error de BD Hibernate");
            session.getTransaction().rollback();
        }
        session.clear();
        return completadoCorrectamente;
    }

    @Override
    public boolean borrarCoche(Coche c) {
        boolean completadoCorrectamente = true;
        try{
            session.beginTransaction();
            session.remove(c);
            session.getTransaction().commit();
        }catch (Exception e){
            completadoCorrectamente = false;
            System.out.println("Error de BD Hibernate");
            session.getTransaction().rollback();
        }
        session.clear();
        return completadoCorrectamente;
    }

    @Override
    public boolean actualizarCoche(Coche c) {
        boolean completadoCorrectamente = true;
        try{
            session.beginTransaction();
            session.update(c);
            session.getTransaction().commit();
        }catch (Exception e){
            completadoCorrectamente = false;
            System.out.println("Error de BD Hibernate");
            session.getTransaction().rollback();
        }
        session.clear();
        return completadoCorrectamente;
    }

    @Override
    public Coche buscarCoche(String matricula) {
        Coche c = null;
        try{
            c = session.createQuery(" from Coche where matricula = '"+matricula+"'", Coche.class).uniqueResult();
        }catch (Exception e){
            session.clear();
            System.out.println("Error de BD Hibernate");
        }
        return c;
    }

    @Override
    public List<Coche> listarCoches() {
        List<Coche> coches = new ArrayList<>();
        try{
            coches = session.createQuery(" from Coche", Coche.class).getResultList();
        }catch (Exception e){
            session.clear();
            System.out.println("Error de BD Hibernate");
        }
        return coches;
    }
}
