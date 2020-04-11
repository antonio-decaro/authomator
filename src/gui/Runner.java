package gui;

import gui.components.MainWindow;

import javax.swing.*;

public class Runner {
    public static void main(String[] args) {
        JFrame frame = new MainWindow();
        frame.setTitle("Authomator");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
