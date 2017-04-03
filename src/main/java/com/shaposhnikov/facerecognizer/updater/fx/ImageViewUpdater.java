package com.shaposhnikov.facerecognizer.updater.fx;

import com.shaposhnikov.facerecognizer.updater.Updater;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by Kirill on 27.02.2017.
 */
public class ImageViewUpdater implements Updater<Image> {

    private final ImageView imageView;

    public ImageViewUpdater(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void update(Image arg) {
        Platform.runLater(() -> {
            imageView.imageProperty().set(arg);
        });

    }
}
