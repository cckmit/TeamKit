package org.team4u.base.debug;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class Benchmark {

    private Status status = new Status();
    private LinkedList<Status> snapshotList = new LinkedList<Status>();
    private long snapshotInterval = 1000;
    private long statInterval = 10000;

    private boolean printError = false;

    private void initTimer() {
        Timer timer = new Timer("BenchmarkTimerThread", true);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                snapshotList.add(status.createSnapshot());
                if (snapshotList.size() > 10) {
                    snapshotList.removeFirst();
                }
            }
        }, snapshotInterval, snapshotInterval);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                show();
            }
        }, statInterval, statInterval);
    }

    public void show() {
        if (snapshotList.size() >= 10) {
            show(snapshotList.getFirst(), snapshotList.getLast());
        }
    }

    private void show(Status begin, Status end) {
        long count = end.successCount.get() - begin.successCount.get();
        long tps = (long) ((count / (double) (end.snapshotTime - begin.snapshotTime)) * 1000);
        double averageRT = (end.successTimeTotal.get() - begin.successTimeTotal.get()) / (double) count;

        System.out.printf("TPS: %d Average RT: %7.3f Max RT: %d Success: %d Failed: %d\n",
                tps,
                averageRT,
                status.maxRt.get(),
                status.totalSuccessCount.get(),
                status.failedCount.get()
        );
    }

    public Benchmark start(Runnable worker) {
        return start(5, worker, 0);
    }

    public Benchmark start(int threadCount, Runnable worker) {
        return start(threadCount, worker, 0);
    }

    public Benchmark start(int threadCount, final Runnable worker, final int maxRunningTime) {
        ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        initTimer();

        final long startTime = System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    while (maxRunningTime <= 0 || System.currentTimeMillis() - startTime < maxRunningTime) {
                        try {
                            long startTime = System.currentTimeMillis();
                            worker.run();
                            long endTime = System.currentTimeMillis();
                            status.success(endTime - startTime);
                        } catch (Exception e) {
                            if (printError) {
                                e.printStackTrace();
                            }
                            status.failedCount.incrementAndGet();
                        }
                    }

                    countDownLatch.countDown();
                }
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        show();

        return this;
    }

    public long getSnapshotInterval() {
        return snapshotInterval;
    }

    public void setSnapshotInterval(long snapshotInterval) {
        this.snapshotInterval = snapshotInterval;
    }

    public long getStatInterval() {
        return statInterval;
    }

    public void setStatInterval(long statInterval) {
        this.statInterval = statInterval;
    }

    public boolean isPrintError() {
        return printError;
    }

    public void setPrintError(boolean printError) {
        this.printError = printError;
    }


    private class Status {

        AtomicLong maxRt = new AtomicLong(0L);

        AtomicLong successCount = new AtomicLong(0L);

        AtomicLong totalSuccessCount = new AtomicLong(0L);

        AtomicLong failedCount = new AtomicLong(0L);

        AtomicLong successTimeTotal = new AtomicLong(0L);

        long snapshotTime;

        public void compareMaxRT(long currentRT) {
            long prevMaxRT = maxRt.get();
            while (currentRT > prevMaxRT) {
                boolean updated = maxRt.compareAndSet(prevMaxRT, currentRT);

                if (updated) {
                    break;
                }

                prevMaxRT = maxRt.get();
            }
        }

        public Status createSnapshot() {
            Status s = new Status();
            s.maxRt = new AtomicLong(maxRt.get());
            s.successCount = new AtomicLong(successCount.get());
            s.failedCount = new AtomicLong(failedCount.get());
            s.successTimeTotal = new AtomicLong(successTimeTotal.get());
            s.totalSuccessCount = new AtomicLong(totalSuccessCount.get());
            s.snapshotTime = System.currentTimeMillis();
            return s;
        }

        public void success(long duration) {
            successCount.incrementAndGet();
            successTimeTotal.addAndGet(duration);
            totalSuccessCount.incrementAndGet();
            compareMaxRT(duration);
        }

        public AtomicLong getMaxRt() {
            return maxRt;
        }

        public void setMaxRt(AtomicLong maxRt) {
            this.maxRt = maxRt;
        }

        public AtomicLong getSuccessCount() {
            return successCount;
        }

        public void setSuccessCount(AtomicLong successCount) {
            this.successCount = successCount;
        }

        public AtomicLong getFailedCount() {
            return failedCount;
        }

        public void setFailedCount(AtomicLong failedCount) {
            this.failedCount = failedCount;
        }

        public AtomicLong getSuccessTimeTotal() {
            return successTimeTotal;
        }

        public void setSuccessTimeTotal(AtomicLong successTimeTotal) {
            this.successTimeTotal = successTimeTotal;
        }

        public long getSnapshotTime() {
            return snapshotTime;
        }

        public void setSnapshotTime(long snapshotTime) {
            this.snapshotTime = snapshotTime;
        }

        public AtomicLong getTotalSuccessCount() {
            return totalSuccessCount;
        }

        public void setTotalSuccessCount(AtomicLong totalSuccessCount) {
            this.totalSuccessCount = totalSuccessCount;
        }
    }
}
