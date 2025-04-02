import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class bilder {
    private JButton prevBtn;
    private JPanel mainPanel;
    private JButton nextBtn;
    private JLabel picLabel;
    private JTextField inputtxt;
    private JButton sucheButton;
    private JLabel wortAnzeigen;

    private int picCounter = 1;
    private final int MAX_FEHLER = 10;
    private String lösungsWortPrivate;
    private char[] aktuellerStand;

    public bilder() {
        generiereWort();
        wortAnzeigen.setText(String.valueOf(aktuellerStand));

        setHangmanBild(picCounter);

        prevBtn.addActionListener(new ImageChangeBtnClick(-1));
        nextBtn.addActionListener(new ImageChangeBtnClick(1));

        sucheButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prüfeBuchstabe();
            }
        });
    }

    private void setHangmanBild(int nummer) {
        String pfad = "hangman/hangman" + nummer + ".png";
        ImageIcon icon = new ImageIcon(pfad);

        if (icon.getIconWidth() == -1) {
            System.out.println("Bild nicht gefunden: " + pfad);
            return;
        }

        Image image = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        picLabel.setIcon(new ImageIcon(image));
    }

    private class ImageChangeBtnClick implements ActionListener {
        private final int direction;

        public ImageChangeBtnClick(int direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            picCounter += direction;
            if (picCounter < 1) picCounter = 10;
            else if (picCounter > 10) picCounter = 1;

            setHangmanBild(picCounter);
        }
    }

    private void generiereWort() {
        ArrayList<String> woerter = new ArrayList<>();
        woerter.add("Apfel");
        woerter.add("Banane");
        woerter.add("Kirsche");
        woerter.add("Mango");
        woerter.add("Orange");
        woerter.add("Kiwi");
        woerter.add("Melone");
        woerter.add("Zitrone");

        Random rand = new Random();
        lösungsWortPrivate = woerter.get(rand.nextInt(woerter.size())).toUpperCase();

        aktuellerStand = new char[lösungsWortPrivate.length()];
        for (int i = 0; i < aktuellerStand.length; i++) {
            aktuellerStand[i] = '_';
        }
    }

    private void prüfeBuchstabe() {
        String eingabe = inputtxt.getText().toUpperCase().trim();
        if (eingabe.length() != 1) {
            JOptionPane.showMessageDialog(mainPanel, "Bitte einen einzelnen Buchstaben eingeben!");
            return;
        }

        char buchstabe = eingabe.charAt(0);
        boolean gefunden = false;

        for (int i = 0; i < lösungsWortPrivate.length(); i++) {
            if (lösungsWortPrivate.charAt(i) == buchstabe) {
                aktuellerStand[i] = buchstabe;  // Buchstabe aufdecken
                gefunden = true;
            }
        }

        wortAnzeigen.setText(String.valueOf(aktuellerStand));

        if (!gefunden) {
            picCounter++;
            if (picCounter >= MAX_FEHLER) {
                JOptionPane.showMessageDialog(mainPanel, "Spiel vorbei! Das Wort war: " + lösungsWortPrivate);
                resetSpiel();
                return;
            }
            setHangmanBild(picCounter);
        }

        if (String.valueOf(aktuellerStand).equals(lösungsWortPrivate)) {
            JOptionPane.showMessageDialog(mainPanel, "Glückwunsch! Du hast das Wort erraten: " + lösungsWortPrivate);
            resetSpiel();
        }

        inputtxt.setText("");
    }

    private void resetSpiel() {
        picCounter = 1;
        generiereWort();
        wortAnzeigen.setText(String.valueOf(aktuellerStand));
        setHangmanBild(picCounter);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Hangman Bilder");
            frame.setContentPane(new bilder().mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
