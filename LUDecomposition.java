public class LUDecomposition {
    private final Matrix L;
    private final Matrix U;

    public LUDecomposition(Matrix matrix) {
        int n = matrix.getRows();
        L = new Matrix(n, n);
        U = new Matrix(n, n);

        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                double sum = 0;
                for (int k = 0; k < i; k++) {
                    sum += L.get(i, k) * U.get(k, j);
                }
                U.set(i, j, matrix.get(i, j) - sum);
            }

            for (int j = i; j < n; j++) {
                if (i == j) {
                    L.set(i, i, 1);
                } else {
                    double sum = 0;
                    for (int k = 0; k < i; k++) {
                        sum += L.get(j, k) * U.get(k, i);
                    }
                    L.set(j, i, (matrix.get(j, i) - sum) / U.get(i, i));
                }
            }
        }
    }

    public Matrix getL() {
        return L;
    }

    public Matrix getU() {
        return U;
    }
}
