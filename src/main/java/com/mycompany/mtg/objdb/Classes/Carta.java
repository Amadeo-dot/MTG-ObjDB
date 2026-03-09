/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mtg.objdb.Classes;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author alumne
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Carta implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nom;

    @Column(length = 500)
    private String descripcio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Raresa raresa;

    @Column(length = 80)
    private String edicio;

    @Embedded
    private CostMana cost;

    protected Carta() {
    }

    public Carta(String nom, String descripcio, Raresa raresa, String edicio, CostMana cost) {
        this(null, nom, descripcio, raresa, edicio, cost);
    }

    public Carta(Long id, String nom, String descripcio, Raresa raresa, String edicio, CostMana cost) {
        this.id = id;
        this.nom = nom;
        this.descripcio = descripcio;
        this.raresa = raresa;
        this.edicio = edicio;
        this.cost = cost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public Raresa getRaresa() {
        return raresa;
    }

    public void setRaresa(Raresa raresa) {
        this.raresa = raresa;
    }

    public String getEdicio() {
        return edicio;
    }

    public void setEdicio(String edicio) {
        this.edicio = edicio;
    }

    public CostMana getCost() {
        return cost;
    }

    public void setCost(CostMana cost) {
        this.cost = cost;
    }
    
}
