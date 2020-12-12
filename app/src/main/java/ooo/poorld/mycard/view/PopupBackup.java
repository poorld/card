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

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ooo.poorld.mycard.R;
import ooo.poorld.mycard.entity.Upload;
import ooo.poorld.mycard.model.myself.adapter.BackupAdapter;


/**
 * author: teenyda
 * date: 2019/8/22
 * description:
 */
public class PopupBackup {

    private Context mContext;
    private PopupWindow mPopupWindow;
    private View mView;
    private ItemClickListener mItemClickListener;
    private RecyclerView mRecyclerView;
    private BackupAdapter mBackupAdapter;

    public PopupBackup(Context context) {
        mContext = context;
        initPopup();
    }

    public interface ItemClickListener{
        void onItemClick(Upload upload);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        mBackupAdapter.setItemClickListener(itemClickListener);
    }

    public void addUploadDatas(List<Upload> datas) {
        mBackupAdapter.addList(datas);
    }

    private void initPopup() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.popup_backup, null);
        mPopupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setAnimationStyle(R.style.AnimationBottomInAndOut);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(1f);
            }
        });

        mRecyclerView = mView.findViewById(R.id.rv);
        mBackupAdapter = new BackupAdapter(mContext);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mBackupAdapter);
    }

    public void show(View view) {
        mPopupWindow.setFocusable(true);
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
