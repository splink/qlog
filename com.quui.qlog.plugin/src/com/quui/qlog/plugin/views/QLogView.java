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

import com.quui.qlog.plugin.Activator;
import com.quui.qlog.swing.data.QLogDataTransformerFactory;
import com.quui.qlog.swing.gui.Filter;
import com.quui.qlog.swing.gui.TableBuilder;
import com.quui.qlog.swing.properties.PropertiesReader;
import com.quui.server.Server;
import com.quui.utils.event.IDistributor;
import com.quui.utils.event.IEvent;
import com.quui.utils.event.IListener;

/**
 * View to display QLog logging messages.
 * @author fsteeg
 */
public class QLogView extends ViewPart implements IListener {
    private static final String CHANGE = "document.getElementById(\"content\").innerHTML =";
    protected static final String BODY = "";// overflow:hidden;
    final String SCRIPT_SCROLL = "window.scrollTo(0,100000);";
    private Action clearAction;
    private Action filterAction;
    private TableBuilder tableBuilder;
    private Browser browser = null;
    private PropertiesReader reader;
    private String allHTML = "";
    private Server server;
    protected String filter;
    private Action restartAction;
    private boolean locked;

    /**
     * Create a qLog view using the default configuration file.
     */
    public QLogView() {
        String loc = Activator.getFileLocationInPlugin("config.xml");
        reader = new PropertiesReader(loc);
        reader.initialize();
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(Composite parent) {
        makeActions();
        contributeToActionBars();
        browser = new Browser(parent, SWT.MOZILLA);
        tableBuilder = new TableBuilder();
        startServer();
    }

    private void startServer() {
        String color = "white";
        try {
            if (server != null) {
                server.destroy();
            }
            server = new Server(reader.getIp(), reader.getPort(),
                    new QLogDataTransformerFactory());
            allHTML += tableBuilder.buildHTML(color, "Started server at: "
                    + reader.getIp() + ":" + reader.getPort());
            // TableBuilder.tableWith("Started server at: "
            // + reader.getIp() + ":" + reader.getPort(), color);
        } catch (BindException e) {
            e.printStackTrace();
            allHTML += tableBuilder.buildHTML(color,
                    "Could not start the server: " + e.getMessage());
        } finally {
            browser.setText(allHTML);
            scrollDown();
        }
    }

    void restartServer() {
        String color = "white";
        try {
            if (server != null) {
                server.destroy();
            }
            server = new Server(reader.getIp(), reader.getPort(),
                    new QLogDataTransformerFactory());
            allHTML += "Restarted server at: " + reader.getIp() + ":"
                    + reader.getPort();
        } catch (BindException e) {
            e.printStackTrace();
            allHTML += "Could not restart the server: " + e.getMessage();
        } finally {
            allHTML += "<br/>";
            setInnerHtml(allHTML);
        }
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
    private void fillLocalPullDown(IMenuManager manager) {
        manager.add(filterAction);
        manager.add(clearAction);
        manager.add(new Separator());
        manager.add(restartAction);
    }

    /**
     * @param manager The manager to be filled with actions for the tool bar
     */
    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(filterAction);
        manager.add(clearAction);
        manager.add(new Separator());
        manager.add(restartAction);
    }

    private void makeActions() {
        clearAction = new Action() {
            public void run() {
                tableBuilder.clear();
                allHTML = "";
                browser.setText(allHTML + tableBuilder.buildHTML("", "white"));
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
                allHTML = matches;
                browser.setText(allHTML);
                scrollDown();
            }
        };
        filterAction.setText("Filter");
        filterAction.setToolTipText("Filter the Messages");
        filterAction.setImageDescriptor(Activator
                .getImageDescriptor("icons/search.gif"));
    }

    /**
     * @param content The content for the browser
     */
    private void setInnerHtml(String content) {
        content = content.replaceAll("'", "\\\\'");
        browser.execute(CHANGE + " '" + content + "'");
        scrollDown();
    }

    /**
     * {@inheritDoc}
     * @see de.quui.qlog.IInvoker#incomingCommand(java.lang.String)
     */
    public void incomingCommand(String command) {
        if (command.equals("clear")) {
            tableBuilder.clear();
            set(tableBuilder.getContent());
        }
    }

    /**
     * {@inheritDoc}
     * @see de.quui.qlog.IInvoker#incomingMessage(java.lang.String,
     *      java.lang.String)
     */
    public void incomingMessage(String color, String message) {
        message = message.replace(" ", "&nbsp;");
        final String newMsg = tableBuilder.buildHTML(color, message);
        allHTML += newMsg;
        final String newHTML = allHTML;
        if (newMsg == null) {
            return;
        }
        set(newHTML);
    }

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

    /**
     * @param newHTML The new html to display in the browser
     */
    private void set(final String newHTML) {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                System.out.println("Setting: " + newHTML);
                setInnerHtml(newHTML);
            }
        });
    }

    @Override
    public void dispose() {
        if (server != null) {
            server.destroy();
        }
        super.dispose();
    }

    @Override
    public void setFocus() {}

    @Override
    public void onEvent(IEvent event, IListener listener) {
        String type = event.getType();
        IDistributor source = event.getSource();
        System.out.println(String.format(
                "Recieved event type %s from distributor %s", type, source));

    }
}
