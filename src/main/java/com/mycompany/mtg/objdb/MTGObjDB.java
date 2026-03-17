package com.mycompany.mtg.objdb;

import com.mycompany.mtg.objdb.Classes.Carta;
import com.mycompany.mtg.objdb.Classes.Jugador;
import com.mycompany.mtg.objdb.Classes.Mazo;
import com.mycompany.mtg.objdb.Services.ConsultesCartes;
import com.mycompany.mtg.objdb.Services.GestorCartes;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MTGObjDB {

    public static void main(String[] args) {
        String rutaBd = "objectdb:db/mtg.odb";
        String rutaFitxer = "src/main/resources/cartes.txt";
        boolean reiniciar = args != null && args.length > 0
                && "reiniciar".equalsIgnoreCase(args[0]);

        GestorCartes gestor = new GestorCartes();
        gestor.obrirConnexio(rutaBd);

        try (Scanner scanner = new Scanner(System.in)) {
            if (reiniciar) {
                gestor.reiniciarBaseDades();
                gestor.importarCartes(rutaFitxer);
                System.out.println("Base de datos reiniciada e importada.");
            } else if (gestor.comptarCartes() == 0) {
                gestor.importarCartes(rutaFitxer);
                System.out.println("Cartas importadas por primera vez.");
            }

            mostrarMenu(gestor, scanner, rutaFitxer);
        } finally {
            gestor.tancarConnexio();
        }
    }

    private static void mostrarMenu(GestorCartes gestor, Scanner scanner, String rutaFitxer) {
        boolean salir = false;

        while (!salir) {
            System.out.println();
            System.out.println("==== POLY-DECK MENU ====");
            System.out.println("1. Listar cartas");
            System.out.println("2. Listar jugadores");
            System.out.println("3. Listar mazos");
            System.out.println("4. Crear jugador");
            System.out.println("5. Crear mazo para jugador");
            System.out.println("6. Anadir carta a mazo");
            System.out.println("7. Ejecutar consultas JPQL");
            System.out.println("8. Probar update y merge");
            System.out.println("9. Reiniciar base e importar cartas");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");

            String opcion = scanner.nextLine().trim();
            System.out.println();

            switch (opcion) {
                case "1" -> listarCartas(gestor);
                case "2" -> listarJugadores(gestor);
                case "3" -> listarMazos(gestor);
                case "4" -> crearJugador(gestor, scanner);
                case "5" -> crearMazo(gestor, scanner);
                case "6" -> anadirCartaAMazo(gestor, scanner);
                case "7" -> ejecutarConsultas(gestor, scanner);
                case "8" -> probarEstados(gestor, scanner);
                case "9" -> reiniciarEImportar(gestor, rutaFitxer);
                case "0" -> salir = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private static void listarCartas(GestorCartes gestor) {
        List<Carta> cartas = gestor.llistarCartes();
        if (cartas.isEmpty()) {
            System.out.println("No hay cartas cargadas.");
            return;
        }

        System.out.println("Listado de cartas:");
        for (Carta carta : cartas) {
            System.out.println(carta.getId() + " - " + carta.getNom()
                    + " (" + carta.getClass().getSimpleName() + ")");
        }
    }

    private static void listarJugadores(GestorCartes gestor) {
        List<Jugador> jugadors = gestor.llistarJugadors();
        if (jugadors.isEmpty()) {
            System.out.println("No hay jugadores.");
            return;
        }

        System.out.println("Listado de jugadores:");
        for (Jugador jugador : jugadors) {
            System.out.println(jugador.getId() + " - " + jugador.getNick()
                    + " (nivel " + jugador.getNivell() + ")");
        }
    }

    private static void listarMazos(GestorCartes gestor) {
        List<Mazo> mazos = gestor.llistarMazos();
        if (mazos.isEmpty()) {
            System.out.println("No hay mazos.");
            return;
        }

        System.out.println("Listado de mazos:");
        for (Mazo mazo : mazos) {
            String propietario = mazo.getJugador() == null ? "sin jugador" : mazo.getJugador().getNick();
            System.out.println(mazo.getId() + " - " + mazo.getNom()
                    + " [" + propietario + "] - " + mazo.getCartes().size() + " cartas");
        }
    }

    private static void crearJugador(GestorCartes gestor, Scanner scanner) {
        System.out.print("Nick del jugador: ");
        String nick = scanner.nextLine().trim();
        System.out.print("Nivel del jugador: ");
        int nivel = llegirEnter(scanner);

        Long id = gestor.crearJugador(nick, nivel);
        System.out.println("Jugador creado con id " + id);
    }

    private static void crearMazo(GestorCartes gestor, Scanner scanner) {
        listarJugadores(gestor);
        System.out.print("Id del jugador: ");
        long idJugador = llegirLong(scanner);
        System.out.print("Nombre del mazo: ");
        String nomMazo = scanner.nextLine().trim();

        Long idMazo = gestor.crearMazoPerJugador(idJugador, nomMazo, LocalDate.now());
        System.out.println("Mazo creado con id " + idMazo);
    }

    private static void anadirCartaAMazo(GestorCartes gestor, Scanner scanner) {
        listarMazos(gestor);
        System.out.print("Id del mazo: ");
        long idMazo = llegirLong(scanner);
        listarCartas(gestor);
        System.out.print("Id de la carta: ");
        long idCarta = llegirLong(scanner);

        gestor.afegirCartaAMazo(idMazo, idCarta);
        System.out.println("Carta anadida al mazo.");
    }

    private static void ejecutarConsultas(GestorCartes gestor, Scanner scanner) {
        ConsultesCartes consultes = new ConsultesCartes(gestor.getEntityManager());

        System.out.println("1. Criaturas que vuelan y tienen negro > 2");
        System.out.println("2. Consulta polimorfica equivalente");
        System.out.println("3. Media de fuerza de criaturas de un jugador");
        System.out.println("4. Encantamientos sin azul ni blanco y con incolor alto");
        System.out.print("Elige consulta: ");

        String opcion = scanner.nextLine().trim();

        switch (opcion) {
            case "1" -> System.out.println(consultes.nomsCriaturesQueVolenINegreMesDe(2));
            case "2" -> System.out.println(consultes.cartesPolimorfiquesCriaturesQueVolenINegreMesDe(2));
            case "3" -> {
                System.out.print("Nick del jugador: ");
                String nick = scanner.nextLine().trim();
                System.out.println(consultes.mitjanaForcaCriaturesDeJugador(nick));
            }
            case "4" -> System.out.println(consultes.encanterisSenseBlauNiBlancIAmbIncolorAlt(3));
            default -> System.out.println("Consulta no valida.");
        }
    }

    private static void probarEstados(GestorCartes gestor, Scanner scanner) {
        listarCartas(gestor);
        System.out.print("Id de la carta para probar update/merge: ");
        long idCarta = llegirLong(scanner);

        Carta carta = gestor.buscarPerId(idCarta);
        if (carta == null) {
            System.out.println("Carta no encontrada.");
            return;
        }

        gestor.actualitzarDescripcioManaged(idCarta, "Descripcion cambiada desde el menu.");
        System.out.println("Update managed hecho.");

        Carta cartaDetached = gestor.obtenirCartaDetachedTancantEM(idCarta);
        gestor.ferMergeCartaDetached(cartaDetached, cartaDetached.getNom() + " [EDITADA]");
        System.out.println("Merge sobre objeto detached hecho.");
    }

    private static void reiniciarEImportar(GestorCartes gestor, String rutaFitxer) {
        gestor.reiniciarBaseDades();
        gestor.importarCartes(rutaFitxer);
        System.out.println("Base de datos reiniciada y cartas importadas.");
    }

    private static int llegirEnter(Scanner scanner) {
        while (true) {
            String text = scanner.nextLine().trim();
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException ex) {
                System.out.print("Introduce un numero valido: ");
            }
        }
    }

    private static long llegirLong(Scanner scanner) {
        while (true) {
            String text = scanner.nextLine().trim();
            try {
                return Long.parseLong(text);
            } catch (NumberFormatException ex) {
                System.out.print("Introduce un id valido: ");
            }
        }
    }
}
