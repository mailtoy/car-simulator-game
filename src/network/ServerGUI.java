package network;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ServerGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private Server server;
	
	private JTextArea responsesArea;
	private String[] mapNames = new String[] { "map1", "map2" };
	private JComboBox<String> mapList;
	

	public ServerGUI(Server server) {
		this.server = server;

		setFrame();
	}

	private void setFrame() {
		setSize(new Dimension(500, 300));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		initWidgets();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initWidgets() {
		setLayout(new BorderLayout());

		mapList = new JComboBox<String>(mapNames);
		JPanel mapSelectPanel = new JPanel(new FlowLayout());
		mapSelectPanel.add(mapList);

		responsesArea = new JTextArea(50, 100);
		responsesArea.setEditable(false);
		
		add(mapSelectPanel, BorderLayout.NORTH);
		add(responsesArea, BorderLayout.CENTER);
		
		mapList.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				
			}
		});

		pack();
	}

	public void appendResponse(String response) {
		responsesArea.append(response + "\n");
	}
}
