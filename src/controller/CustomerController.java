package controller;

import exceptions.ExceptionCode;
import exceptions.LogicalException;
import model.Customer;
import model.Order;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
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

	private FileLock lock;
	private FileChannel channel;

	private HashMap<String, Integer> price = new HashMap<String, Integer>();

	public CustomerController()
	{
		guest = new Customer();
		price.put("김밥", 1500);
		price.put("떡볶이", 2500);
		price.put("순대", 2000);
		price.put("오뎅", 1000);
		price.put("튀김", 3000);
	}

	public int getMenuCost(String menu)
	{
		return price.get(menu);
	}

	private HashMap<String, Customer> loadCustomInfoMap()
	{
		HashMap<String, Customer> customerInfoMap;
		customerInfoMap = new HashMap<String, Customer>();

		FileInputStream fis = null;
		ObjectInputStream ois = null;

		try
		{
			File f = new File("custom.txt");
			channel = new RandomAccessFile(f, "rw").getChannel();

			fis = new FileInputStream("custom.txt");
			ois = new ObjectInputStream(fis);

			HashMap<String, Customer> tempMap = (HashMap)ois.readObject();

			for (Map.Entry<String, Customer> entry : tempMap.entrySet())
				customerInfoMap.put(entry.getKey(), entry.getValue());

			guest = (Customer)ois.readObject();

			lock = channel.tryLock();

			if (lock == null) {
				channel.close();
				throw new LogicalException(ExceptionCode.FILE_LOCKED, "이미 파일이 다른 곳에서 수정중입니다.");
			}
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

		return customerInfoMap;
	}

	private void saveData(HashMap<String, Customer> customerInfoMap)
	{
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;

		try
		{
			fos = new FileOutputStream("custom.txt");
			oos = new ObjectOutputStream(fos);

			oos.writeObject(customerInfoMap);
			oos.writeObject(guest);

			if (lock != null) {
				lock.release();
				channel.close();
			}
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
		HashMap<String, Customer> customerInfoMap = loadCustomInfoMap();
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
		finally
		{
			saveData(customerInfoMap);
		}
	}

	public synchronized void deleteCustomer(String number)
	{
		HashMap<String, Customer> customerInfoMap = loadCustomInfoMap();
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
		finally
		{
			saveData(customerInfoMap);
		}
	}

	public synchronized Customer findCustomer(String number)
	{
		HashMap<String, Customer> customerInfoMap = loadCustomInfoMap();
		try
		{
			if (!customerInfoMap.containsKey(number))
				throw new LogicalException(ExceptionCode.CANNOT_FIND_CUSTOMER, "해당 고객을 찾을 수 없습니다.");
		}
		catch (LogicalException ex)
		{
			ex.showExceptionPopup();
		}
		finally
		{
			saveData(customerInfoMap);
		}

		return customerInfoMap.get(number);
	}

	public synchronized boolean addOrder(String number, String date, String menu)
	{
		HashMap<String, Customer> customerInfoMap = loadCustomInfoMap();
		boolean result = false;
		try
		{
			if (number == null || number.length() == 0)
			{
				if (date == null || date.length() == 0)
					throw new LogicalException(ExceptionCode.NO_INFORMATION, "주문날짜가 입력되지 않았습니다.");

				if(!date.matches("[0-9|/]*"))
					throw new LogicalException(ExceptionCode.INVALID_FORMAT_DATE, "날짜에 비정상적인 문자가 입력되었습니다.");

				result = guest.addOrder(date, menu);
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
				result = customer.addOrder(date, menu);
			}
		}
		catch (LogicalException ex)
		{
			ex.showExceptionPopup();
		}
		finally
		{
			saveData(customerInfoMap);
		}
		return result;
	}

	public synchronized void cancelOrder(String number, String date)
	{
		HashMap<String, Customer> customerInfoMap = loadCustomInfoMap();
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
		finally
		{
			saveData(customerInfoMap);
		}
	}

	public HashMap<String, Integer> inquiry(String startDate, String endDate)
	{
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		HashMap<String, Customer> customerInfoMap = loadCustomInfoMap();
		for (Customer customer : customerInfoMap.values())
		{
			for (Order order : customer.getOrders())
			{
				if (startDate.compareTo(order.getDate()) <= 0 &&
					order.getDate().compareTo(endDate) <= 0)
				{
					if (!result.containsKey(order.getMenu()))
						result.put(order.getMenu(), 0);

					int prev = result.get(order.getMenu());
					result.put(order.getMenu(), prev + 1);
				}
			}
		}

		for (Order order : guest.getOrders())
		{
			if (startDate.compareTo(order.getDate()) <= 0 &&
				order.getDate().compareTo(endDate) <= 0)
			{
				if (!result.containsKey(order.getMenu()))
					result.put(order.getMenu(), 0);

				int prev = result.get(order.getMenu());
				result.put(order.getMenu(), prev + 1);
			}
		}

		for (Customer customer : customerInfoMap.values())
		{
			for (String couponDate : customer.getCoupons())
			{
				if (startDate.compareTo(couponDate) <= 0 &&
					couponDate.compareTo(endDate) <= 0)
				{
					if (!result.containsKey("쿠폰"))
						result.put("쿠폰", 0);

					int prev = result.get("쿠폰");
					result.put("쿠폰", prev + 1);
				}
			}
		}

		for (String couponDate : guest.getCoupons())
		{
			if (startDate.compareTo(couponDate) <= 0 &&
				couponDate.compareTo(endDate) <= 0)
			{
				if (!result.containsKey("쿠폰"))
					result.put("쿠폰", 0);

				int prev = result.get("쿠폰");
				result.put("쿠폰", prev + 1);
			}
		}

		saveData(customerInfoMap);

		return result;
	}
}
