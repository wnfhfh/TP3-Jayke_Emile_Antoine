package a23.sim203.tp3.services;

import a23.sim203.tp3.controller.AffichageResultatsController;
import a23.sim203.tp3.controller.TableauController;
import a23.sim203.tp3.modele.MoteurCalcul;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.mariuszgromada.math.mxparser.Constant;

import java.time.LocalDateTime;

public class SimulationService extends ScheduledService<Void> {
    private MoteurCalcul moteurCalcul;
    private double endTime;
    private double timeScale;
    private double startTime;
    private double dt;
    private AffichageResultatsController affichageResultatsController;
    private TableauController tableauController;


    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
        startTime = System.currentTimeMillis();
        this.moteurCalcul = moteurCalcul;
        endTime = System.currentTimeMillis();
        dt = endTime-startTime;
    }

    public void setMoteurCalculEtScale(MoteurCalcul moteurCalcul, double scale) {
        startTime = System.currentTimeMillis()/1000;
//        dtDepart = (double) System.currentTimeMillis() /1000;
        this.moteurCalcul = moteurCalcul;
        this.timeScale = scale;
    }

    public void setAffichageResultatsController(AffichageResultatsController aRC) {
        this.affichageResultatsController = aRC;
    }

    private void gererAffichageGraphique() {
        for (String equationAMettreDansGraphique :
                affichageResultatsController.getBoutonsCliques()) {
            affichageResultatsController.rafraichirGraphique(equationAMettreDansGraphique, moteurCalcul.getNouvelleValeurVariableMap().get(equationAMettreDansGraphique), startTime);
        }
    }

    public void setTableauController(TableauController tableauController1) {
        this.tableauController = tableauController1;
    }

    private void gererTableauController() {
        tableauController.ajouterEquationTableau();
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                startTime = System.currentTimeMillis();

                moteurCalcul.calculeSim();
                moteurCalcul.refreshEquations();
                System.out.println(moteurCalcul.getNouvelleValeurVariableMap().get("t_").getConstantValue());
                System.out.println(moteurCalcul.getConstanteValeurMap().get("d_").getConstantValue());
                moteurCalcul.avancePasDeTemps();


                endTime = System.currentTimeMillis();
                dt = endTime-startTime;

                try {
                    if (affichageResultatsController != null) {
                        gererAffichageGraphique();
                    }
                    if (tableauController != null) {
                        gererTableauController();
                    }
                } catch (Exception e) {
                    System.out.println("erreur affichage" + e.getMessage() + "\nligne: " + e.getStackTrace()[0].getLineNumber());
                }
                System.out.println("service roule");
                return null;
            }
        };
    }
}
