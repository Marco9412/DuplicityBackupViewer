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

package process;

import viewer.LoadingWindow;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BackupExtractor {

    private String uri;
    private String path;
    private String lpath;

    public BackupExtractor(String backupPosition, String pathToExtract, String localPath) {
        this.uri = backupPosition;
        this.path = pathToExtract;
        this.lpath = localPath;
    }

    public void extract() {
        new Thread(() -> {
            LoadingWindow.showLoadingWindow("Extracting...");
            try {
                Process p = new ProcessBuilder("/usr/bin/duplicity", "--no-encryption", "--file-to-restore=" + path + "", uri, lpath).start();

                try {
                    p.waitFor();
                } catch (InterruptedException ignored) {
                }

                if (p.exitValue() != 0) JOptionPane.showMessageDialog(null,
                        "duplicity process returned non-0 exit code! -> " + new BufferedReader(new InputStreamReader(p.getErrorStream())).readLine());

                LoadingWindow.hideLoadingWindow();
            } catch (IOException e) {
                e.printStackTrace();

                JOptionPane.showMessageDialog(null, "Exception while extracting backup! -> " + e.getLocalizedMessage());
            }


            LoadingWindow.hideLoadingWindow();
        }).start();


    }
}
