package start;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import controller.Classifier;

public class TUI {
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
					.println("What folder or file in the test folder is to be tested ");
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
			File newfile = null;
			String rightfolder = "";
			if (filesnames.contains(testclass)) {
				if (testrealpath.isDirectory()) {
					for (File testfolders : filesintestfolder) {
						File[] t = new File(testfolders.getAbsolutePath())
								.listFiles();
						for (File file : t) {
							totalTestCount++;
							String result = classifier
									.ApplyBinominalNaiveBayes(classes, file,
											testrealpath);
							System.out.println("filename = " + file.getName());
							System.out.println("Name = " + file.getName()
									+ " result = " + result);
							System.out.println("Is " + result
									+ " the right class for " + file.getName()
									+ "? Y/N");
							String feedback = user_input.next();
							if (feedback.equals("Y") || feedback.equals("y")) {
								newfile = new File(
										trainrealpath.getAbsolutePath() + "\\"
												+ result + "\\"
												+ file.getName());
							} else if (feedback.equals("N")
									|| feedback.equals("n")) {
								System.out
										.println("To which class does the test file have to be copied?");
								rightfolder = user_input.next();
								newfile = new File(
										trainrealpath.getAbsolutePath() + "\\"
												+ rightfolder + "\\"
												+ file.getName());
							}
							try {
								newfile.createNewFile();
								System.out.println(newfile.getAbsolutePath());
								PrintWriter writer = new PrintWriter(
										newfile.getAbsolutePath(), "UTF-8");
								writer.println(Classifier.ReadFile(file));
								System.out.println("File added to "
										+ rightfolder + " increase accuracy");
								writer.close();
							} catch (IOException e) {
								e.printStackTrace();
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
					if (feedback.equals("Y") || feedback.equals("y")) {
						newfile = new File(trainrealpath.getAbsolutePath()
								+ "\\" + result + "\\" + testrealpath.getName());
					} else if (feedback.equals("N") || feedback.equals("n")) {
						System.out
								.println("To which class does the test file have to be copied?");
						rightfolder = user_input.next();
						newfile = new File(trainrealpath.getAbsolutePath()
								+ "\\" + rightfolder + "\\"
								+ testrealpath.getName());
					}
					try {

						newfile.createNewFile();
						System.out.println(newfile.getAbsolutePath());
						PrintWriter writer = new PrintWriter(
								newfile.getAbsolutePath(), "UTF-8");
						writer.println(Classifier.ReadFile(testrealpath));
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

					totalTestCount++;
				}

			} else if (testclass.equals("exit")) {
				System.out.println("The application will be terminated.");
			} else {
				System.out.println("This is not a valid folder.");
			}
			boolean isvalidcommand = false;
			while (!isvalidcommand) {
				System.out.println("1. Retrain with new train files");
				System.out.println("2. Test a new file");
				System.out.println("3. exit");
				user_input.nextLine();
				String nextcommand = user_input.nextLine();
				if (nextcommand.equals("1")) {
					classifier.TrainBinominalNaiveBayes(classes, trainrealpath);
					System.out.println("Application has been retrained");
					isvalidcommand = true;
				} else if (nextcommand.equals("2")) {
					System.out.println("Select new file/folder to test");
					isvalidcommand = true;
				} else if (nextcommand.equals("3")) {
					System.out.println("The application will be terminated.");
					isvalidcommand = true;
				} else {
					System.out
							.println("This is not a valid command. Input convention is just the number.");
				}

			}
		}
		user_input.close();

	}
}
