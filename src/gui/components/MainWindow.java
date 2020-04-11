package gui.components;

import javax.swing.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        super();
        setJMenuBar(createMenuBar());
        setContentPane(mainPanel);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        return menuBar;
    }

    private JPanel mainPanel;
    private JTextField textField1;
    private JButton computeButton;
}
