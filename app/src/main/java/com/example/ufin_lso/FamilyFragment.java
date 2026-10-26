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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class FamilyFragment extends Fragment {
    View rootView;
    MainActivity mainActivity;

    private int RelationIDMainMember;
    private int RelationIndexMainMember;

    private boolean dbMaster = false;
    private ArrayList<Integer> ReligionIDMaster = new ArrayList<Integer>();
    private ArrayList<String> ReligionMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Integer> RelationIDMaster = new ArrayList<Integer>();
    private ArrayList<String> RelationMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Integer> QualificationIDMaster = new ArrayList<Integer>();
    private ArrayList<String> QualificationMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Integer> MaritalStatusIDMaster = new ArrayList<Integer>();
    private ArrayList<String> MaritalStatusMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Integer> HealthProblemIDMaster = new ArrayList<Integer>();
    private ArrayList<String> HealthProblemMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Integer> HabbitAddictionIDMaster = new ArrayList<Integer>();
    private ArrayList<String> HabbitAddictionMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Integer> BusinessIDMaster = new ArrayList<Integer>();
    private ArrayList<String> BusinessMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Integer> CasteIDMaster = new ArrayList<Integer>();
    private ArrayList<Integer> CasteReligionIDMaster = new ArrayList<Integer>();
    private ArrayList<Integer> CasteCategoryIDMaster = new ArrayList<Integer>();
    private ArrayList<String> CasteMarathiDescriptionMaster = new ArrayList<String>();
    static ArrayList<String> CasteForReligionMarathiDescription = new ArrayList<>();
    static ArrayList<Integer> CasteIDForReligion = new ArrayList<>();
    static ArrayList<Integer> CasteCategoryIDForReligion = new ArrayList<>();
    static int selectedReligionPosition, selectedCastePosition;

    String[] GenderIDMaster;
    String[] GenderMarathiDescriptionMaster;

    String[] WhosContactNumberMaster;

    boolean viewStatus = true;
////bmp 26-Dec-24
//    boolean IsMemberGuarantor = false;
    ////bmp 26-Dec-24
    public FamilyFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_family, container, false);
        mainActivity = (MainActivity) getActivity();

        if(!dbMaster) {
            SQLite_getReligionMaster();
            SQLite_getRelationMaster();
            SQLite_getQualificationMaster();
            SQLite_getMaritalStatusMaster();
            SQLite_getHealthProblemMaster();
            SQLite_getHabbitAddictionMaster();
            SQLite_getBusinessMaster();
            SQLite_getCasteMaster();
            dbMaster = true;
        }

        EditText et = (EditText) rootView.findViewById(R.id.editTextFname);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    mainActivity.Member.get(mainActivity.currentMemberIndex).Fname = v.getText().toString();
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                        return;
                    mainActivity.Member.get(mainActivity.currentMemberIndex).Fname = ((EditText) view).getText().toString();
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextMname);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    mainActivity.Member.get(mainActivity.currentMemberIndex).Mname = v.getText().toString();
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                        return;
                    mainActivity.Member.get(mainActivity.currentMemberIndex).Mname = ((EditText) view).getText().toString();
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextLname);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    mainActivity.Member.get(mainActivity.currentMemberIndex).Lname = v.getText().toString();
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                        return;
                    mainActivity.Member.get(mainActivity.currentMemberIndex).Lname = ((EditText) view).getText().toString();
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextAddress);
        et.setHorizontallyScrolling(false);
        et.setLines(2);
        et.setMaxLines(2);
        et.setMinLines(2);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    if (mainActivity.currentMemberIndex == 0) {
                        mainActivity.MemberAddress.Address = v.getText().toString();
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                        return;
                    if (mainActivity.currentMemberIndex == 0) {
                        mainActivity.MemberAddress.Address = ((EditText) view).getText().toString();
                    }
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextPincode);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    if (mainActivity.currentMemberIndex == 0) {
                        String strTemp = v.getText().toString();
                        try {
                            int iTemp = Integer.parseInt(strTemp);
                            if ((strTemp.length() == 6) && (strTemp.charAt(0) == '4')) {
                                mainActivity.MemberAddress.Pincode = strTemp;
                            } else {
                                //Invalid Pincode
                                v.setText("");
                            }
                        } catch (NumberFormatException e) {
                            //Invalid Pincode
                            v.setText("");
                        }
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                        return;
                    if (mainActivity.currentMemberIndex == 0) {
                        String strTemp = ((EditText) view).getText().toString();
                        try {
                            int iTemp = Integer.parseInt(strTemp);
                            if ((strTemp.length() == 6) && (strTemp.charAt(0) == '4')) {
                                mainActivity.MemberAddress.Pincode = strTemp;
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
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextAge);
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
                        mainActivity.Member.get(mainActivity.currentMemberIndex).BirthDate = strBirthDate;
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
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
                        mainActivity.Member.get(mainActivity.currentMemberIndex).BirthDate = strBirthDate;
                    } catch (NumberFormatException e) {
                    }
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncome);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    try {
                        mainActivity.PersonalImpactInfo.get(mainActivity.currentMemberIndex).MonthlyIncome = Integer.parseInt(v.getText().toString());
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                        return;
                    try {
                        mainActivity.PersonalImpactInfo.get(mainActivity.currentMemberIndex).MonthlyIncome = Integer.parseInt(((EditText) view).getText().toString());
                    } catch (NumberFormatException e) {
                    }
                }
            }
        });
        et = (EditText) rootView.findViewById(R.id.editTextMobileNumber);
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
                    mainActivity.Member.get(mainActivity.currentMemberIndex).ContactNo = strTemp;
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
                            mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                        return;
                    String strTemp = ((EditText) view).getText().toString();
                    if(strTemp == null)
                        strTemp = "0";
                    else if(strTemp.isEmpty())
                        strTemp = "0";
                    else if(strTemp.length() < 10)
                        strTemp = "0";
                    mainActivity.Member.get(mainActivity.currentMemberIndex).ContactNo = strTemp;
                }
            }
        });

        WhosContactNumberMaster = getResources().getStringArray(R.array.whoscontactnumber_array);
        ArrayAdapter<String> adapterWhosContactNumber = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item_center, WhosContactNumberMaster);
        adapterWhosContactNumber.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerWhosContactNumber);
        sp.setAdapter(adapterWhosContactNumber);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                if(mainActivity.currentMemberIndex == 0) {
                    mainActivity.MemberFamilyDetails.WhoseContactNo = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupHMF);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (rb.getId() == R.id.radioButtonHMFyes)
                    mainActivity.Member.get(mainActivity.currentMemberIndex).HMFApplicable = 1;
                else
                    mainActivity.Member.get(mainActivity.currentMemberIndex).HMFApplicable = 0;
            }
        });
        rg = (RadioGroup) rootView.findViewById(R.id.radioGroupHeir);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;

                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (rb.getId() == R.id.radioButtonHeirYes) {
                    mainActivity.LMFnominee.NomineeIsFamilyMember = 1;
                    mainActivity.LMFnominee.IsActive = 1;
                    mainActivity.LMFnominee.MemberID = mainActivity.Member.get(0).MemberID;
                    if(mainActivity.LMFnominee.NomineeID != mainActivity.Member.get(mainActivity.currentMemberIndex).MemberID) {
                        mainActivity.LMFnominee.NomineeID = mainActivity.Member.get(mainActivity.currentMemberIndex).MemberID;
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        Calendar cal = Calendar.getInstance();
                        String strDate = sdf1.format(cal.getTime());// all done
                        mainActivity.LMFnominee.NomineeStartDate = strDate;
                    }
                } else if (rb.getId() == R.id.radioButtonHeirNo) {
                    if(mainActivity.LMFnominee.NomineeID == mainActivity.Member.get(mainActivity.currentMemberIndex).MemberID) {
                        mainActivity.LMFnominee.NomineeIsFamilyMember = 0;
                        mainActivity.LMFnominee.NomineeID = 0;
                    }
                    //NomineeEndDate and IsActive flag for Previous Nominee will be updated when PIF is saved.
                }
            }
        });
//bmp 26-Mar-24
//        rg = (RadioGroup) rootView.findViewById(R.id.radioGroupGuarantor);
//        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (mainActivity.FragmentVisibleToUser !=
//                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
//                    return;
//                if (mainActivity.iCurrentScreen !=
//                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
//                    return;
//
//                RadioButton rb = (RadioButton) group.findViewById(checkedId);
//                if (rb.getId() == R.id.radioButtonGuarantorYes) {
//                    mainActivity.AdditionalDetails.LoanGuarantorID =
//                            mainActivity.Member.get(mainActivity.currentMemberIndex).MemberID;
//                    mainActivity.AdditionalDetails.GuarantorType = 1;
//                } else {
//                    if(mainActivity.AdditionalDetails.LoanGuarantorID ==
//                            mainActivity.Member.get(mainActivity.currentMemberIndex).MemberID) {
//                        mainActivity.AdditionalDetails.LoanGuarantorID = 0;
//                        mainActivity.AdditionalDetails.GuarantorType = 3;
//                    }
//                }
//            }
//        });
//bmp 26-Mar-24
        ArrayAdapter<String> adapterReligion = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item_center, ReligionMarathiDescriptionMaster);
        adapterReligion.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerReligion);
        sp.setAdapter(adapterReligion);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                if (selectedReligionPosition != position) {
                    selectedReligionPosition = position;
                    selectedCastePosition = -1;
                    int iReligionID = ReligionIDMaster.get(position);
                    mainActivity.Member.get(mainActivity.currentMemberIndex).ReligionID = iReligionID;

                    CasteForReligionMarathiDescription.clear();
                    CasteIDForReligion.clear();
                    CasteForReligionMarathiDescription.add("");
                    CasteIDForReligion.add(0);
                    CasteCategoryIDForReligion.clear();
                    CasteCategoryIDForReligion.add(0);
                    for (int i = 0; i < CasteReligionIDMaster.size(); i++) {
                        if (iReligionID == CasteReligionIDMaster.get(i)) {
                            CasteForReligionMarathiDescription.add(CasteMarathiDescriptionMaster.get(i));
                            CasteIDForReligion.add(CasteIDMaster.get(i));
                            CasteCategoryIDForReligion.add(CasteCategoryIDMaster.get(i));
                        }
                    }
                }
                ArrayAdapter<String> adapterCaste = new ArrayAdapter<String>(getActivity().getBaseContext(),
                        R.layout.simple_spinner_item_center, CasteForReligionMarathiDescription);
                adapterCaste.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                Spinner sp1 = (Spinner) rootView.findViewById(R.id.spinnerCaste);
                sp1.setAdapter(adapterCaste);
                sp1.setSelection(selectedCastePosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Spinner sp1 = (Spinner) rootView.findViewById(R.id.spinnerCaste);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                selectedCastePosition = position;
                mainActivity.Member.get(mainActivity.currentMemberIndex).CasteID = CasteIDForReligion.get(position);
                mainActivity.Member.get(mainActivity.currentMemberIndex).CasteCategoryID = CasteCategoryIDForReligion.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterRelation = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item_center, RelationMarathiDescriptionMaster);
        adapterRelation.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner sp2 = (Spinner) rootView.findViewById(R.id.spinnerRelation0);
        sp2.setAdapter(adapterRelation);
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                //Main Member Relation is not editable
                //Disallow setting Family Member as Main Member
                if(RelationIDMaster.get(position) != RelationIDMainMember) {
                    mainActivity.Member.get(mainActivity.currentMemberIndex).RelationID = RelationIDMaster.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterQualification = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item_center, QualificationMarathiDescriptionMaster);
        adapterQualification.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner sp3 = (Spinner) rootView.findViewById(R.id.spinnerQualification);
        sp3.setAdapter(adapterQualification);
        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                mainActivity.PersonalImpactInfo.get(mainActivity.currentMemberIndex).QualificationID = QualificationIDMaster.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterMaritalStatus = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item_center, MaritalStatusMarathiDescriptionMaster);
        adapterMaritalStatus.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner sp4 = (Spinner) rootView.findViewById(R.id.spinnerMaritalStatus);
        sp4.setAdapter(adapterMaritalStatus);
        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                mainActivity.Member.get(mainActivity.currentMemberIndex).MaritalStatusID = MaritalStatusIDMaster.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterHealthProblem = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item_center, HealthProblemMarathiDescriptionMaster);
        adapterHealthProblem.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner sp5 = (Spinner) rootView.findViewById(R.id.spinnerIllness);
        sp5.setAdapter(adapterHealthProblem);
        sp5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                mainActivity.PersonalImpactInfo.get(mainActivity.currentMemberIndex).HealthProblemID = HealthProblemIDMaster.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterHabbitAddiction = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item_center, HabbitAddictionMarathiDescriptionMaster);
        adapterHabbitAddiction.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner sp6 = (Spinner) rootView.findViewById(R.id.spinnerAddiction);
        sp6.setAdapter(adapterHabbitAddiction);
        sp6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                mainActivity.PersonalImpactInfo.get(mainActivity.currentMemberIndex).HabitID = HabbitAddictionIDMaster.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterBusiness = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item_center, BusinessMarathiDescriptionMaster);
        adapterBusiness.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner sp7 = (Spinner) rootView.findViewById(R.id.spinnerOccupation);
        sp7.setAdapter(adapterBusiness);
        sp7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                mainActivity.PersonalImpactInfo.get(mainActivity.currentMemberIndex).BusinessID = BusinessIDMaster.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        GenderIDMaster = getResources().getStringArray(R.array.genderID_array);
        GenderMarathiDescriptionMaster = getResources().getStringArray(R.array.gender_array);

        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item_center, GenderMarathiDescriptionMaster);
        adapterGender.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner sp8 = (Spinner) rootView.findViewById(R.id.spinnerGender);
        sp8.setAdapter(adapterGender);
        sp8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mainActivity.FragmentVisibleToUser !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                if (mainActivity.iCurrentScreen !=
                        mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY)
                    return;
                mainActivity.Member.get(mainActivity.currentMemberIndex).Gender = GenderIDMaster[position];
                if (mainActivity.Member.get(mainActivity.currentMemberIndex).Gender.equalsIgnoreCase("M")) {
                    mainActivity.Member.get(mainActivity.currentMemberIndex).Title = "Mr.";
                } else {
                    mainActivity.Member.get(mainActivity.currentMemberIndex).Title = "Ms.";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Button btn = (Button) rootView.findViewById(R.id.buttonNextFamilyMember);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainActivity.Member.size() > 1) {
                    if(mainActivity.currentMemberIndex < (mainActivity.Member.size() - 1)) {
                        mainActivity.currentMemberIndex++;
                        if(mainActivity.Member.get(mainActivity.currentMemberIndex).IsActive == 0) {
                            viewStatus = false;
                        } else {
                            viewStatus = true;
////bmp 26-Dec-24
//                            IsMemberGuarantor = false;
//                            if(mainActivity.AdditionalDetails.LoanGuarantorID == mainActivity.Member.get(mainActivity.currentMemberIndex).MemberID) {
//                                IsMemberGuarantor = true;
//                            }
////bmp 26-Dec-24
                        }
                        RefreshScreen();
                    }
                }
            }
        });
        btn = (Button) rootView.findViewById(R.id.buttonPreviousFamilyMember);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainActivity.Member.size() > 1) {
                    if(mainActivity.currentMemberIndex > 0) {
                        mainActivity.currentMemberIndex--;
                        if(mainActivity.currentMemberIndex > 0) {
                            if(mainActivity.Member.get(mainActivity.currentMemberIndex).IsActive == 0) {
                                viewStatus = false;
                            } else {
                                viewStatus = true;
////bmp 26-Dec-24
//                                IsMemberGuarantor = false;
//                                if(mainActivity.AdditionalDetails.LoanGuarantorID == mainActivity.Member.get(mainActivity.currentMemberIndex).MemberID) {
//                                    IsMemberGuarantor = true;
//                                }
////bmp 26-Dec-24
                            }
                        }
                        RefreshScreen();
                    }
                }
            }
        });

        return rootView;
    }

    public void RefreshScreen() {
        TextView tv = (TextView) rootView.findViewById(R.id.editTextRationCard);
        tv.setText(mainActivity.MemberFamilyDetails.RationCardNo);

        tv = (TextView) rootView.findViewById(R.id.textViewSN);
        tv.setText(String.valueOf(mainActivity.currentMemberIndex + 1));
        tv = (TextView) rootView.findViewById(R.id.textViewAadhaarNumber);
        tv.setText("");
        for(int i=0; i<mainActivity.MemberDocRcvd.size(); i++) {
            if( (mainActivity.MemberDocRcvd.get(i).MemberDocumentsID == mainActivity.DocumentIDs.MEMBER_DOC_ID_AADHAARCARD) &&
                    (mainActivity.MemberDocRcvd.get(i).MemberID == mainActivity.Member.get(mainActivity.currentMemberIndex).MemberID) ) {
                tv.setText(mainActivity.MemberDocRcvd.get(i).DocumentNo);
                break;
            }
        }

        tv = (TextView) rootView.findViewById(R.id.editTextFname);
        tv.setText(mainActivity.Member.get(mainActivity.currentMemberIndex).Fname);
        tv = (TextView) rootView.findViewById(R.id.editTextMname);
        tv.setText(mainActivity.Member.get(mainActivity.currentMemberIndex).Mname);
        tv = (TextView) rootView.findViewById(R.id.editTextLname);
        tv.setText(mainActivity.Member.get(mainActivity.currentMemberIndex).Lname);

        tv = (TextView) rootView.findViewById(R.id.editTextAddress);
        tv.setText(mainActivity.MemberAddress.Address);
        tv = (TextView) rootView.findViewById(R.id.editTextPincode);
        tv.setText(mainActivity.MemberAddress.Pincode);
//bmp 13-Aug-25
        tv = (TextView) rootView.findViewById(R.id.editTextDigipin);
        tv.setText(mainActivity.MemberAddress.DigiPin);
//bmp 13-Aug-25
        EditText et2 = (EditText) rootView.findViewById(R.id.editTextAddress);
        EditText et3 = (EditText) rootView.findViewById(R.id.editTextPincode);
        if(mainActivity.currentMemberIndex == 0) {
            et2.setEnabled(true);
            et3.setEnabled(true);
        } else {
            et2.setEnabled(false);
            et3.setEnabled(false);
        }

        tv = (TextView) rootView.findViewById(R.id.editTextAge);
        String strBirthDate = mainActivity.Member.get(mainActivity.currentMemberIndex).BirthDate;
        int iAge = 0;
        try {
            if(strBirthDate.length() > 10)
                strBirthDate = strBirthDate.substring(0, 10);
            String[] strSplit = strBirthDate.split("-");
            Calendar dob = Calendar.getInstance();
            dob.set(Calendar.YEAR, Integer.parseInt(strSplit[0]));
            dob.set(Calendar.MONTH, (Integer.parseInt(strSplit[1]) - 1));
            dob.set(Calendar.DATE, Integer.parseInt(strSplit[2]));
            Calendar today = Calendar.getInstance();
            iAge = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
                iAge--;
            }
        }catch (Exception e){
        }
        tv.setText(String.valueOf(iAge));

        tv = (TextView) rootView.findViewById(R.id.editTextMonthlyIncome);
        if( (mainActivity.PersonalImpactInfo.get(mainActivity.currentMemberIndex).MonthlyIncome > 0) &&
                (mainActivity.Member.get(mainActivity.currentMemberIndex).IsActive == 1) ) {
            tv.setText(String.valueOf(mainActivity.PersonalImpactInfo.get(mainActivity.currentMemberIndex).MonthlyIncome));
        } else {
            tv.setText("");
        }

        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerWhosContactNumber);
        if(mainActivity.currentMemberIndex == 0) {
            sp.setSelection(mainActivity.MemberFamilyDetails.WhoseContactNo);
        } else {
            sp.setSelection(0);
        }
        if(mainActivity.currentMemberIndex > 0) {
            sp.setEnabled(false);
        } else {
            sp.setEnabled(true);
        }

        tv = (TextView) rootView.findViewById(R.id.editTextMobileNumber);
        tv.setText(mainActivity.Member.get(mainActivity.currentMemberIndex).ContactNo);

        if(mainActivity.currentMemberIndex == 0) {
            RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupHMF);
            rg.setEnabled(false);
            RadioButton rb = (RadioButton) rootView.findViewById(R.id.radioButtonHMFyes);
            rb.setEnabled(false);
            rb.setChecked(true);
            rb = (RadioButton) rootView.findViewById(R.id.radioButtonHMFno);
            rb.setEnabled(false);
        } else {
            RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupHMF);
            rg.setEnabled(true);
            RadioButton rb = (RadioButton) rootView.findViewById(R.id.radioButtonHMFyes);
            rb.setEnabled(true);
            rb = (RadioButton) rootView.findViewById(R.id.radioButtonHMFno);
            rb.setEnabled(true);

            if(mainActivity.Member.get(mainActivity.currentMemberIndex).HMFApplicable == 1) {
                rb = (RadioButton) rootView.findViewById(R.id.radioButtonHMFyes);
                rb.setChecked(true);
            } else {
                rb = (RadioButton) rootView.findViewById(R.id.radioButtonHMFno);
                rb.setChecked(true);
            }
        }
        if(mainActivity.currentMemberIndex == 0) {
            RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupHeir);
            rg.setEnabled(false);
            RadioButton rb = (RadioButton) rootView.findViewById(R.id.radioButtonHeirYes);
            rb.setEnabled(false);
            rb = (RadioButton) rootView.findViewById(R.id.radioButtonHeirNo);
            rb.setChecked(true);
            rb.setEnabled(false);
        } else {
            RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupHeir);
            rg.setEnabled(true);
            RadioButton rb = (RadioButton) rootView.findViewById(R.id.radioButtonHeirYes);
            rb.setEnabled(true);
            rb = (RadioButton) rootView.findViewById(R.id.radioButtonHeirNo);
            rb.setEnabled(true);

            if(mainActivity.LMFnominee.NomineeID
                    == mainActivity.Member.get(mainActivity.currentMemberIndex).MemberID) {
                /*RadioButton*/ rb = (RadioButton) rootView.findViewById(R.id.radioButtonHeirYes);
                rb.setChecked(true);
            } else {
                /*RadioButton*/ rb = (RadioButton) rootView.findViewById(R.id.radioButtonHeirNo);
                rb.setChecked(true);
            }
        }
//bmp 26-Mar-24
//        if(mainActivity.currentMemberIndex == 0) {
//            RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupGuarantor);
//            rg.setEnabled(false);
//            RadioButton rb = (RadioButton) rootView.findViewById(R.id.radioButtonGuarantorYes);
//            rb.setEnabled(false);
//            rb = (RadioButton) rootView.findViewById(R.id.radioButtonGuarantorNo);
//            rb.setChecked(true);
//            rb.setEnabled(false);
//        } else {
//            RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupGuarantor);
//            rg.setEnabled(true);
//            RadioButton rb = (RadioButton) rootView.findViewById(R.id.radioButtonGuarantorYes);
//            rb.setEnabled(true);
//            rb = (RadioButton) rootView.findViewById(R.id.radioButtonGuarantorNo);
//            rb.setEnabled(true);
//
//            if(mainActivity.AdditionalDetails.LoanGuarantorID
//                    == mainActivity.Member.get(mainActivity.currentMemberIndex).MemberID) {
//                /*RadioButton*/ rb = (RadioButton) rootView.findViewById(R.id.radioButtonGuarantorYes);
//                rb.setChecked(true);
//            } else {
//                /*RadioButton*/ rb = (RadioButton) rootView.findViewById(R.id.radioButtonGuarantorNo);
//                rb.setChecked(true);
//            }
//        }
//
//        mainActivity.AdditionalDetails.GuarantorType = 3;
//        for(int i=1; i<mainActivity.Member.size(); i++) {
//            if(mainActivity.Member.get(i).IsActive == 1) {
//                if(mainActivity.AdditionalDetails.LoanGuarantorID
//                        == mainActivity.Member.get(i).MemberID) {
//                    mainActivity.AdditionalDetails.GuarantorType = 1;
//                    break;
//                }
//            }
//        }
//bmp 26-Mar-24
        //Added to avoid crash due to incomplete data
        if(mainActivity.Member.get(mainActivity.currentMemberIndex).ReligionID == 0)
            mainActivity.Member.get(mainActivity.currentMemberIndex).ReligionID = mainActivity.Member.get(0).ReligionID;

        int iReligionID = 0;
        sp = (Spinner) rootView.findViewById(R.id.spinnerReligion);
        sp.setSelection(0);
        for(int i=0; i<ReligionIDMaster.size(); i++) {
            if(ReligionIDMaster.get(i) == mainActivity.Member.get(mainActivity.currentMemberIndex).ReligionID) {
                sp.setSelection(i);
                iReligionID = ReligionIDMaster.get(i);
                selectedReligionPosition = i;
                break;
            }
        }
        CasteForReligionMarathiDescription.clear();
        CasteIDForReligion.clear();
        CasteForReligionMarathiDescription.add("");
        CasteIDForReligion.add(0);
        CasteCategoryIDForReligion.clear();
        CasteCategoryIDForReligion.add(0);
        for (int i = 0; i < CasteReligionIDMaster.size(); i++) {
            if (iReligionID == CasteReligionIDMaster.get(i)) {
                CasteForReligionMarathiDescription.add(CasteMarathiDescriptionMaster.get(i));
                CasteIDForReligion.add(CasteIDMaster.get(i));
                CasteCategoryIDForReligion.add(CasteCategoryIDMaster.get(i));
            }
        }
        ArrayAdapter<String> adapterCaste = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item_center, CasteForReligionMarathiDescription);
        adapterCaste.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        sp = (Spinner) rootView.findViewById(R.id.spinnerCaste);
        sp.setAdapter(adapterCaste);
        sp.setSelection(0);
        for(int i=0; i<CasteIDForReligion.size(); i++) {
            if(CasteIDForReligion.get(i) == mainActivity.Member.get(mainActivity.currentMemberIndex).CasteID) {
                sp.setSelection(i);
                selectedCastePosition = i;
                mainActivity.Member.get(mainActivity.currentMemberIndex).CasteCategoryID = CasteCategoryIDForReligion.get(i);
                break;
            }
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerRelation0);
        if(mainActivity.currentMemberIndex == 0) {
            sp.setSelection(RelationIndexMainMember);    //Main Member
            sp.setEnabled(false);
        } else {
            sp.setSelection(0);
            for(int i=0; i<RelationIDMaster.size(); i++) {
                if(RelationIDMaster.get(i) == mainActivity.Member.get(mainActivity.currentMemberIndex).RelationID) {
                    sp.setSelection(i);
                    break;
                }
            }
            sp.setEnabled(true);
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerMaritalStatus);
        sp.setSelection(0);
        for(int i=0; i<MaritalStatusIDMaster.size(); i++) {
            if(MaritalStatusIDMaster.get(i) == mainActivity.Member.get(mainActivity.currentMemberIndex).MaritalStatusID) {
                sp.setSelection(i);

                //Disallow changing group formation criteria
                if(     (mainActivity.currentMemberIndex == 0) &&
                        (mainActivity.newGroup.GroupStatus != clsNewGroup.GROUP_STATUS_NO_LOAN) )
                    sp.setEnabled(false);
                else
                    sp.setEnabled(true);

                break;
            }
        }

        sp = (Spinner) rootView.findViewById(R.id.spinnerGender);
        sp.setSelection(0);
        for(int i=0; i<GenderIDMaster.length; i++) {
            if(GenderIDMaster[i].equalsIgnoreCase(mainActivity.Member.get(mainActivity.currentMemberIndex).Gender)) {
                sp.setSelection(i);
                break;
            }
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerQualification);
        sp.setSelection(0);
        for(int i=0; i<QualificationIDMaster.size(); i++) {
            if(QualificationIDMaster.get(i) == mainActivity.PersonalImpactInfo.get(mainActivity.currentMemberIndex).QualificationID) {
                sp.setSelection(i);
                break;
            }
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerIllness);
        sp.setSelection(0);
        for(int i=0; i<HealthProblemIDMaster.size(); i++) {
            if(HealthProblemIDMaster.get(i) == mainActivity.PersonalImpactInfo.get(mainActivity.currentMemberIndex).HealthProblemID) {
                sp.setSelection(i);
                break;
            }
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerAddiction);
        sp.setSelection(0);
        for(int i=0; i<HabbitAddictionIDMaster.size(); i++) {
            if(HabbitAddictionIDMaster.get(i) == mainActivity.PersonalImpactInfo.get(mainActivity.currentMemberIndex).HabitID) {
                sp.setSelection(i);
                break;
            }
        }
        sp = (Spinner) rootView.findViewById(R.id.spinnerOccupation);
        sp.setSelection(0);
        for(int i=0; i<BusinessIDMaster.size(); i++) {
            if(BusinessIDMaster.get(i) == mainActivity.PersonalImpactInfo.get(mainActivity.currentMemberIndex).BusinessID) {
                sp.setSelection(i);
                break;
            }
        }

        if(mainActivity.currentMemberIndex > 0) {
//bmp 19-Jun-25
//            EditText et = (EditText) rootView.findViewById(R.id.editTextFname);
//            et.setEnabled(viewStatus);
//            et = (EditText) rootView.findViewById(R.id.editTextMname);
//            et.setEnabled(viewStatus);
//            et = (EditText) rootView.findViewById(R.id.editTextLname);
//            et.setEnabled(viewStatus);
            boolean flagEnable = viewStatus;
            String strMemberID = String.valueOf(mainActivity.Member.get(mainActivity.currentMemberIndex).MemberID);
//bmp 20-Jun-25
//            if(strMemberID.length() < 18) {
            if(strMemberID.length() < 17) {
//bmp 20-Jun-25
                flagEnable = false;
            }
            EditText et = (EditText) rootView.findViewById(R.id.editTextFname);
            et.setEnabled(flagEnable);
            et = (EditText) rootView.findViewById(R.id.editTextMname);
            et.setEnabled(flagEnable);
            et = (EditText) rootView.findViewById(R.id.editTextLname);
            et.setEnabled(flagEnable);
//bmp 19-Jun-25
            et = (EditText) rootView.findViewById(R.id.editTextAddress);
            et.setEnabled(viewStatus);
            et = (EditText) rootView.findViewById(R.id.editTextPincode);
            et.setEnabled(viewStatus);
            Spinner sp1 = (Spinner) rootView.findViewById(R.id.spinnerReligion);
            sp1.setEnabled(viewStatus);
            sp1 = (Spinner) rootView.findViewById(R.id.spinnerCaste);
            sp1.setEnabled(viewStatus);
            sp1 = (Spinner) rootView.findViewById(R.id.spinnerQualification);
            sp1.setEnabled(viewStatus);
//bmp 19-Jun-25
//            sp1 = (Spinner) rootView.findViewById(R.id.spinnerRelation0);
//            sp1.setEnabled(viewStatus);
//            et = (EditText) rootView.findViewById(R.id.editTextAge);
//            et.setEnabled(viewStatus);
//            sp1 = (Spinner) rootView.findViewById(R.id.spinnerGender);
//            sp1.setEnabled(viewStatus);
            sp1 = (Spinner) rootView.findViewById(R.id.spinnerRelation0);
            sp1.setEnabled(flagEnable);
            et = (EditText) rootView.findViewById(R.id.editTextAge);
            et.setEnabled(flagEnable);
            sp1 = (Spinner) rootView.findViewById(R.id.spinnerGender);
            sp1.setEnabled(flagEnable);
//bmp 19-Jun-25
            sp1 = (Spinner) rootView.findViewById(R.id.spinnerOccupation);
            sp1.setEnabled(viewStatus);
            et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncome);
            et.setEnabled(viewStatus);
            sp1 = (Spinner) rootView.findViewById(R.id.spinnerMaritalStatus);
            sp1.setEnabled(viewStatus);
            et = (EditText) rootView.findViewById(R.id.editTextMobileNumber);
            et.setEnabled(viewStatus);
            sp1 = (Spinner) rootView.findViewById(R.id.spinnerAddiction);
            sp1.setEnabled(viewStatus);
            sp1 = (Spinner) rootView.findViewById(R.id.spinnerIllness);
            sp1.setEnabled(viewStatus);
            RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupHMF);
            rg.setEnabled(viewStatus);
            RadioButton rb = (RadioButton) rootView.findViewById(R.id.radioButtonHMFyes);
            rb.setEnabled(viewStatus);
            rb = (RadioButton) rootView.findViewById(R.id.radioButtonHMFno);
            rb.setEnabled(viewStatus);
            rg = (RadioGroup) rootView.findViewById(R.id.radioGroupHeir);
            rg.setEnabled(viewStatus);
            rb = (RadioButton) rootView.findViewById(R.id.radioButtonHeirYes);
            rb.setEnabled(viewStatus);
            rb = (RadioButton) rootView.findViewById(R.id.radioButtonHeirNo);
            rb.setEnabled(viewStatus);
//bmp 26-Mar-24
//            rg = (RadioGroup) rootView.findViewById(R.id.radioGroupGuarantor);
//            rg.setEnabled(viewStatus);
//            rb = (RadioButton) rootView.findViewById(R.id.radioButtonGuarantorYes);
//            rb.setEnabled(viewStatus);
//            rb = (RadioButton) rootView.findViewById(R.id.radioButtonGuarantorNo);
//            rb.setEnabled(viewStatus);
//bmp 26-Mar-24
        } else {
            EditText et = (EditText) rootView.findViewById(R.id.editTextFname);
            et.setEnabled(false);
            et = (EditText) rootView.findViewById(R.id.editTextMname);
            et.setEnabled(false);
            et = (EditText) rootView.findViewById(R.id.editTextLname);
            et.setEnabled(false);
            et = (EditText) rootView.findViewById(R.id.editTextAge);
            et.setEnabled(false);
            Spinner sp1 = (Spinner) rootView.findViewById(R.id.spinnerOccupation);
            sp1.setEnabled(false);
        }
////bmp 26-Dec-24
//        if(IsMemberGuarantor) {
//            EditText et = (EditText) rootView.findViewById(R.id.editTextFname);
//            et.setEnabled(false);
//            et = (EditText) rootView.findViewById(R.id.editTextMname);
//            et.setEnabled(false);
//            et = (EditText) rootView.findViewById(R.id.editTextLname);
//            et.setEnabled(false);
//            et = (EditText) rootView.findViewById(R.id.editTextAddress);
//            et.setEnabled(false);
//            et = (EditText) rootView.findViewById(R.id.editTextPincode);
//            et.setEnabled(false);
////            Spinner sp1 = (Spinner) rootView.findViewById(R.id.spinnerReligion);
////            sp1.setEnabled(viewStatus);
////            sp1 = (Spinner) rootView.findViewById(R.id.spinnerCaste);
////            sp1.setEnabled(viewStatus);
////            sp1 = (Spinner) rootView.findViewById(R.id.spinnerQualification);
////            sp1.setEnabled(viewStatus);
//            Spinner sp1 = (Spinner) rootView.findViewById(R.id.spinnerRelation0);
//            sp1.setEnabled(false);
//            et = (EditText) rootView.findViewById(R.id.editTextAge);
//            et.setEnabled(false);
//            sp1 = (Spinner) rootView.findViewById(R.id.spinnerGender);
//            sp1.setEnabled(false);
////            sp1 = (Spinner) rootView.findViewById(R.id.spinnerOccupation);
////            sp1.setEnabled(viewStatus);
////            et = (EditText) rootView.findViewById(R.id.editTextMonthlyIncome);
////            et.setEnabled(viewStatus);
////            sp1 = (Spinner) rootView.findViewById(R.id.spinnerMaritalStatus);
////            sp1.setEnabled(viewStatus);
////            et = (EditText) rootView.findViewById(R.id.editTextMobileNumber);
////            et.setEnabled(viewStatus);
////            sp1 = (Spinner) rootView.findViewById(R.id.spinnerAddiction);
////            sp1.setEnabled(viewStatus);
////            sp1 = (Spinner) rootView.findViewById(R.id.spinnerIllness);
////            sp1.setEnabled(viewStatus);
////            RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupHMF);
////            rg.setEnabled(viewStatus);
////            RadioButton rb = (RadioButton) rootView.findViewById(R.id.radioButtonHMFyes);
////            rb.setEnabled(viewStatus);
////            rb = (RadioButton) rootView.findViewById(R.id.radioButtonHMFno);
////            rb.setEnabled(viewStatus);
////            rg = (RadioGroup) rootView.findViewById(R.id.radioGroupHeir);
////            rg.setEnabled(viewStatus);
////            rb = (RadioButton) rootView.findViewById(R.id.radioButtonHeirYes);
////            rb.setEnabled(viewStatus);
////            rb = (RadioButton) rootView.findViewById(R.id.radioButtonHeirNo);
////            rb.setEnabled(viewStatus);
//        }
////bmp 26-Dec-24

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
            mainActivity.FragmentVisibleToUser = mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_FAMILY;

            RefreshScreen();
        }
    }

    public void SQLite_getReligionMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.ReligionMaster.COLUMN_NAME_ReligionID,
                ufinDbContract.ReligionMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.ReligionMaster.COLUMN_NAME_SequenceID,
        };
        String Active = "1";

        String selection1 = ufinDbContract.ReligionMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.ReligionMaster.COLUMN_NAME_SequenceID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.ReligionMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        ReligionIDMaster.clear();
        ReligionMarathiDescriptionMaster.clear();
        ReligionIDMaster.add(0);
        ReligionMarathiDescriptionMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            ReligionIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReligionMaster.COLUMN_NAME_ReligionID))));
                            String str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.ReligionMaster.COLUMN_NAME_MarathiDescription));
                            if (str1 == null) {
                                str1 = "?";
                            }
                            ReligionMarathiDescriptionMaster.add(str1);
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
    public void SQLite_getRelationMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.RelationMaster.COLUMN_NAME_RelationID,
                ufinDbContract.RelationMaster.COLUMN_NAME_Description,
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
        RelationIDMaster.add(0);
        RelationMarathiDescriptionMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            RelationIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.RelationMaster.COLUMN_NAME_RelationID))));
                            String str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.RelationMaster.COLUMN_NAME_MarathiDescription));
                            if (str1 == null) {
                                str1 = "?";
                            }
                            RelationMarathiDescriptionMaster.add(str1);
                            String str2 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.RelationMaster.COLUMN_NAME_Description));
                            if(str2.equalsIgnoreCase("Main Member")) {
                                RelationIndexMainMember = RelationIDMaster.size() - 1;
                                RelationIDMainMember = RelationIDMaster.get(RelationIndexMainMember);
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
    public void SQLite_getQualificationMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.QualificationMaster.COLUMN_NAME_QualificationID,
                ufinDbContract.QualificationMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.QualificationMaster.COLUMN_NAME_SequenceID,
        };
        String Active = "1";

        String selection1 = ufinDbContract.QualificationMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.QualificationMaster.COLUMN_NAME_SequenceID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.QualificationMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        QualificationIDMaster.clear();
        QualificationMarathiDescriptionMaster.clear();
        QualificationIDMaster.add(0);
        QualificationMarathiDescriptionMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            QualificationIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.QualificationMaster.COLUMN_NAME_QualificationID))));
                            String str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.QualificationMaster.COLUMN_NAME_MarathiDescription));
                            if (str1 == null) {
                                str1 = "?";
                            }
                            QualificationMarathiDescriptionMaster.add(str1);
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
    public void SQLite_getMaritalStatusMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.MaritalStatusMaster.COLUMN_NAME_MaritalStatusID,
                ufinDbContract.MaritalStatusMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.MaritalStatusMaster.COLUMN_NAME_SequenceID,
        };
        String Active = "1";

        String selection1 = ufinDbContract.MaritalStatusMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.MaritalStatusMaster.COLUMN_NAME_SequenceID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.MaritalStatusMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        MaritalStatusIDMaster.clear();
        MaritalStatusMarathiDescriptionMaster.clear();
        MaritalStatusIDMaster.add(0);
        MaritalStatusMarathiDescriptionMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            MaritalStatusIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.MaritalStatusMaster.COLUMN_NAME_MaritalStatusID))));
                            String str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.MaritalStatusMaster.COLUMN_NAME_MarathiDescription));
                            if (str1 == null) {
                                str1 = "?";
                            }
                            MaritalStatusMarathiDescriptionMaster.add(str1);
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
    public void SQLite_getHealthProblemMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.HealthProblemMaster.COLUMN_NAME_HealthProblemID,
                ufinDbContract.HealthProblemMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.HealthProblemMaster.COLUMN_NAME_SequenceID,
        };
        String Active = "1";

        String selection1 = ufinDbContract.HealthProblemMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.HealthProblemMaster.COLUMN_NAME_SequenceID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.HealthProblemMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        HealthProblemIDMaster.clear();
        HealthProblemMarathiDescriptionMaster.clear();
        HealthProblemIDMaster.add(0);
        HealthProblemMarathiDescriptionMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            HealthProblemIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.HealthProblemMaster.COLUMN_NAME_HealthProblemID))));
                            String str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.HealthProblemMaster.COLUMN_NAME_MarathiDescription));
                            if (str1 == null) {
                                str1 = "?";
                            }
                            HealthProblemMarathiDescriptionMaster.add(str1);
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
    public void SQLite_getHabbitAddictionMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.HabbitAddictionMaster.COLUMN_NAME_HabbitAddictionID,
                ufinDbContract.HabbitAddictionMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.HabbitAddictionMaster.COLUMN_NAME_SequenceID,
        };
        String Active = "1";

        String selection1 = ufinDbContract.HabbitAddictionMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.HabbitAddictionMaster.COLUMN_NAME_SequenceID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.HabbitAddictionMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        HabbitAddictionIDMaster.clear();
        HabbitAddictionMarathiDescriptionMaster.clear();
        HabbitAddictionIDMaster.add(0);
        HabbitAddictionMarathiDescriptionMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            HabbitAddictionIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.HabbitAddictionMaster.COLUMN_NAME_HabbitAddictionID))));
                            String str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.HabbitAddictionMaster.COLUMN_NAME_MarathiDescription));
                            if (str1 == null) {
                                str1 = "?";
                            }
                            HabbitAddictionMarathiDescriptionMaster.add(str1);
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
        BusinessIDMaster.add(0);
        BusinessMarathiDescriptionMaster.add("");

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            BusinessIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.BusinessMaster.COLUMN_NAME_BusinessID))));
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
    public void SQLite_getCasteMaster() {
        ufinDbHelper mDbHelper = new ufinDbHelper(getActivity().getBaseContext());
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection1 = {
                ufinDbContract.CasteMaster.COLUMN_NAME_CasteID,
                ufinDbContract.CasteMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.CasteMaster.COLUMN_NAME_CasteReligionID,
                ufinDbContract.CasteMaster.COLUMN_NAME_SequenceID,
                ufinDbContract.CasteMaster.COLUMN_NAME_CategoryID,
        };
        String Active = "1";

        String selection1 = ufinDbContract.CasteMaster.COLUMN_NAME_IsActive + "=" + "'" + Active + "'";

        // How you want the results sorted in the resulting Cursor
        String sortOrder1 = ufinDbContract.CasteMaster.COLUMN_NAME_SequenceID + " ASC";

        Cursor c1 = db.query(
                ufinDbContract.CasteMaster.TABLE_NAME,  // The table to query
                projection1,                               // The columns to return
                selection1,                                // The columns for the WHERE clause
                null,
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder1                                 // The sort order
        );

        CasteIDMaster.clear();
        CasteMarathiDescriptionMaster.clear();
        CasteReligionIDMaster.clear();
        CasteCategoryIDMaster.clear();

        try {
            if (c1 != null && c1.moveToFirst()) {
                if (c1.getCount() > 0) {
                    try {
                        int iCount = c1.getCount();
                        for (int i = 0; i < iCount; i++) {
                            CasteIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CasteMaster.COLUMN_NAME_CasteID))));
                            CasteReligionIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CasteMaster.COLUMN_NAME_CasteReligionID))));
                            CasteCategoryIDMaster.add(Integer.valueOf(c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CasteMaster.COLUMN_NAME_CategoryID))));

                            String str1 = c1.getString(c1.getColumnIndexOrThrow(ufinDbContract.CasteMaster.COLUMN_NAME_MarathiDescription));
                            if (str1 == null) {
                                str1 = "?";
                            }
                            CasteMarathiDescriptionMaster.add(str1);
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