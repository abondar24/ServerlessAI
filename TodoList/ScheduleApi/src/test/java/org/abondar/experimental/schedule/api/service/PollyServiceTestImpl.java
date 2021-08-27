package org.abondar.experimental.schedule.api.service;

import org.abondar.experimental.schedule.api.data.TaskResult;
import org.abondar.experimental.schedule.service.PollyService;

public class PollyServiceTestImpl implements PollyService {

    @Override
    public TaskResult startTask(String text) {
        return new TaskResult("id","status","uri");
    }

    @Override
    public TaskResult pollTask(String taskId) {
        var res = new TaskResult("id","status","uri");
        res.setSignedUrl("url");
        return res;
    }
}
