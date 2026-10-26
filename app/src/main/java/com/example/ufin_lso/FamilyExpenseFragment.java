package com.example.ufin_lso;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FamilyExpenseFragment extends Fragment {

    private static final int TEXT_SP = 18;
    private static final int SMALL_SP = 16;
    private static final String[] EXPENSE_LABELS = {
            "१) घरखर्च (किराणा)",
            "२) घरभाडे (घर भाड्याचे असेल तर)",
            "३) लाईटबिल",
            "४) प्रॉपर्टी टॅक्स + पाणीपट्टी",
            "५) शिक्षणाचा खर्च",
            "६) आजारपणाचा खर्च",
            "७) इतर खर्च :\n अ) सण-समारंभ\n ब) पाहुणचार\n क) पूजा-अर्चा",
            "८) महिन्याला भरण्याची रक्कम\n अ) भिशी\n ब) फंड\n क) बचत गट",
            "९) इन्शुरन्स /एलआयसी साठी भरणारी रक्कम\n (त्रैमासीक,सहामाही,वार्षिक)\n (महिना विभागून रक्कम भरावी)",
            "१०) दरमहा बचतीसाठी भरणारी रक्कम (अन्नपूर्णा आर. डी., आधारपूर्णा)"};
    private static final int I_FOOD = 0, I_RENT = 1, I_EDU = 4, I_MED = 5, I_OTHER = 6, I_BHISHI = 7;

    private static final String[] INCREASE_LABELS = {
            "'ए' ग्रेड गट बनवल्याबद्दल",
            "आधीच्या कर्जाची उत्कृष्ट कर्जफेड केल्याबद्दल",
            "बचत, शेअर्स ची रक्कम काढून न घेतल्याबद्दल",
            "आर डी नियमित भरल्याबद्दल",
            "आधारपूर्णा नियमित भरल्याबद्दल",
            "फायनान्शीयल लिटरसी ट्रेनिंगला पूर्ण वेळ हजर राहिल्याबद्दल",
            "कम्युनिटी मिटींगला पूर्ण वेळ हजर राहिल्याबद्दल",
            "एजीएमला पूर्ण वेळ हजर राहिल्याबद्दल"};
    private static final String[] MAR_DIGITS = {"१", "२", "३", "४", "५", "६", "७", "८"};

    private View rootView;
    private MainActivity mainActivity;
    private long currentMemberId = 0;
    private long totalIncome = 0;
    private LinearLayout ll;
    private ScrollView scrollView;
    private View bottomSpacer;
    private final List<EditText> expenseEdits = new ArrayList<>();
    private final List<EditText> increaseEdits = new ArrayList<>();
    private EditText etTotalExpense, etMfi, etIncome, etHalfHome, etHalfInst, etExpSummary,
            etMfiSummary, etBalHome, etBalInst, etFitted, etIncreaseTotal, etNextLoan;
    private Button btSave, btClose, btPrevious, btNext;
    private boolean saving = false;
    private String baseline = null;
    private final ExecutorService saveExecutor = Executors.newSingleThreadExecutor();
    private int oldSoftInputMode = -1;

    public FamilyExpenseFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_family_expense, container, false);
        mainActivity = (MainActivity) getActivity();
        if (getArguments() != null) {
            currentMemberId = getArguments().getLong(FamilyIncomeFragment.ARG_MEMBER_ID, 0);
            totalIncome = getArguments().getLong(FamilyIncomeFragment.ARG_TOTAL_INCOME, 0);
        }
        if (totalIncome <= 0) {
            try { totalIncome = mainActivity.FamilyImpactInfo.FamilyTotalIncome; } catch (Exception ignored) { }
        }
        ll = rootView.findViewById(R.id.llExpenseFields);
        scrollView = rootView.findViewById(R.id.scrollFamilyExpense);
        btPrevious = rootView.findViewById(R.id.buttonPrevious);
        btClose = rootView.findViewById(R.id.buttonCloseFamilyExpense);
        btSave = rootView.findViewById(R.id.buttonSaveFamilyExpense);
        btNext = rootView.findViewById(R.id.buttonNext);

        loadExpenseData();
        buildScreen();
        prefill();
        recalc();
        setupButtons();
        baseline = signature();
        updateSaveEnabled();
        setupKeyboardSpacer();
        return rootView;
    }

    // ------------------------------------------------------------ helpers
    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density);
    }

    private LinearLayout newRow() {
        LinearLayout row = new LinearLayout(requireContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ll.addView(row);
        return row;
    }

    /** Text cell. gravity: Gravity.CENTER or START. */
    private TextView cell(LinearLayout row, String text, float weight, int gravity,
                          boolean bold, boolean green, int sp) {
        TextView t = new TextView(requireContext());
        t.setText(text);
        t.setTextColor(Color.BLACK);
        t.setTextSize(sp);
        t.setTypeface(null, bold ? Typeface.BOLD : Typeface.NORMAL);
        t.setGravity(gravity | Gravity.CENTER_VERTICAL);
        t.setBackgroundResource(R.drawable.customborder);
        t.setPadding(dp(6), dp(8), dp(6), dp(8));
        t.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, weight));
        row.addView(t);
        return t;
    }

    /** Number cell (editable or read-only), text centered. */
    private EditText numCell(LinearLayout row, float weight, boolean editable, int sp) {
        EditText e = new EditText(requireContext());
        e.setTextColor(Color.BLACK);
        e.setTextSize(sp);
        e.setGravity(Gravity.CENTER);
        e.setBackgroundResource(R.drawable.customborder);
        e.setPadding(dp(4), dp(8), dp(4), dp(8));
        e.setInputType(InputType.TYPE_CLASS_NUMBER);
        e.setKeyListener(android.text.method.DigitsKeyListener.getInstance("0123456789"));
        e.setSingleLine(true);
        e.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_NEXT);
        if (editable) e.setFilters(new android.text.InputFilter[]{new android.text.InputFilter.LengthFilter(6)});
        e.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, weight));
        if (!editable) {
            e.setFocusable(false);
            e.setCursorVisible(false);
            e.setTypeface(null, Typeface.BOLD);
        } else {
            final LinearLayout parentRow = row;
            e.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    v.postDelayed(() -> {
                        if (scrollView != null && isAdded())
                            scrollView.smoothScrollTo(0, Math.max(0, parentRow.getTop() - dp(120)));
                    }, 300);
                }
            });
        }
        row.addView(e);
        return e;
    }

    // ------------------------------------------------------------ screen
    private void buildScreen() {
        ll.removeAllViews();
        expenseEdits.clear();
        increaseEdits.clear();

        // Title
        LinearLayout r = newRow();
        cell(r, "मासिक खर्च", 1f, Gravity.CENTER, true, false, 20);

        // Center : Group : Member line
        r = newRow();
        String name = "";
        try {
            clsMemberData m = mainActivity.Member.get(0);
            name = (m.Fname + " " + m.Mname + " " + m.Lname).replaceAll("\\s+", " ").trim();
        } catch (Exception ignored) { }
        String center = "", group = "";
        try {
            long mid = currentMemberId;
            for (int i = 0; i < mainActivity.RevisitMemberList.size(); i++) {
                clsRevisitMember rm = mainActivity.RevisitMemberList.get(i);
                if (rm.MemberID == mid) {
                    // Center / group number exactly as shown on the Revisit screen
                    center = String.valueOf(rm.CenterID);
                    group = String.valueOf(rm.GroupID);
                    if (group.length() >= 17) {
                        int n = group.length() - 8;
                        center = String.valueOf(rm.CenterID).substring(0, Math.min(n, center.length()));
                        group = group.substring(0, n);
                    }
                    break;
                }
            }
        } catch (Exception ignored) { }
        cell(r, "सेंटर : " + center + "      गट : " + group + "      सभासद : " + name,
                1f, Gravity.START, true, false, TEXT_SP);

        r = newRow();
        cell(r, "कुटूंबाचा एकूण खर्च", 1f, Gravity.CENTER, true, false, 20);

        // A) header
        r = newRow();
        cell(r, "A) कुटुंबाचा महिन्याचा एकूण खर्च", 1f, Gravity.START, true, true, TEXT_SP);
        cell(r, "मासिक खर्च", 1f, Gravity.CENTER, true, true, TEXT_SP);

        for (String label : EXPENSE_LABELS) {
            r = newRow();
            cell(r, label, 1f, Gravity.START, true, true, TEXT_SP);
            expenseEdits.add(numCell(r, 1f, true, TEXT_SP));
        }

        r = newRow();
        cell(r, "कुटूंबाचा एकूण खर्च", 1f, Gravity.CENTER, true, true, TEXT_SP);
        etTotalExpense = numCell(r, 1f, false, TEXT_SP);

        r = newRow();
        cell(r, "B) इतर MFI/ बँकांचा एकूण कर्जाचा हप्ता", 1f,
                Gravity.START, true, true, TEXT_SP);
        etMfi = numCell(r, 1f, true, TEXT_SP);

        // Summary
        r = newRow();
        cell(r, "कुटूंबाचे उत्पन्न व कुटूंबाच्या खर्चाची समरी", 1f, Gravity.CENTER, true, true, 20);

        r = newRow();
        cell(r, "सभासदाचे व कुटूंबाचे एकूण उत्पन्न", 4f, Gravity.CENTER, true, true, TEXT_SP);
        etIncome = numCell(r, 1.4f, false, SMALL_SP);

        r = newRow();
        cell(r, "उत्पन्नातील एकूण ५० टक्के रक्कम घरखर्चासाठी", 3f, Gravity.CENTER, true, true, SMALL_SP);
        etHalfHome = numCell(r, 1.5f, false, SMALL_SP);
        cell(r, "उत्पन्नातील एकूण ५० टक्के रक्कम हप्त्यासाठी", 3f, Gravity.CENTER, true, true, SMALL_SP);
        etHalfInst = numCell(r, 1.5f, false, SMALL_SP);

        r = newRow();
        cell(r, "सभासदाच्या कुटूंबाचा एकूण खर्च", 3f, Gravity.CENTER, true, true, SMALL_SP);
        etExpSummary = numCell(r, 1.5f, false, SMALL_SP);
        cell(r, "इतर MFI/ बँकांचा एकूण कर्जाचा हप्ता", 3f,
                Gravity.CENTER, true, true, SMALL_SP);
        etMfiSummary = numCell(r, 1.5f, false, SMALL_SP);

        r = newRow();
        cell(r, "शिल्लक रक्कम\n(हि रक्कम घरखर्चासाठी बाजूला ठेवणे)", 3f, Gravity.CENTER, true, true, SMALL_SP);
        etBalHome = numCell(r, 1.5f, false, SMALL_SP);
        cell(r, "हप्त्यासाठी शिल्लक रक्कम\n(या बसणा-या हप्त्याच्या रकमे इतके कर्ज सभासदाला देता येईल)", 3f,
                Gravity.CENTER, true, true, SMALL_SP);
        etBalInst = numCell(r, 1.5f, false, SMALL_SP);

        r = newRow();
        cell(r, "सभासदाला मिळू शकणारी कर्ज रक्कम खालील प्रमाणे आहे", 1f, Gravity.CENTER, true, true, TEXT_SP);

        r = newRow();
        cell(r, "सभासदाची फिटलेली कर्ज रक्कम -", 1f, Gravity.START, true, true, TEXT_SP);
        etFitted = numCell(r, 1f, true, TEXT_SP);

        r = newRow();
        cell(r, "पुढील मिळणा-या वाढीव कर्ज रक्कमेचे निकष", 1f, Gravity.CENTER, true, true, TEXT_SP);
        cell(r, "वाढ रू", 1f, Gravity.CENTER, true, true, TEXT_SP);

        for (int i = 0; i < INCREASE_LABELS.length; i++) {
            r = newRow();
            LinearLayout inner = new LinearLayout(requireContext());
            inner.setOrientation(LinearLayout.HORIZONTAL);
            inner.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
            r.addView(inner);
            cell(inner, MAR_DIGITS[i], 0.45f, Gravity.CENTER, true, true, TEXT_SP);
            cell(inner, INCREASE_LABELS[i], 3f, Gravity.START, false, true, SMALL_SP);
            increaseEdits.add(numCell(r, 1f, true, TEXT_SP));
        }

        r = newRow();
        cell(r, "एकूण मिळणारी कर्ज वाढ रक्कम", 1f, Gravity.CENTER, true, true, TEXT_SP);
        etIncreaseTotal = numCell(r, 1f, false, TEXT_SP);

        r = newRow();
        cell(r, "एकूण मिळू शकणारी पुढील कर्ज रक्कम :\n(ही कर्ज रक्कम अशी असावी जेणेकरून कर्जाचा हप्ता हा खर्च वजा जाता  बाकी हप्त्याच्या शिल्लक रकमेत बसेल .)",
                1f, Gravity.CENTER, true, true, SMALL_SP);
        etNextLoan = numCell(r, 1f, false, TEXT_SP);

        // Bottom spacer only while the keyboard is visible; no blank space otherwise
        bottomSpacer = new View(requireContext());
        bottomSpacer.setLayoutParams(new LinearLayout.LayoutParams(1, 0));
        ll.addView(bottomSpacer);

        TextWatcher w = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int a, int b, int c) { }
            public void onTextChanged(CharSequence s, int a, int b, int c) { }
            public void afterTextChanged(Editable s) { recalc(); }
        };
        for (EditText e : expenseEdits) e.addTextChangedListener(w);
        for (EditText e : increaseEdits) e.addTextChangedListener(w);
        etMfi.addTextChangedListener(w);
        etFitted.addTextChangedListener(w);
    }

    private void setSpacer(int h) {
        if (bottomSpacer == null) return;
        ViewGroup.LayoutParams lp = bottomSpacer.getLayoutParams();
        if (lp.height != h) {
            lp.height = h;
            bottomSpacer.setLayoutParams(lp);
        }
    }

    private void setupKeyboardSpacer() {
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            applyImeSpacer(insets);
            return insets;
        });
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (!isAdded()) return;
            applyImeSpacer(ViewCompat.getRootWindowInsets(rootView));
        });
    }

    private void applyImeSpacer(WindowInsetsCompat insets) {
        if (insets == null) return;
        boolean ime = insets.isVisible(WindowInsetsCompat.Type.ime());
        int h = ime ? insets.getInsets(WindowInsetsCompat.Type.ime()).bottom : 0;
        setSpacer(h);
    }

    // ------------------------------------------------------------ data
    private boolean dataLoaded = false;   // was this member's MemberFamilyDetails found in the DB
    private int otherMfiSum = 0;
    private int insuranceAmt = 0;   // 9) insurance / LIC (monthly)
    private int savingsAmt = 0;     // 10) RD + Adharpurna (monthly)

    /**
     * When coming from Revisit, the PIF MemberFamilyDetails / FamilyImpactInfo are not loaded in mainActivity,
     * so they are loaded here from the DB for this member (currentMemberId), same as MainActivity ~28396.
     */
    private void loadExpenseData() {
        dataLoaded = false;
        otherMfiSum = 0;
        insuranceAmt = 0;
        savingsAmt = 0;
        try {
            long mid = currentMemberId;
            if (mid <= 0 && mainActivity.Member != null && mainActivity.Member.size() > 0)
                mid = mainActivity.Member.get(0).MemberID;
            // Insurance and savings (RD / Adharpurna) come from the collection member list (TD_MEMBER_RECOVERY)
            for (int i = 0; i < mainActivity.CollectionMemberList.size(); i++) {
                clsCollectionMember cm = mainActivity.CollectionMemberList.get(i);
                if (cm.MemberID == mid) {
                    insuranceAmt = cm.Monthly_PolicyRenewalAmt;
                    savingsAmt = cm.Monthly_ADHP + cm.RD1MonthlyInstallmentAmt + cm.RD2MonthlyInstallmentAmt
                            + cm.RD3MonthlyInstallmentAmt + cm.RD4MonthlyInstallmentAmt
                            + cm.RD5MonthlyInstallmentAmt;
                    break;
                }
            }
        } catch (Exception ignored) { }
        try {
            long id = currentMemberId;
            if (id <= 0 && mainActivity.Member != null && mainActivity.Member.size() > 0)
                id = mainActivity.Member.get(0).MemberID;
            mainActivity.SQLite_getMemberFamilyDetails(id);
            // do not use stale data left over from another member
            if (mainActivity.MemberFamilyDetails.MemberFamilyDetailsID > 0
                    && mainActivity.MemberFamilyDetails.MemberID == id) {
                mainActivity.SQLite_getFamilyImpactInfo(mainActivity.MemberFamilyDetails.MemberFamilyDetailsID);
                dataLoaded = true;
            }
            mainActivity.SQLite_getMemberOtherMFIData(id);
            for (int i = 0; i < mainActivity.OtherMFIData.size(); i++)
                otherMfiSum += mainActivity.OtherMFIData.get(i).MonthlyInstallment;
        } catch (Exception ignored) { }
    }

    private void prefill() {
        // 9 and 10 are filled automatically but can still be edited
        if (insuranceAmt > 0) expenseEdits.get(8).setText(String.valueOf(insuranceAmt));
        if (savingsAmt > 0) expenseEdits.get(9).setText(String.valueOf(savingsAmt));
        if (!dataLoaded) {
            // No record: only the other-MFI installment (if any)
            if (otherMfiSum > 0) etMfi.setText(String.valueOf(otherMfiSum));
            return;
        }
        try {
            // Rule of the old Expense screen: no house rent if the member owns the house (or LSO_ID == 0)
            boolean ownHouse = false;
            try { ownHouse = mainActivity.PersonalImpactInfo.get(0).OwnHouse == 1; } catch (Exception ignored) { }
            if (ownHouse || mainActivity.LSO_ID == 0) {
                EditText rent = expenseEdits.get(I_RENT);
                rent.setText("");
                rent.setEnabled(false);
            }
            if (mainActivity.FamilyImpactInfo != null) {
                if (!(ownHouse || mainActivity.LSO_ID == 0))
                    set(I_RENT, mainActivity.FamilyImpactInfo.HouseRentExpense);
                set(I_FOOD, mainActivity.FamilyImpactInfo.FoodExpense);
                set(I_EDU, mainActivity.FamilyImpactInfo.EducationExpense);
                set(I_OTHER, mainActivity.FamilyImpactInfo.OtherExpenses);
            }
            if (mainActivity.MemberFamilyDetails != null) {
                set(I_MED, mainActivity.MemberFamilyDetails.MedicalExpenses);
                set(I_BHISHI, mainActivity.MemberFamilyDetails.MonthlyBhishi);
                int inst = mainActivity.MemberFamilyDetails.MonthlyInstallments;
                if (inst <= 0) inst = otherMfiSum;   // sum of the installments of the other MFI loans
                if (inst > 0) etMfi.setText(String.valueOf(inst));
            }
        } catch (Exception ignored) { }
    }

    private void set(int idx, int v) {
        if (v > 0) expenseEdits.get(idx).setText(String.valueOf(v));
    }

    private long val(EditText e) {
        try {
            String s = e.getText().toString().replaceAll("[^0-9]", "");
            return s.isEmpty() ? 0 : Long.parseLong(s);
        } catch (Exception ex) { return 0; }
    }

    private void recalc() {
        if (etNextLoan == null) return;
        long exp = 0;
        for (EditText e : expenseEdits) exp += val(e);
        long mfi = val(etMfi);
        long half = totalIncome / 2;
        long inc = 0;
        for (EditText e : increaseEdits) inc += val(e);
        etTotalExpense.setText(String.valueOf(exp));
        etIncome.setText(String.valueOf(totalIncome));
        etHalfHome.setText(String.valueOf(half));
        etHalfInst.setText(String.valueOf(half));
        etExpSummary.setText(String.valueOf(exp));
        etMfiSummary.setText(String.valueOf(mfi));
        etBalHome.setText(String.valueOf(half - exp));
        etBalInst.setText(String.valueOf(half - mfi));
        etIncreaseTotal.setText(String.valueOf(inc));
        etNextLoan.setText(String.valueOf(val(etFitted) + inc));
        // Used by other screens (RepayFragment: Monthly_Surplus); same formula as the old Expense screen
        try {
            mainActivity.ExpenseTotal = (int) (val(expenseEdits.get(I_RENT)) + val(expenseEdits.get(I_FOOD))
                    + val(expenseEdits.get(I_EDU)) + val(expenseEdits.get(I_MED))
                    + val(expenseEdits.get(I_OTHER)) + val(expenseEdits.get(I_BHISHI)) + mfi);
        } catch (Exception ignored) { }
        updateSaveEnabled();
    }

    private String signature() {
        StringBuilder sb = new StringBuilder();
        for (EditText e : expenseEdits) sb.append(val(e)).append('|');
        sb.append(val(etMfi)).append('|').append(val(etFitted));
        for (EditText e : increaseEdits) sb.append('|').append(val(e));
        return sb.toString();
    }

    private void updateSaveEnabled() {
        if (btSave == null || baseline == null || saving) return;
        btSave.setEnabled(!signature().equals(baseline));
    }

    // ------------------------------------------------------------ buttons / save
    private void setupButtons() {
        btPrevious.setEnabled(true);
        btNext.setEnabled(false);
        btPrevious.setOnClickListener(v -> {
            if (isAdded()) requireActivity().getSupportFragmentManager().popBackStack();
        });
        btClose.setOnClickListener(v -> {
            if (!isAdded() || saving) return;
            FragmentManager fm = requireActivity().getSupportFragmentManager();
            fm.popBackStack();
            fm.popBackStack();
        });
        btSave.setOnClickListener(v -> startSave());
    }

    private void startSave() {
        if (saving) return;
        if (!dataLoaded) {
            Toast.makeText(requireContext(),
                    "या सदस्याचा कुटुंब तपशील सापडला नाही, म्हणून सेव्ह होऊ शकत नाही.", Toast.LENGTH_LONG).show();
            return;
        }
        saving = true;
        btSave.setEnabled(false);
        btClose.setEnabled(false);
        btPrevious.setEnabled(false);
        final int rent = (int) val(expenseEdits.get(I_RENT)), food = (int) val(expenseEdits.get(I_FOOD)),
                edu = (int) val(expenseEdits.get(I_EDU)), med = (int) val(expenseEdits.get(I_MED)),
                bhishi = (int) val(expenseEdits.get(I_BHISHI)), other = (int) val(expenseEdits.get(I_OTHER)),
                inst = (int) val(etMfi);
        saveExecutor.execute(() -> {
            boolean ok = true;
            try {
                mainActivity.FamilyImpactInfo.HouseRentExpense = rent;
                mainActivity.FamilyImpactInfo.FoodExpense = food;
                mainActivity.FamilyImpactInfo.EducationExpense = edu;
                mainActivity.FamilyImpactInfo.OtherExpenses = other;
                mainActivity.MemberFamilyDetails.MedicalExpenses = med;
                mainActivity.MemberFamilyDetails.MonthlyBhishi = bhishi;
                mainActivity.MemberFamilyDetails.MonthlyInstallments = inst;
                long t = Math.max(mainActivity.LastTimeInMillis, System.currentTimeMillis()) + 1000;
                mainActivity.LastTimeInMillis = t;
                ok &= mainActivity.SQLite_saveFamilyImpactInfo(t, true);
                t += 1000;
                mainActivity.LastTimeInMillis = t;
                ok &= mainActivity.SQLite_saveMemberFamilyDetails(t, true);
            } catch (Exception ex) {
                ok = false;
            }
            final boolean result = ok;
            if (!isAdded()) return;
            requireActivity().runOnUiThread(() -> {
                saving = false;
                if (!isAdded()) return;
                if (result) baseline = signature();
                updateSaveEnabled();
                btClose.setEnabled(true);
                btPrevious.setEnabled(true);
                Toast.makeText(requireContext(),
                        result ? "सेव्ह झाले." : "सेव्ह करताना अडचण आली.", Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            oldSoftInputMode = getActivity().getWindow().getAttributes().softInputMode;
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null && oldSoftInputMode != -1) {
            getActivity().getWindow().setSoftInputMode(oldSoftInputMode);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveExecutor.shutdown();
    }
}