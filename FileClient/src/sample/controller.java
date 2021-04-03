package sample;

import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class controller {

    @FXML
    private Button Download;
    @FXML
    private Button Upload;
    @FXML
    private Button Update;
    @FXML
    private ListView client;
    @FXML
    private ListView Server;
    @FXML
    private javafx.scene.control.Label lblSystemMessage;
    @FXML
    private javafx.scene.control.Label text1;
    @FXML
    private javafx.scene.control.Label text2;
    @FXML
    private javafx.scene.control.Button Hide;



    protected List<String> ClientFiles = new ArrayList<>();
    protected List<String> ServerFiles = new ArrayList<>();
    protected ListProperty<String> ClientFileList = new SimpleListProperty<>();
    protected ListProperty<String> ServerFileList = new SimpleListProperty<>();
    final File Client_folder = new File("ClientFiles");
    private PrintWriter Out = null;
    private BufferedReader In = null;

    public void initialize() {
        text1.setFont(new Font("Cambria", 25));
        text2.setFont(new Font("Cambria", 25));
        String DataFromServer = "";
        String message1= "";
        String message2= "";

        try{
            Socket clientSocket = new Socket("localhost", 8080);
            Out = new PrintWriter(new BufferedOutputStream(clientSocket.getOutputStream()));
            In =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            message1 = In.readLine();
            message2 = In.readLine();
            Out.println("Dir");
        Out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            DataFromServer = In.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] FromeServer = DataFromServer.split(" ");
        for (String file : FromeServer) {

            ServerFiles.add(file);

        }


        // takes the file folder and makes it a list that we can go through
        File[] listOfFilesClient = Client_folder.listFiles();
        for (File file : listOfFilesClient) {
            if (file.isFile()) {
                ClientFiles.add(file.getName());
            }
        }

        Server.itemsProperty().bind(ServerFileList);
        client.itemsProperty().bind(ClientFileList);
        ClientFileList.set(FXCollections.observableArrayList(ClientFiles));
        ServerFileList.set(FXCollections.observableArrayList(ServerFiles));
    }

    public void helpLable(ActionEvent event){
        Hide.setVisible(true);
        lblSystemMessage.setText(
                "Help Menu:" +
                "\nUploaded:" +
                "\n 1.choose folder you would like to upload" +
                "\n  from client-side then press Upload"+
                "\nDownload:" +
                "\n 1.choose folder you would like to download" +
                "\n  from server-side Then press Download"
        );
        Hide.setText("Hide Text");
        Hide.setLayoutX(390);
        Hide.setLayoutY(845);
    }
    public void hideLable(ActionEvent event){
        lblSystemMessage.setText("");
        Hide.setVisible(false);
    }

    public void btnOnPressdownload(ActionEvent actionEvent) throws IOException {
        Socket clientSocket = null;
        PrintWriter OUT = null;
        String DataFromServer;
        String message1= "";
        String message2= "";

        try {
            clientSocket = new Socket("localhost",8080);
            OUT = new PrintWriter(new BufferedOutputStream(clientSocket.getOutputStream()));
            OUT.println("DownLoad"+" "+Server.getSelectionModel().getSelectedItems().get(0));
            In =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            message1 = In.readLine();
            message2 = In.readLine();
            OUT.flush();

      } catch (Exception e) {
          e.printStackTrace();
      }

        try{
            DataFromServer = In.readLine();
            String path = "ClientFiles/" + Server.getSelectionModel().getSelectedItems().get(0);
            File IncomingFile= new File(path);
            if(!IncomingFile.exists()) {
                IncomingFile.createNewFile();
            }
            FileWriter fw=new FileWriter(path);
            String [] Sentences =DataFromServer.split(" ");
            for(String Sentence:Sentences){
                fw.write(Sentence+" ");
            }
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        clientSocket.close();
        ListViewUpdate();
    }

    public void btnOnPressUpload(ActionEvent actionEvent) {
        Socket clientSocket = null;
        PrintWriter out = null;
        ClientFiles.clear();
        ServerFiles.clear();
        try{
            clientSocket = new Socket("localhost",8080);
            out = new PrintWriter(new BufferedOutputStream(clientSocket.getOutputStream()));
            out.println("Upload"+" "+client.getSelectionModel().getSelectedItems().get(0));
            File[] listOfFilesClient = Client_folder.listFiles();
            for (File file : listOfFilesClient) {
                if (file.getName().equals(client.getSelectionModel().getSelectedItems().get(0)) ) {
                    //this is where the file content is sent through the socket
                    File FileReading = new File(String.valueOf(file.getAbsoluteFile()));
                    Scanner sc = new Scanner(file);
                    String FileSending = " ";
                    while (sc.hasNextLine()) {
                        FileSending +=sc.nextLine()+" ";
                    }
                    out.println(FileSending);
                }
            }
            out.flush();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    ListViewUpdate();
    }

    public void exitProgram(ActionEvent event){
        System.exit(0);
    }

    public void ListViewUpdate() {
        ServerFiles.clear();
        ClientFiles.clear();

        String DataFromServer = "";
        String message1= "";
        String message2= "";

        try{
            Socket clientSocket = new Socket("localhost", 8080);
            Out = new PrintWriter(new BufferedOutputStream(clientSocket.getOutputStream()));
            In =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            message1 = In.readLine();
            message2 = In.readLine();
            Out.println("Dir");
            Out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            DataFromServer = In.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] FromeServer = DataFromServer.split(" ");
        for (String file : FromeServer) {

            ServerFiles.add(file);

        }


        // takes the file folder and makes it a list that we can go through
        File[] listOfFilesClient = Client_folder.listFiles();
        for (File file : listOfFilesClient) {
            if (file.isFile()) {
                ClientFiles.add(file.getName());
            }
        }



        Server.itemsProperty().bind(ServerFileList);
        client.itemsProperty().bind(ClientFileList);
        ClientFileList.set(FXCollections.observableArrayList(ClientFiles));
        ServerFileList.set(FXCollections.observableArrayList(ServerFiles));
    }


    public void btnOnPressupdate(ActionEvent actionEvent) {
        ListViewUpdate();
    }
}
