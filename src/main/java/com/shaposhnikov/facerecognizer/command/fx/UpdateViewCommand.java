package com.shaposhnikov.facerecognizer.command.fx;

import com.shaposhnikov.facerecognizer.command.Command;
import com.shaposhnikov.facerecognizer.updater.fx.ImageViewUpdater;
import javafx.scene.image.Image;

/**
 * Created by Kirill on 28.03.2017.
 */
public class UpdateViewCommand {

    private final ImageViewUpdater updater;

    public UpdateViewCommand(ImageViewUpdater updater) {
        this.updater = updater;
    }

    public Void doWork(Image image) {
        updater.update(image);
        return null;
    }
}
