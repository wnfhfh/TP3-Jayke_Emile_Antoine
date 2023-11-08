package a23.sim203.tp3.app;


import a23.sim203.tp3.controller.CalculatriceController;
import a23.sim203.tp3.modele.MoteurCalcul;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class GestionAffichage {
    MoteurCalcul moteurCalcul;
    String stringAffiche;
    CalculatriceController calculatriceController;

    public GestionAffichage() {
        moteurCalcul = new MoteurCalcul();
        stringAffiche = "";
    }

    public void setBoutonCaractere(char caractere, Button bouton) {
        bouton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                stringAffiche += caractere;
                calculatriceController.setStringAffiche(stringAffiche);
            }
        });
    }

    public void setCalculatriceController(CalculatriceController calculatriceController) {
        this.calculatriceController = calculatriceController;
    }

    public void actionBoutonEffacer(Button bouton) {
        bouton.setOnAction(event -> {
            stringAffiche = "";
            calculatriceController.setStringAffiche(stringAffiche);
        });
    }

    public void actionBoutonReculer(Button bouton) {
        bouton.setOnAction(event -> {
            stringAffiche = stringAffiche.substring(0, stringAffiche.length() - 1);
            calculatriceController.setStringAffiche(stringAffiche);
        });
    }

    public void actionBoutonPlusMinus(Button bouton) {
        bouton.setOnAction(event -> {
            if (stringAffiche.charAt(0) == '-') {
                stringAffiche = stringAffiche.substring(1);
                calculatriceController.setStringAffiche(stringAffiche);
            } else {
                stringAffiche = "-" + stringAffiche;
                calculatriceController.setStringAffiche(stringAffiche);
            }
        });
    }

    public void actionBoutonEgal(Button bouton) {
        bouton.setOnAction(event -> {


//            créerAlerteBoutonÉgal();          Va devoir être mise dans un if
        });
    }

    public void créerAlerteBoutonÉgal() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Expression invalide");
        alert.setTitle("Calculateur avancée");
        alert.setContentText("L'expression saisie ne peut être calculée");
        alert.showAndWait();
    }

}
