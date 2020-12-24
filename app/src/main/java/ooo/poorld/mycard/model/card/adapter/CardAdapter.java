package ooo.poorld.mycard.model.card.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ooo.poorld.mycard.R;
import ooo.poorld.mycard.common.CommonPopView;
import ooo.poorld.mycard.entity.Card;
import ooo.poorld.mycard.entity.FileData;
import ooo.poorld.mycard.model.card.CardInfoAct;

/**
 * author: teenyda
 * date: 2020/10/29
 * description:
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private Context mContext;
    private List<Card> mCards;
    private CommonPopView mCommonPopView;
    private CardDeletedListener mCardDeletedListener;

    public CardAdapter(Context context) {
        this.mContext = context;
        mCards = new ArrayList<>();
        mCommonPopView = new CommonPopView<Card>(context);
        mCommonPopView.setTitle("提示");
        mCommonPopView.setMessage("您确定要删除该卡片吗？");
        mCommonPopView.setLeftTitle("取消");
        mCommonPopView.setRightTitle("确定");
    }

    public interface CardDeletedListener{
        void onFileDeleted(Card card);
    }
    public void setCardDeletedListener(CardDeletedListener fileDeletedListener) {
        this.mCardDeletedListener = fileDeletedListener;
    }
    public void addAll(List<Card> cards) {
        mCards.clear();
        mCards.addAll(cards);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.activity_card_item, viewGroup, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Card card = mCards.get(i);
        viewHolder.card_name.setText(card.getCardName());
        String cardNumber = card.getCardNumber();
        int length = cardNumber.length();
        String num;
        if (length > 8) {
            num = cardNumber.substring(0, 3) + "********" + cardNumber.substring(length - 4, length - 1);
        } else {
            num = "****************";
        }
        viewHolder.card_num.setText(num);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardInfoAct.startActivity(mContext, card.getId());
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mCommonPopView.show(viewHolder.card_name, card);
                return true;
            }
        });

        mCommonPopView.setOnBtnClick(new CommonPopView.OnBtnClick<Card>() {
            @Override
            public void onLeftClick(Card fileData) {
                mCommonPopView.dismiss();
            }

            @Override
            public void onRightClick(Card fileData) {
                if (mCardDeletedListener != null) {
                    mCardDeletedListener.onFileDeleted(fileData);
                }
                mCommonPopView.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView card_name;
        TextView card_num;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card_name = itemView.findViewById(R.id.card_name);
            card_num = itemView.findViewById(R.id.card_num);

        }
    }
}
