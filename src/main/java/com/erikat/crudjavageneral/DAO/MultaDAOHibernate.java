package com.erikat.crudjavageneral.DAO;

import com.erikat.crudjavageneral.Model.Coche;
import com.erikat.crudjavageneral.Model.Multa;
import com.erikat.crudjavageneral.Util.HibernateUtils;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

//DAO de multas con Hibernate: Funca igual que las DAO de coche
public class MultaDAOHibernate implements MultaDAOInterface{
    Session session;
    public MultaDAOHibernate(){
        session = HibernateUtils.getSession();
    }
    @Override
    public boolean insertarMulta(Multa m) {
        boolean completadoCorrectamente = true;
        try{
            session.beginTransaction();
            session.save(m);
            session.flush();
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
    public boolean borrarMulta(Multa m) {
        boolean completadoCorrectamente = true;
        try{
            session.beginTransaction();
            session.remove(m);
            session.flush();
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
    public boolean actualizarMulta(Multa m) {
        boolean completadoCorrectamente = true;
        try{
            session.beginTransaction();
            session.update(m);
            session.flush();
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
    public Multa buscarMulta(int id) {
        Multa m = null;
        try{
            session.beginTransaction();
            m = session.get(Multa.class, id);
            session.flush();
            session.getTransaction().commit();
        }catch (Exception e){
            System.out.println("Error de BD Hibernate");
            session.getTransaction().rollback();
        }
        session.clear();
        return m;
    }

    @Override
    public List<Multa> listarMultas(String matricula) {
        List<Multa> multas = new ArrayList<>();
        try{
            multas = session.createQuery(" from Multa where matricula = '"+matricula+"'", Multa.class).getResultList();
        }catch (Exception e){
            session.clear();
            System.out.println("Error de BD Hibernate" + e.getMessage());
        }
        return multas;
    }
}
