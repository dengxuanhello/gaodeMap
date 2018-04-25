package com.example.dx.gaodemap.permisions;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Delegate class to make permission calls based on the 'host' (Fragment, Activity, etc).
 */
//@RestrictTo(RestrictTo.Scope.LIBRARY)

public class PermissionHelper {
    private static final String TAG = "PermissionHelper";

    private Object mHost;

    @NonNull
    public static PermissionHelper newInstance(Object host) {
        return new PermissionHelper(host);
    }
    /*
    @NonNull
    public static PermissionHelper newInstance(Activity host) {
        if (Build.VERSION.SDK_INT < 23) {
            return new LowApiPermissionsHelper(host);
        }

        return new ActivityPermissionHelper(host);
    }

    @NonNull
    public static PermissionHelper newInstance(Fragment host) {
        if (Build.VERSION.SDK_INT < 23) {
            return new LowApiPermissionsHelper(host);
        }

        return new SupportFragmentPermissionHelper(host);
    }

    @NonNull
    public static PermissionHelper newInstance(android.app.Fragment host) {
        if (Build.VERSION.SDK_INT < 23) {
            return new LowApiPermissionsHelper(host);
        }

        return new FrameworkFragmentPermissionHelper(host);
    }
    */

    // ============================================================================
    // Public concrete methods
    // ============================================================================

    public PermissionHelper(@NonNull Object host) {
        mHost = host;
    }

    public boolean shouldShowRationale(@NonNull String... perms) {
        for (String perm : perms) {
            if (shouldShowRequestPermissionRationale(perm)) {
                return true;
            }
        }
        return false;
    }

    public void requestPermissions(@NonNull String rationale,
                                   @StringRes int positiveButton,
                                   @StringRes int negativeButton,
                                   int requestCode,
                                   @NonNull String... perms) {
//        if (shouldShowRationale(perms)) {
//            showRequestPermissionRationale(
//                    rationale, positiveButton, negativeButton, requestCode, perms);
//        } else {
            directRequestPermissions(requestCode, perms);
//        }
    }

    public boolean somePermissionPermanentlyDenied(@NonNull List<String> perms) {
        for (String deniedPermission : perms) {
            if (permissionPermanentlyDenied(deniedPermission)) {
                return true;
            }
        }

        return false;
    }

    public boolean permissionPermanentlyDenied(@NonNull String perms) {
        return !shouldShowRequestPermissionRationale(perms);
    }

    public boolean somePermissionDenied(@NonNull String... perms) {
        return shouldShowRationale(perms);
    }

    @NonNull
    public Object getHost() {
        return mHost;
    }

    // ============================================================================
    // Public abstract methods
    // ============================================================================

    public void directRequestPermissions(int requestCode, @NonNull String... perms) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (mHost instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) mHost, perms, requestCode);
        } else if (mHost instanceof Fragment) {
            ((Fragment) mHost).requestPermissions(perms, requestCode);
        } else if (mHost instanceof android.app.Fragment) {
            ((android.app.Fragment) mHost).requestPermissions(perms, requestCode);
        }
    }

    public boolean shouldShowRequestPermissionRationale(@NonNull String perm) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        if (mHost instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) mHost, perm);
        } else if (mHost instanceof Fragment) {
            return ((Fragment) mHost).shouldShowRequestPermissionRationale(perm);
        } else if (mHost instanceof android.app.Fragment) {
            return ((android.app.Fragment) mHost).shouldShowRequestPermissionRationale(perm);
        }
        return  false;
    }

    public void showRequestPermissionRationale(@NonNull String rationale,
                                                        @StringRes int positiveButton,
                                                        @StringRes int negativeButton,
                                                        int requestCode,
                                                        @NonNull String... perms) {

    }

    public Context getContext() {
        if (mHost instanceof Activity) {
            return (Activity) mHost;
        } else if (mHost instanceof Fragment) {
            return ((Fragment) mHost).getActivity();
        } else if (mHost instanceof android.app.Fragment) {
            return ((android.app.Fragment) mHost).getActivity();
        }
        return null;
    }
}
