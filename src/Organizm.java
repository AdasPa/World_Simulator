import java.awt.Color;
public abstract class Organizm {
    protected int x;
    protected int y;
    protected int wiek;

    protected int tarcza;
    protected boolean exist;
    protected int sila;
    protected int inicjatywa;

    protected char typ;
    protected boolean wykonano;
    protected Swiat swiat;

    public Organizm(int x, int y, int sila, int inicjatywa, char typ, Swiat swiat) {
        this.x = x;
        this.y = y;
        this.sila = sila;
        this.inicjatywa = inicjatywa;
        this.wiek = 0;
        this.swiat = swiat;
        this.typ = typ;
        this.tarcza = -10;
        this.wykonano = false; ////TODO do każdego dodać wykonano, aby uniknąć podwójnego wykonywania akcji
        this.exist = true;

        swiat.dodajOrganizm(this);
    }

    public abstract void akcja();

    public void kolizja(Organizm napastnik){};

    public boolean czyZjedzonaRoslina(Organizm organizm){return false;};
    public abstract Color getKolor();

    public boolean czyZabija(Organizm organizm) {
        return false;
    }

    public char getTyp(){
        return typ;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSila() {
        return sila;
    }

    public void setSila(int sila) {
        this.sila = sila;
    }

    public int getInicjatywa() {
        return inicjatywa;
    }

    public void setInicjatywa(int inicjatywa) {
        this.inicjatywa = inicjatywa;
    }

    public Swiat getSwiat() {
        return swiat;
    }

    public void setSwiat(Swiat swiat) {
        this.swiat = swiat;
    }

    public String getNazwa() {
        char typ = getTyp();
        switch (typ) {
            case 'W':
                return "Wilk";
            case 'A':
                return "Antylopa";
            case 'Z':
                return "Zolw";
            case 'L':
                return "Lis";
            case 'O':
                return "Owca";
            case 'C':
                return "Czlowiek";
            case 'T':
                return "Trawa";
            case 'M':
                return "Mlecz";
            case 'G':
                return "Guarana";
            case 'J':
                return "Wilcze Jagody";
            case 'B':
                return "Barszcz Sosnowskiego";
            default:
                return "Nieznany";
        }
    }

    public boolean czyOdparlAtak(Organizm organizm) {
        return false;
    }
    public void setTarcza(int t)
    {
        this.tarcza = t;
    }

    public void setWykonano(boolean b)
    {
        this.wykonano = b;
    }

    public boolean getWykonano()
    {
        return this.wykonano;
    }
}
