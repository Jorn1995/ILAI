package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Classifier {

	List<String> vocabulary;
	List<String> vocabularyInClass;
	int countNumberOfDocs;
	int countDocsInClass;
	float prior;
	static File[] files = new File[0];
	Map<String, Map<String, Double>> map;
	Map<String, Double> priormap;
	Classifier classifier;


	public static void main(String[] args) {
		
		String path = new File("").getAbsolutePath()+"\\Train\\blogs";
		String testfilepath = new File("").getAbsolutePath()+"\\Test\\blogs\\M\\M-test11.txt";
		File test = new File(path);
		File testfile = new File(testfilepath);
		String[] classes = new String[2];
		classes[0] = "M";
		classes[1] = "F";
		Classifier classifier = new Classifier();
		classifier.TrainBinominalNaiveBayes(classes, test);
		classifier.ApplyBinominalNaiveBayes(classes, testfile, test);

	}

	public void TrainBinominalNaiveBayes(String[] c, File folder) {
		map = new HashMap<String, Map<String, Double>>();
		vocabulary = ExtractVocabulary(folder);
		countNumberOfDocs = CountNumberOfDocs(folder);
		priormap = new HashMap<String, Double>();
		for(String sort : c){
			Map<String, Double> tempMap = new HashMap<String, Double>();
			countDocsInClass = countDocsInClass(sort, folder);
			vocabularyInClass = ConcatenateAllTextsOfDocsInClass(sort, folder);
			priormap.put(sort, (double)countDocsInClass/countNumberOfDocs);
			for(String t : vocabulary) {
				double chance = 0.0;
				int countOfWord = 0;
				int countOfAllWords = 0;
				for(String s : vocabularyInClass) {
					countOfAllWords++;
					if(t.equals(s)) {
						countOfWord++;
					}
				}
				chance = Math.log(countOfWord+1) - Math.log(countOfAllWords+2);
				System.out.println("chance = " + t + chance);
				tempMap.put(t, chance);
			}
			map.put(sort, tempMap);
			
		}
	}
	
	public String ApplyBinominalNaiveBayes(String[] c, File file, File folder){
		List<String> vocabularyOfFile = ExtractVocabularyFromFile(file);
		Map<String, Double> classA = map.get(c[0]);
		Map<String, Double> classB = map.get(c[1]);
		Map<String, Double> determineMap = new HashMap<String, Double>();
		for(String sort : c) {
			double score = Math.log(priormap.get(sort));
			System.out.println("Score sort:" + score);
			vocabularyInClass = ConcatenateAllTextsOfDocsInClass(sort, folder);
			 for(String t : vocabularyInClass){
				 score += Math.log(map.get(sort).get(t));
				 //System.out.println("Score t:" + score);
			 } 
			 determineMap.put(sort, score); 
		} 
		Entry<String,Double> maxEntry = null;
		for(Entry<String,Double> entry : determineMap.entrySet()) {
		    if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
		        maxEntry = entry;
		    }
		} 
		System.out.println(maxEntry.getKey());
		return maxEntry.getKey();
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
	public List<String> ExtractVocabularyFromFile(File file){
		List<String> tokenizedResult = new ArrayList<String>();
		String result = null;
		//files = fileLister(folder);
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while(br.ready()) {
				result += br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			tokenizedResult = new Tokenizer().tokenizer(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return tokenizedResult;
		
	}
}
