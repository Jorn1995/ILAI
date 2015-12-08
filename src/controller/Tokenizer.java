package controller;


import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.io.IOException;
public class Tokenizer {
	
	
	
	/*public void getTexts(File folder) throws IOException {
		File[] filelist = folder.listFiles();
		for(int i = 0; i <= filelist.length-1; i++){
			List<String> lines = Files.readAllLines(filelist[i].toPath(), Charset.forName("ISO-8859-1"));
			System.out.println("Raw text " + lines);
			List<String> lines2 = lines.subList(1, (lines.size()-1));
			classA += lines2;
		}
	}
	*/
	
	public List<String> tokenizer(String input) throws IOException {
		StringTokenizer st = new StringTokenizer(input);
	    List<String> result = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			result.add(st.nextToken().replaceAll("\\p{Punct}+", "").toLowerCase());
	     }
		System.out.println("result: " + result);
		return result;
	}

	public static void main(String[] arg) throws IOException{
		Tokenizer test = new Tokenizer();
		test.tokenizer("Dit is een, test");
	}
}
