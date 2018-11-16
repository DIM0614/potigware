public interface Observer {
	public void started(Invocation invocation, long sizeInvocation, BasicRemotingPatterns pattern);
	public void done(Invocation invocation);
}
