package gui.components.drawing;

import javax.swing.*;

/**
 * This class rapresents the pop up menu showed when right clicking on a selected state.
 * */
public class StatePopupMenu extends JPopupMenu {

    /**
     * Class constructor
     * @param parent the object where this menu is contained
     * */
    public StatePopupMenu(ContentPanel parent) {
        super();
        this.parent = parent;
        addMenuItems();
    }

    /**
     * Initialize the pop up menu with elements
     * */
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
