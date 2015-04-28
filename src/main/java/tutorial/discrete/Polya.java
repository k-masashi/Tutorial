package tutorial.discrete;

public class Polya {
	private int K;
	private Dirichlet dir;
	private int[] n;
	private int N;
	
	public Polya(Dirichlet param){
		K = param.K();
		dir = param;
		n = new int[K];
		N = 0;
	}
	
	public double p(int x){
		return ((double)n[x] + (double)dir.alpha(x)) / ((double)N + (double)dir.sumAlpha);
	}
	
	public double p(int[] X){
		double p = 1.0;
		for(int x : X){
			p = p*p(x);
			observe(x);
		}
		for(int x : X){
			forget(x);
		}
		return p;
	}
	
	public void observe(int x){
		n[x]++;
		N++;
	}
	public void forget(int x){
		n[x]--;
		N--;
	}
}
