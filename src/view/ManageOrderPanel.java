package view;

import controller.CustomerController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageOrderPanel extends JPanel
{
	private static final String[] menu = {"김밥", "떡볶이", "순대", "오뎅", "튀김"};
	
	private JTextField dateField;
	private JTextField customerNumberField;
	private JComboBox<String> menuComboBox;

	private JButton orderButton;
	private JButton cancelButton;

	public ManageOrderPanel()
	{
		super(null);

		JLabel dateLabel = new JLabel("날    짜");
		dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dateLabel.setBounds(new Rectangle(100, 30, 100, 50));
		add(dateLabel);

		dateField = new JTextField();
		dateField.setBounds(new Rectangle(300, 30, 200, 50));
		add(dateField);

		JLabel customerNumberLabel = new JLabel("고객번호");
		customerNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
		customerNumberLabel.setBounds(new Rectangle(100, 130, 100, 50));
		add(customerNumberLabel);

		customerNumberField = new JTextField();
		customerNumberField.setBounds(new Rectangle(300, 130, 200, 50));
		add(customerNumberField);

		JLabel menuLabel = new JLabel("메    뉴");
		menuLabel.setHorizontalAlignment(SwingConstants.CENTER);
		menuLabel.setBounds(new Rectangle(100, 230, 100, 50));
		add(menuLabel);

		menuComboBox = new JComboBox<String>(menu);
		menuComboBox.setBounds(new Rectangle(300, 240, 200, 30));
		add(menuComboBox);

		orderButton = new JButton("주문");
		orderButton.setBounds(new Rectangle(100, 330, 150, 50));
		orderButton.addActionListener(new OrderActionListener());
		add(orderButton);

		cancelButton = new JButton("주문취소");
		cancelButton.setBounds(new Rectangle(350, 330, 150, 50));
		cancelButton.addActionListener(new CancelActionListener());
		add(cancelButton);

	}

	public void clear()
	{
		dateField.setText("");
		customerNumberField.setText("");
		menuComboBox.setSelectedIndex(0);
	}

	@Override
	public String toString()
	{
		return "주문관리";
	}

	private class OrderActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			Thread thread = new Thread() {
				@Override
				public void run()
				{
					boolean result = CustomerController.getInstance().addOrder(
						customerNumberField.getText(),
						dateField.getText()
					);

					if (result)
					{
						JOptionPane.showMessageDialog(
							ManageOrderPanel.this,
							customerNumberField.getText() + "번 고객님 무료쿠폰이 배송되었습니다.",
							"Coupon!",
							JOptionPane.PLAIN_MESSAGE, null
						);
					}

					clear();
				}
			};

			thread.start();
		}
	}

	private class CancelActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Thread thread = new Thread() {
				@Override
				public void run()
				{
					CustomerController.getInstance().cancelOrder(
						customerNumberField.getText(),
						dateField.getText()
					);

					clear();
				}
			};

			thread.start();
		}
	}
}
