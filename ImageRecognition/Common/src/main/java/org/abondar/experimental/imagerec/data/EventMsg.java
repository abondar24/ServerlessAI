package org.abondar.experimental.imagerec.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class EventMsg {

    private String url;

    public EventMsg(String url) {
        this.url = url;
    }
}
