package com.erikat.crudjavageneral.DAO;

import com.erikat.crudjavageneral.Model.Coche;
import com.erikat.crudjavageneral.Model.Multa;
import com.erikat.crudjavageneral.Util.MySQLUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CocheDAOMySQL implements CocheDAOInterface{
    public Connection con;
    public CocheDAOMySQL(){
        con = MySQLUtils.getConnection();
    }
    @Override
    public boolean insertarCoche(Coche c) {
        boolean cambiadoCorrectamente = false;
        try {
            String sql = "INSERT INTO COCHES(matricula, marca, modelo, tipo, codSecreto) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, c.getMatricula());
            ps.setString(2, c.getMarca());
            ps.setString(3, c.getModelo());
            ps.setString(4, c.getTipo());
            ps.setString(5, c.getCodSecreto());

            int filasInsertadas = ps.executeUpdate();
            cambiadoCorrectamente = filasInsertadas==1;
        }catch (Exception e){
            System.out.println("Error de BD MySQL");
        }
        return cambiadoCorrectamente;
    }

    @Override
    public boolean borrarCoche(Coche c) {
        boolean cambiadoCorrectamente = false;
        try {
            String sql = "DELETE FROM COCHES WHERE MATRICULA = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, c.getMatricula());
            int filasActualizadas = ps.executeUpdate();
            cambiadoCorrectamente = filasActualizadas==1;
            if (cambiadoCorrectamente){
                MultaDAOMySQL multaDAO = new MultaDAOMySQL();
                List<Multa> multas = multaDAO.listarMultas(c.getMatricula());
                for (Multa m : multas){
                    multaDAO.borrarMulta(m);
                }
            }
        }catch (Exception e){
            System.out.println("Error de BD MySQL");
        }
        return cambiadoCorrectamente;
    }

    @Override
    public boolean actualizarCoche(Coche c) {
        boolean cambiadoCorrectamente = false;
        try {
            String sql = "UPDATE COCHES SET marca = ?, modelo = ?, tipo = ?, codSecreto = ? where matricula = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(5, c.getMatricula());
            ps.setString(1, c.getMarca());
            ps.setString(2, c.getModelo());
            ps.setString(3, c.getTipo());
            ps.setString(4, c.getCodSecreto());

            int filasActualizadas = ps.executeUpdate();
            cambiadoCorrectamente = filasActualizadas==1;
        }catch (Exception e){
            System.out.println("Error de BD MySQL");
        }
        return cambiadoCorrectamente;
    }

    @Override
    public Coche buscarCoche(String matricula) {
        Coche c = null;
        try{
            String sql = "SELECT * FROM COCHES WHERE MATRICULA = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, matricula);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                c = new Coche(
                        matricula,
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("tipo"),
                        rs.getString("codSecreto")
                );
                c.setId(rs.getInt("id"));
            }
        }catch (Exception e){
            System.out.println("Error de BD MySQL");
        }
        return c;
    }

    @Override
    public List<Coche> listarCoches() {
        List<Coche> listaCoches = new ArrayList<>();
        try{
            String sql = "SELECT * FROM COCHES";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Coche c = new Coche(
                        rs.getString("matricula"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("tipo"),
                        rs.getString("codSecreto")
                );
                c.setId(rs.getInt("id"));
                listaCoches.add(c);
            }
        }catch (Exception e){
            System.out.println("Error de BD MySQL");
        }
        return listaCoches;
    }
}
