package home;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import network.SMNet;

public class SignUpPanel extends JPanel{
	private static final String BG_LOGIN = "res/background/backgroundLogin.png";
	private static final int WIDTH = 500;
	private static final int HEIGHT = 550;
	private Image backgroundLogin;

	private HomePanel homePanel;
	private SMNet smNet;
	
	JTextField tf_ID; // ID�� �Է¹�����
	JTextField tf_PW; // IP�� �Է¹�����
	JTextField tf_PWC; // IP�� �Է¹�����

	private JButton btnSignup; // �α��� ��ư
	private JButton btnCancel; // �α��� ��ư
		
	public SignUpPanel(SMNet smNet, HomePanel homePanel) {
		this.homePanel = homePanel;
		this.smNet = smNet;
		backgroundLogin = Toolkit.getDefaultToolkit().getImage(BG_LOGIN);
		initGUI();
	}
	
	public void initGUI() { // ȭ�� ����
		setBounds(0, 0, WIDTH, HEIGHT);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBackground(new Color(100,200,255));
		setLayout(null);

		JLabel lblLogo = new JLabel("Sign Up");
		lblLogo.setBounds(100, 60, 400, 100);
		lblLogo.setFont(new Font("Serif", Font.PLAIN, 100));
		add(lblLogo);
		
		JLabel lblID = new JLabel("ID");
		lblID.setBounds(170, 200, 90, 40);
		lblID.setFont(new Font("���� ���", Font.BOLD, 30));
		add(lblID);

		tf_ID = new JTextField();
		tf_ID.setBounds(220, 208, 150, 30);
		tf_ID.setSize(150, 30);
		add(tf_ID);
		tf_ID.setColumns(10);

		JLabel lblPW = new JLabel("Password");
		lblPW.setBounds(64, 261, 200, 34);
		lblPW.setFont(new Font("���� ���", Font.BOLD, 30));
		add(lblPW);

		tf_PW = new JTextField();
		tf_PW.setColumns(10);
		tf_PW.setBounds(220, 268, 150, 30);
		add(tf_PW);
		
		JLabel lblPWC = new JLabel("Password");
		lblPWC.setBounds(64, 320, 150, 34);
		lblPWC.setFont(new Font("���� ���", Font.BOLD, 30));
		add(lblPWC);
		
		JLabel lblPWC2 = new JLabel("Confirm");
		lblPWC2.setBounds(73, 345, 150, 34);
		lblPWC2.setFont(new Font("���� ���", Font.BOLD, 30));
		add(lblPWC2);
		
		tf_PWC = new JTextField();
		tf_PWC.setColumns(10);
		tf_PWC.setBounds(220, 335, 150, 30);
		add(tf_PWC);
		
		btnSignup = new JButton("Sign up");
		btnSignup.setBounds(160, 420, 186, 30);
		add(btnSignup);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(160, 470, 186, 30);
		add(btnCancel);
		
		SignupAction signupAction = new SignupAction();
		btnSignup.addActionListener(signupAction);
		tf_ID.addActionListener(signupAction);
		tf_PW.addActionListener(signupAction);
		tf_PWC.addActionListener(signupAction);
		
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				homePanel.remove(homePanel.signUpPanel);
				homePanel.showLogin();
			}
		});
		
		setVisible(true);
	}
	class SignupAction implements ActionListener { 
		@Override
		public void actionPerformed(ActionEvent event) {
			String id = tf_ID.getText().trim(); // ������ ���� �𸣴� ���� ���� trim() ���
			String pw = tf_PW.getText().trim(); // ������ ������ �𸣹Ƿ� ��������
			String pwc = tf_PWC.getText().trim(); // ������ ������ �𸣹Ƿ� ��������
			
			if(id.equals("")) {
				JOptionPane.showMessageDialog(homePanel, "���̵� �Է��ϼ���.", "Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			else if(pw.equals("") || pwc.equals("")) {
				JOptionPane.showMessageDialog(homePanel, "��й�ȣ�� �Է��ϼ���.", "Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			else if(!pw.equals(pwc)) {
				JOptionPane.showMessageDialog(homePanel, "��й�ȣ�� ��ġ���� �ʽ��ϴ�.", "Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			smNet.sendMSG("/SIGNUP "+ id +" " + pw);
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundLogin, 0, 0, getWidth(), getHeight(), this);
	}
}
