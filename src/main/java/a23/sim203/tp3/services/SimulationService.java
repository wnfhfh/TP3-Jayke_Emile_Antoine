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
    private long deltaTemps;
//    public void setDeltaTemps(long deltaTemps) {
//        this.deltaTemps = 3;
//    }
//
//    public void setStartTime(long startTime) {
//        this.startTime = System.currentTimeMillis();
//    }


    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
        this.moteurCalcul = moteurCalcul;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
//                moteurCalcul.calcule("sim_");
//                moteurCalcul.avancePasDeTemps();
                return null;
            }
        };
    }
}
