package br.ufrn.dimap.middleware.remotting.impl;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import br.ufrn.dimap.middleware.remotting.interfaces.PollObject;

/**
 * Implements default PollObject for this invocation asynchrony pattern
 * @author victoragnez
 *
 */
public class DefaultPollObject implements PollObject {
	
	/*
	 * result from server
	 */
	private Object result;
	
	/**
	 * result type
	 */
	private Class<?> resultType;
	
	/**
	 * Error if occurred
	 */
	private RemoteError error;
	
	/**
	 * true if no error occurred
	 * false if a error occurred
	 */
	private Boolean receivedResult;
	
	/**
	 * Lock held by getBlockingResult
	 */
	private final ReentrantLock lock = new ReentrantLock();
	
	/*
	 * Condition to signal when receive a result
	 */
	private final Condition receive = lock.newCondition();
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.PollObject#resultAvailable()
	 */
	@Override
	public boolean resultAvailable() {
		return (receivedResult != null);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.PollObject#getResult()
	 */
	@Override
	public synchronized Object getResult() throws RemoteError {
		if(resultAvailable()) {
			if(receivedResult) {
				return result;
			}
			else {
				throw error;
			}
		}
		else {
			return null;
		}
	}
	
	@Override
	public Class<?> getResultType() {
		return this.resultType;
	}
	
	@Override
	public void setResultType(Class<?> resultType) {
		this.resultType = resultType;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.PollObject#getBlockingResult()
	 */
	@Override
	public Object getBlockingResult() throws InterruptedException, RemoteError {
		final ReentrantLock lock = this.lock;
		lock.lockInterruptibly();
		try {
            while(!resultAvailable()) {
                receive.await();
            }
            return getResult();
        } finally {
            lock.unlock();
        }
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.PollObject#storeResult(java.lang.Object)
	 */
	@Override
	public synchronized void storeResult(Object result) {
		if(receivedResult != null) {
			return;
		}
		receivedResult = true;
		this.result = result;
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			receive.signalAll();
		} finally {
			lock.unlock();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.PollObject#onError(br.ufrn.dimap.middleware.remotting.impl.RemoteError)
	 */
	public synchronized void onError(RemoteError error) {
		if(receivedResult != null) {
			return;
		}
		receivedResult = false;
		this.error = error;
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			receive.signalAll();
		} finally {
			lock.unlock();
		}
	}

}
