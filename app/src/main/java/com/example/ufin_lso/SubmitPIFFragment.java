package com.example.ufin_lso;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;

public class SubmitPIFFragment extends Fragment {

    private boolean dbMaster = false;
    private long RelationIDDaughter = 0;
    private long RelationIDSon = 0;
    private long ProfessionIDStudent = 0;

    View rootView;
    MainActivity mainActivity;

    ArrayList<Integer> PIFProblemsSequence = new ArrayList<Integer>();
    ArrayList<String> PIFProblems = new ArrayList<String>();
    int selectedQueryPosition = -1;

    public SubmitPIFFragment() {
// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_submit_pif, container, false);
        mainActivity = (MainActivity) getActivity();

        if(!dbMaster) {
            SQLite_getBusinessMaster();
            SQLite_getRelationMaster();

            dbMaster = true;
        }

        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxLastLoanRemarks);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshScreen();
            }
        });

        Button btn = (Button) rootView.findViewById(R.id.buttonSubmitPIF);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.Member.size() <= 0)
                    return;

                if (!ValidatePIF())
                    return;

                CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxLastLoanRemarks);
                if(!cb.isChecked())
                    return;

                DeactivateDocumentsWithoutPhoto();
//bmp 07-Sep-22
                DeactivateDuplicateImpactAnswers();
//bmp 07-Sep-22

                //Submit Member PIF to Validator
                mainActivity.newGroup.arrGroupMember.get(mainActivity.selectedMemberPosition).MemberStatus
                        = clsGroupMember.MEMBER_STATUS_PIF_SUBMIT;

                for(int k=0; k<mainActivity.RevisitMemberList.size(); k++) {
                    if(mainActivity.RevisitMemberList.get(k).MemberID ==
                            mainActivity.newGroup.arrGroupMember.get(mainActivity.selectedMemberPosition).MemberID) {
                        mainActivity.RevisitMemberList.get(k).RevisitMemberStatus = clsRevisitMember.REVISIT_MEMBER_STATUS_PIF_SUBMITTED;
                        mainActivity.RevisitMemberList.get(k).Edited = true;
                        mainActivity.SQLite_updateRevisitMember(k, false);
                        break;
                    }
                }

                Button bt = (Button) mainActivity.findViewById(R.id.buttonSavePIF);
                bt.performClick();
            }
        });

        return rootView;
    }

    boolean ValidatePIF() {
        boolean Status = true;

        PIFProblemsSequence.clear();
        PIFProblems.clear();

        int indexMemberCaste = -1;
        int indexEducation = -1;
        int indexMaritalStatus = -1;
        int indexHabbit = -1;
        int indexDisease = -1;
        int indexPerCapitaIncome = -1;
        int indexAdditionalIncomeFromVillage = -1;

        for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
            if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1) {
                long answerID = mainActivity.MemberImpactAnswer.get(i).IPAnswerID;

                if (    (answerID == mainActivity.ImpactAnswer.MEMBER_CASTE_NT_ST) ||
                        (answerID == mainActivity.ImpactAnswer.MEMBER_CASTE_VJNT) ||
                        (answerID == mainActivity.ImpactAnswer.MEMBER_CASTE_SC) ||
                        (answerID == mainActivity.ImpactAnswer.MEMBER_CASTE_BC_SBC_BSC) ||
                        (answerID == mainActivity.ImpactAnswer.MEMBER_CASTE_OBC) ||
                        (answerID == mainActivity.ImpactAnswer.MEMBER_CASTE_OPEN) )
                    indexMemberCaste = i;

                if (    (answerID == mainActivity.ImpactAnswer.FAMILY_CHILDREN_EDUCATION_NOT_GOING_TO_SCHOOL) ||
                        (answerID == mainActivity.ImpactAnswer.FAMILY_CHILDREN_EDUCATION_PRIMARY) ||
                        (answerID == mainActivity.ImpactAnswer.FAMILY_CHILDREN_EDUCATION_SECONDARY) ||
                        (answerID == mainActivity.ImpactAnswer.FAMILY_CHILDREN_EDUCATION_GRADUATE) ||
                        (answerID == mainActivity.ImpactAnswer.FAMILY_CHILDREN_EDUCATION_PG_PROFESSIONAL_COURSES) )
                    indexEducation = i;

                if (    (answerID == mainActivity.ImpactAnswer.MARITAL_STATUS_UNMARRIED) ||
                        (answerID == mainActivity.ImpactAnswer.MARITAL_STATUS_SINGLE) ||
                        (answerID == mainActivity.ImpactAnswer.MARITAL_STATUS_REMARRIED) ||
                        (answerID == mainActivity.ImpactAnswer.MARITAL_STATUS_MARRIED) )
                    indexMaritalStatus = i;

                if (    (answerID == mainActivity.ImpactAnswer.BAD_HABBIT_WITHIN_FAMILY_MEMBERS_ALCOHOL) ||
                        (answerID == mainActivity.ImpactAnswer.BAD_HABBIT_WITHIN_FAMILY_MEMBERS_SMOKING) ||
                        (answerID == mainActivity.ImpactAnswer.BAD_HABBIT_WITHIN_FAMILY_MEMBERS_TOBBACO_PAN) ||
                        (answerID == mainActivity.ImpactAnswer.BAD_HABBIT_WITHIN_FAMILY_MEMBERS_NONE) )
                    indexHabbit = i;

                if (    (answerID == mainActivity.ImpactAnswer.ANY_DISEASE_WITHIN_FAMILY_MEMBERS_YES) ||
                        (answerID == mainActivity.ImpactAnswer.ANY_DISEASE_WITHIN_FAMILY_MEMBERS_NO) )
                    indexDisease = i;

                if (    (answerID == mainActivity.ImpactAnswer.PER_CAPITA_INCOME_0_899) ||
                        (answerID == mainActivity.ImpactAnswer.PER_CAPITA_INCOME_900_1349) ||
                        (answerID == mainActivity.ImpactAnswer.PER_CAPITA_INCOME_1350_1799) ||
                        (answerID == mainActivity.ImpactAnswer.PER_CAPITA_INCOME_1800_2249) ||
                        (answerID == mainActivity.ImpactAnswer.PER_CAPITA_INCOME_2250_3374) ||
                        (answerID == mainActivity.ImpactAnswer.PER_CAPITA_INCOME_3375_3937) ||
                        (answerID == mainActivity.ImpactAnswer.PER_CAPITA_INCOME_3938_4499) ||
                        (answerID == mainActivity.ImpactAnswer.PER_CAPITA_INCOME_4500_5624) ||
                        (answerID == mainActivity.ImpactAnswer.PER_CAPITA_INCOME_5625_6749) ||
                        (answerID == mainActivity.ImpactAnswer.PER_CAPITA_INCOME_6750_Onward) )
                    indexPerCapitaIncome = i;

                if (    (answerID == mainActivity.ImpactAnswer.ADDITIONAL_INCOME_FROM_VILLAGE_NO) ||
                        (answerID == mainActivity.ImpactAnswer.ADDITIONAL_INCOME_FROM_VILLAGE_YES) )
                    indexAdditionalIncomeFromVillage = i;
            }
        }

        long CasteCategoryImpactAnswerID = mainActivity.ImpactAnswer.MEMBER_CASTE_OPEN;
        switch ((int) mainActivity.Member.get(0).CasteCategoryID) {
            case 1:
                CasteCategoryImpactAnswerID = mainActivity.ImpactAnswer.MEMBER_CASTE_SC;
                break;
            case 2:
                CasteCategoryImpactAnswerID = mainActivity.ImpactAnswer.MEMBER_CASTE_NT_ST;
                break;
            case 3:
                CasteCategoryImpactAnswerID = mainActivity.ImpactAnswer.MEMBER_CASTE_OBC;
                break;
            case 4:
                CasteCategoryImpactAnswerID = mainActivity.ImpactAnswer.MEMBER_CASTE_BC_SBC_BSC;
                break;
            case 5:
                CasteCategoryImpactAnswerID = mainActivity.ImpactAnswer.MEMBER_CASTE_VJNT;
                break;
            case 6:
                CasteCategoryImpactAnswerID = mainActivity.ImpactAnswer.MEMBER_CASTE_VJNT;
                break;
            case 7:
                CasteCategoryImpactAnswerID = mainActivity.ImpactAnswer.MEMBER_CASTE_OPEN;
                break;
        }

        if(indexMemberCaste < 0) {
            clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
            tnewAnswer.Init_MemberImpactAnswer();
            mainActivity.tMemberImpactAnswer.add(tnewAnswer);

            clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
            newAnswer.Init_MemberImpactAnswer();
            newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
            newAnswer.IPAnswerID = CasteCategoryImpactAnswerID;
            mainActivity.MemberImpactAnswer.add(newAnswer);
        } else if(mainActivity.MemberImpactAnswer.get(indexMemberCaste).IPAnswerID != CasteCategoryImpactAnswerID) {
            mainActivity.MemberImpactAnswer.get(indexMemberCaste).IsActive = 0;

            clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
            tnewAnswer.Init_MemberImpactAnswer();
            mainActivity.tMemberImpactAnswer.add(tnewAnswer);

            clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
            newAnswer.Init_MemberImpactAnswer();
            newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
            newAnswer.IPAnswerID = CasteCategoryImpactAnswerID;
            mainActivity.MemberImpactAnswer.add(newAnswer);
        }

        int educationScore = 0;
        for (int i = 0; i < mainActivity.PersonalImpactInfo.size(); i++) {
            if(mainActivity.PersonalImpactInfo.get(i).IsActive == 0)
                continue;
            if(     (mainActivity.PersonalImpactInfo.get(i).BusinessID == ProfessionIDStudent) &&
                    (       (mainActivity.Member.get(i).RelationID == RelationIDDaughter) ||
                            (mainActivity.Member.get(i).RelationID == RelationIDSon)) ) {
                    //Student Daughter or Student Son
                switch ((int)mainActivity.PersonalImpactInfo.get(i).QualificationID) {
                    case 1:
                        if(educationScore < 2)
                            educationScore = 2;
                        break;
                    case 2:
                        if(educationScore < 3)
                            educationScore = 3;
                        break;
                    case 3:
                        if(educationScore < 2)
                            educationScore = 2;
                        break;
//                        case 4:
//                            break;
                    case 5:
                        if(educationScore < 1)
                            educationScore = 1;
                        break;
                    case 6:
                        if(educationScore < 5)
                            educationScore = 5;
                        break;
//                        case 7:
//                            break;
//                        case 8:
//                            break;
                    case 9:
                        if(educationScore < 5)
                            educationScore = 5;
                        break;
                    case 10:
                        if(educationScore < 5)
                            educationScore = 5;
                        break;
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                        if(educationScore < 3)
                            educationScore = 3;
                        break;
                }
            }
        }
        long FamilyChildrenEducationImpactAnswerID = mainActivity.ImpactAnswer.FAMILY_CHILDREN_EDUCATION_NOT_GOING_TO_SCHOOL;
        switch (educationScore) {
            case 0:
                FamilyChildrenEducationImpactAnswerID = mainActivity.ImpactAnswer.FAMILY_CHILDREN_EDUCATION_NOT_GOING_TO_SCHOOL;
                break;
            case 1:
                FamilyChildrenEducationImpactAnswerID = mainActivity.ImpactAnswer.FAMILY_CHILDREN_EDUCATION_PRIMARY;
                break;
            case 2:
                FamilyChildrenEducationImpactAnswerID = mainActivity.ImpactAnswer.FAMILY_CHILDREN_EDUCATION_SECONDARY;
                break;
            case 3:
                FamilyChildrenEducationImpactAnswerID = mainActivity.ImpactAnswer.FAMILY_CHILDREN_EDUCATION_GRADUATE;
                break;
            case 5:
                FamilyChildrenEducationImpactAnswerID = mainActivity.ImpactAnswer.FAMILY_CHILDREN_EDUCATION_PG_PROFESSIONAL_COURSES;
                break;
        }
        if(indexEducation < 0) {
            clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
            tnewAnswer.Init_MemberImpactAnswer();
            mainActivity.tMemberImpactAnswer.add(tnewAnswer);

            clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
            newAnswer.Init_MemberImpactAnswer();
            newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
            newAnswer.IPAnswerID = FamilyChildrenEducationImpactAnswerID;
            mainActivity.MemberImpactAnswer.add(newAnswer);
        } else if(mainActivity.MemberImpactAnswer.get(indexEducation).IPAnswerID != FamilyChildrenEducationImpactAnswerID) {
            mainActivity.MemberImpactAnswer.get(indexEducation).IsActive = 0;

            clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
            tnewAnswer.Init_MemberImpactAnswer();
            mainActivity.tMemberImpactAnswer.add(tnewAnswer);

            clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
            newAnswer.Init_MemberImpactAnswer();
            newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
            newAnswer.IPAnswerID = FamilyChildrenEducationImpactAnswerID;
            mainActivity.MemberImpactAnswer.add(newAnswer);
        }

        long MaritalStatusImpactAnswerID = mainActivity.ImpactAnswer.MARITAL_STATUS_UNMARRIED;
        switch ((int) mainActivity.Member.get(0).MaritalStatusID) {
            case 1:
                MaritalStatusImpactAnswerID = mainActivity.ImpactAnswer.MARITAL_STATUS_MARRIED;
                break;
            case 2:
                MaritalStatusImpactAnswerID = mainActivity.ImpactAnswer.MARITAL_STATUS_UNMARRIED;
                break;
            case 3:
            case 4:
            case 5:
            case 6:
                MaritalStatusImpactAnswerID = mainActivity.ImpactAnswer.MARITAL_STATUS_SINGLE;
                break;
            case 7:
                MaritalStatusImpactAnswerID = mainActivity.ImpactAnswer.MARITAL_STATUS_REMARRIED;
                break;
        }
        if(indexMaritalStatus < 0) {
            clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
            tnewAnswer.Init_MemberImpactAnswer();
            mainActivity.tMemberImpactAnswer.add(tnewAnswer);

            clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
            newAnswer.Init_MemberImpactAnswer();
            newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
            newAnswer.IPAnswerID = MaritalStatusImpactAnswerID;
            mainActivity.MemberImpactAnswer.add(newAnswer);
        } else if(mainActivity.MemberImpactAnswer.get(indexMaritalStatus).IPAnswerID != MaritalStatusImpactAnswerID) {
            mainActivity.MemberImpactAnswer.get(indexMaritalStatus).IsActive = 0;

            clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
            tnewAnswer.Init_MemberImpactAnswer();
            mainActivity.tMemberImpactAnswer.add(tnewAnswer);

            clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
            newAnswer.Init_MemberImpactAnswer();
            newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
            newAnswer.IPAnswerID = MaritalStatusImpactAnswerID;
            mainActivity.MemberImpactAnswer.add(newAnswer);
        }

        int familyHabbitLevel = 3;
        for(int i=0; i<mainActivity.PersonalImpactInfo.size(); i++) {
            if(mainActivity.PersonalImpactInfo.get(i).IsActive == 0)
                continue;

            switch ((int) mainActivity.PersonalImpactInfo.get(i).HabitID) {
                case 0:
                case 1:
                    familyHabbitLevel = 0;
                    break;
                case 2:
                    if(familyHabbitLevel > 1)
                        familyHabbitLevel = 1;
                    break;
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                    if(familyHabbitLevel > 2)
                        familyHabbitLevel = 2;
                    break;
            }
        }
        long HabbitImpactAnswerID = mainActivity.ImpactAnswer.BAD_HABBIT_WITHIN_FAMILY_MEMBERS_ALCOHOL;
        switch (familyHabbitLevel) {
            case 0:
                HabbitImpactAnswerID = mainActivity.ImpactAnswer.BAD_HABBIT_WITHIN_FAMILY_MEMBERS_ALCOHOL;
                break;
            case 1:
                HabbitImpactAnswerID = mainActivity.ImpactAnswer.BAD_HABBIT_WITHIN_FAMILY_MEMBERS_SMOKING;
                break;
            case 2:
                HabbitImpactAnswerID = mainActivity.ImpactAnswer.BAD_HABBIT_WITHIN_FAMILY_MEMBERS_TOBBACO_PAN;
                break;
            case 3:
                HabbitImpactAnswerID = mainActivity.ImpactAnswer.BAD_HABBIT_WITHIN_FAMILY_MEMBERS_NONE;
                break;
        }
        if(indexHabbit < 0) {
            clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
            tnewAnswer.Init_MemberImpactAnswer();
            mainActivity.tMemberImpactAnswer.add(tnewAnswer);

            clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
            newAnswer.Init_MemberImpactAnswer();
            newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
            newAnswer.IPAnswerID = HabbitImpactAnswerID;
            mainActivity.MemberImpactAnswer.add(newAnswer);
        } else if(mainActivity.MemberImpactAnswer.get(indexHabbit).IPAnswerID != HabbitImpactAnswerID) {
            mainActivity.MemberImpactAnswer.get(indexHabbit).IsActive = 0;

            clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
            tnewAnswer.Init_MemberImpactAnswer();
            mainActivity.tMemberImpactAnswer.add(tnewAnswer);

            clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
            newAnswer.Init_MemberImpactAnswer();
            newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
            newAnswer.IPAnswerID = HabbitImpactAnswerID;
            mainActivity.MemberImpactAnswer.add(newAnswer);
        }

        long DiseaseWithinFamilyImpactAnswerID = mainActivity.ImpactAnswer.ANY_DISEASE_WITHIN_FAMILY_MEMBERS_NO;
        for(int i=0; i<mainActivity.PersonalImpactInfo.size(); i++) {
            if(mainActivity.PersonalImpactInfo.get(i).IsActive == 0)
                continue;

            if(mainActivity.PersonalImpactInfo.get(i).HealthProblemID != 17) {
                DiseaseWithinFamilyImpactAnswerID = mainActivity.ImpactAnswer.ANY_DISEASE_WITHIN_FAMILY_MEMBERS_YES;
                break;
            }
        }
        if(indexDisease < 0) {
            clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
            tnewAnswer.Init_MemberImpactAnswer();
            mainActivity.tMemberImpactAnswer.add(tnewAnswer);

            clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
            newAnswer.Init_MemberImpactAnswer();
            newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
            newAnswer.IPAnswerID = DiseaseWithinFamilyImpactAnswerID;
            mainActivity.MemberImpactAnswer.add(newAnswer);
        } else if(mainActivity.MemberImpactAnswer.get(indexDisease).IPAnswerID != DiseaseWithinFamilyImpactAnswerID) {
            mainActivity.MemberImpactAnswer.get(indexDisease).IsActive = 0;

            clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
            tnewAnswer.Init_MemberImpactAnswer();
            mainActivity.tMemberImpactAnswer.add(tnewAnswer);

            clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
            newAnswer.Init_MemberImpactAnswer();
            newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
            newAnswer.IPAnswerID = DiseaseWithinFamilyImpactAnswerID;
            mainActivity.MemberImpactAnswer.add(newAnswer);
        }

        long PerCapitaIncomeImpactAnswerID = mainActivity.ImpactAnswer.PER_CAPITA_INCOME_6750_Onward;
        if(mainActivity.FamilyImpactInfo.PerCapitaIncome < 900)
            PerCapitaIncomeImpactAnswerID = mainActivity.ImpactAnswer.PER_CAPITA_INCOME_0_899;
        else if(mainActivity.FamilyImpactInfo.PerCapitaIncome < 1350)
            PerCapitaIncomeImpactAnswerID = mainActivity.ImpactAnswer.PER_CAPITA_INCOME_900_1349;
        else if(mainActivity.FamilyImpactInfo.PerCapitaIncome < 1800)
            PerCapitaIncomeImpactAnswerID = mainActivity.ImpactAnswer.PER_CAPITA_INCOME_1350_1799;
        else if(mainActivity.FamilyImpactInfo.PerCapitaIncome < 2250)
            PerCapitaIncomeImpactAnswerID = mainActivity.ImpactAnswer.PER_CAPITA_INCOME_1800_2249;
        else if(mainActivity.FamilyImpactInfo.PerCapitaIncome < 3375)
            PerCapitaIncomeImpactAnswerID = mainActivity.ImpactAnswer.PER_CAPITA_INCOME_2250_3374;
        else if(mainActivity.FamilyImpactInfo.PerCapitaIncome < 3938)
            PerCapitaIncomeImpactAnswerID = mainActivity.ImpactAnswer.PER_CAPITA_INCOME_3375_3937;
        else if(mainActivity.FamilyImpactInfo.PerCapitaIncome < 4500)
            PerCapitaIncomeImpactAnswerID = mainActivity.ImpactAnswer.PER_CAPITA_INCOME_3938_4499;
        else if(mainActivity.FamilyImpactInfo.PerCapitaIncome < 5625)
            PerCapitaIncomeImpactAnswerID = mainActivity.ImpactAnswer.PER_CAPITA_INCOME_4500_5624;
        else if(mainActivity.FamilyImpactInfo.PerCapitaIncome < 6750)
            PerCapitaIncomeImpactAnswerID = mainActivity.ImpactAnswer.PER_CAPITA_INCOME_5625_6749;

        if(indexPerCapitaIncome < 0) {
            clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
            tnewAnswer.Init_MemberImpactAnswer();
            mainActivity.tMemberImpactAnswer.add(tnewAnswer);

            clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
            newAnswer.Init_MemberImpactAnswer();
            newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
            newAnswer.IPAnswerID = PerCapitaIncomeImpactAnswerID;
            mainActivity.MemberImpactAnswer.add(newAnswer);
        } else if(mainActivity.MemberImpactAnswer.get(indexPerCapitaIncome).IPAnswerID != PerCapitaIncomeImpactAnswerID) {
            mainActivity.MemberImpactAnswer.get(indexPerCapitaIncome).IsActive = 0;

            clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
            tnewAnswer.Init_MemberImpactAnswer();
            mainActivity.tMemberImpactAnswer.add(tnewAnswer);

            clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
            newAnswer.Init_MemberImpactAnswer();
            newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
            newAnswer.IPAnswerID = PerCapitaIncomeImpactAnswerID;
            mainActivity.MemberImpactAnswer.add(newAnswer);
        }

        long AdditionalIncomeImpactAnswerID = mainActivity.ImpactAnswer.ADDITIONAL_INCOME_FROM_VILLAGE_NO;
        if(mainActivity.PersonalImpactInfo.get(0).YearlyIncome > 0)
            AdditionalIncomeImpactAnswerID = mainActivity.ImpactAnswer.ADDITIONAL_INCOME_FROM_VILLAGE_YES;
        if(indexAdditionalIncomeFromVillage < 0) {
            clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
            tnewAnswer.Init_MemberImpactAnswer();
            mainActivity.tMemberImpactAnswer.add(tnewAnswer);

            clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
            newAnswer.Init_MemberImpactAnswer();
            newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
            newAnswer.IPAnswerID = AdditionalIncomeImpactAnswerID;
            mainActivity.MemberImpactAnswer.add(newAnswer);
        } else if(mainActivity.MemberImpactAnswer.get(indexAdditionalIncomeFromVillage).IPAnswerID != AdditionalIncomeImpactAnswerID) {
            mainActivity.MemberImpactAnswer.get(indexAdditionalIncomeFromVillage).IsActive = 0;

            clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
            tnewAnswer.Init_MemberImpactAnswer();
            mainActivity.tMemberImpactAnswer.add(tnewAnswer);

            clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
            newAnswer.Init_MemberImpactAnswer();
            newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
            newAnswer.IPAnswerID = AdditionalIncomeImpactAnswerID;
            mainActivity.MemberImpactAnswer.add(newAnswer);
        }

        mainActivity.ValidateScreenMember();
        for(int i=0; i<mainActivity.PIFProblemsMember.size(); i++) {
            PIFProblems.add(mainActivity.PIFProblemsMember.get(i));
            PIFProblemsSequence.add(mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_MEMBER);
        }
        mainActivity.ValidateScreenFamily();
        for(int i=0; i<mainActivity.PIFProblemsFamily.size(); i++) {
            PIFProblems.add(mainActivity.PIFProblemsFamily.get(i));
            PIFProblemsSequence.add(mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY);
        }
        mainActivity.ValidateScreenIncome();
        for(int i=0; i<mainActivity.PIFProblemsIncome.size(); i++) {
            PIFProblems.add(mainActivity.PIFProblemsIncome.get(i));
            PIFProblemsSequence.add(mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_INCOME);
        }
        mainActivity.ValidateScreenOtherLoan();
        for(int i=0; i<mainActivity.PIFProblemsOtherLoans.size(); i++) {
            PIFProblems.add(mainActivity.PIFProblemsOtherLoans.get(i));
            PIFProblemsSequence.add(mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_OTHER_LOANS);
        }
        mainActivity.ValidateScreenExpenses();
        for(int i=0; i<mainActivity.PIFProblemsExpenses.size(); i++) {
            PIFProblems.add(mainActivity.PIFProblemsExpenses.get(i));
            PIFProblemsSequence.add(mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_EXPENSES);
        }
        mainActivity.ValidateScreenLoan();
        for(int i=0; i<mainActivity.PIFProblemsLoan.size(); i++) {
            PIFProblems.add(mainActivity.PIFProblemsLoan.get(i));
            PIFProblemsSequence.add(mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_LOAN);
        }
        mainActivity.ValidateScreenRepay();
        for(int i=0; i<mainActivity.PIFProblemsRepay.size(); i++) {
            PIFProblems.add(mainActivity.PIFProblemsRepay.get(i));
            PIFProblemsSequence.add(mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_REPAY);
        }
//bmp 26-Mar-24
//        mainActivity.ValidateScreenOtherLoan();
//        for(int i=0; i<mainActivity.PIFProblemsOtherLoans.size(); i++) {
//            PIFProblems.add(mainActivity.PIFProblemsOtherLoans.get(i));
//            PIFProblemsSequence.add(mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_OTHER_LOANS);
//        }
//        mainActivity.ValidateScreenBankAccount();
//        for(int i=0; i<mainActivity.PIFProblemsBankAccount.size(); i++) {
//            PIFProblems.add(mainActivity.PIFProblemsBankAccount.get(i));
//            PIFProblemsSequence.add(mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_BANK_ACCOUNT);
//        }
//        mainActivity.ValidateScreenNominee();
//        for(int i=0; i<mainActivity.PIFProblemsNominee.size(); i++) {
//            PIFProblems.add(mainActivity.PIFProblemsNominee.get(i));
//            PIFProblemsSequence.add(mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE);
//        }
//        mainActivity.ValidateScreenGuarantor();
//        for(int i=0; i<mainActivity.PIFProblemsGuarantor.size(); i++) {
//            PIFProblems.add(mainActivity.PIFProblemsGuarantor.get(i));
//            PIFProblemsSequence.add(mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_GUARANTOR);
//        }
        mainActivity.ValidateScreenGuarantor();
        for(int i=0; i<mainActivity.PIFProblemsGuarantor.size(); i++) {
            PIFProblems.add(mainActivity.PIFProblemsGuarantor.get(i));
            PIFProblemsSequence.add(mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_GUARANTOR);
        }
        mainActivity.ValidateScreenNominee();
        for(int i=0; i<mainActivity.PIFProblemsNominee.size(); i++) {
            PIFProblems.add(mainActivity.PIFProblemsNominee.get(i));
            PIFProblemsSequence.add(mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE);
        }
//        mainActivity.ValidateScreenOtherLoan();
//        for(int i=0; i<mainActivity.PIFProblemsOtherLoans.size(); i++) {
//            PIFProblems.add(mainActivity.PIFProblemsOtherLoans.get(i));
//            PIFProblemsSequence.add(mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_OTHER_LOANS);
//        }
        mainActivity.ValidateScreenBankAccount();
        for(int i=0; i<mainActivity.PIFProblemsBankAccount.size(); i++) {
            PIFProblems.add(mainActivity.PIFProblemsBankAccount.get(i));
            PIFProblemsSequence.add(mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_BANK_ACCOUNT);
        }
//bmp 26-Mar-24
        mainActivity.ValidateScreenHouse();
        for(int i=0; i<mainActivity.PIFProblemsHouse.size(); i++) {
            PIFProblems.add(mainActivity.PIFProblemsHouse.get(i));
            PIFProblemsSequence.add(mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_HOUSE);
        }
        mainActivity.ValidateScreenEconomicState();
        for(int i=0; i<mainActivity.PIFProblemsEconomicState.size(); i++) {
            PIFProblems.add(mainActivity.PIFProblemsEconomicState.get(i));
            PIFProblemsSequence.add(mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_ECONOMIC_STATE);
        }
        mainActivity.ValidateScreenAssets();
        for(int i=0; i<mainActivity.PIFProblemsAssets.size(); i++) {
            PIFProblems.add(mainActivity.PIFProblemsAssets.get(i));
            PIFProblemsSequence.add(mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_ASSETS);
        }
        mainActivity.ValidateScreenHobbies();
        for(int i=0; i<mainActivity.PIFProblemsHobbies.size(); i++) {
            PIFProblems.add(mainActivity.PIFProblemsHobbies.get(i));
            PIFProblemsSequence.add(mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_HOBBIES);
        }
        mainActivity.ValidateScreenDocuments();
        for(int i=0; i<mainActivity.PIFProblemsDocuments.size(); i++) {
            PIFProblems.add(mainActivity.PIFProblemsDocuments.get(i));
            PIFProblemsSequence.add(mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_DOCUMENTS);
        }

        TableLayout tblPIFCheckup = (TableLayout) rootView.findViewById(R.id.tblLayoutPIFCheckup);
        tblPIFCheckup.removeAllViews();
        TextView tvref = (TextView) rootView.findViewById(R.id.textViewPIFCheckup);
        if(PIFProblems.size() == 0) {
            tvref.setText("PIF ठीक आहे.");
        } else {
            Status = false;
            tvref.setText("त्रुटींची पूर्तता करून PIF Submit करा.");
        }

        String[] strPIFscreens = getResources().getStringArray(R.array.PIF_Screen_array);

        for(int k=0; k<(mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_ROLL_OVER-1); k++) {
            boolean firstEntry = true;
            for (int i = 0; i < PIFProblems.size(); i++) {
                if (PIFProblemsSequence.get(i) != k)
                    continue;
                else if(firstEntry) {
                    firstEntry = false;

                    TextView tvScreen = new TextView(getActivity().getBaseContext());
                    tvScreen.setTextSize(30);
                    tvScreen.setBackgroundColor(Color.CYAN);
                    tvScreen.setTextColor(tvref.getTextColors());
                    tvScreen.setTypeface(Typeface.DEFAULT_BOLD);
                    tvScreen.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            1f));
                    tvScreen.setGravity(Gravity.CENTER_VERTICAL);
                    tvScreen.setText(strPIFscreens[k]);

                    TableRow tblRow = new TableRow(getActivity().getBaseContext());
                    tblRow.setPadding(5, 5, 5, 5);
                    tblRow.addView(tvScreen);
                    tblPIFCheckup.addView(tblRow);
                }

                TextView tv = new TextView(getActivity().getBaseContext());
                tv.setTextSize(30);
                tv.setBackgroundColor(Color.WHITE);
                tv.setTextColor(tvref.getTextColors());
                tv.setTypeface(Typeface.DEFAULT_BOLD);
                tv.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1f));
                tv.setGravity(Gravity.CENTER_VERTICAL);
                tv.setText(PIFProblems.get(i));

                TableRow tblRow = new TableRow(getActivity().getBaseContext());
                tblRow.setPadding(5, 5, 5, 5);
                tblRow.addView(tv);
                tblRow.setId(i);
                tblRow.setEnabled(true);
                tblRow.setClickable(true);
                tblRow.setOnClickListener(SubmitPIFOnClickListener);

                tblPIFCheckup.addView(tblRow);
            }
        }

        return Status;
    }

    private View.OnClickListener SubmitPIFOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            if (selectedQueryPosition >= 0) {
                View vPrev = (View) rootView.findViewById(selectedQueryPosition);
                vPrev.setBackgroundColor(Color.WHITE);
            }
            selectedQueryPosition = v.getId();
            v.setBackgroundColor(Color.RED);

            int iTemp = PIFProblemsSequence.get(selectedQueryPosition);
            System.out.println(String.valueOf(iTemp));

            mainActivity.iCurrentScreen = PIFProblemsSequence.get(selectedQueryPosition) + 2;
            Button bt = (Button) mainActivity.findViewById(R.id.buttonPrevious);
            bt.performClick();
            bt = (Button) mainActivity.findViewById(R.id.buttonPrevious);
            bt.performClick();
        }
    };

    boolean isDocumentPhotoAvailable(long memberID, long docID) {
        for(int i=0; i<mainActivity.MemberDocRcvd.size(); i++) {
            if ((mainActivity.MemberDocRcvd.get(i).IsActive == 1) &&
                    (mainActivity.MemberDocRcvd.get(i).MemberID == memberID) &&
                    (mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == docID)) {
                for (int k = 0; k < mainActivity.DocumentPhotos.size(); k++) {
                    if ((mainActivity.MemberDocRcvd.get(i).MemberDocuemntsRcvdID ==
                            mainActivity.DocumentPhotos.get(k).MemberDocuemntsRcvdID) &&
                            (mainActivity.DocumentPhotos.get(k).IsActive == 1)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
//bmp 07-Sep-22
    void DeactivateDuplicateImpactAnswers() {
        for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
            if (mainActivity.MemberImpactAnswer.get(i).IsActive == 1) {
                long answerID1 = mainActivity.MemberImpactAnswer.get(i).IPAnswerID;
                String strTimestamp1 = mainActivity.MemberImpactAnswer.get(i).ImpactEncodedDate;
                long lTimeStamp1 = 0L;
                Calendar recordTimeStamp1 = Calendar.getInstance();
                try {
                    recordTimeStamp1.set(Calendar.YEAR, Integer.parseInt(strTimestamp1.substring(0, 4)));
                    recordTimeStamp1.set(Calendar.MONTH, (Integer.parseInt(strTimestamp1.substring(5, 7)) - 1));
                    recordTimeStamp1.set(Calendar.DATE, Integer.parseInt(strTimestamp1.substring(8, 10)));
                    recordTimeStamp1.set(Calendar.HOUR, Integer.parseInt(strTimestamp1.substring(11, 13)));
                    recordTimeStamp1.set(Calendar.MINUTE, Integer.parseInt(strTimestamp1.substring(14, 16)));
                    recordTimeStamp1.set(Calendar.SECOND, Integer.parseInt(strTimestamp1.substring(17, 19)));
                }catch (Exception e){
                }
                lTimeStamp1 = recordTimeStamp1.getTimeInMillis();
                for (int j = (i+1); j < mainActivity.MemberImpactAnswer.size(); j++) {
                    if (mainActivity.MemberImpactAnswer.get(j).IsActive == 1) {
                        if (mainActivity.MemberImpactAnswer.get(j).IPAnswerID == answerID1) {
                            String strTimestamp2 = mainActivity.MemberImpactAnswer.get(j).ImpactEncodedDate;
                            long lTimeStamp2 = 0L;
                            Calendar recordTimeStamp2 = Calendar.getInstance();
                            try {
                                recordTimeStamp2.set(Calendar.YEAR, Integer.parseInt(strTimestamp1.substring(0, 4)));
                                recordTimeStamp2.set(Calendar.MONTH, (Integer.parseInt(strTimestamp1.substring(5, 7)) - 1));
                                recordTimeStamp2.set(Calendar.DATE, Integer.parseInt(strTimestamp1.substring(8, 10)));
                                recordTimeStamp2.set(Calendar.HOUR, Integer.parseInt(strTimestamp1.substring(11, 13)));
                                recordTimeStamp2.set(Calendar.MINUTE, Integer.parseInt(strTimestamp1.substring(14, 16)));
                                recordTimeStamp2.set(Calendar.SECOND, Integer.parseInt(strTimestamp1.substring(17, 19)));
                            } catch (Exception e) {
                            }
                            lTimeStamp2 = recordTimeStamp2.getTimeInMillis();
                            if (lTimeStamp1 >= lTimeStamp2) {
                                mainActivity.MemberImpactAnswer.get(j).IsActive = 0;
                            } else {
                                mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
//bmp 07-Sep-22
    void DeactivateDocumentsWithoutPhoto() {
        for(int i=0; i<mainActivity.MemberDocRcvd.size(); i++) {
            if(mainActivity.MemberDocRcvd.get(i).IsActive == 1) {
                int iDocumentPhotoAvailable = 0;
                for (int k = 0; k < mainActivity.DocumentPhotos.size(); k++) {
                    if ((mainActivity.MemberDocRcvd.get(i).MemberDocuemntsRcvdID ==
                            mainActivity.DocumentPhotos.get(k).MemberDocuemntsRcvdID) &&
                            (mainActivity.DocumentPhotos.get(k).IsActive == 1)) {
                        iDocumentPhotoAvailable = 1;
                        break;
                    }
                }
                mainActivity.MemberDocRcvd.get(i).IsActive = iDocumentPhotoAvailable;
//bmp 25-Jul-24
//                if(     (mainActivity.MemberDocRcvd.get(i).IsActive == 0) &&
//                        (mainActivity.MemberDocRcvd.get(i).MemberDocumentsID ==
//                                mainActivity.DocumentIDs.MEMBER_DOC_ID_RATIONCARD) ) {
//                    mainActivity.Member.get(0).RationCard_Fname = "NA";
//                    mainActivity.Member.get(0).RationCard_Mname = "NA";
//                    mainActivity.Member.get(0).RationCard_Lname = "NA";
//                    mainActivity.MemberFamilyDetails.RationCardNo = "NA";
//                    mainActivity.MemberFamilyDetails.RationCardType = "White";
//                    mainActivity.MemberFamilyDetails.AddressOnRationcard = "NA";
//                }
//bmp 25-Jul-24
                if(mainActivity.MemberDocRcvd.get(i).IsActive == 0) {
                    System.out.println("Document entry Deactivated as Photo Not Available");
                }
            }
        }
//bmp 25-Jul-24
        boolean flagIsActive = false;
        for(int i=0; i<mainActivity.MemberDocRcvd.size(); i++) {
            if(     (mainActivity.MemberDocRcvd.get(i).IsActive == 1) &&
                    (mainActivity.MemberDocRcvd.get(i).MemberDocumentsID ==
                            mainActivity.DocumentIDs.MEMBER_DOC_ID_RATIONCARD) ) {
                mainActivity.MemberFamilyDetails.RationCardNo = mainActivity.MemberDocRcvd.get(i).DocumentNo;
                flagIsActive = true;
                break;
            }
        }
        if(!flagIsActive) {
            mainActivity.Member.get(0).RationCard_Fname = "NA";
            mainActivity.Member.get(0).RationCard_Mname = "NA";
            mainActivity.Member.get(0).RationCard_Lname = "NA";
            mainActivity.MemberFamilyDetails.RationCardNo = "NA";
            mainActivity.MemberFamilyDetails.RationCardType = "White";
            mainActivity.MemberFamilyDetails.AddressOnRationcard = "NA";
        }
//bmp 25-Jul-24
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            if(mainActivity == null)
                return;

            mainActivity.currentMemberIndex = 0;
            mainActivity.FragmentVisibleToUser = mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_SUBMIT;
            RefreshScreen();
        }
    }

    public void RefreshScreen() {
        if (mainActivity.Member.size() <= 0)
            return;
        if (!ValidatePIF())
            return;
        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxLastLoanRemarks);
        if(!cb.isChecked())
            return;
        else {
            Button btn = (Button) rootView.findViewById(R.id.buttonSubmitPIF);
            btn.setEnabled(true);
        }
    }
    public void SQLite_getRelationMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.RelationMaster.COLUMN_NAME_RelationID,
                ufinDbContract.RelationMaster.COLUMN_NAME_Description,
        };
        String Active = "1";

        String selection1 = ufinDbContract.RelationMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";

        // How you want the results sorted in the resulting Cursor
//        String sortOrder1 = ufinDbContract.RelationMaster.COLUMN_NAME_SequenceID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.RelationMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

//        RelationIDMaster.clear();
//        RelationMarathiDescriptionMaster.clear();
//        RelationIDMaster.add(0);
//        RelationMarathiDescriptionMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            String str2 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.RelationMaster.COLUMN_NAME_Description));
                            if(str2.equalsIgnoreCase("Daughter")) {
                                RelationIDDaughter = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.RelationMaster.COLUMN_NAME_RelationID)));
                            } else if(str2.equalsIgnoreCase("Son")) {
                                RelationIDSon = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.RelationMaster.COLUMN_NAME_RelationID)));
                            }
                            c1.moveToNext();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            if(c1 != null) { c1.close(); }
            db.close();
        }
    }
    public void SQLite_getBusinessMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.BusinessMaster.COLUMN_NAME_BusinessID,
                ufinDbContract.BusinessMaster.COLUMN_NAME_Description,
        };
        String Active = "1";

        String selection1 = ufinDbContract.BusinessMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";

        // How you want the results sorted in the resulting Cursor
//        String sortOrder1 = ufinDbContract.BusinessMaster.COLUMN_NAME_SequenceID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.BusinessMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

//        BusinessIDMaster.clear();
//        BusinessMarathiDescriptionMaster.clear();
//        BusinessIDMaster.add(0);
//        BusinessMarathiDescriptionMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            String str2 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.BusinessMaster.COLUMN_NAME_Description));
                            if(str2.equalsIgnoreCase("Student")) {
                                ProfessionIDStudent = Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.BusinessMaster.COLUMN_NAME_BusinessID)));
                                break;
                            }
                            c1.moveToNext();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } finally {
            if(c1 != null) { c1.close(); }
            db.close();
        }
    }
}
