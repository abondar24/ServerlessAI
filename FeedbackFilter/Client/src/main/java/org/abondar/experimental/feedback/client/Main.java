package org.abondar.experimental.feedback.client;

import java.io.IOException;

import static org.abondar.experimental.feedback.common.util.Error.WRONG_CLIENT_KEY;
import static org.abondar.experimental.feedback.common.util.Error.WRONG_NUMBER_OF_ARGUMENTS;


public class Main {
    public static void main(String[] args) {

        var url = "https://avp6698ta4.execute-api.eu-west-1.amazonaws.com/test/feedback";
        var client = new ClassifyClient(url);

        try {
            if (args.length ==2 ){
                if (args[0].equals("-u")){
                    client.uploadFeedback(args[1]);
                } else if ( args[0].equals("-c")){
                    client.getClassifyResults(args[1]);
                } else {
                    System.err.println(WRONG_CLIENT_KEY);
                }
            } else {
                System.err.println(WRONG_NUMBER_OF_ARGUMENTS);
            }
        } catch (InterruptedException | IOException ex){
            System.err.println(ex.getMessage());
        }

    }
}
