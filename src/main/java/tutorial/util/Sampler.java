package tutorial.util;

import java.util.Random;

public class Sampler {
	static Random rand = new Random();
	
	static public int nextInt(int k) {
		return rand.nextInt(k);
	}
	
	static public double nextDouble(){
		return rand.nextDouble();
	}
	
	static public int sample(double[] p){
		double u = rand.nextDouble() * sum(p);
		for(int i=0; i<p.length; i++){
			if(u <= p[i] ){
				return i;
			}
			u -= p[i];
		}
		return p.length-1;
	}
	
	static public double sum(double[] array){
		double s = 0;
		for(int i=0; i<array.length; i++){
			s += array[i];
		}
		return s;
	}
}
