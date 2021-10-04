package zad1;

public class PolePuste extends Pole{
    public PolePuste() {
        super();
    }

    @Override
    public int oddajJedzenie() {
        return 0;
    }

    @Override
    public boolean czyPoleZywieniowe() {
        return false;
    }
}
