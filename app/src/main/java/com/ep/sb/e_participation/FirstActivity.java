package com.ep.sb.e_participation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirstActivity extends AppCompatActivity {
    private DatabaseReference userDR;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private Button transport, food, service, social, other;
    RecyclerView mRecyclerView;
    private DatabaseReference mRef;
    private DatabaseReference msocial;
    private DatabaseReference mfood;
    private DatabaseReference mservice;
    private DatabaseReference mother;
    FirebaseDatabase mfirebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Button loginbutton = (Button) findViewById(R.id.loginbutton);
        transport = (Button) findViewById(R.id.transportation);
        food = (Button) findViewById(R.id.food);
        service = (Button) findViewById(R.id.services);
        social = (Button) findViewById(R.id.social);
        other = (Button) findViewById(R.id.other);
        auth = FirebaseAuth.getInstance();
        mfirebaseDatabase = FirebaseDatabase.getInstance();
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

                //check users
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if( user!=null)
                {
                    final String userId = user.getUid();
                    userDR = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                    userDR.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Intent moveToHome = new Intent(FirstActivity.this, MainActivity.class);
                                moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);;
                                startActivity(moveToHome);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        };
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstActivity.this,LoginActivity.class));
            }
        });
        auth.addAuthStateListener(mAuthListener);
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

                                        Toast.makeText(FirstActivity.this, "You must log-in the system to vote", Toast.LENGTH_SHORT).show();
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

                                        Toast.makeText(FirstActivity.this, "You must log-in the system to vote", Toast.LENGTH_SHORT).show();
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

                                        Toast.makeText(FirstActivity.this, "You must log-in the system to vote", Toast.LENGTH_SHORT).show();
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

                                        Toast.makeText(FirstActivity.this, "You must log-in the system to vote", Toast.LENGTH_SHORT).show();
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

                                        Toast.makeText(FirstActivity.this, "You must log-in the system to vote", Toast.LENGTH_SHORT).show();
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
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        auth.addAuthStateListener(mAuthListener);
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
                                Toast.makeText(FirstActivity.this, "You must log-in the system to vote", Toast.LENGTH_SHORT).show();
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
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(mAuthListener);
    }
}
