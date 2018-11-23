package generated;

import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

public class MathImpl extends MathInvoker {
    @Override
    public Float pi(Float precision) throws RemoteError {
        return 3.14f;
    }

    @Override
    public Integer fibonacci(Integer start, Integer i) throws RemoteError {
        return 2;
    }
}
