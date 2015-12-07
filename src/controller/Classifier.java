package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Classifier {

	String vocabulary;
	String vocabularyInClass;
	int countNumberOfDocs;
	int countDocsInClass;
	int prior;
	static File[] files = new File[0];
	
	public static void main(String[] args) {
		File test = new File("C:\\Users\\JornB\\Downloads\\blogs");
		System.out.println(CountNumberOfDocs(test));
	}
	
	public void TrainMultinomialNaiveBayes(List<Class> c, File folder) {		
		vocabulary = ExtractVocabulary(folder);
		countNumberOfDocs = CountNumberOfDocs(folder);
		for(Class sort : c){
			countDocsInClass = countDocsInClass(sort, folder);
			prior = countDocsInClass/countNumberOfDocs;
			vocabularyInClass = ConcatenateAllTextsOfDocsInClass(folder, c);
		}
		
	}

	private String ConcatenateAllTextsOfDocsInClass(File folder, List<Class> c) {
		
		return null;
	}

	private int countDocsInClass(Class a, File folder) {
		return 0; 
	}

	private static int CountNumberOfDocs(File folder) {
		File[] files = fileLister(folder);
		return files.length;
	}

	private String ExtractVocabulary(File folder) {
		files = fileLister(folder);
		String result = "";
		 for (File file : files) {
			 
		    }
		return result;
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
