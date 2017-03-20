/**
 * 
 */
package com.alipay.zdal.test.ut.datasource;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:xiang.yangx@alipay.com">Yang Xiang</a>
 *
 */
public class ExceptionChecker extends Thread {

	protected ConcurrentHashMap<Long, List<Exception>> exceptionCollection;
	
	protected boolean running = true;
	
	protected int interval;
	
	protected Logger logger;
	
	public ExceptionChecker(ConcurrentHashMap<Long, List<Exception>> exceptionCollection,
			int interval, Logger logger){
		this.exceptionCollection = exceptionCollection;
		this.interval = interval;
		this.logger = logger;
	}
	
	public void run(){
		while( running ){
			printlnexceptionCollection(exceptionCollection);
			try {
				sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void printlnexceptionCollection(
			ConcurrentHashMap<Long, List<Exception>> exceptionCollection2) {
		for( Entry<Long, List<Exception>> excepEntry : exceptionCollection2.entrySet() ){
			if( null != excepEntry && null != excepEntry.getValue() && !excepEntry.getValue().isEmpty()){
				logger.debug("Thread " + excepEntry.getKey() + " Exception numbers " + excepEntry.getValue().size());
			}
		}
	}

	public void stopRunning(){
		running = true;
	}
}
