package com.delivery.mydelivery.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.delivery.mydelivery.R;
import com.delivery.mydelivery.retrofit.RetrofitService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 이메일 등록 액티비티
public class EmailRegisterActivity extends AppCompatActivity {

    EditText emailET;
    Button duplicationCkBtn;
    Button nextBtn;

    // 레트로핏, api
    RetrofitService retrofitService;
    RegisterApi api;

    UserVO userVO; // 데이터를 담을 객체

    Boolean regExFlag = false; // 정규식 검사 성공 여부

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);

        emailRegExCk(); // 실시간 이메일 정규식 검사 메소드

        emailET = findViewById(R.id.emailET);
        duplicationCkBtn = findViewById(R.id.duplicationCkBtn);
        nextBtn = findViewById(R.id.nextBtn);

        // 중복검사 이벤트
        duplicationCkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString();

                if (email.isEmpty()) { // 공백
                    Toast.makeText(EmailRegisterActivity.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                    nextBtn.setVisibility(View.INVISIBLE);
                } else if (regExFlag == false) { // 정규식 검사 실패
                    Toast.makeText(EmailRegisterActivity.this, "이메일을 올바르게 입력해주세요", Toast.LENGTH_SHORT).show();
                    nextBtn.setVisibility(View.INVISIBLE);
                } else { // 검사 성공
                    callDuplicateCkApi(email); // 중복검사 api 호출
                }
            }
        });

        // 다음 액티비티로 이동 이벤트
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString();

                // registerVO에 입력한 이메일을 저장
                userVO = new UserVO();
                userVO.setEmail(email);

                // 다음 액티비티로 이동
                Intent intent = new Intent(EmailRegisterActivity.this, AuthRegisterActivity.class);
                intent.putExtra("userVO", userVO);
                startActivity(intent);
                finish();
            }
        });

    }

    // 실시간 이메일 정규식 검사 메소드
    private void emailRegExCk() {
        emailET = findViewById(R.id.emailET);

        emailET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = emailET.getText().toString();
                boolean flag = ckEmailRegEx(email);

                if (flag == true) { // 정규식 검사 성공시 색상 변경, regExFlag를 true로 설정
                    emailET.setBackgroundResource(R.drawable.edit_text_border_green);
                    regExFlag = true;
                } else { // 실패시
                    emailET.setBackgroundResource(R.drawable.edit_text_border_red);
                    regExFlag = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    // 이메일 정규식 검사
    private boolean ckEmailRegEx(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(email);

        if (matcher.find()) { // 이메일 형식이 맞을경우
            return true;
        } else {
            return false;
        }
    }

    // 중복검사 api 호출
    private void callDuplicateCkApi(String email) {
        retrofitService = new RetrofitService();
        api = retrofitService.getRetrofit().create(RegisterApi.class);

        nextBtn = findViewById(R.id.nextBtn);

        api.duplicateEmailCk(email)
                .enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        boolean duplicateCkResult = response.body(); // 중복여부 저장

                        if (duplicateCkResult == true) { // 중복되지 않은 이메일
                            Toast.makeText(EmailRegisterActivity.this, "사용가능한 이메일입니다.", Toast.LENGTH_SHORT).show();
                            nextBtn.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(EmailRegisterActivity.this, "중복된 이메일입니다.", Toast.LENGTH_SHORT).show();
                            nextBtn.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                    }
                });
    }

}

