package gameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartGame extends JFrame {

	private int level = -1;
	private long id = 0;
	private Ex2 ex2;

	public StartGame(Ex2 start) {
		ex2=start;
	}

	public void draw_start_game() {
		this.setSize(350, 120);
		this.setTitle("Start Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		JLabel idLabel = new JLabel("Enter ID:");
		JLabel leveLabel = new JLabel("Enter Level [0,23]:");
		JTextField idText = new JTextField(9);
		JTextField levelText = new JTextField(2);
		JButton Run = new JButton("Run");
		panel.add(idLabel);
		panel.add(idText);
		panel.add(leveLabel);
		panel.add(levelText);
		panel.add(Run);
		this.getContentPane().add(BorderLayout.CENTER, panel);
		JFrame temp = this;
		this.setVisible(true);
		clickButton(Run,idText,levelText,temp);
	}
	
	
	public void clickButton(JButton Run,JTextField idText,JTextField levelText,JFrame temp) {
		Run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				id = Integer.parseInt(idText.getText());
				level = Integer.parseInt(levelText.getText());
				if (String.valueOf(id).length() == 9 && level >= 0 && level <= 23) {
					ex2.setid(id);
					ex2.setNum(level);
					Thread client = new Thread(ex2);
					client.start();
					temp.setVisible(false);
				} else
					JOptionPane.showMessageDialog(temp, "ID or Level Number is Wrong");
			}
		});
	}
}