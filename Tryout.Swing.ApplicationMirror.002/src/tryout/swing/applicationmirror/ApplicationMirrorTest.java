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
    ObjectOutputStream objOutputStream = null;
    ServerSocket objServer = null;
    Socket objSocketForServer = null;
    int iServerSocketPort = 6754; //ORIGINAL: 6754;
    String sServerUrl = "localhost";
    
    public ApplicationMirrorTest() {
        component_map = new ComponentMap();
        
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        
        final JButton button = new JButton("Clicke -> übertrage an Server den Click.");
        button.setName("button");
        
        //Allerdings wird der Text nicht übertragen.
        JTextField tf = new JTextField("Text. Wie übertragen?");
        tf.setName("textfield");
        
        //Damit auch mal was passiert, wenn man den Button clickt...
        MyActionListerner4Button myListener = new MyActionListerner4Button(this, tf);
        button.addActionListener(myListener);
        
      //Aber auch auch ohne meinen ActionListener, wird in der Server Applikation auch der Button geclickt....
        //und auch das Bewegen des Mauszeigers auf den Button erzeugt auf dem Server einen Effekt.
        frame.getContentPane().add(button);
        
       
        frame.getContentPane().add(tf);
        
        frame.pack();
        frame.show();
    }
    
    public void openSender(Socket sock) throws Exception {    	
    	this.isServer(false);
          
    	final ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());  
    	this.setObjectOutputStream(out);
        Toolkit.getDefaultToolkit().addAWTEventListener(
            new AWTEventListener() {
                public void eventDispatched(AWTEvent evt) {
                    try {
                        if(evt instanceof MouseEvent) {
                            System.out.println("Client: MouseEvent passiert...");
                            MouseEvent me = (MouseEvent)evt;
                            
                            //Versuche etwas sinnvolles mitzugeben.
                            //1. Die Komponente beim Sender im Zugriff....
//                            if(me.getComponent() instanceof JTextField){
//                            	JTextField objText = (JTextField) me.getComponent();
//                            	String stemp = objText.getText();
//                            	System.out.print("Eingegebener Text: " + stemp + "\n");
//                            	//Das klappt nicht out.writeObject("Eingegebener Text:"+stemp);
//                            	//Versuch den Event zu manipulieren.... Wenn das Klappt kann man ggfs. einen eigenen Event "verschicken"
//                            	
//                            	 /* Create The Message Object to send */
//                                Vector<MyMessage> vecMessage = new Vector<MyMessage>();
//                                MyMessage msg = new MyMessage();
//                                msg.setMessage(stemp);
//                                msg.setIndex(1);
//                                vecMessage.add(msg);
//
//                                /* Send the Message Object to the server */
//                                out.writeObject(vecMessage); 
//                            	
//                            }
                            
                            out.writeObject(me.getComponent().getName());                            
                            out.writeObject(evt);
                        }else{
                        	System.out.println("Client: ANDERER EVENT\n");
                        	System.out.println(evt.getClass().getName());
                        	
//                        	MyTextSendEvent mtse = (MyTextSendEvent)evt;
//                        	 out.writeObject(mtse.getComponent().getName());                            
//                             out.writeObject(evt);
                        }
                    } catch (Exception ex) { 
                    	System.out.println("Client: Exception: " + ex);
                    }
                }
            },
            AWTEvent.ACTION_EVENT_MASK | 
            AWTEvent.MOUSE_EVENT_MASK
        );
    }
    
    public void openReceiver() throws Exception  {
        // receive events        
        this.isServer(true);    
        System.out.println("Server: Trying - Open Socket for server.");   
        ServerSocket server = new ServerSocket(this.getServerSocketPort());
        System.out.println("Server: Waiting for connection. Before 'accept()'.");
        Socket sock = server.accept();
        
        
        //klappt nicht mit dem accept() darin.... Socket sock = this.getSocketForServer();
        //ServerSocket socket = this.getServerSocket();
        //System.out.println("Server: Waiting for connection. Before 'accept()'.");
        //final Socket sock = socket.accept(); //DAS blockiert solange, bis ein Client eine Verbindung aufbaut.
        System.out.println("Server: Socket for server opened. After 'accept()'.");
        
        EventQueue eq = Toolkit.getDefaultToolkit().getSystemEventQueue();
        System.out.println("Server: EventQueue geholt.");
        
        InputStream in = sock.getInputStream();
        System.out.println("Server: InputStream geholt.");
        
        ObjectInputStream in4object = new ObjectInputStream(in);
        System.out.println("Server: ObjectInputStream geholt.");      
        String id = "ohne id";
        while(true) {
        	System.out.println("Server: In While Loop");
        	Object obj = in4object.readObject();
        	System.out.println("Server: Class of readObject(): " + obj.getClass().getName());
        	        	        	
        	if(obj instanceof AWTEvent){
	            AWTEvent evt = (AWTEvent) obj;
	            if(evt instanceof MouseEvent) {
	            	System.out.println("Server: Mouse Event empfangen .... ");
	                MouseEvent me = (MouseEvent)evt;
	                
	                
	                //Also DIREKT steht die Komponente wohl nicht zur Verfügung. Fehlermeldung: exception: java.net.ConnectException: Connection refused: connect
	//                System.out.println("Component: " + me.getComponent().getClass().getSimpleName());
	//                if(me.getComponent() instanceof JTextField){
	//                	JTextField objText = (JTextField) me.getComponent();
	//                	String stemp = objText.getText();
	//                	System.out.print("Text: " + stemp);
	//                }
	                
	                 
	                System.out.println("Server: Eventid = " + me.getID());
	                System.out.println(me.paramString());
	                
	                System.out.println("Server: Make the mouse event a clone.");
	                
	                //Aus dem empfangenen Event wird ein neuer Event gebaut. Dieser wird in den EventQueue gestellt.
	                //Das reicht aus, um den Click auf den Button zu übertragen.
	                
	                //1. Anhand der component_map die componente über die angegeben ID holenn
	                Component objComponent = (Component)component_map.get(id);
	                if(objComponent==null){
	                	System.out.println("Server: Compoent with id '" + id + "' not found.");
	                	
	                }else{
		                MouseEvent me2 = new MouseEvent(
	                    objComponent,
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
	                }
	            }else{
	            	//BAUE DEN EMPFANGSTEIL FÜR ANDERE EVENTS, DIE ICH KÜNSTLICH HINZUGEFÜGT HABE...
	            	System.out.println("Server: ANDEREN EVENT empfangen .... ");
	            	System.out.println(evt.getClass().getName());  
	            	
	            	
	            	
	            }
        	}else{
        		System.out.print("Server: ANDERES OBJECT empfangen .... ");
            	System.out.println(obj.getClass().getName());       
            	
            	if(obj instanceof String){
    	            id = (String) obj;
    	            System.out.println("Server: openReceiver() . Lese id="+id);
            	}
            	
            	 //Hole ein Objekt aus dem Stream...
                if(obj instanceof MyMessage){
	                MyMessage objMessage = (MyMessage) obj;
	                if(objMessage!=null){
	                	 System.out.println("Server: Aus MyMessage empfangener STRING = " + objMessage.getMessage());
	                	 
	                	 //TODO: Aus der objComponent nun das JTextfield Objekt holen.
	                	 //      Dazu muss die ID in der MyMessage enthalten sein.
	                	 //      Dann kann man den Text in das JTextfield Objekt setzen.
	                }
                }
            	
        	}
        }

    }
    
    public void start() {
        try {
            // send events            
        	final Socket sock = this.getSocketForClient();
            System.out.println("Client: Socket geöffnet... Dann steht die Ressource des Servers zur Verfügung. Ist also Sender...");
            openSender(sock);
        } catch (Exception ex) {
            try {
            	System.out.println("Client: Couldn't open socket. Must be the server");
                openReceiver();
            } catch (Exception ex2) {
                System.out.println("Server: Exception: " + ex2);
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

    private boolean isServer(){
    	return this.bServer;
    }
    private void isServer(boolean bServer){
    	this.bServer = bServer;
    }
    public ObjectOutputStream getObjectOutputStream() throws IOException{
    	//if(this.objOutputStream==null){
    		//NEIN: Der ist nur 1x möglich, sonst "Gebunden", Socket sock = this.getSocketForServer();
    		//ALSO: 
    		//Socket sock = this.getSocketForClient();
    		//this.objOutputStream = new ObjectOutputStream(sock.getOutputStream());
    	//}
    	return this.objOutputStream;
    		//return new ObjectOutputStream(sock.getOutputStream());
    }
    public void setObjectOutputStream(ObjectOutputStream objOutputStream){
    	this.objOutputStream = objOutputStream;
    }
    
    public ServerSocket getServerSocket() throws IOException{
    	if(this.objServer==null){
    		System.out.println("Server - Trying: New ServerSocket for port " + this.getServerSocketPort());
    		this.objServer = new ServerSocket(this.getServerSocketPort());    		
    		System.out.println("Server - ServerSocket for port " + this.getServerSocketPort() + " created.");
    	}
    	return this.objServer;
    }    
    
    public Socket getSocketForServer() throws IOException{
    	if(this.objSocketForServer==null){
    		ServerSocket objServer = this.getServerSocket();
    		System.out.println("Server: Trying - Create SocketForServer.");
    		Socket objSocket = objServer.accept();
    		
    		System.out.println("Server: SocketForServer created()");
    		this.objSocketForServer = objSocket;
    	}
    	return this.objSocketForServer;
    }
    
    public int getServerSocketPort(){
    	return this.iServerSocketPort;
    }
    public String getServerUrl(){
    	return this.sServerUrl;
    }

    public Socket getSocketForClient() throws IOException{
    	System.out.println("Client: Trying - Create SocketForClient.");
    	final Socket sock = new Socket(this.getServerUrl(),this.getServerSocketPort());
    	return sock;
    }
}
