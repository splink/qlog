package com.quui.qlog.plugin.views;

import java.net.BindException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

import com.quui.qlog.core.Filter;
import com.quui.qlog.core.PropertiesReader;
import com.quui.qlog.core.TableBuilder;
import com.quui.qlog.plugin.Activator;
import com.quui.server.Server;

/**
 * View to display QLog logging messages.
 * @author Fabian Steeg
 */
public final class QuuiLogView extends ViewPart {
    private static final String SCRIPT_SCROLL = "window.scrollTo(0,100000);";
    private Action clearAction;
    private Action filterAction;
    private TableBuilder tableBuilder;
    private Browser browser = null;
    private PropertiesReader reader;
    private String allHtml = "";
    private Server server;
    private String filter;
    private Action restartAction;
    private boolean locked;

    /**
     * Create a qLog view using the default configuration file.
     */
    public QuuiLogView() {
        String loc = Activator.getFileLocationInPlugin("config.xml");
        reader = new PropertiesReader(loc);
        reader.initialize();
    }

    /**
     * Clears the current log output.
     */
    void clear() {
        tableBuilder.clear();
        setHtml(tableBuilder.getCss() + tableBuilder.getContent());

    }

    /**
     * Update the log output with new input.
     * @param message The incoming message
     * @param color The color
     */
    void update(final String message, final String color) {
        String messageCopy = message.replace(" ", "&nbsp;");
        final String newMsg = tableBuilder.buildHTML(color, messageCopy);
        String allHtmlCopy = allHtml;
        allHtmlCopy += newMsg;
        final String newHTML = allHtmlCopy;
        if (newMsg == null) {
            return;
        }
        setHtml(newHTML);
    }

    private void startServer() {
        String color = "white";
        try {
            createServer();
            allHtml += tableBuilder.buildHTML(color, "Started server at: "
                    + reader.getIp() + ":" + reader.getPort());
        } catch (BindException e) {
            e.printStackTrace();
            allHtml += tableBuilder.buildHTML(color,
                    "Could not start the server: " + e.getMessage());
        } finally {
            setHtml(allHtml);
        }
    }

    private void createServer() throws BindException {
        if (server != null) {
            server.destroy();
        }
        server = new Server(reader.getIp(), reader.getPort(),
                new EclipseDataTransformerFactory(new EclipseMediator(this)));
    }

    private void restartServer() {
        try {
            createServer();
            allHtml += "Restarted server at: " + reader.getIp() + ":"
                    + reader.getPort();
        } catch (BindException e) {
            e.printStackTrace();
            allHtml += "Could not restart the server: " + e.getMessage();
        } finally {
            allHtml += "<br/>";
            setHtml(allHtml);
        }
    }

    private void setHtml(final String newHtml) {
        allHtml = newHtml;
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                browser.setText(newHtml);
                /*
                 * This seemed to be a better solution (avoided the flickering
                 * when scrolling), but currently does not work for me at all.
                 */
                // String content = newHtml.replaceAll("'", "\\\\'");
                // browser.execute("document.getElementById(\"content\").innerHTML ="
                // + " '" + content + "'");
                scrollDown();
            }
        });
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(final Composite parent) {
        makeActions();
        contributeToActionBars();
        browser = new Browser(parent, SWT.NONE);
        tableBuilder = new TableBuilder(12);
        allHtml = tableBuilder.getCss() + allHtml;
        startServer();
    }

    private void makeActions() {
        clearAction = new Action() {
            public void run() {
                tableBuilder.clear();
                allHtml = tableBuilder.getCss() + "";
                browser.setText(allHtml + tableBuilder.buildHTML("", "white"));
                scrollDown();
            }
        };
        clearAction.setText("Clear");
        clearAction.setToolTipText("Clear the Log Outputs");
        clearAction.setImageDescriptor(Activator
                .getImageDescriptor("icons/clear.gif"));
        restartAction = new Action() {
            public void run() {
                restartServer();
            }
        };
        restartAction.setText("Restart");
        restartAction.setToolTipText("Restart the Log Server");
        restartAction.setImageDescriptor(Activator
                .getImageDescriptor("icons/update.gif"));
        filterAction = new Action() {
            public void run() {
                InputDialog d = new InputDialog(getSite().getShell(), "Filter",
                        "Filter", "", null);
                d.open();
                filter = d.getValue();
                String matches = Filter.find(filter, tableBuilder);
                allHtml = matches;
                browser.setText(allHtml);
                scrollDown();
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
                    browser.execute(SCRIPT_SCROLL);
                }
            });
        }
    }
}
