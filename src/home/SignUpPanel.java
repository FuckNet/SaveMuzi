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
	private static final int WIDTH = 400;
	private static final int HEIGHT = 400;
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
		setBounds(100, 100, 288, 392);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBackground(new Color(100,200,255));
		setLayout(null);

		JLabel lblLogo = new JLabel("Sign Up");
		lblLogo.setBounds(100, 25, 90, 34);
		lblLogo.setFont(new Font("Serif", Font.PLAIN, 22));
		add(lblLogo);
		
		JLabel lblID = new JLabel("ID");
		lblID.setBounds(60, 77, 90, 34);
		add(lblID);

		tf_ID = new JTextField();
		tf_ID.setBounds(92, 84, 150, 21);
		add(tf_ID);
		tf_ID.setColumns(10);

		JLabel lblPW = new JLabel("Password");
		lblPW.setBounds(14, 126, 90, 34);
		add(lblPW);

		tf_PW = new JTextField();
		tf_PW.setColumns(10);
		tf_PW.setBounds(92, 133, 150, 21);
		add(tf_PW);
		
		JLabel lblPWC = new JLabel("Password");
		lblPWC.setBounds(14, 166, 90, 34);
		add(lblPWC);
		
		JLabel lblPWC2 = new JLabel("Confirm");
		lblPWC2.setBounds(18, 186, 90, 34);
		add(lblPWC2);
		
		tf_PWC = new JTextField();
		tf_PWC.setColumns(10);
		tf_PWC.setBounds(92, 178, 150, 21);
		add(tf_PWC);
		
		btnSignup = new JButton("Sign up");
		btnSignup.setBounds(46, 240, 186, 30);
		add(btnSignup);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(46, 290, 186, 30);
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
