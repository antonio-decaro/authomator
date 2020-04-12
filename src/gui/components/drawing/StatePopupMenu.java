package gui.components.drawing;

import javax.swing.*;

public class StatePopupMenu extends JPopupMenu {

    public StatePopupMenu(ContentPanel parent) {
        super();
        this.parent = parent;
        addMenuItems();
    }

    private void addMenuItems() {
        JMenuItem finalStateMenu = new JMenuItem("Final");
        finalStateMenu.addActionListener(a -> {
            state.setFinal(!state.isFinal());
            parent.refresh();
            this.setVisible(false);
        });

        JMenuItem initialStateMenu = new JMenuItem("Initial");
        initialStateMenu.addActionListener(a -> {
            state.setInitial(!state.isInitial());
            parent.refresh();
            this.setVisible(false);
        });

        add(finalStateMenu);
        add(initialStateMenu);
    }

    public StateComponent getState() {
        return state;
    }

    public void setState(StateComponent state) {
        this.state = state;
    }

    private StateComponent state;
    private ContentPanel parent;
}
