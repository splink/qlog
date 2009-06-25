package com.quui.qlog.plugin.views;

import org.w3c.dom.Document;

import com.quui.qlog.core.data.IGuiMediator;
import com.quui.utils.util.IDestroyable;

/**
 * Mediator for the experimental table-based Eclipse view.
 * @author Fabian Steeg (fsteeg)
 */
public final class EclipseTableMediator implements IGuiMediator {

    private QuuiLogTableView view;

    /**
     * @param viewer The view to update depending on incoming messages
     */
    public EclipseTableMediator(final QuuiLogTableView view) {
        this.view = view;
    }

    /**
     * {@inheritDoc}
     * @see com.quui.qlog.swing.data.IGuiMediator#onCommand(java.lang.String)
     */
    public void onCommand(final String command) {
        System.out.println("onCommand: " + command);
    }

    /**
     * {@inheritDoc}
     * @see com.quui.qlog.swing.data.IGuiMediator#onMessage(java.lang.String,
     *      java.lang.String)
     */
    public void onMessage(final String message, final String color) {
       view.add(message,color);
    }

    /**
     * {@inheritDoc}
     * @see com.quui.qlog.swing.data.IGuiMediator#onLogin(java.lang.String)
     */
    public void onLogin(final String name) {}

    /**
     * {@inheritDoc}
     * @see com.quui.qlog.swing.data.IGuiMediator#onTree(org.w3c.dom.Document)
     */
    public void onTree(final Document doc) {}

    /**
     * {@inheritDoc}
     * @see com.quui.qlog.swing.data.IGuiMediator#setDataTransformer(com.quui.utils.util.IDestroyable)
     */
    public void setDataTransformer(final IDestroyable dataHandler) {}

    /**
     * {@inheritDoc}
     * @see com.quui.utils.util.IDestroyable#destroy()
     */
    public void destroy() {}

}
