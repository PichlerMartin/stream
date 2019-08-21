package testui;

import client.StreamClient;
import client.StreamOptions;
import filelibrary.Library;
import filelibrary.PublicLibrary;
import filelibrary.TorrentInFileSystem;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import support.SupportMethods;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;
import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

/**
 * Controller for testing class UI_Main
 */
public class UI_Controller implements Controller {

    @FXML
    private Stage parentStage;
    @FXML
    private ProgressBar prog_m;
    @FXML
    private Label lbl_status;

    private Library torrents = new PublicLibrary();

    public void setParentStage (Stage root) {
        this.parentStage = root;
    }

    private void loadChooseFileWindow() {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/choosen_files.fxml"));
            root = loader.load();

            Stage stage = new Stage();

            stage.setTitle("Choose Files");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads all files from the resources/torrents directory at startup, for testing purposes
     * @param actionEvent:  ActionEvent-Parameter, currently not in use
     * @throws IOException: In case the files won't load properly
     */
    public void LoadStartConfiguration(ActionEvent actionEvent) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get("target\\classes\\torrents"))) {


            Object[] directoryContent = paths.toArray();

            for (Object s: directoryContent
                 ) {
                String content = s.toString();
                String contentname = content.substring(content.lastIndexOf('\\') + 1);

                if (!contentname.equals("torrents")){
                    torrents.addTorrent(new TorrentInFileSystem(content, contentname.substring(0, contentname.length() - 8)));
                }
            }

            //torrents.getContents().forEach(x -> lbl_filelib.getItems().add(x.getName()));

            loadChooseFileWindow();
        }

        lbl_status.setText("files loaded!");
    }

    /**
     *
     * https://stackoverflow.com/questions/3436823/how-to-calculate-the-hash-value-of-a-torrent-using-java
     *
     * @param torrentPath: path to torrent file
     * @return returns hash value
     */
    private String convertTorrentToMagnet(File torrentPath) {
        String text = "";

        try {
            BufferedReader br = new BufferedReader(new FileReader(torrentPath));
            text = br.readLine();

            text = text.substring(0, text.lastIndexOf(':') + 1);

            String s1 = text.substring(0, 2);
            String s2 = text.substring(text.indexOf("pieces") - 1, text.lastIndexOf(':') - 1);
            String s3 = text.substring(text.indexOf(":name") - 1, text.lastIndexOf(':'));
            //s3 = s3.substring(0, s3.lastIndexOf(':'));
            text = s1 + s2 + ":...................." + s3.substring(0, s3.lastIndexOf(':'));

        } catch (IOException ex){
            ex.printStackTrace();
        }

        return text;
    }

    /**
     * @param mouseEvent: mouse event get thrown when clicked
     * desc:    https://stackoverflow.com/questions/32757069/updating-progressbar-value-within-a-for-loop
     */
    public void click_makeProgress(MouseEvent mouseEvent) {
        new Thread(() -> {
            for(int i = 0; i <=100; i++){
                final int position = i;
                Platform.runLater(() -> {
                    prog_m.setProgress(position/100.0);
                });
                try{
                    Thread.sleep(100);
                }catch(Exception e){ System.err.println(e); }
            }
        }).start();
    }

    @Override
    public Label getLabel() {
        return this.lbl_status;
    }
    @Override
    public void setLabel(Label status) {
        this.lbl_status = status;
    }
}
