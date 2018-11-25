package br.ufrn.dimap.middleware.infrastructure.qos;

import java.time.Duration;
import java.time.LocalTime;

import br.ufrn.dimap.middleware.remotting.impl.Invocation;
import br.ufrn.dimap.middleware.remotting.impl.InvocationData;

public class LogInvocation {

	private String method;
	private Object[] parameters;
	private RemotingPatterns pattern;
	private LocalTime startTime;
	private LocalTime endTime;
	private long size;
	private boolean isAlive;

	public LogInvocation(Invocation invocation, RemotingPatterns pattern, long size){
		InvocationData invocationData = invocation.getInvocationData();
		this.method = invocationData.getOperationName();
		this.parameters = invocationData.getActualParams();
		this.pattern = pattern;
		this.startTime = LocalTime.now();
		this.size = size;
		this.isAlive = true;
	}

	public void done() {
		this.isAlive = false;
		this.endTime = LocalTime.now();
	}

	public boolean isAlive() {
		return isAlive;
	}

	@Override
	public String toString(){
		Duration duration = Duration.between(startTime, endTime);
		return "Method: " + this.method + "\n" +
			   "Parameters: " + this.parameters.toString() + "\n" +
			   "LocationPattern: " + this.pattern + "\n" +
			   "Start Time: " + this.startTime.toString() + "\n" +
			   "End Time: " + this.endTime.toString() + "\n" +
			   "Duration: " +  duration.getSeconds() + "seconds\n" +
			   "Size: " + this.size + "\n";
	}
}
