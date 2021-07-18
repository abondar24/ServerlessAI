package org.abondar.experimental.todo.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class TodoItem {

    private String id;
    private String dueDate;

    @JsonProperty("action")
    private Boolean checked;

    private String note;

    public TodoItem( Boolean checked, String note) {
        this.checked = checked;
        this.note = note;
    }
}
