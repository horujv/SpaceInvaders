package invader;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class InvaderTest {


    @Test
    public void getTypeTest() {
        Invader invader =new Invader(1,1);
        int expected =1 ;
        int observed = invader.getType();
        assertEquals(expected,observed);

    }

    @Test
    public void setSlow() {
        Invader invader =new Invader(1,1);
        invader.setSlow(true);
        boolean expected = true;
        boolean observed=invader.isSlow();
        assertEquals(expected,observed);
    }

    @Test
    public void isHit() {
        Invader invader =new Invader(1,1);
        boolean expected = true;
        invader.hit();
        boolean observed = invader.isHit();
        assertEquals(expected,observed);
    }

    @Test
    public void getRank() {
        Invader invader =new Invader(1,2);
        int expected =2 ;
        int observed = invader.getRank();
        assertEquals(expected,observed);

    }

    @Test
    public void isDeadTest() {
        Invader invader =new Invader(1,2);
        boolean expected = false;
        boolean observed = invader.isDead();
        assertEquals(expected,observed);

    }
}