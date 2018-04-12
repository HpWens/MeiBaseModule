package com.meis.base.mei.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;

import com.meis.base.R;
import com.trello.rxlifecycle2.components.support.RxAppCompatDialogFragment;

/**
 * author: ws4
 * created on: 2018/4/11 14:06
 * description:
 */
public class CompatDialog extends RxAppCompatDialogFragment {

    protected final String TAG = getClass().getSimpleName();

    private boolean mImmersive;

    private DialogInterface.OnDismissListener mOnDismissListener;

    public <T extends View> T findViewById(@IdRes int id) {
        return (T) getView().findViewById(id);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (mImmersive) {
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
        return dialog;
    }

    protected void setFullScreen() {
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MeiBaseDialog);
    }

    public void show(FragmentActivity activity) {
        activity.getSupportFragmentManager().beginTransaction().add(this, "dialog_" + toString())
                .commitAllowingStateLoss();
    }

    /**
     * 沉浸模式下调用这个方法
     *
     * @param activity
     */
    public void showImmersive(FragmentActivity activity) {
        try {
            mImmersive = true;
            activity.getSupportFragmentManager().beginTransaction().add(this, "dialog_" +
                    toString())
                    .commitNowAllowingStateLoss();
            getDialog().getWindow().getDecorView().setSystemUiVisibility(
                    getActivity().getWindow().getDecorView().getSystemUiVisibility()
            );
            getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss(dialog);
        }
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

    @Override
    public void dismiss() {
        dismissAllowingStateLoss();
    }

    @UiThread
    protected void finishActivity() {
        try {
            ActivityCompat.finishAfterTransition(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
