package a23.sim203.tp3.modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnregistreurTest {

    private Enregistreur enregistreur;

    @BeforeEach
    void prepare() {
        enregistreur = new Enregistreur();
    }

    @Test
    void enregsitreEquationSimple() throws IOException {
        File fichier1 = new File("test1.mod");
        Collection<String> equations = List.of("force_=masse_0*acceleration_0");
        Collection<String> variables = List.of("masse_0=345.56", "acceleration_0=-12.54");

        enregistreur.enregistreEquation(equations, variables, new ArrayList<>(), fichier1);
        assertTrue(Files.mismatch(fichier1.toPath(), Path.of(getClass().getResource("test1.mod").getPath())) == -1);

    }

    @Test
    void ChargeEquationSimple() throws IOException {
        File fichier1 = new File(getClass().getResource("test1.mod").getFile());

        Enregistreur.EquationsConstantesEtVariables resultat = enregistreur.chargeModele(fichier1);

        assertTrue(resultat.getEquations().containsAll(List.of("force_=masse_0*acceleration_0")));
        assertTrue(resultat.getVariables().containsAll(List.of("masse_0=345.56", "acceleration_0=-12.54")));

    }

}