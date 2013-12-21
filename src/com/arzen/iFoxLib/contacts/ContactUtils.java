package com.arzen.iFoxLib.contacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.arzen.iFoxLib.api.HttpIfoxApi;
import com.arzen.iFoxLib.api.HttpSetting;
import com.arzen.iFoxLib.bean.BaseBean;
import com.arzen.iFoxLib.utils.CommonUtil;
import com.encore.libs.http.HttpConnectManager;
import com.encore.libs.http.OnRequestListener;
import com.encore.libs.json.JacksonUtils;
import com.encore.libs.utils.Log;

public class ContactUtils {
	// private Context mContext;
	//
	// public AboveContact(Context mContext) {
	// this.mContext = mContext;
	// }

	/**
	 * 得到全部通讯录
	 * 
	 * @param context
	 * @param cb
	 */
	public static void getAllConcatsDatas(final Context context, final ContactCallBack cb) {
		new Thread() {
			public void run() {
				int i = 0;
				// String orderBy = PhoneLookup.DISPLAY_NAME +
				// " COLLATE LOCALIZED ASC";
				// 获取游标
				Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, "");
				ArrayList<Contact> contacts = new ArrayList<Contact>();
				// HashMap<String, Contact> contactsMaps = new HashMap<String,
				// Contact>();
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
//					contact.systemId = contactId;
					contact.name = name.trim();
					contact.phone = phoneNumber.trim();
					contacts.add(contact);
					// contactsMaps.put(phoneNumber, contact);
					i++;
				}

				if (!cursor.isClosed())
					cursor.close();

				if (cb != null) {
					cb.onCallBack(contacts);
				}
			};
		}.start();
	}

	public static final String CHACHE_NAME = "/contacts.c";

	/**
	 * 保存通讯录
	 * 
	 * @param context
	 * @param contacts
	 */
	public static void saveContacts(Context context, ArrayList<Contact> contacts) {
		if (contacts != null && contacts.size() != 0) {
			String path = context.getFilesDir().toString() + CHACHE_NAME;
			CommonUtil.object2File(contacts, path);
		}
	}

	/**
	 * 获取所有缓存
	 * 
	 * @param context
	 * @return
	 */
	public static ArrayList<Contact> getAllContactsByCache(Context context) {
		String path = context.getFilesDir().toString() + CHACHE_NAME;
		Object object = CommonUtil.file2Object(path);
		if (object != null) {
			ArrayList<Contact> contacts = (ArrayList<Contact>) object;
			return contacts;
		}
		return null;
	}

	/**
	 * 获取需要上传的通讯录
	 * 
	 * @param systemContacts
	 * @param cacheContacts
	 * @return
	 */
	public static ArrayList<Contact> getUpLoadContacts(ArrayList<Contact> systemContacts, ArrayList<Contact> cacheContacts) {
		ArrayList<Contact> upLoadContacts = new ArrayList<Contact>();
		if(systemContacts == null || cacheContacts == null){
			return upLoadContacts;
		}
		HashMap<String, String> maps = new HashMap<String, String>();
		for (int j = 0; j < cacheContacts.size(); j++) {
			Contact  cacheContact = cacheContacts.get(j);
			maps.put(cacheContact.phone, cacheContact.name);
		}
		
		for (int i = 0; i < systemContacts.size(); i++) {
			Contact systemContact = systemContacts.get(i);
			boolean isExists = maps.containsKey(systemContact.phone); //是否存在
			//当前不存在 在缓存当中
			if(!isExists){
				upLoadContacts.add(systemContact); //保存到上传list 列表
			}
		}
		return upLoadContacts;
	}
	
	/**
	 * 上传通讯录，每次只能上传100条数据
	 */
	public static void upLoadContacts(Context context,String token,ArrayList<Contact> uploadContacts,String gid,String cid,String clientId,String clientSecret)
	{
		if(uploadContacts == null || uploadContacts.size() == 0){
			return;
		}
		Log.d("Contact", "upLoadContact() !!!");
		//少于100条，直接上传
		if(uploadContacts.size() < 100){
			String json = JacksonUtils.shareJacksonUtils().parseObj2Json(uploadContacts);
			
			upLoad(context, token, json,gid, cid, clientId, clientSecret);
		}else{ //大于100条分组上传
			
			int maxCount = 100;
			
			int totalSize = uploadContacts.size();
			int upSize = totalSize / maxCount;// 上传次数
			int mo = totalSize % maxCount; // +1
			if (mo != 0) {
				upSize += 1;
			}
			Log.d("Contact", "totalSize:" + totalSize + " upSize:" + upSize + " mo:" + mo);
			
			for (int i = 0; i < upSize; i++) {
				List<Contact> subContacts = null;
				if (i == (upSize - 1)) {
					int start = i * maxCount;
					if (start > uploadContacts.size()) {
						start = uploadContacts.size();
					}
					subContacts = uploadContacts.subList(start, uploadContacts.size());
				} else {
					int start = i * maxCount;
					int end = (i + 1) * maxCount;
					if (start > uploadContacts.size()) {
						start = uploadContacts.size();
					}
					if (end >= uploadContacts.size()) {
						end = uploadContacts.size();
					}
					subContacts = uploadContacts.subList(start, end);
				}

				if (subContacts != null) {
					String json = JacksonUtils.shareJacksonUtils().parseObj2Json(subContacts);
					upLoad(context, token, json,gid, cid, clientId, clientSecret);
					// YateInterface.getInstance().uploadContacts(subContacts);
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private static void upLoad(Context context,String token,String json,String gid,String cid,String clientId,String clientSecret)
	{
		
		HttpIfoxApi.upLoadContacts(context, token, json, gid, cid, clientId, clientSecret, new OnRequestListener() {
			
			@Override
			public void onResponse(final String url, final int state, final Object result, final int type) {
				// TODO Auto-generated method stub
				if (state == HttpConnectManager.STATE_SUC && result != null && result instanceof BaseBean) {
					BaseBean baseBean = (BaseBean) result;
					if (baseBean.getCode() == HttpSetting.RESULT_CODE_OK) {
						Log.d("Contact", "upLoad contact success");
					} else {
						Log.d("Contact", "upLoad contact fail:" + baseBean.getMsg());
					}
				} else if (state == HttpConnectManager.STATE_TIME_OUT) { // 请求超时
					Log.d("Contact", "upLoad contact time out");
				} else { // 请求失败
					Log.d("Contact", "upLoad contact fail!");
				}
			}
		});
	}
	
	

	public interface ContactCallBack {
		public void onCallBack(ArrayList<Contact> contacts);
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
