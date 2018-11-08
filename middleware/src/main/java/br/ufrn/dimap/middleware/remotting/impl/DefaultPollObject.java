package br.ufrn.dimap.middleware.remotting.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import br.ufrn.dimap.middleware.remotting.interfaces.PollObject;

/**
 * Implements default PollObject for this invocation asynchrony pattern
 * @author victoragnez
 *
 */
public class DefaultPollObject implements PollObject {
	
	/*
	 * Queue of server responses
	 */
	private final BlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.PollObject#resultAvailable()
	 */
	@Override
	public boolean resultAvailable() {
		return !queue.isEmpty();
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.PollObject#getResult()
	 */
	@Override
	public Object getResult() {
		return queue.poll();
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.PollObject#getBlockingResult()
	 */
	@Override
	public Object getBlockingResult() throws InterruptedException {
		return queue.take();
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.ufrn.dimap.middleware.remotting.interfaces.PollObject#storeResult(java.lang.Object)
	 */
	@Override
	public void storeResult(Object obj) {
		queue.add(obj);
	}

}
