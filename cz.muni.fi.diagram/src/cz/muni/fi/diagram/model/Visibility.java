package cz.muni.fi.diagram.model;

/**
 * Visibility of methods and fields.
 * PlantUML syntax.
 * 
 * @author nxf92568
 */
public enum Visibility {
	PRIVATE("-"), PUBLIC("+"), PROTECTED("#"), PACKAGE_PRIVATE("~");

	public final String character;

	Visibility(String character) {
		this.character = character;
	}
}
