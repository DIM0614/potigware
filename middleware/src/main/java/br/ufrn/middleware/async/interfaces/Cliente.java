package br.ufrn.middleware.async.interfaces;

import br.ufrn.dimap.middleware.async.CallbackImpl;
import br.ufrn.dimap.middleware.async.Middleware;

public class Cliente {

	
	public void batatinha() {
		
		CallbackImpl callback = CallbackImpl.builder()
		.onResult((data) -> System.out.println(data))
		.onError((erro) -> System.out.println())		
		.build();
		
		Middleware.doRequestAsync("objetoRemoto", "metodo", null);
	}
	
}

	
