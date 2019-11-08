package engineTester;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.plaf.basic.BasicArrowButton;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.awt.Font;
import java.awt.event.KeyListener;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javax.swing.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import connection.ClientType;

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
	String[] maps = { "blueMap", "reaMap", "roadMap"};
	
	JComboBox<String> carList = new JComboBox<>(cars);
	JComboBox<String> mapList = new JComboBox<>(maps);
	
	String arrow[] = { "^", "v", "<", ">"};
	String option[] = { "accelerate", "break", "stop" };

	JButton forward, backward, turnLeft, turnRight, accelerate, breakcar, stop;

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
	public void keyReleased(KeyEvent evt) {

	}

	public Controller() {
		super();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.getContentPane().setPreferredSize(new Dimension(600, 100));
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
		text.setPreferredSize(new Dimension(600, 50));
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
		JPanel jpNote = new JPanel();
		JPanel jpArrow = new JPanel();

		f.add(jpNorth, BorderLayout.NORTH);
		f.add(jpNote);
		f.add(jpCenter, BorderLayout.CENTER);
		f.add(jpKeyboard, BorderLayout.SOUTH);
		
		jpNorth.setLayout(new FlowLayout());
		jpNorth.add(selectCar);
		jpNorth.add(carList);
		jpNorth.add(selectMap);
		jpNorth.add(mapList);

		jpCenter.setLayout(new BorderLayout());
		jpCenter.add(text, BorderLayout.CENTER);

		jpKeyboard.setLayout(new BorderLayout());
		jpKeyboard.add(jpArrow, BorderLayout.EAST);
		
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

		checkButtonPress();

	} // end of initWidgets

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
		String petName = (String) cb.getSelectedItem();
		text.setText(petName);
	}

}