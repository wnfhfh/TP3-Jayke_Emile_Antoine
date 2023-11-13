package a23.sim203.tp3.app;

import a23.sim203.tp3.modele.Equation;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class Enregistreur {

    public void enregistreEquation(Collection<String> equations, Collection<String> variables, ArrayList constantes, File fichier) {
        String stringEnregistrer = "Équations:\n";
        for (String equation : equations) {
            stringEnregistrer += equation + "\n";
        }
        stringEnregistrer += "Variables:\n";
        for (String variable : variables) {
            stringEnregistrer += variable + "\n";
        }

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fichier));
            bufferedWriter.write(stringEnregistrer);
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
