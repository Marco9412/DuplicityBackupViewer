/*
    Copyright (C) 2017  Marco Panato marco.panato94@gmail.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package viewer;

import javax.swing.*;
import java.awt.*;

public class LoadingWindow {

    private static LoadingWindow instance;

    private JFrame frame;
    private boolean shown;

    private LoadingWindow(String message) {
        frame = new JFrame();

        ImageIcon loading = new ImageIcon("images/ajax-loader.gif");
        frame.add(new JLabel(message, loading, JLabel.CENTER));

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBounds(
                Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 100,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 40,
                200,
                80
        );
        frame.setVisible(true);
        shown = true;

        instance = this;
    }

    public static void showLoadingWindow(String message) {
        hideLoadingWindow();

        new LoadingWindow(message);
    }

    public static void hideLoadingWindow() {
        if (instance != null) {
            instance.hide();
            instance = null;
        }
    }

    private void hide() {
        frame.dispose();
        shown = false;
    }
}
