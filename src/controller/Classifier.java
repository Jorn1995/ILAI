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
	int countNumberOfDocs;
	float prior;
	static File[] files = new File[0];
	Map<String, Map<String, Double>> map;
	Map<String, Double> priormap;
	Classifier classifier;
	static Map<String, List<String>> fileMap;


	public static void main(String[] args) {

		String path = new File("").getAbsolutePath()+"\\Train\\blogs";
		File test = new File(path);
		//ExtractVocabulary(test);
				String testPathF = new File("").getAbsolutePath()+"\\Test\\blogs\\F";
				String testPathM = new File("").getAbsolutePath()+"\\Test\\blogs\\M";
				String[] classes = new String[2];
				classes[0] = "M";
				classes[1] = "F";
				Classifier classifier = new Classifier();
				classifier.TrainBinominalNaiveBayes(classes, test);
				int i = 0;
				for (File file : new File(testPathF).listFiles()){
					String result = classifier.ApplyBinominalNaiveBayes(classes, file, test);
					System.out.println("expected class = F");
					if(result == "F") {
						i++;
						
					}
				}
				for (File file : new File(testPathM).listFiles()){
					String result = classifier.ApplyBinominalNaiveBayes(classes, file, test);
					System.out.println("expected class = M");
					if(result == "M") {
						i++;
					}
				}
				System.out.println(((double)i/50.0));
	}

	public void TrainBinominalNaiveBayes(String[] c, File folder) {
		map = new HashMap<String, Map<String, Double>>();
		vocabulary = ExtractVocabulary(folder);
		countNumberOfDocs = CountNumberOfDocs(folder);
		priormap = new HashMap<String, Double>();
		for(String sort : c){
			Map<String, Double> tempMap = new HashMap<String, Double>();
			File[] DocsInClass = getDocsInClass(sort, folder);
			int countDocsInClass = countDocsInClass(sort, folder);
			priormap.put(sort, ((double)countDocsInClass/countNumberOfDocs));
			for(String t : vocabulary) {
				if(!tempMap.containsKey(t)){
					int countDocsContainingWord = 0;
					for(File doc : DocsInClass) {
						List<String> wordsInFile = fileMap.get(doc.getName());
						if(wordsInFile.contains(t)) {
							countDocsContainingWord++;
						}
					}
					double chance = ((double)countDocsContainingWord+1)/((double)countDocsInClass+2);
					System.out.println("word: " + t + "    " + "countDocsContainingWord: " + countDocsContainingWord  + "    " 
							+ "countDocsInClass:" + countDocsInClass + "    " + "chance: " + chance + "class: " + sort);
					tempMap.put(t, chance);
				}
			}
			map.put(sort, tempMap);

		}
	}

	private File[] getDocsInClass(String sort, File folder) {
		File folderOfClass = new File (folder.getAbsolutePath()+"\\"+sort);
		File[] filesOfClass = fileLister(folderOfClass);
		return filesOfClass;
	}

	public String ApplyBinominalNaiveBayes(String[] c, File file, File folder){
		Map<String, Double> determineMap = new HashMap<String, Double>();
		List<String> termsFromDoc = ExtractVocabularyFromFile(file);
		for(String sort : c) {
			List<String> vocabularyInClass = ConcatenateAllTextsOfDocsInClass(sort, folder);
			double score = Math.log(priormap.get(sort));
			for(String t : vocabularyInClass) {
				if(termsFromDoc.contains(t)) {
					score += Math.log(map.get(sort).get(t));
				} else {
					score += Math.log((((double)1)-(map.get(sort).get(t))));
				}
			}
			System.out.println("class: " + sort + " score: " + score);
			determineMap.put(sort, score);
		}
		
		Entry<String,Double> maxEntry = null;
		for(Entry<String,Double> entry : determineMap.entrySet()) {
			if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
				maxEntry = entry;
			}
		} 
		System.out.println("result class: " + maxEntry.getKey());
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
		List<String> tokenizedResult = new ArrayList<String>();
		fileMap = new HashMap<String, List<String>>();
		for(File doc: fileLister(folder)) {
			List<String> temp = ExtractVocabularyFromFile(doc);
			for(String line : temp) {
				tokenizedResult.add(line);
			}
			fileMap.put(doc.getName(), temp);
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
	public static List<String> ExtractVocabularyFromFile(File file){
		List<String> tokenizedResult = new ArrayList<String>();
		String result = null;
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
