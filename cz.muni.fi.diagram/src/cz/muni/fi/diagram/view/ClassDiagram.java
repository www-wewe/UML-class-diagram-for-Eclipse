package cz.muni.fi.diagram.view;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.diagram.model.ClassModel;

public class ClassDiagram {
    private String packageName;
    private List<ClassObject> classes;
    private List<Relationship> relationships;

    public ClassDiagram() {
        this.classes = new ArrayList<>();
        this.relationships = new ArrayList<>();
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void addClass(ClassObject classObject) {
        classes.add(classObject);
    }

    public List<ClassObject> getClasses() {
        return classes;
    }

    public void addRelationship(Relationship relationship) {
        relationships.add(relationship);
    }

    public List<Relationship> getRelationships() {
        return relationships;
    }
}

