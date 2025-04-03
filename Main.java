import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el tamaño de la matriz (n x n): ");
        int n = scanner.nextInt();

        Matrix matrix = new Matrix(n, n);
        System.out.println("Ingrese los elementos de la matriz fila por fila:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix.set(i, j, scanner.nextDouble());
            }
        }

        LUDecomposition lu = new LUDecomposition(matrix);

        System.out.println("Matriz L:");
        lu.getL().print();

        System.out.println("Matriz U:");
        lu.getU().print();

        scanner.close();
    }
}
