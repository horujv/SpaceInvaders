package userControl;
import java.awt.*;

import bullet.*;
public class Player {

    private int x;
    private int y;
    private int r;

    private int dx;
    private int dy;
    private int speed;
    private boolean hit;
    private boolean firing;
    private long firingTimer;
    private  long firingDelay;


    private long hitTimer;

    private boolean left;
    private boolean right;
    private Color color1;
    private Color color2;

    private int score;

    private double lives;

    private int powerLevel;
    private int power;
    private int[] requiredPower={1,2,3,4,5};


    public Player(){
        x=GameBoard.WIDTH/2;
        y=GameBoard.HEIGHT-80;
        r=5;

        dx=0;
        dy=0;
        speed=5;
        color1=Color.WHITE;
        color2=Color.PINK;
        firing=false;
        firingTimer=System.nanoTime();
        firingDelay=200;
        lives=6;


        score=0;
        hit=false;
        hitTimer=0;


    }
    //Functions

    public void update(){
        if(right){
            dx=speed;
        }

        if(left){
            dx=-speed;
        }
        x+=dx;
        y+=dy;

        if(x<r) x=r;
        if(y<r) y=r;
        if(x>GameBoard.WIDTH-r) x=GameBoard.WIDTH-r;
        if(y>GameBoard.HEIGHT-r) y=GameBoard.HEIGHT-r;

        dx=0;
        dy=0;

        //firing
        if(firing){
            long elapsed=(System.nanoTime()-firingTimer)/1000000;

            if(elapsed>firingDelay){
                firingTimer=System.nanoTime();

                if(powerLevel<2){
                    GameBoard.bullets.add(new Bullet(270,x,y,10,Color.YELLOW));
                }

                else if(powerLevel < 4){
                    GameBoard.bullets.add(new Bullet(270,x+5,y,10,Color.YELLOW));
                    GameBoard.bullets.add(new Bullet(270,x-5,y,10,Color.YELLOW));
                }
                else{
                    GameBoard.bullets.add(new Bullet(270,x,y,10,Color.YELLOW));
                    GameBoard.bullets.add(new Bullet(275,x+5,y,10,Color.YELLOW));
                    GameBoard.bullets.add(new Bullet(265,x-5,y,10,Color.YELLOW));
                }
            }
        }

        if(hit) {
            long elapsed = (System.nanoTime() - hitTimer) / 1000000;
            if(elapsed > 50){
                hit=false;
                hitTimer = 0;
            }
        }
    }

    public void draw(Graphics2D g){
        g.setColor(color1);
        g.fillOval(x - r, y - r, r * 2, r * 2);
        g.setStroke(new BasicStroke());

        g.setColor(color1.darker());
        g.drawOval(x - r, y - r, 2 * r, 2 * r);
        g.setStroke(new BasicStroke(1));

    }

    public int getX(){return x;}

    public int getY(){return y;}

    public int getR(){return r;}

    public double getLives(){ return lives;}

    public long getHitTImer(){
        return hitTimer;
    }

    public boolean isHit(){
        return hit;
    }

    public int getScore(){
        return score;
    }

    public boolean isDead(){
        return lives<=0;
    }

    public void setLeft(boolean b){
        left=b;
    }

    public void setRight(boolean b){
        right=b;
    }

    public void setFiring(boolean b) {
        firing=b;
    }

    public void loseLife(){
        lives--;
    }

    public void addScore(int i){
        score+= i;
    }

    public void increasePower(int n){
        power+=n;
        if(power >= requiredPower[powerLevel]){
            power-=requiredPower[powerLevel];
            powerLevel++;
        }
    }

    public int getPowerLevel(){
        return powerLevel;
    }

    public int getPower(){
        return power;
    }

    public int getRequiredPower(){
        return requiredPower[powerLevel];
    }

    public void gainLife(){
        lives++;
    }

    public void hit(){
        lives=lives-0.5;
        hit=true;
        hitTimer=System.nanoTime();
    }
}
