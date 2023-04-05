/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.image_generator;

import org.eclipse.swt.graphics.Image;

import cz.muni.fi.diagram.ui.view.ClassDiagram;

/**
 * Interface for functionality of getting image from ClassDiagram
 * @author Veronika Lenková
 *
 */
public interface IClassDiagramImageGenerator {

	/**
	 * @param classDiagram
	 * @return Image of class diagram
	 */
	public Image getImage(ClassDiagram classDiagram);
}
