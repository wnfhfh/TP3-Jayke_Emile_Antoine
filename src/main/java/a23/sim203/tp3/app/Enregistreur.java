package a23.sim203.tp3.app;

import a23.sim203.tp3.modele.Equation;
import a23.sim203.tp3.modele.MoteurCalcul;
import org.mariuszgromada.math.mxparser.Constant;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * La classe Enregistreur est utilisée pour gérer l'enregistrement et le chargement de données
 * liées au moteur de calcul et à l'affichage dans une application de calculatrice avancée.
 */
public class Enregistreur {
    /**
     * Moteur de calcul associé à l'instance de la classe Enregistreur.
     */
    MoteurCalcul moteurCalcul;
    /**
     * Moteur de calcul associé à l'instance de la classe Enregistreur.
     */
    GestionAffichage gestionAffichage;
    /**
     * Constructeur permettant d'initialiser un Enregistreur avec un moteur de calcul et un gestionnaire d'affichage.
     *
     * @param moteurCalcul    Le moteur de calcul à associer à l'enregistreur.
     * @param gestionAffichage Le gestionnaire d'affichage à associer à l'enregistreur.
     */
    public Enregistreur(MoteurCalcul moteurCalcul, GestionAffichage gestionAffichage) {
        this.moteurCalcul = moteurCalcul;
        this.gestionAffichage = gestionAffichage;
    }
    /**
     * Constructeur par défaut de la classe Enregistreur.
     */
    public Enregistreur() {
    }
    /**
     * Enregistre les équations, variables et constantes dans un fichier spécifié.
     *
     * @param equations   Collection de chaînes représentant les équations à enregistrer.
     * @param variables   Collection de chaînes représentant les variables à enregistrer.
     * @param constantes  Liste d'objets Constant représentant les constantes à enregistrer.
     * @param fichier     Le fichier dans lequel enregistrer les données.
     */
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
    /**
     * Charge un modèle de données, comprenant des équations, variables et constantes, à partir d'un fichier spécifié.
     *
     * @param fichier Le fichier à partir duquel charger le modèle.
     * @return Une instance d'EquationsConstantesEtVariables contenant les équations, variables et constantes chargées.
     * @throws FileNotFoundException Si le fichier spécifié n'est pas trouvé.
     */
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
                    eCET.getMapAncienneValeur().put(lecture.split("=")[0], new Constant(lecture.split("=")[0], Double.parseDouble(lecture.split("=")[1])));
                }
            }
            while (lecture != null) {
                lecture = reader.readLine();
                if (lecture != null) {
                    eCET.getVariables().put(lecture.split("=")[0], new Constant(lecture.split("=")[0], Double.parseDouble(lecture.split("=")[1])));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return eCET;
    }
    /**
     * La classe {@code EquationsConstantesEtVariables} représente un conteneur pour stocker des équations,
     * des variables et des constantes utilisées dans une application de calculatrice avancée.
     */
    public class EquationsConstantesEtVariables {
        /**
         * Map associant le nom des variables à des objets Constant.
         */
        public EquationsConstantesEtVariables() {
            variableMap = new HashMap<>();
            equationMap = new HashMap<>();
            mapAncienneValeur = new HashMap<>();
        }
        /**
         * Map associant le nom des équations à des objets Equation.
         */
        private HashMap<String, Constant> variableMap;
        /**
         * Map associant le nom des constantes à des objets Constant, représentant les valeurs précédentes.
         */
        private HashMap<String, Equation> equationMap;
        /**
         * Obtient la map associant le nom des constantes à des objets Constant, représentant les valeurs précédentes.
         *
         * @return La map des constantes avec les valeurs précédentes.
         */
        private HashMap<String, Constant> mapAncienneValeur;
        /**
         * Obtient la map associant le nom des constantes à des objets Constant, représentant les valeurs précédentes.
         *
         * @return La map des constantes avec les valeurs précédentes.
         */
        public HashMap<String, Constant> getMapAncienneValeur() {
            return mapAncienneValeur;
        }
        /**
         * Obtient la map associant le nom des variables à des objets Constant.
         *
         * @return La map des variables.
         */
        public HashMap<String, Constant> getVariables() {
            return variableMap;
        }
        /**
         * Obtient la map associant le nom des équations à des objets Equation.
         *
         * @return La map des équations.
         */
        public HashMap<String, Equation> getEquations() {
            return equationMap;
        }
    }

//    public void addVariable()
}