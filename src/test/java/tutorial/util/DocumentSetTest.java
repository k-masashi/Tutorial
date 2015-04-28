package tutorial.util;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

public class DocumentSetTest {

	@Test
	public void test() throws IOException {
		String testWord = "私";
	    DocumentSet sut = new DocumentSet("src/test/resources/tsubasa.txt");
	    assertThat(sut.getDocuments().size(),is(4));
	    assertThat(sut.getVocabulary().size(),is(11));
	    Integer testSize = sut.getVocabulary().size();
	    assertThat(testSize,is(sut.getVocabulary().getCode(testWord)));
	    /*
		for (int i=0; i<sut.getVocabulary().size(); i++){
			if(testWord.equals(sut.getVocabulary().getStr(i))){				
			}
		}
		*/
	}	
}
