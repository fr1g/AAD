package moe.vot.own.projs.aad.pr.navi.contactbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContentListRenderAdapter extends RecyclerView.Adapter<ContentListRenderAdapter.ViewHolder>{

    private List<Content> items;

    public ContentListRenderAdapter(List<Content> contents){
        this.items = contents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Content item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();// Math.toIntExact();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(Content item) {

            ((TextView)itemView.findViewById(R.id.value_nameField)).setText(item.name);
            ((TextView)itemView.findViewById(R.id.value_idField)).setText("#" + item.id);
            ((TextView)itemView.findViewById(R.id.value_emailField)).setText(item.email);
            ((TextView)itemView.findViewById(R.id.value_dateField)).setText(item.dateString);

        }
    }

}
