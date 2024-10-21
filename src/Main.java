/*****************************************************************************************************

 M03_REP_EXTRA: Battleship

 Description: Battleship is a strategic two-player game where each player positions their ships on
 a hidden grid and attempts to guess the opponent's ship locations by launching attacks at specific
 coordinates. The goal is to sink all of the opponent's ships before they do the same to you, employing
 tactics and deductions to emerge victorious in the naval battle.

 Autor: Roger Alonso
 Cicle Formatiu Grau Superior DAM1A - Centre d'Estudis Politecnics 2023-2024

 ******************************************************************************************************/

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_REVERSED = "\u001b[7m";
    public static final String ANSI_NEGRITA = "\u001b[1m";
    public static final String WHITE_BACKGROUND = "\033[47m";
    public static final String BLACK = "\u001B[30m";


    public static void main(String[] args) {

        //Declarations
        int menu;
        boolean exit;


        //Game
        exit = false;
        do {
            menu = getMenuOption();

            switch (menu) {
                case 1:
                    //Gameplay 1vs1
                    gameplay();
                    break;

                case 2:
                    //Gameplay vs IA
                    gameplayIA();
                    break;

                case 3:
                    //Exit
                    exit = exit();
                    break;
            }
        } while (!exit);
    }
    private static void gameplayIA() {
        String name1, name2, aux;
        char[][] tablero1;
        char[][] tablero2;

        boolean play;

        loadingScreen();

        name1 = crearJugador(1);
        tablero1 = crearTablero();

        name2 = "IA";
        tablero2 = crearTablero();

        if (escogerPrimerTurno(name1, name2)) {
            aux = name2;
            name2 = name1;
            name1 = aux;
        }

        if (name1.equals("IA")){
            colocarBarcos(tablero1, name1, true);
            colocarBarcos(tablero2, name2, preguntarColocarBarcos(name2));
            pausa(5000);
        } else {
            colocarBarcos(tablero1, name1, preguntarColocarBarcos(name1));
            pausa(5000);
            colocarBarcos(tablero2, name2, true);
        }

        limpiarPantalla();

        play = true;
        do {

            if (name1.equals("IA")){
                if (finalGame(tablero2)) {
                    play = false;
                } else {
                    showTableroGame(tablero1);
                    System.out.println("IA es hora de disparar! ");
                    pausa(2000);
                    disparar(tablero1, (int) (Math.random() * 10), (int) (Math.random() * 10));
                    pausa(2000);
                }

                showTableroGame(tablero2);
                System.out.println(name2 + " es hora de disparar! ");
                disparar(tablero2, y(), x());

            } else {
                if (finalGame(tablero2)) {
                    play = false;
                } else {
                    showTableroGame(tablero2);
                    System.out.println(name1 + " es hora de disparar! ");
                    disparar(tablero2, y(), x());
                }

                showTableroGame(tablero1);
                System.out.println("IA es hora de disparar! ");
                pausa(2000);
                disparar(tablero1, (int) (Math.random() * 10), (int) (Math.random() * 10));
                pausa(2000);

                if (finalGame(tablero1)) {
                    play = false;
                }
            }
        } while (play);
    }
    private static void gameplay() {
        String name1, name2, aux;
        char[][] tablero1;
        char[][] tablero2;
        int[] aciertos = new int[4]; // 0 - aciertos jugador 1, 1 - aciertos jugador 2, 2 - fallos jugador 1, 3 - fallos jugador 2
        boolean play;


        loadingScreen();

        name1 = crearJugador(1);
        tablero1 = crearTablero();
        aciertos[0] = 0;

        name2 = crearJugador(2);
        tablero2 = crearTablero();
        aciertos[2] = 0;

        if (escogerPrimerTurno(name1, name2)) {
            aux = name2;
            name2 = name1;
            name1 = aux;
        }
        colocarBarcos(tablero1, name1, preguntarColocarBarcos(name1));

        pausa(5000);
        limpiarPantalla();

        colocarBarcos(tablero2, name2, preguntarColocarBarcos(name2));

        pausa(5000);
        limpiarPantalla();

        play = true;
        do {
            jugada(tablero2, name1, aciertos, 0, 2);

            pausa(2000);
            limpiarPantalla();

            if (finalGame(tablero2)) {
                mensajeFinal(name1, name2, aciertos, name1);
                play = false;
            } else {
                jugada(tablero1, name2, aciertos, 1, 3);
            }

            pausa(2000);
            limpiarPantalla();

            if (finalGame(tablero1)) {
                mensajeFinal(name1, name2, aciertos, name2);
                play = false;
            }
        } while (play);
    }
    private static void mensajeFinal(String name1, String name2, int[] aciertos, String ganador) {
        //Displays the final message at the end of the game.
        System.out.println("Ha ganado " + ganador);
        System.out.println(name1 + " ha acertado " + aciertos[0] + " de " + aciertos[2] + " que es un total de " + ((aciertos[0] * 100) / aciertos[2]) + "%.");
        System.out.println(name2 + " ha acertado " + aciertos[1] + " de " + aciertos[3] + " que es un total de " + ((aciertos[1] * 100) / aciertos[3]) + "%.");
        pausa(3000);
    }
    private static boolean escogerPrimerTurno(String name1, String name2) {
        //Choose which user is in charge of starting the shift.
        int random1, random2;
        boolean aux;


        do {
            aux = true;
            System.out.println("Tira el dado " + name1);
            random1 = (int) (Math.random() * 6) + 1;
            pausa(1000);
            System.out.println(name1 + " ha sacado el número " + random1 + "\n\n");

            pausa(1000);

            System.out.println("Tira el dado " + name2);
            random2 = (int) (Math.random() * 6) + 1;
            pausa(1000);
            System.out.println(name2 + " ha sacado el número " + random2 + "\n\n");

            pausa(1000);

            if (random1 == random2) {
                aux = false;
            }
        } while (!aux);

        if (random1 > random2) {
            System.out.println("Empieza " + name1 + "\n\n");
            pausa(2000);
            return false;
        } else {
            System.out.println("Empieza " + name2 + "\n\n");
            pausa(2000);
            return true;
        }
    }
    private static void jugada(char[][] tablero, String name, int[] aciertos, int jugador, int tirosTotales) {
        //Turn to fire on the board.
        boolean acierto;

        showTableroGame(tablero);
        do {
            System.out.println(name + " es hora de disparar! ");
            acierto = disparar(tablero, y(), x());
            if (acierto){
                aciertos[jugador] ++;
            }
            aciertos[tirosTotales] ++;
        } while (acierto && !finalGame(tablero));
    }
    private static void pausa(int i) {
        //Pauses the execution of a game.
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private static String crearJugador(int i) {
        //Create player.
        System.out.println("Nombre del jugador " + i + ": ");
        return Teclat.llegirString();
    }
    private static void colocarBarcos(char[][] tablero, String name, boolean esIA) {
        //Manual boat positioning function.
        if(!esIA) showTablero(tablero);

        if (!esIA) System.out.println(name + " és hora de colocar tu barco de 5 casillas: \n");
        colocarBarco(tablero, 5, esIA);
        if (!esIA) showTablero(tablero);

        if (!esIA) System.out.println(name + " és hora de colocar tus barcos de 4 casillas: \n");
        for (int i = 0; i < 2; i++) {
            colocarBarco(tablero, 4, esIA);
            if (!esIA) showTablero(tablero);
        }

        if (!esIA) System.out.println(name + " és hora de colocar tus barcos de 3 casillas: \n");
        for (int i = 0; i < 3; i++) {
            colocarBarco(tablero, 3, esIA);
            if (!esIA) showTablero(tablero);
        }

        if (!esIA) System.out.println(name + " és hora de colocar tus barcos de 2 casillas: \n");
        for (int i = 0; i < 4; i++) {
            colocarBarco(tablero, 2, esIA);
            if (!esIA) showTablero(tablero);
        }
        limpiarPantalla();
        showTablero(tablero);
    }
    private static void limpiarPantalla() {
        //Clean the console.
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
    private static boolean disparar(char[][] tablero, int y, int x) {
        //Check what happens with the shot on the given coordinates.
        if (tablero[x][y] == 'X') {
            showTableroGame(tablero);
            System.out.println("Casilla ya jugada, pierdes el tiro.");
            return false;
        } else if (tablero[x][y] == 'O') {
            showTableroGame(tablero);
            System.out.println("Casilla ya jugada, has perdido el tiro.");
            return false;
        } else if (tablero[x][y] == 'S') {
            tablero[x][y] = 'O';
            showTableroGame(tablero);
            System.out.println("                          " + ANSI_NEGRITA + ANSI_REVERSED + ANSI_YELLOW + "IMPACTO!" + ANSI_RESET);
            pausa(1000);
            return true;
        } else {
            showTableroGame(tablero);
            System.out.println("                         " + ANSI_NEGRITA + ANSI_REVERSED + ANSI_BLUE + "AGUA!" + ANSI_RESET);
            return false;
        }
    }
    private static void colocarBarco(char[][] tablero, int size, boolean esIA) {
        //Coloca los barcos sobre el talbero.
        boolean aux;
        int x, y, aux2;
        char orientacion;

        aux = true;
        do {
            if (esIA) {
                y = (int) (Math.random() * 10);
                x = (int) (Math.random() * 10);
                aux2 = (int) (Math.random() * 2);
                orientacion = (aux2 == 0) ? 'h' : 'v';
            } else {
                y = y();
                x = x();
                orientacion = orientacion();
            }

            if (casillaOcupada(tablero, x, y, orientacion, size)) {
                if (!esIA) System.out.println("Casilla ocupada");
            } else {
                if (orientacion == 'h') {
                    for (int i = 0; i < size; i++) {
                        tablero[x][y + i] = 'S';
                    }
                } else {
                    for (int i = 0; i < size; i++) {
                        tablero[x + i][y] = 'S';
                    }
                }
                aux = false;
            }
        } while (aux);
    }
    private static char orientacion() {
        //Returns the orientation in which we want to insert the ship.
        while (true) {
            try {
                System.out.println("En que orientación quieres colocar el barco? (V/H) ");
                char aux = Teclat.llegirChar();
                aux = Character.toLowerCase(aux);

                if (aux == 'v' || aux == 'h') {
                    return aux;
                } else {
                    System.out.println("Error: Debes ingresar una posición válida.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Debes ingresar una posición.");
            }
        }
    }
    private static int x() {
        //Return the coordinate X.
        while (true) {
            try {
                System.out.println("Dime la fila (0 - 9): ");
                int aux = Teclat.llegirInt();

                if (aux >= 0 && aux <= 9) {
                    return aux;
                } else {
                    System.out.println("Error: Debes ingresar un número válido.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Debes ingresar un número entero.");
            }
        }
    }
    private static int y() {
        //Return the coordinate Y.
        while (true) {
            try {
                System.out.println("Dime la columna (0 - 9): ");
                int aux = Teclat.llegirInt();

                if (aux >= 0 && aux <= 9) {
                    return aux;
                } else {
                    System.out.println("Error: Debes ingresar un número válido.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Debes ingresar un número entero.");
            }
        }
    }
    private static boolean finalGame(char[][] tablero) {
        //Return true if the game not ended
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++) {
                if (tablero[i][j] == 'S') return false;
            }
        return true;
    }
    private static void showTableroGame(char[][] tablero) {
        //Displays the console board without showing the ships.

        System.out.print("      ");
        for (int i = 0; i < 10; i++) {
            System.out.print(WHITE_BACKGROUND + BLACK + " " + i + " " + ANSI_RESET + "  ");
        }
        System.out.println("\n");

        for (int i = 0; i < 10; i++) {
            System.out.print(WHITE_BACKGROUND + BLACK + " " + (i) + " " + ANSI_RESET + "    ");
            for (int j = 0; j < 10; j++) {
                if (tablero[i][j] == 'S') {
                    System.out.print(ANSI_BLUE + "·    " + ANSI_RESET);
                } else if (tablero[i][j] == 'O') {
                    System.out.print(ANSI_YELLOW + "X    " + ANSI_RESET);
                } else {
                    System.out.print(ANSI_BLUE + "·    " + ANSI_RESET);
                }
            }
            System.out.println("\n");

        }
    }
    private static void showTablero(char[][] tablero) {
        //Displays the full console dashboard.

        System.out.print("      ");
        for (int i = 0; i < 10; i++) {
            System.out.print(WHITE_BACKGROUND + BLACK + " " + i + " " + ANSI_RESET + "  ");
        }
        System.out.println("\n");
        for (int i = 0; i < 10; i++) {
            System.out.print(WHITE_BACKGROUND + BLACK + " " + (i) + " " + ANSI_RESET + "    ");
            for (int j = 0; j < 10; j++) {
                if (tablero[i][j] == 'S') {
                    System.out.print(ANSI_GREEN + tablero[i][j] + "    " + ANSI_RESET);
                } else {
                    System.out.print(ANSI_BLUE + "·    " + ANSI_RESET);
                }
            }
            System.out.println("\n");

        }
    }
    private static char[][] crearTablero() {
        //Create a void board.

        return new char[][]{
                {'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A'},
                {'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A'},
                {'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A'},
                {'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A'},
                {'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A'},
                {'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A'},
                {'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A'},
                {'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A'},
                {'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A'},
                {'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A'},
        };
    }
    private static boolean exit() {
        //Function to terminate programme execution.
        for (int i = 0; i < 15; i++) {
            System.out.println(WHITE_BACKGROUND + "\n");
        }
        System.out.print(BLACK + ANSI_NEGRITA + "\n                                    ______                     __           __                     __\n");
        System.out.print("                                   / ____/  ____   ____   ____/ /          / /_    __  __  ___    / /\n");
        System.out.print("                                  / / __   / __ \\ / __ \\ / __  /          / __ \\  / / / / / _ \\  / / \n");
        System.out.print("                                 / /_/ /  / /_/ // /_/ // /_/ /          / /_/ / / /_/ / /  __/ /_/  \n");
        System.out.print("                                 \\____/   \\____/ \\____/ \\__,_/          /_.___/  \\__, /  \\___/ (_)   \n");
        System.out.print("                                                                                /____/               \n\n");

        return true;
    }
    private static void loadingScreen() {
        //Void function that shows a loading screen
        System.out.println("                                            Loading...");
        System.out.print("                                    ");
        for (int i = 0; i <= 24; i++) {
            System.out.print(ANSI_REVERSED + ANSI_NEGRITA + "*" + ANSI_RESET);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (int i = 0; i < 5; i++) {
            System.out.println("\n");
        }

        System.out.println(ANSI_GREEN + "                                       Loading Complete!\n\n\n\n" + ANSI_RESET);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private static boolean casillaOcupada(char[][] tablero, int x, int y, char orientacion, int size) {
        //Return true if can't put the ship, else false if can put the ship
        if (orientacion == 'h') {
            if (y > 10 - size) {
                return true;
            } else {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= size; j++) {
                        if (x + i >= 0 && y + j >= 0 && x + i < 10 && y + j < 10 && tablero[(x + i)][(y + j)] != 'A') {
                            return true;
                        }
                    }
                }
            }
        } else {
            if (x > 10 - size) {
                return true;
            } else {
                for (int i = -1; i <= size; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (x + i >= 0 && y + j >= 0 && x + i < 10 && y + j < 10 && tablero[(x + i)][(y + j)] != 'A') {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    private static int getMenuOption() {
        //Return the option switched by the user
        int menu;
        char move;

        menu = 1;
        do {
            limpiarPantalla();

            System.out.println("====================================================================================================");
            System.out.println("|| " + ANSI_RED + "  ____               _______   _______   _        ______    _____   _    _   _____   _____  " + ANSI_RESET + "   ||" + ANSI_RED);
            System.out.println(ANSI_RESET + "||" + ANSI_RED + "  |  _ \\      /\\     |__   __| |__   __| | |      |  ____|  / ____| | |  | | |_   _| |  __ \\ " + ANSI_RESET + "   ||" + ANSI_RED);
            System.out.println(ANSI_RESET + "||" + ANSI_RED + "  | |_) |    /  \\       | |       | |    | |      | |__    | (___   | |__| |   | |   | |__) |" + ANSI_RESET + "   ||" + ANSI_RED);
            System.out.println(ANSI_RESET + "||" + ANSI_RED + "  |  _ <    / /\\ \\      | |       | |    | |      |  __|    \\___ \\  |  __  |   | |   |  ___/ " + ANSI_RESET + "   ||" + ANSI_RED);
            System.out.println(ANSI_RESET + "||" + ANSI_RED + "  | |_) |  / ____ \\     | |       | |    | |____  | |____   ____) | | |  | |  _| |_  | |     " + ANSI_RESET + "   ||" + ANSI_RED);
            System.out.println(ANSI_RESET + "||" + ANSI_RED + "  |____/  /_/    \\_\\    |_|       |_|    |______| |______| |_____/  |_|  |_| |_____| |_|     " + ANSI_RESET + "   ||");
            System.out.println("====================================================================================================\n");


            switch (menu) {
                case 1:
                    System.out.println("                                        " + ANSI_YELLOW + "1.- " + ANSI_RESET + ANSI_NEGRITA + ANSI_REVERSED + " Play 1 vs 1 " + ANSI_RESET);
                    System.out.println("                                        " + ANSI_YELLOW + "2.-" + ANSI_RESET + " Play 1 vs IA");
                    System.out.println("                                        " + ANSI_YELLOW + "3.-" + ANSI_RESET + " Exit");
                    System.out.println("\n                                      (W) Up / (X) Down / (S) Select");
                    break;

                case 2:
                    System.out.println("                                        " + ANSI_YELLOW + "1.-" + ANSI_RESET + " Play 1 vs 1");
                    System.out.println("                                        " + ANSI_YELLOW + "2.- " + ANSI_RESET + ANSI_NEGRITA + ANSI_REVERSED + " Play 1 vs IA " + ANSI_RESET);
                    System.out.println("                                        " + ANSI_YELLOW + "3.-" + ANSI_RESET + " Exit");
                    System.out.println("\n                                      (W) Up / (X) Down / (S) Select");

                    break;

                case 3:
                    System.out.println("                                        " + ANSI_YELLOW + "1.-" + ANSI_RESET + " Play 1 vs 1");
                    System.out.println("                                        " + ANSI_YELLOW + "2.-" + ANSI_RESET + " Play 1 vs IA");
                    System.out.println("                                        " + ANSI_YELLOW + "3.- " + ANSI_RESET + ANSI_NEGRITA + ANSI_REVERSED + " Exit " + ANSI_RESET);
                    System.out.println("\n                                      (W) Up / (X) Down / (S) Select");

                    break;
            }

            try {

                move = Character.toLowerCase(Teclat.llegirChar());

                if (move == 'x') {
                    if (menu < 3) {
                        menu++;
                    }
                } else if (move == 'w') {
                    if (menu > 1) {
                        menu--;
                    }
                } else if (move == 's') {
                    return menu;
                } else {
                    System.out.println("Error: Debes ingresar una opción válida.");
                }

            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Debes ingresar una letra.");
            }
        } while (true);
    }
    private static boolean preguntarColocarBarcos(String name) {
        // Return true or false if the user wants to insert the ships automatically.
        char move;
        int option;

        option = 1;
        do {

            limpiarPantalla();

            System.out.println(name + " quieres colocar los barcos de forma aleatoria?");

            switch (option) {
                case 1:
                    System.out.println("          " + ANSI_NEGRITA + ANSI_REVERSED + " - Colocar barcos de forma automática " + ANSI_RESET);
                    System.out.println("          Colocar barcos de forma manual");
                    System.out.println("            (W) Up / (X) Down / (S) Select");
                    break;
                case 2:
                    System.out.println("          Colocar barcos de forma automática");
                    System.out.println("          " + ANSI_NEGRITA + ANSI_REVERSED + " - Colocar barcos de forma manual " + ANSI_RESET);
                    System.out.println("            (W) Up / (X) Down / (S) Select");
                    break;
            }

            try {

                move = Character.toLowerCase(Teclat.llegirChar());

                if (move == 'x') {
                    if (option < 2) {
                        option++;
                    }
                } else if (move == 'w') {
                    if (option > 1) {
                        option--;
                    }
                } else if (move == 's') {
                    return option == 1;
                } else {
                    System.out.println("Error: Debes ingresar una opción válida.");
                }

            } catch (java.util.InputMismatchException e) {
                System.out.println("Error: Debes ingresar una letra.");
            }
        } while (true);
    }
}