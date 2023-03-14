package cz.muni.fi.diagram.view;

import cz.muni.fi.diagram.model.ClassModel;

public class ClassObject {

    private ClassModel classModel;
    private int x;
    private int y;
    private int width;
    private int height;

    public ClassObject(ClassModel classModel, int x, int y, int width, int height) {
        this.classModel = classModel;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public ClassModel getClassModel() {
        return classModel;
    }

    public void setClassModel(ClassModel classModel) {
        this.classModel = classModel;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

	public String getClassName() {
		return classModel.getName();
	}

}
