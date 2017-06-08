package view;

import controller.CustomerController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class SalesInquiryPanel extends JPanel
{
	private DateTextField fromDateField;
	private DateTextField toDateField;

	private JTextArea resultArea;

	private JButton inquiryButton;
	private JButton cancelButton;

	public SalesInquiryPanel()
	{
		super(null);

		JLabel dateLabel = new JLabel("날짜");
		dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dateLabel.setBounds(95, 30, 30, 30);
		add(dateLabel);

		fromDateField = new DateTextField();
		fromDateField.setBounds(125, 30, 150, 30);
		add(fromDateField);

		JLabel fromTo = new JLabel("~");
		fromTo.setHorizontalAlignment(SwingConstants.CENTER);
		fromTo.setBounds(275, 30, 30, 30);
		add(fromTo);

		toDateField = new DateTextField();
		toDateField.setBounds(305, 30, 150, 30);
		add(toDateField);

		JLabel until = new JLabel("까지");
		until.setHorizontalAlignment(SwingConstants.CENTER);
		until.setBounds(455, 30, 30, 30);
		add(until);

		resultArea = new JTextArea();
		resultArea.setBounds(95, 70, 390, 250);
		resultArea.setEditable(false);
		add(resultArea);

		inquiryButton = new JButton("조회");
		inquiryButton.setBounds(95, 330, 150, 50);
		inquiryButton.addActionListener(new InquiryActionListener());
		add(inquiryButton);

		cancelButton = new JButton("취소");
		cancelButton.setBounds(335, 330, 150, 50);
		cancelButton.addActionListener(new CancelActionListener());
		add(cancelButton);

	}

	@Override
	public String toString()
	{
		return "매출조회";
	}

	private class InquiryActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			HashMap<String, Integer> map =  CustomerController.getInstance().inquiry(fromDateField.getText(), toDateField.getText());

			int couponCount = 0;
			int sumCost = 0;

			String result = "메뉴\t갯수\t매출금액\r\n";
			result += "==============================\r\n";
			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				String menu = entry.getKey();
				int count = entry.getValue();

				if (menu.compareTo("쿠폰") == 0)
				{
					couponCount = count;
					continue;
				}

				int cost = CustomerController.getInstance().getMenuCost(menu);

				result += menu + "\t" + count + "\t" + count * cost + "\r\n";
				sumCost += count * cost;
			}

			result += "쿠폰" + "\t\t" + couponCount + "\r\n";
			result += "==============================\r\n";
			result += "매출합계" + "\t\t" + sumCost;

			resultArea.setText(result);
		}
	}

	private class CancelActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			resultArea.setText("");
		}
	}
}
