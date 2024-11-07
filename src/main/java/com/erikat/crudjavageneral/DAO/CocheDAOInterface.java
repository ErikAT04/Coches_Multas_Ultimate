package com.erikat.crudjavageneral.DAO;

import com.erikat.crudjavageneral.Model.Coche;

import java.util.List;

public interface CocheDAOInterface {
    public boolean insertarCoche(Coche c);
    public boolean borrarCoche(Coche c);
    public boolean actualizarCoche(Coche c);
    public Coche buscarCoche(String matricula);
    public List<Coche> listarCoches();
}
