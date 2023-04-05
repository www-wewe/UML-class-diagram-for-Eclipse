/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Method in class model.
 * 
 * @author Veronika Lenková
 */

public class MethodModel {

    private String name;
    private String returnType;
    private List<ParameterModel> parameters = new ArrayList<>();
	private boolean isStatic = false;
    private boolean isAbstract = false;
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

    public String getParametersToString() {
    	return parameters.stream()
    		      .map(ParameterModel::toString)
    		      .collect(Collectors.joining(", ", "", ""));
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
		return getVisibility().character + (isStatic() ? "{static} " : "") + (isAbstract() ? "{abstract} " : "") + 
				getName() + "(" + getParametersToString() + ")" + (getReturnType() == null ? "" : " : " + getReturnType());
	}

}
