package com.boha.geo.monitor.data;

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
