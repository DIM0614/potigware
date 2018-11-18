package br.ufrn.dimap.middleware.remotting.interfaces;

import java.io.File;

/**
 * Should be implemented by lookups for registering
 * new remote object classes in the naming service.
 *
 * @author vitorgreati
 */
public interface NamingInstaller {
    void installBase(final File interfaceFile, final File invokerFile);
    void installImplementation(String objName, final File invokerFile);
}
