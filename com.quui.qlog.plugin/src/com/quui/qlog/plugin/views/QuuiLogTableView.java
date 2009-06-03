package com.quui.qlog.plugin.views;

import java.net.BindException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.quui.qlog.core.Level;
import com.quui.qlog.core.PropertiesReader;
import com.quui.qlog.plugin.Activator;
import com.quui.server.Server;

/**
 * View to display QLog logging messages.
 * @author Fabian Steeg
 */
public final class QuuiLogTableView extends ViewPart {
    private Action clearAction;
    private Action filterAction;
    private PropertiesReader reader;
    private Server server;
    private Action restartAction;
    private boolean locked;
    private TableViewer viewer;

    /**
     * Representation of a logging message in the table.
     * @author Fabian Steeg (fsteeg)
     */
    static class Message {
        Level level;
        Image image;
        String message;

        Message(Level level, String message) {
            this.level = level;
            String loc = ISharedImages.IMG_TOOL_UP;
            // TODO would it make sense to add the icon locations to the actual
            // enum?
            switch (level) {
            case INFO:
                loc = ISharedImages.IMG_OBJS_INFO_TSK;
                break;
            case DEBUG:
                loc = ISharedImages.IMG_OBJS_BKMRK_TSK;
                break;
            case TRACE:
                loc = ISharedImages.IMG_TOOL_UP;
                break;
            case FATAL:
                loc = ISharedImages.IMG_TOOL_UNDO;
                break;
            case ERROR:
                loc = ISharedImages.IMG_OBJS_ERROR_TSK;
                break;
            case WARN:
                loc = ISharedImages.IMG_OBJS_WARN_TSK;
                break;
            case GARBAGE:
                loc = ISharedImages.IMG_TOOL_UP;
                break;
            }
            this.image = PlatformUI.getWorkbench().getSharedImages().getImage(
                    loc);
            this.message = message;
        }
    }

    /*
     * The content provider class is responsible for providing objects to the
     * view. It can wrap existing objects in adapters or simply return objects
     * as-is. These objects may be sensitive to the current input of the view,
     * or ignore it and always show the same content (like Task List, for
     * example).
     */

    // static class QuuiLogContentProvider implements IStructuredContentProvider
    // {
    // QuuiLogMessage[] messages = new QuuiLogMessage[] { new QuuiLogMessage(
    // Level.INFO, PlatformUI.getWorkbench().getSharedImages()
    // .getImage(ISharedImages.IMG_OBJS_INFO_TSK),
    // "Welcome to qlog4eclipse!") };
    //
    // public void inputChanged(Viewer v, Object oldInput, Object newInput) {
    // System.out.println(String
    // .format("Input changed for %s from %s to %s", v, oldInput,
    // newInput));
    // messages = (QuuiLogMessage[]) newInput;
    // }
    // public void dispose() {}
    // @Override
    // public Object[] getElements(Object parent) {
    // return messages;
    // }
    // }
    /**
     * Provides labels in the table, for message objects.
     * <p/>
     * TODO: use multiple columns
     * @author Fabian Steeg (fsteeg)
     */
    static class QuuiLogLabelProvider extends LabelProvider implements
            ITableLabelProvider {
        @Override
        public String getColumnText(Object obj, int index) {
            System.out.println("getImage: " + obj);
            return getText(obj);
        }
        @Override
        public String getText(Object obj) {
            Message message = (Message) obj;
            return message.level + " -- " + message.message;
        }
        @Override
        public Image getColumnImage(Object obj, int index) {
            System.out.println("getImage: " + obj);
            return getImage(obj);
        }
        @Override
        public Image getImage(Object obj) {
            System.out.println("getImage: " + obj);
            return ((Message) obj).image;
        }
    }

    static class NameSorter extends ViewerSorter {}

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(final Composite parent) {

        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.BORDER);
        // viewer.setContentProvider(new QuuiLogContentProvider());
        viewer.setLabelProvider(new QuuiLogLabelProvider());
        // viewer.setSorter(new NameSorter());
        // viewer.setInput(getViewSite());

        makeActions();
        contributeToActionBars();
        createServer();
    }

    /**
     * Create a qLog view using the default configuration file.
     */
    public QuuiLogTableView() {
        String loc = Activator.getFileLocationInPlugin("config.xml");
        reader = new PropertiesReader(loc);
        reader.initialize();
    }

    private void createServer() {
        if (server != null) {
            server.destroy();
        }
        try {
            server = new Server(reader.getIp(), reader.getPort(),
                    new EclipseDataTransformerFactory(new EclipseTableMediator(
                            this)));
        } catch (BindException e) {
            e.printStackTrace();
        }
    }

    private void makeActions() {
        clearAction = new Action() {
            public void run() {
                viewer.getTable().clearAll();
            }
        };
        clearAction.setText("Clear");
        clearAction.setToolTipText("Clear the Log Outputs");
        clearAction.setImageDescriptor(Activator
                .getImageDescriptor("icons/clear.gif"));
        // TODO implement for table, content provider etc.
        clearAction.setEnabled(false);
        restartAction = new Action() {
            public void run() {
                createServer();
            }
        };
        restartAction.setText("Restart");
        restartAction.setToolTipText("Restart the Log Server");
        restartAction.setImageDescriptor(Activator
                .getImageDescriptor("icons/update.gif"));
        // TODO implement for table, content provider etc.
        restartAction.setEnabled(false);
        filterAction = new Action() {
            public void run() {
                InputDialog d = new InputDialog(getSite().getShell(), "Filter",
                        "Filter", "", null);
                d.open();
                String filter = d.getValue();
                throw new NotImplementedException();
            }
        };
        filterAction.setText("Filter");
        filterAction.setToolTipText("Filter the Messages");
        filterAction.setImageDescriptor(Activator
                .getImageDescriptor("icons/search.gif"));
        // TODO implement for table, content provider etc.
        filterAction.setEnabled(false);
    }

    /**
     * Adds actions to menu and tool bar.
     */
    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }

    /**
     * @param manager The manager to be filled with actions for the menu
     */
    private void fillLocalPullDown(final IMenuManager manager) {
        manager.add(filterAction);
        manager.add(clearAction);
        manager.add(new Separator());
        manager.add(restartAction);
    }

    /**
     * @param manager The manager to be filled with actions for the tool bar
     */
    private void fillLocalToolBar(final IToolBarManager manager) {
        manager.add(filterAction);
        manager.add(clearAction);
        manager.add(new Separator());
        manager.add(restartAction);
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    @Override
    public void dispose() {
        if (server != null) {
            server.destroy();
        }
        super.dispose();
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus() {}

    /**
     * Scrolls the browser widget down using JavaScript.
     */
    private void scrollDown() {
        if (!locked) {
            Display.getDefault().asyncExec(new Runnable() {
                public void run() {
                    Table table = viewer.getTable();
                    TableItem last = table.getItem(table.getItemCount() - 1);
                    table.showItem(last);
                }
            });
        }
    }

    public void add(final String message, String color) {
        final Level level = Level.from(color); // TODO use level enum everywhere
        if (level == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not create a level for: %s", color));
        }
        // TODO use content provider instead
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                viewer.add(new QuuiLogTableView.Message(level, message));
                scrollDown();
            }
        });

    }
}
