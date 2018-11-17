package br.ufrn.dimap.middleware.infrastructure.qos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WritterLog extends Thread {
	private static final String FILENAME = "log-potigware-";
	private final Set<LogInvocation> logs;
	
	public WritterLog(Set<LogInvocation> logs){
		this.logs = ConcurrentHashMap.<LogInvocation>newKeySet();
		this.logs.addAll(logs);
	}
	
	public void run(){
		Instant now = Instant.now();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME+now.toString()))) {
			for (LogInvocation log : logs) {
				bw.write(log.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
