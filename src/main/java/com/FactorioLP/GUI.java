package com.FactorioLP;

import static com.FactorioLP.Item.ALL_ITEMS;
import static com.FactorioLP.Item.nameToItem;
import static com.FactorioLP.Solver.Supplies;
import static com.FactorioLP.Solver.Targets;

import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;

public class GUI {

  static final String[] solutionColumnNames = {"Target", "# of Producers", "Units per Minute"};
  static final String[] supplyColumnNames = {"Supply", "Units per Second"};
  private static final ComboBoxModel<String> comboBoxModelAllItems = new DefaultComboBoxModel<>(
      Arrays.stream(ALL_ITEMS).map(Item::toString).toArray(String[]::new));
  static List<String> itemConstraintsText = new ArrayList<>();
  static List<String> supplyConstraintsText = new ArrayList<>();
  static List<String> targetConstraintsText = new ArrayList<>();
  static List<String> productionConstraintsText = new ArrayList<>();
  static List<String> sourceText = new ArrayList<>();
  static List<String> solutionText = new ArrayList<>();
  static List<String> spmText = new ArrayList<>();
  static ArrayList<Object[]> solutionMatrix = new ArrayList<>();
  private JPanel panelMain;
  private JTextPane paneOutput1;
  private JTextPane paneItemConstraints;
  private JTextPane paneSupplyConstraints;
  private JTextPane paneSPM;
  private JTextPane paneSources;
  private JTextPane paneTargets;
  private JButton buttonSolve;
  private JSplitPane splitPaneSPM;
  private JTextPane paneProductionConstraints;
  private JPanel panelItemList;
  private JTable tableSolution;
  private JSpinner spinnerTargetPriority;
  private JComboBox comboBoxSupply;
  private JPanel panelConstraints;
  private JSpinner spinnerSupplyPerSec;
  private JButton buttonSetSupply;
  private JPanel panelAddNewSupply;
  private JSplitPane splitSupplySpinner;
  private JTable tableSupplies;
  private JTable tableTarget;
  private JPanel panelAddNewTarget;
  private JComboBox comboBoxTarget;
  private JSplitPane splitTargetSpinner;
  private JButton buttonSetTarget;
  private JButton clearTargetsButton;
  private JButton clearSuppliesButton;
  private JButton buttonRemoveSupply;
  private JButton buttonRemoveTarget;
  private JList listSupply;
  private JSpinner spinnerTarget;

  public GUI() {
    $$$setupUI$$$();

    makeSolveButton();
    // Targets tab
    updateTargetsPane();
    makeSetTargetButton();

    // Supplies tab
    updateSuppliesPane();
    makeSetSupplyButton();
    clearTargetsButton.addActionListener(e -> {
      Solver.Targets.clear();
      updateTargetsPane();
    });
    clearSuppliesButton.addActionListener(e -> {
      Solver.Supplies.clear();
      updateSuppliesPane();
    });
    buttonRemoveTarget.addActionListener(e -> {
      Item selected = nameToItem((String) comboBoxTarget.getSelectedItem());
      Solver.Targets.remove(selected);
      updateTargetsPane();
    });
    buttonRemoveSupply.addActionListener(e -> {
      Item selected = nameToItem((String) comboBoxSupply.getSelectedItem());
      Solver.Supplies.remove(selected);
      updateSuppliesPane();
    });
  }

  private static void setDefaults() {
    final Map<Item, Double> defaultSupplies = Map.of(Item.COPPER_PLATE, 4 * 30.0,
        Item.IRON_PLATE, 4 * 30.0, Item.COAL, 2 * 30.0, Item.STONE, 2 * 30.0,
        Item.PETROLEUM_GAS, 1 * 3000.0, Item.WATER, 1 * 3000.0, Item.LUBRICANT, 1 * 3000.0
    );

    final ArrayList<Item> defaultTargets = new ArrayList<>(
        List.of(new Item[]{Item.AUTOMATION_SCIENCE_PACK, Item.CHEMICAL_SCIENCE_PACK,
            Item.LOGISTIC_SCIENCE_PACK, Item.MILITARY_SCIENCE_PACK,
            Item.UTILITY_SCIENCE_PACK, Item.PRODUCTION_SCIENCE_PACK}));

    Supplies.clear();
    Targets.clear();
    Supplies.putAll(defaultSupplies);
    Targets.addAll(defaultTargets);
  }

  public static void main(String[] args) {
    try {
      FlatNordIJTheme.setup();
    } catch (Exception ex) {
      System.err.println("Failed to initialize LaF");
    }

    setDefaults();

    JFrame frame = new JFrame("GUI");
    frame.setContentPane(new GUI().panelMain);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

  public static void updateTextStrings(List<List<String>> list) {
    itemConstraintsText = list.get(0);
    supplyConstraintsText = list.get(1);
    targetConstraintsText = list.get(2);
    productionConstraintsText = list.get(3);
    spmText = list.get(4);
  }

  public static void updateSolutionMatrix(ArrayList<Object[]> arr) {
    solutionMatrix = arr;
  }

  private void makeSolveButton() {
    buttonSolve.setEnabled(!Targets.isEmpty());
    buttonSolve.addActionListener(e -> {
      Solver.main(new String[0]);
      updateConstraintPanes();
      updateSolutionPanes();
    });
  }

  private void makeSetSupplyButton() {
    buttonSetSupply.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        double supplyPerSec = (Integer) spinnerSupplyPerSec.getValue();
        int ind = comboBoxSupply.getSelectedIndex();
        Item item = ALL_ITEMS[ind];
        Solver.Supplies.remove(item);
        Solver.Supplies.put(item, supplyPerSec);
        updateSuppliesPane();
      }
    });
  }

  private void makeSetTargetButton() {
    buttonSetTarget.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int ind = comboBoxTarget.getSelectedIndex();
        Item item = ALL_ITEMS[ind];
        Solver.Targets.remove(item);
        Solver.Targets.add(item);
        buttonSolve.setEnabled(true);
        updateTargetsPane();
      }
    });
  }

  private void updateTargetsPane() {
    DefaultTableModel model = (DefaultTableModel) tableTarget.getModel();
    model.setNumRows(0);
    for (Item item : Targets) {
      model.addRow(new String[]{item.toString()});
    }

    panelMain.revalidate();
  }

  private void updateSuppliesPane() {
    DefaultTableModel model = (DefaultTableModel) tableSupplies.getModel();
    model.setNumRows(0);
    for (Item item : Supplies.keySet()) {
      model.addRow(new Object[]{item.toString(), Supplies.get(item)});
    }
    panelMain.revalidate();
  }

  private void updateConstraintPanes() {
    Solver.main(new String[]{"false"});
    setPaneText(paneItemConstraints, itemConstraintsText);
    setPaneText(paneSupplyConstraints, supplyConstraintsText);
    setPaneText(paneProductionConstraints, productionConstraintsText);
    panelMain.revalidate();
  }

  private void updateSolutionPanes() {
    setPaneText(paneSPM, spmText);

    DefaultTableModel model = (DefaultTableModel) tableSolution.getModel();
    model.setNumRows(0);
    for (Object[] o : solutionMatrix) {
      model.addRow(o);
    }

    panelMain.revalidate();
  }

  private void setPaneText(JTextPane pane, List<String> textList) {
    pane.setText(String.join("\n", textList));
    pane.setCaretPosition(0);
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
   * call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    createUIComponents();
    panelMain = new JPanel();
    panelMain.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
    Font panelMainFont = UIManager.getFont("TextPane.font");
    if (panelMainFont != null) {
      panelMain.setFont(panelMainFont);
    }
    final JToolBar toolBar1 = new JToolBar();
    toolBar1.setBorderPainted(true);
    toolBar1.setEnabled(true);
    toolBar1.setFloatable(false);
    panelMain.add(toolBar1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
    toolBar1.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "FactorioLP",
            TitledBorder.LEADING, TitledBorder.BOTTOM,
            this.$$$getFont$$$(null, -1, 14, toolBar1.getFont()), null));
    final JSplitPane splitPane1 = new JSplitPane();
    splitPane1.setContinuousLayout(true);
    splitPane1.setDividerLocation(250);
    splitPane1.setDividerSize(0);
    splitPane1.setEnabled(false);
    splitPane1.setOneTouchExpandable(false);
    panelMain.add(splitPane1,
        new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
            new Dimension(400, 400), null, null, 0, false));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
    splitPane1.setRightComponent(panel1);
    panel1.setBorder(
        BorderFactory.createTitledBorder(null, "Solution", TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION, null, null));
    splitPaneSPM = new JSplitPane();
    splitPaneSPM.setDividerLocation(140);
    splitPaneSPM.setDividerSize(0);
    splitPaneSPM.setEnabled(false);
    splitPaneSPM.setOneTouchExpandable(false);
    splitPaneSPM.setOrientation(1);
    splitPaneSPM.setResizeWeight(0.0);
    panel1.add(splitPaneSPM,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null,
            new Dimension(250, -1), new Dimension(250, -1), 0, false));
    final JLabel label1 = new JLabel();
    label1.setHorizontalAlignment(0);
    label1.setHorizontalTextPosition(0);
    label1.setText("Targets per minute:");
    label1.setVerticalAlignment(0);
    splitPaneSPM.setLeftComponent(label1);
    paneSPM = new JTextPane();
    paneSPM.setEditable(false);
    splitPaneSPM.setRightComponent(paneSPM);
    final JScrollPane scrollPane1 = new JScrollPane();
    panel1.add(scrollPane1,
        new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null,
            null, null, 0, false));
    scrollPane1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null,
        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    tableSolution.setAutoCreateRowSorter(true);
    tableSolution.setAutoResizeMode(2);
    tableSolution.setEnabled(false);
    scrollPane1.setViewportView(tableSolution);
    final JTabbedPane tabbedPane1 = new JTabbedPane();
    tabbedPane1.setTabLayoutPolicy(0);
    tabbedPane1.setTabPlacement(1);
    splitPane1.setLeftComponent(tabbedPane1);
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
    tabbedPane1.addTab("Targets", panel2);
    final JScrollPane scrollPane2 = new JScrollPane();
    panel2.add(scrollPane2,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null,
            null, null, 0, false));
    scrollPane2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null,
        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    tableTarget.setAutoCreateRowSorter(true);
    tableTarget.setEnabled(false);
    scrollPane2.setViewportView(tableTarget);
    panelAddNewTarget = new JPanel();
    panelAddNewTarget.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
    panel2.add(panelAddNewTarget, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
        null, 0, false));
    panelAddNewTarget.setBorder(
        BorderFactory.createTitledBorder(null, "Set Target", TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION, null, null));
    panelAddNewTarget.add(comboBoxTarget,
        new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST,
            GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    splitTargetSpinner = new JSplitPane();
    splitTargetSpinner.setDividerLocation(125);
    splitTargetSpinner.setDividerSize(0);
    splitTargetSpinner.setEnabled(false);
    panelAddNewTarget.add(splitTargetSpinner,
        new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_SOUTH,
            GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null,
            new Dimension(200, 10), null, 0, false));
    final JLabel label2 = new JLabel();
    label2.setHorizontalTextPosition(10);
    label2.setText("Priority");
    splitTargetSpinner.setLeftComponent(label2);
    splitTargetSpinner.setRightComponent(spinnerTargetPriority);
    buttonSetTarget = new JButton();
    buttonSetTarget.setText("Set");
    panelAddNewTarget.add(buttonSetTarget,
        new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    buttonRemoveTarget = new JButton();
    buttonRemoveTarget.setText("Remove");
    panelAddNewTarget.add(buttonRemoveTarget,
        new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    clearTargetsButton = new JButton();
    clearTargetsButton.setText("Clear All");
    panel2.add(clearTargetsButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel3 = new JPanel();
    panel3.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
    tabbedPane1.addTab("Supplies", panel3);
    final JScrollPane scrollPane3 = new JScrollPane();
    scrollPane3.setEnabled(false);
    panel3.add(scrollPane3,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null,
            null, null, 0, false));
    scrollPane3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null,
        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    tableSupplies.setAutoCreateRowSorter(true);
    tableSupplies.setEnabled(false);
    tableSupplies.setShowHorizontalLines(false);
    scrollPane3.setViewportView(tableSupplies);
    panelAddNewSupply = new JPanel();
    panelAddNewSupply.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
    panel3.add(panelAddNewSupply, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
        null, 0, false));
    panelAddNewSupply.setBorder(
        BorderFactory.createTitledBorder(null, "Set Supply", TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION, null, null));
    panelAddNewSupply.add(comboBoxSupply,
        new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST,
            GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    splitSupplySpinner = new JSplitPane();
    splitSupplySpinner.setDividerLocation(125);
    splitSupplySpinner.setDividerSize(0);
    splitSupplySpinner.setEnabled(false);
    splitSupplySpinner.setResizeWeight(0.0);
    panelAddNewSupply.add(splitSupplySpinner,
        new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_SOUTH,
            GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    final JLabel label3 = new JLabel();
    label3.setHorizontalAlignment(10);
    label3.setText("Supply per sec:");
    label3.setToolTipText("15/30/45 per full yellow/red/blue/belt respectively");
    splitSupplySpinner.setLeftComponent(label3);
    splitSupplySpinner.setRightComponent(spinnerSupplyPerSec);
    buttonSetSupply = new JButton();
    buttonSetSupply.setText("Set");
    panelAddNewSupply.add(buttonSetSupply,
        new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    buttonRemoveSupply = new JButton();
    buttonRemoveSupply.setText("Remove");
    panelAddNewSupply.add(buttonRemoveSupply,
        new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
    clearSuppliesButton = new JButton();
    clearSuppliesButton.setText("Clear All");
    panel3.add(clearSuppliesButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    buttonSolve = new JButton();
    buttonSolve.setHorizontalAlignment(0);
    buttonSolve.setText("Solve");
    buttonSolve.setMnemonic('S');
    buttonSolve.setDisplayedMnemonicIndex(0);
    panelMain.add(buttonSolve,
        new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), new Dimension(100, -1),
            0, false));
    panelConstraints = new JPanel();
    panelConstraints.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
    panelMain.add(panelConstraints,
        new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null,
            new Dimension(-1, 400), null, 0, false));
    final JScrollPane scrollPane4 = new JScrollPane();
    panelConstraints.add(scrollPane4,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    scrollPane4.setBorder(BorderFactory.createTitledBorder(null, "Item Constraints",
        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    paneItemConstraints = new JTextPane();
    paneItemConstraints.setEditable(false);
    Font paneItemConstraintsFont = this.$$$getFont$$$("Iosevka", -1, 12,
        paneItemConstraints.getFont());
    if (paneItemConstraintsFont != null) {
      paneItemConstraints.setFont(paneItemConstraintsFont);
    }
    paneItemConstraints.setText("");
    scrollPane4.setViewportView(paneItemConstraints);
    final JScrollPane scrollPane5 = new JScrollPane();
    panelConstraints.add(scrollPane5,
        new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    scrollPane5.setBorder(BorderFactory.createTitledBorder(null, "Production Constraints",
        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    paneProductionConstraints = new JTextPane();
    paneProductionConstraints.setEditable(false);
    Font paneProductionConstraintsFont = this.$$$getFont$$$("Iosevka", -1, 12,
        paneProductionConstraints.getFont());
    if (paneProductionConstraintsFont != null) {
      paneProductionConstraints.setFont(paneProductionConstraintsFont);
    }
    scrollPane5.setViewportView(paneProductionConstraints);
    final JScrollPane scrollPane6 = new JScrollPane();
    panelConstraints.add(scrollPane6,
        new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    scrollPane6.setBorder(BorderFactory.createTitledBorder(null, "Supply Constraints",
        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    paneSupplyConstraints = new JTextPane();
    paneSupplyConstraints.setEditable(false);
    Font paneSupplyConstraintsFont = this.$$$getFont$$$("Iosevka", -1, 12,
        paneSupplyConstraints.getFont());
    if (paneSupplyConstraintsFont != null) {
      paneSupplyConstraints.setFont(paneSupplyConstraintsFont);
    }
    scrollPane6.setViewportView(paneSupplyConstraints);
    label1.setLabelFor(paneSPM);
  }

  /**
   * @noinspection ALL
   */
  private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
    if (currentFont == null) {
      return null;
    }
    String resultName;
    if (fontName == null) {
      resultName = currentFont.getName();
    } else {
      Font testFont = new Font(fontName, Font.PLAIN, 10);
      if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
        resultName = fontName;
      } else {
        resultName = currentFont.getName();
      }
    }
    Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(),
        size >= 0 ? size : currentFont.getSize());
    boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
    Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize())
        : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
    return fontWithFallback instanceof FontUIResource ? fontWithFallback
        : new FontUIResource(fontWithFallback);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return panelMain;
  }

  private void createUIComponents() {
    // Items tab
    // "priority" spinner
    SpinnerNumberModel spinnerModelPriority = new SpinnerNumberModel(1, 1, 99999, 1);
    spinnerTarget = new JSpinner(spinnerModelPriority);
    // item picker
    comboBoxTarget = new JComboBox(comboBoxModelAllItems);
    // target list
    DefaultTableModel tableModelTarget = new DefaultTableModel((new String[]{"Item"}), 0);
    tableTarget = new JTable(tableModelTarget);

    // Supply tab
    // "Supply Per Item" spinner
    SpinnerNumberModel spinnerModelSupplyBelt = new SpinnerNumberModel(30, 1, 99999, 15);
    spinnerSupplyPerSec = new JSpinner(spinnerModelSupplyBelt);
    // item picker
    comboBoxSupply = new JComboBox(comboBoxModelAllItems);
    // supply list
    DefaultTableModel tableModelSupply = new DefaultTableModel(supplyColumnNames, 0);
    tableSupplies = new JTable(tableModelSupply);

    // Solution pane
    // table
    DefaultTableModel tableModelSolution = new DefaultTableModel(solutionColumnNames, 0);
    tableSolution = new JTable(tableModelSolution);

  }
}
