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

package utils;

import java.awt.*;
import java.io.IOException;

public class Utils {

    public static Dimension getMainWindowSize() {
        return new Dimension(
                (int) (Toolkit.getDefaultToolkit().getScreenSize().width / 2.3),
                (int) (Toolkit.getDefaultToolkit().getScreenSize().height / 2.0)
        );
    }

    public static boolean isDuplicityIstalled() {
        try {
            Process p = Runtime.getRuntime().exec("/usr/bin/which duplicity");

            p.waitFor();

            return p.exitValue() == 0;
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            System.err.println("Cannot check duplicity installation status...");
            return false;
        }
    }
}
