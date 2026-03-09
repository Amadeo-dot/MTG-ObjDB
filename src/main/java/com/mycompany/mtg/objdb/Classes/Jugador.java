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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "jugadors")
public class Jugador implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nick;

    @Column(nullable = false)
    private int nivell;

    @OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mazo> mazos = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "jugador_colleccio",
            joinColumns = @JoinColumn(name = "jugador_id"),
            inverseJoinColumns = @JoinColumn(name = "carta_id")
    )
    private List<Carta> colleccio = new ArrayList<>();

    protected Jugador() {
    }

    public Jugador(String nick, int nivell) {
        this.nick = nick;
        this.nivell = nivell;
    }

    public Long getId() {
        return id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getNivell() {
        return nivell;
    }

    public void setNivell(int nivell) {
        this.nivell = nivell;
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

    public List<Carta> getColleccio() {
        return colleccio;
    }

    public void setColleccio(List<Carta> colleccio) {
        this.colleccio = colleccio == null ? new ArrayList<>() : colleccio;
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

    public void afegirCartaColleccio(Carta carta) {
        if (carta != null && !this.colleccio.contains(carta)) {
            this.colleccio.add(carta);
        }
    }

    public boolean eliminarCartaColleccio(Carta carta) {
        if (carta == null) {
            return false;
        }
        return this.colleccio.remove(carta);
    }
}
