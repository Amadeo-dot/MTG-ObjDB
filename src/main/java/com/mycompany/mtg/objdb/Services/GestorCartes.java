package com.mycompany.mtg.objdb.Services;

import com.mycompany.mtg.objdb.Classes.Carta;
import com.mycompany.mtg.objdb.Classes.ColorMana;
import com.mycompany.mtg.objdb.Classes.CostMana;
import com.mycompany.mtg.objdb.Classes.Criatura;
import com.mycompany.mtg.objdb.Classes.Encanteri;
import com.mycompany.mtg.objdb.Classes.Jugador;
import com.mycompany.mtg.objdb.Classes.Mazo;
import com.mycompany.mtg.objdb.Classes.Raresa;
import com.mycompany.mtg.objdb.Classes.Terra;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.text.Normalizer;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class GestorCartes {

    private EntityManagerFactory emf;
    private EntityManager em;

    public void obrirConnexio(String rutaBd) {
        emf = Persistence.createEntityManagerFactory(rutaBd);
        em = emf.createEntityManager();
    }

    public void tancarConnexio() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    public void reobrirEntityManager() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        em = emf.createEntityManager();
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public long comptarCartes() {
        try {
            return em.createQuery("SELECT COUNT(c) FROM Carta c", Long.class).getSingleResult();
        } catch (RuntimeException ex) {
            return 0;
        }
    }

    public List<Carta> llistarCartes() {
        return em.createQuery("SELECT c FROM Carta c", Carta.class).getResultList();
    }

    public List<Jugador> llistarJugadors() {
        return em.createQuery("SELECT j FROM Jugador j", Jugador.class).getResultList();
    }

    public List<Mazo> llistarMazos() {
        return em.createQuery("SELECT m FROM Mazo m", Mazo.class).getResultList();
    }

    public void importarCartes(String rutaArxiu) {
        List<String> linies;
        try {
            linies = Files.readAllLines(Path.of(rutaArxiu), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException("No s'ha pogut llegir el fitxer: " + rutaArxiu, ex);
        }

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            for (String linia : linies) {
                Carta carta = parsejarCarta(linia);
                if (carta != null) {
                    em.persist(carta);
                }
            }
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public void reiniciarBaseDades() {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            List<Jugador> jugadors = em.createQuery("SELECT j FROM Jugador j", Jugador.class)
                    .getResultList();
            for (Jugador jugador : jugadors) {
                em.remove(jugador);
            }

            List<Carta> cartes = em.createQuery("SELECT c FROM Carta c", Carta.class)
                    .getResultList();
            for (Carta carta : cartes) {
                em.remove(carta);
            }

            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public Long crearJugador(String nick, int nivell) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Jugador jugador = new Jugador(nick, nivell);
            em.persist(jugador);
            em.flush();
            tx.commit();
            return jugador.getId();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public Long crearMazoPerJugador(Long idJugador, String nomMazo, LocalDate dataCreacio) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Jugador jugador = em.find(Jugador.class, idJugador);
            if (jugador == null) {
                throw new IllegalArgumentException("Jugador no trobat: " + idJugador);
            }

            Mazo mazo = new Mazo(nomMazo, dataCreacio);
            jugador.afegirMazo(mazo);
            em.persist(mazo);
            em.flush();
            tx.commit();
            return mazo.getId();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public void afegirCartaAMazo(Long idMazo, Long idCarta) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Mazo mazo = em.find(Mazo.class, idMazo);
            Carta carta = em.find(Carta.class, idCarta);
            if (mazo != null && carta != null) {
                mazo.afegirCarta(carta);
            }
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public void afegirCartaAColleccioJugador(Long idJugador, Long idCarta) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Jugador jugador = em.find(Jugador.class, idJugador);
            Carta carta = em.find(Carta.class, idCarta);
            if (jugador != null && carta != null) {
                jugador.afegirCartaColleccio(carta);
            }
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public Carta buscarPerId(long id) {
        return em.find(Carta.class, id);
    }

    public boolean comprovarGarantiaIdentitat(long id) {
        Carta c1 = em.find(Carta.class, id);
        Carta c2 = em.find(Carta.class, id);
        return c1 != null && c1 == c2;
    }

    public void actualitzarDescripcioManaged(long id, String novaDescripcio) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Carta carta = em.find(Carta.class, id);
            if (carta != null) {
                carta.setDescripcio(novaDescripcio);
            }
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public Carta obtenirCartaDetachedTancantEM(long id) {
        Carta carta = em.find(Carta.class, id);
        if (em.isOpen()) {
            em.close();
        }
        em = emf.createEntityManager();
        return carta;
    }

    public void ferMergeCartaDetached(Carta cartaDetached, String nouNom) {
        if (cartaDetached == null) {
            return;
        }
        cartaDetached.setNom(nouNom);

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(cartaDetached);
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public void eliminarCartaPerId(long id) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Carta carta = em.find(Carta.class, id);
            if (carta != null) {
                em.remove(carta);
            }
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public void eliminarCartaDeMazo(long idMazo, long idCarta) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Mazo mazo = em.find(Mazo.class, idMazo);
            Carta carta = em.find(Carta.class, idCarta);
            if (mazo != null && carta != null) {
                mazo.eliminarCarta(carta);
            }
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public boolean existeixCarta(long idCarta) {
        return em.find(Carta.class, idCarta) != null;
    }

    private Carta parsejarCarta(String linia) {
        if (linia == null) {
            return null;
        }
        String txt = linia.trim();
        if (txt.isEmpty() || txt.startsWith("#")) {
            return null;
        }

        if (txt.contains("|")) {
            return parsejarCartaFormatPipe(txt);
        }

        return parsejarCartaFormatAntic(txt);
    }

    private Carta parsejarCartaFormatAntic(String txt) {
        String[] p = txt.split(";");
        if (p.length < 8) {
            throw new IllegalArgumentException("Linia invalida: " + txt);
        }

        String tipus = p[0].trim().toUpperCase();
        String nom = p[1].trim();
        String descripcio = p[2].trim();
        Raresa raresa = parsejarRaresa(p[3]);
        String edicio = p[4].trim();
        CostMana cost = parsejarCostOrdreAntic(p[5].trim());

        if ("CRIATURA".equals(tipus)) {
            return new Criatura(
                    nom,
                    descripcio,
                    raresa,
                    edicio,
                    cost,
                    Integer.parseInt(p[6].trim()),
                    Integer.parseInt(p[7].trim()),
                    p[8].trim(),
                    Boolean.parseBoolean(p[9].trim())
            );
        }

        if ("TERRA".equals(tipus)) {
            return new Terra(
                    nom,
                    descripcio,
                    raresa,
                    edicio,
                    cost,
                    ColorMana.valueOf(normalitzarToken(p[6])),
                    Boolean.parseBoolean(p[7].trim())
            );
        }

        if ("ENCANTERI".equals(tipus)) {
            return new Encanteri(
                    nom,
                    descripcio,
                    raresa,
                    edicio,
                    cost,
                    p[6].trim(),
                    Boolean.parseBoolean(p[7].trim())
            );
        }

        throw new IllegalArgumentException("Tipus desconegut: " + tipus);
    }

    private Carta parsejarCartaFormatPipe(String txt) {
        String[] p = txt.split("\\|");
        for (int i = 0; i < p.length; i++) {
            p[i] = p[i].trim();
        }

        String tipus = normalitzarToken(p[0]);
        String nom = p[1];
        String descripcio = p[2];
        Raresa raresa = parsejarRaresa(p[3]);
        String edicio = "";

        if ("CRIATURA".equals(tipus)) {
            CostMana cost = parsejarCostOrdreImatge(p[4]);
            return new Criatura(
                    nom,
                    descripcio,
                    raresa,
                    edicio,
                    cost,
                    Integer.parseInt(p[5]),
                    Integer.parseInt(p[6]),
                    p[7],
                    Boolean.parseBoolean(p[8])
            );
        }

        if ("TERRA".equals(tipus)) {
            return new Terra(
                    nom,
                    descripcio,
                    raresa,
                    edicio,
                    new CostMana(),
                    ColorMana.valueOf(normalitzarToken(p[4])),
                    Boolean.parseBoolean(p[5])
            );
        }

        if ("ENCANTERI".equals(tipus)) {
            CostMana cost = parsejarCostOrdreImatge(p[4]);
            return new Encanteri(
                    nom,
                    descripcio,
                    raresa,
                    edicio,
                    cost,
                    p[5],
                    Boolean.parseBoolean(p[6])
            );
        }

        throw new IllegalArgumentException("Tipus desconegut: " + tipus);
    }

    private CostMana parsejarCostOrdreAntic(String txt) {
        String[] c = txt.split(",");
        if (c.length != 6) {
            throw new IllegalArgumentException("Cost invalid: " + txt);
        }
        return new CostMana(
                Integer.parseInt(c[0].trim()),
                Integer.parseInt(c[1].trim()),
                Integer.parseInt(c[2].trim()),
                Integer.parseInt(c[3].trim()),
                Integer.parseInt(c[4].trim()),
                Integer.parseInt(c[5].trim())
        );
    }

    private CostMana parsejarCostOrdreImatge(String txt) {
        String[] c = txt.split(",");
        if (c.length != 6) {
            throw new IllegalArgumentException("Cost invalid: " + txt);
        }
        int blau = Integer.parseInt(c[0].trim());
        int negre = Integer.parseInt(c[1].trim());
        int vermell = Integer.parseInt(c[2].trim());
        int verd = Integer.parseInt(c[3].trim());
        int blanc = Integer.parseInt(c[4].trim());
        int incolor = Integer.parseInt(c[5].trim());

        return new CostMana(blanc, blau, negre, vermell, verd, incolor);
    }

    private Raresa parsejarRaresa(String txt) {
        return Raresa.valueOf(normalitzarToken(txt));
    }

    private String normalitzarToken(String txt) {
        String text = txt == null ? "" : txt.trim().toUpperCase();
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = text.replaceAll("\\p{M}", "");
        return text;
    }
}
