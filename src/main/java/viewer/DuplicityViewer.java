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

import model.DuplicityModel;
import model.Model;
import model.TreeUserObject;
import process.BackupExtractor;
import utils.Utils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

public class DuplicityViewer {
    private JTree tree1;
    private JPanel panel1;

    private Model model;

    private DuplicityViewer(String path) {
        super();

        initData(path);
    }

    public static void show(String path) {
        JFrame frame = new JFrame("DuplicityViewer");
        frame.setContentPane(new DuplicityViewer(path).panel1);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setPreferredSize(Utils.getMainWindowSize());
        frame.pack();
        frame.setVisible(true);

        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                String ObjButtons[] = {"Yes", "No"};
                int PromptResult = JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", "", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
                if (PromptResult == JOptionPane.YES_OPTION) {
                    frame.dispose();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }

    private void initData(String path) {
        //model = new DuplicityModel("sftp://server@192.168.0.239//shared/backups/dati/Driver");

        LoadingWindow.showLoadingWindow("Loading data...");
        model = new DuplicityModel(path);
        LoadingWindow.hideLoadingWindow();

        tree1.setModel(new DefaultTreeModel(model.getData()));
        tree1.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

                setText(((TreeUserObject) ((DefaultMutableTreeNode) value).getUserObject()).getTitle());

                return this;
            }
        });
        tree1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int selRow = tree1.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = tree1.getPathForLocation(e.getX(), e.getY());
                if (selRow != -1) {
                    if (e.getClickCount() == 1) {
                        tree1.setSelectionPath(selPath);
                        if (SwingUtilities.isRightMouseButton(e))
                            showMenu(e.getX(), e.getY());
                    }
                }
            }
        });
    }

    private void showMenu(int x, int y) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem mExtract = new JMenuItem("Extract");
        mExtract.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(null, "Do you want to extract this item?", "Are you sure?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                JFileChooser jfc = new JFileChooser();

                TreePath p = tree1.getSelectionPath();
                // set correct filename
                jfc.setSelectedFile(new File(
                        ((TreeUserObject)((DefaultMutableTreeNode)p.getPath()[p.getPathCount()-1]).getUserObject()).getTitle()));

                if (jfc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                String localp = jfc.getSelectedFile().getAbsolutePath();
                //String localp = JOptionPane.showInputDialog(null, "Insert local path where place data:");
                if (localp.equals("")) {
                    JOptionPane.showMessageDialog(null, "Wrong path!");
                    return;
                }

                StringBuilder completePath = new StringBuilder();
                for (int i = 1; i < p.getPath().length; ++i) {
                    TreeUserObject current = (TreeUserObject) ((DefaultMutableTreeNode) p.getPath()[i]).getUserObject();
                    completePath.append(current.getTitle()).append("/");
                }
                // Skip last /
                new BackupExtractor(model.getBackupPath(), completePath.substring(0, completePath.length() - 1), localp).extract();
            }
        });

        popupMenu.add(mExtract);
        popupMenu.show(tree1, x, y);
    }
}
