public interface Observer {
	public void started(Invocation invocation, BasicRemotingPatterns pattern);
	public void done(Invocation invocation);
}
