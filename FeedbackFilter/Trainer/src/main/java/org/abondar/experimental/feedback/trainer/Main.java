package org.abondar.experimental.feedback.trainer;

public class Main {

    public static void main(String[] args) {
        var trainer = new FeedbackTrainer();
        var classifierArn = trainer.createDocumentClassifier();

        if (classifierArn.isEmpty()){
            System.exit(1);
        }

        System.out.printf("Created classifier with arn %s\n", classifierArn);
        trainer.createClassifierEndpoint(classifierArn);
    }
}
