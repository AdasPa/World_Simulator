import java.awt.*;
import java.util.List;
import java.util.Random;

public class Zolw extends Zwierze {
    private double prawdopodobienstwoRuchu;
    public Zolw(int x, int y, Swiat swiat) {
        super(x, y, 2, 1, 'Z', swiat);
        this.prawdopodobienstwoRuchu = 0.25;
    }

    @Override
    public Color getKolor() {
        return new Color(10, 255, 50);
    }

    @Override
    public void akcja() {
        if (!czyRuszycSie())
                return;
        List<Punkt> dostepnePola = swiat.getSasiedzi(x, y);
        if (!dostepnePola.isEmpty()) {
            Random random = new Random();
            int losowyIndex = random.nextInt(dostepnePola.size());
            Punkt nowePole = dostepnePola.get(losowyIndex);
            swiat.wykonajRuch(this, x, y, nowePole.getX(), nowePole.getY());
        }
    }


    private boolean czyRuszycSie() {
        return Math.random() < prawdopodobienstwoRuchu;
    }

    @Override
    public boolean czyOdparlAtak(Organizm organizm) {
        if(organizm.getSila()<5)
            return true;
        return false;
    }
}