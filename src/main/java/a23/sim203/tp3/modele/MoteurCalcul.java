/**
 * Gère les équations et les variables pour effectuer des calculs avancés.
 * Utilise la bibliothèque mathématique mxparser pour l'évaluation des expressions.
 *
 * @Author Jayke Gagné, Antoine Houde, Émile Roy
 */
package a23.sim203.tp3.modele;

import a23.sim203.tp3.app.GestionAffichage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.mariuszgromada.math.mxparser.Constant;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.License;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoteurCalcul {

    // ajoutez les attributs pour stocker les équations et les variables
    private Long pasDeTempsEnCours;
    /**
     * Stock les variables avec leur nom et valeur associés.
     */
    private HashMap<String, Constant> constantMap;
    /**
     * Stock les équations avec leur nom et expression associés.
     */
    private HashMap<String, Equation> equationMap;
    /**
     * Stock les équations et variables utilisées dans le calcul.
     */
    private HashMap<String, Object> equationEtconstantMap;

    private HashMap<String, Constant> mapAncienneValeur;
    private HashMap<String, Constant> mapNouvelleValeur;
    private HashMap<String, Map<String, Constant>> historique;

    /**
     * Constructeur par défaut du moteur de calcul.
     * Initialise les maps et confirme l'utilisation non commerciale de la licence mxparser.
     */
    public MoteurCalcul() {
        License.iConfirmNonCommercialUse("Cegep Limoilou");
        constantMap = new HashMap<>();
        equationMap = new HashMap<>();
        equationEtconstantMap = new HashMap<>();
        mapAncienneValeur = new HashMap<>();
        mapNouvelleValeur = new HashMap<>();
        historique = new HashMap<>();
    }

    public long avancePasDeTemps() {
        historique = new HashMap<>((Map) mapAncienneValeur.values());
        long nouveauPasDeCalcul = 0;
        for (int i = 0; i < pasDeTempsEnCours.intValue(); i++) {
            nouveauPasDeCalcul = avancePasDeTemps() + i;
        }
        mapAncienneValeur.put(mapNouvelleValeur.keySet().toString(), (Constant) mapNouvelleValeur.values());
        mapNouvelleValeur.clear();
        return nouveauPasDeCalcul;
    }

    /**
     * Retourne l'ensemble des variables requises en tant que {@code Set<String>}.
     *
     * @return L'ensemble des variables requises.
     */
    private Set<String> determineToutesVariablesRequises() {
        return (Set<String>) getToutesLesVariables();
    }

    /**
     * Ajoute une variable à la carte des variables avec la valeur spécifiée.
     *
     * @param variable Le nom de la variable à ajouter.
     * @param valeur   La valeur de la variable à ajouter.
     */
    private void ajouteVariable(String variable, double valeur) {
        constantMap.put(variable, new Constant(variable, valeur));
    }

    /**
     * Permet de définir la valeur d'une variable existante.
     *
     * @param nomVariable Le nom de la variable à laquelle définir la valeur.
     * @param valeur      La nouvelle valeur de la variable.
     */
    public void setValeurVariable(String nomVariable, double valeur) {
        constantMap.put(nomVariable, new Constant(nomVariable, valeur));
    }

    /**
     * Ajoute une nouvelle équation au calculateur.
     *
     * <p>Cette méthode analyse la chaîne de caractères représentant l'équation, la transforme en objet Equation
     * et l'ajoute aux maps correspondantes. Si l'équation n'est pas récursive, elle est également ajoutée
     * à la map d'équations et de variables, les variables associées sont extraites et ajoutées à la carte des variables.
     * Si une variable avec le même nom que l'équation existe, elle est supprimée de la carte des variables.
     * Si l'équation est récursive, une alerte est affichée et l'équation n'est pas ajoutée.</p>
     *
     * @param nouvelleEquation La chaîne de caractères représentant la nouvelle équation.
     */
    public void ajouteEquation(String nouvelleEquation) {

            Equation equation = parseEquation(nouvelleEquation);
            equationMap.put(equation.getNom(), equation);
            if (!equationEstRecursive(equation.getNom())) {
                equationEtconstantMap.put(equation.getNom(), equation);
                addVariablesFromEquation(equation);
                mapAncienneValeur.put(equation.getNom(), new Constant(equation.getNom(), calcule(equation))); // ajoute variable dans la Map des variable
                constantMap.remove(equation.getNom()); // Supprime la variable existante avec le même nom
            } else {
                // Affiche une alerte en cas d'expression récursive

                equationMap.remove(equation.getNom());
                equationEtconstantMap.remove(equation.getNom());

            }
    }


    /**
     * Retire les variables inutiles du calculateur.
     * <p>
     * Cette méthode identifie les variables inutiles en calculant la différence entre l'ensemble de toutes les variables
     * et l'ensemble de tous les éléments requis. Les variables inutiles sont ensuite retirées des cartes des variables
     * et des équations et variables associées.
     * </p>
     * <p>
     * Remarque : Cette méthode peut avoir un impact sur les équations du calculateur en retirant les variables inutilisées.
     * </p>
     */

    public void retireVariablesInutiles() {
        Set<String> variablesInutiles = getAllVariables();
        variablesInutiles.removeAll(getAllElementsRequis());

        if (variablesInutiles.size() > 0) {
            Iterator<String> iterator = variablesInutiles.iterator();
            while (iterator.hasNext()) {
                String variableTemp = iterator.next();
                constantMap.remove(variableTemp);
                equationEtconstantMap.remove(variableTemp);
            }
        }
    }

    /**
     * Retourne l'ensemble de tous les éléments requis par les équations présentes dans le calculateur.
     * <p>
     * Cette méthode itère sur toutes les équations présentes dans la carte d'équations du calculateur
     * et récupère l'ensemble des éléments requis par chaque équation. Ces éléments requis sont ajoutés à un ensemble
     * qui est ensuite retourné.
     * </p>
     *
     * @return L'ensemble de tous les éléments requis par les équations du calculateur.
     */
    private Set<String> getAllElementsRequis() {
        Set<String> variablesRequises = new HashSet<>();
        Iterator<Equation> iterator = equationMap.values().iterator();
        while (iterator.hasNext()) {
            variablesRequises.addAll(iterator.next().getElementsRequis());
        }
        return variablesRequises;
    }

    /**
     * Analyse une chaîne de caractères représentant une équation et crée un objet Equation correspondant.
     * <p>
     * Cette méthode prend une chaîne de caractères représentant une équation et la divise en deux parties en utilisant
     * le signe égal (=) comme séparateur. Les deux parties sont utilisées pour créer un nouvel objet Equation,
     * qui est ensuite retourné.
     * </p>
     *
     * @param equationString La chaîne de caractères représentant l'équation à analyser.
     * @return L'objet Equation créé à partir de la chaîne de caractères.
     * @throws RuntimeException Si la chaîne de caractères ne peut pas être correctement analysée pour créer une équation.
     */
    public Equation parseEquation(String equationString) {
        String[] equationSplit = equationString.split("=");

        return new Equation(equationSplit[0], equationSplit[1]);
    }

    /**
     * Ajoute les variables nécessaires à l'équation à la carte des variables du calculateur.
     * <p>
     * Cette méthode prend une équation en paramètre, extrait l'ensemble des éléments requis de cette équation,
     * puis ajoute chaque variable nécessaire à la carte des variables. Si une variable n'est pas déjà présente
     * dans la carte d'équations et de variables, elle est ajoutée avec une valeur par défaut de Double.NaN.
     * </p>
     *
     * @param equation L'équation dont les variables nécessaires doivent être ajoutées à la carte des variables.
     */
    private void addVariablesFromEquation(Equation equation) {
        Set<String> elementsRequis = equation.getElementsRequis();
        Iterator<String> iterator = elementsRequis.iterator();
        while (iterator.hasNext()) {
            String nomVariable = iterator.next();
            if (!equationEtconstantMap.containsKey(nomVariable)) {
                ajouteVariable(nomVariable, Double.NaN);
                equationEtconstantMap.put(nomVariable, Double.NaN);
            }
        }
    }

    /**
     * Efface une équation du calculateur et met à jour les valeurs associées dans la carte des variables.
     * <p>
     * Cette méthode prend le nom d'une équation en paramètre, vérifie si l'équation existe dans la carte des équations,
     * puis la supprime. Ensuite, elle récupère l'expression associée à l'équation, crée un nouvel objet Expression avec
     * cette expression, et met à jour la valeur associée dans la carte des variables avec la nouvelle valeur NaN.
     * </p>
     *
     * @param nomEquation Le nom de l'équation à effacer.
     */
    public void effaceEquation(String nomEquation) {
        if (equationMap.containsKey(nomEquation)) {
            Equation equation = equationMap.get(nomEquation);
            Expression associatedExpression = new Expression(equation.getExpression());
            equationMap.remove(nomEquation);
            mapAncienneValeur.remove(nomEquation);
            mapNouvelleValeur.remove(nomEquation);

            // Met à jour l'expression dans constantMap avec la nouvelle valeur NaN
            constantMap.replace(associatedExpression.getExpressionString(), new Constant(associatedExpression.getExpressionString(), Double.NaN));
            retireVariablesInutiles();
        }
    }

    /**
     * Calcule le résultat d'une équation spécifiée par son nom.
     * <p>
     * Cette méthode prend le nom d'une équation en paramètre et utilise la méthode de calcul appropriée pour
     * déterminer le résultat. Si le nom d'équation a une longueur de 2 caractères, on suppose qu'il s'agit d'un nom
     * direct d'équation et on utilise la méthode de calcul directement. Sinon, on crée une nouvelle équation fictive
     * avec le nom "o9" et l'expression spécifiée, puis on utilise la méthode de calcul avec cette équation.
     * </p>
     *
     * @param nomEquation Le nom de l'équation pour laquelle calculer le résultat.
     * @return Le résultat du calcul de l'équation.
     */
    public double calcule(String nomEquation) {
        Double resultat;
        Pattern pattern = Pattern.compile("[a-z]");
        Matcher matcher = pattern.matcher(nomEquation);

        if (matcher.find()) resultat = calcule(equationMap.get(nomEquation));
        else resultat = calcule(new Equation("o9", nomEquation.replace(" ", "")));
        return resultat;
    }

    /**
     * Retourne l'ensemble de toutes les variables présentes dans le calculateur.
     *
     * @return L'ensemble de toutes les variables.
     */
    public Set<String> getAllVariables() {
        return constantMap.keySet();
    }

    /**
     * Retourne une collection de toutes les équations présentes dans le calculateur.
     *
     * @return La collection de toutes les équations.
     */

    public Collection<Equation> getAllEquations() {
        return equationMap.values();
    }

    /**
     * Retourne la carte des valeurs actuelles des variables du calculateur.
     *
     * @return La carte des valeurs des variables.
     */
    public HashMap<String, Constant> getVariableValues() {
        return constantMap;
    }

    /**
     * Retourne la carte des expressions de toutes les équations présentes dans le calculateur.
     *
     * @return La carte des expressions des équations.
     */
    public HashMap<String, Equation> getEquationExpressions() {
        return equationMap;
    }

    /**
     * Calcule le résultat d'une équation donnée en utilisant les valeurs actuelles des variables du calculateur.
     * <p>
     * Cette méthode prend une équation en paramètre, remplace toutes les occurrences d'autres équations dans son expression,
     * récupère les éléments requis de l'expression traitée, puis utilise les valeurs actuelles des variables pour
     * effectuer le calcul de l'équation.
     * </p>
     *
     * @param equation L'équation pour laquelle calculer le résultat.
     * @return Le résultat du calcul de l'équation.
     */
    public double calcule(Equation equation) {
        Double resultat;

        String expressionStringTemp = equation.getExpression();
        expressionStringTemp = remplacerEquations(expressionStringTemp);
        Set<String> elementsRequis = new Equation("a_", expressionStringTemp).getElementsRequis();
        ArrayList<Constant> constants = new ArrayList();

        for (String element : elementsRequis) {
            if (constantMap.containsKey(element))
                constants.add(constantMap.get(element));
        }

        for (int i = 0; i < constants.size(); i++) {
            expressionStringTemp = expressionStringTemp.replace(constants.get(i).getConstantName(), Double.toString(constants.get(i).getConstantValue()));
        }
        resultat = new Expression(expressionStringTemp).calculate();
        if (Objects.equals(equation.getNom(), mapAncienneValeur.toString())) {
            mapNouvelleValeur.put(resultat.toString(), constants.get(resultat.intValue()));
        }
        for (int i = 0; i < avancePasDeTemps(); i++) {
            mapNouvelleValeur = mapAncienneValeur;
        }
        return resultat;
    }

    /**
     * Remplace les occurrences des équations dans une expression par leurs expressions respectives.
     * <p>
     * Cette méthode prend une chaîne de caractères représentant une expression en paramètre,
     * remplace toutes les occurrences des noms d'équations présentes dans la carte des équations par
     * leurs expressions respectives enveloppées de parenthèses, et retourne la nouvelle expression.
     * </p>
     *
     * @param expressionStringTemp L'expression à traiter.
     * @return La nouvelle expression avec les équations remplacées.
     */
    private String remplacerEquations(String expressionStringTemp) {
        String equationDecompressee = "";
        Set<String> equations = equationMap.keySet();

        for (String nomEquationTemp : equations) {
            String equationUpdate = expressionStringTemp.replace(nomEquationTemp, '(' + equationMap.get(nomEquationTemp).getExpression() + ')');
            if (equationUpdate.length() > equationDecompressee.length()) equationDecompressee = equationUpdate;
        }
        if (equationDecompressee == "") equationDecompressee = expressionStringTemp;
        if (!equationDecompressee.equals(expressionStringTemp))
            equationDecompressee = remplacerEquations(equationDecompressee);
        return equationDecompressee;
    }

    /**
     * Retourne une collection de toutes les variables du calculateur avec leurs noms et valeurs.
     * <p>
     * Cette méthode itère sur toutes les constantes présentes dans la carte des variables du calculateur,
     * récupère le nom et la valeur de chaque constante, et les ajoute à une collection. La collection résultante
     * contient des chaînes de caractères au format "nomVariable = valeurVariable".
     * </p>
     *
     * @return La collection de toutes les variables avec leurs noms et valeurs.
     */
    public Collection<String> getToutesLesVariables() {
        HashSet<String> toutesLesVariables = new HashSet<String>();

        Iterator<Constant> iteratorValues = constantMap.values().iterator();
        while (iteratorValues.hasNext()) {
            Constant constantTemp = iteratorValues.next();
            toutesLesVariables.add(constantTemp.getConstantName() + " = " + constantTemp.getConstantValue());
        }

        return toutesLesVariables;
    }

    /**
     * Retourne une carte représentant les noms de variables associés à leurs valeurs actuelles dans le calculateur.
     *
     * @return La carte des noms de variables associés à leurs valeurs.
     */
    public Map<String, Constant> getVariableValueMap() {
        return constantMap;
    }

    /**
     * Retourne une carte représentant les noms d'équations associés à leurs expressions dans le calculateur.
     *
     * @return La carte des noms d'équations associés à leurs expressions.
     */
    public Map<String, Equation> getEquationMap() {
        return equationMap;
    }

    /**
     * Démontre l'utilisation d'expressions avec plusieurs variables.
     * <p>
     * Ce programme utilise la classe Expression pour créer deux expressions mathématiques avec plusieurs variables,
     * puis calcule et affiche les résultats.
     * </p>
     * <p>
     * Note : Ce programme assume l'utilisation d'une classe License pour la confirmation d'utilisation non commerciale.
     * </p>
     *
     * @param args Les arguments de ligne de commande (non utilisés dans cet exemple).
     */
    public static void main(String[] args) {

        // Comment utiliser les expressions avec plusieurs variables

        License.iConfirmNonCommercialUse("Cegep Limoilou");

        Constant A0 = new Constant("a0", 3);
        Constant B0 = new Constant("b0", 3);

        Expression e1 = new Expression("3+4+a0+b0", A0, B0);
        Expression e2 = new Expression("3+4+a0+b0", new Constant[]{A0, B0});// alternative avec tableau

        System.out.println("e1=" + e1.calculate());
        System.out.println("e2=" + e2.calculate());

    }

    /**
     * Vérifie si une équation est récursive.
     *
     * @param nomEquation Le nom de l'équation à vérifier.
     * @return true si l'équation est récursive, false sinon.
     */

    public boolean equationEstRecursive(String nomEquation) {
        boolean estRecursive = false;
        Equation equation = equationMap.get(nomEquation);
        Set<String> elementsRequis = equation.getElementsRequis();

        for (int i = 0; i < elementsRequis.size(); i++) {
            if (elementsRequis.contains(nomEquation)) {
                estRecursive = true;
                break;
            }
        }
        return estRecursive;
    }

    public HashMap<String, Constant> getConstanteValeurMap() {
        return constantMap;
    }

    public HashMap<String, Constant> getAncienneValeurVariableMap() {
        return mapAncienneValeur;
    }

    public HashMap<String, Constant> getNouvelleValeurVariableMap() {
        return mapNouvelleValeur;
    }

    public void setValeurConstante(String x, Double i) {
        new Constant(x, i);
    }

    public Long getPasDeTempsActuel() {
        return pasDeTempsEnCours;
    }

    public Collection<String> getAllEquationsString() {
        Collection<String> equationsString = new ArrayList<>();
        for (Equation equation :
                getAllEquations()) {
            equationsString.add(equation.toString());
        }
        return equationsString;
    }

    public void setEquationMap(HashMap<String, Equation> equationMap) {
        this.equationMap = equationMap;
    }

    public void setVariableMap(HashMap<String, Constant> variableMap) {
        this.constantMap = variableMap;
    }
    public void setValeurInitiale(String nom, Double x) {
        mapAncienneValeur.put(nom, new Constant("",x));
    }

}
