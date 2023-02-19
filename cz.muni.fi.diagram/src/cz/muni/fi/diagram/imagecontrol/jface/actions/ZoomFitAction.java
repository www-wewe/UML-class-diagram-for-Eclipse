package cz.muni.fi.diagram.imagecontrol.jface.actions;

import java.util.function.Supplier;

import org.eclipse.jface.resource.ImageDescriptor;

import cz.muni.fi.diagram.imagecontrol.ImageControl;

public class ZoomFitAction extends ControlAction<ImageControl> {

	public ZoomFitAction(final Supplier<ImageControl> imageControlSupplier) {
		super(imageControlSupplier);
		setImageDescriptor(ImageDescriptor.createFromFile(ControlAction.class, "/icons/ZoomFit16.gif"));
	}

	@Override
	public void run() {
		getControl().fitZoom();
	}
}
