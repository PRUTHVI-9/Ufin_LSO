package com.example.ufin_lso;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FamilyExpenseFragment extends Fragment {

    private static final int TEXT_SP = 18;
    private static final int SMALL_SP = 16;
    private static final int HEAD_SP = 15;   // column headings of the tables

    private static final String[] EXPENSE_LABELS = {
            "१) प्रॉपर्टी टॅक्स (घर स्वतःचे असेल तर)",
            "२) घरभाडे (घर भाड्याचे असेल तर)",
            "३) लाईटबिल",
            "४) पाणीपट्टी",
            "५) घरखर्च (किराणा)",
            "६) शिक्षणाचा खर्च",
            "७) आजारपणाचा खर्च",
            "८) इतर खर्च :\n अ) सण-समारंभ\n ब) पाहुणचार\n क) पुजा-अर्चा",
            "९) उधारी बाकी असणे. (किराणा, इतर देणी)",
            "१०) महिन्याला भरण्याची बचत रक्कम\n अ) भिशी\n ब) फंड\n क) बचत गट\n ड) बाहेरील RD",
            "११) इन्शुरन्स /एलआयसी साठी भरणारी रक्कम",
            "१२) दरमहा अन्नपूर्णा बचतीसाठी भरणारी रक्कम\n (आर. डी., आधारपूर्णा)"};
    private static final int I_TAX = 0, I_RENT = 1, I_LIGHT = 2, I_WATER = 3, I_FOOD = 4, I_EDU = 5,
            I_MED = 6, I_OTHER = 7, I_CREDIT = 8, I_BHISHI = 9, I_INS = 10, I_SAV = 11;

    // Frequency options of the dropdown (item 11 also allows quarterly / half-yearly, as in the Excel label)
    private static final String[] FREQ = {"Daily", "Weekly", "Monthly", "Yearly"};
    private static final String[] FREQ_INS = {"Daily", "Weekly", "Monthly", "Quarterly", "Half-yearly", "Yearly"};
    // Weeks counted in one month for the "Weekly" option (change here if the business rule differs)
    private static final double WEEKS_PER_MONTH = 4;

    // The loan must end before the member turns this age (same rule as the Repay screen)
    private static final int MAX_AGE_AT_LOAN_END = 65;

    // Column weights of the expense table: label | frequency | rupees | monthly rupees
    private static final float W_LABEL = 2.4f, W_FREQ = 1.7f, W_AMT = 1.1f, W_MON = 1.2f;

    private static final String[] INCREASE_LABELS = {
            "'ए' ग्रेड गट बनवल्याबद्दल",
            "आधीच्या कर्जाची उत्कृष्ट कर्जफेड केल्याबद्दल",
            "बचत, शेअर्स ची रक्कम काढून न घेतल्याबद्दल",
            "आर डी नियमित भरल्याबद्दल",
            "आधारपूर्णा नियमित भरल्याबद्दल",
            "कम्युनिटी मिटींगला पूर्ण वेळ हजर राहिल्याबद्दल",
            "एजीएमला पूर्ण वेळ हजर राहिल्याबद्दल"};
    private static final String[] MAR_DIGITS = {"१", "२", "३", "४", "५", "६", "७"};

    private View rootView;
    private MainActivity mainActivity;
    private long currentMemberId = 0;
    private long totalIncome = 0;
    private LinearLayout ll;
    private ScrollView scrollView;
    private View bottomSpacer;
    private final List<EditText> amtEdits = new ArrayList<>();      // rupees typed by the user (per frequency)
    private final List<EditText> monEdits = new ArrayList<>();      // monthly rupees (calculated, read-only)
    private final List<Spinner> freqSpinners = new ArrayList<>();
    private final List<EditText> increaseEdits = new ArrayList<>();
    private final List<EditText> scoreEdits = new ArrayList<>();    // internal score per criterion
    private EditText etTotalExpense, etMfi, etMfiAmt, etIncome, etHalfHome, etHalfInst, etExpSummary,
            etMfiSummary, etBalHome, etBalInst, etFitted, etIncreaseTotal, etScoreTotal;
    private TextView tvCriteria1;   // label of criteria row 1 ('ए' / 'बी' ग्रेड गट)
    private TextView etNextLoan;   // loan we can give, shown as text: amount, months, installment
    private Spinner spMfi;
    private TextView tvExpenseWarn;   // shown when the family expense is more than 50% of the income
    // Score chart
    private EditText etChartScore1, etChartScore2, etChartLoan1, etChartLoan2,
            etChartScoreTotal, etChartLoanTotal;
    private Button btSave, btClose, btPrevious, btNext;

    // Active loan products (same products and filters as the Repay screen); used to find the largest loan that fits
    private final ArrayList<Integer> allDetailsIds = new ArrayList<>();      // every valid product detail
    private final ArrayList<Integer> allProductIds = new ArrayList<>();
    private final ArrayList<String> allPrincipals = new ArrayList<>();
    private final ArrayList<String> allPeriods = new ArrayList<>();
    private final ArrayList<String> allInstallments = new ArrayList<>();
    private final HashMap<Integer, Boolean> productJointRequired = new HashMap<>();
    private boolean saving = false;
    private String baseline = null;
    private final ExecutorService saveExecutor = Executors.newSingleThreadExecutor();
    private int oldSoftInputMode = -1;

    // true when this screen is shown inside the MainActivity PIF screens (R.layout.newpersonal_info).
    // There MainActivity's own |< < CLOSE SAVE > >| bar is used: this fragment hides its own buttons and
    // keeps MainActivity.FamilyImpactInfo / MemberFamilyDetails up to date, so MainActivity's SAVE saves it.
    // When opened from Revisit this is false and everything works as before.
    private boolean embedded = false;
    private boolean ready = false;    // initial fill finished (do not push the prefilled values to the model)
    private boolean pushed = false;   // user changed something at least once

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

        // Inside MainActivity's PIF screens the activity content view is newpersonal_info
        embedded = (mainActivity != null && mainActivity.CurrentContentView == R.layout.newpersonal_info);

        loadExpenseData();
        loadLoanMaster();
        buildScreen();
        prefill();
        recalc();
        setupButtons();
        baseline = signature();
        updateSaveEnabled();
        setupKeyboardSpacer();
        ready = true;
        return rootView;
    }

    /** Hides this fragment's own < CLOSE SAVE > buttons (PIF screen uses MainActivity's bar). */
    private void hideOwnButtons() {
        Button[] bs = {btPrevious, btClose, btSave, btNext};
        for (Button b : bs) {
            if (b != null) b.setVisibility(View.GONE);
        }
        View p = btClose != null ? (View) btClose.getParent() : null;
        if (p instanceof ViewGroup && p != rootView) {
            ViewGroup g = (ViewGroup) p;
            boolean allGone = true;
            for (int i = 0; i < g.getChildCount(); i++) {
                if (g.getChildAt(i).getVisibility() != View.GONE) {
                    allGone = false;
                    break;
                }
            }
            if (allGone) g.setVisibility(View.GONE);
        }
    }

    private void applyToModel(long[] m, int inst) {
        mainActivity.FamilyImpactInfo.PropertyTax = (int) m[I_TAX];
        mainActivity.FamilyImpactInfo.HouseRentExpense = (int) m[I_RENT];
        mainActivity.FamilyImpactInfo.LightBill = (int) m[I_LIGHT];
        mainActivity.FamilyImpactInfo.PaniPatti = (int) m[I_WATER];
        mainActivity.FamilyImpactInfo.FoodExpense = (int) m[I_FOOD];
        mainActivity.FamilyImpactInfo.EducationExpense = (int) m[I_EDU];
        mainActivity.FamilyImpactInfo.OtherExpenses = (int) m[I_OTHER];   // item 8 (one amount)
        mainActivity.FamilyImpactInfo.UdhariExpense = (int) m[I_CREDIT];
        mainActivity.FamilyImpactInfo.InsuranceExpense = (int) m[I_INS];
        mainActivity.FamilyImpactInfo.AnnPurnaRDADHP = (int) m[I_SAV];

        mainActivity.MemberFamilyDetails.MedicalExpenses = (int) m[I_MED];
        mainActivity.MemberFamilyDetails.MonthlyBhishi = (int) m[I_BHISHI];
        mainActivity.MemberFamilyDetails.MonthlyInstallments = inst;
        setIntField(mainActivity.FamilyImpactInfo, "OtherLoanInstallMent", inst);   // new column
    }

    /** Monthly value of every expense row (frequency applied), in the I_* order. */
    private long[] monthlyValues() {
        long[] m = new long[amtEdits.size()];
        for (int i = 0; i < m.length; i++) m[i] = mon(i);
        return m;
    }

    private void pushToModel() {
        if (!embedded || !ready || baseline == null) return;
        if (!pushed && signature().equals(baseline)) return;
        pushed = true;
        try {
            applyToModel(monthlyValues(), (int) monthlyOf(spMfi, etMfiAmt));
        } catch (Exception ignored) { }
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

    /** Frequency dropdown cell (bordered box holding a Spinner), default = Monthly. */
    private Spinner freqCell(LinearLayout row, String[] options, float weight) {
        FrameLayout box = new FrameLayout(requireContext());
        box.setBackgroundResource(R.drawable.customborder);
        box.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, weight));
        Spinner sp = new Spinner(requireContext());
        ArrayAdapter<String> ad = new ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_spinner_item, options) {
            private View style(View v) {
                if (v instanceof TextView) {
                    TextView t = (TextView) v;
                    t.setTextColor(Color.BLACK);
                    t.setTextSize(14);
                    t.setTypeface(null, Typeface.BOLD);
                    t.setGravity(Gravity.CENTER);
                }
                return v;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return style(super.getView(position, convertView, parent));
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return style(super.getDropDownView(position, convertView, parent));
            }
        };
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(ad);
        sp.setSelection(2);   // Monthly
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recalc();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        box.addView(sp, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL));
        row.addView(box);
        return sp;
    }

    /** Multiplier that converts the entered amount into a monthly amount. */
    private static double factor(String freq) {
        switch (freq) {
            case "Daily": return 30;
            case "Weekly": return WEEKS_PER_MONTH;
            case "Quarterly": return 1 / 3.0;
            case "Half-yearly": return 1 / 6.0;
            case "Yearly": return 1 / 12.0;
            default: return 1;   // Monthly
        }
    }

    private long monthlyOf(Spinner sp, EditText amt) {
        String f = sp.getSelectedItem() == null ? "Monthly" : sp.getSelectedItem().toString();
        return Math.round(val(amt) * factor(f));
    }

    /** One expense row: label | frequency | rupees | monthly rupees. */
    private void expenseRow(String label, String[] options) {
        LinearLayout r = newRow();
        cell(r, label, W_LABEL, Gravity.START, true, true, TEXT_SP);
        freqSpinners.add(freqCell(r, options, W_FREQ));
        amtEdits.add(numCell(r, W_AMT, true, SMALL_SP));
        monEdits.add(numCell(r, W_MON, false, SMALL_SP));
    }

    // ------------------------------------------------------------ screen
    private void buildScreen() {
        ll.removeAllViews();
        amtEdits.clear();
        monEdits.clear();
        freqSpinners.clear();
        increaseEdits.clear();
        scoreEdits.clear();

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
        cell(r, "कुटुंबाचा एकूण खर्च", 1f, Gravity.CENTER, true, false, 20);

        // A) header: label | expense (frequency) | rupees | monthly expense rupees
        r = newRow();
        cell(r, "A) कुटुंबाचा महिन्याचा खर्च", W_LABEL, Gravity.START, true, true, TEXT_SP);
        cell(r, "खर्च\n(Daily, Weekly, Monthly, Yearly)", W_FREQ, Gravity.CENTER, true, true, HEAD_SP);
        cell(r, "रूपये", W_AMT, Gravity.CENTER, true, true, HEAD_SP);
        cell(r, "मासिक खर्च रूपये", W_MON, Gravity.CENTER, true, true, HEAD_SP);

        for (int i = 0; i < EXPENSE_LABELS.length; i++)
            expenseRow(EXPENSE_LABELS[i], i == I_INS ? FREQ_INS : FREQ);

        r = newRow();
        cell(r, "कुटुंबाचा एकूण खर्च", W_LABEL + W_FREQ + W_AMT, Gravity.CENTER, true, true, TEXT_SP);
        etTotalExpense = numCell(r, W_MON, false, SMALL_SP);

        r = newRow();
        TextView mfiLink = cell(r, "B) इतर MFI/ बँकांचा एकूण कर्जाचा हप्ता", W_LABEL, Gravity.START, true, true, TEXT_SP);
        // Link to the MFI details screen
        mfiLink.setTextColor(Color.parseColor("#0D47A1"));
        mfiLink.setPaintFlags(mfiLink.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG);
        mfiLink.setOnClickListener(v -> openMfiDetails());
        spMfi = freqCell(r, FREQ, W_FREQ);
        etMfiAmt = numCell(r, W_AMT, true, SMALL_SP);
        etMfi = numCell(r, W_MON, false, SMALL_SP);

        // Summary
        r = newRow();
        cell(r, "कुटुंबाचे उत्पन्न व कुटुंबाच्या खर्चाची समरी", 1f, Gravity.CENTER, true, true, 20);

        r = newRow();
        cell(r, "सभासदाचे व कुटुंबाचे एकूण मासिक उत्पन्न", 4f, Gravity.CENTER, true, true, TEXT_SP);
        etIncome = numCell(r, 1.4f, false, SMALL_SP);

        r = newRow();
        cell(r, "उत्पन्नातील एकूण ५० टक्के रक्कम घरखर्चासाठी", 3f, Gravity.CENTER, true, true, SMALL_SP);
        etHalfHome = numCell(r, 1.5f, false, SMALL_SP);
        cell(r, "उत्पन्नातील एकूण ५० टक्के रक्कम हप्त्यासाठी", 3f, Gravity.CENTER, true, true, SMALL_SP);
        etHalfInst = numCell(r, 1.5f, false, SMALL_SP);

        r = newRow();
        cell(r, "सभासदाच्या कुटुंबाचा एकूण खर्च", 3f, Gravity.CENTER, true, true, SMALL_SP);
        etExpSummary = numCell(r, 1.5f, false, SMALL_SP);
        cell(r, "इतर MFI/ बँकांचा एकूण कर्जाचा हप्ता", 3f, Gravity.CENTER, true, true, SMALL_SP);
        etMfiSummary = numCell(r, 1.5f, false, SMALL_SP);

        r = newRow();
        cell(r, "शिल्लक रक्कम\n(हि रक्कम घरखर्चासाठी बाजूला ठेवणे)", 3f, Gravity.CENTER, true, true, SMALL_SP);
        etBalHome = numCell(r, 1.5f, false, SMALL_SP);
        cell(r, "हप्त्यासाठी रक्कम\n(यात बसणाऱ्या हप्त्याच्या रकमे इतके कर्ज सभासदाला देता येईल.)", 3f,
                Gravity.CENTER, true, true, SMALL_SP);
        etBalInst = numCell(r, 1.5f, false, SMALL_SP);

        // Message when the expense is more than 50% of the income
        r = newRow();
        tvExpenseWarn = cell(r, "", 1f, Gravity.CENTER, true, true, SMALL_SP);
        tvExpenseWarn.setTextColor(Color.RED);
        tvExpenseWarn.setVisibility(View.GONE);

        r = newRow();
        cell(r, "सभासदाला मिळू शकणारी कर्ज रक्कम खालील प्रमाणे आहे (प्रपोज कर्ज रक्कम)", 1f,
                Gravity.CENTER, true, true, TEXT_SP);

        r = newRow();
        cell(r, "सभासदाची फिटलेली कर्ज रक्कम -", 1f, Gravity.START, true, true, TEXT_SP);
        etFitted = numCell(r, 1f, false, TEXT_SP);

        // Increase criteria: criterion | internal score | increase amount (same column grid as the table above)
        float wCrit = W_LABEL + W_FREQ;
        r = newRow();
        cell(r, "पुढील मिळणाऱ्या वाढीव कर्ज रक्कमेचे निकष", wCrit, Gravity.CENTER, true, true, TEXT_SP);
        cell(r, "इंटरनल स्कोअर", W_AMT, Gravity.CENTER, true, true, HEAD_SP);
        cell(r, "वाढीव रक्कम", W_MON, Gravity.CENTER, true, true, HEAD_SP);

        for (int i = 0; i < INCREASE_LABELS.length; i++) {
            r = newRow();
            LinearLayout inner = new LinearLayout(requireContext());
            inner.setOrientation(LinearLayout.HORIZONTAL);
            inner.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, wCrit));
            r.addView(inner);
            cell(inner, MAR_DIGITS[i], 0.45f, Gravity.CENTER, true, true, TEXT_SP);
            TextView critLabel = cell(inner, INCREASE_LABELS[i], 3f, Gravity.START, false, true, SMALL_SP);
            if (i == 0) tvCriteria1 = critLabel;
            scoreEdits.add(numCell(r, W_AMT, false, SMALL_SP));
            increaseEdits.add(numCell(r, W_MON, false, SMALL_SP));
        }

        r = newRow();
        cell(r, "एकूण मिळणारी कर्ज वाढ रक्कम", wCrit, Gravity.CENTER, true, true, TEXT_SP);
        etScoreTotal = numCell(r, W_AMT, false, SMALL_SP);
        etIncreaseTotal = numCell(r, W_MON, false, SMALL_SP);

        // Score chart: label | score | loan increase as per score
        float wA = 3.2f, wB = 1.1f, wC = 2.1f;
        r = newRow();
        cell(r, "स्कोअर चार्ट", 1f, Gravity.CENTER, true, true, 20);

        r = newRow();
        cell(r, "", wA, Gravity.CENTER, true, true, SMALL_SP);
        cell(r, "स्कोअर", wB, Gravity.CENTER, true, true, HEAD_SP);
        cell(r, "अन्नपूर्णातुन मिळणारी स्कोअर प्रमाणे कर्जवाढ", wC, Gravity.CENTER, true, true, HEAD_SP);

        r = newRow();
        cell(r, "१ - अन्नपूर्णातुन मिळणारा इंटरनल स्कोअर", wA, Gravity.START, true, true, SMALL_SP);
        etChartScore1 = numCell(r, wB, false, SMALL_SP);
        etChartLoan1 = numCell(r, wC, false, SMALL_SP);

        r = newRow();
        cell(r, "२ - क्रेडिट ब्युरो स्कोअर", wA, Gravity.START, true, true, SMALL_SP);
        etChartScore2 = numCell(r, wB, false, SMALL_SP);
        etChartLoan2 = numCell(r, wC, false, SMALL_SP);

        r = newRow();
        cell(r, "एकूण स्कोअर", wA, Gravity.START, true, true, SMALL_SP);
        etChartScoreTotal = numCell(r, wB, false, SMALL_SP);
        cell(r, "", wC, Gravity.CENTER, true, true, SMALL_SP);

        r = newRow();
        cell(r, "एकूण मिळू शकणारी कर्ज रक्कम", wA, Gravity.START, true, true, SMALL_SP);
        cell(r, "", wB, Gravity.CENTER, true, true, SMALL_SP);
        etChartLoanTotal = numCell(r, wC, false, SMALL_SP);

        r = newRow();
        cell(r, "एकूण मिळू शकणारी पुढील कर्ज रक्कम\n( ही कर्ज रक्कम अशी असावी जेणेकरून कर्जाचा हप्ता हा खर्च वजा करून उत्पन्नातील एकूण 50%  रकमेमध्ये बसेल)",
                1f, Gravity.CENTER, true, true, SMALL_SP);
        etNextLoan = cell(r, "", 1f, Gravity.CENTER, true, true, SMALL_SP);


        // Bottom spacer only while the keyboard is visible; no blank space otherwise
        bottomSpacer = new View(requireContext());
        bottomSpacer.setLayoutParams(new LinearLayout.LayoutParams(1, 0));
        ll.addView(bottomSpacer);

        TextWatcher w = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int a, int b, int c) { }
            public void onTextChanged(CharSequence s, int a, int b, int c) { }
            public void afterTextChanged(Editable s) { recalc(); }
        };
        for (EditText e : amtEdits) e.addTextChangedListener(w);
        for (EditText e : increaseEdits) e.addTextChangedListener(w);
        for (EditText e : scoreEdits) e.addTextChangedListener(w);
        etMfiAmt.addTextChangedListener(w);
        etFitted.addTextChangedListener(w);
        etChartScore2.addTextChangedListener(w);
        etChartLoan1.addTextChangedListener(w);
        etChartLoan2.addTextChangedListener(w);
    }

    /** Opens the "other MFI loans" details screen (link on the B) row). */
    private void openMfiDetails() {
        if (!isAdded() || mainActivity == null) return;
        long mid = currentMemberId;
        if (mid <= 0 && mainActivity.Member != null && mainActivity.Member.size() > 0)
            mid = mainActivity.Member.get(0).MemberID;
        long org = mainActivity.OrgID;
        if (mid <= 0 || org <= 0) {
            Toast.makeText(requireContext(), "सभासद / Org ID सापडला नाही.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Shown on top of the whole screen (android.R.id.content) so it also works inside the PIF
        // ViewPager. This screen stays underneath untouched, so the entered values are kept; the CB
        // screen's back button (or the device back button) removes it and returns to this page.
        final FragmentManager fm = requireActivity().getSupportFragmentManager();
        if (fm.isStateSaved()) return;
        fm.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewCreated(FragmentManager f, androidx.fragment.app.Fragment fr,
                                              View v, Bundle savedInstanceState) {
                if (fr instanceof CreditCheckDetailFragment) {
                    v.setClickable(true);   // do not let touches reach the screen below
                    if (v.getBackground() == null) v.setBackgroundColor(Color.WHITE);
                    fm.unregisterFragmentLifecycleCallbacks(this);
                }
            }
        }, false);
        fm.beginTransaction()
                .setReorderingAllowed(true)
                .add(android.R.id.content, CreditCheckDetailFragment.newInstance(mid, org))
                .addToBackStack("mfi_details")
                .commit();
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
    private int insuranceAmt = 0;   // 11) insurance / LIC (monthly)
    private int savingsAmt = 0;     // 12) RD + Adharpurna (monthly)

    private void loadExpenseData() {
        dataLoaded = false;
        otherMfiSum = 0;
        insuranceAmt = 0;
        savingsAmt = 0;
        try {
            long mid = currentMemberId;
            if (mid <= 0 && mainActivity.Member != null && mainActivity.Member.size() > 0)
                mid = mainActivity.Member.get(0).MemberID;
        } catch (Exception ignored) { }
        if (embedded) {
            // PIF screen: use MainActivity's in-memory data as it is (may contain unsaved edits and
            // a new member that is not in the database yet). Do not reload from the database.
            dataLoaded = true;
            try {
                for (int i = 0; i < mainActivity.OtherMFIData.size(); i++)
                    otherMfiSum += mainActivity.OtherMFIData.get(i).MonthlyInstallment;
            } catch (Exception ignored) { }
            return;
        }
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

    /** "पुढील मिळणाऱ्या वाढीव कर्ज रक्कमेचे निकष": amounts from the promotion list (TD_PromotionLists / PunarbhetSchedule). */

    private String promoTotalScore = null;   // TotalScore from TD_PromotionLists, shown as received

    /*
     * Criteria table = promotion list only (TD_PromotionLists CSV -> RevisitMemberList item), shown as it comes:
     * "NA" -> NA, null -> null, number -> number; no value / field / member missing -> "--".
     * Field names are the CSV column names (the match ignores upper / lower case). Each row = {score, amount}.
     */
    private static final String[][] PROMO_FIELDS = {
            {"ScoreATypeGroup", "incentiveATypeGroup"},                       // 1 'ए' ग्रेड गट बनवल्याबद्दल
            {"ScoreRegularLoanRepay", "incentiveRegularLoanRepay"},           // 2 उत्कृष्ट कर्जफेड
            {"ScoreRequiredJLGAvailable", "incentiveRequiredJLGAvailable"},   // 3 बचत, शेअर्स न काढणे
            {"ScoreRegularRDpay", "incentiveRegularRDpay"},                   // 4 आर डी नियमित
            {"ScoreRegularADHPpay", "incentiveRegularADHPpay"},               // 5 आधारपूर्णा नियमित
            {"ScoreComMeetingAttendance", "incentiveComMeetingAttendance"},   // 6 कम्युनिटी मिटींग
            {"ScoreAGMAttendance", "incentiveAGMAttendance"}};                // 7 एजीएम

    /** Value of a field of the promotion-list member as text (field name matched ignoring case); null if there is no such field. */
    private String fieldText(Object o, String name) {
        try {
            for (java.lang.reflect.Field f : o.getClass().getDeclaredFields()) {
                if (f.getName().equalsIgnoreCase(name)) {
                    f.setAccessible(true);   // the model fields are package-private
                    Object v = f.get(o);
                    return v == null ? "null" : String.valueOf(v);
                }
            }
        } catch (Exception ignored) { }
        return null;   // field not present in clsRevisitMember
    }

    /** First field (of the given names) that has a value; "--" when none has. */
    private String firstOf(Object o, String... names) {
        for (String n : names) {
            String v = fieldText(o, n);
            if (v != null && !v.trim().isEmpty()) return v;
        }
        return "--";
    }

    /**
     * Grade of the group (TD_NewGroup_Info / NewGroupInfo.GradeOfGroup). Revisit (promotion list) is always 'A'.
     * In the PIF screens the grade of the member's group is read from the database; when it cannot be read it is 'A'.
     */
    private boolean isAGradeGroup() {
        if (!embedded) return true;
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            ufinDbHelper helper = new ufinDbHelper(mainActivity.getBaseContext());
            db = helper.getReadableDatabase();
            c = db.query(ufinDbContract.NewGroupInfo.TABLE_NAME,
                    new String[]{ufinDbContract.NewGroupInfo.COLUMN_NAME_GradeOfGroup},
                    ufinDbContract.NewGroupInfo.COLUMN_NAME_GroupID + "='" + mainActivity.selectedGroupID + "'",
                    null, null, null, null);
            if (c != null && c.moveToFirst()) {
                String g = c.getString(0);
                if (g != null && g.trim().toUpperCase().startsWith("B")) return false;
            }
        } catch (Exception ignored) {
        } finally {
            try { if (c != null) c.close(); } catch (Exception ignored) { }
            try { if (db != null) db.close(); } catch (Exception ignored) { }
        }
        return true;
    }

    /** Criteria table + score chart values from the promotion list data (RevisitMemberList = PunarbhetSchedule / CSV). */
    private void showPromotionData() {
        long mid = currentMemberId;
        if (mid <= 0 && mainActivity.Member != null && mainActivity.Member.size() > 0)
            mid = mainActivity.Member.get(0).MemberID;
        clsRevisitMember pm = null;
        try {
            for (int i = 0; i < mainActivity.RevisitMemberList.size(); i++) {
                if (mainActivity.RevisitMemberList.get(i).MemberID == mid) {
                    pm = mainActivity.RevisitMemberList.get(i);
                    break;
                }
            }
        } catch (Exception ignored) { }
        // no data -> "--"
        // row 1 depends on the grade of the group: A -> A-type group fields, B -> B-type group fields
        boolean aGrade = isAGradeGroup();
        if (tvCriteria1 != null)
            tvCriteria1.setText(aGrade ? INCREASE_LABELS[0] : "'बी' ग्रेड गट बनवल्याबद्दल");
        for (int i = 0; i < PROMO_FIELDS.length && i < scoreEdits.size(); i++) {
            String[] f = PROMO_FIELDS[i];
            if (i == 0 && !aGrade) f = new String[]{"ScoreBTypeGroup", "IncentiveBTypeGroup"};
            scoreEdits.get(i).setText(pm == null ? "--" : firstOf(pm, f[0]));
            increaseEdits.get(i).setText(pm == null ? "--" : firstOf(pm, f[1]));
        }
        // क्रेडिट ब्युरो स्कोअर / एकूण स्कोअर (the "कर्जवाढ" cells of the chart are not filled)
        etChartScore2.setText(pm == null ? "--" : firstOf(pm, "CreditBureau"));
        promoTotalScore = pm == null ? "--" : firstOf(pm, "TotalScore");
        // सभासदाची फिटलेली कर्ज रक्कम = NewLoanAmt of the promotion list
        etFitted.setText(pm == null ? "--" : firstOf(pm, "NewLoanAmt", "NewLoanAmount"));
    }

    /** Value exactly as received: null -> "null", "NA" -> "NA", 5.0 -> "5". */
    private String rawValue(org.json.JSONObject o, String key) {
        if (!o.has(key)) return "--";
        if (o.isNull(key)) return "null";
        Object v = o.opt(key);
        if (v instanceof Double && ((Double) v) == Math.rint((Double) v)) return String.valueOf(((Double) v).longValue());
        return String.valueOf(v);
    }

    private void prefill() {
        showPromotionData();
        // 11 and 12 are filled automatically (collection list) but can still be edited
        if (savingsAmt > 0) amtEdits.get(I_SAV).setText(String.valueOf(savingsAmt));
        if (!dataLoaded) {
            // No record: only the other-MFI installment (if any)
            if (otherMfiSum > 0) etMfiAmt.setText(String.valueOf(otherMfiSum));
            return;
        }
        try {
            if (mainActivity.FamilyImpactInfo != null) {
                set(I_TAX, mainActivity.FamilyImpactInfo.PropertyTax);
                set(I_RENT, mainActivity.FamilyImpactInfo.HouseRentExpense);   // house rent is always editable
                set(I_LIGHT, mainActivity.FamilyImpactInfo.LightBill);
                set(I_WATER, mainActivity.FamilyImpactInfo.PaniPatti);
                set(I_FOOD, mainActivity.FamilyImpactInfo.FoodExpense);
                set(I_EDU, mainActivity.FamilyImpactInfo.EducationExpense);
                set(I_OTHER, mainActivity.FamilyImpactInfo.OtherExpenses);
                set(I_CREDIT, mainActivity.FamilyImpactInfo.UdhariExpense);
                // the automatic values (collection list) win; the saved value is used only when there is none
                set(I_INS, mainActivity.FamilyImpactInfo.InsuranceExpense);   // 11) insurance: only from FamilyImpactInfo.InsuranceExpense
                if (savingsAmt <= 0) set(I_SAV, mainActivity.FamilyImpactInfo.AnnPurnaRDADHP);
            }
            if (mainActivity.MemberFamilyDetails != null) {
                set(I_MED, mainActivity.MemberFamilyDetails.MedicalExpenses);
                set(I_BHISHI, mainActivity.MemberFamilyDetails.MonthlyBhishi);
                // B) other loans installment: FamilyImpactInfo.OtherLoanInstallMent (new column);
                // while that field does not exist yet the old sources are used
                String olim = mainActivity.FamilyImpactInfo == null ? null
                        : fieldText(mainActivity.FamilyImpactInfo, "OtherLoanInstallMent");
                int inst;
                if (olim != null) {
                    inst = (int) Math.round(parseNum(olim));
                } else {
                    inst = mainActivity.MemberFamilyDetails.MonthlyInstallments;
                    if (inst <= 0) inst = otherMfiSum;
                }
                if (inst > 0) etMfiAmt.setText(String.valueOf(inst));
            }
        } catch (Exception ignored) { }
    }

    private static double parseNum(String t) {
        try { return Double.parseDouble(t.trim()); } catch (Exception e) { return 0; }
    }

    /** Sets an int field by name (new columns that may not exist in the class yet are skipped). */
    private static void setIntField(Object o, String name, int v) {
        try {
            java.lang.reflect.Field f = o.getClass().getDeclaredField(name);
            f.setAccessible(true);
            if (f.getType() == int.class) f.setInt(o, v);
            else if (f.getType() == long.class) f.setLong(o, v);
            else if (f.getType() == String.class) f.set(o, String.valueOf(v));
        } catch (Exception ignored) { }
    }

    private void set(int idx, int v) {
        if (v > 0) amtEdits.get(idx).setText(String.valueOf(v));
    }

    private long val(EditText e) {
        try {
            String t = e.getText().toString().trim();
            if (t.matches("\\d+(\\.\\d+)?")) return Math.round(Double.parseDouble(t));   // e.g. 2.5 from the server
            String s = t.replaceAll("[^0-9]", "");
            return s.isEmpty() ? 0 : Long.parseLong(s);
        } catch (Exception ex) { return 0; }
    }

    /** Monthly amount of expense item i (frequency applied). */
    private long mon(int i) {
        return monthlyOf(freqSpinners.get(i), amtEdits.get(i));
    }

    private void recalc() {
        if (etNextLoan == null) return;
        long exp = 0;
        for (int i = 0; i < amtEdits.size(); i++) {
            long m = mon(i);
            monEdits.get(i).setText(String.valueOf(m));
            exp += m;
        }
        long mfi = monthlyOf(spMfi, etMfiAmt);
        etMfi.setText(String.valueOf(mfi));
        long half = totalIncome / 2;
        long inc = 0, score = 0;
        for (EditText e : increaseEdits) inc += val(e);
        for (EditText e : scoreEdits) score += val(e);
        etTotalExpense.setText(String.valueOf(exp));
        etIncome.setText(String.valueOf(totalIncome));
        etHalfHome.setText(String.valueOf(half));
        etHalfInst.setText(String.valueOf(half));
        etExpSummary.setText(String.valueOf(exp));
        etMfiSummary.setText(String.valueOf(mfi));
        etBalHome.setText(String.valueOf(half - exp));
        // Amount available for installments: 50% of the income; when the expense is more than that,
        // income - expense is used instead (never the 50%). The other MFI installment is deducted in both cases.
        boolean expenseHigh = exp > half;
        long instBal = (expenseHigh ? totalIncome - exp : half) - mfi;
        etBalInst.setText(String.valueOf(Math.max(0, instBal)));   // never show a negative amount
        if (!expenseHigh) {
            tvExpenseWarn.setVisibility(View.GONE);
        } else {
            tvExpenseWarn.setText(totalIncome - exp <= 0
                    ? "कुटुंबाचा खर्च उत्पन्नापेक्षा जास्त/बरोबर आहे, त्यामुळे हप्त्यासाठी रक्कम शिल्लक नाही."
                    : "कुटुंबाचा खर्च उत्पन्नाच्या ५०% पेक्षा जास्त आहे, हप्त्यासाठी शिल्लक रक्कम = उत्पन्न - खर्च (५०% नाही).");
            tvExpenseWarn.setVisibility(View.VISIBLE);
        }
        etIncreaseTotal.setText(String.valueOf(inc));
        etScoreTotal.setText(String.valueOf(score));
        // "एकूण मिळू शकणारी पुढील कर्ज रक्कम": largest loan whose installment fits the installment balance
        int li = maxEligibleLoanIndex(instBal);
        if (li < 0) {
            etNextLoan.setText("हप्त्यासाठी रकमेत बसणारे कर्ज नाही.");
        } else {
            String t = "रु. " + allPrincipals.get(li) + "/- " + allPeriods.get(li) + "  महिने"
                    + "  हप्ता रु. " + allInstallments.get(li) + "/-";
            if (Boolean.TRUE.equals(productJointRequired.get(allProductIds.get(li)))) t += " *";
            etNextLoan.setText(t);
        }
        // Score chart: row 1 score = total of the internal scores above
        etChartScore1.setText(String.valueOf(score));
        etChartScoreTotal.setText(promoTotalScore != null ? promoTotalScore
                : String.valueOf(score + val(etChartScore2)));
        etChartLoanTotal.setText(String.valueOf(val(etChartLoan1) + val(etChartLoan2)));
        // Used by other screens (RepayFragment: Monthly_Surplus); same formula as the old Expense screen
        try {
            mainActivity.ExpenseTotal = (int) (mon(I_RENT) + mon(I_FOOD) + mon(I_EDU) + mon(I_MED)
                    + mon(I_OTHER) + mon(I_BHISHI) + mfi);
        } catch (Exception ignored) { }
        pushToModel();
        updateSaveEnabled();
    }

    // ------------------------------------------------------------ loan product dropdown
    private void loadLoanMaster() {
        allDetailsIds.clear();
        allProductIds.clear();
        allPrincipals.clear();
        allPeriods.clear();
        allInstallments.clear();
        productJointRequired.clear();
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            ufinDbHelper helper = new ufinDbHelper(mainActivity.getBaseContext());
            db = helper.getReadableDatabase();
            String reason = "";
            try {
                if (mainActivity.AdditionalDetails.strLoanReason != null)
                    reason = mainActivity.AdditionalDetails.strLoanReason;
            } catch (Exception ignored) { }
            boolean home = reason.equalsIgnoreCase("Home");

            // Member birth date + 65 years: a loan that ends after this date is not allowed
            Calendar ageLimit = null;
            try {
                String bd = mainActivity.Member.get(0).BirthDate;
                if (bd.length() > 10) bd = bd.substring(0, 10);
                String[] bs = bd.split("-");
                ageLimit = Calendar.getInstance();
                ageLimit.set(Calendar.YEAR, Integer.parseInt(bs[0]));
                ageLimit.set(Calendar.MONTH, Integer.parseInt(bs[1]) - 1);
                ageLimit.set(Calendar.DATE, Integer.parseInt(bs[2]));
                ageLimit.add(Calendar.YEAR, MAX_AGE_AT_LOAN_END);
            } catch (Exception ignored) {
                ageLimit = null;   // birth date not available: no age filter
            }

            // Products valid on the loan date (group test date + 50 days).
            // In the Revisit flow the group test date can be missing: today is used then.
            Calendar cal = Calendar.getInstance();
            try {
                String[] sp = mainActivity.newGroup.GroupTestDate.substring(0, 10).split("-");
                cal.set(Calendar.YEAR, Integer.parseInt(sp[0]));
                cal.set(Calendar.MONTH, Integer.parseInt(sp[1]) - 1);
                cal.set(Calendar.DATE, Integer.parseInt(sp[2]));
            } catch (Exception ignored) {
                cal = Calendar.getInstance();
            }
            long loanDate = cal.getTimeInMillis() + 1000L * 60L * 60L * 24L * 50L;

            c = db.query(ufinDbContract.LoanProductsMaster.TABLE_NAME,
                    new String[]{
                            ufinDbContract.LoanProductsMaster.COLUMN_NAME_LoanProductID,
                            ufinDbContract.LoanProductsMaster.COLUMN_NAME_ProductName,
                            ufinDbContract.LoanProductsMaster.COLUMN_NAME_ProductStartDate,
                            ufinDbContract.LoanProductsMaster.COLUMN_NAME_ProductEndDate,
                            ufinDbContract.LoanProductsMaster.COLUMN_NAME_JointAccountHolderRequired},
                    ufinDbContract.LoanProductsMaster.COLUMN_NAME_IsActive + "='1'",
                    null, null, null,
                    ufinDbContract.LoanProductsMaster.COLUMN_NAME_LoanProductID + " ASC");
            while (c != null && c.moveToNext()) {
                try {
                    String name = c.getString(c.getColumnIndexOrThrow(ufinDbContract.LoanProductsMaster.COLUMN_NAME_ProductName));
                    if (name == null || name.trim().isEmpty()) continue;
                    name = name.trim();
                    if (home) {
                        if (!name.startsWith("Home")) continue;
                    } else if (name.contains("Home")) continue;
                    String start = c.getString(c.getColumnIndexOrThrow(ufinDbContract.LoanProductsMaster.COLUMN_NAME_ProductStartDate));
                    if (start == null || start.isEmpty()) continue;
                    String[] s2 = start.substring(0, 10).split("-");
                    cal.set(Calendar.YEAR, Integer.parseInt(s2[0]));
                    cal.set(Calendar.MONTH, Integer.parseInt(s2[1]) - 1);
                    cal.set(Calendar.DATE, Integer.parseInt(s2[2]));
                    if (loanDate < cal.getTimeInMillis()) continue;
                    String end = c.getString(c.getColumnIndexOrThrow(ufinDbContract.LoanProductsMaster.COLUMN_NAME_ProductEndDate));
                    if (end != null && !end.isEmpty()) {
                        String[] s3 = end.substring(0, 10).split("-");
                        cal.set(Calendar.YEAR, Integer.parseInt(s3[0]));
                        cal.set(Calendar.MONTH, Integer.parseInt(s3[1]) - 1);
                        cal.set(Calendar.DATE, Integer.parseInt(s3[2]));
                        if (loanDate > cal.getTimeInMillis()) continue;
                    }
                    int pid = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(ufinDbContract.LoanProductsMaster.COLUMN_NAME_LoanProductID)));
                    boolean joint = false;
                    try {
                        joint = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(
                                ufinDbContract.LoanProductsMaster.COLUMN_NAME_JointAccountHolderRequired))) == 1;
                    } catch (Exception ignored) { }
                    productJointRequired.put(pid, joint);
                } catch (Exception ignored) { }
            }
            if (c != null) c.close();

            c = db.query(ufinDbContract.LoanProductDetailsMaster.TABLE_NAME,
                    new String[]{
                            ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_LoanProductDetailsID,
                            ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_LoanProductID,
                            ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_PrincipalAmt,
                            ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_LoanPeriodInMonths,
                            ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_TotalMonthlyInstallmentAmt},
                    ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_IsActive + "='1'",
                    null, null, null,
                    ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_PrincipalAmt + " DESC");
            while (c != null && c.moveToNext()) {
                try {
                    int pid = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_LoanProductID)));
                    if (!productJointRequired.containsKey(pid)) continue;   // product not valid for this loan
                    int did = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_LoanProductDetailsID)));
                    String pr = c.getString(c.getColumnIndexOrThrow(ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_PrincipalAmt));
                    String pe = c.getString(c.getColumnIndexOrThrow(ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_LoanPeriodInMonths));
                    String in = c.getString(c.getColumnIndexOrThrow(ufinDbContract.LoanProductDetailsMaster.COLUMN_NAME_TotalMonthlyInstallmentAmt));
                    Integer.parseInt(pr);
                    int months = Integer.parseInt(pe);
                    Integer.parseInt(in);
                    // Age limit: the loan must end before the member turns 65 (same as the Repay screen)
                    if (ageLimit != null) {
                        Calendar loanEnd = Calendar.getInstance();
                        loanEnd.add(Calendar.MONTH, months);
                        if (loanEnd.getTimeInMillis() > ageLimit.getTimeInMillis()) {
                            continue;
                        }
                    }
                    allDetailsIds.add(did);
                    allProductIds.add(pid);
                    allPrincipals.add(pr);
                    allPeriods.add(pe);
                    allInstallments.add(in);
                } catch (Exception ignored) { }
            }
        } catch (Exception ignored) {
        } finally {
            try { if (c != null) c.close(); } catch (Exception ignored) { }
            try { if (db != null) db.close(); } catch (Exception ignored) { }
        }
    }

    /**
     * Largest loan whose installment fits the installment balance
     * (50% of the income, or income - expense when the expense is more than 50%, minus the other MFI installments).
     * Returns the index in the all* lists, or -1 when no loan fits.
     */
    private int maxEligibleLoanIndex(long instBalance) {
        int best = -1;
        long bestPrincipal = 0, bestInst = 0;
        for (int i = 0; i < allDetailsIds.size(); i++) {
            try {
                long inst = Long.parseLong(allInstallments.get(i));
                long principal = Long.parseLong(allPrincipals.get(i));
                if (inst > instBalance) continue;
                // largest loan; for the same loan amount the smaller installment (longer period)
                if (principal > bestPrincipal || (principal == bestPrincipal && best >= 0 && inst < bestInst)) {
                    best = i;
                    bestPrincipal = principal;
                    bestInst = inst;
                }
            } catch (Exception ignored) { }
        }
        return best;
    }

    private String signature() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < amtEdits.size(); i++)
            sb.append(val(amtEdits.get(i))).append(':').append(freqSpinners.get(i).getSelectedItemPosition()).append('|');
        sb.append(val(etMfiAmt)).append(':').append(spMfi.getSelectedItemPosition())
                .append('|').append(val(etFitted));
        for (EditText e : increaseEdits) sb.append('|').append(val(e));
        for (EditText e : scoreEdits) sb.append('|').append(val(e));
        sb.append('|').append(val(etChartScore2)).append('|').append(val(etChartLoan1))
                .append('|').append(val(etChartLoan2));
        return sb.toString();
    }

    private void updateSaveEnabled() {
        if (btSave == null || baseline == null || saving) return;
        btSave.setEnabled(!signature().equals(baseline));
    }

    // ------------------------------------------------------------ buttons / save
    private void setupButtons() {
        if (embedded) {
            hideOwnButtons();
            return;   // MainActivity's bar does Close / Save / navigation
        }
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
        // values are read on the UI thread, the background thread only writes them
        final long[] mv = monthlyValues();
        final int inst = (int) monthlyOf(spMfi, etMfiAmt);
        saveExecutor.execute(() -> {
            boolean ok = true;
            try {
                applyToModel(mv, inst);
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