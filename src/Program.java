package zad1;

import java.util.ArrayList;
import java.util.Random;

public class Program {
    private ArrayList<Instrukcja> instrukcje;

    public Program() {
       this.instrukcje = new ArrayList<Instrukcja>();
    }

    public Program(ArrayList<Instrukcja> inst) {
        this.instrukcje = new ArrayList<Instrukcja>(inst);
    }

    public Program(Program program) {
        this.instrukcje = new ArrayList<Instrukcja>(program.dajInstrukcje());
    }

    public int dlugoscProgramu() {
        return instrukcje.size();
    }

    public ArrayList<Instrukcja> dajInstrukcje() {
        return instrukcje;
    }

    public void dodajInstrukcje(Instrukcja instrukcja) {
        instrukcje.add(instrukcja);
    }

    public boolean wykonaj(Rob rob, Plansza plansza) {
        for (Instrukcja ins : instrukcje) {
            if (!rob.czyMaEnergie())
                return false;

            switch (ins.dajNazwe()) {
                case 'l': rob.obrocLewo();
                case 'p': rob.obrocPrawo();
                case 'i': rob.idz(plansza);
                case 'w': rob.wachaj(plansza);
                case 'j': rob.jedz(plansza);
            }

            rob.zabierzEnergie(1);
        }
        return rob.czyMaEnergie();
    }

    public Program mutujProgram(int prUsunieciaInstr, int prDodaniaInstr, int prZmianyInstr, ArrayList<Instrukcja> spisInstrukcji) {
        ArrayList<Instrukcja> kopia = new ArrayList<Instrukcja>(instrukcje);
        Random rand = new Random();

        if (rand.nextInt(100) < prUsunieciaInstr && kopia.size() != 0)
            kopia.remove(kopia.size() - 1);

        if (rand.nextInt(100) < prDodaniaInstr) {
            int indeksNowejInstrukcji = rand.nextInt(spisInstrukcji.size());
            kopia.add(new Instrukcja(spisInstrukcji.get(indeksNowejInstrukcji)));
        }

        if (rand.nextInt(100) < prZmianyInstr && kopia.size() != 0) {
            int indeksInstrukcjiDoZmiany = rand.nextInt(kopia.size());
            int indeksNowejInstrukcji = rand.nextInt(spisInstrukcji.size());
            kopia.set(indeksInstrukcjiDoZmiany, new Instrukcja(spisInstrukcji.get(indeksNowejInstrukcji)));
        }

        return new Program(kopia);
    }

    @Override
    public String toString() {
        if (instrukcje.size() == 0)
            return new String("Pusty program");
        StringBuilder str = new StringBuilder();
        for (Instrukcja ins : instrukcje)
            str.append(ins.dajNazwe()).append(" ");
        return str.toString();
    }

}
