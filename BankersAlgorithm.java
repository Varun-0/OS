import java.util.Scanner;

public class BankersAlgorithm {
    private int numberOfProcesses;
    private int numberOfResources;
    private int[] available;
    private int[][] maximum;
    private int[][] allocation;
    private int[][] need;

    public BankersAlgorithm(int numberOfProcesses, int numberOfResources) {
        this.numberOfProcesses = numberOfProcesses;
        this.numberOfResources = numberOfResources;
        this.available = new int[numberOfResources];
        this.maximum = new int[numberOfProcesses][numberOfResources];
        this.allocation = new int[numberOfProcesses][numberOfResources];
        this.need = new int[numberOfProcesses][numberOfResources];
    }

    public void input() {
        // Scanner scanner = new Scanner(System.in);

        // System.out.println("Enter the Available Resources:");
        // for (int i = 0; i < numberOfResources; i++) {
        //     available[i] = scanner.nextInt();
        // }

        // System.out.println("Enter the Maximum Matrix:");
        // for (int i = 0; i < numberOfProcesses; i++) {
        //     for (int j = 0; j < numberOfResources; j++) {
        //         maximum[i][j] = scanner.nextInt();
        //     }
        // }

        // System.out.println("Enter the Allocation Matrix:");
        // for (int i = 0; i < numberOfProcesses; i++) {
        //     for (int j = 0; j < numberOfResources; j++) {
        //         allocation[i][j] = scanner.nextInt();
        //         need[i][j] = maximum[i][j] - allocation[i][j];
        //     }
        // }
        // scanner.close();

        // Hard-coded values for testing
        available = new int[]{3, 3, 2};

        maximum = new int[][]{
            {7, 5, 3},
            {3, 2, 2},
            {9, 0, 2},
            {2, 2, 2},
            {4, 3, 3}
        };

        allocation = new int[][]{
            {0, 1, 0},
            {2, 0, 0},
            {3, 0, 2},
            {2, 1, 1},
            {0, 0, 2}
        };

        for (int i = 0; i < numberOfProcesses; i++) {
            for (int j = 0; j < numberOfResources; j++) {
                need[i][j] = maximum[i][j] - allocation[i][j];
            }
        }
    }

    public boolean isSafe() {
        int[] work = available.clone();
        boolean[] finish = new boolean[numberOfProcesses];
        int[] safeSequence = new int[numberOfProcesses];
        int count = 0;

        while (count < numberOfProcesses) {
            boolean found = false;
            for (int i = 0; i < numberOfProcesses; i++) {
                if (!finish[i]) {
                    int j;
                    for (j = 0; j < numberOfResources; j++) {
                        if (need[i][j] > work[j]) {
                            break;
                        }
                    }
                    if (j == numberOfResources) {
                        for (int k = 0; k < numberOfResources; k++) {
                            work[k] += allocation[i][k];
                        }
                        safeSequence[count++] = i;
                        finish[i] = true;
                        found = true;
                    }
                }
            }
            if (!found) {
                System.out.println("System is not in a safe state.");
                return false;
            }
        }

        System.out.println("System is in a safe state.");
        System.out.print("Safe sequence is: ");
        for (int i = 0; i < numberOfProcesses; i++) {
            System.out.print(safeSequence[i] + " ");
        }
        System.out.println();
        return true;
    }

    public static void main(String[] args) {
        // Scanner scanner = new Scanner(System.in);

        // System.out.print("Enter the number of processes: ");
        // int numberOfProcesses = scanner.nextInt();

        // System.out.print("Enter the number of resources: ");
        // int numberOfResources = scanner.nextInt();

        int numberOfProcesses = 5;
        int numberOfResources = 3;

        BankersAlgorithm bankersAlgorithm = new BankersAlgorithm(numberOfProcesses, numberOfResources);
        bankersAlgorithm.input();

        bankersAlgorithm.isSafe();
        // scanner.close();
    }
}
