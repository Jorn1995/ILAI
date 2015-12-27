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
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							TestFile window = new views.TestFile(cmboitem, classifier);
							window.getFrame().setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		panel.add(btnTrain);
	    
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

}
