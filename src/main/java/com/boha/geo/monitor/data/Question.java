package com.boha.geo.monitor.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Document(collection = "Question")

public class Question {
    private String  _partitionKey;
    @Id
    private String  _id;

    private String text;
    private List<Answer> answers;
    private String questionType;
    private List<String> choices;


}
