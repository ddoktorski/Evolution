package zad1;

public class Instrukcja {
    private char nazwa;

    public Instrukcja(char nazwa) {
        this.nazwa = nazwa;
    }

    public Instrukcja(Instrukcja instr) {
        this.nazwa = instr.dajNazwe();
    }

    public char dajNazwe() {
        return nazwa;
    }
}
