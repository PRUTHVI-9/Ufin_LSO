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
import java.util.Calendar;

public class HeirFragment extends Fragment {
    View rootView;
    MainActivity mainActivity;

    private boolean dbMaster = false;
    private ArrayList<Long> RelationIDMaster = new ArrayList<Long>();
    private ArrayList<String> RelationMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Long> BusinessIDMaster = new ArrayList<Long>();
    private ArrayList<String> BusinessMarathiDescriptionMaster = new ArrayList<String>();

//    private boolean NomineeIsFamilyMember = false;
//    private boolean NomineeIsFamilyMemberOtherThanSpouse = false;
//    int NomineeFamilyMemberIndex;
//
//    private ArrayList<Integer> NomineeMemberIndexMaster = new ArrayList<Integer>();
//    private ArrayList<String> NomineeMaster = new ArrayList<String>();
//    int selectedNomineeIndex;
//    int selectedNomineePosition = -1;

    public HeirFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_heir, container, false);
        mainActivity = (MainActivity) getActivity();

        if(!dbMaster) {
            SQLite_getRelationMaster();
            SQLite_getBusinessMaster();
            dbMaster = true;
        }

//        NomineeMemberIndexMaster.clear();
//        NomineeMemberIndexMaster.add(-1);
//        NomineeMaster.clear();
//        NomineeMaster.add("");
//        for(int i=1; i<mainActivity.Member.size(); i++) {
//            if(mainActivity.Member.get(i).IsActive == 1) {
//                NomineeMemberIndexMaster.add(i);
//                NomineeMaster.add(mainActivity.Member.get(i).Fname);
//            }
//        }
//        NomineeMemberIndexMaster.add(-1);
//        NomineeMaster.add("इतर वारसदार");
//
//        ArrayAdapter<String> adapterNominee = new ArrayAdapter<String>(getActivity().getBaseContext(),
//                R.layout.simple_spinner_item, NomineeMaster);
//        adapterNominee.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
//        Spinner sp1 = (Spinner) rootView.findViewById(R.id.spinnerNominee);
//        sp1.setAdapter(adapterNominee);
//        sp1.setSelection(0);
//        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (mainActivity.FragmentVisibleToUser !=
//                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
//                    return;
//                if (mainActivity.iCurrentScreen !=
//                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
//                    return;
//                if(selectedNomineePosition == position)
//                    return;
//
//                selectedNomineePosition = position;
//
//                if(position == 0) {
//                    //Nominee Not Selected
//                    NomineeIsFamilyMember = false;
//                    NomineeFamilyMemberIndex = -1;
////                    if(!mainActivity.NomineeRequired)
//                    if(false)
//                        mainActivity.LMFnominee.NomineeID = -1;
//                    else
//                        mainActivity.LMFnominee.NomineeID = 0;
//                } else if(position == (NomineeMaster.size() - 1)) {
//                    //Other Nominee
//                    NomineeIsFamilyMember = false;
//                    NomineeFamilyMemberIndex = -1;
//                    mainActivity.LMFnominee.NomineeID = 0;
//                } else  {
//                    //Family Member Nominee
//                    NomineeIsFamilyMember = true;
//                    NomineeFamilyMemberIndex = NomineeMemberIndexMaster.get(position);
//                    mainActivity.LMFnominee.NomineeID =
//                            mainActivity.Member.get(NomineeFamilyMemberIndex).MemberID;
//                }
//
//                RefreshScreen();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });

        EditText et = (EditText) rootView.findViewById(R.id.editTextNomineeFname);
        if(mainActivity.LMFnominee.NomineeIsFamilyMember == 1){
            et.setEnabled(false);
        } else {
            et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                            (actionId == EditorInfo.IME_ACTION_NEXT)) {
                        mainActivity.Othernominee.FName = v.getText().toString();
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
                                mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
                            return;
                        mainActivity.Othernominee.FName = ((EditText) view).getText().toString();
                    }
                }
            });
        }
        et = (EditText) rootView.findViewById(R.id.editTextNomineeMname);
        if(mainActivity.LMFnominee.NomineeIsFamilyMember == 1){
            et.setEnabled(false);
        } else {
            et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                            (actionId == EditorInfo.IME_ACTION_NEXT)) {
                        mainActivity.Othernominee.MName = v.getText().toString();
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
                                mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
                            return;
                        mainActivity.Othernominee.MName = ((EditText) view).getText().toString();
                    }
                }
            });
        }
        et = (EditText) rootView.findViewById(R.id.editTextNomineeLname);
        if(mainActivity.LMFnominee.NomineeIsFamilyMember == 1){
            et.setEnabled(false);
        } else {
            et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                            (actionId == EditorInfo.IME_ACTION_NEXT)) {
                        mainActivity.Othernominee.LName = v.getText().toString();
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
                                mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
                            return;
                        mainActivity.Othernominee.LName = ((EditText) view).getText().toString();
                    }
                }
            });
        }
//bmp 25-Jul-23
        et = (EditText) rootView.findViewById(R.id.editTextAddress1);
        et.setHorizontallyScrolling(false);
        et.setLines(2);
        et.setMaxLines(2);
        et.setMinLines(2);
        if(mainActivity.LMFnominee.NomineeIsFamilyMember == 1){
            et.setEnabled(false);
        } else {
            et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                            (actionId == EditorInfo.IME_ACTION_NEXT)) {
                        mainActivity.OthernomineeAddress.Address = v.getText().toString();
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
                                mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
                            return;
                        mainActivity.OthernomineeAddress.Address = ((EditText) view).getText().toString();
                    }
                }
            });
        }
        et = (EditText) rootView.findViewById(R.id.editTextPincode1);
        if(mainActivity.LMFnominee.NomineeIsFamilyMember == 1){
            et.setEnabled(false);
        } else {
            et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                            (actionId == EditorInfo.IME_ACTION_NEXT)) {
                        String strTemp = v.getText().toString();
                        try {
                            int iTemp = Integer.parseInt(strTemp);
                            if ((strTemp.length() == 6) && (strTemp.charAt(0) == '4')) {
                                mainActivity.OthernomineeAddress.Pincode = strTemp;
                            } else {
                                //Invalid Pincode
                                v.setText("");
                            }
                        } catch (NumberFormatException e) {
                            //Invalid Pincode
                            v.setText("");
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
                                mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
                            return;
                        String strTemp = ((EditText) view).getText().toString();
                        try {
                            int iTemp = Integer.parseInt(strTemp);
                            if ((strTemp.length() == 6) && (strTemp.charAt(0) == '4')) {
                                mainActivity.OthernomineeAddress.Pincode = strTemp;
                            } else {
                                //Invalid Pincode
                                ((EditText) view).getText().clear();
                            }
                        } catch (NumberFormatException e) {
                            //Invalid Pincode
                            ((EditText) view).getText().clear();
                        }
                    }
                }
            });
        }
//bmp 25-Jul-23
        et = (EditText) rootView.findViewById(R.id.editTextAge);
        if(mainActivity.LMFnominee.NomineeIsFamilyMember == 1){
            et.setEnabled(false);
        } else {
            et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                            (actionId == EditorInfo.IME_ACTION_NEXT)) {
                        try {
                            int iAge = Integer.parseInt(v.getText().toString());
                            Calendar CalendarDOB = Calendar.getInstance();
                            int iYear = CalendarDOB.get(Calendar.YEAR);
                            int iYearOfBirth = iYear - iAge;
                            CalendarDOB.set(Calendar.YEAR, iYearOfBirth);
                            CalendarDOB.set(Calendar.MONTH, Calendar.JANUARY);
                            CalendarDOB.set(Calendar.DATE, 1);
                            String strBirthDate = String.format("%04d", CalendarDOB.get(Calendar.YEAR)) + "-"
                                    + String.format("%02d", (CalendarDOB.get(Calendar.MONTH) + 1)) + "-"
                                    + String.format("%02d", CalendarDOB.get(Calendar.DATE));
                            mainActivity.Othernominee.DateOfBirth = strBirthDate;

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
                                mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
                            return;
                        try {
                            int iAge = Integer.parseInt(((EditText) view).getText().toString());
                            Calendar CalendarDOB = Calendar.getInstance();
                            int iYear = CalendarDOB.get(Calendar.YEAR);
                            int iYearOfBirth = iYear - iAge;
                            CalendarDOB.set(Calendar.YEAR, iYearOfBirth);
                            CalendarDOB.set(Calendar.MONTH, Calendar.JANUARY);
                            CalendarDOB.set(Calendar.DATE, 1);
                            String strBirthDate = String.format("%04d", CalendarDOB.get(Calendar.YEAR)) + "-"
                                    + String.format("%02d", (CalendarDOB.get(Calendar.MONTH) + 1)) + "-"
                                    + String.format("%02d", CalendarDOB.get(Calendar.DATE));
                            mainActivity.Othernominee.DateOfBirth = strBirthDate;

                            RefreshScreen();
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            });
        }
        et = (EditText) rootView.findViewById(R.id.editTextMobile1);
        if(mainActivity.LMFnominee.NomineeIsFamilyMember == 1){
            et.setEnabled(false);
        } else {
            et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                            (actionId == EditorInfo.IME_ACTION_NEXT)) {
                        String strTemp = v.getText().toString();
                        if(strTemp == null)
                            strTemp = "0";
                        else if(strTemp.isEmpty())
                            strTemp = "0";
                        else if(strTemp.length() < 10)
                            strTemp = "0";
                        mainActivity.Othernominee.ContactNo = strTemp;
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
                                mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
                            return;
                        String strTemp = ((EditText) view).getText().toString();
                        if(strTemp == null)
                            strTemp = "0";
                        else if(strTemp.isEmpty())
                            strTemp = "0";
                        else if(strTemp.length() < 10)
                            strTemp = "0";
                        mainActivity.Othernominee.ContactNo = strTemp;
                    }
                }
            });
        }

        et = (EditText) rootView.findViewById(R.id.editTextNomineeGuardianFname);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    String strName = v.getText().toString();
                    mainActivity.LMFnominee.Guardian_FName = strName;
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
                        return;
                    String strName = ((EditText) view).getText().toString();
                    mainActivity.LMFnominee.Guardian_FName = strName;
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextNomineeGuardianMname);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    String strName = v.getText().toString();
                    mainActivity.LMFnominee.Guardian_MName = strName;
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
                        return;
                    String strName = ((EditText) view).getText().toString();
                    mainActivity.LMFnominee.Guardian_MName = strName;
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextNomineeGuardianLname);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    String strName = v.getText().toString();
                    mainActivity.LMFnominee.Guardian_LName = strName;
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
                        return;
                    String strName = ((EditText) view).getText().toString();
                    mainActivity.LMFnominee.Guardian_LName = strName;
                }
            }
        });

        et = (EditText) rootView.findViewById(R.id.editTextAddress2);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    mainActivity.LMFnominee.GuardianAddress = v.getText().toString();
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
                        return;
                    mainActivity.LMFnominee.GuardianAddress = ((EditText) view).getText().toString();
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextMobile2);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    String strTemp = v.getText().toString();
                    if(strTemp == null)
                        strTemp = "0";
                    else if(strTemp.isEmpty())
                        strTemp = "0";
                    else if(strTemp.length() < 10)
                        strTemp = "0";
                    mainActivity.AdditionalDetails.NomineeGuardianContactNumber = strTemp;
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
                        return;
                    String strTemp = ((EditText) view).getText().toString();
                    if(strTemp == null)
                        strTemp = "0";
                    else if(strTemp.isEmpty())
                        strTemp = "0";
                    else if(strTemp.length() < 10)
                        strTemp = "0";
                    mainActivity.AdditionalDetails.NomineeGuardianContactNumber = strTemp;
                }
            }
        });
        ArrayAdapter<String> adapterRelation = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, RelationMarathiDescriptionMaster);
        adapterRelation.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerRelation1);
        sp.setAdapter(adapterRelation);
        if(mainActivity.LMFnominee.NomineeIsFamilyMember == 1){
            sp.setEnabled(false);
        } else {
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (mainActivity.FragmentVisibleToUser !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
                        return;
                    if (mainActivity.iCurrentScreen !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
                        return;
                    mainActivity.Othernominee.RelationID = RelationIDMaster.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerRelation2);
        sp.setAdapter(adapterRelation);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
                    return;
                mainActivity.AdditionalDetails.NomineeGuardianRelationID = RelationIDMaster.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterBusiness = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, BusinessMarathiDescriptionMaster);
        adapterBusiness.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupation1);
        sp.setAdapter(adapterBusiness);
        if(mainActivity.LMFnominee.NomineeIsFamilyMember == 1){
            sp.setEnabled(false);
        } else {
            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (mainActivity.FragmentVisibleToUser !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
                        return;
                    if (mainActivity.iCurrentScreen !=
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
                        return;
                    mainActivity.AdditionalDetails.NomineeBusinessID = BusinessIDMaster.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupation2);
        sp.setAdapter(adapterBusiness);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE)
                    return;
                mainActivity.AdditionalDetails.NomineeGuardianBusinessID = BusinessIDMaster.get(position);
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
            mainActivity.FragmentVisibleToUser = mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_NOMINEE;
            RefreshScreen();
        }
    }

    public void RefreshScreen() {
        int indexHeir = -1;
        for(int i=1; i<mainActivity.Member.size(); i++) {
            if(mainActivity.Member.get(i).IsActive == 1) {
                if(mainActivity.LMFnominee.NomineeID
                        == mainActivity.Member.get(i).MemberID) {
                    indexHeir = i;
                    break;
                }
            }
        }

//        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerNominee);
////        if(!mainActivity.NomineeRequired) {
//        if(false) {
//            sp.setEnabled(false);
//            sp.setSelection(0);
//        } else {
//            if(mainActivity.LMFnominee.NomineeID == 0) {
//                NomineeIsFamilyMember = false;
//                sp.setSelection(NomineeMemberIndexMaster.size()-1);
//            } else {
//                for(int k=0; k<NomineeMemberIndexMaster.size(); k++) {
//                    if(NomineeMemberIndexMaster.get(k) < 0)
//                        continue;
//                    int iIndex = NomineeMemberIndexMaster.get(k);
//                    if(mainActivity.LMFnominee.NomineeID ==
//                            mainActivity.Member.get(iIndex).MemberID) {
//                        NomineeIsFamilyMember = true;
//                        NomineeFamilyMemberIndex = iIndex;
//                        sp.setSelection(k);
//                        break;
//                    }
//                }
//            }
//            sp.setEnabled(true);
//        }

        String strFname1 = "";
        String strMname1 = "";
        String strLname1 = "";
        long lRelation1 = -1;
        String strAge1 = "";
        int iAge = 0;
        long lOccupation1 = -1;
        String strNomineeContactNumber1 = "";
        String strAddress = "";
        String strPincode = "";
        if(indexHeir > 0) {
            strFname1 = mainActivity.Member.get(indexHeir).Fname;
            strMname1 = mainActivity.Member.get(indexHeir).Mname;
            strLname1 = mainActivity.Member.get(indexHeir).Lname;
            lRelation1 = mainActivity.Member.get(indexHeir).RelationID;
            String strDOB = mainActivity.Member.get(indexHeir).BirthDate;
            if(strDOB.length() > 10)
                strDOB = strDOB.substring(0, 10);
            try {
                String[] strSplit = strDOB.split("-");
                Calendar dob = Calendar.getInstance();
                dob.set(Calendar.YEAR, Integer.parseInt(strSplit[0]));
                dob.set(Calendar.MONTH, Integer.parseInt(strSplit[1]) - 1);
                dob.set(Calendar.DATE, Integer.parseInt(strSplit[2]));
                Calendar today = Calendar.getInstance();
                iAge = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
                if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
                    iAge--;
                }
            }catch (Exception e){
            }
            strAge1 = String.valueOf(iAge);

            lOccupation1 = mainActivity.PersonalImpactInfo.get(indexHeir).BusinessID;
            strNomineeContactNumber1 = mainActivity.Member.get(indexHeir).ContactNo;
            strAddress = mainActivity.MemberAddress.Address;
            strPincode = mainActivity.MemberAddress.Pincode;
        } else {
//bmp 28-Jun-23
            mainActivity.Othernominee.MemberID = mainActivity.Member.get(0).MemberID;
//bmp 28-Jun-23
//bmp 25-Jul-23
            mainActivity.LMFnominee.NomineeID = mainActivity.Othernominee.NomineeID;
//bmp 25-Jul-23
            strFname1 = mainActivity.Othernominee.FName;
            strMname1 = mainActivity.Othernominee.MName;
            strLname1 = mainActivity.Othernominee.LName;
            lRelation1 = mainActivity.Othernominee.RelationID;

            String strDOB = mainActivity.Othernominee.DateOfBirth;
            if(strDOB.length() > 10)
                strDOB = strDOB.substring(0, 10);
            try {
                String[] strSplit = strDOB.split("-");
                Calendar dob = Calendar.getInstance();
                dob.set(Calendar.YEAR, Integer.parseInt(strSplit[0]));
                dob.set(Calendar.MONTH, Integer.parseInt(strSplit[1]) - 1);
                dob.set(Calendar.DATE, Integer.parseInt(strSplit[2]));
                Calendar today = Calendar.getInstance();
                iAge = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
                if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
                    iAge--;
                }
            }catch (Exception e){
            }
            strAge1 = String.valueOf(iAge);

            lOccupation1 = mainActivity.AdditionalDetails.NomineeBusinessID;
            strNomineeContactNumber1 = mainActivity.Othernominee.ContactNo;
            strAddress = mainActivity.OthernomineeAddress.Address;
            strPincode = mainActivity.OthernomineeAddress.Pincode;
        }
        TextView tv = (TextView) rootView.findViewById(R.id.editTextNomineeFname);
        tv.setText(strFname1);
        tv = (TextView) rootView.findViewById(R.id.editTextNomineeMname);
        tv.setText(strMname1);
        tv = (TextView) rootView.findViewById(R.id.editTextNomineeLname);
        tv.setText(strLname1);
        tv = (TextView) rootView.findViewById(R.id.editTextAddress1);
        tv.setText(strAddress);
        tv = (TextView) rootView.findViewById(R.id.editTextPincode1);
        tv.setText(strPincode);
        tv = (TextView) rootView.findViewById(R.id.editTextAge);
        tv.setText(strAge1);
        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerRelation1);
        sp.setSelection(0);
        for(int i=0; i<RelationIDMaster.size(); i++) {
            if(RelationIDMaster.get(i) == lRelation1) {
                sp.setSelection(i);
                break;
            }
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupation1);
        sp.setSelection(0);
        for(int i=0; i<BusinessIDMaster.size(); i++) {
            if(BusinessIDMaster.get(i) == lOccupation1) {
                sp.setSelection(i);
                break;
            }
        }
        tv = (TextView) rootView.findViewById(R.id.editTextMobile1);
        tv.setText(strNomineeContactNumber1);

        tv = (TextView) rootView.findViewById(R.id.editTextNomineeGuardianFname);
        if(iAge < 18) {
            tv.setText(mainActivity.LMFnominee.Guardian_FName);
            tv.setEnabled(true);
        } else {
            mainActivity.LMFnominee.Guardian_FName = "NA";
            tv.setText(mainActivity.LMFnominee.Guardian_FName);
            tv.setEnabled(false);
        }
        tv = (TextView) rootView.findViewById(R.id.editTextNomineeGuardianMname);
        if(iAge < 18) {
            tv.setText(mainActivity.LMFnominee.Guardian_MName);
            tv.setEnabled(true);
        } else {
            mainActivity.LMFnominee.Guardian_MName = "NA";
            tv.setText(mainActivity.LMFnominee.Guardian_MName);
            tv.setEnabled(false);
        }
        tv = (TextView) rootView.findViewById(R.id.editTextNomineeGuardianLname);
        if(iAge < 18) {
            tv.setText(mainActivity.LMFnominee.Guardian_LName);
            tv.setEnabled(true);
        } else {
            mainActivity.LMFnominee.Guardian_LName = "NA";
            tv.setText(mainActivity.LMFnominee.Guardian_LName);
            tv.setEnabled(false);
        }
        tv = (TextView) rootView.findViewById(R.id.editTextAddress2);
        if(iAge < 18) {
            tv.setText(mainActivity.LMFnominee.GuardianAddress);
            tv.setEnabled(true);
        } else {
            mainActivity.LMFnominee.GuardianAddress = "NA";
            tv.setText(mainActivity.LMFnominee.GuardianAddress);
            tv.setEnabled(false);
        }
        tv = (TextView) rootView.findViewById(R.id.editTextMobile2);
        if(iAge < 18) {
            tv.setText(mainActivity.AdditionalDetails.NomineeGuardianContactNumber);
            tv.setEnabled(true);
        } else {
            mainActivity.AdditionalDetails.NomineeGuardianContactNumber = "0";
            tv.setText(mainActivity.AdditionalDetails.NomineeGuardianContactNumber);
            tv.setEnabled(false);
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerRelation2);
        sp.setSelection(0);
        if(iAge < 18) {
            for(int i=0; i<RelationIDMaster.size(); i++) {
                if(RelationIDMaster.get(i) == mainActivity.AdditionalDetails.NomineeGuardianRelationID) {
                    sp.setSelection(i);
                    break;
                }
            }
            sp.setEnabled(true);
        } else {
            mainActivity.AdditionalDetails.NomineeGuardianRelationID = 0;
            sp.setEnabled(false);
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupation2);
        sp.setSelection(0);
        if(iAge < 18) {
            for (int i = 0; i < BusinessIDMaster.size(); i++) {
                if (BusinessIDMaster.get(i) == mainActivity.AdditionalDetails.NomineeGuardianBusinessID) {
                    sp.setSelection(i);
                    break;
                }
            }
            sp.setEnabled(true);
        } else {
            mainActivity.AdditionalDetails.NomineeGuardianBusinessID = 0;
            sp.setEnabled(false);
        }

        tv = (TextView) rootView.findViewById(R.id.textViewRequestFocus);
        tv.requestFocus();
    }

    public void SQLite_getRelationMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.RelationMaster.COLUMN_NAME_RelationID,
                ufinDbContract.RelationMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.RelationMaster.COLUMN_NAME_SequenceID,
        };
        String Active = "1";

        String selection1 = ufinDbContract.RelationMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.RelationMaster.COLUMN_NAME_SequenceID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.RelationMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        RelationIDMaster.clear();
        RelationMarathiDescriptionMaster.clear();
        RelationIDMaster.add(0L);
        RelationMarathiDescriptionMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            RelationIDMaster.add(Long.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.RelationMaster.COLUMN_NAME_RelationID))));
                            String str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.RelationMaster.COLUMN_NAME_MarathiDescription));
                            if (str1 == null) {
                                str1 = "?";
                            }
                            RelationMarathiDescriptionMaster.add(str1);
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
                ufinDbContract.BusinessMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.BusinessMaster.COLUMN_NAME_SequenceID,
        };
        String Active = "1";

        String selection1 = ufinDbContract.BusinessMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.BusinessMaster.COLUMN_NAME_SequenceID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.BusinessMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        BusinessIDMaster.clear();
        BusinessMarathiDescriptionMaster.clear();
        BusinessIDMaster.add(0L);
        BusinessMarathiDescriptionMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            BusinessIDMaster.add(Long.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.BusinessMaster.COLUMN_NAME_BusinessID))));
                            String str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.BusinessMaster.COLUMN_NAME_MarathiDescription));
                            if (str1 == null) {
                                str1 = "?";
                            }
                            BusinessMarathiDescriptionMaster.add(str1);
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
