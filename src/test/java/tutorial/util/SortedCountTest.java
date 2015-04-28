package tutorial.util;
 
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
 
import org.junit.Test;
 
public class SortedCountTest {
	
	@Test
	public void test() throws Exception {
		int[] counts = { 2, 6, 0, 0, 4, 0, 0, 1, 0, 3 };
		SortedCount sut = new SortedCount(counts);
		assertThat(sut.numNonzero(), is(5));
		int[] expectCounts = { 6, 4, 3, 2, 1 };
		int[] expectTerms = { 1, 4, 9, 0, 7 };
		for (int i = 0; i < sut.numNonzero(); i++) {
			assertThat(sut.ithFreqCount(i), is(expectCounts[i]));
			assertThat(sut.ithFreqTerm(i), is(expectTerms[i]));
		}
	}
 
	@Test
	public void testDec() {
		int[] counts = { 2, 6, 0, 0, 4, 0, 0, 1, 0, 3 };
		SortedCount sut = new SortedCount(counts);
 
		sut.dec(7);
		assertThat(sut.numNonzero(), is(4));
		int[] expectCounts = { 6, 4, 3, 2 };
		int[] expectTerms = { 1, 4, 9, 0 };
		for (int i = 0; i < sut.numNonzero(); i++) {
			assertThat(sut.ithFreqCount(i), is(expectCounts[i]));
			assertThat(sut.ithFreqTerm(i), is(expectTerms[i]));
		}
	}
 
	@Test
	public void testInc() {
		int[] counts = { 2, 6, 0, 0, 4, 0, 0, 0, 0, 3 };
		SortedCount sut = new SortedCount(counts);
		
		sut.inc(7);
		assertThat(sut.numNonzero(), is(5));
		int[] expectCounts = { 6, 4, 3, 2, 1 };
		int[] expectTerms = { 1, 4, 9, 0, 7 };
		for (int i = 0; i < sut.numNonzero(); i++) {
			assertThat(sut.ithFreqCount(i), is(expectCounts[i]));
			assertThat(sut.ithFreqTerm(i), is(expectTerms[i]));
		}
	}
}