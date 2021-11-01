package com.ddi_cds_app.model;

public class Alternative {
    //CYP3A4
    public static final String DISCONTINUE_COLCHICINE = "Discontinue colchicine.";
    public static final String ITRACONAZOLE_KETOCONAZOLE_POSACONAZOLE = "The antifungal terbinafine does not appear to affect CYP3A4, and is unlikely to interact with colchicine. However, there are some clinical situations where terbinafine would not be a suitable substitute for an azole antifungal. Voriconazole is less likely to interact with colchicine than itraconazole, ketoconazole, or posaconazole. Fluconazole has been studied in healthy subjects, and found to produce a 40% increase in colchicine AUC (See the Mitigare Product Label <a href='https://dailymed.nlm.nih.gov/dailymed/drugInfo.cfm?setid=cb5f9d85-6b81-49f8-bcd6-17b7bfbc10f2#section-7.1' target='_blank'>Section 7</href> & <a href='https://dailymed.nlm.nih.gov/dailymed/drugInfo.cfm?setid=cb5f9d85-6b81-49f8-bcd6-17b7bfbc10f2#section-12.2' target='_blank'>Section 12</a>).";
    public static final String VORICONAZOLE = "No alternative recommended. A healthy subject study found no effect of voriconazole on colchicine pharmacokinetics. The possible explanation is that voriconazole might have little effect on the P-glycoprotein transporter. The antifungal terbinafine does not appear to affect CYP3A4, and is unlikely to interact with colchicine. However, there are some clinical situations where terbinafine would not be a suitable substitute for an azole antifungal.";
    public static final String CLARITHROMYCIN = "Consider alternative antibiotics to clarithromycin, erythromycin, quinupristin, and telithromycin that are not CYP3A4 inhibitors. If a macrolide antibiotic is required, azithromycin might be a less risky choice but it has been shown to reduce colchicine clearance. Evidence suggests that ciprofloxacin is a weak inhibitor of CYP3A4 but no P-glycoprotein and so might be a good choice if a quinolone is needed. Most other antibiotics are not known to inhibit both CYP3A4 and P-glycoprotein.";
    public static final String NEFAZODONE = "If an SSRI/SNRI is indicated, consider escitalopram, sertraline, mirtazapine, venlafaxine, desvenlafaxine, or buproprion. Avoid fluVoXamine since it is a modest CYP3A4 inhibitor. FluOXetine may be a weak CYP3A4 inhibitor, and probably doesn't have much effect on colchicine.";
    public static final String DILTIAZEM_VERAPAMIL = "Many other calcium channel blockers (CCG) other than diltiazem and verapamil have either a small or no effect on CYP3A4 and would be preferable depending on if clinical indication allows use of an alternate CCB.";
    public static final String DRONEDARONE = "Both dronedarone and amiodarone are inhibitors of CYP3A4/P-glycoprotein. No other antiarrhythmic is known to be a good inhibitor of both CYP3A4 and P-gp. Propafenone did not affect colchicine in healthy subject PK studies (See the Mitigare Product Label <a href='https://dailymed.nlm.nih.gov/dailymed/drugInfo.cfm?setid=cb5f9d85-6b81-49f8-bcd6-17b7bfbc10f2#section-7.1' target='_blank'>Section 7</href> & <a href='https://dailymed.nlm.nih.gov/dailymed/drugInfo.cfm?setid=cb5f9d85-6b81-49f8-bcd6-17b7bfbc10f2#section-12.2' target='_blank'>Section 12</a>). Ranolazine appears to be a weak inhibitor of CYP3A4 and P-gp, so less likely to interac. Quinidine is a potent P-gp inhibitor but it has not been studied for an interaction with colchicine. ";

    //CYP1A2
    public static final String CIPROFLOXACIN_ALTERNATIVE = "Other quinolone antibiotics such as ofloxacin, moxifloxacin, and levofloxacin and lomefloxacin do not appear to affect CYP1A2, and therefore do not interact with tizanidine.";
    public static final String FLUVOXAMINE_ALTERNATIVE = "Other SSRI/SNRIs agents such as sertraline, fluoxetine, paroxetine, venlafaxine, citalopram or escitalopram appear to have little effect on CYP1A2.";
    public static final String ZAFIRLUKAST_ALTERNATIVES = "Montelukast, another leukotriene receptor antagonist, does not interact with tizanidine because it has not been shown to be an inhibitor of the CYP1A2 pathway.";

    String drugName;
    String drugCode;
    String alternativeText;

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    public String getAlternativeText() {
        return alternativeText;
    }

    public void setAlternativeText(String alternativeText) {
        this.alternativeText = alternativeText;
    }

}
