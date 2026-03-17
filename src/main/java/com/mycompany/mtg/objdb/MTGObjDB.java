/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mtg.objdb;

import com.mycompany.mtg.objdb.Classes.Carta;
import com.mycompany.mtg.objdb.Services.ConsultesCartes;
import com.mycompany.mtg.objdb.Services.GestorCartes;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author alumne
 */
public class MTGObjDB {

    public static void main(String[] args) {
        String rutaBd = "objectdb:db/mtg.odb";
        String rutaFitxer = "src/main/resources/cartes.txt";
        boolean reiniciar = args != null && args.length > 0
                && "reiniciar".equalsIgnoreCase(args[0]);

        GestorCartes gestor = new GestorCartes();
        gestor.obrirConnexio(rutaBd);

        try {
            if (reiniciar) {
                gestor.reiniciarBaseDades();
                gestor.importarCartes(rutaFitxer);
                System.out.println("Base de dades reiniciada i cartes importades.");
            } else if (gestor.comptarCartes() == 0) {
                gestor.importarCartes(rutaFitxer);
                System.out.println("Cartes importades.");
            } else {
                System.out.println("Ja hi ha cartes a la base de dades.");
            }

            String nick = "jugador_" + (System.currentTimeMillis() % 10000);
            Long idJugador = gestor.crearJugador(nick, 5);
            Long idMazo = gestor.crearMazoPerJugador(idJugador, "Mazo Base", LocalDate.now());
            System.out.println("Jugador creat: " + nick + " (id=" + idJugador + ")");
            System.out.println("Mazo creat: id=" + idMazo);

            List<Carta> cartes = gestor.llistarCartes();
            int limit = Math.min(3, cartes.size());
            for (int i = 0; i < limit; i++) {
                gestor.afegirCartaAMazo(idMazo, cartes.get(i).getId());
                gestor.afegirCartaAColleccioJugador(idJugador, cartes.get(i).getId());
            }
            System.out.println("Cartes afegides al mazo i a la col.leccio.");

            long idCartaProva = cartes.get(0).getId();
            Carta carta = gestor.buscarPerId(idCartaProva);
            System.out.println("READ per ID: " + carta.getNom());
            System.out.println("Garantia d'identitat (obj1 == obj2): "
                    + gestor.comprovarGarantiaIdentitat(idCartaProva));

            ConsultesCartes consultes = new ConsultesCartes(gestor.getEntityManager());
            System.out.println("JPQL 1 - Noms criatures que volen i negre > 2:");
            System.out.println(consultes.nomsCriaturesQueVolenINegreMesDe(2));

            System.out.println("JPQL 2 - Mateixa idea en polimorfic pur:");
            System.out.println(consultes.cartesPolimorfiquesCriaturesQueVolenINegreMesDe(2).size());

            System.out.println("JPQL 3 - Mitjana forca criatures del jugador " + nick + ":");
            System.out.println(consultes.mitjanaForcaCriaturesDeJugador(nick));

            System.out.println("JPQL 4 - Encanteris sense blau ni blanc i incolor alt:");
            System.out.println(consultes.encanterisSenseBlauNiBlancIAmbIncolorAlt(3).size());

            gestor.actualitzarDescripcioManaged(idCartaProva, "Descripcio actualitzada (managed).");
            System.out.println("UPDATE managed correcte.");

            Carta cartaDetached = gestor.obtenirCartaDetachedTancantEM(idCartaProva);
            gestor.ferMergeCartaDetached(cartaDetached, cartaDetached.getNom() + " [MERGE]");
            System.out.println("UPDATE detached + merge correcte.");

            if (cartes.size() > 1) {
                long idCartaMazo = cartes.get(1).getId();
                gestor.eliminarCartaDeMazo(idMazo, idCartaMazo);
                System.out.println("S'ha tret una carta del mazo (nomes relacio).");
                System.out.println("La carta continua existint? " + gestor.existeixCarta(idCartaMazo));
            }
        } finally {
            gestor.tancarConnexio();
        }
    }
}
