package zad1;
import java.lang.*;

public class Symulacja {

    private static void przeprowadzSymulacje(Parser parser, Plansza plansza) {
        plansza.rozstawRoby(parser.dajParametr("pocz_ile_robów"), parser.dajParametr("pocz_energia"), parser.poczProgram);
        int ileDoWypisania = parser.dajParametr("co_ile_wypisz");
        plansza.wypiszStanSymulacji();
        for (int i = 0; i < parser.dajParametr("ile_tur"); ++i) {
            plansza.aktualizujPolaZywieniowe();
            plansza.wykonajTure(parser.parametry, parser.spisInstrukcji);
            plansza.wypiszPodstawoweDane(i + 1);

            ileDoWypisania--;
            if (ileDoWypisania == 0) {
                plansza.wypiszStanSymulacji();
                ileDoWypisania = parser.dajParametr("co_ile_wypisz");
            }
        }
        plansza.wypiszStanSymulacji();
    }

    public static void main(String[] args) throws NiepoprawneDane {
	    if (args.length != 2) {
            System.out.println("Niepoprawna liczba parametrów programu");
            System.exit(1);
        }
	    Parser parser = new Parser(args[0], args[1]);
        parser.wczytajParametry();
        Plansza plansza = parser.wczytajPlansze();
	    przeprowadzSymulacje(parser, plansza);
    }
}
