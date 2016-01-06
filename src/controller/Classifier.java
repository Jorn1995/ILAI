package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

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

		Scanner user_input = new Scanner(System.in);
		String path = new File("").getAbsolutePath() + "\\Train\\";
		File test = new File(path);
		String testPath = new File("").getAbsolutePath() + "\\Test\\";
		File testfile = new File(testPath);
		System.out
				.println("Which type of file do you want to classify? Possible classes:");
		int i2 = 0;
		File[] trainoutput = test.listFiles();
		while (i2 < trainoutput.length) {
			System.out.println(trainoutput[i2].getName());
			i2++;
		}
		String trainclass = user_input.next();
		File trainrealpath = new File(path + trainclass + "\\");

		System.out.println(trainrealpath.getAbsolutePath());
		File[] filesinfolder = trainrealpath.listFiles();

		String[] classes = new String[filesinfolder.length];
		int j = 0;
		for (File f : filesinfolder) {
			classes[j] = f.getName();
			j++;
		}
		Classifier classifier = new Classifier();
		classifier.TrainBinominalNaiveBayes(classes, trainrealpath);
		String testclass = "";
		while (!testclass.equals("exit")) {
			System.out
					.println("What folder in the test folder is to be tested ");
			int i3 = 0;
			File[] testoutput = testfile.listFiles();
			ArrayList<String> filesnames = new ArrayList<String>();
			while (i3 < testoutput.length) {
				System.out.println(testoutput[i3].getName());
				filesnames.add(testoutput[i3].getName());
				i3++;
			}
			testclass = user_input.next();
			File testrealpath = new File(testPath + testclass + "\\");
			File[] filesintestfolder = testrealpath.listFiles();
			int i = 0;
			int totalTestCount = 0;
			if (filesnames.contains(testclass)) {
				if (testrealpath.isDirectory()) {
					for (File testfolders : filesintestfolder) {
						File[] t = new File(testfolders.getAbsolutePath())
								.listFiles();
						for (File file : t) {
							totalTestCount++;
							System.out.println(classes.toString() + " "
									+ file.getAbsolutePath() + " "
									+ testrealpath.getAbsolutePath());
							String result = classifier
									.ApplyBinominalNaiveBayes(classes, file,
											testrealpath);
							// System.out.println("Is " + result +
							// " the right class for " + file.getName() +
							// "? Y/N");
							// String feedback = user_input.next();
							// if (feedback.equals("Y")){
							//
							// }

							System.out.println("filename = " + file.getName());
							if (file.getName().contains(result)) {
								i++;

							}
						}
					}
				} else {
					String result = classifier.ApplyBinominalNaiveBayes(
							classes, testrealpath, testrealpath);
					System.out.println("Name = " + testrealpath.getName()
							+ " result = " + result);
					System.out.println("Is " + result + " the right class for "
							+ testrealpath.getName() + "? Y/N");
					String feedback = user_input.next();
					if (feedback.equals("Y")) {
						try {
							File newfile = new File(
									trainrealpath.getAbsolutePath() + "\\"
											+ result + "\\"
											+ testrealpath.getName());
							newfile.createNewFile();
							PrintWriter writer = new PrintWriter(
									newfile.getAbsolutePath(), "UTF-8");
							writer.println(ReadFile(testrealpath));
							System.out.println("File added to " + result
									+ " increase accuracy");
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (feedback.equals("N")) {
						try {
							System.out
									.println("To which folder does the test file have to be added in order to increase accuracy? The relative path to the train set "
											+ trainrealpath.getAbsolutePath());
							String rightfolder = user_input.next();
							File newfile = new File(
									trainrealpath.getAbsolutePath() + "\\"
											+ rightfolder + "\\"
											+ testrealpath.getName());
							newfile.createNewFile();
							PrintWriter writer = new PrintWriter(
									newfile.getAbsolutePath(), "UTF-8");
							writer.println(ReadFile(testrealpath));
							System.out.println("File added to " + rightfolder
									+ " increase accuracy");
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
					totalTestCount++;
				}
				System.out.println("Amount of files tested: " + totalTestCount);
				System.out.println("Accuracy = "
						+ (((double) i / (double) totalTestCount)) * 100 + "%");
			} else if (testclass.equals("exit")) {
				System.out.println("The application will be terminated.");
			} else {
				System.out.println("This is not a valid folder.");
			}

		}
		user_input.close();

	}

	public void TrainBinominalNaiveBayes(String[] c, File folder) {
		map = new HashMap<String, Map<String, Double>>();
		vocabulary = ExtractVocabulary(folder);
		countNumberOfDocs = CountNumberOfDocs(folder);
		priormap = new HashMap<String, Double>();
		for (String sort : c) {
			Map<String, Double> tempMap = new HashMap<String, Double>();
			File[] DocsInClass = getDocsInClass(sort, folder);
			int countDocsInClass = countDocsInClass(sort, folder);
			priormap.put(sort, ((double) countDocsInClass / countNumberOfDocs));
			for (String t : vocabulary) {
				if (!tempMap.containsKey(t)) {
					int countDocsContainingWord = 0;
					for (File doc : DocsInClass) {
						List<String> wordsInFile = fileMap.get(doc.getName());
						if (wordsInFile.contains(t)) {
							countDocsContainingWord++;
						}
					}
					double chance = ((double) countDocsContainingWord + 1)
							/ ((double) countDocsInClass + 2);
					System.out.println("word: " + t + "    "
							+ "countDocsContainingWord: "
							+ countDocsContainingWord + "    "
							+ "countDocsInClass:" + countDocsInClass + "    "
							+ "chance: " + chance + " class: " + sort);

					tempMap.put(t, chance);

				}
			}
			map.put(sort, tempMap);

		}
	}

	private File[] getDocsInClass(String sort, File folder) {
		File folderOfClass = new File(folder.getAbsolutePath() + "\\" + sort);
		File[] filesOfClass = fileLister(folderOfClass);
		return filesOfClass;
	}

	public String ApplyBinominalNaiveBayes(String[] c, File file, File folder) {
		Map<String, Double> determineMap = new HashMap<String, Double>();
		List<String> termsFromDoc = ExtractVocabularyFromFile(file);
		for (String sort : c) {
			List<String> vocabularyInClass = ConcatenateAllTextsOfDocsInClass(
					sort, folder);
			double score = Math.log(priormap.get(sort));
			for (String t : termsFromDoc) {
				if (map.get(sort).containsKey(t)) {
					score += Math.log(map.get(sort).get(t));
					// } else {
					// score += Math.log((((double)1)-(map.get(sort).get(t))));
				}
			}
			System.out.println("class: " + sort + " score: " + score);
			determineMap.put(sort, score);
		}

		Entry<String, Double> maxEntry = null;
		for (Entry<String, Double> entry : determineMap.entrySet()) {
			if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
				maxEntry = entry;
			}
		}
		System.out.println("result class: " + maxEntry.getKey());
		return maxEntry.getKey();
	}

	private static List<String> ConcatenateAllTextsOfDocsInClass(String c,
			File folder) {
		File folderOfClass = new File(folder.getAbsolutePath() + "\\" + c);
		return ExtractVocabulary(folderOfClass);
	}

	private static int countDocsInClass(String c, File folder) {
		File folderOfClass = new File(folder.getAbsolutePath() + "\\" + c);
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
		for (File doc : fileLister(folder)) {
			List<String> temp = ExtractVocabularyFromFile(doc);
			for (String line : temp) {
				tokenizedResult.add(line);
			}
			fileMap.put(doc.getName(), temp);
		}
		return tokenizedResult;
	}

	public static File[] fileLister(File folder) {
		files = new File[0];
		File[] filesInFolder = folder.listFiles();
		if (filesInFolder != null) {
			for (File file : filesInFolder) {
				if (file.isDirectory()) {
					File[] temp = files;
					File[] directory = fileLister(file);
					files = new File[temp.length + directory.length];
					for (int i = 0; i < temp.length; i++) {
						files[i] = temp[i];
					}
					for (int j = temp.length; j < (temp.length + directory.length); j++) {
						files[j] = directory[j - temp.length];
					}
				} else {
					File[] temp = new File[files.length];
					for (int i = 0; i < temp.length; i++) {
						temp[i] = files[i];
					}
					files = new File[temp.length + 1];
					for (int i = 0; i < temp.length; i++) {
						files[i] = temp[i];
					}
					files[temp.length] = file;
				}
			}

		}
		return files;
	}

	public static List<String> ExtractVocabularyFromFile(File file) {
		List<String> tokenizedResult = new ArrayList<String>();
		String result = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while (br.ready()) {
				result += br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			if(result.length() > 0){
				tokenizedResult = new Tokenizer().tokenizer(result);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tokenizedResult;

	}

	public static String ReadFile(File file) {
		String result = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while (br.ready()) {
				result += br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
