package zad1;

public class PoleZywieniowe extends Pole {
    int jedzenie;
    int jedzeniePoOdnowieniu;
    int ileTurDoOdnowienia;
    int ileRosnieJedzenie;


    public PoleZywieniowe(int ileDajeJedzenia, int ileRosnieJedzenie) {
        super();
        jedzenie = ileDajeJedzenia;
        jedzeniePoOdnowieniu = ileDajeJedzenia;
        ileTurDoOdnowienia = 0;
        this.ileRosnieJedzenie = ileRosnieJedzenie;
    }

    public void aktualizujJedzenie() {
        if (!czyJestJedzenie()) {
            if (ileTurDoOdnowienia == 0)
                jedzenie = jedzeniePoOdnowieniu;
            else
                ileTurDoOdnowienia--;
        }
    }

    public boolean czyJestJedzenie() {
        return jedzenie > 0;
    }

    public int ileJedzenia() {
        return jedzenie;
    }

    public int ileZostaloDoOdnowienia() {
        return ileTurDoOdnowienia;
    }

    @Override
    public int oddajJedzenie() {
        int ile = jedzenie;
        jedzenie = 0;
        ileTurDoOdnowienia = ileRosnieJedzenie;
        return ile;
    }

    @Override
    public boolean czyPoleZywieniowe() {
        return true;
    }

}
