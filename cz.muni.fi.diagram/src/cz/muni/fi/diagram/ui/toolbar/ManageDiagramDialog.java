/** Copyright (c) 2023, Veronika Lenková */
package cz.muni.fi.diagram.ui.toolbar;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
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

    /** Button for hide fields */
    private Button checkboxHideField;
    /** Button for hide methods */
    private Button checkboxHideMethod;
    /** Button for hide interfaces */
    private Button checkboxHideInterface;
    /** Button for hide children */
    private Button checkboxHideChildren;
    /** Button for hide parents */
    private Button checkboxHideParent;
    /** Button for hide packages */
    private Button checkboxHidePackage;
    /** Button for hide packages */
    private Button checkboxHideNestedClasses;

	/** Button for removing all classes */
	private Button removeAllButton;
	/** Button for removing one class */
	private Button removeButton;
	/** Button for moving a function up */
	private Button upButton;
	/** Button for moving a function down */
	private Button downButton;

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
        try {
			createHeaderComposite(dialog);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
        addListViewer(dialog);
        addCheckboxes(dialog);
        return dialog;
    }

	/**
	 * Add list viewer with classes of diagram.
	 * @param dialog - parent composite
	 */
	private void addListViewer(Composite dialog) {
//		ScrolledComposite scrolledComposite = new ScrolledComposite(dialog, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
//		
//		Composite listComposite = new Composite(scrolledComposite, SWT.NONE);
//		listComposite.setLayout(new GridLayout(1, false));

        listViewer = new ListViewer(dialog, SWT.BORDER | SWT.V_SCROLL);
        listViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        listViewer.setContentProvider(ArrayContentProvider.getInstance());
        listViewer.setInput(classModels);

//        scrolledComposite.setContent(listComposite);
//        scrolledComposite.setSize(150, 150);
//        scrolledComposite.setMinSize(150, 150);
        addListViewerListener();
	}

	/**
	 * Adds listener to list viewer.
	 */
	private void addListViewerListener() {
		listViewer.addSelectionChangedListener(event-> {
        	IStructuredSelection selection = listViewer.getStructuredSelection();
        	removeButton.setEnabled(!selection.isEmpty());
        	removeAllButton.setEnabled(!classModels.isEmpty());
        	if (!selection.isEmpty()) {
                ClassModel selectedModel = (ClassModel) selection.getFirstElement();
                int index = classModels.indexOf(selectedModel);
                upButton.setEnabled(index >= 1);
                downButton.setEnabled(index + 1 < classModels.size());
            }
        	else {
        		upButton.setEnabled(false);
        		downButton.setEnabled(false);
        	}
        });
	}

	/**
	 * Create header composite with buttons for table.
	 * @param dialog parent composite
	 * @return newly created composite for a table
	 * @throws MalformedURLException - if the images for the buttons were not created
	 */
	private Composite createHeaderComposite(Composite dialog) throws MalformedURLException {
		Composite headerComposite = new Composite(dialog, SWT.NONE);
		GridLayout gridLayout = new GridLayout(4, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginLeft = 0;
		gridLayout.marginRight = 0;
		headerComposite.setLayout(gridLayout);
		GridData headerContainerLayoutData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		headerComposite.setLayoutData(headerContainerLayoutData);
		// create header buttons (right part of the header)
		createHeaderButtons(headerComposite);
		return headerComposite;
	}

	/**
	 * Create composite for buttons in a header.
	 * @param headerComposite parent composite
	 * @return newly created composite for buttons in a header
	 * @throws MalformedURLException - if the images for the buttons were not created
	 */
	private void createHeaderButtons(Composite headerComposite) throws MalformedURLException {
		// create composite and layouts
		Composite headerButtonsContainer = createHeaderButtonsComposite(headerComposite);
		// add buttons with listener
		addRemoveButton(headerButtonsContainer);
        addRemoveAllButton(headerButtonsContainer);
        addUpButton(headerButtonsContainer);
        addDownButton(headerButtonsContainer);
	}

	/**
	 * Add move down button.
	 * @param headerButtonsContainer - parent composite
	 * @throws MalformedURLException - if the images for the buttons were not created
	 */
	private void addDownButton(Composite headerButtonsContainer) throws MalformedURLException {
		downButton = new Button(headerButtonsContainer, SWT.PUSH);
        downButton.setToolTipText("Move the selected class down");
        downButton.setEnabled(false);
        Image downImg = ImageDescriptor.createFromURL(
        		new URL("platform:/plugin/cz.muni.fi.diagram/icons/down.gif")).createImage();
        downButton.setImage(downImg);
        downButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	IStructuredSelection selection = listViewer.getStructuredSelection();
                if (!selection.isEmpty()) {
                    ClassModel selectedModel = (ClassModel) selection.getFirstElement();
                    int index = classModels.indexOf(selectedModel);
                    Collections.swap(classModels, index, index + 1);
                    listViewer.setInput(classModels);
                    listViewer.setSelection(selection);
                }
            }
        });
	}

	/**
	 * Add move up button.
	 * @param headerButtonsContainer - parent composite
	 * @throws MalformedURLException - if the images for the buttons were not created
	 */
	private void addUpButton(Composite headerButtonsContainer) throws MalformedURLException {
		upButton = new Button(headerButtonsContainer, SWT.PUSH);
        upButton.setToolTipText("Move the selected class up");
        upButton.setEnabled(false);
        Image upImg = ImageDescriptor.createFromURL(
        		new URL("platform:/plugin/cz.muni.fi.diagram/icons/up.png")).createImage();
        upButton.setImage(upImg);
        upButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	IStructuredSelection selection = listViewer.getStructuredSelection();
                if (!selection.isEmpty()) {
                    ClassModel selectedModel = (ClassModel) selection.getFirstElement();
                    int index = classModels.indexOf(selectedModel);
                    Collections.swap(classModels, index, index - 1);
                    listViewer.setInput(classModels);
                    listViewer.setSelection(selection);
                }
            }
        });
	}

	/**
	 * Add remove all classes button.
	 * @param headerButtonsContainer - parent composite
	 * @throws MalformedURLException - if the images for the buttons were not created
	 */
	private void addRemoveAllButton(Composite headerButtonsContainer) throws MalformedURLException {
		removeAllButton = new Button(headerButtonsContainer, SWT.PUSH);
        removeAllButton.setToolTipText("Remove all classes");
        removeAllButton.setEnabled(!classModels.isEmpty());
        Image removeAllImg = ImageDescriptor.createFromURL(
        		new URL("platform:/plugin/cz.muni.fi.diagram/icons/removeAll.png")).createImage();
        removeAllButton.setImage(removeAllImg);
        removeAllButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                classModels.clear();
                listViewer.setInput(classModels);
                removeAllButton.setEnabled(false);
            }
        });
	}

	/**
	 * Add remove button.
	 * @param headerButtonsContainer - parent composite
	 * @throws MalformedURLException - if the images for the buttons were not created
	 */
	private void addRemoveButton(Composite headerButtonsContainer) throws MalformedURLException {
		removeButton = new Button(headerButtonsContainer, SWT.PUSH);
        removeButton.setToolTipText("Remove selected class");
        Image addImg = ImageDescriptor.createFromURL(
        		new URL("platform:/plugin/cz.muni.fi.diagram/icons/remove.png")).createImage();
        removeButton.setImage(addImg);
        removeButton.setEnabled(false);
        removeButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                IStructuredSelection selection = listViewer.getStructuredSelection();
                if (!selection.isEmpty()) {
                    ClassModel selectedModel = (ClassModel) selection.getFirstElement();
                    classModels.remove(selectedModel);
                    listViewer.setInput(classModels);
                    removeButton.setEnabled(false);
                }
            }
        });
	}

	/**
	 * Create composite for buttons in a header.
	 * @param headerComposite parent composite
	 * @return newly created composite for buttons in a header
	 */
	private Composite createHeaderButtonsComposite(Composite headerComposite) {
		Composite headerButtonsContainer = new Composite(headerComposite, SWT.NONE);
		GridLayout headerButtonsContainerLayout = new GridLayout(5, false);
		headerButtonsContainerLayout.horizontalSpacing = 2;
		headerButtonsContainer.setLayout(headerButtonsContainerLayout);
		GridData headerButtonsContainerGridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		headerButtonsContainer.setLayoutData(headerButtonsContainerGridData);
		return headerButtonsContainer;
	}

	/**
	 * Adds checkboxes to dialog that set properties of class diagram.
	 * @param dialog
	 */
	private void addCheckboxes(Composite dialog) {
		checkboxHideField = new Button(dialog, SWT.CHECK);
        checkboxHideField.setText("Hide fields");
        checkboxHideField.setSelection(classDiagram.isHideFields());

        checkboxHideMethod = new Button(dialog, SWT.CHECK);
        checkboxHideMethod.setText("Hide methods");
        checkboxHideMethod.setSelection(classDiagram.isHideMethods());

        checkboxHideInterface = new Button(dialog, SWT.CHECK);
        checkboxHideInterface.setText("Hide interfaces");
        checkboxHideInterface.setSelection(classDiagram.isHideInterface());

        checkboxHideChildren = new Button(dialog, SWT.CHECK);
        checkboxHideChildren.setText("Hide children");
        checkboxHideChildren.setSelection(classDiagram.isHideChildren());
        
        checkboxHideParent = new Button(dialog, SWT.CHECK);
        checkboxHideParent.setText("Hide parents");
        checkboxHideParent.setSelection(classDiagram.isHideParent());

        checkboxHidePackage = new Button(dialog, SWT.CHECK);
        checkboxHidePackage.setText("Hide packages");
        checkboxHidePackage.setSelection(classDiagram.isHidePackage());

        checkboxHideNestedClasses= new Button(dialog, SWT.CHECK);
        checkboxHideNestedClasses.setText("Hide nested classes");
        checkboxHideNestedClasses.setSelection(classDiagram.isHideNestedClasses());
	}

    @Override
    protected void okPressed() {
    	classDiagram.setClasses(classModels);

    	classDiagram.setHideFields(checkboxHideField.getSelection());
    	classDiagram.setHideMethods(checkboxHideMethod.getSelection());
    	classDiagram.setHideInterface(checkboxHideInterface.getSelection());
    	classDiagram.setHideParent(checkboxHideParent.getSelection());
    	classDiagram.setHideChildren(checkboxHideChildren.getSelection());
    	classDiagram.setHidePackage(checkboxHidePackage.getSelection());
    	classDiagram.setHideNestedClasses(checkboxHideNestedClasses.getSelection());

    	classDiagramCanvas.setClassDiagram(classDiagram);
    	classDiagramCanvas.redraw();
    	disposeImages();
    	super.okPressed();
    }

    /**
     * Disposes buttons images.
     */
    private void disposeImages() {
    	removeButton.getImage().dispose();
    	removeAllButton.getImage().dispose();
    	upButton.getImage().dispose();
    	downButton.getImage().dispose();
    }

	@Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Manage Classes Dialog");
    }

    @Override
    protected void cancelPressed() {
    	disposeImages();
    	super.cancelPressed();
    }

    @Override
    public boolean close() {
    	disposeImages();
    	return super.close();
    }

}
