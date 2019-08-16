package testui;

import client.StreamClient;
import client.StreamOptions;
import filelibrary.Library;
import filelibrary.PublicLibrary;
import filelibrary.TorrentInFileSystem;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    private ListView<String> lbl_filelib;
    @FXML
    private Button cmd_enter;
    @FXML
    private Button cmd_start;
    /**
     * Progressbar-Test to overlook the functionality and ability of the use-case
     */
    @FXML
    private ProgressBar prog_m;
    @FXML
    private Label lbl_status;
    @FXML
    private TextField txt_dldirectory;
    @FXML
    private TextField txt_magnetlink;

    private Library torrents = new PublicLibrary();

    public void setParentStage (Stage root) {
        this.parentStage = root;
    }

    public void Enter_Action(ActionEvent actionEvent) throws MalformedURLException {
        String DownloadDirectory = "C:\\";
        String DefaultMagnetLink = "magnet:?xt=urn:btih:d1eb2b5cf80e286a7f848ab0c31638856db102d4";

        if (txt_dldirectory.getText().contains("\\")){
            DownloadDirectory = txt_dldirectory.getText();
        }

        if (txt_magnetlink.getText().contains("magnet:?xt=urn:btih:")){
            DefaultMagnetLink = txt_magnetlink.getText();
        }


        //  ToDo:   something, something like this below but has to be altered (magnet-link, etc.)
        //  ToDo:   update: below if statement does not work and should be removed in next update

        if(lbl_filelib.getSelectionModel().getSelectedItem() != null){
            final String MagnetLink = lbl_filelib.getSelectionModel().getSelectedItems().get(0);
            Optional<TorrentInFileSystem> MagnetLinkOptional = torrents.getContents().stream().filter(x -> x.getName().equals(MagnetLink)).findFirst();

            if (MagnetLinkOptional.isPresent()){
                DefaultMagnetLink = "magnet:?xt=urn:btih:" + sha256Hex(convertTorrentToMagnet(new File(MagnetLinkOptional.get().getPath())));
            }
        }

        StreamOptions options = new StreamOptions(DefaultMagnetLink, new File(DownloadDirectory));

        StreamClient client = new StreamClient(options, this);
        new Thread(() -> {
            client.start();
        }).start();

		
		//	ToDo:	Check sample from bt github page

        /*
        // enable multithreaded verification of torrent data
        Config config = new Config() {
            @Override
            public int getNumOfHashingThreads() {
                return Runtime.getRuntime().availableProcessors() * 2;
            }
        };

        // enable bootstrapping from public routers
        Module dhtModule = new DHTModule(new DHTConfig() {
            @Override
            public boolean shouldUseRouterBootstrap() {
                return true;
            }
        });

        // get download directory
        Path targetDirectory = new File("~/Downloads").toPath();

        // create file system based backend for torrent data
        Storage storage = new FileSystemStorage(targetDirectory);

        // create client with a private runtime
        BtClient client = Bt.client()
                .config(config)
                .storage(storage)
                .magnet("magnet:?xt=urn:btih:af0d9aa01a9ae123a73802cfa58ccaf355eb19f1")
                .autoLoadModules()
                .module(dhtModule)
                .stopWhenDownloaded()
                .build();

        // launch
        client.startAsync().join();
        */
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

            torrents.getContents().forEach(x -> lbl_filelib.getItems().add(x.getName()));
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
        /*
        MessageDigest md5;
        byte[] buffer = new byte[1024];
        int bytesRead;
        InputStream is;
        StringBuffer sb = new StringBuffer();

        try {
            is = new FileInputStream(torrentPath);

            md5 = MessageDigest.getInstance("MD5");

            try {
                while ((bytesRead = is.read(buffer)) > 0) {
                    md5.update(buffer, 0, bytesRead);
                }
            } catch (IOException e) {

                e.printStackTrace();
            }

            byte[] md5ChkSumBytes = md5.digest();

            sb = new StringBuffer();

            for (int j = 0; j < md5ChkSumBytes.length; j++) {
                String hex = Integer.toHexString(
                        (md5ChkSumBytes[j] & 0xff | 0x100)).substring(1, 3);
                sb.append(hex);
            }
        } catch (FileNotFoundException ex){
            ex.printStackTrace();
        } catch (NoSuchAlgorithmException ax){
            ax.printStackTrace();
        }

        return sb.toString();
        */
        /*
        MessageDigest sha1 = null;
        InputStream input = null;

        try {
            sha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex){
            ex.printStackTrace();
        }

        try {
            input = new FileInputStream(torrentPath);
            StringBuilder builder = new StringBuilder();
            while (!builder.toString().endsWith("4:info")) {
                builder.append((char) input.read()); // It's ASCII anyway.
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            for (int data; (data = input.read()) > -1; output.write(data));
            sha1.update(output.toByteArray(), 0, output.size() - 1);
        } catch (IOException ex){
            ex.printStackTrace();
        } finally {
            if (input != null) try { input.close(); } catch (IOException ignore) {}
        }

        byte[] hash = sha1.digest(); // Here's your hash. Do your thing with it.
        return hash;
        */
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
