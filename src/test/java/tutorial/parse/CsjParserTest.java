package tutorial.parse;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class CsjParserTest {
	@Test
	public void test() throws ParserConfigurationException, SAXException, IOException {
		CsjParser csj = new CsjParser("/Users/masashi/プロジェクト/corpus/corpus/csj/CUXML/CU/ds");
	}

}
