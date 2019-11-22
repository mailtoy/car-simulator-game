package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.lwjgl.input.Keyboard;

import eu.hansolo.steelseries.gauges.Radial;

@SuppressWarnings("serial")
public class ControllerHandler implements KeyListener, ActionListener {
	private Controller controller;

	private static float RUN_SPEED = 20;
	private static final float RUN_BREAK = -5;
	private static final float RUN_ACC = 5;
	private static final float TURN_SPEED = 30;
	private static boolean running = false;
	private Thread moveThread;

	private static JFrame f = new JFrame();
	private JTextArea text = new JTextArea();

	String[] cars = { "Car1", "Car2", "Car3", "Car4" };
	String[] maps = { "map1", "map2", };

	JComboBox<String> carList = new JComboBox<>(cars);
	JComboBox<String> mapList = new JComboBox<>(maps);

	String arrow[] = { "^", "v", "<", ">" };
	String option[] = { "accelerate", "break", "stop" };

	JButton forward, backward, turnLeft, turnRight, accelerate, breakcar, stop;
	JButton nameBtn[] = { forward, backward, turnLeft, turnRight, accelerate, breakcar, stop };

	public Radial gauge;

	// default color of the button to be repainted when key released
	Color cc = new JButton().getBackground();

	public ControllerHandler(Controller controller) {
		this.controller = controller;

		setFrame();
	}

	private void initWidgets() {
		// set the text area on top
		text.setPreferredSize(new Dimension(400, 50));
		JLabel info = new JLabel("<html>Note</html>");
		// set the bold font for info
		info.setFont(new Font("Verdana", Font.BOLD, 14));

		/** combobox **/
		// create labels
		JLabel selectCar = new JLabel("select your city: ");
		selectCar.setForeground(Color.red);
		JLabel selectMap = new JLabel("select your map: ");
		selectMap.setForeground(Color.blue);

		box(cars, carList);
		box(maps, mapList);

		/** end combobox **/

		/* set the layout and place compomnet in place and pack it */
		f.setLayout(new BorderLayout());
		/* Various panel for the layout */
		JPanel jpNorth = new JPanel();
		JPanel jpCenter = new JPanel();
		JPanel jpKeyboard = new JPanel();
		JPanel jpGauge = new JPanel();
		JPanel jpArrow = new JPanel();

		JPanel buttonsPanel = new JPanel();
		gauge = new Radial();

		f.add(jpNorth, BorderLayout.NORTH);
		f.add(jpGauge, BorderLayout.WEST);
		// f.add(jpCenter, BorderLayout.CENTER);
		f.add(jpKeyboard, BorderLayout.EAST);

		jpNorth.setLayout(new FlowLayout());
		jpNorth.add(selectCar);
		jpNorth.add(carList);
		jpNorth.add(selectMap);
		jpNorth.add(mapList);

		// jpCenter.setLayout(new BorderLayout());
		// jpCenter.add(text, BorderLayout.CENTER);

		jpKeyboard.setPreferredSize(new Dimension(800, 100));
		jpKeyboard.setLayout(new BorderLayout());
		jpKeyboard.add(jpArrow, BorderLayout.CENTER);

		jpGauge.setLayout(new FlowLayout());
		jpGauge.add(gauge);
		jpGauge.add(buttonsPanel);

		f.pack();

		/* add button to the keyboard */
		forward = new JButton(arrow[0]);
		backward = new JButton(arrow[1]);
		turnLeft = new JButton(arrow[2]);
		turnRight = new JButton(arrow[3]);

		accelerate = new JButton(option[0]);
		breakcar = new JButton(option[1]);
		stop = new JButton(option[2]);
		jpNorth.add(stop);
		jpNorth.add(breakcar);
		jpNorth.add(accelerate);
		//
		// jpCenter.setLayout(new BorderLayout());
		// jpCenter.add(stop, BorderLayout.CENTER);

		jpArrow.add(forward, BorderLayout.NORTH);
		jpArrow.add(backward, BorderLayout.SOUTH);
		jpArrow.add(turnLeft, BorderLayout.WEST);
		jpArrow.add(turnRight, BorderLayout.EAST);

		/** gauge **/
		gauge.setTitle("Gauge");
		gauge.setUnitString("Some units");
		gauge.setPreferredSize(new Dimension(200, 200));

		JLabel valueLabel = new JLabel("Value:");

		final JTextField valueField = new JTextField(7);
		valueField.setText("30");
		JButton button = new JButton("Set");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					double value = Double.valueOf(valueField.getText());
					gauge.setValueAnimated(value);
				} catch (NumberFormatException ex) {
					System.err.println("invalid input");
				}
			}
		});

		buttonsPanel.add(valueLabel);
		buttonsPanel.add(valueField);
		buttonsPanel.add(button);
		/** end gauge **/

		checkButtonPress();
		f.pack();
	}

	private void setFrame() {
		f.setSize(new Dimension(1300, 250));
		f.setLocation(10, 550);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		initWidgets();
		f.setVisible(true);
	}

	public void checkButtonPress() {

		forward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.setText("^");
				try {
					gauge.setValueAnimated(RUN_SPEED);
					running = true;
					moveThread = new Thread(new Runnable() {
						@Override
						public void run() {
							while (running) {
								try {
									forward.setEnabled(false);
									stop.setEnabled(true);
									// client.sendKeyInput(Keyboard.KEY_UP,
									// RUN_SPEED);
									Thread.sleep((long) 25);
								} catch (Exception e) {
									e.printStackTrace();
								}
								// if (Thread.interrupted()) {
								// return;
								// }
							}
						}
					});
					moveThread.start();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});

		turnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// client.sendKeyInput(Keyboard.KEY_LEFT, TURN_SPEED);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		backward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// client.sendKeyInput(Keyboard.KEY_DOWN, -RUN_SPEED);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		turnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// client.sendKeyInput(Keyboard.KEY_RIGHT, -TURN_SPEED);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					forward.setEnabled(true);
					stop.setEnabled(false);
					running = false;
					// moveThread.interrupted();
					// client.sendKeyInput(Keyboard.KEY_0, 0);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		breakcar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					RUN_SPEED = RUN_SPEED + RUN_BREAK;
					// client.sendKeyInput(Keyboard.KEY_DOWN, RUN_SPEED);
					gauge.setValueAnimated(RUN_SPEED);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});

		accelerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					RUN_SPEED = RUN_SPEED + RUN_ACC;
					// client.sendKeyInput(Keyboard.KEY_DOWN, RUN_SPEED);
					gauge.setValueAnimated(RUN_SPEED);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
	}

	public void box(String name[], JComboBox<String> nameList) {
		// Create the combo box, select item at index 0.
		nameList = new JComboBox(name);
		nameList.setSelectedIndex(0);
		nameList.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox) e.getSource();
		String name = (String) cb.getSelectedItem();
		text.setText(name);

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
