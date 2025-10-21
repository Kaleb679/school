package edu.pg.in256;

import javax.swing.SwingUtilities;
public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SentimentModel model = new SentimentModel();
            MainFrame view = new MainFrame();
            new SentimentController(model, view);
            view.setVisible(true);
        });
    }
}