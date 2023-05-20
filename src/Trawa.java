import java.awt.*;

public class Trawa extends Roslina {
    public Trawa(int x, int y, Swiat swiat) {
        super(x, y,0,  0, 'T', swiat);
    }

    @Override
    public Color getKolor() {
        return new Color(150, 255, 150);
    }
}