package com.lv.test;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PermissionManager {
	
    private Object mObject;
    private String[] mPermissions;
    private int mRequestCode;
    private PermissionListener mListener;
    private boolean mIsPositive = false;

    public static PermissionManager with(Activity activity) {
        return new PermissionManager(activity);
    }

    public static PermissionManager with(Fragment fragment) {
        return new PermissionManager(fragment);
    }

    public PermissionManager permissions(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    public PermissionManager addRequestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    public PermissionManager setPermissionsListener(PermissionListener listener) {
        this.mListener = listener;
        return this;
    }

    public PermissionManager(Object object) {
        this.mObject = object;
    }

    public PermissionManager request() {
        request(mObject, mPermissions, mRequestCode);
        return this;
    }

    private void request(Object object, String[] permissions, int requestCode) {
        Map<String, List<String>> map = findDeniedPermissions(getActivity(object), permissions);
        List<String> deniedPermissions = map.get("deny");
        List<String> rationales = map.get("rationale");
        if (deniedPermissions.size() > 0) {
            if (rationales.size() > 0 && !mIsPositive) {
                if (mListener != null ) {
                    mListener.onShowRationale(rationales.toArray(new String[rationales.size()]));
                }
                return;
            }
            if (object instanceof Activity) {
                ActivityCompat.requestPermissions((Activity) object, deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else if (object instanceof Fragment) {
                ((Fragment) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else {
                throw new IllegalArgumentException(object.getClass().getName() + " is not supported");
            }
        } else {
            if (mListener != null) {
                mListener.onGranted();
            }
        }
    }


    
    private Map<String, List<String>> findDeniedPermissions(Activity activity, String... permissions) {
        Map<String, List<String>> map = new ArrayMap<>();
        List<String> denyList = new ArrayList<>();
        List<String> rationaleList = new ArrayList<>();
        for (String value : permissions) {
            if (ContextCompat.checkSelfPermission(activity, value) != PackageManager.PERMISSION_GRANTED) {
                denyList.add(value);
                if (shouldShowRequestPermissionRationale(value)) {
                    rationaleList.add(value);
                }
            }
        }
        map.put("deny", denyList);
        map.put("rationale", rationaleList);
        return map;
    }

    private Activity getActivity(Object object) {
        if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof Activity) {
            return (Activity) object;
        }
        return null;
    }

    private boolean shouldShowRequestPermissionRationale(String permission) {
        if (mObject instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) mObject, permission);
        } else if (mObject instanceof Fragment) {
            return ((Fragment) mObject).shouldShowRequestPermissionRationale(permission);
        } else {
            throw new IllegalArgumentException(mObject.getClass().getName() + " is not supported");
        }
    }

    public void setIsPositive(boolean isPositive) {
        this.mIsPositive = isPositive;
    }
}
