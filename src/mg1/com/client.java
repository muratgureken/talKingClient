package mg1.com;

import java.net.*;
import java.sql.SQLException;
import java.util.LinkedList;
import java.io.*; 

public class client{
	// initialize socket and input output streams 
	private Socket socket            = null; 
	private DataInputStream  input   = null; 
	private DataOutputStream out     = null; 
	private boolean keepTalking=true;
	public int messageId, userId;
	public static String messageIn, userName;
	public String otherUserName;
	public LinkedList<Integer> ids = new LinkedList<Integer>();
	public LinkedList<String> names = new LinkedList<String>();
	public LinkedList<Integer> conState = new LinkedList<Integer>();
	public LinkedList<Integer> sendIds = new LinkedList<Integer>();
	public int dbSize;
	public boolean flag=false;
	messageProtocol msgp;
	private String tcpMessage;
	public boolean userConnState;
	public boolean justMessageReceived=false;

	// constructor to put ip address and port 
	public client() 
	{ 		
		msgp = new messageProtocol();

	}

	public void clientConnect(String address, int port) 
	{
		//port = 4500;
		// establish a connection 
		try
		{ 
			socket = new Socket(address, port); 
			System.out.println("Connected"); 

			// takes input from terminal 
			input = new DataInputStream(
					new BufferedInputStream(socket.getInputStream()));  
			// sends output to the socket 
			OutputStream outToServer = socket.getOutputStream();
			out = new DataOutputStream(outToServer);
		} 
		catch(UnknownHostException u) 
		{ 
			System.out.println(u); 
		} 
		catch(IOException i) 
		{ 
			System.out.println(i); 
		} 

		// string to read message from input 

		// keep reading until "Over" is input 
		Thread talKingTh = new Thread()
		{
			int nameSize, messageOfset;
			
			public void run()
			{								
				for(;;)
				{
					if(keepTalking)
					{
						String line = ""; 
						System.out.println("client mesaj bekler");
						try {
							line = input.readUTF();
							System.out.println("client mesaj bekler-2");
							justMessageReceived = false;
							messageId = Integer.parseInt(line.substring(0,1));
							//NEDEN VAR?
							//out.writeUTF(line); 
							
							switch(messageId)
							{
							case 1:
								nameSize = Integer.parseInt(line.substring(1,9));
								otherUserName = line.substring(9,9+nameSize);
								messageIn = line.substring(9+nameSize);
								justMessageReceived = true;
								break;
							case 2:
								flag = true;
								messageOfset = 0;
								ids.clear();
								names.clear();
								conState.clear();
								dbSize = Integer.parseInt(line.substring(1,9));
								System.out.println("client 2 nolu mesaj:"+line+" dbsize:"+dbSize);
								System.out.println("mesaj ofset1: "+messageOfset+" mesaj: "+line.substring(9+messageOfset,17+messageOfset)+
										" "+line.substring(17+messageOfset,25+messageOfset));

								for(int i=0;i<dbSize;i++)
								{
									System.out.println("adim: "+i);
									ids.add(Integer.parseInt(line.substring(9+messageOfset,17+messageOfset)));
									nameSize = Integer.parseInt(line.substring(17+messageOfset,25+messageOfset));
									names.add(line.substring(25+messageOfset,25+nameSize+messageOfset));
									System.out.println("mesaj ofset: "+messageOfset+" mesaj: "+line.substring(9+messageOfset,17+messageOfset)+
											" "+line.substring(17+messageOfset,25+messageOfset)+" "+line.substring(25+messageOfset,25+nameSize+messageOfset)+
											" "+line.substring(25+nameSize+messageOfset,25+nameSize+messageOfset+1));
									conState.add(Integer.parseInt(line.substring(25+nameSize+messageOfset,25+nameSize+messageOfset+1)));
									messageOfset = 17+nameSize+messageOfset;
								}

								break;
							case 3:
								userId = Integer.parseInt(line.substring(1,9));
								break;
							}
						} 
	
						catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					}
				}
			}
		};
		talKingTh.start();
	}

	public void sendUserMessage()
	{
		tcpMessage = msgp.clientMessage(sendIds, userId, messageIn);
		try {
			out.writeUTF(tcpMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendUpdate()
	{
		tcpMessage = msgp.updateUserMessage(userId, userConnState);
		try {
			System.out.println("client update message: "+tcpMessage);
			out.writeUTF(tcpMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendRegister()
	{
		tcpMessage = msgp.registerUserMessage(userName, "123");
		try {
			out.writeUTF(tcpMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void extiChat()
	{
		tcpMessage = msgp.exitChat(userId);
		System.out.println("exit message : "+tcpMessage);
		try {
			out.writeUTF(tcpMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void clientStopTalking()
	{
		keepTalking = false;
		// close the connection 
		try
		{ 
			input.close(); 
			out.close(); 
			socket.close(); 
		} 
		catch(IOException i) 
		{ 
			System.out.println(i); 
		} 
	}
}
