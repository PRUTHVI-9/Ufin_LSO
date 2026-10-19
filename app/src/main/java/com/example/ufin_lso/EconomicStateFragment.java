package com.example.ufin_lso;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class EconomicStateFragment extends Fragment {
    View rootView;
    MainActivity mainActivity;

    public EconomicStateFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_economicstate, container, false);
        mainActivity = (MainActivity) getActivity();

        RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupFamilyHead);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                boolean bUpdated = false;
                int iAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_FAMILYHEAD_MALE;
                if (rb.getId() == R.id.radioButtonHeadMale) {
                    iAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_FAMILYHEAD_MALE;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_FAMILYHEAD_MALE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_FAMILYHEAD_FEMALE)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_FAMILYHEAD_MALE;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonHeadFemale) {
                    iAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_FAMILYHEAD_FEMALE;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_FAMILYHEAD_MALE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_FAMILYHEAD_FEMALE)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_FAMILYHEAD_FEMALE;
                            bUpdated = true;
                        }
                    }
                }
                if(!bUpdated) {
                    clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                    tnewAnswer.Init_MemberImpactAnswer();
                    mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                    clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                    newAnswer.Init_MemberImpactAnswer();
                    newAnswer.IPAnswerID = iAnswerID;
                    newAnswer.IsActive = 1;
                    newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                    mainActivity.MemberImpactAnswer.add(newAnswer);
                }
            }
        });
        rg = (RadioGroup) rootView.findViewById(R.id.radioGroupSalary);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                boolean bUpdated = false;
                int iAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_NO;
                if (rb.getId() == R.id.radioButtonSalaryNone) {
                    iAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_NO;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_NO) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_YES)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_NO;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonSalary1) {
                    iAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_YES;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_NO) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_YES)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_YES;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonSalary2) {
                    iAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_YES;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_NO) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_YES)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_YES;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonSalary3) {
                    iAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_YES;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_NO) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_YES)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_YES;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonSalary4) {
                    iAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_YES;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_NO) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_YES)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_YES;
                            bUpdated = true;
                        }
                    }
                }
                if(!bUpdated) {
                    clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                    tnewAnswer.Init_MemberImpactAnswer();
                    mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                    clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                    newAnswer.Init_MemberImpactAnswer();
                    newAnswer.IPAnswerID = iAnswerID;
                    newAnswer.IsActive = 1;
                    newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                    mainActivity.MemberImpactAnswer.add(newAnswer);
                }
            }
        });
        rg = (RadioGroup) rootView.findViewById(R.id.radioGroupRationcard);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                boolean bUpdated = false;
                int iAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_RATIONCARD_COLOR_YELLOW;
                if (rb.getId() == R.id.radioButtonYellow) {
                    iAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_RATIONCARD_COLOR_YELLOW;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_RATIONCARD_COLOR_YELLOW) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_RATIONCARD_COLOR_ORANGE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_RATIONCARD_COLOR_WHITE)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_RATIONCARD_COLOR_YELLOW;
                            mainActivity.MemberFamilyDetails.RationCardType = "Yellow";
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonOrange) {
                    iAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_RATIONCARD_COLOR_ORANGE;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_RATIONCARD_COLOR_YELLOW) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_RATIONCARD_COLOR_ORANGE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_RATIONCARD_COLOR_WHITE)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_RATIONCARD_COLOR_ORANGE;
                            mainActivity.MemberFamilyDetails.RationCardType = "Orange";
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonWhite) {
                    iAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_RATIONCARD_COLOR_WHITE;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_RATIONCARD_COLOR_YELLOW) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_RATIONCARD_COLOR_ORANGE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_RATIONCARD_COLOR_WHITE)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_RATIONCARD_COLOR_WHITE;
                            mainActivity.MemberFamilyDetails.RationCardType = "White";
                            bUpdated = true;
                        }
                    }
                }
                if(!bUpdated) {
                    clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                    tnewAnswer.Init_MemberImpactAnswer();
                    mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                    clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                    newAnswer.Init_MemberImpactAnswer();
                    newAnswer.IPAnswerID = iAnswerID;
                    newAnswer.IsActive = 1;
                    newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                    mainActivity.MemberImpactAnswer.add(newAnswer);
                }
            }
        });
        CheckBox cb;
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxSavingNone);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxBhishi);
                    cb.setChecked(false);
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxFund);
                    cb.setChecked(false);
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxGroup);
                    cb.setChecked(false);
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxBank);
                    cb.setChecked(false);
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxRDFD);
                    cb.setChecked(false);
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxOtherFI);
                    cb.setChecked(false);
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxAAscheme);
                    cb.setChecked(false);
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxOtherPension);
                    cb.setChecked(false);

                    boolean bUpdated = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated = true;
                        } else if(((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_BHISHI_FUND_GROUP) ||
                                ((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_ANNAPURNA_RD_FD) ||
                                ((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_BANK_OTHER_FI) ||
                                ((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_ANNAPURNA_PENSION) ||
                                ((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_OTHER_PENSION) ) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                    if(!bUpdated) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_NONE;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxBhishi);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxSavingNone);
                    cb.setChecked(false);
                    boolean bUpdated = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_BHISHI_FUND_GROUP) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated = true;
                        }
                    }
                    if(!bUpdated) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_BHISHI_FUND_GROUP;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_BHISHI_FUND_GROUP) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxFund);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxSavingNone);
                    cb.setChecked(false);
                    boolean bUpdated = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_BHISHI_FUND_GROUP) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated = true;
                        }
                    }
                    if(!bUpdated) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_BHISHI_FUND_GROUP;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_BHISHI_FUND_GROUP) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxGroup);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxSavingNone);
                    cb.setChecked(false);
                    boolean bUpdated = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_BHISHI_FUND_GROUP) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated = true;
                        }
                    }
                    if(!bUpdated) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_BHISHI_FUND_GROUP;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_BHISHI_FUND_GROUP) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxBank);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxSavingNone);
                    cb.setChecked(false);
                    boolean bUpdated = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_BANK_OTHER_FI) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated = true;
                        }
                    }
                    if(!bUpdated) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_BANK_OTHER_FI;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_BANK_OTHER_FI) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxRDFD);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxSavingNone);
                    cb.setChecked(false);
                    boolean bUpdated = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_ANNAPURNA_RD_FD) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated = true;
                        }
                    }
                    if(!bUpdated) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_ANNAPURNA_RD_FD;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_ANNAPURNA_RD_FD) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxOtherFI);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxSavingNone);
                    cb.setChecked(false);
                    boolean bUpdated = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_BANK_OTHER_FI) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated = true;
                        }
                    }
                    if(!bUpdated) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_BANK_OTHER_FI;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_BANK_OTHER_FI) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxAAscheme);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxSavingNone);
                    cb.setChecked(false);
                    boolean bUpdated = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_ANNAPURNA_PENSION) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated = true;
                        }
                    }
                    if(!bUpdated) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_ANNAPURNA_PENSION;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_ANNAPURNA_PENSION) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxOtherPension);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxSavingNone);
                    cb.setChecked(false);
                    boolean bUpdated = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_OTHER_PENSION) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated = true;
                        }
                    }
                    if(!bUpdated) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_OTHER_PENSION;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_OTHER_PENSION) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxRationcard);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    boolean bUpdated = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_RATIONCARD) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated = true;
                            break;
                        }
                    }
                    if(!bUpdated) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_RATIONCARD;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_RATIONCARD) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxPassbook);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    boolean bUpdated = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_BANKPASSBOOK) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated = true;
                            break;
                        }
                    }
                    if(!bUpdated) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_BANKPASSBOOK;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_BANKPASSBOOK) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxAadhaarcard);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    boolean bUpdated = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_AADHAARCARD) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated = true;
                            break;
                        }
                    }
                    if(!bUpdated) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_AADHAARCARD;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_AADHAARCARD) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxPancard);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    boolean bUpdated = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_PANCARD) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated = true;
                            break;
                        }
                    }
                    if(!bUpdated) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_PANCARD;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_PANCARD) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxElectioncard);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    boolean bUpdated = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_ELECTIONCARD) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated = true;
                            break;
                        }
                    }
                    if(!bUpdated) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_ELECTIONCARD;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_ELECTIONCARD) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxDrivingLicense);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    boolean bUpdated = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_DRIVINGLICENSE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated = true;
                            break;
                        }
                    }
                    if(!bUpdated) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_DRIVINGLICENSE;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_DRIVINGLICENSE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxElectricBill);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    boolean bUpdated = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_ELECTRICBILL) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated = true;
                            break;
                        }
                    }
                    if(!bUpdated) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_ELECTRICBILL;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_ELECTRICBILL) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxBirthCertificate);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    boolean bUpdated = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_BIRTHCERTIFICATE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated = true;
                            break;
                        }
                    }
                    if(!bUpdated) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_BIRTHCERTIFICATE;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_BIRTHCERTIFICATE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxPropertyTax);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    boolean bUpdated = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_PROPERTYTAX) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated = true;
                            break;
                        }
                    }
                    if(!bUpdated) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_PROPERTYTAX;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_PROPERTYTAX) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            if(mainActivity == null)
                return;

            mainActivity.currentMemberIndex = 0;
            mainActivity.FragmentVisibleToUser = mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_ECONOMIC_STATE;
            RefreshScreen();
        }
    }

    public void RefreshScreen() {

        RadioGroup rg1 = (RadioGroup) rootView.findViewById(R.id.radioGroupFamilyHead);
        RadioGroup rg4 = (RadioGroup) rootView.findViewById(R.id.radioGroupSalary);
        RadioGroup rg5 = (RadioGroup) rootView.findViewById(R.id.radioGroupRationcard);

        CheckBox cb1 = (CheckBox) rootView.findViewById(R.id.checkBoxSavingNone);
        cb1.setChecked(false);
        CheckBox cb2 = (CheckBox) rootView.findViewById(R.id.checkBoxBhishi);
        cb2.setChecked(false);
        CheckBox cb3 = (CheckBox) rootView.findViewById(R.id.checkBoxFund);
        cb3.setChecked(false);
        CheckBox cb4 = (CheckBox) rootView.findViewById(R.id.checkBoxGroup);
        cb4.setChecked(false);
        CheckBox cb5 = (CheckBox) rootView.findViewById(R.id.checkBoxBank);
        cb5.setChecked(false);
        CheckBox cb6 = (CheckBox) rootView.findViewById(R.id.checkBoxRDFD);
        cb6.setChecked(false);
        CheckBox cb7 = (CheckBox) rootView.findViewById(R.id.checkBoxOtherFI);
        cb7.setChecked(false);
        CheckBox cb8 = (CheckBox) rootView.findViewById(R.id.checkBoxAAscheme);
        cb8.setChecked(false);
        CheckBox cb9 = (CheckBox) rootView.findViewById(R.id.checkBoxOtherPension);
        cb9.setChecked(false);

        CheckBox cb10 = (CheckBox) rootView.findViewById(R.id.checkBoxRationcard);
        cb10.setChecked(false);
        CheckBox cb11 = (CheckBox) rootView.findViewById(R.id.checkBoxPassbook);
        cb11.setChecked(false);
        CheckBox cb12 = (CheckBox) rootView.findViewById(R.id.checkBoxAadhaarcard);
        cb12.setChecked(false);
        CheckBox cb13 = (CheckBox) rootView.findViewById(R.id.checkBoxPancard);
        cb13.setChecked(false);
        CheckBox cb14 = (CheckBox) rootView.findViewById(R.id.checkBoxElectioncard);
        cb14.setChecked(false);
        CheckBox cb15 = (CheckBox) rootView.findViewById(R.id.checkBoxDrivingLicense);
        cb15.setChecked(false);
        CheckBox cb16 = (CheckBox) rootView.findViewById(R.id.checkBoxElectricBill);
        cb16.setChecked(false);
        CheckBox cb17 = (CheckBox) rootView.findViewById(R.id.checkBoxBirthCertificate);
        cb17.setChecked(false);
        CheckBox cb18 = (CheckBox) rootView.findViewById(R.id.checkBoxPropertyTax);
        cb18.setChecked(false);

        for(int i=0; i<mainActivity.MemberImpactAnswer.size(); i++) {
            if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_ELECTIONCARD) {
                if (mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb14.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_AADHAARCARD) {
                if (mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb12.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_RATIONCARD) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb10.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_BANKPASSBOOK) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb11.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_ELECTRICBILL) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb16.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_PANCARD) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb13.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_PROPERTYTAX) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb18.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_DRIVINGLICENSE) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb15.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_DOCUMENT_BIRTHCERTIFICATE) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb17.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_FAMILYHEAD_MALE) {
                rg1.check(R.id.radioButtonHeadMale);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_FAMILYHEAD_FEMALE) {
                rg1.check(R.id.radioButtonHeadFemale);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_NO) {
                rg4.check(R.id.radioButtonSalaryNone);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_REGULAR_INCOME_YES) {
                rg4.check(R.id.radioButtonSalary1);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_RATIONCARD_COLOR_YELLOW) {
                rg5.check(R.id.radioButtonYellow);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_RATIONCARD_COLOR_ORANGE) {
                rg5.check(R.id.radioButtonOrange);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_RATIONCARD_COLOR_WHITE) {
                rg5.check(R.id.radioButtonWhite);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_NONE) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb1.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_BHISHI_FUND_GROUP) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1) {
                    cb2.setChecked(true);
                    cb3.setChecked(true);
                    cb4.setChecked(true);
                }
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_BANK_OTHER_FI) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb7.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_OTHER_PENSION) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb9.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_ANNAPURNA_RD_FD) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb6.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ECONOMIC_STATE_SAVINGS_ANNAPURNA_PENSION) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb8.setChecked(true);
            }
        }

        TextView tv = (TextView) rootView.findViewById(R.id.textViewRequestFocus);
        tv.requestFocus();
    }

}
