/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.resource.util.timeout;

import com.alipay.zdal.datasource.resource.util.ThrowableHandler;
import com.alipay.zdal.datasource.resource.util.threadpool.BasicThreadPool;
import com.alipay.zdal.datasource.resource.util.threadpool.BlockingMode;
import com.alipay.zdal.datasource.resource.util.threadpool.ThreadPool;

/**
 * The timeout factory.
 *
 * This is written with performance in mind. In case of <code>n</code>
 * active timeouts, creating, cancelling and firing timeouts all operate
 * in time <code>O(log(n))</code>.
 *
 * If a timeout is cancelled, the timeout is not discarded. Instead the
 * timeout is saved to be reused for another timeout. This means that if
 * no timeouts are fired, this class will eventually operate without
 * allocating anything on the heap.
 *
 * @author ²®ÑÀ
 * @version $Id: TimeoutFactory.java, v 0.1 2014-1-6 ÏÂÎç05:46:16 Exp $
 */
public class TimeoutFactory {
    //   private static final Logger    logger                = Logger.getLogger(TimeoutFactory.class);
    //  Code commented out with the mark "INV:" are runtime checks
    //  of invariants that are not needed for a production system.
    //  For problem solving, you can remove these comments.
    //  Multithreading notes:
    //
    //  While a TimeoutImpl is enqueued, its index field contains the index
    //  of the instance in the queue; that is, for 1 <= n <= size,
    //  q[n].index = n.
    //  Modifications of an enqueued TimeoutImpl instance may only happen
    //  in code synchronized on the TimeoutFactory instance that has it
    //  enqueued.
    //  Modifications on the priority queue may only happen while running in
    //  code synchronized on the TimeoutFactory instance that holds the queue.
    //  When a TimeoutImpl instance is no longer enqueued, its index field
    //  changes to one of the negative constants declared in the TimeoutImpl
    //  class.
    //  When a TimeoutImpl is not in use, its index field is TimeoutImpl.DONE
    //  and it is on the freeList.
    //
    //  Cancellation may race with the timeout.
    //  To avoid problems with this, the TimeoutImpl index field is set to
    //  TimeoutImpl.TIMEOUT when the TimeoutImpl is taken out of the queue.
    //  Finally the index field is set to TimeoutImpl.DONE, and
    //  the TimeoutImpl instance is discarded.

    // Static --------------------------------------------------------

    /** Our singleton instance */
    private static TimeoutFactory  singleton;

    /** Number of TimeoutFactories created */
    private static int             timeoutFactoriesCount = 0;

    /** The default threadpool used to execute timeouts */
    private static BasicThreadPool DEFAULT_TP            = new BasicThreadPool("Timeouts");
    static {
        DEFAULT_TP.setBlockingMode(BlockingMode.RUN);
    }

    /** Lazy constructions of the TimeoutFactory singleton */
    private synchronized static TimeoutFactory getSingleton() {
        if (singleton == null) {
            singleton = new TimeoutFactory(DEFAULT_TP);
        }
        return singleton;
    }

    // Private data --------------------------------------------------

    /** Used for graceful exiting */
    private boolean       cancelled;

    /** The daemon thread that dequeues timeouts tasks and issues
        them for execution to the thread pool */
    private Thread        workerThread;

    /** Per TimeoutFactory thread pool used to execute timeouts */
    private ThreadPool    threadPool;

    /** Linked list of free TimeoutImpl instances. */
    private TimeoutImpl   freeList;

    /** The size of the timeout queue. */
    private int           size;

    /**
     *  Our priority queue.
     *
     *  This is a balanced binary tree. If nonempty, the root is at index 1,
     *  and all nodes are at indices 1..size. Nodes with index greater than
     *  size are null. Index 0 is never used.
     *  Children of the node at index <code>j</code> are at <code>j*2</code>
     *  and <code>j*2+1</code>. The children of a node always fire the timeout
     *  no earlier than the node.
     *
     *
     *  Or, more formally:
     *
     *  Only indices <code>1</code>..<code>size</code> of this array are used.
     *  All other indices contain the null reference.
     *  This array represent a balanced binary tree.
     *
     *  If <code>size</code> is <code>0</code> the tree is empty, otherwise
     *  the root of the tree is at index <code>1</code>.
     *
     *  Given an arbitrary node at index <code>n</code> that is not the root
     *  node, the parent node of <code>n</code> is at index <code>n/2</code>.
     *
     *  Given an arbitrary node at index <code>n</code>; if
     *  <code>2*n <= size</code> the node at <code>n</code> has its left child
     *  at index <code>2*n</code>, otherwise the node at <code>n</code> has
     *  no left child.
     *
     *  Given an arbitrary node at index <code>n</code>; if
     *  <code>2*n+1 <= size</code> the node at <code>n</code> has its right child
     *  at index <code>2*n+1</code>, otherwise the node at <code>n</code> has
     *  no right child.
     *
     *  The priority function is called T. Given a node <code>n</code>,
     *  <code>T(n)</code> denotes the absolute time (in milliseconds since
     *  the epoch) that the timeout for node <code>n</code> should happen.
     *  Smaller values of <code>T</code> means higher priority.
     *
     *  The tree satisfies the following invariant:
     *  <i>
     *  For any node <code>n</code> in the tree:
     *  If node <code>n</code> has a left child <code>l</code>,
     *  <code>T(n) <= T(l)</code>.
     *  If node <code>n</code> has a right child <code>r</code>,
     *  <code>T(n) <= T(r)</code>.
     *  </i>
     *
     *
     *  The invariant may be temporarily broken while executing synchronized
     *  on <code>this</code> instance, but is always reestablished before
     *  leaving the synchronized code.
     *
     *  The node at index <code>1</code> is always the first node to timeout,
     *  as can be deduced from the invariant.
     *
     *  For the following algorithm pseudocode, the operation
     *  <code>swap(n,m)</code> denotes the exchange of the nodes at indices
     *  <code>n</code> and <code>m</code> in the tree.
     *
     *  Insertion of a new node happend as follows:
     *  <pre>
     *    IF size = q.length THEN
     *      "expand q array to be larger";
     *    ENDIF
     *    size <- size + 1;
     *    q[size] <- "new node";
     *    n <- size;
     *    WHILE n > 1 AND T(n/2) > T(n) DO
     *      swap(n/2, n);
     *      n <- n/2;
     *    ENDWHILE
     *  </pre>
     *  Proof that this insertion algorithm respects the invariant is left to
     *  the interested reader.
     *
     *  The removal algorithm is a bit more complicated. To remove the node
     *  at index <code>n</code>:
     *  <pre>
     *    swap(n, size);
     *    size <- size - 1;
     *    IF n > 1 AND T(n/2) > T(n) THEN
     *      WHILE n > 1 AND T(n/2) > T(n) DO
     *        swap(n/2, n);
     *        n <- n/2;
     *      ENDWHILE
     *    ELSE
     *      WHILE 2*n <= size DO
     *        IF 2*n+1 <= size THEN
     *          // Both children present
     *          IF T(2*n) <= T(2*n+1) THEN
     *            IF T(n) <= T(2*n) THEN
     *              EXIT;
     *            ENDIF
     *            swap(n, 2*n);
     *            n <- 2*n;
     *          ELSE
     *            IF T(n) <= T(2*n+1) THEN
     *              EXIT;
     *            ENDIF
     *            swap(n, 2*n+1);
     *            n <- 2*n+1;
     *          ENDIF
     *        ELSE
     *          // Only left child, right child not present.
     *          IF T(n) <= T(2*n) THEN
     *            EXIT;
     *          ENDIF
     *          swap(n, 2*n);
     *          n <- 2*n;
     *        ENDIF
     *      ENDWHILE
     *    ENDIF
     *  </pre>
     *  Proof that this removal algorithm respects the invariant is left to
     *  the interested reader. Really, I am not going to prove it here.
     *
     *  If you are interested, you can find this data structure and its
     *  associated operations in most textbooks on algorithmics.
     *
     *  @see checkTree
     */
    private TimeoutImpl[] q;

    // Public API ----------------------------------------------------

    /**
     *  Schedules a new timeout using the singleton TimeoutFactory
     */
    static public Timeout createTimeout(long time, TimeoutTarget target) {
        return getSingleton().schedule(time, target);
    }

    /**
     * Constructs a new TimeoutFactory that uses the provided ThreadPool
     */
    public TimeoutFactory(ThreadPool threadPool) {
        this.threadPool = threadPool;
        q = new TimeoutImpl[16];
        freeList = null;
        size = 0;

        // setup the workerThread
        workerThread = new Thread("TimeoutFactory-" + timeoutFactoriesCount++) {
            @Override
            public void run() {
                doWork();
            }
        };
        workerThread.setDaemon(true);
        workerThread.start();
    }

    /**
     * Constructs a new TimeoutFactory that uses the default thread pool
     */
    public TimeoutFactory() {
        this(DEFAULT_TP);
    }

    /**
     * Schedules a new timeout.
     * 
     * @param time absolute time
     * @param target target to fire
     */
    public Timeout schedule(long time, TimeoutTarget target) {
        if (cancelled == true)
            throw new IllegalStateException("TimeoutFactory has been cancelled");
        if (time < 0)
            throw new IllegalArgumentException("Negative time");
        if (target == null)
            throw new IllegalArgumentException("Null timeout target");

        return newTimeout(time, target);
    }

    /**
     * Schedules a new timeout.
     * 
     * @param time absolute time
     * @param run runnable to run
     */
    public Timeout schedule(long time, Runnable run) {
        return schedule(time, new TimeoutTargetImpl(run));
    }

    /**
     * Cancels all submitted tasks, stops the worker
     * thread and clean-ups everything except for the
     * thread pool. Scheduling new timeouts after cancel
     * is called results in a IllegalStateException.
     */
    public void cancel() {
        // obviously the singleton TimeoutFactory cannot
        // be cancelled since its reference is not accessible

        // let the worker thread cleanup
        cancelled = true;

        // signal the worker if idle or waiting for the next timeout
        synchronized (this) {
            notify();
        }
    }

    /**
     * Returns true if the TimeoutFactory has been cancelled,
     * false if it is operational (i.e. accepts timeout schedules).
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     *  Swap two nodes in the tree.
     */
    private void swap(int a, int b) {
        // INV: assertExpr(a > 0);
        // INV: assertExpr(a <= size);
        // INV: assertExpr(b > 0);
        // INV: assertExpr(b <= size);
        // INV: assertExpr(q[a] != null);
        // INV: assertExpr(q[b] != null);
        // INV: assertExpr(q[a].index == a);
        // INV: assertExpr(q[b].index == b);
        TimeoutImpl temp = q[a];
        q[a] = q[b];
        q[a].index = a;
        q[b] = temp;
        q[b].index = b;
    }

    /**
     *  A new node has been added at index <code>index</code>.
     *  Normalize the tree by moving the new node up the tree.
     *
     *  @return True iff the tree was modified.
     */
    private boolean normalizeUp(int index) {
        // INV: assertExpr(index > 0);
        // INV: assertExpr(index <= size);
        // INV: assertExpr(q[index] != null);
        if (index == 1)
            return false; // at root
        boolean ret = false;
        long t = q[index].time;
        int p = index >> 1;
        while (q[p].time > t) {
            // INV: assertExpr(q[index].time == t);
            swap(p, index);
            ret = true;
            if (p == 1)
                break; // at root
            index = p;
            p >>= 1;
        }
        return ret;
    }

    /**
     *  Remove a node from the tree and normalize.
     *
     *  @return The removed node.
     */
    private TimeoutImpl removeNode(int index) {
        // INV: assertExpr(index > 0);
        // INV: assertExpr(index <= size);
        TimeoutImpl res = q[index];
        // INV: assertExpr(res != null);
        // INV: assertExpr(res.index == index);
        if (index == size) {
            --size;
            q[index] = null;
            return res;
        }
        swap(index, size); // Exchange removed node with last leaf node
        --size;
        // INV: assertExpr(res.index == size + 1);
        q[res.index] = null;
        if (normalizeUp(index))
            return res; // Node moved up, so it shouldn't move down
        long t = q[index].time;
        int c = index << 1;
        while (c <= size) {
            // INV: assertExpr(q[index].time == t);
            TimeoutImpl l = q[c];
            // INV: assertExpr(l != null);
            // INV: assertExpr(l.index == c);
            if (c + 1 <= size) {
                // two children, swap with smallest
                TimeoutImpl r = q[c + 1];
                // INV: assertExpr(r != null);
                // INV: assertExpr(r.index == c+1);
                if (l.time <= r.time) {
                    if (t <= l.time)
                        break; // done
                    swap(index, c);
                    index = c;
                } else {
                    if (t <= r.time)
                        break; // done
                    swap(index, c + 1);
                    index = c + 1;
                }
            } else { // one child
                if (t <= l.time)
                    break; // done
                swap(index, c);
                index = c;
            }
            c = index << 1;
        }
        return res;
    }

    /**
     *  Create a new timeout.
     */
    private synchronized Timeout newTimeout(long time, TimeoutTarget target) {
        // INV: checkTree();
        // INV: assertExpr(size < q.length);
        if (++size == q.length) {
            TimeoutImpl[] newQ = new TimeoutImpl[2 * q.length];
            System.arraycopy(q, 0, newQ, 0, q.length);
            q = newQ;
        }
        // INV: assertExpr(size < q.length);
        // INV: assertExpr(q[size] == null);
        TimeoutImpl timeout;
        if (freeList != null) {
            timeout = q[size] = freeList;
            freeList = timeout.nextFree;
            timeout.nextFree = null;
            // INV: checkFreeList();
            // INV: assertExpr(timeout.index == TimeoutImpl.DONE);
        } else
            timeout = q[size] = new TimeoutImpl();
        timeout.index = size;
        timeout.time = time;
        timeout.target = target;
        normalizeUp(size);
        if (timeout.index == 1)
            notify();
        // INV: checkTree();
        return timeout;
    }

    /**
     *  Cancel a timeout.
     */
    private boolean dropTimeout(TimeoutImpl timeout) {
        synchronized (this) {
            if (timeout.index > 0) {
                // Active timeout, remove it.
                // INV: assertExpr(q[timeout.index] == timeout);
                // INV: checkTree();
                removeNode(timeout.index);
                // INV: checkTree();
                timeout.index = TimeoutImpl.DONE;
                timeout.nextFree = freeList;
                freeList = timeout;
                // INV: checkFreeList();

                // execution cancelled
                return true;
            } else {
                // has already been executed (DONE) or
                // is currently executing (TIMEOUT)
                return false;
            }
        }
    }

    /**
     *  Timeout worker method.
     */
    private void doWork() {
        while (!cancelled) {
            TimeoutImpl work = null;
            // Look for work
            synchronized (this) {
                if (size == 0 && !cancelled) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                    }
                } else {
                    long now = System.currentTimeMillis();
                    if (q[1].time > now && !cancelled) {
                        try {
                            wait(q[1].time - now);
                        } catch (InterruptedException ex) {
                        }
                    }
                    if (size > 0 && q[1].time <= System.currentTimeMillis() && !cancelled) {
                        work = removeNode(1);
                        work.index = TimeoutImpl.TIMEOUT;
                    }
                }
            }
            // Do work, if any
            if (work != null) {
                // Wrap the TimeoutImpl with a runnable that invokes the target callback
                TimeoutWorker worker = new TimeoutWorker(work);
                try {
                    threadPool.run(worker);
                } catch (Throwable t) {
                    // protect the worker thread from pool enqueue errors
                    ThrowableHandler.add(ThrowableHandler.Type.ERROR, t);
                }
                synchronized (work) {
                    // maybe we should have a state POOL_ENQUEUE_FAILED
                    work.index = TimeoutImpl.DONE;
                }
            }
        }

        // TimeoutFactory was cancelled
        cleanup();
    }

    /**
     * Cleanup everything; threadPool needs stop()
     */
    private void cleanup() {
        // clean free list
        freeList = cleanupTimeoutImpl(freeList);

        // cleanup queue
        for (int i = 1; i <= size; i++) {
            q[i] = cleanupTimeoutImpl(q[i]);
        }
        q = null;

        threadPool = null;
        workerThread = null;
    }

    /**
     * Recursive cleanup of a TimeoutImpl
     * @return null
     */
    private TimeoutImpl cleanupTimeoutImpl(TimeoutImpl timeout) {
        if (timeout != null) {
            timeout.target = null;
            timeout.nextFree = cleanupTimeoutImpl(timeout.nextFree);
        }
        return null;
    }

    // Private Inner Classes -----------------------------------------

    /**
     *  Our private Timeout implementation.
     */
    private class TimeoutImpl implements Timeout {
        static final int DONE    = -1; // done, may be finalized or reused

        static final int TIMEOUT = -2; // target being called

        int              index;       // index in queue, or one of constants above.

        long             time;        // time to fire

        TimeoutTarget    target;      // target to fire at

        TimeoutImpl      nextFree;    // next on free list

        public boolean cancel() {
            return TimeoutFactory.this.dropTimeout(this);
        }
    }

    /**
     *  A runnable that fires the timeout callback
     */
    private static class TimeoutWorker implements Runnable {
        private final TimeoutImpl work;

        /**
         *  Create a new instance.
         *
         *  @param work The timeout that should be fired.
         */
        TimeoutWorker(TimeoutImpl work) {
            this.work = work;
        }

        /**
         *  Override to fire the timeout.
         */
        public void run() {
            try {
                work.target.timedOut(work);
            } catch (Throwable t) {
                // protect the thread pool thread from receiving this error
                ThrowableHandler.add(ThrowableHandler.Type.ERROR, t);
            }
            synchronized (work) {
                // maybe we should have a state EXECUTION_FAILED
                work.index = TimeoutImpl.DONE;
            }
        }
    }

    /**
     * Simple TimeoutTarget implementation that wraps a Runnable
     */
    private static class TimeoutTargetImpl implements TimeoutTarget {
        Runnable runnable;

        TimeoutTargetImpl(Runnable runnable) {
            this.runnable = runnable;
        }

        public void timedOut(Timeout ignored) {
            runnable.run();
        }
    }
}