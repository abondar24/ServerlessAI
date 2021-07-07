package org.abondar.experimental.imagerec.data;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ImageLabel {


    @JsonProperty("Name")
    private String name;

    @JsonProperty("Confidence")
    private Float confidence;

    public ImageLabel(String name, Float confidence) {
        this.name = name;
        this.confidence = confidence;
    }
}
