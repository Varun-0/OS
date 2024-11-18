//Process control system calls: The demonstration of FORK and WAIT system calls along with zombie and 
//orphan states. Implement the program in which the main program accepts the integers to be sorted. 
//Main program uses the FORK system call to create a new process called a child process. 
//Parent process sorts the integers using a sorting algorithm and waits for the child process using 
//WAIT system call to sort the integers using any sorting algorithm. 
//Also demonstrate zombie and orphan states.

import java.util.Arrays;
import java.util.Scanner;

class BubbleSortThread extends Thread {
    private int[] arr;

    public BubbleSortThread(int[] arr) {
        this.arr = arr;
    }

    @Override
    public void run() {
        System.out.println("Parent Thread: Sorting using Bubble Sort...");
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        System.out.println("Parent Thread: Sorted array (Bubble Sort): " + Arrays.toString(arr));
        try {
            Thread.sleep(5000); // Simulate waiting for child thread
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Parent Thread: Exiting...");
    }
}

class SelectionSortThread extends Thread {
    private int[] arr;

    public SelectionSortThread(int[] arr) {
        this.arr = arr;
    }

    @Override
    public void run() {
        System.out.println("Child Thread: Sorting using Selection Sort...");
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            int temp = arr[minIndex];
            arr[minIndex] = arr[i];
            arr[i] = temp;
        }
        System.out.println("Child Thread: Sorted array (Selection Sort): " + Arrays.toString(arr));
        System.out.println("Child Thread: Simulating orphan state by sleeping...");
        try {
            Thread.sleep(5000); // Simulate orphan state
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Child Thread: Orphan state handled. Exiting...");
    }
}

public class ForkAndWait {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input array size and elements
        System.out.print("Enter the number of integers: ");
        int n = scanner.nextInt();

        int[] parentArray = new int[n];
        int[] childArray = new int[n];

        System.out.println("Enter the integers: ");
        for (int i = 0; i < n; i++) {
            parentArray[i] = scanner.nextInt();
            childArray[i] = parentArray[i]; // Copy for child thread
        }

        // Create threads for sorting
        BubbleSortThread parentThread = new BubbleSortThread(parentArray);
        SelectionSortThread childThread = new SelectionSortThread(childArray);

        // Start threads
        childThread.start();

        try {
            // Wait for child thread to start sorting
            Thread.sleep(2000); // Simulate delay for zombie state
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Main Thread: Simulating zombie state...");
        parentThread.start();

        try {
            // Wait for parent thread to finish
            parentThread.join();
            System.out.println("Main Thread: Waiting for child thread to complete...");
            childThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Main Thread: Both threads completed. Program exiting.");
        scanner.close();
    }
}
