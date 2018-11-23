# Middleware

## Running

1. Run the NameServerMain (identification package)
2. Run the MiddlewareMain (remotting/exec package)
3. Run the ClientInstaller and follow the instructions
    - Have a json described IDL in your PC and type its absolute path, press enter
    - After the generation process, create a class in the generated package that extends the `_YOURMODULENAME_Invoker` class,
        and implement all the necessary methods, type its name in the installer screen
    - Type localhost for the host and 8001 for the port
