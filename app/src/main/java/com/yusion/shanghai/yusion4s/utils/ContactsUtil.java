package com.yusion.shanghai.yusion4s.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Created by ice on 2017/8/7.
 */

public class ContactsUtil {

    public static boolean isContactsAvailable(Context context) {
        Cursor cursor = getCursor(context);
        boolean isAvailable = isCursorAvailable(cursor);
        if (isAvailable) {
            cursor.close();
        }
        return isAvailable;
    }

    public static String[] getPhoneContacts(Context context, Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = context.getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            try {
                contact[0] = cursor.getString(nameFieldColumnIndex);
            } catch (CursorIndexOutOfBoundsException e) {
                return null;//适配小米手机权限拒绝的情况
            }
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if (phone != null) {
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phone.close();
            }
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }

    private static boolean isCursorAvailable(Cursor cursor) {
        return cursor != null && cursor.getCount() > 0;
    }

    private static Cursor getCursor(Context context) {
        return context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
    }
}