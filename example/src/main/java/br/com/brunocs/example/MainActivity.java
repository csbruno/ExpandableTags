package br.com.brunocs.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.security.SecureRandom;
import java.util.Random;

import br.com.brunocs.expandabletags.ExpandableTags;
import br.com.brunocs.expandabletags.Tag;

public class MainActivity extends AppCompatActivity {

    private ExpandableTags expandableTags;
    private Button button;
    private Button collapse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expandableTags = (ExpandableTags) findViewById(R.id.expandable_tag);
        button = (Button) findViewById(R.id.button);
        collapse = (Button) findViewById(R.id.collapse);

        expandableTags.addTag(new Tag("Price","'Description"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableTags.addTag(new Tag(randomString((new Random()).nextInt(10)),"desc"));
            }
        });
        collapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableTags.animateCollapse();
            }
        });

      //  expandableTags.addTag(new Tag("Coderino","'Description"));
    }
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
}
