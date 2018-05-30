package tryout.swing.applicationmirror;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import java.awt.event.*;
import java.net.*;
import java.io.*;
 /**
  * In Anlehnung an das Buch "Swing Hacks":
  * Man startet die Applikation 2x.
  * Die zuerst gestartete wird der Server (, da noch kein Socket aufgebaut werden kann, geht die Applikation davon aus, sie muss Server werden.
  * Die danach gestartete Applikation wird Client (, nun kann der Socket aufgebaut werden). 
  * @author lindhaueradmin
  *
  */
public class ApplicationMirrorTest {
    Map component_map;
    boolean bServer = false;
    public ApplicationMirrorTest() {
        component_map = new ComponentMap();
        
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        
        final JButton button = new JButton("Clicke -> übertrage an Server den Click.");
        button.setName("button");
        
        
        //Damit auch auf der Konsole mal was passiert, wenn man den Button clickt...
        MyActionListerner4Button myListener = new MyActionListerner4Button(this);
        button.addActionListener(myListener);
        
        //Aber auch auch ohne meinen ActionListener, wird in der Server Applikation auch der Button geclickt....
        //und auch das Bewegen des Mauszeigers auf den Button erzeugt auf dem Server einen Effekt.
        frame.getContentPane().add(button);
        
        //Allerdings wird der Text nicht übertragen.
        JTextField tf = new JTextField("Text. Wie übertragen?");
        tf.setName("textfield");
        frame.getContentPane().add(tf);
        
        frame.pack();
        frame.show();
    }
    
    public void openSender(Socket sock) throws Exception {
    	System.out.println("socket geöffnet... Dann steht die Ressource des Servers zur Verfügung. Ist also Sender...");
    	this.isServer(false);
        final ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            
        Toolkit.getDefaultToolkit().addAWTEventListener(
            new AWTEventListener() {
                public void eventDispatched(AWTEvent evt) {
                    try {
                        if(evt instanceof MouseEvent) {
                            System.out.print("MouseEvent passiert...");
                            MouseEvent me = (MouseEvent)evt;
                            
                            //Versuche etwas sinnvolles mitzugeben.
                            //1. Die Komponente beim Sender im Zugriff....
                            if(me.getComponent() instanceof JTextField){
                            	JTextField objText = (JTextField) me.getComponent();
                            	String stemp = objText.getText();
                            	System.out.print("Eingegebener Text: " + stemp + "\n");
                            	//Das klappt nicht out.writeObject("Eingegebener Text:"+stemp);
                            	//Versuch den Event zu manipulieren.... Wenn das Klappt kann man ggfs. einen eigenen Event "verschicken"
                            	
                            	
                            }
                            
                            out.writeObject(me.getComponent().getName());                            
                            out.writeObject(evt);
                        }else{
                        	System.out.println("ANDERER EVENT\n");
                        	System.out.println(evt.getClass().getName());

                        	//bisher ist hier noch kein anderer Event angekommen.
                        }
                    } catch (Exception ex) { }
                }
            },
            AWTEvent.ACTION_EVENT_MASK | 
            AWTEvent.MOUSE_EVENT_MASK
        );
    }
    
    public void openReceiver() throws Exception  {
        // receive events
        System.out.println("couldn't open socket. must be the server");
    	this.isServer(true);
        ServerSocket server = new ServerSocket(6754);
        Socket sock = server.accept();
        
        EventQueue eq = Toolkit.getDefaultToolkit().getSystemEventQueue();
        
        ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
        while(true ) {
            String id = (String) in.readObject();
            System.out.println("openReceiver() . Lese id="+id);
            
            AWTEvent evt = (AWTEvent) in.readObject();
            if(evt instanceof MouseEvent) {
            	System.out.println("Mouse Event empfangen .... ");
                MouseEvent me = (MouseEvent)evt;
                
                
                //Also DIREKT steht die Komponente wohl nicht zur Verfügung. Fehlermeldung: exception: java.net.ConnectException: Connection refused: connect
//                System.out.println("Component: " + me.getComponent().getClass().getSimpleName());
//                if(me.getComponent() instanceof JTextField){
//                	JTextField objText = (JTextField) me.getComponent();
//                	String stemp = objText.getText();
//                	System.out.print("Text: " + stemp);
//                }
                
                System.out.print("Eventid = " + me.getID());
                System.out.println(me.paramString());
                
                //Aus dem empfangenen Event wird ein neuer Event gebaut. Dieser wird in den EventQueue gestellt.
                //Das reicht aus, um den Click auf den Button zu übertragen.
                MouseEvent me2 = new MouseEvent(
                    (Component)component_map.get(id),
                    me.getID(),
                    me.getWhen(),
                    me.getModifiers(),
                    me.getX(),
                    me.getY(),
                    me.getClickCount(),
                    me.isPopupTrigger(),
                    me.getButton()
                    );
                eq.postEvent(me2);
            }else{
            	//BAUE DEN EMPFANGSTEIL FÜR ANDERE EVENTS, DIE ICH KÜNSTLICH HINZUGEFÜGT HABE...
            	System.out.println("ANDEREN EVENT empfangen .... ");
            	System.out.println(evt.getClass().getName());
            	
            	//bisher ist hier noch kein anderer Event angekommen.
            }
        }

    }
    
    public void start() {
        try {
            // send events
            final Socket sock = new Socket("localhost",6754);            
            openSender(sock);
        } catch (Exception ex) {
            try {
                openReceiver();
            } catch (Exception ex2) {
                System.out.println("exception: " + ex);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ApplicationMirrorTest mirror = new ApplicationMirrorTest();
        mirror.start();        
    }
    


    public static void p(String str) {
        System.out.println(str);
    }
    
    public Map getComponen_Map(){
    	return this.component_map;
    }
    
    private boolean isSever(){
    	return this.bServer;
    }
    private void isServer(boolean bServer){
    	this.bServer = bServer;
    }

}
