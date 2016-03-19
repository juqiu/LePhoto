package com.little.framework.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 *
 */
public class FileUtils {

    /**
     * Comparator of files.
     */
    public interface FileComparator {
        public boolean equals(File lhs, File rhs);
    }

    /**
     * Simple file comparator which only depends on file length and modification time.
     */
    public final static FileComparator SIMPLE_COMPARATOR = new FileComparator() {
        @Override
        public boolean equals(File lhs, File rhs) {
            return (lhs.length() == rhs.length()) && (lhs.lastModified() == rhs.lastModified());
        }
    };

    /**
     * Strict file comparator which depends on file md5.
     */
    public final static FileComparator STRICT_COMPARATOR = new FileComparator() {
        @Override
        public boolean equals(File lhs, File rhs) {
            String lhsMd5 = SecurityUtils.encrypt(lhs);
            if (lhsMd5 == null) {
                return false;
            }
            String rhsMd5 = SecurityUtils.encrypt(rhs);
            return lhsMd5.equals(rhsMd5);
        }
    };

    /**
     * Comparator of asset and target file.
     */
    public interface AssetFileComparator {
        public boolean equals(Context context, String assetPath, File dstFile);
    }

    /**
     * Simple asset file comparator which only depends on asset file length.
     */
    public final static AssetFileComparator SIMPLE_ASSET_COMPARATOR = new AssetFileComparator() {
        @Override
        public boolean equals(Context context, String assetPath, File dstFile) {
            long assetFileLength = getAssetLength(context, assetPath);
            return assetFileLength != -1 && assetFileLength == dstFile.length();
        }
    };

    private final static String TAG = "FileUtils";

    private final static int ASSET_SPLIT_BASE = 0;

    private final static int BUFFER_SIZE = 8192;

    /**
     * Copy files. If src is a directory, then all it's sub files will be copied into directory dst.
     * If src is a file, then it will be copied to file dst. Notice, a {@link #SIMPLE_COMPARATOR} is used.
     *
     * @param src file or directory to copy.
     * @param dst destination file or directory.
     * @return true if copy complete perfectly, false otherwise (more than one file cannot be copied).
     */
    public static boolean copyFiles(File src, File dst) {
        return copyFiles(src, dst, null);
    }

    /**
     * Copy files. If src is a directory, then all it's sub files will be copied into directory dst.
     * If src is a file, then it will be copied to file dst. Notice, a {@link #SIMPLE_COMPARATOR} is used.
     *
     * @param src    file or directory to copy.
     * @param dst    destination file or directory.
     * @param filter a file filter to determine whether or not copy corresponding file.
     * @return true if copy complete perfectly, false otherwise (more than one file cannot be copied).
     */
    public static boolean copyFiles(File src, File dst, FileFilter filter) {
        return copyFiles(src, dst, filter, SIMPLE_COMPARATOR);
    }

    /**
     * Copy files. If src is a directory, then all it's sub files will be copied into directory dst.
     * If src is a file, then it will be copied to file dst.
     *
     * @param src        file or directory to copy.
     * @param dst        destination file or directory.
     * @param filter     a file filter to determine whether or not copy corresponding file.
     * @param comparator a file comparator to determine whether src & dst are equal files. Null to overwrite all dst files.
     * @return true if copy complete perfectly, false otherwise (more than one file cannot be copied).
     */
    public static boolean copyFiles(File src, File dst, FileFilter filter, FileComparator comparator) {
        if (src == null || dst == null) {
            return false;
        }

        if (!src.exists()) {
            return false;
        }
        if (src.isFile()) {
            return performCopyFile(src, dst, filter, comparator);
        }

        File[] paths = src.listFiles();
        if (paths == null) {
            return false;
        }
        // default is true.
        boolean result = true;
        for (File sub : paths) {
            if (!copyFiles(sub, new File(dst, sub.getName()), filter)) {
                result = false;
            }
        }
        return result;
    }

    private static boolean performCopyFile(File srcFile, File dstFile, FileFilter filter, FileComparator comparator) {
        if (srcFile == null || dstFile == null) {
            return false;
        }
        if (filter != null && !filter.accept(srcFile)) {
            return false;
        }

        FileChannel inc = null;
        FileChannel ouc = null;
        try {
            if (!srcFile.exists() || !srcFile.isFile()) {
                return false;
            }

            if (dstFile.exists()) {
                if (comparator != null && comparator.equals(srcFile, dstFile)) {
                    // equal files.
                    return true;
                } else {
                    // delete it in case of folder.
                    delete(dstFile);
                }
            }

            File toParent = dstFile.getParentFile();
            if (toParent.isFile()) {
                delete(toParent);
            }
            if (!toParent.exists() && !toParent.mkdirs()) {
                return false;
            }

            inc = (new FileInputStream(srcFile)).getChannel();
            ouc = (new FileOutputStream(dstFile)).getChannel();

            ouc.transferFrom(inc, 0, inc.size());

        } catch (Throwable e) {
            Log.i(TAG, "fail to copy file", e);
            // exception occur, delete broken file.
            delete(dstFile);
            return false;
        } finally {
            closeSilently(inc);
            closeSilently(ouc);
        }
        return true;
    }

    /**
     * Copy file corresponding stream to des file. The source input stream will be left open.
     *
     * @param source source input stream.
     * @param dst    destination file.
     * @return true if copy complete perfectly, false otherwise.
     */
    public static boolean copyFile(InputStream source, File dst) {
        return copyFile(source, dst, false);
    }

    /**
     * Copy file corresponding stream to des file.
     *
     * @param source          source input stream.
     * @param dst             destination file.
     * @param closeWhenFinish whether closeWhenFinish source input stream when operation finished.
     * @return true if copy complete perfectly, false otherwise.
     */
    public static boolean copyFile(InputStream source, File dst, boolean closeWhenFinish) {
        if (source == null || dst == null) {
            return false;
        }
        OutputStream ous = null;
        try {
            ous = new BufferedOutputStream(new FileOutputStream(dst), BUFFER_SIZE);
            return performCopyStream(source, ous);

        } catch (Throwable e) {
            Log.i(TAG, "fail to copy file", e);
        } finally {
            if (closeWhenFinish) {
                closeSilently(source);
            }
            closeSilently(ous);
        }
        return false;
    }

    private static boolean performCopyStream(InputStream ins, OutputStream ous) {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int count;
            while ((count = ins.read(buffer)) > 0) {
                ous.write(buffer, 0, count);
            }
            return true;

        } catch (Throwable e) {
            Log.i(TAG, "fail to copy stream", e);
        }
        return false;
    }

    /**
     * Copy asset files. If assetName is a file, the it will be copied to file dst. Notice, a {@link #SIMPLE_ASSET_COMPARATOR} is used.
     *
     * @param context   application context.
     * @param assetName asset name to copy.
     * @param dst       destination file.
     */
    public static boolean copyAssets(Context context, String assetName, String dst) {
        return copyAssets(context, assetName, dst, SIMPLE_ASSET_COMPARATOR);
    }

    /**
     * Copy asset files. If assetName is a file, the it will be copied to file dst.
     *
     * @param context    application context.
     * @param assetName  asset name to copy.
     * @param dst        destination file.
     * @param comparator a asset file comparator to determine whether asset & dst are equal files. Null to overwrite all dst files.
     */
    public static boolean copyAssets(Context context, String assetName, String dst, AssetFileComparator comparator) {
        return performCopyAssetsFile(context, assetName, dst, comparator);
    }

    private static boolean performCopyAssetsFile(Context context, String assetPath, String dstPath, AssetFileComparator comparator) {
        if (isEmpty(assetPath) || isEmpty(dstPath)) {
            return false;
        }

        AssetManager assetManager = context.getAssets();
        File dstFile = new File(dstPath);

        boolean succeed = false;
        InputStream in = null;
        OutputStream out = null;
        try {
            if (dstFile.exists()) {
                if (comparator != null && comparator.equals(context, assetPath, dstFile)) {
                    return true;
                } else {
                    // file will be overwrite later.
                    if (dstFile.isDirectory()) {
                        delete(dstFile);
                    }
                }
            }

            File parent = dstFile.getParentFile();
            if (parent.isFile()) {
                delete(parent);
            }
            if (!parent.exists() && !parent.mkdirs()) {
                return false;
            }

            in = assetManager.open(assetPath);
            out = new BufferedOutputStream(new FileOutputStream(dstFile), BUFFER_SIZE);
            byte[] buf = new byte[BUFFER_SIZE];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            succeed = true;

        } catch (Throwable e) {
            Log.i(TAG, "fail to copy assets file", e);
            // delete broken file.
            delete(dstFile);
        } finally {
            closeSilently(in);
            closeSilently(out);
        }
        return succeed;
    }

    public static long getAssetLength(Context context, String assetPath) {
        AssetManager assetManager = context.getAssets();
        // try to determine whether or not copy this asset file, using their size.
        try {
            AssetFileDescriptor fd = assetManager.openFd(assetPath);
            return fd.getLength();

        } catch (IOException e) {
            // this file is compressed. cannot determine it's size.
        }

        // try stream.
        InputStream tmpIn = null;
        try {
            tmpIn = assetManager.open(assetPath);
            return tmpIn.available();

        } catch (IOException e) {
            // do nothing.
        } finally {
            closeSilently(tmpIn);
        }
        return -1;
    }

    /**
     * Delete corresponding path, file or directory.
     *
     * @param file path to delete.
     */
    public static void delete(File file) {
        delete(file, false);
    }

    /**
     * Delete corresponding path, file or directory.
     *
     * @param file      path to delete.
     * @param ignoreDir whether ignore directory. If true, all files will be deleted while directories is reserved.
     */
    public static void delete(File file, boolean ignoreDir) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }

        File[] fileList = file.listFiles();
        if (fileList == null) {
            return;
        }

        for (File f : fileList) {
            delete(f, ignoreDir);
        }
        // delete the folder if need.
        if (!ignoreDir) file.delete();
    }

    private static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable e) {
                // empty.
            }
        }
    }

    private static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
