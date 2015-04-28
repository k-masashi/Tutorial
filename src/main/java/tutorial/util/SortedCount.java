package tutorial.util;
 
import java.util.Arrays;
 
public class SortedCount {
	private int K;
	private int termBits;
	private int termMask;
	private int[] sc;
	private int density = 0;
 
	public SortedCount(int[] counts) {
		K = counts.length;
		termMask = (Integer.highestOneBit(K - 1) << 1) - 1;
		termBits = Integer.bitCount(termMask);
		sc = new int[K];
		for (int k = 0; k < K; k++) {
			sc[k] = (counts[k] << termBits) + k;
			if (counts[k] > 0) {
				density++;
			}
		}
		Arrays.sort(sc);
	}
 
	private int search(int k) {
		for (int i = 0; i < K; i++) {
			if ((sc[i] & termMask) == k) {
				return i;
			}
		}
		return -1;
	}
 
	private void swap(int i, int j) {
		int tmp = sc[i];
		sc[i] = sc[j];
		sc[j] = tmp;
	}
 
	public void inc(int k) {
		int i = search(k);
		int c = sc[i] >> termBits;
		c = c + 1;
		sc[i] = (c << termBits) + k;
		if (c == 1) {
			density++;
		}
		// bubble sort
		while (i < K - 1) {
			if (sc[i] > sc[i + 1]) {
				swap(i, i + 1);
				i++;
			} else {
				break;
			}
		}
	}
 
	public void dec(int k) {
		int i = search(k);
		int c = sc[i] >> termBits;
		c = c - 1;
		sc[i] = (c << termBits) + k;
		if (c == 0) {
			density--;
		}
		// bubble sort
		while (i > 0) {
			if (sc[i - 1] > sc[i]) {
				swap(i - 1, i);
				i--;
			} else {
				break;
			}
		}
	}
 
	public int numNonzero() {
		return density;
	}
 
	public int ithFreqTerm(int i) {
		return sc[K - 1 - i] & termMask;
	}
 
	public int ithFreqCount(int i) {
		return sc[K - 1 - i] >> termBits;
	}
 
	public int getCount(int k) {
		int i = search(k);
		return sc[i] >> termBits;
	}
}