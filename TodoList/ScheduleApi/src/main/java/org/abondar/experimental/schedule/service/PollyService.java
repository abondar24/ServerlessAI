package org.abondar.experimental.schedule.service;

import org.abondar.experimental.schedule.api.data.TaskResult;

public interface PollyService {

    TaskResult startTask(String text);
    TaskResult pollTask(String taskId);
}
