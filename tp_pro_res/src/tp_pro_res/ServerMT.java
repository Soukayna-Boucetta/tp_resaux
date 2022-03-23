package tp_pro_res;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMT extends Thread {
	private boolean isActive=true;
	private int nombreClient=0;

	public static void main(String[] args) {
		new ServerMT().start();

	}

	@Override
	public void run() {
		try {
			ServerSocket ss=new ServerSocket(1234);
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
				while(true){
					String req=br.readLine();
					System.out.println("le client "+IP+" envoyer un requet "+req);
					String reponse="length= "+req.length();
					pw.println(reponse);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}			
		
		}
		
	}

}
