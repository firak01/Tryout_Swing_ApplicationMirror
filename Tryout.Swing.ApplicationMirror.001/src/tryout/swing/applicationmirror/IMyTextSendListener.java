package tryout.swing.applicationmirror;

import java.util.EventListener;

public interface IMyTextSendListener extends EventListener{
	public abstract void sendText(MyTextSendEvent e);
	
}
