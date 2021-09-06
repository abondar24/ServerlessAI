package org.abondar.experimental.schedule.api.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TaskResult {

    private String taskId;
    private String taskStatus;
    private String taskUri;
    private String signedUrl;

    public TaskResult(String taskId, String taskStatus, String taskUri) {
        this.taskId = taskId;
        this.taskStatus = taskStatus;
        this.taskUri = taskUri;
    }
}
