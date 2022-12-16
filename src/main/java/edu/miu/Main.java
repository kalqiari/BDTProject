package edu.miu;

import edu.miu.producer.Producer;

public class Main
{
    public static void main( String[] args ) throws Exception {
        Producer twitterProducer = new Producer();
        twitterProducer.run();
    }
}
