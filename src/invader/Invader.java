package invader;

import java.awt.*;
import userControl.GameBoard;

public class Invader {

    private double x;
    private double y;
    private int r;

    private double dx;
    private double dy;
    private double rad;
    private  double speed;

    private int health;
    private int type;
    private int rank;

    private Color color1;

    private boolean ready;
    private boolean dead;
    private boolean hit;
    private long hitTimer;
    private boolean slow;

    public Invader(int type,int rank){

        this.type=type;
        this.rank=rank;
        //default enemy
        if(type==1){
            color1 = new Color(255,255,255,255);
            if(rank ==1 ){
                speed=30;
                r=10;
                health=1;
            }
            if(rank == 2){
                speed =20;
                r=20;
                health=2;
            }
            if(rank == 3){
                speed =1.5 ;
                r=30;
                health=3;
            }
            if(rank == 4){
                speed = 1.5;
                r=40;
                health = 4;
            }
        }

        //stronger faster enemy
        if(type == 2){
            // color1 =Color.PINK;
            color1= new Color (255,0,0,128);
            if(rank == 1){
                speed = 3;
                r = 10;
                health = 2;
            }
            if(rank == 2){
                speed= 3 ;
                r = 20;
                health= 3 ;
            }
            if(rank == 3){
                speed = 2.5;
                r=30;
                health=3;
            }
            if(rank == 4){
                speed=2.5;
                r=40;
                health=4;

            }
        }

        //slow , but hard to kill
        if(type == 3 ){
            //color1=Color.GREEN;
            color1= new Color(0,255,0,128);
            if(rank == 1){
                speed= 1.5;
                r=20;
                health =5 ;
            }
            if(rank == 2){
                speed= 1.5;
                r=20;
                health =6 ;
            }
            if(rank == 3){
                speed= 1.5;
                r=25;
                health =7 ;
            }
            if(rank == 4){
                speed= 1.5;
                r=45;
                health =8;
            }

        }
        x=Math.random()*GameBoard.WIDTH/2+GameBoard.WIDTH/4;
        y=-r;

        // angle = Math.random()*140+20;
        //rad=Math.toRadians(angle);
        dx=1;//Math.cos(rad)*speed;
        dy=1;//Math.sin(rad)*speed;
        ready=false;
        dead=false;
        hit=false;
        hitTimer=0;
    }

    // FUNCTIONS

    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }
    public double getR(){
        return this.r;
    }
    public int getType(){
        return type;
    }
    public void setSlow(boolean slow){
        this.slow=slow;
    }
    public boolean isSlow(){
       return this.slow;
    }
    public long getHitTImer(){
        return hitTimer;
    }
    public boolean isHit(){
        return hit;
    }


    public int getRank(){
        return rank;
    }

    public void hit(){
        health--;
        if(health<=0){
            dead=true;

        }
        hit=true;
        hitTimer=System.nanoTime();
    }

    public boolean isDead(){
        return dead;
    }

    public void explode(){
        if(rank > 1){
            int amount = 0;
            if(type ==1){
                amount=3;
            }
            if(type == 2){
                amount =3;
            }
            if(type == 3){
                amount =4  ;
            }


            for(int i = 0 ; i<amount;i++){
                //that means we use the same enemy type and a rank lower
                Invader e = new Invader(getType(),getRank()-1);
                e.x=this.x+Math.random()*10;
                e.y=this.y;
                double angle = 0;
                if(!ready){
                    angle=Math.random() *140 + 20;
                }
                else {
                    angle= Math.random() *360;
                }
                e.rad = Math.toRadians(angle);

                GameBoard.invaders.add(e);

            }
        }
    }

    public void update(){
        if(slow){
            x+=dx*0.01;
            y+=dy*0.3;
        }
        else{
            x+=dx*0.02;
            y+=dy*0.5;
        }


        if(!ready){
            if(x>r && x<GameBoard.WIDTH-r && y>r && y<GameBoard.WIDTH-r){
                ready=true;
            }
        }

        if(x<r && dx<0) dx=-dx;
        if(y<r && dy<0) dy=-dy;
        if(x>GameBoard.WIDTH - r && dx>0) dx=-dx;
        if(y>GameBoard.HEIGHT-r && dy>0) dy=-dy;

        if(hit) {
            long elapsed = (System.nanoTime() - hitTimer) / 1000000;
            if(elapsed > 50){
                hit=false;
                hitTimer = 0;
            }
        }
    }

    public void draw(Graphics2D g){
        if(hit){
            g.setColor(Color.WHITE);
            g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
            g.setStroke(new BasicStroke(3)); // why do we do it
            g.setColor(Color.WHITE.darker());
            g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
            g.setStroke(new BasicStroke(1));
        }
        else {

            g.setColor(color1);
            g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
            g.setStroke(new BasicStroke(3)); // why do we do it
            g.setColor(color1.darker());
            g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
            g.setStroke(new BasicStroke(1));
        }
    }


}
