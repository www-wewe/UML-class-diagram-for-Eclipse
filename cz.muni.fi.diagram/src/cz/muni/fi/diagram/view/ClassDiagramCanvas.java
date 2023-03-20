package cz.muni.fi.diagram.view;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import cz.muni.fi.diagram.model.ClassModel;
import net.sourceforge.plantuml.SourceStringReader;

/**
 * A canvas that displays a UML class diagram.
 * 
 * @author Veronika Lenkova
 */
public class ClassDiagramCanvas extends Canvas {
	/** Class diagram **/
	private ClassDiagram classDiagram;
	/** Actually displayed image in canvas **/
	private Image image = null;

	/**
     * Creates a new class diagram canvas.
     * 
     * @param parent composite
     * @param classDiagram which have to be displayed
     */
    public ClassDiagramCanvas(final Composite parent, ClassDiagram classDiagram) {
    	super(parent, SWT.NONE);
    	this.classDiagram = classDiagram;
    	addPaintListener(event -> {
    		draw(event.gc);
    	});
	}

    /**
     * @param classDiagram to display on canvas
     */
    public void setClassDiagram(ClassDiagram classDiagram) {
        this.classDiagram = classDiagram;
        redraw();
    }

    /**
     * @return classDiagram
     */
    public ClassDiagram getClassDiagram() {
    	return this.classDiagram;
    }

    /**
     * Generates image and draws it to canvas.
     * 
     * @param gc class which draws image
     */
    public void draw(GC gc) {
    	// set white background
		Rectangle clientArea = getClientArea();
		Color white = getParent().getDisplay().getSystemColor(SWT.COLOR_WHITE);
		gc.setBackground(white);
		gc.fillRectangle(clientArea);

    	if (!classDiagram.getClasses().isEmpty()) {
    		image = generateImage();

    		Rectangle canvasBounds = getBounds();
            int imgX = (canvasBounds.width - image.getBounds().width) / 2;
            int imgY = (canvasBounds.height - image.getBounds().height) / 2;
            gc.drawImage(image, imgX, imgY);

    		image.dispose();
            
        }
    }

    /**
     * @return generated image
     */
    @SuppressWarnings("deprecation")
	private Image generateImage() {
		String diagramPlantUMLSource = classDiagram.getPlantUMLSource();
		ByteArrayOutputStream pngStream = new ByteArrayOutputStream();
		SourceStringReader reader = new SourceStringReader(diagramPlantUMLSource);
		try (FileOutputStream fos = new FileOutputStream("mydiagram.png")) {
			reader.generateImage(pngStream);
			fos.write(pngStream.toByteArray());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return new Image(Display.getDefault(), "mydiagram.png");
	}

    /**
     * Adds classModel representing one class to class diagram.
     * @param classModel
     * @return true if class was added - false otherwise (class is already in diagram)
     */
	public boolean addClass(ClassModel classModel) {
        // add a new class to the diagram
		if (classDiagram.addClass(classModel)) {
			redraw();
			return true;
		}
		return false;
    }

	public Image getImage() {
		return image;
	}


	/*private void paintClasses(SWTGraphics graphics) {
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
	}*/

    // other methods for handling user input, saving and loading the diagram, etc.

}


