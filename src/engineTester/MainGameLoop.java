package engineTester;

import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
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

import org.lwjgl.input.Keyboard;

import connection.Client;

public class MainGameLoop extends JFrame implements KeyListener {

	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 160;
	protected Client client;
	JFrame f;
	JPanel p;
	// Individual keyboard rows
	String fourthRow[] = { "^" };
	String fifthRow[] = { "<", "v", ">" };

	// all keys without shift key press
	String noShift = "`1234567890-=qwertyuiop[]\\asdfghjkl;'zxcvbnm,./";
	// special charactors on keyboard that has to be addressed duing keypress
	String specialChars = "~-+[]\\;',.?";

	// Jbuttons corresponting to each individual rows
	JButton first[];

	JButton second[];

	JButton third[];

	JButton fourth[];

	JButton fifth[];

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

	public MainGameLoop() {

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// set non resizable
		this.setResizable(false);
		// super.setSize(500,300);
		// set size of the content pane ie frame
		// show text
		this.getContentPane().setPreferredSize(new Dimension(1250, 200));
		// super.getContentPane().setSize(800,400);
		// set location for the frame
		this.setLocation(10, 550);
		// init and paint frame
		initWidgets();
	}

	private void addSpace(String arr[]) {
		p = new JPanel(new GridLayout(1, arr.length));
		p.add(new JPanel());
		p.add(new JPanel());
		p.add(new JPanel());
		p.add(new JPanel());
		p.add(new JPanel());
		p.add(new JPanel());
		p.add(new JPanel());
		p.add(new JPanel());
	}

	/**
	 * Method to initialize frame component
	 */
	private void initWidgets() {
		// set the text area on top
		JTextArea text = new JTextArea();
		text.setPreferredSize(new Dimension(800, 200));

		JLabel info = new JLabel("<html>Note</html>");
		// set the bold font for info
		info.setFont(new Font("Verdana", Font.BOLD, 14));

		/* set the layout and place compomnet in place and pack it */
		setLayout(new BorderLayout());
		/* Various panel for the layout */
		JPanel jpNorth = new JPanel();
		JPanel jpCenter = new JPanel();
		JPanel jpKeyboard = new JPanel();
		JPanel jpNote = new JPanel();
		add(jpNorth, BorderLayout.NORTH);
		add(jpNote);
		add(jpCenter, BorderLayout.CENTER);
		add(jpKeyboard, BorderLayout.SOUTH);

		jpNorth.setLayout(new BorderLayout());
		jpNorth.add(info, BorderLayout.WEST);
		jpNorth.add(info, BorderLayout.SOUTH);

		jpCenter.setLayout(new BorderLayout());
		jpCenter.add(text, BorderLayout.WEST);
		jpCenter.add(text, BorderLayout.CENTER);

		// add(text,BorderLayout.WEST);
		// add(scrollPane,BorderLayout.CENTER);

		// layout for keyboard
		jpKeyboard.setLayout(new GridLayout(2, 1));
		// pack the components
		pack();

		/* paint fourth keyboard row and add it to the keyboard */
		fourth = new JButton[fourthRow.length];
		// get the panel for the row
		p = new JPanel(new GridLayout(1, fourthRow.length));
		/* put empty panel for layout adjustments */
		for (int i = 0; i < 1; ++i) {
			JPanel spacePanel = new JPanel();
			p.add(spacePanel);
			// add empty panels for layout
			addSpace(fourthRow);
		}
		for (int i = 0; i < fourthRow.length; ++i) {
			fourth[i] = new JButton(fourthRow[i]);
			p.add(fourth[i]);
			if (i == fourthRow.length - 2)
				p.add(new JPanel());

		}
		p.add(new JPanel());
		jpKeyboard.add(p);

		// paint the fifth row
		fifth = new JButton[fifthRow.length];
		// get the panel for the row
		p = new JPanel(new GridLayout(1, fifthRow.length));
		/* put empty panel for layout adjustments */
		for (int i = 0; i < 1; ++i) {
			JPanel spacePanel = new JPanel();
			p.add(spacePanel);
		}
		/* draw the buttons */
		for (int i = 0; i < fifthRow.length; ++i) {
			if (i == 1) // space bar panel
			{
				JButton b = new JButton(fifthRow[i]);
				b.setPreferredSize(new Dimension(400, 10));
				b.setBounds(10, 10, 10, 100);
				fifth[i] = b;

			} else {
				fifth[i] = new JButton(fifthRow[i]);
			}
			if (i == 0) // first black panel
			{

				// add empty panels for layout
				addSpace(fifthRow);
			}

			p.add(fifth[i]);

		}
		jpKeyboard.add(p);
		/* add listeners */
		getContentPane().addKeyListener(this);
		text.addKeyListener(this);
		/* add listeners to all the button */
		for (JButton b : fourth)
			b.addKeyListener(this);

		for (JButton b : fifth)
			b.addKeyListener(this);

		fourth[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.setText("^");
				try {
					client.sendKeyInput(Keyboard.KEY_UP, RUN_SPEED);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		fifth[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.setText("<");
			}
		});
		fifth[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.setText("v");
				try {
					client.sendKeyInput(Keyboard.KEY_DOWN, -RUN_SPEED);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		fifth[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				text.setText(">");
			}
		});
	} // end of initWidgets

	public static void main(String[] args) {

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
