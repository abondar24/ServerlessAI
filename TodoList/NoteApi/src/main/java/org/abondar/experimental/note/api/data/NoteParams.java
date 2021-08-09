package org.abondar.experimental.note.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoteParams {

    @JsonProperty("noteLang")
    private String languageCode;


    private MediaFile media;

    @JsonProperty("noteFormat")
    private String mediaFormat;

    @JsonProperty("noteName")
    private String transcriptionJobName;

    @JsonProperty("noteSampleRate")
    private int mediaSampleRateHertz;

    private NoteSettings settings;
}
