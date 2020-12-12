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
import ooo.poorld.mycard.entity.Card;
import ooo.poorld.mycard.model.card.CardInfoAct;

/**
 * author: teenyda
 * date: 2020/10/29
 * description:
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private Context mContext;
    private List<Card> mCards;

    public CardAdapter(Context context) {
        this.mContext = context;
        mCards = new ArrayList<>();
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
