package br.ufrn.dimap.middleware.remotting.impl;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.identification.lookup.Lookup;

import java.lang.reflect.InvocationTargetException;

public class ProxyCreator {

    private static Lookup lookup;

    public ProxyCreator() {
        lookup = null;//Middleware.getService("lookup") ;
    }

    public synchronized static <T> ClientProxy create(String objectName, Class<? extends ClientProxy> proxyClass) throws RemoteError, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // get AOR
        AbsoluteObjectReference aor = lookup.find(objectName);
        // make proxy
        return proxyClass.getConstructor(AbsoluteObjectReference.class).newInstance(aor);
    }

}
