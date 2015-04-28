package tutorial.util;
import java.util.ArrayList;
import java.util.HashMap;

public class SymbolSet {
	private HashMap<String, Integer> stoi = new HashMap<String, Integer>();
	private ArrayList<String> itos = new ArrayList<String>();

	public int getCode(String str) {
		if (stoi.containsKey(str)) {
			return stoi.get(str);
		} else {
			int code = itos.size();
			stoi.put(str, code);
			itos.add(str);
			return code;
		}
	}

	public String getStr(int code) {
		return itos.get(code);
	}

	public int size() {
		return itos.size();
	}
}