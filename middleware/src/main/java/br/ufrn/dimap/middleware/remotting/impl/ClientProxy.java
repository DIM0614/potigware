package br.ufrn.dimap.middleware.remotting.impl;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;

public abstract class ClientProxy {

    protected AbsoluteObjectReference absoluteObjectReference;

    public ClientProxy(AbsoluteObjectReference absoluteObjectReference) {
        this.absoluteObjectReference = absoluteObjectReference;
    }

}
