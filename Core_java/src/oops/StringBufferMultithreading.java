package oops;

class MyThread extends Thread {
    private StringBuffer stringBuffer;

    // Constructor to accept StringBuffer
    public MyThread(StringBuffer stringBuffer) {
        this.stringBuffer = stringBuffer;
    }

    @Override
    public void run() {
        // Each thread appends its name to the StringBuffer
        for (int i = 0; i < 10; i++) {
            stringBuffer.append(Thread.currentThread().getName() + " ");
            try {
                // Sleep for a short time to simulate some delay
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class StringBufferMultithreading {
    public static void main(String[] args) throws InterruptedException {
        // Creating a StringBuffer instance
        StringBuffer stringBuffer = new StringBuffer();

        // Creating multiple threads
        Thread t1 = new MyThread(stringBuffer);
        Thread t2 = new MyThread(stringBuffer);
        Thread t3 = new MyThread(stringBuffer);

        // Start the threads
        t1.start();
        t2.start();
        t3.start();

        // Waiting for all threads to finish
        t1.join();
        t2.join();
        t3.join();

        // Printing the result after all threads finish their operation
        System.out.println("Final StringBuffer content: " + stringBuffer.toString());
    }
}

