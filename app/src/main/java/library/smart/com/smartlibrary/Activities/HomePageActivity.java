package library.smart.com.smartlibrary.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import library.smart.com.smartlibrary.Adapters.BookDetailsAdapter;
import library.smart.com.smartlibrary.Models.BooksDetailsGS;
import library.smart.com.smartlibrary.R;

public class HomePageActivity extends AppCompatActivity {

    private Spinner semesterSpinner, booksSpinner;
    private DatabaseReference mRef;
    private String sem = "", booksSelected = "";
    private String[] books;
    private RecyclerView recyclerView;
    private BookDetailsAdapter bookDetailsAdapter;
    private BooksDetailsGS booksDetailsGS;
    private List<BooksDetailsGS> booksDetailsGSList;
    private static String TAG = "HomePageActivity";
    private Button getDetailsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomePageActivity.this));
        semesterSpinner = findViewById(R.id.semester_spinner);
        booksSpinner = findViewById(R.id.books_spinner);
        getDetailsButton = findViewById(R.id.get_details_button);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.semesters, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semesterSpinner.setAdapter(adapter);

        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                sem = adapterView.getItemAtPosition(position).toString();
                Log.d(TAG, "Semester:" + sem);

                mRef = FirebaseDatabase.getInstance().getReference();
                mRef.child("Semester").child(sem).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int childCount = (int) dataSnapshot.getChildrenCount();
                        Log.d(TAG, "Child Count:" + childCount);
                        books = new String[childCount];
                        Log.d(TAG, "Count" + childCount);
                        for (int i = 0; i < childCount; i++) {
                            Log.d(TAG, "Subject" + (i + 1));
                            books[i] = dataSnapshot.child("Subject" + (i + 1)).getValue().toString();

                        }
                        if (books != null) {
                            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(HomePageActivity.this, android.R.layout.simple_spinner_dropdown_item, books);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            booksSpinner.setAdapter(adapter2);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        booksSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                booksSelected = adapterView.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (booksSelected != null && !booksSelected.equals("")) {
                    mRef.child("Books").child(booksSelected).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()) {
                                booksDetailsGSList = new ArrayList<>();
                                booksDetailsGS = new BooksDetailsGS();
                                bookDetailsAdapter = new BookDetailsAdapter(HomePageActivity.this, R.layout.books_details_card_view, booksDetailsGSList);
                                booksDetailsGS.setAuthorName(dataSnapshot.child("Author").getValue().toString());
                                booksDetailsGS.setNoOfCopies(dataSnapshot.child("No of Copies").getValue().toString());
                                booksDetailsGS.setSemester(dataSnapshot.child("Sem").getValue().toString());
                                booksDetailsGS.setTitle(booksSelected);
                                booksDetailsGS.setLocation(dataSnapshot.child("Location").child("1").getValue().toString());
                                booksDetailsGSList.add(booksDetailsGS);
                                recyclerView.setAdapter(bookDetailsAdapter);
                            } else {
                                Toast.makeText(HomePageActivity.this, "No Details Available Right Now", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });


    }
}
