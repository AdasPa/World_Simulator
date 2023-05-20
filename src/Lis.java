import java.awt.*;
import java.util.List;
import java.util.Random;

public class Lis extends Zwierze {
    public Lis(int x, int y,  Swiat swiat) {
        super(x, y, 3, 7, 'L', swiat);
    }

    @Override
    public void akcja() {
        List<Punkt> dostepnePola = swiat.getSasiedzi(x, y);
        if (!dostepnePola.isEmpty()) {
            Random random = new Random();
            int losowyIndex = random.nextInt(dostepnePola.size());
            Punkt nowePole = dostepnePola.get(losowyIndex);

            if(swiat.getSilaOrganizmu(nowePole.getX(), nowePole.getY()) <= this.getSila())
                swiat.wykonajRuch(this, x, y, nowePole.getX(), nowePole.getY());
            else
                swiat.dodajLog("Lis skorzystal z wechu i nie ruszyl sie na " + nowePole.getX() + " " + nowePole.getY());
        }
    }

    @Override
    public Color getKolor() {
        return new Color(200, 100, 0);
    }
}