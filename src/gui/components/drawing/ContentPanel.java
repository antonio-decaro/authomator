package gui.components.drawing;

import core.TransitionFunction;
import javafx.util.Pair;

import javax.swing.*;
import javax.swing.border.StrokeBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.List;
import java.util.*;

public class ContentPanel extends JPanel {

    public ContentPanel() {
        super();
        states = new LinkedList<>();
        edges = new HashMap<>();
        statePopupMenu = new StatePopupMenu(this);
        add(statePopupMenu);

        setFocusable(true);
        setBackground(Color.WHITE);
        setBorder(new StrokeBorder(new BasicStroke(2.5f)));
        addListeners();
    }

    public Set<String> getStates() {
        Set<String> ret = new TreeSet<>();
        for (StateComponent stateComponent : states)
            ret.add(stateComponent.getLabel());
        return ret;
    }

    public Set<String> getAlphabet() {
        Set<String> ret = new HashSet<>();
        ret.add("a");
        ret.add("b");
        return ret;
    }

    public String getInitialState() {
        for (StateComponent stateComponent : states) {
            if (stateComponent.isInitial())
                return stateComponent.getLabel();
        }
        return null;
    }

    public Set<String> getFinalStates() {
        Set<String> ret = new TreeSet<>();
        for (StateComponent stateComponent : states)
            if (stateComponent.isFinal())
                ret.add(stateComponent.getLabel());
        return ret;
    }

    public TransitionFunction getTransitionFunction() {
        HashMap<Pair<String, Character>, String> table = new HashMap<>();
        for (Pair<StateComponent, StateComponent> pair : edges.keySet()) {
            String stateFrom, stateTo;
            stateFrom = pair.getKey().getLabel();
            stateTo = pair.getValue().getLabel();
            String[] simbols = edges.get(pair).getLabel().split("-");

            for (String simbol : simbols) {
                table.put(new Pair<String, Character>(stateFrom, simbol.charAt(0)), stateTo);
            }
        }

        return (state, c) -> {
            for (Pair<String, Character> pair : table.keySet()) {
                if (pair.getKey().equals(state) && pair.getValue().equals(c))
                    return table.get(pair);
            }
            return null;
        };
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
            if (selectedEdge == edge)
                edge.draw(g2d, true);
            else
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
                grabFocus();
                if (e.getClickCount() >= 2 && SwingUtilities.isLeftMouseButton(e)) {
                    insertState(e.getX(), e.getY());
                } if (SwingUtilities.isRightMouseButton(e) && selectedState != null) {
                    openStateMenu(e.getXOnScreen(), e.getYOnScreen());
                } if (SwingUtilities.isLeftMouseButton(e)) {
                    selectObject(e.getX(), e.getY());
                } if (SwingUtilities.isRightMouseButton(e) && selectedState == null) {
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
                    deleteObject();
                } else if (Character.isAlphabetic(e.getKeyChar())) {
                    renameEdge(e.getKeyChar());
                } else if (e.getKeyCode() == 8) {
                    renameEdge();
                }
            }
        });
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                statePopupMenu.setVisible(false);
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

    private void deleteObject() {
        if (selectedState != null) {
            states.remove(selectedState);
            edges.keySet().removeIf(pair -> selectedState == pair.getKey() || selectedState == pair.getValue());
            selectedState = null;
            refresh();
        } else if (selectedEdge != null) {
            edges.keySet().removeIf(pair -> edges.get(pair) == selectedEdge);
            refresh();
        }
    }

    private void renameEdge(char c) {
        if (selectedEdge != null) {
            if (selectedEdge.getLabel().equals("ε"))
                selectedEdge.setLabel(c + "");
            else
                selectedEdge.setLabel(selectedEdge.getLabel() + '-' + c);
            refresh();
        }
    }

    private void renameEdge() {
        if (selectedEdge != null) {
            String label = selectedEdge.getLabel();
            if (label.length() > 1)
                selectedEdge.setLabel(selectedEdge.getLabel().substring(0, label.length() - 2));
            else if (label.length() == 1)
                selectedEdge.setLabel("ε");
            refresh();
        }
    }

    private void selectObject(int x, int y) {
        statePopupMenu.setVisible(false);
        selectedState = null;
        selectedEdge = null;
        for (StateComponent stateComponent : states) {
            if (stateComponent.contains(x, y)) {
                selectedState = stateComponent;
                break;
            }
        }
        for (Pair<StateComponent, StateComponent> key : edges.keySet()) {
            LinkComponent edge = edges.get(key);
            if (edge.contains(x, y)) {
                selectedEdge = edge;
                break;
            }
        }
        refresh();
    }

    private void openStateMenu(int x, int y) {
        statePopupMenu.setLocation(x, y);
        statePopupMenu.setAlignmentX(x);
        statePopupMenu.setAlignmentY(y);
        statePopupMenu.setState(selectedState);
        statePopupMenu.setVisible(true);
        refresh();
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
            LinkComponent edge = new LinkComponent(tempFirstState, tempSecondState, "ε");
            Pair<StateComponent, StateComponent> pair = new Pair<>(tempFirstState, tempSecondState);
            edges.put(pair, edge);
            refresh();
        }

        tempFirstState = tempSecondState = null;
    }

    private StatePopupMenu statePopupMenu;
    private Line2D guideArrow;
    private StateComponent selectedState, tempFirstState, tempSecondState;
    private LinkComponent selectedEdge;
    private List<StateComponent> states;
    private Map<Pair<StateComponent, StateComponent>, LinkComponent> edges;

}
