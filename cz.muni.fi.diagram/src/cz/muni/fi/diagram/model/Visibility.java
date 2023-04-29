/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.model;

/**
 * Visibility of methods and fields.
 * PlantUML syntax.
 * 
 * @author Veronika Lenková.
 */
public enum Visibility {
	PRIVATE("-"), PUBLIC("+"), PROTECTED("#"), PACKAGE_PRIVATE("~");

	public final String character;

	private Visibility(String character) {
		this.character = character;
	}
}
