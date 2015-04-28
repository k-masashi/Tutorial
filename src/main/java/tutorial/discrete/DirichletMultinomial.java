package tutorial.discrete;

public class DirichletMultinomial extends Multinomial {
	private Dirichlet dir;
	
	public DirichletMultinomial(double[] param, Dirichlet prior) {
		super(param);
		dir = prior;
	}	
	
	static public DirichletMultinomial newUniform(Dirichlet dir) {
		int K = dir.K();
		double[] param = new double[K];
		for (int k = 0; k < K; k++) {
			param[k] = 1.0 / (double) K;
		}
		return new DirichletMultinomial(param, dir);
	}
	
	public void estimateParam(int[] X) {
		int N = X.length;
		int[] n = new int[K];
		for (int i= 0; i < X.length; i++){
			n[X[i]]++;
		}
		for (int k = 0; k < K; k++){
			mu[k] = (double) (n[k] + dir.alpha(k) - 1) / (double) (N + dir.sumAlpha() - K);
		}
	}
	
	public void estimateParamByFreq(int[] n) {
		int N = 0;
		for (int k = 0; k < n.length; k++) {
			N += n[k];
		}
		for (int k = 0; k < n.length; k++){
			mu[k] = (double) (n[k] + dir.alpha(k) -1) / (double) (N + dir.sumAlpha() - K);
		}
	}
}
