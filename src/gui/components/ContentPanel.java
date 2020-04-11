package gui.components;

import javax.swing.*;
import javax.swing.border.StrokeBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;

public class ContentPanel extends JPanel {

    public ContentPanel() {
        super();
        states = new LinkedList<>();


        setFocusable(true);
        setBackground(Color.WHITE);
        setBorder(new StrokeBorder(new BasicStroke(2.5f)));
        addListeners();
    }

    void refresh(){
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0; i < states.size(); i++) {
            StateComponent stateComponent = states.get(i);
            stateComponent.setLabel("q" + i);
            if (stateComponent == selectedState)
                stateComponent.draw(g2d, true);
            else
                stateComponent.draw(g2d);
        }
    }

    private void addListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() >= 2 && SwingUtilities.isLeftMouseButton(e)) {
                    insertState(e.getX(), e.getY());
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    setInitialState(e.getX(), e.getY());
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    selectState(e.getX(), e.getY());
                } else if (!SwingUtilities.isLeftMouseButton(e) && !SwingUtilities.isRightMouseButton(e)) {
                    setStateFinal(e.getX(), e.getY());
                }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e))
                    moveState(e.getX(), e.getY());
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_DELETE) {
                    if (selectedState != null) {
                        states.remove(selectedState);
                        refresh();
                    }
                }
            }
        });
    }

    private void insertState(int x, int y) {
        for (StateComponent state : states) {
            if (state.contains(x, y))
                return;
        }
        states.add(new StateComponent(x, y, this));
        refresh();
    }

    private void selectState(int x, int y) {
        selectedState = null;
        for (StateComponent stateComponent : states) {
            if (stateComponent.contains(x, y)) {
                selectedState = stateComponent;
                break;
            }
        }
        refresh();
    }

    private void setStateFinal(int x, int y) {
        if (selectedState == null) return;
        for (StateComponent stateComponent : states) {
            if (stateComponent == selectedState) {
                stateComponent.setFinal(!stateComponent.isFinal());
            }
            refresh();
        }
    }

    private void setInitialState(int x, int y) {
        if (selectedState == null) return;
        for (StateComponent stateComponent : states) {
            if (stateComponent == selectedState) {
                stateComponent.setInitial(!stateComponent.isInitial());
            } else {
                stateComponent.setInitial(false);
            }
            refresh();
        }
    }

    private void moveState(int x, int y) {
        for (StateComponent state : states) {
            if (state.contains(x, y)) {
                if (selectedState == state) {
                    state.setX(x);
                    state.setY(y);
                    refresh();
                }
                break;
            }
        }
    }

    private StateComponent selectedState;
    private List<StateComponent> states;

}
