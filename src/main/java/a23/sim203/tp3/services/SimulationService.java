package a23.sim203.tp3.services;

import a23.sim203.tp3.controller.AffichageResultatsController;
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


    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
        startTime = System.currentTimeMillis()/1000;
        this.moteurCalcul = moteurCalcul;
    }

    public void setAffichageResultatsController(AffichageResultatsController aRC) {
        this.affichageResultatsController = aRC;
    }

    public void setMoteurCalculEtScale(MoteurCalcul moteurCalcul, double scale) {
        startTime = System.currentTimeMillis()/1000;
//        dtDepart = (double) System.currentTimeMillis() /1000;
        this.moteurCalcul = moteurCalcul;
        this.timeScale = scale;
    }

    private void gererAffichageGraphique() {
        for (String equationAMettreDansGraphique :
                affichageResultatsController.getBoutonsCliques()) {
            affichageResultatsController.rafraichirGraphique(equationAMettreDansGraphique, moteurCalcul.getNouvelleValeurVariableMap().get(equationAMettreDansGraphique), endTime);
        }
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                endTime = System.currentTimeMillis()/1000;
                dt = endTime-startTime;
                startTime = endTime;

                moteurCalcul.calculeSim();
                moteurCalcul.avancePasDeTemps();
                moteurCalcul.refreshEquations();
                System.out.println(moteurCalcul.getNouvelleValeurVariableMap().get("t_").getConstantValue());
                System.out.println(moteurCalcul.getAncienneValeurVariableMap().get("dt_"));
                try {
                    gererAffichageGraphique();
                } catch (Exception e) {
                    System.out.println("erreur affichage" + e.getMessage() + "\nligne: " + e.getStackTrace()[0].getLineNumber());
                }
                System.out.println("service roule");
                return null;
            }
        };
    }
}
