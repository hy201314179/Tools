package com.jkx4rh.client.tool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class StringUtils {

	/**
	 * 电话号码中间四位****号显示
	 */
	public static String getXPhoneNum(String phoneNum) {

		if (phoneNum == null) {
			return phoneNum;
		}
		if (phoneNum.length() == 11) {
			return phoneNum.substring(0, 3) + "****" + phoneNum.substring(7);

		}
		return phoneNum;
	}

	public static String getDateString(String date) {
		if (date == null) {
			return date;
		}
		return date.replace('-', '.');
	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 处理字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String isNullStr(String str) {
		if (str == null || str.equals(null) || str.equals("null")) {
			return "";
		} else {
			return str;
		}
	}

	/**
	 * 处理数字类字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String isNullNumber(String str) {
		if (str == null || str.equals(null) || str.equals("null")) {
			return "0.00";
		} else {
			return str;
		}
	}

	/**
	 * 根据身份证号获取年龄
	 * 
	 * @param sfcard
	 *            身份证号
	 * 
	 * @return 年龄
	 */
	public static String sFcardToAge(String sfcard) {
		if (TextUtils.isEmpty(sfcard)) {
			return "";
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
		String years = simpleDateFormat.format(new Date());
		if (sfcard.length() == 15) {
			// 获取出生日期
			int year = Integer.parseInt("19" + sfcard.substring(6, 8));
			// 获取年龄
			int age = Integer.parseInt(years) - year;
			return age + "";
		} else if (sfcard.length() == 18) {
			// 获取出生年份
			int year = Integer.parseInt(sfcard.substring(6, 10));
			int age = Integer.parseInt(years) - year;
			return age + "";
		} else {
			return "";
		}
	}

	/**
	 * 根据身份证算性别
	 * 
	 * @param sfcard
	 * @return
	 */
	public static String sFcardToSex(String sfcard) {
		String gender = "";
		if (sfcard.length() == 15) {
			String str = sfcard.substring(13, 14);

			if (Integer.parseInt(str) % 2 != 0) {
				gender = "1";
			} else {
				gender = "2";
			}
			return gender;
		} else if (sfcard.length() == 18) {
			String str = sfcard.substring(16, 17);
			if (Integer.parseInt(str) % 2 != 0) {
				gender = "1";
			} else {
				gender = "2";
			}
			return gender;
		} else {
			return "";
		}
	}

	/**
	 * 处理日期类字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String interceptDate(String str) {
		if (str == null || str.equals(null) || str.equals("null")) {
			return "";
		} else if (str.indexOf("T") >= 0) {
			str = str.substring(0, str.indexOf("T"));
		}
		return str;
	}

	/**
	 * 身份证号前三位后四位显示中间用*显示
	 */
	public static String getXIDCard(String idcard) {
		if (idcard == null) {
			return idcard;
		}

		if (idcard.length() == 15) {
			return idcard.substring(0, 8) + "****" + idcard.substring(12);
		}
		if (idcard.length() == 18) {
			return idcard.substring(0, 10) + "****" + idcard.substring(14);
		}
		return idcard;
	}

	/**
	 * 根据身份证验证年龄 和性别
	 * 
	 * @param idCard
	 *            输入的身份证号
	 * @param sex
	 *            输入的性别(男 ，女)
	 * @param age
	 *            输入的年龄
	 * @return
	 */
	public static boolean verifySaveInformation(String idCard, String sex,
			String age, Context context) {
		if (idCard == null) {
			return false;
		}
		if (TextUtils.isEmpty(sex)) {
			return true;
		}
		if (TextUtils.isEmpty(age)) {
			return true;
		}
		if (idCard.length() == 15) {
			return SaveInformationBy15bit(idCard, sex, age, context);
		} else if (idCard.length() == 18) {
			return SaveInformationBy18bit(idCard, sex, age, context);
		} else {
			ToastUtil.showToast(context, "输入的身份证号不正确", Toast.LENGTH_SHORT);
			return false;
		}
	}

	/**
	 * 15位的身份证校验 性别与出生日期
	 * 
	 * @param idCard
	 * @param context
	 * @return
	 */
	public static boolean SaveInformationBy15bit(String idCard, String sex,
			String age, Context context) {
		String id14 = idCard.substring(13, 14);
		String gender = "";
		if (Integer.parseInt(id14) % 2 != 0) {
			gender = "1";
		} else {
			gender = "2";
		}
		if ("男".equals(sex)) {
			sex = "1";
		} else {
			sex = "2";
		}
		if (gender.equals(sex)) {
			return verifyAge15(idCard, age, context);
		} else {
			ToastUtil.showToast(context, "身份证性别与选择性别不匹配", Toast.LENGTH_SHORT);
			return false;
		}
	}

	/**
	 * 18位的身份证校验 性别与出生日期
	 * 
	 * @param idcard
	 * @param age
	 * @param sex
	 * @param context
	 * @return
	 */
	public static boolean SaveInformationBy18bit(String idcard, String sex,
			String age, Context context) {

		// 获取性别
		String id17 = idcard.substring(16, 17);
		String gender = "";
		if (Integer.parseInt(id17) % 2 != 0) {
			gender = "1";
		} else {
			gender = "2";
		}
		if ("男".equals(sex)) {
			sex = "1";
		} else {
			sex = "2";
		}
		if (gender.equals(sex)) {
			return verifyAge18(idcard, age, context);
		} else {
			ToastUtil.showToast(context, "身份证性别与选择性别不匹配", Toast.LENGTH_SHORT);
			return false;
		}
	}

	/**
	 * 15位身份证验证年龄
	 * 
	 * @param idCard
	 * @param age
	 * @param context
	 * @return
	 */
	public static boolean verifyAge15(String idCard, String age, Context context) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
		String years = simpleDateFormat.format(new Date());
		// 获取出生日期
		int year = Integer.parseInt("19" + idCard.substring(6, 8));
		// 获取年龄
		int lAge = Integer.parseInt(years) - year;
		if (lAge == Integer.parseInt(age)) {
			return true;
		} else {
			ToastUtil.showToast(context, "输入年龄与身份证不符", Toast.LENGTH_SHORT);
			return false;
		}
	}

	/**
	 * 18位身份证验证年龄
	 * 
	 * @param idCard
	 * @param age
	 * @param context
	 * @return
	 */
	public static boolean verifyAge18(String idCard, String age, Context context) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
		String years = simpleDateFormat.format(new Date());
		int year = Integer.parseInt(idCard.substring(6, 10));
		int lAge = Integer.parseInt(years) - year;
		if (lAge == Integer.parseInt(age)) {
			return true;
		} else {
			ToastUtil.showToast(context, "输入年龄与身份证不符", Toast.LENGTH_SHORT);
			return false;
		}
	}

}
