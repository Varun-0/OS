import java.util.Random;
import java.util.Scanner;

public class MatrixOperations {

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // Input for Matrix A
        System.out.print("Enter the number of rows for Matrix A: ");
        int rowsA = scanner.nextInt();
        System.out.print("Enter the number of columns for Matrix A: ");
        int colsA = scanner.nextInt();

        // Input for Matrix B
        System.out.print("Enter the number of rows for Matrix B: ");
        int rowsB = scanner.nextInt();
        System.out.print("Enter the number of columns for Matrix B: ");
        int colsB = scanner.nextInt();

        // Generate random matrices
        int[][] matrixA = generateRandomMatrix(rowsA, colsA, random);
        int[][] matrixB = generateRandomMatrix(rowsB, colsB, random);

        // Initialize result matrices
        int[][] additionResult = null;
        int[][] subtractionResult = null;
        if (rowsA == rowsB && colsA == colsB) {
            additionResult = new int[rowsA][colsA];
            subtractionResult = new int[rowsA][colsA];
        } else {
            System.out.println("Addition and subtraction skipped: Matrices must have the same dimensions.");
        }

        int[][] multiplicationResult = null;
        if (colsA == rowsB) {
            multiplicationResult = new int[rowsA][colsB];
        } else {
            System.out.println("Multiplication skipped: Number of columns in Matrix A must equal number of rows in Matrix B.");
        }

        // Perform addition and subtraction
        if (additionResult != null && subtractionResult != null) {
            Thread[] addThreads = new Thread[rowsA * colsA];
            Thread[] subThreads = new Thread[rowsA * colsA];
            int threadCount = 0;

            for (int i = 0; i < rowsA; i++) {
                for (int j = 0; j < colsA; j++) {
                    addThreads[threadCount] = new Thread(new ElementAdditionTask(matrixA, matrixB, additionResult, i, j));
                    subThreads[threadCount] = new Thread(new ElementSubtractionTask(matrixA, matrixB, subtractionResult, i, j));
                    addThreads[threadCount].start();
                    subThreads[threadCount].start();
                    threadCount++;
                }
            }

            // Wait for addition and subtraction threads to finish
            for (int i = 0; i < threadCount; i++) {
                addThreads[i].join();
                subThreads[i].join();
            }
        }

        // Perform multiplication
        if (multiplicationResult != null) {
            Thread[] mulThreads = new Thread[rowsA * colsB];
            int threadCount = 0;

            for (int i = 0; i < rowsA; i++) {
                for (int j = 0; j < colsB; j++) {
                    mulThreads[threadCount] = new Thread(new ElementMultiplicationTask(matrixA, matrixB, multiplicationResult, i, j));
                    mulThreads[threadCount].start();
                    threadCount++;
                }
            }

            // Wait for multiplication threads to finish
            for (int i = 0; i < threadCount; i++) {
                mulThreads[i].join();
            }
        }

        // Print matrices
        System.out.println("Matrix A:");
        printMatrix(matrixA);
        System.out.println("Matrix B:");
        printMatrix(matrixB);

        if (additionResult != null) {
            System.out.println("Addition Result:");
            printMatrix(additionResult);
        }
        if (subtractionResult != null) {
            System.out.println("Subtraction Result:");
            printMatrix(subtractionResult);
        }
        if (multiplicationResult != null) {
            System.out.println("Multiplication Result:");
            printMatrix(multiplicationResult);
        }
        scanner.close();
    }

    public static int[][] generateRandomMatrix(int rows, int cols, Random random) {
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = random.nextInt(10); // Random numbers between 0-9
            }
        }
        return matrix;
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    // Task for element-wise addition
    static class ElementAdditionTask implements Runnable {
        private final int[][] matrixA;
        private final int[][] matrixB;
        private final int[][] result;
        private final int row, col;

        public ElementAdditionTask(int[][] matrixA, int[][] matrixB, int[][] result, int row, int col) {
            this.matrixA = matrixA;
            this.matrixB = matrixB;
            this.result = result;
            this.row = row;
            this.col = col;
        }

        @Override
        public void run() {
            result[row][col] = matrixA[row][col] + matrixB[row][col];
        }
    }

    // Task for element-wise subtraction
    static class ElementSubtractionTask implements Runnable {
        private final int[][] matrixA;
        private final int[][] matrixB;
        private final int[][] result;
        private final int row, col;

        public ElementSubtractionTask(int[][] matrixA, int[][] matrixB, int[][] result, int row, int col) {
            this.matrixA = matrixA;
            this.matrixB = matrixB;
            this.result = result;
            this.row = row;
            this.col = col;
        }

        @Override
        public void run() {
            result[row][col] = matrixA[row][col] - matrixB[row][col];
        }
    }

    // Task for element-wise multiplication
    static class ElementMultiplicationTask implements Runnable {
        private final int[][] matrixA;
        private final int[][] matrixB;
        private final int[][] result;
        private final int row, col;

        public ElementMultiplicationTask(int[][] matrixA, int[][] matrixB, int[][] result, int row, int col) {
            this.matrixA = matrixA;
            this.matrixB = matrixB;
            this.result = result;
            this.row = row;
            this.col = col;
        }

        @Override
        public void run() {
            result[row][col] = 0;
            for (int k = 0; k < matrixA[0].length; k++) {
                result[row][col] += matrixA[row][k] * matrixB[k][col];
            }
        }
    }
}
