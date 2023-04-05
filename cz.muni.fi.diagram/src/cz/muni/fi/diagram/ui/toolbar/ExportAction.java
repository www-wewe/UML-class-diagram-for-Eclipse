/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.toolbar;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import cz.muni.fi.diagram.ui.view.ClassDiagramCanvas;

/**
 * Action for export image of diagram to .PNG file.
 * 
 * @author Veronika Lenková
 */
public class ExportAction extends Action {
	
	ClassDiagramCanvas classDiagramCanvas;
	private final Shell shell;
    
    public ExportAction(Shell shell, ClassDiagramCanvas classDiagramCanvas) {
        setText("Export");
        setToolTipText("Export diagram");
        this.classDiagramCanvas = classDiagramCanvas;
        this.shell = shell;
    }

    @Override
    public void run() {
    	Image image = classDiagramCanvas.getImageToExport();
        ImageLoader imageLoader = new ImageLoader();
        imageLoader.data = new ImageData[] { image.getImageData() };

        // Prompt the user for an output path
        String outputPath = openFileDialog();
        if (outputPath != null) {
            // Save the content of the canvas as a PNG file
        	imageLoader.save(outputPath, SWT.IMAGE_PNG);
        }
        image.dispose();
    }

    public String openFileDialog() {
        FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
        fileDialog.setFilterNames(new String[] { "PNG Images (*.png)", "All Files (*.*)" });
        fileDialog.setFilterExtensions(new String[] { "*.png", "*.*" });
        fileDialog.setFilterIndex(0);
        fileDialog.setOverwrite(true);
        return fileDialog.open();
    }
}