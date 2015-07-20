package tutorial.util;

import java.util.Set;

import edu.cmu.lti.jawjaw.JAWJAW;
import edu.cmu.lti.jawjaw.pobj.POS;
public class Wordnet {

	public static void main(String[] args) {
		//AdvancedDemo.run( "踊る", POS.v );
		//AdvancedDemo.run( "踊る", POS.r );
		AdvancedDemo.run( "会社", POS.n );
		System.out.println(AdvancedDemo.getHypernyms( "会社", POS.n ));
		//AdvancedDemo.run( "踊る", POS.v );
	}

	public static class AdvancedDemo {
		private static void run( String word, POS pos ) {
	        // ファサードから日本語 WordNet にアクセス
	        Set<String> hypernyms = JAWJAW.findHypernyms(word, pos);
	        Set<String> hyponyms = JAWJAW.findHyponyms(word, pos);
	        Set<String> consequents = JAWJAW.findEntailments(word, pos);
	        Set<String> translations = JAWJAW.findTranslations(word, pos);
	        Set<String> definitions = JAWJAW.findDefinitions(word, pos);
	        // 結果表示
	        System.out.println( "hypernyms of "+word+" : \t"+ hypernyms );
	        System.out.println( "hyponyms of "+word+" : \t"+ hyponyms );
	        System.out.println( word+" entails : \t\t"+ consequents );
	        System.out.println( "translations of "+word+" : \t"+ translations );
	        System.out.println( "definitions of "+word+" : \t"+ definitions );		
	   }
	
		
		public static String getHypernyms(String word, POS pos){
			Set<String> hypernyms = JAWJAW.findHypernyms(word, pos);
			Object[] strings= hypernyms.toArray();
			StringBuilder builder = new StringBuilder();

			for(Object str : strings) {
			  builder.append(str).append(" ");
			}

			String result = builder.substring(0, builder.length() - 1);
			return result;
		}
	}	
	
	
}
