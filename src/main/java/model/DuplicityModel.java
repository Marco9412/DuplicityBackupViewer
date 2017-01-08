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

package model;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class DuplicityModel implements Model {

    private String path;
    private TreeNode data;

    public DuplicityModel(String path) {
        this.path = path;

        load(execDuplicity());
    }

    private String execDuplicity() {
        try {
            // Search for line "dow mon dom hh:mm:ss yyyy ."
            Pattern pattern = Pattern.compile("[a-zA-Z]{3,3} [a-zA-Z]{3,3} ( [0-9]|[0-9]{2,2}) [0-9]{2,2}:[0-9]{2,2}:[0-9]{2,2} [0-9]{4,4} \\.");
            StringBuilder res = new StringBuilder(500);
            String tmp;
            final String CMD = "/usr/bin/duplicity list-current-files " + path + "";
            Process p = Runtime.getRuntime().exec(CMD);

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((tmp = br.readLine()) != null && !pattern.matcher(tmp).matches()) ; // skip first lines

            while ((tmp = br.readLine()) != null) {
                res.append(tmp);
                res.append('\n');
            }
            br.close();

            if (!p.isAlive() && p.exitValue() != 0) System.err.println("duplicity process returned non-0 exit code!");

            return res.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void load(String data) {
        String current;
        String date;
        if (data == null) return;
        StringTokenizer st = new StringTokenizer(data, "\n", false);

        // Build tree
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new TreeUserObject("/", ""));
        while (st.hasMoreTokens()) {
            current = st.nextToken();
            date = current.substring(0, 24);
            current = current.substring(25);

            addNodes(root, current, date);
        }

        this.data = root;
    }

    private void addNodes(DefaultMutableTreeNode root, String path, String date) {
        DefaultMutableTreeNode current = root;
        DefaultMutableTreeNode tmpNode;
        String tmp;
        StringTokenizer st = new StringTokenizer(path, "/", false);

        while (st.hasMoreTokens()) {
            tmp = st.nextToken();

            // get child if exist
            tmpNode = getChild(current, tmp);
            if (tmpNode == null) {
                tmpNode = new DefaultMutableTreeNode(new TreeUserObject(tmp, date));
                current.add(tmpNode);
            }

            current = tmpNode;
        }
    }

    private DefaultMutableTreeNode getChild(DefaultMutableTreeNode node, String name) {
        // TODO efficient implementation?
        for (int i = 0; i < node.getChildCount(); ++i)
            if (((TreeUserObject) ((DefaultMutableTreeNode) node.getChildAt(i)).getUserObject()).getTitle().equals(name))
                return (DefaultMutableTreeNode) node.getChildAt(i);
        return null;
    }

    @Override
    public TreeNode getData() {
        return data;
    }

    @Override
    public String getBackupPath() {
        return path;
    }
}
