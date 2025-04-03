import java.util.ArrayList;
import java.util.List;

public class LUDecomposition {
    private final Matrix L;
    private final Matrix U;
    private final List<String> steps; // Lista para registrar los pasos del cálculo

    public LUDecomposition(Matrix matrix) {
        int n = matrix.getRows();
        L = new Matrix(n, n);
        U = new Matrix(n, n);
        steps = new ArrayList<>(); // Inicializar la lista de pasos

        steps.add("Inicio del procedimiento de Factorización LU:\n");

        for (int i = 0; i < n; i++) {
            steps.add(String.format("Paso %d: Procesando fila %d\n", i + 1, i + 1));

            // Calcular los elementos de U
            for (int j = i; j < n; j++) {
                double sum = 0;
                for (int k = 0; k < i; k++) {
                    sum += L.get(i, k) * U.get(k, j);
                }
                U.set(i, j, matrix.get(i, j) - sum);
                steps.add(String.format("  U[%d][%d] = A[%d][%d] - Σ(L[%d][k] * U[k][%d]) = %.4f",
                        i, j, i, j, i, j, U.get(i, j)));
            }

            // Calcular los elementos de L
            for (int j = i; j < n; j++) {
                if (i == j) {
                    L.set(i, i, 1);
                    steps.add(String.format("  L[%d][%d] = 1 (diagonal principal)", i, i));
                } else {
                    double sum = 0;
                    for (int k = 0; k < i; k++) {
                        sum += L.get(j, k) * U.get(k, i);
                    }
                    L.set(j, i, (matrix.get(j, i) - sum) / U.get(i, i));
                    steps.add(String.format("  L[%d][%d] = (A[%d][%d] - Σ(L[%d][k] * U[k][%d])) / U[%d][%d] = %.4f",
                            j, i, j, i, j, i, i, i, L.get(j, i)));
                }
            }
        }

        steps.add("\nFin del procedimiento de Factorización LU.");
    }

    public Matrix getL() {
        return L;
    }

    public Matrix getU() {
        return U;
    }

    public List<String> getSteps() {
        return steps; // Devolver los pasos registrados
    }
}
