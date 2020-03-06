package se1302.demo.contentproviderdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import se1302.demo.contentproviderdemo.models.StudentModel;
import se1302.demo.contentproviderdemo.providers.StudentProvider;

public class DetailStudentActivity extends AppCompatActivity {
    EditText edtName, edtCore;
    RadioGroup rdGraduate;
    StudentModel student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_student);
        edtName = findViewById(R.id.edtName);
        edtCore = findViewById(R.id.edtCore);
        Intent intent = this.getIntent();
        Cursor cursor = getContentResolver().query(StudentProvider.CONTENT_URI, null, "Id = ?", new String[] {intent.getStringExtra("id")}, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                student = new StudentModel(
                        cursor.getString(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getInt(cursor.getColumnIndex("core")),
                        cursor.getInt(cursor.getColumnIndex("isGraduate"))
                );
            }
            cursor.close();
        }
        edtName.setText(student.getName());
        edtCore.setText(student.getCore() + "");
        rdGraduate = (RadioGroup) findViewById(R.id.rdGraduate);
        ((RadioButton) findViewById(student.isGradute() == 0 ? R.id.graduated : R.id.studying)).setChecked(true);
        rdGraduate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.graduated) {
                    student.setGradute(0);
                } else {
                    student.setGradute(1);
                }
            }
        });
    }

    public void clickToUpdateStudent(View view) {
        student.setName(edtName.getText().toString());
        student.setCore(Integer.parseInt(edtCore.getText().toString()));
        Intent intent = this.getIntent();
        ContentValues values = new ContentValues();
        values.put("name", student.getName());
        values.put("core", student.getCore());
        values.put("isGraduate", student.isGradute());
        boolean check = getContentResolver().update(StudentProvider.CONTENT_URI, values, "Id = ?", new String[] {student.getId()}) > 0;
        if (check) {
            Toast.makeText(this, "Cập nhật học sinh thành công", Toast.LENGTH_SHORT).show();
            this.setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Cập nhật học sinh thất bại", Toast.LENGTH_SHORT).show();
            this.setResult(RESULT_CANCELED, intent);
            finish();
        }
    }

}
