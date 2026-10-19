package com.example.ufin_lso;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

public class HobbiesFragment extends Fragment {
    View rootView;
    MainActivity mainActivity;

    public HobbiesFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_hobbies, container, false);
        mainActivity = (MainActivity) getActivity();

        RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupHobbies);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                boolean bUpdated = false;
                boolean flagSomeHobby = false;
                int iAnswerID = mainActivity.ImpactAnswer.HOBBIES_NO;
                if (rb.getId() == R.id.radioButtonHobbyNo) {
                    iAnswerID = mainActivity.ImpactAnswer.HOBBIES_NO;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
//bmp 05-Sep-22
//                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_YES) {
                        if(     ((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_YES) ||
                                ((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_NO) ) {
//bmp 05-Sep-22
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.HOBBIES_NO;
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated = true;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            flagSomeHobby = true;
                        } else if(  ((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_WATCHING_TV_SERIALS) ||
                                    ((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_PAPER_READING_NEWS) ||
                                    ((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_OTHER) ) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                            bUpdated = true;
                        }
                    }
                    if (!bUpdated) {
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
                    if(!flagSomeHobby) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.HOBBIES_NONE;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                    CheckBox cb1 = (CheckBox) rootView.findViewById(R.id.checkBoxReadingNews);
                    cb1.setChecked(false);
                    cb1.setEnabled(false);
                    CheckBox cb2 = (CheckBox) rootView.findViewById(R.id.checkBoxWatchingTV);
                    cb2.setChecked(false);
                    cb2.setEnabled(false);
                    CheckBox cb3 = (CheckBox) rootView.findViewById(R.id.checkBoxChitchat);
                    cb3.setChecked(false);
                    cb3.setEnabled(false);
                    CheckBox cb4 = (CheckBox) rootView.findViewById(R.id.checkBoxOtherHobbies);
                    cb4.setChecked(false);
                    cb4.setEnabled(false);
                } else if (rb.getId() == R.id.radioButtonHobbyYes) {
                    flagSomeHobby = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
//bmp 05-Sep-22
//                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_NO) {
                        if(     ((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_NO) ||
                                ((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_YES) ) {
//bmp 05-Sep-22
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.HOBBIES_YES;
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated = true;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        } else if(  ((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_WATCHING_TV_SERIALS) ||
                                ((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_PAPER_READING_NEWS) ||
                                ((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_OTHER) ) {
                            if (mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                                flagSomeHobby = true;
                        }
                    }
                    if (!bUpdated) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.HOBBIES_YES;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                    if (!flagSomeHobby) {
                        bUpdated = false;
                        for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                            if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_OTHER) {
                                mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                                bUpdated = true;
                            }
                        }
                        if (!bUpdated) {
                            clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                            tnewAnswer.Init_MemberImpactAnswer();
                            mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                            clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                            newAnswer.Init_MemberImpactAnswer();
                            newAnswer.IPAnswerID = mainActivity.ImpactAnswer.HOBBIES_OTHER;
                            newAnswer.IsActive = 1;
                            newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                            mainActivity.MemberImpactAnswer.add(newAnswer);
                        }
                        CheckBox cb4 = (CheckBox) rootView.findViewById(R.id.checkBoxOtherHobbies);
                        cb4.setChecked(true);
                    }
                    CheckBox cb1 = (CheckBox) rootView.findViewById(R.id.checkBoxReadingNews);
                    cb1.setEnabled(true);
                    CheckBox cb2 = (CheckBox) rootView.findViewById(R.id.checkBoxWatchingTV);
                    cb2.setEnabled(true);
                    CheckBox cb3 = (CheckBox) rootView.findViewById(R.id.checkBoxChitchat);
                    cb3.setEnabled(true);
                    CheckBox cb4 = (CheckBox) rootView.findViewById(R.id.checkBoxOtherHobbies);
                    cb4.setEnabled(true);
                }
            }
        });
        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxReadingNews);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    boolean bUpdated1 = false;
                    boolean bUpdated2 = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_NO) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.HOBBIES_YES;
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated1 = true;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_PAPER_READING_NEWS) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated2 = true;
                        }
                    }
                    if (!bUpdated1) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.HOBBIES_YES;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                    if (!bUpdated2) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.HOBBIES_PAPER_READING_NEWS;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_PAPER_READING_NEWS) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxWatchingTV);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    boolean bUpdated1 = false;
                    boolean bUpdated2 = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_NO) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.HOBBIES_YES;
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated1 = true;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_WATCHING_TV_SERIALS) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated2 = true;
                        }
                    }
                    if (!bUpdated1) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.HOBBIES_YES;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                    if (!bUpdated2) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.HOBBIES_WATCHING_TV_SERIALS;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_WATCHING_TV_SERIALS) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxChitchat);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    boolean bUpdated1 = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated1 = true;
                            break;
                        }
                    }
                    if (!bUpdated1) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.HOBBIES_NONE;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxOtherHobbies);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    boolean bUpdated1 = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_OTHER) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated1 = true;
                            break;
                        }
                    }
                    if (!bUpdated1) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.HOBBIES_OTHER;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_OTHER) {
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
            mainActivity.FragmentVisibleToUser = mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_HOBBIES;
            RefreshScreen();
        }
    }

    public void RefreshScreen() {

        RadioGroup rg1 = (RadioGroup) rootView.findViewById(R.id.radioGroupHobbies);
        CheckBox cb1 = (CheckBox) rootView.findViewById(R.id.checkBoxReadingNews);
        cb1.setChecked(false);
        CheckBox cb2 = (CheckBox) rootView.findViewById(R.id.checkBoxWatchingTV);
        cb2.setChecked(false);
        CheckBox cb3 = (CheckBox) rootView.findViewById(R.id.checkBoxChitchat);
        cb3.setChecked(false);
        CheckBox cb4 = (CheckBox) rootView.findViewById(R.id.checkBoxOtherHobbies);
        cb4.setChecked(false);

        for(int i=0; i<mainActivity.MemberImpactAnswer.size(); i++) {
            if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_NO) {
                rg1.check(R.id.radioButtonHobbyNo);
                cb1.setEnabled(false);
                cb2.setEnabled(false);
                cb3.setEnabled(false);
                cb4.setEnabled(false);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_YES) {
                rg1.check(R.id.radioButtonHobbyYes);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_NONE) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1) {
                    rg1.check(R.id.radioButtonHobbyNo);
                    cb1.setEnabled(false);
                    cb2.setEnabled(false);
                    cb3.setEnabled(false);
                    cb4.setEnabled(false);
                }
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_WATCHING_TV_SERIALS) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1) {
                    cb2.setChecked(true);
                    rg1.check(R.id.radioButtonHobbyYes);
                }
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_PAPER_READING_NEWS) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1) {
                    cb1.setChecked(true);
                    rg1.check(R.id.radioButtonHobbyYes);
                }
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOBBIES_OTHER) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1) {
                    cb4.setChecked(true);
                    rg1.check(R.id.radioButtonHobbyYes);
                }
            }
        }

        RadioButton rb = (RadioButton) rg1.findViewById(rg1.getCheckedRadioButtonId());
        if(rb != null) {
            if (rb.getId() == R.id.radioButtonHobbyYes) {
                if(!cb1.isChecked() && !cb2.isChecked() && !cb3.isChecked() && !cb4.isChecked()) {
                    cb4.setChecked(true);
                }
            }
        }
    }
}
