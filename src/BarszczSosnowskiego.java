import java.awt.*;
import java.util.List;

public class BarszczSosnowskiego extends Roslina {
    public BarszczSosnowskiego(int x, int y, Swiat swiat) {
        super(x, y, 10, 0, 'B', swiat);
    }

    @Override
    public void akcja() {

        List<Punkt> sasiedzi = swiat.getSasiedzi(this.getX(), this.getY());
        for (Punkt sasiad : sasiedzi) {
            Organizm o = swiat.getOrganizm(sasiad.getX(), sasiad.getY());
            if (o instanceof Zwierze) {
                swiat.usunOrganizm(o);
                swiat.dodajLog(this.getNazwa() + " zabil sasiadujacego " + o.getNazwa() + " na polu " + sasiad.getX() + " " + sasiad.getY());
            }
        }

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
    public Color getKolor() {
        return Color.magenta;
    }

    @Override
    public boolean czyZabija(Organizm organizm) {
        return true;
    }
    private boolean czyRozprzestrzenic() {
        return Math.random() < prawdopodobienstwoRozprzestrzenienia;
    }

    private int losujLiczbe(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
    }

    private void zasiejNowaRosline(Punkt pole) {
        Organizm nowaRoslina = swiat.stworzOrganizm(pole.getX(), pole.getY(), this.getTyp());
        swiat.dodajOrganizm(nowaRoslina);
        swiat.dodajLog(this.getNazwa() + " rozsial sie na pole " + pole.getX() + " " +  pole.getY());
    }

}