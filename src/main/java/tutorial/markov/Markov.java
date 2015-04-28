package tutorial.markov;

import java.util.ArrayList;
import java.util.List;

import tutorial.discrete.Multinomial;
import tutorial.util.DocumentSet;
import tutorial.util.Sampler;

public class Markov {
	private int V;
	private Multinomial pi;
	private Multinomial[] A;
	
	public Markov(Multinomial paramInit, Multinomial[] paramTrans) {
		V = paramInit.getDimension();
		pi = paramInit;
		A = paramTrans;
	}
	
	public double p(int[] X){
		int T = X.length;
		double p = pi.p(X[0]);

		for (int t=1; t< X.length; t++){
			p = p * A[X[t - 1]].p(X[t]);
		}
		p = p* A[X[T - 1]].p(V);
		return p;
	}
	
	public double[] multiplyA(double[] p) {
		double[] pA = new double[V];
		for (int j = 0; j < A.length; j++) {
            double y=0.0;
            for (int i = 0; i < A.length; i++) {
                y += p[i] * A[i].p(j);
            }
            pA[j] = y;
        }
		return pA;
	}

	public void estimateParam(List<int[]> X) {
		int N = X.size();
		int[] nPi = new int[V];
		int[][] nA = new int[V][V + 1];
		for (int d=0; d < N; d++){
			int[] Xd = X.get(d);
			int Td = Xd.length;
			nPi[Xd[0]] += 1;
			for (int t = 1; t < Td; t++){
				nA[Xd[t-1]][Xd[t]] += 1;
			}
			nA[Xd[Td - 1]][V] += 1;
		}
		
		pi.estimateParamByFreq(nPi);
		for (int i = 0; i < V; i++) {
			A[i].estimateParamByFreq(nA[i]);
		}
	}
	
	public Multinomial getPi(){
		return pi;
	}
	
	public Multinomial[] getA(){
		return A;
	}
	
	public List<Integer> sample() {
		List<Integer> samples = new ArrayList<Integer>();
		samples.add(Sampler.sample(pi.getMu()));
		for (int t = 1; samples.get(t-1) != V; t++){
			samples.add(Sampler.sample(A[samples.get(t - 1)].getMu()));
		}
		return samples;
	}

	static public void main(String[] args) throws Exception {
		//DocumentSet corpus = new DocumentSet("src/test/resources/hashiru.txt");
		DocumentSet corpus = new DocumentSet("src/test/resources/hashiru.txt");
		int V = corpus.getVocabulary().size();
		Multinomial pi = new Multinomial(new double[V]);
		Multinomial[] A = new Multinomial[V];
		for (int i = 0; i < V; i++){
			A[i] = new Multinomial(new double[V+1]);
		}
		Markov mm = new Markov(pi, A);
		mm.estimateParam(corpus.getDocuments());
		List<Integer> sample = mm.sample();
		for (int t = 0; t < sample.size() - 1; t++){
			String word = corpus.getVocabulary().getStr(sample.get(t));
			System.out.println(word + " ");
		}
	}
}
