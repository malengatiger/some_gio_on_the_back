package com.boha.geo.datadto;

import com.boha.geo.monitor.data.Section;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Document(collection = "Questionnaire")
public class Questionnaire {

    private String  _partitionKey;
    @Id
    private String  _id;
    private String organizationId;
    private String created;
    private String questionnaireId;
    private String title;
    private String projectId;
    private String description;
    private List<Section> sections;


}
