package a23.sim203.tp3.services;

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
                System.out.println(moteurCalcul.getNouvelleValeurVariableMap().get("dt_"));
                System.out.println(moteurCalcul.getAncienneValeurVariableMap().get("dt_"));
                System.out.println("service roule");
                return null;
            }
        };
    }
}
