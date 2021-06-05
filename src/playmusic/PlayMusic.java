package playmusic;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javax.swing.JFrame;

public class PlayMusic {

//    MediaPlayer mediaPlayer;
//    
//    @Override  
//    public void start (Stage primaryStage) throws Exception {  
////        System.out.println("Here.");
////        System.exit(0);
//        // TODO Auto-generated method stub  
//        //Initialising path of the media file, replace this with your file path   
//        String path = "C:/Users/Ethan/Music/Building 429 - Blessing I Can't See (Feat. Brooke Deleary) [Official Lyric Video].mp3";  
//        
//        //Instantiating Media class  
//        Media media = new Media(new File(path).toURI().toString());  
//          
//        //Instantiating MediaPlayer class   
//        mediaPlayer = new MediaPlayer(media); 
//          
//        //by setting this property to true, the audio will be played   
//        mediaPlayer.play();  
//        primaryStage.setTitle("Playing Audio"); 
//        //primaryStage.show();  
//    }  
    public static void main(String[] args) {

        
        /*
        IDEAS:
        
        - Wipe all saved song ratings.
        - 
        
        */
        
        JFrame frame = new JFrame("Music Player");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new PlayMusicJPanelForm());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setExtendedState(frame.getExtendedState() | frame.MAXIMIZED_BOTH);
        
//        try {
//            try {
//                //new ProcessBuilder("cmd", "/c", "start wmplayer \"C:\\Users\\Ethan\\Music\\NONAH - Jump (Official Lyric Video).mp3\"").inheritIO().start().waitFor();
//                new ProcessBuilder("cmd", "/c", "start wmplayer \"C:\\Users\\Ethan\\Music\\Red Rocks Worship - Echo Holy (Live from Littleton) [Official Lyric Video].mp3\"").inheritIO().start().waitFor();
//            } catch (InterruptedException ex) {
//                System.out.println(ex.getMessage());
//            System.out.println("Error2.");
//            }
//        } catch (IOException ex) {
//            System.out.println(ex.getMessage());
//            System.out.println("Error.");
//        }
    }

}
