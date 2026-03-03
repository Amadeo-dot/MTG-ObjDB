package com.mycompany.mtg.objdb.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "mazos")
public class Mazo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jugador_id")
    private Jugador jugador;

    @ManyToMany
    @JoinTable(
            name = "mazo_cartas",
            joinColumns = @JoinColumn(name = "mazo_id"),
            inverseJoinColumns = @JoinColumn(name = "carta_id")
    )
    private List<Carta> cartes = new ArrayList<>();

    protected Mazo() {
    }

    public Mazo(String nom) {
        this.nom = nom;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public List<Carta> getCartes() {
        return cartes;
    }

    public void setCartes(List<Carta> cartes) {
        this.cartes = cartes;
    }

    public void afegirCarta(Carta carta) {
        if (carta != null) {
            this.cartes.add(carta);
        }
    }

    public boolean eliminarCarta(Carta carta) {
        return this.cartes.remove(carta);
    }
}
