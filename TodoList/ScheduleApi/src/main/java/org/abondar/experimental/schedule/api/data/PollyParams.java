package org.abondar.experimental.schedule.api.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static org.abondar.experimental.schedule.util.Constants.BUCKET;

@Getter
@Setter
@ToString
public class PollyParams {

    private String outPutFormat;

    private String sampleRate;

    private String text;

    private String languageCode;

    private String textType;

    private String voiceId;

    private String bucket;

    private String keyPrefix;

    public PollyParams(String text){
        this.outPutFormat = "mp3";
        this.sampleRate = "8000";
        this.text = text;
        this.languageCode = "en-GB";
        this.textType = "ssml";
        this.voiceId = "Joanna";
        this.bucket = BUCKET;
        this.keyPrefix = "schedule";
    }

}
