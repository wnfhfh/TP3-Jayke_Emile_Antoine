package a23.sim203.tp3.services;

import a23.sim203.tp3.modele.MoteurCalcul;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.time.LocalDateTime;

public class SimulationService extends ScheduledService<Void> {
    private MoteurCalcul moteurCalcul;
    private long startTime;
    private long endTime;
    private double timeScale;

//
//    public void setStartTime(long startTime) {
//        this.startTime = System.currentTimeMillis();
//    }


    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
        this.moteurCalcul = moteurCalcul;
    }

    public void setMoteurCalcul(MoteurCalcul moteurCalcul,double scale) {
        this.moteurCalcul = moteurCalcul;
        this.timeScale = scale;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                double deltaTemps = getPeriod().toSeconds()*timeScale;
                moteurCalcul.ajouteEquation("dt_ = "+ deltaTemps);
                moteurCalcul.ajouteEquation("t_ = ");
//                moteurCalcul.calcule("sim_");
//                moteurCalcul.avancePasDeTemps();
                System.out.println(getPeriod().toSeconds());
                System.out.println("service roule");
                return null;
            }
        };
    }
}
