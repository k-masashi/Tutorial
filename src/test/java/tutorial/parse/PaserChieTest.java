package tutorial.parse;

import static org.junit.Assert.*;

import org.junit.Test;

public class PaserChieTest {

	@Test
	public void test() {
		PaserChie paser = new PaserChie();
		paser.makeDocuments("/Users/masashi/java/questions_topic93.txt");
	}

}
