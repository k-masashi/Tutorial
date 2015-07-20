package tutorial.topic;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import javax.swing.JTextArea;

public class GetAnswer {

    public static void main(String[] args) throws UnsupportedEncodingException{
    	Object o1 = null;
		try {
			String keyword = URLEncoder.encode("試験","utf-8");
			o1 = new Getter5(new URL("http://chiebukuro.search.yahoo.co.jp/search?p=" + keyword));
		} catch (MalformedURLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		//((Getter5)o1).Show();
		String result = ((Getter5)o1).getHtml();
		String[] array = result.split("<div class=\"KSsin\"");
		for(int i=1; i < array.length; i++){
			System.out.println("------");
			System.out.println(array[i]);
		}
    }
}
class Getter5{
	private String charset = "utf-8";
	private JTextArea htmlArea;
	
	public Getter5(URL url){
		htmlArea = new JTextArea();
        // Webページを読み込む
        try {
            // 接続
            URLConnection uc = url.openConnection();
            // HTMLを読み込む
            BufferedInputStream bis = new BufferedInputStream(uc.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(bis, charset));
            htmlArea.setText("");//初期化
            String line;
            while ((line = br.readLine()) != null) {
                htmlArea.append(line + "\n");
            }
        } catch (MalformedURLException ex) {
            htmlArea.setText("URLが不正です。");
            ex.printStackTrace();
        } catch (UnknownHostException ex) {
            htmlArea.setText("サイトが見つかりません。");
        } catch (IOException ex) {
            ex.printStackTrace();
        }    
    }
	
	public String getHtml(){
		return htmlArea.getText();
	}
	
	public void Show(){
		System.out.println(htmlArea.getText());
	}
}