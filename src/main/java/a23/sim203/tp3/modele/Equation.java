package a23.sim203.tp3.modele;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Représente une équation mathématique avec un nom, une expression et les éléments requis.
 * Permet de créer, valider et manipuler des équations mathématiques.
 * <p>
 * Exemple d'utilisation :
 * <p>
 * {@code
 * Equation equation = new Equation("Equation1", "x + y = z");
 * System.out.println(equation.toString());
 * }
 */
public class Equation {
    private String nom;
    private Set<String> elementsRequis;
    private String expression;

    /**
     * Constructeur de la classe Equation.
     *
     * @param nom        Le nom de l'équation.
     * @param expression L'expression mathématique de l'équation.
     * @throws RuntimeException Si le nom n'est pas valide.
     */
    public Equation(String nom, String expression) {
        this.nom = nom;
        this.expression = expression;
        this.elementsRequis = new HashSet<>();

        //on valide l'Équation
        if (!validateNom(nom)) throw new RuntimeException("Mauvais nom");

        //on extrait le variables requises
        this.elementsRequis.addAll(extraitElementsRequis(expression));
    }

    /**
     * Obtient le nom de l'équation.
     *
     * @return Le nom de l'équation.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de l'équation.
     *
     * @param nom Le nouveau nom de l'équation.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Obtient l'expression mathématique de l'équation.
     *
     * @return L'expression mathématique de l'équation.
     */
    public String getExpression() {
        return expression;
    }

    /**
     * Définit l'expression mathématique de l'équation.
     * Met à jour les éléments requis en extrayant les variables de l'expression.
     *
     * @param expression La nouvelle expression mathématique de l'équation.
     */
    public void setExpression(String expression) {
        this.expression = expression;

        //on extrait le variables requises
        this.elementsRequis.addAll(extraitElementsRequis(expression));
    }

    /**
     * Obtient l'ensemble des éléments requis pour l'équation.
     *
     * @return L'ensemble des éléments requis.
     */
    public Set<String> getElementsRequis() {
        return elementsRequis;
    }

    // Méthode privée pour extraire les éléments requis de l'expression
    private static Set<String> extraitElementsRequis(String expressionString) {
        Set<String> elementSet = new HashSet<>();

        //on trouve les elements requis
        Pattern patron = Pattern.compile("([a-zA-Z]+_@?[0-9]*#?[0-9]*)", Pattern.CASE_INSENSITIVE);

        Matcher matcher = patron.matcher(expressionString);
        while (matcher.find()) {
            elementSet.add(matcher.group());
        }

        return elementSet;
    }
    // Méthode privée pour valider le nom de l'équation

    private boolean validateNom(String name) {
        return extraitElementsRequis(name).size() == 1;
    }

    /**
     * Retourne une représentation textuelle de l'équation.
     *
     * @return Une chaîne de caractères représentant l'équation.
     */
    @Override
    public String toString() {
        return nom + "=" + expression + " requiert: " + elementsRequis;
    }
}
