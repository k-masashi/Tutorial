package tutorial.discrete;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;

import org.junit.Test;

import tutorial.util.DocumentSet;

public class PolyaTest {

	@Test
	public void test() {
		double[] param = {3.0, 2.0, 2.0};
		Polya sut = new Polya(new Dirichlet(param));
		int[] X = {0, 2, 2, 0, 0, 1};
		assertThat(sut.p(X), is(closeTo(0.00108225108, 10E-10)));
	}
	
	public void testUnigram() throws Exception {
		DocumentSet corpus = new DocumentSet("src/test/resources/news.txt");
		double[] param = new double[12];
		for(int k=0; k<12; k++){
			param[k] = 1.0;
		}
		
		Polya sut = new Polya(new Dirichlet(param));
		for(int[] doc : corpus.getDocuments()){
			for(int x : doc){
				sut.observe(x);
			}
		}
		assertThat(sut.p(0),is(0.125));
	}

}
