package tutorial.discrete;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import tutorial.util.DocumentSet;

public class MultinomialTest {

	@Test
	public void test() {
		double[] param = {0.3, 0.1, 0.6};
		Multinomial sut = new Multinomial(param);
		assertThat(sut.p(2),is(0.6));
		
		int[] data = {2, 1, 2, 0, 1};
		sut.estimateParam(data);
		assertThat(sut.p(0),is(1.0/5.0));
		assertThat(sut.p(1),is(2.0/5.0));
		assertThat(sut.p(2),is(2.0/5.0));
	}
	
	public void testUnigram() throws Exception {
		DocumentSet corpus = new DocumentSet("src/test/resources/news.txt");
		double[] param = new double[12];
		for(int k=0; k<12; k++){
			param[k] = 1.0 / 12.0;
		}
		Multinomial sut = new Multinomial(param);
		int[] X = new int[20];
		int i = 0;
		for(int[] doc : corpus.getDocuments()){
			for(int x : doc){
				X[i] = x;
				i++;
			}
		}
		sut.estimateParam(X);
		assertThat(sut.p(0),is(0.15));
	}
 }
