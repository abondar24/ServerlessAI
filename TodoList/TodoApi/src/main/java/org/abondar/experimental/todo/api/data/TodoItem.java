package org.abondar.experimental.todo.api.data;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static org.abondar.experimental.todo.api.constant.Constants.TABLE_NAME;

@NoArgsConstructor
@Getter
@Setter
@ToString
@DynamoDBTable(tableName = TABLE_NAME)
public class TodoItem {

    @DynamoDBHashKey(attributeName = "id")
    private String id;

    private String dueDate;

    private String action;

    @JsonProperty("stat")
    private Boolean checked;

    private String note;

    public TodoItem(Boolean checked, String note) {
        this.checked = checked;
        this.note = note;
    }
}
