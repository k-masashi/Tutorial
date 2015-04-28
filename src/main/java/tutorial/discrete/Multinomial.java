package tutorial.discrete;

public class Multinomial {
	protected int K;
	protected double[] mu;
	
	public Multinomial(double[] param){
		K = param.length;
		mu = param;
	}
	
	static public Multinomial newUniform(int dim) {
		double[] param = new double[dim];
		for (int k = 0; k < dim; k++){
			param[k] = 1.0 / (double) dim;
		}
		return new Multinomial(param);
	}
	
	public double p(int x){
		return mu[x];
	}
	
	public void estimateParam(int[] X){
		int N = X.length;
		int[] n = new int[K];
		for(int i=0; i<X.length; i++){
			n[X[i]]++;
		}
		
		for(int k=0; k<K; k++){
			mu[k] = n[k] / (double) N;
		}
		
	}
	
	public int getDimension(){
		return K;
	}
	
	public void estimateParamByFreq(int[] n) {
		int N = 0;
		for (int k = 0; k < n.length; k++) {
			N += n[k];
		}
		for (int k = 0; k < n.length; k++){
			mu[k] = (double) n[k] / (double) N;
		}
	}
	
	public double[] getMu() {
		return mu;
	}

}
