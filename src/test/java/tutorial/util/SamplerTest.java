package tutorial.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class SamplerTest {

	@Test
	public void test() {
		double[] p = {0.3, 0.1, 0.4, 0.2};
		int[] freq = new int[4];
		int N = 10000;
		
		for(int i=0; i<N; i++){
			int r = Sampler.sample(p);
			freq[r]++;
		}
		
		for(int k=0; k<4; k++){
			double ratio = (double)freq[k] / (double)N;
			assertThat(ratio,is(closeTo(p[k],0.01)));
		}
	}
}
