import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class HTMLValidatorGUI extends JFrame {
    private JTextField filePathField;
    private JButton analyzeButton;
    private JTextArea resultArea;
    private JTable tagsTable;
    private DefaultTableModel tableModel;
    private HTMLValidator validator;
    private HTMLTagCounter tagCounter;

    public HTMLValidatorGUI() {
        validator = new HTMLValidator();
        tagCounter = new HTMLTagCounter();

        setTitle("Valida HTML");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        filePathField = new JTextField();
        analyzeButton = new JButton("Análise");

        // Set preferred size for the analyze button
        Dimension buttonSize = new Dimension(70, 30);
        analyzeButton.setPreferredSize(buttonSize);
        analyzeButton.setMaximumSize(buttonSize);
        analyzeButton.setMinimumSize(buttonSize);

        // Add padding to the analyze button
        analyzeButton.setBorder(new EmptyBorder(0, 0, 0, 15));

        JLabel fileLabel = new JLabel("Arquivo:");
        fileLabel.setBorder(new EmptyBorder(0, 5, 0, 0)); // Add padding to the left of the label

        topPanel.add(fileLabel, BorderLayout.WEST);
        topPanel.add(filePathField, BorderLayout.CENTER);
        topPanel.add(analyzeButton, BorderLayout.EAST);

        resultArea = new JTextArea();
        resultArea.setEditable(false);

        String[] columnNames = {"Tag", "Número de ocorrências"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tagsTable = new JTable(tableModel);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(new JLabel("Tags e suas ocorrências:"), BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(tagsTable), BorderLayout.CENTER);

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
            String hTMLContent = new String(Files.readAllBytes(Paths.get(filePath)));
            if (validator.isValidHtml(hTMLContent)) {
                resultArea.setText("HTML bem formatado");
                updateTable(hTMLContent);
            } else {
                resultArea.setText("HTML mal formatado");
            }
        } catch (IOException e) {
            resultArea.setText("Erro na leitura do arquivo: " + e.getMessage());
        }
    }

    private void updateTable(String hTMLContent) {
        Map<String, Integer> tagFrequency = tagCounter.countTags(hTMLContent);

        tableModel.setRowCount(0);
        tagFrequency.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()}));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HTMLValidatorGUI().setVisible(true);
            }
        });
    }
}
