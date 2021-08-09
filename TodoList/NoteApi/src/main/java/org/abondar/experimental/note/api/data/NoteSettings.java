package org.abondar.experimental.note.api.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoteSettings {

    private boolean channelIdentification;

    private int maxSpeakerLabels;

    private boolean showSpeakerLabels;

}
