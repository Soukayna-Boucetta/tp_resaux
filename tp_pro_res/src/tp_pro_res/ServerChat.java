package tp_pro_res;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerChat extends Thread {
	private boolean isActive=true;
	private int nombreClient=0;
	private List<Conversation> Clients=new ArrayList<Conversation>();

	public static void main(String[] args) {
		new ServerChat().start();

	}

	@Override
	public void run() {
		try {
			ServerSocket ss=new ServerSocket(1234);
			while(isActive) {
				Socket socket=ss.accept();
				++nombreClient;
				Conversation conversation=new Conversation(socket,nombreClient);
				Clients.add(conversation);
				conversation.start();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	class Conversation extends Thread{
		protected int nombreclient;
		protected Socket socketclient;
		public Conversation(Socket socketclient,int nombreclient) {
			this.nombreclient=nombreClient;
			this.socketclient=socketclient;
		}
		
		public void boadcastMessage(String message,Socket socket,int numeroclient) {
			
				try {
					for(Conversation client:Clients) {
						if(client.socketclient!=socket) {
							if(client.nombreclient==numeroclient || numeroclient==-1) {
								PrintWriter printWriter=new PrintWriter(client.socketclient.getOutputStream(),true);
								printWriter.println(message);
							}
							
						}
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			
		}
		
		
		@Override
		public void run() {
			try {
				InputStream is=socketclient.getInputStream();
				InputStreamReader isr=new InputStreamReader(is);
				BufferedReader br=new BufferedReader(isr);
				
				OutputStream os=socketclient.getOutputStream();
				PrintWriter pw=new PrintWriter(os,true);
				String IP=socketclient.getRemoteSocketAddress().toString();
				System.out.println("connexion du client numéro "+nombreclient+" IP="+IP);
				pw.println("Bien venue vous etes le client numéro "+nombreclient);
				while(true){
					String req=br.readLine();
					if(req.contains("=>")) {
						String requestParams[]=req.split("=>");
						if(requestParams.length==2);
						String message=requestParams[1];
						int numeroClient=Integer.parseInt(requestParams[0]);
						boadcastMessage(req,socketclient,numeroClient);
						}
					else {
						boadcastMessage(req,socketclient,-1);
					}
					}
					
				
			} catch (IOException e) {
				e.printStackTrace();
			}			
		
		}
		
	}

}
