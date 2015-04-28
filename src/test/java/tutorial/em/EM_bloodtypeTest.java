package tutorial.em;
import org.junit.Test;
public class EM_bloodtypeTest {
	@Test
	public void test() {
		//観測データを入力
		EM_bloodtype bloodtype = new EM_bloodtype(10,20,10,5,0.4,0.3,0.3);
		for(int i=0; i < 25; i++){
			System.out.println(i + "回目");
			System.out.println("po:" + String.format("%.4f", bloodtype.get_p()[0]));
			System.out.println("pa:" + String.format("%.4f", bloodtype.get_p()[1]));
			System.out.println("pb:" + String.format("%.4f", bloodtype.get_p()[2]));
			System.out.println("------");
			bloodtype.Estep();
			bloodtype.Mstep();
		}
	}
}
