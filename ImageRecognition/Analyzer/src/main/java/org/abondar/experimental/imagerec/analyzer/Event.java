package org.abondar.experimental.imagerec.analyzer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Event {
    private String action;

    private EventMsg msg;
}
