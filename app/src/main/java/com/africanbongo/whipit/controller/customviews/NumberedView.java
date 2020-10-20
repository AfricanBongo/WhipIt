package com.africanbongo.whipit.controller.customviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.africanbongo.whipit.R;

public class NumberedView {

    private ConstraintLayout constraintLayout;
    private TextView stepNumber;
    private TextView stepContent;

    public NumberedView(@NonNull Context context, int number, String content) {

        // Inflate and attach
        View view = LayoutInflater.from(context).inflate(R.layout.numbered_view_layout, null);

        constraintLayout = view.findViewById(R.id.steps_constraint);
        stepNumber = view.findViewById(R.id.step_number);
        stepContent = view.findViewById(R.id.step_content);

        // Set text fields of the views
        stepNumber.setText(String.valueOf(number));
        stepContent.setText(content);
    }

    public ConstraintLayout getStepView() {
        return constraintLayout;
    }
}
