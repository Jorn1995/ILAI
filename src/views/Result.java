package views;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Result {

	private JFrame frame;


	/**
	 * Create the application.
	 */
	public Result(String result) {
		initialize(result);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String result) {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		getFrame().getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel txtGetest;
		txtGetest = new JLabel();
		txtGetest.setText("Getest!");
		panel.add(txtGetest);
		
		JLabel txtResult;
		txtResult = new JLabel();
		txtResult.setText("Resultat: " + result);
		panel.add(txtResult);
		
	}


	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

}
