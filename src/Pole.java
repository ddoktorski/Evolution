package zad1;
import java.util.ArrayList;

public abstract class Pole {
    private ArrayList<Rob> roby;

    public Pole() {
        roby = new ArrayList<Rob>();
    }

    public abstract boolean czyPoleZywieniowe();

    public abstract int oddajJedzenie();

    public void dodajRoba(Rob rob) {
        roby.add(rob);
    }

    public void usunRoba(Rob rob) {
        roby.remove(rob);
    }

    public ArrayList<Rob> dajRoby() {
        return roby;
    }
}
