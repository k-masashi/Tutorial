package tutorial.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CsjParser {	
	private String dirName;//xmlファイルが入っているフォルダのパス 
	private ArrayList<String> corpus; //corpusリスト: １要素が１スピーチに対応(単語のみ)
	private ArrayList<String> speaker; //speakerリスト 1要素が１スピーチに対応
	private ArrayList<String> sentence; //sentenceリスト 1要素が１スピーチに対応(原文の復元文章)
	private String saveCorpus; 
	private String saveSentence;
	
	public CsjParser(String d) throws ParserConfigurationException, SAXException, IOException{
		dirName = d;
		corpus = new ArrayList<String>();
		speaker = new ArrayList<String>();
		sentence = new ArrayList<String>();
		saveCorpus = "";
		
		saveSentence = "";
	    File dir = new File(d);
	    File[] files = dir.listFiles();
	    for (int i = 0; i < files.length; i++) {
	    	System.out.println(i);
	        if(getSuffix(files[i].toString()).equals("xml")){
	        	domRead(files[i].toString());
	        }
	    }
	    saveText(saveCorpus,dirName + "/corpus2.txt");
	    saveText(saveSentence,dirName + "/sentence.txt");
	}
	
	//コーパス(配列)を返す
	public ArrayList<String> getCorpus() {
		return corpus;
	}
	//書き起こし文章の配列を返す
	public ArrayList<String> getSentence() {
		return sentence;
	}
	//コーパスを保存したファイル名を返す
	public String getCorpusFile(){
		return dirName + "/corpus.txt";
	}
	//文章を保存したファイル名を返す
	public String getSentenceFile(){
		return dirName + "/sentence.txt";
	}
	
	public void domRead(String file) throws ParserConfigurationException, SAXException, IOException{
		String strCorpus = "";
		String strSentence = "";
		String nameSpeaker = "";
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		Document document = documentBuilder.parse(file);
		Element root = document.getDocumentElement();
		nameSpeaker = root.getAttribute("SpeakerID");
		NodeList rootChildren = root.getChildNodes();
		
		for(int i=0; i < rootChildren.getLength(); i++) {
			Node node = rootChildren.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element)node;
				if (element.getNodeName().equals("ClauseUnit")) {
					NodeList clauseList = node.getChildNodes();
					for (int j=0; j < clauseList.getLength(); j++) {
						Node node2 = clauseList.item(j);
						NodeList bunsetsuList = node2.getChildNodes();
						
						for (int k=0; k < bunsetsuList.getLength(); k++){
							Node node3 = bunsetsuList.item(k);
							NodeList suwList = node3.getChildNodes();
							if (node3.getNodeType() == Node.ELEMENT_NODE) {
								for (int l=0; l < suwList.getLength(); l++){
									Node node5 = suwList.item(l);
									if (node5.getNodeType() == Node.ELEMENT_NODE) {
										Element element2 = (Element)node5;
										if (node5.getNodeName().equals("SUW")) {
											strSentence += " " + element2.getAttribute("PlainOrthographicTranscription");
											if(element2.getAttribute("SUWPOS").equals("名詞")){
												if(element2.getAttribute("PlainOrthographicTranscription").length() > 1){
													strCorpus += " " + element2.getAttribute("PlainOrthographicTranscription");													
												}
											}											
											if(element2.getAttribute("SUWPOS").equals("感動詞")){
												strSentence += "、";
											}
											if(element2.getAttribute("ClauseBoundaryLabel").equals("[文末]")){
												strSentence += "\n";
											}
										}

									}
								}
							}

						}
					}
				}
			}


		}
		strCorpus = strCorpus.substring(1);
		sentence.add(strSentence);
		corpus.add(strCorpus);
		speaker.add(nameSpeaker);
		if(saveSentence.equals("")){
			saveSentence = strSentence;
			saveCorpus = strCorpus;
		}else{
			saveSentence += "\n" + strSentence;
			saveCorpus += "\n" + strCorpus;
		}
	}
	//拡張子を取得するメソッド
	public String getSuffix(String fileName) {
	    if (fileName == null)
	        return null;
	    int point = fileName.lastIndexOf(".");
	    if (point != -1) {
	        return fileName.substring(point + 1);
	    }
	    return fileName;
	}
	
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
	
}
