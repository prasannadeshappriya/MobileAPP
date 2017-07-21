package com.a14roxgmail.prasanna.mobileapp.Algorithm;

import android.content.Context;
import android.util.Log;

import com.a14roxgmail.prasanna.mobileapp.Constants.Constants;
import com.a14roxgmail.prasanna.mobileapp.Constants.GpaPoints;
import com.a14roxgmail.prasanna.mobileapp.DAO.CourseDAO;
import com.a14roxgmail.prasanna.mobileapp.DAO.GradeDAO;
import com.a14roxgmail.prasanna.mobileapp.Model.Course;
import com.a14roxgmail.prasanna.mobileapp.Model.GPA;

import java.util.ArrayList;

/**
 * Created by Prasanna Deshappriya on 1/24/2017.
 */
public class GPA2 {
    //this will calculate the gpa
    //  gpa = [sigma(gpa point * credit)]/[sigma(credit)]
    private ArrayList<GPA> sgpa_list;
    private GradeDAO grade_dao;
    private CourseDAO course_dao;
    private String userIndex;
    private Context context;
    public GPA2(Context context, GradeDAO grade_dao, String userIndex){
        this.grade_dao = grade_dao;
        this.userIndex = userIndex;
        this.context = context;
        course_dao = new CourseDAO(context);
        sgpa_list = grade_dao.getSGPAArray(userIndex);
    }

    public boolean calculate() {
        String max = course_dao.getMaxSemester(userIndex);
        double total_credits = 0.0;
        double sum = 0.0;
        for(int i=0; i<Integer.parseInt(max+1); i++){
            ArrayList<Course> arrCourse = course_dao.getAllCourseBySemester(String.valueOf(i), userIndex);
            for(int j=0; j<arrCourse.size(); j++){
                Course course = arrCourse.get(j);
                if(course_dao.isGradeExist(userIndex,course.getCourseCode())) {
                    double gpa_point = Double.parseDouble(GpaPoints.getPoint(course.getGrade()));
                    double credit = Double.parseDouble(course.getCredits());

                    sum+=(gpa_point*credit);
                    Log.i(Constants.LOG_TAG, "gpa_point :- " + gpa_point + ", credit :- " + credit + ", sum :- " + sum);
                    total_credits += credit;
                    Log.i(Constants.LOG_TAG, "total credits :- " + total_credits);
                }
            }
        }

        double gpa = (sum / total_credits);
        Log.i(Constants.LOG_TAG, "Calculate gpa using method 2 :- " + gpa);

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

