package main;

import java.awt.Dimension;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class ControllerHandler extends JFrame {
	private Controller controller;

	public ControllerHandler(Controller controller) {
		this.controller = controller;

		initWidgets();
		setFrame();
	}

	private void initWidgets() {
		pack();
	}

	private void setFrame() {
		setSize(new Dimension(1300, 250));
		setLocation(10, 550);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}

}
