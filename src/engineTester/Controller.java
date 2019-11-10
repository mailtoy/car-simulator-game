package engineTester;

import java.awt.event.KeyEvent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Font;
import java.awt.event.KeyListener;

import javax.swing.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import connection.ClientType;
import eu.hansolo.steelseries.gauges.Radial;

/**
 * Controller controls the movement of the car, then sends it through Server to
 * be able to parallel move in Simulator.
 * 
 * @author Issaree Srisomboon
 *
 */
public class Controller extends ClientType implements KeyListener, ActionListener {

	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 70;

	private static JFrame f = new JFrame();
	private JTextArea text = new JTextArea();
		
	String[] cars = { "Car1", "Car2", "Car3", "Car4"};
	String[] maps = { "map1", "map2",};
	
	JComboBox<String> carList = new JComboBox<>(cars);
	JComboBox<String> mapList = new JComboBox<>(maps);
	
	String arrow[] = { "^", "v", "<", ">"};
	String option[] = { "accelerate", "break", "stop" };

	JButton forward, backward, turnLeft, turnRight, accelerate, breakcar, stop;
	
	public String map = null;
	
	public Radial gauge;

	// default color of the button to be repainted when key released
	Color cc = new JButton().getBackground();

	/*
	 * Invoked when a key has been pressed.
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent evt) {

	}// end of keypressed

	/**
	 * Invoked when a key has been released.
	 */
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_RIGHT){
			
		}
		if(keyCode == KeyEvent.VK_LEFT){
			
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
			gauge.setValueAnimated(0);
		}
		if(keyCode == KeyEvent.VK_DOWN){
			
		}
		if(keyCode == KeyEvent.VK_SPACE){

		}
		if(keyCode == KeyEvent.VK_ESCAPE) {
			
		}

	}

	public Controller() {
		super();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.getContentPane().setPreferredSize(new Dimension(1200, 250));
		f.setLocation(10, 550);
		initWidgets();
		f.setVisible(true);
		run();
	}

	/**
	 * Method to initialize frame component
	 */
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
//		f.add(jpCenter, BorderLayout.CENTER);
		f.add(jpKeyboard, BorderLayout.EAST);
		
		jpNorth.setLayout(new FlowLayout());
		jpNorth.add(selectCar);
		jpNorth.add(carList);
		jpNorth.add(selectMap);
		jpNorth.add(mapList);

//		jpCenter.setLayout(new BorderLayout());
//		jpCenter.add(text, BorderLayout.CENTER);

		jpKeyboard.setPreferredSize(new Dimension(800, 100));
		jpKeyboard.setLayout(new BorderLayout());
		jpKeyboard.add(jpArrow, BorderLayout.CENTER);
		
		jpGauge.setLayout(new FlowLayout());
		jpGauge.add(gauge);
		jpGauge.add(buttonsPanel);
		
		f.pack();

		/*add button to the keyboard */
		forward = new JButton(arrow[0]);
		backward = new JButton(arrow[1]);
		turnLeft = new JButton(arrow[2]);
		turnRight = new JButton(arrow[3]);
		
		accelerate = new JButton(option[0]);
		breakcar = new JButton(option[1]);
		stop = new JButton(option[2]);
		
		jpArrow.add(forward, BorderLayout.NORTH);
		jpArrow.add(backward, BorderLayout.SOUTH);
		jpArrow.add(turnLeft, BorderLayout.WEST);
		jpArrow.add(turnRight, BorderLayout.EAST);
		
		
		/**gauge**/
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
		/**end gauge**/

		checkButtonPress();
	}
	
	public String getMap(){
		if (map == null) {
			map = "1";
			return "map1";
		}
		return (String)mapList.getSelectedItem();
	}
	

	public static void main(String[] args) {
		new Controller();
	}

	public void run() {
		while (!Display.isCloseRequested()) {
			try {
				checkKeyInput();
				render();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		closeRequest();
	}

	public void checkKeyInput() throws Exception {
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			gauge.setValueAnimated(RUN_SPEED);
			client.sendKeyInput(Keyboard.KEY_UP, RUN_SPEED);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			client.sendKeyInput(Keyboard.KEY_DOWN, -RUN_SPEED);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			client.sendKeyInput(Keyboard.KEY_RIGHT, -TURN_SPEED);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			client.sendKeyInput(Keyboard.KEY_LEFT, TURN_SPEED);
		}
	}

	public void checkButtonPress() {
		forward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.setText("^");
				try {
					gauge.setValueAnimated(RUN_SPEED);
					System.out.println(getMap());
					client.sendKeyInput(Keyboard.KEY_UP, RUN_SPEED);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});

		turnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.setText("<");
				try {
					client.sendKeyInput(Keyboard.KEY_LEFT, TURN_SPEED);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		backward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.setText("v");
				try {
					client.sendKeyInput(Keyboard.KEY_DOWN, -RUN_SPEED);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		turnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.setText(">");
				try {
					client.sendKeyInput(Keyboard.KEY_RIGHT, -TURN_SPEED);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	@Override
	public void keyTyped(KeyEvent e) {

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

}