package com.little.framework.utils;

import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 * Alibaba
 * Author: juqiu.lt
 * Date: 2015-1-30
 * Time: 下午2:53:24 
 *
 * @param <T>
 */
public class Pack<T> implements Serializable {

    private static final String LOG_TAG = "Pack";

    private static final long serialVersionUID = -2313525078625494026L;

    private HashMap<T, Object> mMap = new HashMap<T, Object>();

    public Pack() {

    }

    public Pack(Pack<T> pack) {
        mMap.putAll(pack.mMap);
    }

    //------------get for common-------------
    public boolean getBoolean(T key, boolean defaultValue) {
        Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (Boolean) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Boolean", defaultValue, e);
            return defaultValue;
        }
    }

    public byte getByte(T key, byte defaultValue) {
        Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (Byte) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Byte", defaultValue, e);
            return defaultValue;
        }
    }

    public char getChar(T key, char defaultValue) {
        Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (Character) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Character", defaultValue, e);
            return defaultValue;
        }
    }

    public short getShort(T key, short defaultValue) {
        Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (Short) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Short", defaultValue, e);
            return defaultValue;
        }
    }

    public int getInt(T key, int defaultValue) {
        Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (Integer) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Integer", defaultValue, e);
            return defaultValue;
        }
    }

    public long getLong(T key, long defaultValue) {
        Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (Long) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Long", defaultValue, e);
            return defaultValue;
        }
    }

    public float getFloat(T key, float defaultValue) {
        Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (Float) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Float", defaultValue, e);
            return defaultValue;
        }
    }

    public double getDouble(T key, double defaultValue) {
        Object o = mMap.get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (Double) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "Double", defaultValue, e);
            return defaultValue;
        }
    }

    public CharSequence getCharSequence(T key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (CharSequence) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "CharSequence", e);
            return null;
        }
    }

    public String getString(T key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (String) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "String", e);
            return null;
        }
    }

    //-----------get for common array----------
    public boolean[] getBooleanArray(T key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (boolean[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "boolean[]", e);
            return null;
        }
    }

    public byte[] getByteArray(T key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (byte[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "byte[]", e);
            return null;
        }
    }

    public char[] getCharArray(T key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (char[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "char[]", e);
            return null;
        }
    }

    public int[] getIntArray(T key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (int[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "int[]", e);
            return null;
        }
    }

    public long[] getLongArray(T key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (long[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "long[]", e);
            return null;
        }
    }

    public float[] getFloatArray(T key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (float[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "float[]", e);
            return null;
        }
    }

    public double[] getDoubleArray(T key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (double[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "double[]", e);
            return null;
        }
    }

    public String[] getStringArray(T key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (String[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "String[]", e);
            return null;
        }
    }

    public CharSequence[] getCharSequenceArray(T key) {
        Object o = mMap.get(key);
        if (o == null) {
            return null;
        }
        try {
            return (CharSequence[]) o;
        } catch (ClassCastException e) {
            typeWarning(key, o, "CharSequence[]", e);
            return null;
        }
    }

    //--------------get for object--------------
    public Object get(T key) {
        return mMap.get(key);
    }

    //--------------put for common--------------
    public void putBoolean(T key, boolean value) {
        mMap.put(key, value);
    }

    public void putByte(T key, byte value) {
        mMap.put(key, value);
    }

    public void putChar(T key, char value) {
        mMap.put(key, value);
    }

    public void putShort(T key, short value) {
        mMap.put(key, value);
    }

    public void putInt(T key, int value) {
        mMap.put(key, value);
    }

    public void putLong(T key, long value) {
        mMap.put(key, value);
    }

    public void putFloat(T key, float value) {
        mMap.put(key, value);
    }

    public void putDouble(T key, double value) {
        mMap.put(key, value);
    }

    public void putString(T key, String value) {
        mMap.put(key, value);
    }

    public void putCharSequence(T key, CharSequence value) {
        mMap.put(key, value);
    }

    //--------------put for common array--------------
    public void putBooleanArray(T key, boolean[] value) {
        mMap.put(key, value);
    }

    public void putByteArray(T key, byte[] value) {
        mMap.put(key, value);
    }

    public void putCharArray(T key, char[] value) {
        mMap.put(key, value);
    }

    public void putShortArray(T key, short[] value) {
        mMap.put(key, value);
    }

    public void putIntArray(T key, int[] value) {
        mMap.put(key, value);
    }

    public void putLongArray(T key, long[] value) {
        mMap.put(key, value);
    }

    public void putFloatArray(T key, float[] value) {
        mMap.put(key, value);
    }

    public void putDoubleArray(T key, double[] value) {
        mMap.put(key, value);
    }

    public void putStringArray(T key, String[] value) {
        mMap.put(key, value);
    }

    public void putCharSequenceArray(T key, CharSequence[] value) {
        mMap.put(key, value);
    }

    //--------------put for object--------------
    public void put(T key, Object value) {
        mMap.put(key, value);
    }

    //----------------put for map---------------
    public void putAll(Pack<T> pack) {
        mMap.putAll(pack.mMap);
    }

    public boolean contains(T key) {
        return mMap.containsKey(key);
    }

    public void clear() {
        mMap.clear();
    }

    //------------------------------------------
    // Log a message if the value was non-null but not of the expected type
    @SuppressWarnings("StringBufferReplaceableByString")
    private void typeWarning(T key, Object value, String className,
                             Object defaultValue, ClassCastException e) {
        StringBuilder sb = new StringBuilder();
        sb.append("Key ");
        sb.append(key);
        sb.append(" expected ");
        sb.append(className);
        sb.append(" but value was a ");
        sb.append(value.getClass().getName());
        sb.append(".  The default value ");
        sb.append(defaultValue);
        sb.append(" was returned.");
        Log.w(LOG_TAG, sb.toString());
        Log.w(LOG_TAG, "Attempt to cast generated internal exception:", e);
    }

    private void typeWarning(T key, Object value, String className,
                             ClassCastException e) {
        typeWarning(key, value, className, "<null>", e);
    }
}
