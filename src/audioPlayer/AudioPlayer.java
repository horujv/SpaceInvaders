package audioPlayer;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class AudioPlayer implements AudioPlayerInterface {
    private static Clip clip;

    public static void main(String[] args){
        playMusic("spaceinvaders1.wav");
    }

    public static void stopMusic(){
       clip.stop();
    }

    public static void playMusic(String filePath){

        try{ clip = AudioSystem.getClip();
           clip.open(AudioSystem.getAudioInputStream(new File(filePath)));
           clip.start();
           

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
