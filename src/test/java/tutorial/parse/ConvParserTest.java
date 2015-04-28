package tutorial.parse;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConvParserTest {

	@Test
	public void test() {
		ConvParser paser = new ConvParser();
		paser.makeDocuments("/Users/masashi/java/Data/conv422.csv");

	}

}
