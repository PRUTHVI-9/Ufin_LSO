App-Purna_LSO Revised version 1 dated 04-Sep-23, Database version 14
Changes from previous version ( App-Purna_LSO-release_07Aug23.apk, Database version 14 ):

Layout Changes:
?
1] Added HomeLoanAmount textview to the Punarbhet, NewGroup and Regroup Step-1 screens.
2] Added Othernominee address and pincode edittext to the Nominee screen.

Database Changes:
?
1] TD_PromotionLists: Added HomeLoanAmount field to the table. 
2] M_Loan_Product: Added ProductName field to the table.

Features/Logic Changes:
?
1] NewIntroFragment: Added code to move Deactive member to 'No Loan' group to avoid more than 5 members in 'active loan' group.
2] MainActivity: Added code to get Main Member Birthdate from server in case PIF can not be submitted due to Invalid Age.
3] MainActivity: Added code to upload Group and Member Status packets only after other PIF packets are uploaded to server.
4] MainActivity: Added code to ensure group having members with 0 NewLoanAmount are displayed on Step-1 screen for Regrouping.
5] RegroupFragment: Removed 'Hamipatra' document photo related code for member removed from group on Step-1 screen for Regrouping.
6] RegroupFragment, NewGroupFragment: Removed Loan Reason drop-down from Step-1 screen for Regrouping. 
7] RevisitFragment, RegroupFragment, NewGroupFragment: Added code to show 'HomeLoanAmount' available to member.
8] RegroupFragment: Set outgoing member RevisitStatus to 'Revisited' instead of 'Regrouped' to show member again on Punarbhet screen.  
9] MainActivity: Added code to receive database update from server only after all packets in queue are uploaded to server.
10] LoanFragment, RepayFragment, MainActivity: Code changes to show loan products based on selected loan reason - 'House/Plot Construction/Purchase' or Other.
11] RevisitFragment: Code changes to show remark: 'Contact D.M. for member mispayments' for members from pink, blue, white promotion lists.

Bug Fixes/Code Cleanup:
?
1] RevisitFragment: Added code to ensure ActualVisitDate is sent in UpdateRevisitDetails packet.
2] FamilyFragment, HeirFragment, MainActivity: Code changes related to LMFNominee and Othernominee information storage and packets.
3] NewIntroFragment: Added code for correct age calculation from date of birth.
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 07-Aug-23, Database version 14
Changes from previous version ( App-Purna_LSO-release_07Jul23.apk, Database version 13 ):

Layout Changes:
1] Added HomeLoanAmount textview to the Punarbhet, NewGroup and Regroup Step-1 screens.
2] Added Othernominee address and pincode edittext to the Nominee screen.

Database Changes:
1] TD_PromotionLists: Added HomeLoanAmount field to the table. 
2] M_Loan_Product: Added ProductName field to the table.

Features/Logic Changes:
1] NewIntroFragment: Added code to move Deactive member to 'No Loan' group to avoid more than 5 members in 'active loan' group.
2] MainActivity: Added code to get Main Member Birthdate from server in case PIF can not be submitted due to Invalid Age.
3] MainActivity: Added code to upload Group and Member Status packets only after other PIF packets are uploaded to server.
4] MainActivity: Added code to ensure group having members with 0 NewLoanAmount are displayed on Step-1 screen for Regrouping.
5] RegroupFragment: Removed 'Hamipatra' document photo related code for member removed from group on Step-1 screen for Regrouping.
6] RegroupFragment, NewGroupFragment: Removed Loan Reason drop-down from Step-1 screen for Regrouping. 
7] RevisitFragment, RegroupFragment, NewGroupFragment: Added code to show 'HomeLoanAmount' available to member.
8] RegroupFragment: Set outgoing member RevisitStatus to 'Revisited' instead of 'Regrouped' to show member again on Punarbhet screen.  
9] MainActivity: Added code to receive database update from server only after all packets in queue are uploaded to server.
10] LoanFragment, RepayFragment, MainActivity: Code changes to show loan products based on selected loan reason - 'House/Plot Construction/Purchase' or Other.
11] RevisitFragment: Code changes to show remark: 'Contact D.M. for member mispayments' for members from pink, blue, white promotion lists.

Bug Fixes/Code Cleanup:
1] RevisitFragment: Added code to ensure ActualVisitDate is sent in UpdateRevisitDetails packet.
2] FamilyFragment, HeirFragment, MainActivity: Code changes related to LMFNominee and Othernominee information storage and packets.
3] NewIntroFragment: Added code for correct age calculation from date of birth.
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 07-Jul-23, Database version 13
Changes from previous version ( App-Purna_LSO-release_25Jun23.apk, Database version 12 ):

Layout Changes:

Database Changes:
1] M_PunarbhetVisitDetailsMaster: Added column to decide entries to be shown for selection drop-down.
 

Features/Logic Changes:
1] CollectionFragment: Added/Modified notes printed on EMI and ADHP collection receipts.
2] RevisitFragment, RegroupFragment: Code to show visit details drop-down only if Newloan Amount > 0.

Bug Fixes/Code Cleanup:
1] HeirFragment: In case nominee is not a family member, Set OtherNominee record key (MemberID) to MainMember ID.
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 25-Jun-23, Database version 12
Changes from previous version ( App-Purna_LSO-release_22Jun23.apk, Database version 12 ):

Layout Changes:

Database Changes:

Features/Logic Changes:

Bug Fixes/Code Cleanup:
1] MainActivity: Modified code to check Age criteria for 'NewMember' and 'ExistingMember' while submitting PIF.
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 22-Jun-23, Database version 12
Changes from previous version ( App-Purna_LSO-release_21Jun23_2.apk, Database version 12 ):

Layout Changes:

Database Changes:

Features/Logic Changes:

Bug Fixes/Code Cleanup:
1] ReGroupFragments: Modified code to 'Reactivate' and update SQLite database for all 5 members of the group saved so that PIF can be opened. 
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 21-Jun-23, Database version 12
Changes from previous version ( App-Purna_LSO-release_13Jun23.apk, Database version 11 ):

Layout Changes:
1] activity_sync: Added TextView to show Secondary Server IPAddress.
2] activity_register: Added EditText to enter Secondary Server IPAddress.

Database Changes:
1] UserForTabMaster Table: Added column to store Secondary Server IPAddress.

Features/Logic Changes:
1] NewGroup and ReGroup Fragments: Modified code to 'Reactivate' all the 5 members of the group saved so that PIF can be opened. 
2] MainActivity: Modified code to store and use Primary and Secondary Server IPAddresses in UserForTabMaster Table.
3] MainActivity: Modified Server sync-up code for 'Get/Post' APIs to use Secondary Server IPAddress if not successful using Primary Server IPAddress.
4] NewIntroFragment: Added listtype 'NewMember' and 'DeActive' for respective members.
5] Revisit, NewGroup and ReGroup Fragments: Modified code to display member listtype in background color. 
6] NewIntroFragment: Added code to show GroupID of the member found in PromotionList to the Dialogbox before saving newly introduced member.
    
Bug Fixes/Code Cleanup:
1] MainActivity: Modified Guarantor screen validation to identify cases where Guarantor is Spouse/Other Family Member/Non-family person.
2] CollectionFragment: Modified code to initialize 'SMSResponse' parameter with "NA" instead of "?".
3] CollectionFragment: Modified code to save CollectionSMS packet records with String values for 'IsNewRecord' and 'IsReady' fields.
4] MainActivity: Added code to copy IsActive status in Member records to PersonalImpactInfo records.
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 13-Jun-23, Database version 11
Changes from previous version ( App-Purna_LSO-release_02Jun23.apk, Database version 11 ):

Layout Changes:

Database Changes:

Features/Logic Changes:
1] NewMemberIntroFragment: Modified code to allow re-introduce old member after confirming SavingACNo in database.
2] NewGroup and ReGroup Fragments: Removed Loan Cycle difference limit check when saving group.
3] NewGroup and ReGroup Fragments: Modified code to ignore Group Grade criteria and update GroupGrade value between "A" to "E".
4] MainActivity: Added Status value of 13 for 'Ready For GRT' Group and Member.
5] GroupFormFragment: Added TextView to show Grade of Selected Group.
6] NewGroup and ReGroup Fragments: Modified code to 'Reactivate' member added to group so that PIF can be opened. 
    
Bug Fixes/Code Cleanup:
1] MainActivity: Modified code to check if Target Disbursement date mentioned in Promotion list is crossed by 30 days. This is used to delete or retain document photos of Work In Progress members.
2] RepayFragment: Modified code to enable drop-down to select 'Reason for Co-Applicant other than spouse' for loan > 2,00,000/-.
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 02-Jun-23, Database version 11
Changes from previous version ( App-Purna_LSO-release_01Jun23.apk, Database version 11 ):

Layout Changes:

Database Changes:

Features/Logic Changes:
1] NewMemberIntroFragment: Added code to show Calendar when entering birthdate manually.
2] MainActivity: Reintroduced code to validate Main member age before submitting PIF.
  
Bug Fixes/Code Cleanup:
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 01-Jun-23, Database version 11
Changes from previous version ( App-Purna_LSO-release_11May23.apk, Database version 11 ):

Layout Changes:
1] fragment_collection: Changes to show Payable Amounts as on Scheduled date and Collection date.
2] fragment_newintro: Added layout to scan Aadhaarcard and enter essential information for new member.

Database Changes:

Features/Logic Changes:
1] CollectionFragment: Added code to show Payable Amounts as on Scheduled date and Collection date.
2] CollectionFragment: Added code to send to Server: Current and Past bifurcation of collected Interest, Principal, LateCharges, JLG.
3] CollectionFragment: Added code to send to Server: Information required to send collection SMS to member.
4] MainActivity, CollectionFragment: Added code to facilitate Additional EMI collection for members with zero current dues.
5] CollectionFragment: Added collection center name to center drop-down info.
6] MainActivity: Added code to delete all log files from previous cycle before new collection update.
7] MainActivity: Modified code to read revised Reasons and Relation master csv files.
  
Bug Fixes/Code Cleanup:
1] RegroupFragment, NewgroupFragment: Modified code to correctly display Member Visit Details.
2] CollectionFragment: Added code to initialize starting notes count, receipt count and receipt value to 0.
3] CollectionFragment: Added code to display previously selected RD status if collection member is reselected again.
4] RegroupFragment: Added code to initialize timestamp before saving group.
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 11-May-23, Database version 11
Changes from previous version ( App-Purna_LSO-release_28Apr23.apk, Database version 11 ):

Layout Changes:

Database Changes:

Features/Logic Changes:
  
Bug Fixes/Code Cleanup:
1] MainActivity, RevisitFragment: Modified code to correctly display Member Last Visit Details on Punarbhet screen.
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 28-Apr-23, Database version 11
Changes from previous version ( App-Purna_LSO-release_08Apr23.apk, Database version 10 ):

Layout Changes:

Database Changes:
Unused columns removed from database table definitions.
Unused tables removed from database definitions.

Features/Logic Changes:
1] CollectionFragment: Modified Extra Receipt number timestamp format to include milliseconds to ensure unique receipt numbers.
2] CollectionFragment: Added code to check if EMI information is available for selected member and disable Additional Collection if NOT.
3] CollectionFragment: Added code to disable naration editing when center collection amount is 0.
4] CollectionFragment: Added code to Roundup calculated Interest in case of earlier / late collection for Reducing Interest type.
  
Bug Fixes/Code Cleanup:
1] CollectionFragment: Added code refresh member due details to allow current / past / total RD dues collection.
2] Modified packet timestamp format to include milliseconds to ensure unique timestamp for all packets.
3] CollectionFragment: Modified code for Reducing interest, Additional Repay case - principal repay till Pending principal > 0, any excess amount will go to JLG.
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 08-Apr-23, Database version 10
Changes from previous version ( App-Purna_LSO-release_31Mar23.apk, Database version 9 ):

Layout Changes:

Database Changes:
1] TD_MEMBER_RECOVERY, TD_RD_Receipts: Removed unused columns from table definition.
2] TD_Loan_WO_RepaymentReceipt: Added table for Writeoff member collection data.
3] TD_MEMBER_RECOVERY: Replaced column LoanCycleNo with LoanInstallmentNo.

Features/Logic Changes:
1] MainActivity: Moved code related to Punarbhet, Newgroup, Regroup, Groupform, DocQuery, WIP to respective fragment .java files.  
2] MainActivity: Disable 'Collect' button if current month is not same as scheduled recovery month.  
3] MainActivity: Code changes to unconditionally generate all packets when PIF is submitted.
4] MainActivity: Code changes to delete only document photo PNG files and avoid deleting other member records after loan disbursement. 
5] CollectionFragment: On Save button data is saved to SQLite database and PDF receipts are generated. On Print button receipts are printed.
6] CollectionFragment: timestamp + LSOID based receipt numbers for 2nd collection onwards.
7] CollectionFragment: Added Receipt number and other changes to match App-Purna InsertCollectionData API.
8] CollectionFragment: Code changes for Onetime Closure Extra Collection.
9] CollectionFragment: Code changes to send FirstACHolder name and ID to server in the RD collection packet.
 
Bug Fixes/Code Cleanup:
1] CollectionFragment: Number of days calculation for reducing interest rate.
2] CollectionFragment: Added Pending Principal check in Additional EMI collection.
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 30-Mar-23, Database version 9
Changes from previous version ( App-Purna_LSO-release_09Mar23_2.apk, Database version 9 ):

Layout Changes:

Database Changes:

Features/Logic Changes:
1] MainActivity: Code changes to allow members and groups with list type 'pista' to form Grade B group.

Bug Fixes/Code Cleanup:
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 09-Mar-23, Database version 9
Changes from previous version ( App-Purna_LSO-release_08Feb23_2.apk, Database version 8 ):

Layout Changes:
1] fragment_member: Modified layout for Family Member display and editing. 
2] fragment_guarantor: Removed OtherGuarantor and ID photos display.
3] fragment_collection: Modified layout to allow entries to add break-up of 'Extra Collection'.
4] fragment_collection: Added 'Reprint' button to allow reprinting of collection receipts.
5] fragment_collection: Increased maximum number of RDs to 12 and removed 'More RDs' from Extra Collection. 
  
Database Changes:
1] TD_MEMBER_RECOVERY: Added columns required for Collection module changes to the table definition.
2] TD_RD_Receipts: Added column OperatedBY, InstallmentNo to the table definition.
3] TD_CollectionSummary: Added columns required to update total collection receipt count and value.

Features/Logic Changes:
1] AndroidManifest: Added permissions necessary for bluetooth printer interface.
2] CollectionFragment: Added code for generating PDF Receipt layout compatible with 3 Inch Thermal Printer for EMI,ADHP,RD,Extra Collection.
3] CollectionFragment: Added code for printing Collection Receipts on 3 Inch Thermal Printer using bluetooth interface.
4] CollectionFragment: Added code for further break-up of Extra collection amount.
5] CollectionFragment: Added code to improve editing of Additional Repay amount and Extra amounts.
6] CollectionFragment: Modified code to send MemberID and SubModuleID with Collection API.
7] CollectionFragment: Modified code to save Receipt files in '/data/receipt' folder.
8] CollectionFragment: Added code to delete all receipt files from previous cycle before new Collection schedule is updated. 
9] CollectionFragment: Added code to pause printing after receipts for one member are printed.
10] CollectionFragment: Added code for reprinting of collection receipts.
11] CollectionFragment: Added code for printing of 'Daily Collection Summary' receipts for LSO and BM.
12] MainActivity: Added code to request user to grant permissions necessary on new app installation.
13] MainActivity: Added code to generate separate .txt files for packets, response and debug logs.
14] MainActivity: Modified code to try uploading collection packets whenever user is on App-Purna desktop screen.
15] MemberFragment: Modified code for Family Member Add/Remove/Restore on PIF screen.

Bug Fixes/Code Cleanup:
1] GuarantorFragment: Removed code for OtherGuarantor and ID photos display.
2] RepayFragment: Removed code for OtherJointLoanee and ID photos display.
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 08-Feb-23, Database version 8
Changes from previous version ( App-Purna_LSO-release_10Jan23.apk, Database version 8 ):

Layout Changes:
1] fragment_collection: Added Member mobile number editbox.
 
Database Changes:

Features/Logic Changes:
1] MainActivity: Added code to exclude Punarbhet List Members with Expired Target Disbursement date from WIP. 
2] MainActivity: Changed validation limit for GRT to disbursement gap from 30 to 50 days. 
3] MainActivity: Modified Step-1 code to allow group member from Pink or Yellow list with Loan Cycle = 1.
4] MainActivity: Modified Step-1 code to disallow 'B' grade group with member from Pista list.  
5] CollectionFragment: Added code to edit Member mobile number on Collection screen and send to server, if updated.
6] MainActivity: Added code to call 'GET' APIs every 60sec instead of everytime.

Bug Fixes/Code Cleanup:
1] MainActivity: Removed code to Auto-ReSubmit Group and Member Info if server status is not "Submitted". 
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 10-Jan-23, Database version 8
Changes from previous version ( App-Purna_LSO-release_02Jan23.apk, Database version 8 ):

Layout Changes:
 
Database Changes:

Features/Logic Changes:
1] MainActivity: Added code to handle 'Post' APIs response including 'Internal Server Error code 500' and doubley quoted strings received from server.
2] MainActivity: Modified font size for 1st line of member information displayed on Punarbhet screen to accomodate list color, type text.

Bug Fixes/Code Cleanup:
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 02-Jan-23, Database version 8
Changes from previous version ( App-Purna_LSO-release_07Dec22.apk, Database version 7 ):

Layout Changes:
1] fragment_collection: Added screen layout for Collection module UI.
 
Database Changes:
1] Added tables to save Collection module data: TD_MEMBER_RECOVERY, TD_RD_Receipts, TD_CenterCollection, TD_CollectionSummary.

Features/Logic Changes:
1] CollectionFragment: Added code for Collection module.
2] MainActivity: Modified criteria for Grade-B group members from LoanCycle +/- 3 to +/- 8.


Bug Fixes/Code Cleanup:

//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 07-Dec-22, Database version 7
Changes from previous version ( App-Purna_LSO-release_06Oct22.apk, Database version 6 ):

Layout Changes:
1] activity_wip: Added drop-down for Reason to Remove group from Work-In-Progess.
2] activity_docquery: Added new screen layout to handle member PIF document photo queries.
3] activity_desktop: Added buttons to select newly designed Document Query screen, and Collection screen.
 
Database Changes:
1] Added columns ComMeetingDate, LoanDisbursementDate to SQLite table TD_PromotionLists.

Features/Logic Changes:
1] DocsFragment: Added code to validate Bank Passbook Document Number format.
2] MainActivity: Added code related to WIP Remove Request, Document Query
3] MainActivity: Modified code to allow removing group from WIP if GroupForm not submitted without approval. 
4] MainActivity: Added code to confirm before Submitting Member Query Documents.
5] MainActivity: Modified code to delete all records related to Group and it's Members whos status is 'Loan Disbursed'.
6] MainActivity: Added code to read ComMeetingDate, LoanDisbursementDate from .csv file to SQLite table TD_PromotionLists.
7] MainActivity: Added code to show ComMeetingDate for each member on Punarbhet screen.
8] MainActivity: Added code to Delete all records related to Group and it's Members when GroupStatus=5(GRT OK) and GroupLoanStatus=6(Loan Disbursed). Also added code to append Time, GroupID, MemberIDs and Names to 'LoanDisbursed' text file.
9] MainActivity: Added code to allow Member PIF edit and resubmit, in case 'Member PIF Query' is received.
10]MainActivity: Added code to parse database sync-up message to note 'Partial' or 'Full' update and use it to delete 'Not in WIP' records from TD_PromotionLists table at the time of update.

Bug Fixes/Code Cleanup:
1] MainActivity: Added code to check Group Grade before saving group on 'Punarbandhani Step1' screen.
2] MainActivity: Added code to check if 'Iter Natewaik Chitthi' document photo is taken in case of Other Guarantor.
3] MainActivity: Removed code to retain TD_PromotionLists records if Punarbhet Remark is entered.
4] MainActivity: Added code to save updated RevisitMemberStatus in case of Member PIF Resubmit Query.
5] MainActivity: Code change to read message inbox before first InitDesktop call to reflect correct Query Count. 
6] MainActivity: Code change to read Long New GroupID when member document query is submitted.
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 06-Oct-22, Database version 6
Changes from previous version ( App-Purna_LSO-release_03Oct22.apk, Database version 6 ):

Layout Changes:

Database Changes:

Features/Logic Changes:

Bug Fixes/Code Cleanup:
1] MainActivity: Code change to avoid app crash while adding member to group on 'Punarbandhani Step1' screen.
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 03-Oct-22, Database version 6
Changes from previous version ( App-Purna_LSO-release_23Sep22.apk, Database version 6 ):

Layout Changes:

Database Changes:

Features/Logic Changes:

Bug Fixes/Code Cleanup:
1] MainActivity: Code change to avoid validation check of Main Member Profession on PIF screen.
2] MainActivity: Code change to avoid deleting 'TD_PromotionLists' records if VisitDetailsID is non-zero at the time of data update.
3] MainActivity: Code change to avoid group member 'Add/Replace' if any group member has On-going Loan. 
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 23-Sep-22, Database version 6
Changes from previous version ( App-Purna_LSO-release_26Aug22.apk, Database version 5 ):

Layout Changes:
1] activity_desktop: Added soft button for 'NoLoan/New Gatbandhani' and rearranged soft buttons.
2] activity_newgroup: Added new screen for 'NoLoan/New Group' processing.

Database Changes:
1] Added 'LoanNotAllowedReasonId' field to 'M_MEMBER_FAMILY_DETAILS' table.
2] Added 'GradeOfGroup' field to 'TD_NewGroup_Info' table.

Features/Logic Changes:
1] FamilyFragment: Code change to disable Profession drop-down in case of Main Member to avoid change in PIF.
2] FamilyFragment: Code change to set HMF applicable by default for main member.
3] SubmitPIFFragment: Added code for function 'DeactivateDuplicateImpactAnswers' called when PIF is submitted.
4] MainActivity: Added Validation on Punarbandhani-Step1 to check if New Loan Amount is not 0 for loan applicant group member.
5] MainActivity: Added Validation on PIF Member screen to check for duplicate family member names.
6] MainActivity: Added code to display long temporary Center and Group IDs for New Group formed.
7] MainActivity: Added code to Add/Remove members from NoLoan groups to form a New Group with new center under selected cluster-community.
8] MainActivity: Added code to Validate and Save New group with New center under selected cluster-community.
9] MainActivity: Added code to validate and allow only 'A' and 'B' grade New Groups.
10] MainActivity: Added code to pass-on 'LoanNotAllowedReasonId' in MemberFamilyDetails to server.
11] MainActivity: Added code to validate and disallow certain Professions for Mainmember, Guarantor and Familymembers.

Bug Fixes/Code Cleanup:
1] HobbiesFragment: Code change to avoid repeated additions to TD_Member_Impact_Answers table in case of swipe-through the screen.
2] FamilyFragment: Code change to disable Guarantor radiobutton in case of deactivated family member to avoid change in PIF.
3] DocsFragment: Code change to set document number to '0' for document type 'Iter Natewaik Chitthi'.
4] MainActivity: Code to validate and allow regrouping only to form 'A' and 'B' grade groups. 
5] RepayFragment: Code change to handle 'LoanProductEndDate' entry from Loan Product Master table.
6] MainActivity: Code change to properly 'Remove' selected member on 'NoLoan-NewGroup' screen.
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 26-Aug-22, Database version 5
Changes from previous version ( App-Purna_LSO-release_05Aug22.apk ):

Layout Changes:
1] ServerSyncup: Increased font size of pending upload table entries.
2] ServerSyncup: Changed Text Color for NewGroupInfo and NewGroupMemberInfo tables to Red.

Database Changes:

Features/Logic Changes:
1] MainActivity: Update GRTOK Group Status in NewGroupInfo SQLite table.
2] MainActivity: Update GRTOK Member Status in NewGroupMemberInfo SQLite table.
3] MainActivity: Delete GRTOK member entries from Punarbhet List SQLite table.
4] MainActivity: Delete 'Not In WIP' entries from Punarbhet List SQLite table.
5] MainActivity: Document Validation Check for GuarantorID changed to Aadhaarcard/Pancard. 
6] FamilyFragment: Code changes to allow selection of Religion and Caste for Main member and family members.
7] SavingAccountFragment: Code changes to allow empty Bank_Mname and Bank_Lname.
8] MainActivity: Validation Check before Punarbandhani Step-2 'Submit' to disallow same date for Group Test and Loan Disbursement.
9] MainActivity: Validation Check to ensure both Bank_Mname and Bank_Lname are NOT empty and both are NOT "NA".
10] MainActivity: Added code to auto resubmit NewGroupInfo table if group status received from server does not match.
11] MainActivity: Validation Check to ensure member with LoanCycle=1 can not be added to another group on Punarbandhani Step1 screen.

Bug Fixes/Code Cleanup:
1] MainActivity: Code change in 'SQLite_getGroupMembersInfo' to avoid skipping 5th member details. 
2] MainActivity: Code change to avoid selecting 'Hamipatr' photo as Member Photo on Punarbandhani Step-2 screen. 
3] MainActivity: Code change to read SequenceID for M_Profession.csv file while copying monthly data upadate.
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 05-Aug-22, Database version 5
Changes from previous version ( App-Purna_LSO-release_04Aug22.apk ):

Layout Changes:
1] Changed Spinner Item size from 30sp to 26sp to free space for other items on screens.
 
Database Changes:

Features/Logic Changes:
1] Punarbandhani-Step1-'Outgoing Member Undertaking' photo for 5 Punarbhet Remarks instead of 4 Remark.
2] DocsFragment: Duplicate Document number Dialogbox message is avoided when Camera button is pressed.
3] MainActivity: Punarbandhani-Step1 - On Save Regroup - Code to identify group members not in Promotion List and change their GroupID to NoLoan Group of the center.

Bug Fixes/Code Cleanup:
1] MainActivity:Punarbandhani-Step2:On Submit Group-Code to update member Occupation in PersonalImpactInfo and Marital Status in M_Member tables.
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 04-Aug-22, Database version 5
Changes from previous version ( App-Purna_LSO-release_03Aug22.apk ):

Layout Changes:
 
Database Changes:

Features/Logic Changes:

Bug Fixes/Code Cleanup:
1] MainActivity: Code changes to avoid multiple ImageButtons for Punarbandhani-Step1-'Outgoing Member Undertaking' photo.
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 03-Aug-22, Database version 5
Changes from previous version ( App-Purna_LSO-release_02Aug22.apk ):

Layout Changes:
 
Database Changes:

Features/Logic Changes:
1] MainActivity: Punarbandhani-Step1-'Outgoing Member Undertaking' photo for 4 Punarbhet Remarks instead of only 1 Remark.
2] DocsFragment: Only Dialogbox message is shown in case of Duplicate Document number, allow to take photo and submit PIF.

Bug Fixes/Code Cleanup:
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 02-Aug-22, Database version 5
Changes from previous version ( App-Purna_LSO-release_28Jul22.apk ):

Layout Changes:
 
Database Changes:

Features/Logic Changes:

Bug Fixes/Code Cleanup:
1] MainActivity: Punarbandhani-Step1 - Add/Replace Member - Code changed to Ignore Type-B Group Criteria if joining member has same LastGroupID. 
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 28-Jul-22, Database version 5
Changes from previous version ( App-Purna_LSO-release_25Jul22.apk ):

Layout Changes:
 
Database Changes:

Features/Logic Changes:
1] MainActivity: Added code to capture and view 'Outgoing Member Undertaking' photo on Punarbandhani Step-1 screen.
2] MainActivity: Added code to check if Undertaking photo is captured before member is selected for replacement on Punarbandhani Step-1.
3] MainActivity: Added code to make member document entry and save Undertaking photo.
4] MainActivity: Added code to copy Undertaking photo and save it as Group document on Punarbandhani Step-1.
5] MainActivity: Added code to send Guarantor name as "Removed from WIP" to server for group members removed from WIP.  

Bug Fixes/Code Cleanup:

//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 25-Jul-22, Database version 5
Changes from previous version ( App-Purna_LSO-release_21Jul22.apk ):

Layout Changes:
 
Database Changes:
1] ufinDbContract: Added 'CheckDuplicate' column to the 'M_Member_Documents' master table.
2] ufinDbHelper: Added 'dbMigrateVersion4To5' to Upgrade database version from 4 to 5.

Features/Logic Changes:
1] DocsFragment: Added code to check duplicate document numbers if 'CheckDuplicate' is set for document type in Document master table. 
2] MainActivity: Added code to show 'Submission Date' for Group Form and PIF on Server Sync-up screen.
3] MainActivity: Modified code to valiadte Loan Cycle difference between newly added member and existing group members.
4] MainActivity: Added Pending Punarbhet Members / Total Punarbhet Members to Punarbhet button on Desktop.

Bug Fixes/Code Cleanup:
1] Removed files and code related to separate document master tables for Pune and Mumbai.
//////////////////////////////////////////////////////////////////////////////////////////
App-Purna_LSO Revised version 1 dated 21-Jul-22
Changes from previous version ( App-Purna_LSO-release_18Jul22.apk ):

Layout Changes:
 
Database Changes:

Features/Logic Changes:
1] MainActivity: Added code to save modified status after removal of Work-In-Progress Groups/Members.
2] MainActivity: Added code to avoid adding GRTOK groups to Work-In-Progress Groups/Members.
3] MainActivity: Added code to generate packet to send updated status after Work-In-Progress Groups/Members removal.

Bug Fixes/Code Cleanup:
1] MainActivity: Added code to Enable/Disable member entries as per Loan Applicant Yes/No status on Punarbandhani Step-2 screen.
//////////////////////////////////////////////////////////////////////////////////////////

App-Purna_LSO Revised version 1 dated 18-Jul-22
Changes from previous version ( App-Purna_LSO-release_08Jul22.apk ):

Layout Changes:
1] fragment_docs: Modified attributes to disallow small letters a-to-z in Document Number.
2] activity_wip: Added screen layout.
 
Database Changes:

Features/Logic Changes:
1] MainActivity: Added code to identify records corresponding to Work-In-Progress Groups/Members.
2] MainActivity: Added code to display, select and remove Work-In-Progress Groups/Members.
3] MainActivity: Added code to avoid updating records corresponding to Work-In-Progress Groups/Members.


Bug Fixes/Code Cleanup:
1] DocsFragment: Modified code to disallow image capture in case Document Number is Empty/Invalid/Duplicate.
//////////////////////////////////////////////////////////////////////////////////////////

App-Purna_LSO Revised version 1 dated 08-Jul-22
Changes from previous version ( App-Purna_LSO-release_07Jul22.apk ):

Layout Changes:

Database Changes:

Features/Logic Changes:

Bug Fixes/Code Cleanup:
1] MainActivity: Code to avoid adding members with DeathDate to the selected Group.
//////////////////////////////////////////////////////////////////////////////////////////

App-Purna_LSO Revised version 1 dated 07-Jul-22
Changes from previous version ( App-Purna_LSO-release_02Jul22.apk ):

Layout Changes:

Database Changes:

Features/Logic Changes:
1] MainActivity: Decreased msecServerSyncSleep from 5000 msec to 1000 msec.
2] MainActivity: Added interlocks to refresh screen only after syncing Group and Member Status with server.
3] MainActivity: To expedite sync-up, modified Server Sync-up round robin code to continue with next switch case when syncup list size is 0.

Bug Fixes/Code Cleanup:
1] MainActivity: Cleared list everytime before Sorting the Image files.
//////////////////////////////////////////////////////////////////////////////////////////

App-Purna_LSO Revised version 1 dated 02-Jul-22
Changes from previous version ( App-Purna_LSO-release_26Jun22.apk ):

Layout Changes:

Database Changes:

Features/Logic Changes:
1] MainActivity: Increased msecServerSyncSleep from 1000 msec to 5000 msec.
2] MainActivity: Added dialogbox to Select or Cancel Server Data Update.
3] MainActivity: Added code to use Android X compatible Actionbar.
4] MainActivity: Added code to bypass Group Criteria Check if Member with same Last GroupID added to Group.
5] MainActivity: Added code to resend NewGroupMemberInfo packet in case server status is "Not Submitted".
6] MainActivity: Allowed Group to reopen on Punarbandhani-Step1 in case of Group Query.
7] MainActivity: Added code to Sort the Image files in Ascending filesize to allow uploading smaller files first.

Bug Fixes/Code Cleanup:
1] Removed code realated to BM login in all PIF fragments.
2] MainActivity: Code to avoid adding Deactivated members to the selected Group.
//////////////////////////////////////////////////////////////////////////////////////////

App-Purna_LSO Revised version 1 dated 26-Jun-22
Changes from previous version ( App-Purna-release_01Jun22.apk ):

****Created separate Android Studio project and APK for LSO.****
 
Layout Changes:
1] Changes textsize for spinner dropdown from 32sp to 28sp.
2] Added fields to Punarbhet screen: MaturityDate, BalanceAmount, OverdueAmount.
3] Added fields to Punarbandhani-Step1 screen: For Ready For Loan Members from other groups: LastCenterID, LastGroupID.
4] Added to Syncup screen: Group and Member Status as received from Server.

Database Changes:
1] Added fields to TD_PromotionLists table: MaturityDate, BalanceAmount, OverdueAmount, LastCenterID, LastGroupID

Features/Logic Changes:
1] DocsFragment, MainActivity: Added code to verify image file size.
2] MainActivity: Code change to allow only Aadhaarcard as Age Proof for Main Member.
3] MainActivity: Code change to allow only Aadhaarcard/Pancard as Age Proof for Guarantor/Nominee.
4] Added fields to clsRevisitMember: MaturityDate, BalanceAmount, OverdueAmount, LastCenterID, LastGroupID.
5] Added constant definitions to clsRevisitMember for REVISIT_REMARK_IDs.
6] Added ACK states to confirm successful upload of NewGrtMemberInfo and NewGrtInfo to server.
7] Punarbandhani-Step1: Added preference for matching "LastGroupID" while preparing list of "Ready For Loan Members from other groups".
8] Punarbandhani-Step1: Added code to bypass Group Formation Rules for groups with matching "LastGroupID" members.
9] Added code to update Member and Group Status based on Member PIF or Group Query Messages.
10] Added format validation for Aadhaarcard, PANcard and Electioncard document numbers.
11] Added duplicate entry check for all document numbers.
****Following changes are Commented in this Release**** 
12] Punarbhet: Added validation to allow loan prepayment upto 180 days while selecting Punarbhet Remark. 
13] Punarbhet: Added validation to disallow "Ongoing Loan" remark for Maturity Date within 90 days.
 
Bug Fixes/Code Cleanup:
1] Removed code realated to unused Topup Loan feature.
2] Removed code related to Tab-Purna hardware features.
3] Added code at app restart to sort the pending upload packets in cronological order.
4] Code changes to show missing info on Punarbandhani-Step2 screen on Group Query: member photo, PIF datetime, GRT datetime, Loan disbursement date.
//////////////////////////////////////////////////////////////////////////////////////////

App-Purna Revised version 1 dated 17-Jun-22
Changes from previous version ( App-Purna-release_01Jun22.apk ):

Layout Changes:
1] Changes textsize for spinner dropdown from 32sp to 28sp.
2] Added fields to Punarbhet screen: MaturityDate, BalanceAmount, OverdueAmount.
3] Added fields to Punarbandhani-Step1 screen: For Ready For Loan Members from other groups: LastCenterID, LastGroupID.

Database Changes:
1] Added fields to TD_PromotionLists table: MaturityDate, BalanceAmount, OverdueAmount, LastCenterID, LastGroupID

Features/Logic Changes:
1] DocsFragment, MainActivity: Added code to verify image file size.
2] MainActivity: Code change to allow only Aadhaarcard as Age Proof for Main Member.
3] MainActivity: Code change to allow only Aadhaarcard/Pancard as Age Proof for Guarantor/Nominee.
4] Added fields to clsRevisitMember: MaturityDate, BalanceAmount, OverdueAmount, LastCenterID, LastGroupID.
5] Added constant definitions to clsRevisitMember for REVISIT_REMARK_IDs.
6] Added ACK states to confirm successful upload of NewGrtMemberInfo and NewGrtInfo to server.
7] Punarbhet: Added validation to allow loan prepayment upto 180 days while selecting Punarbhet Remark.
8] Punarbhet: Added validation to disallow "Ongoing Loan" remark for Maturity Date within 90 days.
9] Punarbandhani-Step1: Added preference for matching "LastGroupID" while preparing list of "Ready For Loan Members from other groups".
10] Punarbandhani-Step1: Added code to bypass Group Formation Rules for groups with matching "LastGroupID" members.
11] Added code to update Member and Group Status based on Member PIF or Group Query Messages.
 
Bug Fixes/Code Cleanup:
1] Removed code realated to unused Topup Loan feature.
2] Removed code related to Tab-Purna hardware features.
3] Added code at app restart to sort the pending upload packets in cronological order.
4] Code changes to show missing info on Punarbandhani-Step2 screen on Group Query: member photo, PIF datetime, GRT datetime, Loan disbursement date.
//////////////////////////////////////////////////////////////////////////////////////////

App-Purna Revised version 1 dated 01-Jun-22
Changes from previous version ( ufin_28Aug21 ):

Layout Changes:
1] Added new screens: App-Purna Desktop, Punarbhet, Punarbandhani-Step1, Punarbandhani-Step2

Database Changes:
1] Added new table: PunarbhetList

Features/Logic Changes:
1] Major Code changes in-line with the added screens to handle newly developed workflow.

Bug Fixes/Code Cleanup:

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna Revised version 1 dated 28-Aug-21
Changes from previous version ( ufin_13Aug21 ):

Layout Changes:

Database Changes:

Features/Logic Changes:
1] In MainActivity: Added code to check if first byte received in reply to Registration API is 1 to ignore empty response.
2] In MainActivity: Added code to replace empty MName field with "NA" to allow Registration with empty MName.

Bug Fixes/Code Cleanup:

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna Revised version 1 dated 13-Aug-21
Changes from previous version ( ufin_loan_12Aug21 ):

Layout Changes:

Database Changes:

Features/Logic Changes:

Bug Fixes/Code Cleanup:
1] Restored application folder name as 'ufin' instead of 'ufin_loan'.
2] Restored Android Studio project folder name as 'ufin1' instead of 'ufin_loan'.

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna Revised version 1 dated 12-Aug-21
Changes from previous version ( ufin_loan_09Aug21 ):

Layout Changes:

Database Changes:

Features/Logic Changes:

Bug Fixes/Code Cleanup:
1] In MainActivity: Modified string comparison to select 'Top up Reloan' product from master table.

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna Revised version 1 dated 09-Aug-21
Changes from previous version ( ufin_03Aug21 ):

Layout Changes:
1] Removed all xml files related to Vima-Purna.

Database Changes:
1] Removed all tables related to Vima-Purna.

Features/Logic Changes:
1] Removed all .java files related to Vima-Purna.

Bug Fixes/Code Cleanup:

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna Revised version 1 dated 03-Aug-21
Changes from previous version ( ufin_08Jul21 ):

Layout Changes:
1] Added xml files related to Topup Loan form edit and submit.
2] Added  xml files related to HMF Claim form edit and submit.

Database Changes:

Features/Logic Changes:
1] Added TopupLoanFragment: Code added to facilitate topuploan form edit and submit.
2] In clsNewGroupMemberInfo: Added 'EligibleLoan' and 'selectedPositionLoanProduct' to allow review of Topup loan form.
3] Added HMFClaimFragment:  Code added to facilitate HMF Claim form edit and submit.

Bug Fixes/Code Cleanup:

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna Revised version 1 dated 08-Jul-21
Changes from previous version ( ufin_11Jun21 ):

Layout Changes:
1] In grt_checklist.xml: Changed text displayed on button from "Save" to "Submit".

Database Changes:

Features/Logic Changes:
1] In MainActivity: Code added to delete old SQLite database master table before copying new data from CSV file for following: M_MEMBER_DOCUMENTS, M_RELIGION, M_MARITAL_STATUS, M_CASTE, M_RELATION, M_QUALIFICATION, M_HABBIT_ADDICTION, M_BANK, M_BankBranch, M_PROFESSION, M_HEALTH_PROBLEMS, M_REASONS, M_LOAN_PRODUCT, M_LOAN_PRODUCT_DETAILS, M_LOAN_UTILIZATION, M_OTHER_BORROWING_SOURCES, M_LOAN_STATUS, M_ORG_STRUCTURE
2] In MainActivity: Code added to facilitate BM to select from Latest Loan Product Details at the time of GRT.
3] In MainActivity: Code commented to disable topuploan form fragments as development is not yet completed.  

Bug Fixes/Code Cleanup:
1] In ufinDbHelper: Commented unused code for duplicate table 'UtilizationMaster'.
2] In ufinDbContract: Commented unused code for duplicate table 'UtilizationMaster'.

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna Revised version 1 dated 11-Jun-21
Changes from previous version ( ufin_18Mar21 ):

Layout Changes:
1] Added topuploan_info,fragment_topuploan,fragment_submit_topuploan layout files.
2] In activity_main.xml: Added button for topuploan form.
3] Added fragment_hmf layout file.
4] Added activity_main_se.xml layout file.
5] Added vertical_progress_bar.xml to 'drawable' folder.
6] In activity_sync.xml: Added Vertical Progressbar for WiFi RSSI and Server Responsetime.
7] In activity_sync.xml: Added data Upload and Download speed in KBPS.

Database Changes:
1] In M_Member_ForTab table: Added fields LoanProductDetailsID, LoanStartDate.
2] In TD_NewGroupMember_Info table: Added field GroupID.

Features/Logic Changes:
1] In RepayFragment,MemberFragment,GuarantorFragment,GroupCheckFragment: Code added to show dialog alert for 'Scan Request Cancelled'.
2] In GroupIdentityFragment,DocsFragment,MainActivity: Code added to show dialog alert for 'Camera Request Cancelled'.
3] In MainActivity: Code added to invoke topuploan form fragments.
4] In MainActivity: Code added to support Group Meeting screen for topuploan incase of BM Login.
5] In MainActivity: Code added to delete document photo files for 'GRT OK' groups and it's members at the time of monthly database sync-up.
6] In DocsFragment: Disable document number editbox for document types: house agreement/member/family member/guarantor/joint loanee photo.
7] In MainActivity: Code changes for BM login to facilitate Group Meeting for Topuploan.
8] In MainActivity: Added code to calculate data Upload and Download speed during 'GET' and 'POST' communication with server.
9] Added 'TopupLoanFragment' and 'SubmitTopupLoanFragment' .java files.
 
Bug Fixes/Code Cleanup:
1] In MainActivity: Code changes to initialize Othernominee Bank_FName, Bank_MName, Bank_LName to "NA" in case it is empty.
2] In MainActivity: Code changes to rename database sync-up report file for new device.
3] In RepayFragment: Code changes to recalculate FamilyTotalIncome, ExpenseTotal for forward/backward screen navigation.
4] In MainActivity: Added code to validate device date at application start-up and show alert dialog if required.
//////////////////////////////////////////////////////////////////////////////////////////

App-Purna Revised version 1 dated 18-Mar-21
Changes from previous version ( ufin_10Mar21 ):

Layout Changes:

Database Changes:

Features/Logic Changes:
1] In MainActivity: Code changes in all Post and Get methods to disconnect HttpURLConnection on Socket Timeout or any exception.
2] In MainActivity: Removed writing Post and Get parameters to HttpURLConnection OutputStream in addition to appending to API URL.
3] In MainActivity: Code changes to loop through the Post API call in case of success and break in case of Exception.
4] In MainActivity: Code changes to Always break loop for Document Upload Post API even in case of success or Exception.
 
Bug Fixes/Code Cleanup:
1] In MainActivity: Added Sync information update for Post Other Nominee Details API.

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna Revised version 1 dated 10-Mar-21
Changes from previous version ( ufin_04Mar21 ):

Layout Changes:
1] activity_register screen: Added screen for new device user registration.

Database Changes:

Features/Logic Changes:
1] In MainActivity: Code added for new device user registration.
2] In MainActivity: Code added to define global constants for HttpURLConnectionTimeout, ServerSyncSleep, GET APIs timestamp Overlap.
3] In MainActivity: Code added for Server APIs: GetUserFile, InsertMonthlyDataStatus, PostDebugInfoFile.
4] In MainActivity: Code added to delete csv database files and folder after successful SQLite update.
5] In MainActivity: Code to add App Version and timestamp to the Debug and Log files.

Bug Fixes/Code Cleanup:
1] In SubmitPIFFragment: Code changes to avoid hardcoding of Relation IDs for son, daughter and Profeesion ID for student.
 
//////////////////////////////////////////////////////////////////////////////////////////

App-Purna Revised version 1 dated 04-Mar-21
Changes from previous version ( ufin_10Feb21 ):

Layout Changes:
1] On fragment_savingaccount screen: Removed edittext for SB Account number.
2] On activity_sync screen: Changed SyncClose from ImageButton to Button with ">" text.

Database Changes:

Features/Logic Changes:
1] In SavingAccountFragment: Removed code related to edittext for SB Account number.
2] In MainActivity: Code to add timestamp to the Log file.

Bug Fixes/Code Cleanup:
1] In RepayFragment: Code changes to remove hardcoding for RELATION_ID_SPOUSE, RELATION_ID_MAIN_MEMBER.
2] In GuarantorFragment: Code changes to remove hardcoding for RELATION_ID_SPOUSE, RELATION_ID_MAIN_MEMBER.
3] In MemberFragment: Code changes to remove hardcoding for RELATION_ID_UNKNOWN, RELATION_ID_MAIN_MEMBER.
4] In FamilyFragment: Code changes to remove hardcoding for RELATION_ID_MAIN_MEMBER.
5] In ExpenseFragment: Code changes to set parameter value to 0 if it is edited to blank edittext using backspace.
6] In MainActivity: Code to initialize MemberAddress, FamilyImpactInfo, MemberFamilyDetails before getting Member Info.
 
//////////////////////////////////////////////////////////////////////////////////////////

App-Purna Revised version 1 dated 10-Feb-21
Changes from previous version ( ufin_06Feb21 ):

Layout Changes:
1] On main screen for LSO login: Removed textView for Battery Status indication.
2] On main screen for BM login: Removed textView for Battery Status indication.
 

Database Changes:

Features/Logic Changes:
1] In MainActivity: Removed functions to check and update textview for 'Battery Low' and 'Battery Charging' status.

Bug Fixes/Code Cleanup:
 
//////////////////////////////////////////////////////////////////////////////////////////

App-Purna Revised version 1 dated 06-Feb-21
Changes from previous version ( ufin_05Feb21 ):

Layout Changes:

Database Changes:

Features/Logic Changes:
1] In MainActivity: Code change to run hardware specific APIs for Torch, Printer and Battery Status based on screen layout size. This is to facilitate using same APK on 7inch tabs for testing purpose.
 
Bug Fixes/Code Cleanup:
1] In MainActivity: Code change to create new 'Personal Impact Info' for existing member if not found in database table.
 
//////////////////////////////////////////////////////////////////////////////////////////

App-Purna Revised version 1 dated 05-Feb-21
Changes from previous version ( ufin_03Feb21 ):

Layout Changes:

Database Changes:
1] Added ReferenceMemberDocumentID_1 field to M_PROFESSION table

Features/Logic Changes:
1] Added clsDocumentIDs,clsDocumentIDsOrgID1,clsDocumentIDsOrgID2 to select M_MEMBER_DOCUMENT DocumentIDs as per the user OrgID.
2] In MainActivity,DocumentFragment,FamilyFragment,GuarantorFragment,SubmitPIFFragment,RepayFragment,MemberFragment: Code change to refer M_MEMBER_DOCUMENT DocumentIDs as per the user OrgID.
3] In SubmitPIFFragment: Added code for newly added QualificationIDs in education score logic related to impact.
4] In MainActivity: Code change to refer ReferenceMemberDocumentID_1 field from M_PROFESSION table in PIF validation for 'Kamachi Chitthi' and 'Shopact' documents.
 
Bug Fixes/Code Cleanup:
 
//////////////////////////////////////////////////////////////////////////////////////////

App-Purna Revised version 1 dated 03-Feb-21
Changes from previous version ( ufin_23Jan21_Rev2 ):

Layout Changes:

Database Changes:

Features/Logic Changes:
1] In MainActivity: Uncommented hardware specific code for Torch On/Off, Battery Charging Check and Printer.
2] In MainActivity: Code change to adjust group photo preview orientation suitable for camera mount.

Bug Fixes/Code Cleanup:
1] In GroupCheckFragment, GuarantorFragment, RepayFragment, MemberFragment: Code change to derive Date Of Birth from Year Of Birth for Aadhaar not having DOB information.
 
//////////////////////////////////////////////////////////////////////////////////////////

App-Purna Revised version 1 dated 23-Jan-21
Changes from previous version ( ufin_23Jan21 ):

Layout Changes:

Database Changes:

Features/Logic Changes:

Bug Fixes/Code Cleanup:
1] In MainActivity: Code changes to ensure basic validations are done even for group member not applying for loan. This is to disallow group submission with less than 5 members.

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna updated version 1 dated 23-Jan-21
Changes from previous version ( ufin_14Jan21 ):

Layout Changes:
1] In activity_main: Added Textview for Battery Status.
2] In activity_main_bm: Added Textview for Battery Status.

Database Changes:

Features/Logic Changes:
1] In ufin app folder: Added jni subfolder with device.java file for code to access SBC hardware features.
2] In RepayFragment: Added code to switch on/off LED on camera module.
3] In MemberFragment: Added code to switch on/off LED on camera module.
4] In DocsFragment: Added code to switch on/off LED on camera module.
5] In GroupCheckFragment: Added code to switch on/off LED on camera module.
6] In GroupIdentityFragment: Added code to switch on/off LED on camera module.
7] In GroupIdentityFragment: Added code to flip image horizontally and/or vertically to correct mirror image captured by camera module.
8] In GuarantorFragment: Added code to switch on/off LED on camera module.
9] In MainActivity: Added code to switch on/off LED on camera module.
10] In MainActivity: Added code to check for Battery Status.
11] In MainActivity: Added code to test thermal printer.

Bug Fixes/Code Cleanup:
1] In SubmitPIFFragment: Added code to apply validations for active family members only.
2] In RepayFragment: Added code to clear loan product details in case monthly surplus is changed.
3] In GroupCheckFragment: Code changes to parse Aadhaarcard birthdate format.
4] In MainActivity: Code changes to remove validation of Group criteria on Regroup screen.
5] In MainActivity: Code changes to avoid validation of deactivated family members.

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna updated version 1 dated 14-Jan-21
Changes from previous version ( ufin_18Dec20 ):

Layout Changes:
1] In grt_checklist: Replaced Loan Amount and Duration EditText objects with LoanProductDetails spinner objects.
2] In activity_sync: Added Logout button.
3] In activity_main: Removed GUI objects related to Sync-up with Server.
4] In activity_main_bm: Removed GUI objects related to Sync-up with Server.

Database Changes:
1] Added Monthly_Surplus column in table TD_MemberAdditionalDetails.

Features/Logic Changes:
1] In MainActivity: Code changes to facilitate BM to modify LoanProductDetails on Group Test screen.
2] In clsNewGroup: Added LoanProductMasterID ArrayList object to facilitate BM to modify LoanProductDetails on Group Test screen.
3] In clsGroupMember: Added objects required to facilitate BM to modify LoanProductDetails on Group Test screen.
4] In RepayFragment: Code changes to save/update Monthly_Surplus in database table TD_MemberAdditionalDetails.
5] In MainActivity: Added code for background timer task to allow auto-sync with server when app is inactive.

Bug Fixes/Code Cleanup:
1] In DocsFragment: Added code to allow document photo only after selecting family member and document type.
2] In MainActivity: Added code to save Renamed memberphoto filename.
3] In MainActivity: Added code to shift focus away from fragment screen object to allow saving changes.

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna updated version 1 dated 18-Dec-20
Changes from previous version ( ufin_26Nov20 ):

Layout Changes:
1] In group_info: Changed ViewPager type to Disable swipe action for Group Form screens.
2] In personal_info: Changed ViewPager type to Disable swipe action for PIF screens.
3] In grt_checklist: Added title for 'Group Photo'.
4] In fragment_repay: Increased width for Monthly Income, Expense and Surplus fields.
5] In fragment_hobbies: Removed Other Hobbies Details field.
6] In fragment_group_identity: Added title for 'Member Photo'.
7] In activity_main: Removed soft-key for EMI Collection.
 
Database Changes:
1] Removed tables for schedule, loan_repayment, RD and ADHP.
2] Added OrgID column in table M_USER_ForTab to support branchwise database at server.

Features/Logic Changes:
1] In AndroidManifest: Added Intents to make App-Purna Default Home application.
2] In MainActivity: Removed code for Collection fragments.
3] In MainActivity: Added code for OrgID parameter to support branchwise database at server.
4] In MainActivity: Code changes to disable ViewPager swipe action for Group Form and PIF screens.
5] In MainActivity: Code changes for additional validations on Group Form and PIF Submit.

Bug Fixes/Code Cleanup:
1] In OtherLoanFragment: Disable editing for BM Login.
2] In HobbiesFragment: Removed Other Hobbies Details editing.
3] In MainActivity: Added code to check if Group is 'Ready For GRT'.

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna updated version 1 dated 26-11-2020
Changes from previous version ( ufin_041120 ):

Layout Changes:
1] Added new screens fragment_schedule, fragment_attendance, fragment_collection, fragment_receipt.
 
Database Changes:
1] Added new tables for schedule, loan_repayment, RD and ADHP.

Features/Logic Changes:
1] In MainActivity: Added initialization logic for Collection fragments.
2] Added new code for Collection fragments: schedule, attendance, collection, receipt.

Bug Fixes/Code Cleanup:
1] In MainActivity: Code changes to validate Member Photo file on Group Form Submit.

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna updated version 1 dated 04-11-2020
Changes from previous version ( ufin_271020 ):

Layout Changes:

Database Changes:

Features/Logic Changes:
1] In MainActivity: For Regroup screen - Added Dialog Alert whenever selected action to Add/Replace member violates group criteria.

Bug Fixes/Code Cleanup:
1] In all PIF...Fragments: Added check for currentScreen in all editbox focus change listeners to avoid wrong entry due to screen swipe.
2] In RepayFragment: Code changes to display last saved Loan Product Details.
3] In MainActivity: Code changes to form Member Photo update packets on Group Form Submit.

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna updated version 1 dated 27-10-2020
Changes from previous version ( ufin_061020 ):

Layout Changes:
1] On group_check screen: Changes to allow 3 editboxes for Member Firstname, Middlename and Lastname.

Database Changes:
1] In AdditionalDetails table: Changed OtherJointLoaneeFName, OtherJointLoaneeMName, OtherJointLoaneeLName definition to match latest API.

Features/Logic Changes:
1] In RepayFragment: Changes to handle Aadhaarcard scan returning multi-word Name field for OtherJointLoanee.
2] In RepayFragment: Changes to use 'M_REASONS' master to select reason for 'Other Joint Loanee'.
3] In GuarantorFragment: Changes to handle Aadhaarcard scan returning multi-word Name field for Other Guarantor.
4] In GuarantorFragment: Changes to use 'M_REASONS' master to select reason for 'Other Guarantor'.
5] In MemberFragment: Changes to handle Aadhaarcard scan returning multi-word Name field for Family Members.
6] In GroupIdentityFragment: Changes to show Member name using Firstname, Middlename and Lastname.
7] In GroupCheckFragment: Changes to show and edit Member name using Firstname, Middlename and Lastname.
8] In MainActivity: Changes to show Member name using Firstname, Middlename and Lastname.
9] In FamilyFragment: Changes to 'Allow changing guarantor on PIF.
10] In Strings.xml: Changed App-Version format to DDMMMYY.
 
Bug Fixes/Code Cleanup:
1] In MainActivity: Code to communicate successful downloading of database and app update files to server.
2] In MainActivity: Code to handle saving member photos on Group Form Save and uploading on Group Form Submit.
3] In MainActivity: Code changes for proper logging of packet URLs and Response from server to debuginfo file.

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna updated version 1 dated 06-10-2020
Changes from previous version ( ufin_011020 ):

Layout Changes:

Database Changes:
1] In TD_Member_Personal_Impact_Info table: Added 'OtherMonthlyIncome' field as integer.
 
Features/Logic Changes:
1] In clsPersonalImpactInfo: Added 'OtherMonthlyIncome' field as integer.
2] In clsFamilyImpactInfo: Removed 'FamilyOtherIncome' integer field.
3] In IncomeFragment: Code changes related to 'OtherMonthlyIncome' field in clsPersonalImpactInfo.
4] In MainActivity: Code changes related to 'OtherMonthlyIncome' field - in csv file read, Post and Get APIs.

Bug Fixes/Code Cleanup:
1] In MainActivity: Code changes to verify Loan Disbursement Date only for group members applying for Loan.
2] In MainActivity: Code to enable SyncDB flag on receiving 'database sync-up' message from server - MessageType = 'S'.

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna updated version 1 dated 01-10-2020
Changes from previous version ( ufin_260920 ):

Layout Changes:
1] On grt_checklist screen: Rearranged Member Information fields to allow more space for Guarantor Name and Relation.

Database Changes:

Features/Logic Changes:
1] In FamilyFragment: Code added to 'Disallow changing guarantor on PIF for Married Member if Guarantor is selected as Spouse on Group Form'.
2] In SavingAccountFragment: Code changes to show Bank and Bank Branch names in English instead of Marathi. 
3] In MainActivity: Code to enable SyncDB flag on receiving 'database sync-up' message from server.

Bug Fixes/Code Cleanup:
1] In MainActivity: Code changes to show group member information on 'Group Test' screen.

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna updated version 1 dated 26-09-2020
Changes from previous version ( ufin_140920 ):

Layout Changes:

Database Changes:

Features/Logic Changes:
1] In MainActivity: Code added for database sync-up with server - SyncDB flag disabled in this version.
2] In MainActivity: Code added for latest app version download from server and update - SyncDB flag disabled in this version.
3] In strings.xml: App version numbering style changed to integer to help in app version auto-updates. 
 
Bug Fixes/Code Cleanup:
1] In RepayFragment: Bug fixed while converting string to integer for 'Principal Amount'.
2] In clsNewGroupInfo: Initialized GroupTest, PIF date and time to valid date and time strings.
3] In clsNewGroup: Initialized LoanDisbursement, LoanRepayInstallment, GroupTest, PIF date and time to valid date and time strings.
4] In MainActivity: Code added to insert 'NA' instead of empty first/middle/last name only while forming packets for server.

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna updated version 1.0 dated 14-09-2020
Changes from previous version ( ufin_040920 ):

Layout Changes:
1] On fragment_repay screen: Increased 'Monthly Surplus' maximum width to 7 digits. (worksheet row no. 116)
2] On fragment_family screen: By default disabled Fname, Mname, Lname, Age fields. (worksheet row no. 100)

Database Changes:
1] In SQLiteDatabase: Added LoanRulesMaster table.

Features/Logic Changes:
 
Bug Fixes/Code Cleanup:
1] In MainActivity: Code added to change group status to 'Edit_GF' when 'End Loan' group form is saved.
2] In SubmitPIFFragment: Code added to initialize RationCard details to default values if RationCard document photo is not taken. (worksheet row no. 109)
3] In all GF and PIF fragments (SavingAccount, Repay, OtherLoans, Docs, Family, GroupCheck, Guarantor, Heir, Loan): Bypassed edittext and spinner updates on all screen if it is not 'CurrentScreen'. (worksheet row no. 113, 114)
4] In FamilyFragment: Allowed Occupation selection while filling PIF. (worksheet row no. 113, 114)
5] In FamilyFragment: Disabled Fname, Mname, Lname, Age fields for main member. (worksheet row no. 100)
6] In MainActivity: Code added to insert 'NA' instead of empty middle name only while forming packets for server.
7] In MainActivity: Code change to fix the bug related to 'Loan Disbursement' date on Group Form. (worksheet row no. 111)
8] In MainActivity: Code change to update only newGroupInfo.GroupStatus when saving Regroup changes (To avoid empty date, time fields).

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna updated version 1.0 dated 04-09-2020
Changes from previous version ( ufin_190820 ):

Layout Changes:
1] On activity_main_bm screen: Disabled buttons for 'Group Test' and 'View PIF' by default.
 
Database Changes:
1] M_USER_ForTab Table: Added ServerIP, ServerPort and DefaultTimestamp fields.
2] ufin_config.txt: Removed from ufin/config folder.
3] TD_MemberAdditionalDetails Table: Renamed fields related to Other Guarantor to match latest Web API definition. Also removed unused fields.

Features/Logic Changes:
1] In DocsFragment: Added validation check for Aadhaarcard number.
2] In GroupCheckFragment: To improve user interface, initialize 'LoanApplicant' = 0 when member is added from other group.
3] In GuarantorFragment: Renamed fields related to Other Guarantor to match latest Web API definition.
4] In RepayFragment: Added code to restrict 'Loan Amount' for 'New Loan' members.
5] In MainActivity: Added code to read ServerIP, ServerPort and DefaultTimestamp fields from M_USER_ForTab table.
6] In MainActivity For BM login: Enable 'GroupTest' button only if 'MemberStatus' is 'PIF_OK' for all Loan Applicants.
7] In MainActivity on 'Regroup' screen: Enable 'Add' and 'Replace' buttons for selection improving group criteria count.
8] In MainActivity: For Loan-InProcess Group -> Fetch Data from NewGroupMemberInfo and NewGroupInfo tables.
9] In MainActivity: For Loan-Ongoing and Loan-Ended Groups -> Fetch Data from Last Month's database.
10] In MainActivity: Added flag to all SQLite_savexxxx methods to generate packets to upload only when group form and PIF are submitted.
11] In MainActivity: Modified code to write packet to 'logfile' if uploaded successfully otherwise write packet to 'debugfile'.
12] In MainActivity: Added 'Loan Rule Reference Number' to the on-screen validation messages.
13] In MainActivity: Added code to initialize Other Guarantor fields to default values, in case Guarantor is family member.
14] In MainActivity: Updated 'ProfessionIDs' in validation for shopact document.
15] In MainActivity: Updated 'ProfessionIDs' in validation for 'Kaamaachi Chitthi' document.
16] In MainActivity: Updated 'DocumentIDs' in validation for 'Own House' document.
   
Bug Fixes/Code Cleanup:
1] In MainActivity: Validate 'Rationcard color' only if member has Rationcard or it's compulsory as per Loan Rules.
2] In clsGroupMember: Removed 'MemberLoanStatus' and 'MemberPIFStatus' variables to avoid confusion with 'MemberStatus'.
3] In clsAdditionalDetails: Renamed fields related to Other Guarantor to match latest Web API definition. Also removed unused fields.

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna updated version 1.0 dated 19-08-2020
Changes from previous version ( ufin_180820 ):

Layout Changes:
1] On activity_main_bm screen: Changes to properly display all group member names.
 
Database Changes:
1] MessagesInbox Table: Added MessagesFilter field to MessagesInbox table.

Features/Logic Changes:
1] In MainActivity: Modified code to handle MessagesFilter field in MessagesInbox table.
2] In MainActivity for activity_main_bm screen: Button 'ViewPIF' enabled only for Member applying for loan.
3] In MainActivity for activity_main_bm screen: Button 'GroupTest' enabled only if 'MemberStatus=PIF OK' for all Members applying for loan.

Bug Fixes:
1] In MainActivity for GetNewGroupMemberInfo: SQLiteDatabase read, write handles closed finally after finishing all read, write operations.
2] In MainActivity for GetNewGroupInfo: SQLiteDatabase read, write handles closed finally after finishing all read, write operations.

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna updated version 1.0 dated 18-08-2020
Changes from previous version ( ufin_120820 ):

Layout Changes:
1] On group_identity screen: Added planned 'Loan Disbursement Date' for the Group.
 
Database Changes:
1] M_Reasons.csv: Deleted Marathi Description for ReasonIDs not having entries in M_Loan_Utilization.csv 

Features/Logic Changes:
1] In GroupIdentityFragment: Added code to enter planned 'Loan Disbursement Date' for the Group.
2] In RepayFragment: Added code to populate available 'Loan Product Details' based on planned 'Loan Disbursement Date' for the Group.
3] In MainActivity for ValidateScreenGroupIdentity: Added code to validate 'Loan Disbursement Date' for the Group.
4] In MainActivity for grt_checklist screen: Added code to Verify if selected LoanProduct is available for the modified LoanDisbursementDate.
5] In MainActivity for ValidateScreenDocuments: Commented code to validate 'Other Loan Repay Document'.
6] In MainActivity for ValidateScreenMember: Added code to validate family member name is entered.
7] In LoanFragment: Modified code to skip M_Reason entry without Marathi Description.
 
Bug Fixes:
1] In SubmitGroupFragment: Updated MemberStatus to 'PIF_EDIT' for members applying for loan.
2] In MainActivity for ValidateScreenGroupCheck: Member Newloan/Reloan status is checked using 'MemberDataForTab.LoanEndDate'.

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna updated version 1.0 dated 12-08-2020
Changes from previous version ( ufin_310720 ):

Layout Changes:
1] For BM Login on grt_checklist screen: Added TextViews to show if Group Member is Loan Applicant. 
2] On group_identity screen: 'Marital Status' and 'Own/Rented House' re-aligned to allow more space for 'Member Name' and 'Profession'.
3] On family screen: Used Button widgets with Text labels instead of using ImageButton widgets to navigate different family members info.  
4] On Other Loans screen: Added scrollview to help data entry with soft keypad.
 
Database Changes:
1] In ufin_config.txt file: Added DefaultTimestamp to make it configurable.
2] In SQLite table definitions: Removed NewGroupMemberInfoID, NewGroupInfoID, AdditionalDetailsID fields from respective tables.

Features/Logic Changes:
1] In MainActivity for grt_checklist screen: Loan Amount and Loan Duration editable only if Group Member is Loan Applicant.
2] In MainActivity for grt_checklist screen: Validation applied only if Group Member is Loan Applicant. 
3] In MainActivity: To initialize DefaultTimestamp from config file.
4] In MainActivity: Skip showing remaining EMIs for empty Group Member entries.
5] In MainActivity: Added code to generate Log file for uploaded packets alongwith return values.
6] In MainActivity: Ignore 'EducationExpense' validation while submitting PIF.
7] In class definitions: Removed NewGroupMemberInfoID, NewGroupInfoID, AdditionalDetailsID objects from classes.
8] In MainActivity: Removed code references related to NewGroupMemberInfoID, NewGroupInfoID, AdditionalDetailsID objects.
9] In class LMFNominee: Changed default initialization for 'NomineeIsFamilyMember' from 1 to 0.
   
Bug Fixes:
1] In MainActivity: Changes for BM login to avoid unintended overwritting of group status and member status while syncing with server.
2] In MainActivity: Changes for BM login to avoid duplicate record Inserts to GroupMemberInfo, GroupInfo tables while syncing with server.
3] In clsNewGroup: Added 'LoanApplicant' field in class object Compare and Copy methods.
4] In GroupIdentityFragment: Added Group Member Name to 'Collection Place' drop-down master only if Member is Saved to database (MemberID > 0)
5] In FamilyFragment: Code changes to select and deselect LMFNominee correctly from family members.
6] In DocsFragment: Code changes to fix problem related to de-activated family members.
7] In MainActivity: Added code to read 'Loan Applicant' value every time Member Info is read.
8] In MainActivity: Code changes to avoid group entry in multiple Group Status drop-downs.

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna updated version 1.0 dated 31-07-2020
Changes from previous version ( ufin_170720 ):

Layout Changes:
1] On LSO Main screen: Simplified Group Selection drop-down options to: "In-Process", "On-Going Loan", "Loan Ended".
2] On LSO Main screen: Added InProcessGroupStatus drop-down options: "Group Form Edit", "PIF Edit", "Ready for GRT", "GRT OK"
3] On LSO Main screen: Removed TextView for selected group status.
4] On ReGroup screen: Added Button to Remove Group Member.
  
Database Changes:

Features/Logic Changes:
1] In MainActivity: Code changes to categorise Groups as per latest drop-down options.
2] In MainActivity: Replaced spinner adaptor code with function calls for all spinners on LSO and BM Main screens.
3] In MainActivity: Code changes to effect changes mentioned in other points. 

Bug Fixes:
1] In MainActivity: Code changes to update GroupInfo using GroupID and GroupMemberInfo using MemberID to avoid duplicate entries.
2] In MainActivity: Code changes for LSO login to avoid unintended overwritting of group status and member status while syncing with server.
//////////////////////////////////////////////////////////////////////////////////////////

App-Purna updated version 1.0 dated 17-07-2020
Changes from previous version ( ufin_030720 ):

Layout Changes:
1] On LSO Main screen: Group Status drop-down option "Upcoming maturity/Mix Group" merged with "On-going Loan Group"
2] On LSO Main screen: Number of pending EMIs added in row showing Member Name.  
3] In Group Form: New screen "Group Submit" added at the end, related UI objects moved from "Group Identity" screen to "Group Submit" screen.
4] In Group Form Footer: Buttons added for "Jump to first screen" and "Jump to last screen".
5] On Group Check screen: UI added to select member as Loan Applicant.
6] On Group Check screen: UI added to show and edit member's birthdate.
7] In PIF: New screen "PIF Submit" added at the end, related UI objects moved from "Hobbies" screen to "PIF Submit" screen.
8] In PIF Footer: Buttons added for "Jump to first screen" and "Jump to last screen".
9] In PIF: Documents screen moved to screen before last screen ("PIF Submit").
  
Database Changes:
1] In "TD_NewGroupMember_Info" table: Added column "LoanApplicant" as integer type.

Features/Logic Changes:
1] "int LoanApplicant" and to classes clsGroupMember, clsNewGroupMemberInfo and clsNoLoanGroupMember.
2] "int RemainingLoanMonths" added to class clsGroupMember.
3] "MEMBER_STATUS_LOAN_NEAR_ENDING" removed from class clsGroupMember.
4] SubmitGroupFragment added to Validate Group before Submit.
5] In GroupIdentityFragment: Moved code related to Group Validation and Submit to "SubmitGroupFragment".
6] SubmitPIFFragment added to Validate PIF before Submit.
7] In HobbiesFragment: Moved code related to PIF Validation and Submit to "SubmitPIFFragment".
8] In GroupCheckFragment: Added code for show/edit "Loan Applicant" and birthdate information. Enabled/Disabled Other fields based on "Loan Applicant" selection.
9] In RepayFragment: Changed Available Loan Principal-Duration-EMI list sorting as per Descending Principal Amount.
10] In MainActivity: Added Problem Lists and Validation Logic for every Group Form and PIF screen.
11] In MainActivity: Code changes to merge "Upcoming maturity/Mix Groups" with "On-going Loan Groups".
12] In MainActivity: Code changes to effect changes mentioned in other points. 

Bug Fixes:
1] Please refer worksheet updated on 17-07-2020 
//////////////////////////////////////////////////////////////////////////////////////////

App-Purna updated version 1.0 dated 03-07-2020
Changes from previous version ( ufin_160620 ):

Layout Changes:
1] On Other Loans screen: Loan Serial Numbers are Left Aligned and square brackets added.

Database Changes:

Features/Logic Changes:

Bug Fixes:
1] In Document fragment: In case Rationcard is Deactivated from Member Documents table, reset Member Name and Address on Rationcard from Member data and Family Details tables.
2] In Family fragment: On deselecting Nominee - Reset LMFnominee.NomineeIsFamilyMember, LMFnominee.MemberID and LMFnominee.NomineeID. This is to allow Nominee Other than family member.
3] In Heir fragment: Disable Nominee Guardian details for BM Login.
4] In Bank Account fragment: Disable Bank Account Number editing. It is editable for LSO login only from Document screen.
5] In Repay fragment: Conditional update of AdditionalDetails.JointLoaneeID depending on flag 'JointAccountHolderRequired'. This is to avoid  wrong value incase loan combination is changed multiple times.
6] In Member fragment: Update MemberIsActive Checkbox when Member screen is visible on swipe screen.    

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna updated version 1.0 dated 16-06-2020
Changes from previous version ( ufin_150620 ):

Layout Changes:

Database Changes:

Features/Logic Changes:

Bug Fixes:
1] On Document fragment: Refresh FamilyMember Dropdown list only if any Family Member is Added/Removed/Restored on Member Screen;

//////////////////////////////////////////////////////////////////////////////////////////

App-Purna updated version 1.0 dated 15-06-2020
Changes from previous version ( ufin_280520 ):

Layout Changes:
1] Soft keys added to navigate to Previous screen and Next screen on Group Form;
2] Soft keys added to navigate to Previous screen and Next screen on PIF;
3] On PIF-Member screen:
    - "ADD" key is removed,
    - "+" key is renamed as "ADD",
    - "Restore" key is added;
4] On main screen for LSO login: "Regroup" and "Group Form" key positions are interchanged;  

Database Changes:
1] M_Member_ForTab table definition:
    - LoanID field added;
    - LoanStatusID field added;
2] TD_LoanGuarantorForTab table added;

Features/Logic Changes:
1] On Hobbies fragment: Added Validation check on PIF Submit for - Nominee details entries;
2] On Member fragment: Added "Restore" to reactivate Main and Family Members;
3] On Member fragment: Merged functionality to save new member to Adding new member;
4] On Login: Do not kill application on wrong login password;
5] For Group Form and PIF: Soft keys alternative to swipe action for Previous screen, Next screen navigation;
6] On Group Form: Changed functionality from "Save and Exit" to "Save and Continue";
7] On Group Form: For Reloan members - Show Guarantor Name and Relation for last loan;
8] On Group Test: Modified validation remarks to Marathi messages;
9] On Group Test: Added validation checks for Member PIF OK and Group Form OK;

Bug Fixes:
1] On Document fragment: Force FamilyMemberMaster update everytime on RefreshScreen() to reflect changes to family members on Member screen;
2] On GroupCheck fragment: On Aadhaarcard scan update respective Aadhaarcard number EditText;
3] On PIF - Bank Account Details made Read-Only for BM login;
4] On Group Form and PIF save: update records only incase of change in values;
5] On Group Form save without submit: copy member status for reloan member to avoid treating reloan member as new loan member;
6] Impact Parameters: Removed Encoded Date comparison to select latest record. 

