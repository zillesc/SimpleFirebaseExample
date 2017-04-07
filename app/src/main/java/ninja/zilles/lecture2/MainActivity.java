package ninja.zilles.lecture2;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private ActionBar mSupportActionBar;
    private DatabaseReference mTitleReference;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mZillesCommentsReference;
    private RecyclerView mRecyclerView;
    private DatabaseReference angraveReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // for getting access to the Title text
        mSupportActionBar = getSupportActionBar();

        // Get a reference to the Firebase database, and portions of that database.
        mDatabase = FirebaseDatabase.getInstance();
        mTitleReference = mDatabase.getReference("title");
        mZillesCommentsReference = mDatabase.getReference("zilles/comment");
        angraveReference = mDatabase.getReference("angrave");

        // demonstration that you can just push objects into firebase database
        final ObjectExample angraveObject = new ObjectExample("has a cool accent", 11, 5 / 7.0);
        angraveReference.push().setValue(angraveObject);

        // continuously update title based on String value in real-time database
        mTitleReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String title = dataSnapshot.getValue(String.class);
                mSupportActionBar.setTitle(title);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Use the Fab to pop up a dialog that asks for a String and append that String
        // to an array in the Firebase database
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Build a dialog from layout XML
                final Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.dialog);
                dialog.setTitle("What is your comment?");

                // Get a reference to the EditText; note that this is final so that we can
                // use it when we create ClickListener below knowing that it won't change
                final EditText editText = (EditText) dialog.findViewById(R.id.editText);

                final Button button = (Button) dialog.findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Grab the contents of the EditText.
                        final String comment = editText.getText().toString();

                        // Push creates a new node in the database, and then we set its value.
                        mZillesCommentsReference.push().setValue(comment);
                        dialog.dismiss();
                    }
                });
                dialog.show();


//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        // Similar to the RecyclerAdapter that you've already seen, but you can just point it
        // to an array in the Firebase database.
        final FirebaseRecyclerAdapter<String, CommentViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<String, CommentViewHolder>(String.class, R.layout.comment,
                        CommentViewHolder.class, mZillesCommentsReference) {
                    @Override
                    protected void populateViewHolder(CommentViewHolder viewHolder, String model, int position) {
                        viewHolder.mTextView.setText(model);
                    }
                };

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Just like the ViewHolders you had to write previously.
    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public CommentViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.textView);
        }
    }
}
