package com.example.roomsample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.NotesViewHolder> implements Filterable {
    private ArrayList<NoteEntity> noteEntityArrayList;
    private INoteClick iNoteClick;
    private IItemCheck iItemCheck;
    private ArrayList<NoteEntity> noteEntityArrayListFiltered;

    public void setiNoteClick(INoteClick iNoteClick) {
        this.iNoteClick = iNoteClick;
    }

    public void setiItemCheck(IItemCheck iItemCheck){
        this.iItemCheck = iItemCheck;
    }

    public NotesRecyclerAdapter(ArrayList<NoteEntity> noteEntityArrayList) {
        this.noteEntityArrayList = noteEntityArrayList;
        this.noteEntityArrayListFiltered = noteEntityArrayList;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.txtDescription.setText(noteEntityArrayListFiltered.get(position).getDescription());
        holder.txtTitle.setText(noteEntityArrayListFiltered.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return noteEntityArrayListFiltered.size();
    }

    public void setNoteEntityArrayList(ArrayList<NoteEntity> noteEntityArrayList) {
        this.noteEntityArrayList = noteEntityArrayList;
        this.noteEntityArrayListFiltered = noteEntityArrayList;
    }



    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String keyword = charSequence.toString();
                if (keyword.isEmpty()){
                    noteEntityArrayListFiltered =noteEntityArrayList;
                } else{
                    ArrayList<NoteEntity> noteEntities = new ArrayList<>();
                    for(NoteEntity row: noteEntityArrayList){
                        if (row.getTitle().toLowerCase().contains(keyword.toLowerCase()) || row.getDescription().toLowerCase().contains(keyword.toLowerCase())){
                            noteEntities.add(row);
                        }
                    }
                        noteEntityArrayListFiltered = noteEntities;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = noteEntityArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                noteEntityArrayListFiltered = (ArrayList<NoteEntity>) filterResults.values;
                if (noteEntityArrayListFiltered.size()>0){
                    iItemCheck.hasItems(true);
                } else{
                    iItemCheck.hasItems(false);
                }
                notifyDataSetChanged();
            }
        };
    }

    class NotesViewHolder extends RecyclerView.ViewHolder{
    private TextView txtTitle, txtDescription;
        public NotesViewHolder(@NonNull final View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iNoteClick.noteClickListener(noteEntityArrayListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    interface INoteClick{
        void noteClickListener(NoteEntity noteEntity);
    }
    interface IItemCheck{
        void hasItems(Boolean result);
    }
}
