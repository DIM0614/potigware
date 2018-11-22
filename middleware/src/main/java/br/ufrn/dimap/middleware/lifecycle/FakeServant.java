package br.ufrn.dimap.middleware.lifecycle;

@PerRequest
public class FakeServant {
	private int a;
	
	public FakeServant() {
		
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}
	
}
