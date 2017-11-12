
import java.util.ArrayList;
import javax.swing.JOptionPane;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hopeful
 */
public class TwitterHastag extends ChatBotFormGUI {

    public ArrayList<Status> retrieveTweets(String myHshTage) {        

    try{
        // TODO add your handling code here:

        ConfigurationBuilder myConfigBuilder = new ConfigurationBuilder();
        
        myConfigBuilder.setOAuthConsumerKey("BXIbXevwj61gf2JGwHKvwsXDA")
                .setOAuthConsumerSecret("eIs26w0Bf0NWH9T3W7pBem0Dgtk16ZyLLDLc7u4OoEguWgulnM")
                .setOAuthAccessToken("925645786322436096-QTedV9f7IcBCShBcpgjL2N7vybJIvf6")
                .setOAuthAccessTokenSecret("ndboLTPvmib11tfnXxgEMa5737PpzcOZleWIUjR748mrg");
        
        TwitterFactory tf = new TwitterFactory(myConfigBuilder.build());
        Twitter twitter = tf.getInstance();
        Query query = new Query(myHshTage);
        query.setLang("en");
        int numberOfTweets = 10;
        long lastID = Long.MAX_VALUE;
        ArrayList<Status> tweets = new ArrayList<Status>();
        
        while (tweets.size() < numberOfTweets) {
            if (numberOfTweets - tweets.size() > 100) {
                query.setCount(100);
            } else {
                query.setCount(numberOfTweets - tweets.size());
            }
            try {
                QueryResult result = twitter.search(query);
                tweets.addAll(result.getTweets());
                for (Status t : tweets) {
                    if (t.getId() < lastID) {
                        lastID = t.getId();
                    }
                }

            } catch (TwitterException te) {
                System.out.println("Couldn't connect: " + te);
            }
            query.setMaxId(lastID - 1);
        }
        return tweets;
    }
    catch(Exception ex)
    {
        JOptionPane.showMessageDialog(null, ex.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
    }
        return null;
    }
}

