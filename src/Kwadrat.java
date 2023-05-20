import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.KeyEvent;
public class Kwadrat extends Swiat{
    public Kwadrat(int szerokosc, int wysokosc) {
        super(szerokosc, wysokosc);
    }

    @Override
    public void rysujSwiat() {


        JFrame frame = new JFrame("Kwadratowy świat - Adam Pacek 193318");
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, 500, 500);
                int cellWidth = 500/szerokosc;
                int cellHeight = 500/wysokosc;

                for (int x = 0; x < szerokosc; x++) {
                    for (int y = 0; y < wysokosc; y++) {
                        Organizm organizm = plansza[x][y];

                        if (organizm != null) {
                            Color kolor = organizm.getKolor();
                            if (kolor != null) {
                                g.setColor(kolor);
                                g.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
                            }
                        } else {
                            g.setColor(Color.WHITE);
                            g.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
                        }
                    }
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

                int cellWidth = gamePanel.getWidth() / szerokosc;
                int cellHeight = gamePanel.getHeight() / wysokosc;

                int clickedX = mouseX / cellWidth;
                int clickedY = mouseY / cellHeight;

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
                    Organizm nowyOrganizm = stworzOrganizm(clickedX, clickedY, selectedOption.charAt(0));
                    dodajOrganizm(nowyOrganizm);

                    gamePanel.repaint();
                }
                gamePanel.requestFocusInWindow();
            }
        });

        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                int direction = -1;



                if (keyCode == KeyEvent.VK_RIGHT) {
                    direction = 0; // prawo
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    direction = 1; // dół
                } else if (keyCode == KeyEvent.VK_LEFT) {
                    direction = 2; // lewo
                } else if (keyCode == KeyEvent.VK_UP) {
                    direction = 3; // góra
                } else if (keyCode == KeyEvent.VK_SPACE) {
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

    @Override
    public List<Punkt> getSasiedzi(int x, int y) {
        List<Punkt> sasiedzi = new ArrayList<>();

        if (czyLegalne(x, y - 1))
            sasiedzi.add(new Punkt(x, y - 1)); // Góra

        if (czyLegalne(x, y + 1))
            sasiedzi.add(new Punkt(x, y + 1)); // Dół

        if (czyLegalne(x - 1, y))
            sasiedzi.add(new Punkt(x - 1, y)); // Lewo

        if (czyLegalne(x + 1, y))
            sasiedzi.add(new Punkt(x + 1, y)); // Prawo

        return sasiedzi;
    }

    @Override
    public List<Punkt> getDalsiSasiedzi(int x, int y) {
        List<Punkt> sasiedzi = new ArrayList<>();

        if (czyLegalne(x, y - 2))
            sasiedzi.add(new Punkt(x, y - 2)); // Góra

        if (czyLegalne(x, y + 2))
            sasiedzi.add(new Punkt(x, y + 2)); // Dół

        if (czyLegalne(x - 2, y))
            sasiedzi.add(new Punkt(x - 2, y)); // Lewo

        if (czyLegalne(x + 2, y))
            sasiedzi.add(new Punkt(x + 2, y)); // Prawo

        return sasiedzi;
    }

    @Override
    public Punkt generujPole(int x, int y) {
        int kierunek = getKierunek();

        switch (kierunek) {
            case 0: // prawo
                return new Punkt(x + 1, y);
            case 1: // dół
                return new Punkt(x, y + 1);
            case 2: // lewo
                return new Punkt(x - 1, y);
            case 3: // góra
                return new Punkt(x, y - 1);
            case -2:
                return new Punkt(-1, -1);
            default:
                return new Punkt(x, y);
        }
    }
}
