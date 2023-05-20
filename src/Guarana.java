import java.awt.*;

public class Guarana extends Roslina {
    public Guarana(int x, int y, Swiat swiat) {
        super(x, y,0,0, 'G', swiat);
    }
    @Override
    public boolean czyZjedzonaRoslina(Organizm organizm) {
        organizm.setSila(organizm.getSila()+3);
        return true;
    }

    @Override
    public Color getKolor() {
        return new Color(204, 0, 0);
    }
}