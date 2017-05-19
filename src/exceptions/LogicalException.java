package exceptions;

import javax.swing.*;

public class LogicalException extends Exception
{
	public static JPanel root;
	private ExceptionCode code;

	public LogicalException(ExceptionCode code, String message)
	{
		super(message);

		this.code = code;
	}

	public void showExceptionPopup()
	{
		JOptionPane.showMessageDialog(root, getMessage(), "Exception!", JOptionPane.PLAIN_MESSAGE, null);
	}
}
