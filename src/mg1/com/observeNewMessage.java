package mg1.com;

public class observeNewMessage implements Observer{

	@Override
	public void notify(boolean justReceived) {
		if(justReceived)
		{
			System.out.println("mesaj geldi");
		}
	}

}
