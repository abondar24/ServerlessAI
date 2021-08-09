package org.abondar.experimental.note.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MediaFile {
    @JsonProperty("noteUri")
    private String mediaFileUri;
}
