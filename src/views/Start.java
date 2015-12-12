package views;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JList;
import javax.swing.JTree;

import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;

public class Start {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
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
	}

	/**
	 * Create the application.
	 */
	public Start(String[] choices) {
		initialize(choices);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String[] choices) {
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
		Object cmboitem = jComboBox1.getSelectedItem();
	    
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

}
