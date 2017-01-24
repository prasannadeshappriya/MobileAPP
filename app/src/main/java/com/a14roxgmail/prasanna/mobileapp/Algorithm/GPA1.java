package com.a14roxgmail.prasanna.mobileapp.Algorithm;

import android.content.Context;
import android.util.Log;
import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.DAO.GradeDAO;
import com.a14roxgmail.prasanna.mobileapp.Model.GPA;

import java.util.ArrayList;

/**
 * Created by Prasanna Deshappriya on 1/24/2017.
 */

public class GPA1 {
    //this will calculate the gpa
    //  gpa = [sigma(gpa)]/[semester count
    private ArrayList<GPA> sgpa_list;
    private GradeDAO grade_dao;
    private String userIndex;
    private Context context;

    public GPA1(Context context,GradeDAO grade_dao, String userIndex){
        this.context = context;
        this.grade_dao = grade_dao;
        this.userIndex = userIndex;
        sgpa_list = grade_dao.getSGPAArray(userIndex);
    }

    public boolean calculate() {
        double totalSgpa = 0.0;

        for (int i = 0; i < sgpa_list.size(); i++) {
            GPA gpa = sgpa_list.get(i);
            try {
                totalSgpa += Double.parseDouble(gpa.getGpa());
            } catch (NumberFormatException e) {
                Log.i(Constants.LOG_TAG, "Error :- " + e.toString());
            }
        }

        double gpa = (totalSgpa / sgpa_list.size());
        Log.i(Constants.LOG_TAG, "Calculate gpa using method 1 :- " + gpa);

        if (grade_dao.isGPAExist(userIndex)) {
            grade_dao.updateGPA(
                    userIndex,
                    String.valueOf(gpa)
            );
        } else {
            if (!Double.isNaN(gpa)) {
                grade_dao.addGpa(
                        new GPA(
                                Constants.GPA_FLAG,
                                "",
                                String.valueOf(gpa),
                                userIndex,
                                ""
                        )
                );
            }
        }
        return true;
    }
}
