package org.outrospective.leap.twitter.jfx;

import javafx.scene.CacheHint;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import org.outrospective.jfx.HrefStruct;
import org.outrospective.jfx.HyperlinkHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;

/**
 * User: kon
 * Date: 9/09/13
 * Time: 9:24 PM
 */
class TweetListCell extends ListCell<Status> {
    private final Logger logger = LoggerFactory.getLogger(TweetListCell.class);
    private static final Map<String, Image> urlImageMap = new ConcurrentHashMap<>(20);

    @Override
    protected void updateItem(Status status, boolean empty) {
        super.updateItem(status, empty);
        if (empty) {
            setGraphic(new Label("??? EMPTY ???"));
        }
        if (!empty && status != null) {
            VBox avatarBox = new VBox();
            String imageURL = status.getUser().getBiggerProfileImageURL();
            ImageView avatar = new ImageView(getImage(imageURL));
            avatar.setCache(true);
            avatar.setCacheHint(CacheHint.SPEED);

            Hyperlink hyperlink = new Hyperlink(status.getUser().getScreenName());
            hyperlink.setTooltip(new Tooltip(status.getUser().getName()));
            hyperlink.setOnAction(actionEvent -> HyperlinkHelper.visit("http://twitter.com/" + status.getUser().getScreenName()));
            hyperlink.setTextAlignment(TextAlignment.CENTER);

            avatarBox.getChildren().addAll(
                    avatar,
                    hyperlink
            );

            setGraphic(avatarBox);
            setText(String.format("%s (%s)", status.getText(), introspectHref(status.getSource()).getText()));
            setWrapText(true);

            // make the preferred width the width of the parent
            prefWidthProperty().bind(getListView().prefWidthProperty().subtract(20));

            setPrefHeight(100);

        }
    }

    private Image getImage(String imageURL) {
        if (urlImageMap.containsKey(imageURL)) {
            return urlImageMap.get(imageURL);
        } else {
            Image image = new Image(imageURL, true);
            return urlImageMap.putIfAbsent(imageURL, image);
        }
    }

    private HrefStruct introspectHref(String anchorTag) {
        HrefStruct href = new HrefStruct();
        int endOfAnchorOpeningTag = anchorTag.indexOf('>') + 1;
        int beginingOfAnchorClosingTag = anchorTag.lastIndexOf('<');
        if (endOfAnchorOpeningTag != -1 && beginingOfAnchorClosingTag != -1) {
            try {
                href.setText(anchorTag.substring(endOfAnchorOpeningTag, beginingOfAnchorClosingTag));
                Matcher matcher = TwitterController.HREF.matcher(anchorTag);
                if (matcher.find()) {
                    href.setUrl(matcher.group(1));
                }
            } catch (IndexOutOfBoundsException ioobe) {
                logger.debug("Failed to match on anchor tag {}", anchorTag);
            }
        } else {
            href.setText(anchorTag);
        }
        return href;
    }
}
