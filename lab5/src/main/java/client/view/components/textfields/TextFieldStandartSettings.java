package client.view.components.textfields;

import javax.swing.*;
import java.awt.*;

public interface TextFieldStandartSettings {

    void makeVisual(Font font, Color color);

    default void standartSettingsTextField(Font font, Color color) {
        makeVisual(font, color);
    }
}
