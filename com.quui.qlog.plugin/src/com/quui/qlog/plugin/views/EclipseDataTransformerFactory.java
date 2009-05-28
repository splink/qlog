package com.quui.qlog.plugin.views;

import com.quui.qlog.ui.data.IGuiMediator;
import com.quui.qlog.ui.data.QLogDataTransformer;
import com.quui.server.IDataTransformer;
import com.quui.server.IDataTransformerFactory;

/**
 * Factory used by the Eclipse view.
 * @author Fabian Steeg (fsteeg)
 */
public final class EclipseDataTransformerFactory implements
        IDataTransformerFactory {

    private IGuiMediator mediator;

    /**
     * @param mediator The mediator to use with this factory.
     */
    public EclipseDataTransformerFactory(final IGuiMediator mediator) {
        this.mediator = mediator;
    }

    /**
     * {@inheritDoc}
     * @see com.quui.server.IDataTransformerFactory#create()
     */
    @Override
    public IDataTransformer create() {
        return new QLogDataTransformer(mediator);
    }

}
