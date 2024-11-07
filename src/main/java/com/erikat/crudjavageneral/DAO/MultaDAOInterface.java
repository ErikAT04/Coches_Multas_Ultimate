package com.erikat.crudjavageneral.DAO;

import com.erikat.crudjavageneral.Model.Multa;

import java.util.List;

public interface MultaDAOInterface {
    public boolean insertarMulta(Multa m);
    public boolean borrarMulta(Multa m);
    public boolean actualizarMulta(Multa m);
    public Multa buscarMulta(int id);
    public List<Multa> listarMultas(String matricula);
}
