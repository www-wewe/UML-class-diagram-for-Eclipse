/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.image_generator;

import org.eclipse.swt.graphics.Image;

import cz.muni.fi.diagram.ui.view.ClassDiagram;

/**
 * Interface for functionality of getting image from ClassDiagram.
 * 
 * @author Veronika Lenková
 */
public interface IClassDiagramImageGenerator {

	/**
	 * Gets new generated image from class diagram.
	 * Image should be after using disposed.
	 * 
	 * @return Image of class diagram
	 */
	public Image getImage(ClassDiagram classDiagram);

	/**
	 * Gets the latest generated image.
	 * Image should be after using disposed.
	 * 
	 * @return Image of class diagram
	 */
	public Image getImage();

	/**
	 * Sets scale of image. Used for zooming.
	 * @param scale to be set
	 */
	public void setScale(double scale);

}
