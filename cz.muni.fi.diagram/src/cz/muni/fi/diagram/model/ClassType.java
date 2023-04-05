/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.model;

/**
 * Class Type.
 * 
 * @author Veronika Lenková
 */
public enum ClassType {
	ENUM("enum"), CLASS("class"), INTERFACE("interface"), ABSTRACT_CLASS("abstract class");

	public final String character;

	ClassType(String character) {
		this.character = character;
	}
	
}
