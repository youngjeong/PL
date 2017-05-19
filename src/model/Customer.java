package model;

import exceptions.ExceptionCode;
import exceptions.LogicalException;

import java.io.Serializable;
import java.util.ArrayList;

public class Customer implements Serializable
{
	private String number;
	private String name;
	private String phone;
	private String date;
	private int couponCount;
	private ArrayList<String> orders;

	public Customer()
	{
		orders = new ArrayList<String>();
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number) throws LogicalException
	{
		if (number == null || number.length() == 0)
			throw new LogicalException(ExceptionCode.NO_INFORMATION, "고객번호가 입력되지 않았습니다.");

		if(!number.matches("[0-9]*"))
			throw new LogicalException(ExceptionCode.INVALID_FORMAT_SPECIAL_CHARACTER, "고객번호에 비정상적인 문자가 입력되었습니다.");

		this.number = number;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name) throws LogicalException
	{
		if (name == null || name.length() == 0)
			throw new LogicalException(ExceptionCode.NO_INFORMATION, "이름이 입력되지 않았습니다.");

		if (name.length() >= 10)
		{
			throw new LogicalException(ExceptionCode.INVALID_FORMAT_TOO_LONG_NAME, "고객 이름이 너무 깁니다.");
		}

		if(!name.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*"))
			throw new LogicalException(ExceptionCode.INVALID_FORMAT_SPECIAL_CHARACTER, "고객번호에 비정상적인 문자가 입력되었습니다.");

		this.name = name;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone) throws LogicalException
	{
		if (phone == null || phone.length() == 0)
			throw new LogicalException(ExceptionCode.NO_INFORMATION, "전화번호가 입력되지 않았습니다.");

		if(!phone.matches("[0-9|-]*"))
			throw new LogicalException(ExceptionCode.INVALID_FORMAT_PHONE, "전화번호에 비정상적인 문자가 입력되었습니다.");

		this.phone = phone;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date) throws LogicalException
	{
		if (date == null || date.length() == 0)
			throw new LogicalException(ExceptionCode.NO_INFORMATION, "날짜가 입력되지 않았습니다.");

		if(!date.matches("[0-9|/]*"))
			throw new LogicalException(ExceptionCode.INVALID_FORMAT_DATE, "날짜에 비정상적인 문자가 입력되었습니다.");

		this.date = date;
	}

	public int getCouponCount()
	{
		return couponCount;
	}

	public ArrayList<String> getOrders()
	{
		return orders;
	}

	public boolean addOrder(String newOrder)
	{
		orders.add(newOrder);

		this.couponCount ++;
		if (couponCount >= 3)
		{
			couponCount = 0;
			return true;
		}

		return false;
	}

	public void deleteOrder(String delDate) throws LogicalException
	{
		if (orders.indexOf(delDate) == -1)
			throw new LogicalException(ExceptionCode.NO_INFORMATION, "해당 주문을 찾을 수 없습니다.");

		orders.remove(delDate);

		this.couponCount --;
		if (couponCount < 0)
			couponCount = 0;
	}
}
