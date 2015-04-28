package tutorial.em;
public class EM_bloodtype {
	private double[] n_obs; //観測データ  no, na, nb, nab
	private double[] p;//確率(初期値) po, pa, pb
	private double[] n; //完全データ nAA, nAO, nBB, nBO, nAB
	
	EM_bloodtype(int o, int a, int b, int ab, double p1, double p2, double p3){
		n_obs = new double[4];
		n = new double[6];
		p = new double[3];
		n_obs[0] = o;
		n_obs[1] = a;
		n_obs[2] = b;
		n_obs[3] = ab;
		p[0] = p1;
		p[1] = p2;
		p[2] = p3;
	}
	
	public void Estep(){
		n[0] = n_obs[1] * p[1]*p[1] / (p[1]*p[1] + 2 * p[1] * p[0]); //nAAの期待値
		n[1] = (n_obs[1] + n_obs[0]) * 2 * p[0]*p[1] / (p[1]*p[1] + 2 * p[1] * p[0] + p[0] * p[0]); //nAOの期待値
		n[2] = n_obs[2] * p[2]*p[2] / (p[2]*p[2] + 2 * p[1] * p[2]); //nBBの期待値
		n[3] = (n_obs[2] + n_obs[0]) * 2 * p[0]*p[2] / (p[2]*p[2] + 2 * p[0] * p[2]  + p[0] * p[0]); //nBOの期待値
		n[4] = n_obs[3] * 2 * p[1]*p[2] / (2 * p[1] * p[2] + p[1] * p[1] + p[2] * p[2]); //nABの期待値
	}
	
	public void Mstep(){
		p[1] = sumA() / (n_obs[0] + n_obs[1] + n_obs[2]);
		p[2] = sumB() / (n_obs[0] + n_obs[1] + n_obs[2]);
		p[0] = 1 - p[1] - p[2];
	}
	
	public double sumA(){ //A型となりうる頻度
		return n[0] + 0.5*n[1]  + 0.5*n[4];
	}
	public double sumB(){ //B型となりうる頻度
		return n[2] + 0.5*n[3] + 0.5*n[4];
	}
	public double sumO(){ //O型となりうる頻度
		return n_obs[0] + 0.5*n[1] + 0.5*n[3];
	}
	
	public double[] get_p(){
		return p;
	}
}
