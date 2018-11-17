package br.ufrn.dimap.middleware.remotting.interfaces;

import java.io.File;

/**
 * Should be implemented by lookups for registering
 * new remote object classes in the naming service.
 *
 * @author vitorgreati
 */
public interface NamingInstaller {
    void install(String objName, final File interfaceFile, final File invokerFile);
}
