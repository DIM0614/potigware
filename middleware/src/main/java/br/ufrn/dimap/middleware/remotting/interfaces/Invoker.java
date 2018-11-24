package br.ufrn.dimap.middleware.remotting.interfaces;

import java.io.IOException;

import br.ufrn.dimap.middleware.remotting.impl.Invocation;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

public interface Invoker {
	public Object invoke(Invocation invocation) throws RemoteError, IOException,ClassNotFoundException;
}
