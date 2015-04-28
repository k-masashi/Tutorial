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
public class PaserWIki {
	//ファイル読み込み用メソッド
	public static void  fileRead(String filePath) {
		FileReader fr = null;
		String ResultRead = "";
		BufferedReader br = null;
		Tokenizer tokenizer = Tokenizer.builder().build();
		try {
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);
			String line;
			int i = 0;
			while ((line = br.readLine()) != null) {
				i ++;
				try{
					line = line.replace("\"", "");
					String[] array = line.split(",");
					String word = array[1];
					String read = "";
					String yomi = "unknown";
					List<Token> tokens = tokenizer.tokenize(word);
					// 結果を出力してみる
					int k = 0;
					for (Token token : tokens) {
						/*
					    System.out.println("==================================================");
					    System.out.println("allFeatures : " + token.getAllFeatures());
					    System.out.println("partOfSpeech : " + token.getPartOfSpeech());
					    System.out.println("position : " + token.getPosition());
					    System.out.println("reading : " + token.getReading());
					    System.out.println("surfaceFrom : " + token.getSurfaceForm());
					    System.out.println("allFeaturesArray : " + Arrays.asList(token.getAllFeaturesArray()));
					    System.out.println("辞書にある言葉? : " + token.isKnown());
					    System.out.println("未知語? : " + token.isUnknown());
					    System.out.println("ユーザ定義? : " + token.isUser());
					    */
					    
					    
					    if(token.getReading() != null){				    	
					    	read += token.getReading();
					    	if(k==0){
					    		yomi = read.substring(0, 1);
					    		k++;
					    	}				    	
					    	
					    }
					    
					      
					}
					if(yomi.equals("unknown")){
						yomi = array[1].toString().substring(0,1);
						read = array[1];
					}
					System.out.println(i + " " + array[1] + "," + array[2] + "," + read + "\n");
					saveText(array[1] + "," + array[2] + "," + read + "\n", "/Users/masashi/java/wikiAlpha/" + yomi + ".csv");
					//System.out.println(line);
	
				}catch(Exception e){
					
				}
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
		//return ResultRead;
	}
	
	public static String TagRemover(String str) {
		// 文字列のすべてのタグを取り除く
		return str.replaceAll("[.+?]", "");
	}
	
	//ファイル書き込み用メソッド
	public static void saveText(String str, String filepath) {
		try {
			File file = new File(filepath);
			FileWriter filewriter = new FileWriter(file, true);
			filewriter.write(str);
			filewriter.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
