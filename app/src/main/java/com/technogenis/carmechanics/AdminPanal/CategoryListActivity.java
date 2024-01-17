package com.technogenis.carmechanics.AdminPanal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.technogenis.carmechanics.R;

public class CategoryListActivity extends AppCompatActivity implements View.OnClickListener{

    TextView bumper_text,fender_text,door_text,bonnet_text,diggi_text,roof_text,screen_replace_frt_text
            ,screen_replace_rr_text,dashboard_text,room_fitting_text,engine_text,suspension_text
            ,frame_repairing_text,wheel_alignment_text,door_glass_text,door_mirror_text,loading_cabin_text
            ,tail_gate_text,head_light_text,back_light_text,radiator_text,full_compound_text
            ,interior_text,under_coating_text;
    String selectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        initViews();

        bumper_text.setOnClickListener(this::onClick);
        under_coating_text.setOnClickListener(this::onClick);
        interior_text.setOnClickListener(this::onClick);
        full_compound_text.setOnClickListener(this::onClick);
        radiator_text.setOnClickListener(this::onClick);
        back_light_text.setOnClickListener(this::onClick);
        head_light_text.setOnClickListener(this::onClick);
        tail_gate_text.setOnClickListener(this::onClick);
        suspension_text.setOnClickListener(this::onClick);
        engine_text.setOnClickListener(this::onClick);
        room_fitting_text.setOnClickListener(this::onClick);
        dashboard_text.setOnClickListener(this::onClick);
        screen_replace_rr_text.setOnClickListener(this::onClick);
        screen_replace_frt_text.setOnClickListener(this::onClick);
        roof_text.setOnClickListener(this::onClick);
        fender_text.setOnClickListener(this::onClick);
        door_text.setOnClickListener(this::onClick);
        bonnet_text.setOnClickListener(this::onClick);
        diggi_text.setOnClickListener(this::onClick);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v)
    {
        int id = v.getId();


        if (id == R.id.bumper_text){
            selectText = bumper_text.getText().toString();
        }else if(id == R.id.fender_text){
            selectText = fender_text.getText().toString();
        } else if(id == R.id.door_text){
            selectText = door_text.getText().toString();
        }else if(id == R.id.bonnet_text){
            selectText = bonnet_text.getText().toString();
        }else if(id == R.id.diggi_text){
            selectText = diggi_text.getText().toString();
        }else if(id == R.id.roof_text){
            selectText = roof_text.getText().toString();
        }else if(id == R.id.screen_replace_frt_text){
            selectText = screen_replace_frt_text.getText().toString();
        }else if(id == R.id.screen_replace_rr_text){
            selectText = screen_replace_rr_text.getText().toString();
        }else if(id == R.id.dashboard_text){
            selectText = dashboard_text.getText().toString();
        }else if(id == R.id.room_fitting_text){
            selectText = room_fitting_text.getText().toString();
        }else if(id == R.id.engine_text){
            selectText = engine_text.getText().toString();
        }if (id == R.id.suspension_text) {
        selectText = suspension_text.getText().toString();
    } else if (id == R.id.frame_repairing_text) {
        selectText = frame_repairing_text.getText().toString();
    } else if (id == R.id.wheel_alignment_text) {
        selectText = wheel_alignment_text.getText().toString();
    } else if (id == R.id.door_glass_text) {
        selectText = door_glass_text.getText().toString();
    } else if (id == R.id.door_mirror_text) {
        selectText = door_mirror_text.getText().toString();
    } else if (id == R.id.loading_cabin_text) {
        selectText = loading_cabin_text.getText().toString();
    } else if (id == R.id.tail_gate_text) {
        selectText = tail_gate_text.getText().toString();
    } else if (id == R.id.head_light_text) {
        selectText = head_light_text.getText().toString();
    } else if (id == R.id.back_light_text) {
        selectText = back_light_text.getText().toString();
    } else if (id == R.id.radiator_text) {
        selectText = radiator_text.getText().toString();
    } else if (id == R.id.full_compound_text) {
        selectText = full_compound_text.getText().toString();
    } else if (id == R.id.interior_text) {
        selectText = interior_text.getText().toString();
    } else if (id == R.id.under_coating_text) {
        selectText = under_coating_text.getText().toString();
    }

        Intent intent = new Intent();
        intent.putExtra("categoryListIntent", selectText);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initViews()
    {
        bumper_text = findViewById(R.id.bumper_text);
        fender_text = findViewById(R.id.fender_text);
        door_text = findViewById(R.id.door_text);
        bonnet_text = findViewById(R.id.bonnet_text);
        diggi_text = findViewById(R.id.diggi_text);
        roof_text = findViewById(R.id.roof_text);
        screen_replace_frt_text = findViewById(R.id.screen_replace_frt_text);
        screen_replace_rr_text = findViewById(R.id.screen_replace_rr_text);
        dashboard_text = findViewById(R.id.dashboard_text);
        room_fitting_text = findViewById(R.id.room_fitting_text);
        engine_text = findViewById(R.id.engine_text);
        suspension_text = findViewById(R.id.suspension_text);
        frame_repairing_text = findViewById(R.id.frame_repairing_text);
        wheel_alignment_text = findViewById(R.id.wheel_alignment_text);
        door_glass_text = findViewById(R.id.door_glass_text);
        door_mirror_text = findViewById(R.id.door_mirror_text);
        loading_cabin_text = findViewById(R.id.loading_cabin_text);
        tail_gate_text = findViewById(R.id.tail_gate_text);
        head_light_text = findViewById(R.id.head_light_text);
        back_light_text = findViewById(R.id.back_light_text);
        radiator_text = findViewById(R.id.radiator_text);
        full_compound_text = findViewById(R.id.full_compound_text);
        interior_text = findViewById(R.id.interior_text);
        under_coating_text = findViewById(R.id.under_coating_text);
    }
}