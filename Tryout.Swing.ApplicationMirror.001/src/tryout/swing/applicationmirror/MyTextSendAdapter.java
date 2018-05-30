package tryout.swing.applicationmirror;

public class MyTextSendAdapter implements IMyTextSendListener {

	@Override
	public void sendText(MyTextSendEvent e) {
		System.out.println("MyTextSendAdapter.sentText()");
	}

}
