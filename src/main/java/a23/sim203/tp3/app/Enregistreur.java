package a23.sim203.tp3.app;

import a23.sim203.tp3.modele.Equation;
import a23.sim203.tp3.modele.MoteurCalcul;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.mariuszgromada.math.mxparser.Constant;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Enregistreur {

    MoteurCalcul moteurCalcul;
    GestionAffichage gestionAffichage;

    public Enregistreur(MoteurCalcul moteurCalcul, GestionAffichage gestionAffichage) {
        this.moteurCalcul = moteurCalcul;
        this.gestionAffichage = gestionAffichage;
    }

    public Enregistreur() {
    }

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

    public EquationsConstantesEtVariables chargeModele(File fichier) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(fichier));
        EquationsConstantesEtVariables eCET = new EquationsConstantesEtVariables();

        try {
            String lecture = reader.readLine();
            while (!lecture.equals("Variables:")) {
                lecture = reader.readLine();
                eCET.getEquations().put(lecture.split("=")[0], new Equation(lecture.split("=")[0], lecture.split("=")[1]));
            }
            reader.readLine();
            while (!lecture.equals("Constantes:")) {
                lecture = reader.readLine();
                eCET.getVariables().put(lecture.split("=")[0], new Constant(lecture.split("=")[0], Double.parseDouble(lecture.split("=")[1])));
            }
            while (lecture!=null) {
                lecture = reader.readLine();
                eCET.getMapAncienneValeur().put(lecture.split("=")[0], new Constant(lecture.split("=")[0], Double.parseDouble(lecture.split("=")[1])));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return eCET;
    }

    public class EquationsConstantesEtVariables {

        public EquationsConstantesEtVariables() {
            variableMap = new HashMap<>();
            equationMap = new HashMap<>();
            mapAncienneValeur = new HashMap<>();
        }

        private HashMap<String, Constant> variableMap;

        private HashMap<String, Equation> equationMap;

        private HashMap<String, Constant> mapAncienneValeur;

        public HashMap<String, Constant> getMapAncienneValeur() {
            return mapAncienneValeur;
        }

        public HashMap<String, Constant> getVariables() {
            return variableMap;
        }

        public HashMap<String, Equation> getEquations() {
            return equationMap;
        }
    }

//    public void addVariable()
}