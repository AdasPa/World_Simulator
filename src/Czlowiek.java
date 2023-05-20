import java.awt.*;
public class Czlowiek extends Zwierze {

    public Czlowiek(int x, int y, Swiat swiat) {
        super(x, y, 5, 4, 'C', swiat);
    }

    @Override
    public Color getKolor() {
        return Color.BLUE;
    }

    @Override
    public void akcja() {
        Punkt nowePole = swiat.generujPole(x, y);

        System.out.println(x + " " + y);
        System.out.println(nowePole.getX() + " " + nowePole.getY());

        if(!swiat.exist)
            return;
        if(nowePole.getX() == -1 && nowePole.getY() == -1)
            aktywujTarcze();
        else if((nowePole.getX() != x || nowePole.getY() != y) && nowePole.getX() >=0 && nowePole.getY() >=0 )
            swiat.wykonajRuch(this, x, y, nowePole.getX(), nowePole.getY());

        tarcza--;

    }

    public void aktywujTarcze() {
        if(tarcza < -5) {
            tarcza = 5;
            swiat.dodajLog("Aktywowano tarcze ALZUUURA!");
        }
        else if(tarcza < 0) {
            swiat.dodajLog("Nie mozesz aktywowac tarczy ALZUUURA");
        }
        else {
            swiat.dodajLog("Tarcza ALZUUURA jest juz aktywna!");
        }
    }

    @Override
    public boolean czyOdparlAtak(Organizm organizm) {
        if(tarcza>=0) {
            swiat.dodajLog("Tarcza ALZUUUURA uzyta w obronie przed " + organizm.getNazwa() + " na " + organizm.getX() + " " + organizm.getY());
            return true;
        }
        return false;
    }
}