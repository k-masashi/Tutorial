package tutorial.topic;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

import tutorial.discrete.Dirichlet;
import tutorial.discrete.Proportion;
import tutorial.discrete.SymmetricDirichlet;
import tutorial.sqlite.DB_chie;
import tutorial.util.DocumentSet;
import tutorial.util.Sampler;
import tutorial.util.SortedCount;
import tutorial.util.SymbolSet;
 
public class SparseLDA {
	private int M;
	private int V;
	private Dirichlet topicDir;
	private Dirichlet wordDir;
	private SortedCount[] nvt;
 
	public SparseLDA(Dirichlet topicPrior, Dirichlet wordPrior) {
		M = topicPrior.K();
		V = wordPrior.K();
		topicDir = topicPrior;
		wordDir = wordPrior;
		nvt = new SortedCount[V];
	}
 
	private ArrayList<int[]> X;
	private int[][] ndt;
	private int[] nt;
	private Proportion termS;
	private Proportion termR;
	private Proportion termU;
	private Proportion termQ;
	private double[] denomCache;
 
	public int[][] sample(ArrayList<int[]> docs, int numIteration) {
		X = docs;
		int D = X.size();
		ndt = new int[D][M];
		nt = new int[M];
		termS = new Proportion(M);
		termR = new Proportion(M);
		termU = new Proportion(M);
		termQ = new Proportion(M);
		denomCache = new double[M];
		// ハイパーパラメータ学習の準備
		int ndMax = X.stream().mapToInt(doc -> doc.length).max().getAsInt();
		int[] C_ = new int[ndMax + 1];
		for (int d = 0; d < D; d++) {
			C_[X.get(d).length]++;
		}
		// サンプルの初期化
		int[][] samples = new int[D][];
		int[][] nvtTmp = new int[V][M];
		for (int d = 0; d < D; d++) {
			int[] Xd = X.get(d);
			int Nd = Xd.length;
			samples[d] = new int[Nd];
			for (int w = 0; w < Nd; w++) {
				samples[d][w] = Sampler.nextInt(M);
				int m = samples[d][w];
				ndt[d][m]++;
				nvtTmp[X.get(d)[w]][m]++;
				nt[m]++;
			}
		}
		// sort nvt
		for (int v = 0; v < V; v++) {
			nvt[v] = new SortedCount(nvtTmp[v]);
		}
		// Gibbs sampling
		for (int iteration = 0; iteration < numIteration; iteration++) {
			initGlobalCache();
			// for each doc
			for (int d = 0; d < D; d++) {
				int[] Xd = X.get(d);
				int Nd = Xd.length;
				initDocumentCache(d);
				// for each word
				for (int w = 0; w < Nd; w++) {
					int m = samples[d][w];
					removeSample(d, w, m);
					updateCache(d, m);
					initWordCache(Xd[w]);
					// choose bucket
					double C = termS.getSum() + termR.getSum() + termQ.getSum();
					double rand = Sampler.nextDouble() * C;
					int bucket = termQ.checkBucket(rand);
					if (bucket != -1) {
						bucket = nvt[Xd[w]].ithFreqTerm(bucket);
					} else {
						rand -= termQ.getSum();
						bucket = termR.checkBucket(rand);
						if (bucket == -1) {
							rand -= termR.getSum();
							bucket = termS.checkBucket(rand);
						}
					}
					samples[d][w] = bucket;
					addSample(d, w, bucket);
					updateCache(d, bucket);
				}
			}
			// optimize hyperparameters
			int[] ndkMax = new int[M];
			int[][] Ck = new int[M][ndMax + 1];
			for (int m = 0; m < M; m++) {
				for (int d = 0; d < D; d++) {
					int ndk = ndt[d][m];
					Ck[m][ndk]++;
					ndkMax[m] = Math.max(ndkMax[m], ndk);
				}
			}
			topicDir.optimizeParam(Ck, ndkMax, C_, ndMax, 20);
		}
 
		wordPredict = new double[M][V];
		for (int v = 0; v < V; v++) {
			for (int i = 0; i < nvt[v].numNonzero(); i++) {
				int m = nvt[v].ithFreqTerm(i);
				int c = nvt[v].ithFreqCount(i);
				wordPredict[m][v] = (c + wordDir.alpha(m))
						/ (wordDir.sumAlpha() + nt[m]);
			}
		}
 
		return samples;
	}
 
	private void addSample(int d, int i, int m) {
		int v = X.get(d)[i];
		ndt[d][m]++;
		nvt[v].inc(m);
		nt[m]++;
	}
 
	private void removeSample(int d, int i, int m) {
		int v = X.get(d)[i];
		ndt[d][m]--;
		nvt[v].dec(m);
		nt[m]--;
	}
 
	private void initGlobalCache() {
		termS.reset(M);
		for (int m = 0; m < M; m++) {
			denomCache[m] = wordDir.sumAlpha() + nt[m];
			termS.init(m, topicDir.alpha(m) * wordDir.alpha(m) / denomCache[m]);
		}
	}
 
	private void initDocumentCache(int d) {
		termR.reset(M);
		termU.reset(M);
		for (int m = 0; m < M; m++) {
			termR.init(m, ndt[d][m] * wordDir.alpha(m) / denomCache[m]);
			termU.init(m, (topicDir.alpha(m) + ndt[d][m]) / denomCache[m]);
		}
	}
 
	private void initWordCache(int v) {
		int density = nvt[v].numNonzero();
		termQ.reset(density);
		for (int i = 0; i < density; i++) {
			int m = nvt[v].ithFreqTerm(i);
			int c = nvt[v].ithFreqCount(i);
			termQ.init(i, termU.getValue(m) * c);
		}
	}
 
	private void updateCache(int d, int m) {
		denomCache[m] = wordDir.sumAlpha() + nt[m];
		termS.replace(m, topicDir.alpha(m) * wordDir.alpha(m) / denomCache[m]);
		termR.replace(m, ndt[d][m] * wordDir.alpha(m) / denomCache[m]);
		termU.replace(m, (topicDir.alpha(m) + ndt[d][m]) / denomCache[m]);
	}
 
	private double[][] wordPredict;
 
	public double wordPredict(int m, int v) {
		return wordPredict[m][v];
	}
 
	public double topicPredict(int d, int m) {
		int Nd = X.get(d).length;
		return (ndt[d][m] + topicDir.alpha(m)) / (topicDir.sumAlpha() + Nd);
	}
 
	public static void main(String[] args) throws Exception {
		DB_chie db = new DB_chie();
		//DocumentSet corpus = new DocumentSet(
		//"/Dropbox/corpus/mai95/mai95_word1m.txt");
		//DocumentSet corpus = new DocumentSet("/Users/masashi/java/textShogi_out.txt");
		//DocumentSet corpus = new DocumentSet("src/test/resources/news.txt");
		
		DocumentSet corpus = new DocumentSet("/Volumes/TOSHIBA EXT/corpus/droid_result_6_22.txt");
		SymbolSet voc = corpus.getVocabulary();
		int V = voc.size();
		int M = 1000;
		int numIteration = 1000;
		double[] alpha = IntStream.range(0, M).mapToDouble(i -> 0.01).toArray();
		Dirichlet wordPrior = new SymmetricDirichlet(V, 0.01);
		Dirichlet topicPrior = new Dirichlet(alpha);
		SparseLDA lda = new SparseLDA(topicPrior, wordPrior);
		lda.sample(corpus.getDocuments(), numIteration);
						
		for (int m = 0; m < M; m++) {
			System.out.println(m + "\talpha\t" + topicPrior.alpha(m));
			
			for (int v = 0; v < V; v++) {
				double p = lda.wordPredict(m, v);
				if (p > 0.0001) {
					db.insertWORD(v, m, voc.getStr(v), p);
					//System.out.println(m + "\t" + voc.getStr(v) + "\t" + p);
				}
			}
		}		
		db.executeDB();
	}
}