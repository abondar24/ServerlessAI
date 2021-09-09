package org.abondar.experimental.feedback.common.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Message {
    String originalText;
    String source;
    String originator;
    String originalLanguage;
    String text;
    String sentiment;
    String sentimentScore;
}
