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

public class AssetsFragment extends Fragment {

    View rootView;
    MainActivity mainActivity;

    public AssetsFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_assets, container, false);
        mainActivity = (MainActivity) getActivity();

        RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupMobile);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                boolean bUpdated = false;
                int iAnswerID = mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_NONE;
                if (rb.getId() == R.id.radioButtonMobileNone) {
                    iAnswerID = mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_NONE;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_NONE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_ONE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_TWO) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_THREE)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_NONE;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonMobile1) {
                    iAnswerID = mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_ONE;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_NONE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_ONE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_TWO) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_THREE)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_ONE;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonMobile2) {
                    iAnswerID = mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_TWO;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_NONE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_ONE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_TWO) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_THREE)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_TWO;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonMobile3) {
                    iAnswerID = mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_THREE;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_NONE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_ONE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_TWO) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_THREE)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_THREE;
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
        rg = (RadioGroup) rootView.findViewById(R.id.radioGroupCookingFuel);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                boolean bUpdated = false;
                int iAnswerID = mainActivity.ImpactAnswer.ASSETS_COOKING_FUEL_BIO;
                if (rb.getId() == R.id.radioButtonBio) {
                    iAnswerID = mainActivity.ImpactAnswer.ASSETS_COOKING_FUEL_BIO;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_COOKING_FUEL_BIO) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_COOKING_FUEL_STOVE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_COOKING_FUEL_GAS)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ASSETS_COOKING_FUEL_BIO;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonStove) {
                    iAnswerID = mainActivity.ImpactAnswer.ASSETS_COOKING_FUEL_STOVE;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_COOKING_FUEL_BIO) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_COOKING_FUEL_STOVE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_COOKING_FUEL_GAS)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ASSETS_COOKING_FUEL_STOVE;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonGas) {
                    iAnswerID = mainActivity.ImpactAnswer.ASSETS_COOKING_FUEL_GAS;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_COOKING_FUEL_BIO) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_COOKING_FUEL_STOVE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_COOKING_FUEL_GAS)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ASSETS_COOKING_FUEL_GAS;
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
        CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxTVLCD);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateElectricGoods();
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxDVD);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateElectricGoods();
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxPClaptop);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateElectricGoods();
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxNoGadget);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxTVLCD);
                    cb.setChecked(false);
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxDVD);
                    cb.setChecked(false);
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxPClaptop);
                    cb.setChecked(false);
                } else {
                }

                UpdateElectricGoods();
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxCooker);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAppliances();
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxMixer);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAppliances();
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxFridge);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAppliances();
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxWashingMachine);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAppliances();
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxCooler);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAppliances();
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxNoAppliance);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox cb1 = (CheckBox) rootView.findViewById(R.id.checkBoxCooker);
                    cb1.setChecked(false);
                    cb1 = (CheckBox) rootView.findViewById(R.id.checkBoxMixer);
                    cb1.setChecked(false);
                    cb1 = (CheckBox) rootView.findViewById(R.id.checkBoxFridge);
                    cb1.setChecked(false);
                    cb1 = (CheckBox) rootView.findViewById(R.id.checkBoxWashingMachine);
                    cb1.setChecked(false);
                    cb1 = (CheckBox) rootView.findViewById(R.id.checkBoxCooler);
                    cb1.setChecked(false);
                } else {
                }

                UpdateAppliances();
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxVehicleNone);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxCycle);
                    cb.setChecked(false);
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxMotorcycle);
                    cb.setChecked(false);
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxCar);
                    cb.setChecked(false);
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxTempo);
                    cb.setChecked(false);
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxAutoRiksha);
                    cb.setChecked(false);
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxRentalCar);
                    cb.setChecked(false);
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxRentalTempo);
                    cb.setChecked(false);
                    cb = (CheckBox) rootView.findViewById(R.id.checkBoxRentalAutoRiksha);
                    cb.setChecked(false);
                    boolean bUpdated = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bUpdated = true;
                        } else if(  ((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_BICYCLE) ||
                                    ((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_TWO_WHEELER) ||
                                    ((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_FOUR_WHEELER) ||
                                    ((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_THREE_WHEELER) ||
                                    ((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_THREE_FOUR_WHEELER_ONRENT) ) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                    if(!bUpdated) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ASSETS_VEHICLES_NONE;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxCycle);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxVehicleNone);
                    cb.setChecked(false);
                    boolean bBicycle = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_BICYCLE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bBicycle = true;
                        }
                    }
                    if(!bBicycle) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ASSETS_VEHICLES_BICYCLE;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_BICYCLE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxMotorcycle);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxVehicleNone);
                    cb.setChecked(false);
                    boolean bTwoWheeler = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_TWO_WHEELER) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bTwoWheeler = true;
                        }
                    }
                    if(!bTwoWheeler) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ASSETS_VEHICLES_TWO_WHEELER;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_TWO_WHEELER) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxCar);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxVehicleNone);
                    cb.setChecked(false);
                    boolean bFourWheeler = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_FOUR_WHEELER) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bFourWheeler = true;
                        }
                    }
                    if(!bFourWheeler) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ASSETS_VEHICLES_FOUR_WHEELER;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_FOUR_WHEELER) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxTempo);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxVehicleNone);
                    cb.setChecked(false);
                    boolean bThreeWheeler = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_THREE_WHEELER) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bThreeWheeler = true;
                        }
                    }
                    if(!bThreeWheeler) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ASSETS_VEHICLES_THREE_WHEELER;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_THREE_WHEELER) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxAutoRiksha);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxVehicleNone);
                    cb.setChecked(false);
                    boolean bThreeWheeler = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_THREE_WHEELER) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bThreeWheeler = true;
                        }
                    }
                    if(!bThreeWheeler) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ASSETS_VEHICLES_THREE_WHEELER;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_THREE_WHEELER) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxRentalCar);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxVehicleNone);
                    cb.setChecked(false);
                    boolean bThreeFourWheelerOnRent = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_THREE_FOUR_WHEELER_ONRENT) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bThreeFourWheelerOnRent = true;
                        }
                    }
                    if(!bThreeFourWheelerOnRent) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ASSETS_VEHICLES_THREE_FOUR_WHEELER_ONRENT;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_THREE_FOUR_WHEELER_ONRENT) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxRentalTempo);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxVehicleNone);
                    cb.setChecked(false);
                    boolean bThreeFourWheelerOnRent = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_THREE_FOUR_WHEELER_ONRENT) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bThreeFourWheelerOnRent = true;
                        }
                    }
                    if(!bThreeFourWheelerOnRent) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ASSETS_VEHICLES_THREE_FOUR_WHEELER_ONRENT;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_THREE_FOUR_WHEELER_ONRENT) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });
        cb = (CheckBox) rootView.findViewById(R.id.checkBoxRentalAutoRiksha);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    CheckBox cb = (CheckBox) rootView.findViewById(R.id.checkBoxVehicleNone);
                    cb.setChecked(false);
                    boolean bThreeFourWheelerOnRent = false;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_NONE) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_THREE_FOUR_WHEELER_ONRENT) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                            bThreeFourWheelerOnRent = true;
                        }
                    }
                    if(!bThreeFourWheelerOnRent) {
                        clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                        tnewAnswer.Init_MemberImpactAnswer();
                        mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                        clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                        newAnswer.Init_MemberImpactAnswer();
                        newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ASSETS_VEHICLES_THREE_FOUR_WHEELER_ONRENT;
                        newAnswer.IsActive = 1;
                        newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                        mainActivity.MemberImpactAnswer.add(newAnswer);
                    }
                } else {
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_THREE_FOUR_WHEELER_ONRENT) {
                            mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                        }
                    }
                }
            }
        });

        return rootView;
    }

    void UpdateElectricGoods() {
        int iCount = 0;

        CheckBox cb1 = (CheckBox) rootView.findViewById(R.id.checkBoxTVLCD);
        if(cb1.isChecked())
            iCount++;
        CheckBox cb2 = (CheckBox) rootView.findViewById(R.id.checkBoxDVD);
        if(cb2.isChecked())
            iCount++;
        CheckBox cb3 = (CheckBox) rootView.findViewById(R.id.checkBoxPClaptop);
        if(cb3.isChecked())
            iCount++;

        switch (iCount) {
            case 0:
                CheckBox cb4 = (CheckBox) rootView.findViewById(R.id.checkBoxNoGadget);
                cb4.setChecked(true);
                boolean bNone = false;
                for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                    if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_NONE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                        bNone = true;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_ONE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_TWO) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_THREE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    }
                }
                if(!bNone) {
                    clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                    tnewAnswer.Init_MemberImpactAnswer();
                    mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                    clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                    newAnswer.Init_MemberImpactAnswer();
                    newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_NONE;
                    newAnswer.IsActive = 1;
                    newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                    mainActivity.MemberImpactAnswer.add(newAnswer);
                }
                break;
            case 1:
                cb4 = (CheckBox) rootView.findViewById(R.id.checkBoxNoGadget);
                cb4.setChecked(false);
                boolean bOne = false;
                for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                    if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_NONE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_ONE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                        bOne = true;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_TWO) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_THREE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    }
                }
                if(!bOne) {
                    clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                    tnewAnswer.Init_MemberImpactAnswer();
                    mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                    clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                    newAnswer.Init_MemberImpactAnswer();
                    newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_ONE;
                    newAnswer.IsActive = 1;
                    newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                    mainActivity.MemberImpactAnswer.add(newAnswer);
                }
                break;
            case 2:
                cb4 = (CheckBox) rootView.findViewById(R.id.checkBoxNoGadget);
                cb4.setChecked(false);
                boolean bTwo = false;
                for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                    if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_NONE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_ONE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_TWO) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                        bTwo = true;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_THREE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    }
                }
                if(!bTwo) {
                    clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                    tnewAnswer.Init_MemberImpactAnswer();
                    mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                    clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                    newAnswer.Init_MemberImpactAnswer();
                    newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_TWO;
                    newAnswer.IsActive = 1;
                    newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                    mainActivity.MemberImpactAnswer.add(newAnswer);
                }
                break;
            case 3:
                cb4 = (CheckBox) rootView.findViewById(R.id.checkBoxNoGadget);
                cb4.setChecked(false);
                boolean bThree = false;
                for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                    if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_NONE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_ONE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_TWO) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_THREE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                        bThree = true;
                    }
                }
                if(!bThree) {
                    clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                    tnewAnswer.Init_MemberImpactAnswer();
                    mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                    clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                    newAnswer.Init_MemberImpactAnswer();
                    newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_THREE;
                    newAnswer.IsActive = 1;
                    newAnswer.MemberFamilyDetailsID = mainActivity.MemberFamilyDetails.MemberFamilyDetailsID;
                    mainActivity.MemberImpactAnswer.add(newAnswer);
                }
                break;
        }
    }

    void UpdateAppliances() {
        int iCount = 0;
        CheckBox cb5 = (CheckBox) rootView.findViewById(R.id.checkBoxCooker);
        if(cb5.isChecked())
            iCount++;
        CheckBox cb6 = (CheckBox) rootView.findViewById(R.id.checkBoxMixer);
        if(cb6.isChecked())
            iCount++;
        CheckBox cb7 = (CheckBox) rootView.findViewById(R.id.checkBoxFridge);
        if(cb7.isChecked())
            iCount++;
        CheckBox cb8 = (CheckBox) rootView.findViewById(R.id.checkBoxWashingMachine);
        if(cb8.isChecked())
            iCount++;
        CheckBox cb9 = (CheckBox) rootView.findViewById(R.id.checkBoxCooler);
        if(cb9.isChecked())
            iCount++;
        switch (iCount) {
            case 0:
                CheckBox cb10 = (CheckBox) rootView.findViewById(R.id.checkBoxNoAppliance);
                cb10.setChecked(true);
                boolean bNone = false;
                for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                    if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_NONE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                        bNone = true;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_ONE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_TWO) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_THREE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    }
                }
                if(!bNone) {
                    clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                    tnewAnswer.Init_MemberImpactAnswer();
                    mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                    clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                    newAnswer.Init_MemberImpactAnswer();
                    newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_NONE;
                    newAnswer.IsActive = 1;
                    newAnswer.MemberFamilyDetailsID = mainActivity.MemberImpactAnswer.get(0).MemberFamilyDetailsID;
                    mainActivity.MemberImpactAnswer.add(newAnswer);
                }
                break;
            case 1:
                cb10 = (CheckBox) rootView.findViewById(R.id.checkBoxNoAppliance);
                cb10.setChecked(false);
                boolean bOne = false;
                for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                    if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_NONE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_ONE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                        bOne = true;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_TWO) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_THREE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    }
                }
                if(!bOne) {
                    clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                    tnewAnswer.Init_MemberImpactAnswer();
                    mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                    clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                    newAnswer.Init_MemberImpactAnswer();
                    newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_ONE;
                    newAnswer.IsActive = 1;
                    newAnswer.MemberFamilyDetailsID = mainActivity.MemberImpactAnswer.get(0).MemberFamilyDetailsID;
                    mainActivity.MemberImpactAnswer.add(newAnswer);
                }
                break;
            case 2:
                cb10 = (CheckBox) rootView.findViewById(R.id.checkBoxNoAppliance);
                cb10.setChecked(false);
                boolean bTwo = false;
                for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                    if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_NONE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_ONE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_TWO) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                        bTwo = true;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_THREE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    }
                }
                if(!bTwo) {
                    clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                    tnewAnswer.Init_MemberImpactAnswer();
                    mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                    clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                    newAnswer.Init_MemberImpactAnswer();
                    newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_TWO;
                    newAnswer.IsActive = 1;
                    newAnswer.MemberFamilyDetailsID = mainActivity.MemberImpactAnswer.get(0).MemberFamilyDetailsID;
                    mainActivity.MemberImpactAnswer.add(newAnswer);
                }
                break;
            case 3:
            case 4:
            case 5:
                cb10 = (CheckBox) rootView.findViewById(R.id.checkBoxNoAppliance);
                cb10.setChecked(false);
                boolean bThreeOrMore = false;
                for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                    if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_NONE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_ONE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_TWO) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 0;
                    } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_THREE) {
                        mainActivity.MemberImpactAnswer.get(i).IsActive = 1;
                        bThreeOrMore = true;
                    }
                }
                if(!bThreeOrMore) {
                    clsMemberImpactAnswer tnewAnswer = new clsMemberImpactAnswer();
                    tnewAnswer.Init_MemberImpactAnswer();
                    mainActivity.tMemberImpactAnswer.add(tnewAnswer);
                    clsMemberImpactAnswer newAnswer = new clsMemberImpactAnswer();
                    newAnswer.Init_MemberImpactAnswer();
                    newAnswer.IPAnswerID = mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_THREE;
                    newAnswer.IsActive = 1;
                    newAnswer.MemberFamilyDetailsID = mainActivity.MemberImpactAnswer.get(0).MemberFamilyDetailsID;
                    mainActivity.MemberImpactAnswer.add(newAnswer);
                }
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            if(mainActivity == null)
                return;

            mainActivity.currentMemberIndex = 0;
            mainActivity.FragmentVisibleToUser = mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_ASSETS;
            RefreshScreen();
        }
    }

    public void RefreshScreen() {

        RadioGroup rg1 = (RadioGroup) rootView.findViewById(R.id.radioGroupMobile);
        RadioGroup rg2 = (RadioGroup) rootView.findViewById(R.id.radioGroupCookingFuel);
        CheckBox cb1 = (CheckBox) rootView.findViewById(R.id.checkBoxTVLCD);
        cb1.setChecked(false);
        CheckBox cb2 = (CheckBox) rootView.findViewById(R.id.checkBoxDVD);
        cb2.setChecked(false);
        CheckBox cb3 = (CheckBox) rootView.findViewById(R.id.checkBoxPClaptop);
        cb3.setChecked(false);
        CheckBox cb4 = (CheckBox) rootView.findViewById(R.id.checkBoxNoGadget);
        cb4.setChecked(false);

        CheckBox cb5 = (CheckBox) rootView.findViewById(R.id.checkBoxCooker);
        cb5.setChecked(false);
        CheckBox cb6 = (CheckBox) rootView.findViewById(R.id.checkBoxMixer);
        cb6.setChecked(false);
        CheckBox cb7 = (CheckBox) rootView.findViewById(R.id.checkBoxFridge);
        cb7.setChecked(false);
        CheckBox cb8 = (CheckBox) rootView.findViewById(R.id.checkBoxWashingMachine);
        cb8.setChecked(false);
        CheckBox cb9 = (CheckBox) rootView.findViewById(R.id.checkBoxCooler);
        cb9.setChecked(false);
        CheckBox cb10 = (CheckBox) rootView.findViewById(R.id.checkBoxNoAppliance);
        cb10.setChecked(false);

        CheckBox cb11 = (CheckBox) rootView.findViewById(R.id.checkBoxVehicleNone);
        cb11.setChecked(false);
        CheckBox cb12 = (CheckBox) rootView.findViewById(R.id.checkBoxCycle);
        cb12.setChecked(false);
        CheckBox cb13 = (CheckBox) rootView.findViewById(R.id.checkBoxMotorcycle);
        cb13.setChecked(false);
        CheckBox cb14 = (CheckBox) rootView.findViewById(R.id.checkBoxCar);
        cb14.setChecked(false);
        CheckBox cb15 = (CheckBox) rootView.findViewById(R.id.checkBoxTempo);
        cb15.setChecked(false);
        CheckBox cb16 = (CheckBox) rootView.findViewById(R.id.checkBoxAutoRiksha);
        cb16.setChecked(false);
        CheckBox cb17 = (CheckBox) rootView.findViewById(R.id.checkBoxRentalCar);
        cb17.setChecked(false);
        CheckBox cb18 = (CheckBox) rootView.findViewById(R.id.checkBoxRentalTempo);
        cb18.setChecked(false);
        CheckBox cb19 = (CheckBox) rootView.findViewById(R.id.checkBoxRentalAutoRiksha);
        cb19.setChecked(false);

        for(int i=0; i<mainActivity.MemberImpactAnswer.size(); i++) {
            if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_NONE) {
                if (mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb4.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_THREE) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1) {
                    cb3.setChecked(true);
                    cb2.setChecked(true);
                    cb1.setChecked(true);
                }
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_ONE) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb1.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_ELECTRIC_GOODS_TWO) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1) {
                    cb2.setChecked(true);
                    cb1.setChecked(true);
                }
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_NONE) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb10.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_ONE) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb5.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_TWO) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1) {
                    cb5.setChecked(true);
                    cb6.setChecked(true);
                }
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_KITCHEN_APPLIANCES_THREE) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1) {
                    cb5.setChecked(true);
                    cb6.setChecked(true);
                    cb7.setChecked(true);
                }
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_NONE) {
                rg1.check(R.id.radioButtonMobileNone);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_ONE) {
                rg1.check(R.id.radioButtonMobile1);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_TWO) {
                rg1.check(R.id.radioButtonMobile2);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_MOBILE_HANDSET_THREE) {
                rg1.check(R.id.radioButtonMobile3);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_NONE) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb11.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_BICYCLE) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb12.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_TWO_WHEELER) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb13.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_THREE_FOUR_WHEELER_ONRENT) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1) {
                    cb17.setChecked(true);
                    cb18.setChecked(true);
                    cb19.setChecked(true);
                }
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_THREE_WHEELER) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb16.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_VEHICLES_FOUR_WHEELER) {
                if(mainActivity.MemberImpactAnswer.get(i).IsActive == 1)
                    cb14.setChecked(true);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_COOKING_FUEL_BIO) {
                rg2.check(R.id.radioButtonBio);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_COOKING_FUEL_STOVE) {
                rg2.check(R.id.radioButtonStove);
            } else if((int) mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ASSETS_COOKING_FUEL_GAS) {
                rg2.check(R.id.radioButtonGas);
            }
        }

        TextView tv = (TextView) rootView.findViewById(R.id.textViewRequestFocus);
        tv.requestFocus();
    }
}
