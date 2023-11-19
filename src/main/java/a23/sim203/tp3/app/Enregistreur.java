package a23.sim203.tp3.app;

import a23.sim203.tp3.modele.Equation;
import a23.sim203.tp3.modele.MoteurCalcul;
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

    public void enregistreEquation(Collection<String> equations, Collection<String> variables, ArrayList<Constant> constantes, File fichier) {
        StringBuilder stringEnregistrer = new StringBuilder("Équations:\n");
        for (String equation : equations) {
            stringEnregistrer.append(equation).append("\n");
        }
        stringEnregistrer.append("Variables:\n");
        for (String variable : variables) {
            stringEnregistrer.append(variable).append("\n");
        }

        stringEnregistrer.append("Constantes:\n");
        for (Constant constante : constantes) {
            stringEnregistrer.append(constante.getConstantName()).append('=').append(constante.getConstantValue()).append("\n");
        }

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fichier));
            bufferedWriter.write(stringEnregistrer.toString());
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
                if (!lecture.equals("Variables:")) {
                    eCET.getEquations().put(lecture.split("=")[0], new Equation(lecture.split("=")[0], lecture.split("=")[1]));
                }
            }

            while (!lecture.equals("Constantes:")) {
                lecture = reader.readLine();
                if (!lecture.equals("Constantes:")) {
                    eCET.getVariables().put(lecture.split("=")[0], new Constant(lecture.split("=")[0], Double.parseDouble(lecture.split("=")[1])));
                }
            }
            while (lecture != null) {
                lecture = reader.readLine();
                if (lecture != null) {
                    eCET.getMapAncienneValeur().put(lecture.split("=")[0], new Constant(lecture.split("=")[0], Double.parseDouble(lecture.split("=")[1])));
                }
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