/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.model;

import cz.muni.fi.diagram.parser.IPlantUML;

/**
 * One parameter in method model.
 * 
 * @author Veronika Lenková
 */

public class ParameterModel implements IPlantUML {
	/** Name of the parameter */
    private String name;
    /** Type of the parameter */
    private String type;

    public ParameterModel() {
		// Intentionally empty
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

	@Override
	public String getPlantUMLString() {
		return getType() + " " + getName();
	}
	
}
