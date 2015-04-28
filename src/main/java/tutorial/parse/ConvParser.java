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

import tutorial.sqlite.DB_chie;

public class ConvParser {
	public void makeDocuments(String fileName){
		System.out.println("読み込み開始");
		String getStr = fileRead(fileName);
		System.out.println("読み込み終了");
		/*
		getStr = getStr.replace("\n","<-->");
		System.out.println(String.valueOf(getStr.indexOf("<-->")));
		System.out.println("----");		
		String[] array = getStr.split("<-->");
		for(int i=0; i<array.length; i++){
		}	
		*/

	}

	public static void wakati(String str1,String str2, String str3){
		//str = str.replace("|", "<-->");
		
		Tokenizer tokenizer = Tokenizer.builder().build();
		//String checkWord = str.split("<-->")[1];
		//String questionWord = str.split("<-->")[0];		
		List<Token> tokens = tokenizer.tokenize(str2);
		String words = "";
		// 結果を出力してみる
		for (Token token : tokens) {
		    //if(token.getAllFeatures().split(",")[0].equals("名詞") || token.getAllFeatures().split(",")[0].equals("動詞") || token.getAllFeatures().split(",")[0].equals("感動詞") || token.getAllFeatures().split(",")[0].equals("形容詞") || token.getAllFeatures().split(",")[0].equals("副詞")) {
		    	if(!token.getAllFeatures().split(",")[0].equals("助詞") && !token.getAllFeatures().split(",")[0].equals("助動詞") && !token.getAllFeatures().split(",")[0].equals("記号")){
		    		words += token.getSurfaceForm() + " ";		
		    	}
		     
		    //}
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
		
		//words = words.substring(0, words.length() - 2);
		
		saveText(str1 + "," + words + "," + str3 + "\n", "/Users/masashi/java/conv_result_4_22.txt");
		//saveText(questionWord + "\n", "/Users/masashi/java/questiones_out.txt");

	}
	//ファイル読み込み用メソッド
	public static String fileRead(String filePath) {
		FileReader fr = null;
		String ResultRead = "";
		String historyText = "";
		BufferedReader br = null;
		DB_chie db = new DB_chie();
		
		try {
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);
			String line;
			
			int i = 0;
			while ((line = br.readLine()) != null) {
				ResultRead += line;
				System.out.println(i);
				String dbStr = line;
				//dbStr = dbStr.replace("|", "<-->");
				/*
				try{
					db.insertPARE(i, dbStr.split("<-->")[0], dbStr.split("<-->")[1]);	
				}catch(Exception e){
					
				}
				*/				
				//wakati(line);
				
				String[] array = line.split(",");
				wakati(array[0],array[1],array[2]);				
				i++;
			}
			
			//db.executePARE();
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
