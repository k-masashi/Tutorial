package tutorial.discrete;
 
import java.util.Arrays;
 
public class Dirichlet {
	protected int K;
	protected double[] alpha;
	protected double sumAlpha;
 
	public Dirichlet(double[] param) {
		K = param.length;
		alpha = Arrays.copyOf(param, K);
		sumAlpha = 0.0;
		for (int k = 0; k < K; k++) {
			sumAlpha += alpha[k];
		}
	}
 
	public int K() {
		return K;
	}
 
	public double alpha(int k) {
		return alpha[k];
	}
 
	public double sumAlpha() {
		return sumAlpha;
	}
 
	private double digammaRecurrence(int nMax, int[] C, double z) {
		double R = 0;
		double S = 0;
		for (int n = 1; n <= nMax; n++) {
			R += 1.0 / (n - 1 + z);
			S += C[n] * R;
		}
		return S;
	}
 
	public void optimizeParam(int[][] Ck, int[] ndkMax, int[] C_, int ndMax, int numIteration) {
		for (int iteration = 0; iteration < numIteration; iteration++) {
			double denom = digammaRecurrence(ndMax, C_, sumAlpha);
			for (int k = 0; k < K; k++) {
				double numer = digammaRecurrence(ndkMax[k], Ck[k], alpha[k]);
				alpha[k] = alpha[k] * numer / denom;
			}
			sumAlpha = Arrays.stream(alpha).sum();
		}
	}
}