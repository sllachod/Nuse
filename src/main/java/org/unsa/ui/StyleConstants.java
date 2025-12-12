package org.unsa.ui;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class StyleConstants {
    public static final Color PRIMARY_RED = new Color(220, 20, 60);
    public static final Color DARK_RED = new Color(139, 0, 0);
    public static final Color BLACK = new Color(18, 18, 18);
    public static final Color DARK_GRAY = new Color(30, 30, 30);
    public static final Color LIGHT_GRAY = new Color(50, 50, 50);
    public static final Color TEXT_COLOR = new Color(240, 240, 240);
    public static final Color HOVER_RED = new Color(255, 50, 90);
    
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    
    public static class RoundedPanel extends JPanel {
        private int radius;
        private Color backgroundColor;

        public RoundedPanel(int radius, Color backgroundColor) {
            this.radius = radius;
            this.backgroundColor = backgroundColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
    }

    public static class RoundedButton extends JButton {
        private Color normalColor;
        private Color hoverColor;
        private int radius;

        public RoundedButton(String text, Color normalColor, Color hoverColor) {
            super(text);
            this.normalColor = normalColor;
            this.hoverColor = hoverColor;
            this.radius = 20;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setFont(BUTTON_FONT);
            setForeground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (getModel().isPressed()) {
                g2.setColor(hoverColor.darker());
            } else if (getModel().isRollover()) {
                g2.setColor(hoverColor);
            } else {
                g2.setColor(normalColor);
            }
            
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }
    }

    public static JTextField createStyledTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setFont(NORMAL_FONT);
        field.setBackground(LIGHT_GRAY);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return field;
    }

    public static JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(NORMAL_FONT);
        comboBox.setBackground(LIGHT_GRAY);
        comboBox.setForeground(TEXT_COLOR);
        comboBox.setBorder(BorderFactory.createLineBorder(LIGHT_GRAY));
        
        // Custom renderer for the list items
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    setBackground(PRIMARY_RED);
                    setForeground(Color.WHITE);
                } else {
                    setBackground(DARK_GRAY);
                    setForeground(TEXT_COLOR);
                }
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        });
        
        return comboBox;
    }

    public static JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };
        field.setOpaque(false);
        field.setFont(NORMAL_FONT);
        field.setBackground(LIGHT_GRAY);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return field;
    }

    public static void configureTable(JTable table) {
        table.setBackground(DARK_GRAY);
        table.setForeground(TEXT_COLOR);
        table.setFont(NORMAL_FONT);
        table.setRowHeight(45);
        table.setSelectionBackground(PRIMARY_RED);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(LIGHT_GRAY);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(BLACK);
        header.setForeground(PRIMARY_RED);
        header.setFont(HEADER_FONT);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_RED));
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 50));
        
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? DARK_GRAY : new Color(45, 45, 45));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        };
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
}
