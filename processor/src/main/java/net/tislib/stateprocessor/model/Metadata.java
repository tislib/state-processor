package net.tislib.stateprocessor.model;

import lombok.Data;

import java.util.Map;

@Data
public class Metadata {

    private String name;

    private Map<String, String> labels;

}
