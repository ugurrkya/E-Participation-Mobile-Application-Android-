package com.ep.sb.e_participation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class VoteDetailActivity extends AppCompatActivity implements View.OnClickListener {
private TextView username,titlename,datehere,desc;
private Button yesbtn, nobtn;
private String buttonname;
private Long yes, no, votednumber;
private DatabaseReference mButtoncontrol;
    FirebaseDatabase mfirebaseDatabase;
    private FirebaseUser fuser;
    private TextView alr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_detail);

        alr = (TextView) findViewById(R.id.already);
        username = (TextView)findViewById(R.id.dtnameofuser);
        titlename = (TextView)findViewById(R.id.fortitle);
        datehere =  (TextView)findViewById(R.id.fordate);
        desc =  (TextView)findViewById(R.id.fordescription);
        buttonname = getIntent().getStringExtra("buttonname");
        String titlee = getIntent().getStringExtra("title");
        String descr = getIntent().getStringExtra("description");
        String name = getIntent().getStringExtra("name");
        String date = getIntent().getStringExtra("date");
        yes = getIntent().getLongExtra("yes",0);
        no = getIntent().getLongExtra("no",0);
        votednumber = getIntent().getLongExtra("votednumber",0);
        username.setText(name);
        titlename.setText(titlee);
        datehere.setText(date);
        desc.setText(descr);
        yesbtn = (Button)findViewById(R.id.yesbutton);
        nobtn = (Button) findViewById(R.id.nobutton);
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        yesbtn.setOnClickListener(this);
        nobtn.setOnClickListener(this);
        mButtoncontrol = FirebaseDatabase.getInstance().getReference().child("Votes").child(buttonname).child(fuser.getUid()+titlename.getText());
        mButtoncontrol.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    alr.setVisibility(View.VISIBLE);
                    yesbtn.setVisibility(View.GONE);
                    nobtn.setVisibility(View.GONE);


                }else{
                    alr.setVisibility(View.GONE);
                    yesbtn.setVisibility(View.VISIBLE);
                    nobtn.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.yesbutton:
                final DatabaseReference yesf;
                final DatabaseReference foryes;
                foryes = FirebaseDatabase.getInstance().getReference().child(buttonname).child(String.valueOf(titlename.getText()));
                yesf = FirebaseDatabase.getInstance().getReference();
                Query query = FirebaseDatabase.getInstance().getReference().child("Votes")
                        .child(buttonname).child(fuser.getUid()+titlename.getText());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            Toast.makeText(VoteDetailActivity.this, "You already voted", Toast.LENGTH_SHORT).show();
                        }else {
                            HashMap hashMap = new HashMap();
                            hashMap.put("title", fuser.getUid()+titlename.getText());
                            hashMap.put("userid", fuser.getUid());
                            yesf.child("Votes").child(buttonname).child(fuser.getUid()+titlename.getText()).setValue(hashMap);
                            HashMap hash = new HashMap();
                            hash.put("yes",yes+1);
                            hash.put("votednumber",votednumber+1);
                            foryes.updateChildren(hash);
                            Toast.makeText(VoteDetailActivity.this, "Voted as Yes", Toast.LENGTH_SHORT).show();
                        }}
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                break;
            case R.id.nobutton:
                final DatabaseReference nof;
                final DatabaseReference forno;
                forno = FirebaseDatabase.getInstance().getReference().child(buttonname).child(String.valueOf(titlename.getText()));
                nof = FirebaseDatabase.getInstance().getReference();
                Query queryy = FirebaseDatabase.getInstance().getReference().child("Votes")
                        .child(buttonname).child(fuser.getUid()+titlename.getText());
                queryy.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            Toast.makeText(VoteDetailActivity.this, "You already voted", Toast.LENGTH_SHORT).show();
                        }else {
                            HashMap hashMap = new HashMap();
                            hashMap.put("title", fuser.getUid()+titlename.getText());
                            hashMap.put("userid", fuser.getUid());
                            nof.child("Votes").child(buttonname).child(fuser.getUid()+titlename.getText()).setValue(hashMap);
                            HashMap hash = new HashMap();
                            hash.put("no",no+1);
                            hash.put("votednumber",votednumber+1);
                            forno.updateChildren(hash);
                            Toast.makeText(VoteDetailActivity.this, "Voted as No", Toast.LENGTH_SHORT).show();
                        }}
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;

        }
    }
}
