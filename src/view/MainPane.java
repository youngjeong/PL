package view;

import javax.swing.*;
import java.awt.*;

public class MainPane extends JPanel
{
	private MainPane() {
		super(new GridLayout(1, 1));
		this.setPreferredSize(new Dimension(600, 460));

		JTabbedPane tabbedPane = new JTabbedPane();

		final ManageOrderPanel manageOrderPanel = new ManageOrderPanel();
		tabbedPane.addTab(manageOrderPanel.toString(), null, manageOrderPanel, null);

		final ManageCustomerPanel manageCustomerPanel = new ManageCustomerPanel();
		tabbedPane.addTab(manageCustomerPanel.toString(), null, manageCustomerPanel, null);

		final SalesInquiryPanel salesInquiryPanel = new SalesInquiryPanel();
		tabbedPane.addTab(salesInquiryPanel.toString(), null, salesInquiryPanel, null);

		add(tabbedPane);
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("분식집 고객관리 시스템");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(new MainPane(), BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				MainPane.createAndShowGUI();
			}
		});
	}
}