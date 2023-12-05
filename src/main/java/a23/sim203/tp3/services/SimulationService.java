package a23.sim203.tp3.services;

import a23.sim203.tp3.controller.AffichageResultatsController;
import a23.sim203.tp3.controller.TableauController;
import a23.sim203.tp3.modele.MoteurCalcul;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.mariuszgromada.math.mxparser.Constant;

import java.time.LocalDateTime;

/**
 * Un service de simulation qui utilise le moteur de calcul pour avancer dans le temps.
 * Ce service peut être démarré, mis en pause et repris en fonction des besoins de la simulation.
 * Les valeurs des variables et constantes sont calculées à chaque pas de temps et mises à jour.
 */
public class SimulationService extends ScheduledService<Void> {
    private MoteurCalcul moteurCalcul;
    private double endTime;
    private double timeScale;
    private double startTime;
    private double dt;
    private AffichageResultatsController affichageResultatsController;
    private TableauController tableauController;
    private double tempsDebutPause;
    private double tempsFinPause;

    /**
     * Définit le moteur de calcul pour le service de simulation.
     * Initialise également le temps de début et de fin pour le calcul de la différence de temps.
     *
     * @param moteurCalcul Le moteur de calcul utilisé pour effectuer les calculs.
     */
    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
        startTime = System.currentTimeMillis();
        this.moteurCalcul = moteurCalcul;
        endTime = System.currentTimeMillis();
        dt = endTime - startTime;
    }

    /**
     * Définit le moteur de calcul et l'échelle de temps pour le service de simulation.
     *
     * @param moteurCalcul Le moteur de calcul utilisé pour effectuer les calculs.
     * @param scale        L'échelle de temps à appliquer.
     */
    public void setMoteurCalculEtScale(MoteurCalcul moteurCalcul, double scale) {
        this.moteurCalcul = moteurCalcul;
        this.timeScale = scale;
    }

    /**
     * Définit le contrôleur d'affichage des résultats pour le service de simulation.
     *
     * @param aRC Le contrôleur d'affichage des résultats.
     */
    public void setAffichageResultatsController(AffichageResultatsController aRC) {
        this.affichageResultatsController = aRC;
    }

    /**
     * Définit le contrôleur du tableau pour le service de simulation.
     *
     * @param tableauController1 Le contrôleur du tableau.
     */
    public void setTableauController(TableauController tableauController1) {
        this.tableauController = tableauController1;
    }

    /**
     * Démarre le service de simulation.
     */
    public void startService() {
        super.start();
    }

    /**
     * Reprise du service après une pause.
     */
    public void resumeService() {
        tempsFinPause = System.currentTimeMillis();
        super.start();
    }

    /**
     * Met en pause le service et enregistre le temps de début de la pause.
     */
    public void pauseService() {
        tempsDebutPause = System.currentTimeMillis();
        super.cancel();
        super.reset();
    }

    /**
     * Crée la tâche de simulation à exécuter par le service.
     * La tâche calculera les valeurs des variables et constantes à chaque pas de temps.
     * En cas de pause, la méthode spéciale de pause est appelée pour ajuster le temps.
     *
     * @return Une tâche représentant la simulation.
     */
    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (tempsFinPause - tempsDebutPause != 0) {
                    startTime = System.currentTimeMillis();
                    moteurCalcul.calculeSim();
                    moteurCalcul.refreshEquations();
                    System.out.println("methode speciale pause");
                    System.out.println(moteurCalcul.getNouvelleValeurVariableMap().get("t_").getConstantValue());
                    System.out.println(moteurCalcul.getConstanteValeurMap().get("d_").getConstantValue());
                    moteurCalcul.avancePasDeTemps();
                    endTime = System.currentTimeMillis() - (tempsFinPause - tempsDebutPause);
                    dt = endTime - startTime;
                    return null;
                } else {
                    startTime = System.currentTimeMillis();

                    moteurCalcul.calculeSim();
                    moteurCalcul.refreshEquations();
                    System.out.println(moteurCalcul.getNouvelleValeurVariableMap().get("t_").getConstantValue());
                    System.out.println(moteurCalcul.getConstanteValeurMap().get("d_").getConstantValue());
                    moteurCalcul.avancePasDeTemps();

                    if (tempsFinPause - tempsDebutPause != 0) {
                        endTime = System.currentTimeMillis() - (tempsFinPause - tempsDebutPause);
                        return null;
                    }
                    endTime = System.currentTimeMillis();
                    dt = endTime - startTime;
                    System.out.println("service roule");
                    return null;
                }
            }
        };
    }
}