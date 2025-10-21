package edu.pg.in256;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    JTextField filePathField;
    JTextArea outputArea;
    JButton browseButton;
    JButton analyzeButton;

    public MainFrame() {
        super("IN256 Sentiment Analyzer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 10));

        // file chooser
        JPanel top = new JPanel(new BorderLayout(5, 5));
        filePathField = new JTextField();
        browseButton = new JButton("Browse CSV");
        top.add(filePathField, BorderLayout.CENTER);
        top.add(browseButton, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        // text area/ center
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // button
        
        analyzeButton = new JButton("Analyze Sentiment");
        add(analyzeButton, BorderLayout.SOUTH);
    }

    public void appendOutput(String text) {
        outputArea.append(text + "\n");
    }
}