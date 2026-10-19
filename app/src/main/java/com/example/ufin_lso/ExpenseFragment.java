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

public class ExpenseFragment extends Fragment {
    View rootView;
    MainActivity mainActivity;

    public ExpenseFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_expense, container, false);
        mainActivity = (MainActivity) getActivity();

        EditText et = (EditText) rootView.findViewById(R.id.editTextExpenseRent);
        if( (mainActivity.LSO_ID == 0) || (mainActivity.PersonalImpactInfo.get(0).OwnHouse == 1) ) {
            et.setEnabled(false);
//bmp 13-Jun-24
            if(mainActivity.PersonalImpactInfo.get(0).OwnHouse == 1)
                mainActivity.FamilyImpactInfo.HouseRentExpense = 0;
//bmp 13-Jun-24
        } else {
            et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        try {
                            mainActivity.FamilyImpactInfo.HouseRentExpense = Integer.parseInt(v.getText().toString());
                        } catch (NumberFormatException e) {
                            mainActivity.FamilyImpactInfo.HouseRentExpense = 0;
                        }
                        UpdateTotalExpense();
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
                                mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_EXPENSES)
                            return;
                        try {
                            mainActivity.FamilyImpactInfo.HouseRentExpense = Integer.parseInt(((EditText) view).getText().toString());
                        } catch (NumberFormatException e) {
                            mainActivity.FamilyImpactInfo.HouseRentExpense = 0;
                        }
                        UpdateTotalExpense();
                    }
                }
            });
        }
        et = (EditText) rootView.findViewById(R.id.editTextExpenseGrocery);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    try {
                        mainActivity.FamilyImpactInfo.FoodExpense = Integer.parseInt(v.getText().toString());
                    } catch (NumberFormatException e) {
                        mainActivity.FamilyImpactInfo.FoodExpense = 0;
                    }
                    UpdateTotalExpense();
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_EXPENSES)
                        return;
                    try {
                        mainActivity.FamilyImpactInfo.FoodExpense = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        mainActivity.FamilyImpactInfo.FoodExpense = 0;
                    }
                    UpdateTotalExpense();
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextExpenseEducation);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    try {
                        mainActivity.FamilyImpactInfo.EducationExpense = Integer.parseInt(v.getText().toString());
                    } catch (NumberFormatException e) {
                        mainActivity.FamilyImpactInfo.EducationExpense = 0;
                    }
                    UpdateTotalExpense();
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_EXPENSES)
                        return;
                    try {
                        mainActivity.FamilyImpactInfo.EducationExpense = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        mainActivity.FamilyImpactInfo.EducationExpense = 0;
                    }
                    UpdateTotalExpense();
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextExpenseMedicine);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    try {
                        mainActivity.MemberFamilyDetails.MedicalExpenses = Integer.parseInt(v.getText().toString());
                    } catch (NumberFormatException e) {
                        mainActivity.MemberFamilyDetails.MedicalExpenses = 0;
                    }
                    UpdateTotalExpense();
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_EXPENSES)
                        return;
                    try {
                        mainActivity.MemberFamilyDetails.MedicalExpenses = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        mainActivity.MemberFamilyDetails.MedicalExpenses = 0;
                    }
                    UpdateTotalExpense();
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextExpenseOther);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    try {
                        mainActivity.FamilyImpactInfo.OtherExpenses = Integer.parseInt(v.getText().toString());
                    } catch (NumberFormatException e) {
                        mainActivity.FamilyImpactInfo.OtherExpenses = 0;
                    }
                    UpdateTotalExpense();
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_EXPENSES)
                        return;
                    try {
                        mainActivity.FamilyImpactInfo.OtherExpenses = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        mainActivity.FamilyImpactInfo.OtherExpenses = 0;
                    }
                    UpdateTotalExpense();
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextExpenseMonthlyBhishi);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    try {
                        mainActivity.MemberFamilyDetails.MonthlyBhishi = Integer.parseInt(v.getText().toString());
                    } catch (NumberFormatException e) {
                        mainActivity.MemberFamilyDetails.MonthlyBhishi = 0;
                    }
                    UpdateTotalExpense();
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_EXPENSES)
                        return;
                    try {
                        mainActivity.MemberFamilyDetails.MonthlyBhishi = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        mainActivity.MemberFamilyDetails.MonthlyBhishi = 0;
                    }
                    UpdateTotalExpense();
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextExpenseOtherMFIInstallments);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    try {
                        mainActivity.MemberFamilyDetails.MonthlyInstallments = Integer.parseInt(v.getText().toString());
                    } catch (NumberFormatException e) {
                        mainActivity.MemberFamilyDetails.MonthlyInstallments = 0;
                    }
                    UpdateTotalExpense();
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_EXPENSES)
                        return;
                    try {
                        mainActivity.MemberFamilyDetails.MonthlyInstallments = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                        mainActivity.MemberFamilyDetails.MonthlyInstallments = 0;
                    }
                    UpdateTotalExpense();
                }
            }
        });

        return rootView;
    }

    public void UpdateTotalExpense() {
        mainActivity.ExpenseTotal =
                mainActivity.FamilyImpactInfo.HouseRentExpense +
                        mainActivity.FamilyImpactInfo.FoodExpense +
                        mainActivity.FamilyImpactInfo.EducationExpense +
                        mainActivity.MemberFamilyDetails.MedicalExpenses +
                        mainActivity.FamilyImpactInfo.OtherExpenses +
                        mainActivity.MemberFamilyDetails.MonthlyBhishi +
                        mainActivity.MemberFamilyDetails.MonthlyInstallments;
//        for(int i=0; i<mainActivity.OtherMFIData.size(); i++) {
//            mainActivity.ExpenseTotal = mainActivity.ExpenseTotal + mainActivity.OtherMFIData.get(i).MonthlyInstallment;
//        }
        TextView tv = (TextView) rootView.findViewById(R.id.editTextExpenseTotal);
        tv.setText(String.valueOf(mainActivity.ExpenseTotal));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            if(mainActivity == null)
                return;

            mainActivity.currentMemberIndex = 0;
            mainActivity.FragmentVisibleToUser = mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_EXPENSES;
            RefreshScreen();
        }
    }

    public void RefreshScreen() {
        TextView tv = (TextView) rootView.findViewById(R.id.editTextExpenseRent);
//bmp 13-Jun-24
        if(mainActivity.PersonalImpactInfo.get(0).OwnHouse == 1)
            mainActivity.FamilyImpactInfo.HouseRentExpense = 0;
//bmp 13-Jun-24
        if(mainActivity.FamilyImpactInfo.HouseRentExpense > 0) {
            tv.setText(String.valueOf(mainActivity.FamilyImpactInfo.HouseRentExpense));
        } else {
            tv.setText("");
        }

        tv = (TextView) rootView.findViewById(R.id.editTextExpenseGrocery);
        if(mainActivity.FamilyImpactInfo.FoodExpense > 0) {
            tv.setText(String.valueOf(mainActivity.FamilyImpactInfo.FoodExpense));
        } else {
            tv.setText("");
        }

        tv = (TextView) rootView.findViewById(R.id.editTextExpenseEducation);
        if(mainActivity.FamilyImpactInfo.EducationExpense > 0) {
            tv.setText(String.valueOf(mainActivity.FamilyImpactInfo.EducationExpense));
        } else {
            tv.setText("");
        }

        tv = (TextView) rootView.findViewById(R.id.editTextExpenseMedicine);
        if(mainActivity.MemberFamilyDetails.MedicalExpenses > 0) {
            tv.setText(String.valueOf(mainActivity.MemberFamilyDetails.MedicalExpenses));
        } else {
            tv.setText("");
        }

        tv = (TextView) rootView.findViewById(R.id.editTextExpenseOther);
        if(mainActivity.FamilyImpactInfo.OtherExpenses > 0) {
            tv.setText(String.valueOf(mainActivity.FamilyImpactInfo.OtherExpenses));
        } else {
            tv.setText("");
        }

        tv = (TextView) rootView.findViewById(R.id.editTextExpenseMonthlyBhishi);
        if(mainActivity.MemberFamilyDetails.MonthlyBhishi > 0) {
            tv.setText(String.valueOf(mainActivity.MemberFamilyDetails.MonthlyBhishi));
        } else {
            tv.setText("");
        }

        tv = (TextView) rootView.findViewById(R.id.editTextExpenseOtherMFIInstallments);
        if(mainActivity.MemberFamilyDetails.MonthlyInstallments > 0) {
            tv.setText(String.valueOf(mainActivity.MemberFamilyDetails.MonthlyInstallments));
        } else {
            tv.setText("");
        }
//        int iTotal = 0;
//        for(int i=0; i<mainActivity.OtherMFIData.size(); i++) {
//            iTotal = iTotal + mainActivity.OtherMFIData.get(i).MonthlyInstallment;
//        }
//        if(iTotal > 0) {
//            tv.setText(String.valueOf(iTotal));
//        } else {
//            tv.setText("");
//        }

        UpdateTotalExpense();

        tv = (TextView) rootView.findViewById(R.id.textViewRequestFocus);
        tv.requestFocus();
    }

}
