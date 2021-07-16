/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playmusic;

/**
 *
 * @author Ethan
 */
public class Song {
    private String fileName;
    private int numberOfPlays;
    private int rating;
    
    public Song(String fileName, int numberOfPlays, int rating) {
        this.fileName = fileName;
        this.rating = rating;
        this.fileName = fileName;
    }
    
    public String getFilename() {
        return fileName;
    }
    
    public int getRating() {
        return rating;
    }
    
    public int getNumberOfPlays() {
        return numberOfPlays;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setNumberOfPlays(int numberOfPlays) {
        this.numberOfPlays = numberOfPlays;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    
    
    
}
