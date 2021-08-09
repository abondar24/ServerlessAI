package org.abondar.experimental.note.api.handler;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.transcribe.model.Media;
import com.amazonaws.services.transcribe.model.Settings;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobRequest;
import org.abondar.experimental.note.api.data.MediaFile;
import org.abondar.experimental.note.api.data.NoteParams;
import org.abondar.experimental.note.api.data.NoteSettings;

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

            return buildResponse(200, "Transcribe Job Started");
        } catch (IOException ex) {
            logger.log(ex.getMessage());
            return buildResponse(500, String.format(MSG_FORMAT, MALFORMED_BODY_ERROR));
        } catch (AmazonServiceException ex) {
            logger.log(ex.getMessage());
            return buildResponse(502, String.format(MSG_FORMAT, AWS_NOT_AVAILABLE));
        }
    }

    private StartTranscriptionJobRequest getJobRequest(NoteParams params) {
        var request = new StartTranscriptionJobRequest();
        request.setLanguageCode(params.getLanguageCode());
        request.setMedia(getMedia(params.getMedia()));
        request.setMediaFormat(params.getMediaFormat());
        request.setTranscriptionJobName(params.getTranscriptionJobName());
        request.setMediaSampleRateHertz(params.getMediaSampleRateHertz());
        request.setSettings(getSettings(params.getSettings()));

        return request;
    }

    private Media getMedia(MediaFile mediaFile) {
        var media = new Media();
        media.setMediaFileUri(mediaFile.getMediaFileUri());
        return media;
    }

    private Settings getSettings(NoteSettings noteSettings) {
        var settings = new Settings();
        settings.setChannelIdentification(noteSettings.isChannelIdentification());
        settings.setMaxSpeakerLabels(noteSettings.getMaxSpeakerLabels());
        settings.setShowSpeakerLabels(noteSettings.isShowSpeakerLabels());

        return settings;
    }
}
