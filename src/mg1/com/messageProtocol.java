package mg1.com;

import java.util.LinkedList;

public class messageProtocol {
	public String clientMessage(LinkedList<Integer> receiveId, int sendId, String messageToSend)
	{
		String messageT2M="1";

		int size = receiveId.size();
		String buffer = Integer.toString(size);
		messageT2M = messageT2M+zeroPad(buffer,8);
		buffer = Integer.toString(sendId);
		messageT2M = messageT2M + zeroPad(buffer,8);

		for(int i=0;i<receiveId.size();i++)
		{
			buffer = Integer.toString(receiveId.get(i));
			messageT2M = messageT2M + zeroPad(buffer,8);
		}

		messageT2M = messageT2M + messageToSend;

		return messageT2M;
	}

	public String registerUserMessage(String name, String password)
	{
		String messageT2M="3";
		messageT2M = messageT2M + stringFormat(name);
		messageT2M = messageT2M + password;
		return messageT2M;
	}

	public String updateUserMessage(int sendId, boolean connState)
	{
		int state=0;
		if(connState)
		{
			state = 1;
		}

		String messageT2M="2";
		String buffer = Integer.toString(sendId);
		messageT2M = messageT2M + zeroPad(buffer,8);
		buffer = Integer.toString(state);
		messageT2M = messageT2M + buffer;
		return messageT2M;
	}        

	public String IdQueryUserMessage(String name)
	{
		String messageT2M="5";
		messageT2M = messageT2M + name;

		return messageT2M;
	}

	public String serverMessage(String name, String message)
	{
		String messageT2M="1";
		messageT2M = messageT2M + stringFormat(name);
		messageT2M = messageT2M + message;
		return messageT2M;
	}

	public String IdResponseMessage(int Id)
	{
		String messageT2M="3";
		String buffer = Integer.toString(Id);
		messageT2M = messageT2M + zeroPad(buffer,8);
		return messageT2M;
	}

	public String allDatabaseMessage(LinkedList<Integer> ids, LinkedList<String> names, LinkedList<Boolean> connStates)
	{
		String messageT2M="2";
		int count = ids.size();
		messageT2M = messageT2M + zeroPad(Integer.toString(count),8);
		int connS;
		for(int i=0;i<count;i++)
		{
			connS= 0;
			messageT2M = messageT2M + zeroPad(Integer.toString(ids.get(i)),8);
			messageT2M = messageT2M + stringFormat(names.get(i));
			if(connStates.get(i))
			{
				connS= 1;
			}
			messageT2M = messageT2M + Integer.toString(connS);              
		}
		return messageT2M;
	}

	public String exitChat(int id)
	{
		String messageT2M="5";
		String buffer = Integer.toString(id);
		messageT2M = messageT2M + zeroPad(buffer,8);

		return messageT2M;		
	}
	
	public String updateRequest(int id)
	{
		String messageT2M="4";
		String buffer = Integer.toString(id);
		messageT2M = messageT2M + zeroPad(buffer,8);

		return messageT2M;
	}
	
	private String stringFormat(String input)
	{
		String str, str2;
		
		str2 = Integer.toString(input.length());
		str2 = zeroPad(str2, 8);
		str = str2;
		str = str + input;
		
		return str;
	}
	
	private String zeroPad(String input, int padding)
	{
		String padded="";

		int diff = padding - input.length();

		for (int i=0;i<diff;i++)
		{
			padded = padded+"0";            
		}
		padded = padded + input;

		return padded;
	}
}
