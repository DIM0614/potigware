package br.ufrn.dimap.middleware.lookup;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.identification.lookup.DefaultLookup;
import br.ufrn.dimap.middleware.remotting.impl.RemoteError;

import java.io.IOException;

public class LoadFromNamingTest {

    public static void main(String[] args) throws RemoteError, IOException, ClassNotFoundException {

        AbsoluteObjectReference aor = DefaultLookup.getInstance().find("math");

        //DefaultLookup.getInstance().findAndLocallyInstall(aor.getObjectId());



    }


}
