package com.robosolutions.temipatrol.google;

import android.content.Context;

import com.google.api.services.drive.Drive;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriveServiceHelper {
    private final String TAG = "DrievServiceHelper";
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private final Drive mDriveService;
    private Context context;

    public DriveServiceHelper(Drive mDriveService) {
        this.mDriveService = mDriveService;
    }

//    public DriveServiceHelper getInstanceFor(Context context) {
//        if (INSTANCE == null) {
//            Log.e(TAG, "DriveServiceHelper has not been initialized");
//            Toast.makeText(context, "Error: DriveServiceHelper has not been initialized",
//                    Toast.LENGTH_LONG).show();
//        }
//        INSTANCE.setContext(context);
//        return INSTANCE;
//
//    }
//
//    /**
//     * Returns an {@link Intent} for opening the Storage Access Framework file picker.
//     */
//    public Intent createImgPickerIntent() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        return intent;
//    }
//
//
//    /**
//     * Opens the file at the {@code uri} returned by a Storage Access Framework {@link Intent}
//     * created by {@link #createImgPickerIntent()} using the given {@code contentResolver}.
//     */
//    public Task<ImageAction> downloadImgUsingStorageAccessFramework(
//            ContentResolver contentResolver, Uri uri) {
//        return Tasks.call(mExecutor, () -> {
//            // Retrieve document's display name from metadata
//            try {
//                String name;
//                Cursor cursor = contentResolver.query(uri, null, null, null, null);
//                if (cursor != null && cursor.moveToFirst()) {
//                    int nameIdx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
//                    name = cursor.getString(nameIdx);
//                } else {
//                    throw new IOException("Empty cursor returned for file");
//                }
//                InputStream is = contentResolver.openInputStream(uri);
//                String outputPath = context.getExternalFilesDir(Values.IMAGE_DIR).getPath() + "/" + name;
//                Log.i(TAG, "current path: " + outputPath);
//                File newImgFile = new File(outputPath);
//                if (!newImgFile.exists()) {
//                    newImgFile.getParentFile().mkdirs();
//                    newImgFile.createNewFile();
//                }
//
//                byte[] inputData = getBytes(is);
//                writeFile(inputData, outputPath);
//
//                int THUMB_SIZE = 64;
//                Bitmap bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(newImgFile.getAbsolutePath()),
//                        THUMB_SIZE, THUMB_SIZE);
//                ImageAction action = new ImageAction(outputPath, bitmap, -1, name);
//
//                return action;
//
//            }catch (Exception e) {
//                Log.e(TAG, e.toString());
//                return null;
//            }
//        });
//    }
//
//    /**
//     * Returns an {@link Intent} for opening the Storage Access Framework file picker.
//     */
//    public Intent createVidPickerIntent() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("video/*");
//        return intent;
//    }
//
//
//    /**
//     * Opens the file at the {@code uri} returned by a Storage Access Framework {@link Intent}
//     * created by {@link #createImgPickerIntent()} using the given {@code contentResolver}.
//     */
//    public Task<Pair<String, String>> downloadVidUsingStorageAccessFramework(
//            ContentResolver contentResolver, Uri uri) {
//        return Tasks.call(mExecutor, () -> {
//            // Retrieve document's display name from metadata
//            try {
//                String name;
//                Cursor cursor = contentResolver.query(uri, null, null, null, null);
//                if (cursor != null && cursor.moveToFirst()) {
//                    int nameIdx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
//                    name = cursor.getString(nameIdx);
//                } else {
//                    throw new IOException("Empty cursor returned for file");
//                }
//                InputStream is = contentResolver.openInputStream(uri);
//                String outputPath = context.getExternalFilesDir(Values.VIDEO_DIR).getPath() + "/" + name;
//                Log.i(TAG, "current path: " + outputPath);
//                File newVidFile = new File(outputPath);
//                if (!newVidFile.exists()) {
//                    newVidFile.getParentFile().mkdirs();
//                    newVidFile.createNewFile();
//                }
//
//                byte[] inputData = getBytes(is);
//                writeFile(inputData, outputPath);
//
//                return Pair.create(name, outputPath);
//
//            }catch (Exception e) {
//                Log.e(TAG, e.toString());
//                return null;
//            }
//        });
//    }
//
//    /** This function puts everything in the provided InputStream into a byte array
//     * and returns it to the calling function. */
//    public byte[] getBytes(InputStream inputStream) throws IOException {
//
//        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
//        int bufferSize = 1024;
//        byte[] buffer = new byte[bufferSize];
//
//        int len = 0;
//
//        while ((len = inputStream.read(buffer)) != -1) {
//            byteBuffer.write(buffer, 0, len);
//        }
//
//        return byteBuffer.toByteArray();
//    }
//
//    /**   This function rewrites the byte array to the provided filename.
//     *
//     * Note: A String, NOT a file object, though you could easily tweak it to do
//     * that. */
//    public void writeFile(byte[] data, String fileName) throws IOException{
//        FileOutputStream out = new FileOutputStream(fileName);
//        out.write(data);
//        out.close();
//    }
//
//    public void setContext(Context context) {
//        this.context = context;
//    }
}