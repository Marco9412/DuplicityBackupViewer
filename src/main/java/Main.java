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

import utils.Utils;
import viewer.DuplicityViewer;

import javax.swing.*;

public class Main {

    public static void main(String args[]) {
        // Check duplicity
        if (!Utils.isDuplicityIstalled()) {
            JOptionPane.showMessageDialog(null, "You must install duplicity to use this frontend!\n(ubuntu apt install duplicity)");
            System.exit(1);
        }

        String path = JOptionPane.showInputDialog(null, "Insert backup path:");
        if (path != null) DuplicityViewer.show(path);
    }
}
