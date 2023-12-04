package a23.sim203.tp3.app;

import a23.sim203.tp3.controller.CalculatriceController;
import a23.sim203.tp3.vue.Animations;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * La classe {@code SimulateurApp} représente l'application de simulateur, étendant la classe {@code Application}.
 * Elle initialise et lance l'interface graphique du simulateur.
 */
public class SimulateurApp extends Application {
    /**
     * Stage (scène) principal de l'application.
     */
    private Stage stage;
    /**
     * Gestion de l'affichage associée à l'instance de la classe.
     */
    GestionAffichage gestionAffichage;
    /**
     * Contrôleur de la calculatrice associé à l'instance de la classe.
     */
    CalculatriceController calculatriceController;
    /**
     * Instance de la classe Animations utilisée pour gérer les animations associée à l'instance de la classe.
     */
    Animations animations;

    /**
     * Méthode appelée lors du lancement de l'application.
     *
     * @param primaryStage Le stage principal de l'application.
     * @throws Exception En cas d'erreur lors du chargement de l'interface graphique.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("calculatriceV2.fxml"));
        Node root = fxmlLoader.load();
        Scene scene = new Scene((Parent) root);
        primaryStage.setScene(scene);

        calculatriceController = fxmlLoader.getController();
        gestionAffichage = calculatriceController.getGestionAffichage();
        gestionAffichage.setStage(primaryStage);
        primaryStage.show();
    }
}