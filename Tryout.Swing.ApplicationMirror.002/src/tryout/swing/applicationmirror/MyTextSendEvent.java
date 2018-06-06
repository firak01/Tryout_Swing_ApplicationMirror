package tryout.swing.applicationmirror;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.ComponentEvent;

//* FGL: Gescheiterter Versuch einen Event künstlich zu erezugen und zu verschicken.
//public class MyTextSendEvent extends AWTEvent{ //So kann man ihn nicht in die Component Map einschleusen.
public class MyTextSendEvent extends ComponentEvent{
	String sTextToSend = null;
	
	public MyTextSendEvent(Component objC, int iId, String sTextToSend){
		super(objC, iId);
		this.setTextToSend(sTextToSend);
	}
	
	public String getTextToSend(){
		return this.sTextToSend;
	}
	public void setTextToSend(String sTextToSend){
		this.sTextToSend = sTextToSend;
	}
	
	
}
