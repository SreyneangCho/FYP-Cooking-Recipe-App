package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AddInstructionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instruction);

        ImageView imgCancel = (ImageView) findViewById(R.id.cancel_instruction);
        imgCancel.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("Status", "Cancel New Instruction");
            setResult(1,intent);
            finish();
        });

        final EditText etxt_step =(EditText)findViewById(R.id.etxt_instruction_step);
        final EditText etxt_detail =(EditText)findViewById(R.id.etxt_instruction_detail);
        Button button_add =(Button)findViewById(R.id.btn_add_instruction);
        button_add.setOnClickListener(v -> {
            String step = etxt_step.getText().toString();
            String detail = etxt_detail.getText().toString();
            if (step.matches("")||detail.matches("")){
                AlertDialog alertDialog = new AlertDialog.Builder(AddInstructionActivity.this).create();
                alertDialog.setTitle("Forget:");
                alertDialog.setMessage("Input Step and Detail of Your Recipe");
                alertDialog.show();
            }else {
                Intent intent = new Intent();
                intent.putExtra("Instruction_Step", step);
                intent.putExtra("Instruction_Detail", detail);
                intent.putExtra("Status", "Add New Instruction");
                setResult(1, intent);
                finish();
            }
        });


    }


}
