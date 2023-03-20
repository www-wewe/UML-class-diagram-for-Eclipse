package cz.muni.fi.diagram.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model of one class in class diagram.
 * 
 * @author Veronika Lenkova
 */
public class ClassModel {
	ClassType type;
	private String name;
	private String packageName;
	private String superClassName;
    private List<FieldModel> fields;
    private List<MethodModel> methods;
    private List<String> subclasses;
    private List<String> interfaces;

    public ClassModel() {
    	this.name = null;
    	this.type = null;
    	this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.superClassName = null;
        this.subclasses = new ArrayList<>();
        this.interfaces = new ArrayList<>();
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

	@Override
	public String toString() {
		return "Type=" + getType() + "Name=" + getName() + ", Fields=" + getFields() + ", SuperClassName="
				+ getSuperClassName() + ", Methods=" + getMethods() + ", Subclasses=" + getSubclasses()
				+ ", Interfaces=" + getInterfaces() + ", PackageName()=" + getPackageName();
	}

}

