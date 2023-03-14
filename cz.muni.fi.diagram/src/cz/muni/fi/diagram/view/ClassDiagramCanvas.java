package cz.muni.fi.diagram.view;

import java.awt.Font;

import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.MouseEvent;

import cz.muni.fi.diagram.model.ClassModel;
import cz.muni.fi.diagram.model.FieldModel;
import cz.muni.fi.diagram.model.MethodModel;

/**
 * A canvas that displays a UML class diagram.
 */
public class ClassDiagramCanvas extends Canvas{
	private ClassDiagram classDiagram;

	/**
     * Creates a new class diagram canvas.
     * 
     * @param parent the parent composite
     */
    public ClassDiagramCanvas(final Composite parent, ClassDiagram classDiagram) {
    	super(parent, SWT.NULL);
    	this.classDiagram = classDiagram;
    	addPaintListener(event -> draw(event.gc));
	}

    /**
     * Sets the class diagram to display on this canvas.
     * 
     * @param classDiagram the class diagram to display
     */
    public void setClassDiagram(ClassDiagram classDiagram) {
        this.classDiagram = classDiagram;
        redraw();
    }

    public void draw(GC gc) {
    	if (classDiagram != null) {
            SWTGraphics graphics = new SWTGraphics(gc);
            gc.setForeground(new Color(255,255,255));
            gc.setLineWidth(5);
            paintClasses(graphics);
            paintRelationships(graphics);
        }
    }

	private void paintClasses(SWTGraphics graphics) {
		// Paint each class on the canvas
        for (ClassObject classObject : classDiagram.getClasses()) {
            // Draw the class rectangle
        	int x = classObject.getX();
            int y = classObject.getY();
            int width = classObject.getWidth();
            int height = classObject.getHeight();

            // draw the class rectangle on the canvas
            graphics.drawRectangle(x, y, width, height);
            //graphics.setFill(new Color(255, 0, 0));
            graphics.fillRectangle(x, y, width, height);

            // Draw the class name and fields/methods inside the rectangle
            // ...
            // draw the class name
            int yOffset = 10;
            ClassModel classModel = classObject.getClassModel();
            graphics.setFont(Fonts.TITLE_FONT); // nefunguje
            graphics.drawString(classModel.getName(), x + 10, y + yOffset);
            yOffset += 20;
            graphics.drawLine(x, y + yOffset, x + width, y + yOffset);
            yOffset += 10;
            // draw each class field and method in the rectangle
            graphics.setFont(Fonts.FIELD_FONT); // nefunguje
            for (FieldModel field : classModel.getFields()) {
            	graphics.drawString(field.toString(), x + 10, y + yOffset);
                yOffset += 20;
            }
            graphics.drawLine(x, y + yOffset, x + width, y + yOffset);
            yOffset += 10;
            graphics.setFont(Fonts.METHOD_FONT); // nefunguje
            for (MethodModel method : classModel.getMethods()) {
            	graphics.drawString(method.toString(), x + 10, y + yOffset);
                yOffset += 20;
            }
        }
	}

	private void paintRelationships(SWTGraphics graphics) {
		// Paint each relationship on the canvas
		for (Relationship relationship : classDiagram.getRelationships()) {
			// calculate the position of the start and end points of the relation
	        ClassObject startClass = relationship.getFromClass();
	        ClassObject endClass = relationship.getToClass();
	        int startX = startClass.getX() + startClass.getWidth() / 2;
	        int startY = startClass.getY() + startClass.getHeight() / 2;
	        int endX = endClass.getX() + endClass.getWidth() / 2;
	        int endY = endClass.getY() + endClass.getHeight() / 2;
	        
	        // draw the relation arrow on the canvas
	        graphics.drawLine(startX, startY, endX, endY);
	        graphics.drawLine(endX, endY, endX - 10, endY - 10);
	        graphics.drawLine(endX, endY, endX - 10, endY + 10);
		}
	}
	
	public void addClass(ClassObject classObject) {
        // add a new class to the diagram
		classDiagram.addClass(classObject);
    }

    // other methods for handling user input, saving and loading the diagram, etc.

}


