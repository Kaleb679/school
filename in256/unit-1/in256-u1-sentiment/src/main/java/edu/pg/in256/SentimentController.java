package edu.pg.in256;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;


public class SentimentController {
    private final SentimentModel model;
    private final MainFrame view;
    
    private File lastDirectory = new File(System.getProperty("user.dir"));

    public SentimentController(SentimentModel model, MainFrame view) {
        this.model = model;
        this.view = view;
        // Wire up event listeners
        view.browseButton.addActionListener(this::handleBrowse);
        view.analyzeButton.addActionListener(this::handleAnalyze);
    }

    //open the browser in the last directory or open the local project folder
    private void handleBrowse(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        if (lastDirectory == null || !lastDirectory.exists() || !lastDirectory.isDirectory()) {
            lastDirectory = new File(System.getProperty("user.dir"));
        }
        chooser.setCurrentDirectory(lastDirectory);
        
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));
        chooser.setDialogTitle("Select CSV File");


        if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            view.filePathField.setText(file.getAbsolutePath());
            view.appendOutput("Selected: " + file.getName());
        }
    }

    
    private void handleAnalyze(ActionEvent e) {
    String path = view.filePathField.getText().trim();
    if (path.isEmpty()) {
        JOptionPane.showMessageDialog(view, "Select a CSV file first!");
        return;
    }

    try {
        File file = new File(path);
        String results = model.analyzeSentiment(file);
        view.outputArea.setText(results);
    } catch (Exception ex) {
        view.outputArea.setText("Error: " + ex.getMessage());
    }
}
}
