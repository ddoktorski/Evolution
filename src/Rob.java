package zad1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Rob {
    private int kierunek; // przyjmuje wartości ze zbioru {0,1,2,3}, które oznaczają odpowiednio góra, prawo, dół, lewo
    private int energia;
    private int wiek;
    private boolean wykonalTure; // informuje o tym, czy rob wykonał program w danej turze
    private Program program;

    // wspołrzędne pola, na których stoi rob
    private int x;
    private int y;

    // tablica pomocnicza
    final int[][] kierunki = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {1, 1}};

    public Rob(Program poczProgram, int poczEnergia, int kierunek) {
        energia = poczEnergia;
        program = new Program(poczProgram);
        this.kierunek = kierunek;
        wiek = 0;
        wykonalTure = false;
    }

    public boolean czyWykonalTure() {
        return wykonalTure;
    }

    public void zmienWykonalTure(boolean zamienNa) {
        wykonalTure = zamienNa;
    }

    public int ileEnergii() {
        return energia;
    }

    public int dajDlugoscProgramu() {
        return program.dlugoscProgramu();
    }

    public int dajWiek() {
        return wiek;
    }

    public int dajKierunek() {
        return kierunek;
    }

    public String dajKierunekSlownie() {
        switch (kierunek) {
            case 0: return "góra";
            case 1: return "prawo";
            case 2: return "dół";
            case 3: return "lewo";
        }
        return "";
    }

    public String jakiProgram() {
        return program.toString();
    }

    public int podajX() {
        return x;
    }

    public int podajY() {
        return y;
    }

    public void ustawX(int x) {
        this.x = x;
    }

    public void ustawY(int y) {
        this.y = y;
    }

    public void zwiekszWiek() {
        wiek++;
    }

    public void obrocLewo() {
        kierunek -= 1;
        kierunek %= 4;
    }

    public void obrocPrawo() {
        kierunek += 1;
        kierunek %= 4;
    }

    public void wachaj(Plansza plansza) {
        for (int i = 0; i < 4; ++i) {
            if (plansza.czyPoleZywieniowe(plansza.indeksRzad(x + kierunki[i][0]), plansza.indeksKolumna(y + kierunki[i][1]))) {
                if (kierunki[i][0] == 0)
                    kierunek = (kierunki[i][1] == 1) ? 1 : 3;
                else
                    kierunek = (kierunki[i][0] == 1) ? 0 : 2;
                break;
            }
        }
    }

    public void idz(Plansza plansza) {
        plansza.przesunRobaDoPrzodu(this);
        zjedz(plansza);
    }

    public void jedz(Plansza plansza) {
        for (int[] k : kierunki) {
            if (plansza.czyPoleZywieniowe(x + k[0], y + k[1])) {
                plansza.przeniesRobaNaPole(this, x + k[0], y + k[1]);
                zjedz(plansza);
                break;
            }
        }
    }

    public void zjedz(Plansza plansza) {
        energia += plansza.zabierzJedzenieZpola(x, y);
    }

    public void zabierzEnergie(int ile) {
        energia -= ile;
    }

    public boolean czyMaEnergie() {
        return energia > 0;
    }

    public boolean wykonajProgram(Plansza plansza) {
        if (energia <= 0)
            return false;
        return program.wykonaj(this, plansza);
    }

    public Rob sprobujPowielic(ArrayList<Instrukcja> spisInstrukcji, HashMap<String, Integer> parametry) {
        Random rand = new Random();

        if (energia >= parametry.get("limit_powielania") && rand.nextInt(100) < parametry.get("pr_powielenia"))
            return powielRoba(spisInstrukcji, parametry);
        else
            return null;
    }

    public Rob powielRoba(ArrayList<Instrukcja> spisInstrukcji, HashMap<String, Integer> parametry) {
        Program zmutowanyProgram = program.mutujProgram(parametry.get("pr_usunięcia_instr"),
                                                        parametry.get("pr_dodania_instr"),
                                                        parametry.get("pr_zmiany_instr"),
                                                        spisInstrukcji);
        zabierzEnergie(parametry.get("ułamek_energii_rodzica"));
        Rob nowyRob = new Rob(zmutowanyProgram, parametry.get("ułamek_energii_rodzica"), (kierunek + 2) % 4);
        nowyRob.ustawX(this.x);
        nowyRob.ustawY(this.y);
        nowyRob.zmienWykonalTure(true);
        return nowyRob;
    }
}
