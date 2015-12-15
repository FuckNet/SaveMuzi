package home;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.SMFrame;
import network.SMNet;

public class LoginPanel extends JPanel {
	private static final String BG_LOGIN = "res/background/backgroundLogin.png";
	private static final String FILE_NAME = "res/login/user.txt";

	private static final int WIDTH = 500;
	private static final int HEIGHT = 550;
	private Image backgroundLogin;

	private HomePanel homePanel;
	private SMNet smNet;

	JTextField tf_ID; // ID�� �Է¹�����
	JTextField tf_PW; // IP�� �Է¹�����
	JCheckBox checkSave; // ���̵�, ��� ���� üũ�ڽ�
	JCheckBox checkAuto; // �ڵ� �α��� üũ�ڽ�
	JButton btnLogin; // �α��� ��ư
	JButton btnSignup; // �α��� ��ư

	private BufferedReader in; // �����Է�
	private BufferedWriter out; // �������

	String isSave;
	String isAuto;
	public static String userID;
	public static String userPW;

	public LoginPanel(SMNet smNet, HomePanel homePanel) {
		this.smNet = smNet;
		this.homePanel = homePanel;
		backgroundLogin = Toolkit.getDefaultToolkit().getImage(BG_LOGIN);
		initGUI();
		//loadUserInfo();
	}

	public void resetAutoLogin() {
		isAuto = "false";
		checkAuto.setSelected(false);
		writeUserInfo();
	}
	
	public void loadUserInfo() {
		try {
			in = new BufferedReader(new FileReader(FILE_NAME));

			if ((isSave = in.readLine()) != null) {
				isAuto = in.readLine();
				userID = in.readLine();
				userPW = in.readLine();
				if (isSave.equals("true")) {
					tf_ID.setText(userID);
					tf_PW.setText(userPW);
					checkSave.setSelected(true);
				}
				if (isAuto.equals("true")) {
					checkAuto.setSelected(true);
					logIn(userID, userPW);
				}
			}
			in.close();
		} catch (Exception e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	public void initGUI() { // ȭ�� ����
		setBounds(0, 0, WIDTH, HEIGHT);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(null);
		setBackground(new Color(100, 200, 255));

		JLabel lblLogo = new JLabel("Log In");
		lblLogo.setBounds(110, 60, 400, 100);
		lblLogo.setFont(new Font("Serif", Font.PLAIN, 100));
		add(lblLogo);

		JLabel lblNewLabel = new JLabel("ID");
		lblNewLabel.setBounds(160, 200, 90, 40);
		lblNewLabel.setFont(new Font("���� ���", Font.BOLD, 30));
		add(lblNewLabel);

		tf_ID = new JTextField();
		tf_ID.setBounds(210, 208, 150, 30);
		tf_ID.setSize(150, 30);
		add(tf_ID);
		tf_ID.setColumns(10);

		JLabel lblServerIp = new JLabel("Password");
		lblServerIp.setBounds(54, 261, 200, 34);
		lblServerIp.setFont(new Font("���� ���", Font.BOLD, 30));
		add(lblServerIp);

		tf_PW = new JTextField();
		tf_PW.setBounds(210, 268, 150, 30);
		add(tf_PW);
		tf_PW.setColumns(10);

		checkSave = new JCheckBox();
		checkSave.setText("���̵�, ��й�ȣ ����");
		checkSave.setBounds(140, 325, 230, 21);
		checkSave.setOpaque(false);
		checkSave.setFont(new Font("���� ���", Font.BOLD, 20));
		add(checkSave);

		checkAuto = new JCheckBox();
		checkAuto.setText("�ڵ� �α���");
		checkAuto.setBounds(140, 365, 150, 21);
		checkAuto.setOpaque(false);
		checkAuto.setFont(new Font("���� ���", Font.BOLD, 20));
		add(checkAuto);

		btnLogin = new JButton("Log in");
		btnLogin.setBounds(150, 425, 186, 30);
		add(btnLogin);

		btnSignup = new JButton("Sign up");
		btnSignup.setBounds(150, 475, 186, 30);
		add(btnSignup);

		ConnectAction action = new ConnectAction(); 
		btnLogin.addActionListener(action);
		tf_ID.addActionListener(action);
		tf_PW.addActionListener(action);
		SignupAction signupAction = new SignupAction();
		btnSignup.addActionListener(signupAction);

		checkAuto.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				isAuto = Boolean.toString(checkAuto.isSelected());
				if (isAuto.equals("false")) {
					writeUserInfo();
				}
			}
		});

		checkSave.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				isSave = Boolean.toString(checkSave.isSelected());
				if (isSave.equals("false")) {
					writeUserInfo();
				}
			}

		});
		setVisible(true);
	}

	void writeUserInfo() {
		try {
			out = new BufferedWriter(new FileWriter(FILE_NAME));
			out.write(isSave);
			out.newLine();
			out.write(isAuto);
			out.newLine();
			out.write(userID);
			out.newLine();
			out.write(userPW);
			out.newLine();
			out.close();
		} catch (Exception e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	class ConnectAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			String _id = tf_ID.getText().trim(); // ������ ���� �𸣴� ���� ���� trim() ���
			String _pw = tf_PW.getText().trim(); // ������ ������ �𸣹Ƿ� ��������

			if (_id.equals("")) {
				JOptionPane.showMessageDialog(homePanel, "���̵� �Է��ϼ���.", "Message", JOptionPane.ERROR_MESSAGE);
				return;
			} else if (_pw.equals("")) {
				JOptionPane.showMessageDialog(homePanel, "��й�ȣ�� �Է��ϼ���.", "Message", JOptionPane.ERROR_MESSAGE);
				return;
			}
			logIn(_id, _pw);
		}
	}

	public void logIn(String id, String pw) {
		smNet.sendMSG("/LOGIN " + id + " " + pw);
	}

	class SignupAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			homePanel.remove(homePanel.loginPanel);
			homePanel.showSignup();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundLogin, 0, 0, getWidth(), getHeight(), this);
	}
}
