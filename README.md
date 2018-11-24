# Middleware

## Running

1. Run the `NameServerMain` (identification package)
2. Run the `MiddlewareMain` (remotting/exec package)
3. Run the `ClientInstaller` (installer package) and follow the instructions
    - Have a json described IDL in your PC and type its absolute path, press enter
    - After the generation process, create a class in the generated package that extends the `_YOURMODULENAME_Invoker` class,
        and implement all the necessary methods, type its name in the installer screen
    - Type `localhost` for the host and `8001` for the port
4. Write your client class, that will get a proxy to the methods via middleware. 

There is an example embedded in the middleware that uses the json description present in the `src/test` folder. 
The generated code for it is in the `generated` package (outside the middleware package), the implementation
is `MathImpl` and the usage is in `test/.../integration/IntegrationTest`.
