/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.view;

import java.util.ArrayList;
import java.util.List;

import cz.muni.fi.diagram.model.ClassModel;

/**
 * Class representing class diagram.
 * 
 * @author Veronika Lenková
 *
 */
public class ClassDiagram {
	/** Classes in class diagram */
    private List<ClassModel> classes;

	private int scaleWidth = -1;
	private int scaleHeight = -1;
	/** Decides if the fields of the diagram should be hidden */
    private boolean hideFields = false;
    /** Decides if the methods of the diagram should be hidden */
    private boolean hideMethods = false;
    /** Decides if the interfaces should be hidden */
    private boolean hideInterface = false;
    /** Decides if the enum classes should be hidden */
    private boolean hideEnum = false;
    /** Decides if the parents should be hidden */
    private boolean hideParent = false;
    /** Decides if the children should be hidden */
    private boolean hideChildren = false;
    /** Decides if the nested classes should be hidden */
    private boolean hideNestedClasses = false;
    /** Decides if the package name should be hidden */
    private boolean hidePackage = true;

	/**
     * Constructor
     */
    public ClassDiagram() {
        this.classes = new ArrayList<>();
    }

    /**
     * Removes all classes from the class diagram.
     */
    public void clear() {
    	classes.clear();
    }

    /**
     * Adds class model to the class diagram.
     * If the class already exists in the class diagram, returns false, true otherwise.
     * @param classModel one class
     * @return true if the class is added false otherwise
     */
    public boolean addClass(ClassModel classModel) {
    	if (classModel == null || classModel.getName() == null) {
			return false;
		}
		if (getClasses().stream().noneMatch(x -> x.getName().equals(classModel.getName()))) {
			classes.add(classModel);
			return true;
		}
		return false;        
    }

    /**
     * Removes class from class diagram.
     * If the class diagram doesn't contain the class, returns false, true otherwise.
     * @param classModel one class
     * @return true if class is removed, false otherwise
     */
    public boolean removeClass(ClassModel classModel) {
    	if (classModel == null || classModel.getName() == null) {
			return false;
		}
		if (getClasses().stream().anyMatch(x -> x.getName().equals(classModel.getName()))) {
			classes.remove(classModel);
			return true;
		}
    	return false;
    }

    /**
     * @return classes in class diagram
     */
    public List<ClassModel> getClasses() {
        return new ArrayList<>(classes);
    }

    /**
     * Sets classes of the class diagram.
     * @param classes to be set
     */
    public void setClasses(List<ClassModel> classes) {
        this.classes = classes;
    }

    /**
     * @return true if the class diagram is empty, false otherwise
     */
    public boolean isEmpty() {
		return getClasses().isEmpty();
	}

    public int getScaleWidth() {
		return scaleWidth;
	}

	public void setScaleWidth(int scale) {
		this.scaleWidth = scale;
	}

	public int getScaleHeight() {
		return scaleHeight;
	}

	public void setScaleHeight(int scale) {
		this.scaleHeight = scale;
	}

	public boolean isHideFields() {
		return hideFields;
	}

	public void setHideFields(boolean hideFields) {
		this.hideFields = hideFields;
	}

	public boolean isHideMethods() {
		return hideMethods;
	}

	public void setHideMethods(boolean hideMethods) {
		this.hideMethods = hideMethods;
	}

	public boolean isHideInterface() {
		return hideInterface;
	}

	public void setHideInterface(boolean hideInterface) {
		this.hideInterface = hideInterface;
	}

	public boolean isHideEnum() {
		return hideEnum;
	}

	public void setHideEnum(boolean hideEnum) {
		this.hideEnum = hideEnum;
	}

    public boolean isHideParent() {
		return hideParent;
	}

	public void setHideParent(boolean hideParent) {
		this.hideParent = hideParent;
	}

	public boolean isHideChildren() {
		return hideChildren;
	}

	public void setHideChildren(boolean hideChildren) {
		this.hideChildren = hideChildren;
	}

	public boolean isHideNestedClasses() {
		return hideNestedClasses;
	}

	public void setHideNestedClasses(boolean hideNestedClasses) {
		this.hideNestedClasses = hideNestedClasses;
	}

	public boolean isHidePackage() {
		return hidePackage;
	}

	public void setHidePackage(boolean hidePackage) {
		this.hidePackage = hidePackage;
	}

}

