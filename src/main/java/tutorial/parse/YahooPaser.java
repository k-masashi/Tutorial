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

public class YahooPaser {
	
	public YahooPaser(String file){
		String getStr = fileRead(file);
		/*
		String[] array = getStr.split("。");
		Tokenizer tokenizer = Tokenizer.builder().build();
		for(int i=0; i<array.length; i++){
			System.out.println(array[i]);
			List<Token> tokens = tokenizer.tokenize(array[i]);						
			// 結果を出力してみる
			for (Token token : tokens) {
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
			}
		}
		*/
	}
	
	public static void wakati(String str){
		Tokenizer tokenizer = Tokenizer.builder().build();
		List<Token> tokens = tokenizer.tokenize(str);
		String words = "";
		for (Token token : tokens) {
		    //if(token.getAllFeatures().split(",")[0].equals("名詞") || token.getAllFeatures().split(",")[0].equals("動詞") || token.getAllFeatures().split(",")[0].equals("感動詞") || token.getAllFeatures().split(",")[0].equals("形容詞") || token.getAllFeatures().split(",")[0].equals("副詞")) {
			if(!token.getAllFeatures().split(",")[0].equals("助詞") && !token.getAllFeatures().split(",")[0].equals("助動詞")){	
		    	if(token.getAllFeatures().split(",")[0].equals("動詞") ){
		    		words += token.getAllFeatures().split(",")[6] + " ";
		    	}else if(token.getAllFeatures().split(",")[0].equals("形容詞") || token.getAllFeatures().split(",")[0].equals("形容動詞")){
		    		words += token.getAllFeatures().split(",")[6] + " ";
		    	}else{
		    		words += token.getSurfaceForm() + " ";
		    	}								 
		    }
			
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
		    
		}			
		saveText(words  + "\n", "/Volumes/TOSHIBA EXT/result/result519doc.txt");
	}
	
	
	//ファイル読み込み用メソッド
	public static String fileRead(String filePath) {
		FileReader fr = null;
		String ResultRead = "";
		BufferedReader br = null;
		try {
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);
			String line;
			int i = 0;
			while ((line = br.readLine()) != null) {
				String getLine = line.split("	")[4];
				wakati(getLine);
				System.out.println(String.valueOf(i) + " " + getLine);
				ResultRead += line;		
				i++;
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
			FileWriter filewriter = new FileWriter(file, true);
			filewriter.write(str);
			filewriter.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
