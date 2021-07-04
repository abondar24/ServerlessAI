package org.abondar.experimental.imagerec.analyzer.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ImageResult {

    private String image;

    private List<ImageLabel> labels;

    public ImageResult(String image) {
        this.image = image;
    }
}
