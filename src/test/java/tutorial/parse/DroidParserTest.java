package tutorial.parse;

import static org.junit.Assert.*;

import org.junit.Test;

public class DroidParserTest {

	@Test
	public void test() {
		DroidParser paser = new DroidParser();
		paser.makeDocuments("/Volumes/TOSHIBA EXT/corpus/622.csv");
	}

}
