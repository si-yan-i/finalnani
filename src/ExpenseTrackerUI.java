import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class ExpenseTrackerUI extends JFrame {


    static final Color SIDEBAR_BG     = new Color(15,  23,  42);
    static final Color SIDEBAR_HOVER  = new Color(51,  65,  85);
    static final Color SIDEBAR_ACTIVE = new Color(47, 106, 229);

    static final Color PAGE_BG = new Color(236, 240, 249);

    // Metric card
    static final Color CARD_TOTAL_BG  = new Color(235, 244, 255);
    static final Color CARD_TOTAL_FG  = new Color( 24,  90, 190);
    static final Color CARD_BUDGET_BG = new Color(232, 248, 240);
    static final Color CARD_BUDGET_FG = new Color( 15, 110,  72);
    static final Color CARD_REMAIN_BG = new Color(255, 247, 230);
    static final Color CARD_REMAIN_FG = new Color(150,  85,   0);
    static final Color CARD_COUNT_BG  = new Color(243, 235, 255);
    static final Color CARD_COUNT_FG  = new Color( 90,  40, 180);

    //  Left-panel card
    static final Color ADD_CARD_BG     = new Color(255, 255, 255);
    static final Color ADD_CARD_B      = new Color(12, 42, 76);
    static final Color ADD_CARD_BORDER = new Color(198, 218, 255);
    static final Color ADD_HEADER_BAR  = new Color(47, 106, 229);

    static final Color BUDGET_CARD_BG     = new Color(240, 253, 246);
    static final Color BUDGET_CARD_B      = new Color(165, 212, 173);
    static final Color BUDGET_CARD_BORDER = new Color(167, 224, 196);
    static final Color BUDGET_HEADER_BAR  = new Color(22, 138, 86);

    static final Color WEEKLY_CARD_B      = new Color(172, 160, 209);
    static final Color WEEKLY_CARD_BORDER = new Color(200, 180, 240);
    static final Color WEEKLY_HEADER_BAR  = new Color(90, 40, 180);

    //  Right-panel card
    static final Color DONUT_CARD_BG    = new Color(255, 195, 243);
    static final Color DONUT_CARD_BORDER = new Color(230, 180, 218);
    static final Color DONUT_HEADER_BAR  = new Color(172, 36, 120);

    static final Color SEARCH_CARD_BG    = new Color(12, 42, 76);
    static final Color SEARCH_CARD_B     = new Color(0, 128, 255);
    static final Color SEARCH_CARD_BORDER = new Color(180, 210, 245);
    static final Color SEARCH_HEADER_BAR  = new Color(178, 216, 255);

    // Table panel
    static final Color TABLE_PANEL_BG   = new Color(22,  33,  62);
    static final Color TABLE_HEADER_BG  = new Color(30,  41,  59);
    static final Color TABLE_HEADER_F   = new Color(206, 234, 255);
    static final Color TABLE_ROW_BG     = new Color(15,  23,  42);
    static final Color TABLE_ROW_ALT_BG = new Color(33,  48,  84);
    static final Color TABLE_ROW_FG     = new Color(251, 255, 254);
    static final Color TABLE_SEL_BG     = new Color(47, 106, 229);
    static final Color TABLE_SEL_FG     = Color.WHITE;
    static final Color TABLE_BORDER     = new Color(45,  65, 110);
    static final Color TABLE_AMT_FG     = new Color(100, 220, 160);

    // General accents
    static final Color ACCENT_BLUE  = new Color(47, 106, 229);
    static final Color ACCENT_GREEN = new Color(22, 138,  86);
    static final Color ACCENT_RED   = new Color(220,  53,  69);
    static final Color ACCENT_AMBER = new Color(180, 100,   0);

    static final Color[] CAT_COLORS = {
            new Color(29, 158, 117), new Color(55, 138, 221),
            new Color(212, 83, 126), new Color(226,  75,  74),
            new Color(186, 117,  23), new Color(136, 135, 128),
    };

    //  Dynamic colors
    final Color BG_SECONDARY;
    final Color TEXT_PRIMARY;
    final Color TEXT_MUTED;
    final Color BORDER_COLOR;

    static Color uiColor(String k1, String k2, Color fb) {
        Color c = UIManager.getColor(k1);
        if (c != null) return c;
        c = UIManager.getColor(k2);
        return c != null ? c : fb;
    }

    Toast  toast;

    private String  loggedInUser = "User";
    private JPanel  contentArea;
    private CardLayout cardLayout;


    private DashboardPanel      dashboardPanel;
    private ExpensesPanel       expensesPanel;
    private CategoriesPanel     categoriesPanel;
    private BudgetPanel         budgetPanel;
    private WeeklyPanel         weeklyPanel;


    private JButton activeNavBtn = null;

    // ─────────────────────────────────────────────────────────────────────
    public ExpenseTrackerUI(String username) {
        super("CentSible: Expense Tracker");
        this.loggedInUser = (username != null && !username.isBlank()) ? username : "User";

        BG_SECONDARY = uiColor("TextField.background", "controlHighlight",         new Color(230, 235, 248));
        TEXT_PRIMARY = uiColor("Label.foreground",      "TextField.foreground",     new Color(28,  35,  55));
        TEXT_MUTED   = uiColor("textInactiveText",      "Label.disabledForeground", new Color(110, 120, 150));
        BORDER_COLOR = uiColor("Component.borderColor", "Separator.foreground",     new Color(200, 210, 230));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1080, 700));
        setBackground(PAGE_BG);
        toast = new Toast(this);

        cardLayout  = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(PAGE_BG);

        // sub-panels
        dashboardPanel  = new DashboardPanel(this);
        expensesPanel   = new ExpensesPanel(this);
        categoriesPanel = new CategoriesPanel(this);
        budgetPanel     = new BudgetPanel(this);
        weeklyPanel     = new WeeklyPanel(this);

        contentArea.add(dashboardPanel,  "Dashboard");
        contentArea.add(expensesPanel,   "Expenses");
        contentArea.add(categoriesPanel, "Categories");
        contentArea.add(budgetPanel,     "Budget");
        contentArea.add(weeklyPanel,     "WeeklyBreakdown");

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(PAGE_BG);
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(contentArea,   BorderLayout.CENTER);
        add(root);

        setResizable(true);
        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        refreshAll();
        showPage("Dashboard");
    }

    // ─────────────────────────────────────────────────────────────────────
    //  SIDEBARRRRRRRRRRRRRRRRRRRRRR
    // ─────────────────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sb = new JPanel();
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));
        sb.setBackground(SIDEBAR_BG);
        sb.setPreferredSize(new Dimension(250, 0));
        sb.setBorder(new EmptyBorder(18, 16, 18, 16));

        JLabel logo = new JLabel("💰 CentSible");
        logo.setFont(new Font("SansSerif", Font.BOLD, 28));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLbl = new JLabel("Expense Tracker");
        subtitleLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitleLbl.setForeground(new Color(180, 190, 210));
        subtitleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        sb.add(logo);
        sb.add(Box.createVerticalStrut(4));
        sb.add(subtitleLbl);
        sb.add(Box.createVerticalStrut(30));

        // Profile
        JPanel profile = new JPanel();
        profile.setOpaque(false);
        profile.setLayout(new BoxLayout(profile, BoxLayout.X_AXIS));
        profile.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel avatar = new JLabel(loggedInUser.substring(0, 1).toUpperCase());
        avatar.setOpaque(true);
        avatar.setBackground(new Color(37, 99, 235));
        avatar.setForeground(Color.WHITE);
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setFont(new Font("SansSerif", Font.BOLD, 22));
        avatar.setPreferredSize(new Dimension(50, 50));
        avatar.setMaximumSize(new Dimension(50, 50));
        avatar.setBorder(new LineBorder(new Color(37, 99, 235), 1, true));

        JPanel userInfo = new JPanel();
        userInfo.setOpaque(false);
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
        userInfo.setBorder(new EmptyBorder(0, 12, 0, 0));
        JLabel userLabel = new JLabel(loggedInUser);
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel roleLabel = new JLabel("Personal account");
        roleLabel.setForeground(new Color(180, 190, 210));
        userInfo.add(userLabel);
        userInfo.add(roleLabel);
        profile.add(avatar);
        profile.add(userInfo);
        sb.add(profile);

        sb.add(Box.createVerticalStrut(30));

        // Navigation items
        JButton dashBtn    = navBtn("🏠", "Dashboard",        "Dashboard");
        JButton expBtn     = navBtn("💸", "Expenses",         "Expenses");
        JButton catBtn     = navBtn("📂", "Categories",       "Categories");
        JButton budBtn     = navBtn("🎯", "Budget",           "Budget");
        JButton wkBtn      = navBtn("📅", "Weekly Breakdown", "WeeklyBreakdown");


        setActiveSidebarBtn(dashBtn);

        sb.add(wrap(dashBtn));
        sb.add(wrap(expBtn));
        sb.add(wrap(catBtn));
        sb.add(wrap(budBtn));
        sb.add(wrap(wkBtn));

        sb.add(Box.createVerticalStrut(30));

        JLabel actions = new JLabel("ACTIONS");
        actions.setForeground(new Color(120, 140, 170));
        actions.setFont(new Font("SansSerif", Font.BOLD, 11));
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        sb.add(actions);
        sb.add(Box.createVerticalStrut(10));

        JButton chartBtn = actionBtn("📈", "View Bar Chart", () -> ExpenseTracker.showChart());
        JButton csvBtn   = actionBtn("⬇",  "Export CSV",     this::exportCSV);
        sb.add(wrap(chartBtn));
        sb.add(wrap(csvBtn));

        sb.add(Box.createVerticalGlue());
        sb.add(Box.createVerticalStrut(10));

        JLabel accounts = new JLabel("ACCOUNTS");
        accounts.setForeground(new Color(120, 140, 170));
        accounts.setFont(new Font("SansSerif", Font.BOLD, 11));
        accounts.setAlignmentX(Component.LEFT_ALIGNMENT);
        sb.add(accounts);

        JButton logoutBtn = actionBtn("⎋", "Logout", this::handleLogout);
        sb.add(wrap(logoutBtn));
        sb.add(Box.createVerticalStrut(20));

        JLabel version = new JLabel("CentSible v1.0");
        version.setForeground(new Color(120, 140, 170));
        version.setFont(new Font("SansSerif", Font.PLAIN, 11));
        version.setAlignmentX(Component.LEFT_ALIGNMENT);
        sb.add(version);

        return sb;
    }


    private JButton navBtn(String icon, String label, String card) {
        JButton btn = new JButton(icon + "  " + label);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(SIDEBAR_BG);
        btn.setForeground(new Color(220, 230, 245));
        btn.setBorder(new EmptyBorder(14, 16, 14, 16));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (activeNavBtn != btn) btn.setBackground(SIDEBAR_HOVER);
            }
            public void mouseExited(MouseEvent e) {
                if (activeNavBtn != btn) btn.setBackground(SIDEBAR_BG);
            }
        });
        btn.addActionListener(e -> {
            setActiveSidebarBtn(btn);
            showPage(card);
        });
        return btn;
    }


    private JButton actionBtn(String icon, String label, Runnable action) {
        JButton btn = new JButton(icon + "  " + label);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(SIDEBAR_BG);
        btn.setForeground(new Color(220, 230, 245));
        btn.setBorder(new EmptyBorder(14, 16, 14, 16));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(SIDEBAR_HOVER); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(SIDEBAR_BG); }
        });
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private JPanel wrap(JButton btn) {
        JPanel w = new JPanel(new BorderLayout());
        w.setOpaque(false);
        w.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        w.setBorder(new EmptyBorder(4, 0, 4, 0));
        w.add(btn);
        return w;
    }

    private void setActiveSidebarBtn(JButton btn) {
        if (activeNavBtn != null) {
            activeNavBtn.setBackground(SIDEBAR_BG);
            activeNavBtn.setForeground(new Color(220, 230, 245));
        }
        activeNavBtn = btn;
        btn.setBackground(SIDEBAR_ACTIVE);
        btn.setForeground(Color.WHITE);
    }

    // ─────────────────────────────────────────────────────────────────────
    //  PAGE NAVIGATION
    // ─────────────────────────────────────────────────────────────────────
    void showPage(String name) {
        cardLayout.show(contentArea, name);
        refreshAll();
    }

    // ─────────────────────────────────────────────────────────────────────
    //  SHARED REFRESH — pushes latest data to ALL sub-panels hihihihi
    // ─────────────────────────────────────────────────────────────────────
    void refreshAll() {
        List<Expense> all   = ExpenseTracker.getExpensesList();
        double total  = ExpenseTracker.calculateTotal();
        double budget = ExpenseTracker.getBudget();
        dashboardPanel .refresh(all, total, budget);
        expensesPanel  .refresh(all);
        categoriesPanel.refresh(all);
        budgetPanel    .refresh(total, budget);
        weeklyPanel    .refresh();
    }

    // ─────────────────────────────────────────────────────────────────────
    //  ACTIONS
    // ─────────────────────────────────────────────────────────────────────
    void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to log out?", "Log Out",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            String user = loggedInUser; dispose();
            SwingUtilities.invokeLater(() -> new LogoutUI(user).setVisible(true));
        }
    }

    void exportCSV() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("expenses_export.csv"));
        int res = fc.showSaveDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            try {
                ExpenseTracker.exportToCSV(fc.getSelectedFile().getAbsolutePath());
                toast.show("Exported: " + fc.getSelectedFile().getName(), ACCENT_GREEN);
            } catch (Exception ex) { toast.show("Export failed: " + ex.getMessage(), ACCENT_RED); }
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    //  SHARED HELPERS
    // ─────────────────────────────────────────────────────────────────────
    String fmt(double v) { return String.format("₱%.2f", v); }

    JPanel pageHeader(String title, String subtitle) {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(new EmptyBorder(0, 0, 18, 0));
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel t = new JLabel(title);
        t.setFont(new Font("SansSerif", Font.BOLD, 34));
        t.setForeground(new Color(12, 24, 48));
        JLabel s = new JLabel(subtitle);
        s.setFont(new Font("SansSerif", Font.PLAIN, 15));
        s.setForeground(new Color(100, 116, 139));
        left.add(t); left.add(Box.createVerticalStrut(4)); left.add(s);
        top.add(left, BorderLayout.WEST);
        return top;
    }

    JPanel colorCard(String title, Color bodyBg, Color borderColor, Color headerBg, Color headerFg) {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(bodyBg);
        outer.setBorder(new LineBorder(borderColor, 1, true));
        JPanel strip = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 7));
        strip.setBackground(headerBg);
        JLabel lbl = new JLabel(title.toUpperCase());
        lbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        lbl.setForeground(headerFg);
        strip.add(lbl);
        outer.add(strip, BorderLayout.NORTH);
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(bodyBg);
        body.setBorder(new EmptyBorder(10, 12, 12, 12));
        outer.add(body, BorderLayout.CENTER);
        return new JPanel(new BorderLayout()) {
            { setOpaque(false); add(outer, BorderLayout.CENTER); }
            public Component add(Component comp) {
                if (comp == outer) return super.add(comp);
                body.add(comp); body.revalidate(); return comp;
            }
            public void add(Component comp, Object constraints) {
                if (comp == outer) { super.add(comp, constraints); return; }
                body.add(comp, constraints); body.revalidate();
            }
        };
    }

    JPanel formRow(String label, JComponent field) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setMaximumSize(new Dimension(300, 42));
        field.setPreferredSize(new Dimension(300, 42));
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(6));
        panel.add(field);
        return panel;
    }

    JTextField inputField(String placeholder, Color bg) {
        JTextField f = new JTextField(18);
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBackground(new Color(
                Math.max(bg.getRed() - 12, 0),
                Math.max(bg.getGreen() - 12, 0),
                Math.max(bg.getBlue() - 12, 0)));
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(TEXT_PRIMARY);
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true), new EmptyBorder(4, 8, 4, 8)));
        f.setToolTipText(placeholder);
        return f;
    }

    void styleCombo(JComboBox<?> cb, Color bg) {
        cb.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cb.setBackground(new Color(
                Math.max(bg.getRed() - 12, 0),
                Math.max(bg.getGreen() - 12, 0),
                Math.max(bg.getBlue() - 12, 0)));
        cb.setForeground(TEXT_PRIMARY);
        cb.setBorder(new LineBorder(BORDER_COLOR, 1, true));
    }

    JButton styledBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(bg.darker(), 1, true), new EmptyBorder(6, 14, 6, 14)));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    JButton darkSmallBtn(String text, Color fg) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 10));
        b.setForeground(fg);
        b.setBackground(new Color(40, 55, 90));
        b.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(fg.darker(), 1, true), new EmptyBorder(2, 7, 2, 7)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(new Color(55, 75, 120)); }
            public void mouseExited(MouseEvent e)  { b.setBackground(new Color(40, 55, 90)); }
        });
        return b;
    }

    // ─────────────────────────────────────────────────────────────────────
    //  INNER: TOAST
    // ─────────────────────────────────────────────────────────────────────
    class Toast {
        private final JWindow window;
        private final JLabel  label;
        private final JFrame  parent;
        private javax.swing.Timer hideTimer;
        Toast(JFrame parent) {
            this.parent = parent;
            window = new JWindow(parent);
            label = new JLabel();
            label.setFont(new Font("SansSerif", Font.BOLD, 12));
            label.setForeground(Color.WHITE);
            label.setBorder(new EmptyBorder(8, 16, 8, 16));
            window.add(label);
            window.setOpacity(0.93f);
        }
        void show(String msg, Color bg) {
            label.setText(msg);
            window.getContentPane().setBackground(bg);
            window.pack();
            Point loc = parent.getLocation();
            Dimension ps = parent.getSize();
            window.setLocation(loc.x + ps.width - window.getWidth() - 24,
                    loc.y + ps.height - window.getHeight() - 24);
            window.setVisible(true);
            if (hideTimer != null) hideTimer.stop();
            hideTimer = new javax.swing.Timer(2200, e -> window.setVisible(false));
            hideTimer.setRepeats(false);
            hideTimer.start();
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}


// ═════════════════════════════════════════════════════════════════════════════
//  DASHBOARD PANEL
// ═════════════════════════════════════════════════════════════════════════════
class DashboardPanel extends JPanel {

    private final ExpenseTrackerUI ui;
    private JLabel totalLabel, budgetLabel, remainingLabel, countLabel;
    private DonutChartPanel donutChart;
    private ExpenseCategory activeFilter = null;
    private JTextField searchField;
    private JLabel statusLabel;
    private JTable expenseTable;
    private DefaultTableModel tableModel;

    DashboardPanel(ExpenseTrackerUI ui) {
        this.ui = ui;
        setBackground(ExpenseTrackerUI.PAGE_BG);
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(20, 22, 20, 22));
        buildUI();
    }

    private void buildUI() {
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(ui.pageHeader("Dashboard", "Track your spending, stay in control."));
        header.add(buildMetricsBar());
        add(header, BorderLayout.NORTH);

        JSplitPane mid = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildLeftPanel(), buildRightPanel());
        mid.setOpaque(false);
        mid.setBackground(ExpenseTrackerUI.PAGE_BG);
        mid.setBorder(new EmptyBorder(12, 0, 0, 0));
        mid.setContinuousLayout(true);
        mid.setResizeWeight(0.24);
        mid.setDividerSize(8);
        mid.setOneTouchExpandable(true);
        add(mid, BorderLayout.CENTER);
    }

    private JPanel buildMetricsBar() {
        JPanel p = new JPanel(new GridLayout(1, 4, 12, 0));
        p.setOpaque(false);
        totalLabel     = metricLabel("₱0.00", ExpenseTrackerUI.CARD_TOTAL_FG);
        budgetLabel    = metricLabel("—",      ExpenseTrackerUI.CARD_BUDGET_FG);
        remainingLabel = metricLabel("—",      ExpenseTrackerUI.CARD_REMAIN_FG);
        countLabel     = metricLabel("0",      ExpenseTrackerUI.CARD_COUNT_FG);
        p.add(metricCard("💸 Total Spent",  totalLabel,     ExpenseTrackerUI.CARD_TOTAL_BG,  ExpenseTrackerUI.CARD_TOTAL_FG,  "📈 this month"));
        p.add(metricCard("🎯 Budget",        budgetLabel,    ExpenseTrackerUI.CARD_BUDGET_BG, ExpenseTrackerUI.CARD_BUDGET_FG, "monthly limit"));
        p.add(metricCard("💰 Remaining",     remainingLabel, ExpenseTrackerUI.CARD_REMAIN_BG, ExpenseTrackerUI.CARD_REMAIN_FG, "left to spend"));
        p.add(metricCard("🧾 Expenses",      countLabel,     ExpenseTrackerUI.CARD_COUNT_BG,  ExpenseTrackerUI.CARD_COUNT_FG,  "total entries"));
        return p;
    }

    private JLabel metricLabel(String val, Color c) {
        JLabel l = new JLabel(val);
        l.setFont(new Font("SansSerif", Font.BOLD, 22));
        l.setForeground(c);
        return l;
    }

    private JPanel metricCard(String title, JLabel vl, Color bg, Color accent, String sub) {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(bg);
        outer.setBorder(new LineBorder(accent.brighter(), 1, true));
        JPanel bar = new JPanel(); bar.setBackground(accent); bar.setPreferredSize(new Dimension(4, 0));
        outer.add(bar, BorderLayout.WEST);
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);
        inner.setBorder(new EmptyBorder(18, 18, 18, 18));
        JLabel lbl = new JLabel(title); lbl.setFont(new Font("SansSerif", Font.BOLD, 11)); lbl.setForeground(accent);
        JLabel s   = new JLabel(sub);   s.setFont(new Font("SansSerif", Font.PLAIN, 10));  s.setForeground(accent);
        inner.add(lbl); inner.add(Box.createVerticalStrut(5)); inner.add(vl); inner.add(Box.createVerticalStrut(3)); inner.add(s);
        outer.add(inner, BorderLayout.CENTER);
        return outer;
    }

    // Left panel: Add Expense form lang galing
    private JPanel buildLeftPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.add(buildAddPanel());
        return p;
    }

    private JTextField descField, amtField, dateField;
    private JComboBox<ExpenseCategory> catCombo;
    private JButton addBtn;
    private int editingIndex = -1;

    private JPanel buildAddPanel() {
        JPanel card = ui.colorCard("✦ Add Expense", ExpenseTrackerUI.ADD_CARD_B, ExpenseTrackerUI.ADD_CARD_BORDER, ExpenseTrackerUI.ADD_HEADER_BAR, Color.WHITE);
        descField = ui.inputField("e.g. Grocery run", ExpenseTrackerUI.ADD_CARD_BG);
        amtField  = ui.inputField("0.00",             ExpenseTrackerUI.ADD_CARD_BG);
        dateField = ui.inputField(java.time.LocalDate.now().toString(), ExpenseTrackerUI.ADD_CARD_BG);
        catCombo  = new JComboBox<>(ExpenseCategory.values());
        ui.styleCombo(catCombo, ExpenseTrackerUI.ADD_CARD_BG);
        card.add(ui.formRow("Description",       descField)); card.add(Box.createVerticalStrut(6));
        card.add(ui.formRow("Amount (₱)",        amtField));  card.add(Box.createVerticalStrut(6));
        card.add(ui.formRow("Date (YYYY-MM-DD)", dateField)); card.add(Box.createVerticalStrut(6));
        card.add(ui.formRow("Category",          catCombo));  card.add(Box.createVerticalStrut(10));
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        btnRow.setOpaque(false);
        addBtn = ui.styledBtn("✓ Add Expense", ExpenseTrackerUI.ADD_HEADER_BAR, Color.BLACK);
        JButton clearBtn = ui.styledBtn("Clear", new Color(220, 228, 245), ui.TEXT_PRIMARY);
        addBtn.addActionListener(e -> submitExpense());
        clearBtn.addActionListener(e -> cancelEdit());
        btnRow.add(addBtn); btnRow.add(clearBtn);
        card.add(btnRow);
        return card;
    }

    private void submitExpense() {
        String desc = descField.getText().trim();
        String amtStr = amtField.getText().trim();
        String date = dateField.getText().trim();
        ExpenseCategory cat = (ExpenseCategory) catCombo.getSelectedItem();
        if (desc.isEmpty()) { ui.toast.show("Please enter a description", ExpenseTrackerUI.ACCENT_RED); return; }
        double amt;
        try { amt = Double.parseDouble(amtStr); } catch (NumberFormatException ex) { ui.toast.show("Enter a valid amount", ExpenseTrackerUI.ACCENT_RED); return; }
        if (amt < 0) { ui.toast.show("Amount must be positive", ExpenseTrackerUI.ACCENT_RED); return; }
        if (date.isEmpty() || !date.matches("\\d{4}-\\d{2}-\\d{2}")) { ui.toast.show("Use date format YYYY-MM-DD", ExpenseTrackerUI.ACCENT_RED); return; }
        if (editingIndex >= 0) { ExpenseTracker.updateExpense(editingIndex, desc, amt, date, cat); ui.toast.show("Expense updated ✓", ExpenseTrackerUI.ACCENT_GREEN); }
        else { ExpenseTracker.addExpense(desc, amt, date, cat); ui.toast.show("Expense added ✓", ExpenseTrackerUI.ACCENT_GREEN); }
        cancelEdit();
        ui.refreshAll();
    }

    void editExpense(int realIndex) {
        List<Expense> all = ExpenseTracker.getExpensesList();
        if (realIndex < 0 || realIndex >= all.size()) return;
        Expense e = all.get(realIndex);
        descField.setText(e.getDescription());
        amtField.setText(String.valueOf(e.getAmount()));
        dateField.setText(e.getDate());
        catCombo.setSelectedItem(e.getCategory());
        editingIndex = realIndex;
        addBtn.setText("✓ Save Changes");
        descField.requestFocus();
        ui.toast.show("Editing — make changes and save", ExpenseTrackerUI.ACCENT_AMBER);
    }

    void deleteExpense(int realIndex) {
        int confirm = JOptionPane.showConfirmDialog(ui, "Remove this expense?", "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            ExpenseTracker.removeExpense(realIndex);
            ui.refreshAll();
            ui.toast.show("Expense removed", ExpenseTrackerUI.ACCENT_AMBER);
        }
    }

    private void cancelEdit() {
        editingIndex = -1;
        addBtn.setText("✓ Add Expense");
        descField.setText(""); amtField.setText("");
        dateField.setText(java.time.LocalDate.now().toString());
        catCombo.setSelectedIndex(0);
    }

    //  Right panel: Donut + Search/Filter + Table
    private JPanel buildRightPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setOpaque(false);
        JPanel topRow = new JPanel(new BorderLayout(10, 0));
        topRow.setOpaque(false);
        JPanel donutCard = ui.colorCard("🍩 Manage Categories", ExpenseTrackerUI.DONUT_CARD_BG, ExpenseTrackerUI.DONUT_CARD_BORDER, ExpenseTrackerUI.DONUT_HEADER_BAR, Color.WHITE);
        donutCard.setPreferredSize(new Dimension(240, 280));
        donutChart = new DonutChartPanel(ui);
        donutChart.setPreferredSize(new Dimension(220, 230));
        donutCard.add(donutChart);
        topRow.add(donutCard, BorderLayout.WEST);
        topRow.add(buildSearchFilterPanel(), BorderLayout.CENTER);
        p.add(topRow, BorderLayout.NORTH);
        p.add(buildTablePanel(), BorderLayout.CENTER);
        return p;
    }

    private JPanel buildSearchFilterPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(ExpenseTrackerUI.SEARCH_CARD_BG);
        outer.setBorder(new LineBorder(ExpenseTrackerUI.SEARCH_CARD_BORDER, 1, true));
        JPanel strip = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 8));
        strip.setBackground(ExpenseTrackerUI.SEARCH_CARD_B);
        JLabel hdr = new JLabel("🔍 Search & Filter");
        hdr.setFont(new Font("SansSerif", Font.BOLD, 11));
        hdr.setForeground(Color.WHITE);
        strip.add(hdr);
        outer.add(strip, BorderLayout.NORTH);
        JPanel body = new JPanel(new BorderLayout(0, 8));
        body.setBackground(ExpenseTrackerUI.SEARCH_CARD_BG);
        body.setBorder(new EmptyBorder(10, 14, 10, 14));
        searchField = ui.inputField("Search expenses...", ExpenseTrackerUI.SEARCH_HEADER_BAR);
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { refreshTable(ExpenseTracker.getExpensesList()); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { refreshTable(ExpenseTracker.getExpensesList()); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { refreshTable(ExpenseTracker.getExpensesList()); }
        });
        body.add(ui.formRow("Search", searchField), BorderLayout.NORTH);
        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        filterRow.setOpaque(false);
        addFilterBtn(filterRow, "All", null);
        for (ExpenseCategory cat : ExpenseCategory.values()) addFilterBtn(filterRow, cat.getLabel(), cat);
        body.add(filterRow, BorderLayout.CENTER);
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        statusLabel.setForeground(ui.TEXT_MUTED);
        body.add(statusLabel, BorderLayout.SOUTH);
        outer.add(body, BorderLayout.CENTER);
        return outer;
    }

    private void addFilterBtn(JPanel row, String label, ExpenseCategory cat) {
        JToggleButton btn = new JToggleButton(label);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boolean sel = (cat == null && activeFilter == null) || cat == activeFilter;
        styleFilterBtn(btn, sel);
        btn.addActionListener(e -> {
            activeFilter = cat;
            for (Component c : row.getComponents())
                if (c instanceof JToggleButton tb) {
                    Object tc = tb.getClientProperty("cat");
                    boolean s = (cat == null && tc == null) || tc == cat;
                    tb.setSelected(s); styleFilterBtn(tb, s);
                }
            refreshTable(ExpenseTracker.getExpensesList());
        });
        btn.putClientProperty("cat", cat);
        btn.setSelected(sel);
        row.add(btn);
    }

    private void styleFilterBtn(JToggleButton btn, boolean selected) {
        if (selected) {
            btn.setBackground(ExpenseTrackerUI.SEARCH_HEADER_BAR); btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createCompoundBorder(new LineBorder(ExpenseTrackerUI.SEARCH_HEADER_BAR.darker(), 1, true), new EmptyBorder(3, 10, 3, 10)));
        } else {
            btn.setBackground(new Color(210, 228, 248)); btn.setForeground(new Color(30, 70, 130));
            btn.setBorder(BorderFactory.createCompoundBorder(new LineBorder(ExpenseTrackerUI.SEARCH_CARD_BORDER, 1, true), new EmptyBorder(3, 10, 3, 10)));
        }
    }

    private JPanel buildTablePanel() {
        String[] cols = {"#", "Description", "Category", "Date", "Amount (₱)"};
        tableModel = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        expenseTable = new JTable(tableModel);
        expenseTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        expenseTable.setRowHeight(38);
        expenseTable.setShowGrid(false);
        expenseTable.setGridColor(new Color(30, 41, 59));
        expenseTable.setIntercellSpacing(new Dimension(0, 3));
        expenseTable.setBackground(ExpenseTrackerUI.TABLE_ROW_BG);
        expenseTable.setForeground(ExpenseTrackerUI.TABLE_ROW_FG);
        expenseTable.setSelectionBackground(ExpenseTrackerUI.TABLE_SEL_BG);
        expenseTable.setSelectionForeground(ExpenseTrackerUI.TABLE_SEL_FG);
        expenseTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        expenseTable.getTableHeader().setForeground(Color.BLACK);
        expenseTable.getTableHeader().setBackground(ExpenseTrackerUI.TABLE_HEADER_BG);
        expenseTable.getTableHeader().setBorder(new MatteBorder(0, 0, 2, 0, ExpenseTrackerUI.TABLE_BORDER));
        expenseTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                l.setBackground(sel ? ExpenseTrackerUI.TABLE_SEL_BG : (row % 2 == 0 ? ExpenseTrackerUI.TABLE_ROW_BG : ExpenseTrackerUI.TABLE_ROW_ALT_BG));
                l.setForeground(sel ? ExpenseTrackerUI.TABLE_SEL_FG : ExpenseTrackerUI.TABLE_ROW_FG);
                l.setBorder(new EmptyBorder(0, 6, 0, 6));
                return l;
            }
        });
        expenseTable.getColumnModel().getColumn(0).setMaxWidth(36);
        expenseTable.getColumnModel().getColumn(2).setPreferredWidth(110);
        expenseTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        expenseTable.getColumnModel().getColumn(4).setPreferredWidth(100);

        DefaultTableCellRenderer amtR = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                l.setHorizontalAlignment(SwingConstants.RIGHT);
                l.setForeground(sel ? ExpenseTrackerUI.TABLE_SEL_FG : ExpenseTrackerUI.TABLE_AMT_FG);
                l.setBackground(sel ? ExpenseTrackerUI.TABLE_SEL_BG : (row % 2 == 0 ? ExpenseTrackerUI.TABLE_ROW_BG : ExpenseTrackerUI.TABLE_ROW_ALT_BG));
                l.setBorder(new EmptyBorder(0, 6, 0, 10));
                return l;
            }
        };
        expenseTable.getColumnModel().getColumn(4).setCellRenderer(amtR);

        JScrollPane scroll = new JScrollPane(expenseTable);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBackground(ExpenseTrackerUI.TABLE_PANEL_BG);
        scroll.getViewport().setBackground(ExpenseTrackerUI.TABLE_ROW_BG);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(ExpenseTrackerUI.TABLE_PANEL_BG);
        wrapper.setBorder(new LineBorder(ExpenseTrackerUI.TABLE_BORDER, 1, true));
        JPanel tableStrip = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 8));
        tableStrip.setBackground(ExpenseTrackerUI.TABLE_HEADER_BG);
        JLabel tableTitle = new JLabel("📋 Expense Records");
        tableTitle.setFont(new Font("SansSerif", Font.BOLD, 11));
        tableTitle.setForeground(ExpenseTrackerUI.TABLE_HEADER_F);
        tableStrip.add(tableTitle);
        wrapper.add(tableStrip, BorderLayout.NORTH);
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    void refresh(List<Expense> all, double total, double budget) {
        totalLabel.setText(ui.fmt(total));
        countLabel.setText(String.valueOf(all.size()));
        if (budget > 0) {
            budgetLabel.setText(ui.fmt(budget));
            double rem = budget - total;
            remainingLabel.setText((rem < 0 ? "−" : "") + ui.fmt(Math.abs(rem)));
            remainingLabel.setForeground(rem < 0 ? ExpenseTrackerUI.ACCENT_RED : ExpenseTrackerUI.ACCENT_GREEN);
            statusLabel.setText(rem < 0 ? "⚠ Over budget by " + ui.fmt(-rem) : "✓ Within budget — " + ui.fmt(rem) + " remaining");
            statusLabel.setForeground(rem < 0 ? ExpenseTrackerUI.ACCENT_RED : ExpenseTrackerUI.ACCENT_GREEN);
        } else {
            budgetLabel.setText("—"); remainingLabel.setText("—");
            remainingLabel.setForeground(ExpenseTrackerUI.CARD_REMAIN_FG); statusLabel.setText(" ");
        }
        donutChart.setData(all); donutChart.repaint();
        refreshTable(all);
    }

    private void refreshTable(List<Expense> all) {
        tableModel.setRowCount(0);
        String search = searchField == null ? "" : searchField.getText().trim().toLowerCase();
        List<Expense> sorted = new ArrayList<>(all);
        sorted.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        int idx = 1;
        for (Expense e : sorted) {
            if (activeFilter != null && e.getCategory() != activeFilter) continue;
            if (!search.isEmpty() && !e.getDescription().toLowerCase().contains(search) && !e.getDate().contains(search)) continue;
            tableModel.addRow(new Object[]{ idx++, e.getDescription(), e.getCategory().getLabel(), e.getDate(), String.format("%.2f", e.getAmount()), all.indexOf(e) });
        }
    }
}


// ═════════════════════════════════════════════════════════════════════════════
//  EXPENSES PANEL
// ═════════════════════════════════════════════════════════════════════════════
class ExpensesPanel extends JPanel {
    private final ExpenseTrackerUI ui;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField descField, amtField, dateField;
    private JComboBox<ExpenseCategory> catCombo;
    private JButton addBtn;
    private int editingIndex = -1;

    ExpensesPanel(ExpenseTrackerUI ui) {
        this.ui = ui;
        setBackground(ExpenseTrackerUI.PAGE_BG);
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(20, 22, 20, 22));
        buildUI();
    }

    private void buildUI() {
        add(ui.pageHeader("💸 Expenses", "Add, edit, and manage all your expense entries."), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildForm(), buildTable());
        split.setOpaque(false); split.setBackground(ExpenseTrackerUI.PAGE_BG);
        split.setContinuousLayout(true); split.setResizeWeight(0.32);
        split.setDividerSize(8); split.setOneTouchExpandable(true);
        split.setBorder(BorderFactory.createEmptyBorder());
        add(split, BorderLayout.CENTER);
    }

    private JPanel buildForm() {
        JPanel card = ui.colorCard("✦ Add / Edit Expense", ExpenseTrackerUI.ADD_CARD_B, ExpenseTrackerUI.ADD_CARD_BORDER, ExpenseTrackerUI.ADD_HEADER_BAR, Color.WHITE);
        descField = ui.inputField("e.g. Grocery run", ExpenseTrackerUI.ADD_CARD_BG);
        amtField  = ui.inputField("0.00",             ExpenseTrackerUI.ADD_CARD_BG);
        dateField = ui.inputField(java.time.LocalDate.now().toString(), ExpenseTrackerUI.ADD_CARD_BG);
        catCombo  = new JComboBox<>(ExpenseCategory.values());
        ui.styleCombo(catCombo, ExpenseTrackerUI.ADD_CARD_BG);
        card.add(ui.formRow("Description",       descField)); card.add(Box.createVerticalStrut(6));
        card.add(ui.formRow("Amount (₱)",        amtField));  card.add(Box.createVerticalStrut(6));
        card.add(ui.formRow("Date (YYYY-MM-DD)", dateField)); card.add(Box.createVerticalStrut(6));
        card.add(ui.formRow("Category",          catCombo));  card.add(Box.createVerticalStrut(10));
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        btnRow.setOpaque(false);
        addBtn = ui.styledBtn("✓ Add Expense", ExpenseTrackerUI.ADD_HEADER_BAR, Color.BLACK);
        JButton clearBtn = ui.styledBtn("Clear", new Color(220, 228, 245), ui.TEXT_PRIMARY);
        addBtn.addActionListener(e -> submit());
        clearBtn.addActionListener(e -> clear());
        btnRow.add(addBtn); btnRow.add(clearBtn);
        card.add(btnRow);
        return card;
    }

    private JPanel buildTable() {
        String[] cols = {"#", "Description", "Category", "Date", "Amount (₱)", "Actions"};
        tableModel = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return c == 5; } };
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(38); table.setShowGrid(false);
        table.setGridColor(new Color(30, 41, 59));
        table.setIntercellSpacing(new Dimension(0, 3));
        table.setBackground(ExpenseTrackerUI.TABLE_ROW_BG);
        table.setForeground(ExpenseTrackerUI.TABLE_ROW_FG);
        table.setSelectionBackground(ExpenseTrackerUI.TABLE_SEL_BG);
        table.setSelectionForeground(ExpenseTrackerUI.TABLE_SEL_FG);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setBackground(ExpenseTrackerUI.TABLE_HEADER_BG);
        table.getTableHeader().setBorder(new MatteBorder(0, 0, 2, 0, ExpenseTrackerUI.TABLE_BORDER));
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                l.setBackground(sel ? ExpenseTrackerUI.TABLE_SEL_BG : (row%2==0 ? ExpenseTrackerUI.TABLE_ROW_BG : ExpenseTrackerUI.TABLE_ROW_ALT_BG));
                l.setForeground(sel ? ExpenseTrackerUI.TABLE_SEL_FG : ExpenseTrackerUI.TABLE_ROW_FG);
                l.setBorder(new EmptyBorder(0,6,0,6)); return l;
            }
        });
        table.getColumnModel().getColumn(0).setMaxWidth(36);
        table.getColumnModel().getColumn(5).setMinWidth(160);
        table.getColumnModel().getColumn(5).setMaxWidth(160);

        table.getColumnModel().getColumn(5).setCellRenderer(
                new TableActionRenderer(ui)
        );

        table.getColumnModel().getColumn(5).setCellEditor(
                new TableActionEditor(ui, tableModel, null) {

                    protected void onEdit(int idx) {
                        loadForEdit(idx);
                    }

                    protected void onDelete(int idx) {

                        int c = JOptionPane.showConfirmDialog(
                                ui,
                                "Remove this expense?",
                                "Confirm",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.PLAIN_MESSAGE
                        );

                        if (c == JOptionPane.YES_OPTION) {
                            ExpenseTracker.removeExpense(idx);
                            ui.refreshAll();
                            ui.toast.show(
                                    "Removed",
                                    ExpenseTrackerUI.ACCENT_AMBER
                            );
                        }
                    }
                }
        );
        DefaultTableCellRenderer amtR = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                l.setHorizontalAlignment(SwingConstants.RIGHT);
                l.setForeground(sel ? ExpenseTrackerUI.TABLE_SEL_FG : ExpenseTrackerUI.TABLE_AMT_FG);
                l.setBackground(sel ? ExpenseTrackerUI.TABLE_SEL_BG : (row%2==0 ? ExpenseTrackerUI.TABLE_ROW_BG : ExpenseTrackerUI.TABLE_ROW_ALT_BG));
                l.setBorder(new EmptyBorder(0,6,0,10)); return l;
            }
        };
        table.getColumnModel().getColumn(4).setCellRenderer(amtR);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(ExpenseTrackerUI.TABLE_ROW_BG);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(ExpenseTrackerUI.TABLE_PANEL_BG);
        wrapper.setBorder(new LineBorder(ExpenseTrackerUI.TABLE_BORDER, 1, true));
        JPanel strip = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 8));
        strip.setBackground(ExpenseTrackerUI.TABLE_HEADER_BG);
        JLabel t2 = new JLabel("📋 All Expenses"); t2.setFont(new Font("SansSerif", Font.BOLD, 11)); t2.setForeground(ExpenseTrackerUI.TABLE_HEADER_F);
        strip.add(t2); wrapper.add(strip, BorderLayout.NORTH); wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    private void submit() {
        String desc = descField.getText().trim(), amtStr = amtField.getText().trim(), date = dateField.getText().trim();
        ExpenseCategory cat = (ExpenseCategory) catCombo.getSelectedItem();
        if (desc.isEmpty()) { ui.toast.show("Please enter a description", ExpenseTrackerUI.ACCENT_RED); return; }
        double amt;
        try { amt = Double.parseDouble(amtStr); } catch (NumberFormatException ex) { ui.toast.show("Enter a valid amount", ExpenseTrackerUI.ACCENT_RED); return; }
        if (amt < 0) { ui.toast.show("Amount must be positive", ExpenseTrackerUI.ACCENT_RED); return; }
        if (date.isEmpty() || !date.matches("\\d{4}-\\d{2}-\\d{2}")) { ui.toast.show("Use YYYY-MM-DD", ExpenseTrackerUI.ACCENT_RED); return; }
        if (editingIndex >= 0) { ExpenseTracker.updateExpense(editingIndex, desc, amt, date, cat); ui.toast.show("Updated ✓", ExpenseTrackerUI.ACCENT_GREEN); }
        else { ExpenseTracker.addExpense(desc, amt, date, cat); ui.toast.show("Added ✓", ExpenseTrackerUI.ACCENT_GREEN); }
        clear(); ui.refreshAll();
    }

    void loadForEdit(int idx) {
        List<Expense> all = ExpenseTracker.getExpensesList();
        if (idx < 0 || idx >= all.size()) return;
        Expense e = all.get(idx);
        descField.setText(e.getDescription()); amtField.setText(String.valueOf(e.getAmount()));
        dateField.setText(e.getDate()); catCombo.setSelectedItem(e.getCategory());
        editingIndex = idx; addBtn.setText("✓ Save Changes");
        ui.toast.show("Editing — make changes and save", ExpenseTrackerUI.ACCENT_AMBER);
    }

    private void clear() {
        editingIndex = -1; addBtn.setText("✓ Add Expense");
        descField.setText(""); amtField.setText("");
        dateField.setText(java.time.LocalDate.now().toString()); catCombo.setSelectedIndex(0);
    }

    void refresh(List<Expense> all) {
        tableModel.setRowCount(0);
        List<Expense> sorted = new ArrayList<>(all);
        sorted.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        int i = 1;
        for (Expense e : sorted)
            tableModel.addRow(new Object[]{ i++, e.getDescription(), e.getCategory().getLabel(), e.getDate(), String.format("%.2f", e.getAmount()), all.indexOf(e) });
    }
}


// ═════════════════════════════════════════════════════════════════════════════
//  CATEGORIES PANEL
// ═════════════════════════════════════════════════════════════════════════════
class CategoriesPanel extends JPanel {
    private final ExpenseTrackerUI ui;
    private final JLabel[] totalsLabels = new JLabel[ExpenseCategory.values().length];
    private final JLabel[] pctLabels    = new JLabel[ExpenseCategory.values().length];
    private DonutChartPanel bigDonut;

    CategoriesPanel(ExpenseTrackerUI ui) {
        this.ui = ui;
        setBackground(ExpenseTrackerUI.PAGE_BG);
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(20, 22, 20, 22));
        buildUI();
    }

    private void buildUI() {
        add(ui.pageHeader("📂 Categories", "See how your spending is distributed by category."), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(16, 0));
        center.setOpaque(false);

        // Large (strawberry) dunkin donut chart
        JPanel donutCard = ui.colorCard("🍩 Spending by Category", ExpenseTrackerUI.DONUT_CARD_BG, ExpenseTrackerUI.DONUT_CARD_BORDER, ExpenseTrackerUI.DONUT_HEADER_BAR, Color.WHITE);
        bigDonut = new DonutChartPanel(ui);
        bigDonut.setPreferredSize(new Dimension(300, 380));
        donutCard.add(bigDonut);
        center.add(donutCard, BorderLayout.WEST);

        // Category break it down yow
        JPanel tableCard = ui.colorCard("📊 Category Breakdown", ExpenseTrackerUI.SEARCH_CARD_BG, ExpenseTrackerUI.SEARCH_CARD_BG, ExpenseTrackerUI.SEARCH_CARD_B, Color.WHITE);
        JPanel grid = new JPanel(new GridLayout(ExpenseCategory.values().length + 1, 3, 8, 6));
        grid.setOpaque(false);

        // Header row
        for (String h : new String[]{"Category", "Total Spent", "% of Total"}) {
            JLabel lh = new JLabel(h); lh.setFont(new Font("SansSerif", Font.BOLD, 12)); lh.setForeground(Color.WHITE); grid.add(lh);
        }
        ExpenseCategory[] cats = ExpenseCategory.values();
        for (int i = 0; i < cats.length; i++) {
            JLabel name = new JLabel(cats[i].getLabel()); name.setFont(new Font("SansSerif", Font.BOLD, 13)); name.setForeground(ExpenseTrackerUI.CAT_COLORS[i]);
            totalsLabels[i] = new JLabel("₱0.00"); totalsLabels[i].setFont(new Font("SansSerif", Font.PLAIN, 13)); totalsLabels[i].setForeground(Color.WHITE);
            pctLabels[i]    = new JLabel("0%");    pctLabels[i].setFont(new Font("SansSerif", Font.PLAIN, 13));    pctLabels[i].setForeground(Color.WHITE);
            grid.add(name); grid.add(totalsLabels[i]); grid.add(pctLabels[i]);
        }
        tableCard.add(grid);
        center.add(tableCard, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    void refresh(List<Expense> all) {
        bigDonut.setData(all); bigDonut.repaint();
        double total = all.stream().mapToDouble(Expense::getAmount).sum();
        ExpenseCategory[] cats = ExpenseCategory.values();
        for (int i = 0; i < cats.length; i++) {
            final ExpenseCategory currentCat = cats[i];

            double t = all.stream()
                    .filter(e -> e.getCategory() == currentCat)
                    .mapToDouble(Expense::getAmount)
                    .sum();

            totalsLabels[i].setText(String.format("₱%.2f", t));

            pctLabels[i].setText(
                    total > 0
                            ? String.format("%.1f%%", t / total * 100)
                            : "0%"
            );
        }
    }
}

// ═════════════════════════════════════════════════════════════════════════════
//  BUDGET PANEL
// ═════════════════════════════════════════════════════════════════════════════
class BudgetPanel extends JPanel {
    private final ExpenseTrackerUI ui;
    private JTextField budgetField;
    private JPanel budgetBarPanel;
    private JLabel spentLabel, budgetAmtLabel, remainingLabel, pctLabel;

    BudgetPanel(ExpenseTrackerUI ui) {
        this.ui = ui;
        setBackground(ExpenseTrackerUI.PAGE_BG);
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(20, 22, 20, 22));
        buildUI();
    }

    private void buildUI() {
        add(ui.pageHeader("🎯 Budget", "Set and track your monthly budget."), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 16, 0));
        center.setOpaque(false);


        JPanel formCard = ui.colorCard("🎯 Set Monthly Budget", ExpenseTrackerUI.BUDGET_CARD_B, ExpenseTrackerUI.BUDGET_CARD_BORDER, ExpenseTrackerUI.BUDGET_HEADER_BAR, Color.WHITE);
        budgetField = ui.inputField("e.g. 5000.00", ExpenseTrackerUI.BUDGET_CARD_BG);
        formCard.add(ui.formRow("Monthly Budget (₱)", budgetField));
        formCard.add(Box.createVerticalStrut(10));
        JButton setBtn = ui.styledBtn("Set Budget", ExpenseTrackerUI.BUDGET_HEADER_BAR, Color.BLACK);
        setBtn.addActionListener(e -> setBudget());
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnRow.setOpaque(false); btnRow.add(setBtn);
        formCard.add(btnRow);
        center.add(formCard);


        JPanel statusCard = ui.colorCard("📊 Budget Status", ExpenseTrackerUI.BUDGET_CARD_BG, ExpenseTrackerUI.BUDGET_CARD_BORDER, ExpenseTrackerUI.BUDGET_HEADER_BAR, Color.WHITE);
        spentLabel     = styledStat("Total Spent",  "₱0.00", ExpenseTrackerUI.ACCENT_RED);
        budgetAmtLabel = styledStat("Budget Set",   "—",     ExpenseTrackerUI.ACCENT_GREEN);
        remainingLabel = styledStat("Remaining",    "—",     ExpenseTrackerUI.ACCENT_BLUE);
        pctLabel       = styledStat("Used",         "0%",    ExpenseTrackerUI.BUDGET_HEADER_BAR);
        budgetBarPanel = new JPanel();
        budgetBarPanel.setLayout(new BoxLayout(budgetBarPanel, BoxLayout.Y_AXIS));
        budgetBarPanel.setOpaque(false);
        statusCard.add(spentLabel); statusCard.add(Box.createVerticalStrut(10));
        statusCard.add(budgetAmtLabel); statusCard.add(Box.createVerticalStrut(10));
        statusCard.add(remainingLabel); statusCard.add(Box.createVerticalStrut(10));
        statusCard.add(pctLabel); statusCard.add(Box.createVerticalStrut(12));
        statusCard.add(budgetBarPanel);
        center.add(statusCard);

        add(center, BorderLayout.CENTER);
    }

    private JLabel styledStat(String label, String val, Color color) {
        JLabel l = new JLabel("<html><span style='font-size:11px;color:#6b7280'>" + label + "</span><br><b style='font-size:20px;color:" + colorHex(color) + "'>" + val + "</b></html>");
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private String colorHex(Color c) { return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue()); }

    private void setBudget() {
        try {
            double b = Double.parseDouble(budgetField.getText().trim());
            if (b < 0) throw new NumberFormatException();
            ExpenseTracker.setBudget(b);
            ui.toast.show("Budget set to ₱" + String.format("%.2f", b), ExpenseTrackerUI.ACCENT_GREEN);
            ui.refreshAll();
        } catch (NumberFormatException ex) { ui.toast.show("Enter a valid budget amount", ExpenseTrackerUI.ACCENT_RED); }
    }

    void refresh(double total, double budget) {
        spentLabel.setText(statHtml("Total Spent", ui.fmt(total), ExpenseTrackerUI.ACCENT_RED));
        if (budget > 0) {
            double rem = budget - total;
            double pct = Math.min(total / budget, 1.0) * 100;
            budgetAmtLabel.setText(statHtml("Budget Set",  ui.fmt(budget),                           ExpenseTrackerUI.ACCENT_GREEN));
            remainingLabel.setText(statHtml("Remaining",   (rem < 0 ? "−" : "") + ui.fmt(Math.abs(rem)), rem < 0 ? ExpenseTrackerUI.ACCENT_RED : ExpenseTrackerUI.ACCENT_BLUE));
            pctLabel.setText(statHtml("Used",              String.format("%.1f%%", pct),              ExpenseTrackerUI.BUDGET_HEADER_BAR));
            refreshBar(total, budget);
        } else {
            budgetAmtLabel.setText(statHtml("Budget Set",  "—", ExpenseTrackerUI.ACCENT_GREEN));
            remainingLabel.setText(statHtml("Remaining",   "—", ExpenseTrackerUI.ACCENT_BLUE));
            pctLabel.setText(statHtml("Used",              "0%", ExpenseTrackerUI.BUDGET_HEADER_BAR));
            budgetBarPanel.setVisible(false);
        }
    }

    private String statHtml(String label, String val, Color c) {
        return "<html><span style='font-size:11px;color:#6b7280'>" + label + "</span><br><b style='font-size:20px;color:" + colorHex(c) + "'>" + val + "</b></html>";
    }

    private void refreshBar(double total, double budget) {
        budgetBarPanel.removeAll();
        budgetBarPanel.setVisible(true);
        double pct = Math.min(total / budget, 1.0);
        Color fill = pct >= 1.0 ? ExpenseTrackerUI.ACCENT_RED : pct >= 0.8 ? ExpenseTrackerUI.ACCENT_AMBER : ExpenseTrackerUI.ACCENT_GREEN;
        JPanel track = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(190, 220, 200));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                int fw = (int)(getWidth() * pct);
                if (fw > 0) { g2.setColor(fill); g2.fill(new RoundRectangle2D.Float(0, 0, fw, getHeight(), 10, 10)); }
            }
        };
        track.setPreferredSize(new Dimension(300, 14));
        track.setMaximumSize(new Dimension(Integer.MAX_VALUE, 14));
        track.setOpaque(false);
        JLabel barLbl = new JLabel(String.format("%.0f%% of ₱%.2f used", pct * 100, budget));
        barLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        barLbl.setForeground(ExpenseTrackerUI.BUDGET_HEADER_BAR);
        barLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        track.setAlignmentX(Component.LEFT_ALIGNMENT);
        budgetBarPanel.add(track); budgetBarPanel.add(Box.createVerticalStrut(6)); budgetBarPanel.add(barLbl);
        budgetBarPanel.revalidate(); budgetBarPanel.repaint();
    }
}


// ═════════════════════════════════════════════════════════════════════════════
//  WEEKLY BREAKDOWN PANEL
// ═════════════════════════════════════════════════════════════════════════════
class WeeklyPanel extends JPanel {
    private final ExpenseTrackerUI ui;
    private JPanel barsPanel;

    WeeklyPanel(ExpenseTrackerUI ui) {
        this.ui = ui;
        setBackground(ExpenseTrackerUI.PAGE_BG);
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(20, 22, 20, 22));
        buildUI();
    }

    private void buildUI() {
        add(ui.pageHeader("📅 Weekly Breakdown", "View your spending week by week."), BorderLayout.NORTH);
        JPanel card = ui.colorCard("📅 Weekly Spending", ExpenseTrackerUI.WEEKLY_CARD_B, ExpenseTrackerUI.WEEKLY_CARD_BORDER, ExpenseTrackerUI.WEEKLY_HEADER_BAR, Color.WHITE);
        barsPanel = new JPanel();
        barsPanel.setLayout(new BoxLayout(barsPanel, BoxLayout.Y_AXIS));
        barsPanel.setOpaque(false);
        card.add(barsPanel);
        JScrollPane scroll = new JScrollPane(card);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false); scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);
    }

    void refresh() {
        barsPanel.removeAll();
        Map<Integer, Double> weeks = ExpenseTracker.getWeeklyTotals();
        if (weeks.isEmpty()) {
            JLabel none = new JLabel("No expense data yet.");
            none.setFont(new Font("SansSerif", Font.ITALIC, 14)); none.setForeground(new Color(130, 100, 190));
            barsPanel.add(none); barsPanel.revalidate(); return;
        }
        double max = weeks.values().stream().mapToDouble(Double::doubleValue).max().orElse(1);
        for (Map.Entry<Integer, Double> entry : weeks.entrySet()) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setOpaque(false); row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

            JLabel wk = new JLabel("Week " + entry.getKey());
            wk.setFont(new Font("SansSerif", Font.BOLD, 13)); wk.setForeground(ExpenseTrackerUI.WEEKLY_HEADER_BAR);
            wk.setPreferredSize(new Dimension(70, 22));

            double pct = entry.getValue() / max;
            JPanel track = new JPanel(null) {
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(210, 195, 240));
                    g2.fill(new RoundRectangle2D.Float(0, 6, getWidth(), 10, 6, 6));
                    g2.setColor(ExpenseTrackerUI.WEEKLY_HEADER_BAR);
                    g2.fill(new RoundRectangle2D.Float(0, 6, (int)(getWidth() * pct), 10, 6, 6));
                }
            };
            track.setOpaque(false);

            JLabel amt = new JLabel("₱" + String.format("%.2f", entry.getValue()));
            amt.setFont(new Font("SansSerif", Font.BOLD, 13)); amt.setForeground(ui.TEXT_PRIMARY);
            amt.setPreferredSize(new Dimension(90, 22)); amt.setHorizontalAlignment(SwingConstants.RIGHT);

            row.add(wk, BorderLayout.WEST); row.add(track, BorderLayout.CENTER); row.add(amt, BorderLayout.EAST);
            barsPanel.add(row); barsPanel.add(Box.createVerticalStrut(10));
        }
        barsPanel.revalidate(); barsPanel.repaint();
    }
}


// ═════════════════════════════════════════════════════════════════════════════
//  SHARED: YOU DONUT CHART PANEL
// ═════════════════════════════════════════════════════════════════════════════
class DonutChartPanel extends JPanel {
    private final ExpenseTrackerUI ui;
    private List<Expense> expenses = new ArrayList<>();
    DonutChartPanel(ExpenseTrackerUI ui) { this.ui = ui; setOpaque(false); }
    void setData(List<Expense> data) { this.expenses = data; }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ExpenseCategory[] cats = ExpenseCategory.values();

        // Count only categories that have data (for legend sizinggggggg)
        Map<ExpenseCategory, Double> totals = new LinkedHashMap<>();
        for (ExpenseCategory cat : cats) totals.put(cat, 0.0);
        for (Expense e : expenses) totals.merge(e.getCategory(), e.getAmount(), Double::sum);
        long activeCats = java.util.Arrays.stream(cats).filter(c -> totals.get(c) >= 0.01).count();

        int legendRowH  = 16;
        int legendGap   = 8;
        int legendHeight = (int)(activeCats * legendRowH + legendGap);

        int ringAreaH = getHeight() - legendHeight;
        int padding   = 20;
        int sz = Math.min(getWidth() - padding * 2, ringAreaH - padding * 2);
        if (sz < 10) return;  // too small to draw

        int ringX = (getWidth() - sz) / 2;
        int ringY = (ringAreaH - sz) / 2;

        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();

        if (total == 0) {
            g2.setColor(new Color(230, 200, 225));
            g2.setStroke(new BasicStroke(20, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawOval(ringX + 10, ringY + 10, sz - 20, sz - 20);
            return;
        }


        int strokeW = Math.max(14, sz / 8);
        g2.setStroke(new BasicStroke(strokeW, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
        double start = -90;
        int pad = strokeW / 2 + 2;
        for (int i = 0; i < cats.length; i++) {
            double pct = totals.get(cats[i]) / total;
            if (pct < 0.001) continue;
            double sweep = pct * 360;
            g2.setColor(ExpenseTrackerUI.CAT_COLORS[i]);
            g2.drawArc(ringX + pad, ringY + pad, sz - pad * 2, sz - pad * 2, (int) start, (int) Math.ceil(sweep));
            start += sweep;
        }


        int cx = ringX + sz / 2;
        int cy = ringY + sz / 2;

        g2.setColor(ExpenseTrackerUI.DONUT_HEADER_BAR);
        g2.setFont(new Font("SansSerif", Font.BOLD, Math.max(11, sz / 10)));
        String totalStr = String.format("₱%.0f", total);
        FontMetrics fmBold = g2.getFontMetrics();
        g2.drawString(totalStr, cx - fmBold.stringWidth(totalStr) / 2, cy + fmBold.getAscent() / 2 - 4);

        g2.setFont(new Font("SansSerif", Font.PLAIN, Math.max(9, sz / 14)));
        g2.setColor(new Color(160, 80, 130));
        FontMetrics fmSmall = g2.getFontMetrics();
        g2.drawString("total", cx - fmSmall.stringWidth("total") / 2, cy + fmBold.getAscent() / 2 + fmSmall.getHeight() - 4);


        int lx = 10;
        int ly = ringAreaH + legendGap;
        for (int i = 0; i < cats.length; i++) {
            if (totals.get(cats[i]) < 0.01) continue;
            g2.setColor(ExpenseTrackerUI.CAT_COLORS[i]);
            g2.fillRoundRect(lx, ly + 3, 10, 10, 3, 3);
            g2.setColor(new Color(80, 40, 70));
            g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g2.drawString(cats[i].getLabel(), lx + 14, ly + 12);
            ly += legendRowH;
        }
    }
}


// ═════════════════════════════════════════════════════════════════════════════
//  SHARED: TABLE ACTION RENDERER / EDITOR
// ═════════════════════════════════════════════════════════════════════════════
class TableActionRenderer implements TableCellRenderer {
    private final JPanel  p    = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 3));
    private final JButton edit;
    private final JButton del;
    TableActionRenderer(ExpenseTrackerUI ui) {
        edit = ui.darkSmallBtn("✎ Edit",   new Color(100, 160, 255));
        del  = ui.darkSmallBtn("✕ Delete", new Color(255, 100, 100));
        p.setOpaque(true); p.add(edit); p.add(del);
    }
    public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
        p.setBackground(sel ? ExpenseTrackerUI.TABLE_SEL_BG : (r%2==0 ? ExpenseTrackerUI.TABLE_ROW_BG : ExpenseTrackerUI.TABLE_ROW_ALT_BG));
        return p;
    }
}

class TableActionEditor extends AbstractCellEditor implements TableCellEditor {
    protected final JPanel  p    = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 3));
    protected final JButton edit;
    protected final JButton del;
    protected int realIndex = -1;
    private   final DefaultTableModel tableModel;

    TableActionEditor(ExpenseTrackerUI ui, DefaultTableModel tableModel, DashboardPanel dashPanel) {
        this.tableModel = tableModel;
        edit = ui.darkSmallBtn("✎ Edit",   new Color(100, 160, 255));
        del  = ui.darkSmallBtn("✕ Delete", new Color(255, 100, 100));
        p.setOpaque(true); p.setBackground(ExpenseTrackerUI.TABLE_ROW_BG);
        edit.addActionListener(e -> { int idx = realIndex; stopCellEditing(); onEdit(idx); });
        del.addActionListener(e  -> { int idx = realIndex; stopCellEditing(); onDelete(idx); });
        p.add(edit); p.add(del);
    }
    protected void onEdit(int idx)   { /* overridden */ }
    protected void onDelete(int idx) { /* overridden */ }
    public Component getTableCellEditorComponent(JTable t, Object v, boolean sel, int row, int col) {
        Object raw = tableModel.getValueAt(row, 5);
        realIndex = (raw instanceof Integer) ? (Integer) raw : -1;
        return p;
    }
    public Object getCellEditorValue() { return null; }
    public boolean isCellEditable(java.util.EventObject e) { return true; }
    public boolean shouldSelectCell(java.util.EventObject e) { return true; }
}