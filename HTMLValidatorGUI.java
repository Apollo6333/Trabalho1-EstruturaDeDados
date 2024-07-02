import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLValidatorGUI extends JFrame {
    private JTextField filePathField;
    private JButton analyzeButton;
    private JTextArea resultArea;
    private JTable tagsTable;
    private DefaultTableModel tableModel;
    private static final Set<String> SINGLETON_TAGS = new HashSet<>(Arrays.asList(
        "meta", "base", "br", "col", "command", "embed", "hr", "img", "input", "link", "param", "source", "!DOCTYPE"
    ));
    private static final Pattern TAG_PATTERN = Pattern.compile("<\\s*([^\\s>]+)[^>]*>");

    public HTMLValidatorGUI() {
        setTitle("HTML Validator");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        filePathField = new JTextField();
        analyzeButton = new JButton("Analyze");
        topPanel.add(new JLabel("File:"), BorderLayout.WEST);
        topPanel.add(filePathField, BorderLayout.CENTER);
        topPanel.add(analyzeButton, BorderLayout.EAST);

        resultArea = new JTextArea();
        resultArea.setEditable(false);

        String[] columnNames = {"Tag", "Number of Occurrences"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tagsTable = new JTable(tableModel);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(new JLabel("Tags and their occurrences:"), BorderLayout.NORTH);
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
            String htmlContent = new String(Files.readAllBytes(Paths.get(filePath)));
            validateHTML(htmlContent);
        } catch (IOException e) {
            resultArea.setText("Error reading file: " + e.getMessage());
        }
    }

    private void validateHTML(String htmlContent) {
        Stack<String> stack = new Stack<>();
        String[] lines = htmlContent.split("\n");
        int lineNumber = 0;
        Map<String, Integer> tagFrequency = new HashMap<>();

        for (String line : lines) {
            lineNumber++;
            Matcher matcher = TAG_PATTERN.matcher(line);

            while (matcher.find()) {
                String tag = matcher.group(1).split("\\s+")[0].toLowerCase();

                if (SINGLETON_TAGS.contains(tag)) {
                    tagFrequency.put(tag, tagFrequency.getOrDefault(tag, 0) + 1);
                } else if (tag.startsWith("/")) {
                    if (stack.isEmpty() || !stack.peek().equals(tag.substring(1))) {
                        resultArea.setText("Error at line " + lineNumber + ": expected </" + (stack.isEmpty() ? "none" : stack.peek()) + ">, found </" + tag.substring(1) + ">");
                        return;
                    }
                    stack.pop();
                } else {
                    stack.push(tag);
                    tagFrequency.put(tag, tagFrequency.getOrDefault(tag, 0) + 1);
                }
            }
        }

        if (!stack.isEmpty()) {
            resultArea.setText("Missing end tags for: " + stack);
            return;
        }

        resultArea.setText("The file is well formatted.");
        updateTable(tagFrequency);
    }

    private void updateTable(Map<String, Integer> tagFrequency) {
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
