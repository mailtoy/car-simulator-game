package network;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ServerGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private Server server;
	private JTextArea responsesArea;
	private JList<String> clientsList;
	private String[] mapNames = new String[] { "map1", "map2" };
	private JComboBox<String> mapList;
	private DefaultListModel<String> clientModel;

	public ServerGUI(Server server) {
		this.server = server;

		setFrame();
	}

	private void setFrame() {
		setTitle("Server");
		setSize(new Dimension(750, 500));
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

		responsesArea = new JTextArea();
		responsesArea.setEditable(false);
		responsesArea.setPreferredSize(new Dimension(500, 250));
		JScrollPane responsesScroller = new JScrollPane(responsesArea);
		JLabel responsesLabel = new JLabel("Status");
		responsesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel responsePanel = new JPanel();
		responsePanel.setLayout(new BoxLayout(responsePanel, BoxLayout.Y_AXIS));
		responsePanel.add(responsesLabel);
		responsePanel.add(responsesScroller);

		clientModel = new DefaultListModel<String>();
		clientsList = new JList<String>(clientModel);
		clientsList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		clientsList.setVisibleRowCount(-1);
		clientsList.setPreferredSize(new Dimension(150, 250));
		JScrollPane clientsScroller = new JScrollPane(clientsList);
		JLabel clientsLabel = new JLabel("Clients in Server");
		clientsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel clientsPanel = new JPanel();
		clientsPanel.setLayout(new BoxLayout(clientsPanel, BoxLayout.Y_AXIS));
		clientsPanel.add(clientsLabel);
		clientsPanel.add(clientsScroller);

		add(mapSelectPanel, BorderLayout.NORTH);
		add(responsePanel, BorderLayout.WEST);
		add(clientsPanel, BorderLayout.EAST);

		pack();
	}

	public void appendResponse(String response) {
		responsesArea.append(response + "\n");
	}

	public void addClient(String client) {
		clientModel.addElement(client);
	}
	
	public void removeClient(String client) {
		clientModel.removeElement(client);
	}

	public void setMapEnabled(boolean enable) {
		mapList.setEnabled(enable);
	}

	public String getSelectedMap() {
		return mapList.getSelectedItem().toString();
	}
}
