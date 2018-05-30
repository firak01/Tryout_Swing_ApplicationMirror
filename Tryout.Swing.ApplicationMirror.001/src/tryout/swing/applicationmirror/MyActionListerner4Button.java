package tryout.swing.applicationmirror;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyActionListerner4Button implements ActionListener{
	ApplicationMirrorTest objAppl = null;
	public MyActionListerner4Button(ApplicationMirrorTest objAppl){
		this.objAppl = objAppl;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		System.out.println("ActionPerformed.");
		
		MyTextSendEvent myEvent = new MyTextSendEvent((Component)arg0.getSource(), 1, "TTTTT");	
		
		
		//Schleuse diesen "künstlichen" Event nun in die ComponentMap ein....
		ComponentMap objMapComponent = (ComponentMap) this.getApplication().component_map;
		//.put(100, myEvent); //Füge das irgendwie dem Stack hinzu.
		objMapComponent.eventDispatched(myEvent);
	}
	
	public ApplicationMirrorTest getApplication(){
		return objAppl;
	}

}
