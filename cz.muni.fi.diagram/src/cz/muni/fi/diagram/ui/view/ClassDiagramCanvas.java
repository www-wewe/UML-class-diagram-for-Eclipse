/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import cz.muni.fi.diagram.model.ClassModel;
import cz.muni.fi.diagram.ui.image_generator.IClassDiagramImageGenerator;
import cz.muni.fi.diagram.ui.image_generator.PlantUMLImageGenerator;

/**
 * A canvas that displays a UML class diagram.
 * 
 * @author Veronika Lenková
 */
public class ClassDiagramCanvas extends Canvas {
	/** Class diagram **/
	private ClassDiagram classDiagram;
	/** Actually displayed image in canvas **/
	private Image image = null;
	/** Image generator which can generate image from class diagram */
	private IClassDiagramImageGenerator imageGenerator = new PlantUMLImageGenerator();

	/**
     * Creates a new class diagram canvas.
     * 
     * @param parent composite
     * @param classDiagram which have to be displayed
     */
    public ClassDiagramCanvas(final Composite parent, ClassDiagram classDiagram) {
    	super(parent, SWT.NONE);
    	this.classDiagram = classDiagram;
    	addPaintListener(event -> draw(event.gc));
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
		/*Rectangle clientArea = getParent().getClientArea(); // mainComposite
		Color white = getParent().getDisplay().getSystemColor(SWT.COLOR_WHITE);
		gc.setBackground(white);
		gc.fillRectangle(clientArea);*/

    	if (!classDiagram.isEmpty()) {
    		Rectangle canvasBounds = getBounds();
            int imgX = (canvasBounds.width - image.getBounds().width) / 2;
            int imgY = (canvasBounds.height - image.getBounds().height) / 2;
            gc.drawImage(image, imgX, imgY);

            setSize(image.getBounds().width + 50, image.getBounds().height + 50);
            getParent().setSize(image.getBounds().width + 100, image.getBounds().height + 50);
        }
    }

    /**
     * Adds classModel representing one class to class diagram.
     * @param classModel
     * @return true if class was added - false otherwise (class is already in diagram)
     */
	public boolean addClass(ClassModel classModel) {
        // add a new class to the diagram
		if (classDiagram.addClass(classModel)) {
			if (image != null) {
				image.dispose();
			}
			image = imageGenerator.getImage(classDiagram);
			redraw();
			return true;
		}
		return false;
    }

	public Image getImageToExport() {
		return imageGenerator.getImage(classDiagram);
	}

	public void clear() {
		getClassDiagram().clear();
		redraw();
	}

}


