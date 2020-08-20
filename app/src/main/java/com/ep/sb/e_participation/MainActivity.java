package com.ep.sb.e_participation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private TextView username;
    private FirebaseUser fuser;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mDatabaseRef;
    private Toolbar toolbar;
    private DatabaseReference profileBusinessRef;
    private Button transport, food, service, social, other;
    RecyclerView mRecyclerView;
    private DatabaseReference mRef;
    private DatabaseReference msocial;
    private DatabaseReference mfood;
    private DatabaseReference mservice;
    private DatabaseReference mother;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);



        Button add_post = (Button) findViewById(R.id.add_post);
        transport = (Button) findViewById(R.id.transportation);
        food = (Button) findViewById(R.id.food);
        service = (Button) findViewById(R.id.services);
        social = (Button) findViewById(R.id.social);
        other = (Button) findViewById(R.id.other);

        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddPostActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        profileBusinessRef= FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid());
        profileBusinessRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){


                    username = (TextView) findViewById(R.id.username);
                    String myname = dataSnapshot.child("username").getValue().toString();
                    username.setText(myname);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mRef = mfirebaseDatabase.getReference().child("Transportation");
        msocial = mfirebaseDatabase.getReference().child("Social");
        mfood = mfirebaseDatabase.getReference().child("Food");
        mservice = mfirebaseDatabase.getReference().child("Services");
        mother = mfirebaseDatabase.getReference().child("Other");
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    final String userId = user.getUid();
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(userId)){

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {

                }
            }

        };

        mAuth.addAuthStateListener(mAuthListener);
        transport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String d = mRef.push().getKey();
                Query transpor = mRef.orderByChild("counter");
                FirebaseRecyclerAdapter<Vote, ViewHoldervote> firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<Vote, ViewHoldervote>(
                                Vote.class,
                                R.layout.rowvote,
                                ViewHoldervote.class,
                                transpor
                        ) {
                            @Override
                            protected void populateViewHolder(ViewHoldervote viewHolder, Vote vote, int position) {

                                viewHolder.setDetails(getApplicationContext().getApplicationContext(),vote.getDate(),vote.getUserid(),
                                        vote.getName(),vote.getDescription(),vote.getTitle(),vote.getVotednumber(),vote.getYes(),
                                        vote.getNo(),vote.getCounter());

                            }

                            @Override
                            public ViewHoldervote onCreateViewHolder(ViewGroup parent, int viewType) {

                                ViewHoldervote viewHolder = super.onCreateViewHolder(parent, viewType);


                                viewHolder.setOnClickListener(new ViewHoldervote.ClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        //Views


                                        //get data from views

                                        String mTitleName = getItem(position).getTitle();
                                        String mUserName =getItem(position).getName();
                                        long mYes = getItem(position).getYes();
                                        long mNo = getItem(position).getNo();
                                        long mVotedNumber = getItem(position).getVotednumber();
                                        String mDescription = getItem(position).getDescription();
                                        String mDate = getItem(position).getDate();
                                        String buttonname = "Transportation";

                                        //pass this data to new activity
                                        Intent intent = new Intent(view.getContext(),VoteDetailActivity.class);
                                        intent.putExtra("title", mTitleName);
                                        intent.putExtra("name",mUserName);
                                        intent.putExtra("yes", mYes);
                                        intent.putExtra("no", mNo);
                                        intent.putExtra("date", mDate);
                                        intent.putExtra("description", mDescription);
                                        intent.putExtra("votednumber", mVotedNumber);
                                        intent.putExtra("buttonname",buttonname);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onItemLongClick(View view, int position) {
                                        //TODO
                                    }
                                });

                                return viewHolder;
                            }
                        };

                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                food.setBackgroundColor(Color.TRANSPARENT);
                social.setBackgroundColor(Color.TRANSPARENT);
                other.setBackgroundColor(Color.TRANSPARENT);
                service.setBackgroundColor(Color.TRANSPARENT);
                transport.setBackgroundColor(getResources().getColor(R.color.butoncolor));

            }
        });
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String d = mRef.push().getKey();
                Query foo = mfood.orderByChild("counter");
                FirebaseRecyclerAdapter<Vote, ViewHoldervote> firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<Vote, ViewHoldervote>(
                                Vote.class,
                                R.layout.rowvote,
                                ViewHoldervote.class,
                                foo
                        ) {
                            @Override
                            protected void populateViewHolder(ViewHoldervote viewHolder, Vote vote, int position) {

                                viewHolder.setDetails(getApplicationContext().getApplicationContext(),vote.getDate(),vote.getUserid(),
                                        vote.getName(),vote.getDescription(),vote.getTitle(),vote.getVotednumber(),vote.getYes(),
                                        vote.getNo(),vote.getCounter()


                                );

                            }

                            @Override
                            public ViewHoldervote onCreateViewHolder(ViewGroup parent, int viewType) {

                                ViewHoldervote viewHolder = super.onCreateViewHolder(parent, viewType);


                                viewHolder.setOnClickListener(new ViewHoldervote.ClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        //Views


                                        //get data from views

                                        String mTitleName = getItem(position).getTitle();
                                        String mUserName =getItem(position).getName();
                                        long mYes = getItem(position).getYes();
                                        long mNo = getItem(position).getNo();
                                        long mVotedNumber = getItem(position).getVotednumber();
                                        String mDescription = getItem(position).getDescription();
                                        String mDate = getItem(position).getDate();
                                        String buttonname = "Food";

                                        //pass this data to new activity
                                        Intent intent = new Intent(view.getContext(),VoteDetailActivity.class);
                                        intent.putExtra("title", mTitleName);
                                        intent.putExtra("name",mUserName);
                                        intent.putExtra("yes", mYes);
                                        intent.putExtra("no", mNo);
                                        intent.putExtra("date", mDate);
                                        intent.putExtra("description", mDescription);
                                        intent.putExtra("votednumber", mVotedNumber);
                                        intent.putExtra("buttonname",buttonname);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onItemLongClick(View view, int position) {
                                        //TODO
                                    }
                                });

                                return viewHolder;
                            }
                        };

                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                transport.setBackgroundColor(Color.TRANSPARENT);
                social.setBackgroundColor(Color.TRANSPARENT);
                other.setBackgroundColor(Color.TRANSPARENT);
                service.setBackgroundColor(Color.TRANSPARENT);
                food.setBackgroundColor(getResources().getColor(R.color.butoncolor));

            }
        });
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String d = mRef.push().getKey();
                Query serv = mservice.orderByChild("counter");
                FirebaseRecyclerAdapter<Vote, ViewHoldervote> firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<Vote, ViewHoldervote>(
                                Vote.class,
                                R.layout.rowvote,
                                ViewHoldervote.class,
                                serv
                        ) {
                            @Override
                            protected void populateViewHolder(ViewHoldervote viewHolder, Vote vote, int position) {

                                viewHolder.setDetails(getApplicationContext().getApplicationContext(),vote.getDate(),vote.getUserid(),
                                        vote.getName(),vote.getDescription(),vote.getTitle(),vote.getVotednumber(),vote.getYes(),
                                        vote.getNo(),vote.getCounter()


                                );

                            }

                            @Override
                            public ViewHoldervote onCreateViewHolder(ViewGroup parent, int viewType) {

                                ViewHoldervote viewHolder = super.onCreateViewHolder(parent, viewType);


                                viewHolder.setOnClickListener(new ViewHoldervote.ClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        //Views


                                        //get data from views

                                        String mTitleName = getItem(position).getTitle();
                                        String mUserName =getItem(position).getName();
                                        long mYes = getItem(position).getYes();
                                        long mNo = getItem(position).getNo();
                                        long mVotedNumber = getItem(position).getVotednumber();
                                        String mDescription = getItem(position).getDescription();
                                        String mDate = getItem(position).getDate();
                                        String buttonname = "Services";

                                        //pass this data to new activity
                                        Intent intent = new Intent(view.getContext(),VoteDetailActivity.class);
                                        intent.putExtra("title", mTitleName);
                                        intent.putExtra("name",mUserName);
                                        intent.putExtra("yes", mYes);
                                        intent.putExtra("no", mNo);
                                        intent.putExtra("date", mDate);
                                        intent.putExtra("description", mDescription);
                                        intent.putExtra("votednumber", mVotedNumber);
                                        intent.putExtra("buttonname",buttonname);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onItemLongClick(View view, int position) {
                                        //TODO
                                    }
                                });

                                return viewHolder;
                            }
                        };

                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                food.setBackgroundColor(Color.TRANSPARENT);
                social.setBackgroundColor(Color.TRANSPARENT);
                other.setBackgroundColor(Color.TRANSPARENT);
                transport.setBackgroundColor(Color.TRANSPARENT);
                service.setBackgroundColor(getResources().getColor(R.color.butoncolor));

            }
        });

        social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String d = mRef.push().getKey();
                Query soci = msocial.orderByChild("counter");
                FirebaseRecyclerAdapter<Vote, ViewHoldervote> firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<Vote, ViewHoldervote>(
                                Vote.class,
                                R.layout.rowvote,
                                ViewHoldervote.class,
                                soci
                        ) {
                            @Override
                            protected void populateViewHolder(ViewHoldervote viewHolder, Vote vote, int position) {

                                viewHolder.setDetails(getApplicationContext().getApplicationContext(),vote.getDate(),vote.getUserid(),
                                        vote.getName(),vote.getDescription(),vote.getTitle(),vote.getVotednumber(),vote.getYes(),
                                        vote.getNo(),vote.getCounter()


                                );

                            }

                            @Override
                            public ViewHoldervote onCreateViewHolder(ViewGroup parent, int viewType) {

                                ViewHoldervote viewHolder = super.onCreateViewHolder(parent, viewType);


                                viewHolder.setOnClickListener(new ViewHoldervote.ClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        //Views


                                        //get data from views

                                        String mTitleName = getItem(position).getTitle();
                                        String mUserName =getItem(position).getName();
                                        long mYes = getItem(position).getYes();
                                        long mNo = getItem(position).getNo();
                                        long mVotedNumber = getItem(position).getVotednumber();
                                        String mDescription = getItem(position).getDescription();
                                        String mDate = getItem(position).getDate();
                                        String buttonname = "Social";

                                        //pass this data to new activity
                                        Intent intent = new Intent(view.getContext(),VoteDetailActivity.class);
                                        intent.putExtra("title", mTitleName);
                                        intent.putExtra("name",mUserName);
                                        intent.putExtra("yes", mYes);
                                        intent.putExtra("no", mNo);
                                        intent.putExtra("date", mDate);
                                        intent.putExtra("description", mDescription);
                                        intent.putExtra("votednumber", mVotedNumber);
                                        intent.putExtra("buttonname",buttonname);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onItemLongClick(View view, int position) {
                                        //TODO
                                    }
                                });

                                return viewHolder;
                            }
                        };

                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                food.setBackgroundColor(Color.TRANSPARENT);
                transport.setBackgroundColor(Color.TRANSPARENT);
                other.setBackgroundColor(Color.TRANSPARENT);
                service.setBackgroundColor(Color.TRANSPARENT);
                social.setBackgroundColor(getResources().getColor(R.color.butoncolor));

            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String d = mRef.push().getKey();
                Query oth = mother.orderByChild("counter");
                FirebaseRecyclerAdapter<Vote, ViewHoldervote> firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<Vote, ViewHoldervote>(
                                Vote.class,
                                R.layout.rowvote,
                                ViewHoldervote.class,
                                oth
                        ) {
                            @Override
                            protected void populateViewHolder(ViewHoldervote viewHolder, Vote vote, int position) {

                                viewHolder.setDetails(getApplicationContext().getApplicationContext(),vote.getDate(),vote.getUserid(),
                                        vote.getName(),vote.getDescription(),vote.getTitle(),vote.getVotednumber(),vote.getYes(),
                                        vote.getNo(),vote.getCounter()


                                );

                            }

                            @Override
                            public ViewHoldervote onCreateViewHolder(ViewGroup parent, int viewType) {

                                ViewHoldervote viewHolder = super.onCreateViewHolder(parent, viewType);


                                viewHolder.setOnClickListener(new ViewHoldervote.ClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        //Views


                                        //get data from views

                                        String mTitleName = getItem(position).getTitle();
                                        String mUserName =getItem(position).getName();
                                        long mYes = getItem(position).getYes();
                                        long mNo = getItem(position).getNo();
                                        long mVotedNumber = getItem(position).getVotednumber();
                                        String mDescription = getItem(position).getDescription();
                                        String mDate = getItem(position).getDate();
                                        String buttonname = "Other";

                                        //pass this data to new activity
                                        Intent intent = new Intent(view.getContext(),VoteDetailActivity.class);
                                        intent.putExtra("title", mTitleName);
                                        intent.putExtra("name",mUserName);
                                        intent.putExtra("yes", mYes);
                                        intent.putExtra("no", mNo);
                                        intent.putExtra("date", mDate);
                                        intent.putExtra("description", mDescription);
                                        intent.putExtra("votednumber", mVotedNumber);
                                        intent.putExtra("buttonname",buttonname);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onItemLongClick(View view, int position) {
                                        //TODO
                                    }
                                });

                                return viewHolder;
                            }
                        };

                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                food.setBackgroundColor(Color.TRANSPARENT);
                social.setBackgroundColor(Color.TRANSPARENT);
                transport.setBackgroundColor(Color.TRANSPARENT);
                service.setBackgroundColor(Color.TRANSPARENT);
                other.setBackgroundColor(getResources().getColor(R.color.butoncolor));

            }
        });




    }



    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        Query transpor = mRef.orderByChild("counter");
        FirebaseRecyclerAdapter<Vote, ViewHoldervote> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Vote, ViewHoldervote>(
                        Vote.class,
                        R.layout.rowvote,
                        ViewHoldervote.class,
                        transpor
                ) {
                    @Override
                    protected void populateViewHolder(ViewHoldervote viewHolder, Vote vote, int position) {

                        viewHolder.setDetails(getApplicationContext().getApplicationContext(),vote.getDate(),vote.getUserid(),
                                vote.getName(),vote.getDescription(),vote.getTitle(),vote.getVotednumber(),vote.getYes(),
                                vote.getNo(),vote.getCounter()


                        );

                    }

                    @Override
                    public ViewHoldervote onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHoldervote viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHoldervote.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //Views


                                //get data from views

                                String mTitleName = getItem(position).getTitle();
                                String mUserName =getItem(position).getName();
                                long mYes = getItem(position).getYes();
                                long mNo = getItem(position).getNo();
                                long mVotedNumber = getItem(position).getVotednumber();
                                String mDescription = getItem(position).getDescription();
                                String mDate = getItem(position).getDate();
                                String buttonname = "Transportation";

                                //pass this data to new activity
                                Intent intent = new Intent(view.getContext(),VoteDetailActivity.class);
                                intent.putExtra("title", mTitleName);
                                intent.putExtra("name",mUserName);
                                intent.putExtra("yes", mYes);
                                intent.putExtra("no", mNo);
                                intent.putExtra("date", mDate);
                                intent.putExtra("description", mDescription);
                                intent.putExtra("votednumber", mVotedNumber);
                                intent.putExtra("buttonname",buttonname);
                                startActivity(intent);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                //TODO
                            }
                        });

                        return viewHolder;
                    }
                };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        food.setBackgroundColor(Color.TRANSPARENT);
        social.setBackgroundColor(Color.TRANSPARENT);
        other.setBackgroundColor(Color.TRANSPARENT);
        service.setBackgroundColor(Color.TRANSPARENT);
        transport.setBackgroundColor(getResources().getColor(R.color.butoncolor));

    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.logmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, FirstActivity.class));
                break;
        }
        return true;
    }
}
