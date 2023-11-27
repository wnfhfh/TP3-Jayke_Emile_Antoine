package a23.sim203.tp3.modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariuszgromada.math.mxparser.Constant;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MoteurCalculTest {

    private MoteurCalcul moteurCalcul;

    @BeforeEach
    public void prepareMoteur() {
        moteurCalcul = new MoteurCalcul();
    }

    @Test
    public void testAjoute1Variable() {
        moteurCalcul.ajouteEquation("a_0=x_1");
        moteurCalcul.ajouteEquation("a_=x_");
//        Set<Map.Entry<String, Constant>> resultat = moteurCalcul.getVariableValueMap().entrySet();
        Constant resultat = moteurCalcul.getConstanteValeurMap().get("x_1");
        assertEquals(Double.NaN, resultat.getConstantValue());
        assertEquals("x_1", resultat.getConstantName());

        Constant resultat2 = moteurCalcul.getConstanteValeurMap().get("x_");
        assertEquals(Double.NaN, resultat2.getConstantValue());
        assertEquals("x_", resultat2.getConstantName());
    }

    @Test
    public void testAjoute1Equation1Variable() {
        moteurCalcul.ajouteEquation("a_=x_");
        moteurCalcul.setValeurConstante("x_", 1.0);

//        Set<Map.Entry<String, Constant>> resultat = moteurCalcul.getVariableValueMap().entrySet();
        Constant resultat = moteurCalcul.getConstanteValeurMap().get("x_");
        assertEquals(1, resultat.getConstantValue());
        assertEquals("x_", resultat.getConstantName());
    }

    @Test
    public void testAjoute1Equation3Variable() {
        moteurCalcul.ajouteEquation("a_0=x_0+x_1+x_3");
        boolean resultat = moteurCalcul.getConstanteValeurMap().keySet().containsAll(
                Set.of("x_0", "x_1", "x_3"));
        assertTrue(resultat);
    }

    @Test
    public void testRemplaceVariableParEquation() {
        moteurCalcul.ajouteEquation("a_0=x_0+1");// varaible x0 créée
        boolean etape1 = moteurCalcul.getConstanteValeurMap().containsKey("x_0");

        moteurCalcul.ajouteEquation("x_0=x_1+3");// variable x0 devient équation
        boolean etape2 = !moteurCalcul.getConstanteValeurMap().containsKey("x_0");
        boolean etape3 = moteurCalcul.getConstanteValeurMap().containsKey("x_1");

        assertTrue(etape1 && etape2 && etape3);
    }

    @Test
    public void testEffaceEquationSimple() {
        moteurCalcul.ajouteEquation("a_0=x_0+1");// varaible x0 créée
        boolean etape1 = moteurCalcul.getConstanteValeurMap().containsKey("x_0");
        boolean etape2 = moteurCalcul.getEquationMap().containsKey("a_0");

        moteurCalcul.effaceEquation("a_0");// variable x0 devient équation
        boolean etape3 = !moteurCalcul.getConstanteValeurMap().containsKey("x_0");
        boolean etape4 = !moteurCalcul.getEquationMap().containsKey("a_0");

        assertTrue(etape1 && etape2 && etape3 && etape4);
    }


    @Test
    public void testEffaceEquationVariablePartagee() {
        moteurCalcul.ajouteEquation("a_0=x_0+1");// varaible x0 créée
        moteurCalcul.ajouteEquation("b_0=x_0+2");// varaible x0 encore utilisé

        boolean etape1 = moteurCalcul.getConstanteValeurMap().containsKey("x_0");
        boolean etape2 = moteurCalcul.getEquationMap().containsKey("a_0");
        boolean etape3 = moteurCalcul.getEquationMap().containsKey("b_0");

        moteurCalcul.effaceEquation("a_0");// variable x0 doit demeurer
        boolean etape4 = moteurCalcul.getConstanteValeurMap().containsKey("x_0");
        boolean etape5 = !moteurCalcul.getEquationMap().containsKey("a_0");
        boolean etape6 = moteurCalcul.getEquationMap().containsKey("b_0");


        assertTrue(etape1 && etape2 && etape3 && etape4 && etape5 && etape6);
    }

    @Test
    public void testCalculSimple() {
        Equation equation = new Equation("test_0", "2*(3-6)");
        double resultat = moteurCalcul.calcule(equation);
        assertEquals(-6, resultat);
    }

    @Test
    public void testCalcul1Variable() {
        moteurCalcul.ajouteEquation("a_0=b_0+1");
        moteurCalcul.setValeurConstante("b_0", 4.0);
        double resultat = moteurCalcul.calcule("a_0");
        assertEquals(5, resultat);
    }

    @Test
    public void testCalcul2Variable() {
        moteurCalcul.ajouteEquation("a_0=b_0*c_0");
        moteurCalcul.setValeurConstante("b_0", 4.0);
        moteurCalcul.setValeurConstante("c_0", 5.0);

        double resultat = moteurCalcul.calcule("a_0");
        assertEquals(20, resultat);
    }

    @Test
    public void testCalcul2Equation() {
        moteurCalcul.ajouteEquation("a_0=b_0*2");
        moteurCalcul.ajouteEquation("b_0=3+v_0");
        moteurCalcul.setValeurConstante("v_0", 4.0);

        double resultat = moteurCalcul.calcule("a_0");
        assertEquals(14, resultat);
    }

    @Test
    public void testCalcul2Equation2Var() {
        moteurCalcul.ajouteEquation("a_0=b_0*c_2");
        moteurCalcul.ajouteEquation("b_0=c_2+g_77");
        moteurCalcul.setValeurConstante("c_2", 4.0);
        moteurCalcul.setValeurConstante("g_77", 5.0);
        double resultat = moteurCalcul.calcule("a_0");
        assertEquals(36, resultat);
    }

    @Test
    void testAvancePasDeTemps() {
        Constant c1 = new Constant("x_1", 11);
        Constant c2 = new Constant("x_2", -12);
        Constant c3 = new Constant("x_3", 3.5);
        HashMap<String, Constant> valeursTest = new HashMap<>(Map.of("x_1", c1, "x_2", c2, "x_3", c3));

        moteurCalcul.getNouvelleValeurVariableMap().putAll(valeursTest);
        moteurCalcul.getAncienneValeurVariableMap().clear();
        assertEquals(2, moteurCalcul.avancePasDeTemps());

        assertTrue(valeursTest.equals(moteurCalcul.getAncienneValeurVariableMap()));
        assertTrue(moteurCalcul.getNouvelleValeurVariableMap().size() == 0);
        assertEquals(2, moteurCalcul.getPasDeTempsActuel());

    }

    @Test
    void ajouteEquationRecursive() {
        moteurCalcul.ajouteEquation("y_=y_+1");
        assertTrue(moteurCalcul.getAncienneValeurVariableMap().containsKey("y_"));
        assertFalse(moteurCalcul.getNouvelleValeurVariableMap().containsKey("y_"));
        assertFalse(moteurCalcul.getConstanteValeurMap().containsKey("y_"));
        assertTrue(moteurCalcul.getEquationMap().containsKey("y_"));

    }

    @Test
    void testCalcule2PasDeTempsSimple() {

        moteurCalcul.ajouteEquation("y_=y_+1");
        moteurCalcul.setValeurInitiale("y_", 0.5);

        assertEquals(1.5, moteurCalcul.calcule("y_"));
        assertEquals(0.5, moteurCalcul.getAncienneValeurVariableMap().get("y_").getConstantValue());
        assertEquals(1.5, moteurCalcul.getNouvelleValeurVariableMap().get("y_").getConstantValue());

        assertEquals(2, moteurCalcul.avancePasDeTemps());
        assertEquals(2.5, moteurCalcul.calcule("y_"));
        assertEquals(1.5, moteurCalcul.getAncienneValeurVariableMap().get("y_").getConstantValue());
        assertEquals(2.5, moteurCalcul.getNouvelleValeurVariableMap().get("y_").getConstantValue());
    }

    @Test
    void testCalcule2PasDeTempsAvecConstante() {

        moteurCalcul.ajouteEquation("y_=y_+c_");
        moteurCalcul.setValeurInitiale("y_", 0.5);
        moteurCalcul.setValeurConstante("c_", 11.0);

        assertEquals(11.5, moteurCalcul.calcule("y_"));
        assertEquals(0.5, moteurCalcul.getAncienneValeurVariableMap().get("y_").getConstantValue());
        assertEquals(11.5, moteurCalcul.getNouvelleValeurVariableMap().get("y_").getConstantValue());
        assertEquals(11, moteurCalcul.getConstanteValeurMap().get("c_").getConstantValue());
        assertEquals(2, moteurCalcul.avancePasDeTemps());

        assertEquals(22.5, moteurCalcul.calcule("y_"));
        assertEquals(11.5, moteurCalcul.getAncienneValeurVariableMap().get("y_").getConstantValue());
        assertEquals(22.5, moteurCalcul.getNouvelleValeurVariableMap().get("y_").getConstantValue());
        assertEquals(11, moteurCalcul.getConstanteValeurMap().get("c_").getConstantValue());

    }

    @Test
    void testCalcule2PasDeTemps2EquationsConstante() {

        moteurCalcul.ajouteEquation("y_=x_+1");
        moteurCalcul.ajouteEquation("x_=c_+5");
        moteurCalcul.setValeurConstante("c_", 0.5);

        assertEquals(6.5, moteurCalcul.calcule("y_"));
        assertTrue(Double.isNaN(moteurCalcul.getAncienneValeurVariableMap().get("y_").getConstantValue()));
        assertEquals(6.5, moteurCalcul.getNouvelleValeurVariableMap().get("y_").getConstantValue());
        assertTrue(Double.isNaN(moteurCalcul.getAncienneValeurVariableMap().get("x_").getConstantValue()));
       // assertEquals(5.5, moteurCalcul.getNouvelleValeurVariableMap().get("x_").getConstantValue());


        assertEquals(2, moteurCalcul.avancePasDeTemps());
        assertEquals(6.5, moteurCalcul.calcule("y_"));
        assertEquals(6.5, moteurCalcul.getAncienneValeurVariableMap().get("y_").getConstantValue());
        assertEquals(6.5, moteurCalcul.getNouvelleValeurVariableMap().get("y_").getConstantValue());
        assertEquals(5.5, moteurCalcul.getAncienneValeurVariableMap().get("x_").getConstantValue());
        assertEquals(5.5, moteurCalcul.getNouvelleValeurVariableMap().get("x_").getConstantValue());
    }

    @Test
    void testCalcule2PasDeTemps2Equations() {

        moteurCalcul.ajouteEquation("y_=x_+1");
        moteurCalcul.ajouteEquation("x_=x_*dt_");
        moteurCalcul.setValeurConstante("dt_", 0.5);
        moteurCalcul.setValeurInitiale("x_", 3.0);

        assertEquals(2.5, moteurCalcul.calcule("y_"));
        assertTrue(Double.isNaN(moteurCalcul.getAncienneValeurVariableMap().get("y_").getConstantValue()));
        assertEquals(2.5, moteurCalcul.getNouvelleValeurVariableMap().get("y_").getConstantValue());
        assertEquals(3.0, moteurCalcul.getAncienneValeurVariableMap().get("x_").getConstantValue());
        assertEquals(1.5, moteurCalcul.getNouvelleValeurVariableMap().get("x_").getConstantValue());

        assertEquals(2, moteurCalcul.avancePasDeTemps());
        assertEquals(1.75, moteurCalcul.calcule("y_"));
        assertEquals(2.5, moteurCalcul.getAncienneValeurVariableMap().get("y_").getConstantValue());
        assertEquals(1.75, moteurCalcul.getNouvelleValeurVariableMap().get("y_").getConstantValue());
        assertEquals(1.5, moteurCalcul.getAncienneValeurVariableMap().get("x_").getConstantValue());
        assertEquals(0.75, moteurCalcul.getNouvelleValeurVariableMap().get("x_").getConstantValue());
    }


}