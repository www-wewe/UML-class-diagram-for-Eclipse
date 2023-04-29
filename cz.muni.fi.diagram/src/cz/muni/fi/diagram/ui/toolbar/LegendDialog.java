/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.toolbar;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Dialog with legend of class diagram.
 * 
 * @author Veronika Lenková
 */
class LegendDialog extends Dialog {

	/** Legend of class diagram */
    private Image image;

    /**
     * Constructor.
     * @param shell - main window
     * @param image of legend
     */
    public LegendDialog(Shell shell, Image image) {
        super(shell);
        this.image = image;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new GridLayout(1, false));

        Label legendImage = new Label(container, SWT.NONE);
        legendImage.setImage(image);
        legendImage.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

        return container;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Class diagram legend");
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
    	createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
    }

    @Override
    protected void cancelPressed() {
    	image.dispose();
    	super.cancelPressed();
    }

    @Override
    public boolean close() {
    	image.dispose();
    	return super.close();
    }
}
