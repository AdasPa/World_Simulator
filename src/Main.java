///
/// Stworzone przez Adama Packa 17.05.2023
/// do użytku publicznego
///

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        String[] options = { "HeXagonalny", "Kwadratowy" };
        int choice = JOptionPane.showOptionDialog(null, "Wybierz układ świata:", "Wybór układu", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }

        int szerokosc = 0;
        int wysokosc = 0;

        while (szerokosc <= 0 || wysokosc <= 0) {
            try {
                szerokosc = Integer.parseInt(JOptionPane.showInputDialog("Podaj szerokość świata:"));
                wysokosc = Integer.parseInt(JOptionPane.showInputDialog("Podaj wysokość świata:"));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Nieprawidłowy format danych. Wprowadź poprawne liczby.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }

        Swiat s;
        if (choice == 0)
        {
           s = new Hex(szerokosc, wysokosc);
        }
        else
        {
            s = new Kwadrat(szerokosc, wysokosc);
        }


        Czlowiek c = new Czlowiek(0, 0, s);
        Wilk w1 = new Wilk(1, 1, s);
        Wilk w2 = new Wilk(1, 2, s);

        Antylopa a1 = new Antylopa(3, 4, s );
        Antylopa a2 = new Antylopa(5, 6, s );

        Lis l1 = new Lis(7, 8, s );
        Lis l2 = new Lis(9, 10, s );

        Owca o1 = new Owca(11, 12, s );
        Owca o2 = new Owca(13, 14, s );

        Guarana g1 = new Guarana(2, 3, s );
        Guarana g2 = new Guarana(4, 5, s );

        BarszczSosnowskiego b1 = new BarszczSosnowskiego(6, 7, s );
        BarszczSosnowskiego b2 = new BarszczSosnowskiego(8, 9, s );

        WilczeJagody wj1 = new WilczeJagody(10, 11, s );
        WilczeJagody wj2 = new WilczeJagody(12, 1, s );

        Mlecz m1 = new Mlecz(4, 10, s );
        Mlecz m2 = new Mlecz(6, 8, s );

        Trawa t1 = new Trawa(2, 6, s );
        Trawa t2 = new Trawa(7, 11, s );

        Zolw z1 = new Zolw(1, 7, s);
        Zolw z2 = new Zolw(1,8,s);
        Zolw z3 = new Zolw(1,9,s);

        s.rysujSwiat();
    }
}