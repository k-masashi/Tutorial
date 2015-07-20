package tutorial.parse;

import static org.junit.Assert.*;

import org.junit.Test;

public class DroidParser_aidutiTest {

	@Test
	public void test() {
		DroidParser_aiduti paser = new DroidParser_aiduti();
		paser.makeDocuments("/Users/masashi/java/Data/droidchan_data.csv");
	}

}
