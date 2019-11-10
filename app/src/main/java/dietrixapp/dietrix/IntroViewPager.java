package dietrixapp.dietrix;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class IntroViewPager extends AppCompatActivity {

    Button introContBtn;
    ViewPager viewPager;
    int[] layouts = {R.layout.intro_1, R.layout.intro_2, R.layout.intro_3, R.layout.intro_4, R.layout.intro_5};
    private LinearLayout dots_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_view_pager);

        introContBtn = findViewById(R.id.IntroContBtn);
        introContBtn.setVisibility(View.INVISIBLE);
        viewPager = findViewById(R.id.introViewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(layouts, this);
        viewPager.setAdapter(pagerAdapter);
        dots_layout = findViewById(R.id.dot_ll_layout);
        createDots(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                createDots(position);
                if(position==layouts.length-1){
                    introContBtn.setVisibility(View.VISIBLE);
                    introContBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getBaseContext(),FirstBoot.class);
                            startActivity(intent);
                        }
                    });
                }
                else {introContBtn.setVisibility(View.INVISIBLE);}
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void createDots(int position){
        if(dots_layout != null)
            dots_layout.removeAllViews();

        ImageView[] dots = new ImageView[layouts.length];

            for(int i = 0; i<layouts.length;i++){
                dots[i] = new ImageView(this);
                if(i == position){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dot));
                }
                else { dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.inactive_dot));}

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(4,0,4,0);

                dots_layout.addView(dots[i],params);
            }

    }
}

