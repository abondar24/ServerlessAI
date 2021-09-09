package org.abondar.experimental.feedback.translate.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.translate.AmazonTranslateAsync;
import com.amazonaws.services.translate.AmazonTranslateAsyncClient;
import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.experimental.feedback.common.data.Message;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.comprehend.ComprehendAsyncClient;
import software.amazon.awssdk.services.comprehend.model.DetectDominantLanguageRequest;
import software.amazon.awssdk.services.comprehend.model.LanguageCode;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

import static org.abondar.experimental.feedback.common.util.Constant.SENTIMENT_STREAM;

public class TranslateHandler implements RequestHandler<KinesisEvent, Void> {


    private final KinesisAsyncClient kinesisClient;

    private final AmazonTranslateAsync translateClient;

    private final ComprehendAsyncClient comprehendClient;

    private final ObjectMapper mapper;

    public TranslateHandler(){
        this.kinesisClient = buildKinesisClient();

        this.translateClient = buildTranslateClient();

        this.comprehendClient = buildComprehendClient();

        this.mapper = new ObjectMapper();
    }

    private KinesisAsyncClient buildKinesisClient(){
        return KinesisAsyncClient.builder()
                .build();
    }

    private AmazonTranslateAsync buildTranslateClient(){
        return AmazonTranslateAsyncClient.asyncBuilder()
                .build();
    }

    private ComprehendAsyncClient buildComprehendClient(){
        return ComprehendAsyncClient.builder()
                .build();
    }


    @Override
    public Void handleRequest(KinesisEvent input, Context context) {
        var logger = context.getLogger();
        var records = input.getRecords();

        records.forEach(r -> {
            try {
                var payload = r.getKinesis().getData().array();
                var msg = mapper.readValue(payload, Message.class);

                var languageRequest = buildLangRequest(msg);
                var languageResponse = comprehendClient.detectDominantLanguage(languageRequest)
                        .get();

                if (!languageResponse.languages().isEmpty()){
                    var langCode = languageResponse
                            .languages()
                            .get(0)
                            .languageCode();

                    msg.setOriginalLanguage(langCode);
                    if (langCode.equals(LanguageCode.EN.toString())){
                        msg.setText(msg.getOriginalText());
                        pushToSentimentStream(msg);
                    }  else {
                        var translatedText = translateMessage(msg.getOriginalText(),langCode);
                        if (!translatedText.isEmpty()){
                            msg.setText(translatedText);
                            pushToSentimentStream(msg);
                        }
                    }

                }

            } catch (IOException | InterruptedException | ExecutionException ex) {
                logger.log(ex.getMessage());
            }
        });


        return null;
    }

    private DetectDominantLanguageRequest buildLangRequest(Message message){
        return DetectDominantLanguageRequest.builder()
                .text(message.getOriginalText())
                .build();
    }

    private String translateMessage(String text,String langCode) throws InterruptedException,ExecutionException{
        var translateRequest = buildTranslateRequest(text,langCode);
        var translateResult = translateClient.translateTextAsync(translateRequest);
        return translateResult.get().getTranslatedText();
    }

    private TranslateTextRequest buildTranslateRequest(String text, String langCode) {
        var req = new TranslateTextRequest();
        req.setText(text);
        req.setSourceLanguageCode(langCode);
        req.setTargetLanguageCode(LanguageCode.EN.toString());

        return req;
    }

    private void pushToSentimentStream(Message message) throws IOException{
        var body = mapper.writeValueAsString(message);
        var recordRequest = buildRecordRequest(body);

        kinesisClient.putRecord(recordRequest);
    }

    private PutRecordRequest buildRecordRequest(String eventBody) {
        return PutRecordRequest.builder()
                .data(SdkBytes.fromString(eventBody, StandardCharsets.UTF_8))
                .partitionKey("1")
                .streamName(SENTIMENT_STREAM)
                .build();
    }


}
