package com.example.ufin_lso;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class LoanFragment extends Fragment {
    View rootView;
    MainActivity mainActivity;

    private boolean dbMaster = false;
    private ArrayList<Integer> ReasonIDMaster = new ArrayList<Integer>();
//bmp 31-Jul-23
    private ArrayList<String> ReasonMaster = new ArrayList<String>();
//bmp 31-Jul-23
    private ArrayList<String> ReasonMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Integer> UtilizationIDMaster = new ArrayList<Integer>();
    private ArrayList<Integer> LoanReasonIDMaster = new ArrayList<Integer>();
    private ArrayList<String> UtilizationMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Integer> SelectedUtilizationIDMaster = new ArrayList<Integer>();
    private ArrayList<String> SelectedUtilizationMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Integer> OtherBorrowingSourcesIDMaster = new ArrayList<Integer>();
    private ArrayList<String> OtherBorrowingSourcesMarathiDescriptionMaster = new ArrayList<String>();

    static int selectedLoanReasonPosition;
    static int selectedLoanUtilization1Position;
    static int selectedLoanUtilization2Position;
    static int selectedLoanUtilization3Position;
    static int selectedLoanUtilization4Position;
    static int selectedDeficitFundResourcePosition;

    public LoanFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_loan, container, false);
        mainActivity = (MainActivity) getActivity();

        if(!dbMaster) {
            SQLite_getReasonMaster();
            SQLite_getLoanUtilizationMaster();
            SQLite_getOtherBorrowingSourcesMaster();
            dbMaster = true;
        }
        ArrayAdapter<String> adapterLoanReason = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, ReasonMarathiDescriptionMaster);
        adapterLoanReason.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerLoanReason);
        sp.setAdapter(adapterLoanReason);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_LOAN)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_LOAN)
                    return;
                if (selectedLoanReasonPosition != position) {
                    mainActivity.AdditionalDetails.LoanReasonID = ReasonIDMaster.get(position);
                    mainActivity.AdditionalDetails.strLoanReason = ReasonMaster.get(position);
                    String strReason = ReasonMaster.get(position);
                    if(strReason.trim().equalsIgnoreCase("Debt repayment"))
                        mainActivity.AdditionalDetails.strLoanReason = "Debt";
                    else if(strReason.trim().equalsIgnoreCase("Education Loan"))
                        mainActivity.AdditionalDetails.strLoanReason = "Education";
                    else if(strReason.trim().equalsIgnoreCase("Asset Creation"))
                        mainActivity.AdditionalDetails.strLoanReason = "Asset";
                    else if(strReason.trim().equalsIgnoreCase("Business Expansion"))
                        mainActivity.AdditionalDetails.strLoanReason = "Business";
//bmp 11-Jun-24
//                    else if(strReason.trim().equalsIgnoreCase("House Repairs Loan"))
//                        mainActivity.AdditionalDetails.strLoanReason = "Home";
//                    else if(strReason.trim().equalsIgnoreCase("House Construction"))
//                        mainActivity.AdditionalDetails.strLoanReason = "Home";
//                    else if(strReason.trim().equalsIgnoreCase("House/Plot Purchase"))
//                        mainActivity.AdditionalDetails.strLoanReason = "Home";
                    else if(strReason.trim().equalsIgnoreCase("House Repairs Loan"))
                        mainActivity.AdditionalDetails.strLoanReason = "House";
//bmp 11-Jun-24
                    else if(strReason.trim().startsWith("HL-"))
                        mainActivity.AdditionalDetails.strLoanReason = "Home";

                    selectedLoanReasonPosition = position;

                    SelectedUtilizationIDMaster.clear();
                    SelectedUtilizationMarathiDescriptionMaster.clear();
                    SelectedUtilizationIDMaster.add(0);
                    SelectedUtilizationMarathiDescriptionMaster.add("");
                    for(int i=0; i<UtilizationIDMaster.size(); i++) {
                        if(LoanReasonIDMaster.get(i) == mainActivity.AdditionalDetails.LoanReasonID) {
                            SelectedUtilizationIDMaster.add(UtilizationIDMaster.get(i));
                            SelectedUtilizationMarathiDescriptionMaster.add(UtilizationMarathiDescriptionMaster.get(i));
                        }
                    }

                    mainActivity.AdditionalDetails.LoanUtilizationID1 = 0;
                    mainActivity.AdditionalDetails.LoanUtilizationID2 = 0;
                    mainActivity.AdditionalDetails.LoanUtilizationID3 = 0;
                    mainActivity.AdditionalDetails.LoanUtilizationID4 = 0;

                    selectedLoanUtilization1Position = 0;
                    selectedLoanUtilization2Position = 0;
                    selectedLoanUtilization3Position = 0;
                    selectedLoanUtilization4Position = 0;
                }

                ArrayAdapter<String> adapterLoanUtilization1 = new ArrayAdapter<String>(getActivity().getBaseContext(),
                        R.layout.simple_spinner_item, SelectedUtilizationMarathiDescriptionMaster);
                adapterLoanUtilization1.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerLoanUtilization1);
                sp.setAdapter(adapterLoanUtilization1);
                sp.setSelection(selectedLoanUtilization1Position);

                ArrayAdapter<String> adapterLoanUtilization2 = new ArrayAdapter<String>(getActivity().getBaseContext(),
                        R.layout.simple_spinner_item, SelectedUtilizationMarathiDescriptionMaster);
                adapterLoanUtilization2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                sp = (Spinner) rootView.findViewById(R.id.spinnerLoanUtilization2);
                sp.setAdapter(adapterLoanUtilization2);
                sp.setSelection(selectedLoanUtilization2Position);

                ArrayAdapter<String> adapterLoanUtilization3 = new ArrayAdapter<String>(getActivity().getBaseContext(),
                        R.layout.simple_spinner_item, SelectedUtilizationMarathiDescriptionMaster);
                adapterLoanUtilization3.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                sp = (Spinner) rootView.findViewById(R.id.spinnerLoanUtilization3);
                sp.setAdapter(adapterLoanUtilization3);
                sp.setSelection(selectedLoanUtilization3Position);

                ArrayAdapter<String> adapterLoanUtilization4 = new ArrayAdapter<String>(getActivity().getBaseContext(),
                        R.layout.simple_spinner_item, SelectedUtilizationMarathiDescriptionMaster);
                adapterLoanUtilization4.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                sp = (Spinner) rootView.findViewById(R.id.spinnerLoanUtilization4);
                sp.setAdapter(adapterLoanUtilization4);
                sp.setSelection(selectedLoanUtilization4Position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterLoanUtilization1 = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, SelectedUtilizationMarathiDescriptionMaster);
        adapterLoanUtilization1.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerLoanUtilization1);
        sp.setAdapter(adapterLoanUtilization1);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_LOAN)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_LOAN)
                    return;
                if (selectedLoanUtilization1Position != position) {
                    mainActivity.AdditionalDetails.LoanUtilizationID1 = SelectedUtilizationIDMaster.get(position);
                    selectedLoanUtilization1Position = position;
//bmp 07-Aug-24
                    if(mainActivity.AdditionalDetails.LoanUtilizationID1 == 0) {
                        mainActivity.AdditionalDetails.LoanUtilizationID2 = 0;
                        mainActivity.AdditionalDetails.LoanUtilizationID3 = 0;
                        mainActivity.AdditionalDetails.LoanUtilizationID4 = 0;
                        selectedLoanUtilization2Position = 0;
                        selectedLoanUtilization3Position = 0;
                        selectedLoanUtilization4Position = 0;
                        RefreshScreen();
                    }
//bmp 07-Aug-24
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterLoanUtilization2 = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, SelectedUtilizationMarathiDescriptionMaster);
        adapterLoanUtilization2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerLoanUtilization2);
        sp.setAdapter(adapterLoanUtilization2);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_LOAN)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_LOAN)
                    return;
                if (selectedLoanUtilization2Position != position) {
//bmp 08-Aug-24
//                    mainActivity.AdditionalDetails.LoanUtilizationID2 = SelectedUtilizationIDMaster.get(position);
//                    selectedLoanUtilization2Position = position;
                    if(mainActivity.AdditionalDetails.LoanUtilizationID1 != 0) {
                        mainActivity.AdditionalDetails.LoanUtilizationID2 = SelectedUtilizationIDMaster.get(position);
                        selectedLoanUtilization2Position = position;
                    }
                    RefreshScreen();
//bmp 08-Aug-24
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterLoanUtilization3 = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, SelectedUtilizationMarathiDescriptionMaster);
        adapterLoanUtilization3.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerLoanUtilization3);
        sp.setAdapter(adapterLoanUtilization3);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_LOAN)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_LOAN)
                    return;
                if (selectedLoanUtilization3Position != position) {
//bmp 08-Aug-24
//                    mainActivity.AdditionalDetails.LoanUtilizationID3 = SelectedUtilizationIDMaster.get(position);
//                    selectedLoanUtilization3Position = position;
                    if(mainActivity.AdditionalDetails.LoanUtilizationID2 != 0) {
                        mainActivity.AdditionalDetails.LoanUtilizationID3 = SelectedUtilizationIDMaster.get(position);
                        selectedLoanUtilization3Position = position;
                    }
                    RefreshScreen();
//bmp 08-Aug-24
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterLoanUtilization4 = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, SelectedUtilizationMarathiDescriptionMaster);
        adapterLoanUtilization4.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerLoanUtilization4);
        sp.setAdapter(adapterLoanUtilization4);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_LOAN)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_LOAN)
                    return;
                if (selectedLoanUtilization4Position != position) {
//bmp 08-Aug-24
//                    mainActivity.AdditionalDetails.LoanUtilizationID4 = SelectedUtilizationIDMaster.get(position);
//                    selectedLoanUtilization4Position = position;
                    if(mainActivity.AdditionalDetails.LoanUtilizationID3 != 0) {
                        mainActivity.AdditionalDetails.LoanUtilizationID4 = SelectedUtilizationIDMaster.get(position);
                        selectedLoanUtilization4Position = position;
                    }
                    RefreshScreen();
//bmp 08-Aug-24
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterDeficitFundResource = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, OtherBorrowingSourcesMarathiDescriptionMaster);
        adapterDeficitFundResource.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerDeficitFundResource);
        sp.setAdapter(adapterDeficitFundResource);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_LOAN)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_LOAN)
                    return;
                if (selectedDeficitFundResourcePosition != position) {
                    mainActivity.AdditionalDetails.DeficitFundResourceID = OtherBorrowingSourcesIDMaster.get(position);
                    selectedDeficitFundResourcePosition = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
            mainActivity.FragmentVisibleToUser = mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_LOAN;
            RefreshScreen();
        }
    }

    public void RefreshScreen() {
        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerLoanReason);
        sp.setSelection(0);
        for(int i=0; i<ReasonIDMaster.size(); i++) {
            if(ReasonIDMaster.get(i) == mainActivity.AdditionalDetails.LoanReasonID) {
                selectedLoanReasonPosition = i;
                sp.setSelection(i);
                mainActivity.AdditionalDetails.strLoanReason = ReasonMaster.get(i);
                String strReason = ReasonMaster.get(i);
                if(strReason.trim().equalsIgnoreCase("Debt repayment"))
                    mainActivity.AdditionalDetails.strLoanReason = "Debt";
                else if(strReason.trim().equalsIgnoreCase("Education Loan"))
                    mainActivity.AdditionalDetails.strLoanReason = "Education";
                else if(strReason.trim().equalsIgnoreCase("Asset Creation"))
                    mainActivity.AdditionalDetails.strLoanReason = "Asset";
                else if(strReason.trim().equalsIgnoreCase("Business Expansion"))
                    mainActivity.AdditionalDetails.strLoanReason = "Business";
//bmp 11-Jun-24
//                else if(strReason.trim().equalsIgnoreCase("House Repairs Loan"))
//                    mainActivity.AdditionalDetails.strLoanReason = "Home";
//                else if(strReason.trim().equalsIgnoreCase("House Construction"))
//                    mainActivity.AdditionalDetails.strLoanReason = "Home";
//                else if(strReason.trim().equalsIgnoreCase("House/Plot Purchase"))
//                    mainActivity.AdditionalDetails.strLoanReason = "Home";
                else if(strReason.trim().equalsIgnoreCase("House Repairs Loan"))
                    mainActivity.AdditionalDetails.strLoanReason = "House";
//bmp 11-Jun-24
                else if(strReason.trim().startsWith("HL-"))
                    mainActivity.AdditionalDetails.strLoanReason = "Home";

                break;
            }
        }

        SelectedUtilizationIDMaster.clear();
        SelectedUtilizationMarathiDescriptionMaster.clear();
        SelectedUtilizationIDMaster.add(0);
        SelectedUtilizationMarathiDescriptionMaster.add("");
        for(int i=0; i<UtilizationIDMaster.size(); i++) {
            if(LoanReasonIDMaster.get(i) == mainActivity.AdditionalDetails.LoanReasonID) {
                SelectedUtilizationIDMaster.add(UtilizationIDMaster.get(i));
                SelectedUtilizationMarathiDescriptionMaster.add(UtilizationMarathiDescriptionMaster.get(i));
            }
        }

        ArrayAdapter<String> adapterLoanUtilization1 = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, SelectedUtilizationMarathiDescriptionMaster);
        adapterLoanUtilization1.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerLoanUtilization1);
        sp.setAdapter(adapterLoanUtilization1);
        sp.setSelection(0);
        for(int i=0; i<SelectedUtilizationIDMaster.size(); i++) {
            if(SelectedUtilizationIDMaster.get(i) == mainActivity.AdditionalDetails.LoanUtilizationID1) {
                selectedLoanUtilization1Position = i;
                sp.setSelection(i);
                break;
            }
        }

        ArrayAdapter<String> adapterLoanUtilization2 = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, SelectedUtilizationMarathiDescriptionMaster);
        adapterLoanUtilization2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerLoanUtilization2);
        sp.setAdapter(adapterLoanUtilization2);
        sp.setSelection(0);
        for(int i=0; i<SelectedUtilizationIDMaster.size(); i++) {
            if(SelectedUtilizationIDMaster.get(i) == mainActivity.AdditionalDetails.LoanUtilizationID2) {
                selectedLoanUtilization2Position = i;
                sp.setSelection(i);
                break;
            }
        }

        ArrayAdapter<String> adapterLoanUtilization3 = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, SelectedUtilizationMarathiDescriptionMaster);
        adapterLoanUtilization3.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerLoanUtilization3);
        sp.setAdapter(adapterLoanUtilization3);
        sp.setSelection(0);
        for(int i=0; i<SelectedUtilizationIDMaster.size(); i++) {
            if(SelectedUtilizationIDMaster.get(i) == mainActivity.AdditionalDetails.LoanUtilizationID3) {
                selectedLoanUtilization3Position = i;
                sp.setSelection(i);
                break;
            }
        }

        ArrayAdapter<String> adapterLoanUtilization4 = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, SelectedUtilizationMarathiDescriptionMaster);
        adapterLoanUtilization4.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerLoanUtilization4);
        sp.setAdapter(adapterLoanUtilization4);
        sp.setSelection(0);
        for(int i=0; i<SelectedUtilizationIDMaster.size(); i++) {
            if(SelectedUtilizationIDMaster.get(i) == mainActivity.AdditionalDetails.LoanUtilizationID4) {
                selectedLoanUtilization4Position = i;
                sp.setSelection(i);
                break;
            }
        }

        sp = (Spinner) rootView.findViewById(R.id.spinnerDeficitFundResource);
        sp.setSelection(0);
        for(int i=0; i<OtherBorrowingSourcesIDMaster.size(); i++) {
            if(OtherBorrowingSourcesIDMaster.get(i) == mainActivity.AdditionalDetails.DeficitFundResourceID) {
                selectedDeficitFundResourcePosition = i;
                sp.setSelection(i);
                break;
            }
        }
    }

    public void SQLite_getReasonMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonID,
                ufinDbContract.ReasonMaster.COLUMN_NAME_Reason,
                ufinDbContract.ReasonMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.ReasonMaster.COLUMN_NAME_SequenceID,
        };
        String Active = "1";
        String strReasonType = "Loan Application";
        String selection1 = ufinDbContract.ReasonMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'" +
                " AND " + ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonType + "=" + "'" + strReasonType + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.ReasonMaster.COLUMN_NAME_SequenceID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.ReasonMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        ReasonIDMaster.clear();
        ReasonMarathiDescriptionMaster.clear();
        ReasonIDMaster.add(0);
        ReasonMarathiDescriptionMaster.add("");
        ReasonMaster.clear();
        ReasonMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            String Str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_MarathiDescription));
                            if ((Str1 == null) || (Str1.equalsIgnoreCase(" ")) || (Str1.equalsIgnoreCase(""))) {
                            } else {
                                String Str2 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_Reason));
                                if( (Str2.startsWith("HL-")) && (mainActivity.AdditionalDetails.HomeLoanAmount == 0) ) {
                                } else {
                                    ReasonIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_ReasonID))));
                                    ReasonMarathiDescriptionMaster.add(Str1);
                                    ReasonMaster.add(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReasonMaster.COLUMN_NAME_Reason)));
                                }
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
    public void SQLite_getLoanUtilizationMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.LoanUtilizationMaster.COLUMN_NAME_LoanUtilizationID,
                ufinDbContract.LoanUtilizationMaster.COLUMN_NAME_LoanReasonID,
                ufinDbContract.LoanUtilizationMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.LoanUtilizationMaster.COLUMN_NAME_SequenceID,
        };
        String Active = "1";
        String selection1 = ufinDbContract.LoanUtilizationMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.LoanUtilizationMaster.COLUMN_NAME_SequenceID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.LoanUtilizationMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        UtilizationIDMaster.clear();
        LoanReasonIDMaster.clear();
        UtilizationMarathiDescriptionMaster.clear();
        UtilizationIDMaster.add(0);
        LoanReasonIDMaster.add(0);
        UtilizationMarathiDescriptionMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            UtilizationIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanUtilizationMaster.COLUMN_NAME_LoanUtilizationID))));
                            LoanReasonIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanUtilizationMaster.COLUMN_NAME_LoanReasonID))));
                            String Str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.LoanUtilizationMaster.COLUMN_NAME_MarathiDescription));
                            if (Str1 == null) {
                                Str1 = "?";
                            }
                            UtilizationMarathiDescriptionMaster.add(Str1);
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
    public void SQLite_getOtherBorrowingSourcesMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.OtherBorrowingSourcesMaster.COLUMN_NAME_BorrowingSourcesID,
                ufinDbContract.OtherBorrowingSourcesMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.OtherBorrowingSourcesMaster.COLUMN_NAME_SequenceID,
        };
        String Active = "1";
        String selection1 = ufinDbContract.OtherBorrowingSourcesMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.OtherBorrowingSourcesMaster.COLUMN_NAME_SequenceID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.OtherBorrowingSourcesMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        OtherBorrowingSourcesIDMaster.clear();
        OtherBorrowingSourcesMarathiDescriptionMaster.clear();
        OtherBorrowingSourcesIDMaster.add(0);
        OtherBorrowingSourcesMarathiDescriptionMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            OtherBorrowingSourcesIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.OtherBorrowingSourcesMaster.COLUMN_NAME_BorrowingSourcesID))));
                            String Str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.OtherBorrowingSourcesMaster.COLUMN_NAME_MarathiDescription));
                            if (Str1 == null) {
                                Str1 = "?";
                            }
                            OtherBorrowingSourcesMarathiDescriptionMaster.add(Str1);
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
