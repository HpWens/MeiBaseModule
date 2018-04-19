package com.meis.base.mei.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.view.WindowManager;

import com.meis.base.R;

/**
 * desc:
 * author: ws
 * date: 2018/4/19.
 */

public class MeiCompatDialogDelegate {
    private IMeiCompatDialog mMeiCompatDialog;
    private AppCompatDialogFragment mFragment;

    private boolean mImmersive = false;

    private DialogInterface.OnDismissListener mOnDismissListener;

    public MeiCompatDialogDelegate(IMeiCompatDialog meiCompatDialog) {
        if (!(meiCompatDialog instanceof AppCompatDialogFragment)) {
            throw new RuntimeException("Must extends Fragment");
        }
        mMeiCompatDialog = meiCompatDialog;
        mFragment = (AppCompatDialogFragment) meiCompatDialog;
    }

    public Dialog onCreateDialog(Dialog dialog) {
        if (mImmersive) {
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }
        return dialog;
    }


    public <T extends View> T findViewById(@IdRes int id) {
        return (T) mFragment.getView().findViewById(id);
    }

    /**
     * 沉浸模式下调用这个方法
     *
     * @param activity
     */
    public void showImmersive(FragmentActivity activity) {
        try {
            mImmersive = true;
            activity.getSupportFragmentManager().beginTransaction().add(mFragment, "dialog_" + mFragment.getClass()
                    .getSimpleName()).commitNowAllowingStateLoss();
            mFragment.getDialog().getWindow().getDecorView().setSystemUiVisibility(
                    mFragment.getActivity().getWindow().getDecorView().getSystemUiVisibility()
            );
            mFragment.getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setFullScreen() {
        mFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MeiBaseDialog);
    }

    public void onDismiss(DialogInterface dialog) {
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss(dialog);
        }
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

    public void dismiss() {
        mFragment.dismissAllowingStateLoss();
    }

}
