package a23.sim203.tp3.app;

import a23.sim203.tp3.controller.CalculatriceController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SimulateurApp extends Application {
    GestionAffichage gestionAffichage;
    CalculatriceController calculatriceController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(SimulateurApp.class.getResource("calculatrice.fxml"));
        Node root = fxmlLoader.load();
        Scene scene = new Scene((Parent) root);
        primaryStage.setScene(scene);

        calculatriceController = fxmlLoader.getController();
        gestionAffichage = calculatriceController.getGestionAffichage();
        gestionAffichage.setCalculatriceController(calculatriceController);

        primaryStage.show();
    }
}