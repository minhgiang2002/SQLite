package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.content.DialogInterface;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView todoListView;
    Button btnAdd, btnUpdate, btnDelete;
    EditText task_title, task_content, task_date;
    LinearLayout LinerListView;
    private int id;
    private int status;

    private void refreshListView() {
        ToDoDAO todoDAO = new ToDoDAO(MainActivity.this);
        ArrayList<ToDo> list = todoDAO.getListToDo();
        ToDoAdapter toDoAdapter = new ToDoAdapter(MainActivity.this, list);
        todoListView.setAdapter(toDoAdapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoListView = findViewById(R.id.todoListView);
        ToDoDAO todoDAO = new ToDoDAO(this);
        ArrayList<ToDo> list = todoDAO.getListToDo();
        ToDoAdapter toDoAdapter  = new ToDoAdapter(this, list);
        todoListView.setAdapter(toDoAdapter);

        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        Button btnDelete = findViewById(R.id.btnDelete);
        final EditText edtTitle = findViewById(R.id.edtTitle);
        final EditText edtContent = findViewById(R.id.edtContent);
        final EditText edtDate = findViewById(R.id.edtDate);
        final EditText edtType = findViewById(R.id.edtType);

        todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy dữ liệu từ mục được chọn trong ListView
                ToDo selectedToDo = (ToDo) parent.getItemAtPosition(position);

                // Hiển thị dữ liệu lên EditTexts tương ứng
                edtTitle.setText(selectedToDo.getTitle());
                edtContent.setText(selectedToDo.getContent());
                edtDate.setText(selectedToDo.getDate());
                edtType.setText(selectedToDo.getType());

                MainActivity.this.id = selectedToDo.getId();
                MainActivity.this.status = selectedToDo.getStatus();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ EditText
                String title = edtTitle.getText().toString();
                String content = edtContent.getText().toString();
                String date = edtDate.getText().toString();
                String type = edtType.getText().toString();
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content) || TextUtils.isEmpty(date) || TextUtils.isEmpty(type)) {
                    // Hiển thị thông báo lỗi nếu có trường nào đó rỗng
                    Toast.makeText(MainActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    // Tạo một đối tượng ToDo mới
                    ToDo newToDo = new ToDo(0, title, content, date, type, 0);

                    // Thêm ToDo vào cơ sở dữ liệu
                    ToDoDAO todoDAO = new ToDoDAO(MainActivity.this);
                    boolean isSuccess = todoDAO.Add(newToDo);

                    if (isSuccess) {
                        // Nếu thêm thành công, hiển thị thông báo thành công
                        Toast.makeText(MainActivity.this, "Thêm mới thành công", Toast.LENGTH_SHORT).show();
                        // Cập nhật ListView
                        ArrayList<ToDo> list = todoDAO.getListToDo();
                        ToDoAdapter toDoAdapter = new ToDoAdapter(MainActivity.this, list);
                        todoListView.setAdapter(toDoAdapter);
                    } else {
                        // Nếu thêm không thành công, hiển thị thông báo lỗi
                        Toast.makeText(MainActivity.this, "Thêm mới thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo và hiển thị AlertDialog để xác nhận việc xóa
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Bạn có chắc chắn muốn xóa không?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Xác nhận xóa, gọi phương thức Delete trong ToDoDAO
                                ToDoDAO todoDAO = new ToDoDAO(MainActivity.this);
                                todoDAO.Delete(id);
                                refreshListView();
                                // Xóa dữ liệu trong EditTexts
                                edtTitle.setText("");
                                edtContent.setText("");
                                edtDate.setText("");
                                edtType.setText("");
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Không làm gì khi nhấn "Không"
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ EditText
                String title = edtTitle.getText().toString();
                String content = edtContent.getText().toString();
                String date = edtDate.getText().toString();
                String type = edtType.getText().toString();

                // Kiểm tra xem có trường nào đó rỗng không
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content) || TextUtils.isEmpty(date) || TextUtils.isEmpty(type)) {
                    // Hiển thị thông báo lỗi nếu có trường nào đó rỗng
                    Toast.makeText(MainActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    // Tạo và hiển thị AlertDialog để xác nhận việc cập nhật
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Bạn có chắc chắn muốn cập nhật không?")
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Xác nhận cập nhật, gọi phương thức Update trong ToDoDAO
                                    ToDo updatedToDo = new ToDo(id, title, content, date, type, status);
                                    ToDoDAO todoDAO = new ToDoDAO(MainActivity.this);
                                    todoDAO.Update(updatedToDo);
                                    refreshListView();

                                    // Xóa dữ liệu trong EditTexts
                                    edtTitle.setText("");
                                    edtContent.setText("");
                                    edtDate.setText("");
                                    edtType.setText("");
                                }
                            })
                            .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Không làm gì khi nhấn "Không"
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });
    }
}