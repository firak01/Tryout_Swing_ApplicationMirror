package tryout.swing.applicationmirror;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class ComponentMap extends HashMap implements AWTEventListener {
    
    public ComponentMap() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        tk.addAWTEventListener(this,
            AWTEvent.COMPONENT_EVENT_MASK);
    }

    public void eventDispatched(AWTEvent evt) {
        try {
            System.out.println("evt = " + evt);
                       
            ComponentEvent ce = (ComponentEvent)evt;            
            System.out.println("storing component: " + ce.getComponent().getName());
            this.put(
                ce.getComponent().getName(),
                ce.getComponent()
                );
        } catch (Exception ex) {
            System.out.println("ex: " + ex);
        }
    }

}
