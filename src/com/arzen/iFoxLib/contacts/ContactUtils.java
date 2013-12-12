package com.arzen.iFoxLib.contacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;

public class ContactUtils {
//	private Context mContext;
//
//	public AboveContact(Context mContext) {
//		this.mContext = mContext;
//	}

	/**
	 * 得到全部通讯录
	 * @param context
	 * @param cb
	 */
	public static void getAllConcatsDatas(final Context context,final ContactCallBack cb) {
		new Thread() {
			public void run() {
				int i = 0;
				String orderBy = PhoneLookup.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
				// 获取游标
				Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, orderBy);
//				ArrayList<Contact> contacts = new ArrayList<Contact>();
				HashMap<String, Contact> contactsMaps = new HashMap<String, Contact>();
				while (cursor.moveToNext()) {
					// get Contact data id
					String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
					if (contactId == null) {
						continue;
					}
					// get contact people Name
					String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
					if (name == null || name.trim().equals("")) {
						name = "无姓名";
					}
					String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

					if (hasPhone.equalsIgnoreCase("1")) {
						hasPhone = "true";
					} else {
						hasPhone = "false";
					}
					String phoneNumber = "";
					if (Boolean.parseBoolean(hasPhone)) {
						// get phoneNumber by id
						Cursor phones = context.getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
						boolean phoneFlag = phones.moveToFirst();
						if (phoneFlag) {
							phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							if (phoneNumber != null && phoneNumber.indexOf("-") != -1) {
								phoneNumber = phoneNumber.replace("-", "");
							}
							if (phoneNumber.trim().length() < 11) {
								continue;
							}
							String filterPhoneNumber = StringFilter(phoneNumber);
							String filter = validatePhoneNumber(filterPhoneNumber);

							if (filter.length() > 11) {
								int index = filter.length() - 11;
								filter = filter.substring(index, filter.length());
							}
							if (!isMobileNO(filter)) {
								continue;
							} else {
								phoneNumber = filter;
							}

						}
						if (!phones.isClosed()) {
							phones.close();
						}
					}
					if (phoneNumber == null) {
						// phoneNumber =AbsContact.NOPHONENUMBER;
						continue;
					}
					if (phoneNumber != null && phoneNumber.trim().equals("")) {
						// phoneNumber =AbsContact.NOPHONENUMBER;
						continue;
					}
					Contact contact = new Contact();
					contact.systemId = contactId;
					contact.name = name.trim();
					contact.phone = phoneNumber.trim();
//					contacts.add(contact);
					contactsMaps.put(phoneNumber, contact);
					i++;
				}

				if (!cursor.isClosed())
					cursor.close();
				
				if(cb != null){
					cb.onCallBack(contactsMaps);
				}
			};
		}.start();
	}

	public interface ContactCallBack {
		public void onCallBack(HashMap<String,Contact> contacMaps);
	}

	private static String StringFilter(String str) throws PatternSyntaxException {
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	private static String StringFilter2(String str) throws PatternSyntaxException {
		if (str.indexOf("?") != -1) {
			str = str.replace("?", "");
		}
		if (str.indexOf("=") != -1) {
			str = str.replace("=", "");
		}
		return str;
	}

	private static String StringFilter1(String str) throws PatternSyntaxException {
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	private static boolean isExisIllegalityChar(String str) throws PatternSyntaxException {
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String regEx = "^[\u4E00-\u9FA5A-Za-z0-9_]+$";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	private static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	private static String validatePhoneNumber(String addressString) {
		if (addressString == null) {
			return addressString;
		}
		if (addressString.startsWith("+86")) {
			addressString = addressString.substring(3, addressString.length());
		}
		if (addressString.startsWith("86")) {
			addressString = addressString.substring(2, addressString.length());
		}
		if (addressString.startsWith("086")) {
			addressString = addressString.substring(3, addressString.length());
		}
		if (addressString.startsWith("+086")) {
			addressString = addressString.substring(4, addressString.length());
		}
		if (addressString.endsWith(";")) {
			addressString = addressString.substring(0, addressString.lastIndexOf(';'));
		}
		if (addressString.endsWith(";0")) {
			addressString = addressString.substring(0, addressString.lastIndexOf(";0"));
		}
		if (addressString.endsWith(":0")) {
			addressString = addressString.substring(0, addressString.lastIndexOf(":0"));
		}
		return addressString;
	}

}
