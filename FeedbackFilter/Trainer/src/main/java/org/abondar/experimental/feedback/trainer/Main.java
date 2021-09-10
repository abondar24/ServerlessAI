package org.abondar.experimental.feedback.trainer;

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
        } else {
            System.err.println("Enter key -e or -c to continue");
        }

    }
}
