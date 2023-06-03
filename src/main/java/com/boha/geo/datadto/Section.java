package com.boha.geo.datadto;

import com.boha.geo.monitor.data.Question;
import lombok.Data;

import java.util.List;
@Data
public class Section {
    private int  sectionNumber;
    private String  title;
    private String sectionId;
    private String  description;
    private List<Question> questions;


}
