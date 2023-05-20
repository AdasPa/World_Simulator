import java.awt.*;

public class WilczeJagody extends Roslina {
    public WilczeJagody(int x, int y, Swiat swiat) {
        super(x, y, 99, 0, 'J', swiat);
    }
    public boolean czyZabija(Organizm organizm) {
        return true;
    }

    @Override
    public Color getKolor() {
        return new Color(102, 0, 102);
    }
}