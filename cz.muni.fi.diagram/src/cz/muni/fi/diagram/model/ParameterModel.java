/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.model;

/**
 * One parameter in method model.
 * 
 * @author Veronika Lenkova
 */

public class ParameterModel {
    private String name;
    private String type;

    public ParameterModel() {
		// empty
	}

	public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return getType() + " " + getName();
	}
	
}
