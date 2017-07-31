package com.zhangqie.ormlite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangqie.ormlite.dao1.UserDao;
import com.zhangqie.ormlite.entity.User;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView textView;
    UserDao userDao;
    int ni=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        textView= (TextView) findViewById(R.id.text);
        userDao=new UserDao(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
                testAddUser();
                break;
            case R.id.btn2:
                testDeleteUser();


                break;
            case R.id.btn3:
                testUpdateUser();


                break;
            case R.id.btn4:
                testList();
                break;
            case  R.id.btn5:
                startActivity(new Intent(this,Test.class));
                break;
        }
    }



    public void testAddUser()
    {
            User u1 = new User("zhangqie"+ni++, "2B青年");
            userDao.add(u1);
            testList();
    }

    public void testDeleteUser()
    {
        int is=  userDao.delete(1);
        Toast.makeText(getApplicationContext(),is+"",Toast.LENGTH_LONG).show();
        testList();
    }

    public void testUpdateUser()
    {
        User u1 = new User("zhangqie-android", "2B青年");
        u1.setId(1);
        int is= userDao.update(u1);
        Toast.makeText(getApplicationContext(),is+"",Toast.LENGTH_LONG).show();
        testList();
    }

    public void testList()
    {
        List<User> users = userDao.query();
        String username="";
        for (int i=0;i<users.size();i++){
            username+=+users.get(i).getId()+"-----"+users.get(i).getName()+"\n";
        }
        textView.setText(username);
    }


    public void m(List<User> users){
        String username="";
        for (int i=0;i<users.size();i++){
            username+=+users.get(i).getId()+"-----"+users.get(i).getName()+"\n";
        }
        textView.setText(username);
    }

}
