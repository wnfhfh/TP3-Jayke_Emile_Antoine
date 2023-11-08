package a23.sim203.tp3.services;

import a23.sim203.tp3.modele.MoteurCalcul;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.time.LocalDateTime;

public class SimulationService extends Service<Void> {
    private MoteurCalcul moteurCalcul;
    private long startTime;
    private long endTime;
    private long differenceTemps;
    private long intervalleDeTemps;

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i < intervalleDeTemps; i++) {
                    startTime = (System.currentTimeMillis() * 1000);


                    endTime = (System.currentTimeMillis() * 1000);
                    differenceTemps = endTime - startTime;
                }
                return null;
            }
        };
    }
}
