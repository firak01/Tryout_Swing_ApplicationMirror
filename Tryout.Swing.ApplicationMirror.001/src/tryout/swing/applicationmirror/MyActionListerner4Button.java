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
	}
	
	public ApplicationMirrorTest getApplication(){
		return objAppl;
	}

}
