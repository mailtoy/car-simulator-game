package network;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import network.packet.DisconnectPacket;

/**
 * Create an user interface of the server and handle behaviors of the server
 * acts.
 * 
 * @author Issaree Srisomboon
 *
 */
public class ServerGUI extends JFrame implements WindowListener {
	private static final long serialVersionUID = 1L;
	private Server server;
	private JTextArea responsesArea;
	private JList<String> clientsList;
	private String[] mapNames = new String[] { "map1", "map2" };
	private JComboBox<String> mapList;
	private JLabel clientsLabel;
	private DefaultListModel<String> clientModel;

	/**
	 * Constructor of the server.
	 * 
	 * @param server
	 */
	public ServerGUI(Server server) {
		this.server = server;

		setFrame();
	}

	/**
	 * Set up main setting of the server.
	 */
	private void setFrame() {
		setTitle("Server");
		setPreferredSize(new Dimension(980, 620));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(this);

		initWidgets();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Initialize all components.
	 */
	private void initWidgets() {
		setLayout(new BorderLayout());

		mapList = new JComboBox<String>(mapNames);
		JLabel mapSelectLabel = new JLabel("Select a Map: ");
		JPanel mapSelectPanel = new JPanel();
		mapSelectPanel.add(mapSelectLabel);
		mapSelectPanel.add(mapList);
		JLabel serverIPLabel = new JLabel("Server is running on " + server.getLocalIPAddr());
		JPanel infromPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 15));
		infromPanel.add(serverIPLabel);
		infromPanel.add(mapSelectPanel);

		responsesArea = new JTextArea(30, 35);
		responsesArea.setEditable(false);
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
		clientsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		clientsList.setVisibleRowCount(-1);
		JScrollPane clientsScroller = new JScrollPane(clientsList);
		clientsLabel = new JLabel("Clients in Server: " + clientModel.getSize());
		clientsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton removeClientBtn = new JButton("Remove");
		removeClientBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		removeClientBtn.setEnabled(false);

		removeClientBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				forceDisconnect(clientsList.getSelectedValue());

				int index = clientsList.getSelectedIndex();
				clientModel.remove(index);
				clientsLabel.setText("Clients in Server: " + clientModel.getSize());

				if (clientModel.getSize() == 0) {
					removeClientBtn.setEnabled(false);
				} else {
					if (index == clientModel.getSize()) {
						index--;
					}
					clientsList.setSelectedIndex(index);
					clientsList.ensureIndexIsVisible(index);
				}
			}
		});

		clientsList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					if (clientsList.getSelectedIndex() == -1) {
						removeClientBtn.setEnabled(false);
					} else {
						removeClientBtn.setEnabled(true);
					}
				}
			}
		});

		JPanel clientsPanel = new JPanel();
		clientsPanel.setLayout(new BoxLayout(clientsPanel, BoxLayout.Y_AXIS));
		clientsPanel.setPreferredSize(new Dimension(300, 300));
		clientsPanel.add(clientsLabel);
		clientsPanel.add(clientsScroller);
		clientsPanel.add(removeClientBtn);

		JPanel shownPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 5));
		shownPanel.add(responsePanel);
		shownPanel.add(clientsPanel);

		add(infromPanel, BorderLayout.NORTH);
		add(shownPanel, BorderLayout.CENTER);

		pack();
	}

	/**
	 * Append events text occurred in the server on response TextArea.
	 * 
	 * @param response Text received from server.
	 */
	public void appendResponse(String response) {
		responsesArea.append(response + "\n");
		responsesArea.setCaretPosition(responsesArea.getDocument().getLength());
	}

	/**
	 * Add a latest connected client into the list. If the client's type is a
	 * controller, then show the color of the car in the list as well.
	 * 
	 * @param client A type of new client
	 */
	public void addClient(String client) {
		if (server.isController(client)) {
			String[] splitClient = client.split(":");
			String color = splitClient[1].substring(0, splitClient[1].indexOf("Color"));
			String capColor = Character.toUpperCase(color.charAt(0)) + color.substring(1);
			clientModel.addElement(splitClient[0] + " (" + capColor + " Car)");
		} else {
			clientModel.addElement(client);
		}
		clientsLabel.setText("Clients in Server: " + clientModel.getSize());
	}

	/**
	 * Remove the client from the client list.
	 * 
	 * @param client A type of client that is needed to be removed from the list.
	 */
	public void removeClient(String client) {
		for (int i = 0; i < clientModel.getSize(); i++) {
			if (clientModel.get(i).contains(client)) {
				clientModel.remove(i);
			}
		}
		clientsLabel.setText("Clients in Server: " + clientModel.getSize());
	}

	/**
	 * Force the client to disconnect from the server.
	 * 
	 * @param client A type of client that is needed to be disconnected.
	 */
	private void forceDisconnect(String client) {
		DisconnectPacket disconnectPacket = null;
		if (server.isController(client)) {
			String type = client.substring(0, client.indexOf(" "));
			disconnectPacket = new DisconnectPacket(type);
		} else {
			disconnectPacket = new DisconnectPacket(client);
		}
		disconnectPacket.writeData(server);
	}

	/**
	 * Set enable of map.
	 * 
	 * @param enable boolean true or false.
	 */
	public void setMapEnabled(boolean enable) {
		mapList.setEnabled(enable);
	}

	/**
	 * Get a selected map from JComboBox maps list.
	 * 
	 * @return A name of selected map
	 */
	public String getSelectedMap() {
		return mapList.getSelectedItem().toString();
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Handler when closing the serverGUI window.
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		for (int i = 0; i < clientModel.getSize(); i++) {
			forceDisconnect(clientModel.get(0));
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}
}
