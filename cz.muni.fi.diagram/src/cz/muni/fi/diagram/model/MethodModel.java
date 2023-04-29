/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cz.muni.fi.diagram.parser.IPlantUMLString;

/**
 * Method in class model.
 * 
 * @author Veronika Lenková
 */

public class MethodModel implements IPlantUMLString {

	/** Name of the method */
    private String name;
    /** Return type of the method */
    private String returnType;
    private List<ParameterModel> parameters = new ArrayList<>();
    /** Decides whether method is static */
	private boolean isStatic = false;
	/** Decides whether method is abstract */
    private boolean isAbstract = false;
    /** Visibility of the method */
    private Visibility visibility = Visibility.PUBLIC;

    public MethodModel() {
    	// Intentionally empty
	}

	public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<ParameterModel> getParameters() {
        return parameters;
    }

    public String getParametersToPlantUMLString() {
    	return parameters.stream()
    		      .map(ParameterModel::toString)
    		      .collect(Collectors.joining(", "));
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public Visibility getVisibility() {
        return visibility;
    }

	public void setName(String name) {
		this.name = name;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public void addParameter(ParameterModel paramModel) {
		this.parameters.add(paramModel);
	}
	
	 public void setParameters(List<ParameterModel> parameters) {
			this.parameters = parameters;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

	@Override
	public String toString() {
		return getVisibility().character + (isStatic() ? "static " : "") + (isAbstract() ? "abstract " : "") + 
				getName() + "(" + getParametersToPlantUMLString() + ")" + (getReturnType() == null ? "" : " : " + getReturnType());
	}

	@Override
	public String toPlantUMLString() {
		return getVisibility().character + (isStatic() ? "{static} " : "") + (isAbstract() ? "{abstract} " : "") + 
				getName() + "(" + getParametersToPlantUMLString() + ")" + (getReturnType() == null ? "" : " : " + getReturnType());
	}

}
