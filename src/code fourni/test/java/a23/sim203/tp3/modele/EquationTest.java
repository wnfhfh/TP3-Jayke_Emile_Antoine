package a23.sim203.tp3.modele;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EquationTest {

    @Test
    public void testNomValide() {
        Equation eq = new Equation("e_","x_+x_12+x_@3+x_#+x_#34+x_@#+x_@34#+x_34#23+x_@3#4");
                assertTrue(eq.getElementsRequis().containsAll(
                        List.of("x_","x_12","x_@3","x_#","x_#34","x_@#","x_@34#","x_34#23","x_@3#4")));
    }

    @Test
    public void testNomNonValide() {
        Equation eq = new Equation("e_","x+x@+x#+x@#+@+#+@#");
        assertTrue(eq.getElementsRequis().size()==0);
    }

}