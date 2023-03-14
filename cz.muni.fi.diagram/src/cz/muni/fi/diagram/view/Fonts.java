package cz.muni.fi.diagram.view;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

public final class Fonts {

	private Fonts() {
		// Empty
	}

	public static final Font TITLE_FONT = JFaceResources.getFontRegistry().get(FontDescriptor.createFrom(new FontData("Arial", 10, SWT.BOLD)).getFontData()[0].toString());
	public static final Font FIELD_FONT = JFaceResources.getFontRegistry().get(FontDescriptor.createFrom(new FontData("Arial", 9, SWT.NONE)).getFontData()[0].toString());
	public static final Font METHOD_FONT = JFaceResources.getFontRegistry().get(FontDescriptor.createFrom(new FontData("Arial", 9, SWT.NONE)).getFontData()[0].toString());
	public static final Font RELATIONSHIP_FONT = JFaceResources.getFontRegistry().get(FontDescriptor.createFrom(new FontData("Arial", 9, SWT.ITALIC)).getFontData()[0].toString());

}
