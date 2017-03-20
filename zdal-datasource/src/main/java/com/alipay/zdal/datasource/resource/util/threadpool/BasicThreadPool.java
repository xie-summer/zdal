/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util.threadpool;

import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alipay.zdal.datasource.resource.util.collection.WeakValueHashMap;
import com.alipay.zdal.datasource.resource.util.concurrent.BoundedLinkedQueue;
import com.alipay.zdal.datasource.resource.util.concurrent.Heap;
import com.alipay.zdal.datasource.resource.util.concurrent.SynchronizedBoolean;
import com.alipay.zdal.datasource.resource.util.concurrent.SynchronizedInt;
import com.alipay.zdal.datasource.resource.util.concurrent.ThreadFactory;

/**
 * A basic thread pool.
 *
 * @author ²®ÑÀ
 * @version $Id: BasicThreadPool.java, v 0.1 2014-1-6 ÏÂÎç05:43:37 Exp $
 */
public class BasicThreadPool implements ThreadPool, BasicThreadPoolMBean {
    // Constants -----------------------------------------------------

    /** The jboss thread group */
    private static final ThreadGroup     JBOSS_THREAD_GROUP = new ThreadGroup(
                                                                "JBoss Pooled Threads");

    /** The thread groups */
    private static final Map             threadGroups       = Collections
                                                                .synchronizedMap(new WeakValueHashMap());

    /** The internal pool number */
    private static final SynchronizedInt lastPoolNumber     = new SynchronizedInt(0);

    private static Logger                log                = Logger
                                                                .getLogger(BasicThreadPool.class);

    // Attributes ----------------------------------------------------

    /** The thread pool name */
    private String                       name;

    /** The internal pool number */
    private final int                    poolNumber;

    /** The blocking mode */
    private BlockingMode                 blockingMode       = BlockingMode.ABORT;

    /** The pooled executor */
    private final MinPooledExecutor      executor;

    /** The queue */
    private final BoundedLinkedQueue     queue;

    /** The thread group */
    private ThreadGroup                  threadGroup;

    /** The last thread number */
    private final SynchronizedInt        lastThreadNumber   = new SynchronizedInt(0);

    /** Has the pool been stopped? */
    private final SynchronizedBoolean    stopped            = new SynchronizedBoolean(false);
    /** The Heap<TimeoutInfo> of tasks ordered by their completion timeout */
    private final Heap                   tasksWithTimeouts  = new Heap(13);
    /** The task completion timeout monitor runnable */
    private TimeoutMonitor               timeoutTask;

    // Static --------------------------------------------------------

    // Constructors --------------------------------------------------

    /**
     * Constructs a new thread pool
     */
    public BasicThreadPool() {
        this("ThreadPool");
    }

    /**
     * Constructs a new thread pool with a default queue size of 1024, max pool
     * size of 100, min pool size of 100, and a keep alive of 60 seconds.
     *
     * @param name the pool name
     */
    public BasicThreadPool(String name) {
        this(name, JBOSS_THREAD_GROUP);
    }

    /**
     * Constructs a new thread pool with a default queue size of 1024, max pool
     * size of 100, min pool size of 100, and a keep alive of 60 seconds.
     *
     * @param name the pool name
     * @param threadGroup threadGroup
     */
    public BasicThreadPool(String name, ThreadGroup threadGroup) {
        ThreadFactory factory = new ThreadPoolThreadFactory();

        queue = new BoundedLinkedQueue(1024);

        executor = new MinPooledExecutor(queue, 100);
        executor.setMinimumPoolSize(100);
        executor.setKeepAliveTime(60 * 1000);
        executor.setThreadFactory(factory);
        executor.abortWhenBlocked();

        poolNumber = lastPoolNumber.increment();
        setName(name);
        this.threadGroup = threadGroup;
    }

    // Public --------------------------------------------------------

    // ThreadPool ----------------------------------------------------

    public void stop(boolean immediate) {
        if (log.isDebugEnabled()) {
            log.debug("stop, immediate=" + immediate);
        }
        stopped.set(true);
        if (immediate) {
            executor.shutdownNow();
        } else {
            executor.shutdownAfterProcessingCurrentlyQueuedTasks();
        }
    }

    public void waitForTasks() throws InterruptedException {
        executor.awaitTerminationAfterShutdown();
    }

    public void waitForTasks(long maxWaitTime) throws InterruptedException {
        executor.awaitTerminationAfterShutdown(maxWaitTime);
    }

    public void runTaskWrapper(TaskWrapper wrapper) {
        if (log.isDebugEnabled()) {
            log.debug("runTaskWrapper, wrapper=" + wrapper);
        }

        if (stopped.get()) {
            wrapper.rejectTask(new ThreadPoolStoppedException("Thread pool has been stopped"));
            return;
        }

        wrapper.acceptTask();

        long completionTimeout = wrapper.getTaskCompletionTimeout();
        TimeoutInfo info = null;
        if (completionTimeout > 0) {
            checkTimeoutMonitor();
            // Install the task in the
            info = new TimeoutInfo(wrapper, completionTimeout);
            tasksWithTimeouts.insert(info);
        }
        int waitType = wrapper.getTaskWaitType();
        switch (waitType) {
            case Task.WAIT_FOR_COMPLETE: {
                executeOnThread(wrapper);
                break;
            }
            default: {
                execute(wrapper);
            }
        }
        waitForTask(wrapper);
    }

    public void runTask(Task task) {
        BasicTaskWrapper wrapper = new BasicTaskWrapper(task);
        runTaskWrapper(wrapper);
    }

    public void run(Runnable runnable) {
        run(runnable, 0, 0);
    }

    public void run(Runnable runnable, long startTimeout, long completeTimeout) {
        RunnableTaskWrapper wrapper = new RunnableTaskWrapper(runnable, startTimeout,
            completeTimeout);
        runTaskWrapper(wrapper);
    }

    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }

    // ThreadPoolMBean implementation --------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoolNumber() {
        return poolNumber;
    }

    public String getThreadGroupName() {
        return threadGroup.getName();
    }

    public void setThreadGroupName(String threadGroupName) {
        ThreadGroup group;
        synchronized (threadGroups) {
            group = (ThreadGroup) threadGroups.get(threadGroupName);
            if (group == null) {
                group = new ThreadGroup(JBOSS_THREAD_GROUP, threadGroupName);
                threadGroups.put(threadGroupName, group);
            }
        }
        threadGroup = group;
    }

    public int getQueueSize() {
        return queue.size();
    }

    public int getMaximumQueueSize() {
        return queue.capacity();
    }

    public void setMaximumQueueSize(int size) {
        queue.setCapacity(size);
    }

    public int getPoolSize() {
        return executor.getPoolSize();
    }

    public int getMinimumPoolSize() {
        return executor.getMinimumPoolSize();
    }

    public void setMinimumPoolSize(int size) {
        synchronized (executor) {
            executor.setKeepAliveSize(size);
            // Don't let the min size > max size
            if (executor.getMaximumPoolSize() < size) {
                executor.setMinimumPoolSize(size);
                executor.setMaximumPoolSize(size);
            }
        }
    }

    public int getMaximumPoolSize() {
        return executor.getMaximumPoolSize();
    }

    public void setMaximumPoolSize(int size) {
        synchronized (executor) {
            executor.setMinimumPoolSize(size);
            executor.setMaximumPoolSize(size);
            // Don't let the min size > max size
            if (executor.getKeepAliveSize() > size)
                executor.setKeepAliveSize(size);
        }
    }

    public long getKeepAliveTime() {
        return executor.getKeepAliveTime();
    }

    public void setKeepAliveTime(long time) {
        executor.setKeepAliveTime(time);
    }

    public BlockingMode getBlockingMode() {
        return blockingMode;
    }

    public void setBlockingMode(BlockingMode mode) {
        blockingMode = mode;

        if (blockingMode == BlockingMode.RUN) {
            executor.runWhenBlocked();
        } else if (blockingMode == BlockingMode.WAIT) {
            executor.waitWhenBlocked();
        } else if (blockingMode == BlockingMode.DISCARD) {
            executor.discardWhenBlocked();
        } else if (blockingMode == BlockingMode.DISCARD_OLDEST) {
            executor.discardOldestWhenBlocked();
        } else if (blockingMode == BlockingMode.ABORT) {
            executor.abortWhenBlocked();
        } else {
            throw new IllegalArgumentException("Failed to recognize mode: " + mode);
        }
    }

    /**
     * For backward compatibility with the previous string based mode
     * @param name - the string form of the mode enum
     */
    public void setBlockingMode(String name) {
        blockingMode = BlockingMode.toBlockingMode(name);
        if (blockingMode == null)
            blockingMode = BlockingMode.ABORT;
    }

    public ThreadPool getInstance() {
        return this;
    }

    public void stop() {
        stop(false);
    }

    // Object overrides ----------------------------------------------

    @Override
    public String toString() {
        return name + '(' + poolNumber + ')';
    }

    // Package protected ---------------------------------------------

    // Protected -----------------------------------------------------

    /**
     * Execute a task on the same thread
     *
     * @param wrapper the task wrapper
     */
    protected void executeOnThread(TaskWrapper wrapper) {
        if (log.isDebugEnabled()) {
            log.debug("executeOnThread, wrapper=" + wrapper);
        }

        wrapper.run();
    }

    /**
     * Execute a task
     *
     * @param wrapper the task wrapper
     */
    protected void execute(TaskWrapper wrapper) {
        if (log.isDebugEnabled()) {
            log.debug("execute, wrapper=" + wrapper);
        }

        try {
            executor.execute(wrapper);
        } catch (Throwable t) {
            wrapper.rejectTask(new ThreadPoolFullException(t.toString()));
        }
    }

    /**
     * Wait for a task
     *
     * @param wrapper the task wrapper
     */
    protected void waitForTask(TaskWrapper wrapper) {
        wrapper.waitForTask();
    }

    /**
     * Used to lazily create the task completion timeout thread and monitor
     */
    protected synchronized void checkTimeoutMonitor() {
        if (timeoutTask == null) {
            timeoutTask = new TimeoutMonitor(name, log);
        }
    }

    protected TimeoutInfo getNextTimeout() {
        TimeoutInfo info = (TimeoutInfo) this.tasksWithTimeouts.extract();
        return info;
    }

    // Private -------------------------------------------------------

    // Inner classes -------------------------------------------------

    /**
     * A factory for threads
     */
    private class ThreadPoolThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable runnable) {
            String threadName = BasicThreadPool.this.toString() + "-"
                                + lastThreadNumber.increment();
            Thread thread = new Thread(threadGroup, runnable, threadName);
            thread.setDaemon(true);
            return thread;
        }
    }

    /** An encapsulation of a task and its completion timeout
     */
    private static class TimeoutInfo implements Comparable {
        long        start;
        long        timeoutMS;
        TaskWrapper wrapper;
        boolean     firstStop;

        TimeoutInfo(TaskWrapper wrapper, long timeout) {
            this.start = System.currentTimeMillis();
            this.timeoutMS = start + timeout;
            this.wrapper = wrapper;
        }

        public void setTimeout(long timeout) {
            this.start = System.currentTimeMillis();
            this.timeoutMS = start + timeout;
        }

        /** Order TimeoutInfo based on the timestamp at which the task needs to
         * be completed by.
         * @param o a TimeoutInfo
         * @return the diff between this timeoutMS and the argument timeoutMS
         */
        public int compareTo(Object o) {
            TimeoutInfo ti = (TimeoutInfo) o;
            long to0 = timeoutMS;
            long to1 = ti.timeoutMS;
            int diff = (int) (to0 - to1);
            return diff;
        }

        TaskWrapper getTaskWrapper() {
            return wrapper;
        }

        public long getTaskCompletionTimeout() {
            return wrapper.getTaskCompletionTimeout();
        }

        /** Get the time remaining to the complete timeout timestamp in MS.
         * @param now - the current System.currentTimeMillis value
         * @return the time remaining to the complete timeout timestamp in MS.
         */
        public long getTaskCompletionTimeout(long now) {
            return timeoutMS - now;
        }

        /** Invoke stopTask on the wrapper and indicate whether this was the first
         * time the task has been notified to stop.
         * @return true if this is the first stopTask, false on the second.
         */
        public boolean stopTask() {
            wrapper.stopTask();
            boolean wasFirstStop = firstStop == false;
            firstStop = true;
            return wasFirstStop;
        }
    }

    /**
     * The monitor runnable which validates that threads are completing within
     * the task completion timeout limits.
     */
    private class TimeoutMonitor implements Runnable {
        final Logger log;

        TimeoutMonitor(String name, Logger log) {
            this.log = log;
            Thread t = new Thread(this, name + " TimeoutMonitor");
            t.setDaemon(true);
            t.start();
        }

        /** The monitor thread loops until the pool is shutdown. It waits for
         * tasks with completion timeouts and sleeps until the next completion
         * timeout and then interrupts the associated task thread, and invokes
         * stopTask on the TaskWrapper. A new timeout check is then inserted with
         * a 1 second timeout to validate that the TaskWrapper has exited the
         * run method. If it has not, then the associated task thread is stopped
         * using the deprecated Thread.stop method since this is the only way to
         * abort a thread that is in spin loop for example.
         * 
         * @todo this is not responsive to new tasks with timeouts smaller than
         * the current shortest completion expiration. We probably should interrupt
         * the thread on each insertion into the timeout heap to ensure better
         * responsiveness.
         */
        public void run() {
            boolean isStopped = stopped.get();
            while (isStopped == false) {
                try {
                    TimeoutInfo info = getNextTimeout();
                    if (info != null) {
                        long now = System.currentTimeMillis();
                        long timeToTimeout = info.getTaskCompletionTimeout(now);
                        if (timeToTimeout > 0) {
                            if (log.isDebugEnabled()) {
                                log.debug("Will check wrapper=" + info.getTaskWrapper() + " after "
                                          + timeToTimeout);
                            }
                            Thread.sleep(timeToTimeout);
                        }
                        // Check the status of the task
                        TaskWrapper wrapper = info.getTaskWrapper();
                        if (wrapper.isComplete() == false) {
                            if (log.isDebugEnabled()) {
                                log.debug("Failed completion check for wrapper=" + wrapper);
                            }

                            if (info.stopTask() == true) {
                                // Requeue the TimeoutInfo to see that the task exits run
                                info.setTimeout(1000);
                                tasksWithTimeouts.insert(info);
                                if (log.isDebugEnabled()) {
                                    log
                                        .debug("Rescheduled completion check for wrapper="
                                               + wrapper);
                                }
                            }
                        }
                    } else {
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    if (log.isDebugEnabled()) {
                        log.debug("Timeout monitor has been interrupted", e);
                    }
                } catch (Throwable e) {
                    if (log.isDebugEnabled()) {
                        log.debug("Timeout monitor saw unexpected error", e);
                    }
                }
                isStopped = stopped.get();
            }
        }
    }
}
