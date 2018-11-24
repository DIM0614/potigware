package generated;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;
import java.util.Arrays;

public class MathImpl extends MathInvoker {
    @Override
    public Float pi(Float precision) throws RemoteError {
        return 3.14159265f;
    }

    @Override
    public Integer fibonacci(Integer start, Integer i) throws RemoteError {
        if(i < 1) {
        	throw new IllegalArgumentException("Index is not positive");
        }
        int cur = start, pr = 0;
        for(int id = 1; id < i; id++) {
        	int nv = cur + pr;
			pr = cur;
			cur = nv;
        }
        return cur;
    }
    
    @Override
	public Void div(Integer a, Integer b) throws RemoteError {
		a /= b;
		return null;
	}

	@Override
	public Integer[] sort(Integer[] vet) throws RemoteError {
		Arrays.sort(vet);
		return vet;
	}
}
