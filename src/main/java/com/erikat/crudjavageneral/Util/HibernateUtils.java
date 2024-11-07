package com.erikat.crudjavageneral.Util;


import com.erikat.crudjavageneral.Model.Coche;
import com.erikat.crudjavageneral.Model.Multa;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


//Clase que gestiona lo relacionado a Hibernate:
public class HibernateUtils {
    static Session session;
    static SessionFactory factory;
    static {
        Configuration configuration = new Configuration();
        configuration.configure(R.getHibernateConfig("hibernate.cfg.xml"));
        configuration.addAnnotatedClass(Multa.class);
        configuration.addAnnotatedClass(Coche.class);

        factory = configuration.buildSessionFactory();

        session = factory.openSession();
    }

    public static Session getSession(){ //Devuelve la sesión
        return session;
    }
}
