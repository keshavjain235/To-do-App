package com.keshav.to_donotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference root;

    FloatingActionButton add;
//    String[] names = {"Captain Marvel","Hulk","Thor","Stark","Spider-Man","Groot","Rocket","Dr. Strange"};
    RecyclerView rc;
    EditText topic, desc;

    List<Todo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = findViewById(R.id.add);
        topic = findViewById(R.id.topic);
        desc = findViewById(R.id.desc);

        //init the recycler view object
        rc = findViewById(R.id.rc);
        rc.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        list = new ArrayList<>();
        database = FirebaseDatabase.getInstance();

        root = database.getReference("todo");

//        rc = findViewById(R.id.rc);
//        rc.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//        Adapter a = new Adapter(MainActivity.this,names);
//        rc.setAdapter(a);



//        root.child(key).setValue(new Todo("math","today i have to do chapter 12","not done"));

//        root.setValue(new Todo("math","today i have to do chapter 12","not done"));


        //receiving the data from server
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot d:dataSnapshot.getChildren()){

                    Todo t = d.getValue(Todo.class);
                    list.add(t);

                }

                rc.setAdapter(new Adapter(MainActivity.this,list));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String key = root.push().getKey();
                root.child(key).setValue(new Todo(topic.getText().toString(),desc.getText().toString(),"not done"))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            topic.setText("");
                            desc.setText("");
                            Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}