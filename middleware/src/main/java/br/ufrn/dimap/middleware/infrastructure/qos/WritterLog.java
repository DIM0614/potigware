import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;


public class WritterLog extends Thread {
	private static final String FILENAME = "log-potigware-";
	private Set<LogInvocation> logs;
	
	public WritterLog(Set<LogInvocation> logs){
		this.logs = new HashSet<LogInvocation>(logs);
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
