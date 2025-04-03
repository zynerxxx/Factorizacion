public class Matrix {
    private final double[][] data;
    private final int rows;
    private final int cols;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    public void set(int row, int col, double value) {
        data[row][col] = value;
    }

    public double get(int row, int col) {
        return data[row][col];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public void print() {
        for (double[] row : data) {
            for (double value : row) {
                System.out.printf("%10.4f", value);
            }
            System.out.println();
        }
    }
}
