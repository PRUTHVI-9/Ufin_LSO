package com.example.ufin_lso;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class OtherLoansFragment extends Fragment {
    View rootView;
    MainActivity mainActivity;

//    private boolean dbMaster = false;
//    private ArrayList<Integer> OtherBorrowingSourcesIDMaster = new ArrayList<Integer>();
//    private ArrayList<String> OtherBorrowingSourcesMarathiDescriptionMaster = new ArrayList<String>();

    static int selectedDeficitFundResource1Position;
    static int selectedDeficitFundResource2Position;

    public OtherLoansFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_otherloans, container, false);
        mainActivity = (MainActivity) getActivity();

//        if(!dbMaster) {
//            SQLite_getOtherBorrowingSourcesMaster();
//            dbMaster = true;
//        }

//        EditText et = (EditText) rootView.findViewById(R.id.editTextLoanAmount1);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    try {
//                        mainActivity.AdditionalDetails.OtherLoanAmount1 = Integer.valueOf(v.getText().toString());
//                    } catch (NumberFormatException e) {
//                        mainActivity.AdditionalDetails.OtherLoanAmount1 = 0;
//                    }
//                }
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    if (mainActivity.iCurrentScreen !=
//                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_OTHER_LOANS)
//                        return;
//                    try {
//                        mainActivity.AdditionalDetails.OtherLoanAmount1 = Integer.valueOf(((EditText) view).getText().toString());
//                    } catch (NumberFormatException e) {
//                        mainActivity.AdditionalDetails.OtherLoanAmount1 = 0;
//                    }
//                }
//            }
//        });
//        et = (EditText) rootView.findViewById(R.id.editTextLoanAmount2);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    try {
//                        mainActivity.AdditionalDetails.OtherLoanAmount2 = Integer.valueOf(v.getText().toString());
//                    } catch (NumberFormatException e) {
//                        mainActivity.AdditionalDetails.OtherLoanAmount2 = 0;
//                    }
//                }
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    if (mainActivity.iCurrentScreen !=
//                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_OTHER_LOANS)
//                        return;
//                    try {
//                        mainActivity.AdditionalDetails.OtherLoanAmount2 = Integer.valueOf(((EditText) view).getText().toString());
//                    } catch (NumberFormatException e) {
//                        mainActivity.AdditionalDetails.OtherLoanAmount2 = 0;
//                    }
//                }
//            }
//        });
//        et = (EditText) rootView.findViewById(R.id.editTextRepayInstallment1);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    try {
//                        mainActivity.AdditionalDetails.OtherLoanInstallment1 = Integer.valueOf(v.getText().toString());
//                    } catch (NumberFormatException e) {
//                        mainActivity.AdditionalDetails.OtherLoanInstallment1 = 0;
//                    }
//                }
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    if (mainActivity.iCurrentScreen !=
//                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_OTHER_LOANS)
//                        return;
//                    try {
//                        mainActivity.AdditionalDetails.OtherLoanInstallment1 = Integer.valueOf(((EditText) view).getText().toString());
//                    } catch (NumberFormatException e) {
//                        mainActivity.AdditionalDetails.OtherLoanInstallment1 = 0;
//                    }
//                }
//            }
//        });
//        et = (EditText) rootView.findViewById(R.id.editTextRepayInstallment2);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    try {
//                        mainActivity.AdditionalDetails.OtherLoanInstallment2 = Integer.valueOf(v.getText().toString());
//                    } catch (NumberFormatException e) {
//                        mainActivity.AdditionalDetails.OtherLoanInstallment2 = 0;
//                    }
//                }
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    if (mainActivity.iCurrentScreen !=
//                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_OTHER_LOANS)
//                        return;
//                    try {
//                        mainActivity.AdditionalDetails.OtherLoanInstallment2 = Integer.valueOf(((EditText) view).getText().toString());
//                    } catch (NumberFormatException e) {
//                        mainActivity.AdditionalDetails.OtherLoanInstallment2 = 0;
//                    }
//                }
//            }
//        });
//        et = (EditText) rootView.findViewById(R.id.editTextRepayMonths1);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    try {
//                        mainActivity.AdditionalDetails.OtherLoanRepayMonths1 = Integer.valueOf(v.getText().toString());
//                    } catch (NumberFormatException e) {
//                        mainActivity.AdditionalDetails.OtherLoanRepayMonths1 = 0;
//                    }
//                }
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    if (mainActivity.iCurrentScreen !=
//                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_OTHER_LOANS)
//                        return;
//                    try {
//                        mainActivity.AdditionalDetails.OtherLoanRepayMonths1 = Integer.valueOf(((EditText) view).getText().toString());
//                    } catch (NumberFormatException e) {
//                        mainActivity.AdditionalDetails.OtherLoanRepayMonths1 = 0;
//                    }
//                }
//            }
//        });
//        et = (EditText) rootView.findViewById(R.id.editTextRepayMonths2);
//        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
//                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
//                    try {
//                        mainActivity.AdditionalDetails.OtherLoanRepayMonths2 = Integer.valueOf(v.getText().toString());
//                    } catch (NumberFormatException e) {
//                        mainActivity.AdditionalDetails.OtherLoanRepayMonths2 = 0;
//                    }
//                }
//                return false;
//            }
//        });
//        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                } else {
//                    if (mainActivity.iCurrentScreen !=
//                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_OTHER_LOANS)
//                        return;
//                    try {
//                        mainActivity.AdditionalDetails.OtherLoanRepayMonths2 = Integer.valueOf(((EditText) view).getText().toString());
//                    } catch (NumberFormatException e) {
//                        mainActivity.AdditionalDetails.OtherLoanRepayMonths2 = 0;
//                    }
//                }
//            }
//        });

//        RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupOtherLoan);
//        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                RadioButton rb = (RadioButton) group.findViewById(checkedId);
//                if (rb.getId() == R.id.radioButtonOtherLoanYes) {
//                    mainActivity.AdditionalDetails.OtherLoan = 1;
//                } else if (rb.getId() == R.id.radioButtonOtherLoanNo) {
//                    mainActivity.AdditionalDetails.OtherLoan = 0;
//                }
//                RefreshScreen();
//            }
//        });
//        ArrayAdapter<String> adapterFundResource = new ArrayAdapter<String>(getActivity().getBaseContext(),
//                R.layout.simple_spinner_item, OtherBorrowingSourcesMarathiDescriptionMaster);
//        adapterFundResource.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
//        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerLoanBankName1);
//        sp.setAdapter(adapterFundResource);
//        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (mainActivity.FragmentVisibleToUser !=
//                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_OTHER_LOANS)
//                    return;
//                if (mainActivity.iCurrentScreen !=
//                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_OTHER_LOANS)
//                    return;
//                if (selectedDeficitFundResource1Position != position) {
//                    mainActivity.AdditionalDetails.OtherLoanBankID1 = OtherBorrowingSourcesIDMaster.get(position);
//                    selectedDeficitFundResource1Position = position;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//        sp = (Spinner) rootView.findViewById(R.id.spinnerLoanBankName2);
//        sp.setAdapter(adapterFundResource);
//        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (mainActivity.FragmentVisibleToUser !=
//                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_OTHER_LOANS)
//                    return;
//                if (mainActivity.iCurrentScreen !=
//                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_OTHER_LOANS)
//                    return;
//                if (selectedDeficitFundResource2Position != position) {
//                    mainActivity.AdditionalDetails.OtherLoanBankID2 = OtherBorrowingSourcesIDMaster.get(position);
//                    selectedDeficitFundResource2Position = position;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });

        RefreshScreen();

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
            mainActivity.FragmentVisibleToUser = mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_OTHER_LOANS;
            RefreshScreen();
        }
    }

    public void RefreshScreen() {
//        boolean allowEdit = true;
//
//        RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupOtherLoan);
//        if(mainActivity.AdditionalDetails.OtherLoan == 1) {
//            rg.check(R.id.radioButtonOtherLoanYes);
//
//            Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerLoanBankName1);
//            sp.setSelection(0);
//            for(int i=0; i<OtherBorrowingSourcesIDMaster.size(); i++) {
//                if(OtherBorrowingSourcesIDMaster.get(i) == mainActivity.AdditionalDetails.OtherLoanBankID1) {
//                    selectedDeficitFundResource1Position = i;
//                    sp.setSelection(i);
//                    break;
//                }
//            }
//            sp.setEnabled(allowEdit);
//
//            sp = (Spinner) rootView.findViewById(R.id.spinnerLoanBankName2);
//            sp.setSelection(0);
//            for(int i=0; i<OtherBorrowingSourcesIDMaster.size(); i++) {
//                if(OtherBorrowingSourcesIDMaster.get(i) == mainActivity.AdditionalDetails.OtherLoanBankID2) {
//                    selectedDeficitFundResource2Position = i;
//                    sp.setSelection(i);
//                    break;
//                }
//            }
//            sp.setEnabled(allowEdit);
//
//            EditText et = (EditText) rootView.findViewById(R.id.editTextLoanAmount1);
//            et.setEnabled(allowEdit);
//            et.setText(String.valueOf(mainActivity.AdditionalDetails.OtherLoanAmount1));
//            et.setText(String.valueOf(mainActivity.OtherMFIData.get(0).LoanAmount));
//            et = (EditText) rootView.findViewById(R.id.editTextLoanAmount2);
//            et.setEnabled(allowEdit);
//            et.setText(String.valueOf(mainActivity.AdditionalDetails.OtherLoanAmount2));
//            et.setText(String.valueOf(mainActivity.OtherMFIData.get(1).LoanAmount));
//            et = (EditText) rootView.findViewById(R.id.editTextLoanAmount3);
//            et.setEnabled(allowEdit);
//            et.setText(String.valueOf(mainActivity.AdditionalDetails.OtherLoanAmount3));
//            et.setText(String.valueOf(mainActivity.OtherMFIData.get(2).LoanAmount));
//
//            et = (EditText) rootView.findViewById(R.id.editTextRepayInstallment1);
//            et.setEnabled(allowEdit);
//            et.setText(String.valueOf(mainActivity.AdditionalDetails.OtherLoanInstallment1));
//            et.setText(String.valueOf(mainActivity.OtherMFIData.get(0).MonthlyInstallment));
//            et = (EditText) rootView.findViewById(R.id.editTextRepayInstallment2);
//            et.setEnabled(allowEdit);
//            et.setText(String.valueOf(mainActivity.AdditionalDetails.OtherLoanInstallment2));
//            et.setText(String.valueOf(mainActivity.OtherMFIData.get(1).MonthlyInstallment));
//            et = (EditText) rootView.findViewById(R.id.editTextRepayMonths1);
//            et.setEnabled(allowEdit);
//            et.setText(String.valueOf(mainActivity.AdditionalDetails.OtherLoanRepayMonths1));
//            et = (EditText) rootView.findViewById(R.id.editTextRepayMonths2);
//            et.setEnabled(allowEdit);
//            et.setText(String.valueOf(mainActivity.AdditionalDetails.OtherLoanRepayMonths2));
//        } else {
//            rg.check(R.id.radioButtonOtherLoanNo);
//
//            Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerLoanBankName1);
//            sp.setSelection(0);
//            sp.setEnabled(false);
//            sp = (Spinner) rootView.findViewById(R.id.spinnerLoanBankName2);
//            sp.setSelection(0);
//            sp.setEnabled(false);
//
//            EditText et = (EditText) rootView.findViewById(R.id.editTextLoanAmount1);
//            et.setEnabled(false);
//            et.setText("");
//            et = (EditText) rootView.findViewById(R.id.editTextLoanAmount2);
//            et.setEnabled(false);
//            et.setText("");
//            et = (EditText) rootView.findViewById(R.id.editTextRepayInstallment1);
//            et.setEnabled(false);
//            et.setText("");
//            et = (EditText) rootView.findViewById(R.id.editTextRepayInstallment2);
//            et.setEnabled(false);
//            et.setText("");
//            et = (EditText) rootView.findViewById(R.id.editTextRepayMonths1);
//            et.setEnabled(false);
//            et.setText("");
//            et = (EditText) rootView.findViewById(R.id.editTextRepayMonths2);
//            et.setEnabled(false);
//            et.setText("");
//        }

        switch (mainActivity.OtherMFIData.size()) {
            case 0:
                EditText et = (EditText) rootView.findViewById(R.id.editTextOtherMFIName1);
                et.setText("");
                et = (EditText) rootView.findViewById(R.id.editTextOtherMFIName2);
                et.setText("");
                et = (EditText) rootView.findViewById(R.id.editTextOtherMFIName3);
                et.setText("");
                et = (EditText) rootView.findViewById(R.id.editTextLoanAmount1);
                et.setText("");
                et = (EditText) rootView.findViewById(R.id.editTextLoanAmount2);
                et.setText("");
                et = (EditText) rootView.findViewById(R.id.editTextLoanAmount3);
                et.setText("");
                et = (EditText) rootView.findViewById(R.id.editTextRepayInstallment1);
                et.setText("");
                et = (EditText) rootView.findViewById(R.id.editTextRepayInstallment2);
                et.setText("");
                et = (EditText) rootView.findViewById(R.id.editTextRepayInstallment3);
                et.setText("");
                break;
            case 1:
                et = (EditText) rootView.findViewById(R.id.editTextOtherMFIName1);
                et.setText(mainActivity.OtherMFIData.get(0).MFI_Name);
                et = (EditText) rootView.findViewById(R.id.editTextOtherMFIName2);
                et.setText("");
                et = (EditText) rootView.findViewById(R.id.editTextOtherMFIName3);
                et.setText("");
                et = (EditText) rootView.findViewById(R.id.editTextLoanAmount1);
                et.setText(String.valueOf(mainActivity.OtherMFIData.get(0).LoanAmount));
                et = (EditText) rootView.findViewById(R.id.editTextLoanAmount2);
                et.setText("");
                et = (EditText) rootView.findViewById(R.id.editTextLoanAmount3);
                et.setText("");
                et = (EditText) rootView.findViewById(R.id.editTextRepayInstallment1);
                et.setText(String.valueOf(mainActivity.OtherMFIData.get(0).MonthlyInstallment));
                et = (EditText) rootView.findViewById(R.id.editTextRepayInstallment2);
                et.setText("");
                et = (EditText) rootView.findViewById(R.id.editTextRepayInstallment3);
                et.setText("");
                break;
            case 2:
                et = (EditText) rootView.findViewById(R.id.editTextOtherMFIName1);
                et.setText(mainActivity.OtherMFIData.get(0).MFI_Name);
                et = (EditText) rootView.findViewById(R.id.editTextOtherMFIName2);
                et.setText(mainActivity.OtherMFIData.get(1).MFI_Name);
                et = (EditText) rootView.findViewById(R.id.editTextOtherMFIName3);
                et.setText("");
                et = (EditText) rootView.findViewById(R.id.editTextLoanAmount1);
                et.setText(String.valueOf(mainActivity.OtherMFIData.get(0).LoanAmount));
                et = (EditText) rootView.findViewById(R.id.editTextLoanAmount2);
                et.setText(String.valueOf(mainActivity.OtherMFIData.get(1).LoanAmount));
                et = (EditText) rootView.findViewById(R.id.editTextLoanAmount3);
                et.setText("");
                et = (EditText) rootView.findViewById(R.id.editTextRepayInstallment1);
                et.setText(String.valueOf(mainActivity.OtherMFIData.get(0).MonthlyInstallment));
                et = (EditText) rootView.findViewById(R.id.editTextRepayInstallment2);
                et.setText(String.valueOf(mainActivity.OtherMFIData.get(1).MonthlyInstallment));
                et = (EditText) rootView.findViewById(R.id.editTextRepayInstallment3);
                et.setText("");
                break;
            case 3:
                et = (EditText) rootView.findViewById(R.id.editTextOtherMFIName1);
                et.setText(mainActivity.OtherMFIData.get(0).MFI_Name);
                et = (EditText) rootView.findViewById(R.id.editTextOtherMFIName2);
                et.setText(mainActivity.OtherMFIData.get(1).MFI_Name);
                et = (EditText) rootView.findViewById(R.id.editTextOtherMFIName3);
                et.setText(mainActivity.OtherMFIData.get(2).MFI_Name);
                et = (EditText) rootView.findViewById(R.id.editTextLoanAmount1);
                et.setText(String.valueOf(mainActivity.OtherMFIData.get(0).LoanAmount));
                et = (EditText) rootView.findViewById(R.id.editTextLoanAmount2);
                et.setText(String.valueOf(mainActivity.OtherMFIData.get(1).LoanAmount));
                et = (EditText) rootView.findViewById(R.id.editTextLoanAmount3);
                et.setText(String.valueOf(mainActivity.OtherMFIData.get(2).LoanAmount));
                et = (EditText) rootView.findViewById(R.id.editTextRepayInstallment1);
                et.setText(String.valueOf(mainActivity.OtherMFIData.get(0).MonthlyInstallment));
                et = (EditText) rootView.findViewById(R.id.editTextRepayInstallment2);
                et.setText(String.valueOf(mainActivity.OtherMFIData.get(1).MonthlyInstallment));
                et = (EditText) rootView.findViewById(R.id.editTextRepayInstallment3);
                et.setText(String.valueOf(mainActivity.OtherMFIData.get(2).MonthlyInstallment));
                break;
        }

//        TextView tv = (TextView) rootView.findViewById(R.id.textViewRequestFocus);
//        tv.requestFocus();
    }

//    public void SQLite_getOtherBorrowingSourcesMaster() {
//        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
//        // Gets the data repository in write mode
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//
//        // Define a projection that specifies which columns from the database
//        // you will actually use after this query.
//        String[] projection1 = {
//                ufinDbContract.OtherBorrowingSourcesMaster.COLUMN_NAME_BorrowingSourcesID,
//                ufinDbContract.OtherBorrowingSourcesMaster.COLUMN_NAME_MarathiDescription,
//                ufinDbContract.OtherBorrowingSourcesMaster.COLUMN_NAME_SequenceID,
//        };
//        String Active = "1";
//        String selection1 = ufinDbContract.OtherBorrowingSourcesMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";
//
//        // How you want the results sorted in the resulting Cursor
//        String sortOrder1 = ufinDbContract.OtherBorrowingSourcesMaster.COLUMN_NAME_SequenceID + " ASC";
//
//        Cursor c1 = db.query(
//                ufinDbContract.OtherBorrowingSourcesMaster.TABLE_NAME,  // The table to query
//                projection1,                               // The columns to return
//                selection1,                                // The columns for the WHERE clause
//                null,
//                null,                                     // don't group the rows
//                null,                                     // don't filter by row groups
//                sortOrder1                                 // The sort order
//        );
//
//        OtherBorrowingSourcesIDMaster.clear();
//        OtherBorrowingSourcesMarathiDescriptionMaster.clear();
//        OtherBorrowingSourcesIDMaster.add(0);
//        OtherBorrowingSourcesMarathiDescriptionMaster.add("");
//
//        try {
//            if (c1 != null && c1.moveToFirst()) {
//                if (c1.getCount() > 0) {
//                    try {
//                        int iCount = c1.getCount();
//                        for (int i = 0; i < iCount; i++) {
//                            OtherBorrowingSourcesIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.OtherBorrowingSourcesMaster.COLUMN_NAME_BorrowingSourcesID))));
//                            String Str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.OtherBorrowingSourcesMaster.COLUMN_NAME_MarathiDescription));
//                            if (Str1 == null) {
//                                Str1 = "?";
//                            }
//                            OtherBorrowingSourcesMarathiDescriptionMaster.add(Str1);
//                            c1.moveToNext();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        } finally {
//            if(c1 != null) { c1.close(); }
//            db.close();
//        }
//    }
}
