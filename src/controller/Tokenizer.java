package controller;


import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.io.IOException;
public class Tokenizer {
	
	public List<String> tokenizer(String input) throws IOException {
		StringTokenizer st = new StringTokenizer(input);
	    List<String> result = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			result.add(st.nextToken().replaceAll("\\p{Punct}+", "").toLowerCase());
	     }
		return result;
	}
}
