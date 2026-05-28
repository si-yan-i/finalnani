import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class LoginUI extends JFrame {

    // ─────────────────────────────────────────
    // COLORS
    // ─────────────────────────────────────────
    private final Color BG_PRIMARY;
    private final Color BG_SECONDARY;
    private final Color BG_CARD;
    private final Color BORDER_COLOR;
    private final Color TEXT_PRIMARY;
    private final Color TEXT_MUTED;

    private static final Color ACCENT_BLUE =
            new Color(47, 106, 229);

    private static final Color ACCENT_RED =
            new Color(220, 38, 38);

    // ─────────────────────────────────────────
    // COMPONENTS
    // ─────────────────────────────────────────
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;

    // ─────────────────────────────────────────
    // CONSTRUCTOR
    // ─────────────────────────────────────────
    public LoginUI() {

        super("CentSible — Login");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(460, 640));

        BG_PRIMARY   = new Color(15, 23, 42);
        BG_SECONDARY = new Color(241, 245, 249);
        BG_CARD      = new Color(255, 255, 255);
        BORDER_COLOR = new Color(203, 213, 225);
        TEXT_PRIMARY = new Color(15, 23, 42);
        TEXT_MUTED   = new Color(100, 116, 139);

        getContentPane().setBackground(BG_PRIMARY);

        // ROOT PANELlist
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(BG_PRIMARY);
        root.setBorder(new EmptyBorder(30, 30, 30, 30));

        add(root);

        // CENTER CARD
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        card.setBackground(BG_CARD);

        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(30, 41, 59), 1, true),
                new EmptyBorder(35, 35, 35, 35)
        ));

        card.setPreferredSize(new Dimension(360, 500));


        JLabel logo = new JLabel("💰 CentSible");
        logo.setFont(new Font("SansSerif", Font.BOLD, 34));
        logo.setForeground(TEXT_PRIMARY);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline = new JLabel("Track smarter. Spend wiser.");
        tagline.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tagline.setForeground(TEXT_MUTED);
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(logo);
        card.add(Box.createVerticalStrut(8));
        card.add(tagline);
        card.add(Box.createVerticalStrut(24));


        // USERNAME

        JLabel userLabel = fieldLabel("Username");
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = styledTextField("Enter your username");

        // PASSWORD

        JLabel passLabel = fieldLabel("Password");
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = styledPasswordField("Enter your password");


        JPanel form = new JPanel();
        form.setOpaque(false);

        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        form.add(userLabel);
        form.add(Box.createVerticalStrut(6));
        form.add(usernameField);

        form.add(Box.createVerticalStrut(18));

        form.add(passLabel);
        form.add(Box.createVerticalStrut(6));
        form.add(passwordField);

        form.add(Box.createVerticalStrut(18));


        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        errorLabel.setForeground(ACCENT_RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        form.add(errorLabel);
        form.add(Box.createVerticalStrut(10));


        JButton loginBtn = styledButton(
                "Login",
                ACCENT_BLUE,
                Color.BLACK
        );

        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginBtn.addActionListener(e -> handleLogin());

        form.add(loginBtn);

        form.add(Box.createVerticalStrut(14));


        JSeparator sep = divider();
        form.add(sep);


        form.add(Box.createVerticalStrut(16));

        JPanel signUpRow = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 4, 0)
        );

        signUpRow.setOpaque(false);

        JLabel noAccountLbl =
                new JLabel("Don't have an account?");

        noAccountLbl.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );

        noAccountLbl.setForeground(TEXT_MUTED);

        JButton signUpBtn = linkButton("Create one");

        signUpBtn.addActionListener(e -> openSignUp());

        signUpRow.add(noAccountLbl);
        signUpRow.add(signUpBtn);

        form.add(signUpRow);

        card.add(form);

        root.add(card);
        card.add(Box.createVerticalStrut(10));


        ActionListener enterAction = e -> handleLogin();

        usernameField.addActionListener(enterAction);
        passwordField.addActionListener(enterAction);

        pack();

        setLocationRelativeTo(null);
        setResizable(true);
    }


    // LOGIN

    private void handleLogin() {

        String username =
                usernameField.getText().trim();

        String password =
                new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {

            showError("Please fill in both fields.");
            return;
        }

        if (UserStore.authenticate(username, password)) {

            ExpenseTracker.setCurrentUser(username);

            dispose();

            SwingUtilities.invokeLater(() ->
                    new ExpenseTrackerUI(username)
                            .setVisible(true));

        } else {

            showError("Invalid username or password.");

            passwordField.setText("");
            passwordField.requestFocus();
        }
    }


    // OPEN SIGNUP

    private void openSignUp() {

        dispose();

        SwingUtilities.invokeLater(() ->
                new SignUpUI().setVisible(true));
    }


    // ERROR DISPLAY

    private void showError(String message) {

        errorLabel.setText(message);
    }


    // FIELD LABEL

    private JLabel fieldLabel(String text) {

        JLabel lbl = new JLabel(text);

        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(TEXT_MUTED);

        return lbl;
    }


    // TEXT FIELD

    private JTextField styledTextField(String placeholder) {

        JTextField f = new JTextField();

        f.setFont(new Font("SansSerif", Font.PLAIN, 13));

        f.setBackground(BG_SECONDARY);
        f.setForeground(TEXT_PRIMARY);

        f.setCaretColor(TEXT_PRIMARY);

        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));

        f.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 42)
        );

        f.setHorizontalAlignment(JTextField.CENTER);

        return f;
    }



    private JPasswordField styledPasswordField(
            String placeholder) {

        JPasswordField f = new JPasswordField();

        f.setFont(new Font("SansSerif", Font.PLAIN, 13));

        f.setBackground(BG_SECONDARY);
        f.setForeground(TEXT_PRIMARY);

        f.setCaretColor(TEXT_PRIMARY);

        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));

        f.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 42)
        );

        f.setHorizontalAlignment(JTextField.CENTER);

        return f;
    }


    private JButton styledButton(
            String text,
            Color bg,
            Color fg) {

        JButton btn = new JButton(text);

        btn.setFont(
                new Font("SansSerif", Font.BOLD, 14)
        );

        btn.setBackground(bg);
        btn.setForeground(fg);

        btn.setFocusPainted(false);

        btn.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(bg.darker(), 1, true),
                new EmptyBorder(11, 24, 11, 24)
        ));

        btn.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 45)
        );

        btn.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.darker());
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });

        return btn;
    }

    private JButton linkButton(String text) {

        JButton btn = new JButton(
                "<html><u>" + text + "</u></html>"
        );

        btn.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );

        btn.setForeground(ACCENT_BLUE);

        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);

        btn.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        return btn;
    }

    private JSeparator divider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_COLOR);
        sep.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 1)
        );
        return sep;
    }

    public void prefillUsername(String username) {
        usernameField.setText(username);
        passwordField.requestFocus();
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception ignored) {}
        UserStore.loadUsers();
        SwingUtilities.invokeLater(() ->
                new LoginUI().setVisible(true)
        );
    }
}