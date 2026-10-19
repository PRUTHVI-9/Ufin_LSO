package com.example.ufin_lso;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class IncomeFragment extends Fragment {
    View rootView;
    MainActivity mainActivity;
    int iFamilySize = 1;
    int iFamilyMonthlyIncome = 0;
    int iEffectiveMonthlyIncome = 0;

    public IncomeFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_income, container, false);
        mainActivity = (MainActivity) getActivity();

        iEffectiveMonthlyIncome = mainActivity.PersonalImpactInfo.get(0).YearlyIncome / 12;
        iFamilySize = 0;
        iFamilyMonthlyIncome = 0;
        for(int i=0; i<mainActivity.Member.size(); i++) {
            if(mainActivity.Member.get(i).IsActive == 1) {
//bmp 23-Dec-24
                if(mainActivity.PersonalImpactInfo.size() <= i) {
                    clsPersonalImpactInfo newInfo = new clsPersonalImpactInfo();
                    newInfo.Init_PersonalImpactInfo();
                    newInfo.MemberID = mainActivity.Member.get(i).MemberID;
                    mainActivity.PersonalImpactInfo.add(newInfo);
                    mainActivity.tPersonalImpactInfo.add(newInfo);
                }
//bmp 23-Dec-24
                iFamilyMonthlyIncome += mainActivity.PersonalImpactInfo.get(i).MonthlyIncome;
                iFamilySize++;
            }
        }
        mainActivity.FamilyImpactInfo.FamilyTotalIncome =
                iEffectiveMonthlyIncome + iFamilyMonthlyIncome + mainActivity.PersonalImpactInfo.get(0).OtherMonthlyIncome;

        EditText et = (EditText) rootView.findViewById(R.id.editTextAnnualIncome);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    try {
                        mainActivity.PersonalImpactInfo.get(0).YearlyIncome = Integer.parseInt(((EditText) v).getText().toString());
                    } catch (NumberFormatException e) {
                        mainActivity.PersonalImpactInfo.get(0).YearlyIncome = 0;
                    }
                    RefreshScreen();
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    if (mainActivity.iCurrentScreen !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_INCOME)
                        return;
                    try {
                        mainActivity.PersonalImpactInfo.get(0).YearlyIncome = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        mainActivity.PersonalImpactInfo.get(0).YearlyIncome = 0;
                    }
                    RefreshScreen();
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextOtherIncome);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    try {
                        mainActivity.PersonalImpactInfo.get(0).OtherMonthlyIncome = Integer.parseInt(v.getText().toString());
                        RefreshScreen();
                    } catch (NumberFormatException e) {
                    }
                }

                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    if (mainActivity.iCurrentScreen !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_INCOME)
                        return;
                    try {
                        mainActivity.PersonalImpactInfo.get(0).OtherMonthlyIncome = Integer.parseInt(((EditText) view).getText().toString());
                        RefreshScreen();
                    } catch (NumberFormatException e) {
                    }
                }
            }
        });

        return rootView;
    }

    public void RefreshScreen() {
        //Update Calculations
        iEffectiveMonthlyIncome = mainActivity.PersonalImpactInfo.get(0).YearlyIncome / 12;

        iFamilySize = 0;
        iFamilyMonthlyIncome = 0;
        for(int i=0; i<mainActivity.Member.size(); i++) {
            if(mainActivity.Member.get(i).IsActive == 1) {
                iFamilyMonthlyIncome += mainActivity.PersonalImpactInfo.get(i).MonthlyIncome;
                iFamilySize++;
            }
        }
        mainActivity.FamilyImpactInfo.FamilyTotalIncome =
                iEffectiveMonthlyIncome + iFamilyMonthlyIncome + mainActivity.PersonalImpactInfo.get(0).OtherMonthlyIncome;
        if(iFamilySize > 0) {
            mainActivity.FamilyImpactInfo.PerCapitaIncome = mainActivity.FamilyImpactInfo.FamilyTotalIncome / iFamilySize;
        } else {
            mainActivity.FamilyImpactInfo.PerCapitaIncome = 0;
        }
        //Update screen
        TextView tv = (TextView) rootView.findViewById(R.id.editTextAnnualIncome);
        if(mainActivity.PersonalImpactInfo.get(0).YearlyIncome > 0) {
            tv.setText(String.valueOf(mainActivity.PersonalImpactInfo.get(0).YearlyIncome));
        } else {
            tv.setText("");
        }
        tv = (TextView) rootView.findViewById(R.id.editTextEffectiveMonthly);
        tv.setText(String.valueOf(iEffectiveMonthlyIncome));

        tv = (TextView) rootView.findViewById(R.id.editTextMonthlyIncome);
        tv.setText(String.valueOf(iFamilyMonthlyIncome));

        tv = (TextView) rootView.findViewById(R.id.editTextOtherIncome);
        if(mainActivity.PersonalImpactInfo.get(0).OtherMonthlyIncome > 0) {
            tv.setText(String.valueOf(mainActivity.PersonalImpactInfo.get(0).OtherMonthlyIncome));
        } else {
            tv.setText("");
        }

        tv = (TextView) rootView.findViewById(R.id.editTextTotalIncome);
        tv.setText(String.valueOf(mainActivity.FamilyImpactInfo.FamilyTotalIncome));

        tv = (TextView) rootView.findViewById(R.id.editTextPerCapitaIncome);
        tv.setText(String.valueOf(mainActivity.FamilyImpactInfo.PerCapitaIncome));

        tv = (TextView) rootView.findViewById(R.id.textViewRequestFocus);
        tv.requestFocus();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            if(mainActivity == null)
                return;

            mainActivity.currentMemberIndex = 0;
            mainActivity.FragmentVisibleToUser = mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_INCOME;
            RefreshScreen();
        }
    }
}
