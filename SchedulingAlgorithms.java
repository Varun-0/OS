import java.util.*;

class Process {
    int pid; // Process ID
    int arrivalTime; // Arrival Time
    int burstTime; // Burst Time
    int priority; // Priority (for Priority Scheduling)
    int remainingTime; // Remaining Time (for Preemptive Scheduling)
    int finishTime; // Finish Time
    int turnaroundTime; // Turnaround Time
    int waitingTime; // Waiting Time

    public Process(int pid, int arrivalTime, int burstTime, int priority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime; // For preemptive scheduling
    }

    public void reset() {
        this.remainingTime = this.burstTime;
        this.finishTime = 0;
        this.turnaroundTime = 0;
        this.waitingTime = 0;
    }
}

public class SchedulingAlgorithms {
    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>();
        processes.add(new Process(1, 0, 8, 2));
        processes.add(new Process(2, 1, 4, 1));
        processes.add(new Process(3, 2, 9, 3));
        processes.add(new Process(4, 3, 5, 2));

        System.out.println("First Come First Serve (FCFS):");
        fcfs(new ArrayList<>(processes));

        resetProcesses(processes);
        System.out.println("\nShortest Job First (Non-Preemptive):");
        sjfNonPreemptive(new ArrayList<>(processes));

        resetProcesses(processes);
        System.out.println("\nShortest Job First (Preemptive):");
        sjfPreemptive(new ArrayList<>(processes));

        resetProcesses(processes);
        System.out.println("\nPriority Scheduling (Non-Preemptive):");
        priorityNonPreemptive(new ArrayList<>(processes));

        resetProcesses(processes);
        System.out.println("\nPriority Scheduling (Preemptive):");
        priorityPreemptive(new ArrayList<>(processes));

        resetProcesses(processes);
        System.out.println("\nRound Robin (Quantum = 2):");
        roundRobin(new ArrayList<>(processes), 2);
    }

    public static void resetProcesses(List<Process> processes) {
        for (Process p : processes) {
            p.reset();
        }
    }

    public static void fcfs(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int currentTime = 0;

        for (Process p : processes) {
            if (currentTime < p.arrivalTime) {
                currentTime = p.arrivalTime;
            }
            p.finishTime = currentTime + p.burstTime;
            p.turnaroundTime = p.finishTime - p.arrivalTime;
            p.waitingTime = p.turnaroundTime - p.burstTime;
            currentTime += p.burstTime;
        }

        printMetrics(processes);
    }

    public static void sjfNonPreemptive(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int currentTime = 0;
        List<Process> readyQueue = new ArrayList<>();
        List<Process> completedProcesses = new ArrayList<>();

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                readyQueue.add(processes.remove(0));
            }
            if (!readyQueue.isEmpty()) {
                readyQueue.sort(Comparator.comparingInt(p -> p.burstTime));
                Process p = readyQueue.remove(0);
                if (currentTime < p.arrivalTime) {
                    currentTime = p.arrivalTime;
                }
                p.finishTime = currentTime + p.burstTime;
                p.turnaroundTime = p.finishTime - p.arrivalTime;
                p.waitingTime = p.turnaroundTime - p.burstTime;
                currentTime += p.burstTime;
                completedProcesses.add(p);
            } else {
                currentTime++;
            }
        }

        printMetrics(completedProcesses);
    }

    public static void sjfPreemptive(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int currentTime = 0;
        List<Process> readyQueue = new ArrayList<>();
        List<Process> completedProcesses = new ArrayList<>();

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                readyQueue.add(processes.remove(0));
            }
            if (!readyQueue.isEmpty()) {
                readyQueue.sort(Comparator.comparingInt(p -> p.remainingTime));
                Process p = readyQueue.get(0);
                p.remainingTime--;
                currentTime++;
                if (p.remainingTime == 0) {
                    p.finishTime = currentTime;
                    p.turnaroundTime = p.finishTime - p.arrivalTime;
                    p.waitingTime = p.turnaroundTime - p.burstTime;
                    readyQueue.remove(p);
                    completedProcesses.add(p);
                }
            } else {
                currentTime++;
            }
        }

        printMetrics(completedProcesses);
    }

    public static void priorityNonPreemptive(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int currentTime = 0;
        List<Process> readyQueue = new ArrayList<>();
        List<Process> completedProcesses = new ArrayList<>();

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                readyQueue.add(processes.remove(0));
            }
            if (!readyQueue.isEmpty()) {
                readyQueue.sort(Comparator.comparingInt(p -> p.priority));
                Process p = readyQueue.remove(0);
                if (currentTime < p.arrivalTime) {
                    currentTime = p.arrivalTime;
                }
                p.finishTime = currentTime + p.burstTime;
                p.turnaroundTime = p.finishTime - p.arrivalTime;
                p.waitingTime = p.turnaroundTime - p.burstTime;
                currentTime += p.burstTime;
                completedProcesses.add(p);
            } else {
                currentTime++;
            }
        }

        printMetrics(completedProcesses);
    }

    public static void priorityPreemptive(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime)); // Sort processes by arrival time
        int currentTime = 0;
        List<Process> readyQueue = new ArrayList<>();
        List<Process> completedProcesses = new ArrayList<>();
    
        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            // Add all processes that have arrived to the ready queue
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                readyQueue.add(processes.remove(0));
            }
    
            if (!readyQueue.isEmpty()) {
                // Sort the ready queue based on priority (lower priority value means higher priority)
                readyQueue.sort(Comparator.comparingInt(p -> p.priority));
                Process currentProcess = readyQueue.get(0);
    
                // Execute the process for 1 time unit
                currentProcess.remainingTime--;
                currentTime++;
    
                // If the process has finished execution, calculate metrics
                if (currentProcess.remainingTime == 0) {
                    currentProcess.finishTime = currentTime;
                    currentProcess.turnaroundTime = currentProcess.finishTime - currentProcess.arrivalTime;
                    currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
                    completedProcesses.add(currentProcess);
                    readyQueue.remove(0); // Remove the finished process from the ready queue
                }
            } else {
                // If no process is in the ready queue, move the time forward
                currentTime++;
            }
        }
    
        // Print the final metrics for completed processes
        printMetrics(completedProcesses);
    }  

    public static void roundRobin(List<Process> processes, int quantum) {
        int currentTime = 0;
        Queue<Process> readyQueue = new LinkedList<>();
        List<Process> completedProcesses = new ArrayList<>();
    
        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            // Add all processes that have arrived to the ready queue
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                readyQueue.add(processes.remove(0));
            }
    
            if (!readyQueue.isEmpty()) {
                Process currentProcess = readyQueue.poll(); // Get the process from the queue
    
                // Determine how much time to execute
                int timeToExecute = Math.min(currentProcess.remainingTime, quantum);
                currentProcess.remainingTime -= timeToExecute;
                currentTime += timeToExecute;
    
                // If the process has finished execution, calculate metrics
                if (currentProcess.remainingTime == 0) {
                    currentProcess.finishTime = currentTime;
                    currentProcess.turnaroundTime = currentProcess.finishTime - currentProcess.arrivalTime;
                    currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
                    completedProcesses.add(currentProcess);
                } else {
                    // If not finished, put it back into the ready queue
                    readyQueue.add(currentProcess);
                }
            } else {
                // If no process is in the ready queue, move the time forward
                currentTime++;
            }
        }
    
        // Print the final metrics for completed processes
        printMetrics(completedProcesses);
    }    

    public static void printMetrics(List<Process> processes) {
        System.out.println("PID\tFinish\tTurnaround\tWaiting");
        for (Process p : processes) {
            System.out.println(p.pid + "\t" + p.finishTime + "\t" + p.turnaroundTime + "\t\t" + p.waitingTime);
        }
    }
}
