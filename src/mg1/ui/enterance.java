package mg1.ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import mg1.com.Observable;
import mg1.com.Observer;
import mg1.com.client;
import mg1.com.observeNewMessage;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class enterance extends JFrame{
	private JTextField txtUserName;
	private JTextField txtIp;
	private JTextField txtPort;
	private JTable table;
	JLabel lblUserName;
	JLabel lblIp;
	JLabel lblPort;
	JButton btnConnect;
	JScrollPane scrollPane;
	private JButton btnUpdate;
	JTextArea txtMessageRead;
	JTextArea txtMessageSend;
	JButton btnSend;
	JButton btnExit;
	Thread listenDataUpdate;
	client clt;
	observeNewMessage onm;
    private Observable observable;

	public enterance() {
		setBounds(100,100,500,450);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		clt = new client();
		//observer
		onm = new observeNewMessage();
		clt.addObserver(onm);
		
		String[] columnNames = {"Id","User","State"};

		lblUserName = new JLabel("User Name");
		lblUserName.setBounds(27, 24, 71, 14);
		getContentPane().add(lblUserName);

		lblIp = new JLabel("IP");
		lblIp.setBounds(27, 50, 46, 14);
		getContentPane().add(lblIp);

		lblPort = new JLabel("Port");
		lblPort.setBounds(27, 75, 46, 14);
		getContentPane().add(lblPort);

		txtUserName = new JTextField();
		txtUserName.setBounds(108, 21, 86, 20);
		getContentPane().add(txtUserName);
		txtUserName.setColumns(10);

		txtIp = new JTextField("192.168.1.36");
		txtIp.setBounds(108, 47, 86, 20);
		getContentPane().add(txtIp);
		txtIp.setColumns(10);

		txtPort = new JTextField("5100");
		txtPort.setBounds(108, 72, 86, 20);
		getContentPane().add(txtPort);
		txtPort.setColumns(10);

		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clt.clientConnect(txtIp.getText(), Integer.parseInt(txtPort.getText()));
				clt.userName = txtUserName.getText();
				clt.sendRegister();
				//gelecek: clt.userId;
				btnUpdate.setEnabled(true);
				btnConnect.setEnabled(false);
				btnExit.setEnabled(true);
			}
		});
		btnConnect.setBounds(27, 109, 89, 23);
		getContentPane().add(btnConnect);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(251, 23, 211, 148);
		getContentPane().add(scrollPane);

		table = new JTable();
		table.setEnabled(true);
		//table.setColumnSelectionAllowed(true);
		//table.setRowSelectionAllowed(true);
		scrollPane.setColumnHeaderView(table);

		btnUpdate = new JButton("Update");
		btnUpdate.setEnabled(false);
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clt.userConnState = true;
				clt.sendUpdate();
				tabloDoldur();
			}
		});
		btnUpdate.setBounds(132, 145, 89, 23);
		getContentPane().add(btnUpdate);

		txtMessageRead = new JTextArea();
		txtMessageRead.setEditable(false);
		txtMessageRead.setBounds(27, 195, 434, 132);
		getContentPane().add(txtMessageRead);

		txtMessageSend = new JTextArea();
		txtMessageSend.setBounds(27, 339, 301, 45);
		getContentPane().add(txtMessageSend);

		btnSend = new JButton("Send");
		btnSend.setEnabled(false);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int[] rows = new int[table.getSelectedRowCount()];
				String str;
				rows = table.getSelectedRows();
				System.out.println("secilen satir sayisi:"+table.getSelectedRowCount()+" satirlar : "+rows+" row sayisi : "+rows.length);
				clt.sendIds.clear();
				clt.messageIn = "";
				//sendIds, userId, messageIn
				for(int i=0; i<rows.length; i++)
				{
					str = ""+table.getValueAt(rows[i], 0);
					int idvalue = Integer.parseInt(str);
					clt.sendIds.add(idvalue);
					clt.messageIn = txtMessageSend.getText();
				}
				clt.sendUserMessage();

				String input = "["+clt.userId+"]   "+clt.messageIn;
				txtMessageRead.setText(input);

			}
		});
		btnSend.setBounds(355, 349, 89, 23);
		getContentPane().add(btnSend);

		btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clt.extiChat();
			}
		});
		btnExit.setEnabled(false);
		btnExit.setBounds(132, 109, 89, 23);
		getContentPane().add(btnExit);
	}
	
	public void tabloDoldur()
	{
		btnUpdate.setEnabled(false);
		repaint();

		for(;;)
		{
			if (clt.flag)
			{
				System.out.println("flag durum:"+clt.flag);

				clt.flag = false;
				System.out.println("tablo boyu : "+clt.ids.size());
				String[][] data1 = new String[clt.ids.size()][3];

				for(int i=0;i<clt.ids.size();i++)
				{
					data1[i][0] = Integer.toString(clt.ids.get(i));
					data1[i][1] = clt.names.get(i);
					data1[i][2] = Integer.toString(clt.conState.get(i));
				}
				btnSend.setEnabled(true);
				DefaultTableModel tm = new DefaultTableModel(data1, new String[]{"Id","User","State"});
				table.setModel(tm);
				clt.flag = false;
				btnUpdate.setEnabled(true);
				break;
			}
		}
	}
}
