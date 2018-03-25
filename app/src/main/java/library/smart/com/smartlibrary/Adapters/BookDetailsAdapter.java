package library.smart.com.smartlibrary.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import library.smart.com.smartlibrary.Models.BooksDetailsGS;
import library.smart.com.smartlibrary.R;

/**
 * Created by HP on 11-02-2018.
 */

public class BookDetailsAdapter extends RecyclerView.Adapter<BookDetailsAdapter.ViewHolder> {
    private List<BooksDetailsGS> booksDetailsGS;
    private Context context;
    private LayoutInflater inflater;
    private int resource;

    public BookDetailsAdapter(Context context, int resource, List<BooksDetailsGS> booksDetailsGS) {
        this.context = context;
        this.resource = resource;
        this.booksDetailsGS = booksDetailsGS;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public BookDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.books_details_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookDetailsAdapter.ViewHolder holder, int position) {
        BooksDetailsGS booksDetailsGSList = booksDetailsGS.get(position);
        holder.title.setText(booksDetailsGSList.getTitle());
        holder.authorName.setText(booksDetailsGSList.getAuthorName());
        holder.semester.setText(booksDetailsGSList.getSemester());
        holder.noOfCopies.setText(booksDetailsGSList.getNoOfCopies());
        holder.location.setText(booksDetailsGSList.getLocation());
    }

    @Override
    public int getItemCount() {
        return booksDetailsGS.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public TextView title, authorName, semester, location, noOfCopies;
        public CardView detailsCard;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            title = view.findViewById(R.id.book_title_card_view);
            authorName = view.findViewById(R.id.author_name_card_view);
            semester = view.findViewById(R.id.semester_card_view);
            location = view.findViewById(R.id.location_card_view);
            noOfCopies = view.findViewById(R.id.no_of_copies_card_view);


        }
    }
}
