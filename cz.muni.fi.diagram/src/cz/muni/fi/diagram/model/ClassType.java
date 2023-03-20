package cz.muni.fi.diagram.model;

/**
 * Class Type
 * 
 * @author Veronika Lenkova
 */
public enum ClassType {
	ENUM("enum"), CLASS("class"), INTERFACE("interface"), ABSTRACT_CLASS("abstract class");

	public final String character;

	ClassType(String character) {
		this.character = character;
	}
	
}
