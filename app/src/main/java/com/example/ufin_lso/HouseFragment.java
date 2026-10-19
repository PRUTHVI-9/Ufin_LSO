package com.example.ufin_lso;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class HouseFragment extends Fragment {
    View rootView;
    MainActivity mainActivity;

    private boolean dbMaster = false;
    private ArrayList<Integer> RelationIDMaster = new ArrayList<Integer>();
    private ArrayList<String> RelationMarathiDescriptionMaster = new ArrayList<String>();
    private ArrayList<Long> HouseOwnerIDMaster = new ArrayList<Long>();
    private ArrayList<String> HouseOwnerDescriptionMaster = new ArrayList<String>();

    public HouseFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_house, container, false);
        mainActivity = (MainActivity) getActivity();

        if(!dbMaster) {
            SQLite_getRelationMaster();
            dbMaster = true;
        }

        HouseOwnerIDMaster.clear();
        HouseOwnerIDMaster.add((long)0);
        HouseOwnerDescriptionMaster.clear();
        HouseOwnerDescriptionMaster.add("");
        for(int i=0; i<mainActivity.Member.size(); i++) {
            if(mainActivity.Member.get(i).IsActive == 1) {
                HouseOwnerIDMaster.add(mainActivity.Member.get(i).MemberID);
                HouseOwnerDescriptionMaster.add(mainActivity.Member.get(i).Fname);
            }
        }
        ArrayAdapter<String> adapterRelation = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.simple_spinner_item, HouseOwnerDescriptionMaster);
        adapterRelation.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerRelation4);
        sp.setAdapter(adapterRelation);
        for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
            if (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOUSE_RENTED) {
                sp.setEnabled(false);
            }
        }
        sp.setEnabled(false);

        RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.radioGroupHouse);
        if(     (mainActivity.currentMemberIndex == 0) &&
                (mainActivity.newGroup.GroupStatus != clsNewGroup.GROUP_STATUS_NO_LOAN) ) {
            rg.setEnabled(false);
            RadioButton rb1 = (RadioButton) rootView.findViewById(R.id.radioButtonHouseYes);
            rb1.setEnabled(false);
            rb1 = (RadioButton) rootView.findViewById(R.id.radioButtonHouseNo);
            rb1.setEnabled(false);
        } else {
            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                    boolean bUpdated = false;
                    int iAnswerID = mainActivity.ImpactAnswer.HOUSE_RENTED;
                    if (rb.getId() == R.id.radioButtonHouseYes) {
                        iAnswerID = mainActivity.ImpactAnswer.HOUSE_OWNED;
                        for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                            if (    (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOUSE_RENTED) ||
                                    (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOUSE_OWNED) ) {
                                mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.HOUSE_OWNED;

                                Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerRelation4);
                                sp.setSelection(0);
                                for(int j=0; j<HouseOwnerIDMaster.size(); j++) {
                                    if(HouseOwnerIDMaster.get(j) == mainActivity.Member.get(0).MemberID) {
                                        sp.setSelection(j);
                                        break;
                                    }
                                }
                                sp.setEnabled(true);

                                bUpdated = true;
                            }
                        }
                    } else if (rb.getId() == R.id.radioButtonHouseNo) {
                        iAnswerID = mainActivity.ImpactAnswer.HOUSE_RENTED;
                        for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                            if (    (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOUSE_RENTED) ||
                                    (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOUSE_OWNED) ) {
                                mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.HOUSE_RENTED;

                                Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerRelation4);
                                sp.setSelection(0);
                                sp.setEnabled(false);

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
        }
        rg = (RadioGroup) rootView.findViewById(R.id.radioGroupRooms);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                boolean bUpdated = false;
                int iAnswerID = mainActivity.ImpactAnswer.ROOMS_ONE_SMALL;
                if (rb.getId() == R.id.radioButton1Small) {
                    iAnswerID = mainActivity.ImpactAnswer.ROOMS_ONE_SMALL;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ROOMS_ONE_SMALL) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ROOMS_ONE_BIG) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ROOMS_TWO_MEDIUM) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ROOMS_THREE_OR_MORE)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ROOMS_ONE_SMALL;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonRooms1Big) {
                    iAnswerID = mainActivity.ImpactAnswer.ROOMS_ONE_BIG;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ROOMS_ONE_SMALL) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ROOMS_ONE_BIG) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ROOMS_TWO_MEDIUM) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ROOMS_THREE_OR_MORE)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ROOMS_ONE_BIG;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonRooms2) {
                    iAnswerID = mainActivity.ImpactAnswer.ROOMS_TWO_MEDIUM;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ROOMS_ONE_SMALL) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ROOMS_ONE_BIG) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ROOMS_TWO_MEDIUM) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ROOMS_THREE_OR_MORE)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ROOMS_TWO_MEDIUM;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonRooms3) {
                    iAnswerID = mainActivity.ImpactAnswer.ROOMS_THREE_OR_MORE;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ROOMS_ONE_SMALL) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ROOMS_ONE_BIG) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ROOMS_TWO_MEDIUM) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ROOMS_THREE_OR_MORE)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ROOMS_THREE_OR_MORE;
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
        rg = (RadioGroup) rootView.findViewById(R.id.radioGroupWalls);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                boolean bUpdated = false;
                int iAnswerID = mainActivity.ImpactAnswer.WALLS_PLASTIC;
                if (rb.getId() == R.id.radioButtonPlastic) {
                    iAnswerID = mainActivity.ImpactAnswer.WALLS_PLASTIC;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WALLS_PLASTIC) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WALLS_SHEET_CLAY) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WALLS_BRICKS) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WALLS_BRICKS_PLASTER)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.WALLS_PLASTIC;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonSheet) {
                    iAnswerID = mainActivity.ImpactAnswer.WALLS_SHEET_CLAY;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WALLS_PLASTIC) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WALLS_SHEET_CLAY) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WALLS_BRICKS) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WALLS_BRICKS_PLASTER)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.WALLS_SHEET_CLAY;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonBrick) {
                    iAnswerID = mainActivity.ImpactAnswer.WALLS_BRICKS;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WALLS_PLASTIC) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WALLS_SHEET_CLAY) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WALLS_BRICKS) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WALLS_BRICKS_PLASTER)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.WALLS_BRICKS;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonPlaster) {
                    iAnswerID = mainActivity.ImpactAnswer.WALLS_BRICKS_PLASTER;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WALLS_PLASTIC) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WALLS_SHEET_CLAY) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WALLS_BRICKS) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WALLS_BRICKS_PLASTER)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.WALLS_BRICKS_PLASTER;
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
        rg = (RadioGroup) rootView.findViewById(R.id.radioGroupFloor);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                boolean bUpdated = false;
                int iAnswerID = mainActivity.ImpactAnswer.FLOOR_CLAY;
                if (rb.getId() == R.id.radioButtonClay) {
                    iAnswerID = mainActivity.ImpactAnswer.FLOOR_CLAY;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.FLOOR_CLAY) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.FLOOR_CEMENT) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.FLOOR_SLATE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.FLOOR_TILES)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.FLOOR_CLAY;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonCement) {
                    iAnswerID = mainActivity.ImpactAnswer.FLOOR_CEMENT;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.FLOOR_CLAY) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.FLOOR_CEMENT) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.FLOOR_SLATE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.FLOOR_TILES)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.FLOOR_CEMENT;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonSlate) {
                    iAnswerID = mainActivity.ImpactAnswer.FLOOR_SLATE;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.FLOOR_CLAY) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.FLOOR_CEMENT) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.FLOOR_SLATE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.FLOOR_TILES)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.FLOOR_SLATE;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonTiles) {
                    iAnswerID = mainActivity.ImpactAnswer.FLOOR_TILES;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.FLOOR_CLAY) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.FLOOR_CEMENT) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.FLOOR_SLATE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.FLOOR_TILES)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.FLOOR_TILES;
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
        rg = (RadioGroup) rootView.findViewById(R.id.radioGroupDrinkingWater);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                boolean bUpdated = false;
                int iAnswerID = mainActivity.ImpactAnswer.WATERTAP_NONE;
                if (rb.getId() == R.id.radioButtonNoTap) {
                    iAnswerID = mainActivity.ImpactAnswer.WATERTAP_NONE;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WATERTAP_NONE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WATERTAP_PUBLIC) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WATERTAP_OWN)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.WATERTAP_NONE;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonCommonTap) {
                    iAnswerID = mainActivity.ImpactAnswer.WATERTAP_PUBLIC;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WATERTAP_NONE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WATERTAP_PUBLIC) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WATERTAP_OWN)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.WATERTAP_PUBLIC;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonPrivateTap) {
                    iAnswerID = mainActivity.ImpactAnswer.WATERTAP_OWN;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WATERTAP_NONE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WATERTAP_PUBLIC) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WATERTAP_OWN)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.WATERTAP_OWN;
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
        rg = (RadioGroup) rootView.findViewById(R.id.radioGroupToilet);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                boolean bUpdated = false;
                int iAnswerID = mainActivity.ImpactAnswer.TOILET_NONE;
                if (rb.getId() == R.id.radioButtonNoToilet) {
                    iAnswerID = mainActivity.ImpactAnswer.TOILET_NONE;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.TOILET_NONE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.TOILET_PUBLIC) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.TOILET_OWN)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.TOILET_NONE;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonCommon) {
                    iAnswerID = mainActivity.ImpactAnswer.TOILET_PUBLIC;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.TOILET_NONE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.TOILET_PUBLIC) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.TOILET_OWN)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.TOILET_PUBLIC;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonPrivate) {
                    iAnswerID = mainActivity.ImpactAnswer.TOILET_OWN;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.TOILET_NONE) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.TOILET_PUBLIC) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.TOILET_OWN)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.TOILET_OWN;
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
        rg = (RadioGroup) rootView.findViewById(R.id.radioGroupElectricity);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                boolean bUpdated = false;
                int iAnswerID = mainActivity.ImpactAnswer.ELECTRICITY_NO;
                if (rb.getId() == R.id.radioButtonEleNo) {
                    iAnswerID = mainActivity.ImpactAnswer.ELECTRICITY_NO;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ELECTRICITY_NO) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ELECTRICITY_YES)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ELECTRICITY_NO;
                            bUpdated = true;
                        }
                    }
                } else if (rb.getId() == R.id.radioButtonEleYes) {
                    iAnswerID = mainActivity.ImpactAnswer.ELECTRICITY_YES;
                    for (int i = 0; i < mainActivity.MemberImpactAnswer.size(); i++) {
                        if ((mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ELECTRICITY_NO) ||
                                (mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ELECTRICITY_YES)) {
                            mainActivity.MemberImpactAnswer.get(i).IPAnswerID = mainActivity.ImpactAnswer.ELECTRICITY_YES;
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
            mainActivity.FragmentVisibleToUser = mainActivity.PIF_FRAGMENT_SEQUENCE_NUMBER_HOUSE;
            RefreshScreen();
        }
    }

    public void RefreshScreen() {
        RadioGroup rg1 = (RadioGroup) rootView.findViewById(R.id.radioGroupHouse);
        RadioGroup rg2 = (RadioGroup) rootView.findViewById(R.id.radioGroupRooms);
        RadioGroup rg3 = (RadioGroup) rootView.findViewById(R.id.radioGroupWalls);
        RadioGroup rg4 = (RadioGroup) rootView.findViewById(R.id.radioGroupFloor);
        RadioGroup rg5 = (RadioGroup) rootView.findViewById(R.id.radioGroupDrinkingWater);
        RadioGroup rg6 = (RadioGroup) rootView.findViewById(R.id.radioGroupToilet);
        RadioGroup rg7 = (RadioGroup) rootView.findViewById(R.id.radioGroupElectricity);
        for(int i=0; i<mainActivity.MemberImpactAnswer.size(); i++) {
            if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOUSE_OWNED) {
                rg1.check(R.id.radioButtonHouseYes);

                //Disallow changing group formation criteria - if already selected
                rg1.setEnabled(false);
                RadioButton rb1 = (RadioButton) rootView.findViewById(R.id.radioButtonHouseYes);
                rb1.setEnabled(false);
                rb1 = (RadioButton) rootView.findViewById(R.id.radioButtonHouseNo);
                rb1.setEnabled(false);

                Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerRelation4);
                sp.setSelection(0);
                for (int j = 0; j < HouseOwnerIDMaster.size(); j++) {
                    if (HouseOwnerIDMaster.get(j) == mainActivity.Member.get(0).MemberID) {
                        sp.setSelection(j);
                        break;
                    }
                }
                sp.setEnabled(false);
            } else if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.HOUSE_RENTED) {
                rg1.check(R.id.radioButtonHouseNo);

                //Disallow changing group formation criteria - if already selected
                rg1.setEnabled(false);
                RadioButton rb1 = (RadioButton) rootView.findViewById(R.id.radioButtonHouseYes);
                rb1.setEnabled(false);
                rb1 = (RadioButton) rootView.findViewById(R.id.radioButtonHouseNo);
                rb1.setEnabled(false);

                Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerRelation4);
                sp.setSelection(0);
                sp.setEnabled(false);
            } else if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ROOMS_ONE_SMALL) {
                rg2.check(R.id.radioButton1Small);
            } else if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ROOMS_ONE_BIG) {
                rg2.check(R.id.radioButtonRooms1Big);
            } else if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ROOMS_TWO_MEDIUM) {
                rg2.check(R.id.radioButtonRooms2);
            } else if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ROOMS_THREE_OR_MORE) {
                rg2.check(R.id.radioButtonRooms3);
            } else if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WALLS_PLASTIC) {
                rg3.check(R.id.radioButtonPlastic);
            } else if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WALLS_SHEET_CLAY) {
                rg3.check(R.id.radioButtonSheet);
            } else if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WALLS_BRICKS) {
                rg3.check(R.id.radioButtonBrick);
            } else if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WALLS_BRICKS_PLASTER) {
                rg3.check(R.id.radioButtonPlaster);
            } else if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.FLOOR_CLAY) {
                rg4.check(R.id.radioButtonClay);
            } else if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.FLOOR_CEMENT) {
                rg4.check(R.id.radioButtonCement);
            } else if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.FLOOR_SLATE) {
                rg4.check(R.id.radioButtonSlate);
            } else if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.FLOOR_TILES) {
                rg4.check(R.id.radioButtonTiles);
            } else if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WATERTAP_NONE) {
                rg5.check(R.id.radioButtonNoTap);
            } else if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WATERTAP_PUBLIC) {
                rg5.check(R.id.radioButtonCommonTap);
            } else if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.WATERTAP_OWN) {
                rg5.check(R.id.radioButtonPrivateTap);
            } else if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.TOILET_NONE) {
                rg6.check(R.id.radioButtonNoToilet);
            } else if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.TOILET_PUBLIC) {
                rg6.check(R.id.radioButtonCommon);
            } else if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.TOILET_OWN) {
                rg6.check(R.id.radioButtonPrivate);
            } else if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ELECTRICITY_NO) {
                rg7.check(R.id.radioButtonEleNo);
            } else if((int)mainActivity.MemberImpactAnswer.get(i).IPAnswerID == mainActivity.ImpactAnswer.ELECTRICITY_YES) {
                rg7.check(R.id.radioButtonEleYes);
            }
        }

        try {
            if(mainActivity.newGroup.arrGroupMember.get(mainActivity.selectedMemberPosition).OwnHouse == 1) {
                rg1.check(R.id.radioButtonHouseYes);
                Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerRelation4);
                sp.setSelection(0);
                for(int j=0; j<HouseOwnerIDMaster.size(); j++) {
                    if(HouseOwnerIDMaster.get(j) == mainActivity.Member.get(0).MemberID) {
                        sp.setSelection(j);
                        break;
                    }
                }
                sp.setEnabled(false);
            } else {
                rg1.check(R.id.radioButtonHouseNo);
                Spinner sp = (Spinner) rootView.findViewById(R.id.spinnerRelation4);
                sp.setSelection(0);
                sp.setEnabled(false);
            }
        } catch (Exception e) {
        }

        TextView tv = (TextView) rootView.findViewById(R.id.textViewRequestFocus);
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
