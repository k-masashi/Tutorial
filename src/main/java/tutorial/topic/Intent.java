package tutorial.topic;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Intent {
	private static int countTOPIC;
	private static double[] arrayScore;
	private static String[] arrayWakati;
	
	private static String[] labelSearch = {"検索","調べる","意味"};
	private static String[] labelMovie = {"見る","みたい","見たい","聞く","聴く","視聴"};
	private static String[] labelShohin = {"商品","欲しい","買う"};
	private static String[] labelMove = {"行く","駅","乗り換え","いく","移動","交通","ルート","行き方"};
	private static String[] labelMap = {"地図","コンビニ","現在地","ここ","何処","どこ"};
	private static String[] labelGurmet = {"食べる","飲む","食事","料理","レストラン","居酒屋","酒"};
	private static String[] labelInfo = {"ニュース","出来事","できごと","情報"};
	private static String[] labelApp = {"アプリ","起動","開く"};
	private static String[] labelContact = {"連絡","電話","メール"};
	private static String[] labelGreet = {"おはよう","ありがとう","こんにちは","こんばんは","うーん"};
	private static String[] labeltalk = {"誕生","歌","話す","アシスタント","ドロイド","かわいい","可愛い"};
	
	private static Integer[] intentSearch = {72,70};
	private static Integer[] intentMovie = {12,68};
	private static Integer[] intentRoute = {34,54,69,75,86,96,58};
	private static Integer[] intentMap = {50,41};
	private static Integer[] intentGourmet = {22,39};
	private static Integer[] intentNews = {62,9,59,82};
	private static Integer[] intentApp = {6,12};
	private static Integer[] intentContact = {63,34,58,69};
	private static Integer[] intentGreet = {54,55,59,61};
	private static Integer[] intentState = {12,20,23,37,56,58,60,81,81,82,87,90};
	private static Integer[] intentWeather = {0,38,77,85,87};
	
	public static void main(String[] args) {
		countTOPIC = 100;
		arrayScore = new double[countTOPIC];
		String query = "背を伸ばしたい";
		String str = executeGet(query);
		System.out.println("問いかけ:" + query );
		System.out.println("応答文  :" + str);
		arrayWakati = tutorial.parse.PaserUtil.wakati(str);
		for(int i = 0; i < arrayScore.length; i++){
			arrayScore[i] = 0.0;
		}
		checkTopic();
	}

	public static void checkTopic(){
		  try {
			   // JDBCドライバーの指定
			   Class.forName("org.sqlite.JDBC");			 
			   // データベースに接続する なければ作成される
			   Connection con = DriverManager.getConnection("jdbc:sqlite:/Volumes/TOSHIBA EXT/result/chie_sparse_714_100.sqlite3");
			   Statement stmt = con.createStatement();
			   String sql = "select * from WORD where ";
			   int l = 0;
			   for (int k=0; k < arrayWakati.length; k++){
				   if(!arrayWakati[k].equals("")){
					   if(l==0){
						   sql += "(WORD='" + arrayWakati[k] + "')";   
					   }else{
						   sql += " or (WORD='" + arrayWakati[k] + "')";
					   }					   
					   l++;
				   }
				    
			   }
			   sql += ";";
			   ResultSet rs = stmt.executeQuery(sql);			    
			   while( rs.next() ) {
				   arrayScore[rs.getInt("TOPIC")] += rs.getDouble("P");
			       //System.out.println(rs.getInt("TOPIC") + " , " + rs.getString("WORD") + " = " + rs.getString("P"));
			   }
			   
				double maxScore = 0.0;
				int maxIndex = -1;
				double secondScore = 0.0;
				int secondIndex = -1;
				double thirdScore = 0.0;
				int thirdIndex = -1;

	
				for (int i = 0; i < arrayScore.length; i++) {
					if(i==72 ||i==70 ||i==12 ||i==68 ||i==34 ||i==54 ||i==69 ||i==75 ||i==86 ||i==96 ||i==58 ||i==50 ||i==41 ||i==22 ||i==39 ||i==62 ||i==9 ||i==59 ||i==82 ||i==6 ||i==12 ||i==63 ||i==34 ||i==58 ||i==69 ||i==54 ||i==55 ||i==59 ||i==61 ||i==12 ||i==20 ||i==23 ||i==37 ||i==56 ||i==58 ||i==60 ||i==81 ||i==82 ||i==87 ||i==90 ||i==0 ||i==38 ||i==77 ||i==85 ||i==87 ){
						if(arrayScore[i] > 0.008){
							//System.out.println("TOPIC" + i + ": " + arrayScore[i]);						
						}
						if (maxScore < arrayScore[i]) {
							if(thirdIndex != secondIndex){
								thirdIndex = secondIndex;
								thirdScore = secondScore;							
							}
							
							if(secondIndex != maxIndex){
								secondIndex = maxIndex;
								secondScore = maxScore;							
							}
							
							maxScore = arrayScore[i];
							maxIndex = i;
						}
						
					}
				}
												   
				System.out.println("ドメイン1: " + checkArray(maxIndex) + " - " + maxScore);
				System.out.println("ドメイン2: " + checkArray(secondIndex) + " - " + secondScore);
				System.out.println("ドメイン3: " + checkArray(thirdIndex) + " - " + thirdScore);
			   //トピックのドメインを選択
				String sql2 = "select * from WORD where TOPIC = " + String.valueOf(maxIndex) + " order by P desc limit 30;";
			   ResultSet rs2 = stmt.executeQuery(sql2);
			   while( rs2.next() ) {
			       //System.out.println("ドメインワード:" + rs2.getString("WORD"));
			   }
			   
			  } catch (ClassNotFoundException e) {
			   e.printStackTrace();
			  } catch (SQLException e) {
			   // Connection の例外が発生した時			 
			   e.printStackTrace();
			  }
		  	  		  
	}
		
	public static String checkArray(int intentNumber){
		String result = "";
		System.out.println(intentNumber);
        if(Arrays.asList(intentSearch).contains(intentNumber)){
        	result += "情報検索 ";
        }
        if(Arrays.asList(intentMovie).contains(intentNumber)){
        	result += "音楽・動画 ";
        }
        if(Arrays.asList(intentRoute).contains(intentNumber)){
        	result += "ルート検索 ";
        }
        if(Arrays.asList(intentGourmet).contains(intentNumber)){
        	result += "グルメ ";
        }
        if(Arrays.asList(intentApp).contains(intentNumber)){
        	result += "アプリ ";
        }
        if(Arrays.asList(intentContact).contains(intentNumber)){
        	result += "連絡 ";
        }
        if(Arrays.asList(intentGreet).contains(intentNumber)){
        	result += "挨拶・相槌 ";
        }
        if(Arrays.asList(intentState).contains(intentNumber)){
        	result += "雑談 ";
        }
        if(Arrays.asList(intentWeather).contains(intentNumber)){
        	result += "天気 ";
        }
		return result;
	}
	
    private static String executeGet(String str) {
        String ret = "";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            //HttpGet getMethod = new HttpGet("http://realfind.jp/droid_new_notAPI.php?word=" + str + "&query=" + str);
        	HttpGet getMethod = new HttpGet("http://realfind.jp/ConvAPI/conv_API.php?word=" + str + "&query=" + str);

            try (CloseableHttpResponse response = httpClient.execute(getMethod)) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    ret = (EntityUtils.toString(entity,StandardCharsets.UTF_8));
                    ret = ret.split("<==>")[0];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        ret = ret.replace("<==>","");
        ret = ret.replace("<q","");
        ret = ret.replace("<br />","。");
        ret = ret.replace(">","");
        ret = ret.replace("$$$","");
       return ret;        
    }

}
