package gui.components.drawing;

import javafx.util.Pair;

import javax.swing.*;
import javax.swing.border.StrokeBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ContentPanel extends JPanel {

    public ContentPanel() {
        super();
        states = new LinkedList<>();
        edges = new HashMap<>();

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

        for (Pair<StateComponent, StateComponent> pair : edges.keySet()) {
            LinkComponent edge = edges.get(pair);
            edge.draw(g2d);
        }

        if (guideArrow != null) {
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.setStroke(new BasicStroke(1f));
            g2d.drawLine((int)guideArrow.getX1(), (int)guideArrow.getY1(), (int)guideArrow.getX2(), (int)guideArrow.getY2());
        }
    }

    private void addListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() >= 2 && SwingUtilities.isLeftMouseButton(e)) {
                    insertState(e.getX(), e.getY());
                } if (SwingUtilities.isRightMouseButton(e)) {
                    setInitialState(e.getX(), e.getY());
                } if (SwingUtilities.isLeftMouseButton(e)) {
                    selectState(e.getX(), e.getY());
                } if (!SwingUtilities.isLeftMouseButton(e) && !SwingUtilities.isRightMouseButton(e)) {
                    setStateFinal(e.getX(), e.getY());
                } if (SwingUtilities.isRightMouseButton(e)) {
                    setFirstStateForLine(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    setSecondStateForLine(e.getX(), e.getY());
                    guideArrow = null;
                    refresh();
                }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e))
                    moveState(e.getX(), e.getY());
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (guideArrow != null) {
                        guideArrow.setLine(guideArrow.getX1(), guideArrow.getY1(), e.getX(), e.getY());
                        refresh();
                    }
                }
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_DELETE) {
                    deleteState();
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

    private void deleteState() {
        if (selectedState != null) {
            states.remove(selectedState);
            edges.keySet().removeIf(pair -> selectedState == pair.getKey() || selectedState == pair.getValue());
            selectedState = null;
            refresh();
        }
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

    private void setFirstStateForLine(int x, int y) {
        for (StateComponent state : states) {
            if (state.contains(x, y)) {
                tempFirstState = state;
                guideArrow = new Line2D.Double(x, y, x, y);
                break;
            }
        }
    }

    private void setSecondStateForLine(int x, int y) {
        for (StateComponent state : states) {
            if (state.contains(x, y) && tempFirstState != null) {
                tempSecondState = state;
                break;
            }
        }
        insertEdge();
    }

    private void insertEdge() {
        if (tempSecondState != null && tempFirstState != null) {
            LinkComponent edge = new LinkComponent(tempFirstState, tempSecondState, "alfred");
            Pair<StateComponent, StateComponent> pair = new Pair<>(tempFirstState, tempSecondState);
            edges.put(pair, edge);
            refresh();
        }

        tempFirstState = tempSecondState = null;
    }

    private Line2D guideArrow;
    private StateComponent selectedState, tempFirstState, tempSecondState;
    private List<StateComponent> states;
    private Map<Pair<StateComponent, StateComponent>, LinkComponent> edges;

}
