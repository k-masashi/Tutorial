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

import edu.cmu.lti.jawjaw.pobj.POS;
import tutorial.sqlite.DB_chie;
import tutorial.util.Wordnet;

public class DroidParser {
	private static int mMeishi,mKeiyoushi,mKeiyoudoushi,mDoushi,mFukushi,mKandoushi,mDaimeishi;
	public void makeDocuments(String fileName){
		System.out.println("読み込み開始");
		String getStr = fileRead(fileName);
		System.out.println("読み込み終了");
	}

	public static void wakati(String str){
		Tokenizer tokenizer = Tokenizer.builder().build();
		List<Token> tokens = tokenizer.tokenize(str);
		String words = "";
		
		int cMeishi = 0;
		int cKeiyoushi = 0;
		int cKeiyoudoushi = 0;
		int cDoushi = 0;
		int cFukushi = 0;
		int cKandoushi = 0;
		int cDaimeishi = 0;
		
		// 結果を出力してみる
		for (Token token : tokens) {
			if(!token.getAllFeatures().split(",")[0].equals("助詞") && !token.getAllFeatures().split(",")[0].equals("助動詞")){	
		    	if(token.getAllFeatures().split(",")[0].equals("動詞") ){
		    		words += token.getAllFeatures().split(",")[6] + " ";
		    	}else if(token.getAllFeatures().split(",")[0].equals("形容詞") || token.getAllFeatures().split(",")[0].equals("形容動詞")){
		    		words += token.getAllFeatures().split(",")[6] + " ";
		    	}else{
		    		words += token.getSurfaceForm() + " " + Wordnet.AdvancedDemo.getHypernyms( token.getSurfaceForm(), POS.n ) + " ";
		    	}								 
		    }
		    String hinshi = token.getAllFeatures().split(",")[0];
			if(hinshi.equals("名詞")){
				cMeishi ++;
			}else if(hinshi.equals("動詞")){
				cDoushi ++;
			}else if(hinshi.equals("形容詞")){
				cKeiyoushi ++;
			}else if(hinshi.equals("形容動詞")){
				cKeiyoudoushi ++;				
			}else if(hinshi.equals("副詞")){
				cFukushi ++;
			}else if(hinshi.equals("感動詞")){
				cKandoushi ++;
			}else if(hinshi.equals("代名詞")){
				cDaimeishi ++;
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
		
		
		//words = words.substring(0, words.length() - 2);
		String infoHinshi = String.valueOf(cMeishi) + "," + String.valueOf(cDoushi) + "," + String.valueOf(cKeiyoushi) + "," + String.valueOf(cKeiyoudoushi) + "," + String.valueOf(cFukushi) + "," + String.valueOf(cKandoushi) + "," + String.valueOf(cDaimeishi);
		mMeishi += cMeishi;
		mDoushi += cDoushi;
		mKeiyoushi += cKeiyoushi;
		mKeiyoudoushi = cKeiyoudoushi;
		mFukushi += cFukushi;
		mKandoushi += cKandoushi;
		mDaimeishi += cDaimeishi;
		
		saveText(words  + "\n", "/Volumes/TOSHIBA EXT/result/droid_result_7_14.txt");
		saveText(infoHinshi + "\n", "/Volumes/TOSHIBA EXT/result/droid_result_7_14_hinshi.txt");
		//saveText(questionWord + "\n", "/Users/masashi/java/questiones_out.txt");

	}
	//ファイル読み込み用メソッド
	public static String fileRead(String filePath) {
		mMeishi = 0;
		mDoushi = 0;
		mKeiyoushi = 0;
		mKeiyoudoushi = 0;
		mFukushi = 0;
		mKandoushi = 0;
		mDaimeishi = 0;
		
		FileReader fr = null;
		String ResultRead = "";
		String historyText = "";
		BufferedReader br = null;
		DB_chie db = new DB_chie();

		int i = 0;
		try {
			fr = new FileReader(filePath);
			br = new BufferedReader(fr);
			String line;
			
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
				
				String[] array = line.split(";");
				try{
					if(array[1].indexOf("<<") > 0){
					}else if(array[0].indexOf("|") > 0){					
					}else if(array[0].indexOf("どう思いますか") > 0){
					}else if(array[0].indexOf("どう思う") > 0){
					}else if(historyText.equals(array[0])){
					}else if(array[0].indexOf(historyText.replace("\"", "") + " ") > 0){
					}else{
						historyText = array[0];
						String wakatiText = array[0].replace("\"", "");
						wakati(wakatiText);
						//saveText(array[0]  + "," + array[3] + "\n", "/Users/masashi/java/droid_data_.txt");
					}
					
				}catch(Exception e){					
				}
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
		saveText(String.valueOf(i) + "\n", "/Volumes/TOSHIBA EXT/result/droid_result_6_22_hinshi.txt");
		String infoHinshi = String.valueOf(mMeishi) + "," + String.valueOf(mDoushi) + "," + String.valueOf(mKeiyoushi) + "," + String.valueOf(mKeiyoudoushi) + "," + String.valueOf(mFukushi) + "," + String.valueOf(mKandoushi) + "," + String.valueOf(mDaimeishi);
		saveText(infoHinshi + "\n", "/Volumes/TOSHIBA EXT/result/droid_result_6_22_hinshi.txt");
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
