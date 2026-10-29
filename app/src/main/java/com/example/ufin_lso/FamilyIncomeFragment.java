package com.example.ufin_lso;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;
import java.util.List;

public class FamilyIncomeFragment extends Fragment {

    public static final String ARG_MEMBER_ID = "MemberID";
    public static final String ARG_TOTAL_INCOME = "FamilyTotalIncome";

    private View rootView;
    private MainActivity mainActivity;

    private LinearLayout llMembers;
    private Button btSave;
    private Button btClose;
    private Button btPrevious;
    private Button btNext;
    private long currentMemberId = 0;
    private ScrollView scrollView;
    private volatile boolean saving = false;
    private final ExecutorService saveExecutor = Executors.newSingleThreadExecutor();
    private int oldSoftInputMode = -1;
    private EditText etTotalMonthly;      // A) auto-calculated
    private EditText etAnnualFarm;        // B) yearly (farm) income
    private EditText etEffectiveMonthly;  // B) divided by 12, auto-calculated
    private EditText etRent;              // C) amount typed by the user
    private EditText etOtherIncome;       // D) amount typed by the user
    private EditText etRentMonthly;       // C) monthly amount (calculated)
    private EditText etOtherMonthly;      // D) monthly amount (calculated)
    private Spinner spFreqRent, spFreqOther;   // frequency dropdowns of C and D (B is always Annual)
    private EditText etFamilyTotal;       // grand total, auto-calculated
    private EditText etPerCapita;         // family total / number of members, auto-calculated

    /** One member row as loaded from the DB (used to build the table). */
    private static class MemberRow {
        String name;
        String relation;
        String source;
        long relationId;
        long businessId;
        int monthlyIncome;
        clsMemberData member;            // member from DB
        clsPersonalImpactInfo info;      // income record from DB (can be null)
    }

    // Dropdown lists. Index 0 = blank ("select"), same as CreditCheckFragment.
    private final ArrayList<Integer> relationIds = new ArrayList<>();
    private final ArrayList<String> relationNames = new ArrayList<>();
    private final ArrayList<Integer> businessIds = new ArrayList<>();
    private final ArrayList<String> businessNames = new ArrayList<>();

    private final ArrayList<MemberRow> rows = new ArrayList<>();
    private int totalMonthly = 0;   // sum of MonthlyIncome of all active members

    public FamilyIncomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_family_income, container, false);
        mainActivity = (MainActivity) getActivity();

        if (mainActivity == null) {
            return rootView;
        }

        llMembers = rootView.findViewById(R.id.llMembers);
        etTotalMonthly = rootView.findViewById(R.id.editTextTotalMonthly);
        etAnnualFarm = rootView.findViewById(R.id.editTextAnnualFarm);
        etEffectiveMonthly = rootView.findViewById(R.id.editTextEffectiveMonthly);
        etRent = rootView.findViewById(R.id.editTextRent);
        etOtherIncome = rootView.findViewById(R.id.editTextOtherIncome);
        etFamilyTotal = rootView.findViewById(R.id.editTextFamilyTotal);
        etPerCapita = rootView.findViewById(R.id.editTextPerCapitaIncome);
        etRentMonthly = rootView.findViewById(R.id.editTextRentMonthly);
        etOtherMonthly = rootView.findViewById(R.id.editTextOtherMonthly);
        spFreqRent = rootView.findViewById(R.id.spinnerFreqRent);
        spFreqOther = rootView.findViewById(R.id.spinnerFreqOther);
        setupFreqSpinner(spFreqRent, FREQ_MONTHLY);
        setupFreqSpinner(spFreqOther, FREQ_MONTHLY);

        // 1) Load the data
        long memberId = 0;
        if (getArguments() != null) {
            memberId = getArguments().getLong(ARG_MEMBER_ID, 0);
        }
        if (memberId == 0) {
            memberId = mainActivity.selectedMemberID;   // fallback
        }
        currentMemberId = memberId;
        loadFamilyData(memberId);

        // 2) Show the member rows
        showMemberCards();

        // 3) Initial B and D values (from the main member), C stays empty
        fillEditableDefaults();

        // 4) Recalculate totals whenever a number changes
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) { }
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) { }
            @Override public void afterTextChanged(Editable s) { calculate(); }
        };
        etAnnualFarm.addTextChangedListener(watcher);
        etRent.addTextChangedListener(watcher);
        etOtherIncome.addTextChangedListener(watcher);

        calculate();
        setupButtons();
        baseline = signature();
        updateSaveEnabled();
        for (EditText e : new EditText[]{etAnnualFarm, etRent, etOtherIncome}) {
            e.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) scrollFieldAboveKeyboard(v);
                }
            });
        }

        TextView tv = rootView.findViewById(R.id.textViewRequestFocus);
        if (tv != null) {
            tv.requestFocus();
        }
        return rootView;
    }

    // Loading data (same approach as ArchivedListFragment / GetMemberInfo)
    private void loadFamilyData(long memberId) {

        rows.clear();
        totalMonthly = 0;

        // Dropdown lists for relation and income source
        loadMaster(ufinDbContract.RelationMaster.TABLE_NAME,
                ufinDbContract.RelationMaster.COLUMN_NAME_RelationID,
                ufinDbContract.RelationMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.RelationMaster.COLUMN_NAME_IsActive,
                ufinDbContract.RelationMaster.COLUMN_NAME_SequenceID,
                relationIds, relationNames);
        loadMaster(ufinDbContract.BusinessMaster.TABLE_NAME,
                ufinDbContract.BusinessMaster.COLUMN_NAME_BusinessID,
                ufinDbContract.BusinessMaster.COLUMN_NAME_MarathiDescription,
                ufinDbContract.BusinessMaster.COLUMN_NAME_IsActive,
                ufinDbContract.BusinessMaster.COLUMN_NAME_SequenceID,
                businessIds, businessNames);

        if (memberId == 0) {
            return;
        }

        mainActivity.SQLite_getMembers(memberId, "MemberID");
        if (mainActivity.Member.size() == 0) {
            return;
        }

        // remaining family members
        mainActivity.SQLite_getMembers(mainActivity.Member.get(0).MemberID, "MainMemberID");

        // PersonalImpactInfo of every member
        // (the list is cleared for the first member, the others are appended)
        for (int i = 0; i < mainActivity.Member.size(); i++) {
            mainActivity.SQLite_getPersonalImpactInfo(mainActivity.Member.get(i).MemberID);
        }

        // Build the member rows (same as IncomeFragment: only IsActive == 1)
        for (int i = 0; i < mainActivity.Member.size(); i++) {

            clsMemberData member = mainActivity.Member.get(i);
            if (member.IsActive != 1) {
                continue;
            }

            clsPersonalImpactInfo info = findImpactInfo(member.MemberID);

            MemberRow row = new MemberRow();
            row.name = buildName(member);
            row.member = member;
            row.info = info;
            row.relation = lookupMarathi(
                    ufinDbContract.RelationMaster.TABLE_NAME,
                    ufinDbContract.RelationMaster.COLUMN_NAME_RelationID,
                    ufinDbContract.RelationMaster.COLUMN_NAME_MarathiDescription,
                    member.RelationID);

            row.relationId = member.RelationID;
            ensureInList(relationIds, relationNames, member.RelationID, row.relation);

            if (info != null) {
                row.businessId = info.BusinessID;
                row.source = lookupMarathi(
                        ufinDbContract.BusinessMaster.TABLE_NAME,
                        ufinDbContract.BusinessMaster.COLUMN_NAME_BusinessID,
                        ufinDbContract.BusinessMaster.COLUMN_NAME_MarathiDescription,
                        info.BusinessID);
                row.monthlyIncome = info.MonthlyIncome;
                ensureInList(businessIds, businessNames, info.BusinessID, row.source);
            } else {
                row.source = "";
                row.businessId = 0;
                row.monthlyIncome = 0;
            }

            totalMonthly += row.monthlyIncome;
            rows.add(row);
        }
    }

    private clsPersonalImpactInfo findImpactInfo(long memberId) {
        for (int j = 0; j < mainActivity.PersonalImpactInfo.size(); j++) {
            if (mainActivity.PersonalImpactInfo.get(j).MemberID == memberId) {
                return mainActivity.PersonalImpactInfo.get(j);
            }
        }
        return null;
    }

    private String buildName(clsMemberData m) {
        StringBuilder sb = new StringBuilder();
        String[] parts = {m.Fname, m.Mname, m.Lname};
        for (String p : parts) {
            if (p == null) continue;
            p = p.trim();
            if (p.isEmpty() || p.equalsIgnoreCase("NA")) continue;
            if (sb.length() > 0) sb.append(' ');
            sb.append(p);
        }
        return sb.toString();
    }

    /** Reads IsActive=1 rows from M_RELATION / M_PROFESSION ordered by SequenceID. Index 0 = blank. */
    private void loadMaster(String table, String idCol, String textCol, String activeCol,
                            String seqCol, ArrayList<Integer> ids, ArrayList<String> names) {

        ids.clear();
        names.clear();
        ids.add(0);
        names.add("");

        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            ufinDbHelper helper = new ufinDbHelper(mainActivity.getBaseContext());
            db = helper.getReadableDatabase();
            c = db.query(table,
                    new String[]{idCol, textCol},
                    activeCol + "=?",
                    new String[]{"1"},
                    null, null,
                    seqCol + " ASC");

            if (c != null && c.moveToFirst()) {
                do {
                    ids.add(Integer.valueOf(c.getString(0)));
                    String s = c.getString(1);
                    names.add(s == null ? "?" : s);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) c.close();
            if (db != null) db.close();
        }
    }

    private void ensureInList(ArrayList<Integer> ids, ArrayList<String> names, long id, String text) {
        if (id == 0 || ids.contains((int) id)) {
            return;
        }
        ids.add((int) id);
        names.add((text == null || text.isEmpty()) ? "?" : text);
    }

    /** Reads the Marathi description from M_RELATION / M_PROFESSION. */
    private String lookupMarathi(String table, String idColumn, String textColumn, long id) {

        if (id == 0) {
            return "";
        }

        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            ufinDbHelper helper = new ufinDbHelper(mainActivity.getBaseContext());
            db = helper.getReadableDatabase();
            c = db.query(table,
                    new String[]{textColumn},
                    idColumn + "=?",
                    new String[]{String.valueOf(id)},
                    null, null, null);

            if (c != null && c.moveToFirst()) {
                String s = c.getString(0);
                return (s == null) ? "" : s;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) c.close();
            if (db != null) db.close();
        }
        return "";
    }

    // Filling the screen
    // Font size of the table cells (7 columns have to fit on the screen)
    private static final int TEXT_SP = 16;
    private static final int NUM_SP = 15;   // number cells (narrow columns)

    // Table column weights: serial | name | relation | source | frequency | rupees | monthly income
    // (the same numbers are used in fragment_family_income.xml for rows B, C, D so the columns line up)
    private static final float[] COL_WEIGHT = {22f, 66f, 72f, 82f, 68f, 54f, 54f};

    // Frequency dropdown (Daily / Weekly / Monthly / Annual)
    private static final String[] FREQ = {"Daily", "Weekly", "Monthly", "Annual"};
    private static final int FREQ_MONTHLY = 2, FREQ_ANNUAL = 3;
    // Weeks counted in one month for the "Weekly" option (change here if the business rule differs)
    private static final double WEEKS_PER_MONTH = 4;

    /** Multiplier that converts the entered amount into a monthly amount. */
    private static double factor(int freqPos) {
        switch (freqPos) {
            case 0: return 30;               // Daily
            case 1: return WEEKS_PER_MONTH;  // Weekly
            case 3: return 1 / 12.0;         // Annual
            default: return 1;               // Monthly
        }
    }

    private int monthlyOf(Spinner sp, EditText amt) {
        return (int) Math.round(toInt(amt) * factor(sp.getSelectedItemPosition()));
    }

    /** One editable table row. */
    private static class RowHolder {
        LinearLayout line;
        TextView tvSerial;
        EditText etName;
        Spinner spRelation;
        Spinner spSource;
        Spinner spFreq;          // Daily / Weekly / Monthly / Annual
        EditText etIncome;       // rupees typed by the user (per frequency)
        TextView tvMonthly;      // monthly income (calculated)
        clsMemberData member;            // null = new (not yet saved) row
        clsPersonalImpactInfo info;
    }

    private final ArrayList<RowHolder> holders = new ArrayList<>();
    // Rows of members that exist in the DB and were deleted from the table; they are deactivated on Save
    private final ArrayList<RowHolder> removedHolders = new ArrayList<>();
    private boolean rebalancePending = false;

    private final TextWatcher rowWatcher = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) { }
        @Override public void onTextChanged(CharSequence s, int st, int b, int c) { }
        @Override public void afterTextChanged(Editable s) {
            calculate();          // A) and the total update immediately
            scheduleRebalance();  // add / delete rows
        }
    };

    private void showMemberCards() {

        llMembers.removeAllViews();
        holders.clear();

        // Header row (index 0)
        llMembers.addView(makeHeaderRow(
                new String[]{"अनु क्र.", "कुटुंबातील सदस्यांची नावे", "नाते",
                        "सभासदाच्या उत्पन्नाचे साधन - नोकरी/व्यवसाय",
                        "उत्पन्न\n(Daily, Weekly, Monthly, Annual)", "रूपये", "मासिक उत्पन्न"}));

        // Members from the DB
        for (int i = 0; i < rows.size(); i++) {
            MemberRow r = rows.get(i);
            RowHolder h = addRow(r.name, r.relationId, r.businessId, String.valueOf(r.monthlyIncome));
            h.member = r.member;
            h.info = r.info;
        }

        // Always keep one blank row at the end and fix the serial numbers
        rebalance();
    }

    // ------------------------------------------------------------------
    // Rules for adding / deleting rows
    // ------------------------------------------------------------------

    /** All four fields of the row are empty. */
    private boolean isRowEmpty(RowHolder h) {
        return text(h.etName).isEmpty()
                && h.spRelation.getSelectedItemPosition() == 0
                && h.spSource.getSelectedItemPosition() == 0
                && text(h.etIncome).isEmpty();
    }

    /** All four fields of the row are filled. (Relax the rule here if needed.) */
    private boolean isRowComplete(RowHolder h) {
        return !text(h.etName).isEmpty()
                && h.spRelation.getSelectedItemPosition() > 0
                && h.spSource.getSelectedItemPosition() > 0
                && !text(h.etIncome).isEmpty();
    }

    /** A new (unsaved) row: its dropdowns appear and are enabled only after a name has been typed. */
    private void applyRowState(RowHolder h) {
        boolean on = (h.member != null) || !text(h.etName).isEmpty();
        int vis = on ? View.VISIBLE : View.INVISIBLE;
        h.spRelation.setVisibility(vis);
        h.spSource.setVisibility(vis);
        h.spFreq.setVisibility(vis);
        h.spRelation.setEnabled(on);
        h.spSource.setEnabled(on);
        h.spFreq.setEnabled(on);
    }

    private String text(EditText e) {
        return e.getText().toString().trim();
    }

    private void scheduleRebalance() {
        if (rebalancePending) return;
        rebalancePending = true;
        llMembers.post(new Runnable() {
            @Override
            public void run() {
                rebalancePending = false;
                if (isAdded()) {
                    rebalance();
                }
            }
        });
    }

    private void rebalance() {

        // 1) Remove completely empty rows except the last one
        //    (the main member's row is never removed: the main member cannot be deactivated)
        RowHolder mainHolder = firstSavedHolder();
        for (int i = holders.size() - 2; i >= 0; i--) {
            RowHolder h = holders.get(i);
            if (isRowEmpty(h) && h != mainHolder) {
                llMembers.removeView(h.line);
                holders.remove(i);
                // A member that is already in the DB must also be deactivated in the DB on Save
                if (h.member != null && !removedHolders.contains(h)) removedHolders.add(h);
            }
        }

        // 2) Last row: if it is filled add a new blank row below; if there are no rows add the first one
        if (holders.isEmpty()) {
            addRow("", 0, 0, "");
        } else {
            RowHolder last = holders.get(holders.size() - 1);
            if (isRowComplete(last)) {
                addRow("", 0, 0, "");
            }
        }

        for (RowHolder h : holders) applyRowState(h);

        // 3) Renumber the serial numbers
        // A blank row has no serial number; it gets one as soon as something is filled in
        int serial = 1;
        for (int i = 0; i < holders.size(); i++) {
            RowHolder h = holders.get(i);
            if (isRowEmpty(h)) {
                h.tvSerial.setText("");
            } else {
                h.tvSerial.setText(toMarathiDigits(serial++));
            }
        }

        calculate();
    }

    // Building the table rows
    private LinearLayout makeHeaderRow(String[] titles) {

        LinearLayout line = newRowLayout();

        for (int c = 0; c < titles.length; c++) {
            TextView tv = new TextView(requireContext());
            styleCell(tv, c);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);   // table headings
            tv.setText(titles[c]);
            line.addView(tv);
        }
        return line;
    }

    private RowHolder addRow(String name, long relationId, long businessId, String income) {

        RowHolder h = new RowHolder();
        h.line = newRowLayout();

        h.tvSerial = new TextView(requireContext());
        styleCell(h.tvSerial, 0);
        h.tvSerial.setGravity(Gravity.CENTER);
        h.line.addView(h.tvSerial);

        h.etName = makeEditCell(1, name, false);
        h.spRelation = makeSpinnerCell(2, relationNames, relationIds, relationId);
        h.spSource = makeSpinnerCell(3, businessNames, businessIds, businessId);
        h.spFreq = makeFreqCell(4);
        h.etIncome = makeEditCell(5, income, true);
        h.tvMonthly = new TextView(requireContext());
        styleCell(h.tvMonthly, 6);
        h.tvMonthly.setGravity(Gravity.CENTER);
        h.tvMonthly.setTypeface(null, Typeface.BOLD);
        h.tvMonthly.setTextSize(TypedValue.COMPLEX_UNIT_SP, NUM_SP);
        h.tvMonthly.setText(income);

        h.line.addView(h.etName);
        h.line.addView(boxed(h.spRelation, 2));
        h.line.addView(boxed(h.spSource, 3));
        h.line.addView(boxed(h.spFreq, 4));
        h.line.addView(h.etIncome);
        h.line.addView(h.tvMonthly);

        llMembers.addView(h.line);   // after the header, at the end
        holders.add(h);
        applyRowState(h);
        return h;
    }

    /** Bordered table cell that holds a Spinner; the Spinner keeps its default style, so the dropdown arrow is visible. */
    private View boxed(Spinner sp, int col) {
        FrameLayout box = new FrameLayout(requireContext());
        box.setBackgroundResource(R.drawable.customborder);
        box.setMinimumHeight(dp(44));
        box.setLayoutParams(new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, COL_WEIGHT[col]));
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL);
        lp.setMargins(dp(6), dp(4), 0, dp(4));   // keep the text away from the left border
        box.addView(sp, lp);
        return box;
    }

    /** Frequency dropdown for a table row (default Monthly). */
    private Spinner makeFreqCell(int col) {
        Spinner sp = new Spinner(requireContext());
        sp.setAdapter(new FreqAdapter());
        sp.setSelection(FREQ_MONTHLY, false);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        return sp;
    }

    /** Frequency dropdown of the B, C, D rows (defined in the XML). */
    private void setupFreqSpinner(Spinner sp, int selection) {
        sp.setAdapter(new FreqAdapter());
        sp.setSelection(selection, false);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    /** Adapter of the frequency dropdown: black, bold, centred text. */
    private class FreqAdapter extends ArrayAdapter<String> {

        FreqAdapter() {
            super(requireContext(), android.R.layout.simple_spinner_item, FREQ);
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }

        private View style(View v, int sp) {
            TextView tv = (TextView) v;
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
            tv.setTextColor(Color.BLACK);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setGravity(Gravity.CENTER);
            tv.setSingleLine(false);
            tv.setPadding(0, 0, 0, 0);
            return tv;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return style(super.getView(position, convertView, parent), 15);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View v = style(super.getDropDownView(position, convertView, parent), 20);
            v.setPadding(dp(12), dp(12), dp(12), dp(12));
            return v;
        }
    }

    /** Dropdown cell for relation / income source. */
    private Spinner makeSpinnerCell(int col, List<String> names, List<Integer> ids, long selectedId) {

        Spinner sp = new Spinner(requireContext());
        sp.setAdapter(new CellAdapter(names));

        int pos = ids.indexOf((int) selectedId);
        sp.setSelection(pos < 0 ? 0 : pos, false);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculate();
                scheduleRebalance();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        return sp;
    }

    /** Dropdown adapter: black text in the closed cell; larger, easy-to-tap items when opened. */
    private class CellAdapter extends ArrayAdapter<String> {

        CellAdapter(List<String> items) {
            super(requireContext(), android.R.layout.simple_spinner_item, items);
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = (TextView) super.getView(position, convertView, parent);
            tv.setText(getItem(position));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SP);
            tv.setTextColor(Color.BLACK);
            tv.setSingleLine(false);
            tv.setMaxLines(8);      // long names are shown fully; the row gets taller
            tv.setEllipsize(null);
            tv.setPadding(0, 0, 0, 0);
            return tv;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
            tv.setText(position == 0 ? "— निवडा —" : getItem(position));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            tv.setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
            tv.setSingleLine(false);
            tv.setEllipsize(null);
            tv.setPadding(dp(12), dp(12), dp(12), dp(12));
            return tv;
        }
    }

    private EditText makeEditCell(int col, String value, boolean number) {

        EditText et = new EditText(requireContext());
        styleCell(et, col);
        et.setText(value);   // text is set before the watcher is attached, so nothing is triggered

        if (number) {
            et.setInputType(InputType.TYPE_CLASS_NUMBER);
            et.setMaxLines(1);
            et.setFilters(new android.text.InputFilter[]{new android.text.InputFilter.LengthFilter(6)});
            et.setGravity(Gravity.CENTER);
            et.setTextSize(TypedValue.COMPLEX_UNIT_SP, NUM_SP);
        } else {
            et.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            et.setHorizontallyScrolling(false);
            et.setMaxLines(3);
            et.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            et.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        }

        et.addTextChangedListener(rowWatcher);
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) scrollFieldAboveKeyboard(v);
            }
        });
        return et;
    }

    /** Scrolls so that the field stays visible above the keyboard (~400dp). */
    private void scrollFieldAboveKeyboard(final View v) {
        if (scrollView == null) return;
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isAdded()) return;
                int[] loc = new int[2];
                v.getLocationOnScreen(loc);
                int[] sloc = new int[2];
                scrollView.getLocationOnScreen(sloc);
                int fieldTop = loc[1] - sloc[1] + scrollView.getScrollY();
                scrollView.smoothScrollTo(0, Math.max(0, fieldTop - dp(120)));
            }
        }, 250);
    }

    private LinearLayout newRowLayout() {
        LinearLayout line = new LinearLayout(requireContext());
        // Cells have different baselines (TextView / EditText / Spinner); with baseline alignment
        // they shift vertically after a re-layout and gaps appear between rows.
        line.setBaselineAligned(false);
        line.setOrientation(LinearLayout.HORIZONTAL);
        line.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return line;
    }

    /** Same look for every cell: border, black text, common font size. */
    private void styleCell(TextView tv, int col) {
        tv.setLayoutParams(new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, COL_WEIGHT[col]));
        tv.setBackgroundResource(R.drawable.customborder);
        tv.setPadding(dp(4), dp(8), dp(4), dp(8));
        tv.setMinHeight(dp(44));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SP);
        tv.setTextColor(Color.BLACK);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) applyNoResizeMode();
    }

    @Override
    public void onPause() {
        super.onPause();
        restoreSoftInputMode();
    }

    /** Called when the next screen opens (hidden) and when we come back (shown). */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            restoreSoftInputMode();
        } else {
            applyNoResizeMode();
        }
    }

    /** Keeps the footer buttons fixed when the keyboard opens; the keyboard simply covers them. */
    private void applyNoResizeMode() {
        if (getActivity() == null) return;
        if (oldSoftInputMode == -1) {
            oldSoftInputMode = getActivity().getWindow().getAttributes().softInputMode;
        }
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    private void restoreSoftInputMode() {
        if (getActivity() != null && oldSoftInputMode != -1) {
            getActivity().getWindow().setSoftInputMode(oldSoftInputMode);
            oldSoftInputMode = -1;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveExecutor.shutdown();
    }

    // ------------------------------------------------------------------
    // Close / Save
    // ------------------------------------------------------------------
    private void setupButtons() {
        btClose = rootView.findViewById(R.id.buttonCloseFamilyIncome);
        btSave = rootView.findViewById(R.id.buttonSaveFamilyIncome);
        btPrevious = rootView.findViewById(R.id.buttonPrevious);
        btNext = rootView.findViewById(R.id.buttonNext);
        scrollView = rootView.findViewById(R.id.scrollFamilyIncome);

        // First screen: nothing before it, Next is available
        btPrevious.setEnabled(false);
        btNext.setEnabled(true);

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saving) return;      // no Close while saving
                closeScreen();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSave();
            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saving) return;
                openNextScreen();
            }
        });
    }

    /** > : opens the next screen (monthly expense). This screen is only hidden, so the entered data is still there when we come back. */
    private void openNextScreen() {
        if (!isAdded()) return;

        // close the keyboard if it is open
        View focus = rootView.findFocus();
        if (focus != null) {
            android.view.inputmethod.InputMethodManager imm =
                    (android.view.inputmethod.InputMethodManager)
                            requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
            focus.clearFocus();
        }

        FamilyExpenseFragment next = new FamilyExpenseFragment();
        Bundle args = new Bundle();
        args.putLong(FamilyIncomeFragment.ARG_MEMBER_ID, currentMemberId);
        long totalIncome = 0;
        try {
            String t = etFamilyTotal.getText().toString().replaceAll("[^0-9]", "");
            if (!t.isEmpty()) totalIncome = Long.parseLong(t);
        } catch (Exception ignored) { }
        args.putLong(ARG_TOTAL_INCOME, totalIncome);
        next.setArguments(args);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .hide(this)
                .add(R.id.fragment_container, next)
                .addToBackStack(null)
                .commit();
    }

    private void closeScreen() {
        if (isAdded()) {
            requireActivity().getSupportFragmentManager().popBackStack();
        }
    }

    /** Splits the name into "First Middle Last". A single word goes only into Fname. */
    private void applyName(clsMemberData m, String full) {
        String[] p = full.trim().split("\\s+");
        if (p.length == 1) {
            m.Fname = p[0];
            m.Mname = "";
            m.Lname = "";
        } else if (p.length == 2) {
            m.Fname = p[0];
            m.Mname = "";
            m.Lname = p[1];
        } else {
            m.Fname = p[0];
            m.Lname = p[p.length - 1];
            StringBuilder mid = new StringBuilder();
            for (int k = 1; k < p.length - 1; k++) {
                if (mid.length() > 0) mid.append(' ');
                mid.append(p[k]);
            }
            m.Mname = mid.toString();
        }
    }

    /** UI thread: collects the values first, then the DB work runs in the background. */
    private void startSave() {

        if (saving) return;          // ignore double clicks / repeated taps
        saving = true;
        btSave.setEnabled(false);
        btClose.setEnabled(false);
        btNext.setEnabled(false);
        btSave.setText("...");

        // close the keyboard and clear focus from any field
        View focus = rootView.findFocus();
        if (focus != null) {
            android.view.inputmethod.InputMethodManager imm =
                    (android.view.inputmethod.InputMethodManager)
                            requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
            focus.clearFocus();
        }

        // Read the UI values now (never touch Views from the background thread)
        final ArrayList<SaveItem> items = new ArrayList<>();
        RowHolder first = firstSavedHolder();
        for (RowHolder h : holders) {
            if (isRowEmpty(h)) continue;
            SaveItem it = new SaveItem();
            it.holder = h;
            it.member = h.member;
            it.info = h.info;
            it.isNewRow = (h.member == null);
            it.serial = h.tvSerial.getText().toString();
            it.name = text(h.etName).replaceAll("\\s+", " ");   // remove double spaces
            int relPos = h.spRelation.getSelectedItemPosition();
            int busPos = h.spSource.getSelectedItemPosition();
            it.relationId = (relPos > 0) ? relationIds.get(relPos) : 0;
            it.businessId = (busPos > 0) ? businessIds.get(busPos) : 0;
            it.income = monthlyOf(h.spFreq, h.etIncome);   // monthly amount
            it.complete = isRowComplete(h);
            it.isMainMember = (h == first);
            items.add(it);
        }
        // B) is stored as a yearly amount, C + D as monthly amounts
        final ArrayList<RowHolder> toDeactivate = new ArrayList<>(removedHolders);
        final int yearly = toInt(etAnnualFarm);   // B) is always a yearly amount
        final int other = monthlyOf(spFreqRent, etRent) + monthlyOf(spFreqOther, etOtherIncome);

        // Main member's info needed for new members (Branch, Group, Address, MainMemberID)
        final clsMemberData mainMember =
                (mainActivity.Member.size() > 0) ? mainActivity.Member.get(0) : null;

        saveExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final String msg = doSave(items, toDeactivate, yearly, other, mainMember);
                final FragmentActivity act = getActivity();
                if (act == null) {
                    saving = false;
                    return;
                }
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        saving = false;
                        if (!isAdded()) return;
                        btClose.setEnabled(true);
                        btNext.setEnabled(true);
                        btSave.setText("Save");
                        baseline = signature();
                        updateSaveEnabled();

                        for (SaveItem it : items) {
                            if (it.holder == null) continue;

                            // If a new row was created in the DB it is now an "existing" row,
                            // so the next Save updates it instead of creating the member again.
                            if (it.isNewRow && it.createdMember) {
                                it.holder.member = it.member;
                                it.holder.info = it.info;

                                // Also add it to mainActivity's lists for other screens (Member and PersonalImpactInfo indexes must match)
                                mainActivity.Member.add(it.member);
                                clsMemberData tm = new clsMemberData();
                                it.member.Copy_Memberdata(tm);
                                mainActivity.tMember.add(tm);

                                mainActivity.PersonalImpactInfo.add(it.info);
                                clsPersonalImpactInfo ti = new clsPersonalImpactInfo();
                                it.info.Copy_PersonalImpactInfo(ti);
                                mainActivity.tPersonalImpactInfo.add(ti);

                            } else if (it.holder.info == null && it.info != null) {
                                it.holder.info = it.info;
                            }
                        }

                        // Deleted members that were deactivated in the DB are no longer pending
                        for (RowHolder d : toDeactivate) {
                            if (d.member != null && d.member.IsActive == 0) removedHolders.remove(d);
                        }

                        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show();
                        // do not close the page; it closes only when Close is pressed
                    }
                });
            }
        });
    }

    /** Copy of the UI values for use on the background thread. */
    private static class SaveItem {
        RowHolder holder;
        clsMemberData member;
        clsPersonalImpactInfo info;
        boolean isNewRow;
        boolean complete;           // all four fields filled
        boolean createdMember;      // the new member was created in the DB
        boolean isMainMember;
        String serial;
        String name;
        long relationId;
        long businessId;
        int income;
    }

    /** Background thread: DB work only. Returns the message to show. */
    private String doSave(ArrayList<SaveItem> items, ArrayList<RowHolder> toDeactivate,
                          int yearly, int other, clsMemberData mainMember) {

        int memberUpdated = 0;
        int incomeUpdated = 0;
        int memberCreated = 0;
        int memberRemoved = 0;
        boolean error = false;
        StringBuilder incomplete = new StringBuilder();

        // App rule: move the time forward by 1 second for every save, because MemberID is built from the time.
        // If the time stayed the same, two new members would get the same MemberID.
        long t = Math.max(mainActivity.LastTimeInMillis, System.currentTimeMillis());

        try {
            for (SaveItem it : items) {

                // ---------- New row: create a new member in the DB ----------
                if (it.isNewRow) {

                    if (!it.complete) {
                        if (incomplete.length() > 0) incomplete.append(", ");
                        incomplete.append(it.serial);
                        continue;
                    }
                    if (mainMember == null || mainMember.MemberID == 0) {
                        error = true;
                        continue;
                    }

                    // New member, same as MemberFragment.MemberInit()
                    clsMemberData m = new clsMemberData();
                    m.InitNewMember(mainActivity.strToday);
                    m.BranchID = mainMember.BranchID;
                    m.GroupID = mainMember.GroupID;
                    m.MainMemberID = mainMember.MemberID;
                    m.AddressID = mainMember.AddressID;
                    m.RelationID = it.relationId;
                    m.EncodedBy = mainActivity.UserID;
                    m.JoiningDate = mainActivity.strToday;
                    applyName(m, it.name);

                    t += 1000;
                    if (mainActivity.SQLite_saveMember(m, t, true)) {
                        it.member = m;
                        it.createdMember = true;
                        memberCreated++;
                    } else {
                        m.MemberID = 0;     // the next Save will insert it again
                        error = true;
                        continue;
                    }

                    clsPersonalImpactInfo ni = new clsPersonalImpactInfo();
                    ni.Init_PersonalImpactInfo();
                    ni.EncodedBy = mainActivity.UserID;
                    ni.MemberID = m.MemberID;
                    ni.MonthlyIncome = it.income;
                    ni.BusinessID = it.businessId;
                    it.info = ni;

                    t += 1000;
                    if (mainActivity.SQLite_savePersonalImpactInfo(ni, t, true)) {
                        incomeUpdated++;
                    } else {
                        error = true;   // member was created; the income will be saved on the next Save
                    }
                    continue;
                }

                // ---------- Existing member: update if something changed ----------
                long newRelationId = (it.relationId > 0) ? it.relationId : it.member.RelationID;

                boolean memberChanged = false;
                if (!it.name.isEmpty() && !it.name.equals(buildName(it.member))) {
                    applyName(it.member, it.name);
                    memberChanged = true;
                }
                if (newRelationId != it.member.RelationID) {
                    it.member.RelationID = newRelationId;
                    memberChanged = true;
                }
                if (memberChanged) {
                    t += 1000;
                    if (mainActivity.SQLite_saveMember(it.member, t, true)) {
                        memberUpdated++;
                    } else {
                        error = true;
                    }
                }

                clsPersonalImpactInfo info = it.info;
                boolean incomeChanged = false;
                if (info == null) {
                    info = new clsPersonalImpactInfo();
                    info.Init_PersonalImpactInfo();
                    info.EncodedBy = mainActivity.UserID;
                    info.MemberID = it.member.MemberID;
                    it.info = info;
                    incomeChanged = true;
                }
                if (info.MemberDetailsForImpactID == 0) {
                    incomeChanged = true;       // was never written to the DB before
                }
                if (info.MonthlyIncome != it.income) {
                    info.MonthlyIncome = it.income;
                    incomeChanged = true;
                }
                if (info.BusinessID != it.businessId) {
                    info.BusinessID = it.businessId;
                    incomeChanged = true;
                }

                // Main member (first row): B, C, D are also stored in his income record
                if (it.isMainMember) {
                    if (info.YearlyIncome != yearly) {
                        info.YearlyIncome = yearly;
                        incomeChanged = true;
                    }
                    if (info.OtherMonthlyIncome != other) {
                        info.OtherMonthlyIncome = other;
                        incomeChanged = true;
                    }
                }

                if (incomeChanged) {
                    t += 1000;
                    if (mainActivity.SQLite_savePersonalImpactInfo(info, t, true)) {
                        incomeUpdated++;
                    } else {
                        error = true;
                    }
                }
            }

            // ---------- Members deleted from the table: deactivate them in the DB ----------
            String today = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.ENGLISH)
                    .format(java.util.Calendar.getInstance().getTime());
            for (RowHolder d : toDeactivate) {
                clsMemberData m = d.member;
                if (m == null || m.IsActive == 0) continue;
                m.IsActive = 0;
                m.DeactivateDate = today;
                m.DeletionReasonID = 22;      // 22 = Other (same as MemberFragment)
                t += 1000;
                if (mainActivity.SQLite_saveMember(m, t, true)) {
                    memberRemoved++;
                } else {
                    m.IsActive = 1;           // not saved; will be tried again on the next Save
                    m.DeactivateDate = "NA";
                    m.DeletionReasonID = 0;
                    error = true;
                    continue;
                }
                if (d.info != null) {
                    d.info.IsActive = 0;
                    t += 1000;
                    if (!mainActivity.SQLite_savePersonalImpactInfo(d.info, t, true)) error = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = true;
        }

        mainActivity.LastTimeInMillis = t;

        String msg;
        if (error) {
            msg = "सेव्ह करताना अडचण आली.";
        } else if (memberCreated + memberUpdated + incomeUpdated + memberRemoved == 0 && incomplete.length() == 0) {
            msg = "काही बदल नाही.";
        } else {
            msg = "सेव्ह झाले.";
            if (memberCreated > 0) {
                msg += " नवीन सदस्य: " + memberCreated + ".";
            }
            if (memberRemoved > 0) {
                msg += " काढलेले सदस्य: " + memberRemoved + ".";
            }
        }
        if (incomplete.length() > 0) {
            msg += " ओळ " + incomplete + " अपूर्ण आहे (नाव, नाते, साधन, उत्पन्न चारही भरा), ती सेव्ह झाली नाही.";
        }
        return msg;
    }

    /** The first DB member row = the main member. */
    private RowHolder firstSavedHolder() {
        for (RowHolder h : holders) {
            if (h.member != null) return h;
        }
        return null;
    }

    private void fillEditableDefaults() {

        // Main member = PersonalImpactInfo.get(0), same as IncomeFragment
        clsPersonalImpactInfo first = null;
        if (mainActivity.PersonalImpactInfo != null
                && mainActivity.PersonalImpactInfo.size() > 0) {
            first = mainActivity.PersonalImpactInfo.get(0);
        }

        etAnnualFarm.setText((first != null && first.YearlyIncome > 0)
                ? String.valueOf(first.YearlyIncome) : "");

        // The app has a single "other income (rent etc.)" field (OtherMonthlyIncome),
        // so it is shown in D. C is left empty.
        etOtherIncome.setText((first != null && first.OtherMonthlyIncome > 0)
                ? String.valueOf(first.OtherMonthlyIncome) : "");

        etRent.setText("");
        spFreqRent.setSelection(FREQ_MONTHLY);
        spFreqOther.setSelection(FREQ_MONTHLY);   // DB value is a monthly amount
    }

    /**
     * A     = sum of the monthly income of all members
     * B, C, D = amount x frequency factor (monthly amount)
     * total = A + B + C + D (all monthly)
     */
    private void calculate() {
        if (etFamilyTotal == null || etPerCapita == null || spFreqRent == null || spFreqOther == null) return;

        // A) = sum of the monthly income of all table rows (updates immediately when edited)
        totalMonthly = 0;
        for (RowHolder h : holders) {
            int m = monthlyOf(h.spFreq, h.etIncome);
            totalMonthly += m;
            h.tvMonthly.setText(text(h.etIncome).isEmpty() ? "" : String.valueOf(m));
        }

        int effectiveMonthly = (int) Math.round(toInt(etAnnualFarm) / 12.0);   // B) yearly / 12
        int rentMonthly = monthlyOf(spFreqRent, etRent);
        int otherMonthly = monthlyOf(spFreqOther, etOtherIncome);
        int familyTotal = totalMonthly + effectiveMonthly + rentMonthly + otherMonthly;

        etTotalMonthly.setText(String.valueOf(totalMonthly));
        etEffectiveMonthly.setText(String.valueOf(effectiveMonthly));
        etRentMonthly.setText(String.valueOf(rentMonthly));
        etOtherMonthly.setText(String.valueOf(otherMonthly));
        etFamilyTotal.setText(String.valueOf(familyTotal));

        // Per capita income = family total / number of members listed in the table (same rule as the old IncomeFragment)
        int members = 0;
        for (RowHolder h : holders) {
            if (!isRowEmpty(h)) members++;
        }
        etPerCapita.setText(String.valueOf(members > 0 ? familyTotal / members : 0));
        updateSaveEnabled();
    }

    // Save is enabled only after something has changed
    private String baseline = null;

    private String signature() {
        StringBuilder sb = new StringBuilder();
        for (RowHolder h : holders) {
            if (isRowEmpty(h)) continue;
            sb.append(text(h.etName)).append('|')
                    .append(h.spRelation.getSelectedItemPosition()).append('|')
                    .append(h.spSource.getSelectedItemPosition()).append('|')
                    .append(h.spFreq.getSelectedItemPosition()).append('|')
                    .append(text(h.etIncome)).append(';');
        }
        sb.append(text(etAnnualFarm)).append('|')
                .append(text(etRent)).append(':').append(spFreqRent.getSelectedItemPosition()).append('|')
                .append(text(etOtherIncome)).append(':').append(spFreqOther.getSelectedItemPosition());
        return sb.toString();
    }

    private void updateSaveEnabled() {
        if (btSave == null || baseline == null || saving) return;
        btSave.setEnabled(!signature().equals(baseline));
    }

    // Small helpers
    private int toInt(EditText e) {
        String s = e.getText().toString().trim();
        if (s.isEmpty()) return 0;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    private String toMarathiDigits(int number) {
        String digits = "०१२३४५६७८९";
        StringBuilder sb = new StringBuilder();
        for (char ch : String.valueOf(number).toCharArray()) {
            sb.append(Character.isDigit(ch) ? digits.charAt(ch - '0') : ch);
        }
        return sb.toString();
    }

    public int getFamilyTotal() {
        return toInt(etFamilyTotal);
    }
}