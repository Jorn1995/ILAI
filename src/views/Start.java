package views;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import controller.Classifier;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class Start {

	private JFrame frame;
	private JTextField txtGetraind;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					views.Start window = new views.Start();
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Start() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Classifier classifier = new Classifier();
		File dir  = new File(new File("").getAbsolutePath()+"\\Train\\");
		String[] choices = new String[dir.listFiles().length];
		File[] dirFiles = dir.listFiles();
		for (int i = 0; i< dirFiles.length; i++) {
			choices[i] = dirFiles[i].getName();
		}
		setFrame(new JFrame());
		getFrame().setBounds(100, 100, 450, 300);
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		getFrame().getContentPane().add(panel, BorderLayout.CENTER);
		
		JComboBox jComboBox1 = new JComboBox();
		for(String folder : choices) {
			jComboBox1.addItem(folder);
		}
		panel.add(jComboBox1);
		
		JButton btnTrain = new JButton("Train");
		btnTrain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cmboitem = (String) jComboBox1.getSelectedItem();

				File classDir = new File(new File("").getAbsolutePath()+"\\Test\\"+cmboitem);
				String[] classes = new String[classDir.listFiles().length];
				File[] classDirFiles = classDir.listFiles();
				for (int i = 0; i< classDirFiles.length; i++) {
					classes[i] = classDirFiles[i].getName();
				}
				classifier.TrainBinominalNaiveBayes(classes, new File(new File("").getAbsolutePath()+"\\Train\\"+cmboitem));	
				txtGetraind = new JTextField();
				txtGetraind.setText("Getraind!");
				panel.add(txtGetraind);
				txtGetraind.setColumns(10);
			}
		});
		panel.add(btnTrain);
		
//		File classDir = new File(new File("").getAbsolutePath()+"\\Test\\"+cmboitem);
//		String[] classes = new String[classDir.listFiles().length];
//		File[] classDirFiles = classDir.listFiles();
//		for (int i = 0; i< classDirFiles.length; i++) {
//			classes[i] = classDirFiles[i].getName();
//		}
//		JComboBox jComboBox2 = new JComboBox();
//		for(String folder : classes) {
//			jComboBox2.addItem(folder);
//		}
//		panel.add(jComboBox2);
//		jComboBox2.
//		String cmboitem2 = (String) jComboBox2.getSelectedItem();
//		
//		File fileDir = new File(classDir.getAbsolutePath()+ "\\" +cmboitem2);
//		String[] files = new String[fileDir.listFiles().length];
//		File[] fileArray = fileDir.listFiles();
//		for (int i = 0; i< fileArray.length; i++) {
//			files[i] = fileArray[i].getName();
//		}
//		JComboBox jComboBox3 = new JComboBox();
//		for(String folder : files) {
//			jComboBox3.addItem(folder);
//		}
//		panel.add(jComboBox3);
//		File testFile = new File(fileDir.getAbsolutePath() + "\\" + (String) jComboBox3.getSelectedItem());
//		
//		JButton btnTest = new JButton("Test");
//		btnTest.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {				
//				String result = classifier.ApplyBinominalNaiveBayes(choices, testFile, classDir);
//				txtGetraind = new JTextField();
//				txtGetraind.setText("Getest: " + result);
//				panel.add(txtGetraind);
//				txtGetraind.setColumns(10);
//			}
//		});
//		panel.add(btnTest);
	    
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

}
