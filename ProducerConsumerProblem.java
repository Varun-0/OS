import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

class Buffer {
    private Queue<Integer> queue = new LinkedList<>();
    private int capacity;

    // Semaphores to manage buffer access
    private Semaphore mutex = new Semaphore(1);
    private Semaphore items = new Semaphore(0);
    private Semaphore spaces;

    public Buffer(int capacity) {
        this.capacity = capacity;
        this.spaces = new Semaphore(capacity);
    }

    public void produce(int item) throws InterruptedException {
        spaces.acquire(); // Wait for space to be available
        mutex.acquire();  // Acquire exclusive access to the buffer

        queue.add(item);
        System.out.println("Produced: " + item);

        mutex.release();  // Release exclusive access to the buffer
        items.release();  // Signal that an item is available
    }

    public int consume() throws InterruptedException {
        items.acquire(); // Wait for an item to be available
        mutex.acquire(); // Acquire exclusive access to the buffer

        int item = queue.poll();
        System.out.println("Consumed: " + item);

        mutex.release(); // Release exclusive access to the buffer
        spaces.release(); // Signal that space is available

        return item;
    }
}

class Producer extends Thread {
    private Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                buffer.produce(i);
                Thread.sleep((int) (Math.random() * 1000)); // Simulate production time
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer extends Thread {
    private Buffer buffer;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                buffer.consume();
                Thread.sleep((int) (Math.random() * 1000)); // Simulate consumption time
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class ProducerConsumerProblem {
    public static void main(String[] args) {
        Buffer buffer = new Buffer(5); // Buffer capacity of 5

        Producer producer = new Producer(buffer);
        Consumer consumer = new Consumer(buffer);

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Producer and Consumer have finished their tasks.");
    }
}
