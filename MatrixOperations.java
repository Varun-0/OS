import java.util.Random;
import java.util.Scanner;

class MatrixAddition extends Thread {
    private int[][] matrixA, matrixB, result;
    private int startRow, endRow;

    public MatrixAddition(int[][] matrixA, int[][] matrixB, int[][] result, int startRow, int endRow) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.result = result;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    public void run() {
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < matrixA[0].length; j++) {
                result[i][j] = matrixA[i][j] + matrixB[i][j];
            }
        }
    }
}

class MatrixSubtraction extends Thread {
    private int[][] matrixA, matrixB, result;
    private int startRow, endRow;

    public MatrixSubtraction(int[][] matrixA, int[][] matrixB, int[][] result, int startRow, int endRow) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.result = result;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    public void run() {
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < matrixA[0].length; j++) {
                result[i][j] = matrixA[i][j] - matrixB[i][j];
            }
        }
    }
}

class MatrixMultiplications extends Thread {
    private int[][] matrixA, matrixB, result;
    private int startRow, endRow;

    public MatrixMultiplications(int[][] matrixA, int[][] matrixB, int[][] result, int startRow, int endRow) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.result = result;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    public void run() {
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < matrixB[0].length; j++) {
                result[i][j] = 0;
                for (int k = 0; k < matrixA[0].length; k++) {
                    result[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
    }
}

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

        // Number of threads
        int threadCount = 2;
        int rowsPerThread = rowsA / threadCount;

        if (additionResult != null && subtractionResult != null) {
            MatrixAddition[] addThreads = new MatrixAddition[threadCount];
            MatrixSubtraction[] subThreads = new MatrixSubtraction[threadCount];
            for (int i = 0; i < threadCount; i++) {
                int startRow = i * rowsPerThread;
                int endRow = (i == threadCount - 1) ? rowsA : startRow + rowsPerThread;

                // Addition
                addThreads[i] = new MatrixAddition(matrixA, matrixB, additionResult, startRow, endRow);
                addThreads[i].start();

                // Subtraction
                subThreads[i] = new MatrixSubtraction(matrixA, matrixB, subtractionResult, startRow, endRow);
                subThreads[i].start();
            }

            // Wait for addition and subtraction threads to finish
            for (int i = 0; i < threadCount; i++) {
                addThreads[i].join();
                subThreads[i].join();
            }
        }

        if (multiplicationResult != null) {
            MatrixMultiplications[] mulThreads = new MatrixMultiplications[threadCount];
            for (int i = 0; i < threadCount; i++) {
                int startRow = i * rowsPerThread;
                int endRow = (i == threadCount - 1) ? rowsA : startRow + rowsPerThread;

                mulThreads[i] = new MatrixMultiplications(matrixA, matrixB, multiplicationResult, startRow, endRow);
                mulThreads[i].start();
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
}
