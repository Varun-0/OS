import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class SharedResource {
    private List<Integer> data = new ArrayList<>();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void read() {
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " read: " + data);
            Thread.sleep(1000); // Simulate reading time
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void write(int value) {
        lock.writeLock().lock();
        try {
            data.add(value);
            System.out.println(Thread.currentThread().getName() + " wrote: " + value);
            Thread.sleep(1000); // Simulate writing time
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }
}

class Reader extends Thread {
    private SharedResource resource;
    private int readCount;

    public Reader(SharedResource resource, int readCount) {
        this.resource = resource;
        this.readCount = readCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < readCount; i++) {
            resource.read();
        }
    }
}

class Writer extends Thread {
    private SharedResource resource;
    private int writeCount;

    public Writer(SharedResource resource, int writeCount) {
        this.resource = resource;
        this.writeCount = writeCount;
    }

    @Override
    public void run() {
        for (int i = 0; i < writeCount; i++) {
            resource.write(i);
        }
    }
}

public class ReaderWriterProblem {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();

        int readCount = 10;
        int writeCount = 5;

        Reader reader1 = new Reader(resource, readCount);
        Reader reader2 = new Reader(resource, readCount);
        Writer writer1 = new Writer(resource, writeCount);
        Writer writer2 = new Writer(resource, writeCount);

        reader1.setName("Reader-1");
        reader2.setName("Reader-2");
        writer1.setName("Writer-1");
        writer2.setName("Writer-2");

        reader1.start();
        reader2.start();
        writer1.start();
        writer2.start();

        try {
            reader1.join();
            reader2.join();
            writer1.join();
            writer2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Readers and Writers have finished their tasks.");
    }
}
