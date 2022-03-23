package tp_pro_res;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ClientChat extends Application{
	private PrintWriter pw;
	public static void main(String[] args) {
		 launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Client Chat");
		BorderPane borderPane=new BorderPane(); //element conteneur (poss�de 5 parties N S E O C)
		
		Label labelHost=new Label("Host:");
		TextField textFieldHost=new TextField("localhost");
		Label labelPort=new Label("Port:");
		TextField textFieldPort=new TextField("1234");
		Button buttonConnecter=new Button("Connecter");
		
		HBox hbox=new HBox(); hbox.setSpacing(10); hbox.setPadding(new Insets(10));
		hbox.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, null, null)));
		hbox.getChildren().addAll(labelHost,textFieldHost,labelPort,textFieldPort,buttonConnecter);
		borderPane.setTop(hbox);
		
		VBox vbox=new VBox(); vbox.setSpacing(10); vbox.setPadding(new Insets(10));
		ObservableList<String> listModel=FXCollections.observableArrayList(); //listModel o� se trouve les donn�es
		ListView<String> listView=new ListView<String>(listModel); 
		vbox.getChildren().add(listView);
		borderPane.setCenter(vbox);
		
		Label labelMessage=new Label("Message");
		TextField textFieldMessage=new TextField(); textFieldMessage.setPrefSize(300, 30);
		Button buttonEnvoyer=new Button("Envoyer");
		
		HBox hbox2=new HBox(); hbox2.setSpacing(10); hbox2.setPadding(new Insets(10));
		hbox2.setBackground(new Background(new BackgroundFill(Color.CORNSILK, null, null)));
		hbox2.getChildren().addAll(labelMessage,textFieldMessage,buttonEnvoyer);
		borderPane.setBottom(hbox2);
		
		Scene scene=new Scene(borderPane,500,400); //espace dans lequel je vais afficher quelque chose
		primaryStage.setScene(scene);
		primaryStage.show();
		
		buttonConnecter.setOnAction((evt)->{
			String host=textFieldHost.getText();
			int port=Integer.parseInt(textFieldPort.getText());
			try {
				Socket socket=new Socket(host,port);
				InputStream is=socket.getInputStream();
				InputStreamReader isr=new InputStreamReader(is);
				BufferedReader br=new BufferedReader(isr);
				pw=new PrintWriter(socket.getOutputStream(),true);
				new Thread(()->{	  
					while(true) {
						
							try {
							 String reponse = br.readLine(); 
							 Platform.runLater(()->{
							 listModel.add(reponse);
							 });
							}catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	
					}
				}).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		});
		
		buttonEnvoyer.setOnAction((evt)->{
			String message=textFieldMessage.getText();
			pw.println(message);
		});
		
	}
	
	    
}