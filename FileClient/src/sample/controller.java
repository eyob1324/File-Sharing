package sample;

import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.PrintWriter;
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
    private ListView client;
    @FXML
    private ListView Server;

    protected List<String> ClientFiles = new ArrayList<>();
    protected List<String> ServerFiles = new ArrayList<>();
    protected ListProperty<String> ClientFileList = new SimpleListProperty<>();
    final File Client_folder = new File("ClientFiles");

    public void initialize() {

        // takes the file folder and makes it a list that we can go through
        File[] listOfFilesClient = Client_folder.listFiles();
        for (File file : listOfFilesClient) {
            if (file.isFile()) {
                ClientFiles.add(file.getName());

            }
        }


        client.itemsProperty().bind(ClientFileList);
        ClientFileList.set(FXCollections.observableArrayList(ClientFiles));
    }

    public void btnOnPressdownload(ActionEvent actionEvent) {

    }

    public void btnOnPressUpload(ActionEvent actionEvent) {
        Socket clientSocket = null;
        PrintWriter out = null;



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

    }
}
