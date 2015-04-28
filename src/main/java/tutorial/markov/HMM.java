package tutorial.markov;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import tutorial.discrete.Dirichlet;
import tutorial.discrete.DirichletMultinomial;
import tutorial.discrete.Multinomial;
import tutorial.discrete.SymmetricDirichlet;
import tutorial.util.DocumentSet;
import tutorial.util.Sampler;

public class HMM {
	private int N;
	private int V;
	private Markov mm;
	private Multinomial [] B;
	
	public HMM(Multinomial paramInit, Multinomial[] paramTrans, Multinomial[] paramEmit){
		N = paramInit.getDimension();
		V = paramEmit[0].getDimension();
		mm = new Markov(paramInit, paramTrans);		
		B = paramEmit;
	}
	
	public double[][] forward(int[] X, double[] scale) {
		int T = X.length;
		double[][] alpha = new double[T][];
		alpha[0] = mm.getPi().getMu();
		alpha[0] = multiplyXi(X[0], alpha[0]);
		scale[0] = scale(alpha[0]);
		
		for (int t=1; t < T; t++) {
			alpha[t] = mm.multiplyA(alpha[t-1]);
			alpha[t] = multiplyXi(X[t], alpha[t]);
			scale[t] = scale(alpha[0]);
		}
		
		return alpha;
	}
	
	public double scale(double[] alphat) {
		double c = 0.0;
		for (int i = 0; i < N; i++) {
			c += alphat[i];
		}
		for (int i = 0; i < N; i++){
			alphat[i] = alphat[i] / c;
		}
		
		return c;
	}
	
	public double[] multiplyXi(int v, double[] p){
		double[] pXi = new double[N];
		for (int i=0; i < N; i++){
			pXi[i] = p[i] * B[i].p(v);
		}
		return pXi;
	}
	
	public double p(int[] X) {
		int T = X.length;
		double[] c = new double[T];
		double[][] alpha = forward(X, c);
		double p = 0.0;
		double cProd = 1.0;
		
		for (int t = 0; t < T; t++) {
			cProd = cProd * c[t];
		}
		
		for (int i = 0; i < N; i++) {
			p += alpha[T - 1][i] * mm.getA()[i].p(N);
		}
		return p * cProd;
	}
	
	private int backwardSample(double[] alphat, int ztp1) {
		double[] p = new double[N];
		for (int i =0; i < N; i++){
			p[i] = alphat[i] * mm.getA()[i].p(ztp1);
		}
		return Sampler.sample(p);
	}
	
	public int[] sampleZ(int[] X) {
		int T = X.length;
		double[] scale = new double[T];
		double[][] alpha = forward(X, scale);
		int[] z = new int[T];
		z[T - 1] = backwardSample(alpha[T - 1],N);
		for (int t = T -2; t > -1; t--) {
			z[t] = backwardSample(alpha[t], z[t+1]);
		}		
		return z;
	}
	
	public void estimateParam(List<int[]> X, List<int[]> Z) {
		int D = X.size();
		mm.estimateParam(Z);
		int[][] nB = new int[N][V];
		for (int d = 0; d < D; d++){
			int[] Xd = X.get(d);
			int[] Zd = Z.get(d);
			int Td = Xd.length;
			
			for (int t = 0; t < Td; t++){
				nB[Zd[t]][Xd[t]] += 1;
			}
		}
		for (int i = 0; i < N; i++){
			B[i].estimateParamByFreq(nB[i]);
		}
	}
	
	public void estimateParam(List<int[]> X, int numIteration) {
		int D = X.size();
		List<int[]> Z = new ArrayList<int[]>();
		for (int iteration = 0; iteration < numIteration; iteration++) {
			for (int d = 0; d < D; d++) {
				int[] Xd = X.get(d);
				int[] Zd = sampleZ(Xd);
				Z.add(Zd);
				//System.out.println(calcLogLikelihood(Z));
			}

			estimateParam(X,Z);
			Z.clear();
		}
			
	}	
	
	private double calcLogLikelihood(List<int[]> X) {
		double logLikelihood = 0.0;
		int D = X.size();
		for (int d=0; d < D; d++){
			int[] Xd = X.get(d);
			int T = Xd.length;
			double[] scale = new double[T];
			double[][] alpha = forward(Xd, scale);
			double ll = 1.0;
			for(int t=0; t<T; t++){
				ll *= scale[t];
			}
			double sum = IntStream.range(0, N)
					.mapToDouble(i -> alpha[T-1][i] * mm.getA()[i].p(N))
					.sum();
			logLikelihood += Math.log(ll * sum);
 		}
		return logLikelihood;
	}
	
	
	static public void main(String[] args) throws Exception {
		DocumentSet corpus = new DocumentSet("src/test/resources/hashiru.txt");
		int D = corpus.getDocuments().size();
		int N = 3;
		int V = corpus.getVocabulary().size();
		
		Dirichlet priorPi = new SymmetricDirichlet(N, 1.1);
		Dirichlet priorA = new SymmetricDirichlet(N + 1, 1.1);
		Dirichlet priorB = new SymmetricDirichlet(V, 1.1);
		Multinomial pi = DirichletMultinomial.newUniform(priorPi);
		Multinomial[] A = new Multinomial[N];
		Multinomial[] B = new Multinomial[N];
		for (int i = 0; i < N; i++){
			A[i] = DirichletMultinomial.newUniform(priorA);
			B[i] = DirichletMultinomial.newUniform(priorB);
		}
		/*
		Multinomial pi = Multinomial.newUniform(N);
		Multinomial[] A = new Multinomial[N];
		Multinomial[] B = new Multinomial[N];
		for (int i = 0; i < N; i++){
			A[i] = Multinomial.newUniform(N + 1);
			B[i] = Multinomial.newUniform(V);
		}
		*/
		
		HMM sut = new HMM(pi, A, B);
		sut.estimateParam(corpus.getDocuments(), 100);
		
		for (int d = 0; d < D; d++) {
			int[] Xd = corpus.getDocuments().get(d);
			int[] Zd = sut.sampleZ(Xd);
			String word = corpus.getVocabulary().getStr(d);
			//System.out.println("X:" + Arrays.toString(Xd));
			System.out.println(Arrays.toString(Zd));
		}
		
	}

}
