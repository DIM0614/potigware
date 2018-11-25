package generated;

import java.util.Arrays;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

public class MathImpl extends MathInvoker implements Math {

	@Override 
	public Float pi(Float precision) throws RemoteError {
		float ret = 0;
		for(int i = 0; ; i++) {
			float pr = ret;
			ret += ((i&1) != 0 ? -1. : 1.)*4./(2.*i + 1.);
			if(pr - ret < precision && ret - pr < precision) {
				return ret;
			}
		}
	}

	@Override
	public Integer fibonacci(Integer start, Integer i) throws RemoteError {
		if(i < 1) {
			throw new IllegalArgumentException("index should be positive");
		}
		int ret = start, pr = 0;
		while(i-- > 1) {
			int nv = ret + pr;
			pr = ret;
			ret = nv;
		}
		return ret;
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

