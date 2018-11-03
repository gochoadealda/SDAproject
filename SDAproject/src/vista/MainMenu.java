package vista;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.JDesktopPane;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JTable;
import java.awt.Component;
import javax.swing.JList;
import javax.swing.AbstractListModel;

public class MainMenu extends JFrame implements ActionListener {
	private Dimension dim;
	private JTextField tfIp;
	private JTextField tfPuerto;
	private JTextField tfID;
	private JTable tableTrackers, tableSwarms;
	private DefaultTableModel modelTrackers, modelSwarms;
	private JTable tablePeers;

	public MainMenu() {
		super(); // usamos el contructor de la clase padre JFrame
		configurarVentana(); // configuramos la ventana
		inicializarComponentes(); // inicializamos los atributos o componentes
	}

	private void configurarVentana() {
		dim = super.getToolkit().getScreenSize();
		super.setSize(dim);
		this.setTitle("Main Menu");
		this.setExtendedState(MAXIMIZED_BOTH);
		getContentPane().setLayout(null);
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void inicializarComponentes() {

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 1917, 1002);
		getContentPane().add(tabbedPane);

		// MENU DE CONFIGURACION (TAB1)
		JPanel panel1 = new JPanel();
		tabbedPane.addTab("Configuration menu", null, panel1, null);
		panel1.setLayout(null);

		JLabel label = new JLabel("IP");
		label.setBounds(12, 39, 86, 37);
		label.setFont(new Font("Consolas", Font.PLAIN, 33));
		panel1.add(label);

		tfIp = new JTextField();
		tfIp.setBounds(142, 42, 177, 37);
		tfIp.setColumns(10);
		panel1.add(tfIp);

		JLabel lblPuerto = new JLabel("Port");
		lblPuerto.setFont(new Font("Consolas", Font.PLAIN, 33));
		lblPuerto.setBounds(12, 94, 118, 37);
		panel1.add(lblPuerto);

		JLabel lblId = new JLabel("ID");
		lblId.setFont(new Font("Consolas", Font.PLAIN, 33));
		lblId.setBounds(12, 144, 86, 37);
		panel1.add(lblId);

		tfPuerto = new JTextField();
		tfPuerto.setColumns(10);
		tfPuerto.setBounds(142, 97, 177, 37);
		panel1.add(tfPuerto);

		tfID = new JTextField();
		tfID.setColumns(10);
		tfID.setBounds(143, 147, 177, 37);
		panel1.add(tfID);

		JButton btnIniciar = new JButton("Start/Stop");
		btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Comprobación de si la IP introducida es válida mediante una expresión regular");
				if (!validate(tfIp.getText())) {
					JOptionPane.showMessageDialog(panel1, "Invalid IP", "Error", JOptionPane.ERROR_MESSAGE);
					tfIp.setText("");
				}
			}
		});
		btnIniciar.setFont(new Font("Consolas", Font.PLAIN, 33));
		btnIniciar.setBounds(33, 238, 286, 54);
		panel1.add(btnIniciar);

		// GESTOR DE TRACKERS (TAB2)
		JPanel panel2 = new JPanel();
		tabbedPane.addTab("Tracker gestor", null, panel2, null);
		panel2.setLayout(null);

		// headers for the table
		String[] columnsTabla1 = new String[] { "Id", "Master or Slave", "Last Keepalive", "Active" };

		// actual data for the table in a 2d array
		Object[][] dataTabla1 = new Object[][] { { 1, "Master", "1s", true }, { 2, "Slave", "1s", true },
				{ 3, "Slave", "1s", true }, { 4, "Slave", "4s", false }, };

		final Class[] columnClassTabla1 = new Class[] { Integer.class, String.class, String.class, Boolean.class };
		modelTrackers = new DefaultTableModel(dataTabla1, columnsTabla1) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnClassTabla1[columnIndex];
			}
		};

		tableTrackers = new JTable(modelTrackers);
		JScrollPane scrollPaneTabla1 = new JScrollPane(tableTrackers);
		scrollPaneTabla1.setBounds(32, 28, 452, 431);
		panel2.add(scrollPaneTabla1);
		// panel2.add(table);

		JPanel panel3 = new JPanel();
		panel3.setForeground(Color.BLACK);
		tabbedPane.addTab("Gestor de Peers", null, panel3, null);
		panel3.setLayout(null);

		JLabel lblSwarmsActivos = new JLabel("Active Swarms");
		lblSwarmsActivos.setFont(new Font("Consolas", Font.PLAIN, 33));
		lblSwarmsActivos.setBounds(62, 50, 266, 37);
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
		swarmList.setBounds(62, 116, 254, 387);
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
		scrollPaneTabla2.setBounds(595, 148, 726, 43);
		panel3.add(scrollPaneTabla2);
		
		JScrollPane scrollPaneTabla3 = new JScrollPane();
		scrollPaneTabla3.setBounds(595, 309, 726, 347);
		panel3.add(scrollPaneTabla3);
		
		tablePeers = new JTable();
		scrollPaneTabla3.setViewportView(tablePeers);
		tablePeers.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
			},
			new String[] {
				"ID", "ip", "Port", "Bytes pending", "Downloaded Bytes"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, Integer.class, Double.class, Double.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		
		JLabel lblPeersInfo = new JLabel("Peers Info");
		lblPeersInfo.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblPeersInfo.setBounds(595, 250, 121, 48);
		panel3.add(lblPeersInfo);
		
		JLabel lblSwarmInfo = new JLabel("Swarm Info");
		lblSwarmInfo.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lblSwarmInfo.setBounds(595, 100, 135, 37);
		panel3.add(lblSwarmInfo);
		tablePeers.getColumnModel().getColumn(3).setPreferredWidth(97);
		tablePeers.getColumnModel().getColumn(4).setPreferredWidth(105);

	}

	public static boolean validate(final String ip) { // Método para verificar que la IP introducida cumple los
														// requisitos de cualquier IPV4
		String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

		return ip.matches(PATTERN);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// JOptionPane.showMessageDialog(this, "Hola " + nombre + "."); // mostramos un
		// mensaje (frame, mensaje)
	}
}
