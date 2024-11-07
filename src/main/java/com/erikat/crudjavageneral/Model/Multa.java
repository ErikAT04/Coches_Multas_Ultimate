package com.erikat.crudjavageneral.Model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "multas")
public class Multa implements Serializable {
  /*
  TABLE multas (
  id_multa integer unsigned NOT NULL AUTO_INCREMENT,
  precio DOUBLE NOT NULL,
  fecha DATE DEFAULT NULL,
  matricula varchar(7) NOT NULL,
  PRIMARY KEY (id_multa)
  )
  */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_multa")
    private int id;

    @Column(name = "precio")
    private double precio;

    @Column(name = "fecha")
    private LocalDate fecha;

    @ManyToOne //ManyToOne: Lo tiene el de la relación de la N
    @JoinColumn(name = "matricula", referencedColumnName = "matricula")
    private Coche coche;

    public Multa(int id, double precio, LocalDate fecha, Coche coche) {
        this.id = id;
        this.precio = precio;
        this.fecha = fecha;
        this.coche = coche;
    }

    public Multa(double precio, LocalDate fecha, Coche coche) {
        this.precio = precio;
        this.fecha = fecha;
        this.coche = coche;
    }

    public Multa() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Coche getCoche() {
        return coche;
    }

    public void setCoche(Coche coche) {
        this.coche = coche;
    }
}
