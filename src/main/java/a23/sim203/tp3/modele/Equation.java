package a23.sim203.tp3.modele;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Equation {
    private String nom;
    private Set<String> elementsRequis;
    private String expression;

    public Equation(String nom, String expression) {
        this.nom = nom;
        this.expression = expression;
        this.elementsRequis = new HashSet<>();

        //on valide l'Équation
        if (!validateNom(nom)) throw new RuntimeException("Mauvais nom");

        //on extrait le variables requises
        this.elementsRequis.addAll(extraitElementsRequis(expression));
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;

        //on extrait le variables requises
        this.elementsRequis.addAll(extraitElementsRequis(expression));
    }



    public Set<String> getElementsRequis() {
        return elementsRequis;
    }


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

    private boolean validateNom(String name) {
        return extraitElementsRequis(name).size() == 1;
    }

    @Override
    public String toString() {
        return nom + "=" + expression + " requiert: " + elementsRequis;
    }
}
