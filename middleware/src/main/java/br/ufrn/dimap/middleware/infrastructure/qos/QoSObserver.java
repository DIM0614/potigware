package br.ufrn.dimap.middleware.infrastructure.qos;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import br.ufrn.dimap.middleware.remotting.impl.Invocation;

public class QoSObserver implements Observer {

	private static final int maxSizeSet = 1000;
	private final ConcurrentMap<Invocation, LogInvocation> map;
	private final Set<LogInvocation> set;

	public QoSObserver(){
		this.map = new ConcurrentHashMap<Invocation, LogInvocation>();
		this.set = ConcurrentHashMap.<LogInvocation>newKeySet();
	}

	@Override
	public synchronized void started(Invocation invocation, RemotingPatterns pattern) {
		long size = estimatedSize(Invocation invocation);
		LogInvocation log = new LogInvocation(invocation, pattern, size);
		map.put(invocation, log);
	}

	public long estimatedSize(Invocation invocation){
		InvocationData invocationData = invocation.getInvocationData();
		long operationName = (long) invocationData.getOperationName().getBytes().length;
		long size = operationName;
		Object[] actualParams = invocationData.getActualParams();
		for(Object param: actualParams) {
			if(param instanceof Double){
				size += 8;
			}
			if(param instanceof Float){
				size += 4;
			}
			if(param instanceof Integer){
				size += 4;
			}
			if(param instanceof Long){
				size += 8;
			}
			if(param instanceof Short){
				size += 2;
			}
			if (param instanceof String) {
				size += param.getBytes().length;
			}
    }
		return operationName;
	}

	@Override
	public synchronized void done(Invocation invocation){
		LogInvocation log = map.get(invocation);
		if(log.isAlive()){
			log.done();
			set.add(log);
			map.remove(invocation);
		}

		if(set.size() == maxSizeSet){
			writeLogs();
			set.clear();
		}
	}

	private void writeLogs(){
		WritterLog wl = new WritterLog(set);
        wl.start();
	}

}
