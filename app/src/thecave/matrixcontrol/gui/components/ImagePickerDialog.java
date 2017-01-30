package thecave.matrixcontrol.gui.components;

import thecave.matrixcontrol.gui.utils.WrapLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ImagePickerDialog extends JDialog {
    public static final int RESULT_CANCELLED = -1;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel imageListPanel;

    private int pickedImage;

    private ImagePickerDialog(Window owner, ImageSource imageSource, int selectedImageIndex) {
        super(owner, ModalityType.APPLICATION_MODAL);
        setTitle("Pick image");
        setContentPane(contentPane);

        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!SwingUtilities.isLeftMouseButton(e)) return;

                ImageEditorPanel iep = getImageEditorPanel(pickedImage);
                if (iep != null) iep.setSelected(false);
                iep = (ImageEditorPanel) e.getSource();
                iep.setSelected(true);
                pickedImage = iep.getImageIndex();

                if (e.getClickCount() == 2) {
                    onOK();
                }
            }
        };

        imageListPanel.setLayout(new WrapLayout(WrapLayout.LEFT));
        int imageCount = imageSource.getImageCount();
        this.pickedImage = Math.min(Math.max(-1, selectedImageIndex), imageCount - 1);
        for (int i = 0; i < imageCount; i++) {
            ImageEditorPanel iep = new ImageEditorPanel();
            iep.setMinimumSize(new Dimension(40, 40));
            iep.setPreferredSize(iep.getMinimumSize());
            iep.setSelected(i == pickedImage);
            iep.setImageIndex(i);
            iep.setShowIgnored(false);
            iep.setDeviceImage(imageSource.getImageWithId(i));

            iep.addMouseListener(mouseListener);
            imageListPanel.add(iep);
        }

        getRootPane().setDefaultButton(buttonOK);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private ImageEditorPanel getImageEditorPanel(int imageId) {
        try {
            return (ImageEditorPanel) imageListPanel.getComponent(imageId);
        } catch (Exception e) {
            return null;
        }
    }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        pickedImage = RESULT_CANCELLED;
        dispose();
    }

    public static int pickDeviceImage(Window owner, ImageSource imageSource, int selectedImageIndex) {
        ImagePickerDialog dialog = new ImagePickerDialog(owner, imageSource, selectedImageIndex);
        dialog.pack();
        dialog.setLocationRelativeTo(owner);
        dialog.setVisible(true);
        return dialog.pickedImage;
    }
}
