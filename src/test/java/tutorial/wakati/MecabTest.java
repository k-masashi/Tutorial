package tutorial.wakati;

import static org.junit.Assert.*;

import org.junit.Test;

public class MecabTest {

	@SuppressWarnings("static-access")
	@Test
	public void test() {
		Mecab mecab = new Mecab();
		mecab.saveText(mecab.fileRead("/Users/masashi/プロジェクト/mecab-jumandic-7.0-20130310/ContentW.csv"),"/Users/masashi/プロジェクト/mecabNoum.csv");
	}

}
