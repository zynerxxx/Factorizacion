import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Factorización LU");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLayout(new BorderLayout());

            // Panel superior para el tamaño de la matriz
            JPanel inputPanel = new JPanel(new FlowLayout());
            JLabel sizeLabel = new JLabel("Dimensión de la matriz:");
            JTextField sizeField = new JTextField(5);
            JButton generateButton = new JButton("Generar Matriz");
            JButton clearButton = new JButton("Limpiar");
            inputPanel.add(sizeLabel);
            inputPanel.add(sizeField);
            inputPanel.add(generateButton);
            inputPanel.add(clearButton);

            // Panel central para la matriz y resultados
            JPanel matrixPanel = new JPanel();
            matrixPanel.setLayout(new GridBagLayout());
            JScrollPane matrixScrollPane = new JScrollPane(matrixPanel);

            JPanel resultsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
            JLabel matrixALabel = new JLabel("Matriz A", SwingConstants.CENTER);
            JLabel matrixLLabel = new JLabel("Matriz L", SwingConstants.CENTER);
            JLabel matrixULabel = new JLabel("Matriz U", SwingConstants.CENTER);
            JTable matrixATable = new JTable();
            JTable matrixLTable = new JTable();
            JTable matrixUTable = new JTable();
            resultsPanel.add(createLabeledPanel(matrixALabel, matrixATable));
            resultsPanel.add(createLabeledPanel(matrixLLabel, matrixLTable));
            resultsPanel.add(createLabeledPanel(matrixULabel, matrixUTable));

            // Acción para generar la matriz
            generateButton.addActionListener(e -> {
                try {
                    int n = Integer.parseInt(sizeField.getText());
                    if (n <= 0) throw new NumberFormatException();

                    matrixPanel.removeAll();
                    matrixPanel.setLayout(new GridLayout(n, n, 5, 5));
                    JTextField[][] fields = new JTextField[n][n];

                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            fields[i][j] = new JTextField();
                            fields[i][j].setHorizontalAlignment(JTextField.CENTER);
                            matrixPanel.add(fields[i][j]);
                        }
                    }

                    JButton calculateButton = new JButton("Calcular Factorización LU");
                    calculateButton.addActionListener(event -> {
                        try {
                            Matrix matrix = new Matrix(n, n);
                            for (int i = 0; i < n; i++) {
                                for (int j = 0; j < n; j++) {
                                    matrix.set(i, j, Double.parseDouble(fields[i][j].getText()));
                                }
                            }

                            LUDecomposition lu = new LUDecomposition(matrix);

                            // Mostrar las matrices A, L y U
                            updateTable(matrixATable, matrix);
                            updateTable(matrixLTable, lu.getL());
                            updateTable(matrixUTable, lu.getU());
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Por favor, ingrese valores válidos en la matriz.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    });

                    matrixPanel.add(calculateButton);
                    matrixPanel.revalidate();
                    matrixPanel.repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Por favor, ingrese un tamaño válido para la matriz.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Acción para limpiar la interfaz
            clearButton.addActionListener(e -> {
                sizeField.setText("");
                matrixPanel.removeAll();
                updateTable(matrixATable, null);
                updateTable(matrixLTable, null);
                updateTable(matrixUTable, null);
                matrixPanel.revalidate();
                matrixPanel.repaint();
            });

            frame.add(inputPanel, BorderLayout.NORTH);
            frame.add(matrixScrollPane, BorderLayout.CENTER);
            frame.add(resultsPanel, BorderLayout.SOUTH);
            frame.setVisible(true);
        });
    }

    private static JPanel createLabeledPanel(JLabel label, JTable table) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private static void updateTable(JTable table, Matrix matrix) {
        if (matrix == null) {
            table.setModel(new javax.swing.table.DefaultTableModel());
            return;
        }
        int rows = matrix.getRows();
        int cols = matrix.getCols();
        String[][] data = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = String.format("%.4f", matrix.get(i, j));
            }
        }
        String[] columnNames = new String[cols];
        for (int i = 0; i < cols; i++) {
            columnNames[i] = "Col " + (i + 1);
        }
        table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }
}
