package tp_pro_res;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServerJeu extends Thread {
	private boolean isActive=true;
	private int nombreClient=0;
	private int nombreSecret;
	private boolean fin;
	private String gagnant;

	public static void main(String[] args) {
		new ServerJeu().start();

	}

	@Override
	public void run() {
		try {
			ServerSocket ss=new ServerSocket(1234);
			nombreSecret=new Random().nextInt(1000);
			System.out.println(" le serveur a choisi son secret : "+nombreSecret);
			while(isActive) {
				Socket socket=ss.accept();
				++nombreClient;
				new Conversation(socket,nombreClient).start();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	class Conversation extends Thread{
		private int nombreclient;
		private Socket socket;
		
		public Conversation(Socket socket,int nombreclient) {
			this.nombreclient=nombreClient;
			this.socket=socket;
		}
		@Override
		public void run() {
			try {
				InputStream is=socket.getInputStream();
				InputStreamReader isr=new InputStreamReader(is);
				BufferedReader br=new BufferedReader(isr);
				
				OutputStream os=socket.getOutputStream();
				PrintWriter pw=new PrintWriter(os,true);
				String IP=socket.getRemoteSocketAddress().toString();
				System.out.println("connexion du client numéro "+nombreclient+" IP="+IP);
				pw.println("Bien venue vous etes le client numéro "+nombreclient);
				pw.println("Devieny le nombre Secret ...");
				
				while(true){
					String req=br.readLine();
					int nombre=0;
					boolean correctFormatRequest=false;
					try {
						nombre=Integer.parseInt(req);
						correctFormatRequest=true;
					}
					catch(NumberFormatException e){
						correctFormatRequest=false;
					}
					if(correctFormatRequest) {
						System.out.println("Client "+IP+" Tentative avec le nombre "+nombre);
						if(fin==false){
							if(nombre>nombreSecret) {
								pw.println("Votre nombre est supérieure au nombre de Secret");
							}
							else if(nombre<nombreSecret) {
								pw.println("Votre nombre est inférieure au nombre de Secret");
							}
							else {
								pw.println("BRAVO, Vous avey gagné");
								gagnant=IP;
								System.out.println("BRAVO au gagnant , IP Client : "+gagnant);
								fin=true;
							}
						}
						else {
							pw.println("JEu terminé, le gagnant est : "+gagnant);
						}
						
					}
					else {
						pw.println("Format de nombre n'est pas correcte");
					}
					
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}			
		
		}
		
	}

}
