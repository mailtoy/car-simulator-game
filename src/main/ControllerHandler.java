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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import eu.hansolo.steelseries.gauges.Radial;

public class ControllerHandler extends JFrame {
	private static final long serialVersionUID = 1L;
	private Controller controller;

//	private static float RUN_SPEED = 20;
//	private static final float RUN_BREAK = -5;
//	private static final float RUN_ACC = 5;
//	private static final float TURN_SPEED = 30;
//	private static boolean running = false;
//	private Thread moveThread;

	private JButton forwardBtn, backwardBtn, leftBtn, rightBtn, accelerateBtn, breakBtn, stopBtn;
	private ArrayList<JButton> directionButtons;

	// default color of the button to be repainted when key released
	private Color cc = new JButton().getBackground();

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

		String[] maps = { "map1", "map2" };
		JComboBox<String> mapList = new JComboBox<String>(maps);
		mapList.setSelectedIndex(0);
//		mapList.addActionListener(this); // fix this later

		Radial gauge = new Radial();
		gauge.setTitle("Controller");
		gauge.setUnitString("Km / Hr");
		gauge.setPreferredSize(new Dimension(200, 200));
//		gauge.setValueAnimated(value);

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
		JPanel mapPanel = new JPanel();
		JPanel gaugePanel = new JPanel();
		JPanel keyboardPanel = new JPanel();
		JPanel directionPanel = new JPanel();
		JPanel operationPanel = new JPanel();

		add(mapPanel, BorderLayout.NORTH);
		add(gaugePanel, BorderLayout.WEST);
		add(keyboardPanel, BorderLayout.EAST);

		mapPanel.setLayout(new FlowLayout());
		mapPanel.add(selectMap);
		mapPanel.add(mapList);

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

//	@Override
//	public void actionPerformed(ActionEvent e) {
//		String action = e.getActionCommand();
//		if (action.equals("^")) {
//			try {
////				gauge.setValueAnimated(RUN_SPEED);
//				running = true;
//				moveThread = new Thread(new Runnable() {
//					@Override
//					public void run() {
//						while (running) {
//							try {
////								forward.setEnabled(false);
////								stop.setEnabled(true);
//								// client.sendKeyInput(Keyboard.KEY_UP, RUN_SPEED);
//								Thread.sleep((long) 25);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				});
//				moveThread.start();
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//		} else if (action.equals("v")) {
//			// client.sendKeyInput(Keyboard.KEY_DOWN, -RUN_SPEED);
//		} else if (action.equals("<")) {
//			// client.sendKeyInput(Keyboard.KEY_LEFT, TURN_SPEED);
//		} else if (action.equals(">")) {
//			// client.sendKeyInput(Keyboard.KEY_RIGHT, -TURN_SPEED);
//		} else if (action.equals("accelerate")) {
//			RUN_SPEED = RUN_SPEED + RUN_ACC;
//			// client.sendKeyInput(Keyboard.KEY_DOWN, RUN_SPEED);
////			gauge.setValueAnimated(RUN_SPEED);
//		} else if (action.equals("break")) {
//			RUN_SPEED = RUN_SPEED + RUN_BREAK;
//			// client.sendKeyInput(Keyboard.KEY_DOWN, RUN_SPEED);
////			gauge.setValueAnimated(RUN_SPEED);
//		} else if (action.equals("stop")) {
//			try {
////				forward.setEnabled(true);
////				stop.setEnabled(false);
//				running = false;
//				// client.sendKeyInput(Keyboard.KEY_0, 0);
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//		}
//	}
}
