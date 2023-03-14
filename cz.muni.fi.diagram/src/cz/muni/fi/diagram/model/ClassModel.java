package cz.muni.fi.diagram.model;

import java.util.ArrayList;
import java.util.List;

public class ClassModel {
    private String name;
	private String packageName;
	private String superClassName;
    private List<FieldModel> fields;
    private List<MethodModel> methods;
    private List<String> subclasses;
    private List<String> interfaces;
    private boolean isAbstract;
    private boolean isInterface;
    private boolean isEnum;
    private boolean isAnnotation;
    // ... other properties and methods


    public ClassModel() {
    	this.name = null;
    	this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.superClassName = null;
        this.subclasses = new ArrayList<>();
        this.interfaces = new ArrayList<>();
	}

	public String getName() {
        return name;
    }

    // ... other methods for adding and getting properties

	public void setName(String name) {
		this.name = name;
	}

	public void setSuperClassName(String name) {
		this.superClassName = name;
	}

	public void addInterfaceName(String name) {
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

	public List<String> getSubclasses() {
		return subclasses;
	}

	public void setSubclasses(List<String> subclasses) {
		this.subclasses = subclasses;
	}
	
	public void addSubclass(String subclassName) {
		this.subclasses.add(subclassName);
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

	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public boolean isInterface() {
		return isInterface;
	}

	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}

	public boolean isEnum() {
		return isEnum;
	}

	public void setEnum(boolean isEnum) {
		this.isEnum = isEnum;
	}

	public boolean isAnnotation() {
		return isAnnotation;
	}

	public void setAnnotation(boolean isAnnotation) {
		this.isAnnotation = isAnnotation;
	}

	@Override
	public String toString() {
		return "ClassModel [getName()=" + getName() + ", getFields()=" + getFields() + ", getSuperClassName()="
				+ getSuperClassName() + ", getMethods()=" + getMethods() + ", getSubclasses()=" + getSubclasses()
				+ ", getInterfaces()=" + getInterfaces() + ", getPackageName()=" + getPackageName() + ", isAbstract()="
				+ isAbstract() + ", isInterface()=" + isInterface() + ", isEnum()=" + isEnum() + ", isAnnotation()="
				+ isAnnotation() + "]";
	}

    // ... other methods for adding and getting relationships

}

