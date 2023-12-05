package a23.sim203.tp3.controller;

import a23.sim203.tp3.modele.Equation;
import a23.sim203.tp3.modele.MoteurCalcul;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.mariuszgromada.math.mxparser.Constant;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

/**
 * Le contrôleur {@code AffichageResultatsController} gère l'affichage des résultats dans une interface graphique.
 * Il implémente l'interface {@code Initializable} pour permettre l'initialisation de la classe lors du chargement du fichier FXML.
 */
public class AffichageResultatsController implements Initializable {
    /**
     * Valeur initiale utilisée dans le contexte de l'affichage des résultats.
     */
    Double i = 0d;
    /**
     * Bouton de vidage associé à l'instance de la classe.
     */
    @FXML
    private Button boutonVider;
    /**
     * Graphique en ligne associé à l'instance de la classe.
     */
    @FXML
    private LineChart lineChart;
    /**
     * TilePane pour afficher les équations associé à l'instance de la classe.
     */
    @FXML
    private TilePane tilePaneEquations;
    /**
     * Axe X du graphique en ligne.
     */

    final private NumberAxis axeX = new NumberAxis();
    /**
     * Axe Y du graphique en ligne.
     */
    final private NumberAxis axeY = new NumberAxis();
    /**
     * Moteur de calcul associé à l'instance de la classe.
     */
    private MoteurCalcul moteurCalcul;
    /**
     * Ensemble de boutons cliqués associé à l'instance de la classe.
     */
    private HashSet<String> boutonsCliques;
    /**
     * Ensemble de séries déjà présentes dans le graphique associé à l'instance de la classe.
     */
    private HashSet<XYChart.Series> seriesDejaDansGraphique;

    /**
     * Crée un graphique de ligne (LineChart) avec les configurations initiales.
     */
    public void creerLineChart() {
//        XYChart.Series series = new XYChart.Series<>();
//        series.getData().addAll(
//                new XYChart.Data<>(1, 2),
//                new XYChart.Data<>(2, 2),
//                new XYChart.Data<>(3, 2),
//                new XYChart.Data<>(4, 2),
//                new XYChart.Data<>(5, 2));
//        lineChart.getData().add(series);
    }

    /**
     * Rafraîchit le graphique avec une nouvelle valeur pour une équation donnée à un certain temps.
     *
     * @param nomEquation Le nom de l'équation.
     * @param y           La constante représentant la valeur de l'équation.
     * @param endTime     Le temps associé à la nouvelle valeur.
     */
    public void rafraichirGraphique(String nomEquation, Constant y, double endTime) { //TODO figurer si on se fie a endtime pour calc
        if (dejaDansGraphique(nomEquation) != null) {
            XYChart.Series serieARafraichir = dejaDansGraphique(nomEquation);
            serieARafraichir.setName(nomEquation);
            XYChart.Data data = new XYChart.Data<Double, Double>(endTime, y.getConstantValue());
            Circle cercleData = new Circle(5);
            Tooltip.install(cercleData, new Tooltip(" + y.getConstantValue())"));
            data.setNode(cercleData);
            serieARafraichir.getData().add(data);
        } else {
            XYChart.Series aAjouter = new XYChart.Series();
            aAjouter.setName(nomEquation);
            seriesDejaDansGraphique.add(aAjouter);
            XYChart.Data data = new XYChart.Data<Double, Double>(endTime, y.getConstantValue());
            Circle cercleData = new Circle(5);
            Tooltip.install(cercleData, new Tooltip(" + y.getConstantValue())"));
            data.setNode(cercleData);
            aAjouter.getData().add(data);
            lineChart.setCreateSymbols(true);
            lineChart.getData().add(aAjouter);
        }
    }

    /**
     * Vérifie si une série de données pour une équation donnée est déjà présente dans le graphique.
     *
     * @param nomEquation Le nom de l'équation.
     * @return La série de données si elle est déjà présente, sinon null.
     */
    private XYChart.Series dejaDansGraphique(String nomEquation) {
        XYChart.Series seriesDejaDansGraphique = null;
        for (XYChart.Series series :
                this.seriesDejaDansGraphique) {
            if (nomEquation.equals(series.getName())) seriesDejaDansGraphique = series;
        }
        return seriesDejaDansGraphique;
    }

    /**
     * Obtient l'ensemble des boutons cliqués.
     *
     * @return L'ensemble des boutons cliqués.
     */
    public HashSet<String> getBoutonsCliques() {
        return boutonsCliques;
    }

    /**
     * Ajoute le nom d'une équation à l'ensemble des boutons cliqués.
     *
     * @param nomEquation Le nom de l'équation à ajouter.
     */
    public void ajouterEquationAGraph(String nomEquation) {
        boutonsCliques.add(nomEquation);
    }

    /**
     * Rafraîchit la liste des équations affichées dans le TilePane.
     */
    @FXML
    public void rafraichirEquations() {
        tilePaneEquations.getChildren().clear();
        for (String equationNom :
                equationsAMettreBouton()) {
            Button button = new Button(equationNom);
            button.setPrefSize(100, 100);
            button.setMinSize(100, 100);
            button.setMaxSize(100, 100);
            button.setOnAction(event1 -> {
                ajouterEquationAGraph(button.getText());
                tilePaneEquations.getChildren().remove(button);
            });
            tilePaneEquations.getChildren().add(button);
        }
    }

    /**
     * Obtient l'ensemble des noms d'équations à mettre dans les boutons du TilePane.
     *
     * @return L'ensemble des noms d'équations à afficher.
     */
    private HashSet<String> equationsAMettreBouton() {
        HashSet<String> equationsAMettreBouton = new HashSet<>();
        HashSet<String> equationsDejaDansGraph = new HashSet<>();

        for (XYChart.Series series :
                seriesDejaDansGraphique) {
            equationsDejaDansGraph.add(series.getName());
        }
        for (Equation equation :
                moteurCalcul.getAllEquations()) {
            if (!equationsDejaDansGraph.contains(equation.getNom())) {
                equationsAMettreBouton.add(equation.getNom());
            }
        }
        return equationsAMettreBouton;
    }

    /**
     * Associe le moteur de calcul à la classe.
     *
     * @param moteurCalcul Le moteur de calcul à associer.
     */
    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
        this.moteurCalcul = moteurCalcul;
    }

    /**
     * Gère l'action du bouton pour vider les données et rafraîchir l'interface graphique.
     */
    public void boutonViderAction() {
        boutonsCliques.clear();
        lineChart.getData().clear();
        seriesDejaDansGraphique.clear();
        rafraichirEquations();
    }

    /**
     * Initialise la classe, créant un nouveau moteur de calcul, initialisant les ensembles
     * et configurant l'action du bouton Vider.
     *
     * @param location  L'emplacement du fichier FXML.
     * @param resources Les ressources utilisées pour localiser l'emplacement.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        moteurCalcul = new MoteurCalcul();
        boutonsCliques = new HashSet<>();
        seriesDejaDansGraphique = new HashSet<>();
        tilePaneEquations.setPadding(new Insets(10, 10, 10, 10));
        boutonVider.setOnAction(event -> {
            boutonViderAction();
        });
    }
}
