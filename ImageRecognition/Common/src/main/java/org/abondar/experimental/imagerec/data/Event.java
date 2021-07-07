package org.abondar.experimental.imagerec.data;

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

    public Event(String action, EventMsg msg) {
        this.action = action;
        this.msg = msg;
    }
}
