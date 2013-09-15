package org.outrospective.leap.twitter

import org.junit.Test

/**
 * User: kon
 * Date: 15/09/13
 * Time: 10:17 PM
 */
class TweetReaderTest {

    @Test
    void testGetMyTweetsPage1() {
        def tweets = TweetReader.getMyTweets(1)
        assert tweets.size() > 0
        tweets.each { println it }
    }

    @Test
    void testGetMyTweetsPage2() {
        def tweets = TweetReader.getMyTweets(2)
        assert tweets.size() > 0
        tweets.each { println it }
    }
}
