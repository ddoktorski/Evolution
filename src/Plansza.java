package zad1;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Plansza {
    private Pole[][] pola;
    private ArrayList<Rob> wszystkieRoby;

    public Plansza(int x, int y) {
        pola = new Pole[x][y];
        wszystkieRoby = new ArrayList<>();
    }

    public void wstawPustePole(int x, int y) {
        pola[x][y] = new PolePuste();
    }

    public void wstawPoleZywieniowe(int x, int y, int ileDajeJedzenia, int ileRosnieJedzenie) {
        pola[x][y] = new PoleZywieniowe(ileDajeJedzenia, ileRosnieJedzenie);
    }

    // rozstawia roby na dowolnych miejscach na początku symulacji
    public void rozstawRoby(int ileRobow, int poczEnergia, Program poczProgram) {
        Random rand = new Random();
        for (int i = 0; i < ileRobow; ++i) {
            int x = rand.nextInt(liczbaRzedow());
            int y = rand.nextInt(liczbaKolumn());
            Rob nowyRob = new Rob(poczProgram, poczEnergia, rand.nextInt(4));
            nowyRob.ustawX(x);
            nowyRob.ustawY(y);
            pola[x][y].dodajRoba(nowyRob);
            wszystkieRoby.add(nowyRob);
        }
    }

    public int liczbaRzedow() {
        return pola.length;
    }

    public int liczbaKolumn() {
        return pola[0].length;
    }

    // indeksy liczone są modulo, ponieważ z treści zadania plansza jest zamknięta
    public int indeksRzad(int x) {
        if (x < 0)
            return ((x % liczbaRzedow()) + liczbaRzedow()) % liczbaRzedow();
        return x % liczbaRzedow();
    }

    public int indeksKolumna(int y) {
        if (y < 0)
            return ((y % liczbaKolumn()) + liczbaKolumn()) % liczbaKolumn();
        return y % liczbaKolumn();
    }

    public boolean czyPoleZywieniowe(int x, int y) {
        return pola[indeksRzad(x)][indeksKolumna(y)].czyPoleZywieniowe();
    }

    // przesuwa roba o jedno pole w kierunku, w którym jest zwrócony
    public void przesunRobaDoPrzodu(Rob rob) {
        int kierunek = rob.dajKierunek();
        int przesuniecie_x = (kierunek == 1 || kierunek == 3) ? rob.podajX() : indeksRzad(rob.podajX()) + ((kierunek == 0) ? 1 : -1);
        int przesuniecie_y = (kierunek == 0 || kierunek == 2) ? rob.podajY() : indeksKolumna(rob.podajY()) + ((kierunek == 1) ? 1 : -1);

        pola[rob.podajX()][rob.podajY()].usunRoba(rob);
        pola[indeksRzad(rob.podajX() + przesuniecie_x)][indeksKolumna(rob.podajY() + przesuniecie_y)].dodajRoba(rob);

        rob.ustawX(indeksRzad(rob.podajX() + przesuniecie_x));
        rob.ustawY(indeksKolumna(rob.podajY() + przesuniecie_y));
    }

    public void przeniesRobaNaPole(Rob rob, int x, int y) {
        pola[rob.podajX()][rob.podajY()].usunRoba(rob);
        pola[indeksRzad(x)][indeksKolumna(y)].dodajRoba(rob);
        rob.ustawX(indeksRzad(x));
        rob.ustawY(indeksKolumna(y));
    }

    public int zabierzJedzenieZpola(int x, int y) {
        return pola[x][y].oddajJedzenie();
    }

    // po każdej turze kontroluje czas do odnowienia pożywienia na polach żywieniowych
    public void aktualizujPolaZywieniowe() {
        for (int x = 0; x < liczbaRzedow(); ++x) {
            for (int y = 0; y < liczbaKolumn(); ++y) {
                if (pola[x][y].czyPoleZywieniowe())
                    ((PoleZywieniowe) pola[x][y]).aktualizujJedzenie();
            }
        }
    }

    public void wypiszPodstawoweDane(int numerTury) {
        int ileRobow = 0;
        int liczbaPolZjedzeniem = 0;
        int minDlugoscProgramu = Integer.MAX_VALUE, maxDlugoscProgramu = -1;
        int minEnergia = Integer.MAX_VALUE, maxEnergia = -1;
        float lacznaDlugoscProgramow = 0, lacznaEnergia = 0;
        int minWiek = Integer.MAX_VALUE, maxWiek = -1, lacznyWiek = 0;

        for (int x = 0; x < liczbaRzedow(); ++x) {
            for (int y = 0; y < liczbaKolumn(); ++y) {

                if (pola[x][y].czyPoleZywieniowe() && ((PoleZywieniowe) pola[x][y]).czyJestJedzenie())
                    liczbaPolZjedzeniem++;

                ArrayList<Rob> roby = pola[x][y].dajRoby();
                for (Rob rob : roby) {
                    ileRobow++;

                    minDlugoscProgramu = Math.min(minDlugoscProgramu, rob.dajDlugoscProgramu());
                    maxDlugoscProgramu = Math.max(maxDlugoscProgramu, rob.dajDlugoscProgramu());
                    lacznaDlugoscProgramow += rob.dajDlugoscProgramu();

                    minEnergia = Math.min(minEnergia, rob.ileEnergii());
                    maxEnergia = Math.max(maxEnergia, rob.ileEnergii());
                    lacznaEnergia += rob.ileEnergii();

                    minWiek = Math.min(minWiek, rob.dajWiek());
                    maxWiek = Math.max(maxWiek, rob.dajWiek());
                    lacznyWiek += rob.dajWiek();

                    if (rob.ileEnergii() < 0) {
                        System.out.println(rob.ileEnergii());
                    }

                    // przygotowujemy wszystkie roby na kolejną turę
                    rob.zmienWykonalTure(false);
                }
            }
        }
        DecimalFormat f = new DecimalFormat("##.00");

        if (ileRobow == 0) {
            System.out.println(numerTury + ", rob: 0, żyw: " + liczbaPolZjedzeniem + ", prg: 0/0/0, energ: 0/0/0, wiek: 0/0/0");
        }
        else {
            System.out.println(numerTury +
                    ", rob: " + ileRobow +
                    ", żyw: " + liczbaPolZjedzeniem +
                    ", prg: " + minDlugoscProgramu + "/" + f.format(lacznaDlugoscProgramow/ileRobow) + "/" + maxDlugoscProgramu +
                    ", energ: " + minEnergia + "/" + f.format(lacznaEnergia/ileRobow) + "/" + maxEnergia +
                    ", wiek: " + minWiek + "/" + f.format(lacznyWiek/ileRobow) + "/" + maxWiek);
        }
    }

    public void wykonajTure(HashMap<String, Integer> parametry, ArrayList<Instrukcja> spisInstrukcji) {
        ArrayList<Rob> doUsuniecia = new ArrayList<>();
        ArrayList<Rob> doDodania = new ArrayList<>();
        Rob rob;

        for (int i = 0; i < wszystkieRoby.size(); ++i) {
            rob = wszystkieRoby.get(i);
            if (!rob.czyWykonalTure()) {
                rob.zabierzEnergie(parametry.get("koszt_tury"));
                boolean czyWykonalProgram = rob.wykonajProgram(this);
                if (!czyWykonalProgram) {
                    doUsuniecia.add(rob);
                    pola[rob.podajX()][rob.podajY()].usunRoba(rob);
                }
                else {
                    Rob powielony = rob.sprobujPowielic(spisInstrukcji, parametry);
                    if (powielony != null) {
                        doDodania.add(powielony);
                        pola[powielony.podajX()][powielony.podajY()].dodajRoba(powielony);
                    }
                    rob.zmienWykonalTure(true);
                    rob.zwiekszWiek();
                }
            }
        }
        wszystkieRoby.removeAll(doUsuniecia);
        wszystkieRoby.addAll(doDodania);
        doUsuniecia.clear();
    }

    public void wypiszStanSymulacji() {
        System.out.println("--------------------------------------------------------------------");
        System.out.println("STAN SYMULACJI");
        System.out.println("Informacje o robach");
        for (Rob rob : wszystkieRoby) {
            System.out.println("pozycja roba: (" + rob.podajX() + "," + rob.podajY() + "), " +
                                "kierunek: " + rob.dajKierunekSlownie() +
                                ", wiek: " + rob.dajWiek() +
                                ", energia: " + rob.ileEnergii() +
                                ", program: " + rob.jakiProgram());
        }
        System.out.println();
        System.out.println("Pola z jedzeniem w trakcie odnawiania");
        for (int x = 0; x < liczbaRzedow(); ++x) {
            for (int y = 0; y < liczbaKolumn(); ++y) {
                if (pola[x][y].czyPoleZywieniowe() && !((PoleZywieniowe) pola[x][y]).czyJestJedzenie()) {
                    System.out.println("współrzędne: (" + x + "," + y + "), " +
                                     "ile tur do odnowienia: " + ((PoleZywieniowe) pola[x][y]).ileZostaloDoOdnowienia());

                }
            }
        }
        System.out.println("--------------------------------------------------------------------");
    }
}
