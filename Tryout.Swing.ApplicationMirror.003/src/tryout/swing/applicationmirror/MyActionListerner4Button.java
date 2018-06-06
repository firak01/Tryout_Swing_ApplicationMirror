package tryout.swing.applicationmirror;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.swing.JTextField;

public class MyActionListerner4Button implements ActionListener{
	ApplicationMirrorTest objAppl = null;
	JTextField objField = null;
	public MyActionListerner4Button(ApplicationMirrorTest objAppl, JTextField objField){
		this.setApplication(objAppl);
		this.setTextFieldAssigned(objField);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		/* Sogar diese Action wird "gespiegelt". D.h. der Server würde sich dann wie der Client verhalten.
		 *  Das gibt aber einen Fehlermeldung. Darum hier sicherstellen, dass der Code nur als Client ausgeführt wird.
		 */
		if(this.getApplication().isServer()){
			System.out.println("Server: Hinder ActionPerformed.");
		}else{
			System.out.println("Client: ActionPerformed.");
	
			try {
			JTextField objText = (JTextField) this.getTextFieldAssigned();
	    	String stemp = objText.getText();
	    	System.out.print("Client: Eingegebener Text: " + stemp + "\n");
	    	//Das klappt nicht out.writeObject("Eingegebener Text:"+stemp);
	    	//Versuch den Event zu manipulieren.... Wenn das Klappt kann man ggfs. einen eigenen Event "verschicken"
	    	
	    	System.out.println("Client: Text wird gelöscht.");
	    	objText.setText("");
	    	stemp = objText.getText();
	    	
	    	 /* Create The Message Object to send */
	        //Vector<MyMessage> vecMessage = new Vector<MyMessage>();
	        MyMessage msg = new MyMessage();
	        msg.setMessage(stemp);
	        
	        //Hole die Componente des Textfields
	        //System.out.print("Client: UIClassID des Textfelds: " + this.getTextField().getUIClassID() + "\n");
	        //System.out.print("Client: Name des Textfelds: " + this.getTextField().getName() + "\n");	        
	        msg.setComponentNameAffected(this.getTextFieldAssigned().getName());
	        
	        msg.setIndex(1);
	        //vecMessage.add(msg);
	
	        /* Send the Message Object to the server */
	        ObjectOutputStream out = this.getApplication().getObjectOutputStream();        
	        //out.writeObject(vecMessage);
	        out.writeObject(msg);
	
	        	System.out.println("Client: Messageobjekt an den output Stream übergeben.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	//        
	    	
	    	
	    	//NEINE NEIN
	//		MyTextSendEvent myEvent = new MyTextSendEvent((Component)arg0.getSource(), 1, "TTTTT");	
	//		
	//		
	//		//Schleuse diesen "künstlichen" Event nun in die ComponentMap ein....
	//		ComponentMap objMapComponent = (ComponentMap) this.getApplication().component_map;
	//		//.put(100, myEvent); //Füge das irgendwie dem Stack hinzu.
	//		objMapComponent.eventDispatched(myEvent);
			
		}//end if .isServer()
	}
	
	public ApplicationMirrorTest getApplication(){
		return this.objAppl;
	}
	private void setApplication(ApplicationMirrorTest objAppl){
		this.objAppl = objAppl;
	}
	public JTextField getTextFieldAssigned(){
		return this.objField;
	}
	private void setTextFieldAssigned(JTextField objField){
		this.objField = objField;
	}

}
