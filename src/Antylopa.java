import java.awt.*;
import java.util.List;
import java.util.Random;

public class Antylopa extends Zwierze {
    private double prawdopodobienstwoUcieczki;
    public Antylopa(int x, int y, Swiat swiat) {
        super(x, y, 4, 4, 'A', swiat);
        this.prawdopodobienstwoUcieczki = 0.5;
    }

    @Override
    public Color getKolor() {
        return new Color(200, 200, 0);
    }

    @Override
    public void akcja() {
        List<Punkt> dostepnePola = swiat.getDalsiSasiedzi(x, y);
        if (!dostepnePola.isEmpty()) {
            Random random = new Random();
            int losowyIndex = random.nextInt(dostepnePola.size());
            Punkt nowePole = dostepnePola.get(losowyIndex);
            swiat.wykonajRuch(this, x, y, nowePole.getX(), nowePole.getY());
        }
    }

    @Override
    public void kolizja(Organizm napastnik) {
        if (this.getClass().equals(napastnik.getClass())) {

            int x = this.getX();
            int y = this.getY();

            List<Punkt> wolnePola = swiat.getWolniSasiedzi(x, y);

            if (!wolnePola.isEmpty()) {
                Random random = new Random();
                Punkt pole = wolnePola.get(random.nextInt(wolnePola.size()));

                Organizm noweZwierze = swiat.stworzOrganizm(pole.getX(), pole.getY(), this.getTyp());
                noweZwierze.setX(pole.getX());
                noweZwierze.setY(pole.getY());
                swiat.dodajOrganizm(noweZwierze);

                swiat.dodajLog(this.getNazwa() + " rozmnozyl sie! " + pole.getX() + " " + pole.getY());
            }
        } else {
            if(czyUciekac()){
                List<Punkt> wolnePola = swiat.getWolniSasiedzi(x, y);
                if(!wolnePola.isEmpty()) {
                    Random random = new Random();
                    Punkt wolnePole = wolnePola.get(random.nextInt(wolnePola.size()));
                    this.swiat.przesunOrganizm(this, wolnePole.getX(), wolnePole.getY());
                    this.swiat.dodajLog("Antylopa uciekla przed walka na " + wolnePole.getX() + " " + wolnePole.getY());
                    return;
                }
            }

            if (this.getSila() > napastnik.getSila())
            {
                if(napastnik.czyZjedzonaRoslina(this))
                {
                    if(napastnik.czyZabija(this))
                    {
                        swiat.dodajLog( this.getNazwa() + " schrupal " + napastnik.getNazwa() + " i zdechl na polu " + napastnik.getX() + " " + napastnik.getY());
                        swiat.usunOrganizm(napastnik);
                        swiat.usunOrganizm(this);
                        return;
                    }
                    else
                    {
                        swiat.dodajLog( this.getNazwa() + " schrupal " + napastnik.getNazwa() + " na polu " + napastnik.getX() + " " + napastnik.getY());
                        swiat.usunOrganizm(napastnik);
                        this.swiat.przesunOrganizm(this, napastnik.getX(), napastnik.getY());

                        return;
                    }
                }
                swiat.dodajLog( this.getNazwa() + " wygral z " + napastnik.getNazwa() + " na polu " + napastnik.getX() + " " + napastnik.getY());
                this.swiat.usunOrganizm(napastnik);
                this.swiat.przesunOrganizm(this, napastnik.getX(), napastnik.getY());


            }
            else
            {
                if(napastnik.czyZjedzonaRoslina(this))
                {
                    swiat.dodajLog( this.getNazwa() + " schrupal " + napastnik.getNazwa() + " i zdechl na polu " + napastnik.getX() + " " + napastnik.getY());
                    swiat.usunOrganizm(napastnik);
                    swiat.usunOrganizm(this);
                    return;
                }
                swiat.dodajLog( this.getNazwa() + " przegral z " + napastnik.getNazwa() + " na polu " + napastnik.getX() + " " + napastnik.getY());
                this.swiat.usunOrganizm(this);
            }
        }
    }

    private boolean czyUciekac() {
        return Math.random() < prawdopodobienstwoUcieczki;
    }
}