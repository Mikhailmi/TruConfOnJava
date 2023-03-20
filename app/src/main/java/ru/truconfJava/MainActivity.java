package ru.truconfJava;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import ru.truconfJava.databinding.ActivityMainBinding;

import java.util.*;


public class MainActivity extends AppCompatActivity implements OnTouchListener {

    ActivityMainBinding binding;
    int bottom = 0;
    int top = 0;
    private boolean upMove = false;
    private Thread thread;
    boolean isAlive = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        view.setOnTouchListener(this);
        setContentView(view);
        binding.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.interrupt();
                isAlive = false;
            }
        });
    }

    boolean flag = true;

    @SuppressLint({"ResourceAsColor", "ClickableViewAccessibility"})
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (thread != null && !thread.isInterrupted()) {
            isAlive = false;
            thread.interrupt();
        }
        final int x = (int) event.getRawX();
        final int y = (int) event.getRawY();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) binding.textView.getLayoutParams();
        layoutParams.leftMargin = x - 100;
        layoutParams.topMargin = y - 250;
        layoutParams.rightMargin = 0;
        layoutParams.bottomMargin = 0;
        binding.textView.setLayoutParams(layoutParams);
        if (Locale.getDefault().getCountry().equals("RU")) binding.
                textView.setTextColor(R.color.purple_700);
        else if (Locale.getDefault().getCountry().equals("EN")) binding.textView.setTextColor(
                R.color.red
        );
        binding.container.invalidate();
        upMove = false;
        Runnable runnable = () -> {
            while (isAlive) {
                if (upMove) {
                    bottom = binding.textView.getBottom() - 1;
                    top = binding.textView.getTop() - 1;
                    if (0 >= top)
                        upMove = false;
                } else {
                    bottom = binding.textView.getBottom() + 1;
                    top = binding.textView.getTop() + 1;
                    if (bottom >= binding.container.getHeight())
                        upMove = true;
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                binding.textView.post(() -> {
                    if (flag) {
                        try {
                            Thread.sleep(5000);
                            flag = false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    binding.textView.setBottom(bottom);
                    binding.textView.setTop(top);
                });
            }
        };
        thread = new Thread(runnable);
        isAlive = true;
        flag = true;
        thread.start();
        return true;
    }
}