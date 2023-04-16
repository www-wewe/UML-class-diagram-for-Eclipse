/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.model;

import cz.muni.fi.diagram.parser.IPlantUMLString;

/**
 * Field in class model.
 * 
 * @author Veronika Lenková
 */

public class FieldModel implements IPlantUMLString {
    private String name;
    private String type;
	private boolean isStatic = false;
    private boolean isFinal = false;
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
	public String toPlantUMLString() {
		int indexOfDelimeter = getName().indexOf('=');
		String fieldName = indexOfDelimeter == -1 ? getName() : getName().substring(0, indexOfDelimeter);
		return  getVisibility().character + (isStatic() ? "{static} " : "") + (isFinal() ? "final " : "") + 
				fieldName + (getType() == null ? "" : " : " + getType());
	}
}
