package org.abondar.experimental.note.api;

import software.amazon.awssdk.services.transcribe.TranscribeClient;

public class TestTranscribeClient implements TranscribeClient {
    @Override
    public String serviceName() {
        return null;
    }

    @Override
    public void close() {

    }
}
