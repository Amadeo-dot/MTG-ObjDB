package com.mycompany.mtg.objdb.Services;

import com.mycompany.mtg.objdb.Classes.Carta;
import com.mycompany.mtg.objdb.Classes.Criatura;
import com.mycompany.mtg.objdb.Classes.Encanteri;
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
        String jpql = "SELECT c FROM Carta c, Criatura cr "
                + "WHERE c = cr "
                + "AND cr.vola = true "
                + "AND cr.cost.negre > :minimNegre";
        return em.createQuery(jpql, Carta.class)
                .setParameter("minimNegre", minimNegre)
                .getResultList();
    }

    public Double mitjanaForcaCriaturesDeJugador(String nick) {
        String jpql = "SELECT AVG(c.forca) "
                + "FROM Jugador j JOIN j.mazos m, Criatura c "
                + "WHERE j.nick = :nick AND c MEMBER OF m.cartes";
        Double mitjana = em.createQuery(jpql, Double.class)
                .setParameter("nick", nick)
                .getSingleResult();
        return mitjana == null ? 0.0 : mitjana;
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
