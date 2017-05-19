package controller;

import exceptions.ExceptionCode;
import exceptions.LogicalException;
import model.Customer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CustomerController
{
	private static CustomerController instance = new CustomerController();

	public static CustomerController getInstance()
	{
		return instance;
	}

	private Customer guest;
	private HashMap<String, Customer> customerInfoMap;

	public CustomerController()
	{
		guest = new Customer();
		customerInfoMap = new HashMap<String, Customer>();

		FileInputStream fis = null;
		ObjectInputStream ois = null;

		try
		{
			fis = new FileInputStream("customer.txt");
			ois = new ObjectInputStream(fis);

			HashMap<String, Customer> tempMap = (HashMap)ois.readObject();

			for (Map.Entry<String, Customer> entry : tempMap.entrySet())
				customerInfoMap.put(entry.getKey(), entry.getValue());
		}
		catch (FileNotFoundException fnex) {}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				if (fis != null) fis.close();
				if (ois != null) ois.close();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	private void saveData()
	{
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;

		try
		{
			fos = new FileOutputStream("customer.txt");
			oos = new ObjectOutputStream(fos);

			oos.writeObject(customerInfoMap);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				if (fos != null) fos.close();
				if (oos != null) oos.close();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public synchronized void setCustomer(String number, String name, String phone, String date)
	{
		Customer customer = new Customer();
		try
		{
			customer.setNumber(number);
			customer.setName(name);
			customer.setPhone(phone);
			customer.setDate(date);

			customerInfoMap.put(number, customer);
		}
		catch (LogicalException ex)
		{
			ex.showExceptionPopup();
		}

		saveData();
	}

	public synchronized void deleteCustomer(String number)
	{
		try
		{
			if (!customerInfoMap.containsKey(number))
				throw new LogicalException(ExceptionCode.CANNOT_FIND_CUSTOMER, "해당 고객을 찾을 수 없습니다.");

			customerInfoMap.remove(number);
		}
		catch (LogicalException ex)
		{
			ex.showExceptionPopup();
		}

		saveData();
	}

	public synchronized Customer findCustomer(String number)
	{
		try
		{
			if (!customerInfoMap.containsKey(number))
				throw new LogicalException(ExceptionCode.CANNOT_FIND_CUSTOMER, "해당 고객을 찾을 수 없습니다.");
		}
		catch (LogicalException ex)
		{
			ex.showExceptionPopup();
		}

		return customerInfoMap.get(number);
	}

	public synchronized boolean addOrder(String number, String date)
	{
		boolean result = false;
		try
		{
			if (number == null || number.length() == 0)
			{
				if (date == null || date.length() == 0)
					throw new LogicalException(ExceptionCode.NO_INFORMATION, "주문날짜가 입력되지 않았습니다.");

				if(!date.matches("[0-9|/]*"))
					throw new LogicalException(ExceptionCode.INVALID_FORMAT_DATE, "날짜에 비정상적인 문자가 입력되었습니다.");

				result = guest.addOrder(date);
			}
			else
			{
				if(!number.matches("[0-9]*"))
					throw new LogicalException(ExceptionCode.INVALID_FORMAT_SPECIAL_CHARACTER, "고객번호에 비정상적인 문자가 입력되었습니다.");

				if (!customerInfoMap.containsKey(number))
					throw new LogicalException(ExceptionCode.CANNOT_FIND_CUSTOMER, "해당 고객을 찾을 수 없습니다.");

				if (date == null || date.length() == 0)
					throw new LogicalException(ExceptionCode.NO_INFORMATION, "주문날짜가 입력되지 않았습니다.");

				if(!date.matches("[0-9|/]*"))
					throw new LogicalException(ExceptionCode.INVALID_FORMAT_DATE, "날짜에 비정상적인 문자가 입력되었습니다.");

				Customer customer = customerInfoMap.get(number);
				result = customer.addOrder(date);
			}
		}
		catch (LogicalException ex)
		{
			ex.showExceptionPopup();
		}

		saveData();
		return result;
	}

	public synchronized void cancelOrder(String number, String date)
	{
		try
		{
			if (number == null || number.length() == 0)
			{
				if (date == null || date.length() == 0)
					throw new LogicalException(ExceptionCode.NO_INFORMATION, "주문날짜가 입력되지 않았습니다.");

				if(!date.matches("[0-9|/]*"))
					throw new LogicalException(ExceptionCode.INVALID_FORMAT_DATE, "날짜에 비정상적인 문자가 입력되었습니다.");

				guest.deleteOrder(date);
			}
			else
			{
				if(!number.matches("[0-9]*"))
					throw new LogicalException(ExceptionCode.INVALID_FORMAT_SPECIAL_CHARACTER, "고객번호에 비정상적인 문자가 입력되었습니다.");

				if (!customerInfoMap.containsKey(number))
					throw new LogicalException(ExceptionCode.CANNOT_FIND_CUSTOMER, "해당 고객을 찾을 수 없습니다.");

				if (date == null || date.length() == 0)
					throw new LogicalException(ExceptionCode.NO_INFORMATION, "주문날짜가 입력되지 않았습니다.");

				if (!date.matches("[0-9|/]*"))
					throw new LogicalException(ExceptionCode.INVALID_FORMAT_DATE, "날짜에 비정상적인 문자가 입력되었습니다.");

				Customer customer = customerInfoMap.get(number);

				customer.deleteOrder(date);
			}
		}
		catch (LogicalException ex)
		{
			ex.showExceptionPopup();
		}

		saveData();
	}

}
