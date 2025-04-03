import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = createMainFrame();

            // Panel superior para ingresar el tamaño de la matriz
            JPanel sizePanel = createTitledPanel("Tamaño de la Matriz", new Color(169, 169, 169), new Color(70, 70, 70));
            JLabel sizeLabel = createLabel("Tamaño (n x n):");
            JTextField sizeField = createTextField(5);
            JButton generateButton = createStyledButton("Generar Matriz", new Color(119, 136, 153), new Color(105, 105, 105));
            JButton clearButton = createStyledButton("Limpiar", new Color(205, 92, 92), new Color(178, 34, 34));
            sizePanel.add(sizeLabel);
            sizePanel.add(sizeField);
            sizePanel.add(generateButton);
            sizePanel.add(clearButton);

            // Botón para explicar el ejercicio
            JButton helpButton = createStyledButton("¿Qué es esto?", new Color(100, 149, 237), new Color(70, 130, 180));
            helpButton.addActionListener(e -> {
                String message = """
                        Este programa realiza la factorización LU de una matriz cuadrada.
                        
                        La factorización LU descompone una matriz A en el producto de dos matrices:
                        - L: Una matriz triangular inferior con unos en la diagonal principal.
                        - U: Una matriz triangular superior.
                        
                        Pasos:
                        1. Ingrese el tamaño de la matriz (n x n).
                        2. Genere la matriz y complete los valores.
                        3. Presione "Calcular LU" para obtener las matrices L y U.
                        
                        ¡Disfruta aprendiendo álgebra lineal!
                        """;
                JOptionPane.showMessageDialog(frame, message, "Explicación del Ejercicio", JOptionPane.INFORMATION_MESSAGE);
            });

            sizePanel.add(helpButton); // Agregar el botón al panel superior

            // Panel central para ingresar los datos de la matriz
            JPanel matrixInputPanel = new JPanel();
            matrixInputPanel.setBorder(createTitledBorder("Datos de la Matriz", new Color(112, 128, 144), new Color(70, 70, 70)));
            matrixInputPanel.setLayout(new GridBagLayout());
            JScrollPane matrixScrollPane = new JScrollPane(matrixInputPanel);
            matrixScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            matrixScrollPane.getViewport().setBackground(new Color(245, 245, 245));

            // Panel inferior para mostrar los resultados
            JPanel resultsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
            resultsPanel.setBorder(createTitledBorder("Resultados", new Color(169, 169, 169), new Color(70, 70, 70)));
            JTable matrixATable = createStyledTable();
            JTable matrixLTable = createStyledTable();
            JTable matrixUTable = createStyledTable();
            resultsPanel.add(createStyledPanel("Matriz A", matrixATable, new Color(230, 230, 250)));
            resultsPanel.add(createStyledPanel("Matriz L", matrixLTable, new Color(240, 255, 240)));
            resultsPanel.add(createStyledPanel("Matriz U", matrixUTable, new Color(255, 240, 245)));

            // Panel para el botón de calcular LU
            JPanel calculatePanel = createTitledPanel("Acción", new Color(112, 128, 144), new Color(70, 70, 70));
            JButton calculateButton = createStyledButton("Calcular LU", new Color(119, 136, 153), new Color(105, 105, 105));
            calculatePanel.add(calculateButton);

            // Variable para almacenar la última factorización LU
            final LUDecomposition[] lu = {null};

            // Botón para explicar el procedimiento del ejercicio
            JButton explainButton = createStyledButton("Explicar", new Color(100, 149, 237), new Color(70, 130, 180));
            explainButton.addActionListener(e -> {
                if (lu[0] != null) { // Verificar si ya se realizó el cálculo
                    StringBuilder explanation = new StringBuilder("Pasos del cálculo:\n\n");
                    int stepNumber = 1;
                    for (String step : lu[0].getSteps()) {
                        explanation.append(String.format("%d. %s\n", stepNumber++, step));
                    }
                    JOptionPane.showMessageDialog(frame, explanation.toString(), "Explicación del Resultado", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Primero debe calcular la factorización LU.", "Error", JOptionPane.WARNING_MESSAGE);
                }
            });

            calculatePanel.add(explainButton); // Agregar el botón a la sección de acción

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
                            fields[i][j] = createTextField(0);
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

                            lu[0] = new LUDecomposition(matrix); // Guardar la factorización

                            // Mostrar las matrices A, L y U
                            updateTable(matrixATable, matrix);
                            updateTable(matrixLTable, lu[0].getL());
                            updateTable(matrixUTable, lu[0].getU());
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

                // Eliminar todos los ActionListeners del botón "Calcular LU"
                for (java.awt.event.ActionListener al : calculateButton.getActionListeners()) {
                    calculateButton.removeActionListener(al);
                }
            });

            frame.add(sizePanel, BorderLayout.NORTH);
            frame.add(matrixScrollPane, BorderLayout.CENTER);
            frame.add(calculatePanel, BorderLayout.EAST);
            frame.add(resultsPanel, BorderLayout.SOUTH);
            frame.setVisible(true);
        });
    }

    private static JFrame createMainFrame() {
        JFrame frame = new JFrame("Factorización LU");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 850);
        frame.setLayout(new BorderLayout());
        return frame;
    }

    private static JPanel createTitledPanel(String title, Color borderColor, Color titleColor) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBorder(createTitledBorder(title, borderColor, titleColor));
        return panel;
    }

    private static TitledBorder createTitledBorder(String title, Color borderColor, Color titleColor) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(borderColor, 2),
            title,
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 16),
            titleColor
        );
    }

    private static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }

    private static JTextField createTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBackground(new Color(245, 245, 245));
        textField.setBorder(BorderFactory.createLineBorder(new Color(169, 169, 169)));
        return textField;
    }

    private static JButton createStyledButton(String text, Color background, Color hoverBackground) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(background.darker(), 2));
        button.setOpaque(true);
        button.setMargin(new Insets(10, 20, 10, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverBackground);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(background);
            }
        });

        return button;
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

    private static JPanel createStyledPanel(String title, JTable table, Color backgroundColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);
        JLabel label = createLabel(title);
        label.setHorizontalAlignment(SwingConstants.CENTER);
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
