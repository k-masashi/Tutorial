package tutorial.util;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class SymbolSetTest {

	@Test
	public void test() {
		SymbolSet sut = new SymbolSet();
		sut.getCode("a");
		sut.getCode("b");
		assertThat(sut.size(), is(2));
		assertThat(sut.getStr(0), is("a"));
		assertThat(sut.getCode("b"), is(1));
	}

}
