package views;

import java.awt.EventQueue;

import java.io.File;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import controller.Classifier;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import controller.Classifier;

public class TestFile {

	private JFrame frame;


	/**
	 * Create the application.
	 */
	public TestFile(String folder, Classifier classifier) {
		initialize(folder, classifier);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String folder, Classifier classifier) {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		getFrame().getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel txtGetraind;
		txtGetraind = new JLabel();
		txtGetraind.setText("Getraind!");
		panel.add(txtGetraind);
		
		File dir = new File(new File("").getAbsolutePath()+"\\Test\\"+folder);
		File[] fileArray = Classifier.fileLister(dir);
		String[] files = new String[fileArray.length];
		for (int i = 0; i< fileArray.length; i++) {
			files[i] = fileArray[i].getName();
		}
		JComboBox jComboBox2 = new JComboBox();
		for(String name : files) {
			jComboBox2.addItem(name);
		}
		panel.add(jComboBox2);
		
		JButton btnTest = new JButton("Test");
		btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cmboitem2 = (String) jComboBox2.getSelectedItem();
				File testFile = null;
				for (int i = 0; i< fileArray.length; i++) {
					if(fileArray[i].getName().equals(cmboitem2)) {
						testFile = fileArray[i];
					}
				}
				File classDir = new File(new File("").getAbsolutePath()+"\\Test\\"+folder);
				String[] classes = new String[classDir.listFiles().length];
				File[] classDirFiles = classDir.listFiles();
				for (int i = 0; i< classDirFiles.length; i++) {
					classes[i] = classDirFiles[i].getName();
				}
				String result = classifier.ApplyBinominalNaiveBayes(classes, testFile, classDir);
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							Result window = new views.Result(result);
							window.getFrame().setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		panel.add(btnTest);
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

}
