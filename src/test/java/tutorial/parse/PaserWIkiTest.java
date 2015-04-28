package tutorial.parse;

import static org.junit.Assert.*;

import org.junit.Test;

public class PaserWIkiTest {

	@Test
	public void test() {
		PaserWIki yahoo = new PaserWIki();
		yahoo.fileRead("/Users/masashi/java/wiki_content.csv");
	}

}
