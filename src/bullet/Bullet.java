package bullet;

import java.awt.*;

import userControl.GameBoard;

public class Bullet  {
    private double x;
    private double y;
    private int r;

    private double dx;
    private double dy;
    private double rad;
    private double speed;

    private Color color1;

    public Bullet(double angle,double x,double y,int speed,Color color){
        this.x=x;
        this.y=y;
        r=2;
        rad=Math.toRadians(angle);
        dx=Math.cos(rad)*speed;
        dy=Math.sin(rad)*speed;
        this.color1=color;
    }

    //Functions
    public boolean update(){
        x+=dx;
        y+=dy;
        if(x<-r || x>GameBoard.WIDTH+r || y<-r || y>GameBoard.HEIGHT +r)
            return true;
        return false;
    }

    public void draw(Graphics2D g){
        g.setColor(color1   );
        g.fillOval((int)(x - r),(int)(y - r),  2*r,2*r);
    }

    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }
    public double getR(){
        return this.r;
    }

}

