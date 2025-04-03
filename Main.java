import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Factorización LU");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 800);
            frame.setLayout(new BorderLayout());

            // Panel superior para ingresar el tamaño de la matriz
            JPanel sizePanel = new JPanel(new FlowLayout());
            sizePanel.setBorder(new TitledBorder("Tamaño de la Matriz"));
            JLabel sizeLabel = new JLabel("Tamaño (n x n):");
            JTextField sizeField = new JTextField(5);
            JButton generateButton = new JButton("Generar Matriz");
            JButton clearButton = new JButton("Limpiar");
            sizePanel.add(sizeLabel);
            sizePanel.add(sizeField);
            sizePanel.add(generateButton);
            sizePanel.add(clearButton);

            // Panel central para ingresar los datos de la matriz
            JPanel matrixInputPanel = new JPanel();
            matrixInputPanel.setBorder(new TitledBorder("Datos de la Matriz"));
            matrixInputPanel.setLayout(new GridBagLayout());
            JScrollPane matrixScrollPane = new JScrollPane(matrixInputPanel);

            // Panel inferior para mostrar los resultados
            JPanel resultsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
            resultsPanel.setBorder(new TitledBorder("Resultados"));
            JLabel matrixALabel = new JLabel("Matriz A", SwingConstants.CENTER);
            JLabel matrixLLabel = new JLabel("Matriz L", SwingConstants.CENTER);
            JLabel matrixULabel = new JLabel("Matriz U", SwingConstants.CENTER);
            JTable matrixATable = createStyledTable();
            JTable matrixLTable = createStyledTable();
            JTable matrixUTable = createStyledTable();
            resultsPanel.add(createStyledPanel(matrixALabel, matrixATable, new Color(240, 248, 255)));
            resultsPanel.add(createStyledPanel(matrixLLabel, matrixLTable, new Color(240, 255, 240)));
            resultsPanel.add(createStyledPanel(matrixULabel, matrixUTable, new Color(255, 240, 245)));

            // Panel para el botón de calcular LU
            JPanel calculatePanel = new JPanel(new FlowLayout());
            calculatePanel.setBorder(new TitledBorder("Acción"));
            JButton calculateButton = new JButton("Calcular LU");
            calculateButton.setPreferredSize(new Dimension(200, 40));
            calculatePanel.add(calculateButton);

            // Acción para generar la matriz
            generateButton.addActionListener(e -> {
                try {
                    int n = Integer.parseInt(sizeField.getText());
                    if (n <= 0) throw new NumberFormatException();

                    matrixInputPanel.removeAll();
                    matrixInputPanel.setLayout(new GridLayout(n, n, 5, 5));
                    JTextField[][] fields = new JTextField[n][n];

                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            fields[i][j] = new JTextField();
                            fields[i][j].setHorizontalAlignment(JTextField.CENTER);
                            fields[i][j].setFont(new Font("Arial", Font.PLAIN, 14));
                            matrixInputPanel.add(fields[i][j]);
                        }
                    }

                    matrixInputPanel.revalidate();
                    matrixInputPanel.repaint();

                    // Eliminar todos los ActionListeners previos antes de agregar uno nuevo
                    for (java.awt.event.ActionListener al : calculateButton.getActionListeners()) {
                        calculateButton.removeActionListener(al);
                    }

                    // Acción para calcular LU
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
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Por favor, ingrese un tamaño válido para la matriz.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Acción para limpiar la interfaz
            clearButton.addActionListener(e -> {
                sizeField.setText("");
                matrixInputPanel.removeAll();
                updateTable(matrixATable, null);
                updateTable(matrixLTable, null);
                updateTable(matrixUTable, null);
                matrixInputPanel.revalidate();
                matrixInputPanel.repaint();
            });

            frame.add(sizePanel, BorderLayout.NORTH);
            frame.add(matrixScrollPane, BorderLayout.CENTER);
            frame.add(calculatePanel, BorderLayout.EAST);
            frame.add(resultsPanel, BorderLayout.SOUTH);
            frame.setVisible(true);
        });
    }

    private static JTable createStyledTable() {
        JTable table = new JTable();
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(200, 200, 200));
        table.getTableHeader().setForeground(Color.BLACK);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        return table;
    }

    private static JPanel createStyledPanel(JLabel label, JTable table, Color backgroundColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);
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
