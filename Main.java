import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Factorización LU");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLayout(new BorderLayout());

            JPanel inputPanel = new JPanel(new GridLayout(0, 1));
            JTextField sizeField = new JTextField();
            inputPanel.add(new JLabel("Ingrese el tamaño de la matriz (n x n):"));
            inputPanel.add(sizeField);

            JButton generateButton = new JButton("Generar Matriz");
            inputPanel.add(generateButton);

            JPanel matrixPanel = new JPanel();
            matrixPanel.setLayout(new GridLayout(0, 1));

            generateButton.addActionListener(e -> {
                int n = Integer.parseInt(sizeField.getText());
                matrixPanel.removeAll();
                JTextField[][] fields = new JTextField[n][n];
                matrixPanel.setLayout(new GridLayout(n, n));
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        fields[i][j] = new JTextField();
                        matrixPanel.add(fields[i][j]);
                    }
                }
                matrixPanel.revalidate();
                matrixPanel.repaint();

                JButton calculateButton = new JButton("Calcular LU");
                matrixPanel.add(calculateButton);

                calculateButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
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
                    }
                });
            });

            frame.add(inputPanel, BorderLayout.NORTH);
            frame.add(new JScrollPane(matrixPanel), BorderLayout.CENTER);
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
