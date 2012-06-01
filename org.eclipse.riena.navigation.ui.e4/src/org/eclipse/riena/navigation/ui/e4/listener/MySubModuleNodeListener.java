package org.eclipse.riena.navigation.ui.e4.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.widgets.ToolItem;

import org.eclipse.riena.internal.navigation.ui.swt.handlers.NavigationSourceProvider;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.navigation.ui.e4.Activator;
import org.eclipse.riena.navigation.ui.e4.part.HeaderPart;
import org.eclipse.riena.navigation.ui.e4.part.PartWrapper;
import org.eclipse.riena.navigation.ui.swt.component.MenuCoolBarComposite;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewProvider;
import org.eclipse.riena.navigation.ui.swt.views.SubApplicationView;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.controller.IController;

/**
 * This listener of a sub module ensures the preparation of nodes (if necessary).
 */
@SuppressWarnings("restriction")
public class MySubModuleNodeListener extends SubModuleNodeListener {
	private static final String HEADER_PART_ID = "org.eclipse.riena.navigation.ui.e4.headerPart"; //$NON-NLS-1$

	private static final String VIEWS_EXT_POINT = "org.eclipse.ui.views"; //$NON-NLS-1$
	private static final String ID = "id"; //$NON-NLS-1$

	private final NavigationSourceProvider navigationSourceProvider = new NavigationSourceProvider();

	@Inject
	private IEclipseContext context;

	/**
	 * {@inheritDoc}
	 * <p>
	 * After activation of a sub module prepare - if necessary - every child node.
	 */
	@Override
	public void activated(final ISubModuleNode source) {
		MySubApplicationNodeListener.prepare(source);
		showPart(source);
		updateNavigationSourceProvider(source);
	}

	private void showPart(final ISubModuleNode source) {
		final String partId = SwtViewProvider.getInstance().getSwtViewId(source).getId();
		final EModelService modelService = context.get(EModelService.class);
		final MApplication searchRoot = context.get(MApplication.class);
		final List<MPart> parts = modelService.findElements(searchRoot, partId, MPart.class, null, EModelService.IN_ANY_PERSPECTIVE);

		MPart partToActivate = null;
		if (parts.isEmpty()) {
			final ISubApplicationNode subApplicationNode = source.getParentOfType(ISubApplicationNode.class);
			final String perspectiveId = SwtViewProvider.getInstance().getSwtViewId(subApplicationNode).getId();
			final List<MPerspective> perspectives = modelService.findElements(searchRoot, perspectiveId, MPerspective.class, null);
			if (perspectives.isEmpty()) {
				throw new IllegalStateException("Parent perspective not found. partId: " + partId + ", perspectiveId: " + perspectiveId); //$NON-NLS-1$ //$NON-NLS-2$
			}
			final List<MPartStack> stacks = modelService.findElements(perspectives.get(0), "contentPartStack", MPartStack.class, null); //$NON-NLS-1$
			if (stacks.isEmpty()) {
				throw new IllegalStateException("Part stack not found on parent perspective. partId: " + partId + ", perspectiveId: " + perspectiveId); //$NON-NLS-1$ //$NON-NLS-2$
			}

			final MElementContainer parent = stacks.get(0);
			try {
				partToActivate = createFromPluginXmlContribution(partId);

				// construct the String as in Application.e4xmi
				final String pluginId = Activator.getDefault().getBundleContext().getBundle().getSymbolicName();
				partToActivate.setContributionURI("bundleclass://" + pluginId + "/" + PartWrapper.class.getName());

				// set parent and add as child
				parent.getChildren().add(partToActivate);
				partToActivate.setParent(parent);
			} catch (final CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			partToActivate = parts.get(0);

			/** handle shared views **/
			final Object viewInstance = partToActivate.getTransientData().get(PartWrapper.VIEW_KEY);
			if (viewInstance instanceof SubModuleView) {
				((SubModuleView) viewInstance).setNavigationNode(source);
				((SubModuleView) viewInstance).prepareNode(source);
			}
		}
		if (partToActivate == null) {
			throw new IllegalStateException("Part not found, partId: " + partId);
		}
		partToActivate.getParent().setSelectedElement(partToActivate);
	}

	private void updateNavigationSourceProvider(final ISubModuleNode source) {
		navigationSourceProvider.activeNodeChanged(source);
		for (final Entry<String, Object> e : navigationSourceProvider.getCurrentState().entrySet()) {
			context.set(e.getKey(), e.getValue());
		}
	}

	/**
	 * @param partId
	 * @return
	 * @throws CoreException
	 */
	private MPart createFromPluginXmlContribution(final String partId) throws CoreException {
		final IExtensionRegistry extensionRegistry = context.get(IExtensionRegistry.class);
		for (final IConfigurationElement e : extensionRegistry.getConfigurationElementsFor(VIEWS_EXT_POINT)) {
			if (partId.equals(e.getAttribute(ID))) {
				final MPart part = MBasicFactory.INSTANCE.createPart();
				part.setElementId(partId);
				//				part.setObject(e.createExecutableExtension(CLASS));
				return part;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * After the parent of a sub module changed prepare - if necessary - every child node.
	 */
	@Override
	public void parentChanged(final ISubModuleNode source) {
		MySubApplicationNodeListener.prepare(source);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#disposed(org.eclipse.riena.navigation.INavigationNode)
	 */
	@Override
	public void disposed(final ISubModuleNode source) {
		System.err.println("MySubModuleNodeListener.disposed()");
		// TODO Auto-generated method stub
		super.disposed(source);
	}

	/**
	 * Code from {@link SubApplicationView}
	 */
	@Override
	public void afterActivated(final ISubModuleNode source) {
		final List<MenuCoolBarComposite> menuCoolBarComposites = getMenuCoolBarComposites();
		for (final MenuCoolBarComposite menuBarComp : menuCoolBarComposites) {
			final List<ToolItem> changedItems = menuBarComp.updateMenuItems();
			//				if (!changedItems.isEmpty()) {
			final IController controller = (IController) source.getParentOfType(ISubApplicationNode.class).getNavigationNodeController();
			//			createRidgets(controller);
			//			menuItemBindingManager.bind(controller, getUIControls());
			//				}
		}
	}

	/**
	 * @param mWindow
	 * @return
	 */
	private List<MenuCoolBarComposite> getMenuCoolBarComposites() {
		final EModelService modelService = context.get(EModelService.class);
		final MPart headerPart = (MPart) modelService.find(HEADER_PART_ID, context.get(MApplication.class));
		final List<MenuCoolBarComposite> result = new ArrayList<MenuCoolBarComposite>();
		final Object value = headerPart.getTransientData().get(HeaderPart.MENU_COMPOSITE_KEY);
		if (value instanceof MenuCoolBarComposite) {
			result.add((MenuCoolBarComposite) value);
		}
		return result;
	}
}