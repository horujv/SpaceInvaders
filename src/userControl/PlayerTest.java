package userControl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    public void isHitTest() {
        Player player = new Player();

        assertNotNull("","" + player.isHit());
    }

    @Test
    public void loseLifeTest() {
        Player player = new Player();
        player.loseLife();
        assertNotNull("","" + player.getLives());
    }

    @Test
    public void addScoreTest() {
        Player player = new Player();
        player.addScore(1);
        assertNotNull("","" + player.getScore());
    }

    @Test
    public void gainLifeTest() {

        Player player = new Player();
        player.gainLife();
        assertNotNull("","" + player.getLives());
    }

    @Test
    public void increasePowerTest(){
        Player player = new Player();
        player.increasePower(1);
        assertNotNull("","" + player.getPower());
    }
}