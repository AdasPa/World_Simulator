import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

import static java.lang.Math.min;

public class Hex extends Swiat{
    public Hex(int szerokosc, int wysokosc) {
        super(szerokosc, wysokosc);
    }

    @Override
    public void rysujSwiat() {


        JFrame frame = new JFrame("Heksagonalny świat - Adam Pacek 193318");
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, 1500, 1500);
                int cellWidth = 350/min(szerokosc, wysokosc);
                int cellHeight = 350/min(szerokosc, wysokosc);
                int offset = 0;

                for (int y = 0; y < wysokosc; y++) {
                    for (int x = 0; x < szerokosc; x++) {
                        Organizm organizm = plansza[x][y];

                        if (organizm != null) {
                            Color kolor = organizm.getKolor();
                            if (kolor != null) {
                                g.setColor(kolor);
                                System.out.println(organizm.getNazwa());
                                rysujHexagon(g, 50+x*cellWidth + offset, 50+y*cellHeight, cellWidth, cellHeight, kolor);
                            }
                        } else {
                            g.setColor(Color.WHITE);
                            rysujHexagon(g, 50+x*cellWidth + offset, 50+y*cellHeight, cellWidth, cellHeight, Color.WHITE);
                        }
                    }
                    offset += cellWidth/2;
                }
            }
        };
        gamePanel.setPreferredSize(new Dimension(500, 500));
        gamePanel.setFocusable(true);



        JPanel buttonLogPanel = new JPanel();
        buttonLogPanel.setLayout(new BoxLayout(buttonLogPanel, BoxLayout.Y_AXIS));

        JTextArea logArea = new JTextArea();
        logArea.setEditable(false);


        logArea.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));

        JButton nextTurnButton = new JButton("Następna tura");
        JButton saveButton = new JButton("Zapisz");
        JButton loadButton = new JButton("Wczytaj");

        nextTurnButton.setPreferredSize(new Dimension(150, 30));
        saveButton.setPreferredSize(new Dimension(100, 30));
        loadButton.setPreferredSize(new Dimension(100, 30));

        nextTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wykonajTure(-1);
                logArea.setText("WYDARZENIA NA SWIECIE\n");
                gamePanel.repaint();
                for(String log: logi)
                {
                    logArea.append(log + "\n");
                }
                czyscLogi();
            }
        });

        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                int cellWidth = 350 / Math.min(szerokosc, wysokosc);
                int cellHeight = 350 / Math.min(szerokosc, wysokosc);
                int offset = 0;

                for (int y = 0; y < wysokosc; y++) {
                    for (int x = 0; x < szerokosc; x++) {
                        int hexagonX = 50 + x * cellWidth + offset;
                        int hexagonY = 50 + y * cellHeight;

                        if (czyPunktWHexagonie(mouseX, mouseY, hexagonX, hexagonY, cellWidth, cellHeight)) {
                            String[] options = { "Wilk", "Lisa", "Owca", "Zółw", "Antylopa", "Trawa", "Mlecz", "Guarana", "Barszcz Sosnowskiego", "Jilcze Wagody" };

                            String selectedOption = (String) JOptionPane.showInputDialog(
                                    gamePanel,
                                    "Wybierz organizm:",
                                    "Dodawanie organizmu",
                                    JOptionPane.PLAIN_MESSAGE,
                                    null,
                                    options,
                                    options[0]
                            );

                            if (selectedOption != null) {
                                Organizm nowyOrganizm = stworzOrganizm(x, y, selectedOption.charAt(0));
                                dodajOrganizm(nowyOrganizm);

                                gamePanel.repaint();
                            }

                            return;
                        }
                    }
                    offset += cellWidth / 2;
                }
                gamePanel.requestFocusInWindow();
            }
        });

        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                int direction = -1;

                if (keyCode == KeyEvent.VK_D) {
                    direction = 0; // prawo
                } else if (keyCode == KeyEvent.VK_X) {
                    direction = 1; // prawo dół
                } else if (keyCode == KeyEvent.VK_Z) {
                    direction = 2; // lewo dół
                } else if (keyCode == KeyEvent.VK_A) {
                    direction = 3; // prawo
                } else if (keyCode == KeyEvent.VK_W) {
                    direction = 4; // lewo góra
                } else if (keyCode == KeyEvent.VK_E) {
                    direction = 5; // prawo góra
                }else if (keyCode == KeyEvent.VK_SPACE) {
                    direction = -2;
                } else if (e.getKeyCode() == KeyEvent.VK_L) {
                    wyswietlWyjasnienieKolorow();
                }

                if (direction != -1) {
                    wykonajTure(direction);
                    logArea.setText("WYDARZENIA NA SWIECIE\n");
                    gamePanel.repaint();
                    for (String log : logi) {
                        logArea.append(log + "\n");
                    }
                    czyscLogi();
                }
                gamePanel.requestFocusInWindow();
            }
        });
        saveButton.addActionListener(e -> {
            zapisz();
            gamePanel.requestFocusInWindow();
        });
        loadButton.addActionListener(e -> {
            wczytaj();
            gamePanel.requestFocusInWindow();
            gamePanel.repaint();
        });


        JScrollPane scrollPane = new JScrollPane(logArea);

        buttonLogPanel.add(nextTurnButton, BorderLayout.NORTH);
        buttonLogPanel.add(saveButton, BorderLayout.NORTH);
        buttonLogPanel.add(loadButton, BorderLayout.NORTH);

        buttonLogPanel.add(scrollPane, BorderLayout.SOUTH);

        mainPanel.setPreferredSize(new Dimension(750, 500));
        mainPanel.add(gamePanel, BorderLayout.WEST);
        mainPanel.add(buttonLogPanel, BorderLayout.EAST);

        frame.getRootPane().setDefaultButton(nextTurnButton);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        gamePanel.requestFocusInWindow();
        frame.setLocationRelativeTo(null);
        frame.pack();
    }

    private boolean czyPunktWHexagonie(int x, int y, int centerX, int centerY, int width, int height) {

        int relativeX = x - centerX;
        int relativeY = y - centerY;


        double scaledX = (double) relativeX / (width / 2);
        double scaledY = (double) relativeY / (height / 2);

        double distance = Math.sqrt(scaledX * scaledX + scaledY * scaledY);

        return distance <= 1.0;
    }
    private void rysujHexagon(Graphics g, int x, int y, int width, int height, Color color) {

        int l = (int)(width/2);

        int xPoints[] = new int[6];
        int yPoints[] = new int[6];

        for(int i = 0; i<6; i++)
        {
            double angle = 60*i - 30;
            double angle_r = Math.PI / 180 * angle;

            xPoints[i] = (int) (x + l * Math.cos(angle_r));
            yPoints[i] = (int) (y + l * Math.sin(angle_r));

        }

        g.setColor(color);
        g.fillPolygon(xPoints, yPoints, 6);
        g.setColor(Color.BLACK);
        g.drawPolygon(xPoints, yPoints, 6);
    }

    @Override
    public List<Punkt> getSasiedzi(int x, int y) {
        List<Punkt> sasiedzi = new ArrayList<>();

        if (czyLegalne(x+1, y))
            sasiedzi.add(new Punkt(x + 1, y)); // prawo

        if (czyLegalne(x, y + 1))
            sasiedzi.add(new Punkt(x, y + 1)); // prawo dół

        if (czyLegalne(x - 1, y + 1))
            sasiedzi.add(new Punkt(x - 1, y + 1)); // lewo dół

        if (czyLegalne(x - 1, y))
            sasiedzi.add(new Punkt(x - 1, y)); // lewo

        if (czyLegalne(x, y - 1))
            sasiedzi.add(new Punkt(x, y - 1)); // lewo góra

        if (czyLegalne(x + 1, y - 1))
            sasiedzi.add(new Punkt(x + 1, y - 1)); // prawo góra

        return sasiedzi;
    }

    @Override
    public List<Punkt> getDalsiSasiedzi(int x, int y) {
        List<Punkt> sasiedzi = new ArrayList<>();

        if (czyLegalne(x + 2, y))
            sasiedzi.add(new Punkt(x + 2, y)); // prawo

        if (czyLegalne(x, y + 2))
            sasiedzi.add(new Punkt(x, y + 2)); // prawo dół

        if (czyLegalne(x - 2, y + 2))
            sasiedzi.add(new Punkt(x - 2, y + 2)); // lewo dół

        if (czyLegalne(x - 2, y))
            sasiedzi.add(new Punkt(x - 2, y)); // lewo

        if (czyLegalne(x, y - 2))
            sasiedzi.add(new Punkt(x, y - 2)); // lewo góra

        if (czyLegalne(x + 2, y - 2))
            sasiedzi.add(new Punkt(x + 2, y - 2)); // prawo góra

        return sasiedzi;
    }
    @Override
    public Punkt generujPole(int x, int y) {
        int kierunek = getKierunek();

        switch (kierunek) {
            case 0: // prawo
                return new Punkt(x + 1, y);
            case 1: // prawo dół ---
                return new Punkt(x, y + 1);
            case 2: // lewo dół
                return new Punkt(x - 1, y + 1);
            case 3: // lewo
                return new Punkt(x - 1, y);
            case 4: // lewo góra ---
                return new Punkt(x, y - 1);
            case 5: // prawo góra
                return new Punkt(x + 1, y - 1);
            case -2:
                return new Punkt(-1, -1);
            default:
                return new Punkt(x, y);
        }
    }
}
