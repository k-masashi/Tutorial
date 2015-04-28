package tutorial.topic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import tutorial.discrete.Dirichlet;
import tutorial.discrete.Polya;
import tutorial.discrete.SymmetricDirichlet;
import tutorial.sqlite.DB_chie;
import tutorial.util.DocumentSet;
import tutorial.util.Sampler;

public class LDA {
	private int M;
	private Dirichlet topicDir;
	private Polya[] wordPolya;
	
	public LDA(Dirichlet topicPrior, Dirichlet wordPrior) {
		M = topicPrior.K();
		topicDir = topicPrior;
		wordPolya = new Polya[M];
		for (int m = 0; m < M; m++){
			wordPolya[m] = new Polya(wordPrior);
		}
	}
	
	private ArrayList<int[]> X;
	private Polya[] topicPolya;
	public int[][] sample(ArrayList<int[]> docs){
		X = docs;
		int D = X.size();
		topicPolya = new Polya[D];
		//サンプルの初期化
		int[][] samples = new int[D][];//文書ごとのサンプルを用意
		for (int d= 0; d < D; d++){
			int Nd = X.get(d).length; //文書内の単語数
			samples[d] = new int[Nd]; 
			topicPolya[d] = new Polya(topicDir);//トピックのPolya分布を用意
			for (int i = 0; i < Nd; i++) {
				samples[d][i] = Sampler.nextInt(M); //トピック数の範囲で乱数を生成
				addSample(d, i, samples[d][i]); //サンプルへ適用
			}
		}
		
		for (int iteration = 0; iteration < 1000; iteration++) {
			for (int d = 0; d < D; d++){
				int Nd = X.get(d).length;
				for (int i = 0; i < Nd; i++){
					removeSample(d, i, samples[d][i]);
					samples[d][i] = Sampler.sample(posterior(d, i));
					addSample(d, i, samples[d][i]);
				}
			}
		}
		return samples;
	}
	
	private void addSample(int d, int i, int m) {
		int v = X.get(d)[i];
		topicPolya[d].observe(m);
		wordPolya[m].observe(v);
	}
	
	private void removeSample(int d, int i, int m) {
		int v = X.get(d)[i];
		topicPolya[d].forget(m);
		wordPolya[m].forget(v);
	}
	
	public double[] posterior(int d, int i) {
		int v = X.get(d)[i];
		double[] posterior = new double[M];
		for (int m = 0; m < M; m++){
			posterior[m] = topicPolya[d].p(m) * wordPolya[m].p(v);
		}
		return posterior;
	}
	
	public double wordPredict(int m, int v){
		return wordPolya[m].p(v);
	}
	
	public double topicPredict(int d, int m){
		return topicPolya[d].p(m);
	}
	
	public static void main(String[] args) throws Exception{
		
		DB_chie db = new DB_chie();
		Connection con = null;
		Statement smt = null;
		ResultSet rs = null;
		
		ArrayList<Double> max_voc = new ArrayList<Double>();
		ArrayList<Integer> max_voc_m = new ArrayList<Integer>();
		int cluster = 8;
		// CsjParser csj = new
		// CsjParser("/Users/masashi/プロジェクト/corpus/corpus/csj/CUXML/CU/core");
		//DocumentSet corpus = new DocumentSet("src/test/resources/news.txt");
		// DocumentSet corpus = new
		DocumentSet corpus = new DocumentSet("/Users/masashi/java/questions_rep.txt");
		//DocumentSet corpus = new DocumentSet("/Users/masashi/java/droid_result_4_20.txt");
		// DocumentSet corpus = new DocumentSet(csj.getCorpusFile());
		Dirichlet wordPrior = new SymmetricDirichlet(corpus.getVocabulary()
				.size(), 0.01);// 単語のディリクレ分布を用意
		Dirichlet topicPrior = new SymmetricDirichlet(cluster, 0.01);// トピックのディリクレ分布を用意
		LDA lda = new LDA(topicPrior, wordPrior); // 単語のpolya分布を用意
		int[][] samples = lda.sample(corpus.getDocuments());
		
		System.out.println(samples.length);
		System.out.println(corpus.getVocabulary().size());
		
		for (int i = 0; i < corpus.getVocabulary().size(); i++) {
			max_voc.add(0.0);
			max_voc_m.add(0);
		}
		
		for (int d = 0; d < samples.length; d++) {
			// [単語トピックの分布]
			System.out.println(Arrays.toString(samples[d]));			
			db.insertTOPIC(d, Arrays.toString(samples[d]));
			// saveText(Arrays.toString(samples[d]) + "\n",
			// "/Users/masashi/java/chieLDA.txt");
		}

		for (int m = 0; m < cluster; m++) {
			for (int v = 0; v < corpus.getVocabulary().size(); v++) {
				// System.out.println(m + " " + corpus.getVocabulary().getStr(v)
				// + " " + lda.wordPredict(m, v));
				if (max_voc.get(v) < lda.wordPredict(m, v)) {
					max_voc.set(v, lda.wordPredict(m, v));
					max_voc_m.set(v, (Integer) m);
				}
				// 単語のトピック　確率
				db.insertWORD(v, m, corpus.getVocabulary().getStr(v), lda.wordPredict(m, v));
				System.out.println(m + " " + corpus.getVocabulary().getStr(v)
						+ " " + String.format("%.9f", lda.wordPredict(m, v)));
				// saveText(m + " " + corpus.getVocabulary().getStr(v) + " " +
				// String.format("%.9f",lda.wordPredict(m, v))+ "\n",
				// "/Users/masashi/java/chieLDA.txt");
			}
		}

		for (int d = 0; d < corpus.getDocuments().size(); d++) {
			for (int m = 0; m < cluster; m++) {
				// System.out.println(d + " " + m + " " + lda.topicPredict(d,
				// m));
				// ドキュメントごとのトピック
				if (lda.topicPredict(d, m) > 0.001) {
					db.insertDOCMENT(d, m, lda.topicPredict(d, m));
					// System.out.println(d + " " + m + " " +
					// String.format("%.9f",lda.topicPredict(d, m)));
					// saveText(d + " " + m + " " +
					// String.format("%.9f",lda.topicPredict(d, m)) + "\n",
					// "/Users/masashi/java/chieLDA.txt");
				}
			}
		}

		for (int m = 0; m < cluster; m++) {
			System.out.println("-----" + m + "------");
			// 各トピックの確率上位の単語
			saveText("-----" + m + "------" + "\n",
					"/Users/masashi/java/chieLDA.txt");
			for (int i = 0; i < max_voc.size(); i++) {
				if (max_voc_m.get(i) == m && lda.wordPredict(m, i) > 0.01) {
					System.out.println(corpus.getVocabulary().getStr(i) + " "
							+ String.format("%.9f", lda.wordPredict(m, i)));
					// saveText(corpus.getVocabulary().getStr(i) + " " +
					// String.format("%.9f",lda.wordPredict(m, i))+ "\n",
					// "/Users/masashi/java/chieLDA.txt");
				}
			}
		}
		db.executeDB();
		
		
	}

	// ファイル書き込み用メソッド
	public static void saveText(String str, String filepath) {
		try {
			File file = new File(filepath);
			FileWriter filewriter = new FileWriter(file, true);
			filewriter.write(str);
			filewriter.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
