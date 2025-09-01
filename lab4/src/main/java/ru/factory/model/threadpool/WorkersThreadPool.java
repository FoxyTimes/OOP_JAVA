package ru.factory.model.threadpool;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class WorkersThreadPool {
    private final BlockingQueue<Runnable> taskQueue;
    private final ArrayList<WorkerThread> threads;
    private volatile boolean isShutdown;

    public WorkersThreadPool(int numberOfThreads) {
        if (numberOfThreads <= 0) {
            throw new IllegalArgumentException("Number of threads must be positive");
        }

        this.taskQueue = new LinkedBlockingQueue<>();
        this.threads = new ArrayList<>(numberOfThreads);
        this.isShutdown = false;

        // Создаем и запускаем рабочие потоки
        for (int i = 0; i < numberOfThreads; i++) {
            WorkerThread worker = new WorkerThread("WorkerThread-" + i);
            worker.start();
            threads.add(worker);
        }
    }

    public void execute(Runnable task) {
        if (task == null) {
            throw new NullPointerException("Task cannot be null");
        }
        if (isShutdown) {
            return;
        }

        try {
            taskQueue.put(task);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void shutdown() {
        isShutdown = true;
        for (WorkerThread thread : threads) {
            thread.interrupt();
        }
    }

    public void awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long endTime = System.currentTimeMillis() + unit.toMillis(timeout);
        for (WorkerThread thread : threads) {
            long remainingTime = endTime - System.currentTimeMillis();
            if (remainingTime <= 0) {
                break;
            }
            thread.join(remainingTime);
        }
    }

    private class WorkerThread extends Thread {
        public WorkerThread(String name) {
            super(name);
            setDaemon(false);
        }

        @Override
        public void run() {
            while (!isShutdown || !taskQueue.isEmpty()) {
                try {
                    Runnable task = taskQueue.take();
                    task.run();
                } catch (InterruptedException e) {
                    if (isShutdown) {
                        Thread.currentThread().interrupt();
                    }
                } catch (Exception e) {
                    System.err.println("Error executing task in " + getName() + ": " + e.getMessage());
                }
            }
        }
    }
}
