import java.awt.*;

public class Wilk extends Zwierze {
    public Wilk(int x, int y, Swiat swiat) {
        super(x, y, 9, 5, 'W', swiat);
    }
    @Override
    public Color getKolor() {
        return Color.GRAY;
    }
}
