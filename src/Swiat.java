import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


public abstract class Swiat extends JPanel {
    protected Organizm[][] plansza;
    protected int kierunek;
    protected int szerokosc;
    protected int wysokosc;

    protected List<String> logi;

    protected List<Organizm> organizmy;

    public Swiat(int szerokosc, int wysokosc) {
        this.szerokosc = szerokosc;
        this.wysokosc = wysokosc;
        this.plansza = new Organizm[szerokosc][wysokosc];
        this.logi = new ArrayList<>();
        this.organizmy = new ArrayList<>();
        this.kierunek = -1;
        this.exist = true;

        setLayout(new GridLayout(szerokosc, wysokosc));
        setPreferredSize(new Dimension(szerokosc * 50, wysokosc * 50));
    }

    public abstract void rysujSwiat();

    public abstract List<Punkt> getSasiedzi(int x, int y);
    public abstract List<Punkt> getDalsiSasiedzi(int x, int y);

    public abstract Punkt generujPole(int x, int y);

    public void wykonajTure(int kierunek) {
        this.kierunek = kierunek;
        Collections.sort(organizmy, new Comparator<Organizm>() {
            @Override
            public int compare(Organizm o1, Organizm o2) {
                return Integer.compare(o2.getInicjatywa(), o1.getInicjatywa());
            }
        });

        int s = organizmy.size();
        Organizm o;
        for (int i = 0; i<s; i++) {
            if(i>=organizmy.size())
                break;
            o = organizmy.get(i);
            if(o != null) {
                if (!o.getWykonano() && o.exist)
                {
                    o.akcja();
                    o.setWykonano(true);
                }
            }
        }

        for (Organizm org : organizmy) {
            if (org != null && org.getWykonano()) {
                org.setWykonano(false);
            }
        }
    }

    public Organizm getOrganizm(int x, int y) {
        if (czyLegalne(x, y)) {
            if (plansza[x][y] instanceof Organizm)
                return plansza[x][y];
            return null;
        }
        return null;
    }
    public List<Punkt> getWolniSasiedzi(int x, int y) {
        List<Punkt> wolniSasiedzi = new ArrayList<>();

        List<Punkt> sasiedzi = getSasiedzi(x, y);
        for (Punkt sasiad : sasiedzi) {
            if (czyWolnePole(sasiad.getX(), sasiad.getY())) {
                wolniSasiedzi.add(sasiad);
            }
        }
        return wolniSasiedzi;
    }


    public boolean czyLegalne(int x, int y) {
        if (x<0)
            return false;
        if (y<0)
            return false;
        if (x>=szerokosc)
            return false;
        if (y>=wysokosc)
            return false;
        return true;
    }

    public boolean czyWolnePole(int x, int y) {
        if (x >= 0 && x < szerokosc && y >= 0 && y < wysokosc && plansza[x][y] == null) {
            return true;
        }
        return false;
    }

    public boolean exist;

    public void dodajOrganizm(Organizm organizm) {
        if(!czyLegalne(organizm.getX(), organizm.getY()))
            return;
        plansza[organizm.getX()][organizm.getY()] = organizm;
        organizmy.add(organizm);
    }

    public void wykonajRuch(Zwierze zwierze, int aktualnyX, int aktualnyY, int nowyX, int nowyY) {
        Organizm organizm = getOrganizm(aktualnyX, aktualnyY);
        Organizm napastnik = getOrganizm(nowyX, nowyY);
        if(organizm == null)
            return;

        if (napastnik == null)
        {
            plansza[aktualnyX][aktualnyY] = null;
            plansza[nowyX][nowyY] = organizm;
            organizm.setX(nowyX);
            organizm.setY(nowyY);
        }
        else
        {
            if(napastnik.czyOdparlAtak(organizm))
                dodajLog(napastnik.getNazwa() + " odparl atak " + organizm.getNazwa() + " na " + napastnik.getX() + " " + napastnik.getY());
            else
                organizm.kolizja(napastnik);
        }
    }

    public void usunOrganizm(Organizm organizm) {
        int x = organizm.getX();
        int y = organizm.getY();
        plansza[x][y] = null;
        this.exist = (organizm instanceof Czlowiek) ? false : this.exist;
        organizm.exist = false;
        organizmy.remove(organizm);

    }

    public void przesunOrganizm(Organizm organizm, int nowyX, int nowyY) {
        int staryX = organizm.getX();
        int staryY = organizm.getY();

        plansza[staryX][staryY] = null;
        plansza[nowyX][nowyY] = organizm;
        organizm.setX(nowyX);
        organizm.setY(nowyY);
    }

    public Organizm stworzOrganizm(int x, int y, char typ) {
        Organizm nowyOrganizm = null;
        switch (typ) {
            case 'W':
                nowyOrganizm = new Wilk(x, y, this);
                break;
            case 'Z':
                nowyOrganizm = new Zolw(x, y, this);
                break;
            case 'A':
                nowyOrganizm = new Antylopa(x, y, this);
                break;
            case 'L':
                nowyOrganizm = new Lis(x, y, this);
                break;
            case 'O':
                nowyOrganizm = new Owca(x, y, this);
                break;
            case 'C':
                nowyOrganizm = new Czlowiek(x, y, this);
                break;
            case 'T':
                nowyOrganizm = new Trawa(x, y, this);
                break;
            case 'M':
                nowyOrganizm = new Mlecz(x, y, this);
                break;
            case 'G':
                nowyOrganizm = new Guarana(x, y, this);
                break;
            case 'J':
                nowyOrganizm = new WilczeJagody(x, y, this);
                break;
            case 'B':
                nowyOrganizm = new BarszczSosnowskiego(x, y, this);
                break;
        }

        return nowyOrganizm;
    }


    public int getSilaOrganizmu(int x, int y){

        if(!czyLegalne(x, y))
            return -1;
        if (getOrganizm(x, y) == null)
            return -1;
        return getOrganizm(x, y).getSila();
    }

    public void dodajLog(String log) {
        logi.add(log);
    }
    public void czyscLogi() {
        logi.clear();
    }
    public int getKierunek(){return this.kierunek;}

    public void zapisz() {

        String nazwaPliku = JOptionPane.showInputDialog("Podaj nazwę pliku:");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nazwaPliku))) {

            writer.write(String.valueOf(szerokosc));
            writer.newLine();
            writer.write(String.valueOf(wysokosc));
            writer.newLine();
            for (int y = 0; y < wysokosc; y++) {
                for (int x = 0; x < szerokosc; x++) {
                    Organizm organizm = plansza[x][y];
                    if(organizm == null)
                        continue;
                    writer.write(String.valueOf(organizm.getTyp()));
                    writer.write(" ");
                    writer.write(String.valueOf(organizm.getX()));
                    writer.write(" ");
                    writer.write(String.valueOf(organizm.getY()));
                    writer.write(" ");
                    writer.write(String.valueOf(organizm.getSila()));
                    writer.write(" ");
                    writer.write(String.valueOf(organizm.getInicjatywa()));
                    writer.write(" ");
                    writer.write(String.valueOf(organizm.tarcza));
                    writer.write("\n");

                }
            }

            System.out.println("Rozgrywka została zapisana do pliku: " + nazwaPliku);
        } catch (IOException e) {
            System.err.println("Błąd podczas zapisu do pliku: " + e.getMessage());
        }
    }
    public void wczytaj() {
        String nazwaPliku = JOptionPane.showInputDialog("Podaj nazwę pliku do wczytania:");

        try (BufferedReader reader = new BufferedReader(new FileReader(nazwaPliku))) {

            szerokosc = Integer.parseInt(reader.readLine());
            wysokosc = Integer.parseInt(reader.readLine());

            plansza = new Organizm[szerokosc][wysokosc];
            organizmy.clear();

            String linia;
            while ((linia = reader.readLine()) != null) {
                String[] daneOrganizmu = linia.split(" ");
                if (daneOrganizmu.length != 6) {
                    System.err.println("Błędny format danych w pliku.");
                    return;
                }

                String typ = daneOrganizmu[0];
                int x = Integer.parseInt(daneOrganizmu[1]);
                int y = Integer.parseInt(daneOrganizmu[2]);
                int sila = Integer.parseInt(daneOrganizmu[3]);
                int inicjatywa = Integer.parseInt(daneOrganizmu[4]);
                int tarcza = Integer.parseInt(daneOrganizmu[5]);

                Organizm organizm = stworzOrganizm(x, y, typ.charAt(0));
                if (organizm instanceof Czlowiek) this.exist = true;
                organizm.setSila(sila);
                organizm.setInicjatywa(inicjatywa);
                organizm.setTarcza(tarcza);
                plansza[x][y] = organizm;
                organizmy.add(organizm);
            }

            System.out.println("Rozgrywka została wczytana z pliku: " + nazwaPliku);
        } catch (IOException e) {
            System.err.println("Błąd podczas wczytywania pliku: " + e.getMessage());
        }
    }
    private static final Map<String, String> kolorOrganizmu = new HashMap<>();

    static {
        kolorOrganizmu.put("Wilk", "Szary");
        kolorOrganizmu.put("Owca", "Jasnoszary");
        kolorOrganizmu.put("Lis", "Pomaranczowy");
        kolorOrganizmu.put("Żółw", "Zielony");
        kolorOrganizmu.put("Antylopa", "Zolty");
        kolorOrganizmu.put("Trawa", "Jasnozielony");
        kolorOrganizmu.put("Mlecz", "Jasnozolty");
        kolorOrganizmu.put("Guarana", "Czerwony");
        kolorOrganizmu.put("Barszcz Sosnowskiego", "Magenta");
        kolorOrganizmu.put("Wilcze Jagody", "Fioletowy");
    }
    protected void wyswietlWyjasnienieKolorow() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : kolorOrganizmu.entrySet()) {
            String organizm = entry.getKey();
            String kolor = entry.getValue();
            sb.append(organizm).append(": ").append(kolor).append("\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString(), "Wyjaśnienie kolorów", JOptionPane.INFORMATION_MESSAGE);
    }

}