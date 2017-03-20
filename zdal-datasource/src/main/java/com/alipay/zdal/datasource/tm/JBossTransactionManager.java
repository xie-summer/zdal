/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.tm;

import javax.transaction.InvalidTransactionException;

import com.alipay.zdal.datasource.tm.integrity.TransactionIntegrity;
import com.alipay.zdal.datasource.transaction.HeuristicMixedException;
import com.alipay.zdal.datasource.transaction.HeuristicRollbackException;
import com.alipay.zdal.datasource.transaction.NotSupportedException;
import com.alipay.zdal.datasource.transaction.RollbackException;
import com.alipay.zdal.datasource.transaction.SystemException;
import com.alipay.zdal.datasource.transaction.Transaction;
import com.alipay.zdal.datasource.transaction.TransactionManager;

/**
 * 
 * @author 伯牙
 * @version $Id: JBossTransactionManager.java, v 0.1 2014-1-6 下午05:46:55 Exp $
 */
public class JBossTransactionManager implements TransactionManager {

    public void setDefaultTransactionTimeout(int seconds) {
        getTxManager().setDefaultTransactionTimeout(seconds);
    }

    public void setGlobalIdsEnabled(boolean newValue) {
        getTxManager().setGlobalIdsEnabled(newValue);
    }

    public void setInterruptThreads(boolean interruptThreads) {
        getTxManager().setInterruptThreads(interruptThreads);
    }

    public void setTransactionIntegrity(TransactionIntegrity integrity) {
        getTxManager().setTransactionIntegrity(integrity);
    }

    public void setTransactionTimeout(int seconds) throws SystemException {
        getTxManager().setTransactionTimeout(seconds);
    }

    private TxManager getTxManager() {
        return TxManager.getInstance();
    }

    public void begin() throws NotSupportedException, SystemException {
        getTxManager().begin();

    }

    public void commit() throws RollbackException, HeuristicMixedException,
                        HeuristicRollbackException, SecurityException, IllegalStateException,
                        SystemException {
        getTxManager().commit();

    }

    public int getStatus() throws SystemException {
        return getTxManager().getStatus();
    }

    public Transaction getTransaction() throws SystemException {
        return getTxManager().getTransaction();
    }

    public void resume(Transaction transaction) throws InvalidTransactionException,
                                               IllegalStateException, SystemException {
        getTxManager().resume(transaction);

    }

    public void rollback() throws IllegalStateException, SecurityException, SystemException {
        getTxManager().rollback();

    }

    public void setRollbackOnly() throws IllegalStateException, SystemException {
        getTxManager().setRollbackOnly();

    }

    public Transaction suspend() throws SystemException {
        return getTxManager().suspend();
    }
}
