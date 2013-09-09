package org.outrospective.leap.twitter.jfx;

import javafx.scene.CacheHint;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.outrospective.jfx.HrefStruct;
import org.outrospective.jfx.HyperlinkHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;

import java.util.regex.Matcher;

/**
* User: kon
* Date: 9/09/13
* Time: 9:24 PM
*/
class TweetListCell extends ListCell<Status> {
    private final Logger logger = LoggerFactory.getLogger(TweetListCell.class);

    @Override
    protected void updateItem(Status status, boolean empty) {
        super.updateItem(status, empty);
        if (empty) {
            setGraphic(new Label("??? EMPTY ???"));
        }
        if (!empty && status != null) {
            HBox tweetBox = new HBox();
            VBox avatarBox = new VBox();
            ImageView avatar = new ImageView(status.getUser().getBiggerProfileImageURL());
            avatar.setCache(true);
            avatar.setCacheHint(CacheHint.SPEED);

            Hyperlink hyperlink = new Hyperlink(status.getUser().getScreenName());
            hyperlink.setTooltip(new Tooltip(status.getUser().getName()));
            hyperlink.setOnAction(actionEvent -> HyperlinkHelper.visit("http://twitter.com/" + status.getUser().getScreenName()));

            avatarBox.getChildren().addAll(
                    avatar,
                    hyperlink
            );
            tweetBox.getChildren().addAll(
                avatarBox,
                new Label(status.getText()+" using "),
                HyperlinkHelper.makeHyperlink(introspectHref(status.getSource()))
            );
            setGraphic(tweetBox);
            setPrefHeight(100);
        }
    }

    private HrefStruct introspectHref(String anchorTag) {
        HrefStruct href = new HrefStruct();
        int endOfAnchorOpeningTag = anchorTag.indexOf('>')+1;
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
