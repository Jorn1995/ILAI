package controller;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFrame;


public class Start {
	
	private JFrame frame;

	public static void main(String[] args) {
		File dir  = new File(new File("").getAbsolutePath()+"\\Test\\");
		String[] choices = new String[dir.listFiles().length];
		File[] dirFiles = dir.listFiles();
		for (int i = 0; i< dirFiles.length; i++) {
			choices[i] = dirFiles[i].getName();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					views.Start window = new views.Start(choices);
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
//		String testfilepath = new File("").getAbsolutePath()+"\\Test\\blogs\\M\\M-test11.txt";
//		File test = new File(path);
//		File testfile = new File(testfilepath);
//		String[] classes = new String[2];
//		classes[0] = "M";
//		classes[1] = "F";
//		Classifier classifier = new Classifier();
//		classifier.TrainBinominalNaiveBayes(classes, test);
//		classifier.ApplyBinominalNaiveBayes(classes, testfile, test);

	}
}
