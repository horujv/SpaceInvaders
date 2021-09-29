package userControl;

import java.awt.*;
import javax.swing.*;


import audioPlayer.AudioPlayer;
import hit.Hit;
import invader.Invader;
import powerUp.PowerUp;
import text.Text;
import bullet.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.*;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;


public class GameBoard extends JPanel implements Runnable, KeyListener{

    public static int WIDTH= 520;
    public static  int HEIGHT= 520;

    private Thread thread;
    private AudioPlayer audioPlayer=new AudioPlayer();
    private boolean running;
    private BufferedImage image;
    private Graphics2D g;
    private int FPS=30  ;
    private double avgFPS;
    public static Player player;
    public static ArrayList<Bullet> bullets;
    public static ArrayList<Invader> invaders;
    public static ArrayList<PowerUp> powerUps;
    public static ArrayList<Hit> hits;
    public static ArrayList<Text> texts;

    private long waveStartTimer;
    private int waveDelay=2000;
    private long waveStartTimerDiff;
    private int waveNumber;
    private boolean waveStart;

    private long slowDownTimer;
    private long slowDownTimerDiff;
    private int slowDownLength = 6000;


    //CONSTRUCTOR
    public GameBoard(){
        super();
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setFocusable(true);
        requestFocus();

    }

    public void addNotify(){
        super.addNotify();
        if(thread==null){
            thread=new Thread(this);
            thread.start();
        }
        addKeyListener(this);
    }

    public void run(){

        running=true;


        image=new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_BGR);
        g=(Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        long startTime;
        long URDTimeMillis;
        long waitTime;
        long totalTime = 0;

        int frameCount=0;
        int maxFrameCount=30;

        int targetTime=1000/FPS;
        bullets=new ArrayList<Bullet>();
        player=new Player();
        invaders=new ArrayList<Invader>();
        powerUps=new ArrayList<PowerUp>();
        hits=new ArrayList<Hit>();
        texts = new ArrayList<Text>();


        waveStartTimer=0;
        waveStartTimerDiff=0;
        waveStart=true;
        waveNumber=0;

        audioPlayer.playMusic("spaceinvaders1.wav");
        while(running){

            startTime=System.nanoTime();

            gameUpdate();
            gameRender();
            gameDraw();

            URDTimeMillis=(System.nanoTime()-startTime)/1000000;
            waitTime=targetTime-URDTimeMillis;
            try{
                Thread.sleep(waitTime);
            }
            catch (Exception e){

            }

            totalTime+=System.nanoTime()-startTime;
            frameCount++;
            if(frameCount==maxFrameCount){
                avgFPS=1000.0/(totalTime/frameCount/1000000);
                frameCount=0;
                totalTime=0;
            }
        }
        File file=new File("Score");
        try {
            FileWriter fw = new FileWriter(file, true);
            fw.write("\n" + this.player.getScore());
            fw.flush();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(!running){
            audioPlayer.stopMusic();
            audioPlayer.playMusic("gameover.wav");
        }
        g.setColor(new Color(0,0,0));
        g.fillRect(0,0,WIDTH,HEIGHT);
        g.setColor(Color.white);
        g.setFont(new Font("Century Gotchic",Font.PLAIN,16));
        String s="G A M E  O V E R";
        int length=(int) g.getFontMetrics().getStringBounds(s,g).getWidth();
        g.drawString(s,(WIDTH - length)/2 , HEIGHT/2);
        s="Final Score: " + player.getScore();
        length=(int) g.getFontMetrics().getStringBounds(s,g).getWidth();
        g.drawString(s,(WIDTH - length)/2 , HEIGHT/2 + 30);
        s="Implemented by HAJI ORUJOV";
        g.drawString(s,(WIDTH - length)/2 -55 , HEIGHT -50);
        s="ge49hux";
        g.drawString(s,(WIDTH - length)/2 +20 , HEIGHT -30);
        gameDraw();
    }

    public void gameUpdate(){
        //new wave
        if(waveStartTimer==0 && invaders.size()==0){
            waveNumber++;
            waveStart=false;
            waveStartTimer=System.nanoTime();
        }
        else{
            waveStartTimerDiff=(System.nanoTime()-waveStartTimer)/1000000;
            if(waveStartTimerDiff>waveDelay){
                waveStart=true;

                waveStartTimerDiff=0;
                waveStartTimer=0;
            }
        }



        if(waveStart&&invaders.size()==0){
            createNewEnemies();

        }
        //player update
        player.update();
        //bullets update
        for(int i=0;i<bullets.size();i++){
            boolean remove=bullets.get(i).update();
            if(remove){
                bullets.remove(i);
                i--;
            }
        }
        //invaders update
        for(int i=0;i<invaders.size();i++){
            invaders.get(i).update();
        }
        //powerUp update
        for(int i=0;i<powerUps.size();i++){
            boolean remove = powerUps.get(i).update();
            if(remove){
                powerUps.remove(i);
                i--;
            }
        }
        //explosion update
        for(int i = 0;i<hits.size();i++){
            boolean remove=hits.get(i).update();
            if(remove){
                hits.remove(i);
                i--;
            }
        }


        //enemy collision with bullets
        for(int i=0;i<bullets.size();i++){
            Bullet b=bullets.get(i);
            double bx=b.getX();
            double by=b.getY();
            double br=b.getR();

            for(int k=0;k<invaders.size();k++){
                Invader e=invaders.get(k);
                double ex=e.getX();
                double ey=e.getY();
                double er=e.getR();
                double dx=bx-ex;
                double dy=by-ey;
                double dist=Math.sqrt(dx * dx + dy*dy);

                if(dist<br+er){
                    e.hit();
                    bullets.remove(i);
                    i--;
                    break;
                }
            }
        }

        //check dead invaders
        for (int i=0;i<invaders.size();i++){
            if(invaders.get(i).isDead()){
                Invader e=invaders.get(i);

                //chance for power Up
                double rand=Math.random();
                if(rand<0.15) powerUps.add(new PowerUp(1,e.getX(),e.getY()));
                else if(rand<0.15) powerUps.add(new PowerUp(3,e.getX(),e.getY()));
                else if(rand<0.14) powerUps.add(new PowerUp(2,e.getX(),e.getY()));
                else if(rand < 0.32) powerUps.add(new PowerUp(4,e.getX(),e.getY()));

                player.addScore(e.getType()+ e.getRank());
                invaders.remove(i);
                i--;

                e.explode();
                hits.add(new Hit(e.getX(),e.getY(),(int)e.getR(),(int)(e.getR() + 20)));
            }
        }
        //player collision with bullets
        for(int i=0;i<bullets.size();i++){
            Bullet b=bullets.get(i);
            double bx=b.getX();
            double by=b.getY();
            double br=b.getR();

            double ex=player.getX();
            double ey=player.getY();
            double er=player.getR();
            double dx=bx-ex;
            double dy=by-ey;
            double dist=Math.sqrt(dx * dx + dy*dy);

            if(dist<br+er){
                player.hit();
                bullets.remove(i);
                i--;
                break;
            }

        }
        //Enemies fire bullets
        for (int i=0;i<invaders.size();i++){
            Invader e=invaders.get(i);
            //Enemies firing a bullet
            double rand=Math.random();
            if(rand<0.01) bullets.add(new Bullet(90,e.getX(),e.getY()+e.getR(),3,Color.RED));


        }

        //Check dead player
        if(player.isDead() ){
            running=false;
        }


        //check enemy get to the end

        int px=player.getX();
        int py=player.getY();
        int pr=player.getR();
        for(int i=0;i<invaders.size();i++){
            Invader e=invaders.get(i);
            double ex=  e.getX();
            double ey= e.getY();
            double er= e.getR();


            double dy=py-ey;
            double dist=Math.sqrt(dy*dy );

            if(dist<pr+er){
                running=false;
            }
        }

        //player-powerUp collission
        int qx=player.getX();
        int qy=player.getY();
        int qr=player.getR();
        for(int i=0;i<powerUps.size();i++){
            PowerUp p=powerUps.get(i);
            double x=p.getX();
            double y=p.getY();
            double r=p.getR();
            double dx=qx-x;
            double dy=qy-y;
            double dist=Math.sqrt(dx*dx + dy*dy);
            //collected powerUP
            if(dist<pr+r){
                int type=p.getType();
                if(type==1){
                    player.gainLife();
                    texts.add(new Text(player.getX(),player.getY(),2000,"extra life"));
                }
                if(type==2){
                    player.increasePower(1);
                    texts.add(new Text(player.getX(),player.getY(),2000,"power"));
                }
                if(type==3){
                    player.increasePower(2);
                    texts.add(new Text(player.getX(),player.getY(),2000,"double power"));
                }
                if(type == 4){
                    slowDownTimer = System.nanoTime();
                    for(int j = 0;j<invaders.size();j++){
                        invaders.get(j).setSlow(true);
                    }
                    texts.add(new Text(player.getX(),player.getY(),2000,"slow mode"));
                }

                powerUps.remove(i);
                i--;
            }
        }
        //slowDown update
        if(slowDownTimer!= 0){
            slowDownTimerDiff=(System.nanoTime() - slowDownTimer)/1000000;
            if(slowDownTimerDiff>slowDownLength){
                slowDownTimer= 0;
                for(int j = 0;j<invaders.size();j++){
                    invaders.get(j).setSlow(false);
                }
            }
        }
        //text update
        for(int i = 0 ; i <texts.size();i++){
            boolean remove = texts.get(i).update();
            if(remove){
                texts.remove(i);
                i--;
            }
        }
    }

    private void createNewEnemies() {

        invaders.clear();
        Invader e;

        if(waveNumber==1){
            for(int i=0;i<4;i++){
                invaders.add(new Invader(1,1));
            }

        }
        if(waveNumber==2){
            for(int i=0;i<8;i++){
                invaders.add(new Invader(1,1));
            }

        }
        if(waveNumber == 3) {
            for(int i=0;i<4;i++){
                invaders.add(new Invader(1,1));
            }
            invaders.add(new Invader(1,2));
            invaders.add(new Invader(1,2));
        }
        if(waveNumber== 4){
            invaders.add(new Invader(1,3));
            invaders.add(new Invader(1,4));
            for(int i=0;i<4;i++){
                invaders.add(new Invader(2,1));
            }
        }
        if(waveNumber == 5){
            for(int i=0;i<2;i++){
                invaders.add(new Invader(1,3));
            }
            invaders.add(new Invader(2,4));
        }
        if(waveNumber == 6) {
            invaders.add(new Invader(1, 3));
            for (int i = 0; i < 4; i++) {
                invaders.add(new Invader(2, 3));
                invaders.add(new Invader(3, 1));
            }
        }
        if(waveNumber == 7) {
            invaders.add(new Invader(1, 3));
            invaders.add(new Invader(2, 3));
            invaders.add(new Invader(3, 3));
        }
        if(waveNumber == 8) {
            invaders.add(new Invader(1, 4));
            invaders.add(new Invader(2, 4));
            invaders.add(new Invader(3, 4));
        }
        if(waveNumber==9){

            running=false;
        }
    }

    public void gameRender(){
        //draw background
        g.setColor(new Color(0,0,0));
        g.fillRect(0,0,WIDTH,HEIGHT);

        //draw slowdown screen
        if(slowDownTimer!=0){
            g.setColor(new Color(255,255,255,64));
            g.fillRect(0,0,WIDTH,HEIGHT);
        }
        //draw Player
        player.draw(g);

        //draw bullets
        for(int i=0;i<bullets.size();i++){
            bullets.get(i).draw(g);
        }

        //draw Enemies
        for(int i=0;i<invaders.size();i++){
            invaders.get(i).draw(g );
        }

        //draw wave Number
        if(waveStartTimer!=0){
            g.setFont(new Font("Centure Gothic",Font.PLAIN,18));
            String s="- W A V E    " + waveNumber + "   -";
            int length=(int) g.getFontMetrics().getStringBounds(s,g).getWidth();
            int alpha=(int)(255* Math.sin(3.14*waveStartTimerDiff/waveDelay));
            if(alpha>255) alpha=255;
            g.setColor(new Color(255,255,255,alpha));
            g.drawString(s,WIDTH/2-length/2,HEIGHT/2);
        }

        //draw Player lives
        for(int i=0;i<player.getLives();i++){
            g.setColor(Color.RED);
            g.fillOval(20+ (20*i),20,player.getR()*2,player.getR()*2);
            g.setStroke(new BasicStroke(3));
            g.setColor(Color.RED.darker());
            g.drawOval(20+ (20*i),20,player.getR()*2,player.getR()*2);
            g.setStroke(new BasicStroke(1));
        }

        //draw Player power
        g.setColor(Color.YELLOW);
        g.fillRect(20,40,player.getPower()*8,8);
        g.setColor(Color.YELLOW.darker());
        g.setStroke(new BasicStroke(2));
        for(int i=0;i<player.getRequiredPower();i++){
            g.drawRect(20 + 8*i,40,8,8);
        }
        g.setStroke(new BasicStroke(1));
        // draw Player score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Century Gothic",Font.PLAIN,14));
        g.drawString("Score: " + player.getScore(), WIDTH-80,25 );


        //draw hits
        for(int i = 0;i<hits.size();i++){
            hits.get(i).draw(g);
        }
        //draw power Ups
        for(int i=0;i<powerUps.size();i++){
            powerUps.get(i).draw(g);
        }
        //draw slowdown meter
        if(slowDownTimer!=0){
            g.setColor(Color.WHITE);
            g.drawRect(20,60,100,8);
            g.fillRect(20,60,(int)(100- 100.0 * slowDownTimerDiff/slowDownLength),8);
        }

        //draw text
        for(int i = 0 ; i <texts.size() ; i++){
            texts.get(i).draw(g);
        }
    }

    public void gameDraw(){
        Graphics g2=this.getGraphics();
        g2.drawImage(image,0,0,null);
        g2.dispose();
    }

    public void keyTyped(KeyEvent key){
    }

    public void keyPressed(KeyEvent key){
        int keyCode=key.getKeyCode();
        if(keyCode==KeyEvent.VK_LEFT){
            player.setLeft(true);
        }
        if(keyCode==KeyEvent.VK_RIGHT){
            player.setRight(true);
        }
        if(keyCode==KeyEvent.VK_SPACE){
            player.setFiring(true);
        }
    }

    public void keyReleased(KeyEvent key){
        int keyCode=key.getKeyCode();
        if(keyCode==KeyEvent.VK_LEFT){
            player.setLeft(false);
        }
        if(keyCode==KeyEvent.VK_RIGHT){
            player.setRight(false);
        }
        if(keyCode==KeyEvent.VK_SPACE){
            player.setFiring(false);
        }
    }
}
