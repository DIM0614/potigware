package br.ufrn.dimap.middleware.infrastructure.qos;

import br.ufrn.dimap.middleware.remotting.impl.Invocation;

public interface Observer {
	public void started(Invocation invocation, long sizeInvocation);
	public void done(Invocation invocation);
}
