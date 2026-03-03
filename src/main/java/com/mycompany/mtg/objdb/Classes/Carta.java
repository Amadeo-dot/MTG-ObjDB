/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mtg.objdb.Classes;

/**
 *
 * @author alumne
 */
public abstract class Carta {
    Long id;
    String nom;
    String descripcio;
    Raresa raresa;
    String edicio;
    CostMana cost;

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
