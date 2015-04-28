package tutorial.bayes;

import static org.junit.Assert.*;

import org.junit.Test;

public class Bayes_multinomialTest {
	@Test
	public void test() {
		Bayes_multinomial b = new Bayes_multinomial();
		String[] c = {"good","good","bad","boring"};
		b.Classifier(c);
	}

}
