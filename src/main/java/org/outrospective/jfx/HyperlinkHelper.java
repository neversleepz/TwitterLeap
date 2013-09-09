package org.outrospective.jfx;

import javafx.scene.control.Hyperlink;
import org.outrospective.leap.twitter.jfx.TwitterController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

/**
 * User: kon
 * Date: 9/09/13
 * Time: 8:56 PM
 */
public class HyperlinkHelper {
    private static final Logger logger = LoggerFactory.getLogger(HyperlinkHelper.class);

    public static void visit(String url) {
        if (url == null || url.isEmpty()) return;
        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException e) {
            logger.error("Unable to handle click to "+ url, e);
        }
    }

    public static Hyperlink makeHyperlink(HrefStruct hrefStruct) {
        Hyperlink hyperlink =
                new Hyperlink(hrefStruct.getText());
        hyperlink.setOnAction(actionEvent -> {
            String url = hrefStruct.getUrl();
            visit(url);
        });
        return hyperlink;
    }
}
