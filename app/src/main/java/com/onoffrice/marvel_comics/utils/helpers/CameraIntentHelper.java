package com.onoffrice.marvel_comics.utils.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;

import com.onoffrice.marvel_comics.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CameraIntentHelper implements PermissionDispatcherHelper.OnPermissionResult {
    //ERROR CODES
    public static final int ERROR_PERMISSION_DENIED = 0;
    public static final int ERROR_PERMISSION_DENIED_WITH_NEVER_ASK_AGAIN = 1;
    public static final int ERROR_PHOTO_NOT_TAKEN = 2;
    public static final int ERROR_UNKNOWN = 3;
    //REQUEST CODES
    public static final int REQUEST_TAKE_PICTURE = 600;
    //PERMISSIONS
    private static final String[] STORAGE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
    private String AUTHORITY = Constants.PACKAGE_NAME + ".fileprovider";
    private Activity mActivity;
    private Fragment mFragment;
    private OnCameraResultListener mOnImageResultListener;
    private PermissionDispatcherHelper mPermissionDispatcherHelper;
    private File mFile;
    private boolean mOnlyImage;

    public CameraIntentHelper(Activity activity, OnCameraResultListener onImageResultListener, boolean onlyImg) {
        mOnImageResultListener = onImageResultListener;
        mActivity = activity;
        mOnlyImage = onlyImg;
        mPermissionDispatcherHelper = new PermissionDispatcherHelper(activity, REQUEST_TAKE_PICTURE, STORAGE_PERMISSIONS, this);
    }

    public CameraIntentHelper(Fragment fragment, OnCameraResultListener onImageResultListener, boolean onlyImg) {
        mOnImageResultListener = onImageResultListener;
        mFragment = fragment;
        mOnlyImage = onlyImg;
        mActivity = fragment.getActivity();
        mPermissionDispatcherHelper = new PermissionDispatcherHelper(fragment, REQUEST_TAKE_PICTURE, STORAGE_PERMISSIONS, this);
    }

    public void startCamera() {
        //Dispatch permissions, gallery will open if the permissions are accepted
        mPermissionDispatcherHelper.dispatchPermissions();
    }

    /**
     * Open camera
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent;
        takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            // Create the File where the photo should go
            mFile = null;
            try {
                mFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                mOnImageResultListener.onMediaFailed(ERROR_UNKNOWN, ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (mFile != null) {
                Uri photoURI = FileProvider.getUriForFile(mActivity,
                        AUTHORITY,
                        mFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);

                //COMPATIBILITY TO DEVICES WITH ANDROID VERSION LOWER THAN 5 (VERSION 5IS INCLUDED IN THE CONDITIION BLOCK BECAUSE
                //IT'S POSSIBLE TO FIND LOLIPOP DEVICES THAT ALSO NEED COMPATIBILITY
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    takePictureIntent.setClipData(ClipData.newRawUri("", photoURI));
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }

                if (mFragment == null) {
                    mActivity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);
                } else {
                    mFragment.startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);
                }
            } else {
                mOnImageResultListener.onMediaFailed(ERROR_UNKNOWN, "PHOTO NOT TAKEN");
                boolean isFileDeleted = mFile.delete();
            }
        } else {
            mOnImageResultListener.onMediaFailed(ERROR_UNKNOWN, "Not possible to open camera intent.");
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TAKE_PICTURE) {
            mOnImageResultListener.onMediaReady(mFile, !isVideoFile(mFile.getPath()));
        } else if(requestCode == REQUEST_TAKE_PICTURE) {
            mOnImageResultListener.onMediaFailed(ERROR_PHOTO_NOT_TAKEN, "Image not selected");
        }
    }

    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        mPermissionDispatcherHelper.onRequestPermissionsResult(permsRequestCode, permissions, grantResults);
    }

    @Override
    public void onAllPermissionsGranted(int requestCode) {
        dispatchTakePictureIntent();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NotNull List<String> deniedPermissions, @NotNull List<String> deniedPermissionsWithNeverAskAgainOption) {
        if(!deniedPermissionsWithNeverAskAgainOption.isEmpty()) {
            mOnImageResultListener.onMediaFailed(ERROR_PERMISSION_DENIED_WITH_NEVER_ASK_AGAIN, "Permission denied with never again option.");
        } else if(!deniedPermissions.isEmpty()) {
            mOnImageResultListener.onMediaFailed(ERROR_PERMISSION_DENIED, "Permission denied.");
        } else {
            mOnImageResultListener.onMediaFailed(ERROR_UNKNOWN, "On permission dispatch error.");
        }
    }

    public interface OnCameraResultListener {
        void onMediaReady(File file, boolean isImage);

        void onMediaFailed(int code, String msg);
    }
}

