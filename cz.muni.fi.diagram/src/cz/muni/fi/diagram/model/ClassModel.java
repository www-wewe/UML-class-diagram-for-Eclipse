/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;

import cz.muni.fi.diagram.parser.ClassModelParser;

/**
 * Model of one class in class diagram.
 * 
 * @author Veronika Lenková
 */
public class ClassModel {
	ClassType type;
	private String name;
	private String packageName;
	private String superClassName;
    private List<FieldModel> fields = new ArrayList<>();
    private List<MethodModel> methods = new ArrayList<>();
    private List<ClassModel> nestedClasses = new ArrayList<>();
    private List<String> subClasses = new ArrayList<>();
    private List<String> interfaces = new ArrayList<>();

    public ClassModel() {
    	// Intentionally empty
	}

    /**
	 * Creates Class Model from CompilationUnit
	 * @param unit
	 * @return Class Model
	 */
	public static ClassModel create(ICompilationUnit unit) {
		return ClassModelParser.createClassModel(unit);
	}

	public void setType(ClassType type) {
		this.type = type;
	}

    public ClassType getType() {
		return type;
	}

	public String getName() {
        return name;
    }

	public void setName(String name) {
		this.name = name;
	}

	public void setSuperClassName(String name) {
		this.superClassName = name;
	}

	public void addInterface(String name) {
		this.interfaces.add(name);
	}

	public void addMethod(MethodModel methodModel) {
		this.methods.add(methodModel);
	}
	
    public void addField(FieldModel field) {
        fields.add(field);
    }

    public List<FieldModel> getFields() {
        return fields;
    }
    
    public String getSuperClassName() {
		return superClassName;
	}

	public List<MethodModel> getMethods() {
		return methods;
	}

	public void setMethods(List<MethodModel> methods) {
		this.methods = methods;
	}

	public List<ClassModel> getNestedClasses() {
		return nestedClasses;
	}

	public void setNestedClasses(List<ClassModel> subclasses) {
		this.nestedClasses = subclasses;
	}

	public List<String> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(List<String> interfaces) {
		this.interfaces = interfaces;
	}

	public void setFields(List<FieldModel> fields) {
		this.fields = fields;
	}
	
    public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public List<String> getSubClasses() {
		return subClasses;
	}

	public void setSubClasses(List<String> subClasses) {
		this.subClasses = subClasses;
	}

	public void addSubClass(String classModel) {
		this.subClasses.add(classModel);
	}

	@Override
	public String toString() {
		return getName();
	}
}

