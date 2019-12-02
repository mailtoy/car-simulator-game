package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import eu.hansolo.steelseries.gauges.Radial;

public class ControllerHandler extends JFrame {
	private static final long serialVersionUID = 1L;
	private Controller controller;
	private Radial gauge = new Radial();

	private JButton forwardBtn, backwardBtn, leftBtn, rightBtn, accelerateBtn, breakBtn, stopBtn;
	private ArrayList<JButton> directionButtons;

	// default color of the button to be repainted when key released
//	private Color cc = new JButton().getBackground();

	public ControllerHandler(Controller controller) {
		this.controller = controller;
		setFrame();
	}

	private void setFrame() {
		setSize(new Dimension(1300, 250));
		setLocation(10, 550);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		initWidgets();
		setVisible(true);
	}

	private void initWidgets() {
		JLabel info = new JLabel("Note");
		info.setFont(new Font("Verdana", Font.BOLD, 14));

		JLabel selectMap = new JLabel("select your map: ");
		selectMap.setForeground(Color.blue);

		gauge.setTitle("Controller");
		gauge.setUnitString("Km / Hr");
		gauge.setPreferredSize(new Dimension(200, 200));

		forwardBtn = new JButton("^");
		backwardBtn = new JButton("v");
		leftBtn = new JButton("<");
		rightBtn = new JButton(">");
		directionButtons = new ArrayList<JButton>();
		directionButtons.add(forwardBtn);
		directionButtons.add(backwardBtn);
		directionButtons.add(leftBtn);
		directionButtons.add(rightBtn);

		accelerateBtn = new JButton("accelerate");
		breakBtn = new JButton("break");
		stopBtn = new JButton("stop");
		addActionListener();

		setLayout(new BorderLayout());
		JPanel gaugePanel = new JPanel();
		JPanel keyboardPanel = new JPanel();
		JPanel directionPanel = new JPanel();
		JPanel operationPanel = new JPanel();

		add(gaugePanel, BorderLayout.WEST);
		add(keyboardPanel, BorderLayout.EAST);

		gaugePanel.setLayout(new FlowLayout());
		gaugePanel.add(gauge);

		operationPanel.setLayout(new FlowLayout());
		operationPanel.add(stopBtn);
		operationPanel.add(breakBtn);
		operationPanel.add(accelerateBtn);

		directionPanel.add(forwardBtn, BorderLayout.NORTH);
		directionPanel.add(backwardBtn, BorderLayout.CENTER);
		directionPanel.add(leftBtn, BorderLayout.WEST);
		directionPanel.add(rightBtn, BorderLayout.EAST);
		directionPanel.add(operationPanel, BorderLayout.SOUTH);

		keyboardPanel.setPreferredSize(new Dimension(800, 100));
		keyboardPanel.setLayout(new BorderLayout());
		keyboardPanel.add(directionPanel, BorderLayout.CENTER);

		pack();
	}

	public void addActionListener() {
		for (JButton button : directionButtons) {
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					controller.setPressed(button.getText());
				}
			});
		}

		accelerateBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		breakBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		stopBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void updateSpeed(Float currentSpeed) {
		gauge.setValue(Math.abs(currentSpeed));
	}

}