package com.sample;

import com.sample.model.ConceptSetItem;
import edu.pitt.dbmi.ohdsiv5.db.*;
import org.apache.log4j.Logger;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.conf.RuleEngineOption;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DroolsTest {
    private static List<String> configOptions = new ArrayList<>(Arrays.asList("user", "password", "connectionURL", "schema", "ruleFolder", "sslmode"));
    final static Logger logger = Logger.getLogger(DroolsTest.class);

    @SuppressWarnings({"unchecked"})
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        String dateStr = args[0];
        if (dateStr == null) {
            logger.error("ERROR: Pass a date that will be used to extract data to run the rule engine in the format YYY-MM-DD");
            System.exit(1);
        } else {
            logger.info("INFO: Running rule engine with data from date: " + dateStr);
        }

        String rule_folder = "";
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("config.properties");
            prop.load(input);
            rule_folder = prop.getProperty("ruleFolder");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Map<String, String> userConfiguration = new HashMap<>();
        if (args.length > 1) {
            for (int i = 1; i < args.length; i++) {
                String argument = args[i];
                if (argument.contains("=")) {
                    String[] configuration = argument.split("=");
                    if (configOptions.contains(configuration[0])) {
                        logger.info("Using configuration: " + configuration[0] + " = " + configuration[1]);
                        userConfiguration.put(configuration[0], configuration[1]);
                    }
                } else {
                    logger.error("Pass a configuration in the format ruleFolder=rules_progress");
                    System.exit(1);
                }
            }
        }

        logger.info("Rule engine session open!");

        //////////////////////////////
        // Set up Drools KIE
        //////////////////////////////
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();

        KieBaseConfiguration kconfig = ks.newKieBaseConfiguration();
        // kconfig.setOption(RuleEngineOption.RETEOO);
        kconfig.setOption(RuleEngineOption.PHREAK);

        KieBase kbase;
        if (userConfiguration.containsKey("ruleFolder")) {
            kbase = kContainer.newKieBase(userConfiguration.get("ruleFolder"), kconfig);
        } else {
            kbase = kContainer.newKieBase("rules_progress", kconfig);
        }
        KieSession kSession = kbase.newKieSession();
//        KieRuntimeLogger kieLogger = ks.getLoggers().newFileLogger(kSession, "audit");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // calendar object still useful here for subtracting days
            kSession.setGlobal("currentDateStr", dateStr);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(sdf.parse(dateStr));
            Calendar cal3 = Calendar.getInstance();
            cal3.setTime(sdf.parse(dateStr));
            cal3.add(Calendar.DAY_OF_YEAR, -2);
            Calendar cal4 = Calendar.getInstance();
            cal4.setTime(sdf.parse(dateStr));
            cal4.add(Calendar.DAY_OF_YEAR, -28);
            Calendar cal5 = Calendar.getInstance();
            cal5.setTime(sdf.parse(dateStr));
            cal5.add(Calendar.DAY_OF_YEAR, 1);
            cal5.set(Calendar.HOUR, 0);
            cal5.set(Calendar.MINUTE, 0);
            cal5.set(Calendar.SECOND, 0); // midnight of the next day
            Calendar cal6 = Calendar.getInstance();
            cal6.setTime(sdf.parse(dateStr));
            cal6.add(Calendar.DAY_OF_YEAR, -7);
            kSession.setGlobal("currentDate", new Timestamp(cal2.getTimeInMillis()));
            kSession.setGlobal("within48hours", new Timestamp(cal3.getTimeInMillis()));
            kSession.setGlobal("within7days", new Timestamp(cal6.getTimeInMillis()));
            kSession.setGlobal("within28days", new Timestamp(cal4.getTimeInMillis()));
            kSession.setGlobal("plus1day", new Timestamp(cal5.getTimeInMillis()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ////////////////////////////////////////////////////////////////////////////
        // QUERY AND LOAD
        ////////////////////////////////////////////////////////////////////////////
        Class.forName("org.postgresql.Driver");
        String connectionURL, schema;
        if(userConfiguration.containsKey("connectionURL")) {
            connectionURL = userConfiguration.get("connectionURL");
        } else {
            connectionURL = prop.getProperty("connectionURL");
        }

        if (userConfiguration.containsKey("schema")) {
            schema = userConfiguration.get("schema");
        } else {
            schema = prop.getProperty("schema");
        }

        if(userConfiguration.containsKey("user")) {
            prop.setProperty("user", userConfiguration.get("user"));
        }

        if(userConfiguration.containsKey("password")) {
            prop.setProperty("password", userConfiguration.get("password"));
        }

        if(userConfiguration.containsKey("sslmode")) {
            prop.setProperty("sslmode", userConfiguration.get("sslmode"));
        }

        String url = connectionURL + "?currentSchema=" + schema;
        Connection conn = DriverManager.getConnection(url, prop);

        int cnt = 0; // fact counter - counts what is iterated, not necessarily what is finally in working memory

        logger.info("Gathering data...");
        Statement personSt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet personQuery = personSt.executeQuery(
                "SELECT "
                        + "person_id"
                        + ",gender_concept_id"
                        + ",year_of_birth"
                        + ",month_of_birth"
                        + ",day_of_birth"
                        + ",race_concept_id"
                        + ",ethnicity_concept_id"
                        + " FROM person"
        );
        personQuery.last(); // cursor to last row to get # of results
        logger.info("# of persons: " + personQuery.getRow());
        personQuery.beforeFirst(); // reset cursor so that iterators below will work.

        while (personQuery.next()) {
            Person p = new Person(personQuery.getLong("person_id"), personQuery.getInt("year_of_birth"), personQuery.getInt("gender_concept_id"), personQuery.getInt("race_concept_id"), personQuery.getInt("ethnicity_concept_id"));
            p.setMonthOfBirth(personQuery.getInt("month_of_birth"));
            p.setDayOfBirth(personQuery.getInt("day_of_birth"));
            p.setDateOfBirth();
            kSession.insert(p);
            cnt++;
        }

        Statement csSt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet csQuery = csSt.executeQuery(
                "SELECT"
                        + " concept_set_name"
                        + ",concept_id"
                        + " FROM ohdsi.concept_set cs"
                        + " INNER JOIN ohdsi.concept_set_item csi"
                        + " ON cs.concept_set_id = csi.concept_set_id"
        );
        csQuery.last();
        logger.info("# of conceptTpls: " + csQuery.getRow());
        csQuery.beforeFirst();
        while (csQuery.next()) {
            kSession.insert(new ConceptSetItem(csQuery.getString("concept_set_name"), csQuery.getInt("concept_id")));
            cnt++;
        }

        Statement voSt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet voQuery = voSt.executeQuery(
                "SELECT"
                        + " visit_occurrence_id"
                        + ",person_id"
                        + ",visit_start_date"
                        + ",visit_end_date"
                        + ",visit_concept_id"
                        + " FROM visit_occurrence"
                        + " WHERE visit_start_date <= (TO_DATE('" + dateStr + "','yyyy-MM-dd')) AND visit_end_date >= (TO_DATE('" + dateStr + "','yyyy-MM-dd'))"
        );
        voQuery.last();
        logger.info("# of visits: " + voQuery.getRow());
        voQuery.beforeFirst();
        while (voQuery.next()) {
            VisitOccurrence vo = new VisitOccurrence(
                    voQuery.getLong("visit_occurrence_id"),
                    voQuery.getLong("person_id"),
                    voQuery.getTimestamp("visit_start_date"),
                    voQuery.getTimestamp("visit_end_date"),
                    voQuery.getInt("visit_concept_id")
            );
            kSession.insert(vo);
            cnt++;
        }

        Statement measSt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet measQuery = measSt.executeQuery(
                "SELECT"
                        + " measurement_id"
                        + ",person_id"
                        + ",measurement_concept_id"
                        + ",measurement_date"
                        + ",measurement_datetime"
                        + ",measurement_time"
                        + ",measurement_type_concept_id"
                        + ",operator_concept_id"
                        + ",value_as_number"
                        + ",value_as_concept_id"
                        + ",unit_concept_id"
                        + ",range_low"
                        + ",range_high"
                        + ",provider_id"
                        + ",visit_occurrence_id"
                        + ",measurement_source_value"
                        + ",measurement_source_concept_id"
                        + ",unit_source_value"
                        + ",value_source_value"
                        + " FROM measurement"
                        + " WHERE measurement_datetime BETWEEN (TO_DATE('" + dateStr + "','yyyy-MM-dd') - INTERVAL '7 days') AND TO_DATE('" + dateStr + "','yyyy-MM-dd')" // recent measurements within 7 days may be considered that aren't necessarily on the same day.
        );
        measQuery.last();
        logger.info("# of meas: " + measQuery.getRow());
        measQuery.beforeFirst();
        while (measQuery.next()) {
            Measurement meas = new Measurement(
                    measQuery.getInt("measurement_id"),
                    measQuery.getInt("person_id"),
                    measQuery.getInt("measurement_concept_id"),
                    measQuery.getDate("measurement_date"),
                    measQuery.getTimestamp("measurement_datetime"),
                    measQuery.getString("measurement_time"),
                    measQuery.getInt("measurement_type_concept_id"),
                    measQuery.getInt("operator_concept_id"),
                    measQuery.getBigDecimal("value_as_number"),
                    measQuery.getInt("value_as_concept_id"),
                    measQuery.getInt("unit_concept_id"),
                    measQuery.getBigDecimal("range_low"),
                    measQuery.getBigDecimal("range_high"),
                    measQuery.getInt("provider_id"),
                    measQuery.getInt("visit_occurrence_id"),
                    measQuery.getString("measurement_source_value"),
                    measQuery.getInt("measurement_source_concept_id"),
                    measQuery.getString("unit_source_value"),
                    measQuery.getString("value_source_value")
            );
            kSession.insert(meas);
            cnt++;
        }

        Statement deraSt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet deraQuery = deraSt.executeQuery(
                "SELECT"
                        + " drug_era_id"
                        + ",to_char(drug_era_start_date, 'yyyy-MM-dd HH24:MI:SS') as drug_era_start_date"
                        + ",person_id"
                        + ",to_char(drug_era_end_date, 'yyyy-MM-dd HH24:MI:SS') as drug_era_end_date"
                        + ",drug_concept_id"
                        + ",drug_exposure_count"
                        + " FROM drug_era"
                        + " WHERE DRUG_ERA_START_DATE <= TO_DATE('" + dateStr + "','yyyy-MM-dd') AND DRUG_ERA_END_DATE >= (TO_DATE('" + dateStr + "','yyyy-MM-dd'))"
        );
        deraQuery.last();
        logger.info("# of deras: " + deraQuery.getRow());
        deraQuery.beforeFirst();
        while (deraQuery.next()) {
            Timestamp start = Timestamp.valueOf(deraQuery.getString("drug_era_start_date"));
            Timestamp end = Timestamp.valueOf(deraQuery.getString("drug_era_end_date"));
            kSession.insert(new DrugEra(deraQuery.getLong("drug_era_id"), start, deraQuery.getLong("person_id"), end, deraQuery.getInt("drug_concept_id"), deraQuery.getInt("drug_exposure_count")));
            cnt++;
        }

        Statement ceraSt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet ceraQuery = ceraSt.executeQuery(
                "SELECT"
                        + " condition_era_id"
                        + ",to_char(condition_era_start_date, 'yyyy-MM-dd HH24:MI:SS') as condition_era_start_date"
                        + ",to_char(condition_era_end_date, 'yyyy-MM-dd HH24:MI:SS') as condition_era_end_date"
                        + ",person_id"
                        + ",condition_concept_id"
                        + ",condition_occurrence_count"
                        + " FROM condition_era"
                        + " WHERE CONDITION_ERA_START_DATE <= TO_DATE('" + dateStr + "','yyyy-MM-dd')"
                // + " AND CONDITION_ERA_END_DATE >= (TO_DATE('" + dateStr + "','yyyy-MM-dd'))"
                // condition occurs at any time in the past.
        );
        ceraQuery.last();
        logger.info("# of ceras: " + ceraQuery.getRow());
        ceraQuery.beforeFirst();
        while (ceraQuery.next()) {
            Timestamp start = Timestamp.valueOf(ceraQuery.getString("condition_era_start_date"));
            Timestamp end = Timestamp.valueOf(ceraQuery.getString("condition_era_end_date"));
            kSession.insert(new ConditionEra(ceraQuery.getLong("condition_era_id"), start, end, ceraQuery.getLong("person_id"), ceraQuery.getInt("condition_concept_id"), ceraQuery.getInt("condition_occurrence_count")));
            cnt++;
        }

        Statement dexpSt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
	/* If datetime fields are not available use: 
	to_char(dexp.drug_exposure_start_date, 'yyyy-MM-dd HH24:MI:SS') as drug_exposure_start_date, to_char(dexp.drug_exposure_end_date, 'yyyy-MM-dd HH24:MI:SS') as drug_exposure_end_date
	Instead of drug_exposure_start_datetime, drug_exposure_end_datetime
	*/
        ResultSet dexpQuery = dexpSt.executeQuery(
                "SELECT"
                        + " dexp.drug_exposure_id, dexp.person_id, dexp.drug_concept_id, drug_exposure_start_datetime, drug_exposure_end_datetime, dexp.drug_type_concept_id, dexp.stop_reason, dexp.refills, dexp.quantity, dexp.days_supply, dexp.sig, sm.expected, sm.min, sm.max, dexp.route_concept_id, dexp.lot_number, dexp.provider_id, dexp.visit_occurrence_id, dexp.drug_source_value, dexp.drug_source_concept_id, dexp.route_source_value, dexp.dose_unit_source_value, dstr.ingredient_concept_id, dstr.amount_value, dstr.amount_unit_concept_id, dstr.numerator_value, dstr.numerator_unit_concept_id, dstr.denominator_value, dstr.denominator_unit_concept_id, cd.concept_name AS drug_name, ci.concept_name AS ingredient_name"
                        + " FROM drug_exposure dexp"
                        + " INNER JOIN drug_strength dstr ON dexp.drug_concept_id = dstr.drug_concept_id"
                        + " INNER JOIN concept cd ON dexp.drug_concept_id = cd.concept_id"
                        + " INNER JOIN concept ci ON dstr.ingredient_concept_id = ci.concept_id"
                        + " LEFT JOIN sig_mapping sm ON dexp.sig = sm.sig"
                        + " AND dexp.person_id IN"
                        + "(SELECT DISTINCT de.person_id FROM drug_era AS de WHERE drug_era_start_date <= TO_DATE('" + dateStr + "','yyyy-MM-dd') AND drug_era_end_date >= (TO_DATE('" + dateStr + "','yyyy-MM-dd')))"
        );
        dexpQuery.last();
        logger.info("# of dexps: " + dexpQuery.getRow());
        dexpQuery.beforeFirst();

        while (dexpQuery.next()) {
            ExtendedDrugExposure ex_dexp = new ExtendedDrugExposure(
                    dexpQuery.getLong("drug_exposure_id"), // drugExposureId
                    dexpQuery.getTimestamp("drug_exposure_start_datetime"), // Timestamp drugExposureStartDate
                    dexpQuery.getTimestamp("drug_exposure_end_datetime"), // Timestamp drugeExposureEndDate
                    dexpQuery.getLong("person_id"), // personId
                    dexpQuery.getInt("drug_concept_id"), // drugConceptId
                    dexpQuery.getInt("drug_type_concept_id"), // drugTypeConceptId
                    dexpQuery.getString("stop_reason"), // stopReason
                    dexpQuery.getShort("refills"), // refills
                    dexpQuery.getInt("quantity"), // drugQuantity
                    dexpQuery.getShort("days_supply"), // daysSupply
                    dexpQuery.getString("sig"), // sig
                    dexpQuery.getInt("expected"), // sigExpected
                    dexpQuery.getInt("min"), // sigMin
                    dexpQuery.getInt("max"), // sigMax
                    dexpQuery.getInt("route_concept_id"), // routeConceptId
                    dexpQuery.getString("lot_number"), // lotNumber
                    dexpQuery.getInt("provider_id"), // providerId
                    dexpQuery.getLong("visit_occurrence_id"), // visitOccurrenceId
                    dexpQuery.getString("drug_source_value"), // drugSourceValue
                    dexpQuery.getInt("drug_source_concept_id"), // drugSourceConceptId
                    dexpQuery.getString("route_source_value"), // routeSourceValue
                    dexpQuery.getString("dose_unit_source_value"), // doseUnitSourceValue
                    dexpQuery.getInt("ingredient_concept_id"), // ingredientConceptId
                    dexpQuery.getDouble("amount_value"), // amountValue
                    dexpQuery.getInt("amount_unit_concept_id"), // amountUnitConceptId
                    dexpQuery.getDouble("numerator_value"), // numeratorValue
                    dexpQuery.getInt("numerator_unit_concept_id"), // numeratorUnitConceptId
                    dexpQuery.getDouble("denominator_value"), // denominatorValue
                    dexpQuery.getInt("denominator_unit_concept_id"), // denominatorUnitConceptId
                    0.00, // dailyDosage - default value 0.00, set below
                    // dexpQuery.getInt("indication_concept_id"),  // indicationConceptId
                    dexpQuery.getString("drug_name"),
                    dexpQuery.getString("ingredient_name")
            );
            // NOTE: If you want to troubleshoot daily dosage calculations the commented-out print statements here may help debug.
            if (dexpQuery.getString("amount_value") != null && dexpQuery.getString("expected") != null) {
                ex_dexp.setSigDailyDosage(dexpQuery.getDouble("amount_value"), dexpQuery.getDouble("expected"));
                // System.out.println("\tPERSON=" + dexpQuery.getLong("person_id") + " DEXP DRUG " + ex_dexp.getDrugConceptName() + " -- ID=" + ex_dexp.getDrugConceptId() + " SET DAILY DOSAGE: sigDailyDosage " + ex_dexp.getDailyDosage() + " = amount " + dexpQuery.getDouble("amount_value") + " * sigExpected " + dexpQuery.getInt("expected") + "[sig='" + dexpQuery.getString("sig") + "']");
            } else if (dexpQuery.getString("numerator_value") != null && dexpQuery.getString("denominator_value") != null && dexpQuery.getString("expected") != null) {
                //in the database the numerator value is already proportionate based on the denominator value if the denominator exists.
                ex_dexp.setSigComplexDailyDosage(dexpQuery.getDouble("numerator_value"), dexpQuery.getDouble("expected"));
                // System.out.println("\tPERSON=" + dexpQuery.getLong("person_id") + " DEXP DRUG " + ex_dexp.getDrugConceptName() + " -- ID=" + ex_dexp.getDrugConceptId() + " SET DAILY DOSAGE: sigComplexDailyDosage " + ex_dexp.getDailyDosage() + " = numerator " + dexpQuery.getDouble("numerator_value") + " * sigExpected " + dexpQuery.getInt("expected") + "[sig='" + dexpQuery.getString("sig") + "']");
            } else if (dexpQuery.getString("quantity") != null && dexpQuery.getString("days_supply") != null && dexpQuery.getString("amount_value") != null) {
                ex_dexp.setRegDailyDosage(dexpQuery.getInt("quantity"), dexpQuery.getShort("days_supply"), dexpQuery.getDouble("amount_value"));
                // System.out.println("\tPERSON=" + dexpQuery.getLong("person_id") + " DEXP DRUG " + ex_dexp.getDrugConceptName() + " -- ID=" + ex_dexp.getDrugConceptId() + " SET DAILY DOSAGE: regDailyDosage " + ex_dexp.getDailyDosage() + " = quant " + dexpQuery.getInt("quantity") + " * amount " + dexpQuery.getDouble("amount_value") + " / daysSupply " + dexpQuery.getShort("days_supply"));

            } else if (dexpQuery.getString("quantity") != null && dexpQuery.getString("days_supply") != null && dexpQuery.getString("numerator_value") != null) {
                ex_dexp.setComplexDailyDosage(dexpQuery.getInt("quantity"), dexpQuery.getShort("days_supply"), dexpQuery.getDouble("numerator_value"));
                // System.out.println("\tPERSON=" + dexpQuery.getLong("person_id") + " DEXP DRUG " + ex_dexp.getDrugConceptName() + " -- ID=" + ex_dexp.getDrugConceptId() + " SET DAILY DOSAGE: complexDailyDosage" + ex_dexp.getDailyDosage() + " = quant " + dexpQuery.getInt("quantity") + " * numerator " + dexpQuery.getDouble("numerator_value") + " / daysSupply " + dexpQuery.getShort("days_supply"));
            } else {
                // System.out.println("\tPERSON=" + dexpQuery.getLong("person_id") + " DEXP DRUG " + ex_dexp.getDrugConceptName() + " -- ID=" + ex_dexp.getDrugConceptId() + " SET DAILY DOSAGE NOT SUCCESSFUL -- sig text=" + dexpQuery.getString("sig"));
                // ex_dexp.setNullDailyDosage(dexpQuery.getDouble("amount_value"));
            }
            kSession.insert(ex_dexp);
            cnt++;
        }

        ////////////////////////////////////////////////////////////////////////////
        // CREATE OBJECTS...
        ////////////////////////////////////////////////////////////////////////////

        System.out.println("Done gathering and loading data...");


        System.out.println("Firing rules for assessment...");
        int nrules = -1;
        try {
            nrules = kSession.fireAllRules();
        } catch (Throwable t) {
            System.out.println("Firing rules triggered an exception:");
            t.printStackTrace();
        }
        System.out.println("INFO: number of rules fired (-1 on error):" + nrules);

//        kieLogger.close();
        kSession.dispose();
        kSession.destroy();

        personQuery.close();
        personSt.close();
        csQuery.close();
        csSt.close();
        voQuery.close();
        voSt.close();
        measQuery.close();
        measSt.close();
        deraQuery.close();
        deraSt.close();
        ceraQuery.close();
        ceraSt.close();
        dexpQuery.close();
        dexpSt.close();

        System.out.println("INFO: Rule engine session closed!");

    }
}
