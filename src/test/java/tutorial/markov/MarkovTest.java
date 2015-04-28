package tutorial.markov;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import tutorial.discrete.Dirichlet;
import tutorial.discrete.DirichletMultinomial;
import tutorial.discrete.Multinomial;
import tutorial.discrete.SymmetricDirichlet;
import tutorial.util.DocumentSet;

public class MarkovTest {

	@Test
	public void test() throws Exception{
		Multinomial pi = new Multinomial(new double[]{ 0.1, 0.5, 0.4} );
		Multinomial[] A = new Multinomial[3];
		A[0] = new Multinomial(new double[]{ 0.1, 0.3, 0.5, 0.1});
		A[1] = new Multinomial(new double[]{ 0.7, 0.1, 0.1, 0.1});
		A[2] = new Multinomial(new double[]{ 0.2, 0.4, 0.1, 0.3});
		
		Markov sut = new Markov(pi, A);
		int[] X = {1, 0, 2};
		assertThat(sut.p(X), is(0.5*0.7*0.5*0.3));		
		double[] p = {0.0, 1.0, 0.0};
		double[] pA = sut.multiplyA(p);
		double[] pAA = sut.multiplyA(pA);
		assertThat(pA,is(new double[]{0.7, 0.1, 0.1}));
		assertThat(pAA,is(new double[]{
			0.7*0.1 + 0.1*0.7 + 0.1*0.2,
			0.7*0.3 + 0.1*0.1 + 0.1*0.4,
			0.7*0.5 + 0.1*0.1 + 0.1*0.1
		}));
	}
	
	@Test
	public void testMLE() throws Exception {
		DocumentSet corpus = new DocumentSet("src/test/resources/hashiru.txt");
		int V = corpus.getVocabulary().size();
		Multinomial pi = new Multinomial(new double[V]);
		Multinomial[] A = new Multinomial[V];
		for (int i = 0; i < V; i++){
			A[i] = new Multinomial(new double[V+1]);
		}
		Markov sut = new Markov(pi, A);
		sut.estimateParam(corpus.getDocuments());		
		int watashi = corpus.getVocabulary().getCode("わたし");
		int kimi = corpus.getVocabulary().getCode("きみ");
		int ha = corpus.getVocabulary().getCode("は");
		int ga = corpus.getVocabulary().getCode("が");
		int hashiru = corpus.getVocabulary().getCode("はしる");
		int End = V;
		
		assertThat(sut.getPi().p(watashi),is(3.0/6.0));
	}

	@Test
	public void testMap() throws Exception {
		DocumentSet corpus = new DocumentSet("src/test/resources/hashiru.txt");
		int V = corpus.getVocabulary().size();
		Dirichlet priorPi = new SymmetricDirichlet(V, 2.0);
		Dirichlet priorA = new SymmetricDirichlet(V + 1, 2.0);
		Multinomial pi = new DirichletMultinomial(new double[V], priorPi);
		Multinomial[] A = new Multinomial[V];
		for (int i = 0; i < V; i++){
			A[i] = new DirichletMultinomial(new double[V+1], priorA);
		}
		Markov sut = new Markov(pi, A);
		sut.estimateParam(corpus.getDocuments());
		
		int watashi = corpus.getVocabulary().getCode("わたし");
		int kimi = corpus.getVocabulary().getCode("きみ");
		int ha = corpus.getVocabulary().getCode("は");
		int ga = corpus.getVocabulary().getCode("が");
		int hashiru = corpus.getVocabulary().getCode("はしる");
		int End = V;
		
		assertThat(sut.getPi().p(watashi),is((3.0 + 1) / (6.0 + V)));
	}
}
