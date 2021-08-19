package org.abondar.experimental.schedule.api.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaskResult {

    private String taskId;
    private String taskStatus;
    private String taskUri;
    private String signedUri;
}
