package tutorial.discrete;
 
public class SymmetricDirichlet extends Dirichlet {
	private double alpha;
 
	public SymmetricDirichlet(int dim, double param) {
		super(new double[0]);
		K = dim;
		alpha = param;
		sumAlpha = param * dim;
	}
 
	@Override
	public double alpha(int k) {
		return alpha;
	}
 
	@Override
	public void optimizeParam(int[][] Ck, int[] ndkMax, int[] C_, int ndMax,
			int numIteration) throws UnsupportedOperationException {
		throw new UnsupportedOperationException(
				"Can't optimize symmetric dirichlet parameter");
	}
}