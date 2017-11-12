/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hopeful
 */

import java.io.*;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import twitter4j.Status;
/**
 *
 * @author Chriss Winchester
 */
class TWEETS extends TwitterHastag{
     public float polarity;
     public String tweet;  
}

public class AnalyseTweets extends TWEETS{
    
    private final String[] tweets;
    private final float[] POLARITY;
    private double average;
    private Scanner file;
    private String[] temp_word;
    TWEETS TEMPO = new TWEETS();
    ArrayList<TWEETS> new_tweet = new ArrayList<TWEETS>();
    
    public AnalyseTweets(String myHshTage){
        
        TwitterHastag retrived = new TwitterHastag();
         ArrayList<Status> from_mohtweets;
        from_mohtweets = retrived.retrieveTweets(myHshTage);
        int number_of_tweets = from_mohtweets.size();
        POLARITY = new float[number_of_tweets];
        int m;
        tweets = new String[number_of_tweets];
        
        System.out.println("**************************************************************************");
        System.out.println("\t\tANALYSIS");
        System.out.println("**************************************************************************");
        for(m=0;m<number_of_tweets;m++){
            Status t = (Status) from_mohtweets.get(m);           
            String from_arraylist =  t.getText();
            tweets[m]= from_arraylist;
        } //ENF OF FOR LOOP
         for( int i=0;i<10;i++){
            System.out.println("[ "+(i+1)+" ]"+ tweets[i]);
        }       
    }
 
    //this is a code to close the senticnet4 analysis file
    public void openFile(){      
       try{
            file = new Scanner(new FileInputStream("senticnet4.txt"));            
        }
        catch(FileNotFoundException e){
            //System.err.println( "Error opening file." );
            System.out.println( "*******************************\nFile was not found\n*********************");
            System.exit(0);
        }
    }
    
    public ArrayList<TWEETS> analyse(){
        int i,j,compared;
        String[] from_file;
        String temp_string;
        float polarity,count=0,total_count = 0;
        openFile(); // Opening the file
        
        //looping through the tweets
        
        for(j=0;j<tweets.length;j++){ 
           System.out.println("******************** THE "+ (j+1)+ " TWEET ***********************");         
            tweets[j] = tweets[j].toLowerCase();
            temp_word = tweets[j].split("\\W");
            
            // analysing one tweet
            for(i=0;i<temp_word.length ;i++){
                // checking to see the EOF character
            while(file.hasNext()){                
                temp_string = file.nextLine();
                temp_string = temp_string.toLowerCase();              
                from_file = temp_string.split("\\s+");
                
                compared= temp_word[i].compareTo(from_file[0]);
                if(compared == 0){
                    polarity = Float.parseFloat(from_file[2]);
                 // System.out.println(temp_word[i]+" -----> found  polarity--> "+ polarity);
                    count = count + polarity;
                    break;
                } 
                
              } // end of while
             openFile();           
           } // end of inner for

         
           POLARITY[j] = count;
            total_count += count;
            count = 0;
        } //end of outer for
        average = total_count/10;
        System.out.println("********** THE TOTAL NUMBER OF TWEETS IS " + tweets.length + "\n"
                + "     THE TOTAL COUNT IS " + total_count + " \n     AND THE AVERAGE IS "+(total_count/tweets.length) ); // (total_count/tweets.length)
               
        //closing the ******.txt file
        file.close();
       
        for(i=0;i<10;i++){
            TWEETS TEMPO = new TWEETS();
            TEMPO.polarity = POLARITY[i];
            TEMPO.tweet = tweets[i];
            new_tweet.add(TEMPO);
        }
      System.out.println("*********************************************************************************************");
       
        
        Collections.sort(new_tweet, new Comparator<TWEETS>(){
            // SORTING IN ASCENDING ORDER..
            public int compare(TWEETS t1,TWEETS t2){
                return Float.valueOf(t1.polarity).compareTo(t2.polarity);
            }
        });
        System.out.println("****************************************");
//        for(i=0;i<10;i++){
//            System.out.println("[ "+(i+1)+" ]"+new_tweet.get(i).tweet+ "--------->" + new_tweet.get(i).polarity);
//        }
        //System.out.println( "\nFile is Closed");   
        for(i=0;i<10;i++){
            
           System.out.println("[ "+(i+1)+" ]"+new_tweet.get(i).tweet+ "--------->" + new_tweet.get(i).polarity);
            System.out.println("***********************************");
        }
        
        return new_tweet;
    }    
    
}

