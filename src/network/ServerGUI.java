package network;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class ServerGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private Server server;
	private JTextArea responsesArea;
	private JList<String> clientsList;
	private String[] mapNames = new String[] { "map1", "map2" };
	private JComboBox<String> mapList;
	private JLabel clientsLabel;
	private DefaultListModel<String> clientModel;

	public ServerGUI(Server server) {
		this.server = server;

		setFrame();
	}

	private void setFrame() {
		setTitle("Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initWidgets();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initWidgets() {
		setLayout(new BorderLayout());

		mapList = new JComboBox<String>(mapNames);
		JLabel mapSelectLabel = new JLabel("Select a Map: ");
		JPanel mapSelectPanel = new JPanel(new FlowLayout());
		mapSelectPanel.add(mapSelectLabel);
		mapSelectPanel.add(mapList);

		responsesArea = new JTextArea(25, 25);
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
		clientsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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
				DisconnectPacket disconnectPacket = new DisconnectPacket(clientsList.getSelectedValue());
				disconnectPacket.writeData(server);

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
		clientsPanel.setPreferredSize(new Dimension(250, 150));
		clientsPanel.add(clientsLabel);
		clientsPanel.add(clientsScroller);
		clientsPanel.add(removeClientBtn);

		add(mapSelectPanel, BorderLayout.NORTH);
		add(responsePanel, BorderLayout.WEST);
		add(clientsPanel, BorderLayout.EAST);

		pack();
	}

	public void appendResponse(String response) {
		responsesArea.append(response + "\n");
		responsesArea.setCaretPosition(responsesArea.getDocument().getLength());
	}

	public void addClient(String client) {
		clientModel.addElement(client);
		clientsLabel.setText("Clients in Server: " + clientModel.getSize());
	}

	public void removeClient(String client) {
		clientModel.removeElement(client);
		clientsLabel.setText("Clients in Server: " + clientModel.getSize());
	}

	public void setMapEnabled(boolean enable) {
		mapList.setEnabled(enable);
	}

	public String getSelectedMap() {
		return mapList.getSelectedItem().toString();
	}
}
