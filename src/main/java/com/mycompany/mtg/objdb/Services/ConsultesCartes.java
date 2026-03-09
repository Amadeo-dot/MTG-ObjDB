package com.mycompany.mtg.objdb.Services;

import com.mycompany.mtg.objdb.Classes.Carta;
import com.mycompany.mtg.objdb.Classes.Criatura;
import com.mycompany.mtg.objdb.Classes.Encanteri;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

public class ConsultesCartes {

    private final EntityManager em;

    public ConsultesCartes(EntityManager em) {
        this.em = em;
    }

    public List<String> nomsCriaturesQueVolenINegreMesDe(int minimNegre) {
        String jpql = "SELECT c.nom FROM Criatura c "
                + "WHERE c.vola = true AND c.cost.negre > :minimNegre";
        return em.createQuery(jpql, String.class)
                .setParameter("minimNegre", minimNegre)
                .getResultList();
    }

    public List<Carta> cartesPolimorfiquesCriaturesQueVolenINegreMesDe(int minimNegre) {
        String jpql = "SELECT c FROM Criatura c "
                + "WHERE c.vola = true AND c.cost.negre > :minimNegre";
        List<Criatura> criatures = em.createQuery(jpql, Criatura.class)
                .setParameter("minimNegre", minimNegre)
                .getResultList();
        return new ArrayList<>(criatures);
    }

    public Double mitjanaForcaCriaturesDeJugador(String nick) {
        String jpql = "SELECT c FROM Jugador j JOIN j.mazos m JOIN m.cartes c "
                + "WHERE j.nick = :nick";
        List<Carta> cartes = em.createQuery(jpql, Carta.class)
                .setParameter("nick", nick)
                .getResultList();

        int suma = 0;
        int total = 0;
        for (Carta carta : cartes) {
            if (carta instanceof Criatura criatura) {
                suma += criatura.getForca();
                total++;
            }
        }

        if (total == 0) {
            return 0.0;
        }
        return (double) suma / total;
    }

    public List<Encanteri> encanterisSenseBlauNiBlancIAmbIncolorAlt(int minimIncolor) {
        String jpql = "SELECT e FROM Encanteri e "
                + "WHERE e.cost.blau = 0 AND e.cost.blanc = 0 "
                + "AND e.cost.incolor > :minimIncolor";
        return em.createQuery(jpql, Encanteri.class)
                .setParameter("minimIncolor", minimIncolor)
                .getResultList();
    }

    public List<Criatura> criaturesQueVolenINegreMesDeSenseTypeTreat(int minimNegre) {
        String jpql = "SELECT c FROM Criatura c "
                + "WHERE c.vola = true AND c.cost.negre > :minimNegre";
        return em.createQuery(jpql, Criatura.class)
                .setParameter("minimNegre", minimNegre)
                .getResultList();
    }
}
