/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.model;

import cz.muni.fi.diagram.parser.IPlantUML;

/**
 * Field in class model.
 * 
 * @author Veronika Lenková
 */

public class FieldModel implements IPlantUML {
	/** Name of the field */
    private String name;
    /** Type of the field */
    private String type;
    /** Decides whether field is static */
	private boolean isStatic = false;
	/** Decides whether method is final */
    private boolean isFinal = false;
    /** Visibility of the field */
    private Visibility visibility = Visibility.PACKAGE_PRIVATE;

    public FieldModel() {
    	// Intentionally empty
	}

	public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public Visibility getVisibility() {
        return visibility;
    }

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

    @Override
	public String toString() {
		return  getVisibility().character + (isStatic() ? "static " : "") + (isFinal() ? "final " : "") + 
				getName() + (getType() == null ? "" : " : " + getType());
	}

	@Override
	public String getPlantUMLString() {
		int indexOfDelimeter = getName().indexOf('=');
		String fieldName = indexOfDelimeter == -1 ? getName() : getName().substring(0, indexOfDelimeter);
		return  getVisibility().character + (isStatic() ? "{static} " : "") + (isFinal() ? "final " : "") + 
				fieldName + (getType() == null ? "" : " : " + getType());
	}
}
