package tutorial.discrete;
 
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
 
import org.junit.Test;
 
public class DirichletTest {
 
	@Test
	public void testOptimization() {
		double[] alpha = {1, 1, 1};
		//int[][] X = {
		//		{0, 1, 1, 0},
		//		{1, 0, 1},
		//		{0, 1, 1, 2, 1}
		//};
		double denom = 
				1.0/(1-1+3)+1.0/(2-1+3)+1.0/(3-1+3)+1.0/(4-1+3) + 
				1.0/(1-1+3)+1.0/(2-1+3)+1.0/(3-1+3) + 
				1.0/(1-1+3)+1.0/(2-1+3)+1.0/(3-1+3)+1.0/(4-1+3)+1.0/(5-1+3);
		double numer0 =
				1.0/(1-1+1)+1.0/(2-1+1) +
				1.0/(1-1+1) +
				1.0/(1-1+1);
		double numer1 =
				1.0/(1-1+1)+1.0/(2-1+1) +
				1.0/(1-1+1)+1.0/(2-1+1) +
				1.0/(1-1+1)+1.0/(2-1+1)+1.0/(3-1+1);
		double numer2 =
				0.0 +
				0.0 +
				1.0/(1-1+1);
		double[] opt1 = {1.0*numer0/denom, 1.0*numer1/denom, 1.0*numer2/denom};
		
		int[] C_ = {0, 0, 0, 1, 1, 1};
		int ndMax = 5;
		int[][] Ck = {
				{0, 2, 1, 0, 0, 0},
				{0, 0, 2, 1, 0, 0},
				{2, 1, 0, 0, 0, 0}
		};
		int[] ndkMax = {2, 3, 1};
		Dirichlet sut = new Dirichlet(alpha);
		sut.optimizeParam(Ck, ndkMax, C_, ndMax, 1);
		assertThat(sut.alpha,is(opt1));
	}
}