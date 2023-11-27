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
    private double startTime = 0;
    private AffichageResultatsController affichageResultatsController;
    private TableauController tableauController;

//
//    public void setStartTime(long startTime) {
//        this.startTime = System.currentTimeMillis();
//    }


    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
        this.moteurCalcul = moteurCalcul;
    }
    public void setMoteurCalculEtScale(MoteurCalcul moteurCalcul, double scale) {
        this.moteurCalcul = moteurCalcul;
        this.timeScale = scale;
    }

    public void setAffichageResultatsController(AffichageResultatsController aRC) {
        this.affichageResultatsController = aRC;
    }
    private void gererAffichageGraphique() {
        for (String equationAMettreDansGraphique :
                affichageResultatsController.getBoutonsCliques()) {
            affichageResultatsController.rafraichirGraphique(equationAMettreDansGraphique, moteurCalcul.getNouvelleValeurVariableMap().get(equationAMettreDansGraphique), endTime);
        }
    }
    public void setTableauController(TableauController tableauController1){
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
                endTime = System.currentTimeMillis() + startTime;
                double deltaTemps = (endTime - System.currentTimeMillis()) * timeScale;
                startTime = endTime;
                moteurCalcul.calculeSim();
                moteurCalcul.avancePasDeTemps();
                moteurCalcul.refreshEquations();
                System.out.println(moteurCalcul.getNouvelleValeurVariableMap().get("dt_"));
                System.out.println(moteurCalcul.getAncienneValeurVariableMap().get("dt_"));
                try {
                    //FIXME
                    gererAffichageGraphique();
                    gererTableauController();
                } catch (Exception e) {
                    System.out.println("erreur affichage" + e.getMessage());
                }
                System.out.println("service roule");
                return null;
            }
        };
    }
}
