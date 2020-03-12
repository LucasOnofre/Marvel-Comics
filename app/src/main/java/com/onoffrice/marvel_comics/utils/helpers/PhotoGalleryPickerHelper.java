package com.onoffrice.marvel_comics.utils.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.provider.MediaStore;

import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PhotoGalleryPickerHelper implements PermissionDispatcherHelper.OnPermissionResult {
    //ERROR CODES
    public static final int ERROR_PERMISSION_DENIED = 0;
    public static final int ERROR_PERMISSION_DENIED_WITH_NEVER_ASK_AGAIN = 1;
    public static final int ERROR_PHOTO_NOT_SELECTED = 2;
    public static final int ERROR_UNKNOWN = 3;

    //PERMISSIONS
    private static final String[] STORAGE_PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};

    //REQUEST CODES
    public static final int REQUEST_IMAGE_SELECTOR = 500;

    private Activity mActivity;
    private Fragment mFragment;
    private OnImageResultListener mOnImageResultListener;
    private PermissionDispatcherHelper mPermissionDispatcherHelper;
    private File mFile;
    private int mImageMaxSizeInPixels;

    public PhotoGalleryPickerHelper(Activity activity, int imageMaxSizeInPixels, OnImageResultListener onImageResultListener) {
        mOnImageResultListener = onImageResultListener;
        mActivity = activity;
        mImageMaxSizeInPixels = imageMaxSizeInPixels;
        mPermissionDispatcherHelper = new PermissionDispatcherHelper(activity, REQUEST_IMAGE_SELECTOR, STORAGE_PERMISSIONS, this);
    }

    public PhotoGalleryPickerHelper(Fragment fragment, int imageMaxSizeInPixels, OnImageResultListener onImageResultListener) {
        mOnImageResultListener = onImageResultListener;
        mFragment = fragment;
        mImageMaxSizeInPixels = imageMaxSizeInPixels;
        mPermissionDispatcherHelper = new PermissionDispatcherHelper(fragment, REQUEST_IMAGE_SELECTOR, STORAGE_PERMISSIONS, this);
    }

    public void startGalleryPicker() {
        //Dispatch permissions, gallery will open if the permissions are accepted
        mPermissionDispatcherHelper.dispatchPermissions();
    }

    /**
     * Opens Gallery to select image.
     */
    private void dispatchPhotoSelectionIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");

        if (mFragment == null) {
            mActivity.startActivityForResult(galleryIntent, REQUEST_IMAGE_SELECTOR);
        } else {
            mFragment.startActivityForResult(galleryIntent, REQUEST_IMAGE_SELECTOR);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_SELECTOR) {
            if (data != null && data.getData() != null) {
                mFile = getSelectedFile(data);
                if (mFile == null) {
                    mOnImageResultListener.onImageFailed(ERROR_UNKNOWN, "Unknown error");
                } else {
                    Bitmap bm;
//
//                    try {
//                        bm = compressFile(mFile);
//                    } catch (Exception e) {
//                        e.printStackTrace();
                    bm = BitmapFactory.decodeFile(mFile.getAbsolutePath());
//                    }
////
//                    try {
//                        File f = new File(mActivity.getCacheDir(), "resize.jpg");
//                        f.createNewFile();
//
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//
//                        byte[] bitmapdata = baos.toByteArray();
//
//                        FileOutputStream fos = new FileOutputStream(f);
//                        fos.write(bitmapdata);
//                        fos.flush();
//                        fos.close();
//
//                        mFile = f;

                    try {
                        mOnImageResultListener.onImageReady(mFile, compressFile(mFile));
                    } catch (IOException e) {
                        mOnImageResultListener.onImageFailed(ERROR_UNKNOWN, "Unknown error");
                    }
//                    } catch (IOException e) {
//                        mOnImageResultListener.onImageFailed(ERROR_UNKNOWN, "");
//                        e.printStackTrace();
//                    }
                }
            } else {
                mOnImageResultListener.onImageFailed(ERROR_UNKNOWN, "Unknown error");
            }
        } else {
            mOnImageResultListener.onImageFailed(ERROR_PHOTO_NOT_SELECTED, "Image not selected");
        }
    }

    private Bitmap compressFile(File file) throws IOException {
        Matrix matrix = PhotoUtil.rotateImageBasedOnExifData(file.getPath());
        Bitmap bpm = PhotoUtil.rescaleImage(file.getPath(), mImageMaxSizeInPixels);
        return Bitmap.createBitmap(bpm, 0, 0, bpm.getWidth(), bpm.getHeight(), matrix, true);
    }

    private File getSelectedFile(Intent data) {
        File currentPhoto = null;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor;
        if(mActivity == null) {
            cursor = mFragment.getActivity().getContentResolver().query(data.getData(), filePathColumn, null, null, null);
        } else {
            cursor = mActivity.getContentResolver().query(data.getData(), filePathColumn, null, null, null);
        }
        if (cursor == null || cursor.getCount() < 1) {
            return currentPhoto;
        }
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        if (columnIndex < 0) { // no column index
            return currentPhoto;
        }
        currentPhoto = new File(cursor.getString(columnIndex));
        cursor.close();
        return currentPhoto;
    }

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        mPermissionDispatcherHelper.onRequestPermissionsResult(permsRequestCode, permissions, grantResults);
    }

    @Override
    public void onAllPermissionsGranted(int requestCode) {
        dispatchPhotoSelectionIntent();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NotNull List<String> deniedPermissions, @NotNull List<String> deniedPermissionsWithNeverAskAgainOption) {
        if(!deniedPermissionsWithNeverAskAgainOption.isEmpty()) {
            mOnImageResultListener.onImageFailed(ERROR_PERMISSION_DENIED_WITH_NEVER_ASK_AGAIN, "Permission denied with never again option.");
        } else if(!deniedPermissions.isEmpty()) {
            mOnImageResultListener.onImageFailed(ERROR_PERMISSION_DENIED, "Permission denied.");
        } else {
            mOnImageResultListener.onImageFailed(ERROR_UNKNOWN, "On permission dispatch error.");
        }
    }

    public interface OnImageResultListener {
        void onImageReady(File file, Bitmap bitmap);

        void onImageFailed(int code, String msg);
    }
}