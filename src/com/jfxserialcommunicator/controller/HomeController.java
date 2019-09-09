package com.jfxserialcommunicator.controller;

import com.fazecast.jSerialComm.SerialPort;
import com.jfxserialcommunicator.core.IniHelper;
import com.jfxserialcommunicator.core.SerialPortEvent_DataListener;
import com.jfxserialcommunicator.core.SerialPortHelper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.control.StatusBar;
import org.ini4j.Wini;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Locale;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    SerialPortHelper serialPortHelper;

    @FXML
    private AnchorPane root;

    @FXML
    private MenuItem menuItemTurkce;

    @FXML
    private MenuItem menuItemIngilizce;

    @FXML
    private MenuItem menuItemClose;

    @FXML
    private ComboBox<String> cbPort;

    @FXML
    private ComboBox<String> cbBaund;

    @FXML
    private ComboBox<String> cbParity;

    @FXML
    private ComboBox<String> cbDataBits;

    @FXML
    private ComboBox<String> cbStopBits;

    @FXML
    private Button btnConnect;

    @FXML
    private TextArea txtConsole;

    @FXML
    private StatusBar statusBar;

    private Label statusBarStatusLabel;
    private Label statusBarStatusDetailLabel;
    private Button statusBarClearButton;

    @FXML
    private Label lblPort;

    @FXML
    private Label lblBaund;

    @FXML
    private Label lblParity;

    @FXML
    private Label lblDataBits;

    @FXML
    private Label lblStopBits;

    private Wini ini;

    private Wini iniDefault;

    private Stage myStage;
    public void setStage(Stage stage) {
        myStage = stage;
    }

    //IniHelper iniHelper = new IniHelper();

    void AlertWindow(String title, String headerText, String contentText) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        //alert.setContentText("Invalid or Incorrect SerialPort Selection!");

        alert.showAndWait();
    }

    @FXML
    public void changeLang(ActionEvent event) {
        try {
            MenuItem selectLangMenu = (MenuItem)event.getSource();
            if(selectLangMenu.getId().equals("menuItemTurkish")){
                Locale.setDefault(new Locale("tr","TR"));
                iniDefault.put("Default","defaultLanguage", Locale.getDefault().toString());

                System.out.println("Selected language is Turkish");
            }
            else if(selectLangMenu.getId().equals("menuItemEnglish")){
                Locale.setDefault(new Locale("en","US"));
                iniDefault.put("Default","defaultLanguage", Locale.getDefault().toString());

                System.out.println("Selected language is English");
            }
            else if(selectLangMenu.getId().equals("menuItemRussian")){
                Locale.setDefault(new Locale("ru","RU"));
                iniDefault.put("Default","defaultLanguage", Locale.getDefault().toString());

                System.out.println("Selected language is Turkish");
            }
            else{
                return;
            }

            // default ini save after changes
            IniHelper.saveIni(iniDefault);

            // language ini reload after locale changes
            ini = IniHelper.loadLangIni(Locale.getDefault().toString());

            // ui elements reload after language and locale changes
            InitUiLoadingWithLanguageSupport();

        }catch (InvalidPropertiesFormatException e) {
            System.out.println("Invalid file format.");
        } catch (IOException e) {
            System.out.println("Problem reading file.");
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void btnConnection_Click(ActionEvent event) {
        try {

            if(!serialPortHelper.IsOpen()){
                if(cbPort.getItems().size() > 0 && cbBaund.getItems().size() > 0 &&
                        cbParity.getItems().size() > 0 &&
                        cbDataBits.getItems().size()>0 &&
                        cbStopBits.getItems().size()>0){

                    serialPortHelper.setPortName(cbPort.getSelectionModel().getSelectedItem().trim());
                    serialPortHelper.setPortBaund(Integer.parseInt(cbBaund.getSelectionModel().getSelectedItem().trim()));
                    int _parity = iniDefault.get("HomeController", "parity_"+ cbParity.getSelectionModel().getSelectedItem().toString(), int.class);
                    serialPortHelper.setPortParity(_parity);
                    serialPortHelper.setPortDataBits(Integer.parseInt(cbDataBits.getSelectionModel().getSelectedItem().trim()));
                    serialPortHelper.setPortStopBits(Integer.parseInt(cbStopBits.getSelectionModel().getSelectedItem().trim()));

                    SerialPortEvent_DataListener listen = new SerialPortEvent_DataListener(txtConsole);

                    try {
                        serialPortHelper.PortOpen();
                        serialPortHelper.getPort().addDataListener(listen);

                        //btnConnect.setText(ini.get("HomePage","button_disconnect"));
                        InitUiLoadingWithLanguageSupport();
                    }catch (Exception e) { e.printStackTrace(); }

                }
                else{
                    AlertWindow("Warning!", null, ini.get("Alert","alert_incompleteOrIncorrect"));
                }
            }
            else{
                serialPortHelper.PortClose();
                //btnConnect.setText(ini.get("HomePage","button_connect"));
                InitUiLoadingWithLanguageSupport();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void closeApplication(ActionEvent event) {
        serialPortHelper.PortClose();
        Platform.exit();
        System.exit(0);
    }

    private void InitUI(){

        root.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                // scene is set for the first time. Now its the time to listen stage changes.
                newScene.windowProperty().addListener((observableWindow, oldWindow, newWindow) -> {
                    if (oldWindow == null && newWindow != null) {
                        // stage is set. now is the right time to do whatever we need to the stage in the controller.
                        ((Stage) newWindow).setOnCloseRequest(new EventHandler<WindowEvent>() {
                            public void handle(WindowEvent we) {
                                System.out.println("Stage is closing");
                                serialPortHelper.PortClose();
                            }
                        });
                    }
                });
            }
        });

        statusBar.setText(null);
        statusBarStatusLabel = new Label(ini.get("HomePage","statusbar_statuslabel"));
        statusBarStatusLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5px 0 0 0;");

        statusBarClearButton = new Button(ini.get("HomePage","statusbar_buttonclear"));
        statusBarClearButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override public void handle(ActionEvent e) {
                txtConsole.clear();
            }
        });

        //statusBarStatusDetailLabel = new Label(ini.get("HomePage","statusbar_statusdetaillabel"));
        if(serialPortHelper.IsOpen())
            statusBarStatusDetailLabel = new Label(ini.get("HomePage","button_disconnect"));
        else
            statusBarStatusDetailLabel = new Label(ini.get("HomePage","button_disconnect"));

        statusBarStatusDetailLabel.setStyle("-fx-padding: 5px 0 0 0;");

        Label _splitChar = new Label(" | ");
        _splitChar.setStyle("-fx-padding: 5px 0 0 0;");

        statusBar.getLeftItems().add(0,statusBarStatusLabel);
        statusBar.getLeftItems().add(1, _splitChar);
        statusBar.getLeftItems().add(2, statusBarStatusDetailLabel);

        statusBar.getRightItems().add(0, statusBarClearButton);

        String[] baund = iniDefault.get("HomeController","baund", String.class).split(",");
        String[] parity = iniDefault.get("HomeController","parity", String.class).split(",");
        String[] databits = iniDefault.get("HomeController","databits", String.class).split(",");
        String[] stopbits = iniDefault.get("HomeController","stopbits", String.class).split(",");

        System.out.println("Baund List : " + baund.length);
        System.out.println("Parity List : " + parity.length);
        System.out.println("Databits List : " + databits.length);
        System.out.println("StopBits List : " + stopbits.length);

        cbBaund.getItems().addAll(baund);
        cbParity.getItems().addAll(parity);
        cbDataBits.getItems().addAll(databits);
        cbStopBits.getItems().addAll(stopbits);


        cbBaund.getSelectionModel().select(0);
        cbParity.getSelectionModel().select(0);
        cbDataBits.getSelectionModel().select(0);
        cbStopBits.getSelectionModel().select(0);

    }

	private void InitUiLoadingWithLanguageSupport() {

        lblPort.setText(ini.get("HomePage","label_port"));
        lblBaund.setText(ini.get("HomePage","label_baund"));
        lblParity.setText(ini.get("HomePage","label_parity"));
        lblDataBits.setText(ini.get("HomePage","label_databits"));
        lblStopBits.setText(ini.get("HomePage","label_stopbits"));

        if(serialPortHelper.IsOpen())
            btnConnect.setText(ini.get("HomePage","button_disconnect"));
        else
            btnConnect.setText(ini.get("HomePage","button_connect"));


        statusBarStatusLabel.setText(ini.get("HomePage","statusbar_statuslabel"));
        //statusBarStatusDetailLabel.setText(ini.get("HomePage","statusbar_statusdetaillabel"));
        if(serialPortHelper.IsOpen())
            statusBarStatusDetailLabel.setText(ini.get("HomePage","button_disconnect"));
        else
            statusBarStatusDetailLabel.setText(ini.get("HomePage","button_connect"));

        statusBarClearButton.setText(ini.get("HomePage","statusbar_buttonclear"));
	}

	private void loadLang(){
        try {
            ini = IniHelper.loadLangIni(Locale.getDefault().toString());
            iniDefault = IniHelper.loadIni("config/Default.ini");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshPortList(){
        HashMap<String, SerialPort> portList = serialPortHelper.PortList(true);

        if(portList != null && portList.size()>0){
            //portList.forEach((k,v) -> System.out.println("Port Adı" + k.toString()));
            /*for (Map.Entry<String, SerialPort> port: portList.entrySet()) {
                cbPort.getItems().add(port.getKey());
            }*/
            portList.forEach((k,v) -> {
                    cbPort.getItems().add(k);
                }
            );

            cbPort.getSelectionModel().select(0);
        }
        else
            System.out.println("Port bilgisi bulunamadı");
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/*bundle = ResourceBundle
	            .getBundle("lang", new Locale("tr", "TR"));*/
        Platform.setImplicitExit(true);

        //iniDefault = new Wini();

        serialPortHelper = new SerialPortHelper();

        // language loading
        loadLang();
        InitUI();
        InitUiLoadingWithLanguageSupport();

        refreshPortList();
		
	}

}
