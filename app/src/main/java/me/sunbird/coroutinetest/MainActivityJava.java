package me.sunbird.coroutinetest;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;

public class MainActivityJava extends AppCompatActivity {

    private TextView tvContent;
    private Button btnEditByCoroutine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_edit_by_tradition).setVisibility(View.INVISIBLE);
        tvContent = findViewById(R.id.tv_content);
        btnEditByCoroutine = findViewById(R.id.btn_edit_by_coroutine);
        btnEditByCoroutine.setOnClickListener((view) -> {
            String content = tvContent.getText().toString().trim();
            EditActivity.editContent(MainActivityJava.this, content, new Continuation<String>() {
                @NotNull
                @Override
                public CoroutineContext getContext() {
                    return EmptyCoroutineContext.INSTANCE;
                }

                @Override
                public void resumeWith(@NotNull Object o) {
                    String newContent = (String) o;
                    if (!TextUtils.isEmpty(content)) {
                        tvContent.setText(newContent);
                    }
                }
            });
        });
    }
}