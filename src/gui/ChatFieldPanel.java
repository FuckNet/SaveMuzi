package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.ScrollPane;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import main.SMFrame;

public class ChatFieldPanel extends JPanel {
	Image backgroundChatField;

	public ChatFieldPanel(JScrollPane scrollPane, String imgStr) {
		backgroundChatField = Toolkit.getDefaultToolkit().getImage(imgStr);
		setLayout(null);
		setBorder(new BevelBorder(BevelBorder.LOWERED, Color.LIGHT_GRAY, Color.LIGHT_GRAY));
		setBounds(scrollPane.getX(), scrollPane.getY() + scrollPane.getHeight(), SMFrame.SCREEN_WIDTH * 3 / 8,
				SMFrame.SCREEN_HEIGHT * 1 / 10);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundChatField, 0, 0, getWidth(), getHeight(), this);
	}
}