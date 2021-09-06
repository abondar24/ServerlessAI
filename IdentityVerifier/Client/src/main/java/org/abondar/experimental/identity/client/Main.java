package org.abondar.experimental.identity.client;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

public class Main {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.err.println("No file provided for upload");
            System.exit(1);
        }

        var url = "https://9tn5d8t36g.execute-api.eu-west-1.amazonaws.com/test/analysis";
        var client = new IdentityClient(url);
        try {
            System.out.println("Uploading image");
            var id = client.uploadImage(args[0]);
            if (id!=null){
                var result = client.analyseResults(id);
                System.out.println(result);
                saveResult(result);
            }


        } catch (IOException | InterruptedException ex) {
            System.err.println(ex.getMessage());
            System.exit(2);
        }

    }

    private static void saveResult(String result) throws IOException {
        var timeStamp = new Date().getTime();
        var filename = String.format("result_%d.json",timeStamp);
        var file = new File(filename);

        Files.writeString(file.toPath(), result);
    }

}
