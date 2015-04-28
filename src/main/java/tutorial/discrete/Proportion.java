package tutorial.discrete;

public class Proportion {
    private int K;
    private double[] prop;
    private double sum;
    public Proportion(int dim) {
      K = dim;
      prop = new double[dim];
      sum = 0.0; 
    }
    
    public void replace(int k, double value) {
      sum -= prop[k];
      prop[k] = value;
      sum += value;
    }
    
    public double getValue(int k) {
      return prop[k];
    }
    
    public double getSum() {
      return sum;
    } 

    public void reset(int dim) {
        K = dim;
        sum = 0.0; 
    }
    
    public void init(int k, double value) {
        prop[k] = value;
        sum += value;
    }
    
    public int checkBucket(double u) {
        if (u > sum) {
        	return -1; 
        }
        for (int k = 0; k < K; k++) {
          if (u < prop[k]) {
        	  return k; 
          }
          u -= prop[k];
        }
        return -1; 
   }
}