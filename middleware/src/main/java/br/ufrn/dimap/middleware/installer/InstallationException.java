package br.ufrn.dimap.middleware.installer;

/**
 * Exception thrown when an error occur during the installation of a remote object.
 *
 * @author Daniel Smith
 */
public class InstallationException extends Throwable {
    /**
     * Default message for the exception.
     */
    public static final String INSTALLATION_EXCEPTION_DEFAULT_MESSAGE = "An error occurred while installing the app on " +
            "the middleware. See the stacktrace for more details.";

    public InstallationException(String message, Throwable cause) {
        super(message,cause);
    }

    public InstallationException(Throwable cause) {
        super(INSTALLATION_EXCEPTION_DEFAULT_MESSAGE,cause);
    }

    public InstallationException(String message) {
        super(message);
    }
}
