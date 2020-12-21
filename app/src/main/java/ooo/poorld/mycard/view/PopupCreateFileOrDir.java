package ooo.poorld.mycard.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import ooo.poorld.mycard.R;


/**
 * author: teenyda
 * date: 2019/8/22
 * description:
 */
public class PopupCreateFileOrDir {

    private Context mContext;
    private PopupWindow mPopupWindow;
    private View mView;
    private CreateListener mGetPhotoListener;

    public PopupCreateFileOrDir(Context context) {
        mContext = context;
        initPopup();
    }

    public interface CreateListener{
        void onCreateDir();

        void onCreateFile();
    }

    public void setCreateListener(CreateListener getPhotoListener) {
        mGetPhotoListener = getPhotoListener;
    }

    private void initPopup() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.popup_crate_file_dir, null);
        mPopupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setAnimationStyle(R.style.AnimationBottomInAndOut);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(1f);
            }
        });

        mView.findViewById(R.id.create_dir_rl)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mGetPhotoListener != null) {
                            mGetPhotoListener.onCreateDir();
                        }
                    }
                });
        mView.findViewById(R.id.create_file_rl)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mGetPhotoListener != null) {
                            mGetPhotoListener.onCreateFile();
                        }
                    }
                });
        mView.findViewById(R.id.photo_cancel_rl)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
    }

    public void show(View view) {
        mPopupWindow.setFocusable(false);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        setAlpha(0.5f);
    }

    public void dismiss() {
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    private void setAlpha(float alpha) {
        WindowManager.LayoutParams layoutParams = ((Activity) mContext).getWindow().getAttributes();
        layoutParams.alpha = alpha;
        ((Activity) mContext).getWindow().setAttributes(layoutParams);
    }

}
