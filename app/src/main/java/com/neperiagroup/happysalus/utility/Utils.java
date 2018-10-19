package com.neperiagroup.happysalus.utility;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Utils
{
    public static UUID convertFromInteger(int i)
    {
        final long MSB = 0x0000000000001000L;
        final long LSB = 0x800000805f9b34fbL;
        long value = i & 0xFFFFFFFF;
        return new UUID(MSB | (value << 32), LSB);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String parse(final BluetoothGattCharacteristic characteristic)
    {
        final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        final byte[] data = characteristic.getValue();
        if (data == null) return "";
        final int length = data.length;
        if (length == 0) return "";

        final char[] out = new char[length * 3 - 1];
        for (int j = 0; j < length; j++)
        {
            int v = data[j] & 0xFF;
            out[j * 3] = HEX_ARRAY[v >>> 4];
            out[j * 3 + 1] = HEX_ARRAY[v & 0x0F];
            if (j != length - 1) out[j * 3 + 2] = '-';
        }
        return new String(out);
    }

    public static byte[] changeToByte(String sendData)
    {
        byte[] myByte = new byte[16];
        int[] myInt = new int[16];
        for (int i = 0; i < myByte.length; i++)
        {
            myInt[i] = Integer.valueOf(sendData.substring(i * 2, (i + 1) * 2), 16);
        }
        for (int i = 0; i < myByte.length; i++)
        {
            myByte[i] = (byte) myInt[i];
        }
        return myByte;
    }

    public static byte[] hexStringToByteArray(String s)
    {
        int len = s.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2)
        {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }

        return data;
    }

    final protected static char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String byteArrayToHexString(byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];
        int v;

        for (int j = 0; j < bytes.length; j++)
        {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }

    public static String xorHex(String a, String b)
    {
        char[] chars = new char[a.length()];
        for (int i = 0; i < chars.length; i++)
        {
            chars[i] = toHex(fromHex(a.charAt(i)) ^ fromHex(b.charAt(i)));
        }
        return new String(chars);
    }

    public static int fromHex(char c)
    {
        if (c >= '0' && c <= '9')
        {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F')
        {
            return c - 'A' + 10;
        }
        if (c >= 'a' && c <= 'f')
        {
            return c - 'a' + 10;
        }
        throw new IllegalArgumentException();
    }

    public static char toHex(int nybble)
    {
        if (nybble < 0 || nybble > 15)
        {
            throw new IllegalArgumentException();
        }
        return "0123456789ABCDEF".charAt(nybble);
    }

    public static Date getDate(int year, int month, int day, int hour, int minute, int second)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static String panString(String value, String pan, int count)
    {
        String panned = null;
        int len = 0;

        panned = value;
        len = panned.length();

        for (int i = count; i > len; i--)
        {
            panned = pan + panned;
        }

        return panned;
    }

    public static byte getBit(byte ID, int position)
    {
        return (byte)((ID >> position) & 1);
    }
}
