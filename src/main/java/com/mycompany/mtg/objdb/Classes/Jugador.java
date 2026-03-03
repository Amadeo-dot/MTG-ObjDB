package com.mycompany.mtg.objdb.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "jugadors")
public class Jugador implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nom;

    @Column(length = 160, unique = true)
    private String email;

    @OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mazo> mazos = new ArrayList<>();

    protected Jugador() {
    }

    public Jugador(String nom) {
        this.nom = nom;
    }

    public Jugador(String nom, String email) {
        this.nom = nom;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Mazo> getMazos() {
        return mazos;
    }

    public void setMazos(List<Mazo> mazos) {
        List<Mazo> actuals = new ArrayList<>(this.mazos);
        for (Mazo mazoActual : actuals) {
            eliminarMazo(mazoActual);
        }
        if (mazos != null) {
            for (Mazo mazo : mazos) {
                afegirMazo(mazo);
            }
        }
    }

    public void afegirMazo(Mazo mazo) {
        if (mazo != null && !this.mazos.contains(mazo)) {
            this.mazos.add(mazo);
            mazo.setJugador(this);
        }
    }

    public boolean eliminarMazo(Mazo mazo) {
        if (mazo == null) {
            return false;
        }
        boolean eliminat = this.mazos.remove(mazo);
        if (eliminat) {
            mazo.setJugador(null);
        }
        return eliminat;
    }
}
