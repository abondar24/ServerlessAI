package org.abondar.experimental.schedule.service.impl;


import org.abondar.experimental.schedule.api.data.TaskResult;
import org.abondar.experimental.schedule.service.PollyService;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.polly.PollyClient;
import software.amazon.awssdk.services.polly.model.GetSpeechSynthesisTaskRequest;
import software.amazon.awssdk.services.polly.model.StartSpeechSynthesisTaskRequest;
import software.amazon.awssdk.services.polly.model.SynthesisTask;
import software.amazon.awssdk.services.s3.S3Client;


import static org.abondar.experimental.schedule.util.Constants.BUCKET;

public class PollyServiceImpl implements PollyService {

    private final PollyClient polly;

    private final S3Client s3;

    public PollyServiceImpl() {
        polly = PollyClient.builder()
                .region(Region.EU_WEST_1)
                .build();

        s3 = S3Client.builder()
                .region(Region.EU_WEST_1)
                .build();
    }

    @Override
    public TaskResult startTask(String text) {
        var req = buildStartSpeechRequest(text);
        var resp = polly.startSpeechSynthesisTask(req);
        return buildTaskResult(resp.synthesisTask(),false);
    }

    private StartSpeechSynthesisTaskRequest buildStartSpeechRequest(String text) {
        return StartSpeechSynthesisTaskRequest
                .builder()
                .outputFormat("mp3")
                .sampleRate("8000")
                .text(text)
                .textType("en-US")
                .voiceId("Joanna")
                .outputS3BucketName(BUCKET)
                .outputS3KeyPrefix("schedule")
                .build();
    }


    @Override
    public TaskResult pollTask(String taskId) {
        var req = getTaskSynthesisRequest(taskId);
        var resp = polly.getSpeechSynthesisTask(req);
        return buildTaskResult(resp.synthesisTask(),true);
    }

    private GetSpeechSynthesisTaskRequest getTaskSynthesisRequest(String taskId) {
        return GetSpeechSynthesisTaskRequest.builder()
                .taskId(taskId)
                .build();
    }

    private TaskResult buildTaskResult(SynthesisTask task, boolean urlRequired) {
        var res = new TaskResult(task.taskId(),task.taskStatusAsString(),task.outputUri());

        if (urlRequired){
            res.setSignedUrl(getSignetUrl(task.taskId()));
        }

        return res;
    }

    private String getSignetUrl(String taskId) {
        var key = String.format("schedule.%s.mp3", taskId);

        return s3.utilities()
                .getUrl(builder -> builder.bucket(BUCKET).key(key))
                .toExternalForm();

    }

}
