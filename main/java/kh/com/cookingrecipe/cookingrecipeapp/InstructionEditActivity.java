package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class InstructionEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_edit);

        Intent getIntent = getIntent();
        String instruction = getIntent.getStringExtra("instruction");
        String step_name = getIntent.getStringExtra("step");
        String position = getIntent.getStringExtra("position");

        final EditText etxt_step = findViewById(R.id.edit_instruction_step);
        final EditText etxt_detail = findViewById(R.id.edit_instruction_detail);

        etxt_step.setText(step_name);
        etxt_detail.setText(instruction);

        Button button_add = findViewById(R.id.btn_edit_instruction);
        button_add.setOnClickListener(v -> {
            String step = etxt_step.getText().toString();
            String detail = etxt_detail.getText().toString();
            if (step.matches("")||detail.matches("")){
                AlertDialog alertDialog = new AlertDialog.Builder(InstructionEditActivity.this).create();
                alertDialog.setTitle("Forget:");
                alertDialog.setMessage("Input Step and Detail of Your Recipe");
                alertDialog.show();
            }else {
                Intent intent = new Intent();
                intent.putExtra("Instruction_Step", step);
                intent.putExtra("Instruction_Detail", detail);
                intent.putExtra("position", position);
                intent.putExtra("Status", "Edit Instruction");
                setResult(1001, intent);
                finish();
            }
        });
        ImageView imgCancel = findViewById(R.id.cancel_instruction_edit);
        imgCancel.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("Status", "Cancel Edit Instruction");
            setResult(1001,intent);
            finish();
        });

    }
}