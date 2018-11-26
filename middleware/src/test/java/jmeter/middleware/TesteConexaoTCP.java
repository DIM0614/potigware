package jmeter.middleware;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.io.Serializable;
import java.net.Socket;


public class TesteConexaoTCP extends AbstractJavaSamplerClient implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private static final String ARG1_TAG = "Endereço";
    private static final String ARG2_TAG = "Porta";


    @Override
    public Arguments getDefaultParameters() {

        Arguments defaultParameters = new Arguments();
        defaultParameters.addArgument(ARG1_TAG,"localhost");
        defaultParameters.addArgument(ARG2_TAG,"8001");

        return defaultParameters;
    }

    //@Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {

        String arg1 = javaSamplerContext.getParameter(ARG1_TAG);
        String arg2 = javaSamplerContext.getParameter(ARG2_TAG);

        SampleResult sampleResult = new SampleResult();
        sampleResult.sampleStart();

        try {
        	
            Boolean sucesso = this.testFunction(arg1,arg2);
            if(sucesso) {
	            sampleResult.sampleEnd();
	            sampleResult.setSuccessful(true);
	            sampleResult.setResponseCodeOK();
	            sampleResult.setResponseMessage("Conexao feita!");
            }
            else {
                sampleResult.sampleEnd();
                sampleResult.setResponseMessage("Conexao recusada");
                sampleResult.setSuccessful(false);
            }
        } catch (Exception e) {
            sampleResult.sampleEnd();
            sampleResult.setResponseMessage(e.getMessage());
            sampleResult.setSuccessful(false);
        }

        return sampleResult;
    }

    public Boolean testFunction(String endereco, String port) throws Exception {

  	  Socket clientSocket = new Socket(endereco, Integer.parseInt(port));
  	  clientSocket.close();
  	  
  	  return true;
  	  
  }
    
}
