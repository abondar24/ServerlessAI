package org.abondar.experimental.feedback.trainer;

import static org.abondar.experimental.feedback.common.util.Error.WRONG_NUMBER_OF_ARGUMENTS;
import static org.abondar.experimental.feedback.common.util.Error.WRONG_TRAINER_KEY;

public class Main {

    public static void main(String[] args) {
        var trainer = new FeedbackTrainer();

        if (args.length == 2 && args[0].equals("-e")) {
            trainer.createClassifierEndpoint(args[1]);
        } else if (args.length ==1 && args[0].equals("-c")) {

            var classifierArn = trainer.createDocumentClassifier();
            if (classifierArn.isEmpty()){
                System.exit(1);
            }
            System.out.printf("Created classifier with arn %s\n", classifierArn);
        } else if (args.length==0){
            System.err.println(WRONG_NUMBER_OF_ARGUMENTS);
        } else {
            System.err.println(WRONG_TRAINER_KEY);
        }

    }
}
