package com.erikat.crudjavageneral.DAO;

import com.erikat.crudjavageneral.Model.Multa;
import com.erikat.crudjavageneral.Util.MySQLUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MultaDAOMySQL implements MultaDAOInterface{
    public Connection con;
    public MultaDAOMySQL(){
        con = MySQLUtils.getConnection();
    }
    @Override
    public boolean insertarMulta(Multa m) {
        boolean cambiadoCorrectamente = false;
        try {
            String sql = "INSERT INTO MULTAS(precio, fecha, matricula) VALUES(?, str_to_date(?, '%Y-%m-%d'), ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDouble(1, m.getPrecio());
            ps.setString(2, m.getFecha().toString());
            ps.setString(3, m.getCoche().getMatricula());

            int filasInsertadas = ps.executeUpdate();
            cambiadoCorrectamente = filasInsertadas==1;
        }catch (Exception e){
            System.out.println("Error de BD MySQL" + e.getMessage());
        }
        return cambiadoCorrectamente;
    }

    @Override
    public boolean borrarMulta(Multa m) {
        boolean cambiadoCorrectamente = false;
        try {
            String sql = "DELETE FROM MULTAS WHERE ID_MULTA = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, m.getId());

            int filasInsertadas = ps.executeUpdate();
            cambiadoCorrectamente = filasInsertadas==1;
        }catch (Exception e){
            System.out.println("Error de BD MySQL");
        }
        return cambiadoCorrectamente;
    }

    @Override
    public boolean actualizarMulta(Multa m) {
        boolean cambiadoCorrectamente = false;
        try {
            String sql = "UPDATE MULTAS SET precio=?, fecha=str_to_date(?, '%Y-%m-%d'), matricula = ? WHERE ID_MULTA = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDouble(1, m.getPrecio());
            ps.setString(2, m.getFecha().toString());
            ps.setString(3, m.getCoche().getMatricula());
            ps.setInt(4, m.getId());

            int filasInsertadas = ps.executeUpdate();
            cambiadoCorrectamente = filasInsertadas==1;
        }catch (Exception e){
            System.out.println("Error de BD MySQL" + e.getMessage());
        }
        return cambiadoCorrectamente;
    }

    @Override
    public Multa buscarMulta(int id) {
        Multa multa = null;
        try{
            String sql = "SELECT * FROM MULTAS WHERE ID_MULTA = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                multa = new Multa(
                        id,
                        rs.getDouble("precio"),
                        LocalDate.parse(rs.getString("fecha")),
                        new CocheDAOMySQL().buscarCoche(rs.getString("matricula"))
                );
            }
        }catch (Exception e){
            System.out.println("Error de BD MySQL");
        }
        return multa;
    }

    @Override
    public List<Multa> listarMultas(String matricula) {
        List<Multa> listaMultas = new ArrayList<>();
        try{
            String sql = "SELECT * FROM MULTAS WHERE MATRICULA = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, matricula);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Multa multa = new Multa(
                        rs.getInt("id_multa"),
                        rs.getDouble("precio"),
                        LocalDate.parse(rs.getString("fecha")),
                        new CocheDAOMySQL().buscarCoche(rs.getString("matricula"))
                );
                listaMultas.add(multa);
            }
        }catch (Exception e){
            System.out.println("Error de BD MySQL");
        }
        return listaMultas;
    }
}
