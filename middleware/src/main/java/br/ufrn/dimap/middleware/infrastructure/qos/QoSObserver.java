import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class QoSObserver implements Observer {
	
	private static Instrumentation instrumentation;  
	private static final int maxSizeSet = 1000;
	private Map<Invocation, LogInvocation> map;
	private Set<LogInvocation> set;
	
	public QoSObserver(){
		map = new HashMap<Invocation, LogInvocation>();
		set = new HashSet<LogInvocation>();
	}
	
	@Override
	public synchronized void started(Invocation invocation, BasicRemotingPatterns pattern) {
		long size = instrumentation.getObjectSize(invocation);
		LogInvocation log = new LogInvocation(invocation, pattern, size);
		map.put(invocation, log);	
	}

	@Override
	public synchronized void done(Invocation invocation) throws RuntimeException{
		LogInvocation log = map.get(invocation);
		if(log.isAlive()){
			log.done();
			set.add(log);
			map.remove(invocation);
		}
		else{
			throw new RuntimeException("Invocation cannot finish an object that is already dead.");
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
