import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class LogoutUI extends JFrame {


    private static final Color DARK_BG      = new Color(7, 18, 48);
    private static final Color CARD_BG      = new Color(245, 247, 250);
    private static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    private static final Color TEXT_MUTED   = new Color(100, 116, 139);

    private static final Color ACCENT_BLUE  = new Color(59, 130, 246);
    private static final Color ACCENT_RED   = new Color(239, 68, 68);

    public LogoutUI(String username) {

        super("CentSible — Logged Out");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 420);
        setMinimumSize(new Dimension(500, 420));
        setLocationRelativeTo(null);
        setResizable(false);


        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(DARK_BG);
        root.setBorder(new EmptyBorder(25, 25, 25, 25));
        add(root);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;


        JLabel logo = new JLabel("💰 CentSible", SwingConstants.CENTER);
        logo.setFont(new Font("SansSerif", Font.BOLD, 40));
        logo.setForeground(Color.WHITE);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        root.add(logo, gbc);


        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(40, 40, 40, 40)
        ));

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        root.add(card, gbc);

        JLabel icon = new JLabel("👋", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 42));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(icon);
        card.add(Box.createVerticalStrut(15));

        String name = (username != null && !username.isBlank())
                ? username
                : "User";

        JLabel heading = new JLabel(
                "Goodbye, " + name + "!",
                SwingConstants.CENTER
        );

        heading.setFont(new Font("SansSerif", Font.BOLD, 24));
        heading.setForeground(TEXT_PRIMARY);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(heading);
        card.add(Box.createVerticalStrut(8));

        JLabel sub = new JLabel(
                "You have been successfully logged out.",
                SwingConstants.CENTER
        );

        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(TEXT_MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(sub);
        card.add(Box.createVerticalStrut(28));

        JPanel btnRow = new JPanel(new FlowLayout(
                FlowLayout.CENTER,
                12,
                0
        ));

        btnRow.setOpaque(false);

        JButton loginAgainBtn = styledButton(
                "Log back in",
                ACCENT_BLUE
        );

        loginAgainBtn.addActionListener(e -> {
            dispose();

            SwingUtilities.invokeLater(() ->
                    new LoginUI().setVisible(true)
            );
        });

        JButton exitBtn = styledButton(
                "Exit",
                ACCENT_RED
        );

        exitBtn.addActionListener(e -> System.exit(0));

        btnRow.add(loginAgainBtn);
        btnRow.add(exitBtn);

        card.add(btnRow);
    }


    private JButton styledButton(String text, Color bg) {

        JButton btn = new JButton(text);

        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(Color.BLACK);
        btn.setBackground(bg);

        btn.setFocusPainted(false);
        btn.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.darker()),
                new EmptyBorder(12, 28, 12, 28)
        ));

        btn.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }
}
