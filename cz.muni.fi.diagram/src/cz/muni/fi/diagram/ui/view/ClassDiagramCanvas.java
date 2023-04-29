/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.view;

import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
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
	/** Zoom scale */
	private double scale = 1.0;
	/** Toolbar actions whose visibility is changing according to content of canvas */
	private List<IAction> toolbarActions;

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
     * Generates image and draws it to canvas.
     * 
     * @param gc class which draws image
     */
    public void draw(GC gc) {
    	if (!classDiagram.isEmpty() && image != null && !image.isDisposed()) {
    		int width = (image.getBounds().width > ClassDiagramView.MIN_WIDTH) ? image.getBounds().width : ClassDiagramView.MIN_WIDTH;
    		int height = (image.getBounds().height > ClassDiagramView.MIN_HEIGHT) ? image.getBounds().height : ClassDiagramView.MIN_HEIGHT;

    		setSize(width, height);
    		getParent().setSize(width, height);
    		((ScrolledComposite) getParent().getParent()).setMinSize(width, height);

    		gc.drawImage(image, 0, 0);
    		setEnabledActions(true);
        }
    	else {
    		gc.drawText(text, 5, 5, true);
    		setSize(ClassDiagramView.MIN_WIDTH, ClassDiagramView.MIN_HEIGHT);
            getParent().setSize(ClassDiagramView.MIN_WIDTH, ClassDiagramView.MIN_HEIGHT);
            setEnabledActions(false);
    	}
    }

	/**
     * Adds classModel representing one class to class diagram.
     * 
     * @param classModel to be added
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
		return imageGenerator.getImage();
	}

	/**
	 * Clears the class diagram and generates new empty image that will be displayed.
	 */
	public void clear() {
		getClassDiagram().clear();
		generateImage();
		redraw();
	}

	/**
	 * Zoom image in.
	 */
	public void zoomIn() {
		scale += 0.1;
		imageGenerator.setScale(scale);
		generateImage();
		redraw();
	}

	/**
	 * Zoom image out.
	 */
	public void zoomOut() {
		scale -= 0.1;
		imageGenerator.setScale(scale);
		generateImage();
		redraw();
	}

	/**
	 * Sets visibility of toolbar actions.
	 * @param enabled - visibility
	 */
    private void setEnabledActions(boolean enabled) {
		for (IAction action : toolbarActions) {
			action.setEnabled(enabled);
		}
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
	 * Adds toolbar actions.
	 * @param toolbarActions to be added
	 */
	public void addToolbarActions(List<IAction> toolbarActions) {
		this.toolbarActions = toolbarActions;
	}
}
