package com.ep.sb.e_participation;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class AddPostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Button addpost_button;
    EditText postdescription,titlename;
    private TextView nameofuser;
    DatabaseReference reference;
    private FirebaseUser fuser;
    private DatabaseReference PostsRef;
    private ProgressDialog progressDialog;
    private long countPosts =0;
    DatabaseReference databaseReference;
    private TextView timeof;
    private Spinner dtlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        dtlist = findViewById(R.id.dtlistname);
        postdescription = findViewById(R.id.postdescription);
        postdescription.setMovementMethod(new ScrollingMovementMethod());
        addpost_button = (Button) findViewById(R.id.addpost_button);
        nameofuser = (TextView) findViewById(R.id.nameofuser);
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        timeof = findViewById(R.id.timeof);
        timeof.setText(currentDate);
        titlename = (EditText) findViewById(R.id.titlename);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.lists,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dtlist.setAdapter(adapter);
        dtlist.setOnItemSelectedListener(this);
        String as= String.valueOf(dtlist.getSelectedItem().toString());
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        PostsRef = FirebaseDatabase.getInstance().getReference().child(as);
        PostsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    countPosts= dataSnapshot.getChildrenCount();

                } else {
                    countPosts = 0;
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    User user = dataSnapshot.getValue(User.class);
                    String myIdentity = dataSnapshot.child("username").getValue().toString();
                    nameofuser.setText(myIdentity);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        progressDialog = new ProgressDialog(this);


        addpost_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                String as= String.valueOf(dtlist.getSelectedItem().toString());
                PostsRef = FirebaseDatabase.getInstance().getReference().child(as);
                PostsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            countPosts = dataSnapshot.getChildrenCount();

                        } else {
                            countPosts = 0;
                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });

                String s = String.valueOf(dtlist.getSelectedItem());
                Query query = FirebaseDatabase.getInstance().getReference().child(s).orderByChild("title")
                        .equalTo(String.valueOf(titlename.getText()));

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            Toast.makeText(AddPostActivity.this, "Choose a different title name", Toast.LENGTH_SHORT).show();
                        }else {
                            if (String.valueOf(titlename.getText()).equals("")) {
                                Toast.makeText(AddPostActivity.this, "Please type a title", Toast.LENGTH_SHORT).show();
                            }else if (String.valueOf(titlename.getText()).contains(".")) {
                                Toast.makeText(AddPostActivity.this, "You should use *,* instead of *.*", Toast.LENGTH_SHORT).show();
                            }else if (String.valueOf(postdescription.getText()).equals("")) {
                                Toast.makeText(AddPostActivity.this, "You should fill post description", Toast.LENGTH_SHORT).show();
                            } else if (String.valueOf(titlename.getText()).contains("#")) {
                                Toast.makeText(AddPostActivity.this, "You can't use special characters in the title", Toast.LENGTH_SHORT).show();
                            } else if (String.valueOf(titlename.getText()).contains("$")) {
                                Toast.makeText(AddPostActivity.this, "You can't use special characters in the title", Toast.LENGTH_SHORT).show();
                            } else if (String.valueOf(titlename.getText()).contains("[")) {
                                Toast.makeText(AddPostActivity.this, "You can't use special characters in the title", Toast.LENGTH_SHORT).show();
                            } else if (String.valueOf(titlename.getText()).contains("]")) {
                                Toast.makeText(AddPostActivity.this, "You can't use special characters in the title", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.setMessage("Post is Loading, Please Wait...........");
                                progressDialog.show();
                                String as = String.valueOf(dtlist.getSelectedItem().toString());
                                PostsRef = FirebaseDatabase.getInstance().getReference().child(as);
                                PostsRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            countPosts = dataSnapshot.getChildrenCount();

                                        } else {
                                            countPosts = 0;
                                        }

                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }

                                });
                                final String spin = String.valueOf(dtlist.getSelectedItem().toString());
                                final long voted = 0;
                                final long yes = 0;
                                final long no = 0;

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                HashMap hashMp = new HashMap();

                                hashMp.put("name", String.valueOf(nameofuser.getText()));
                                hashMp.put("title", String.valueOf(titlename.getText()));
                                hashMp.put("counter", countPosts);
                                hashMp.put("description", postdescription.getText().toString());
                                hashMp.put("date", timeof.getText().toString());
                                hashMp.put("votednumber",voted);
                                hashMp.put("userid", fuser.getUid());
                                hashMp.put("yes", yes);
                                hashMp.put("no",no);
                                databaseReference.child(spin).child(String.valueOf(titlename.getText())).setValue(hashMp);
                                Toast.makeText(AddPostActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                addpost_button.setVisibility(View.GONE);
                                progressDialog.dismiss();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
