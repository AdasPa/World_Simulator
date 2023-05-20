import java.awt.*;
import java.util.List;
import java.util.Random;

public abstract class Zwierze extends Organizm {
    public Zwierze(int x, int y, int sila, int inicjatywa, char typ, Swiat swiat) {
        super(x, y, sila, inicjatywa, typ, swiat);
    }

    @Override
    public void akcja() {
        List<Punkt> dostepnePola = swiat.getSasiedzi(x, y);
        if (!dostepnePola.isEmpty()) {
            Random random = new Random();
            int losowyIndex = random.nextInt(dostepnePola.size());
            Punkt nowePole = dostepnePola.get(losowyIndex);
            swiat.wykonajRuch(this, x, y, nowePole.getX(), nowePole.getY());
        }
    }
    @Override
    public abstract Color getKolor();

    @Override
    public void kolizja(Organizm napastnik)
    {
        if (this.getClass().equals(napastnik.getClass()))
        {

            int x = this.getX();
            int y = this.getY();

            List<Punkt> wolnePola = swiat.getWolniSasiedzi(x, y);

            if (!wolnePola.isEmpty())
            {
                Random random = new Random();
                Punkt pole = wolnePola.get(random.nextInt(wolnePola.size()));

                Organizm noweZwierze = swiat.stworzOrganizm(pole.getX(), pole.getY(), this.getTyp());
                noweZwierze.setX(pole.getX());
                noweZwierze.setY(pole.getY());
                swiat.dodajOrganizm(noweZwierze);

                swiat.dodajLog(this.getNazwa() + " rozmnozyl się! " + pole.getX() + " " + pole.getY());
            }
        }
        else
        {
            if (this.getSila() > napastnik.getSila())
            {
                if(napastnik.czyZjedzonaRoslina(this))
                {
                    if(napastnik.czyZabija(this))
                    {
                        this.swiat.dodajLog( this.getNazwa() + " schrupal " + napastnik.getNazwa() + " i zdechl na polu " + napastnik.getX() + " " + napastnik.getY());
                        this.swiat.usunOrganizm(napastnik);
                        this.swiat.usunOrganizm(this);
                        return;
                    }
                    else
                    {
                        this.swiat.dodajLog( this.getNazwa() + " schrupal " + napastnik.getNazwa() + " na polu " + napastnik.getX() + " " + napastnik.getY());
                        this.swiat.usunOrganizm(napastnik);
                        this.swiat.przesunOrganizm(this, napastnik.getX(), napastnik.getY());
                        return;
                    }
                }
                this.swiat.dodajLog( this.getNazwa() + " wygral z " + napastnik.getNazwa() + " na polu " + napastnik.getX() + " " + napastnik.getY());
                this.swiat.usunOrganizm(napastnik);
                this.swiat.przesunOrganizm(this, napastnik.getX(), napastnik.getY());


            }
            else
            {
                if(napastnik.czyZjedzonaRoslina(this))
                {
                    this.swiat.dodajLog( this.getNazwa() + " schrupal " + napastnik.getNazwa() + " i zdechl na polu " + napastnik.getX() + " " + napastnik.getY());
                    this.swiat.usunOrganizm(napastnik);
                    this.swiat.usunOrganizm(this);
                    return;
                }
                this.swiat.dodajLog( this.getNazwa() + " przegral z " + napastnik.getNazwa() + " na polu " + napastnik.getX() + " " + napastnik.getY());
                this.swiat.usunOrganizm(this);
            }
        }
    }
}
