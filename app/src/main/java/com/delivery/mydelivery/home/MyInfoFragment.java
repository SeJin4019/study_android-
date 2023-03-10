package com.delivery.mydelivery.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.delivery.mydelivery.MainActivity;
import com.delivery.mydelivery.R;;
import com.delivery.mydelivery.preferenceManager.PreferenceManager;
import com.delivery.mydelivery.register.UserVO;
import com.google.gson.Gson;

// 내정보 프래그먼트
public class MyInfoFragment extends Fragment {

    TextView jsonTextTV; // json데이터 출력 테스트
    Button logoutBtn; // 로그아웃 버튼

    View view; // 해당 view에 지정한 프래그먼트 추가

    Context context; // context

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_myinfo, container, false); // view 설정
        context = container.getContext(); // context 초기화

        jsonTextTV = view.findViewById(R.id.jsonTextTV);
        logoutBtn = view.findViewById(R.id.logoutBtn);

        String loginInfo = PreferenceManager.getLoginInfo(context);

        if (!loginInfo.isEmpty()) {
            Gson gson = new Gson();
            UserVO user = gson.fromJson(loginInfo, UserVO.class);

            jsonTextTV.setText(user.getEmail());
        } else {
            jsonTextTV.setText(loginInfo);
        }

        // 로그아웃 이벤트
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceManager.logout(context);

                // 프래그먼트 종료
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }


}
