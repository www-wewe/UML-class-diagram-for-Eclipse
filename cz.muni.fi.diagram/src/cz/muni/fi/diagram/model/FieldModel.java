package cz.muni.fi.diagram.model;

public class FieldModel {
    private String name;
    private String type;
	private boolean isStatic;
    private boolean isFinal;
    private Visibility visibility;
    // ... other properties and methods

    public FieldModel() {
    	this.name = null;
        this.type = null;
        this.isStatic = false;
        this.isFinal = false;
        this.visibility = Visibility.PUBLIC;
	}

	public FieldModel(String name, String type, boolean isStatic, boolean isFinal, Visibility visibility) {
        this.name = name;
        this.type = type;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.visibility = visibility;
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

    // ... other methods for setting and getting properties

    @Override
	public String toString() {
    	
		return  getVisibility() + " " + (isStatic() ? "static " : "") + (isFinal() ? "final " : "") + 
				getType() + " " + getName();
	}
}
