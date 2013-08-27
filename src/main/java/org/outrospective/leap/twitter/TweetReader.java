package org.outrospective.leap.twitter;

import twitter4j.*;

/**
 * User: kon
 * Date: 25/08/13
 * Time: 4:51 AM
 */
public class TweetReader {
    static Twitter twitter = TwitterFactory.getSingleton();

    public static void main(String[] args) throws TwitterException {
        ResponseList<Status> list = getMelbjvmTweets();
        list.forEach( st -> System.out.printf("%s - %s\n", st.getUser().getName(), st.getText()) );
    }

    public static ResponseList<Status> getMelbjvmTweets() throws TwitterException {
        return twitter.getUserTimeline("melbjvm");
    }
}
