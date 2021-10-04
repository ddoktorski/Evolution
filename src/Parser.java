package zad1;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Parser {
    private Scanner skanerPlanszy;
    private Scanner skanerParametry;
    public HashMap<String, Integer> parametry;
    public ArrayList<Instrukcja> spisInstrukcji;
    public Program poczProgram;

    public Parser(String nazwaPlikuPlanszy, String nazwaPlikuParametry) {
        spisInstrukcji = new ArrayList<Instrukcja>();
        parametry = new HashMap<String, Integer>();

        try {
            this.skanerPlanszy = new Scanner(new File(nazwaPlikuPlanszy));
        } catch (FileNotFoundException e) {
            System.err.println("Plik " + nazwaPlikuPlanszy + " nie został znaleziony");
        }

        try {
            this.skanerParametry = new Scanner(new File(nazwaPlikuParametry));
        } catch (FileNotFoundException e) {
            System.err.println("Plik " + nazwaPlikuParametry + " nie został znaleziony");
        }
    }

    public void sprawdzInstrukcje(char ins) throws NiepoprawneDane {
        if (ins != 'l' && ins != 'p' && ins != 'w' && ins != 'j' && ins != 'i')
            throw new NiepoprawneDane("Niepoprawna instrukcja");
    }

    // Zakładam, że wartości parametrów związanych z prawdopodobieństwem są podane w procentach
    // tzn. powinny przyjmować wartość całkowitą z przedziału [0, 100]
    // jeżeli jednak ta wartość jest większa niż 100 to program będzie działać tak jak to prawdopodobieństwo byłoby równe 1
    public void wczytajParametry() throws NiepoprawneDane{
        while (skanerParametry.hasNext()) {
            String nazwaParametru = skanerParametry.next();

            if (nazwaParametru.equals("spis_instr")) {
                String instrukcje = skanerParametry.next();
                for (char ins : instrukcje.toCharArray()) {
                    sprawdzInstrukcje(ins);
                    spisInstrukcji.add(new Instrukcja(ins));
                }
            }
            else if (nazwaParametru.equals("pocz_progr")) {
                poczProgram = new Program();
                String program = skanerParametry.next();
                for (char ins : program.toCharArray()) {
                    sprawdzInstrukcje(ins);
                    poczProgram.dodajInstrukcje(new Instrukcja(ins));
                }
            }
            else {
                int wartosc = skanerParametry.nextInt();
                if (wartosc < 0)
                    throw new NiepoprawneDane("Błąd parametru '" + nazwaParametru + "'");
                parametry.put(nazwaParametru, wartosc);
            }
        }
        skanerParametry.close();
    }

    public Plansza wczytajPlansze() throws NiepoprawneDane {
        int x = parametry.get("rozmiar_planszy_x");
        int y = parametry.get("rozmiar_planszy_y");
        Plansza plansza = new Plansza(x, y);

        for (int i = 0; i < x; ++i) {
            String linia = skanerPlanszy.nextLine();
            for (int j = 0; j < y; ++j) {
                if (linia.charAt(j) != ' ' && linia.charAt(j) != 'x')
                    throw new NiepoprawneDane("Niepoprawne pole");

                if (linia.charAt(j) == 'x')
                    plansza.wstawPoleZywieniowe(i, j, parametry.get("ile_daje_jedzenia"), parametry.get("ile_rośnie_jedzenie"));
                else
                    plansza.wstawPustePole(i, j);
            }
        }

        skanerPlanszy.close();
        return plansza;
    }

    public int dajParametr(String nazwa) {
        return parametry.get(nazwa);
    }
}
