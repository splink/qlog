package com.quui.qlog.plugin.views;

import com.quui.qlog.core.data.IGuiMediator;
import com.quui.qlog.core.data.QLogDataTransformer;
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
    // TODO actually, every mediator should have its own client (e.g. a tab)
    /**
     * {@inheritDoc}
     * @see com.quui.server.IDataTransformerFactory#create()
     */
    public IDataTransformer create() {
        return new QLogDataTransformer(mediator);
    }

}
