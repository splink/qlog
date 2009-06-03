package com.quui.qlog.plugin.views;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.quui.qlog.core.Level;

/**
 * Representation of a logging message in the table.
 * @author Fabian Steeg (fsteeg)
 */
class QuuiLogMessage {
    Level level;
    Image image;
    Color color;
    String message;

    QuuiLogMessage(final Level level, final String message) {
        this.level = level;
        String loc = null;
        Display display = PlatformUI.getWorkbench().getDisplay();
        // TODO would it make sense to add the icon locations to the actual
        // enum? Or RGB colors? these are Eclipse-spacific classes...
        switch (level) {
        case INFO:
            loc = ISharedImages.IMG_OBJS_INFO_TSK;
            color = new Color(display, new RGB(204, 255, 153));
            break;
        case DEBUG:
            loc = ISharedImages.IMG_OBJS_BKMRK_TSK;
            color = new Color(display, new RGB(240, 240, 240));
            break;
        case TRACE:
            loc = ISharedImages.IMG_TOOL_UP;
            color = new Color(display, new RGB(255, 255, 255));
            break;
        case FATAL:
            loc = ISharedImages.IMG_TOOL_UNDO;
            color = new Color(display, new RGB(204, 51, 51));
            break;
        case ERROR:
            loc = ISharedImages.IMG_OBJS_ERROR_TSK;
            color = new Color(display, new RGB(255, 204, 102));
            break;
        case WARN:
            loc = ISharedImages.IMG_OBJS_WARN_TSK;
            color = new Color(display, new RGB(153, 204, 255));
            break;
        case GARBAGE:
            loc = ISharedImages.IMG_TOOL_UP;
            color = new Color(display, new RGB(153, 153, 0));
            break;
        default:
            loc = ISharedImages.IMG_TOOL_UP;
            color = new Color(display, new RGB(255, 255, 255));
        }
        this.image = PlatformUI.getWorkbench().getSharedImages().getImage(loc);
        this.message = message;
    }
}
