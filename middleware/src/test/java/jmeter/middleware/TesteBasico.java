package jmeter.middleware;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import br.ufrn.dimap.middleware.remotting.impl.ProxyCreator;
import generated.ClientMath;

import java.io.Serializable;


public class TesteBasico extends AbstractJavaSamplerClient implements Serializable {
	private static final long serialVersionUID = 1L;

    private generated.ClientMath math;

    @Override
    public void setupTest(JavaSamplerContext context) {
    	try {
			this.math = (generated.ClientMath) ProxyCreator.getInstance().create("math", ClientMath.class);
		} catch (Exception e) {
			this.math = null;
		}
    }
    
    @Override
    public Arguments getDefaultParameters() {

        Arguments defaultParameters = new Arguments();

        return defaultParameters;
    }

    //@Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {

        SampleResult sampleResult = new SampleResult();
        sampleResult.sampleStart();

    	
        String r = "";
		try {
			r = math.pi(0.1f).toString();
		} catch (Exception e) {
            sampleResult.sampleEnd();
            sampleResult.setResponseMessage(e.getMessage());
            sampleResult.setSuccessful(false);
		}
        
        sampleResult.sampleEnd();
        sampleResult.setSuccessful(true);
        sampleResult.setResponseCodeOK();
        sampleResult.setResponseMessage(r);
    	
        return sampleResult;
    }
 
}
