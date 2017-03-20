/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.tm;

import com.alipay.zdal.datasource.transaction.SystemException;
import com.alipay.zdal.datasource.transaction.Transaction;
import com.alipay.zdal.datasource.transaction.TransactionManager;

/**
 * A TransactionLocal is similar to ThreadLocal except it is keyed on the
 * Transactions. A transaction local variable is cleared after the transaction
 * completes.
 *
 * @author ²®ÑÀ
 * @version $Id: TransactionLocal.java, v 0.1 2014-1-6 ÏÂÎç05:48:09 Exp $
 */
public class TransactionLocal {

    /**
     * To simplify null values handling in the preloaded data pool we use
     * this value instead of 'null'
     */
    private static final Object        NULL_VALUE = new Object();

    /**
     * The transaction manager is maintained by the system and
     * manges the assocation of transaction to threads.
     */
    protected final TransactionManager transactionManager;

    /**
     * The delegate
     */
    protected TransactionLocalDelegate delegate;

    /**
     * Creates a transaction local variable. Using the given transaction manager
     *
     * @param tm the transaction manager
     */
    public TransactionLocal(TransactionManager tm) {
        if (tm == null)
            throw new IllegalArgumentException("Null transaction manager");
        this.transactionManager = tm;
        initDelegate();
    }

    /**
     * Lock the TransactionLocal using the current transaction<p>
     *
     * WARN: The current implemention just "locks the transactions"
     *
     * @throws IllegalStateException if the transaction is not active
     * @throws InterruptedException if the thread is interrupted
     */
    public void lock() throws InterruptedException {
        lock(getTransaction());
    }

    /**
     * Lock the TransactionLocal using the provided transaction<p>
     *
     * WARN: The current implemention just "locks the transactions"
     *
     * @param transaction the transaction
     * @throws IllegalStateException if the transaction is not active
     * @throws InterruptedException if the thread is interrupted
     */
    public void lock(Transaction transaction) throws InterruptedException {
        // ignore when there is no transaction
        if (transaction == null)
            return;

        delegate.lock(this, transaction);
    }

    /**
     * Unlock the TransactionLocal using the current transaction
     */
    public void unlock() {
        unlock(getTransaction());
    }

    /**
     * Unlock the ThreadLocal using the provided transaction
     *
     * @param transaction the transaction
     */
    public void unlock(Transaction transaction) {
        // ignore when there is no transaction
        if (transaction == null)
            return;

        delegate.unlock(this, transaction);
    }

    /**
     * Returns the initial value for this thransaction local.  This method
     * will be called once per accessing transaction for each TransactionLocal,
     * the first time each transaction accesses the variable with get or set.
     * If the programmer desires TransactionLocal variables to be initialized to
     * some value other than null, TransactionLocal must be subclassed, and this
     * method overridden. Typically, an anonymous inner class will be used.
     * Typical implementations of initialValue will call an appropriate
     * constructor and return the newly constructed object.
     *
     * @return the initial value for this TransactionLocal
     */
    protected Object initialValue() {
        return null;
    }

    /**
     * get the transaction local value.
     */
    protected Object getValue(Transaction tx) {
        return delegate.getValue(this, tx);
    }

    /**
     * put the value in the TransactionImpl map
     */
    protected void storeValue(Transaction tx, Object value) {
        delegate.storeValue(this, tx, value);
    }

    /**
     * does Transaction contain object?
     */
    protected boolean containsValue(Transaction tx) {
        return delegate.containsValue(this, tx);
    }

    /**
     * Returns the value of this TransactionLocal variable associated with the
     * thread context transaction. Creates and initializes the copy if this is
     * the first time the method is called in a transaction.
     *
     * @return the value of this TransactionLocal
     */
    public Object get() {
        return get(getTransaction());
    }

    /**
     * Returns the value of this TransactionLocal variable associated with the
     * specified transaction. Creates and initializes the copy if this is the
     * first time the method is called in a transaction.
     *
     * @param transaction the transaction for which the variable it to
     * be retrieved
     * @return the value of this TransactionLocal
     * @throws IllegalStateException if an error occures while registering
     * a synchronization callback with the transaction
     */
    public Object get(Transaction transaction) {
        if (transaction == null)
            return initialValue();

        Object value = getValue(transaction);

        // is we didn't get a value initalize this object with initialValue()
        if (value == null) {
            // get the initial value
            value = initialValue();

            // if value is null replace it with the null value standin
            if (value == null) {
                value = NULL_VALUE;
            }

            // store the value
            try {
                storeValue(transaction, value);
            } catch (IllegalStateException e) {
                // depending on the delegate implementation it may be considered an error to
                // call storeValue after the tx has ended. Further, the tx ending may have
                // caused the disposal of a previously stored initial value.
                // for user convenience we ignore such errors and return the initialvalue here.
                return initialValue();
            }
        }

        // if the value is the null standin return null
        if (value == NULL_VALUE) {
            return null;
        }

        // finall return the value
        return value;
    }

    /**
     * Sets the value of this TransactionLocal variable associtated with the
     * thread context transaction. This is only used to change the value from
     * the one assigned by the initialValue method, and many applications will
     * have no need for this functionality.
     *
     * @param value the value to be associated with the thread context
     * transactions's TransactionLocal
     */
    public void set(Object value) {
        set(getTransaction(), value);
    }

    /**
     * Sets the value of this TransactionLocal variable associtated with the
     * specified transaction. This is only used to change the value from
     * the one assigned by the initialValue method, and many applications will
     * have no need for this functionality.
     *
     * @param transaction the transaction for which the value will be set
     * @param value the value to be associated with the thread context
     * transactions's TransactionLocal
     */
    public void set(Transaction transaction, Object value) {
        if (transaction == null)
            throw new IllegalStateException("there is no transaction");
        // If this transaction is unknown, register for synchroniztion callback,
        // and call initialValue to give subclasses a chance to do some
        // initialization.
        if (!containsValue(transaction)) {
            initialValue();
        }

        // if value is null replace it with the null value standin
        if (value == null) {
            value = NULL_VALUE;
        }

        // finally store the value
        storeValue(transaction, value);
    }

    public Transaction getTransaction() {
        try {
            return transactionManager.getTransaction();
        } catch (SystemException e) {
            throw new IllegalStateException("An error occured while getting the "
                                            + "transaction associated with the current thread: "
                                            + e);
        }
    }

    /**
     * Initialise the delegate
     */
    protected void initDelegate() {
        if (transactionManager instanceof TransactionLocalDelegate)
            delegate = (TransactionLocalDelegate) transactionManager;
        else
            delegate = new TransactionLocalDelegateImpl(transactionManager);
    }
}
