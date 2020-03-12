package com.onoffrice.marvel_comics.utils.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public class MediaGalleryPickerHelper implements PermissionDispatcherHelper.OnPermissionResult {

    public enum Type {
        IMAGE, VIDEO, BOTH, FILES
    }

    //ERROR CODES
    public static final int ERROR_PERMISSION_DENIED = 0;
    public static final int ERROR_PERMISSION_DENIED_WITH_NEVER_ASK_AGAIN = 1;
    public static final int ERROR_PHOTO_NOT_SELECTED = 2;
    public static final int ERROR_UNKNOWN = 3;
    //REQUEST CODES
    public static final int REQUEST_IMAGE_SELECTOR = 500;
    public static final int REQUEST_FILE_SELECTOR = 501;
    //PERMISSIONS
    private static final String[] STORAGE_PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private Activity mActivity;
    private Fragment mFragment;
    private Context mContext;
    private OnImageResultListener mOnImageResultListener;
    private PermissionDispatcherHelper mPermissionDispatcherHelper;
    private File mFile;
    private boolean mOnlyImage;
    private Type mSelectingType;

    public MediaGalleryPickerHelper(Activity activity, OnImageResultListener onImageResultListener, boolean onlyImage) {
        mOnImageResultListener = onImageResultListener;
        mActivity = activity;
        mContext = mActivity;
        mOnlyImage = onlyImage;
        mPermissionDispatcherHelper = new PermissionDispatcherHelper(activity, REQUEST_IMAGE_SELECTOR, STORAGE_PERMISSIONS, this);
    }

    public MediaGalleryPickerHelper(Fragment fragment, OnImageResultListener onImageResultListener, boolean onlyImage) {
        mOnImageResultListener = onImageResultListener;
        mFragment = fragment;
        mOnlyImage = onlyImage;
        mContext = mFragment.getContext();
        mPermissionDispatcherHelper = new PermissionDispatcherHelper(fragment, REQUEST_IMAGE_SELECTOR, STORAGE_PERMISSIONS, this);
    }

    public void startGalleryPicker(Type type) {
        //Dispatch permissions, gallery will open if the permissions are accepted
        mSelectingType = type;
        mPermissionDispatcherHelper.dispatchPermissions();
    }

    /**
     * Opens Gallery to select image.
     */
    private void dispatchPhotoSelectionIntent() {
        Intent galleryIntent;
        if (mSelectingType.equals(Type.IMAGE)) {
            galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");
        } else if (mSelectingType.equals(Type.VIDEO)) {
            galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("video/*");
        } else if (mSelectingType.equals(Type.FILES)) {
            galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            galleryIntent.setType("*/*");
        } else {
            galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*,video/*");
        }

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
                    mOnImageResultListener.onMediaFailed(ERROR_UNKNOWN, "Unknown error");
                } else {
                    String type = mContext.getContentResolver().getType(data.getData());
                    if (type.contains("image")) {
                        mOnImageResultListener.onMediaReady(mFile, true);
                    } else if (type.contains("video")) {
                        mOnImageResultListener.onMediaReady(mFile, false);
                    } else {
                        mOnImageResultListener.onMediaFailed(ERROR_UNKNOWN, "Unknown error");
                    }
                }
            } else {
                mOnImageResultListener.onMediaFailed(ERROR_UNKNOWN, "Unknown error");
            }
        } else {
            mOnImageResultListener.onMediaFailed(ERROR_PHOTO_NOT_SELECTED, "Image not selected");
        }
    }

    public static String writeToTempImageAndGetPath(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return path;
    }

    private File getSelectedFile(Intent data) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor;
        if (mActivity == null) {
            cursor = mFragment.getActivity().getContentResolver().query(data.getData(), filePathColumn, null, null, null);
        } else {
            cursor = mActivity.getContentResolver().query(data.getData(), filePathColumn, null, null, null);
        }
        if (cursor == null || cursor.getCount() < 1) {
            return null;
        }
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        if (columnIndex < 0) { // no column index
            return null;
        }
        String filePath = cursor.getString(columnIndex);
        if (filePath == null || filePath.isEmpty()) {
            return null;
        }
        File currentPhoto = new File(filePath);
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
        mOnImageResultListener.onMediaFailed(ERROR_PERMISSION_DENIED, "Permission denied.");
    }

    public interface OnImageResultListener {
        void onMediaReady(File file, boolean isImage);

        void onMediaFailed(int code, String msg);
    }
}

