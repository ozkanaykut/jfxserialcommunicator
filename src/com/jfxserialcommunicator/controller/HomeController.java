package com.jfxserialcommunicator.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.InvalidPropertiesFormatException;
import java.util.Locale;
import java.util.ResourceBundle;

import com.jfxserialcommunicator.core.IniHelper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.controlsfx.control.StatusBar;
import org.ini4j.Wini;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class HomeController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private MenuItem menuItemTurkce;

    @FXML
    private MenuItem menuItemIngilizce;

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

    //IniHelper iniHelper = new IniHelper();

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

    private void InitUI(){
        statusBar.setText(null);
        statusBarStatusLabel = new Label(ini.get("HomePage","statusbar_statuslabel"));
        statusBarStatusLabel.setStyle("-fx-font-weight: bold");
        statusBarStatusDetailLabel = new Label(ini.get("HomePage","statusbar_statusdetaillabel"));
        statusBar.getLeftItems().add(0,statusBarStatusLabel);
        statusBar.getLeftItems().add(1, new Label (" | "));
        statusBar.getLeftItems().add(2, statusBarStatusDetailLabel);

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

        btnConnect.setText(ini.get("HomePage","button_connect"));


        statusBarStatusLabel.setText(ini.get("HomePage","statusbar_statuslabel"));
        statusBarStatusDetailLabel.setText(ini.get("HomePage","statusbar_statusdetaillabel"));

	}

	private void loadLang(){
        try {
            ini = IniHelper.loadLangIni(Locale.getDefault().toString());
            iniDefault = IniHelper.loadIni("config/Default.ini");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/*bundle = ResourceBundle
	            .getBundle("lang", new Locale("tr", "TR"));*/
        Platform.setImplicitExit(true);

        //iniDefault = new Wini();

        // language loading
        loadLang();

        InitUI();
        InitUiLoadingWithLanguageSupport();
		
	}

}
