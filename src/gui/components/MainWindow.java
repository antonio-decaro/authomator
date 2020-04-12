package gui.components;

import core.Authom;
import gui.components.drawing.ContentPanel;

import javax.swing.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        super();
        setJMenuBar(createMenuBar());
        setContentPane(mainPanel);

        addListeners();
    }

    private void createUIComponents() {
        contentPanel = new ContentPanel();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        return menuBar;
    }

    private void addListeners() {
        computeButton.addActionListener(e-> startComputation());
    }

    private void startComputation() {
        String string = computeStringTF.getText();
        if (string == null) {
            return;
        }

        string = string.trim();

        Authom authom = new Authom(contentPanel.getStates(), contentPanel.getAlphabet(),
                contentPanel.getTransitionFunction(), contentPanel.getInitialState(), contentPanel.getFinalStates());

        try {
            boolean result = authom.compute(string);
            JOptionPane.showMessageDialog(this, result, "Result", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Bad format for authom", "Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private JPanel mainPanel;
    private JTextField computeStringTF;
    private JButton computeButton;

    private ContentPanel contentPanel;
}
