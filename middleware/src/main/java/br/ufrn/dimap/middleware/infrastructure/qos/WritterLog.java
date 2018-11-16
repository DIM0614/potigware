import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;


public class WritterLog extends Thread {
	private static final String FILENAME = "log-potigware-";
	private NavigableSet<LogInvocation> logs;
	
	public WritterLog(NavigableSet<LogInvocation> logs){
		this.logs = new ConcurrentSkipListSet<LogInvocation>(logs);
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
