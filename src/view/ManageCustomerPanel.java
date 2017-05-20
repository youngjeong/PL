package view;

import model.Customer;
import controller.CustomerController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageCustomerPanel extends JPanel
{
	private JTextField numberField;
	private JTextField nameField;
	private JTextField phoneField;
	private JTextField joinDateField;

	private JButton registerButton;
	private JButton searchButton;
	private JButton deleteButton;

	public ManageCustomerPanel()
	{
		super(null);

		JLabel numberLabel = new JLabel("고객번호");
		numberLabel.setHorizontalAlignment(SwingConstants.CENTER);
		numberLabel.setBounds(new Rectangle(100, 30, 100, 50));
		add(numberLabel);

		numberField = new JTextField();
		numberField.setBounds(new Rectangle(300, 30, 200, 50));
		add(numberField);

		JLabel nameLabel = new JLabel("고 객 명");
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nameLabel.setBounds(new Rectangle(100, 90, 100, 50));
		add(nameLabel);

		nameField = new JTextField();
		nameField.setBounds(new Rectangle(300, 90, 200, 50));
		add(nameField);

		JLabel phoneLabel = new JLabel("전화번호");
		phoneLabel.setHorizontalAlignment(SwingConstants.CENTER);
		phoneLabel.setBounds(new Rectangle(100, 150, 100, 50));
		add(phoneLabel);

		phoneField = new JTextField();
		phoneField.setBounds(new Rectangle(300, 150, 200, 50));
		add(phoneField);

		JLabel joinDateLabel = new JLabel("가 입 일");
		joinDateLabel.setHorizontalAlignment(SwingConstants.CENTER);
		joinDateLabel.setBounds(new Rectangle(100, 210, 100, 50));
		add(joinDateLabel);

		joinDateField = new JTextField();
		joinDateField.setBounds(new Rectangle(300, 210, 200, 50));
		add(joinDateField);

		registerButton = new JButton("고객등록");
		registerButton.setBounds(new Rectangle(100, 330, 100, 50));
		registerButton.addActionListener(new RegisterActionListener());
		add(registerButton);

		searchButton = new JButton("고객검색");
		searchButton.setBounds(new Rectangle(250, 330, 100, 50));
		searchButton.addActionListener(new SearchActionListener());
		add(searchButton);

		deleteButton = new JButton("고객삭제");
		deleteButton.setBounds(new Rectangle(400, 330, 100, 50));
		deleteButton.addActionListener(new DeleteActionListener());
		add(deleteButton);

	}

	private void clear()
	{
		numberField.setText("");
		nameField.setText("");
		phoneField.setText("");
		joinDateField.setText("");
	}

	@Override
	public String toString()
	{
		return "고객관리";
	}

	private class RegisterActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Thread thread = new Thread() {
				@Override
				public void run()
				{
					CustomerController.getInstance().setCustomer(
						numberField.getText(),
						nameField.getText(),
						phoneField.getText(),
						joinDateField.getText()
					);

					clear();
				}
			};

			thread.start();
		}
	}

	private class SearchActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			Thread thread = new Thread() {
				@Override
				public void run()
				{
					Customer customer = CustomerController.getInstance().findCustomer(
						numberField.getText()
					);

					if (customer != null)
					{
						numberField.setText(customer.getNumber());
						nameField.setText(customer.getName());
						phoneField.setText(customer.getPhone());
						joinDateField.setText(customer.getDate());
					}
				}
			};

			thread.start();
		}
	}

	private class DeleteActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			Thread thread = new Thread() {
				@Override
				public void run()
				{
					CustomerController.getInstance().deleteCustomer(
						numberField.getText()
					);

					clear();
				}
			};

			thread.start();
		}
	}
}
