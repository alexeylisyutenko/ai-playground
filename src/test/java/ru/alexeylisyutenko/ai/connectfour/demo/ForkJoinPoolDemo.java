package ru.alexeylisyutenko.ai.connectfour.demo;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinPoolDemo {

    @Test
    void demo() throws InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        DemoRecursiveTask recursiveTask = new DemoRecursiveTask(1, 5000);
        forkJoinPool.submit(recursiveTask);

        new Thread(() -> {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            recursiveTask.cancel(false);
        }).start();

        System.out.println("Before join");
        try {
            recursiveTask.join();
            System.out.println("After join");
        } catch (CancellationException e) {
            e.printStackTrace();
        }

        Thread.sleep(10000);
    }

    private static class DemoRecursiveTask extends RecursiveTask<Void> {
        private final int count;
        private final long sleepMillis;

        public DemoRecursiveTask(int count, long sleepMillis) {
            this.count = count;
            this.sleepMillis = sleepMillis;
        }

        @Override
        protected Void compute() {
            if (count == 0) {
                System.out.println(String.format("DemoRecursiveTask %d: Waiting for %d ms...", count, sleepMillis));
                try {
                    long repeats = sleepMillis / 1000;
                    for (int i = 0; i < repeats; i++) {
                        System.out.println(String.format("DemoRecursiveTask %d: isCancelled: %b", count, isCancelled()));
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    System.out.println(String.format("DemoRecursiveTask %d: InterruptedException occurred while sleeping", count));
                    Thread.currentThread().interrupt();
                }
                System.out.println(String.format("DemoRecursiveTask %d: Finished waiting", count));
                return null;
            }

            DemoRecursiveTask demoRecursiveTask = new DemoRecursiveTask(count - 1, sleepMillis);
            System.out.println(String.format("DemoRecursiveTask %d: Forking new task...", count));
            demoRecursiveTask.fork();

            System.out.println(String.format("DemoRecursiveTask %d:: Joining new task...", count));
            try {
                demoRecursiveTask.join();
                System.out.println(String.format("DemoRecursiveTask %d:: join() finished", count));
            } catch (RuntimeException e) {
                System.out.println(String.format("DemoRecursiveTask %d:: join() threw exception: %s", count, e.toString()));
                return null;
            }

            return null;
        }
    }
}
