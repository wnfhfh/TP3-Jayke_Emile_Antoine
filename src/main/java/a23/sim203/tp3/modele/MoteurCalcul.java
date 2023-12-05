/**
 * Gère les équations et les variables pour effectuer des calculs avancés.
 * Utilise la bibliothèque mathématique mxparser pour l'évaluation des expressions.
 *
 * @Author Jayke Gagné, Antoine Houde, Émile Roy
 */
package a23.sim203.tp3.modele;

import org.mariuszgromada.math.mxparser.Constant;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.License;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoteurCalcul {

    // ajoutez les attributs pour stocker les équations et les variables
    private Long pasDeTempsActuel;
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
    private HashMap<Long, Map<String, Constant>> historique;

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
        pasDeTempsActuel = 1l;
    }

    /**
     * Avance le pas de temps actuel et enregistre l'état actuel dans l'historique.
     * Met à jour les maps d'anciennes et nouvelles valeurs.
     *
     * @return Le nouveau pas de temps actuel après l'avancement.
     */
    public long avancePasDeTemps() {
        historique.put(pasDeTempsActuel, faireMapHistorique());
        pasDeTempsActuel++;
        mapAncienneValeur.putAll(mapNouvelleValeur);
        mapNouvelleValeur.clear();
        return pasDeTempsActuel;
    }

    /**
     * Crée et retourne une nouvelle map contenant l'historique des constantes
     * avec les valeurs actuelles et les constantes initiales.
     *
     * @return Une map contenant l'historique des constantes.
     */
    private Map<String, Constant> faireMapHistorique() {
        HashMap mapAAjouter = new HashMap<String, Constant>();

        mapAAjouter.putAll(mapAncienneValeur);
        mapAAjouter.putAll(constantMap);
        return mapAAjouter;
    }

    /**
     * Retourne l'ensemble des variables requises en tant que {@code Set<String>}.
     *
     * @return L'ensemble des variables requises.
     */
    private Set<String> determineToutesVariablesRequises() {
        return (Set<String>) getToutesLesConstantesString();
    }

    /**
     * Ajoute une variable à la carte des variables avec la valeur spécifiée.
     *
     * @param constante Le nom de la variable à ajouter.
     * @param valeur    La valeur de la variable à ajouter.
     */
    private void ajouteConstante(String constante, double valeur) {
        constantMap.remove(constante);
        constantMap.put(constante, new Constant(constante, valeur));
    }

    /**
     * Permet de définir la valeur d'une variable existante.
     *
     * @param nomVariable Le nom de la variable à laquelle définir la valeur.
     * @param valeur      La nouvelle valeur de la variable.
     */
    public void setValeurConstante(String nomVariable, double valeur) {
        constantMap.remove(nomVariable);
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
            addConstantesFromEquation(equation);
            constantMap.remove(equation.getNom()); // Supprime la variable existante avec le même nom
        }
        mapAncienneValeur.put(equation.getNom(), new Constant(equation.getNom(), Double.NaN)); // TODO figure out par quoi le remplacer
        mapNouvelleValeur.put(equation.getNom(), new Constant(equation.getNom(), calcule(equation)));
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

    public void retireConstantesInutiles() { //todo verif s'il faut retirer variables of const inutiles
        Set<String> variablesInutiles = getAllConstantes();
        variablesInutiles.removeAll(getAllequationsRequises());

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
    private Set<String> getAllequationsRequises() {
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
    private void addConstantesFromEquation(Equation equation) {
        Set<String> equationsRequises = equation.getElementsRequis();
        Iterator<String> iterator = equationsRequises.iterator();
        while (iterator.hasNext()) {
            String nomVariable = iterator.next();
            if (!equationMap.containsKey(nomVariable) && !constantMap.containsKey(nomVariable) && nomVariable != equation.getNom()) {
                ajouteConstante(nomVariable, Double.NaN);
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
            retireConstantesInutiles();
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
        Pattern pattern = Pattern.compile("[a-zA-Z]");
        Matcher matcher = pattern.matcher(nomEquation);

        if (matcher.find()) {
            resultat = calcule(equationMap.get(nomEquation));
            mapNouvelleValeur.put(nomEquation, new Constant(nomEquation, resultat));
        } else resultat = calcule(new Equation("o_", nomEquation.replace(" ", "")));
        return resultat;
    }

    /**
     * Retourne l'ensemble de toutes les variables présentes dans le calculateur.
     *
     * @return L'ensemble de toutes les variables.
     */
    public Set<String> getAllConstantes() {
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

        String expressionStringTemp = remplacerEquations(equation, equation.getExpression(), new HashSet<>());
        Set<String> equationsRequises = new Equation("a_", expressionStringTemp).getElementsRequis();
        ArrayList<Constant> constants = new ArrayList();

        for (String element : equationsRequises) {
            if (constantMap.containsKey(element))
                constants.add(constantMap.get(element));
        }

        for (int i = 0; i < constants.size(); i++) {
            expressionStringTemp = expressionStringTemp.replace(constants.get(i).getConstantName(), Double.toString(constants.get(i).getConstantValue()));
        }
        resultat = new Expression(expressionStringTemp).calculate();

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
     * @param equation L'equation à traiter.
     * @return La nouvelle expression avec les équations remplacées.
     */
    private String remplacerEquations(Equation equation, String expressionAvant, Set<String> equationsRequisesRecursifsRemplaces) {
        String expressionApres = expressionAvant;
        Set<String> equationsRequises = getEquationsDansExpression(equation.getElementsRequis());
        Set<String> equationsRequisesRecursifs = getEquationsRequisesRecursifs(equation);


        for (String element :
                equationsRequises) {
            if (expressionApres.contains(element)) {
                if (equationsRequisesRecursifs.contains(element)) {
                    if (!(equationsRequisesRecursifsRemplaces.contains(element))) {
                        equationsRequisesRecursifsRemplaces.add(element);
                    }
                    expressionApres = expressionApres.replace(element, Double.toString(mapAncienneValeur.get(element).getConstantValue()));
                } else {
                    expressionApres = expressionApres.replace(element, '(' + equationMap.get(element).getExpression() + ')');
                }
            }
        }

        if (!expressionApres.equals(expressionAvant)) {
            return remplacerEquations(equation, expressionApres, equationsRequisesRecursifsRemplaces);
        } else {
            return expressionApres;
        }
    }

    /**
     * Obtient l'ensemble des équations présentes parmi les éléments requis.
     *
     * @param elementsRequis L'ensemble des éléments requis.
     * @return L'ensemble des équations présentes dans les éléments requis.
     */
    private Set<String> getEquationsDansExpression(Set<String> elementsRequis) {
        Set<String> equationsDansExpression = new HashSet<>();

        for (String element :
                elementsRequis) {
            if (equationMap.containsKey(element)) equationsDansExpression.add(element);
        }
        return equationsDansExpression;
    }

    /**
     * Obtient de manière récursive l'ensemble des équations requises par une équation donnée.
     *
     * @param equation L'équation pour laquelle obtenir les équations requises.
     * @return L'ensemble des équations requises de manière récursive.
     */
    private Set<String> getEquationsRequisesRecursifs(Equation equation) {
        Set<String> equationsRequises = equation.getElementsRequis();
        Set<String> equationsRequisesRecursifs = new HashSet<>();

        for (String element :
                equationsRequises) {
            if (equationMap.containsKey(element)) {
                Equation equationAVerif = equationMap.get(element);
                if (equationAVerif.getExpression().contains(element)) equationsRequisesRecursifs.add(element);
            }
        }
        return equationsRequisesRecursifs;
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
    public Collection<String> getToutesLesConstantesString() {
        HashSet<String> toutesLesConstantes = new HashSet<String>();

        Iterator<Constant> iteratorValues = constantMap.values().iterator();
        while (iteratorValues.hasNext()) {
            Constant constantTemp = iteratorValues.next();
            toutesLesConstantes.add(constantTemp.getConstantName() + " = " + constantTemp.getConstantValue());
        }

        return toutesLesConstantes;
    }

    /**
     * Obtient une collection de toutes les variables sous forme de chaînes de caractères,
     * avec leur nom et valeur actuelle.
     *
     * @return Une collection de chaînes représentant toutes les variables.
     */
    public Collection<String> getToutesLesVariablesString() {
        HashSet<String> toutesLesVariables = new HashSet<String>();

        Iterator<Constant> iteratorValues = mapAncienneValeur.values().iterator();
        while (iteratorValues.hasNext()) {
            Constant variableTemp = iteratorValues.next();
            toutesLesVariables.add(variableTemp.getConstantName() + " = " + variableTemp.getConstantValue());
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
        Set<String> equationsRequises = equation.getElementsRequis();

        for (int i = 0; i < equationsRequises.size(); i++) {
            if (equationsRequises.contains(nomEquation)) {
                estRecursive = true;
                break;
            }
        }
        return estRecursive;
    }

    /**
     * Obtient une map contenant les constantes avec leur nom comme clé.
     *
     * @return Une map des constantes avec leur nom comme clé.
     */
    public HashMap<String, Constant> getConstanteValeurMap() {
        return constantMap;
    }

    /**
     * Obtient une map contenant les anciennes valeurs des variables avec leur nom comme clé.
     *
     * @return Une map des anciennes valeurs des variables avec leur nom comme clé.
     */
    public HashMap<String, Constant> getAncienneValeurVariableMap() {
        return mapAncienneValeur;
    }

    /**
     * Obtient une map contenant les nouvelles valeurs des variables avec leur nom comme clé.
     *
     * @return Une map des nouvelles valeurs des variables avec leur nom comme clé.
     */
    public HashMap<String, Constant> getNouvelleValeurVariableMap() {
        return mapNouvelleValeur;
    }

    /**
     * Obtient le pas de temps actuel.
     *
     * @return Le pas de temps actuel.
     */
    public Long getPasDeTempsActuel() {
        return pasDeTempsActuel;
    }

    /**
     * Obtient le pas de temps actuel moins un.
     *
     * @return Le pas de temps actuel moins un.
     */
    public Long getPasDeTempsActuelMoinsUn() {
        return (pasDeTempsActuel - 1);
    }

    /**
     * Obtient une collection de chaînes représentant toutes les équations.
     *
     * @return Une collection de chaînes représentant toutes les équations.
     */
    public Collection<String> getAllEquationsString() {
        Collection<String> equationsString = new ArrayList<>();
        for (Equation equation :
                getAllEquations()) {
            equationsString.add(equation.toString());
        }
        return equationsString;
    }

    /**
     * Définit la map des équations de l'objet EquationSolver.
     *
     * @param equationMap La nouvelle map des équations.
     */
    public void setEquationMap(HashMap<String, Equation> equationMap) {
        this.equationMap = equationMap;
    }

    /**
     * Définit la map des constantes de l'objet EquationSolver.
     *
     * @param constantMap La nouvelle map des constantes.
     */
    public void setConstantMap(HashMap<String, Constant> constantMap) {
        this.constantMap = constantMap;
    }

    /**
     * Définit la map des anciennes valeurs de l'objet EquationSolver.
     *
     * @param mapAncienneValeur La nouvelle map des anciennes valeurs.
     */
    public void setMapAncienneValeur(HashMap<String, Constant> mapAncienneValeur) {
        this.mapAncienneValeur = mapAncienneValeur;
    }

    /**
     * Définit la valeur d'une constante dans la map des constantes.
     *
     * @param nom Le nom de la constante.
     * @param x   La nouvelle valeur de la constante.
     */
    public void setValeurConstante(String nom, Double x) {
        constantMap.put(nom, new Constant(nom, x));
    }

    /**
     * Définit la valeur initiale d'une constante dans la map des anciennes valeurs.
     *
     * @param nom Le nom de la constante.
     * @param x   La nouvelle valeur initiale de la constante.
     */
    public void setValeurInitiale(String nom, Double x) {
        mapAncienneValeur.put(nom, new Constant(nom, x));
    }

    /**
     * Calcule la variable "sim_" en additionnant toutes les équations présentes dans la map des équations.
     * La valeur résultante est ensuite calculée et mise à jour dans les maps des anciennes et nouvelles valeurs.
     */
    public void calculeSim() {
        StringBuilder expressionSim = new StringBuilder("");
        for (Equation equation :
                equationMap.values()) {
            expressionSim.append(equation.getNom() + "+");
        }
        equationMap.put("sim_", new Equation("sim_", expressionSim.substring(0, expressionSim.length() - 1)));
        mapAncienneValeur.put("sim_", new Constant("sim_", 0));
        calcule("sim_");
        mapNouvelleValeur.put("sim_", new Constant("sim_", calcule("sim_")));
    }

    /**
     * Actualise les valeurs des équations dans la map des nouvelles valeurs.
     */
    public void refreshEquations() {
        for (Equation equation :
                equationMap.values()) {
            mapNouvelleValeur.remove(equation.getNom());
            mapNouvelleValeur.put(equation.getNom(), new Constant(equation.getNom(), calcule(equation)));
        }
    }

    public HashMap<String, Double> getDataTableau() {
        HashMap<String, Double> dataTableau = new HashMap<>();

        for (String s :
                mapAncienneValeur.keySet()) {
            dataTableau.put(s, mapAncienneValeur.get(s).getConstantValue());
        }
        for (String s :
                constantMap.keySet()) {
            dataTableau.put(s, constantMap.get(s).getConstantValue());
        }

        return dataTableau;
    }

    /**
     * Obtient l'historique des valeurs des constantes à différents pas de temps.
     *
     * @return Une map contenant l'historique des valeurs des constantes à différents pas de temps.
     */
    public HashMap<Long, Map<String, Constant>> getHistorique() {
        return historique;
    }
}