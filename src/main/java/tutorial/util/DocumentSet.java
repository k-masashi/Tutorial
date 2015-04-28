package tutorial.util;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;

import tutorial.sqlite.DB_chie;

public class DocumentSet {
	private ArrayList<int[]> documents = new ArrayList<int[]>();
	private SymbolSet vocabulary = new SymbolSet();

	public DocumentSet(String path) throws IOException {
		DB_chie db = new DB_chie();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(path)));
		String line;
		int docNum = 0;
		int outNum = 0;
		while ((line = br.readLine()) != null) {
			docNum += 1;
			System.out.println(String.valueOf(docNum) + " " + String.valueOf(outNum));
			if(!line.equals("")){
				String[] words = line.split(" ");
				int[] codes = new int[words.length];
				for (int i = 0; i < words.length; i++) {
					codes[i] = vocabulary.getCode(words[i]);
				}
				documents.add(codes);
				
				db.insertAnswer(docNum, outNum, line);
				outNum += 1;
			}else{
				db.insertAnswer(docNum, -1, line);				
			}
		}
		br.close();		
		try {
			db.executeDB();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<int[]> getDocuments() {
		return documents;
	}

	public SymbolSet getVocabulary() {
		return vocabulary;
	}
}