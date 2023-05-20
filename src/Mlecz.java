import java.awt.*;
import java.util.List;

public class Mlecz extends Roslina {

    public Mlecz(int x, int y, Swiat swiat) {
        super(x, y,0,  0, 'M', swiat);
    }

    @Override
    public void akcja() {
        for(int i = 0; i < 3; i++) {
            if (czyRozprzestrzenic()) {
                List<Punkt> wolniSasiedzi = swiat.getWolniSasiedzi(this.getX(), this.getY());

                if (!wolniSasiedzi.isEmpty()) {
                    int losowyIndeks = losujLiczbe(0, wolniSasiedzi.size() - 1);
                    Punkt wybranePole = wolniSasiedzi.get(losowyIndeks);
                    zasiejNowaRosline(wybranePole);
                }
            }
        }
    }

    @Override
    public Color getKolor() {
        return new Color(255, 255, 153);
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
