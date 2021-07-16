package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class AddRecipeActivity extends AppCompatActivity {

    private final ArrayList<RecipeInstructionDataList> list_instruction = new ArrayList();
    private final ArrayList<RecipeIngredientDataList> list_ingredient = new ArrayList();
    private final ArrayList<InstructionInfo> arr_instruction = new ArrayList();
    private final ArrayList<IngredientInfo> arr_ingredient = new ArrayList();
    private int time_cooking;
    private int serving;
    private ImageView btnSelect;
    private ImageView imageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        btnSelect = findViewById(R.id.btn_select_image);
        imageView = findViewById(R.id.linearlayout_imageView);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnSelect.setOnClickListener(v -> {
            SelectImage();
            btnSelect.setVisibility(View.GONE);
        });
        

        TextView txtCancel = findViewById(R.id.cancel_new_recipe);
        txtCancel.setOnClickListener(v -> finish());

        TextView txtAddInstruction = findViewById(R.id.add_instruction);
        txtAddInstruction.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AddInstructionActivity.class);
            startActivityForResult(intent, 1);
        });

        TextView txtAddIngredient = findViewById(R.id.add_ingredient);
        txtAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AddIngredientActivity.class);
            startActivityForResult(intent, 2);
        });


        SeekBar Timebar = findViewById(R.id.SeekBarTime);

        Timebar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                TextView txt_time = findViewById(R.id.txt_time);
                txt_time.setText(String.valueOf(seekBar.getProgress()));
                time_cooking = seekBar.getProgress();
            }
        });
        SeekBar Peoplebar = findViewById(R.id.SeekBarPeople);

        Peoplebar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                TextView txt_people = findViewById(R.id.txt_people);
                txt_people.setText(String.valueOf(seekBar.getProgress()));
                serving = seekBar.getProgress();
            }
        });

        Button btnUploadRecipe = findViewById(R.id.btn_upload_recipe);
        btnUploadRecipe.setOnClickListener(v -> uploadImage());


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {

            String instruction_step = data.getStringExtra("Instruction_Step");
            String instruction_step_num =  String.valueOf(list_instruction.size()+1);
            String instruction_detail = data.getStringExtra("Instruction_Detail");
            String status = data.getStringExtra("Status");
            if(status.equals("Add New Instruction")){
                list_instruction.add(new RecipeInstructionDataList(Integer.parseInt(instruction_step_num), instruction_step, instruction_detail));
                arr_instruction.add(new InstructionInfo(instruction_step, instruction_detail));

                RecyclerView recyclerView= findViewById(R.id.recylerView_add_instruction);
                MyRecipeInstructionListAdapter adapter = new MyRecipeInstructionListAdapter(list_instruction);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(AddRecipeActivity.this));
                recyclerView.setAdapter(adapter);
            }
        }

        if(requestCode==1000){
            String position = data.getStringExtra("position");
            String ingredient =  data.getStringExtra("Ingredient_Name");
            String quantity = data.getStringExtra("Ingredient_Quantity");
            String status = data.getStringExtra("Status");
            if(status.equals("Edited Ingredient")){

                list_ingredient.set(Integer.parseInt(position)-1, new RecipeIngredientDataList(Integer.parseInt(position),ingredient , quantity));
                arr_ingredient.set(Integer.parseInt(position)-1, new IngredientInfo(ingredient, quantity));

                RecyclerView recyclerView= findViewById(R.id.recylerView_add_ingredient);
                MyRecipeIngredientListAdapter adapter = new MyRecipeIngredientListAdapter(list_ingredient);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(AddRecipeActivity.this));
                recyclerView.setAdapter(adapter);
            }
        }

        if(requestCode==1001){
            String position = data.getStringExtra("position");
            String step =  data.getStringExtra("Instruction_Step");
            String instruction = data.getStringExtra("Instruction_Detail");
            String status = data.getStringExtra("Status");
            if(status.equals("Edit Instruction")){

                list_instruction.set(Integer.parseInt(position)-1, new RecipeInstructionDataList(Integer.parseInt(position),step , instruction));
                arr_instruction.set(Integer.parseInt(position)-1, new InstructionInfo(step , instruction));

                RecyclerView recyclerView= findViewById(R.id.recylerView_add_instruction);
                MyRecipeInstructionListAdapter adapter = new MyRecipeInstructionListAdapter(list_instruction);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(AddRecipeActivity.this));
                recyclerView.setAdapter(adapter);
            }
        }

        if(requestCode==2)
        {
            String ingredient_num = String.valueOf(list_ingredient.size()+1);
            String ingredient_name = data.getStringExtra("Ingredient_Name");
            String ingredient_quantity = data.getStringExtra("Ingredient_Quantity");
            String status = data.getStringExtra("Status");
            if(status.equals("Add New Ingredient")){
                list_ingredient.add(new RecipeIngredientDataList(Integer.parseInt(ingredient_num),ingredient_name , ingredient_quantity));
                arr_ingredient.add(new IngredientInfo(ingredient_name, ingredient_quantity));

                RecyclerView recyclerView= findViewById(R.id.recylerView_add_ingredient);
                MyRecipeIngredientListAdapter adapter = new MyRecipeIngredientListAdapter(list_ingredient);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(AddRecipeActivity.this));
                recyclerView.setAdapter(adapter);
            }

        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
    private void SelectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult( Intent.createChooser( intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }
    private  void uploadImage() {
        if (filePath != null) {
            EditText etxt_recipe_name = findViewById(R.id.etxt_recipe_name);
            final String recipe_name = etxt_recipe_name.getText().toString();

            String status;

            if(recipe_name.equals("")){
                status = "Forget: Recipe Name";
            }else if(arr_ingredient.isEmpty()){
                status = "Forget: Ingredient";
            }else if(arr_instruction.isEmpty()){
                status = "Forget: Instruction";
            }else{
                status = "ok";
            }
            if(status.equals("ok")) {

                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

                ref.putFile(filePath).addOnSuccessListener(taskSnapshot -> {

                }).addOnFailureListener(e -> {

                    progressDialog.dismiss();
                    Toast.makeText(AddRecipeActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }).addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                }).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    String image_path = uri.toString();

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    assert user != null;
                    String userId = user.getUid();
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                    String format = simpleDateFormat.format(new Date());
                    DatabaseReference mRef = database.getReference().child("Recipe").push();
                    String key_id = mRef.getKey();
                    RecipeData NewRecipe = new RecipeData(recipe_name, image_path, time_cooking, serving, 0, userId, format, 0);
                    mRef.setValue(NewRecipe);

                    DatabaseReference mRef1 = database.getReference().child("Recipe/" + key_id + "/instruction");
                    for (int i = 0; i < arr_instruction.size(); i++) {
                        int n = i + 1;
                        mRef1.child(String.valueOf(n)).setValue(arr_instruction.get(i));
                    }
                    DatabaseReference mRef2 = database.getReference().child("Recipe/" + key_id + "/ingredient");
                    for (int i = 0; i < arr_ingredient.size(); i++) {
                        int n = i + 1;
                        mRef2.child(String.valueOf(n)).setValue(arr_ingredient.get(i));
                    }
                    progressDialog.dismiss();
                    Toast.makeText(AddRecipeActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    finish();
                }));

            }else {
            Toast.makeText(AddRecipeActivity.this, status, Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(AddRecipeActivity.this, "Select your image.", Toast.LENGTH_SHORT).show();
        }
    }

}
