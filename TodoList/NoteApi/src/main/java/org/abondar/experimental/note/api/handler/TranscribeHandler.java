package org.abondar.experimental.note.api.handler;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import org.abondar.experimental.note.api.data.NoteParams;
import org.abondar.experimental.note.api.data.NoteSettings;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.transcribe.model.Media;
import software.amazon.awssdk.services.transcribe.model.Settings;
import software.amazon.awssdk.services.transcribe.model.StartTranscriptionJobRequest;


import java.io.IOException;

import static org.abondar.experimental.note.api.util.Errors.AWS_NOT_AVAILABLE;
import static org.abondar.experimental.note.api.util.Errors.MALFORMED_BODY_ERROR;
import static org.abondar.experimental.note.api.util.Errors.MSG_FORMAT;

public class TranscribeHandler extends NoteHandler
        implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        var logger = context.getLogger();

        var body = input.getBody();
        logger.log(body);

        try {
            var noteParams = mapper.readValue(body, NoteParams.class);
            client.startTranscriptionJob(getJobRequest(noteParams));

            return buildResponse(200, mapper.writeValueAsString("Transcribe Job Started"));
        } catch (IOException ex) {
            logger.log(ex.getMessage());
            return buildResponse(500, String.format(MSG_FORMAT, MALFORMED_BODY_ERROR));
        } catch (AwsServiceException ex) {
            logger.log(ex.getMessage());
            return buildResponse(502, String.format(MSG_FORMAT,AWS_NOT_AVAILABLE));
        }
    }

    private StartTranscriptionJobRequest getJobRequest(NoteParams params) {
        var settings = params.getSettings()!=null ?  getSettings(params.getSettings()) :getDefaultSettings();

        return StartTranscriptionJobRequest.builder()
                .languageCode(params.getLanguageCode())
                .outputBucketName(BUCKET)
                .media(getMedia(params.getMediaFileUri()))
                .mediaFormat(params.getMediaFormat())
                .settings(settings)
                .transcriptionJobName(params.getTranscriptionJobName())
                .mediaSampleRateHertz(params.getMediaSampleRateHertz())
                .build();

    }

    private Media getMedia(String mediaFileUri) {
       return Media.builder()
                .mediaFileUri(mediaFileUri)
                .build();
    }

    private Settings getSettings(NoteSettings noteSettings) {
        return Settings.builder()
                .channelIdentification(noteSettings.isChannelIdentification())
                .maxSpeakerLabels(noteSettings.getMaxSpeakerLabels())
                .showSpeakerLabels(noteSettings.isShowSpeakerLabels())
                .build();
    }

    private Settings getDefaultSettings() {
        return Settings.builder()
                .channelIdentification(false)
                .maxSpeakerLabels(4)
                .showSpeakerLabels(true)
                .build();
    }
}
