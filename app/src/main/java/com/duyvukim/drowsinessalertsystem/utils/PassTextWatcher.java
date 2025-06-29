package com.duyvukim.drowsinessalertsystem.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.duyvukim.drowsinessalertsystem.R;

public class PassTextWatcher implements TextWatcher {
    private View currentView;
    private View nextView;

    public PassTextWatcher(View currentView, View nextView) {
        this.currentView = currentView;
        this.nextView = nextView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Không cần làm gì ở đây
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Kiểm tra nếu EditText hiện tại có văn bản và không rỗng
        if (s.length() == 1 && nextView != null) {
            nextView.requestFocus(); // Chuyển focus sang EditText tiếp theo
        } else if (s.length() == 0 && before == 1 && currentView.getId() != R.id.digit1) {
            // Nếu người dùng xóa ký tự và không phải là ô đầu tiên, chuyển focus về ô trước đó
            if (currentView.focusSearch(View.FOCUS_LEFT) != null) {
                currentView.focusSearch(View.FOCUS_LEFT).requestFocus();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Không cần làm gì ở đây
    }
}
