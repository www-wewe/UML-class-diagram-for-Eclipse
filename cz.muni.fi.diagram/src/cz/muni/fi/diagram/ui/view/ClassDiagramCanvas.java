/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
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
	/** Text displayed in the canvas when the class diagram is empty */
	private String text = "Drop a Java class here to generate a class diagram.";

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
        generateImage();
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

            setSize(image.getBounds().width, image.getBounds().height);
            getParent().setSize(image.getBounds().width, image.getBounds().height);
            //getParent().getParent().setSize(image.getBounds().width + 100, image.getBounds().height + 50);
            ((ScrolledComposite) getParent().getParent()).setMinSize(image.getBounds().width, image.getBounds().height);
        }
    	else {
    		gc.drawText(text, 5, 5, true);
    		setSize(400, 750);
            getParent().setSize(400, 750);
    	}
    }

    /**
     * Adds classModel representing one class to class diagram.
     * 
     * @param classModel
     * @return true if class was added - false otherwise (class is already in diagram)
     */
	public boolean addClass(ClassModel classModel) {
		if (classModel == null) {
			return false;
		}
		if (classDiagram.addClass(classModel)) {
			generateImage();
			redraw();
			return true;
		}
		return false;
    }

	/**
	 * Generates image and stores it in {@code image}.
	 */
	private void generateImage() {
		if (image != null) {
			image.dispose();
		}
		image = imageGenerator.getImage(classDiagram);
	}

	/**
	 * @return Image to be exported
	 */
	public Image getImageToExport() {
		return imageGenerator.getImage(classDiagram);
	}

	/**
	 * Clears the class diagram and generates new empty image that will be displayed.
	 */
	public void clear() {
		getClassDiagram().clear();
		generateImage();
		redraw();
	}

}
