package tutorial.util;

import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

public class MakeId {

	public static void main(String[] args) {
		System.out.println(makeId());
	}
	public static String makeId(){
	    TimeZone tz1 = TimeZone.getTimeZone("Europe/London");
	    Calendar cal1 = Calendar.getInstance(tz1);
		String i;
        //Randomクラスの生成
        Random r = new Random();
        //乱数の取得
        int idNumber = r.nextInt(1000);
        i = cal1.get(Calendar.SECOND)+ cal1.get(Calendar.MINUTE) +  cal1.get(Calendar.HOUR_OF_DAY)  + String.valueOf(idNumber);
		return i;
	}
}
