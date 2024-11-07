package com.erikat.crudjavageneral.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "coches")
public class Coche implements Serializable {
    /*
  TABLE coches (
  id integer NOT NULL AUTO_INCREMENT,
  matricula varchar(50) DEFAULT NULL,
  marca varchar(50) DEFAULT NULL,
  modelo varchar(50) DEFAULT NULL,
  tipo varchar(50) DEFAULT NULL,
  PRIMARY KEY (id)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "matricula")
    private String matricula;

    @Column(name = "marca")
    private String marca;

    @Column(name = "modelo")
    private String modelo;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "codSecreto")
    private String codSecreto;

    @OneToMany(mappedBy = "coche", cascade = CascadeType.ALL) //OneToMany: Lo tiene el de la relación 1
    private List<Multa> multas; //Un coche puede tener varias multas

    public Coche(String matricula, String marca, String modelo, String tipo, String codSecreto) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
        this.codSecreto = codSecreto;
        multas = new ArrayList<>();
    }

    public Coche(int id, String matricula, String marca, String modelo, String tipo, List<Multa> multas) {
        this.id = id;
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
        this.multas = multas;
    }

    public Coche() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<Multa> getMultas() {
        return multas;
    }

    public void setMultas(List<Multa> multas) {
        this.multas = multas;
    }

    public String getCodSecreto() {
        return codSecreto;
    }

    public void setCodSecreto(String codSecreto) {
        this.codSecreto = codSecreto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coche coche = (Coche) o;
        return Objects.equals(getMatricula(), coche.getMatricula()) && Objects.equals(getMarca(), coche.getMarca()) && Objects.equals(getModelo(), coche.getModelo()) && Objects.equals(getTipo(), coche.getTipo()) && Objects.equals(coche.getCodSecreto(), getCodSecreto());
    }
}
