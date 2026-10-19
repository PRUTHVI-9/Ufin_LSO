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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class SavingAccountFragment extends Fragment {
    View rootView;
    MainActivity mainActivity;

    private boolean dbMaster = false;
    private ArrayList<Integer> BankIDMaster = new ArrayList<Integer>();
    private ArrayList<String> BankMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Integer> BankBranchIDMaster = new ArrayList<Integer>();
    private ArrayList<String> BankBranchName_IFSCCodeMaster = new ArrayList<String>();
    static int selectedBankPosition, selectedBankBranchPosition;

    public SavingAccountFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_savingaccount, container, false);
        mainActivity = (MainActivity) getActivity();

        if(!dbMaster) {
            SQLite_getBankMaster();
            dbMaster = true;
        }

        ArrayAdapter<String> adapterBank = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, BankMarathiDescriptionMaster);
        adapterBank.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerSBname1);
        sp.setAdapter(adapterBank);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_BANK_ACCOUNT)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_BANK_ACCOUNT)
                    return;
                if (selectedBankPosition != position) {
                    selectedBankPosition = position;
                    mainActivity.Member.get(0).BankID = BankIDMaster.get(position);
                    SQLite_getBankBranchMaster(mainActivity.Member.get(0).BankID);
                    selectedBankBranchPosition = -1;
                }

                ArrayAdapter<String> adapterBankBranch = new ArrayAdapter<String>(getActivity().getBaseContext(),
                        R.layout.simple_spinner_item, BankBranchName_IFSCCodeMaster);
                adapterBankBranch.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                Spinner sp1 = (Spinner) rootView.findViewById(R.id.spinnerSBbranch);
                sp1.setAdapter(adapterBankBranch);
                sp1.setSelection(selectedBankBranchPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Spinner sp1 = (Spinner) rootView.findViewById(R.id.spinnerSBbranch);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_BANK_ACCOUNT)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_BANK_ACCOUNT)
                    return;
                if (selectedBankBranchPosition != position) {
                    mainActivity.Member.get(0).BankBranchID = BankBranchIDMaster.get(position);
                    selectedBankBranchPosition = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        EditText et = (EditText) rootView.findViewById(R.id.editTextBankFname);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    String strName = v.getText().toString();
                    if(!strName.trim().isEmpty())
                        mainActivity.Member.get(0).Bank_Fname = strName;
                    else
                        mainActivity.Member.get(0).Bank_Fname = "NA";
                }

                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    if(mainActivity.FragmentVisibleToUser !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_BANK_ACCOUNT)
                        return;
                    if (mainActivity.iCurrentScreen !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_BANK_ACCOUNT)
                        return;

                    String strName = ((TextView)view).getText().toString();
                    if(!strName.trim().isEmpty())
                        mainActivity.Member.get(0).Bank_Fname = strName;
                    else
                        mainActivity.Member.get(0).Bank_Fname = "NA";
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextBankMname);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    String strName = v.getText().toString();
                    if(!strName.trim().isEmpty())
                        mainActivity.Member.get(0).Bank_Mname = strName;
                    else
//bmp 23-Aug-22
//                        mainActivity.Member.get(0).Bank_Mname = "NA";
                        mainActivity.Member.get(0).Bank_Mname = "";
//bmp 23-Aug-22
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    if(mainActivity.FragmentVisibleToUser !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_BANK_ACCOUNT)
                        return;
                    if (mainActivity.iCurrentScreen !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_BANK_ACCOUNT)
                        return;

                    String strName = ((TextView)view).getText().toString();
                    if(!strName.trim().isEmpty())
                        mainActivity.Member.get(0).Bank_Mname = strName;
                    else
//bmp 23-Aug-22
//                        mainActivity.Member.get(0).Bank_Mname = "NA";
                        mainActivity.Member.get(0).Bank_Mname = "";
//bmp 23-Aug-22
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextBankLname);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    String strName = v.getText().toString();
                    if(!strName.trim().isEmpty())
                        mainActivity.Member.get(0).Bank_Lname = strName;
                    else
//bmp 23-Aug-22
//                        mainActivity.Member.get(0).Bank_Lname = "NA";
                        mainActivity.Member.get(0).Bank_Lname = "";
//bmp 23-Aug-22
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    if(mainActivity.FragmentVisibleToUser !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_BANK_ACCOUNT)
                        return;
                    if (mainActivity.iCurrentScreen !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_BANK_ACCOUNT)
                        return;

                    String strName = ((TextView)view).getText().toString();
                    if(!strName.trim().isEmpty())
                        mainActivity.Member.get(0).Bank_Lname = strName;
                    else
//bmp 23-Aug-22
//                        mainActivity.Member.get(0).Bank_Lname = "NA";
                        mainActivity.Member.get(0).Bank_Lname = "";
//bmp 23-Aug-22
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
            mainActivity.FragmentVisibleToUser = mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_BANK_ACCOUNT;
            RefreshScreen();
        }
    }

    public void RefreshScreen() {
        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerSBname1);
        sp.setSelection(0);
        for (int i = 0; i < BankIDMaster.size(); i++) {
            if (BankIDMaster.get(i) == mainActivity.Member.get(0).BankID) {
                sp.setSelection(i);
                selectedBankPosition = i;
                break;
            }
        }

        SQLite_getBankBranchMaster(mainActivity.Member.get(0).BankID);

        ArrayAdapter<String> adapterBankBranch = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, BankBranchName_IFSCCodeMaster);
        adapterBankBranch.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner sp1 = (Spinner) rootView.findViewById(R.id.spinnerSBbranch);
        sp1.setAdapter(adapterBankBranch);
        sp1.setSelection(0);
        for (int i = 0; i < BankBranchIDMaster.size(); i++) {
            if (BankBranchIDMaster.get(i) == mainActivity.Member.get(0).BankBranchID) {
                sp1.setSelection(i);
                selectedBankBranchPosition = i;
                break;
            }
        }

        EditText et = (EditText) rootView.findViewById(R.id.editTextBankFname);
        et.setText(mainActivity.Member.get(0).Bank_Fname);
        et = (EditText) rootView.findViewById(R.id.editTextBankMname);
        et.setText(mainActivity.Member.get(0).Bank_Mname);
        et = (EditText) rootView.findViewById(R.id.editTextBankLname);
        et.setText(mainActivity.Member.get(0).Bank_Lname);

        TextView tv = (TextView) rootView.findViewById(R.id.textViewRequestFocus);
        tv.requestFocus();
    }

    public void SQLite_getBankMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.BankMaster.COLUMN_NAME_BankID,
                ufinDbContract.BankMaster.COLUMN_NAME_Description,
                ufinDbContract.BankMaster.COLUMN_NAME_SequenceID,
        };
        String Active = "1";

        String selection1 = ufinDbContract.BankMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.BankMaster.COLUMN_NAME_SequenceID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.BankMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        BankIDMaster.clear();
        BankMarathiDescriptionMaster.clear();
        BankIDMaster.add(0);
        BankMarathiDescriptionMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            BankIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.BankMaster.COLUMN_NAME_BankID))));
                            String str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.BankMaster.COLUMN_NAME_Description));
                            if (str1 == null) {
                                str1 = "?";
                            }
                            BankMarathiDescriptionMaster.add(str1);
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

    public void SQLite_getBankBranchMaster(long iBankID) {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.BankBranchMaster.COLUMN_NAME_BankBranchID,
                ufinDbContract.BankBranchMaster.COLUMN_NAME_BankBranchName,
                ufinDbContract.BankBranchMaster.COLUMN_NAME_IFSCCode,
                ufinDbContract.BankBranchMaster.COLUMN_NAME_SequenceID,
        };
        String Active = "1";

        String selection1 = ufinDbContract.BankBranchMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'"
                + " AND " + ufinDbContract.BankBranchMaster.COLUMN_NAME_BankID + "=" + "'" + String.valueOf(iBankID) + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.BankBranchMaster.COLUMN_NAME_SequenceID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.BankBranchMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        BankBranchIDMaster.clear();
        BankBranchName_IFSCCodeMaster.clear();
        BankBranchIDMaster.add(0);
        BankBranchName_IFSCCodeMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            BankBranchIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.BankBranchMaster.COLUMN_NAME_BankBranchID))));
                            String Str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.BankBranchMaster.COLUMN_NAME_BankBranchName));
                            if (Str1 == null) {
                                Str1 = "?";
                            }
                            String Str2 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.BankBranchMaster.COLUMN_NAME_IFSCCode));
                            if (Str2 == null) {
                                Str2 = "?";
                            }
                            Str1 = Str1 + " IFSC:" + Str2;
                            BankBranchName_IFSCCodeMaster.add(Str1);
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
