package tutorial.markov;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import tutorial.discrete.Multinomial;

public class HMMTest {

	@Test
	public void test() {
		int N = 3;
		Multinomial pi = new Multinomial(new double[]{0.1, 0.5, 0.4});
		Multinomial[] A = new Multinomial[3];
		A[0] = new Multinomial(new double[]{0.1, 0.3, 0.5, 0.1});
		A[1] = new Multinomial(new double[]{0.7, 0.1, 0.1, 0.1});
		A[2] = new Multinomial(new double[]{0.2, 0.4, 0.1, 0.3});
		Multinomial[] B = new Multinomial[3];
		B[0] = new Multinomial(new double[]{ 0.7, 0.1, 0.1, 0.1});
		B[1] = new Multinomial(new double[]{ 0.2, 0.6, 0.1, 0.2});
		B[2] = new Multinomial(new double[]{ 0.1, 0.1, 0.4, 0.4});
		
		HMM sut = new HMM(pi, A, B);
		int[] X = {1, 0, 3, 1};
		
		double p = 0.0;
		for(int z0=0; z0<N; z0++){
			for(int z1=0; z1<N; z1++){
				for(int z2=0; z2<N; z2++){
					for(int z3=0; z3<N; z3++){
						p += pi.p(z0) * B[z0].p(X[0]) *
								A[z0].p(z1) * B[z1].p(X[1]) *
								A[z1].p(z2) * B[z2].p(X[2]) *
								A[z2].p(z3) * B[z3].p(X[3]) *
								A[z3].p(N);
					}						
				}
			}
		}
		
		assertThat(sut.p(X),is(p));		
	}
	
	@Test
	public void testSample() throws Exception {
		int N = 3;
		Multinomial pi = new Multinomial(new double[]{0.1, 0.5, 0.4});
		Multinomial[] A = new Multinomial[3];
		A[0] = new Multinomial(new double[]{0.1, 0.3, 0.5, 0.1});
		A[1] = new Multinomial(new double[]{0.7, 0.1, 0.1, 0.1});
		A[2] = new Multinomial(new double[]{0.2, 0.4, 0.1, 0.3});
		Multinomial[] B = new Multinomial[3];
		B[0] = new Multinomial(new double[]{ 0.7, 0.1, 0.1, 0.1});
		B[1] = new Multinomial(new double[]{ 0.2, 0.6, 0.1, 0.2});
		B[2] = new Multinomial(new double[]{ 0.1, 0.1, 0.4, 0.4});
		
		HMM sut = new HMM(pi, A, B);
		int[] X = {1, 0, 3, 1};
		
		int Ns = 10000;
		int[][] n = new int[4][N];
		for(int s=0; s<Ns; s++){
			int[] Z = sut.sampleZ(X);
			for(int t=0; t<4; t++){
				n[t][Z[t]] += 1;
			}
		}
		
		double[][] expect = new double[4][N];
		for(int z0=0; z0<N; z0++){
			for(int z1=0; z1<N; z1++){
				for(int z2=0; z2<N; z2++){
					for(int z3=0; z3<N; z3++){
						double p = pi.p(z0) * B[z0].p(X[0]) *
								A[z0].p(z1) * B[z1].p(X[1]) *
								A[z1].p(z2) * B[z2].p(X[2]) *
								A[z2].p(z3) * B[z3].p(X[3]) *
								A[z3].p(N);
						
						expect[0][z0] += p;
						expect[1][z1] += p;
						expect[2][z2] += p;
						expect[3][z3] += p;
					}						
				}
			}
		}
		
		double pX = sut.p(X);
		for(int t=0; t<4; t++){
			for(int i=0; i<N; i++){
				double ratio = (double)n[t][i] / (double)Ns;
				assertThat(ratio,is(closeTo(expect[t][i]/pX,0.05)));
			}
		}
		
	}

}
