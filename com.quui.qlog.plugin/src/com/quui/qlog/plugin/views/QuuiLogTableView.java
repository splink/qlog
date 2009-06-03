package com.quui.qlog.plugin.views;

import java.net.BindException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

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
    private Action restartAction;
    private PropertiesReader reader;
    private Server server;
    private boolean locked;
    private TableViewer viewer;

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(final Composite parent) {
        Composite tableComposite = new Composite(parent, SWT.NONE);
        TableColumnLayout tableColumnLayout = new TableColumnLayout();
        tableComposite.setLayout(tableColumnLayout);
        viewer = new TableViewer(tableComposite, SWT.BORDER
                | SWT.FULL_SELECTION);
        viewer.getTable().setHeaderVisible(true);
        viewer.getTable().setLinesVisible(true);
        addIconColumn(tableColumnLayout);
        addLevelColumn(tableColumnLayout);
        addMessageColumn(tableColumnLayout);
        makeActions();
        contributeToActionBars();
        createServer();
    }

    private void addMessageColumn(final TableColumnLayout tableColumnLayout) {
        TableViewerColumn viewerMessageColumn = new TableViewerColumn(viewer,
                SWT.NONE);
        viewerMessageColumn.getColumn().setText("Message");
        tableColumnLayout.setColumnData(viewerMessageColumn.getColumn(),
                new ColumnWeightData(85, 200, true));
        viewerMessageColumn.setLabelProvider(new CellLabelProvider() {
            @Override
            public void update(final ViewerCell cell) {
                QuuiLogMessage message = (QuuiLogMessage) cell.getElement();
                cell.setText(message.message);
                cell.setBackground(message.color);
            }
        });
    }

    private void addLevelColumn(final TableColumnLayout tableColumnLayout) {
        TableViewerColumn viewerNameColumn = new TableViewerColumn(viewer,
                SWT.NONE);
        viewerNameColumn.getColumn().setText("Level");
        tableColumnLayout.setColumnData(viewerNameColumn.getColumn(),
                new ColumnPixelData(50));
        viewerNameColumn.setLabelProvider(new CellLabelProvider() {

            @Override
            public void update(final ViewerCell cell) {
                QuuiLogMessage message = (QuuiLogMessage) cell.getElement();
                cell.setText(message.level.toString());
            }

        });
    }

    private void addIconColumn(final TableColumnLayout tableColumnLayout) {
        TableViewerColumn viewerIconColumn = new TableViewerColumn(viewer,
                SWT.NONE);
        viewerIconColumn.getColumn().setText("");
        tableColumnLayout.setColumnData(viewerIconColumn.getColumn(),
                new ColumnPixelData(30));
        viewerIconColumn.setLabelProvider(new CellLabelProvider() {
            @Override
            public void update(final ViewerCell cell) {
                QuuiLogMessage message = (QuuiLogMessage) cell.getElement();
                cell.setImage(message.image);
            }
        });
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
        buildClearAction();
        buildRestartAction();
        buildFilterAction();

    }

    private void buildClearAction() {
        clearAction = new Action() {
            public void run() {
                viewer.getTable().clearAll();
                viewer.refresh();
            }
        };
        clearAction.setText("Clear");
        clearAction.setToolTipText("Clear the Log Outputs");
        clearAction.setImageDescriptor(Activator
                .getImageDescriptor("icons/clear.gif"));
    }

    private void buildRestartAction() {
        restartAction = new Action() {
            public void run() {
                createServer();
            }
        };
        restartAction.setText("Restart");
        restartAction.setToolTipText("Restart the Log Server");
        restartAction.setImageDescriptor(Activator
                .getImageDescriptor("icons/update.gif"));
    }

    private void buildFilterAction() {
        filterAction = new Action() {
            public void run() {
                InputDialog d = new InputDialog(getSite().getShell(), "Filter",
                        "Filter", "", null);
                d.open();
                final String filter = d.getValue();
                ViewerFilter searchFilter = new ViewerFilter() {
                    @Override
                    public boolean select(final Viewer viewer,
                            final Object parentElement, final Object element) {
                        QuuiLogMessage message = (QuuiLogMessage) element;
                        String content = message.message.toLowerCase() + " "
                                + message.level.toString().toLowerCase();
                        return filter.trim().equals("")
                                || content.contains(filter.toLowerCase());
                    }
                };
                viewer.setFilters(new ViewerFilter[] { searchFilter });
            }
        };
        filterAction.setText("Filter");
        filterAction.setToolTipText("Filter the Messages");
        filterAction.setImageDescriptor(Activator
                .getImageDescriptor("icons/search.gif"));
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

    /**
     * @param message The message to add
     * @param color The message color
     */
    public void add(final String message, final String color) {
        final Level level = Level.from(color); // TODO use level enum everywhere
        if (level == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not create a level for: %s", color));
        }
        // TODO use content provider instead
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                viewer.add(new QuuiLogMessage(level, message));
                scrollDown();
            }
        });
    }
}
