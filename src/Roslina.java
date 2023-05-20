import java.awt.*;
import java.util.List;



public abstract class Roslina extends Organizm {
    protected double prawdopodobienstwoRozprzestrzenienia;
    public Roslina(int x, int y, int sila, int inicjatywa, char typ, Swiat swiat) {
        super(x, y, sila, inicjatywa, typ, swiat);
        this.prawdopodobienstwoRozprzestrzenienia = 0.2;
    }

    @Override
    public void akcja() {

        if (czyRozprzestrzenic()) {
            List<Punkt> wolniSasiedzi = swiat.getWolniSasiedzi(this.getX(), this.getY());

            if (!wolniSasiedzi.isEmpty()) {
                int losowyIndeks = losujLiczbe(0, wolniSasiedzi.size() - 1);
                Punkt wybranePole = wolniSasiedzi.get(losowyIndeks);
                zasiejNowaRosline(wybranePole);
            }
        }
    }

    @Override
    public abstract Color getKolor();

    public boolean czyZjedzonaRoslina(Organizm organizm) {
        return true;
    }

    private boolean czyRozprzestrzenic() {
        return Math.random() < prawdopodobienstwoRozprzestrzenienia;
    }

    private void zasiejNowaRosline(Punkt pole) {
        Organizm nowaRoslina = swiat.stworzOrganizm(pole.getX(), pole.getY(), this.getTyp());
        swiat.dodajOrganizm(nowaRoslina);
        swiat.dodajLog(this.getNazwa() + " rozsial sie na pole " + pole.getX() + " " +  pole.getY());
    }

    private int losujLiczbe(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
    }
}
