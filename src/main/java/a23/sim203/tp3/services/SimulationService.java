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
    private double tempsDebutPause;
    private double tempsFinPause;

    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
        startTime = System.currentTimeMillis();
        this.moteurCalcul = moteurCalcul;
        endTime = System.currentTimeMillis();
        dt = endTime - startTime;
    }

    public void setMoteurCalculEtScale(MoteurCalcul moteurCalcul, double scale) {
        this.moteurCalcul = moteurCalcul;
        this.timeScale = scale;
    }

    public void setAffichageResultatsController(AffichageResultatsController aRC) {
        this.affichageResultatsController = aRC;
    }

    public void setTableauController(TableauController tableauController1) {
        this.tableauController = tableauController1;
    }

    public void startService() {
        super.start();
    }

    public void resumeService() {
        tempsFinPause = System.currentTimeMillis();
        super.start();
    }

    public void pauseService() {
        tempsDebutPause = System.currentTimeMillis();
        super.cancel();
        super.reset();
    }

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