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

                        JTextArea lArea = new JTextArea();
                        lArea.setEditable(false);
                        lArea.append("Matriz L:\n");
                        for (int i = 0; i < n; i++) {
                            for (int j = 0; j < n; j++) {
                                lArea.append(String.format("%10.4f", lu.getL().get(i, j)));
                            }
                            lArea.append("\n");
                        }

                        JTextArea uArea = new JTextArea();
                        uArea.setEditable(false);
                        uArea.append("Matriz U:\n");
                        for (int i = 0; i < n; i++) {
                            for (int j = 0; j < n; j++) {
                                uArea.append(String.format("%10.4f", lu.getU().get(i, j)));
                            }
                            uArea.append("\n");
                        }

                        resultFrame.add(new JScrollPane(lArea));
                        resultFrame.add(new JScrollPane(uArea));
                        resultFrame.setVisible(true);
                    }
                });
            });

            frame.add(inputPanel, BorderLayout.NORTH);
            frame.add(new JScrollPane(matrixPanel), BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}
