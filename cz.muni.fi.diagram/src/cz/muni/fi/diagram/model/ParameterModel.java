package cz.muni.fi.diagram.model;

public class ParameterModel {
    private String name;
    private String type;
    // ... other properties and methods

    public ParameterModel(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public ParameterModel() {
		// TODO Auto-generated constructor stub
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

    // ... other methods for setting and getting properties
	
}
