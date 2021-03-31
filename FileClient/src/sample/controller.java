package sample;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        System.out.println(client.getSelectionModel().getSelectedItems());
    }
}
