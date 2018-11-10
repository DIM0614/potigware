package br.ufrn.middleware.async.interfaces;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

public interface OnErrorCallback {

	void invoke(RemoteError error);

}
