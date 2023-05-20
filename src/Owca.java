import java.awt.*;

public class Owca extends Zwierze {
    public Owca(int x, int y,  Swiat swiat) {
        super(x, y, 4, 4, 'O', swiat);
    }
    @Override
    public Color getKolor() {
        return Color.LIGHT_GRAY;
    }

}