package tryout.swing.applicationmirror;

import java.io.Serializable;

public class MyMessage implements Serializable {
	private String sMessage=null;
	private int iIndex = -1;
	
	public MyMessage(){		
	}
	
	public void setMessage(String sMessage){
		this.sMessage=sMessage;
	}
	public String getMessage(){
		return this.sMessage;
	}
	
	public void setIndex(int iIndex){
		this.iIndex = iIndex;
	}
	public int getIndex(){
		return this.iIndex;
	}
	
}
