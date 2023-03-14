package cz.muni.fi.diagram.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MethodModel {
    private String name;
    private String returnType;
    private List<ParameterModel> parameters;
	private boolean isStatic;
    private boolean isAbstract;
    private Visibility visibility;
    // ... other properties and methods
    
    public MethodModel() {
    	this.name = null;
        this.returnType = null;
        this.parameters = new ArrayList<>();
        this.isStatic = false;
        this.isAbstract = false;
        this.visibility = Visibility.PUBLIC;
	}

    public MethodModel(String name, String returnType, List<ParameterModel> parameters, boolean isStatic, boolean isAbstract, Visibility visibility) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.isStatic = isStatic;
        this.isAbstract = isAbstract;
        this.visibility = visibility;
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

    public String getParametersString() {
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
		return getVisibility() + " " + getName() + "(" + getParametersString() + ") : " + getReturnType();
	}

    // ... other methods for setting and getting properties
}
