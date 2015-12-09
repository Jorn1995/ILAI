package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Classifier {

	List<String> vocabulary;
	List<String> vocabularyInClass;
	int countNumberOfDocs;
	int countDocsInClass;
	float prior;
	static File[] files = new File[0];
	Map<String, Map<String, Double>> map;

	public static void main(String[] args) {
		String path = new File("").getAbsolutePath()+"\\Train\\blogs";
		File test = new File(path);
		String[] classes = new String[2];
		classes[0] = "M";
		classes[1] = "F";
		new Classifier().TrainBinominalNaiveBayes(classes, test);

	}

	public void TrainBinominalNaiveBayes(String[] c, File folder) {
		map = new HashMap<String, Map<String, Double>>();
		vocabulary = ExtractVocabulary(folder);
		countNumberOfDocs = CountNumberOfDocs(folder);
		for(String sort : c){
			Map<String, Double> tempMap = new HashMap<String, Double>();
			countDocsInClass = countDocsInClass(sort, folder);
			vocabularyInClass = ConcatenateAllTextsOfDocsInClass(sort, folder);
			for(String t : vocabulary) {
				double chance = 0;
				int countOfWord = 0;
				int countOfAllWords = 0;
				for(String s : vocabularyInClass) {
					countOfAllWords++;
					if(t.equals(s)) {
						countOfWord++;
					}
				}
				chance = (countOfWord+1)/(countOfAllWords+2);
				System.out.println(chance);
				tempMap.put(t, chance);
			}
			map.put(sort, tempMap);
		}
	}
	
	public void ApplyBinominalNaiveBayes(String[] c, File file){
		List<String> vocabularyOfFile = ExtractVocabulary(file);
		Map<String, Double> classA = map.get(c[0]);
		Map<String, Double> classB = map.get(c[1]);
		for(String word : vocabularyOfFile) {
			
		}
	}

	private static List<String> ConcatenateAllTextsOfDocsInClass(String c, File folder) {
		File folderOfClass = new File (folder.getAbsolutePath()+"\\"+c);
		return ExtractVocabulary(folderOfClass);
	}

	private static int countDocsInClass(String c, File folder) {
		File folderOfClass = new File (folder.getAbsolutePath()+"\\"+c);
		File[] filesOfClass = fileLister(folderOfClass);
		return filesOfClass.length; 
	}

	private static int CountNumberOfDocs(File folder) {
		files = fileLister(folder);
		return files.length;
	}

	private static List<String> ExtractVocabulary(File folder) {
		files = fileLister(folder);
		List<String> tokenizedResult = new ArrayList<String>();
		String result = "";
		for (File file : files) {
			try {
				String line ="";
				BufferedReader br = new BufferedReader(new FileReader(file));
				while(br.ready()) {
					result += br.readLine();
				}
			} catch (FileNotFoundException e) {

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			tokenizedResult = new Tokenizer().tokenizer(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tokenizedResult;
	}

	private static File[] fileLister(File folder) {
		files = new File[0];
		File[] filesInFolder = folder.listFiles();
		for (File file : filesInFolder) {
			if(file.isDirectory()) {
				File[] temp = files;
				File[] directory = fileLister(file);	
				files = new File[temp.length+directory.length];
				for(int i = 0; i<temp.length; i++) {
					files[i] = temp[i];
				}
				for(int j = temp.length ; j<(temp.length+directory.length); j++) {
					files[j] = directory[j-temp.length];
				}
			} else {
				File[] temp = new File[files.length];
				for(int i = 0; i<temp.length; i++) {
					temp[i] = files[i];
				}
				files = new File[temp.length+1];
				for(int i = 0; i<temp.length; i++) {
					files[i] = temp[i];
				}
				files[temp.length] = file;
			}
		}	
		return files;
	}
}
