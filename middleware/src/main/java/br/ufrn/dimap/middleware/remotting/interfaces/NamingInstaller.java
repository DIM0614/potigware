package br.ufrn.dimap.middleware.remotting.interfaces;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import br.ufrn.dimap.middleware.remotting.impl.DeploymentDescriptor;

/**
 * Should be implemented by lookups for registering
 * new remote object classes in the naming service.
 *
 * @author vitorgreati
 */
public interface NamingInstaller {
    void install(final DeploymentDescriptor deploymentDescriptor) throws IOException;
}
