/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.toolbar;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;


import cz.muni.fi.diagram.model.ClassModel;
import cz.muni.fi.diagram.ui.view.ClassDiagram;
import cz.muni.fi.diagram.ui.view.ClassDiagramCanvas;

/**
 * Dialog for managing class diagram.
 * 
 * @author Veronika Lenková
 */
public class ManageDiagramDialog extends Dialog {

	/** Canvas with class diagram */
	private ClassDiagramCanvas classDiagramCanvas;
	/** Class diagram with class models */
	private ClassDiagram classDiagram;
	/** Classes of class diagram */
	private List<ClassModel> classModels;
	/** List viewer with class models */
    private ListViewer listViewer;
    
    Button checkboxHideField;
    Button checkboxHideMethod;
    // TODO
    Button checkboxHideInterface;
    // TODO
    Button checkboxHideChildren;
    // TODO
    Button checkboxHideParent;
    Button checkboxHidePackage;
    Text scaleWidthText;
    Text scaleHeightText;

    /**
     * Constructor.
     * @param shell main window
     * @param classDiagramCanvas with
     */
	public ManageDiagramDialog(Shell shell, ClassDiagramCanvas classDiagramCanvas) {
		super(shell);
		this.classDiagramCanvas = classDiagramCanvas;
		this.classDiagram = classDiagramCanvas.getClassDiagram();
		if (classDiagram == null) {
			classDiagram = new ClassDiagram();
		}
		this.classModels = classDiagram.getClasses();
	}

	@Override
    protected Control createDialogArea(Composite parent) {
        Composite dialog = (Composite) super.createDialogArea(parent);
        dialog.setLayout(new GridLayout(1, false));

        listViewer = new ListViewer(dialog, SWT.BORDER | SWT.V_SCROLL);
        listViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        listViewer.setContentProvider(ArrayContentProvider.getInstance());
        listViewer.setInput(classModels);

        Button deleteButton = new Button(dialog, SWT.PUSH);
        deleteButton.setText("Delete");
        deleteButton.setEnabled(false);
        deleteButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                IStructuredSelection selection = listViewer.getStructuredSelection();
                if (!selection.isEmpty()) {
                    ClassModel selectedModel = (ClassModel) selection.getFirstElement();
                    classModels.remove(selectedModel);
                    listViewer.setInput(classModels);
                    deleteButton.setEnabled(!listViewer.getStructuredSelection().isEmpty());
                }
            }
        });
        Button removeAll = new Button(dialog, SWT.PUSH);
        removeAll.setText("Delete All");
        removeAll.setEnabled(!classModels.isEmpty());
        removeAll.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                classModels.clear();
                listViewer.setInput(classModels);
            }
        });

        addCheckboxes(dialog);
        listViewer.addSelectionChangedListener(event-> {
        	deleteButton.setEnabled(!listViewer.getStructuredSelection().isEmpty());
        	removeAll.setEnabled(!listViewer.getStructuredSelection().isEmpty());
        });
        return dialog;
    }

	private void addCheckboxes(Composite dialog) {
		checkboxHideField = new Button(dialog, SWT.CHECK);
        checkboxHideField.setText("Hide fields");
        checkboxHideField.setSelection(classDiagram.isHideFields());

        checkboxHideMethod = new Button(dialog, SWT.CHECK);
        checkboxHideMethod.setText("Hide methods");
        checkboxHideMethod.setSelection(classDiagram.isHideMethods());

        /*checkboxHideInterface = new Button(dialog, SWT.CHECK);
        checkboxHideInterface.setText("Hide interfaces");
        checkboxHideInterface.setSelection(classDiagram.isHideInterface());

        checkboxHideChildren = new Button(dialog, SWT.CHECK);
        checkboxHideChildren.setText("Hide children");
        checkboxHideChildren.setSelection(classDiagram.isHideSubclass());
        
        checkboxHideParent = new Button(dialog, SWT.CHECK);
        checkboxHideParent.setText("Hide parents");
        checkboxHideParent.setSelection(classDiagram.isHideParent());*/

        checkboxHidePackage = new Button(dialog, SWT.CHECK);
        checkboxHidePackage.setText("Hide packages");
        checkboxHidePackage.setSelection(classDiagram.isHidePackage());

        Label width = new Label(dialog, SWT.NONE);
        width.setText("Scale width:");
        scaleWidthText = new Text(dialog, SWT.BORDER);
        if (classDiagram.getScaleWidth() != -1) {        	
        	scaleWidthText.setText(String.valueOf(classDiagram.getScaleWidth()));
        }

        Label height = new Label(dialog, SWT.NONE);
        height.setText("Scale height:");
        scaleHeightText = new Text(dialog, SWT.BORDER);
        if (classDiagram.getScaleHeight() != -1) {        	
        	scaleHeightText.setText(String.valueOf(classDiagram.getScaleHeight()));
        }
	}

    @Override
    protected void okPressed() {
    	classDiagram.setClasses(classModels);

    	classDiagram.setHideFields(checkboxHideField.getSelection());
    	classDiagram.setHideMethods(checkboxHideMethod.getSelection());
    	//classDiagram.setHideInterface(checkboxHideInterface.getSelection());
    	//classDiagram.setHideParent(checkboxHideParent.getSelection());
    	//classDiagram.setHideChildren(checkboxHideChildren.getSelection());
    	classDiagram.setHidePackage(checkboxHidePackage.getSelection());

    	if (check(scaleHeightText)) {
    		classDiagram.setScaleHeight(Integer.parseInt(scaleHeightText.getText()));
    	}
    	if (check(scaleHeightText)) {    		
    		classDiagram.setScaleWidth(Integer.parseInt(scaleWidthText.getText()));
    	}
    	
    	classDiagramCanvas.setClassDiagram(classDiagram);
    	super.okPressed();
    }

    /**
     * Checks the value provided by user.
     * @param scale have to be number
     * @return true if scale is real
     */
    private boolean check(Text scale) {
    	int scaleNumber;
    	try {    		
    		scaleNumber = Integer.parseInt(scale.getText());
    	}
    	catch (NumberFormatException e) {
    		return false;
    	}
    	return scaleNumber > 100;
	}

	@Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Manage Classes Dialog");
    }

}
