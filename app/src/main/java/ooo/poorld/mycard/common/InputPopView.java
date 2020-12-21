package ooo.poorld.mycard.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import ooo.poorld.mycard.R;
import ooo.poorld.mycard.entity.FileData;


public class InputPopView {

    private Context mContext;
    private PopupWindow mPopupWindow;
    private View mView;

    private OnBtnClick mOnBtnClick;

    public InputPopView(Context context) {
        mContext = context;

        initPopView();
    }

    public interface OnBtnClick{
        void onLeftClick(String input);

        void onRightClick(String input);
    }

    private void initPopView() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.pop_input, null, false);
        mPopupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    public InputPopView setOnBtnClick(OnBtnClick onBtnClick) {
        mOnBtnClick = onBtnClick;

        mView.findViewById(R.id.rl_pop_left)
                .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = getInputEditText().getText().toString();
                    mOnBtnClick.onLeftClick(s);
                }
            });
        mView.findViewById(R.id.rl_pop_right)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s = getInputEditText().getText().toString();
                        mOnBtnClick.onRightClick(s);
                    }
                });

        return this;

    }

    public InputPopView setTitle(String title) {
        TextView leftTv = mView.findViewById(R.id.tv_pop_title);
        leftTv.setText(title);
        return this;
    }

    public InputPopView setText(String msg) {
        getInputEditText().setText(msg);
        return this;
    }

    public InputPopView setHint(String msg) {
        getInputEditText().setHint(msg);
        return this;
    }

    public InputPopView setLeftTitle(String title) {
        TextView leftTv = mView.findViewById(R.id.tv_pop_left_title);
        leftTv.setText(title);
        return this;
    }

    public InputPopView setRightTitle(String title) {
        TextView leftTv = mView.findViewById(R.id.tv_pop_right_title);
        leftTv.setText(title);
        return this;
    }

    private EditText getInputEditText() {
        return mView.findViewById(R.id.et_input_text);
    }



    public void show(View v) {
        // setFocusable 默认是false
        // setFocusable(true)外部和内部都会响应，点击外部就会取消，setOutsideTouchable(false)失效
        mPopupWindow.setFocusable(true);
        // 设置PopupWindow是否能响应点击事件
        mPopupWindow.setTouchable(true);
        // 设置PopupWindow内容区域外的区域是否响应点击事件（true：响应；false：不响应）
        mPopupWindow.setOutsideTouchable(false);

        mPopupWindow.showAtLocation(v, Gravity.CENTER, 0 , 0);
        backgroundAlpha(0.5f);
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }
}
