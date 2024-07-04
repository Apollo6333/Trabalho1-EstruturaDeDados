package Tentativa2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HtmlValidatorGUI extends JFrame {
    private JTextField filePathField;
    private JButton analyzeButton;
    private JTextArea resultArea;
    private JTable semanticTagTable;
    private HtmlValidator validator;

    public HtmlValidatorGUI() {
        validator = new HtmlValidator();

        setTitle("HTML Validator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        filePathField = new JTextField();
        analyzeButton = new JButton("Analyze");

        JLabel fileLabel = new JLabel("File:");
        fileLabel.setBorder(new EmptyBorder(0, 5, 0, 0)); // Add padding to the left of the label

        topPanel.add(fileLabel, BorderLayout.WEST);
        topPanel.add(filePathField, BorderLayout.CENTER);
        topPanel.add(analyzeButton, BorderLayout.EAST);

        resultArea = new JTextArea();
        resultArea.setEditable(false);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        semanticTagTable = new JTable(new DefaultTableModel(new Object[]{"Tag", "Número de ocorrências"}, 0));
        bottomPanel.add(new JScrollPane(semanticTagTable), BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        analyzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                analyzeFile();
            }
        });
    }

    private void analyzeFile() {
        String filePath = filePathField.getText();
        try {
            String htmlContent = new String(Files.readAllBytes(Paths.get(filePath)));
            ValidationResult validationResult = validator.validate(htmlContent);
            resultArea.setText(validationResult.getMessage());
            updateSemanticTagTable(validationResult.getSemanticTagCount());
        } catch (IOException e) {
            resultArea.setText("Error reading file: " + e.getMessage());
        }
    }

    private void updateSemanticTagTable(int[] semanticTagCount) {
        DefaultTableModel model = (DefaultTableModel) semanticTagTable.getModel();
        model.setRowCount(0); // Limpa as linhas existentes

        // Adiciona cada tag semântica à tabela
        for (int i = 0; i < HtmlValidator.SEMANTIC_TAGS.length; i++) {
            model.addRow(new Object[]{HtmlValidator.SEMANTIC_TAGS[i], semanticTagCount[i]});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HtmlValidatorGUI().setVisible(true);
            }
        });
    }
}
