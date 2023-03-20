package cz.muni.fi.diagram.view;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.diagram.model.ClassModel;
import cz.muni.fi.diagram.model.FieldModel;
import cz.muni.fi.diagram.model.MethodModel;
import cz.muni.fi.diagram.model.Relationship;

/**
 * Class representing class diagram.
 * 
 * @author Veronika Lenkova
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

    /**
     * Gets PlantUML source code which will be used for generating image from it.
     * See {@link https://plantuml.com/class-diagram} for more about PlantUML class diagram syntax.
     * 
     * @return PlantUML text
     */
	public String getPlantUMLSource() {
		StringBuilder source = new StringBuilder("@startuml\n");
		StringBuilder relationshipsString = new StringBuilder();
		for (ClassModel classModel : getClasses()) {
			StringBuilder oneClass =  new StringBuilder();
			oneClass.append(classModel.getType().character).append(" ");
			oneClass.append(classModel.getName()).append(" {\n");
			for (FieldModel fieldModel : classModel.getFields() ) {
				oneClass.append("{field}").append(fieldModel).append("\n");
			}
			for (MethodModel methodModel : classModel.getMethods() ) {
				oneClass.append("{method}").append(methodModel).append("\n");
			}
			// Relationships
			String superClass = classModel.getSuperClassName();
			if (superClass != null) {
				relationshipsString.append(classModel.getName()).append(" --|> ").append(superClass).append("\n");
			}
			for (String interfaceClass : classModel.getInterfaces()) {
				relationshipsString.append(classModel.getName()).append(" ..|> ").append(interfaceClass).append("\n");
			}
			for (String subclass : classModel.getSubclasses()) {
				relationshipsString.append(classModel.getName()).append(" --|> ").append(subclass).append("\n");
			}
			oneClass.append("}\r\n");
			source.append(oneClass);
		}
		source.append(relationshipsString);
		source.append("@enduml\n");
		System.out.print(source);
		return source.toString();
	}
}

