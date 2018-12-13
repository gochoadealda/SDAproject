package vista;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import controller.PeerController;
import modelo.Peer;


import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.AbstractListModel;

@SuppressWarnings("serial")
public class PeerGestor extends JFrame implements ActionListener {
	private Dimension dim;
	private JTable tableSwarms;
	@SuppressWarnings("unused")
	private DefaultTableModel modelSwarms;
	private JTable tablePeers;
	private PeerController peerController;
	private boolean isTheMaster;
	private Object[][] dataTabla1;
	@SuppressWarnings("rawtypes")
	private Class[] columnClassTabla1;
	private String[] columnsTabla1;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	
	public PeerGestor(Peer peer) {
		super(); // usamos el contructor de la clase padre JFrame

		this.peerController = new PeerController(peer);
		configurarVentana(); // configuramos la ventana
		inicializarComponentes(); // inicializamos los atributos o componentes

	}

	private void configurarVentana() {
		dim = super.getToolkit().getScreenSize();
		super.setSize(dim);
		this.setExtendedState(MAXIMIZED_BOTH);
		getContentPane().setLayout(null);
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@SuppressWarnings({ "unchecked", "rawtypes"})
	private void inicializarComponentes() {

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 1917, 1002);
		getContentPane().add(tabbedPane);

		// headers for the table
		columnsTabla1 = new String[] { "Id","Time" };

		
		//		dataTabla1 = new Object[][] { { myTracker.getID(),isMaster , "1s", isActive }, { 2, "Slave", "1s", true },
		//			{ 3, "Slave", "1s", true }, { 4, "Slave", "4s", false }, };
		//
		columnClassTabla1 = new Class[] { Integer.class , String.class};
		//			modelTrackers = new DefaultTableModel(dataTabla1, columnsTabla1) {
		//				@Override
		//				public boolean isCellEditable(int row, int column) {
		//					return false;
		//				}
		//
		//				@Override
		//				public Class<?> getColumnClass(int columnIndex) {
		//					return columnClassTabla1[columnIndex];
		//				}
		//			};
		//
		//			tableTrackers = new JTable(modelTrackers);
		//			JScrollPane scrollPaneTabla1 = new JScrollPane(tableTrackers);
		//			scrollPaneTabla1.setBounds(32, 28, 452, 431);
		//			panel2.add(scrollPaneTabla1);
		// panel2.add(table);
		
		

		JPanel panel3 = new JPanel();
		panel3.setForeground(Color.BLACK);
		tabbedPane.addTab("Peers gestor", null, panel3, null);
		panel3.setLayout(null);
		
		
		
	      

		
		
		FileDialog dialog = new FileDialog((Frame) getContentPane(), "Select File to Open");
		Button showFileDialogButton = new Button("Open File");
	      showFileDialogButton.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	 dialog.setVisible(true);
	         }
	      });
	    dialog.setMode(FileDialog.LOAD);
	    dialog.setVisible(true);
	    String file = dialog.getFile();
	    showFileDialogButton.setBounds(51, 201, 307, 48);
	    panel3.add(showFileDialogButton);
	    
		 JFileChooser chooser = new JFileChooser();
	        FileNameExtensionFilter filter = new FileNameExtensionFilter(
	                "JPG & GIF Images", "jpg", "gif");
	        chooser.setFileFilter(filter);
	        int returnVal = chooser.showOpenDialog(null);
	        if(returnVal == JFileChooser.APPROVE_OPTION) {
	            System.out.println("You chose to open this file: " +
	                    chooser.getSelectedFile().getName());
	        }
	    //chooser.setBounds(70, 390, 266, 37);
	    panel3.add(chooser);

		JLabel lblSwarmsActivos = new JLabel("Active Swarms");
		lblSwarmsActivos.setFont(new Font("Consolas", Font.PLAIN, 33));
		lblSwarmsActivos.setBounds(62, 345, 266, 37);
		panel3.add(lblSwarmsActivos);

		// headers for the table
		String[] columnsTabla2 = new String[] { "Id", "Content name", "Size", "Number of seeders",
		"Leechers" };

		// actual data for the table in a 2d array
		Object[][] dataTabla2 = new Object[][] { { 1, "aaaaa", 1231, 4, 8 }, { 2, "bbbbb", 1233, 5, 9 },
			{ 3, "ccccc", 4244, 6, 10 }, { 4, "ddddd", 5654, 7, 11 }, };

			final Class[] columnClassTabla2 = new Class[] { Integer.class, String.class, Integer.class, Integer.class,
					Integer.class };
			modelSwarms = new DefaultTableModel(dataTabla2, columnsTabla2) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}

				@Override
				public Class<?> getColumnClass(int columnIndex) {
					return columnClassTabla2[columnIndex];
				}
			};

			
			JList swarmList = new JList();
			swarmList.setFont(new Font("Tahoma", Font.PLAIN, 20));
			swarmList.setModel(new AbstractListModel() {
				String[] values = new String[] {"Swarm1", "Swarm2", "Swarm3", "Swarm4"};
				public int getSize() {
					return values.length;
				}
				public Object getElementAt(int index) {
					return values[index];
				}
			});
			swarmList.setBounds(62, 414, 296, 487);
			panel3.add(swarmList);

			tableSwarms = new JTable(new DefaultTableModel(
					new Object[][] {
						{new Integer(1), "aaaaa", new Integer(1231), new Integer(4), new Integer(8)},
					},
					new String[] {
							"Id", "Content name", "Size", "Seeders", "Leechers"
					}
					));
			JScrollPane scrollPaneTabla2 = new JScrollPane(tableSwarms);
			scrollPaneTabla2.setBounds(595, 97, 1249, 197);
			panel3.add(scrollPaneTabla2);

			JScrollPane scrollPaneTabla3 = new JScrollPane();
			scrollPaneTabla3.setBounds(595, 414, 1249, 421);
			panel3.add(scrollPaneTabla3);

			tablePeers = new JTable();
			scrollPaneTabla3.setViewportView(tablePeers);
			tablePeers.setModel(new DefaultTableModel(
					new Object[][] {
						{null, null, null, null, null, null},
						{null, null, null, null, null, null},
						{null, null, null, null, null, null},
					},
					new String[] {
							"ID", "ip", "Port", "Bytes pending", "Downloaded Bytes", "Uploaded Bytes"
					}
					) {
				Class[] columnTypes = new Class[] {
						Integer.class, String.class, Integer.class, Double.class, Double.class, Double.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
			});

			JLabel lblPeersInfo = new JLabel("Peers Info");
			lblPeersInfo.setFont(new Font("Tahoma", Font.PLAIN, 25));
			lblPeersInfo.setBounds(595, 337, 121, 48);
			panel3.add(lblPeersInfo);

			JLabel lblSwarmInfo = new JLabel("Swarm Info");
			lblSwarmInfo.setFont(new Font("Tahoma", Font.PLAIN, 25));
			lblSwarmInfo.setBounds(595, 35, 135, 37);
			panel3.add(lblSwarmInfo);
			
			JLabel label = new JLabel("IP");
			label.setFont(new Font("Consolas", Font.PLAIN, 33));
			label.setBounds(51, 37, 86, 37);
			panel3.add(label);
			
			textField = new JTextField();
			textField.setText((String) null);
			textField.setColumns(10);
			textField.setBounds(181, 40, 177, 37);
			panel3.add(textField);
			
			JLabel label_1 = new JLabel("Port");
			label_1.setFont(new Font("Consolas", Font.PLAIN, 33));
			label_1.setBounds(51, 92, 118, 37);
			panel3.add(label_1);
			
			textField_1 = new JTextField();
			textField_1.setText((String) null);
			textField_1.setColumns(10);
			textField_1.setBounds(181, 95, 177, 37);
			panel3.add(textField_1);
			
			JLabel label_2 = new JLabel("ID");
			label_2.setFont(new Font("Consolas", Font.PLAIN, 33));
			label_2.setBounds(51, 142, 86, 37);
			panel3.add(label_2);
			
			textField_2 = new JTextField();
			textField_2.setText((String) null);
			textField_2.setColumns(10);
			textField_2.setBounds(182, 145, 177, 37);
			panel3.add(textField_2);
			
			JButton button = new JButton("Stop");
			button.setFont(new Font("Consolas", Font.PLAIN, 33));
			button.setBounds(51, 262, 307, 54);
			panel3.add(button);
			tablePeers.getColumnModel().getColumn(3).setPreferredWidth(97);
			tablePeers.getColumnModel().getColumn(4).setPreferredWidth(105);

	}

	public static boolean validateIP(final String ip) { // M�todo para verificar que la IP introducida cumple los
		// requisitos de cualquier IPV4
		String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

		return ip.matches(PATTERN);
	}

	public static boolean validatePort(final String port) { // M�todo para verificar que el port introducida cumple los
		// requisitos de cualquier puerto (valor entre 0 y 65535)
		boolean validation=false;
		int number= Integer.parseInt(port);
		if(number<65536&&number>=0) {
			validation=true;
		}
		return validation;
	}

	public static boolean validateID(final String id) { // M�todo para verificar que la Id introducida cumple los
		// requisitos (que sea igual o mayor que 0)
		boolean validation=false;
		int number= Integer.parseInt(id);
		if(number>=-1) {
			validation=true;
		}
		return validation;
	}

	public Peer getMyPeer() {
		return peerController.getModel();
	}

	public boolean isTheMaster() {
		return isTheMaster;
	}

	public Object[][] getDataTabla1() {
		return dataTabla1;
	}

	public void setDataTabla1(Object[][] dataTabla1) {
		this.dataTabla1 = dataTabla1;
	}

	@SuppressWarnings("rawtypes")
	public Class[] getColumnClassTabla1() {
		return columnClassTabla1;
	}

	@SuppressWarnings("rawtypes")
	public void setColumnClassTabla1(Class[] columnClassTabla1) {
		this.columnClassTabla1 = columnClassTabla1;
	}

	
	public String[] getColumnsTabla1() {
		return columnsTabla1;
	}

	public void setColumnsTabla1(String[] columnsTabla1) {
		this.columnsTabla1 = columnsTabla1;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

	}
	
}