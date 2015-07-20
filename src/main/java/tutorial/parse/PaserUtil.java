package tutorial.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

import tutorial.util.Wordnet;
import edu.cmu.lti.jawjaw.pobj.POS;
public class PaserUtil {
	//ファイル読み込み用メソッド
	public String fileRead(String filePath) {
		FileReader fr = null;
		String ResultRead = "";
		BufferedReader br = null;
		try {
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ResultRead;
	}
	
	//ファイル書き込み用メソッド
	public static void saveText(String str, String filepath) {
		try {
			File file = new File(filepath);
			FileWriter filewriter = new FileWriter(file, false);
			filewriter.write(str);
			filewriter.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	public static String[] wakati(String str){
		Tokenizer tokenizer = Tokenizer.builder().build();
		List<Token> tokens = tokenizer.tokenize(str);
		String words = "";
		for (Token token : tokens) {
			String hinshi = token.getAllFeatures().split(",")[0];
			if(!hinshi.equals("助詞") && !hinshi.equals("助動詞") && !hinshi.equals("記号")){
				//System.out.println(hinshi + ":" + token.getSurfaceForm());
				try{
					words += token.getSurfaceForm() + " " + Wordnet.AdvancedDemo.getHypernyms( token.getSurfaceForm(), POS.n );					
				}catch(Exception e){
					
				}

				//System.out.println(words);
			}
		}
		return words.split(" ");
	}
}
