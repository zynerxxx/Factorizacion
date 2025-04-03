import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Factorización LU");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 500);
            frame.setLayout(new BorderLayout());

            // Panel superior para el tamaño de la matriz
            JPanel inputPanel = new JPanel(new FlowLayout());
            JLabel sizeLabel = new JLabel("Ingrese el tamaño de la matriz (n x n):");
            JTextField sizeField = new JTextField(5);
            JButton generateButton = new JButton("Generar Matriz");
            inputPanel.add(sizeLabel);
            inputPanel.add(sizeField);
            inputPanel.add(generateButton);

            // Panel central para la matriz
            JPanel matrixPanel = new JPanel();
            matrixPanel.setLayout(new GridBagLayout());
            JScrollPane matrixScrollPane = new JScrollPane(matrixPanel);

            // Panel inferior para el botón de cálculo
            JPanel actionPanel = new JPanel(new FlowLayout());
            JButton calculateButton = new JButton("Calcular LU");
            calculateButton.setEnabled(false); // Deshabilitado hasta que se genere la matriz
            actionPanel.add(calculateButton);

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

                    calculateButton.setEnabled(true); // Habilitar el botón de cálculo
                    matrixPanel.revalidate();
                    matrixPanel.repaint();

                    // Acción para calcular LU
                    calculateButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                Matrix matrix = new Matrix(n, n);
                                for (int i = 0; i < n; i++) {
                                    for (int j = 0; j < n; j++) {
                                        matrix.set(i, j, Double.parseDouble(fields[i][j].getText()));
                                    }
                                }

                                LUDecomposition lu = new LUDecomposition(matrix);

                                JFrame resultFrame = new JFrame("Resultados");
                                resultFrame.setSize(600, 400);
                                resultFrame.setLayout(new GridLayout(1, 2));

                                JTable lTable = createMatrixTable(lu.getL());
                                JTable uTable = createMatrixTable(lu.getU());

                                resultFrame.add(new JScrollPane(lTable));
                                resultFrame.add(new JScrollPane(uTable));
                                resultFrame.setVisible(true);
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(frame, "Por favor, ingrese valores válidos en la matriz.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Por favor, ingrese un tamaño válido para la matriz.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            frame.add(inputPanel, BorderLayout.NORTH);
            frame.add(matrixScrollPane, BorderLayout.CENTER);
            frame.add(actionPanel, BorderLayout.SOUTH);
            frame.setVisible(true);
        });
    }

    private static JTable createMatrixTable(Matrix matrix) {
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
        return new JTable(data, columnNames);
    }
}
