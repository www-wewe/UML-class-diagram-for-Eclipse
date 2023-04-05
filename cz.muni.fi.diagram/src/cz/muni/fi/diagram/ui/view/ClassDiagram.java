/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.view;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.diagram.model.ClassModel;
import cz.muni.fi.diagram.model.FieldModel;
import cz.muni.fi.diagram.model.MethodModel;
import cz.muni.fi.diagram.model.Relationship;

/**
 * Class representing class diagram.
 * 
 * @author Veronika Lenková
 *
 */
public class ClassDiagram {
    private List<ClassModel> classes;
    // delete TODO
    private List<Relationship> relationships;
    private String packageName;

    public ClassDiagram() {
        this.classes = new ArrayList<>();
        this.relationships = new ArrayList<>();
    }

    public void clear() {
    	classes.clear();
    	relationships.clear();
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean addClass(ClassModel classModel) {
    	if (classModel.getName() == null) {
			return false;
		}
		if (getClasses().stream().noneMatch(x -> x.getName().equals(classModel.getName()))) {
			classes.add(classModel);
			return true;
		}
		return false;        
    }

    public List<ClassModel> getClasses() {
        return classes;
    }

    public void addRelationship(Relationship relationship) {
        relationships.add(relationship);
    }

    public List<Relationship> getRelationships() {
        return relationships;
    }

    public boolean isEmpty() {
		return getClasses().isEmpty();
	}

}

