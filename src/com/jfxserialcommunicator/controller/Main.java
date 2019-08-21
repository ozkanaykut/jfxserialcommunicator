package com.jfxserialcommunicator.controller;

import com.jfxserialcommunicator.core.IniHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.ini4j.Wini;

import java.io.File;
import java.util.Locale;

public class Main extends Application {

    private int windowWidth = 640 , windowHeight = 480;
    private String path;

    public Main() {
        path = System.getProperty("user.dir");

        try {
            //IniHelper iniHelper = new IniHelper();

            Wini ini = IniHelper.loadIni("config/Default.ini");

            windowWidth = ini.get("HomeController", "windowWidth", int.class);
            windowHeight = ini.get("HomeController", "windowHeight", int.class);

            //String lang = ini.get("Default","defaultLanguage", String.class);
            //String langCountry = ini.get("Default","defaultLangugeCountry", String.class);

            String lang = ini.get("Default","defaultLanguage");
            //Locale.setDefault(new Locale(lang, langCountry));
            Locale.setDefault(new Locale(lang));

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        // windows close after program exit
        Platform.setImplicitExit(true);

        Parent root = FXMLLoader.load(getClass().getResource("/com/jfxserialcommunicator/view/Home.fxml"));
        primaryStage.setTitle("JFX Serial Communicator App");
        primaryStage.setScene(new Scene(root, windowWidth, windowHeight));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
